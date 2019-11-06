package edu.mit.coeus.organization.bean;
import edu.mit.coeus.gui.CoeusMessageResources;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;
import java.util.Date.*;

public class TableSortingTool implements TableModel,TableModelListener{
    private DefaultTableModel curModel;
    private int rowIndexes[];         //This is to store the row indexes.
    protected boolean sortAsc;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    public TableSortingTool(DefaultTableModel model) {
        //this.sortAsc = Ascending;
        coeusMessageResources = CoeusMessageResources.getInstance();
        if (model == null) throw new IllegalArgumentException(
                                    coeusMessageResources.parseMessageKey(
                                            "tblSortTool_exceptionCode.1150"));

        this.curModel = model;
        curModel.addTableModelListener(new TableModelListener(){
            public void tableChanged(TableModelEvent evnt){
                allocIndexes();
            }
        });
        allocIndexes();
    }                //get all row indexes in rowIndexes variable
    public Object getValueAt( int row,int col) {
        return curModel.getValueAt(rowIndexes[row],col);
    }
    public void setValueAt(Object cellValue,int row,int col) {
        curModel.setValueAt(cellValue,rowIndexes[row],col);
    }
    public void tableChanged(TableModelEvent evnt){
        allocIndexes();
    }
    public void sort(int col) {

        int rowCount = getRowCount();
        for (int i = 0;i < rowCount; i++) {
            for(int j = i + 1; j < rowCount ; j++) {
                if ((sortAsc) ? (compareCells(rowIndexes[i],rowIndexes[j],col) > 0) :
                    (compareCells(rowIndexes[i],rowIndexes[j],col) < 0 )){
                        swap(i,j);
                        //if Row i value is greater than row j value swap two rows else leave as it is .
                    }
            }
        }

    }

    public void swap(int i,int j) {
        int tmp = rowIndexes[i];
        rowIndexes[i] = rowIndexes[j];
        rowIndexes[j] = tmp;
    }

    //This method is to compare the cell values to sort.Get the column class and compare the values...

    public int compareCells(int row1,int row2,int col) {
        Object objCell1 = curModel.getValueAt(row1,col);
        Object objCell2 = curModel.getValueAt(row2,col);

        Class colType = curModel.getColumnClass(col);

        //check for nulls
        if ( objCell1 == null && objCell2 == null ) {
            return 0;
        } else if( objCell1 == null ) {
            return -1;
        } else if(objCell2 == null) {
            return 1;
        }
        if (colType.getSuperclass() == java.lang.Number.class){


            Number n1 = (Number)curModel.getValueAt(row1,col);
            double  d1 = n1.doubleValue();
            Number n2 = (Number)curModel.getValueAt(row2,col);
            double  d2 = n2.doubleValue();



            if ( d1 < d2 ){

                return -1;
            } else if ( d1 > d2 ) {

                return 1;
            } else {

                return 0;
            }
        }
        else if (colType == java.util.Date.class) {

            Date d1 = (Date) curModel.getValueAt(row1,col);
            long n1 = d1.getTime();
            Date d2 = (Date) curModel.getValueAt(row2,col);
            long n2 = d2.getTime();
            if (n1 < n2) {
                return -1;
            } else if (n1 > n2 ) {
                return 1;
            }  else {
                return 0;
            }
        } else if ( colType == String.class ) {

            String s1 = (String)curModel.getValueAt(row1,col);
            String s2 = (String)curModel.getValueAt(row2,col);
            int result = s1.compareTo(s2);

            return (result < 0) ? -1 : ((result > 0) ? 1:0);

        } else if (colType == Boolean.class) {
            Boolean bool1 = (Boolean)curModel.getValueAt(row1,col);
            boolean b1 = bool1.booleanValue();
            Boolean bool2 = (Boolean)curModel.getValueAt(row2,col) ;
            boolean b2 = bool2.booleanValue();

            return (b1 == b2) ? 0 : ( b1 ? 1 : -1);

        } else {
            Object obj1 = curModel.getValueAt(row1,col);
            String s1 = obj1.toString();
            Object obj2 = curModel.getValueAt(row2,col);
            String s2 = obj2.toString();
            int result = s1.compareTo(s2);

            return (result < 0) ? -1 : ((result > 0) ? 1:0);
        }

    } //method CompareCells ends here




    private void allocIndexes(){
        rowIndexes = new int[getRowCount()];
        for(int i = 0 ; i < getRowCount() ; i++){
            rowIndexes[i] = i;
        }
    }


    //Table Model pass thru methods...over riding..
    public void addRow(Vector rowData) {
        curModel.addRow(rowData);
    }
    public void removeRow(int rowNum) {
        curModel.removeRow(rowNum);
    }
    public int getRowCount() {
        return curModel.getRowCount();
    }
    public int getColumnCount() {
        return curModel.getColumnCount();
    }
    public String getColumnName(int colIndex) {
        return curModel.getColumnName(colIndex);
    }
    public Class getColumnClass(int colIndex) {
        return curModel.getColumnClass(colIndex);
    }
    public boolean isCellEditable(int rowIndex,int colIndex) {
        return curModel.isCellEditable(rowIndex,colIndex);
    }
    public void addTableModelListener(TableModelListener tl) {
        curModel.addTableModelListener(tl);
    }
    public void removeTableModelListener(TableModelListener tl) {
        curModel.removeTableModelListener(tl);
    }
    public void addMouseListenerToHeaderInTable(JTable table) {
        final TableSortingTool sorter = this;
        final JTable tableView = table;
        tableView.setColumnSelectionAllowed(false);
        MouseAdapter listMouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);
                if (e.getClickCount() == 1 && column != -1) {
                    int shiftPressed = e.getModifiers()&InputEvent.SHIFT_MASK;
                    boolean ascending = (shiftPressed == 0);
                    sortAsc  = ascending;
                    sorter.sort(column);
                }
            }
        };
        JTableHeader th = tableView.getTableHeader();
        th.addMouseListener(listMouseListener);

    }

}
