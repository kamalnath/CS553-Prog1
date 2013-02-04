/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkBench;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kamalnath_ng
 */
class ThreadServerClient extends Thread {

    Socket clientSocket = null;

    ThreadServerClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        PrintStream os = null;
        BufferedReader reader = null;
        try {
            //Create input and output streams for this client.
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new PrintStream(clientSocket.getOutputStream());
            byte[] b= new byte[1];
            while (true) {
                String line = reader.readLine();
                //System.out.println("RECEIVED: " + line.length());
                os.println();

            }

        } catch (IOException e) {
        } finally {
            try {
                os.close();
                reader.close();
                clientSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}