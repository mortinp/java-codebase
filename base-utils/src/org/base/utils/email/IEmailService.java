/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.email;

/**
 *
 * @author Luis Valdes Guerrero <lvaldes@grm.desoft.cu>
 */
public interface IEmailService {

    void send(String body, String from, String subject, String ... to);
    
}
