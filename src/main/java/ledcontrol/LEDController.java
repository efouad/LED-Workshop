package ledcontrol;

import java.awt.Color;
import com.fazecast.jSerialComm.SerialPort;

/**
 * LEDController manages serial communication with an Arduino-based LED strip.
 *
 * It constructs and sends RGB packets using a simple protocol with header,
 * footer, and checksum validation. It also auto-detects the Arduino port.
 */
public class LEDController {

    private SerialPort comPort;

    /**
     * Constructs a new LEDController with a default baud rate and attempts to
     * connect to an Arduino device over serial.
     */
    public LEDController() {
        this.connectToArduino(9600);
    }

    /**
     * Sends an RGB color command to a specific LED index.
     *
     * Constructs a 7-byte packet with header, index, RGB values, checksum,
     * and footer, then transmits it over the serial interface.
     *
     * @param index the LED index to update
     * @param red   the red component (0–255)
     * @param green the green component (0–255)
     * @param blue  the blue component (0–255)
     */
    public void sendRGB(int index, int red, int green, int blue) {
        byte[] packet = new byte[7];
        packet[0] = (byte) 0xAA;  // Header
        packet[1] = (byte) index;  // LED number
        packet[2] = (byte) red;  // red value
        packet[3] = (byte) green;  // green value
        packet[4] = (byte) blue;  // blue value
        packet[5] = (byte) ((index + red + green + blue) % 256);  // Checksum
        packet[6] = (byte) 0x55;  // Footer
        comPort.writeBytes(packet, packet.length);
    }
    
    /**
     * Sends an RGB color command using java.awt.Color to a specific LED index.
     *
     * @param index the LED index to update
     * @param color the java.awt.Color object representing the RGB components
     */
    public void sendColor(int index, Color color) {
        this.sendRGB(index, color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Attempts to connect to an Arduino device by scanning available serial
     * ports and selecting one whose description contains "arduino".
     *
     * @param baudRate the baud rate for serial communcation with the Arduino
     */
    public void connectToArduino(int baudRate) {
        // Get all available serial ports
        SerialPort[] ports = SerialPort.getCommPorts();

        // If no ports are found, exit early
        if (ports.length == 0) {
            System.out.println("No serial ports found.");
            return;
        }

        // Try to find a port that looks like it's an Arduino
        for (SerialPort port : ports) {
            String desc = port.getDescriptivePortName().toLowerCase();
            String info = port.getPortDescription().toLowerCase();

            // Check if either descriptor mentions "arduino"
            if (desc.contains("arduino") || info.contains("arduino")) {
                comPort = port;
                break;
            }
        }

        // If no Arduino-like port was found, exit
        if (comPort == null) {
            System.out.println("No Arduino Nano Every found.");
            return;
        }

        // Set baud rate and try to open the port
        comPort.setBaudRate(baudRate);
        if (comPort.openPort()) {
            System.out.println("Connected to " + comPort.getSystemPortName());
        } else {
            System.out.println("Failed to open Arduino port.");
        }

        // Assign comPort field to the newly opened port.
        this.comPort = comPort;
        
        // Wait 1 second to ensure Arduino has finished starting up
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted during Arduino startup.");
        }
    }
}
