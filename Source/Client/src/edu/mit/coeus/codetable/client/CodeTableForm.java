/**
 * @(#)CodeTableForm.java 1.0 06/13/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


/* PMD check performed, and commented unused imports and variables on 09-NOV-2010
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeus.codetable.client;

import edu.mit.coeus.bean.CoeusTypeBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.IrbWindowConstants;
import edu.mit.coeus.utils.query.Equals;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
//import java.beans.*;

//import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
//import edu.mit.coeus.gui.CoeusAboutForm ;
//import edu.mit.coeus.gui.CoeusDlgWindow ;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.codetable.bean.*;
import edu.mit.coeus.brokers.* ;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
//coeusqa 2296 start
import java.text.*;
//coeusqa 2296 end


//javax.swing.JApplet
public class CodeTableForm extends JComponent 
{   
//    private javax.swing.JButton jButtonAdd;
    private javax.swing.JTable tblCodeTable;
//    private javax.swing.JButton btnDelete;
//    private javax.swing.JButton btnClose;
    private javax.swing.JTree treeCodeTable ;
    private javax.swing.JScrollPane scrlpnlForTableCtrl;
    private javax.swing.JScrollPane scrlpnlForListCtrl;
//    private javax.swing.JScrollPane jScrollPane1;
//    private javax.swing.JPanel pnlForBtn;
    private javax.swing.JSplitPane splitpnlMain;
//    private javax.swing.JButton btnSave;
   
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    
    ExtendedDefaultTableModel codeTableModel ; // this model will hold the actual data
    // keeping two CodeTableColumnModels will fix the problem as the remove column will delete the actual column 
    // from the tablecolumnmodel if u associate the tablecolumnmodel to a Jtable. If u dont want to
    // associate then header values dont show up
    DefaultTableColumnModel codeTableColumnModel ; // this model will hold the structure if the table
    DefaultTableColumnModel displayCodeTableColumnModel ; // this model will hold just the visual columns structure
//    TableStructureBean tableStructureBean = new TableStructureBean();
        TableStructureBean tableStructureBean = null;
    TableStructureBean tableStructureBean_temp = new TableStructureBean();
    AllCodeTablesBean allCodeTablesBean = new AllCodeTablesBean();
    HashMap hashAllCodeTableStructure = new HashMap();
    Vector vecOriginalData = null;
    Vector vecDeletedRows = new Vector();    
    Vector vecModifiedData = new Vector();
    //holds list of procedure category codes
    Vector vecProcCategoryType=new Vector();
    //holds list of look up arguments
    CoeusVector cvLookupArguments = new CoeusVector();
    //how many columns are going to display on the screen
    int numColumnsDisplay;    
    //contains real ColumnNames,those columns are displayed 
    //on screen with displayname.
    String columnNames[];
       
    DataBean accDataBean = new DataBean();

    //Main MDI Form
    private CoeusAppletMDIForm parent;
  
    /* This is used to know whether data is modified or add  */
    private boolean saveRequired = false;   
    
    private boolean askedChangeList = false;
 
    private TreePath selectedNode;
    
    private TreePath perSelectedNode;
    
    //holds the largerst primay
    private int currentMaxId ;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
//    private TableSorter sorter ;
    private TableSorterCodeTable sorter ;
    
    //hold rule function names for a combobox, the data is from sp, not from xml file
    private  HashMap hmFunctionNames = new HashMap();
    //hold login user id
    String   userId;

    boolean userHasRights = false;
    DefaultMutableTreeNode top = null ;
    //Added for COEUSDEV-86 : Questionnaire for a Submission -Start
    private HashMap hmModulesSubModules = new HashMap(); 
    private static final String SUB_MODULE_CODE = "SUB_MODULE_CODE";
    private static final String NONE_ITEM_IN_COMBOBOX = "None";
    //COEUSDEV-86 : End
    //Added for IACUC-Start
    private static final int IRB_MODULE = 7;
    private static final int IACUC_MODULE = 9;
    //Added for IACUC-End
    //coeusqa 2296 start
    private DateUtils dateUtils;
    private SimpleDateFormat simpleDateFormat;
    private static final String DATE_SEPARATERS = ":/.,|-";
//    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    //coeusqa 2296 end
    //Variables for Procedure category custom data Screen.
    //Added with COEUSQA-2667:User interface for setting up question details for procedure categories
    private static final String PROC_CAT_CUSTOM_DATA = "OSP$AC_PROC_CAT_CUSTOM_DATA";
    //Added for COEUSQA-2540 Membership role related changes-start
    private static final String IRB_MEMBERSHIP_ROLE = "osp$MEMBERSHIP_ROLE";
    private static final String IACUC_MEMBERSHIP_ROLE = "osp$AC_MEMBERSHIP_ROLE";
    //Added for COEUSQA-2540 Membership role related changes-End
    //Added for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release _start
    private static final String IACUC_SCHEDULE_ACT_ITEM = "osp$AC_SCHEDULE_ACT_ITEM_TYPE";
    private static final String IRB_SCHEDULE_ACT_ITEM = "osp$SCHEDULE_ACT_ITEM_TYPE";
    //Added for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release _end
    private static final String PROCEDURES = "Procedures";
    private static final String PROCEDURE_CAT_CODE_COL = "PROCEDURE_CATEGORY_CODE";
    private static final String LOOKUP_WINDOW_COL = "LOOKUP_WINDOW";
    private static final String HAS_LOOKUP_COL = "HAS_LOOKUP";
    private static final String LOOKUP_ARG_COL = "LOOKUP_ARGUMENT";
    //COEUSQA-2667:End
    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
    private static final String LOCATION_TYPE_CODE_COL = "LOCATION_TYPE_CODE";
    private static final String LOCATION_NAME = "Location Name";
    private static final String LOCATION_NAME_TABLE_NAME = "OSP$AC_LOCATION_NAME";
    Vector vecLocationType=null;
    //COEUSQA:3005 - End
    
    //COEUSQA:1691 - Front End Configurations to CoeusLite Pages - Start
    private final String ARRA_MESSAGE_PROPERTY = "OSP$ARRA_MESSAGES";
    private final String BUDGET_MESSAGE_PROPERTY = "OSP$BUDGET_MESSAGES";
    private final String IRB_PROTO_MESSAGE_PROPERTY = "OSP$IRB_PROTOCOL_MESSAGES";
    private final String AC_PROTO_MESSAGE_PROPERTY = "OSP$AC_PROTOCOL_MESSAGES";
    private final String NEGOTIATION_MESSAGE_PROPERTY = "OSP$NEGOTIATION_MESSAGES";
    private final String PROPOSAL_MESSAGE_PROPERTY = "OSP$PROPOSAL_MESSAGES";
    private final String SUB_CONTRACT_MESSAGE_PROPERTY = "OSP$SUBCONTRACT_MESSAGES";
    private final String COI_MESSAGE_PROPERTY = "OSP$COI_MESSAGES";
    //COEUSQA:1691 - End
    // Added for COEUSQA-3471 : IACUC CoeusLite Review Type Determination Values   - Start
    private static final String REVIEW_TYPE_DETERMINATION = "Review Type Determination";
    private Vector vecProtocolReviewType = new Vector();
    // Added for COEUSQA-3471 : IACUC CoeusLite Review Type Determination Values   - End
    public CodeTableForm() 
    {
                
    }
    
    public JComponent getCodeTableComponent(CoeusAppletMDIForm parent)
    {
       this.parent = parent;
       
       coeusMessageResources = CoeusMessageResources.getInstance();
       allCodeTablesBean =  getAllCodeTableBeanData();
        if (allCodeTablesBean != null)
        { 
            hashAllCodeTableStructure  = allCodeTablesBean.getHashAllCodeTableStructure();
            initComponents();  
//            if (!userHasRights)
//                tblCodeTable.setEnabled(false); 
        } 
    
       
       return this ;
    }
    
   private void initComponents() 
    {
//        lstCodeTable = new javax.swing.JList();

        //coeusqa 2296  start
        dateUtils = new DateUtils();
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        //coeusqa 2296  end


          top = new DefaultMutableTreeNode("Code Tables");
            createNodes(top);
            treeCodeTable = new JTree(top);
            treeCodeTable.getSelectionModel().setSelectionMode
            (TreeSelectionModel.SINGLE_TREE_SELECTION);
            
            treeCodeTable.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                   treeCodeTable.getLastSelectedPathComponent();

                if (node == null) return;

               
                if (node.isLeaf() && node.getLevel() > 1 ) 
                {
                    tableStructureBean_temp = (TableStructureBean)node.getUserObject();
                    tableNameChanged();
                } 
                else
                {
                    askedChangeList = false;
                    tableStructureBean_temp = null;
                    tableNameChanged();
                }
               }
        });
        
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(new ImageIcon(getClass().getClassLoader().getResource("images/CodetableGroup.gif"))) ;
        renderer.setClosedIcon(new ImageIcon(getClass().getClassLoader().getResource("images/CodetableGroup.gif"))) ;
        renderer.setLeafIcon(new ImageIcon(getClass().getClassLoader().getResource("images/CodeTable.gif"))) ;
        treeCodeTable.setCellRenderer(renderer);

//        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
//        renderer.setBackgroundNonSelectionColor(Color.white);
//        Icon icn =  new ImageIcon(getClass().getClassLoader().getResource("images/CodeTable.gif"));
//        renderer.setOpenIcon(icn);
//        renderer.setClosedIcon(icn);
//        renderer.setLeafIcon(icn);
//        treeCodeTable.setCellRenderer(renderer);
        
        scrlpnlForListCtrl = new javax.swing.JScrollPane( treeCodeTable);
        scrlpnlForListCtrl.setMinimumSize(new java.awt.Dimension(230, 460)) ;
        scrlpnlForListCtrl.setPreferredSize(new java.awt.Dimension(230, 460));
        
//        sorter = new TableSorter() ;
        sorter = new TableSorterCodeTable() ;
       
        tblCodeTable = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblCodeTable); 
                
        tblCodeTable.setRowHeight(22) ;
        tblCodeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;     
        tblCodeTable.getTableHeader().setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));
 
        // The following code will make sure the table gets resized properly
        
        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;
        //pnlTable.setMinimumSize(new java.awt.Dimension(550, 460)) ;
        //pnlTable.setPreferredSize(new java.awt.Dimension(550, 460)) ;
        
        pnlTable.add(tblCodeTable.getTableHeader(), BorderLayout.NORTH) ; 
        pnlTable.add(tblCodeTable , BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;
       
        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);
        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(550, 460)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(550, 460)) ;
               
        splitpnlMain = new javax.swing.JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrlpnlForListCtrl, scrlpnlForTableCtrl );
        splitpnlMain.setDividerSize(2);
        splitpnlMain.setOneTouchExpandable(true) ;
        splitpnlMain.setBackground(Color.white) ;
        
                
        //splitpnlMain.setBorder(BorderFactory.createLineBorder(Color.black));
               
       java.awt.GridBagConstraints gridBagConstraints;
       
       setLayout(new java.awt.GridBagLayout());
       setPreferredSize( new java.awt.Dimension( 500, 500 ));
       setBorder(new javax.swing.border.EtchedBorder());
       
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0 ;
        gridBagConstraints.weighty = 1.0 ;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
       
        
        add(splitpnlMain, gridBagConstraints);
        

    }
    
   
   private void createNodes(DefaultMutableTreeNode top)
    {
        DefaultMutableTreeNode groupNames = null;
        DefaultMutableTreeNode tableNames = null;
//        Vector vecGroupName = allCodeTablesBean.getGroupNameList() ;
        HashMap hashGroupName = allCodeTablesBean.getHashAllGroupNameList() ;
        // keep a seperate int hashIndex which should be used  
        // with tableNames = new DefaultMutableTreeNode( new Integer(hashIndex))
        // this hashIndex is the index of the tablestructurebean
        // in the hashAllCodeTableStructure
               
        for (int outerLoop=0 ;outerLoop < hashGroupName.size() ; outerLoop++ )
        {
//            String groupName = hashGroupName.get(outerLoop).toString() ;
            String groupName = (String)hashGroupName.get(new Integer(outerLoop));
            groupNames = new DefaultMutableTreeNode(groupName) ;
            top.add(groupNames) ;
            
        }
        if(hashAllCodeTableStructure != null)
        {
            String gName;
            DefaultMutableTreeNode gTempNode;
            int tableListLength = hashAllCodeTableStructure.size();
            for (int index = 0; index < tableListLength; index++)
            {
               TableStructureBean tempTableStructureBean = (TableStructureBean) hashAllCodeTableStructure.get(new Integer(index)) ;
               gName = tempTableStructureBean.getGroupName() ;
               tableNames = new DefaultMutableTreeNode(tempTableStructureBean);
               for(gTempNode = (DefaultMutableTreeNode)top.getFirstChild(); gTempNode != null ;gTempNode = (DefaultMutableTreeNode)top.getChildAfter(gTempNode))
               {
                   if (gTempNode.getUserObject().equals(gName))
                   {
                       gTempNode.add(tableNames);
                       break;
                   }
               }

            }
        }
     }

    
    /**
     * This method is used to determine whether the data is to be saved or not.
     * @returns boolean true if any modifications have been done and are not
     * saved, else it returns false.
     */
    
    public boolean isSaveRequired()
    {
        return saveRequired;
    }
    
    
    //check cell editor will have to be assigned for the new row.
    public void addBtnClicked(java.awt.event.ActionEvent evt) 
    {
        Object[]  oneRow = new Object[] {" "};
        int colIdx = 0 ;  //this need not be zero always.. u can get the column index of the primary key column using the Column Bean
//        int newPKey = 0 ;
        
        //in case there is no code table was selected.
        if (tableStructureBean == null) return;
      
        // Added for COEUSQA-3471 : IACUC CoeusLite Review Type Determination Values   - Start        
        if(tableStructureBean.getActualName().equals("OSP$AC_REVIEW_TYPE_DETER")){
            TypeSelectionLookUp selectionLookUp = new TypeSelectionLookUp("Select a Review Type",ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            CoeusVector cvReviewTypes = new CoeusVector();
            Vector vecReviewType = getCodeTableData("GET_AC_PROTOCOL_REVIEW_TYPES");
            CoeusTypeBean coeusTypeBean = null;
            int rowCount = tblCodeTable.getRowCount();
              
            
            if(vecReviewType != null && !vecReviewType.isEmpty()){
                for(int index = 0; index<vecReviewType.size();index++){
                    HashMap hmReviewType = (HashMap)vecReviewType.get(index);
                    int reviewTypeCode = Integer.parseInt(hmReviewType.get("PROTOCOL_REVIEW_TYPE_CODE").toString());
                    String reviewDescription = hmReviewType.get("DESCRIPTION").toString();
                    coeusTypeBean = new CoeusTypeBean();
                    coeusTypeBean.setTypeCode(reviewTypeCode);
                    coeusTypeBean.setTypeDescription(reviewDescription);
                    cvReviewTypes.add(coeusTypeBean);
                }
            }
            
            for(int rowIndex=0;rowIndex<rowCount;rowIndex++){
                if (codeTableModel.getValueAt(rowIndex,0) != null) {
                    codeTableModel.getValueAt(rowIndex,0);
                    int reviewTypeCode = Integer.parseInt(codeTableModel.getValueAt(rowIndex,0).toString());
                    if(cvReviewTypes != null && !cvReviewTypes.isEmpty()){
                        CoeusVector cvFilteredTypes = cvReviewTypes.filter(new Equals("typeCode",reviewTypeCode));
                        if(cvFilteredTypes != null && !cvFilteredTypes.isEmpty()){
                            cvReviewTypes.remove(cvFilteredTypes.get(0));
                        }
                    }
                }
            }
            if(cvReviewTypes != null && !cvReviewTypes.isEmpty()){
                selectionLookUp.setFormData(cvReviewTypes);
                try {
                    selectionLookUp.display();
                    CoeusVector cvSelectedTypes = selectionLookUp.getSelectedTypes();
                    if(cvSelectedTypes != null && !cvSelectedTypes.isEmpty()){
                        for(Object reviewType : cvSelectedTypes){
                            CoeusTypeBean reviewTypeDetails = (CoeusTypeBean)reviewType;
                            Vector vecNewData = new Vector();
                            vecNewData.add(0,""+reviewTypeDetails.getTypeCode());
                            vecNewData.add(1,reviewTypeDetails.getTypeDescription());
                            vecNewData.add(2,"");
                            vecNewData.add(3,userId);
                            vecNewData.add(4,"");
                            vecNewData.add(5,"");
                            vecNewData.add(6,TypeConstants.INSERT_RECORD);
                            ((DefaultTableModel)tblCodeTable.getModel()).addRow(vecNewData);
                            ((DefaultTableModel)tblCodeTable.getModel()).fireTableDataChanged();
                            saveRequired = true;
                        }
                        int lastRow = tblCodeTable.getRowCount();
                        tblCodeTable.setRowSelectionInterval(lastRow-1,lastRow-1);
                    }
                } catch (CoeusException ex) {
                    ex.printStackTrace();
                }
            }else{
                CoeusOptionPane.showWarningDialog("There is no protocol review type exists.");
            }
            
        }
        // Added for COEUSQA-3471 : IACUC CoeusLite Review Type Determination Values   - End
         // get the primary key value if primary key is auto generated
        else if (tableStructureBean.isIdAutoGenerated())
         {  
            currentMaxId++;
            colIdx = tableStructureBean.getPrimaryKeyIndex(0) ;
            sorter.insertRow(sorter.getRowCount(),oneRow);
            
 
            // set primary key
            codeTableModel.setValueAt(new Integer(currentMaxId), codeTableModel.getRowCount()-1,  colIdx);
            // set the duplicate index interface aw_...
            codeTableModel.setValueAt(new Integer(currentMaxId), codeTableModel.getRowCount()-1, tableStructureBean.getDuplicateIndex(0) );
            // set AC_TYPE
            codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
            saveRequired = true;
            // set the user name 
            codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureBean.getUserIndex() );
 
            tblCodeTable.changeSelection(codeTableModel.getRowCount()-1,  colIdx+1, false, false) ;
            tblCodeTable.editCellAt(codeTableModel.getRowCount()-1,  colIdx+1) ;
            tblCodeTable.requestFocus();
          }    
         else
         {    
            // table oosp$person_editable_columns add function is a spacial case
            if (tableStructureBean.getActualName().equalsIgnoreCase("osp$person_editable_columns"))
            {
                Vector vecToChild = new Vector() ;
                FormPersonEditColumn frmPersonEditColumn = new FormPersonEditColumn(parent,"Choose Editable Column", true, allCodeTablesBean) ;
                // those itens already in selected shud not show up in the dialog
                if (tblCodeTable.getRowCount()>0)
                {    
                   for (int rowCount= 0; rowCount< tblCodeTable.getRowCount(); rowCount++)
                   {
                        vecToChild.add(rowCount, tblCodeTable.getValueAt(rowCount, 0 )) ;
                   }    
                    
                    frmPersonEditColumn.setItemsToRemove(vecToChild) ;
                }    
                frmPersonEditColumn.showForm() ;
                HashMap hashValues = frmPersonEditColumn.getSelectedRow() ;
                // change one row to the values obtd from the dialog
                if (hashValues != null) // do it only if a row was selected in the table(on the dialog)
                {    
//                    oneRow = new Object[]{ hashValues.get("COLUMN_NAME").toString(), "" , hashValues.get("DATA_TYPE").toString(), hashValues.get("DATA_LENGTH").toString(), new ComboBoxBean("N", "No")} ;
                   
                    oneRow = new Object[]{ hashValues.get("COLUMN_NAME").toString()} ;
                    sorter.insertRow(sorter.getRowCount(),oneRow);
        
                    codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, tableStructureBean.getDuplicateIndex(0) );
                    // set AC_TYPE
                    codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
                    saveRequired = true;
                    // set the user name 
                    codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureBean.getUserIndex() );
                                     
                    tblCodeTable.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
                    tblCodeTable.editCellAt(codeTableModel.getRowCount()-1,  0) ;
                    tblCodeTable.requestFocus();
                }//end if    
            }else
            {
          
            // table osp$eps_prop_columns_to_alter add function is different from the rest
            if (tableStructureBean.getActualName().equalsIgnoreCase("osp$eps_prop_columns_to_alter"))
            {
                Vector vecToChild = new Vector() ;
                FormProposalDevEditColumn frmProposalDevEditColumn = new FormProposalDevEditColumn(parent,"Choose Editable Column", true, allCodeTablesBean) ;
                // those itens already in selected shud not show up in the dialog
                if (tblCodeTable.getRowCount()>0)
                {    
                   for (int rowCount= 0; rowCount< tblCodeTable.getRowCount(); rowCount++)
                   {
                        vecToChild.add(rowCount, tblCodeTable.getValueAt(rowCount, 0 )) ;
                   }    
                    
                    frmProposalDevEditColumn.setItemsToRemove(vecToChild) ;
                }    
                frmProposalDevEditColumn.showForm() ;
                HashMap hashValues = frmProposalDevEditColumn.getSelectedRow() ;
                // change one row to the values obtd from the dialog
                if (hashValues != null) // do it only if a row was selected in the table(on the dialog)
                {    
                    oneRow = new Object[]{ hashValues.get("COLUMN_NAME").toString(), "" , hashValues.get("DATA_TYPE").toString(), hashValues.get("DATA_LENGTH").toString(), new ComboBoxBean("N", "No")} ;
                 
                    sorter.insertRow(sorter.getRowCount(),oneRow);
        
                    codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, tableStructureBean.getDuplicateIndex(0) );
                    // set AC_TYPE
                    codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
                    saveRequired = true;
                    // set the user name 
                    codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureBean.getUserIndex() );
                                     
                    tblCodeTable.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
                    tblCodeTable.editCellAt(codeTableModel.getRowCount()-1,  0) ;
                    tblCodeTable.requestFocus();
                }//end if    
            } else 
                // Added for coeus4.3 enhancements - starts
                //For Protocol Amendments/Renewal module
                    //Modified for IACUC changes - Start
//                if (tableStructureBean.getActualName().equalsIgnoreCase("OSP$PROTOCOL_MODULES"){
                    if (tableStructureBean.getActualName().equalsIgnoreCase("OSP$PROTOCOL_MODULES")||tableStructureBean.getActualName().equalsIgnoreCase("OSP$AC_PROTOCOL_MODULES")) {
                    int moduleCode=0;
                    if(tableStructureBean.getActualName().equalsIgnoreCase("OSP$PROTOCOL_MODULES")){
                        moduleCode = IRB_MODULE;
                    }else if(tableStructureBean.getActualName().equalsIgnoreCase("OSP$AC_PROTOCOL_MODULES")){
                        moduleCode = IACUC_MODULE;
                    }
                    Vector vecToChild = new Vector() ;
                    //FormEditModules formEditModules = new FormEditModules(parent, allCodeTablesBean) ;
                    FormEditModules formEditModules = new FormEditModules(parent, allCodeTablesBean,moduleCode) ;
                    //IACUC Changes - End
                if (tblCodeTable.getRowCount()>0)
                {    
                   for (int rowCount= 0; rowCount< tblCodeTable.getRowCount(); rowCount++)
                   {
                        // Modified for Comparing the Description of the Protocol Modules while Adding Protocol Modules
                        // vecToChild.add(rowCount, tblCodeTable.getValueAt(rowCount, 0 )) ;
                        vecToChild.add(rowCount, tblCodeTable.getValueAt(rowCount, 1 )) ;
                   }    
                    
                    formEditModules.setItemsToRemove(vecToChild) ;
                }    
                formEditModules.showForm() ;
                HashMap hashValues = formEditModules.getSelectedRow() ;
                // change one row to the values obtd from the dialog
                if (hashValues != null) // do it only if a row was selected in the table(on the dialog)
                {    
//                    oneRow = new Object[]{ hashValues.get("COLUMN_NAME").toString(), "" , hashValues.get("DATA_TYPE").toString(), hashValues.get("DATA_LENGTH").toString(), new ComboBoxBean("N", "No")} ;
                   
                    oneRow = new Object[]{ hashValues.get("PROTOCOL MODULE CODE").toString(), hashValues.get("DESCRIPTION").toString()} ;
                    sorter.insertRow(sorter.getRowCount(),oneRow);
        
                    codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, tableStructureBean.getDuplicateIndex(0) );                    // set AC_TYPE
                    
                    codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
                    saveRequired = true;
                    // set the user name 
                    codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureBean.getUserIndex() );
                                     
                    tblCodeTable.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
                    tblCodeTable.editCellAt(codeTableModel.getRowCount()-1,  0) ;
                    tblCodeTable.requestFocus();
                }//end if                   
                // Added for coeus4.3 enhancements - ends    
            } else if(tableStructureBean.getActualName().equalsIgnoreCase("osp$notif_action_type")) {
                Vector vecToChild = new Vector();
                FormActionModule frmActionModule = new FormActionModule(parent,"Choose Action", true, allCodeTablesBean);
                if(tblCodeTable.getRowCount() > 0) {
                    for(int rowCount = 0; rowCount < tblCodeTable.getRowCount(); rowCount++) {
                        vecToChild.add(rowCount, tblCodeTable.getValueAt(rowCount, 0));
                    }
                    frmActionModule.setItemsToRemove(vecToChild);
                }
                frmActionModule.showForm();
                HashMap hmSelectedRow = frmActionModule.getSelectedRow();
                if (hmSelectedRow != null) // do it only if a row was selected in the table(on the dialog)
                {    
//                    oneRow = new Object[]{ hashValues.get("COLUMN_NAME").toString(), "" , hashValues.get("DATA_TYPE").toString(), hashValues.get("DATA_LENGTH").toString(), new ComboBoxBean("N", "No")} ;
                   
                    oneRow = new Object[]{ hmSelectedRow.get("ACTIONID").toString(),hmSelectedRow.get("DESCRIPTION").toString(), hmSelectedRow.get("MODULE").toString(), "", "", "", "", "", "", "", hmSelectedRow.get("MODULEID").toString()};
                    sorter.insertRow(sorter.getRowCount(),oneRow);
        
                    codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, tableStructureBean.getDuplicateIndex(0) );                    // set AC_TYPE
                    
                    codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
                    saveRequired = true;
                    // set the user name 
                    codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureBean.getUserIndex() );
                                     
                    tblCodeTable.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
                    tblCodeTable.editCellAt(codeTableModel.getRowCount()-1,  0) ;
                    tblCodeTable.requestFocus();
                }//end if                   
            } else 
                // Added for coeus4.2.3 enhancements - starts
                //For Tuition Fee Auto Calculation
                if (tableStructureBean.getActualName().equalsIgnoreCase("OSP$COST_ELEMENT_PERIOD")) {
                Vector vecToChild = new Vector() ;
                FormCostElementPeriod formCostElement = new FormCostElementPeriod(parent, allCodeTablesBean) ;
                if (tblCodeTable.getRowCount()>0)
                {    
                   for (int rowCount= 0; rowCount< tblCodeTable.getRowCount(); rowCount++)
                   {
                        vecToChild.add(rowCount, tblCodeTable.getValueAt(rowCount, 0 )) ;
                   }    
                    
                    formCostElement.setItemsToRemove(vecToChild);
                }    
                formCostElement.showForm() ;
                HashMap hashValues = formCostElement.getSelectedRow() ;
                // change one row to the values obtd from the dialog
                if (hashValues != null) // do it only if a row was selected in the table(on the dialog)
                {    
                    oneRow = new Object[]{ hashValues.get("Cost Element").toString(), hashValues.get("DESCRIPTION").toString()} ;
                    sorter.insertRow(sorter.getRowCount(),oneRow);
        
                    codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, tableStructureBean.getDuplicateIndex(0) );                    // set AC_TYPE
                    
                    codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
                    saveRequired = true;
                    // set the user name 
                    codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureBean.getUserIndex() );
                                     
                    tblCodeTable.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
                    tblCodeTable.editCellAt(codeTableModel.getRowCount()-1,  0) ;
                    tblCodeTable.requestFocus();
                }//end if                   
                // Added for coeus4.3 enhancements - ends    
            }
            
            else
            {  
                sorter.insertRow(sorter.getRowCount(),oneRow);
                // set the duplicate index interface aw_...
                codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, tableStructureBean.getDuplicateIndex(0) );
                // set AC_TYPE
                codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
                saveRequired = true;
                // set the user name 
                codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureBean.getUserIndex() );

                //Added for Case#4136 - add a new rule function, the sub_module_code in osp$rule_functions should be set to 0 -Start
                if (tableStructureBean.getActualName().equalsIgnoreCase("osp$rule_functions")){
                    //set the sub module code as zero
                    if(codeTableModel.getValueAt(codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-4) == null ){
                        codeTableModel.setValueAt("0", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-4);
                    }                    
                }else if (tableStructureBean.getActualName().equalsIgnoreCase("osp$rule_variables")){
                   if(codeTableModel.getValueAt(codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-4) == null ){
                        codeTableModel.setValueAt("0", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-4);
                   }
                }
                //Added for Case#4136 - add a new rule function, the sub_module_code in osp$rule_functions should be set to 0 -End
                tblCodeTable.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
                tblCodeTable.editCellAt(codeTableModel.getRowCount()-1,  0) ;
                tblCodeTable.requestFocus();
            }
           }
         } 
        //set defaultvalue if required.
        int colTotal;
        colTotal = tblCodeTable.getColumnCount();
        TableColumn column; 
        ColumnBean columnBean;
//        tableStructureBean.getHashTableColumns()
        for (int i = 0 ; i < colTotal; i++)
        {
            column = codeTableColumnModel.getColumn(i) ;
            columnBean = (ColumnBean) column.getIdentifier();
            if (columnBean.getDefaultValue() != null)
            {
                if (columnBean.getOptions() != null)//for combobox with options( in xml)
                {   
                    HashMap hashOptions = columnBean.getOptions() ;
                    for (Iterator it=hashOptions.keySet().iterator(); it.hasNext(); )
                    {    
                        String strKey =  it.next().toString() ; 
                        if (columnBean.getDefaultValue().toString().equals(strKey))
                        {  
                           // add comboboxbean itself to the data
                            codeTableModel.setValueAt(new ComboBoxBean(strKey, hashOptions.get(strKey).toString()), codeTableModel.getRowCount()-1, i );                           
                        }    
                            
                    }                  
                }
                else
                {
                    //Added for Case#4136 - add a new rule function, the sub_module_code in osp$rule_functions should be set to 0 -Start
                    if (tableStructureBean.getActualName().equalsIgnoreCase("osp$rule_functions")){
                        //set the sub module code as zero
                        if(codeTableModel.getValueAt(codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-4) == null ){
                            codeTableModel.setValueAt((Object)columnBean.getDefaultValue(), codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-4);
                        }
                    }else if (tableStructureBean.getActualName().equalsIgnoreCase("osp$rule_variables")){
                        //set the sub module code as zero
                        if(codeTableModel.getValueAt(codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-4) == null ){
                            codeTableModel.setValueAt((Object)columnBean.getDefaultValue(), codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-4);
                        }
                    }else{
                        codeTableModel.setValueAt((Object)columnBean.getDefaultValue(), codeTableModel.getRowCount()-1, i );
                    }
                    //Added for Case#4136 - add a new rule function, the sub_module_code in osp$rule_functions should be set to 0 -End
                }
            }
            
        }
    }

    
    public void btnDeleteMouseClicked(java.awt.event.ActionEvent evt) 
    {
        int rowNum = tblCodeTable.getSelectedRow();
        if (rowNum == -1) return;
        //COEUSQA:1691 - Front End Configurations to CoeusLite Pages - Start
        if(ARRA_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName()) || BUDGET_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName())
            || IRB_PROTO_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName()) || AC_PROTO_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName())
            || NEGOTIATION_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName()) || PROPOSAL_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName())
            || SUB_CONTRACT_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName()) || COI_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName())) {
            return;
        }
        //COEUSQA:1691 - End
        
        //Modified for Case#4021 - narrative code auto generated causes conflicts  - Start
//        if ( tblCodeTable.getEditingColumn() >= 0 )
//        {
//           tblCodeTable.getCellEditor().stopCellEditing(); 
//        }        
        if(!tableStructureBean.getActualName().equals("OSP$NARRATIVE_TYPE")){
            if ( tblCodeTable.getEditingColumn() >= 0 ) {
                tblCodeTable.getCellEditor().stopCellEditing();
            }
        }
        //Modified for Case#4021 - narrative code auto generated causes conflicts  - End
        
        //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
        if(!tableStructureBean.getActualName().equals("OSP$PROPOSAL_ATTACHMENTS")){
            if ( tblCodeTable.getEditingColumn() >= 0 ) {
                tblCodeTable.getCellEditor().stopCellEditing();
            }
        }
        //COEUSQA-1525 : End
        
        //here need to check dependency first
        if (!checkDependency(rowNum, "Delete"))
        {
              return;
        }
        //code added for coeus4.3 enhancements - starts,
        //if the selection module is Others then warning message will be poped up
        if (tableStructureBean.getActualName().equalsIgnoreCase("OSP$PROTOCOL_MODULES") &&
                codeTableModel.getValueAt(sorter.getIndexForRow(rowNum), 0).equals(IrbWindowConstants.OTHERS)) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                            "protocolModules_exceptionCode.2131"));
                return;
        }
        //code added for coeus4.3 enhancements - ends
        String msg = coeusMessageResources.parseMessageKey(
                            "generalDelConfirm_exceptionCode.2100");
        int confirm = CoeusOptionPane.showQuestionDialog(
                                            msg,
                                            CoeusOptionPane.OPTION_YES_NO,
                                            CoeusOptionPane.DEFAULT_YES);
        if (confirm == 1) return;
//        System.out.println("  *** Delete row selected row is  ***  " + rowNum) ; 
        //keep all deleted row into vecDeletedRows.
        if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),codeTableModel.getColumnCount()-1) != "I")
        {  
            //if not new inserted row, come to here and set AC_tYPE to "D"
            codeTableModel.setValueAt("D", sorter.getIndexForRow(rowNum), codeTableModel.getColumnCount()-1);
            saveRequired = true;
            //save to vecDeletedRows with hash of vec
            //vecDeletedRows.add(codeTableModel.getDataVector().elementAt(rowNum));   
            
            vecDeletedRows.add(getTableRow(rowNum)) ;
        }//end if
        
        sorter.removeRow(rowNum);
          
        if ( rowNum != 0 )
        {
            rowNum--; 
        }
        else
        {
            tblCodeTable.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ; 
        }
         
        if (tblCodeTable.getRowCount() > 0  )
        {
            tblCodeTable.changeSelection(rowNum,  1, false, false) ; 
        }
         
    }
        
    private void  tableNameChanged()
    {
       
        //in case data were changed in one table and user switch to another table, ask user to save the chagnes or not.
     
        GetLastValue();
        if (askedChangeList ) return;
        if(! btnCloseActionPerformed()) // cancle the selection change 
        {
            askedChangeList = true;
            treeCodeTable.setSelectionPath(selectedNode);
            askedChangeList = false;
            return;
        }
               

        saveRequired = false;
        perSelectedNode = selectedNode;
        selectedNode = treeCodeTable.getSelectionPath();
        tableStructureBean = tableStructureBean_temp;
        refreshTable();

    }

    // get the tablestructure bean of the selected table and display its description
    public Vector showTableDescription(java.awt.event.ActionEvent evt)
    {
//        tableStructureBean = (TableStructureBean)hashAllCodeTableStructure.get(new Integer(lstCodeTable.getSelectedIndex()));
        Vector vecDetails = new Vector() ;
        TreePath tp = treeCodeTable.getSelectionPath();
        
        // in case there was nothing being selected
        if (tp == null) return vecDetails ;
        
//        vecDetails.add(0, lstCodeTable.getSelectedValue()) ;
        vecDetails.add(0, tp.getLastPathComponent()) ;
        if (tableStructureBean == null)
        {
            vecDetails.add(1, "");
        }
        else
        {
            vecDetails.add(1, tableStructureBean.getDescription()) ;
        }
        return vecDetails ;
    }
    
    
    
    /**
     *return true will close this window in the CodeTableBaseWindow 
     */
   // public boolean btnCloseActionPerformed(java.awt.event.ActionEvent evt) 
    public boolean btnCloseActionPerformed() 
    {   
        // check for the unsaved data and prompt user for saving and then exit
        String msg = coeusMessageResources.parseMessageKey(
                                                        "saveConfirmCode.1002");
        /* before closing this window check for table data is changed or not and 
        * confirm the user for saving the ination.
        */

        if ( isSaveRequired())
        {    
             int confirm = CoeusOptionPane.showQuestionDialog(msg,
                                    CoeusOptionPane.OPTION_YES_NO_CANCEL, 
                                    CoeusOptionPane.DEFAULT_YES);
            
             switch(confirm)
             {
                case(JOptionPane.YES_OPTION):
                    try
                    {                 
                        if (!saveData()) return false;                        
                        return true;
                        //parent.dispose(); 
                    }catch(Exception ex)
                     {
                        ex.printStackTrace();
                        String exMsg = ex.getMessage();
                                CoeusOptionPane.showWarningDialog(exMsg);
                    }
                case(JOptionPane.NO_OPTION):
                    return true;
                case(JOptionPane.CANCEL_OPTION):   
                    return false;
            }
                 
      }
     
        return true; //there is no data changed
    }
        
        
    
    
   // when user hits save btn first build the vector which has modified rows 
   // then get the stored proc bean for update and stick them in the Requesttxn bean and send it to the 
    // servlet
    public void btnSaveActionPerformed(java.awt.event.ActionEvent evt) 
    {
        // in case of there was no code table selected
        if (tableStructureBean == null) return;
        
        int  selectedRow = tblCodeTable.getSelectedRow();
        // Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE - Start
        if(tblCodeTable.getCellEditor() != null){
            tblCodeTable.getCellEditor().stopCellEditing();
        }
        // Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE - End
        //Modify for fix array index out of bounds exception start by tarique
        if (!saveData()) return;
        refreshTable();
        if(selectedRow == -1){
            return;
        }
        //Modify for fix array index out of bounds exception end by tarique
        GetLastValue();
        
        Object temp = codeTableModel.getValueAt(sorter.getIndexForRow(selectedRow),0);
        int sorteColumn = sorter.getSortedColNum();
        boolean asc = sorter.getAscending();
        boolean sorted = sorter.getHasSorted();
        
        //return if failed to save.
//        if (!saveData()) return;
//        refreshTable();
        if (sorted)
        {
            sorter.sortByColumn(sorteColumn,asc);
        }

//        //re-selected the row same as before save performed.
//        if (selectedRow > 0)
//            tblCodeTable.setRowSelectionInterval(selectedRow,selectedRow);
        if (selectedRow >= 0)
        {
            for (int i = 0; i < tblCodeTable.getRowCount(); i++)
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(i),0).toString().equals(temp.toString()))
                {
                    tblCodeTable.setRowSelectionInterval(i,i);
                    break;
                }
                else
                {
                    if ( i == tblCodeTable.getRowCount() -1)
                        tblCodeTable.setRowSelectionInterval(0,0);
                }
            }
        }
    }
    
    
    // overloaded method added by chandra. 
    // Save the code table when user saves frm file - save
    public void btnSaveActionPerformed() 
    {
        // in case of there was no code table selected
        if (tableStructureBean == null) return;
        
        int  selectedRow = tblCodeTable.getSelectedRow();
        // Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE - Start
        if(tblCodeTable.getCellEditor() != null){
            tblCodeTable.getCellEditor().stopCellEditing();
        }
        //return if failed to save.
        if (!saveData()) return;
        refreshTable();
        if(selectedRow == -1){
            return;
        }
        // Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE - End
        GetLastValue();
        Object temp = codeTableModel.getValueAt(sorter.getIndexForRow(selectedRow),0);
        int sorteColumn = sorter.getSortedColNum();
        boolean asc = sorter.getAscending();
        boolean sorted = sorter.getHasSorted();
        if (sorted)
        {
            sorter.sortByColumn(sorteColumn,asc);
        }

//        //re-selected the row same as before save performed.
//        if (selectedRow > 0)
//            tblCodeTable.setRowSelectionInterval(selectedRow,selectedRow);
        if (selectedRow >= 0)
        {
            for (int i = 0; i < tblCodeTable.getRowCount(); i++)
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(i),0).toString().equals(temp.toString()))
                {
                    tblCodeTable.setRowSelectionInterval(i,i);
                    break;
                }
                else
                {
                    if ( i == tblCodeTable.getRowCount() -1)
                        tblCodeTable.setRowSelectionInterval(0,0);
                }
            }
        }
    }
    
 
    private AllCodeTablesBean getAllCodeTableBeanData()
    {
       /*  send the request to the server using RequestTxnBean */ 
       AllCodeTablesBean act = null ;
        
       try
       {    // send request
//          System.out.println("*** Sending RequestTxnBean with LIST_TABLES action ***") ;
          RequestTxnBean requestTxnBean = new RequestTxnBean();
          requestTxnBean.setAction("LIST_TABLES");
          //act = (AllCodeTablesBean)getDataFromServlet(requestTxnBean) ;
          
          RequesterBean requester = new RequesterBean();
         
          requester.setDataObject(requestTxnBean) ;
         
          String connectTo =CoeusGuiConstants.CONNECTION_URL
            + CODE_TABLE_SERVLET ;
        AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        
        ResponderBean responder = comm.getResponse();
        if (responder.hasResponse())
        {
            act = (AllCodeTablesBean)responder.getDataObject() ;
            userId = responder.getId() ;
        }
          
        }
       catch(Exception ex)
       {
//        System.out.println("  *** Error in getting structure data ***  ") ; 
        ex.printStackTrace() ;
       }
        
       return act;
    }
   
    
    public Vector drawTableUsingTSB(TableStructureBean tsb) 
    {
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        HashMap hmTableColumnBean = tsb.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
        numColumnsDisplay = tsb.getNumColumns() ;
        //need to add a AC_TYPE column, that's +1
        columnNames = new String[numColumnsDisplay + 1];
        // create a table colunmodel thing can used to access Table Column directly
        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ;
        //Added for COEUSDEV-86 : Questionnaire for a Submission -Start
        TableColumn tbColumnSubModules = null;
        String moduleCode = "";
        //COEUSDEV-86 : End
        int lastDisplayCol = 0;
        
        // populate the column header name on the the screen
        if (numColumnsDisplay > 0)
        {
            for (int i = 0; i < numColumnsDisplay; i++)
            {   
                //to add columns
              ColumnBean newColumnBean = (ColumnBean)hmTableColumnBean.get(new Integer(i)) ;
              TableColumn newColumn = new TableColumn(i, newColumnBean.getDisplaySize()) ; // set the model index and width
              newColumn.setIdentifier(newColumnBean) ; // set the column bean itself as identifier
              newColumn.setHeaderValue(newColumnBean.getDisplayName()) ;
              if (newColumnBean.getColumnVisible())
              {
                  lastDisplayCol = i;
              }
              if (tsb.getDisplayName().equalsIgnoreCase("Rule Functions Arguments") && newColumnBean.getColumnName().equalsIgnoreCase("FUNCTION_NAME"))
              {
                  //build data for combobox
                  CoeusComboBox comb = new CoeusComboBox() ;
                  
//                  if (hmData == null ||hmData.size() < 1 )
//                  { 
//                    getDataForTheCombobox();                  
//                  }
                  getRuleFunctionNames(); 
                  for (int rowNum = 0; rowNum < hmFunctionNames.size(); rowNum++ )
                  {                      
                    ComboBoxBean comboBoxBean = new ComboBoxBean(hmFunctionNames.get(new Integer(rowNum)).toString(), hmFunctionNames.get(new Integer(rowNum)).toString());
                    comb.addItem(comboBoxBean) ;
                  }
                  
                  newColumn.setCellEditor(new ExtendedCellEditor(comb)) ;
              }
              //Modified for  COEUSDEV-86 : Questionnaire for a Submission -Start
              //Sub module code for a module is fetched from the database
              //Only the module details are fetched from CodeTable.xml
//               else
//              {
//               if (newColumnBean.getOptions()!= null )
//              {
//                HashMap hashOptions = newColumnBean.getOptions() ;
//                CoeusComboBox comb = new CoeusComboBox() ;
//                
//                if (newColumnBean.getColumnCanBeNull())
//                {
//                    comb.addItem(new ComboBoxBean("", "")) ;
//                }
//                for (Iterator it=hashOptions.keySet().iterator(); it.hasNext(); )
//                {    
//                    String strKey =  it.next().toString() ; 
//                    
//                    ComboBoxBean comboBoxBean = new ComboBoxBean(strKey, hashOptions.get(strKey).toString());
//                    comb.addItem(comboBoxBean) ;
//                                       
//                } 
//                
//                 newColumn.setCellEditor(new ExtendedCellEditor(comb)) ;
//
//              }
             else if(newColumnBean.getColumnName().equalsIgnoreCase(SUB_MODULE_CODE)){
                  HashMap hmSubModules =  new HashMap();
                  hmSubModules.put("0",NONE_ITEM_IN_COMBOBOX);
                  CoeusComboBox subModuleComboBox = new CoeusComboBox() ;
                  for (Iterator it=hmSubModules.keySet().iterator(); it.hasNext(); ) {
                      String strKey =  it.next().toString() ;
                      String description =  (String)hmSubModules.get(strKey);
                      ComboBoxBean comboBoxBean = new ComboBoxBean(strKey,description );
                      subModuleComboBox.addItem(comboBoxBean) ;
                  }
                  tbColumnSubModules = newColumn;
                  
                  newColumn.setCellEditor(new ExtendedCellEditor(subModuleComboBox)) ;
                }//Added for IACUC get Procedure category code-Start
              //Modified with COEUSQA-2667:User interface for setting up question details for procedure categories
                else  if (newColumnBean.getColumnName().equalsIgnoreCase(PROCEDURE_CAT_CODE_COL)){
                  if(tsb.getDisplayName().equalsIgnoreCase(PROCEDURES) || tsb.getActualName().equalsIgnoreCase(PROC_CAT_CUSTOM_DATA)){  
                  CoeusComboBox procCategoryComboBox = new CoeusComboBox();
                    vecProcCategoryType =getProcedureCategoryCode();
                    if(vecProcCategoryType != null && vecProcCategoryType.size() > 0){
                        for(int index = 0; index<vecProcCategoryType.size();index++){
                            ComboBoxBean comboBoxbean = (ComboBoxBean)vecProcCategoryType.get(index);
                            procCategoryComboBox.addItem(comboBoxbean);
                        }
                    }
                    newColumn.setCellEditor(new ExtendedCellEditor(procCategoryComboBox)) ;
                  }
                }//Added for IACUC get Procedure category code-End
              // Modified for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - Start
//              //Added with COEUSQA-2667:User interface for setting up question details for procedure categories-Start
//                else  if ((newColumnBean.getColumnName().equalsIgnoreCase(LOOKUP_ARG_COL)) && tsb.getActualName().equalsIgnoreCase(PROC_CAT_CUSTOM_DATA)){
//                  
//                  CoeusComboBox argumentcomboBox = new CoeusComboBox();
//                  getLookUpArguments();
//                  argumentcomboBox.addItem(new ComboBoxBean("",""));
//                  newColumn.setCellEditor(new ExtendedCellEditor(argumentcomboBox)) ;
//                }
//              //COEUSQA-2667:End
               //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
                else if(LOCATION_TYPE_CODE_COL.equalsIgnoreCase(newColumnBean.getColumnName())) {               
                  if(LOCATION_NAME.equalsIgnoreCase(tsb.getDisplayName()) || LOCATION_NAME_TABLE_NAME.equalsIgnoreCase(tsb.getActualName())){
                      CoeusComboBox locationTypeComboBox = new CoeusComboBox();
                      vecLocationType = getLocationTypeCode();
                      if(vecLocationType != null && vecLocationType.size() > 0){
                          for(int index = 0; index<vecLocationType.size();index++){
                              ComboBoxBean comboBoxbean = (ComboBoxBean)vecLocationType.get(index);
                              locationTypeComboBox.addItem(comboBoxbean);
                          }
                      }
                      newColumn.setCellEditor(new ExtendedCellEditor(locationTypeComboBox)) ;
                  }
                }
              //COEUSQA:3005 - End
                else  if ((newColumnBean.getColumnName().equalsIgnoreCase(LOOKUP_ARG_COL)) 
                && (tsb.getActualName().equalsIgnoreCase(PROC_CAT_CUSTOM_DATA) || "OSP$EPS_PROP_COLUMNS_TO_ALTER".equalsIgnoreCase(tsb.getActualName()))){
                  CoeusComboBox argumentcomboBox = new CoeusComboBox();
                  getLookUpArguments();
                  argumentcomboBox.addItem(new ComboBoxBean("",""));
                  newColumn.setCellEditor(new ExtendedCellEditor(argumentcomboBox)) ;
                }
              // Modified for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - End
              else if(newColumnBean.getOptions() != null){
                  
                  HashMap hashOptions = newColumnBean.getOptions();
                  CoeusComboBox comb = new CoeusComboBox() ;
                  
                  if (newColumnBean.getColumnCanBeNull()) {
                      comb.addItem(new ComboBoxBean("", "")) ;
                  }
                   if(newColumnBean.getColumnName().equalsIgnoreCase("MODULE_CODE")){
                      hmModulesSubModules.clear();
                   }
                  for (Iterator it=hashOptions.keySet().iterator(); it.hasNext(); ) {
                      String strKey =  it.next().toString() ;
                      ComboBoxBean comboBoxBean = new ComboBoxBean(strKey, hashOptions.get(strKey).toString());
                      //SubModules are fetched for all the modules available
                      if(newColumnBean.getColumnName().equalsIgnoreCase("MODULE_CODE")){
                          Map subModule = getSubModulesForModule(strKey);
                          hmModulesSubModules.put(strKey,subModule);
                      }
                      comb.addItem(comboBoxBean) ;
                      
                  }
                  newColumn.setCellEditor(new ExtendedCellEditor(comb)) ;
              } 
             //COEUSDEV-86 : End
              
              else
              {    
                newColumn.setCellEditor(new ExtendedCellEditor(newColumnBean, i)) ;
              }
              
              if (newColumnBean.getDisplaySize()<=2)
              {
                  newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 60) ;
              }
              else if (newColumnBean.getDisplaySize()> 2 && newColumnBean.getDisplaySize()<=5)
              { 
//                 newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 35) ;
                  newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 25) ;
              }   
              else if (newColumnBean.getDisplaySize()>5 && newColumnBean.getDisplaySize() <= 15)
              { 
                 newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 30) ;
              }
              else if (newColumnBean.getDisplaySize()>15 && newColumnBean.getDisplaySize() <= 30)
              { 
                 newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 20) ;
              }
              else if (newColumnBean.getDisplaySize()>30 && newColumnBean.getDisplaySize() <= 50)
              {    
                  newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 12) ;
              }
              else
              {
                  newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
              }    
              
              // add the new columnto the column model also
             
              newColumn.setMinWidth(10) ;
            
              
              codeTableColumnModel.addColumn(newColumn) ;
              displayCodeTableColumnModel.addColumn(newColumn) ;

              // add it to the tablemodel
              codeTableModel.addColumn(newColumn);
              
              columnNames[i] = ((ColumnBean)hmTableColumnBean.get(new Integer(i))).getColumnName();
              
            }
            if (lastDisplayCol <= 1) {
                TableColumn newColumn = codeTableColumnModel.getColumn(lastDisplayCol) ;   
                newColumn.setPreferredWidth(655) ;
                newColumn.setHeaderValue(" " + newColumn.getHeaderValue().toString().trim()+"                                                                              "); 
            }
                                   
//            newColumn.setPreferredWidth(1000) ;
//            newColumn.setHeaderValue(" " + newColumn.getHeaderValue().toString().trim()+"                                                                                                                                                                                                                                                                                                                                                         "); 
//              
//                        
            // using the table sturcture bean u can find the stored proc for select or insert or update
            // so pass that tsb and get the DataBean from the servlet.
            populateTheTable(tsb);
            // populate the data on the the screen
            vdata = accDataBean.getVectData();
            HashMap htRow = new HashMap();
            for (int i=0; i < vdata.size(); i++) //loop for num of rows
            {
                htRow = (HashMap)vdata.elementAt(i);
                Object [] rowColumnDatas = new Object[numColumnsDisplay];
                // take out code = -1 row from the table
                if (tableStructureBean.isIdAutoGenerated() 
                        && htRow.get(columnNames[0]) != null 
                            && htRow.get(columnNames[0]).toString().equals("-1") )continue;
                
                for (int j = 0; j < numColumnsDisplay; j++) // loop for num of columns will display on screen
                {
                    rowColumnDatas[j] = (Object)htRow.get(columnNames[j]);
                    TableColumn column = codeTableColumnModel.getColumn(j) ;
                    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                    //case 2296 start
                    //for date
                    if (columnBean.getDataType().equals("DNUMBER")&& rowColumnDatas[j]!= null)
                    {
//                        rowColumnDatas[j] = dateUtils.formatDate(rowColumnDatas[j].toString(),DATE_FORMAT_DISPLAY);
                        rowColumnDatas[j] = dateUtils.formatDate(rowColumnDatas[j].toString(),SIMPLE_DATE_FORMAT);

                    }
                    //case 2296 end
                     if (columnBean.getOptions()!= null ) // then itz a combo box so display user understandable choice rather than the one stored to the database
                     {
                         HashMap hashOptions = columnBean.getOptions() ;
                         for (Iterator it=hashOptions.keySet().iterator(); it.hasNext(); )
                         {    
                            String strKey =  it.next().toString() ; 
                            if (rowColumnDatas[j] != null)
                            {  
                                if (rowColumnDatas[j].toString().equals(strKey))
                                {
                                    // add comboboxbean itself to the data
                                    rowColumnDatas[j] =   new ComboBoxBean(strKey, hashOptions.get(strKey).toString()) ; 
                                    //Added for COEUSDEV-86 : Questionnaire for a Submission -Start
                                    moduleCode = strKey;
                                    //COEUSDEV-86 : End
                                }    
                            }    
                            
                          }   
                     }    
                   
                    else if (tsb.getDisplayName().equalsIgnoreCase("Rule Functions Arguments") && columnBean.getColumnName().equalsIgnoreCase("FUNCTION_NAME")) 
                     {
                         for (int keyVal = 0; keyVal < hmFunctionNames.size(); keyVal++ )
                         {    
                    
                            if (rowColumnDatas[j] != null)
                            {  
                                if (rowColumnDatas[j].toString().equals(hmFunctionNames.get(new Integer(keyVal)).toString()))
                                {
                                    // add comboboxbean itself to the data
                                    rowColumnDatas[j] =  new ComboBoxBean(hmFunctionNames.get(new Integer(keyVal)).toString(), hmFunctionNames.get(new Integer(keyVal)).toString());
                               
                                }    
                            }    
                            
                          }   
                     }  
					//Added for Iacuc to add combobox value--start
                    else if(tsb.getDisplayName().equalsIgnoreCase(PROCEDURES) && columnBean.getColumnName().equalsIgnoreCase(PROCEDURE_CAT_CODE_COL) ||
                            //Added with COEUSQA-2667:User interface for setting up question details for procedure categories
                            (tableStructureBean.getActualName().equalsIgnoreCase(PROC_CAT_CUSTOM_DATA)  && columnBean.getColumnName().equalsIgnoreCase(PROCEDURE_CAT_CODE_COL))){
                        ComboBoxBean comboBoxBean = null;
                        for (int keyVal = 0; keyVal <vecProcCategoryType.size(); keyVal++ ) {
                            comboBoxBean = (ComboBoxBean)vecProcCategoryType.get(keyVal);
                            if (rowColumnDatas[j] != null) {
                                
                                if (rowColumnDatas[j].toString().equals(comboBoxBean.getCode())) {
                                    // add comboboxbean itself to the data
                                    rowColumnDatas[j] = vecProcCategoryType.get(keyVal); //new ComboBoxBean(vecProcCategoryType.get(keyVal).toString(), vecProcCategoryType.get(keyVal).toString());
                                    
                                }
                            }
                            
                        }
                    }
                    //Added for Iacuc to add combobox value--End
                    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
                    else if(LOCATION_NAME.equalsIgnoreCase(tsb.getDisplayName()) && LOCATION_TYPE_CODE_COL.equalsIgnoreCase(columnBean.getColumnName()) ||
                              (LOCATION_NAME_TABLE_NAME.equalsIgnoreCase(tableStructureBean.getActualName())  && LOCATION_TYPE_CODE_COL.equalsIgnoreCase(columnBean.getColumnName()))){
                        ComboBoxBean comboBoxBean = null;
                        for (int keyVal = 0; keyVal <vecLocationType.size(); keyVal++ ) {
                            comboBoxBean = (ComboBoxBean)vecLocationType.get(keyVal);
                            if (rowColumnDatas[j] != null) {
                                if (rowColumnDatas[j].toString().equals(comboBoxBean.getCode())) {
                                    rowColumnDatas[j] = vecLocationType.get(keyVal);
                                }
                            }
                        }
                    }
                    //COEUSQA:3005 -End
           
                    //Added for Case#4136 - add a new rule function, the sub_module_code in osp$rule_functions should be set to 0 -Start
                    else if (columnBean.getColumnName().equalsIgnoreCase(SUB_MODULE_CODE)) {
                        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
                        //Based on the module in the row submodules are set to the combo-box
                        Map subModules =  (Map)hmModulesSubModules.get(moduleCode);
                        CoeusComboBox subModuleComboBox = new CoeusComboBox() ;
                        if(subModules != null && subModules.size() > 0){
                            for (Iterator it= subModules.keySet().iterator(); it.hasNext(); ) {
                                String strKey =  it.next().toString() ;
                                String description =  (String)subModules.get(new Integer(strKey));
                                ComboBoxBean comboBoxBean = new ComboBoxBean(strKey, description);
                                subModuleComboBox.addItem(comboBoxBean) ;
                            }
                            tbColumnSubModules.setCellEditor(new ExtendedCellEditor(subModuleComboBox)) ;
                            for (Iterator it= subModules.keySet().iterator(); it.hasNext(); ) {
                                String strKey =  it.next().toString() ;
                                if (rowColumnDatas[j] != null) {
                                    if (rowColumnDatas[j].toString().equals(strKey)) {
                                        rowColumnDatas[j] =   new ComboBoxBean(strKey, subModules.get(new Integer(strKey)).toString()) ;
                                    }
                                }
                                
                            }
                        }
                        //COEUSDEV-86 : End
                        if (rowColumnDatas[j] ==null) {
                            rowColumnDatas[j] = "0";
                        }                        
                    }
                    //Added for Case#4136 - add a new rule function, the sub_module_code in osp$rule_functions should be set to 0 -End
                    //COEUSQA-2667:User interface for setting up question details for procedure categories - Start
                    //For Look up arguments, the code and description are same.
                    else if (columnBean.getColumnName().equalsIgnoreCase(LOOKUP_ARG_COL)){
                        
                        if (rowColumnDatas[j] != null) {
                            rowColumnDatas[j] = new ComboBoxBean(rowColumnDatas[j].toString(),rowColumnDatas[j].toString());
                        }
                    }
                    //COEUSQA-2667 : End
//                    Object obj = rowColumnDatas[j];
                    if (columnBean.getColumnVisible())
                    {    
                         if (rowColumnDatas[j] !=null)
                         {    
                            if (rowColumnDatas[j].equals("null"))
                            {
                                rowColumnDatas[j] = "";
                            }
                         }
                         else
                         {
                            rowColumnDatas[j] = null ; 
                         }    
                    }
                    
                }
                
                codeTableModel.addRow(rowColumnDatas);
//                sorter.insertRow(sorter.getRowCount(),rowColumnDatas);
            }            
             
       } 
       return vdata;
    }
    
   private void populateTheTable(TableStructureBean tableStructure)
    {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
      Vector vectData = null;
      try
      {
//          if(tableStructure == null)
//            System.out.println("tableStructure == null");
          //Get all stored procedures associated with this table.
          HashMap hashStoredProcedure =
            (HashMap)tableStructure.getHashStoredProceduresForThisTable();
//          if(hashStoredProcedure == null)
//            System.out.println("hashStoredProcedure == null");
          //Get the select stored procedure associated witht this table.
          StoredProcedureBean selectStoredProcedure =
            (StoredProcedureBean)hashStoredProcedure.get(new Integer(0));
          
          RequestTxnBean requestTxnBean = new RequestTxnBean();
          requestTxnBean.setAction("GET_DATA");
          requestTxnBean.setStoredProcedureBean(selectStoredProcedure);
         // accDataBean = (DataBean)getDataFromServlet(requestTxnBean) ;
          
          
          RequesterBean requester = new RequesterBean();
          requester.setDataObject(requestTxnBean) ;
         
          String connectTo =CoeusGuiConstants.CONNECTION_URL
            + CODE_TABLE_SERVLET ;
          AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo,requester);
          comm.send();
         
          ResponderBean responder = comm.getResponse();
          if (responder.hasResponse())
         {
             accDataBean = (DataBean)responder.getDataObject() ;
         }
          
          
          
          if(accDataBean != null)
          {
//            System.out.println("*** Recvd a DataBean ***");
            vectData = accDataBean.getVectData();
//            System.out.println("vectData.size() in GUI: "+vectData.size());
          }
     }
     catch(Exception ex)
     {
        ex.printStackTrace() ;
        
     }
          
     accDataBean.setVectData(vectData);   
           
  }
   
   // Added for COEUSQA-3471 : IACUC CoeusLite Review Type Determination Values   - Start
    /**
     * Method to get the records from the given procedure
     * @param procedureName 
     * @return Vector
     */
   private Vector getCodeTableData(String procedureName) {
       
       Vector vectData = null;
       try {
           StoredProcedureBean selectStoredProcedure = new StoredProcedureBean();
           selectStoredProcedure.setName(procedureName);
           RequestTxnBean requestTxnBean = new RequestTxnBean();
           requestTxnBean.setAction("GET_DATA");
           requestTxnBean.setStoredProcedureBean(selectStoredProcedure);
           RequesterBean requester = new RequesterBean();
           requester.setDataObject(requestTxnBean) ;
           String connectTo =CoeusGuiConstants.CONNECTION_URL
                   + CODE_TABLE_SERVLET ;
           AppletServletCommunicator comm
                   = new AppletServletCommunicator(connectTo,requester);
           comm.send();
           
           ResponderBean responder = comm.getResponse();
           if (responder.hasResponse()) {
               accDataBean = (DataBean)responder.getDataObject() ;
           }
           if(accDataBean != null) {
               vectData = accDataBean.getVectData();
           }
       } catch(Exception ex) {
           ex.printStackTrace() ;
           
       }
       return vectData;
   }
   // Added for COEUSQA-3471 : IACUC CoeusLite Review Type Determination Values   - End
   
    private Object getDataFromServlet(Object request)
    {
     Object response = null ;
     try
     { 
         RequesterBean requester = new RequesterBean();
         requester.setDataObject(request) ;
         
         String connectTo =CoeusGuiConstants.CONNECTION_URL
            + CODE_TABLE_SERVLET ;
        AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        
        ResponderBean responder = comm.getResponse();
        if (responder.hasResponse())
        {
            response = responder.getDataObject() ;
        }    
           
       }catch(Exception ex)
       {
//        System.out.println("  *** Error in Applet Servlet communication ***  ") ; 
        ex.printStackTrace() ;
       }

       return response ;
   }
   
   // this method will check the AC_Type field of all the rows in the table and build a vector of modified
   // rows. modified rows include newly inserted rows as well
   private Vector getModifiedRows()
   {
      Vector vecUpdatedRows = new Vector();
      int rowCount = 0;
      
      /* CASE #607 Add deletes to the vector before inserts and updates to avoid 
       * unique constraint error */
      
      if (vecDeletedRows.size() > 0 )
      {//append deleted rows to vecUpdatedRows
          for (int row = 0; row < vecDeletedRows.size(); row++)
          {
              vecUpdatedRows.add(vecDeletedRows.get(row));
          }          
      }      
      
      while(rowCount < tblCodeTable.getRowCount())
      {// check AC_TYPE field
//        String tmpACType ;
        if (codeTableModel.getValueAt(sorter.getIndexForRow(rowCount), codeTableModel.getColumnCount()-1) != null)
        {
            vecUpdatedRows.add(getTableRow(rowCount)) ;
        }
        rowCount++ ;  
      }//end while
   
      return vecUpdatedRows ;
   }
   
   //copy one table row to a hashmap
   private HashMap getTableRow(int rowNum)
   {
        HashMap hashRow = new HashMap() ;
//        int colTotal = codeTableModel.getColumnCount()-1;
        for (int colCount=0; colCount <= codeTableModel.getColumnCount()-1; colCount++)
        { // for each column u will build a hashmap 
            TableColumn column = codeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            Object val = null ;
            // the whole idea of setting up the columnBean as the identifier is that u get to know
            // all abt the structure details of the column, so u can create appropriate type of object
            // and stick it in to the hashMap
            if (columnBean.getDataType().equals("NUMBER"))
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount) == null
                        ||(codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).equals("")))
                {  
                    val = null ; //new Integer(0) 
                }
                else
                {
                    //Modified with case 2158: sub module code is of type NUMBER; but in a combobox.
//                    val = new Integer(codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).toString()) ;
                    val = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount) ;
                    if (val instanceof ComboBoxBean) {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val;
                        //Added for IACUC For getting Procedure category code-start
                        if(columnBean.getColumnName().equalsIgnoreCase(PROCEDURE_CAT_CODE_COL)) {
                            ComboBoxBean comboBoxBean = null;
                            for (int keyVal = 0; keyVal <vecProcCategoryType.size(); keyVal++ ) {
                                comboBoxBean = (ComboBoxBean)vecProcCategoryType.get(keyVal);
                                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount) != null) {
                                    
                                    if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).toString().equals(comboBoxBean.getDescription())) {
                                        // add comboboxbean itself to the data
                                        val =  new Integer(comboBoxBean.getCode());
                                        
                                    }
                                }
                                
                            }
                        }
                        //Added for IACUC For getting Procedure category code-End
                        else{
                            val = new Integer(cmbTmp.getCode()) ;
                        }
                    } else {
                        val = new Integer(val.toString()) ;
                    }
                    //2158 End
                } 
            }
            
            //for float
            if (columnBean.getDataType().equals("FNUMBER"))
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount) == null
                        ||(codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).equals("")))
                {  
                    val = null ; //new 
                }
                else
                {
                    val = new Float(codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).toString()) ;
                }    
            }


                
            if (columnBean.getDataType().indexOf("VARCHAR2") != -1)
            {              
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount)==null)
                {
                    val = "";
                }
                else
                {
                    val = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount) ;
                    if (val instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val ; 
                        val = cmbTmp.getCode() ;
                    }

                    else
                    {
                        val = val.toString() ;
                    }
                }
            }
                
            if (columnBean.getDataType().equals("DATE"))
            {
                //coeusdev-568 start
                //Date today = new Date();
                //coeusdev-568 end
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum), codeTableModel.getColumnCount()-1).equals("I")) // if itz an insert
                {   //AV_ & AW_ will be the same for insert
                    //coeusdev-568 start
                    //val = new java.sql.Timestamp(today.getTime());
                    val = CoeusUtils.getDBTimeStamp();
                    //coeusdev-568 end
                }
                else
                {  // for update
                   // there will be only two dates in any table one AV_UPDATE_TIMESTAMP and the other one AW_UPDATE_TIMESTAMP
                    if (columnBean.getQualifier().equals("VALUE"))
                    {  //AV_...
                        //coeusdev-568 start
                      //val = new java.sql.Timestamp(today.getTime());
                        val = CoeusUtils.getDBTimeStamp();
                        //coeusdev-568 end
                    }
                    else
                    {  //AW_...
                        val =  java.sql.Timestamp.valueOf(codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).toString()) ;
                    }
                }
            }
            //case 2296 start
            //for date
            if (columnBean.getDataType().equals("DNUMBER"))
            {
              try {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount) == null
                        ||(codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).equals("")))
                {
                    val = null ; //new
                }
                else
                {
                    String  strDate;
                    Date date;
                    strDate = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum), colCount).toString().trim();
                    date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                    val = new java.sql.Date(date.getTime());
                }
               }catch (ParseException parseException) {
                   parseException.printStackTrace();
               }
            }
            //case 2296 end
                
            hashRow.put(columnBean.getColIdentifier(), val) ;
        } //end for
        return hashRow;
   }
   
   // this function should do the validation of rows
   private boolean tableDataValid()
   { 
    // if there are any form level validations that should go there
        int rowTotal = tblCodeTable.getRowCount();
        int colTotal = tblCodeTable.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++)
        {
            for (int colIndex = 0; colIndex < colTotal; colIndex++)
            {
                TableColumn column = codeTableColumnModel.getColumn(colIndex) ;
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                if (columnBean.getColumnEditable()
                    && !columnBean.getColumnCanBeNull())
                {
                     if ( tblCodeTable.getValueAt(rowIndex,colIndex) != null)
                    {
                        if (tblCodeTable.getValueAt(rowIndex,colIndex).equals("")
                            || tblCodeTable.getValueAt(rowIndex,colIndex).toString().trim().equals("") )
                        {   
                            String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblCodeTable.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    }
                    else
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");
                        
                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblCodeTable.changeSelection(rowIndex,  colIndex, false, false) ;
                        return false;
                    }
                }
            }
           
        }   
    return true ;
   }
    
  
  private void refreshTable()
  {
      vecDeletedRows.clear();
      if (tableStructureBean == null )
      {
          codeTableModel = new ExtendedDefaultTableModel() ;
          sorter.setModel(codeTableModel, false) ;
          tblCodeTable.setModel(sorter) ;
          tblCodeTable.repaint();
 
          askedChangeList = false; 
          return;
      }
      if (tableStructureBean.getFormComponent() == null) // other tables
      {    

        codeTableModel = new ExtendedDefaultTableModel() ;
        vecOriginalData = drawTableUsingTSB(tableStructureBean);
        
        sorter.setModel(codeTableModel, false) ;
        tblCodeTable.setModel(sorter) ;
                       
        tblCodeTable.setColumnModel(displayCodeTableColumnModel); // also give the structure
       
       // tblCodeTable.addFocusListener(this) ;        
        tblCodeTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        javax.swing.table.JTableHeader header
            = tblCodeTable.getTableHeader();
        header.setReorderingAllowed(false);
                   
        // show only columns wich can be edited 
        int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;
        for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--)    
        {  
            TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                        
            if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
            {
                TableColumnModel columnModel = tblCodeTable.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblCodeTable.removeColumn(inColumn ); 
            }
        }
        currentMaxId = 0;
        if (tblCodeTable.getRowCount() > 0)
        {
            tblCodeTable.setRowSelectionInterval(0,0) ;
            tblCodeTable.repaint() ;
       
            // initialise currentMaxId
            if (tableStructureBean.isIdAutoGenerated())
            {  
                GetMaxId();
            }
         }
      }//end if other tables
      else
      {  // special case tables, show the new form
          
          Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
          
          
        if(tableStructureBean.getFormComponent().equals("frmState"))
        {
            FormStateTable frmState = new FormStateTable(parent,"State", true,userHasRights, allCodeTablesBean) ;
            frmState.setLocation(screenSize.width/5 , screenSize.height/5 );
            frmState.setVisible(true);
//            frmState.show() ;
        } 
        if(tableStructureBean.getFormComponent().equals("frmEDIEnabledSponsor"))
        {
            FormEDIEnabledSponsor frmEDIEnabledSponsor = new FormEDIEnabledSponsor(parent,"EDI Enabled Sponsors", true,userHasRights, allCodeTablesBean) ;
            frmEDIEnabledSponsor.setLocation(screenSize.width/5 , screenSize.height/5 );
//            frmEDIEnabledSponsor.show() ;
            frmEDIEnabledSponsor.setVisible(true);
        } 
        if(tableStructureBean.getFormComponent().equals("frmProtoCorrespDefRecip"))
        {
            FormProtoCorrespDefRecip frmProtoCorrespDefRecip = new FormProtoCorrespDefRecip(parent,"Protocol Correspondence Recipients", true,userHasRights, allCodeTablesBean) ;
            frmProtoCorrespDefRecip.setLocation(screenSize.width/5 , screenSize.height/5 );
//            frmProtoCorrespDefRecip.show() ;
            frmProtoCorrespDefRecip.setVisible(true);
        } 
        if(tableStructureBean.getFormComponent().equals("frmUnitCorrespondents"))
        {
            FormUnitCorrespondents frmUnitCorrespondents = new FormUnitCorrespondents(parent,"Unit Correspondents", true, userHasRights, allCodeTablesBean) ;
            frmUnitCorrespondents.setLocation(screenSize.width/5 , screenSize.height/5 );
//            frmUnitCorrespondents.show() ;
            frmUnitCorrespondents.setVisible(true);
        }
          // Added for Coeus4.3 Organization Correspondents Enhancement -Start
         if(tableStructureBean.getFormComponent().equals("frmOrgCorrespondents"))
        {
            FormOrgCorrespondents frmOrgCorrespondents = new FormOrgCorrespondents(parent,"Organization Correspondents", true, userHasRights, allCodeTablesBean) ;
            frmOrgCorrespondents.setLocation(screenSize.width/5 , screenSize.height/5 );
//            frmOrgCorrespondents.show() ;
            frmOrgCorrespondents.setVisible(true);
        } 
          // Added for Coeus4.3 Organization Correspondents enhancement -End
        /* CASE #IRBEN00078 Begin */
        if(tableStructureBean.getFormComponent().equals("frmProtocolFollowupAction"))
        {
            FormProtocolFollowupAction frmProtocolFollowupAction = 
                new FormProtocolFollowupAction(parent, "Protocol Follow-Up Actions",
                true,userHasRights, allCodeTablesBean);
            frmProtocolFollowupAction.setLocation(screenSize.width/5, screenSize.height/5);
//            frmProtocolFollowupAction.show();
            frmProtocolFollowupAction.setVisible(true);
        }
            //Added for IACUC-Start
            if(tableStructureBean.getFormComponent().equals("frmIacucProtocolFollowupAction")) {
                FormIacucProtocolFollowupAction frmIacucProtocolFollowupAction =
                        new FormIacucProtocolFollowupAction(parent, "Protocol Follow-Up Actions",
                        true,userHasRights, allCodeTablesBean);
                frmIacucProtocolFollowupAction.setLocation(screenSize.width/5, screenSize.height/5);
//            frmIacucProtocolFollowupAction.show();
                frmIacucProtocolFollowupAction.setVisible(true);
            }
          //Added for COEUSQA-2320 : Show in Lite for Special Review in Code table - Start
          if(tableStructureBean.getFormComponent().equals("frmSpecialReviewUsage")) {
              FormSpecialReviewUsage frmSpecialReviewUsage =
                      new FormSpecialReviewUsage(parent, "Special Review Usage",
                      true,userHasRights, allCodeTablesBean);
              frmSpecialReviewUsage.setLocation(screenSize.width/5, screenSize.height/5);
              frmSpecialReviewUsage.setVisible(true);
          }
          //COEUSQA-2320 :End
          // Added for COEUSQA-1412 Subcontract Module changes - Start
          if(tableStructureBean.getFormComponent().equals("frmContactUsage")) {
              FormContactUsage frmSpecialReviewUsage = new FormContactUsage(parent, "Contact Usage", true,userHasRights, allCodeTablesBean);
              frmSpecialReviewUsage.setLocation(screenSize.width/5, screenSize.height/5);
              frmSpecialReviewUsage.setVisible(true);
          }
          // Added for COEUSQA-1412 Subcontract Module changes - End
          
          // Added for COEUSQA-3397 : IACUC: Review Type vs. Recommended Action Code Table - Start
          if(tableStructureBean.getFormComponent().equals("frmRecommendAction")) {
              FormRecommendAction frmRecommendAction = new FormRecommendAction(parent, "Recommend Action", true,userHasRights, allCodeTablesBean);
              frmRecommendAction.setLocation(screenSize.width/5, screenSize.height/5);
              frmRecommendAction.setVisible(true);
          }
          // Added for COEUSQA-3397 : IACUC: Review Type vs. Recommended Action Code Table - End
            if(tableStructureBean.getFormComponent().equals("frmIacucProtoActionCorresp")) {
                FormIacucProtoActionCorresp frmIacucProtoActionCorresp =
                        new FormIacucProtoActionCorresp(parent, "Correspondence Generated for Protocol Actions",
                        true, userHasRights, allCodeTablesBean);
                frmIacucProtoActionCorresp.setLocation(screenSize.width/5, screenSize.height/5);
//            frmIacucProtoActionCorresp.show();
                frmIacucProtoActionCorresp.setVisible(true);
            }
            if(tableStructureBean.getFormComponent().equals("frmIacucProtoCorrespDefRecip")) {
                FormIacucProtoCorrespDefRecip frmIacucProtoCorrespDefRecip = new FormIacucProtoCorrespDefRecip(parent,"Protocol Correspondence Recipients", true,userHasRights, allCodeTablesBean) ;
                frmIacucProtoCorrespDefRecip.setLocation(screenSize.width/5 , screenSize.height/5 );
//            frmIacucProtoCorrespDefRecip.show() ;
                frmIacucProtoCorrespDefRecip.setVisible(true);
            }
            if(tableStructureBean.getFormComponent().equals("FormIacucTypeQualifierSubType")) {
                FormIacucTypeQualifierSubType frmIacucTypeQualifierSubType =
                        new FormIacucTypeQualifierSubType(parent, "Applicable Qualifier for Submission Type",
                        true, userHasRights, allCodeTablesBean);
                frmIacucTypeQualifierSubType.setLocation(screenSize.width/5, screenSize.height/5);
                frmIacucTypeQualifierSubType.setVisible(true);
            }
            if(tableStructureBean.getFormComponent().equals("FormIacucReviewTypeSubType")) {
                FormIacucReviewTypeSubType frmIacucReviewTypeSubType =
                        new FormIacucReviewTypeSubType(parent, "Applicable Review Type for Submission Type",
                        true, userHasRights, allCodeTablesBean);
                frmIacucReviewTypeSubType.setLocation(screenSize.width/5, screenSize.height/5);
                frmIacucReviewTypeSubType.setVisible(true);
            }
            //Added for case-COEUSQA-2540 Need ability to separate Correspondent Types-Start
            if("frmIacucUnitCorrespondents".equals(tableStructureBean.getFormComponent())){
                FormIacucUnitCorrespondents frmIacucUnitCorrespondents =
                        new FormIacucUnitCorrespondents(parent,"Unit Correspondents", true,
                        userHasRights, allCodeTablesBean) ;
                frmIacucUnitCorrespondents.setLocation(screenSize.width/5 , screenSize.height/5 );
                frmIacucUnitCorrespondents.setVisible(true);
            }
            if("frmIacucOrgCorrespondents".equals(tableStructureBean.getFormComponent())){
                FormIacucOrgCorrespondents frmIacucOrgCorrespondents = 
                        new FormIacucOrgCorrespondents(parent,"Organization Correspondents", true,
                        userHasRights, allCodeTablesBean) ;
                frmIacucOrgCorrespondents.setLocation(screenSize.width/5 , screenSize.height/5 );
                frmIacucOrgCorrespondents.setVisible(true);
            }              
            //Added for case-COEUSQA-2540 Need ability to separate Correspondent Types-end
            //Added for IACUC-End
        /* CASE #IRBEN00078 End */
        /* CASE #IRBEN00079 Begin */
        if(tableStructureBean.getFormComponent().equals("frmProtoActionCorresp"))
        {
            FormProtoActionCorresp frmProtoActionCorresp = 
                new FormProtoActionCorresp(parent, "Correspondence Generated for Protocol Actions", 
                true, userHasRights, allCodeTablesBean);
            frmProtoActionCorresp.setLocation(screenSize.width/5, screenSize.height/5);
//            frmProtoActionCorresp.show();
            frmProtoActionCorresp.setVisible(true);
        }
        /* CASE #IRBEN00079 End */     
        
        //Modified for Case#3053_Submission Details Type Qualifier Filter - Start
        // Added for Submission Type - Review Type Code Table
          if(tableStructureBean.getFormComponent().equals("FormTypeQualifierSubType"))
        {
            FormTypeQualifierSubType frmTypeQualifierSubType = 
                new FormTypeQualifierSubType(parent, "Applicable Qualifier for Submission Type", 
                true, userHasRights, allCodeTablesBean);
            frmTypeQualifierSubType.setLocation(screenSize.width/5, screenSize.height/5);
            frmTypeQualifierSubType.setVisible(true);
        }
        if(tableStructureBean.getFormComponent().equals("FormReviewTypeSubType"))
        {
            FormReviewTypeSubType frmReviewTypeSubType = 
                new FormReviewTypeSubType(parent, "Applicable Review Type for Submission Type", 
                true, userHasRights, allCodeTablesBean);
            frmReviewTypeSubType.setLocation(screenSize.width/5, screenSize.height/5);
            frmReviewTypeSubType.setVisible(true);
        }
         //Modified for Case#3053_Submission Details Type Qualifier Filter - End
          
        //case #683 begin
        if(tableStructureBean.getFormComponent().equals("frmCorrespBatch"))
        {
            FormCorrespBatch frmCorrespBatch = 
                new FormCorrespBatch(parent, "Correspondence Batch",
                true,userHasRights, allCodeTablesBean);
            frmCorrespBatch.setLocation(screenSize.width/5, screenSize.height/5);
//            frmCorrespBatch.show();
            frmCorrespBatch.setVisible(true);
        }
        //case #683 end 
        
        /*
         * Added for Email Implementation
         */
        if(tableStructureBean.getFormComponent().equals("frmMailRecipient"))
        {
//            FormMailRecipientModule frmMailRecipient =
//                new FormMailRecipientModule(parent, "Module Specification for Recipient",
//                true, userHasRights, allCodeTablesBean);
//            frmMailRecipient.setLocation(screenSize.width/5, screenSize.height/5);
//            frmMailRecipient.show();
//              FormRoleModule frmRoleModule = new FormRoleModule(parent, "Role Module",
//                      true, userHasRights, allCodeTablesBean);
//              frmRoleModule.setLocation(screenSize.width/5, screenSize.height/5);
////              frmRoleModule.show();
//              frmRoleModule.setVisible(true);
              //Modified for Case #2214 Email enhancement
              RoleModuleForm frmRoleModule = new RoleModuleForm(parent, "Roles in Coeus", true);
              frmRoleModule.setSize(700, 500);
              frmRoleModule.setLocation(screenSize.width/5, screenSize.height/5);
              frmRoleModule.setVisible(true);
        }
        
//        if(tableStructureBean.getFormComponent().equals("frmMailAction"))
//        {
//            FormMailAction frmMailAction = 
//                new FormMailAction(parent, "Action Creation", 
//                true, userHasRights, allCodeTablesBean);
//            frmMailAction.setLocation(screenSize.width/5, screenSize.height/5);
//            frmMailAction.show();
//        }
        //Email Implementation - End
         
        //Added for COEUSQA-2675 Remaining IACUC Protocol Actions Batch Correspondence-Start
        if(tableStructureBean.getFormComponent().equals("frmIacucCorrespBatch"))
        {
            FormIacucCorrespBatch frmIacucCorrespBatch = 
                new FormIacucCorrespBatch(parent, "IACUC Correspondence Batch",
                true,userHasRights, allCodeTablesBean);
            frmIacucCorrespBatch.setLocation(screenSize.width/5, screenSize.height/5);
            frmIacucCorrespBatch.setVisible(true);
        }
        //Added for COEUSQA-2675 Remaining IACUC Protocol Actions Batch Correspondence-End
          //COEUSQA:2393 - Revamp Coeus Budget Engine - Start
          if("RateClassForm".equals(tableStructureBean.getFormComponent())) {
              RateClassForm rateClassForm = new RateClassForm(parent, "Budget Inclusion and Exclusion Rates");
              FormTypeQualifierSubType frmTypeQualifierSubType =
                      new FormTypeQualifierSubType(parent, "Applicable Qualifier for Submission Type",
                      true, userHasRights, allCodeTablesBean);
              rateClassForm.showWindow();
          }
          //COEUSQA:2393 - End

        if ( perSelectedNode != null )
        {
            DefaultMutableTreeNode dmtn_temp = (DefaultMutableTreeNode)perSelectedNode.getLastPathComponent();
            if (dmtn_temp.isLeaf() && dmtn_temp.getLevel() > 1)
            {
                TableStructureBean tsb_temp = (TableStructureBean)dmtn_temp.getUserObject();
    //            if (! (tsb_temp.getFormComponent().equals("frmEDIEnabledSponsor") | tsb_temp.getFormComponent().equals("frmState")))
                if (! (tsb_temp.getFormComponent() == null)) 
                {
                    treeCodeTable.scrollPathToVisible(selectedNode.getParentPath());
                    treeCodeTable.setSelectionPath(selectedNode.getParentPath());
                    refreshTable();
                    askedChangeList = false; 
                    return;
                }
            }
            treeCodeTable.scrollPathToVisible(perSelectedNode);
            treeCodeTable.setSelectionPath(perSelectedNode);
            refreshTable();
            askedChangeList = false; 
        }
        else
        {
            treeCodeTable.scrollPathToVisible(selectedNode.getParentPath());
            treeCodeTable.setSelectionPath(selectedNode.getParentPath());
            refreshTable();
            askedChangeList = false; 
            return;
        }
      }//end else, special case    
 
  }
  
    
  
  private boolean saveData()
  {
    
     // do the validation b4 u go and build the vector of modified rows
      if (tableDataValid())
       { 
                // get the modified rows
                Vector vecModifiedRows = getModifiedRows() ;
                if (vecModifiedRows != null)
                {
//                    System.out.println("obtd modified rows successfuly") ;
                }   

                HashMap hashStoredProcedure = (HashMap)tableStructureBean.getHashStoredProceduresForThisTable();
//                if(hashStoredProcedure == null)
//                    System.out.println("hashStoredProcedure == null");
                  //Get the update stored procedure associated witht this table.
                  StoredProcedureBean updateStoredProcedure =
                    (StoredProcedureBean)hashStoredProcedure.get(new Integer(1));

                  RequestTxnBean requestTxnBean = new RequestTxnBean();
                  requestTxnBean.setAction("MODIFY_DATA");
                  requestTxnBean.setStoredProcedureBean(updateStoredProcedure);
                  requestTxnBean.setRowsToModify(vecModifiedRows) ;

                  // the servlet will return if the saving process was successful or not
                  Boolean success = (Boolean)getDataFromServlet(requestTxnBean) ;
                   if (success == null) // Error while saving data
                  {
                        String msg = coeusMessageResources.parseMessageKey(
                            "saveFail_exceptionCode.1102");
               
                        CoeusOptionPane.showInfoDialog(msg);
                        return false;
                  }
                  else
                  {//Data Saved Successfully
                      saveRequired = false;
                      vecDeletedRows.clear();
                      return true;
                  }

      }// end if data validation
      else
      {
          return false;
      }
  }
  
  //find Max Id for PrimaryKey.
  private void GetMaxId()
  {
      int colIdx = tableStructureBean.getPrimaryKeyIndex(0) ;
      int rowTotal = tblCodeTable.getRowCount();
      int currentId;
      if (rowTotal > 0)
      {
          currentMaxId  = Integer.parseInt(tblCodeTable.getValueAt(0, colIdx).toString()) ;
          for (int index = 1; index < rowTotal ; index++)
          {
            currentId  = Integer.parseInt(tblCodeTable.getValueAt(index, colIdx).toString()) ;
            if (currentId > currentMaxId )  currentMaxId = currentId;
          }
      }
      else
      {
          currentMaxId = 0;
      }
      
  }

    
  class ExtendedCellEditor extends DefaultCellEditor implements TableCellEditor
  {
        private CoeusTextField txtCell = new CoeusTextField();
        int selRow=0 ;
        int selColumn=0 ;
        Object oldValue = null ;
        
        public ExtendedCellEditor(CoeusComboBox comb) 
        {
            super(comb);
            // Commented for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - Start
            // Commented to fix JAVA 1.6 issue
//            comb.addFocusListener(new FocusAdapter() {
//                
//                public void focusGained(FocusEvent e) {
//                    // System.out.println("*** focus Gained ***") ;
//                }
//                
//                public void focusLost(FocusEvent fe) {
//                    if (!fe.isTemporary()) {
////                        System.out.println("*** focus lost ***") ;
//                        GetLastValue();
//                    }// end if
//                }// end focus lost
//            }); // end focus listener
            // Commented for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - End
            //Added for COEUSDEV-86 : Questionnaire for a Submission -Start
            //When module is changed, 'None' in the submodule is selected
            comb.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                     int selectedColumn  =  tblCodeTable.getSelectedColumn();
                     int selectedRow = tblCodeTable.getSelectedRow();
                    if(e.getStateChange() == e.DESELECTED){
                        return ;
                    }
                        
                    if(e.getStateChange() == e.SELECTED && selectedColumn > 0){
//                        System.out.println("1"+codeTableColumnModel.getColumnCount());
//                        System.out.println("2"+tblCodeTable.getSelectedColumn());
                        TableColumn tColumn = codeTableColumnModel.getColumn(selectedColumn) ;
                        ColumnBean columnBean = (ColumnBean)tColumn.getIdentifier() ;
                        if(columnBean.getColumnName().equals("MODULE_CODE")){
                            HashMap hmTableColumnBean = tableStructureBean.getHashTableColumns();
                            int numColumnsForDisplay = tableStructureBean.getNumColumns();
//                            int moduleCodeColumnIndex = 0;
                            if (numColumnsForDisplay > 0) {
                                for (int index = 0; index < numColumnsDisplay; index++) {
                                    ColumnBean columnDisplayBean = (ColumnBean)hmTableColumnBean.get(new Integer(index)) ;
                                    if(columnDisplayBean != null && columnDisplayBean.getColumnName().equals(SUB_MODULE_CODE)){
                                        ComboBoxBean comboBoxBean = new ComboBoxBean("0", NONE_ITEM_IN_COMBOBOX);
                                        tblCodeTable.setValueAt(comboBoxBean,selectedRow,index);
                                        break;
                                    }
                                }
                            }
                        }
                        //Added with COEUSQA-2667:User interface for setting up question details for procedure categories - Start
                        //If haslook up is changed from Yes to No, clear the lookup window and look up argument
//                        else if(columnBean.getColumnName().equals(HAS_LOOKUP_COL)){
//                            HashMap hmTableColumnBean = tableStructureBean.getHashTableColumns();
//                            int numColumnsForDisplay = tableStructureBean.getNumColumns();
//                            if (numColumnsForDisplay > 0) {
//                                for (int index = 0; index < numColumnsDisplay; index++) {
//                                    ColumnBean columnDisplayBean = (ColumnBean)hmTableColumnBean.get(new Integer(index)) ;
//                                    String columnName = (columnDisplayBean != null)?columnDisplayBean.getColumnName():null;
//                                    if(LOOKUP_WINDOW_COL.equals(columnName) || LOOKUP_ARG_COL.equals(columnName)){
//                                        ComboBoxBean comboBoxBean = new ComboBoxBean("", "");
//                                        tblCodeTable.setValueAt(comboBoxBean,selectedRow,index);
//                                    }
//                                }
//                            }
//                        }
                        //If look up window is changed, clear the look up argument selected.
                        else if(columnBean.getColumnName().equals(LOOKUP_WINDOW_COL)){
                            HashMap hmTableColumnBean = tableStructureBean.getHashTableColumns();
                            int numColumnsForDisplay = tableStructureBean.getNumColumns();
                            if (numColumnsForDisplay > 0) {
                                for (int index = 0; index < numColumnsDisplay; index++) {
                                    ColumnBean columnDisplayBean = (ColumnBean)hmTableColumnBean.get(new Integer(index)) ;
                                    if(columnDisplayBean != null && LOOKUP_ARG_COL.equals(columnDisplayBean.getColumnName())){
                                        ComboBoxBean comboBoxBean = new ComboBoxBean("", "");
                                        tblCodeTable.setValueAt(comboBoxBean,selectedRow,index);
                                        break;
                                    }
                                }
                            }
                        }
                        //COEUSQA-2667:End
                    }
                }
            });
            //COEUSDEV-86 : End
        }

        public ExtendedCellEditor(ColumnBean columnBeanIn, int colIndex)
        {
            super( new CoeusTextField() );
            //Modified with COEUSQA-2667:User interface for setting up question details for procedure categories
            int maxLength = columnBeanIn.getMaxLength()>0 ? columnBeanIn.getMaxLength():columnBeanIn.getDisplaySize();
            
            if (columnBeanIn.getDataType().equals("NUMBER"))
            {
                txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC,maxLength));
            }
            else
            {
//                if ((colIndex == tableStructureBean.getPrimaryKeyIndex(0))
//                        && !tableStructureBean.isIdAutoGenerated()  )
                if (columnBeanIn.getDataType().equals("FNUMBER"))
                {
                    txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC + ". -",maxLength));
                }
                else
                {
                    //case 2296 start
                  if (columnBeanIn.getDataType().equals("DNUMBER"))
                  {
//                        txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC + "/ -",columnBeanIn.getDisplaySize( )));
                        txtCell.setDocument(new LimitedPlainDocument(11));
                  }
                  else
                  {

                    //case 2296 end
                    if (columnBeanIn.getDataType().equals("UVARCHAR2"))
                    {
                        txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.UPPERCASE, maxLength));
                    }
                    else
                    {
                        if (columnBeanIn.getDataType().equals("MVARCHAR2"))//upcpercase with "." and "number"
                        {
                            txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.UPPERCASE + ". -" + JTextFieldFilter.NUMERIC, maxLength));
                        }
                        else
                        {
                            txtCell.setDocument( new LimitedPlainDocument( maxLength));
                        }
                    }
                  } //case 2296
                }
            }
                txtCell.addFocusListener(new FocusAdapter()
                    {
                       
                        public void focusGained(FocusEvent e)
                        {
                           // System.out.println("*** focus Gained ***") ; 
                        }
                       
                        public void focusLost(FocusEvent fe)
                        {
                            if (!fe.isTemporary())
                            {
//                                System.out.println("*** focus lost ***") ; 
                                GetLastValue(); 
                              }// end if
                            }// end focus lost
                        }); // end focus listener 
        }  
        /**
         * This method is overloaded to get the selected cell component in the
         * table.
         * @param table JTable instance for which component is derived
         * @param value object value.
         * @param isSelected particular table cell is selected or not
         * @param row row index
         * @param colunn column index
         * @return Component
         */
        
        public Component getTableCellEditorComponent(JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column)
        {
           selRow = row ;
            selColumn = column ;            
            
            TableColumn tColumn = codeTableColumnModel.getColumn(selColumn) ;
            ColumnBean columnBean = (ColumnBean)tColumn.getIdentifier() ;
              
//            if (tableStructureBean.getDisplayName().equalsIgnoreCase("Rule Functions Arguments") && columnBean.getColumnName().equalsIgnoreCase("FUNCTION_NAME"))
            if (tableStructureBean.getDisplayName().equalsIgnoreCase("Rule Functions Arguments") &&
                    columnBean.getColumnName().equalsIgnoreCase("FUNCTION_NAME") ||
                    (tableStructureBean.getDisplayName().equalsIgnoreCase(PROCEDURES)  &&
                    columnBean.getColumnName().equalsIgnoreCase(PROCEDURE_CAT_CODE_COL)) ||
                    //Added with COEUSQA-2667:User interface for setting up question details for procedure categories
                    (tableStructureBean.getActualName().equalsIgnoreCase(PROC_CAT_CUSTOM_DATA)  &&
                    columnBean.getColumnName().equalsIgnoreCase(PROCEDURE_CAT_CODE_COL)) ||
                    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
                    (tableStructureBean.getDisplayName().equalsIgnoreCase(LOCATION_NAME)  &&
                    columnBean.getColumnName().equalsIgnoreCase(LOCATION_TYPE_CODE_COL)) 
                    //COEUSQA:3005 - End
                    ){
                if (value  instanceof ComboBoxBean) {
                        ComboBoxBean cmbTmp = (ComboBoxBean)value ; 
                        Object objTmp = cmbTmp.getCode() ;
                        CoeusComboBox CompTmp = (CoeusComboBox)getComponent();
                        CompTmp.setSelectedItem(objTmp);
                        return (Component)CompTmp;
                       
                }  
                else
                {
                 return (Component)((CoeusComboBox)getComponent());
                }
            }
            //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
            //Based on the module, submodules are loaded
            else if (columnBean.getColumnName().equalsIgnoreCase(SUB_MODULE_CODE)){
                if (value  instanceof ComboBoxBean){
                    ComboBoxBean cmbTmp = (ComboBoxBean)value ;
                    Object objTmp = cmbTmp.getCode() ;
                    CoeusComboBox compTmp = (CoeusComboBox)getComponent();
                    HashMap hmTableColumnBean = tableStructureBean.getHashTableColumns();
                    int numColumnsForDisplay = tableStructureBean.getNumColumns();
                    int moduleCodeColumnIndex = 0;
                    if (numColumnsForDisplay > 0) {
                        for (int index = 0; index < numColumnsDisplay; index++) {
                            ColumnBean columnDisplayBean = (ColumnBean)hmTableColumnBean.get(new Integer(index)) ;
                            if(columnDisplayBean != null && columnDisplayBean.getColumnName().equals("MODULE_CODE")){
                                    moduleCodeColumnIndex = index;
                                    break;
                            }
                        }
                    }
                    if(moduleCodeColumnIndex > 0){
                        Object data = tblCodeTable.getValueAt(row,moduleCodeColumnIndex);
                        if(data instanceof ComboBoxBean){
                            compTmp =  populateSubModuleCode(((ComboBoxBean)data).getCode(),compTmp);
                        }
                        compTmp.setSelectedItem(objTmp);
                        return (Component)compTmp;
                    }else{
                        return (Component)compTmp;
                    }
                       
                }  
                else
                {
                 return (Component)((CoeusComboBox)getComponent());
                }
            }
            //COEUSDEV-86 : End
            //Added with COEUSQA-2667:User interface for setting up question details for procedure categories - Start
            //Based on the argument window, look ups are loaded
            else if (columnBean.getColumnName().equalsIgnoreCase(LOOKUP_ARG_COL)) {
                CoeusComboBox editingComponent = (CoeusComboBox)getComponent();
                editingComponent.removeAllItems();
                if (value  instanceof ComboBoxBean){
                    ComboBoxBean editingValue = (ComboBoxBean)value ;
                    Object objTmp = editingValue.getCode() ;
                    HashMap hmTableColumnBean = tableStructureBean.getHashTableColumns();
                    Object lookupWindow = null;
                    ColumnBean columnDisplayBean;
                    for (int index = 0; index < numColumnsDisplay; index++) {
                        columnDisplayBean = (ColumnBean)hmTableColumnBean.get(new Integer(index)) ;
                        if(columnDisplayBean != null && columnDisplayBean.getColumnName().equals(LOOKUP_WINDOW_COL)){
                            lookupWindow = tblCodeTable.getValueAt(row,index);
                            break;
                        }
                    }
                    if(lookupWindow!=null && lookupWindow instanceof ComboBoxBean){
                        populateLookUpArguments(((ComboBoxBean)lookupWindow).getCode(),editingComponent);
                    }
                    editingComponent.setSelectedItem(objTmp);
                // Added for COEUSQA-2520: Data Override functionality in Coeus Proposal Development -  Start
                }else if(value == null || CoeusGuiConstants.EMPTY_STRING.equals(value)){
                     HashMap hmTableColumnBean = tableStructureBean.getHashTableColumns();
                    Object lookupWindow = null;
                    ColumnBean columnDisplayBean;
                    for (int index = 0; index < numColumnsDisplay; index++) {
                        columnDisplayBean = (ColumnBean)hmTableColumnBean.get(new Integer(index)) ;
                        if(columnDisplayBean != null && columnDisplayBean.getColumnName().equals(LOOKUP_WINDOW_COL)){
                            lookupWindow = tblCodeTable.getValueAt(row,index);
                            break;
                        }
                    }
                    if(lookupWindow!=null && lookupWindow instanceof ComboBoxBean){
                        populateLookUpArguments(((ComboBoxBean)lookupWindow).getCode(),editingComponent);
                    }
                }
               
                // Added for COEUSQA-2520: Data Override functionality in Coeus Proposal Development -  End                
                return editingComponent;
            } else if (columnBean.getOptions()!= null ) {
//                return (Component)((CoeusComboBox)getComponent());
                CoeusComboBox editingCombo = (CoeusComboBox)getComponent();
                // Modified for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - Start
//                if (value  instanceof ComboBoxBean){
//                    ComboBoxBean cmbTmp = (ComboBoxBean)value ;
//                    Object objTmp = cmbTmp.getCode() ;
//                    editingCombo.setSelectedItem(objTmp);
//                }else if(editingCombo.getItemCount()>0){
//                    editingCombo.setSelectedIndex(0);
//                }
                if (value  instanceof ComboBoxBean){
                    ComboBoxBean cmbTmp = (ComboBoxBean)value ;
                    Object objTmp = cmbTmp.getCode() ;
                    editingCombo.setSelectedItem(objTmp);
                } else if(value instanceof String){
                    editingCombo.setSelectedItem(value);
                }else if(editingCombo.getItemCount()>0){
                    editingCombo.setSelectedIndex(0);
                }
                // Modified for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - End
                return editingCombo;
                //COEUSQA-2667 : End
            }else if (value != null){
//                System.out.println("*** setText in getTableCellEditorComponent ***" + value) ;
                txtCell.setText(value.toString().trim()) ;
            }else{  
                txtCell.setText(null) ;
            }
            return txtCell;
        }

        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return true if editing was stopped; false otherwise
         */
 
//        public boolean stopCellEditing() 
//        {
//            System.out.println("*** stopcellediting ***") ;
//            if (triggerValidation())
//            {    
//               return super.stopCellEditing();
//            }
//            else
//            { // returning false will not let the focus get out of the cell unless user
//              // keys in valid data  
//                tblCodeTable.changeSelection(selRow,  1, false, false) ; 
//              return false ;
//            }    
//        }
        //Added for COEUSDEV-86 : Questionnaire for a Submission -Start
        /*
         * Popup the subModule data based on the module code which is passed and loaded the sub modules to the ComboxBox
         * @param moduleCode
         * @param cmbSubModule
         * @return  cmbSubModule
         */
        private CoeusComboBox populateSubModuleCode(String moduleCode,CoeusComboBox cmbSubModule){
            if(moduleCode.equals(CoeusGuiConstants.EMPTY_STRING)){
                return new CoeusComboBox();
            }
            if(cmbSubModule != null && cmbSubModule.getItemCount() > 0){
                cmbSubModule.removeAllItems();
            }
            Map subModules =  (Map)hmModulesSubModules.get(moduleCode);
            for (Iterator it = subModules.keySet().iterator(); it.hasNext(); ) {
                String strKey =  it.next().toString() ;
                String description =  (String)subModules.get(new Integer(strKey));
                ComboBoxBean comboBoxBean = new ComboBoxBean(strKey, description);
                cmbSubModule.addItem(comboBoxBean) ;
            }
            return cmbSubModule;
        }
        //COEUSDEC-86 : End
        /** Returns the value contained in the editor.
         * @return Object the value contained in the editor
         */
               
        public Object getCellEditorValue() {
                    
//            System.out.println("*** get cell editor value:  ***"  +txtCell.getText()) ;
           
            Object oldValue = tblCodeTable.getValueAt(selRow,selColumn);
            Object newValue = ((CoeusTextField)txtCell).getText(); 
            
            TableColumn column = codeTableColumnModel.getColumn(selColumn) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            
//            if (oldValue instanceof ComboBoxBean)
            if (oldValue instanceof ComboBoxBean | columnBean.getOptions()!= null) //need another way to take care for not options drop dwon
            {
//                ComboBoxBean cmbTmp = (ComboBoxBean)oldValue ; 
//                oldValue = cmbTmp.getCode() ;
                newValue = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
            }
            if (!tableStructureBean.isIdAutoGenerated())
            {
            int colIdx1 = -1;
            int colIdx2 = -1;
            int colIdx3 = -1;
            Vector vecPrimaryKey = tableStructureBean.getPrimaryKeyIndex();
            if ( vecPrimaryKey.size() == 1)
            {
                colIdx1 = tableStructureBean.getPrimaryKeyIndex(0);
            }else if ( vecPrimaryKey.size() == 2 )
            {
                colIdx1 = tableStructureBean.getPrimaryKeyIndex(0);
                colIdx2 = tableStructureBean.getPrimaryKeyIndex(1);
            }else if ( vecPrimaryKey.size() == 3 )
            {
                colIdx1 = tableStructureBean.getPrimaryKeyIndex(0);
                colIdx2 = tableStructureBean.getPrimaryKeyIndex(1);
                colIdx3 = tableStructureBean.getPrimaryKeyIndex(2);
            }
            
            //Modified for Case#4021 - narrative code auto generated causes conflicts  - Start
            if (selRow > -1)
            {   
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                {
                    if (tblCodeTable.getValueAt(selRow,selColumn)!=null)
                    {
                        if (!(tblCodeTable.getValueAt(selRow,selColumn).toString().equals(((CoeusTextField)txtCell).getText())))
                        {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name 
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBean.getUserIndex() );
                            saveRequired = true;
//                            System.out.println("*** Set AcType to U  in getCellEditorValue***") ; 
                        }
                    }
                    else
                    { 
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name 
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBean.getUserIndex() );
                        saveRequired = true;
//                        System.out.println("*** Set AcType to U  in getCellEditorValue***") ; 
                    } 
                }
        
            }
            //Modified for Case#4021 - narrative code auto generated causes conflicts  - End
                if (selColumn == colIdx1 | selColumn == colIdx2 | selColumn == colIdx3)
                {        
                //Modified for COEUSQA-2667:User interface for setting up question details for procedure categories - Start
                //Check dependency for update and delete. Check uniqueid for insert and update.
                //Modified for Case#4021 - narrative code auto generated causes conflicts  - Start
                if(tblCodeTable.isEditing() && codeTableModel.getValueAt(sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1) != null){
//                     if(tableStructureBean.getActualName().equals("OSP$NARRATIVE_TYPE") && columnBean.getColumnName().equals("NARRATIVE_TYPE_CODE") ){
                    if(codeTableModel.getValueAt(sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1).equals("I")
                    || codeTableModel.getValueAt(sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1).equals("U")){
                        if(!CheckUniqueId(newValue.toString(), selRow, selColumn)) {
                            String msg = coeusMessageResources.parseMessageKey(
                                    "chkPKeyUniqVal_exceptionCode.2401");
                            CoeusOptionPane.showInfoDialog(msg);
                            return oldValue; //fail in uniqueid check
                        }
                    }if(codeTableModel.getValueAt(sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1).equals("U")
                    && !checkDependency(selRow, "")){
                        return oldValue;
                    }
                     //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
                     }else if(tableStructureBean.getActualName().equals("OSP$PROPOSAL_ATTACHMENT_TYPE") && 
                             columnBean.getColumnName().equals("ATTACHMENT_TYPE_CODE") ){
                         // Dependency check is not done during Insert ,it is required during updation and deletion
                         if(codeTableModel.getValueAt(sorter.getIndexForRow(selRow),
                                 codeTableModel.getColumnCount()-1).equals("I")
                            || codeTableModel.getValueAt(sorter.getIndexForRow(selRow), 
                                 codeTableModel.getColumnCount()-1).equals("U")){
                             if(!CheckUniqueId(newValue.toString(), selRow, selColumn)) {
                                 String msg = coeusMessageResources.parseMessageKey(
                                         "chkPKeyUniqVal_exceptionCode.2401");
                                 CoeusOptionPane.showInfoDialog(msg);
                                 return oldValue; //fail in uniqueid check
                             }
                         }if(codeTableModel.getValueAt(sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1).equals("U")
                             && !checkDependency(selRow, "")){
                             return oldValue;
                         }
                     //COEUSQA-1525 : End    
                }//else{
//                         if (checkDependency(selRow, "")) {
//                             if(!CheckUniqueId(newValue.toString(), selRow, selColumn)) {
//                                 String msg = coeusMessageResources.parseMessageKey(
//                                         "chkPKeyUniqVal_exceptionCode.2401");
//                                 CoeusOptionPane.showInfoDialog(msg);
//                                 return oldValue; //fail in uniqueid check
//                             }
//                         } else {
//                             return oldValue;//fail in dependency check
//                         }
//                     }
//                }
                //COEUSQA-2667:End


//                 if (checkDependency(selRow, ""))
//                 {
//                    if(!CheckUniqueId(newValue.toString(), selRow, selColumn))
//                    {
//                        String msg = coeusMessageResources.parseMessageKey(
//                            "chkPKeyUniqVal_exceptionCode.2401");
//               
//                        CoeusOptionPane.showInfoDialog(msg);                             
//                        return oldValue; //fail in uniqueid check
//                    }
//                 }
//                 else
//                 {
//                    return oldValue;//fail in dependency check
//                 }
                  //Modified for Case#4021 - narrative code auto generated causes conflicts  - End
                }
                    
            }
            // Case: 4198: Award Transaction Type is not saving - Start
            // UnCommeneted the Code
            // The Table id is Not autogenerated and if any of the row data is modified,
            // set the propoer value and actype 
            if (selRow > -1) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null) {
                    if (tblCodeTable.getValueAt(selRow,selColumn)!=null) {
                        if (!(tblCodeTable.getValueAt(selRow,selColumn).toString().equals(((CoeusTextField)txtCell).getText()))) {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBean.getUserIndex() );
                            saveRequired = true;
                        }
                    } else {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBean.getUserIndex() );
                        saveRequired = true;
                    }
                }
            }
            // Case: 4198: Award Transaction Type is not saving - End
 
//            TableColumn column = codeTableColumnModel.getColumn(selColumn) ;
//            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            // Modified for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - Start
//            if (columnBean.getOptions()!= null | (tableStructureBean.getDisplayName().equalsIgnoreCase("Rule Functions Arguments") && columnBean.getColumnName().equalsIgnoreCase("FUNCTION_NAME"))||
//                    (tableStructureBean.getDisplayName().equalsIgnoreCase(PROCEDURES)  && columnBean.getColumnName().equalsIgnoreCase(PROCEDURE_CAT_CODE_COL)) ||
//                    //Added with COEUSQA-2667:User interface for setting up question details for procedure categories
//                    (tableStructureBean.getActualName().equalsIgnoreCase(PROC_CAT_CUSTOM_DATA)  && columnBean.getColumnName().equalsIgnoreCase(PROCEDURE_CAT_CODE_COL)) ||
//                    (tableStructureBean.getActualName().equalsIgnoreCase(PROC_CAT_CUSTOM_DATA)  && columnBean.getColumnName().equalsIgnoreCase(LOOKUP_ARG_COL))) {
//                return (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
//            }
            if (columnBean.getOptions()!= null | (tableStructureBean.getDisplayName().equalsIgnoreCase("Rule Functions Arguments") && columnBean.getColumnName().equalsIgnoreCase("FUNCTION_NAME"))||
                    (tableStructureBean.getDisplayName().equalsIgnoreCase(PROCEDURES)  && columnBean.getColumnName().equalsIgnoreCase(PROCEDURE_CAT_CODE_COL)) ||
                    (tableStructureBean.getActualName().equalsIgnoreCase(PROC_CAT_CUSTOM_DATA)  && columnBean.getColumnName().equalsIgnoreCase(PROCEDURE_CAT_CODE_COL)) ||
                    ((tableStructureBean.getActualName().equalsIgnoreCase(PROC_CAT_CUSTOM_DATA) || 
                    "OSP$EPS_PROP_COLUMNS_TO_ALTER".equalsIgnoreCase(tableStructureBean.getActualName())) && columnBean.getColumnName().equalsIgnoreCase(LOOKUP_ARG_COL)) ||
                    //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
                    (tableStructureBean.getDisplayName().equalsIgnoreCase(LOCATION_NAME)  &&
                    columnBean.getColumnName().equalsIgnoreCase(LOCATION_TYPE_CODE_COL)) 
                    //COEUSQA:3005 - End
                    ) {
                return (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
            }
            // Modified for COEUSQA-2520: Data Override functionality in Coeus Proposal Development - End
            //Added for COEUSDEV-86 : Questionnaire for a Submission -Start
            if (columnBean.getColumnName().equalsIgnoreCase(SUB_MODULE_CODE)) {
                return (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
            }
            //COEUSDEV-86 : End
             return ((CoeusTextField)txtCell).getText();
             
        }

        /**
         * This method is over loaded to handle the item state changed events.
         * @param e ItemEvent
         */
        
        public void itemStateChanged(ItemEvent e) 
        {
//             System.out.println("*** get cell editor value:in itemStateChanged event  ***"  +txtCell.getText()) ;
             super.fireEditingStopped();
        }

//        /**
//         * Supporting method used to validate person Name for its correctness
//         * existance with db data.
//        */
//        // there will various types of triggervalidation functions which takes care
//        // of validation for different types of data.
//        private boolean triggerValidation()
//        {           
//            System.out.println("*** Validation row : " + selRow + " col : " + selColumn + " ***") ;
//          /*
//           * using the colIndex u can get the column bean for that column and then depending the 
//           * datatype of the column u apply appropriate validation rules
//           */
//            Object newValue = ((CoeusTextField)txtCell).getText(); 
//
//             if (!tableStructureBean.isIdAutoGenerated() 
//                && (selColumn == tableStructureBean.getPrimaryKeyIndex(0)))
//            {
//                if (checkDependency(selRow, ""))
//                {
//                    if(!CheckUniqueId(newValue.toString()))
//                    {
//                        String msg = coeusMessageResources.parseMessageKey(
//                            "chkPKeyUniqVal_exceptionCode.2401");
//               
//                        CoeusOptionPane.showInfoDialog(msg);
//                                
//                        return false; //fail in uniqueid check
//                    }
//                    else
//                    {
//                        return true;
//                    }
//                }
//                else
//                {
//                    return false;//fail in dependency check
//                }
//                    
//            }
//             else
//             {
//                 return true;
//             }
//                
//            
//        }
        
//        public Object getCellEditorValueForComboBox(int row, int col) 
//        {
//            selRow=row ;
//            selColumn=col ;
//            TableColumn column = codeTableColumnModel.getColumn(selColumn) ;
//            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
//            return (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
//        }
        

      } // end class

     
     
 class ExtendedDefaultTableModel extends DefaultTableModel   
 {
    
    public ExtendedDefaultTableModel()
    {
        
    }
   
     public boolean isCellEditable(int row, int col)
     {
        if (!userHasRights){
            return false;
        }else{
            TableColumn column = codeTableColumnModel.getColumn(col) ;
            ColumnBean columnBean = (ColumnBean) column.getIdentifier();
            //COEUSQA:1691 - Front End Configurations to CoeusLite Pages - Start
            if(ARRA_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName()) || BUDGET_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName())
                || IRB_PROTO_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName()) || AC_PROTO_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName())
                || NEGOTIATION_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName()) || PROPOSAL_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName())
                || SUB_CONTRACT_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName()) || COI_MESSAGE_PROPERTY.equals(tableStructureBean.getActualName())) {                
                
                if(accDataBean.getVectData() != null && !accDataBean.getVectData().isEmpty() && accDataBean.getVectData().size() < tblCodeTable.getRowCount() && row == accDataBean.getVectData().size()) {
                    return true;
                }
            }
            //COEUSQA:1691 - End
            return columnBean.getColumnEditable() ;
        }
        
     }
          
 }// end inner class
 
 private boolean CheckUniqueId(String newVal, int selRow, int selCol)
 {
    int rowTotal = tblCodeTable.getRowCount();
    int row = tblCodeTable.getEditingRow();
    if (rowTotal > 0 && row >= 0)
    {   
        Vector vecPrimaryKey = tableStructureBean.getPrimaryKeyIndex();
        if (vecPrimaryKey.size() == 1 )
        {
            int colIdx = tableStructureBean.getPrimaryKeyIndex(0) ;    
    
            for (int index = 0; index < rowTotal ; index++)
            {
                if (index != row)
                {
                    Object val = codeTableModel.getValueAt(sorter.getIndexForRow(index), colIdx);
                    if (val instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val ; 
//                        val = cmbTmp.getCode() ;
                        val = cmbTmp.getDescription();
                    }  
                    
                    if (val != null)
                    {
                        if (newVal.toString().trim().equals(val.toString().trim()))
                            return false;
                    }
                
                }
            }
        }
        else if  (vecPrimaryKey.size() == 2)
        {   
            int colIdx1 ;    
            int colIdx2 ; 
            //keep selected column number in colIdx1
            if (selCol == tableStructureBean.getPrimaryKeyIndex(0))
            {
                colIdx1 = tableStructureBean.getPrimaryKeyIndex(0) ;    
                colIdx2 = tableStructureBean.getPrimaryKeyIndex(1) ;  
            }
            else
            {
                colIdx1 = tableStructureBean.getPrimaryKeyIndex(1) ;    
                colIdx2 = tableStructureBean.getPrimaryKeyIndex(0) ;  
            }
            
            Object valTemp = codeTableModel.getValueAt(sorter.getIndexForRow(selRow), colIdx2);
            if ( valTemp instanceof ComboBoxBean)
            {
                ComboBoxBean cmbTmp = (ComboBoxBean)valTemp ; 
                valTemp = cmbTmp.getCode() ;
            }
            for (int index = 0; index < rowTotal ; index++)
            {
                if (index != row)
                {
                    Object val1 = codeTableModel.getValueAt(sorter.getIndexForRow(index), colIdx1);
                    Object val2 = codeTableModel.getValueAt(sorter.getIndexForRow(index), colIdx2);
                    if (val1 instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val1 ; 
//                        val1 = cmbTmp.getCode() ;
                        val1 = cmbTmp.getDescription();
                        
                    }  
                    
                    if (val2 instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val2 ; 
                        val2 = cmbTmp.getCode() ;
                        
                    }  
                    //case 1433 begin
                    //if (val1 != null && val2 != null) 
                    if (val1 != null && val2 != null && valTemp != null)
                    //case 1433 end
                    {
                        if (newVal.toString().trim().equals(val1.toString().trim()) && valTemp.toString().trim().equals(val2.toString().trim()))
                            return false;
                    }
                }
            }
        }
        else if  (vecPrimaryKey.size() == 3)
        {   
            int colIdx1 ;    
            int colIdx2 ; 
            int colIdx3 ; 
            //keep selected column number in colIdx1
            if (selCol == tableStructureBean.getPrimaryKeyIndex(0))
            {
                colIdx1 = tableStructureBean.getPrimaryKeyIndex(0) ;    
                colIdx2 = tableStructureBean.getPrimaryKeyIndex(1) ; 
                colIdx3 = tableStructureBean.getPrimaryKeyIndex(2) ;
            }
            else if (selCol == tableStructureBean.getPrimaryKeyIndex(1))
            {
                colIdx1 = tableStructureBean.getPrimaryKeyIndex(1) ;    
                colIdx2 = tableStructureBean.getPrimaryKeyIndex(0) ;
                colIdx3 = tableStructureBean.getPrimaryKeyIndex(2) ;
            }
            else
            {
                colIdx1 = tableStructureBean.getPrimaryKeyIndex(2) ;    
                colIdx2 = tableStructureBean.getPrimaryKeyIndex(0) ;
                colIdx3 = tableStructureBean.getPrimaryKeyIndex(1) ;
            }
            
            Object valTemp = codeTableModel.getValueAt(sorter.getIndexForRow(selRow), colIdx2);
            Object valTemp2 = codeTableModel.getValueAt(sorter.getIndexForRow(selRow), colIdx3);
            if ( valTemp instanceof ComboBoxBean)
            {
                ComboBoxBean cmbTmp = (ComboBoxBean)valTemp ; 
                valTemp = cmbTmp.getCode() ;
            }
            
            if ( valTemp2 instanceof ComboBoxBean)
            {
                ComboBoxBean cmbTmp = (ComboBoxBean)valTemp2 ; 
                valTemp2 = cmbTmp.getCode() ;
            }
            
            for (int index = 0; index < rowTotal ; index++)
            {
                if (index != row)
                {
                    Object val1 = codeTableModel.getValueAt(sorter.getIndexForRow(index), colIdx1);
                    Object val2 = codeTableModel.getValueAt(sorter.getIndexForRow(index), colIdx2);
                    Object val3 = codeTableModel.getValueAt(sorter.getIndexForRow(index), colIdx3);
                    
                    if (val1 instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val1 ; 
//                        val1 = cmbTmp.getCode() ;
                        val1 = cmbTmp.getDescription() ;//because the newVal is description
                    }  
                    
                    if (val2 instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val2 ; 
                        val2 = cmbTmp.getCode() ;
                    }  
                    if (val3 instanceof ComboBoxBean)
                    { 
                        ComboBoxBean cmbTmp = (ComboBoxBean)val3;
                        val3 = cmbTmp.getCode() ;
                    }  
                    
                    if (val1 != null && val2 != null && val3 != null && valTemp != null && valTemp2 != null)
                    {
                        if (newVal.toString().trim().equals(val1.toString().trim()) && 
                            valTemp.toString().trim().equals(val2.toString().trim()) &&
                            valTemp2.toString().trim().equals(val3.toString().trim()))
                            
                            return false;
                            
//                         if (newVal.toString().equals(val1.toString()))
//                         {
//                            
//                            if (valTemp.toString().equals(val2.toString()))
//                           {
//                               
//                             if (valTemp2.toString().equals(val3.toString()))
//                             {
//                                
//                                  return false;
//                             }
//                           }
//                         }
                         
                            
                           
                    }
                }
            }
        }
           
    }
  
    return true;
   
 }
 

private boolean checkDependency(int rowNumber, String from)
{
    HashMap hmDependencyCheckValues = new HashMap();
    int colNumber = tableStructureBean.getPrimaryKeyIndex(0);
    TableColumn column = codeTableColumnModel.getColumn(colNumber) ;
    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
    HashMap hmDependencyTables = (HashMap)tableStructureBean.getHashTableDependency();
    int depenTabNum; 
    //Added with COEUSQA-2667:User interface for setting up question details for procedure categories - Start
    if(PROC_CAT_CUSTOM_DATA.equals(tableStructureBean.getActualName())){
        return checkprocedureCategoryDependency(rowNumber,colNumber, from);
    }
    //Added with COEUSQA-2540:IRB/IACUC menbership role dependency - Start
    else if (IRB_MEMBERSHIP_ROLE.equals(tableStructureBean.getActualName())){
        return checkIRBMembershipRoleDependency(rowNumber,colNumber, from);
    }else if (IACUC_MEMBERSHIP_ROLE.equals(tableStructureBean.getActualName())){
        return checkIACUCMembershipRoleDependency(rowNumber,colNumber, from);
    }
     //Added with COEUSQA-2540:IRB/IACUC menbership role dependency - end
    //Added for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release _start
    else if (IACUC_SCHEDULE_ACT_ITEM.equals(tableStructureBean.getActualName())){
        return checkIACUCScheduleActionItemsDependency(rowNumber,colNumber, from);
    }else if (IRB_SCHEDULE_ACT_ITEM.equals(tableStructureBean.getActualName())){
        return checkIRBScheduleActionItemsDependency(rowNumber,colNumber, from);
    }
    //Added for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release _end
    else if (hmDependencyTables == null){    
        return true; //there is no dependency need to check
    }    
    //COEUSQA-2667:End
    depenTabNum = hmDependencyTables.size();
    for(int i = 0; i < depenTabNum ; i++)
    {
        HashMap hmDependency = (HashMap)hmDependencyTables.get(new Integer(i));
        hmDependencyCheckValues.put("a_table",hmDependency.get("Table"));  
        hmDependencyCheckValues.put("a_column",hmDependency.get("Column"));
        RequestTxnBean requestTxnBean = new RequestTxnBean();
    
        if (columnBean.getDataType().equals("NUMBER"))
        {
            //Modified for Case#4021 - narrative code auto generated causes conflicts  - Start
            if(tableStructureBean.getActualName().equals("OSP$NARRATIVE_TYPE") && columnBean.getColumnName().equals("NARRATIVE_TYPE_CODE") ){
                if(tblCodeTable.getValueAt(rowNumber, colNumber) == null 
                     || tblCodeTable.getValueAt(rowNumber, colNumber).toString().trim().equals("")){
                    return true;
                }else{
                    hmDependencyCheckValues.put("a_column_value",new Integer(tblCodeTable.getValueAt(rowNumber, colNumber).toString()));
                    requestTxnBean.setAction("DEPENDENCY_CHECK_INT");
                }
            
            //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start                
            }else   if(tableStructureBean.getActualName().equals("OSP$PROPOSAL_ATTACHMENT_TYPE") && 
                    columnBean.getColumnName().equals("ATTACHMENT_TYPE_CODE") ){
                if(tblCodeTable.getValueAt(rowNumber, colNumber) == null
                        || tblCodeTable.getValueAt(rowNumber, colNumber).toString().trim().equals("")){
                    return true;
            }else{
                    hmDependencyCheckValues.put("a_column_value",
                            new Integer(tblCodeTable.getValueAt(rowNumber, colNumber).toString()));
                    requestTxnBean.setAction("DEPENDENCY_CHECK_INT");
                }
            //COEUSQA-1525 : End    
            }else{
                hmDependencyCheckValues.put("a_column_value",new Integer(tblCodeTable.getValueAt(rowNumber, colNumber).toString()));
                requestTxnBean.setAction("DEPENDENCY_CHECK_INT");
            }            
//            hmDependencyCheckValues.put("a_column_value",new Integer(tblCodeTable.getValueAt(rowNumber, colNumber).toString()));
//            requestTxnBean.setAction("DEPENDENCY_CHECK_INT");
            //Modified for Case#4021 - narrative code auto generated causes conflicts  - End
         }
        else
        {
            hmDependencyCheckValues.put("a_column_value",tblCodeTable.getValueAt(rowNumber, colNumber).toString());
            requestTxnBean.setAction("DEPENDENCY_CHECK_VARCHAR2");
        }
    
        requestTxnBean.setDependencyCheck(hmDependencyCheckValues);
        Boolean success = (Boolean)getDataFromServlet(requestTxnBean) ;
        
        
        if (success.booleanValue() != true) 
        //there is dependency in other table.
        {
            String msg = coeusMessageResources.parseMessageKey(
                                "chkPKeyDependency_exceptionCode.2403");
            String msg1;

            if (from == "Delete")
            {
                msg1 = coeusMessageResources.parseMessageKey(
                                "chkPKeyDependency_exceptionCode.2404");
            }
            else
            {
                msg1 = coeusMessageResources.parseMessageKey(
                                "chkPKeyDependency_exceptionCode.2405");
            }
        
            String tableName =  hmDependency.get("Table").toString() ;   //tableStructureBean.getActualName();
            CoeusOptionPane.showInfoDialog(msg + tableName + msg1 );
            //once found the dependency, just return
            return false; 
        }
    }
    return true;
}

 private void GetLastValue()
 {
    int row = tblCodeTable.getEditingRow();
    int col = tblCodeTable.getEditingColumn();
    if (row != -1 && col != -1) 
    {
        tblCodeTable.getCellEditor(row, col).stopCellEditing();
        TableColumn column = codeTableColumnModel.getColumn(col) ;
        //make sure to set AC_TYPE
        column.getCellEditor().getCellEditorValue();
    }

 }
 

 //get the data from the stored procedure which is not in the xml file
  private void getRuleFunctionNames()
 {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
      DataBean accData = new DataBean();
      Vector vdata = null;
      HashMap htRow = new HashMap();
      try
      {
          RequestTxnBean requestTxnBean = new RequestTxnBean();
          requestTxnBean.setAction("GET_FUNCTION_LIST");
   
          RequesterBean requester = new RequesterBean();
          requester.setDataObject(requestTxnBean) ;
         
          String connectTo =CoeusGuiConstants.CONNECTION_URL
            + CODE_TABLE_SERVLET ;
          AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo,requester);
          comm.send();
        
          ResponderBean responder = comm.getResponse();
          if (responder.hasResponse())
         {
             accData = (DataBean)responder.getDataObject() ;
         }
          
          if(accData != null)
          {
            
            vdata = accData.getVectData();
          
          }
      }
      catch(Exception ex)
      {
        ex.printStackTrace() ;
        
      }  
           
            
      for (int i=0; i < vdata.size(); i++) //loop for num of rows
      {
            htRow = (HashMap)vdata.elementAt(i);
         
            hmFunctionNames.put(new Integer(i), (Object)htRow.get("FUNCTION_NAME"));
      }    
           
 } 
  
  //Added for COEUSDEV-86 : Questionnaire for a Submission -Start
  /*
   * Method to get submodules for the module
   * @param moduleCode
   * @return tmSubModules
   */
  private TreeMap getSubModulesForModule(String moduleCode){
      TreeMap tmSubModules = new TreeMap();
      DataBean accData = new DataBean();
      Vector vdata = null;
      HashMap htRow = new HashMap();
      try {
          RequestTxnBean requestTxnBean = new RequestTxnBean();
          requestTxnBean.setAction("GET_SUB_MODULES_FOR_MODULE");
          RequesterBean requester = new RequesterBean();
          requester.setDataObject(requestTxnBean) ;
          requester.setId(moduleCode);
          String connectTo =CoeusGuiConstants.CONNECTION_URL
                  + CODE_TABLE_SERVLET ;
          AppletServletCommunicator comm
                  = new AppletServletCommunicator(connectTo,requester);
          comm.send();
          
          ResponderBean responder = comm.getResponse();
          if (responder.hasResponse()) {
              accData = (DataBean)responder.getDataObject() ;
          }
          
          if(accData != null) {
              
              vdata = accData.getVectData();
              
          }
      } catch(Exception ex) {
          ex.printStackTrace() ;
          
      }
      for (int i=0; i < vdata.size(); i++) {
          htRow = (HashMap)vdata.elementAt(i);
          int subModuleCode = ((java.math.BigDecimal)htRow.get(SUB_MODULE_CODE)).intValue();
          tmSubModules.put(new Integer(subModuleCode), (Object)htRow.get("DESCRIPTION"));
      }
      tmSubModules.put(new Integer(0), NONE_ITEM_IN_COMBOBOX);
      return tmSubModules;
  }
  //COEUSDEV-86 : End
  //Added to get the IACUC procedures catgory -Start
  private Vector getProcedureCategoryCode() {
//        HashMap htRow = new HashMap();
        Vector vecProceCategoryType=new Vector();
        RequesterBean requester = new RequesterBean();
        RequestTxnBean requestTxnBean = new RequestTxnBean();
        try{
            requester.setDataObject(requestTxnBean) ;
            requestTxnBean.setAction("PROCEDURE_CATEGORY_DATA");
            String connectTo =CoeusGuiConstants.CONNECTION_URL
                    + CODE_TABLE_SERVLET ;
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            ResponderBean responder = comm.getResponse();
            if(responder.hasResponse()){
                DataBean dataBean = (DataBean)responder.getDataObject() ;
                vecProceCategoryType = dataBean.getVectData();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return vecProceCategoryType;
    }
    //Added to get the IACUC procedures catgory -End

  
  //Added with COEUSQA-2667:User interface for setting up question details for procedure categories - Start
  /*
   *  Method to get the list of look up arguments.
   *  The value of CoeusVector cvLookupArguments is set in this method
   *  The cvLookupArguments contains vector of ComboBoxBean
   *  with Lookup Window as code and Lookup Argument as description.
   */
  private void getLookUpArguments() {
//      HashMap htRow = new HashMap();
      RequesterBean requester = new RequesterBean();
      RequestTxnBean requestTxnBean = new RequestTxnBean();
      try{
          requester.setDataObject(requestTxnBean) ;
          requestTxnBean.setAction(LOOKUP_ARG_COL);
          String connectTo =CoeusGuiConstants.CONNECTION_URL
                  + CODE_TABLE_SERVLET ;
          AppletServletCommunicator comm
                  = new AppletServletCommunicator(connectTo,requester);
          comm.send();
          ResponderBean responder = comm.getResponse();
          if(responder.hasResponse()){
              DataBean dataBean = (DataBean)responder.getDataObject() ;
              cvLookupArguments = (CoeusVector)dataBean.getVectData();
          }
      }catch(Exception ex){
          ex.printStackTrace();
      }
  }
  
  /*
   * Method to populate the look up arguments combo box based on the lookup window selected.
   * @param lookUpWindowName The selected lookup window name.
   * @return  cmbArgLookUp The lookup arguments combobox.
   */
  private CoeusComboBox populateLookUpArguments(String lookUpWindowName,CoeusComboBox cmbArgLookUp){
      
      Equals args;
      if(lookUpWindowName!=null && !CoeusGuiConstants.EMPTY_STRING.equals(lookUpWindowName)){
          cmbArgLookUp.addItem(new ComboBoxBean("",""));
      }
      
      if("w_arg_code_tbl".equalsIgnoreCase(lookUpWindowName)){
          args = new Equals("code", "CODE_TABLE");
      }else if("w_arg_value_list".equalsIgnoreCase(lookUpWindowName)){
          args = new Equals("code", "ARG_TABLE");
      }else{
          args = new Equals("code", "Other");
      }
      CoeusVector  filteredData = cvLookupArguments.filter(args);
      if(filteredData != null && !filteredData.isEmpty()) {
          for(Object obj : filteredData ) {
              ComboBoxBean combo = (ComboBoxBean)obj;
              cmbArgLookUp.addItem(new ComboBoxBean(combo.getDescription(),combo.getDescription()));
          }
      }
      
      return cmbArgLookUp;
  }
  
  /*
   * Method to check the dependency for procedure category custom data
   * @param rowNumber - The selected row.
   * @param colNumber - The primary key column
   * @param from - Indication of from where this is invoked from
   * @return boolean false if the data has dependency.
   */
  private boolean checkprocedureCategoryDependency(int rowNumber,int colNumber, String from) {
      boolean hasDependency = false;
      //Get values
      Object columnName = tblCodeTable.getValueAt(rowNumber, colNumber);
      String columnNameValue = (columnName == null)?"":columnName.toString().trim();
      colNumber = tableStructureBean.getPrimaryKeyIndex(1);
      String categoryCode = ((ComboBoxBean)tblCodeTable.getValueAt(rowNumber, colNumber)).getCode();
      //prepare query
      String  ls_select = "select count(1) ";
      String  ls_from = "from osp$ac_proto_study_custom_data customdata, osp$ac_protocol_study_groups studygroup ";
      StringBuilder sbWhere = new StringBuilder("where ");
      sbWhere.append(" customdata.column_name = '");
      sbWhere.append(columnNameValue);
      sbWhere.append("' and studygroup.procedure_category_code = '");
      sbWhere.append(categoryCode);
      sbWhere.append("'");
      sbWhere.append(" and studygroup.protocol_number = customdata.protocol_number");
      sbWhere.append(" and studygroup.sequence_number = customdata.sequence_number");
      sbWhere.append(" and studygroup.study_group_id = customdata.study_group_id");
      //Check dependency
      HashMap hmDependencyCheckValues = new HashMap();
      hmDependencyCheckValues.put("as_select", ls_select);
      hmDependencyCheckValues.put("as_from", ls_from);
      hmDependencyCheckValues.put("as_where", sbWhere.toString());
      RequestTxnBean requestTxnBean = new RequestTxnBean();
      requestTxnBean.setAction("DEPENDENCY_CHECK_STATE");
      requestTxnBean.setDependencyCheck(hmDependencyCheckValues);
      Boolean success = (Boolean)getDataFromServlet(requestTxnBean) ;
      if(success.booleanValue()){
          hasDependency = true;
      }
      
      if(!hasDependency){
          //Dependency exist, show error message.
          String msg = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2403");
          String msg1 = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2405");
          if (from == "Delete") {
              msg1 = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2404");
          }
          CoeusOptionPane.showInfoDialog(msg + " osp$ac_proto_study_custom_data" + msg1 );
      }
      return hasDependency;
  }
  
  //New Method Added for COEUSQA-2540 Membership role Dependency Check-Start
  /*
   * Method to check the dependency for IACUC membership role data
   * @param rowNumber - The selected row.
   * @param colNumber - The primary key column
   * @param from - Indication of from where this is invoked from
   * @return boolean false if the data has dependency.
   */
   
  private boolean checkIACUCMembershipRoleDependency(int rowNumber,int colNumber, String from) {
      boolean hasDependency = false;
      //Get values
      String menmberShipRoleCode = tblCodeTable.getValueAt(rowNumber, colNumber).toString();
      
      //prepare query
      String  ls_select = "select count(1) ";
      String  ls_from = "from OSP$COMM_MEMBER_ROLES ";
      StringBuilder sbWhere = new StringBuilder("where ");
      sbWhere.append(" MEMBERSHIP_ID IN ( ");
       
      sbWhere.append(" SELECT DISTINCT OSP$COMM_MEMBERSHIPS.MEMBERSHIP_ID FROM OSP$COMM_MEMBERSHIPS, OSP$COMMITTEE ");
     
      sbWhere.append(" WHERE OSP$COMM_MEMBERSHIPS.COMMITTEE_ID = OSP$COMMITTEE.COMMITTEE_ID ");
      sbWhere.append("  AND OSP$COMMITTEE.COMMITTEE_TYPE_CODE = 2) ");
      sbWhere.append(" AND MEMBERSHIP_ROLE_CODE = '");
      sbWhere.append(menmberShipRoleCode);
      sbWhere.append("'");
      //Check dependency
      HashMap hmDependencyCheckValues = new HashMap();
      hmDependencyCheckValues.put("as_select", ls_select);
      hmDependencyCheckValues.put("as_from", ls_from);
      hmDependencyCheckValues.put("as_where", sbWhere.toString());
      RequestTxnBean requestTxnBean = new RequestTxnBean();
      requestTxnBean.setAction("DEPENDENCY_CHECK_STATE");
      requestTxnBean.setDependencyCheck(hmDependencyCheckValues);
      Boolean success = (Boolean)getDataFromServlet(requestTxnBean) ;
      if(success.booleanValue()){
          hasDependency = true;
      }
      
      if(!hasDependency){
          //Dependency exist, show error message.
          String msg = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2403");
          String msg1 = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2405");
          if (from == "Delete") {
              msg1 = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2404");
          }
          CoeusOptionPane.showInfoDialog(msg + " OSP$COMM_MEMBER_ROLES" + msg1 );
      }
      return hasDependency;
  }
  
  
  /*
   * Method to check the dependency for IRB membership role data
   * @param rowNumber - The selected row.
   * @param colNumber - The primary key column
   * @param from - Indication of from where this is invoked from
   * @return boolean false if the data has dependency.
   */
  private boolean checkIRBMembershipRoleDependency(int rowNumber,int colNumber, String from) {
      boolean hasDependency = false;
      //Get values
      String menmberShipRoleCode = tblCodeTable.getValueAt(rowNumber, colNumber).toString();
      
      //prepare query
      String  ls_select = "select count(1) ";
      String  ls_from = "from OSP$COMM_MEMBER_ROLES ";
      StringBuilder sbWhere = new StringBuilder("where ");
      sbWhere.append(" MEMBERSHIP_ID IN ( ");
       
      sbWhere.append(" SELECT DISTINCT OSP$COMM_MEMBERSHIPS.MEMBERSHIP_ID FROM OSP$COMM_MEMBERSHIPS, OSP$COMMITTEE ");
     
      sbWhere.append(" WHERE OSP$COMM_MEMBERSHIPS.COMMITTEE_ID = OSP$COMMITTEE.COMMITTEE_ID ");
      sbWhere.append("  AND OSP$COMMITTEE.COMMITTEE_TYPE_CODE = 1) ");
      sbWhere.append(" AND MEMBERSHIP_ROLE_CODE = '");
      sbWhere.append(menmberShipRoleCode);
      sbWhere.append("'");
      //Check dependency
      HashMap hmDependencyCheckValues = new HashMap();
      hmDependencyCheckValues.put("as_select", ls_select);
      hmDependencyCheckValues.put("as_from", ls_from);
      hmDependencyCheckValues.put("as_where", sbWhere.toString());
      RequestTxnBean requestTxnBean = new RequestTxnBean();
      requestTxnBean.setAction("DEPENDENCY_CHECK_STATE");
      requestTxnBean.setDependencyCheck(hmDependencyCheckValues);
      Boolean success = (Boolean)getDataFromServlet(requestTxnBean) ;
      if(success.booleanValue()){
          hasDependency = true;
      }
      
      if(!hasDependency){
          //Dependency exist, show error message.
          String msg = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2403");
          String msg1 = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2405");
          if (from == "Delete") {
              msg1 = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2404");
          }
          CoeusOptionPane.showInfoDialog(msg + " OSP$COMM_MEMBER_ROLES" + msg1 );
      }
      return hasDependency;
  }
  //New Method Added for COEUSQA-2540 Membership role Dependency Check-End
  
  //COEUSQA-2667 -End
  
  //Added for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release - Strat
  /*
   * Method to check the dependency for IRB Schedule Other Action Item data
   * @param rowNumber - The selected row.
   * @param colNumber - The primary key column
   * @param from - Indication of from where this is invoked from
   * @return boolean false if the data has dependency.
   */
  private boolean checkIRBScheduleActionItemsDependency(int rowNumber,int colNumber, String from) {
      boolean hasDependency = false;
      //Get values
      String scheduleActItemCode = tblCodeTable.getValueAt(rowNumber, colNumber).toString();
      
      //prepare query
      String  ls_select = "select count(1) ";
      String  ls_from = "from OSP$COMM_SCHEDULE_ACT_ITEMS ";
      StringBuilder sbWhere = new StringBuilder("where ");
      sbWhere.append(" SCHEDULE_ID IN ( ");
       
      sbWhere.append(" SELECT DISTINCT OSP$COMM_SCHEDULE.SCHEDULE_ID FROM OSP$COMM_SCHEDULE, OSP$COMMITTEE ");
     
      sbWhere.append(" WHERE OSP$COMM_SCHEDULE.COMMITTEE_ID = OSP$COMMITTEE.COMMITTEE_ID ");
      sbWhere.append("  AND OSP$COMMITTEE.COMMITTEE_TYPE_CODE = 1) ");
      sbWhere.append(" AND SCHEDULE_ACT_ITEM_TYPE_CODE = '");
      sbWhere.append(scheduleActItemCode);
      sbWhere.append("'");
      //Check dependency
      HashMap hmDependencyCheckValues = new HashMap();
      hmDependencyCheckValues.put("as_select", ls_select);
      hmDependencyCheckValues.put("as_from", ls_from);
      hmDependencyCheckValues.put("as_where", sbWhere.toString());
      RequestTxnBean requestTxnBean = new RequestTxnBean();
      requestTxnBean.setAction("DEPENDENCY_CHECK_STATE");
      requestTxnBean.setDependencyCheck(hmDependencyCheckValues);
      Boolean success = (Boolean)getDataFromServlet(requestTxnBean) ;
      if(success.booleanValue()){
          hasDependency = true;
      }
      
      if(!hasDependency){
          //Dependency exist, show error message.
          String msg = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2403");
          String msg1 = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2405");
          if (from == "Delete") {
              msg1 = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2404");
          }
          CoeusOptionPane.showInfoDialog(msg + " OSP$COMM_SCHEDULE_ACT_ITEMS" + msg1 );
      }
      return hasDependency;
  }
  
  /*
   * Method to check the dependency for IACUC Schedule Other Action Item data
   * @param rowNumber - The selected row.
   * @param colNumber - The primary key column
   * @param from - Indication of from where this is invoked from
   * @return boolean false if the data has dependency.
   */
  private boolean checkIACUCScheduleActionItemsDependency(int rowNumber,int colNumber, String from) {
      boolean hasDependency = false;
      //Get values
      String scheduleActItemCode = tblCodeTable.getValueAt(rowNumber, colNumber).toString();
      
      //prepare query
      String  ls_select = "select count(1) ";
      String  ls_from = "from OSP$COMM_SCHEDULE_ACT_ITEMS ";
      StringBuilder sbWhere = new StringBuilder("where ");
      sbWhere.append(" SCHEDULE_ID IN ( ");
       
      sbWhere.append(" SELECT DISTINCT OSP$COMM_SCHEDULE.SCHEDULE_ID FROM OSP$COMM_SCHEDULE, OSP$COMMITTEE ");
     
      sbWhere.append(" WHERE OSP$COMM_SCHEDULE.COMMITTEE_ID = OSP$COMMITTEE.COMMITTEE_ID ");
      sbWhere.append("  AND OSP$COMMITTEE.COMMITTEE_TYPE_CODE = 2) ");
      sbWhere.append(" AND SCHEDULE_ACT_ITEM_TYPE_CODE = '");
      sbWhere.append(scheduleActItemCode);
      sbWhere.append("'");
      //Check dependency
      HashMap hmDependencyCheckValues = new HashMap();
      hmDependencyCheckValues.put("as_select", ls_select);
      hmDependencyCheckValues.put("as_from", ls_from);
      hmDependencyCheckValues.put("as_where", sbWhere.toString());
      RequestTxnBean requestTxnBean = new RequestTxnBean();
      requestTxnBean.setAction("DEPENDENCY_CHECK_STATE");
      requestTxnBean.setDependencyCheck(hmDependencyCheckValues);
      Boolean success = (Boolean)getDataFromServlet(requestTxnBean) ;
      if(success.booleanValue()){
          hasDependency = true;
      }
      
      if(!hasDependency){
          //Dependency exist, show error message.
          String msg = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2403");
          String msg1 = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2405");
          if (from == "Delete") {
              msg1 = coeusMessageResources.parseMessageKey("chkPKeyDependency_exceptionCode.2404");
          }
          CoeusOptionPane.showInfoDialog(msg + " OSP$COMM_SCHEDULE_ACT_ITEMS" + msg1 );
      }
      return hasDependency;
  }
  //Added for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release - end
  
  //COEUSQA:3005 - Dependency between the Location Name and Location Type in IACUC - Start
  /*
   * Method to get all the locationtypes
   * @return vecLocationType
   */
  private Vector getLocationTypeCode() {
      Vector vecLocationType=new Vector();
      RequesterBean requester = new RequesterBean();
      RequestTxnBean requestTxnBean = new RequestTxnBean();
      try{
          requester.setDataObject(requestTxnBean) ;
          requestTxnBean.setAction("LOCATION_TYPE_DATA");
          String connectTo =CoeusGuiConstants.CONNECTION_URL
                  + CODE_TABLE_SERVLET ;
          AppletServletCommunicator comm
                  = new AppletServletCommunicator(connectTo,requester);
          comm.send();
          ResponderBean responder = comm.getResponse();
          if(responder.hasResponse()){
              DataBean dataBean = (DataBean)responder.getDataObject() ;
              vecLocationType = dataBean.getVectData();
          }
      }catch(Exception ex){
          ex.printStackTrace();
      }
      return vecLocationType;
  }
  //COEUSQA:3005 - End
}



