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

    // The client socket
    private static Socket clientSocket = null;
    // The output stream
    private static BufferedOutputStream os = null;
    // The default port.
    int portNumber = 2222;
    // The default host.
    String host = "localhost";
    private static byte[] sendData;
    // The input stream reader
    BufferedReader reader;

    public TCPChatClientCallable(int size) {
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
    }
   
    public static void main(String[] args) throws Exception {
        TCPChatClientCallable objTCPChatClientCallable ;
        double[] sampleSorted = new double[3000];
        for (int i = 0; i < 3000; i++) {
            long startwrite = System.nanoTime();
            objTCPChatClientCallable = new TCPChatClientCallable(1024*63);
            objTCPChatClientCallable.call();
            sampleSorted[i] = (System.nanoTime() - startwrite) * 1e-9;
        }
        Arrays.sort(sampleSorted);
        System.out.print("		LATENCY  : second(s)/operation [ min :" + sampleSorted[0] + " | max : " + sampleSorted[sampleSorted.length - 1] + " | median : " + CalcSupport.median(sampleSorted));
        System.out.println(" | mean : " + CalcSupport.mean(sampleSorted) + " ]  ");
        System.out.println("		THROUGHPUT :(MB/sec) " + ((3000 / CalcSupport.sum(sampleSorted)) * (1 / 1)));
    }

    @Override
    public Object call() throws Exception {
        long ret = 0;
        os.write(sendData);
        os.write('\n');
        os.flush();
        reader.readLine();
        return ret;
    }
    @Override
    public TCPChatClientCallable clone(){
       return new TCPChatClientCallable(sendData.length) ;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
