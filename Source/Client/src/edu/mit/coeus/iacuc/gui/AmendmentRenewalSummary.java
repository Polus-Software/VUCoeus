/*
 * AmendmentRenewalSummary.java
 *
 * Created on August 25, 2003, 10:30 AM
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.iacuc.bean.IrbWindowConstants;
import edu.mit.coeus.iacuc.bean.ProtocolAmendRenewalBean;
import edu.mit.coeus.iacuc.bean.ProtocolModuleBean;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;


import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author  ravikanth
 */
public class AmendmentRenewalSummary extends javax.swing.JComponent implements TypeConstants {
    
    private Vector amendRenewalList;
    private char functionType;
    private ProtocolAmendRenewalBean amendRenewalBean;
    private boolean saveRequired;
    private CoeusMessageResources messageResources;
    //Added for coeus4.3 enhancements - starts
    //For Editable modules
    private HashMap hmEditableModules;
    private Vector vecEditedModuleData;
    private HashMap hmModuleData;
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    //for editing original protocol questionnaires
    private HashMap hmEditableQuestionnaires;
    private Vector vecEditedQnrData;
    private HashMap hmQuestionnaireData;
    private HashMap qnrCheckBoxes = new HashMap();
    private Vector vecCurrentEditableModules;
    private Vector vecCurrentEditableQuestionnaires;
    // CoeusQA2313: Completion of Questionnaire for Submission - End
    //Added for coeus4.3 enhancements - ends
    /** Creates new form AmendmentRenewalSummary */
    public AmendmentRenewalSummary(Vector amendRenewalData, char functionType, Vector editableModules, HashMap hmApplicableQuestionnaire) {
        this.amendRenewalList = amendRenewalData;
        this.functionType = functionType;
        //case 4277:Now that there is New Amendment/Renewal, do not allow changes in an Renewal.
        if(editableModules!=null && editableModules.size()>0){
            this.hmEditableModules = (HashMap) editableModules.get(0);
        }
        this.hmEditableModules = (this.hmEditableModules!=null)? this.hmEditableModules : new HashMap();
        this.hmEditableQuestionnaires = hmApplicableQuestionnaire;
        //4277 End
        initComponents();
        //Code for focus traversal - start
        //Added for coeus4.3 enhancements
        //Focus traversal added for module checkboxes.
        java.awt.Component[] components = {txtArSummary, chkGeneralInfo, chkProtocolOrganizations, 
                        chkProtocolInvestigators, chkKeyStudyPersonnel, chkProtocolCorrespondents, 
                        chkAreasofResearch, chkFundingSource ,chkSpecialReview,
                        chkProtocolReferences, chkUploadDocuments, chkOthers,chkSpecies,
                        chkAlternativeSearch,chkScientificJustification};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        //Code for focus traversal - end        
        txtArSummary.setDisabledTextColor(java.awt.Color.black);
         //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){

            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
           txtArSummary.setBackground(bgListColor);
        }
        else{
            txtArSummary.setBackground(java.awt.Color.white);  
        }
        //end Amit 
        messageResources = CoeusMessageResources.getInstance();
        setValues ( amendRenewalList );
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
            chkGeneralInfo.setEnabled(false);
            chkProtocolOrganizations.setEnabled(false);
            chkProtocolInvestigators.setEnabled(false);
            chkKeyStudyPersonnel.setEnabled(false);
            chkProtocolCorrespondents.setEnabled(false);
            chkAreasofResearch.setEnabled(false);             
            chkSpecialReview.setEnabled(false);
            chkProtocolReferences.setEnabled(false);
            chkUploadDocuments.setEnabled(false);
            chkOthers.setEnabled(false);            
            chkFundingSource.setEnabled(false);
            chkSpecies.setEnabled(false);
            chkAlternativeSearch.setEnabled(false);             
//            chkStudyGroup.setEnabled(false);
            chkScientificJustification.setEnabled(false);
            
        }        
    }
    
    public void setValues( Vector amendRenewalData ) {
        this.amendRenewalList = amendRenewalData;
        if( amendRenewalList != null && amendRenewalList.size() > 0 ) {
          amendRenewalBean = ( ProtocolAmendRenewalBean ) amendRenewalList.elementAt( 0 );
          txtArSummary.setText( amendRenewalBean.getSummary() );
        }else{
            txtArSummary.setText(null);
        }
        if( functionType != DISPLAY_MODE ) {
            txtArSummary.setEnabled( true );
        }else{
            txtArSummary.setEnabled( false );
        }
        txtArSummary.setCaretPosition( 0 );
        //code added for coeus4.3 enhancement - starts
        String amendRenewalnumber = "";
        if(amendRenewalBean!=null){
            vecCurrentEditableModules = amendRenewalBean.getVecModuleData();
            this.hmModuleData = getFormatedModuleData(vecCurrentEditableModules);
            // Added with CoeusQA2313: Completion of Questionnaire for Submission
            vecCurrentEditableQuestionnaires = amendRenewalBean.getVecSelectedOrigProtoQnr();
            this. hmQuestionnaireData = getFormattedQnrData(vecCurrentEditableQuestionnaires);
            // CoeusQA2313: Completion of Questionnaire for Submission - end
            amendRenewalnumber = amendRenewalBean.getProtocolAmendRenewalNumber();
        } else {
            this.hmModuleData = new HashMap();
            this. hmQuestionnaireData = new HashMap();
        }
        //COEUSQA-2602-Remove checkboxes from Renewal Summary screen - 
        //Setting the Check Boxes invisible for Renewal Summary Screen
        //For Renewal, 'hmEditableModules' should be empty
        if(hmEditableModules.size() == 0) {
            setCheckBoxVisible(false);
            setCheckBoxLabelVisible(false);
        } else {
            //setting the checkbox enabled and checked as per the module details.
            setEditableModules(IrbWindowConstants.GENERAL_INFO, chkGeneralInfo, amendRenewalnumber);
            setEditableModules(IrbWindowConstants.ORGANIZATION, chkProtocolOrganizations, amendRenewalnumber);
            setEditableModules(IrbWindowConstants.INVESTIGATOR, chkProtocolInvestigators, amendRenewalnumber);
            setEditableModules(IrbWindowConstants.KEY_STUDY_PERSONS, chkKeyStudyPersonnel, amendRenewalnumber);
            setEditableModules(IrbWindowConstants.CORRESPONDENTS, chkProtocolCorrespondents, amendRenewalnumber);
            setEditableModules(IrbWindowConstants.AREA_OF_RESEARCH, chkAreasofResearch, amendRenewalnumber);
            //Case#3070 - Ability to change a funding source - starts
            setEditableModules(IrbWindowConstants.FUNDING_SOURCE, chkFundingSource, amendRenewalnumber);
            //Case#3070 - Ability to change a funding source - ends      
            setEditableModules(IrbWindowConstants.SPECIAL_REVIEW, chkSpecialReview, amendRenewalnumber);
            setEditableModules(IrbWindowConstants.IDENTIFIERS, chkProtocolReferences, amendRenewalnumber);
            setEditableModules(IrbWindowConstants.UPLOAD_DOCUMENTS, chkUploadDocuments, amendRenewalnumber);
            setEditableModules(IrbWindowConstants.OTHERS, chkOthers, amendRenewalnumber);

            setEditableModules(IrbWindowConstants.SPECIES_STUDY_GROUP,chkSpecies,amendRenewalnumber);
            setEditableModules(IrbWindowConstants.ALTERNATIVE_SEARCH,chkAlternativeSearch,amendRenewalnumber);
            setEditableModules(IrbWindowConstants.SCIENTIFIC_JUSTIFICATION,chkScientificJustification,amendRenewalnumber);
            //Commented with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
    //        setEditableModules(IrbWindowConstants.STUDY_GROUP,chkStudyGroup,amendRenewalnumber);         

    //        chkOthers.setEnabled(false);
            //code added for coeus4.3 enhancement - ends
            // Added with CoeusQA2313: Completion of Questionnaire for Submission
            addQuestionnaires(amendRenewalnumber);
            // CoeusQA2313: Completion of Questionnaire for Submission - end
        }
    }
    
    /**
     * Added for coeus4.3 enhancements
     * To set the module checkbox as editable and checked as per the modules locked by the protocolNumber
     * @param moduleCode 
     * @param chkBox 
     * @param amendProtocolNumber 
     */
    public void setEditableModules(String moduleCode, JCheckBox chkBox, 
            String amendProtocolNumber){
        if(hmEditableModules.containsKey(moduleCode) && hmModuleData.containsKey(moduleCode) &&
                    hmModuleData.get(moduleCode).equals(amendProtocolNumber)){
            chkBox.setEnabled(true);
            chkBox.setSelected(true);
        } else if(hmEditableModules.containsKey(moduleCode) && !hmModuleData.containsKey(moduleCode)){
            chkBox.setEnabled(true);
        } else {
            chkBox.setEnabled(false);
            chkBox.setSelected(false);            
        }
    }
    
   
    
    //COEUSQA-2602-Remove checkboxes from Renewal Summary screen - start
    
    /** Method for setting the check boxes visibility.
     * @param visible - boolean value for setting the visibility.
     */
    public void setCheckBoxVisible(boolean visible) {
  
        chkAlternativeSearch.setVisible(visible);
        chkAreasofResearch.setVisible(visible);
        chkFundingSource.setVisible(visible);
        chkGeneralInfo.setVisible(visible);
        chkKeyStudyPersonnel.setVisible(visible);
        chkOthers.setVisible(visible);
        chkProtocolCorrespondents.setVisible(visible);
        chkProtocolInvestigators.setVisible(visible);
        chkProtocolOrganizations.setVisible(visible);
        chkProtocolReferences.setVisible(visible);
        chkScientificJustification.setVisible(visible);
        chkSpecialReview.setVisible(visible);
        chkSpecies.setVisible(visible);
        chkUploadDocuments.setVisible(visible);
        
    }
    
    /** Method for setting the check boxes label visibility.
     * @param visible - boolean value for setting the visibility.
     */
    public void setCheckBoxLabelVisible(boolean visible) {
        
        lblAlternativeSearch.setVisible(visible);
        lblAreasofResearch.setVisible(visible);
        lblFundingSource.setVisible(visible);
        lblGeneralInfo.setVisible(visible);
        lblKeyStudyPersonnel.setVisible(visible);
        lblOthers.setVisible(visible);
        lblProtocoCorrespondents.setVisible(visible);
        lblProtocolInvestigators.setVisible(visible);
        lblProtocolOrganizations.setVisible(visible);
        lblProtocolReferences.setVisible(visible);
        lblScientificJustification.setVisible(visible);
        lblSpecialReview.setVisible(visible);
        lblSpecies.setVisible(visible);
        lblUploadDocuments.setVisible(visible);
        
    }
    //COEUSQA-2602-Remove checkboxes from Renewal Summary screen - end
    
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
        // to fire save required when data changes
        getFormData();
        
        return saveRequired;
    }    
  
    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    public boolean validateData() throws CoeusUIException {
        if( txtArSummary.getText() == null || txtArSummary.getText().trim().length() == 0 ) {
            CoeusUIException coeusUIException = 
                new CoeusUIException("amend_ren_summary_exceptionCode.1101",CoeusUIException.WARNING_MESSAGE);
            //Modified for case 3552 - IRB attachments - start
            coeusUIException.setTabIndex(CoeusGuiConstants.IACUC_AMEND_REN_SUMM_EXCEP_TAB_INDEX);
            //Modified for case 3552 - IRB attachments - end
            throw coeusUIException;
        }
        return true;
    }
    
    public Vector getFormData() {
        //Modified for Case#3940 In premium the copied protocol with the original protocols approval date _ Start
        if( functionType == ADD_MODE || functionType == AMEND_MODE ) {
       // if( functionType == ADD_MODE || functionType == AMEND_MODE || functionType == 'P' ) {
            //Modified for Case#3940 In premium the copied protocol with the original protocols approval date - End
            amendRenewalList = new Vector();
            amendRenewalBean = new ProtocolAmendRenewalBean();
            amendRenewalBean.setSummary( txtArSummary.getText() );
            amendRenewalBean.setAcType(INSERT_RECORD);
            amendRenewalList.add( amendRenewalBean );
        }else{
            amendRenewalBean = ( ProtocolAmendRenewalBean ) amendRenewalList.elementAt(0);
            //code modified for adding Null check.
//            if( !amendRenewalBean.getSummary().equals( txtArSummary.getText() ) ){            
            if( amendRenewalBean.getSummary() !=null && 
                    !amendRenewalBean.getSummary().equals( txtArSummary.getText() ) ){
                saveRequired = true;
            }
            amendRenewalBean.setSummary( txtArSummary.getText() );
            amendRenewalBean.setAcType( UPDATE_RECORD );
            amendRenewalList.setElementAt( amendRenewalBean, 0 );
        }
        // Added for coeus4.3 enhancements - starts
        //To get the edited modules data.
        amendRenewalBean =(ProtocolAmendRenewalBean) amendRenewalList.get(0);
        vecEditedModuleData = new Vector();
        vecEditedQnrData = new Vector();
        String amendRenewalnumber = amendRenewalBean.getProtocolAmendRenewalNumber();
        setEditedModules(IrbWindowConstants.GENERAL_INFO, chkGeneralInfo, amendRenewalnumber);
        setEditedModules(IrbWindowConstants.ORGANIZATION, chkProtocolOrganizations, amendRenewalnumber);
        setEditedModules(IrbWindowConstants.INVESTIGATOR, chkProtocolInvestigators, amendRenewalnumber);
        setEditedModules(IrbWindowConstants.KEY_STUDY_PERSONS, chkKeyStudyPersonnel, amendRenewalnumber);
        setEditedModules(IrbWindowConstants.CORRESPONDENTS, chkProtocolCorrespondents, amendRenewalnumber);
        setEditedModules(IrbWindowConstants.AREA_OF_RESEARCH, chkAreasofResearch, amendRenewalnumber);
        //Case#3070 - Ability to change a funding source - starts
        setEditedModules(IrbWindowConstants.FUNDING_SOURCE, chkFundingSource, amendRenewalnumber);
        //Case#3070 - Ability to change a funding source - ends        
        setEditedModules(IrbWindowConstants.SPECIAL_REVIEW, chkSpecialReview, amendRenewalnumber);
        setEditedModules(IrbWindowConstants.IDENTIFIERS, chkProtocolReferences, amendRenewalnumber);
        setEditedModules(IrbWindowConstants.UPLOAD_DOCUMENTS, chkUploadDocuments, amendRenewalnumber); 
        setEditedModules(IrbWindowConstants.OTHERS, chkOthers, amendRenewalnumber); 
        
        setEditedModules(IrbWindowConstants.SPECIES_STUDY_GROUP,chkSpecies,amendRenewalnumber);
        setEditedModules(IrbWindowConstants.ALTERNATIVE_SEARCH,chkAlternativeSearch,amendRenewalnumber);
        setEditedModules(IrbWindowConstants.SCIENTIFIC_JUSTIFICATION,chkScientificJustification,amendRenewalnumber);
        //Commented with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
//        setEditedModules(IrbWindowConstants.STUDY_GROUP,chkStudyGroup,amendRenewalnumber);         
        setEditedQuestionnaires(amendRenewalnumber);
        amendRenewalBean.setVecModuleData(vecEditedModuleData);
        // Added with CoeusQA2313: Completion of Questionnaire for Submission
        amendRenewalBean.setVecSelectedOrigProtoQnr(vecEditedQnrData);
        // CoeusQA2313: Completion of Questionnaire for Submission - End
        amendRenewalList.setElementAt( amendRenewalBean, 0 );
        // Added for coeus4.3 enhancements - ends
        return amendRenewalList;
    }
    
    public void requestInitialFocus(){
        txtArSummary.requestFocusInWindow();
    }

    /**
     * Added for coeus4.3 enhancements
     * To get the modules data in modulecode as key and protocolNumber as value format
     * @param vecModuleData 
     * @return HashMap
     */
    public HashMap getFormatedModuleData(Vector vecModuleData){
        HashMap hmModuleData = new HashMap();
        ProtocolModuleBean protocolModuleBean = null;
        if(vecModuleData!=null && vecModuleData.size() > 0){
            for(int index = 0 ; index < vecModuleData.size() ; index++){
                protocolModuleBean = (ProtocolModuleBean) vecModuleData.get(index);
                if(protocolModuleBean!=null){
                    hmModuleData.put(protocolModuleBean.getProtocolModuleCode(),
                            protocolModuleBean.getProtocolAmendRenewalNumber());
                }
            }
        }
        return hmModuleData;
    }
    
    
    /**
     * Added for coeus4.3 enhancements
     * To set the edited modules data to save
     * @param moduleCode 
     * @param chkBox 
     * @param amendProtocolNumber 
     */
    public void setEditedModules(String moduleCode, JCheckBox chkBox, 
            String amendProtocolNumber){
        // If the check box is enabled and 
        // the module code is in the DB as editable for this protocol number and
        // it is not checked, 
        // then that data to be deleted in the DB and mark this module as non editable for this protocol number
        if(chkBox.isEnabled() && hmModuleData.containsKey(moduleCode) && !chkBox.isSelected()){
            if(amendRenewalBean!=null){
                // Commented with Internal Issue Fix : Amendment -Modify checkboxes doesnot save details
//                Vector vecEditableModules = (Vector) amendRenewalBean.getVecModuleData();
                if(vecCurrentEditableModules!=null && vecCurrentEditableModules.size()>0){
                    for(int index = 0 ; index < vecCurrentEditableModules.size() ; index++){
                        ProtocolModuleBean protocolModuleBean = (ProtocolModuleBean) vecCurrentEditableModules.get(index);
                        if(moduleCode.equals(protocolModuleBean.getProtocolModuleCode())){
                            protocolModuleBean.setAcType(DELETE_RECORD);
                            vecEditedModuleData.add(protocolModuleBean);
                            saveRequired = true;
                            break;
                        }
                    }
                }
            }
        }
        // If the check box is enabled and 
        // the module code is not in the DB as editable for this protocol number and
        // it is checked, 
        // then that data to be added in the DB and mark this module as editable for this protocol number            
         else if(chkBox.isEnabled() && !hmModuleData.containsKey(moduleCode) && chkBox.isSelected()){
            ProtocolModuleBean protocolModuleBean = new ProtocolModuleBean();
            protocolModuleBean.setProtocolAmendRenewalNumber(amendProtocolNumber);
            protocolModuleBean.setProtocolModuleCode(moduleCode);
            protocolModuleBean.setAcType(INSERT_RECORD);            
            vecEditedModuleData.add(protocolModuleBean);
            saveRequired = true;
        } 
    }
    
   
    
    public HashMap getHmModuleData() {
        return hmModuleData;
    }

    public void setHmModuleData(HashMap hmModuleData) {
        this.hmModuleData = hmModuleData;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnSummary = new javax.swing.JScrollPane();
        txtArSummary = new javax.swing.JTextArea();
        chkGeneralInfo = new javax.swing.JCheckBox();
        chkProtocolOrganizations = new javax.swing.JCheckBox();
        chkProtocolInvestigators = new javax.swing.JCheckBox();
        chkKeyStudyPersonnel = new javax.swing.JCheckBox();
        chkProtocolCorrespondents = new javax.swing.JCheckBox();
        chkAreasofResearch = new javax.swing.JCheckBox();
        chkOthers = new javax.swing.JCheckBox();
        chkSpecialReview = new javax.swing.JCheckBox();
        chkProtocolReferences = new javax.swing.JCheckBox();
        chkUploadDocuments = new javax.swing.JCheckBox();
        lblGeneralInfo = new javax.swing.JLabel();
        lblProtocolInvestigators = new javax.swing.JLabel();
        lblKeyStudyPersonnel = new javax.swing.JLabel();
        lblProtocolOrganizations = new javax.swing.JLabel();
        lblProtocoCorrespondents = new javax.swing.JLabel();
        lblAreasofResearch = new javax.swing.JLabel();
        lblOthers = new javax.swing.JLabel();
        lblSpecialReview = new javax.swing.JLabel();
        lblProtocolReferences = new javax.swing.JLabel();
        lblUploadDocuments = new javax.swing.JLabel();
        chkFundingSource = new javax.swing.JCheckBox();
        lblFundingSource = new javax.swing.JLabel();
        chkSpecies = new javax.swing.JCheckBox();
        chkAlternativeSearch = new javax.swing.JCheckBox();
        chkScientificJustification = new javax.swing.JCheckBox();
        lblSpecies = new javax.swing.JLabel();
        lblAlternativeSearch = new javax.swing.JLabel();
        lblScientificJustification = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(500, 296));
        scrPnSummary.setMinimumSize(new java.awt.Dimension(500, 200));
        scrPnSummary.setPreferredSize(new java.awt.Dimension(500, 200));
        txtArSummary.setFont(CoeusFontFactory.getNormalFont());
        txtArSummary.setLineWrap(true);
        txtArSummary.setWrapStyleWord(true);
        scrPnSummary.setViewportView(txtArSummary);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(scrPnSummary, gridBagConstraints);

        chkGeneralInfo.setFont(CoeusFontFactory.getLabelFont());
        chkGeneralInfo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkGeneralInfo.setEnabled(false);
        chkGeneralInfo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 3, 0, 0);
        add(chkGeneralInfo, gridBagConstraints);

        chkProtocolOrganizations.setFont(CoeusFontFactory.getLabelFont());
        chkProtocolOrganizations.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkProtocolOrganizations.setEnabled(false);
        chkProtocolOrganizations.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 14, 0, 0);
        add(chkProtocolOrganizations, gridBagConstraints);

        chkProtocolInvestigators.setFont(CoeusFontFactory.getLabelFont());
        chkProtocolInvestigators.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkProtocolInvestigators.setEnabled(false);
        chkProtocolInvestigators.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 14, 0, 0);
        add(chkProtocolInvestigators, gridBagConstraints);

        chkKeyStudyPersonnel.setFont(CoeusFontFactory.getLabelFont());
        chkKeyStudyPersonnel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkKeyStudyPersonnel.setEnabled(false);
        chkKeyStudyPersonnel.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 0, 0);
        add(chkKeyStudyPersonnel, gridBagConstraints);

        chkProtocolCorrespondents.setFont(CoeusFontFactory.getLabelFont());
        chkProtocolCorrespondents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkProtocolCorrespondents.setEnabled(false);
        chkProtocolCorrespondents.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 14, 0, 0);
        add(chkProtocolCorrespondents, gridBagConstraints);

        chkAreasofResearch.setFont(CoeusFontFactory.getLabelFont());
        chkAreasofResearch.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkAreasofResearch.setEnabled(false);
        chkAreasofResearch.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 14, 0, 0);
        add(chkAreasofResearch, gridBagConstraints);

        chkOthers.setFont(CoeusFontFactory.getLabelFont());
        chkOthers.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkOthers.setEnabled(false);
        chkOthers.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 14, 0, 0);
        add(chkOthers, gridBagConstraints);

        chkSpecialReview.setFont(CoeusFontFactory.getLabelFont());
        chkSpecialReview.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkSpecialReview.setEnabled(false);
        chkSpecialReview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 14, 0, 9);
        add(chkSpecialReview, gridBagConstraints);

        chkProtocolReferences.setFont(CoeusFontFactory.getLabelFont());
        chkProtocolReferences.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkProtocolReferences.setEnabled(false);
        chkProtocolReferences.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 14, 0, 0);
        add(chkProtocolReferences, gridBagConstraints);

        chkUploadDocuments.setFont(CoeusFontFactory.getLabelFont());
        chkUploadDocuments.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkUploadDocuments.setEnabled(false);
        chkUploadDocuments.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 0, 0);
        add(chkUploadDocuments, gridBagConstraints);

        lblGeneralInfo.setFont(CoeusFontFactory.getLabelFont());
        lblGeneralInfo.setText("General Info");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 19, 0, 0);
        add(lblGeneralInfo, gridBagConstraints);

        lblProtocolInvestigators.setFont(CoeusFontFactory.getLabelFont());
        lblProtocolInvestigators.setText("Protocol Investigators");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 30, 0, 0);
        add(lblProtocolInvestigators, gridBagConstraints);

        lblKeyStudyPersonnel.setFont(CoeusFontFactory.getLabelFont());
        lblKeyStudyPersonnel.setText("Study Personnel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 19, 0, 0);
        add(lblKeyStudyPersonnel, gridBagConstraints);

        lblProtocolOrganizations.setFont(CoeusFontFactory.getLabelFont());
        lblProtocolOrganizations.setText("Protocol Organizations");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 30, 0, 0);
        add(lblProtocolOrganizations, gridBagConstraints);

        lblProtocoCorrespondents.setFont(CoeusFontFactory.getLabelFont());
        lblProtocoCorrespondents.setText("Protocol Correspondents");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 0, 0);
        add(lblProtocoCorrespondents, gridBagConstraints);

        lblAreasofResearch.setFont(CoeusFontFactory.getLabelFont());
        lblAreasofResearch.setText("Areas of Research");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 0, 0);
        add(lblAreasofResearch, gridBagConstraints);

        lblOthers.setFont(CoeusFontFactory.getLabelFont());
        lblOthers.setText("Others");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 0, 0);
        add(lblOthers, gridBagConstraints);

        lblSpecialReview.setFont(CoeusFontFactory.getLabelFont());
        lblSpecialReview.setText("Special Review");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 0, 0);
        add(lblSpecialReview, gridBagConstraints);

        lblProtocolReferences.setFont(CoeusFontFactory.getLabelFont());
        lblProtocolReferences.setText("Protocol References");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 0, 0);
        add(lblProtocolReferences, gridBagConstraints);

        lblUploadDocuments.setFont(CoeusFontFactory.getLabelFont());
        lblUploadDocuments.setText("Add/Modify Attachments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 19, 0, 0);
        add(lblUploadDocuments, gridBagConstraints);

        chkFundingSource.setFont(CoeusFontFactory.getLabelFont());
        chkFundingSource.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkFundingSource.setEnabled(false);
        chkFundingSource.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 0, 0);
        add(chkFundingSource, gridBagConstraints);

        lblFundingSource.setFont(CoeusFontFactory.getLabelFont());
        lblFundingSource.setText("Funding Source");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 19, 0, 0);
        add(lblFundingSource, gridBagConstraints);

        chkSpecies.setFont(CoeusFontFactory.getLabelFont());
        chkSpecies.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkSpecies.setEnabled(false);
        chkSpecies.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chkSpecies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSpeciesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 14, 0, 9);
        add(chkSpecies, gridBagConstraints);

        chkAlternativeSearch.setFont(CoeusFontFactory.getLabelFont());
        chkAlternativeSearch.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkAlternativeSearch.setEnabled(false);
        chkAlternativeSearch.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 0, 0);
        add(chkAlternativeSearch, gridBagConstraints);

        chkScientificJustification.setFont(CoeusFontFactory.getLabelFont());
        chkScientificJustification.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkScientificJustification.setEnabled(false);
        chkScientificJustification.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 14, 0, 0);
        add(chkScientificJustification, gridBagConstraints);

        lblSpecies.setFont(CoeusFontFactory.getLabelFont());
        lblSpecies.setText("Species/Procedures");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 0, 1);
        add(lblSpecies, gridBagConstraints);

        lblAlternativeSearch.setFont(CoeusFontFactory.getLabelFont());
        lblAlternativeSearch.setText("Alternatives Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 19, 0, 0);
        add(lblAlternativeSearch, gridBagConstraints);

        lblScientificJustification.setFont(CoeusFontFactory.getLabelFont());
        lblScientificJustification.setText("Scientific Justification");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 0, 0);
        add(lblScientificJustification, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void chkSpeciesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSpeciesActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_chkSpeciesActionPerformed

    // Added for CoeusQA2313: Completion of Questionnaire for Submission
    /**
     * Method to add questionnaire checkboxes to form
     */
    private void addQuestionnaires(String amendRenewalnumber) {
        
        int qnrCount = hmEditableQuestionnaires.size();
        qnrCheckBoxes = new HashMap();
        if(qnrCount >0){
            
            JPanel pnlQnr = new JPanel();
            pnlQnr.setLayout(new GridBagLayout());
            // Add Checkboxes and labels
            JCheckBox chkQnr;
            JLabel lblQnr;
            GridBagConstraints gridBagConstraints;
            String qnrId,qnrLabel;
            Dimension lblMaxDimension = new Dimension(216,18);
            Iterator it = hmEditableQuestionnaires.keySet().iterator();
            for(int gridx = 0,gridy = 0; it.hasNext() ; gridx++){
                //Go to next line after 3 columns
                if(gridx>1){
                    gridx = 0;
                    gridy++;
                }
                qnrId = String.valueOf(it.next());
                qnrLabel = (String)hmEditableQuestionnaires.get(qnrId);
                chkQnr = new JCheckBox();
                chkQnr.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
                gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = gridx;
                gridBagConstraints.gridy = gridy;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                if(gridx==0){
                    gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 0);
                }else{
                    gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
                }
                pnlQnr.add(chkQnr, gridBagConstraints);
                lblQnr = new JLabel(qnrLabel);
                
                lblQnr.setMaximumSize(lblMaxDimension);
                lblQnr.setMinimumSize(lblMaxDimension);
                lblQnr.setPreferredSize(lblMaxDimension);
                lblQnr.setFont(CoeusFontFactory.getLabelFont());
                if(gridx==0){
                    gridBagConstraints.insets = new java.awt.Insets(3, 16, 0, 0);
                }else{
                    gridBagConstraints.insets = new java.awt.Insets(3, 27, 0, 0);
                }
                pnlQnr.add(lblQnr, gridBagConstraints);
                
                chkQnr.putClientProperty(0,qnrId);
                enableQuestionnaireCheckBoxes(qnrId,chkQnr,amendRenewalnumber);
                chkQnr.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        JCheckBox checkBox = (JCheckBox)e.getSource();
                        String qnrId = (String)checkBox.getClientProperty(0);
                        qnrCheckBoxes.put(qnrId,checkBox.isSelected());
                    }
                });
            }
            
             if(qnrCount < 3){
                    gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = qnrCount;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.weightx = 1;
                pnlQnr.add(new JLabel(),gridBagConstraints);
            }
            
            JScrollPane scrPnQnr = new JScrollPane(pnlQnr);
            scrPnQnr.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Questionnaire",TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 6;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            add(scrPnQnr,gridBagConstraints);
        }
    }
   
         
    /**
     * To set the module checkbox as editable and checked as per the questionnaires locked by the protocolNumber
     * @param moduleCode
     * @param chkBox
     * @param amendProtocolNumber
     */
    private void enableQuestionnaireCheckBoxes(String questionnaireId, JCheckBox chkBox,
            String amendProtocolNumber){
        if(hmEditableQuestionnaires.containsKey(questionnaireId) && hmQuestionnaireData.containsKey(questionnaireId) &&
                hmQuestionnaireData.get(questionnaireId).equals(amendProtocolNumber)){
            chkBox.setEnabled(true);
            chkBox.setSelected(true);
        } else if(hmEditableQuestionnaires.containsKey(questionnaireId) && !hmQuestionnaireData.containsKey(questionnaireId)){
            chkBox.setEnabled(true);
        } else {
            chkBox.setEnabled(false);
            chkBox.setSelected(false);
        }
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
            chkBox.setEnabled(false);
        }
        qnrCheckBoxes.put(questionnaireId,chkBox.isSelected());
    }
    
    /**
     * To get the QuestionnaireId as key and protocolNumber as value format
     * @param questionnaires - Vector
     * @return hmModuleData - HashMap
     */
    public HashMap getFormattedQnrData(Vector questionnaires){
        HashMap hmQnrData = new HashMap();
        ProtocolModuleBean protocolModuleBean = null;
        if(questionnaires!=null && !questionnaires.isEmpty()){
            for(Object obj: questionnaires){
                protocolModuleBean = (ProtocolModuleBean)obj;
                if(protocolModuleBean!=null){
                    hmQnrData.put(protocolModuleBean.getProtocolModuleCode(),
                            protocolModuleBean.getProtocolAmendRenewalNumber());
                }
            }
        }
        return hmQnrData;
    }
    
    /**
     * To get the QuestionnaireId as key and protocolNumber as value format
     * @param questionnaires - Vector
     * @return hmModuleData - HashMap
     */
    public HashMap getEditableQnrData(Vector questionnaires){
        HashMap hmQnrData = new HashMap();
        ProtocolModuleBean protocolModuleBean = null;
        if(questionnaires!=null && !questionnaires.isEmpty()){
            for(Object obj: questionnaires){
                protocolModuleBean = (ProtocolModuleBean)obj;
                if(protocolModuleBean!=null){
                    hmQnrData.put(protocolModuleBean.getProtocolModuleCode(),
                            protocolModuleBean.getProtocolAmendRenewalNumber());
                }
            }
        }
        return hmQnrData;
    }
    /**
     * Method to get the questionnaire data to be updated to database
     */
    private void setEditedQuestionnaires(String amendProtocolNumber){
        
        if(qnrCheckBoxes!=null && !qnrCheckBoxes.isEmpty()){
            String qnrIdKey;
            Iterator it = qnrCheckBoxes.keySet().iterator();
//            for(Object obj : qnrCheckBoxes ){
            while(it.hasNext()){
            qnrIdKey =  (String) it.next();
            boolean selected = (Boolean)qnrCheckBoxes.get(qnrIdKey);
//                String qnrId = (String)chkBox.getClientProperty(0);
                // If the check box is enabled and
                // the module code is in the DB as editable for this protocol number and
                // it is not checked,
                // then that data to be deleted in the DB and mark this module as non editable for this protocol number
                if(!selected && hmQuestionnaireData.containsKey(qnrIdKey) ){
                    if(amendRenewalBean!=null){
                        // Commented with Internal Issue Fix : Amendment -Modify checkboxes doesnot save details
//                        Vector vecEditableQuestionnaires = (Vector) amendRenewalBean.getVecSelectedOrigProtoQnr();
                        if(vecCurrentEditableQuestionnaires!=null && !vecCurrentEditableQuestionnaires.isEmpty()){
                            ProtocolModuleBean protocolModuleBean;
                            for(Object objBean : vecCurrentEditableQuestionnaires){
                                protocolModuleBean = (ProtocolModuleBean)objBean;
                                if(qnrIdKey.equals(protocolModuleBean.getProtocolModuleCode())){
                                    protocolModuleBean.setAcType(DELETE_RECORD);
                                    vecEditedQnrData.add(protocolModuleBean);
                                    saveRequired = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                // If the check box is enabled and
                // the module code is not in the DB as editable for this protocol number and
                // it is checked,
                // then that data to be added in the DB and mark this module as editable for this protocol number
                else if(selected && !hmQuestionnaireData.containsKey(qnrIdKey)){
                    ProtocolModuleBean protocolModuleBean = new ProtocolModuleBean();
                    protocolModuleBean.setProtocolAmendRenewalNumber(amendProtocolNumber);
                    protocolModuleBean.setProtocolModuleCode(qnrIdKey);
                    protocolModuleBean.setAcType(INSERT_RECORD);
                    vecEditedQnrData.add(protocolModuleBean);
                    saveRequired = true;
                }
            }
        }
    }
    
    /**
     * Method to get the details of selected qnr check boxes
     * returns hashmap with key as questionnaire Id and value as Y/N
     * based on whether it is selected or not.
     */
    public HashMap getAllSelectedQuestionnaires() {
        return qnrCheckBoxes;
    }
    

    // CoeusQA2313: Completion of Questionnaire for Submission - End

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkAlternativeSearch;
    private javax.swing.JCheckBox chkAreasofResearch;
    private javax.swing.JCheckBox chkFundingSource;
    private javax.swing.JCheckBox chkGeneralInfo;
    private javax.swing.JCheckBox chkKeyStudyPersonnel;
    public javax.swing.JCheckBox chkOthers;
    private javax.swing.JCheckBox chkProtocolCorrespondents;
    private javax.swing.JCheckBox chkProtocolInvestigators;
    private javax.swing.JCheckBox chkProtocolOrganizations;
    private javax.swing.JCheckBox chkProtocolReferences;
    private javax.swing.JCheckBox chkScientificJustification;
    private javax.swing.JCheckBox chkSpecialReview;
    private javax.swing.JCheckBox chkSpecies;
    private javax.swing.JCheckBox chkUploadDocuments;
    private javax.swing.JLabel lblAlternativeSearch;
    private javax.swing.JLabel lblAreasofResearch;
    private javax.swing.JLabel lblFundingSource;
    private javax.swing.JLabel lblGeneralInfo;
    private javax.swing.JLabel lblKeyStudyPersonnel;
    private javax.swing.JLabel lblOthers;
    private javax.swing.JLabel lblProtocoCorrespondents;
    private javax.swing.JLabel lblProtocolInvestigators;
    private javax.swing.JLabel lblProtocolOrganizations;
    private javax.swing.JLabel lblProtocolReferences;
    private javax.swing.JLabel lblScientificJustification;
    private javax.swing.JLabel lblSpecialReview;
    private javax.swing.JLabel lblSpecies;
    private javax.swing.JLabel lblUploadDocuments;
    private javax.swing.JScrollPane scrPnSummary;
    private javax.swing.JTextArea txtArSummary;
    // End of variables declaration//GEN-END:variables
    
}
