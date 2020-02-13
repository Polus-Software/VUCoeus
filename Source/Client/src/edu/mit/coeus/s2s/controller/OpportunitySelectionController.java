/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * OpportunitySelectionController.java
 *
 */

package edu.mit.coeus.s2s.controller;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.s2s.bean.DBOpportunityInfoBean;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.gui.OpportunitySelectionForm;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;

import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author  Geo Thomas
 * Created on February 15, 2005, 12:18 PM
 */
public class OpportunitySelectionController implements ActionListener,
                                                    ListSelectionListener,
                                                    MouseListener{
    private OpportunitySelectionForm oppSelForm;
    private CoeusDlgWindow dlgOppSelForm;
    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm;
    private OppSelectionTableModel oppSelTableModel;
    private static final int WIDTH=690;
    private static final int HEIGHT = 420;
    private static final int OPP_ID_COLUMN=0;
    private static final int OPP_TITLE_COLUMN=1;
    private static final int CFDA_COLUMN=2;
    private static final int COMPETITION_ID_COLUMN=3;
    private static final int STARTING_DATE_COLUMN=4;
    private static final int CLOSING_DATE_COLUMN=5;
    private static final String EMPTY_STRING = "";
    private static final String S2S_SERVLET = CoeusGuiConstants.CONNECTION_URL+ "/S2SServlet";

    private static String OPPORTUNITY_ID = "Opportunity Id";
    private static String OPPORTUNITY_TITLE = "Opportunity Title";
    private static String CFDA_NUMBER = "CFDA Number";
    private static String COMPETITION_ID = "Competition Id";
    private static String STARTING_DATE = "Starting Date";
    private static String CLOSING_DATE = "Closing Date";
        
    private boolean saveNContinue;
    private String submissionTitle;
    private Timestamp awUpdateTimestamp;
    private S2SHeader oppHeader;
    private char functionType;
    private String errorMessage;
    /** Creates a new instance of OpportunitySelectionController */
    public OpportunitySelectionController(CoeusAppletMDIForm mdiForm) throws CoeusException {
        this.mdiForm = mdiForm;
        oppSelForm = new OpportunitySelectionForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setFormData(null);
        setColumnData();
        postInitComponents();
        formatFields();
    }
    public OpportunitySelectionController() throws CoeusException {
        this(CoeusGuiConstants.getMDIForm());
    }    
    public void formatFields() {
        oppSelForm.lblCfdaNumber.setFont(CoeusFontFactory.getLabelFont());
        oppSelForm.lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        oppSelForm.lblOppId.setFont(CoeusFontFactory.getLabelFont());
        oppSelForm.lblProposalNumber.setFont(CoeusFontFactory.getLabelFont());
    }
    
    public java.awt.Component getControlledUI() {
        return oppSelForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public OpportunityInfoBean getSltdOpportunity(){
        return (OpportunityInfoBean)oppSelTableModel.getValueAt(
                        oppSelForm.tblOppSel.getSelectedRow());
    }
    
    public void registerComponents() {
        oppSelTableModel = new OppSelectionTableModel();
        oppSelForm.tblOppSel.setModel(oppSelTableModel);
        oppSelForm.tblOppSel.getSelectionModel().addListSelectionListener(this);
        oppSelForm.btnOK.addActionListener(this);
        oppSelForm.btnCancel.addActionListener(this);
        oppSelForm.lblInstructionUrl.addMouseListener(this);
        oppSelForm.lblSchemaUrl.addMouseListener(this);
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        
        String instrMsg = coeusMessageResources.parseMessageKey("s2sOppFrmInstr_Code.1001");
        if(errorMessage!=null){
            oppSelForm.txtInstrVal.setText(errorMessage);
            oppSelForm.txtInstrVal.setForeground(Color.RED);
        }else{
            oppSelForm.txtInstrVal.setText(instrMsg);
        }
        oppSelTableModel.setData((Vector)data);
        if(data!=null && ((Vector)data).size()==1)
            oppSelForm.tblOppSel.setRowSelectionInterval(0, 0 );
        
    }
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        int sltdIndex = oppSelForm.tblOppSel.getSelectedRow();
        if(sltdIndex==-1) throw new CoeusException(
            coeusMessageResources.parseMessageKey("s2soppfrm_exceptionCode.1002"));
        RequesterBean request = new RequesterBean();
        DBOpportunityInfoBean sltdOppBean = (DBOpportunityInfoBean)oppSelTableModel.getValueAt(oppSelForm.tblOppSel.getSelectedRow());
        sltdOppBean.setProposalNumber(getSubmissionTitle());
        sltdOppBean.setAcType(functionType);
        sltdOppBean.setUpdateUser(mdiForm.getUserId());
        sltdOppBean.setAwUpdateTimestamp(getAwUpdateTimestamp());
        sltdOppBean.setAwProposalNumber(this.submissionTitle);;
        if (oppHeader.getSubmissionTitle() == null) {
            //Proposal Number == null i.e. this window is opened to create new proposal from Opportunity
            request.setDataObject(sltdOppBean);
            request.setFunctionType(S2SConstants.CHECK_FORMS_AVAILABLE);
            AppletServletCommunicator comm = new AppletServletCommunicator(S2S_SERVLET, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(!response.hasResponse()){ //If this throws exception then schema validation has failed
                return;
            }
            dlgOppSelForm.dispose();
            setSaveNContinue(true);
            return;
        }
        request.setDataObject(sltdOppBean);
        request.setFunctionType(S2SConstants.SAVE_OPPORTUNITY);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(S2S_SERVLET, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.hasResponse()){
            oppHeader.setOpportunityId(sltdOppBean.getOpportunityId());
            oppHeader.setCfdaNumber(sltdOppBean.getCfdaNumber());
            oppHeader.setCompetitionId(sltdOppBean.getCompetitionId());
            dlgOppSelForm.dispose();
            setSaveNContinue(true);
        }
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    public void display(){
        dlgOppSelForm.setVisible(true);
    }
    private void setColumnData(){
        JTableHeader tableHeader = oppSelForm.tblOppSel.getTableHeader();
//        tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        oppSelForm.lblSchUrl.setFont(CoeusFontFactory.getLabelFont());
        oppSelForm.lblInstUrl.setFont(CoeusFontFactory.getLabelFont());
        oppSelForm.tblOppSel.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        oppSelForm.tblOppSel.setRowHeight(22);
        oppSelForm.tblOppSel.setShowHorizontalLines(true);
        oppSelForm.tblOppSel.setShowVerticalLines(true);
        oppSelForm.tblOppSel.setOpaque(true);
        oppSelForm.tblOppSel.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        oppSelForm.tblOppSel.setRowSelectionAllowed(true);
        
        TableColumn column = oppSelForm.tblOppSel.getColumnModel().getColumn(OPP_ID_COLUMN);
        column.setPreferredWidth(250);
        column.setResizable(true);
        
        column = oppSelForm.tblOppSel.getColumnModel().getColumn(OPP_TITLE_COLUMN);
        column.setPreferredWidth(200);
        column.setResizable(true);
//        OppSelectionRenderer oppSelRenderer = new OppSelectionRenderer();
//        column.setCellRenderer(oppSelRenderer);
        
        column = oppSelForm.tblOppSel.getColumnModel().getColumn(CFDA_COLUMN);
        column.setPreferredWidth(100);
        column.setResizable(true);
        column = oppSelForm.tblOppSel.getColumnModel().getColumn(COMPETITION_ID_COLUMN);
        column.setPreferredWidth(100);
        column.setResizable(true);
        column = oppSelForm.tblOppSel.getColumnModel().getColumn(STARTING_DATE_COLUMN);
        column.setPreferredWidth(100);
        column.setResizable(true);
        column = oppSelForm.tblOppSel.getColumnModel().getColumn(CLOSING_DATE_COLUMN);
        column.setPreferredWidth(100);
        column.setResizable(true);

    }
    
    /** Specifies the Modal window */
    private void postInitComponents() {

        Component[] components = { oppSelForm.tblOppSel,oppSelForm.btnOK,oppSelForm.btnCancel};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        oppSelForm.setFocusTraversalPolicy(traversePolicy);
        oppSelForm.setFocusCycleRoot(true);
        
        dlgOppSelForm = new CoeusDlgWindow(mdiForm);
        dlgOppSelForm.getContentPane().add(getControlledUI());
        dlgOppSelForm.setTitle("Select an Opportunity");
        dlgOppSelForm.setFont(CoeusFontFactory.getLabelFont());
        dlgOppSelForm.setModal(true);
        dlgOppSelForm.setResizable(false);
//        dlgOppSelForm.setSize(WIDTH,HEIGHT);
        dlgOppSelForm.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgOppSelForm.getSize();
        dlgOppSelForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgOppSelForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgOppSelForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgOppSelForm.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgOppSelForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    private void setWindowFocus(){
        this.oppSelForm.btnOK.requestFocusInWindow();
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        try{
            dlgOppSelForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            
            Object source = actionEvent.getSource();
            if(source.equals(oppSelForm.btnCancel)){
                performCancelAction();
            }else if(source.equals(oppSelForm.btnOK)){
                saveFormData();
            }
        }catch (Exception  coeusException){
            edu.mit.coeus.utils.CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }finally{
            dlgOppSelForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void performCancelAction(){
        setSaveNContinue(false);
        dlgOppSelForm.dispose();
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
        int selRow = this.oppSelForm.tblOppSel.getSelectedRow();
        OppSelectionTableModel oppTblModel = (OppSelectionTableModel)oppSelForm.tblOppSel.getModel();
        OpportunityInfoBean oppInfo = (OpportunityInfoBean)oppTblModel.getValueAt(selRow);
        oppSelForm.lblSchemaUrl.setText(oppInfo.getSchemaUrl());
        oppSelForm.lblInstructionUrl.setText(oppInfo.getInstructionUrl());
        oppSelForm.lblOpportunityIdVal.setText(oppInfo.getOpportunityId());
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
    
    /**
     * Getter for property awUpdateTimestamp.
     * @return Value of property awUpdateTimestamp.
     */
    public java.sql.Timestamp getAwUpdateTimestamp() {
        return awUpdateTimestamp;
    }
    
    /**
     * Setter for property awUpdateTimestamp.
     * @param awUpdateTimestamp New value of property awUpdateTimestamp.
     */
    public void setAwUpdateTimestamp(java.sql.Timestamp awUpdateTimestamp) {
        this.awUpdateTimestamp = awUpdateTimestamp;
    }
    
    /**
     * Getter for property oppHeader.
     * @return Value of property oppHeader.
     */
    public S2SHeader getOppHeader() {
        return oppHeader;
    }
    
    /**
     * Setter for property oppHeader.
     * @param oppHeader New value of property oppHeader.
     */
    public void setOppHeader(S2SHeader oppHeader) {
        this.oppHeader = oppHeader;
        oppSelForm.lblPropNumVal.setText(oppHeader.getSubmissionTitle());
        oppSelForm.lblSponsorVal.setText(oppHeader.getAgency());
        oppSelForm.lblOpportunityIdVal.setText(oppHeader.getOpportunityId());
        oppSelForm.lblCfdaNumVal.setText(oppHeader.getCfdaNumber());
    }
    
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if(e.getClickCount()==2){
            if(source.equals(oppSelForm.lblInstructionUrl) || 
                    source.equals(oppSelForm.lblSchemaUrl)){
                try{
                    URLOpener.openUrl(((JLabel)source).getText());
                }catch(Exception ex){
                    ex.printStackTrace();
                    CoeusOptionPane.showErrorDialog(ex.getMessage());
                }
            }
                
        }
    }
    
    public void mouseEntered(MouseEvent e) {
        return;
    }
    
    public void mouseExited(MouseEvent e) {
        return;
    }
    
    public void mousePressed(MouseEvent e) {
        return;
    }
    
    public void mouseReleased(MouseEvent e) {
        return;
    }
    
    /**
     * Getter for property errorMessage.
     * @return Value of property errorMessage.
     */
    public java.lang.String getErrorMessage() {
        return errorMessage;
    }    
    
    /**
     * Setter for property errorMessage.
     * @param errorMessage New value of property errorMessage.
     */
    public void setErrorMessage(java.lang.String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public class OppSelectionTableModel extends AbstractTableModel{
        private String[] colName = {"Opportunity Id", "Opportunity Title", "CFDA Number", 
                                    "Competition Id", "Starting Date", "Closing Date"};
        private Class[] colClass = {String.class, String.class, String.class, 
                                    String.class, Object.class, Object.class};
        private Vector oppList = new Vector();
            /*For formating the date*/
        private static final String DATE_FORMAT = "dd-MMM-yyyy";
        private static final String DATE_SEPARATERS = ":/.,|-";

        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
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
            this.oppList = data;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(oppList==null){
                return 0;
            }else{
                return oppList.size();
            }
        }
        
        public Object getValueAt(int row) {
            if(getRowCount()==0) return null;
            return oppList.elementAt(row);
        }

        public Object getValueAt(int row,int col) {
            OpportunityInfoBean oppBean = (OpportunityInfoBean)getValueAt(row);
            DateUtils dateUtil = new DateUtils();
            switch(col){
                case(OPP_ID_COLUMN):
                    return oppBean.getOpportunityId();
                case(OPP_TITLE_COLUMN):
                    return oppBean.getOpportunityTitle();
                case(CFDA_COLUMN):
                    return oppBean.getCfdaNumber();
                case(COMPETITION_ID_COLUMN):
                    return oppBean.getCompetitionId();
                case(STARTING_DATE_COLUMN):
                    return oppBean.getOpeningDate()==null?"":
                        dateUtil.formatDate(oppBean.getOpeningDate().toString(),DATE_FORMAT);
                case(CLOSING_DATE_COLUMN):
                    return oppBean.getClosingDate()==null?"":
                        dateUtil.formatDate(oppBean.getClosingDate().toString(),DATE_FORMAT);
            }
            return EMPTY_STRING;
        }
    }// end of table model class
}
