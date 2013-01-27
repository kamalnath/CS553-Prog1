/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kamalnath_ng
 */
public class DiskBenchUtil {
  
    public static void customBufferBufferedStreamWrite(OutputStream fos, int bufflen, byte[] buf) {
        try {
            fos.write(buf, 0, bufflen);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
    }

    public static void customBufferBufferedStreamRead(InputStream fis, byte[] buf) {
        try {
            fis.read(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fis);
        }
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

    public static void RandFileRead(RandomAccessFile file, byte[] fbuf) {
        try {
            int rand =RandomUtils.getRandomGenerator().nextInt((int)file.length());
            file.seek(rand);
            file.read(fbuf);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            close(file);
        }
    }
    public static void RandFileWrite(RandomAccessFile file, byte[] fbuf) {
        try {
            int rand =RandomUtils.getRandomGenerator().nextInt((int)file.length());
            file.seek(rand);
            file.write(fbuf);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            close(file);
        }
    }
}
