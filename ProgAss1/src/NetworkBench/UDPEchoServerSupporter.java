/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkBench;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author KamalNath_NG
 */
public class UDPEchoServerSupporter implements Runnable {

    DatagramPacket receivePacket;
    DatagramSocket serverSocket;
    byte[] sendData;

    public UDPEchoServerSupporter(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    }

    UDPEchoServerSupporter(DatagramPacket receivePacket, byte[] sendData, DatagramSocket serverSocket) {
        this.receivePacket = receivePacket;
        this.sendData = sendData;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {

        try {
            String sentence = new String(receivePacket.getData());
            System.out.println("RECEIVED: " + sentence);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            sendData = sentence.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}