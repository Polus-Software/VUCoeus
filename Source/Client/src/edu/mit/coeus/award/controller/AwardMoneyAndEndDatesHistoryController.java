/*
 * AwardMoneyAndEndDatesHistoryController.java
 *
 * Created on June 7, 2004, 12:54 PM
 */

package edu.mit.coeus.award.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.text.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;


import edu.mit.coeus.award.bean.AwardAmountInfoBean.*;
import edu.mit.coeus.award.controller.AwardController.*;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.event.BeanUpdatedListener;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.award.gui.AwardMoneyAndEndDatesHistoryForm;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.applet.AppletContext;

/**
 *
 * @author  surekhan
 */
public class AwardMoneyAndEndDatesHistoryController extends AwardController 
    implements ActionListener,BeanUpdatedListener,AdjustmentListener{
    private AwardMoneyAndEndDatesHistoryForm awardMoneyAndEndDatesHistoryForm;
    private MoneyAndEndDatesForm moneyAndEndDatesForm;
    private AwardAmountTreeTable awardAmountTreeTable;
    private QueryEngine queryEngine;
    private CoeusVector cvHistory, cvTraversalBeans;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusDlgWindow dlgHistory;
    /**holds the index of the bean in traversal data
     *whose history is displayed.
     *will increment when NEXT, decrement when PREV
     */
    private int index;
    
    /**
     * holds the index of the traversal data in History screen
     */
    // Added for # 3857/654  ArrayIndex out of bound exception.
    private int traversalIndex;
    
//    private static final int WIDTH = 775;
    //Modified for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
    private static final int WIDTH = 960;
    private static final int HEIGHT = 540;
    //Modified for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    private char functionType;
    private static final char GET_HISTORY_DATA = 'M';
    private static final String AWARD_SERVLET = "/AwardMaintenanceServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
    AWARD_SERVLET;
    private static final String PRINT_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
    private HistoryTableModel historyTableModel;
    //Commented for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
//    private static final int SR_NUM = 0;
//    private static final int CHANGE = 1;
//    private static final int TOTAL = 2;
//    private static final int OBL_DIST_AMOUNT = 3;
//    private static final int ANTI_AMOUNT_CHANGE = 4;
//    private static final int ANTI_AMOUNT_TOTAL = 5;
//    private static final int ANTI_AMOUNT_DISTRIBUTABLE = 6;
//    private static final int FINAL_EXP_DATE = 7;
//    private static final int OBL_EFF_DATE = 8;
//    private static final int OBL_EXP_DATE = 9;
//    private static final int TIME = 10;
//    private static final int USER = 11;
    //Commented for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
    private static final int COMMENTS_COL = 0;
    private static final int SR_NUM = 1;
    private static final int TRANSACTION_TYPE_COL = 2;
    private static final int CHANGE = 3;
    private static final int TOTAL = 4;
    private static final int OBL_DIST_AMOUNT = 5;
    private static final int ANTI_AMOUNT_CHANGE = 6;
    private static final int ANTI_AMOUNT_TOTAL = 7;
    private static final int ANTI_AMOUNT_DISTRIBUTABLE = 8;
    private static final int FINAL_EXP_DATE = 9;
    private static final int OBL_EFF_DATE = 10;
    private static final int OBL_EXP_DATE = 11;
    private static final int NOTICE_DATE_COL = 12;
    private static final int TIME = 13;
    private static final int USER = 14;
    
    //#3857  ----  start
    public static final int DIRECT_OBLIGATED_COL = 15;
    public static final int INDIRECT_OBLIGATED_COL = 16;
    public static final int DIRECT_ANTICIPATED_COL = 17;
    public static final int INDIRECT_ANTICIPATED_COL = 18;
    
    private int visibleColumns[] ;
    //#3857 -- end  
    
    
    
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    private HistoryRenderer historyRenderer;
    private JTable tableHistoryModel;
    private AwardBaseBean awardBaseBean;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private DateUtils dateUtils;
    private JScrollBar tableScrollBar;
    
    //Modified for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
//    private static final int ROW_HEIGHT = 22;
//    private static final int NEW_ROW_HEIGHT = 50;
    private static final int ROW_HEIGHT = 20;
    private static final int NEW_ROW_HEIGHT = 40;
    //Modified for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
//    private int number = 0;
    
    private HistoryHeaderRenderer historyHeaderRenderer;
    
    //Money history report
    private static final char PRINT_MONEY_HISTORY = 'h';
    
    private HashMap objMap = new HashMap();
    
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
    private HistoryTableEditor historyTableEditor;
    private ImageIcon imgIcnJustified, imgIcnNotJustified;
    private final Color negativeAmountFontColor = Color.RED;
    private final Color modifiedDataFontColor = Color.BLUE;
    private final Color defaultFontColor = Color.BLACK;
    private final Color selectedRowBackgroundColor = Color.YELLOW;
    private final Color defaultRowBackgroundColor = (Color) UIManager.getDefaults().get("Panel.background");
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    
    /** Creates a new instance of AwardMoneyAndEndDatesHistoryController */
    public AwardMoneyAndEndDatesHistoryController(AwardBaseBean awardBaseBean) {
        
        super(awardBaseBean);
      
        // this.mdiForm = mdiForm;
        this.functionType = functionType;
        cvHistory = new CoeusVector();
        dateUtils = new DateUtils();
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        imgIcnJustified = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
        imgIcnNotJustified = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NOT_JUSTIFIED));
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        //coeusMessageResources = CoeusMessageResources.getInstance();
        awardMoneyAndEndDatesHistoryForm = new AwardMoneyAndEndDatesHistoryForm();
        historyTableModel = new HistoryTableModel();
      
        historyRenderer = new HistoryRenderer();
        historyHeaderRenderer = new HistoryHeaderRenderer();
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        historyTableEditor = new HistoryTableEditor();
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        postInitComponents();
        registerComponents();
        setTableEditors();
        // #3857 -- start  
        initSettings();
        //#3857 -- end
        registerEvents();
    }
    
     public void registerEvents() {
        addBeanUpdatedListener(this, AwardAmountInfoBean.class);
    }
    
        public void beanUpdated(BeanEvent beanEvent) {
            if(beanEvent.getSource().getClass().equals(AwardTransactionDetailsController.class)) {
                if(beanEvent.getBean().getClass().equals(AwardAmountInfoBean.class)) {
                    index=beanEvent.getMessageId();
                    if(awardMoneyAndEndDatesHistoryForm.tblHistory.getRowCount()>index){
                        awardMoneyAndEndDatesHistoryForm.tblHistory.setRowSelectionInterval(index,index);
                    }
                    awardMoneyAndEndDatesHistoryForm.tblHistory.scrollRectToVisible(
                    awardMoneyAndEndDatesHistoryForm.tblHistory.getCellRect(
                    index ,0, true));
                }
            }
        }
        
    private void postInitComponents(){
        dlgHistory = new CoeusDlgWindow(mdiForm);
        dlgHistory.setResizable(false);
        dlgHistory.setModal(true);
        dlgHistory.getContentPane().add(awardMoneyAndEndDatesHistoryForm);
        dlgHistory.setFont(CoeusFontFactory.getLabelFont());
        dlgHistory.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgHistory.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgHistory.getSize();
        dlgHistory.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        
        dlgHistory.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgHistory.dispose();
            }
        });
        dlgHistory.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
        dlgHistory.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgHistory.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                dlgHistory.dispose();
            }
        });

    }
    
    
    private void requestDefaultFocus(){
        awardMoneyAndEndDatesHistoryForm.btnClose.requestFocus();
    }
    
    public void display() {
        //dlgHistory.show();
        dlgHistory.setVisible(true);
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return awardMoneyAndEndDatesHistoryForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        java.awt.Component[] components = {awardMoneyAndEndDatesHistoryForm.btnClose,
        awardMoneyAndEndDatesHistoryForm.btnNext,
        awardMoneyAndEndDatesHistoryForm.btnPrevious,
        awardMoneyAndEndDatesHistoryForm.btnPrint,
        awardMoneyAndEndDatesHistoryForm.btnShowDetailTransaction,
        awardMoneyAndEndDatesHistoryForm.tblHistory};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardMoneyAndEndDatesHistoryForm.setFocusTraversalPolicy(traversePolicy);
        awardMoneyAndEndDatesHistoryForm.setFocusCycleRoot(true);
        
        historyTableModel = new HistoryTableModel();
        awardMoneyAndEndDatesHistoryForm.tblHistory.setModel(historyTableModel);
        awardMoneyAndEndDatesHistoryForm.btnClose.addActionListener(this);
        awardMoneyAndEndDatesHistoryForm.btnNext.addActionListener(this);
        awardMoneyAndEndDatesHistoryForm.btnPrevious.addActionListener(this);
        awardMoneyAndEndDatesHistoryForm.btnPrint.addActionListener(this);
        awardMoneyAndEndDatesHistoryForm.btnShowDetailTransaction.addActionListener(this);
        awardMoneyAndEndDatesHistoryForm.tblHistory.setRowHeight(ROW_HEIGHT);
        awardMoneyAndEndDatesHistoryForm.lblHeader1.setText("Obligated Amount");
        awardMoneyAndEndDatesHistoryForm.lblHeader1.setFont(CoeusFontFactory.getLabelFont());
        awardMoneyAndEndDatesHistoryForm.lblHeader2.setText("Anticipated Amount");
        awardMoneyAndEndDatesHistoryForm.lblHeader2.setFont(CoeusFontFactory.getLabelFont());
        awardMoneyAndEndDatesHistoryForm.lblHeader3.setText("Dates");
        awardMoneyAndEndDatesHistoryForm.lblHeader3.setFont(CoeusFontFactory.getLabelFont());
        awardMoneyAndEndDatesHistoryForm.lblHeader4.setText("Last Update");
        awardMoneyAndEndDatesHistoryForm.lblHeader4.setFont(CoeusFontFactory.getLabelFont());
        //#3857 -- start
        if(AwardAmountTreeTable.SET_DITRECT_INDIRECT) {
              awardMoneyAndEndDatesHistoryForm.lblHeader2.setText("");
            awardMoneyAndEndDatesHistoryForm.lblHeader7.setText("Anticipated Amount");
            awardMoneyAndEndDatesHistoryForm.lblHeader7.setFont(CoeusFontFactory.getLabelFont());
            
            awardMoneyAndEndDatesHistoryForm.lblHeader3.setText("");
            awardMoneyAndEndDatesHistoryForm.lblHeader5.setText("Dates");
            awardMoneyAndEndDatesHistoryForm.lblHeader5.setFont(CoeusFontFactory.getLabelFont());
            
            awardMoneyAndEndDatesHistoryForm.lblHeader4.setText("");
            awardMoneyAndEndDatesHistoryForm.lblHeader6.setText("Last Update");
            awardMoneyAndEndDatesHistoryForm.lblHeader6.setFont(CoeusFontFactory.getLabelFont());
        } else {
            awardMoneyAndEndDatesHistoryForm.lblHeader5.setText("");
            awardMoneyAndEndDatesHistoryForm.lblHeader6.setText("");
            awardMoneyAndEndDatesHistoryForm.lblHeader7.setText("");
        }
        //#3857 -- end
        
        awardMoneyAndEndDatesHistoryForm.pnlHeader.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        tableScrollBar = awardMoneyAndEndDatesHistoryForm.scrPnTable.getHorizontalScrollBar();
        tableScrollBar.addAdjustmentListener(this);
        
    }
    
    public void saveFormData() {
    }
    
    public void setFormData(Object data) {
        //enable - disable PREV Button
       // Added for # 3857/654  ArrayIndex out of bound exception.
        //if(index > 0) {
        if(traversalIndex > 0) {
            //has prev elements. enable PREV
            awardMoneyAndEndDatesHistoryForm.btnPrevious.setEnabled(true);
        }else {
            awardMoneyAndEndDatesHistoryForm.btnPrevious.setEnabled(false);
        }
        //enable - disable NEXT Button
        // Added for # 3857/654  ArrayIndex out of bound exception.
        //if(index == cvTraversalBeans.size() - 1) {
        if(traversalIndex == cvTraversalBeans.size() - 1) {
            //already reached end. no more next elements.
            awardMoneyAndEndDatesHistoryForm.btnNext.setEnabled(false);
        }else {
            awardMoneyAndEndDatesHistoryForm.btnNext.setEnabled(true);
        }
        
        awardBaseBean = (AwardBaseBean)data;
        try{
        CoeusVector cvHistory = getDataFromServer();
        int seqNo = -1;
        if(cvHistory != null) {
             //3857 Direct Indirect Tracking and some other money and end date screen changes -- start
            historyTableModel.setData(cvHistory);
            historyTableModel.fireTableDataChanged();
             //3857 Direct Indirect Tracking and some other money and end date screen changes -- end
            for( int index = 0; index < cvHistory.size(); index++ ){
                AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvHistory.get(index);
                if( seqNo != awardAmountInfoBean.getSequenceNumber()){
                    awardMoneyAndEndDatesHistoryForm.tblHistory.setRowHeight(index, NEW_ROW_HEIGHT);
                    seqNo = awardAmountInfoBean.getSequenceNumber();
                }else{
                    awardMoneyAndEndDatesHistoryForm.tblHistory.setRowHeight(index, ROW_HEIGHT);
                }
            }
            //3857 Direct Indirect Tracking and some other money and end date screen changes -- start
           //   historyTableModel.setData(cvHistory);
             //3857 Direct Indirect Tracking and some other money and end date screen changes -- end
        }
        //moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean().getMitAwardNumber()
        dlgHistory.setTitle("Money And End Dates History for Award - "+awardBaseBean.getMitAwardNumber());
        }catch (CoeusClientException ex){
            CoeusOptionPane.showDialog(ex);
            ex.printStackTrace();
        }
        
    }
    
    
    //    public void setData(CoeusVector cvHistory, AwardAmountInfoBean infoBean){
    //        historyTableModel.setValues(cvHistory);
    //
    //    }
    
    //    public void setValues(CoeusVector cvHistory){
    //        AwardAmountInfoBean infoBean;
    //        infoBean = moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean();
    //        this.cvHistory = cvHistory;
    //        for(int index = 0; index < cvHistory.size() ; index++){
    //            AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvHistory.get(0);
    //            historyTableModel.(index,index);
    //
    //
    //        }
    //    }
    
    //For print money history report
    private void printMoneyHistory()throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(PRINT_MONEY_HISTORY);
        //requesterBean.setDataObject(awardBaseBean.getMitAwardNumber());
        
        //For Streaming
        Hashtable hashtable = new Hashtable();
        hashtable.put("MIT_AWARD_NUMBER", awardBaseBean.getMitAwardNumber());
        requesterBean.setDataObject(hashtable);
        requesterBean.setId("Award/MoneyAndEndDatesHistory");
        requesterBean.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator comm= new AppletServletCommunicator(PRINT_SERVLET, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        String fileName = "";      
        if(responderBean.isSuccessfulResponse()){
//             AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
//             
             fileName = (String)responderBean.getDataObject();
//             System.out.println("Report Filename is=>"+fileName);
//             
//             fileName.replace('\\', '/') ; // this is fix for Mac
//             URL reportUrl = null;
             try{
//                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
//             
//             
//             if (coeusContxt != null) {
//                 coeusContxt.showDocument( reportUrl, "_blank" );
//             }else {
//                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                 bs.showDocument( reportUrl );
//             }
                URL urlObj = new URL(fileName);
                URLOpener.openUrl(urlObj);
             }catch(MalformedURLException muEx){
                 throw new CoeusException(muEx.getMessage());
             }catch(Exception uaEx){
                 throw new CoeusException(uaEx.getMessage());
             }
             
        }else{
             throw new CoeusException(responderBean.getMessage());
        }
    }
 
 
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    /**
     * To make the class level insatnces as null
     */
    public void cleanUp() {
        awardMoneyAndEndDatesHistoryForm = null;
        moneyAndEndDatesForm = null;
        awardAmountTreeTable = null;
        cvHistory = null;
        cvTraversalBeans = null;
        dlgHistory = null;
        historyTableModel = null;
        historyRenderer = null;
        tableHistoryModel = null;
        awardBaseBean = null;
        dateUtils = null;
        historyHeaderRenderer = null;
        removeBeanUpdatedListener(this,AwardAmountInfoBean.class);
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            dlgHistory.setCursor( new Cursor(Cursor.WAIT_CURSOR));
            Object source = actionEvent.getSource();
            if(source.equals(awardMoneyAndEndDatesHistoryForm.btnClose)){
                dlgHistory.dispose();
            }else if(source.equals(awardMoneyAndEndDatesHistoryForm.btnNext)){
                // Added for # 3857/654  ArrayIndex out of bound exception.
               // index = index + 1;
                traversalIndex = traversalIndex + 1;
                AwardBaseBean awardBaseBean = (AwardBaseBean)cvTraversalBeans.get(traversalIndex);
                setFormData(awardBaseBean);
                
            }else if(source.equals(awardMoneyAndEndDatesHistoryForm.btnPrevious)){
                 // Added for # 3857/654  ArrayIndex out of bound exception.
               // index = index - 1;
                traversalIndex = traversalIndex - 1;
                AwardBaseBean awardBaseBean = (AwardBaseBean)cvTraversalBeans.get(traversalIndex);
                setFormData(awardBaseBean);
            }else if(source.equals(awardMoneyAndEndDatesHistoryForm.btnShowDetailTransaction)){
                AwardTransactionDetailsController controller= null;
                int selRow = awardMoneyAndEndDatesHistoryForm.tblHistory.getSelectedRow();
                if(selRow!= -1){
                    if(controller== null){
                        controller = new AwardTransactionDetailsController(dlgHistory);
                        controller.setData(cvHistory,selRow);                     
                        controller.display();
                        
                        
                    }
                }else{
                    CoeusOptionPane.showInfoDialog("Please select a row ");
                }
            }else if (source.equals(awardMoneyAndEndDatesHistoryForm.btnPrint)){
                if (awardMoneyAndEndDatesHistoryForm.tblHistory.getRowCount()> 0) {
                    try{
                        printMoneyHistory();

                    }catch(Exception e){
                        e.printStackTrace();
                        CoeusOptionPane.showErrorDialog(e.getMessage());
                    }
                    
                }
            }
        }
        finally {
            dlgHistory.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    public void setTableEditors(){
        
        JTableHeader tableHeader = awardMoneyAndEndDatesHistoryForm.tblHistory.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        //tableHeader.setHorizontalAlignment().
        //awardMoneyAndEndDatesHistoryForm.tblHistory.setSelectionBackground(java.awt.Color.GRAY);
        //awardMoneyAndEndDatesHistoryForm.tblHistory.setSelectionForeground(java.awt.Color.black);
        awardMoneyAndEndDatesHistoryForm.tblHistory.setOpaque(false);
        awardMoneyAndEndDatesHistoryForm.tblHistory.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        awardMoneyAndEndDatesHistoryForm.tblHistory.setShowVerticalLines(false);
        awardMoneyAndEndDatesHistoryForm.tblHistory.setShowHorizontalLines(false);
        awardMoneyAndEndDatesHistoryForm.tblHistory.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        awardMoneyAndEndDatesHistoryForm.tblHistory.setIntercellSpacing(new java.awt.Dimension(0,0));
        
        /*
         * UserId to UserName Enhancement - Start
         * Modified the width of the user id to display username
         */         
        //int colSize[] = {20,130, 100, 140, 140, 100, 150, 130, 130, 130, 200, 90};
        
        
        
        //Commented for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        //int colSize[] = {20,130, 100, 140, 140, 100, 150, 130, 130, 130, 200, 200};
        //Commented for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        //UserId to UserName Enhancement - End

        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        //int colSize[] = {20,20,120,100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 150,130};
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        
        int colSize[];
        
        //#Case 3857 -- start
        if(AwardAmountTreeTable.SET_DITRECT_INDIRECT) {
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
            colSize = new int[]{20,20,120,100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 150,130,100,100,100,100};
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        } else {
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
            colSize = new int[]{20,20,120,100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 150,130};
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        }
          //#Case 3857 -- end
        
        for(int col = 0; col < colSize.length; col++) {
            TableColumn tableColumn = awardMoneyAndEndDatesHistoryForm.tblHistory.getColumnModel().getColumn(col);
            tableColumn.setPreferredWidth(colSize[col]);
            tableColumn.setCellRenderer(historyRenderer);
        }
        for ( int index = 0; index < historyTableModel.getColumnCount(); index ++){
            TableColumn column = awardMoneyAndEndDatesHistoryForm.tblHistory.getColumnModel().getColumn(index);
            column.setHeaderRenderer(historyHeaderRenderer);
        }
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        TableColumn column = awardMoneyAndEndDatesHistoryForm.tblHistory.getColumnModel().getColumn(COMMENTS_COL);
        column.setCellEditor(historyTableEditor);
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    }
    
    public CoeusVector getDataFromServer() throws CoeusClientException{
        cvHistory = new CoeusVector();
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_HISTORY_DATA);
        requesterBean.setDataObject(awardBaseBean.getMitAwardNumber());
        AppletServletCommunicator comm= new AppletServletCommunicator(connectTo,
        requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean.isSuccessfulResponse()) {
            cvHistory = (CoeusVector)responderBean.getDataObject();
            return cvHistory;
        }else {
           throw new CoeusClientException(responderBean.getMessage(), CoeusClientException.ERROR_MESSAGE);
           // return null;
        }
    }
    
    //For testing purpose only
    public static void main(String s[]) {
        AwardDetailsBean awardDetailsBean = new AwardDetailsBean();
        awardDetailsBean.setMitAwardNumber("000006-001");
        
        AwardMoneyAndEndDatesHistoryController awardMoneyAndEndDatesHistoryController =
        new AwardMoneyAndEndDatesHistoryController(awardDetailsBean);
        awardMoneyAndEndDatesHistoryController.display();
    }
    
    public void setTraversalData(CoeusVector cvTraversalBeans, AwardBaseBean selectedBean, int index){
        this.cvTraversalBeans = cvTraversalBeans;
         // Added for # 3857/654  ArrayIndex out of bound exception.
        //  this.index = index;
        this.traversalIndex = index;
        setFormData(selectedBean);
    }
    
    public void adjustmentValueChanged(AdjustmentEvent e) {
        int val = tableScrollBar.getValue();
        //JScrollBar headerScrollBar = awardMoneyAndEndDatesHistoryForm.scrPnHeader.getHorizontalScrollBar();
        //headerScrollBar.setValue(val);
        awardMoneyAndEndDatesHistoryForm.scrPnHeader.getHorizontalScrollBar().setValue(val);
    }    
    
    
    class HistoryTableModel extends AbstractTableModel{
        //Commented for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
//        String colNames[] = {"","Change", "Total", "Distributable", "Change", "Total", "Distributable",
//        "Final Exp","Obl Eff","Obl Exp","Time","User" };
//         Class[] colTypes = new Class [] { Integer.class ,String.class , String.class, String.class, String.class, String.class, String.class,
//        String.class, String.class, String.class,String.class,String.class};
        //Commented for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        
       // #3857 -- start
        String colNames[];
        Class[] colTypes ;
       
       public HistoryTableModel() {
           if(AwardAmountTreeTable.SET_DITRECT_INDIRECT) {
               colNames = new String[]{"","","Transaction Type", "Change", "Total", "Distributable", "Change", "Total", "Distributable",
               "Final Exp","Obl Eff","Obl Exp","Notice Date", "Time","User","Direct","Indirect" ,"Direct","Indirect"};
               colTypes = new Class [] {String.class, Integer.class, String.class, String.class , String.class, String.class, String.class, String.class, String.class,
               String.class, String.class, String.class,String.class,String.class, String.class,String.class,String.class,String.class,String.class};
           } else {
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
               colNames = new String[]{"","","Transaction Type", "Change", "Total", "Distributable", "Change", "Total", "Distributable",
               "Final Exp","Obl Eff","Obl Exp","Notice Date", "Time","User" };
               colTypes = new Class [] {String.class, Integer.class, String.class, String.class , String.class, String.class, String.class, String.class, String.class,
               String.class, String.class, String.class,String.class,String.class, String.class};
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
           }
       }
       // #3857 -- start
        
        public int getColumnCount() {
            return colNames.length;
        }
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        
        public int getRowCount() {
            if(cvHistory == null ){
                return 0;
            }else{
                return cvHistory.size();
            }
        }
      
        
        /* returns true if cell is editable else returns false*/
        public boolean isCellEditable(int row, int col){
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
            if(col == COMMENTS_COL){
                AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvHistory.get(row);
                AwardAmountTransactionBean awardAmountTransactionBean = awardAmountInfoBean.getAwardAmountTransaction();
                if(awardAmountTransactionBean !=null 
                        && awardAmountTransactionBean.getComments() != null 
                        && !awardAmountTransactionBean.getComments().equals(EMPTY)){
                        return true;
                }else{
                    return false;
                }
            }
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
            return false;
        }
        
        public void setData(CoeusVector cvHistory){
            cvHistory = cvHistory;
        }
        
        public Class getColumnClass(int colIndex){
            return colTypes[colIndex];
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            AwardAmountInfoBean bean = (AwardAmountInfoBean)cvHistory.get(rowIndex);
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
            AwardAmountTransactionBean awardAmountTransactionBean = bean.getAwardAmountTransaction();
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
            switch(columnIndex){
              
                case SR_NUM:
                    //return ""+rowIndex + 1;
                    return new Integer(bean.getAmountSequenceNumber());
                case CHANGE:
                    return new Double(bean.getObligatedChange());
                case TOTAL:
                    return new Double(bean.getAmountObligatedToDate());
                case OBL_DIST_AMOUNT:
                    return new Double(bean.getObliDistributableAmount());
                case ANTI_AMOUNT_CHANGE:
                    return new Double(bean.getAnticipatedChange());
                case ANTI_AMOUNT_TOTAL:
                    return new Double(bean.getAnticipatedTotalAmount());
                case ANTI_AMOUNT_DISTRIBUTABLE:
                    
                    return new Double(bean.getAnticipatedDistributableAmount());
                case FINAL_EXP_DATE:
//                    String dt = bean.getFinalExpirationDate().toString();
//                    String strDate = dateUtils.formatDate(dt,DATE_FORMAT);
                    //return strDate;
                    if(bean.getFinalExpirationDate()!= null){
                        return bean.getFinalExpirationDate().toString();
                    }
                case OBL_EFF_DATE:
//                    String dat = bean.getCurrentFundEffectiveDate().toString();
//                    String strDat = dateUtils.formatDate(dat,DATE_FORMAT);
//                    return strDat;
                    if(bean.getCurrentFundEffectiveDate()!= null){
                        return bean.getCurrentFundEffectiveDate().toString();
                    }
                case OBL_EXP_DATE:
//                    String date = bean.getObligationExpirationDate().toString();
//                    String strDt = dateUtils.formatDate(date,DATE_FORMAT);
//                    return strDt;
                    if(bean.getObligationExpirationDate()!= null){
                        return bean.getObligationExpirationDate().toString();
                    }
                case TIME:
                    //                    String time = bean.getUpdateTimestamp().toString();
                    //                    String tm = dateUtils.formatTime(time);
                    //                    return tm;
                    return bean.getUpdateTimestamp();
                case USER:
                    return bean.getUpdateUser();
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
                case TRANSACTION_TYPE_COL:
                    if(awardAmountTransactionBean != null){
                        return awardAmountTransactionBean.getTransactionTypeDescription()==null?
                            "" :awardAmountTransactionBean.getTransactionTypeDescription();
                    }else{
                        return "";
                    }
                case NOTICE_DATE_COL:
                    if(awardAmountTransactionBean != null && awardAmountTransactionBean.getNoticeDate() != null){
                        return awardAmountTransactionBean.getNoticeDate().toString();
                    }else{
                        return "";
                    }
                case COMMENTS_COL:
                     if(awardAmountTransactionBean != null ){
                        return awardAmountTransactionBean.getComments();
                    }else{
                        return "";
                    }
              //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
               
                //#3857 -- start
                case DIRECT_OBLIGATED_COL:
                    return new Double(bean.getDirectObligatedChange());
                case INDIRECT_OBLIGATED_COL:
                    return new Double(bean.getIndirectObligatedChange());
                case DIRECT_ANTICIPATED_COL:
                    return new Double(bean.getDirectAnticipatedChange());
                case INDIRECT_ANTICIPATED_COL:
                    return new Double(bean.getIndirectAnticipatedChange());
                //#3857 -- end
                    
            }
            return new Integer(1);
        }
        
    }
    
    class HistoryRenderer  extends DefaultTableCellRenderer   implements TableCellRenderer {
        JPanel pnlGroup;
        JLabel lblGroup, lblValue, lblTemp;
        private int sequenceNo = -1;
        private CoeusTextField coeusTextField;
//        private int number = 0;
        private AwardAmountInfoBean awardAmountInfoBean;
        private static final String AWARD_SEQUENCE = "Award Sequence: ";
        private static final String EMPTY_STRING = "";
        private boolean newGroup;
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
        private JButton btnPanelCommentsPresent,btnCommentsPresent;
        private JButton btnPanelCommentsNotPresent,btnCommentsNotPresent;
        private AwardAmountTransactionBean awardAmountTransacionBean;
        private AwardAmountTransactionBean prevAwardAmountTransacionBean;
        private JPanel pnlButtonComments, pnlButtonNoComments;
        private JLabel lblInvisible;
        private AwardAmountInfoBean prevAwardAmountInfoBean;
        //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        HistoryRenderer(){
            pnlGroup = new JPanel();
            lblValue = new JLabel();
            lblGroup = new JLabel();
            lblTemp = new JLabel();
            coeusTextField = new CoeusTextField();
            coeusTextField.setBorder(new EmptyBorder(0,0,0,0));
            pnlGroup.setLayout(new GridLayout(2, 1));
            lblGroup = new JLabel();
            lblGroup.setFont(CoeusFontFactory.getLabelFont());
            lblGroup.setForeground(Color.BLUE);
            
            lblValue.setOpaque(true);
            lblValue.setBorder(new EmptyBorder(0,0,0,0));
            lblValue.setHorizontalAlignment(JFormattedTextField.RIGHT);
            lblTemp.setHorizontalAlignment(JFormattedTextField.RIGHT);
            pnlGroup.add(lblGroup);
            pnlGroup.add(lblValue);
            
            lblTemp.setOpaque(true);
            lblTemp.setBorder(new EmptyBorder(0,0,0,0));
            
            DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
            decimalFormat.setMinimumIntegerDigits(0);
            decimalFormat.setMaximumIntegerDigits(10);
            
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(2);
            
            decimalFormat.setDecimalSeparatorAlwaysShown(true);
            coeusTextField = new CoeusTextField();
            FormattedDocument formattedDocument = new FormattedDocument(decimalFormat,coeusTextField);
            formattedDocument.setNegativeAllowed(true);
            coeusTextField.setDocument(formattedDocument);
            
            coeusTextField.setHorizontalAlignment(JFormattedTextField.RIGHT);
            
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
            btnCommentsPresent = new JButton(imgIcnJustified);
            btnCommentsNotPresent = new JButton(imgIcnNotJustified);
            btnPanelCommentsPresent = new JButton(imgIcnJustified);
            btnPanelCommentsNotPresent = new JButton(imgIcnNotJustified);
            
            pnlButtonComments = new JPanel();
            pnlButtonComments.setOpaque(true);
            pnlButtonComments.setLayout(new GridLayout(2, 1));
            lblInvisible = new JLabel();
            lblInvisible.setOpaque(true);
            pnlButtonComments.add(lblInvisible);
            pnlButtonComments.add(btnPanelCommentsPresent); 
            
            pnlButtonNoComments = new JPanel();
            pnlButtonNoComments.setOpaque(true);
            pnlButtonNoComments.setLayout(new GridLayout(2, 1));
            lblInvisible = new JLabel();
            lblInvisible.setOpaque(true);
            pnlButtonNoComments.add(lblInvisible);
            pnlButtonNoComments.add(btnPanelCommentsNotPresent); 
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            //Modified for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
            if( row > 0 ){
                prevAwardAmountInfoBean = (AwardAmountInfoBean)cvHistory.get(row - 1);
                prevAwardAmountTransacionBean = prevAwardAmountInfoBean.getAwardAmountTransaction();
                awardAmountInfoBean = (AwardAmountInfoBean)cvHistory.get(row - 1);
                sequenceNo = awardAmountInfoBean.getSequenceNumber();
            }else {
                prevAwardAmountInfoBean = null;
                prevAwardAmountTransacionBean = null;
                sequenceNo = -1;
            }
            
            awardAmountInfoBean = (AwardAmountInfoBean)cvHistory.get(row);
            awardAmountTransacionBean = awardAmountInfoBean.getAwardAmountTransaction();
            if( sequenceNo != awardAmountInfoBean.getSequenceNumber()){
                newGroup = true;
                switch(col){
                    case TRANSACTION_TYPE_COL:
                        lblGroup.setText(AWARD_SEQUENCE+ awardAmountInfoBean.getSequenceNumber());
                        break;
                    default:
                        lblGroup.setText(EMPTY_STRING);
                        break;
                }
            }else {
                newGroup = false;
            }
            //#3857 -- start
            if(AwardAmountTreeTable.SET_DITRECT_INDIRECT) {
                col = visibleColumns[col];
            }
            //#3857 -- end
            switch(col){
                case SR_NUM:
                    //lblValue.setText(EMPTY_STRING+1);
                    if(awardAmountInfoBean.getAmountSequenceNumber()!= -1){
                        lblValue.setText(""+awardAmountInfoBean.getAmountSequenceNumber());
                        lblTemp.setText(""+awardAmountInfoBean.getAmountSequenceNumber());
                    }
                    lblTemp.setForeground(defaultFontColor);
                    lblValue.setForeground(defaultFontColor);
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    break;
                case CHANGE:
                    coeusTextField.setText(EMPTY_STRING+awardAmountInfoBean.getObligatedChange());
                    lblValue.setText(coeusTextField.getText());
                    lblTemp.setText(coeusTextField.getText());
                    
                    if(awardAmountInfoBean.getObligatedChange() < 0){
                        //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getObligatedChange());
                        lblValue.setForeground(negativeAmountFontColor);
                        //lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getObligatedChange());
                        lblTemp.setForeground(negativeAmountFontColor);
                    }
                    else if(awardAmountInfoBean.getObligatedChange() > 0){
                        lblTemp.setForeground(modifiedDataFontColor);
                        lblValue.setForeground(modifiedDataFontColor);
                    }else{
                        lblTemp.setForeground(Color.BLACK);
                        lblValue.setForeground(Color.BLACK);
                        lblTemp.setBackground(table.getSelectionBackground());
                        lblValue.setBackground(table.getSelectionBackground());
                    }
                    lblTemp.setHorizontalAlignment(RIGHT);
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    break;
                case TOTAL:
                    coeusTextField.setText(EMPTY_STRING+awardAmountInfoBean.getAmountObligatedToDate());
                    lblValue.setText(coeusTextField.getText());
                    lblTemp.setText(coeusTextField.getText());
                    
                    if(awardAmountInfoBean.getAmountObligatedToDate() < 0){
                      //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getAmountObligatedToDate());
                      lblValue.setForeground(negativeAmountFontColor);
                      //lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getAmountObligatedToDate());
                      lblTemp.setForeground(negativeAmountFontColor);
                    }else if((prevAwardAmountInfoBean == null && awardAmountInfoBean.getAmountObligatedToDate() >0)
                            ||(prevAwardAmountInfoBean != null 
                            && awardAmountInfoBean.getAmountObligatedToDate() != prevAwardAmountInfoBean.getAmountObligatedToDate())){
                            lblValue.setForeground(modifiedDataFontColor);
                            lblTemp.setForeground(modifiedDataFontColor);
                    }else{
                         //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getAmountObligatedToDate());
                         lblValue.setForeground(defaultFontColor);
                         //lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getAmountObligatedToDate());
                         lblTemp.setForeground(defaultFontColor);
                    }
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    lblTemp.setHorizontalAlignment(RIGHT);
                    lblValue.setHorizontalAlignment(RIGHT);
                    break;
                case OBL_DIST_AMOUNT:
                    coeusTextField.setText(EMPTY_STRING+awardAmountInfoBean.getObliDistributableAmount());
                    lblValue.setText(coeusTextField.getText());
                    lblTemp.setText(coeusTextField.getText());
                    
                    if(awardAmountInfoBean.getObliDistributableAmount() < 0){
                      //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getObliDistributableAmount());
                      lblValue.setForeground(negativeAmountFontColor);
                     // lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getObliDistributableAmount());
                      lblTemp.setForeground(negativeAmountFontColor);
                    }else if((prevAwardAmountInfoBean == null && awardAmountInfoBean.getObliDistributableAmount() >0)
                            ||(prevAwardAmountInfoBean != null 
                            && awardAmountInfoBean.getObliDistributableAmount() != prevAwardAmountInfoBean.getObliDistributableAmount())){
                            lblValue.setForeground(modifiedDataFontColor);
                            lblTemp.setForeground(modifiedDataFontColor);
                    }else{
                        //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getObliDistributableAmount());
                        lblValue.setForeground(defaultFontColor);
                        //lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getObliDistributableAmount());
                        lblTemp.setForeground(defaultFontColor);
                    }
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    lblTemp.setBackground(defaultRowBackgroundColor);
                    lblValue.setBackground(defaultRowBackgroundColor);
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    break;
                case ANTI_AMOUNT_CHANGE:
                    coeusTextField.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedChange());
                    lblValue.setText(coeusTextField.getText());
                    lblTemp.setText(coeusTextField.getText());
                    if(awardAmountInfoBean.getAnticipatedChange() < 0){
                        //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedChange());
                        lblValue.setForeground(negativeAmountFontColor);
                        //lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedChange());
                        lblTemp.setForeground(negativeAmountFontColor);
                    }
                    else if(awardAmountInfoBean.getAnticipatedChange() != 0){
                        lblTemp.setForeground(modifiedDataFontColor);
                        lblValue.setForeground(modifiedDataFontColor);
                    }else{
                        lblTemp.setForeground(defaultFontColor);
                        lblValue.setForeground(defaultFontColor);
                    }
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    break;
                //#3857 -- start    
                case DIRECT_OBLIGATED_COL:
                
                    coeusTextField.setText(EMPTY_STRING+awardAmountInfoBean.getDirectObligatedChange());
                    lblValue.setText(coeusTextField.getText());
                    lblTemp.setText(coeusTextField.getText());
                    if(awardAmountInfoBean.getDirectObligatedChange() < 0){
                        //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedChange());
                        lblValue.setForeground(negativeAmountFontColor);
                        //lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedChange());
                        lblTemp.setForeground(negativeAmountFontColor);
                    } else if(awardAmountInfoBean.getDirectObligatedChange() != 0){
                        lblTemp.setForeground(modifiedDataFontColor);
                        lblValue.setForeground(modifiedDataFontColor);
                    }else{
                        lblTemp.setForeground(defaultFontColor);
                        lblValue.setForeground(defaultFontColor);
                    }
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    break;
                case INDIRECT_OBLIGATED_COL:
                    
                    coeusTextField.setText(EMPTY_STRING+awardAmountInfoBean.getIndirectObligatedChange());
                    lblValue.setText(coeusTextField.getText());
                    lblTemp.setText(coeusTextField.getText());
                    if(awardAmountInfoBean.getIndirectObligatedChange() < 0){
                        //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedChange());
                        lblValue.setForeground(negativeAmountFontColor);
                        //lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedChange());
                        lblTemp.setForeground(negativeAmountFontColor);
                    } else if(awardAmountInfoBean.getIndirectObligatedChange() != 0){
                        lblTemp.setForeground(modifiedDataFontColor);
                        lblValue.setForeground(modifiedDataFontColor);
                    }else{
                        lblTemp.setForeground(defaultFontColor);
                        lblValue.setForeground(defaultFontColor);
                    }
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    break;
                case DIRECT_ANTICIPATED_COL:
                    
                    coeusTextField.setText(EMPTY_STRING+awardAmountInfoBean.getDirectAnticipatedChange());
                    lblValue.setText(coeusTextField.getText());
                    lblTemp.setText(coeusTextField.getText());
                    if(awardAmountInfoBean.getDirectAnticipatedChange() < 0){
                        //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedChange());
                        lblValue.setForeground(negativeAmountFontColor);
                        //lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedChange());
                        lblTemp.setForeground(negativeAmountFontColor);
                    } else if(awardAmountInfoBean.getDirectAnticipatedChange() != 0){
                        lblTemp.setForeground(modifiedDataFontColor);
                        lblValue.setForeground(modifiedDataFontColor);
                    }else{
                        lblTemp.setForeground(defaultFontColor);
                        lblValue.setForeground(defaultFontColor);
                    }
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    break;
                case INDIRECT_ANTICIPATED_COL:
                    
                     coeusTextField.setText(EMPTY_STRING+awardAmountInfoBean.getIndirectAnticipatedChange());
                    lblValue.setText(coeusTextField.getText());
                    lblTemp.setText(coeusTextField.getText());
                    if(awardAmountInfoBean.getIndirectAnticipatedChange() < 0){
                        //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedChange());
                        lblValue.setForeground(negativeAmountFontColor);
                        //lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedChange());
                        lblTemp.setForeground(negativeAmountFontColor);
                    } else if(awardAmountInfoBean.getIndirectAnticipatedChange() != 0){
                        lblTemp.setForeground(modifiedDataFontColor);
                        lblValue.setForeground(modifiedDataFontColor);
                    }else{
                        lblTemp.setForeground(defaultFontColor);
                        lblValue.setForeground(defaultFontColor);
                    }
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    break;
                    
                    
               //#3857 -- end                
                case ANTI_AMOUNT_TOTAL:
                    coeusTextField.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedTotalAmount());
                    lblValue.setText(coeusTextField.getText());
                    lblTemp.setText(coeusTextField.getText());
                    
                    if(awardAmountInfoBean.getAnticipatedTotalAmount() < 0){
                     //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedTotalAmount());
                     lblValue.setForeground(negativeAmountFontColor);
                     //lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedTotalAmount());
                     lblTemp.setForeground(negativeAmountFontColor);
                    }else if((prevAwardAmountInfoBean == null && awardAmountInfoBean.getAnticipatedTotalAmount() >0)
                            ||(prevAwardAmountInfoBean != null 
                            && awardAmountInfoBean.getAnticipatedTotalAmount() != prevAwardAmountInfoBean.getAnticipatedTotalAmount())){
                            lblValue.setForeground(modifiedDataFontColor);
                            lblTemp.setForeground(modifiedDataFontColor);
                    }else{
                    //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedTotalAmount());
                    lblValue.setForeground(defaultFontColor);

                   // lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedTotalAmount());
                    lblTemp.setForeground(Color.BLACK);
                    }
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    break;
                case ANTI_AMOUNT_DISTRIBUTABLE:
                    coeusTextField.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedDistributableAmount());
                    lblValue.setText(coeusTextField.getText());
                    lblTemp.setText(coeusTextField.getText());
                    
                    if(awardAmountInfoBean.getAnticipatedDistributableAmount() < 0){
                       // lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedDistributableAmount());
                        lblValue.setForeground(negativeAmountFontColor);
                        lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedDistributableAmount());
                        lblTemp.setForeground(negativeAmountFontColor);

                    }else if((prevAwardAmountInfoBean == null && awardAmountInfoBean.getAnticipatedDistributableAmount() >0)
                            ||(prevAwardAmountInfoBean != null 
                            && awardAmountInfoBean.getAnticipatedDistributableAmount() != prevAwardAmountInfoBean.getAnticipatedDistributableAmount())){
                            lblValue.setForeground(modifiedDataFontColor);
                            lblTemp.setForeground(modifiedDataFontColor);
                    }else {
                        //lblValue.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedDistributableAmount());
                        lblValue.setForeground(defaultFontColor);
                        //lblTemp.setText(EMPTY_STRING+awardAmountInfoBean.getAnticipatedDistributableAmount());
                        lblTemp.setForeground(defaultFontColor);
                    }
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    break;
                case FINAL_EXP_DATE:
                    if(awardAmountInfoBean.getFinalExpirationDate()!= null){
                        String dt = awardAmountInfoBean.getFinalExpirationDate().toString();
                        lblValue.setText(dateUtils.formatDate(dt,DATE_FORMAT));
                        lblTemp.setText(dateUtils.formatDate(dt,DATE_FORMAT));
                        if((prevAwardAmountInfoBean == null && awardAmountInfoBean.getFinalExpirationDate()!=null)
                            ||(prevAwardAmountInfoBean != null 
                                && awardAmountInfoBean.getFinalExpirationDate() != null
                                && prevAwardAmountInfoBean.getFinalExpirationDate() !=null
                                && !awardAmountInfoBean.getFinalExpirationDate().equals(prevAwardAmountInfoBean.getFinalExpirationDate()))){
                            lblValue.setForeground(modifiedDataFontColor);
                            lblTemp.setForeground(modifiedDataFontColor);
                        }else{
                            lblValue.setForeground(defaultFontColor);
                            lblTemp.setForeground(defaultFontColor);
                        }
                    }else{
                        lblValue.setText(EMPTY_STRING);
                        lblTemp.setText(EMPTY_STRING);
                    }
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    break;
                case OBL_EFF_DATE:
                    if(awardAmountInfoBean.getCurrentFundEffectiveDate()!= null){
                        String dat = awardAmountInfoBean.getCurrentFundEffectiveDate().toString();
                        lblValue.setText(dateUtils.formatDate(dat,DATE_FORMAT));
                        lblTemp.setText(dateUtils.formatDate(dat,DATE_FORMAT));
                        if((prevAwardAmountInfoBean == null && awardAmountInfoBean.getCurrentFundEffectiveDate()!=null)
                            ||(prevAwardAmountInfoBean != null 
                                && awardAmountInfoBean.getCurrentFundEffectiveDate() != null
                                && prevAwardAmountInfoBean.getCurrentFundEffectiveDate() !=null
                                && !awardAmountInfoBean.getCurrentFundEffectiveDate().equals(prevAwardAmountInfoBean.getCurrentFundEffectiveDate()))){
                            lblValue.setForeground(modifiedDataFontColor);
                            lblTemp.setForeground(modifiedDataFontColor);
                        }else{
                            lblValue.setForeground(defaultFontColor);
                            lblTemp.setForeground(defaultFontColor);
                        }
                    }else{
                        lblValue.setText(EMPTY_STRING);
                        lblTemp.setText(EMPTY_STRING);
                    }
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    break;
                case OBL_EXP_DATE:
                    if(awardAmountInfoBean.getObligationExpirationDate()!= null){
                        String date = awardAmountInfoBean.getObligationExpirationDate().toString();
                        lblValue.setText(dateUtils.formatDate(date,DATE_FORMAT));
                        lblTemp.setText(dateUtils.formatDate(date,DATE_FORMAT));
                         if((prevAwardAmountInfoBean == null && awardAmountInfoBean.getObligationExpirationDate()!=null)
                            ||(prevAwardAmountInfoBean != null 
                                && awardAmountInfoBean.getObligationExpirationDate() != null
                                && prevAwardAmountInfoBean.getObligationExpirationDate() !=null
                                && !awardAmountInfoBean.getObligationExpirationDate().equals(prevAwardAmountInfoBean.getObligationExpirationDate()))){
                            lblValue.setForeground(modifiedDataFontColor);
                            lblTemp.setForeground(modifiedDataFontColor);
                        }else{
                            lblValue.setForeground(defaultFontColor);
                            lblTemp.setForeground(defaultFontColor);
                        }
                    }else{
                        lblValue.setText(EMPTY_STRING);
                        lblTemp.setText(EMPTY_STRING);
                    }
                    lblTemp.setBackground(table.getSelectionBackground());
                    lblValue.setBackground(table.getSelectionBackground());
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    break;
                case TIME:
                    if(awardAmountInfoBean.getUpdateTimestamp()!= null){
                        String time = awardAmountInfoBean.getUpdateTimestamp().toString();
                        //String tm = dateUtils.formatTime(time);
//                    lblValue.setText(dateUtils.formatTime(time));
                        lblValue.setText( CoeusDateFormat.format(time));
//                        lblTemp.setText(dateUtils.formatTime(time));
                        lblTemp.setText( CoeusDateFormat.format(time));
                    }else{
                        lblValue.setText(EMPTY_STRING);
                        lblTemp.setText(EMPTY_STRING);
                    }
                    lblValue.setForeground(defaultFontColor);
                    lblTemp.setForeground(defaultFontColor);
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    break;
                case USER:
                    if(awardAmountInfoBean.getUpdateUser()!= null){
                        /*
                         * UserId to UserName Enhancement - Start
                         * Added to avoid the repetetive database interaction for getting 
                         * the username instead of going to the database everytime mainting the values locally in HashMap
                         */
                        //lblValue.setText(awardAmountInfoBean.getUpdateUser());
                        //lblTemp.setText(awardAmountInfoBean.getUpdateUser());
                        lblValue.setText(getUpdatedUser(awardAmountInfoBean.getUpdateUser()));
                        lblTemp.setText(getUpdatedUser(awardAmountInfoBean.getUpdateUser()));
                        //UserId to UserName Enhancement End.
                    }else{
                        lblValue.setText(EMPTY_STRING);
                        lblTemp.setText(EMPTY_STRING);
                    }
                    lblValue.setForeground(defaultFontColor);
                    lblTemp.setForeground(defaultFontColor);
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    lblTemp.setBorder(new EmptyBorder(0,10,0,0));
                    lblValue.setBorder(new EmptyBorder(0,10,0,0));
                    break;
                 //Modified for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
                
                //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
                case TRANSACTION_TYPE_COL:
                    if(awardAmountTransacionBean != null){
                        lblValue.setText(awardAmountTransacionBean.getTransactionTypeDescription());
                        lblTemp.setText(awardAmountTransacionBean.getTransactionTypeDescription());
                    }else{
                        lblValue.setText(EMPTY_STRING);
                        lblTemp.setText(EMPTY_STRING);
                    }
                    lblValue.setForeground(defaultFontColor);
                    lblTemp.setForeground(defaultFontColor);
                    lblValue.setHorizontalAlignment(LEFT);
                    lblTemp.setHorizontalAlignment(LEFT);
                    lblTemp.setBorder(new EmptyBorder(0,10,0,0));
                    lblValue.setBorder(new EmptyBorder(0,10,0,0));
                    break;
                case NOTICE_DATE_COL:
                    if(awardAmountTransacionBean != null && awardAmountTransacionBean.getNoticeDate() !=null){
                        String date = awardAmountTransacionBean.getNoticeDate().toString();
                        lblValue.setText(dateUtils.formatDate(date,DATE_FORMAT));
                        lblTemp.setText(dateUtils.formatDate(date,DATE_FORMAT));
                    }else{
                        lblValue.setText(EMPTY_STRING);
                        lblTemp.setText(EMPTY_STRING);
                    }
                    if((prevAwardAmountTransacionBean == null 
                            && awardAmountTransacionBean !=null 
                            && awardAmountTransacionBean.getNoticeDate() != null)
                            
                            ||(prevAwardAmountTransacionBean != null 
                            && awardAmountTransacionBean !=null 
                            && prevAwardAmountTransacionBean.getNoticeDate() == null 
                            && awardAmountTransacionBean.getNoticeDate() != null)
                            
                            ||(prevAwardAmountTransacionBean !=null
                            && awardAmountTransacionBean!=null
                            && prevAwardAmountTransacionBean.getNoticeDate() !=null
                            && awardAmountTransacionBean.getNoticeDate() != null
                            && !prevAwardAmountTransacionBean.getNoticeDate().equals(awardAmountTransacionBean.getNoticeDate()))){
                        lblTemp.setForeground(modifiedDataFontColor);
                        lblValue.setForeground(modifiedDataFontColor);
                    }else{
                        lblTemp.setForeground(defaultFontColor);
                        lblValue.setForeground(defaultFontColor);
                    }
                    lblValue.setHorizontalAlignment(RIGHT);
                    lblTemp.setHorizontalAlignment(RIGHT);
                    lblTemp.setBorder(new EmptyBorder(0,0,0,0));
                    lblValue.setBorder(new EmptyBorder(0,0,0,0));
                    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
            }
            //Modified for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
            if(isSelected) {
                lblValue.setBackground(selectedRowBackgroundColor);
                lblTemp.setBackground(selectedRowBackgroundColor);
            }else {
                lblValue.setBackground(defaultRowBackgroundColor);
                lblTemp.setBackground(defaultRowBackgroundColor);
            }
            if(newGroup && col == COMMENTS_COL){
                if(value != null && !value.toString().equals(EMPTY_STRING)){
                    return pnlButtonComments;
                }else{
                    return pnlButtonNoComments;
                }
            }else if(col == COMMENTS_COL){
                if(value != null && !value.toString().equals(EMPTY_STRING)){
                    return btnCommentsPresent;
                }else{
                    return btnCommentsNotPresent;
                }
            }
            //Modifed for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
            else if( newGroup ){
                return pnlGroup;
            }
            return lblTemp;
        }
    }
    
    /*
     * UserId to UserName Enhancement - Start
     * getUpdatedUser(String) method added to avoid the repetetive database
     * interaction for getting the username by
     * mainting the values locally in HashMap
     */
    public String getUpdatedUser(String strUserId) {
        String userName = "";
        if(objMap.containsKey(strUserId)) {
            userName =objMap.get(strUserId).toString();
        }
        else {
            userName = UserUtils.getDisplayName(strUserId);
            objMap.put(strUserId, userName);
        }
        return userName;
    }

    //UserId to UserName Enhancement - End
    

    class HistoryHeaderRenderer extends DefaultTableCellRenderer
    implements TableCellRenderer {
        
        private JLabel label;
        HistoryHeaderRenderer(){
            label = new JLabel();
            //label.setHorizontalAlignment(JLabel.RIGHT);
            label.setFont(CoeusFontFactory.getLabelFont());
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            label.setText(value.toString());
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
            if(column == TRANSACTION_TYPE_COL || column == USER){
                label.setBorder(new EmptyBorder(0,10,0,0));
                label.setHorizontalAlignment(JLabel.LEFT);                
            }else{
                label.setBorder(null);
                label.setHorizontalAlignment(JLabel.RIGHT);
            }
            //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
            return label;
        }
        
    }
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
    /**
     * This class is used as the editor for the history table
     */
    class HistoryTableEditor extends DefaultCellEditor implements ActionListener{
        private boolean newGroup;
        private JButton btnPanelCommentsPresent,btnCommentsPresent;
        private JButton btnPanelCommentsNotPresent,btnCommentsNotPresent;
        private AwardAmountTransactionBean awardAmountTransacionBean;
        private JPanel pnlButtonComments, pnlButtonNoComments;
        private JLabel lblInvisible;
        
        private edu.mit.coeus.utils.CommentsForm commentsForm;
        private AwardAmountInfoBean awardAmountInfoBean;
        int sequenceNo;
        public HistoryTableEditor(){
            super(new JComboBox());
            btnCommentsPresent = new JButton(imgIcnJustified);
            btnCommentsNotPresent = new JButton(imgIcnNotJustified);
            btnPanelCommentsPresent = new JButton(imgIcnJustified);
            btnPanelCommentsNotPresent = new JButton(imgIcnNotJustified);
            
            pnlButtonComments = new JPanel();
            pnlButtonComments.setOpaque(true);
            pnlButtonComments.setLayout(new GridLayout(2, 1));
            lblInvisible = new JLabel();
            lblInvisible.setOpaque(true);
            pnlButtonComments.add(lblInvisible);
            pnlButtonComments.add(btnPanelCommentsPresent); 
            
            pnlButtonNoComments = new JPanel();
            pnlButtonNoComments.setOpaque(true);
            pnlButtonNoComments.setLayout(new GridLayout(2, 1));
            lblInvisible = new JLabel();
            lblInvisible.setOpaque(true);
            pnlButtonNoComments.add(lblInvisible);
            pnlButtonNoComments.add(btnPanelCommentsNotPresent); 
            commentsForm = new edu.mit.coeus.utils.CommentsForm("Comments");
            
            btnCommentsPresent.addActionListener(this);
            btnCommentsNotPresent.addActionListener(this);
            btnPanelCommentsPresent.addActionListener(this);
            btnPanelCommentsNotPresent.addActionListener(this);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
             if( row > 0 ){
                awardAmountInfoBean = (AwardAmountInfoBean)cvHistory.get(row - 1);
                sequenceNo = awardAmountInfoBean.getSequenceNumber();
            }else {
                sequenceNo = -1;
            }
            
            awardAmountInfoBean = (AwardAmountInfoBean)cvHistory.get(row);
            if( sequenceNo != awardAmountInfoBean.getSequenceNumber()){
                newGroup = true;
            }else {
                newGroup = false;
            }
            if(newGroup && column == COMMENTS_COL){
                if(value != null && !value.toString().equals(EMPTY)){
                    return pnlButtonComments;
                }else{
                    return pnlButtonNoComments;
                }
            }else if(column == COMMENTS_COL){
                if(value != null && !value.toString().equals(EMPTY)){
                    return btnCommentsPresent;
                }else{
                    return btnCommentsNotPresent;
                }
            }
            return btnCommentsPresent;
        }
        public void actionPerformed(ActionEvent e){
            Object source = e.getSource();
            if(source.equals(btnCommentsNotPresent) 
                || source.equals(btnCommentsPresent)
                || source.equals(btnPanelCommentsNotPresent)
                || source.equals(btnPanelCommentsPresent)){
                
                int selectedRow = awardMoneyAndEndDatesHistoryForm.tblHistory.getSelectedRow();
                if(selectedRow != -1){
                    AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvHistory.get(selectedRow);
                    if(awardAmountInfoBean.getAwardAmountTransaction() != null){
                        commentsForm.setData(awardAmountInfoBean.getAwardAmountTransaction().getComments());
                        commentsForm.display();
                        awardMoneyAndEndDatesHistoryForm.tblHistory.getCellEditor().stopCellEditing();
                    }
                }
            }
        }
    }
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    
    //#3857 -- start
    private void initSettings() {
        if(AwardAmountTreeTable.SET_DITRECT_INDIRECT) {
            
            awardMoneyAndEndDatesHistoryForm.tblHistory.getColumnModel().moveColumn(15,3);
            awardMoneyAndEndDatesHistoryForm.tblHistory.getColumnModel().moveColumn(16,4);
            awardMoneyAndEndDatesHistoryForm.tblHistory.getColumnModel().moveColumn(17,8);
            awardMoneyAndEndDatesHistoryForm.tblHistory.getColumnModel().moveColumn(18,9);
            
            visibleColumns = new int[19];
            visibleColumns[0] = 0;
            visibleColumns[1] = 1;
            visibleColumns[2] = 2;
            visibleColumns[3] = 15;
            visibleColumns[4] = 16;
            visibleColumns[5] = 3;
            visibleColumns[6] = 4;
            visibleColumns[7] = 5;
            visibleColumns[8] = 17;
            visibleColumns[9] = 18;
            visibleColumns[10] = 6;
            visibleColumns[11] = 7;
            visibleColumns[12] = 8;
            visibleColumns[13] = 9;
            visibleColumns[14] = 10;
            visibleColumns[15] = 11;
            visibleColumns[16] = 12;
            visibleColumns[17] = 13;
            visibleColumns[18] = 14;
            
        }
    }
    // #3857 -- end
}
