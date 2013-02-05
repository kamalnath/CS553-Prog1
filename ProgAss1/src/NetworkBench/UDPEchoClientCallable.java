package NetworkBench;

import BenchCommonUtils.CalcSupport;
import DiskBenchmark.MyCallable;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

    
public class UDPEchoClientCallable implements MyCallable {

    private static DatagramSocket clientSocket;
    private static InetAddress IPAddress;
    private static byte[] sendData;
    private static byte[] receiveData;
    private static DatagramPacket sendPacket;
    private static DatagramPacket receivePacket;

    public  DatagramSocket getClientSocket() {
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
        
        UDPEchoClientCallable objUDPEchoClientCallable = new UDPEchoClientCallable((1024*63));
        double[] sampleSorted = new double[8000];
        for (int i = 0; i < 8000; i++) {
            long startwrite = System.nanoTime();
            objUDPEchoClientCallable.call();
            sampleSorted[i] = (System.nanoTime() - startwrite) * 1e-9;
        }
        Arrays.sort(sampleSorted);
        System.out.print("		LATENCY  : second(s)/operation [ min :" + sampleSorted[0] + " | max : " + sampleSorted[sampleSorted.length - 1] + " | median : " + CalcSupport.median(sampleSorted));
        System.out.println(" | mean : " + CalcSupport.mean(sampleSorted) + " ]  ");
        System.out.println("		THROUGHPUT :(MB/sec) " + (((8000 / CalcSupport.sum(sampleSorted)) * 63 )/ 1024));
    }

    @Override
    public Object call()  {
         long retvalue=0;
        try {
            sendData = new byte[2];
            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            clientSocket.send(sendPacket);
            clientSocket.setSoTimeout(30);
            clientSocket.receive(receivePacket);
           // System.out.println("RECEIVED: " + receivePacket.getLength());
        }catch (SocketTimeoutException ex) { //System.out.println("timeout");
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        return retvalue;
    }
}