/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkBench;

import java.io.*;
import java.net.Socket;

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

        try {
            PrintStream os;

            /*
             * Create input and output streams for this client.
             */

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new PrintStream(clientSocket.getOutputStream());
            /*
             * Start the conversation.
             */
            while (true) {
                String line = reader.readLine();
                if (line.startsWith("/quit")) {
                    break;
                }
                System.out.println("RECEIVED: " + line);
                os.println("*** from server " + line + " ***");
                
            }
            /*
             * Close the output stream, close the input stream, close the
             * socket.
             */

            os.close();
            reader.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}