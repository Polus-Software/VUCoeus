/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.gui;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.budget.bean.*;

/** This class displays the persons who have multiple appointments
 * AppointmentsForPersonForm.java
 * @author  Vyjayanthi
 * Created on October 13, 2003, 10:49 AM
 */
public class AppointmentsForPersonForm extends javax.swing.JComponent 
implements ActionListener{
    
    CoeusDlgWindow dlgAppointmentsForPersonForm;
    private boolean modal;
    private Frame parent;
    private String personId;
    private String personName;
    private String message = "";
    private DateUtils dtUtils;
    private AppointmentsBean appointmentsBean;
    private CoeusVector vecAppointments;
    private static final int WIDTH = 550;
    private static final int HEIGHT = 300;

    private int selectedRow = 0;
    private int selectedRowCount = 0;
    private String selectedJobCode;
    private String selectedAppointmentType;
    private double selectedSalary;
    
    private static final String SELECT_AN_APPOINTMENT = "budget_common_exceptionCode.1003";
    
    /** Holds CoeusMessageResources instance used for reading message Properties. */    
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    
    /** Creates new form AppointmentsForPersonForm 
     * @param parent takes the frame
     * @param modal true if modal, false otherwise
     * @param personId takes the person id
     * @param personName takes the person name
     * @param vecAppoint takes the appointments vector
     */
    public AppointmentsForPersonForm(Frame parent, boolean modal, 
    String personId, String personName, CoeusVector vecAppoint) {
        this.vecAppointments = vecAppoint;
        this.parent = parent;
        this.modal = modal;
        this.personId = personId;
        this.personName = personName;
        initComponents();
        postInitComponents();
        if( vecAppointments != null && vecAppointments.size() > 0 ){
            setTableData(vecAppointments);
        }
        setTableEditors();
    }

    /** Method to set all listeners and set default properties */
    private void postInitComponents(){
        dlgAppointmentsForPersonForm = new CoeusDlgWindow(parent,modal);
        dlgAppointmentsForPersonForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        //dlgAppointmentsForPersonForm.setUndecorated(true);
        dlgAppointmentsForPersonForm.getContentPane().add(this);
        dlgAppointmentsForPersonForm.pack();
        dlgAppointmentsForPersonForm.setResizable(false);
        dlgAppointmentsForPersonForm.setTitle("Appointments");
        txtArTitle.setText("Selected person " + personName + 
                " has multiple appointments. Please select a relevant appointment");
        dlgAppointmentsForPersonForm.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAppointmentsForPersonForm.getSize();
        dlgAppointmentsForPersonForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        btnOk.addActionListener(this);
        
        dlgAppointmentsForPersonForm.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    //Do nothing
                }
        });

        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { tblAppointments, btnOk};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        tblAppointments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Primary/Secondary", "Start Date", "End Date", "Appointment Type", 
                "Job Code", "Job Title", "Unit No.", "Unit Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        tblAppointments.setSelectionForeground(Color.black);
        tblAppointments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    /** Sets the data for the table */    
    private void setTableData(CoeusVector vecAppointments){
        Vector tableRow ;
        dtUtils = new DateUtils();
        for(int index = 0; index < vecAppointments.size(); index++ ){
            appointmentsBean = ( AppointmentsBean ) vecAppointments.get(index);
            tableRow = new Vector();
            tableRow.addElement(appointmentsBean.getPrimarySecondaryIndicator());
            //Check for the null value
            // Added by chandra 27/02/2004 - start
            if(appointmentsBean.getAppointmentStartDate()!=null){
                tableRow.addElement( dtUtils.formatDate( 
                    appointmentsBean.getAppointmentStartDate().toString(), REQUIRED_DATEFORMAT ));
            }else{
                tableRow.addElement("");
            }
            if(appointmentsBean.getAppointmentEndDate()!=null){
                tableRow.addElement( dtUtils.formatDate( 
                    appointmentsBean.getAppointmentEndDate().toString(), REQUIRED_DATEFORMAT ));
            }else{
                tableRow.addElement("");
            }
            // Added by chandra 27/02/2004 - End
            tableRow.addElement( appointmentsBean.getAppointmentType() );
            tableRow.addElement( appointmentsBean.getJobCode() );
            tableRow.addElement( appointmentsBean.getJobTitle());
            tableRow.addElement( appointmentsBean.getUnitNumber());
            tableRow.addElement( appointmentsBean.getUnitName() );
            ((DefaultTableModel)tblAppointments.getModel()).addRow(tableRow);
        }
        tblAppointments.setRowSelectionInterval(0,0);
    }

    /** Displays the <CODE>dlgAppointmentsForPersonForm</CODE>
     * @return returns the AppointmentsBean
     */    
    public AppointmentsBean display(){
        btnOk.requestFocusInWindow();
        dlgAppointmentsForPersonForm.setVisible(true);
        return appointmentsBean;
    }
    
    /** Method to set the table column widths */   
    private void setTableEditors(){
        JTableHeader header = tblAppointments.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
       
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

        TableColumn column = tblAppointments.getColumnModel().getColumn(0);
        column.setMinWidth(200);
        column.setMaxWidth(250);
        column.setPreferredWidth(120);
        
        column = tblAppointments.getColumnModel().getColumn(1);
        column.setMinWidth(100);
        column.setMaxWidth(160);
        column.setPreferredWidth(80);
        
        column= tblAppointments.getColumnModel().getColumn(2);
        column.setMinWidth(100);
        column.setMaxWidth(160);
        column.setPreferredWidth(80);
        
        column= tblAppointments.getColumnModel().getColumn(3);
        column.setMinWidth(200);
        column.setMaxWidth(250);
        column.setPreferredWidth(90);
        
        column= tblAppointments.getColumnModel().getColumn(4);
        column.setMinWidth(100);
        column.setMaxWidth(160);
        column.setPreferredWidth(100);
        
        column = tblAppointments.getColumnModel().getColumn(5);
        column.setMinWidth(200);
        column.setMaxWidth(250);
        column.setPreferredWidth(120);
        
        column = tblAppointments.getColumnModel().getColumn(6);
        column.setMinWidth(100);
        column.setMaxWidth(160);
        column.setPreferredWidth(100);

        column = tblAppointments.getColumnModel().getColumn(7);
        column.setMinWidth(250);
        column.setMaxWidth(300);
        column.setPreferredWidth(170);
        
        tblAppointments.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        txtArTitle = new javax.swing.JTextArea();
        btnOk = new javax.swing.JButton();
        scrPnAppointments = new javax.swing.JScrollPane();
        tblAppointments = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        txtArTitle.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtArTitle.setEditable(false);
        txtArTitle.setFont(CoeusFontFactory.getLabelFont());
        txtArTitle.setLineWrap(true);
        txtArTitle.setText("Selected person xxxx has multiple appointments. Please select a relevant appointment");
        txtArTitle.setWrapStyleWord(true);
        txtArTitle.setMinimumSize(new java.awt.Dimension(450, 35));
        txtArTitle.setPreferredSize(new java.awt.Dimension(450, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(txtArTitle, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(81, 27));
        btnOk.setMinimumSize(new java.awt.Dimension(81, 27));
        btnOk.setPreferredSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(btnOk, gridBagConstraints);

        scrPnAppointments.setMinimumSize(new java.awt.Dimension(540, 220));
        scrPnAppointments.setPreferredSize(new java.awt.Dimension(540, 220));
        tblAppointments.setFont(CoeusFontFactory.getNormalFont());
        tblAppointments.setSelectionBackground(java.awt.Color.yellow);
        scrPnAppointments.setViewportView(tblAppointments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(scrPnAppointments, gridBagConstraints);

    }//GEN-END:initComponents

    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals( btnOk ) ){
            selectedRowCount = tblAppointments.getSelectedRowCount();
            message = coeusMessageResources.parseMessageKey(SELECT_AN_APPOINTMENT);
            if ( selectedRowCount == 0 || selectedRowCount > 1){
                CoeusOptionPane.showInfoDialog(message);
            }else{
                selectedRow = tblAppointments.getSelectedRow();
                appointmentsBean = ( AppointmentsBean ) vecAppointments.get(selectedRow);
                selectedJobCode = appointmentsBean.getJobCode();
                selectedAppointmentType = appointmentsBean.getAppointmentType();
                selectedSalary = appointmentsBean.getSalary();
                dlgAppointmentsForPersonForm.dispose();
            }
        }
    }   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnOk;
    public javax.swing.JScrollPane scrPnAppointments;
    public javax.swing.JTable tblAppointments;
    public javax.swing.JTextArea txtArTitle;
    // End of variables declaration//GEN-END:variables

}
