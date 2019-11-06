/*
 * CurrentAndPendingForm.java
 *
 * Created on November 3, 2004, 10:29 AM
 */
/*
 * PMD check performed, and commented unused imports and variables on 19-APR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.award.bean.AwardCustomDataBean;
//import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
//import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.departmental.bean.DepartmentCurrentReportBean;
import edu.mit.coeus.gui.event.BlockingGlassPane;
//import edu.mit.coeus.utils.dbengine.DBException;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.Component;
import edu.mit.coeus.utils.DollarCurrencyTextField;
/**
 *
 * @author  chandrashekara
 */

public class CurrentSupportForm extends javax.swing.JComponent {
    private static final String REQUIRED_DATE_FORMAT = "dd-MMM-yyyy";
    private  DateUtils dtUtils = new DateUtils();
    private static final String EMPTY_STRING = "";
    // Column indicies for the current table
    private static final int CURRENT_SPONSOR_AWARD_NO_COLUMN = 0;
    private static final int CURRENT_AGENY_COLUMN = 1;
    private static final int CURRENT_PI_COLUMN = 2;
    private static final int CURRENT_TITLE_COLUMN=3;
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    /*private static final int CURRENT_AWARD_AMOUNT_COLUMN=4;
    private static final int CURRENT_EFFECTIVE_DATE_COLUMN = 5;
    private static final int CURRENT_END_DATE_COLUMN=6;
    //New columns added with 3505:Add % effort to current and pending support:start
    private static final int CURRENT_PERCENTAGE_EFFORT_COLUMN=7;
    private static final int CURRENT_ACADEMIC_EFFORT_COLUMN=8;
    private static final int CURRENT_SUMMER_EFFORT_COLUMN=9;
    private static final int CURRENT_CALENDAR_EFFORT_COLUMN=10;*/
    private static final int CURRENT_AWARD_DIRECT_AMOUNT_COLUMN=4;
    private static final int CURRENT_AWARD_INDIRECT_AMOUNT_COLUMN=5;
    private static final int CURRENT_AWARD_AMOUNT_COLUMN=6;
    private static final int CURRENT_EFFECTIVE_DATE_COLUMN = 7;
    private static final int CURRENT_END_DATE_COLUMN=8;
    //New columns added with 3505:Add % effort to current and pending support:start
    private static final int CURRENT_PERCENTAGE_EFFORT_COLUMN=9;
    private static final int CURRENT_ACADEMIC_EFFORT_COLUMN=10;
    private static final int CURRENT_SUMMER_EFFORT_COLUMN=11;
    private static final int CURRENT_CALENDAR_EFFORT_COLUMN=12;
    private static final int DEFAULT_STATIC_COLUMNS = 13;
    Object [] uniqueColumnName;
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
    //3505 end
    private CurrentTableModel currentTableModel;
    private CurrentTableCellRenderer currentTableCellRenderer;
    private CoeusVector currentData;
    private String headerValue;
    private BlockingGlassPane blockingGlassPane;
    
    /** Creates new form CurrentAndPendingForm */
    public CurrentSupportForm() {
        initComponents();
        jcrPnCurrentSupport.getViewport().setBackground(Color.white);
        pnlHeader.setBackground(Color.white);
    }
    // @returns the current component
    public Component getControlledUI(){
        return this;
    }
    // Used to instanitiate table model, renderer and setting the table values
    public void registerModels(){
        currentTableCellRenderer = new CurrentTableCellRenderer();
        currentTableModel = new CurrentTableModel();
        tblCurrentSupport.setModel(currentTableModel);
        currentTableModel.setData(currentData);
    }
    
    // Sets the header properticies and sets the column height and width.
    public void setColumnData(){
        JTableHeader currentTableHeader = tblCurrentSupport.getTableHeader();
        currentTableHeader.addMouseListener(new ColumnHeaderListener());
        currentTableHeader.setReorderingAllowed(false);
        currentTableHeader.setFont(CoeusFontFactory.getLabelFont());
                
        tblCurrentSupport.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblCurrentSupport.setRowHeight(22);
        tblCurrentSupport.setSelectionBackground(java.awt.Color.white);
        tblCurrentSupport.setSelectionForeground(java.awt.Color.black);
        tblCurrentSupport.setShowHorizontalLines(true);
        tblCurrentSupport.setShowVerticalLines(true);
        tblCurrentSupport.setOpaque(false);
        
        //Modified with 3505:Add % effort to current and pending support:start
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        /*String [] colNames = {"<html>Sponsor<br>Award No</html>","Agency","PI",
            "Title","<html>Award<br>Amount</html>", "<html>Effective<br>Date</html>", "End Date",
            "Effort %","<html>Academic <br>Year Effort%</html>", "<html>Summer <br>Year Effort%</html>",
                "<html>Calendar<br>Year Effort%</html>" };*/
        
        String [] colNames = addCustomDataColumnNames();
        int noOfColumns = colNames.length;
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        
        TableColumn column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_SPONSOR_AWARD_NO_COLUMN);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        //column.setPreferredWidth(115);
        column.setPreferredWidth(57);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        column.setResizable(true);
        column.setHeaderValue(colNames[CURRENT_SPONSOR_AWARD_NO_COLUMN]);
        
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_AGENY_COLUMN);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        //column.setPreferredWidth(100);
        column.setPreferredWidth(60);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        column.setResizable(true);
        column.setHeaderValue(colNames[CURRENT_AGENY_COLUMN]);
        
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_PI_COLUMN);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        //column.setPreferredWidth(30);
        column.setPreferredWidth(78);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_PI_COLUMN]);
        
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_TITLE_COLUMN);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        //column.setPreferredWidth(170);
        column.setPreferredWidth(130);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        column.setResizable(true);
        column.setHeaderValue(colNames[CURRENT_TITLE_COLUMN]);
        
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_AWARD_DIRECT_AMOUNT_COLUMN);
        column.setPreferredWidth(85);
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_AWARD_DIRECT_AMOUNT_COLUMN]);
        
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_AWARD_INDIRECT_AMOUNT_COLUMN);
        column.setPreferredWidth(92);
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_AWARD_INDIRECT_AMOUNT_COLUMN]);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_AWARD_AMOUNT_COLUMN);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        //column.setPreferredWidth(100);
        column.setPreferredWidth(85);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_AWARD_AMOUNT_COLUMN]);
        
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_EFFECTIVE_DATE_COLUMN);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        //column.setPreferredWidth(70);
        column.setPreferredWidth(70);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_EFFECTIVE_DATE_COLUMN]);
        
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_END_DATE_COLUMN);
        column.setPreferredWidth(70);
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_END_DATE_COLUMN]);
        
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_PERCENTAGE_EFFORT_COLUMN);
        column.setPreferredWidth(60);
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_PERCENTAGE_EFFORT_COLUMN]);
        
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_ACADEMIC_EFFORT_COLUMN);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        //column.setPreferredWidth(90);
        column.setPreferredWidth(86);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_ACADEMIC_EFFORT_COLUMN]);
        
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_SUMMER_EFFORT_COLUMN);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        //column.setPreferredWidth(90);
        column.setPreferredWidth(86);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_SUMMER_EFFORT_COLUMN]);
        
        column = tblCurrentSupport.getColumnModel().getColumn(CURRENT_CALENDAR_EFFORT_COLUMN);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        //column.setPreferredWidth(90);
        column.setPreferredWidth(86);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
        column.setHeaderValue(colNames[CURRENT_CALENDAR_EFFORT_COLUMN]);
        //3505 end
        
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        for(int colNamesCount=DEFAULT_STATIC_COLUMNS; colNamesCount < noOfColumns;colNamesCount++){
            //To check if there is any custom elements data
            if(colNamesCount > DEFAULT_STATIC_COLUMNS){
                column = tblCurrentSupport.getColumnModel().getColumn(colNamesCount);
                column.setPreferredWidth(86);
                column.setResizable(true);
                column.setCellRenderer(currentTableCellRenderer);
                column.setHeaderValue(colNames[colNamesCount]);
            }
        }
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
    }
    
    // Utility method used to get the hour glass Icon untill the process complete
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
            {"0","sponsorAwardNumber" },
            {"1","sponsorName" },
            {"2","prinicipleInvestigator"},
            {"3","title" },
            {"4","obliDistrubutableAmount"},
            {"5","awardEffectiveDate"},
            {"6","finalExpirationDate" },
            //added with 3505:Add % effort to current and pending support:start
            {"7","percentageEffort" },
            {"8","academicYearEffort" },
            {"9","summerYearEffort" },
            {"10","calendarYearEffort" }
            //3505 end
        };*/
                
        String nameBeanId [][] = addCustomDataColumnSorting();
        
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        boolean sort =true;
        public void mouseClicked(java.awt.event.MouseEvent evt) {
           try {
               if(evt.getClickCount()!= 1) return ;
                blockEvents(true);
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
               // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(currentData!=null && currentData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)currentData).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    currentTableModel.fireTableRowsUpdated(0, currentTableModel.getRowCount());
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
        

    
    /** Helper class which will design the model for the Current details
     *of the window. Holds the data related to the current support for the person
     */
    public class CurrentTableModel extends AbstractTableModel{
        //added with case 3505:Add % effort to current and pending support:start
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        /*private String [] colNames = {"Sponsor Award No","Agency","PI",
            "Title","Award Amount", "Effective Date", "End Date",
            "Effort %","Academic Year Effort%", "Summer Year Effort%",
                "Calendar Year Effort%" };*/
        
        public String [] colNames= addCustomDataColumnNames();
        
        private Object uniqueColumnName [] = customDataColumns();
        /*private Class[] colClass={String.class,String.class,String.class,String.class,
        Double.class,String.class,String.class, Float.class, Float.class, Float.class, Float.class};*/
        //3505 end
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
        public void setData(CoeusVector currentData){
            currentData = currentData;
        }
        
        public int getRowCount() {
            if(currentData== null){
                return 0;
            }else{
                return currentData.size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            DepartmentCurrentReportBean currentBean =
            (DepartmentCurrentReportBean)currentData.get(row);
            //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
            HashMap hmCustomDataElements = getCustomDataElements(currentBean);
            //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
            switch(col){
                case CURRENT_SPONSOR_AWARD_NO_COLUMN:
                    return currentBean.getSponsorAwardNumber();
                case CURRENT_AGENY_COLUMN:
                    return currentBean.getSponsorName();
                case CURRENT_PI_COLUMN:
                    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                    //return currentBean.getPrinicipleInvestigator();
                    if(currentBean.getPrinicipleInvestigator()!=null){
                        return currentBean.getPrinicipleInvestigator();
                    }else{
                        return "Key-Per";
                    }
                    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                case CURRENT_TITLE_COLUMN:
                    return currentBean.getTitle();
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                case CURRENT_AWARD_DIRECT_AMOUNT_COLUMN:
                    return  new Double(currentBean.getObliDirectAmount());
                case CURRENT_AWARD_INDIRECT_AMOUNT_COLUMN:
                    return  new Double(currentBean.getObliInDirectAmount());
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                case CURRENT_AWARD_AMOUNT_COLUMN:
                    return  new Double(currentBean.getObliDistrubutableAmount());
                case CURRENT_EFFECTIVE_DATE_COLUMN:
                    return currentBean.getAwardEffectiveDate();
                case CURRENT_END_DATE_COLUMN:
                    return currentBean.getFinalExpirationDate();
                //added with case 3505:Add % effort to current and pending support:start
                case CURRENT_PERCENTAGE_EFFORT_COLUMN:
                    return new Float(currentBean.getPercentageEffort());
                case CURRENT_ACADEMIC_EFFORT_COLUMN:
                    return new Float(currentBean.getAcademicYearEffort());
                case CURRENT_SUMMER_EFFORT_COLUMN:
                    return new Float(currentBean.getSummerYearEffort());
                case CURRENT_CALENDAR_EFFORT_COLUMN:
                    return new Float(currentBean.getCalendarYearEffort());                                
                    //3505 end
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
        
    }// End of CurrentTableModel
    
    
    // This is a overridden class used  set the renderer for the table
    public class CurrentTableCellRenderer extends DefaultTableCellRenderer{
        private DollarCurrencyTextField txtComponent;
        private CoeusTextField txtDatecomponent,txtComp;
        //Added with case 3505 - Add percentage effort fields for current support - start
        private CurrencyField txtEffort;
        private JLabel lblEffort;
        //Added with case 3505 - Add percentage effort fields for current support - end
        public CurrentTableCellRenderer(){
            txtComponent = new DollarCurrencyTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtComp = new CoeusTextField();
            txtComp.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtDatecomponent =  new CoeusTextField();
            txtDatecomponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            //Added with case 3505 - Add percentage effort fields for current support - start
            txtComp.setHorizontalAlignment(JTextField.CENTER);
            txtEffort = new CurrencyField();
            txtEffort.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            //Added with case 3505 - Add percentage effort fields for current support - end
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row,int column) {
            switch(column){
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                case CURRENT_AWARD_DIRECT_AMOUNT_COLUMN:
                    txtComponent.setValue(new Double(value.toString()).doubleValue());
                    return txtComponent;
                case CURRENT_AWARD_INDIRECT_AMOUNT_COLUMN:
                    txtComponent.setValue(new Double(value.toString()).doubleValue());
                    return txtComponent;
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                case CURRENT_AWARD_AMOUNT_COLUMN:
                    txtComponent.setValue(new Double(value.toString()).doubleValue());
                    return txtComponent;
                case CURRENT_EFFECTIVE_DATE_COLUMN:
                case CURRENT_END_DATE_COLUMN:
                    if(value!= null && (!value.toString().trim().equals(EMPTY_STRING))){
                        value = dtUtils.formatDate(value.toString(),REQUIRED_DATE_FORMAT);
                        txtDatecomponent.setText(value.toString());
                    }else{
                        txtDatecomponent.setText(EMPTY_STRING);
                    }
                    return txtDatecomponent;
                    
                case CURRENT_PI_COLUMN:                    
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
        for(Object singleCurrentData : currentData){
            DepartmentCurrentReportBean currentBean = (DepartmentCurrentReportBean)singleCurrentData;
            CoeusVector cvCustomData = currentBean.getCustomElements();
            if(cvCustomData!=null){
                for(Object cvCustomDataBean : cvCustomData){
                    AwardCustomDataBean awardCustomDataBean = (AwardCustomDataBean)cvCustomDataBean;
                    if(awardCustomDataBean.getGroupCode()!=null && awardCustomDataBean.getGroupCode().equals(currentBean.getGroupCode())){
                        String columnName = awardCustomDataBean.getColumnName();
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
        //To fetch the unique column names
        uniqueColumnName = customDataColumns();
        HashMap hmCustomDataColumnNames = getCustomDataColumnNames();
        String [] colNames = new String[DEFAULT_STATIC_COLUMNS+uniqueColumnName.length];
        for(int colNamesCount = 0;colNamesCount < DEFAULT_STATIC_COLUMNS+uniqueColumnName.length;colNamesCount++){
            if(colNamesCount>12){
                colNames[colNamesCount] = (String)hmCustomDataColumnNames.get((String)
                                    uniqueColumnName[colNamesCount-DEFAULT_STATIC_COLUMNS]);
            }else{
                switch(colNamesCount){
                    case CURRENT_SPONSOR_AWARD_NO_COLUMN:
                        colNames[colNamesCount]="<html>Sponsor<br>Award No</html>";
                        break;
                    case CURRENT_AGENY_COLUMN:
                        colNames[colNamesCount]="Agency";
                        break;
                    case CURRENT_PI_COLUMN:
                        colNames[colNamesCount]="PI/Key-Per";
                        break;
                    case CURRENT_TITLE_COLUMN:
                        colNames[colNamesCount]="Title";
                        break;
                    case CURRENT_AWARD_DIRECT_AMOUNT_COLUMN:
                        colNames[colNamesCount]="<html>Total Direct<br>Amount</html>";
                        break;
                    case CURRENT_AWARD_INDIRECT_AMOUNT_COLUMN:
                        colNames[colNamesCount]="<html>Total Indirect<br>Amount</html>";
                        break;
                    case CURRENT_AWARD_AMOUNT_COLUMN:
                        colNames[colNamesCount]="<html>Award<br>Amount</html>";
                        break;
                    case CURRENT_EFFECTIVE_DATE_COLUMN:
                        colNames[colNamesCount]="<html>Effective<br>Date</html>";
                        break;
                    case CURRENT_END_DATE_COLUMN:
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
                    case CURRENT_SPONSOR_AWARD_NO_COLUMN:
                        colClass[colNamesCount]=String.class;
                        break;
                    case CURRENT_AGENY_COLUMN:
                        colClass[colNamesCount]=String.class;
                        break;
                    case CURRENT_PI_COLUMN:
                        colClass[colNamesCount]=String.class;
                        break;
                    case CURRENT_TITLE_COLUMN:
                        colClass[colNamesCount]=String.class;
                        break;
                    case CURRENT_AWARD_DIRECT_AMOUNT_COLUMN:
                        colClass[colNamesCount]=Double.class;
                        break;
                    case CURRENT_AWARD_INDIRECT_AMOUNT_COLUMN:
                        colClass[colNamesCount]=Double.class;
                        break;
                    case CURRENT_AWARD_AMOUNT_COLUMN:
                        colClass[colNamesCount]=Double.class;
                        break;
                    case CURRENT_EFFECTIVE_DATE_COLUMN:
                        colClass[colNamesCount]=String.class;
                        break;
                    case CURRENT_END_DATE_COLUMN:
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
        //To fetch the unique column column names in the data
        uniqueColumnName = customDataColumns();
        String [] [] nameBeanId = new String[DEFAULT_STATIC_COLUMNS+uniqueColumnName.length][2];
        for(int counter =0;counter<=1;counter++){
            for(int colNamesCount = 0;colNamesCount < DEFAULT_STATIC_COLUMNS+uniqueColumnName.length;colNamesCount++){
                if(counter>0){
                    if(colNamesCount>12){
                        nameBeanId[colNamesCount][counter] = (String)uniqueColumnName[colNamesCount-DEFAULT_STATIC_COLUMNS];
                    }else{
                        switch(colNamesCount){
                            case CURRENT_SPONSOR_AWARD_NO_COLUMN:
                                nameBeanId[colNamesCount][counter]="sponsorAwardNumber";
                                break;
                            case CURRENT_AGENY_COLUMN:
                                nameBeanId[colNamesCount][counter]="sponsorName";
                                break;
                            case CURRENT_PI_COLUMN:
                                nameBeanId[colNamesCount][counter]="prinicipleInvestigator";
                                break;
                            case CURRENT_TITLE_COLUMN:
                                nameBeanId[colNamesCount][counter]="title";
                                break;
                            case CURRENT_AWARD_DIRECT_AMOUNT_COLUMN:
                                nameBeanId[colNamesCount][counter]="obliDirectAmount";
                                break;
                            case CURRENT_AWARD_INDIRECT_AMOUNT_COLUMN:
                                nameBeanId[colNamesCount][counter]="obliInDirectAmount";
                                break;
                            case CURRENT_AWARD_AMOUNT_COLUMN:
                                nameBeanId[colNamesCount][counter]="obliDistrubutableAmount";
                                break;
                            case CURRENT_EFFECTIVE_DATE_COLUMN:
                                nameBeanId[colNamesCount][counter]="awardEffectiveDate";
                                break;
                            case CURRENT_END_DATE_COLUMN:
                                nameBeanId[colNamesCount][counter]="finalExpirationDate";
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
    private HashMap getCustomDataElements(DepartmentCurrentReportBean currentBean){
        CoeusVector cvCustomData = currentBean.getCustomElements();
        HashMap hmCustomData = new HashMap();
        if(cvCustomData!=null){
            for(Object cvCustomDataBean : cvCustomData){
                AwardCustomDataBean awardCustomDataBean = (AwardCustomDataBean)cvCustomDataBean;
                if(awardCustomDataBean.getGroupCode()!=null && awardCustomDataBean.getGroupCode().equals(currentBean.getGroupCode())){
                    hmCustomData.put(awardCustomDataBean.getColumnName(),awardCustomDataBean.getColumnValue());
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
        for(Object singleCurrentData : currentData){
            DepartmentCurrentReportBean currentBean = (DepartmentCurrentReportBean)singleCurrentData;
            CoeusVector cvCustomData = currentBean.getCustomElements();
            if(cvCustomData!=null){
                for(Object cvCustomDataBean : cvCustomData){
                    AwardCustomDataBean awardCustomDataBean = (AwardCustomDataBean)cvCustomDataBean;
                    if(awardCustomDataBean.getGroupCode()!=null && awardCustomDataBean.getGroupCode().equals(currentBean.getGroupCode())){
                        hmCustomColumnLabel.put(awardCustomDataBean.getColumnName(),awardCustomDataBean.getColumnLabel());
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

        jcrPnCurrentSupport = new javax.swing.JScrollPane();
        tblCurrentSupport = new javax.swing.JTable();
        pnlHeader = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        jcrPnCurrentSupport.setBackground(new java.awt.Color(255, 255, 255));
        jcrPnCurrentSupport.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jcrPnCurrentSupport.setMinimumSize(new java.awt.Dimension(988, 518));
        jcrPnCurrentSupport.setPreferredSize(new java.awt.Dimension(988, 518));
        tblCurrentSupport.setModel(new javax.swing.table.DefaultTableModel(
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
        jcrPnCurrentSupport.setViewportView(tblCurrentSupport);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(jcrPnCurrentSupport, gridBagConstraints);

        pnlHeader.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblHeader.setFont(CoeusFontFactory.getLabelFont());
        lblHeader.setText("Current Support for");
        pnlHeader.add(lblHeader);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlHeader, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    /** Getter for property currentData.
     * @return Value of property currentData.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getCurrentData() {
        return currentData;
    }
    
    /** Setter for property currentData.
     * @param currentData New value of property currentData.
     *
     */
    public void setCurrentData(edu.mit.coeus.utils.CoeusVector currentData) {
        this.currentData = currentData;
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
        lblHeader.setText("<html><u>Current Support for "+headerValue+" </u></html>");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JScrollPane jcrPnCurrentSupport;
    public javax.swing.JLabel lblHeader;
    public javax.swing.JPanel pnlHeader;
    public javax.swing.JTable tblCurrentSupport;
    // End of variables declaration//GEN-END:variables
   
}
