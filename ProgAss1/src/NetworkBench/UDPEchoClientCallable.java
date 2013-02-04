package NetworkBench;

import BenchCommonUtils.CalcSupport;
import DiskBenchmark.MyCallable;
import java.net.*;
import java.util.Arrays;

    
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
        double[] sampleSorted = new double[10];
        for (int i = 0; i < 10; i++) {
            long startwrite = System.nanoTime();
            objUDPEchoClientCallable.call();
            sampleSorted[i] = (System.nanoTime() - startwrite) * 1e-9;
        }
        Arrays.sort(sampleSorted);
        System.out.print("		LATENCY  : second(s)/operation [ min :" + sampleSorted[0] + " | max : " + sampleSorted[sampleSorted.length - 1] + " | median : " + CalcSupport.median(sampleSorted));
        System.out.println(" | mean : " + CalcSupport.mean(sampleSorted) + " ]  ");
        System.out.println("		THROUGHPUT :(MB/sec) " + ((10 / CalcSupport.sum(sampleSorted)) * (1 / 1)));
    }

    @Override
    public Object call() throws Exception {
        long retvalue=0;
        clientSocket.send(sendPacket);
        clientSocket.receive(receivePacket);
        return retvalue;
    }
}