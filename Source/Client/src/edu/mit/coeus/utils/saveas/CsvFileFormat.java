/*
 * CsvFileFormat.java
 *
 * Created on May 19, 2003, 11:49 AM
 */

package edu.mit.coeus.utils.saveas;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.util.Hashtable;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 *
 * @author  senthilar
 */
public class CsvFileFormat extends FileFormat {
    
    /** Creates a new instance of CsvFileFormat */
    public CsvFileFormat() {
    }
    
    /**
     * Creates a new instance of CsvFileFormat
     * @param tblModel the <code>TableModel</code> that contains data for report generation
     * @param path the absolute path of the file
     */
    public CsvFileFormat(TableModel tblModel, String path) throws FileNotFoundException,IOException {
        super(tblModel,path);
        //System.out.println("Inside the CsvFileFormat..");
        //System.out.println("CsvFileFormat table model = " + super.tblModel);
    }
    
    /**
     * Creates a new instance of CsvFileFormat
     * @param searchData the <code>Hashtable</code> that contains data for report generation
     * @param path the absolute path of the file
     */
    public CsvFileFormat(Hashtable searchData, String path) throws FileNotFoundException,IOException {
        super(searchData,path);
        //System.out.println("Inside the CsvFileFormat..");
        //System.out.println("CsvFileFormat table model = " + super.tblModel);
    }
    
    //Added by sharath - 23-Oct-2003 to take table as data - Start
    /**
     * Creates a new instance of CsvFileFormat
     * @param tableData the <code>JTable</code> that contains data for report generation
     * @param path the absolute path of the file
     */
    public CsvFileFormat(JTable tableData, String path) throws FileNotFoundException,IOException {
        super(tableData, path);
        //System.out.println("Inside the CsvFileFormat..");
        //System.out.println("CsvFileFormat table model = " + super.tblModel);
    }
    //Added by sharath - 23-Oct-2003 to take table as data - End
    
    /**
     * This method will process the data and format it inot CSV compatible.
     */
    public void processData() {
        if (isTableModel) {
            //System.out.println("The Table model is = " + tblModel);
            StringBuffer sbuf = new StringBuffer();
            int rowCount = tblModel.getRowCount();
            int colCount = tblModel.getColumnCount();
            
            //Added by sharath - 23 - Oct - 2003 for Headers - Start
            for(int index = 0; index < colCount; index++) {
                sbuf.append(tblModel.getColumnName(index));
                /* Append the tab char as delimiter */
                sbuf.append(',');
            }
            writeLine(sbuf.toString());
            sbuf.delete(0,sbuf.length());
            //Added by sharath - 23 - Oct - 2003 for Headers - End
            
            for (int i=0; i<rowCount; i++) {
                for (int j=0; j<colCount; j++) {
                    Object value = tableData.getValueAt(i,j);
                    
                    if(value == null) {
                        value = "";
                    }
                    
                    value = value.toString().replace('\n',' ').replace('\t',' ').replace(',',';');
                    sbuf.append(value);
                    //sbuf.append(tblModel.getValueAt(i,j).toString().replace(',',';'));
                    /* Append the tab char as delimiter */
                    sbuf.append(',');
                }
                writeLine(sbuf.toString());
                sbuf.delete(0,sbuf.length());
            }
            close();
        }
        //Added by sharath - 23-Oct-2003 to take table as data - Start
        else if(isTable) {
            StringBuffer sbuf = new StringBuffer();
            int rowCount = tableData.getRowCount();
            int colCount = tableData.getColumnCount();
            
            //Added by sharath - 23 - Oct - 2003 for Headers - Start
            for(int index = 0; index < colCount; index++) {
                if(tableData.getColumnModel().getColumn(index).getPreferredWidth() == 0) {
                    continue;
                }
                sbuf.append(tableData.getColumnName(index));
                /* Append the tab char as delimiter */
                sbuf.append(',');
            }
            writeLine(sbuf.toString());
            sbuf.delete(0,sbuf.length());
            //Added by sharath - 23 - Oct - 2003 for Headers - End
            
            for (int i=0; i<rowCount; i++) {
                for (int j=0; j<colCount; j++) {
                    if(tableData.getColumnModel().getColumn(j).getPreferredWidth() == 0) {
                        continue;
                    }
                    Object value = tableData.getValueAt(i,j);
                    
                    if(value == null) {
                        value = "";
                    }
                    
                    value = value.toString().replace('\n',' ').replace('\t',' ').replace(',',';');
                    sbuf.append(value);
                    /* Append the tab char as delimiter */
                    sbuf.append(',');
                }
                writeLine(sbuf.toString());
                sbuf.delete(0,sbuf.length());
            }
            close();
        }
        //Added by sharath - 23-Oct-2003 to take table as data - Start
        else {
            //System.out.println("The HashTable is = " + finalSearchData);
            StringBuffer sbuf = new StringBuffer();
            int rowCount = finalSearchData.length;
            int colCount = finalSearchData[0].length;
            
            for (int i=0; i<rowCount; i++) {
                for (int j=0; j<colCount; j++) {
                    sbuf.append(finalSearchData[i][j]);
                    /* Append the tab char as delimiter */
                    sbuf.append(',');
                }
                writeLine(sbuf.toString());
                sbuf.delete(0,sbuf.length());
            }
            close();
        }
    }
}