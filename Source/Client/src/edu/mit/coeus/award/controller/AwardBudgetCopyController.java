/*
 * AwardBudgetCopyController.java
 *
 * Created on July 14, 2005, 3:19 PM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardAmountInfoBean;
import edu.mit.coeus.award.bean.AwardBudgetCopyBean;
import edu.mit.coeus.award.gui.AwardBudgetCopyForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author  vinayks
 */
public class AwardBudgetCopyController extends AwardBudgetController implements ActionListener{
    
    private AwardBudgetCopyForm awardBudgetCopyForm;
    private CoeusMessageResources coeusMessageResources;
    private AwardAmountInfoBean awardAmountInfoBean;
    private static final String EMPTY_STRING = "";
    private CoeusVector cvCopyData;
    private AwardBudgetCopyTableModel awardBudgetCopyTableModel;
    private AwardBudgetCopyTableCellRenderer awardBudgetCopyTableCellRenderer;
    /** specifies the column index for the Copy Award budget
     */
    private static final int PROPOSAL_COLUMN = 0;
    private static final int VERSION_COLUMN =1;
    private static final int  PERIOD_COLUMN = 2;
    private static final int START_DATE_COLUMN = 3;
    private static final int END_DATE_COLUMN = 4;
    private static final int TOTAL_COLUMN = 5;
    
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String TITLE = "Award Budget Summary Copy";
    private static final int WIDTH = 440;
    private static final int HEIGHT = 270;
    
    private DateUtils dtUtils = new DateUtils();
    private CoeusDlgWindow dlgAwardBudgetCopy;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    //To make the server call
    private static final String AWARD_BUDGET_SERVLET = "/AwardBudgetMaintainanceServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
    AWARD_BUDGET_SERVLET;
    private static final char GET_AWARD_BUDGET_COPY = 'B';
    private static final char COPY_AWARD_BUDGET ='D';
    
    private boolean OK_CLICKED = false;
    
    
    /** Please select a row to copy
     */
    private static final String SELECT_ROW_FOR_COPY ="awardBudgetCopy_exceptionCode.1001";
    
    
    /** Creates a new instance of AwardBudgetCopyController */
    public AwardBudgetCopyController(AwardAmountInfoBean awardAmountInfoBean) {
        this.awardAmountInfoBean = awardAmountInfoBean;
        coeusMessageResources = CoeusMessageResources.getInstance();
        awardBudgetCopyForm = new AwardBudgetCopyForm();
    }
    
    
    public void registerComponents() {
        awardBudgetCopyTableModel = new AwardBudgetCopyTableModel();
        awardBudgetCopyTableCellRenderer = new AwardBudgetCopyTableCellRenderer();
        awardBudgetCopyForm.tblBudgetCopy.setModel(awardBudgetCopyTableModel);
        awardBudgetCopyForm.btnCancel.addActionListener(this);
        awardBudgetCopyForm.btnOk.addActionListener(this);
        java.awt.Component[] components={awardBudgetCopyForm.btnOk,
        awardBudgetCopyForm.btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardBudgetCopyForm.setFocusTraversalPolicy(traversePolicy);
        awardBudgetCopyForm.setFocusCycleRoot(true);
        setTableEditors();
        
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        cvCopyData = new CoeusVector();
        //cvCopyData = getAwardBudgetCopyData();
        cvCopyData = (CoeusVector)data;
        
        if(cvCopyData != null && cvCopyData.size()>0){
            for(int index = 0 ; index < cvCopyData.size() ; index++ ){
                AwardBudgetCopyBean budgetCopyBean = 
                        (AwardBudgetCopyBean)cvCopyData.get(index);
                budgetCopyBean.setMitAwardNumber(awardAmountInfoBean.getMitAwardNumber());
                budgetCopyBean.setSequenceNumber(awardAmountInfoBean.getSequenceNumber());
                budgetCopyBean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
            }//End for
            awardBudgetCopyTableModel.setData(cvCopyData);
        }//End If
    }//End setFormData
    
    // Get the Copy data by making server call
    /*private CoeusVector getAwardBudgetCopyData() throws CoeusException{
        CoeusVector cvCopyData=null;
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setId(awardAmountInfoBean.getMitAwardNumber());
        request.setFunctionType(GET_AWARD_BUDGET_COPY);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                cvCopyData = (CoeusVector)response.getDataObjects();
            }else{
                throw new CoeusException(response.getMessage(),0);
            }
        }
        return cvCopyData;
    }*/
    
    
    public void formatFields() {
        
    }
    
    
    
    public java.awt.Component getControlledUI() {
        return awardBudgetCopyForm;
    }
    
    public boolean validate()throws edu.mit.coeus.exception.CoeusUIException {
        int selRow= awardBudgetCopyForm.tblBudgetCopy.getSelectedRow();
        if(selRow<0){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ROW_FOR_COPY));
            return false;
        }
        return true;
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
       
        
        //AwardBudgetCopyBean awardBudgetCopyBean = prepareBeanForCopy();
        int selectedRows[] = awardBudgetCopyForm.tblBudgetCopy.getSelectedRows();
        CoeusVector cvDataToServer = new CoeusVector();
        
        for (int index = 0 ; index <selectedRows.length ; index++){
            cvDataToServer.add(cvCopyData.get(selectedRows[index]));
        }
               
        request.setFunctionType(COPY_AWARD_BUDGET);
        request.setDataObject(cvDataToServer);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage());
            }else{
                Integer versionNo = (Integer)response.getDataObject();
                int newVersionNo = versionNo.intValue();
            }
        }
    }
    
    public void display() {
        if(awardBudgetCopyForm.tblBudgetCopy.getRowCount()>0){
            awardBudgetCopyForm.tblBudgetCopy.setRowSelectionInterval(0,0);
        }
        dlgAwardBudgetCopy.setVisible(true);
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent){
        Object source = actionEvent.getSource();
        try{
            if(source.equals(awardBudgetCopyForm.btnCancel)){
                performCancelAction();
            }else if(source.equals(awardBudgetCopyForm.btnOk)){
                performOKAction();
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (CoeusUIException coeusUiException){
            coeusUiException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusUiException.getMessage());
        }
    }
    
    private void performCancelAction() throws CoeusException{
        dlgAwardBudgetCopy.dispose();
    }
    
    private void performOKAction() throws CoeusException,CoeusUIException{
        if(validate()){
            awardBudgetCopyForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            saveFormData();
            setOK_CLICKED(true);
            awardBudgetCopyForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            dlgAwardBudgetCopy.dispose();
        }
    }
    
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = awardBudgetCopyForm.tblBudgetCopy.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setMaximumSize(new Dimension(100,27));
            tableHeader.setMinimumSize(new Dimension(100,27));
            tableHeader.setPreferredSize(new Dimension(100,27));
            
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            awardBudgetCopyForm.tblBudgetCopy.setRowHeight(22);
            awardBudgetCopyForm.tblBudgetCopy.setShowHorizontalLines(true);
            awardBudgetCopyForm.tblBudgetCopy.setShowVerticalLines(true);
            awardBudgetCopyForm.tblBudgetCopy.setOpaque(false);
            
            awardBudgetCopyForm.tblBudgetCopy.setSelectionMode(
            DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            TableColumn columnDetails;
            int size[] = {80,65,65,85,85,100};
            for(int index=0;index<size.length;index++){
                columnDetails=awardBudgetCopyForm.tblBudgetCopy.getColumnModel().getColumn(index);
                columnDetails.setCellRenderer(awardBudgetCopyTableCellRenderer);
                columnDetails.setPreferredWidth(size[index]);
            }
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    /** Specifies the Modal window */
    public  void postInitComponents() {
        dlgAwardBudgetCopy = new CoeusDlgWindow(mdiForm);
        dlgAwardBudgetCopy.getContentPane().add(awardBudgetCopyForm);
        dlgAwardBudgetCopy.setTitle(TITLE);
        dlgAwardBudgetCopy.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardBudgetCopy.setModal(true);
        dlgAwardBudgetCopy.setResizable(false);
        dlgAwardBudgetCopy.setSize(WIDTH,HEIGHT);
        dlgAwardBudgetCopy.setLocation(CoeusDlgWindow.CENTER);
        
        dlgAwardBudgetCopy.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                try{
                    performCancelAction();
                }catch (CoeusException coeusException){
                    coeusException.printStackTrace();
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                }
            }
        });
        dlgAwardBudgetCopy.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardBudgetCopy.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                try{
                    performCancelAction();
                    return;
                }catch (CoeusException coeusException){
                    coeusException.printStackTrace();
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                }
            }
        });
        
        dlgAwardBudgetCopy.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    private void setWindowFocus(){
        awardBudgetCopyForm.btnOk.requestFocusInWindow();
    }
    
    /**
     * Getter for property OK_CLICKED.
     * @return Value of property OK_CLICKED.
     */
    public boolean isOK_CLICKED() {
        return OK_CLICKED;
    }    
    
    /**
     * Setter for property OK_CLICKED.
     * @param OK_CLICKED New value of property OK_CLICKED.
     */
    public void setOK_CLICKED(boolean OK_CLICKED) {
        this.OK_CLICKED = OK_CLICKED;
    }
    
    
    /*private AwardBudgetCopyBean prepareBeanForCopy(){
        int selectedRows[] = awardBudgetCopyForm.tblBudgetCopy.getSelectedRows();
        
        String proposalNumber = "";
        String proposalVerNumber = "";
        String budgetPeriod = "";
        AwardBudgetCopyBean copyBean = new AwardBudgetCopyBean();
        
        for (int index = 0 ; index <selectedRows.length ; index++){        
            AwardBudgetCopyBean awardBudgetCopyBean = (AwardBudgetCopyBean)cvCopyData.get(selectedRows[index]);
            
            if(proposalNumber.equals("")){
                proposalNumber = awardBudgetCopyBean.getProposalNumber();
            }else{
                proposalNumber = proposalNumber+","+awardBudgetCopyBean.getProposalNumber();
            }
            
            if(proposalVerNumber.equals("")){
                proposalVerNumber = ""+awardBudgetCopyBean.getVersionNumber();
            }else{
                proposalVerNumber = proposalVerNumber+","+awardBudgetCopyBean.getVersionNumber();
            }
            

            
            if(budgetPeriod.equals("")){
                budgetPeriod = ""+awardBudgetCopyBean.getBudgetPeriod();
            }else{
                budgetPeriod = budgetPeriod+","+awardBudgetCopyBean.getBudgetPeriod();
            }
        }
        
        copyBean.setProposalNumber(proposalNumber);
        copyBean.setVersionNumber(proposalVerNumber);
        copyBean.setBudgetPeriod(budgetPeriod);
        
        return copyBean;
    }*/
    
    /** this is an Inner class which behaves like a model for the JTable.
     *It manipulated the Award Copy budget tabdle data
     */
    public class AwardBudgetCopyTableModel extends AbstractTableModel{
        
        private String proposalNumber = "<html>Proposal<br>Number</html>";
        private String versionNumber = "<html>Version<br>Number</html>";
        private String budgetPeriod="<html>Budget<br>Period</html>";
        private String startDate = "<html>Start Date<br></html>";
        private String endDate = "<html>End Date<br></html>";
        private String totalCost = "<html>Total Cost<br></html>";
        
        private String []colNames = {proposalNumber,versionNumber,budgetPeriod,startDate,endDate,totalCost};
        private Class colClass[]  = {String.class, Integer.class, Integer.class, String.class, String.class, Double.class};
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public void setData(CoeusVector cvCopyData){
            cvCopyData = cvCopyData;
        }
        
        public String getColumnName(int col){
            return colNames[col];
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getRowCount() {
            if(cvCopyData==null){
                return 0;
            }else{
                return cvCopyData.size();
            }
        }
        public Object getValueAt(int row, int col) {
            AwardBudgetCopyBean copyBean = (AwardBudgetCopyBean)cvCopyData.get(row);
            switch(col){
                case PROPOSAL_COLUMN:
                    return copyBean.getProposalNumber();
                case VERSION_COLUMN:
                    return new Integer(copyBean.getVersionNumber());
                case PERIOD_COLUMN:
                    return new Integer(copyBean.getBudgetPeriod());
                case START_DATE_COLUMN:
                    return copyBean.getStartDate();
                case END_DATE_COLUMN:
                    return copyBean.getEndDate();
                case TOTAL_COLUMN:
                    return new Double(copyBean.getTotalCost());
                    
            }
            return EMPTY_STRING;
        }
        
    }// end of Table Model class.......
    
    
    /** It is an inner class which provide renderer for the Table
     */
    public class AwardBudgetCopyTableCellRenderer extends DefaultTableCellRenderer{
        private CoeusTextField txtComponent;
        private DollarCurrencyTextField txtDollar;
        
        
        public AwardBudgetCopyTableCellRenderer(){
            txtComponent = new CoeusTextField();
            txtDollar =  new DollarCurrencyTextField();
            
            txtComponent.setBorder(new EmptyBorder(0,0,0,0));
            txtDollar.setBorder(new EmptyBorder(0,0,0,0));
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int col){
            
            switch(col){
                case PROPOSAL_COLUMN:
                    if(isSelected){
                        txtComponent.setBackground(java.awt.Color.YELLOW);
                        txtComponent.setForeground(java.awt.Color.black);
                    }else{
                        txtComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        txtComponent.setForeground(java.awt.Color.black);
                    }
                    txtComponent.setText(value.toString());
                    return txtComponent;
                case VERSION_COLUMN:
                case PERIOD_COLUMN:
                    if(isSelected){
                        txtComponent.setBackground(java.awt.Color.YELLOW);
                        txtComponent.setForeground(java.awt.Color.black);
                    }else{
                        txtComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        txtComponent.setForeground(java.awt.Color.black);
                    }
                    txtComponent.setText(value.toString());
                    return txtComponent;
                case START_DATE_COLUMN:
                case END_DATE_COLUMN:
                    if(isSelected){
                        txtComponent.setBackground(java.awt.Color.YELLOW);
                        txtComponent.setForeground(java.awt.Color.black);
                    }else{
                        txtComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        txtComponent.setForeground(java.awt.Color.black);
                    }
                    if(value == null || value.toString().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        return txtComponent;
                    }else{
                        value = dtUtils.formatDate(value.toString(),REQUIRED_DATEFORMAT);
                        txtComponent.setText(value.toString());
                        return txtComponent;
                    }
                case TOTAL_COLUMN:
                    if(isSelected){
                        txtDollar.setBackground(java.awt.Color.YELLOW);
                        txtDollar.setForeground(java.awt.Color.black);
                    }else{
                        txtDollar.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        txtDollar.setForeground(java.awt.Color.black);
                    }
                    txtDollar.setValue(new Double(value.toString()).doubleValue());
                    return txtDollar;
            }
            return txtComponent;
        }
    }// end of AwardBudgetCopyTableCellRenderer...
    
    public void cleanUp(){
        coeusMessageResources = null;
        awardAmountInfoBean = null;
        awardBudgetCopyTableModel = null;
        awardBudgetCopyTableCellRenderer = null;
        cvCopyData = null;
        dlgAwardBudgetCopy = null;
        awardBudgetCopyForm = null;
    }
}
