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
public class CustBuffBuffStreamRunnableWrite implements MyRunnable {

    OutputStream fos;
    int ibufflen;
    byte[] fbuf;
    private long overheadtime;
    String filePath;

    public CustBuffBuffStreamRunnableWrite( int ibufflen, byte[] fbuf,String filePath) {
        try {
            this.ibufflen = ibufflen;
            this.fbuf = fbuf;
            this.filePath= filePath;
            fos = new BufferedOutputStream(new FileOutputStream(filePath+RandomUtils.getRandFileName()));
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        long ovrheadtime = 0;
            DiskBenchUtil.customBufferBufferedStreamWrite(fos, ibufflen,  fbuf);
            setOverheadtime(ovrheadtime);
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

    @Override
    public MyRunnable clone() {
       return new CustBuffBuffStreamRunnableWrite(ibufflen, fbuf, filePath);
    }

    @Override
    public Closeable getClose() {
        return fos;
    }
    
}

