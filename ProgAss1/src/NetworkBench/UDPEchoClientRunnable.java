package NetworkBench;

import BenchCommonUtils.CalcSupport;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPEchoClientRunnable implements Callable {

    private int Size;
    private int loop;
private String host;
    public UDPEchoClientRunnable(String host) {
        this.host=host;
    }
    public static void main(String args[]) throws Exception {
        UDPEchoClientRunnable objUDPEchoClientRunnable = new UDPEchoClientRunnable("localhost");
        objUDPEchoClientRunnable.domeasurement(63 * 1024, 1000);
    }
    @Override
    public double[] call() {
        //System.out.println("loop" + loop);
        byte[] sendData;
        byte[] receiveData;
        InetAddress IPAddress;
        double[] sampleSorted = new double[loop];
        DatagramSocket clientSocket = null;
        if (Size == 1) {
            sendData = new byte[1];
        } else if (Size == 1024) {
            sendData = new byte[2];
        } else {
            sendData = new byte[3];
        }
        receiveData = new byte[Size];
        try {
            IPAddress = InetAddress.getByName("localhost");
            clientSocket = new DatagramSocket();
            DatagramPacket sendPacket;
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            for (int i = 0; i < loop; i++) {
                try {
                    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
                    long startwrite = System.nanoTime();
                    clientSocket.send(sendPacket);
                    clientSocket.setSoTimeout(30);
                    clientSocket.receive(receivePacket);
                    sampleSorted[i] = (System.nanoTime() - startwrite) * 1e-9;
                    //System.out.println("receivePacket" + receivePacket.getData().length);
                } catch (SocketTimeoutException ex) {
                    //System.out.println("timeout");
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sampleSorted;
    }

    public void domeasurement(int size, int loop) throws InterruptedException, ExecutionException {
        double[] sampleSorted1, sampleSorted2, sampleSorted;
        UDPEchoClientRunnable objUDPEchoClientRunnable1 = new UDPEchoClientRunnable(host);
        objUDPEchoClientRunnable1.loop = loop;
        objUDPEchoClientRunnable1.Size = size;
        UDPEchoClientRunnable objUDPEchoClientRunnable2 = new UDPEchoClientRunnable(host);
        objUDPEchoClientRunnable2.loop = loop;
        objUDPEchoClientRunnable2.Size = size;
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<double[]> future1 = executor.submit(objUDPEchoClientRunnable1);
        Future<double[]> future2 = executor.submit(objUDPEchoClientRunnable2);
        sampleSorted1 = future1.get();
        sampleSorted2 = future2.get();
        sampleSorted = concat(sampleSorted1, sampleSorted2);
        Arrays.sort(sampleSorted);
        System.out.print("		LATENCY  : second(s)/operation [ min :" + sampleSorted[0] + " | max : " + sampleSorted[sampleSorted.length - 1] + " | median : " + CalcSupport.median(sampleSorted));
        System.out.println(" | mean : " + CalcSupport.mean(sampleSorted) + " ]  ");
        System.out.println("		THROUGHPUT :(MB/sec) " + (((loop * 2 / CalcSupport.sum(sampleSorted)) * size) / (1024 * 1024)));
        executor.shutdown();
    }

    double[] concat(double[] A, double[] B) {
        int aLen = A.length;
        int bLen = B.length;
        double[] C = new double[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }
}