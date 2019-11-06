/*
 * @(#)AlternativeSearchDataBaseForm.java  12/03/2010
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 16-Dec-2010
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.iacuc.gui;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.iacuc.bean.ProtocolAlterDatabaseSearchBean;
import edu.mit.coeus.iacuc.bean.ProtocolAlternativeSearchBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author  mdehtesham
 */
public class AlternativeSearchDataBaseForm extends javax.swing.JPanel implements ActionListener {
    
    /* This is used to hold MDI form reference */
    private CoeusAppletMDIForm mdiReference;
    private CoeusDlgWindow dlgIacucAlternativeSearch;   
    //holds the ProtocolAlternativeSearchBean sent from AlternativeSearchDataBaseForm
    ProtocolAlternativeSearchBean newAlternativeSearchBean;
    CoeusVector cvAlternativeDBSearch;
    private char functionType;
    public boolean saveRequired = false;
    boolean isAltDBChecked = false;
    CoeusVector  vecChkAltDatabase;
    Vector vecAltDBSearchForDIsplay;
    Vector vecSendData;
    Vector unCheckedData;   
    private boolean altSearchPropertyChanged = false;
    private CoeusMessageResources messageResources;
    JCheckBox chkBoxAltDatabase;
    private String oldYearSearched = "";
    private String oldKeyWordSearched = "";
    private String oldComments = "";
    private String oldSearchDate = "";
    private final String DATE_SEPERATOR = "/-:,";
    private final String DATE_FORMAT = "dd-MMM-yyyy";    
    //Exception code   
    private static final String SELECT_ALTSEARCH_DATE = "iacucProtoAlterSearchDateFrm_exceptionCode.1000";
    private static final String SELECT_ALTSEARCH_TYPE = "iacucProtoAlterSearchTypeFrm_exceptionCode.1001"; 
    private static final String SELECT_ALTSEARCH_YEAR =  "iacucProtoAlterSearchYearFrm_exceptionCode.1002";
    private static final String SELECT_ALTSEARCH_KEYWORD = "iacucProtoAlterSearchKeyFrm_exceptionCode.1003";
    private final String WRNG_INVALID_DATE_FORMAT = "memMntFrm_exceptionCode.1048";
    //Date convertion
    private SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy");
    private DateUtils dtUtils = new DateUtils();
    /** Creates new form AlternativeSearchDataBaseForm */
    public AlternativeSearchDataBaseForm() {
        initComponents();
    }
    
    /**
     * Creates new form AlternativeSearchDataBaseForm     
     * 
     * @param mdiReference mdiReference for this form
     * @param newAlternativeSearchBean The new ProtocolExceptionBean
     */
    public AlternativeSearchDataBaseForm(CoeusAppletMDIForm mdiReference, ProtocolAlternativeSearchBean newAlternativeSearchBean,
            CoeusVector cvAlternativeDBSearch, char functionType, Vector vecAltBDSearchForDIsplay){
        this.mdiReference = mdiReference;
        this.newAlternativeSearchBean = newAlternativeSearchBean;
        this.cvAlternativeDBSearch = cvAlternativeDBSearch;
        this.functionType = functionType;  
        this.messageResources = CoeusMessageResources.getInstance();
        this.vecAltDBSearchForDIsplay = vecAltBDSearchForDIsplay;
        vecSendData = new Vector();
        initComponents();
        setDefaultAltDBSearchChkBox();
        setListenersForButtons();
        setFormData();
        showAlternativeSearchForm();                
    }
    
    /** This method uses the cvExceptionsCategory Vector for the protocol
     * cvExceptionsCategory and displays the contents of the vector in the ScientificJustificationExceptionForm
     *
     */
    private void setFormData(){
        unCheckedData = new Vector();
        if (newAlternativeSearchBean != null){                     
            try {                  
                 if(vecChkAltDatabase != null && vecChkAltDatabase.size()> 0 ){
                   for(Object objCheck:vecChkAltDatabase){
                     chkBoxAltDatabase = (javax.swing.JCheckBox)objCheck;                        
                     if(chkBoxAltDatabase.getText() != null){
                       if(vecAltDBSearchForDIsplay != null && vecAltDBSearchForDIsplay.size()>0){
                          ProtocolAlterDatabaseSearchBean protocolAlterDatabaseSearchBean = new ProtocolAlterDatabaseSearchBean();
                            for(Object obj:vecAltDBSearchForDIsplay){
                              protocolAlterDatabaseSearchBean = (ProtocolAlterDatabaseSearchBean)obj;
                              if(chkBoxAltDatabase.getText().equals(protocolAlterDatabaseSearchBean.getDescription())
                                 && !TypeConstants.DELETE_RECORD.equals(protocolAlterDatabaseSearchBean.getAcType())
                                 && protocolAlterDatabaseSearchBean.isSelected()){
                                    chkBoxAltDatabase.setSelected(true);
                               }
                            }                                 
                       }
                    }
                  }
               }
                    if(newAlternativeSearchBean.getSearchDate() != null){
                    String searchDate = dtUtils.formatDate(newAlternativeSearchBean.getSearchDate().toString(),"dd-MMM-yyyy");
                    oldSearchDate = searchDate;
                    txtSearchDate.setText(searchDate);
                    }
                    oldYearSearched = newAlternativeSearchBean.getYearsSearched();
                    oldKeyWordSearched = newAlternativeSearchBean.getKeyWordsSearched();
                    oldComments = newAlternativeSearchBean.getComments();                    
                    txtArYearSearched.setText(newAlternativeSearchBean.getYearsSearched());
                    txtArKeyWordSearch.setText(newAlternativeSearchBean.getKeyWordsSearched());
                    txtArComments.setText(newAlternativeSearchBean.getComments());
                    //Added for GN44 issue #76-Start
                    txtArYearSearched.setCaretPosition(0);
                    txtArKeyWordSearch.setCaretPosition(0);
                    txtArComments.setCaretPosition(0);
                    //Added for GN44 issue #76-End
                                         
                if(vecChkAltDatabase != null && vecChkAltDatabase.size()> 0 ){
                        boolean found = false;
                        for(Object objUnchecked:vecChkAltDatabase){
                            chkBoxAltDatabase = (javax.swing.JCheckBox)objUnchecked;
                             if(!chkBoxAltDatabase.isSelected()){
                                if(vecAltDBSearchForDIsplay != null && vecAltDBSearchForDIsplay.size()>0){
                                      ProtocolAlterDatabaseSearchBean protocolAlterDatabaseSearchBean
                                              = new ProtocolAlterDatabaseSearchBean();
                                      for(Object objAlt:vecAltDBSearchForDIsplay){
                                         protocolAlterDatabaseSearchBean = (ProtocolAlterDatabaseSearchBean)objAlt;
                                         if(chkBoxAltDatabase.getText().equals(protocolAlterDatabaseSearchBean.getDescription())){
                                         found =true;
                                         }
                                      }

                                  }
                                if(!found){
                                unCheckedData.add(chkBoxAltDatabase);
                                }
                             }
                        }
}        
        }catch(Exception e){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
        }
    }
                         
    /**
     * Method to initialise dialog     
     */
    public void showAlternativeSearchForm(){
        String protocolNumber = newAlternativeSearchBean.getProtocolNumber();
        String title = "New Alternative Search for IACUC Protocol";
        if( newAlternativeSearchBean.getAcType() == null || TypeConstants.UPDATE_RECORD.equals(newAlternativeSearchBean.getAcType()) ){
            title = "Alternative Search for IACUC Protocol";
        } 
        if(protocolNumber == null){
        protocolNumber = "";
        }
        dlgIacucAlternativeSearch = new CoeusDlgWindow(mdiReference,
        title + " - "+protocolNumber,true);
        Dimension screenSize = Toolkit.getDefaultToolkit().
        getScreenSize();         
        dlgIacucAlternativeSearch.setResizable(false);
        dlgIacucAlternativeSearch.setSize(600,500);
        dlgIacucAlternativeSearch.getContentPane().add(this);
        Dimension dlgSize = dlgIacucAlternativeSearch.getSize();
        dlgIacucAlternativeSearch.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        screenSize = null;          
        dlgIacucAlternativeSearch.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        dlgIacucAlternativeSearch.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent e) {
                if(e.getComponent()==null){
                    txtSearchDate.requestFocusInWindow();
                }
            }
            public void windowClosing(WindowEvent we){                 
                try{
                    validateData();
                    dlgIacucAlternativeSearch.dispose();
                    
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });
        dlgIacucAlternativeSearch.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                try{
                    if(TypeConstants.DISPLAY_MODE == functionType){
                    dlgIacucAlternativeSearch.dispose();         
                }else{
                    validateData();    
                }                 
                    
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });         
        dlgIacucAlternativeSearch.pack();
        dlgIacucAlternativeSearch.show();
    }
    
    /** This method is used to load the default check box
     * initialize the form.     
     */
    private void setDefaultAltDBSearchChkBox(){
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        pnlAltDBSearch.setLayout(flowLayout);  
        vecChkAltDatabase = new  CoeusVector();         
            if(cvAlternativeDBSearch != null || cvAlternativeDBSearch.size()> 0 ){
            int count = 0;
            for(int index=0;index<cvAlternativeDBSearch.size();index++){
                ComboBoxBean comboBoxBean = (ComboBoxBean) cvAlternativeDBSearch.get(index);
                chkBoxAltDatabase =  new javax.swing.JCheckBox();
                Dimension max = new Dimension(230,20);
                chkBoxAltDatabase.setMaximumSize(max);
                chkBoxAltDatabase.setMinimumSize(max);
                chkBoxAltDatabase.setPreferredSize(max);
                count++;
                if(comboBoxBean != null){
                    chkBoxAltDatabase.setText(comboBoxBean.getDescription());                    
                    pnlAltDBSearch.add(chkBoxAltDatabase);
                    vecChkAltDatabase.add(chkBoxAltDatabase);
                }
            }
            if(count > 6){
                Dimension altDatabaseDimension = new Dimension(500,60+(count/2*25));
                scrPnAltSearchDatabase.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                pnlAltDBSearch.setMaximumSize(altDatabaseDimension);
                pnlAltDBSearch.setMinimumSize(altDatabaseDimension);
                pnlAltDBSearch.setPreferredSize(altDatabaseDimension);
            }
            }      
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlAltSearchColumns = new javax.swing.JPanel();
        lblYearSearched = new javax.swing.JLabel();
        lblKeyWordSearch = new javax.swing.JLabel();
        lblComments = new javax.swing.JLabel();
        scrPnYearSearched = new javax.swing.JScrollPane();
        txtArYearSearched = new javax.swing.JTextArea();
        scrPnKeyWordSearch = new javax.swing.JScrollPane();
        txtArKeyWordSearch = new javax.swing.JTextArea();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        pnlButton = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancle = new javax.swing.JButton();
        pnlaltSearchDate = new javax.swing.JPanel();
        lblSearchDate = new javax.swing.JLabel();
		txtSearchDate = new edu.mit.coeus.utils.CoeusTextField();
        scrPnAltSearchDatabase = new javax.swing.JScrollPane();
        pnlAltDBSearch = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(600, 460));
        setMinimumSize(new java.awt.Dimension(600, 460));
        setPreferredSize(new java.awt.Dimension(600, 460));
        pnlAltSearchColumns.setLayout(new java.awt.GridBagLayout());

        pnlAltSearchColumns.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pnlAltSearchColumns.setMaximumSize(new java.awt.Dimension(500, 270));
        pnlAltSearchColumns.setMinimumSize(new java.awt.Dimension(500, 270));
        pnlAltSearchColumns.setPreferredSize(new java.awt.Dimension(500, 270));
        lblYearSearched.setFont(CoeusFontFactory.getLabelFont());
        lblYearSearched.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblYearSearched.setText("Years Searched:");
        lblYearSearched.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblYearSearched.setMaximumSize(new java.awt.Dimension(120, 20));
        lblYearSearched.setMinimumSize(new java.awt.Dimension(120, 20));
        lblYearSearched.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 60, 0);
        pnlAltSearchColumns.add(lblYearSearched, gridBagConstraints);

        lblKeyWordSearch.setFont(CoeusFontFactory.getLabelFont());
        lblKeyWordSearch.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKeyWordSearch.setText("Keywords Searched:");
        lblKeyWordSearch.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblKeyWordSearch.setMaximumSize(new java.awt.Dimension(120, 20));
        lblKeyWordSearch.setMinimumSize(new java.awt.Dimension(120, 20));
        lblKeyWordSearch.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 60, 0);
        pnlAltSearchColumns.add(lblKeyWordSearch, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblComments.setText("Comments:");
        lblComments.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblComments.setMaximumSize(new java.awt.Dimension(120, 20));
        lblComments.setMinimumSize(new java.awt.Dimension(120, 20));
        lblComments.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 60, 0);
        pnlAltSearchColumns.add(lblComments, gridBagConstraints);

        scrPnYearSearched.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnYearSearched.setHorizontalScrollBar(null);
        scrPnYearSearched.setMaximumSize(new java.awt.Dimension(370, 80));
        scrPnYearSearched.setMinimumSize(new java.awt.Dimension(370, 80));
        scrPnYearSearched.setPreferredSize(new java.awt.Dimension(370, 80));
        txtArYearSearched.setColumns(20);
        txtArYearSearched.setDocument(new LimitedPlainDocument( 2000 ) );
        txtArYearSearched.setFont(CoeusFontFactory.getNormalFont());
        txtArYearSearched.setLineWrap(true);
        txtArYearSearched.setRows(5);
        txtArYearSearched.setWrapStyleWord(true);
        txtArYearSearched.setNextFocusableComponent(txtArKeyWordSearch);
        scrPnYearSearched.setViewportView(txtArYearSearched);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        pnlAltSearchColumns.add(scrPnYearSearched, gridBagConstraints);

        scrPnKeyWordSearch.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnKeyWordSearch.setMaximumSize(new java.awt.Dimension(370, 80));
        scrPnKeyWordSearch.setMinimumSize(new java.awt.Dimension(370, 80));
        scrPnKeyWordSearch.setPreferredSize(new java.awt.Dimension(370, 80));
        txtArKeyWordSearch.setColumns(20);
        txtArKeyWordSearch.setDocument(new LimitedPlainDocument( 2000 ) );
        txtArKeyWordSearch.setFont(CoeusFontFactory.getNormalFont());
        txtArKeyWordSearch.setLineWrap(true);
        txtArKeyWordSearch.setRows(5);
        txtArKeyWordSearch.setWrapStyleWord(true);
        txtArKeyWordSearch.setNextFocusableComponent(txtArComments);
        scrPnKeyWordSearch.setViewportView(txtArKeyWordSearch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        pnlAltSearchColumns.add(scrPnKeyWordSearch, gridBagConstraints);

        scrPnComments.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnComments.setMaximumSize(new java.awt.Dimension(370, 80));
        scrPnComments.setMinimumSize(new java.awt.Dimension(370, 80));
        scrPnComments.setPreferredSize(new java.awt.Dimension(370, 80));
        txtArComments.setColumns(20);
        txtArComments.setDocument(new LimitedPlainDocument( 2000 ) );
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setRows(5);
        txtArComments.setWrapStyleWord(true);
        txtArComments.setNextFocusableComponent(btnOk);
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlAltSearchColumns.add(scrPnComments, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 5, 10);
        add(pnlAltSearchColumns, gridBagConstraints);

        pnlButton.setLayout(new java.awt.GridBagLayout());

        pnlButton.setMaximumSize(new java.awt.Dimension(80, 100));
        pnlButton.setMinimumSize(new java.awt.Dimension(80, 100));
        pnlButton.setPreferredSize(new java.awt.Dimension(80, 100));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.setMargin(new java.awt.Insets(0, 14, 2, 14));
        btnOk.setMaximumSize(new java.awt.Dimension(75, 26));
        btnOk.setMinimumSize(new java.awt.Dimension(75, 26));
        btnOk.setNextFocusableComponent(btnCancle);
        btnOk.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 5);
        pnlButton.add(btnOk, gridBagConstraints);

        btnCancle.setFont(CoeusFontFactory.getLabelFont());
        btnCancle.setText("Cancel");
        btnCancle.setMaximumSize(new java.awt.Dimension(75, 26));
        btnCancle.setMinimumSize(new java.awt.Dimension(75, 26));
        btnCancle.setNextFocusableComponent(txtSearchDate);
        btnCancle.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlButton.add(btnCancle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 50, 0);
        add(pnlButton, gridBagConstraints);

        pnlaltSearchDate.setLayout(new java.awt.GridBagLayout());

        pnlaltSearchDate.setMaximumSize(new java.awt.Dimension(500, 20));
        pnlaltSearchDate.setMinimumSize(new java.awt.Dimension(500, 20));
        pnlaltSearchDate.setPreferredSize(new java.awt.Dimension(500, 20));
        lblSearchDate.setFont(CoeusFontFactory.getLabelFont());
        lblSearchDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSearchDate.setText("Search Date:");
        lblSearchDate.setMaximumSize(new java.awt.Dimension(80, 14));
        lblSearchDate.setMinimumSize(new java.awt.Dimension(80, 14));
        lblSearchDate.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 90, 0, 5);
        pnlaltSearchDate.add(lblSearchDate, gridBagConstraints);

        txtSearchDate.setDocument(new LimitedPlainDocument(11));
        txtSearchDate.setFont(CoeusFontFactory.getNormalFont());
        txtSearchDate.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtSearchDate.setMaximumSize(new java.awt.Dimension(90, 20));
        txtSearchDate.setMinimumSize(new java.awt.Dimension(90, 20));
        txtSearchDate.setNextFocusableComponent(txtArYearSearched);
        txtSearchDate.setPreferredSize(new java.awt.Dimension(90, 20));
        txtSearchDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchDateActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 330);
        pnlaltSearchDate.add(txtSearchDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 10);
        add(pnlaltSearchDate, gridBagConstraints);

        scrPnAltSearchDatabase.setBorder(javax.swing.BorderFactory.createTitledBorder("Database Searched"));
        scrPnAltSearchDatabase.setMaximumSize(new java.awt.Dimension(500, 110));
        scrPnAltSearchDatabase.setMinimumSize(new java.awt.Dimension(500, 110));
        scrPnAltSearchDatabase.setPreferredSize(new java.awt.Dimension(500, 110));
        pnlAltDBSearch.setLayout(new java.awt.GridBagLayout());

        pnlAltDBSearch.setPreferredSize(new java.awt.Dimension(0, 0));
        scrPnAltSearchDatabase.setViewportView(pnlAltDBSearch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 10);
        add(scrPnAltSearchDatabase, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchDateActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_txtSearchDateActionPerformed

    /** Action Performed Method
     * @param actionEvent Action Event Object
     */    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();        
        try{
            if(actionSource.equals(btnOk)){
                setChangedData();
                if(altSearchPropertyChanged){
                setSaveRequired(true);
                }
                dlgIacucAlternativeSearch.dispose();                
            } else if (actionSource.equals(btnCancle)){
                if(TypeConstants.DISPLAY_MODE == functionType){
                    dlgIacucAlternativeSearch.dispose();         
                }else{
                    validateData();    
                }
            } else if (actionSource.equals(chkBoxAltDatabase)){                 
                altSearchPropertyChanged = true;
                setSaveRequired(true);
            } else{                 
                altSearchPropertyChanged = true;
                setSaveRequired(true);
            }
        }catch(Exception e){            
            CoeusOptionPane.showInfoDialog( e.getMessage() );
        }
    }
    
    /**
     * Set Changed Data Method
     * This method sets data from the form to the newAlternativeSearchBean
     * 
     * @param type indicates if OK button is pressed or Cancel button is pressed.
     * @throws Exception Throws exception if data field is empty or null
     */
    public void setChangedData() throws Exception{                                   
        if(TypeConstants.INSERT_RECORD.equals(newAlternativeSearchBean.getAcType())){
            setNewAddedCheckBox( );
        }
        else if(TypeConstants.UPDATE_RECORD.equals(newAlternativeSearchBean.getAcType())){            
                setModifiedCheckBox( );
        }
        validateFormData();
    }
    
     /**
     * validateFormData is used to validate the form data and filter the duplicate bean
     * This method sets data from the form to the newAlternativeSearchBean
     * @throws Exception Throws exception if data field is empty or null
     */
    private void validateFormData() throws ParseException, CoeusUIException {
        HashSet hsFinalData = new HashSet();
        String newSearchDate = txtSearchDate.getText();
        String newYearSearched = txtArYearSearched.getText();
        String newKeyWordSearch = txtArKeyWordSearch.getText();
        if(newSearchDate == null || newSearchDate.length()<=0){
            txtSearchDate.requestFocusInWindow();
            errorMessage(messageResources.parseMessageKey(SELECT_ALTSEARCH_DATE));                         
        }else{
            newAlternativeSearchBean.setSearchDate(new java.sql.Date(dtFormat.parse(dtUtils.restoreDate(newSearchDate, DATE_SEPERATOR)).getTime()));
        }         
        if(vecChkAltDatabase != null && vecChkAltDatabase.size() > 0){
            isAltDBChecked = false;
           StringBuilder dataBaseSearched = new StringBuilder();   
           for(Object chkObj:vecChkAltDatabase){
              chkBoxAltDatabase = (javax.swing.JCheckBox)chkObj;                
              if(chkBoxAltDatabase != null && chkBoxAltDatabase.isSelected()){
                    isAltDBChecked = true;
                    dataBaseSearched.append(chkBoxAltDatabase.getText());                                    
                    dataBaseSearched.append(", ");   
                }               
            } 
           newAlternativeSearchBean.setDatabaseSeartched(dataBaseSearched.toString());
        } 
        if(!isAltDBChecked){
            errorMessage(messageResources.parseMessageKey(SELECT_ALTSEARCH_TYPE));                       
        }else{
            //Block is used to filter the data on iteration of modified button multiple times
            CoeusVector vecOldAlterSearchDataBase = new CoeusVector();
            ProtocolAlterDatabaseSearchBean protocolAlterDatabaseSearchBean = new ProtocolAlterDatabaseSearchBean();
            Vector vecAltData = newAlternativeSearchBean.getChkDatabaseOldSearchedCode();
            if(vecAltData != null && vecAltData.size()>0){
                vecOldAlterSearchDataBase.addAll(vecAltData);
            }
            //Block is used to filter the duplicate record.
            if(vecSendData !=null && vecSendData.size()>0){
                Equals eqCorr;                       
                And andAltSearch;
                for(Object obj: vecSendData){
                    protocolAlterDatabaseSearchBean = (ProtocolAlterDatabaseSearchBean)obj;
                    eqCorr= new Equals("databaseSearchedCode",protocolAlterDatabaseSearchBean.getDatabaseSearchedCode());
                    andAltSearch = new And(eqCorr,CoeusVector.FILTER_ACTIVE_BEANS);
                    CoeusVector filteredResult = vecOldAlterSearchDataBase.filter(andAltSearch);
                    if(filteredResult.size()==0){
                        vecOldAlterSearchDataBase.add(protocolAlterDatabaseSearchBean);
                    }
                }
            }
            //Block is used to filter and set the acType to the bean
            hsFinalData.addAll(vecOldAlterSearchDataBase);
            vecOldAlterSearchDataBase = new CoeusVector();
            vecOldAlterSearchDataBase.addAll(hsFinalData);
            vecSendData = new Vector();            
            if(vecChkAltDatabase != null && vecChkAltDatabase.size()>0){
                if(vecChkAltDatabase != null && vecChkAltDatabase.size() > 0){
                    for(Object chkObj:vecChkAltDatabase){
                        chkBoxAltDatabase = (javax.swing.JCheckBox)chkObj;
                        if(vecOldAlterSearchDataBase !=null && vecOldAlterSearchDataBase.size()>0){
                            ProtocolAlterDatabaseSearchBean altsearchDataBaseBean = new ProtocolAlterDatabaseSearchBean();
                            for(Object obj: vecOldAlterSearchDataBase){
                                altsearchDataBaseBean = (ProtocolAlterDatabaseSearchBean)obj;
                                if(chkBoxAltDatabase != null && chkBoxAltDatabase.isSelected()){                                                                                                                                                                                                                                                                                                                     
                                    if(chkBoxAltDatabase.getText().equals(altsearchDataBaseBean.getDescription())){
                                        altsearchDataBaseBean.setSelected(true);
                                        if(altsearchDataBaseBean.getAcType()== null){
                                            altsearchDataBaseBean.setAcType(null);
                                        }else if(TypeConstants.INSERT_RECORD.equals(altsearchDataBaseBean.getAcType())){
                                            altsearchDataBaseBean.setAcType(TypeConstants.INSERT_RECORD);
                                        }else if(TypeConstants.DELETE_RECORD.equals(altsearchDataBaseBean.getAcType())){
                                            altsearchDataBaseBean.setAcType(null);
                                        }
                                        vecSendData.add(altsearchDataBaseBean);
                                    }
                                }else if(chkBoxAltDatabase != null && !chkBoxAltDatabase.isSelected()){
                                    if(chkBoxAltDatabase.getText().equals(altsearchDataBaseBean.getDescription())){
                                        altsearchDataBaseBean.setSelected(false);
                                        if(altsearchDataBaseBean.getAcType()== null){
                                            altsearchDataBaseBean.setAcType(TypeConstants.DELETE_RECORD);
                                            vecSendData.add(altsearchDataBaseBean);
                                        }else if(TypeConstants.INSERT_RECORD.equals(altsearchDataBaseBean.getAcType())){
                                            altsearchDataBaseBean.setAcType(null);
                                        }else if(TypeConstants.DELETE_RECORD.equals(altsearchDataBaseBean.getAcType())){
                                            altsearchDataBaseBean.setAcType(TypeConstants.DELETE_RECORD);
                                            vecSendData.add(altsearchDataBaseBean);
                                        }
                                        //vecSendData.add(altsearchDataBaseBean);
                                    }
                                }
                            }
                        }
                    }
                }
            }            
            newAlternativeSearchBean.setChkDatabaseSearchedCode(vecSendData);
        }
        if(newYearSearched == null || newYearSearched.trim().length()<=0){
            txtArYearSearched.setText("");
            txtArYearSearched.requestFocusInWindow(); 
            errorMessage(messageResources.parseMessageKey(SELECT_ALTSEARCH_YEAR));               
        }else{
            newAlternativeSearchBean.setYearsSearched(newYearSearched.trim());
        }
        if(newKeyWordSearch == null || newKeyWordSearch.trim().length()<=0){
            txtArKeyWordSearch.setText("");
            txtArKeyWordSearch.requestFocusInWindow();
            errorMessage(messageResources.parseMessageKey(SELECT_ALTSEARCH_KEYWORD));             
        }else{
            newAlternativeSearchBean.setKeyWordsSearched(newKeyWordSearch.trim());
        }
        newAlternativeSearchBean.setComments(txtArComments.getText().trim());
        setProtocolAltSearchBean(newAlternativeSearchBean);
    }

    /**
     * setModifiedCheckBox is used to set the value of modified check box to save
     * This method sets data from the form to the newAlternativeSearchBean
     * @throws Exception Throws NumberFormatException  
     */
    private void setModifiedCheckBox() throws NumberFormatException, CoeusUIException, ParseException {
        String newSearchDate = txtSearchDate.getText();        
        String newYearSearched = txtArYearSearched.getText();
        String newKeyWordSearch = txtArKeyWordSearch.getText();
        if(newSearchDate == null || newSearchDate.length()<=0){
            txtSearchDate.requestFocusInWindow();             
            errorMessage(messageResources.parseMessageKey(SELECT_ALTSEARCH_DATE));           
        }else{
            newAlternativeSearchBean.setSearchDate(new java.sql.Date(dtFormat.parse(dtUtils.restoreDate(newSearchDate, DATE_SEPERATOR)).getTime()));
        }         
        if(vecChkAltDatabase != null && vecChkAltDatabase.size() > 0){
            isAltDBChecked = false;
           for(Object chkObj:vecChkAltDatabase){
              chkBoxAltDatabase = (javax.swing.JCheckBox)chkObj;                
              if(chkBoxAltDatabase != null && chkBoxAltDatabase.isSelected()){
                    isAltDBChecked = true;                      
                }               
            }             
        }if(!isAltDBChecked){
            errorMessage(messageResources.parseMessageKey(SELECT_ALTSEARCH_TYPE)); 
        }
        if(newYearSearched == null || newYearSearched.trim().length()<=0){
            txtArYearSearched.setText("");
            txtArYearSearched.requestFocusInWindow(); 
            errorMessage(messageResources.parseMessageKey(SELECT_ALTSEARCH_YEAR));               
        }else{
            newAlternativeSearchBean.setYearsSearched(newYearSearched.trim());
        }
        if(newKeyWordSearch == null || newKeyWordSearch.trim().length()<=0){
            txtArKeyWordSearch.setText("");
            txtArKeyWordSearch.requestFocusInWindow();
            errorMessage(messageResources.parseMessageKey(SELECT_ALTSEARCH_KEYWORD));             
        }else{
            newAlternativeSearchBean.setKeyWordsSearched(newKeyWordSearch.trim());
        }
        if(!isAltDBChecked){
            errorMessage(messageResources.parseMessageKey(SELECT_ALTSEARCH_TYPE));                          
        }else{
        ProtocolAlterDatabaseSearchBean altsearchDataBaseBean; 
        if(vecChkAltDatabase != null && vecChkAltDatabase.size() > 0){
            for(Object chkObj:vecChkAltDatabase){
                chkBoxAltDatabase = (javax.swing.JCheckBox)chkObj;
                if(vecAltDBSearchForDIsplay !=null && vecAltDBSearchForDIsplay.size()>0){                     
                    for(Object obj: vecAltDBSearchForDIsplay){
                        altsearchDataBaseBean =    (ProtocolAlterDatabaseSearchBean)obj;
                            if(chkBoxAltDatabase != null && chkBoxAltDatabase.isSelected()){                             
                                if(chkBoxAltDatabase.getText().equals(altsearchDataBaseBean.getDescription())){                                      
                                    vecSendData.add(altsearchDataBaseBean);                            
                            }
                        }else if(chkBoxAltDatabase != null && !chkBoxAltDatabase.isSelected()){                            
                                if(chkBoxAltDatabase.getText().equals(altsearchDataBaseBean.getDescription())){
                                    if(altsearchDataBaseBean.getAcType()== null){
                                    altsearchDataBaseBean.setAcType(TypeConstants.DELETE_RECORD);
                                    }
                                    vecSendData.add(altsearchDataBaseBean);     
                                }                       
                        }
                    }
                }
            }
        } 
        if(unCheckedData !=null && unCheckedData.size()>0){
            for(Object obj:unCheckedData){
                chkBoxAltDatabase = (JCheckBox)obj;
                if(chkBoxAltDatabase !=null && chkBoxAltDatabase.isSelected()){
                for(Object objAltsearchdataBase:cvAlternativeDBSearch){
                   ComboBoxBean comboBoxBean = (ComboBoxBean)objAltsearchdataBase;
                if(comboBoxBean.getDescription().equals(chkBoxAltDatabase.getText())){
                   ProtocolAlterDatabaseSearchBean newProtoAltDBSearchBean = new ProtocolAlterDatabaseSearchBean();
                   newProtoAltDBSearchBean.setProtocolNumber(newAlternativeSearchBean.getProtocolNumber());
                   newProtoAltDBSearchBean.setSequenceNumber(newAlternativeSearchBean.getSequenceNumber());
                   newProtoAltDBSearchBean.setDatabaseSearchedCode(Integer.parseInt(comboBoxBean.getCode()));
                   newProtoAltDBSearchBean.setDescription(comboBoxBean.getDescription());
                   newProtoAltDBSearchBean.setAcType(TypeConstants.INSERT_RECORD);
                   vecSendData.add(newProtoAltDBSearchBean);                 
                   }
                }
            }
            }
        }
        }        
    }

    /**
     * setNewAddedCheckBox is used to set the value of new added check box to save
     * This method sets data from the form to the newAlternativeSearchBean
     * @throws Exception Throws NumberFormatException  
     */
    private void setNewAddedCheckBox() throws NumberFormatException {
        if(vecChkAltDatabase != null && vecChkAltDatabase.size() > 0){
            vecSendData = new Vector();
            for(Object chkObj:vecChkAltDatabase){
                chkBoxAltDatabase = (javax.swing.JCheckBox)chkObj;
                if(chkBoxAltDatabase != null && chkBoxAltDatabase.isSelected()){
                    for(Object obj:cvAlternativeDBSearch){
                        ComboBoxBean comboBoxBean = (ComboBoxBean)obj;
                        if(comboBoxBean.getDescription().equals(chkBoxAltDatabase.getText())){
                            ProtocolAlterDatabaseSearchBean protocolAlterDatabaseSearchBean = new ProtocolAlterDatabaseSearchBean();
                            protocolAlterDatabaseSearchBean.setProtocolNumber(newAlternativeSearchBean.getProtocolNumber());
                            protocolAlterDatabaseSearchBean.setSequenceNumber(newAlternativeSearchBean.getSequenceNumber());
                            protocolAlterDatabaseSearchBean.setDatabaseSearchedCode(Integer.parseInt(comboBoxBean.getCode()));
                            protocolAlterDatabaseSearchBean.setDescription(comboBoxBean.getDescription());                           
                            protocolAlterDatabaseSearchBean.setAcType(TypeConstants.INSERT_RECORD);
                            vecSendData.add(protocolAlterDatabaseSearchBean);
                        }
                    }                    
                }
            }
        }        
    }
    
     /**
     * validateData is used to validate the cancel button    
     * @throws Exception Throws Exception  
     */
     private void validateData()throws Exception{
            validateCancle();
        if ( altSearchPropertyChanged ) {
            String msg = messageResources.parseMessageKey(
            "saveConfirmCode.1002");
            
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case ( JOptionPane.NO_OPTION ) :                                                           
                    setSaveRequired( false );
                    if(oldSearchDate !=null && oldSearchDate.trim().length()>0){
                    newAlternativeSearchBean.setSearchDate(new java.sql.Date(dtFormat.parse(dtUtils.restoreDate(oldSearchDate, DATE_SEPERATOR)).getTime()));
                    }
                    newAlternativeSearchBean.setYearsSearched(oldYearSearched);
                    newAlternativeSearchBean.setKeyWordsSearched(oldKeyWordSearched);
                    newAlternativeSearchBean.setComments(oldComments);
                    newAlternativeSearchBean.setDatabaseSeartched(newAlternativeSearchBean.getDatabaseSeartched()+", ");                     
                    dlgIacucAlternativeSearch.dispose();
                    break;
                case ( JOptionPane.YES_OPTION ) :
                    setChangedData();
                    setSaveRequired(true);
                    dlgIacucAlternativeSearch.dispose();
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :                                                              
                    setSaveRequired( false );
                    dlgIacucAlternativeSearch.setVisible( true );
                    break;
            }
            
        }else{ 
            setAltSearchDbdescription(); 
            dlgIacucAlternativeSearch.dispose();
        }
    }
        
   /** Setter for property saveRequired.
    * @param saveRequired New value of property saveRequired.
    */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    /** Getter for property saveRequired.
     * @return saveRequired
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /**
     * validateCancle Method
     * This method used to validate the form while clicking on cancle button
     * and display the save confirmation message if value is modified     
     */
    public void validateCancle() throws Exception{
             
            String newSearchDate = txtSearchDate.getText();
            newAlternativeSearchBean.setYearsSearched(txtArYearSearched.getText().trim());
            newAlternativeSearchBean.setKeyWordsSearched(txtArKeyWordSearch.getText().trim());
            newAlternativeSearchBean.setComments(txtArComments.getText().trim());
            if(newSearchDate !=null && newSearchDate.trim().length()>0){
            newAlternativeSearchBean.setSearchDate(new java.sql.Date(dtFormat.parse(dtUtils.restoreDate(newSearchDate, DATE_SEPERATOR)).getTime()));                     
            }
    }
    
     /**
     * Getter for property newAlternativeSearchBean.
     * @return Value of property newAlternativeSearchBean.
     */
    public ProtocolAlternativeSearchBean getProtocolAltSearchBean() {
        return newAlternativeSearchBean;
    }
    
    /**
     * Setter for property newAlternativeSearchBean.
     * @param newAlternativeSearchBean New value of property newAlternativeSearchBean.
     */
    public void setProtocolAltSearchBean(ProtocolAlternativeSearchBean newAlternativeSearchBean) {
        this.newAlternativeSearchBean = newAlternativeSearchBean;
    }
    
    /** This method is used to set the listeners to the buttons OK and Cancel */
    private void setListenersForButtons(){ 
        txtSearchDate.addFocusListener(new CustomFocusAdapter());
        btnOk.addActionListener(this);
        if(TypeConstants.DISPLAY_MODE != functionType){
        if(vecChkAltDatabase != null && vecChkAltDatabase.size()>0){
            for(Object obj:vecChkAltDatabase){
            chkBoxAltDatabase = (javax.swing.JCheckBox)obj;
            chkBoxAltDatabase.addActionListener(this);
            }
        }}
        String altSearchBeanAcType = newAlternativeSearchBean.getAcType();
        if((altSearchBeanAcType != null)
            &&(TypeConstants.INSERT_RECORD.equals(altSearchBeanAcType) 
            || TypeConstants.UPDATE_RECORD.equals(altSearchBeanAcType))){
            txtSearchDate.setRequestFocusEnabled(true);
        } 
        if(TypeConstants.DISPLAY_MODE== functionType){
            if(vecChkAltDatabase != null && vecChkAltDatabase.size()>0){
            for(Object obj:vecChkAltDatabase){
            chkBoxAltDatabase = (javax.swing.JCheckBox)obj;
            chkBoxAltDatabase.setEnabled(false);
            }
            }
            btnCancle.setFocusable(true);
            btnOk.setEnabled(false);
            txtSearchDate.setEditable(false);
            txtArYearSearched.setEditable(false);
            txtArKeyWordSearch.setEditable(false);
            txtArComments.setEditable(false);            
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");       
            txtArYearSearched.setBackground(bgListColor);  
            txtArKeyWordSearch.setBackground(bgListColor);  
            txtArComments.setBackground(bgListColor);  
            txtSearchDate.setBackground(bgListColor);  
        }
        
        btnCancle.addActionListener(this);        
        //Add PropertyChangeListeners for the newAlternativeSearchBean
        newAlternativeSearchBean.addPropertyChangeListener(
            new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent pce){
                    if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                        altSearchPropertyChanged = true;
                    }
                    if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                        altSearchPropertyChanged = true;
                    }
                    if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                        if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                            altSearchPropertyChanged = true;
                        }
                    }
                }
            });
            
        if(!txtSearchDate.hasFocus()) {
            if(!txtSearchDate.isRequestFocusEnabled()) { txtSearchDate.setRequestFocusEnabled(true); }
            txtSearchDate.requestFocus();
        }
    }
    
   /**
    * Custom focus adapter which is used for text fields which consists of
    * date values. This is mainly used to format and restore the date value
    * during focus gained / focus lost of the text field.
    */
    private class CustomFocusAdapter extends FocusAdapter{
        //hols the data display Text Field
        CoeusTextField dateField;
        String strDate = "";
        String oldData = "";
        boolean temporary = false;

        public void focusGained (FocusEvent fe){
            if (fe.getSource().equals(txtSearchDate)){
                dateField = (CoeusTextField)fe.getSource();
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))) {                   
                    saveRequired = true;
                    oldData = dateField.getText();
                    String focusDate = dtUtils.restoreDate(
                            dateField.getText(),DATE_SEPERATOR);
                    dateField.setText(focusDate);                    
                    //oldSearchDate = oldData;
                }
            }
        }

        public void focusLost (FocusEvent fe){
            if (fe.getSource().equals(txtSearchDate)){
                dateField = (CoeusTextField)fe.getSource();
                temporary = fe.isTemporary();
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))
                        && (!temporary) ) {
                    strDate = dateField.getText();
                    String convertedDate =
                            dtUtils.formatDate(dateField.getText(), DATE_SEPERATOR ,
                                    DATE_FORMAT);                     
                    if(convertedDate == null) {
                        convertedDate = dtUtils.restoreDate(dateField.getText(),"-");
                        if(!dtUtils.validateDate(convertedDate, "/")) {
                            convertedDate =  null;
                        } 
                    }
                    if (convertedDate==null){                        
                         String oldDate = dtUtils.restoreDate(oldData,DATE_SEPERATOR);
                         if(oldDate == null || !oldDate.equals(strDate)){
                             saveRequired = true;
                             dateField.setText(oldData);                             
                             CoeusOptionPane.showErrorDialog(
                                                messageResources.parseMessageKey(
                                                WRNG_INVALID_DATE_FORMAT));
                             temporary = true;
                             dateField.requestFocus(); 
                        }
                 
                    }else {
                        convertedDate = dtUtils.restoreDate(dateField.getText(),"-");
                        convertedDate = dtUtils.formatDate(convertedDate, DATE_SEPERATOR , DATE_FORMAT);
                        dateField.setText(convertedDate);
                        temporary = false;
                        if(!oldData.equals(convertedDate)){                             
                            saveRequired = true;
                        }else{
                            saveRequired = false;
                        }
                    }
                }
            }
        }
    }
    
    /**
    * Method setAltSearchDbdescription is used to set the description of alternative search
    * database description.    
    */
    private void setAltSearchDbdescription() {
        if(vecChkAltDatabase != null && vecChkAltDatabase.size() > 0){
        StringBuilder dataBaseSearched = new StringBuilder();   
        for(Object chkObj:vecChkAltDatabase){
          chkBoxAltDatabase = (javax.swing.JCheckBox)chkObj;                
          if(chkBoxAltDatabase != null && chkBoxAltDatabase.isSelected()){                 
                dataBaseSearched.append(chkBoxAltDatabase.getText());                                    
                dataBaseSearched.append(", ");   
            }               
        } 
           newAlternativeSearchBean.setDatabaseSeartched(dataBaseSearched.toString());
        } 
    }
    
    /* To Throw a coeusexception
     */
    private void errorMessage(String mesg) throws CoeusUIException{
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);         
        throw coeusUIException;
    }
    //End
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblComments;
    private javax.swing.JLabel lblKeyWordSearch;
    private javax.swing.JLabel lblSearchDate;
    private javax.swing.JLabel lblYearSearched;
    private javax.swing.JPanel pnlAltDBSearch;
    private javax.swing.JPanel pnlAltSearchColumns;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlaltSearchDate;
    private javax.swing.JScrollPane scrPnAltSearchDatabase;
    private javax.swing.JScrollPane scrPnComments;
    private javax.swing.JScrollPane scrPnKeyWordSearch;
    private javax.swing.JScrollPane scrPnYearSearched;
    private javax.swing.JTextArea txtArComments;
    private javax.swing.JTextArea txtArKeyWordSearch;
    private javax.swing.JTextArea txtArYearSearched;
	private edu.mit.coeus.utils.CoeusTextField txtSearchDate;
    // End of variables declaration//GEN-END:variables
    
}
