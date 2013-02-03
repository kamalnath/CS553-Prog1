/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkBench;

import BenchCommonUtils.RandomUtils;
import DiskBenchmark.MyRunnable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPChatClientRunnable implements MyRunnable {

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
    private long overheadtime;

    public TCPChatClientRunnable(int size) {
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
     TCPChatClientRunnable objTCPChatClientCallable= new TCPChatClientRunnable(1024);
     objTCPChatClientCallable.run();
 }
    @Override
    public void run() {

        try {
            os.write(sendData);
            os.write('\n');
            os.flush();
            reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
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
        return new TCPChatClientRunnable( sendData.length);
    }

    @Override
    public Closeable getClose() {
        return null;
    }

   
}
