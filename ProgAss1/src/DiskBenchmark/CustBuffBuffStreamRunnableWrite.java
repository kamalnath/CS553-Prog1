/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 *
 * @author kamalnath_ng
 */
public class CustBuffBuffStreamRunnableWrite implements MyRunnable {

    OutputStream fos;
    int ibufflen;
    byte[] fbuf;
    private long overheadtime;
    String filePath;

    public CustBuffBuffStreamRunnableWrite( int ibufflen, byte[] fbuf,String filePath) {
        this.ibufflen = ibufflen;
        this.fbuf = fbuf;
        this.filePath= filePath;
    }

    @Override
    public void run() {
        long ovrheadtime = 0;
        try {
            long startfilemake = System.nanoTime();
            fos = new BufferedOutputStream(new FileOutputStream(filePath+RandomUtils.getRandFileName()));
            ovrheadtime = System.nanoTime() - startfilemake;
            ovrheadtime += DiskBenchUtil.customBufferBufferedStreamWrite(fos, ibufflen,  fbuf);
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
        }
        finally{
            setOverheadtime(ovrheadtime);
        }
    }

    public OutputStream getFos() {
        return fos;
    }

    public void setFos(OutputStream fos) {
        this.fos = fos;
    }

   

    public byte[] getFbuf() {
        return fbuf;
    }

    public void setFbuf(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    public int getIbufflen() {
        return ibufflen;
    }

    public void setIbufflen(int ibufflen) {
        this.ibufflen = ibufflen;
    }
    @Override
    public long getOverheadtime() {
        return overheadtime;
    }

    @Override
    public void setOverheadtime(long time) {
        overheadtime = time;
    }
}

