/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.activation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.base.core.domain.Model;

/**
 *
 * @author mproenza
 */
public class UserSettings implements Model {
    
    String userId;
    String userType;
    String accountType;
    Date accountActivationDate;
    SerialKey currentSerialKey;
    
    List<SerialKey> serialKeyHistory = new ArrayList<SerialKey>();

    public Date getAccountActivationDate() {
        return accountActivationDate;
    }

    public void setAccountActivationDate(Date accountActivationDate) {
        this.accountActivationDate = accountActivationDate;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public SerialKey getCurrentSerialKey() {
        return currentSerialKey;
    }

    public void setCurrentSerialKey(SerialKey currentSerialKey) {
        this.currentSerialKey = currentSerialKey;
    }

    public List<SerialKey> getSerialKeyHistory() {
        return serialKeyHistory;
    }

    public void setSerialKeyHistory(List<SerialKey> serialKeyHistory) {
        this.serialKeyHistory = serialKeyHistory;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    } 

    @Override
    public String getKeyValueExpression() {
        return userId;
    }
    
}
