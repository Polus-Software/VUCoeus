/*
 * @(#)ScientificJustificationExceptionForm.java  8/20/2010
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 24-AUGUST-2010
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.iacuc.bean.ProtocolExceptionBean;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.TypeConstants;

import java.awt.event.*;
import java.awt.*;
import java.awt.Container.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JInternalFrame.*;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;

/** This Form is used to Add New Exception records for a IACUC Protocol.
 * @author Md.Ehtesham Ansari
 */
public class ScientificJustificationExceptionForm extends javax.swing.JComponent implements ActionListener{

    /* This is used to hold MDI form reference */
    private CoeusAppletMDIForm mdiReference;
    private CoeusDlgWindow dlgIacucException;
    private CoeusMessageResources messageResources;
            Component parentDialog;
    //holds the ProtocolExceptionBean sent from ScientificJustificationExceptionForm
            ProtocolExceptionBean newExceptionBean;
            CoeusVector cvExceptionsCategory;
    //To indicate if the newProtocolExceptionBean has been changed
    private boolean exceptionPropertyChanged = false;
    //To indicate if the newProtocolExceptionBean has to be saved to DB thru ScientificJustificationExceptionForm
    private boolean saveRequired = false;
    private char functionType;
        
    /**
     * This variable is used to store the old Restricted Flag value while in 
     * setChangedData(), These values will be reset to the bean in the case 
     * of a NO or CANCEL option  in YES_NO_CANCEL Dialog box.
     */
    private String oldDescription = "" ;
    int cvExceptionCount = 0;
    
    /** Creates new form ScientificJustificationExceptionForm */
    public ScientificJustificationExceptionForm() {
        initComponents();
        
    }
    
    /**
     * Creates new form ScientificJustificationExceptionForm     
     * @param mdiReference mdiReference for this form
     * @param newExceptionBean The new ProtocolExceptionBean
     */
    public ScientificJustificationExceptionForm(CoeusAppletMDIForm mdiReference, ProtocolExceptionBean newExceptionBean,CoeusVector cvExceptionsCategory,char functionType) {
        this.mdiReference = mdiReference;
        this.newExceptionBean = newExceptionBean;
        this.cvExceptionsCategory = cvExceptionsCategory;
        this.functionType = functionType;
        initComponents();
        messageResources = CoeusMessageResources.getInstance();
        setListenersForButtons();
        setFormData();
    }

    /** This method is used to set the listeners to the buttons OK and Cancel */
    private void setListenersForButtons(){
        
        //Setting font for labels
        lblBy.setFont(CoeusFontFactory.getLabelFont());         
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
                 
        btnOk.addActionListener(this);
        String exceptionBeanAcType = newExceptionBean.getAcType();
        if((exceptionBeanAcType != null)
            &&(TypeConstants.INSERT_RECORD.equals(exceptionBeanAcType) 
            || TypeConstants.UPDATE_RECORD.equals(exceptionBeanAcType))){
            txtArComments.setRequestFocusEnabled(true);
        } 
        if(TypeConstants.DISPLAY_MODE== functionType){
            btnOk.setEnabled(false);
            txtArComments.setEditable(false);
            exceptionComboBox.setEnabled(false);
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");       
            txtArComments.setBackground(bgListColor);                                
        }
        
        btnCancel.addActionListener(this);        
        //Add PropertyChangeListeners for the newExceptionBean
        newExceptionBean.addPropertyChangeListener(
            new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent pce){
                    if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                        exceptionPropertyChanged = true;
                    }
                    if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                        exceptionPropertyChanged = true;
                    }
                    if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                        if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                            exceptionPropertyChanged = true;
                        }
                    }
                }
            });
            
        if(!txtArComments.hasFocus()) {
            if(!txtArComments.isRequestFocusEnabled()) { txtArComments.setRequestFocusEnabled(true); }
            txtArComments.requestFocus();
        }
    }
    
    /** Action Performed Method
     * @param actionEvent Action Event Object
     */    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();
        try{
            if(actionSource.equals(btnOk)){
                setChangedData('O');
                dlgIacucException.dispose();                
            } else if (actionSource.equals(btnCancel)){
                validateData();                  
            }  
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog( e.getMessage() );
        }
    }
    /**
     * Set Changed Data Method
     * This method sets data from the form to the newExceptionBean
     * 
     * @throws Exception Throws Exception if Comments String is empty or null
     */
    public void setChangedData() throws Exception{
        setChangedData('Z');
    }
    /**
     * Set Changed Data Method
     * This method sets data from the form to the newExceptionBean
     * 
     * @param type indicates if OK button is pressed or Cancel button is pressed.
     * @throws Exception Throws exception if comments field is empty or null
     */
    public void setChangedData(char type) throws Exception{        
        String newComment = txtArComments.getText();
        if((newComment != null)&&(newComment.trim().length() > 0)){
            setSaveRequired(true);  
            ComboBoxBean exceptionsComboBoxBean = (ComboBoxBean)cvExceptionsCategory.get(exceptionComboBox.getSelectedIndex());
            newExceptionBean.setExceptionCategoryCode(Integer.parseInt(exceptionsComboBoxBean.getCode()));
            newExceptionBean.setExceptionCategoryDesc(exceptionsComboBoxBean.getDescription());            
            oldDescription = newExceptionBean.getExceptionDescription();            
            newExceptionBean.setExceptionDescription(newComment.trim());
        } else if(type=='O'){
            newExceptionBean.setExceptionDescription(oldDescription); 
            throw new Exception(messageResources.parseMessageKey(
            "iacucPrtoScientJustFrm_exceptionCode.1002" ));
            
        }
    }
    
    /** This method uses the cvExceptionsCategory Vector for the protocol
     * cvExceptionsCategory and displays the contents of the vector in the ScientificJustificationExceptionForm
     *
     */
    private void setFormData(){
        if (newExceptionBean != null){                     
            try {                     
                exceptionComboBox.setModel(new DefaultComboBoxModel(cvExceptionsCategory));
                if(newExceptionBean.getExceptionCategoryCode()==0){
                    exceptionComboBox.setSelectedIndex(0);
                }else{
                    for(cvExceptionCount = 0;cvExceptionCount<cvExceptionsCategory.size();cvExceptionCount++){
                        if(cvExceptionsCategory.elementAt(cvExceptionCount).toString().equals(newExceptionBean.getExceptionCategoryDesc())){
                            break;
                        }
                    }
                    exceptionComboBox.setSelectedIndex(cvExceptionCount); 
                }                
                txtArComments.setText(newExceptionBean.getExceptionDescription());
                oldDescription = newExceptionBean.getExceptionDescription();                 
            }catch(Exception e){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
        }
    }
    
    /** This method is used by the ScientificJustificationExceptionForm to Display
     * the Dialog which will contain the ScientificJustificationExceptionForm Form
     */
    public void showProtocolExceptionForm(){
        String title = "New Exception for IACUC Protocol - ";
        if( newExceptionBean.getAcType() == null || TypeConstants.UPDATE_RECORD.equals(newExceptionBean.getAcType()) ){
            title = "Exception for IACUC Protocol - ";
        }
        dlgIacucException = new CoeusDlgWindow(mdiReference,
        title + newExceptionBean.getProtocolNumber(),true);
        
        dlgIacucException.getContentPane().add(this);
        dlgIacucException.setResizable(false);
        dlgIacucException.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().
        getScreenSize();
        Dimension dlgSize = dlgIacucException.getSize();
        dlgIacucException.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgIacucException.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        dlgIacucException.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent e) {
                txtArComments.requestFocusInWindow();
            }
            public void windowClosing(WindowEvent we){                 
                try{
                    validateData();
                    
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });
        dlgIacucException.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                try{
                    validateData();
                    
                }catch(Exception ex){
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        });
        dlgIacucException.show();
    }
    
    private void validateData()throws Exception{
        validateCancle();
        if ( exceptionPropertyChanged ) {
            String msg = messageResources.parseMessageKey(
            "saveConfirmCode.1002");
            
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case ( JOptionPane.NO_OPTION ) :                                      
                    newExceptionBean.setExceptionDescription(oldDescription);
                    //oldDescription = "";
                    ComboBoxBean exceptionsComboBoxBean = (ComboBoxBean)cvExceptionsCategory.get(cvExceptionCount);
                    newExceptionBean.setExceptionCategoryCode(Integer.parseInt(exceptionsComboBoxBean.getCode()));
                    newExceptionBean.setExceptionCategoryDesc(exceptionsComboBoxBean.getDescription()); 
                    setSaveRequired( false );
                    dlgIacucException.dispose();
                    break;
                case ( JOptionPane.YES_OPTION ) :
                    setChangedData('O');
                    dlgIacucException.dispose();
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :                                       
                    newExceptionBean.setExceptionDescription(oldDescription);                    
                    setSaveRequired( false );
                    dlgIacucException.setVisible( true );
                    break;
            }
            
        }else{             
            dlgIacucException.dispose();
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

        pnlTextArea = new javax.swing.JPanel();
        scrPnArComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblBy = new javax.swing.JLabel();
        exceptionComboBox = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(550, 275));
        setMinimumSize(new java.awt.Dimension(550, 275));
        setPreferredSize(new java.awt.Dimension(550, 275));
        pnlTextArea.setLayout(new java.awt.GridBagLayout());

        pnlTextArea.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Description", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, CoeusFontFactory.getLabelFont()));
        pnlTextArea.setMaximumSize(new java.awt.Dimension(445, 250));
        pnlTextArea.setMinimumSize(new java.awt.Dimension(445, 250));
        pnlTextArea.setPreferredSize(new java.awt.Dimension(445, 250));
        scrPnArComments.setMaximumSize(new java.awt.Dimension(430, 220));
        scrPnArComments.setMinimumSize(new java.awt.Dimension(430, 220));
        scrPnArComments.setPreferredSize(new java.awt.Dimension(430, 220));
        txtArComments.setDocument(txtArComments.getDocument());
        LimitedPlainDocument plainDocument = new LimitedPlainDocument(1000);
        txtArComments.setDocument(plainDocument);
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        txtArComments.setMaximumSize(new java.awt.Dimension(425, 214));
        txtArComments.setMinimumSize(new java.awt.Dimension(425, 214));
        txtArComments.setNextFocusableComponent(btnOk);
        txtArComments.setPreferredSize(new java.awt.Dimension(425, 214));
        scrPnArComments.setViewportView(txtArComments);

        pnlTextArea.add(scrPnArComments, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(pnlTextArea, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        pnlButtons.setMaximumSize(new java.awt.Dimension(80, 100));
        pnlButtons.setMinimumSize(new java.awt.Dimension(80, 100));
        pnlButtons.setPreferredSize(new java.awt.Dimension(80, 100));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(75, 26));
        btnOk.setMinimumSize(new java.awt.Dimension(75, 26));
        btnOk.setNextFocusableComponent(btnCancel);
        btnOk.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 7, 5);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(75, 26));
        btnCancel.setMinimumSize(new java.awt.Dimension(75, 26));
        btnCancel.setNextFocusableComponent(exceptionComboBox);
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlButtons.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(pnlButtons, gridBagConstraints);

        lblBy.setText("Category : ");
        lblBy.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblBy.setAlignmentX(100.0F);
        lblBy.setAlignmentY(100.0F);
        lblBy.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        lblBy.setMaximumSize(new java.awt.Dimension(75, 20));
        lblBy.setMinimumSize(new java.awt.Dimension(75, 20));
        lblBy.setPreferredSize(new java.awt.Dimension(60, 20));
        lblBy.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(7, 14, 5, 0);
        add(lblBy, gridBagConstraints);

        exceptionComboBox.setActionCommand("");
        exceptionComboBox.setAlignmentX(0.0F);
        exceptionComboBox.setAlignmentY(0.0F);
        exceptionComboBox.setMaximumSize(new java.awt.Dimension(50, 20));
        exceptionComboBox.setMinimumSize(new java.awt.Dimension(50, 20));
        exceptionComboBox.setNextFocusableComponent(txtArComments);
        exceptionComboBox.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 200);
        add(exceptionComboBox, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
         
    /**
     * Getter for property newExceptionBean.
     * 
     * @return Value of property newExceptionBean.
     */
    public ProtocolExceptionBean getProtocolExceptionBean() {
        return newExceptionBean;
    }
    
    /**
     * Setter for property newExceptionBean.
     * 
     * @param newExceptionBean New value of property newExceptionBean.
     */
    public void setProtocolExceptionBean(ProtocolExceptionBean newExceptionBean) {
        this.newExceptionBean = newExceptionBean;
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
       
    /**
     * validateCancle Method
     * This method used to validate the form while clicking on cancle button
     * and display the save confirmation message if value is modified     
     */
    public void validateCancle() throws Exception{                             
            ComboBoxBean exceptionsComboBoxBean = (ComboBoxBean)cvExceptionsCategory.get(exceptionComboBox.getSelectedIndex());
            newExceptionBean.setExceptionCategoryCode(Integer.parseInt(exceptionsComboBoxBean.getCode()));
            newExceptionBean.setExceptionCategoryDesc(exceptionsComboBoxBean.getDescription());            
            oldDescription = newExceptionBean.getExceptionDescription();            
            newExceptionBean.setExceptionDescription(txtArComments.getText().trim());      
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox exceptionComboBox;
    private javax.swing.JLabel lblBy;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlTextArea;
    private javax.swing.JScrollPane scrPnArComments;
    private javax.swing.JTextArea txtArComments;
    // End of variables declaration//GEN-END:variables
    
}
