/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkBench;

import BenchCommonUtils.CalcSupport;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.*;

public class TCPChatClientRunnable implements Callable {

    // The output stream
    private BufferedOutputStream os = null;
    private static byte[] sendData;
    private long overheadtime;
    BufferedReader reader;
    private int loop;
    private int size;
    private String host;

    public static void main(String[] args) throws Exception {
        TCPChatClientRunnable objUDPEchoClientCallable1 = new TCPChatClientRunnable(1,"192.168.1.4");
        objUDPEchoClientCallable1.domeasurement(1, 1000);
    }

    TCPChatClientRunnable(int i, String strServer) {
        this.host=strServer;
    }
    public void domeasurement(int size, int loop) throws InterruptedException, ExecutionException {
        double[] sampleSorted1, sampleSorted2, sampleSorted;
        TCPChatClientRunnable objUDPEchoClientCallable1 = new TCPChatClientRunnable(1,host);
        
        objUDPEchoClientCallable1.loop = loop;
        objUDPEchoClientCallable1.size = size;
        TCPChatClientRunnable objUDPEchoClientCallable2 = new TCPChatClientRunnable(1,host);
        objUDPEchoClientCallable2.loop = loop;
        objUDPEchoClientCallable2.size = size;
        ExecutorService executor = Executors.newFixedThreadPool(4);
        long startwrite = System.nanoTime();
        Future<double[]> future1 = executor.submit(objUDPEchoClientCallable1);
        Future<double[]> future2 = executor.submit(objUDPEchoClientCallable2);
        sampleSorted1 = future1.get();
        sampleSorted2 = future2.get();
        sampleSorted = concat(sampleSorted1, sampleSorted2);
        Arrays.sort(sampleSorted);
        System.out.print("		LATENCY  : milli second(s)/operation [ min :" + sampleSorted[0]*1000 + " | max : " + sampleSorted[sampleSorted.length - 1]*1000 + " | median : " + CalcSupport.median(sampleSorted)*1000);
        System.out.println(" | mean : " + CalcSupport.mean(sampleSorted)*1000 + " ]  ");
         double headdercorrection = (double)(loop * 20*2)/(1024*1024);
         double time =(System.nanoTime() - startwrite) * 1e-9;
        System.out.println("		THROUGHPUT :(MB/sec) " + ((((loop+headdercorrection) * 2 / time) * size) / (1024 * 1024)));
  //       System.out.println("		THROUGHPUT :(MB/sec) " + ((((loop+0) * 2 / CalcSupport.sum(sampleSorted)) * size) / (1024 * 1024)));
//         System.out.println("		THROUGHPUT :(MB/sec) " + ((2*loop*size)+headdercorrection)/(1024*1024*CalcSupport.sum(sampleSorted)));
         
        executor.shutdown();
    }

    @Override
    public double[] call() {
        // The client socket
        Socket clientSocket = null;
        // The default port.
        int portNumber = 2222;
        // The input stream reader
        try {
            sendData = new byte[size];
            clientSocket = new Socket(host, portNumber);
            os = new BufferedOutputStream(clientSocket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //System.out.println("loop" + loop);
        double[] sampleSorted = new double[loop];
        try {
            for (int i = 0; i < loop; i++) {
                if (i == 0) {
                    sendData = new byte[size];
                } else {
                    sendData = new byte[1];
                }
                os.write(sendData);
                long startwrite = System.nanoTime();
                os.write('\n');
                os.flush();
                String line = reader.readLine();
                sampleSorted[i] = (System.nanoTime() - startwrite) * 1e-9;
                //System.out.println("length" + line.getBytes().length);
            }
            os.write("quit".getBytes());
            os.write('\n');
            os.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sampleSorted;
    }

    double[] concat(double[] A, double[] B) {
        int aLen = A.length;
        int bLen = B.length;
        double[] C = new double[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }

    @Override
    public Callable clone() {
        return new TCPChatClientRunnable(sendData.length);
    }

    public TCPChatClientRunnable(int size) {

    }
}
