/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.*;

/**
 *
 * @author kamalnath_ng
 */
public class DiskBenchUtil {

    public static long customBufferBufferedStreamWrite(OutputStream fos, int bufflen, byte[] buf) {
        long overheadtime = 0;
        try {
            fos.write(buf, 0, bufflen);
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            long startfileclose = System.nanoTime();
            close(fos);
            overheadtime = System.nanoTime() - startfileclose;
        }
        return overheadtime;
    }

    public static long customBufferBufferedStreamRead(InputStream fis, byte[] buf) {
        long overheadtime = 0;
        try {
            fis.read(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long startfileclose = System.nanoTime();
            //close(fis);
            overheadtime = System.nanoTime() - startfileclose;
        }
        return overheadtime;
    }

    public static void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static long RandFileRead(RandomAccessFile file, byte[] fbuf) {
        long overheadtime = 0;
        try {
            long startfileclose = System.nanoTime();
            int rand = RandomUtils.getRandomGenerator().nextInt((int) file.length());
            overheadtime += System.nanoTime() - startfileclose;
            file.seek(rand);
            file.read(fbuf);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            long startfileclose = System.nanoTime();
            //close(file);
            overheadtime += System.nanoTime() - startfileclose;
        }
        return overheadtime;
    }

    public static long RandFileWrite(RandomAccessFile file, byte[] fbuf) {
        long overheadtime = 0;
        try {
            long startfileclose = System.nanoTime();
            int rand = RandomUtils.getRandomGenerator().nextInt(fbuf.length);
            overheadtime += System.nanoTime() - startfileclose;
            file.seek(rand);
            file.write(fbuf);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            long startfileclose = System.nanoTime();
            close(file);
            overheadtime += System.nanoTime() - startfileclose;
        }
        return overheadtime;
    }
}
