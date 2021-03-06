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
public class CustBuffBuffStreamRunnableRead implements MyRunnable {

    InputStream fis;
    byte[] fbuf;
    static int i;
    long overheadtime = 0;
    String filePath;

    public synchronized static int getI() {
        if (i > 2) {
            i = 0;
        } else {
            i++;
        }
        return i;
    }

    public CustBuffBuffStreamRunnableRead(byte[] fbuf,String filePath) {
        this.fbuf = fbuf;
        this.filePath=filePath;
    }

    public InputStream getFis() {
        return fis;
    }

    public void setFis(InputStream fis) {
        this.fis = fis;
    }

    public byte[] getFbuf() {
        return fbuf;
    }

    public void setFbuf(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    @Override
    public void run() {
        long ovrheadtime = 0;
        try {

            long startfilemake = System.nanoTime();
            
            fis = new BufferedInputStream(new FileInputStream(filePath+RandomUtils.getFilepathRead() + getI()));
            ovrheadtime = System.nanoTime() - startfilemake;

            ovrheadtime += DiskBenchUtil.customBufferBufferedStreamRead(fis, fbuf);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            setOverheadtime(ovrheadtime);
        }
    }

    @Override
    public long getOverheadtime() {
        return overheadtime;
    }

    @Override
    public void setOverheadtime(long time) {
        overheadtime = time;
    }

    @Override
    public MyRunnable clone() {
        return new CustBuffBuffStreamRunnableRead( fbuf, filePath);
    }

    @Override
    public Closeable getClose() {
        return null;
    }
}
