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
public class RandomUtils {

    private static final String filepathWrite = "D:/Data/tmp/write/";
    private static final String filepathRead = "D:/Data/read/FileRead";
    private static final String filepathRandRead = "D:/Data/read/RandFileRead";

    public static String getFilepathRandRead() {
        return filepathRandRead;
    }
    
    public static String getFilepathRead() {
        return filepathRead;
    }
    
    private static Random randomGenerator = new Random();

    public static Random getRandomGenerator() {
        return randomGenerator;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println(getRandFileName());
        System.out.println(getRandFileName());

    }

    public static String getRandFileName() {
        String name = filepathWrite + "File_"+getRandomGenerator().nextInt(1000000)+System.currentTimeMillis();
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
