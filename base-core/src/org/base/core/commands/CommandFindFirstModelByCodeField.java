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
import org.base.dao.filters.FilterSimple;

/**
 *
 * @author martin
 */
public class CommandFindFirstModelByCodeField implements ICommand {

    //llave principal
    private JTextComponent objCampoCodigo;
    private String nombreCampoCodigo;
    
    //filtros
    private List<IFilter> lstFiltros = new ArrayList<IFilter>();
    
    //recibidor
    private IModelReceiver objRecibidor;
    
    //servicio
    private IEntityFinder nomencladorSE;   
    
    public CommandFindFirstModelByCodeField(String entityAlias, 
                                          JTextComponent objCampoCodigo,
                                          String nombreCampoCodigo,
                                          IModelReceiver objRecibidor) {        
        this.objCampoCodigo = objCampoCodigo;
        this.nombreCampoCodigo = nombreCampoCodigo;
        this.objRecibidor = objRecibidor;        

        this.nomencladorSE = RegistryEntityManager.getEntityManager(entityAlias);        
    }
    
    public CommandFindFirstModelByCodeField(String strNombreModelo, 
                                          JTextComponent objCampoCodigo,
                                          String nombreCampoCodigo,
                                          IModelReceiver objRecibidor,
                                          List<IFilter> lstFiltros) {        
        this(strNombreModelo, objCampoCodigo, nombreCampoCodigo, objRecibidor);
        this.lstFiltros = lstFiltros;
    }
    
    @Override
    public void execute() {        
        //TODO: Check the type of text component so as to act accordingly
        String key = objCampoCodigo.getText().replace(".", "");
        
        List<Object> lstModelos = new ArrayList<Object>();
        if(key != null && !key.equals("")) {
            //adicionar filtro por campo codigo
            IFilter objFiltroCodigo = new FilterSimple(nombreCampoCodigo, key);
            lstFiltros.add(objFiltroCodigo); 
            
            //obtener listado filtrado
            lstModelos = nomencladorSE.findAll(lstFiltros);
            
            //quitar filtro de codigo
            lstFiltros.remove(lstFiltros.size() - 1);
        }       
        
        //enviar al recibidor el primer modelo de la lista (si existe)
        if(lstModelos.isEmpty()) {
            objRecibidor.receiveModel(null);
        }
        else {
            objRecibidor.receiveModel(lstModelos.get(0));
        }        
    }
    
}
