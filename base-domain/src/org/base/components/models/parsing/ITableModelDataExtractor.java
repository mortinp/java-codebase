/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.models.parsing;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin
 */
public interface ITableModelDataExtractor {
    
    public ArrayList<ArrayList> getDataMatrix(List lstDataObjects, int first, int last);    
    public ArrayList getDataArray(Object dataObject);
}
