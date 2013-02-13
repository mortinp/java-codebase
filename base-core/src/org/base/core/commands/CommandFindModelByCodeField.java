/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.commands;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.base.dao.filters.IFilter;
import org.base.core.delegates.IModelReceiver;
import org.base.core.service.IEntityFinder;
import org.base.core.service.RegistryEntityManager;


/**
 *
 * @author martin
 */
public class CommandFindModelByCodeField implements ICommand {

    private JTextComponent codeField;
    private IModelReceiver receiver;
    
    private IEntityFinder nomencladorSE;
    
    //filtros
    private List<IFilter> lstFilters = new ArrayList<IFilter>();
    
    public CommandFindModelByCodeField(JTextComponent codeField,
                                       String entityAlias, 
                                       IModelReceiver receiver) {
        this.codeField = codeField;
        this.receiver = receiver;
        this.nomencladorSE = RegistryEntityManager.getEntityManager(entityAlias);
    }
    
    public CommandFindModelByCodeField(JTextComponent codeField,
                                       String modelAlias, 
                                       IModelReceiver receiver,
                                       List<IFilter> lstFilters) {
        this(codeField, modelAlias, receiver);
        this.lstFilters = lstFilters;
    }
    
    @Override
    public void execute() {
        Object objModelo = null;                        
        String key = codeField.getText()/*.replace(".", "")*/;
        if(key != null && !key.equals(""))
            objModelo = nomencladorSE.findOne((Object) key, lstFilters);

        receiver.receiveModel(objModelo);
    }
    
}
