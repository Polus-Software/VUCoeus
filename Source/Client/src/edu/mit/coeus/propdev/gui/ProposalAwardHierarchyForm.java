/*
 * ProposalAwardHierarchyForm.java
 *
 * Created on January 2, 2004, 3:10 PM
 */

package edu.mit.coeus.propdev.gui;


import edu.mit.coeus.gui.CoeusFontFactory;
//import edu.mit.coeus.utils.AppletServletCommunicator;
//import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Or;

import java.util.*;
import java.awt.event.*;
//import java.awt.Component;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
/**
 *
 * @author  chandrashekara
 */
public class ProposalAwardHierarchyForm extends javax.swing.JComponent implements ItemListener{
    
    private Hashtable htMedusa;
    private static final char MEDUSA_DETAIL = 'L';
    private final String PROPOSAL_AWARD_DETAIL ="/ProposalActionServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROPOSAL_AWARD_DETAIL;
    private String selectedNodeId;
    //  private ProposalDetailForm proposalDetailForm;
    private ProposalAwardNodeRenderer proposalAwardNodeRenderer;
    private static final String EMPTY_STRING = "";
    
    private CoeusVector cvInstProp , cvAward,cvDevProp,cvSubContract, cvIrbProtocol, cvIacucProtocol;
    private HashMap hmInstProp = new HashMap();
    private HashMap hmDevProp = new HashMap();
    private HashMap hmAward = new HashMap();
    private HashMap hmSubCont = new HashMap();
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    private HashMap hmProtocol;
    //COEUSQA:2653 - End
    private ProposalAwardHierarchyLinkBean selectedObject;
    private TreeSelectionListener treeSelectionListener;
    private boolean showSubContract = true;
    private boolean saveRequired = false;
    private DefaultMutableTreeNode root;
    private ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean;
    private boolean listenerRemoved;
    /** Creates new form ProposalAwardHierarchyForm */
    public ProposalAwardHierarchyForm() {
    }
   
    
    /**
     * @param selectedNodeId
     */    
//    public void construct(String selectedNodeId) {
    public void construct(ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean) {
        //this.selectedNodeId = selectedNodeId;
        this.proposalAwardHierarchyLinkBean = proposalAwardHierarchyLinkBean;
        if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
            selectedNodeId = proposalAwardHierarchyLinkBean.getDevelopmentProposalNumber();
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
            selectedNodeId = proposalAwardHierarchyLinkBean.getInstituteProposalNumber();
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
            selectedNodeId = proposalAwardHierarchyLinkBean.getAwardNumber();
        }
        //COEUSQA:2653 - Add Protocols to Medusa - Start
        else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IRB_PROTOCOL)){
            selectedNodeId = proposalAwardHierarchyLinkBean.getIrbProtocolNumber();
        }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IACUC_PROTOCOL)){
            selectedNodeId = proposalAwardHierarchyLinkBean.getIacucProtocolNumber();
        }
        //COEUSQA:2653 - End
        initComponents();
        proposalAwardNodeRenderer = new ProposalAwardNodeRenderer();
        registerComponents();
        ((DefaultTreeModel)treeProposalAwardHierarchy.getModel()).setRoot(new ProposalAwardHierarchyNode(new ProposalAwardHierarchyLinkBean()));
        root = (DefaultMutableTreeNode)((DefaultTreeModel)treeProposalAwardHierarchy.getModel()).getRoot();
        setFormData(false);
        treeProposalAwardHierarchy.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        display();
        
        setDefaultFocusForComponent();
    }

    /**
     * @param htValues
     * @param selectedNodeId
     *This overidden method is used to open the notepad for a selected node value
     */    
    public void construct(Hashtable htValues,String selectedNodeId, ProposalAwardHierarchyLinkBean linkBean) {
        this.htMedusa = htValues;
        this.proposalAwardHierarchyLinkBean = linkBean;
        this.selectedNodeId = selectedNodeId;
        initComponents();
        proposalAwardNodeRenderer = new ProposalAwardNodeRenderer();
        registerComponents();
        ((DefaultTreeModel)treeProposalAwardHierarchy.getModel()).setRoot(new ProposalAwardHierarchyNode(new ProposalAwardHierarchyLinkBean()));
        root = (DefaultMutableTreeNode)((DefaultTreeModel)treeProposalAwardHierarchy.getModel()).getRoot();
        setFormData(true);
        treeProposalAwardHierarchy.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        display();
        
        java.awt.Component[] components = {rdbtnProposalToAward,rdbtnProposalToAward,
        treeProposalAwardHierarchy};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
        setDefaultFocusForComponent();
    }
    
    
    /** get the data for the institute proposa number, development proposal number,
     *award and subcontract number and get the 4 vectors for each number
     */
    private Hashtable getFormData() throws CoeusClientException{
        Hashtable htMedusa;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(MEDUSA_DETAIL);
        //request.setDataObject(selectedNodeId);
        request.setDataObject(proposalAwardHierarchyLinkBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            htMedusa = (Hashtable)response.getDataObject();
            return htMedusa;
        }else {
            throw new CoeusClientException(response.getMessage());
        }
    }
    
    public void setDefaultFocusForComponent(){
        rdbtnProposalToAward.requestFocusInWindow();
    }
    
    private void display(){
        rdbtnProposalToAward.setSelected(true);
        this.setVisible(true);
    }
    /** Extract the data from the hash map and assign to the 4 vectors, correspondingly
     *for the INSTITUTE_PROPOSAL,AWARDS,DEVELOPMENT_PROPOSAL and SUBCONTRACT
     */
    private void setFormData(boolean dataAvailable){
        try{
            if( ! dataAvailable ) {
                htMedusa = getFormData();
            }
            cvInstProp = null;
            cvAward = null;
            cvDevProp = null;
            cvSubContract = null;
            cvIrbProtocol = null;
            cvIacucProtocol = null;
            
            cvInstProp = (CoeusVector) htMedusa.get("INSTITUTE_PROPOSAL");
            cvInstProp.sort("instituteProposalNumber",true);
            cvAward = (CoeusVector) htMedusa.get("AWARDS");
            cvDevProp = (CoeusVector) htMedusa.get("DEVELOPMENT_PROPOSAL");
            cvDevProp.sort("developmentProposalNumber",true);
            cvSubContract = (CoeusVector) htMedusa.get("SUBCONTRACT");
            cvIrbProtocol = (CoeusVector)htMedusa.get("IRB_PROTOCOL");
            cvIacucProtocol = (CoeusVector)htMedusa.get("IACUC_PROTOCOL");
            
        }catch(CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
        }
        
        treeProposalAwardHierarchy.setRootVisible(false);
    }
    
    
    private void registerComponents(){
        rdbtnProposalToAward.addItemListener(this);
        rdbtnAwardToProposal.addItemListener(this);
        
        treeProposalAwardHierarchy.setCellRenderer(proposalAwardNodeRenderer);
        treeProposalAwardHierarchy.addTreeSelectionListener(treeSelectionListener);
        
        java.awt.Component[] components = {rdbtnProposalToAward,rdbtnProposalToAward,
        treeProposalAwardHierarchy};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
        
       
    }
    
    public void itemStateChanged(ItemEvent itemEvent) {
        TreePath selectedPath = treeProposalAwardHierarchy.getSelectionPath();
        String selectedNodeValue = "";
        if( selectedPath != null ) {
            ProposalAwardHierarchyNode selectedNode = ( ProposalAwardHierarchyNode )selectedPath.getLastPathComponent();
            selectedNode = ( ProposalAwardHierarchyNode )selectedPath.getLastPathComponent();
            ProposalAwardHierarchyLinkBean linkBean = selectedNode.getDataObject();
            selectedNodeValue = linkBean.toString();
        }else{
            selectedNodeValue = selectedNodeId;
        }
        
        Object source = itemEvent.getSource();
        if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
            if(source ==rdbtnProposalToAward){
                // This is used for Notepad to fire the save confirmation
                if(isSaveRequired()){
                    treeProposalAwardHierarchy.removeTreeSelectionListener(treeSelectionListener);
                    listenerRemoved = true;
                    buildProposalToAwardTreeHierarchy();
                }else{
                    listenerRemoved = false;
                    buildProposalToAwardTreeHierarchy();
                }
            }else if(source.equals(rdbtnAwardToProposal)){
                if(isSaveRequired()){
                    treeProposalAwardHierarchy.removeTreeSelectionListener(treeSelectionListener);
                    listenerRemoved = true;
                    buildAwardToProposalTreeHierarchy();
                }else{
                    listenerRemoved = false;
                    buildAwardToProposalTreeHierarchy();
                }
            }
            
              /* When the selection changes between Proposal -> Award and Award - > Proposal
               *the same selected node has to selected when the state changes
               */
            TreePath newSelectedPath = findByName(treeProposalAwardHierarchy,selectedNodeValue );
            if( newSelectedPath != null ) {
                treeProposalAwardHierarchy.expandPath( newSelectedPath );
                treeProposalAwardHierarchy.setSelectionPath( newSelectedPath );
                treeProposalAwardHierarchy.scrollRowToVisible(
                treeProposalAwardHierarchy.getRowForPath(newSelectedPath));
            }
            if(listenerRemoved){
                treeProposalAwardHierarchy.addTreeSelectionListener(treeSelectionListener);
            }
        }
    }
        
    /** Construct the tree when proposal->Award is selected.first get the inst propo number
     *by passing the development proposal number and get the one inst prop number associated
     *with it. Get the award numbers for the inst prop number and keep one hash map to
     *avaoid the duplication. filter for the award number for a particular inst prop number
     *and extract the award numbers and extract for each award number corresponding
     *inst prop number. For each inst prop number get the corresponding dev prop number.
     *Get the subcontract number for each award number. For each inst prop number, Dev prop
     *number, Award and Subcontract keep one hash map where bean as a key and corresponding
     *number as a value. Clear the hash map after each block is over
     */
    public void buildProposalToAwardTreeHierarchy(){
        root.removeAllChildren();
        if( cvInstProp != null && cvInstProp.size() >  0 ) {
            ProposalAwardHierarchyNode instTreeNode = null;
            for( int instIndex = 0 ; instIndex < cvInstProp.size(); instIndex++ ) {
                ProposalAwardHierarchyBean instHierarchyBean = (ProposalAwardHierarchyBean)cvInstProp.get(instIndex);
                if( !hmInstProp.containsKey(instHierarchyBean.getInstituteProposalNumber()) && instHierarchyBean.getInstituteProposalNumber() != null) {
                    hmInstProp.put(instHierarchyBean.getInstituteProposalNumber(),instHierarchyBean);
                    ProposalAwardHierarchyLinkBean instLinkBean =
                    new ProposalAwardHierarchyLinkBean( instHierarchyBean );
                    instLinkBean.setBaseType(CoeusConstants.INST_PROP);
                    instTreeNode = new ProposalAwardHierarchyNode(instLinkBean);
                    root.add(instTreeNode);
                    CoeusVector cvDev = cvDevProp.filter(
                    new Equals("instituteProposalNumber", instHierarchyBean.getInstituteProposalNumber()));
                    
                    if( cvDev != null && cvDev.size() > 0 ) {
                        ProposalAwardHierarchyNode devTreeNode = null;
                        for ( int devIndex = 0 ; devIndex < cvDev.size(); devIndex++ ) {
                            ProposalAwardHierarchyBean devHierarchyBean = (ProposalAwardHierarchyBean)cvDev.get(devIndex);
                            if( !hmDevProp.containsKey(devHierarchyBean.getDevelopmentProposalNumber()) && devHierarchyBean.getDevelopmentProposalNumber() != null ) {
                                hmDevProp.put(devHierarchyBean.getDevelopmentProposalNumber(), devHierarchyBean);
                                ProposalAwardHierarchyLinkBean devLinkBean =
                                new ProposalAwardHierarchyLinkBean( devHierarchyBean );
                                devLinkBean.setBaseType(CoeusConstants.DEV_PROP);
                                devTreeNode = new ProposalAwardHierarchyNode(devLinkBean);
                            /*If there is no inst prop number for the Dev prop number then
                             *display only the current dev prop number. It means this tree
                             *doesn't contain any inst prop number, award and subcontract
                             */
                                //                                 if( proposalId.equals(devLinkBean.getDevelopmentProposalNumber())){
                                //                                     currentDevPropNode = devTreeNode;
                                //                                 }
                                instTreeNode.add(devTreeNode);
                                //COEUSQA:2653 - Add Protocols to Medusa - Start
                                // IRB Protocols
                                if(cvIrbProtocol != null) {
                                    CoeusVector cvIrbProt = cvIrbProtocol.filter(
                                            new Equals("developmentProposalNumber", devHierarchyBean.getDevelopmentProposalNumber()));
                                    if( cvIrbProt != null && cvIrbProt.size() > 0 ) {                                        
                                       buildTreeForProtocol(devTreeNode, cvIrbProt, CoeusConstants.IRB_PROTOCOL);
                                    }
                                }                                
                                //IACUC Protocols
                                if(cvIacucProtocol != null) {
                                    CoeusVector cvIacucProt = cvIacucProtocol.filter(
                                            new Equals("developmentProposalNumber", devHierarchyBean.getDevelopmentProposalNumber()));
                                    if( cvIacucProt != null && cvIacucProt.size() > 0 ) {                                        
                                           buildTreeForProtocol(devTreeNode, cvIacucProt, CoeusConstants.IACUC_PROTOCOL);
                                    }
                                }                                
                                //COEUSQA:2653 - End
                            }
                        }
                    }
                    //COEUSQA:2653 - Add Protocols to Medusa - Start
                    // IRB Protocols
                    if(cvIrbProtocol != null) {
                        CoeusVector cvIrbProt = cvIrbProtocol.filter(
                                new Equals("instituteProposalNumber", instHierarchyBean.getInstituteProposalNumber()));
                        if( cvIrbProt != null && cvIrbProt.size() > 0 ) {
                              buildTreeForProtocol(instTreeNode, cvIrbProt, CoeusConstants.IRB_PROTOCOL);
                        }
                    }
                    
                    //IACUC Protocols
                    if(cvIacucProtocol != null) {
                        CoeusVector cvIacucProt = cvIacucProtocol.filter(
                                new Equals("instituteProposalNumber", instHierarchyBean.getInstituteProposalNumber()));
                        if( cvIacucProt != null && cvIacucProt.size() > 0 ) {                            
                             buildTreeForProtocol(instTreeNode, cvIacucProt, CoeusConstants.IACUC_PROTOCOL);
                        }
                    }
                    //COEUSQA:2653 - End
                    CoeusVector cvInstAwards = cvAward.filter(
                    new Equals("instituteProposalNumber", instHierarchyBean.getInstituteProposalNumber()));
                    
                    if( cvInstAwards != null && cvInstAwards.size() > 0 ) {
                        ProposalAwardHierarchyNode awdTreeNode = null;
                        for ( int awdIndex = 0 ; awdIndex < cvInstAwards.size(); awdIndex++ ) {
                            ProposalAwardHierarchyBean awdHierarchyBean = (ProposalAwardHierarchyBean)cvInstAwards.get(awdIndex);
                            if( !hmAward.containsKey(awdHierarchyBean.getAwardNumber()) && awdHierarchyBean.getAwardNumber() != null) {
                                hmAward.put(awdHierarchyBean.getAwardNumber(), awdHierarchyBean);
                                ProposalAwardHierarchyLinkBean awdLinkBean =
                                new ProposalAwardHierarchyLinkBean( awdHierarchyBean );
                                awdLinkBean.setBaseType(CoeusConstants.AWARD);
                                awdTreeNode = new ProposalAwardHierarchyNode(awdLinkBean);
                                instTreeNode.add(awdTreeNode);
                                
                                if( showSubContract ) {
                                    CoeusVector cvSubCon = cvSubContract.filter(
                                    new Equals("awardNumber", awdHierarchyBean.getAwardNumber()));
                                    if( cvSubCon != null && cvSubCon.size() > 0 ) {
                                        ProposalAwardHierarchyNode awdSubTreeNode = null;
                                        for ( int subIndex = 0 ; subIndex < cvSubCon.size(); subIndex++ ) {
                                            ProposalAwardHierarchyBean subHierarchyBean = (ProposalAwardHierarchyBean)cvSubCon.get(subIndex);
                                            if( !hmSubCont.containsKey(subHierarchyBean.getSubcontractNumber()) ) {
                                                hmSubCont.put(subHierarchyBean.getSubcontractNumber(), subHierarchyBean);
                                                ProposalAwardHierarchyLinkBean subLinkBean =
                                                new ProposalAwardHierarchyLinkBean( subHierarchyBean );
                                                subLinkBean.setBaseType(CoeusConstants.SUBCONTRACT);
                                                awdSubTreeNode = new ProposalAwardHierarchyNode(subLinkBean);
                                                awdTreeNode.add(awdSubTreeNode);
                                            }
                                        }
                                        hmSubCont.clear();
                                    }
                                }
                                
                                //COEUSQA:2653 - Add Protocols to Medusa - Start
                                // IRB Protocols
                                if(cvIrbProtocol != null) {
                                    CoeusVector cvIrbProtForAward = cvIrbProtocol.filter(
                                            new Equals("awardNumber", awdHierarchyBean.getAwardNumber()));
                                    if( cvIrbProtForAward != null && cvIrbProtForAward.size() > 0 ) {                                        
                                        buildTreeForProtocol(awdTreeNode, cvIrbProtForAward, CoeusConstants.IRB_PROTOCOL);
                                    }
                                }                                
                                //IACUC Protocols
                                if(cvIacucProtocol != null) {
                                    CoeusVector cvIacucProtForAward = cvIacucProtocol.filter(
                                            new Equals("awardNumber", awdHierarchyBean.getAwardNumber()));
                                    if( cvIacucProtForAward != null && cvIacucProtForAward.size() > 0 ) {                                         
                                        buildTreeForProtocol(awdTreeNode, cvIacucProtForAward, CoeusConstants.IACUC_PROTOCOL);
                                    }
                                }
                                //COEUSQA:2653 - End
                            }
                        }
                        hmAward.clear();
                    }        
                }
            }
            
            //COEUSQA:2653 - Add Protocols to Medusa - Start
            if( cvDevProp != null && cvDevProp.size() > 0 ) {
                Or irbOrIacuc = new Or(new NotEquals("irbProtocolNumber", null), new NotEquals("iacucProtocolNumber", null));
                CoeusVector cvDevProposals = cvDevProp.filter(irbOrIacuc);
                if( cvDevProposals != null && cvDevProposals.size() > 0 ) {
                    ProposalAwardHierarchyNode devTreeNode = null;
                    for (Object devProposals : cvDevProposals) {
                        ProposalAwardHierarchyBean devHierarchyBean = (ProposalAwardHierarchyBean)devProposals;
                        
                        if( !hmDevProp.containsKey(devHierarchyBean.getDevelopmentProposalNumber()) && devHierarchyBean.getDevelopmentProposalNumber() != null) {
                            hmDevProp.put(devHierarchyBean.getDevelopmentProposalNumber(), devHierarchyBean);
                            ProposalAwardHierarchyLinkBean devLinkBean =
                                    new ProposalAwardHierarchyLinkBean( devHierarchyBean );
                            devLinkBean.setBaseType(CoeusConstants.DEV_PROP);
                            devTreeNode = new ProposalAwardHierarchyNode(devLinkBean);
                            root.add(devTreeNode);
                            //IRB Protocols
                            if(cvIrbProtocol != null) {
                                CoeusVector cvIrbProt = cvIrbProtocol.filter(
                                        new Equals("developmentProposalNumber", devHierarchyBean.getDevelopmentProposalNumber()));
                                if( cvIrbProt != null && cvIrbProt.size() > 0 ) {
                                    buildTreeForProtocol(devTreeNode, cvIrbProt, CoeusConstants.IRB_PROTOCOL);
                                }
                            }
                            //IACUC Protocols
                            if(cvIacucProtocol != null) {
                                CoeusVector cvIacucProt = cvIacucProtocol.filter(
                                        new Equals("developmentProposalNumber", devHierarchyBean.getDevelopmentProposalNumber()));
                                if( cvIacucProt != null && cvIacucProt.size() > 0 ) {
                                    buildTreeForProtocol(devTreeNode, cvIacucProt, CoeusConstants.IACUC_PROTOCOL);
                                }
                            }
                        }
                    }
                }
            }
            //COEUSQA:2653 - End            
            hmInstProp.clear();
            hmDevProp.clear();
            (( DefaultTreeModel )treeProposalAwardHierarchy.getModel() ).reload();
            expandAll(treeProposalAwardHierarchy,true);
        }
        else{
            ProposalAwardHierarchyNode devTreeNode = null;
            ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
            if( proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
                if( !hmDevProp.containsKey(selectedNodeId) ) {
                    proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
                    proposalAwardHierarchyBean.setDevelopmentProposalNumber(selectedNodeId);
                    hmDevProp.put(proposalAwardHierarchyBean.getDevelopmentProposalNumber(), proposalAwardHierarchyBean);
                    ProposalAwardHierarchyLinkBean devLinkBean;
                    devLinkBean = new ProposalAwardHierarchyLinkBean( proposalAwardHierarchyBean );
                    devLinkBean.setBaseType(CoeusConstants.DEV_PROP);
                    devTreeNode = new ProposalAwardHierarchyNode(devLinkBean);
                    root.add(devTreeNode);
                    
                    //COEUSQA:2653 - Add Protocols to Medusa - Start
                    // IRB Protocols
                    if(cvIrbProtocol != null) {
                        CoeusVector cvIrbProt = cvIrbProtocol.filter(
                                new Equals("developmentProposalNumber", proposalAwardHierarchyBean.getDevelopmentProposalNumber()));
                        if( cvIrbProt != null && cvIrbProt.size() > 0 ) {                            
                             buildTreeForProtocol(devTreeNode, cvIrbProt, CoeusConstants.IRB_PROTOCOL);
                        }
                    }                    
                    //IACUC Protocols
                    if(cvIacucProtocol != null) {
                        CoeusVector cvIacucProt = cvIacucProtocol.filter(
                                new Equals("developmentProposalNumber", proposalAwardHierarchyBean.getDevelopmentProposalNumber()));
                        if( cvIacucProt != null && cvIacucProt.size() > 0 ) {                            
                             buildTreeForProtocol(devTreeNode, cvIacucProt, CoeusConstants.IACUC_PROTOCOL);
                        }
                    }
                    //COEUSQA:2653 - End
                }
                hmDevProp.clear();
            }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
                if( !hmAward.containsKey(selectedNodeId) ) {
                    ProposalAwardHierarchyNode awdTreeNode = null;
                    ProposalAwardHierarchyBean awdHierarchyBean = new ProposalAwardHierarchyBean();
                    awdHierarchyBean.setAwardNumber(selectedNodeId);
                    hmAward.put(awdHierarchyBean.getAwardNumber(), awdHierarchyBean);
                    ProposalAwardHierarchyLinkBean awdLinkBean =
                    new ProposalAwardHierarchyLinkBean( awdHierarchyBean );
                    awdLinkBean.setBaseType(CoeusConstants.AWARD);
                    awdTreeNode = new ProposalAwardHierarchyNode(awdLinkBean);
                    root.add(awdTreeNode);
                    
                    if( showSubContract ) {
                        CoeusVector cvSubCon = cvSubContract.filter(
                        new Equals("awardNumber", awdHierarchyBean.getAwardNumber()));
                        if( cvSubCon != null && cvSubCon.size() > 0 ) {
                            ProposalAwardHierarchyNode awdSubTreeNode = null;
                            for ( int subIndex = 0 ; subIndex < cvSubCon.size(); subIndex++ ) {
                                ProposalAwardHierarchyBean subHierarchyBean = (ProposalAwardHierarchyBean)cvSubCon.get(subIndex);
                                if( !hmSubCont.containsKey(subHierarchyBean.getSubcontractNumber()) ) {
                                    hmSubCont.put(subHierarchyBean.getSubcontractNumber(), subHierarchyBean);
                                    ProposalAwardHierarchyLinkBean subLinkBean =
                                    new ProposalAwardHierarchyLinkBean( subHierarchyBean );
                                    subLinkBean.setBaseType(CoeusConstants.SUBCONTRACT);
                                    awdSubTreeNode = new ProposalAwardHierarchyNode(subLinkBean);
                                    awdTreeNode.add(awdSubTreeNode);
                                }
                            }
                            hmSubCont.clear();
                        }
                    }                                       
                    
                    //COEUSQA:2653 - Add Protocols to Medusa - Start
                    // IRB Protocols
                    if(cvIrbProtocol != null) {
                        CoeusVector cvIrbProt = cvIrbProtocol.filter(
                                new Equals("awardNumber", awdHierarchyBean.getAwardNumber()));
                        if( cvIrbProt != null && cvIrbProt.size() > 0 ) {
                              buildTreeForProtocol(awdTreeNode, cvIrbProt, CoeusConstants.IRB_PROTOCOL);
                        }
                    }                    
                    //IACUC Protocols
                    if(cvIacucProtocol != null) {
                        CoeusVector cvIacucProt = cvIacucProtocol.filter(
                                new Equals("awardNumber", awdHierarchyBean.getAwardNumber()));
                        if( cvIacucProt != null && cvIacucProt.size() > 0 ) {
                            buildTreeForProtocol(awdTreeNode, cvIacucProt, CoeusConstants.IACUC_PROTOCOL);
                        }
                    }
                    //COEUSQA:2653 - End                  
                }
                hmAward.clear();
                
            }else if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
                if( !hmInstProp.containsKey(selectedNodeId) ) {
                    ProposalAwardHierarchyNode instTreeNode = null;
                    ProposalAwardHierarchyBean instHierarchyBean = new ProposalAwardHierarchyBean();
                    instHierarchyBean.setInstituteProposalNumber(selectedNodeId);
                    hmInstProp.put(instHierarchyBean.getInstituteProposalNumber(), instHierarchyBean);
                    ProposalAwardHierarchyLinkBean instLinkBean =
                    new ProposalAwardHierarchyLinkBean( instHierarchyBean );
                    instLinkBean.setBaseType(CoeusConstants.INST_PROP);
                    instTreeNode  = new ProposalAwardHierarchyNode(instLinkBean );
                    root.add(instTreeNode);
                    
                     CoeusVector cvDev = cvDevProp.filter(
                    new Equals("instituteProposalNumber", instHierarchyBean.getInstituteProposalNumber()));
                    if( cvDev != null && cvDev.size() > 0 ) {
                        ProposalAwardHierarchyNode devNode = null;
                        for ( int devIndex = 0 ; devIndex < cvDev.size(); devIndex++ ) {
                            ProposalAwardHierarchyBean devHierarchyBean = (ProposalAwardHierarchyBean)cvDev.get(devIndex);
                            if( !hmDevProp.containsKey(devHierarchyBean.getDevelopmentProposalNumber()) ) {
                                hmDevProp.put(devHierarchyBean.getDevelopmentProposalNumber(), devHierarchyBean);
                                ProposalAwardHierarchyLinkBean devLinkBean =
                                new ProposalAwardHierarchyLinkBean( devHierarchyBean);
                                devLinkBean.setBaseType(CoeusConstants.DEV_PROP);
                                devNode = new ProposalAwardHierarchyNode(devLinkBean);
                                instTreeNode.add(devNode);
                            }
                        }
                        hmDevProp.clear();
                    }
                }
                hmInstProp.clear();
            }
            (( DefaultTreeModel )treeProposalAwardHierarchy.getModel() ).reload();
            expandAll(treeProposalAwardHierarchy,true);
        }
    }
    
    
    /** Construct the tree when Award->Proposal is selected.First loop throgh the
     *each Award number and get the correponding Subcontract number by filtering
     *award number in subcontact vector.Then get the inst prop number for each
     *award number by filtering the award number in inst prop vector.add each node to the
     *award node.Then for each inst prop number get the corresponding dev
     *prop number and these  numbers to the inst prop node.
     */
    public void buildAwardToProposalTreeHierarchy(){
        
        root.removeAllChildren();
        if(cvAward!=null && cvAward.size() > 0){
            cvAward.sort("awardNumber",true);
            ProposalAwardHierarchyNode awdTreeNode = null;
            for(int awdIndex = 0; awdIndex < cvAward.size(); awdIndex++ ){
                ProposalAwardHierarchyBean awdHierarchyBean = (ProposalAwardHierarchyBean)cvAward.get(awdIndex);
                if(!hmAward.containsKey(awdHierarchyBean.getAwardNumber()) && awdHierarchyBean.getAwardNumber() != null){
                    hmAward.put(awdHierarchyBean.getAwardNumber(), awdHierarchyBean);
                    ProposalAwardHierarchyLinkBean awdLinkBean =
                    new ProposalAwardHierarchyLinkBean( awdHierarchyBean );
                    awdLinkBean.setBaseType(CoeusConstants.AWARD);
                    awdTreeNode = new ProposalAwardHierarchyNode(awdLinkBean);
                    root.add(awdTreeNode);
                    
                    if( showSubContract ) {
                        CoeusVector cvSubContracts = cvSubContract.filter(
                        new Equals("awardNumber",awdHierarchyBean.getAwardNumber()));
                        if( cvSubContracts != null && cvSubContracts.size() > 0 ) {
                            ProposalAwardHierarchyNode subTreeNode = null;
                            for( int subIndex = 0 ; subIndex < cvSubContracts.size(); subIndex++ ) {
                                ProposalAwardHierarchyBean subHierarchyBean = (ProposalAwardHierarchyBean)cvSubContracts.get(subIndex);
                                if( !hmSubCont.containsKey(subHierarchyBean.getSubcontractNumber()) ) {
                                    hmSubCont.put(subHierarchyBean.getSubcontractNumber(), subHierarchyBean);
                                    ProposalAwardHierarchyLinkBean subLinkBean =
                                    new ProposalAwardHierarchyLinkBean( subHierarchyBean );
                                    subLinkBean.setBaseType(CoeusConstants.SUBCONTRACT);
                                    subTreeNode = new ProposalAwardHierarchyNode(subLinkBean);
                                    awdTreeNode.add(subTreeNode);
                                }
//                                hmSubCont.clear();
                            }
                            hmSubCont.clear();
                        }
                    }
                    

                    // get the Institute proposal number for the Award number.
                    //CoeusVector cvAwdInsts = cvAward.filter(new Equals("awardNumber",awdHierarchyBean.getAwardNumber()));
                  //  if(awdHierarchyBean.getInstituteProposalNumber()!= null){
                     if( cvInstProp != null && cvInstProp.size() >  0 ) {
                        //CoeusVector cvAwdInsts = cvAward.filter(new Equals("instituteProposalNumber",awdHierarchyBean.getInstituteProposalNumber()));
                         CoeusVector cvAwdInsts = cvInstProp.filter(new Equals("awardNumber",awdHierarchyBean.getAwardNumber()));
                        //CoeusVector cvAwdInsts = cvAward.filter(new Equals("awardNumber",awdHierarchyBean.getAwardNumber()));
                        if(cvAwdInsts != null && cvAwdInsts .size() > 0){
                            cvAwdInsts.sort("instituteProposalNumber");
                            ProposalAwardHierarchyNode instTreeNode = null;
                            for(int instIndex = 0; instIndex < cvAwdInsts.size(); instIndex++ ){
                                ProposalAwardHierarchyBean instHierarchyBean = (ProposalAwardHierarchyBean)cvAwdInsts.get(instIndex);
                                if(!hmInstProp.containsKey(instHierarchyBean.getInstituteProposalNumber()) && instHierarchyBean.getInstituteProposalNumber() != null){
                                    hmInstProp.put(instHierarchyBean.getInstituteProposalNumber(), instHierarchyBean);
                                    ProposalAwardHierarchyLinkBean instLinkBean =
                                    new ProposalAwardHierarchyLinkBean( instHierarchyBean );
                                    instLinkBean.setBaseType(CoeusConstants.INST_PROP);
                                    instTreeNode = new ProposalAwardHierarchyNode(instLinkBean);
                                    awdTreeNode.add(instTreeNode);
                                    
                                    
                                    CoeusVector cvDevProposal = cvDevProp.filter(
                                    new Equals("instituteProposalNumber",instHierarchyBean.getInstituteProposalNumber()));
                                    if(cvDevProposal != null && cvDevProposal.size() > 0 ){
                                        ProposalAwardHierarchyNode instDevTreeNode = null;
                                        for(int devIndex = 0; devIndex < cvDevProposal.size() ; devIndex++ ){
                                            ProposalAwardHierarchyBean devHierarchyBean = (ProposalAwardHierarchyBean)cvDevProposal.get(devIndex);
                                            if(!hmDevProp.containsKey(devHierarchyBean.getDevelopmentProposalNumber()) && devHierarchyBean.getDevelopmentProposalNumber() != null){
                                                hmDevProp.put(devHierarchyBean.getDevelopmentProposalNumber(), devHierarchyBean);
                                                ProposalAwardHierarchyLinkBean devLinkBean =
                                                new ProposalAwardHierarchyLinkBean( devHierarchyBean );
                                                devLinkBean.setBaseType(CoeusConstants.DEV_PROP);
                                                instDevTreeNode = new ProposalAwardHierarchyNode(devLinkBean);
                                                //                                             if( proposalId.equals(devLinkBean.getDevelopmentProposalNumber())){
                                                //                                                 currentDevPropNode = instDevTreeNode;
                                                //                                             }
                                                instTreeNode.add(instDevTreeNode);
                                                
                                                //COEUSQA:2653 - Add Protocols to Medusa - Start
                                                // IRB Protocols
                                                if(cvIrbProtocol != null) {
                                                    CoeusVector cvIrbProt = cvIrbProtocol.filter(
                                                            new Equals("developmentProposalNumber", devHierarchyBean.getDevelopmentProposalNumber()));
                                                    if( cvIrbProt != null && cvIrbProt.size() > 0 ) {                                                        
                                                         buildTreeForProtocol(instDevTreeNode, cvIrbProt, CoeusConstants.IRB_PROTOCOL);
                                                    }
                                                }                                                
                                                //IACUC Protocols
                                                if(cvIacucProtocol != null) {
                                                    CoeusVector cvIacucProt = cvIacucProtocol.filter(
                                                            new Equals("developmentProposalNumber", devHierarchyBean.getDevelopmentProposalNumber()));
                                                    if( cvIacucProt != null && cvIacucProt.size() > 0 ) {
                                                        buildTreeForProtocol(instDevTreeNode, cvIacucProt, CoeusConstants.IACUC_PROTOCOL);
                                                    }
                                                }
                                                //COEUSQA:2653 - End
                                            }
                                        }
                                        // hmDevProp.clear();
                                        
                                        
                                    }
                                    
                                    //COEUSQA:2653 - Start
                                    if(cvIrbProtocol != null) {
                                        CoeusVector cvIrbProt = cvIrbProtocol.filter(
                                                new Equals("instituteProposalNumber", instHierarchyBean.getInstituteProposalNumber()));
                                        if( cvIrbProt != null && cvIrbProt.size() > 0 ) {
                                             buildTreeForProtocol(instTreeNode, cvIrbProt, CoeusConstants.IRB_PROTOCOL);
                                        }
                                    }                                    
                                    //IACUC Protocols
                                    if(cvIacucProtocol != null) {
                                        CoeusVector cvIacucProt = cvIacucProtocol.filter(
                                                new Equals("instituteProposalNumber", instHierarchyBean.getInstituteProposalNumber()));
                                        if( cvIacucProt != null && cvIacucProt.size() > 0 ) {
                                             buildTreeForProtocol(instTreeNode, cvIacucProt, CoeusConstants.IACUC_PROTOCOL);
                                        }
                                    }                                    
                                    //COEUSQA:2653 - End
                                }
                            }
                            hmInstProp.clear();
                            hmDevProp.clear();
                        }
                    }
                    
                    //COEUSQA:2653 - Add Protocols to Medusa - Start
                    // IRB Protocols
                    if(cvIrbProtocol != null) {
                        CoeusVector cvIrbProtForAward = cvIrbProtocol.filter(
                                new Equals("awardNumber", awdHierarchyBean.getAwardNumber()));
                        if( cvIrbProtForAward != null && cvIrbProtForAward.size() > 0 ) {
                            buildTreeForProtocol(awdTreeNode, cvIrbProtForAward, CoeusConstants.IRB_PROTOCOL);
                        }
                    }                    
                    //IACUC Protocols
                    if(cvIacucProtocol != null) {
                        CoeusVector cvIacucProtForAward = cvIacucProtocol.filter(
                                new Equals("awardNumber", awdHierarchyBean.getAwardNumber()));
                        if( cvIacucProtForAward != null && cvIacucProtForAward.size() > 0 ) {
                            buildTreeForProtocol(awdTreeNode, cvIacucProtForAward, CoeusConstants.IACUC_PROTOCOL);
                        }
                    }                    
                    //COEUSQA:2653 - End                    
                }
            }
            hmAward.clear();
            //hmSubCont.clear();
            (( DefaultTreeModel )treeProposalAwardHierarchy.getModel() ).reload();
            expandAll(treeProposalAwardHierarchy,true);
            
        }else{
            buildProposalToAwardTreeHierarchy();
        }
    }
    
    
    /**This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        btnGrpProposalAward = new javax.swing.ButtonGroup();
        pnlSelection = new javax.swing.JPanel();
        rdbtnProposalToAward = new javax.swing.JRadioButton();
        rdbtnAwardToProposal = new javax.swing.JRadioButton();
        jcrPnHierarchy = new javax.swing.JScrollPane();
        treeProposalAwardHierarchy = new javax.swing.JTree();

        setLayout(new java.awt.BorderLayout());

        setMinimumSize(new java.awt.Dimension(190, 700));
        setPreferredSize(new java.awt.Dimension(190, 700));
        pnlSelection.setLayout(new java.awt.GridBagLayout());

        pnlSelection.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "View", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlSelection.setMinimumSize(new java.awt.Dimension(180, 76));
        rdbtnProposalToAward.setFont(CoeusFontFactory.getLabelFont());
        rdbtnProposalToAward.setText("Proposal --> Award");
        rdbtnProposalToAward.setToolTipText("");
        btnGrpProposalAward.add(rdbtnProposalToAward);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSelection.add(rdbtnProposalToAward, gridBagConstraints);

        rdbtnAwardToProposal.setFont(CoeusFontFactory.getLabelFont());
        rdbtnAwardToProposal.setText("Award --> Proposal");
        btnGrpProposalAward.add(rdbtnAwardToProposal);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSelection.add(rdbtnAwardToProposal, gridBagConstraints);

        add(pnlSelection, java.awt.BorderLayout.NORTH);

        jcrPnHierarchy.setBorder(new javax.swing.border.EtchedBorder());
        jcrPnHierarchy.setMinimumSize(new java.awt.Dimension(190, 700));
        jcrPnHierarchy.setPreferredSize(new java.awt.Dimension(190, 700));
        treeProposalAwardHierarchy.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        treeProposalAwardHierarchy.setFont(CoeusFontFactory.getNormalFont());
        treeProposalAwardHierarchy.setRootVisible(false);
        treeProposalAwardHierarchy.setShowsRootHandles(true);
        jcrPnHierarchy.setViewportView(treeProposalAwardHierarchy);

        add(jcrPnHierarchy, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.ButtonGroup btnGrpProposalAward;
    public javax.swing.JScrollPane jcrPnHierarchy;
    public javax.swing.JPanel pnlSelection;
    public javax.swing.JRadioButton rdbtnAwardToProposal;
    public javax.swing.JRadioButton rdbtnProposalToAward;
    public javax.swing.JTree treeProposalAwardHierarchy;
    // End of variables declaration//GEN-END:variables
    
    /** Getter for property proposalId.
     * @return Value of property proposalId.
     *
     */
//    public java.lang.String getProposalId() {
//        return proposalId;
//    }
//    
//    /** Setter for property proposalId.
//     * @param proposalId New value of property proposalId.
//     *
//     */
//    public void setProposalId(java.lang.String proposalId) {
//        
//    }
    /**
     * Method used to expand/ collapse all the nodes in the tree.
     * @param tree JTree whose nodes are to be expanded/ collapsed.
     * @param expand  boolean true to expand all nodes, false to collapse.
     */
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
    
    /**
     * @param tree
     * @param parent
     * @param expand
     */    
    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
    
    /** Getter for property showSubContract.
     * @return Value of property showSubContract.
     *
     */
    public boolean isShowSubContract() {
        return showSubContract;
    }
    
    /** Setter for property showSubContract.
     * @param showSubContract New value of property showSubContract.
     *
     */
    public void setShowSubContract(boolean showSubContract) {
        this.showSubContract = showSubContract;
    }
    
    /** Getter for property selectedObject.
     * @return Value of property selectedObject.
     *
     */
    public edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean getSelectedObject() {
        TreePath path = treeProposalAwardHierarchy.getSelectionPath();
        if( path!= null ) {
            ProposalAwardHierarchyNode selection = (ProposalAwardHierarchyNode)path.getLastPathComponent();
            if( selection != null ){
                selectedObject = selection.getDataObject();
                return selectedObject;
            }
        }
        return null;
    }
    
    /** Setter for property selectedObject.
     * @param selectedObject New value of property selectedObject.
     *
     */
    public void setSelectedObject(edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean selectedObject) {
        this.selectedObject = selectedObject;
    }
    
    /** Getter for property treeSelectionListener.
     * @return Value of property treeSelectionListener.
     *
     */
    public javax.swing.event.TreeSelectionListener getTreeSelectionListener() {
        return treeSelectionListener;
    }
    
    /** Setter for property treeSelectionListener.
     * @param treeSelectionListener New value of property treeSelectionListener.
     *
     */
    public void setTreeSelectionListener(javax.swing.event.TreeSelectionListener treeSelectionListener) {
        this.treeSelectionListener = treeSelectionListener;
    }
    
    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     *
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     *
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    
    /**
     * To get the TreePath from the selected Tree Node.
     * @param tree DnDJTree Instance of complete AOR data
     * @param name Name of the String to be Searched in the tree Nodes.
     *
     * @return TreePath complete path from root to the match found node.
     */
    public TreePath findByName( JTree tree, String name ) {
        
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        TreePath result = find2(tree, new TreePath(root), name);
        return result;
    }
    
    
    //supporting method for findByName method - recurcive implementation.
    /**
     * @param tree
     * @param parent
     * @param nodeName
     * @return
     */    
    private TreePath find2(JTree tree, TreePath parent, String nodeName) {
        
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node != null && node.toString().trim().startsWith(nodeName)) {
            return parent;
        }else{
            
           // Object o = node;
            if (node.getChildCount() >= 0) {
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                    TreeNode nodes = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(nodes);
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
    
    /** Getter for property htMedusa.
     * @return Value of property htMedusa.
     *
     */
    public java.util.Hashtable getHtMedusa() {
        return htMedusa;
    }    
    
    /** Setter for property htMedusa.
     * @param htMedusa New value of property htMedusa.
     *
     */
    public void setHtMedusa(java.util.Hashtable htMedusa) {
        this.htMedusa = htMedusa;
    }    
    
    /** Getter for property selectedNodeId.
     * @return Value of property selectedNodeId.
     *
     */
    public java.lang.String getSelectedNodeId() {
        return selectedNodeId;
    }    
    
    /** Setter for property selectedNodeId.
     * @param selectedNodeId New value of property selectedNodeId.
     *
     */
    public void setSelectedNodeId(java.lang.String selectedNodeId) {
        this.selectedNodeId = selectedNodeId;
        TreePath selectedPath;
        selectedPath = findByName( treeProposalAwardHierarchy,
        selectedNodeId );
        if( selectedPath == null ) {
            if(proposalAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
                proposalAwardHierarchyLinkBean.setAwardNumber(selectedNodeId);
            }
            setFormData(false);
            treeProposalAwardHierarchy.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            if( rdbtnProposalToAward.isSelected() ){
                buildProposalToAwardTreeHierarchy();
                rdbtnProposalToAward.setSelected(true);
            }else if(rdbtnAwardToProposal.isSelected()){
                buildAwardToProposalTreeHierarchy();
                rdbtnAwardToProposal.setSelected(true);
            }
            selectedPath = findByName( treeProposalAwardHierarchy,selectedNodeId );
        }
        if( selectedPath != null ) {
            treeProposalAwardHierarchy.expandPath( selectedPath );
            treeProposalAwardHierarchy.setSelectionPath( selectedPath );
            treeProposalAwardHierarchy.scrollRowToVisible(
            treeProposalAwardHierarchy.getRowForPath(selectedPath));
        }
    }    
    

    
    /** Set the Image icons for the tree and check for null.Get the Image icons for
     *Institute Proposal, development Proposal, award and subcontract
     */
    public class ProposalAwardNodeRenderer extends DefaultTreeCellRenderer{
        ImageIcon instProp,devProp,award,subContract,negotiationStatus,irbProtocol, iacucProtocol;
        
        ProposalAwardNodeRenderer(){
            super();
            
            java.net.URL instituteProposal = getClass().getClassLoader().getResource( CoeusGuiConstants.MEDUSA_INST_PROPOSAL_ICON );
            java.net.URL developmentProposal = getClass().getClassLoader().getResource( CoeusGuiConstants.MEDUSA_DEV_PROP_ICON );
            java.net.URL awards = getClass().getClassLoader().getResource( CoeusGuiConstants.MEDUSA_AWARD_ICON);
            java.net.URL subCon = getClass().getClassLoader().getResource( CoeusGuiConstants.MEDUSA_SUBCONTRACT_ICON);
            java.net.URL negoStatus = getClass().getClassLoader().getResource( CoeusGuiConstants.MEDUSA_NEGOTIATION_ICON);
            //COEUSQA:2653 - Add Protocols to Medusa - Start
            java.net.URL irbProt = getClass().getClassLoader().getResource( CoeusGuiConstants.PROTOCOL_ICON);
            java.net.URL iacucProt = getClass().getClassLoader().getResource( CoeusGuiConstants.IACUC_PROTOCOL_ICON);
            //COEUSQA:2653 - End
            
            instProp = new ImageIcon(instituteProposal);
            devProp = new ImageIcon(developmentProposal);
            award = new ImageIcon(awards);
            subContract = new ImageIcon(subCon);
            negotiationStatus = new ImageIcon(negoStatus);
            //COEUSQA:2653 - Add Protocols to Medusa - Start
            irbProtocol = new ImageIcon(irbProt);
            iacucProtocol = new ImageIcon(iacucProt);
            //COEUSQA:2653 - End
        }
        
        /**
         * @param tree
         * @param value
         * @param selected
         * @param expanded
         * @param leaf
         * @param row
         * @param hasFocus
         * @return
         */        
        public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf,int row, boolean hasFocus) {
            setFont(CoeusFontFactory.getNormalFont());
            
            JPanel pnlNegotiation = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));;
            JLabel lblNegotiaion = new JLabel();
            JLabel lblInstNumber = new JLabel();
            
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            setBackgroundNonSelectionColor((Color) UIManager.getDefaults().get("Panel.background"));
            pnlNegotiation.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
            setBackgroundSelectionColor((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
            
            if( selected ) {
                lblInstNumber.setForeground(Color.white);
                setForeground(Color.white);
            }else{
                pnlNegotiation.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
                lblInstNumber.setForeground(Color.black);
                setForeground(Color.black);
                
            }
            Object obj = ((ProposalAwardHierarchyNode)value).getDataObject();
            if( obj instanceof ProposalAwardHierarchyLinkBean ){
                ProposalAwardHierarchyLinkBean awardLinkBean = ( ProposalAwardHierarchyLinkBean ) obj;
                if( CoeusConstants.INST_PROP.equalsIgnoreCase(awardLinkBean.getBaseType())){
                    /** Filter the inst Prop vector to find any negotiation number is
                     *associated with this. IF yes add different icons else add only inst prop icons
                     */
                    CoeusVector cvNegoTiation = cvInstProp.filter(new Equals("instituteProposalNumber",awardLinkBean.getInstituteProposalNumber()));
                    if(cvNegoTiation!=null && cvNegoTiation.size() > 0){
                        ProposalAwardHierarchyBean proposalAwardHierarchyBean = (ProposalAwardHierarchyBean)cvNegoTiation.get(0);
                        if(proposalAwardHierarchyBean.isHasNegotiationNumber()){
                            
                            lblNegotiaion.setText(EMPTY_STRING);
                            lblNegotiaion.setIcon(negotiationStatus);
                            lblInstNumber.setIcon(instProp);
                            lblInstNumber.setText(proposalAwardHierarchyBean.getInstituteProposalNumber());
                            pnlNegotiation.add(lblNegotiaion);
                            pnlNegotiation.add(lblInstNumber);
                            
                            return pnlNegotiation;
                        }else {
                            setIcon(instProp);
                        }
                    }
                    
                }else if( CoeusConstants.AWARD.equalsIgnoreCase(awardLinkBean.getBaseType())){
                    setIcon(award);
                }else if( CoeusConstants.DEV_PROP.equalsIgnoreCase(awardLinkBean.getBaseType())){
                    setIcon(devProp);
                }else if( CoeusConstants.SUBCONTRACT.equalsIgnoreCase(awardLinkBean.getBaseType())){
                    setIcon(subContract);
                }
                //COEUSQA:2653 - Add Protocols to Medusa - Start
                else if( CoeusConstants.IRB_PROTOCOL.equalsIgnoreCase(awardLinkBean.getBaseType())){
                    setIcon(irbProtocol);
                } else if( CoeusConstants.IACUC_PROTOCOL.equalsIgnoreCase(awardLinkBean.getBaseType())){
                    setIcon(iacucProtocol);
                }
                //COEUSQA:2653 - End
                setText(awardLinkBean.toString());
            }else{
                setText((String)((DefaultMutableTreeNode)value).getUserObject());
            }
            return this;
        }
        
        /** Getter for property instProp.
         * @return Value of property instProp.
         *
         */
        public javax.swing.ImageIcon getInstProp() {
            return instProp;
        }
        
        /** Setter for property instProp.
         * @param instProp New value of property instProp.
         *
         */
        public void setInstProp(javax.swing.ImageIcon instProp) {
            this.instProp = instProp;
        }
        
        /** Getter for property devProp.
         * @return Value of property devProp.
         *
         */
        public javax.swing.ImageIcon getDevProp() {
            return devProp;
        }
        
        /** Setter for property devProp.
         * @param devProp New value of property devProp.
         *
         */
        public void setDevProp(javax.swing.ImageIcon devProp) {
            this.devProp = devProp;
        }
        
        /** Getter for property award.
         * @return Value of property award.
         *
         */
        public javax.swing.ImageIcon getAward() {
            return award;
        }
        
        /** Setter for property award.
         * @param award New value of property award.
         *
         */
        public void setAward(javax.swing.ImageIcon award) {
            this.award = award;
        }
        
        /** Getter for property subContract.
         * @return Value of property subContract.
         *
         */
        public javax.swing.ImageIcon getSubContract() {
            return subContract;
        }
        
        /** Setter for property subContract.
         * @param subContract New value of property subContract.
         *
         */
        public void setSubContract(javax.swing.ImageIcon subContract) {
            this.subContract = subContract;
        }
        
    }// End of class ProposalAwardNodeRenderer.........................
    
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    /**
     * Method to build Tree Hierarchy for IRB and IACUC Protocols 
     * @param parentNode 
     * @param cvProtocolDetails 
     * @param baseType 
     */
    public void buildTreeForProtocol(ProposalAwardHierarchyNode parentNode, CoeusVector cvProtocolDetails, String baseType) {
        String protocolNumber="";
        CoeusVector cvFilteredDetails;
        ProposalAwardHierarchyNode childTreeNode = null;
        hmProtocol = new HashMap();
         if(CoeusConstants.IRB_PROTOCOL.equals(baseType)) {
            cvFilteredDetails  = cvProtocolDetails.filter(
                    new NotEquals("irbProtocolNumber", null));
        }else {
            cvFilteredDetails  = cvProtocolDetails.filter(
                    new NotEquals("iacucProtocolNumber", null));
        }
        if( cvFilteredDetails != null && cvFilteredDetails.size() > 0 ) {
            for (Object cvprotoDetails : cvFilteredDetails) {
                ProposalAwardHierarchyBean subHierarchyBean = (ProposalAwardHierarchyBean)cvprotoDetails;
                if(CoeusConstants.IRB_PROTOCOL.equals(baseType)) {
                    protocolNumber = subHierarchyBean.getIrbProtocolNumber();
                } else {
                    protocolNumber = subHierarchyBean.getIacucProtocolNumber();
                }
                if( !hmProtocol.containsKey(protocolNumber) ) {
                    hmProtocol.put(protocolNumber, subHierarchyBean);
                    ProposalAwardHierarchyLinkBean protoLinkBean =
                            new ProposalAwardHierarchyLinkBean(subHierarchyBean );
                    protoLinkBean.setBaseType(baseType);
                    childTreeNode = new ProposalAwardHierarchyNode(protoLinkBean);
                    parentNode.add(childTreeNode);
                }
            }
        }                    
        hmProtocol = null;
    }
    //COEUSQA:2653 - End
}
