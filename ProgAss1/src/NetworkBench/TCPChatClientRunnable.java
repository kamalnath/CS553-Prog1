/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkBench;

import BenchCommonUtils.CalcSupport;
import DiskBenchmark.MyRunnable;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPChatClientRunnable implements MyRunnable {

    // The output stream
    private BufferedOutputStream os = null;
    private static byte[] sendData;
    private long overheadtime;
    BufferedReader reader;
   
    public TCPChatClientRunnable(int size) {
        // The client socket
        Socket clientSocket = null;
        // The default port.
        int portNumber = 2222;
        // The default host.
        String host = "localhost";
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
    }

    public static void main(String[] args) throws Exception {

        TCPChatClientRunnable objUDPEchoClientCallable1 = new TCPChatClientRunnable((1024 * 3));
        TCPChatClientRunnable objUDPEchoClientCallable2 = new TCPChatClientRunnable((1024 * 3));
        double[] sampleSorted = new double[1000];
        for (int i = 0; i < 1000; i++) {
            Thread tr1 = new Thread(objUDPEchoClientCallable2.clone());
            Thread tr2 = new Thread(objUDPEchoClientCallable1.clone());
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
        System.out.println("		THROUGHPUT :(MB/sec) " + (((1000 * 2) / CalcSupport.sum(sampleSorted)) * (1 / 1)));
    }

    @Override
    public void run() {
        try {
            os.write(sendData);
            os.write('\n');
            os.flush();
            //reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                os.close();
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public long getOverheadtime() {
        return overheadtime;
    }

    @Override
    public void setOverheadtime(long time) {
        this.overheadtime = time;
    }

    @Override
    public MyRunnable clone() {
        return new TCPChatClientRunnable(sendData.length);
    }

    @Override
    public Closeable getClose() {
        return null;
    }
}
