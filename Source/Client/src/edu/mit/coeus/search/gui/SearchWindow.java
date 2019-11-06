/*
 * @(#)SearchWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on August 14, 2002, 12:01 PM
 * @author  Geo Thomas
 * @version 1.0
 * @modified by Sagin
 * @date 25-10-02
 * Description : Java V1.3 compatibility, Commented the focuslost listener for  
 *               Table columns, since the Combobox was throwing Illegalstate error.
 *
 */

package edu.mit.coeus.search.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.GregorianCalendar;
import java.util.Calendar;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.bean.CriteriaBean;
import edu.mit.coeus.search.bean.FieldBean;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.search.bean.ColumnBean;
import edu.mit.coeus.search.bean.AttributeBean;
import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusOptionPane;

/**
 *  The class which provides required APIs for the search criteria panel
 */
public class SearchWindow{

    private final int NUMBER_OF_ROWS = SearchInfoHolderBean.NUMBER_OF_ROWS;
    // Variables declaration 
    //criteria table
    private JTable tblCriteria;
    //SearchInfoHolder information
    private SearchInfoHolderBean searchInfoHolder;
    //CoeusSearch
    private CoeusSearch coeusSearch;
    //Criteria list as Vector
    private Vector criteriaList;
    //Search label
    private String searchLabel;
    // End of variables declaration
    
    private CoeusTextField txtDateField;
    /* updated on 18-02-03 to fix the bug with id: #153 */
    /* to provide conditional searching for dates like >,<,<=,>=,<> */
    private boolean temporary;
    private boolean performFocusLost = true;
    /** Creates new form SearchWindow */
    SearchWindow(CoeusSearch coeusSearch,SearchInfoHolderBean searchInfoHolder){
        this.coeusSearch = coeusSearch;
        this.searchInfoHolder = searchInfoHolder;
        this.searchLabel = searchInfoHolder.getDisplayLabel();
        this.criteriaList = searchInfoHolder.getCriteriaList();
        this.tblCriteria = buildTable();
    }

    /**
     *  Method used to get the SearchInfoHodler bean instance
     * @return search information as <code>SearchInfoHolder</code> instance
     */
    SearchInfoHolderBean getSearchInfoHolder(){
        return searchInfoHolder;
    }
    /**
     *  Method used to get the criteria table
     *  @return criteria table
     */
    JTable getCriteriaTable(){
        return tblCriteria;
    }

    //column names for the criteria header
    private String [] columnNames;
    private String focusDate = "";
    private GregorianCalendar gcal;
    private DateUtils dtUtils;
    private JTable tblCrit;
    private JTable buildTable(){
        tblCrit = new JTable(){
            public void changeSelection(int row, int column, boolean toggle, boolean extend){
                super.changeSelection(row, column, toggle, extend);
                tblCrit.editCellAt(row,column);
                tblCrit.getEditorComponent().requestFocus();
            }            
        };
        tblCrit.setFont(CoeusFontFactory.getNormalFont());
        ((JTextField)((DefaultCellEditor)tblCrit.getDefaultEditor(String.class)
            ).getComponent()).setFont(CoeusFontFactory.getNormalFont());
        
        int criteriaCnt = criteriaList.size();
        //attach the column names in an array
        String[] criteriaLabels = new String[criteriaCnt];
        for(int criteriaIndex = 0;criteriaIndex<criteriaCnt;criteriaIndex++){
            CriteriaBean criteria = (CriteriaBean)criteriaList.get(criteriaIndex);
            FieldBean fieldBean = criteria.getFieldBean();
            criteriaLabels[criteriaIndex] = fieldBean.getLabel();
        }
        columnNames = criteriaLabels;
        tblCrit.setModel(new DefaultTableModel(criteriaLabels,NUMBER_OF_ROWS));
        Vector criteriaData = new Vector(3,2);
        for(int criteriaIndex = 0;criteriaIndex<criteriaCnt;criteriaIndex++){
            CriteriaBean criteria = (CriteriaBean)criteriaList.get(criteriaIndex);
            int cellSize = criteria.getSize();
            FieldBean fieldBean = criteria.getFieldBean();
            TableColumn column = tblCrit.getColumnModel().getColumn(criteriaIndex);
            if(fieldBean.isListBox()){
                //build combobox
                Vector comboList = fieldBean.getComboList();
                int comboLength = comboList.size();
                CoeusComboBox  coeusCombo = new CoeusComboBox(comboList,false);
                coeusCombo.setPopupBackground(java.awt.Color.white);
                coeusCombo.setPopupSelectionBackground(java.awt.Color.lightGray);
                column.setCellEditor(new DefaultCellEditor(coeusCombo ){
                    public Object getCellEditorValue(){
                        return (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
                    }
                });
            }
            column.setPreferredWidth(cellSize);
        }
        tblCrit.setRowHeight(20);
        tblCrit.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblCrit.getTableHeader().setReorderingAllowed(false);
        ((JTextField)((DefaultCellEditor)tblCrit.getDefaultEditor(String.class)
            ).getComponent()).addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent kEvent){
                    if( kEvent.getKeyCode() == KeyEvent.VK_ENTER ){//&& 
    //                        kEvent.getSource() instanceof JTable){
                        if( tblCrit.isEditing() && tblCrit.getCellEditor() != null ){
                            tblCrit.getCellEditor().stopCellEditing();
                        }
                        coeusSearch.fireAction(kEvent.getKeyCode());
                        tblCrit.requestFocusInWindow();
                        kEvent.consume();
                    }
                }
        });
        tblCrit.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_ENTER ){//&& 
//                        kEvent.getSource() instanceof JTable){
                    if( tblCrit.isEditing() && tblCrit.getCellEditor() != null ){
                        tblCrit.getCellEditor().stopCellEditing();
                    }
                    coeusSearch.fireAction(kEvent.getKeyCode());
                    tblCrit.requestFocusInWindow();
                    kEvent.consume();
                }else if ( kEvent.getKeyCode() == KeyEvent.VK_DELETE ) {
                    int selRow = tblCrit.getSelectedRow();
                    int selCol = tblCrit.getSelectedColumn();
                    if( selRow != -1 && selCol != -1 ){
                        tblCrit.getCellEditor(selRow,selCol).stopCellEditing();
                        tblCrit.setValueAt("",selRow,selCol);
                        kEvent.consume();
                    }
                }
            }
        });
        tblCrit.addColumnSelectionInterval(0,0);
        tblCrit.setForeground(java.awt.Color.black);
        tblCrit.setSelectionBackground(java.awt.Color.white);
        tblCrit.setSelectionForeground(java.awt.Color.black);
        tblCrit.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        return tblCrit;
    }

    /**
     *  Method to clear all the cells
     */
    void clearAll(){
        int colCnt = tblCriteria.getColumnCount();
        int rowCnt = tblCriteria.getRowCount();
        for(int rowIndex = 0;rowIndex<rowCnt;rowIndex++){
            for(int colIndex = 0;colIndex<colCnt;colIndex++){
                tblCriteria.setValueAt(null,rowIndex,colIndex);
            }
        }
    }
}