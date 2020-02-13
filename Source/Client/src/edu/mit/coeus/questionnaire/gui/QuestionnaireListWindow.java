/*
 * QuestionnaireListWindow.java
 *
 * Created on September 20, 2006, 11:38 AM
 */

package edu.mit.coeus.questionnaire.gui;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import java.util.Vector;
import java.awt.event.*;
import java.awt.*;



import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
//import edu.mit.coeus.utils.CoeusOptionPane;
/**
 *
 * @author  chandrashekara
 */
public class QuestionnaireListWindow extends CoeusInternalFrame{
    
     public JTable tblResults;
    private JScrollPane scrPnResults;
    private CoeusAppletMDIForm mdiForm;
    private final String SEPERATOR="seperator";
    
    // Declaring the toolbar buttons
    public CoeusToolBarButton btnNewQuestiionnaire, btnModifyQuestiionnaire,btnDisplayQuestionnaire,
                btnSortQuestionnaire, btnSearchQuestionnaire, btnSaveas, btnClose, btnCopyQuestionnaire;
    
    // Declaring the menu items for the File Menu
    public CoeusMenuItem mnuItmInbox, mnuItmClose,mnuItmSaveas, mnuItmChangePassword, 
        /*Added for Case#3682 - Enhancements related to Delegations - Start*/
        mnuItmDelegations,
        /*Added for Case#3682 - Enhancements related to Delegations - End*/
        mnuItmPreferences, mnuItmExit,mnuItmSearch,
        /*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/;
    
    // Declaring the menu items for the Edit menu
    public CoeusMenuItem mnuItmNewQuestiionnaire,mnuItmDisplayQuestionnaire,
            mnuItmModifyQuestiionnaire, mnuItmCopyQuestiionnaire;
    
    private CoeusMenu mnuFile, mnuEdit;
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    public javax.swing.JComboBox cmbGroup;
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
    
    /** Creates a new instance of QuestionnaireListWindow */
     public QuestionnaireListWindow(String title, CoeusAppletMDIForm mdiForm){
        super(title,mdiForm);
        this.mdiForm = mdiForm;
    }
    
    // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start      
//     public void initComponents(JTable tblEmptyResults) {
//         
//         setFrameToolBar(getInstProposalToolBar());
//         prepareMenus();
//         tblResults = tblEmptyResults;
//         scrPnResults = new JScrollPane(tblResults);
//         scrPnResults.getViewport().setBackground(Color.white);
//         getContentPane().add(scrPnResults);
//     }
     public void initComponents(JTable tblEmptyResults) {
         setFrameToolBar(getInstProposalToolBar());
         prepareMenus();
         tblResults = tblEmptyResults;
         
         java.awt.GridBagConstraints gridBagConstraints;
         javax.swing.JPanel pnlQuestions = new javax.swing.JPanel();
         javax.swing.JScrollPane scrPnQuestions = new javax.swing.JScrollPane();
         javax.swing.JPanel pnlGroup = new javax.swing.JPanel();
         javax.swing.JLabel lblGroup = new javax.swing.JLabel();
         cmbGroup = new javax.swing.JComboBox();
         
         getContentPane().setLayout(new java.awt.GridBagLayout());
         
         getContentPane().setMinimumSize(new java.awt.Dimension(833, 480));
         getContentPane().setPreferredSize(new java.awt.Dimension(833, 480));
         pnlQuestions.setLayout(new java.awt.GridBagLayout());
         
         // Modified for COEUSQA-3841 : Coeus 4.5.1 Questionnaire Maintenance UI Issues - Start 
//         pnlQuestions.setMinimumSize(new java.awt.Dimension(1350, 550));
//         pnlQuestions.setPreferredSize(new java.awt.Dimension(1350, 550));
//         scrPnQuestions.setMinimumSize(new java.awt.Dimension(1350, 550));
//         scrPnQuestions.setPreferredSize(new java.awt.Dimension(1350, 550));
         pnlQuestions.setMinimumSize(new java.awt.Dimension(1000, 550));
         pnlQuestions.setPreferredSize(new java.awt.Dimension(1000, 550));
         scrPnQuestions.setMinimumSize(new java.awt.Dimension(1000, 550));
         scrPnQuestions.setPreferredSize(new java.awt.Dimension(1000, 550));
         // Modified for COEUSQA-3841 : Coeus 4.5.1 Questionnaire Maintenance UI Issues - End
         
         scrPnQuestions.setViewportView(tblResults);
         
         pnlQuestions.add(scrPnQuestions, new java.awt.GridBagConstraints());
         
         gridBagConstraints = new java.awt.GridBagConstraints();
         gridBagConstraints.gridx = 0;
         gridBagConstraints.gridy = 1;
         gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
         gridBagConstraints.weightx = 1.0;
         gridBagConstraints.weighty = 1.0;
         gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
         getContentPane().add(pnlQuestions, gridBagConstraints);
         
         pnlGroup.setLayout(new java.awt.GridBagLayout());
         
         pnlGroup.setMaximumSize(new java.awt.Dimension(300, 30));
         pnlGroup.setMinimumSize(new java.awt.Dimension(300, 30));
         pnlGroup.setPreferredSize(new java.awt.Dimension(300, 30));
         lblGroup.setFont(CoeusFontFactory.getLabelFont());
         lblGroup.setText("Group:");
         gridBagConstraints = new java.awt.GridBagConstraints();
         gridBagConstraints.gridx = 0;
         gridBagConstraints.gridy = 0;
         gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
         gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
         pnlGroup.add(lblGroup, gridBagConstraints);
         
         cmbGroup.setMaximumSize(new java.awt.Dimension(250, 20));
         cmbGroup.setMinimumSize(new java.awt.Dimension(250, 20));
         cmbGroup.setPreferredSize(new java.awt.Dimension(250, 20));
         gridBagConstraints = new java.awt.GridBagConstraints();
         gridBagConstraints.gridx = 1;
         gridBagConstraints.gridy = 0;
         gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
         pnlGroup.add(cmbGroup, gridBagConstraints);
         
         gridBagConstraints = new java.awt.GridBagConstraints();
         gridBagConstraints.gridx = 0;
         gridBagConstraints.gridy = 0;
         gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
         gridBagConstraints.weightx = 1.0;
         gridBagConstraints.weighty = 1.0;
         gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
         getContentPane().add(pnlGroup, gridBagConstraints);
         getContentPane().setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
         
     }
     // Modified for COEUSQA-3287 Questionnaire Maintenance Features - Start      
     
       private JToolBar getInstProposalToolBar(){
          JToolBar questionnaireToolBar = new JToolBar();
          
          btnNewQuestiionnaire = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
            null, "Create a new Questionnaire");
          btnModifyQuestiionnaire = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
            null, "Modify Questionnaire");
          btnDisplayQuestionnaire = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
            null, "Display Questionnaire");
          
          btnSortQuestionnaire = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),
            null, "Sort Questionnaire");
          btnSearchQuestionnaire = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)),
            null, "Search for Questionnaire");
          btnSaveas = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
            null, "Save As");
          
          btnClose = new CoeusToolBarButton(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
            null, "Close");
          
          btnCopyQuestionnaire = new CoeusToolBarButton(new ImageIcon(
                  getClass().getClassLoader().getResource(CoeusGuiConstants.COPY_ICON)),
                  null, "Copy Questionnaire");          
          
          questionnaireToolBar.add(btnNewQuestiionnaire);
          questionnaireToolBar.add(btnModifyQuestiionnaire);
          questionnaireToolBar.add(btnDisplayQuestionnaire);
          questionnaireToolBar.add(btnCopyQuestionnaire);
          questionnaireToolBar.addSeparator();
         // questionnaireToolBar.add(btnSortQuestionnaire);
       //   questionnaireToolBar.add(btnSearchQuestionnaire);
          questionnaireToolBar.add(btnSaveas);
          questionnaireToolBar.addSeparator();
          questionnaireToolBar.add(btnClose);
          return questionnaireToolBar;
     }
       
       public void prepareMenus(){
          if(mnuFile== null){
         // Holds the File Menu details
             Vector vecFileMenu = new Vector();
             // Holds the Edit Menu details

              mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
              mnuItmInbox.setMnemonic('I');

              mnuItmClose = new CoeusMenuItem("Close", null, true, true);
              mnuItmClose.setMnemonic('C');

              mnuItmSaveas = new CoeusMenuItem("SaveAs...", null, true, true);
              mnuItmSaveas.setMnemonic('A');

              mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
              mnuItmChangePassword.setMnemonic('h');

             //Added for Case#3682 - Enhancements related to Delegations - Start             
             mnuItmDelegations= new CoeusMenuItem("Delegations...", null, true, true);
             mnuItmDelegations.setMnemonic('g');
             //Added for Case#3682 - Enhancements related to Delegations - End  
              
              mnuItmPreferences = new CoeusMenuItem("Preferences...", null, true, true);
              mnuItmPreferences.setMnemonic('P');

              mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
              mnuItmExit.setMnemonic('x');
              mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
              mnuItmCurrentLocks.setMnemonic('L');

              vecFileMenu.add(mnuItmInbox);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmClose);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmSaveas);
              vecFileMenu.add(mnuItmChangePassword);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmCurrentLocks);
              vecFileMenu.add(SEPERATOR);
              //Added for Case#3682 - Enhancements related to Delegations - Start 
              vecFileMenu.add(mnuItmDelegations);
              vecFileMenu.add(SEPERATOR);
              //Added for Case#3682 - Enhancements related to Delegations - End
              vecFileMenu.add(mnuItmPreferences);
              vecFileMenu.add(SEPERATOR);
              vecFileMenu.add(mnuItmExit);              
              mnuFile = new CoeusMenu("File", null, vecFileMenu, true, true);
              mnuFile.setMnemonic('F');
          }
          if(mnuEdit == null){
              Vector vecEditMenu = new Vector();
              mnuItmNewQuestiionnaire = new CoeusMenuItem("New Questionnaire", null, true, true);
              mnuItmNewQuestiionnaire.setMnemonic('N');

              mnuItmDisplayQuestionnaire = new CoeusMenuItem("Display Questionnaire", null, true, true);
              mnuItmDisplayQuestionnaire.setMnemonic('D');

              mnuItmModifyQuestiionnaire = new CoeusMenuItem("Modify Questionnaire", null, true, true);
              mnuItmModifyQuestiionnaire.setMnemonic('Q');
              
              mnuItmCopyQuestiionnaire = new CoeusMenuItem("Copy Questionnaire", null, true, true);
              mnuItmCopyQuestiionnaire.setMnemonic('C');              
              
              vecEditMenu.add(mnuItmNewQuestiionnaire);
              vecEditMenu.add(mnuItmDisplayQuestionnaire);
              vecEditMenu.add(mnuItmModifyQuestiionnaire);
              vecEditMenu.add(mnuItmCopyQuestiionnaire);
              mnuEdit = new CoeusMenu("Edit", null, vecEditMenu, true, true);
              mnuEdit.setMnemonic('E');
          }
     }
       
       private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        mdiForm.getCoeusMenuBar().add(mnuEdit, 1);
        mdiForm.getCoeusMenuBar().validate();
    }
     
      private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().remove(mnuEdit);
        mdiForm.getCoeusMenuBar().validate();
    }
      
       public void displayResults(JTable results) {
        if(results == null) return ;
        tblResults = results;
        scrPnResults.setViewportView(tblResults);
        this.revalidate();
    }
       
        public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {
        super.internalFrameActivated(internalFrameEvent);
        loadMenus();
    }
    
    
    public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {
        unloadMenus();
        super.internalFrameDeactivated(internalFrameEvent);
        
    }
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
}
