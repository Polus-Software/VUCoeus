/*
 * SaveAsDialog.java
 *
 * Created on May 19, 2003, 11:35 AM
 */

package edu.mit.coeus.utils.saveas;

import edu.mit.coeus.departmental.gui.CurrentAndPendingReportDetailForm;
import edu.mit.coeus.utils.CoeusFileChooser;
import java.io.File;
import java.util.HashMap;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Vector;
import java.io.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
/**
 * This class will pop up the Save File Dialog when the "Save As" Toolbar
 * is pressed for ProposalBase Window
 *
 * @version @version :1.0 May 14, 2003, 2:16 PM
 * @author  senthilar
 */
public class SaveAsDialog {
    /* Stores the file extensions that are supported */
    private static final String[] fileExt = {"csv","txt","htm","XLS"};
    private TableModel tblModel;
    private Hashtable searchData;
    private boolean isTableModel;
    private Vector searchResultRecords;
    private String path;
    
    //Added by sharath - 23-Oct-2003 to take table as data - Start
    private JTable table;
    private boolean isTable;  
    
    
    //Added by sharath - 23-Oct-2003 to take table as data - End   
    
    // Added 19th April 2011 - to support only XLS format - Start
    private boolean isSearchData;
    public static boolean isXLSFile;
    private static final String[] fileExtXLS = {"XLS"};
    private HashMap hmReport;
    // Added 19th April 2011 - to support only XLS format - End

 
    
    /**
     * Creates a new instance of SaveAsDialog
     * @param tblModel The model that contains the report data
     */
    public SaveAsDialog(TableModel tblModel) {
        this.tblModel = tblModel;
        isTableModel = true;
        buildFileChooser();
    }
    
    /** Creates a new instance of SaveAsDialog */
    public SaveAsDialog(Hashtable searchData) {
        this.searchData = searchData;
        isTableModel = false;
        isSearchData = true;
        buildFileChooser();
    }
    
    //Added by sharath - 23-Oct-2003 to take table as data - Start
    /** Creates a new instance of SaveAsDialog */
    public SaveAsDialog(JTable table) {
        this.table = table;
        isTableModel = false;
        isTable = true;
        buildFileChooser();
    }
     
    //Added by sharath - 23-Oct-2003 to take table as data - End
    
    //Added on - 20-Apr-2011 -to take hashMap as data - Start
     public SaveAsDialog(HashMap hmReport) {
        this.hmReport = hmReport;
        isTableModel = false;
        isTable = false;
        isSearchData = false;
        buildFileChooser();
    }
    //Added on - 20-Apr-2011 to take hashMap as data - End
    
    
    /**
     * This method will build the file chooser and display
     */
    public void buildFileChooser() {
        // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done - Start
//        JFileChooser fileChooser = new JFileChooser();
        CoeusFileChooser fileChooser = new CoeusFileChooser(CoeusGuiConstants.getMDIForm());
//        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        ExtensionFileFilter filter = null;
        fileChooser.resetFileFilter();
        //Added on - 20-Apr-2011 -to take XLS as file type - Start
        if(isXLSFile == true) {
            for (int i=0; i < fileExtXLS.length; i++) {
                filter = new ExtensionFileFilter();
                filter.addExtension(fileExtXLS[i]);
                filter.setDescription(fileExtXLS[i]);
                fileChooser.setFilter(filter);
            }
            isXLSFile = false;
        }
        //Added on - 20-Apr-2011 -to take XLS as file type - End
        else {
            for (int i=0; i < fileExt.length; i++) {
                filter = new ExtensionFileFilter();
                filter.addExtension(fileExt[i]);
                filter.setDescription(fileExt[i]);
//            fileChooser.addChoosableFilter(filter);
                fileChooser.setFilter(filter);
            }
        }
       
        
        int selected = fileChooser.showSaveDialog(CoeusGuiConstants.getMDIForm());
//        fileChooser.showFileChooser();
        switch ( selected ) {
//        if(fileChooser.isFileSelected()){
            case JFileChooser.APPROVE_OPTION:
//                File selectedFile = fileChooser.getSelectedFile();
                File selectedFile = fileChooser.getFileName();
//                File directory = fileChooser.getCurrentDirectory();
                File directory = fileChooser.getDirectory();
                // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done - End
                String fileName, ext;
                int index;
                
                fileName =  selectedFile.getName();
                    
                index = selectedFile.getName().lastIndexOf('.');
                
                if(index != -1) {
                    ext = selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.') + 1);
                    if(ext.equalsIgnoreCase(fileChooser.getFileFilter().getDescription())) {
                        fileName = selectedFile.getName().substring(0, index);
                    }
                }
                
                path = directory.getAbsolutePath() +
                System.getProperty("file.separator") +
                //selectedFile.getName() + "." +
                fileName + "." +
                fileChooser.getFileFilter().getDescription();
                if ( selectedFile != null ) {
                    // Generate the file according to the file type selected
                    //System.out.println("The Selected file = " + selectedFile.getName() + "." + fileChooser.getFileFilter().getDescription() );
                    //System.out.println("The Selected directory = " + directory.getAbsolutePath() );
                    /* Here use the factory pattern to create a specific subclass */
                    //BaseFileFormat format = new BaseFileFormat(tblModel,path);
                    if (exists(path)) {
                        showMessageDialog(path);
                    }
                    else{
                        FileFormat fileFormat = null;
                        try{
                            if (isTableModel) {
                                fileFormat = FileFormatFactory.createFileFormat(tblModel, path);
                            }
                            //Added by sharath - 23-Oct-2003 to take table as data - Start
                            else if(isTable) {
                                fileFormat = FileFormatFactory.createFileFormat(table, path);
                            }
                            //Added by sharath - 23-Oct-2003 to take table as data - End
                            else if(isSearchData) {
                                fileFormat = FileFormatFactory.createFileFormat(searchData, path);
                            }
                            //Added on - 20-Apr-2011 -to take hashMap as data - Start
                            else{
                                fileFormat = FileFormatFactory.createFileFormat(hmReport, path);
                            }
                            //Added on - 20-Apr-2011 -to take hashMap as data - End
                            fileFormat.processData();
                            
                        }
                        catch(FileNotFoundException fnfe){
                            JOptionPane.showMessageDialog(CoeusGuiConstants.getMDIForm(),"The file " + path + " is already opened.\nClose it and try again.");
                            return;
                        }
                        catch(IOException ioe){
                            JOptionPane.showMessageDialog(CoeusGuiConstants.getMDIForm(),"The file " + path + " is already opened.\nClose it and try again.");
                            return;
                        }
                    }
                }
                break;
            case JFileChooser.CANCEL_OPTION:
                break;
            default:
                break;
        }
    }
    
     
    
    /**
     * Check for whether the file already exists
     * @return return true is file already exists or false if not.
     */
    public boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }
    
    /**
     * This method show the confirmation dialog whether the file should be overwritten.
     */
    
    public void showMessageDialog(String path) {
        int result = JOptionPane.showConfirmDialog(CoeusGuiConstants.getMDIForm(), path+" already exists.\nDo you want to overwrite it?",
        "Save As", JOptionPane.YES_NO_OPTION);
        File outFile = new File(path);
        if (result == JOptionPane.YES_OPTION) {
            if (!outFile.canWrite()) {
                JOptionPane.showMessageDialog(CoeusGuiConstants.getMDIForm(),"The file " + path + " may already be opened.\nClose it and try again.");
                return;
            }
            FileFormat fileFormat = null;
            try{
                if (isTableModel) {
                    fileFormat = FileFormatFactory.createFileFormat(tblModel, path);
                }
                //Added by sharath - 27-Oct-2003 to take table as data - Start
                else if(isTable) {
                    fileFormat = FileFormatFactory.createFileFormat(table, path);
                   // fileFormat = FileFormatFactory.createFileFormat(dataVector,table, path);
                }
                //Added by sharath - 27-Oct-2003 to take table as data - End
                else if(isSearchData) {
                    fileFormat = FileFormatFactory.createFileFormat(searchData, path);
                }
                //Added on - 20-Apr-2011 -to take hashMap as data - Start
                else {
                    fileFormat = FileFormatFactory.createFileFormat(hmReport, path);
                }
                //Added on - 20-Apr-2011 -to take hashMap as data - End
                fileFormat.processData();
            }
            catch(FileNotFoundException fnfe){
                JOptionPane.showMessageDialog(CoeusGuiConstants.getMDIForm(),"The file " + path + " may already be opened.\nClose it and try again.");
                return;
            }
            catch (IOException ioe){
                JOptionPane.showMessageDialog(CoeusGuiConstants.getMDIForm(),"FileFormat1 The file " + path + " may be already opened.\nClose it and try again.");
                //System.out.println("IOException at FileFormt Constructor");
                ioe.printStackTrace();
            }
            /*
            try{
                fileFormat.writeDummy();
            }
            catch(FileNotFoundException fnfe){
                JOptionPane.showMessageDialog(null,"The file " + path + " is already opened.\nClose it and try again.");
                return;
            }
            catch(IOException ioe){
                JOptionPane.showMessageDialog(null,"The file " + path + " is already opened.\nClose it and try again.");
                return;
            }
             */
        }
    }
    
    /* This method is used only for Unit Testing purpose */
    public static void main(String args[]) {
        ObjectInputStream ois1 = null;
        ObjectInputStream ois2 = null;
        File file1 = new File("D:/senthil/temp/hastable.ser");
        File file2 = new File("D:/senthil/temp/vector.ser");
        Hashtable hash = null;
        Vector vector = null;
        try {
            ois1 = new ObjectInputStream(new FileInputStream(file1));
            hash = (Hashtable)ois1.readObject();
            ois1.close();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        final Hashtable hash2 = hash;
        JFrame frame = new JFrame("Testing the Dialog");
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        
        JButton test = new JButton("Testing");
        final TableModel tableModel = new JTable().getModel();
        test.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                SaveAsDialog app = new SaveAsDialog(hash2);
            }
        });
        Container content = frame.getContentPane();
        content.add(test);
        //content.add(table);
        frame.pack();
        frame.show();
    }
}
