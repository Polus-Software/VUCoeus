/**
 * @(#)ProposalHierarchyContoller.java  August 16, 2005, 12:48 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.budget.controller.BudgetPersonController;
import edu.mit.coeus.budget.gui.ProposalBudgetHierarchyDetailsForm;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.propdev.bean.ProposalBudgetBean;
import edu.mit.coeus.propdev.bean.ProposalBudgetVersionBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalHierarchyBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 *
 * @author  nadhgj
 */
public class ProposalHierarchyContoller extends Controller
implements TreeSelectionListener /*, MouseListener*/{
    private JTree proposalHierarchy = null;
    private ProposalHierarchyForm proposalHierarchyForm= new ProposalHierarchyForm();
    private ProposalHierarchyBean proposalHierarchyBean = null;
    private String module = "";
    private String selProposal;
    private int selVersion = -1;
    
    private static final String HIERARCHY_SERVLET = "/ProposalHierarchyServlet";
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL;
    private static final String BUDGET_SERVLET = "/BudgetMaintenanceServlet";
    private BudgetPersonController budgetPersonController = null;
    private MedusaProposalDetailForm propDetailsForm = null;
    private MedusaInvestigatorUnitForm investigatorForm = null;
    private ProposalAwardHierarchyLinkBean linkBean = null;
    //    private ProposalBudgetHierarchyDetailsForm budgetdeatilsForm = new ProposalBudgetHierarchyDetailsForm();;
    private ProposalBudgetHierarchyDetailsForm budgetdeatilsForm = null;
    
    private static final char CHECK_VIEW_RIGHT = 'L';
    
    private String unitNumber = null;
    
    private ProposalHierarchyRenderar renderer = null;
    
    private boolean setFormCalled = false;
   //Added by tarique for showing legends start
    private PropHierarchyShowLegendForm propHierarchyShowLegendForm = null;
    //Added by tarique for showing legends end
    //Modified for showing budget status by tarique
    private HashMap hmProposalData;
       
    /** Creates a new instance of ProposalHierarchyContoller */
    public ProposalHierarchyContoller(ProposalHierarchyBean bean) throws Exception {
        //this(bean,null);
        this.proposalHierarchyBean = bean;
        budgetdeatilsForm = new ProposalBudgetHierarchyDetailsForm();
    }
    
    /** Creates a new instance of ProposalHierarchyContoller */
    /*public ProposalHierarchyContoller(ProposalHierarchyBean bean,String module) throws Exception{
        this.module = module;
        this.proposalHierarchyBean = bean;
        //initComponents();
        //registerComponents();
        //formatFields();
        //postInitComponents();
        //setFormData(null);
    }*/
    
    /*private void initComponents() {
        buildTree();
        proposalHierarchy.putClientProperty("Jtree.lineStyle", "Angled");
        proposalHierarchyForm = new ProposalHierarchyForm();
        proposalHierarchy.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        proposalHierarchyForm.scrProposalHirarchy.setViewportView(proposalHierarchy);
        ProposalHierarchyRenderar renderar = new ProposalHierarchyRenderar();
        proposalHierarchy.setCellRenderer(renderar);
        proposalHierarchy.setSelectionRow(0);
        proposalHierarchy.getInputMap().put(javax.swing.KeyStroke.getKeyStroke("SPACE"), "none");
    }*/
    
    private void postInitComponents() {
        if(CoeusGuiConstants.BUDGET_MODULE.equals(module))
            showBudgetPersonsDialog();
        else {
            proposalHierarchyForm.pnlDetails.add(budgetdeatilsForm);
        }
    }
    
    
    private void buildTree() {
        DefaultMutableTreeNode node = null;
        if(proposalHierarchyBean != null) {
            node = new DefaultMutableTreeNode(proposalHierarchyBean.getParentProposalNumber());
            CoeusVector cvChildData = proposalHierarchyBean.getProposalData();
            if(cvChildData != null && cvChildData.size()>0) {
                for(int index=0;index<cvChildData.size();index++) {
                    ProposalBudgetBean proposalBudgetBean = (ProposalBudgetBean)cvChildData.get(index);
                    node.add(getChilds(proposalBudgetBean));
                }
            }
        }
        proposalHierarchy = new JTree(node);
    }
    
    private DefaultMutableTreeNode getChilds(ProposalBudgetBean proposalBean) {
        ProposalHierarchyBudgetNode node = new ProposalHierarchyBudgetNode(proposalBean);
        CoeusVector cvData = proposalBean.getBudgetVersions();
        cvData.sort("versionNumber");
        if(cvData !=null && cvData.size()>0) {
            for(int index=0;index<cvData.size();index++) {
                node.add(new ProposalHierarchyBudgetVersionNode(cvData.get(index)));
            }
        }
        return node;
    }
    
    private void showBudgetPersonsDialog() {
        setBudgetpersons();
        final CoeusDlgWindow dlgPersonBudget = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm());
        dlgPersonBudget.getContentPane().add(proposalHierarchyForm);
        dlgPersonBudget.setTitle("Budget Persons");
        dlgPersonBudget.setFont(CoeusFontFactory.getLabelFont());
        dlgPersonBudget.setModal(true);
        dlgPersonBudget.setResizable(false);
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        //dlgPersonBudget.setSize(805,280);//645,280
        dlgPersonBudget.setSize(805,350);
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dlgSize = dlgPersonBudget.getSize();
        dlgPersonBudget.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgPersonBudget.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgPersonBudget.dispose();
                return;
            }
        });
        dlgPersonBudget.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgPersonBudget.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                dlgPersonBudget.dispose();
                return;
            }
        });
        
        dlgPersonBudget.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                //setWindowFocus();
            }
        });
        budgetPersonController.setDialogRef(dlgPersonBudget);
        budgetPersonController.setFunctionType(edu.mit.coeus.utils.TypeConstants.DISPLAY_MODE);
        dlgPersonBudget.setVisible(true);
    }
    
    private void setBudgetpersons() {
        //       budgetPersonController = new BudgetPersonController(CoeusGuiConstants.getMDIForm(),true,new edu.mit.coeus.budget.bean.BudgetInfoBean());
        //       proposalHierarchyForm.pnlDetails.add(budgetPersonController.getControlledUI());
        TreePath root  = new TreePath(proposalHierarchy.getModel().getRoot());
        proposalHierarchy.expandPath(root);
        proposalHierarchy.expandRow(1);
        proposalHierarchy.setSelectionRow(2);
        proposalHierarchy.scrollRowToVisible(2);
    }
    
    private Object getBudgetPersonDetails(String proposalNumber,int version,char functionType) throws CoeusClientException {
        RequesterBean request = new RequesterBean();
        CoeusVector data = null;
        CoeusVector cvData = new CoeusVector();
        cvData.addElement(proposalNumber);
        cvData.addElement(new Integer(version));
        request.setDataObjects(cvData);
        request.setFunctionType(functionType);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo+HIERARCHY_SERVLET, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return response.getDataObject();
        }else {
            throw new CoeusClientException(response.getMessage());
        }
    }
    
    public static void main(String[] a) {
        //        ProposalHierarchyBean bean = new ProposalHierarchyBean();
        //        bean.setParentProposalNumber("001");
        //        CoeusVector cv = new CoeusVector();
        //        ProposalBudgetBean pbvb = new ProposalBudgetBean();
        //        pbvb.setProposalNumber("002");
        //        CoeusVector cv1 = new CoeusVector();
        //        ProposalBudgetVersionBean b = new ProposalBudgetVersionBean();
        //        b.setVersionNumber(2);
        //        b.setVersionFlag(true);
        //        ProposalBudgetVersionBean b1 = new ProposalBudgetVersionBean();
        //        b1.setVersionNumber(3);
        //        cv1.addElement(b);
        //        cv1.addElement(b1);
        //        pbvb.setBudgetVersions(cv1);
        //        ProposalBudgetBean pbvb1 = new ProposalBudgetBean();
        //        pbvb1.setProposalNumber("003");
        //        pbvb1.setBudgetVersions(cv1);
        //        cv.addElement(pbvb);
        //        cv.addElement(pbvb1);
        //        bean.setProposalData(cv);
        //        javax.swing.JDialog dlg = new javax.swing.JDialog();
        //        ProposalHierarchyContoller con = new ProposalHierarchyContoller(bean);
        //        dlg.getContentPane().add(con.getControlledUI());
        //        dlg.pack();
        //        dlg.show();
        
    }
    
    public void display() {
    }
    
    public void formatFields() {
        proposalHierarchy.putClientProperty("Jtree.lineStyle", "Angled");
        proposalHierarchy.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        
        renderer = null;
        renderer = new ProposalHierarchyRenderar();
        proposalHierarchy.setCellRenderer(renderer);
        
        proposalHierarchy.setSelectionRow(0);
        proposalHierarchy.getInputMap().put(javax.swing.KeyStroke.getKeyStroke("SPACE"), "none");
        
        proposalHierarchyForm.scrProposalHirarchy.setViewportView(proposalHierarchy);
    }
    
    public java.awt.Component getControlledUI() {
        return proposalHierarchyForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        proposalHierarchy.addTreeSelectionListener(this);
        //proposalHierarchy.addMouseListener(this);
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        unitNumber = (String)data;
        buildTree();
        registerComponents();
        formatFields();
        postInitComponents();
        showHierarchyProposalDetails(proposalHierarchy.getModel().getRoot().toString());
        setFormCalled = true;
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    public void valueChanged(javax.swing.event.TreeSelectionEvent selectionEvent) {
        TreePath selTreePath = selectionEvent.getNewLeadSelectionPath();
        selProposal = "";
        String unitNo = "";
        selVersion = -1;
        if(selTreePath == null)
            return;
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selTreePath.getLastPathComponent();
        Object object = selectedNode.getUserObject();
        try {
            if(object instanceof ProposalBudgetBean) {
                selProposal = ((ProposalBudgetBean)object).getProposalNumber();
                unitNo = ((ProposalBudgetBean)object).getUnitNumber();
                if(checkForViewRight(selProposal , unitNo, false)){
                    showHierarchyProposalDetails(selProposal);
                }else{
                    CoeusOptionPane.showErrorDialog("You do not have rights to view Proposal "+selProposal);
                }
                
            }else if(object instanceof ProposalBudgetVersionBean) {
                selVersion = ((ProposalBudgetVersionBean)object).getVersionNumber();
                ProposalBudgetBean userObj = (ProposalBudgetBean)((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject();
                selProposal = userObj.getProposalNumber();
                unitNo = ((ProposalBudgetVersionBean)object).getUnitNumber();
                if(checkForViewRight(selProposal , unitNo, false)){
                    showBudgetVersionDetails(selProposal,selVersion);
                }else{
                    CoeusOptionPane.showErrorDialog("You do not have rights to view Budget for Proposal "+selProposal);
                }
            }else if(selectedNode.isRoot()){
                if(checkForViewRight(selectedNode.toString() , unitNumber , true)){
                    showHierarchyProposalDetails(selectedNode.toString());
                }else{
                    CoeusOptionPane.showErrorDialog("You do not have rights to view Proposal "+selectedNode.toString());
                }
            }
        }catch (CoeusException ex) {
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }catch (CoeusClientException ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }
    
    private void showHierarchyProposalDetails(String proposalNumber) throws CoeusException{
        if(!CoeusGuiConstants.BUDGET_MODULE.equals(module) || module == null) {
            if(propDetailsForm == null) {
                propDetailsForm = new MedusaProposalDetailForm(CoeusGuiConstants.getMDIForm());
                investigatorForm = new MedusaInvestigatorUnitForm();
                linkBean = new ProposalAwardHierarchyLinkBean();
                linkBean.setBaseType(CoeusConstants.DEV_PROP);
                proposalHierarchyForm.pnlDetails.add(propDetailsForm, java.awt.BorderLayout.NORTH);
                //Added by tarique for showing legend start
                proposalHierarchyForm.pnlDetails.add(investigatorForm, java.awt.BorderLayout.WEST);
                propHierarchyShowLegendForm = new PropHierarchyShowLegendForm();
                propHierarchyShowLegendForm.pnlShowLegend.setVisible(false);
                proposalHierarchyForm.pnlDetails.add(propHierarchyShowLegendForm,java.awt.BorderLayout.SOUTH);
                //Added by tarique for showing legend end
            }
            propDetailsForm.setVisible(true);
            investigatorForm.setVisible(true);
            budgetdeatilsForm.setVisible(false);
            hmProposalData = getPropDetails(proposalNumber,'F');
            propDetailsForm.showValues(
            (ProposalDevelopmentFormBean)hmProposalData.get(ProposalDevelopmentFormBean.class));
            investigatorForm.setDataValues((Vector)hmProposalData.get(ProposalInvestigatorFormBean.class),linkBean);
            investigatorForm.setFormData();
        }
    }
    
    private void showBudgetVersionDetails(String selProposal,int selVersion) throws CoeusClientException, CoeusException {
        if(CoeusGuiConstants.BUDGET_MODULE.equals(module)) {
            Object objData = getBudgetPersonDetails(selProposal, selVersion,'G');
            if(budgetPersonController == null) {
                budgetPersonController = new BudgetPersonController(CoeusGuiConstants.getMDIForm(), (edu.mit.coeus.budget.bean.BudgetInfoBean)(
                (HashMap)objData).get(edu.mit.coeus.budget.bean.BudgetInfoBean.class));
                proposalHierarchyForm.pnlDetails.add(budgetPersonController.getControlledUI());
            }
            budgetPersonController.refresh((CoeusVector)((HashMap)objData).get(BudgetPersonsBean.class),selProposal,selVersion);
        }else if(!CoeusGuiConstants.BUDGET_MODULE.equals(module) || module == null){
            propDetailsForm.setVisible(false);
            investigatorForm.setVisible(false);
            budgetdeatilsForm.setVisible(true);
            Object objData = getBudgetPersonDetails(selProposal, selVersion,'J');
            //Added by tarique to show budget status in budget details start
            HashMap hmProposalData = getPropDetails(selProposal,'F');
            budgetdeatilsForm.setBudgetStatus(((ProposalDevelopmentFormBean)
                hmProposalData.get(ProposalDevelopmentFormBean.class)).getBudgetStatus());
            hmProposalData = null;
            //Added by tarique to show budget status in budget details end
            budgetdeatilsForm.setFormData((java.util.HashMap)objData);
        }
    }
    
    
    private HashMap getPropDetails(String propNumber,char funType) throws CoeusException {
        RequesterBean request = new RequesterBean();
        request.setId(propNumber);
        request.setFunctionType(funType);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo+HIERARCHY_SERVLET, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return (HashMap)response.getDataObject();
        }else {
            throw new CoeusException(response.getMessage());
        }
    }
    
    /*public void mouseClicked(java.awt.event.MouseEvent e) {
        int xPoint = e.getX();
        int yPoint = e.getY();
        TreePath selPath = proposalHierarchy.getPathForLocation(e.getX(), e.getY());
        if(selPath == null){
            return ;
        }
        Object obj = selPath.getLastPathComponent();
     
        if(obj instanceof ProposalHierarchyBudgetNode){
             Object nodeInfo =( (ProposalHierarchyBudgetNode)obj ).getUserObject();
             if(nodeInfo != null){
                 ProposalBudgetBean proposalBudgetBean = (ProposalBudgetBean)nodeInfo;
                 checkForViewRight();
             }
        }else if(obj instanceof ProposalHierarchyBudgetVersionNode){
            Object nodeInfo =( (ProposalHierarchyBudgetVersionNode)obj ).getUserObject();
            if(nodeInfo != null){
                ProposalBudgetVersionBean propBudgetVersionBean
                                            = (ProposalBudgetVersionBean)nodeInfo;
                checkForViewRight();
            }
        }
     
    }
     
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
     
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
     
    public void mousePressed(java.awt.event.MouseEvent e) {
    }
     
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }*/
    
    private boolean checkForViewRight(String proposalNo , String unitNo, boolean isRoot) throws CoeusClientException{
        boolean hasRights = false;
        String proposalNumber;
        Vector vecFnParams = new Vector();
        
        if(!setFormCalled){
            hasRights = true;
        }else{
            RequesterBean request = new RequesterBean();
            //CoeusVector data = null;
            CoeusVector cvData = new CoeusVector();
            cvData.addElement(proposalNo);
            cvData.addElement(unitNumber);
            cvData.addElement(new Boolean(isRoot));
            request.setDataObject(cvData);
            request.setFunctionType(CHECK_VIEW_RIGHT);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo+HIERARCHY_SERVLET, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                Boolean right = (Boolean)response.getDataObject();
                hasRights = right.booleanValue();
            }else {
                throw new CoeusClientException(response.getMessage());
            }
         }
        return hasRights;
    }
    
    /**
     * Getter for property module.
     * @return Value of property module.
     */
    public java.lang.String getModule() {
        return module;
    }
    
    /**
     * Setter for property module.
     * @param module New value of property module.
     */
    public void setModule(java.lang.String module) {
        this.module = module;
    }
    
    /**
     * Getter for property dataObject.
     * @return Value of property dataObject.
     */
    public edu.mit.coeus.utils.CoeusVector getDataObject() {
        TreePath selTreePath = proposalHierarchy.getSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selTreePath.getLastPathComponent();
        CoeusVector cvData = new CoeusVector();
        Object object = selectedNode.getUserObject();
        if(object instanceof ProposalBudgetVersionBean){
            selVersion = ((ProposalBudgetVersionBean)object).getVersionNumber();
            ProposalBudgetBean userObj = (ProposalBudgetBean)((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject();
            selProposal = userObj.getProposalNumber();
            cvData.addElement(new Integer(selVersion));
            cvData.addElement(selProposal);
        }
        return cvData;
    }
    
    public void cleanUp(){
        proposalHierarchy.removeTreeSelectionListener(this);
        proposalHierarchy = null;
        proposalHierarchyForm = null;
        proposalHierarchyBean = null;
        budgetPersonController = null;
        propDetailsForm = null;
        investigatorForm = null;
        linkBean = null;
        budgetdeatilsForm = null;
        renderer = null;
    }
    
    /**
     * Getter for property proposalHierarchyBean.
     * @return Value of property proposalHierarchyBean.
     */
    public edu.mit.coeus.propdev.bean.ProposalHierarchyBean getProposalHierarchyBean() {
        return proposalHierarchyBean;
    }
    
    /**
     * Setter for property proposalHierarchyBean.
     * @param proposalHierarchyBean New value of property proposalHierarchyBean.
     */
    public void setProposalHierarchyBean(edu.mit.coeus.propdev.bean.ProposalHierarchyBean proposalHierarchyBean) {
        this.proposalHierarchyBean = proposalHierarchyBean;
    }
    
    public class ProposalHierarchyRenderar extends DefaultTreeCellRenderer {
        ImageIcon rootIcon,proposalIcon,budgetIcon,
        budgetCompleteIcon,budgetIncompleteIcon,
        versionFinalIcon,versionProvisionalIcon, syncIcon, notSyncIcon,subProjectIcon;
        JPanel pnlIcons = null;
        JPanel pnlText = null;
        JLabel lblBudget = null;
        JLabel lblVersion = null;
        JLabel lblText = null;
        JLabel lblSync = null;
        //JLabel lblNotSync = null;
        /** Creates a new instance of ProposalHierarchyRenderar */
        public ProposalHierarchyRenderar() {
            super();
            initComponets();
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
        
        private void initComponets() {
            pnlIcons = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
            pnlText = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
            lblBudget = new JLabel("");
            lblVersion = new JLabel("");
            lblText = new JLabel("");
            lblSync = new JLabel("");
            rootIcon = new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.PARENT_PROP_HIE_ICON ));
            proposalIcon = new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.CHILD_PROP_HIE_ICON ));
            budgetIcon = new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.BUDGET_ICON ));
            budgetCompleteIcon = new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.COMPLETE_ICON ));
            budgetIncompleteIcon = new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.INCOMPLETE_ICON ));
            // versionProvisionalIcon = new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.PROVISION_ICON ));
            //Added by tarique start
            syncIcon = new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.GREEN ));
            notSyncIcon = new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.RED ));
            versionFinalIcon = new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.BUDGET_FINAL_ICON ));
            subProjectIcon = new ImageIcon(getClass().getClassLoader().getResource( CoeusGuiConstants.AWARDS_ICON ));
            //Added by tarique end
            pnlIcons.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            
        }
        
        public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded,
        boolean leaf,int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            setBackgroundNonSelectionColor((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            setBackgroundSelectionColor((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
            pnlText.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
            if( sel ) {
                lblText.setForeground(java.awt.Color.white);
                pnlText.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
                setForeground(java.awt.Color.white);
            }else{
                pnlText.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                lblText.setForeground(java.awt.Color.black);
                setForeground(java.awt.Color.black);
                
            }
            lblVersion.setText(" ");
            Object obj = node.getUserObject();
            if(node.isRoot()){
                setIcon(rootIcon);
            }else if(obj instanceof ProposalBudgetVersionBean) {            //Icon renderer for the budget Node
                ProposalBudgetVersionBean versionBean = (ProposalBudgetVersionBean)obj;
                if(versionBean!= null && !versionBean.equals("")){
                    if(versionBean.getChildType().equals("Sub Project")){
                        lblBudget.setIcon(subProjectIcon);
                    }else{
                        lblBudget.setIcon(budgetIcon);
                    }
                }else{
                    lblBudget.setIcon(budgetIcon);
                }
                
                lblText.setText(versionBean.toString());
                if(versionBean.isVersionFlag()) {
                    lblVersion.setIcon(versionFinalIcon);
                }else{
                    lblVersion.setIcon(null);
                }
                //Added by tarique start
                lblSync.setIcon((versionBean.isBudgetSynced() == true) ? syncIcon : notSyncIcon);
                //Added by tarique end
                pnlText.add(lblText);
                pnlIcons.add(lblBudget);
                pnlIcons.add(lblVersion);
                //Added by tarique start
                pnlIcons.add(lblSync);
                //Added by tarique end
                pnlIcons.add(pnlText);
                return pnlIcons;
            }else {                                                     //Icon renderer for the proposal Node
                ProposalBudgetBean bean = (ProposalBudgetBean)obj;
                lblBudget.setIcon(proposalIcon);
                lblText.setText(bean.toString());
                if(bean.isBudgetStatus()) {
                    lblVersion.setIcon(budgetCompleteIcon);
                }else{
                    lblVersion.setIcon(budgetIncompleteIcon);
                }
                lblSync.setIcon((bean.isProposalSynced() == true) ? syncIcon : notSyncIcon);
                pnlText.add(lblText);
                pnlIcons.add(lblBudget);
                pnlIcons.add(lblVersion);
                //Added by tarique start
                pnlIcons.add(lblSync);
                //Added by tarique end
                pnlIcons.add(pnlText);
                return pnlIcons;
            }
            return this;
        }
    }
}
