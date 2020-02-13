/**
 * @(#)MailForm.java  1.0  July 24, 2003, 11:00 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 15-JULY-2010
 * by Johncy M John
 */

package edu.mit.coeus.irb.gui;

//import edu.mit.coeus.gui.*;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
//import edu.mit.coeus.utils.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import edu.mit.coeus.bean.MailInfoBean;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;

/**
 * <CODE>MailForm </CODE>is a form object which display
 * the list of mail recipients and it is used to <CODE> add/delete/display/Modify </CODE> the persons to whom we need to send mails.
 * This class will be instantiated from <CODE>MinuteViewForm</CODE>.
 *
 * @version 1.0 July 24, 2003, 11:00 AM
 * @author Raghunath P.V.
 */
public class MailForm extends javax.swing.JComponent implements TypeConstants, ActionListener{

    private char functionType;
    private CoeusAppletMDIForm mdiReference;

    private char mailFunctionalityCode;
    private String scheduleId;
    private String attachId;
    private static final char MINUTE_MAIL = 'K';
    private static final char AGENDA_MAIL = 'A';
    private Vector vecReceipients;
    private boolean saveRequired;
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    /* used in searching for rolodex */
    private static final String ROLODEX_SEARCH = "rolodexSearch";
    /* used to specify to search in person table */
    private static final String PERSON_SEARCH = "personSearch";
    /* used to display title in the search window */
    private static final String DISPLAY_TITLE = "DISPLAY ROLODEX";
    /** This is used to verify the vaues which come from the database are null or not*/
    private edu.mit.coeus.utils.Utils Utils;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    //holds the zero count value
    private static final int ZERO_COUNT = 0;

    private CoeusDlgWindow dlgParentComponent;

    //Commented For PMD Check
    // private CoeusUtils coeusUtils = CoeusUtils.getInstance();



    /** Creates new form AgendaMailForm */
    public MailForm() {
    }

    /** Constructor that instantiate MailForm and populate the component with specified data.
     * And sets the enabled status for all components depending on the functionType.
     * @param recipientData a Vector which consists of all the persons whom we need to send mail
     *
     * @param functionType is a Char variable which specifies the mode in which the
     * form will be displayed.
     * <B>'A'</B> specifies that the form is in Add Mode
     * <B>'M'</B> specifies that the form is in Modify Mode
     * <B>'D'</B> specifies that the form is in Display Mode
     */

    public MailForm(char functionType, java.util.Vector recipientData) {

        this.vecReceipients = recipientData;
        this.functionType = functionType;
    }

    public void showMailInfo(){
        coeusMessageResources = CoeusMessageResources.getInstance();

        this.mdiReference = CoeusGuiConstants.getMDIForm();
        initComponents();
        btnSend.setMnemonic('S');
        tblReceipientDetailsInfo.setSelectionForeground(java.awt.Color.black);
        //raghuSV :focus traversal
        //starts..
        java.awt.Component[] components = {tblReceipientDetailsInfo,btnSend,btnCancel,btnAdd,btnDelete,btnFindRolodex,btnFindPerson};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        //ends

        if(mailFunctionalityCode == MINUTE_MAIL){//
            lblReceipient.setText(coeusMessageResources.parseMessageKey(
                    "mailForm_exceptionCode.1204"));
        }else if(mailFunctionalityCode == AGENDA_MAIL){
            lblReceipient.setText(coeusMessageResources.parseMessageKey(
                    "mailForm_exceptionCode.1205"));
        }
        setListeners();
        formatFields();
        setFormData();
        setTableEditors();
        formatFields();
        if( tblReceipientDetailsInfo!=null && tblReceipientDetailsInfo.getRowCount() > ZERO_COUNT ){
            tblReceipientDetailsInfo.setRowSelectionInterval(ZERO_COUNT,ZERO_COUNT);
        }else{
            btnDelete.setEnabled(false);
        }
        tblReceipientDetailsInfo.getTableHeader().setFont(CoeusFontFactory.getLabelFont());

        dlgParentComponent = new CoeusDlgWindow(mdiReference, "Mail Form", true);

        dlgParentComponent.getContentPane().add(this);
        dlgParentComponent.pack();
        dlgParentComponent.setResizable(false);
        //Modified for the case coeusdev219 - send functionality for agenda-start
        dlgParentComponent.setSize(570,500);
        //Modified for the case coeusdev219 - send functionality for agenda-end
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
            screenSize.height/2 - (dlgSize.height/2));
        if( tblReceipientDetailsInfo.getRowCount() > 0 ) {
            tblReceipientDetailsInfo.requestFocusInWindow();
        }else{
            btnAdd.requestFocusInWindow();
        }
        dlgParentComponent.show();
    }

    private void setFormData(){

        Vector vcDataPopulate = new Vector();
        Vector vcData = null;
        if((vecReceipients != null) &&
                        (vecReceipients.size()>0)){
            int size = vecReceipients.size();
            MailInfoBean mailbean = null;
            for(int index = 0;index < size; index++){

                mailbean = ( MailInfoBean ) vecReceipients.get(index);
                String personId = mailbean.getPersonID();
                String personname = mailbean.getPersonName();
                String role = mailbean.getRoleName();
                String emailId = mailbean.getEmailId();
                boolean nonEmployee = mailbean.getNonEmployeeFlag();

                vcData= new Vector();

                vcData.addElement("");
                vcData.addElement(personname == null ? "" : personname);
                //vcData.addElement(emailId == null ? "" : emailId);
                vcData.addElement(emailId); 
                vcData.addElement(role == null ? "" : role); 
                vcData.addElement( personId );
                vcData.addElement(new Boolean( nonEmployee ));
                //Modified for the case# COEUSDEV-219 send functionality for agenda-start
                if(emailId!= null){
                    vcDataPopulate.addElement(vcData);
                }
                //Modified for the case# COEUSDEV-219 send functionality for agenda-end
            }
            ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setDataVector(
                                            vcDataPopulate,getColumnNames());
        }
    }

    /**
     * This method is used to get the Column Names of mailers
     * table data.
     * @return Vector collection of column names of  table.
     */

    private Vector getColumnNames(){

        Enumeration enumColNames = tblReceipientDetailsInfo.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }

    private void formatFields(){

        boolean enabled = functionType != 'D' ? true : false ;
        btnFindRolodex.setEnabled( enabled );
        btnFindPerson.setEnabled( enabled );
        btnDelete.setEnabled( enabled );
        btnAdd.setEnabled( enabled );
        tblReceipientDetailsInfo.setEnabled(enabled);
        btnSend.setEnabled( enabled );
        if( tblReceipientDetailsInfo.getRowCount() < 1 ){
            btnDelete.setEnabled( false );
        }
    }

    private void setTableEditors(){

        JTableHeader header = tblReceipientDetailsInfo.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        tblReceipientDetailsInfo.setRowHeight(24);

        tblReceipientDetailsInfo.setOpaque(false);
        tblReceipientDetailsInfo.setShowVerticalLines(false);
        tblReceipientDetailsInfo.setShowHorizontalLines(false);
        tblReceipientDetailsInfo.setSelectionMode(
                            DefaultListSelectionModel.SINGLE_SELECTION);
        tblReceipientDetailsInfo.setRowHeight(20);

        tblReceipientDetailsInfo.getTableHeader().setResizingAllowed(true);
        tblReceipientDetailsInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumn column = tblReceipientDetailsInfo.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setPreferredWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        //column.setResizable(false);
        column.setCellRenderer(new IconRenderer());

        NameEditor nmEdtName = new NameEditor( "Name", 90 );
        column = tblReceipientDetailsInfo.getColumnModel().getColumn(1);
        column.setPreferredWidth(175);
        //column.setMaxWidth(120);
        //column.setPreferredWidth(120);
        column.setCellEditor( nmEdtName );

        NameEditor nmEdtEmail = new NameEditor( "Email", 90 );
        column = tblReceipientDetailsInfo.getColumnModel().getColumn(2);
        //column.setMinWidth(120);
        //column.setMaxWidth(120);
        column.setPreferredWidth(174);
        column.setCellEditor( nmEdtEmail );
        //Modified for the case coeusdev219 - send functionality for agenda-start
//        NameEditor nmEdtRole = new NameEditor( "Role", 60 );
//        column = tblReceipientDetailsInfo.getColumnModel().getColumn(3);
        //column.setMinWidth(120);
        //column.setMaxWidth(120);
//        column.setPreferredWidth(120);
//        //column.setResizable(false);
//        column.setCellEditor( nmEdtRole );
        column = tblReceipientDetailsInfo.getColumnModel().getColumn(3);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        //Modified for the case coeusdev219 - send functionality for agenda-end
        column = tblReceipientDetailsInfo.getColumnModel().getColumn(4);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);

        column = tblReceipientDetailsInfo.getColumnModel().getColumn(5);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
    }

    private void setListeners(){

        btnSend.addActionListener(this);
        btnCancel.addActionListener(this);
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        btnFindPerson.addActionListener(this);
        btnFindRolodex.addActionListener(this);
        tblReceipientDetailsInfo.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){

              if(me.getClickCount() == 2) {
                  int selRow = tblReceipientDetailsInfo.getEditingRow();

                  boolean nonEmployee = ((Boolean)tblReceipientDetailsInfo.getModel().
                                   getValueAt(selRow,5)).booleanValue();

                  String stId = (String)tblReceipientDetailsInfo.
                                    getModel().getValueAt(selRow,4);

                  String stPersonName = (String)tblReceipientDetailsInfo.
                                    getModel().getValueAt(selRow,1);

                  stPersonName = stPersonName == null ? "" : stPersonName;

                  if ( stPersonName.trim().length() <= 0 ){
                      CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                                "protoKeyStPsnlFrm_exceptionCode.1069"));
                  }else if((nonEmployee) && (stId!=null)){

                      if(tblReceipientDetailsInfo.isEditing()){
                          if(tblReceipientDetailsInfo.getCellEditor() != null){
                              ((TableCellEditor)tblReceipientDetailsInfo.getCellEditor()).cancelCellEditing();
                          }
                      }
                      RolodexMaintenanceDetailForm frmRolodex =
                             new RolodexMaintenanceDetailForm('V',stId);
                      frmRolodex.showForm(mdiReference,DISPLAY_TITLE,true);
                  }else if( !nonEmployee ){
                      try{
                          if(tblReceipientDetailsInfo.isEditing()){
                              if(tblReceipientDetailsInfo.getCellEditor() != null){
                                  ((TableCellEditor)tblReceipientDetailsInfo.getCellEditor()).cancelCellEditing();
                              }
                          }
                          String loginUserName = mdiReference.getUserName();

                          //Bug Fix: Pass the person id to get the person details Start 1
                          //String personName=(String)tblReceipientDetailsInfo.getValueAt(selRow,1);
                          /*Bug Fix:to get the person details with the person id instead of the person name*/
                          //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
                          //PersonDetailForm personDetailForm =
                          //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,'D');

                          PersonDetailForm personDetailForm = new PersonDetailForm(stId ,loginUserName,'D');
                          //Bug Fix: Pass the person id to get the person details End 1

                      }catch(Exception exception){
                          exception.printStackTrace();
                      }
                  }
              }
            }
        });
    }

    private void performWindowClosing(){
        dlgParentComponent.dispose();
    }

    //raghuSV : method to check email id for each row in the table having person's name
    //starts...
    public boolean validateTableData(){
        int rowCount = tblReceipientDetailsInfo.getRowCount();
        for(int index = 0; index < rowCount ; index++){
            //Commented For PMD Check
            //String pId = (String)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(index,4);
            String pName = (String)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(index,1);
            String email = (String)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(index,2);
            //String pRole = (String)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(index,3);
            //boolean empFlag = ((Boolean)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(index,5)).booleanValue();
            if(pName == null && pName.trim().length() == 0){
                continue ;
            }
            if(email != null && email.trim().length() > 0 ){
                continue;
            }else if ( pName != null && pName.trim().length() != 0 ){
                CoeusOptionPane.showErrorDialog("Please enter the Email ID");
                tblReceipientDetailsInfo.requestFocus();
                tblReceipientDetailsInfo.setColumnSelectionInterval(2,2);
                tblReceipientDetailsInfo.setRowSelectionInterval(index,index);
                return false;
            }
        }
        return true;
    }
    //ends

    private Vector getFormData(){

        Vector mailData = new Vector();
        int rowCount = tblReceipientDetailsInfo.getRowCount();
        //Changed MailInfoBean to PersonrecipientBean with COEUSDEV 75:Rework email engine so the email body is picked up from one place
        PersonRecipientBean recipient;
        for(int index = 0; index < rowCount ; index++){
            String pId = (String)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(index,4);
            String pName = (String)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(index,1);
            String email = (String)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(index,2);
            //Commented For PMD Check
            //String pRole = (String)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(index,3);
            //boolean empFlag = ((Boolean)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(index,5)).booleanValue();

            if(pName != null && pName.trim().length() > 0 && email != null && email.trim().length() > 0 ){
                recipient = new PersonRecipientBean();
                recipient.setPersonName(pName);
                recipient.setEmailId(email);
                recipient.setPersonId(pId);
                mailData.addElement(recipient);
            }
            //COEUSDEV 75 End

            /*if(pName != null && pName.trim().length() > 0 && email != null && email.trim().length() > 0){
                mailBean = new MailInfoBean();
                mailBean.setPersonName(pName);
                mailBean.setRoleName( (pRole.trim().length() == 0) ? null : pRole );
                mailBean.setEmailId(email);
                mailBean.setNonEmployeeFlag(empFlag);
                mailBean.setPersonID(pId);
                mailData.addElement(mailBean);
            }else{
                CoeusOptionPane.showErrorDialog("Please enter the Name and Email ID");
                tblReceipientDetailsInfo.requestFocus();

            }*/

        }
        return mailData;
    }
    
    //Commented For PMD Check
//    private void sendMail(){
//
//    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlButtons = new javax.swing.JPanel();
        btnSend = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnFindPerson = new javax.swing.JButton();
        btnFindRolodex = new javax.swing.JButton();
        lblReceipient = new javax.swing.JLabel();
        scrPnReceipientDetailsInfo = new javax.swing.JScrollPane();
        tblReceipientDetailsInfo = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnSend.setFont(CoeusFontFactory.getLabelFont());
        btnSend.setText("Send");
        btnSend.setDisplayedMnemonicIndex(0);
        btnSend.setMaximumSize(new java.awt.Dimension(81, 27));
        btnSend.setMinimumSize(new java.awt.Dimension(81, 27));
        btnSend.setPreferredSize(new java.awt.Dimension(110, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnSend, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(81, 27));
        btnCancel.setMinimumSize(new java.awt.Dimension(81, 27));
        btnCancel.setPreferredSize(new java.awt.Dimension(110, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 9, 2);
        pnlButtons.add(btnCancel, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(81, 27));
        btnAdd.setMinimumSize(new java.awt.Dimension(81, 27));
        btnAdd.setPreferredSize(new java.awt.Dimension(110, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(81, 27));
        btnDelete.setMinimumSize(new java.awt.Dimension(81, 27));
        btnDelete.setPreferredSize(new java.awt.Dimension(110, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 9, 2);
        pnlButtons.add(btnDelete, gridBagConstraints);

        btnFindPerson.setFont(CoeusFontFactory.getLabelFont());
        btnFindPerson.setMnemonic('P');
        btnFindPerson.setText("Find Person");
        btnFindPerson.setMaximumSize(new java.awt.Dimension(81, 27));
        btnFindPerson.setMinimumSize(new java.awt.Dimension(105, 27));
        btnFindPerson.setPreferredSize(new java.awt.Dimension(110, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnFindPerson, gridBagConstraints);

        btnFindRolodex.setFont(CoeusFontFactory.getLabelFont());
        btnFindRolodex.setMnemonic('R');
        btnFindRolodex.setText("Find Rolodex");
        btnFindRolodex.setMaximumSize(new java.awt.Dimension(115, 27));
        btnFindRolodex.setMinimumSize(new java.awt.Dimension(105, 27));
        btnFindRolodex.setPreferredSize(new java.awt.Dimension(110, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnFindRolodex, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 0);
        add(pnlButtons, gridBagConstraints);

        lblReceipient.setFont(CoeusFontFactory.getLabelFont());
        lblReceipient.setText("recipients who will receive a copy of the Agenda");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 0);
        add(lblReceipient, gridBagConstraints);

        scrPnReceipientDetailsInfo.setBorder(new javax.swing.border.EtchedBorder());
        scrPnReceipientDetailsInfo.setMaximumSize(new java.awt.Dimension(400, 450));
        scrPnReceipientDetailsInfo.setMinimumSize(new java.awt.Dimension(400, 420));
        scrPnReceipientDetailsInfo.setPreferredSize(new java.awt.Dimension(400, 420));
        tblReceipientDetailsInfo.setFont(CoeusFontFactory.getNormalFont());
        tblReceipientDetailsInfo.setModel(new CustomTableModel());
        tblReceipientDetailsInfo.setPreferredScrollableViewportSize(null);
        tblReceipientDetailsInfo.setRowHeight(20);
        tblReceipientDetailsInfo.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tblReceipientDetailsInfo.setShowHorizontalLines(false);
        tblReceipientDetailsInfo.setShowVerticalLines(false);
        tblReceipientDetailsInfo.setOpaque(false);
        scrPnReceipientDetailsInfo.setViewportView(tblReceipientDetailsInfo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(scrPnReceipientDetailsInfo, gridBagConstraints);

    }//GEN-END:initComponents

    /** Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }

    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }

    /** Getter for property vecReceipients.
     * @return Value of property vecReceipients.
     */
    public Vector getVecReceipients() {
        return vecReceipients;
    }

    /** Setter for property vecReceipients.
     * @param vecReceipients New value of property vecReceipients.
     */
    public void setVecReceipients(Vector vecReceipients) {
        this.vecReceipients = vecReceipients;
    }

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {

        Object actionSource = actionEvent.getSource();
        if(actionSource.equals( btnAdd )){
            performAdd();
        }else if(actionSource.equals( btnCancel )){
            performWindowClosing();
        }else if(actionSource.equals( btnDelete )){
            performDelete();
        }else if(actionSource.equals( btnFindPerson )){
            performFindPerson();
        }else if(actionSource.equals( btnFindRolodex )){
            performFindRolodex();
        }else if(actionSource.equals( btnSend )){

           //raghuSV : to stop editing the cell before executing the performMailSend() method
           //starts...
           if(tblReceipientDetailsInfo.getRowCount()>0){
            if(tblReceipientDetailsInfo.isEditing()){
                if(tblReceipientDetailsInfo.getCellEditor() != null){
                    tblReceipientDetailsInfo.getCellEditor().stopCellEditing();
                 }
             }
           }
           //ends

            performMailSend();

        }
    }

    private void performAdd(){

        if (tblReceipientDetailsInfo.getModel() instanceof DefaultTableModel) {

            ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).addRow(
                new Object[]{"","","","","",new Boolean(false)});
            ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).fireTableDataChanged();

            int lastRow = tblReceipientDetailsInfo.getRowCount() - 1;
            if(lastRow >= 0){
                btnDelete.setEnabled(true);
                tblReceipientDetailsInfo.setRowSelectionInterval(lastRow,lastRow );
                tblReceipientDetailsInfo.scrollRectToVisible(
                    tblReceipientDetailsInfo.getCellRect(lastRow,ZERO_COUNT, true));

                //raghuSV: to edit the first editable cell of the row
                //starts...
                 tblReceipientDetailsInfo.editCellAt(lastRow,1);
                 tblReceipientDetailsInfo.getEditorComponent().requestFocusInWindow();
                //ends
            }
            //setSaveRequired(true);
            scrPnReceipientDetailsInfo.setViewportView(tblReceipientDetailsInfo);
        }
    }

    private void performDelete(){

        int totalRows = tblReceipientDetailsInfo.getRowCount();
        /* If there are more than one row in table then delete it */
        if (totalRows > 0) {
            /* get the selected row */
            int selectedRow = tblReceipientDetailsInfo.getSelectedRow();
            if (selectedRow != -1) {
                int selectedOption = CoeusOptionPane.
                                    showQuestionDialog(
                                    "Are you sure you want to delete this person?",
                                    CoeusOptionPane.OPTION_YES_NO,
                                    CoeusOptionPane.DEFAULT_YES);
                // if Yes then selectedOption is 0
                // if No then selectedOption is 1
                if (0 == selectedOption) {
                    ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).removeRow(selectedRow);
                    ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).fireTableDataChanged();
                    //saveRequired = true;
                    tblReceipientDetailsInfo.clearSelection();
                    // find out again row count in table
                    int newRowCount = tblReceipientDetailsInfo.getRowCount();
                    if(newRowCount == 0){
                        btnDelete.setEnabled(false);
                    }else{
                        // select the next row if exists
                        if (newRowCount > selectedRow) {
                            (tblReceipientDetailsInfo.getSelectionModel())
                            .setSelectionInterval(selectedRow,
                            selectedRow);
                        } else {
                            tblReceipientDetailsInfo.setRowSelectionInterval(
                                newRowCount - 1, newRowCount -1 );
                            tblReceipientDetailsInfo.scrollRectToVisible(
                            tblReceipientDetailsInfo.getCellRect(
                                            newRowCount - 1 ,
                                            ZERO_COUNT, true));
                        }
                    }
                }

            } // if total rows >0 and row is selected
            else{
                // if total rows >0 and row is not selected
                CoeusOptionPane.
                            showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                    "protoFndSrcFrm_exceptionCode.1057"));
            }
        }
    }

    private void performFindPerson(){

     try{
        int inIndex=tblReceipientDetailsInfo.getSelectedRow();

        if(tblReceipientDetailsInfo.isEditing()){
            tblReceipientDetailsInfo.getCellEditor().cancelCellEditing();
        }

        CoeusSearch coeusSearch =
        new CoeusSearch(mdiReference, PERSON_SEARCH, CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION );
        coeusSearch.showSearchWindow();

        Vector vSelectedPersons = coeusSearch.getMultipleSelectedRows();
        if( vSelectedPersons != null ){
            HashMap singlePersonData = null;
            for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){
                singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;
                if( singlePersonData != null ){
                    /* construct the full name of person */

                    String name = Utils.
                        convertNull(singlePersonData.get( "FULL_NAME" ));
                    String tmpEmail = Utils.
                        convertNull(singlePersonData.get( "EMAIL_ADDRESS" ));
                    String role = Utils.
                        convertNull(singlePersonData.get( "DIRECTORY_TITLE" ));
                    String personID = Utils.
                        convertNull(singlePersonData.get( "PERSON_ID" ));

                    boolean duplicate = checkDuplicatePerson(personID);

                    if( tblReceipientDetailsInfo.getSelectedRow() == -1 ){

                        Vector newEntry = new Vector();

                        newEntry.addElement( "" );
                        newEntry.addElement( name );
                        newEntry.addElement( tmpEmail );
                        newEntry.addElement( role );
                        newEntry.addElement( personID );
                        newEntry.addElement( new Boolean(false) );
                        ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).addRow( newEntry );
                        ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).fireTableDataChanged();
                        tblReceipientDetailsInfo.setRowSelectionInterval(0,0);
                        btnDelete.setEnabled(true);
                        saveRequired=true;
                        inIndex = 0;
                        continue;
                    }
                    String stPersonId = ( String)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(inIndex,4);

                    Vector vFundInfo=null;
                    if(stPersonId != null && stPersonId.trim().length() >0){
                        if(!duplicate){
                            vFundInfo = new Vector();

                            vFundInfo.addElement( "" );
                            vFundInfo.addElement( name );
                            vFundInfo.addElement( tmpEmail );
                            vFundInfo.addElement( role );
                            vFundInfo.addElement( personID );
                            vFundInfo.addElement( new Boolean(false) );
                            ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).addRow(vFundInfo);
                            ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).fireTableDataChanged();
                            int newRowCount = tblReceipientDetailsInfo.getRowCount();
                            tblReceipientDetailsInfo.getSelectionModel().setSelectionInterval(
                                    newRowCount - 1, newRowCount - 1);
                            saveRequired = true;
                        }else{
                            CoeusOptionPane.showErrorDialog("'" + name +"' "+
                                coeusMessageResources.parseMessageKey(
                                    "general_duplicateNameCode.2277"));
                        }
                     }else{
                        if(!duplicate){
                            ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setValueAt("",inIndex,0);
                            ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setValueAt(name,inIndex,1);
                            ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setValueAt(tmpEmail,inIndex,2);
                            ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setValueAt(role,inIndex,3);
                            ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setValueAt(personID,inIndex,4);
                            ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setValueAt(new Boolean(false),inIndex,5);
                            tblReceipientDetailsInfo.getSelectionModel().setLeadSelectionIndex(inIndex);
                            //saveRequired = true;
                        }else{
                            CoeusOptionPane.showErrorDialog("'" + name +"' "+
                                coeusMessageResources.parseMessageKey(
                                    "general_duplicateNameCode.2277"));
                        }
                      }
                }
            }
        }// end of vSelectedPerson != null
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void performFindRolodex(){

     try{
        int inIndex=tblReceipientDetailsInfo.getSelectedRow();
        if( tblReceipientDetailsInfo.isEditing() ){
            String value = ((javax.swing.text.JTextComponent)
            tblReceipientDetailsInfo.getEditorComponent()).getText();
            if( (value != null)){
                tblReceipientDetailsInfo.setValueAt(value,inIndex,1);
            }
            tblReceipientDetailsInfo.getCellEditor().cancelCellEditing();
        }

        CoeusSearch coeusSearch = new CoeusSearch(mdiReference, ROLODEX_SEARCH,
                                        CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
        coeusSearch.showSearchWindow();
        Vector vSelectedRolodex = coeusSearch.getMultipleSelectedRows();
        if ( vSelectedRolodex != null ){

        HashMap singleRolodexData = null;
        for(int indx = 0; indx < vSelectedRolodex.size(); indx++ ){

            singleRolodexData = (HashMap)vSelectedRolodex.get( indx ) ;
            if( singleRolodexData !=null){

                String rolodexId = Utils.
                    convertNull(singleRolodexData.get("ROLODEX_ID"));
                String email = Utils.
                    convertNull(singleRolodexData.get("EMAIL_ADDRESS"));
                String role = Utils.
                    convertNull(singleRolodexData.get("TITLE"));
                String lastName = Utils.
                    convertNull(singleRolodexData.get("LAST_NAME"));
                String firstName = Utils.
                    convertNull(singleRolodexData.get("FIRST_NAME"));
                String middleName = Utils.
                    convertNull(singleRolodexData.get("MIDDLE_NAME"));
                String suffix = Utils.
                    convertNull(singleRolodexData.get("SUFFIX"));
                String prefix = Utils.
                    convertNull(singleRolodexData.get("PREFIX"));
                String name = lastName+" "+suffix+", "+prefix+" "+firstName+" "+middleName;

                if ( lastName.length() > 0) {
                    name = ( lastName +", " + suffix + " " +
                            prefix + " " +
                            firstName + " " + middleName ).trim();
                } else {
                    name = Utils.convertNull( singleRolodexData.get("ORGANIZATION") );
                }

                // use this name variable to display in Jtable
                boolean duplicate = checkDuplicatePerson(rolodexId);
                if( tblReceipientDetailsInfo.getSelectedRow() == -1 ){
                    Vector newEntry = new Vector();
                    newEntry.addElement( "" );
                    newEntry.addElement( name );
                    newEntry.addElement( email );
                    newEntry.addElement( role );
                    newEntry.addElement( rolodexId );
                    newEntry.addElement(new Boolean(true) );
                    ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).addRow( newEntry );
                    ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).fireTableDataChanged();
                    tblReceipientDetailsInfo.setRowSelectionInterval(0,0);
                    btnDelete.setEnabled(true);
                    saveRequired = true;
                    inIndex = 0;
                    continue;
                }
                String stPersonId=( String)((DefaultTableModel)tblReceipientDetailsInfo.getModel()).getValueAt(inIndex,4);
                Vector vFundInfo = null;
                if(stPersonId != null && stPersonId.trim().length() >0){
                    if(!duplicate){
                        vFundInfo = new Vector();
                        vFundInfo.addElement( "" );
                        vFundInfo.addElement( name );
                        vFundInfo.addElement( email );
                        vFundInfo.addElement( role );
                        vFundInfo.addElement( rolodexId );
                        vFundInfo.addElement( new Boolean(true) );

                        ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).addRow(vFundInfo);
                        ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).fireTableDataChanged();

                        int newRowCount = tblReceipientDetailsInfo.getRowCount();
                        tblReceipientDetailsInfo.getSelectionModel().setSelectionInterval(
                                newRowCount - 1, newRowCount - 1);
                        saveRequired = true;
                    }else{
                        CoeusOptionPane.showErrorDialog("' " + name + "' " +
                            coeusMessageResources.parseMessageKey(
                                "general_duplicateNameCode.2277"));
                    }
                 }else{
                        if(!duplicate){

                        ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setValueAt(name,inIndex,1);
                        ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setValueAt(email,inIndex,2);
                        ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setValueAt(role,inIndex,3);
                        ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setValueAt(rolodexId,inIndex,4);
                        ((DefaultTableModel)tblReceipientDetailsInfo.getModel()).setValueAt(new Boolean(true),inIndex,5);
                        tblReceipientDetailsInfo.getSelectionModel().setLeadSelectionIndex(inIndex);
                        saveRequired = true;
                        }else{
                            CoeusOptionPane.showErrorDialog("' " + name + "' " +
                                coeusMessageResources.parseMessageKey(
                                   "general_duplicateNameCode.2277"));
                        }
                    }
               }// singleRolodexData != null //personInfo !=null
            }
        }// end of vSelectedPerson != null
    }catch(Exception e){
       // UtilFactory.log(e.getMessage()); 
    }
    }
    private void performMailSend(){
        Vector vctMailInfoBean ;

        if(!validateTableData()){
            return;
        }
        vctMailInfoBean = getFormData();
        String scheduleId = getScheduleId();
        String attachId = getAttachId();
        Vector dataObjects = new Vector();
        String connectTo = connectionURL + "/MailServlet";
        // connect to the database and get the formData for the given organization id
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(mailFunctionalityCode);
        requester.setId(scheduleId);
        dataObjects.addElement(attachId);
        dataObjects.addElement(vctMailInfoBean);
        //COEUSQA-1724 Email-Notifications For All Actions In IACUC - start
        dataObjects.addElement(ModuleConstants.PROTOCOL_MODULE_CODE);
        //COEUSQA-1724 Email-Notifications For All Actions In IACUC - end
        requester.setDataObjects(dataObjects);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);


        dlgParentComponent.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        //dlgParentComponent.getCursor().WAIT_CURSOR;
        dlgParentComponent.getGlassPane().getCursor();
        comm.send();
        ResponderBean response = comm.getResponse();
        dlgParentComponent.setCursor(java.awt.Cursor.getDefaultCursor());

        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
            "server_exceptionCode.1000"));
        }

        if (response.isSuccessfulResponse()) {
             dataObjects = response.getDataObjects();

         //raghuSV : starts...
             Boolean mail= (Boolean) dataObjects.get(0);
             if(mail.booleanValue()){
                CoeusOptionPane.showInfoDialog(
                                    coeusMessageResources.parseMessageKey(
                                        "mailFrm_exceptionCode.2003"));
             }else{
                 CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey(
                                        "mailFrm_exceptionCode.2002"));
             }

        }else{
            CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey(
                                        "mailFrm_exceptionCode.2002"));
        }
        //ends

        dlgParentComponent.dispose();
       // mdiForm.setCursor(new Cursor(java.awt.Cursor.getDefaultCursor()));
    }


    /** Getter for property mailFunctionalityCode.
     * @return Value of property mailFunctionalityCode.
     */
    public char getMailFunctionalityCode() {
        return mailFunctionalityCode;
    }

    /** Setter for property mailFunctionalityCode.
     * @param mailFunctionalityCode New value of property mailFunctionalityCode.
     */
    public void setMailFunctionalityCode(char mailFunctionalityCode) {
        this.mailFunctionalityCode = mailFunctionalityCode;
    }

        /**
     * This method is used to get the collection of PersonInfoFormBean.
     * @return Vector of Bean elements
     */

    private Vector getPersonInfo(String name){

        Vector vsearchData=null;
        RequesterBean requester = new RequesterBean();
        if(name!=null && name.trim().length() > 0){
            requester.setDataObject("GET_PERSONINFO");
            requester.setId(name);
        }
        String connectTo = connectionURL + "/coeusFunctionsServlet";
        AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){

            vsearchData = new Vector();
            PersonInfoFormBean personInfoFormBean =
                                (PersonInfoFormBean) response.getDataObject();
            if(personInfoFormBean != null){

                String stName = personInfoFormBean.getFullName();
                String eMail = personInfoFormBean.getEmail();
                String stRole = personInfoFormBean.getDirTitle();
                String stId = personInfoFormBean.getPersonID();

                vsearchData.addElement("");
                vsearchData.addElement(stName);
                vsearchData.addElement(eMail);
                vsearchData.addElement(stRole);
                vsearchData.addElement(stId);
                vsearchData.addElement(new Boolean(false));
            }
        }
        return vsearchData;
    }

    /**
     *  Method used to validate whether the Person is duplicate or not
     */

    private boolean checkDuplicatePerson(String personId){

        boolean duplicate = false;
        String oldId = "";
        int size = tblReceipientDetailsInfo.getRowCount();
        for(int index = 0; index < size; index++){
            oldId = (String)tblReceipientDetailsInfo.getValueAt(index,4);
            if(oldId != null){
                if(oldId.equals(personId)){
                    duplicate = true;
                    break;
                }
            }
        }
        return duplicate;
    }

    private boolean checkDuplicatePerson(String personId, int selectedRow){

        boolean duplicate = false;
        String oldId = "";
        int size = tblReceipientDetailsInfo.getRowCount();
        for(int index = 0; index < size; index++){
            if(index != selectedRow){
                oldId = (String)tblReceipientDetailsInfo.getValueAt(index,4);
                if(oldId != null){
                    if(oldId.equals(personId)){
                        duplicate = true;
                        break;
                    }
                }
            }
        }
        return duplicate;
    }

    /** Getter for property scheduleId.
     * @return Value of property scheduleId.
     */
    public java.lang.String getScheduleId() {
        return scheduleId;
    }

    /** Setter for property scheduleId.
     * @param scheduleId New value of property scheduleId.
     */
    public void setScheduleId(java.lang.String scheduleId) {
        this.scheduleId = scheduleId;
    }

    /** Getter for property attachId.
     * @return Value of property attachId.
     */
    public java.lang.String getAttachId() {
        return attachId;
    }

    /** Setter for property attachId.
     * @param attachId New value of property attachId.
     */
    public void setAttachId(java.lang.String attachId) {
        this.attachId = attachId;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFindPerson;
    private javax.swing.JButton btnFindRolodex;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel lblReceipient;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JScrollPane scrPnReceipientDetailsInfo;
    private javax.swing.JTable tblReceipientDetailsInfo;
    // End of variables declaration//GEN-END:variables

    /**
     * This is a Custom table model for the special review Jtable.
     */
    public class CustomTableModel extends DefaultTableModel{

        public CustomTableModel(){
            super(new Object[][]{}, new Object []
                {"Icon", "Name", "Email Id", "Role", "personid", "nonEmployeeFlag"});
        }
        public boolean isCellEditable(int row, int col){
                if((functionType == DISPLAY_MODE) || (col == 0)){
                    return false;
                }else{
                    return true;
                }
        }
        /* This method is invoked when ever the user changes the
               contents in the table cell */
        public void fireTableCellUpdated(int row,int column){
            super.fireTableCellUpdated(row,column);
        }

        public Class getColumnClass(int col){
            return Object.class;
        }
    }

/**
     * Inner Class used to provide textField as cell editor.
     * It extends DefaulCellEditor and implements TableCellEditor interface.
     * This class overides getTableCellEditorComponent method which returns the
     * editor component to the JTable Column.
     */

    class NameEditor extends DefaultCellEditor implements TableCellEditor {

        private JTextField txtName;
        int selRow = 0;
        int selCol = 0;
        String stTempPersonName = null;
        String stTempRole = null;
        String stTempEmail = null;
        String stTempFaculty;
        String stPersonId;
        boolean nonEmployeeFlag;

        /**
         * Constructor for NameEditor
         * @colName Column Name
         * @len length of the editor field.
         */
        NameEditor( String colName, int len ){

            super( new JTextField() );
            txtName = new JTextField();
            if(colName.equals("Name")){

                txtName.setDocument( new LimitedPlainDocument( len ));
                txtName.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent me){

                      if(me.getClickCount() == 2) {

                          boolean nonEmployee = ((Boolean)tblReceipientDetailsInfo.getModel().
                                           getValueAt(selRow,5)).booleanValue();
                          String stId = (String)tblReceipientDetailsInfo.
                                            getModel().getValueAt(selRow,4);
                          if (txtName.getText().equals("") ){
                              CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey(
                                        "protoKeyStPsnlFrm_exceptionCode.1069"));
                          }else if((nonEmployee) && (stId!=null)){

                              RolodexMaintenanceDetailForm frmRolodex =
                                     new RolodexMaintenanceDetailForm('V',stId);
                              frmRolodex.showForm(mdiReference,DISPLAY_TITLE,true);

                          }else if( !nonEmployee ){
                              int selRow = tblReceipientDetailsInfo.getSelectedRow();
                              try{
                                  String loginUserName = mdiReference.getUserName();

                                  //Bug Fix: Pass the person id to get the person details Start 2
                                  //String personName=(String)tblReceipientDetailsInfo.getValueAt(selRow,1);
                                  /*Bug Fix:to get the person details with the person id instead of the person name*/
                                  //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
                                  //PersonDetailForm personDetailForm =
                                  //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,'D');

                                  PersonDetailForm personDetailForm = new PersonDetailForm(stId,loginUserName,'D');
                                  //Bug Fix: Pass the person id to get the person details End 2

                              }catch(Exception exception){
                                  exception.printStackTrace();
                              }
                          }
                        }
                    }
                });
                txtName.addFocusListener(new FocusAdapter(){
                    public void focusLost(FocusEvent fe){
                        if (!fe.isTemporary()){
                           validatePersonName();
                        }
                    }
                });
                txtName.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        validatePersonName();
                    }
                });

            }else if( colName.equals("Role") ){
                txtName.setDocument(new LimitedPlainDocument(len));
                txtName.setEditable(false);
            }else if(colName.equals("Email")){

                txtName.setDocument( new LimitedPlainDocument( len ));

                txtName.addFocusListener(new FocusAdapter(){
                    public void focusLost(FocusEvent fe){
                        if (!fe.isTemporary()){
                           setEMailInfo();
                        }
                    }
                });

                txtName.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        setEMailInfo();
                    }
                });
            }
        }

        /** This overridden to get the custom cell component in the
         * JTable.
         * @param table JTable instance for which component is derived
         * @param value object value.
         * @param isSelected particular table cell is selected or not
         * @param row row index
         * @param column column index
         * @return a Component which is a editor component to the JTable cell.
         */

        public Component getTableCellEditorComponent(JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column){

            selRow = row;
            selCol = column;
            stTempPersonName = (String)tblReceipientDetailsInfo.getValueAt(row,1);
            stTempEmail = (String)tblReceipientDetailsInfo.getValueAt(row,2);
            stTempRole = (String)tblReceipientDetailsInfo.getValueAt(row,3);
            stPersonId = (String)tblReceipientDetailsInfo.getValueAt(row,4);
            nonEmployeeFlag = ((Boolean)tblReceipientDetailsInfo.getModel().
                                           getValueAt(selRow,5)).booleanValue();
            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 0 ){
                txtName.setText( (String)value );
            }else{
                txtName.setText("");
            }
            return txtName;
        }

        /**
        * Forwards the message from the CellEditor to the delegate. Tell the
        * editor to stop editing and accept any partially edited value as the
        * value of the editor.
        * @return true if editing was stopped; false otherwise
        */

        public boolean stopCellEditing() {

            if(selCol == 1){
                validatePersonName();
            }else if (selCol == 2) {
                setEMailInfo();
            }
            return super.stopCellEditing();
        }

        /** Returns the value contained in the editor.
         * @return a value contained in the editor
         */

        public Object getCellEditorValue() {
            return ((JTextField)txtName).getText();
        }

        /** This Overridden is used to handle the item state changed events.
         * @param e ItemEvent
         */

        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }

        private void setEMailInfo(){

            String stEmailValue = txtName.getText();
            if(stEmailValue != null && stEmailValue.trim().length() > 0){
                tblReceipientDetailsInfo.getModel().
                            setValueAt(stTempPersonName == null ?
                                            "":stEmailValue ,selRow,2);
            }
        }
        /**
         * Supporting method used to validate person Name for its correctness
         * existance with db data.
        */

        private void validatePersonName(){

            Vector vecData = new Vector();
            String stTxtNameValue = txtName.getText();
            if (stTxtNameValue.trim().length() <= 0) {

                tblReceipientDetailsInfo.getModel().
                            setValueAt(stTempPersonName == null ?
                                            "":stTempPersonName ,selRow,1);
                tblReceipientDetailsInfo.getModel().
                            setValueAt(stTempRole == null ?
                                            "":stTempEmail,selRow,2);

                tblReceipientDetailsInfo.getModel().
                            setValueAt(stTempRole == null ?
                                            "":stTempRole,selRow,2);

                if(tblReceipientDetailsInfo.getCellEditor() != null){
                    tblReceipientDetailsInfo.getCellEditor().cancelCellEditing();
                }

            } else {
                if(!(stTempPersonName.equalsIgnoreCase(stTxtNameValue))){
                    vecData = getPersonInfo(txtName.getText().trim());
                    if(vecData != null && vecData.size()>0){

                        String stNm=(String)vecData.get(1);
                        String stEmail=(String)vecData.get(2);
                        String stRole=(String)vecData.get(3);
                        String stId=(String)vecData.get(4);
                        Boolean nonEmpFlag = (Boolean)vecData.get(5);

                        boolean duplicate = checkDuplicatePerson(stId,selRow);
                        if(!duplicate){
                            tblReceipientDetailsInfo.getModel().setValueAt(stNm,selRow,1);
                            tblReceipientDetailsInfo.getModel().setValueAt(stEmail,selRow,2);
                            tblReceipientDetailsInfo.getModel().setValueAt(stRole,selRow,3);
                            tblReceipientDetailsInfo.getModel().setValueAt(stId,selRow,4);
                            tblReceipientDetailsInfo.getModel().setValueAt(nonEmpFlag,selRow,5);
                            if(tblReceipientDetailsInfo.getCellEditor() !=null){
                                tblReceipientDetailsInfo.getCellEditor().cancelCellEditing();
                            }
                            saveRequired = true;
                        }else{
                            CoeusOptionPane.showErrorDialog("'" + stNm +"' "+
                                coeusMessageResources.parseMessageKey(
                                   "general_duplicateNameCode.2277"));
                            tblReceipientDetailsInfo.getModel().setValueAt("",selRow,1);
                            if(tblReceipientDetailsInfo.getCellEditor() !=null){
                                tblReceipientDetailsInfo.getCellEditor().cancelCellEditing();
                            }
                        }
                    }else{
                        CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                    "protoCorroFrm_exceptionCode.1054"));
                        tblReceipientDetailsInfo.getModel().setValueAt(stTempPersonName,selRow,1);
                        tblReceipientDetailsInfo.getModel().setValueAt(stTempEmail,selRow,2);
                        tblReceipientDetailsInfo.getModel().setValueAt(stTempRole,selRow,3);
                        tblReceipientDetailsInfo.getModel().setValueAt(stPersonId,selRow,4);
                        tblReceipientDetailsInfo.getModel().setValueAt(new Boolean(nonEmployeeFlag),selRow,5);

                        if(tblReceipientDetailsInfo.getCellEditor() !=null){
                          tblReceipientDetailsInfo.getCellEditor().cancelCellEditing();
                        }
                    }
                    tblReceipientDetailsInfo.getSelectionModel().setLeadSelectionIndex(selRow);
                }else{
                    if(tblReceipientDetailsInfo.getCellEditor() !=null){
                          tblReceipientDetailsInfo.getCellEditor().cancelCellEditing();
                    }
                }
            }
        }
    }
}
