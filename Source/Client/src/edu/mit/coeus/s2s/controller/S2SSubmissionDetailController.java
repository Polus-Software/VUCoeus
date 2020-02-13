/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * S2SSubmissionDetailController.java
 *
 * Created on February 15, 2005, 12:18 PM
 */

package edu.mit.coeus.s2s.controller;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.bean.DBOpportunityInfoBean;
import edu.mit.coeus.s2s.bean.FormInfoBean;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.s2s.bean.ProcessGrantsSubmission;
import edu.mit.coeus.s2s.bean.SubmissionDetailInfoBean;
//import edu.mit.coeus.s2s.gui.IGrantsSubmission;
//import edu.mit.coeus.s2s.gui.OpportunitySelectionForm;
import edu.mit.coeus.s2s.gui.S2SStatusDetailForm;
import edu.mit.coeus.s2s.gui.S2SSubmissionDetailForm;
//import edu.mit.coeus.s2s.util.*;
import edu.mit.coeus.s2s.validator.S2SValidationException;
//import edu.mit.coeus.utils.tree.xml.XMLTreeCellRenderer;
//import edu.mit.coeus.utils.tree.xml.XMLTreeModel;

import java.util.List;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author  Geo Thomas
 */
public class S2SSubmissionDetailController implements ActionListener, 
                                                      ListSelectionListener,
                                                      MouseListener, ItemListener
{
    private S2SSubmissionDetailForm subDetailForm;
    private CoeusDlgWindow dlgSubDetailForm;
    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm;
    private OppFormsTableModel oppFrmTblMdl;
    private SubAttachmentsTableModel attTblMdl;
    //private static final int WIDTH=690;
    //private static final int HEIGHT = 420;
    private static final int FORM_NAME_COLUMN=0;
    private static final int MANDATORY_COLUMN=1;
    private static final int INCLUDE_COLUMN=2;
    private static final int AVAILABLE_COLUMN=3;
    private static final String EMPTY_STRING = "";
    private static final String S2S_SERVLET = CoeusGuiConstants.CONNECTION_URL+ "/S2SServlet";
    
    private static String FORM_NAME = "Form Name";
    private static String MANDATORY = "Mandatory";
    private static String INCLUDE = "Include";
    
    private boolean saveNContinue;
    private String submissionTitle;
    private OpportunityInfoBean opportunityInfoBean;
    private Vector opportunityFormDetails;
    private Vector submissionDetails;
    private ProcessGrantsSubmission processGrant;
    private String sponsor;
    private char functionType;
    private SimpleDateFormat dateFormat;
    private HashMap rightFlags;
    private int optIncludedFormsNo,availableFormsNo,mandatoryFormsNo;
    private char mode;
    
    //modification for new columns in OSP$S2S_OPPORTUNITY - S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - 1 - START
    private java.util.List lstSubmissionTypeCode;
    private java.util.List lstSubmisionEndPoints;
    //private int defaultSubmissionTypeCode;
    private char INC_AWARD = 'A';
    private char DEC_AWARD = 'B';
    private char INC_DURATION = 'C';
    private char DEC_DURATION = 'D';
    private char OTHER = 'E';
    
    private static final String pattern = "dd-MMM-yyyy hh:mm:ss a";
    private DateUtils dateUtils;
    
    private boolean dataModified = false;
    //modification for new columns in OSP$S2S_OPPORTUNITY, S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION- 1 - END
    
     private boolean editable ;
             
    /** Creates a new instance of QuestionMaintainanceController */
    public S2SSubmissionDetailController(ProcessGrantsSubmission processGrant) 
                                                        throws CoeusException {
        this.processGrant = processGrant;
        this.mdiForm = CoeusGuiConstants.getMDIForm();
        subDetailForm = new S2SSubmissionDetailForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        dateFormat = new SimpleDateFormat(pattern);
        dateUtils = new DateUtils();
        registerComponents();
        setFormData(null);
        setColumnData();
        formatFields();
        postInitComponents();

    }
    public S2SSubmissionDetailController() throws CoeusException {
        this(null);
    }
    
    public void formatFields() {
        subDetailForm.lblCosingDate.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblOpportunityId.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblOpportunityTitle.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.txtOpportunityTitle.setFont(subDetailForm.txtOpportunityId.getFont());
        subDetailForm.lblRecDate.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblCFDANumber.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblCompetitionId.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblInstructionUrl.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblSchemaUrl.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblProposalNum.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblAgTrackingId.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblLastModDate.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblNotes.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblStatusRecDate.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblSubmStatus.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblTrackingId.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.btnClose.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.btnSltOpportunity.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.btnSubmit.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.btnValidate.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.btnRefresh.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.btnPrint.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblSubmissionType.setFont(CoeusFontFactory.getLabelFont());
        subDetailForm.lblSubmissionEndPoint.setFont(CoeusFontFactory.getLabelFont());
        //Case:2913 - START
        subDetailForm.btnSave.setFont(CoeusFontFactory.getLabelFont());
        //Case:2913 - END
    }
    private boolean subBtnEnbFlag = true;
    private void enableButtons(){
        boolean attrMatchFlag = true;
        
        boolean alterProposalData = false;
        
        if(rightFlags!=null) {
            attrMatchFlag = ((Boolean)rightFlags.get(
                    S2SConstants.IS_ATTR_MATCH)).booleanValue();
            subBtnEnbFlag = attrMatchFlag && subBtnEnbFlag && ((Boolean)rightFlags.get(
                    S2SConstants.SUBMIT_TO_SPONSOR)).booleanValue()&&
                    ((Boolean)rightFlags.get(
                    S2SConstants.IS_READY_TO_SUBMIT)).booleanValue();
            if(getMode() == TypeConstants.DISPLAY_MODE) {
                alterProposalData = ((Boolean)rightFlags.get(S2SConstants.ALTER_PROPOSAL_DATA)).booleanValue();
            }else {
                alterProposalData = true;
            }
        }
//        subBtnEnbFlag = subBtnEnbFlag && (mandatoryFormsNo>0 || includedFormsNo>0) && availableFormsNo>0;
        boolean submitted = subStatusDetails!=null && this.subStatusDetails.getGrantsGovTrackingNumber()!=null;
        editable  = (!submitted && alterProposalData);
        
        oppFrmTblMdl.setEditable(editable );
         
//        this.subDetailForm.btnRefresh.setEnabled(submitted);
        subDetailForm.btnRefresh.setEnabled(subStatusDetails!=null && subStatusDetails.getStatus()!=null && !subStatusDetails.getStatus().trim().equals(""));
        subDetailForm.btnSltOpportunity.setEnabled(editable );
        subDetailForm.btnOppDelete.setEnabled(editable );
        subDetailForm.btnValidate.setEnabled(editable );
        subBtnEnbFlag = subBtnEnbFlag && availableFormsNo>0 && !submitted;
        subDetailForm.btnSubmit.setEnabled(subBtnEnbFlag);
        subDetailForm.btnMoreSubStatus.setEnabled(!editable );
        
        subDetailForm.rdBtnDecreaseAward.setEnabled(editable );
        subDetailForm.rdBtnDecreaseDuration.setEnabled(editable );
        subDetailForm.rdBtnIncreaseAward.setEnabled(editable );
        subDetailForm.rdBtnIncreaseDuration.setEnabled(editable );
        subDetailForm.rdBtnOther.setEnabled(editable );
        subDetailForm.cmbSubmissionType.setEnabled(editable );
        subDetailForm.cmbSubmissionEndPoint.setEnabled(editable );
        
        if(!submitted && !attrMatchFlag){
            subDetailForm.btnRefresh.setEnabled(false);
            subDetailForm.btnSltOpportunity.setEnabled(true);
            subDetailForm.btnOppDelete.setEnabled(true);
            subDetailForm.btnValidate.setEnabled(false);
            subDetailForm.btnSubmit.setEnabled(false);
            subDetailForm.btnPrint.setEnabled(false);
            subDetailForm.lblMessage.setText(coeusMessageResources.parseMessageKey(
                        "s2ssubdetfrm_exceptionCode.1016"));
            subDetailForm.lblMessage.setForeground(Color.red);
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
//                        "s2ssubdetfrm_exceptionCode.1016")+"\n"+
//                        coeusMessageResources.parseMessageKey("s2ssubdetfrm_exceptionCode.1017"));
        }
        //Case:2913 - START
        subDetailForm.btnSave.setEnabled(editable);
        //Case:2913 - END
    }
    
    private void validate() throws Exception{
        if(mandatoryFormsNo==0 && optIncludedFormsNo==0)
            throw new CoeusException(coeusMessageResources.parseMessageKey(
                        "s2ssubdetfrm_exceptionCode.1008"));
        //modification for new columns in OSP$S2S_OPPORTUNITY - S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - 2 - START
        //ButtonModel bm = null;
        if(processGrant.getPropDevBean()!=null)
        if(processGrant.getPropDevBean().getProposalTypeCode() == 4) { // 4 == Revision
            if((subDetailForm.btnGrpAward.getSelection() == null || subDetailForm.rdBtnAward.isSelected()) && 
            (subDetailForm.btnGrpDuration.getSelection() == null || subDetailForm.rdBtnDuration.isSelected()) &&
            !subDetailForm.rdBtnOther.isSelected()){
                //Select Revision Type
                throw new CoeusException(coeusMessageResources.parseMessageKey("s2ssubdetfrm_exceptionCode.1014"));
            }else if(subDetailForm.rdBtnOther.isSelected() && subDetailForm.txtOther.getText().trim().length() == 0){
                //Enter Other Revision Type
                throw new CoeusException(coeusMessageResources.parseMessageKey("s2ssubdetfrm_exceptionCode.1015"));
            }
        }
        //modification for new columns in OSP$S2S_OPPORTUNITY - S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - 2 - END
    }
    public java.awt.Component getControlledUI() {
        return subDetailForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        oppFrmTblMdl = new OppFormsTableModel();
        subDetailForm.tblForms.setModel(oppFrmTblMdl);
        subDetailForm.tblForms.getSelectionModel().addListSelectionListener(this);
        
        attTblMdl = new SubAttachmentsTableModel();
        subDetailForm.tblAttachments.setModel(attTblMdl);
        subDetailForm.tblAttachments.getSelectionModel().addListSelectionListener(this);

        //--subDetailForm.txtSchemaURL.addMouseListener(this);
        //--subDetailForm.txtInstructionURL.addMouseListener(this);
        subDetailForm.txtSubmStatus.addMouseListener(this);
        
        subDetailForm.btnClose.addActionListener(this);
        subDetailForm.btnSltOpportunity.addActionListener(this);
        subDetailForm.btnOppDelete.addActionListener(this);
        subDetailForm.btnSubmit.addActionListener(this);
        subDetailForm.btnValidate.addActionListener(this);
        subDetailForm.btnRefresh.addActionListener(this);
        subDetailForm.btnPrint.addActionListener(this);
        subDetailForm.btnMoreSubStatus.addActionListener(this);
        //Case:2913 - START
        subDetailForm.btnSave.addActionListener(this);
        //Case:2913 - END
        
        //modification for new columns in OSP$S2S_OPPORTUNITY - S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - 3 - START
        subDetailForm.rdBtnDecreaseAward.addItemListener(this);
        subDetailForm.rdBtnDecreaseDuration.addItemListener(this);
        subDetailForm.rdBtnIncreaseAward.addItemListener(this);
        subDetailForm.rdBtnIncreaseDuration.addItemListener(this);
        subDetailForm.rdBtnOther.addItemListener(this);        
        subDetailForm.cmbSubmissionType.addActionListener(this);
        subDetailForm.cmbSubmissionEndPoint.addActionListener(this);
        
        subDetailForm.txtOther.setDocument(new LimitedPlainDocument(45));
        
        subDetailForm.rdBtnAward.setVisible(false);
        subDetailForm.rdBtnDuration.setVisible(false);
        //modification for new columns in OSP$S2S_OPPORTUNITY - S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - 3 - END
        
    }
    private SubmissionDetailInfoBean subStatusDetails;
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        if(data==null) return;
         Object[] formData = (Object[])data;
        opportunityInfoBean = (OpportunityInfoBean)formData[0];
        subDetailForm.lblProposalNumberVal.setText(submissionTitle);
        subDetailForm.lblSponsorVal.setText(sponsor);
        subDetailForm.lblMessage.setText(null);
        setOppData(opportunityInfoBean);
        ArrayList formListData = (ArrayList)formData[1];
        availableFormsNo = 0;optIncludedFormsNo=0;mandatoryFormsNo=0;
        if(formListData!=null && !formListData.isEmpty()){
            Vector oppFrmTblData = new Vector(formListData);
            for(int index=0;index<oppFrmTblData.size();index++){
                FormInfoBean frmInfo = (FormInfoBean)oppFrmTblData.get(index);
                
                //////////////////////
                if(frmInfo.isAvailable()){
                    availableFormsNo++;
                    if(frmInfo.isMandatory())
                        mandatoryFormsNo++;
                    else if(frmInfo.isInclude())
                        optIncludedFormsNo++;
                }else if(frmInfo.isMandatory()){
                    subBtnEnbFlag = false;//make submit button disable
                                            //If a mandatory form is not available
                }
                
                frmInfo.setProposalNumber(this.submissionTitle);
                frmInfo.setUpdateUser(CoeusGuiConstants.getMDIForm().getUserId());
                if(frmInfo.getAcType()=='\u0000') frmInfo.setAcType('I');
            }
            if ((availableFormsNo-mandatoryFormsNo)>0) 
                subDetailForm.lblMessage.setText("Selected "+optIncludedFormsNo+
                      " out of "+(availableFormsNo-mandatoryFormsNo)+ 
                          " Available Optional Forms");
            int[] sltdIndices = subDetailForm.tblForms.getSelectedRows();
            oppFrmTblMdl.setData(oppFrmTblData);
            oppFrmTblMdl.fireTableDataChanged();
            if(sltdIndices.length==0){
                subDetailForm.tblForms.setRowSelectionInterval(0, 0 );
            }else{
                ListSelectionModel lstModel = subDetailForm.tblForms.getSelectionModel();
                int totRows = subDetailForm.tblForms.getRowCount();
                for(int i=0;i<sltdIndices.length&&i<totRows;i++)
                    lstModel.addSelectionInterval(sltdIndices[i], sltdIndices[i]);
            }
//            subDetailForm.tblForms.setRowSelectionInterval(0, 0 );
        }
        //modification for new columns in OSP$S2S_OPPORTUNITY - S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - 4 - START
        if(formData.length >= 5){
            lstSubmissionTypeCode = (java.util.List)formData[4];
            int submissionTypeCode = Integer.parseInt((String)formData[5]);//Default Submission Type Code
            int selectedIndex = 0;
            ComboBoxBean comboBoxBean;
            submissionTypeCode = opportunityInfoBean.getSubmissionTypeCode();
            if(opportunityInfoBean.getSubmissionTypeCode() != 0) {
                submissionTypeCode = opportunityInfoBean.getSubmissionTypeCode();
            }
            //If Combobox is already populated. no need to add data again.
            boolean populateItems = subDetailForm.cmbSubmissionType.getItemCount() == 0;
            for(int index = 0; index < lstSubmissionTypeCode.size(); index++){
                comboBoxBean = (ComboBoxBean)lstSubmissionTypeCode.get(index);
                if(Integer.parseInt(comboBoxBean.getCode()) == submissionTypeCode) {
                    selectedIndex = index;
                }
                
                if(populateItems){
                    subDetailForm.cmbSubmissionType.addItem(comboBoxBean.getDescription());
                }
            }
            subDetailForm.cmbSubmissionType.setSelectedIndex(selectedIndex);
        }
        
        if(opportunityInfoBean.getRevisionCode() != null || (processGrant.getPropDevBean() != null && processGrant.getPropDevBean().getProposalTypeCode() == 6)) { // 6 == Revision
            subDetailForm.txtOther.setVisible(false);
            //set values to fields
            if(opportunityInfoBean.getRevisionCode() != null) {
                if(opportunityInfoBean.getRevisionCode().indexOf(INC_AWARD) >= 0) {
                    subDetailForm.rdBtnIncreaseAward.setSelected(true);
                }else if(opportunityInfoBean.getRevisionCode().indexOf(DEC_AWARD) >= 0){
                    subDetailForm.rdBtnDecreaseAward.setSelected(true);
                }
                
                if(opportunityInfoBean.getRevisionCode().indexOf(INC_DURATION) >= 0) {
                    subDetailForm.rdBtnIncreaseDuration.setSelected(true);
                }else if(opportunityInfoBean.getRevisionCode().indexOf(DEC_DURATION) >= 0){
                    subDetailForm.rdBtnDecreaseDuration.setSelected(true);
                }
                
                if(opportunityInfoBean.getRevisionCode().indexOf(OTHER) >= 0) {
                    subDetailForm.rdBtnOther.setSelected(true);
                    subDetailForm.txtOther.setVisible(true);
                    subDetailForm.txtOther.setText(opportunityInfoBean.getRevisionOtherDescription());
                }
            }//End if RevisionCode != null
        }else {
            //Disable Revision Types
            subDetailForm.rdBtnDecreaseAward.setEnabled(false);
            subDetailForm.rdBtnDecreaseDuration.setEnabled(false);
            subDetailForm.rdBtnIncreaseAward.setEnabled(false);
            subDetailForm.rdBtnIncreaseDuration.setEnabled(false);
            subDetailForm.rdBtnOther.setEnabled(false);
            subDetailForm.txtOther.setVisible(false);
        }
        
        //modification for new columns in OSP$S2S_OPPORTUNITY - S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - 4 - END
        
        subStatusDetails = (SubmissionDetailInfoBean)formData[2];
        if(formData.length>=4)
            this.rightFlags = (HashMap)formData[3];
        
        if(formData.length >= 7){
           lstSubmisionEndPoints  = (java.util.List)formData[6];
           ComboBoxBean comboBoxBean;
           int selectedIndex = 0;
           //Case 4380 - START 1
           boolean submissionEndPointSaved = false;
           //Case 4380 - END 1
            String sltdSubmissionEndPoint = opportunityInfoBean.getSubmissionEndPoint();
            //If Combobox is already populated. no need to add data again.
            boolean populateItems = subDetailForm.cmbSubmissionEndPoint.getItemCount() == 0;
            for(int index = 0; index < lstSubmisionEndPoints.size(); index++){
                comboBoxBean = (ComboBoxBean)lstSubmisionEndPoints.get(index);
                if(comboBoxBean.getCode().equals(sltdSubmissionEndPoint)) {
                    selectedIndex = index;
                    //Case 4380 - START 2
                    submissionEndPointSaved = true;
                    //Case 4380 - END 2
                }
                if(populateItems){
                    subDetailForm.cmbSubmissionEndPoint.addItem(comboBoxBean.getDescription());
                }
            }
            //Case 4380 - START 3
            if(submissionEndPointSaved) {
                subDetailForm.cmbSubmissionEndPoint.setSelectedIndex(selectedIndex);
            }else {
                subDetailForm.cmbSubmissionEndPoint.setSelectedIndex(0);//select 2007 End point if data not available.
                //S2SSubmissionDataTxnBean.getSubmissionEndPoints() has 2007 as 0th index. so select index 0.
            }
            //Case 4380 - END 3
        }

        if(subStatusDetails!=null){
            setSubmissionData(subStatusDetails);
        }
        enableButtons();
        dataModified = false;
    }
    public void setOppData(OpportunityInfoBean oppInfoBean){
        DBOpportunityInfoBean oppInfo = (DBOpportunityInfoBean)oppInfoBean;
        if(oppInfo==null) return;
       subDetailForm.txtOpportunityTitle.setText(oppInfo.getOpportunityTitle());
       subDetailForm.txtOpportunityTitle.setCaretPosition(0);
       subDetailForm.txtCFDANumber.setText(oppInfo.getCfdaNumber());
       if(oppInfo.getClosingDate()!=null){
           
//           subDetailForm.txtClosingDate.setText(dateFormat.format(oppInfo.getClosingDate()));
           subDetailForm.txtClosingDate.setText(dateUtils.formatDate(oppInfo.getClosingDate(),pattern));
       }
       subDetailForm.txtClosingDate.setCaretPosition(0);
       if(oppInfo.getOpeningDate()!=null){
//            subDetailForm.txtOpeningDate.setText(dateFormat.format(oppInfo.getOpeningDate()));
           subDetailForm.txtOpeningDate.setText(dateUtils.formatDate(oppInfo.getOpeningDate(),pattern));
       }
       subDetailForm.txtOpeningDate.setCaretPosition(0);
       subDetailForm.txtCompetitionId.setText(oppInfo.getCompetitionId());
       subDetailForm.txtInstructionURL.setText(breakText(oppInfo.getInstructionUrl()));
       subDetailForm.txtInstructionURL.setUrl(oppInfo.getInstructionUrl());
       //subDetailForm.txtInstructionURL.setText(oppInfo.getInstructionUrl());
       //--subDetailForm.txtInstructionURL.setCaretPosition(0);
       //--subDetailForm.txtInstructionURL.setCaretColor(Color.BLACK);
//       subDetailForm.txtInstructionURL.getCaret().setVisible(true);
       //--subDetailForm.txtInstructionURL.setCursor(new java.awt.Cursor(Cursor.TEXT_CURSOR));
       subDetailForm.txtOpportunityId.setText(oppInfo.getOpportunityId());
       //subDetailForm.txtSchemaURL.setText(oppInfo.getSchemaUrl());
       subDetailForm.txtSchemaURL.setText(breakText(oppInfo.getSchemaUrl()));
       subDetailForm.txtSchemaURL.setUrl(oppInfo.getSchemaUrl());
       //--subDetailForm.txtSchemaURL.setCaretPosition(0);
       //--subDetailForm.txtSchemaURL.setCaretColor(Color.BLACK);
//       subDetailForm.txtSchemaURL.getCaret().setVisible(true);
       //--subDetailForm.txtSchemaURL.setCursor(new java.awt.Cursor(Cursor.TEXT_CURSOR));
       subDetailForm.lblProposalNumberVal.setText(oppInfo.getProposalNumber());
       subDetailForm.lblSponsorVal.setText(sponsor);
       
       //Fix for Label Crunching when URL is long - START
       int maxLength = oppInfo.getInstructionUrl().length() > oppInfo.getSchemaUrl().length() ? oppInfo.getInstructionUrl().length() : oppInfo.getSchemaUrl().length();
       if(maxLength > 100) {
           java.awt.Dimension dimension = new java.awt.Dimension(subDetailForm.pnlOpportunity.getWidth() + (maxLength - 100)*5, subDetailForm.pnlOpportunity.getHeight());
           subDetailForm.pnlOpportunity.setPreferredSize(dimension);
           subDetailForm.pnlOpportunity.revalidate();
           subDetailForm.pnlOpportunity.repaint();
       }
       //Fix for Label Crunching when URL is long - END
       
    }
    
    public SubmissionDetailInfoBean getSubmissionData(){
        return subStatusDetails;
    }
    public void setSubmissionData(SubmissionDetailInfoBean subInfo){
        if(subInfo==null) return;
       subDetailForm.txtGgTrackingId.setText(subInfo.getGrantsGovTrackingNumber());
//       subDetailForm.txtLastModDate.setText(Utils.convertNull(subInfo.getStatusDate()));
//       subDetailForm.txtLastModDate.setText(subInfo.getStatusDate()==null?"":dateFormat.format(subInfo.getStatusDate()));
//       subDetailForm.txtStatusRecDate.setText(subInfo.getReceivedDateTime()==null?"":dateFormat.format(subInfo.getReceivedDateTime()));
       subDetailForm.txtLastModDate.setText(subInfo.getStatusDate()==null?"":dateUtils.formatDate(subInfo.getStatusDate(),pattern));
       subDetailForm.txtStatusRecDate.setText(subInfo.getReceivedDateTime()==null?"":dateUtils.formatDate(subInfo.getReceivedDateTime(),pattern));
       subDetailForm.txtSubmStatus.setText(subInfo.getStatus());
       subDetailForm.txtAgTrackingId.setText(subInfo.getAgencyTrackingNumber());
       subDetailForm.txtNotes.setText(subInfo.getComments());
       subDetailForm.txtNotes.setCaretPosition(0);
       ArrayList attachments = subInfo.getAttachments();
        if(attachments!=null && !attachments.isEmpty()){
            Vector synchAttData = new Vector(attachments);
            attTblMdl.setData(synchAttData);
            subDetailForm.tblAttachments.setRowSelectionInterval(0, 0 );
        }
       String existingMsg = subDetailForm.lblMessage.getText();
       subDetailForm.lblMessage.setText(null);
       if(subStatusDetails!=null){
           
           if(subInfo.getAgencyTrackingNumber()!=null){
               subDetailForm.lblMessage.setText(coeusMessageResources.parseMessageKey(
                        "s2ssubdetfrm_exceptionCode.1011"));
           }else{
                subDetailForm.lblMessage.setText(subInfo.getStatus());
           }
       }else{
           subDetailForm.lblMessage.setText("<html>"+existingMsg+
                        "<br>"+ coeusMessageResources.parseMessageKey(
                        "s2ssubdetfrm_exceptionCode.1006")+"</html>");
       }
       subDetailForm.lblMessage.repaint();
       subDetailForm.lblMessage.revalidate();
       subDetailForm.repaint();
       subDetailForm.revalidate();
       subDetailForm.btnSubmit.setEnabled(((Boolean)rightFlags.get(
                    S2SConstants.SUBMIT_TO_SPONSOR)).booleanValue());
//       subDetailForm.btnPrint.setEnabled(false);
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        RequesterBean request = new RequesterBean();
        int[] sltdRows = subDetailForm.tblForms.getSelectedRows();
        if(sltdRows!=null && sltdRows.length==0) return;
        Vector formsList = new Vector(sltdRows.length);
        for(int i=0;i<sltdRows.length;i++){
            FormInfoBean formBean = (FormInfoBean)oppFrmTblMdl.getValueAt(i);
            formsList.addElement(formBean);
        }
        request.setDataObject(formsList);
        request.setFunctionType(S2SConstants.SAVE_OPP_FORMS);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(S2S_SERVLET, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        //if(response.hasResponse()){
//            dlgSubDetailForm.dispose();
//            setSaveNContinue(true);
        //}
    }
    
    public void display(){
        dlgSubDetailForm.setVisible(true);
    }
    
    public void dispose() {
        dlgSubDetailForm.dispose();
    }
    
    private JCheckBox chkInclude;
    private void setColumnData(){
        JTableHeader formsTableHeader = subDetailForm.tblForms.getTableHeader();
        formsTableHeader.setReorderingAllowed(false);
        formsTableHeader.setFont(CoeusFontFactory.getLabelFont());

        JTableHeader attTableHeader = subDetailForm.tblAttachments.getTableHeader();
        attTableHeader.setReorderingAllowed(false);
        attTableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        subDetailForm.tblAttachments.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        subDetailForm.tblAttachments.setRowHeight(22);
        subDetailForm.tblAttachments.setShowHorizontalLines(true);
        subDetailForm.tblAttachments.setShowVerticalLines(true);
        subDetailForm.tblAttachments.setOpaque(true);
        subDetailForm.tblAttachments.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        subDetailForm.tblAttachments.setRowSelectionAllowed(true);

        subDetailForm.tblForms.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        subDetailForm.tblForms.setRowHeight(22);
        subDetailForm.tblForms.setShowHorizontalLines(true);
        subDetailForm.tblForms.setShowVerticalLines(true);
        subDetailForm.tblForms.setOpaque(true);
        subDetailForm.tblForms.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        subDetailForm.tblForms.setRowSelectionAllowed(true);
        
        TableColumn column = subDetailForm.tblAttachments.getColumnModel().getColumn(0);
        column.setPreferredWidth(518);
        column.setResizable(true);

        column = subDetailForm.tblForms.getColumnModel().getColumn(FORM_NAME_COLUMN);
        column.setPreferredWidth(250);
        column.setResizable(true);
        
        column = subDetailForm.tblForms.getColumnModel().getColumn(MANDATORY_COLUMN);
        column.setPreferredWidth(80);
        column.setResizable(true);

        column = subDetailForm.tblForms.getColumnModel().getColumn(INCLUDE_COLUMN);
        column.setPreferredWidth(50);
        column.setResizable(true);
        chkInclude = new JCheckBox();
        chkInclude.setHorizontalAlignment(SwingConstants.CENTER);
        final DefaultCellEditor includeCellEditor = new DefaultCellEditor(chkInclude);
        column.setCellEditor(includeCellEditor);
        chkInclude.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent ie){
                includeCellEditor.stopCellEditing();
            }
        });
        chkInclude.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ie){
                includeCellEditor.stopCellEditing();
            }
        });
        
        
        column = subDetailForm.tblForms.getColumnModel().getColumn(AVAILABLE_COLUMN);
        column.setPreferredWidth(100);
        column.setResizable(true);
    }
    
    /** Specifies the Modal window */
    private void postInitComponents() {

        Component[] pnlOppComponents = {subDetailForm.btnSltOpportunity,
                                        subDetailForm.btnValidate,
                                        subDetailForm.btnSubmit,
                                        subDetailForm.btnRefresh,
                                        subDetailForm.btnPrint,
                                        subDetailForm.btnClose};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( pnlOppComponents );
        subDetailForm.pnlOpportunity.setFocusTraversalPolicy(traversePolicy);
        subDetailForm.pnlOpportunity.setFocusCycleRoot(true);
        
        dlgSubDetailForm = new CoeusDlgWindow(mdiForm);
        dlgSubDetailForm.getContentPane().add(getControlledUI());
        dlgSubDetailForm.setTitle("Grants Gov Submission Details");
        dlgSubDetailForm.setFont(CoeusFontFactory.getLabelFont());
        dlgSubDetailForm.setModal(true);
        dlgSubDetailForm.setResizable(false);
        dlgSubDetailForm.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgSubDetailForm.getSize();
        dlgSubDetailForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgSubDetailForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgSubDetailForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgSubDetailForm.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgSubDetailForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    private void setWindowFocus(){
        subDetailForm.btnClose.requestFocusInWindow();
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        boolean refrRqd = false;
        try{
            dlgSubDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            
            Object source = actionEvent.getSource();
            if(source.equals(subDetailForm.btnClose)){
                performCancelAction();//@todo:remove this line comment
            }else if(source.equals(subDetailForm.btnValidate)){
                refrRqd = true;
                validateGrantsGovData();
            }else if(source.equals(subDetailForm.btnSltOpportunity)){
                String ggTrId = subDetailForm.txtGgTrackingId.getText();
                if(ggTrId!=null && !ggTrId.equals("")){
                    throw new CoeusException(
                        coeusMessageResources.parseMessageKey("s2ssubdetfrm_exceptionCode.1003"));
                }
                refrRqd = false;
                invokeSelectOppForm();
            }else if(source.equals(subDetailForm.btnOppDelete)){
                refrRqd = false;
                String msg = coeusMessageResources.parseMessageKey("s2ssubdetfrm_exceptionCode.1013");
                int confirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                switch(confirm){
                    case(JOptionPane.YES_OPTION):
                        deleteOpportunity();
                        break;
                    case(JOptionPane.NO_OPTION):
                        return;
                }
            }else if(source.equals(subDetailForm.btnSubmit)){
                String ggTrId = subDetailForm.txtGgTrackingId.getText();
                if(ggTrId!=null && !ggTrId.equals("")){
                    throw new CoeusException(
                        coeusMessageResources.parseMessageKey("s2ssubdetfrm_exceptionCode.1003"));
                }
                refrRqd = true;
                submitGrantsGovData();
            }else if(source.equals(subDetailForm.btnRefresh)){
                refreshGrantsGovData();
//                refrRqd = true;
            }else if(source.equals(subDetailForm.btnPrint)){
                refrRqd = true;
                printForms();
            }else if(source.equals(subDetailForm.cmbSubmissionType) || source.equals(subDetailForm.cmbSubmissionEndPoint)) {
                dataModified = true;
            }else if(source.equals(subDetailForm.btnMoreSubStatus)) {
                Object obj = processGrant.getStatusDetail(this.subStatusDetails.getGrantsGovTrackingNumber());
                if(obj!=null)
                    showStatusForm(obj.toString());
            }//Case:2913 - START
            else if(source.equals(subDetailForm.btnSave)) {
                submitGrantsGovData(S2SConstants.SAVE_GRANTS_GOV);
            }
            //Case:2913 - START
        }catch (CoeusException coeusException) {
            refrRqd = true;
            edu.mit.coeus.utils.CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (S2SValidationException  s2sException){
            S2SErrorFormController errContlr = new S2SErrorFormController();
            if(s2sException.getOppSchemaUrl()==null || s2sException.getOppSchemaUrl().equals("")){
                s2sException.setOppSchemaUrl(this.getOpportunityInfoBean().getSchemaUrl());
            }
            if(s2sException.getOppInstrUrl()==null || s2sException.getOppInstrUrl().equals("")){
                s2sException.setOppInstrUrl(this.getOpportunityInfoBean().getInstructionUrl());
            }
            errContlr.setFormData(processGrant.getHeaderParam(),s2sException);
            errContlr.display();
//            edu.mit.coeus.utils.CoeusOptionPane.showErrorDialog(s2sException.getHtmlOutput());
        }catch (Exception  ex){
            ex.printStackTrace();
            edu.mit.coeus.utils.CoeusOptionPane.showErrorDialog(ex.getMessage());
        }finally{
            dlgSubDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            try{
                if(refrRqd) processGrant.refreshForm();
            }catch(Exception ex){
                ex.printStackTrace();
                CoeusOptionPane.showInfoDialog(ex.getMessage());
                performCancelAction();
            }
        }
    }
    private void deleteOpportunity() throws Exception{
        processGrant.deleteOpportunity();
    }

    private void invokeSelectOppForm() throws Exception{
        processGrant.showS2SOppForm();
    }
    public void performCancelAction(){
        if(subStatusDetails!=null && subStatusDetails.getGrantsGovTrackingNumber()!=null && !subStatusDetails.getGrantsGovTrackingNumber().trim().equals("")) {
            //Nothing would have been modified.
            dlgSubDetailForm.dispose();
        }
        
        if(!editable) {
            dlgSubDetailForm.dispose();
        }
        
        if(dataModified) {
            try{
                int selection = CoeusOptionPane.showQuestionDialog("Do you want to save changes?", CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_YES) {
                    submitGrantsGovData(S2SConstants.SAVE_GRANTS_GOV);
                    dlgSubDetailForm.dispose();
                }else if(selection == CoeusOptionPane.SELECTION_NO) {
                    dlgSubDetailForm.dispose();
                }
            }catch (Exception exception) {
                CoeusOptionPane.showErrorDialog(exception.getMessage());
            }
        }//End If
        else{
            dlgSubDetailForm.dispose();
        }
    }
    private void validateGrantsGovData()  throws Exception{
        validateGrantsGovData(null);
    }

    private void validateGrantsGovData(Vector data)  throws Exception{
        validate();
        Vector vector = new Vector();
        //Updated for calling validate with print data
        vector.add(data==null?oppFrmTblMdl.getData():data);
        vector.add(buildOpportunityInfoBean());
        if(processGrant.validateApplication(vector))
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("s2ssubdetfrm_exceptionCode.1004"));
    }

    private void submitGrantsGovData(char saveType) throws Exception{
        validate();
        Vector vector = new Vector();
        vector.add(oppFrmTblMdl.getData());
        
        
        vector.add(buildOpportunityInfoBean());
        
        boolean saved = false;
        if(saveType == S2SConstants.SAVE_FORMS_N_SUBMIT_APP) {
            saved = processGrant.submitGrantsGov(vector);
        }else if(saveType == S2SConstants.SAVE_GRANTS_GOV){
            saved = processGrant.saveSubmissionDetails( S2SConstants.SAVE_GRANTS_GOV, vector);
        }
        
        if(saved && saveType == S2SConstants.SAVE_FORMS_N_SUBMIT_APP){
//            processGrant.refreshForm();
            subDetailForm.tbdPnSubmission.setSelectedIndex(2);
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("s2ssubdetfrm_exceptionCode.1005"));
        }
        dataModified = false;
    }
    
    private OpportunityInfoBean buildOpportunityInfoBean() {
        //modification for new columns in OSP$S2S_OPPORTUNITY - S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - 5 - START
        int selectedIndex = subDetailForm.cmbSubmissionType.getSelectedIndex();
        ComboBoxBean comboBoxBean = (ComboBoxBean)lstSubmissionTypeCode.get(selectedIndex);
        opportunityInfoBean.setSubmissionTypeCode(Integer.parseInt(comboBoxBean.getCode()));
        
        int selectedEndPointIndex = subDetailForm.cmbSubmissionEndPoint.getSelectedIndex();
        ComboBoxBean endPointComboBoxBean = (ComboBoxBean)lstSubmisionEndPoints.get(selectedEndPointIndex);
        opportunityInfoBean.setSubmissionEndPoint(endPointComboBoxBean.getCode());

        String revisionType = "";
        String revisionOtherDescription = "";
        
        if(subDetailForm.rdBtnOther.isSelected()) {
            revisionType = revisionType + OTHER;
            revisionOtherDescription = subDetailForm.txtOther.getText();
        }else{
            if(subDetailForm.rdBtnIncreaseAward.isSelected()){
                revisionType = revisionType + INC_AWARD;
            }else if(subDetailForm.rdBtnDecreaseAward.isSelected()) {
                revisionType = revisionType + DEC_AWARD;
            }
            
            if(subDetailForm.rdBtnIncreaseDuration.isSelected()){
                revisionType = revisionType + INC_DURATION;
            }else if(subDetailForm.rdBtnDecreaseDuration.isSelected()) {
                revisionType = revisionType + DEC_DURATION;
            }
        }
        
        //If revision is empty set to null
        revisionType = revisionType.trim().length() == 0 ? null : revisionType;
        revisionOtherDescription = revisionOtherDescription.trim().length() == 0 ? null : revisionOtherDescription;
        
        opportunityInfoBean.setRevisionCode(revisionType);
        opportunityInfoBean.setRevisionOtherDescription(revisionOtherDescription);
        //modification for new columns in OSP$S2S_OPPORTUNITY - S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - 5 - END
        
        return opportunityInfoBean;
    }
    
    private void submitGrantsGovData()throws Exception{
        submitGrantsGovData(S2SConstants.SAVE_FORMS_N_SUBMIT_APP);
    }
    
    private void refreshGrantsGovData() throws Exception{
        availableFormsNo = 0;
        optIncludedFormsNo = 0;
        //Commented by Geo on 3/22/2007
        //Do not check grants id before refreshing, check only status is null or not
//        if(subStatusDetails!=null && 
//            subStatusDetails.getGrantsGovTrackingNumber()!=null && 
//                !subStatusDetails.getGrantsGovTrackingNumber().equals("") && 
//                processGrant.refreshGrantsGovData()){
        if(subStatusDetails!=null && subStatusDetails.getStatus()!=null && 
                !subStatusDetails.getStatus().trim().equals("")
                && processGrant.refreshGrantsGovData()){
            subDetailForm.tbdPnSubmission.setSelectedIndex(2);
        }
    }
    private void printForms() throws Exception{
        int[] sltdRows = subDetailForm.tblForms.getSelectedRows();
        if(sltdRows.length==0) 
            throw new CoeusException(coeusMessageResources.parseMessageKey(
                        "s2ssubdetfrm_exceptionCode.1007"));
        Vector sltdFrmList = new Vector(sltdRows.length);
        for(int frmIndex=0;frmIndex<sltdRows.length;frmIndex++){
            FormInfoBean frmInfo = (FormInfoBean)oppFrmTblMdl.getValueAt(sltdRows[frmIndex]);
            if(!frmInfo.isAvailable())
                throw new CoeusException(coeusMessageResources.parseMessageKey(
                        "s2ssubdetfrm_exceptionCode.1007"));
           	sltdFrmList.add(oppFrmTblMdl.getValueAt(sltdRows[frmIndex]));
        }
//        validateGrantsGovData();
        String fileName = processGrant.printForms(sltdFrmList);
        if(fileName!=null){
        		URLOpener.openUrl(fileName);
        }
    }
    /**
     * Getter for property saveNContinue.
     * @return Value of property saveNContinue.
     */
    public boolean isSaveNContinue() {
        return saveNContinue;
    }
    
    /**
     * Setter for property saveNContinue.
     * @param saveNContinue New value of property saveNContinue.
     */
    public void setSaveNContinue(boolean saveNContinue) {
        this.saveNContinue = saveNContinue;
    }
    
    /**
     * Getter for property submissionTitle.
     * @return Value of property submissionTitle.
     */
    public java.lang.String getSubmissionTitle() {
        return submissionTitle;
    }
    
    /**
     * Setter for property submissionTitle.
     * @param submissionTitle New value of property submissionTitle.
     */
    public void setSubmissionTitle(java.lang.String submissionTitle) {
        this.submissionTitle = submissionTitle;
    }
    
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        ListSelectionModel source = (ListSelectionModel)listSelectionEvent.getSource();
        int selRow = subDetailForm.tblForms.getSelectedRow();
    }
    
    /**
     * Getter for property opportunityInfoBean.
     * @return Value of property opportunityInfoBean.
     */
    public edu.mit.coeus.s2s.bean.OpportunityInfoBean getOpportunityInfoBean() {
        return opportunityInfoBean;
    }
    
    /**
     * Setter for property opportunityInfoBean.
     * @param opportunityInfoBean New value of property opportunityInfoBean.
     */
    public void setOpportunityInfoBean(edu.mit.coeus.s2s.bean.OpportunityInfoBean opportunityInfoBean) {
        this.opportunityInfoBean = opportunityInfoBean;
    }
    
    /**
     * Getter for property opportunityFormDetails.
     * @return Value of property opportunityFormDetails.
     */
    public java.util.Vector getOpportunityFormDetails() {
        return opportunityFormDetails;
    }
    
    /**
     * Setter for property opportunityFormDetails.
     * @param opportunityFormDetails New value of property opportunityFormDetails.
     */
    public void setOpportunityFormDetails(java.util.Vector opportunityFormDetails) {
        this.opportunityFormDetails = opportunityFormDetails;
    }
    
    /**
     * Getter for property submissionDetails.
     * @return Value of property submissionDetails.
     */
    public java.util.Vector getSubmissionDetails() {
        return submissionDetails;
    }
    
    /**
     * Setter for property submissionDetails.
     * @param submissionDetails New value of property submissionDetails.
     */
    public void setSubmissionDetails(java.util.Vector submissionDetails) {
        this.submissionDetails = submissionDetails;
    }
    
    private void submitGrantsGov() throws Exception {
        HashMap params = new HashMap();
        params.put("PROPOSAL_NUMBER", submissionTitle);
        processGrant.submitGrantsGov(params);
    }
    
    private void validateApplication() throws Exception {
        HashMap params = new HashMap();
        params.put("PROPOSAL_NUMBER", submissionTitle);
        processGrant.validateApplication(params);
    }
    
    /**
     * Getter for property sponsor.
     * @return Value of property sponsor.
     */
    public java.lang.String getSponsor() {
        return sponsor;
    }
    
    /**
     * Setter for property sponsor.
     * @param sponsor New value of property sponsor.
     */
    public void setSponsor(java.lang.String sponsor) {
        this.sponsor = sponsor;
    }
    
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if(source.equals(subDetailForm.txtSchemaURL)||
            source.equals(subDetailForm.txtInstructionURL)){
            if(e.getClickCount()==2){
                System.out.println("url is"+((JTextField)source).getText());
                try{
                    URLOpener.openUrl(((JTextField)source).getText());
                }catch (Exception ex){
                    ex.printStackTrace();
                    CoeusOptionPane.showInfoDialog(ex.getMessage());
                }
            }
        }else if(source.equals(subDetailForm.txtSubmStatus)){
            
            if(e.getClickCount()==2){
                //call the GetStatusDetail web service
                
                try{
                    dlgSubDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    Object obj = processGrant.getStatusDetail(this.subStatusDetails.getGrantsGovTrackingNumber());
                    if(obj!=null)
                        showStatusForm(obj.toString());
                }catch(Exception ex){
                    ex.printStackTrace();
                    CoeusOptionPane.showErrorDialog(ex.getMessage());
                }finally{
                    dlgSubDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                }
            }
        }
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    private CoeusDlgWindow dlgSubStatusForm;
    private void showStatusForm(String str) throws CoeusException{
            dlgSubStatusForm = new CoeusDlgWindow(dlgSubDetailForm);
            S2SStatusDetailForm statDetFrm = new S2SStatusDetailForm();
            //        statDetFrm.trS2SStatus.setModel(new XMLTreeModel(Converter.string2Dom(str)));
            //        statDetFrm.trS2SStatus.setCellRenderer(new XMLTreeCellRenderer());
            String msg = str;
            if(str.startsWith("<?xml ")){
                XPathExecutor x = new XPathExecutor(str);
                msg = x.execute("Errors/ProcessingError/INFO");
            }
            if(str!=null && str.length()>0){
                statDetFrm.txtArStatDetail.setText(msg);
            }else{
                statDetFrm.txtArStatDetail.setText(str);
            }
            
            dlgSubStatusForm.getContentPane().add(statDetFrm);
            dlgSubStatusForm.setTitle("Submission Status Detail");
            dlgSubStatusForm.setFont(CoeusFontFactory.getLabelFont());
            dlgSubStatusForm.setModal(true);
            //        dlgSubStatusForm.setSize(300,300);
            //        dlgSubStatusForm.setResizable(false);
            dlgSubStatusForm.pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dlgSize = dlgSubStatusForm.getSize();
            dlgSubStatusForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
            screenSize.height/2 - (dlgSize.height/2));
            
            dlgSubStatusForm.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    dlgSubStatusForm.dispose();
                    return;
                }
            });
            dlgSubDetailForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
            dlgSubDetailForm.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    dlgSubStatusForm.dispose();
                    return;
                }
            });
            statDetFrm.btnOk.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dlgSubStatusForm.dispose();
                }
            });
            dlgSubStatusForm.setVisible(true);
    }
    /**
     * Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /**
     * Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    //modification for new columns in OSP$S2S_OPPORTUNITY - S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - 6 - START
    public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getStateChange() == ItemEvent.DESELECTED) return;
        
        if(itemEvent.getSource().equals(subDetailForm.rdBtnOther)) {
            subDetailForm.txtOther.setVisible(subDetailForm.rdBtnOther.isSelected());
            //deselect other radio buttons
            subDetailForm.rdBtnAward.setSelected(true);
            subDetailForm.rdBtnDuration.setSelected(true);
        }else{
            subDetailForm.rdBtnOther.setSelected(false);
            subDetailForm.txtOther.setText("");
            subDetailForm.txtOther.setVisible(false);
        }
        dataModified = true;
    }
    //modification for new columns in OSP$S2S_OPPORTUNITY - S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - 6 - END
    
    private String breakText(String text) {
        if (text != null && text.length() > 75) {
            StringBuffer instructionURL = new StringBuffer();
            int totalLength = text.length();
            int cutOff = 75;
            int loopCount = totalLength / cutOff;
            for (int index = 0; index < loopCount; index++) {
                instructionURL = instructionURL.append(text.substring(cutOff * index, (cutOff * index) + 75));
                if (index > 0 || loopCount == 1 || totalLength % cutOff > 0) {
                    instructionURL = instructionURL.append("<br>");
                }
            }
            //remaining
            instructionURL = instructionURL.append(text.substring(instructionURL.length()- (4*loopCount), totalLength));
            return instructionURL.toString();
        } else {
            return text;
        }
    }
    
    public class OppFormsTableModel extends AbstractTableModel{
        private String[] colName = {FORM_NAME, MANDATORY, INCLUDE, EMPTY_STRING};
        private Class[] colClass = {String.class, Boolean.class, Boolean.class, String.class};
        private Vector formsList;
            /*For formating the date*/
        private static final String DATE_FORMAT = "dd-MMM-yyyy";
        private static final String DATE_SEPARATERS = ":/.,|-";
        
        private boolean editable = false;
        
        boolean[] canEdit = new boolean [] {
            false, false, true, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if(subStatusDetails!=null && subStatusDetails.getGrantsGovTrackingNumber()!=null && !subStatusDetails.getGrantsGovTrackingNumber().trim().equals("")) {
                return false;
            }
            FormInfoBean formInfo = (FormInfoBean)getValueAt(rowIndex);
            if((columnIndex==INCLUDE_COLUMN) && ( !formInfo.isAvailable() || formInfo.isMandatory()))
                return false;
            else if(!editable)
                return false;
            else
                return canEdit [columnIndex];
        }

        public int getColumnCount() {
            return colName.length;
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public void setData(Vector data){
            this.formsList = data;
            fireTableDataChanged();
        }
        public Vector getData(){
//            Vector includedForms = new Vector();
//            for(int i=0;i<formsList.size();i++){
//                FormInfoBean frm = (FormInfoBean)getValueAt(i);
//                if(frm.isInclude())
//                    includedForms.add(frm);
//            }
//            return includedForms;
            return formsList;
        }
        
        public int getRowCount() {
            if(formsList==null){
                return 0;
            }else{
                return formsList.size();
            }
        }
        
        public Object getValueAt(int row) {
            if(getRowCount()==0) return null;
            return formsList.elementAt(row);
        }

        public void setValueAt(Object obj,int row,int col) {
            switch (col) {
                case(INCLUDE_COLUMN):
                    FormInfoBean formInfo = (FormInfoBean)getValueAt(row);
                    formInfo.setInclude(((Boolean)obj).booleanValue());
                    optIncludedFormsNo++;
                    dataModified = true;
                    break;
            }
        }
        public Object getValueAt(int row,int col) {
            FormInfoBean formInfo = (FormInfoBean)getValueAt(row);
//            formInfo.setAcType('\u0000');
            formInfo.setProposalNumber(getSubmissionTitle());
            formInfo.setUpdateUser(CoeusGuiConstants.getMDIForm().getUserId());
            if(formInfo.getAcType()=='\u0000') formInfo.setAcType('I');
            DateUtils dateUtil = new DateUtils();
            switch(col){
                case(FORM_NAME_COLUMN):
                    return formInfo.getFormName();
                case(MANDATORY_COLUMN):
                    return new Boolean(formInfo.isMandatory());
                case(INCLUDE_COLUMN):
//                    if(chkInclude!=null) return new Boolean(chkInclude.isSelected());
                    return new Boolean(formInfo.isInclude());
                case(AVAILABLE_COLUMN):
                    return formInfo.isAvailable()?"Available":"Not Available";
            }
            return EMPTY_STRING;
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }
    }// end of table model class
    public class SubAttachmentsTableModel extends AbstractTableModel{
        private static final String ATTACHMENT_NAME = "Attachments";
        private static final int ATTACHMENT_COLUMN = 0;
        
        private String[] colName = {ATTACHMENT_NAME};
        private Class[] colClass = {String.class};
        private Vector attList;
            /*For formating the date*/
        private static final String DATE_FORMAT = "dd-MMM-yyyy";
        private static final String DATE_SEPARATERS = ":/.,|-";

        public boolean isCellEditable(int rowIndex, int columnIndex) {
           return false;
        }

        public int getColumnCount() {
            return colName.length;
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public void setData(Vector data){
            this.attList = data;
            fireTableDataChanged();
        }
        public Vector getData(){
            return attList;
        }
        
        public int getRowCount() {
            if(attList==null){
                return 0;
            }else{
                return attList.size();
            }
        }
        
        public Object getValueAt(int row) {
            if(getRowCount()==0) return null;
            return attList.elementAt(row);
        }

        public Object getValueAt(int row,int col) {
            Attachment attachement = (Attachment)getValueAt(row);
            DateUtils dateUtil = new DateUtils();
            switch(col){
                case(ATTACHMENT_COLUMN):
                    return attachement.getContentId();
            }
            return EMPTY_STRING;
        }
    }// end of table model class

    public char getMode() {
        return mode;
    }

    public void setMode(char mode) {
        this.mode = mode;
    }
}
