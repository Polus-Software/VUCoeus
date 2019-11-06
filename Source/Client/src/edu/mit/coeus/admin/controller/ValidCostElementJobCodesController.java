/*
 * ValidCostElementJobCodesController.java
 *
 * Created on December 2, 2004, 11:19 AM
 */

package edu.mit.coeus.admin.controller;

import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.dnd.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Cursor;
import java.util.Hashtable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.AbstractAction;
import java.awt.event.MouseListener;
import java.awt.Point;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionListener;
import javax.swing.SwingUtilities;

import edu.mit.coeus.admin.gui.ValidCostElementJobCodesForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.budget.bean.AppointmentsBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.budget.bean.ValidCEJobCodesBean;
import edu.mit.coeus.admin.gui.CostElementsTableForm;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import javax.swing.KeyStroke;

/**
 *
 * @author  surekhan
 */
public class ValidCostElementJobCodesController extends AdminController implements ActionListener, ListSelectionListener ,
MouseListener, TreeSelectionListener ,DragGestureListener,DragSourceListener,DropTargetListener {
    /*Instantiating the job codes form*/
    private ValidCostElementJobCodesForm validCostElementJobCodesForm; 
    
    /* Intantiating the dialog */
    private CoeusDlgWindow dlgValidJobCodesForm;
    
    /*mdiForm*/
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /*width of the dialog*/
    private static final int WIDTH = 790;
    
    /*height of the dialog*/
    private static final int HEIGHT = 500;
    
    /*title of the dialog*/
    private static final String WINDOW_TITLE = "Valid Cost Element Job Codes";
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/BudgetMaintenanceServlet";
    
    /*the boolean used to check the rights*/
    private boolean userRight;
    
    /*this vector holds the job codes and titles*/
    private CoeusVector cvJobCodesAndTitles;
    
    /*this vector holds the child job codes for the costElemennt*/
    private CoeusVector cvAllValidJobCodes;
    
    /*this vector holds the costElementList*/
    private CoeusVector cvCostElementList;
    
    /*key to get the rights from the hash table*/
    private static final int USER_RIGHT = 0;
    
    /*key to get the job codes and titles from the hash table*/
    private static final int JOB_CODES_AND_TITLES = 1;
    
    /* key to get all valid job codes from the hash table*/
    private static final int ALL_VALID_JOB_CODES = 2;
    
    /*key to get the costelement list from the hash table*/
    private static final int COST_ELEMENT_LIST = 3;
    
    /*to set the column number 0 to the job code*/
    private static final int JOB_CODE = 0;
    
    /*to set the column number 1 to the job title*/
    private static final int JOB_TITLE = 1;
    
    /*an empty string*/
    private static final String EMPTY_STRING = "";
    
    /*the table model*/
    private ValidJobCodesTableModel validJobCodesTableModel;
    
    //variables for sorting purpose. will be true if last sort was Ascending
    //else will be false.
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    
    private static final String ROOT_NAME = "Valid Cost Elements Job Codes";
    
    private JTree costElementTree;
    
    private DefaultMutableTreeNode treeNode;
    
    /*to set the cost elements initially to the tree*/
    private String costElementName;
    
    /*to set the child names initially to the tree*/
    private String childElementName;
    
    private DefaultTreeCellRenderer cellRenderer;
    
    DragSource dragSource;
    Cursor dragCursor = new Cursor(Cursor.HAND_CURSOR);
    DropTarget targetDrop;
    private CostElementTreeRenderer costElementRenderer;

    /*if any thing is modified then set it to true*/
    private boolean dataModified;
    
    /*this vector is the final vector to send to the dataBase*/
    private CoeusVector cvSaveJobCodes;
    
    /*it has a coy of all the job codes ans cost elements, and if any thing is deleted ,
     *it gets deleted from this vector
     *this vector is used for getting the position to add a job code*/
    private CoeusVector cvJobCodesCopy;
    
    /*all the added beans are stored in theis vector*/
    private CoeusVector cvAddedItem;
    
    private CoeusMessageResources coeusMessageResources;
    
    /*Do u want to save the changes*/
    private static final String CANCEL_CONFIRMATION = "frequeny_exceptionCode.1403";
    
    private static final String ROOT_MSG = "validJobCodes_exceptionCode.1501";
    
    /** Creates a new instance of ValidCostElementJobCodesController */
    public ValidCostElementJobCodesController() {
        validCostElementJobCodesForm = new ValidCostElementJobCodesForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        cvAllValidJobCodes = new CoeusVector();
        cvJobCodesCopy = new CoeusVector();
        cvJobCodesAndTitles = new CoeusVector();
        cvCostElementList = new CoeusVector();
        cvSaveJobCodes = new CoeusVector();
        cvAddedItem = new CoeusVector();
        cellRenderer = new DefaultTreeCellRenderer();
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(validCostElementJobCodesForm.tblJobCodes, DnDConstants.ACTION_MOVE, this);
        registerComponents();
        setFormData(null);
        setTableEditors();
        postInitComponents();
        createCostElementNodes();
        
    }
    
    /** Specifies the Modal window */
    private void postInitComponents(){
        
        dlgValidJobCodesForm = new CoeusDlgWindow(mdiForm);
        dlgValidJobCodesForm.setTitle(WINDOW_TITLE);
        dlgValidJobCodesForm.setResizable(false);
        dlgValidJobCodesForm.setModal(true);
        dlgValidJobCodesForm.getContentPane().add(validCostElementJobCodesForm);
        dlgValidJobCodesForm.setFont(CoeusFontFactory.getLabelFont());
        dlgValidJobCodesForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgValidJobCodesForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgValidJobCodesForm.getSize();
        dlgValidJobCodesForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgValidJobCodesForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgValidJobCodesForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgValidJobCodesForm.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
    }
    
    /*to display the form*/
    public void display() {
        if(userRight){
            setRequestFocusInThread(validCostElementJobCodesForm.btnOK);
        }else{
            setRequestFocusInThread(validCostElementJobCodesForm.btnCancel);
        }
        dlgValidJobCodesForm.show();
    }
    
    /*to set the default focus in the window*/
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    public void formatFields() {
        
    }
    
    /*returns the form*/
    public java.awt.Component getControlledUI() {
        return validCostElementJobCodesForm;
    }
    
    public Object getFormData() {
        return null;
    }
    
    /*to register all the components in the form*/
    public void registerComponents() {
        java.awt.Component[] component = {
        validCostElementJobCodesForm.btnOK,validCostElementJobCodesForm.scrPnCostElements,
        validCostElementJobCodesForm.btnCancel,
        validCostElementJobCodesForm.btnAdd,validCostElementJobCodesForm.btnDelete};
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        validCostElementJobCodesForm.setFocusTraversalPolicy(policy);
        validCostElementJobCodesForm.setFocusCycleRoot(true);
        
        
        validCostElementJobCodesForm.btnOK.addActionListener(this);
        validCostElementJobCodesForm.btnCancel.addActionListener(this);
        validCostElementJobCodesForm.btnAdd.addActionListener(this);
        validCostElementJobCodesForm.btnDelete.addActionListener(this);
        
        validJobCodesTableModel = new ValidJobCodesTableModel();
        validCostElementJobCodesForm.tblJobCodes.setModel(validJobCodesTableModel);
        validCostElementJobCodesForm.tblJobCodes.getSelectionModel().addListSelectionListener(this);
        validCostElementJobCodesForm.tblJobCodes.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        //listener for header for sorting purpose
        validCostElementJobCodesForm.tblJobCodes.getTableHeader().addMouseListener(this);
        validCostElementJobCodesForm.tblJobCodes.addMouseListener(this);
    }
    
    /*to save the data to the dataBase*/
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        saveInsertRecords();
        saveDeleteRecords();
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('T');
        if(cvSaveJobCodes != null && cvSaveJobCodes.size() > 0){
            requesterBean.setDataObject(cvSaveJobCodes);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(CONNECTION_STRING);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean!= null){
                if(!responderBean.isSuccessfulResponse()){
                    throw new CoeusException(responderBean.getMessage(), 1);
                }
           }
            
            dlgValidJobCodesForm.setVisible(false);
        }else{
            dlgValidJobCodesForm.setVisible(false);
        }
    }
    
    
    /*filter for the Insert records and add these to the final vetor*/
    private void saveInsertRecords(){
        CoeusVector cvData = new CoeusVector();
        Equals eqInsertAcType;
        eqInsertAcType = new Equals("acType",new String("I"));
        cvData = cvAllValidJobCodes.filter(eqInsertAcType);
        for(int i=0;i<cvData.size();i++){
            ValidCEJobCodesBean bean = (ValidCEJobCodesBean)cvData.get(i);
            cvSaveJobCodes.add(bean);
        }
    }
    
    /*filter for the deleted records and add to the final vector*/
    private void saveDeleteRecords(){
        
        CoeusVector cvData = new CoeusVector();
        Equals eqDeleteAcType;
        eqDeleteAcType = new Equals("acType",new String("D"));
        cvData = cvAllValidJobCodes.filter(eqDeleteAcType);
         /*check if the deleted record is again added and already exists in the added vector
          *if exists then delete it from the filetered vector and then add to the final vector*/ 
         for(int i=0;i<cvAddedItem.size();i++){
                ValidCEJobCodesBean dataBean = (ValidCEJobCodesBean)cvAddedItem.get(i);
              for(int k=0;k<cvData.size();k++){
                  ValidCEJobCodesBean bean = (ValidCEJobCodesBean)cvData.get(k);
                  if(dataBean.getCostElement().equals(bean.getCostElement()) && dataBean.getJobCode().equals(bean.getJobCode())){
                      cvData.remove(bean);
                  }
                }
                
        }
        
       for(int i=0;i<cvData.size();i++){
            ValidCEJobCodesBean bean = (ValidCEJobCodesBean)cvData.get(i);
            cvSaveJobCodes.add(bean);
        }
    }
    
    /*to set the form data*/
    public void setFormData(Object data) {
        Hashtable jobCodesData ;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('X');
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CONNECTION_STRING);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean.isSuccessfulResponse()){
            jobCodesData = (Hashtable)responderBean.getDataObject();
            if(jobCodesData!=null){
                userRight = ((Boolean)jobCodesData.get(new Integer(USER_RIGHT))).booleanValue();
                cvJobCodesAndTitles = (CoeusVector)jobCodesData.get(new Integer(JOB_CODES_AND_TITLES));
                cvAllValidJobCodes = (CoeusVector)jobCodesData.get(new Integer(ALL_VALID_JOB_CODES));
                //Bug Fix by Geo
                //throwing null pointer when there is no valid job codes.
                if(cvAllValidJobCodes==null) cvAllValidJobCodes=new CoeusVector();
                cvJobCodesCopy = (CoeusVector)cvAllValidJobCodes.clone();
                cvCostElementList = (CoeusVector)jobCodesData.get(new Integer(COST_ELEMENT_LIST));
                cvCostElementList.sort("costElement", true);
            }
        }
        
        if(!userRight){
            validCostElementJobCodesForm.btnAdd.setEnabled(false);
            validCostElementJobCodesForm.btnOK.setEnabled(false);
            validCostElementJobCodesForm.btnDelete.setEnabled(false);
            validCostElementJobCodesForm.tblJobCodes.setDragEnabled(false);
        }
        
        treeNode = new DefaultMutableTreeNode("Valid Cost Element Job Codes");
        costElementTree = new JTree(treeNode);
        costElementTree.setOpaque(false);
        costElementTree.setShowsRootHandles(true);
        costElementTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        validCostElementJobCodesForm.scrPnCostElements.setViewportView(costElementTree);
        costElementRenderer = new CostElementTreeRenderer();
        costElementTree.setCellRenderer(costElementRenderer);
        costElementTree.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),"none");
        targetDrop = new DropTarget(costElementTree,new CostElementTreeDropTarget());
    }
    
    /*to create the  nodes*/
    private void createCostElementNodes(){
        DefaultMutableTreeNode costElementNode = null;
        if(cvCostElementList != null && cvCostElementList.size()>0){
            for(int i=0 ; i < cvCostElementList.size() ; i++){
                CostElementsBean bean = (CostElementsBean)cvCostElementList.get(i);
                String costElement = bean.getCostElement();
                String description = bean.getDescription();
                costElementName = costElement + " : " +description;
                costElementNode = new DefaultMutableTreeNode(costElementName,true);
                treeNode.add(costElementNode);
                CoeusVector cvChild  = getChildJobCodeAndTitle(costElement);
                if(cvChild == null || cvChild.size() <= 0){
                    continue;
                }else{
                    String name = "";
                    for(int j = 0;j<cvChild.size();j++){
                        name = cvChild.get(j).toString();
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(name,false);
                        costElementNode.add(node);
                    }
                }
            }
            
            int childCount = costElementNode.getChildCount();
            TreePath path = costElementTree.getPathForRow(childCount);
            costElementTree.expandPath(path);
            costElementTree.setSelectionPath(new TreePath(costElementTree.getModel().getRoot()));
            costElementTree.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
       }
        
   }
    
    /*to create the job codes i.e the child nodes*/
    private CoeusVector getChildJobCodeAndTitle(String nodeName){
        CoeusVector cvChildNames = new CoeusVector();
        String name = nodeName;
        for(int j=0;j<cvAllValidJobCodes.size();j++){
            if(((ValidCEJobCodesBean)cvAllValidJobCodes.elementAt(j)).getCostElement().equals(name)){
                ValidCEJobCodesBean bean = (ValidCEJobCodesBean)cvAllValidJobCodes.get(j);
                String jobCode = bean.getJobCode();
                String jobTitle = getJobTitle(jobCode);
                String childElementName = jobCode +" : "+ jobTitle;
                cvChildNames.add(childElementName);
            }
            
        }
        
        return cvChildNames;
    }
    
    
    /*to get the title for the job code*/
    private String getJobTitle(String Code){
        String jobCode = Code;
        String jobTitle = "";
        for(int i=0;i<cvJobCodesAndTitles.size();i++){
            if(((AppointmentsBean)cvJobCodesAndTitles.elementAt(i)).getJobCode().equals(jobCode)){
                AppointmentsBean bean = (AppointmentsBean)cvJobCodesAndTitles.get(i);
                jobTitle = bean.getJobTitle();
                return jobTitle;
            }
        }
        return jobTitle;
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return true;
    }
    
    /*the actions performed on the click of the buttons*/
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        dlgValidJobCodesForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(source.equals(validCostElementJobCodesForm.btnCancel)){
            performCancelAction();
        }else if(source.equals(validCostElementJobCodesForm.btnAdd)){
            performAddAction();
        }else if(source.equals(validCostElementJobCodesForm.btnDelete)){
            performDeleteAction();
        }else if(source.equals(validCostElementJobCodesForm.btnOK)){
            try{
                if(dataModified){
                    saveFormData();
                }else{
                    dlgValidJobCodesForm.dispose();
                }
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
        dlgValidJobCodesForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
   /*the actions performed on the click of the cancel buttons*/
    private void performCancelAction(){
        try{
            if(dataModified){
                String mesg = CANCEL_CONFIRMATION;
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    saveFormData();
                } else if(selectedOption == CoeusOptionPane.OPTION_OK_CANCEL){
                    dlgValidJobCodesForm.setVisible(false);
                }
            }else{
                dlgValidJobCodesForm.setVisible(false);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    
    }
    
    /*the actions performed on the click of the add buttons*/
    private void performAddAction(){
        
        CoeusVector  cvData = new CoeusVector();
        String jobCode,jobTitle,childElementName,costElement = "",elementValue;
        int[] rows = validCostElementJobCodesForm.tblJobCodes.getSelectedRows();
        TreePath treePath = costElementTree.getSelectionPath();
        
        javax.swing.tree.MutableTreeNode selNode = (javax.swing.tree.MutableTreeNode)treePath.getLastPathComponent();
        
        if(selNode == treeNode){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ROOT_MSG));
            return;
        }
        if(selNode != null){
        String val  = (String)((DefaultMutableTreeNode)selNode).getUserObject();
        costElement = val.substring(0,val.indexOf(":")).trim();
        elementValue = val.substring(val.indexOf(":")+1).trim();
        if(selNode.isLeaf() && !selNode.getAllowsChildren()){
          selNode = (javax.swing.tree.MutableTreeNode)selNode.getParent();
            DefaultMutableTreeNode nodeVal = (DefaultMutableTreeNode)selNode;
            String nameValue = (String)nodeVal.getUserObject();
            costElement = nameValue.substring(0,nameValue.indexOf(":")).trim();
            elementValue = (String)nameValue.substring(nameValue.indexOf(":")+1).trim();
       }
        for(int index=0; index<rows.length; index++) {
            AppointmentsBean bean = (AppointmentsBean)cvJobCodesAndTitles.get(rows[index]);
            jobCode = bean.getJobCode();
            jobTitle = bean.getJobTitle();
            childElementName = jobCode +" : "+ jobTitle;
            int count = selNode.getChildCount();
            boolean treeFlag = false;
            for(int i=0;i<count;i++){
                DefaultMutableTreeNode name = (DefaultMutableTreeNode)selNode.getChildAt(i);
                String selectedValue = (String)name.getUserObject();
                if(selectedValue.equals(childElementName)){
                    CoeusOptionPane.showInfoDialog("Job Code " +jobCode+ " already exists in " +"'"+elementValue+"' Cost Element. ");
                    validCostElementJobCodesForm.tblJobCodes.removeRowSelectionInterval(rows[index], rows[index]);
                    treeFlag = true;
                }
            }
            if(!treeFlag){
                int indexPos = 0;
                ValidCEJobCodesBean jobBean = new ValidCEJobCodesBean();
                jobBean.setJobCode(jobCode);
                jobBean.setCostElement(costElement);
                jobBean.setDescription("");
                indexPos = getIndexValue(jobBean,costElement,jobCode);
                if(selNode.getChildCount() == 0){
                    indexPos = 0;
                }
                ((javax.swing.tree.DefaultTreeModel)costElementTree.getModel()).
                        insertNodeInto((javax.swing.tree.MutableTreeNode)new DefaultMutableTreeNode(
                                                        childElementName,false), selNode, indexPos);
                validCostElementJobCodesForm.tblJobCodes.removeRowSelectionInterval(rows[index],rows[index]);
                
                dataModified = true;
                jobBean.setAcType(TypeConstants.INSERT_RECORD);
                if(!getBeansForCostElement(jobBean,costElement)){
                    cvAllValidJobCodes.add(jobBean);
                    
                }
                cvJobCodesCopy.add(jobBean);
                cvAddedItem.add(jobBean);
            }
        }
      
        
    }
        costElementTree.expandPath(treePath);
    }
    
    /*to check whether the record added is already in the data base,if it doesnt exists then only set the 
     *Actype to "I"*/
    private boolean getBeansForCostElement(ValidCEJobCodesBean bean,String value){
        boolean flag = false;
        Equals eqCost;
        eqCost = new Equals("costElement",new String(value));
        CoeusVector cvAvailable = cvAllValidJobCodes.filter(eqCost);
        for(int i=0;i<cvAvailable.size();i++){
            ValidCEJobCodesBean dataBean = (ValidCEJobCodesBean)cvAvailable.get(i);
            if(dataBean.getJobCode().equals(bean.getJobCode())){
                flag = true;
            }
        }
       return flag;
    }
    
    
    /*filter the alljobcodes and get the beans for that cost element and sort them in the
     *ascending order and get the position of the bean to be added and insert the child to that
     *position in the node */
    private int getIndexValue(ValidCEJobCodesBean jobBean ,String costValue,String code){
        int indexPostn = 0;
        String value = costValue;
        String jobCode = code;
        Equals eqCost;
        eqCost = new Equals("costElement",new String(value));
        CoeusVector cvDataVector = cvJobCodesCopy.filter(eqCost);
        cvDataVector.add(jobBean);
        cvDataVector.sort("jobCode",true);
        for(int j = 0;j<cvDataVector.size();j++){
            ValidCEJobCodesBean dataBeans = (ValidCEJobCodesBean)cvDataVector.get(j);
            if(dataBeans.getJobCode().equals(jobCode)){
                return indexPostn = j;
            }
        }
        return indexPostn;
    }
    
    
    /*to set the selection in the tree*/
    public TreePath findByName(JTree tree, String name) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        TreePath result = find2(tree, new TreePath(root), name);
        return result;
    }
    
    /*to set the selection in the tree*/
    private TreePath find2(JTree tree, TreePath parent, String nodeName) {
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node != null && node.toString().trim().startsWith(nodeName)) {
            return parent;
        }else{

            Object o = node;
            if (node.getChildCount() >= 0) {
                for (java.util.Enumeration e=node.children(); e.hasMoreElements(); ) {
                    TreeNode n = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(n);
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
    
    /*the action performed on the click of the delete button*/
    private void performDeleteAction(){
            int[] selRows = costElementTree.getSelectionRows();
            
        for (int index=selRows.length; index>0; index--) {
            TreePath treePath = costElementTree.getSelectionPath();
            int row = costElementTree.getRowForPath(treePath);
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)selNode.getParent();
            if(selNode.isRoot()){
                CoeusOptionPane.showInfoDialog("Root of valid cost element job codes \ncannot be deleted.");
                return;
            }
            String costElementCode = (String)selNode.getUserObject();
            String costValue = (String)treeNode.getUserObject();
            String selectedValue = (String)selNode.getUserObject();
            
            if(selNode.isLeaf() && selNode.getAllowsChildren()){
                String value = selectedValue.substring(selectedValue.indexOf(":")+1).trim();
                CoeusOptionPane.showInfoDialog("There is no job codes assigned for " +"'"+value+"'. ");
                return;
            }else if(selNode.isLeaf() && !selNode.getAllowsChildren()){
                 String JobCodeTitle = (String)selNode.getUserObject();
                    String jobCode = JobCodeTitle.substring(0,JobCodeTitle.indexOf(":")).trim();
                    String costElement = costValue.substring(0,costValue.indexOf(":")).trim();
                    String value = costValue.substring(costValue.indexOf(":")+1).trim();
                int option = CoeusOptionPane.showQuestionDialog("Are you sure you want to delete Job Code " +jobCode+  " \nfrom " +"'"+value+"'?" ,CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
                if(option ==CoeusOptionPane.SELECTION_YES){
                    DefaultTreeModel model = (DefaultTreeModel)costElementTree.getModel();
                    TreePath path = costElementTree.getSelectionPath();
                    model.removeNodeFromParent((javax.swing.tree.MutableTreeNode)selNode);
                    dataModified = true;
                    if(((DefaultMutableTreeNode)costElementTree.getPathForRow(row).getLastPathComponent()).getAllowsChildren())
                        costElementTree.setSelectionPath(treePath.getParentPath());
                    else
                        costElementTree.setSelectionRow(row);
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)selNode.getNextLeaf();
                    Equals eqCostElement;
                    eqCostElement = new Equals("costElement",new String(costElement));
                    CoeusVector cvData = cvAllValidJobCodes.filter(eqCostElement);
                    for(int i=0;i<cvData.size();i++){
                        ValidCEJobCodesBean bean = (ValidCEJobCodesBean)cvData.get(i);
                        if(bean.getCostElement().equals(costElement) && bean.getJobCode().equals(jobCode)){
                              bean.setAcType(TypeConstants.DELETE_RECORD);
                              cvJobCodesCopy.remove(bean);
                        }
                    }
                    
                   
                }
                return;
                
            }else if(selNode.getChildCount() > 0){
                String value = selectedValue.substring(selectedValue.indexOf(":")+1).trim();
                int option = CoeusOptionPane.showQuestionDialog("Are you sure you want to delete all Job Codes \nfrom Cost Element " +"'"+value+"'?"  ,CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
                if(option == CoeusOptionPane.SELECTION_YES){
                    selNode.removeAllChildren();
                    dataModified = true;
                    DefaultTreeModel treeModel = ( DefaultTreeModel )costElementTree.getModel();
                    treeModel.reload();
                    Equals eqCostElement;
                    String codeValue = costElementCode.substring(0,costElementCode.indexOf(":")).trim();
                    eqCostElement = new Equals("costElement",new String(codeValue));
                    CoeusVector cvData = cvAllValidJobCodes.filter(eqCostElement);
                    for(int i=0;i<cvData.size();i++){
                        ValidCEJobCodesBean bean = (ValidCEJobCodesBean)cvData.get(i);
                        bean.setAcType(TypeConstants.DELETE_RECORD);
                        cvJobCodesCopy.remove(bean);
                        
                    }
                     if(((DefaultMutableTreeNode)costElementTree.getPathForRow(row).getLastPathComponent()).getAllowsChildren()){
                         costElementTree.setSelectionPath( new TreePath(selNode.getPath()));
                     }
                    return;
                }
           }
            
        }
    }

    /*to set the column sizes and the table properties*/
    private void setTableEditors(){
        JTableHeader tableHeader = validCostElementJobCodesForm.tblJobCodes.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        validCostElementJobCodesForm.tblJobCodes.setBackground(bgColor);
        validCostElementJobCodesForm.tblJobCodes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        validCostElementJobCodesForm.tblJobCodes.setRowHeight(22);
        validCostElementJobCodesForm.tblJobCodes.setShowHorizontalLines(true);
        validCostElementJobCodesForm.tblJobCodes.setShowVerticalLines(true);
        validCostElementJobCodesForm.tblJobCodes.setOpaque(true);
        validCostElementJobCodesForm.tblJobCodes.setRowSelectionAllowed(true);
        
        TableColumn column = validCostElementJobCodesForm.tblJobCodes.getColumnModel().getColumn(JOB_CODE);
        column.setPreferredWidth(61);
        column.setResizable(true);
        
        column = validCostElementJobCodesForm.tblJobCodes.getColumnModel().getColumn(JOB_TITLE);
        column.setPreferredWidth(290);
        column.setResizable(true);
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
    }    
    
    /*action on double click of the row and on the clickof the header*/
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        String jobCode;
        CoeusVector cvTemp = new CoeusVector();
        CoeusVector cvTableData = new CoeusVector();
        Object source = mouseEvent.getSource();
        int clickCount = mouseEvent.getClickCount();
        if(clickCount == 2 && !source.equals(validCostElementJobCodesForm.tblJobCodes.getTableHeader())){
            int selectedRow = validCostElementJobCodesForm.tblJobCodes.getSelectedRow();
            if(selectedRow == -1 || selectedRow < 0 )return;
            AppointmentsBean bean = (AppointmentsBean)cvJobCodesAndTitles.get(selectedRow);
            jobCode = bean.getJobCode();
            Equals eqJobFilter;
            eqJobFilter = new Equals("jobCode",new String(jobCode));
            cvTemp = cvAllValidJobCodes.filter(eqJobFilter);
           for(int i=0 ; i<cvTemp.size();i++){
                ValidCEJobCodesBean validCEJobCodesBean = (ValidCEJobCodesBean)cvTemp.get(i);
                cvTableData.add(validCEJobCodesBean);
            }
            if(cvTableData == null || cvTableData.size()<=0){
                CoeusOptionPane.showInfoDialog("There is no Cost Elements with \nJob Code "+bean.getJobCode()+ " assigned to. ");
                return;
            }
            CostElementsTableForm costElementsTableForm  = new CostElementsTableForm(cvTableData,jobCode);
            String name = costElementsTableForm.searchString();
            if(name != null){
            TreePath path = findByName(costElementTree, name);
            costElementTree.expandPath(path);
            costElementTree.setSelectionPath(path);
	    costElementTree.scrollRowToVisible(
                costElementTree.getRowForPath(path));
            }
        }
        if(source.equals(validCostElementJobCodesForm.tblJobCodes.getTableHeader())){
            dlgValidJobCodesForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            int size = validCostElementJobCodesForm.tblJobCodes.getRowCount();
            Point clickedPoint = mouseEvent.getPoint();
            int xPosition = (int)clickedPoint.getX();
            int columnIndex = validCostElementJobCodesForm.tblJobCodes.getColumnModel().getColumnIndexAtX(xPosition);
            switch (columnIndex) {
                case JOB_CODE:
                    if(sortCodeAsc) {
                        //Code already sorted in Ascending order. Sort now in Descending order.
                        cvJobCodesAndTitles.sort("jobCode", false);
                        sortCodeAsc = false;
                    }else {
                        //Code already sorted in Descending order. Sort now in Ascending order.
                        cvJobCodesAndTitles.sort("jobCode", true);
                        sortCodeAsc = true;
                    }
                    break;
                case JOB_TITLE:
                    if(sortDescAsc){
                        cvJobCodesAndTitles.sort("jobTitle",false);
                        sortDescAsc = false;
                    }else {
                        cvJobCodesAndTitles.sort("jobTitle",true);
                        sortDescAsc = true;
                    }
                    break;
            }//End Switch
            validJobCodesTableModel.fireTableDataChanged();
            dlgValidJobCodesForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
      
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }
    
    public void treeCollapsed(javax.swing.event.TreeExpansionEvent event) {
    }
    
    public void treeExpanded(javax.swing.event.TreeExpansionEvent event) {
    }
    
    public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
        if(userRight){
        int[] rows = validCostElementJobCodesForm.tblJobCodes.getSelectedRows();
        if(rows.length > 0) {
            CoeusVector  cvData = new CoeusVector();
            for(int index=0; index<rows.length; index++) {
               cvData.add((AppointmentsBean)cvJobCodesAndTitles.get(rows[index]));
            }
            TransferableUserRoleData transferableData = new TransferableUserRoleData(cvData);
            dragSource.startDrag(dragGestureEvent, dragCursor,transferableData, this);
            
        }
        }
        
    }
    
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }
    
    public void dragEnter(DragSourceDragEvent dsde) {
    }
    
    public void dragExit(DragSourceEvent dse) {
    }
    
    public void dragOver(DragSourceDragEvent dsde) {
    }
    
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }
    
    public void dragEnter(DropTargetDragEvent dtde) {
    }
    
    public void dragExit(DropTargetEvent dte) {
    }
    
    public void dragOver(DropTargetDragEvent dtde) {
    }
    
    public void drop(DropTargetDropEvent dtde) {
     
    }
    
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }
    
    public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
    }
    
    /*
     *It's an inner class which specifies the table model
     */
    public class ValidJobCodesTableModel extends AbstractTableModel{
        
        
        // represents the column names of the columns of table
        private String[] colName = {"Job Code","Job Title"};
        
        // represents the column class of the fields of table
        private Class[] colClass = {String.class,String.class};
        
        
        /*returns true if the cell is editable else returns false*/
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        /**
         * To get the column count
         * @return int
         **/
        public int getColumnCount() {
            return colName.length;
        }
        
        
        /**
         * To get the column count
         * @param col int
         * @return String
         **/
        public String getColumnName(int col){
            return colName[col];
        }
        
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        /**
         * To set the  data in the table
         * @param cvTableData CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvJobCodesAndTitles){
            cvJobCodesAndTitles = cvJobCodesAndTitles;
            fireTableDataChanged();
        }
        
        
        /**
         * To get the row count
         * @return int
         **/
        public int getRowCount() {
            if(cvJobCodesAndTitles==null){
                return 0;
            }else{
                return cvJobCodesAndTitles.size();
            }
            
        }
        
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int row, int col) {
            AppointmentsBean appointmentsBean = (AppointmentsBean)cvJobCodesAndTitles.get(row);
            switch(col){
                case JOB_CODE:
                    return appointmentsBean.getJobCode();
                case JOB_TITLE:
                    return appointmentsBean.getJobTitle();
            }
            return EMPTY_STRING;
        }
        
       
    }
    
    /*drop target in the tree*/
     public class CostElementTreeDropTarget implements DropTargetListener {
         
         public void dragEnter(DropTargetDragEvent dtde) {
         }
         
         public void dragExit(DropTargetEvent dte) {
         }
         
         public void dragOver(DropTargetDragEvent dtde) {
             Point loc = dtde.getLocation();
             DropTargetContext dtc = dtde.getDropTargetContext();
             JTree tree = (JTree)dtc.getComponent();
             TreePath destinationPath = tree.getPathForLocation(loc.x, loc.y);
             if ( destinationPath != null ) {
                 DefaultMutableTreeNode newParent =
                 (DefaultMutableTreeNode)destinationPath.getLastPathComponent();
                 tree.setSelectionPath( destinationPath );
             }
         }
         
         public void drop(DropTargetDropEvent dtde) {
             
             CoeusVector cvChild = new CoeusVector();
             String jobCode;
             DropTargetContext dtc = dtde.getDropTargetContext();
             JTree tree = (JTree)dtc.getComponent();
             TreePath parentpath = tree.getSelectionPath();
             if(parentpath == null) {
                 dtde.getDropTargetContext().dropComplete(false);
             }
            
             DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)parentpath.getLastPathComponent();
             
             if(selNode == treeNode){
                 CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ROOT_MSG));
                 return;
             }
            if (selNode.isLeaf() && !selNode.getAllowsChildren()) {
                 selNode =(DefaultMutableTreeNode) selNode.getParent();
             }
             try{
                 Transferable transferable = dtde.getTransferable();
                 dtde.acceptDrop(dtde.getDropAction());
                 AppointmentsBean bean;
                 CoeusVector cvTreeData = (CoeusVector)transferable.getTransferData(TransferableUserRoleData.MULTIPLE_USERS_FLAVOR);
                 for(int i=0;i<cvTreeData.size();i++){
                     bean = (AppointmentsBean)cvTreeData.get(i);
                     jobCode = bean.getJobCode();
                     String jobTitle = bean.getJobTitle();
                     String childElementName = jobCode +" : "+ jobTitle;
                     
                     int count = selNode.getChildCount();
                     boolean treeFlag = false;
                     for(int j=0;j<count;j++){
                         DefaultMutableTreeNode name = (DefaultMutableTreeNode)selNode.getChildAt(j);
                         String selectedValue = (String)name.getUserObject();
                         String val  = (String)((DefaultMutableTreeNode)selNode).getUserObject();
                         String costElement = val.substring(val.indexOf(":")+1).trim();
                         if(selectedValue.equals(childElementName)){
                             CoeusOptionPane.showInfoDialog("Job Code " +jobCode+ " already exists in "+"'"+costElement+"' Cost Element. ");
                             treeFlag = true;
                         }
                     }
                     if(!treeFlag){
                     int index = 0;
                     ValidCEJobCodesBean dataBean = new ValidCEJobCodesBean();
                     String val  = (String)((DefaultMutableTreeNode)selNode).getUserObject();
                     String costElement = val.substring(0,val.indexOf(":")).trim();
                     String desc = val.substring(val.indexOf(":")+1).trim();
                     dataBean.setJobCode(jobCode);
                     dataBean.setCostElement(costElement);
                     dataBean.setDescription(desc);
                     index = getIndexValue(dataBean, costElement, jobCode);
                   ((javax.swing.tree.DefaultTreeModel)costElementTree.getModel()).insertNodeInto((javax.swing.tree.MutableTreeNode)new DefaultMutableTreeNode(childElementName,false), selNode,index);
                     costElementTree.expandPath(parentpath);
                     dataModified = true;
                     dataBean.setAcType(TypeConstants.INSERT_RECORD);
                     if(!getBeansForCostElement(dataBean,costElement)){
                         cvAllValidJobCodes.add(dataBean);
                     }
                     cvJobCodesCopy.add(dataBean);
                     cvAddedItem.add(dataBean);
                     validCostElementJobCodesForm.tblJobCodes.removeRowSelectionInterval(i,i);
                     }
                 }
             dtde.dropComplete(true);
             int selRows[] = validCostElementJobCodesForm.tblJobCodes.getSelectedRows();
             for(int i = 0;i< selRows.length;i++){
                 validCostElementJobCodesForm.tblJobCodes.removeRowSelectionInterval(selRows[i], selRows[i]);
             }
             return;
             }catch ( UnsupportedFlavorException ex ){
                 dtde.getDropTargetContext().dropComplete(false);
             }catch (Exception e) {
                 e.printStackTrace();
                 dtde.getDropTargetContext().dropComplete(false);
                 dtde.rejectDrop();
             }
         }
         
         public void dropActionChanged(DropTargetDragEvent dtde) {
         }
         
     }
     
     
    
  public class CostElementTreeRenderer extends DefaultTreeCellRenderer{
      Icon folderNode , hierarchyRoot , costElement;
      
      public CostElementTreeRenderer(){
          folderNode =  new ImageIcon(getClass().getClassLoader().getResource("images/folderNode.gif"));
          hierarchyRoot = new ImageIcon(getClass().getClassLoader().getResource("images/hierarchyRoot.gif"));
          costElement = new ImageIcon(getClass().getClassLoader().getResource("images/jobcode.gif"));
      }
      
      public Component getTreeCellRendererComponent(JTree tree,Object value, boolean sel, boolean expanded,boolean leaf,int row,boolean hasFocus) {
           super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row,hasFocus);
           DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)value;
           setBackgroundNonSelectionColor((java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background"));
           if(selNode.getAllowsChildren() && !selNode.isRoot()) {
                setIcon(folderNode);
                setOpenIcon(folderNode);
                setClosedIcon(folderNode);
            }else if(!selNode.getAllowsChildren()) {
                setIcon(costElement);
            }else if(selNode.isRoot()){
                setIcon(hierarchyRoot);
            }
           return this;
        }
      
  }
   
}
