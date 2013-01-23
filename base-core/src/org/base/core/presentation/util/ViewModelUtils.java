/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.base.dao.DAOFactory;
import org.base.dao.IDAO;
import org.base.exceptions.system.SystemException;
import org.base.dao.filters.IFilter;
import org.base.components.models.ComboBoxMO;
import org.base.components.models.TableMO;
import org.base.components.models.parsing.DefaultTableModelDataExtractor;

/**
 *
 * @author leo
 */
public class ViewModelUtils {

    // <editor-fold defaultstate="collapsed" desc="DECLARACION DE VARIABLES">
    
    private static final String DEFAULT_CONFIG_FILE_PATH = "org/base/core/config/models_views.properties";
    private static Properties propertiesFile = null;
    
    static class TableViewConfig {
        public List listaColumnas = new ArrayList();
        public List listaAtributos = new ArrayList();
        public List listaTipos = new ArrayList();
        public List listaLongitudes = new ArrayList();
    }
    
    public static class ColumnaGrid {
        public String atributo;
        public String descripcion;
        public String tipo;
        public int longitud;
        
        public ColumnaGrid(String atributo, String descripcion, String tipo, int longitud) {
            this.atributo = atributo;
            this.descripcion = descripcion;
            this.tipo = tipo;
            this.longitud = longitud;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="METODOS DE SERVICIO">
    public static ComboBoxMO fillComboboxModel(String nomenclador) {
        ComboBoxMO modelo = new ComboBoxMO();
        List lstLista = getDAO(nomenclador).findAll();
        for (Object object : lstLista) {
            modelo.addElement(object);
        }
        return modelo;
    }
    
    /*public static ComboBoxMO fillComboboxModel(String nomenclador, List<IFilter> listaFiltros) {
        ComboBoxMO modelo = new ComboBoxMO();
        InterfaceDAO objDao = getDAO(nomenclador);
        objDao.setFilters(listaFiltros);  
        ArrayList lstLista = objDao.findAll();
        for (Object object : lstLista) {
            modelo.addElement(object);
        }
        return modelo;
    }*/
    
    public static ComboBoxMO fillComboboxModel(String nomenclador, IFilter ... listaFiltros) {
        ComboBoxMO modelo = new ComboBoxMO();
        IDAO objDao = getDAO(nomenclador);
        objDao.setFilters(listaFiltros);  
        List lstLista = objDao.findAll();
        for (Object object : lstLista) {
            modelo.addElement(object);
        }
        return modelo;
    }

    public static TableMO fillTableModel(String nomenclador) {
        TableViewConfig objConfigTabla = getTableViewConfig(nomenclador);     
        TableMO modelo = getTableModelFromConfig(objConfigTabla);

        List lstLista = getDAO(nomenclador).findAll();
        modelo.setRowObjects(lstLista);

        return modelo;
    }
    
    public static TableMO fillTableModel(String nomenclador, List<IFilter> listaFiltros) {        
        TableViewConfig objConfigTabla = getTableViewConfig(nomenclador);       
        TableMO modelo = getTableModelFromConfig(objConfigTabla);      

        IDAO objDao = getDAO(nomenclador);
        objDao.setFilters(listaFiltros);        
        List lstLista = objDao.findAll();
        modelo.setRowObjects(lstLista);

        return modelo;
    }
    
    public static TableMO fillTableModel(String nomenclador, IFilter ... listaFiltros) {
        TableViewConfig objConfigTabla = getTableViewConfig(nomenclador);      
        TableMO modelo = getTableModelFromConfig(objConfigTabla);
        
        IDAO objDao = getDAO(nomenclador);
        objDao.setFilters(listaFiltros);        
        List lstLista = objDao.findAll();
        modelo.setRowObjects(lstLista);

        return modelo;
    }

    public static TableMO getEmptyTableModel(String strNombreTabla) {
        TableViewConfig objConfigTabla = getTableViewConfig(strNombreTabla);        
        return getTableModelFromConfig(objConfigTabla);        
    }
    
    public static TableMO createTableModel(List listaModelos, List<ColumnaGrid> listaColumnas) throws SystemException {
        TableViewConfig objConfigTabla = new TableViewConfig();
        for (ColumnaGrid columnaGrid : listaColumnas) {
            objConfigTabla.listaColumnas.add(columnaGrid.descripcion);
            objConfigTabla.listaAtributos.add(columnaGrid.atributo);
            objConfigTabla.listaTipos.add(columnaGrid.tipo);
            objConfigTabla.listaLongitudes.add(columnaGrid.longitud);
        }        

        TableMO modelo = getTableModelFromConfig(objConfigTabla);
        modelo.setRowObjects(listaModelos);

        return modelo;
    }
    // </editor-fold>    

    private static TableMO getTableModelFromConfig(TableViewConfig objConfigTabla) {
        TableMO modelo = new TableMO(objConfigTabla.listaColumnas, objConfigTabla.listaLongitudes, 
                                     new DefaultTableModelDataExtractor((ArrayList)objConfigTabla.listaTipos, (ArrayList)objConfigTabla.listaAtributos));
        return modelo;
    }
    
    private static TableViewConfig getTableViewConfig(String strTabla) {
        boolean noCorrectConfigFound = true;
        TableViewConfig objConfigTabla = null;
        
        List lstColumnas = new ArrayList();
        List lstAtributos = new ArrayList();
        List lstTipos = new ArrayList();
        List lstlongitudes = new ArrayList();

        String strColumnas = getPropiedades().getProperty(strTabla + "_columns", "");
        String strAtributos = getPropiedades().getProperty(strTabla + "_attributes", "");
        String strTipos = getPropiedades().getProperty(strTabla + "_types", "");
        String strLongitud = getPropiedades().getProperty(strTabla + "_colwidths", "");
        
        if(strColumnas != null && !strColumnas.isEmpty()) {
            noCorrectConfigFound = false;
        }
        
        if(noCorrectConfigFound) throw new SystemException("Configuration for table ('" + strTabla + "') was not found ... check configuration file(s)");

        StringTokenizer st = new StringTokenizer(strColumnas, "|");
        while (st.hasMoreElements()) {
            lstColumnas.add(st.nextToken());
        }
        st = new StringTokenizer(strAtributos, "|");
        while (st.hasMoreElements()) {
            lstAtributos.add(st.nextToken());
        }
        st = new StringTokenizer(strTipos, "|");
        while (st.hasMoreElements()) {
            lstTipos.add(st.nextToken());
        }
        if ((strLongitud == null) || (strLongitud.length() == 0)) {
            lstlongitudes = null;
        } else {
            st = new StringTokenizer(strLongitud, "|");
            while (st.hasMoreElements()) {
                lstlongitudes.add(Integer.parseInt(st.nextToken()));
            }
        }

        objConfigTabla = new TableViewConfig();
        objConfigTabla.listaColumnas = lstColumnas;
        objConfigTabla.listaAtributos = lstAtributos;
        objConfigTabla.listaTipos = lstTipos;
        objConfigTabla.listaLongitudes = lstlongitudes;

        return objConfigTabla;
    }

    private static Properties getPropiedades() {
        if (propertiesFile == null) {
            try {
                InputStream objArchivo = ClassLoader.getSystemResourceAsStream(DEFAULT_CONFIG_FILE_PATH);
                propertiesFile = new Properties();
                propertiesFile.load(objArchivo);
            } catch (IOException ex) {
                throw new SystemException(ex);
            }
        }
        return propertiesFile;
    }

    public static void setProperties(Properties properties) {
        propertiesFile = properties;
    }
    
    private static IDAO getDAO(String nomenclador) {
        return DAOFactory.getDAO(nomenclador);
    }
}
