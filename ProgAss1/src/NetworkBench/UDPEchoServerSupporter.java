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

    UDPEchoServerSupporter(DatagramPacket receivePacket, DatagramSocket serverSocket) {
        this.receivePacket = receivePacket;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            //System.out.println("RECEIVED: " + receivePacket.getLength());
            int isize = receivePacket.getLength();
            if(isize==1){
                sendData = new byte[1];
            }else if(isize==2){
                sendData = new byte[1024];
            }else{
                sendData = new byte[1024*63];
            }
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}