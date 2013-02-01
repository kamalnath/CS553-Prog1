package NetworkBench;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPEchoServer {

    DatagramSocket serverSocket;
    byte[] receiveData;
    byte[] sendData;

    public UDPEchoServer() {
        try {
            serverSocket = new DatagramSocket(9876);
            receiveData = new byte[1024];
            sendData = new byte[1024];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void waitClient() {
        while (true) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                Thread objThread = new Thread(new UDPEchoServerSupporter(receivePacket, sendData, serverSocket));
                objThread.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws Exception {
        new UDPEchoServer().waitClient();
    }
}
