package NetworkBench;

import java.net.DatagramPacket;
import java.net.DatagramSocket;



public class UDPEchoServer extends Thread{

    DatagramSocket serverSocket;
    byte[] receiveData;

    public UDPEchoServer() {
        super();
        try {
            serverSocket = new DatagramSocket(9876);
            receiveData = new byte[1024*64];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
        public void run() {
            waitClient();
        }
    public void waitClient() {
        while (true) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                Thread objThread = new Thread(new UDPEchoServerSupporter(receivePacket,serverSocket));
                objThread.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws Exception {
        new UDPEchoServer().start();
    }
}
