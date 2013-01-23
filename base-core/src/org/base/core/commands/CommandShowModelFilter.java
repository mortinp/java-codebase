/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.commands;

import java.util.ArrayList;
import java.util.List;
import org.base.dao.filters.IFilter;
import org.base.components.models.TableMO;
import org.base.core.presentation.screens.extensions.FiltroModeloTabla;
import org.base.core.presentation.util.ViewModelUtils;

/**
 *
 * @author martin
 */
public class CommandShowModelFilter implements ICommand {  
    
    public static final int BUSCAR_AL_CREAR_COMANDO = 0;
    public static final int BUSCAR_AL_MOSTAR_FILTRO= 1;
    
    private int modoBusqueda = BUSCAR_AL_MOSTAR_FILTRO;//default
    
    private FiltroModeloTabla filtroModeloTabla;
    private String nombreModelo;
    
    //filtros (opcional)
    private List<IFilter> listaFiltros = new ArrayList<IFilter>();
    
    public CommandShowModelFilter(String nombreModelo, FiltroModeloTabla filtroModeloTabla) {
        this.filtroModeloTabla = filtroModeloTabla;
        this.nombreModelo = nombreModelo;
    }
    
    public CommandShowModelFilter(String nombreModelo, FiltroModeloTabla filtroModeloTabla, List<IFilter> filtros) {
        this(nombreModelo, filtroModeloTabla);
        this.listaFiltros = filtros;
    }
    
    public CommandShowModelFilter( String nombreModelo, FiltroModeloTabla filtroModeloTabla, int modoBusqueda) {
        this(nombreModelo, filtroModeloTabla);
        this.modoBusqueda = modoBusqueda;
        if(modoBusqueda == BUSCAR_AL_CREAR_COMANDO) {
            llenarModeloTabla();
        }         
    }
    
    public CommandShowModelFilter(String nombreModelo, FiltroModeloTabla filtroModeloTabla, int modoBusqueda, List<IFilter> filtros) {
        this(nombreModelo, filtroModeloTabla, filtros);
        this.modoBusqueda = modoBusqueda;
        if(modoBusqueda == BUSCAR_AL_CREAR_COMANDO) {
            llenarModeloTabla();
        }         
    } 
    
    @Override
    public void execute() {         
        if(modoBusqueda == BUSCAR_AL_MOSTAR_FILTRO) {
            llenarModeloTabla();
        }
                                
        filtroModeloTabla.show();
    }
    
    private void llenarModeloTabla() {
        TableMO modeloNomenclador = ViewModelUtils.fillTableModel(nombreModelo, listaFiltros);
        filtroModeloTabla.setModeloTabla(modeloNomenclador);
    }    
}
