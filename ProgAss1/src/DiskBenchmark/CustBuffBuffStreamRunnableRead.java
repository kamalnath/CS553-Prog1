/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author kamalnath_ng
 */
public class CustBuffBuffStreamRunnableRead implements Runnable {

    InputStream fis;
    byte[] fbuf;
    static int i;

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
        try {
            fis = new BufferedInputStream(new FileInputStream(RandomUtils.getFilepathRead()+i));
            i++;
            DiskBenchUtil.customBufferBufferedStreamRead(fis,   fbuf);
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
        }
    }
    
}

