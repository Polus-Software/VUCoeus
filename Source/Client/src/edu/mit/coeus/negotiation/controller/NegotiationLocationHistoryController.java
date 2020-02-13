/*
 * NegotiationLocationHistoryController.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.negotiation.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.negotiation.bean.NegotiationBaseBean;
import edu.mit.coeus.negotiation.bean.NegotiationLocationBean;
import edu.mit.coeus.negotiation.gui.NegotiationLocationHistory;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observer;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;


public class NegotiationLocationHistoryController extends NegotiationController implements ActionListener{
    
    private NegotiationLocationHistory negotiationLocationHistory;
    private static final char GET_NEGOTIATION_LOCATION_HISTORY = 'S';
    private static final String SERVLET = "/NegotiationMaintenanceServlet";
    final CoeusDlgWindow coeusDlgWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm());
    //Added for case:4185 - Effective Date should be editable - Start 
    private static final String ERRKEY_EMPTY_EFF_DATE  = "negotiationDetail_exceptionCode.1110";
    private static final String ERRKEY_INVALID_EFF_DATE= "negotiationDetail_exceptionCode.1119";
    private static final String ERRKEY_EFF_DATE_AFTER  = "negotiationDetail_exceptionCode.1200";
    private static final String ERRKEY_EFF_DATE_BEFORE = "negotiationDetail_exceptionCode.1201";
    private static final String PROMPT_SAVE_CHANGES    = "saveConfirmCode.1002";
    private static final char SAVE_LOCATION_HISTORY    = 'L';
    private static final int EFF_DATE_COL              =  2;
    private SimpleDateFormat timeStampFormat;
    private SimpleDateFormat simpleDateFormat;
    private CoeusMessageResources coeusMessageResources;
    private DateUtils dateUtils;
    private  NegotiationLocHistoryEditor negotiationLocHistoryEditor;
    private BaseWindowObservable observable = new BaseWindowObservable();
    private CoeusVector cvUpdateData = new CoeusVector();
    //Added for case:4185 - Effective Date should be editable - End
    
    /** Creates a new instance of NegotiationLocationHistoryController */
    public NegotiationLocationHistoryController(NegotiationBaseBean negotiationBaseBean) {
        this.negotiationBaseBean = negotiationBaseBean;
        negotiationLocationHistory = new NegotiationLocationHistory();
        //Added for case:4185 - Effective Date should be editable - Start
        timeStampFormat     = new SimpleDateFormat(CoeusGuiConstants.TIMESTAMP_FORMAT);
        simpleDateFormat    = new SimpleDateFormat(CoeusGuiConstants.DEFAULT_DATE_FORMAT);
        negotiationLocationHistory      = new NegotiationLocationHistory();
        negotiationLocHistoryEditor     = new NegotiationLocHistoryEditor();
        coeusMessageResources           = CoeusMessageResources.getInstance();
        dateUtils = new DateUtils();
        //Added for case:4185 - Effective Date should be editable - End
        registerComponents();
    }
    
    public void display() {
        try{
            setFormData(negotiationBaseBean);
            
            coeusDlgWindow.setResizable(false);
            coeusDlgWindow.setModal(true);
            coeusDlgWindow.getContentPane().add(negotiationLocationHistory);
            coeusDlgWindow.setTitle("Location History - " + negotiationBaseBean.getNegotiationNumber());
            coeusDlgWindow.setFont(CoeusFontFactory.getLabelFont());
            coeusDlgWindow.setSize(700, 350);
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dlgSize = coeusDlgWindow.getSize();
            coeusDlgWindow.setLocation(screenSize.width/2 - (dlgSize.width/2), screenSize.height/2 - (dlgSize.height/2));
            //Added for case:4185 - Effective Date should be editable - Start
            coeusDlgWindow.addEscapeKeyListener(new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    performCancelAction();
                    return;
                }
            });
            
            coeusDlgWindow.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    performCancelAction();
                }
            });
            coeusDlgWindow.addComponentListener(
                    new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
            });
//            //Added for case:4185 - Effective Date should be editable - End
 
            coeusDlgWindow.setVisible(true);
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    public void cleanUp() {
    }
       
    public void setFormData(Object data) throws CoeusException {
        //write code for filling this GUI
        CoeusVector cvlocationHistory = (CoeusVector)getFormData();
        if(cvlocationHistory != null && cvlocationHistory.size()>0) {
            JTable history = negotiationLocationHistory.tblNegLocHistory;
            history.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            history.setModel(new LocationHistoryTableModel(cvlocationHistory));
            int colWidth[] = {85, 110, 100, 80, 130, 90};
            //Added for case:4185 - Effective Date should be editable - Start
            history.setRowSelectionInterval(0,0);
            TableColumn column=null;
            NegotiationLocHistoryRenderer negotiationLocHistoryRenderer = new NegotiationLocHistoryRenderer();
            for(int index=0; index <colWidth.length; index++) {
                column = history.getColumnModel().getColumn(index);
                column.setPreferredWidth(colWidth[index]);
                column.setCellRenderer(negotiationLocHistoryRenderer);
                column.setCellEditor(negotiationLocHistoryEditor);
            }
            
            if(getFunctionType()==TypeConstants.DISPLAY_MODE){
                negotiationLocationHistory.btnOk.setEnabled(false);
            }
            //Added for case:4185 - Effective Date should be editable - End
            history.setSelectionBackground((Color)UIManager.getDefaults().get("Table.selectionBackground"));
            history.setSelectionForeground(Color.white);
            history.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("TabbedPane.tabAreaBackground"));
            history.setRowHeight(25);
            history.getTableHeader().setReorderingAllowed(false);
            history.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        }
    }
    
    public boolean validate() throws CoeusUIException {
        return true;
    }
    
    
    //New methods Added for case:4185 - Effective Date should be editable - Start
    
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    public void requestDefaultFocus(){
        if(getFunctionType()==TypeConstants.DISPLAY_MODE){
            negotiationLocationHistory.btnCancel.requestFocusInWindow();
        }else{
            negotiationLocationHistory.btnOk.requestFocusInWindow();
        }
    }

    public void registerComponents() {
        negotiationLocationHistory.btnOk.addActionListener(this);
        negotiationLocationHistory.btnCancel.addActionListener(this);
        negotiationLocationHistory.btnOk.requestFocusInWindow();
        Component[] comp = {negotiationLocationHistory.btnOk,negotiationLocationHistory.btnCancel,negotiationLocationHistory.tblNegLocHistory};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        negotiationLocationHistory.setFocusTraversalPolicy(traversal);
        negotiationLocationHistory.setFocusCycleRoot(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source=e.getSource();
        coeusDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(source.equals(negotiationLocationHistory.btnCancel)) {
            performCancelAction();
        }else if(source.equals(negotiationLocationHistory.btnOk)) {
            performOkAction();
        }
        coeusDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        
    }
    
    private void performCancelAction() {
        
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            coeusDlgWindow.dispose();
            return;
        }
        
        setSaveRequired(true);
        negotiationLocHistoryEditor.stopCellEditing();
        if(!isSaveRequired() || cvUpdateData.size()>0){
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(PROMPT_SAVE_CHANGES),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
            if( option == JOptionPane.YES_OPTION ) {
                saveFormData();
                coeusDlgWindow.dispose();
            }else if( option == JOptionPane.NO_OPTION ) {
                coeusDlgWindow.dispose();
            }
        }else{
            coeusDlgWindow.dispose();
        }
    }
    
    
    private void performOkAction() {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            coeusDlgWindow.dispose();
            return;
        }
        setSaveRequired(true);
        negotiationLocHistoryEditor.stopCellEditing();
        if(isSaveRequired()){
            if(cvUpdateData.size()>0){
                saveFormData();
                coeusDlgWindow.dispose();
            }else{
                coeusDlgWindow.dispose();
            }
        }
    }
    
    public void saveFormData() {
        
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(SAVE_LOCATION_HISTORY);
        Vector vecData =  new Vector();
        vecData.add(negotiationBaseBean.getNegotiationNumber());
        vecData.add(cvUpdateData);
        requesterBean.setDataObjects(vecData);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
        }else{
            CoeusVector cvNegotiationLocaton = (CoeusVector)responderBean.getDataObject();
            if(cvNegotiationLocaton!=null && !cvNegotiationLocaton.isEmpty()){//notify parent screen
                observable.notifyObservers( cvNegotiationLocaton.get(0) );
            }
        }
    }
    
    //New Methods Added for case:4185 - Effective Date should be editable - End
    
    public Object getFormData() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_NEGOTIATION_LOCATION_HISTORY);
        
        requesterBean.setDataObject(negotiationBaseBean.getNegotiationNumber());
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return null;
        }
        return responderBean.getDataObjects();
    }
    
    public Component getControlledUI() {
        return negotiationLocationHistory;
    }
    
    public void formatFields() {
    }
    
    
    
    
    private class LocationHistoryTableModel extends AbstractTableModel{
        
        private String colNames[] = {"Location No", "Location Name", "Effective Date", "No of Days", "Update Timestamp", "Update User"};
        private CoeusVector locationHistory;
        private Object retValue;
        private Date effectiveDateNext, effectiveDate;
        private DateUtils dateUtils;
        private NegotiationLocationBean negotiationLocationBean;
        
        LocationHistoryTableModel(CoeusVector locationHistory) {
            this.locationHistory = locationHistory;
            dateUtils = new DateUtils();
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Object getValueAt(int row, int column) {
            
            negotiationLocationBean = (NegotiationLocationBean)locationHistory.get(row);
            
            switch (column) {
                case 0:
                    retValue = new Integer(negotiationLocationBean.getLocationNumber());
                    break;
                case 1:
                    retValue = negotiationLocationBean.getNegotiationLocationTypeDes();
                    break;
                case 2:
                    //  retValue = reqDateFormat.format(negotiationLocationBean.getEffectiveDate());
                    retValue = negotiationLocationBean.getEffectiveDate();
                    break;
                case 3:
                    retValue = new Integer(10);
                    effectiveDate = negotiationLocationBean.getEffectiveDate();
                    if(row == (getRowCount()-1)) {
                        effectiveDateNext = new Date();
                    }else {
                        
                        NegotiationLocationBean negotiationLocationBeanNext = (NegotiationLocationBean)locationHistory.get(row + 1);
                        effectiveDateNext = negotiationLocationBeanNext.getEffectiveDate();
                    }
                    
                    if(effectiveDate.after(effectiveDateNext)) {
                        retValue = "";//new Integer(0);
                    }else {
                        int dateDiff = dateUtils.dateDifference(effectiveDateNext, effectiveDate);
                        retValue = new Integer(dateDiff);
                    }
                    break;
                case 4:
                    Date date = new Date(negotiationLocationBean.getUpdateTimestamp().getTime());
                    retValue = timeStampFormat.format(date);
                    break;
                case 5:
                    retValue = negotiationLocationBean.getUpdateUser();
                    break;
            }
            
            return  retValue;
        }
        
        public int getRowCount() {
            if(locationHistory != null) {
                return locationHistory.size();
            }
            return 0;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        //Added for case 4185 - Effective Date should be editable - Start
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            
            if(columnIndex==EFF_DATE_COL && getFunctionType()!=TypeConstants.DISPLAY_MODE){
                return true;
            }else{
                return false;
            }
        }
        
        public void setValueAt(Object value, int row, int column) {
            
            negotiationLocationBean = (NegotiationLocationBean)locationHistory.elementAt(row);
            Date date       = null;
            String strDate  = null;
            switch(column) {
                
                case EFF_DATE_COL :
                    try{
                        if (value.toString().trim().length() > 0) {
                            strDate = dateUtils.formatDate(value.toString().trim(), CoeusGuiConstants.DATE_SEPARATORS, CoeusGuiConstants.UI_DATE_FORMAT);
                        } else {
                            throw new CoeusException(coeusMessageResources.parseMessageKey(ERRKEY_EMPTY_EFF_DATE));
                        }
                        strDate = dateUtils.restoreDate(strDate, CoeusGuiConstants.DATE_SEPARATORS);
                        if(strDate==null) {
                            throw new CoeusException(coeusMessageResources.parseMessageKey(ERRKEY_INVALID_EFF_DATE));
                        }
                        date = simpleDateFormat.parse(strDate.trim());
                        validateDateRange(row,date);
                        if(!date.equals(negotiationLocationBean.getEffectiveDate())){
                            negotiationLocationBean.setEffectiveDate(new java.sql.Date(date.getTime()));
                            negotiationLocationBean.setAcType(TypeConstants.UPDATE_RECORD);
                            cvUpdateData.add(negotiationLocationBean);
                            if(row>0){
                                fireTableRowsUpdated(row-1,row-1);
                            }
                        }
                    }catch (CoeusException exception) {
                        CoeusOptionPane.showWarningDialog(exception.getUserMessage());
                        setSaveRequired(false);
                        negotiationLocationHistory.tblNegLocHistory.scrollRectToVisible(negotiationLocationHistory.tblNegLocHistory.getCellRect(row,column,true));
                    }catch(ParseException parseEx){
                        parseEx.printStackTrace();
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(ERRKEY_INVALID_EFF_DATE));
                        setSaveRequired(false);
                        negotiationLocationHistory.tblNegLocHistory.scrollRectToVisible(negotiationLocationHistory.tblNegLocHistory.getCellRect(row,column,true));
                    }
                    break;
                default:
                    break;
                    
            }
        }
        
        private void validateDateRange(int row, Date currentDate) throws CoeusException{
            Date prevDate = null,nextDate  = null;
            int prevIndex = row-1;
            int nextIndex = row+1;
            
            if(prevIndex>=0){
                prevDate = ((NegotiationLocationBean)locationHistory.elementAt(prevIndex)).getEffectiveDate();
                if(prevDate.after(currentDate)){
                    throw new CoeusException(
                            coeusMessageResources.parseMessageKey(ERRKEY_EFF_DATE_AFTER)+dateUtils.formatDate(prevDate.toString(),CoeusGuiConstants.UI_DATE_FORMAT));
                }
            }
            if(nextIndex<getRowCount()){
                nextDate = ((NegotiationLocationBean)locationHistory.elementAt(nextIndex)).getEffectiveDate();
                if(nextDate.before(currentDate)){
                    throw new CoeusException(
                            coeusMessageResources.parseMessageKey(ERRKEY_EFF_DATE_BEFORE)+dateUtils.formatDate(nextDate.toString(),CoeusGuiConstants.UI_DATE_FORMAT));
                }
            }
        }
        //Added for case:4185 - Effective Date should be editable - End
    }
//    Renderer class for NegotiationLocationHistory table - Added for case 4185 - Start
    private class NegotiationLocHistoryRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Color color =(Color) UIManager.getDefaults().get("Table.selectionBackground");
            setHorizontalAlignment(JLabel.LEFT);
            switch(column) {
                case EFF_DATE_COL:
                    JLabel lbl = new JLabel();
                    if(getFunctionType()!=TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                            lbl.setBackground(Color.YELLOW);
                            
                        }else{
                            lbl.setBackground(Color.white);
                        }
                        lbl.setForeground(Color.BLACK);
                    }else{
                        if(isSelected){
                            lbl.setBackground(color);
                            lbl.setForeground(Color.white);
                            
                        }else{
                            lbl.setForeground(Color.BLACK);
                        }
                    }
                    lbl.setOpaque(true);
                    if(value == null || value.toString().equals(CoeusGuiConstants.EMPTY_STRING)){
                        lbl.setText(CoeusGuiConstants.EMPTY_STRING);
                        return lbl;
                    }else {
                        String val=null;
                        val= dateUtils.formatDate(value.toString(),CoeusGuiConstants.UI_DATE_FORMAT);
                        lbl.setText(val);
                        return lbl;
                    }
                default :
                    return super.getTableCellRendererComponent(table, value,isSelected,hasFocus,row,column);
            } //switch ends
        } // getTableCellRendererComponent() ends
    }
    //  End of  NegotiationLocHistoryRenderer
    
    
//    Editor class for NegotiationLocationHistory table - Added for case 4185 - Start
    private class NegotiationLocHistoryEditor extends AbstractCellEditor implements TableCellEditor{
        
        private CoeusTextField txtComponent;
        
        NegotiationLocHistoryEditor() {
            txtComponent = new CoeusTextField();
        }
        public Object getCellEditorValue() {
            return txtComponent.getText();
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            txtComponent.setHorizontalAlignment(JLabel.LEFT);
//            txtComponent.setDocument(new edu.mit.coeus.utils.LimitedPlainDocument(1000));
            switch(column) {
                case EFF_DATE_COL :
                    if(value == null || value.toString().equals(CoeusGuiConstants.EMPTY_STRING)) {
                        txtComponent.setText(CoeusGuiConstants.EMPTY_STRING);
                    }else{
                        String val = dateUtils.formatDate(value.toString(),CoeusGuiConstants.DEFAULT_DATE_FORMAT);
                        txtComponent.setText(val);
                    }
                    break;
                default:
                    txtComponent.setText(value.toString());
                    break;
            }
            return txtComponent;
        }
        
        public int getClickCountToStart(){
            return 1;
        }
    }
    //  End of  NegotiationLocHistoryEditor    
}
