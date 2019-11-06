/*
 * @(#)VulnerableSubjectsForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
 */

package edu.mit.coeus.irb.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Enumeration;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;


/** This class is used to display all available Vulnerable Subjects in a table.
 * User can select any number of Vulnerable Subjects to be added to a Protocol.
 * Duplicate subjects if selected, will not be added to protocol Vulnerable
 * Subjects.
 *
 * @author ravikanth
 * Created on October 27, 2002, 2:15 PM
 * @version: 1.0
 */
public class VulnerableSubjectsForm extends JComponent {

        
    // Variables declaration - do not modify
    private JPanel pnlMain;
    private JButton btnCancel;
    private JButton btnOk;
    private JScrollPane scrPnSubjects;
    private JTable tblSubjects;
    private Component parentDialog;

    /* holds the list of selected Subjects */
    private Vector selectedSubjects=null;
       
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    // End of variables declaration
    

    /**
     * Default Constructor 
     */
    public VulnerableSubjectsForm(){
    }
    
    /**
     * Creates new form <CODE>VulnerableSubjectsForm</CODE> with specified parent dialog
     * @param parentDialog reference to parent dialog Component.
     */
    public VulnerableSubjectsForm(Component parentDialog) {
        this.parentDialog = parentDialog;
        initComponents();
        tblSubjects.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
    }
    
    /**
     * Creates new form <CODE>VulnerableSubjectsForm</CODE> with given Subjects.
     * @param parentDialog reference to parent dialog Component.
     * @param subjects Vector which consists of all available Vulnerable Subjects
     */    
    public VulnerableSubjectsForm(Component parentDialog,
            Vector subjects) {
        this.parentDialog = parentDialog;
        initComponents();
        tblSubjects.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        Enumeration enumColNames = 
                tblSubjects.getColumnModel().getColumns();
        Vector vecColNames = new java.util.Vector(5,5);
        String strName = null;
        while(enumColNames.hasMoreElements()){
            strName = (String)((TableColumn)
                    enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        ((DefaultTableModel)tblSubjects.getModel()).setDataVector(
                constructTableData(subjects),vecColNames);
        ((DefaultTableModel)
                tblSubjects.getModel()).fireTableDataChanged();

        TableColumn  column = tblSubjects.getColumnModel().getColumn(0);
        column.setMinWidth(50);
        column.setMaxWidth(50);
        column.setPreferredWidth(50);
        
    }
    
    //supporting method to get the table data as vector.
    private Vector constructTableData(Vector subjects){
    
        Vector tableData = new Vector();
        Vector tableRowData = null;       
        ComboBoxBean subBean = null;
        if(subjects != null){
            for(int subIndex = 0 ; subIndex < subjects.size() ; subIndex++ ){
                subBean = (ComboBoxBean)//(ProtocolVulnerableSubListsBean)
                        subjects.elementAt(subIndex);
                tableRowData = new Vector();                
                tableRowData.addElement(subBean.getCode());
                tableRowData.addElement(subBean.getDescription());
                tableData.addElement(tableRowData);
            }
        }
        return tableData;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        GridBagConstraints gridBagConstraints;
        
        pnlMain = new JPanel();
        selectedSubjects = new Vector(5,5);
        scrPnSubjects = new JScrollPane();
        tblSubjects = new JTable();
        btnOk = new JButton();
        btnCancel = new JButton();
        
        pnlMain.setLayout(new GridBagLayout());
        
        scrPnSubjects.setPreferredSize(new Dimension(350, 200));
        tblSubjects.setFont(CoeusFontFactory.getNormalFont());
        tblSubjects.setModel(new DefaultTableModel(
        new Object [][] { {null, null,} },
        new String [] { "Code","Description" }) {
            public boolean isCellEditable(int row, int col){
                return false;
            }
        });
        scrPnSubjects.setViewportView(tblSubjects);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(8, 8, 8, 0);
        pnlMain.add(scrPnSubjects, gridBagConstraints);
        
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                if(tblSubjects.getRowCount()>0){
                    /* if Subjects are available user has to select 
                       atleast one  */
                    if(tblSubjects.getSelectedRowCount()>0){
                        int rows[] = tblSubjects.getSelectedRows();
                        Vector vecRow = null;
                        for(int loopIndex=0;loopIndex<rows.length;loopIndex++){
                            vecRow = new Vector();
                            vecRow.addElement(
                                tblSubjects.getValueAt(
                                        rows[loopIndex],0).toString());
                            vecRow.addElement(
                                tblSubjects.getValueAt(
                                        rows[loopIndex],1).toString());
                            selectedSubjects.addElement(vecRow);
                        }
                        ((JDialog)parentDialog).dispose();
                    }else{
                        CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                                            "vulSubjFrm_exceptionCode.1142"));
                    }
                }else{
                    /* if there are no Subjects available then close the 
                       window. */
                    ((JDialog)parentDialog).dispose();
                }
                
            }
        });
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(8, 5, 0, 8);
        pnlMain.add(btnOk, gridBagConstraints);
        
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 5, 0, 8);
        pnlMain.add(btnCancel, gridBagConstraints);
        setLayout(new BorderLayout());
        add(pnlMain,BorderLayout.CENTER);
        
    }
    
    /** This method returns the selected Subjects from the list of available
     * Subjects.
     *
     * @return Vector of selected Subjects with code and description as elements
     * for each selected subject.
     */
    
    public Vector getSelectedSubjects(){
        return selectedSubjects;
    }
    public void requestDefaultFocusForComponent(){
        if( tblSubjects.getRowCount() > 0 ) {
            tblSubjects.requestFocusInWindow();
        }else{
            btnCancel.requestFocusInWindow();
        }
    }
}
