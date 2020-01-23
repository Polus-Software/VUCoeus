/*
 * @(#)CoeusDepartmentalMenu.java 1.0 10/18/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui.menu;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusAppletMDIForm;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.departmental.gui.PersonBaseWindow;
import edu.mit.coeus.mapsrules.controller.MapMaintenanceBaseWindowController;
import edu.mit.coeus.mapsrules.controller.RuleBaseWindowController;
import edu.mit.coeus.mapsrules.gui.MapMaintenanceBaseWindow;
import edu.mit.coeus.mapsrules.gui.RuleBaseWindow;
import edu.mit.coeus.unit.gui.DepartmentalDelegationsForm;
import edu.mit.coeus.user.gui.UserMaintenanceBaseWindow;


/**
 * This class creates Departmental menu for the coeus application.
 *
 * @version :1.0 October 18, 2002, 3:11 PM
 * @author Guptha
 */

public class CoeusDepartmentalMenu extends JMenu implements ActionListener{

    /*
     * Departmental menu items
     */
    public CoeusMenuItem businessRules,maps, personnel, users, userDeligation;

    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";

    private CoeusMenu coeusMenu;
    
    private String unitNumber;

    private CoeusAppletMDIForm mdiForm;
       
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
   /** Default constructor which constructs the departmental menu for coeus application.
     * @param mdiForm  CoeusAppletMDIForm
     */
    public CoeusDepartmentalMenu(CoeusAppletMDIForm mdiForm){
        super();
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        createMenu();
    }

    /**
     * This method is used to get the maintain menu
     *
     * @return JMenu coeus maintain menu
     */
    public JMenu getMenu(){
        return coeusMenu;
    }

    /**
     * This method is used to create maintain menu for coeus application.
     */
    private void createMenu(){
        Vector fileChildren = new Vector();
        businessRules = new CoeusMenuItem("Business Rules...",null,true,true);
        businessRules.setMnemonic('B');
        maps = new CoeusMenuItem("Maps...",null,true,true);
        maps.setMnemonic('M');
        personnel = new CoeusMenuItem("Personnel...",null,true,true);
        personnel.setMnemonic('P');
        users = new CoeusMenuItem("Users...",null,true,true);
        users.setMnemonic('U');
        userDeligation = new CoeusMenuItem("User Delegations",null,true,true);
        userDeligation.setMnemonic('D');
        fileChildren.add(businessRules);
        fileChildren.add(maps);
        fileChildren.add(personnel);
        fileChildren.add(users);
        fileChildren.add(userDeligation);
        coeusMenu = new CoeusMenu("Departmental",null,fileChildren,true,true);
        coeusMenu.setMnemonic('D');

        //add listener
        businessRules.addActionListener(this);
        maps.addActionListener(this);
        personnel.addActionListener(this);
        users.addActionListener(this);
        userDeligation.addActionListener(this);

    }

    /** This method is used to handle the action event for the departmental menu items.
     * @param ae  ActionEvent
     */
    public void actionPerformed(ActionEvent ae){
        // Implemented For Person
        try{
            if (ae.getSource().equals(personnel)){
                showDepartmentPerson();
            }else if (ae.getSource().equals(users)){
                showUserMaintenance();
            }else if( ae.getSource().equals(userDeligation)){
                showDepartmentDelegations();
            }else if( ae.getSource().equals(maps)){
                showMaps();// Added by Shiji to display the Map Maintainance window
            }else if(ae.getSource().equals(businessRules)){
                showBusinessRules();
            }
            else{
                throw new Exception(coeusMessageResources.parseMessageKey(
                                                "funcNotImpl_exceptionCode.1100"));
            }
        }catch(Exception ex){
            ex.printStackTrace();
            CoeusOptionPane.showInfoDialog(ex.getMessage());
        }
    }
    
     private void showBusinessRules() throws Exception{
         String unitNumber = mdiForm.getUnitNumber();
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
    
    private void showDepartmentPerson() throws Exception{
        PersonBaseWindow personBaseWindow = null;
        if( ( personBaseWindow = (PersonBaseWindow)mdiForm.getFrame(
                CoeusGuiConstants.PERSON_BASE_FRAME_TITLE))!= null ){
              if( personBaseWindow.isIcon() ){
                personBaseWindow.setIcon(false);
              }
              personBaseWindow.setSelected( true );
              return;
        }
        personBaseWindow = new PersonBaseWindow(mdiForm);
        personBaseWindow.setVisible(true);
    }
    
    public void setUnitNumber(String unitNumb) {
        this.unitNumber = unitNumb;
    }
    
    //To show the Map Maintainance base window
    public void showMaps() throws Exception{
        MapMaintenanceBaseWindow mapMaintenanceBaseWindow = null;
         if(unitNumber == null){
            unitNumber=mdiForm.getUnitNumber();
        }
        if( ( mapMaintenanceBaseWindow = (MapMaintenanceBaseWindow)mdiForm.getFrame(
                CoeusGuiConstants.MAPS_BASE_FRAME_TITLE+" "+unitNumber))!= null ){
              if( mapMaintenanceBaseWindow.isIcon() ){
                mapMaintenanceBaseWindow.setIcon(false);
              }
              mapMaintenanceBaseWindow.setSelected( true );
              return;
        }
       
        MapMaintenanceBaseWindowController mapMaintenanceBaseWindowController = new MapMaintenanceBaseWindowController(unitNumber,false);
        mapMaintenanceBaseWindowController.display();
        
    }

    private void showUserMaintenance() throws Exception{
       UserMaintenanceBaseWindow userMaintBaseWindow = null;
       userMaintBaseWindow = (UserMaintenanceBaseWindow)mdiForm.getFrame(
            CoeusGuiConstants.USER_ROLE_MAINTENENCE_BASE_WINDOW_FRAME_TITLE);
       if( userMaintBaseWindow != null) {
            
            if(userMaintBaseWindow.isIcon()) userMaintBaseWindow.setIcon(false);
            userMaintBaseWindow.setSelected(true);
            //userMaintBaseWindow.showUserDetails();
            return ;
       }
       userMaintBaseWindow = new UserMaintenanceBaseWindow(CoeusGuiConstants.USER_ROLE_MAINTENENCE_BASE_WINDOW_FRAME_TITLE, mdiForm);
       //userMaintBaseWindow.showUserDetails();
    }
    
    private void showDepartmentDelegations() throws Exception {
        DepartmentalDelegationsForm departmentalDelegationsForm = 
            new DepartmentalDelegationsForm(mdiForm,
                CoeusGuiConstants.INSTITUTE_UNIT_NUMBER,CoeusGuiConstants.MIT);
    }
    /**
     * display alert message
     *
     * @param mesg the message to be displayed
     */
    private void log(String mesg) {
        CoeusOptionPane.showInfoDialog(mesg);
    }
}