/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkBench;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class EchoServer {

//Initialize Port number and Packet Size
    static final int serverPort = 1026;
    static final int packetSize = 1024;

    public static void main(String args[])
            throws SocketException {

        DatagramPacket packet;
        DatagramSocket socket;
        byte[] data;    // For data to be         Sent in packets
        int clientPort;
        InetAddress address = null;
        String str;

        socket = new DatagramSocket(serverPort);

        for (;;) {
            data = new byte[packetSize];

            // Create packets to receive the message

            packet = new DatagramPacket(data, packetSize);
            System.out.println("Waiting to receive the packets ");

            try {

                // wait infinetely for arrive of the packet

                socket.receive(packet);

            } catch (IOException ie) {
                System.out.println(" Could not Receive  :" + ie.getMessage());
                System.exit(0);
            }

            // get data about client in order to echo data back address = packet.getAddress();
            clientPort = packet.getPort();

            // print string that was received 
            //on server 's console
            str = new String(data, 0, 0, packet.getLength());
            System.out.println("Message  :" + str.trim());
            System.out.println("From   :" + address);

            // echo data back to the client 
            // Create packets to send to the client

            packet = new DatagramPacket(data, packetSize,
                    address, clientPort);

            try {
                // sends packet   

                socket.send(packet);

            } catch (IOException ex) {
                System.out.println(
                        "Could not Send   " + ex.getMessage());
                System.exit(0);
            }

        } // for loop

    } // main
} // class EchoServer