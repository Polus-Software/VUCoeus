
package edu.mit.coeus.utils;

import java.awt.Component;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;


public class CurrencyEditor extends AbstractCellEditor implements TableCellEditor {

    private String colName;

    private JComponent currencyComponent = null;

    public CurrencyEditor(String colName) {
        this.colName = colName;
        currencyComponent = new CurrencyField();
    }

    /**
     * An overridden method to set the editor component in a cell.
     * @param table - the JTable that is asking the editor to edit; can be null
     * @param value - the value of the cell to be edited; it is up to the
     * specific editor to interpret and draw the value.
     * For example, if value is the string "true", it could be rendered as a
     * string or it could be rendered as a check box that is checked. null is a
     * valid value
     * @param isSelected - true if the cell is to be rendered with highlighting
     * @param row - the row of the cell being edited
     * @param column - the column of the cell being edited
     * @return the component for editing
     */
    public Component getTableCellEditorComponent(JTable table,Object value,
        boolean isSelected,
    int row,int column){

        String currencyValue ="";
        if (value != null) {
            currencyValue = value.toString();
        }
        ((CurrencyField)currencyComponent).setText(currencyValue);
//        currencyComponent = new CurrencyField(currencyValue);

        return currencyComponent;
    }

    /**
     * Forwards the message from the CellEditor to the delegate.
     * @return true if editing was stopped; false otherwise
     */
    public boolean stopCellEditing() {

        try {

            String editingValue = (String)getCellEditorValue();

            if( (editingValue == null ) || (editingValue.trim().length()== 0 )){
                return super.stopCellEditing();
            }

        }
        catch(ClassCastException exception) {
            return false;
        }
        return super.stopCellEditing();
    }

    /** Returns the value contained in the editor.
     * @return the value contained in the editor
     */
    public Object getCellEditorValue() {
        return ((JTextField)currencyComponent).getText();
    }

    /**
     * Invoked when an cell has been selected or deselected by the user.
     * The code written for this method performs the operations that need to
     * occur when an cell is selected (or deselected).
     * @param e an ItemEvent.
     */
    public void itemStateChanged(ItemEvent e) {
        super.fireEditingStopped();
    }
}
