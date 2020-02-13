/*
 * @(#)QuestionnaireBaseWindow.java September 19, 2006, 7:29 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * QuestionnaireBaseWindow.java
 *
 * Created on September 19, 2006, 7:29 PM
 */

package edu.mit.coeus.questionnaire.gui;

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
public class QuestionnaireBaseWindow extends CoeusInternalFrame{
    private CoeusAppletMDIForm mdiForm;
    public  CoeusMenuItem inboxMenuItem,closeMenuItem, saveMenuItem,
    changePasswordMenuItem,
    /*Added for Case#3682 - Enhancements related to Delegations - Start*/
    delegationsMenuItem,
    /*Added for Case#3682 - Enhancements related to Delegations - End*/
    preferencesMenuItem, exitMenuItem,/*Case 2110 Start1*/currentLocksMenuItem/*Case 2110 End1*/;
    //Code added for coeus4.3 questionnaire enhancements case#2946
    public  CoeusMenuItem printMenuItem;
    public CoeusToolBarButton btnPrint;
    public CoeusToolBarButton btnSave,btnClose;
    private final String SEPERATOR="seperator";
    private CoeusMenu questionnaireRuleData = null;
    // 4272: Maintain history of Questionnaires - Start
    private CoeusMenu questionnaireEditMenu;
    public  CoeusMenuItem deleteVersionMenuItem;
    public CoeusToolBarButton btnDeleteVersion;
    // 4272: Maintain history of Questionnaires - End
    /** Creates a new instance of QuestionnaireBaseWindow */
//    public QuestionnaireBaseWindow() {
//        
//    }
    
    public QuestionnaireBaseWindow(String title,CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    private void initComponents(){
        createQuestionnaireFileMenu();
        // 4272: Maintain history of Questionnaires 
        createQuestionnaireEditMenu();
        createQuestionnaireToolBar();
//        createQuestionnaireToolBar();
    }
    
    public void createQuestionnaireFileMenu(){
        
        inboxMenuItem = new CoeusMenuItem("Inbox", null, true, true);
        inboxMenuItem.setMnemonic('I');
        
        closeMenuItem = new CoeusMenuItem("Close", null, true, true);
        closeMenuItem.setMnemonic('C');
        
        saveMenuItem = new CoeusMenuItem("Save", null, true, true);
        saveMenuItem.setMnemonic('S');
        changePasswordMenuItem = new CoeusMenuItem("Change Password", null, true, true);
        changePasswordMenuItem.setMnemonic('p');
        
        currentLocksMenuItem = new CoeusMenuItem("Current Locks",null,true,true);
        currentLocksMenuItem.setMnemonic('L');
        
        //Added for Case#3682 - Enhancements related to Delegations - Start 
        delegationsMenuItem = new CoeusMenuItem("Delegations...", null, true, true);
        delegationsMenuItem.setMnemonic('D');
        //Added for Case#3682 - Enhancements related to Delegations - End
        
        preferencesMenuItem = new CoeusMenuItem("Preferences...", null, true, true);
        preferencesMenuItem.setMnemonic('f');
        
        exitMenuItem = new CoeusMenuItem("Exit", null, true, true);
        exitMenuItem.setMnemonic('x');
        
        //Code added for coeus4.3 questionnaire enhancement case#2946
        printMenuItem = new CoeusMenuItem("Print", null, true, true);
        printMenuItem.setMnemonic('r');        
        
    }
    
    public void createQuestionnaireToolBar() {
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null,"Close");
        
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null,"Save");
        
        //Code added for coeus4.3 questionnaire enhancement case#2946
        btnPrint = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_ICON)),
        null,"Print");    
        
        // c4272: Maintain history of Questionnaires  - Start
        btnDeleteVersion = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
                null,"Delete this version of Questionnaire");
        // 4272: Maintain history of Questionnaires - End
    }
    
    public JToolBar getToolBar(){
        JToolBar toolBar = new JToolBar();
        //Code added for coeus4.3 questionnaire enhancement case#2946
        toolBar.add(btnPrint);
        // 4272: Maintain history of Questionnaires - Start
        toolBar.add(btnDeleteVersion);
        toolBar.addSeparator();        
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
        mdiForm.getCoeusMenuBar().remove(getQuestionnaireFileMenu());
        mdiForm.getCoeusMenuBar().add(mdiForm.getFileMenu(),0);
        // 4272: Maintain history of Questionnaires - Start
        mdiForm.getCoeusMenuBar().remove(getQuestionnaireEditMenu());
        mdiForm.getCoeusMenuBar().add(mdiForm.getFileMenu(),1);
        // 4272: Maintain history of Questionnaires - End
    }
    
    private void loadMenus(){
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(getQuestionnaireFileMenu(), 0);
        // 4272: Maintain history of Questionnaires - Start
        // Commented for displaying the Maintain menu - Start
//        mdiForm.getCoeusMenuBar().remove(1);
        // Commented for displaying the Maintain menu - End
        mdiForm.getCoeusMenuBar().add(getQuestionnaireEditMenu(), 1);
        // 4272: Maintain history of Questionnaires - End
    }
    
    
    
    public CoeusMenu getQuestionnaireFileMenu() {
        Vector fileChildren = new Vector();
        fileChildren.add(inboxMenuItem);
        fileChildren.add(SEPERATOR);
        //Code added for coeus4.3 questionnaire enhancement case#2946
        fileChildren.add(printMenuItem);
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
        if(questionnaireRuleData == null){
            questionnaireRuleData= new CoeusMenu("File", null, fileChildren, true, true);
            questionnaireRuleData.setMnemonic('F');
        }
        
        return questionnaireRuleData;
    }
    
    public void setMenus() {
        setFrameToolBar(getToolBar());
        this.setFrameIcon(mdiForm.getCoeusIcon());
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    // 4272: Maintain history of Questionnaires - Start
    private void createQuestionnaireEditMenu() {
        deleteVersionMenuItem = new CoeusMenuItem("Delete this version", null, true, true);
        deleteVersionMenuItem.setMnemonic('D');
    }
    
    public CoeusMenu getQuestionnaireEditMenu() {
        Vector vecEditMenuItems = new Vector();
        vecEditMenuItems.add(deleteVersionMenuItem);
        
        if(questionnaireEditMenu == null){
            questionnaireEditMenu = new CoeusMenu("Edit", null, vecEditMenuItems, true, true);
            questionnaireEditMenu.setMnemonic('E');
        }
        
        return questionnaireEditMenu;
    }
    // 4272: Maintain history of Questionnaires - End
}
