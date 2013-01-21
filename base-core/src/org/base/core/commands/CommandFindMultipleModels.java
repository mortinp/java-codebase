/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.commands;

import java.util.ArrayList;
import java.util.List;
import org.base.dao.filters.IFilter;
import org.base.core.delegates.IMultipleModelsReceiver;
import org.base.core.service.EntityManager;
import org.base.core.service.IEntityFinder;


/**
 *
 * @author martin
 */
public class CommandFindMultipleModels implements ICommand {

    private IMultipleModelsReceiver receiver;
    private IEntityFinder nomencladorSE;
    //filtros
    private List<IFilter> lstFilters = new ArrayList<IFilter>();
    
    public CommandFindMultipleModels(String modelAlias, IMultipleModelsReceiver receiver) {
        this.receiver = receiver;
        this.nomencladorSE = new EntityManager(modelAlias);
    }
    
    public CommandFindMultipleModels(String modelAlias, 
                                    IMultipleModelsReceiver receiver,
                                    List<IFilter> lstFilters) {
        this( modelAlias, receiver);
        this.lstFilters = lstFilters;
    }
    
    @Override
    public void execute() {
        receiver.receiveModels(nomencladorSE.findAll(lstFilters));
    }
    
}
