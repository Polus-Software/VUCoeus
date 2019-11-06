/*
 * FileFormatFactory.java
 *
 * Created on May 19, 2003, 11:46 AM
 */

package edu.mit.coeus.utils.saveas;

import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.util.Hashtable;
import java.io.FileNotFoundException;
import java.io.IOException;
 
/**
 *
 * @author  senthilar
 */
public class FileFormatFactory {
    
    private FileFormat fileFormat;
    private static boolean isTableModel = false;
    
    /** 
     * Creates a new instance of FileFormatFactory 
     */
    public FileFormatFactory() {
    }
    
   /** 
    * This method will parse the path and finds out which class to be instatntiated depending 
    * on the extension. It then returns the instantiated Object.
    * @param tblModel the model that contains data for generating report
    * @param path the absolute path of the file.
    * @return the FileFormat object depending on the file extension
    */
    public static FileFormat createFileFormat(TableModel tableModel, String path) throws FileNotFoundException, IOException{
        isTableModel = true;        
        FileFormat fileFormat = null;
        String ext = path.substring(path.lastIndexOf('.')+1);
        if (ext.equalsIgnoreCase("txt")) {
            //System.out.println("The file is base file format.");
            fileFormat = new BaseFileFormat(tableModel, path);
        }
        else if (ext.equalsIgnoreCase("csv")){
            //System.out.println("The file is csv file format.");          
            fileFormat = new CsvFileFormat(tableModel, path);
        }
        else if (ext.equalsIgnoreCase("htm") || ext.equalsIgnoreCase("html")) {
            //System.out.println("The file is html file format.");
            fileFormat = new HtmlFileFormat(tableModel, path);
        }   
        else if (ext.equalsIgnoreCase("XLS")) {
            //System.out.println("The file is XLS file format.");
            //fileFormat = new BaseFileFormat(tableModel, path);
            fileFormat = new ExcelFileFormat(tableModel, path);
        }                
        return fileFormat;        
    }
 
   /** 
    * This method will parse the path and finds out which class to be instatntiated depending 
    * on the extension. It then returns the instantiated Object.
    * @param searchData the hashtable that contains data for generating report
    * @param path the absolute path of the file.
    * @return the FileFormat object depending on the file extension
    */
    public static FileFormat createFileFormat(Hashtable searchData, String path) throws FileNotFoundException,IOException {
        isTableModel = false;
        FileFormat fileFormat = null;
        String ext = path.substring(path.lastIndexOf('.')+1);
        //System.out.println("The Extension = " + ext);
        if (ext.equalsIgnoreCase("txt")) {
            //System.out.println("The file is base file format.");
            fileFormat = new BaseFileFormat(searchData, path);
        }
        else if (ext.equalsIgnoreCase("csv")){
            //System.out.println("The file is csv file format.");          
            fileFormat = new CsvFileFormat(searchData, path);
        }
        else if (ext.equalsIgnoreCase("htm") || ext.equalsIgnoreCase("html")) {
            //System.out.println("The file is html file format.");
            fileFormat = new HtmlFileFormat(searchData, path);
        }   
        else if (ext.equalsIgnoreCase("XLS")) {
            //System.out.println("The file is XLS file format.");
            fileFormat = new ExcelFileFormat(searchData, path);
        }                
        return fileFormat;        
    }
    
    //Added by sharath - 23-Oct-2003 to take table as data - Start
    /** 
    * This method will parse the path and finds out which class to be instatntiated depending 
    * on the extension. It then returns the instantiated Object.
    * @param tableData the table that contains data for generating report
    * @param path the absolute path of the file.
    * @return the FileFormat object depending on the file extension
    */
    public static FileFormat createFileFormat(JTable tableData, String path) throws FileNotFoundException,IOException {
        isTableModel = false;
        FileFormat fileFormat = null;
        String ext = path.substring(path.lastIndexOf('.')+1);
        //System.out.println("The Extension = " + ext);
        if (ext.equalsIgnoreCase("txt")) {
            //System.out.println("The file is base file format.");
            fileFormat = new BaseFileFormat(tableData, path);
        }
        else if (ext.equalsIgnoreCase("csv")){
            //System.out.println("The file is csv file format.");          
            fileFormat = new CsvFileFormat(tableData, path);
        }
        else if (ext.equalsIgnoreCase("htm") || ext.equalsIgnoreCase("html")) {
            //System.out.println("The file is html file format.");
            fileFormat = new HtmlFileFormat(tableData, path);
        }   
        else if (ext.equalsIgnoreCase("XLS")) {
            //System.out.println("The file is XLS file format.");
            //fileFormat = new BaseFileFormat(tableData, path);
            fileFormat = new ExcelFileFormat(tableData, path);                   
        }                
        return fileFormat;        
    }
    //Added by sharath - 23-Oct-2003 to take table as data - End
    
    
    //Added on - 20-Apr-2011 -to take hashMap as data - Start
    
     public static FileFormat createFileFormat(HashMap hmReport, String path) throws FileNotFoundException,IOException {
        isTableModel = false;
        FileFormat fileFormat = null;
        String ext = path.substring(path.lastIndexOf('.')+1);
        //System.out.println("The Extension = " + ext);
       /* if (ext.equalsIgnoreCase("txt")) {
            //System.out.println("The file is base file format.");
            fileFormat = new BaseFileFormat(tableData, path);
        }
        else if (ext.equalsIgnoreCase("csv")){
            //System.out.println("The file is csv file format.");          
            fileFormat = new CsvFileFormat(tableData, path);
        }
        else if (ext.equalsIgnoreCase("htm") || ext.equalsIgnoreCase("html")) {
            //System.out.println("The file is html file format.");
            fileFormat = new HtmlFileFormat(tableData, path);
        }   
        else 
        */
        if (ext.equalsIgnoreCase("XLS")) {
            //System.out.println("The file is XLS file format.");
            fileFormat = new ExcelFileFormat(hmReport, path);
                   
        }                
        return fileFormat;        
    }
    //Added on - 20-Apr-2011 -to take hashMap as data - End
    
     
}