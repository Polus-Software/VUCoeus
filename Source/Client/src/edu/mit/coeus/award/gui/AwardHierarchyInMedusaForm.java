/*
 * AwardHierarchyInMedusaForm.java
 *
 * Created on July 13, 2004, 11:30 AM
 */

package edu.mit.coeus.award.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.award.bean.AwardHierarchyBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.award.controller.AwardBaseWindowController;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusInternalFrame;

import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.beans.PropertyVetoException;

/**
 *
 * @author  chandru
 */
public class AwardHierarchyInMedusaForm extends javax.swing.JComponent implements
ActionListener,TreeSelectionListener {
    private String awardNumber;
    private CoeusAppletMDIForm mdiForm;
    /** Holds an instance of the TreeSelectionListener */
    private TreeSelectionListener treeSelectionListener;
    private static final String EMPTY_STRING = "";
    private static final String SELECT_AWARD = "award_hierarchy_Medusa_exceptionCode.1552";
    private CoeusMessageResources coeusMessageResources;
    private static final char DISPLAY_MODE = 'D';
    
    /** Creates new form AwardHierarchyInMedusaForm */
    public AwardHierarchyInMedusaForm(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        initComponents();
        registerComponents();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    private void registerComponents(){
        chkDetails.setSelected(false);
        setFormSize(false);
        awardDetailsPanelInMedusa.setVisible(false);
        
        chkDetails.addActionListener(this);
        btnDisplay.addActionListener(this);
        btnMedusa.addActionListener(this);
        awardHierarchyTreeInMedusa.setTreeSelectionListener(this);
    }
    
    /**
     * @param actionEvent
     */    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(chkDetails)){
            showDetails( chkDetails.isSelected());
        }else if(source.equals(btnMedusa)){
            showMedusa();
        }else if(source.equals(btnDisplay)){
            showAward();
        }
    }
    // Show the award details from in display mode. check for the maximum number
    // of instances of the window. If it exceeds throw and info message
    private void showAward(){
        AwardBean awardBean = new AwardBean();
        AwardHierarchyBean bean = awardHierarchyTreeInMedusa.getSelectedObject();
        if(bean.getMitAwardNumber().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_AWARD));
            return ;
        }
        //Code added for Case#3388 - Implementing authorization check at department level - starts
        //Check the user is having rights to view this award
        if(!canViewAward(bean.getMitAwardNumber())){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1056"));
            return;
        }        
        //Code added for Case#3388 - Implementing authorization check at department level - ends
        awardBean.setMitAwardNumber(bean.getMitAwardNumber());
        // Check if the displayed windows exceeds its size(5), then throw the message.
        if(isAwardWindowOpen(bean.getMitAwardNumber(), DISPLAY_MODE)) {
            return ;
        }
        
        AwardBaseWindowController awardBaseWindowController = new AwardBaseWindowController("Display Award : ", DISPLAY_MODE , awardBean);
        awardBaseWindowController.display();
    }
    
    /** Check whether the check box is selected or not. If checked, then display
     * the medusa details. else dont display
     * @param showDetails
     */
    private void showDetails(boolean showDetails){
        if( showDetails ){
            AwardHierarchyBean bean = awardHierarchyTreeInMedusa.getSelectedObject();
            // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role.
            if(canViewAward(bean.getMitAwardNumber())){
                //Show the award details
                awardDetailsPanelInMedusa.setFormData(getAwardNumber());
                awardDetailsPanelInMedusa.setVisible(true);
                setFormSize(true);
            }
        }else{
            //Hide the award details
            awardDetailsPanelInMedusa.setVisible(false);
            setFormSize(false);
        }
    }
    /** Show the medusa window. Select a node from the award hierarchy, select
     *a award number, pass this number and construct the medusa tree once again
     */
    private void showMedusa(){
        try{
            AwardHierarchyBean bean = awardHierarchyTreeInMedusa.getSelectedObject();
            String selAwardNumber = bean.getMitAwardNumber();
            if(selAwardNumber.trim().equals(EMPTY_STRING)){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_AWARD));
                return ;
            }
            ProposalAwardHierarchyLinkBean linkBean;
            MedusaDetailForm medusaDetailform;
            linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setAwardNumber(selAwardNumber);
            linkBean.setBaseType(CoeusConstants.AWARD);
            if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
            CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                if( medusaDetailform.isIcon() ){
                    medusaDetailform.setIcon(false);
                }
                medusaDetailform.setSelectedNodeId(selAwardNumber);
                medusaDetailform.setSelected( true );
                
                return;
            }
            medusaDetailform = new MedusaDetailForm(mdiForm, linkBean);
            medusaDetailform.setVisible(true);
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * @param treeSelectionEvent
     */    
    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        AwardHierarchyBean selectedBean = awardHierarchyTreeInMedusa.getSelectedObject();
        if( selectedBean == null ) return ;
        String selectedMitAwardNumber = selectedBean.getMitAwardNumber();
        if( chkDetails.isSelected() ){
            AwardHierarchyBean bean = awardHierarchyTreeInMedusa.getSelectedObject();
            // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role.
            if(canViewAward(selectedBean.getMitAwardNumber())){
                awardDetailsPanelInMedusa.setFormData(selectedMitAwardNumber);
            }
        }
    }
    
    /** set the size of the form based on whether the check box is selected or not.
     * If selected, set different frame size else cover the window
     * @param isSelected
     */
    private void setFormSize(boolean isSelected){
        if(isSelected){
            pnlTree.setMinimumSize(new java.awt.Dimension(1137, 425));
            pnlTree.setPreferredSize(new java.awt.Dimension(1137, 425));
            awardHierarchyTreeInMedusa.setMinimumSize(new java.awt.Dimension(1137, 425));
            awardHierarchyTreeInMedusa.setPreferredSize(new java.awt.Dimension(1137, 425));
            java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            pnlTree.add(awardHierarchyTreeInMedusa, gridBagConstraints);
        }else{
            pnlTree.setMinimumSize(new java.awt.Dimension(1137, 546));
            pnlTree.setPreferredSize(new java.awt.Dimension(1137, 546));
            awardHierarchyTreeInMedusa.setMinimumSize(new java.awt.Dimension(1137, 546));
            awardHierarchyTreeInMedusa.setPreferredSize(new java.awt.Dimension(1137, 546));
            java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            pnlTree.add(awardHierarchyTreeInMedusa, gridBagConstraints);
        }
    }
    
    /** This method is used to check whether the given Award number is already
     * opened in the given mode or not.
     * @param refId refId - for award its Award Number.
     * @param mode mode of Form open.
     * @param displayMessage if true displays error messages else doesn't.
     * @return true if Award window is already open else returns false.
     */
    boolean isAwardWindowOpen(String refId, char mode, boolean displayMessage) {
        boolean duplicate = false;
        try{
            duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.AWARD_BASE_WINDOW, refId, mode );
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record.
            duplicate = true;
            if( displayMessage ){
                if(e.getMessage().length() > 0 ) {
                    CoeusOptionPane.showInfoDialog(e.getMessage());
                }
            }
            // try to get the requested frame which is already opened
            CoeusInternalFrame frame = mdiForm.getFrame(
            CoeusGuiConstants.AWARD_BASE_WINDOW,refId);
            if (frame != null){
                try{
                    frame.setSelected(true);
                    frame.setVisible(true);
                }catch (PropertyVetoException propertyVetoException) {
                    
                }
            }
            return true;
        }
        return false;
    }
    
    /** This method is used to check whether the given Award number is already
     * opened in the given mode or not and displays message if the award is open
     * @param refId refId - for award its Award Number.
     * @param mode mode of Form open.
     * @return true if Award window is already open else returns false.
     */
    boolean isAwardWindowOpen(String refId, char mode){
        return isAwardWindowOpen(refId, mode, true);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        btnMedusa = new javax.swing.JButton();
        btnDisplay = new javax.swing.JButton();
        chkDetails = new javax.swing.JCheckBox();
        awardDetailsPanelInMedusa = new edu.mit.coeus.award.gui.AwardDetailsPanel();
        pnlTree = new javax.swing.JPanel();
        awardHierarchyTreeInMedusa = new edu.mit.coeus.award.gui.AwardHierarchyTree();

        setLayout(new java.awt.GridBagLayout());

        btnMedusa.setFont(CoeusFontFactory.getLabelFont());
        btnMedusa.setMnemonic('M');
        btnMedusa.setText("Medusa");
        btnMedusa.setMinimumSize(new java.awt.Dimension(113, 23));
        btnMedusa.setPreferredSize(new java.awt.Dimension(113, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 3);
        add(btnMedusa, gridBagConstraints);

        btnDisplay.setFont(CoeusFontFactory.getLabelFont());
        btnDisplay.setMnemonic('D');
        btnDisplay.setText("Display Award");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 0);
        add(btnDisplay, gridBagConstraints);

        chkDetails.setFont(CoeusFontFactory.getLabelFont());
        chkDetails.setText("Show Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 3);
        add(chkDetails, gridBagConstraints);

        awardDetailsPanelInMedusa.setMinimumSize(new java.awt.Dimension(450, 121));
        awardDetailsPanelInMedusa.setPreferredSize(new java.awt.Dimension(425, 121));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(awardDetailsPanelInMedusa, gridBagConstraints);

        pnlTree.setLayout(new java.awt.GridBagLayout());

        pnlTree.setBorder(new javax.swing.border.EtchedBorder());
        pnlTree.setMinimumSize(new java.awt.Dimension(724, 425));
        pnlTree.setPreferredSize(new java.awt.Dimension(724, 425));
        awardHierarchyTreeInMedusa.setMinimumSize(new java.awt.Dimension(1137, 340));
        awardHierarchyTreeInMedusa.setPreferredSize(new java.awt.Dimension(1137, 340));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlTree.add(awardHierarchyTreeInMedusa, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(pnlTree, gridBagConstraints);

    }//GEN-END:initComponents

    /** Getter for property awardNumber.
     * @return Value of property awardNumber.
     *
     */
    public java.lang.String getAwardNumber() {
        return awardNumber;
    }    

    /** Setter for property awardNumber.
     * @param awardNumber New value of property awardNumber.
     *
     */
    public void setAwardNumber(java.lang.String awardNumber) {
        this.awardNumber = awardNumber;
    }    
  
    /** Getter for property treeSelectionListener.
     * @return Value of property treeSelectionListener.
     *
     */
    public TreeSelectionListener getTreeSelectionListener() {
        return treeSelectionListener;
    }    
    
    /** Setter for property treeSelectionListener.
     * @param treeSelectionListener New value of property treeSelectionListener.
     *
     */
    public void setTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
        this.treeSelectionListener = treeSelectionListener;
    }
    
    /**
     * Code added for Case#3388 - Implementing authorization check at department level
     * To check the user is having rights to view this award
     * @param awardNumber
     * @throws CoeusClientException
     * @return boolean
     */    
    private boolean canViewAward(String awardNumber){
        boolean canView = false;
        RequesterBean request = new RequesterBean();
        char CAN_VIEW_AWARD =  'f' ;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalActionServlet";        
        request.setFunctionType(CAN_VIEW_AWARD);
        request.setDataObject(awardNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            canView = ((Boolean)response.getDataObject()).booleanValue();
        }
        return canView;
    }    
     
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.award.gui.AwardDetailsPanel awardDetailsPanelInMedusa;
    public edu.mit.coeus.award.gui.AwardHierarchyTree awardHierarchyTreeInMedusa;
    public javax.swing.JButton btnDisplay;
    public javax.swing.JButton btnMedusa;
    public javax.swing.JCheckBox chkDetails;
    public javax.swing.JPanel pnlTree;
    // End of variables declaration//GEN-END:variables
    
}
