/*
 * AwardMoneyAndEndDatesActionSummaryController.java
 *
 * Created on October 3, 2008, 6:33 PM
 *
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardAmountInfoBean;
import edu.mit.coeus.award.bean.AwardAmountTransactionBean;
import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardTransactionInfoBean;
import edu.mit.coeus.award.gui.AwardAmountTreeTable;
import edu.mit.coeus.award.gui.AwardMoneyAndEndDatesActionSummary;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.gui.event.BeanUpdatedListener;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.FormattedDocument;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author manjunathbr
 */
public class AwardMoneyAndEndDatesActionSummaryController extends AwardController
        implements ActionListener,BeanUpdatedListener,AdjustmentListener {
    
    private AwardMoneyAndEndDatesActionSummary awardMoneyAndEndDatesActionSummary = null;
    
    private DateUtils dateUtils = null;
    
    private CoeusVector cvAccountActionSummary = null;
    
    private CoeusVector cvTraversalBeans = null;
    
    private ActionSummaryTableModel actionSummaryTableModel = null;
    
    private ActionSummaryRenderer actionSummaryRenderer = null;
    
    private CoeusDlgWindow dlgAwardActionSummary = null;
    
    private CoeusAppletMDIForm mdiForm = null;
    
    private JScrollBar tableScrollBar = null;
    
    private  ActionSummaryHeaderRenderer  actionSummaryHeaderRenderer = null;
    
    private int index;
    
    private final int WIDTH = 750;
    
    private final int HEIGHT = 540;
    
    private final int ROW_HEIGHT = 20;
    
    private final int NEW_ROW_HEIGHT = 40;
    
    private static final int SERIAL_NUMBER_COLUMN = 0;
    private final int AWARD_DATE_COLUMN = 1;
    private final int AWARD_ACTION_COLUMN = 2;
    private final int START_DATE_COLUMN = 3;
    private final int END_DATE_COLUMN = 4;
    private final int DIRECT_COLUMN = 6;
    private final int INDIRECT_COLUMN = 7;
    private final int TOTAL_COLUMN = 5;
    
    private static final String EMPTY_STRING = "";
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    private static final char GET_AWARD_ACTION_SUMMARY = 'a';
    
    private static final String AWARD_SERVLET = "/AwardMaintenanceServlet";
    
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL +
            AWARD_SERVLET;
    
      private int visibleColumns[] ;
    
    /** Creates a new instance of AwardMoneyAndEndDatesActionSummaryController */
    public AwardMoneyAndEndDatesActionSummaryController(AwardBaseBean awardBaseBean) {
        
        super(awardBaseBean);
        cvAccountActionSummary = new CoeusVector();
        dateUtils = new DateUtils();
        mdiForm =   CoeusGuiConstants.getMDIForm();
        awardMoneyAndEndDatesActionSummary = new AwardMoneyAndEndDatesActionSummary();
        actionSummaryRenderer = new ActionSummaryRenderer();
        actionSummaryHeaderRenderer = new ActionSummaryHeaderRenderer();
        postInitComponents();
        registerComponents();
        setTableEditors();
        registerEvents();
     
    }
    
    public void registerEvents() {
        addBeanUpdatedListener(this, AwardAmountInfoBean.class);
    }
    
    public Component getControlledUI() {
        return awardMoneyAndEndDatesActionSummary;
    }
    
    public void setFormData(Object data) {
        if(index > 0) {
            awardMoneyAndEndDatesActionSummary.btnPrevious.setEnabled(true);
        }else {
            awardMoneyAndEndDatesActionSummary.btnPrevious.setEnabled(false);
        }
        if(index == cvTraversalBeans.size() - 1) {
            awardMoneyAndEndDatesActionSummary.btnNext.setEnabled(false);
        }else {
            awardMoneyAndEndDatesActionSummary.btnNext.setEnabled(true);
        }
        awardBaseBean = (AwardBaseBean)data;
        
        try{
            CoeusVector cvActionSummary = getDataFromServer();
            if(cvActionSummary != null) {
                for( int index = 0; index < cvActionSummary.size(); index++ ){
                    AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvActionSummary.get(index);
                }             
              
            }
              actionSummaryTableModel.setData(cvActionSummary);
              actionSummaryTableModel.fireTableDataChanged();
              
            //moneyAndEndDatesForm.awardAmountTreeTable.getSelectedBean().getMitAwardNumber()
            dlgAwardActionSummary.setTitle("Money And End Dates Action Summary for Award- "+awardBaseBean.getMitAwardNumber());
        }catch (CoeusClientException ex){
            CoeusOptionPane.showDialog(ex);
            ex.printStackTrace();
        }
        
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        dlgAwardActionSummary.setCursor( new Cursor(Cursor.WAIT_CURSOR));
        Object source = actionEvent.getSource();
        if(source.equals( awardMoneyAndEndDatesActionSummary.btnClose)){
            dlgAwardActionSummary.dispose();
        } else if(source.equals( awardMoneyAndEndDatesActionSummary.btnPrevious)) {
            index = index - 1;
            AwardBaseBean awardBaseBean = (AwardBaseBean)cvTraversalBeans.get(index);
            setFormData(awardBaseBean);
        }else if(source.equals( awardMoneyAndEndDatesActionSummary.btnNext)) {
            index = index + 1;
            AwardBaseBean awardBaseBean = (AwardBaseBean)cvTraversalBeans.get(index);
            setFormData(awardBaseBean);
        }
        dlgAwardActionSummary.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void formatFields() {
    }
    
    public boolean validate() throws CoeusUIException {
        return true;
    }
    
    public void registerComponents() {
        actionSummaryTableModel = new ActionSummaryTableModel();
        awardMoneyAndEndDatesActionSummary.tblActionSummary.setModel(actionSummaryTableModel);
        awardMoneyAndEndDatesActionSummary.btnPrevious.addActionListener(this);
        awardMoneyAndEndDatesActionSummary.btnNext.addActionListener(this);
        awardMoneyAndEndDatesActionSummary.btnClose.addActionListener(this);
        awardMoneyAndEndDatesActionSummary.tblActionSummary.setRowHeight(ROW_HEIGHT);
        awardMoneyAndEndDatesActionSummary.lblHeader1.setText("Award Year Breakdown");
        awardMoneyAndEndDatesActionSummary.lblHeader1.setFont(CoeusFontFactory.getLabelFont());
        
        tableScrollBar = awardMoneyAndEndDatesActionSummary.srcPnTable.getHorizontalScrollBar();
        tableScrollBar.addAdjustmentListener(this);
        
    }
    
    public void saveFormData() throws CoeusException {
    }
    
    public void display() {
        dlgAwardActionSummary.setVisible(true);
    }
    
    
    public void beanUpdated(BeanEvent beanEvent) {
        if(beanEvent.getSource().getClass().equals(AwardTransactionDetailsController.class)) {
            if(beanEvent.getBean().getClass().equals(AwardAmountInfoBean.class)) {
                index=beanEvent.getMessageId();
                if( awardMoneyAndEndDatesActionSummary.tblActionSummary.getRowCount()>index){
                    awardMoneyAndEndDatesActionSummary.tblActionSummary.setRowSelectionInterval(index,index);
                }
                awardMoneyAndEndDatesActionSummary.tblActionSummary.scrollRectToVisible(
                        awardMoneyAndEndDatesActionSummary.tblActionSummary.getCellRect(
                        index ,0, true));
            }
        }
    }
    
    public void adjustmentValueChanged(AdjustmentEvent e) {
    }
    
    public void setTraversalData(CoeusVector cvTraversalBeans, AwardBaseBean selectedBean, int index){
        this.cvTraversalBeans = cvTraversalBeans;
        this.index = index;
        setFormData(selectedBean);
    }
    
    private void requestDefaultFocus(){
        awardMoneyAndEndDatesActionSummary.btnClose.requestFocusInWindow();
    }
   
    private void postInitComponents(){
        dlgAwardActionSummary = new CoeusDlgWindow(mdiForm);
        dlgAwardActionSummary.setResizable(false);
        dlgAwardActionSummary.setModal(true);
        dlgAwardActionSummary.getContentPane().add(awardMoneyAndEndDatesActionSummary);
        dlgAwardActionSummary.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardActionSummary.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
      // dlgAwardActionSummary.setSize(WIDTH, HEIGHT);
        dlgAwardActionSummary.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardActionSummary.getSize();
        dlgAwardActionSummary.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        
        dlgAwardActionSummary.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgAwardActionSummary.dispose();
            }
        });
        dlgAwardActionSummary.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
        dlgAwardActionSummary.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardActionSummary.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                dlgAwardActionSummary.dispose();
            }
        });
     
    }
    
    public void setTableEditors(){
        JTableHeader tableHeader =  awardMoneyAndEndDatesActionSummary.tblActionSummary.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
       
        awardMoneyAndEndDatesActionSummary.tblActionSummary.setOpaque(false);
        awardMoneyAndEndDatesActionSummary.tblActionSummary.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        awardMoneyAndEndDatesActionSummary.tblActionSummary.setShowVerticalLines(false);
        awardMoneyAndEndDatesActionSummary.tblActionSummary.setShowHorizontalLines(false);
        awardMoneyAndEndDatesActionSummary.tblActionSummary.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        awardMoneyAndEndDatesActionSummary.tblActionSummary.setIntercellSpacing(new java.awt.Dimension(0,0));
        awardMoneyAndEndDatesActionSummary.tblActionSummary.setRowHeight(25);
        int colSize[] ;
        
        if(AwardAmountTreeTable.SET_DITRECT_INDIRECT) {
            colSize =new int[]{20,100, 100, 100, 100, 100, 100,100};
            visibleColumns = new int[colSize.length];
            awardMoneyAndEndDatesActionSummary.tblActionSummary.getColumnModel().moveColumn(5,7);
            visibleColumns[5] = DIRECT_COLUMN;
            visibleColumns[6] = INDIRECT_COLUMN;
            visibleColumns[7] = TOTAL_COLUMN;
        } else {
            colSize =new int[]{20,100, 100, 100, 100, 100};
            visibleColumns = new int[colSize.length];
        }
        
        
        for(int index = 0;index<colSize.length;index++) {
            visibleColumns[index] = index;
        }
    
        for(int col = 0; col < colSize.length; col++) {
            TableColumn tableColumn = awardMoneyAndEndDatesActionSummary.tblActionSummary.getColumnModel().getColumn(col);
            tableColumn.setPreferredWidth(colSize[col]);
            tableColumn.setCellRenderer(actionSummaryRenderer);
        }
        for ( int index = 0; index < actionSummaryTableModel.getColumnCount(); index ++){
            TableColumn column = awardMoneyAndEndDatesActionSummary.tblActionSummary.getColumnModel().getColumn(index);
            column.setHeaderRenderer(actionSummaryHeaderRenderer);
        }        
    }
    
    
    public CoeusVector getDataFromServer() throws CoeusClientException{
        
        cvAccountActionSummary = new CoeusVector();
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_AWARD_ACTION_SUMMARY);
        requesterBean.setDataObject(awardBaseBean.getMitAwardNumber());
        AppletServletCommunicator comm= new AppletServletCommunicator(connectTo,
                requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean.isSuccessfulResponse()) {
            cvAccountActionSummary = (CoeusVector)responderBean.getDataObject();
            return cvAccountActionSummary;
        }else {
            throw new CoeusClientException(responderBean.getMessage(), CoeusClientException.ERROR_MESSAGE);
        }
        
    }
    
    class ActionSummaryTableModel extends AbstractTableModel{
        private  String colNames[];
        
        private  Class[] colTypes ;
        
        public ActionSummaryTableModel() {
            initSettings();
        }
        
        public int getRowCount() {
            if(cvAccountActionSummary == null ){
                return 0;
            }else{
                return cvAccountActionSummary.size();
            }
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            AwardAmountInfoBean bean = (AwardAmountInfoBean)cvAccountActionSummary.get(rowIndex);
            AwardAmountTransactionBean awardAmountTransactionBean = bean.getAwardAmountTransaction();
            
            String data = "";
            
            switch(columnIndex){
                case SERIAL_NUMBER_COLUMN:
                    data = String.valueOf(rowIndex+1);
                    break;
                case TOTAL_COLUMN:
                    //Modified for Case#4559 - Action Summary / Award Year Breakdown is displaying the wrong Total Amount  - Start
                    //data = new Double(bean.getAmountObligatedToDate()).toString();
                    data = new Double(bean.getObligatedChange()).toString();
                    //Case#4559 - End
                  
                    break;
                case AWARD_DATE_COLUMN:
                    if(awardAmountTransactionBean != null && awardAmountTransactionBean.getNoticeDate() != null){
                        data = awardAmountTransactionBean.getNoticeDate().toString();
                    }
                    break;
                case START_DATE_COLUMN:
                    System.out.println("Start date  "+bean.getCurrentFundEffectiveDate());
                    if(bean.getCurrentFundEffectiveDate()!= null){
                        data = bean.getCurrentFundEffectiveDate().toString();
                    }
                    break;
                case END_DATE_COLUMN:
                    
                    if(bean.getObligationExpirationDate()!= null){
                        data = bean.getObligationExpirationDate().toString();
                    }
                    break;
                case AWARD_ACTION_COLUMN:
                    if(awardAmountTransactionBean != null){
                        data = awardAmountTransactionBean.getTransactionTypeDescription()==null?
                            "" :awardAmountTransactionBean.getTransactionTypeDescription();
                    }
                    break;
                case DIRECT_COLUMN:
                    data = new Double(bean.getDirectObligatedChange()).toString();
                    break;
                case INDIRECT_COLUMN:
                    data =  new Double(bean.getIndirectObligatedChange()).toString();
                    break;
            }
            return data;
        }
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public void setData(CoeusVector cvAccountActionSummary){
            cvAccountActionSummary = cvAccountActionSummary;
        }

        private void initSettings() {
            if(AwardAmountTreeTable.SET_DITRECT_INDIRECT) {
                colNames = new String[]{"","Award Date", "Award Action", "Start Date", "End Date",  "Total","Direct","Indirect"};
                colTypes = new Class [] {String.class,String.class, String.class, String.class , String.class,String.class,
                String.class, String.class};
            } else {
                colNames = new String[]{"","Award Date", "Award Action", "Start Date", "End Date", "Total"};
                colTypes = new Class [] {String.class,String.class, String.class, String.class , String.class,String.class};
            }
        }
    }
    
    class ActionSummaryRenderer  extends DefaultTableCellRenderer   implements TableCellRenderer {
      
        private JLabel lblValue = null;
        private final Color selectedRowBackgroundColor = Color.YELLOW;
        private final Color defaultRowBackgroundColor = (Color) UIManager.getDefaults().get("Panel.background");
        private CoeusTextField coeusTextField;
        
        ActionSummaryRenderer() {
          
            lblValue = new JLabel();
            lblValue.setOpaque(true);
            lblValue.setHorizontalAlignment(RIGHT);
            lblValue.setBorder(new EmptyBorder(0,0,0,0));
            
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
            
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col){
           
            String tableData =  value.toString();
            if(AwardAmountTreeTable.SET_DITRECT_INDIRECT) {
                col = visibleColumns[col];
            }
            switch(col){
                  case AWARD_ACTION_COLUMN:
                    lblValue.setText("   "+tableData);  
                    break;
                case SERIAL_NUMBER_COLUMN:
                    lblValue.setText(tableData);   
                    break;
                case AWARD_DATE_COLUMN:
                    if(tableData.length() >0) {
                        lblValue.setText(dateUtils.formatDate(tableData,DATE_FORMAT));
                    }else{
                        lblValue.setText(tableData);
                    }
                    break;              
                case START_DATE_COLUMN:
                    
                    if(tableData.length() >0) {
                        lblValue.setText(dateUtils.formatDate(tableData,DATE_FORMAT));
                    }else{
                        lblValue.setText(tableData);
                    }
                      break;
                case END_DATE_COLUMN:
                    if(tableData.length() >0) {
                        lblValue.setText(dateUtils.formatDate(tableData,DATE_FORMAT));
                    }else{
                        lblValue.setText(tableData);
                    }
                    break;
                case DIRECT_COLUMN:
                    coeusTextField.setText(EMPTY_STRING+tableData);
                    lblValue.setText(coeusTextField.getText());
                    break;
                case INDIRECT_COLUMN:
                    coeusTextField.setText(EMPTY_STRING+tableData);
                    lblValue.setText(coeusTextField.getText());
                    break;
                case TOTAL_COLUMN:
                    coeusTextField.setText(EMPTY_STRING+tableData);
                    lblValue.setText(coeusTextField.getText());
                    
                    break;
            }
              if(col == AWARD_ACTION_COLUMN){
                
                lblValue.setHorizontalAlignment(JLabel.LEFT);                
            }else{
             
                lblValue.setHorizontalAlignment(JLabel.RIGHT);
            }
            if(isSelected) {
                lblValue.setBackground(selectedRowBackgroundColor);
            }else {
                lblValue.setBackground(defaultRowBackgroundColor);
            }
           
            return lblValue;
        }
    }
    
    
    class ActionSummaryHeaderRenderer extends DefaultTableCellRenderer
            implements TableCellRenderer {
        
        private JLabel label;
        
        ActionSummaryHeaderRenderer(){
            label = new JLabel();
            label.setFont(CoeusFontFactory.getLabelFont());
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
             if(column == AWARD_ACTION_COLUMN){
                label.setBorder(new EmptyBorder(0,10,0,0));
                label.setHorizontalAlignment(JLabel.LEFT);                
            }else{
                label.setBorder(null);
                label.setHorizontalAlignment(JLabel.RIGHT);
            }
            label.setText(value.toString());
           
            return label;
        }
    }
}
