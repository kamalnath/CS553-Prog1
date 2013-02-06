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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class BenchNetworkMain {

    private static final Params params = new Params();
    static String strServer;
    public static void main(String[] args) throws Exception {
        benchNetworkAll();
    }

    private static void benchNetworkAll() throws Exception {
        System.out.println("Please enter server/receiver location");
        Scanner sc = new Scanner(System.in);
         strServer = sc.nextLine();
        if(strServer.equalsIgnoreCase("")){
            System.out.println("local servers/receiver used as default");
            strServer = "localhost";
        }
        if (!(strServer.equals("localhost") || strServer.equals("127.0.0.1"))) {
            System.out.println("Please confirm the server/receiver has been startted ");
            sc.nextLine();
        } else {
            System.out.println("Starting local servers/receiver ");
            new UDPEchoServer().start();
            new TCPMultiThreadChatServerEcho().start();
        }
        while (true) {
            System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
            System.out.println("                     Select your choice                               ");
            System.out.println("  0  >------> Run all network Bench marks for all size packets <--------------<   ");
            System.out.println("  1  >------>      Run all network Bench marks for 1 Byte      <--------------<   ");
            System.out.println("  2  >------>      Run all network Bench marks for 1 KB        <--------------<   ");
            System.out.println("  3  >------>      Run all network Bench marks for 64 KB       <--------------<   ");
            System.out.println("  4  >------>                     To Exit                      <--------------<   ");
            System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
            switch (sc.nextInt()) {
                case 0:
                    benchNetwork(1);
                    benchNetwork(2);
                    benchNetwork(3);
                    break;
                case 1:
                    benchNetwork(1);
                    break;
                case 2:
                    benchNetwork(2);
                    break;
                case 3:
                    benchNetwork(3);
                    break;
                default:
                    sc.nextLine();
                    System.exit(0);
            }
        }
    }

    private static void benchNetwork(final int iType) throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
        int bufflen = 0;
        switch (iType) {
            case 1:
                System.out.println("");
                System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
                System.out.println("Start Network benchmark with little block (1 B)");
                params.setWarmupTime(1);
                params.setNumberMeasurements(3000);
                bufflen = 1;
                break;
            case 2:
                System.out.println("");
                System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
                System.out.println("Start Network benchmark with little block (1 KB)");
                params.setWarmupTime(1);
                params.setNumberMeasurements(3000);
                bufflen = 1024;
                break;
            case 3:
                System.out.println("");
                System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
                System.out.println("Start Network benchmark with little block (64KB)");
                System.out.println("");
                params.setNumberMeasurements(1000);
                params.setWarmupTime(1);
                bufflen = 1024 * 63;
                break;

        }
        final int ibufflen = bufflen;
        params.setFactor((double) bufflen / (1024 * 1024));
        System.out.println("");
        System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
        System.out.println("");
        System.out.println("--- Results for Single threaded Runs--");
        System.out.println("      UDP Packets :");
        UDPEchoClientCallable objUDPEchoClientCallable = new UDPEchoClientCallable(ibufflen,strServer);
        BenchMarkExecuterNetwork benchUDPEchoClientCallable = new BenchMarkExecuterNetwork(objUDPEchoClientCallable, params);
        //objUDPEchoClientCallable.getClientSocket().close();
        cleanJvm();
        System.out.println("      TCP Packets :");
        
        new TCPChatClientCallable(strServer).domeasurement(bufflen, 1000);
        cleanJvm(); 
        params.setNumberThreads(2);
        System.out.println("");
        System.out.println("------------------------------ --------------------------------- --------------------------------- --------------------------------- --- ------------------- --");
        System.out.println("");
        System.out.println("--- Results for Multi threaded Runs--");
        System.out.println("      UDP Packets :");
        new UDPEchoClientRunnable(strServer).domeasurement(bufflen, 1000);
        cleanJvm();
        System.out.println("      TCP Packets :");
        new TCPChatClientRunnable(1,strServer).domeasurement(bufflen, 1000);
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
