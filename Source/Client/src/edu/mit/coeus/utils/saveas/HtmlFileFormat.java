/*
 * HtmlFileFormat.java
 *
 * Created on May 19, 2003, 11:51 AM
 */

package edu.mit.coeus.utils.saveas;

import javax.swing.JTable;
import java.util.Hashtable;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author  senthilar
 */
public class HtmlFileFormat extends FileFormat {
    
    private static final String HTML_SPACE = "&nbsp;";
    
    /** Creates a new instance of HtmlFileFormat */
    public HtmlFileFormat() {
    }
    
    /**
     * Creates a new instance of HtmlFileFormat
     * @param tblModel the model that contains the data for generating report
     * @param path the absolute path of the file.
     */
    public HtmlFileFormat(javax.swing.table.TableModel tblModel, String path) throws FileNotFoundException,IOException{
        super(tblModel, path);
    }
    
    /**
     * Creates a new instance of HtmlFileFormat
     * @param searchData the <code>Hashtable</code> that contains the data for generating report
     * @param path the absolute path of the file.
     */
    public HtmlFileFormat(Hashtable searchData, String path) throws FileNotFoundException,IOException{
        super(searchData, path);
    }
    
    //Added by sharath - 23-Oct-2003 to take table as data - Start
    /**
     * Creates a new instance of HtmlFileFormat
     * @param searchData the <code>Hashtable</code> that contains the data for generating report
     * @param path the absolute path of the file.
     */
    public HtmlFileFormat(JTable tableData, String path) throws FileNotFoundException,IOException{
        super(tableData, path);
    }
    //Added by sharath - 23-Oct-2003 to take table as data - End
    
    /**
     * This method parses the data and format it into the HTML Table
     */
    public void processData() {
        if (isTableModel) {
            //System.out.println("The Table model is = " + tblModel);
            int rowCount = tblModel.getRowCount();
            int colCount = tblModel.getColumnCount();
            
            writeLine("<title>Coeus</title>");
            writeLine("<TABLE BORDER>");
            writeLine("<TR>");
            for (int i=0; i<colCount; i++){
                writeLine("    <TH>" + tblModel.getColumnName(i) + "</TH>");
            }
            writeLine("</TR>");
            
            for (int i=0; i<rowCount; i++) {
                writeLine("<TR>");
                for (int j=0; j<colCount; j++) {
                    writeLine("<TD>" + HTML_SPACE + tblModel.getValueAt(i,j) + "</TD>");
                }
                writeLine("</TR>");
            }
            writeLine("</TABLE>");
            close();
        }
        
        //Added by sharath - 23-Oct-2003 to take table as data - Start
        else if(isTable) {
            int rowCount = tableData.getRowCount();
            int colCount = tableData.getColumnCount();
            
            writeLine("<title>Coeus</title>");
            writeLine("<TABLE BORDER>");
            writeLine("<TR>");
            
            for (int i=0; i<colCount; i++){
                if(tableData.getColumnModel().getColumn(i).getPreferredWidth() == 0) {
                    continue;
                }
                writeLine("    <TH>" +tableData.getColumnName(i) + "</TH>");
            }
            writeLine("</TR>");
            
            for (int i=0; i<rowCount; i++) {
                writeLine("<TR>");
                for (int j=0; j<colCount; j++) {
                    if(tableData.getColumnModel().getColumn(j).getPreferredWidth() == 0) {
                        continue;
                    }
                    writeLine("<TD>" + HTML_SPACE + tableData.getValueAt(i,j) + "</TD>");
                }
                writeLine("</TR>");
            }
            writeLine("</TABLE>");
            close();
        }
        //Added by sharath - 23-Oct-2003 to take table as data - End
        
        else {
            //System.out.println("The HashTable is = " + finalSearchData);
            int rowCount = finalSearchData.length;
            int colCount = finalSearchData[0].length;
            
            writeLine("<title>Coeus</title>");
            writeLine("<TABLE BORDER>");
            writeLine("<TR>");
            for (int i=0; i<colCount; i++){
                writeLine("    <TH>" + finalSearchData[0][i] + "</TH>");
            }
            writeLine("</TR>");
            
            for (int i=0; i<rowCount; i++) {
                writeLine("<TR>");
                for (int j=0; j<colCount; j++) {
                    writeLine("<TD>" + finalSearchData[i][j] + "</TD>");
                }
                writeLine("</TR>");
            }
            writeLine("</TABLE>");
            close();
        }
    }
}