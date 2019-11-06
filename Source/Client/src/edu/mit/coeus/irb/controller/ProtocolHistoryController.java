/*
 * ProtocolHistoryController.java
 *
 * Created on July 13, 2007, 1:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.irb.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.irb.gui.ProtocolHistoryForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.ChangeHistoryBean;
import edu.mit.coeus.utils.ChangeHistoryGroup;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Date;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * This class is used as the controller for  the ProtocolHistoryForm window
 *
 * @author leenababu
 */
public class ProtocolHistoryController extends Controller implements ActionListener,ListSelectionListener{
    
    private ProtocolHistoryBean protocolHistoryBean;
    private boolean userHasViewRight;
    private String protocolNumber = "";
    private ProtocolHistoryForm protocolHistoryForm;
    
    private Vector columnVector = null;
    private CoeusAppletMDIForm mdiForm;
    private int maxSequenceNo = 0;
    
    //Added for Case# 3087 - In Premium - Review History Add the following elements - Start
    private Vector itemListTableData = new Vector();  
    private Vector historyListTableData = new Vector();
    private Vector currentSequence = new Vector();
    private Vector previousSequence = new Vector();
    private String keyValue = "";
    private ProtocolHistoryBean protocolDetailsBean = null;
    private ListItemTableModel listItemTableModel;
    private HistoryOfChangesTableModel historyOfChangesTableModel;
    private TreeMap hmProtoHist = new TreeMap();
    //Added for Case# 3087 - In Premium - Review History Add the following elements - End  
    
    public CoeusToolBarButton btnNext, btnPrevious, btnClose;
    public final char GET_PROTO_HISTORY_CHANGES = 'e';
    /** Creates a new instance of ProtocolHistoryController */
    public ProtocolHistoryController(String protocolNumber) {
        this.protocolNumber = protocolNumber;
        mdiForm = CoeusGuiConstants.getMDIForm();
        getProtocolHistoryDetails(0,0);
    }
    
    /**
     * Gets the protocol history  change between the sequences currSequence and prevSequence
     * from the server
     *
     * @param currSequence Current sequence number
     * @param prevSequence Previous sequence number
     */
//Modified for Case# 3087 - In Premium - Review History Add the following elements - Start
//    public ProtocolHistoryBean getProtocolHistoryDetails(int currSequence, int prevSequence){
    public TreeMap getProtocolHistoryDetails(int currSequence, int prevSequence){
//Modified for Case# 3087 - In Premium - Review History Add the following elements - End        
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_PROTO_HISTORY_CHANGES);
        Vector serverDataObjects = new Vector();
        serverDataObjects.add(protocolNumber);
        serverDataObjects.add(new Integer(currSequence));
        serverDataObjects.add(new Integer(prevSequence));
        requesterBean.setDataObjects(serverDataObjects);
        String connectTo = CoeusGuiConstants.CONNECTION_URL+"/protocolMntServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator
                (connectTo, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean != null){
            if(responderBean.isSuccessfulResponse()) {
                serverDataObjects = responderBean.getDataObjects();
                userHasViewRight = ((Boolean)serverDataObjects.get(0)).booleanValue();
                maxSequenceNo = ((Integer)serverDataObjects.get(1)).intValue();
                //Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - Start  
//                protocolHistoryBean = (ProtocolHistoryBean)serverDataObjects.get(2);
                hmProtoHist = (TreeMap)serverDataObjects.get(2);
                if(hmProtoHist != null){
                     protocolDetailsBean = (ProtocolHistoryBean) hmProtoHist.get("ProtocolHistoryBean");
                    hmProtoHist.remove("ProtocolHistoryBean");
                }
            //Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - End                  
            }
        }
//Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - End          
//        return protocolHistoryBean;
        return hmProtoHist;
//Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - End          
    }
    
    
    /**
     * Populates the history table with the data
     */
    public void populateTableModel(){
//Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - Start        
//        CoeusVector historyGroupList = protocolHistoryBean.getChangeHistoryGroupList();        
//        if(historyGroupList!=null){
//            ChangeHistoryGroup changeHistoryGroup = null;
//            ChangeHistoryBean changeHistoryBean = null;
//            String status = "";
//            Vector tableData = new Vector();
//            Vector rowList = null;
//            DateUtils dtUtils = new DateUtils();
//            for(int i = 0; i < historyGroupList.size(); i++){
//                changeHistoryGroup = (ChangeHistoryGroup)historyGroupList.get(i);
//                for(int j=0; j<changeHistoryGroup.getHistoryBeanList().size(); j++ ){
//                    rowList = new Vector();
//                    changeHistoryBean = (ChangeHistoryBean)changeHistoryGroup.getHistoryBeanList().get(j);
//                    if(changeHistoryBean.getStatus() == 1){
//                        status = "Modified";
//                    }else if(changeHistoryBean.getStatus() == 2){
//                        status = "New";
//                    }else if(changeHistoryBean.getStatus() == 3){
//                        status = "Deleted";
//                    }else{
//                        status = "";
//                    }
//                    rowList.add(status);
//                    if(changeHistoryGroup.getName().equals("General Info")){
//                        rowList.add(changeHistoryBean.getName());
//                    }else{
//                        if(j == 0){
//                            rowList.add(changeHistoryGroup.getName());
//                        }else{
//                            rowList.add(changeHistoryBean.getName());
//                        }
//                    }
//                    if(changeHistoryBean.getType().equals("Date") && changeHistoryBean.getValue()!=null && (!changeHistoryBean.getValue().equals(""))){
//                        rowList.add(dtUtils.formatDate(changeHistoryBean.getValue(),"dd-MMM-yyyy"));
//                    }else{
//                        rowList.add(changeHistoryBean.getValue());
//                    }
//                    rowList.add(status);
//                    tableData.add(rowList);
//                }
//            }
//            ((DefaultTableModel)protocolHistoryForm.tblDetails.getModel()).setDataVector(tableData, columnVector);
//        }
//        protocolHistoryForm.tblDetails.getColumnModel().getColumn(0).setHeaderRenderer(new EmptyHeaderRenderer());
//        protocolHistoryForm.tblDetails.getColumnModel().getColumn(0).setCellRenderer(new StatusIconRenderer());
//        
//        protocolHistoryForm.tblDetails.getColumnModel().getColumn(0).setPreferredWidth(30);
//        protocolHistoryForm.tblDetails.getColumnModel().getColumn(0).setMaxWidth(30);
//        protocolHistoryForm.tblDetails.getColumnModel().getColumn(0).setMinWidth(30);
//        
//
//        
//        protocolHistoryForm.tblDetails.getColumnModel().getColumn(1).setPreferredWidth(150);
//        protocolHistoryForm.tblDetails.getColumnModel().getColumn(1).setMinWidth(150);
//        protocolHistoryForm.tblDetails.getColumnModel().getColumn(1).setMaxWidth(150);
//        
//        protocolHistoryForm.tblDetails.getColumnModel().getColumn(3).setPreferredWidth(100);
//        protocolHistoryForm.tblDetails.getColumnModel().getColumn(3).setMinWidth(100);
//        protocolHistoryForm.tblDetails.getColumnModel().getColumn(3).setMaxWidth(100);
        
        if(hmProtoHist != null){
            itemListTableData.removeAllElements();
            Set keys = hmProtoHist.keySet();
            Iterator keyIter = keys.iterator();
            while(keyIter.hasNext()){
              String mapKeys = (String)keyIter.next();  
              itemListTableData.add(mapKeys) ;
            }
        }       
        listItemTableModel.setData(itemListTableData);
        listItemTableModel.fireTableDataChanged();
        protocolHistoryForm.tblItemList.setRowSelectionInterval(0,0);  
        
        historyOfChangesTableModel = new HistoryOfChangesTableModel();
        protocolHistoryForm.tblItemValues.setModel(historyOfChangesTableModel);
        //Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - End
        

    }
    
    /**
     * Performs the close operation
     */
    public void performClose(){
        if(mdiForm.getFrame(CoeusGuiConstants.PROTOCOL_HISTORY_WINDOW, protocolNumber)!=null){
            protocolHistoryForm = (ProtocolHistoryForm)mdiForm.getFrame(CoeusGuiConstants.PROTOCOL_HISTORY_WINDOW, protocolNumber);
            mdiForm.removeFrame(CoeusGuiConstants.PROTOCOL_HISTORY_WINDOW, protocolNumber);
            protocolHistoryForm.doDefaultCloseAction();
            protocolHistoryForm.setVisible(false);
        }
    }
    /**
     * Invoked when an action event occurs
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if(source.equals(btnPrevious)){
//Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - Start                
//                protocolHistoryBean = getProtocolHistoryDetails(
//                        protocolHistoryBean.getCurrProtocolInfoBean().getSequenceNumber()-1,
//                        protocolHistoryBean.getPrevProtocolInfoBean().getSequenceNumber()-1);
                
                hmProtoHist = getProtocolHistoryDetails(
                        protocolDetailsBean.getCurrProtocolInfoBean().getSequenceNumber()-1,
                        protocolDetailsBean.getPrevProtocolInfoBean().getSequenceNumber()-1);
                
                setFormData(null);
            }else if(source.equals(btnNext)){
//                protocolHistoryBean = getProtocolHistoryDetails(
//                        protocolHistoryBean.getCurrProtocolInfoBean().getSequenceNumber()+1,
//                        protocolHistoryBean.getPrevProtocolInfoBean().getSequenceNumber()+1);
                
                hmProtoHist = getProtocolHistoryDetails(
                        protocolDetailsBean.getCurrProtocolInfoBean().getSequenceNumber()+1,
                        protocolDetailsBean.getPrevProtocolInfoBean().getSequenceNumber()+1);
//Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - End                
                setFormData(null);
            }else if(source.equals(btnClose)){
                performClose();
            }
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Displays the window
     */
    public void display() {
        try {
            if((mdiForm.getFrame(CoeusGuiConstants.PROTOCOL_HISTORY_WINDOW, protocolNumber ))!=null){
                protocolHistoryForm = (ProtocolHistoryForm)mdiForm.getFrame(CoeusGuiConstants.PROTOCOL_HISTORY_WINDOW, protocolNumber );
            }else{
                registerComponents();
                mdiForm.putFrame(CoeusGuiConstants.PROTOCOL_HISTORY_WINDOW, protocolNumber, 'D', protocolHistoryForm);
                mdiForm.getDeskTopPane().add(protocolHistoryForm);
                setFormData(null);
            }
            
            protocolHistoryForm = (ProtocolHistoryForm)mdiForm.getFrame(CoeusGuiConstants.PROTOCOL_HISTORY_WINDOW, protocolNumber );
            protocolHistoryForm.setVisible(true);
            protocolHistoryForm.setSelected(true);
        }catch (PropertyVetoException ex) {
            ex.printStackTrace();
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }
    
    public void formatFields() {
    }
    
    public Component getControlledUI() {
        return null;
    }
    
    public Object getFormData() {
//Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - Start        
//        return protocolHistoryBean;
        return protocolDetailsBean;
//Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - End        
    }
    
    /**
     * Initial settings for the components in the form
     */
    public void registerComponents() {
        protocolHistoryForm = new ProtocolHistoryForm(CoeusGuiConstants.PROTOCOL_HISTORY_WINDOW, mdiForm);
        //Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - Start
//        if(protocolHistoryBean!=null){
//            protocolHistoryForm.setMaxSequenceNumber(protocolHistoryBean.getCurrProtocolInfoBean().getSequenceNumber());
//        }        
        if(protocolDetailsBean!=null){
            protocolHistoryForm.setMaxSequenceNumber(protocolDetailsBean.getCurrProtocolInfoBean().getSequenceNumber());
        }
        //Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - End
        
        protocolHistoryForm.setFrameToolBar(getToolBarMenu());
        protocolHistoryForm.setFrame(CoeusGuiConstants.PROTOCOL_HISTORY_WINDOW);
        protocolHistoryForm.setFrameIcon( mdiForm.getCoeusIcon() );
//Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - Start        
//        protocolHistoryForm.tblDetails.setRowHeight(22);
//        protocolHistoryForm.tblDetails.setEnabled(false);
//        protocolHistoryForm.tblDetails.getTableHeader().setReorderingAllowed(false);
        
//        columnVector =  new Vector();
//        columnVector.add("");
//        columnVector.add("Name");
//        columnVector.add("Value");
//        columnVector.add("Status");
        historyOfChangesTableModel = new HistoryOfChangesTableModel();
        protocolHistoryForm.tblItemValues.setRowHeight(22);
        protocolHistoryForm.tblItemValues.setEnabled(false);
        
        JTableHeader tableHeader = protocolHistoryForm.tblItemValues.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0,22));
       
        protocolHistoryForm.tblItemValues.setModel(historyOfChangesTableModel);        
        
        listItemTableModel = new ListItemTableModel();
        protocolHistoryForm.tblItemList.setRowHeight(22);
        protocolHistoryForm.tblItemList.setEnabled(true);
        
        JTableHeader tableItemHeader = protocolHistoryForm.tblItemList.getTableHeader();
        tableItemHeader.setReorderingAllowed(false);
        tableItemHeader.setFont(CoeusFontFactory.getLabelFont());
        tableItemHeader.setPreferredSize(new Dimension(0,22));
        
        protocolHistoryForm.tblItemList.setModel(listItemTableModel);
        protocolHistoryForm.tblItemList.getSelectionModel().addListSelectionListener(this);
        protocolHistoryForm.tblItemList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        protocolHistoryForm.tblItemList.setRowSelectionAllowed(true);
        //Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - End
        
        btnPrevious.addActionListener(this);
        btnNext.addActionListener(this);
        btnClose.addActionListener(this);
    }
    
    /**
     * Returns the toolbar for the ProtoclHistory Window
     * @return JToolBar - toolbar for the window.
     */
    private JToolBar getToolBarMenu(){
        JToolBar protocolHistoryToolBar = new JToolBar();
        
        btnPrevious = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ELEFT_ARROW_ICON)),
                null, "Previous");
        
        btnNext = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ERIGHT_ARROW_ICON)),
                null, "Next");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
                null, "Close");
        
        protocolHistoryToolBar.add(btnPrevious);
        protocolHistoryToolBar.add(btnNext);
        protocolHistoryToolBar.addSeparator();
        protocolHistoryToolBar.add(btnClose);
        return protocolHistoryToolBar;
    }
    
    public void saveFormData() throws CoeusException {
    }
    
    /**
     * Set the data into the form componenets
     */
    public void setFormData(Object data) throws CoeusException {
//Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - Start
//        if(protocolHistoryBean!=null){
//            protocolHistoryForm.lblProtlNoValue.setText(protocolHistoryBean.getProtocolNumber());
//            protocolHistoryForm.lblSeqValue.setText(protocolHistoryBean.getSequenceNumber());
//            protocolHistoryForm.lblPIValue.setText(protocolHistoryBean.getPrincpalInvestigator());
//            protocolHistoryForm.lblActionValue.setText(protocolHistoryBean.getLastAction());
//            protocolHistoryForm.lblTitleValue.setText(protocolHistoryBean.getTitle());
//
//            protocolHistoryForm.setMaxSequenceNumber(maxSequenceNo);
//            if(protocolHistoryBean.getCurrProtocolInfoBean().getSequenceNumber()>=protocolHistoryForm.getMaxSequenceNumber()){
//                ((CoeusInternalFrame)protocolHistoryForm).getFrameToolBar().getComponentAtIndex(1).setEnabled(false);
//                //btnNext.setEnabled(false);
//            }else{
//                ((CoeusInternalFrame)protocolHistoryForm).getFrameToolBar().getComponentAtIndex(1).setEnabled(true);
//                //    btnNext.setEnabled(true);
//            }
//            if(protocolHistoryBean.getPrevProtocolInfoBean().getSequenceNumber()<=1){
//
//                ((CoeusInternalFrame)protocolHistoryForm).getFrameToolBar().getComponentAtIndex(0).setEnabled(false);
//                //btnPrevious.setEnabled(false);
//            }else{
//                ((CoeusInternalFrame)protocolHistoryForm).getFrameToolBar().getComponentAtIndex(0).setEnabled(true);
//                //btnPrevious.setEnabled(true);
//            }
//        }
//        populateTableModel();
        if(protocolDetailsBean!=null){
            protocolHistoryForm.lblProtlNoValue.setText(protocolDetailsBean.getProtocolNumber());
            protocolHistoryForm.lblSeqValue.setText(protocolDetailsBean.getSequenceNumber());
            protocolHistoryForm.lblPIValue.setText(protocolDetailsBean.getPrincpalInvestigator());
            protocolHistoryForm.lblActionValue.setText(protocolDetailsBean.getLastAction());
            protocolHistoryForm.txtArTitle.setEditable(false);
            protocolHistoryForm.txtArTitle.setText(protocolDetailsBean.getTitle());
            protocolHistoryForm.setMaxSequenceNumber(maxSequenceNo);
            if(protocolDetailsBean.getCurrProtocolInfoBean().getSequenceNumber()>=protocolHistoryForm.getMaxSequenceNumber()){
                ((CoeusInternalFrame)protocolHistoryForm).getFrameToolBar().getComponentAtIndex(1).setEnabled(false);
            }else{
                ((CoeusInternalFrame)protocolHistoryForm).getFrameToolBar().getComponentAtIndex(1).setEnabled(true);
            }
            if(protocolDetailsBean.getPrevProtocolInfoBean().getSequenceNumber()<=1){
                ((CoeusInternalFrame)protocolHistoryForm).getFrameToolBar().getComponentAtIndex(0).setEnabled(false);
            }else{
                ((CoeusInternalFrame)protocolHistoryForm).getFrameToolBar().getComponentAtIndex(0).setEnabled(true);
            }
            populateTableModel();
        }
        //Commented and Added for Case# 3087 - In Premium - Review History Add the following elements - End
    }
    
    
    public boolean validate() throws CoeusUIException {
        return false;
    }
    
    /**
     * Getter method for the property userHasViewRight
     * @return boolean
     */
    public boolean isUserHasViewRight() {
        return userHasViewRight;
    }
    
    /**
     * Getter method for the maxSequenceNo
     */
    public int getMaxSequenceNo() {
        return maxSequenceNo;
    }
//Commented for Case# 3087 - In Premium - Review History Add the following elements - Start    
    /**
     * This class is used as the renderer for the history table to show images
     * for the status New, Modified and Deleted
     */
//    class StatusIconRenderer extends DefaultTableCellRenderer{
//        private ImageIcon newIcon;
//        private ImageIcon deleteIcon;
//        private ImageIcon modifiedIcon;
//        private ImageIcon noChangeIcon;
//        public StatusIconRenderer(){
//            newIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.HISTORY_NEW_ICON));
//            deleteIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.HISTORY_DELETED_ICON));
//            modifiedIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.HISTORY_MODIFIED_ICON));
//        }
//        public Component getTableCellRendererComponent(JTable table,
//                Object value, boolean isSelected, boolean hasFocus, int row,
//                int column) {
//            setOpaque(false);
//            if(column == 0){
//                if(table.getValueAt(row,column)!=null){
//                    if(table.getValueAt(row,column).toString().equals("New")){
//                        setIcon(newIcon);
//                    }else if(table.getValueAt(row,column).toString().equals("Modified")){
//                        setIcon(modifiedIcon);
//                    }else if(table.getValueAt(row,column).toString().equals("Deleted")){
//                        setIcon(deleteIcon);
//                    } else{
//                        setIcon(noChangeIcon) ;
//                    }
//                }else{
//                    setIcon(noChangeIcon);
//                }
//            }
//            setHorizontalAlignment(SwingConstants.CENTER);
//            return this;
//        }
//    }
//Commented for Case# 3087 - In Premium - Review History Add the following elements - End
    
//Added for Case# 3087 - In Premium - Review History Add the following elements - Start      
    /* This is an inner class specify the table model for the list of items */
    class ListItemTableModel extends AbstractTableModel {	
            
            String colNames[] = {"Select a Name to view the changes"};
	    Class[] colTypes = new Class[]{String.class};
                
		/**
		 * This method will check whether the given field is ediatble or not
		 * @param row int
		 * @param col int
		 * @return boolean
		 */
                public boolean isCellEditable(int row, int col){                   
                    return false;
                }
		/**
		 * Thie mthod will return the column count
		 * @return int
		 */
		public int getColumnCount() {
			return colNames.length;
		}
		
		/**
		 * This method will return the row count
		 * @return int
		 */
		public int getRowCount() {
                    // the vector which has the list of items
                    if(itemListTableData == null)
                        return 0;
                    else
                        return itemListTableData.size();
			
		}
		
		/**
		 * This method will return the column class
		 * @param columnIndex int
		 * @return Class
		 */
		public Class getColumnClass(int columnIndex) {
			return colTypes [columnIndex];
		}
		
		/**
		 * To get the column name
		 * @param column int
		 * @return String
		 */
		public String getColumnName(int column) {
			return colNames[column];
		}
		
		/**
		 * settig the data to the vector
		 * @param cvTableData CoeusVector
		 */		
		public void setData(Vector itemListTableData){
			itemListTableData = itemListTableData;
		}
		
		/**
		 * To get the value depends on the row and column
		 * @param rowIndex int
		 * @param columnIndex int
		 * @return Object
		 */
		
		public Object getValueAt(int rowIndex, int columnIndex) {
                    return itemListTableData.get(rowIndex);
			
		} // end of getValueAt()
	} // end of listItemTableModel    
    
 
    /* This is an inner class specify the table model for the history of changes in consecutive sequences */
	class HistoryOfChangesTableModel extends AbstractTableModel {
            
            String colNames[] = getColumnNames();
            Class[] colTypes = new Class[]{String.class,String.class};
            DateUtils dtUtils = new DateUtils();
            /**
             * This method will check whether the given field is ediatble or not
             * @param row int
             * @param col int
             * @return boolean
             */
            public boolean isCellEditable(int row, int col){
                return false;
            }
            
            
            /**
             * Thie mthod will return the column count
             * @return int
             */
            public int getColumnCount() {
                return colNames.length;
            }
            
            /**
             * This method will return the row count
             * @return int
             */
            public int getRowCount() {
                if( currentSequence == null && previousSequence == null ){
                    return 0;
                }else{
                    if(currentSequence.size() >= previousSequence.size()){
                        return currentSequence.size();
                    }else{
                        return previousSequence.size();
                    }
                }
            }
          
            /**
             * This method will return the column class
             * @param columnIndex int
             * @return Class
             */
            public Class getColumnClass(int columnIndex) {
                return colTypes [columnIndex];
            }
            
            /**
             * To get the column name
             * @param column int
             * @return String
             */
            public String getColumnName(int column) {
                return colNames[column];
            }
            
           /*
            * Get the column names dynamically
            * @return array of String 
            */
            public String[] getColumnNames(){
                String currentSeq = "Sequence - ";
                String prevSeq = "Sequence - ";
                currentSeq = currentSeq + protocolDetailsBean.getCurrProtocolInfoBean().getSequenceNumber();
                int prevSequence = protocolDetailsBean.getPrevProtocolInfoBean().getSequenceNumber();
                prevSeq = prevSeq + prevSequence ;
                String columns[] = {currentSeq,prevSeq};
                return columns;
            }
            
            /**
             * To get the values for the table
             * @param Object obj
             * @return String
             */
            
            
            public String getTableValues(Object obj){
                if(obj instanceof java.util.Date ){  
                    return dtUtils.formatCalendar(dtUtils.getCalendar((Date) obj), "dd-MMM-yyyy");
                    
                }else if(obj instanceof edu.mit.coeus.irb.bean.ProtocolFundingSourceBean ){
                    
                    ProtocolFundingSourceBean protocolFundingSourceBean =
                            (ProtocolFundingSourceBean)obj;
                    
                    return protocolFundingSourceBean.getFundingSourceTypeDesc()+
                            " : "+protocolFundingSourceBean.getFundingSource();
                    
                }else if(obj instanceof edu.mit.coeus.irb.bean.ProtocolInvestigatorsBean ){
                    
                    ProtocolInvestigatorsBean protocolInvestigatorsBean =
                            (ProtocolInvestigatorsBean)obj;
                    return protocolInvestigatorsBean.getPersonName();
                    
                }else if(obj instanceof edu.mit.coeus.irb.bean.ProtocolKeyPersonnelBean ){
                    
                    ProtocolKeyPersonnelBean protocolKeyPersonnelBean =
                            (ProtocolKeyPersonnelBean)obj;
                    return protocolKeyPersonnelBean.getPersonName();
                    
                }else if(obj instanceof edu.mit.coeus.irb.bean.ProtocolLocationListBean ){
                    
                    ProtocolLocationListBean protocolLocationListBean =
                            (ProtocolLocationListBean)obj;
                    return protocolLocationListBean.getOrganizationTypeName()+
                            " : "+protocolLocationListBean.getOrganizationName();
                    
                }else if(obj instanceof edu.mit.coeus.irb.bean.ProtocolCorrespondentsBean ){
                    
                    ProtocolCorrespondentsBean protocolCorrespondentsBean =
                            (ProtocolCorrespondentsBean)obj;
                    return protocolCorrespondentsBean.getCorrespondentTypeDesc()
                    +" : "+protocolCorrespondentsBean.getPersonName();
                    
                }else if(obj instanceof edu.mit.coeus.irb.bean.ProtocolReasearchAreasBean ){
                    
                    ProtocolReasearchAreasBean protocolReasearchAreasBean =
                            (ProtocolReasearchAreasBean)obj;
                    return protocolReasearchAreasBean.getResearchAreaDescription();
              
                }else if(obj instanceof edu.mit.coeus.irb.bean.ProtocolVulnerableSubListsBean ){
                    ProtocolVulnerableSubListsBean protocolVulnerableSubListsBean =
                            (ProtocolVulnerableSubListsBean)obj;
                    return protocolVulnerableSubListsBean.getVulnerableSubjectTypeDesc()+" : "+protocolVulnerableSubListsBean.getSubjectCount();
                    
                }else if(obj instanceof edu.mit.coeus.irb.bean.ProtocolSpecialReviewFormBean ){
                    ProtocolSpecialReviewFormBean protocolSpecialReviewFormBean =
                            (ProtocolSpecialReviewFormBean)obj;
                    return protocolSpecialReviewFormBean.getSpecialReviewDescription();
                    
                }else if(obj instanceof edu.mit.coeus.irb.bean.UploadDocumentBean ){
                    UploadDocumentBean uploadDocumentBean =
                            (UploadDocumentBean)obj;
                    return uploadDocumentBean.getDocType()+":"+uploadDocumentBean.getDescription();
                    
               }else if(obj instanceof String){
                    return obj.toString();
                }
                return "";
            }

             /**
             * To get the value depends on the row and column
             * @param rowIndex int
             * @param columnIndex int
             * @return Object
             */            
            public Object getValueAt(int rowIndex, int columnIndex) {
                Object obj = null;
                String values = "";
                if(columnIndex == 0){
                    if(currentSequence.size() > rowIndex){
                            obj = currentSequence.get(rowIndex);
                            values = getTableValues(obj);
                    } 
                }else if(columnIndex == 1){
                    if(previousSequence.size() > rowIndex){   
                            obj = previousSequence.get(rowIndex);
                             values = getTableValues(obj);
                    }
                }
                return values;
            } // end of getValueAt()
        } // end of HistoryOfChangesTableModel
    
 
    
        /*
         * Get the values for the item selected from the Items table
         * @param ListSelectionEvent
         */
        public void valueChanged(ListSelectionEvent e) {
            
            int selectedRow = protocolHistoryForm.tblItemList.getSelectedRow();
            if(selectedRow == -1){
                return;
            }else{
                keyValue  = (String)protocolHistoryForm.tblItemList.getModel().getValueAt(selectedRow,0);
                historyListTableData = (Vector)hmProtoHist.get(keyValue);
                currentSequence = (Vector)historyListTableData.get(0);
                previousSequence = (Vector)historyListTableData.get(1);
                protocolHistoryForm.pnlItemValues.setBorder(
                        BorderFactory.createTitledBorder(null, "Changes in "+keyValue
                        , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION
                        , javax.swing.border.TitledBorder.DEFAULT_POSITION
                        , CoeusFontFactory.getLabelFont()));
               historyOfChangesTableModel.fireTableDataChanged();
            }
        }
// Added for Case# 3087 - In Premium - Review History Add the following elements - End  
}
