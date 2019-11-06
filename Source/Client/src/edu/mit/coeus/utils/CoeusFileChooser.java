/*
 * CoeusFileChooser.java
 *
 * Created on July 21, 2003, 11:49 AM
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.utils.CoeusFileFilter;
import edu.mit.coeus.gui.CoeusAppletMDIForm;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;


/* @author  chandrashekara*/
/*This class will pop up the Open File Dialog.  */

public class CoeusFileChooser extends JComponent {
    
    private JFileChooser fileChooser;
    private File selectedFile;
    private CoeusAppletMDIForm mdiForm;
    private CoeusFileFilter filter;
    private byte[] fileContents = null;
    // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done
//    private static String[] selectedFileExtension={"pdf","rtf","doc","xml", "xsl"};
    private String[] selectedFileExtension={"pdf","doc","rtf","xml","xsl"}; // JM 06-02-2015 fixed file type order
    //Code modified by Vyjayanthi on 27/01/2004
    //Replaced Dialog with Component
    private java.awt.Component parent = null;   
    // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done
    private static File currentDirectory;
    /** Creates a new instance of CoeusFileChooser */
    public CoeusFileChooser(java.awt.Dialog parent){/*CoeusAppletMDIForm mdiForm*/
        this.parent = parent;
        initComponents();
    }
    
    //Added by Vyjayanthi
    /** Creates a new instance of CoeusFileChooser
     * @param mdiForm holds the frame
     */
    public CoeusFileChooser(java.awt.Frame frame){
        this.parent = frame;
        initComponents();        
    }
    
    /**Inializes the Components*/
    private void initComponents(){
        fileChooser = new JFileChooser(); 
        
        for(int i=0;i<selectedFileExtension.length;i++){
            filter = new CoeusFileFilter();
            filter.addExtension(selectedFileExtension[i]);
            filter.setDescription("");
            fileChooser.addChoosableFileFilter(filter); 
        }
    }
    
    /* This method will build the file chooser and display*/
    public void showFileChooser() {
        // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done
        if(currentDirectory != null){
            fileChooser.setCurrentDirectory(currentDirectory);
        }
        int returnVal = fileChooser.showOpenDialog(parent);
        if(returnVal==JFileChooser.CANCEL_OPTION){
            selectedFile = null;
            return;
        }
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        }
        if(selectedFile==null||selectedFile.getName().equals(""))
            return;
    }
    
    
    /** Added by chandra to select the entire directory instead of file
     *Get the selected directory content and display it.
     *Added on 28th Sept 2004
     *Bug Id #1090
     */
    public void showDirectory() {
        int result;
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setSelectedFile(selectedFile);
        // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done
        if(currentDirectory != null && selectedFile == null){
            fileChooser.setCurrentDirectory(currentDirectory);
        }
        result = fileChooser.showOpenDialog(parent);
        if ( result == JFileChooser.APPROVE_OPTION) {
          //  selectedFile = fileChooser.getCurrentDirectory();
            selectedFile  = fileChooser.getSelectedFile();
        }
        if(result == JFileChooser.CANCEL_OPTION){
            selectedFile = null;
            return ;
        }
        if(selectedFile==null || selectedFile.getName().equals("")){
            return ;
        }
    }// End showDirectory() - End Chandra 28 Sept 2004 Bug Id #1090
    
    public boolean isFileSelected()
    {
        if(selectedFile==null||selectedFile.getName().equals("")){
            return false;
        } else {
            // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done
            currentDirectory = fileChooser.getCurrentDirectory();
            return true;
        }
    }
     
    public int showSaveDialog(Component parent)throws HeadlessException {
        // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done - Start
        boolean isCurrentDirectoryNull = false;
        if(currentDirectory != null){
            fileChooser.setCurrentDirectory(currentDirectory);
        } else {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            isCurrentDirectoryNull = true;
        }
//        return fileChooser.showSaveDialog(parent);
        int returnVal =  fileChooser.showSaveDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            currentDirectory = fileChooser.getCurrentDirectory();
        } else if (isCurrentDirectoryNull){
            fileChooser.setCurrentDirectory(null);
        }
        return returnVal;
        // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done - End
    }
    
    
    
    //Added by Vyjayanthi
    /** Method to set the selected file extension */
    public void setSelectedFileExtension(String fileExtension){
        filter = new CoeusFileFilter();
        filter.addExtension(fileExtension);
        filter.setDescription("");
        fileChooser.resetChoosableFileFilters();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(filter);
    }
    
    public void setSelectedFileName(File f){
        fileChooser.setSelectedFile(f);
    }
   /** Added by chandra to fix #1090
    *@param File f
    *returns void 
    *returns the directory path for the selected directory structure.
    *28th sept 2004
    */
    public void setDirectory(File f){
        fileChooser.setCurrentDirectory(f);
        // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done 
        currentDirectory = f;
    }// End Chandra to fix #1090 on 28th Sept 2004
    
  
    
    //Added by Vyjayanthi
    /** Method to set the file extensions
     * @param fileExtension holds an array of the required file extensions
     */
    public void setSelectedFileExtension(String[] fileExtension){
        fileChooser.resetChoosableFileFilters();
        fileChooser.setAcceptAllFileFilterUsed(false);
        for( int index = 0; index < fileExtension.length; index++ ){
            filter = new CoeusFileFilter();
            filter.addExtension(fileExtension[index]);
            filter.setDescription("");
            fileChooser.setFileFilter(filter);
        }
    }
    
        /* This method will hold the Selected file instance
    //@returns the file in byte format to read.
         */
    public byte[] getFile(){
        
        try{
            int fileSize = (int)selectedFile.length();
            fileContents = new byte[fileSize];
            FileInputStream fileInputStream= new FileInputStream(selectedFile);
            fileInputStream.read(fileContents);
            //ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileContents);
            //byteArrayInputStream.close();
        }
        catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        return fileContents;
        
    }
    
        public String getSelectedFile()
    {
        if(isFileSelected()){
            return selectedFile.toString();
        }else{
           return null;
        }
    }
    


    
    public File getFileName() {
        return fileChooser.getSelectedFile();
    }
    
    
    public void setFileFilter(FileFilter filter) {
        fileChooser.setFileFilter(filter);
    }
    
    public void setAcceptAllFileFilterUsed(boolean accept) {
        fileChooser.setAcceptAllFileFilterUsed(accept);
    }
    
    
    
    
   /*  This method is used only for Unit Testing purpose 
    public static void main(String args[]){
        JFrame frame = new javax.swing.JFrame();
        java.awt.Container container = frame.getContentPane();
        container.setLayout(new java.awt.FlowLayout());
        JButton btnopenFile = new javax.swing.JButton("Open File");
        container.add(btnopenFile);
        frame.setSize(400,300);
        frame.setVisible(true);
        btnopenFile.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                CoeusFileChooser coeusFiPleChooser = new CoeusFileChooser();
                coeusFileChooser.initComponents();
                coeusFileChooser.showFileChooser();
                byte[] tempFileBytes = coeusFileChooser.getFile();
                for(int i=0;i<tempFileBytes.length;i++){
                    //System.out.println("bytes------------->");
                    System.out.print((char)tempFileBytes[i]);
                }
                //btnopenFileActionPerformed(e);
            }
        });
        //coeusFileChooser.showFileChooser();
        
        
    }*/
    // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done - Start
    /**
     * This method returns the property 'currentDirectory' of the 
     * 'fileChooser' of this class. 
     * @return File
     */
    public File getDirectory() {
        return fileChooser.getCurrentDirectory();
    }
    /**
     * setter method for the 'filter'. 
     * @param filter FileFilter
     */
    public void setFilter(FileFilter filter){
        fileChooser.setFileFilter(filter);
    }
    /**
     * getter method for the 'filter'.
     * @ return FileFilter
     */
    public FileFilter getFileFilter() {
        return fileChooser.getFileFilter();
    }
    /**
     * This methos resets the 'choosableFileFilters' of 'fileChooser' of this class. 
     */
    public void resetFileFilter() {
        fileChooser.resetChoosableFileFilters();
    }
 
    // COEUSQA-1925: Narrative upload recall the last folder from which an upload Can't be done - End
}