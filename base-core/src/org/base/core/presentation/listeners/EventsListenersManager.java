/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.listeners;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * @author martin
 * 
 * The <code>EventsListenersManager</code> class can be used to register, unregister or switch event listeners in a 
 * component. It manages and holds references to all the listeners added to the components, as long as this 
 * operation is made through the functions this class provides.
 * 
 * Sometimes one needs to use listeners dynamically, for instance, to be able to use one listener or other in
 * different situations, which depends on varying factors. For this, one must hold instances of listeners 
 * in order to be able to reference them later (to remove them, access them, etc). This turns out to be a difficult
 * task, since you must know very well the type of the listener in order to access it through the correct function.
 * 
 * You can use <code>EventsListenersManager</code> class to register a listener in a component, and later remove it, or switch
 * it with another listener, all this through a very simple interface and without worrying about the type of the
 * component or the listener.
 */
public class EventsListenersManager {
    
    /**
     * A map that holds all the listeners currently registered.
     * 
     * The map contains pairs which key is a unique name you assign to the listener, and the value is the
     * listener itself. You can retrieve listeners then by using it unique name.
     */
    static Map<String, EventListener> listenersMap = new HashMap<String, EventListener>();
    
    /**
     * Registers a listener in a component. The listener is mapped to a unique name as specified by the
     * parameter <code>uniqueListenerName</code>.
     */
    public static void registerEventListener(JComponent component, EventListener listener, String uniqueListenerName) {
        addEventListener(component, listener);
        listenersMap.put(uniqueListenerName, listener);
    }
    
    /**
     * Unregisters a listener from a component. The listener is retrieved by using its unique name as specified
     * by the parameter <code>uniqueListenerName</code>.
     */
    public static void unregisterEventListener(JComponent component, String uniqueListenerName) {
        EventListener listener = getListenerById(uniqueListenerName);
        if(listener != null) {
            removeEventListener(component, listener);
            listenersMap.remove(uniqueListenerName);
        }
    }
    
    /**
     * Unregisters all the listener currently registered. This means that <code>listenersMap</code> ends up
     * being empty.
     */
    public static void unregisterAll() {
        listenersMap.clear();
    }
    
    public static void enableEventListener(JComponent component, String uniqueListenerName) {
        EventListener listener = getListenerById(uniqueListenerName);
        addEventListener(component, listener);
    }
    
    public static void disableEventListener(JComponent component, String uniqueListenerName) {
        EventListener listener = getListenerById(uniqueListenerName);
        if(listener != null) {
            removeEventListener(component, listener);
        }
    }
    
    /**
     * Switches between two listeners. The listener previously registered with the name specified in parameter
     * <code>uniqueListenerName</code> is unregistered, and the new one specified in parameter <code>newListener</code>
     * is registered in the component with the same name as the previous one. From this moment, you can access the
     * new listener using the same name you used for the old listener.
     */
    public static void switchEventListener(JComponent component, EventListener newListener, String uniqueListenerName) {
        unregisterEventListener(component, uniqueListenerName);
        registerEventListener(component, newListener, uniqueListenerName);
    }
    
    private static EventListener getListenerById(String uniqueListenerName) {
        EventListener listener = listenersMap.get(uniqueListenerName);
        //if(listener == null) throw new SistemaExcepcion(new NotSuchListenerRegistered());
        return listener;
    }
    
    private static void addEventListener(JComponent component, EventListener listener) {
        if(!tryAddCommonEventListener(component, listener)) {
            //component-specific listener
            if(component instanceof JButton)
                addButtonEventListener((JButton) component, listener);
            else if(component instanceof JTextField)
                addTextFieldEventListener((JTextField) component, listener);
            else if(component instanceof JTable)
                addTableEventListener((JTable) component, listener);
        }
    }
    
    private static boolean tryAddCommonEventListener(JComponent component, EventListener listener) {
        if(listener instanceof FocusListener) {
            component.addFocusListener((FocusListener) listener);
            return true;
        }
        else if(listener instanceof KeyListener) {
            component.addKeyListener((KeyListener) listener);
            return true;
        }
        else if(listener instanceof MouseListener) {
            component.addMouseListener((MouseListener) listener);
            return true;
        }
        return false;
    }
    
    private static void removeEventListener(JComponent component, EventListener listener) {
        if(!tryRemoveCommonEventListener(component, listener)) {
            //component-specific listener
            if(component instanceof JButton)
                removeButtonEventListener((JButton) component, listener);
            else if(component instanceof JTextField)
                removeTextFieldEventListener((JTextField) component, listener);
            else if(component instanceof JTable)
                removeButtonEventListener((JTable) component, listener);
        }
    }
    
    private static boolean tryRemoveCommonEventListener(JComponent component, EventListener listener) {
        if(listener instanceof FocusListener) {
            component.removeFocusListener((FocusListener) listener);
            return true;
        }
        else if(listener instanceof KeyListener) {
            component.removeKeyListener((KeyListener) listener);
            return true;
        }
        else if(listener instanceof MouseListener) {
            component.removeMouseListener((MouseListener) listener);
            return true;
        }
        return false;
    }
    
    private static void addButtonEventListener(JButton button, EventListener listener) {
        if(listener instanceof ActionListener)
            button.addActionListener((ActionListener) listener);
        //else throw new SistemaExcepcion(new ExceptionWrongListener());
    }
    
    private static void removeButtonEventListener(JButton button, EventListener listener) {
        if(listener instanceof ActionListener)
            button.removeActionListener((ActionListener) listener);
        //else throw new SistemaExcepcion(new ExceptionWrongListener());
    }
    
    private static void addTableEventListener(JTable table, EventListener listener) {
        //else throw new SistemaExcepcion(new ExceptionWrongListener());
    }
    
    private static void removeButtonEventListener(JTable table, EventListener listener) {
        //else throw new SistemaExcepcion(new ExceptionWrongListener());
    }
    
    private static void addTextFieldEventListener(JTextField textField, EventListener listener) {
        if(listener instanceof ActionListener)
            textField.addActionListener((ActionListener) listener);
        //else throw new SistemaExcepcion(new ExceptionWrongListener());
    }
    
    private static void removeTextFieldEventListener(JTextField textField, EventListener listener) {
        if(listener instanceof ActionListener)
            textField.removeActionListener((ActionListener) listener);
        //else throw new SistemaExcepcion(new ExceptionWrongListener());
    }
}
