/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mproenza
 */
public class StreamGobbler extends Thread {

    InputStream is;
    String type;
    OutputStream os;
    Logger logger;
    
    String messages = "";

    public StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this(is, type);
        this.os = redirect;
    }
    
    public StreamGobbler(InputStream is, String type, Logger logger) {
        this(is, type);
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            PrintWriter pw = null;
            if (os != null) {
                pw = new PrintWriter(os);
            }

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (pw != null) {
                    pw.println(line);
                }
                System.out.println(type + ">" + line);
                messages += line + "\n";
            }
            if (pw != null) {
                pw.flush();
            }
            
            if(logger != null) logger.log(Level.SEVERE, messages);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    
}
