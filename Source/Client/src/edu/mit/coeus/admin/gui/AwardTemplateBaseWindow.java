/*
 *  AwardTemplateBaseWindow.java
 *
 * Created on December 14, 2004, 5:34 PM
 * @author  bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.admin.gui;

import edu.mit.coeus.admin.bean.TemplateBaseBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusFileMenu;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameEvent;

public class AwardTemplateBaseWindow extends CoeusInternalFrame {
    
    // Tool bar items
    public CoeusToolBarButton btnSeclectTemplate,btnSave, btnClose;
    //case 1632 begin
    public CoeusToolBarButton btnPrint;
    public CoeusMenuItem mnuItmPrint;
    //case 1632 end
    
    private TemplateBaseBean templateBaseBean;
    //File Menu Items
    public CoeusMenuItem mnuItmInbox, mnuItmClose,mnuItmSave,mnuItmSeclectTemplate, 
    mnuItmChangePassword, mnuItmPreferences, mnuItmExit,
    /*Added for Case#3682 - Enhancements related to Delegations - Start*/
    mnuItmDelegations,
    /*Added Case#3682 - Enhancements related to Delegations - End*/
    /*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/;
    
    private CoeusMenu mnuFile;

    // To indicate horizondal seperator in menu items
    private final String SEPERATOR="seperator";
    
    private CoeusAppletMDIForm mdiForm;
    
    private char functionType;
    
    
    /** Creates a new instance of AwardTemplateBaseWindow */
    public AwardTemplateBaseWindow(String title, CoeusAppletMDIForm coeusAppletMDIForm) {
        super(title, coeusAppletMDIForm);
        this.mdiForm = coeusAppletMDIForm;
        initComponents();
    }
    /**
     * Initializes the components
     */
    private void initComponents() {
        setFrameToolBar(getBaseWindowToolBar());
        prepareMenus();
    }
    
    /**
     * Creates the toolbar and returns it
     */
    private JToolBar getBaseWindowToolBar() {
        JToolBar toolBar = new JToolBar();
        
        btnSeclectTemplate = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SELECT_TEMPLATE_ICON)),
        null, "Select Template");

        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        //case 1632 begin
        btnPrint = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_ICON)),
        null, "Print");
        //case 1632 end
        
        toolBar.add(btnSeclectTemplate);
        //case 1632 begin
        toolBar.add(btnPrint);
        //case 1632 end
        toolBar.add(btnSave);
        toolBar.addSeparator();
        toolBar.add(btnClose);

        return toolBar;
    }
    
    /**
     * Prepares the menu
     */
    private void prepareMenus() {
        //build File Menu
        Vector vecFile = new Vector();
        
        mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
        mnuItmInbox.setMnemonic('I');
      
        mnuItmClose = new CoeusMenuItem("Close", null, true, true);
        mnuItmClose.setMnemonic('C');
        
        mnuItmSave = new CoeusMenuItem("Save", null, true, true);
        mnuItmSave.setMnemonic('S');
        mnuItmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        
        mnuItmSeclectTemplate = new CoeusMenuItem("Select Template", null, true, true);
        mnuItmSeclectTemplate.setMnemonic('T');
        
        mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
        mnuItmChangePassword.setMnemonic('h');
        
        //Added for Case#3682 - Enhancements related to Delegations - Start
        mnuItmDelegations = new CoeusMenuItem("Delegations",null,true,true);
        mnuItmDelegations.setMnemonic('D');
        //Added for Case#3682 - Enhancements related to Delegations - End
        mnuItmPreferences = new CoeusMenuItem("Preferences", null, true, true);
        mnuItmPreferences.setMnemonic('P');
        
        mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
        mnuItmExit.setMnemonic('x');
        
        //case 1632 begin
        mnuItmPrint = new CoeusMenuItem("Print", null, true, true);
        mnuItmPrint.setMnemonic('t');
        //case 1632 end
        
        //Case 2110 Start
        mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
        mnuItmCurrentLocks.setMnemonic('L');
        //Case 2110 End
        
        vecFile.add(mnuItmInbox);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmClose);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmSave);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmSeclectTemplate);
        //case 1632 begin
        vecFile.add(mnuItmPrint);
        //case 1632 end
        vecFile.add(mnuItmChangePassword);
        //Case 2110 Start
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmCurrentLocks);
        vecFile.add(SEPERATOR);
        //Case 2110 End
        //Added for Case#3682 - Enhancements related to Delegations - Start
        vecFile.add(mnuItmDelegations);
        vecFile.add(SEPERATOR);
        //Added for Case#3682 - Enhancements related to Delegations - End
        vecFile.add(mnuItmPreferences);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmExit);

        mnuFile = new CoeusMenu("File", null, vecFile, true, true);
        mnuFile.setMnemonic('F');
   }
   
    /**
     * Loads the menu
     */
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
    }
    
    /**
     * Unloads the menu
     */  
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
    }
    
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated(e);
        loadMenus();
        mdiForm.getCoeusMenuBar().validate();
    }
    
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
        mdiForm.getCoeusMenuBar().revalidate();
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
    /**
     * Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /**
     * Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    /**
     * Getter for property templateBaseBean.
     * @return Value of property templateBaseBean.
     */
    public edu.mit.coeus.admin.bean.TemplateBaseBean getTemplateBaseBean() {
        return templateBaseBean;
    }
    
    /**
     * Setter for property templateBaseBean.
     * @param templateBaseBean New value of property templateBaseBean.
     */
    public void setTemplateBaseBean(edu.mit.coeus.admin.bean.TemplateBaseBean templateBaseBean) {
        this.templateBaseBean = templateBaseBean;
    }
    
}
