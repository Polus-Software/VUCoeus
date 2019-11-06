/*
 * ProtocolActionForm.java
 *
 * Created on January 21, 2008, 3:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.irb.form;

import edu.mit.coeuslite.utils.DateUtils;
import java.sql.Timestamp;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

/**
 *
 * @author nandkumarsn
 */
public class ProtocolActionForm extends ValidatorForm{
    
    private String protocolNumber;
    private int sequenceNumber;
    private int submissionNumber;    
    private String scheduleId;
    private String comments;
    private String actionDate;
    private String protocolActionTypeCode;
    private String unitNumber;
    private String leadUnitFlag;
    private String actionId;
    private String committeeId;
    private String approvalDate;
    private String expirationDate;
    private String acType;        
    private FormFile document;
    private String updateUser;
    private Timestamp updateTimestamp;    
    //Added for case#3214 - Withdraw Submission - start
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";    
    //Added for case#3214 - Withdraw Submission - end
    //Code added for Case#3554 - Notify IRB enhancement
    private String notificationType;
    // COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document -Start
    private String description;
    private Vector vecAttachments;
    private String fileName;
    private int documentId;
    // COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document -End
    
    private String oldNotificationType;
    /**
     * Creates a new instance of ProtocolActionForm
     */
    public ProtocolActionForm() {
    }

    public FormFile getDocument() {
        return document;
    }

    public void setDocument(FormFile document) {
        this.document = document;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getSubmissionNumber() {
        return submissionNumber;
    }

    public void setSubmissionNumber(int submissionNumber) {
        this.submissionNumber = submissionNumber;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getProtocolActionTypeCode() {
        return protocolActionTypeCode;
    }

    public void setProtocolActionTypeCode(String protocolActionTypeCode) {
        this.protocolActionTypeCode = protocolActionTypeCode;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getLeadUnitFlag() {
        return leadUnitFlag;
    }

    public void setLeadUnitFlag(String leadUnitFlag) {
        this.leadUnitFlag = leadUnitFlag;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request){
        ActionErrors errors = new ActionErrors();
        DateUtils dtUtils = new DateUtils();
        boolean isValidFile = true;
        if(document != null){
            if(document.getFileName() != null && !document.getFileName().trim().equals("")){
                int index = document.getFileName().lastIndexOf('.');
                if(index != -1 && index != document.getFileName().length()){
                    String extension = document.getFileName().substring(index+1,document.getFileName().length());
                    if(extension==null || extension.equals("")) {
                        isValidFile = false;
                    }
                } else {
                    isValidFile = false;
                }
            }
        }
        if(!isValidFile){
            errors.add("documentFileName", new ActionMessage("error.upload_invalid_type"));
        }
        
        //Added for case#3214 - Withdraw Submission - start
        if(getActionDate() == null || getActionDate().equals("")){
            errors.add("actionDateRequired", new ActionMessage("protocolAction.errors.actionDate1"));
        }else{
            String userActionDate = dtUtils.formatDate(getActionDate(), DATE_SEPARATERS, SIMPLE_DATE_FORMAT);
            if(userActionDate == null){
                errors.add("invalidActionDate", new ActionMessage("protocolAction.errors.actionDate2"));
            }
        }
        //Added for case#3214 - Withdraw Submission - end
        return errors;
    }        

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
    // COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document -Start
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Vector getVecAttachments() {
        return vecAttachments;
    }

    public void setVecAttachments(Vector vecAttachments) {
        this.vecAttachments = vecAttachments;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }
    // COEUSDEV-328: Notify IRB FYI submission only permits the Aggregator to upload one document -End

    public String getOldNotificationType() {
        return oldNotificationType;
    }

    public void setOldNotificationType(String oldNotificationType) {
        this.oldNotificationType = oldNotificationType;
    }
}
