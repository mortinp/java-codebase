/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth.config;

import java.util.logging.Logger;
import org.base.dao.datasources.context.DataSourceContext;

/**
 *
 * @author Martin
 */
public class AuthEntryPoint {
    
    public static DataSourceContext dataSourceContext;
    public static Logger logger = Logger.getLogger("security_logger");
}
