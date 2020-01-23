/*
 * ReportGui.java
 *
 * Created on October 18, 2004, 2:57 PM
 * @author  bijosht
 *@modify Chandra
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.utils;

import java.io.*;
import java.util.Vector;
import javax.swing.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.awt.Cursor;

import org.w3c.dom.*;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;


public class ReportGui implements ActionListener{
    private PlaceHolderContainer placeHolderContainer; //= new PlaceHolderContainer();
    JFrame frame;
    String modifiedXsl;
    private byte[] xslTemplate;
    private boolean clicked;
    private byte[] xslBytes;
    private CoeusDlgWindow dlgWindow;
    private Hashtable xslData;
    public static final int CLICKED_OK=1;
    private int action;
    private CoeusAppletMDIForm mdiForm;
    private static final int WIDTH=400;
    private static final int HEIGHT=230;
    private CoeusMessageResources coeusMessageResources;
    private static final String DISCARD_CUSTOM_TAGS = "report_exceptionCode.1000";
    private String protoCorresTypeDesc;
    
    //PlaceHolderContainer placeHolderContainer;
    
    
    /** Creates a new instance of Test */
    public ReportGui(CoeusAppletMDIForm mdiForm,String protoCorresTypeDesc) {
        this.mdiForm = mdiForm;
        this.protoCorresTypeDesc = protoCorresTypeDesc;
        coeusMessageResources = CoeusMessageResources.getInstance();
        placeHolderContainer = new PlaceHolderContainer();
        
    }
    
    
    public PlaceHolderContainer getPlaceHolderPanel(Vector nodes) {
        placeHolderContainer.setData(nodes);
        return placeHolderContainer;
    }//End getPlaceHolderPanel
    
    public void setTemplateData(CoeusVector cvTemplateData) {
        placeHolderContainer = getPlaceHolderPanel(cvTemplateData);
        placeHolderContainer.btnOk.addActionListener(this);
    }
    /**@return int value
     *Specifies the which action is performed
     *Check whether Ok is clicked or not!
     */
    public int displayReportGui() {
        dlgWindow.setVisible(true);
        return action;
    }
    
    public java.awt.Component getComponent(){
        return placeHolderContainer.getComp();
    }
    
    /** Set the Dialog box and set all the listeners for the dialog box
     */
    public void postInitComponents(){
        dlgWindow = new CoeusDlgWindow(mdiForm);
        dlgWindow.getContentPane().add(placeHolderContainer.getComp());
        dlgWindow.setTitle(""+protoCorresTypeDesc);
        dlgWindow.setFont(edu.mit.coeus.gui.CoeusFontFactory.getLabelFont());
        dlgWindow.setModal(true);
        dlgWindow.setResizable(false);
        dlgWindow.pack();
        //dlgWindow.setSize(WIDTH,HEIGHT);
        
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dlgSize = dlgWindow.getSize();
        dlgWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgWindow.addEscapeKeyListener(
        new javax.swing.AbstractAction("escPressed"){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                performCancelAction();
            }
        });
        dlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgWindow.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent e){
                performCancelAction();
            }
        });
        
        dlgWindow.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                placeHolderContainer.btnOk.requestFocusInWindow();
            }
        });
        
    }
    
    
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        try  {
            dlgWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if(source.equals(placeHolderContainer.btnOk)){
                setClicked(true);
                setAction(CLICKED_OK);
                Vector nodes = placeHolderContainer.getData();
                Vector vecTags = placeHolderContainer.getTags();
                Hashtable htData = new Hashtable();
                String key;
                String value;
                for (int index=0;index<vecTags.size();index++) {
                    key = vecTags.get(index).toString();
                    value = nodes.get(index).toString();
                    value = convertToHTML(value);
                    htData.put(key, value);
                }
                setXslData(htData);
                dlgWindow.setVisible(false);
            }
        } finally {
            dlgWindow.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
    }
    
    public String convertToHTML(String s){
        StringBuffer sb = new StringBuffer();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            switch (c) {
                case '$': sb.append("&#36;"); break;
                case '<': sb.append("&lt;"); break;
                case '>': sb.append("&gt;"); break;
                case '&': sb.append("&amp;"); break;
                case '"': sb.append("&quot;"); break;
                case '€': sb.append("&euro;"); break;
                case '!': sb.append("&#33"); break;
                case '\'': sb.append("&#39"); break;
                case ':': sb.append("&#58"); break;
                case ';': sb.append("&#59"); break;
                case '?': sb.append("&#63"); break;
                case '[': sb.append("&#91"); break;
                case ']': sb.append("&#93"); break;
                case '^': sb.append("&#93"); break;
                case '@': sb.append("&#64"); break;
                case '\n': sb.append("&#10;"); break;
                //case ' ': sb.append("&nbsp;");break;
                
                default:  sb.append(c); break;
            }
        }
        return sb.toString();
    }
    
    
    
    
    
    /** If user checks cancel button then ask for the confirmaion
     *and depending on selection open the report
     */
    private void performCancelAction(){
        dlgWindow.setVisible(false);
        //        int option = CoeusOptionPane.showQuestionDialog(
        //        coeusMessageResources.parseMessageKey(DISCARD_CUSTOM_TAGS),
        //        CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
        //        if(option == CoeusOptionPane.SELECTION_YES){
        //            dlgWindow.setVisible(false);
        //        }
    }
    
    /**
     * Getter for property xslTemplate.
     * @return Value of property xslTemplate.
     */
    public byte[] getXslTemplate() {
        return this.xslTemplate;
    }
    
    /**
     * Setter for property xslTemplate.
     * @param xslTemplate New value of property xslTemplate.
     */
    public void setXslTemplate(byte[] xslTemplate) {
        this.xslTemplate = xslTemplate;
    }
    
    /**
     * Getter for property clicked.
     * @return Value of property clicked.
     */
    public boolean isClicked() {
        return clicked;
    }
    
    /**
     * Setter for property clicked.
     * @param clicked New value of property clicked.
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
    
    /**
     * Getter for property placeHolderContainer.
     * @return Value of property placeHolderContainer.
     */
    public edu.mit.coeus.utils.PlaceHolderContainer getPlaceHolderContainer() {
        return placeHolderContainer;
    }
    
    /**
     * Setter for property placeHolderContainer.
     * @param placeHolderContainer New value of property placeHolderContainer.
     */
    public void setPlaceHolderContainer(edu.mit.coeus.utils.PlaceHolderContainer placeHolderContainer) {
        this.placeHolderContainer = placeHolderContainer;
    }
    
    /** Getter for property xslData.
     * @return Value of property xslData.
     *
     */
    public java.util.Hashtable getXslData() {
        return xslData;
    }
    
    /** Setter for property xslData.
     * @param xslData New value of property xslData.
     *
     */
    public void setXslData(java.util.Hashtable xslData) {
        this.xslData = xslData;
    }
    
    /** Getter for property action.
     * @return Value of property action.
     *
     */
    public int getAction() {
        return action;
    }
    
    /** Setter for property action.
     * @param action New value of property action.
     *
     */
    public void setAction(int action) {
        this.action = action;
    }
    
}
