/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 *
 * @author kamalnath_ng
 */ 
public class CustBuffBuffStreamCallableWrite implements MyCallable {

    OutputStream fos; 
    int ibufflen;
    byte[] fbuf;
    String filePath;

    @Override
    public Object call() throws FileNotFoundException {
        DiskBenchUtil.customBufferBufferedStreamWrite(fos, ibufflen, fbuf);
        return 0;
    }

    public byte[] getFbuf() {
        return fbuf;
    }

    public void setFbuf(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    public OutputStream getFos() {
        return fos;
    }

    public void setFos(OutputStream fos) {
        this.fos = fos;
    }

    public int getIbufflen() {
        return ibufflen;
    }

    public void setIbufflen(int ibufflen) {
        this.ibufflen = ibufflen;
    }

    public CustBuffBuffStreamCallableWrite( int ibufflen, byte[] fbuf,String filePath) {
        try {
            this.ibufflen = ibufflen;
            this.fbuf = fbuf;
            this.filePath =filePath;
            fos = new BufferedOutputStream(new FileOutputStream(filePath+RandomUtils.getRandFileName()));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    

    
}
