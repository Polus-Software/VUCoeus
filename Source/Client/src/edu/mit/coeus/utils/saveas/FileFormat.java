/*
 * FileFormat.java
 *
 * Created on May 19, 2003, 11:42 AM
 */

 /*
  * PMD check performed, and commented unused imports and variables on 22-AUGUST-2011
  * by Maharaja Palanichamy
  */
 
package edu.mit.coeus.utils.saveas;

import javax.swing.JTable;
import javax.swing.table.TableModel;
//import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;
import java.util.HashMap;

import edu.mit.coeus.search.bean.DisplayBean;
import edu.mit.coeus.utils.Utils;

/**
 * 
 * @author senthilar
 */
public abstract class FileFormat {
    
    protected String path;
    
    protected BufferedWriter bw;
    
    protected TableModel tblModel;
    
    protected boolean isTableModel;
    
    protected Hashtable searchData;
    
    protected String[][] finalSearchData;
    
    //Added by sharath - 23-Oct-2003 to take table as data - Start
    protected JTable tableData;
    protected boolean isTable;   
    //Added by sharath - 23-Oct-2003 to take table as data - End
    
    //COEUSQA-1686 : Add additional fields to the Current Pending Support Schema - To take Hashmap as data - Start
    protected HashMap hmReport;
    //COEUSQA-1686 : End
    
    /** Creates a new instance of BaseFileFormat */
    public FileFormat() {
    }
    
    /** 
     * Creates a new instance of BaseFileFormat 
     * @param tblModel the <code>TableModel</code> that contains the data
     *        for generating reports
     * @path  path the absolute of the file
     */
    public FileFormat(TableModel tblModel1, String path) throws FileNotFoundException {
        this.path = path;
        this.tblModel = tblModel;                  
        isTableModel = true;           
            
        try {
            bw = new BufferedWriter(new FileWriter(path));
        }    
        catch (IOException ioe){
            //System.out.println("IOException at FileFormt Constructor");
            ioe.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** 
     * Creates a new instance of BaseFileFormat 
     * @param searchData the <code>Hashtable</code> that contains the data
     *        for generating reports
     * @path  path the absolute of the file
     */

    public FileFormat(Hashtable searchData, String path) throws FileNotFoundException,IOException {
        this.path = path;
        this.searchData = searchData;
        isTableModel = false;
                       
        bw = new BufferedWriter(new FileWriter(path));

        try {
            // Commented and added by chandra to fix 1299 - start
           // storeRecords();
            storeRecordsForData();
            // End bug fix #1229
        }
        catch(Exception ex) {
            ex.printStackTrace();
            //System.out.println("Exception caught while stroing records.");
        }
    }
    //COEUSQA-1686 : Add additional fields to the Current Pending Support Schema - Start
    /**
     * This method removes the HTML tags from Header String
     */        
    
    public static String removeHTMLFromHeader(String htmlString) {
        String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");
        return noHTMLString;
    }      
    
    /**
     * This method removes the HTML tags from JTable Header
     */  
    public void removeHTMLFromJTableHeader(JTable tableData) {
        Vector vecHeader = new Vector();
        Vector<Vector> vecRowData = new Vector<Vector>();
        Vector vecData = null;
        
        for(int index = 0; index < tableData.getColumnCount(); index++) {
            vecHeader.add(removeHTMLFromHeader(tableData.getColumnName(index)));
        }
        for (int rowIndex=0; rowIndex<tableData.getRowCount(); rowIndex++) {
            vecData = new Vector();
            for (int colIndex=0; colIndex<tableData.getColumnCount(); colIndex++) {
                vecData.add(tableData.getValueAt(rowIndex,colIndex));
            }
            vecRowData.add(vecData);
        }
        //COEUSQA-1477 Dates in Search Results - Start
        this.tableData = new JTable(vecRowData, vecHeader);
        //to fetch the data of the field formats and value
        JTable clonedTable = new JTable(vecRowData, vecHeader);
        int columnCount = tableData.getColumnCount();
        for(int index=0;index< columnCount;index++){
            clonedTable.getColumnModel().getColumn(index).setIdentifier(tableData.getColumnModel().getColumn(index).getIdentifier());
        }
        this.tableData = clonedTable;
        //COEUSQA-1477 Dates in Search Results - End
    }
    //COEUSQA-1686 : End
     
    //Added by sharath - 23-Oct-2003 to take table as data - Start
    /** 
     * Creates a new instance of BaseFileFormat 
     * @param tableData the <code>JTable</code> that contains the data
     *        for generating reports
     * @path  path the absolute of the file
     */

    public FileFormat(JTable tableData, String path) throws FileNotFoundException,IOException {
        this.path = path;        
        
        //COEUSQA-1686 : Add additional fields to the Current Pending Support Schema - To remove HTML tags from a String - Start
        // this.tableData = tableData;
        removeHTMLFromJTableHeader(tableData);
        //COEUSQA-1686 : End       
                
        isTable = true;
        
        bw = new BufferedWriter(new FileWriter(path));

        try {
            storeRecords();
        }
        catch(Exception ex) {
            ex.printStackTrace();
            //System.out.println("Exception caught while stroing records.");
        }
    }    
        
    //Added by sharath - 23-Oct-2003 to take table as data - End
    
    //COEUSQA-1686 : Add additional fields to the Current Pending Support Schema - To take Hashmap as data - Start    
     public FileFormat(HashMap hmReport, String path) throws FileNotFoundException,IOException {
        this.path = path;
        this.hmReport = hmReport;
       
        try {
             bw = new BufferedWriter(new FileWriter(path));
        }
        catch(Exception ex) {
            ex.printStackTrace();            
        }
    }     
    //COEUSQA-1686 : End
    
    /** 
     * This method writes a single formatted line at a time. 
     * The formatting depends on the need basis.
     * @param line the line that should be written to a file.
     */
    protected void writeLine(String line) {
        try {
            bw.write(line);
            bw.newLine();
        }
        catch(IOException ioe){
            //JOptionPane.showMessageDialog(null,"The file " + path + " may be already opened.\nClose it and try again.");                        
            //System.out.println("IOException at writeLine");
            ioe.printStackTrace();
            return;
        }
        catch(Exception ex){
            //System.out.println("Exception at writeLine");
            ex.printStackTrace();
        }            
    }
        
    /* 
     * This method is exclusively for Excel sheet alone. 
     * The other write does work with Apache POI WorkBook write. 
     * So this write is created with Stream.
     * @param wb the excel workbook
     */
    protected void write(org.apache.poi.hssf.usermodel.HSSFWorkbook wb) {
        try {
            //Lets close the buffered writer first
            FileOutputStream fos = new FileOutputStream(path);
            wb.write(fos);
            fos.close();
        }
        catch(IOException ioe){
            //System.out.println("IOException at writeLine");
            ioe.printStackTrace();
        }
        catch(Exception ex){
            //System.out.println("Exception at writeLine");
            ex.printStackTrace();
        }            
    }
    
   /**
    * The formatting of the data is done by this method in the subclass.
    */
    public abstract void processData();
    
    /**
     * This method close the file after writing the formatted data.
     * Any exception thrown while closing is caught here.
     */
    public void close() {
        try{
            bw.close();
        }
        catch(IOException ioe) {
            //System.out.println("IOEException thrown while closing.");
            ioe.printStackTrace();
        }
        catch(Exception ex) {
            //System.out.println("Exception thrown while closing.");
            ex.printStackTrace();
        }
    }    
    
   /** 
    * This method sets the path
    * @param path the absolute path of the file
    */
    public void setPath(String path) {
        this.path = path;
    }
    
    /** This method fetch the records from the Hashtable which contains two vectors 
     * One vector contains the Label Names and the other in turn contains another 
     * Hashtable where the table data are stored. This method fetches these data and 
     * store it in the double array strings.
     */
    public void storeRecords() throws Exception {
        if (isTableModel) {
            return;
        }
        
        //Added by sharath - 23-Oct-2003 to take table as data - Start
        if(isTable) return ;
        //Added by sharath - 23-Oct-2003 to take table as data - End
        
        Vector displayList = (Vector)searchData.get("displaylabels");

        Vector reportLabels = new Vector();
        DisplayBean display = null;
        for (int labelList=0; labelList<displayList.size(); labelList++) {
            display = (DisplayBean)displayList.get(labelList);
            if (display.isVisible()) {
                reportLabels.addElement(display);
            }                
        }

        Vector resList = (Vector)searchData.get("reslist");

        if (resList == null){
            finalSearchData = new String[1][reportLabels.size()];
            int disColCnt = reportLabels.size();
            String fieldName = null;
            for (int headerCnt=0; headerCnt<disColCnt; headerCnt++) {
                finalSearchData[0][headerCnt] = (String)((DisplayBean)reportLabels.elementAt(headerCnt)).getValue();
            }            
            return;
        }
        
        finalSearchData = new String[resList.size()+1][reportLabels.size()];        
        int disColCnt = reportLabels.size();
        String fieldName = null;
        
        for (int headerCnt=0,rowInidex=0; headerCnt<disColCnt; headerCnt++) {
            finalSearchData[0][headerCnt] = (String)((DisplayBean)reportLabels.elementAt(headerCnt)).getValue();
        }
        Vector resRowList = new Vector(3,2);
        for(int rowCount=1;rowCount<=resList.size();rowCount++){
            HashMap searchResultRow = (HashMap)resList.elementAt(rowCount-1);

            for(int dispIndex=0;dispIndex<disColCnt;dispIndex++){
                display = (DisplayBean)reportLabels.elementAt(dispIndex);
                if ( display.isVisible() ) {
                    fieldName = display.getName();
                    try{
                        finalSearchData[rowCount][dispIndex] = Utils.convertNull(searchResultRow.get(fieldName)).toString().trim();
                    }
                    catch(NullPointerException nEx){
                        throw new Exception("Please check the entries in "+
                            "the DISPLAY element : "+fieldName+" in the resource XML file");
                    }
                } 
            }
        }
    }
    
    /**Added by chandra to Fix #1229 - 29th Sept 2004
     *run the different method to process even the hidden columns.
     */
     public void storeRecordsForData() throws Exception {
        if (isTableModel) {
            return;
        }
        //Added by sharath - 23-Oct-2003 to take table as data - Start
        if(isTable) return ;
        //Added by sharath - 23-Oct-2003 to take table as data - End
        
        Vector displayList = (Vector)searchData.get("displaylabels");

        Vector reportLabels = new Vector();
        DisplayBean display = null;
        for (int labelList=0; labelList<displayList.size(); labelList++) {
            display = (DisplayBean)displayList.get(labelList);
                reportLabels.addElement(display);
        }

        Vector resList = (Vector)searchData.get("reslist");

        if (resList == null){
            finalSearchData = new String[1][reportLabels.size()];
            int disColCnt = reportLabels.size();
            String fieldName = null;
            for (int headerCnt=0; headerCnt<disColCnt; headerCnt++) {
                finalSearchData[0][headerCnt] = (String)((DisplayBean)reportLabels.elementAt(headerCnt)).getValue();
            }            
            return;
        }
        
        finalSearchData = new String[resList.size()+1][reportLabels.size()];        
        int disColCnt = reportLabels.size();
        String fieldName = null;
        
        for (int headerCnt=0,rowInidex=0; headerCnt<disColCnt; headerCnt++) {
            finalSearchData[0][headerCnt] = (String)((DisplayBean)reportLabels.elementAt(headerCnt)).getValue();
        }
        Vector resRowList = new Vector(3,2);
        for(int rowCount=1;rowCount<=resList.size();rowCount++){
            HashMap searchResultRow = (HashMap)resList.elementAt(rowCount-1);

            for(int dispIndex=0;dispIndex<disColCnt;dispIndex++){
                display = (DisplayBean)reportLabels.elementAt(dispIndex);
                    fieldName = display.getName();
                    try{
                        finalSearchData[rowCount][dispIndex] = Utils.convertNull(searchResultRow.get(fieldName)).toString().trim();
                    }
                    catch(NullPointerException nEx){
                        throw new Exception("Please check the entries in "+
                            "the DISPLAY element : "+fieldName+" in the resource XML file");
                    }
            }
        }                         
        
    }// End storeRecordsForData() by chandra 29th Sept 2004 to fix #122    
                 
    
}