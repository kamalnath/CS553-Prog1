/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author kamalnath_ng
 */
public class CustBuffBuffStreamRunnableRead implements Runnable {

    InputStream fis;
    byte[] fbuf;

    public CustBuffBuffStreamRunnableRead(  byte[] fbuf) {
        this.fbuf = fbuf;
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
        DiskBenchUtil.customBufferBufferedStreamRead(fis,   fbuf);
    }
    
}

