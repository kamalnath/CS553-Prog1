/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author KamalNath_NG
 */
package NetworkBench;

import DiskBenchmark.*;
import BenchCommonUtils.BenchMarkExecuter;
import BenchCommonUtils.Params;
import java.io.*;

public class BenchNetworkMain {

    private static int BUFFER = 8192;
    private static final Params params = new Params();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        benchText();
    }

    private static void benchText() throws FileNotFoundException {

//        System.out.println("Start WRITE benchmark with little block (1 B)");
        benchNetwork(1);
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

    private static void benchNetwork(final int iType) throws FileNotFoundException {

        byte[] buf;
        int bufflen = 0;
        switch (iType) {
            case 1:
                params.setWarmupTime(2);
                params.setNumberMeasurements(10);
                buf = new byte[1];
                break;
            case 2:
                params.setWarmupTime(10);
                params.setNumberMeasurements(1000);
                buf = new byte[512];
                bufflen = 512;
                break;
            case 3:
                params.setNumberMeasurements(1000);
                params.setWarmupTime(10);
                buf = new byte[1024 * 1024];
                bufflen = buf.length;
                break;
            default:
                params.setNumberMeasurements(10);
                params.setWarmupTime(1);
                buf = new byte[1024 * 1024 * 200];
                bufflen = buf.length;
                break;
        }
        final int ibufflen = bufflen;

        UDPEchoClientCallable objUDPEchoClientCallable = new UDPEchoClientCallable(ibufflen);
        BenchMarkExecuter benchUDPEchoClientCallable = new BenchMarkExecuter(objUDPEchoClientCallable, params);
        UDPEchoClientCallable.getClientSocket().close();

        TCPChatClientCallable objTCPChatClientCallable = new TCPChatClientCallable(bufflen);
        BenchMarkExecuter benchTCPChatClientCallable = new BenchMarkExecuter(objTCPChatClientCallable, params);

        params.setNumberThreads(2);
        UDPEchoClientRunnable objUDPEchoClientRunnable = new UDPEchoClientRunnable(ibufflen);
        BenchMarkExecuter benchUDPEchoClientRunnable = new BenchMarkExecuter(objUDPEchoClientRunnable, params);

        TCPChatClientRunnable objTCPChatClientRunnable = new TCPChatClientRunnable(ibufflen);
        BenchMarkExecuter benchTCPChatClientRunnable = new BenchMarkExecuter(objTCPChatClientRunnable, params);

    }
}
