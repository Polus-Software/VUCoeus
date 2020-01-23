
package edu.mit.coeus.utils;

import javax.swing.table.*; 
import javax.swing.event.TableModelListener; 
import javax.swing.event.TableModelEvent; 

import java.util.Vector;

/** 
 * In a chain of data manipulators some behaviour is common. TableMap
 * provides most of this behavour and can be subclassed by filters
 * that only need to override a handful of specific methods. TableMap 
 * implements TableModel by routing all requests to its model, and
 * TableModelListener by routing all events to its listeners. Inserting 
 * a TableMap which has not been subclassed into a chain of table filters 
 * should have no effect.
 *
 * @version 1.4 12/17/97
 * @author Philip Milne */

public class TableMap extends DefaultTableModel 
                      implements TableModelListener {
    protected TableModel model; 
    
    /**
     * This method fetches the table model.
     * @return TableModel
     */
    public TableModel getModel() {
        return model;
    }
    
    /**
     * This method sets the table model.
     * @param model TableModel
     */
    public void setModel(TableModel model) {
        this.model = model; 
        model.addTableModelListener(this); 
    }

    /** By default, implement TableModel by forwarding all messages 
     * to the model. 
     * @param aRow row number to be removed
     */
    public void removeRow(int aRow) {
        ((DefaultTableModel)model).removeRow(aRow); 
    }
       
    /**
     * this will insert a new row
     * @param aRow Row number to be inserted.
     * @param data collection of data values of the row.
     */
    public void insertRow(int aRow, Vector data) {
        ((DefaultTableModel)model).insertRow(aRow,data); 
    }
        
    /**
     * get the specific value of particular row/column
     * @param aColumn column index
     * @param aRow row index
     */
    public Object getValueAt(int aRow, int aColumn) {
        return model.getValueAt(aRow, aColumn); 
    }
       
    /**
     * set the specific value of particular row/column
     * @param aValue value to setted
     * @param aColumn column index
     * @param aRow row index
     */
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        model.setValueAt(aValue, aRow, aColumn); 
    }

    /**
     * get the row count
     * @return int row count
     */
    public int getRowCount() {
        return (model == null) ? 0 : model.getRowCount(); 
    }
    /**
     * get the column count
     * @return int column count
     */
    public int getColumnCount() {
        return (model == null) ? 0 : model.getColumnCount(); 
    }
        
    /**
     * get the column Name
     * @param aColumn column index
     * @return String name of the column
     */
    public String getColumnName(int aColumn) {
        return model.getColumnName(aColumn); 
    }

    /**
     * get the column Class 
     * @param aColumn column index
     * @return Class column class
     */
    public Class getColumnClass(int aColumn) {
        return model.getColumnClass(aColumn); 
    }
     
    /**
     * Editing flag for the specific row/column index.
     * @param row row index.
     * @param column column index
     * @return boolean True if editing enabled else false.
     */
    public boolean isCellEditable(int row, int column) { 
         return model.isCellEditable(row, column); 
    }
//
// Implementation of the TableModelListener interface, 
//
    /** By default forward all events to all the listeners. 
     * @param e TableModelEvent
     */
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }
    
    public void setDataVector(Vector dataVector, Vector columnIdentifiers) {
        if( model instanceof DefaultTableModel ) {
            ((DefaultTableModel)model).setDataVector(dataVector, columnIdentifiers);
        }else{
            super.setDataVector(dataVector, columnIdentifiers);
        }
    }

    public void setDataVector(Object[][] dataVector, Object[] columnIdentifiers) {
        if( model instanceof DefaultTableModel ) {
            ((DefaultTableModel)model).setDataVector(dataVector, columnIdentifiers);
        }else{
            super.setDataVector(dataVector, columnIdentifiers);
        }
    }
}
