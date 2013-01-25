/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import java.io.*;

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
}
