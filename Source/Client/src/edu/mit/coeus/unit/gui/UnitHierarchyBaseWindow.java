/*
 * @(#)UnitHierarchyBaseWindow.java  October 3, 2002, 12:06 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.unit.gui;

import edu.mit.coeus.bean.AuthorizationBean;
import edu.mit.coeus.rates.controller.CERatesController;
import edu.mit.coeus.unit.bean.UnitFormulatedCostBean;
import edu.mit.coeus.unit.controller.UnitFormulatedCostController;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.AuthorizationOperator;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.beans.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusImageIcon;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
//import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.unit.bean.UnitHierarchyFormBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.tree.HierarchyNode;
import edu.mit.coeus.utils.tree.DnDJTree;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.mapsrules.controller.MapMaintenanceBaseWindowController;
import edu.mit.coeus.mapsrules.controller.RuleBaseWindowController;
import edu.mit.coeus.mapsrules.gui.MapMaintenanceBaseWindow;
import edu.mit.coeus.mapsrules.gui.RuleBaseWindow;
import edu.mit.coeus.user.gui.UserMaintenanceBaseWindow;
import edu.mit.coeus.rates.controller.RatesBaseWindowController;
import edu.mit.coeus.rates.bean.InstituteRatesBean;
//import edu.mit.coeus.unit.bean.UnitAdministratorBean;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;

import edu.umdnj.coeus.gui.menu.CoeusReportMenu;
//import java.util.Collections;

/**
 *  The class used to build all the listener tasks, which happens at the
 *  <code> Unit Hierarchy Internal Frame</code>.
 *  The class will be instanciated when the user invokes unit hierarchy
 *  from the menu or tool bar.
 *  All the listeners pertained to unit hierarchy base window will be implemented
 *  in this class.
 *
 * Created on October 3, 2002, 12:06 PM
 * @author  Geo Thomas
 * @version 1.0
 */

public class UnitHierarchyBaseWindow extends CoeusInternalFrame
        implements ActionListener,TreeSelectionListener{
            
            //Added by chandra
        private static final String MAINTAIN_HIERARCHY = "MAINTAIN_HIERARCHY";
        private static final String USER_HAS_OSP_RIGHT = "FN_USER_HAS_OSP_RIGHT";
        private static final String FUNCTION_SERVLET = "/coeusFunctionsServlet";
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ FUNCTION_SERVLET;
        // End Chandra

    // reference of CoeusAppletMDIForm
    private CoeusAppletMDIForm mdiForm;
    //holds UnitDetailForm
    private UnitHierarchyForm unitHierarchyForm;
    //holds the intance of unit hierarchy tree component
    private DnDJTree unitHierarchyTree;
    //holds selected hiearcrhy node, parent node of the selected node
    private UnitHierarchyFormBean selectedHierarchyNode,parentNode;
    //menu items
    private CoeusMenuItem addUnit,modifyUnit,displayUnit,moveUnit,unitSearch;
    
    //Added by Vyjayanthi to display Rates menu item
    private CoeusMenuItem unitRates;
     
    // menu items for the Action menu - Added by chandra
    private CoeusMenuItem userMaintainance, mapMaintainance,rulesMaintainance,
                          lARatemaintainance, userDelegation, mnuFormualtedCost;
    // End chandra

    private CoeusReportMenu coeusReportMenu;
    
    //toolbar buttons
    private CoeusToolBarButton tlbrAddUnit,tlbrModifyUnit,tlbrDisplayUnit,
                                tlbrMoveNode,tlbrSearchUnit,tlbrSaveUnit,
                                tlbrClose,tlbrUserMaint,tlbrMapMaint,
                                tlbrBusRuleMaint,tlbrLARatesMaint;

    //connection url to connect to the servlet
    private final String UNIT_CONNECTION_URL =
                                CoeusGuiConstants.CONNECTION_URL+"/unitServlet";
    private String parentUnitNumber,parentUnitName,unitNumber;


    private java.lang.String unitName;
    //holds the functionaity type
    private char functionalityType;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    //Added for unit hierarchy enhancement start 1
    private CoeusVector cvAdminType =null;
    //Added for unit hierarchy enhancement end 1
    
    //Case Id #3121 Automatic Tution Calculation Enhancement - Start
    private CoeusMenuItem validCERates;
    //Case Id #3121 - End
    // 3587: Multi Campus Enhancement
    private static final String FN_USER_HAS_RIGHT_IN_ANY_UNIT = "FN_USER_HAS_RIGHT_IN_ANY_UNIT";
    /** Creates new UnitHierarchyBaseWindow 
     * @param mdiForm CoeusAppletMDIForm
     */
    public UnitHierarchyBaseWindow(CoeusAppletMDIForm mdiForm) {
        super(CoeusGuiConstants.TITLE_UNIT_HIERARCHY,mdiForm);
        this.mdiForm = mdiForm;
        coeusReportMenu = new CoeusReportMenu(mdiForm,this);

        attachTreeForm();
        
        mdiForm.getDeskTopPane().add(this);
        ImageIcon imgIconCoeus  =  new ImageIcon(getClass().
                                   getClassLoader().getResource("images/coeus16.gif"));
        super.setFrameIcon( imgIconCoeus );
        coeusMessageResources = CoeusMessageResources.getInstance();
        setVisible(true);

    }
    /*
     *  The method used to create the unithierarchyform instance, get the tree
     *  from the form instance and attach this instance with the base window
     */
    private void attachTreeForm(){
        this.setFrameMenu(getUnitEditMenu());
        this.setActionsMenu(getActionMenu());
        final JToolBar unitHierarchyToolBar = getUnitToolBar();
        this.setFrameToolBar(unitHierarchyToolBar);
        this.setToolsMenu(getUnitToolsMenu());
        
        //Check whether the User has rights to Maintain hierarchy- Added by chandra
        boolean hasOrgRight = isUserHasRight();
        tlbrAddUnit.setEnabled(hasOrgRight);
        tlbrModifyUnit.setEnabled(hasOrgRight);
        tlbrMoveNode.setEnabled(hasOrgRight);
        tlbrSaveUnit.setEnabled(hasOrgRight);
        
        addUnit.setEnabled(hasOrgRight);
        modifyUnit.setEnabled(hasOrgRight);
        moveUnit.setEnabled(hasOrgRight);
        maintainSaveMenu(hasOrgRight);
        // End Chandra
       
        
        try{
            /* JM 7-23-2015 pass in user to hierarchy instantiation */
        	// unitHierarchyForm = new UnitHierarchyForm("000001");
            unitHierarchyForm = new UnitHierarchyForm("000001",mdiForm.getUserId());
            /* JM END */
        	this.unitHierarchyTree = unitHierarchyForm.getUnitHierarchyTree();
            unitHierarchyTree.addTreeSelectionListener( this );
            unitHierarchyTree.addMouseListener(ml);
            unitHierarchyTree.setSelectionInterval(0,0);  //Updated for default Root Node Selection. 
            
            //Added by Chandra
            if (!hasOrgRight) {
                unitHierarchyTree.getDropTarget().setActive(false);
            }
    
            this.getContentPane().add(unitHierarchyForm);
            //add listener to handle the close event of the internal frame.
            this.addVetoableChangeListener(new VetoableChangeListener(){
                public void vetoableChange(PropertyChangeEvent pce)
                throws PropertyVetoException {
                    if (pce.getPropertyName().equals(
                            JInternalFrame.IS_CLOSED_PROPERTY)) {
                        boolean changed = (
                            (Boolean) pce.getNewValue()).booleanValue();

                        String msg = coeusMessageResources.parseMessageKey(
                                                        "saveConfirmCode.1002");
                        if (changed && (
                                !unitHierarchyTree.getModifiedNodes().isEmpty()
                                    ||!unitHierarchyTree.getNewNodes().isEmpty())) {
                            int confirm = CoeusOptionPane.showQuestionDialog(msg,
                                            CoeusOptionPane.OPTION_YES_NO_CANCEL,
                                            CoeusOptionPane.DEFAULT_YES);
                            switch(confirm){
                                case(JOptionPane.YES_OPTION):
                                    try{
                                        addUpdateUnitHierarchy();
                                    }catch(Exception ex){
                                        ex.printStackTrace();
                                        String exMsg = ex.getMessage();
                                        CoeusOptionPane.showWarningDialog(exMsg);
                                    }
                                    break;
                                case(JOptionPane.NO_OPTION):
                                    break;
                                case(JOptionPane.CANCEL_OPTION):
                                case(JOptionPane.CLOSED_OPTION):
                                    throw new PropertyVetoException(
                                        coeusMessageResources.parseMessageKey(
                                            "protoDetFrm_exceptionCode.1130"),
                                        null);
                            }
                        }
                    }
                }
            });
            this.addInternalFrameListener(new InternalFrameAdapter(){
                public void internalFrameClosed(InternalFrameEvent e){
                    mdiForm.removeToolBar(unitHierarchyToolBar);
                    mdiForm.removeFrame(CoeusGuiConstants.TITLE_UNIT_HIERARCHY);
                    // Added by chandra
                    maintainSaveMenu(true);
                }
            });
            mdiForm.putFrame(CoeusGuiConstants.TITLE_UNIT_HIERARCHY,this);

        }catch(Exception ex){
            ex.printStackTrace();
            String exMsg = ex.getMessage();
            CoeusOptionPane.showWarningDialog(exMsg);
        }
    }
    /**
     *  The method used to set the unit hierarchy form with the base window
     *  @param unitHierarchyForm Unit Hierarchy Form, a component which provides APIs to manipulate
     *  unit hierarchy tree
     */
    public void setUnitHierarchyForm(UnitHierarchyForm unitHierarchyForm) {
        this.unitHierarchyForm = unitHierarchyForm;
    }

    /**
     * Constructs unit hierarchy edit menu. This menu will get attached as
     * an edit menu with the main menu bar.
     * @return edit menu items of unit hierarchy
     */
    public CoeusMenu getUnitEditMenu(){
        CoeusMenu coeusMenu=null;
        java.util.Vector fileChildren = new java.util.Vector();
        addUnit = new CoeusMenuItem("Add",null,true,true);
        addUnit.addActionListener(this);
        addUnit.setMnemonic('A');
        modifyUnit  = new CoeusMenuItem("Modify",null,true,true);
        modifyUnit.setMnemonic('M');
        modifyUnit.addActionListener(this);
        displayUnit = new CoeusMenuItem("Display",null,true,true);
        displayUnit.setMnemonic('i');
        displayUnit.addActionListener(this);
        moveUnit = new CoeusMenuItem("Move",null,true,true);
        moveUnit.setMnemonic('v');
        moveUnit.addActionListener(this);

        //Added by Vyjayanthi - Start
        //To display Rates menu item
//        unitRates = new CoeusMenuItem("Rates",null,true,true);
//        unitRates.setMnemonic('R');
//        unitRates.addActionListener(this);
        //Added by Vyjayanthi - End
        
        fileChildren.add(addUnit);
        fileChildren.add(modifyUnit);
        fileChildren.add(displayUnit);
        fileChildren.add(moveUnit);
//        fileChildren.add(unitRates);    //Added by Vyjayanthi

        coeusMenu = new CoeusMenu("Edit",null,fileChildren,true,true);
        coeusMenu.setMnemonic('E');
        return coeusMenu;
    }
    
    
    
    /**
     * Constructs unit hierarchy Action menu. This menu will get attached as
     * an Action menu with the main menu bar.
     * @return Action menu items of unit hierarchy - Added by chandra
     */
    public CoeusMenu getActionMenu(){
        CoeusMenu coeusMenu=null;
        java.util.Vector fileChildren = new java.util.Vector();
        
        //Bug #2258 ---Starts here
        // Changed the spelling of 'Maintenance'
        userMaintainance = new CoeusMenuItem("User Maintenance...",null,true,true);
        userMaintainance.addActionListener(this);
        userMaintainance.setMnemonic('U');
        
        mapMaintainance  = new CoeusMenuItem("Map Maintenance...",null,true,true);
        mapMaintainance.setMnemonic('a');
        mapMaintainance.addActionListener(this);
        
        rulesMaintainance = new CoeusMenuItem("Rules Maintenance...",null,true,true);
        rulesMaintainance.setMnemonic('R');
        rulesMaintainance.addActionListener(this);
        
        lARatemaintainance = new CoeusMenuItem("LA Rates Maintenance...",null,true,true);
        lARatemaintainance.setMnemonic('L');
        lARatemaintainance.addActionListener(this);

        //Bug #2258 ----Ends here
        
        userDelegation = new CoeusMenuItem("User Delegations...",null,true,true);
        userDelegation.setMnemonic('U');
        userDelegation.addActionListener(this);
        
        //added by nadh
        //Start
        //To display Rates menu item
        unitRates = new CoeusMenuItem("Rates Maintenance",null,true,true);
        unitRates.setMnemonic('R');
        unitRates.addActionListener(this);
        //end nadh
        
        //Case ID #3121 Automatic Tution Calculation Enhancement - Start
        validCERates = new CoeusMenuItem("Valid CE Rates Maintenance", null, true, true);
        validCERates.setMnemonic('C');
        validCERates.addActionListener(this);
        //Case ID #3121 - End
        
        // Added for COEUSQA-1725 : Extend the functionality of Lab Allocation in proposal development budgeting. - Start
        mnuFormualtedCost = new CoeusMenuItem("Formulated Cost Maintenance", null, true, true);
        mnuFormualtedCost.setMnemonic('F');
        mnuFormualtedCost.addActionListener(this);
        // Added for COEUSQA-1725 : Extend the functionality of Lab Allocation in proposal development budgeting. - End
        
        fileChildren.add(userMaintainance);
        fileChildren.add(mapMaintainance);
        fileChildren.add(rulesMaintainance);
        fileChildren.add(lARatemaintainance);
        fileChildren.add(userDelegation);
        fileChildren.add(unitRates);//added by nadh 
        fileChildren.add(validCERates);//Added for Case Id#3121 - Tuition Fee Calculation
        // Added for COEUSQA-1725 : Extend the functionality of Lab Allocation in proposal development budgeting. - Start
        fileChildren.add(mnuFormualtedCost);
        // Added for COEUSQA-1725 : Extend the functionality of Lab Allocation in proposal development budgeting. - End

        fileChildren.addAll(coeusReportMenu.mainChild);
        coeusMenu = new CoeusMenu("Action",null,fileChildren,true,true);
        coeusMenu.setMnemonic('A');

        //coeusMenu.add(coeusReportMenu);

        return coeusMenu;
    }
    // Menu items for Action Menu - End
    
    
    /**
     *  Constructs the unit hierararchy tool menu. This menu will get attached
     *  as tool menu in the main menu bar
     *  @return Unit hierarchy tool menu
     */
    public CoeusMenu getUnitToolsMenu(){
        CoeusMenu coeusMenu;
        java.util.Vector children = new java.util.Vector();
        java.util.Vector fileChildren = new java.util.Vector();
        unitSearch = new CoeusMenuItem("Search",null,true,true);
        unitSearch.setMnemonic('S');
        unitSearch.addActionListener(this);
        children.add(unitSearch);
        coeusMenu = new CoeusMenu("Tools",null,children,true,true);
        coeusMenu.setMnemonic('T');
        return coeusMenu;
    }

    /** Constructs the tool bar for the unit hierarchy base window. This toolbar
     * will get attached with the default toolbar when this base window got
     * selected.
     *
     * @return JToolBar unit hierarchy toolbar
     */
    public JToolBar getUnitToolBar(){
        JToolBar toolbar = new JToolBar();
        tlbrAddUnit = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource("images/add.gif")),null,
                "Add a new unit");
        tlbrModifyUnit = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource("images/edit.gif")),null,
                "Modify a unit");
        tlbrDisplayUnit = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource("images/display.gif")),null,
                "Display unit details");
        tlbrMoveNode = new CoeusToolBarButton( new ImageIcon(
                getClass().getClassLoader().getResource("images/move.gif") ),
                null, "Move a unit");
        tlbrSearchUnit = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource("images/find.gif")),null,
                "Search");
        tlbrUserMaint = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource("images/usermaint.gif")),null,
                "User Maintenance for selected unit");
        tlbrMapMaint = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource("images/mapmaint.gif")),null,
                "Map Maintenance for selected unit");
        tlbrBusRuleMaint = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource("images/busrulemaint.gif")),null,
                "Business rules Maintenance for selected unit");
        tlbrLARatesMaint = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource("images/laratesmaint.gif")),null,
                "LA Rates Maintenance for selected unit");
        tlbrSaveUnit = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource("images/save.gif")),null,
                "Save");
        tlbrClose = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource("images/close.gif")),null,
                "Close");

        tlbrSaveUnit.addActionListener(this);
        tlbrSearchUnit.addActionListener(this);
        tlbrAddUnit.addActionListener(this);
        tlbrModifyUnit.addActionListener(this);
        tlbrDisplayUnit.addActionListener(this);
        tlbrMoveNode.addActionListener(this);
        tlbrClose.addActionListener(this);
        tlbrMapMaint.addActionListener(this);
        tlbrUserMaint.addActionListener( this );
        tlbrLARatesMaint.addActionListener(this);
        tlbrBusRuleMaint.addActionListener(this);
        
        toolbar.add(tlbrAddUnit);
        toolbar.add(tlbrModifyUnit);
        toolbar.add(tlbrDisplayUnit);
        toolbar.add(tlbrMoveNode);
        toolbar.add(tlbrSearchUnit);
        toolbar.addSeparator();
        toolbar.add(tlbrUserMaint);
        toolbar.add(tlbrMapMaint);
        toolbar.add(tlbrBusRuleMaint);
        toolbar.add(tlbrLARatesMaint);
        toolbar.addSeparator();
        toolbar.add(tlbrSaveUnit);
        toolbar.addSeparator();
        toolbar.add(tlbrClose);
        toolbar.setFloatable(false);
        return toolbar;
    }

    /*
     *  Method to update the unit hierarchy. It will collect the newly added nodes
     *  and modified nodes from the unit hierarchy node instance, and send to
     *  the servlet as hashtable.
     */
    private void addUpdateUnitHierarchy() throws Exception{
        Hashtable addUpdateNodes = new Hashtable();
        addUpdateNodes.put("NEW_NODES",unitHierarchyTree.getNewNodes());
        addUpdateNodes.put("UPDATE_NODES",unitHierarchyTree.getModifiedNodes());
        if(unitHierarchyTree.getNewNodes().isEmpty() &&
                unitHierarchyTree.getModifiedNodes().isEmpty()){
            return;
        }

        RequesterBean request = new RequesterBean();
        request.setFunctionType('U');
        request.setDataObject(addUpdateNodes);
        //Added for unit hierarchy enhancement start by tarique 2
        request.setDataObjects(cvAdminType);
        //Added for unit hierarchy enhancement end 2
        AppletServletCommunicator comm = new AppletServletCommunicator(
            UNIT_CONNECTION_URL, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(!response.isSuccessfulResponse()){
            throw new Exception(response.getMessage());
        }else{
            /** begin: fixed bug with id #147  */
            /*   set the message to be displayed in status bar. RefID:#147 */
            /*setStatusMessage(coeusMessageResources.parseMessageKey(
                "general_saveCode.2275"));*/
            /** end: fixed bug with id #147  */
            unitHierarchyTree.clearAllModifiedNodes();
            unitHierarchyTree.clearAllNewNodes();
            unitHierarchyForm.refreshTree();
            unitHierarchyTree = unitHierarchyForm.getUnitHierarchyTree();
            unitHierarchyTree.addTreeSelectionListener( this );
            unitHierarchyTree.addMouseListener(ml);
            //Added for bug fixed for Case #2069 start
            TreePath path = findByName(unitHierarchyTree, selectedHierarchyNode.getNodeID());
            unitHierarchyTree.expandPath(path);
            unitHierarchyTree.setSelectionPath(path);
            unitHierarchyTree.scrollRowToVisible(
            unitHierarchyTree.getRowForPath(path));
            unitHierarchyTree.fireTreeExpanded(path);
            //Added for bug fixed for Case #2069 end
            //selectedHierarchyNode = null; 
        }
        addUpdateNodes.remove("NEW_NODES");
        addUpdateNodes.remove("UPDATE_NODES");
        cvAdminType = null;
        moveFlag = false;
    }

    /* displays the message,it gives the error message.
     * @param mesg String
     */
    private void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }
    /**
     *  Overridden method for the ActionListener. All menu/toolbar actions
     *  implement in this method. If any validation error occures, it will
     *  catch it and display that as an option dialog.
     *  @param event Action event
     */
    public void actionPerformed(ActionEvent event){
        Object eventSource = event.getSource();
        boolean dlgFlag = false;
        final String leadUnitSearchString = "leadunitsearch";
        try{
            if ( (eventSource.equals(addUnit)) || (eventSource.equals(tlbrAddUnit)) ) {
                dlgFlag = true;
                //Added with Case 3587:Multicampus enhancement
//                if(selectedHierarchyNode==null){
//                    throw new Exception(coeusMessageResources.parseMessageKey(
//                            "unitHrchyBaseWin_exceptionCode.1117"));
                if(!checkUserCanMaintainHierarchy(getSelectedUnitNumber())){
                    throw new Exception(coeusMessageResources.parseMessageKey(
                            "unitHrchyBaseWin_exceptionCode.1119"));
                }
                //3587 End
                functionalityType = 'I';
            } else if ( (eventSource.equals(modifyUnit)) || (eventSource.equals(tlbrModifyUnit)) ) {
                dlgFlag = true;
                //Added with Case 3587:Multicampus enhancement
//                if(selectedHierarchyNode==null){
//                    throw new Exception(coeusMessageResources.parseMessageKey(
//                            "unitHrchyBaseWin_exceptionCode.1117"));
                if(!checkUserCanMaintainHierarchy(getSelectedUnitNumber())){
                    throw new Exception(coeusMessageResources.parseMessageKey(
                            "unitHrchyBaseWin_exceptionCode.1119"));
                }
                //3587 End
                functionalityType = 'U';
            } else if ( (eventSource.equals(displayUnit)) || (eventSource.equals(tlbrDisplayUnit)) ) {
                dlgFlag = true;
                if(selectedHierarchyNode==null)
                    throw new Exception(coeusMessageResources.parseMessageKey(
                                        "unitHrchyBaseWin_exceptionCode.1117"));
                functionalityType = 'G';
            } else if ( (eventSource.equals(moveUnit)) || (eventSource.equals(tlbrMoveNode)) ) {
                //Added with Case 3587:Multicampus enhancement
//                if(selectedHierarchyNode==null){
//                    throw new Exception(coeusMessageResources.parseMessageKey(
//                            "unitHrchyBaseWin_exceptionCode.1117"));
                if(!checkUserCanMaintainHierarchy(getSelectedUnitNumber())){
                    throw new Exception(coeusMessageResources.parseMessageKey(
                            "unitHrchyBaseWin_exceptionCode.1119"));
                }
                //3587 End
                //inform mouse listener to start the move option
                moveFlag = true;
                //change the cursor
                unitHierarchyTree.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            //for search
            if(eventSource.equals(tlbrSearchUnit) || eventSource.equals(unitSearch)){
                //open search window with two tabs
                CoeusSearch coeusSearch = new CoeusSearch(mdiForm,
                        leadUnitSearchString, CoeusSearch.TWO_TABS);
                coeusSearch.showSearchWindow();
                String unitCode = coeusSearch.getSelectedValue()+" : ";
                //return if result is empty
                if(unitCode==null)
                    return;
                //append the unit number and unit name for the search in the tree
                int startRow = 0;
                unitHierarchyTree.expandPath(new TreePath(
                        unitHierarchyTree.getModel().getRoot()));
                // Find the path (regardless of visibility) that matches the
                // specified sequence of names
                TreePath path = findByName(unitHierarchyTree, unitCode);

                unitHierarchyTree.expandPath(path);
                unitHierarchyTree.setSelectionPath(path);
                unitHierarchyTree.scrollRowToVisible(
                    unitHierarchyTree.getRowForPath(path));
            }else if(eventSource.equals(tlbrSaveUnit)){
                addUpdateUnitHierarchy();
            }else if(eventSource.equals(tlbrClose)){
                CoeusInternalFrame unitFrame =
                mdiForm.getFrame(CoeusGuiConstants.TITLE_UNIT_HIERARCHY);
                if(unitFrame!=null){
                    unitFrame.doDefaultCloseAction();
                }
            }else if ( eventSource.equals( tlbrUserMaint ) || eventSource.equals(userMaintainance)) {
                showUserMaintenance();
            }else if(eventSource.equals(tlbrLARatesMaint) || eventSource.equals(lARatemaintainance)){
                showLARateMaintainance();
            }else if(eventSource.equals(userDelegation)){
                showDelegationForm();
            }else if( eventSource.equals(unitRates) ){
                showUnitRates();    //Added by Vyjayanthi
            }else if( eventSource.equals(mapMaintainance) || eventSource.equals(tlbrMapMaint)){
                showMapMaintenance(); // Added by Shiji to display the Map Maintainance window    
            }else if(eventSource.equals(tlbrBusRuleMaint) || eventSource.equals(rulesMaintainance)){
                showBusinessRules();
            }else if(eventSource.equals(validCERates)) {
                showCERates(); // Added for Case #3121
            }else if(eventSource.equals(mnuFormualtedCost)) {
                showFormulatedCost();
            }
            coeusReportMenu.actionPerformed(event);
            if(dlgFlag){
                UnitDetailFormBean unitDetail = new UnitDetailFormBean();
                if(selectedHierarchyNode!=null && parentNode!=null){
                    //set the parent name by getting the substring from the
                    //display part of the parent node
                    String parentText = selectedNode.getParent().toString().trim();
                    if(parentText!=null && !parentText.equals("")){
                        String parentNodeId = parentText.substring(0,
                            (parentText.indexOf(':')-1)).trim();
                        String parentNodeName = parentText.substring(
                            (parentText.indexOf(':')+1),
                            parentText.length()).trim();
                        selectedHierarchyNode.setParentNodeID(parentNodeId);
                        selectedHierarchyNode.setParentNodeName(parentNodeName);
                    }else{
                        selectedHierarchyNode.setParentNodeName("");
                    }
                }
                //create unit detail form
                UnitDetailForm frmUnitDetail = new UnitDetailForm(
                    selectedHierarchyNode,functionalityType);
                frmUnitDetail.setUnitHierarchyTree(unitHierarchyTree);
                //show the unit detail dialog window
                frmUnitDetail.showUnitForm(mdiForm);

                //get the unit hierarchy from the form instance
                UnitHierarchyFormBean unitHierarchyBean =
                                        frmUnitDetail.getUnitHierarchyBean();
                //Added for unit hierarchy enhancement start 3 by tarique
                //cvAdminType = frmUnitDetail.getAdminDataObjects();
                if(unitHierarchyBean!=null){
                    if(unitHierarchyBean.getCvAdminType()!=null
                            &&unitHierarchyBean.getCvAdminType().size()>0){
                        if(cvAdminType!=null&&cvAdminType.size()>0){
//                            //cvAdminType.add(unitHierarchyBean.getCvAdminType());
                            if(frmUnitDetail.unitAdminTypeFlag){
                                //COEUSDEV-170 : Array index out of range error - Unit Administrators  - Start
                                //Deletes the INSERT_RECORD bean and bean data from DB and add's the current admin data from UnitHierarachyFormBean
                                Equals insertActype = new Equals("acType",TypeConstants.INSERT_RECORD);
                                Equals unitNumber = new Equals("unitNumber", selectedHierarchyNode.getUnitDetail().getUnitNumber());
                                And awAdmin = new And(insertActype , unitNumber);
                                CoeusVector cvFiltered = new CoeusVector();
                                cvFiltered = (CoeusVector)cvAdminType.filter(awAdmin);
                                if(cvFiltered != null && cvFiltered.size() > 0){
                                    cvAdminType.removeAll(cvFiltered);
                                }
                                Equals emptyActype = new Equals("acType",null);
                                awAdmin = new And(emptyActype , unitNumber);
                                cvFiltered = (CoeusVector)cvAdminType.filter(awAdmin);
                                if(cvFiltered != null && cvFiltered.size() > 0){
                                     cvAdminType.removeAll(cvFiltered);
                                }
                                Equals deleteActype = new Equals("acType",TypeConstants.DELETE_RECORD);
                                awAdmin = new And(deleteActype , unitNumber);
                                CoeusVector cvFilteredMain = (CoeusVector)cvAdminType.filter(awAdmin);
                                cvFiltered = (CoeusVector)unitHierarchyBean.getCvAdminType().filter(awAdmin);
                                //Checks for duplication of the DELETE_RECORD bean and remove it from cvAdminType
                                if(cvFiltered != null && cvFiltered.size() > 0){
                                    cvAdminType.removeAll(cvFiltered);
                                } 
                                cvAdminType.addAll(unitHierarchyBean.getCvAdminType());
                                //COEUSDEV-170 : END
                                
                            }
                        }else{
                            cvAdminType = new CoeusVector();
                            cvAdminType = unitHierarchyBean.getCvAdminType();
                        }
                    }
                    //COEUSDEV-170 : Array index out of range error - Unit Administrators  - Start
                    else{
                        //Delete's the INSERT_RECORD bean when no administrator is available in administrator form table
                        if(frmUnitDetail.unitAdminTypeFlag){
                            Equals insertActype = new Equals("acType","I");
                            Equals unitNumber = new Equals("unitNumber", selectedHierarchyNode.getUnitDetail().getUnitNumber());
                            And awAdmin = new And(insertActype , unitNumber);
                            if(cvAdminType != null && cvAdminType.size() > 0){
                                CoeusVector cvFiltered = (CoeusVector)cvAdminType.filter(awAdmin);
                                cvAdminType.removeAll(cvFiltered);
                            }
                        }
                    }
                    //COEUSDEV-170 : END
                    if(cvAdminType!=null&&cvAdminType.size()>0){
                       unitHierarchyBean.setCvAdminType(cvAdminType);
                    }
                }
                
                //Added for unit hierarchy enhancement end 3 by tarique
                DefaultTreeModel model = (DefaultTreeModel)unitHierarchyTree.getModel();
                if(unitHierarchyBean!=null){
                    switch(functionalityType){
                        case('I'):
                            HierarchyNode childNode = new HierarchyNode(
                                unitHierarchyBean);
                            UnitHierarchyFormBean parentHierarchyBean =
                                (UnitHierarchyFormBean)selectedNode.getUserObject();
                            parentHierarchyBean.setChildrenFlag("Y");
                            selectedNode.setUserObject(parentHierarchyBean);
                            model.insertNodeInto(childNode, selectedNode,
                                            selectedNode.getChildCount());
                            unitHierarchyTree.addNewNode((UnitHierarchyFormBean)
                                    childNode.getUserObject());
                            unitHierarchyTree.addModifiedNode(parentHierarchyBean);
                            break;
                        case('U'):
                            selectedNode.setUserObject(unitHierarchyBean);
                            unitHierarchyTree.setSelectionRow(0);
                            unitHierarchyTree.addModifiedNode(unitHierarchyBean);
                            break;
                    }
                    //Tree Node Label update. Subramanya
                    DefaultTreeModel unitTreeModel = ( DefaultTreeModel )
                                                unitHierarchyTree.getModel();
                    unitTreeModel.reload();
                    TreePath modifiedPath = unitHierarchyTree.findByName(
                            unitHierarchyBean.toString());

                    unitHierarchyTree.expandPath( modifiedPath );
                    unitHierarchyTree.setSelectionPath(modifiedPath);

                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            String exMsg = ex.getMessage();
            CoeusOptionPane.showWarningDialog(exMsg);
        }
    }
    
    
    // To show the Business Rules
     private void showBusinessRules() throws Exception{
         String unitNumber = selectedHierarchyNode.getNodeID();
         if(unitNumber== null){
             unitNumber = mdiForm.getUnitNumber();
         }
         String title = CoeusGuiConstants.BUSINESS_RULE_FRAME_TITLE;
         RuleBaseWindow ruleBaseWindow = null;
         if( ( ruleBaseWindow = (RuleBaseWindow)mdiForm.getFrame(
         title))!= null ){
             if( ruleBaseWindow.isIcon() ){
                 ruleBaseWindow.setIcon(false);
             }
             ruleBaseWindow.setSelected( true );
             return;
         }
         RuleBaseWindowController ruleBaseWindowController = new RuleBaseWindowController(title,mdiForm,unitNumber);
         ruleBaseWindowController.display();
     }
    
    
    //To show the Map Maintainance Base Window
    private void showMapMaintenance() {
        MapMaintenanceBaseWindow mapMaintenanceBaseWindow = null;
        String unitNumber=null;
        String selUnitNumber=null;
        String selUnitName = null;
        if(selectedHierarchyNode==null) {
            unitNumber= mdiForm.getUnitNumber();
        }else {
            selUnitNumber = selectedHierarchyNode.getNodeID();
            selUnitName = selectedHierarchyNode.getNodeName();
            unitNumber = selUnitNumber +" : " +selUnitName;
        }
        try{
            //System.out.println("opend frames:"+mdiForm.get);
            mapMaintenanceBaseWindow = (MapMaintenanceBaseWindow)mdiForm.getFrame(
            CoeusGuiConstants.MAPS_BASE_FRAME_TITLE +" "+ unitNumber);
            
            if( mapMaintenanceBaseWindow!= null) {
                
                if(mapMaintenanceBaseWindow.isIcon()) mapMaintenanceBaseWindow.setIcon(false);
                mapMaintenanceBaseWindow.setSelected(true);
                //userMaintBaseWindow.showUserDetails();
                return ;
            }
            MapMaintenanceBaseWindowController mapMaintenanceBaseWindowController = new MapMaintenanceBaseWindowController(unitNumber,true);
            mapMaintenanceBaseWindowController.display();
            
            
            //userMaintBaseWindow.showUserDetails();
        }catch (Exception exception) {
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    
    //Added by Vyjayanthi
    /** Displays the Rates screen pertaining to the selected unit
     */
    private void showUnitRates() throws Exception {
        if(selectedHierarchyNode==null)
            throw new Exception(coeusMessageResources.parseMessageKey(
                                "unitHrchyBaseWin_exceptionCode.1117"));
        String selUnitNumber = selectedHierarchyNode.getNodeID();
        
        InstituteRatesBean instituteRatesBean = new InstituteRatesBean();
        instituteRatesBean.setUnitNumber(selUnitNumber);
        RatesBaseWindowController ratesBaseWindowController = new RatesBaseWindowController(instituteRatesBean);
        ratesBaseWindowController.display();
    }
    
    /** Departmental Delegation Form - Added by chandra
     */
    private void showDelegationForm() throws Exception{
        DepartmentalDelegationsForm departmentalDelegationsForm;
        if(selectedHierarchyNode==null)
            throw new Exception(coeusMessageResources.parseMessageKey(
                                "unitHrchyBaseWin_exceptionCode.1117"));
       String selUnitNumber = selectedHierarchyNode.getNodeID();  
       String selUnitName = selectedHierarchyNode.getNodeName();
       try{
        departmentalDelegationsForm = new DepartmentalDelegationsForm(mdiForm,selUnitNumber,selUnitName);
       }catch(Exception exception){
           exception.printStackTrace();
       }
    }//End of showDelegationForm.............
    
    /** LA Rate Maintainance details for the selected department
     *Pass Unit Number and Unit Name to the Constructor - Added by chandra
     */
    private void showLARateMaintainance() throws Exception{
        LARateMaintainanceForm lARateMaintainanceForm;
        if(selectedHierarchyNode==null)
            throw new Exception(coeusMessageResources.parseMessageKey(
                                "unitHrchyBaseWin_exceptionCode.1117"));
       String selUnitNumber = selectedHierarchyNode.getNodeID();  
       String selUnitName = selectedHierarchyNode.getNodeName();
       try{
        lARateMaintainanceForm = new LARateMaintainanceForm(mdiForm,selUnitNumber,selUnitName);
       }catch(Exception exception){
           exception.printStackTrace();
       }
    }//End of showLARateMaintainance.............End Chandra
    
    /**
     * This method will be invoked when uers toolbar button is clicked.
     */
    private void showUserMaintenance() throws Exception{
       UserMaintenanceBaseWindow userMaintBaseWindow = null;
        if(selectedHierarchyNode==null)
            throw new Exception(coeusMessageResources.parseMessageKey(
                                "unitHrchyBaseWin_exceptionCode.1117"));
       String selUnitNumber = selectedHierarchyNode.getNodeID();  
       String selUnitName = selectedHierarchyNode.getNodeName();
       try{
       //System.out.println("opend frames:"+mdiForm.get);    
         userMaintBaseWindow = (UserMaintenanceBaseWindow)mdiForm.getFrame(
            CoeusGuiConstants.USER_ROLE_MAINTENENCE_BASE_WINDOW_FRAME_TITLE);
           System.out.println("already exists:"+userMaintBaseWindow); 
       if( userMaintBaseWindow!= null) {

           if(userMaintBaseWindow.isIcon()) userMaintBaseWindow.setIcon(false);
            userMaintBaseWindow.setSelected(true);
            //userMaintBaseWindow.showUserDetails();
            return ;
       }
       userMaintBaseWindow = new UserMaintenanceBaseWindow(CoeusGuiConstants.USER_ROLE_MAINTENENCE_BASE_WINDOW_FRAME_TITLE,
                                    selUnitNumber,selUnitName );
       //userMaintBaseWindow.showUserDetails();
       }catch (Exception exception) {
            CoeusOptionPane.showInfoDialog(exception.getMessage());
       }
    }
    
    /*
     *  Update moved nodes
     */
    private void updateMoveNodes(HierarchyNode parentNode,
            HierarchyNode selectedNode,HierarchyNode targetNode){
        //if the parentnode is a leaf node, set the children flag as false("N")
        if(parentNode.isLeaf()){
            UnitHierarchyFormBean parentNodeBean = (UnitHierarchyFormBean)
                                        parentNode.getUserObject();
            parentNodeBean.setChildrenFlag("N");
            unitHierarchyTree.addModifiedNode(parentNodeBean);
        }
        UnitHierarchyFormBean targetNodeBean = (UnitHierarchyFormBean)
                                        targetNode.getUserObject();
        UnitHierarchyFormBean selectedNodeBean = (UnitHierarchyFormBean)
                                        selectedNode.getUserObject();
        targetNodeBean.setChildrenFlag("Y");
        selectedNodeBean.setParentNodeID(targetNodeBean.getNodeID());
        //add modified nodes to the tree hashtable
        unitHierarchyTree.addModifiedNode(targetNodeBean);
        unitHierarchyTree.addModifiedNode(selectedNodeBean);
    }
    /**
     *  Mouse listener to handle the move option
     */
    MouseListener ml = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            if(moveFlag){
                
                HierarchyNode parentNode = (HierarchyNode)selectedNode.getParent();
                DefaultTreeModel model = (DefaultTreeModel)unitHierarchyTree.getModel();
                int index = model.getIndexOfChild(parentNode,selectedNode);
                try{
                    int selRow = unitHierarchyTree.getRowForLocation(
                        e.getX(), e.getY());
                    TreePath selPath = unitHierarchyTree.getPathForLocation(
                        e.getX(), e.getY());
                    if(selPath==null || selRow == -1){
                        return;
                    }
                    HierarchyNode dropNode =(HierarchyNode)selPath.getLastPathComponent();
                     
                    //Update by Subramanya: to handle Move Sepecific Node to its own Parent. 
                    UnitHierarchyFormBean dropNodeBean = (UnitHierarchyFormBean)
                                                            dropNode.getUserObject();
                    UnitHierarchyFormBean selectedNodeBean = (UnitHierarchyFormBean)
                                        selectedNode.getUserObject();
                    //Added with case 3587: Multicampus Enhancement
                    boolean hasRight = true;
                    if(dropNodeBean.getNodeID().equalsIgnoreCase(
                            selectedNodeBean.getParentNodeID())){
                        CoeusOptionPane.showWarningDialog(
                                coeusMessageResources.parseMessageKey(
                                "Tree_exceptionCode.1004"));
                    }else if(!checkUserCanMaintainHierarchy(dropNodeBean.getNodeID())){
                        hasRight = false;
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                                "unitHrchyBaseWin_exceptionCode.1119"));
                    }else if(!checkUserCanMaintainHierarchy(selectedNodeBean.getParentNodeID())){
                        hasRight = false;
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                                "unitHrchyBaseWin_exceptionCode.1119"));
                        
                    }
                    
                    if(hasRight){
                        model.removeNodeFromParent(selectedNode);
                        model.insertNodeInto(selectedNode, dropNode,
                                dropNode.getChildCount());
                        updateMoveNodes(parentNode,selectedNode,dropNode);
                    }
                    //3587 End
                }catch(Exception ex){
                    ex.printStackTrace();
                    model.insertNodeInto(selectedNode, parentNode,index);
                    String exMsg = coeusMessageResources.parseMessageKey(
                                        "unitHrchyBaseWin_exceptionCode.1118");
                    CoeusOptionPane.showWarningDialog(exMsg);
                }finally{
                    moveFlag = false;
                    unitHierarchyTree.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                
                TreePath movedPath = unitHierarchyTree.findByName(
                            selectedNode.getUserObject().toString());
                unitHierarchyTree.setSelectionPath(movedPath);
                unitHierarchyTree.revalidate();
            }
        }
    };

    private HierarchyNode selectedNode;
    boolean moveFlag;
    /**
     *  Overridden method for the <code>TreeselectionListener</code>
     * @param e TreeSelectionEvent
     */
    public void valueChanged(TreeSelectionEvent e) {
        TreePath selectedTreePath = e.getNewLeadSelectionPath();
        if (selectedTreePath == null) {
            return;
        }
        if(!moveFlag){
            selectedNode =(HierarchyNode)selectedTreePath.getLastPathComponent();
        }
        
        Object nodeInfo = selectedNode.getUserObject();
        Object nodeParent = selectedNode.getParent();
        if (nodeInfo!=null) {
            selectedHierarchyNode = (UnitHierarchyFormBean)nodeInfo;
        }
        if(nodeParent!=null){
            parentNode = (UnitHierarchyFormBean)nodeInfo;
        }
        
    }

    /** Finds the path in tree as specified by the array of names.
     * The names array is a sequence of names where names[0]
     * is the root and names[i] is a child of names[i-1].
     * Comparison is done using String.equals().
     * Returns null if not found.
     * @param tree JTree instance
     * @param name name to the searched/find
     * @return  TreePath found for the specific name
     */
    public TreePath findByName(DnDJTree tree, String name) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        TreePath result = find2(tree, new TreePath(root), name);
        return result;
    }

    private TreePath find2(DnDJTree tree, TreePath parent, String nodeName) {
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node != null && node.toString().trim().startsWith(nodeName)) {
            return parent;
        }else{

            Object o = node;
            if (node.getChildCount() >= 0) {
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                    TreeNode n = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(n);
                    TreePath result = find2(tree, path, nodeName);
                    if(result!=null)
                        return result;
                    // Found a match
                }
            }
        }
        // No match at this branch
        return null;
    }

    /**
     * This method called from Save Menu Item under File Menu.
     * Saves all the changes and refreshes the tree view.
     */
    public void saveActiveSheet() {
        try {
            addUpdateUnitHierarchy();
        } catch(Exception ex) {
            ex.printStackTrace();
            String exMsg = ex.getMessage();
            CoeusOptionPane.showWarningDialog(exMsg);
        }
    }
    
    public void saveAsActiveSheet() {
    }
    
    /* Added by chandra - To check wheter the user  have MMAINTAIN_HIERARCHY 
     *right or not. If yes show the toolbar buttons else disable the buttons. - 
     *Modified on 01/16/2003. Call server whether the user has right or not.
     */
     
    private boolean isUserHasRight() {
        boolean hasOSPRights = false;
        RequesterBean request = new RequesterBean();
        request.setId(MAINTAIN_HIERARCHY);
        // 3587: Multi Campus Enhancement - Start
//        request.setDataObject(USER_HAS_OSP_RIGHT);
        request.setDataObject(FN_USER_HAS_RIGHT_IN_ANY_UNIT);
        // 3587: Multi Campus Enhancement - End
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response.isSuccessfulResponse()) {
            if(response.getDataObject() != null){
                Boolean obj = (Boolean) response.getDataObject();
                hasOSPRights = obj.booleanValue();
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ;
                hasOSPRights = false ;
            }
        }
        
        return  hasOSPRights ;
        
    }
    
    //Added by Chandra Start
    private void maintainSaveMenu(boolean hasRight) {
        JMenu fileMenu = mdiForm.getFileMenu().getMenu();
        fileMenu.getItem(2).setEnabled(hasRight);
    }
    //Added by Chandra End
    
    //CaseId #3121 Automatic Tution Calculation - Start
    /**
     * Displays the ValidCERates form
     */
    private void showCERates() throws Exception {
        if(selectedHierarchyNode==null)
            throw new Exception(coeusMessageResources.parseMessageKey(
                                "unitHrchyBaseWin_exceptionCode.1117"));
        String selUnitNumber = selectedHierarchyNode.getNodeID();
        String selUnitName = selectedHierarchyNode.getNodeName();
        
        CERatesController ceRatesController = new CERatesController(selUnitNumber, selUnitName);
        ceRatesController.display();
    }
    //CaseUD #3121 - End
    
    // Added for COEUSQA-1725 : Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    /**
     * Method to open the formulated cost window
     * @throws java.lang.Exception 
     */
    private void showFormulatedCost() throws Exception{
        if(selectedHierarchyNode==null)
            throw new Exception(coeusMessageResources.parseMessageKey("unitHrchyBaseWin_exceptionCode.1117"));
        String selUnitNumber = selectedHierarchyNode.getNodeID();
        String selUnitName = selectedHierarchyNode.getNodeName();
        UnitFormulatedCostController formualtedCostController = new UnitFormulatedCostController(selUnitNumber,selUnitName,functionalityType);
        formualtedCostController.setFormData();
        formualtedCostController.display();
        
    }
    // Added for COEUSQA-1725 : Extend the functionality of Lab Allocation in proposal development budgeting. - End

    public String getSelectedUnitNumber() throws Exception {
        if(selectedHierarchyNode==null)
            throw new Exception(coeusMessageResources.parseMessageKey(
                                "unitHrchyBaseWin_exceptionCode.1117"));
        String selUnitNumber = selectedHierarchyNode.getNodeID();
        return selUnitNumber;
    }

    public String getSelectedUnitName() throws Exception {
        if(selectedHierarchyNode==null)
            throw new Exception(coeusMessageResources.parseMessageKey(
                                "unitHrchyBaseWin_exceptionCode.1117"));
        String selUnitName = selectedHierarchyNode.getNodeName();
        return selUnitName;
    }
    
    //Added with case 3587: Multicampus enhancement : start
     private boolean checkUserCanMaintainHierarchy(String unitNumber){
        boolean hierarchyRight = false;
        Hashtable authorizations = new Hashtable();
        AuthorizationBean authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(MAINTAIN_HIERARCHY);
        authorizationBean.setFunctionType("RIGHT");
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationBean.setQualifier(unitNumber);
        authorizationBean.setQualifierType("UNIT");
        authorizations.put(MAINTAIN_HIERARCHY, new AuthorizationOperator(authorizationBean));

        RequesterBean requester = new RequesterBean();        
        requester.setAuthorizationOperators(authorizations);
        requester.setIsAuthorizationRequired(true);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL +"/AuthorizationServlet", requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            authorizations = responder.getAuthorizationOperators();
            hierarchyRight = ((Boolean)authorizations.get(MAINTAIN_HIERARCHY)).booleanValue();
        }else{
            CoeusOptionPane.showInfoDialog(responder.getMessage());
        }
        return hierarchyRight;
    }
    // 3587: Multi Campus Enahncements - End
}
