/**
 * @(#)AttendeesLookupWindow.java  1.0  March 19, 2003, 11:15 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.irb.gui;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.*;

import java.awt.event.*;
import java.awt.*;
import java.util.Vector;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.table.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import edu.mit.coeus.irb.bean.AttendanceInfoBean;

/** 
 * <CODE>AttendeesLookupWindow </CODE>is a form object which display
 * the list of attendees details and it is used to <CODE> display </CODE> the attendee details.
 * This class will be instantiated from <CODE>ProtocolVoteForm</CODE>.
 * @version 1.0 March 19, 2003, 11:15 AM
 * @author Raghunath P.V.
 */
public class AttendeesLookupWindow extends javax.swing.JComponent implements ActionListener{
    
    private Vector vecAttendees;
    private Vector vecSelectedRows;
    private JDialog dlgParentComponent;
    private CoeusAppletMDIForm mdiForm;
    private AttendanceInfoBean attendanceInfoBean;
    
    /** Creates new form AttendentiesLookupWindow */
    public AttendeesLookupWindow() {
        
    }
    
    /** Creates new form <CODE>AttendeesLookupWindow</CODE>
     *
     * @param attendees is the list of attendees
     */
    public AttendeesLookupWindow(Vector attendees) {
        
        this.vecAttendees = attendees;
        String title = "Attendees List";
        dlgParentComponent = new JDialog(CoeusGuiConstants.getMDIForm(),
                title, true);
        dlgParentComponent.getContentPane().add(createAttendeesLookupWindow());
        dlgParentComponent.setResizable(false);
        dlgParentComponent.pack();

        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgParentComponent.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    dlgParentComponent.dispose();
                }
            }
        });
        dlgParentComponent.show();
        
    }
     /**
     * Creates a GUI for AttendeesLookupWindow and set the editors and 
     * populate data to the components
     */
    private JComponent createAttendeesLookupWindow(){
        
        initComponents();
        setFormData();
        setEditors();
        return this;
        
    }
    
    public Vector getSelectedAttendees(){
        return vecSelectedRows;
    }
     /**
     * This method is used to set the editors for the JTable. 
     * And is also used to set the listeners
     */
    private void setEditors(){
        
        tblAttendees.getTableHeader().setReorderingAllowed(false);
        tblAttendees.getTableHeader().setResizingAllowed(false);
        tblAttendees.setSelectionMode(
                                DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.tblAttendees.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        TableColumn column = tblAttendees.getColumnModel().getColumn(0);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        
        column = tblAttendees.getColumnModel().getColumn(2);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        
        this.btnOk.addActionListener( this );
        this.btnCancel.addActionListener( this );
        
    }
    
     /**
     * This method is used to set all the values in the Vote form
     */
    private void setFormData(){
        
        AttendanceInfoBean attendInfoBean = null;
        
        if(vecAttendees != null){
            
            Vector vcDataPopulate = new Vector();
            Vector vcData;
            int size = vecAttendees.size();
            for(int index = 0; index < size; index++){
                
                attendInfoBean = (AttendanceInfoBean)vecAttendees.get(index);
                if(attendInfoBean != null){
                    
                    String attendeeId = attendInfoBean.getPersonId();
                    String attendeeName = attendInfoBean.getPersonName();
                    boolean nonEmployeeFlag = attendInfoBean.getNonEmployeeFlag();
                    
                    vcData = new Vector();
                    vcData.addElement(attendeeId == null ? "" : attendeeId);
                    vcData.addElement(attendeeName == null ? "" : attendeeName);
                    vcData.addElement(new Boolean(nonEmployeeFlag));
                    
                    vcDataPopulate.addElement(vcData); 
                    
                }
            }
            
            ((DefaultTableModel)tblAttendees.getModel()).
                            setDataVector(vcDataPopulate,getColumnNames());
            ((DefaultTableModel)tblAttendees.getModel()).
                            fireTableDataChanged();
            
        }
        
    }
    
    /**
     * This helper method is used to get all the column names of the JTable.
     */
    
    private Vector getColumnNames(){
        Enumeration enumColNames = tblAttendees.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlAttendees = new javax.swing.JPanel();
        scrPnAttendees = new javax.swing.JScrollPane();
        tblAttendees = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlAttendees.setLayout(new java.awt.GridBagLayout());

        scrPnAttendees.setPreferredSize(new java.awt.Dimension(275, 250));
        scrPnAttendees.setMinimumSize(new java.awt.Dimension(275, 250));
        scrPnAttendees.setMaximumSize(new java.awt.Dimension(275, 250));
        tblAttendees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PersonId", "Attendees", "NonEmployeeFlag"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAttendees.setFont(CoeusFontFactory.getNormalFont());
        scrPnAttendees.setViewportView(tblAttendees);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 5, 0);
        pnlAttendees.add(scrPnAttendees, gridBagConstraints);

        btnOk.setMnemonic('O');
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 5, 0, 2);
        pnlAttendees.add(btnOk, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 5, 2);
        pnlAttendees.add(btnCancel, gridBagConstraints);

        add(pnlAttendees);

    }//GEN-END:initComponents

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        
        Object actionSource = actionEvent.getSource();
        
        if( actionSource.equals( this.btnOk ) ){
            
            int[] selectedRows = this.tblAttendees.getSelectedRows();
            AttendanceInfoBean attInfoBean = null;
            
            if(selectedRows != null){
                
                int selSize = selectedRows.length;
                
                vecSelectedRows = new Vector();
                for(int index = 0 ; index < selSize; index++){
                    
                    int selIndex = selectedRows[index];
                    
                    String stPersonId = ( String )
                                ((DefaultTableModel)tblAttendees.
                                        getModel()).getValueAt(selIndex,0);
                    String stPersonName = ( String )
                                ((DefaultTableModel)tblAttendees.
                                        getModel()).getValueAt(selIndex,1);
                    boolean boNonEmployeeFlag = ((Boolean)((DefaultTableModel)tblAttendees.
                                        getModel()).getValueAt(selIndex,2)).booleanValue();
                    
                    attInfoBean = new AttendanceInfoBean();
                    attInfoBean.setPersonId(stPersonId);
                    attInfoBean.setPersonName(stPersonName);
                    attInfoBean.setNonEmployeeFlag(boNonEmployeeFlag);

                    vecSelectedRows.addElement(attInfoBean);
                    
                }
            }
            
            if( dlgParentComponent != null ){            
                dlgParentComponent.dispose();
            }
            
        }else if(actionSource.equals( this.btnCancel )){
            
            if( dlgParentComponent != null ){            
                dlgParentComponent.dispose();
            }
            
        }
    }    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JTable tblAttendees;
    private javax.swing.JScrollPane scrPnAttendees;
    private javax.swing.JPanel pnlAttendees;
    private javax.swing.JButton btnCancel;
    // End of variables declaration//GEN-END:variables
    
}
