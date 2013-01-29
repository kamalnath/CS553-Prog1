package NetworkBench;

import DiskBenchmark.MyRunnable;
import java.io.*;
import java.net.*;

public class UDPEchoClientRunnable implements MyRunnable {

    private static DatagramSocket clientSocket;
    private static InetAddress IPAddress;
    private static byte[] sendData;
    private static byte[] receiveData;
    private static DatagramPacket sendPacket;
    private static DatagramPacket receivePacket;
    private long overheadtime ;
    
    public static DatagramSocket getClientSocket() {
        return clientSocket;
    }

    public UDPEchoClientRunnable(int Size) {
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
    public void run() {
        try {
            clientSocket.send(sendPacket);
            clientSocket.receive(receivePacket);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public long getOverheadtime(){
        return overheadtime;
    }

    @Override
    public void setOverheadtime(long time) {
       this.overheadtime=0;
    }
}