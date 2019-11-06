/*
 * FundingProposalsController.java
 *
 * Created on June 10, 2004, 4:05 PM
 * @author   bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.award.bean.AwardFundingProposalBean;
import edu.mit.coeus.award.bean.AwardInvestigatorsBean;
import edu.mit.coeus.award.controller.AwardController;
import edu.mit.coeus.award.gui.AwardHeaderForm;
import edu.mit.coeus.award.gui.FundingProposalForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.instprop.bean.InstituteProposalInvestigatorBean;
import edu.mit.coeus.search.gui.FundingProposalSearch;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusServerProperties;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils ;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.exception.CoeusClientException;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.text.*;
import java.sql.Timestamp;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * This class is controller class which is used to handle
 * the Funding Proposals.
 */
public class FundingProposalsController extends AwardController
implements ActionListener, ListSelectionListener {
    private FundingProposalForm fundingProposalForm;
    public CoeusDlgWindow dlgFundingProposal; // JM 8-14-2013 changed to public so can be referenced
    private CoeusAppletMDIForm mdiForm;
    private AwardBaseBean awardBaseBean;
    private AwardFundingProposalBean awardFundingProposalBean;
    private CoeusVector cvFundingProposal;
    private CoeusVector cvAwardsDetails;
    private CoeusVector cvDeletedData;
    private CoeusVector cvInsertedBeans;
    private QueryEngine queryEngine;
    private CoeusMessageResources coeusMessageResources;
    private FundingProposalTableModel fundingProposalTableModel;
    private FundingProposalRenderer fundingProposalRenderer;
    private AwardHeaderForm awardHeaderForm;
    private static final String EMPTY_STRING = "";
    private ListSelectionModel selectionModel;
    
    private static final String DIALOG_TITLE = "Funding Proposals";
    private static final String TAB_BACKGRND ="TabbedPane.tabAreaBackground";
    
    private static final int PROPOSAL_COLUMN = 0;
    private static final int SEQ_COLUMN = 1;
    private static final int TYPE_COLUMN = 2;
    /*Added for case 3186 :Add fields to the Funding Proposals Screen in the Awards module - start*/
    private static final int PI = 3;
    private static final int LEAD_UNIT = 4;
    /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - end*/

    private static final int START_DATE_COLUMN = 5;
    private static final int END_DATE_COLUMN = 6;
    private static final int DIRECT_COST_COLUMN =7;
    private static final int INDIRECT_COST_COLUMN = 8;
    private static final int TOTAL_COST_COLUMN = 9;
  
    
    private static final String PROPOSAL_NUMBER = "PROPOSAL_NUMBER";
    private static final String SEQUENCE_NUMBER = "SEQUENCE_NUMBER";
    private static final String TOTAL_DIRECT_COST_TOTAL = "TOTAL_DIRECT_COST_TOTAL";
    private static final String TOTAL_INDIRECT_COST_TOTAL = "TOTAL_INDIRECT_COST_TOTAL";
    private static final String REQUESTED_START_DATE_TOTAL = "REQUESTED_START_DATE_TOTAL";
    private static final String REQUESTED_END_DATE_TOTAL = "REQUESTED_END_DATE_TOTAL";
    private static final String STATUS_CODE = "STATUS_CODE";
    private static final String PROPOSAL_TYPE_DESC = "PROPOSAL_TYPE_DESC";
    /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  -start*/
    private static final String UNIT_NUMBER = "UNIT_NUMBER";
    private static final String PERSON_NAME = "PERSON_NAME";
    /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  -end*/
    
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    private static final int HOLD = 6;
    private DateUtils dateUtils = new DateUtils();
    private FundingProposalSearch fundingProposalSearch;
    private boolean modified;
    private int rowId;
    private static final String AWARD_SERVLET = "/AwardMaintenanceServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
    AWARD_SERVLET;
    private static final char GET_DATA = 'D';
    private boolean awardStatusToHold = false;
    
    //private DateUtils dateUtils = new DateUtils();
    
    private SimpleDateFormat simpleDateFormat;
    
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    private static final String DATE_SEPARATERS = "-:/.,|";
    
    //COEUSQA-1477 Dates in Search Results - Start
    private static final String DATE_FORMAT_DELIMITER = "/";
    private static final String DATE_FORMAT_USER_DELIMITER = "-"; 
    private static final String DATE_FORMAT_YEAR_DELIMITER = "y";
    private static final String DATE_FORMAT_MONTH_DELIMITER = "m";
    private static final String DATE_FORMAT_DATE_DELIMITER = "d";
    //COEUSQA-1477 Dates in Search Results - End
    
    /**
     * Contructs an instancce of FundingProposalsController which calles its super
     * @param awardBaseBean The base bean
     * @param functionType Function type
     */
    public FundingProposalsController(AwardBaseBean awardBaseBean,char functionType ) {
        super(awardBaseBean);
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        this.awardBaseBean = awardBaseBean;
        initComponents();
        registerComponents();
        setFunctionType(functionType);
        setFormData(awardBaseBean);
        postInitComponents();
        setColumnData();
    }
    
    /**
     * Creates instances of the gui.
     */
    public void initComponents() {
        modified = false;
        cvFundingProposal = new CoeusVector();
        cvDeletedData = new CoeusVector();
        fundingProposalForm = new FundingProposalForm();
        awardHeaderForm = new AwardHeaderForm();
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        fundingProposalTableModel = new FundingProposalTableModel();
        fundingProposalRenderer =new FundingProposalRenderer();
        cvInsertedBeans=new CoeusVector();
    }
    /**
     * Registers the listeners for the components.
     */
    public void registerComponents() {
        fundingProposalForm.btnAdd.addActionListener(this);
        fundingProposalForm.btnCancel.addActionListener(this);
        fundingProposalForm.btnDelete.addActionListener(this);
        fundingProposalForm.btnOk.addActionListener(this);
        fundingProposalForm.tblFundingProposal.setModel(fundingProposalTableModel);
        selectionModel = fundingProposalForm.tblFundingProposal.getSelectionModel();
        selectionModel.addListSelectionListener(this);
        fundingProposalForm.tblFundingProposal.setSelectionModel(selectionModel);
    }
    
    /**
     * Creates a dialog instance and adds the forms to the dialog.
     */
    public void postInitComponents() {
        mdiForm = CoeusGuiConstants.getMDIForm();
        dlgFundingProposal = new CoeusDlgWindow(mdiForm);
        dlgFundingProposal.getContentPane().setLayout(
        new java.awt.BorderLayout());
        dlgFundingProposal.getContentPane().add(
        awardHeaderForm,java.awt.BorderLayout.NORTH);
        dlgFundingProposal.getContentPane().add(
        fundingProposalForm,java.awt.BorderLayout.CENTER);
        dlgFundingProposal.setResizable(false);
        dlgFundingProposal.setModal(true);
        dlgFundingProposal.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        fundingProposalForm.tblFundingProposal.setIntercellSpacing(new java.awt.Dimension(0,0));
        dlgFundingProposal.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgFundingProposal.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we) {
                setDefaultFocus();
            }
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
    }
    
    /**
     * Displays the dialog and sets the display properties
     */
    public void display() {
        formatFields();
        dlgFundingProposal.setTitle(DIALOG_TITLE);
        dlgFundingProposal.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgFundingProposal.getSize();
        dlgFundingProposal.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgFundingProposal.setVisible(true);
    }
    /**
     * To set the default focus to the btnCancel while opening the screen
     */
    private void setDefaultFocus() {
        fundingProposalForm.btnCancel.requestFocusInWindow();
        if (cvFundingProposal.size() > 0) {
            fundingProposalForm.tblFundingProposal.setRowSelectionInterval(0 , 0);
        }
    }
    
    /**
     * Depending on the function type it makes the buttons enabled aor disabled
     */
    public void formatFields() {
        if (DISPLAY_MODE==getFunctionType()) {
            fundingProposalForm.btnOk.setEnabled(false);
            fundingProposalForm.btnAdd.setEnabled(false);
        } else {
            fundingProposalForm.btnOk.setEnabled(true);
            fundingProposalForm.btnAdd.setEnabled(true);
        }
        fundingProposalForm.btnDelete.setEnabled(false);
    }
    /**
     * No implementation required
     * @return component
     */
    public java.awt.Component getControlledUI() {
        return null;
    }
    
    /**
     * No implementation required
     */
    public Object getFormData() {
        return null;
    }
    /**
     * Inserts all the new items and deletes the deleted items from the query engine
     * Also updates the award details from Proposals selected
     */
    public void saveFormData() {
        try{
            dlgFundingProposal.setCursor( new Cursor(Cursor.WAIT_CURSOR));
            // inserts all the inserted items to the queryengine
            for (int index=0;index<cvFundingProposal.size();index++) {
                AwardFundingProposalBean bean =
                (AwardFundingProposalBean)cvFundingProposal.get(index);
                if (bean.getAcType()==null) {
                    continue;
                }
                if (bean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                    queryEngine.insert(queryKey,bean);
                }
            }
            //deletes all the deleted items from the query engine
            for (int index=0;index<cvDeletedData.size(); index++) {
                AwardFundingProposalBean bean =
                (AwardFundingProposalBean)cvDeletedData.get(index);
                try {
                    bean.setAcType(TypeConstants.DELETE_RECORD);
                    queryEngine.delete(queryKey,bean);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try{
                // To save proposal data to award.
                updateProposalDetailsToAward();
                
                // checks whether the award status has to be made hold,
                // and if so, it fires the bean event
                if(isAwardStatusToHold()) {
                    BeanEvent beanEvent = new BeanEvent();
                    AwardDetailsBean msgBean = new AwardDetailsBean();
                    msgBean.setStatusCode(HOLD);
                    beanEvent.setBean(msgBean);
                    beanEvent.setSource(this);
                    fireBeanUpdated(beanEvent);
                }
            }catch (CoeusClientException exception){
                CoeusOptionPane.showDialog(exception);
                exception.printStackTrace();
            }
        } finally{
            dlgFundingProposal.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ));
        }
    }
    
    /**
     * Sets the data to the form
     */
    public void setFormData(Object data) {
        try {
            cvFundingProposal = queryEngine.executeQuery(queryKey,
            AwardFundingProposalBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            String sortCriteria [] = {"sequenceNumber","rowId"};
            if (cvFundingProposal !=null) {
                cvFundingProposal.sort(sortCriteria,false);
            }
            // To find the maximum of the row ids
            int maxRowId=0;
            for (int index=0;index<cvFundingProposal.size();index++) {
                AwardFundingProposalBean bean = (AwardFundingProposalBean) cvFundingProposal.get(index);
                if (maxRowId<bean.getRowId()) {
                    maxRowId = bean.getRowId();
                }
            }
            rowId = maxRowId; // Assigning maximum rowid to rowId
            cvAwardsDetails = queryEngine.executeQuery(queryKey,
            AwardDetailsBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            awardHeaderForm.setFormData((AwardDetailsBean)cvAwardsDetails.elementAt(0));
            //Case #2336 start
            awardHeaderForm.lblSequenceNumberValue.setText(EMPTY_STRING+awardBaseBean.getSequenceNumber());
            //Case #2336 end
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (cvFundingProposal!=null) {
            fundingProposalTableModel.setData(cvFundingProposal);
        }
    }
    /**
     * Sets the column properties
     */
    private void setColumnData(){
        JTableHeader tableHeader = fundingProposalForm.tblFundingProposal.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        fundingProposalForm.tblFundingProposal.setAutoResizeMode(
        javax.swing.JTable.AUTO_RESIZE_OFF);
        fundingProposalForm.tblFundingProposal.setRowHeight(22);
        int lastSeqNum = -1;
        for(int index = 0; index < cvFundingProposal.size(); index++) {
            awardFundingProposalBean = (AwardFundingProposalBean)cvFundingProposal.get(index);
            if(lastSeqNum != awardFundingProposalBean.getSequenceNumber()) {
                //New seq number, so increase height and get the new seq num.
                fundingProposalForm.tblFundingProposal.setRowHeight(index, 44);
                lastSeqNum = awardFundingProposalBean.getSequenceNumber();
            }
        }
        fundingProposalForm.tblFundingProposal.setShowHorizontalLines(false);
        fundingProposalForm.tblFundingProposal.setShowVerticalLines(false);
        fundingProposalForm.tblFundingProposal.setOpaque(false);
        fundingProposalForm.tblFundingProposal.setBackground(
        (java.awt.Color) javax.swing.UIManager.getDefaults().get(
        TAB_BACKGRND));
        fundingProposalForm.tblFundingProposal.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = fundingProposalForm.tblFundingProposal.getColumnModel().getColumn(PROPOSAL_COLUMN);
        /*Added for the case#3186-Add fields to the Funding Proposals Screen in the Awards module -start*/
//      column.setPreferredWidth(71);
        column.setPreferredWidth(101);
        /*Added for the case#3186-Add fields to the Funding Proposals Screen in the Awards module -end*/
        column.setResizable(false);
        column.setCellRenderer(fundingProposalRenderer);
        
        
        column = fundingProposalForm.tblFundingProposal.getColumnModel().
        getColumn(SEQ_COLUMN);
        /*Added for the case#3186-Add fields to the Funding Proposals Screen in the Awards module -start*/
//      column.setPreferredWidth(30);
        column.setPreferredWidth(55);
        /*Added for the case#3186-Add fields to the Funding Proposals Screen in the Awards module -end*/
        column.setResizable(false);
        column.setCellRenderer(fundingProposalRenderer);
        
        
        column = fundingProposalForm.tblFundingProposal.getColumnModel().
        getColumn(TYPE_COLUMN);
        column.setPreferredWidth(140);
        column.setResizable(false);
        column.setCellRenderer(fundingProposalRenderer);
        /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - start*/
        
        column = fundingProposalForm.tblFundingProposal.getColumnModel().
        getColumn(PI);
        column.setPreferredWidth(140);
        column.setResizable(false);
        column.setCellRenderer(fundingProposalRenderer);
        
        column = fundingProposalForm.tblFundingProposal.getColumnModel().
        getColumn(LEAD_UNIT);
        column.setPreferredWidth(80);
        column.setResizable(false);
        column.setCellRenderer(fundingProposalRenderer);

        /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - end*/
        
        column = fundingProposalForm.tblFundingProposal.getColumnModel().
        getColumn(START_DATE_COLUMN);
        column.setPreferredWidth(80);
        column.setResizable(false);
        column.setCellRenderer(fundingProposalRenderer);
        
        column = fundingProposalForm.tblFundingProposal.getColumnModel().
        getColumn(END_DATE_COLUMN);
        column.setPreferredWidth(75);
        column.setResizable(false);
        column.setCellRenderer(fundingProposalRenderer);
        
        column = fundingProposalForm.tblFundingProposal.getColumnModel().
        getColumn(DIRECT_COST_COLUMN);
        column.setPreferredWidth(100);
        column.setResizable(false);
        column.setCellRenderer(fundingProposalRenderer);
        
        column = fundingProposalForm.tblFundingProposal.getColumnModel().
        getColumn(INDIRECT_COST_COLUMN);
        column.setPreferredWidth(100);
        column.setResizable(false);
        column.setCellRenderer(fundingProposalRenderer);
        
        column = fundingProposalForm.tblFundingProposal.getColumnModel().
        getColumn(TOTAL_COST_COLUMN);
        column.setPreferredWidth(100);
        column.setResizable(false);
        column.setCellRenderer(fundingProposalRenderer);
    }
    /**
     * No implementation required
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    /**
     * Removes the references of the class variables
     */
    public void cleanUp() {
        fundingProposalForm = null;
        dlgFundingProposal = null;
        mdiForm = null;
        awardBaseBean = null;
        awardFundingProposalBean =null;
        cvFundingProposal = null;
        cvAwardsDetails = null;
        cvDeletedData = null;
        cvInsertedBeans = null;
        fundingProposalTableModel = null;
        fundingProposalRenderer = null;
        awardHeaderForm = null;
        selectionModel = null;
        fundingProposalSearch = null;
    }
    /**
     * Handlers for the buttons
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        if (source == fundingProposalForm.btnAdd) {
            performAddAction();
        } else if (source == fundingProposalForm.btnCancel) {
            performCancelAction();
        } else if (source == fundingProposalForm.btnDelete) {
            performDeleteAction();
        } else if (source == fundingProposalForm.btnOk) {
            saveFormData();
            dlgFundingProposal.dispose();
        }
    }
    /**
     * Handler for cancel button
     * It is also called when esc is pressed or the Window close button[X] is pressed
     */
    private void performCancelAction() {

        if (modified) {
            int selectionOption = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("budget_saveChanges_exceptionCode.1210"),CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if (selectionOption==CoeusOptionPane.SELECTION_CANCEL) {
                return;
            } else if (selectionOption == CoeusOptionPane.SELECTION_YES) {
                saveFormData();
            }
        }
        dlgFundingProposal.dispose();
    }
    
    /*
     * Updates the award sheet details with details from proposal
     *
     */
    private void updateProposalDetailsToAward() throws CoeusClientException {
        cvInsertedBeans.sort("rowId",false);
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_DATA);
        requesterBean.setDataObject(cvInsertedBeans);
        AppletServletCommunicator comm= new AppletServletCommunicator(connectTo,
        requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        Hashtable htData = null;
        if(responderBean.isSuccessfulResponse()) {
            htData = (Hashtable)responderBean.getDataObject();
        }else{
            throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
        // Call the AwardController method to update award details
        if(htData!=null) {
            // Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
//            updateAwardDetailsFromProposal(htData,getFunctionType());
            updateAwardDetailsFromProposal(htData, getFunctionType(),true);
            // Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End

        }
    }
    /**
     * Method for handling Add action.
     * Adds rows to the table.
     */
    private void performAddAction() {
        String propNumber=null;
        double directCost = 0.0;
        double indirectCost = 0.0;
        Date startDate=null;
        Date endDate=null;
        String propTypeDesc=null;
        String propSeqNum = null;
        int propStatusCode=0;
        /*Added for case 3186-Add fields to the Funding Proposals Screen in the Awards module-start*/
        String unitNumber = "";
        String piName = null;
        /*Added for case 3186-Add fields to the Funding Proposals Screen in the Awards module-end*/
        try {
            
            fundingProposalSearch = new FundingProposalSearch(mdiForm,"proposalsearch",1);
            fundingProposalSearch.showSearchWindow();
            HashMap hmSelectedProp = fundingProposalSearch.getSelectedRow();
            
            if (hmSelectedProp != null && !hmSelectedProp.isEmpty() ) {
                propNumber = (Utils.convertNull(hmSelectedProp.get(
                        PROPOSAL_NUMBER))).trim();
                propSeqNum = (Utils.convertNull(hmSelectedProp.get(
                        SEQUENCE_NUMBER))).trim();
                // To check whether the selected proposal already exists
                // Checks sequenceNumber,proposal number and proposal sequence number for duplicate checking
                Equals eqAwSeqNum =new Equals("sequenceNumber",new Integer(awardBaseBean.getSequenceNumber()));
                Equals eqPropNum =new Equals("proposalNumber",propNumber);
                Equals eqPropSeqNum =new Equals("proposalSequenceNumber",new Integer(propSeqNum));
                And awSeqAndPropNum =new And(eqAwSeqNum,eqPropNum);
                And awSeqAndPropNumAndPropoSeqNum =new And(awSeqAndPropNum,eqPropSeqNum);
                
                CoeusVector cvFilteredFP = cvFundingProposal.filter(awSeqAndPropNumAndPropoSeqNum);
                int filterSize=cvFilteredFP.size();
                if(filterSize>0) {
                    CoeusOptionPane.showErrorDialog("The proposal '"+propNumber+"' was selected already");
                    return;
                }
                /*Added for case#3186-Add fields to the Funding Proposals Screen in the Awards module-start*/
                unitNumber= hmSelectedProp.get(UNIT_NUMBER) == null ? "" :
                    hmSelectedProp.get(UNIT_NUMBER).toString();
                if(hmSelectedProp.get(PERSON_NAME)!=null){
                    piName = hmSelectedProp.get(PERSON_NAME).toString();
                }
                /*Added for case#3186-Add fields to the Funding Proposals Screen in the Awards module-End*/
                
                if (hmSelectedProp.get(TOTAL_DIRECT_COST_TOTAL)!=null) {
                    directCost = Double.parseDouble(hmSelectedProp.get(
                            TOTAL_DIRECT_COST_TOTAL).toString());
                } else {
                    directCost = 0.0;
                }
                if (hmSelectedProp.get(TOTAL_INDIRECT_COST_TOTAL) == null) {
                    indirectCost = 0.0;
                } else {
                    indirectCost = Double.parseDouble(hmSelectedProp.get(
                            TOTAL_INDIRECT_COST_TOTAL).toString());
                }
                if (hmSelectedProp.get(REQUESTED_START_DATE_TOTAL)==null) {
                    startDate = null;
                } else {
                    String strDate = hmSelectedProp.get(
                            REQUESTED_START_DATE_TOTAL).toString();
                    //COEUSQA-1477 Dates in Search Results - Start
                    strDate = formatDateForSearchResults(strDate);
                    //java.util.Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                    java.util.Date date = (java.util.Date)simpleDateFormat.parse(strDate);
                    //COEUSQA-1477 Dates in Search Results - End
                    startDate = new java.sql.Date(date.getTime());
                }
                if (hmSelectedProp.get(REQUESTED_END_DATE_TOTAL)==null) {
                    endDate = null;
                }else{
                    String enddt = hmSelectedProp.get(REQUESTED_END_DATE_TOTAL).toString();
                    //COEUSQA-1477 Dates in Search Results - Start
                    enddt = formatDateForSearchResults(enddt);
                    //java.util.Date date = (java.util.Date)simpleDateFormat.parse(dateUtils.restoreDate(enddt,DATE_SEPARATERS));
                    java.util.Date date = (java.util.Date)simpleDateFormat.parse(enddt);
                    //COEUSQA-1477 Dates in Search Results - End
                    
                    endDate = new java.sql.Date(date.getTime());
                }
                propStatusCode =  hmSelectedProp.get(STATUS_CODE) == null ? 0 :
                    Integer.parseInt(hmSelectedProp.get(STATUS_CODE).toString());
                propTypeDesc = Utils.convertNull(hmSelectedProp.get(
                        PROPOSAL_TYPE_DESC));
                if(fundingProposalSearch.isAwardStatusToBeMadeHold()) {
                    setAwardStatusToHold(true);
                }
            } else { // no proposal selected
                return;
            }
            
            // create a new bean and assign values to it
            AwardFundingProposalBean newBean = new AwardFundingProposalBean();
            newBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            newBean.setProposalNumber(propNumber);
            newBean.setRowId(++rowId);
            newBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
            newBean.setProposalSequenceNumber(Integer.parseInt(propSeqNum));
            newBean.setProposalTypeDescription(propTypeDesc);
            /*Added for case#3186-Add fields to the Funding Proposals Screen in the Awards module-start*/
            newBean.setLeadUnit(unitNumber);
            newBean.setPiName(piName);
            /*Added for case#3186-Add fields to the Funding Proposals Screen in the Awards module-end*/
            newBean.setTotalDirectCostTotal(directCost);
            newBean.setTotalInDirectCostTotal(indirectCost);
            newBean.setRequestStartDateTotal(startDate);
            newBean.setRequestEndDateTotal(endDate);
            newBean.setProposalStatusCode(propStatusCode);
            newBean.setAcType(TypeConstants.INSERT_RECORD);
            // Added COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
            if(isIPHasNewInvestigator(propNumber)){
                MessageFormat formatter = new MessageFormat("");
                String message = formatter.format(
                        coeusMessageResources.parseMessageKey("awardFundingInvSync_exceptionCode.1000"),"'",propNumber,"'");
                int option = CoeusOptionPane.showQuestionDialog(message,
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                switch( option ){
                    case JOptionPane.YES_OPTION:
                        newBean.setCanSyncIPInvToAward(true);
                        newBean.setCanSyncIPCreditToAward(true);
                }
            }else{
                newBean.setCanSyncIPCreditToAward(true);
            }
            // Added COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
            
            cvInsertedBeans.add(newBean);
            modified = true;
            cvFundingProposal.insertElementAt(newBean, 0);
            fundingProposalTableModel.fireTableRowsInserted(0 , 0 );
            if (cvFundingProposal.size()>1 && ((AwardFundingProposalBean)
            cvFundingProposal.get(1)).getSequenceNumber()==newBean.getSequenceNumber()) {
                fundingProposalForm.tblFundingProposal.setRowHeight(1,22);
            }
            fundingProposalForm.tblFundingProposal.setRowHeight(0, 44);
            fundingProposalForm.tblFundingProposal.setRowSelectionInterval(0 , 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Method for handling Delete action.
     * Deletes user created rows
     */
    private void performDeleteAction() {
        int rowIndex = fundingProposalForm.tblFundingProposal.getSelectedRow();
        if(rowIndex != -1 && rowIndex >= 0){
            String mesg = coeusMessageResources.parseMessageKey(
                "costSharingDistribution__exceptionCode.1513");
            int selectedOption = CoeusOptionPane.showQuestionDialog(mesg,
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                
                cvDeletedData.addElement(cvFundingProposal.elementAt(rowIndex));
                cvInsertedBeans.remove(cvFundingProposal.elementAt(rowIndex));
                cvFundingProposal.remove(rowIndex);
                
                fundingProposalTableModel.fireTableRowsDeleted(rowIndex, rowIndex);
                if (cvFundingProposal.size()>0) {
                    if (rowIndex == 0) {
                        fundingProposalForm.tblFundingProposal.setRowHeight(0,44);
                    }
                }
                if (cvFundingProposal.size()<1) {
                    fundingProposalForm.btnDelete.setEnabled(false);
                } else {
                    fundingProposalForm.tblFundingProposal.setRowSelectionInterval(0,0);
                    if (!fundingProposalForm.btnDelete.isEnabled()) {
                        fundingProposalForm.btnCancel.requestFocusInWindow();
                    }
                }
                modified = true;
            } // selection is yes ends 
        } // rowindex is valid ends
    }
    
    /**
     * Checks whether the selected row is  inserted by the user (i.e. acType is "I")
     * Then only the delete button is made enabled.
     * Otherwise it is made disabled.
     */
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if( !listSelectionEvent.getValueIsAdjusting() ){
            int selectedRow = fundingProposalForm.tblFundingProposal.getSelectedRow();
            if (selectedRow != -1) {
                AwardFundingProposalBean selectedBean =
                (AwardFundingProposalBean)cvFundingProposal.get(selectedRow);
                if (TypeConstants.INSERT_RECORD.equals(selectedBean.getAcType())) {
                    fundingProposalForm.btnDelete.setEnabled(true);
                } else {
                    fundingProposalForm.btnDelete.setEnabled(false);
                }
            }
        }
    }
    
    /**
     * Getter for property awardStatusToHold.
     * @return Value of property awardStatusToHold.
     */
    public boolean isAwardStatusToHold() {
        return awardStatusToHold;
    }
    
    /**
     * Setter for property awardStatusToHold.
     * @param awardStatusToHold New value of property awardStatusToHold.
     */
    public void setAwardStatusToHold(boolean awardStatusToHold) {
        this.awardStatusToHold = awardStatusToHold;
    }
    
    /**
     * Table model for the table
     */
    public class FundingProposalTableModel extends AbstractTableModel {
        /* Added for the case #3186 - Add fields to the Funding Proposals Screen in the Awards module -start-*/
//         private String colNames[] = {"Proposal","Seq","Type",
//        "Start Date","End Date","Direct cost","Indirect Cost","Total Cost",};
        
        private String colNames[] = {"Proposal","Seq","Type","PI","Lead Dept.",
        "Start Date","End Date","Direct cost","Indirect Cost","Total Cost",};
        /* Added for the case #3186 - Add fields to the Funding Proposals Screen in the Awards module -end-*/
        public int getColumnCount() {
            return colNames.length;
        }
        /**
         * Returns the number of rows in the table
         */
        public int getRowCount() {
            if (cvFundingProposal==null) {
                return 0;
            }
            return cvFundingProposal.size();
        }
        /**
         * Sets value at a particular cell
         */
        public String getColumnName(int col) {
            return colNames[col];
        }
        
        /**
         * If cell editable, returns true, else returns false
         */
        public boolean isCellEditable(int row, int col) {
            return false;
        }
        
        /** Returns the value for the cell at column and row.
         * @param rowIndex the rowIndex whose value is to be queried
         * @param columnIndex the columnIndex whose value is to be queried
         * @return the value Object at the specified cell
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            AwardFundingProposalBean awardFundingProposalBean=
            (AwardFundingProposalBean)cvFundingProposal.get(rowIndex);
            switch(columnIndex) {
                case PROPOSAL_COLUMN:
                    return awardFundingProposalBean.getProposalNumber();
                case SEQ_COLUMN:
                    return new Integer(awardFundingProposalBean.
                    getProposalSequenceNumber());
                case TYPE_COLUMN :
                    return awardFundingProposalBean.getProposalTypeDescription();
                /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - start*/
                case PI :
                    return awardFundingProposalBean.getPiName();
                case LEAD_UNIT :
                    return awardFundingProposalBean.getLeadUnit();
                /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - end*/
                case START_DATE_COLUMN:
                    if (awardFundingProposalBean.getRequestStartDateTotal()!=null) {
                        return awardFundingProposalBean.getRequestStartDateTotal();
                    }
                    return EMPTY_STRING;
                case END_DATE_COLUMN:
                    if (awardFundingProposalBean.getRequestEndDateTotal()!=null) {
                        return awardFundingProposalBean.getRequestEndDateTotal();
                    }
                    return EMPTY_STRING;
                case DIRECT_COST_COLUMN:
                    return new Double(awardFundingProposalBean.
                    getTotalDirectCostTotal());
                case INDIRECT_COST_COLUMN:
                    return new Double(awardFundingProposalBean.
                    getTotalInDirectCostTotal());
                case TOTAL_COST_COLUMN:
                    return new Double(awardFundingProposalBean.
                    getTotalDirectCostTotal() +
                    awardFundingProposalBean.getTotalInDirectCostTotal());
                default :
                    return null;
            }
        }
        
        public void setValueAt(Object value, int row, int column) {
        }
        /** sets the data to be displayed by the table.
         * @param cvFundingProposal Data to be displayed.
         */
        public void setData(CoeusVector cvFundingProposal) {
            cvFundingProposal = cvFundingProposal;
        }
        
    } // Table model class ends
    
    /**
     * Renderer for the Budget Period Line Items.
     */
    public class FundingProposalRenderer extends DefaultTableCellRenderer {
        private DollarCurrencyTextField dollarCurrencyTextField;
        private JLabel lblGroup;
        
        private JLabel lblValue,lblDollarTextField;
        private JPanel pnlGroup;
        private int lastSeqNum = -1;
        
        FundingProposalRenderer() {
            dollarCurrencyTextField = new DollarCurrencyTextField();
            
            dollarCurrencyTextField.setHorizontalAlignment(
            DollarCurrencyTextField.LEFT);
            pnlGroup = new JPanel();
            pnlGroup.setLayout(new java.awt.GridLayout(2, 1));
            lblGroup = new JLabel();
            lblGroup.setFont(CoeusFontFactory.getLabelFont());
            
            lblDollarTextField = new JLabel();
            lblDollarTextField.setOpaque(true);
            lblDollarTextField.setBorder(new EmptyBorder(0,0,0,0));
            
            lblValue = new JLabel();
            lblValue.setOpaque(true);
            lblValue.setBorder(new EmptyBorder(0,0,0,0));
            pnlGroup.add(lblGroup);
            pnlGroup.add(lblValue);
        }
        /**
         * Renderer component
         */
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setHorizontalAlignment(JLabel.LEFT);
            switch (column) {
                /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - start*/
                case PI:
                    //Added for COEUSDEV-182 : Award - Funding Proposal screen rendering error build 6/15/09 - Start
                    //To break when Pi value 
                    break;
                    //COEUSDEV-182 : END
                case LEAD_UNIT:
                    //Added for COEUSDEV-182 : Award - Funding Proposal screen rendering error build 6/15/09 - Start
                    //To break when lead unit value 
                    break;
                    //COEUSDEV-182 : END
                /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - end*/
                case DIRECT_COST_COLUMN:
                case INDIRECT_COST_COLUMN:
                case TOTAL_COST_COLUMN:
                    //Added for COEUSDEV-182 : Award - Funding Proposal screen rendering error build 6/15/09 - Start
                    if(value == null){
                        break;
                    }
                    //COEUSDEV-182 : END
                    dollarCurrencyTextField.setText(value.toString());
                    if(isSelected){
                        lblDollarTextField.setBackground(fundingProposalForm.
                        tblFundingProposal.getSelectionBackground());
                        lblDollarTextField.setForeground(fundingProposalForm.
                        tblFundingProposal.getSelectionForeground());
                    }
                    else {
                        lblDollarTextField.setBackground(
                        fundingProposalForm.tblFundingProposal.getBackground());
                        lblDollarTextField.setForeground(
                        fundingProposalForm.tblFundingProposal.getForeground());
                    }
                    break;
                case START_DATE_COLUMN:
                case END_DATE_COLUMN:
                    if (!value.equals(EMPTY_STRING)) {
                        value = dateUtils.formatDate(value.toString(),DATE_FORMAT);
                    }
                    break;
            }
            JComponent retComponent;
            retComponent = (JComponent)super.getTableCellRendererComponent(table,
            value, isSelected, hasFocus, row, column);
            if(row > 0) {
                awardFundingProposalBean = (AwardFundingProposalBean)
                cvFundingProposal.get(row - 1);
                lastSeqNum = awardFundingProposalBean.getSequenceNumber();
            }else {
                lastSeqNum = -1;
            }
            awardFundingProposalBean = (AwardFundingProposalBean)
            cvFundingProposal.get(row);
            int seqNum = awardFundingProposalBean.getSequenceNumber();
            //Checking for Begining of New Sequence number.
            if(lastSeqNum != seqNum) {
                lblGroup.setText(EMPTY_STRING);
                switch (column) {
                    /*Added for the case#3186-Add fields to the Funding Proposals Screen in the Awards module -start*/
//                  case PROPOSAL_COLUMN:
//                        lblGroup.setText(" Award Sequ");
//                        lblGroup.setForeground(Color.BLACK);
//                        break;
//                  case SEQ_COLUMN:
//                        lblGroup.setText("ence");
//                        lblGroup.setForeground(Color.BLACK);
//                        break;
//                  case TYPE_COLUMN:
//                        lblGroup.setText(" Number:  "+seqNum);
//                        lblGroup.setForeground(Color.BLACK);
//                        break;
                    case PROPOSAL_COLUMN:
                        lblGroup.setText(" Award Sequence");
                        lblGroup.setForeground(Color.BLACK);
                        break;
                    case SEQ_COLUMN:
                        lblGroup.setText(" Number:");
                        lblGroup.setForeground(Color.BLACK);
                        break;
                    case TYPE_COLUMN:
                        lblGroup.setText(" "+seqNum);
                        lblGroup.setForeground(Color.BLACK);
                        break;
                        /*Added for the case#3186-Add fields to the Funding Proposals Screen in the Awards module -end*/
                    default :
                }
                switch (column) {
                    case PROPOSAL_COLUMN:
                    case SEQ_COLUMN:
                    case TYPE_COLUMN:
                    /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - start*/
                    case PI:
                    case LEAD_UNIT:
                    /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - end*/
                    case START_DATE_COLUMN:
                    case END_DATE_COLUMN:
                        lblValue.setHorizontalAlignment(JLabel.LEFT);
                        lblValue.setText(((JLabel)retComponent).getText());
                        break;
                    case DIRECT_COST_COLUMN:
                    case INDIRECT_COST_COLUMN:
                    case TOTAL_COST_COLUMN:
                        lblValue.setText(dollarCurrencyTextField.getText());
                }
                if (isSelected) {
                    lblValue.setForeground(fundingProposalForm.tblFundingProposal.
                    getSelectionForeground());
                    lblValue.setBackground(fundingProposalForm.tblFundingProposal.
                    getSelectionBackground());
                } else {
                    lblValue.setForeground(fundingProposalForm.tblFundingProposal.
                    getForeground());
                    lblValue.setBackground((java.awt.Color) javax.swing.UIManager.
                    getDefaults().get(TAB_BACKGRND));
                }
                return pnlGroup;
                
            } // End of check for begining of new Sequnece number.
            
            if(column == DIRECT_COST_COLUMN || column == INDIRECT_COST_COLUMN ||
            column == TOTAL_COST_COLUMN ) {
                lblDollarTextField.setText(dollarCurrencyTextField.getText());
                return lblDollarTextField;
            }
            if (isSelected) {
                retComponent.setForeground(fundingProposalForm.tblFundingProposal.
                getSelectionForeground());
                retComponent.setBackground(fundingProposalForm.tblFundingProposal.
                getSelectionBackground());
            }else {
                retComponent.setForeground(fundingProposalForm.tblFundingProposal.
                getForeground());
                retComponent.setBackground((java.awt.Color) javax.swing.UIManager.
                getDefaults().get(TAB_BACKGRND));
            }
            retComponent.setBorder(new EmptyBorder(0,0,0,0));
            return retComponent;
        }
    }// Table renderer class ends
    
    //COEUSQA-1477 Dates in Search Results - Start
    /**
     * This method format the date which can be passed as a 
     * parameter to be Date() constructor.
     * @param value
     * @return returns dateValue which is formatted date value
     */
    public String formatDateForSearchResults(String value){
        String dateValue = "";
        String validDateFormat = "";
        String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT);
        if(dateFormat.length()>0){
            DateUtils dtUtils = new DateUtils();
            HashMap hmDateFormats = dtUtils.loadFormatsForSearchResults();
            if(hmDateFormats.get(dateFormat)!=null){
                validDateFormat = hmDateFormats.get(dateFormat).toString();
            }
            if(!(validDateFormat.length()>0)){
                //assign default date value
                dateFormat = CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH;
            }
        }else{
            dateFormat = CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH;
        }
        if(value!=null && value.length()>0){
            //to check if date contains user defined delimiter
            if(value.contains(DATE_FORMAT_USER_DELIMITER)){
                 //to replace the user defined delimiter to default date format
                 dateValue = value.replaceAll(DATE_FORMAT_USER_DELIMITER,DATE_FORMAT_DELIMITER);
            }else{
                dateValue = value;
            }
            if(dateValue.indexOf(DATE_FORMAT_DELIMITER)>0){
                String [] dateComponents = dateValue.split(DATE_FORMAT_DELIMITER);
                if((dateComponents[dateComponents.length-1]).length()<=4){
                    dateValue = fetchDateValuesForSearchResults(dateComponents, dateFormat);
                }else{
                    String date = (dateComponents[dateComponents.length-1]);
                    String patternValue = date.substring(0,date.indexOf(" "));
                    String time = date.substring((date.indexOf(patternValue)+patternValue.length()),date.length());
                    dateComponents[dateComponents.length-1] = patternValue;
                    date = fetchDateValuesForSearchResults(dateComponents, dateFormat);
                    dateValue = date+time;
                }
            }
        }
        return dateValue;
    }

    /**
     * This method format the date which can be passed as a 
     * parameter to be Date() constructor.
     * @param dateComponents
     * @param dateFormat
     * @return returns dateValue which is formatted date value
     */
    public String fetchDateValuesForSearchResults(String[] dateComponents, String dateFormat) {
        HashMap hmDateFormat = new HashMap(4);
        if(dateComponents!=null && dateFormat!=null){
            if(dateFormat.contains(DATE_FORMAT_USER_DELIMITER)){
                dateFormat = dateFormat.replaceAll(DATE_FORMAT_USER_DELIMITER,DATE_FORMAT_DELIMITER);
            }
            //to check whether the date contains "/"
            if(dateFormat.indexOf(DATE_FORMAT_DELIMITER)>0){
                String [] dateDefaultComponents = dateFormat.split(DATE_FORMAT_DELIMITER);
                Integer counter = new Integer(0);
                //to add to the collection object if data matches the repective delimiter
                for(String data:dateDefaultComponents){
                    //to remove "fm" from "Month"
                    if(data.contains("fm")){
                        data = data.replaceAll("fm","");
                    }
                    if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_YEAR_DELIMITER)){
                        hmDateFormat.put("Year",counter);
                    }else if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_MONTH_DELIMITER)){
                        hmDateFormat.put("Month",counter);
                    }else if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_DATE_DELIMITER)){
                        hmDateFormat.put("Date",counter);
                    }
                    counter++;
                }
            }
        }
        //to format the date if it month is in Words
        Integer month=01;
        if(dateComponents[(Integer)hmDateFormat.get("Month")].length()>2){
            Enumeration monthNames =  DateUtils.getMonths().keys();
            while( monthNames.hasMoreElements() ){
                String monthName = (String) monthNames.nextElement();
                if( monthName.startsWith(dateComponents[(Integer)hmDateFormat.get("Month")]) ){
                    String monthNumber = (String) DateUtils.getMonths().get(monthName);
                    month = Integer.parseInt(monthNumber);
                    break;
                }
            }
        }else{
            month = (Integer)hmDateFormat.get("Month");
        }
        //formation of the date in the default date format
        String date = dateComponents[(Integer)hmDateFormat.get("Year")]+DATE_FORMAT_DELIMITER
                +month
                +DATE_FORMAT_DELIMITER+dateComponents[(Integer)hmDateFormat.get("Date")];
        return date;
    }
    //COEUSQA-1477 Dates in Search Results - End

    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
    /**
     * Method to check checks if the proposal being linked has any investigators that are not in award already
     * @param propNumber 
     * @return 
     */
    private boolean isIPHasNewInvestigator(String propNumber) throws CoeusException, CoeusClientException {
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType('!');
        requesterBean.setDataObject(propNumber);
        AppletServletCommunicator comm= new AppletServletCommunicator(connectTo,requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        Hashtable htData = null;
        if(responderBean.hasResponse()) {
            CoeusVector cvIPInvestigator = (CoeusVector)responderBean.getDataObject();
            CoeusVector cvAwardInvestigators = queryEngine.getDetails(queryKey,AwardInvestigatorsBean.class);
            if((cvAwardInvestigators == null || cvAwardInvestigators.isEmpty())
            && (cvIPInvestigator != null && !cvIPInvestigator.isEmpty())){
                return true;
            }
            
            if(cvIPInvestigator != null && !cvIPInvestigator.isEmpty()){
                for(Object ipInvestigator : cvIPInvestigator){
                    InstituteProposalInvestigatorBean ipInvestigatorDetails =
                            (InstituteProposalInvestigatorBean)ipInvestigator;
                    And andPerson = new And(new Equals("personId",ipInvestigatorDetails.getPersonId()),
                            CoeusVector.FILTER_ACTIVE_BEANS);
                    CoeusVector cvFilterAwardInv = cvAwardInvestigators.filter(andPerson);
                    if(cvFilterAwardInv == null || cvFilterAwardInv.isEmpty()){
                        return true;
                    }
                }
            }
        }else{
            throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
        
        return false;
    }
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
}
