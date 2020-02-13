/*
 * PendingSupportForm.java
 *
 * Created on November 3, 2004, 11:42 AM
 */
/*
 * PMD check performed, and commented unused imports and variables on 19-APR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.instprop.bean.InstituteProposalCustomDataBean;
//import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.departmental.bean.DepartmentPendingReportBean;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.gui.event.BlockingGlassPane;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.Component;
/**
 *
 * @author  chandrashekara
 */
public class PendingSupportForm extends javax.swing.JComponent  {
    
    // Column Index for the Pending table
    private static final String EMPTY_STRING = "";
    private static final int PENDING_PROPOSAL_NO = 0;
    private static final int PENDING_AGENY_COLUMN = 1;
    private static final int PENDING_PI_COLUMN = 2;
    private static final int PENDING_TITLE_COLUMN=3;
    private static final int PENDING_DIRECT_COST_COLUMN = 4;
    private static final int PENDING_INDIRECT_COST_COLUMN=5;
    private static final int PENDING_TOTAL_REQUESTED_COLUMN=6;
    private static final int PENDING_EFFECTIVE_DATE_COLUMN = 7;
    private static final int PENDING_END_DATE_COLUMN=8;
    //New columns added with 3505:Add % effort to current and pending support:start
    private static final int CURRENT_PERCENTAGE_EFFORT_COLUMN=9;
    private static final int CURRENT_ACADEMIC_EFFORT_COLUMN=10;
    private static final int CURRENT_SUMMER_EFFORT_COLUMN=11;
    private static final int CURRENT_CALENDAR_EFFORT_COLUMN=12;
    //3505 end
    
    private CoeusVector pendingData;
    private PendingTableModel pendingTableModel;
    private PendingTableCellRenderer pendingTableCellRenderer;
    private  DateUtils dtUtils = new DateUtils();
    private static final String REQUIRED_DATE_FORMAT = "dd-MMM-yyyy";
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    private CoeusVector tempVector = new CoeusVector();
    private String headerValue;
    private static BlockingGlassPane blockingGlassPane;
    private JTable tblTest;
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    private static final int DEFAULT_STATIC_COLUMNS = 13;
    Object [] uniqueColumnName;
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End

    /** Creates new form PendingSupportForm */
    public PendingSupportForm() {
        initComponents();
        scrPnPendingSupport.getViewport().setBackground(Color.white);
        pnlHeader.setBackground(Color.white);
    }
    // @returns the current component
    public Component getControlledUI(){
        return this;
    }
     // Used to instanitiate table model, renderer and setting the table values
    public void registerModels(){
        pendingTableModel = new PendingTableModel();
        pendingTableCellRenderer = new PendingTableCellRenderer();
        tblPendingSupport.setModel(pendingTableModel);
        pendingTableModel.setData(pendingData);
    }
    
     // Sets the header properticies and sets the column height and width.
    public void setColumnData(){
        JTableHeader pendingTableHeader = tblPendingSupport.getTableHeader();
        pendingTableHeader.addMouseListener(new ColumnHeaderListener());
        pendingTableHeader.setReorderingAllowed(false);
        pendingTableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        tblPendingSupport.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPendingSupport.setRowHeight(22);
        tblPendingSupport.setSelectionBackground(java.awt.Color.white);
        tblPendingSupport.setSelectionForeground(java.awt.Color.black);
        tblPendingSupport.setShowHorizontalLines(true);
        tblPendingSupport.setShowVerticalLines(true);
        tblPendingSupport.setOpaque(false);
        
        //Modified with 3505:Add % effort to current and pending support:start
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        /*String [] colNames = {"<html>Proposal <br>No</html>","Agency","PI",
                "Title","<html>Total <br>Direct Cost</html>", "<html>Total <br>Indirect Cost</html>",
                "<html>Total <br>Requested</html>","<html>Effective <br>Date</html>", "End Date","Effort %",
                "<html>Academic <br>Year Effort%</html>", "<html>Summer <br>Year Effort%</html>",
                "<html>Calendar<br>Year Effort%</html>"};*/        
        
        String [] colNames = addCustomDataColumnNames();
        int noOfColumns = colNames.length;
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        
        TableColumn column = tblPendingSupport.getColumnModel().getColumn(PENDING_PROPOSAL_NO);
        column.setPreferredWidth(57);
        column.setResizable(true);
        column.setHeaderValue(colNames[PENDING_PROPOSAL_NO]);
        
        column = tblPendingSupport.getColumnModel().getColumn(PENDING_AGENY_COLUMN);
        column.setPreferredWidth(60);
        column.setResizable(true);
        column.setHeaderValue(colNames[PENDING_AGENY_COLUMN]);
        
        column = tblPendingSupport.getColumnModel().getColumn(PENDING_PI_COLUMN);
        column.setPreferredWidth(78);
        column.setResizable(true);
        column.setCellRenderer(pendingTableCellRenderer);
        column.setHeaderValue(colNames[PENDING_PI_COLUMN]);
        
        column = tblPendingSupport.getColumnModel().getColumn(PENDING_TITLE_COLUMN);
        column.setPreferredWidth(130);
        column.setResizable(true);
        column.setHeaderValue(colNames[PENDING_TITLE_COLUMN]);
        
        column = tblPendingSupport.getColumnModel().getColumn(PENDING_DIRECT_COST_COLUMN);
        column.setPreferredWidth(85);
        column.setCellRenderer(pendingTableCellRenderer);
        column.setHeaderValue(colNames[PENDING_DIRECT_COST_COLUMN]);
        
        column = tblPendingSupport.getColumnModel().getColumn(PENDING_INDIRECT_COST_COLUMN);
        column.setPreferredWidth(88);
        column.setCellRenderer(pendingTableCellRenderer);
        column.setHeaderValue(colNames[PENDING_INDIRECT_COST_COLUMN]);
        
        column = tblPendingSupport.getColumnModel().getColumn(PENDING_TOTAL_REQUESTED_COLUMN);
        column.setPreferredWidth(85);
        column.setResizable(true);
        column.setCellRenderer(pendingTableCellRenderer);
        column.setHeaderValue(colNames[PENDING_TOTAL_REQUESTED_COLUMN]);
        
        column = tblPendingSupport.getColumnModel().getColumn(PENDING_EFFECTIVE_DATE_COLUMN);
        column.setPreferredWidth(70);
        column.setResizable(true);
        column.setCellRenderer(pendingTableCellRenderer);
        column.setHeaderValue(colNames[PENDING_EFFECTIVE_DATE_COLUMN]);
        
        column = tblPendingSupport.getColumnModel().getColumn(PENDING_END_DATE_COLUMN);
        column.setPreferredWidth(70);
        column.setResizable(true);
        column.setCellRenderer(pendingTableCellRenderer);
        column.setHeaderValue(colNames[PENDING_END_DATE_COLUMN]);
        
        column = tblPendingSupport.getColumnModel().getColumn(CURRENT_PERCENTAGE_EFFORT_COLUMN);
        column.setPreferredWidth(60);
        column.setResizable(true);
        column.setCellRenderer(pendingTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_PERCENTAGE_EFFORT_COLUMN]);
        
        column = tblPendingSupport.getColumnModel().getColumn(CURRENT_ACADEMIC_EFFORT_COLUMN);
        column.setPreferredWidth(86);
        column.setResizable(true);
        column.setCellRenderer(pendingTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_ACADEMIC_EFFORT_COLUMN]);
        
        column = tblPendingSupport.getColumnModel().getColumn(CURRENT_SUMMER_EFFORT_COLUMN);
        column.setPreferredWidth(86);
        column.setResizable(true);
        column.setCellRenderer(pendingTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_SUMMER_EFFORT_COLUMN]);
        
        column = tblPendingSupport.getColumnModel().getColumn(CURRENT_CALENDAR_EFFORT_COLUMN);
        column.setPreferredWidth(86);
        column.setResizable(true);
        column.setCellRenderer(pendingTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_CALENDAR_EFFORT_COLUMN]);
        //3505 : end
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        for(int colNamesCount=DEFAULT_STATIC_COLUMNS; colNamesCount < noOfColumns;colNamesCount++){
            //To check if there is any custom elements data
            if(colNamesCount > DEFAULT_STATIC_COLUMNS){
                column = tblPendingSupport.getColumnModel().getColumn(colNamesCount);
                column.setPreferredWidth(86);
                column.setResizable(true);
                column.setCellRenderer(pendingTableCellRenderer);
                column.setHeaderValue(colNames[colNamesCount]);
            }
        }
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
    }
    
    
    // Utility method used to get the hour glass Icon untill the required  process complete
     public void blockEvents(boolean block) {
        if(blockingGlassPane == null) {
            blockingGlassPane = new BlockingGlassPane();
            CoeusGuiConstants.getMDIForm().setGlassPane(blockingGlassPane);
        }
        blockingGlassPane.block(block);
    }
    
        /** This class will sort the column values in ascending and descending order
            *based on number of clicks. 
        */
    
    public class ColumnHeaderListener extends java.awt.event.MouseAdapter {
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        /*String nameBeanId [][] ={
            {"0","proposalNumber" },
            {"1","sponsorName" },
            {"2","principleInvestigator"},
            {"3","title" },
            {"4","totalDirectCostTotal"},
            {"5","totalIndirectCostTotal"},
            {"6","totalCost" },
            {"7","requestedStartDateTotal" },
            {"8","requestEndDateTotal" },
            //Modified with case 505:Add % effort to current and pending support:start
            {"9","percentageEffort" },
            {"10","academicYearEffort" },
            {"11","summerYearEffort" },
            {"12","calendarYearEffort" }
            //3505:end
        };*/
                
        String nameBeanId [][] = addCustomDataColumnSorting();
        
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        boolean sort =true;
        /**
         * @param evt
         */        
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            try {
                blockEvents(true);
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
               // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(pendingData!=null && pendingData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)pendingData).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    pendingTableModel.fireTableRowsUpdated(0, pendingTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.printStackTrace();
                exception.getMessage();
            }
            finally{
                blockEvents(false);
            }
        }
    }// End of ColumnHeaderListener.................
    
     public class PendingTableModel extends AbstractTableModel{
        
        //Modified with case 3505:Add % effort to current and pending support:start
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        /*private String [] colNames = {"Proposal No","Agency","PI",
                "Title","Total Direct Cost", "Total Indirect Cost",
                "Total Requested","Effective Date", "End Date","Effort %",
                "Academic Year Effort%", "Summer Year Effort%",
                "Calendar Year Effort%"};*/
        public String [] colNames= addCustomDataColumnNames();
        
        private Object uniqueColumnName [] = customDataColumns();
        /*private Class[] colClass={String.class,String.class,String.class,String.class,
        Double.class,Double.class,Double.class,String.class,String.class
                , Float.class, Float.class, Float.class, Float.class};*/
        //Case 3505:end
        public Class [] colClass= addCustomDataColumnDataTypes();
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public String getColumnName(int col){
            return colNames[col];
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        public void setData(CoeusVector pendingData){
            pendingData = pendingData;
        }
        
        public int getRowCount() {
            if(pendingData== null){
                return 0;
            }else{
                return pendingData.size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            DepartmentPendingReportBean pendingBean =
            (DepartmentPendingReportBean)pendingData.get(row);
            //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
            HashMap hmCustomDataElements = getCustomDataElements(pendingBean);
            //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
            switch(col){
                case PENDING_PROPOSAL_NO:
                    return pendingBean.getProposalNumber();
                case PENDING_AGENY_COLUMN:
                    return pendingBean.getSponsorName();
                case PENDING_PI_COLUMN:
                    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                    //return pendingBean.getPrincipleInvestigator();
                    if(pendingBean.getPrincipleInvestigator()!=null){
                        return pendingBean.getPrincipleInvestigator();
                    }else{
                        return "Key-Per";
                    }
                    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                case PENDING_TITLE_COLUMN:
                    return pendingBean.getTitle();
                case PENDING_DIRECT_COST_COLUMN:
                    return  new Double(pendingBean.getTotalDirectCostTotal());
                case PENDING_INDIRECT_COST_COLUMN:
                    return  new Double(pendingBean.getTotalIndirectCostTotal());
                case PENDING_TOTAL_REQUESTED_COLUMN:
                    return  new Double(pendingBean.getTotalCost());
                case PENDING_EFFECTIVE_DATE_COLUMN:
                    return pendingBean.getRequestedStartDateTotal();
                case PENDING_END_DATE_COLUMN:
                    return pendingBean.getRequestEndDateTotal();
               //Modified with case 3505:Add % effort to current and pending support:start
                case CURRENT_PERCENTAGE_EFFORT_COLUMN:
                    return new Float(pendingBean.getPercentageEffort());
                case CURRENT_ACADEMIC_EFFORT_COLUMN:
                    return new Float(pendingBean.getAcademicYearEffort());
                case CURRENT_SUMMER_EFFORT_COLUMN:
                    return new Float(pendingBean.getSummerYearEffort());
                case CURRENT_CALENDAR_EFFORT_COLUMN:
                    return new Float(pendingBean.getCalendarYearEffort());
                    //3505:end
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                default:
                    String columnName = (String)uniqueColumnName[col-DEFAULT_STATIC_COLUMNS];
                    if(columnName!=null){
                        return hmCustomDataElements.get(columnName);
                    }
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
            }
            return EMPTY_STRING;
        }
     }
     
     /** Customized renderer classfor the table where formats are provided for the
      *required components
      */
     public class PendingTableCellRenderer extends DefaultTableCellRenderer{
         private DollarCurrencyTextField txtComponent;
         private CoeusTextField txtDateField,txtComp;
         //Added with case 3505 - Add percentage effort fields for current support - start
         private CurrencyField txtEffort;
         private JLabel lblEffort;
        //Added with case 3505 - Add percentage effort fields for current support - end
         public PendingTableCellRenderer(){
             txtComponent = new DollarCurrencyTextField();
             txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
             txtDateField = new CoeusTextField();
             txtDateField.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
             txtComp = new CoeusTextField();
             txtComp.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
             //Added with case 3505 - Add percentage effort fields for current support - start
             txtComp.setHorizontalAlignment(JTextField.CENTER);
             txtEffort = new CurrencyField();
             txtEffort.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            //Added with case 3505 - Add percentage effort fields for current support - end
         }
         public Component getTableCellRendererComponent(JTable table, Object value,
         boolean isSelected, boolean hasFocus, int row,int column) {
             switch(column){
                 case PENDING_TOTAL_REQUESTED_COLUMN:
                 case PENDING_DIRECT_COST_COLUMN:
                 case PENDING_INDIRECT_COST_COLUMN:
                     txtComponent.setValue(new Double(value.toString()).doubleValue());
                     return txtComponent;
                 case PENDING_EFFECTIVE_DATE_COLUMN:
                 case PENDING_END_DATE_COLUMN:
                     if(value!= null && (!value.toString().trim().equals(EMPTY_STRING))){
                         value = dtUtils.formatDate(value.toString(),REQUIRED_DATE_FORMAT);
                         txtDateField.setText(value.toString());
                     }else{
                         txtDateField.setText(EMPTY_STRING);
                     }
                     return txtDateField;
                 case PENDING_PI_COLUMN:                     
                     txtComp.setText(value.toString());
                     return txtComp;
//                Added with case 3505 - Add percentage effort fields for current support - start
                case CURRENT_PERCENTAGE_EFFORT_COLUMN:
                case CURRENT_ACADEMIC_EFFORT_COLUMN:
                case CURRENT_SUMMER_EFFORT_COLUMN:
                case CURRENT_CALENDAR_EFFORT_COLUMN:
                    txtEffort.setText(value.toString());
                    return txtEffort;
//               Added with case 3505 - Add percentage effort fields for current support - end
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                default:
                    if(value == null){
                        value = CoeusGuiConstants.EMPTY_STRING;
                    }
                    txtComp.setText(value.toString());
                    return txtComp; 
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
             }
             //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
             //return txtComponent;
             //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
         }
     }
     
     //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
     /** This method will add the custom data column values from all the datas and store them 
     *in a array
     *@return Object[]
     */
     public Object[] customDataColumns(){
         HashSet uniqueColumnData = new HashSet();
         for(Object singlePendingData : pendingData){
             DepartmentPendingReportBean pendingBean = (DepartmentPendingReportBean)singlePendingData;
             CoeusVector cvCustomData = pendingBean.getCustomElements();
             if(cvCustomData!=null){
                 for(Object cvCustomDataBean : cvCustomData){
                     InstituteProposalCustomDataBean instituteProposalCustomDataBean = (InstituteProposalCustomDataBean)cvCustomDataBean;                     
                     if(instituteProposalCustomDataBean.getGroupCode()!=null && instituteProposalCustomDataBean.getGroupCode().equals(pendingBean.getGroupCode())){
                         String columnName = instituteProposalCustomDataBean.getColumnName();
                         uniqueColumnData.add(columnName);
                     }
                 }
             }
         }
         //To store the unique data in to Object array
         Object [] uniqueData = uniqueColumnData.toArray();
         return uniqueData;
     }
     
     /** This method will add the column values from all the datas and store them 
     *in a array
     *@return String[]
     */
     public String[] addCustomDataColumnNames(){
         uniqueColumnName = customDataColumns();
         HashMap hmCustomDataColumnNames = getCustomDataColumnNames();
         String [] colNames = new String[DEFAULT_STATIC_COLUMNS+uniqueColumnName.length];
         for(int colNamesCount = 0;colNamesCount < DEFAULT_STATIC_COLUMNS+uniqueColumnName.length;colNamesCount++){
             if(colNamesCount>12){
                 colNames[colNamesCount] = (String)hmCustomDataColumnNames.get((String)
                                    uniqueColumnName[colNamesCount-DEFAULT_STATIC_COLUMNS]);
             }else{
                 switch(colNamesCount){
                     case PENDING_PROPOSAL_NO:
                         colNames[colNamesCount]="<html>Proposal <br>No</html>";
                         break;
                     case PENDING_AGENY_COLUMN:
                         colNames[colNamesCount]="Agency";
                         break;
                     case PENDING_PI_COLUMN:
                         colNames[colNamesCount]="PI/Key-Per";
                         break;
                     case PENDING_TITLE_COLUMN:
                         colNames[colNamesCount]="Title";
                         break;
                     case PENDING_DIRECT_COST_COLUMN:
                         colNames[colNamesCount]="<html>Total <br>Direct Cost</html>";
                         break;
                     case PENDING_INDIRECT_COST_COLUMN:
                         colNames[colNamesCount]="<html>Total <br>Indirect Cost</html>";
                         break;
                     case PENDING_TOTAL_REQUESTED_COLUMN:
                         colNames[colNamesCount]="<html>Total <br>Requested</html>";
                         break;
                     case PENDING_EFFECTIVE_DATE_COLUMN:
                         colNames[colNamesCount]="<html>Effective <br>Date</html>";
                         break;
                     case PENDING_END_DATE_COLUMN:
                         colNames[colNamesCount]="End Date";
                         break;
                     case CURRENT_PERCENTAGE_EFFORT_COLUMN:
                         colNames[colNamesCount]="Effort %";
                         break;
                     case CURRENT_ACADEMIC_EFFORT_COLUMN:
                         colNames[colNamesCount]="<html>Academic <br>Year Effort%</html>";
                         break;
                     case CURRENT_SUMMER_EFFORT_COLUMN:
                         colNames[colNamesCount]="<html>Summer <br>Year Effort%</html>";
                         break;
                     case CURRENT_CALENDAR_EFFORT_COLUMN:
                         colNames[colNamesCount]="<html>Calendar<br>Year Effort%</html>";
                         break;
                 }
             }
         }
         return colNames;
     }
     
     /** This method will add the column class values from all the datas and store them 
     *in a array
     *@return String[]
     */
     public Class[] addCustomDataColumnDataTypes(){
         uniqueColumnName = customDataColumns();
         Class [] colClass = new Class[DEFAULT_STATIC_COLUMNS+uniqueColumnName.length];
         for(int colNamesCount = 0;colNamesCount < DEFAULT_STATIC_COLUMNS+uniqueColumnName.length;colNamesCount++){
             if(colNamesCount>12){
                 colClass[colNamesCount] = String.class;
             }else{
                 switch(colNamesCount){
                     case PENDING_PROPOSAL_NO:
                         colClass[colNamesCount]=String.class;
                         break;
                     case PENDING_AGENY_COLUMN:
                         colClass[colNamesCount]=String.class;
                         break;
                     case PENDING_PI_COLUMN:
                         colClass[colNamesCount]=String.class;
                         break;
                     case PENDING_TITLE_COLUMN:
                         colClass[colNamesCount]=String.class;
                         break;
                     case PENDING_DIRECT_COST_COLUMN:
                         colClass[colNamesCount]=Double.class;
                         break;
                     case PENDING_INDIRECT_COST_COLUMN:
                         colClass[colNamesCount]=Double.class;
                         break;
                     case PENDING_TOTAL_REQUESTED_COLUMN:
                         colClass[colNamesCount]=Double.class;
                         break;
                     case PENDING_EFFECTIVE_DATE_COLUMN:
                         colClass[colNamesCount]=String.class;
                         break;
                     case PENDING_END_DATE_COLUMN:
                         colClass[colNamesCount]=String.class;
                         break;
                     case CURRENT_PERCENTAGE_EFFORT_COLUMN:
                         colClass[colNamesCount]=Float.class;
                         break;
                     case CURRENT_ACADEMIC_EFFORT_COLUMN:
                         colClass[colNamesCount]=Float.class;
                         break;
                     case CURRENT_SUMMER_EFFORT_COLUMN:
                         colClass[colNamesCount]=Float.class;
                         break;
                     case CURRENT_CALENDAR_EFFORT_COLUMN:
                         colClass[colNamesCount]=Float.class;
                         break;
                 }
             }
         }
         return colClass;
     }
     
     /** This method will add the unique column header values from all the datas and store them 
     *in a array
     *@return String[][]
     */
     public String[][] addCustomDataColumnSorting(){        
        uniqueColumnName = customDataColumns();
        String [] [] nameBeanId = new String[DEFAULT_STATIC_COLUMNS+uniqueColumnName.length][2];
        for(int counter =0;counter<=1;counter++){
            for(int colNamesCount = 0;colNamesCount < DEFAULT_STATIC_COLUMNS+uniqueColumnName.length;colNamesCount++){
                if(counter>0){
                    if(colNamesCount>12){
                        nameBeanId[colNamesCount][counter] = (String)uniqueColumnName[colNamesCount-DEFAULT_STATIC_COLUMNS];
                    }else{
                        switch(colNamesCount){
                            case PENDING_PROPOSAL_NO:
                                nameBeanId[colNamesCount][counter]="proposalNumber";
                                break;
                            case PENDING_AGENY_COLUMN:
                                nameBeanId[colNamesCount][counter]="sponsorName";
                                break;
                            case PENDING_PI_COLUMN:
                                nameBeanId[colNamesCount][counter]="principleInvestigator";
                                break;
                            case PENDING_TITLE_COLUMN:
                                nameBeanId[colNamesCount][counter]="title";
                                break;
                            case PENDING_DIRECT_COST_COLUMN:
                                nameBeanId[colNamesCount][counter]="totalDirectCostTotal";
                                break;
                            case PENDING_INDIRECT_COST_COLUMN:
                                nameBeanId[colNamesCount][counter]="totalIndirectCostTotal";
                                break;
                            case PENDING_TOTAL_REQUESTED_COLUMN:
                                nameBeanId[colNamesCount][counter]="totalCost";
                                break;
                            case PENDING_EFFECTIVE_DATE_COLUMN:
                                nameBeanId[colNamesCount][counter]="requestedStartDateTotal";
                                break;
                            case PENDING_END_DATE_COLUMN:
                                nameBeanId[colNamesCount][counter]="requestEndDateTotal";
                                break;
                            case CURRENT_PERCENTAGE_EFFORT_COLUMN:
                                nameBeanId[colNamesCount][counter]="percentageEffort";
                                break;
                            case CURRENT_ACADEMIC_EFFORT_COLUMN:
                                nameBeanId[colNamesCount][counter]="academicYearEffort";
                                break;
                            case CURRENT_SUMMER_EFFORT_COLUMN:
                                nameBeanId[colNamesCount][counter]="summerYearEffort";
                                break;
                            case CURRENT_CALENDAR_EFFORT_COLUMN:
                                nameBeanId[colNamesCount][counter]="calendarYearEffort";
                                break;
                        }
                    }
                }else{
                    nameBeanId[colNamesCount][counter] = new String(""+colNamesCount);
                }
            }
        }
        return nameBeanId;
    }
    
     /** This method will fetch the column values for all the datas and store them 
     *in a HashMap
     *@param DepartmentCurrentReportBean
     *@return HashMap
     */
     private HashMap getCustomDataElements(DepartmentPendingReportBean pendingBean){
         CoeusVector cvCustomData = pendingBean.getCustomElements();
         HashMap hmCustomData = new HashMap();
         if(cvCustomData!=null){
             for(Object cvCustomDataBean : cvCustomData){
                 InstituteProposalCustomDataBean instituteProposalCustomDataBean = (InstituteProposalCustomDataBean)cvCustomDataBean;
                 if(instituteProposalCustomDataBean.getGroupCode()!=null && instituteProposalCustomDataBean.getGroupCode().equals(pendingBean.getGroupCode())){
                     hmCustomData.put(instituteProposalCustomDataBean.getColumnName(),instituteProposalCustomDataBean.getColumnValue());
                 }
             }
         }
         return hmCustomData;
     }
     
     /** This method will fetch the column namea and labels for all the datas and store them 
     *in a HashMap     
     *@return HashMap
     */
    private HashMap getCustomDataColumnNames(){
        HashMap hmCustomColumnLabel = new HashMap();
        for(Object singlePendingData : pendingData){
             DepartmentPendingReportBean pendingBean = (DepartmentPendingReportBean)singlePendingData;
             CoeusVector cvCustomData = pendingBean.getCustomElements();
             if(cvCustomData!=null){
                 for(Object cvCustomDataBean : cvCustomData){
                     InstituteProposalCustomDataBean instituteProposalCustomDataBean = (InstituteProposalCustomDataBean)cvCustomDataBean;                     
                     if(instituteProposalCustomDataBean.getGroupCode()!=null && instituteProposalCustomDataBean.getGroupCode().equals(pendingBean.getGroupCode())){
                         hmCustomColumnLabel.put(instituteProposalCustomDataBean.getColumnName(),instituteProposalCustomDataBean.getColumnLabel());
                     }
                 }
             }
         }
        return hmCustomColumnLabel;
    }
     //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
     
     /** This method is called from within the constructor to
      * initialize the form.
      * WARNING: Do NOT modify this code. The content of this method is
      * always regenerated by the Form Editor.
      */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnPendingSupport = new javax.swing.JScrollPane();
        tblPendingSupport = new javax.swing.JTable();
        pnlHeader = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        scrPnPendingSupport.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrPnPendingSupport.setMinimumSize(new java.awt.Dimension(988, 518));
        scrPnPendingSupport.setPreferredSize(new java.awt.Dimension(988, 518));
        tblPendingSupport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrPnPendingSupport.setViewportView(tblPendingSupport);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(scrPnPendingSupport, gridBagConstraints);

        pnlHeader.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblHeader.setFont(CoeusFontFactory.getLabelFont());
        lblHeader.setText("Pending Support for");
        pnlHeader.add(lblHeader);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(pnlHeader, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /** Getter for property pendingData.
     * @return Value of property pendingData.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getPendingData() {
        return pendingData;
    }    
    
    /** Setter for property pendingData.
     * @param pendingData New value of property pendingData.
     *
     */
    public void setPendingData(edu.mit.coeus.utils.CoeusVector pendingData) {
        this.pendingData = pendingData;
    } 
    
    /** Getter for property headerValue.
     * @return Value of property headerValue.
     *
     */
    public java.lang.String getHeaderValue() {
        return headerValue;
    }
    
    /** Setter for property headerValue.
     * @param headerValue New value of property headerValue.
     *
     */
    public void setHeaderValue(java.lang.String headerValue) {
        this.headerValue = headerValue;
        lblHeader.setText("<html><u>Pending Support for "+headerValue+" </u></html>");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel lblHeader;
    public javax.swing.JPanel pnlHeader;
    public javax.swing.JScrollPane scrPnPendingSupport;
    public javax.swing.JTable tblPendingSupport;
    // End of variables declaration//GEN-END:variables
    
}
