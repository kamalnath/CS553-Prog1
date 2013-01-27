/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class RandRunnableRead implements Runnable{
    private byte[] fbuf;
    private static int i;

    public RandRunnableRead(byte[] fbuf) {
        this.fbuf = fbuf;
    }
 
    @Override
    public void run() {
        try {
            RandomAccessFile file = new RandomAccessFile(RandomUtils.getFilepathRead()+i, "r");
            i++;
           DiskBenchUtil.RandFileRead(file, fbuf);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
}
