
/*
 * @(#)ScientificJustificationForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 15-NOV-2010
 * by Johncy M John
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.iacuc.bean.ProtocolPrinciplesBean;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import java.util.*;
import java.beans.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.*;

/**
 *
 * @author johncymjohn
 */

public class ScientificJustificationForm extends JComponent {
    
    private CoeusAppletMDIForm mdiForm = null;
    private JPanel pnlPrinciples;
    private char functionType ;
    private boolean saveRequired = false;
    private CoeusMessageResources coeusMessageResources;
    
    String protocolNumber;
    int sequenceNumber;
    String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolServlet";
    Vector vecPrinciples;
    JTextArea txtArPrincOfReduction;
    JTextArea txtArPrincOfRefin;
    JTextArea txtArPrincOfReplac;
    private JScrollPane scrPnCommentsContainer;
   
    private ProtocolPrinciplesBean principles;
    private ProtocolPrinciplesBean newPrinciples;
    
    /**
     * Creates new form ScientificJustificationForm. Default Constructor.
     */
    public ScientificJustificationForm() {
    }

    /**
     * Creates new form ScientificJustificationForm with the parent mdiForm
     * @param mdiParentForm parent form window.
     */
    public ScientificJustificationForm(CoeusAppletMDIForm mdiParentForm ) {
        mdiForm = mdiParentForm;
    }

    /**
     * Creates new ScientificJustificationForm form object with data
     * collections and form mode/type.
     * @param fnType function type/ form mode type.
     */
    public ScientificJustificationForm(String protocolNumber, int sequenceNumber, char fnType ) {
        setProtocolNumber(protocolNumber);
        setSequenceNumber(sequenceNumber);
        this.functionType = fnType;                
    }
    
    public String getProtocolNumber() {
        return protocolNumber;
    }
    
     public int getSequenceNumber() {
        return sequenceNumber;
    }
     
     public void setProtocolNumber(String protocolNumber) {
         this.protocolNumber = protocolNumber;
     }
     
     public void setSequenceNumber(int sequenceNumber) {
         this.sequenceNumber = sequenceNumber;
     }
     
    /**
     * This method is used to initialize the form components and set the form
     * data and set the enabled status for all components depending on the
     * functionType specified while opening the form.
     *
     * @param mdiForm CoeusAppletMDIForm reference to the parent Component
     * @return JComponent reference to the ProtocolMaintenanceForm component
     * after initializing and setting the data
     */
    public JComponent showScientificJustificationsForm(CoeusAppletMDIForm
    mdiForm){
        this.mdiForm = mdiForm;
        initComponents();
        coeusMessageResources = CoeusMessageResources.getInstance();
        loadFormData();
        return this;
    }

    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
    private void formatFields(){

        boolean enabled = (functionType == CoeusGuiConstants.DISPLAY_MODE
                || functionType == CoeusGuiConstants.AMEND_MODE) ? false : true ;
        //Midified for GN44 issue #76-Start
        txtArPrincOfReduction.setEditable(enabled);
        txtArPrincOfRefin.setEditable(enabled);
        txtArPrincOfReplac.setEditable(enabled);
        //Modified for GN44 issue #76-End
        java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        if( !enabled ){
            txtArPrincOfReduction.setBackground(bgListColor);
            txtArPrincOfRefin.setBackground(bgListColor);
            txtArPrincOfReplac.setBackground(bgListColor);
        }else{
            txtArPrincOfReduction.setBackground(Color.white);
            txtArPrincOfRefin.setBackground(Color.white);
            txtArPrincOfReplac.setBackground(Color.white);
        }     
    }
 
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        
        pnlPrinciples = new JPanel();
        txtArPrincOfReduction = new JTextArea();
        setLayout(new java.awt.GridBagLayout());
        setPreferredSize( new java.awt.Dimension( 500, 325 ));  
        pnlPrinciples.setBorder(new TitledBorder(new EtchedBorder(), "Principles", TitledBorder.LEFT,
        TitledBorder.TOP,CoeusFontFactory.getLabelFont()));
        pnlPrinciples.setLayout(new java.awt.GridBagLayout());
        
        JLabel lblPrincipleOfReduction = new JLabel();
        Dimension labelDimension = new Dimension(150,20);
        lblPrincipleOfReduction.setMaximumSize(labelDimension);
        lblPrincipleOfReduction.setMinimumSize(labelDimension);
        lblPrincipleOfReduction.setPreferredSize(labelDimension);
        lblPrincipleOfReduction.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblPrincipleOfReduction.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrincipleOfReduction.setText("Principles of Reduction: ");
        lblPrincipleOfReduction.setFont( CoeusFontFactory.getLabelFont() );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlPrinciples.add(lblPrincipleOfReduction, gridBagConstraints);

        txtArPrincOfReduction.setName("txtArComments");
        txtArPrincOfReduction.setDocument( new LimitedPlainDocument( 2000 ) );
        txtArPrincOfReduction.setWrapStyleWord(true);
        txtArPrincOfReduction.setFont(CoeusFontFactory.getNormalFont());
        txtArPrincOfReduction.setLineWrap(true);
        txtArPrincOfReduction.setRows(2000) ;
        Dimension dimension =  new Dimension(290,80);
        txtArPrincOfReduction.setPreferredSize(dimension);
        txtArPrincOfReduction.setMaximumSize(dimension);
        txtArPrincOfReduction.setMinimumSize(dimension);
        txtArPrincOfReduction.setEditable(true);
        txtArPrincOfReduction.setEnabled(true);

        scrPnCommentsContainer= new javax.swing.JScrollPane();
        scrPnCommentsContainer.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnCommentsContainer.setPreferredSize(new java.awt.Dimension(290,80));
        scrPnCommentsContainer.setMinimumSize(new java.awt.Dimension(290,80));
        scrPnCommentsContainer.setMaximumSize(new java.awt.Dimension(290,80));
        scrPnCommentsContainer.setViewportView(txtArPrincOfReduction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlPrinciples.add(scrPnCommentsContainer, gridBagConstraints);
        
        JLabel lblPrinciplesOfRefinement = new JLabel();
        Dimension labelDimension1 = new Dimension(150,20);
        lblPrinciplesOfRefinement.setMaximumSize(labelDimension1);
        lblPrinciplesOfRefinement.setMinimumSize(labelDimension1);
        lblPrinciplesOfRefinement.setPreferredSize(labelDimension1);
        lblPrinciplesOfRefinement.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblPrinciplesOfRefinement.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrinciplesOfRefinement.setText("Principles of Refinement:");
        lblPrinciplesOfRefinement.setFont( CoeusFontFactory.getLabelFont() );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlPrinciples.add(lblPrinciplesOfRefinement, gridBagConstraints);
        
        txtArPrincOfRefin = new JTextArea();
        
        txtArPrincOfRefin.setName("txtRefinement");
        txtArPrincOfRefin.setDocument( new LimitedPlainDocument( 2000 ) );
        txtArPrincOfRefin.setWrapStyleWord(true);
        txtArPrincOfRefin.setFont(CoeusFontFactory.getNormalFont());
        txtArPrincOfRefin.setRows(2000) ;
        txtArPrincOfRefin.setLineWrap(true);
        Dimension dimension1 = new Dimension(290,80);
        txtArPrincOfRefin.setPreferredSize(dimension1);
        txtArPrincOfRefin.setMaximumSize(dimension1);
        txtArPrincOfRefin.setMinimumSize(dimension1);
        txtArPrincOfRefin.setEditable(true);
        txtArPrincOfRefin.setEnabled(true);
        
        scrPnCommentsContainer= new javax.swing.JScrollPane();
        scrPnCommentsContainer.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnCommentsContainer.setPreferredSize(new java.awt.Dimension(290,80));
        scrPnCommentsContainer.setMinimumSize(new java.awt.Dimension(290,80));
        scrPnCommentsContainer.setMaximumSize(new java.awt.Dimension(290,80));
        scrPnCommentsContainer.setViewportView(txtArPrincOfRefin);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlPrinciples.add(scrPnCommentsContainer, gridBagConstraints);
        
        JLabel lblPrinciplesOfReplacement = new JLabel();
        Dimension labelDimension2 = new Dimension(155,20);
        lblPrinciplesOfReplacement.setMaximumSize(labelDimension2);
        lblPrinciplesOfReplacement.setMinimumSize(labelDimension2);
        lblPrinciplesOfReplacement.setPreferredSize(labelDimension2);
        lblPrinciplesOfReplacement.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblPrinciplesOfReplacement.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrinciplesOfReplacement.setText("Principles of Replacement:");
        lblPrinciplesOfReplacement.setFont( CoeusFontFactory.getLabelFont() );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlPrinciples.add(lblPrinciplesOfReplacement, gridBagConstraints);
        
        txtArPrincOfReplac = new JTextArea();
        txtArPrincOfReplac.setName("txtReplacement");
        txtArPrincOfReplac.setDocument( new LimitedPlainDocument( 2000 ) );
        txtArPrincOfReplac.setWrapStyleWord(true);
        txtArPrincOfReplac.setRows(2000) ;
        txtArPrincOfReplac.setFont(CoeusFontFactory.getNormalFont());
        txtArPrincOfReplac.setLineWrap(true);
        Dimension dimension2=  new Dimension(290,80);
        txtArPrincOfReplac.setPreferredSize(dimension2);
        txtArPrincOfReplac.setMaximumSize(dimension2);
        txtArPrincOfReplac.setMinimumSize(dimension2);
        txtArPrincOfReplac.setEditable(true);
        txtArPrincOfReplac.setEnabled(true);
        
        scrPnCommentsContainer= new javax.swing.JScrollPane();
        scrPnCommentsContainer.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnCommentsContainer.setPreferredSize(new java.awt.Dimension(290,80));
        scrPnCommentsContainer.setMinimumSize(new java.awt.Dimension(290,80));
        scrPnCommentsContainer.setMaximumSize(new java.awt.Dimension(290,80));
        scrPnCommentsContainer.setViewportView(txtArPrincOfReplac);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        //gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlPrinciples.add(scrPnCommentsContainer, gridBagConstraints);        
        
        pnlPrinciples.setMinimumSize(new java.awt.Dimension(300, 100)); 
        pnlPrinciples.setPreferredSize(new java.awt.Dimension(3000, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy =0;
        gridBagConstraints.gridheight = 4;
        
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(pnlPrinciples, gridBagConstraints);         
        
        java.awt.Component[] components = {txtArPrincOfReduction,txtArPrincOfRefin,txtArPrincOfReplac};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
 
    }
 
    /**
     * This method is used to set whether the data is to be saved or not.
     * @param save boolean true if any modifications have been done and are not
     * saved, else  false.
     */
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }
    
    /*
     * Mehthod to validate the form data
     *
     */
    public boolean validateData()throws CoeusUIException{
        return true;
    }
    
//   private void errorMessage(String mesg) throws CoeusUIException{
//        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
//        coeusUIException.setTabIndex(CoeusGuiConstants.IACUC_SCIENTIFIC_JUST_EXCEP_TAB_INDEX);
//        throw coeusUIException;
//    }


    /** This method is used to set the functionType for the form.
     * @param functionType sets the functionType
     */
    public void setFunctionType(char functionType){
        this.functionType = functionType;
        formatFields();
    }

    public void loadFormData(){
        fetchData();
        formatFields();
        fetchPrinciplesData();      
    }
    
    /**
     * This method is used to get the status flag of save operation.
     * @return boolean True is Save required Else False.
     */
     public boolean isSaveRequired(){
         if(functionType == TypeConstants.AMEND_MODE){             
             if(vecPrinciples != null && vecPrinciples.size()>0){
                 return true;
             }
         }
         if(!saveRequired && (vecPrinciples == null || vecPrinciples.size() < 1)){   
             if(txtArPrincOfReduction.getText().trim().length() > 0){
                 saveRequired = true;
             } else if(txtArPrincOfRefin.getText().trim().length() > 0) {
                 saveRequired = true;
             } else if(txtArPrincOfReplac.getText().trim().length() > 0) {
                 saveRequired = true;
             }
         }
        return saveRequired;
    }

    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {       
          txtArPrincOfReduction.requestFocusInWindow();
        }        
    }

    private Vector fetchScientificJustificationData(){
        RequesterBean request = new RequesterBean();
        Vector vecData = new Vector();
        Vector vecProtocolDetail = new Vector();
        vecProtocolDetail.add(0, protocolNumber);
        vecProtocolDetail.add(1, sequenceNumber);
        request.setFunctionType('~');
        request.setDataObjects(vecProtocolDetail);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if(response.isSuccessfulResponse()){
                vecData = response.getDataObjects();
                if(functionType == TypeConstants.AMEND_MODE &&
                        vecData != null &&
                        vecData.size()>0){
                    for(int index=0;index<vecData.size();index++){
                        vecData.get(index);
                    }
                }
            }
        }
        return vecData;
    }

    private void fetchData() {
        Vector vecData = fetchScientificJustificationData();        
        if(vecData != null){
            //Commented for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
            //vecExceptionsData = (Vector) vecData.get(0);
            vecPrinciples = (Vector) vecData.get(0);
           // cvExceptionsCategory = (CoeusVector) vecData.get(2);
            if(functionType == TypeConstants.AMEND_MODE){
                if( vecPrinciples != null &&
                        vecPrinciples.size()>0){
                    for(int index=0;index<vecPrinciples.size();index++){
                        ProtocolPrinciplesBean principleBean = (ProtocolPrinciplesBean)vecPrinciples.get(index);
                        principleBean.setAcType(TypeConstants.INSERT_RECORD);
                                
                    }
                }
            }
        }
    }
    public void setPrinciplesValues() {
        
         if(vecPrinciples!= null && vecPrinciples.size()>0){
           principles = (ProtocolPrinciplesBean) vecPrinciples.elementAt(0);
            if(principles != null){
                principles.setPrincipleOfReduction(txtArPrincOfReduction.getText());
                principles.setPrincipleOfRefinement(txtArPrincOfRefin.getText());
                principles.setPrincipleOfReplacement(txtArPrincOfReplac.getText());      
            }  
        }
         
    }
    
     public Vector getScientJustPrinciplesData(){         
         if(vecPrinciples!= null && vecPrinciples.size()>0){
           principles = (ProtocolPrinciplesBean) vecPrinciples.elementAt(0);
            if(principles != null){
                principles.setProtocolNumber(protocolNumber);
                //principles.setSequenceNumber(Integer.parseInt(vecPrinciples.elementAt(2).toString()));
                principles.setPrincipleOfReduction(txtArPrincOfReduction.getText());
                principles.setPrincipleOfRefinement(txtArPrincOfRefin.getText());
                principles.setPrincipleOfReplacement(txtArPrincOfReplac.getText());
                if(functionType == TypeConstants.AMEND_MODE){
                    principles.setAcType(TypeConstants.INSERT_RECORD); 
                }else {
                    // Added for ISSUEIS#1786 - Error when All data entered and save-Premium - start
                    if (principles.getUpdateTimestamp() !=  null){
                         principles.setAcType(TypeConstants.UPDATE_RECORD);
                    } else {
                        principles.setAcType(TypeConstants.INSERT_RECORD);                   
                     }
                    // Added for ISSUEIS#1786 - Error when All data entered and save-Premium - end
                }  
            }  
        } else {
             //Modified for COEUSQA-3035 Indicator logic
                if(functionType != TypeConstants.AMEND_MODE){
                newPrinciples = new ProtocolPrinciplesBean();
                newPrinciples.setProtocolNumber(protocolNumber);
                newPrinciples.setSequenceNumber(sequenceNumber);
                newPrinciples.setPrincipleOfReduction(txtArPrincOfReduction.getText());
                newPrinciples.setPrincipleOfRefinement(txtArPrincOfRefin.getText());
                newPrinciples.setPrincipleOfReplacement(txtArPrincOfReplac.getText());
                newPrinciples.setAcType(TypeConstants.INSERT_RECORD);
                vecPrinciples.add(newPrinciples);
                }

         }
         return vecPrinciples;
     }
     
     //Commented for COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
//     public Vector getScientJustExceptionsData(){
//         Vector vecAllExceptions = new Vector();
//         vecAllExceptions.addAll(vecDeletedExceptions);
//         vecAllExceptions.addAll(vecExceptionsData);
//         return vecAllExceptions;
//     }
     
    private void fetchPrinciplesData() {
        if(vecPrinciples!= null && vecPrinciples.size()>0){
            principles = (ProtocolPrinciplesBean) vecPrinciples.elementAt(0);
            if(principles != null){
                txtArPrincOfReduction.setText(principles.getPrincipleOfReduction());
                txtArPrincOfRefin.setText(principles.getPrincipleOfRefinement());
                txtArPrincOfReplac.setText(principles.getPrincipleOfReplacement());
                //Added for COEUSQA-2998-IACUC - Usability issue on the Scientific Justification screen
                txtArPrincOfReduction.setCaretPosition(0);
                txtArPrincOfRefin.setCaretPosition(0);
                txtArPrincOfReplac.setCaretPosition(0);
            }  
            if(functionType != TypeConstants.AMEND_MODE){
                
                principles.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent pce) {
                        //Modified If conditions to remove unnecessary save confirmations.
                        if( !( pce.getOldValue() == null && (pce.getNewValue() == null || "".equals(pce.getNewValue().toString())))){
                            principles.setAcType(TypeConstants.UPDATE_RECORD);
                            setSaveRequired(true);
                        }
//                        if (pce.getNewValue() == null && pce.getOldValue() != null) {
//                            principles.setAcType(TypeConstants.UPDATE_RECORD);
//                            saveRequired = true;
//                        }
//                        if(pce.getNewValue() != null && pce.getOldValue() == null) {
//                            principles.setAcType(TypeConstants.UPDATE_RECORD);
//                            saveRequired = true;
//                        }
                        if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                            if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                                principles.setAcType(TypeConstants.UPDATE_RECORD);
                                saveRequired = true;
                            }
                        }
                    }
                });
            }
        }
        //Added for COEUSQA-3035 Indicator logic-start
        else{
            txtArPrincOfReduction.setText("");
            txtArPrincOfRefin.setText("");
            txtArPrincOfReplac.setText("");            
        }
        //Added for COEUSQA-3035 Indicator logic-end
    }     
 }

