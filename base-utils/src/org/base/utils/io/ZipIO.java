/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author mproenza
 */
public class ZipIO {

    public static void zipDir(String dir, ZipOutputStream zos, String removeFromPath) throws IOException {
        // Fix params
        if(!removeFromPath.endsWith("/"))removeFromPath += "/";
        dir = dir.replace("\\", "/");
        removeFromPath = removeFromPath.replace("\\", "/");
        
        File f = new File(dir);
        String[] flist = f.list();
        for (int i = 0; i < flist.length; i++) {
            
            File ff = new File(f, flist[i]);
            
            if (ff.isDirectory()) { // Recursive
                zipDir(ff.getPath(), zos, removeFromPath);
                continue;
            }
            
            String filepath = ff.getPath().replace("\\", "/").replace(removeFromPath, "");
            ZipEntry entries = new ZipEntry(filepath);
            zos.putNextEntry(entries);
            
            FileInputStream fis = new FileInputStream(ff);
            int buffersize = 1024;
            byte[] buffer = new byte[buffersize];
            int count;
            while ((count = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, count);
            }
            fis.close();
        }
    }
}
