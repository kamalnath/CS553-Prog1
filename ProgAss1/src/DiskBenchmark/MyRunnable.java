 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DiskBenchmark;

import java.io.Closeable;

/**
 *
 * @author USER
 */
public interface MyRunnable extends Runnable{
     
    long getOverheadtime();
    void setOverheadtime(long time);
    MyRunnable clone();

    public Closeable getClose();
 
}
