/**
 * @(#)ReferenceNumberForm.java  1.0  July 11, 2004, 4:38 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */ 

package edu.mit.coeus.utils.refno;

import edu.mit.coeus.bean.ReferencesBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.gui.ProtocolDetailForm;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.Date;

/** 
 * ReferenceNumberForm is an form object which is used to display
 * the Reference Numbers and it is used to add/delete/display/Modify the Reference Numbers.
 * @version 1.0  July 11, 2004, 4:38 PM
 * @author Raghunath P.V.
 */
public class ReferenceNumberForm extends javax.swing.JComponent 
                implements TypeConstants, ActionListener, FocusListener, ListSelectionListener{
    
    private char functionType;
    private boolean saveRequired;
    private char windowType;
    
    private int lastSelectedRow = 0;
    private String prvComments = "";
    
    private Vector vecReferenceTypeCode;
    private Vector vecDeletedReferenceNumbers;

    private Vector vecReferenceNumbersData;
    
    private Vector vecRefCodeDescriptions;

    //private Vector vecColumnNames;// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
    private String strReferenceTypeCode;
    private String strReferenceKey;
    private String strReferenceApplDate;
    private String strReferenceApprDate;
    private String strReferenceExprnDate ="";
    
    private String protocolNumber;
    
    // Combo used as a Cell Editor for special review code
    private JComboBox referenceCombo;
    
    /** instance of DateUtils object which will be used for formatting dates */
    private DateUtils dtUtils;
    /** used to parse the dates which are represented as strings */
    private java.text.SimpleDateFormat dtFormat;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final int ZERO_COUNT = 0;
    private TableCellEditor dateEditComponent;
    
    private static final char TAB_WINDOW = 'T';
    private static final char RESPONSE_WINDOW = 'R';
    
    
    private boolean firstEntry = false;
    
    //Added for module code-start
    int moduleCode = 0;
    int columnOneWidth = 0;
    int columnTwoWidth = 0;
    int columnThreeWidth = 0;
    int columnFourWidth = 0;
    int columnFifthWidth = 0;
    //Added for module code-end
    
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    CoeusAppletMDIForm mdiForm;
    
    private int previousSelRow = -1;

    /** Creates new form ReferenceNumberForm */
    public ReferenceNumberForm() {
    }
    
    /** Creates new form <CODE>ReferenceNumberForm</CODE>
     *
     * @param functionType this will open the different mode like Display. 'D' 
     * specifies that the form is in Display Mode
     * @param vecSpecialReviewData is a Vector of Special review info beans.
     */
    public ReferenceNumberForm(char functionType, Vector referenceNumbersData, int moduleCode){
        
        this.functionType = functionType;
        this.vecReferenceNumbersData = referenceNumbersData;
        this.dtUtils = new DateUtils();
        this.dtFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
        this.dateEditComponent = new DateEditor("ApplDate");
        this.referenceCombo = new JComboBox();
        this.referenceCombo.setFont(CoeusFontFactory.getNormalFont());
        this.coeusMessageResources = CoeusMessageResources.getInstance();
        this.vecDeletedReferenceNumbers = new Vector();
        //Added for module code-start
        this.moduleCode = moduleCode;
        //Added for module code-end
        setColumnNames();
    }
    
    /** 
      * This method is used to initialize the components, set the data in the components.
      * @param mdiForm is a reference of CoeusAppletMDIForm
      * @return a JPanel containing all the components with the data populated.
      */
    public JComponent showReferenceNumbers(CoeusAppletMDIForm form){
        
        mdiForm = form;
        if( windowType == TAB_WINDOW ){
            pnlProposalDescription.setVisible(false);
            sptrProposalDescription.setVisible(false); 
        }else if(windowType == RESPONSE_WINDOW){
            pnlProposalDescription.setVisible(true);
            sptrProposalDescription.setVisible(true);
        }
        setFormData();
        setListeners();
        formatFields();
        setTableEditors();
        /* This logic is used to select the first row in the list of available 
           rows in JTable*/
        if( tblReferenceNumbers!=null && tblReferenceNumbers.getRowCount() > ZERO_COUNT ){
            
            tblReferenceNumbers.setRowSelectionInterval(ZERO_COUNT,ZERO_COUNT);
            
            ReferencesBean firstBean =
                (ReferencesBean)vecReferenceNumbersData.get( ZERO_COUNT );
            
            if(firstBean != null){
                txtAreaComments.setText( firstBean.getComments() );
            }
        }else{
            // If no Data is there then set the delete button to disable mode.
            txtAreaComments.setEnabled(false);
            btnDelete.setEnabled(false);
        }
        // setting bold property for table header values
        tblReferenceNumbers.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        return this;
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTableContainer = new javax.swing.JPanel();
        scrPnPane = new javax.swing.JScrollPane();
        tblReferenceNumbers = new javax.swing.JTable();
        pnlAddDeleteButtons = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblReferenceNoComments = new javax.swing.JLabel();
        pnlComments = new javax.swing.JPanel();
        scrPnCommentsContainer = new javax.swing.JScrollPane();
        txtAreaComments = new javax.swing.JTextArea();
        pnlProposalDescriptionContainer = new javax.swing.JPanel();
        pnlProposalDescription = new javax.swing.JPanel();
        lblProtocolNo = new javax.swing.JLabel();
        lblProtocolValue = new javax.swing.JLabel();
        sptrProposalDescription = new javax.swing.JSeparator();

        setLayout(new java.awt.GridBagLayout());

        pnlTableContainer.setLayout(new java.awt.BorderLayout());

        pnlTableContainer.setMaximumSize(new java.awt.Dimension(660, 260));
        pnlTableContainer.setMinimumSize(new java.awt.Dimension(660, 260));
        pnlTableContainer.setPreferredSize(new java.awt.Dimension(660, 260));
        scrPnPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Reference Numbers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnPane.setMaximumSize(new java.awt.Dimension(640, 250));
        scrPnPane.setMinimumSize(new java.awt.Dimension(640, 250));
        scrPnPane.setPreferredSize(new java.awt.Dimension(640, 250));
        tblReferenceNumbers.setFont(CoeusFontFactory.getNormalFont());
        tblReferenceNumbers.setModel(new CustomTableModel());
        tblReferenceNumbers.setShowHorizontalLines(false);
        scrPnPane.setViewportView(tblReferenceNumbers);

        pnlTableContainer.add(scrPnPane, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 2, 3);
        add(pnlTableContainer, gridBagConstraints);

        pnlAddDeleteButtons.setLayout(new java.awt.GridBagLayout());

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(106, 26));
        btnAdd.setMinimumSize(new java.awt.Dimension(106, 26));
        btnAdd.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        pnlAddDeleteButtons.add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(106, 26));
        btnDelete.setMinimumSize(new java.awt.Dimension(106, 26));
        btnDelete.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        pnlAddDeleteButtons.add(btnDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(13, 0, 0, 0);
        add(pnlAddDeleteButtons, gridBagConstraints);

        lblReferenceNoComments.setFont(CoeusFontFactory.getLabelFont());
        lblReferenceNoComments.setText("Reference Number Comments :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(lblReferenceNoComments, gridBagConstraints);

        pnlComments.setLayout(new java.awt.GridBagLayout());

        pnlComments.setMaximumSize(new java.awt.Dimension(660, 130));
        pnlComments.setMinimumSize(new java.awt.Dimension(660, 130));
        pnlComments.setPreferredSize(new java.awt.Dimension(660, 100));
        scrPnCommentsContainer.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnCommentsContainer.setMaximumSize(new java.awt.Dimension(660, 120));
        scrPnCommentsContainer.setMinimumSize(new java.awt.Dimension(660, 100));
        scrPnCommentsContainer.setPreferredSize(new java.awt.Dimension(660, 90));
        txtAreaComments.setDocument(new LimitedPlainDocument( 2000 ));
        txtAreaComments.setFont(CoeusFontFactory.getNormalFont());
        txtAreaComments.setLineWrap(true);
        txtAreaComments.setWrapStyleWord(true);
        scrPnCommentsContainer.setViewportView(txtAreaComments);

        pnlComments.add(scrPnCommentsContainer, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 10, 3);
        add(pnlComments, gridBagConstraints);

        pnlProposalDescriptionContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlProposalDescription.setLayout(new java.awt.GridBagLayout());

        lblProtocolNo.setFont(CoeusFontFactory.getLabelFont());
        lblProtocolNo.setText("Protocol Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlProposalDescription.add(lblProtocolNo, gridBagConstraints);

        lblProtocolValue.setFont(CoeusFontFactory.getNormalFont());
        lblProtocolValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblProtocolValue.setText("xxxxxxxx");
        lblProtocolValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        pnlProposalDescription.add(lblProtocolValue, gridBagConstraints);

        pnlProposalDescriptionContainer.add(pnlProposalDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(pnlProposalDescriptionContainer, gridBagConstraints);

        sptrProposalDescription.setBackground(java.awt.Color.black);
        sptrProposalDescription.setForeground(java.awt.Color.black);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        add(sptrProposalDescription, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    /** 
     * This method is used to get the Form data.
     *
     * @return Vector of ReferencesBean's
     */
    public Vector getFormData(){
        
        if(vecDeletedReferenceNumbers != null){
            
            int delSize = vecDeletedReferenceNumbers.size();
            ReferencesBean referencesBean = null;
            for(int index = 0; index < delSize; index++){
                referencesBean = (ReferencesBean)vecDeletedReferenceNumbers.get(index);
                if(referencesBean != null && vecReferenceNumbersData !=null){
                    vecReferenceNumbersData.insertElementAt(referencesBean,index);
                }
            }
        }
        //printData();
        return vecReferenceNumbersData;
    }
    
    private void printData(){
        if(vecReferenceNumbersData != null){
            ReferencesBean pBean = null;
            System.out.println("*************************");
            for(int index = 0; index < vecReferenceNumbersData.size(); index++){
                pBean = (ReferencesBean)vecReferenceNumbersData.get(index);
                if(pBean != null){
                    System.out.println("In Detail Form Values to Server");
                    System.out.println("Bean "+index);
                    System.out.println("In Actype "+pBean.getAcType());
                    System.out.println("Ref Code "+pBean.getReferenceTypeCode());
                    System.out.println("Ref Key "+pBean.getReferenceKey());
                    System.out.println("Appl Date "+pBean.getApplicationDate());
                    System.out.println("Appr Key "+pBean.getApprovalDate());
                }
            }
            System.out.println("********************************");
        }
    }
    
    /**
     * This method is used to set the available reference Types
     *
     * @param refTypes collection of available reference Types
     */
    public void setReferenceTypes(Vector refTypes){
        
        //System.out.println("refTypes size is "+refTypes.size());
        this.vecReferenceTypeCode = refTypes;
        if(vecReferenceTypeCode != null){
            vecRefCodeDescriptions = getDescriptionsForCombo(vecReferenceTypeCode);
            //System.out.println("vecRefCodeDescriptions size is "+vecRefCodeDescriptions.size());
        }
        if(vecRefCodeDescriptions != null){
            
            referenceCombo.setModel(new CoeusComboBox(
                                        vecRefCodeDescriptions,
                                        false).getModel()); 
            referenceCombo.addItemListener(new ItemListener(){

                public void itemStateChanged( ItemEvent itemEvent ){

                    int row = tblReferenceNumbers.getEditingRow();
                    int col = tblReferenceNumbers.getEditingColumn();
                    //System.out.println("In itemStateChanged row, column is "+row+""+col);
                    ReferencesBean pbean = null;
                    String prevValue = "";
                    if(vecReferenceNumbersData != null && row != -1){
                        //System.out.println("zzzzzzzzzzzzz");
                        pbean = (ReferencesBean)vecReferenceNumbersData.elementAt(row);
                        if(pbean != null){
                            //System.out.println("xxxxxxxxxxxxxx");
                            int code = pbean.getReferenceTypeCode();
                            prevValue = getDescriptionForId(code, vecReferenceTypeCode);
                            String curValue = ((JComboBox)itemEvent.getSource()).getSelectedItem().toString();
                            if(curValue != null) {
                                //System.out.println("ccccccccccccccccccc");
                                if(!curValue.equalsIgnoreCase(prevValue)){
                                    //System.out.println("vvvvvvvvvvvvvvv");
                                    saveRequired = true;
                                    String stCode = getIDForName(curValue, vecReferenceTypeCode); 
                                    pbean.setReferenceTypeCode(new Integer(stCode).intValue());
                                    pbean.setReferenceTypeDescription(curValue);
                                    String aType = pbean.getAcType();
                                    if (aType != null){
                                        //System.out.println("bbbbbbbbbbbbbbbbbbbb");
                                        if(!aType.equalsIgnoreCase(INSERT_RECORD)) {
                                            pbean.setAcType( UPDATE_RECORD );
                                            saveRequired = true;
                                        }
                                        //System.out.println("nnnnnnnnnn");
                                    }else{
                                        //System.out.println("mmmmmmmmmmmmm");
                                        pbean.setAcType( UPDATE_RECORD );
                                        saveRequired = true;
                                    }
                                    //((DefaultTableModel)tblReferenceNumbers.getModel()).setValueAt(curValue,row,col); // Handle
                                }
                            }
                            //((DefaultTableModel)tblReferenceNumbers.getModel()).setValueAt(prevValue,row,col); //// Handle
                        }
                    }else{
                        //System.out.println("vecReferenceNumbersData is NULL");
                    }
                }
            });
        }
    }
    
    /** 
     * This method is used to set the Cell Editors to the JTable.
     */
    private void setTableEditors(){  
        //COEUSQA-1724-Added for New Expiration Date column-start
        if(moduleCode == 9){
        columnOneWidth = 170;
        columnTwoWidth = 125;
        columnThreeWidth = 110;
        columnFourWidth = 110;
        columnFifthWidth = 110;
        }else{          
        columnOneWidth = 250;
        columnTwoWidth = 120;
        columnThreeWidth = 120;
        columnFourWidth = 120;        
        }
        //COEUSQA-1724-Added for New Expiration Date column-end
        ColumnValueEditor columnValueEditor = new ColumnValueEditor(50);
        
        TableColumn column = tblReferenceNumbers.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column.setResizable(false);
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(30);
        
        JTableHeader header = tblReferenceNumbers.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        
        tblReferenceNumbers.setRowHeight(24);
        
        tblReferenceNumbers.setOpaque(false);
        tblReferenceNumbers.setShowVerticalLines(false);
        tblReferenceNumbers.setShowHorizontalLines(false);
        //tblReferenceNumbers.setSelectionBackground(Color.white);
        //tblReferenceNumbers.setSelectionForeground(Color.black);
        tblReferenceNumbers.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        tblReferenceNumbers.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);   
        
        column = tblReferenceNumbers.getColumnModel().getColumn(1);
        
        column.setMinWidth(columnOneWidth);
        //column.setMaxWidth(250);
        column.setPreferredWidth(columnOneWidth);
        //column.setResizable(false);
        column.setCellEditor(new DefaultCellEditor( referenceCombo ){
            public Object getCellEditorValue(){
                return ((JComboBox)getComponent()).getSelectedItem().toString();
            }
        });

        column = tblReferenceNumbers.getColumnModel().getColumn(2);
        column.setMinWidth(columnTwoWidth);
        //column.setMaxWidth(120);
        column.setPreferredWidth(columnTwoWidth);
        //column.setResizable(false);
        column.setCellEditor(columnValueEditor);

        column = tblReferenceNumbers.getColumnModel().getColumn(3);
        column.setMinWidth(columnThreeWidth);
        //column.setMaxWidth(120);
        column.setPreferredWidth(columnThreeWidth);
        //column.setResizable(false);
        column.setCellEditor(dateEditComponent);
        
        column = tblReferenceNumbers.getColumnModel().getColumn(4);
        column.setMinWidth(columnFourWidth);
        //column.setMaxWidth(120);
        column.setPreferredWidth(columnFourWidth);
        //column.setResizable(false);
        column.setCellEditor(dateEditComponent);
        
        //COEUSQA-1724-Added for New Expiration Date column-start
        if(moduleCode == 9){
        column = tblReferenceNumbers.getColumnModel().getColumn(5);
        column.setMinWidth(columnFifthWidth);         
        column.setPreferredWidth(columnFifthWidth);        
        column.setCellEditor(dateEditComponent);
        }else{
         column = tblReferenceNumbers.getColumnModel().getColumn(5);
         column.setMinWidth(0);         
         column.setPreferredWidth(0);                    
        }
        //COEUSQA-1724-Added for New Expiration Date column-end
    }
    
    /** 
     * This method is used to set the data to the JTable.
     */
    private void setFormData(){
        
        Vector vcDataPopulate = new Vector();
        Vector vcData=null;
        
        if((vecReferenceNumbersData!= null) &&
            (vecReferenceNumbersData.size() > 0)){
                
                ReferencesBean referencesBean = null;
                for(int inCtrdata=0;
                    inCtrdata < vecReferenceNumbersData.size();
                    inCtrdata++){
                    
                    referencesBean = (ReferencesBean)
                            vecReferenceNumbersData.get(inCtrdata);

                    
                    String referenceTypeDesc = referencesBean.getReferenceTypeDescription();
                    String referenceKey = referencesBean.getReferenceKey();
                    Date applicationDate = referencesBean.getApplicationDate();
                    Date approvalDate = referencesBean.getApprovalDate();
                    //COEUSQA-1724-Added for New Expiration Date column-start
                    Date expirationDate = referencesBean.getExpirationDate();
                    //COEUSQA-1724-Added for New Expiration Date column-end
                    
                    String comments = referencesBean.getComments();
                    
                    vcData= new Vector();
                    
                    vcData.addElement("");
                    vcData.addElement(referenceTypeDesc);
                    vcData.addElement(referenceKey);
                    if(applicationDate != null){
                        vcData.addElement(dtUtils.formatDate(applicationDate.toString(),"dd-MMM-yyyy"));
                    }else{
                        vcData.addElement("");
                    }
                    if(approvalDate != null){
                        vcData.addElement(dtUtils.formatDate(approvalDate.toString(),"dd-MMM-yyyy"));
                    }else{
                        vcData.addElement("");
                    }
                    //COEUSQA-1724-Added for New Expiration Date column-start
                    if(moduleCode == 9){
                     if(expirationDate != null){
                        vcData.addElement(dtUtils.formatDate(expirationDate.toString(),"dd-MMM-yyyy"));
                    }else{
                        vcData.addElement("");
                    }
                    }
                    //COEUSQA-1724-Added for New Expiration Date column-end
                    
                    vcDataPopulate.addElement(vcData);
                }
                    ((DefaultTableModel)tblReferenceNumbers.getModel()).
                        setDataVector(vcDataPopulate,getColumnNames());
                    ((DefaultTableModel)tblReferenceNumbers.getModel()).
                        fireTableDataChanged();
                        
                    // If the Table contains more than one row.. move the cursor to the first row..
                    if( tblReferenceNumbers.getRowCount() > 0 ){
                        tblReferenceNumbers.setRowSelectionInterval(0,0);                       
                    }else{
                        btnDelete.setEnabled(false);
                    }
            }
    }
    
    /** 
     * This method disables/ enables the components based on the function type.
     */
    private void formatFields(){
        
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        boolean enabled = functionType != DISPLAY_MODE ? true : false;        
        boolean enabled = (functionType == DISPLAY_MODE || functionType == 'E') ? false : true;
        btnAdd.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
        txtAreaComments.setEnabled(enabled);
        
        //Added by Amit 11/19/2003
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(functionType == CoeusGuiConstants.DISPLAY_MODE){        
        if(functionType == CoeusGuiConstants.DISPLAY_MODE || functionType == 'E'){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblReferenceNumbers.setBackground(bgListColor);    
            tblReferenceNumbers.setSelectionBackground(bgListColor );
            tblReferenceNumbers.setSelectionForeground(Color.black);  
            txtAreaComments.setBackground(bgListColor);    
        }
        else{
            tblReferenceNumbers.setBackground(Color.white);            
            tblReferenceNumbers.setSelectionBackground(Color.white);
            tblReferenceNumbers.setSelectionForeground(Color.black);            
            txtAreaComments.setBackground(Color.white);            
        }
        //end Amit 
    }
    /** 
     * This method is used to set the listeners to the components.
     */
    private void setListeners(){
        
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        txtAreaComments.addFocusListener( this );
        ListSelectionModel referenceSelectionModel = tblReferenceNumbers.getSelectionModel();
        referenceSelectionModel.addListSelectionListener( this );
        referenceSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
    }
    
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
    
    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    /** Getter for property vecReferenceTypeCode.
     * @return Value of property vecReferenceTypeCode.
     */
    public java.util.Vector getVecReferenceTypeCode() {
        return vecReferenceTypeCode;
    }
    
    /** Setter for property vecReferenceTypeCode.
     * @param vecReferenceTypeCode New value of property vecReferenceTypeCode.
     */
    public void setVecReferenceTypeCode(java.util.Vector referenceTypeCode) {
        this.vecReferenceTypeCode = referenceTypeCode;
    }
    
    /** Getter for property vecDeletedReferenceNumbers.
     * @return Value of property vecDeletedReferenceNumbers.
     */
    public java.util.Vector getVecDeletedReferenceNumbers() {
        return vecDeletedReferenceNumbers;
    }
    
    /** Setter for property vecDeletedReferenceNumbers.
     * @param vecDeletedReferenceNumbers New value of property vecDeletedReferenceNumbers.
     */
    public void setVecDeletedReferenceNumbers(java.util.Vector vecDeletedReferenceNumbers) {
        this.vecDeletedReferenceNumbers = vecDeletedReferenceNumbers;
    }
    
    /** Getter for property vecReferenceNumbersData.
     * @return Value of property vecReferenceNumbersData.
     */
    public java.util.Vector getVecReferenceNumbersData() {
        return vecReferenceNumbersData;
    }
    
    /** Setter for property vecReferenceNumbersData.
     * @param vecReferenceNumbersData New value of property vecReferenceNumbersData.
     */
    public void setVecReferenceNumbersData(java.util.Vector referenceNumbersData) {
        this.vecReferenceNumbersData = referenceNumbersData;
    }
    
    /**
     * This method is fired whenever the Button triggers action event
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        
        Object actionSource = actionEvent.getSource();
        if(actionSource.equals(btnAdd)){
            // added by manoj to fix the bug #DEF_14 (ProjetTracking.xls) 19/09/2003
//            java.awt.Component[] components = {tblReferenceNumbers,btnAdd,btnDelete,txtAreaComments};
//            ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
//            setFocusTraversalPolicy(traversePolicy);
//            setFocusCycleRoot(true);  

           try{
               
            if(tblReferenceNumbers.getCellEditor() != null){
                tblReferenceNumbers.getCellEditor().cancelCellEditing();
            }
            Vector newRowData = new Vector();
            TableColumn clmTable = tblReferenceNumbers.getColumnModel().getColumn(1);  
            clmTable.setCellEditor(new DefaultCellEditor(referenceCombo ){             
                public Object getCellEditorValue(){                 
                   return ((JComboBox)getComponent()).getSelectedItem().toString();
                }
            });
            String refDesc = (String)getDescriptionsForCombo(vecReferenceTypeCode).elementAt(0);
            String stRefCode = getIDForName(refDesc, vecReferenceTypeCode);
            newRowData.addElement("");
            newRowData.addElement(refDesc);
            newRowData.addElement("");
            newRowData.addElement("");
            newRowData.addElement("");
            
           int refCode = new Integer(stRefCode).intValue();
            
            // Create a new Bean and Add to Special Review Vector
            
            ReferencesBean pBean = new ReferencesBean();
            
            pBean.setAcType(INSERT_RECORD);
            pBean.setReferenceTypeCode(refCode);
            pBean.setReferenceTypeDescription(refDesc);

            if(vecReferenceNumbersData != null){
                vecReferenceNumbersData.addElement(pBean);
            }else{
                vecReferenceNumbersData = new Vector();
                vecReferenceNumbersData.addElement(pBean);
            }
                
            ((DefaultTableModel)tblReferenceNumbers.getModel()).addRow( newRowData );
            ((DefaultTableModel)tblReferenceNumbers.getModel()).fireTableDataChanged();
            //added by manoj to fix the bug DEF_14 (Project Tracking.xls) 19/09/2003
            btnAdd.transferFocus();
            tblReferenceNumbers.editCellAt(tblReferenceNumbers.getRowCount()-1,1);
            tblReferenceNumbers.getEditorComponent().requestFocusInWindow();
            //ends here
            
            int lastRow = tblReferenceNumbers.getRowCount() - 1;
            if(lastRow >= 0){
                btnDelete.setEnabled(true);
                txtAreaComments.setEnabled(true);
                txtAreaComments.setText("");
                tblReferenceNumbers.setRowSelectionInterval( lastRow, lastRow ); 
                tblReferenceNumbers.scrollRectToVisible(tblReferenceNumbers.getCellRect(
                        lastRow ,ZERO_COUNT, true));
            }
            saveRequired=true;
            lastSelectedRow = lastRow;
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(actionSource.equals(btnDelete)){
            
            int totalRows = tblReferenceNumbers.getRowCount();
            if (totalRows > 0) {
                int selectedRow = tblReferenceNumbers.getSelectedRow();
                if (selectedRow != -1) {
                    int selectedOption = CoeusOptionPane.
                                        showQuestionDialog(
                                        "Are you sure you want to delete this Protocol Reference?",
                                        CoeusOptionPane.OPTION_YES_NO, 
                                        CoeusOptionPane.DEFAULT_YES);
                    if (0 == selectedOption) {
                        ReferencesBean sRefBean = null; 
                        if(vecReferenceNumbersData != null){
                            sRefBean = 
                                (ReferencesBean) vecReferenceNumbersData.get( selectedRow );
                        }
                        if( sRefBean != null ){
                            if(sRefBean.getAcType() != null){
                                //System.out.println(">>>>>>>>>>1");
                                if( ! sRefBean.getAcType().equalsIgnoreCase("I") ) {
                                    //System.out.println(">>>>>>>>>>2");
                                    sRefBean.setAcType( "D" );
                                    vecDeletedReferenceNumbers.addElement( sRefBean );
                                    vecReferenceNumbersData.removeElementAt( selectedRow );
                                }
                                if( sRefBean.getAcType().equalsIgnoreCase("I") ){
                                    vecReferenceNumbersData.removeElementAt( selectedRow );
                                }
                                //System.out.println(">>>>>>>>>>3");
                            }else{
                                //System.out.println(">>>>>>>>>>4");
                                sRefBean.setAcType( "D" );
                                vecDeletedReferenceNumbers.addElement( sRefBean );
                                vecReferenceNumbersData.removeElementAt( selectedRow );
                            }
                            saveRequired = true;
                        }
                        ((DefaultTableModel)
                        tblReferenceNumbers.getModel()).removeRow(selectedRow);
                        
                        ((DefaultTableModel)
                        tblReferenceNumbers.getModel()).fireTableDataChanged();

                        saveRequired = true;

                        int newRowCount = tblReferenceNumbers.getRowCount();
                        if(newRowCount == 0){
                            btnAdd.requestFocusInWindow();
                            btnDelete.setEnabled(false);
                            txtAreaComments.setEnabled(false);
                            txtAreaComments.setText("");
                        }else if (newRowCount > selectedRow) {
                            (tblReferenceNumbers.getSelectionModel())
                                .setSelectionInterval(selectedRow,
                                    selectedRow);
                            ReferencesBean firstBean =
                                (ReferencesBean)vecReferenceNumbersData.get(selectedRow);
                            if(firstBean != null){
                                txtAreaComments.setText( firstBean.getComments() );
                            }
                        } else {
                            tblReferenceNumbers.setRowSelectionInterval( newRowCount - 1,
                                           newRowCount -1 ); 
                            tblReferenceNumbers.scrollRectToVisible( tblReferenceNumbers.getCellRect(
                                            newRowCount - 1 ,
                                            ZERO_COUNT, true));
                            ReferencesBean firstBean =
                                (ReferencesBean)vecReferenceNumbersData.get( newRowCount -1 );
                            if(firstBean != null){
                                txtAreaComments.setText( firstBean.getComments() );
                            }
                        }
                    }

                }else{
                    CoeusOptionPane.
                                showErrorDialog(
                                    coeusMessageResources.parseMessageKey(
                                        "protoFndSrcFrm_exceptionCode.1057"));
                }
            }
        }        
    }
    
    // Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
    
    /** Getter for property vecColumnNames.
     * @return Value of property vecColumnNames.
     */
//    public java.util.Vector getVecColumnNames() {
//        return vecColumnNames;
//    }
    
    
    /** Setter for property vecColumnNames.
     * @param vecColumnNames New value of property vecColumnNames.
     */
   /* public void setVecColumnNames(java.util.Vector vecColumnNames) {

        this.vecColumnNames = vecColumnNames;
        if( vecColumnNames != null ){
            
            strReferenceTypeCode = ( String )vecColumnNames.elementAt(0);
            strReferenceKey = ( String )vecColumnNames.elementAt(1);
            strReferenceApplDate = ( String )vecColumnNames.elementAt(2);
            strReferenceApprDate = ( String )vecColumnNames.elementAt(3);
        }
    }*/
    //End : 02-Sep-2005
    
    // Added to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
    /** Sets table column header titles.
     */
    public void setColumnNames() {        
        strReferenceTypeCode =  coeusMessageResources.parseLabelKey("protocolRefcol1Code.1103");
        strReferenceKey = coeusMessageResources.parseLabelKey("protocolRefcol2Code.1104");
        strReferenceApplDate = coeusMessageResources.parseLabelKey("protocolRefcol3Code.1105");
        strReferenceApprDate = coeusMessageResources.parseLabelKey("protocolRefcol4Code.1106");       
        
        if(moduleCode == 9){
           strReferenceExprnDate = coeusMessageResources.parseLabelKey("protocolRefcol4Code.1107");   
        }else{
            strReferenceExprnDate="";
        }
    }
    //End : 02-Sep-2005
    
    /**
     * Supporting method to show the warning message
     */
    private void showWarningMessage(){
        
        if( functionType != DISPLAY_MODE ) {
            CoeusOptionPane.showWarningDialog(
            coeusMessageResources.parseMessageKey(
            "protocol_SpecialReviewForm_exceptionCode.1053"));
        }
    }
    
    /**
     * This method is fired whenever the Focus is gained by the JTextArea
     */
    public void focusGained(java.awt.event.FocusEvent focusEvent) {
        
        Object source = focusEvent.getSource();
        int selectedRow = tblReferenceNumbers.getSelectedRow();
        if( source.equals( txtAreaComments )){
            if( selectedRow == -1 && tblReferenceNumbers.getRowCount() > 0 ){
                showWarningMessage();
                tblReferenceNumbers.requestFocus();
            }
        }
        prvComments = txtAreaComments.getText();
        if(prvComments == null){
            prvComments = "";
        }
    }
    
    /**
     * This method is used to perform Validations.
     *
     * @return true if the validation succeed, false otherwise.
     * @throws Exception is a exception to be thrown in the client side.
     */
    public boolean validateData() throws Exception{
        
        boolean valid = true;
        
        int rowCount = tblReferenceNumbers.getRowCount();        
        //raghuSV : below condition to eliminate 0>=0 error
        if(tblReferenceNumbers.getRowCount()>0){
            if( tblReferenceNumbers.isEditing()){
                if( tblReferenceNumbers.getCellEditor() != null){
                    //                return tblReferenceNumbers.getCellEditor().stopCellEditing();
                    tblReferenceNumbers.getCellEditor().stopCellEditing();
                }
            }
            //int rowCount = tblReferenceNumbers.getRowCount();
            //if(rowCount >= 0){
            
            for(int inInd=0; inInd < rowCount ;inInd++){
                String referenceKey = (String)((DefaultTableModel)
                tblReferenceNumbers.getModel()).
                getValueAt(inInd,2);
                if((referenceKey == null) || (referenceKey.trim().length() <= 0)){
                    valid=false;
                    CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(
                    "memMntFrm_exceptionCode.1049"));
                    //errorMessage("Please enter the reference number.");
                    tblReferenceNumbers.requestFocusInWindow();
                    tblReferenceNumbers.setRowSelectionInterval(inInd, inInd);
                    tblReferenceNumbers.setColumnSelectionInterval(2,2);
                    
                    break;
                }
            
                    //COEUSQA-1724-Added for date column validation-start             
                    String apDate = (String)tblReferenceNumbers.getValueAt(inInd, 3);
                    String arDate = (String)tblReferenceNumbers.getValueAt(inInd, 4);                    
                    //COEUSQA-1724-Added for date column validation-end
                       if( ( apDate != null && apDate.trim().length() > 0 )
                                 && ( arDate != null  && arDate.trim().length() > 0 ) ) {
                                java.util.Date applDate = dtFormat.parse(
                                dtUtils.restoreDate(apDate,"/:-,"));
                                java.util.Date apprDate = dtFormat.parse(
                                dtUtils.restoreDate(arDate,"/:-,"));
                                if(apprDate.compareTo(applDate)<0){
                                    /* Approval Date is earlier than Application Date */
                                    errorMessage(
                                    coeusMessageResources.parseMessageKey(
                                        "protocol_SpecialReviewForm_exceptionCode.1101"));
                                    valid = false;
                                    break;
                                }
                            }
                        if(moduleCode == 9){
                        String expDate = (String)tblReferenceNumbers.getValueAt(inInd, 5);
                        if( ( arDate != null && arDate.trim().length() > 0 )
                             && ( expDate != null  && expDate.trim().length() > 0 ) ) {
                            java.util.Date apprDate = dtFormat.parse(
                            dtUtils.restoreDate(arDate,"/:-,"));
                            java.util.Date exprnDate = dtFormat.parse(
                            dtUtils.restoreDate(expDate,"/:-,"));
                            if(exprnDate.compareTo(apprDate)<0){
                                /* Expiration Date is earlier than Approval Date */
                                errorMessage(
                                coeusMessageResources.parseMessageKey(
                                    "protocol_SpecialReviewForm_exceptionCode.1102"));
                                valid = false;
                                break;
                            }
                        }
                     }
            }
                    //COEUSQA-1724-Added for date column validation-end
            if(!valid){
                return false;
            }
        }
        return true;
    }
    
    /** This method is used to show the alert messages to the user.
     * @param mesg is a message to alert the user.
     * @throws Exception is the <CODE>Exception</CODE> to throw in the client side.
     */
    
    private void errorMessage(String mesg) throws Exception{
        throw new Exception(mesg);
    }
    
    /**
     * This method is fired whenever the Focus is lost by the JTextArea
     */
    public void focusLost(java.awt.event.FocusEvent focusEvent) {
        
        if ( !focusEvent.isTemporary()) {
            Object source = focusEvent.getSource();
            int selectedRow = lastSelectedRow;
            if( source.equals( txtAreaComments ) &&
            lastSelectedRow <= tblReferenceNumbers.getRowCount()  ){
                ReferencesBean prBean = null;
                if(vecReferenceNumbersData != null){
                    prBean = (ReferencesBean )vecReferenceNumbersData.get(selectedRow);
                }
                if(prBean != null){
                    prBean.setComments( txtAreaComments.getText() );
                    //Changes made check from equals to not equals
                    String acTyp = prBean.getAcType();
                    String curComments = txtAreaComments.getText().trim();
                    if( (acTyp == null) ){
                        if( !curComments.equalsIgnoreCase(prvComments) ) {
                            prBean.setAcType( UPDATE_RECORD );
                            saveRequired = true;
                        }
                    }else{
                        if( (!curComments.equalsIgnoreCase(prvComments))
                        && (!acTyp.equalsIgnoreCase(INSERT_RECORD))) {
                            prBean.setAcType( UPDATE_RECORD );
                            saveRequired = true;
                        }
                    }
                    if(vecReferenceNumbersData != null){
                        vecReferenceNumbersData.setElementAt(prBean,selectedRow);
                    }
                }
            }
        }
    }
    
    /**
     * This method is fired whenever the row selection is changed.
     */
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        //System.out.println("In side Value Changed");
        int selectedRow = tblReferenceNumbers.getSelectedRow();
        String comment = null;
        int rowCount =  tblReferenceNumbers.getRowCount();
        if( selectedRow >= 0  && selectedRow <= rowCount &&
        firstEntry && vecReferenceNumbersData != null){
            //System.out.println("Inside If");
            ReferencesBean curBean = (ReferencesBean)
            vecReferenceNumbersData.get( selectedRow );
            if( txtAreaComments.hasFocus() ){
                curBean = (ReferencesBean)vecReferenceNumbersData.get(  lastSelectedRow );
                curBean.setComments( txtAreaComments.getText() );
                return;
            }else{
                lastSelectedRow = selectedRow;
            }
            
            if( curBean != null ){
                comment = curBean.getComments();
                //System.out.println("comment Bean"+comment);
                if( comment == null){
                    comment = "";
                }
                txtAreaComments.setText( comment );
            }else{
                //System.out.println("comment Bean NULL");
            }
        }
        firstEntry = true ;
    }
    
    /**
     * This method is used to get vector of Reference Type Code Descriptions
     * @return Vector of Reference Type Codes
     */
    
    private Vector getDescriptionsForCombo(Vector vecDescValues) {
        
        ComboBoxBean comboEntry = null;
        Vector vecRefDesc = new Vector();
        
        Vector locRefType = vecDescValues;
        
        if( locRefType == null ){
            return vecRefDesc;
        }else{
            int refCodesSize = locRefType.size();
            for( int indx = 0; indx < refCodesSize; indx++ ){
                comboEntry = ( ComboBoxBean ) locRefType.get( indx );
                if(comboEntry != null){
                    vecRefDesc.addElement( comboEntry.getDescription() );
                }
            }
            return vecRefDesc;
        }
    }
    
    /* This method is used to get available type Code value for the
     * available type description selected in JComboBox
     * @return String description
     */
    private String getIDForName( String selType, Vector vecTypeCodes ){
        
        String stTypeID = "1";
        ComboBoxBean comboEntry = null;
        Vector locRefType = vecTypeCodes;
        if( locRefType == null || selType == null ){
            return stTypeID;
        }
        int refCodesSize = locRefType.size();
        for( int indx = 0; indx < refCodesSize; indx++ ){
            
            comboEntry = ( ComboBoxBean ) locRefType.get( indx );
            if( ((String)comboEntry.getDescription()).equalsIgnoreCase(
            selType)  ){
                stTypeID = comboEntry.getCode();
                break;
            }
        }
        return stTypeID;
    }
    
    /* This method is used to get the description for the corresponding id in corresponding vector
     * @return String description
     */
    
    private String getDescriptionForId(int id, Vector vecTypeCodes){
        
        String stDesc = null;
        String stId = new String(""+id);
        ComboBoxBean comboEntry = null;
        if(vecTypeCodes != null){
            int refCodesSize = vecTypeCodes.size();
            for( int indx = 0; indx < refCodesSize; indx++ ){
                
                comboEntry = ( ComboBoxBean ) vecTypeCodes.get( indx );
                if( ((String)comboEntry.getCode()).equalsIgnoreCase(stId)  ){
                    stDesc = comboEntry.getDescription();
                    break;
                }
            }
        }
        return stDesc;
    }
    
    /**
     * This method is used to get all the column names of the JTable.
     * @return Vector of Column Names
     */
    private Vector getColumnNames(){
        
        Enumeration enumColNames = tblReferenceNumbers.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    /** Getter for property windowType.
     * @return Value of property windowType.
     */
    public char getWindowType() {
        return windowType;
    }
    
    /** Setter for property windowType.
     * @param windowType New value of property windowType.
     */
    public void setWindowType(char windowType) {
        this.windowType = windowType;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JLabel lblProtocolNo;
    private javax.swing.JLabel lblProtocolValue;
    private javax.swing.JLabel lblReferenceNoComments;
    private javax.swing.JPanel pnlAddDeleteButtons;
    private javax.swing.JPanel pnlComments;
    private javax.swing.JPanel pnlProposalDescription;
    private javax.swing.JPanel pnlProposalDescriptionContainer;
    private javax.swing.JPanel pnlTableContainer;
    private javax.swing.JScrollPane scrPnCommentsContainer;
    private javax.swing.JScrollPane scrPnPane;
    private javax.swing.JSeparator sptrProposalDescription;
    private javax.swing.JTable tblReferenceNumbers;
    private javax.swing.JTextArea txtAreaComments;
    // End of variables declaration//GEN-END:variables
    
    /** This method is used to add given the Button Objects into the Pannel
     * @param btnOk OK button to be added to panel
     * @param btnCancel Cancel button to be added to panel
     */    
    public void preInitComponents(javax.swing.JButton btnOk, javax.swing.JButton btnCancel ){
        
        initComponents();
        // added by manoj for focus traversal policy 19/09/2003
        java.awt.Component[] components = {tblReferenceNumbers,btnOk,btnCancel,btnAdd,btnDelete,txtAreaComments};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);  
        if(windowType == 'R'){
            String protocolDisplay = "";
            if(protocolNumber!=null && protocolNumber.length() >=10){
                protocolDisplay = protocolNumber.substring(0,10);
            }
            lblProtocolValue.setText(protocolDisplay);
            java.awt.GridBagConstraints gridBagConstraints;        
            gridBagConstraints = new java.awt.GridBagConstraints();        
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);        
            pnlAddDeleteButtons.add(btnOk,gridBagConstraints);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
            pnlAddDeleteButtons.add(btnCancel,gridBagConstraints);
        }
    }
    
    /** Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     */
    public java.lang.String getProtocolNumber() {
        return protocolNumber;
    }
    
    /** Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     */
    public void setProtocolNumber(java.lang.String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    public void requestDefaultFocusForComponent(){
        if( tblReferenceNumbers.getRowCount() > 0 ) {
            tblReferenceNumbers.requestFocusInWindow();
        }else{
            btnAdd.requestFocusInWindow();
        }
    }
    
    /**
     * This is a Custom table model for the Reference Numbers JTable.
     */
    public class CustomTableModel extends DefaultTableModel{
                            
        /**
         * Constructor
         */
        public CustomTableModel(){
            super(new Object[][]{}, new Object [] 
                {"Icon",strReferenceTypeCode,strReferenceKey,strReferenceApplDate,strReferenceApprDate,strReferenceExprnDate});
        }         
        /** This method checks whether a given Cell is editable or not.
         * @param row Row
         * @param col Column
         * @return  boolean whether cell can be edited or not.
         */        
        public boolean isCellEditable(int row, int col){
            //code modified for coeus4.3 enhancements that UI to be in display mode
            //when new amendment or renewal is created            
//            if((functionType == DISPLAY_MODE) || (col == 0)){            
            if((functionType == DISPLAY_MODE) || (col == 0) || functionType == 'E'){
                return false;
            }else{
                return true;
            }
        }
        
        /** This method is invoked when ever the user changes the 
               contents in the table cell 
         * @param row Row
         * @param column Column 
         */        
        public void fireTableCellUpdated(int row,int column){
            super.fireTableCellUpdated(row,column);
        }
        
                        
        /** This method is used to get the Column Class
         * @param col Column
         * @return Class
         */  
        public Class getColumnClass(int col){
            return Object.class;
        }             
    }
    
        /*
     * Inner class to set the editor for date columns/cells.
     */
    class DateEditor extends AbstractCellEditor implements TableCellEditor {

        private String colName;
        private static final String DATE_SEPARATERS = ":/.,|-";
        private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
        private JTextField dateComponent = new JTextField();
        private String stDateValue;
        private int selectedRow;
        private int selectedColumn;
        boolean temporary;
        DateEditor(String colName) {
            this.colName = colName;
            ((JTextField)dateComponent).setFont(CoeusFontFactory.getNormalFont());
            dateComponent.addFocusListener(new FocusAdapter(){
                public void focusGained(FocusEvent fe){
                    temporary = false;
                }
                public void focusLost(FocusEvent fe){
                    if ( !fe.isTemporary()  ){
                        if(!temporary){
                            stopCellEditing();
                        }
                    }
                }
            });
        }
        
        /**
         * Date validation
         */

        private boolean validateEditorComponent(){

            temporary = true;
            String formattedDate = null;
            String editingValue = (String) getCellEditorValue();
            if (editingValue != null && editingValue.trim().length() > 0) {
                // validate date field
                formattedDate = new DateUtils().formatDate(editingValue,
                    DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                if(formattedDate == null && formattedDate!=REQUIRED_DATEFORMAT) {
                    // invalid date                    
                    //CoeusOptionPane.showErrorDialog("Please enter valid date");
                    CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(
                            "memMntFrm_exceptionCode.1048"));
                    dateComponent.setText(stDateValue);
                    dateComponent.requestFocusInWindow();
                    return false;
                }else{
                    // valid date
                    dateComponent.setText(formattedDate);
                    if(!editingValue.equals(stDateValue)){
                        setModel(formattedDate);
                    }
                }
            }
            if( ((editingValue == null ) || (editingValue.trim().length()== 0 )) && 
                        (stDateValue != null) && (stDateValue.trim().length()>= 0 )){
                saveRequired = true;
                setModel(null);
            }
            return true;
        }
        
        // Sets the editing value to the Bean
        private void setModel(String formatDate){//editingValue
            
            saveRequired=true;
            String appDate = dtUtils.restoreDate(formatDate,"/:-,");
            ReferencesBean pBean = 
                        (ReferencesBean)vecReferenceNumbersData.elementAt(selectedRow);
            String aType = pBean.getAcType();
            try{
                if(selectedColumn == 3){
                    if(appDate != null){//editingValue    Handle
                        pBean.setApplicationDate(new java.sql.Date(dtFormat.parse(appDate).getTime()));
                    }else{
                        pBean.setApplicationDate(null);
                    }
                }else if(selectedColumn == 4){
                    if(appDate != null){
                        pBean.setApprovalDate(new java.sql.Date(dtFormat.parse(appDate).getTime()));
                    }else{
                        pBean.setApprovalDate(null);
                    }
                }
                //COEUSQA-1724-Added for New Expiration Date column-start
                else if(selectedColumn == 5){
                    if(appDate != null){
                        pBean.setExpirationDate(new java.sql.Date(dtFormat.parse(appDate).getTime()));
                    }else{
                        pBean.setExpirationDate(null);
                    }
                }
                //COEUSQA-1724-Added for New Expiration Date column-end
            }catch(Exception e){
                e.printStackTrace();
            }
            if(aType != null){
              if(!aType.equalsIgnoreCase(INSERT_RECORD)){
                     pBean.setAcType(UPDATE_RECORD);
              }
            }else{
                pBean.setAcType(UPDATE_RECORD);
            }
        }
        
        /** This nethod is used to get Editor component of the Cell
         * @param table Table
         * @param value Object
         * @param isSelected boolean 
         * @param row Row 
         * @param column Column
         * @return Component
         */        
        public Component getTableCellEditorComponent(JTable table,Object value,
            boolean isSelected, int row,int column){

            selectedRow = row;
            selectedColumn = column;
            JTextField tfield =(JTextField)dateComponent;
            String currentValue = (String)value;
            String stTempValue =(String)tblReferenceNumbers.getValueAt(row,column);
            if(stTempValue != null){
                stDateValue = dtUtils.restoreDate(stTempValue,DATE_SEPARATERS) ;
            }
            if( ( currentValue != null  ) && (currentValue.trim().length()!= 0) ){
                String newValue = dtUtils.restoreDate(currentValue,
                    DATE_SEPARATERS) ;
                tfield.setText(newValue);
                return dateComponent;
            }

            tfield.setText( ((String)value));
            return dateComponent;
        }

        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return boolean contains true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {
            if( validateEditorComponent()){
                return super.stopCellEditing();
            }
              return  false;
            //return super.stopCellEditing();
        }

        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)dateComponent).getText();
        }

        /**
         * Invoked when an cell has been selected or deselected by the user.
         * The code written for this method performs the operations that need to
         * occur when an cell is selected (or deselected).
         * @param e an ItemEvent.
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
        
        /** Gets Click Count To Start
         * @return  int
         */        
        public int getClickCountToStart(){
            return 1;
        }
    }
    
   /**
    * This is a Default Cell Editor  JTectField Component for the Reference Number.
    */
    class ColumnValueEditor extends DefaultCellEditor
                                                implements TableCellEditor {
        private JTextField txtDesc;
        private int selectedRow ;
        private int selectedCol ;
        private String stReferenceKey;
        // Constructor which sets the size of TextField
        ColumnValueEditor(int len ){
            super(new JTextField());
            txtDesc = new JTextField();
            txtDesc.setFont(CoeusFontFactory.getNormalFont());
            txtDesc.setDocument(new LimitedPlainDocument(len));
            txtDesc.addFocusListener(new FocusAdapter(){
                public void focusLost(FocusEvent fe){
                    
                    if (!fe.isTemporary() ){
                       stopCellEditing();
                       tblReferenceNumbers.setRowSelectionInterval(selectedRow,selectedRow);
                    }
                }
            });
        }
        
        // This method sets the edited value in the bean.
        
        private void setEditorValueToBean(String editingValue){
            
            ReferencesBean pBean = null;
            if( (editingValue == null )){
                ((JTextField)txtDesc).setText( "");
                ((DefaultTableModel)tblReferenceNumbers.getModel()).setValueAt("",selectedRow,selectedCol);
                tblReferenceNumbers.setRowSelectionInterval(selectedRow, selectedRow);                
            }else{
                ((JTextField)txtDesc).setText( editingValue);
                if(!editingValue.equalsIgnoreCase(stReferenceKey)){
                    saveRequired = true;
                    if(vecReferenceNumbersData != null){
                        pBean = (ReferencesBean)vecReferenceNumbersData.elementAt(selectedRow);
                        if(pBean != null){
                            pBean.setReferenceKey(editingValue);
                            String aType = pBean.getAcType();
                            if (aType != null){
                                if(!aType.equalsIgnoreCase(INSERT_RECORD)) {
                                    pBean.setAcType( UPDATE_RECORD );
                                    saveRequired = true;
                                }
                            }else{
                                pBean.setAcType( UPDATE_RECORD );
                                saveRequired = true;
                            }
                            if(vecReferenceNumbersData != null){
                                vecReferenceNumbersData.setElementAt(pBean,selectedRow);
                            }
                        }
                    }else{
                        //System.out.println("vecReferenceNumbersData is NULL");
                    }
                    //((DefaultTableModel)tblReferenceNumbers.getModel()).setValueAt(editingValue,selectedRow,selectedCol);// Handle
                    ((DefaultTableModel)tblReferenceNumbers.getModel()).setValueAt(editingValue,selectedRow,selectedCol);// Handle
                }else{
                    ((DefaultTableModel)tblReferenceNumbers.getModel()).setValueAt(stReferenceKey,selectedRow,selectedCol);// Handle
                }
            }
        }
        
        public Component getTableCellEditorComponent(JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column){
            
            selectedRow = row;
            selectedCol = column;
            stReferenceKey = (String)tblReferenceNumbers.getValueAt(row,3);
            if(stReferenceKey == null){
                stReferenceKey = "";
            }
            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 0 ){
                txtDesc.setText( (String)newValue );
            }else{
                txtDesc.setText("");
            }
            this.selectedRow = row;
            return txtDesc;
        }

        public int getClickCountToStart(){
            return 1;
        }
        
        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)txtDesc).getText();
        }
        
        public boolean stopCellEditing() {
            
            String editingValue = (String)getCellEditorValue();
            setEditorValueToBean(editingValue);
            ReferencesBean pBean = null;
            if( (editingValue == null )){// || (editingValue.trim().length()== 0 )
                ((JTextField)txtDesc).setText( editingValue);
                ((DefaultTableModel)tblReferenceNumbers.getModel()).setValueAt(editingValue,selectedRow,selectedCol); // Handle
                tblReferenceNumbers.setRowSelectionInterval(selectedRow, selectedRow);
                return super.stopCellEditing();
            }else{
                if(!editingValue.equalsIgnoreCase(stReferenceKey)){
                    saveRequired = true;
                    if(vecReferenceNumbersData != null){
                        pBean = (ReferencesBean)vecReferenceNumbersData.elementAt(selectedRow);
                        if(pBean != null){
                            pBean.setReferenceKey(editingValue);
                            String aType = pBean.getAcType();
                            if (aType != null){
                                if(!aType.equalsIgnoreCase(INSERT_RECORD)) {
                                    pBean.setAcType( UPDATE_RECORD );
                                    saveRequired = true;
                                }
                            }else{
                                pBean.setAcType( UPDATE_RECORD );
                                saveRequired = true;
                            }
                            if(vecReferenceNumbersData != null){
                                vecReferenceNumbersData.setElementAt(pBean,selectedRow);
                            }
                        }
                    }
                    //((DefaultTableModel)tblReferenceNumbers.getModel()).setValueAt(editingValue,selectedRow,selectedCol);// Handle
                    ((DefaultTableModel)tblReferenceNumbers.getModel()).setValueAt(editingValue,selectedRow,selectedCol);// Handle
                }else{
                    ((DefaultTableModel)tblReferenceNumbers.getModel()).setValueAt(stReferenceKey,selectedRow,selectedCol);// Handle
                }
            }
            ((JTextField)txtDesc).setText( editingValue);
            if(selectedRow != -1){
                tblReferenceNumbers.setRowSelectionInterval(selectedRow, selectedRow);
            }
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
    }
}