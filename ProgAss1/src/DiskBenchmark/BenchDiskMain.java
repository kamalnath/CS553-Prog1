/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author KamalNath_NG
 */
package DiskBenchmark;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BenchDiskMain {

    private static int BUFFER = 8192;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        benchText();
        //bench();
    }

    private static void benchText() {
//        File littleFileRead = new File("D:/Data/read/tmp/little-text");
//        File littleFileWrite = new File("D:/Data/write/tmp/little-text");
//        System.out.println("Start benchmark with little file (1 B)");
//        
//        benchRead(littleFileRead);
//        //benchWrite(littleFileWrite, 1);
//        
//        File middleFileRead = new File("D:/Data/read/tmp/medium-text");
//        File middleFileWrite =
//                new File("D:/Data/write/tmp/medium-text");
//
//        System.out.println("Start benchmark with medium file (1 KB)");
//
//        benchRead(middleFileRead);
//        //benchWrite(middleFileWrite, 2);
//        File bigFileRead = new File("D:/Data/read/tmp/big-text");
//        File bigFileWrite = new File("D:/Data/write/tmp/big-text");
//
//        System.out.println("Start benchmark with big file (1 MB)");
//
//        benchRead(bigFileRead);
//        //benchWrite(bigFileWrite, 3);

        File fatFileRead = new File("D:/Data/read/tmp/fat-text");
        File fatFileWrite = new File("D:/Data/write/tmp/fat-text");

        System.out.println("Start benchmark with fat file (1GB)");

        benchRead(fatFileRead);
        //benchWrite(fatFileWrite, 4);

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

    private static void customBufferBufferedStreamWrite(File target, int iType) {
        OutputStream fos = null;
        try {
            fos = new BufferedOutputStream(new FileOutputStream(target));
            byte[] buf;
            int limit = 0;
            int bufflen = BUFFER;
            int iSize = 0;
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
                    buf = new byte[BUFFER];
                    iSize = 1024 * 1024;
                    limit = iSize / BUFFER ;
                    break;
                case 4:
                    int Fact=1000;
                    buf = new byte[BUFFER * Fact];
                    bufflen = BUFFER * Fact;
                    iSize = 1024 * 1024 * 1024;
                    limit = iSize / (BUFFER * Fact);
                    System.out.println("4" +limit);
                    break;
                default:
                    buf = new byte[BUFFER];
                    break;
            }
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

    private static void benchWrite(File destFileWrite, int i) {
        customBufferBufferedStreamWrite(destFileWrite, i);
    }
}
