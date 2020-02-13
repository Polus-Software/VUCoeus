/*
 * @(#)SearchResultWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on August 21, 2002, 5:52 PM
 * @author  Geo Thomas
 * @version 1.0
 */

package edu.mit.coeus.search.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.util.Hashtable;
import java.util.HashMap;
import java.util.Vector;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.search.bean.DisplayBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.TableSorter;
import edu.mit.coeus.utils.Utils;

/**
 * The class provides required APIs for the search result window customization.
 */
public class SearchResultWindow implements ListSelectionListener{
    
    //holds search result
    private Hashtable searchResult;
    //result table
    private JTable tblResult;
    //main panel
    private JPanel pnlMain;
    //selected value
    private String selectedValue;
    //selected row
    private Hashtable selectedRow;
    //result set collection
    private Vector resList;
    //CoeusSearch class instance
    private CoeusSearch coeusSearch;
    
    private int colCount;
    
    //    private TableSorter sorter; //Commented by Nadh
    
    private MultipleTableColumnSorter sorter;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /** Creates new SearchResultWindow */
    SearchResultWindow(Hashtable result,CoeusSearch coeusSearch) throws Exception{
        this.coeusSearch = coeusSearch;
        this.searchResult = result;
        tblResult = buildTable();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    /**
     *  Method used to get the search result table
     * @return search result table
     */
    JTable getSearchResTable(){
        return tblResult;
    }
    
    /**
     *  Method used to get the result records
     *  @return a collection which has instances of hashtable.
     */
    Vector getResultRecords(){
        return resList;
    }
    
    /**
     *  Method to get the selected value
     * @return selected value
     */
    String getSelectedValue() throws Exception{
        if(selectedRowIndex==-1){
            throw new Exception(coeusMessageResources.parseMessageKey(
            "searchResultWin_exceptionCode.1109"));
        }
        return tblResult.getValueAt(selectedRowIndex,0).toString();
    }
    
    /**
     *  Method to get the selected row as hashtable
     * @return selected row
     */
    HashMap getSelectedRow() throws Exception{
        if(selectedRowIndex==-1){
            throw new Exception(coeusMessageResources.parseMessageKey(
            "searchResultWin_exceptionCode.1109"));
        }
        int index = Integer.parseInt(tblResult.getValueAt(selectedRowIndex,colCount).toString());
        return ((HashMap)resList.elementAt(index));
    }
    
    /**
     * Updated For : REF ID 149 Feb' 14 2003
     * Person Search allows for multiple entries, however,
     * the user can only add 1 at a time
     *
     * Updated by Subramanya Feb' 17 2003
     */
    
    /**
     *  Method to get the selected rows as vector ( used for multiple selection )
     * @return Vector collection of selected row where in each element has Hashmap instance.
     * @throws Exception
     */
    public Vector getMultipleSelectedRows() throws Exception{
        int selectedIndices[] = tblResult.getSelectedRows();
        if( selectedIndices.length == 0 ){
            throw new Exception(coeusMessageResources.parseMessageKey(
            "searchResultWin_exceptionCode.1109"));
        }
        Vector multipleSelectedRow = new Vector();
        for( int i = 0; i < selectedIndices.length; i++ ){
            int index = Integer.parseInt(tblResult.getValueAt(selectedIndices[i],colCount).toString());
            multipleSelectedRow.addElement( resList.elementAt( index) );
        }
        return multipleSelectedRow;
    }
    
    //Added by nadh start - 18-01-2005
    /**
     * This will sort by column index
     * @param sourceTable source Table
     * @param columns type vector
     */
    
    public void sortByColumns(JTable sourceTable,Vector columns) {
        sorter.doSort(sourceTable, columns);
    }
    //end - 18-01-2005
    /*
     *  Method to create table
     */
    private JTable buildTable() throws Exception{
        JTable tblSearchRes = new JTable();
        tblSearchRes.setFont(CoeusFontFactory.getNormalFont());
        if(searchResult==null){
            throw new Exception(coeusMessageResources.parseMessageKey(
            "searchResultWin_exceptionCode.1110"));
        }
        Vector displayList = (Vector)searchResult.get("displaylabels");
        Vector disLabelList = new Vector(3,2);
        int disColCnt = displayList.size();
        for(int disColIndex=0;disColIndex<disColCnt;disColIndex++){
            DisplayBean display = (DisplayBean)displayList.elementAt(disColIndex);
//            if(display.isVisible()){
                disLabelList.addElement(display.getValue());
                
//            }
        }
        disLabelList.addElement("index");
        this.resList = (Vector)searchResult.get("reslist");
        Vector resRowList = new Vector(3,2);
        for(int k=0;k<resList.size();k++){
            HashMap searchResultRow = (HashMap)resList.elementAt(k);
            Vector resultColumn = new Vector(3,2);
            for(int dispIndex=0;dispIndex<disColCnt;dispIndex++){
                DisplayBean display = (DisplayBean)displayList.elementAt(dispIndex);
//                if(display.isVisible()){
                    String fieldName = display.getName();
                    String resValue = null;
                    try{
                        resValue = Utils.convertNull(searchResultRow.get(fieldName)).toString();
                    }catch(NullPointerException nEx){
                        throw new Exception("Please check the entries in "+
                        "the DISPLAY element : "+fieldName+" in the resource XML file");
                    }
                    resultColumn.addElement(Utils.convertNull(resValue));
//                }
            }
            resultColumn.addElement(""+k);
            resRowList.addElement(resultColumn);
        }
        
        /**
         * Updated For : Table Sorting Case Insensitive
         *
         * Updated by Subramanya Feb' 20 2003
         */
        //commented by nadh
       /*sorter = new TableSorter(
            new DefaultTableModel(resRowList,disLabelList){
                    public boolean isCellEditable(int row,int col){
                        return false;
                    }
                }, false ); //ADDED THIS
        sorter.addMouseListenerToHeaderInTable(tblSearchRes); //ADDED THIS */
        
        //Added for supporting multiple sorting - nadh - 18-01-2005
        sorter = new MultipleTableColumnSorter(new DefaultTableModel(resRowList,disLabelList){
            public boolean isCellEditable(int row,int col){
                return false;
            }
        });
        tblSearchRes.setModel(sorter);
        sorter.setTableHeader(tblSearchRes.getTableHeader());
        //end - 18-01-2005
        
        
        int tmpColCnt = disLabelList.size();
        int colIndex=0;
        for(colIndex = 0;colIndex<tmpColCnt-1;colIndex++){
            DisplayBean display = (DisplayBean)displayList.elementAt(colIndex);
            TableColumn column = tblSearchRes.getColumnModel().getColumn(colIndex);
            if(!display.isVisible()){
//                TableHeader tblHeader = tblSearchRes.getTableHeader();
                column.setHeaderValue(" ");// Space given for the bug Fix#1609
                column.setMaxWidth(0);
                column.setMinWidth(0);
                column.setPreferredWidth(0);
//                column.setWidth(0);
            }else{
                column.setPreferredWidth(display.getSize());
            }
            //Case#2908 - Exports from Search Results Do Not Preserve Data Format  - Start
            column.setIdentifier(display);
            //Case#2908 - End
        }
        colCount = colIndex;
        tblSearchRes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn clmName = tblSearchRes.getColumnModel().getColumn(
        tblSearchRes.getColumnCount()-1);
        clmName.setMaxWidth(0);
        clmName.setMinWidth(0);
        //clmName.setWidth(0);
        clmName.setPreferredWidth(0);
        
        tblSearchRes.setRowHeight(20);
        tblSearchRes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel rowSM = tblSearchRes.getSelectionModel();
        rowSM.addListSelectionListener(this);
        
        DefaultCellEditor editor =(DefaultCellEditor)tblSearchRes.getCellEditor();
        if (editor != null){
            editor.stopCellEditing();
        }
        //        tblSearchRes.setCellSelectionEnabled(true);
        tblSearchRes.setRowSelectionAllowed(true);
        tblSearchRes.getTableHeader().setReorderingAllowed(false);
        tblSearchRes.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblSearchRes.requestFocusInWindow();
        tblSearchRes.addRowSelectionInterval(0,0);
        
        tblSearchRes.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent kEvent){
                coeusSearch.fireAction(kEvent.getKeyCode());
            }
        });
        return tblSearchRes;
    }
    private int selectedRowIndex = -1;
    /**
     *Overridden method for the TreeChangeListener
     */
    public void valueChanged(ListSelectionEvent e) {
        //Ignore extra messages.
        if (e.getValueIsAdjusting()) return;
        
        ListSelectionModel lsm =
        (ListSelectionModel)e.getSource();
        if (lsm.isSelectionEmpty()) {
            //no rows are selected
        } else {
            selectedRowIndex = lsm.getMinSelectionIndex();
        }
    }
}