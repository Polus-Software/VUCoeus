/*
 * TableSorter.java
 *
 * Created on May 12, 2003, 5:39 PM
 */

package edu.mit.coeus.utils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.event.*;

import java.util.*;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * A sorter for TableModels. The sorter has a model (conforming to TableModel)
 * and itself implements TableModel. TableSorter does not store or copy
 * the data in the TableModel, instead it maintains an array of
 * integers which it keeps the same size as the number of rows in its
 * model. When the model changes it notifies the sorter that something
 * has changed eg. "rowsAdded" so that its internal array of integers
 * can be reallocated. As requests are made of the sorter (like
 * getValueAt(row, col) it redirects them to its model via the mapping
 * array. That way the TableSorter appears to hold another copy of the table
 * with the rows in a different order. The sorting algorthm used is stable
 * which means that it does not move around rows when its comparison
 * function returns 0 to denote that they are equivalent.
 *
 * @version 1.5 12/17/97
 * @author Geo Thomas
 * 
 * @version 1.6 20/02/2003
 *
 * @Updater Subramanya For Change Request, Feb' 20 2003.
 * Sorting should NOT be case sensitive in all the base Windows. Currently, it is
 * case sensitive. ie small case appears first and follows by the upper case.
 */

public class TableSorter extends TableMap {
    int indexes[];
    Vector sortingColumns = new Vector();
    boolean ascending = true;
    int compares;

    DateUtils dtUtils = new DateUtils();
    private java.text.SimpleDateFormat dtFormat
        = new java.text.SimpleDateFormat("MM/dd/yyyy");
    //holds the Sorting data value comparsion is Case Sensitive or Not.
    boolean         isCaseSensitive = true;
    
    protected int sortedColumn = 0;
    Vector tables = new Vector();
    private JTable currentTable;
    private boolean sortRequired = true;
    /**
     * This will construct the new Table Sorter
     */
    public TableSorter() {
        indexes = new int[0]; // for consistency
    }
    
    /**
     * This will construct the new Table Sorter
     * @param model TableModel
     */
    public TableSorter(TableModel model) {
        setModel(model);
    }
    /**
     * This will construct the new Table Sorter
     * @param model TableModel
     */
    public TableSorter(TableModel model, boolean isCaseSensitive ) {
        this.isCaseSensitive = isCaseSensitive;
        setModel(model);
        //System.out.println("indexes in constructor:"+getIndexesAsString());
    }

    /**
     * This will set the Table model
     * @param model TableModel
     */
    public void setModel(TableModel model) {
        super.setModel(model);
        reallocateIndexes();
    }
    
    /**
     * This method will compare different rows and associated value
     * @param row1 comparing operand
     * @param row2 comparing operator
     * @param column column index
     * @return int 0-2 for compared value(<,>, =)
     */
    public int compareRowsByColumn(int row1, int row2, int column) {
        Class type = model.getColumnClass(column);
        TableModel data = model;
        
        // Check for nulls.
        
        Object o1 = data.getValueAt(row1, column);
        Object o2 = data.getValueAt(row2, column);
        
        // If both values are null, return 0.
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) { // Define null less than everything.
            return -1;
        } else if (o2 == null) {
            return 1;
        }
        
        /*
         * We copy all returned values from the getValue call in case
         * an optimised model is reusing one object to return many
         * values.  The Number subclasses in the JDK are immutable and
         * so will not be used in this way but other subclasses of
         * Number might want to do this to save space and avoid
         * unnecessary heap allocation.
         */
        
        if (type.getSuperclass() == java.lang.Number.class) {
            Number n1 = (Number)data.getValueAt(row1, column);
            double d1 = n1.doubleValue();
            Number n2 = (Number)data.getValueAt(row2, column);
            double d2 = n2.doubleValue();
            
            if (d1 < d2) {
                return -1;
            } else if (d1 > d2) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == java.util.Date.class) {
            Date d1 = (Date)data.getValueAt(row1, column);
            long n1 = d1.getTime();
            Date d2 = (Date)data.getValueAt(row2, column);
            long n2 = d2.getTime();
            
            if (n1 < n2) {
                return -1;
            } else if (n1 > n2) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == String.class) {
            String s1 = (String)data.getValueAt(row1, column);
            String s2    = (String)data.getValueAt(row2, column);
            int result = 0;
            /* added on 1-APR-2003 to fix the bug : date sorting */
            /* try to convert the column data to date. if it is a valid date
             * compare the two rows as dates else do string comparison
             */
            String rest1 = dtUtils.restoreDate(s1,"/:-,");
            String rest2 = dtUtils.restoreDate(s2,"/:-,");
            if(dtUtils.validateDate(rest1,"/:-,") && dtUtils.validateDate(rest2,"/:-,")){
                Date d1,d2;
                long n1=0,n2=0;
                try{
                    d1 = dtFormat.parse(rest1);
                    d2 = dtFormat.parse(rest2);
                    n1 = d1.getTime();
                    n2 = d2.getTime();
                }catch (java.text.ParseException pe){
                    d1 = new Date();
                    d2 = new Date();
                }
                if (n1 < n2) {
                    return -1;
                } else if (n1 > n2) {
                    return 1;
                } else {
                    return 0;
                }
            }
            /* end : bug fix: date sorting */
            
            if( isCaseSensitive ){
                result = s1.compareTo(s2);
            }else{
                result = s1.compareToIgnoreCase( s2 );
            }
            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == Boolean.class) {
            Boolean bool1 = (Boolean)data.getValueAt(row1, column);
            boolean b1 = bool1.booleanValue();
            Boolean bool2 = (Boolean)data.getValueAt(row2, column);
            boolean b2 = bool2.booleanValue();
            
            if (b1 == b2) {
                return 0;
            } else if (b1) { // Define false < true
                return 1;
            } else {
                return -1;
            }
        } else {
            Object v1 = data.getValueAt(row1, column);
            String s1 = v1.toString();
            Object v2 = data.getValueAt(row2, column);
            String s2 = v2.toString();
            int result = 0;
            int val1 = 0, val2 = 0;
            /**
             * checking the object contains number value  to implement number sorting.
             */
            try{
                val1 = Integer.parseInt( s1 );
                val2 = Integer.parseInt( s2 );
                if (val1 < val2) {
                    return -1;
                } else if (val1 > val2) {
                    return 1;
                } else if (val1 == val2){
                    return 0;
                }
                
            }catch(NumberFormatException nfe) {

                /* added on 1-APR-2003 to fix the bug : date sorting */
                /* try to convert the column data to date. if it is a valid date
                 * compare the two rows as dates else do string comparison
                 */
                String rest1 = dtUtils.restoreDate(s1,"/:-,");
                String rest2 = dtUtils.restoreDate(s2,"/:-,");
                if(dtUtils.validateDate(rest1,"/:-,") && dtUtils.validateDate(rest2,"/:-,")){
                    Date d1,d2;
                    long n1=0,n2=0;
                    try{
                        d1 = dtFormat.parse(rest1);
                        d2 = dtFormat.parse(rest2);
                        n1 = d1.getTime();
                        n2 = d2.getTime();
                    }catch (java.text.ParseException pe){
                        d1 = new Date();
                        d2 = new Date();
                    }
                    if (n1 < n2) {
                        return -1;
                    } else if (n1 > n2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
                /* end : bug fix: date sorting */

                if( this.isCaseSensitive ) {
                    result = s1.compareTo(s2);
                }else{
                    result = s1.compareToIgnoreCase(s2);
                }

                if (result < 0) {
                    return -1;
                } else if (result > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }
    
    /**
     * This method will compare different rows and associated value
     * @param row1 comparing operand
     * @param row2 comparing operator    
     * @return int 
     */
    public int compare(int row1, int row2) {
        compares++;
        for (int level = 0; level < sortingColumns.size(); level++) {
            Integer column = (Integer)
            sortingColumns.elementAt(level);
            int result = compareRowsByColumn(row1, row2, column.intValue());
            if (result != 0) {
                return ascending ? result : -result;
            }
        }
        return 0;
    }

    /**
     * this method reallocates the indexes.
     */
    public void reallocateIndexes() {
        int rowCount = model.getRowCount();
        
        // Set up a new array of indexes with the right
        //number of elements
        // for the new data model.
        indexes = new int[rowCount];
        
        // Initialise with the identity mapping.
        for (int row = 0; row < rowCount; row++) {
            indexes[row] = row;
        }
    }
    
    /**
     * Table changed eventes registed in this method
     * @param e TableModelEvent
     */
    public void tableChanged(TableModelEvent e) {
        //System.out.println("Sorter: tableChanged");
        //if( sortedColumn == e.getColumn() ){
            //System.out.println("indexes in table changed starting:"+getIndexesAsString());
            if( sortRequired ) {
                reallocateIndexes();
                sortByColumn(sortedColumn, ascending);
            }
            super.tableChanged(e);
            //System.out.println("indexes in table changed end:"+getIndexesAsString());
        //}
        
    }
    
    /**
     * This method will the check the table model
     */
    public void checkModel() {
        if (indexes.length != model.getRowCount()) {
            tableChanged(new TableModelEvent(this));
            // System.err.println("Sorter not informed
            //of a change in model.");
        }
    }
    
    /**
     * This method is used to sort the table column
     * @param sender asending/descending parameter
     */
    public void sort(Object sender) {
        checkModel();
        compares = 0;
        shuttlesort((int[])indexes.clone(), indexes, 0, indexes.length);
    }

    /**
     * This is supporting method for sorting.
     */
    public void n2sort() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = i+1; j < getRowCount(); j++) {
                if (compare(indexes[i], indexes[j]) == -1) {
                    swap(i, j);
                }
            }
        }
    }
    
    /** This is a home-grown implementation which we have not had time
     *  to research - it may perform poorly in some circumstances. It
     *  requires twice the space of an in-place algorithm and makes
     *  NlogN assigments shuttling the values between the two
     *  arrays. The number of compares appears to vary between N-1 and
     *  NlogN depending on the initial order but the main reason for
     *  using it here is that, unlike qsort, it is stable.
     * @param from collection of from values
     * @param to collection of to values
     * @param low lower limit index
     * @param high upper limit index
     */
    public void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high)/2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);
        
        int p = low;
        int q = middle;
        
        /* This is an optional short-cut; at each recursive call, check to see 
         if the elements in this subset are already ordered. If so, no further 
         comparisons are needed; the sub-array can just be copied. The array must 
         be copied rather than assigned otherwise sister calls in the recursion might
         get out of sinc. When the number of elements is three they are partitioned 
         so that the first set, [low,mid), has one element and and the second, 
         [mid, high), has two. We skip the optimisation when the number of elements is
        three or less as the first compare in the normal merge will produce the same
        sequence of steps. This optimisation seems to be worthwhile for partially 
         ordered lists but some analysis is needed to find out how the performance 
         drops to Nlog(N) as the initial order diminishes - it may drop very quickly. */
        
        if (high - low >= 4 && compare(from[middle-1],
        from[middle]) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }
        // A normal merge.
        
        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            }
            else {
                to[i] = from[q++];
            }
        }
    }
    
    /**
     * This will swap int values.
     * @param i left operand
     * @param j right operand
     */
    public void swap(int i, int j) {
        int tmp = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = tmp;
    }
    
    /** The mapping only affects the contents of the data rows.
     * Pass all requests to these rows through the mapping array: "indexes".
     * @param aRow row index
     * @param aColunn colunn index
     * @return Object value
     */
    public Object getValueAt(int aRow, int aColumn) {
        try {
            checkModel();
            return model.getValueAt(indexes[aRow], aColumn);
        } catch (Exception e) {
            System.err.println(e);
        }
        return "<BAD INDEX " + aRow + ">";
    }
    
    /**
     * This will set the value of the cell in table.
     * @param aRow row index
     * @param aColunn colunn index
     * @param aValue Object value
     */
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        //int tableIndex = Integer.parseInt((String) currentTable.getValueAt(aRow,
        //                    currentTable.getColumnCount() -1 ) );
        
        //model.setValueAt(aValue, indexes[aRow], aColumn);
        model.setValueAt(aValue,aRow,aColumn);
        /*if( aColumn == sortedColumn ) {
            tableChanged(new TableModelEvent(this));
        }*/
        if( sortRequired ) {
            int viewIndex = getViewIndex( aRow );  //getViewIndex( tableIndex ); 
            if( viewIndex != -1 && viewIndex < currentTable.getRowCount() ) {
                currentTable.setRowSelectionInterval(viewIndex, viewIndex );
            }
        }
    }
    public void setSortingRequired(boolean sortRequired ) {
        this.sortRequired = sortRequired ;
    }
    /**
     * This will sort by column index
     * @param column column index
     */
    public void sortByColumn(int column) {
        sortByColumn(column, true);
    }
    
    /**
     * This will sort by column index
     * @param column column index
     * @param ascending type boolean 
     */
    public void sortByColumn(int column, boolean ascending) {
        // stop cell editing in tables
        //System.out.println("indexes in sortByCol start:"+getIndexesAsString());
        Enumeration enums = tables.elements();
        while (enums.hasMoreElements()) {
            JTable table = (JTable)enums.nextElement();
            TableCellEditor editor =
            table.getCellEditor();
            if (editor != null)
                editor.cancelCellEditing();
        }
        this.ascending = ascending;
        sortedColumn = column;
        sortingColumns.removeAllElements();
        sortingColumns.addElement(new Integer(column));
        sort(this);
        super.tableChanged(new TableModelEvent(this));
        //System.out.println("indexes in startByCol end:"+getIndexesAsString());        
    }
    
    /** There is no-where else to put this.
     *  Add a mouse listener to the Table to trigger a table sort
     *  when a column heading is clicked in the JTable.
     * @param table JTable.
     */
    public void addMouseListenerToHeaderInTable(JTable table) {
        final TableSorter sorter = this;
        final JTable tableView = table;
        tableView.setColumnSelectionAllowed(false);
        MouseAdapter listMouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel =
                tableView.getColumnModel();
                int viewColumn =
                columnModel.getColumnIndexAtX(e.getX());
                int column =
                tableView.convertColumnIndexToModel(viewColumn);
                if (e.getClickCount() == 1 && column != -1) {
                    int selRow = currentTable.getSelectedRow();
                    int tableHiddenIndex = 0;
                    boolean tableSelected = false;
                    if( selRow != -1 ) {
                        tableSelected = true;
                        int tempIndex = 0;
                        try{ 
                            tempIndex = Integer.parseInt((String) currentTable.getValueAt(
                                                selRow,currentTable.getColumnCount() -1 ) );
                        }catch(NumberFormatException nfe){
                            tableSelected = false;
                        }
                        tableHiddenIndex = tempIndex;
                    }
                    if( sortedColumn != column ) {
                        // resetting ascending flag if user clicked on any other column
                        // so that it will be sorted in ascending order.
                        ascending = true;
                    }else{
                        ascending = !ascending;
                    }
                    sorter.sortByColumn(column, ascending);
                    if( tableSelected ) {
                        selRow = getViewIndex(tableHiddenIndex);
                        currentTable.setRowSelectionInterval(selRow,selRow);
                    }
                   
                }
            }
        };
        JTableHeader th = tableView.getTableHeader();
        th.addMouseListener(listMouseListener);
        tables.add(table);
        currentTable = table;
    }
    /**
     * This method will return the index value
     * for a specified row in the table.
     * @return int
     * @param row int
     */
    public int getIndexForRow(int aRow) {
        return indexes[aRow];
    }
    
    /**
     * This method will return the index value for a specified row in the table.
     * @return int
     * @param row int
     */
    public int[] getIndexForRows(int[] indexs) {
        int[] myArray = new int[indexs.length];
        int size = indexs.length;
        for (int i = 0; i < size ; i++) {
            myArray[i] = getIndexForRow(indexs[i]);
            
        }
        return myArray;
    }
    /**
     * This method will insert the specified row vector at the mapped index of the
     * model for the given table row index.
     * @param row integer representing the required insert position in table.
     * @param data Vector containing the row data.
     */
    public void insertRow(int row, Vector data) {
        if( row < indexes.length ) {
            super.insertRow( indexes[row], data );
        }else{
            super.insertRow( row, data );
        }
       //super.insertRow( row, data );
       //System.out.println("indexes in insert row:"+getIndexesAsString());
       int lastRow = getInsertedRowPosition();
       currentTable.setRowSelectionInterval(lastRow,lastRow);
    }
    
    public void removeRow(int row ) {
        
        super.removeRow(row);
    }
    private String getIndexesAsString() {
        StringBuffer strIndexs = new StringBuffer();
        for( int i=0; i< indexes.length; i++ ){
            strIndexs.append( indexes[i]+"," );
        }
        return strIndexs.toString();
    }
    
    // should be called after adding / inserting a new row only
    private int getInsertedRowPosition(){
        int lastRow = currentTable.getRowCount() -1;
        for( int i=0; i< indexes.length; i++ ){
            if( lastRow == indexes[i] ) {
                return i;
            }
        }
        return 0;
    }
    
    private int getViewIndex( int row ) {
        int rowCount = currentTable.getRowCount();
        int tempIndex = 0;
        for( int i=0; i< rowCount; i++ ){
            try{ 
                tempIndex = Integer.parseInt((String) currentTable.getValueAt(
                                    i,currentTable.getColumnCount() -1 ) );
                if( row == tempIndex ) {
                    return i;
                }
            }catch(NumberFormatException nfe){
                return 0;
            }
        }
        return 0;
    
    }
}
