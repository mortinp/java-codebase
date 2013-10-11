/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

/**
 *
 * @author Maylen
 */
public class FileIO {
    /** Copy a file from one filename to another */
    public static void copyFile(String inName, String outName) throws FileNotFoundException, IOException {
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(inName));
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outName));
        copyFile(is, os, true);
    }
    
    public static void copyFile(InputStream is, String outName) throws FileNotFoundException, IOException {
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outName));
        copyFile(is, os, true);
    }
    
    /** Copy a file from one filename to another
     * If the destination directory does not exist, it is created
     */
    public static void forceCopyFile(String inName, String outName) throws FileNotFoundException, IOException {
        String dirOutPath = outName.substring(0, outName.lastIndexOf("/"));
        File dirOut = new File(dirOutPath);
        if(!dirOut.exists()) dirOut.mkdirs();
        copyFile(inName, outName);
    }
    
    public static void forceCopyFile(InputStream is, String outName) throws FileNotFoundException, IOException {
        String dirOutPath = outName.substring(0, outName.lastIndexOf("/"));
        File dirOut = new File(dirOutPath);
        if(!dirOut.exists()) dirOut.mkdirs();
        copyFile(is, outName);
    }

    /** Copy a file from an opened InputStream to opened OutputStream
    */
    public static void copyFile(InputStream is, OutputStream os, boolean close) throws IOException {
        int b; // the byte read from the file
        while ((b = is.read( )) != -1) {
            os.write(b);
        }
        is.close( );
        if (close) os.close( );
    }
    
    /** Copy a file from an opened Reader to opened Writer */
    public static void copyFile(Reader is, Writer os, boolean close) throws IOException {
        int b; // the byte read from the file
        while ((b = is.read( )) != -1) {
            os.write(b);
        }
        is.close( );
        if (close) os.close( );
    }
    
    /** Copy a file from a filename to a PrintWriter. */
    public static void copyFile(String inName, PrintWriter pw, boolean close) throws FileNotFoundException, IOException {
        BufferedReader is = new BufferedReader(new FileReader(inName));
        copyFile(is, pw, close);
    }
    
    /** Open a file and read the first line from it. */
    public static String readLine(String inName) throws FileNotFoundException, IOException {
        BufferedReader is = new BufferedReader(new FileReader(inName));
        String line = null;
        line = is.readLine( );
        is.close( );
        return line;
    }
    
    public static void replaceStringInFile(String filePath, String oldString, String newString) throws IOException {
         File file = new File(filePath);
         BufferedReader reader = new BufferedReader(new FileReader(file));
         String line = "", oldtext = "";
         while((line = reader.readLine()) != null) {
             oldtext += line + "\r\n";
         }
         reader.close();

         String newtext = oldtext.replace(oldString, newString);
         FileWriter writer = new FileWriter(filePath);
         writer.write(newtext);writer.close();
    }
    
    public static void writeFile(String filePath, String fileContent) throws IOException {
        File file = new File(filePath);
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();
    }
    
    public static void writeTempFile(String filePath, String fileContent) throws IOException {
        File file = new File(filePath);
        file.deleteOnExit();
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();
    }
    
    /** The size of blocking to use */
    protected static final int BLKSIZ = 8192;
    
    /** Copy a data file from one filename to another, alternate
    method.
    * As the name suggests, use my own buffer instead of letting
    * the BufferedReader allocate and use the buffer.
    */
    public void copyFileBuffered(String inName, String outName) throws
        FileNotFoundException, IOException {
        InputStream is = new FileInputStream(inName);
        OutputStream os = new FileOutputStream(outName);
        int count = 0; // the byte count
        byte b[] = new byte[BLKSIZ]; // the bytes read from the file
        while ((count = is.read(b)) != -1) {
            os.write(b, 0, count);
        }
        is.close( );
        os.close( );
    }
    
    /** Read the entire content of an Reader into a String */
    public static String readerToString(Reader is) throws IOException {
        StringBuffer sb = new StringBuffer( );
        char[] b = new char[BLKSIZ];
        int n;
        // Read a block. If it gets any chars, append them.
        while ((n = is.read(b)) > 0) {
            sb.append(b, 0, n);
        }
        // Only construct the String object once, here.
        return sb.toString( );
    }
    
    /** Read the content of a Stream into a String */
    public static String inputStreamToString(InputStream is) throws IOException {
        return readerToString(new InputStreamReader(is));
    }
    
    /** Read the content of a Stream into a String */
    public static String inputStreamToString(InputStream is, String encoding) throws IOException {
        InputStreamReader isr = new InputStreamReader(is, encoding);
        return readerToString(isr);
    }
    
    public static byte[] inputStreamToBytes(InputStream is/*File file*/) throws IOException {
        //InputStream is = new FileInputStream(file);
        
        // Get the size of the file
        long length = is.available()/*file.length()*/;
    
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file."/*+file.getName()*/);
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    
    public static void deleteFullDirectory(String path) {
        File f = new File(path);
        String[] flist = f.list();
        for (int i = 0; i < flist.length; i++) {
            File ff = new File(f, flist[i]);
            
            if (ff.isDirectory()) { // Recursive
                deleteFullDirectory(ff.getPath());
            }
            ff.delete();
        }
        f.delete();
    }
    
    /*public static void main(String[] av) {
        try {
            FileIO.forceCopyFile("D:/USUARIOS/Martin/pros/proyectos/nbautobuild-wizard/src/nbautobuild/core/FileIO.java", "C:/Users/Maylen/Desktop/prueba aaa/bbb/FileIO.bak");
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }*/
}
