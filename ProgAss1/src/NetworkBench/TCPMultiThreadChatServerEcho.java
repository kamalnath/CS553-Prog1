package NetworkBench;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
   
/*
 * A chat server that delivers public and private messages.
 */
public class TCPMultiThreadChatServerEcho extends Thread{

    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.

    @Override
    public void run() {
        // The default port number.
        int portNumber = 2222;
        Socket clientSocket = null;
        //Open a server socket on the portNumber (default 2222). 
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }
        //Create a client socket for each connection and pass it to a new  client thread.
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                new ThreadServerClient(clientSocket).start();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
    public static void main(String args[]) throws Exception {
        System.out.println("starting TCP server");
        new TCPMultiThreadChatServerEcho().start();
    }
}
