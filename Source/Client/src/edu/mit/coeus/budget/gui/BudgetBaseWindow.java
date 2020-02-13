/*
 * BudgetBaseWindow.java
 *
 * Created on September 30, 2003, 11:20 AM
 */

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.budget.gui.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JToolBar;
/**
 *
 * @author  chandrashekara
 */
public class BudgetBaseWindow extends CoeusInternalFrame{
    
     /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";

    
    /** Menus for the Budget
     */
    // public CoeusMenu fileMenu, editMenu,viewMenu,budgetPeriodsMenu, itemsMenu;
    
    /** Menu Items for the budget - File Menu
     */
    public CoeusMenuItem inboxMenuItem,closeMenuItem, saveMenuItem,printSummaryMenuItem,
    budgetSummaryMenuItem,changePasswordMenuItem,
    /*Added for Case#3682 - Enhancements related to Delegations - Start*/
    delegationsMenuItem,
    /*Added for Case#3682 - Enhancements related to Delegations - end*/
    preferencesMenuItem, exitMenuItem/*, printSetupMenuItem*/;
    
    /** Menu Items for the Budget - Edit menu
     */
    public CoeusMenuItem ratesMenuItem,personsMenuItem,costSharingDistributionMenuItem,
    underRecoveryDistibutionMenuItem,budgetJustificationMenuItem,
    //Case #1625 Start 1
    projectIncomeMenuItem//Case #1625 End 1*
    //case #1626 start 1
    ,budgetModularMenuItem
    //Case #1626 end 1
    ,subAwardMenuItem ,
    //proposal hierarchy
    syncToParentMenuItem ;
    //proposal hierarchy
    
    /** Menu Items for the Budget - View menu
     */
    public CoeusMenuItem customizeMenuItem, salarymenuItem;
    
    /** Menu Items for Budget - Budget Periods
     */
    public CoeusMenuItem addMenuItem,deleteMenuItem,calculateAllPeridosMenuItem,
    calculateCurrentPeriodMenuItem,generateAllPeriodMenuItem,adjustPeriodBoundariesMenuItem;
    
    /** Menu Items for Budget - Line Items
     */
    public CoeusMenuItem addLineItemMenuItem,insertLineItemMenuItem,deleteLineItemMenuItem,
    editDetailsMenuItem,selectCostElementMenuItem,personnelBudgetMenuItem,
    syncToPeriodCostLimitMenuItem,applytoLaterPeriodsMenuItem,syncToDirectCostLimitMenuItem;
    
    /** Menu Items for Budget - Actions 
     */
    //Added with case 2158
     public CoeusMenuItem mnItmValidChk, mnuItmSyncCalLineItemCost;
    // Toolbar for Budget
    
    public CoeusToolBarButton btnAddLineItem;
    public CoeusToolBarButton btnEditLineItem;
    public CoeusToolBarButton btnDeleteLineItem;
    
    public CoeusToolBarButton btnPersonnelBudgetForLineItem;
    public CoeusToolBarButton btnCalculateBudget;
    public CoeusToolBarButton btnGenerateallPeriods;
    public CoeusToolBarButton btnMaintainRatesForProposal;
    public CoeusToolBarButton btnBudgetPersons;
    public CoeusToolBarButton btnCustomizeView;
    
    public CoeusToolBarButton btnSave;
    public CoeusToolBarButton btnClose;
    
    //Proposal Hierarchy Enhancment Start
    public CoeusToolBarButton btnSync;    
    //Proposal Hierarchy Enhancment End
    
    //Main MDI Form.
    public CoeusAppletMDIForm mdiForm;// = null;
    
    public BudgetSummaryForm budgetSummaryForm;
    
    //holds the Tabbed pane of Budget Summary screens
    public JTabbedPane tbdPnBudgetSummary;// = null;
    
    public char functionType;
        
    /** Constructor to create new <CODE>BudgetBaseWindow</CODE> with the given
     * <CODE>CoeusAppletMDIForm</CODE> as parent.
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public BudgetBaseWindow(CoeusAppletMDIForm mdiForm) {
        super("Budget", mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
        mdiForm.putFrame(CoeusGuiConstants.BUDGET_FRAME_TITLE,this);
        mdiForm.getDeskTopPane().add(this);
    }
    
    /** Creates a new instance of BudgetBaseWindow */
    public BudgetBaseWindow(String title, CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.functionType = functionType;
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    /** To construct all the components related to budget
     */
    
    private void initComponents(){
        createBudgetFileMenu();
        createBudgetEditMenu();
        createBudgetViewMenu();
        createBudgetActionMenu();//Case 2158
        createBudgetPeriodsMenu();
        createBudgetItemMenu();
        createBudgetToolBar();
    }
    
    public void setMenus() {
        setMenu(getBudgetFileMenu(),0);
        setMenu(getBudgetEditMenu(),1);
        setMenu(getBudgetViewMenu(),2);
        //Modified with case 2158: Budget Validations - Start
        setMenu(getBudgetActionMenu(),3);
        setMenu(getBudgetPeriodsMenu(),4);
        setMenu(getBudgetItemMenu(),5);
        //2158 End
        setFrameIcon(mdiForm.getCoeusIcon());
        setFrameToolBar(getBudgetToolBar());
        this.setFrameIcon(mdiForm.getCoeusIcon());
    }
    
    public CoeusMenu getBudgetFileMenu() {
        CoeusMenu menuBudgetFile = null;
        Vector fileChildren = new Vector();
        fileChildren.add(inboxMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(closeMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(saveMenuItem);
        fileChildren.add(SEPERATOR);
        
        //Commented since we are not using it in Coeus 4.0
        //fileChildren.add(printSetupMenuItem);
        fileChildren.add(printSummaryMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(budgetSummaryMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(changePasswordMenuItem);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        fileChildren.add(delegationsMenuItem);
        fileChildren.add(SEPERATOR);
        //Added for Case#3682 - Enhancements related to Delegations - End
        fileChildren.add(preferencesMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(exitMenuItem);
        
        menuBudgetFile = new CoeusMenu("File", null, fileChildren, true, true);
        menuBudgetFile.setMnemonic('F');
        return menuBudgetFile;
    }
    
    /**
     * constructs Budget File menu with sub menu Inbox,close, Save, Print setup
     *print summary, budget summary,change password, preferences, exit
     * @return CoeusMenu Protocol edit menu
     */
    public void createBudgetFileMenu(){
                
        inboxMenuItem = new CoeusMenuItem("Inbox", null, true, true);
        inboxMenuItem.setMnemonic('I');
        //inboxMenuItem.addActionListener(this);
        
        closeMenuItem = new CoeusMenuItem("Close", null, true, true);
        closeMenuItem.setMnemonic('C');
        //closeMenuItem.addActionListener(this);
        
        saveMenuItem = new CoeusMenuItem("Save", null, true, true);
        saveMenuItem.setMnemonic('S');
        //saveMenuItem.addActionListener(this);
        
        //Commented since we are not using it in Coeus 4.0
        //printSetupMenuItem = new CoeusMenuItem("Print Setup...", null, true, true);
        //printSetupMenuItem.setMnemonic('u');
        //printSetupMenuItem.addActionListener(this);
        
        printSummaryMenuItem = new CoeusMenuItem("Print Total", null, true, true);
        printSummaryMenuItem.setMnemonic('y');
//        printSummaryMenuItem.setEnabled(false);
        //printSummaryMenuItem.addActionListener(this);
        
        budgetSummaryMenuItem = new CoeusMenuItem("Budget Summary...", null, true, true);
        budgetSummaryMenuItem.setMnemonic('B');
        //budgetSummaryMenuItem.addActionListener(this);
        
        changePasswordMenuItem = new CoeusMenuItem("Change Password", null, true, true);
        changePasswordMenuItem.setMnemonic('p');
        //changePasswordMenuItem.addActionListener(this);
        
        //Added for Case#3682 - Enhancements related to Delegations - Start 
        delegationsMenuItem = new CoeusMenuItem("Delegations...", null, true, true);
        delegationsMenuItem.setMnemonic('g');
        //Added for Case#3682 - Enhancements related to Delegations - End
        
        preferencesMenuItem = new CoeusMenuItem("Preferences...", null, true, true);
        preferencesMenuItem.setMnemonic('f');
        //preferencesMenuItem.addActionListener(this);
        
        exitMenuItem = new CoeusMenuItem("Exit", null, true, true);
        exitMenuItem.setMnemonic('x');
        //exitMenuItem.addActionListener(this);
        
        
    }
    
    public void createBudgetEditMenu() {
        ratesMenuItem = new CoeusMenuItem("Rates...", null, true, true);
        ratesMenuItem.setMnemonic('R');
        //ratesMenuItem.addActionListener(this);
        
        personsMenuItem = new CoeusMenuItem("Persons...", null, true, true);
        personsMenuItem.setMnemonic('P');
        //personsMenuItem.addActionListener(this);
        
        costSharingDistributionMenuItem = new CoeusMenuItem("Cost Sharing Distribution...", null, true, true);
        costSharingDistributionMenuItem.setMnemonic('C');
        //costSharingDistributionMenuItem.addActionListener(this);
        
        underRecoveryDistibutionMenuItem = new CoeusMenuItem("Under Recovery Distribution...", null, true, true);
        underRecoveryDistibutionMenuItem.setMnemonic('U');
        //underRecoveryDistibutionMenuItem.addActionListener(this);
        
        budgetJustificationMenuItem = new CoeusMenuItem("Budget Justification...", null, true, true);
        budgetJustificationMenuItem.setMnemonic('B');
        //budgetJustificationMenuItem.addActionListener(this);
        //Case #1625 Start 2
        projectIncomeMenuItem = new CoeusMenuItem("Project Income...", null, true, true);
        projectIncomeMenuItem.setMnemonic('I');
        //Case #1625 End 2
        //Case #1626 Start 2
        budgetModularMenuItem=new CoeusMenuItem("Modular Budget",null,true,true);
        budgetModularMenuItem.setMnemonic('M');
        //Case #1626 End 2
        
        subAwardMenuItem = new CoeusMenuItem("Sub Award", null, true, true);
        subAwardMenuItem.setMnemonic('S');
        //proposal hierarchy
        syncToParentMenuItem = new CoeusMenuItem("Sync All" , null ,true,true);
        syncToParentMenuItem.setMnemonic('Y');
        //proposal hierarchy
    }
    
    public CoeusMenu getBudgetEditMenu(){
        
        CoeusMenu menuBudgetEdit = null;
        Vector editChildren = new Vector();
                
        editChildren.add(ratesMenuItem);
        editChildren.add(personsMenuItem);
        editChildren.add(costSharingDistributionMenuItem);
        editChildren.add(underRecoveryDistibutionMenuItem);
        editChildren.add(budgetJustificationMenuItem);
        //Case #1625 Start 3
        editChildren.add(projectIncomeMenuItem);
        //Case #1625 End 3
        //Case #1626 Start 3
        editChildren.add(budgetModularMenuItem);
        //Case #1626 End 3
        editChildren.add(subAwardMenuItem);
        //proposal hierarchy
        editChildren.add(syncToParentMenuItem);
        //proposal hierarchy
        
        menuBudgetEdit = new CoeusMenu("Edit", null, editChildren, true, true);
        menuBudgetEdit.setMnemonic('E');
        
        return menuBudgetEdit;
    }
    
    public void createBudgetViewMenu() {
        customizeMenuItem = new CoeusMenuItem("Customize...", null, true, true);
        customizeMenuItem.setMnemonic('C');
        //customizeMenuItem.addActionListener(this);
        
        salarymenuItem = new CoeusMenuItem("Salaries...", null, true, true);
        salarymenuItem.setMnemonic('S');
        //salarymenuItem.addActionListener(this);
    }
    
    public CoeusMenu getBudgetViewMenu(){
        
        CoeusMenu menuBudgetView = null;
        Vector viewChildren = new Vector();
        viewChildren.add(customizeMenuItem);
        viewChildren.add(SEPERATOR);
        viewChildren.add(salarymenuItem);   
        menuBudgetView = new CoeusMenu("View", null, viewChildren,true,true);
        menuBudgetView.setMnemonic('V');
        return menuBudgetView;
    }
    
    //Added with case 2158: Budgetary Validations
    public void createBudgetActionMenu(){
        mnItmValidChk = new CoeusMenuItem("Validation Checks..", null,true,true);
        mnItmValidChk.setMnemonic('V');
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
        mnuItmSyncCalLineItemCost= new CoeusMenuItem("Sync Calculated Line Item costs..", null,true,true);
        mnuItmSyncCalLineItemCost.setMnemonic('Y');
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
    }
    
     public CoeusMenu getBudgetActionMenu(){
        
        CoeusMenu mnBudgetAction = null;
        Vector actionChildren = new Vector();
        
        actionChildren.add(mnItmValidChk);   
        actionChildren.add(mnuItmSyncCalLineItemCost);
        mnBudgetAction = new CoeusMenu("Action", null, actionChildren,true,true);
        mnBudgetAction.setMnemonic('A');
        return mnBudgetAction;
    }
    //case 2158 : End
     
    public void createBudgetPeriodsMenu() {
        addMenuItem = new CoeusMenuItem("Add...", null, true, true);
        addMenuItem.setMnemonic('A');
        //addMenuItem.addActionListener(this);
        
        deleteMenuItem = new CoeusMenuItem("Delete...", null, true, true);
        deleteMenuItem.setMnemonic('D');
        //deleteMenuItem.addActionListener(this);
        
        calculateAllPeridosMenuItem = new CoeusMenuItem("Calculate All Periods", null, true, true);
        calculateAllPeridosMenuItem.setMnemonic('C');
        //calculateAllPeridosMenuItem.addActionListener(this);
        
        calculateCurrentPeriodMenuItem = new CoeusMenuItem("Calculate Current Period", null, true, true);
        calculateCurrentPeriodMenuItem.setMnemonic('P');
        //calculateCurrentPeriodMenuItem.addActionListener(this);
        
        generateAllPeriodMenuItem = new CoeusMenuItem("Generate All Periods", null, true, true);
        generateAllPeriodMenuItem.setMnemonic('G');
        //generateAllPeriodMenuItem.addActionListener(this);
        
        adjustPeriodBoundariesMenuItem = new CoeusMenuItem("Adjust Period Boundaries", null, true, true);
        adjustPeriodBoundariesMenuItem.setMnemonic('B');
        //adjustPeriodBoundariesMenuItem.addActionListener(this);
    }
    
    public CoeusMenu getBudgetPeriodsMenu(){
        
        CoeusMenu menuBudgetPeriods = null;
        Vector periodChildren = new Vector();
        
        periodChildren.add(addMenuItem);
        periodChildren.add(deleteMenuItem);
        periodChildren.add(SEPERATOR);
        periodChildren.add(calculateAllPeridosMenuItem);
        periodChildren.add(calculateCurrentPeriodMenuItem);
        periodChildren.add(generateAllPeriodMenuItem);
         periodChildren.add(SEPERATOR);
        periodChildren.add(adjustPeriodBoundariesMenuItem);
        
        menuBudgetPeriods = new CoeusMenu("Budget Periods", null, periodChildren, true, true);
        menuBudgetPeriods.setMnemonic('B');
        return menuBudgetPeriods;
    }
    
    public void createBudgetItemMenu() {
        addLineItemMenuItem = new CoeusMenuItem("Add Line Item...", null, true, true);
        //addLineItemMenuItem.setMnemonic('A');
        addLineItemMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        //addLineItemMenuItem.addActionListener(this);
        
        insertLineItemMenuItem = new CoeusMenuItem("Insert Line Item...", null, true, true);
        //insertLineItemMenuItem.setMnemonic('I');
        insertLineItemMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        //insertLineItemMenuItem.addActionListener(this);
        
        deleteLineItemMenuItem = new CoeusMenuItem("Delete Line Item...", null, true, true);
        deleteLineItemMenuItem.setMnemonic('D');
        //deleteLineItemMenuItem.addActionListener(this);
        
        editDetailsMenuItem = new CoeusMenuItem("Edit Details...", null, true, true);
        editDetailsMenuItem.setMnemonic('E');
        //editDetailsMenuItem.addActionListener(this);
        
        selectCostElementMenuItem = new CoeusMenuItem("Select Cost Element...", null, true, true);
        selectCostElementMenuItem.setMnemonic('C');
        //selectCostElementMenuItem.addActionListener(this);
        
        personnelBudgetMenuItem = new CoeusMenuItem("Personnel Budget...", null, true, true);
        personnelBudgetMenuItem.setMnemonic('P');
        //personnelBudgetMenuItem.addActionListener(this);
        
        syncToPeriodCostLimitMenuItem = new CoeusMenuItem("Sync to Period Cost Limit", null, true, true);
        //syncToPeriodCostLimitMenuItem.setMnemonic('S');
        syncToPeriodCostLimitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));
        //syncToPeriodCostLimitMenuItem.addActionListener(this);
        
        applytoLaterPeriodsMenuItem = new CoeusMenuItem("Apply to later periods", null, true, true);
        applytoLaterPeriodsMenuItem.setMnemonic('p');
        //applytoLaterPeriodsMenuItem.addActionListener(this);
        //Code added for Case#3472 - Sync to Direct Cost Limit - starts
        //For adding total direct cost limit
        syncToDirectCostLimitMenuItem = new CoeusMenuItem("Sync to Direct Cost Limit", null, true, true);
        syncToDirectCostLimitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK));
        //Code added for Case#3472 - Sync to Direct Cost Limit - ends
    }
    
    public CoeusMenu getBudgetItemMenu(){
        
        CoeusMenu menuLineItem=null;
        Vector itemChildren = new Vector();
                
        itemChildren.add(addLineItemMenuItem);
        itemChildren.add(insertLineItemMenuItem);
        itemChildren.add(deleteLineItemMenuItem);
        itemChildren.add(SEPERATOR);
        itemChildren.add(editDetailsMenuItem);
        itemChildren.add(selectCostElementMenuItem);
        itemChildren.add(personnelBudgetMenuItem);
        itemChildren.add(SEPERATOR);
        itemChildren.add(syncToPeriodCostLimitMenuItem);
        //Code added for Case#3472 - Sync to Direct Cost Limit
        //For adding total direct cost limit
        itemChildren.add(syncToDirectCostLimitMenuItem);
        itemChildren.add(applytoLaterPeriodsMenuItem);
        
        menuLineItem = new CoeusMenu("Items", null, itemChildren, true, true);
        menuLineItem.setMnemonic('I');
        return menuLineItem;
        
    }
    
    public void createBudgetToolBar() {
        btnAddLineItem = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
        null,"Add Line Item");
        
        btnEditLineItem = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
        null,"Line Item Details");
        
        btnDeleteLineItem = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
        null,"Delete Line Item");
        
        btnPersonnelBudgetForLineItem = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.QUALIFICATIONS_ICON)),
        null,"Personnel Budget for Line Item");
        
        btnCalculateBudget = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CALCULATE_BUDGET)),
        null,"Calculate Budget");
        
        btnGenerateallPeriods = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.GENERATE_ALL_PERIODS)),
        null,"Generate All Periods");
        
        btnMaintainRatesForProposal = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.RATES_FOR_PROPOSAL)),
        null,"Maintain Rates for the Proposal");
        
        btnBudgetPersons = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PERSONS_ICON)),
        null,"Budget Persons...");
        
        btnCustomizeView = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CUSTOMIZE_VIEW)),
        null,"Customize View");
        
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null,"Save");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null,"Close");
        
        //Proposal Hierarchy Enhancment Start
        btnSync = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SYNC_ICON)),
        null,"Sync All");
        //Proposal Hierarchy Enhancment End
    }
    
    public JToolBar getBudgetToolBar(){
        
        JToolBar toolBar = new JToolBar();
        
        
        //        btnAddLineItem.addActionListener(this);
        //        btnEditLineItem.addActionListener(this);
        //        btnDeleteLineItem.addActionListener(this);
        //        btnPersonnelBudgetForLineItem.addActionListener(this);
        //        btnCalculateBudget.addActionListener(this);
        //        btnGenerateallPeriods.addActionListener(this);
        //        btnMaintainRatesForProposal.addActionListener(this);
        //        btnBudgetPersons.addActionListener(this);
        //        btnSave.addActionListener(this);
        //        btnClose.addActionListener(this);
        
        toolBar.add(btnAddLineItem);
        toolBar.add(btnEditLineItem);
        toolBar.add(btnDeleteLineItem);
        toolBar.add(btnPersonnelBudgetForLineItem);
        toolBar.addSeparator();
        toolBar.add(btnCalculateBudget);
        toolBar.add(btnGenerateallPeriods);
        toolBar.add(btnMaintainRatesForProposal);
        toolBar.add(btnBudgetPersons);
        toolBar.add(btnCustomizeView);
        toolBar.addSeparator();
        toolBar.add(btnSave);
         
        //Proposal Hierarchy Enhancment Start
        toolBar.addSeparator();
        ///** gnprh commneted for Proposal Hierarchy
        toolBar.add(btnSync);

        toolBar.addSeparator();
        //Proposal Hierarchy Enhancment End
        
        toolBar.add(btnClose);
        return toolBar;
    }
    
    /** Abstract method of CoeusInternalFrame
     */
    public void saveActiveSheet(){
    }
    
    /** Abstract method of CoeusInternalFrame
     */
    public void saveAsActiveSheet(){
    }

    /**
     * This method is used to add custom tool bar and menu bar to the application
     *  when the internal frame is activated.
     * @param e  InternalFrameEvent which delegates the event. */
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated( e );
        // recreate the menu bar
        mdiForm.getCoeusMenuBar().remove( 0 );
        mdiForm.getCoeusMenuBar().add(getBudgetFileMenu(),0);
        mdiForm.getCoeusMenuBar().add( getBudgetEditMenu(), 1 );
        mdiForm.getCoeusMenuBar().add( getBudgetViewMenu(), 2 );
        mdiForm.getCoeusMenuBar().add( getBudgetActionMenu(),3);
        mdiForm.getCoeusMenuBar().add( getBudgetPeriodsMenu(), 4 );
        mdiForm.getCoeusMenuBar().add( getBudgetItemMenu(), 5 );
        mdiForm.getCoeusMenuBar().remove( 6 );
        mdiForm.getCoeusMenuBar().validate();
        
        
    }
    
    
}
