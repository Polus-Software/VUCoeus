/*
 * S2SSubmissionList.java
 *
 * Created on May 12, 2005, 9:03 AM
 */

package edu.mit.coeus.s2s.gui;

/**
 *
 * @author  geot
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.saveas.SaveAsDialog;

public class S2SSubmissionList extends CoeusInternalFrame{
    
    public JTable tblResults;
    private JScrollPane scrPnResults;
    
    private CoeusAppletMDIForm mdiForm;
    
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    
    public CoeusToolBarButton btnS2SDetails, btnSearch, btnSaveAs, btnClose ,btnSort;
    
    //Tools Menu Items
    public CoeusMenuItem mnuItmSearch;
    
    //Edit Menu Items
    public CoeusMenuItem mnuS2SDetails, mnuDisplayProposal;
    
    private CoeusMenu mnuEdit, mnuTools;
    
    /** Creates a new instance of AwardBaseWindow */
    public S2SSubmissionList(String title, CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm,CoeusInternalFrame.LIST_MODE);
        this.mdiForm = mdiForm;
    }
    
    public void initComponents(JTable tblEmptyResults) {
        
        setFrameToolBar(getS2SToolBar());
        prepareMenus();
        
        tblResults = tblEmptyResults;
        
        scrPnResults = new JScrollPane(tblResults);
        
        //Bug Fix : background color should be white not gray - START
        scrPnResults.getViewport().setBackground(Color.white);
        //Bug Fix : background color should be white not gray - END
        
        getContentPane().add(scrPnResults);
    }
    
    private JToolBar getS2SToolBar() {
        JToolBar s2sToolBar = new JToolBar();
        
        btnS2SDetails = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
        null, "S2S Details");
        
        btnSearch = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
        null, "Search for S2S Submission List");
        
        btnSaveAs = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
        null, "Save As");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");

        btnSort = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),
        null, "Sort");
        
        s2sToolBar.add(btnS2SDetails);
        s2sToolBar.addSeparator();
        s2sToolBar.add(btnSort);
        s2sToolBar.add(btnSearch);
        s2sToolBar.add(btnSaveAs);
        s2sToolBar.addSeparator();
        s2sToolBar.add(btnClose);
        
        return s2sToolBar;
    }
    
    private void prepareMenus() {
        //build Edit Menu
        Vector vecEdit = new Vector();
        
        mnuS2SDetails = new CoeusMenuItem("S2S Details", null, true, true);
        mnuS2SDetails.setMnemonic('d');
        
        mnuDisplayProposal = new CoeusMenuItem("Display proposal", null, true, true);
        
        vecEdit.add(mnuS2SDetails);
        vecEdit.add(mnuDisplayProposal);
        
        //Build tools Menu.
        mnuItmSearch = new CoeusMenuItem("Search", null,true, true);
        mnuItmSearch.setMnemonic('S');
        
        Vector vecTools = new Vector();
        vecTools.add(mnuItmSearch);
        
//        mnuFile = new CoeusMenu("File", null, vecFile, true, true);
//        mnuFile.setMnemonic('F');
        
        mnuEdit = new CoeusMenu("Edit", null, vecEdit, true, true);
        mnuEdit.setMnemonic('E');
        
        mnuTools = new CoeusMenu("Tools", null, vecTools, true, true);
        mnuTools.setMnemonic('T');
    }
    
    private void loadMenus() {
        
        mdiForm.getCoeusMenuBar().add(mnuEdit, 1);
        mdiForm.getCoeusMenuBar().add(mnuTools, 6);
        //Setting up Edit Menu Items
        
        mdiForm.getCoeusMenuBar().validate();
    }
    
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuEdit);
        mdiForm.getCoeusMenuBar().remove(mnuTools);
        
        mdiForm.getCoeusMenuBar().validate();
    }
    
    public void displayResults(JTable results) {
        if(results == null) return ;
        tblResults = results;
        scrPnResults.setViewportView(tblResults);
        this.revalidate();
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
        SaveAsDialog saveAsDialog = new SaveAsDialog(tblResults);
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        int clickCount = mouseEvent.getClickCount();
        if(clickCount != 2) return ;
    }
    
    public void mouseEntered(MouseEvent mouseEvent) {
    }
    
    public void mouseExited(MouseEvent mouseEvent) {
    }
    
    public void mousePressed(MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(MouseEvent mouseEvent) {
    }
    
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated(e);
        loadMenus();
    }
    
    
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
        
    }
    
}
