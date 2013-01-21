/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

/**
 *
 * @author mproenza
 */
public class TableModelEditorManager {
    public static void showTableModelEditorToEdit(TableModelEditor editorScreen, Object model, int editingRow) {
        editorScreen.putEditionMode(editingRow, model);
        editorScreen.show();
    }
}
