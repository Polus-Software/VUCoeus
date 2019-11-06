/*
 * BaseFileFormat.java
 *
 * Created on May 19, 2003, 11:48 AM
 */

package edu.mit.coeus.utils.saveas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.util.Hashtable;

/**
 *
 * @author  senthilar
 */
public class BaseFileFormat extends FileFormat {
    
    /**
     * Creates a new instance of BaseFileFormat
     */
    public BaseFileFormat() {
    }
    
    /**
     * Creates a new instance of BaseFileFormat
     * @param tblModel the <code>TableModel</code> that contains the data for report
     * @path path the string that represents the absolute path
     */
    public BaseFileFormat(TableModel tblModel, String path) throws FileNotFoundException,IOException {
        super(tblModel, path);
    }
    
    /**
     * Creates a new instance of BaseFileFormat
     * @param searchData the <code>Hashtable</code> that contains the data for report
     * @path path the string that represents the absolute path
     */
    public BaseFileFormat(Hashtable searchData, String path) throws FileNotFoundException,IOException{
        super(searchData, path);
    }
    
    //Added by sharath - 23-Oct-2003 to take table as data - Start
    /**
     * Creates a new instance of BaseFileFormat
     * @param tableData the <code>JTable</code> that contains the data for report
     * @path path the string that represents the absolute path
     */
    public BaseFileFormat(JTable tableData, String path) throws FileNotFoundException,IOException{
        super(tableData, path);
    }
    //Added by sharath - 23-Oct-2003 to take table as data - Start
    
    /**
     * The default process data will create a text file.
     * For other formats use the respective subclasses processData
     * methods.
     * This method will create data formatted with tab as delimeter
     */
    public void processData() {
        if (isTableModel) {
            StringBuffer sbuf = new StringBuffer();
            int rowCount = tblModel.getRowCount();
            int colCount = tblModel.getColumnCount();
            
            //Added by sharath - 23 - Oct - 2003 for Headers - Start
            for(int index = 0; index < colCount; index++) {
                sbuf.append(tblModel.getColumnName(index));
                /* Append the tab char as delimiter */
                sbuf.append('\0'); sbuf.append('\t');
            }
            writeLine(sbuf.toString());
            sbuf.delete(0,sbuf.length());
            //Added by sharath - 23 - Oct - 2003 for Headers - End
            
            for (int i=0; i<rowCount; i++) {
                for (int j=0; j<colCount; j++) {
                    
                    //Added by sharath - 23-Oct-2003 for excel displaying data in next line if it contains \n - Start
                    Object value = tblModel.getValueAt(i,j);
                    if(value == null) {
                        value = "";
                    }
                    value = value.toString().replace('\n',' ').replace('\t',' ');
                    sbuf.append(value);
                    //Added by sharath - 23-Oct-2003 for excel displaying data in next line if it contains \n - End
                    
                    //sbuf.append(tblModel.getValueAt(i,j)); //Commented by sharath - 23-Oct-2003 since excel displaying data in next line if it contains \n
                    /* Append the tab char as delimiter */
                    sbuf.append('\0');
                    sbuf.append('\t');
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
                sbuf.append('\0'); sbuf.append('\t');
            }
            writeLine(sbuf.toString());
            sbuf.delete(0,sbuf.length());
            //Added by sharath - 23 - Oct - 2003 for Headers - End
            
            for (int i=0; i<rowCount; i++) {
                for (int j=0; j<colCount; j++) {
                    if(tableData.getColumnModel().getColumn(j).getPreferredWidth() == 0) {
                        continue;
                    }
                    //Added by sharath - 23-Oct-2003 for excel displaying data in next line if it contains \n - Start
                    Object value = tableData.getValueAt(i,j);
                    if(value == null) {
                        value = "";
                    }
                    value = value.toString().replace('\n',' ').replace('\t',' ');
                    sbuf.append(value);
                    //Added by sharath - 23-Oct-2003 for excel displaying data in next line if it contains \n - End
                    
                    //sbuf.append(tblModel.getValueAt(i,j)); //Commented by sharath - 23-Oct-2003 since excel displaying data in next line if it contains \n
                    /* Append the tab char as delimiter */
                    sbuf.append('\0');
                    sbuf.append('\t');
                }
                writeLine(sbuf.toString());
                sbuf.delete(0,sbuf.length());
            }
            close();
        }
        //Added by sharath - 23-Oct-2003 to take table as data - Start
        else {
            StringBuffer sbuf = new StringBuffer();
            int rowCount = finalSearchData.length;
            int colCount = finalSearchData[0].length;
            
            for (int i=0; i<rowCount; i++) {
                for (int j=0; j<colCount; j++) {
                    sbuf.append(finalSearchData[i][j]);
                    /* Append the tab char as delimiter */
                    sbuf.append('\t');
                }
                writeLine(sbuf.toString());
                sbuf.delete(0,sbuf.length());
            }
            close();
        }
    }
}