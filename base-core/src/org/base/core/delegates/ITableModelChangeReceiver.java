/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.delegates;

import org.base.core.exceptions.DomainException;

/**
 *
 * @author martin
 */
public interface ITableModelChangeReceiver {

    public void addModel(Object model) throws DomainException;

    public void updateModel(int index, Object newModel) throws DomainException;

    public void deleteModel(int index) throws DomainException;
}
