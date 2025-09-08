// PixelDriver.ino
//
// Simple sketch to talk to an addressable RGB LED strip and pass on commands
// sent over serial.
//
// Uses Adafruit NeoPixel library, released under the GPLv3 license.

#include <Adafruit_NeoPixel.h>

#define LED_DATA_PIN 3  // Which pin on the Arduino is connected to the LEDs

#define STATUS_PIN 13  // Yellow status LED pin on the Arduino

#define NUMPIXELS 8  // How many LEDs are in the strip

// When setting up the NeoPixel library, we tell it how many pixels,
// and which pin to use to send signals. Note that for older NeoPixel
// strips you might need to change the third parameter -- see the
// strandtest example for more information on possible values.
Adafruit_NeoPixel pixels(NUMPIXELS, LED_DATA_PIN, NEO_GRB + NEO_KHZ800);

// A message describes the color to set a single LED.
// The serial buffer consists of seven bytes:
//   (0) Message header (0XAA)
//   (1) LED index number to set
//   (2) Red value, 0-255
//   (3) Green value, 0-255
//   (4) Blue value, 0-255
//   (5) Checksum to verify message integrity: ((1) + (2) + (3) + (4)) mod 256
//   (6) Message footer (0x55)
uint8_t buffer[7];

/** 
 * Runs one time when Arduino powers on.
 * Initialize serial and LED connecions.
 */
void setup() {
    Serial.begin(9600);  // Open Serial interface
    
    // Output high on the status pin to turn on the yellow LED on the Arduino.
    // This indicates that the Arudino has been flashed with this firmware.
    pinMode(STATUS_PIN, OUTPUT);
    digitalWrite(STATUS_PIN, HIGH);

    pixels.begin();  // Initialize LED strip object
    pixels.clear(); // Set all pixel colors to 'off'
}


/** 
 * Runs continuously following setup.
 * Read the seven byte message from the Serial buffer and set the LED of the
 * requested index to the requested color.
 */
void loop() {
    // Wait for serial data (at least seven bytes in the buffer)
    if (Serial.available() >= 7) {
        Serial.readBytes(buffer, 7);

        // Make sure header and footer is intact, i.e. parsing is correct
        if (buffer[0] == 0xAA && buffer[6] == 0x55) {
            uint8_t checksum = (
                buffer[1] + buffer[2] + buffer[3] + buffer[4]
            ) % 256;
            // Make sure checksum is correct, i.e. data was transcribed without errors
            if (checksum == buffer[5]) {
                pixels.setPixelColor(
                    buffer[1],
                    pixels.Color(buffer[2], buffer[3], buffer[4])
                );  // Sets the requested index (1) to the requested RGB color (2,3,4)
            }
        }
    }

    pixels.show();  // Send the updated pixel colors to the hardware.
}