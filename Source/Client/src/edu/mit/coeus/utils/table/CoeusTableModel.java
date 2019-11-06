/*
 * CoeusTableModel.java
 *
 * Created on July 18, 2003, 2:41 PM
 */

package edu.mit.coeus.utils.table;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  sharathk
 */
public class CoeusTableModel extends DefaultTableModel {
    
    private String colNames[];
    private Class colTypes[];
    
    public CoeusTableModel(Class colTypes[])
    {
        this.colTypes = colTypes;
    }
    
    public CoeusTableModel(String colNames[],Class colTypes[])
    {
        this.colNames = colNames; 
        this.colTypes = colTypes;
    }

    /** Creates a new instance of CoeusTableModel */
    public CoeusTableModel(Vector data,String colNames[],Class colTypes[]) {
        this.dataVector = data;
        this.colNames = colNames;
        this.colTypes = colTypes;
    }
    
    
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    public Class getColumnClass(int columnIndex)
    {
        return colTypes[columnIndex];
    }
    
    //commented coz table might not have table headers at all.
    /*public String getColumnName(int column)
    {
        return colNames[column];
    }*/
    
    public int getRowCount() {
        if(dataVector == null)
        {
            //System.out.println("Data = Null");
            return 0;
        }
        //System.out.println("Row Count = "+dataVector.size());
        return dataVector.size();
    }
    
    public Object getValueAt(int row, int column) {
        //System.out.println("Value @ "+row+" : "+column);
        return ((Vector)dataVector.get(row)).get(column);
    }
        
    public int getColumnCount() {
        return colTypes.length;
    }
    
    public Vector getRow(int row)
    {
        return (Vector)dataVector.get(row);
    }
    
}
