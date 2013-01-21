/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.util.directories;

import java.io.File;

/**
 *
 * @author Martin
 */
public class DirectoriesUtility {
    
    public static String getDesktopPath() {
        return formatPath(System.getProperty("user.home") + "/Desktop");
    }
    
    public static String getUserDirectory() {
        return formatPath(System.getProperty("user.home"));
    }
    
    public static String getJarFolderPath() {
        String jarPath = formatPath(cleanPath(new File(ClassLoader.getSystemResource("").getPath()).getPath()));
        
        //path cleaning
        /*if(jarPath.startsWith("file:"))
            jarPath = jarPath.substring(5, jarPath.length());*/
        if(jarPath.contains(".jar!")) {//release cleaning
            int index = jarPath.indexOf(".jar!");
            jarPath = jarPath.substring(0, index);
            jarPath = jarPath.substring(0, jarPath.lastIndexOf("/"));
        }
        else if(jarPath.contains("/build/classes")) {//development cleaning
            int index = jarPath.indexOf("/build/classes");
            jarPath = jarPath.substring(0, index);
        }
        jarPath = jarPath.replace("%20", " ");//space character fix
        
        return jarPath;
    }
    
    public static String getJavaHomePath() {
        String javaHomePath = formatPath(System.getProperty("java.home"));
        
        //FIX1:
        if(javaHomePath.endsWith("/jre")) javaHomePath = javaHomePath.substring(0, javaHomePath.length() - "/jre".length());
        
        //FIX2: Windows seems to confuse jdk with jre when executing the jar
        String [] splitPath = javaHomePath.split("/");
        if(splitPath[splitPath.length - 1].contains("jre")) {
            splitPath[splitPath.length - 1] = splitPath[splitPath.length - 1].replace("jre", "jdk");
            javaHomePath = "";
            String separator = "";
            for (String str : splitPath) {
                javaHomePath += separator + str;
                separator = "/";
            }
        }

        return javaHomePath;
    }
    
    public static String cleanPath(String path) {
        if(path.startsWith("file:"))
            path = path.substring("file:".length(), path.length());
        
        return path;
    }
    
    public static String formatPath(String path) {
        if(path.startsWith("\\")) path = path.substring(1, path.length());//windows fix (release) 
        path = path.replace("\\", "/");//windows fix
        if(path.contains("\"")) path = path.replace("\"", "");//windows fix
        //path = "\"" + path + "\"";
        return path;
    }
}
