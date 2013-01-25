/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchCommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author kamalnath_ng
 *
 */
class RandomUtils {

    private static final String filepathWrite = "D:/Data/tmp/write";
    private static final String filepathRead = "D:/Data/tmp/read/FileRead";

    public static String getFilepathRead() {
        return filepathRead;
    }
    private static Random randomGenerator = new Random();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println(getRandFileName());
        System.out.println(getRandFileName());

    }

    public static String getRandFileName() {
        String name = filepathWrite + System.currentTimeMillis() + "" + randomGenerator.nextLong();
        return name;
    }
    
    public static void fileCleanup(){
        File file = new File(filepathWrite  )   ;     
        String[] myFiles;      
            if(file.isDirectory()){  
                myFiles = file.list();  
                for (int i=0; i<myFiles.length; i++) {  
                    File myFile = new File(file, myFiles[i]);   
                    myFile.delete();  
                }  
             }  
    }
    
}
