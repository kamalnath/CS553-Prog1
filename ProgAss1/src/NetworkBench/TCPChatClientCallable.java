/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkBench;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public class TCPChatClientCallable implements Callable {

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
     TCPChatClientCallable objTCPChatClientCallable= new TCPChatClientCallable(1024);
     objTCPChatClientCallable.call();
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
}
