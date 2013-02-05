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
import java.lang.management.ManagementFactory;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class BenchNetworkMain {

    private static final Params params = new Params();

    public static void main(String[] args) throws Exception {
        benchText();
    }

    private static void benchText() throws Exception {
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
        sc.nextLine();
        System.out.println("Start Network benchmark with little block (1 B)");
        benchNetwork(1);
        sc.nextLine();
        System.out.println("Start Network benchmark with little block (1 KB)");
        benchNetwork(2);
        sc.nextLine();
        System.out.println("Start Network benchmark with little block (64KB)");
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

    private static void benchNetwork(final int iType) throws FileNotFoundException, IOException, InterruptedException, ExecutionException {

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
                params.setNumberMeasurements(1000);
                params.setWarmupTime(1);
                bufflen = 1024 * 63;
                break;

        }
        final int ibufflen = bufflen;
        params.setFactor((double) bufflen / (1024 * 1024));

        UDPEchoClientCallable objUDPEchoClientCallable = new UDPEchoClientCallable(ibufflen);
        BenchMarkExecuterNetwork benchUDPEchoClientCallable = new BenchMarkExecuterNetwork(objUDPEchoClientCallable, params);
        objUDPEchoClientCallable.getClientSocket().close();
        cleanJvm();
        new TCPChatClientCallable(1).domeasurement(bufflen, 2000);
        cleanJvm();
        params.setNumberThreads(2);
        new UDPEchoClientRunnable(ibufflen).domeasurement(bufflen, 1000);
        cleanJvm();
        new TCPChatClientRunnable(1).domeasurement(bufflen, 2000);
    }
    private static void cleanJvm() {
        long memUsedPrev = memoryUsed();
        for (int i = 0; i < 100; i++) {
            System.runFinalization();
            System.gc();
            long memUsedNow = memoryUsed();
            if ((ManagementFactory.getMemoryMXBean().getObjectPendingFinalizationCount() == 0)
                    && (memUsedNow >= memUsedPrev)) {

                break;
            } else {
                memUsedPrev = memUsedNow;
            }
        }
    }

    public static long memoryUsed() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }
}
