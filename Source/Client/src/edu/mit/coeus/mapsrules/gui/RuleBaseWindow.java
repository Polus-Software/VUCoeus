/*
 * RuleBaseWindow.java
 *
 * Created on October 17, 2005, 12:56 PM
 */

package edu.mit.coeus.mapsrules.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.event.InternalFrameEvent;

/**
 *
 * @author  chandrashekara
 */
public class RuleBaseWindow extends CoeusInternalFrame{
    private CoeusAppletMDIForm mdiForm;
    public  CoeusMenuItem inboxMenuItem,closeMenuItem, saveMenuItem,
    changePasswordMenuItem,
    /*Added for Case#3682 - Enhancements related to Delegations - Start*/
    delegationsMenuItem,
    /*Added for Case#3682 - Enhancements related to Delegations - End*/
    preferencesMenuItem, exitMenuItem,/*Case 2110 Start1*/currentLocksMenuItem/*Case 2110 End1*/;
    public CoeusToolBarButton btnSave,btnClose;
    private final String SEPERATOR="seperator";
    private CoeusMenu menuRuleData = null;
    /** Creates a new instance of RuleBaseWindow */
    public RuleBaseWindow(String title,CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
    }
    
     private void initComponents(){
        createRuleFileMenu();
        createRuleToolBar();
    }
     
     /**
     * constructs Budget File menu with sub menu Inbox,close, Save, Print setup
     *print summary, budget summary,change password, preferences, exit
     * @return CoeusMenu Protocol edit menu
     */
    public void createRuleFileMenu(){
                
        inboxMenuItem = new CoeusMenuItem("Inbox", null, true, true);
        inboxMenuItem.setMnemonic('I');
        //inboxMenuItem.addActionListener(this);
        
        closeMenuItem = new CoeusMenuItem("Close", null, true, true);
        closeMenuItem.setMnemonic('C');
        //closeMenuItem.addActionListener(this);
        
        saveMenuItem = new CoeusMenuItem("Save", null, true, true);
        saveMenuItem.setMnemonic('S');
        changePasswordMenuItem = new CoeusMenuItem("Change Password", null, true, true);
        changePasswordMenuItem.setMnemonic('p');
        //changePasswordMenuItem.addActionListener(this);
        
        //Case 2110 Start2
        currentLocksMenuItem = new CoeusMenuItem("Current Locks",null,true,true);
        currentLocksMenuItem.setMnemonic('L');
        
        //Case 2110 End2
        //Added for Case#3682 - Enhancements related to Delegations - Start 
        delegationsMenuItem = new CoeusMenuItem("Delegations...", null, true, true);
        delegationsMenuItem.setMnemonic('D');
        //Added for Case#3682 - Enhancements related to Delegations - End
        preferencesMenuItem = new CoeusMenuItem("Preferences...", null, true, true);
        preferencesMenuItem.setMnemonic('f');
        //preferencesMenuItem.addActionListener(this);
        
        exitMenuItem = new CoeusMenuItem("Exit", null, true, true);
        exitMenuItem.setMnemonic('x');
        //exitMenuItem.addActionListener(this);
        
    }
    
     public void createRuleToolBar() {
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null,"Close");
        
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null,"Save");
    }
     
      public JToolBar getRuleToolBar(){
        JToolBar toolBar = new JToolBar();
        toolBar.add(btnSave);
        toolBar.addSeparator();
        toolBar.add(btnClose);
        return toolBar;
    }
    
     /**
     * This method is used to add custom tool bar and menu bar to the application
     *  when the internal frame is activated.
     * @param e  InternalFrameEvent which delegates the event. */
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated( e );
        loadMenus();
    }
     // Activate the internalFrame based on the actionevent. when it is
    //deActivated unLoad the menus
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
    }
    
     // UnLoad the menus from MDIForm
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(getRuleFileMenu());
        mdiForm.getCoeusMenuBar().add(mdiForm.getFileMenu(),0);
    }
    
    private void loadMenus(){
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(getRuleFileMenu(), 0);
    }
    
     
    
     public CoeusMenu getRuleFileMenu() {
        Vector fileChildren = new Vector();
        fileChildren.add(inboxMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(closeMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(saveMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(changePasswordMenuItem);
        //Case 2110 Start
        fileChildren.add(SEPERATOR);
        fileChildren.add(currentLocksMenuItem);
        fileChildren.add(SEPERATOR);
        //Case 2110 End
        //Added for Case#3682 - Enhancements related to Delegations - Start 
        fileChildren.add(delegationsMenuItem);
        fileChildren.add(SEPERATOR);
        //Added for Case#3682 - Enhancements related to Delegations - End
        fileChildren.add(preferencesMenuItem);        
        fileChildren.add(exitMenuItem);
        if(menuRuleData == null){
            menuRuleData= new CoeusMenu("File", null, fileChildren, true, true);
            menuRuleData.setMnemonic('F');
        }
        
        return menuRuleData;
    }
     
      public void setMenus() {
        //setMenu(getRuleFileMenu(),0);
        setFrameToolBar(getRuleToolBar());
        this.setFrameIcon(mdiForm.getCoeusIcon());
    }
    
    
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
}
