/*
 * SponsorHierarchyListController.java
 *
 * Created on November 17, 2004, 10:16 AM
 */

package edu.mit.coeus.sponsorhierarchy.controller;


import edu.mit.coeus.sponsorhierarchy.gui.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.sponsormaint.bean.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import javax.swing.*;
/**
 *
 * @author  surekhan
 */
public  class SponsorHierarchyListController extends SponsorHierarchyController implements ActionListener{
    
    /*instantiating sponsorHierarchyListForm*/
    private SponsorHierarchyListForm sponsorHierarchyListForm;
    
    /*sponsorHierarchy dialog */
    private CoeusDlgWindow dlgSponsorHierarchyList;
    
    /*instantiating mdiform */
    private CoeusAppletMDIForm mdiForm;
    
    /*queryengine*/
    private QueryEngine queryEngine;
    
    private CoeusMessageResources coeusMessageResources;
    
    /*width of the hierarchy list form*/
    private static final int WIDTH = 435;
    
    /*height of the hierarchy list form*/
    private static final int HEIGHT = 275;
    
    /*title of the window*/
    private static final String WINDOW_TITLE = "Sponsor Hierarchy List";
    
    private CoeusVector cvHierarchy;
    
    private CoeusVector cvHierarchyList;
   
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/spMntServlet";
    
    private boolean maintainSponsor;
    
    private boolean deleteSponsor;
    
    private boolean copySponsor;
    
    private String copyName;
    
    private static final String DELETE_ACTION = "delete_action";
    
    private static final String COPY_ACTION = "copy_action";
    
    private static final String NEW_ACTION = "new_action";
    
    /*Please select a sponsor hierarchy to copy*/
    private static final String COPY_MSG = "sponsorHierarchyList_exceptionCode.1201";
    
    /*Please select a sponsor hierarchy to delete.*/
    private static final String DELETE_MSG = "sponsorHierarchyList_exceptionCode.1200";
    
    /*Please select a sponsor hierarchy*/
    private static final String MAINTAIN_MSG = "sponsorHierarchyList_exceptionCode.1202";
    
    
    
    /** Creates a new instance of SponsorHierarchyListController */
    public SponsorHierarchyListController() {
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        cvHierarchy = new CoeusVector();
        authorizationCheck();
        postInitComponents();
        registerComponents();
        setFormData(null);
    }
    
    private void postInitComponents(){
        sponsorHierarchyListForm = new SponsorHierarchyListForm();
        dlgSponsorHierarchyList = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm() , true);
        dlgSponsorHierarchyList.setResizable(false);
        dlgSponsorHierarchyList.setModal(true);
        dlgSponsorHierarchyList.getContentPane().add(sponsorHierarchyListForm);
        dlgSponsorHierarchyList.setFont(CoeusFontFactory.getLabelFont());
        dlgSponsorHierarchyList.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgSponsorHierarchyList.setSize(WIDTH, HEIGHT);
        dlgSponsorHierarchyList.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgSponsorHierarchyList.getSize();
        dlgSponsorHierarchyList.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgSponsorHierarchyList.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                dlgSponsorHierarchyList.dispose();
            }
        });
        
        dlgSponsorHierarchyList.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent actionEvent){
                    dlgSponsorHierarchyList.dispose();
           }
        });
        
        
     
        if(!maintainSponsor){
            sponsorHierarchyListForm.btnNew.setEnabled(false);
            sponsorHierarchyListForm.btnCopy.setEnabled(false);
            sponsorHierarchyListForm.btnDelete.setEnabled(false);
            sponsorHierarchyListForm.btnClose.setEnabled(true);
            sponsorHierarchyListForm.btnMaintain.setText("View");
            sponsorHierarchyListForm.btnMaintain.setFont(CoeusFontFactory.getLabelFont());
            sponsorHierarchyListForm.btnMaintain.setMnemonic('V');
        }
            
        
        
    }
    
    private void authorizationCheck(){
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('L');
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CONNECTION_STRING);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null){
        }
        CoeusVector  cvSponsorDetails = (CoeusVector)responderBean.getDataObject();
        cvHierarchy = (CoeusVector)cvSponsorDetails.get(0);
        maintainSponsor = ((Boolean)cvSponsorDetails.get(1)).booleanValue();

    }
    
    public void display() { 
        setRequestFocusInThread(sponsorHierarchyListForm.lstSponsorList);
        dlgSponsorHierarchyList.show();
        
    }    
    
    public void formatFields() {
        
    }
    
    public Component getControlledUI() {
        return sponsorHierarchyListForm;
    }
    
    public Object getFormData() {
        return sponsorHierarchyListForm;
    }
    
    public void registerComponents() {
        java.awt.Component[] components = {sponsorHierarchyListForm.lstSponsorList,
        sponsorHierarchyListForm.btnNew,sponsorHierarchyListForm.btnMaintain,
        sponsorHierarchyListForm.btnCopy , sponsorHierarchyListForm.btnDelete,
        sponsorHierarchyListForm.btnClose};
        
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        sponsorHierarchyListForm.setFocusTraversalPolicy(traversePolicy);
        sponsorHierarchyListForm.setFocusCycleRoot(true);
        
        sponsorHierarchyListForm.btnNew.addActionListener(this);
        sponsorHierarchyListForm.btnCopy.addActionListener(this);
        sponsorHierarchyListForm.btnMaintain.addActionListener(this);
        sponsorHierarchyListForm.btnClose.addActionListener(this);
        sponsorHierarchyListForm.btnDelete.addActionListener(this);
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) {
        dlgSponsorHierarchyList.setTitle(WINDOW_TITLE);
        JList lst = new JList(cvHierarchy);
        //Modified for Case #2362 start
        if(cvHierarchy != null && cvHierarchy.size() > 0){
            sponsorHierarchyListForm.lstSponsorList.setListData(cvHierarchy);
            sponsorHierarchyListForm.lstSponsorList.setSelectedIndex(0);
        }
        //Modified for Case #2362 end
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            dlgSponsorHierarchyList.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if(source.equals(sponsorHierarchyListForm.btnNew)){
                performNewAction();
            }else if(source.equals(sponsorHierarchyListForm.btnClose)){
                dlgSponsorHierarchyList.dispose();
            }else if(source.equals(sponsorHierarchyListForm.btnDelete)){
                performDeleteAction();
            }else if(source.equals(sponsorHierarchyListForm.btnCopy)){
                performCopy();
            }else if(source.equals(sponsorHierarchyListForm.btnMaintain)){
                performMaintainAction();
            }
        }finally{
            dlgSponsorHierarchyList.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void performDeleteAction(){
        if(sponsorHierarchyListForm.lstSponsorList.getSelectedIndex() == -1){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(DELETE_MSG));
            return;
        }
        String hierarchyName = sponsorHierarchyListForm.lstSponsorList.getSelectedValue().toString();
        int index = sponsorHierarchyListForm.lstSponsorList.getSelectedIndex();
        if(index != -1){
            int option = CoeusOptionPane.showQuestionDialog("Do you want to delete the hierarchy - " + hierarchyName   , CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
            if(option == CoeusOptionPane.SELECTION_YES){
                getDataFromServer(DELETE_ACTION);
           }
       }
   }
    
    private void performMaintainAction(){
        String title;
        char functionType;
        if(sponsorHierarchyListForm.lstSponsorList.getSelectedIndex() == -1){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(MAINTAIN_MSG));
            return;
        }
        
        String hierarchyName = sponsorHierarchyListForm.lstSponsorList.getSelectedValue().toString();
        if(maintainSponsor){
           title = "Maintain Sponsor Hierarchy - " + hierarchyName;
           functionType = 'M';
        }else{
            title = "Sponsor Hierarchy - " + hierarchyName;
            functionType = 'D';
        }
        if(isHierarchyOpen(hierarchyName,functionType)) {
            dlgSponsorHierarchyList.dispose();
            return;
        }
        SponsorHierarchyBaseWindowController controller = new SponsorHierarchyBaseWindowController(title , hierarchyName , functionType);
        controller.display();
        dlgSponsorHierarchyList.dispose();
    }
    
    private void performCopy(){
        if(sponsorHierarchyListForm.lstSponsorList.getSelectedIndex() == -1){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(COPY_MSG));
            return;
        }
        NewSponsorHierarchyController controller = new NewSponsorHierarchyController(cvHierarchy , true);
        controller.display();
        copyName = controller.HierarchyName();
        getDataFromServer(COPY_ACTION);
    }
    
    private void performNewAction(){
        NewSponsorHierarchyController controller = new NewSponsorHierarchyController(cvHierarchy , false);
        if(controller.display())
            dlgSponsorHierarchyList.dispose();
    }
    
    private void getDataFromServer(String mode){
        String hierarchyName = sponsorHierarchyListForm.lstSponsorList.getSelectedValue().toString();
        int index = sponsorHierarchyListForm.lstSponsorList.getSelectedIndex();
        RequesterBean requesterBean = new RequesterBean();
        if(mode.equals(DELETE_ACTION)){
            requesterBean.setFunctionType('N');
            requesterBean.setDataObject(hierarchyName);
        }else{
            requesterBean.setFunctionType('P');
            CoeusVector cvHierarchyNames = new CoeusVector();
            cvHierarchyNames.add(0, hierarchyName);
            cvHierarchyNames.add(1, copyName);
            requesterBean.setDataObject(cvHierarchyNames);
        }
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CONNECTION_STRING);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(mode.equals(DELETE_ACTION)){
            if(responderBean != null){
                deleteSponsor = responderBean.isSuccessfulResponse();
                if(deleteSponsor){
                    cvHierarchy.remove(index);
                    sponsorHierarchyListForm.lstSponsorList.setListData(cvHierarchy);
                }
            }
        }else{
            if(responderBean != null){
                copySponsor = responderBean.isSuccessfulResponse();
                if(copySponsor){
                    cvHierarchy.add(copyName);
//                    cvHierarchy.sort("hierarchyName", true);
                    sponsorHierarchyListForm.lstSponsorList.setListData(cvHierarchy);
                    
                }
            }
        }
    }
    /** Supporting method which will be used for the focus lost for date
     *fields. This will be fired when the request focus for the specified
     *date field is invoked
     */
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    public void cleanUp() {
        
    }
    
}
