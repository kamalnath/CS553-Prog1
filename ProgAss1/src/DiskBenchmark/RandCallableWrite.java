/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import BenchCommonUtils.RandomUtils;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;

/**
 *
 * @author USER
 */
public class RandCallableWrite implements Callable {

    private byte[] fbuf;

    public RandCallableWrite(byte[] fbuf) {
        this.fbuf = fbuf;
    }

    @Override
    public Object call() throws Exception {
        RandomAccessFile file = new RandomAccessFile(RandomUtils.getRandFileName(), "rw");
        DiskBenchUtil.RandFileWrite(file, fbuf);
        return null;
    }
}
