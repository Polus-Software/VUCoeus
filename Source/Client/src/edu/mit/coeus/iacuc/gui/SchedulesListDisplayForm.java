/*
 * @(#)SchedulesListDisplayForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
 */

package edu.mit.coeus.iacuc.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;

/** This class is used to display all the Schedules generated between the given
 * start and end dates with the frequency specified. If generated schedules are
 * present, then user has to choose atleast one schedule.
 *
 * @author ravikanth
 * Created on September 21, 2000, 1:19 PM
 * @version: 1.0
 */
public class SchedulesListDisplayForm extends JComponent {
       
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /**
     * Default Constructor 
     */
    public SchedulesListDisplayForm(){
    }
    /**
     * Creates new <CODE>SchedulesListDisplayForm</CODE> with specified parent component and
     * initializes the form components.
     * @param parentDialog reference to parent Component.
     */
    public SchedulesListDisplayForm(Component parentDialog) {
        this.parentDialog = parentDialog;
        initComponents();
        tblGeneratedSchedules.getTableHeader().setFont(
            CoeusFontFactory.getLabelFont());
        
    }
    /**
     * Creates new <CODE>SchedulesListDisplayForm</CODE> with given schedules and parent
     * component.
     *
     * @param parentDialog reference to parent Component.
     * @param generatedSchedules Vector  which consists of all generated
     * schedules in <CODE>ScheduleGenerateForm</CODE>.
     */
    
    public SchedulesListDisplayForm(Component parentDialog,
            Vector generatedSchedules) {
        this.parentDialog = parentDialog;
        initComponents();
        tblGeneratedSchedules.getTableHeader().setFont(
            CoeusFontFactory.getLabelFont());
        Enumeration enumColNames = 
                tblGeneratedSchedules.getColumnModel().getColumns();
        Vector vecColNames = new Vector(5,5);
        String strName = null;
        while(enumColNames.hasMoreElements()){
            strName = (String)((TableColumn)
                    enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        ((DefaultTableModel)tblGeneratedSchedules.getModel()).setDataVector(
                generatedSchedules,vecColNames);
        ((DefaultTableModel)
                tblGeneratedSchedules.getModel()).fireTableDataChanged();
    }
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        GridBagConstraints gridBagConstraints;
        
        pnlMain = new JPanel();
        selectedSchedules = new Vector(5,5);
        scrPnGeneratedSchedules = new JScrollPane();
        tblGeneratedSchedules = new JTable();
        tblGeneratedSchedules.setFont(CoeusFontFactory.getNormalFont());
        
        btnOk = new JButton();
        btnCancel = new JButton();
    /** Updated for Change Request: REF ID 173 on Feb' 14 2003
     * 
     * During schedule generation for a committee, once the user is shown a 
     * list of schedules that are generated, there should be an easy way to 
     * select all. A button Select All would be nice
     *
     * Updated by Subramanya Feb' 17 2003
     */
        btnSelectAll = new JButton();
        
        pnlMain.setLayout(new GridBagLayout());
        
        scrPnGeneratedSchedules.setPreferredSize(new Dimension(500, 200));
        tblGeneratedSchedules.setModel(new DefaultTableModel(
        new Object [][] {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
        },
        new String [] {
            "Schedule Date","Day Of Week","Place", "Time"
        }
        ) {
            public boolean isCellEditable(int row, int col){
                return false;
            }
        });
        scrPnGeneratedSchedules.setViewportView(tblGeneratedSchedules);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(8, 8, 8, 0);
        pnlMain.add(scrPnGeneratedSchedules, gridBagConstraints);
        
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                if(tblGeneratedSchedules.getRowCount()>0){
                    /* if generated schedules are available user has to select 
                       atleast one  */
                    if(tblGeneratedSchedules.getSelectedRowCount()>0){
                        DateUtils dUtils = new DateUtils();
                        int rows[] = tblGeneratedSchedules.getSelectedRows();
                        Vector vecRow = null;
                        for(int loopIndex=0;loopIndex<rows.length;loopIndex++){
                            vecRow = new Vector();
                            vecRow.addElement(
                                tblGeneratedSchedules.getValueAt(
                                        rows[loopIndex],0).toString());
                            vecRow.addElement(
                                tblGeneratedSchedules.getValueAt(
                                        rows[loopIndex],1).toString());
                            vecRow.addElement(
                                tblGeneratedSchedules.getValueAt(
                                        rows[loopIndex],2).toString());
                            vecRow.addElement(
                                tblGeneratedSchedules.getValueAt(
                                        rows[loopIndex],3).toString());    
                            selectedSchedules.addElement(vecRow);
                        }
                        ((JDialog)parentDialog).dispose();
                    }else{
                        CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                        "schdLstDispFrm_exceptionCode.1089"));
                    }
                }else{
                    /* if there are no schedules generated then close the 
                       window. */
                    ((JDialog)parentDialog).dispose();
                }
                
            }
        });
        
    /** Updated for Change Request: REF ID 173 on Feb' 14 2003
     * 
     * During schedule generation for a committee, once the user is shown a 
     * list of schedules that are generated, there should be an easy way to 
     * select all. A button Select All would be nice
     *
     * Updated by Subramanya Feb' 17 2003
     */
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(8, 5, 0, 8);
        pnlMain.add(btnOk, gridBagConstraints);
        
        btnSelectAll.setFont(CoeusFontFactory.getLabelFont());        
        btnSelectAll.setText("Select All");
        btnSelectAll.setMnemonic('A');
        btnSelectAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                tblGeneratedSchedules.selectAll(); 
            }
        });  
         
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 5, 0, 8);
        pnlMain.add(btnSelectAll, gridBagConstraints);
        
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                ((JDialog)parentDialog).dispose();
            }
        });  
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 5, 0, 8);
        pnlMain.add(btnCancel, gridBagConstraints);
        setLayout(new BorderLayout());
        add(pnlMain,BorderLayout.CENTER);
        
        //raghuSV: focus traversal
        //starts...
        java.awt.Component[] components = {tblGeneratedSchedules,btnOk,btnSelectAll,btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        //ends
    }
    
    
    private JPanel pnlMain;
    private JButton btnCancel;
    private JButton btnOk;
    
    /** Updated for Change Request: REF ID 173 on Feb' 14 2003
     * 
     * During schedule generation for a committee, once the user is shown a 
     * list of schedules that are generated, there should be an easy way to 
     * select all. A button Select All would be nice
     *
     * Updated by Subramanya Feb' 17 2003
     */
    private JButton btnSelectAll;
    
    private JScrollPane scrPnGeneratedSchedules;
    private JTable tblGeneratedSchedules;
    private Component parentDialog;
    /* holds the list of selected schedules */
    private Vector selectedSchedules=null;
    // End of variables declaration
    
    /** This method returns the selected schedules from the list of generated
     * schedules
     *
     * @return Vector of selected schedules with Scheduled date, Day of Week, Place
     * and Time as elements for each schedule.
     */
    
    public Vector getSelectedSchedules(){
        return selectedSchedules;
    }
    
    public void requestDefaultFocusForComponent(){
        if( tblGeneratedSchedules.getRowCount() > 0 ) {
            tblGeneratedSchedules.requestFocusInWindow();
        }else{
            btnCancel.requestFocusInWindow();
        }
    }
}
