/*
 * @(#)CoeusAdminMenu.java 1.0 10/18/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 05-August-2010
 * by Johncy M John
 */

package edu.mit.coeus.gui.menu;

import edu.mit.coeus.admin.controller.AwardBasisController;
import edu.mit.coeus.admin.controller.ClassReportFreqController;
import edu.mit.coeus.admin.controller.CostElementListController;
import edu.mit.coeus.admin.controller.MailActionController;
import edu.mit.coeus.admin.controller.MethodOfPaymentController;
import edu.mit.coeus.admin.controller.ValidFrequencyController;
import edu.mit.coeus.admin.controller.QuestionMaintainanceController;
import edu.mit.coeus.admin.controller.ValidCostElementJobCodesController;
import edu.mit.coeus.admin.controller.ValidRateTypeController;
import edu.mit.coeus.admin.controller.CustomElementsController;
import edu.mit.coeus.admin.gui.CostElementListForm;
import edu.mit.coeus.admin.gui.SelectAwardTemplateForm;
import edu.mit.coeus.user.gui.UnitUserRolesMaintenanceBaseWindow;


import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.organization.gui.OrganizationBaseWindow;
import edu.mit.coeus.unit.gui.UnitHierarchyBaseWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;

import edu.mit.coeus.irb.gui.AreaOfResearchBaseWindow;  //From CoeusMaintainMenu

import edu.mit.coeus.utils.birt.BirtReport;
import edu.mit.coeus.utils.birt.controller.ReportBaseWindowController;
import edu.mit.coeus.utils.birt.gui.ReportBaseWindow;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.codetable.client.CodeTableBaseWindow ;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.questionnaire.controller.QuestionMaintenanceController;
import edu.mit.coeus.questionnaire.controller.QuestionnaireListController;
import edu.mit.coeus.questionnaire.gui.QuestionnaireListWindow;
import java.beans.PropertyVetoException;

/**
 * This class creates Admin menu for the coeus application.
 *
 * @version :1.0 October 18, 2002, 3:11 PM
 * @author Guptha
 * @modified by Sagin
 * @date 29-10-02
 * Description : Error message pops up while opening Organization.
 */

public class CoeusAdminMenu extends JMenu implements ActionListener{

    /*
     * admin menu items
     */
    public CoeusMenuItem awardTemplate,codeTables,customElements,organization,
            questions, ynq, //commented by nadh rates,
            unitHierarchy;

    /*
     * award rules menu items
     */
    private CoeusMenuItem awardBasics,classReportFrequency,frequency,
            methodOfPayment,questionnaire, mailAction;
    /*
     * cost elements menu items
     */
    private CoeusMenuItem details,validRateTypes, validJobCodes;
    
    private CoeusMenuItem areaOfResearch; //From CoeusMaintainMenu
    
    private CoeusMenuItem iacucAreaOfResearch;
    // Added for COEUSQA-1692_User Access - Maintenance_start
    public CoeusMenuItem userRolesMaintenance;
    // Added for COEUSQA-1692_User Access - Maintenance_end
    /**
     * For Birt Report
     */
    private CoeusMenuItem reportMaintenance;

    /*
     * to indicate horizondal seperator in menu items
     */
   // private final String SEPERATOR="seperator";

    private CoeusMenu coeusMenu;
    

    private CoeusAppletMDIForm mdiForm;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    /** Default constructor which constructs the admin menu for coeus application.
     * @param mdiForm  CoeusAppletMDIForm
     */
    public CoeusAdminMenu(CoeusAppletMDIForm mdiForm){
        super();
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        createMenu();
    }

    /**
     * This method is used to get the Admin menu
     *
     * @return JMenu coeus Admin menu
     */
    public JMenu getMenu(){
        return coeusMenu;
    }

    /**
     * This method is used to create Admin menu for coeus application.
     */
    private void createMenu(){
        java.util.Vector fileChildren = new java.util.Vector();

        Vector awardRulesChildren = new Vector();
        awardBasics = new CoeusMenuItem("Award Basis...",null,true,true);
        awardBasics.setMnemonic('A');
        classReportFrequency = new CoeusMenuItem("Class Report Frequency...",
                                                                null,true,true);
        classReportFrequency.setMnemonic('C');
        frequency = new CoeusMenuItem("Frequency...",null,true,true);
        frequency.setMnemonic('F');
        methodOfPayment = new CoeusMenuItem("Method of Payment...",null,true,true);
        methodOfPayment.setMnemonic('M');
        awardRulesChildren.add(awardBasics);
        awardRulesChildren.add(classReportFrequency);
        awardRulesChildren.add(frequency);
        awardRulesChildren.add(methodOfPayment);
        CoeusMenu awardRules = new CoeusMenu("Award Rules...",null,
                                                awardRulesChildren,true,true);
        awardRules.setMnemonic('U');

        awardTemplate = new CoeusMenuItem("Award Template...",null,true,true);
        awardTemplate.setMnemonic('A');
        codeTables = new CoeusMenuItem("Code Tables...",null,true,true);
        codeTables.setMnemonic('C');

        Vector costElementsChildren = new Vector();
        details = new CoeusMenuItem("Details...",null,true,true);
        details.setMnemonic('D');
        validRateTypes = new CoeusMenuItem("Valid Rate Types...",null,true,true);
        validRateTypes.setMnemonic('V');
        validJobCodes = new CoeusMenuItem("Valid Job Codes...",null,true,true);
        validJobCodes.setMnemonic('J');
        costElementsChildren.add(details);
        costElementsChildren.add(validRateTypes);
        costElementsChildren.add(validJobCodes);
        CoeusMenu costElements = new CoeusMenu("Cost Elements...",null,
                                                costElementsChildren,true,true);
        
        
        costElements.setMnemonic('E');

        customElements = new CoeusMenuItem("Custom Elements...",null,true,true);
        customElements.setMnemonic('S');
        organization = new CoeusMenuItem("Organization...",null,true,true);
        organization.setMnemonic('O');
        
        ynq = new CoeusMenuItem("YNQ...",null,true,true);
        ynq.setMnemonic('Y');
        
        //commented by nadh - Start
//        rates = new CoeusMenuItem("Rates...",null,true,true);
//        rates.setMnemonic('R');
        //commenting end - nadh
        unitHierarchy = new CoeusMenuItem("Unit Hierarchy...",null,true,true);
        unitHierarchy.setMnemonic('H');
        
        areaOfResearch = new CoeusMenuItem("IRB Area Of Research...",null,true,true);
        areaOfResearch.setMnemonic('F'); //From CoeusMaintainMenu
        
        //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research
        iacucAreaOfResearch = new CoeusMenuItem("IACUC Area Of Research...",null,true,true);
        iacucAreaOfResearch.setMnemonic('G'); //From CoeusMaintainMenu
        
        questions = new CoeusMenuItem("Questions...",null,true,true);
        questions.setMnemonic('Q');        
        
        questionnaire = new CoeusMenuItem("Questionnaire...",null,true,true);
        questionnaire.setMnemonic('t'); 
        
//        mailAction = new CoeusMenuItem("Notification Recipients...", null, true, true);
//        mailAction.setMnemonic('M');
        mailAction = new CoeusMenuItem("Notifications...", null, true, true);
        mailAction.setMnemonic('N');
        
        // Added for COEUSQA-1692_User Access - Maintenance_start
        userRolesMaintenance = new CoeusMenuItem("User Role Maintenance...", null, true, true);
        userRolesMaintenance.setMnemonic('l');
        // Added for COEUSQA-1692_User Access - Maintenance_end
        reportMaintenance = new CoeusMenuItem("Report Maintenance", null, true, true);
        reportMaintenance.setMnemonic('R');

        fileChildren.add(awardRules);
        fileChildren.add(awardTemplate);
        fileChildren.add(codeTables);
        fileChildren.add(costElements);
        fileChildren.add(customElements);
        fileChildren.add(organization);
        fileChildren.add(ynq);
//        fileChildren.add(rates); commented by nadh
        fileChildren.add(unitHierarchy);
        
        fileChildren.add(areaOfResearch); //From CoeusMaintainMenu
        //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research
        fileChildren.add(iacucAreaOfResearch);        
        fileChildren.add(questions);        
        fileChildren.add(questionnaire);
        fileChildren.add(mailAction);
        // Added for COEUSQA-1692_User Access - Maintenance_start
        fileChildren.add(userRolesMaintenance);
        // Added for COEUSQA-1692_User Access - Maintenance_end
        fileChildren.add(reportMaintenance);
        
        coeusMenu = new CoeusMenu("Admin",null,fileChildren,true,true);
        coeusMenu.setMnemonic('D');

        //add listener
        awardBasics.addActionListener(this);
        classReportFrequency.addActionListener(this);
        frequency.addActionListener(this);
        methodOfPayment.addActionListener(this);
        awardTemplate.addActionListener(this);
        codeTables.addActionListener(this);

        customElements.addActionListener(this);
        organization.addActionListener(this);
        ynq.addActionListener(this);
//        rates.addActionListener(this); commented by nadh
        unitHierarchy.addActionListener(this);
        details.addActionListener(this);
        validRateTypes.addActionListener(this);
        validJobCodes.addActionListener(this);
        areaOfResearch.addActionListener(this);  //From CoeusMaintainMenu
       
        //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research
        iacucAreaOfResearch.addActionListener(this);
        questions.addActionListener(this);
        questionnaire.addActionListener(this);
        mailAction.addActionListener(this);
        // Added for COEUSQA-1692_User Access - Maintenance_start
        userRolesMaintenance.addActionListener(this);
        // Added for COEUSQA-1692_User Access - Maintenance_end
        reportMaintenance.addActionListener(this);
    }

    /** This method is used to handle the action event for the Admin menu items.
     * @param ae  ActionEvent
     */
    public void actionPerformed(ActionEvent ae){
        Object source = ae.getSource();
        try{
            if (source.equals(awardBasics)){
                showAwardBasis();
            }else if (source.equals(methodOfPayment)){
                showMethodOfPayment();
            }else if (source.equals(organization)){
                showOrganization();
            }else if (source.equals(unitHierarchy)){
                showUnitHierarchy();
            }else if (source.equals(codeTables)){ //prps added
                showCodeTables();                // prps added  
            }else if (source.equals(areaOfResearch)){    //From CoeusMaintainMenu
                showAreaOfResearch();
            }else if(source.equals(classReportFrequency)){
                showClassReportFreq();
            }else if(source.equals(frequency)){
                showFrequencywindow();
            }else if(source.equals(questions)){
                showQuestionsForm();
            }else if(source.equals(validJobCodes)){
                showValidJobCodes();
            }else if(source.equals(details)){
                showCostElementDetails();
           }else if(source.equals(validRateTypes)) {
                showValidRateTypes();
            }else if(source.equals(customElements)) {
                showCustomElements();
            } else if (source.equals(awardTemplate)) {
                showAwardTemplates();
            }else if(source.equals(questionnaire)){
                showQuestionnaire();
            }else if(source.equals(ynq)){
                showYNQ();
            }else if(source.equals(mailAction)) {
                showValidMailAction();
            }else if(source.equals(reportMaintenance)){
                showReportMaintenance();
            } 
            
            //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research
            else if(source.equals(iacucAreaOfResearch)){  
                showIACUCAreaOfResearch();
            }
            // Added for COEUSQA-1692_User Access - Maintenance_start
            else if(source.equals(userRolesMaintenance)){  
                showUserRolesMaintenance();
            }
            // Added for COEUSQA-1692_User Access - Maintenance_end
            else {   
              log(coeusMessageResources.parseMessageKey(
                                                "funcNotImpl_exceptionCode.1100"));
            }
        }catch(Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog(e.getMessage());
        }
    }   
  
    
    private void showQuestionnaire()  throws edu.mit.coeus.exception.CoeusException,PropertyVetoException{
        String title = CoeusGuiConstants.QUESTIONNAIRE_FRAME_TITLE;
        QuestionnaireListWindow listWindow = null;
        if( ( listWindow = (QuestionnaireListWindow)mdiForm.getFrame(
         title))!= null ){
             if( listWindow.isIcon() ){
                 listWindow.setIcon(false);
             }
             listWindow.setSelected( true );
             return;
         }
         QuestionnaireListController controller = new QuestionnaireListController();
        controller.display();
    }
    
  // Added by chandra to implement question form 
    private void showQuestionsForm() throws edu.mit.coeus.exception.CoeusException{
        QuestionMaintenanceController questionMaintenanceController = 
           new QuestionMaintenanceController("Question Maintenance",mdiForm.getUnitNumber());
        questionMaintenanceController.setFormData(null);
        questionMaintenanceController.display();
    }
    
    private void showYNQ() throws edu.mit.coeus.exception.CoeusException{
        QuestionMaintainanceController questionMaintainanceController = 
            new QuestionMaintainanceController(mdiForm);
        questionMaintainanceController.display();
    }    

    // jenlu start
    
   private void showCodeTables()
    {
        System.out.println(" code tables accessed") ;
  
        //jenlu changed to following: jenlu
        CodeTableBaseWindow codeTableBaseWindow =
                (CodeTableBaseWindow) mdiForm.getFrame(CoeusGuiConstants.TITLE_CODE_TABLE);
        if (codeTableBaseWindow != null) {
            try {
                codeTableBaseWindow.setSelected(true);
            } catch (Exception ex) {
            }
            codeTableBaseWindow.setVisible(true);
            return;
        } else {
            codeTableBaseWindow = new CodeTableBaseWindow(mdiForm);
        }

        return ;
    }
 
    
    
    //jenlu end
   
   private void showValidJobCodes() throws edu.mit.coeus.exception.CoeusException{
       ValidCostElementJobCodesController validCostElementJobCodesController = new ValidCostElementJobCodesController();
       validCostElementJobCodesController.display();
       
   }
   
   private void showFrequencywindow() throws edu.mit.coeus.exception.CoeusException{
        ValidFrequencyController validFrequencyController = new ValidFrequencyController(mdiForm);
   }

   public void showValidRateTypes(){
        ValidRateTypeController  validRateTypeController = new ValidRateTypeController(mdiForm);
        validRateTypeController.display();
        
    }
   
   public void showCustomElements() throws CoeusException {
     CustomElementsController customElementsController = new CustomElementsController(mdiForm);
     customElementsController.display();
   }
   
   public void showAwardTemplates () throws CoeusException {
       SelectAwardTemplateForm selectAwardTemplateForm = new SelectAwardTemplateForm (mdiForm,true);
       selectAwardTemplateForm.display();
   }
    
    private void showUnitHierarchy(){
        UnitHierarchyBaseWindow unitHierarchyBaseWindow =
                (UnitHierarchyBaseWindow) mdiForm.getFrame(
                    CoeusGuiConstants.TITLE_UNIT_HIERARCHY);
        if (unitHierarchyBaseWindow != null) {
            try {
                if( unitHierarchyBaseWindow.isIcon() ){
                    unitHierarchyBaseWindow.setIcon(false);
                }
                unitHierarchyBaseWindow.setSelected(true);
            } catch (Exception ex) {
            }
            unitHierarchyBaseWindow.setVisible(true);
            return;
        } else {
            unitHierarchyBaseWindow = new UnitHierarchyBaseWindow(mdiForm);
        }

    }
    
   private void showAreaOfResearch() throws Exception{

        AreaOfResearchBaseWindow aorFrame = null;
        if ( (aorFrame = (AreaOfResearchBaseWindow)mdiForm.getFrame(
                CoeusGuiConstants.TITLE_AREA_OF_RESEARCH))!= null )  {
            if( aorFrame.isIcon() ){
                aorFrame.setIcon(false);
            }
            aorFrame.setSelected(true);
            return;
        }
        aorFrame = new AreaOfResearchBaseWindow(mdiForm);
        aorFrame.setVisible( true );
    }
   
   //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research - start
   
   /**
   * Method used to disply 'IACUC Area Of Research' screen in menu.
   */
   private void showIACUCAreaOfResearch() throws Exception{

     edu.mit.coeus.iacuc.gui.AreaOfResearchBaseWindow aorFrame = null;
     if ( (aorFrame = (edu.mit.coeus.iacuc.gui.AreaOfResearchBaseWindow)mdiForm.getFrame(
            CoeusGuiConstants.TITLE_IACUC_AREA_OF_RESEARCH))!= null )  {
         if( aorFrame.isIcon() ){
            aorFrame.setIcon(false);
         }
         aorFrame.setSelected(true);
         return;
     }
     aorFrame = new edu.mit.coeus.iacuc.gui.AreaOfResearchBaseWindow(mdiForm);
     aorFrame.setVisible( true );
   }
   
   //COEUSQA-2684  IACUC - Areas of research maintenance screen for IACUC areas of research - end
  
   // Added for COEUSQA-1692_User Access - Maintenance_start
   /**
    *Display User Search window if required rights are available else a validation message
    *@exception throws Exception 
    */
   private void showUserRolesMaintenance() throws Exception{
     UnitUserRolesMaintenanceBaseWindow unitUserRolesMaintenanceBaseWindow = null;
     unitUserRolesMaintenanceBaseWindow = new UnitUserRolesMaintenanceBaseWindow();     
   }
   // Added for COEUSQA-1692_User Access - Maintenance_end
   
// From CoeusMaintainMenu
    private void showOrganization(){

        OrganizationBaseWindow organizationBaseWindow = (OrganizationBaseWindow)
                   mdiForm.getFrame(CoeusGuiConstants.ORGANIZATION_FRAME_TITLE);
        if (organizationBaseWindow != null) {
            try {
                if( organizationBaseWindow.isIcon() ){
                        organizationBaseWindow.setIcon(false);
                }
                organizationBaseWindow.setSelected(true);
            } catch (Exception ex) {
            }
            organizationBaseWindow.setVisible(true);
            return;
        } else {

            organizationBaseWindow = new OrganizationBaseWindow(
                                    CoeusGuiConstants.ORGANIZATION_WINDOW_TITLE,
                                    CoeusGuiConstants.ORGANIZATION_FRAME_TITLE,
                                    mdiForm );
        }

    }
    
    //Display Class Report Frequency
    private void showClassReportFreq(){
        ClassReportFreqController classReportFreqController = new ClassReportFreqController(mdiForm);
    }   
    
    //Display AwardBasis
    private void showAwardBasis(){
        try {
            AwardBasisController awardBasisController = new AwardBasisController(mdiForm);
            awardBasisController.display();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //Display MethodOfPayment
    private void showMethodOfPayment(){
        try {
            MethodOfPaymentController methodOfPaymentController = new MethodOfPaymentController(mdiForm);
            methodOfPaymentController.display();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //Display CostElementList
    private void showCostElementDetails(){
        try {
            CostElementListForm costElementList= null;
            if( ( costElementList = (CostElementListForm)mdiForm.getFrame(
            CoeusGuiConstants.COST_ELEMENT_FRAME_TITLE))!= null ){
                return;
            }
            CostElementListController costElementListController = new CostElementListController(mdiForm);
            costElementListController.display();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /*
     * Added this method for implementing email functionality.
     * this interface is used to relate the recipients with corresponding actions
     */
    //Display ValidMailAction
    private void showValidMailAction() {
     MailActionController mailActionController = new MailActionController(mdiForm);
     mailActionController.display();
    }
    //Email implementation - end
    
    private void showReportMaintenance() {
        BirtReport birtReport = new BirtReport();
        boolean hasRights = birtReport.hasMaintainRights();
        if (!hasRights) {
            //Doesn't have rights
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("reportMaintenanceCode.1005"));
        } else {
            ReportBaseWindow reportBaseWindow = (ReportBaseWindow) mdiForm.getFrame(CoeusGuiConstants.REPORT_FRAME_TITLE);
            if (reportBaseWindow != null) {
                try {
                    if (reportBaseWindow.isIcon()) {
                        reportBaseWindow.setIcon(false);
                    }
                    reportBaseWindow.setSelected(true);
                } catch (Exception ex) {
                }
                reportBaseWindow.setVisible(true);
                return;
            } else {
                ReportBaseWindowController reportBaseWindowController = new ReportBaseWindowController(CoeusGuiConstants.REPORT_FRAME_TITLE, mdiForm);
            }
        }
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