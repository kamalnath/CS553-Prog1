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
public class RandRunnableWrite implements Runnable{
    private byte[] fbuf;

    public RandRunnableWrite(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    @Override
    public void run() {
        try {
            RandomAccessFile file = new RandomAccessFile(RandomUtils.getRandFileName(), "rw");
            DiskBenchUtil.RandFileWrite(file, fbuf);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
}
