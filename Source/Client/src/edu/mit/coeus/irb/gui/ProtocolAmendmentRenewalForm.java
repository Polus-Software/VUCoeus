/*
 * ProtocolAmendmentRenewalForm.java
 *
 * Created on August 25, 2003, 12:11 PM
 */

package edu.mit.coeus.irb.gui;

import java.util.HashMap;
import javax.swing.JComponent;
import java.util.Vector;

import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.utils.CoeusGuiConstants;

/**
 *
 * @author  ravikanth
 */
public class ProtocolAmendmentRenewalForm implements TypeConstants {
    
    private char functionType;
    private Vector amendRenewalData;
    private int moduleCode;
    private String title;
    private AmendmentRenewalList amendRenList;
    private AmendmentRenewalSummary amendRenSummary;
    // Added for coeus4.3 enhancement to get editable modiles for Amendments/Renewals
    private Vector editableModules;
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    private HashMap hmApplicableQuestionnaire;
    // CoeusQA2313: Completion of Questionnaire for Submission- end
    
    /** Creates a new instance of ProtocolAmendmentRenewalForm */
    public ProtocolAmendmentRenewalForm(char functionType, Vector amendRenewalData,
        int moduleCode, Vector editableModules,  HashMap hmApplicableQuestionnaire) {
            this.functionType = functionType;
            this.amendRenewalData = amendRenewalData;
            this.moduleCode = moduleCode;
            this.editableModules = editableModules;
            this.hmApplicableQuestionnaire = hmApplicableQuestionnaire;
            if( moduleCode == CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
                title = "Amendments & Renewals";
            }else if( moduleCode == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE ) {
                title = "Amendment Summary";
            }else if( moduleCode == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE ) {
                title = "Renewal Summary";
            }
    }
    
    
    public JComponent getAmendRenewalPanel() {
        if ( moduleCode == CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
            amendRenList = new AmendmentRenewalList(amendRenewalData, functionType);
            return amendRenList;
        }else{
            //code modified for coeus4.3 enhancement
//            amendRenSummary = new AmendmentRenewalSummary(amendRenewalData, functionType );
            // Added new parameter- hmApplicableQuestionnaire - with CoeusQA2313: Completion of Questionnaire for Submission
            amendRenSummary = new AmendmentRenewalSummary(amendRenewalData, functionType, editableModules, hmApplicableQuestionnaire);
            return amendRenSummary;
        }
    }
    
    public boolean isSaveRequired() {
        if( moduleCode != CoeusGuiConstants.PROTOCOL_DETAIL_CODE && 
                functionType != DISPLAY_MODE ){
            return amendRenSummary.isSaveRequired();
        }
        return false;
    }   
    
    public void setSaveRequired(boolean save) {
        if( moduleCode != CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
            amendRenSummary.setSaveRequired( save );
        }
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
        if( moduleCode != CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
            amendRenSummary.setFunctionType( functionType );
        }
        
    }
    
    public void setValues( Vector amendRenData ) {
        this.amendRenewalData = amendRenData;
        if( moduleCode == CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
            amendRenList.setValues( amendRenData );
        }else{
            amendRenSummary.setValues( amendRenData );
        }
    }
    
    /** Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    public boolean validateData() throws CoeusUIException {
        if( moduleCode != CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
            return amendRenSummary.validateData();
        }
        return true;
    }
    
    public Vector getFormData() {
        if( moduleCode != CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
            return amendRenSummary.getFormData();
        }
        return null;
    }
    
    public void requestInitialFocus(){
        if( amendRenSummary != null ) {
            amendRenSummary.requestInitialFocus();
        }
        else{
            amendRenList.setDefaultFocusForComponent();
        }
    }
    
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    /*
     * Getter for all selected questionnaire map from amendment/renewal summary page
     *
     */
    public HashMap getAllEditingOriginalProtocolQuestionnaires(){
        if( moduleCode != CoeusGuiConstants.PROTOCOL_DETAIL_CODE ) {
            return amendRenSummary.getAllSelectedQuestionnaires();
        }else return null;
    }
    
}
