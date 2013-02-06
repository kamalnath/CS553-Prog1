/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkBench;

import BenchCommonUtils.CalcSupport;
import DiskBenchmark.MyCallable;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class TCPChatClientCallable implements MyCallable {

    // The default host.
    //String host = "192.168.33.170";
    String host = "localhost";

    public TCPChatClientCallable(String host) {
        this.host = host;

    }

    public void domeasurement(int size, int loop) {
        double[] sampleSorted = new double[loop];
        try {
            sampleSorted = call(size, loop);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Arrays.sort(sampleSorted);
        System.out.print("		LATENCY  : milli second(s)/operation [ min :" + sampleSorted[0] *1000+ " | max : " + sampleSorted[sampleSorted.length - 1]*1000 + " | median : " + CalcSupport.median(sampleSorted)*1000);
        System.out.println(" | mean : " + CalcSupport.mean(sampleSorted)*1000 + " ]  ");
        double headdercorrection = (double)(loop * 20)/(1024*1024);
        //System.out.println(headdercorrection);
        System.out.println("		THROUGHPUT :(MB/sec) " + ((((loop+headdercorrection) / CalcSupport.sum(sampleSorted)) * size) / (1024 * 1024)));
    }

    private double[] call(int size, int loop) {
        // The client socket
        Socket clientSocket = null;
        // The output stream
        BufferedOutputStream os = null;
        // The default port.
        int portNumber = 2222;
        byte[] sendData;
        // The input stream reader
        BufferedReader reader = null;
        double[] sampleSorted = new double[loop];
        try {
            sendData = new byte[size];
            clientSocket = new Socket(host, portNumber);
            os = new BufferedOutputStream(clientSocket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < loop; i++) {
                if (i == 0) {
                    sendData = new byte[size];
                } else {
                    sendData = new byte[1];
                }
                long startwrite = System.nanoTime();
                os.write(sendData);
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

    public static void main(String[] args) throws Exception {
        TCPChatClientCallable objTCPChatClientCallable;
        objTCPChatClientCallable = new TCPChatClientCallable("192.168.1.4");
        objTCPChatClientCallable.domeasurement(1024, 1000);

    }

    @Override
    public Object call() throws Exception {

        long ret = 0;
        return ret;
    }

    @Override
    public TCPChatClientCallable clone() {
        return new TCPChatClientCallable(host);
    }
}
