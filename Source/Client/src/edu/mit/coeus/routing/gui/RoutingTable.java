/*
 * RoutingTable.java
 *
 * Created on January 16, 2004, 6:57 PM
 */

package edu.mit.coeus.routing.gui;

import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.Color;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.event.ListSelectionListener;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.gui.*;

/**
 *
 * @author  ranjeeva
 */
public class RoutingTable extends javax.swing.JTable implements ListSelectionListener {
    
    private static final int USER_ID_COLUMN = 0;
    private static final String EMPTY_STRING = "";
    
    /** static variable for BLUE color */
    private static final Color BLUE_COLOR  = Color.BLUE;
    private static final Color BLACK_COLOR  = Color.BLACK;
    
      
    private URL imageURLPrimaryVerify;
    private URL imageURLPrimaryApprove;
    private URL imageURLPrimaryWaiting;
    private URL imageURLPrimaryPassed;
    private URL imageURLPrimaryRejected;
    private URL imageURLAlternate;
    private URL imageURLAltVerify;
    private URL imageURLAltApprove;
    private URL imageURLAltWaiting;
    private URL imageURLAltPassed;
    private URL imageURLAltReject;
    private URL imageURLPrimaryByPassed;
    private URL imageURLAltApproveOthers;
    private URL imageURLPrimaryApproveOthers;
    //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
    private URL imageURLAltRecalled;
    //COEUSQA:3441 - End
    
    private CoeusVector cvRoutingDetailsBean ;
    private RoutingTableModel routingTableModel;
    private TableColumn tableColumn;
    private Color tableBackGroundColor;
    private int colSize[];
    private boolean show = true;
    private String approvalFlags [][] = {
        {"W","Waiting for Approval"},
        {"A","Approved"},
        {"R","Rejected"},
        {"P","Passed"},
        {"L","Passed by other"},
        {"O","Approved by other"},
        {"B","Bypassed"},
        {"J","Rejected by other"},
        {"D", "Delegated"},
        {"T", "To be submitted"},
        //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
        {"C", "Recalled"},
        {"E", "Recalled by Other"}
        //COEUSQA:3441 - End
    };
    
    private Vector vecPanelRows;
    
    /**
     * Creates new form RoutingTable
     */
    public RoutingTable() {
        //setting Table Column
        colSize = new int[1];
        colSize[0] = 700;
        
        
        vecPanelRows =  new Vector();
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setShowHorizontalLines(false);
        this.setShowVerticalLines(false);
        
        tableBackGroundColor = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
        this.setBackground(tableBackGroundColor);
        
        this.setFont(CoeusFontFactory.getNormalFont());
        this.setRowHeight(22);
        this.setEnabled(true);
        
        routingTableModel = new RoutingTableModel();
        this.setModel(routingTableModel);
        
        imageURLPrimaryVerify = getClass().getClassLoader().getResource( CoeusGuiConstants.PRIMARY_VERIFY);
        imageURLPrimaryApprove = getClass().getClassLoader().getResource( CoeusGuiConstants.PRIMARY_APPROVE);
        imageURLPrimaryWaiting = getClass().getClassLoader().getResource( CoeusGuiConstants.PRIMARY_WAITING);
        imageURLPrimaryPassed  = getClass().getClassLoader().getResource( CoeusGuiConstants.PRIMARY_PASSED);
        imageURLPrimaryByPassed  = getClass().getClassLoader().getResource( CoeusGuiConstants.PRIMARY_ICON_PATH);
        imageURLPrimaryRejected = getClass().getClassLoader().getResource( CoeusGuiConstants.PRIMARY_REJECT);
        imageURLPrimaryApproveOthers = getClass().getClassLoader().getResource( CoeusGuiConstants.PRIMARY_APPROVE_OTHER);

        imageURLAlternate  = getClass().getClassLoader().getResource( CoeusGuiConstants.ALTERNATE_ICON_PATH);
        imageURLAltVerify  = getClass().getClassLoader().getResource( CoeusGuiConstants.ALTERNATE_VERIFY);
        imageURLAltApprove = getClass().getClassLoader().getResource( CoeusGuiConstants.ALTERNATE_APPROVE);
        imageURLAltWaiting = getClass().getClassLoader().getResource( CoeusGuiConstants.ALTERNATE_WAITING);
        imageURLAltPassed = getClass().getClassLoader().getResource( CoeusGuiConstants.ALTERNATE_PASSED);
        imageURLAltReject = getClass().getClassLoader().getResource( CoeusGuiConstants.ALTERNATE_REJECT);
        imageURLAltApproveOthers = getClass().getClassLoader().getResource( CoeusGuiConstants.ALTERNATE_APPROVE_OTHER);
        //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
        imageURLAltRecalled =  getClass().getClassLoader().getResource( CoeusGuiConstants.RECALLED_ICON_PATH);        
        //COEUSQA:3441 - End
        
    }
    
    public void formatTable() {
        
        TableCellRenderer tableCellRenderer = new TableCellRenderer();
        this.getTableHeader().setDefaultRenderer(new EmptyHeaderRenderer());
        for(int col = 0; col < colSize.length; col++) {
            tableColumn = this.getColumnModel().getColumn(col);
            tableColumn.setCellRenderer(tableCellRenderer);
            tableColumn.setMaxWidth(colSize[col]);
            tableColumn.setPreferredWidth(colSize[col]);
            
        }
    }
    
    
    public void setTableData(CoeusVector cvRoutingDetailsBean) {
        
        String sortingFields [] = { "levelNumber" , "stopNumber"};
        cvRoutingDetailsBean.sort(sortingFields,true);
        this.cvRoutingDetailsBean = cvRoutingDetailsBean;
        routingTableModel.setData(cvRoutingDetailsBean);
        
        routingTableModel.fireTableDataChanged();
    }
    
    public CoeusVector getTableData(){
        return cvRoutingDetailsBean;
    }
    
    public void showStatus(boolean show) {
        this.show = show;
    }
    public RoutingTableModel getTableModel() {
        if(routingTableModel == null){
            routingTableModel = new RoutingTableModel();
        }
        return routingTableModel;
    }
    
    public RoutingTable getTable() {
        return this;
    }
    
    
    public RoutingDetailsBean getSelectedProposalApprovalBean(int selectedIndex)  {
        
        if(vecPanelRows != null && vecPanelRows.size() > 0) {
            RoutingPanelRow routingPanelRow = (RoutingPanelRow) vecPanelRows.get(selectedIndex);
            return routingPanelRow.getRoutingDetailsBean();
        }
        
        return null;
    }
    
    private String getApprovalStatusDescription(String approvalStatusCode) {
        
        if(approvalStatusCode != null) {
            
            for(int index=0;index<approvalFlags.length;index++) {
                
                if(approvalFlags[index][0].equals(approvalStatusCode)) {
                    return approvalFlags[index][1];
                }
                
            }
        }
        return EMPTY_STRING;
    }
    
    public ImageIcon getImageIconForApprvStatus(RoutingDetailsBean routingDetailsBean) {
     /*
         private URL imageURLPrimaryVerify;
    private URL imageURLPrimaryApprove;
    private  URL imageURLPrimaryWaiting;
    private URL imageURLPrimaryPassed;
    private URL imageURLPrimaryRejected;
    private URL imageURLAltVerify;
    private URL imageURLAltApprove;
    private URL imageURLAltWaiting;
    private URL imageURLAltPassed;
    private URL imageURLAltReject;
      */
        
        java.net.URL imageURL = imageURLPrimaryVerify;
        String approvalStatus = routingDetailsBean.getApprovalStatus();
        if(routingDetailsBean.isPrimaryApproverFlag()){
            imageURL = imageURLPrimaryByPassed;
        }
        if(routingDetailsBean.isPrimaryApproverFlag() && approvalStatus.equalsIgnoreCase("B") ){
            imageURL = imageURLPrimaryByPassed;
        }
        
        if(routingDetailsBean.isPrimaryApproverFlag() && approvalStatus.equalsIgnoreCase("T") ){
            imageURL = imageURLPrimaryVerify;
        }
        
        if(routingDetailsBean.isPrimaryApproverFlag() && approvalStatus.equalsIgnoreCase("A") ){
            imageURL = imageURLPrimaryApprove;
        }
        
        if(routingDetailsBean.isPrimaryApproverFlag() && approvalStatus.equalsIgnoreCase("W") ){
            imageURL = imageURLPrimaryWaiting;
        }
        
        if(routingDetailsBean.isPrimaryApproverFlag()
        && ( approvalStatus.equalsIgnoreCase("P") || approvalStatus.equalsIgnoreCase("L") )
        ){
            imageURL = imageURLPrimaryPassed;
        }
        
        if(routingDetailsBean.isPrimaryApproverFlag()
        &&( approvalStatus.equalsIgnoreCase("R") || approvalStatus.equalsIgnoreCase("J") )
        ){
            imageURL = imageURLPrimaryRejected;
            
        }
        
        if(routingDetailsBean.isPrimaryApproverFlag() && approvalStatus.equalsIgnoreCase("O") ){
            imageURL = imageURLPrimaryApproveOthers;
        }
        
        if(!routingDetailsBean.isPrimaryApproverFlag()){
            imageURL = imageURLAlternate;
        }
        //======ALTERNATE PERSONS
        if(!routingDetailsBean.isPrimaryApproverFlag() && approvalStatus.equalsIgnoreCase("T") ){
            imageURL = imageURLAltVerify;
        }
        
        if(!routingDetailsBean.isPrimaryApproverFlag() &&  approvalStatus.equalsIgnoreCase("A") ){
            imageURL = imageURLAltApprove;
        }
        
        if(!routingDetailsBean.isPrimaryApproverFlag() && routingDetailsBean.getApprovalStatus().equalsIgnoreCase("W") ){
            imageURL = imageURLAltWaiting;
        }
        
        if(!routingDetailsBean.isPrimaryApproverFlag()
        && ( approvalStatus.equalsIgnoreCase("P") || approvalStatus.equalsIgnoreCase("L") )
        ){
            imageURL = imageURLAltPassed;
        }
        
        if(!routingDetailsBean.isPrimaryApproverFlag()
        &&( approvalStatus.equalsIgnoreCase("R") || approvalStatus.equalsIgnoreCase("J") )
        ){
            imageURL = imageURLAltReject;
        }
        
        if(!routingDetailsBean.isPrimaryApproverFlag() && (approvalStatus.equalsIgnoreCase("O") )
        ){
            imageURL = imageURLAltApproveOthers;
        }
        //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
        if((approvalStatus.equalsIgnoreCase("C") )
        ){
            imageURL = imageURLAltRecalled;
        }
        if((approvalStatus.equalsIgnoreCase("E") )
        ){
            imageURL = imageURLAltRecalled;
        }
        //COEUSQA:3441 - End
        
        //======ALTERNATE PERSONS
        
        if(routingDetailsBean.isPrimaryApproverFlag() && (routingDetailsBean.getAcType() == TypeConstants.INSERT_RECORD)){
            imageURL = imageURLPrimaryByPassed;
        }
        if(!routingDetailsBean.isPrimaryApproverFlag()&& (routingDetailsBean.getAcType() == TypeConstants.INSERT_RECORD)){
            imageURL = imageURLAlternate;
        }
        return new javax.swing.ImageIcon(imageURL);
        
    }
    
    /** TableCellRenderer */
    class TableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        
        private RoutingPanelRow routingPanelRow;
        private JLabel lbluserID;
        private JLabel lbluserName;
        private JLabel lblApprovalStatus;
        private JLabel lblTimeStamp;
        int alignment = javax.swing.JLabel.LEFT;
        String approvalStatus;
        boolean isSequentialBeanFound = false;
        
        /** constructor TableCellRenderer */
        TableCellRenderer() {
            
            lbluserID = new JLabel();
            lbluserName = new JLabel();
            lblApprovalStatus = new JLabel();
            lblTimeStamp = new JLabel();
            
        }
        
        /** return TableCellRendererComponent
         * @param table Table instance
         * @param value Object
         * @param isSelected boolean
         * @param hasFocus boolean
         * @param row int
         * @param column int
         * @return Component
         */
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setHorizontalAlignment(javax.swing.JLabel.LEFT);
            RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) cvRoutingDetailsBean.get(row);
            isSequentialBeanFound = findSquentialStopBean(cvRoutingDetailsBean);
            routingPanelRow =  new RoutingPanelRow(isSequentialBeanFound);
            routingPanelRow.showStatus(show);
            switch (column) {
                
                case USER_ID_COLUMN :
                    if( routingDetailsBean.getApprovalStatus() == "Z"){
                        routingPanelRow.setRowIcon(null);
                        routingPanelRow.setUserID(" Sequential Stop "+routingDetailsBean.getLevelNumber(),javax.swing.JLabel.LEADING,BLUE_COLOR,CoeusFontFactory.getLabelFont());
                        
                        routingPanelRow.setUserName(EMPTY_STRING);
                        routingPanelRow.setApprovalStatus(EMPTY_STRING);
                        routingPanelRow.setTimeStamp(EMPTY_STRING);
                        routingPanelRow.setBorder(null);
                    } else {
                        routingPanelRow.setRowIcon(getImageIconForApprvStatus(routingDetailsBean));
                        
                        if(!routingDetailsBean.isPrimaryApproverFlag()) { 
                            alignment = javax.swing.JLabel.CENTER;
                            
                        }
                        else {
                            alignment = javax.swing.JLabel.LEFT;
                        }
                        
                        routingPanelRow.setUserID(routingDetailsBean.getUserId().toLowerCase(),alignment,BLACK_COLOR,CoeusFontFactory.getNormalFont());
                        routingPanelRow.setUserName(routingDetailsBean.getUserName());

                        approvalStatus = getApprovalStatusDescription(routingDetailsBean.getApprovalStatus());
                        
                        if(isSequentialBeanFound) {
                            routingPanelRow.setApprovalStatus(approvalStatus);
                            if(routingDetailsBean.getApprovalDate() != null) {
                                
                                String displayDateTimeformat = null;
                                
                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("M/d/yy HH:mm");
                                    java.sql.Date databaseDate = routingDetailsBean.getApprovalDate();
                                    displayDateTimeformat = formatter.format(new java.util.Date(databaseDate.getTime()));
                                    
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                    return null;
                                }
                                
                                routingPanelRow.setTimeStamp(displayDateTimeformat);   //1/2/2003 21.43
                                
                            }
                            
                            
                        }
                        
                        if(isSelected) {
                            routingPanelRow. setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
                        }else
                            routingPanelRow.setBorder(null);
                        
                    }
                    
                    
                    routingPanelRow.setRoutingDetailsBean(routingDetailsBean);
                    vecPanelRows.add(row,routingPanelRow);
                    return routingPanelRow;
            }
            return null;
            
        }
        
        
    }//End Class - TableCellRenderer ----------------------------------------
    
    
    
    class RoutingTableModel extends AbstractTableModel{
        
        private Class columnTypes [] = {Object.class};
        private String columnNames [] = {EMPTY_STRING};
        private CoeusVector cvRoutingDetailsBean;
        
        /** Default Constructor for the RoutingTableModel
         */
        RoutingTableModel() {
        }
        
        /** Return the ColumnClass for the Index
         *@param columnIndex int column Index for which the Column Class is required
         *@return Class
         */
        public Class getColumnClass(int columnIndex) {
            return columnTypes [columnIndex];
        }
        
        
        /** Is the Cell Editable at rowIndex and columnIndex
         * @param rowIndex rowIndex
         * @param columnIndex columnIndex
         * @return if <true> the Cell is editable
         */
        public boolean isCellEditable(int rowIndex, int columnIndex){
            return false;
        }
        
        /**
         * Set  the Vector of beans
         * 
         * @param cvRoutingDetailsBean Vector of beans
         */
        public void setData(CoeusVector cvRoutingDetailsBean) {
            this.cvRoutingDetailsBean = cvRoutingDetailsBean;
            
        }
        
        /** To get the RowCount
         * @return int Row Count
         */
        public int getRowCount() {
            if(cvRoutingDetailsBean == null) {
                return 0;
            }
            return cvRoutingDetailsBean.size();
        }
        
        
        /** to get the Column Name for a columnIndex
         * @param columnIndex columnIndex for which Column Name is retrieved
         * @return String Column Name
         */
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }
        
        /** get the Column Count
         * @return int Column Count
         */
        public int getColumnCount() {
            return columnNames.length;
        }
        
        
        /** get Value at rowIndex columnIndex
         * @param rowIndex int rowIndex
         * @param columnIndex int columnIndex
         * @return Object
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) cvRoutingDetailsBean.get(rowIndex);
            switch(columnIndex) {
                case USER_ID_COLUMN :
                    if(routingDetailsBean != null ){
                        return routingDetailsBean;
                    } else
                        return null;
            }
            return null;
        }
        
        /** set Object at rowIndex columnIndex of table
         * @param value Object to set
         * @param rowIndex int rowIndex
         * @param columnIndex int columnIndex
         */
        public void setValueAt(Object value, int rowIndex,int columnIndex) {
            
            RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) cvRoutingDetailsBean.get(rowIndex);
            RoutingPanelRow eachPanelRow = (RoutingPanelRow) vecPanelRows.get(rowIndex);
            
            switch(columnIndex) {
                case USER_ID_COLUMN :
                    if(value != null) {
                        eachPanelRow.setRowIcon(eachPanelRow.getRowIcon());
                        eachPanelRow.setUserID(routingDetailsBean.getUserId(),eachPanelRow.getUserIdAlignment(), eachPanelRow.getLabelColor(), eachPanelRow.getLabelFont() );
                        eachPanelRow.setUserName(routingDetailsBean.getUserName());
                        eachPanelRow.setApprovalStatus(routingDetailsBean.getApprovalStatus());
                        if(routingDetailsBean.getApprovalDate() != null){
                            eachPanelRow.setTimeStamp(routingDetailsBean.getApprovalDate()+EMPTY_STRING);
                        }
                        eachPanelRow.repaint();
                    }
            }
            routingTableModel.fireTableDataChanged();
        }
        
        
    }//End of Class
    
    /**
     * Method used to check a Sequential Stop bean in CoeusVector of ProposalApprovalBean 
     * that may contain a Bean for Sequential Stop added in addSequentialStops() method in 
     * ProposalRoutingForm.java for the Table in ProposalRoutingForm
     * It checks the approvalStatus Status flag "Z" assigned to such Bean as identifier
     * return true if such bean is found
     */
    
    private boolean findSquentialStopBean(CoeusVector cvRoutingDetailsBean){
        if(cvRoutingDetailsBean != null && cvRoutingDetailsBean.size() > 0) {
            
            Equals equalSequentialID = new Equals("approvalStatus","Z");
            if(cvRoutingDetailsBean.filter(equalSequentialID).size() > 0){
                return true;
            }
        } else
            return true;
        
        return false;
    }
    
}// End of main Class
