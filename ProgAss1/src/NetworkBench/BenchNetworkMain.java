/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author KamalNath_NG
 */
package NetworkBench;

import BenchCommonUtils.BenchMarkExecuterNetwork;
import BenchCommonUtils.Params;
import java.io.*;
import java.util.Scanner;

public class BenchNetworkMain {

    private static final Params params = new Params();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        benchText();
    }

    private static void benchText() throws FileNotFoundException, IOException {
        System.out.println("Please enter server/receiver location");
        Scanner sc = new Scanner(System.in);
        String strServer = sc.nextLine();
        strServer = "localhost";
        if (!(strServer.equals("localhost") || strServer.equals("127.0.0.1"))) {
            System.out.println("Please confirm the server/receiver has been startted ");
            sc.nextLine();
        } else {
            System.out.println("Starting servers/receiver ");
            new UDPEchoServer().start();
            new TCPMultiThreadChatServerEcho().start();
        }
//        System.out.println("Start WRITE benchmark with little block (1 B)");
        benchNetwork(3);
        sc.nextLine();
        System.exit(0);
//        System.out.println("Start WRITE benchmark with medium block (1 KB)");
//        benchWrite(2);
//        System.out.println("Start WRITE benchmark with medium block (1 MB)");
//        benchWrite(3);
//        System.out.println("Start WRITE benchmark with medium block (1 GB)");
//        benchWrite(4);

//            benchRead(1);
//            benchRead(2);
//            benchRead(3);
//            benchRead(4);
    }

    private static void benchNetwork(final int iType) throws FileNotFoundException, IOException {

        int bufflen = 0;
        switch (iType) {
            case 1:
                params.setWarmupTime(1);
                params.setNumberMeasurements(3000);
                bufflen = 1;
                break;
            case 2:
                params.setWarmupTime(1);
                params.setNumberMeasurements(3000);
                bufflen = 1024;
                break; 
            case 3:
                params.setNumberMeasurements(2000);
                params.setWarmupTime(1);
                bufflen = 1024 * 63;
                break;

        }
        final int ibufflen = bufflen;
        params.setFactor((double) 1.0);
        UDPEchoClientCallable objUDPEchoClientCallable = new UDPEchoClientCallable(ibufflen);
        BenchMarkExecuterNetwork benchUDPEchoClientCallable = new BenchMarkExecuterNetwork(objUDPEchoClientCallable, params);
        objUDPEchoClientCallable.getClientSocket().close();

        TCPChatClientCallable objTCPChatClientCallable = new TCPChatClientCallable(bufflen);
        BenchMarkExecuterNetwork benchTCPChatClientCallable = new BenchMarkExecuterNetwork(objTCPChatClientCallable, params);
        objTCPChatClientCallable.getClientSocket().close();

        params.setNumberThreads(2);
        UDPEchoClientRunnable objUDPEchoClientRunnable = new UDPEchoClientRunnable(ibufflen);
        BenchMarkExecuterNetwork benchUDPEchoClientRunnable = new BenchMarkExecuterNetwork(objUDPEchoClientRunnable, params);

        TCPChatClientRunnable objTCPChatClientRunnable = new TCPChatClientRunnable(ibufflen);
        BenchMarkExecuterNetwork benchTCPChatClientRunnable = new BenchMarkExecuterNetwork(objTCPChatClientRunnable, params);

    }
}
