/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.activation;

/**
 *
 * @author mproenza
 */
public class SerialKey {
    int id;
    String key;
    
    String userId;

    public SerialKey() {
    }

    public SerialKey(int id, String key, String userId) {
        this.id = id;
        this.key = key;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    @Override
    public boolean equals(Object o) {
        return ((SerialKey)o).getKey().equals(this.getKey());
    }
}
