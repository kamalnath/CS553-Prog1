package NetworkBench;

import BenchCommonUtils.CalcSupport;
import DiskBenchmark.MyRunnable;
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class UDPEchoClientRunnable implements MyRunnable {

    private DatagramSocket clientSocket;
    private byte[] sendData;
    private byte[] receiveData;
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private long overheadtime;

    public DatagramSocket getClientSocket() {
        return clientSocket;
    }

    public UDPEchoClientRunnable(int Size) {
        InetAddress IPAddress;
        try {
            IPAddress = InetAddress.getByName("localhost");
            clientSocket = new DatagramSocket();
            sendData = new byte[Size];
            receiveData = new byte[10];
            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        UDPEchoClientRunnable objUDPEchoClientRunnable = new UDPEchoClientRunnable((1024 * 63));
        double[] sampleSorted = new double[3000];
        for (int i = 0; i < 3000; i++) {
            Thread tr1 = new Thread(objUDPEchoClientRunnable.clone());
            Thread tr2 = new Thread(objUDPEchoClientRunnable.clone());
            long startwrite = System.nanoTime();
            tr1.start();
            tr2.start();
            tr1.join();
            tr2.join();
            sampleSorted[i] = (System.nanoTime() - startwrite) * 1e-9;
        }
        Arrays.sort(sampleSorted);
        System.out.print("		LATENCY  : second(s)/operation [ min :" + sampleSorted[0] + " | max : " + sampleSorted[sampleSorted.length - 1] + " | median : " + CalcSupport.median(sampleSorted));
        System.out.println(" | mean : " + CalcSupport.mean(sampleSorted) + " ]  ");
        System.out.println("		THROUGHPUT :(MB/sec) " + (((3000 * 2) / CalcSupport.sum(sampleSorted)) * (1 / 1)));
    }

    @Override
    public void run() {
        try {
            clientSocket.send(sendPacket);
            clientSocket.setSoTimeout(30);
            clientSocket.receive(receivePacket);
        } catch (SocketTimeoutException ex) { //System.out.println("timeout");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            clientSocket.close();
        }
    }

    @Override
    public long getOverheadtime() {
        return overheadtime;
    }

    @Override
    public void setOverheadtime(long time) {
        this.overheadtime = 0;
    }

    @Override
    public MyRunnable clone() {
        return new UDPEchoClientRunnable(sendData.length);
    }

    @Override
    public Closeable getClose() {
        return null;
    }
}