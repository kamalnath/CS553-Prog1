package NetworkBench;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPEchoClientCallable implements Callable {

    private static DatagramSocket clientSocket;
    private static InetAddress IPAddress;
    private static byte[] sendData;
    private static byte[] receiveData;
    private static DatagramPacket sendPacket;
    private static DatagramPacket receivePacket;

    public static DatagramSocket getClientSocket() {
        return clientSocket;
    }

    public UDPEchoClientCallable(int Size) {
        try {
            clientSocket = new DatagramSocket();
            IPAddress = InetAddress.getByName("localhost");
            sendData = new byte[Size];
            receiveData = new byte[Size];
            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        clientSocket.send(sendPacket);
        clientSocket.receive(receivePacket);
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);
        clientSocket.close();
    }

    @Override
    public Object call() throws Exception {
        long retvalue=0;
        clientSocket.send(sendPacket);
        clientSocket.receive(receivePacket);
        return retvalue;
    }
}