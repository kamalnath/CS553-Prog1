/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkBench;

/**
 *
 * @author kamalnath_ng
 */
import java.net.*;
import java.io.*;

public class EchoClient {

    static final int serverPort = 1026;
    static final int packetSize = 1024;

    public static void main(String args[]) throws
            UnknownHostException, SocketException {
        DatagramSocket socket; //How we send packets
        DatagramPacket packet; //what we send it in
        InetAddress address; //Where to send
        String messageSend; //Message to be send
        String messageReturn; //What we get back  from the Server
        byte[] data;

        //Checks for the arguments that sent to  the java interpreter
        // Make sure command line parameters correctr



        // Gets the IP address of the Server
        address = InetAddress.getByName("127.0.0.1");
        socket = new DatagramSocket();

        data = new byte[packetSize];
        messageSend = new String("hello");
        messageSend.getBytes(0, messageSend.length(), data, 0);

        // remember datagrams hold bytes
        packet = new DatagramPacket(data, data.length, address, serverPort);
        System.out.println(" Trying to Send the packet ");

        try {
            // sends the packet

            socket.send(packet);

        } catch (IOException ie) {
            System.out.println("Could not Send :" + ie.getMessage());
            System.exit(0);
        }

        //packet is reinitialized to use it for recieving

        packet = new DatagramPacket(data, data.length);

        try {
            // Receives the packet from the server

            socket.receive(packet);

        } catch (IOException iee) {
            System.out.println("Could not receive :  " + iee.getMessage());
            System.exit(0);
        }

        // display message received

        messageReturn = new String(packet.getData(), 0);
        System.out.println("Message Returned : "
                + messageReturn.trim());
    }    // main
} // Class EchoClient
//http://www.javaprogrammingforums.com/java-networking/9565-server-client-chat-application-using-udp.html