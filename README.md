# LED-Workshop
An introductory Java programming workshop using a strip of Light-Emitting Diodes (LEDs).

## Overview
This workshop uses Java to program an individually-addressible Red-Green-Blue (RGB) LED strip consisting of eight LEDs. The strip is wired to an Arduino microcontroller, which connects to a computer over USB. The software to communicate with the Arduino has already been written -- the goal here is to create different light patterns that achieve a particular effect, including
1. Static patterns
   * All LEDs on, single color
   * Rainbow colors
   * Alternating colors (odd vs. even)
   * Fade along strip: dim to bright
2. Time-varying patterns
   * Flashing colors
   * Wipe a color across the strip
   * Bouncing a single color from end to end
   * Pulsing between dim and bright
3. Your own imagination


## Hardware Setup

### Materials
* [Arduino Nano Every](https://store-usa.arduino.cc/products/nano-every?srsltid=AfmBOooJA36aqjCzknBGI81Jtcyiwb-t4eUoDF8LCNA4oYXuugBOLtRx) microcontroller
* [LED Array](https://www.amazon.com/dp/B0BWH95XSH?ref=ppx_yo2ov_dt_b_fed_asin_title) (8x WS2812B 5050 RGB LED Stick)
* [Jumper cable](https://www.amazon.com/dp/B0BKZRGF6N?ref=ppx_yo2ov_dt_b_fed_asin_title) (3x wires needed)
* [USB Cable](https://www.amazon.com/dp/B095JZSHXQ?ref=ppx_yo2ov_dt_b_fed_asin_title&th=1) (USB A to Micro USB)
* (optional) [Arduino Nano Case](https://www.amazon.com/dp/B0916HNSXQ?ref=ppx_yo2ov_dt_b_fed_asin_title)

### Wiring
The LED array must be connected to the Arduino with three wires:
| Arduino | LED Strip | Description | 
| ------- | --------- |------------ |
| 5V      | +5V       | Power       | 
| GND     | GND       | Ground      |
| D3      | DI        | Data In     |

The arduino can power the LEDs from a standard USB connection to a computer. No additional power supply is needed.

### Firmware
The Arduino Nano Every requires firmware to process commands sent by the computer over USB and power each LED appropriately. It should be flashed using the official [Arduino IDE](https://www.arduino.cc/en/software/) with the [MegaAVR Boards Core]( https://docs.arduino.cc/software/ide-v1/tutorials/getting-started/cores/arduino-megaavr/). The sketch to upload is located in this repository at `src/Arduino/PixelDriver/PixelDriver.ino`.

## Software Setup
### Cloning this repository
To work with this code base, you will need to install [git](https://git-scm.com/downloads/win).
Once git is installed, enter the following commands in a terminal such as PowerShell to clone the repository and enter its directory:
```
git clone https://github.com/efouad/LED-Workshop.git
cd LED-Workshop
```

### Dependencies
To develop and build this software, you will only need the [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/#jdk24-windows) version >= 17.0. The build depends on [jSerialComm v2.11.2](https://github.com/Fazecast/jSerialComm/releases) and [Gradle 9.0.0](https://gradle.org/releases/), but these will be downloaded automatically.

### Hello World
Create the file `src/main/java/ledcontrol/LEDAnimator.java` if it doesn't exist and fill it with the following contents. This creates a LEDController object called "controller" and uses the "sendRGB" method to turn on a single LED and set it to a specific color.
```
package ledcontrol;
public class LEDAnimator {
    public static void main(String[] args) {
        LEDController controller = new LEDController();
        controller.sendRGB(0, 255, 0, 0);  // Turns the first LED red
    }
}
```

### Build and Run
To build and run simply run the gradlew file from Powershell:
```
.\gradlew run
```