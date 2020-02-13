/*
 * ExcelFileFormat.java
 *
 * Created on May 23, 2003, 11:58 AM
 */

/* PMD check performed, and commented unused imports and variables on 25-August-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.utils.saveas;

import edu.mit.coeus.search.bean.DisplayBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusServerProperties;
import edu.mit.coeus.utils.DateUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.*;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.util.Hashtable;
import java.util.Date;
import java.text.ParseException;
import java.text.DateFormat;
import java.io.*;
//import org.apache.poi.hssf.model.Workbook;
//import edu.mit.coeus.gui.CoeusMessageResources;
import java.text.DecimalFormat;
import java.util.ArrayList;
//import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 *
 * @author  senthilar
 */
public class ExcelFileFormat extends FileFormat {
    
    Date dataDate = null;
    int dataNum  = 0;
    private static final String EMPTY_STRING ="";
    //Case#2908 - Exports from Search Results Do Not Preserve Data Format  - Start
    //Commented for COEUSQA-1477 Dates in Search Results - Start
    //private static final String EXCEL_DATE_FORMAT = "excel_formatCode.1000";
    //private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    //Commented for COEUSQA-1477 Dates in Search Results - End
    private static final int CHARACTER_INDEX = -1;
    //Case#2908 - End
    
    //Added on - 20-Apr-2011 to take hashMap as data - Start
    private static final String TABLEMODEL_CURRENT_PENDING_REPORT = "tblModel";
    private static final String TABLE_CURRENT_PENDING_REPORT = "tableData";
    private static final String SEARCHDATA_CURRENT_PENDING_REPORT = "SearchData";
    //Added on - 20-Apr-2011 to take hashMap as data - End
    
    //COEUSQA-1477 Dates in Search Results - Start
    private static final String DATE_FORMAT_DELIMITER = "/";
    private static final String DATE_FORMAT_USER_DELIMITER = "-";
    private static final String DATE_FORMAT_YEAR_DELIMITER = "y";
    private static final String DATE_FORMAT_MONTH_DELIMITER = "m";
    private static final String DATE_FORMAT_DATE_DELIMITER = "d";
    //COEUSQA-1477 Dates in Search Results - End
    
    /** Creates a new instance of ExcelFileFormat */
    public ExcelFileFormat() {
    }
    
    /**
     * Creates a new instance of ExcelFileFormat
     * @param tblModel the <code>TableModel</code> that contains data for report data
     * @param path the absolute path of the file
     */
    public ExcelFileFormat(TableModel tblModel, String path) throws FileNotFoundException,IOException{
        super(tblModel, path);
        //System.out.println("ExcelFileFormat table model = " + this.tblModel);
    }
    
    /**
     * Creates a new instance of ExcelFileFormat
     * @param searchData the <code>Hashtable</code> that contains data for report data
     * @param path the absolute path of the file
     */
    public ExcelFileFormat(Hashtable searchData, String path) throws FileNotFoundException,IOException{
        super(searchData, path);
    }
    
    //Added by sharath - 23-Oct-2003 to take table as data - Start
    /**
     * Creates a new instance of ExcelFileFormat
     * @param tableData the <code>JTable</code> that contains data for report data
     * @param path the absolute path of the file
     */    
    public ExcelFileFormat(JTable tableData, String path) throws FileNotFoundException,IOException{
        super(tableData, path);
    }  
       
    //Added by sharath - 23-Oct-2003 to take table as data - End
    
    //Added on - 20-Apr-2011 to take hashMap as data - Start
    public ExcelFileFormat(HashMap hmReport, String path) throws FileNotFoundException,IOException{
        super(hmReport, path);
    }
    //Added on - 20-Apr-2011 to take hashMap as data - End
    
   
    /**
     * This formatting is for excel sheet file.
     * Here the POI package from Jakarta is used for creating excel sheets.
     */
    public void processData() {        
          
        HSSFWorkbook wb = new HSSFWorkbook();
        File file = new File(path);
        //HSSFSheet sheet = wb.createSheet(path);
        //setting the sheet name as file name without extension.  
        String strKeyName = null;
        Object objValue = null;
   
        //Added on - 20-Apr-2011 to take hashMap as data - Start
        if(hmReport == null ||  hmReport.isEmpty()){
            hmReport = new HashMap();
            if(isTableModel){
                hmReport.put(TABLEMODEL_CURRENT_PENDING_REPORT, tblModel);
            }
            else if(isTable){
                hmReport.put(TABLE_CURRENT_PENDING_REPORT, tableData);
            }
            else {
                hmReport.put(SEARCHDATA_CURRENT_PENDING_REPORT, finalSearchData);                     
            }
        }
        
        Set dataSet = hmReport.entrySet();        
        Iterator iterator = dataSet.iterator();        
        
        while(iterator.hasNext()){ 
            
            Map.Entry meData = (Map.Entry) iterator.next();
            strKeyName = meData.getKey().toString();
            objValue = meData.getValue();
            
            if(objValue instanceof  TableModel ) {
                isTableModel = true;
                tblModel = (TableModel)objValue;
                
            }
            else if(objValue instanceof  JTable ) {
                isTable = true;
                //COEUSQA-1686 : Add additional fields to the Current Pending Support Schema - To remove HTML tags from a String - Start      
                //tableData = (JTable)objValue;
                removeHTMLFromJTableHeader((JTable)objValue); 
                //COEUSQA-1686 : End     
            }
            else {
                if(objValue instanceof  Hashtable) {
                    searchData = (Hashtable)objValue;
                    try {
                        storeRecordsForData();
                    } catch(Exception e) {
                    }
                }
                else {
                    finalSearchData = (String[][])objValue;  
                }                            
            }           
                              
            // HSSFSheet sheet = wb.createSheet(file.getName().substring(0, file.getName().lastIndexOf('.')));
            HSSFSheet sheet = null;            
           
            if( TABLEMODEL_CURRENT_PENDING_REPORT.equals(strKeyName) || TABLE_CURRENT_PENDING_REPORT.equals(strKeyName) || SEARCHDATA_CURRENT_PENDING_REPORT.equals(strKeyName)) {
                sheet = wb.createSheet(file.getName().substring(0, file.getName().lastIndexOf('.')));
            }
            else {
                sheet = wb.createSheet(strKeyName);
            }
          //Added on - 20-Apr-2011 to take hashMap as data - End
       
        // Create a row and put some cells in it. Rows are 0 based.
        HSSFRow row = null;
        
        HSSFCell cell = null;
        
        //COEUSQA-1477 Dates in Search Results - Start
        //to create the object of HSSFDataFormat
        HSSFDataFormat hssfDataFormat = wb.createDataFormat();
        //COEUSQA-1477 Dates in Search Results - End
        
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        
        HSSFFont headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFont(headerFont);
        
        Object value = null;
        
         if (isTableModel) {           
            
//            StringBuffer sbuf = new StringBuffer();
            int rowCount = tblModel.getRowCount();
            int colCount = tblModel.getColumnCount();
            
            //Added by sharath - 23 - Oct - 2003 for Headers - Start
            row = sheet.createRow((short)0);
            for(int index = 0; index < tblModel.getColumnCount(); index++) {
                cell = row.createCell((short)index);
                cell.setCellValue(tblModel.getColumnName(index));
                
                //Make Headers BOLD
                cell.setCellStyle(headerStyle);
            }
            
            for (int i=0; i<rowCount; i++) {
                row = sheet.createRow((short)i + 1); //+1 since First Row is Header Row
                for (int j=0; j<colCount; j++) {
                    
                    cell = row.createCell((short)j);
                    if(tblModel.getValueAt(i, j) == null) {
                        value = EMPTY_STRING;
                    }else{
                        value = tblModel.getValueAt(i, j);
                    }
                        
                    cell.setCellValue(value.toString());
                    cell.setCellStyle(cellStyle);
                }
                //writeLine(sbuf.toString());
                //sbuf.delete(0,sbuf.length());
            }
            //close();
          /*  FileOutputStream fileOut = null;
            try{
                fileOut = new FileOutputStream(path);
                wb.write(fileOut);
                //fileOut.close();
                //close();
        }catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }catch (IOException iOException) {
                iOException.printStackTrace();
            }finally {
                try{
                    fileOut.close();
                    close();
                }catch (IOException iOException) {
                    iOException.printStackTrace();
                }
                
            }
           */
        }
        //Added by sharath - 23-Oct-2003 to take table as data - Start
        else if(isTable) {            
            
//            StringBuffer sbuf = new StringBuffer();
            int rowCount = tableData.getRowCount();
            int colCount = tableData.getColumnCount();
            ArrayList styles = new ArrayList();
            //COEUSQA-1477 Dates in Search Results - Start
            DateUtils dtUtils = new DateUtils();
            //COEUSQA-1477 Dates in Search Results - End
            //Added by sharath - 23 - Oct - 2003 for Headers - Start
            // Create a row. Rows are 0 based.
            row = sheet.createRow((short)0);
            short width;
            for(int index = 0; index < tableData.getColumnCount(); index++) {
                width = (short)tableData.getColumnModel().getColumn(index).getPreferredWidth();
                if(width == 0) {
                    continue;
                }
                short columnIndex = (short)index;
                cell = row.createCell(columnIndex);
                cell.setCellValue(tableData.getColumnName(index));               
              
                //Make Headers BOLD
                cell.setCellStyle(headerStyle);
                
                //Converting pixels to twips since Excel Column size is in twips.
                //See http://www.applecore99.com/api/api012.asp
                //for converion from pixels to twips
                width = (short)((width/35.0) * 1440);
                sheet.setColumnWidth((short)index, width);
                //Case#2908 - Exports from Search Results Do Not Preserve Data Format  - Start
                //DisplayBean object is fetched from table column to get datatype and data format for each column
                Object columnObject = tableData.getColumnModel().getColumn(index).getIdentifier();
                if(columnObject instanceof DisplayBean){
                    DisplayBean displayBean = (DisplayBean)columnObject;
                    String dataType = displayBean.getDataType();
                    String format = displayBean.getFormat();
                    if(dataType.equals("number")) {
                        cellStyle = wb.createCellStyle();
                        cellStyle.setAlignment(cellStyle.ALIGN_LEFT);
                        cellStyle.setWrapText(true);
                        if(!format.equals(CoeusGuiConstants.EMPTY_STRING)){
                            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                        }
                        styles.add(cellStyle);
                    }else if(dataType.equals("date")) {
                        cellStyle = wb.createCellStyle();
                        cellStyle.setAlignment(cellStyle.ALIGN_LEFT);
                        cellStyle.setWrapText(true);
                        //Set's date format from CoeusSearch.xml or set's default 'm/d/yy' format
                        if(!format.equals(CoeusGuiConstants.EMPTY_STRING)){
                            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                        }else{
                            //COEUSQA-1477 Dates in Search Results - Start
                            //cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(coeusMessageResources.parseMessageKey(EXCEL_DATE_FORMAT)));                            
                            String userDefinedFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT);
                            //valid date formats
                            HashMap hmDateFormats = dtUtils.loadFormatsForSearchResults();
                            String dateFormat = "";
                            if(hmDateFormats.get(userDefinedFormat)!=null){
                                dateFormat = hmDateFormats.get(userDefinedFormat).toString();
                            }
                            if(dateFormat.length()>0){
                                cellStyle.setDataFormat(hssfDataFormat.getFormat(dateFormat));
                            }else{
                                //assign default date value
                                dateFormat = CoeusConstants.EXCEL_DATE_YYYY_MM_DD_SLASH;
                                cellStyle.setDataFormat(hssfDataFormat.getFormat(dateFormat));
                            }
                            //COEUSQA-1477 Dates in Search Results - End
                        }
                        styles.add(cellStyle);
                    }else{
                        cellStyle = wb.createCellStyle();
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cellStyle.setAlignment(cellStyle.ALIGN_LEFT);
                        cellStyle.setWrapText(true);
                        if(!format.equals(CoeusGuiConstants.EMPTY_STRING)){
                            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                        }
                        styles.add(cellStyle);
                    }
                }else{
                    cellStyle = wb.createCellStyle();
                    cellStyle.setAlignment(cellStyle.ALIGN_LEFT);
                    cellStyle.setWrapText(true);
                    styles.add(cellStyle);
                }
                //Case#2908 - End
            }
            //Added by sharath - 23 - Oct - 2003 for Headers - End
            
            for (int i=0; i<rowCount; i++) {
                short rowIndex = (short)(i+1);
                row = sheet.createRow(rowIndex); //+1 since First Row is Header Row
                for (int j=0; j<colCount; j++) {
                    if(tableData.getColumnModel().getColumn(j).getPreferredWidth() == 0) {
                        continue;
                    }
                    short columnIndex= (short)j;
                    cell = row.createCell(columnIndex);
                    //Case#2908 - Exports from Search Results Do Not Preserve Data Format  - Start
                    //Modified to assign value in number/date format in excel cell
                     value = tableData.getValueAt(i, j);
                     if(value == null || value.equals(CoeusGuiConstants.EMPTY_STRING)) {
                        value = CoeusGuiConstants.EMPTY_STRING;
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(value.toString());
                     }else{
                       //Gets the displaybean from table column for each column
                        Object columnObject = tableData.getColumnModel().getColumn(j).getIdentifier();
                        value = tableData.getValueAt(i, j);
                        if(columnObject instanceof DisplayBean){
                            DisplayBean displayBean = (DisplayBean)columnObject;
                            String dataType = displayBean.getDataType();
                            if(dataType.equals("number")){
                                try{
                                    DecimalFormat decimalFormat = new DecimalFormat();
                                    //value is parsed to check is number or decimal
                                    //number is converted to Integer object and assign to cell
                                    //decimal is converted to Double object and assign to cell
                                    Object parseObject = decimalFormat.parseObject(value.toString());
                                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                                    if(parseObject instanceof Long && value.toString().indexOf(".") == CHARACTER_INDEX){
                                        cell.setCellValue(Integer.parseInt(value.toString()));
                                    }else if(parseObject instanceof Double || value.toString().indexOf(".") != CHARACTER_INDEX){
                                        // Modified for COEUSQA-2953 : Error saving Subcontract search result set as excel file - Start
//                                        cell.setCellValue(Double.parseDouble(value.toString()));
                                        cell.setCellValue(Double.parseDouble(value.toString().replaceAll(",",CoeusGuiConstants.EMPTY_STRING)));                                        
                                        // Modified for COEUSQA-2953 : Error saving Subcontract search result set as excel file - End
                                    }
                                }catch(ParseException pE){
//                                    System.out.println("Data parsing error in "+displayBean.getName());
                                    pE.printStackTrace();
                                }
                            }else if(dataType.equals("date")){
                                //COEUSQA-1477 Dates in Search Results - Start
                                //value = new Date(value.toString());
                                DateFormat df = DateFormat.getDateInstance();
                                String dateValue = formatDateForSearchResults(value.toString());
                                value = new Date(dateValue);
                                //COEUSQA-1477 Dates in Search Results - End
                                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                                cell.setCellValue((Date)value);
                            }else{
                                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                cell.setCellValue(value.toString());
                            }
                        }else{
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            cell.setCellValue(value.toString());
                        }
                     }
                     if(styles.size()-1 > j){
                         cellStyle = (HSSFCellStyle)styles.get(j);
                     }
                    //Column based HSSFCellStyle object is set's to each cell
                    cell.setCellStyle(cellStyle);
                    //Case#2908 - End
                }
            }
            //close();
           /* FileOutputStream fileOut = null;
            try{
                fileOut = new FileOutputStream(path);
                wb.write(fileOut);
                //fileOut.close();
                //close();
            }catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }catch (IOException iOException) {
                iOException.printStackTrace();
            }finally {
                try{
                    fileOut.close();
                    close();
                }catch (IOException iOException) {
                    iOException.printStackTrace();
                }
                
            }
            */
            
        }
        //Added by sharath - 23-Oct-2003 to take table as data - End
        
        else {            
           
            StringBuffer sbuf = new StringBuffer();
            int rowCount = finalSearchData.length;
            int colCount = finalSearchData[0].length;
            
            for (int i=0; i<rowCount; i++) {
                row = sheet.createRow((short)i);
                for (int j=0; j<colCount; j++) {
                    String data = finalSearchData[i][j];
                    //row.createCell((short)j).setCellValue(data);
                    
                    cell = row.createCell((short)j);
                    cell.setCellValue(data);
                    cell.setCellStyle(cellStyle);
                }
                write(wb);
                sbuf.delete(0,sbuf.length());
            }
            close();
        }        
           
        }
        //Added on - 20-Apr-2011 to take hashMap as data - Start
         if (isTableModel || isTable) { 
            
             FileOutputStream fileOut = null;
            try{
                fileOut = new FileOutputStream(path);
                wb.write(fileOut);
                //fileOut.close();
                //close();
            }catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }catch (IOException iOException) {
                iOException.printStackTrace();
            }finally {
                try{
                    fileOut.close();
                    close();
                }catch (IOException iOException) {
                    iOException.printStackTrace();
                }
                
            }            
         }
        //Added on - 20-Apr-2011 to take hashMap as data  - End
        
    }
    
    /**
     * This method will check whether the String value represents
     * the date.
     * @return if the string represents the date then
     *         true is returned
     */
    public boolean isDate(String data) {
        boolean isDate = true;
        try {
            DateFormat df = DateFormat.getDateInstance();
            dataDate = df.parse(data);
            //System.out.println("The parsed date = " + dataDate);
        }
        catch(ParseException pe) {
            isDate=false;
        }
        return isDate;
    }
    
    /**
     * This method will parse the the string to see if the
     * string represents the number.
     * @return returns true if the passed string is a number
     */
    public boolean isNumber(String data){
        
        try{
            dataNum = Integer.parseInt(data);
        }
        catch(NumberFormatException nfe){
            return false;
        }
        if (Integer.parseInt(data.substring(0,1)) == 0) {
            //System.out.println("The zeros are padded " + data);
            return false;
        }
        return true;
    }
    
    //COEUSQA-1477 Dates in Search Results - Start
    /**
     * This method format the date which can be passed as a 
     * parameter to be Date() constructor.
     * @param value
     * @return returns dateValue which is formatted date value
     */
    public String formatDateForSearchResults(String value){
        String dateValue = "";
        String dateFormat = "";
        String validDateFormat = "";
        dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT);
        if(dateFormat.length()>0){
            DateUtils dtUtils = new DateUtils();
            HashMap hmDateFormats = dtUtils.loadFormatsForSearchResults();
            if(hmDateFormats.get(dateFormat)!=null){
                validDateFormat = hmDateFormats.get(dateFormat).toString();
            }
            if(!(validDateFormat.length()>0)){
                //assign default date value
                dateFormat = CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH;
            }
        }else{
            dateFormat = CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH;
        }
        if(value!=null && value.length()>0){
            //to check if date contains user defined delimiter
            if(value.contains(DATE_FORMAT_USER_DELIMITER)){
                 //to replace the user defined delimiter to default date format
                 dateValue = value.replaceAll(DATE_FORMAT_USER_DELIMITER,DATE_FORMAT_DELIMITER);
            }else{
                dateValue = value;
            }
            if(dateValue.indexOf(DATE_FORMAT_DELIMITER)>0){
                String [] dateComponents = dateValue.split(DATE_FORMAT_DELIMITER);
                if((dateComponents[dateComponents.length-1]).length()<=4){
                    dateValue = fetchDateValuesForSearchResults(dateComponents, dateFormat);
                }else{
                    String date = (dateComponents[dateComponents.length-1]);
                    String patternValue = date.substring(0,date.indexOf(" "));
                    String time = date.substring((date.indexOf(patternValue)+patternValue.length()),date.length());
                    dateComponents[dateComponents.length-1] = patternValue;
                    date = fetchDateValuesForSearchResults(dateComponents, dateFormat);
                    dateValue = date+time;
                }
            }
        }
        return dateValue;
    }

    /**
     * This method format the date which can be passed as a 
     * parameter to be Date() constructor.
     * @param dateComponents
     * @param dateFormat
     * @return returns dateValue which is formatted date value
     */
    public String fetchDateValuesForSearchResults(String[] dateComponents, String dateFormat) {
        HashMap hmDateFormat = new HashMap(4);
        if(dateComponents!=null && dateFormat!=null){
            if(dateFormat.contains(DATE_FORMAT_USER_DELIMITER)){
                dateFormat = dateFormat.replaceAll(DATE_FORMAT_USER_DELIMITER,DATE_FORMAT_DELIMITER);
            }
            //to check whether the date contains "/"
            if(dateFormat.indexOf(DATE_FORMAT_DELIMITER)>0){
                String [] dateDefaultComponents = dateFormat.split(DATE_FORMAT_DELIMITER);
                Integer counter = new Integer(0);
                //to add to the collection object if data matches the repective delimiter
                for(String data:dateDefaultComponents){
                    //to remove "fm" from "Month"
                    if(data.contains("fm")){
                        data = data.replaceAll("fm","");
                    }
                    if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_YEAR_DELIMITER)){
                        hmDateFormat.put("Year",counter);
                    }else if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_MONTH_DELIMITER)){
                        hmDateFormat.put("Month",counter);
                    }else if(data.substring(0,1).equalsIgnoreCase(DATE_FORMAT_DATE_DELIMITER)){
                        hmDateFormat.put("Date",counter);
                    }
                    counter++;
                }
            }
        }
        //formation of the date in the default date format
        String date = dateComponents[(Integer)hmDateFormat.get("Year")]+DATE_FORMAT_DELIMITER
                +dateComponents[(Integer)hmDateFormat.get("Month")]
                +DATE_FORMAT_DELIMITER+dateComponents[(Integer)hmDateFormat.get("Date")];
        return date;
    }
    //COEUSQA-1477 Dates in Search Results - End
}



