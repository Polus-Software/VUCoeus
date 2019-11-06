/*
 * AwardBudgetDetailController.java
 *
 * Created on August 1, 2005, 4:14 PM
 */

/* PMD check performed, and commented unused imports and variables 
 * on 24-AUG-2011 by Bharati
 */

package edu.mit.coeus.award.controller;
import edu.mit.coeus.award.bean.AwardBudgetDetailBean;
import edu.mit.coeus.award.bean.AwardBudgetHeaderBean;
import edu.mit.coeus.award.bean.AwardBudgetSummaryBean;
import edu.mit.coeus.award.gui.AwardBudgetDetailForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.budget.gui.CostElementMessageForm;
//import edu.mit.coeus.budget.controller.BudgetBaseWindowController;
//import edu.mit.coeus.departmental.bean.DepartmentBudgetFormBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CostElementsLookupWindow;
import edu.mit.coeus.utils.AppletServletCommunicator;
//import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
//import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.OtherLookupBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.NotEquals;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
// JM 6-16-2011 added package call to assist with automated actions
import java.awt.AWTException;
import java.awt.Robot;
// END
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
/**
 *
 * @author  Tarique
 */
public class AwardBudgetDetailController extends AwardBudgetController 
                                        implements ActionListener, MouseListener,ItemListener {
    private Vector cvDetailData;
    private AwardBudgetSummaryBean awardBudgetSummaryBean;
    private static final String EMPTY_STRING = "";
    private AwardBudgetDetailForm awardBudgetDetailForm;
    private static final int LINE_COLUMN=0;
    private static final int COST_COLUMN=1;
    private static final int COST_DESCR_COLUMN=2;
    private static final int CHANGE_COLUMN=3;
    private static final int OBLIGATED_COLUMN=4;
    private static final int AMOUNT_COLUMN=5;
    private static final int TOTAL_COLUMN=0;
    private static final int CHANGE_AMOUNT=1;
    private static final int TOTAL_OBL_AMOUNT=2;
    private static final int TOTAL_AMOUNT=3;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 550;
    private AwardBudgetHeaderBean awardBudgetHeaderBean;
    private AwardBudgetDetailTableModel awardBudgetDetailTableModel;
    private AwardBudgetDetailTableCellRenderer awardBudgetDetailTableCellRenderer;
    private AwardBudgetDetailTotalTableModel awardBudgetDetailTotalTableModel;
    private AwardBudgetDetailEditor awardBudgetDetailEditor;
    private AwardBudgetOverHeadEditor awardBudgetOverHeadEditor;
    private AwardBudgetOvrheadTableModel awardBudgetOvrheadTableModel;
    private AwardBudgetOverHeadTotalTableModel awardBudgetOverHeadTotalTableModel;
    private AwardBudgetTotalTableCellRenderer awardBudgetTotalTableCellRenderer;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusDlgWindow dlgAwardBudgetMain;
    private javax.swing.JButton btnPost;
    private javax.swing.JButton btnApproval;
    private javax.swing.JButton btnClose;
    private static final String NO_PRIVILIGES="awardBudgetSummary_exceptionCode.2001";
    private static final String NO_ROW_TO_DELETE="awardBudgetSummary_exceptionCode.2005";
    private static final String SELECT_ROW_FOR_PROCEED="awardBudgetSummary_exceptionCode.2003";
    private static final String DELETE_LINE_ITEM="awardBudgetSummary_exceptionCode.2007";
    private static final String VALID_DESCRIPTION="awardBudgetSummary_exceptionCode.2009";
    private static final String VALID_COMMENT="awardBudgetSummary_exceptionCode.2010";
    private static final String HAS_AN_OBLIGATED_AMT="awardBudgetSummary_exceptionCode.2011";
    private static final String SAVE_CHANGES="awardBudgetSummary_exceptionCode.2012";
    private static final String INVALID_COST_ELEMENT="awardBudgetSummary_exceptionCode.2013";
    private static final String SAVE_SUCCESS="awardBudgetSummary_exceptionCode.2014";
    private static final String MODIFICATION_BUDGET_ZERO="awardBudgetSummary_exceptionCode.2015";
    private static final String ENTER_COST_ELEMENT="awardBudgetSummary_exceptionCode.2017";
    private static final String TOTAL_NOT_EQ_OBLAMT_SAVE="awardBudgetSummary_exceptionCode.2018";
    private static final String TOTAL_NOT_EQ_OBLAMT="awardBudgetSummary_exceptionCode.2019";
    private static final String TYPE_COM_FOR_REJ="awardBudgetSummary_exceptionCode.2020";
    private static final String APPROVE_PROCEED="awardBudgetSummary_exceptionCode.2021";
    private static final String BUDGET_SUM_TO_OBLAMT="awardBudgetSummary_exceptionCode.2022";
    private static final String REBUDGET_CHANGE_TO_SUM_ZERO="awardBudgetSummary_exceptionCode.2023";
    private static final String SUBMIT_PROCEED="awardBudgetSummary_exceptionCode.2024";
    private static final String POST_PROCEED="awardBudgetSummary_exceptionCode.2028";
    private static final String RECALCULATE_PROCEED="awardBudgetSummary_exceptionCode.2029";
    private static final String CALCULATE_PROCEED="awardBudgetSummary_exceptionCode.2030";
    private static final String NO_LINE_ITEMS_FOR_CALC = "awardBudgetSummary_exceptionCode.2035";
    private static final String SIMPLE_DATE_FORMAT = "dd-MMM-yyyy";
    private boolean successResponse=false;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    private static final String AWARD_BUDGET_SERVLET = "/AwardBudgetMaintainanceServlet";
    private static final String COST_ELEMENT_LOOKUP_TITLE = "Cost Elements";
    private CostElementsLookupWindow costElementsLookupWindow;
    private CoeusVector cvTableDetails;
    private CoeusVector cvDeletedItem;
    private CoeusVector cvCalCE;
    private CoeusVector cvTableOverHead;
    private CoeusVector cvCostElements;
    private CoeusVector cvType;
    private CoeusVector cvBudgetStatus;
    private CoeusVector cvMaxLineNo;
    private CoeusVector cvOHRatesTypes;
    //private CoeusVector cvToBeDelCalcCE;
    private CoeusVector cvMasterData;
    
    private Integer awdBudCampusBasedOnCE;
    private String postStatus;
    private static final String POST_ENABLED="1";
    private double totalAmt=0;
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    private static final char GET_INACTIVE_COST_ELEMENTS = '3';
    private Vector vecCEMessages;
    //COEUSQA-3273  - end
    
    private CoeusMessageResources coeusMessageResources;
    private static final char GET_AWARD_BUDGET_DATA = 'E';
    private static final char SAVE_AWARD_BUDGET = 'F';
    private static final char GET_AWARD_BUDGET_CE_DETAILS = 'G';
    private static final char CALCULATE_COST_ELEMENT = 'I';
    private static final char SAVE_SUMMARY_DATA = 'H';
    private static String APPROVE_POST_LABEL="Approve/Post";
    private static String APPROVE_LABEL="Approve";
    
    private boolean setFocus=false;
    private boolean modified=false;
    private int duplicateLineNo=0;
    private String strTitle;
    private int awardStatus;
    //Added for case #2100 start 1
    private static final String ENTER_ACCOUNT_NO = "awardBudgetSummary_exceptionCode.2034";
    //Added for case #2100 end 1
    /** Creates a new instance of AwardBudgetMainController */
    public AwardBudgetDetailController(
    AwardBudgetSummaryBean awardBudgetSummaryBean , char functionType,int awardStatus)throws CoeusException{
        this.awardBudgetSummaryBean = awardBudgetSummaryBean;
        coeusMessageResources=CoeusMessageResources.getInstance();
        this.awardStatus=awardStatus;
        registerComponents();
        setFunctionType(functionType);
        setTableEditors();
        postInitComponents();
        setTableKeyTraversal();
     }
    
    public void registerComponents() {
        cvDeletedItem=new CoeusVector();
        btnApproval=new javax.swing.JButton();
        btnApproval.setFont(CoeusFontFactory.getLabelFont());
        btnApproval.setMaximumSize(new java.awt.Dimension(90, 23));
        btnApproval.setMinimumSize(new java.awt.Dimension(90, 23));
        btnApproval.setPreferredSize(new java.awt.Dimension(90, 23));
        btnPost=new javax.swing.JButton();
        btnPost.setFont(CoeusFontFactory.getLabelFont());
        btnPost.setMnemonic('p');
        btnPost.setText("Post");
        btnPost.setMaximumSize(new java.awt.Dimension(90, 23));
        btnPost.setMinimumSize(new java.awt.Dimension(90, 23));
        btnPost.setPreferredSize(new java.awt.Dimension(90, 23));
        btnClose=new javax.swing.JButton();
        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('e');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(90, 23));
        btnClose.setMinimumSize(new java.awt.Dimension(90, 23));
        btnClose.setPreferredSize(new java.awt.Dimension(90, 23));
        awardBudgetDetailForm = new AwardBudgetDetailForm();
        awardBudgetDetailTableModel=new AwardBudgetDetailTableModel();
        awardBudgetDetailTotalTableModel=new AwardBudgetDetailTotalTableModel();
        awardBudgetTotalTableCellRenderer=new AwardBudgetTotalTableCellRenderer();
        awardBudgetDetailTableCellRenderer=new AwardBudgetDetailTableCellRenderer();
        awardBudgetDetailEditor=new AwardBudgetDetailEditor();
        awardBudgetOverHeadEditor=new AwardBudgetOverHeadEditor();
        awardBudgetDetailForm.tblAwardDetail.setModel(awardBudgetDetailTableModel);
        awardBudgetDetailForm.tblDetailTotal.setModel(awardBudgetDetailTotalTableModel);
        
        awardBudgetOvrheadTableModel=new AwardBudgetOvrheadTableModel();
        awardBudgetDetailForm.tblAwardOverhead.setModel(awardBudgetOvrheadTableModel);
        awardBudgetOverHeadTotalTableModel=new AwardBudgetOverHeadTotalTableModel();
        
        awardBudgetDetailForm.tblTotal.setModel(awardBudgetOverHeadTotalTableModel);
        awardBudgetDetailForm.tblAwardDetail.addMouseListener(this);
        awardBudgetDetailForm.btnAdd.addActionListener(this);
        btnApproval.addActionListener(this);
        awardBudgetDetailForm.btnCalculate.addActionListener(this);
        awardBudgetDetailForm.btnCalculator.addActionListener(this);
        btnClose.addActionListener(this);
        awardBudgetDetailForm.btnDelete.addActionListener(this);
        awardBudgetDetailForm.btnReject.addActionListener(this);
        awardBudgetDetailForm.btnSave.addActionListener(this);
        awardBudgetDetailForm.btnSubmit.addActionListener(this);
        awardBudgetDetailForm.btnPrint.addActionListener(this);
        btnPost.addActionListener(this);
        
        awardBudgetDetailForm.chkOnOffCampusFlag.addItemListener(this);
    }
    
    public void setTableEditors(){
        try{
            JTableHeader tableHeader = awardBudgetDetailForm.tblAwardDetail.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setMaximumSize(new Dimension(100,40));
            tableHeader.setMinimumSize(new Dimension(100,40));
            tableHeader.setPreferredSize(new Dimension(100,40));
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            awardBudgetDetailForm.tblAwardDetail.setRowHeight(22);
            awardBudgetDetailForm.tblAwardDetail.setShowHorizontalLines(true);
            awardBudgetDetailForm.tblAwardDetail.setShowVerticalLines(true);
            awardBudgetDetailForm.tblAwardDetail.setOpaque(false);
            awardBudgetDetailForm.tblAwardDetail.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            awardBudgetDetailForm.tblAwardDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            awardBudgetDetailForm.tblDetailTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            awardBudgetDetailForm.tblDetailTotal.setRowHeight(22);
            awardBudgetDetailForm.tblDetailTotal.setShowHorizontalLines(false);
            awardBudgetDetailForm.tblDetailTotal.setOpaque(true);
            
            awardBudgetDetailForm.tblTotal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            awardBudgetDetailForm.tblTotal.setShowHorizontalLines(false);
            awardBudgetDetailForm.tblTotal.setOpaque(true);
            awardBudgetDetailForm.tblTotal.setRowHeight(22);
            
            awardBudgetDetailForm.tblAwardOverhead.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            awardBudgetDetailForm.tblAwardOverhead.setRowHeight(22);
            TableColumn columnDetails;
            TableColumn ovrheadColumnDetails;
            TableColumn detailTotal;
            TableColumn overHeadTotal;
            int size[] = {85,85,197,100,100,100};
            for(int index=0;index<size.length;index++){
                columnDetails=awardBudgetDetailForm.tblAwardDetail.getColumnModel().getColumn(index);
                columnDetails.setPreferredWidth(size[index]);
                columnDetails.setCellEditor(awardBudgetDetailEditor);
                columnDetails.setCellRenderer(awardBudgetDetailTableCellRenderer);
                ovrheadColumnDetails=awardBudgetDetailForm.tblAwardOverhead.getColumnModel().getColumn(index);
                ovrheadColumnDetails.setHeaderRenderer(new EmptyHeaderRenderer());
                ovrheadColumnDetails.setCellEditor(awardBudgetOverHeadEditor);
                ovrheadColumnDetails.setCellRenderer(awardBudgetDetailTableCellRenderer);
                ovrheadColumnDetails.setPreferredWidth(size[index]);
            }
            int totalSize[]={134,100,100,100};
            for(int index=0;index<totalSize.length;index++){
                detailTotal=awardBudgetDetailForm.tblDetailTotal.getColumnModel().getColumn(index);
                detailTotal.setPreferredWidth(totalSize[index]);
                detailTotal.setHeaderRenderer(new EmptyHeaderRenderer());
                detailTotal.setCellRenderer(awardBudgetTotalTableCellRenderer);
                
                overHeadTotal=awardBudgetDetailForm.tblTotal.getColumnModel().getColumn(index);
                overHeadTotal.setHeaderRenderer(new EmptyHeaderRenderer());
                overHeadTotal.setCellRenderer(awardBudgetTotalTableCellRenderer);
                overHeadTotal.setPreferredWidth(totalSize[index]);
            }
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        
     }
    public void postInitComponents() throws CoeusException{
        dlgAwardBudgetMain = new CoeusDlgWindow(mdiForm);
        dlgAwardBudgetMain.setResizable(false);
        dlgAwardBudgetMain.setModal(true);
        dlgAwardBudgetMain.getContentPane().add(awardBudgetDetailForm);
        dlgAwardBudgetMain.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardBudgetMain.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardBudgetMain.getSize();
        dlgAwardBudgetMain.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgAwardBudgetMain.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardBudgetMain.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        dlgAwardBudgetMain.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCloseAction();
            }
        });
        
        dlgAwardBudgetMain.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCloseAction();
            }
        });
    }
    private void requestDefaultFocus(){
        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
            btnClose.requestFocusInWindow();
        }else{
            awardBudgetDetailForm.btnSave.requestFocusInWindow();
        }
        if(awardBudgetDetailForm.tblAwardDetail.getRowCount()!=0){
            awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(0,0);
        }
    }
    private void setApprovePostButtons(String postStatus){
        java.awt.GridBagConstraints gridBagConstraints;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        
        if(postStatus.trim().equalsIgnoreCase(POST_ENABLED)){
            java.awt.Component[] components = {
                awardBudgetDetailForm.btnSave, awardBudgetDetailForm.btnSubmit,awardBudgetDetailForm.btnReject,
                btnApproval,btnPost,btnClose,
                awardBudgetDetailForm.btnAdd,awardBudgetDetailForm.btnDelete,awardBudgetDetailForm.btnPrint,
                awardBudgetDetailForm.btnCalculator,awardBudgetDetailForm.btnCalculate,
                awardBudgetDetailForm.txtDescription,awardBudgetDetailForm.txtArComments,awardBudgetDetailForm.scrPnAwardBudgetDetail
            };
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            awardBudgetDetailForm.setFocusTraversalPolicy(traversePolicy);
            awardBudgetDetailForm.setFocusCycleRoot(true);
            btnPost.setVisible(true);
            btnApproval.setText(APPROVE_LABEL);
            btnApproval.setMnemonic('v');
            awardBudgetDetailForm.add(btnApproval, gridBagConstraints);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 4;
            gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
            awardBudgetDetailForm.add(btnPost, gridBagConstraints);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
            //gridBagConstraints.fill=java.awt.GridBagConstraints.VERTICAL;
            gridBagConstraints.anchor=java.awt.GridBagConstraints.NORTHEAST;
            awardBudgetDetailForm.add(btnClose, gridBagConstraints);
        }else{
            java.awt.Component[] components = {
                awardBudgetDetailForm.btnSave, awardBudgetDetailForm.btnSubmit,awardBudgetDetailForm.btnReject,
                btnApproval,btnClose,
                awardBudgetDetailForm.btnAdd,awardBudgetDetailForm.btnDelete,awardBudgetDetailForm.btnPrint,
                awardBudgetDetailForm.btnCalculator,awardBudgetDetailForm.btnCalculate,
                awardBudgetDetailForm.txtDescription,awardBudgetDetailForm.txtArComments,awardBudgetDetailForm.scrPnAwardBudgetDetail
            };
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            awardBudgetDetailForm.setFocusTraversalPolicy(traversePolicy);
            awardBudgetDetailForm.setFocusCycleRoot(true);
            btnApproval.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            btnApproval.setMargin(new java.awt.Insets(2, 4, 0, 0));
            btnPost.setVisible(false);
            btnApproval.setText(APPROVE_POST_LABEL);
            btnApproval.setMnemonic('v');
            awardBudgetDetailForm.add(btnApproval, gridBagConstraints);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 6;
            gridBagConstraints.gridy = 4;
            gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
            //gridBagConstraints.fill=java.awt.GridBagConstraints.VERTICAL;
            //gridBagConstraints.anchor=java.awt.GridBagConstraints.NORTHEAST;
            awardBudgetDetailForm.add(btnClose, gridBagConstraints);
            
        }
    }
    public void display() {
        
        awardBudgetDetailForm.txtAccountNo.setEnabled(false);
        awardBudgetDetailForm.txtAwardNo.setEnabled(false);
        awardBudgetDetailForm.txtBudgetStatus.setEnabled(false);
        awardBudgetDetailForm.txtEndDate.setEnabled(false);
        awardBudgetDetailForm.txtStartDate.setEnabled(false);
        awardBudgetDetailForm.txtSequenceNo.setEnabled(false);
        awardBudgetDetailForm.txtVersion.setEnabled(false);
        awardBudgetDetailForm.txtObligatedAmt.setEnabled(false);
        awardBudgetDetailForm.txtBudgetAmt.setEnabled(false);
        dlgAwardBudgetMain.setVisible(true);
    }
    /*
     *Method to set the window title
     */
    private void setWindowTitle(String awardBudgetType,String budgetStatus){
        
        ComboBoxBean cmbType=null,cmbStatus=null;
        
        Equals eqBudgetType = new Equals("code" ,awardBudgetType);
        CoeusVector cvFilterBudgetType = cvType.filter(eqBudgetType);
        if(cvFilterBudgetType.size()>0){
            cmbType = (ComboBoxBean)cvFilterBudgetType.get(0);
        }
        Equals eqBudgetStatus = new Equals("code" ,budgetStatus);
        CoeusVector cvFilterBudgetStatus = cvBudgetStatus.filter(eqBudgetStatus);
        if(cvFilterBudgetStatus.size()>0){
            cmbStatus = (ComboBoxBean)cvFilterBudgetStatus.get(0);
        }
        
        dlgAwardBudgetMain.setTitle("Award Budget: " + cmbType.getDescription() 
                                       +" " + cmbStatus.getDescription());
     }
    /*
     * Method for setting data
     **/
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        CoeusVector cvRights = (CoeusVector)data;
        setOspLevelRight((Hashtable)cvRights.get(0));
        setUnitLevelRight((Hashtable)cvRights.get(1));
        
        cvDetailData = new Vector();
        cvCostElements=new CoeusVector();
        cvTableDetails=new CoeusVector();
        cvDetailData = getAwardDetailsData();
        cvType=new CoeusVector();
        cvBudgetStatus=new CoeusVector();
        cvMaxLineNo=new CoeusVector();
        cvOHRatesTypes = new CoeusVector();
        //cvToBeDelCalcCE = new CoeusVector();
        cvMasterData = new CoeusVector();
        
        if(cvDetailData!=null&&cvDetailData.size()>0){
            awardBudgetHeaderBean = (AwardBudgetHeaderBean)cvDetailData.get(0);
            if(getFunctionType()==TypeConstants.REBUDGET_MODE){
                awardBudgetHeaderBean.setVersionNo(awardBudgetHeaderBean.
                                                            getVersionNo()+1);
                awardBudgetHeaderBean.setBudgetStatusCode(IN_PROGRESS);
                awardBudgetHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
                awardBudgetHeaderBean.setOblChangeAmount(0);
                awardBudgetHeaderBean.setDescription("Rebudget");
                awardBudgetHeaderBean.setComments("");
            }
            if(getFunctionType()==TypeConstants.NEW_MODE){
                awardBudgetHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
            }
            if(getFunctionType()==TypeConstants.MODIFY_MODE){
                if(awardBudgetHeaderBean.getBudgetStatusCode()==REJECTED){
                    awardBudgetHeaderBean.setBudgetStatusCode(IN_PROGRESS);
                    awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
            
            cvType = (CoeusVector)cvDetailData.get(2);
            cvCalCE = (CoeusVector)cvDetailData.get(3);
            cvCostElements=(CoeusVector)cvDetailData.get(4);
            cvBudgetStatus=(CoeusVector)cvDetailData.get(5);
            cvOHRatesTypes = (CoeusVector)cvDetailData.get(6);
            awdBudCampusBasedOnCE = (Integer)cvDetailData.get(7);
            postStatus=(String)cvDetailData.get(8);
            
            setApprovePostButtons(postStatus);
            
            awardBudgetDetailForm.cmbOhRateType.setModel(new DefaultComboBoxModel(cvOHRatesTypes));
//            int rateTypeCode = awardBudgetHeaderBean.getOhRateTypeCode();
            int rateTypeCode = awardBudgetHeaderBean.getOhRateClassCode();
            Equals eqRateTypeCode = new  Equals("code", ""+rateTypeCode);
            CoeusVector cvFilteredType = cvOHRatesTypes.filter(eqRateTypeCode);
            if(cvFilteredType != null && cvFilteredType.size()>0){
                ComboBoxBean cmbTypeCode = (ComboBoxBean)cvFilteredType.get(0);
                awardBudgetDetailForm.cmbOhRateType.setSelectedItem(cmbTypeCode);
            }
            
            awardBudgetDetailForm.chkOnOffCampusFlag.setSelected(awardBudgetHeaderBean.isOnOffCampusFlag());
            
            String budgetStatusCode = ""+awardBudgetHeaderBean.getBudgetStatusCode();
            Equals eqBudgetType = new Equals("code" ,budgetStatusCode);
            CoeusVector cvFilterBudgetStatus = cvBudgetStatus.filter(eqBudgetType);
            if(cvFilterBudgetStatus.size()>0){
                ComboBoxBean cmbType = (ComboBoxBean)cvFilterBudgetStatus.get(0);
                awardBudgetDetailForm.txtBudgetStatus.setText(cmbType.getDescription());
            }
            awardBudgetDetailForm.txtAccountNo.setText(awardBudgetHeaderBean.getAccountNumber());
            awardBudgetDetailForm.txtAwardNo.setText(awardBudgetHeaderBean.getMitAwardNumber());
            awardBudgetDetailForm.txtEndDate.setText(""+simpleDateFormat.format(awardBudgetHeaderBean.getEndDate()));
            awardBudgetDetailForm.txtStartDate.setText(""+simpleDateFormat.format(awardBudgetHeaderBean.getStartDate()));
            awardBudgetDetailForm.txtSequenceNo.setText(""+awardBudgetHeaderBean.getSequenceNumber());
            awardBudgetDetailForm.txtVersion.setText(""+awardBudgetHeaderBean.getVersionNo());
            awardBudgetDetailForm.txtObligatedAmt.setText(""+awardBudgetHeaderBean.getOblDisributableAmount());
            awardBudgetDetailForm.txtBudgetAmt.setText(""+awardBudgetHeaderBean.getOblChangeAmount());
            awardBudgetDetailForm.txtDescription.setText(awardBudgetHeaderBean.getDescription());
            String strComment
            =awardBudgetHeaderBean.getComments()==null?EMPTY_STRING:(awardBudgetHeaderBean.getComments().trim());
            awardBudgetDetailForm.txtArComments.setText(strComment);
            awardBudgetDetailForm.txtArComments.setCaretPosition(0);
            cvTableDetails=(CoeusVector)cvDetailData.get(1);
            
            
            
            cvTableOverHead=new CoeusVector();
            boolean calCEFound=false;
                if(cvTableDetails!=null&&cvTableDetails.size()>0){
                    cvMasterData.addAll(cvTableDetails);
                    //Flag for checking Overhead data exist or not
                    cvMaxLineNo.addAll(cvTableDetails);
                   
                    for(int index=0;index<cvCalCE.size();index++){
                        AwardBudgetDetailBean awardBudgetDetailBean = 
                                        (AwardBudgetDetailBean)cvCalCE.get(index);
                        String costElement = awardBudgetDetailBean.getCostElement();
                        
                        for(int j=0;j<cvTableDetails.size();j++){
                            String costElementDetail =
                            ((AwardBudgetDetailBean)cvTableDetails.get(j)).getCostElement();
                            if(costElement.equals(costElementDetail)){
                                calCEFound=true;
                                AwardBudgetDetailBean awdBudDetailBean
                                = (AwardBudgetDetailBean)cvTableDetails.get(j);
                                
                                awdBudDetailBean.setRateClassType(awardBudgetDetailBean.getRateClassType());
                                awdBudDetailBean.setOnOffCampusFlag(awardBudgetDetailBean.isOnOffCampusFlag());
                                cvTableDetails.remove(j);
                                cvTableOverHead.add(awdBudDetailBean);
                                //cvToBeDelCalcCE.add(awdBudDetailBean);
                                break;
                            }//end if
                        }//end inner for
                    }//end outer for
                    /*****************************************
                    *Checking if any entry added to code table
                    /******************************************/
                    for(int index=0;index<cvCalCE.size();index++){
                        AwardBudgetDetailBean awardDetailBean=(AwardBudgetDetailBean)cvCalCE.get(index);
                        String costElement = awardDetailBean.getCostElement();
                        Equals eqType = new Equals("costElement" , costElement);
                        CoeusVector cvfilter =cvTableOverHead.filter(eqType);
                        if(cvfilter.size()==0){
                            awardDetailBean.setMitAwardNumber(awardBudgetHeaderBean.getMitAwardNumber());
                            awardDetailBean.setLineItemNo(getMaxLineItemNumber()+1);
                            awardDetailBean.setVersionNo(awardBudgetHeaderBean.getVersionNo());
                            awardDetailBean.setAmountSequenceNo(awardBudgetHeaderBean.getAmountSequenceNo());
                            awardDetailBean.setSequenceNumber(awardBudgetHeaderBean.getSequenceNumber());
                            awardDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                            cvTableOverHead.add(awardDetailBean);
                            cvMaxLineNo.add(awardDetailBean);
                        }
                     }
//                    if(!calCEFound){
//                        cvTableOverHead.addAll(cvCalCE);
//                        for(int index=0;index<cvTableOverHead.size();index++){
//                            AwardBudgetDetailBean awardBudgetDetailBean
//                            =(AwardBudgetDetailBean)cvTableOverHead.get(index);
//                            awardBudgetDetailBean.setMitAwardNumber(awardBudgetHeaderBean.getMitAwardNumber());
//                            awardBudgetDetailBean.setLineItemNo(getMaxLineItemNumber()+1);
//                            awardBudgetDetailBean.setVersionNo(awardBudgetHeaderBean.getVersionNo());
//                            awardBudgetDetailBean.setAmountSequenceNo(awardBudgetHeaderBean.getAmountSequenceNo());
//                            awardBudgetDetailBean.setSequenceNumber(awardBudgetHeaderBean.getSequenceNumber());
//                            awardBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
//                        }
//                    }//end if
                    //checking for onoff Campus flag
//                    if(cvTableOverHead!=null&&cvTableOverHead.size()>0){
//                        for(int index=0;index<cvTableOverHead.size();index++){
//                            AwardBudgetDetailBean awardBudgetDetailBean
//                            =(AwardBudgetDetailBean)cvTableOverHead.get(index);
//                            if(awdBudCampusBasedOnCE.intValue()==0){
//                                boolean chkBoxFlag=awardBudgetDetailForm.chkOnOffCampusFlag.isSelected();
//                                if(chkBoxFlag&&awardBudgetDetailBean.isOnOffCampusFlag()){
//                                    Equals eqType = new Equals("onOffCampusFlag" , awardBudgetDetailBean.isOnOffCampusFlag());
//                                    cvTableOverHead =cvTableOverHead.filter(eqType);
//                                    //break;
//                                }else if(!chkBoxFlag&&!awardBudgetDetailBean.isOnOffCampusFlag()){
//                                    Equals eqType = new Equals("onOffCampusFlag" , awardBudgetDetailBean.isOnOffCampusFlag());
//                                    cvTableOverHead =cvTableOverHead.filter(eqType);
//                                    //break;
//                                }
//                            }
//                        }
//                    }//end if
                    
                    if(cvTableOverHead!=null&&cvTableOverHead.size()>0){
                        boolean chkBoxFlag=awardBudgetDetailForm.chkOnOffCampusFlag.isSelected();
                        if(awdBudCampusBasedOnCE.intValue()==0){
                            Equals eqType = new Equals("onOffCampusFlag" , chkBoxFlag);
                            cvTableOverHead =cvTableOverHead.filter(eqType);
                        }
                     }
                    /*
                     *Added by Geo 
                     *Disable the on off campus flag if there is already obligated amount for the 
                     *calculated line items for the last posted version.
                     */
                    double postVerCalcOblAmt = cvTableOverHead.sum("oblAmount");
                    if(Utils.round(postVerCalcOblAmt)!=0){
                        awardBudgetDetailForm.chkOnOffCampusFlag.setEnabled(false);
                    }
                }else{
                    //cvTableOverHead.addAll(cvCalCE);
                    if(cvCalCE!=null&&cvCalCE.size()>0){
                        for(int index=0;index<cvCalCE.size();index++){
                            AwardBudgetDetailBean awardBudgetDetailBean
                            =(AwardBudgetDetailBean)cvCalCE.get(index);
                            awardBudgetDetailBean.setMitAwardNumber(awardBudgetHeaderBean.getMitAwardNumber());
                            awardBudgetDetailBean.setLineItemNo(getMaxLineItemNumber()+1);
                            awardBudgetDetailBean.setVersionNo(awardBudgetHeaderBean.getVersionNo());
                            awardBudgetDetailBean.setAmountSequenceNo(awardBudgetHeaderBean.getAmountSequenceNo());
                            awardBudgetDetailBean.setSequenceNumber(awardBudgetHeaderBean.getSequenceNumber());
                            awardBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                            cvMaxLineNo.add(awardBudgetDetailBean);
                            if(awdBudCampusBasedOnCE.intValue()==0){
                                boolean chkBoxFlag=awardBudgetDetailForm.chkOnOffCampusFlag.isSelected();
                                if(chkBoxFlag&&awardBudgetDetailBean.isOnOffCampusFlag()){
                                    cvTableOverHead.add(awardBudgetDetailBean);
                                }else if(!chkBoxFlag&&!awardBudgetDetailBean.isOnOffCampusFlag()){
                                    cvTableOverHead.add(awardBudgetDetailBean);
                                }
                            }else{
                                cvTableOverHead.add(awardBudgetDetailBean);
                            }
                        }
                    }//end if
                }//End if
           // }//end if
            cvTableOverHead = setLineItemForDisplay(cvTableOverHead);
            awardBudgetOvrheadTableModel.setData(cvTableOverHead);
            cvTableDetails = setLineItemForDisplay(cvTableDetails);
            awardBudgetDetailTableModel.setData(cvTableDetails);
            awardBudgetDetailTableModel.fireTableDataChanged();
            awardBudgetOvrheadTableModel.fireTableDataChanged();
            awardBudgetDetailForm.cmbType.setModel(new DefaultComboBoxModel(cvType));
            String awardBudgetType=""+awardBudgetHeaderBean.getAwardBudgetTypeCode();
            CoeusVector cvOverHeadTotal=getOverHeadTotals(cvTableOverHead);
            totalAmt=((Double)cvOverHeadTotal.get(2)).doubleValue();
            if(getFunctionType()==TypeConstants.REBUDGET_MODE){
                String REBUDGET = "2";
                Equals eqType = new Equals("code" , REBUDGET);
                CoeusVector cvFilterData =cvType.filter(eqType);
                if(cvFilterData.size()>0){
                    ComboBoxBean cmbType = (ComboBoxBean)cvFilterData.get(0);
                    awardBudgetDetailForm.cmbType.setSelectedItem(cmbType);
                    int typeCode=Integer.parseInt(cmbType.getCode());
                    awardBudgetHeaderBean.setAwardBudgetTypeCode(typeCode);
                   // awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
                    awardBudgetDetailForm.cmbType.setEnabled(false);
                }
                
            }else{
                if(cvType!=null&&cvType.size()>0){
                 for(int index=0;index<cvType.size();index++){
                        String typeCode=((ComboBoxBean)cvType.get(index)).getCode();
                        if(awardBudgetType.equals(typeCode)){
                            ComboBoxBean cmbType = (ComboBoxBean)cvType.get(index);
                            awardBudgetDetailForm.cmbType.setSelectedItem(cmbType);
                        }//End If
                    }
                }// End if
            }//End if
            awardBudgetDetailForm.cmbType.setEnabled(false);
            setWindowTitle(awardBudgetType, budgetStatusCode);
        }//End of if
        enableDisableButtons(awardBudgetHeaderBean);
        //case #2200 start 1
        boolean isWindows = checkOSVersion();
        if(!isWindows){
            awardBudgetDetailForm.btnCalculator.setVisible(false);
        }
        //case #2200 end 1
    }
  //case #2200 start 2
  private boolean checkOSVersion() {
      String osVersion = System.getProperty("os.name");
      if(osVersion != null && osVersion.startsWith("Windows")){
          return true;
      }
      return false;
  }
  //case #2200 end 2
   private void refresh(CoeusVector cvData){
        awardBudgetHeaderBean = (AwardBudgetHeaderBean)cvData.get(0);
        cvTableDetails = (CoeusVector)cvData.get(1);
        
        
        
        
        cvTableOverHead.removeAllElements();
        if(getFunctionType() == TypeConstants.NEW_MODE){
            cvTableOverHead = cvCalCE;
        }else{
            if(cvTableDetails!=null&&cvTableDetails.size()>0){
                cvMasterData.removeAllElements();
                cvMasterData.addAll(cvTableDetails);
                
                for(int index=0;index<cvCalCE.size();index++){
                    AwardBudgetDetailBean awardBudgetDetailBean = 
                                        (AwardBudgetDetailBean)cvCalCE.get(index);
                    String costElement = awardBudgetDetailBean.getCostElement();
                    for(int j=0;j<cvTableDetails.size();j++){
                        String costElementDetail =
                        ((AwardBudgetDetailBean)cvTableDetails.get(j)).getCostElement();
                        if(costElement.equals(costElementDetail)){
                            AwardBudgetDetailBean awdBudDetailBean
                                    = (AwardBudgetDetailBean)cvTableDetails.get(j);
                            awdBudDetailBean.setRateClassType(awardBudgetDetailBean.getRateClassType());
                            awdBudDetailBean.setOnOffCampusFlag(awardBudgetDetailBean.isOnOffCampusFlag());
                            cvTableDetails.remove(j);
                            cvTableOverHead.add(awdBudDetailBean);
                            break;
                        }
                    }
                }
            }
        }
//        if(cvTableOverHead!=null&&cvTableOverHead.size()>0){
//            for(int index=0;index<cvTableOverHead.size();index++){
//                AwardBudgetDetailBean awardBudgetDetailBean
//                =(AwardBudgetDetailBean)cvTableOverHead.get(index);
//                if(awdBudCampusBasedOnCE.intValue()==0){
//                    boolean chkBoxFlag=awardBudgetDetailForm.chkOnOffCampusFlag.isSelected();
//                    if(chkBoxFlag&&awardBudgetDetailBean.isOnOffCampusFlag()){
//                        Equals eqType = new Equals("onOffCampusFlag" , awardBudgetDetailBean.isOnOffCampusFlag());
//                        cvTableOverHead =cvTableOverHead.filter(eqType);
//                        //break;
//                    }else if(!chkBoxFlag&&!awardBudgetDetailBean.isOnOffCampusFlag()){
//                        Equals eqType = new Equals("onOffCampusFlag" , awardBudgetDetailBean.isOnOffCampusFlag());
//                        cvTableOverHead =cvTableOverHead.filter(eqType);
//                        //break;
//                    }
//                }
//            }
//        }
        if(cvTableOverHead!=null&&cvTableOverHead.size()>0){
            boolean chkBoxFlag=awardBudgetDetailForm.chkOnOffCampusFlag.isSelected();
            if(awdBudCampusBasedOnCE.intValue()==0){
                Equals eqType = new Equals("onOffCampusFlag" , chkBoxFlag);
                cvTableOverHead =cvTableOverHead.filter(eqType);
            }
        }
        if(awardBudgetHeaderBean.getBudgetStatusCode()==SUBMITTED){
            awardBudgetDetailForm.txtBudgetStatus.setText(
                            awardBudgetHeaderBean.getBudgetStatusDesc());
        }
        setWindowTitle(""+awardBudgetHeaderBean.getAwardBudgetTypeCode(), ""+awardBudgetHeaderBean.getBudgetStatusCode());
        enableDisableButtons(awardBudgetHeaderBean);
        
        cvTableDetails = setLineItemForDisplay(cvTableDetails);
        awardBudgetDetailTableModel.setData(cvTableDetails);
        cvTableOverHead = setLineItemForDisplay(cvTableOverHead);
        awardBudgetOvrheadTableModel.setData(cvTableOverHead);
        awardBudgetDetailTableModel.fireTableDataChanged();
        awardBudgetDetailTotalTableModel.fireTableDataChanged();
        awardBudgetOvrheadTableModel.fireTableDataChanged();
        awardBudgetOverHeadTotalTableModel.fireTableDataChanged();
        if(cvTableDetails!=null&&cvTableDetails.size()>0){
            awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(0,0);
        }
    }
    /*
     *Method to enable and disable buttons
     */
   private void enableDisableButtons(AwardBudgetHeaderBean awardBudgetHeaderBean){
       if(getFunctionType()==TypeConstants.DISPLAY_MODE){
           awardBudgetDetailForm.btnSave.setEnabled(false);
           awardBudgetDetailForm.btnSubmit.setEnabled(false);
           awardBudgetDetailForm.btnReject.setEnabled(false);
           btnApproval.setEnabled(false);
           awardBudgetDetailForm.btnAdd.setEnabled(false);
           awardBudgetDetailForm.btnDelete.setEnabled(false);
           awardBudgetDetailForm.btnCalculate.setEnabled(false);
           if(awardBudgetHeaderBean.getBudgetStatusCode() == TO_BE_POSTED){
               btnPost.setEnabled(true);
           }else{
               btnPost.setEnabled(false);
           }
           return;
       }
       switch(awardBudgetHeaderBean.getBudgetStatusCode()){
           case IN_PROGRESS:
               
                   awardBudgetDetailForm.btnSave.setEnabled(true);
                   awardBudgetDetailForm.btnSubmit.setEnabled(true);
                   awardBudgetDetailForm.btnReject.setEnabled(false);
                   btnApproval.setEnabled(false);
                   btnPost.setEnabled(false);
                   awardBudgetDetailForm.btnAdd.setEnabled(true);
                   awardBudgetDetailForm.btnDelete.setEnabled(true);
                   awardBudgetDetailForm.btnCalculate.setEnabled(true);
              
               break;
           case SUBMITTED:
                   awardBudgetDetailForm.btnSave.setEnabled(true);
                   awardBudgetDetailForm.btnSubmit.setEnabled(false);
                   awardBudgetDetailForm.btnReject.setEnabled(true);
                   btnApproval.setEnabled(true);
                   btnPost.setEnabled(false);
                   awardBudgetDetailForm.btnAdd.setEnabled(true);
                   awardBudgetDetailForm.btnDelete.setEnabled(true);
                   awardBudgetDetailForm.btnCalculate.setEnabled(true);
              
               break;
           case REJECTED:
                   awardBudgetDetailForm.btnSave.setEnabled(false);
                   awardBudgetDetailForm.btnSubmit.setEnabled(false);
                   awardBudgetDetailForm.btnReject.setEnabled(false);
                   btnApproval.setEnabled(false);
                   btnPost.setEnabled(false);
                   awardBudgetDetailForm.btnAdd.setEnabled(false);
                   awardBudgetDetailForm.btnDelete.setEnabled(false);
                   awardBudgetDetailForm.btnCalculate.setEnabled(false);
              
               break;
           case POSTED:
           case TO_BE_POSTED:
               awardBudgetDetailForm.btnSave.setEnabled(false);
               awardBudgetDetailForm.btnSubmit.setEnabled(false);
               awardBudgetDetailForm.btnReject.setEnabled(false);
               btnApproval.setEnabled(false);
               btnPost.setEnabled(false);
               awardBudgetDetailForm.btnAdd.setEnabled(false);
               awardBudgetDetailForm.btnDelete.setEnabled(false);
               awardBudgetDetailForm.btnCalculate.setEnabled(false);
               break;
           case ERROR_IN_POSTING:
               awardBudgetDetailForm.btnSave.setEnabled(true);
               awardBudgetDetailForm.btnSubmit.setEnabled(false);
               awardBudgetDetailForm.btnReject.setEnabled(false);
               btnApproval.setEnabled(false);
               btnPost.setEnabled(false);
               awardBudgetDetailForm.btnAdd.setEnabled(true);
               awardBudgetDetailForm.btnDelete.setEnabled(true);
               awardBudgetDetailForm.btnCalculate.setEnabled(true);
               break;
       }
       if(awardStatus!=1){
           btnApproval.setEnabled(false);
           btnPost.setEnabled(false);
       }
   }
    /*
     *Method for getting Award Budget Details from server
     */
    public Vector getAwardDetailsData() throws CoeusException{
        CoeusVector cvDataToServer = new CoeusVector();
        CoeusVector cvData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + AWARD_BUDGET_SERVLET;
        cvDataToServer.add(awardBudgetSummaryBean);
        
        if(getFunctionType() == TypeConstants.NEW_MODE){
            cvDataToServer.add(new Character(TypeConstants.NEW_MODE));
        }else if(getFunctionType() == TypeConstants.REBUDGET_MODE){
            cvDataToServer.add(new Character(TypeConstants.REBUDGET_MODE));
        }else{
            cvDataToServer.add(new Character(TypeConstants.MODIFY_MODE));
        }
        request.setFunctionType(GET_AWARD_BUDGET_DATA);
        request.setDataObject(cvDataToServer);
        
        AppletServletCommunicator comm =
                new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                cvData = (CoeusVector)response.getDataObject();
            }else{
                throw new CoeusException(response.getMessage(),0);
            }
        }
        return cvData;
    }
    
    public void formatFields() {
        
        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
            awardBudgetDetailForm.cmbType.setEnabled(false);
            awardBudgetDetailForm.txtDescription.setEditable(false);
            awardBudgetDetailForm.txtDescription.setOpaque(false);
//            awardBudgetDetailForm.txtDescription.setEnabled(false);
            awardBudgetDetailForm.txtArComments.setBackground(UIManager.getColor("Panel.Background"));
            awardBudgetDetailForm.txtArComments.setEditable(false);
            awardBudgetDetailForm.txtArComments.setEnabled(false);
            awardBudgetDetailForm.btnSave.setEnabled(false);
            awardBudgetDetailForm.btnSubmit.setEnabled(false);
            awardBudgetDetailForm.btnReject.setEnabled(false);
            btnApproval.setEnabled(false);
            awardBudgetDetailForm.btnAdd.setEnabled(false);
            awardBudgetDetailForm.btnDelete.setEnabled(false);
            awardBudgetDetailForm.btnCalculate.setEnabled(false);
            awardBudgetDetailForm.chkOnOffCampusFlag.setEnabled(false);
            awardBudgetDetailForm.cmbOhRateType.setEnabled(false);
        }
    }
    
    public java.awt.Component getControlledUI() {
        return awardBudgetDetailForm;
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        awardBudgetDetailEditor.stopCellEditing();
        awardBudgetOverHeadEditor.stopCellEditing();
        if(getFunctionType()==TypeConstants.REBUDGET_MODE){
            if(awardBudgetDetailForm.txtArComments.getText()==null||
                awardBudgetDetailForm.txtArComments.getText().trim().equals(EMPTY_STRING)){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(VALID_COMMENT));
                setRequestFocusInThread(awardBudgetDetailForm.txtArComments);
                return false;
            }
        }
        if(cvTableDetails!=null&&cvTableDetails.size()>0){
            for(int index=0;index<cvTableDetails.size();index++){
                AwardBudgetDetailBean awardBudgetDetailBean=
                (AwardBudgetDetailBean)cvTableDetails.get(index);
                if(awardBudgetDetailBean.getCostElement()==null||
                awardBudgetDetailBean.getCostElement().trim().equals(EMPTY_STRING)){
                    int line=index+1;
                    String lineNo=""+line;
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                    ENTER_COST_ELEMENT)+" "+lineNo);
                    awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(index,index);
                    awardBudgetDetailForm.tblAwardDetail.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
                    awardBudgetDetailForm.tblAwardDetail.scrollRectToVisible(
                    awardBudgetDetailForm.tblAwardDetail.getCellRect(
                                                        index ,COST_COLUMN, true));
                    awardBudgetDetailForm.tblAwardDetail.editCellAt(index,COST_COLUMN);
                    setRequestFocusInThread(awardBudgetDetailEditor.txtCostElement);
                    return false;
                }
                double totalAmount=awardBudgetDetailBean.getOblAmount()+
                awardBudgetDetailBean.getOblChangeAmount();
                if(totalAmount<0){
                    CoeusOptionPane.showInfoDialog("Total Amount for "
                    +awardBudgetDetailBean.getCostElement()
                    +" can't be negative.");
                    awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(index,index);
                    awardBudgetDetailForm.tblAwardDetail.setColumnSelectionInterval(CHANGE_COLUMN, CHANGE_COLUMN);
                    awardBudgetDetailForm.tblAwardDetail.scrollRectToVisible(
                    awardBudgetDetailForm.tblAwardDetail.getCellRect(
                                                    index ,CHANGE_COLUMN, true));
                    awardBudgetDetailForm.tblAwardDetail.editCellAt(index,CHANGE_COLUMN);
                    setRequestFocusInThread(awardBudgetDetailEditor.txtChangeAmount);
                    return false;
                }//end if
            }//end for
        }//end if
        awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        try{
            //modified=true;
            performCalculationValidation();
        }
        catch(CoeusException ce){
            ce.printStackTrace();
        }finally{
            awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
//        AwardBudgetHeaderBean awardBudgetHeaderBean=
//                        (AwardBudgetHeaderBean)getFormData();
                
        long obligatedAmount=Math.round(awardBudgetHeaderBean.getOblDisributableAmount());
        CoeusVector cvOverHeadTotal=getOverHeadTotals(cvTableOverHead);
        long sum_total_amount=Math.round(((Double)cvOverHeadTotal.get(2)).doubleValue());
        
        if(obligatedAmount==sum_total_amount){
            if(getFunctionType()==TypeConstants.REBUDGET_MODE){
                
                double sum_change_amt=((Double)cvOverHeadTotal.get(0)).doubleValue();
                if(sum_change_amt!=0){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources
                        .parseMessageKey(MODIFICATION_BUDGET_ZERO));
                        return false;
                }
                return true;
             }
        }else{
            switch(awardBudgetHeaderBean.getBudgetStatusCode()){
                case IN_PROGRESS:                   
                        if(getFunctionType()==TypeConstants.REBUDGET_MODE){
                                modified=true;
                            }
                        if(CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(TOTAL_NOT_EQ_OBLAMT_SAVE),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES)==CoeusOptionPane.SELECTION_YES){
                            //case #1932 start 1
                            modified = true;
                            //case #1932 End 1
                            return true;
                        }else{
                            return false;
                        }
                    
                case REJECTED:
                        //case #1932 start 2
                            modified = true;
                        //case #1932 End 2
                        return true;
                default:
                      CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                          TOTAL_NOT_EQ_OBLAMT));
                      return false;
            }
        }
        return true;
    }
    private void performCalculationValidation() throws CoeusException{
        Equals eqType = new Equals("oblChangeAmount" , new Double(0));
        CoeusVector cvFilter =cvTableOverHead.filter(eqType);
        if(cvFilter.size()==cvTableOverHead.size()){
            //Commented for Case #1932
//           int option=CoeusOptionPane.showQuestionDialog(
//                        coeusMessageResources.parseMessageKey(CALCULATE_PROCEED),
//                            CoeusOptionPane.OPTION_YES_NO,
//                                    CoeusOptionPane.DEFAULT_YES); 
//           if(option==CoeusOptionPane.SELECTION_YES){
//               modified=true;
//               performCalculateAction(false);
//           }//end if
            //Case #1932 Start 5
            CoeusVector cvDataFromServer = performValidCE();
            if(cvDataFromServer == null){
                return;
            }
            int awdBudCampusBasedOnCE = ((Integer)cvDataFromServer.get(0)).intValue();
            if(awdBudCampusBasedOnCE == 1){
                double totalOnEBChangeAmount = ((Double)cvDataFromServer.get(1)).doubleValue();
                double totalOffEBChangeAmount = ((Double)cvDataFromServer.get(2)).doubleValue();
                double totalOnOHChangeAmount = ((Double)cvDataFromServer.get(3)).doubleValue();
                double totalOffOHChangeAmount = ((Double)cvDataFromServer.get(4)).doubleValue();
                if(totalOnEBChangeAmount > 0.0 || totalOffEBChangeAmount > 0.0 
                    || totalOnOHChangeAmount > 0.0 || totalOffOHChangeAmount > 0.0){
                int option=CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(CALCULATE_PROCEED),
                            CoeusOptionPane.OPTION_YES_NO,
                                CoeusOptionPane.DEFAULT_YES);
                        if(option==CoeusOptionPane.SELECTION_YES){
                            modified=true;
                            performCalculateAction(false);
                        }//end if
                }
                
            }else{
                double totalEBChangeAmount = ((Double)cvDataFromServer.get(1)).doubleValue();
                double totalOHChangeAmount = ((Double)cvDataFromServer.get(2)).doubleValue();
                if(totalEBChangeAmount > 0.0 || totalOHChangeAmount > 0.0){
                    int option=CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(CALCULATE_PROCEED),
                            CoeusOptionPane.OPTION_YES_NO,
                                        CoeusOptionPane.DEFAULT_YES);
                    if(option==CoeusOptionPane.SELECTION_YES){
                        modified=true;
                        performCalculateAction(false);
                    }//end if
                }//end if
            }//end if
            //Case #1932 End 5
        }//end if
     }
  public void saveFormData() throws CoeusException {
      awardBudgetDetailEditor.stopCellEditing();
        awardBudgetOverHeadEditor.stopCellEditing();  
      try{
            if(validate()){
//                AwardBudgetHeaderBean awardBudgetHeaderBean=
//                (AwardBudgetHeaderBean)getFormData();
                getFormData();
                //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
                Vector vecCostElements = new Vector();
                //get all cost elements
                if(cvTableDetails!=null && cvTableDetails.size()>0){
                    for(int index=0; index < cvTableDetails.size();index++){
                        AwardBudgetDetailBean awardBudgetBean =(AwardBudgetDetailBean)cvTableDetails.get(index);
                        vecCostElements.add(awardBudgetBean.getCostElement());        
                    }                   
                }
                //remove duplicate cost elements from the vector vecCostElements
                for(int index=0; index<vecCostElements.size(); index++) {
                    //costElementIndex Returns the index of the last occurrence of the specified object from the vector vecCostElements.
                    int costElementIndex = vecCostElements.lastIndexOf(vecCostElements.get(index));
                    //if bothe index and costElementIndex holding the same value then remove the costelement form the vector.
                    if(costElementIndex != index) {
                        vecCostElements.remove(costElementIndex);
                        index=index-1;
                    }
                }
                // Modified for COEUSQA-3401 : unable to reject award budgets and rebudgets - Start
                //if it returns true then it allows to create new version of budget
//                boolean allow_save = getCostElementDetails(vecCostElements);
//                if(allow_save){
//                    if (vecCEMessages != null && vecCEMessages.size() > 0 && awardBudgetHeaderBean.getBudgetStatusCode()!= REJECTED) {
//                        displayCENotAvailableMessages();
//                    }
//                    allow_save = false;
//                }else{
//                    allow_save = true;
//                }
                boolean hasInactiveCostElements = false;
                if(awardBudgetHeaderBean.getBudgetStatusCode() != REJECTED){
                    hasInactiveCostElements = getCostElementDetails(vecCostElements);
                    if(hasInactiveCostElements){
                        if (vecCEMessages != null && vecCEMessages.size() > 0) {
                            displayCENotAvailableMessages();
                        }
                    }
                }
                // Modified for COEUSQA-3401 : unable to reject award budgets and rebudgets - End
                
                //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
                
                //if allow_save returns true then allow to save the details
                 // Modified for COEUSQA-3401 : unable to reject award budgets and rebudgets - Start
//                if(modified && allow_save && awardBudgetHeaderBean.getBudgetStatusCode()!= REJECTED){
                 if(modified && !hasInactiveCostElements){ // Modified for COEUSQA-3401 - End
                    CoeusVector cvData = new CoeusVector();
                    RequesterBean request = new RequesterBean();
                    ResponderBean response = null;
                    
                    
                    CoeusVector cvTemp=new CoeusVector();
                    if(cvDeletedItem!=null&&cvDeletedItem.size()>0){
                        boolean checkNew=(getFunctionType()==TypeConstants.NEW_MODE
                                            ||getFunctionType()==TypeConstants.REBUDGET_MODE)
                                                ?false:true;
                        if(checkNew){
                            cvTemp.addAll(cvDeletedItem);
                        }
                    }
                    if(cvTableDetails!=null && cvTableDetails.size()>0){
                        if(getFunctionType()==TypeConstants.REBUDGET_MODE){
                            for(int index=0;index<cvTableDetails.size();index++){
                                AwardBudgetDetailBean awardBudgetDetailBean
                                =(AwardBudgetDetailBean)cvTableDetails.get(index);
                                awardBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                                awardBudgetDetailBean.setVersionNo(awardBudgetHeaderBean.getVersionNo());
                                
                            }
                            cvTemp.addAll(cvTableDetails);
                        }else if(getFunctionType()==TypeConstants.NEW_MODE){
                            for(int index=0;index<cvTableDetails.size();index++){
                                AwardBudgetDetailBean awardBudgetDetailBean
                                        =(AwardBudgetDetailBean)cvTableDetails.get(index);
                                awardBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                                awardBudgetDetailBean.setVersionNo(awardBudgetHeaderBean.getVersionNo());
                            }
                            cvTemp.addAll(cvTableDetails);
                        }else{
                            //Added for the # COEUSQA-1513-Budget Sequence number out of sync between Budget windows-start
                            for(int index=0;index<cvTableDetails.size();index++){
                                AwardBudgetDetailBean awardBudgetDetailBean
                                        =(AwardBudgetDetailBean)cvTableDetails.get(index);
                                //awardBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                                awardBudgetDetailBean.setVersionNo(awardBudgetHeaderBean.getVersionNo());
                            }
                            //Added for the # COEUSQA-1513-Budget Sequence number out of sync between Budget windows-end
                            cvTemp.addAll(cvTableDetails);
                        }
                    }
                    
                    
                    if(awdBudCampusBasedOnCE.intValue() == 0){
                        cvTemp.addAll(deleteCalcCe());
                    }
                    
                    if(cvTableOverHead!=null&&cvTableOverHead.size()>0){
//                        if(getFunctionType()==TypeConstants.NEW_MODE){
//                            for(int index=0;index<cvTableOverHead.size();index++){
//                                AwardBudgetDetailBean awardBudgetDetailBean
//                                =(AwardBudgetDetailBean)cvTableOverHead.get(index);
//                                awardBudgetDetailBean.setLineItemNo(getMaxLineItemNumber()+(index+1));
//                                awardBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
//                                awardBudgetDetailBean.setVersionNo(awardBudgetHeaderBean.getVersionNo());
//                            }
//                        }
//                        cvTemp.addAll(cvTableOverHead);
//                    }
//                    else{
                        for(int index=0;index<cvTableOverHead.size();index++){
                                AwardBudgetDetailBean awardBudgetDetailBean
                                =(AwardBudgetDetailBean)cvTableOverHead.get(index);
                                //awardBudgetDetailBean.setLineItemNo(getMaxLineItemNumber()+(index+1));
                                awardBudgetDetailBean.setVersionNo(awardBudgetHeaderBean.getVersionNo());
                                if(getFunctionType()==TypeConstants.REBUDGET_MODE||
                                            getFunctionType()==TypeConstants.NEW_MODE){
                                    //awardBudgetDetailBean.setVersionNo(awardBudgetHeaderBean.getVersionNo());
                                    awardBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                                }
//                                else{
//                                    awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
//                                }
                                
                            }
                        cvTemp.addAll(cvTableOverHead);
                    }
                    
                    
                    CoeusVector cvDataToServer = new CoeusVector();
                    cvDataToServer.add(awardBudgetHeaderBean);
                    cvDataToServer.add(cvTemp);
                    //cvDataToServer.add(awardBudgetSummaryBean);
                    cvDataToServer.add(new Character(getFunctionType()));
                    awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    String connectTo = CoeusGuiConstants.CONNECTION_URL + AWARD_BUDGET_SERVLET;
                    
                    request.setDataObject(cvDataToServer);
                    request.setFunctionType(SAVE_AWARD_BUDGET);
                    
                    AppletServletCommunicator comm =
                    new AppletServletCommunicator(connectTo, request);
                    comm.send();
                    response = comm.getResponse();
                    if(response!=null){
                        if(response.isSuccessfulResponse()){
                            awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                            modified=false;
                            /*CoeusOptionPane.showInfoDialog(coeusMessageResources
                               .parseMessageKey(SAVE_SUCCESS));*/
                            successResponse=true;
                            if(getFunctionType()==TypeConstants.REBUDGET_MODE||
                                    getFunctionType()==TypeConstants.NEW_MODE){
                                //strTitle="Modify";
                                setFunctionType(TypeConstants.MODIFY_MODE);
                            }
                            cvData = (CoeusVector)response.getDataObject();
                            if(cvData != null && cvData.size()>0){
                                refresh(cvData);
                            }
                        }else{
                            awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                            throw new CoeusException(response.getMessage(),0);
                        }
                    }else{
                        awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        }catch(CoeusUIException e){
            e.printStackTrace();
            throw new CoeusException(e.getMessage());
        }
    }//End SaveFormData
   /*overloaed method added by Geo to not to set the modifyed flag to true, if its afetch after save
    *
    */
  public Object getFormData( ) {
      return getFormData(false);
  }
  public Object getFormData(boolean afterSave) {
        String strComments = awardBudgetDetailForm.txtArComments.getText().trim();
        String strDesc = awardBudgetDetailForm.txtDescription.getText().trim();
//        strDesc = strDesc.equals("")?null:strDesc;
        ComboBoxBean cmbType = (ComboBoxBean)awardBudgetDetailForm.cmbType.getSelectedItem();
        int typeCode = Integer.parseInt(cmbType.getCode());
        
        if(!awardBudgetHeaderBean.getComments().trim().equals(strComments)){
            awardBudgetHeaderBean.setComments(awardBudgetDetailForm.txtArComments.getText().trim());
//            if(getFunctionType()==TypeConstants.NEW_MODE
//                ||getFunctionType()==TypeConstants.REBUDGET_MODE){
//                    awardBudgetHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
//            }else{            
//                awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
//            }
            
            if(!afterSave) modified=true;
        }
        String bnDescStr = awardBudgetHeaderBean.getDescription()==null?"":awardBudgetHeaderBean.getDescription();
        if(!bnDescStr.equals(strDesc)){
            awardBudgetHeaderBean.setDescription(strDesc);
//            if(getFunctionType()==TypeConstants.NEW_MODE
//                ||getFunctionType()==TypeConstants.REBUDGET_MODE){
//                    awardBudgetHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
//            }else{            
//                awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
//            }
            
            if(!afterSave) modified=true;
        }
        if(awardBudgetHeaderBean.getAwardBudgetTypeCode() != typeCode){

            awardBudgetHeaderBean.setAwardBudgetTypeCode(typeCode);
//            if(getFunctionType()==TypeConstants.NEW_MODE
//                ||getFunctionType()==TypeConstants.REBUDGET_MODE){
//                    awardBudgetHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
//            }else{            
//                awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
//            }
            if(!afterSave) modified=true;
        }
        
        boolean campusFlag = awardBudgetDetailForm.chkOnOffCampusFlag.isSelected();
        if(awardBudgetHeaderBean.isOnOffCampusFlag() != campusFlag){
            awardBudgetHeaderBean.setOnOffCampusFlag(campusFlag);
//            if(getFunctionType()==TypeConstants.NEW_MODE
//                ||getFunctionType()==TypeConstants.REBUDGET_MODE){
//                    awardBudgetHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
//            }else{            
//                awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
//            }
            if(!afterSave) modified=true;
        }
        double oldOblChangeAmt = awardBudgetHeaderBean.getOblChangeAmount();
        CoeusVector cvOverHeadTotal=getOverHeadTotals(cvTableOverHead);
        double sumOverHeadTotal=((Double)cvOverHeadTotal.get(2)).doubleValue();
        double oldTotalCost = awardBudgetHeaderBean.getTotalCost();
//        if(totalAmt!=sumOverHeadTotal){
        if(Utils.round(oldTotalCost)!=Utils.round(sumOverHeadTotal)){
            awardBudgetHeaderBean.setTotalCost(sumOverHeadTotal);
//            if(getFunctionType()==TypeConstants.NEW_MODE
//            ||getFunctionType()==TypeConstants.REBUDGET_MODE){
//                awardBudgetHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
//            }else{
//                awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
//                if(!afterSave) modified=true;
//            }
            if(!afterSave) modified=true;
        }
        ComboBoxBean cmbRateType = (ComboBoxBean)awardBudgetDetailForm.cmbOhRateType.getSelectedItem();
        int rateTypeCode = Integer.parseInt(cmbRateType.getCode());
//        if(awardBudgetHeaderBean.getOhRateTypeCode() != rateTypeCode){
//
        awardBudgetHeaderBean.setOhRateTypeCode(1);
        if(awardBudgetHeaderBean.getOhRateClassCode() != rateTypeCode){

            awardBudgetHeaderBean.setOhRateClassCode(rateTypeCode);
//            if(getFunctionType()==TypeConstants.NEW_MODE
//                ||getFunctionType()==TypeConstants.REBUDGET_MODE){
//                    awardBudgetHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
//            }else{            
//                awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
//            }
            if(!afterSave) modified=true;
        }

        /*
         *Avoid redundancy coding
         */
        if(modified){
            if(getFunctionType()==TypeConstants.NEW_MODE
                ||getFunctionType()==TypeConstants.REBUDGET_MODE){
                    awardBudgetHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
            }else{            
                awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
        return awardBudgetHeaderBean;
    }
  
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source= actionEvent. getSource();
        try{
            if(source.equals(awardBudgetDetailForm.btnAdd)){
                performAddAction();
            }else if(source.equals(btnApproval)){
                performApprovalAction();
            }else if(source.equals(awardBudgetDetailForm.btnCalculate)){
                performCalculateAction(true);
            }else if(source.equals(awardBudgetDetailForm.btnCalculator)){
                performCalculatorAction();
            }else if(source.equals(btnClose)){
                performCloseAction();
            }else if(source.equals(awardBudgetDetailForm.btnDelete)){
                performDeleteAction();
                
            }else if(source.equals(awardBudgetDetailForm.btnReject)){
                performRejectAction();
            }else if(source.equals(awardBudgetDetailForm.btnSave)){
                saveFormData();
            }else if(source.equals(awardBudgetDetailForm.btnSubmit)){
                performSubmitAction();
            }else if(source.equals(awardBudgetDetailForm.btnPrint)){
                performPrintAction();
            }else if(source.equals(btnPost)){
                performPostAction();
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
    }
    private void performPrintAction() throws CoeusException{
        
    }
    private void performPostAction() throws CoeusException{
        awardBudgetDetailEditor.stopCellEditing();
        awardBudgetOverHeadEditor.stopCellEditing();
        if(!isPostAwardBudget){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PRIVILIGES)); 
            return;
        }
        int option=CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(POST_PROCEED),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
        if(option==CoeusOptionPane.SELECTION_YES){

            RequesterBean request = new RequesterBean();
            ResponderBean response = null;
            awardBudgetDetailForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));

            awardBudgetSummaryBean.setBudgetStatusCode(POSTED);
            awardBudgetSummaryBean.setAcType(TypeConstants.UPDATE_RECORD);
            
            request.setFunctionType(SAVE_SUMMARY_DATA);
            request.setDataObject(awardBudgetSummaryBean);
            String connectTo = CoeusGuiConstants.CONNECTION_URL + AWARD_BUDGET_SERVLET;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            response = comm.getResponse();
            if(response!=null){
                if(response.isSuccessfulResponse()){
                   awardBudgetDetailForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                   dlgAwardBudgetMain.dispose();
                }else{
                   awardBudgetDetailForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                   throw new CoeusException(response.getMessage(),0);
                }
            }else{
                awardBudgetDetailForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }
    }
    public void performAddAction()throws CoeusException{
        addLineItem();
        modified=true;
    }
    public void performApprovalAction()throws CoeusException{
        //String s_action=EMPTY_STRING;
        awardBudgetDetailEditor.stopCellEditing();
        awardBudgetOverHeadEditor.stopCellEditing();
        if(!isAwardBudgetApprover){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PRIVILIGES)); 
            return;
        }
        if(CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(APPROVE_PROCEED),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES)==CoeusOptionPane.SELECTION_YES){
//           AwardBudgetHeaderBean awardBudgetHeaderBean
//                    =(AwardBudgetHeaderBean)getFormData();
           int oldStatusCode=awardBudgetHeaderBean.getBudgetStatusCode();
           awardBudgetHeaderBean.setBudgetStatusCode(TO_BE_POSTED);
           awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
           modified=true;
           saveFormData();
           if(successResponse){
                dlgAwardBudgetMain.dispose();
            }else{
               // Set the old status
               awardBudgetHeaderBean.setBudgetStatusCode(oldStatusCode);
               awardBudgetHeaderBean.setAcType(null);
           }
           
        }
    }
   public void performRejectAction()throws CoeusException{
        //String s_action=EMPTY_STRING;
        awardBudgetDetailEditor.stopCellEditing();
        awardBudgetOverHeadEditor.stopCellEditing();
//        if(!isAwardBudgetSubmitter&&!isAwardBudgetAggregator&&!isAwardBudgetApprover){
        if(!isAwardBudgetSubmitter&&!isAwardBudgetApprover){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PRIVILIGES)); 
            return;
        }
//        AwardBudgetHeaderBean awardBudgetHeaderBean
//            =(AwardBudgetHeaderBean)getFormData();
        int oldStatusCode=awardBudgetHeaderBean.getBudgetStatusCode();
        if(awardBudgetDetailForm.txtArComments.getText()!=null && 
            !awardBudgetDetailForm.txtArComments.getText().trim().equals(EMPTY_STRING)){
             if(awardBudgetHeaderBean.getBudgetStatusCode() == SUBMITTED){
                 awardBudgetHeaderBean.setBudgetStatusCode(REJECTED);
                 awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
             }
             //strTitle="Display";
             setFunctionType(TypeConstants.DISPLAY_MODE);
             modified=true;
             saveFormData();
             if(successResponse){
                 dlgAwardBudgetMain.dispose();
             }else{
                 awardBudgetHeaderBean.setBudgetStatusCode(oldStatusCode);
                 awardBudgetHeaderBean.setAcType(null);
             }
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    TYPE_COM_FOR_REJ));
            setRequestFocusInThread(awardBudgetDetailForm.txtArComments);
            return;
            
        }
    }
   //Added for case #2100 start 2
   private boolean checkAccountNo() throws CoeusException{
       if(awardBudgetHeaderBean != null){
           if(awardBudgetHeaderBean.getAccountNumber() == null 
                || awardBudgetHeaderBean.getAccountNumber().equals(EMPTY_STRING)){
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(ENTER_ACCOUNT_NO)+" "+awardBudgetHeaderBean.getMitAwardNumber());
                    return false;
           }
           return true;
       }
       return true;
   }
   //Added for case #2100 end 2
   public void performSubmitAction()throws CoeusException{
       //String s_action=EMPTY_STRING;
       awardBudgetDetailEditor.stopCellEditing();
       awardBudgetOverHeadEditor.stopCellEditing();
       
       //AwardBudgetHeaderBean awardBudgetHeaderBean = (AwardBudgetHeaderBean)getFormData();
       //Modified for case #2100 start 3
       if(checkAccountNo()){
       //Modified for case #2100 end 3
           CoeusVector cvOverHeadTotal=getOverHeadTotals(cvTableOverHead);
           
           double sumOverHeadTotal=Utils.round(((Double)cvOverHeadTotal.get(2)).doubleValue());
           
           int budStatusCode = awardBudgetHeaderBean.getBudgetStatusCode();
           int typeCode = awardBudgetHeaderBean.getAwardBudgetTypeCode();

           if(typeCode!=REBUDGET&&
                    awardBudgetHeaderBean.getOblDisributableAmount()!=sumOverHeadTotal&&
                    awardBudgetHeaderBean.getBudgetStatusCode()==IN_PROGRESS){
               CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
               BUDGET_SUM_TO_OBLAMT));
               return;
           }
           
           double sum_change_amt=((Double)cvOverHeadTotal.get(0)).doubleValue();
           
           if(sum_change_amt!=0&&awardBudgetHeaderBean.getBudgetStatusCode()==IN_PROGRESS){
               if(getFunctionType()==TypeConstants.REBUDGET_MODE){
                   CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                   REBUDGET_CHANGE_TO_SUM_ZERO));
                   return;
               }
           }
           
           
           if(budStatusCode == IN_PROGRESS || budStatusCode == REJECTED){
               if(!isAwardBudgetSubmitter){
                   CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                   NO_PRIVILIGES));
                   return;
               }//end if
           }else if((typeCode == REBUDGET) &&
           (budStatusCode == IN_PROGRESS || budStatusCode == REJECTED)){
               //            if(!isAwardBudgetAggregator){
               if(!isAwardBudgetSubmitter){
                   CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                   NO_PRIVILIGES));
                   return;
               }//end if
           }else{
               //            if(!isAwardBudgetAggregator&&!isAwardBudgetSubmitter){
               if(!isAwardBudgetSubmitter){
                   CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                   NO_PRIVILIGES));
                   return;
               }//End if
           }
           
           int selectedOption = CoeusOptionPane.showQuestionDialog(
           coeusMessageResources.parseMessageKey(SUBMIT_PROCEED),
           CoeusOptionPane.OPTION_YES_NO,
           CoeusOptionPane.DEFAULT_YES);
           
           if(selectedOption == CoeusOptionPane.SELECTION_YES){
               int statusCode = awardBudgetHeaderBean.getBudgetStatusCode();
               if(statusCode == IN_PROGRESS || statusCode == REJECTED){
                   awardBudgetHeaderBean.setBudgetStatusCode(SUBMITTED);
                   awardBudgetHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
               }
               modified=true;
               saveFormData();
               if(!successResponse){
                   //get success response from this then do this
                   awardBudgetHeaderBean.setBudgetStatusCode(statusCode);
                   awardBudgetHeaderBean.setAcType(null);
               }else{
                   dlgAwardBudgetMain.dispose();
               }
               
           }
       }
   }
   //Change the method for case #1932
    public void performCalculateAction(boolean validation)throws CoeusException{
        awardBudgetDetailEditor.stopCellEditing();
        awardBudgetOverHeadEditor.stopCellEditing();
        if(validation){
            Equals eqModifiedByUser = new Equals("modifiedByUser" , true);
            
            CoeusVector cvModfiedByuser = cvTableOverHead.filter(eqModifiedByUser);
            if(cvModfiedByuser != null && cvModfiedByuser .size()>0){
                int option = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(RECALCULATE_PROCEED),
                CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
                if(option == CoeusOptionPane.SELECTION_NO){
                    return ;
                }else{
                    for(int index = 0 ; index < cvTableOverHead.size(); index++){
                        AwardBudgetDetailBean awdBudDetailBean =
                        (AwardBudgetDetailBean)cvTableOverHead.get(index);
                        awdBudDetailBean.setModifiedByUser(false);
                    }//End for
                }
            }
        }
        //Commented for Case #1932
//        CoeusVector cvDataToServer = new CoeusVector();
//        awardBudgetHeaderBean.setOnOffCampusFlag(
//                        awardBudgetDetailForm.chkOnOffCampusFlag.isSelected());
//        
//        ComboBoxBean cmbRateType = (ComboBoxBean)awardBudgetDetailForm.cmbOhRateType.getSelectedItem();
//        awardBudgetHeaderBean.setOhRateTypeCode(1);
//        awardBudgetHeaderBean.setOhRateClassCode(Integer.parseInt(cmbRateType.getCode()));
//        cvDataToServer.add(awardBudgetHeaderBean);
//
//        if(cvTableDetails != null && cvTableDetails.size()>0){
//            /*CoeusVector cvData = new CoeusVector();
//            CoeusVector cvOHData = new CoeusVector();
//            
//            cvData.addAll(cvTableDetails);
//            
//            for(int i = 0; i < cvTableOverHead.size(); i++){
//            AwardBudgetDetailBean awardBudgetDetailBean = 
//                                (AwardBudgetDetailBean)cvTableOverHead.get(i);
//            String rateClassType = awardBudgetDetailBean.getRateClassType();
//                if(rateClassType == null || rateClassType.equals("E")){
//                    //cvOHData.add(awardBudgetDetailBean);
//                    //cvCostElements.remove(i);
//                    cvOHData.add(awardBudgetDetailBean);
//                }
//            }
//            cvData.addAll(cvOHData);
//            
//            cvDataToServer.add(cvData);*/
//            cvDataToServer.add(cvTableDetails);
//        }else if(cvTableDetails == null || cvTableDetails.size()==0){
//            CoeusOptionPane.showInfoDialog(
//                    coeusMessageResources.parseMessageKey(NO_LINE_ITEMS_FOR_CALC));
//            return ;
//        }
//        
////        cvDataToServer.add(cvCalCE);
//        CoeusVector cvDataFromServer = calculateCE(cvDataToServer);
        //case #1932 Start 3
        CoeusVector cvDataFromServer = performValidCE();
        //Case #1932 End 3
        Double totalOnEBChangeAmount = null;
        Double totalOffEBChangeAmount = null;
        Double totalOnOHChangeAmount = null;
        Double totalOffOHChangeAmount = null;
        
        Double totalEBChangeAmount = null;
        Double totalOHChangeAmount = null;
        //case #1932 Start 4
        if(cvDataFromServer == null){
            return;
        }
        //Case #1932 End 4
        int awdBudCampusBasedOnCE = ((Integer)cvDataFromServer.get(0)).intValue();
        if(awdBudCampusBasedOnCE == 1){
            totalOnEBChangeAmount = (Double)cvDataFromServer.get(1);
            totalOffEBChangeAmount = (Double)cvDataFromServer.get(2);
            
            totalOnOHChangeAmount = (Double)cvDataFromServer.get(3);
            totalOffOHChangeAmount = (Double)cvDataFromServer.get(4);
            
            Equals eqOnCampusFlag = new Equals("onOffCampusFlag", true);
            Equals eqOffCampusFlag = new Equals("onOffCampusFlag", false);
            
            //Setting values for EB Start
            Equals eqEB = new Equals("rateClassType" , "E" );
            And eqEBAndeqOnCampusFlag = new And(eqEB , eqOnCampusFlag);
            And eqEBAndeqOffCampusFlag = new And(eqEB , eqOffCampusFlag);
            
            //On campus EB
            CoeusVector cvFilter = cvTableOverHead.filter(eqEBAndeqOnCampusFlag);
            if(cvFilter != null && cvFilter.size()>0){
                AwardBudgetDetailBean awardBudgetDetailBean = 
                                        (AwardBudgetDetailBean)cvFilter.get(0);
                awardBudgetDetailBean.setOblChangeAmount(totalOnEBChangeAmount.doubleValue());
                if(awardBudgetDetailBean.getAcType()  == null || 
                   !awardBudgetDetailBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                       awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                }//End of inner if
            }//End of outer if
            
            //Off campus EB
            cvFilter = cvTableOverHead.filter(eqEBAndeqOffCampusFlag);
            if(cvFilter != null && cvFilter.size()>0){
                AwardBudgetDetailBean awardBudgetDetailBean = 
                                        (AwardBudgetDetailBean)cvFilter.get(0);
                awardBudgetDetailBean.setOblChangeAmount(totalOffEBChangeAmount.doubleValue());
                if(awardBudgetDetailBean.getAcType()  == null || 
                   !awardBudgetDetailBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                       awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                }//End of inner if
            }//End of outer if
            //Setting values for EB End
            
            //Setting values for OH Start
            Equals eqOH = new Equals("rateClassType" , "O" );
            And eqOHAndeqOnCampusFlag = new And(eqOH , eqOnCampusFlag);
            And eqOHAndeqOffCampusFlag = new And(eqOH , eqOffCampusFlag);
            
            //On campus OH
            cvFilter = cvTableOverHead.filter(eqOHAndeqOnCampusFlag);
            if(cvFilter != null && cvFilter.size()>0){
                AwardBudgetDetailBean awardBudgetDetailBean = 
                                        (AwardBudgetDetailBean)cvFilter.get(0);
                awardBudgetDetailBean.setOblChangeAmount(totalOnOHChangeAmount.doubleValue());
                if(awardBudgetDetailBean.getAcType()  == null || 
                   !awardBudgetDetailBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                       awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                }//End of inner if
            }//End of outer if
            
            cvFilter = cvTableOverHead.filter(eqOHAndeqOffCampusFlag);
            if(cvFilter != null && cvFilter.size()>0){
                AwardBudgetDetailBean awardBudgetDetailBean = 
                                        (AwardBudgetDetailBean)cvFilter.get(0);
                awardBudgetDetailBean.setOblChangeAmount(totalOffOHChangeAmount.doubleValue());
                if(awardBudgetDetailBean.getAcType()  == null || 
                   !awardBudgetDetailBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                       awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                }//End of inner if
            }//End of outer if
            //Setting values for OH End
            
        }else{
            totalEBChangeAmount = (Double)cvDataFromServer.get(1);
            totalOHChangeAmount = (Double)cvDataFromServer.get(2);

            Equals eqCampusFlag = new Equals("onOffCampusFlag", awardBudgetHeaderBean.isOnOffCampusFlag());

            Equals eqEB = new Equals("rateClassType" , "E" );
            And eqEBAndeqCampusFlag = new And(eqEB ,eqCampusFlag );

            //Setting values for EB
            CoeusVector cvFilter = cvTableOverHead.filter(eqEBAndeqCampusFlag);
            if(cvFilter != null && cvFilter.size()>0){
                AwardBudgetDetailBean awardBudgetDetailBean = 
                                            (AwardBudgetDetailBean)cvFilter.get(0);
                awardBudgetDetailBean.setOblChangeAmount(totalEBChangeAmount.doubleValue());
                if(awardBudgetDetailBean.getAcType()  == null || 
                   !awardBudgetDetailBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                       awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                }//End of inner if
            }//End of outer if

            //Setting values for OH
            Equals eqOH = new Equals("rateClassType" , "O" );
            And eqOHAndeqCampusFlag = new And(eqOH ,eqCampusFlag );

            cvFilter = cvTableOverHead.filter(eqOHAndeqCampusFlag);
            if(cvFilter != null && cvFilter.size()>0){
                AwardBudgetDetailBean awardBudgetDetailBean = 
                                            (AwardBudgetDetailBean)cvFilter.get(0);
                awardBudgetDetailBean.setOblChangeAmount(totalOHChangeAmount.doubleValue());
                if(awardBudgetDetailBean.getAcType()  == null || 
                   !awardBudgetDetailBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                       awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                }//End inner if
            }//End if
        }//End else
        
        awardBudgetOvrheadTableModel.fireTableDataChanged();
        awardBudgetOverHeadTotalTableModel.fireTableDataChanged();
        if(cvTableOverHead != null && cvTableOverHead.size()>0){
            awardBudgetDetailForm.tblAwardOverhead.setRowSelectionInterval(0, 0);
        }
    }
    //Add for case #1932 Start 6
    private CoeusVector performValidCE() throws CoeusException{
        CoeusVector cvDataToServer = new CoeusVector();
        awardBudgetHeaderBean.setOnOffCampusFlag(
        awardBudgetDetailForm.chkOnOffCampusFlag.isSelected());
        
        ComboBoxBean cmbRateType = (ComboBoxBean)awardBudgetDetailForm.cmbOhRateType.getSelectedItem();
        awardBudgetHeaderBean.setOhRateTypeCode(1);
        awardBudgetHeaderBean.setOhRateClassCode(Integer.parseInt(cmbRateType.getCode()));
        cvDataToServer.add(awardBudgetHeaderBean);
        
        if(cvTableDetails != null && cvTableDetails.size()>0){
            /*CoeusVector cvData = new CoeusVector();
            CoeusVector cvOHData = new CoeusVector();
             
            cvData.addAll(cvTableDetails);
             
            for(int i = 0; i < cvTableOverHead.size(); i++){
            AwardBudgetDetailBean awardBudgetDetailBean =
                                (AwardBudgetDetailBean)cvTableOverHead.get(i);
            String rateClassType = awardBudgetDetailBean.getRateClassType();
                if(rateClassType == null || rateClassType.equals("E")){
                    //cvOHData.add(awardBudgetDetailBean);
                    //cvCostElements.remove(i);
                    cvOHData.add(awardBudgetDetailBean);
                }
            }
            cvData.addAll(cvOHData);
             
            cvDataToServer.add(cvData);*/
            cvDataToServer.add(cvTableDetails);
        }else if(cvTableDetails == null || cvTableDetails.size()==0){
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(NO_LINE_ITEMS_FOR_CALC));
            return null;
        }
        
        //        cvDataToServer.add(cvCalCE);
        CoeusVector cvDataFromServer = calculateCE(cvDataToServer);
        return cvDataFromServer;
    }
    //Case #1932 End 6
    public void performCalculatorAction()throws CoeusException{
        awardBudgetDetailEditor.stopCellEditing();
        awardBudgetOverHeadEditor.stopCellEditing();
        Process calculator=null;
        try{
            if(calculator==null){
                calculator=Runtime.getRuntime().exec("calc");
            }
        }catch(IOException e){
            CoeusOptionPane.showErrorDialog("Unknown error "+e.getMessage()+" Contact Coeus Team");
            e.printStackTrace();
            return;
        }
    }
    
    public void performCloseAction() {
        awardBudgetDetailEditor.stopCellEditing();
        awardBudgetOverHeadEditor.stopCellEditing();
        //Modified for Save Confirmation in comments
        //Commented by Geo.
        //Need to check again
        //This is firing save confirmation even after saving
//        AwardBudgetHeaderBean awardBudgetHeaderBean=
//                                        (AwardBudgetHeaderBean)getFormData();
        getFormData();
        //Modified for Save Confirmation in comments
        if(modified){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            2);
            switch( option ) {
                case (CoeusOptionPane.SELECTION_YES):
                    awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    try{
                        saveFormData();
                        if(successResponse){
                            awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                            dlgAwardBudgetMain.dispose();
                        }
                     }catch(CoeusException e){
                        CoeusOptionPane.showErrorDialog(e.getMessage());
                        return;
                    }finally{
                        awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }
                    break;
                case(CoeusOptionPane.SELECTION_NO):
                    dlgAwardBudgetMain.dispose();
                    awardBudgetDetailForm=null;
                    break;
                default:
                    break;
                    
            }
        }else{
            dlgAwardBudgetMain.dispose();
            awardBudgetDetailForm=null;
        }
    }
    /*
     **Method to check duplicate cost element
     **/
    private boolean checkForDuplicateCE(String costElement){
        for(int index=0;index<cvTableDetails.size();index++){
            AwardBudgetDetailBean awardBudgetDetailBean=
            (AwardBudgetDetailBean)cvTableDetails.get(index);
            if(awardBudgetDetailBean.getCostElement().trim().equals(costElement.trim())){
                duplicateLineNo=index+1;
                return true;
            }
        }
        return false;
    }
    /*
     *Delete the selected Row
     **/
    public void performDeleteAction()throws CoeusException{
        int rowCount=awardBudgetDetailForm.tblAwardDetail.getRowCount();
        if(rowCount<=0){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_ROW_TO_DELETE));
            return;
        }
        int selRow = awardBudgetDetailForm.tblAwardDetail.getSelectedRow();
        if(selRow==-1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ROW_FOR_PROCEED));
            return ;
        }
        double oblAmount=((AwardBudgetDetailBean)cvTableDetails.get(selRow)).getOblAmount();
        if(oblAmount>0.00){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(HAS_AN_OBLIGATED_AMT));
            awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(selRow, selRow);
            awardBudgetDetailForm.tblAwardDetail.scrollRectToVisible(
            awardBudgetDetailForm.tblAwardDetail.getCellRect(
            selRow ,0, true));
            awardBudgetDetailForm.tblAwardDetail.getEditorComponent().requestFocusInWindow();
            return;
        }
        try{
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(DELETE_LINE_ITEM), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
            if(selection == CoeusOptionPane.SELECTION_NO) return ;
            AwardBudgetDetailBean awardBudgetDetailBean=(AwardBudgetDetailBean)cvTableDetails.get(selRow);
            awardBudgetDetailEditor.stopCellEditing();
            awardBudgetOverHeadEditor.stopCellEditing();
            
            modified=true;
            if(awardBudgetDetailBean.getAcType()==TypeConstants.INSERT_RECORD){
                cvTableDetails.remove(selRow);
                //cvMaxLineNo.remove(selRow);
            }else{
                cvTableDetails.remove(selRow);
                awardBudgetDetailBean.setAcType(TypeConstants.DELETE_RECORD);
                cvDeletedItem.add(awardBudgetDetailBean);
            }
            awardBudgetDetailTableModel.fireTableRowsDeleted(selRow, selRow);
            awardBudgetDetailTotalTableModel.fireTableDataChanged();
            awardBudgetOverHeadTotalTableModel.fireTableDataChanged();
            //Change other beans(which are below this Deleted bean) Line Item Sequence
            for(int index = selRow; index < cvTableDetails.size(); index++) {
                awardBudgetDetailBean = (AwardBudgetDetailBean)cvTableDetails.get(index);
                awardBudgetDetailBean.setLineItemNoDisplay(awardBudgetDetailBean.getLineItemNoDisplay()- 1);
                if(awardBudgetDetailBean.getAcType()!=null
                    && !awardBudgetDetailBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                    awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                }else if(awardBudgetDetailBean.getAcType()==null){
                    awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                
            }
            awardBudgetDetailTableModel.fireTableRowsUpdated(selRow, cvTableDetails.size());
            if(cvTableDetails.size() == 0) {
                setRequestFocusInThread(awardBudgetDetailForm.btnAdd);
            }else {
                awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(cvTableDetails.size()-1,
                                                                cvTableDetails.size()-1);
                awardBudgetDetailForm.tblAwardDetail.setColumnSelectionInterval(3,3);
                awardBudgetDetailForm.tblAwardDetail.editCellAt(cvTableDetails.size()-1,CHANGE_COLUMN);
                awardBudgetDetailForm.tblAwardDetail.scrollRectToVisible(
                awardBudgetDetailForm.tblAwardDetail.getCellRect(cvTableDetails.size()-1 ,CHANGE_COLUMN, true));
                setRequestFocusInThread(awardBudgetDetailEditor.txtChangeAmount);
            }
            if(awardBudgetDetailForm.tblAwardDetail.getRowCount() <1){
                setRequestFocusInThread(awardBudgetDetailForm.btnAdd);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    /*
     *Method to get Total of Detail table
     *@return CoeusVector
     */
    private CoeusVector getTotalDetails(CoeusVector cvTableDetails){
        CoeusVector cvTotalDetails=new CoeusVector();
        double totalDetailChangeAmount=0.0d , totalObligateAmount=0.0d, sumTotalDetailAmount = 0.0d ;
        if(cvTableDetails!=null && cvTableDetails.size()>0){
            for(int index=0;index<cvTableDetails.size();index++){
                AwardBudgetDetailBean
                awardBudgetDetailBean=(AwardBudgetDetailBean)cvTableDetails.get(index);
                totalDetailChangeAmount=totalDetailChangeAmount+awardBudgetDetailBean.getOblChangeAmount();;
                totalObligateAmount=totalObligateAmount+awardBudgetDetailBean.getOblAmount();
            }
            
            sumTotalDetailAmount = totalDetailChangeAmount+totalObligateAmount;
        }
        
        cvTotalDetails.add(new Double(totalDetailChangeAmount));
        cvTotalDetails.add(new Double(totalObligateAmount));
        cvTotalDetails.add(new Double(sumTotalDetailAmount));
        return cvTotalDetails;
    }
    /*
     *Method to get Total of Overhead table
     *@return CoeusVector
     */
    private CoeusVector getOverHeadTotals(CoeusVector cvTableOverHead){
        CoeusVector cvTotalOverHead=new CoeusVector();
        double totalOverHeadChangeAmount=0,totalOverHeadObligateAmount=0,sumChangeAmt=0
            ,sumObligatedAmt=0,sumTotalOverHeadAmount=0;
        CoeusVector cvTotalDetails=getTotalDetails(cvTableDetails);
        if(cvTableOverHead!=null&&cvTableOverHead.size()>0){
            for(int index=0;index<cvTableOverHead.size();index++){
                AwardBudgetDetailBean
                awardBudgetDetailBean=(AwardBudgetDetailBean)cvTableOverHead.get(index);
                totalOverHeadChangeAmount=totalOverHeadChangeAmount+awardBudgetDetailBean.getOblChangeAmount();;
                totalOverHeadObligateAmount=totalOverHeadObligateAmount+awardBudgetDetailBean.getOblAmount();
            }
          }
        sumChangeAmt=Utils.round(((Double)cvTotalDetails.get(0)).doubleValue())
                        +totalOverHeadChangeAmount;
        sumObligatedAmt=Utils.round(((Double)cvTotalDetails.get(1)).doubleValue())
                        +totalOverHeadObligateAmount;
        sumTotalOverHeadAmount = sumChangeAmt+sumObligatedAmt;
        cvTotalOverHead.add(new Double(sumChangeAmt));
        cvTotalOverHead.add(new Double(sumObligatedAmt));
        cvTotalOverHead.add(new Double(sumTotalOverHeadAmount));
        return cvTotalOverHead;
        
    }
    /** returns max line item number.
     * @return max line item number.
     */
    private int getMaxLineItemNumber() {
        //if(cvTableDetails == null || cvTableDetails.size() == 0) return 0;
        if(cvMaxLineNo==null||cvMaxLineNo.size()==0) {
            return 0;
        }
        CoeusVector vecLineItem = new CoeusVector();
        vecLineItem.addAll(cvMaxLineNo);
        vecLineItem.sort("lineItemNo", false);
        AwardBudgetDetailBean maxLineItemBean = (AwardBudgetDetailBean)vecLineItem.get(0);
        return maxLineItemBean.getLineItemNo();
    }
    /** adds a line item to the end of the period line item table. */
    public void addLineItem() {
        int row = 0;
        
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            return ;
        }
        
        awardBudgetDetailEditor.stopCellEditing();
        awardBudgetOverHeadEditor.stopCellEditing();
        
        if(cvTableDetails == null){
            row = row + 1;
        }else{
            row = cvTableDetails.size()+1;
        }
        
        AwardBudgetDetailBean awardBudgetDetailBean = new AwardBudgetDetailBean();
        awardBudgetDetailBean.setMitAwardNumber(awardBudgetSummaryBean.getMitAwardNumber());
        awardBudgetDetailBean.setSequenceNumber(awardBudgetSummaryBean.getSequenceNumber());
        awardBudgetDetailBean.setAmountSequenceNo(awardBudgetSummaryBean.getAmountSequenceNumber());
        awardBudgetDetailBean.setVersionNo(awardBudgetSummaryBean.getBudgetVersion());
        awardBudgetDetailBean.setLineItemNo(getMaxLineItemNumber()+1);
        awardBudgetDetailBean.setCostElement(EMPTY_STRING);
        awardBudgetDetailBean.setCostElementDescription(EMPTY_STRING);
        awardBudgetDetailBean.setOblChangeAmount(0);
        awardBudgetDetailBean.setOblAmount(0);
        awardBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
        
        if(cvTableDetails == null){
            cvTableDetails = new CoeusVector();
        }
        cvTableDetails.add(awardBudgetDetailBean);
        cvMaxLineNo.add(awardBudgetDetailBean);
        
        cvTableDetails = setLineItemForDisplay(cvTableDetails);
        awardBudgetDetailTableModel.fireTableRowsInserted(row - 1, row - 1);
        
        awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(row - 1 , row - 1);
        awardBudgetDetailForm.tblAwardDetail.setColumnSelectionInterval(COST_COLUMN,COST_COLUMN);
        awardBudgetDetailForm.tblAwardDetail.scrollRectToVisible(
        awardBudgetDetailForm.tblAwardDetail.getCellRect(
        row-1 ,0, true));
        awardBudgetDetailForm.tblAwardDetail.editCellAt(row - 1, COST_COLUMN);
        awardBudgetDetailForm.tblAwardDetail.getEditorComponent().requestFocusInWindow();
        
    }
    /*
     *Display the Cost Elements Dialog
     */
    public void costElementsDialog() throws CoeusClientException{
     
        CostElementsBean costElementsBean;
        ComboBoxBean comboBoxBean;
        CoeusVector cvComboBoxBean = new CoeusVector();
        
        for(int index = 0; index < cvCostElements.size(); index++) {
            costElementsBean = (CostElementsBean)cvCostElements.get(index);
            comboBoxBean = new ComboBoxBean(costElementsBean.getCostElement(), costElementsBean.getDescription());
            cvComboBoxBean.add(comboBoxBean);
        }
        setFocus=false;
        String colNames[] = {"Code","Description"};
        OtherLookupBean otherLookupBean = new OtherLookupBean(COST_ELEMENT_LOOKUP_TITLE, cvComboBoxBean, colNames);
        costElementsLookupWindow = new CostElementsLookupWindow(otherLookupBean);
        
        //Check button click - OK or Cancel
        if(otherLookupBean.getSelectedInd() == -1) return ;
        
        //Get Selected Row for Cost Elements
        int selectedRow = costElementsLookupWindow.getDisplayTable().getSelectedRow();
        if(selectedRow == -1) return ;
        
        costElementsBean = (CostElementsBean)cvCostElements.get(selectedRow);
        
        //Check for the duplicates
        if( checkForDuplicateCE(costElementsBean.getCostElement()) ){
            CoeusOptionPane.showInfoDialog("Cost Element "+costElementsBean.getCostElement() +" is being used on line "+duplicateLineNo);
            return ;
        }
        setFocus=true;
        selectedRow = awardBudgetDetailForm.tblAwardDetail.getSelectedRow();
        AwardBudgetDetailBean awardBudgetDetailBean = (AwardBudgetDetailBean)cvTableDetails.get(selectedRow);
        
        try{
            
            awardBudgetDetailBean.setCostElement(costElementsBean.getCostElement());
            awardBudgetDetailBean.setCostElementDescription(costElementsBean.getDescription());
            awardBudgetDetailTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
            awardBudgetDetailEditor.cancelCellEditing();
           
      
        }catch (Exception coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /*
     ** Method to set focus in any component
     **/
    private void setRequestFocusInThread(final java.awt.Component component){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                component.requestFocusInWindow();
                
            }
        });
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        try{
            if(mouseEvent.getClickCount() != 2||getFunctionType()==TypeConstants.DISPLAY_MODE) return ;
            awardBudgetDetailEditor.stopCellEditing();
            if(awardBudgetDetailForm.tblAwardDetail.getRowCount()<=0) return;
            int row;
            if(mouseEvent.getSource().equals(awardBudgetDetailForm.tblAwardDetail)) {
                row = awardBudgetDetailForm.tblAwardDetail.rowAtPoint(mouseEvent.getPoint());
            }else {
                row = awardBudgetDetailForm.tblAwardDetail.getSelectedRow();
            }
            AwardBudgetDetailBean awardBudgetDetailBean=(AwardBudgetDetailBean)cvTableDetails.get(row);
            if(awardBudgetDetailForm.tblAwardDetail.columnAtPoint(mouseEvent.getPoint()) == COST_DESCR_COLUMN&&awardBudgetDetailBean.getCostElement().equals(EMPTY_STRING)) {
                costElementsDialog();
                if(setFocus){
                    awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(row,row);
                    awardBudgetDetailForm.tblAwardDetail.setColumnSelectionInterval(CHANGE_COLUMN,CHANGE_COLUMN);
                    awardBudgetDetailForm.tblAwardDetail.editCellAt(row,CHANGE_COLUMN);
                    setRequestFocusInThread(awardBudgetDetailEditor.txtChangeAmount);
                }else{
                    awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(row,row);
                    awardBudgetDetailForm.tblAwardDetail.setColumnSelectionInterval(COST_COLUMN,COST_COLUMN);
                    awardBudgetDetailForm.tblAwardDetail.editCellAt(row,COST_COLUMN);
                    setRequestFocusInThread(awardBudgetDetailEditor.txtCostElement);
                }
            }
        }catch (Exception coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
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
    
    /**This is an inner class which behaves like a model for the JTable
     */
    
    public class AwardBudgetDetailTableModel extends AbstractTableModel{
        private String lineNumber="<html>Line<br>Number</html>";
        private String costElement="<html>Cost<br>Element</html>";
        private String costElemementDescr="<html>Cost Element Description<br></html>";
        private String changeAmount="<html>Change Amount<br></html>";
        private String obligatedAmount="<html>Obligated <br>Amount</html>";
        private String totalAmount="<html>Total Amount<br></html>";
        private String []colNames={lineNumber,costElement,costElemementDescr,changeAmount,obligatedAmount,totalAmount};
        private Class colClass[]={String.class,String.class,String.class,Double.class,Double.class,Double.class};
        
        public boolean isCellEditable(int row, int col){
            if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                return false;
            }else if(col==0||col==2||col==4||col==5){
                return false;
            }else if(col == 1){
                boolean editable = false;
                AwardBudgetDetailBean budgetDetailBean =
                (AwardBudgetDetailBean)cvTableDetails.get(row);
                String costElement = budgetDetailBean.getCostElement();
                if(costElement.trim().equals(EMPTY_STRING)){
                    editable = true;
                }else{
                    editable = false;
                }
                return editable;
            }else{
                return true;
            }
            
        }
        public String getColumnName(int col){
            return colNames[col];
        }
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            if(cvTableDetails==null||cvTableDetails.size()==0)
                return 0;
            else
                return cvTableDetails.size();
        }
        public void setData(CoeusVector cvTableDetails){
            cvTableDetails=cvTableDetails;
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            AwardBudgetDetailBean
            awardBudgetDetailBean=(AwardBudgetDetailBean)cvTableDetails.get(rowIndex);
            double totalDetailAmount=awardBudgetDetailBean.getOblChangeAmount()+awardBudgetDetailBean.getOblAmount();
            //Bug Fix by Geo to update the latest amount sequence number and sequence number from
            //amount info table
            //BEGIN 01-Mar-2006
            awardBudgetDetailBean.setAmountSequenceNo(awardBudgetHeaderBean.getAmountSequenceNo());
            awardBudgetDetailBean.setSequenceNumber(awardBudgetHeaderBean.getSequenceNumber());
            //END
            switch(columnIndex){
                case LINE_COLUMN:
                    return new Integer(awardBudgetDetailBean.getLineItemNoDisplay());
                case COST_COLUMN:
                    return new String(awardBudgetDetailBean.getCostElement());
                case COST_DESCR_COLUMN:
                    return new String(awardBudgetDetailBean.getCostElementDescription().trim());
                case CHANGE_COLUMN:
                    return new Double(awardBudgetDetailBean.getOblChangeAmount());
                case OBLIGATED_COLUMN:
                    return new Double(awardBudgetDetailBean.getOblAmount());
                case AMOUNT_COLUMN:
                    return new Double(totalDetailAmount);
            }
            return EMPTY_STRING;
        }
        
        public void setValueAt(Object value,int row,int col){
            if(cvTableDetails.size()==0||cvTableDetails==null) return;
            AwardBudgetDetailBean
                awardBudgetDetailBean=(AwardBudgetDetailBean)cvTableDetails.get(row);
            switch(col){
                case COST_COLUMN:
                    
                    if(value == null || value.toString().equals(EMPTY_STRING)) {
                        awardBudgetDetailBean.setCostElement(EMPTY_STRING);
                        awardBudgetDetailBean.setCostElementDescription(EMPTY_STRING);
                        
                    }
                    else{
                        //Check if old and modified Cost Elements are same. If Same no need to modify
                        if(awardBudgetDetailBean.getCostElement().equals(value.toString())) return ;
                        RequesterBean requesterBean = new RequesterBean();
                        requesterBean.setDataObject(value);
                        requesterBean.setFunctionType(GET_AWARD_BUDGET_CE_DETAILS);
                        awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                        String connectTo = CoeusGuiConstants.CONNECTION_URL + AWARD_BUDGET_SERVLET;
                        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connectTo ,  requesterBean);
                        appletServletCommunicator.setRequest(requesterBean);
                        appletServletCommunicator.send();
                        CostElementsBean costElementsBean = null;
                        
                        ResponderBean responderBean = appletServletCommunicator.getResponse();
                        // JM 6-15-2011 updated to throw useful error rather than hanging on bad cost element; updated from deprecated return call
                        try {
                        // END
                        if(responderBean.isSuccessfulResponse()) {
                            awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                            costElementsBean = (CostElementsBean)responderBean.getDataObject();
                            
                            if(costElementsBean == null || costElementsBean.getDescription() == null){
                                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_COST_ELEMENT));
                                setFocus=false;
                                return ;
                            }else {
                                if( checkForDuplicateCE(value.toString()) ){
                                    setFocus=false;
                                    CoeusOptionPane.showInfoDialog("Cost Element "+value.toString() +" is being used on line "+duplicateLineNo);
                                    return ;
                                }
                                setFocus=true;
                                awardBudgetDetailBean.setCostElement(value.toString());
                                awardBudgetDetailBean.setCostElementDescription(costElementsBean.getDescription());
                                
                            }
                             if(setFocus){
                                 awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(row,row);
                                 awardBudgetDetailForm.tblAwardDetail.setColumnSelectionInterval(CHANGE_COLUMN,CHANGE_COLUMN);
                                 awardBudgetDetailForm.tblAwardDetail.editCellAt(row,CHANGE_COLUMN);
                                 setRequestFocusInThread(awardBudgetDetailEditor.txtChangeAmount);
                             }
                        }else{
                            awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                        }
                        // JM 6-15-2011 new exception; when stored procedure returns no data, produce popup error and return focus to element                    	
                        }catch (Exception coeusException){
                            CoeusOptionPane.showErrorDialog("Cost element " + value.toString() + " is not valid.     ");
					        awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
					        awardBudgetDetailBean.setCostElement(EMPTY_STRING);
					        //awardBudgetDetailBean.setCostElementDescription(EMPTY_STRING);
					        awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
					        awardBudgetDetailEditor.txtCostElement.setRequestFocusEnabled(true);
					        awardBudgetDetailEditor.txtCostElement.requestFocus();
                        
					        try {
								Robot robot = new Robot();
								robot.keyPress(COST_COLUMN);
								robot.keyRelease(COST_COLUMN);
							} catch (AWTException e) {
								e.printStackTrace();
							}
					        return;
						}
                        // JM END                        
                    }
                    break;
            case CHANGE_COLUMN:
                    double changeAmount = 0.00;
                    changeAmount = new Double(value.toString()).doubleValue();
                    
                    // double changeAmount=Double.parseDouble(value.toString());
                    double changeAmountBean = awardBudgetDetailBean.getOblChangeAmount();
                    if (changeAmount!=changeAmountBean) {
                        awardBudgetDetailBean.setOblChangeAmount(changeAmount);
                        awardBudgetDetailBean.setModifiedByUser(true);
                        modified=true;
                        if(awardBudgetDetailBean.getAcType()
                          !=null&&awardBudgetDetailBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                               
                        }else{
                            awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        
                    }
                    if(modified){
                        awardBudgetDetailTableModel.fireTableDataChanged();
                        awardBudgetDetailTotalTableModel.fireTableDataChanged();
                        awardBudgetOverHeadTotalTableModel.fireTableDataChanged();
                    }
                    break;
            }
        }
    }
    
    /** It is an inner class which provide editor for the Table
     */
    class AwardBudgetDetailEditor extends DefaultCellEditor implements FocusListener {
        
        private int row;
        private JLabel lblLineNo;
        private CoeusTextField txtCostElement;
        private CoeusTextField txtDescription;
        private DollarCurrencyTextField txtChangeAmount;
        private DollarCurrencyTextField txtObligatedAmount;
        private DollarCurrencyTextField txtTotal;
        private int column;
        
        AwardBudgetDetailEditor() {
            
            super(new JComboBox());
            lblLineNo=new JLabel("");
            lblLineNo.setOpaque(false);
            txtCostElement = new CoeusTextField();
            txtCostElement.setDocument(new LimitedPlainDocument(8));
            txtCostElement.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent mouseEvent){
                        if(mouseEvent.getClickCount() != 2||getFunctionType()==TypeConstants.DISPLAY_MODE) return ;
                        stopCellEditing();
                        try {
                            int row;
                            if(mouseEvent.getSource().equals(awardBudgetDetailForm.tblAwardDetail)) {
                                row = awardBudgetDetailForm.tblAwardDetail.rowAtPoint(mouseEvent.getPoint());
                            }else {
                                row = awardBudgetDetailForm.tblAwardDetail.getSelectedRow();
                            }
                            if(awardBudgetDetailForm.tblAwardDetail.getRowCount()<=0
                            || !awardBudgetDetailTableModel.isCellEditable(row, 1)){
                                return;
                            }
                            
                            AwardBudgetDetailBean awardBudgetDetailBean=(AwardBudgetDetailBean)cvTableDetails.get(row);
                            if(awardBudgetDetailBean.getCostElement().equals("")){
                                costElementsDialog();
                                if(setFocus){
                                    awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(row,row);
                                    awardBudgetDetailForm.tblAwardDetail.setColumnSelectionInterval(CHANGE_COLUMN,CHANGE_COLUMN);
                                    awardBudgetDetailForm.tblAwardDetail.editCellAt(row,CHANGE_COLUMN);
                                    setRequestFocusInThread(txtChangeAmount);
                                }else{
                                    awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(row,row);
                                    awardBudgetDetailForm.tblAwardDetail.setColumnSelectionInterval(COST_COLUMN,COST_COLUMN);
                                    awardBudgetDetailForm.tblAwardDetail.editCellAt(row,COST_COLUMN);
                                    setRequestFocusInThread(txtCostElement);
                                }
                            }
                        }catch (Exception coeusException){
                            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                        }
                }
            }
            );
            txtDescription = new CoeusTextField();
            txtChangeAmount = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            txtObligatedAmount = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            
            txtTotal = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            
        }
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            this.row = row;
            
            switch (column) {
                case LINE_COLUMN:
                    lblLineNo.setText(value.toString());
                    return lblLineNo;
                case COST_COLUMN:
                    txtCostElement.setText(value.toString());
                    return txtCostElement;
                case COST_DESCR_COLUMN:
                    if(value == null) value = EMPTY_STRING;
                    txtDescription.setText(value.toString());
                    return txtDescription;
                case CHANGE_COLUMN:
                    if(value== null){
                        txtChangeAmount.setValue(0.00);
                    }else{
                        txtChangeAmount.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtChangeAmount;
                    
                case OBLIGATED_COLUMN:
                    txtObligatedAmount.setText(value.toString());
                    return txtObligatedAmount;
                case AMOUNT_COLUMN:
                    txtTotal.setText(value.toString());
                    return txtTotal;
            }
            return lblLineNo;
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case LINE_COLUMN:
                    return lblLineNo.getText();
                case COST_COLUMN:
                    return txtCostElement.getText();
                case COST_DESCR_COLUMN:
                    return txtDescription.getText();
                case CHANGE_COLUMN:
                    return txtChangeAmount.getValue();
                case OBLIGATED_COLUMN:
                    return txtObligatedAmount.getValue();
                case AMOUNT_COLUMN:
                    return txtTotal.getValue();
            }
            return lblLineNo;
        }
       public void focusGained(java.awt.event.FocusEvent e) {
            if(awardBudgetDetailForm.tblAwardDetail.getRowCount()>0){
                awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(0,0);
                String costElement = (String)awardBudgetDetailForm.tblAwardDetail.getValueAt(0, 1);
                if(costElement.trim().equals(EMPTY_STRING)){
                    awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(0,0);
                    awardBudgetDetailForm.tblAwardDetail.setColumnSelectionInterval(1,1);
                    awardBudgetDetailForm.tblAwardDetail.editCellAt(0, 1);
                    awardBudgetDetailForm.tblAwardDetail.getEditorComponent().requestFocusInWindow();
                }else{
                    awardBudgetDetailForm.tblAwardDetail.setRowSelectionInterval(0,0);
                    awardBudgetDetailForm.tblAwardDetail.setColumnSelectionInterval(3,3);
                    awardBudgetDetailForm.tblAwardDetail.editCellAt(0, 3);
                    awardBudgetDetailForm.tblAwardDetail.getEditorComponent().requestFocusInWindow();
                }
            }
        }
        public void focusLost(java.awt.event.FocusEvent e) {
        }
     }
    /*** Table editor for AwardBudgetOverheadTable
     */
     class AwardBudgetOverHeadEditor extends DefaultCellEditor {
        
        private int row;
        private JLabel lblLineNo;
        private CoeusTextField txtCostElement;
        private CoeusTextField txtDescription;
        private DollarCurrencyTextField txtChangeAmount;
        private DollarCurrencyTextField txtObligatedAmount;
        private DollarCurrencyTextField txtTotal;
        private int column;
        
        AwardBudgetOverHeadEditor() {
           super(new JComboBox());
            lblLineNo=new JLabel("");
            lblLineNo.setOpaque(false);
            txtCostElement = new CoeusTextField();
            txtCostElement.setDocument(new LimitedPlainDocument(8));
            txtDescription = new CoeusTextField();
            txtChangeAmount = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            txtObligatedAmount = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            
            txtTotal = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            
        }
       public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            this.row = row;
            
            switch (column) {
                case LINE_COLUMN:
                    lblLineNo.setText(value.toString());
                case COST_COLUMN:
                    txtCostElement.setText(value.toString());
                    return txtCostElement;
                case COST_DESCR_COLUMN:
                    if(value == null) value = EMPTY_STRING;
                    txtDescription.setText(value.toString());
                    return txtDescription;
                case CHANGE_COLUMN:
                    if(value== null){
                        txtChangeAmount.setValue(0.00);
                    }else{
                        txtChangeAmount.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtChangeAmount;
                    
                case OBLIGATED_COLUMN:
                    txtObligatedAmount.setText(value.toString());
                    return txtObligatedAmount;
                case AMOUNT_COLUMN:
                    txtTotal.setText(value.toString());
                    return txtTotal;
            }
            return txtChangeAmount;
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case LINE_COLUMN:
                    return lblLineNo.getText();
                case COST_COLUMN:
                    return txtCostElement.getText();
                case COST_DESCR_COLUMN:
                    return txtDescription.getText();
                case CHANGE_COLUMN:
                    return txtChangeAmount.getValue();
                case OBLIGATED_COLUMN:
                    return txtObligatedAmount.getValue();
                case AMOUNT_COLUMN:
                    return txtTotal.getValue();
            }
            return lblLineNo;
        }
        
    }
    /******************************************/
    public class AwardBudgetDetailTableCellRenderer extends DefaultTableCellRenderer{
        private JLabel lblComponent;
        private JLabel lblCellComponent;
        private DollarCurrencyTextField txtDollar;
        private DollarCurrencyTextField txtChangeAmount;
        private CoeusTextField txtCostElement;
        private JLabel lblDollar;
        public AwardBudgetDetailTableCellRenderer(){
            lblComponent=new JLabel();
            lblDollar=new JLabel();
            lblDollar.setOpaque(true);
            lblDollar.setHorizontalAlignment(RIGHT);
            lblCellComponent=new JLabel();
            txtCostElement=new CoeusTextField();
            lblComponent.setOpaque(true);
            lblCellComponent.setOpaque(true);
            lblCellComponent.setHorizontalAlignment(CENTER);
            txtDollar =  new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            txtChangeAmount=new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
            lblCellComponent.setBorder(new EmptyBorder(0,0,0,0));
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            lblDollar.setBorder(new EmptyBorder(0,0,0,0));
            txtDollar.setBorder(new EmptyBorder(0,0,0,0));
            txtCostElement.setBorder(new EmptyBorder(0,0,0,0));
            txtChangeAmount.setBorder(new EmptyBorder(0,0,0,0));
            
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int col){
           switch(col){
              case LINE_COLUMN:
                    if(isSelected){
                        lblCellComponent.setBackground(java.awt.Color.YELLOW);
                        lblCellComponent.setForeground(java.awt.Color.black);
                    }else{
                        lblCellComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        lblCellComponent.setForeground(java.awt.Color.black);
                    }
                    value=(value==null?EMPTY_STRING:value);
                    lblCellComponent.setText(value.toString().trim());
                    return lblCellComponent;
                case COST_COLUMN:
                    value=(value==null?EMPTY_STRING:value);
                    if(isSelected){
                            lblComponent.setBackground(java.awt.Color.YELLOW);
                            lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(!value.toString().equals(EMPTY_STRING)){
                            lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                                lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                                lblComponent.setForeground(java.awt.Color.black);
                                
                            }else{
                                lblComponent.setBackground(java.awt.Color.WHITE);
                                lblComponent.setForeground(java.awt.Color.black);
                            }
                            
                        }
                    }
                    txtCostElement.setText(value.toString());
                    lblComponent.setText(txtCostElement.getText());
                    return lblComponent;
                case COST_DESCR_COLUMN:
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        lblComponent.setForeground(java.awt.Color.black);
                    }
                    value=(value==null?EMPTY_STRING:value);
                    lblComponent.setText(value.toString());
                    return lblComponent;
                    
                    
                case CHANGE_COLUMN:
                    value=(value==null?"0.00":value);
                    txtChangeAmount.setValue(new Double(value.toString()).doubleValue());
                    lblDollar.setText(txtChangeAmount.getText());
                    if(isSelected){
                        lblDollar.setBackground(java.awt.Color.YELLOW);
                        lblDollar.setForeground(java.awt.Color.black);
                    }else{
                        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblDollar.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                            lblDollar.setForeground(java.awt.Color.black);
                        }else{
                            lblDollar.setBackground(java.awt.Color.white);
                            lblDollar.setForeground(java.awt.Color.black);
                        }
                    }
                    return lblDollar;
                case OBLIGATED_COLUMN:
                case AMOUNT_COLUMN:
                    if(isSelected){
                        lblDollar.setBackground(java.awt.Color.YELLOW);
                        lblDollar.setForeground(java.awt.Color.black);
                    }else{
                        lblDollar.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        lblDollar.setForeground(java.awt.Color.black);
                    }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtDollar.setText(EMPTY_STRING);
                        lblDollar.setText(txtDollar.getText());
                    }else{
                        txtDollar.setValue(new Double(value.toString()).doubleValue());
                        lblDollar.setText(txtDollar.getText());
                    }
                    return lblDollar;
            }
            return lblComponent;
        }
      }
   public class AwardBudgetOvrheadTableModel extends AbstractTableModel{
        
        private String [] colNames = {EMPTY_STRING,EMPTY_STRING,EMPTY_STRING,EMPTY_STRING,EMPTY_STRING,EMPTY_STRING};
        private Class colClass[]={String.class,String.class,String.class,Double.class,Double.class,Double.class};
        
        public boolean isCellEditable(int row, int col){
            if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                return false;
            }
            else if(col==CHANGE_COLUMN){
                return true;
            }
            else{
                return false;
            }
        }
        public int getColumnCount() {
            return colNames.length;
        }
        public String getColumnName(int col){
            return colNames[col];
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        public int getRowCount() {
            if(cvTableOverHead==null||cvTableOverHead.size()==0){
                return 0;
            }else{
                return cvTableOverHead.size();
            }
        }
        public void setData(CoeusVector cvTableOverHead){
            cvTableOverHead=cvTableOverHead;
            
        }
        public void setValueAt(Object value,int row,int col){
            
            if(cvTableOverHead.size()==0||cvTableOverHead==null) return;
            AwardBudgetDetailBean
                awardBudgetDetailBean=(AwardBudgetDetailBean)cvTableOverHead.get(row);
            switch(col){
                case CHANGE_COLUMN:
                    double changeAmount = 0.00;
                    changeAmount = new Double(value.toString()).doubleValue();
                    
                    // double changeAmount=Double.parseDouble(value.toString());
                    double changeAmountFromBean = awardBudgetDetailBean.getOblChangeAmount();
                    if (changeAmount!=changeAmountFromBean) {
                        awardBudgetDetailBean.setOblChangeAmount(changeAmount);
                        awardBudgetDetailBean.setModifiedByUser(true);
                        modified=true;
                        if(getFunctionType()==TypeConstants.NEW_MODE){
                            //do nothing
                        }else{
                            awardBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        
                     }
                    if(modified){
                        awardBudgetOvrheadTableModel.fireTableDataChanged();
                        awardBudgetOverHeadTotalTableModel.fireTableDataChanged();
                    }
                    break;
            }
        }
        public Object getValueAt(int rowIndex, int columnIndex) {
            AwardBudgetDetailBean
            awardBudgetDetailBean=(AwardBudgetDetailBean)cvTableOverHead.get(rowIndex);
            double totalDetailAmount=awardBudgetDetailBean.getOblChangeAmount()+awardBudgetDetailBean.getOblAmount();
            awardBudgetDetailBean.setSequenceNumber(awardBudgetHeaderBean.getSequenceNumber());
            awardBudgetDetailBean.setAmountSequenceNo(awardBudgetHeaderBean.getAmountSequenceNo());
            switch(columnIndex){
                case LINE_COLUMN:
                    //return new Integer(awardBudgetDetailBean.getLineItemNo());
                    //return new Integer(rowIndex+1);
                    return new Integer(awardBudgetDetailBean.getLineItemNoDisplay());
                case COST_COLUMN:
                    return new String(awardBudgetDetailBean.getCostElement());
                case COST_DESCR_COLUMN:
                    return new String(awardBudgetDetailBean.getCostElementDescription().trim());
                case CHANGE_COLUMN:
                    return new Double(awardBudgetDetailBean.getOblChangeAmount());
                case OBLIGATED_COLUMN:
                    return new Double(awardBudgetDetailBean.getOblAmount());
                case AMOUNT_COLUMN:
                    return new Double(totalDetailAmount);
            }
            return EMPTY_STRING;
           
        }
      }
    public class AwardBudgetOverHeadTotalTableModel extends AbstractTableModel{
        private String []colNames={"Total Amount",EMPTY_STRING,EMPTY_STRING,EMPTY_STRING};
        private Class colClass[]={String.class,Double.class,Double.class,Double.class};
        private String strTotal="Total:";
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public int getColumnCount() {
            return colNames.length;
        }
        public String getColumnName(int col){
            return colNames[col];
        }
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getRowCount() {
            return 1;
        }
        public void setData(CoeusVector cvTblOverHead){
            cvTableOverHead=cvTblOverHead;
        }
        public Object getValueAt(int rowIndex, int columnIndex) {
            CoeusVector cvOverHeadTotal=getOverHeadTotals(cvTableOverHead);
            switch(columnIndex){
                case TOTAL_COLUMN:
                    return strTotal;
                case CHANGE_AMOUNT:
                    return cvOverHeadTotal.get(0);
                case TOTAL_OBL_AMOUNT:
                    return cvOverHeadTotal.get(1);
                case TOTAL_AMOUNT:
                    return cvOverHeadTotal.get(2);
            }
            return EMPTY_STRING;
        }
     }
   public class AwardBudgetTotalTableCellRenderer extends DefaultTableCellRenderer{
        private JLabel lblComponent;
        private DollarCurrencyTextField txtDollar;
        
        public AwardBudgetTotalTableCellRenderer(){
            lblComponent=new JLabel();
            lblComponent.setOpaque(true);
            lblComponent.setHorizontalAlignment(RIGHT);
            txtDollar =  new DollarCurrencyTextField();
            
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            txtDollar.setBorder(new EmptyBorder(0,0,0,0));
        }
       public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int col){
           switch (col){
             case TOTAL_COLUMN:
                    lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                    lblComponent.setForeground(java.awt.Color.black);
                    
                    value=(value==null?EMPTY_STRING:value);
                    lblComponent.setText(value.toString());
                    return lblComponent;
                    
                case CHANGE_AMOUNT:
                    txtDollar.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                    txtDollar.setForeground(java.awt.Color.black);
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtDollar.setText(EMPTY_STRING);
                        lblComponent.setText(txtDollar.getText());
                    }else{
                        txtDollar.setValue(new Double(value.toString()).doubleValue());
                        lblComponent.setText(txtDollar.getText());
                    }
                    return lblComponent;
                case TOTAL_OBL_AMOUNT:
                    txtDollar.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                    txtDollar.setForeground(java.awt.Color.black);
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtDollar.setText(EMPTY_STRING);
                        lblComponent.setText(txtDollar.getText());
                    }else{
                        txtDollar.setValue(new Double(value.toString()).doubleValue());
                        lblComponent.setText(txtDollar.getText());
                    }
                    return lblComponent;
                case TOTAL_AMOUNT:
                    txtDollar.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                    txtDollar.setForeground(java.awt.Color.black);
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtDollar.setText(EMPTY_STRING);
                        lblComponent.setText(txtDollar.getText());
                    }else{
                        txtDollar.setValue(new Double(value.toString()).doubleValue());
                        lblComponent.setText(txtDollar.getText());
                    }
                    return lblComponent;
            }
            return lblComponent;
        }
    }
    /** Table model for Award Budget Detail total */
    public class AwardBudgetDetailTotalTableModel extends AbstractTableModel{
        private String []colNames={"Total:",EMPTY_STRING,EMPTY_STRING,EMPTY_STRING};
        private Class colClass[]={String.class,Double.class,Double.class,Double.class};
        private String strTotal="Total:";
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            return 1;
        }
        public void setData(CoeusVector cvTableDetails){
            cvTableDetails=cvTableDetails;
        }
        
        public String getColumnName(int col){
            return colNames[col];
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            CoeusVector cvTotalDetails=getTotalDetails(cvTableDetails);
            switch(columnIndex){
                case TOTAL_COLUMN:
                    return strTotal;
                case CHANGE_AMOUNT:
                    return cvTotalDetails.get(0);
                case TOTAL_OBL_AMOUNT:
                    return cvTotalDetails.get(1);
                case TOTAL_AMOUNT:
                    return cvTotalDetails.get(2);
            }
            return EMPTY_STRING;
        }
     }
   private CoeusVector setLineItemForDisplay(CoeusVector cvData){
        if(cvData != null && cvData.size() > 0){
            int lineNo = 0;
            for (int index = 0 ; index < cvData.size() ; index++){
                
                AwardBudgetDetailBean awardBudgetDetailBean = 
                                        (AwardBudgetDetailBean)cvData.get(index);
                lineNo = lineNo+1;
                awardBudgetDetailBean.setLineItemNoDisplay(lineNo);
            }//end for
        }//end if
        return cvData;
    }//end setLineItemForDisplay
   
    public CoeusVector calculateCE(CoeusVector cvDataToServer) throws CoeusException{
        CoeusVector cvData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        awardBudgetDetailForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        String connectTo = CoeusGuiConstants.CONNECTION_URL + AWARD_BUDGET_SERVLET;
        request.setFunctionType(CALCULATE_COST_ELEMENT);
        request.setDataObject(cvDataToServer);
        AppletServletCommunicator comm =
                 new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                awardBudgetDetailForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                cvData = (CoeusVector)response.getDataObject();
            }else{
                awardBudgetDetailForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                throw new CoeusException(response.getMessage(),0);
            }
        }else{
            awardBudgetDetailForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
        return cvData;
    }
    public void setTableKeyTraversal(){
         javax.swing.InputMap im = awardBudgetDetailForm.tblAwardDetail.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
         KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
         KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
         final Action oldTabAction = awardBudgetDetailForm.tblAwardDetail.getActionMap().get(im.get(tab));
         Action tabAction = new AbstractAction() {
             int row = 0;
             int column =0;
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                boolean selectionOut=false;
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                if((rowCount-1)==row && column==(columnCount-3)){
                    if(getFunctionType()!=TypeConstants.DISPLAY_MODE){
                        selectionOut=true;
                        awardBudgetDetailForm.btnSave.requestFocusInWindow();
                    }
                    else{
                        btnClose.requestFocusInWindow();
                    }
                }
                row = table.getSelectedRow();
                column = table.getSelectedColumn();
                if(rowCount<1){
                    columnCount = 0;
                    row = 0;
                    column=0;
                    awardBudgetDetailForm.btnSave.requestFocusInWindow();
                    return ;
               }
                
               while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    if (row == rowCount) {
                        row = 0;
                   }
                   if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                if(!selectionOut){
                    table.changeSelection(row, column, false, false);
                }
             }
        };
        awardBudgetDetailForm.tblAwardDetail.getActionMap().put(im.get(tab), tabAction);
        final Action oldShiftTabAction = awardBudgetDetailForm.tblAwardDetail.getActionMap().get(im.get(shiftTab));
        Action tabShiftAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldShiftTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                      column -= 1;
                  if (column <= 0) {
                        column = CHANGE_COLUMN;
                        row -=1;
                    }
                   if (row < 0) {
                        row = rowCount-1;
                    }
                if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
              table.changeSelection(row, column, false, false);
            }
        };
        awardBudgetDetailForm.tblAwardDetail.getActionMap().put(im.get(shiftTab), tabShiftAction);
     }
    
    public void itemStateChanged(java.awt.event.ItemEvent e) {
        
        Object source  = e.getSource();
        boolean selected = awardBudgetDetailForm.chkOnOffCampusFlag.isSelected();
        
//        if(selected == awardBudgetHeaderBean.isOnOffCampusFlag()){
//            return ;
//        }
        
        if(source.equals(awardBudgetDetailForm.chkOnOffCampusFlag)){
            /*checkForFlag();
            
            int option = CoeusOptionPane.showQuestionDialog(
            "Changing the Campus Flag will save and recalcualate.",
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            2);
            switch( option ) {
                case (CoeusOptionPane.SELECTION_YES):
                    awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    try{
                        saveFormData();
                        if(successResponse){
                            awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                        }
                     }catch(CoeusException ce){
                        CoeusOptionPane.showErrorDialog(ce.getMessage());
                        return;
                    }finally{
                        awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }
                    break;
                case(CoeusOptionPane.SELECTION_NO):
                    boolean campusFlag = awardBudgetDetailForm.chkOnOffCampusFlag.isSelected();
                    awardBudgetDetailForm.chkOnOffCampusFlag.setSelected(!campusFlag);
                    break;
                default:
                    break;
            }*/
            
            
            boolean onOffcampusFlag = awardBudgetDetailForm.chkOnOffCampusFlag.isSelected();
            if(awdBudCampusBasedOnCE.intValue() == 0){
                awardBudgetDetailEditor.stopCellEditing();
                awardBudgetOverHeadEditor.stopCellEditing();
                Equals eqType = new Equals("onOffCampusFlag" , onOffcampusFlag);
                //cvTableOverHead.removeAllElements();
                cvTableOverHead = cvCalCE.filter(eqType);
                for(int index = 0 ; index<cvTableOverHead.size();index++){
                    AwardBudgetDetailBean awardDetailBean = 
                                (AwardBudgetDetailBean)cvTableOverHead.get(index);
                    awardDetailBean.setMitAwardNumber(awardBudgetHeaderBean.getMitAwardNumber());
                    awardDetailBean.setLineItemNo(getMaxLineItemNumber()+1);
                    awardDetailBean.setVersionNo(awardBudgetHeaderBean.getVersionNo());
                    awardDetailBean.setAmountSequenceNo(awardBudgetHeaderBean.getAmountSequenceNo());
                    awardDetailBean.setSequenceNumber(awardBudgetHeaderBean.getSequenceNumber());
                    awardDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                    awardDetailBean.setOblChangeAmount(0);
                    cvMaxLineNo.add(awardDetailBean);
                }//End of for
                
                cvTableOverHead = setLineItemForDisplay(cvTableOverHead);
                awardBudgetOvrheadTableModel.fireTableDataChanged();
                if(cvTableOverHead.size()>0){
                    awardBudgetDetailForm.tblAwardOverhead.setRowSelectionInterval(0,0);
                }
                
            }//End of inner if
        }//End of if
    }
    
    /*private boolean checkForFlag(){
        boolean campusFlag = awardBudgetDetailForm.chkOnOffCampusFlag.isSelected();
        boolean found = false;
        if(awdBudCampusBasedOnCE.intValue() == 0){
            for(int index = 0 ; index<cvTableOverHead.size() ; index++){
                AwardBudgetDetailBean awardBudgetDetailBean = 
                                        (AwardBudgetDetailBean)cvTableOverHead.get(index);
                if(awardBudgetDetailBean.isOnOffCampusFlag() != campusFlag){
                    Equals eqCE = new Equals("costElement",awardBudgetDetailBean.getCostElement());
                    CoeusVector cvFileterd = cvToBeDelCalcCE.filter(eqCE);
                    
                    if(cvFileterd != null && cvFileterd.size()>0){
                        AwardBudgetDetailBean delBean = (AwardBudgetDetailBean)cvFileterd.get(0);
                        delBean.setAcType(TypeConstants.DELETE_RECORD);
                        cvDeletedItem.add(delBean);
                        found = true;
                    }
                    //cvTableOverHead.remove(index);
                }//End if
            }//End for
            
            Equals eqType = new Equals("onOffCampusFlag" , campusFlag);
            cvTableOverHead =cvTableOverHead.filter(eqType);
        }//End if
        
        return found;
    }//End chechFlag
    
    public boolean checkIfParameterChanged(){
        boolean status = false;
        if(cvTableOverHead!=null&&cvTableOverHead.size()>0){
            if(checkForFlag()){
                int option = CoeusOptionPane.showQuestionDialog(
                "Since the Parameter is changed please save before proceeding",
                CoeusOptionPane.OPTION_YES_NO,
                2);
                switch( option ) {
                    case (CoeusOptionPane.SELECTION_YES):
                        awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                        try{
                            saveFormData();
                            if(successResponse){
                                awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                                status = true;
                                //dlgAwardBudgetMain.dispose();
                            }
                         }catch(CoeusException ce){
                            CoeusOptionPane.showErrorDialog(ce.getMessage());
                        }finally{
                            awardBudgetDetailForm.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                        }
                        break;
                    case(CoeusOptionPane.SELECTION_NO):
                        status = false;
                        //dlgAwardBudgetMain.dispose();
                        //awardBudgetDetailForm=null;
                        break;
                    default:
                        break;
                }//End switch
            }//End checkForFlag
        }//End if
        return status;
    }//End checkIfParameterChanged*/
    
    private CoeusVector deleteCalcCe(){
        //CoeusVector cvMasterData =(CoeusVector)cvDetailData.get(1);
        CoeusVector cvCEToDelete = new CoeusVector();
        
        if(cvMasterData != null && cvMasterData.size()>0){
            for(int index = 0 ; index < cvMasterData.size() ; index++){
                AwardBudgetDetailBean awdBudDetailBean = (AwardBudgetDetailBean)cvMasterData.get(index);
                
                String costElement = awdBudDetailBean.getCostElement().trim();
                for (int j = 0; j < cvCalCE.size(); j++){
                    AwardBudgetDetailBean cmpAwdBudDetailBean = (AwardBudgetDetailBean)cvCalCE.get(j);
                    String cmpCostElement = cmpAwdBudDetailBean.getCostElement().trim();
                    
                    if(costElement.equals(cmpCostElement)){
                        awdBudDetailBean.setAcType(TypeConstants.DELETE_RECORD);
                        cvCEToDelete.add(awdBudDetailBean);
                    }//End of inner if
                }//End of inner for
            }//End of outer for
        }//End of outer if
        
        if(cvCEToDelete != null && cvCEToDelete.size()>0){
            CoeusVector cvData = new CoeusVector();
            cvData.addAll(cvCEToDelete);
            
            for(int index = 0 ; index< cvData.size() ; index++){
                
                AwardBudgetDetailBean awdBudgetDetailBean = (AwardBudgetDetailBean)cvData.get(index);
                Equals eqCostElement = new Equals("costElement" , awdBudgetDetailBean.getCostElement());
                CoeusVector cvFiltered = cvTableOverHead.filter(eqCostElement);
                
                if(cvFiltered != null && cvFiltered.size()>0){
                    AwardBudgetDetailBean detailBean = (AwardBudgetDetailBean)cvFiltered.get(0);
                    detailBean.setAcType(TypeConstants.UPDATE_RECORD);
                    detailBean.setLineItemNo(awdBudgetDetailBean.getLineItemNo());
                    detailBean.setUpdateTimestamp(awdBudgetDetailBean.getUpdateTimestamp());
                    detailBean.setAwLineItemNo(awdBudgetDetailBean.getAwLineItemNo());
                    
                    NotEquals neCostElement = new NotEquals("costElement" , awdBudgetDetailBean.getCostElement());
                    cvCEToDelete = cvCEToDelete.filter(neCostElement);
                }
            }//End of for
        }//End of if
        return cvCEToDelete;
    }//End of deleteCalcCe
    
    public void cleanUp(){
        awardBudgetSummaryBean = null;
        awardBudgetHeaderBean = null;
        //awardBudgetDetailForm.tblAwardDetail.removeMouseListener(this);
        
        awardBudgetDetailTableModel = null;
        awardBudgetDetailTableCellRenderer = null;
        awardBudgetDetailTotalTableModel = null;
        awardBudgetDetailEditor = null;
        awardBudgetOverHeadEditor = null;
        awardBudgetOvrheadTableModel = null;
        awardBudgetOverHeadTotalTableModel = null;
        awardBudgetTotalTableCellRenderer = null;

        btnPost = null;
        btnApproval = null;
        btnClose = null;

        cvTableDetails = null;
        cvDeletedItem = null;
        cvCalCE = null;
        cvTableOverHead = null;
        cvCostElements = null;
        cvType = null;
        cvBudgetStatus = null;
        cvMaxLineNo = null;
        cvOHRatesTypes = null;
        cvMasterData = null;
        
        costElementsLookupWindow = null;
        
        awardBudgetDetailForm = null;
        dlgAwardBudgetMain = null;
    }//End of cleanUp
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    
    /**
     * This method displays the list of Inactive cost elements
     */
    public void displayCENotAvailableMessages() {
        CostElementMessageForm costElementMessageForm = new CostElementMessageForm();
        costElementMessageForm.setFormData(vecCEMessages);
        costElementMessageForm.display();
    }
    
    /*
     * Method to fetch the cost element status from server
     * @param vecCostElements
     * @return boolean value
     */
    private boolean getCostElementDetails(Vector vecCostElements) {
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/BudgetMaintenanceServlet";
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        int selection;
        String message;
        MessageFormat formatter = new MessageFormat("");
        vecCEMessages = new Vector();
        requester.setFunctionType(GET_INACTIVE_COST_ELEMENTS);
        requester.setDataObjects(vecCostElements);
        Vector inActive = new Vector();
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            inActive =(Vector) responder.getDataObjects();
        }
        if(inActive!= null && inActive.size() >0){
            for(int index=0; index<inActive.size();index++){
                String costElement = (String) inActive.get(index);
                String costElementDesc = (String) inActive.get(++index);
                message = formatter.format(coeusMessageResources.parseMessageKey("budgetSelect_exceptionCode.1062"),costElement,costElementDesc);
                vecCEMessages.add(message);
            }
            return true;
        }
        return false;
    }
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
  }
