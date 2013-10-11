/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public class DAOPackage {
    
    public static Logger logger = Logger.getLogger("dao_logger");
    
    public static void log(String message) {
        logger.log(Level.SEVERE, message);
    }
    
    public static void log(Exception e) {
        logger.log(Level.SEVERE, e.getMessage());
    }
    
}
