/**
 * @(#)ProtocolActionsForm.java  1.0 September 30, 2002
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.irb.gui;

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.irb.bean.ProtocolActionDocumentBean;
import edu.mit.coeus.irb.controller.ProtocolMailController;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.controller.QuestionAnswersController;
import edu.mit.coeus.questionnaire.gui.QuestionAnswersForm;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.irb.bean.ProtocolActionsBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.irb.bean.ProtocolActionChangesBean;
import edu.mit.coeus.irb.bean.SubmissionDetailsBean ;
import edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean;
import java.io.ByteArrayInputStream;
import java.text.ParseException;

/** <code> ProtocolActionsForm </code> is a form object which display
 * all the protocol action details. This class will be instantiated from <CODE>ProtocolDetailForm</CODE>.
 *
 * @author kprasad
 * @version: 1.0 September 30, 2002
 * @author Raghunath P.V
 * @version: 1.1 October 24, 2002
 */

public class ProtocolActionsForm extends javax.swing.JComponent implements ProtocolActionsInterface {
    
     /** Used to hold a ProtocolActionsBean Data */
     private ProtocolActionsBean protocolActionBean;
     /** Used to identify the function type I for Add, D for Display
        And U for Modify */
     private char functionType;
     /** Reference to hold MDIForm */
     private CoeusAppletMDIForm parent;
     /** Used to place JTable */
     private JScrollPane scrPnPane1;
     /** Used to hold table data */
     private JTable tblActionForm;
     /** Vector to hold vector of beans */
     private Vector commActionData =null;
	 
     
//     private DefaultTableModel actionTableModel ;
//     private Vector vecColumnNamesforModel ;
     private String protocolId = null ;
     
     //prps start
    private final String SUBMISSION_DETAILS_SERVLET = "/SubmissionDetailsServlet" ;
    //prps end
    
    //Added By sharath for PDF preview - modified the column numbers(Comments column to 7 from 4) by Jobin
    private ProtocolActionTableModel protocolActionTableModel;
    //Modified for case#3046 - Notify IRB attachments - start
    //Code commented and modified for Case#3554 - Notify IRB enhancement- starts
    //To remove the view document column
//    private static final int DOC_BUTTON_COLUMN = 0;
//    private static final int BUTTON_COLUMN = 1;    
//    private static final int DESCRIPTION_COLUMN = 2;
//    private static final int DATE_COLUMN = 3;
//    private static final int ACTION_DATE_COLUMN = 4;    
//    private static final int SCHEDULE_ID_COLUMN = 5;
//    private static final int SEQUENCE_NUMBER_COLUMN = 6;
//    private static final int SUBMISSION_NUMBNER_COLUMN = 7;
//    private static final int COMMENTS_COLUMN = 8;
//    private static final int COMMENTS_ICON_COLUMN = 9;
    //Modified for case#3046 - Notify IRB attachments - end
//    //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//    private static final int BUTTON_COLUMN = 0;    
//    private static final int DESCRIPTION_COLUMN = 1;
//    private static final int DATE_COLUMN = 2;
//    private static final int ACTION_DATE_COLUMN = 3;    
//    private static final int SCHEDULE_ID_COLUMN = 4;
//    private static final int SEQUENCE_NUMBER_COLUMN = 5;
//    private static final int SUBMISSION_NUMBNER_COLUMN = 6;
//    private static final int COMMENTS_COLUMN = 7;
//    private static final int COMMENTS_ICON_COLUMN = 8;
    private static final int QUESTIONNAIRE_BUTTON_COLUMN = 0;
    private static final int BUTTON_COLUMN = 1;
    private static final int DESCRIPTION_COLUMN = 2;
    private static final int DATE_COLUMN = 3;
    private static final int ACTION_DATE_COLUMN = 4;
    private static final int SCHEDULE_ID_COLUMN = 5;
    private static final int SEQUENCE_NUMBER_COLUMN = 6;
    private static final int SUBMISSION_NUMBNER_COLUMN = 7;
    private static final int COMMENTS_COLUMN = 8;
    private static final int COMMENTS_ICON_COLUMN = 9;
    private ImageIcon imgQuestionnaire;
    private CoeusDlgWindow dlgProtoSubQuestionnaire;
    private QuestionAnswersForm questionAnswersForm;
    private static final int PROTOCOL_MODULE = 7;
    private static final int PROTOCOL_SUBMISSION_SUB_MODULE = 2;
    //Modified for COEUSDEV-86 : Questionnaire for a Submission - END
    //Code commented and modified for Case#3554 - Notify IRB enhancement- ends
    private DateUtils dtUtils = new DateUtils();
	
    private ImageIcon imgIcnComments;
    private ImageIcon imgIcnPDF;
    private final String NO_DOCUMENT = "No Document Available";
    private static final int ROW_HEIGHT = 20;
	
	// For the action window -- Added by Jobin - start
	private boolean canAction;
	private Vector vecActions = new Vector(); 
	private Vector vDataObjects = new Vector();// Added by Jobin - end
	
    //Added By sharath for PDF preview
    
    //Protocol Enhancment -  Administrative Correction Start 1    
    private boolean saveRequired = false;
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private Vector vcDataPopulate;
    //Protocol Enhancment -  Administrative Correction End 1
    
    //Case #2080 Start 1
    private BaseWindowObservable observable  = new BaseWindowObservable();
    private boolean openedFormSubWdw;
    //Case #2080 End 1
    
     /** Creates a new <CODE>ProtocolActionsForm</CODE> form object. <p>
      * <I>Default Constructor</I>
      */
    //Added for case #1961 - Start - 1
    private final String PROTOCOL_ACTION_SERVLET = "/protocolActionServlet";
    //Added for case #1961 - End - 1
    //Added for case#3046 - Notify IRB attachments
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    private static final String NO_QUESTIONNAIRE_FOR_SUBMISSION = "protoSubquestionnaire_exceptionCode.1011";
    private static final int NOTIFY_IRB =116;
    private static final int REQUEST_TO_CLOSE_ACTION = 105;
    private static final int REQUEST_FOR_SUSPENSION_ACTION = 106;
    private static final int REQUEST_TO_CLOSE_ENROLLMENT_ACTION = 108;
    private static final int REQUEST_FOR_DATA_ANALYSIS_ONLY_ACTION = 114;
    private static final int REQUEST_FOR_RE_OPEN_ENROLLMENT_ACTION= 115;
    //COEUSDEV-86 : End
    //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
    private boolean actionDateValidationFired;
    //COEUSQA:3187 - End
    public ProtocolActionsForm() {
    }
    
    /** Constructor that instantiate <CODE>ProtocolActionsForm</CODE> and populate the components
     * with the specified data.
     * @param functionType is a <CODE>Char</CODE> variable that specify the mode, in which the
     * form object is to be displayed.
     * <B>'A'</B> specify that the form is in Add Mode
     * <B>'M'</B> specify that the form is in Modify Mode
     * <B>'D'</B> specify that the form is in Display Mode
     * @param keyActionData is a Vector consist of all the <CODE>ProtocolActionsBean</CODE>
     */
    
    public ProtocolActionsForm(char functionType, 
                java.util.Vector keyActionData) {
                    
        commActionData = new Vector();    
        this.commActionData = keyActionData;
		
		this.functionType = functionType;
    }
    
    /** This method is used to initialize the components with the specified data.
     * This method will be invoked from <CODE>ProtocolDetailForm</CODE>.
     * @param parent is a CoeusAppletMDIForm reference.
     * @return a JComponent containing all the form components.
     */
    
    public JComponent showProtocolActionsForm(CoeusAppletMDIForm parent){
        
        this.parent = parent;
        //Case #2080 Start 2
        tblActionForm = new JTable();
        //Case #2080 End 2
                
        //Added By sharath for PDF preview
        postInitComponents();
        setFormData();
        tblActionForm.getTableHeader().setFont(CoeusFontFactory.getLabelFont());

        //Case #2080 Start 3
        
        //Added by Amit 11/18/2003
        /*if(functionType == CoeusGuiConstants.DISPLAY_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
            tblActionForm.setBackground(bgListColor);    
        }else{
            
             tblActionForm.setBackground(Color.white);            
        }*/
        //end Amit 
        
        //Case #2080 End 3
        
        //Added By sharath for PDF preview
        return this;
    }
	// Added by Jobin - start
	 /**
	  * For setting up the values which has to be displayed in the submission dialog window
	  */
	public void showActions(Vector vAction){
		vecActions = vAction;
		canAction = true;
		
	}// Added by Jobin - end
	
	//prps start
    // this method will get the submission details data from the servlet
    //private SubmissionDetailsBean getSubmissionDetails(SubmissionDetailsBean detailsBean)//Commented by sharath
    private Vector getSubmissionDetails(SubmissionDetailsBean detailsBean)
    {
        Vector vecDetails= new Vector() ;
        try
       {    // send request
          System.out.println("*** Sending ***") ;
          
          RequesterBean requester = new RequesterBean();
          requester.setFunctionType('G') ;
          requester.setDataObject(detailsBean) ;
         
          String connectTo =CoeusGuiConstants.CONNECTION_URL
            + SUBMISSION_DETAILS_SERVLET ;
            AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo,requester);
            comm.send();

            ResponderBean responder = comm.getResponse();
            if (responder.isSuccessfulResponse())
            {
                vDataObjects = (Vector)responder.getDataObject();//Added by sharath
                vecDetails = (Vector)vDataObjects.get(0);

            }
          
        }catch(Exception ex){
            System.out.println("  *** Error in getting submission details ***  ") ; 
            ex.printStackTrace() ;
       }
        
       //return detailsBean ; //Commented By sharath
        return vecDetails;
    }
    
    //prps end
    
    private void postInitComponents() {
       
        java.awt.GridBagConstraints gridBagConstraints;
        scrPnPane1 = new javax.swing.JScrollPane();
        GridBagLayout gridBag = new java.awt.GridBagLayout() ;
        setLayout(gridBag );
       
        DefaultTableColumnModel colModel = new DefaultTableColumnModel() ;
//        actionTableModel= new DefaultTableModel() ; 
        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
        imgQuestionnaire = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.QUESTION_PROP_HIE_ICON));
        //Added for COEUSDEV-86 : Questionnaire for a Submission - End
        
        imgIcnPDF = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
        imgIcnComments = new ImageIcon(getClass().getClassLoader().getResource(
                            CoeusGuiConstants.JUSTIFIED));
        protocolActionTableModel = new ProtocolActionTableModel();
        
        //Case #2080 Start 4
        //tblActionForm = new javax.swing.JTable(protocolActionTableModel);
        //Case #2080 End 4
        
        tblActionForm.setRowSelectionAllowed(true);
        tblActionForm.setModel(protocolActionTableModel);
        tblActionForm.setRowHeight(ROW_HEIGHT);
        tblActionForm.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ProtocolActionCellRenderer buttonRenderer = new ProtocolActionCellRenderer();
        EmptyHeaderRenderer emptyHeaderRenderer = new EmptyHeaderRenderer();
        ProtocolActionCellEditor cellEditor = new ProtocolActionCellEditor();
        //Modified/Added for case#3046 - Notify IRB attachments - start
        //Code commented and modified for Case#3554 - Notify IRB enhancement- starts
        //To remove the view document column
//        tblActionForm.getColumnModel().getColumn(0).setCellRenderer(buttonRenderer);
//        tblActionForm.getColumnModel().getColumn(0).setCellEditor(cellEditor);
//        tblActionForm.getColumnModel().getColumn(0).setHeaderRenderer(emptyHeaderRenderer);
//        tblActionForm.getColumnModel().getColumn(1).setCellRenderer(buttonRenderer);
//        tblActionForm.getColumnModel().getColumn(1).setCellEditor(cellEditor);
//        tblActionForm.getColumnModel().getColumn(1).setHeaderRenderer(emptyHeaderRenderer);
        //Modified/Added for case#3046 - Notify IRB attachments - end
        //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//        tblActionForm.getColumnModel().getColumn(BUTTON_COLUMN).setCellRenderer(buttonRenderer);
//        tblActionForm.getColumnModel().getColumn(BUTTON_COLUMN).setCellEditor(cellEditor);
//        tblActionForm.getColumnModel().getColumn(BUTTON_COLUMN).setHeaderRenderer(emptyHeaderRenderer);
        TableColumn correspondenceColumn = tblActionForm.getColumnModel().getColumn(BUTTON_COLUMN);
        correspondenceColumn.setCellRenderer(buttonRenderer);
        correspondenceColumn.setCellEditor(cellEditor);
        correspondenceColumn.setHeaderRenderer(emptyHeaderRenderer);
        correspondenceColumn.setMaxWidth(20);
        correspondenceColumn.setMinWidth(20);
        correspondenceColumn.setPreferredWidth(20);
        TableColumn questionnaireButonColumn = tblActionForm.getColumnModel().getColumn(QUESTIONNAIRE_BUTTON_COLUMN);
        questionnaireButonColumn.setCellRenderer(buttonRenderer);
        questionnaireButonColumn.setCellEditor(cellEditor);
        questionnaireButonColumn.setHeaderRenderer(emptyHeaderRenderer);
        questionnaireButonColumn.setMaxWidth(20);
        questionnaireButonColumn.setMinWidth(20);
        questionnaireButonColumn.setPreferredWidth(20);
        //Code commented and modified for Case#3554 - Notify IRB enhancement- ends
        //Enhancement Case # 2080-start 5
        tblActionForm.setOpaque(false);
        //Enhancement Case # 2080 - End 5

        TableColumn column = tblActionForm.getColumnModel().getColumn(COMMENTS_ICON_COLUMN);
        column.setCellRenderer(buttonRenderer);
        column.setCellEditor(cellEditor);
        column.setHeaderRenderer(emptyHeaderRenderer);
        column.setMaxWidth(20);
        column.setMinWidth(20);
        column.setPreferredWidth(20);
        // setting up the column widths if it is displaying in the submission details dialog window -- Modified by Jobin - start
		if (canAction) {
                        //Modified/Added for case#3046 - Notify IRB attachments
                        //Code commented and modified for Case#3554 - Notify IRB enhancement
                        //To remove the view document column
//			int colWidth[] = {20, 20, 180, 95, 95};            
			int colWidth[] = {20, 180, 95, 95};
			//tblActionForm.getColumnModel().getColumn(0).setMaxWidth(colWidth[0]);
			for(int col = 0; col < colWidth.length; col++) {
				tblActionForm.getColumnModel().getColumn(col).setMinWidth(colWidth[col]);
				tblActionForm.getColumnModel().getColumn(col).setPreferredWidth(colWidth[col]);
                                
                                //Case #2080 Start 6
                                //tblActionForm.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
                                
                                tblActionForm.getColumnModel().getColumn(col).setCellEditor(cellEditor);
                                tblActionForm.getColumnModel().getColumn(col).setCellRenderer(buttonRenderer);
				//Case #2080 End 6
			}
                        //Modified for case#3046 - Notify IRB attachments - start
                        //Code modified for Case#3554 - Notify IRB enhancement- starts
                        //Hardcoded column number modified.
			tblActionForm.getColumnModel().getColumn(SCHEDULE_ID_COLUMN).setMaxWidth(0);
			tblActionForm.getColumnModel().getColumn(SCHEDULE_ID_COLUMN).setMinWidth(0);
			tblActionForm.getColumnModel().getColumn(SCHEDULE_ID_COLUMN).setPreferredWidth(0);
			tblActionForm.getColumnModel().getColumn(SEQUENCE_NUMBER_COLUMN).setMaxWidth(0);
			tblActionForm.getColumnModel().getColumn(SEQUENCE_NUMBER_COLUMN).setMinWidth(0);
			tblActionForm.getColumnModel().getColumn(SEQUENCE_NUMBER_COLUMN).setPreferredWidth(0);
			tblActionForm.getColumnModel().getColumn(SUBMISSION_NUMBNER_COLUMN).setMaxWidth(0);
			tblActionForm.getColumnModel().getColumn(SUBMISSION_NUMBNER_COLUMN).setMinWidth(0);
			tblActionForm.getColumnModel().getColumn(SUBMISSION_NUMBNER_COLUMN).setPreferredWidth(0);
			tblActionForm.getColumnModel().getColumn(COMMENTS_COLUMN).setMinWidth(160);
			tblActionForm.getColumnModel().getColumn(COMMENTS_COLUMN).setPreferredWidth(160);
			//Code modified for Case#3554 - Notify IRB enhancement- ends
                        //Case #2080 Start 7
                        //Code modified for Case#3554 - Notify IRB enhancement
                        //Hardcoded column number modified.
                        tblActionForm.getColumnModel().getColumn(COMMENTS_ICON_COLUMN).setCellRenderer(buttonRenderer);
                        //Case #2080 End 7
                        //Modified for case#3046 - Notify IRB attachments - end
			scrPnPane1.setMinimumSize(new Dimension(592, 77)) ;
			scrPnPane1.setPreferredSize(new Dimension(592, 77)) ;
			
			//scrPnPane1.setPreferredSize(new Dimension(470, 220)) ;
//			scrPnPane1.setMaximumSize(new Dimension(470, 220)) ;
//			scrPnPane1.setMinimumSize(new Dimension(470, 220)) ;
			
//			tblActionForm.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		} else {
                        //Modified for case#3046 - Notify IRB attachments
                        //Code commented and modified for Case#3554 - Notify IRB enhancement- starts
                        //To remove the view document column
//                        int colWidths[] = {20, 20, 350, 130, 100, 400};
			int colWidths[] = {20, 350, 130, 100, 400};
			tblActionForm.getColumnModel().getColumn(0).setMaxWidth(colWidths[0]);
			for(int col = 0; col < colWidths.length; col++) {
				tblActionForm.getColumnModel().getColumn(col).setMinWidth(colWidths[col]);
				tblActionForm.getColumnModel().getColumn(col).setPreferredWidth(colWidths[col]);
                                
                                //Case #2080 Start 13
                                tblActionForm.getColumnModel().getColumn(col).setCellEditor(cellEditor);
                                tblActionForm.getColumnModel().getColumn(col).setCellRenderer(buttonRenderer);
                                //Case #2080 End 13
                                
			}
                        //Modified for case#3046 - Notify IRB attachments - start
                        //Code modified for Case#3554 - Notify IRB enhancement- starts
                        //Hardcoded column number modified.
			tblActionForm.getColumnModel().getColumn(COMMENTS_COLUMN).setMaxWidth(320);
			tblActionForm.getColumnModel().getColumn(COMMENTS_COLUMN).setMinWidth(320);
			tblActionForm.getColumnModel().getColumn(COMMENTS_COLUMN).setPreferredWidth(320);
                        //Code modified for Case#3554 - Notify IRB enhancement- ends
                        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
                        tblActionForm.getColumnModel().getColumn(ACTION_DATE_COLUMN).setMaxWidth(280);
			tblActionForm.getColumnModel().getColumn(ACTION_DATE_COLUMN).setMinWidth(280);
			tblActionForm.getColumnModel().getColumn(ACTION_DATE_COLUMN).setPreferredWidth(280);
                        //Added for COEUSDEV-86 : Questionnaire for a Submission - End
                        //Case #2080 Start 14
                        //Code modified for Case#3554 - Notify IRB enhancement
                        //Hardcoded column number modified.
                        tblActionForm.getColumnModel().getColumn(COMMENTS_COLUMN).setCellRenderer(buttonRenderer);
                        //Case #2080 End 14
                        //Modified for case#3046 - Notify IRB attachments - end
			scrPnPane1.setMinimumSize(new Dimension(970, 520)) ;
			scrPnPane1.setPreferredSize(new Dimension(970, 520)) ;
		} // Jobin -  end
        //Modified for case#3046 - Notify IRB attachments - start
        //Code modified for Case#3554 - Notify IRB enhancement- starts
        //Hardcoded column number modified.
        tblActionForm.getColumnModel().getColumn(SCHEDULE_ID_COLUMN).setMaxWidth(0);
        tblActionForm.getColumnModel().getColumn(SCHEDULE_ID_COLUMN).setMinWidth(0);
        tblActionForm.getColumnModel().getColumn(SCHEDULE_ID_COLUMN).setPreferredWidth(0);
        tblActionForm.getColumnModel().getColumn(SEQUENCE_NUMBER_COLUMN).setMaxWidth(0);
        tblActionForm.getColumnModel().getColumn(SEQUENCE_NUMBER_COLUMN).setMinWidth(0);
        tblActionForm.getColumnModel().getColumn(SEQUENCE_NUMBER_COLUMN).setPreferredWidth(0);
        tblActionForm.getColumnModel().getColumn(SUBMISSION_NUMBNER_COLUMN).setMaxWidth(0);
        tblActionForm.getColumnModel().getColumn(SUBMISSION_NUMBNER_COLUMN).setMinWidth(0);
        tblActionForm.getColumnModel().getColumn(SUBMISSION_NUMBNER_COLUMN).setPreferredWidth(0);
        //Code modified for Case#3554 - Notify IRB enhancement- ends
        //Modified for case#3046 - Notify IRB attachments - end
        tblActionForm.getTableHeader().setReorderingAllowed(false);
        
        tblActionForm.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
        
        tblActionForm.setFont(CoeusFontFactory.getNormalFont());
       
        tblActionForm.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent me)
            {
                if (me.getClickCount() == 2)
                {
					// If the screen is already open then return -- Added by Jobin - start
					if(canAction){
						return;
					} // Jobin - end
                    if (tblActionForm.getSelectedRowCount() > 0)
                    {    
                            int rowSelected = tblActionForm.getSelectedRow() ;
//                            System.out.println("scheduleId : " + protocolActionTableModel.getValueAt(rowSelected, SCHEDULE_ID_COLUMN) + "  submission num : " + protocolActionTableModel.getValueAt(rowSelected, SUBMISSION_NUMBNER_COLUMN)) ;
                            SubmissionDetailsBean detailsBean = new SubmissionDetailsBean() ;
                            detailsBean.setProtocolNumber(protocolId) ;
                            if (protocolActionTableModel.getValueAt(rowSelected, SCHEDULE_ID_COLUMN) == null){
                                detailsBean.setScheduleId(null) ;
                            }else{
                                detailsBean.setScheduleId(protocolActionTableModel.getValueAt(rowSelected, SCHEDULE_ID_COLUMN).toString()) ; 
                            }    
                            if (protocolActionTableModel.getValueAt(rowSelected, SEQUENCE_NUMBER_COLUMN) == null){
                                detailsBean.setSequenceNumber(null) ;
                            }else{
                                detailsBean.setSequenceNumber(new Integer(protocolActionTableModel.getValueAt(rowSelected, SEQUENCE_NUMBER_COLUMN).toString())) ; 
                            }
                            if (protocolActionTableModel.getValueAt(rowSelected, SUBMISSION_NUMBNER_COLUMN) == null){
                                //detailsBean.setSequenceNumber(null) ;
                                detailsBean.setSubmissionNumber(null) ;
                            }else{
                                detailsBean.setSubmissionNumber(new Integer(protocolActionTableModel.getValueAt(rowSelected, SUBMISSION_NUMBNER_COLUMN).toString())) ; 
                            }
                            
                            if (detailsBean.getSubmissionNumber()!= null){    
                                if (detailsBean.getSubmissionNumber().intValue() <= 0){
                                    CoeusOptionPane.showInfoDialog("Submission Details not available for this protocol action") ;
                                    return ;
                                }       
                            }    
                            
                            //Added by sharath - Modified by Jobin
							Vector vecDetails = getSubmissionDetails(detailsBean) ; 
                            Vector vecReviewers = null;
                            //Added by sharath
                            
                            if (vecDetails.size() <= 0){
                                //display appropriate msg
                                CoeusOptionPane.showInfoDialog("Submission Details not available for this protocol action") ;
                            }else{
                                //Added By sharath - START
                                //HashMap hashRow = (HashMap)vecDetails.elementAt(0) ;
                                int recCount;
                                int tmpSubNum = -1;
                                for(recCount = 0; recCount < vecDetails.size(); recCount++) {
                                    ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)vecDetails.get(recCount);
                                    if(detailsBean.getSubmissionNumber().intValue() == protocolSubmissionInfoBean.getSubmissionNumber()
                                      && detailsBean.getSequenceNumber().intValue()==protocolSubmissionInfoBean.getSequenceNumber()){
                                        // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
                                        tmpSubNum = protocolSubmissionInfoBean.getSubmissionNumber();
                                        // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
                                        break;
                                    }
                                }
                                // Commented for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
//                                ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)vecDetails.get(recCount);
//                                int tmpSubNum = protocolSubmissionInfoBean.getSubmissionNumber();
                                // Commented for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end                                
                                
                                // filtering for the modified action -- by Jobin - start
								Vector cvActions = (Vector) vDataObjects.get(1); 
								String subNum = protocolActionTableModel.getValueAt(rowSelected, SUBMISSION_NUMBNER_COLUMN).toString(); 
								int subNumber = 0;
								if (subNum != null) {
									subNumber = Integer.parseInt(subNum);
								}
								Vector newCVActions = new Vector();
								if (cvActions != null && cvActions.size() > 0) {
									for (int index = 0; index < cvActions.size(); index++) {
										ProtocolActionsBean actionBean = (ProtocolActionsBean)cvActions.get(index);
										if (subNumber == actionBean.getSubmissionNumber()) {
											newCVActions.add(actionBean);
										}
									}
								}
								vDataObjects.removeElementAt(1);
								vDataObjects.add(newCVActions); // Jobin - end
					
                                
                                //Protocol Enhancment - Submission Details window editable Start 
                                /*SubmissionDetailsForm frmSubmissionDetailsForm = 
                                    new SubmissionDetailsForm(parent, 
                                    "Submission details for Protocol " + detailsBean.getProtocolNumber(),
                                    detailsBean.getProtocolNumber(), 
                                    true, vDataObjects, vecReviewers, 'V', tmpSubNum ) ;*/
                                  SubmissionDetailsForm frmSubmissionDetailsForm = 
                                  new SubmissionDetailsForm(parent,"Submission details for Protocol " + 
                                      detailsBean.getProtocolNumber(),detailsBean.getProtocolNumber(), 
                                      true, vDataObjects, vecReviewers, 'M', tmpSubNum ) ;
                                //Protocol Enhancment - Submission Details window editable End
                                  
								//setting up the values for the table protocolactions - Added by Jobin - start
								frmSubmissionDetailsForm.commAction(newCVActions); // Added by Jobin - end
                               
                                frmSubmissionDetailsForm.showForm() ;
                                // COEUSQA-2105: No notification for some IRB actions - Start
//				// 3283: Reviewer Notification Changes:Start
//                                //Reviewers would be notified by emails if the reviewer on max submission number is changed.
//                                if(detailsBean.getSequenceNumber()!=null && frmSubmissionDetailsForm.isReviewerPersonsChanged()){
//                                    //Check if the reviewer change is on maxSubmissionNo
//                                    int maxsubmission = 0;
//                                    for(int i = 0 ;i<vecDetails.size();i++){
//                                        ProtocolSubmissionInfoBean protocolSubmissionBean = (ProtocolSubmissionInfoBean)vecDetails.get(i);
//                                        if(protocolSubmissionBean.getSequenceNumber()==detailsBean.getSequenceNumber().intValue()
//                                                                && protocolSubmissionBean.getSubmissionNumber()>maxsubmission){
//                                            maxsubmission    = protocolSubmissionBean.getSubmissionNumber();
//                                        }
//                                    }
//                                    if(detailsBean.getSubmissionNumber().intValue() == maxsubmission){
//                                        
//                                        ProtocolMailController mailController = new ProtocolMailController(true);
//                                        synchronized(mailController) {
//                                            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                                            mailController.sendMail(ACTION_REVIEWER_CHANGE, detailsBean.getProtocolNumber(),detailsBean.getSequenceNumber());
//                                            //Action Id 351 - Change in Reviewer
//                                        }
//                                    }
//                                }
//                                // 3283 - End			
                                // COEUSQA-2105: No notification for some IRB actions - End
                                //Added By sharath - END
                            }
                   }
                }
            }    
            
        }) ;
        
//         scrPnPane1.setMinimumSize(new Dimension(360, 330)) ;
//        scrPnPane1.setPreferredSize(new Dimension(360, 330)) ;
        
        scrPnPane1.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
     
        scrPnPane1.setViewportView(tblActionForm);
        scrPnPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrPnPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        //gridBagConstraints.gridheight = 4;
//        gridBagConstraints.ipadx = 350;
//        gridBagConstraints.ipady = 50;
        //gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.fill = GridBagConstraints.BOTH ;
        
        add(scrPnPane1, gridBagConstraints);
//        add(scrPnPane1,BorderLayout.CENTER);

    }

    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {            
        
            if(tblActionForm.getRowCount() > 0 ) {
                tblActionForm.requestFocusInWindow();
                tblActionForm.setRowSelectionInterval(0, 0);
                tblActionForm.setColumnSelectionInterval(1,1);
            }
        }
    }    
    //end Amit       
   

    public void setFormData(){
		// if the action tab opened then set the vector value of action tab -- Added by Jobin -  start
        if(canAction){
			commActionData = vecActions;
		} // Added by Jobin -  end
		
       if((commActionData != null) && (commActionData.size()>0)){
           
            vcDataPopulate = new Vector();
            Vector vcDataModelPopulate = new Vector();
            Vector vcData;
            Vector vcDataModel;
            
            for(int inCtrdata=0;inCtrdata < commActionData.size();
                                                                inCtrdata++){
                protocolActionBean=(ProtocolActionsBean)
                            commActionData.get(inCtrdata);
                String stDescription=protocolActionBean.getActionTypeDescription() ;
                String stComments=protocolActionBean.getComments();
                int sequenceNumber=protocolActionBean.getSequenceNumber();
                protocolId= protocolActionBean.getProtocolNumber() ;
                int submissionNumber = protocolActionBean.getSubmissionNumber() ;
				                
                edu.mit.coeus.utils.DateUtils test = new edu.mit.coeus.utils.DateUtils();
//				String actionDate = test.formatDate(protocolActionBean.getActionDate().toString(), "dd-MMM-yyyy");// added by Jobin
                java.util.Date dt = null;
                String stDate = test.formatDate(protocolActionBean.getUpdateTimestamp().toString(), "dd-MMM-yyyy");
                
                String stScheduleId = protocolActionBean.getScheduleId() ;
                vcData = new Vector();
                Boolean commentsExist = new Boolean( stComments!= null && stComments.trim().length() > 0);
                //Added for case#3046 - Notify IRB attachments
                //Code commented and modified for Case#3554 - Notify IRB enhancement
                //To remove the view document column
                //vcData.add("");
                vcData.add("");
                vcData.add("");
                vcData.add(stDescription);
                vcData.add(stDate);
                //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
                //vcData.add(protocolActionBean.getActionDate());
                java.text.SimpleDateFormat dtFormat = new java.text.SimpleDateFormat("dd-MMM-yyyy");
                
                if(protocolActionBean.getActionDate() != null){
                    vcData.add(dtFormat.format(protocolActionBean.getActionDate()));
                }else{
                    vcData.add(CoeusGuiConstants.EMPTY_STRING);
                }
                //COEUSQA:3187 - End
                
                vcData.add(stScheduleId);
                vcData.add(new Integer(sequenceNumber)) ;
                vcData.add(new Integer(submissionNumber)) ;
				vcData.add(stComments);
                vcData.add(commentsExist);
				
                vcDataModel = new Vector();
                //Added for case#3046 - Notify IRB attachments
                //Code commented and modified for Case#3554 - Notify IRB enhancement
                //To remove the view document column
                //vcDataModel.add("");
                vcDataModel.add("");
                vcDataModel.add(stDescription);
                vcDataModel.add(stDate);                
                //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
                //vcDataModel.add(protocolActionBean.getActionDate());
                if(protocolActionBean.getActionDate() != null){
                    vcDataModel.add(protocolActionBean.getActionDate());
                }else{
                    vcDataModel.add(CoeusGuiConstants.EMPTY_STRING);
                }
                //COEUSQA:3187 - End
                
                vcDataModel.add(stScheduleId);
                vcDataModel.add(new Integer(sequenceNumber)) ;
                vcDataModel.add(new Integer(submissionNumber)) ; 
				vcDataModel.add(stComments);
                vcDataModel.add(commentsExist);
				
                vcDataPopulate.add(vcData);
                vcDataModelPopulate.add(vcDataModel);
                
            }
                //Added BY sharath
                ((ProtocolActionTableModel)tblActionForm.getModel()).
                        setDataVector(vcDataPopulate);
                protocolActionTableModel.fireTableDataChanged();
       }

    }
   
    //Added By sharath for PDF preview
    //added a new field action date by jobin on aug-11-2004
    class ProtocolActionTableModel extends DefaultTableModel {
        //Code commented and modified for Case#3554 - Notify IRB enhancement- starts
        //To remove the view document column
        //Modified/Added for case#3046 - Notify IRB attachments
//        private String colNames[] = {".",".","Description","Date","Action Date","ScheduleId","SequenceNo","SubmissionNo","Comments","."};
//        private Class colTypes[] = {String.class, String.class, String.class, String.class,String.class, String.class, Integer.class,Integer.class,String.class, Boolean.class};
        private String colNames[] = {".",".","Description","Date","Action Date","ScheduleId","SequenceNo","SubmissionNo","Comments","."};
        private Class colTypes[] = {String.class,String.class, String.class, String.class,String.class, String.class, Integer.class,Integer.class,String.class, Boolean.class};
        //Code commented and modified for Case#3554 - Notify IRB enhancement- ends
        
        public boolean isCellEditable(int row, int column) {
            //if(column == BUTTON_COLUMN) return true; //Commented for the reason mentioned below
            //Added to set editable property to false if no document available for the protocolAction - Start
            ProtocolActionsBean protocolActionBean=(ProtocolActionsBean)commActionData.get(row);
            //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
            if(column == QUESTIONNAIRE_BUTTON_COLUMN){
                return true;
            }
            if(column == BUTTON_COLUMN) {
                //return protocolActionBean.isCorrespondenceExists()
                //Added for case #1961 start - 2
                if(protocolActionBean.isCorrespondenceExists()){
                    return true;
                }else if(protocolActionBean.isIsCorresTemplateExists()){
                    return true;
                }
                return false;
                //Added for case #1961 end - 2
            }else if( column == COMMENTS_ICON_COLUMN){
                String comments = protocolActionBean.getComments();
                //Modified for Protocol Enhancement case #2014 by tarique start 3
                if(comments == null){
                    comments = "";
                    protocolActionBean.setComments(comments);
                }
                return ( comments != null );
                
                //Modified for Protocol Enhancement case #2014 by tarique end 3
                //Commented for Protocol Enhancement case #2014 by tarique start 4
                //return ( comments != null && comments.trim().length() > 0 );
                //Commented for Protocol Enhancement case #2014 by tarique end 4
            //Code commented for Case#3554 - Notify IRB enhancement- starts
            //To remove the view document column
            //Added for case#3046 - Notify IRB attachments - start
//            }else if(column == DOC_BUTTON_COLUMN){  
//                if(protocolActionBean.isIsDocumentExists()){
//                    return true;   
//                }else{
//                    return false;
//                }
            //Code commented for Case#3554 - Notify IRB enhancement - ends
            }
            //Added for case#3046 - Notify IRB attachments - end
            //Enhancement Case # 2080- start 8 
            if(column == ACTION_DATE_COLUMN && isOpenedFormSubWdw()){
                return true;
            }
            if(column == DESCRIPTION_COLUMN || column == DATE_COLUMN || column == COMMENTS_COLUMN ){
                return false;
            }
            //Enhancement Case # 2080 - End 8
            
            //Added to set editable property to false if no document available for the protocolAction  - End
            return false;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Object getValueAt(int row, int column) {
            return super.getValueAt(row,column);
        }
        
        public String getColumnName(int colIndex) {
            return colNames[colIndex];
        }
        
        public Class getColumnClass(int colIndex) {
            return colTypes[colIndex];
        }
        
        public void setDataVector(Vector newData) {
            dataVector = newData;
            
            
        }
        
        public void setValueAt(Object value, int row, int column) {
            //Enhancement Case # 2080 Start 9
            java.text.SimpleDateFormat dtFormat
            = new java.text.SimpleDateFormat("MM/dd/yyyy");
            protocolActionBean=(ProtocolActionsBean)commActionData.get(row);
            Date date;
            String strDate;
            String mesg = null;
            switch(column){
                case ACTION_DATE_COLUMN:
                    try{
                        if (value!= null && value.toString().trim().length() > 0) {
                            //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
                            setActionDateValidationFired(false);
                            String formatedActualDate1 = CoeusGuiConstants.EMPTY_STRING;
                            if(protocolActionBean.getActionDate() != null){
                                String formatedActualDate = dtFormat.format(protocolActionBean.getActionDate());
                                formatedActualDate1 = dtUtils.formatDate(formatedActualDate, ":/.,|-", "dd-MMM-yyyy");
                            }
                            if(protocolActionBean.getActionDate() == null || !value.toString().trim().equals(formatedActualDate1)){
                                strDate = dtUtils.formatDate(value.toString().trim(), ":/.,|-", "dd-MMM-yyyy");                                
                                //If the entered date wont matches MM-DD-YYYY format, showing a Error dialog
                                if(strDate == null) {
                                    CoeusOptionPane.showInfoDialog(
                                            coeusMessageResources.parseMessageKey("protoSubmissionFrm_invalid_date_exceptionCode.2012"));

                                    Vector vecAction = new Vector();
                                    vecAction.add("true");
                                    vecAction.add(commActionData);
                                    observable.notifyObservers(vecAction);
                                    break;
                                }
                                strDate = dtUtils.restoreDate(strDate.trim(), ":/.,|-");
                                date = dtFormat.parse(strDate.trim());
                                protocolActionBean.setActionDate(new java.sql.Date(date.getTime()));
                                protocolActionBean.setAcType(TypeConstants.UPDATE_RECORD);
                                Vector vecAction = new Vector();
                                vecAction.add("false");
                                vecAction.add(commActionData);
                                observable.notifyObservers(vecAction);
                                setFormData();
                                
                            }                            
                            //COEUSQA:3187 - End
                        } else {
                            //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
                            CoeusOptionPane.showInfoDialog(
                                    coeusMessageResources.parseMessageKey("protoSubmissionFrm_empty_date_exceptionCode.2013"));
                            protocolActionBean.setActionDate(null);
                            setFormData();
                            Vector vecAction = new Vector();
                            vecAction.add("true");
                            vecAction.add(commActionData);
                            observable.notifyObservers(vecAction);
                            setActionDateValidationFired(true);
                            break;
                            //COEUSQA:3187 - End
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    } 
                    //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start                            
//                            strDate = dtUtils.formatDate(
//                            value.toString().trim(), ":/.,|-", "dd-MMM-yyyy");
//                        } else {
//                            return;
//                        }
//                        
//                        strDate = dtUtils.restoreDate(strDate, ":/.,|-");
//                        if(strDate==null) {
//                            throw new CoeusException();
//                        }
//                        date = dtFormat.parse(strDate.trim());
//                    }catch (CoeusException coeusException) {
//                        
//                        CoeusOptionPane.showInfoDialog(
//                        coeusMessageResources.parseMessageKey("protoSubmissionFrm_exceptionCode.2010"));
//                        
//                        return ;
//                    }catch (ParseException parseException) {
//                        parseException.getMessage();
//                        CoeusOptionPane.showInfoDialog(
//                        coeusMessageResources.parseMessageKey("protoSubmissionFrm_exceptionCode.2010"));
//                        
//                        return ;
//                    }
//                    
//                    if(protocolActionBean.getActionDate().equals(date)){
//                        break;
//                    }
//                    protocolActionBean.setActionDate(new java.sql.Date(date.getTime()));
//                    protocolActionBean.setAcType(TypeConstants.UPDATE_RECORD);
//                    observable.notifyObservers(commActionData);
//                    setFormData();
//                    break;
                    //COEUSQA:3187 - End
            }
            //Enhancement Case # 2080 End 9
        }
        
    }//End ProtocolActionTableModel
    
    
    class ProtocolActionCellRenderer extends DefaultTableCellRenderer {
        //Modified for case #1961 start 3
        private JButton btnView,btnComments,btnReproduce;
        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
        private JButton btnQuestionnaire;        
        //Added for COEUSDEV-86 : Questionnaire for a Submission - End
        //Added for case#3046 - Notify IRB attachments
        private JButton btnDocument;
        private JPanel pnlNoDocuments;
        //Modified for case #1961 end 3
        //Case #2080 Start 10
        private JLabel lblText;
        java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        //Case #2080 End 10
        
        ProtocolActionCellRenderer() {
            btnView = new JButton(imgIcnPDF);
            
            btnComments = new JButton(imgIcnComments);
            //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
            btnQuestionnaire = new JButton(imgQuestionnaire);
            //Added for case #1961 start 4
            btnReproduce = new JButton("  ");
            //Added for case #1961 end 4
            //Added for case#3046 - Notify IRB attachments
            btnDocument = new JButton();
            pnlNoDocuments = new JPanel();
          
            //Case #2080 Start 11
            lblText = new JLabel();
            lblText.setOpaque(true);
            //Case #2080 End 11
        }
        
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            //Added to disable button if no document available - Start
            ProtocolActionsBean protocolActionBean=(ProtocolActionsBean)commActionData.get(row);

            if( column == ACTION_DATE_COLUMN){
                
                if(!isOpenedFormSubWdw()){
                    lblText.setBackground(bgListColor);
                    lblText.setForeground(java.awt.Color.BLACK);
                }else {
                    lblText.setBackground(java.awt.Color.white);
                    lblText.setForeground(java.awt.Color.black);
                }
                
                if(isSelected){
                    lblText.setBackground(java.awt.Color.YELLOW);
                    lblText.setForeground(java.awt.Color.black);
                }
                
                if(value != null && !value.toString().trim().equals("")){                   
                    //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
                    // value = dtUtils.formatDate(value.toString(),"dd-MMM-yyyy");
                    if( checkFormat(value.toString()) == false ) {
                        value = dtUtils.formatDate(value.toString(),"dd-MMM-yyyy");
                    }
                    //COEUSQA:3187 - End
                    lblText.setText(value.toString());
                }else{
                    lblText.setText("");
                }
                
                return lblText;
                
            }else if( column == DESCRIPTION_COLUMN || column == COMMENTS_COLUMN ||
                      column == DATE_COLUMN ){
               
                if(functionType == TypeConstants.DISPLAY_MODE){
                    lblText.setBackground(bgListColor);
                    lblText.setForeground(java.awt.Color.BLACK);
                }if(isSelected){
                    lblText.setBackground(java.awt.Color.YELLOW);
                    lblText.setForeground(java.awt.Color.black);
                }else{
                    lblText.setBackground(bgListColor);
                    lblText.setForeground(java.awt.Color.BLACK);
                }
                
                if(value == null || value.toString().trim().equals("")){
                    lblText.setText("");
                }else{
                    lblText.setText(value.toString());
                    
                }
                
                return lblText;
            }
            
            //Enhancement Case # 2080 - End
            
            else if( column == COMMENTS_ICON_COLUMN ){
                 //Commented for Protocol Enhancement case #2014 by tarique start 1
                //                if( !((Boolean)value).booleanValue() ){
//                    return pnlNoDocuments;
//                }else{
//                    return btnComments;
//                }
                //Commented for Protocol Enhancement case #2014 by tarique end 1
                return btnComments;
            //Code commented for Case#3554 - Notify IRB enhancement- starts
            //To remove the view document column
            //Added for case#3046 - Notify IRB attachments - start 
//            }else if(column == DOC_BUTTON_COLUMN){
//                if(protocolActionBean.isIsDocumentExists()){
//                    btnDocument.setIcon(imgIcnPDF);                    
//                }else{
//                    btnDocument.setIcon(null);
//                }
//                return btnDocument;
            //Added for case#3046 - Notify IRB attachments - end  
            //Code commented for Case#3554 - Notify IRB enhancement- ends
            //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
            }else if(column == QUESTIONNAIRE_BUTTON_COLUMN){
                
                int actionCode = protocolActionBean.getActionTypeCode();
                /*if(!protocolActionBean.isAnsweredQnrPresent()){
                    btnQuestionnaire.setIcon(null);
                    btnQuestionnaire.setText("...");
                }else if(protocolActionBean.isAnsweredQnrPresent() && (actionCode == NOTIFY_IRB ||*/
                if((actionCode == NOTIFY_IRB ||
                        actionCode ==  REQUEST_TO_CLOSE_ACTION ||
                        actionCode == REQUEST_FOR_SUSPENSION_ACTION  ||
                        actionCode == REQUEST_TO_CLOSE_ENROLLMENT_ACTION  ||
                        actionCode == REQUEST_FOR_DATA_ANALYSIS_ONLY_ACTION  ||
                        actionCode == REQUEST_FOR_RE_OPEN_ENROLLMENT_ACTION)){
                    btnQuestionnaire.setIcon(imgQuestionnaire);
                    btnQuestionnaire.setText("");
                }else{
                    btnQuestionnaire.setIcon(null);
                    btnQuestionnaire.setText("...");
                }
                return btnQuestionnaire;
            }else if(column == BUTTON_COLUMN){
                if(! protocolActionBean.isCorrespondenceExists()) {
                    //Added for case #1961 start 5
                    if(!protocolActionBean.isIsCorresTemplateExists()){
                        return pnlNoDocuments;
                    }
                    return btnReproduce;
                    //Added for case #1961 end 5
                }
            }
            //Added to disable button if no document available - End
            return btnView;
            
        }
        
    }//End ProtocolActionCellRenderer
    
    
    class ProtocolActionCellEditor extends DefaultCellEditor implements ActionListener{
        private int col;
        //Added for case #1961 start 6
        private JButton btnViewDocuments,btnComments, btnReproduce;
        //Added for case #1961 end 6 
        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
        private JButton btnViewSubQuestionnaire;
        //Added for COEUSDEV-86 : Questionnaire for a Submission - End
        //Added for case#3046 - Notify IRB attachments
        private JButton btnDocument;
        private DocumentList documentList;
        private DataViewForm commentsViewForm;
        private CoeusTextField txtDate;
        private JPanel pnlNoDocuments;
        ProtocolActionCellEditor() {
            super(new JComboBox());
            txtDate = new CoeusTextField();
            // Code modified for coeus4.3 enhancement - starts
            documentList = new DocumentList(parent, true, functionType);
            // Code modified for coeus4.3 enhancement - ends
            commentsViewForm = new DataViewForm();
            commentsViewForm.setTitle("Protocol Action Comments");
            btnViewDocuments = new JButton(imgIcnPDF);
            btnComments = new JButton(imgIcnComments);
            //Added for case #1961 start 7
            btnReproduce = new JButton("   ");
            //Added for case #1961 end 7
            btnViewDocuments = new JButton(imgIcnPDF);
            //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
            btnViewSubQuestionnaire = new JButton(imgQuestionnaire);
            btnViewSubQuestionnaire.addActionListener(this);
            //Added for COEUSDEV-86 : Questionnaire for a Submission - End
            //Added for case#3046 - Notify IRB attachments - start
            btnDocument = new JButton();
            btnDocument.addActionListener(this);
            //Added for case#3046 - Notify IRB attachments - end
            btnComments.addActionListener(this);
            //Added for case #1961 start 8
            btnReproduce.addActionListener(this);                        
            pnlNoDocuments = new JPanel();
            //Added for case #1961 end 8
            //Added for case#3072 - Documents Premium - Final flag is not sticking
            btnViewDocuments.addActionListener(this);            
        }
        
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean isSelected, int row, int column) {
            this.col = column;
 
            if( column == COMMENTS_ICON_COLUMN ){
                //Commented for Protocol Enhancement case #2014 by tarique start 2
               // btnComments.setEnabled(((Boolean)value).booleanValue());
                //Commented for Protocol Enhancement case #2014 by tarique end 2
                return btnComments;
                //Enhancement Case # 2080 - start
            }else if(column == ACTION_DATE_COLUMN){
                txtDate.setText(dtUtils.formatDate(value.toString(),"MM/dd/yyyy"));
                return txtDate;
            //Code commented for Case#3554 - Notify IRB enhancement- starts
            //To remove the view document column
            //Added for case#3046 - Notify IRB attachments - start
//            }else if(column == DOC_BUTTON_COLUMN){
//                if(protocolActionBean.isIsDocumentExists()){
//                    btnDocument.setIcon(imgIcnPDF);                    
//                }else{
//                    btnDocument.setIcon(null);
//                }
//                return btnDocument;
            }
            //Added for case#3046 - Notify IRB attachments - end
            //Code commented for Case#3554 - Notify IRB enhancement- ends
            //Enhancement Case # 2080 - End
         
            //Added to disable button if no document savailable - Start
            ProtocolActionsBean protocolActionBean=(ProtocolActionsBean)commActionData.get(row);
            //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
            if(column == QUESTIONNAIRE_BUTTON_COLUMN){
                int actionCode = protocolActionBean.getActionTypeCode();
                /*if(!protocolActionBean.isAnsweredQnrPresent()){
                    btnViewSubQuestionnaire.setIcon(null);
                    btnViewSubQuestionnaire.setText("...");
                }else if( protocolActionBean.isAnsweredQnrPresent() && (actionCode == NOTIFY_IRB ||*/
                if((actionCode == NOTIFY_IRB ||
                        actionCode ==  REQUEST_TO_CLOSE_ACTION ||
                        actionCode == REQUEST_FOR_SUSPENSION_ACTION  ||
                        actionCode == REQUEST_TO_CLOSE_ENROLLMENT_ACTION  ||
                        actionCode == REQUEST_FOR_DATA_ANALYSIS_ONLY_ACTION  ||
                        actionCode == REQUEST_FOR_RE_OPEN_ENROLLMENT_ACTION)){
                    btnViewSubQuestionnaire.setIcon(imgQuestionnaire);
                    btnViewSubQuestionnaire.setText("");
                }else{
                    btnViewSubQuestionnaire.setIcon(null);
                    btnViewSubQuestionnaire.setText("...");
                }
                return btnViewSubQuestionnaire;
            }
            //Added for COEUSDEV-86 : Questionnaire for a Submission - End
            //btnViewDocuments.setEnabled(protocolActionBean.isCorrespondenceExists());
            //Added for case #1961 start 9
            if(column == BUTTON_COLUMN){
                if(protocolActionBean.isCorrespondenceExists()){
                    return btnViewDocuments;
                }else if(protocolActionBean.isIsCorresTemplateExists()){
                    return btnReproduce;
                }
            }
             
            //Added for case #1961 end 9
            //Added to disable button if no document savailable - End
           return pnlNoDocuments;
        }
        
        public void actionPerformed(ActionEvent actionEvent) {
            String protocolNumber;
            int sequenceNumber, actionId;
            protocolActionBean=(ProtocolActionsBean)
                            commActionData.get(tblActionForm.getSelectedRow());
            if( actionEvent.getSource().equals(btnComments)){
                
                //Protocol Enhancment -  Administrative Correction Start 2
                //commentsViewForm.setData(protocolActionBean.getComments());
                commentsViewForm.setData(protocolActionBean);
                commentsViewForm.setFunctionType(functionType);
                ProtocolSubmissionInfoBean porotoSubInfoBean =
                    (ProtocolSubmissionInfoBean)setCommentsData();
                commentsViewForm.setProtoSubmissionBean(porotoSubInfoBean);
                //Protocol Enhancment -  Administrative Correction End 2
                
                
                commentsViewForm.display();
                
                //Protocol Enhancment -  Administrative Correction Start 3
                if(functionType != CoeusGuiConstants.DISPLAY_MODE 
                && commentsViewForm.isOK_CLICKED() ){
                    setSaveRequired(true);
                 }
                //Protocol Enhancment -  Administrative Correction End 3
            }//Added for case #1961 start 10
            else if(actionEvent.getSource().equals(btnReproduce)){
                protocolNumber = protocolActionBean.getProtocolNumber() ;
                sequenceNumber = protocolActionBean.getSequenceNumber();
                actionId = protocolActionBean.getActionId();
                tblActionForm.getCellEditor().stopCellEditing();
                documentList.loadFormData(protocolActionBean);
                
                documentList.display();
                if(documentList.isButtonPress()){
                    //Commented for case#3072 - Documents Premium - Final flag is not sticking - start
//                    if(!protocolActionBean.isCorrespondenceExists()){                            
//                        protocolActionBean.setIsCorrespondenceExists(true);
//                    }
//                    if(!protocolActionBean.isIsCorresTemplateExists()){                            
//                        protocolActionBean.setIsCorresTemplateExists(true);
//                    }
                    //Commented for case#3072 - Documents Premium - Final flag is not sticking - end
                    setFormData();
                }
            //Added for case #1961 end 10 
            //Added for case#3046 - Notify IRB attachments - start    
            }else if(actionEvent.getSource().equals(btnDocument)){
                tblActionForm.getCellEditor().stopCellEditing();
                if(protocolActionBean.isIsDocumentExists()){
                    try{
                        viewSubmissionDocument(protocolActionBean);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
            //Added for COEUSDEV-86 : questionnaire for Submission - Start
            else if(actionEvent.getSource().equals(btnViewSubQuestionnaire)){
                int actionCode = protocolActionBean.getActionTypeCode();
//                if(protocolActionBean.isAnsweredQnrPresent() &&
                if((actionCode == NOTIFY_IRB ||
                        actionCode ==  REQUEST_TO_CLOSE_ACTION ||
                        actionCode == REQUEST_FOR_SUSPENSION_ACTION  ||
                        actionCode == REQUEST_TO_CLOSE_ENROLLMENT_ACTION  ||
                        actionCode == REQUEST_FOR_DATA_ANALYSIS_ONLY_ACTION  ||
                        actionCode == REQUEST_FOR_RE_OPEN_ENROLLMENT_ACTION)){
                    showQuestionnaireWindow(protocolActionBean);
                }else{
                    String message = "";
                    MessageFormat formatter = new MessageFormat("");
                    message = formatter.format(coeusMessageResources.parseMessageKey(NO_QUESTIONNAIRE_FOR_SUBMISSION), protocolActionBean.getActionTypeDescription());
                    CoeusOptionPane.showWarningDialog(message);
                }
            }
            else{
            //Added for case#3046 - Notify IRB attachments - end    
                protocolNumber = protocolActionBean.getProtocolNumber() ;
                sequenceNumber = protocolActionBean.getSequenceNumber();
                actionId = protocolActionBean.getActionId();

                documentList.loadForm(protocolActionBean);
                if(documentList.getDocumentCount() == 0)
                {
                    CoeusOptionPane.showInfoDialog(NO_DOCUMENT);
                    return ;
                }
                documentList.display();
            }
        }
        public Object getCellEditorValue() {
            switch (col) {
                case ACTION_DATE_COLUMN:
                    return txtDate.getText();
            }
            return ((CoeusTextField)txtDate).getText();
        }
    
    }
    
     //Added By sharath for PDF preview
    //Protocol Enhancment -  Administrative Correction Start 4
    private ProtocolSubmissionInfoBean setCommentsData(){
        int rowSelected = tblActionForm.getSelectedRow() ;
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean
                                = new ProtocolSubmissionInfoBean() ;
        
        protocolSubmissionInfoBean.setProtocolNumber(protocolId) ;
        
        if (protocolActionTableModel.getValueAt(rowSelected, SCHEDULE_ID_COLUMN) == null){
            protocolSubmissionInfoBean.setScheduleId(null) ;
        }else{
            protocolSubmissionInfoBean.setScheduleId(protocolActionTableModel.getValueAt(rowSelected, SCHEDULE_ID_COLUMN).toString()) ;
        }
        if (protocolActionTableModel.getValueAt(rowSelected, SEQUENCE_NUMBER_COLUMN) != null){
            String seqNo = protocolActionTableModel.getValueAt(
                    rowSelected, SEQUENCE_NUMBER_COLUMN).toString();
            protocolSubmissionInfoBean.setSequenceNumber(Integer.parseInt(seqNo));
        }
        
        if (protocolActionTableModel.getValueAt(rowSelected, SUBMISSION_NUMBNER_COLUMN) != null){
            String subNo = protocolActionTableModel.getValueAt(
                    rowSelected, SUBMISSION_NUMBNER_COLUMN).toString();
            protocolSubmissionInfoBean.setSubmissionNumber(Integer.parseInt(subNo));
        }
        
        return protocolSubmissionInfoBean;
    }//End setCommentsData
    
    /**
     * Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /**
     * Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    /**
     * Getter for property commActionData.
     * @return Value of property commActionData.
     */
    public java.util.Vector getCommActionData() {
        return commActionData;
    }
    
    /**
     * Setter for property commActionData.
     * @param commActionData New value of property commActionData.
     */
    public void setCommActionData(java.util.Vector commActionData) {
        this.commActionData = commActionData;
    }
    
    //Protocol Enhancment -  Administrative Correction End 4
    
    
    //Case #2080 Start 12
    
    /**
     * Getter for property openedFormSubWdw.
     * @return Value of property openedFormSubWdw.
     */
    public boolean isOpenedFormSubWdw() {
        return openedFormSubWdw;
    }
    
    /**
     * Setter for property openedFormSubWdw.
     * @param openedFormSubWdw New value of property openedFormSubWdw.
     */
    public void setOpenedFormSubWdw(boolean openedFormSubWdw) {
        this.openedFormSubWdw = openedFormSubWdw;
    }
    
    /**
     * Registers the Observer
     */    
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    /**
     * UnRegisters the Observer
     */    
    public void unRegisterObserver( Observer observer ) {
        observable.deleteObserver( observer );
    }
    
    /**
     * Getter for property tblActionForm.
     * @return Value of property tblActionForm.
     */
    public javax.swing.JTable getTblActionForm() {
        return tblActionForm;
    }
    
    /**
     * Setter for property tblActionForm.
     * @param tblActionForm New value of property tblActionForm.
     */
    public void setTblActionForm(javax.swing.JTable tblActionForm) {
        this.tblActionForm = tblActionForm;
    }
   
    //Case #2080 End 12
    
    //Added for case#3046 - Notify IRB attachments - start
    /**
     * This method facilitates the view of blob data
     * @throws Exception
     */
    private void viewSubmissionDocument(ProtocolActionsBean protocolActionBean) throws Exception{
        
        String templateUrl =  null;
        DocumentBean documentBean = new DocumentBean();
        RequesterBean requesterBean = new RequesterBean();
        ProtocolActionDocumentBean protocolActionDocumentBean = new ProtocolActionDocumentBean();
        protocolActionDocumentBean.setProtocolNumber(protocolActionBean.getProtocolNumber());
        protocolActionDocumentBean.setSequenceNumber(protocolActionBean.getSequenceNumber());
        protocolActionDocumentBean.setSubmissionNumber(protocolActionBean.getSubmissionNumber());
        protocolActionDocumentBean.setDocumentId(1);
        Map map = new HashMap();
        map.put("DOCUMENT_TYPE", "SUBMISSION_DOC_DB");
        map.put("PROTO_ACTION_BEAN", protocolActionDocumentBean);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ProtocolDocumentReader");
        documentBean.setParameterMap(map);
        requesterBean.setDataObject(documentBean);
        requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(STREMING_SERVLET, requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        responderBean = comm.getResponse();
        if(!responderBean.isSuccessfulResponse()){
            throw new CoeusException(responderBean.getMessage(),0);
        }
        map = (Map)responderBean.getDataObject();
        templateUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);       
        
        try{
            URL urlObj = new URL(templateUrl);
            URLOpener.openUrl(urlObj);
        }catch(MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();            
        }catch(Exception ue) {
            ue.printStackTrace() ;
        }        
    }   
    //Added for case#3046 - Notify IRB attachments - end
    //Added for COEUSDEV-86 : questionnaire for Submission - Start
    private void showQuestionnaireWindow(ProtocolActionsBean protocolActionBean){
        try{
            QuestionAnswersController questionAnswersController = new QuestionAnswersController(protocolActionBean.getProtocolNumber(),true);
            questionAnswersController.setFunctionType(TypeConstants.DISPLAY_MODE);
            QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = new QuestionnaireAnswerHeaderBean();
            questionnaireAnswerHeaderBean.setModuleItemCode(PROTOCOL_MODULE);
            questionnaireAnswerHeaderBean.setModuleSubItemCode(PROTOCOL_SUBMISSION_SUB_MODULE);
            questionnaireAnswerHeaderBean.setModuleItemKey(protocolActionBean.getProtocolNumber());
            questionnaireAnswerHeaderBean.setModuleSubItemKey(protocolActionBean.getSubmissionNumber()+"");
            questionAnswersController.setFormData(questionnaireAnswerHeaderBean);
            questionAnswersForm = ((QuestionAnswersForm)questionAnswersController.getControlledUI());
            questionAnswersForm.btnModify.setEnabled(false);
            questionAnswersForm.btnStartOver.setEnabled(false);
            questionAnswersForm.setFocusable(true);
            SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    questionAnswersForm.requestFocusInWindow();
                }
            });
     
            CoeusVector cvFormData = (CoeusVector)questionAnswersController.getFormData();
            if(cvFormData != null && cvFormData.size() > 0){
                String title = protocolActionBean.getActionTypeDescription()+" Questionnaire - "+protocolActionBean.getProtocolNumber();
                dlgProtoSubQuestionnaire= new CoeusDlgWindow(parent, title, true);
                dlgProtoSubQuestionnaire.addEscapeKeyListener(new AbstractAction("escPressed"){
                    public void actionPerformed(ActionEvent ae){
                        dlgProtoSubQuestionnaire.dispose();
                    }
                });
                questionAnswersForm.btnClose.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dlgProtoSubQuestionnaire.dispose();
                    }
                });
                dlgProtoSubQuestionnaire.addWindowListener( new WindowAdapter() {
                    public void windowClosing(WindowEvent we){
                        dlgProtoSubQuestionnaire.dispose();
                    }
                });
                
                dlgProtoSubQuestionnaire.setResizable(false);
                dlgProtoSubQuestionnaire.getContentPane().add(questionAnswersController.getControlledUI());
                dlgProtoSubQuestionnaire.setFont(CoeusFontFactory.getLabelFont());
                dlgProtoSubQuestionnaire.setSize(990, 600);
                
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension dlgSize = dlgProtoSubQuestionnaire.getSize();
                dlgProtoSubQuestionnaire.setLocation(screenSize.width/2 - (dlgSize.width/2),
                        screenSize.height/2 - (dlgSize.height/2));
                dlgProtoSubQuestionnaire.show();
            }else{
                String message = "";
                MessageFormat formatter = new MessageFormat("");
                message = formatter.format(coeusMessageResources.parseMessageKey("protocoSubmissionQuestions_exceptionCode.1003"), protocolActionBean.getActionTypeDescription());
                CoeusOptionPane.showWarningDialog(message);
                
            }
        }catch(Exception e){
            CoeusOptionPane.showInfoDialog(e.getMessage());
            
        }
    }
    //Added for COEUSDEV-86 : questionnaire for Submission - End
    
    //COEUSQA:3187 - Error when correcting Action Date on the Submission Details window - Start
    /**
     * verify the input date format.
     * @param dateStr String that is to be verified the input format
     *
     */
    public boolean checkFormat(String dateStr) {
        boolean res = false;
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MMM-yyyy");
        if (dateStr.trim().length() != dateFormat.toPattern().length())
            return false;
        
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateStr.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
    
    
    public boolean isActionDateValidationFired() {
        return actionDateValidationFired;
    }
    
    public void setActionDateValidationFired(boolean actionDateValidationFired) {
        this.actionDateValidationFired = actionDateValidationFired;
    }
    //COEUSQA:3187 - End
    
}
