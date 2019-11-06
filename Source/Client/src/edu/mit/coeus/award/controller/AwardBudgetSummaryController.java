/*
 * AwardBudgetSummaryController.java
 *
 * Created on July 18, 2005, 3:30 PM
 */
package edu.mit.coeus.award.controller;
import edu.mit.coeus.award.bean.AwardAmountInfoBean;
import edu.mit.coeus.award.bean.AwardBudgetSummaryBean;
import edu.mit.coeus.award.gui.AwardBudgetSummaryForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UserUtils;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
/**
 *
 * @author  tarique
 */
public class AwardBudgetSummaryController extends AwardBudgetController 
implements ActionListener, MouseListener, ListSelectionListener{
    
    private static final int BUDGET_VERSION_COLUMN=0;
    private static final int SEQUENCE_COLUMN=1;
    private static final int START_DATE_COLUMN=2;
    private static final int EXPIRATION_DATE_COLUMN=3;
    private static final int BUDGET_STATUS_COLUMN=4;
    private static final int BUDGET_AMOUNT_COLUMN=5;
    private static final int DESCRIPTION_COLUMN=6;
    private static final int UPDATE_USER_COLUMN=7;
    private static final int UPDATE_TIMESTAMP_COLUMN=8;
    /*Award Status*/
    private static final int INACTIVE = 2;
    private static final int TERMINATED = 4;
    private static final int WIDTH=830;
    private static final int HEIGHT=450;
    private static final String EMPTY_STRING="";
    private static String WINDOW_TITLE = "Budget for Award: ";
    private DateUtils dateUtils;
    private AwardBudgetSummaryForm awardBudgetSummaryForm;
    private AwardBudgetSummaryRenderer awardBudgetSummaryRenderer;
    private AwardBudgetSummaryTableModel awardBudgetSummaryTableModel;
    private CoeusDlgWindow dlgAwardBudgetSummary;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusMessageResources coeusMessageResources;
    private CoeusVector cvSummaryData, cvCopyData;
    private AwardAmountInfoBean awardAmountInfoBean;
    private char functionType;
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String DISPLAY_DATE_FORMAT = "dd-MMM-yyyy";
    private static final String REQUIRED_DATE_FORMAT = DISPLAY_DATE_FORMAT +" hh:mm aaa ";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
    private static final String NO_PRIVILIGES = "awardBudgetSummary_exceptionCode.2001";
    private static final String CANNOT_MODIFY_ROW = "awardBudgetSummary_exceptionCode.2002";
    private static final String SELECT_ROW_FOR_PROCEED = "awardBudgetSummary_exceptionCode.2003";
    private static final String NO_ROWS_TO_MODIFY = "awardBudgetSummary_exceptionCode.2004";
    private static final String NO_ROW_TO_DELETE = "awardBudgetSummary_exceptionCode.2005";
    private static final String CANNOT_DELETE_ROW = "awardBudgetSummary_exceptionCode.2006";
    private static final String DELETE_CONFIRM = "awardBudgetSummary_exceptionCode.2007";
    private static final String CHANGE_STATUS = "awardBudgetSummary_exceptionCode.2025";
    private static final String NO_ROW_TO_DISPLAY="awardBudgetSummary_exceptionCode.2026";
    private static final String NO_ROW_TO_REBUDGET="awardBudgetSummary_exceptionCode.2027";
    //To make the server call
    private static final String AWARD_BUDGET_SERVLET = "/AwardBudgetMaintainanceServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
                                                        AWARD_BUDGET_SERVLET;
    private static final char GET_SUMMARY_DATA = 'A';
    private static final char GET_AWARD_BUDGET_COPY = 'B';
    private static final char SAVE_SUMMARY_DATA = 'H';
    private String awardNumber;
    private int sequenceNo;
    private int awardStatusCode;
    //COEUSQA-3937
    private static final char CHECK_UPD_SEQ_IN_BUD_INFO = '1';
    //COEUSQA-3937
    
     /** Creates a new instance of AwardBudgetSummaryController */
    public AwardBudgetSummaryController() throws edu.mit.coeus.exception.CoeusException{
    }
    
    public AwardBudgetSummaryController(AwardAmountInfoBean awardAmountInfoBean,char functionType)
    throws CoeusException{
        this.awardAmountInfoBean = awardAmountInfoBean;
        this.functionType = functionType;
        this.awardNumber = awardAmountInfoBean.getMitAwardNumber();
        this.sequenceNo = awardAmountInfoBean.getSequenceNumber();
        this.awardStatusCode = awardAmountInfoBean.getStatusCode();
        
        coeusMessageResources=CoeusMessageResources.getInstance();
        awardBudgetSummaryForm=new AwardBudgetSummaryForm();
        awardBudgetSummaryForm.lblAccountNumber.setHorizontalAlignment(SwingConstants.RIGHT);
        //Added for case #2100 start
        String accountNo = awardAmountInfoBean.getAccountNumber() == null 
                                ? EMPTY_STRING : awardAmountInfoBean.getAccountNumber();
        //Added for case #2100 end                                
        awardBudgetSummaryForm.lblAccountNumber.setText(accountNo);
        awardBudgetSummaryForm.txtTotalObDisAmt.setValue(awardAmountInfoBean.getObliDistributableAmount());
        dateUtils = new DateUtils();
        //Get the Data for Copy , for enabling disabling the buttons
        this.cvCopyData = getAwardBudgetCopyData();
        registerComponents();
        postInitComponent();
        formatFields();
    }
    
    public void formatFields(){
        functionType = getMode();
        if(functionType==TypeConstants.DISPLAY_MODE){
            awardBudgetSummaryForm.btnNew.setEnabled(false);
            awardBudgetSummaryForm.btnModify.setEnabled(false);
            awardBudgetSummaryForm.btnRebudget.setEnabled(false);
            awardBudgetSummaryForm.btnCopy.setEnabled(false);
            awardBudgetSummaryForm.btnDelete.setEnabled(false);
            awardBudgetSummaryForm.btnChangeStatus.setEnabled(false);
            awardBudgetSummaryForm.btnChangeStatus.setVisible(false);
        }
    }
    
    private char getMode(){
//        if(isAwardBudgetViewer && !isAwardBudgetCreator &&
//           !isAwardBudgetApprover && !isAwardBudgetSubmitter &&
//           !isAwardBudgetModifier && !isAwardBudgetAggregator){
        if(isAwardBudgetViewer && !isAwardBudgetCreator &&
           !isAwardBudgetApprover && !isAwardBudgetSubmitter &&
           !isAwardBudgetModifier){
               functionType = TypeConstants.DISPLAY_MODE;
        }
       return functionType;
    }
    public Component getControlledUI() {
        return awardBudgetSummaryForm;
    }
    public Object getFormData() {
        return null;
    }
    public void registerComponents() {
        awardBudgetSummaryForm.btnNew.addActionListener(this);
        awardBudgetSummaryForm.btnClose.addActionListener(this);
        awardBudgetSummaryForm.btnCopy.addActionListener(this);
        awardBudgetSummaryForm.btnDelete.addActionListener(this);
        awardBudgetSummaryForm.btnDisplay.addActionListener(this);
        awardBudgetSummaryForm.btnModify.addActionListener(this);
        awardBudgetSummaryForm.btnRebudget.addActionListener(this);
        awardBudgetSummaryForm.btnChangeStatus.addActionListener(this);
        awardBudgetSummaryForm.tblBudgetSummary.addMouseListener(this);
        awardBudgetSummaryForm.tblBudgetSummary.getSelectionModel().addListSelectionListener(this);
        
        java.awt.Component[] components = { awardBudgetSummaryForm.btnNew,
        awardBudgetSummaryForm.btnModify,awardBudgetSummaryForm.btnDisplay,
        awardBudgetSummaryForm.btnRebudget,awardBudgetSummaryForm.btnDelete,
        awardBudgetSummaryForm.btnCopy,awardBudgetSummaryForm.btnClose
        };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardBudgetSummaryForm.setFocusTraversalPolicy(traversePolicy);
        awardBudgetSummaryForm.setFocusCycleRoot(true);
        awardBudgetSummaryRenderer=new AwardBudgetSummaryRenderer();
        awardBudgetSummaryTableModel=new AwardBudgetSummaryTableModel();
        awardBudgetSummaryForm.tblBudgetSummary.setModel(awardBudgetSummaryTableModel);
        setTableEditors();
    }
    public void postInitComponent(){
        dlgAwardBudgetSummary = new CoeusDlgWindow(mdiForm);
        // JM 5-14-2012 changed label from Transaction ID to Amount Sequence
        String windowTitle = WINDOW_TITLE + awardAmountInfoBean.getMitAwardNumber()+" "+
                             "Sequence: "+ awardAmountInfoBean.getSequenceNumber()+" "+
                             "Amount Sequence: "+ awardAmountInfoBean.getAmountSequenceNumber();
        dlgAwardBudgetSummary.setTitle(windowTitle);
        dlgAwardBudgetSummary.setResizable(false);
        dlgAwardBudgetSummary.setModal(true);
        dlgAwardBudgetSummary.getContentPane().add(getControlledUI());
        dlgAwardBudgetSummary.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardBudgetSummary.setSize(WIDTH, HEIGHT);
        dlgAwardBudgetSummary.setLocation(dlgAwardBudgetSummary.CENTER);
        dlgAwardBudgetSummary.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardBudgetSummary.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        dlgAwardBudgetSummary.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCloseAction();
            }
        });
        dlgAwardBudgetSummary.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCloseAction();
            }
        });
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        Hashtable unitLevelRight= null;
        Hashtable ospLevelRight = null;        
        
        Hashtable formDatarights = null;     
        
        cvSummaryData = new CoeusVector();
        formDatarights = getSummaryData();
        cvSummaryData  = (CoeusVector)formDatarights.get(KeyConstants.AWARD_BUDGET_SUMMARY_DATA);      
        //COEUSQA-3937
        //check if the sequence and amount sequence in award_budget-info table is same as the latest numbers in amount-_info table.  
        //If not, can we update the sequence numbers in budget_info table to have the latest numbers.          
            boolean modify = false;
            if(cvSummaryData != null && !cvSummaryData.isEmpty()){
                 AwardBudgetSummaryBean awardBudgetSummaryBean = (AwardBudgetSummaryBean)cvSummaryData.get(0);
                 int statusCode = awardBudgetSummaryBean.getBudgetStatusCode();                 
                 switch(statusCode){
                     case IN_PROGRESS:                    
                         modify = true;
                         break;

                     case SUBMITTED:
                         modify = true;
                         break; 
                     case REJECTED:
                         modify = true;
                         break;
                 }
             }
               if(modify){
                   if(checkWhetherToUpdSeqInBugInfo()){
                       cvSummaryData = new CoeusVector();
                       formDatarights = getSummaryData();
                       cvSummaryData  = (CoeusVector)formDatarights.get(KeyConstants.AWARD_BUDGET_SUMMARY_DATA);
                   }
               }  
        //COEUSQA-3937 
        /**
         * Get the Unit level right from the hashtable and update to the base class
         */
        //unitLevelRight = (Hashtable)formDatarights.get(KeyConstants.AWARD_BUDGET_UNIT_LEVEL_RIGHT);
        /**
         * Get the OSP level right from the hashtable and update to the base class
         */
        //ospLevelRight = (Hashtable)formDatarights.get(KeyConstants.AWARD_BUDGET_OSP_LEVEL_RIGHT);
        //setOspLevelRight(ospLevelRight);
        //setUnitLevelRight(unitLevelRight);
        awardBudgetSummaryTableModel.setData(cvSummaryData);
        awardBudgetSummaryTableModel.fireTableDataChanged();
       enableDisableButtons();
    }
    
    private void enableDisableButtons(){
        //Enable/Disable the Copy Button
        if (cvCopyData != null && cvCopyData.size()>0){
            //Enable the Copy button only if the last row is POSTED
            if (cvSummaryData != null && cvSummaryData.size()>0){
                
                /*Equals eqPosted = new Equals("budgetStatusCode" ,new Integer(POSTED));
                CoeusVector cvFilteredData = cvSummaryData.filter(eqPosted);
                if(cvFilteredData .size() > 0 && functionType != TypeConstants.DISPLAY_MODE){
                    
                    awardBudgetSummaryForm.btnCopy.setEnabled(true);
                }else{
                    awardBudgetSummaryForm.btnCopy.setEnabled(false);
                }*/
                
                AwardBudgetSummaryBean awardBudgetSummaryBean = 
                                (AwardBudgetSummaryBean)cvSummaryData.get(cvSummaryData.size()-1);
                
                if(awardBudgetSummaryBean.getBudgetStatusCode() == POSTED 
                   && functionType != TypeConstants.DISPLAY_MODE){
                    awardBudgetSummaryForm.btnCopy.setEnabled(true);
                }else{
                    awardBudgetSummaryForm.btnCopy.setEnabled(false);
                }
            }else{
                if(functionType != TypeConstants.DISPLAY_MODE){
                    awardBudgetSummaryForm.btnCopy.setEnabled(true);
                }//End of if
            }
         }else{
            awardBudgetSummaryForm.btnCopy.setEnabled(false);
        }
        //Enable/Disable the New Button
        if (cvSummaryData != null && cvSummaryData.size()>0){
            //Enable the New button only if the last row is POSTED
            
            /*Equals eqPosted = new Equals("budgetStatusCode" ,new Integer(POSTED));
            CoeusVector cvFilteredData = cvSummaryData.filter(eqPosted);
            if(cvFilteredData .size()>0){
                awardBudgetSummaryForm.btnNew.setEnabled(true);
            }else{
                awardBudgetSummaryForm.btnNew.setEnabled(false);
            }*/
            
            AwardBudgetSummaryBean awardBudgetSummaryBean = 
                                (AwardBudgetSummaryBean)cvSummaryData.get(cvSummaryData.size()-1);
            
            if(awardBudgetSummaryBean.getBudgetStatusCode() == POSTED
               && functionType != TypeConstants.DISPLAY_MODE){
                awardBudgetSummaryForm.btnNew.setEnabled(true);
            }else{
                awardBudgetSummaryForm.btnNew.setEnabled(false);
            }
         }else{
             if(functionType != TypeConstants.DISPLAY_MODE){
                 awardBudgetSummaryForm.btnNew.setEnabled(true);
                 awardBudgetSummaryForm.btnChangeStatus.setEnabled(false);
                 awardBudgetSummaryForm.btnChangeStatus.setVisible(false);
             }//End of if
         }
        
        //Enable/Disable the Rebudget, New, Copy
        if(awardStatusCode == INACTIVE|| awardStatusCode == TERMINATED){
            awardBudgetSummaryForm.btnNew.setEnabled(false);
            awardBudgetSummaryForm.btnRebudget.setEnabled(false);
            awardBudgetSummaryForm.btnCopy.setEnabled(false);
        }
      }//End enableDisableButtons
    
    private Hashtable getSummaryData() throws CoeusException{
        Hashtable formData=null;
        Hashtable awardBudgetRigts = new Hashtable();
    //        awardBudgetRigts.put(KeyConstants.AWARD_BUDGET_OSP_LEVEL_RIGHT, getOspLevelRight());
    //        awardBudgetRigts.put(KeyConstants.AWARD_BUDGET_UNIT_LEVEL_RIGHT, getUnitLevelRight());
       RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setId(awardAmountInfoBean.getMitAwardNumber());
        request.setDataObject(awardBudgetRigts);
        request.setFunctionType(GET_SUMMARY_DATA);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                formData = (Hashtable)response.getDataObject();
            }else{
                throw new CoeusException(response.getMessage(),0);
            }
        }
        return formData;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        try{
            if(source.equals(awardBudgetSummaryForm.btnClose)){
                performCloseAction();
            }else if(source.equals(awardBudgetSummaryForm.btnCopy)){
                performCopyAction();
            }else if(source.equals(awardBudgetSummaryForm.btnModify)){
                performModifyAction();
            }else if(source.equals(awardBudgetSummaryForm.btnRebudget)){
                performRebudgetAction();
            }else if(source.equals(awardBudgetSummaryForm.btnDelete)){
                performDeleteAction();
            }else if(source.equals(awardBudgetSummaryForm.btnNew)){
                performNewAction();
            }else if(source.equals(awardBudgetSummaryForm.btnDisplay)){
                performDisplayAction();
            }else if(source.equals(awardBudgetSummaryForm.btnChangeStatus)){
                performChangeStatus();
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
    public void mouseClicked(MouseEvent e){
        if(e.getClickCount()!=2){
            return;
        }
        try{
            awardBudgetSummaryForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            performDisplayAction();
        }catch (CoeusException ce){
            ce.printStackTrace();
        }finally{
            awardBudgetSummaryForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    public void mousePressed(MouseEvent e){
        
    }
    public void mouseReleased(MouseEvent e){
        
    }
    public void mouseEntered(MouseEvent e){
        
    }
    public void mouseExited(MouseEvent e){
        
    }
    private void performDisplayAction() throws CoeusException{
        displayAwardDetails(TypeConstants.DISPLAY_MODE);
        refresh();
    }
    
    private void performNewAction() throws CoeusException{
        if(!isAwardBudgetCreator){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PRIVILIGES));
            return;
        }else{
            displayAwardDetails(TypeConstants.NEW_MODE);
            refresh();
        }
    }
    
    private void performDeleteAction() throws CoeusException{
        int rowCount=awardBudgetSummaryForm.tblBudgetSummary.getRowCount();
        if(rowCount==0){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(NO_ROW_TO_DELETE));
            return;
        }
        
        int selRow = awardBudgetSummaryForm.tblBudgetSummary.getSelectedRow();
        
        if(selRow==-1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ROW_FOR_PROCEED));
            return ;
        }
        AwardBudgetSummaryBean bean = (AwardBudgetSummaryBean)cvSummaryData.get(selRow);
        int statusCode = bean.getBudgetStatusCode();
        int typeCode = bean.getAwardBudgetTypeCode();
        
        if(statusCode == IN_PROGRESS){
            if(!isAwardBudgetCreator ){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PRIVILIGES));
                return;
            }
        }else if(typeCode == REBUDGET && statusCode == IN_PROGRESS){
            if(!isAwardBudgetModifier){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PRIVILIGES));
                return;
            }
        }else if(statusCode==SUBMITTED || statusCode==POSTED){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_DELETE_ROW));
            return;
        }
        
        int yesNo=CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(DELETE_CONFIRM), 2, 3);
        if(yesNo==CoeusOptionPane.SELECTION_YES){
            int selectedRow = awardBudgetSummaryForm.tblBudgetSummary.getSelectedRow();
            AwardBudgetSummaryBean awardBudgetSummaryBean = 
                            (AwardBudgetSummaryBean)cvSummaryData.get(selectedRow);
                            
            awardBudgetSummaryBean.setMitAwardNumber(awardNumber);
            awardBudgetSummaryBean.setAcType(TypeConstants.DELETE_RECORD);
            saveFormData();
            cvSummaryData.remove(selectedRow);
            awardBudgetSummaryTableModel.fireTableDataChanged();
            enableDisableButtons();
            rowCount = awardBudgetSummaryForm.tblBudgetSummary.getRowCount();
            if(rowCount > 0){
               if(cvSummaryData!=null&&cvSummaryData.size()>0){
                    awardBudgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(cvSummaryData.size()-1,
                    cvSummaryData.size()-1);
                    awardBudgetSummaryForm.tblBudgetSummary.scrollRectToVisible(
                    awardBudgetSummaryForm.tblBudgetSummary.getCellRect(cvSummaryData.size()-1 ,0, true));
                }
             }//ENd If
        }//End if
    }
   private void performChangeStatus() throws CoeusException{
         int option = CoeusOptionPane.showQuestionDialog(
         coeusMessageResources.parseMessageKey(CHANGE_STATUS) ,2,3);
         if(option == CoeusOptionPane.SELECTION_YES){
                     
            int selectedRow = awardBudgetSummaryForm.tblBudgetSummary.getSelectedRow();
            AwardBudgetSummaryBean awardBudgetSummaryBean = 
                            (AwardBudgetSummaryBean)cvSummaryData.get(selectedRow);
            awardBudgetSummaryBean.setMitAwardNumber(awardNumber);
            awardBudgetSummaryBean.setBudgetStatusCode(IN_PROGRESS);
            awardBudgetSummaryBean.setAcType(TypeConstants.UPDATE_RECORD);
            saveFormData();
            dlgAwardBudgetSummary.dispose();
         }
    }
    
    private void performRebudgetAction() throws CoeusException{
        if(!isAwardBudgetModifier){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PRIVILIGES));
            return;
        }else{
            displayAwardDetails(TypeConstants.REBUDGET_MODE);
            refresh();
        }
    }
    
    private void performModifyAction() throws CoeusException{
        int rowCount=awardBudgetSummaryForm.tblBudgetSummary.getRowCount();
        if(rowCount==0){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_ROWS_TO_MODIFY));
            return;
        }
        int selRow = awardBudgetSummaryForm.tblBudgetSummary.getSelectedRow();
        if(selRow==-1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ROW_FOR_PROCEED));
            return ;
        }
        AwardBudgetSummaryBean bean = ((AwardBudgetSummaryBean)cvSummaryData.get(selRow));
        int statusCode = bean.getBudgetStatusCode();
        int typeCode = bean.getAwardBudgetTypeCode();
        if(typeCode == NEW && statusCode == IN_PROGRESS){
            if(!isAwardBudgetCreator){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PRIVILIGES));
                return;
            }else{
               displayAwardDetails(TypeConstants.MODIFY_MODE);
               refresh();
            }
        }else if(typeCode == REBUDGET && statusCode == IN_PROGRESS){
            if(!isAwardBudgetModifier){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PRIVILIGES));
                return;
            }else{
                displayAwardDetails(TypeConstants.MODIFY_MODE);
                refresh();
            }
        }else if(statusCode == POSTED){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_MODIFY_ROW));
            return;
        }else{
            //The will be executed when the status is Submitted
            displayAwardDetails(TypeConstants.MODIFY_MODE);
            refresh();
        }
    }
    
    private void displayAwardDetails(char funType) throws CoeusException{
        int rowCount = awardBudgetSummaryForm.tblBudgetSummary.getRowCount();
        if(rowCount == 0 && funType != TypeConstants.NEW_MODE){
            if(funType==TypeConstants.MODIFY_MODE){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_ROWS_TO_MODIFY));
                return;
            }else if(funType==TypeConstants.REBUDGET_MODE){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_ROW_TO_REBUDGET));
                return;
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_ROW_TO_DISPLAY));
                return;
            }
        }        
        AwardBudgetSummaryBean awardBudgetSummaryBean = new AwardBudgetSummaryBean();
        if(funType != TypeConstants.NEW_MODE){
            int selRow = awardBudgetSummaryForm.tblBudgetSummary.getSelectedRow();
            awardBudgetSummaryBean = (AwardBudgetSummaryBean)cvSummaryData.get(selRow);
        }
        if(funType == TypeConstants.NEW_MODE){
            awardBudgetSummaryBean.setBudgetVersion(0);
            awardBudgetSummaryBean.setSequenceNumber(sequenceNo);
            awardBudgetSummaryBean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
        }
        //awardBudgetSummaryBean.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber());
        awardBudgetSummaryBean.setMitAwardNumber(awardNumber);
        //awardBudgetSummaryBean.setSequenceNumber(sequenceNo);
        CoeusVector cvRights = new CoeusVector();
        cvRights.add(getOspLevelRight());
        cvRights.add(getUnitLevelRight());
        try{
            awardBudgetSummaryForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            AwardBudgetDetailController awardBudgetDetailController =
                    new AwardBudgetDetailController(awardBudgetSummaryBean,funType,awardStatusCode);
            awardBudgetDetailController.setFormData(cvRights);
            awardBudgetDetailController.display();
            awardBudgetDetailController.cleanUp();
            awardBudgetDetailController = null;
        }catch (CoeusException ce){
            // 4031: Unknown error while scrolling the awards list - Start
            ce.setMessage("An error occured while displaying the Budget Window. Contact coeus support team. ");
            // 4031: Unknown error while scrolling the awards list - End
            throw ce;
        }catch (Exception e){
            e.printStackTrace();
            throw new CoeusException(e.getMessage());
        }finally{
            awardBudgetSummaryForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
   private void performCopyAction() throws CoeusException{
        // check the rights
        if(!isAwardBudgetCreator){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PRIVILIGES));
            return;
        }
        awardBudgetSummaryForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        AwardBudgetCopyController awardBudgetCopyController =
                             new AwardBudgetCopyController(awardAmountInfoBean);
        try{
            awardBudgetCopyController.registerComponents();
            awardBudgetCopyController.setFormData(cvCopyData);
            awardBudgetCopyController.postInitComponents();
            awardBudgetCopyController.display();
            awardBudgetCopyController.cleanUp();
            
        }catch (CoeusException ce){
            ce.printStackTrace();
        }finally{
            awardBudgetSummaryForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
        if(awardBudgetCopyController.isOK_CLICKED()){
            awardBudgetCopyController = null;
            refresh();
            displayAwardDetails(TypeConstants.MODIFY_MODE);
        }
    }
    
    public void refresh() throws CoeusException{
        setFormData(null);
        int rowCount = awardBudgetSummaryForm.tblBudgetSummary.getRowCount();
        if(rowCount > 0){
            awardBudgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(cvSummaryData.size()-1, cvSummaryData.size()-1);
        }
    }//End refersh
    
    private void performCloseAction(){
        dlgAwardBudgetSummary.dispose();
    }
    
    private void requestDefaultFocus(){
        if(functionType==TypeConstants.DISPLAY_MODE){
            awardBudgetSummaryForm.btnClose.requestFocus();
        }else{
            awardBudgetSummaryForm.btnDisplay.requestFocus();
        }
    }
    
    public void cleanUp(){
        awardBudgetSummaryForm.btnClose.removeActionListener(this);
        awardBudgetSummaryForm.btnNew.removeActionListener(this);
        awardBudgetSummaryForm.btnModify.removeActionListener(this);
        awardBudgetSummaryForm.btnRebudget.removeActionListener(this);
        awardBudgetSummaryForm.btnDelete.removeActionListener(this);
        awardBudgetSummaryForm.btnCopy.removeActionListener(this);
        awardBudgetSummaryForm.btnDisplay.removeActionListener(this);
        awardBudgetSummaryForm.tblBudgetSummary.removeMouseListener(this);
        awardBudgetSummaryForm.tblBudgetSummary.getSelectionModel().removeListSelectionListener(this);
        
        
        awardBudgetSummaryRenderer=null;
        awardBudgetSummaryTableModel=null;
        awardAmountInfoBean = null;
        cvSummaryData=null;
        cvCopyData = null;
        awardBudgetSummaryForm=null;
        dlgAwardBudgetSummary = null;
    }
    
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = awardBudgetSummaryForm.tblBudgetSummary.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            tableHeader.setMaximumSize(new Dimension(100,40));
            tableHeader.setMinimumSize(new Dimension(100,40));
            tableHeader.setPreferredSize(new Dimension(100,40));
            awardBudgetSummaryForm.tblBudgetSummary.setRowHeight(22);
            awardBudgetSummaryForm.tblBudgetSummary.setSelectionBackground(java.awt.Color.yellow);
            awardBudgetSummaryForm.tblBudgetSummary.setSelectionForeground(java.awt.Color.white);
            awardBudgetSummaryForm.tblBudgetSummary.setShowHorizontalLines(true);
            awardBudgetSummaryForm.tblBudgetSummary.setShowVerticalLines(true);
            awardBudgetSummaryForm.tblBudgetSummary.setOpaque(false);
            awardBudgetSummaryForm.tblBudgetSummary.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
            TableColumn columnDetails;
            /*
             *UserId to UserName Enhancement - Start
             *Modified width of the user id field to display username
             */
//            int size[] = {70,70,70,70,70,90,80,70,130};
            int size[] = {70,70,70,70,70,90,80,130,130};
            //UserId to UserName Enhancement - End
            for(int index=0;index<size.length;index++){
                columnDetails=awardBudgetSummaryForm.tblBudgetSummary.getColumnModel().getColumn(index);
                columnDetails.setCellRenderer(awardBudgetSummaryRenderer);
                columnDetails.setPreferredWidth(size[index]);
            }
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    public void saveFormData() throws CoeusException{
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        awardBudgetSummaryForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        int selectedRow = awardBudgetSummaryForm.tblBudgetSummary.getSelectedRow();
        AwardBudgetSummaryBean awardBudgetSummaryBean = 
                            (AwardBudgetSummaryBean)cvSummaryData.get(selectedRow);
        request.setFunctionType(SAVE_SUMMARY_DATA);
        request.setDataObject(awardBudgetSummaryBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
               awardBudgetSummaryForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }else{
               awardBudgetSummaryForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
               Exception ex = response.getException();
               ex.printStackTrace();
            }
        }else{
            awardBudgetSummaryForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
        
    }
    public boolean validate() {
        return false;
    }
    
    public void display() {
        if(awardBudgetSummaryForm.tblBudgetSummary.getRowCount() > 0){
            awardBudgetSummaryForm.tblBudgetSummary.setRowSelectionInterval(0,0);
        }
        dlgAwardBudgetSummary.setVisible(true);
    }
    
    public class AwardBudgetSummaryTableModel extends AbstractTableModel{
    	// JM 5-14-2012 updated labels for Sequence and Start Date
    	String sequenceNumber = "<html>Sequence<br>Number</html>";
        String budgetVersion = "<html>Budget<br>Version</html>";
        String expirationDate = "<html>Expiration<br>  Date</html>";
        String budgetAmount = "<html>Budget<br>Amount</html>";
        String updateTimeStamp = "<html>Update<br>Timestamp</html>";
        String startDate = "<html>Start Date<br></html>";
        String budgetStatus = "<html>Budget<br>Status</html>";
        /*
         *UserId to UserName Enhancement - Start
         *Modified to display the label Update User in a single line
         */
//        String updateBy= "<html>Update<br>User</html>";
        String updateBy= "<html>Update User<br></html>";
        //UserId to UserName Enhancement - End
        String desc = "<html>Type<br></html>";
        private Class colClass[] = {Integer.class, Integer.class, String.class,String.class,String.class,String.class,String.class,String.class,String.class};
        private String colNames[] = {budgetVersion,sequenceNumber,startDate,expirationDate,budgetStatus,budgetAmount,desc,updateBy,updateTimeStamp};
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        public String getColumnName(int column){
            return colNames[column];
        }
        public int getRowCount() {
            if(cvSummaryData==null){
                return 0;
            }else{
                return cvSummaryData.size();
            }
        }
        public void setData(CoeusVector cvSummaryData){
            cvSummaryData = cvSummaryData;
            
        }
       public Object getValueAt(int row, int col) {
            AwardBudgetSummaryBean bean = (AwardBudgetSummaryBean)cvSummaryData.get(row);
            switch(col){
                case BUDGET_VERSION_COLUMN:
                    return new Integer(bean.getBudgetVersion());
                case SEQUENCE_COLUMN:
                    return new Integer(bean.getSequenceNumber());
                case START_DATE_COLUMN:
                    return bean.getStartDate();
                case EXPIRATION_DATE_COLUMN:
                    return bean.getExpirationDate();
                case BUDGET_STATUS_COLUMN:
                    return bean.getBudgetStatusDescription();
                case BUDGET_AMOUNT_COLUMN:
                    return new Double(bean.getBudgetAmount());
                case DESCRIPTION_COLUMN:
                    return bean.getAwardBudgetTypeDesc();
                case UPDATE_USER_COLUMN:
//                    return bean.getUpdateUser();
                    /*
                     *UserId to UserName Enhancement - Start
                     *Added UserUtils class to change userid to username
                     */
                    return UserUtils.getDisplayName(bean.getUpdateUser().trim());
                    // UserId to UserName Enhancement - End
                case UPDATE_TIMESTAMP_COLUMN:
                    return bean.getUpdateTimestamp();
            }
            return EMPTY_STRING;
        }
    }
    
    /** Table model for AwardBudgetSaummary Copy */
   class AwardBudgetSummaryRenderer extends DefaultTableCellRenderer{
        private JLabel lblComponent,lblCenterComponent,lblRightComponent;
        private DollarCurrencyTextField txtAmount;
        public AwardBudgetSummaryRenderer(){
            lblComponent = new JLabel();
            lblCenterComponent=new JLabel();
            lblCenterComponent.setOpaque(true);
            lblComponent.setOpaque(true);
            lblCenterComponent.setHorizontalAlignment(JLabel.CENTER);
            lblRightComponent=new JLabel();
            lblRightComponent.setOpaque(true);
            lblRightComponent.setHorizontalAlignment(JLabel.RIGHT);
            
            txtAmount=new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            txtAmount.setBorder(new EmptyBorder(0,0,0,0));
        }
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            switch(col){
                case BUDGET_VERSION_COLUMN:
                    if(functionType==TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                            lblCenterComponent.setBackground(java.awt.Color.YELLOW);
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblCenterComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }
                    }else
                        if(isSelected){
                            lblCenterComponent.setBackground(java.awt.Color.YELLOW);
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }
                        else{
                            lblCenterComponent.setBackground(java.awt.Color.white);
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        lblCenterComponent.setText(EMPTY_STRING);
                    }else{
                        lblCenterComponent.setText(""+value.toString());
                    }
                    return lblCenterComponent;
                case SEQUENCE_COLUMN:
                    if(functionType==TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                            lblCenterComponent.setBackground(java.awt.Color.YELLOW);
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblCenterComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }
                    }else
                        if(isSelected){
                            lblCenterComponent.setBackground(java.awt.Color.YELLOW);
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }
                        else{
                            lblCenterComponent.setBackground(java.awt.Color.white);
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        lblCenterComponent.setText(EMPTY_STRING);
                    }else{
                        lblCenterComponent.setText(""+value);
                    }
                    return lblCenterComponent;
                case START_DATE_COLUMN:
                case EXPIRATION_DATE_COLUMN:
                    if(functionType==TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                            lblCenterComponent.setBackground(java.awt.Color.YELLOW);
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblCenterComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }
                    }else
                        if(isSelected){
                            lblCenterComponent.setBackground(java.awt.Color.YELLOW);
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }
                        else{
                            lblCenterComponent.setBackground(java.awt.Color.white);
                            lblCenterComponent.setForeground(java.awt.Color.black);
                        }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        lblCenterComponent.setText(EMPTY_STRING);
                    }else{
                        value = dateUtils.formatDate(value.toString(),DISPLAY_DATE_FORMAT);
                        lblCenterComponent.setText(value.toString());
                    }
                    return lblCenterComponent;
                    
                case BUDGET_STATUS_COLUMN:
                    if(functionType==TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                            lblComponent.setBackground(java.awt.Color.YELLOW);
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }else
                        if(isSelected){
                            lblComponent.setBackground(java.awt.Color.YELLOW);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                        else{
                            lblComponent.setBackground(java.awt.Color.white);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        lblComponent.setText(EMPTY_STRING);
                    }else{
                        lblComponent.setText(value.toString());
                    }
                    return lblComponent;
                case BUDGET_AMOUNT_COLUMN:
                    if(functionType==TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                            lblRightComponent.setBackground(java.awt.Color.YELLOW);
                            lblRightComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblRightComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblRightComponent.setForeground(java.awt.Color.black);
                        }
                    }else
                        if(isSelected){
                            lblRightComponent.setBackground(java.awt.Color.YELLOW);
                            lblRightComponent.setForeground(java.awt.Color.black);
                        }
                        else{
                            lblRightComponent.setBackground(java.awt.Color.white);
                            lblRightComponent.setForeground(java.awt.Color.black);
                        }
                    value=(value==null?"0.00":value);
                    txtAmount.setValue(new Double(value.toString()).doubleValue());
//                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
//                        lblRightComponent.setText(EMPTY_STRING);
//                        
//                    }else{
//                        lblRightComponent.setText(value.toString());
//                    }
                    lblRightComponent.setText(txtAmount.getText());
                    return lblRightComponent;
                case DESCRIPTION_COLUMN:
                    if(functionType==TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                            lblComponent.setBackground(java.awt.Color.YELLOW);
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }else
                        if(isSelected){
                            lblComponent.setBackground(java.awt.Color.YELLOW);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                        else{
                            lblComponent.setBackground(java.awt.Color.WHITE);
                            lblComponent.setForeground(java.awt.Color.BLACK);
                        }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        lblComponent.setText(EMPTY_STRING);
                    }else{
                        lblComponent.setText(value.toString());
                    }
                    return lblComponent;
                case UPDATE_USER_COLUMN:
                    if(functionType==TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                            lblComponent.setBackground(java.awt.Color.YELLOW);
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }else
                        if(isSelected){
                            lblComponent.setBackground(java.awt.Color.YELLOW);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                        else{
                            lblComponent.setBackground(java.awt.Color.white);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        lblComponent.setText(EMPTY_STRING);
                    }else{
                        lblComponent.setText(value.toString());
                    }
                    return lblComponent;
                case UPDATE_TIMESTAMP_COLUMN:
                    if(functionType==TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                            lblComponent.setBackground(java.awt.Color.YELLOW);
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }else
                        if(isSelected){
                            lblComponent.setBackground(java.awt.Color.YELLOW);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                        else{
                            lblComponent.setBackground(java.awt.Color.white);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        lblComponent.setText(EMPTY_STRING);
                    }else{
                        value = simpleDateFormat.format(value);
                        lblComponent.setText(value.toString());
                    }
                    return lblComponent;
            }
            return lblComponent;
        }//End getTableCellRendererComponent
    }//End AwardBudgetSummaryRenderer
    
     private CoeusVector getAwardBudgetCopyData(){
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
               Exception ex = response.getException();
               ex.printStackTrace();
            }
        }
        return cvCopyData;
    }
     //COEUSQA-3937
      private boolean checkWhetherToUpdSeqInBugInfo(){   
         Boolean returnValue  = false;  
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setId(awardAmountInfoBean.getMitAwardNumber());
        request.setFunctionType(CHECK_UPD_SEQ_IN_BUD_INFO);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                 returnValue = (Boolean)response.getParameterValue();
            }else{
               Exception ex = response.getException();
               ex.printStackTrace();
            }
        }
       return returnValue;
    }
   //COEUSQA-3937
     public void valueChanged(ListSelectionEvent listSelectionEvent ) {
         if(functionType == TypeConstants.DISPLAY_MODE){
             return ;
         }
         if(listSelectionEvent.getValueIsAdjusting()){
             return ;
         }
         boolean rebudget = false;
         boolean modify = false;
         boolean delete = false;
         boolean newMode = true;
         boolean status = false;
         int selectedRow = awardBudgetSummaryForm.tblBudgetSummary.getSelectedRow();
         if(selectedRow != -1){
             AwardBudgetSummaryBean awardBudgetSummaryBean = 
                            (AwardBudgetSummaryBean)cvSummaryData.get(selectedRow);
         int statusCode = awardBudgetSummaryBean.getBudgetStatusCode();
             
             switch(statusCode){
                 case IN_PROGRESS:
                     delete = true;
                     modify = true;
                     break;
                 
                 case SUBMITTED:
                     modify = true;
                     break;
                 
                 case POSTED:
                     if(selectedRow == cvSummaryData.size()-1 &&
                        awardStatusCode != INACTIVE && 
                        awardStatusCode != TERMINATED){
                         rebudget = true;
                     }
                     break;
                     
                 case TO_BE_POSTED:
                     newMode = false;
                     if(selectedRow == cvSummaryData.size()-1 && 
                        isAwardBudgetAdmin){
                         status = true;
                     }
                     break;
                 
                 case REJECTED:
                     delete = true;
                     modify = true;
                     break;
             }
             awardBudgetSummaryForm.btnRebudget.setEnabled(rebudget);
             awardBudgetSummaryForm.btnModify.setEnabled(modify);
             awardBudgetSummaryForm.btnDelete.setEnabled(delete);
             awardBudgetSummaryForm.btnChangeStatus.setEnabled(status);
             awardBudgetSummaryForm.btnChangeStatus.setVisible(status);
         }//end of if
     }//End valueChanged
}
