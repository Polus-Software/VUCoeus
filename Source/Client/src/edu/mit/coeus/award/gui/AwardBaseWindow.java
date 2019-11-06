/*
 * AwardBaseWindow.java
 *
 * Created on March 18, 2004, 3:48 PM
 */

package edu.mit.coeus.award.gui;

/**
 *
 * @author  sharathk
 */
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.menu.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.award.bean.AwardBaseBean;

public class AwardBaseWindow extends CoeusInternalFrame{
    
    private CoeusAppletMDIForm mdiForm;
    
    private AwardBaseBean awardBaseBean;
    
    public CoeusToolBarButton btnNext, btnPrevious, btnPrintAwdNotice, btnNotepad,  
        btnRepReq, btnMedusa, btnSave, btnClose, btnSendEmail,btnSync;
    
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    
    //File Menu Items
    public CoeusMenuItem mnuItmInbox, /*mnuItmPrintSetup,*/  mnuItmNext, mnuItmPrevious, 
        mnuItmPrntAwdNotice, mnuItmPrntDeltaRep, mnuItmClose, mnuItmSave,
        mnuItmChangePassword, 
            /*Added for Case#3682 - Enhancements related to Delegations - start*/
            mnuItmDelegations,
            /*Added for Case#3682 - Enhancements related to Delegations - End*/
        mnuItmPreferences, mnuItmExit,
        /*Case 2110 Start*/mnuItmCurrentLocks/*Case 2110 End*/
        //COEUSQA 2111 STARTS
        ,mnuItmRoutPrntAwdNotice,mnuItmRoutPrntDeltaRep,mnuItmRoutingHistory
        //COEUSQA 2111 ENDS
            ;
    
    //Details Menu Items
    public CoeusMenuItem mnuItmCostSharing, mnuItmIndirectCost, mnuItmPaymntSch,
        mnuItmScienceCode, mnuItmSplRate, /* (For the Coeus Enhancement :1799 step:1)mnuItmSplReview,*/ mnuItmSpnsrFndngTrans,
        mnuItmApprEq, mnuItmApprFornTrips, mnuItmFundProps, mnuItmAwdClsOut, 
        mnuItmNotepad, mnuItmRepReq, mnuItmMedusa,mnuItmSync,
            //AWARD ROUTING ENHANCEMENT STARTS
            mnuItmApproveReject,
            //AWARD ROUTING ENHANCEMENT ENDS
            mnuItmDisclosureStatus;
            
    private CoeusMenu mnuFile, mnuDetails;
    
    private char functionType;
    
    /** Creates a new instance of AwardBaseWindow */
    public AwardBaseWindow(String title, CoeusAppletMDIForm mdiForm) {
        super(title, mdiForm);
        this.mdiForm = mdiForm;
        initComponents();
    }
    
    private void initComponents() {
        setFrameToolBar(getAwardBaseWindowToolBar());
        prepareMenus();
    }
    
    private JToolBar getAwardBaseWindowToolBar() {
        JToolBar toolBar = new JToolBar();
        
        btnNext = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ERIGHT_ARROW_ICON)),
        null, "Next");
        
        btnPrevious = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ELEFT_ARROW_ICON)),
        null, "Previous");
        
        btnPrintAwdNotice = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_ICON)),
        null, "Print Award Notice");
        
        btnNotepad = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.NOTEPAD_ICON)),
        null, "Notepad");
        
        btnRepReq = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.REPORTING_REQUIREMENTS_ICON)),
        null, "Reporting Requirements");
        
        btnMedusa = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.MEDUSA_ICON)),
        null, "Medusa");
        
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        //Added for Case#2214 email enhancement
        btnSendEmail = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EMAIL_ICON)), null, "Send Mail Notification");
        
        //Added for case 2796: Sync To Parent
        btnSync = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SYNC_ICON)), null, "Sync to Children");
        
        toolBar.add(btnNext);
        toolBar.add(btnPrevious);
        toolBar.addSeparator();
        toolBar.add(btnPrintAwdNotice);
        toolBar.add(btnNotepad);
        toolBar.add(btnRepReq);
        toolBar.addSeparator();
        toolBar.add(btnMedusa);
        toolBar.add(btnSendEmail);//Added for Case#2214 email enhancement
        toolBar.addSeparator();
        toolBar.add(btnSync);//Added for Case#2796:Sync To Parent
        toolBar.addSeparator();
        toolBar.add(btnSave);
        toolBar.addSeparator();
        toolBar.add(btnClose);
        
        
        return toolBar;
    }
    
    private void prepareMenus() {
        //build File Menu
        Vector vecFile = new Vector();
        
        mnuItmInbox = new CoeusMenuItem("Inbox", null, true, true);
        mnuItmInbox.setMnemonic('I');
        
        // Commented since we are not using it in Coeus 4.0
        //mnuItmPrintSetup = new CoeusMenuItem("PrintSetup", null, true, true);
        //mnuItmPrintSetup.setMnemonic('u');
        
        mnuItmNext = new CoeusMenuItem("Next", null, true, true);
        mnuItmPrevious = new CoeusMenuItem("Previous", null, true, true);
        mnuItmPrntAwdNotice = new CoeusMenuItem("Print Award Notice", null, true, true);
        mnuItmPrntAwdNotice.setMnemonic('N');
        
        mnuItmPrntDeltaRep = new CoeusMenuItem("Print Delta Report", null, true, true);
        mnuItmPrntDeltaRep.setMnemonic('D');
        
        mnuItmClose = new CoeusMenuItem("Close", null, true, true);
        mnuItmClose.setMnemonic('C');
        
        mnuItmSave = new CoeusMenuItem("Save", null, true, true);
        mnuItmSave.setMnemonic('S');
        mnuItmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        
        mnuItmChangePassword = new CoeusMenuItem("Change Password", null, true, true);
        mnuItmChangePassword.setMnemonic('h');
        
        //Added for Case#3682 - Enhancements related to Delegations - Start 
        mnuItmDelegations = new CoeusMenuItem("Delegations", null, true, true);
        mnuItmDelegations.setMnemonic('g');
        //Added for Case#3682 - Enhancements related to Delegations - End
        
        mnuItmPreferences = new CoeusMenuItem("Preferences", null, true, true);
        mnuItmPreferences.setMnemonic('P');
        
        mnuItmExit = new CoeusMenuItem("Exit", null, true, true);
        mnuItmExit.setMnemonic('x');
        
        //Case 2110 Start
        mnuItmCurrentLocks = new CoeusMenuItem("Current Locks",null,true,true);
        mnuItmCurrentLocks.setMnemonic('L');
        //Case 2110 End
        
        //COEUSQA 2111 STARTS
        mnuItmRoutPrntAwdNotice =new CoeusMenuItem("Route Award Notice",null,true,false);
        mnuItmRoutPrntDeltaRep  =new CoeusMenuItem("Route Delta Report",null,true,false);
        mnuItmRoutingHistory    =new CoeusMenuItem("Routing History",null,true,true);
        //COEUSQA 2111 ENDS
        
        vecFile.add(mnuItmInbox);
        vecFile.add(SEPERATOR);
        
        //Commented since we are not using it in Coeus 4.0
        //vecFile.add(mnuItmPrintSetup);
        //vecFile.add(SEPERATOR);
        
        vecFile.add(mnuItmNext);
        vecFile.add(mnuItmPrevious);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmPrntAwdNotice);
        vecFile.add(mnuItmPrntDeltaRep);
        vecFile.add(SEPERATOR);
        //COEUSQA 2111 STARTS
        vecFile.add(mnuItmRoutPrntAwdNotice);
        vecFile.add(mnuItmRoutPrntDeltaRep);
        vecFile.add(SEPERATOR);
        //COEUSQA 2111 ENDS
        vecFile.add(mnuItmClose);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmSave);
        vecFile.add(mnuItmChangePassword);
        //Case 2110 Start
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmCurrentLocks);
        vecFile.add(SEPERATOR);
        //CAse 2110 End
        //Added for Case#3682 - Enhancements related to Delegations - Start
        vecFile.add(mnuItmDelegations);
        vecFile.add(SEPERATOR);
        //Added for Case#3682 - Enhancements related to Delegations - End
        vecFile.add(mnuItmPreferences);
        vecFile.add(SEPERATOR);
        vecFile.add(mnuItmExit);
        
        //Build Details Menu
        mnuItmCostSharing = new CoeusMenuItem("Cost Sharing", null, true, true);
        mnuItmCostSharing.setMnemonic('s');
        
        mnuItmIndirectCost = new CoeusMenuItem("Indirect Cost", null, true, true);
        mnuItmIndirectCost.setMnemonic('d');
        
        mnuItmPaymntSch = new CoeusMenuItem("Payment Schedule", null, true, true);
        mnuItmPaymntSch.setMnemonic('p');
        
        mnuItmScienceCode = new CoeusMenuItem("Science Code", null, true, true);
        mnuItmScienceCode.setMnemonic('c');
                
        mnuItmSplRate = new CoeusMenuItem("Special Rate", null, true, true);
        mnuItmSplRate.setMnemonic('e');
        
        /* For the Coeus Enhancement case:#1799 start step:2 
         *commented for the Coeus Enhancement as it is a tab in the award sheet*/
//        mnuItmSplReview = new CoeusMenuItem("Special Review", null, true, true);
//        mnuItmSplReview.setMnemonic('v');
        /*End Coeus Enhancement case:#1799 step:2*/
        
        mnuItmSpnsrFndngTrans = new CoeusMenuItem("Sponsor Funding Transferred", null, true, true);
        mnuItmSpnsrFndngTrans.setMnemonic('f');
        
        mnuItmApprEq = new CoeusMenuItem("Approved Equipment", null, true, true);
        mnuItmApprEq.setMnemonic('m');
        
        mnuItmApprFornTrips = new CoeusMenuItem("Approved Foreign Trips", null, true, true);
        mnuItmApprFornTrips.setMnemonic('v');
        
        mnuItmFundProps = new CoeusMenuItem("Funding Proposals", null, true, true);
        mnuItmFundProps.setMnemonic('f');
        
        mnuItmAwdClsOut = new CoeusMenuItem("Award Closeout", null, true, true);
        mnuItmAwdClsOut.setMnemonic('l');
        
        mnuItmNotepad = new CoeusMenuItem("Notepad", null, true, true);
        mnuItmNotepad.setMnemonic('t');
        
        mnuItmRepReq = new CoeusMenuItem("Reporting Requirements", null, true, true);
        mnuItmRepReq.setMnemonic('R');
        
        mnuItmMedusa = new CoeusMenuItem("Medusa", null, true, true);
        mnuItmMedusa.setMnemonic('M');
        mnuItmMedusa.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        
        //Added with case 2796: Sync to Parent
        mnuItmSync  = new CoeusMenuItem("Sync to Children", null, true, true);
        mnuItmSync.setMnemonic('y');
        //2796 End
        
        //AWARD ROUTING ENHANCEMENT STARTS
        mnuItmApproveReject=new CoeusMenuItem("Approval/Rejection",null,true,true);
        //AWARD ROUTING ENHANCEMENT ENDS

        //AWARD DISCLOSURE STATUS STARTS
        mnuItmDisclosureStatus = new CoeusMenuItem("COI Disclosure Status...", null, true, true);
        //AWARD DISCLOSURE STATUS ENDS
        Vector vecDetails = new Vector();
        //AWARD ROUTING ENHANCEMENT STARTS
        vecDetails.add(mnuItmApproveReject);
        vecDetails.add(mnuItmRoutingHistory);
        vecDetails.add(SEPERATOR);
       //AWARD ROUTING ENHANCEMENT ENDS
        vecDetails.add(mnuItmCostSharing);
        vecDetails.add(mnuItmIndirectCost);
        vecDetails.add(mnuItmPaymntSch);
        vecDetails.add(mnuItmScienceCode);
        vecDetails.add(mnuItmSplRate);
        //For the Coeus Enhancement case:#1799 start step:3
        //vecDetails.add(mnuItmSplReview);
        //End Coeus Enhancement case:#1799 step:3
        vecDetails.add(mnuItmSpnsrFndngTrans);
        vecDetails.add(SEPERATOR);
        vecDetails.add(mnuItmApprEq);
        vecDetails.add(mnuItmApprFornTrips);
        vecDetails.add(SEPERATOR);
        vecDetails.add(mnuItmFundProps);
        vecDetails.add(SEPERATOR);
        vecDetails.add(mnuItmAwdClsOut);
        vecDetails.add(mnuItmNotepad);
        vecDetails.add(mnuItmRepReq);
        vecDetails.add(SEPERATOR);
        vecDetails.add(mnuItmMedusa);
        //Added with case 2796: Sync to Parent
        vecDetails.add(SEPERATOR);
        vecDetails.add(mnuItmSync);
        //2796 End
        
        

        //AWARD DISCLOSURE STATUS START
        vecDetails.add(mnuItmDisclosureStatus);
        //AWARD DISCLOSURE STATUS END
        mnuFile = new CoeusMenu("File", null, vecFile, true, true);
        mnuFile.setMnemonic('F');
        
        mnuDetails = new CoeusMenu("Details", null, vecDetails, true, true);
        mnuDetails.setMnemonic('t');
    }
    
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
        mdiForm.getCoeusMenuBar().add(mnuDetails, 1);
    }
    
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
        mdiForm.getCoeusMenuBar().add(new CoeusFileMenu(mdiForm).getMenu(), 0);
        mdiForm.getCoeusMenuBar().remove(mnuDetails);
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
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
    
    
    /** Getter for property functionType.
     * @return Value of property functionType.
     *
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     *
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    /**
     * Getter for property awardBaseBean.
     * @return Value of property awardBaseBean.
     */
    public AwardBaseBean getAwardBaseBean() {
        return awardBaseBean;
    }
    
    /**
     * Setter for property awardBaseBean.
     * @param awardBaseBean New value of property awardBaseBean.
     */
    public void setAwardBaseBean(AwardBaseBean awardBaseBean) {
        this.awardBaseBean = awardBaseBean;
    }
    
    //Bug Fix:Performance Issue (Out of memory) Start 1
    public void cleanUp(){
        mdiForm = null;
        awardBaseBean = null;
//        
//        btnNext = null;
//        btnPrevious = null;
//        btnPrintAwdNotice = null;
//        btnNotepad = null;
//        btnRepReq = null;
//        btnMedusa = null;
//        btnSave = null;
//        btnClose = null;
//        
//        mnuItmCostSharing = null;
//        mnuItmIndirectCost = null;
//        mnuItmPaymntSch = null;
//        mnuItmScienceCode = null;
//        mnuItmSplRate = null;
//        mnuItmSplReview = null;
//        mnuItmSpnsrFndngTrans = null;
//        mnuItmApprEq = null;
//        mnuItmApprFornTrips = null;
//        mnuItmFundProps = null;
//        mnuItmAwdClsOut = null;
//        mnuItmNotepad = null;
//        mnuItmRepReq = null;
//        mnuItmMedusa = null;
//        
//        mnuFile = null;
//        mnuDetails = null;
//        
//        mnuItmInbox = null;
//        mnuItmNext= null;
//        mnuItmPrevious = null;
//        mnuItmPrntAwdNotice = null;
//        mnuItmPrntDeltaRep = null;
//        mnuItmClose= null;
//        mnuItmSave= null;
//        mnuItmChangePassword = null;
//        mnuItmPreferences= null;
//        mnuItmExit= null;
    }
    //Bug Fix:Performance Issue (Out of memory) End 1
}
