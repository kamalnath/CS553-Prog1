/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author KamalNath_NG
 */
package DiskBenchmark;

import BenchCommonUtils.BenchMarkExecuter;
import BenchCommonUtils.Params;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.Callable;

public class BenchDiskMain {

    private static int BUFFER = 8192;
    private static final Params params = new Params();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        benchText();
        //bench();params
    }

    private static void benchText() {
        File littleFileRead = new File("D:/Data/read/tmp/little-text");
        File littleFileWrite = new File("D:/Data/write/tmp/little-text");
        System.out.println("Start benchmark with little file (1 B)");

        //benchRead(littleFileRead);
        benchWrite(littleFileWrite, 1);

//        File middleFileRead = new File("D:/Data/read/tmp/medium-text");
//        File middleFileWrite =
//                new File("D:/Data/write/tmp/medium-text");
//
//        System.out.println("Start benchmark with medium file (1 KB)");
//
//        benchRead(middleFileRead);
//        benchWrite(middleFileWrite, 2);
//        File bigFileRead = new File("D:/Data/read/tmp/big-text");
//        File bigFileWrite = new File("D:/Data/write/tmp/big-text");
//
//        System.out.println("Start benchmark with big file (1 MB)");
//
//        //benchRead(bigFileRead);
//        benchWrite(bigFileWrite, 3);
//
//        File fatFileRead = new File("D:/Data/read/tmp/fat-text");
//        File fatFileWrite = new File("D:/Data/write/tmp/fat-text");
//
//        System.out.println("Start benchmark with fat file (1GB)");
//
//        benchRead(fatFileRead);
//        benchWrite(fatFileWrite, 4);

    }

    private static void customBufferBufferedStreamRead(File source) {
        InputStream fis = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(source));
            byte[] buf = new byte[BUFFER];
            int i;
            while ((i = fis.read(buf)) != -1) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fis);
        }
    }

    private static void customBufferBufferedStreamWrite(File target, int bufflen, int limit, byte[] buf) {
        OutputStream fos = null;
        try {
            fos = new BufferedOutputStream(new FileOutputStream(target));
            while (limit >= 0) {
                fos.write(buf, 0, bufflen);
                limit--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
    }

    private static void nioBufferRead(File source) {
        FileChannel in = null;
        try {
            in = new FileInputStream(source).getChannel();
            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER);
            while (in.read(buffer) != -1) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(in);
        }
    }

    private static void nioBufferWrite(File target, int iSize) {
        FileChannel out = null;

        try {
            out = new FileOutputStream(target).getChannel();
            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER);

            int limit = iSize / BUFFER;
            while (limit >= 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    out.write(buffer);
                }
                limit--;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(out);
        }
    }

    private static void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void bench() throws FileNotFoundException, IOException {
        int k = 12345;
        DataOutputStream dos = new DataOutputStream(new FileOutputStream("D:/Data/tmp/little-text.txt"));

        dos.write(k);
        dos.close();
        System.out.println(new File("D:/Data/tmp/little-text.txt").length());
    }

    private static void benchRead(File sourceFileRead) {
        customBufferBufferedStreamRead(sourceFileRead);
    }

    private static void benchWrite(final File destFileWrite, final int iType) {

        byte[] buf;
        int limit = 0;
        int bufflen = 0;
        int iSize = 0;
        int iFact = 1;

        switch (iType) {
            case 1:
                buf = new byte[1];
                bufflen = 1;
                limit = 1;
                break;
            case 2:
                buf = new byte[512];
                bufflen = 512;
                limit = 1;
                break;
            case 3:
                System.out.println("here");
                buf = new byte[BUFFER];
                bufflen = BUFFER;
                iSize = 1024 * 1024;
                limit = iSize / BUFFER;
                System.out.println("  3 " + limit);
                break;
            case 4:
                iFact = 1000;
                buf = new byte[BUFFER * iFact];
                bufflen = BUFFER * iFact;
                iSize = 1024 * 1024 * 1024;
                limit = iSize / (BUFFER * iFact);
                System.out.println("  4 " + limit);
                break;
            default:
                buf = new byte[BUFFER];
                break;
        }
        final int ilimit = limit;
        final int ibufflen = bufflen;
        final byte[] fbuf = buf;
        Callable customBufferBufferedStreamCallable = new Callable() {

            @Override
            public Object call() {
                customBufferBufferedStreamWrite(destFileWrite, ibufflen, ilimit, fbuf);
                return null;
            }
        };

        Runnable customBufferBufferedStreamRunnable = new Runnable() {
            @Override
            public void run() {
                customBufferBufferedStreamWrite(destFileWrite, ibufflen, ilimit, fbuf);
            }
        };
        BenchMarkExecuter benchCustomBufferBufferedStreamSeq = new BenchMarkExecuter(customBufferBufferedStreamCallable, params);
        BenchMarkExecuter benchCustomBufferBufferedStreamParallel = new BenchMarkExecuter(customBufferBufferedStreamRunnable, params);
    }
    
}
