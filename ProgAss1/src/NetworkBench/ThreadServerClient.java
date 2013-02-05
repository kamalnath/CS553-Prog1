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
            int i = 0;
            String line = "";
            String ping="";
            while (true) {
                if (i == 0) {
                    line = reader.readLine();
                    //System.out.println("length "+line.getBytes().length);
                }else{
                    ping = reader.readLine();
                    //System.out.println("ping "+ping.getBytes().length);
                }
                i++;
                //System.out.println("length "+line.length);
                if (ping!=null&&ping.startsWith("quit")) {
                    break;
                }
                os.println(line);
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