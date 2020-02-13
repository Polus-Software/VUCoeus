/*
 * SponsorDetailForm.java
 *
 * Created on January 24, 2005, 3:59 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */ 

/**
 *
 * @author  jinum
 */

package edu.mit.coeus.sponsormaint.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.sponsormaint.bean.SponsorContactBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

public class SponsorDetailForm extends javax.swing.JPanel  implements
ActionListener, ListSelectionListener, ChangeListener {
    
    private SponsorMaintenanceForm sponsorMaintenanceForm;
    private SponsorContactBean sponsorContactBean;
    private CoeusAppletMDIForm mdiForm;
    private ContactListTableModel contactListTableModel;
    public ContactListEditor contactListEditor;
    private ContactListRenderer contactListRenderer;
    private ListSelectionModel contactsSelectionModel;
    private JComboBox cmbContactType;
    private CoeusVector cvContactsData;
    private CoeusVector cvSponsorType;
    private CoeusVector cvDeletedData;
    private CoeusMessageResources coeusMessageResources;
    
    private String sponsorCode;
    private static final java.awt.Color  PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");

    private static final int HAND_ICON_COLUMN = 0;
    private static final int CONTACT_TYPE = 1;
    private static final int NAME_COLUMN = 2;
    private static final String EMPTY_STRING = "";
    private final String SERVLET = "/SponsorMaintenanceServlet";
    private final String TITLE_SPONSOR = "Sponsor Maintenance";
    private final String CONNECT_TO =
        CoeusGuiConstants.CONNECTION_URL + "/spMntServlet";
    private static final String DEL_MESSAGE = "awardContacts_exceptionCode.1204";
    /** Duplicate contct information
     */
    private static final String DUPLICATE_INFO = "awardContacts_exceptionCode.1207";
    private final String NO_SPONSOR_CONTACT_TYPE ="spnrMntFrm_exceptionCode.1124";
    /** This is used to hold the mode .
     *D for Display, I for Add, U for Modify
     */
    private char functionType;
    
    public boolean modified = false;
    
    /** Creates new form SponsorDetailForm */
    public SponsorDetailForm(String sponsorCode,CoeusAppletMDIForm mdiForm,SponsorMaintenanceForm sponsorMaintenanceForm) {
        try{
            this.mdiForm = (CoeusAppletMDIForm)mdiForm;
            this.sponsorMaintenanceForm = sponsorMaintenanceForm;
            this.sponsorCode = sponsorCode;
            initComponents();
            cvContactsData = new CoeusVector();
            cvSponsorType = new CoeusVector();
            cvDeletedData = new CoeusVector();
            coeusMessageResources = CoeusMessageResources.getInstance();
            contactListTableModel = new ContactListTableModel();
            contactListRenderer =  new ContactListRenderer();
            contactListEditor = new ContactListEditor();
            registerComponents();
            setTableEditors();
            setFormData(getFormData());
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /*registers the components and listeners*/
    public void registerComponents() {
        
        tblContactList.setModel(contactListTableModel);
        tblContactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsSelectionModel = tblContactList.getSelectionModel();
        contactsSelectionModel.addListSelectionListener(this);
        tblContactList.setSelectionModel(contactsSelectionModel);
        java.awt.Component[] components = {tblContactList, btnAdd, btnModify, btnDelete };
        
        btnAdd.addActionListener(this);
        btnModify.addActionListener(this);
        btnDelete.addActionListener(this);
        btnOK.addActionListener(this);
        btnCancel.addActionListener(this);
        tblContactList.addMouseListener( new PersonDisplayAdapter());
        tbdPnSponsorForm.addChangeListener(this);
    }
        /*To set the controls state*/
    public void setControls(boolean value){
       btnAdd.setEnabled(value);
       btnModify.setEnabled(value);
       btnDelete.setEnabled(value);
       btnOK.setEnabled(value);
//       tblContactList.setEnabled(value);
    }
    /*to set the sizes to the columns and set the editors and the renderers*/
    private void setTableEditors(){
        
        tblContactList.setRowHeight(22);
        tblContactList.setShowHorizontalLines(false);
        tblContactList.setShowVerticalLines(false);
        
        JTableHeader header = tblContactList.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
        header.setReorderingAllowed(false);
        
        tblContactList.setOpaque(false);
        tblContactList.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        
        TableColumn column = tblContactList.getColumnModel().getColumn(HAND_ICON_COLUMN);
        
        column.setMaxWidth(30);
        column.setMinWidth(30);
        column.setPreferredWidth(30);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = tblContactList.getColumnModel().getColumn(1);
        column.setPreferredWidth(120);
        column.setCellRenderer(contactListRenderer);
        column.setCellEditor(contactListEditor);
        header.setReorderingAllowed(false);
        
        column = tblContactList.getColumnModel().getColumn(2);
        column.setPreferredWidth(200);
        column.setCellRenderer(contactListRenderer);
        header.setReorderingAllowed(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbdPnSponsorForm = new edu.mit.coeus.utils.CoeusTabbedPane();
        scrPnDetails = new javax.swing.JScrollPane();
        scrPnContactList = new javax.swing.JScrollPane();
        pnlContact = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        scrPnPerson = new javax.swing.JScrollPane();
        tblContactList = new javax.swing.JTable();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());
        
        // JM 6-11-2015 static height and width for panels
        final int WIDTH = 680;
        final int HEIGHT = 400;        

        setMinimumSize(new java.awt.Dimension(WIDTH, HEIGHT)); // JM 6-10-2015 was 650,355
        setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT)); // JM 6-10-2015 was 650,355
        tbdPnSponsorForm.setMinimumSize(new java.awt.Dimension(WIDTH, HEIGHT)); // JM 6-10-2015 was 570,355
        tbdPnSponsorForm.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT)); // JM 6-10-2015 was 610,355
        scrPnDetails.setFont(CoeusFontFactory.getLabelFont());
        scrPnDetails.setMinimumSize(new java.awt.Dimension(WIDTH, HEIGHT)); // JM 6-10-2015 was 560,355
        scrPnDetails.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT)); // JM 6-10-2015 was 610,355
        tbdPnSponsorForm.addTab("Sponsor Details", scrPnDetails);

        scrPnContactList.setFont(CoeusFontFactory.getLabelFont());
        scrPnContactList.setMaximumSize(new java.awt.Dimension(WIDTH, HEIGHT)); // JM 6-10-2015 was 770,400
        scrPnContactList.setMinimumSize(new java.awt.Dimension(WIDTH, HEIGHT)); // JM 6-10-2015 was 560,355
        scrPnContactList.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT)); // JM 6-10-2015 was 560,355
        pnlContact.setLayout(new java.awt.GridBagLayout());

        pnlContact.setMinimumSize(new java.awt.Dimension(655, HEIGHT - 40)); // JM 6-10-2015 was 555,325
        pnlContact.setPreferredSize(new java.awt.Dimension(655, HEIGHT - 40)); // JM 6-10-2015 was 555,325
        // JM END
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(100, 25));
        btnAdd.setMinimumSize(new java.awt.Dimension(73, 23));
        btnAdd.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 0);
        pnlContact.add(btnAdd, gridBagConstraints);

        btnModify.setFont(CoeusFontFactory.getLabelFont());
        btnModify.setMnemonic('M');
        btnModify.setText("Modify");
        btnModify.setMaximumSize(new java.awt.Dimension(100, 25));
        btnModify.setMinimumSize(new java.awt.Dimension(73, 23));
        btnModify.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 0);
        pnlContact.add(btnModify, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(100, 25));
        btnDelete.setMinimumSize(new java.awt.Dimension(73, 23));
        btnDelete.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 0);
        pnlContact.add(btnDelete, gridBagConstraints);

        scrPnPerson.setFont(CoeusFontFactory.getNormalFont());
        scrPnPerson.setMinimumSize(new java.awt.Dimension(470, 320));
        scrPnPerson.setPreferredSize(new java.awt.Dimension(470, 320));
        scrPnPerson.setRequestFocusEnabled(false);
        tblContactList.setFont(CoeusFontFactory.getNormalFont());
        tblContactList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrPnPerson.setViewportView(tblContactList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 6;
        pnlContact.add(scrPnPerson, gridBagConstraints);

        scrPnContactList.setViewportView(pnlContact);

        tbdPnSponsorForm.addTab("Institutional Contacts", scrPnContactList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.weighty = 1.0;
        add(tbdPnSponsorForm, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMinimumSize(new java.awt.Dimension(73, 23));
        btnOK.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(btnCancel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnModify;
    public javax.swing.JButton btnOK;
    public javax.swing.JPanel pnlContact;
    public javax.swing.JScrollPane scrPnContactList;
    public javax.swing.JScrollPane scrPnDetails;
    public javax.swing.JScrollPane scrPnPerson;
    public edu.mit.coeus.utils.CoeusTabbedPane tbdPnSponsorForm;
    public javax.swing.JTable tblContactList;
    // End of variables declaration//GEN-END:variables
    
//    public static void main(String args[]){
//        JFrame frame = new JFrame();
//        frame.getContentPane().add(new SponsorDetailForm());
//        frame.setSize(830,630);
//        frame.setVisible(true);
//    }
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        if(functionType == 'D'){
            setDefaultFocus();
        }else{
            if(changeEvent.getSource() == tbdPnSponsorForm){
                int selTab = 0;
                selTab = tbdPnSponsorForm.getSelectedIndex();
                if(sponsorMaintenanceForm != null){
                    if(selTab == 0){
                        sponsorMaintenanceForm.setFocusTraversal(SponsorMaintenanceForm.SPONSOR_DETAIL_TAB);
                        //sponsorMaintenanceForm.setDefaultFocus();
                    }else if(selTab == 1){
                        //setDefaultFocus();
                        sponsorMaintenanceForm.setFocusTraversal(SponsorMaintenanceForm.INSTITUTIONAL_CONTACT_TAB);
                    }
                }
            }
        }
    }
    
    public void setDefaultFocus(){
         SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                btnCancel.requestFocusInWindow();
            }
        });
    }
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if( !listSelectionEvent.getValueIsAdjusting() ){
            int selectedRow = tblContactList.getSelectedRow();
            if (selectedRow != -1) {
                sponsorContactBean = (SponsorContactBean)cvContactsData.get(selectedRow);
            }
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == btnAdd){
            performFindPerson();
        }else if(actionEvent.getSource() == btnModify){
            performModifyAction();
        }else if(actionEvent.getSource() == btnDelete){
            performDeleteAction();
        }else if(actionEvent.getSource() == btnOK){
            performOKAction();
        }else if(actionEvent.getSource() == btnCancel){
            performCancelAction();
        }
    }
    
    /* supporting method used for person search */
    private void performFindPerson(){
        try{
            //Bug Fix Id:1509
            //Jinu - start
            if(cvSponsorType != null && cvSponsorType.size()>0){
                contactListEditor.stopCellEditing();
                CoeusSearch proposalSearch = null;
                proposalSearch = new CoeusSearch( mdiForm, "PERSONSEARCH",
                CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ); //TWO_TABS ) ;
                proposalSearch.showSearchWindow();
                Vector vSelectedPersons = proposalSearch.getMultipleSelectedRows();
                if ( vSelectedPersons != null ){
                    HashMap singlePersonData = null;
                    for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){
                        
                        singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;
                        
                        if( singlePersonData == null || singlePersonData.isEmpty() ){
                            continue;
                        }
                        String personId = checkForNull(singlePersonData.get( "PERSON_ID" ));//result.get( "PERSON_ID" )) ;
                        String personName = checkForNull(singlePersonData.get( "FULL_NAME" ));//result.get( "FULL_NAME" )) ;
                        String homeUnit = checkForNull( singlePersonData.get( "HOME_UNIT" ));//result.get( "HOME_UNIT" ));
                        
                        boolean faculty
                        = checkForNull(singlePersonData.get("IS_FACULTY")).
                        equalsIgnoreCase("y") ? true : false;
                        
                        ComboBoxBean selBean = (ComboBoxBean)cvSponsorType.get(0);
                        int contactTypeCode = -1;
                        if(selBean != null){
                            contactTypeCode = Integer.parseInt(selBean.getCode());
                        }
                        SponsorContactBean sponsorContactBean = new SponsorContactBean();
                        sponsorContactBean.setSponsorCode(sponsorCode);
                        sponsorContactBean.setSponsorContactTypeCode(contactTypeCode);
                        sponsorContactBean.setPersonId(personId);
                        sponsorContactBean.setPersonName(personName);
                        sponsorContactBean.setAcType(TypeConstants.INSERT_RECORD);
                        cvContactsData.add(sponsorContactBean);
                        modified = true;
                        contactListTableModel.fireTableDataChanged();
                        int index = searchIndex(cvContactsData, sponsorContactBean);
                        if(index != -1){
                            tblContactList.setRowSelectionInterval(index, index);
                        }
                        contactListEditor.stopCellEditing();
                    } //end of for loop
                }//end of vSelectedPersons != null
            }else{
                String msg = coeusMessageResources.parseMessageKey(NO_SPONSOR_CONTACT_TYPE);
                CoeusOptionPane.showErrorDialog(msg);
            }
            //Jinu - end
            
        }catch( Exception err ){
            err.printStackTrace();
        }
    }

    private void performModifyAction(){
        try{
            int selRow = tblContactList.getSelectedRow();
            if(selRow != -1){
                sponsorContactBean = (SponsorContactBean)cvContactsData.get(selRow);
                CoeusSearch proposalSearch = null;
                proposalSearch = new CoeusSearch( mdiForm, "PERSONSEARCH",
                CoeusSearch.TWO_TABS ); //TWO_TABS ) ;
                proposalSearch.showSearchWindow();
                Vector vSelectedPersons = proposalSearch.getMultipleSelectedRows();
                if ( vSelectedPersons != null ){
                    HashMap singlePersonData = null;
                    for(int indx = 0; indx < vSelectedPersons.size(); indx++ ){

                        singlePersonData = (HashMap)vSelectedPersons.get( indx ) ;

                        if( singlePersonData == null || singlePersonData.isEmpty() ){
                            continue;
                        }
                        String personId = checkForNull(singlePersonData.get( "PERSON_ID" ));//result.get( "PERSON_ID" )) ;
                        String personName = checkForNull(singlePersonData.get( "FULL_NAME" ));//result.get( "FULL_NAME" )) ;
                        String homeUnit = checkForNull( singlePersonData.get( "HOME_UNIT" ));//result.get( "HOME_UNIT" ));

                        boolean faculty
                        = checkForNull(singlePersonData.get("IS_FACULTY")).
                        equalsIgnoreCase("y") ? true : false;

                        if(sponsorContactBean != null){
                            if(!sponsorContactBean.getPersonId().equals(personId)){
                                sponsorContactBean.setPersonId(personId);
                                sponsorContactBean.setPersonName(personName);
                                if(sponsorContactBean.getAcType() == null ){
                                    sponsorContactBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                                modified = true;
                                contactListTableModel.fireTableDataChanged();
                                int index = searchIndex(cvContactsData, sponsorContactBean);
                                if(index != -1){
                                    tblContactList.setRowSelectionInterval(index, index);
                                }
                                break;
                            }
                        }
                    } //end of for loop
                }//end of vSelectedPersons != null
            }else{
                String msg = coeusMessageResources.parseMessageKey("protoBaseWin_exceptionCode.1051");
                CoeusOptionPane.showInfoDialog(msg);
            }
        }catch( Exception err ){
            err.printStackTrace();
        }
    }

    private void performDeleteAction(){
        int selRow = tblContactList.getSelectedRow();
        if( selRow == -1) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DEL_MESSAGE));
            return;
        }else{
            String msg = coeusMessageResources.parseMessageKey("generalDelConfirm_exceptionCode.2100");
            int selectedOption = CoeusOptionPane.showQuestionDialog(msg+" row? ",
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if( selectedOption == JOptionPane.YES_OPTION ){
                sponsorContactBean = (SponsorContactBean)cvContactsData.get(selRow);
                if(sponsorContactBean.getAcType()== null || 
                sponsorContactBean.getAcType().equals(TypeConstants.UPDATE_RECORD) ){
                    sponsorContactBean.setAcType(TypeConstants.DELETE_RECORD);
                    cvDeletedData.addElement(sponsorContactBean);
                    cvContactsData.removeElementAt(selRow);
                }else if(sponsorContactBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    cvContactsData.removeElementAt(selRow);
                }
                modified = true;
                contactListTableModel.fireTableDataChanged();
                if(cvContactsData.size()>0){
                    tblContactList.setRowSelectionInterval(0, 0);
                }
            }
        }
    }
    private void performOKAction(){
        SponsorContactBean bean = checkForDuplicate();
        if(bean != null){
            int index = searchIndex(cvContactsData, bean);
            if(index != -1){
                tbdPnSponsorForm.setSelectedIndex(1);
                tblContactList.requestFocus();
                tblContactList.setRowSelectionInterval(index, index);
            }
            return;
        }
        if(sponsorMaintenanceForm != null){
            sponsorMaintenanceForm.performOKAction();
        }
    }
    private void performCancelAction(){
        if(sponsorMaintenanceForm != null){
            sponsorMaintenanceForm.performCancelAction();
        }
    }
    /**
     * Method to search index of an object
     * @param coeusVector CoeusVector
     * @param object Object
     * @return int
     **/
    //Modify private to public for fix duplicate contact using cancel button by tarique
    public int searchIndex(CoeusVector coeusVector, Object object){
        if(object != null && coeusVector != null){
            SponsorContactBean bean = (SponsorContactBean)object;
            SponsorContactBean vectorBean = null;
            for(int i=0;i<coeusVector.size();i++){
                vectorBean = (SponsorContactBean)coeusVector.elementAt(i);
                if(vectorBean.getSponsorCode().equals(bean.getSponsorCode()) &&
                   vectorBean.getPersonId().equals(bean.getPersonId()) &&
                   vectorBean.getSponsorContactTypeCode() == bean.getSponsorContactTypeCode() ){
                    return i;
                }
            }
        }
        return -1;
    }
    /**
     * Method to search SponsorContactBean
     * @param personName String
     * @return SponsorContactBean
     **/
    private SponsorContactBean search(String personName){
        if(cvContactsData != null && personName != null){
            SponsorContactBean vectorBean = null;
            for(int i=0;i<cvContactsData.size();i++){
                vectorBean = (SponsorContactBean)cvContactsData.elementAt(i);
                if(vectorBean.getPersonName().equals(personName.trim())){
                    return vectorBean;
                }
            }
        }
        return null;
    }
    
    /**
     * Method to check For Duplicate SponsorContactBean
     * @return boolean
     **/
    //Modify private to public for fix duplicate contact using cancel button by tarique
    public SponsorContactBean checkForDuplicate(){
        if(cvContactsData!= null && cvContactsData.size() > 0){
            Equals contactType;
            Equals personId;
            for(int index = 0;index <cvContactsData.size();index++){
                SponsorContactBean contactsBean = (SponsorContactBean)cvContactsData.get(index);
                contactType = new Equals("sponsorContactTypeCode",new Integer(contactsBean.getSponsorContactTypeCode()));
                personId = new Equals("personId", contactsBean.getPersonId());
                And AwContact = new And(contactType , personId);
                CoeusVector cvFilterd = cvContactsData.filter(AwContact);
                if(cvFilterd!= null && cvFilterd.size() > 1) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DUPLICATE_INFO));
                    return (SponsorContactBean)cvFilterd.get(0);
                }
            }
            
        }
        return null;
    }
    //supporting method to check for null value
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();
    }
    /** sets the form view mode.
     * @param functionType Possible Values:
     * Display Mode = 'D'
     * Modify Mode = 'M'
     * New Mode = 'N'
     */
    public final void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    /** returns the function Type
     * @return function Type
     */
    public final char getFunctionType() {
        return functionType;
    }
    /** returns form data.
     * @return returns form data.
     */
    public Object getFormData() throws CoeusException{
        try{
            RequesterBean requesterBean = new RequesterBean();
            
            requesterBean.setRequestedForm(TITLE_SPONSOR);
            requesterBean.setFunctionType('H');
            requesterBean.setDataObject(sponsorCode);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CONNECT_TO, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            HashMap data=null;
            if(responderBean!= null){
                if(!responderBean.isSuccessfulResponse()){
                    throw new CoeusException(responderBean.getMessage(), 1);
                }else{
                    data = (HashMap)responderBean.getDataObject();
                }
            }
            return data;
        }catch(CoeusException exception){
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        return null;
    }
    /** sets data to form.
     * @param data data to be set.
     */ 
    public void setFormData(Object data){
        HashMap contactData = (HashMap)data;
        if(contactData != null){
            cvContactsData = (CoeusVector)contactData.get(SponsorContactBean.class);
            if(cvContactsData == null){
                cvContactsData = new CoeusVector();
            }
            cvSponsorType = (CoeusVector)contactData.get(ComboBoxBean.class);
            if(cvSponsorType == null){
                cvSponsorType = new CoeusVector();
            }
            contactListTableModel.setData(cvContactsData);
            contactListTableModel.fireTableDataChanged();
            if( cvContactsData != null && cvContactsData.size() > 0){
                tblContactList.setRowSelectionInterval(0,0);
                sponsorContactBean = (SponsorContactBean)cvContactsData.get(0);
            }
        }
    }
    
    /**
     * Getter for property cvContactsData.
     * @return Value of property cvContactsData.
     */
    public edu.mit.coeus.utils.CoeusVector getCvContactsData() {
        return cvContactsData;
    }
    
    /**
     * Setter for property cvContactsData.
     * @param cvContactsData New value of property cvContactsData.
     */
    public void setCvContactsData(edu.mit.coeus.utils.CoeusVector cvContactsData) {
        this.cvContactsData = cvContactsData;
    }
    
    /**
     * Getter for property cvDeletedData.
     * @return Value of property cvDeletedData.
     */
    public edu.mit.coeus.utils.CoeusVector getCvDeletedData() {
        return cvDeletedData;
    }
    
    /**
     * Setter for property cvDeletedData.
     * @param cvDeletedData New value of property cvDeletedData.
     */
    public void setCvDeletedData(edu.mit.coeus.utils.CoeusVector cvDeletedData) {
        this.cvDeletedData = cvDeletedData;
    }
    
    
    /*Renderer for the table columns*/
    class ContactListRenderer extends DefaultTableCellRenderer{
        public ContactListRenderer() {
            BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray);
            setBorder(bevelBorder);
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus, int row, int column){
            
            switch(column) {
                case HAND_ICON_COLUMN:
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case CONTACT_TYPE:
                    setText(value.toString());
                    if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        setBackground(PANEL_BACKGROUND_COLOR);
                    }else{
                        setBackground(Color.WHITE);
                    }
                    return this;
                case NAME_COLUMN:
                    setText(value.toString());
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                    
            }
            return null;
        }
        
        public void setFunctionType(char functionType) {
            if( functionType == TypeConstants.DISPLAY_MODE ) {
                cmbContactType.setBackground(PANEL_BACKGROUND_COLOR);
            }
        }
        
    }
    
    /*Table model*/
    class ContactListTableModel extends AbstractTableModel{
        private CoeusVector cvContactsData;
        
        String colNames[] = {"","Contact Type", "Name"};
        Class[] colTypes = new Class [] {Object.class , String.class, String.class};
        
        public void setData(CoeusVector cvContactsData) {
            this.cvContactsData = cvContactsData;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            SponsorContactBean sponsorContactBean = (SponsorContactBean)cvContactsData.get(rowIndex);
            switch(columnIndex) {
                case HAND_ICON_COLUMN:
                    return EMPTY_STRING;
                case CONTACT_TYPE:
                    int contactTypeCode = sponsorContactBean.getSponsorContactTypeCode();
                    CoeusVector filteredVector = cvSponsorType.filter(
                    new Equals("code", ""+contactTypeCode));
                    if(filteredVector!=null && filteredVector.size() > 0){
                        ComboBoxBean comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                        return comboBoxBean;
                    }else{
                        return new ComboBoxBean("","");
                    }
                case NAME_COLUMN:
                    String personName = sponsorContactBean.getPersonName() == null ?EMPTY_STRING:sponsorContactBean.getPersonName();
                    return personName;
            }
            return EMPTY_STRING;
            
        }
        
        
        
        public void setValueAt(Object value, int row, int column){
            if(cvContactsData == null) return;
           
            SponsorContactBean sponsorContactBean = (SponsorContactBean)cvContactsData.get(row);
            
            switch(column){
                case CONTACT_TYPE:
                    if(value==null || value.toString().equals(EMPTY_STRING)) return ;
                    ComboBoxBean comboBoxBean = (ComboBoxBean)cvSponsorType.filter(new Equals("description", value.toString())).get(0);
                    int contactTypeCode = -1;
                    if(comboBoxBean != null){
                        contactTypeCode = Integer.parseInt(comboBoxBean.getCode());
                    }
                    if(sponsorContactBean != null){
                        if(sponsorContactBean.getSponsorContactTypeCode() != contactTypeCode){
                            if( contactTypeCode != sponsorContactBean.getSponsorContactTypeCode() ){
                                sponsorContactBean.setSponsorContactTypeCode(contactTypeCode);
                                if( sponsorContactBean.getAcType() == null ){
                                    sponsorContactBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                                modified = true;
                            }
                        }
                    }
                    
                    //contactListEditor.stopCellEditing();
                    break;
                case NAME_COLUMN:
                    sponsorContactBean.setPersonName(value.toString().trim());
                    if( sponsorContactBean.getAcType() == null ){
                        sponsorContactBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    modified = true;
            }
            
            
        }
        
        public boolean isCellEditable(int row, int column) {
            if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                return false;
            }else {
                if(column == 1) {
                    return true;
                }else{
                    return false;
                }
            }
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        
        
        public int getRowCount() {
            if( cvContactsData == null ) return 0;
            return cvContactsData.size();
            
        }
    }
    
    class ContactListEditor extends AbstractCellEditor implements TableCellEditor, ActionListener{
        private JComboBox cmbContactType;
        private boolean populated = false;
        private int column;
        ContactListEditor() {
            cmbContactType = new JComboBox();
            cmbContactType.addActionListener(this);
            if (functionType == 'D'){
                cmbContactType.setEditable(false);
            }
        }
        private void populateCombo() {
            cmbContactType.setModel(new DefaultComboBoxModel(cvSponsorType));
        }
        
        
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ComboBoxBean comboBoxBean = (ComboBoxBean)value;
            this.column = column;
            switch(column) {
                case CONTACT_TYPE:
                    if(! populated) {
                        populateCombo();
                        populated = true;
                    }
                    if(comboBoxBean != null){
                        if(comboBoxBean.getDescription() == null || comboBoxBean.getDescription().equals(EMPTY_STRING)) {
                            ComboBoxBean selBean = (ComboBoxBean)cvSponsorType.get(0);
                            cmbContactType.setSelectedItem(selBean);
                            return cmbContactType;
                        }
                        cmbContactType.setSelectedItem(value);
                    }
                    return cmbContactType;
            }
            return null;
        }
        
        public Object getCellEditorValue() {
            this.column = column;
            switch(column) {
                case CONTACT_TYPE:
                    return cmbContactType.getSelectedItem();
            }
            return cmbContactType;
        }
        public int getClickCountToStart(){
            return 1;
        }
        public boolean stopCellEditing(){
            return super.stopCellEditing();
        }
       
//        public void mouseClicked(MouseEvent me) {
//            stopCellEditing();            
//            int selRow = tblContactList.getSelectedRow();
////            SponsorContactBean bean = (SponsorContactBean)cvContactsData.get(selRow);
//
//            if ( me.getClickCount() == 2) {
//                if(selRow != -1){
//                    String personName =
//                    (String)tblContactList.getModel().getValueAt(selRow,2);
//                    String loginUserName = mdiForm.getUserName();
//                    try{
//                        PersonDetailForm personDetailForm =
//                        new PersonDetailForm(personName,loginUserName,TypeConstants.DISPLAY_MODE);
//
//
//                    }catch ( Exception e) {
//                        CoeusOptionPane.showInfoDialog( e.getMessage() );
//                    }
//                }
//            }
//        }
//        
//        public void mouseEntered(MouseEvent e) {
//        }
//        
//        public void mouseExited(MouseEvent e) {
//        }
//        
//        public void mousePressed(MouseEvent e) {
//        }
//        
//        public void mouseReleased(MouseEvent e) {
//        }
        
        public void actionPerformed(ActionEvent e) {
            stopCellEditing();    
        }
        
    }
    // supporting class to display PersonDetails on
    // double clicking of any sponsor contact row.
    class PersonDisplayAdapter extends MouseAdapter {
        public void mouseClicked( MouseEvent me ) {
            int selRow = tblContactList.getSelectedRow();
//            SponsorContactBean bean = (SponsorContactBean)cvContactsData.get(selRow);
            if ( me.getClickCount() == 2) {
                if(selRow != -1){
                    String personName =
                    (String)tblContactList.getModel().getValueAt(selRow,2);
                    SponsorContactBean bean = search(personName);
                    if(bean != null){
                        String personID = bean.getPersonId();
                        String loginUserName = mdiForm.getUserName();
                        try{
                            PersonDetailForm personDetailForm =
                            new PersonDetailForm(personID,loginUserName,TypeConstants.DISPLAY_MODE);


                        }catch ( Exception e) {
                            e.printStackTrace();
                            CoeusOptionPane.showInfoDialog( e.getMessage() );
                        }
                    }
                }
//                tblContactList.getCellEditor(selRow,1).cancelCellEditing();
//                ((NameEditor)tblContactList.getCellEditor(selRow,
//                1)).cancelCellEditing();
                
            }
        }
    }
    
}
