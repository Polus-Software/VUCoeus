/*
 * FeedMaintenanceForm.java
 *
 * Created on December 31, 2004, 10:28 AM
 */

package edu.mit.coeus.centraladmin.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusFileMenu;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.InternalFrameEvent;


/**
 *
 * @author  surekhan
 */
public class FeedMaintenanceForm extends CoeusInternalFrame   {
    
    public JTable tblSapFeedBatches;
    private JScrollPane scrPnSapFeedBatches;
    public JTable tblSapFeedDetails;
    private JScrollPane scrPnSapFeedDetails;
    public JLabel lblFeedDetails;
    public JLabel lblStatus;
    
    public CoeusToolBarButton btnSave,btnClose;
    private CoeusAppletMDIForm mdiForm;
    
    public CoeusMenuItem mnuItmInbox, mnuItmClose,mnuItmSave,
           mnuItmChangePassword,
           /*Added for Case#3682 - Enhancements related to Delegations - Start*/
            mnuItmDelegations,
            /*Added for Case#3682 - Enhancements related to Delegations - End*/ 
            mnuItmPreferences, mnuItmExit,
           /*Case 2110 Start*/mnuItmCurrenLocks/*Case 2110 End*/;
    
    public CoeusMenuItem mnuItmRejectFeed,mnuItmUndoReject,mnuItmAwardFeedHistory,
           mnuItmShowPendingFeeds,mnuItmResendBatch,mnuItmShowFeedData;
    
    private CoeusMenu mnuFile, mnuEdit, mnuTools;
    private final String SEPERATOR="seperator";
    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    
    /** Creates a new instance of FeedMaintenanceForm */
    public FeedMaintenanceForm(String title , CoeusAppletMDIForm mdiForm) {
         super(title,mdiForm);
         this.mdiForm = mdiForm;
         
    }
    
    public boolean requestFocusInWindow() {
        super.requestFocusInWindow();
        tblSapFeedBatches.requestFocus();
        return true;
    }
    
    
    public void initComponents() {
        
        setFrameToolBar(getFeedMaintenanceToolBar());
        prepareMenus();
        lblFeedDetails = new JLabel();
        lblStatus = new JLabel();
        tblSapFeedBatches = new javax.swing.JTable();
        tblSapFeedDetails = new javax.swing.JTable();
        tblSapFeedBatches.setBackground(disabledBackground);
        scrPnSapFeedBatches = new JScrollPane(tblSapFeedBatches);
        scrPnSapFeedBatches.getViewport().setBackground(disabledBackground);
        getContentPane().add(scrPnSapFeedBatches);
        
        scrPnSapFeedDetails = new JScrollPane(tblSapFeedDetails);
        scrPnSapFeedDetails.getViewport().setBackground(disabledBackground);
        getContentPane().add(scrPnSapFeedDetails);
    }
    
    /*to add to the tool bar*/
    private JToolBar getFeedMaintenanceToolBar(){
        JToolBar feedMaintenanceToolBar = new JToolBar();
        
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        feedMaintenanceToolBar.add(btnSave);
        feedMaintenanceToolBar.add(btnClose);
        
        return feedMaintenanceToolBar;
    }
    
    /*to prepare the menu items*/
    public void prepareMenus(){
        if(mnuFile== null){
            // Holds the File Menu details
            Vector vecFileMenu = new Vector();
            mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
            mnuItmInbox.setMnemonic('I');
            
            mnuItmClose = new CoeusMenuItem("Close", null, true, true);
            mnuItmClose.setMnemonic('C');
            
            mnuItmSave = new CoeusMenuItem("Save...", null, true, true);
            mnuItmSave.setMnemonic('S');
        
            mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
            mnuItmChangePassword.setMnemonic('h');
            
            //Added for Case#3682 - Enhancements related to Delegations - Start 
            mnuItmDelegations = new CoeusMenuItem("Delegations...", null, true, true);
            mnuItmDelegations.setMnemonic('g');
            //Added for Case#3682 - Enhancements related to Delegations - End
            
            mnuItmPreferences = new CoeusMenuItem("Preferences...", null, true, true);
            mnuItmPreferences.setMnemonic('P');
            
            mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
            mnuItmExit.setMnemonic('x');
            
            //Case 2110 Start
            mnuItmCurrenLocks = new CoeusMenuItem("Current Locks",null,true,true);
            mnuItmCurrenLocks.setMnemonic('L');
            //Case 2110 End
            
            vecFileMenu.add(mnuItmInbox);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmClose);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmSave);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmChangePassword);
            //Case 2110 Start
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmCurrenLocks);
            vecFileMenu.add(SEPERATOR);
            //CAse 2110 End
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
            mnuItmRejectFeed = new CoeusMenuItem("Reject Feed", null, true, true);
            mnuItmRejectFeed.setMnemonic('R');
            
            mnuItmUndoReject = new CoeusMenuItem("Undo Reject", null, true, true);
            mnuItmUndoReject.setMnemonic('U');
            
            mnuItmAwardFeedHistory = new CoeusMenuItem("Award Feed History...", null, true, true);
            mnuItmAwardFeedHistory.setMnemonic('A');
            
            mnuItmShowPendingFeeds = new CoeusMenuItem("Show Pending Feeds...", null, true, true);
            mnuItmShowPendingFeeds.setMnemonic('P');
            
            mnuItmResendBatch= new CoeusMenuItem("Resend Batch...", null, true, true);
            mnuItmResendBatch.setMnemonic('B');
            
            mnuItmShowFeedData= new CoeusMenuItem("Show Feed Data", null, true, true);
            mnuItmShowFeedData.setMnemonic('D');
            
            
            vecEditMenu.add(mnuItmRejectFeed);
            vecEditMenu.add(mnuItmUndoReject);
            vecEditMenu.add(mnuItmAwardFeedHistory);
            vecEditMenu.add(mnuItmShowPendingFeeds);
            vecEditMenu.add(mnuItmResendBatch);
            vecEditMenu.add(mnuItmShowFeedData);
            mnuEdit = new CoeusMenu("Edit", null, vecEditMenu, true, true);
            mnuEdit.setMnemonic('E');
        }
       
    }
    
    /*to load the menus*/
     private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        mdiForm.getCoeusMenuBar().add(mnuEdit, 1);
        //        mdiForm.getCoeusMenuBar().add(mnuTools, 6);
        mdiForm.getCoeusMenuBar().validate();
    }
     
      /*to unload the menus*/
      private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().remove(mnuEdit);
        //        mdiForm.getCoeusMenuBar().remove(mnuTools);
        mdiForm.getCoeusMenuBar().validate();
    }
      
      
      /*To instantiate the components*/
      public void postInitComponents() {
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        JPanel pnlFeedMaintenance = new JPanel();
        pnlFeedMaintenance.setLayout(new java.awt.GridBagLayout());
        scrPnSapFeedBatches.setBorder(new javax.swing.border.EtchedBorder());
        
        //Bug Fix 1513:Start 1
        scrPnSapFeedBatches.setMinimumSize(new java.awt.Dimension(1000, 275));
        scrPnSapFeedBatches.setPreferredSize(new java.awt.Dimension(1000, 275));
        //Bug Fix 1513:End 1
        
        tblSapFeedBatches.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnSapFeedBatches.setBackground(bgColor);
        pnlFeedMaintenance.setBackground(bgColor);
        scrPnSapFeedBatches.setOpaque(true);        
        scrPnSapFeedBatches.setViewportView(tblSapFeedBatches);
        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
//        gridBagConstraints.weightx = 0;
//        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 3, 1);
        pnlFeedMaintenance.add(scrPnSapFeedBatches,gridBagConstraints);
        
        
//        lblFeedDetails.setMinimumSize(new java.awt.Dimension(200, 21));
//        lblFeedDetails.setPreferredSize(new java.awt.Dimension(200, 21));
        lblFeedDetails.setText("Details for batch : ");
        lblFeedDetails.setFont(CoeusFontFactory.getLabelFont());
        
        GridBagConstraints gridBagConstrnts = new java.awt.GridBagConstraints();
        gridBagConstrnts.gridx = 0;
        gridBagConstrnts.gridy = 2;
        gridBagConstrnts.anchor = java.awt.GridBagConstraints.WEST;
//        gridBagConstrnts.weightx = 0;
//        gridBagConstrnts.weighty = 1.0;
        gridBagConstrnts.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstrnts.insets = new java.awt.Insets(1, 1, 1, 1);
        pnlFeedMaintenance.add(lblFeedDetails,gridBagConstrnts);

        scrPnSapFeedDetails.setBorder(new javax.swing.border.EtchedBorder());
        scrPnSapFeedDetails.setMinimumSize(new java.awt.Dimension(1000, 245));
        scrPnSapFeedDetails.setPreferredSize(new java.awt.Dimension(1000, 245));
        tblSapFeedDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSapFeedDetails.setBackground(disabledBackground);
        scrPnSapFeedDetails.setViewportView(tblSapFeedDetails);
        GridBagConstraints gridBagConstraint = new java.awt.GridBagConstraints();
        gridBagConstraint.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraint.gridx = 0;
        gridBagConstraint.gridy = 3;
//        gridBagConstraint.weightx = 0;
//        gridBagConstraint.weighty = 1.0;
        gridBagConstraint.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraint.insets = new java.awt.Insets(3, 1, 1, 1);
        pnlFeedMaintenance.add(scrPnSapFeedDetails,gridBagConstraint);
        
        
        lblStatus.setBorder(new javax.swing.border.EtchedBorder());
        lblStatus.setMinimumSize(new java.awt.Dimension(1000, 25));
        lblStatus.setPreferredSize(new java.awt.Dimension(1000, 25));
        GridBagConstraints gridBagConst = new java.awt.GridBagConstraints();
        gridBagConst.gridx = 0;
        gridBagConst.gridy = 4;
        gridBagConst.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConst.weightx = 1.0;
        gridBagConst.weighty = 1.0;
        gridBagConst.fill = java.awt.GridBagConstraints.NONE;
        gridBagConst.insets = new java.awt.Insets(1, 1, 1, 1);
        pnlFeedMaintenance.add(lblStatus,gridBagConst); 
        
        //Bug Fix 1513:Start 2
        pnlFeedMaintenance.setPreferredSize(new java.awt.Dimension(900,575));
        //Bug Fix 1513:End 2
        
       JScrollPane scrPnBasepanel = new JScrollPane(pnlFeedMaintenance);
       //scrPnBasepanel.setPreferredSize(new java.awt.Dimension(1200,600));
       getContentPane().add(scrPnBasepanel);
       this.revalidate();
       
    }
      
       private void setColumnSizes() {
        int colSize[] = {75, 90, 125, 50,300};
        for(int columnIndex = 0; columnIndex < colSize.length; columnIndex++) {
            tblSapFeedBatches.getColumnModel().getColumn(columnIndex).setPreferredWidth(colSize[columnIndex]);
            tblSapFeedBatches.getColumnModel().getColumn(columnIndex).setMinWidth(colSize[columnIndex]);
        }
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
