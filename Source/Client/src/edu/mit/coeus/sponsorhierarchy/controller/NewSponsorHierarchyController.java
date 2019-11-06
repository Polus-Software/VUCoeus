/*
 * NewSponsorHierarchyController.java
 *
 * Created on November 18, 2004, 2:56 PM
 */

package edu.mit.coeus.sponsorhierarchy.controller;


import edu.mit.coeus.sponsorhierarchy.gui.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.event.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author  surekhan
 */
public class NewSponsorHierarchyController implements ActionListener  {
    
    /*instantiating sponsorhierarchy form*/
    private NewSponsorHierarchyForm newSponsorHierarchyForm;
    
    /*newSponsorHierarchyDialog*/
    private CoeusDlgWindow dlgNewSponsorHierarchy;
    
    /*mdi form*/
    private CoeusAppletMDIForm mdiForm;
    
    private CoeusMessageResources coeusMessageResources;
    
    /*width of the dialog*/
    private static final int WIDTH = 380;
    
    /*height of the dialog*/
    private static final int HEIGHT = 95;
    
    private static final String WINDOW_TITLE = "New Sponsor Hierarchy";
    
    private static final String EMPTY_STRING = "";
    
    private CoeusVector cvHierarchyNames;
    
    private CoeusVector cvNames = new CoeusVector();
    
    private String  name ;
    
    private boolean btnAction;
    
    private boolean canDispose;
    
    /*Please enter a name for the new Sponsor Hierarchy to be created*/
    private static final String TEXT_MSG = "sponsorHierarchyList_exceptionCode.1203";
    
    /** Creates a new instance of NewSponsorHierarchyController */
    public NewSponsorHierarchyController(CoeusVector cvData , boolean action) {
        this.mdiForm = mdiForm;
        cvNames = cvData;
        btnAction = action;
        coeusMessageResources = CoeusMessageResources.getInstance();
        cvHierarchyNames = new CoeusVector();
        postInitComponents();
        registerComponents();
    }
    
    private void postInitComponents(){
        newSponsorHierarchyForm = new NewSponsorHierarchyForm();
        dlgNewSponsorHierarchy = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm() , true);
        dlgNewSponsorHierarchy.setResizable(false);
        dlgNewSponsorHierarchy.setModal(true);
        dlgNewSponsorHierarchy.getContentPane().add(newSponsorHierarchyForm);
        dlgNewSponsorHierarchy.setFont(CoeusFontFactory.getLabelFont());
        dlgNewSponsorHierarchy.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgNewSponsorHierarchy.setSize(WIDTH, HEIGHT);
        dlgNewSponsorHierarchy.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgNewSponsorHierarchy.getSize();
        dlgNewSponsorHierarchy.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgNewSponsorHierarchy.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                //dlgNewSponsorHierarchy.dispose();
                dlgNewSponsorHierarchy.setVisible(false);
            }
        });
        
         dlgNewSponsorHierarchy.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent actionEvent){
                    dlgNewSponsorHierarchy.setVisible(false);
           }
        });
    }
    
    public boolean display(){
        setRequestFocusInThread(newSponsorHierarchyForm.txtSponsor);
        dlgNewSponsorHierarchy.show();
        return canDispose;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        dlgNewSponsorHierarchy.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(source.equals(newSponsorHierarchyForm.btnOk)){
            if(btnAction){
                performCopyAction();
            }else{
                performNewAction();
                canDispose = true;
            }
        }else if(source.equals(newSponsorHierarchyForm.btnCancel)){
            dlgNewSponsorHierarchy.dispose();
            canDispose = false;
        }
        dlgNewSponsorHierarchy.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    /*the action performed on the click of Copy button*/
    private void performCopyAction(){
        try{
            if(validate()){
                dlgNewSponsorHierarchy.dispose();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    /*the action performed on the click of New button*/
    private void performNewAction(){
        try{
            if(validate()){
                String hierarchyName = newSponsorHierarchyForm.txtSponsor.getText().trim();
                String title = "New Sponsor Hierarchy - " +hierarchyName;
                SponsorHierarchyBaseWindowController controller = new SponsorHierarchyBaseWindowController(title , hierarchyName , 'N');
                dlgNewSponsorHierarchy.dispose();
                controller.display();
            }
        }catch(Exception exception){
             exception.printStackTrace();
        }
    }
    
    /*to register the listeners and the components*/
    private void registerComponents(){
       newSponsorHierarchyForm.txtSponsor.setDocument(new LimitedPlainDocument(100));
       newSponsorHierarchyForm.btnOk.addActionListener(this);
       newSponsorHierarchyForm.btnCancel.addActionListener(this);
       
       java.awt.Component[] component = {newSponsorHierarchyForm.txtSponsor,newSponsorHierarchyForm.btnOk,newSponsorHierarchyForm.btnCancel};
       newSponsorHierarchyForm.txtSponsor.addKeyListener(new KeyAdapter() {
               public void keyPressed(KeyEvent e){
                   if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                       if(!btnAction){
                          performNewAction();
                       }else{
                           performCopyAction();
                       }
                        canDispose = true;
                   }
               }
            });
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        newSponsorHierarchyForm.setFocusTraversalPolicy(policy);
        newSponsorHierarchyForm.setFocusCycleRoot(true);
   }
    
    /*to perform all the validations*/
    private boolean  validate() throws CoeusException{
        if(newSponsorHierarchyForm.txtSponsor.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(TEXT_MSG));
            setRequestFocusInThread(newSponsorHierarchyForm.txtSponsor);
            return false;
        }
        String text = newSponsorHierarchyForm.txtSponsor.getText().trim();
        //Modified for Case #2362 Start
        if(cvNames != null && cvNames.size() > 0){
            if(cvNames.contains(newSponsorHierarchyForm.txtSponsor.getText().trim())){
                CoeusOptionPane.showErrorDialog("A hierarchy already exists with the name " +text+ ". Please enter a unique name");
                setRequestFocusInThread(newSponsorHierarchyForm.txtSponsor);
                return false;
            }else{
                name = newSponsorHierarchyForm.txtSponsor.getText().trim();
            }
        }else{
            name = text;
        }
        //Modified for Case #2362 End
        return true;
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
    
    /*to send the hierarchy name to the base window*/
    public String HierarchyName(){
        return name;
    }
}
