/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * AwardAmountTree.java
 *
 * Created on May 18, 2004, 12:21 PM
 */

package edu.mit.coeus.award.gui;

/**
 *
 * @author  sharathk
 */
/*
 *PMD Check performed and removed unused variables 
 *on 16 Apr 09 by keerthyjayaraj
 */

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.text.*;

import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.treetable.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.query.*;

/**
 * Manages a vector of AwardAmountInfobean viewed as s treetable.
 * sets acType as UPDATE_ANT_CHANGE if only Anticipated Change has been modified
 * sets acType as UPDATE_OBL_CHANGE if only Obligated Change has been modified
 * if both has been modified then sets TypeConstants.UPDATE_RECORD
 * if only any of the dates has been modified sets acType as UPDATE_DATE
 */
public class AwardAmountTreeTable extends javax.swing.JPanel implements ActionListener{
    
    // 3857 -- start
     /**Holds the column names*/
    private String colNames[] ;
    
    /**Holds the column class types*/
    private Class colClass[] ;
    
    /**Holds values for column editable*/
    private boolean colEditable[];
    
    /**Holds values for column visibility*/
    private boolean colVisible[];
    
    /**Holds Table Columns*/
    private TableColumn tableColumns[];
    
    // 3857 -- end
    
    private TableColumnModel awardAmountColumnModel;
    
    private AwardAmountRenderer awardAmountRenderer;
    
    private AwardAmountEditor awardAmountEditor;
    
    /** Holds Visible Columns*/
     private int visibleColumns[] ;
    //visibleColumns = new int[]{0, 3, 7, 9, 10, 11};
    
    //OBL -> Obligated
    //ANT -> Anticipated
    public static final int AWARD_AMT_TREE_COL = 0;
    public static final int OBL_DISTRIBUTED_COL = 1;
    public static final int OBL_DISTRIBUTABLE_COL = 2;
    public static final int OBL_CHANGE_COL = 3;
    public static final int OBL_TOTAL_COL = 4;
    public static final int ANT_DISTRIBUTED_COL = 5;
    public static final int ANT_DISTRIBUTABLE_COL = 6;
    public static final int ANT_CHANGE_COL = 7;
    public static final int ANT_TOTAL_COL = 8;
    public static final int OBL_EFF_DATE_COL = 9;
    public static final int OBL_EXP_DATE_COL = 10;
    //made public since will be used for validation in other screens and will have to set focus.
    public static final int FINAL_EXP_COL = 11;
    private static final int EMPTY_COL = 12;
    
     //  #Case 3857 -- start
    public static final int DIRECT_OBLIGATED_COL = 13;
    public static final int INDIRECT_OBLIGATED_COL = 14;
    public static final int DIRECT_ANTICIPATED_COL = 15;
    public static final int INDIRECT_ANTICIPATED_COL = 16;
    
//    private static final String DIRECT_OBLIGATED = "Direct";
//    private static final String INDIRECT_OBLIGATED = "Indirect";
//    private static final String DIRECT_ANTICIPATED = "Direct";
//    private static final String INDIRECT_ANTICIPATED = "Indirect";
    //html tag implementation of columnnames done with case 4543:Display distributed and distributable with dc/idc
    //Added for the case#4199-Brown's testing of indirect/direct award enhancement -start
    private static final String DIRECT_OBLIGATED_CHANGE    = "<html>Obl. Change<br>Direct</html>";
    private static final String INDIRECT_OBLIGATED_CHANGE  = "<html>Obl. Change<br>Indirect</html>";
    private static final String DIRECT_ANTICIPATED_CHANGE  = "<html>Ant. Change<br>Direct</html>";
    private static final String INDIRECT_ANTICIPATED_CHANGE= "<html>Ant. Change<br>Indirect</html>";
    //Added for the case#4199-Brown's testing of indirect/direct award enhancement -end
    private static final String DIRECT_OBLIGATED_TOTAL     = "<html>Obl. Total<br>Direct</html>";
    private static final String INDIRECT_OBLIGATED_TOTAL   = "<html>Obl. Total<br>Indirect</html>";
    private static final String DIRECT_ANTICIPATED_TOTAL   = "<html>Ant. Total<br>Direct</html>";
    private static final String INDIRECT_ANTICIPATED_TOTAL = "<html>Ant. Total<br>Indirect</html>";
    //4543 End
  //  #Case 3857 -- end
    
//    private static final int NODE_DIFF_WIDTH = 40;
    private static final int TREE_COL_WIDTH = 200;
    
    /**AC Type if only Obligated Change has Changed
     */
    public static final String UPDATE_OBL_CHANGE = "UPDATE_OBL_CHANGE";    
    
    /**AC Type if only Obligated Change has Changed
     */
    public static final String UPDATE_ANT_CHANGE = "UPDATE_ANT_CHANGE";
    
    /**AC Type if this bean has been Changed Indirectly
     */
    public static final String UPDATE_INDIRECT = "UPDATE_INDIRECT";
    
    /**AC Type if only any of the Dates has Changed
     */
    public static final String UPDATE_DATE = "UPDATE_DATE";
    
    
    public static final String UPDATED_LAST_TIME = "UPDATED_LAST_TIME";
    
    private int saveCount;
    
    private boolean editable = true;
    
    private boolean changeVisible = true;
    
    private String awardToColor;
    private Color awardColor;
    private int rowToColor = -1;
    
    /**This will hold the reference of the button other then the Date button
     *when the date button is selected. else will be null.
     */
    private JToggleButton btnOther;
    
    /**This will hold the reference of the second button (other then the btnOther)
     *when the date button is unselected. else will be null.
     */
    private JToggleButton btnSecond;
    
//    private static final String TOTAL_HEADER = "Total Amounts";
//    private static final String DISTRIBUTED_HEADER = "Distributed Amounts";
//    private static final String DISTRIBUTABLE_HEADER = "Distributable Amounts";
//    private static final String CHANGE_HEADER = "Amount Change";
//    private static final String DATE_HEADER = "Dates";
//    private static final String OBL_AMOUNT_HEADER = "Obligated Amount";
//    private static final String ANT_AMOUNT_HEADER = "Anticipated Amount";
    
    private static final String OBLIGATED = "Obligated";
    private static final String ANTICIPATED = "Anticipated";
    
//    private static final String DISTRIBUTED = "Distributed";
//    private static final String DISTRIBUTABLE = "Distributable";
//    private static final String TOTAL = "Total";
//    private static final String CHANGE = "Change";
    
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String EMPTY = "";
    
    private static final String INDIRECT_ENTRY = "I";
    private static final String DIRECT_ENTRY = "D";
    
    /** Please enter a valid Obligation Effective Date
     */
    private static final String INVALID_OBL_EFF_DATE = "awardMoneyAndEndDates_exceptionCode.1157";
    
    /** Please enter a valid Obligation Expiration Date
     */
    private static final String INVALID_OBL_EXP_DATE  = "awardMoneyAndEndDates_exceptionCode.1158";
    
    /** Please enter a valid Final Expiration Date
     */
    private static final String INVALID_FINAL_EXP_DATE = "awardMoneyAndEndDates_exceptionCode.1159";
    
    /** Obligated Total cannot be less than 0
     */
    private static final String OBL_TOT_LT_ZERO = "awardMoneyAndEndDates_exceptionCode.1160";
    
    /** Obligated Total cannot be less than distributed amount
     */
    private static final String OBL_TOT_LT_OBL_DIST = "awardMoneyAndEndDates_exceptionCode.1161";
    
    /** Insufficient funds at the parent.
     */
    private static final String INSUFF_FUNDS = "awardMoneyAndEndDates_exceptionCode.1162";
    
    /** Anticipated Total cannot be less than 0
     */
    private static final String ANT_TOT_LT_ZERO = "awardMoneyAndEndDates_exceptionCode.1163";
    
    /** Anticipated Total cannot be less than distributed amount
     */
    private static final String ANT_TOT_LT_ANT_DIST = "awardMoneyAndEndDates_exceptionCode.1164";
    
    private static final String ROOT_AWARD_NUM = "000000-000";
    
    public JTreeTable jttAwardAmount;
    private AwardAmountTreeTableModel awardAmountTreeTableModel;
    
    private AwardAmountInfoBean rootAwardAmount;
    
    private DateUtils dateUtils;
    
    private BevelBorder border;
    
    private SimpleDateFormat simpleDateFormat;
    
    private CoeusMessageResources coeusMessageResources;
    
    private CoeusVector cvAwardAmount;
    
    //Bug Fix:1410 Start 1
    private char functionType;
    //Bug Fix:1410 End 1
    
    //Bug Fix:1075  for moving the header when the table is moved
//    private JScrollBar horizantalScroll;
    
    
    //#3857 -- start
    // true if ENABLE_AWD_ANT_OBL_DIRECT_INDIRECT_COST =1, false otherwise.
    public static boolean SET_DITRECT_INDIRECT = false;
    
    // true if direct/indirect button is selected,false otherwise.
    private boolean isDirectIndirectSelected = false;

    //JIRA COEUSQA-2871 - START
    private int maxAccountNumberLength = -1;
    //JIRA COEUSQA-2871 - END
//    private String tmpHeader1 = null;
    
//    private String tmpHeader2 = null;
     //#3857 -- end
    /** Creates new form AwardAmountTree */
    public AwardAmountTreeTable() {
        initComponents();
        registerComponents();
        initializeVariables();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlButton = new javax.swing.JPanel();
        lblFooter = new javax.swing.JLabel();
        lblView = new javax.swing.JLabel();
        btnDates = new javax.swing.JToggleButton();
        btnTotal = new javax.swing.JToggleButton();
        btnDistributed = new javax.swing.JToggleButton();
        btnDistributable = new javax.swing.JToggleButton();
        btnChange = new javax.swing.JToggleButton();
        btnDirectInDirect = new javax.swing.JToggleButton();
        scrPnAwardAmount = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout(2, 2));

        setMinimumSize(new java.awt.Dimension(719, 56));
        setPreferredSize(new java.awt.Dimension(800, 48));
        pnlButton.setLayout(new java.awt.GridBagLayout());

        lblFooter.setFont(CoeusFontFactory.getNormalFont());
        lblFooter.setText("footer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        pnlButton.add(lblFooter, gridBagConstraints);

        lblView.setFont(CoeusFontFactory.getLabelFont());
        lblView.setText("View: ");
        pnlButton.add(lblView, new java.awt.GridBagConstraints());

        btnDates.setFont(CoeusFontFactory.getNormalFont());
        btnDates.setText("Dates");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlButton.add(btnDates, gridBagConstraints);

        btnTotal.setFont(CoeusFontFactory.getNormalFont());
        btnTotal.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlButton.add(btnTotal, gridBagConstraints);

        btnDistributed.setFont(CoeusFontFactory.getNormalFont());
        btnDistributed.setText("Distributed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlButton.add(btnDistributed, gridBagConstraints);

        btnDistributable.setFont(CoeusFontFactory.getNormalFont());
        btnDistributable.setText("Distributable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlButton.add(btnDistributable, gridBagConstraints);

        btnChange.setFont(CoeusFontFactory.getNormalFont());
        btnChange.setText("Change");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlButton.add(btnChange, gridBagConstraints);

        btnDirectInDirect.setText("Direct/Indirect");
        btnDirectInDirect.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 140);
        pnlButton.add(btnDirectInDirect, gridBagConstraints);

        add(pnlButton, java.awt.BorderLayout.SOUTH);

        add(scrPnAwardAmount, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    public void setSaveCount(int saveCount) {
        this.saveCount = saveCount;
    }
    
    public void editCellFor(AwardAmountInfoBean awardAmountInfoBean, int column) {
        if(jttAwardAmount != null) {
            gotoMITAwardNumber(awardAmountInfoBean.getMitAwardNumber());
            int selectedRow = jttAwardAmount.getSelectedRow();
            if(selectedRow == -1) return ;
            
            jttAwardAmount.editCellAt(selectedRow, column);
            awardAmountEditor.requestFocus(column);
            
        }
    }
    
    /** will select the row with this mit award number if present.
     */
    public void gotoMITAwardNumber(String mitAwardNumber){
        Equals eqMitAwardNum = new Equals("mitAwardNumber", mitAwardNumber);
        selectBean(eqMitAwardNum);
    }
    
    /**will select the row with this account number if present
     */
    public void gotoAccountNumber(String accountNumber) {
        Equals eqAccountdNum = new Equals("accountNumber", accountNumber);
        selectBean(eqAccountdNum);
    }
    
    private void selectBean(Operator operator) {
        CoeusVector cvBeanPath = cvAwardAmount.filter(operator);
        //check if present
        if(cvBeanPath == null || cvBeanPath.size() == 0) {
            //not present wrong MIT award number
            return ;
        }
        //present. prepare treepath and select this bean.
        AwardAmountInfoBean awardAmountInfoBean;
        Vector vecPath = new Vector();
        awardAmountInfoBean = (AwardAmountInfoBean)cvBeanPath.get(0);//will always return a valid bean.
        vecPath.add(awardAmountInfoBean);
        while(! awardAmountInfoBean.getParentMitAwardNumber().equals(ROOT_AWARD_NUM)) {
            Equals eqParentNum = new Equals("mitAwardNumber", awardAmountInfoBean.getParentMitAwardNumber());
            cvBeanPath = cvAwardAmount.filter(eqParentNum);
            //should return a valid value since its viewwed as a tree.
            //Added for case#2308 - Multiple Award open in display and edit mode - start
            if(cvBeanPath == null || cvBeanPath.size() == 0){
                break;
            }
            //Added for case#2308 - Multiple Award open in display and edit mode - end
            awardAmountInfoBean = (AwardAmountInfoBean)cvBeanPath.get(0);
            vecPath.add(0, awardAmountInfoBean);            
        }//End while
        
        TreePath path = new TreePath(vecPath.toArray());
        jttAwardAmount.getTree().setSelectionPath(path);
        jttAwardAmount.getTree().addSelectionPath(path);
        
        int row = jttAwardAmount.getSelectedRow();
        //Added for COEUSDEV-283 : Award Money and End Dates: change ob exp date and press save; last change amount fills in on the line - Start
        if(row < 0){
            return;
        }
        //COEUSDEV-283 : End
        Rectangle rect = jttAwardAmount.getCellRect(row, AWARD_AMT_TREE_COL, true);
        jttAwardAmount.scrollRectToVisible(rect);
        
    }
    
    // If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void expandAll(boolean expand) {
        JTree tree = jttAwardAmount.getTree();
        AwardAmountInfoBean root = (AwardAmountInfoBean)tree.getModel().getRoot();
        
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
    
    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        AwardAmountInfoBean node = (AwardAmountInfoBean)parent.getLastPathComponent();
        if (node.getChildCount() > 0) {
            CoeusVector children = node.getChildren();
            for (int index = 0; index < children.size(); index++) {
                AwardAmountInfoBean n = (AwardAmountInfoBean)children.get(index);
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
    
    
    /**returns treepath for displayed tree structure
     */
    public AwardAmountInfoBean getBeanForRow(int row) {
        AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)jttAwardAmount.getTree().getPathForRow(row).getLastPathComponent();
        return awardAmountInfoBean;
    }
    
    /**returns the row count
     */
    public int getRowCount() {
        return jttAwardAmount.getRowCount();
    }
    //Bug fix:1410 Start 2
    //Writing a overridden method.
    public void setBeans(CoeusVector cvAwardAmount , AwardAmountInfoBean rootAwardAmount,char functionType){
        //Bug fix:1410 
        this.functionType = functionType;
        //Bug fix:1410
        
        setBeans(cvAwardAmount, rootAwardAmount);
    }
    
    public void setBeans(CoeusVector cvAwardAmount, AwardAmountInfoBean rootAwardAmount) {
        this.cvAwardAmount = cvAwardAmount;
        this.rootAwardAmount = rootAwardAmount;
        
        
        if(cvAwardAmount == null || cvAwardAmount.size() < 1) {
            return ;
        }
        
        int size = cvAwardAmount.size();
        if(size == 1) {
            lblFooter.setText(size+" Award in hierarchy");
        }else {
            lblFooter.setText(size+" Awards in hierarchy");
        }
        
        if(jttAwardAmount == null) {
            awardAmountTreeTableModel = new AwardAmountTreeTableModel(cvAwardAmount, rootAwardAmount);
            
            //jttAwardAmount = new JTreeTable(awardAmountTreeTableModel);
            
            //Added for key traversal - START
            jttAwardAmount = new JTreeTable(awardAmountTreeTableModel){
                public void changeSelection(int row, int column, boolean toggle, boolean extend){
                    super.changeSelection(row, column, toggle, extend);
                    javax.swing.SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            jttAwardAmount.dispatchEvent(new java.awt.event.KeyEvent(
                            jttAwardAmount,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                        }
                    });
                }
            };
            
            jttAwardAmount.addMouseListener(new MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    javax.swing.SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            jttAwardAmount.dispatchEvent(new java.awt.event.KeyEvent(
                            jttAwardAmount,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                            java.awt.event.KeyEvent.CHAR_UNDEFINED) );
                        }
                    });
                }});
                //Added for key traversal - START
                
                //jttAwardAmount.getTableHeader().setSize(jttAwardAmount.getTableHeader().getWidth(), 22);
                jttAwardAmount.getTableHeader().setReorderingAllowed(false);
                jttAwardAmount.setAutoResizeMode(JTreeTable.AUTO_RESIZE_OFF);
                jttAwardAmount.getTree().setCellRenderer(new AmountTreeCellRenderer());
                JScrollPane scrPnAwardAmount = new JScrollPane(jttAwardAmount);
                
                //Bug Fix:1075 Start for moving the header when the table is moved
//                jttAwardAmount.getColumnModel().addColumnModelListener(this);
//                horizantalScroll = scrPnAwardAmount.getHorizontalScrollBar();
//                horizantalScroll.addAdjustmentListener(this);
                //Bug Fix:1075 End for moving the header when the table is moved:End
                
                add(scrPnAwardAmount, java.awt.BorderLayout.CENTER);
                postInitComponents();
                
        }else {
            
            Equals eqMitAward = new Equals("mitAwardNumber", rootAwardAmount.getMitAwardNumber());
            this.rootAwardAmount = (AwardAmountInfoBean)cvAwardAmount.filter(eqMitAward).get(0);
            awardAmountTreeTableModel.setRoot(this.rootAwardAmount);
            //awardAmountTreeTableModel.setRoot(new DefaultMutableTreeNode(this.rootAwardAmount));
            boolean selectedRow = false;
            if(cvAwardAmount!= null && cvAwardAmount.size() > 0){
                selectedRow = true;
            }
            
            awardAmountTreeTableModel.setData(cvAwardAmount);
            awardAmountTreeTableModel.reload();
            ((AbstractTableModel)jttAwardAmount.getModel()).fireTableDataChanged();
            setFocusInThread(selectedRow,0);//4524
//            SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    if(selectedRow){
//                        jttAwardAmount.setRowSelectionInterval(0,0);
//                        jttAwardAmount.getTree().setSelectionInterval(0,0);
//                    }
//                }
//            });
        }
        
    }
    //Method parameters modified with case 4524:Money and End Dates in BIG Structure 
    public  void setFocusInThread(final boolean selrow ,final int selectedRow){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Modified for COEUSDEV-283 : Award Money and End Dates: change ob exp date and press save; last change amount fills in on the line - Start
//                if(selrow){
                if(selrow && selectedRow > -1){//COEUSDEV-283 : End
                jttAwardAmount.setRowSelectionInterval(selectedRow,selectedRow);
                jttAwardAmount.getTree().setSelectionInterval(selectedRow,selectedRow);
               }
            }
        });
    }
    //4524 End
    public boolean isValueSet() {
        return awardAmountTreeTableModel.isValueSet();
    }
    
    public void resetValue() {
        awardAmountTreeTableModel.resetValue();
    }
    
    public CoeusVector getBeans() {
        if(cvAwardAmount == null || cvAwardAmount.size() == 0) {
            return null;
        }
        awardAmountEditor.stopCellEditing();
        
        return cvAwardAmount;
    }
    
    // Added for COEUSQA-3394 : unable to save award transaction type when certain changes are made to award detail tab - Start    
    /*
     * Method to get the AwardAmountTreeTableModel data
     * return CoeusVector
     */
    public CoeusVector getModelData() {
       return awardAmountTreeTableModel.getData();
    }
    
    /*
     * Method to upadte AwardAmountTreeTableModel
     */
    public void updateModelData(CoeusVector cvAwardAmountUpdate ,String mitAwardNumber) {
        if(cvAwardAmountUpdate != null && !cvAwardAmountUpdate.isEmpty()){
            this.cvAwardAmount = this.cvAwardAmount;
            awardAmountTreeTableModel.updateAmountInfo(cvAwardAmountUpdate);
            awardAmountTreeTableModel.reload();
        }
       
    }
    // Added for COEUSQA-3394 : unable to save award transaction type when certain changes are made to award detail tab - End
    
    // 3857 -- start
    /*Selects Direct/indirect button when the frame first loads */
     private void initSettings() {
        if(SET_DITRECT_INDIRECT) {
            btnDirectInDirect.setSelected(true);
            clickDirectIndirect();
        }       
    }
     
     /*Add Direct indirect column to money and end dates      */
      private void clickDirectIndirect() {
          int newVisibleColumns[] = new int[10];
          newVisibleColumns[0] = visibleColumns[0];
          newVisibleColumns[1] = DIRECT_OBLIGATED_COL;
          newVisibleColumns[2] = INDIRECT_OBLIGATED_COL;
          newVisibleColumns[3] = visibleColumns[1];
          newVisibleColumns[4] = DIRECT_ANTICIPATED_COL;
          newVisibleColumns[5] = INDIRECT_ANTICIPATED_COL;
          newVisibleColumns[6] = visibleColumns[2];
          newVisibleColumns[7] = visibleColumns[3];
          newVisibleColumns[8] = visibleColumns[4];
          newVisibleColumns[9] = visibleColumns[5];
          visibleColumns = newVisibleColumns;
          
          jttAwardAmount.getColumnModel().addColumn(tableColumns[13]);
          jttAwardAmount.getColumnModel().addColumn(tableColumns[14]);
          jttAwardAmount.getColumnModel().addColumn(tableColumns[15]);
          jttAwardAmount.getColumnModel().addColumn(tableColumns[16]);
          
          jttAwardAmount.getColumnModel().moveColumn(6, 1);
          jttAwardAmount.getColumnModel().moveColumn(7, 2);
          jttAwardAmount.getColumnModel().moveColumn(8, 4);
          jttAwardAmount.getColumnModel().moveColumn(9, 5);

         setIsDirectIndirectSelected(true);
    }
      // 3857 -- end
      
    private void postInitComponents() {
        
        dateUtils = new DateUtils();
        jttAwardAmount.setRowHeight(20);
        jttAwardAmount.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        jttAwardAmount.setSelectionBackground(UIManager.getDefaults().getColor("Panel.background"));
        
        border = new BevelBorder(BevelBorder.LOWERED, Color.white, Color.lightGray, Color.gray, Color.lightGray);
        
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        
        coeusMessageResources = CoeusMessageResources.getInstance();

        jttAwardAmount.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        jttAwardAmount.getTree().setExpandsSelectedPaths(true);
        
        //setting Table Columns
        TableColumn tableColumn;
        
        awardAmountRenderer = new AwardAmountRenderer();
        awardAmountEditor = new AwardAmountEditor();
        //3857 -- start
      //  int colSize[] = {TREE_COL_WIDTH, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};
        int colSize[] ;
        if(SET_DITRECT_INDIRECT) {
            colSize = new int[] {TREE_COL_WIDTH, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,100,100,100,100};
        } else {
            colSize = new int[] {TREE_COL_WIDTH, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};
        }
      //3857 -- end
     
        for(int col = 0; col < colSize.length; col++) {
            tableColumn = jttAwardAmount.getColumnModel().getColumn(col);
            tableColumn.setPreferredWidth(colSize[col]);
            //Commented with case 4543: Money and End Dates UI Changes
            //Bug Fix for setting the min amd max size for table:Start
//           if(col !=0){
//                tableColumn.setMinWidth(colSize[col]);
//                tableColumn.setMaxWidth(colSize[col]);
//            }
            //Bug Fix for setting the min amd max size for table:End
            
            tableColumns[col] = tableColumn;
            if(!colClass[col].equals(TreeTableModel.class)){
                tableColumn.setCellRenderer(awardAmountRenderer);
                tableColumn.setCellEditor(awardAmountEditor);
                //Added with case 4543: Money and End Dates UI Changes
                tableColumn.setHeaderRenderer(new DefaultTableCellRenderer() {
                    
                    public Component getTableCellRendererComponent(JTable table, Object value,
                            boolean isSelected, boolean hasFocus, int row, int column) {
                        setFont(table.getTableHeader().getFont());
                        setText((value == null) ? "" : value.toString());
                        setPreferredSize(new Dimension(50,35));
                        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                        setHorizontalAlignment(JLabel.CENTER);
                        return this;
                    }
                    
                });
            //4543 End
            }
        }
      
        awardAmountColumnModel = jttAwardAmount.getColumnModel();
        jttAwardAmount.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        jttAwardAmount.getColumnModel().getColumn(EMPTY_COL).setCellRenderer(new EmptyRenderer());
        
        int column = 0;
        
        for(int index = 0; index < colSize.length; index++) {
            if(colVisible[index] == false) {
                tableColumn = jttAwardAmount.getColumnModel().getColumn(column);
                jttAwardAmount.removeColumn(tableColumn);
            }else {
                column++;
            }
        }
        btnDates.setSelected(true);
        btnChange.setSelected(true);
        
        btnOther = btnChange;
        
        visibleColumns = new int[]{0, 3, 7, 9, 10, 11};
        // 3857 -- start
        initSettings();
         // 3857 -- end
        //Commented with case 4543: Money and End Dates UI Changes
//        setLableHeader1(CHANGE_HEADER);
//        setLableHeader2(DATE_HEADER);
//        setLableHeader(isDirectIndirectSelected);
        //4543 End
    }
    
    private void registerComponents() {
        btnChange.addActionListener(this);
        btnDates.addActionListener(this);
        btnDistributable.addActionListener(this);
        btnDistributed.addActionListener(this);
        btnTotal.addActionListener(this);
        btnDirectInDirect.addActionListener(this);
    }
    
    /**For Testing purpose only
     */
    public static void main(String s[]) {
        CoeusVector cvData = new CoeusVector();
        AwardAmountInfoBean bean;
        
        String rootMitAwardNum = "100";
        java.sql.Date date = new java.sql.Date(100);
        
        int status[] = {1, 3, 6, 10,1, 3, 6, 10,1, 3, 6, 10};
        
        for(int count = 0; count < 10; count++) {
            bean = new AwardAmountInfoBean();
            bean.setMitAwardNumber(""+count);
            bean.setRootMitAwardNumber(rootMitAwardNum);
            if(count == 0) {
                bean.setParentMitAwardNumber(ROOT_AWARD_NUM);
            }else {
                bean.setParentMitAwardNumber(""+(count - 1));
            }
            
            bean.setAmountObligatedToDate(count);
            bean.setAmountSequenceNumber(count);
            bean.setAnticipatedChange(count);
            bean.setAnticipatedDistributableAmount(count);
            bean.setAnticipatedTotalAmount(count);
            bean.setCurrentFundEffectiveDate(date);
            bean.setEffectiveDate(date);
            if(count > 5) {
                bean.setFinalExpirationDate(date);
            }
            bean.setObliDistributableAmount(count);
            bean.setObligatedChange(count);
            bean.setObligationExpirationDate(date);
            
            bean.setAccountNumber("123456");
            bean.setSequenceNumber(1);
            
            bean.setStatusCode(status[count]);
            
            cvData.add(bean);
        }
        
        AwardAmountTreeTable aatt = new AwardAmountTreeTable();
        aatt.setBeans(cvData, (AwardAmountInfoBean)cvData.get(0),'P');
        
        //aatt.setEditable(false);
        //aatt.setSelectionEnabled(false);
        //aatt.setChangeVisible(false);
        
        JFrame jf = new JFrame();
        jf.getContentPane().add(aatt);
        jf.setSize(400, 400);
        jf.setVisible(true);
        
        aatt.gotoMITAwardNumber(""+5);
        
        aatt.awardAmountTreeTableModel.setRoot(aatt.rootAwardAmount);
        aatt.awardAmountTreeTableModel.setData(cvData);
        aatt.awardAmountTreeTableModel.reload();
        //((AbstractTableModel)aatt.jttAwardAmount.getModel()).fireTableDataChanged();
        aatt.gotoMITAwardNumber(""+5);
        
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        
        //commit changes if any. if any error occurs return back.
        awardAmountEditor.stopCellEditing();
        if(!awardAmountTreeTableModel.isValueSet()) {
            //some validation error occured.
            awardAmountTreeTableModel.resetValue();
            JToggleButton btnToggle = (JToggleButton)source;
            btnToggle.setSelected(!btnToggle.isSelected());
            return ;
        }
        
        if(source.equals(btnChange)) {
           //3857 -- start
            if(SET_DITRECT_INDIRECT && isDirectIndirectSelected) {
                
                clickedDirectInDirect(btnChange);
            } else {
                clicked(btnChange);
            }
           //3857 -- end
        }else if(source.equals(btnDates)){
            //Commented with case 4543: Money and End Dates UI Changes
            //change column names
//            TableColumn tableColumn;
//            if(btnDates.isSelected()) {
//                tableColumn = tableColumns[OBL_CHANGE_COL];
//                tableColumn.setHeaderValue(OBLIGATED);
//                tableColumn = tableColumns[OBL_DISTRIBUTABLE_COL];
//                tableColumn.setHeaderValue(OBLIGATED);
//                tableColumn = tableColumns[OBL_DISTRIBUTED_COL];
//                tableColumn.setHeaderValue(OBLIGATED);
//                tableColumn = tableColumns[OBL_TOTAL_COL];
//                tableColumn.setHeaderValue(OBLIGATED);
//                
//                tableColumn = tableColumns[ANT_CHANGE_COL];
//                tableColumn.setHeaderValue(ANTICIPATED);
//                tableColumn = tableColumns[ANT_DISTRIBUTABLE_COL];
//                tableColumn.setHeaderValue(ANTICIPATED);
//                tableColumn = tableColumns[ANT_DISTRIBUTED_COL];
//                tableColumn.setHeaderValue(ANTICIPATED);
//                tableColumn = tableColumns[ANT_TOTAL_COL];
//                tableColumn.setHeaderValue(ANTICIPATED);
//            }else {
//                tableColumn = tableColumns[OBL_CHANGE_COL];
//                tableColumn.setHeaderValue(CHANGE);
//                tableColumn = tableColumns[OBL_DISTRIBUTABLE_COL];
//                tableColumn.setHeaderValue(DISTRIBUTABLE);
//                tableColumn = tableColumns[OBL_DISTRIBUTED_COL];
//                tableColumn.setHeaderValue(DISTRIBUTED);
//                tableColumn = tableColumns[OBL_TOTAL_COL];
//                tableColumn.setHeaderValue(TOTAL);
//                
//                tableColumn = tableColumns[ANT_CHANGE_COL];
//                tableColumn.setHeaderValue(CHANGE);
//                tableColumn = tableColumns[ANT_DISTRIBUTABLE_COL];
//                tableColumn.setHeaderValue(DISTRIBUTABLE);
//                tableColumn = tableColumns[ANT_DISTRIBUTED_COL];
//                tableColumn.setHeaderValue(DISTRIBUTED);
//                tableColumn = tableColumns[ANT_TOTAL_COL];
//                tableColumn.setHeaderValue(TOTAL);
//            }
            //4543 End
             //3857 -- start
            if(SET_DITRECT_INDIRECT && isDirectIndirectSelected) {
                dateClickDirectInDirect();
            } else {
                dateClick();
            }
            //3857 -- end
            
            if(!changeVisible) {
                if(btnDates.isSelected()) {
                    btnTotal.doClick();
                }else {
                    if(btnTotal.isSelected() && btnChange.isSelected()) {
                        btnChange.doClick();
                        btnDistributable.doClick();
                    }else if(btnChange.isSelected()) {
                        btnChange.doClick();
                        btnTotal.doClick();
                    }
                }
            }
        //Modified with case 4543: Distributed and Distributable views in dc/idc   
        }else if(source.equals(btnDistributable)) {
             //3857 -- start
            if(SET_DITRECT_INDIRECT && isDirectIndirectSelected) {
//                btnDistributable.setSelected(false);
                clickedDirectInDirect(btnDistributable);
            } else {
                clicked(btnDistributable);
            }
            //3857 -- end
        }else if(source.equals(btnDistributed)) {
             //3857 -- start
            if(SET_DITRECT_INDIRECT && isDirectIndirectSelected) {
//                btnDistributed.setSelected(false);
                clickedDirectInDirect(btnDistributed);
            } else {
                clicked(btnDistributed);
            }
            //3857 -- end
            //4543 End
        }else if(source.equals(btnTotal)) {
             //3857 -- start
            if(SET_DITRECT_INDIRECT && isDirectIndirectSelected) {
                clickedDirectInDirect(btnTotal);
            } else {
                clicked(btnTotal);
            }
        
        } else if(source.equals(btnDirectInDirect)) {
            directInDirectAction();
        } //3857 -- end
        //Added with case 4543: Money and End Dates UI Changes
        if(SET_DITRECT_INDIRECT && isDirectIndirectSelected) {
            resetColumnHeaders();
        }
        //4543 End
    }
   
    //Added with case 4543: Money and End Dates UI Changes
    private void resetColumnHeaders(){
        //Change the header labels
        if(btnTotal.isSelected() ||btnDistributed.isSelected() || btnDistributable.isSelected()){
            TableColumn tableColumn;
            tableColumn = tableColumns[DIRECT_OBLIGATED_COL];
            tableColumn.setHeaderValue(DIRECT_OBLIGATED_TOTAL);
            tableColumn = tableColumns[INDIRECT_OBLIGATED_COL];
            tableColumn.setHeaderValue(INDIRECT_OBLIGATED_TOTAL);
            tableColumn = tableColumns[DIRECT_ANTICIPATED_COL];
            tableColumn.setHeaderValue(DIRECT_ANTICIPATED_TOTAL);
            tableColumn = tableColumns[INDIRECT_ANTICIPATED_COL];
            tableColumn.setHeaderValue(INDIRECT_ANTICIPATED_TOTAL);
        }else if(btnChange.isSelected()){
            TableColumn tableColumn;
            tableColumn = tableColumns[DIRECT_OBLIGATED_COL];
            tableColumn.setHeaderValue(DIRECT_OBLIGATED_CHANGE);
            tableColumn = tableColumns[INDIRECT_OBLIGATED_COL];
            tableColumn.setHeaderValue(INDIRECT_OBLIGATED_CHANGE);
            tableColumn = tableColumns[DIRECT_ANTICIPATED_COL];
            tableColumn.setHeaderValue(DIRECT_ANTICIPATED_CHANGE);
            tableColumn = tableColumns[INDIRECT_ANTICIPATED_COL];
            tableColumn.setHeaderValue(INDIRECT_ANTICIPATED_CHANGE);
        }
    }
    //4543 End
    
    //3857 -- start
    private void dateClickDirectInDirect() {
        if(btnDates.isSelected()) {
            awardAmountColumnModel.addColumn(tableColumns[OBL_EFF_DATE_COL]);
            awardAmountColumnModel.addColumn(tableColumns[OBL_EXP_DATE_COL]);
            awardAmountColumnModel.addColumn(tableColumns[FINAL_EXP_COL]);
            
            int newVisibleColumns[] = new int[10];
            newVisibleColumns[0] = visibleColumns[0];
            newVisibleColumns[1] = visibleColumns[1];
            newVisibleColumns[2] = visibleColumns[2];
            newVisibleColumns[3] = visibleColumns[3];
            newVisibleColumns[4] = visibleColumns[4];
            newVisibleColumns[5] = visibleColumns[5];
            newVisibleColumns[6] = visibleColumns[6];
            newVisibleColumns[7] = OBL_EFF_DATE_COL;
            newVisibleColumns[8] = OBL_EXP_DATE_COL;
            newVisibleColumns[9] = FINAL_EXP_COL;
            visibleColumns = newVisibleColumns;
            //Commented with case 4543: Money and End Dates UI Changes
//            setLableHeader1(getHeaderForButton(btnOther));
//            setLableHeader2(DATE_HEADER);
            //4543 End
        } else {
            awardAmountColumnModel.removeColumn(tableColumns[OBL_EFF_DATE_COL]);
            awardAmountColumnModel.removeColumn(tableColumns[OBL_EXP_DATE_COL]);
            awardAmountColumnModel.removeColumn(tableColumns[FINAL_EXP_COL]);
            
            int newVisibleColumns[] = new int[10];
            newVisibleColumns[0] = visibleColumns[0];
            newVisibleColumns[1] = visibleColumns[1];
            newVisibleColumns[2] = visibleColumns[2];
            newVisibleColumns[3] = visibleColumns[3];
            newVisibleColumns[4] = visibleColumns[4];
            newVisibleColumns[5] = visibleColumns[5];
            newVisibleColumns[6] = visibleColumns[6];
            visibleColumns = newVisibleColumns;
            //Commented with case 4543: Money and End Dates UI Changes
//            setLableHeader1(OBL_AMOUNT_HEADER);
//            setLableHeader2(ANT_AMOUNT_HEADER);
            //4543 End
        }
    }
    //3857 -- end
   private void clickedDirectInDirect(JToggleButton toggleButton) {
       
       if(toggleButton.isSelected()) {
           if(btnDates.isSelected()) {
               //Date is selected. select this button and unselect the other button.
               btnOther.setSelected(false);
               btnOther = toggleButton;
               setColumnsInDirect();
//               setLableHeader1(getHeaderForButton(toggleButton));
           } else  {
               
               btnOther.setSelected(false);
               int columnsToRemove[] =  getColumnsForButton(btnOther);
               awardAmountColumnModel.removeColumn(tableColumns[columnsToRemove[0]]);
               awardAmountColumnModel.removeColumn(tableColumns[columnsToRemove[1]]);
               
               int columnsToInsert[] =  getColumnsForButton(toggleButton);
               awardAmountColumnModel.addColumn(tableColumns[columnsToInsert[0]]);
               awardAmountColumnModel.addColumn(tableColumns[columnsToInsert[1]]);
               visibleColumns[3] = columnsToInsert[0];
               visibleColumns[6] = columnsToInsert[1];
               awardAmountColumnModel.moveColumn(5,3);
               btnOther = toggleButton;
           }
       } else {
          
           toggleButton.setSelected(true);
       }
    }
   
   
   private void setColumnsInDirect() {
       if(btnOther.equals(btnTotal)){
           dateSelOtherClickedDirectInDirect(OBL_TOTAL_COL, ANT_TOTAL_COL);
       }//total
       else if(btnOther.equals(btnDistributed)) {
           dateSelOtherClickedDirectInDirect(OBL_DISTRIBUTED_COL, ANT_DISTRIBUTED_COL);
       }//Distributed
       else if(btnOther.equals(btnDistributable)) {
           dateSelOtherClickedDirectInDirect(OBL_DISTRIBUTABLE_COL, ANT_DISTRIBUTABLE_COL);
       }//Distributable
       else if(btnOther.equals(btnChange)) {
           dateSelOtherClickedDirectInDirect(OBL_CHANGE_COL, ANT_CHANGE_COL);
       }
   }
   
   private void dateSelOtherClickedDirectInDirect(int firstColumn,int secondColumn) {
       
       jttAwardAmount.getColumnModel().removeColumn(tableColumns[visibleColumns[3]]);
       jttAwardAmount.getColumnModel().removeColumn(tableColumns[visibleColumns[6]]);
       
       jttAwardAmount.getColumnModel().addColumn(tableColumns[firstColumn]);
       jttAwardAmount.getColumnModel().addColumn(tableColumns[secondColumn]);
       jttAwardAmount.getColumnModel().moveColumn(8, 3);
       jttAwardAmount.getColumnModel().moveColumn(9, 6);
       
       visibleColumns[3] = firstColumn;
       visibleColumns[6] = secondColumn;
       
   }
    
    
      //3857 -- start
    private void directInDirectAction() {
        //Check for the button selection
        if(btnDirectInDirect.isSelected()) {
            
            if(btnDates.isSelected()) {
                // Check for the selection of dates That is there are already two buttons are selected
                // need to insert direct/indirect inbetween two columns "1" and "3"
                //Modified with case 4543: Distributable and distributed views with dc/idc
                if(btnOther !=null && (btnOther.equals(btnTotal) || btnOther.equals(btnChange) 
                                        || btnOther.equals(btnDistributable) || btnOther.equals(btnDistributed))){
                    setDirectInDirectDateSelected();
                }else {
                    btnDirectInDirect.setSelected(false);
                }
            } else if(btnSecond !=null && btnOther !=null) {
                // Only two buttons are selected other then dates , so need to remove both and need to select
                // change,dates  and direct indirect
                if(btnOther != null && !btnOther.equals(btnChange)) {
                    btnOther.setSelected(false);
                }
                if(btnSecond != null && !btnSecond.equals(btnChange)) {
                    btnSecond.setSelected(false);
                }
                btnDates.setSelected(true);
                btnChange.setSelected(true);
                btnOther = btnChange;
                setColumnsDirectInDirect();
                clickDirectIndirect();
//                 setLableHeader1(getHeaderForButton(btnOther));
//                setLableHeader2(DATE_HEADER);
            } else {
                if(btnSecond !=null)
                btnSecond.setSelected(false);
                if(btnDistributable.isSelected())
                    btnDistributable.setSelected(false);
                 if(btnDistributed.isSelected())
                    btnDistributed.setSelected(false);
                btnDates.setSelected(true);
                btnChange.setSelected(true);
                btnOther = btnChange;
                setColumnsDirectInDirect();
                clickDirectIndirect();
            }
          
        } else {
            if(!btnDates.isSelected() && btnOther !=null) {
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[3]]);
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[6]]);
                awardAmountColumnModel.removeColumn(tableColumns[DIRECT_OBLIGATED_COL]);
                awardAmountColumnModel.removeColumn(tableColumns[INDIRECT_OBLIGATED_COL]);
                awardAmountColumnModel.removeColumn(tableColumns[DIRECT_ANTICIPATED_COL]);
                awardAmountColumnModel.removeColumn(tableColumns[INDIRECT_ANTICIPATED_COL]);

             //retain the other selected button , select change/total.
            if(btnOther.equals(btnChange)) {
                btnTotal.setSelected(true);
                btnSecond = btnTotal;
            }else {
                btnChange.setSelected(true);
                btnSecond = btnChange;
            }
              int otherCols[];
                if(btnOther.equals(btnChange)) {
                    //Total is Selected
                    otherCols = getColumnsForButton(btnTotal);
                }else {
                    //Selcted button retained, change selected
                    otherCols = getColumnsForButton(btnOther);
                }
                
                awardAmountColumnModel.addColumn(tableColumns[OBL_CHANGE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[otherCols[0]]);
                awardAmountColumnModel.addColumn(tableColumns[ANT_CHANGE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[otherCols[1]]);
                
                int newVisibleColumns[] = new int[5];//0th column is tree + 4 cols
                newVisibleColumns[0] = visibleColumns[0];
                newVisibleColumns[1] = OBL_CHANGE_COL;
                newVisibleColumns[2] = otherCols[0];
                newVisibleColumns[3] = ANT_CHANGE_COL;
                newVisibleColumns[4] = otherCols[1];                
                visibleColumns = newVisibleColumns;
            
                
                
            } else {
            awardAmountColumnModel.removeColumn(tableColumns[DIRECT_OBLIGATED_COL]);
            awardAmountColumnModel.removeColumn(tableColumns[INDIRECT_OBLIGATED_COL]);
            awardAmountColumnModel.removeColumn(tableColumns[DIRECT_ANTICIPATED_COL]);
            awardAmountColumnModel.removeColumn(tableColumns[INDIRECT_ANTICIPATED_COL]);
            
            int totalColLength =visibleColumns.length-4;
            int newVisibleColumns[];
            if(totalColLength == 3) {
                awardAmountColumnModel.addColumn(tableColumns[EMPTY_COL]);
                awardAmountColumnModel.addColumn(tableColumns[EMPTY_COL]);
                
                newVisibleColumns = new int[5];
                if(btnTotal.isSelected()) {
                    awardAmountColumnModel.moveColumn(4,1);
                    awardAmountColumnModel.moveColumn(2,4);
                    newVisibleColumns[0] = visibleColumns[0];
                    newVisibleColumns[1] = EMPTY_COL;
                    newVisibleColumns[2] = visibleColumns[3];
                    newVisibleColumns[3] = EMPTY_COL;
                    newVisibleColumns[4] = visibleColumns[6];
                    
                } else if(btnChange.isSelected()) {
                    awardAmountColumnModel.moveColumn(2,3);
                    newVisibleColumns[0] = visibleColumns[0];
                    newVisibleColumns[1] = visibleColumns[3];
                    newVisibleColumns[2] = EMPTY_COL;
                    newVisibleColumns[3] = visibleColumns[6];
                    newVisibleColumns[4] = EMPTY_COL;
                }
            } else {
                newVisibleColumns = new int[totalColLength];//0th column is tree + 4 cols
                newVisibleColumns[0] = visibleColumns[0];
                newVisibleColumns[1] =visibleColumns[3];
                if(visibleColumns[4] == 15) {
                    newVisibleColumns[2] = visibleColumns[6];
                    int visibleIndex = 7;
                    for(int index =3;index<=totalColLength-1;index++) {
                        newVisibleColumns[index] = visibleColumns[visibleIndex];
                        visibleIndex++;
                    }
                } else {
                    newVisibleColumns[2] =visibleColumns[4];
                    int visibleIndex = 7;
                    for(int index =3;index<=totalColLength-1;index++) {
                        newVisibleColumns[index] = visibleColumns[visibleIndex];
                        visibleIndex++;
                    }
                }
            }
              visibleColumns = newVisibleColumns;
            }
          
            setIsDirectIndirectSelected(false);
        }
    }
    
    private void setColumnsDirectInDirect() {
        if(btnDirectInDirect.isSelected()) {
            
            if(visibleColumns[1] ==3 && visibleColumns[3] == 7) {
                // Check wheather visible columns already contains change columns
                // in the columns 1 and 3, if present remove other columns that is 2 and 4 and set visible columns to 5
                 
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[2]]);
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[4]]);
                awardAmountColumnModel.addColumn(tableColumns[OBL_EFF_DATE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[OBL_EXP_DATE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[FINAL_EXP_COL]);
               
                int newVisibleColumns[] = new int[6];
                newVisibleColumns[0] = visibleColumns[0];
                newVisibleColumns[1] = visibleColumns[1];
                newVisibleColumns[2] = visibleColumns[3];
                newVisibleColumns[3] = OBL_EFF_DATE_COL;
                newVisibleColumns[4] = OBL_EXP_DATE_COL;
                newVisibleColumns[5] = FINAL_EXP_COL;
                visibleColumns = newVisibleColumns;
                
            }else if(visibleColumns[2] ==3 && visibleColumns[4] == 7) {
                 // Check wheather visible columns already contains change columns
                // in the columns 2 and 4, if present remove other columns that is 1 and 3 and set visible columns to 5
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[1]]);
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[3]]);
                 awardAmountColumnModel.addColumn(tableColumns[OBL_EFF_DATE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[OBL_EXP_DATE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[FINAL_EXP_COL]);
                int newVisibleColumns[] = new int[6];
                newVisibleColumns[0] = visibleColumns[0];
                newVisibleColumns[1] = visibleColumns[2];
                newVisibleColumns[2] = visibleColumns[4];
                newVisibleColumns[3] = OBL_EFF_DATE_COL;
                newVisibleColumns[4] = OBL_EXP_DATE_COL;
                newVisibleColumns[5] = FINAL_EXP_COL;
                visibleColumns = newVisibleColumns;

            } else {
                // since visible columns does not contain change columns remove all columns other then tree and dates and
                 //insert change column in the location 1 and 2
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[1]]);
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[2]]);
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[3]]);
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[4]]);
                awardAmountColumnModel.addColumn(tableColumns[OBL_CHANGE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[ANT_CHANGE_COL]);
                   awardAmountColumnModel.addColumn(tableColumns[OBL_EFF_DATE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[OBL_EXP_DATE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[FINAL_EXP_COL]);
                int newVisibleColumns[] = new int[6];
                newVisibleColumns[0] = visibleColumns[0];
                newVisibleColumns[1] = OBL_CHANGE_COL;
                newVisibleColumns[2] = ANT_CHANGE_COL;
                newVisibleColumns[3] = OBL_EFF_DATE_COL;
                newVisibleColumns[4] = OBL_EXP_DATE_COL;
                newVisibleColumns[5] = FINAL_EXP_COL;
                visibleColumns = newVisibleColumns;
                
            }
        } 
    }
    
    private void setDirectInDirectDateSelected() {
        
        int newVisibleColumns[] = new int[10];
        newVisibleColumns[0] = visibleColumns[0];
        newVisibleColumns[1] = DIRECT_OBLIGATED_COL;
        newVisibleColumns[2] = INDIRECT_OBLIGATED_COL;
        newVisibleColumns[3] = visibleColumns[1];
        newVisibleColumns[4] = DIRECT_ANTICIPATED_COL;
        newVisibleColumns[5] = INDIRECT_ANTICIPATED_COL;
        newVisibleColumns[6] = visibleColumns[2];
        newVisibleColumns[7] = visibleColumns[3];
        newVisibleColumns[8] = visibleColumns[4];
        newVisibleColumns[9] = visibleColumns[5];
        visibleColumns = newVisibleColumns;
        
        
        jttAwardAmount.getColumnModel().addColumn(tableColumns[13]);
        jttAwardAmount.getColumnModel().addColumn(tableColumns[14]);
        jttAwardAmount.getColumnModel().addColumn(tableColumns[15]);
        jttAwardAmount.getColumnModel().addColumn(tableColumns[16]);
        
        jttAwardAmount.getColumnModel().moveColumn(6, 1);
        jttAwardAmount.getColumnModel().moveColumn(7, 2);
        jttAwardAmount.getColumnModel().moveColumn(8, 4);
        jttAwardAmount.getColumnModel().moveColumn(9, 5);
        
        setIsDirectIndirectSelected(true);
        
    }
      //3857 -- end
    private void dateClick() {
        if(btnDates.isSelected()) {
            if(btnDates.isSelected()) {
                //UnSelect other button(s), select change
                if(btnOther != null && !btnOther.equals(btnChange)) {
                    btnOther.setSelected(false);
                }
                if(btnSecond != null && !btnSecond.equals(btnChange)) {
                    btnSecond.setSelected(false);
                }
                
                btnChange.setSelected(true);
                btnOther = btnChange;
                setColumns();
                //remove last 2 columns and insert date columns
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[3]]);
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[4]]);
                
                awardAmountColumnModel.addColumn(tableColumns[OBL_EFF_DATE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[OBL_EXP_DATE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[FINAL_EXP_COL]);
                
                int newVisibleColumns[] = new int[6];
                newVisibleColumns[0] = visibleColumns[0];
                newVisibleColumns[1] = visibleColumns[1];
                newVisibleColumns[2] = visibleColumns[2];
                newVisibleColumns[3] = OBL_EFF_DATE_COL;
                newVisibleColumns[4] = OBL_EXP_DATE_COL;
                newVisibleColumns[5] = FINAL_EXP_COL;
                visibleColumns = newVisibleColumns;
                //Commented with case 4543: Money and End Dates UI Changes
//                setLableHeader1(getHeaderForButton(btnOther));
//                setLableHeader2(DATE_HEADER);
                //4543 End
            }
        }else{
            
            //retain the other selected button , select change/total.
            if(btnOther.equals(btnChange)) {
                btnTotal.setSelected(true);
                btnSecond = btnTotal;
            }else {
                btnChange.setSelected(true);
                btnSecond = btnChange;
            }
            
            setColumns();
            //Commented with case 4543: Money and End Dates UI Changes
//            setLableHeader1(OBL_AMOUNT_HEADER);
//            setLableHeader2(ANT_AMOUNT_HEADER);
            //4543 End
        }
        
    }//End dateClick
    
    private void clicked(JToggleButton jToggleButton) {
        if(jToggleButton.isSelected()) {
            if(btnDates.isSelected()) {
                //Date is selected. select this button and unselect the other button.
                btnOther.setSelected(false);
                btnOther = jToggleButton;
                setColumns();
//                setLableHeader1(getHeaderForButton(jToggleButton));
            }else if(btnOther != null && btnSecond != null) {
                //Already two buttons are selected.
                jToggleButton.setSelected(false);
            }else{
                //can select. set the instance to btnOther/btnSecound whichever is null
                if(btnOther == null) btnOther = jToggleButton;
                else if(btnSecond == null) btnSecond = jToggleButton;
                dateNotSelSecondSelected(getColumnsForButton(jToggleButton));
            }
        }else{
            if(btnDates.isSelected()) {
                //this button and Date is selected can't unselect
                jToggleButton.setSelected(true);
                return ;
            }else {
                if(btnOther == null || btnSecond == null){
                    //only one button is selected can't unselect that button also.
                    jToggleButton.setSelected(true);
                    return ;
                }
                else if(btnOther.equals(jToggleButton)) {
                    btnOther = null;
                }else if(btnSecond.equals(jToggleButton)) {
                    btnSecond = null;
                }
                dateNotSelSecondUnselected(getColumnsForButton(jToggleButton));
            }
        }
    }//End Clicked
    
    private void setColumns() {
        if(visibleColumns.length > 6){
            //this method called first time.
            //reinitialize visible columns with initial columns visible.
            visibleColumns = new int[]{0, 3, 7, 9, 10, 11};
        }
        
        if(btnDates.isSelected()) {
            //Dates should be visible as last three columns.
            //if date is already visible check for the other details.
            //check the first column to know which other button was selected
//            int firstCol = visibleColumns[1];//0th column is the tree.
            if(btnOther.equals(btnTotal)){
                dateSelOtherClicked(OBL_TOTAL_COL, ANT_TOTAL_COL);
            }//total
            else if(btnOther.equals(btnDistributed)) {
                dateSelOtherClicked(OBL_DISTRIBUTED_COL, ANT_DISTRIBUTED_COL);
            }//Distributed
            else if(btnOther.equals(btnDistributable)) {
                dateSelOtherClicked(OBL_DISTRIBUTABLE_COL, ANT_DISTRIBUTABLE_COL);
            }//Distributable
            else if(btnOther.equals(btnChange)) {
                dateSelOtherClicked(OBL_CHANGE_COL, ANT_CHANGE_COL);
            }
        }else {
            //Dates not selected(unselected) other one / two buttons are selected.
            //check if date was previously selected
            if(visibleColumns[3] == OBL_EFF_DATE_COL) {
                //Date was previously selected.
                //Date got unselected. remove date columns.
                //display columns for second button and Change
                
                awardAmountColumnModel.removeColumn(tableColumns[OBL_EFF_DATE_COL]);
                awardAmountColumnModel.removeColumn(tableColumns[OBL_EXP_DATE_COL]);
                awardAmountColumnModel.removeColumn(tableColumns[FINAL_EXP_COL]);
                
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[1]]);
                awardAmountColumnModel.removeColumn(tableColumns[visibleColumns[2]]);
                
                int otherCols[];
                if(btnOther.equals(btnChange)) {
                    //Total is Selected
                    otherCols = getColumnsForButton(btnTotal);
                }else {
                    //Selcted button retained, change selected
                    otherCols = getColumnsForButton(btnOther);
                }
                
                awardAmountColumnModel.addColumn(tableColumns[OBL_CHANGE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[otherCols[0]]);
                awardAmountColumnModel.addColumn(tableColumns[ANT_CHANGE_COL]);
                awardAmountColumnModel.addColumn(tableColumns[otherCols[1]]);
                
                int newVisibleColumns[] = new int[5];//0th column is tree + 4 cols
                newVisibleColumns[0] = visibleColumns[0];
                newVisibleColumns[1] = OBL_CHANGE_COL;
                newVisibleColumns[2] = otherCols[0];
                newVisibleColumns[3] = ANT_CHANGE_COL;
                newVisibleColumns[4] = otherCols[1];
                
                visibleColumns = newVisibleColumns;
                
            }//else {
                //Date was previously not selected. nothing to do with date.
                //other/second buttton is selected/unselected
//                if(btnOther == null || btnSecond == null) {
                    //one of the button is unselected. should remove those columns
                    //replace with empty columns
//                    JToggleButton selected = btnOther == null ? btnSecond : btnOther;
                    
//                }
//            }
        }//End if else btnDates selected
    }//End setColumns
    
    private void dateNotSelSecondSelected(int ColsToInsert[]) {
        int colPlacement[];
        
        if(visibleColumns[1] == EMPTY_COL) {
            //Columns 1 and 3 are empty.Remove empty columns and insert these columns here.
            colPlacement = new int[]{1, 3};
        }else {
            //Columns 2 and 4 are empty.Remove empty columns and insert these columns here.
            colPlacement = new int[]{2, 4};
        }
        awardAmountColumnModel.removeColumn(tableColumns[EMPTY_COL]);
        awardAmountColumnModel.removeColumn(tableColumns[EMPTY_COL]);
        
        awardAmountColumnModel.addColumn(tableColumns[ColsToInsert[0]]);
        awardAmountColumnModel.addColumn(tableColumns[ColsToInsert[1]]);
        
        awardAmountColumnModel.moveColumn(3, colPlacement[0]);
        awardAmountColumnModel.moveColumn(4, colPlacement[1]);
        
        visibleColumns[colPlacement[0]] =  ColsToInsert[0];
        visibleColumns[colPlacement[1]] =  ColsToInsert[1];
    }
    
    private void dateNotSelSecondUnselected(int ColsToRemove[]) {
        jttAwardAmount.getColumnModel().removeColumn(tableColumns[ColsToRemove[0]]);
        jttAwardAmount.getColumnModel().removeColumn(tableColumns[ColsToRemove[1]]);
        
        jttAwardAmount.getColumnModel().addColumn(tableColumns[EMPTY_COL]);
        
        jttAwardAmount.getColumnModel().addColumn(tableColumns[EMPTY_COL]);
        
        int newVisibleColumns[] = new int[5];//0th column is tree
        newVisibleColumns[0] = visibleColumns[0];
        
        //get the selected button
        JToggleButton selected = btnOther == null ? btnSecond : btnOther;
        int selectedCols[] = getColumnsForButton(selected);
        
        if(selectedCols[0] == visibleColumns[1]) {
            //1st and 3rd columns are same. insert empty rows exist in 2nd and 4th column
            jttAwardAmount.getColumnModel().moveColumn(3, 2);
            
            newVisibleColumns[1] = visibleColumns[1];
            newVisibleColumns[2] = EMPTY_COL;
            newVisibleColumns[3] = visibleColumns[3];
            newVisibleColumns[4] = EMPTY_COL;
        }else {
            //2st and 4rd columns are same. insert empty rows exist in 1st and 3rd column
            jttAwardAmount.getColumnModel().moveColumn(3, 1);
            jttAwardAmount.getColumnModel().moveColumn(4, 3);
            
            newVisibleColumns[1] = EMPTY_COL;
            newVisibleColumns[2] = visibleColumns[2];
            newVisibleColumns[3] = EMPTY_COL;
            newVisibleColumns[4] = visibleColumns[4];
        }
        
        //set visible columns
        visibleColumns = newVisibleColumns;
    }
    
    /**
     *this will remove previously selected columns and insert selected columns
     *for the other button when date is selected.
     */
    private void dateSelOtherClicked(int firstColumn, int secondColumn) {
        int firstCol = visibleColumns[1];//0th column is the tree.
        //Check if first col is this column
        if(firstCol == firstColumn && firstCol == secondColumn) {
            //this button is selected and columns already displayed. Nothing to do.
            //check if date is displayed.else display date.
        }else {
            //if first columns are date columns. move them.
            //remove first two columns. display these columns
            if(firstCol != OBL_EFF_DATE_COL) {
                
                int colCount = jttAwardAmount.getColumnCount();
                
                jttAwardAmount.getColumnModel().removeColumn(tableColumns[visibleColumns[1]]);
                jttAwardAmount.getColumnModel().removeColumn(tableColumns[visibleColumns[2]]);
                
                jttAwardAmount.getColumnModel().addColumn(tableColumns[firstColumn]);
                jttAwardAmount.getColumnModel().addColumn(tableColumns[secondColumn]);
                
                jttAwardAmount.getColumnModel().moveColumn(colCount - 2, 1);
                jttAwardAmount.getColumnModel().moveColumn(colCount - 1, 2);
                
                visibleColumns[1] = firstColumn;
                visibleColumns[2] = secondColumn;
            }
        }
    }
    
    private int[] getColumnsForButton(JToggleButton jToggleButton) {
        // since all buttons manage two cols except Date.
        if(jToggleButton.equals(btnTotal)){
            return new int[]{OBL_TOTAL_COL, ANT_TOTAL_COL};
        }//total
        else if(jToggleButton.equals(btnDistributed)) {
            return new int[]{OBL_DISTRIBUTED_COL, ANT_DISTRIBUTED_COL};
        }//Distributed
        else if(jToggleButton.equals(btnDistributable)) {
            return new int[]{OBL_DISTRIBUTABLE_COL, ANT_DISTRIBUTABLE_COL};
        }//Distributable
        else if(jToggleButton.equals(btnChange)) {
            return new int[]{OBL_CHANGE_COL, ANT_CHANGE_COL};
        } else if (jToggleButton.equals(btnDirectInDirect)){
            return new int[]{DIRECT_OBLIGATED_COL,INDIRECT_OBLIGATED_COL,DIRECT_ANTICIPATED_COL,INDIRECT_ANTICIPATED_COL};
        }//Change//Change
        else{
            return new int[]{OBL_EFF_DATE_COL, OBL_EXP_DATE_COL, FINAL_EXP_COL};
        }//Date
    }
    //Commented with case 4543: Money and end date Ui Changes
   /* private String getHeaderForButton(JToggleButton jToggleButton) {
        if(jToggleButton.equals(btnTotal)){
            return TOTAL_HEADER;
        }//total
        else if(jToggleButton.equals(btnDistributed)) {
            return DISTRIBUTED_HEADER;
        }//Distributed
        else if(jToggleButton.equals(btnDistributable)) {
            return DISTRIBUTABLE_HEADER;
        }//Distributable
        else if(jToggleButton.equals(btnChange)) {
            return CHANGE_HEADER;
        }//Change
        else{
            return DATE_HEADER;
        }//Date
    }*/
    
    public AwardAmountInfoBean getSelectedBean() {
        TreePath treePath = jttAwardAmount.getTree().getSelectionPath();
        if(treePath == null) {
            return null;
        }
        AwardAmountInfoBean selectedBean = (AwardAmountInfoBean)treePath.getLastPathComponent();
        return selectedBean;
    }
    
    public int getSelectedRow() {
        return jttAwardAmount.getSelectedRow();
    }
    
    /**
     * Getter for property editable.
     * @return Value of property editable.
     */
    public boolean isEditable() {
        return editable;
    }
    
    /**
     * Setter for property editable.
     * @param editable New value of property editable.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
        awardAmountRenderer.setRendererBackground(UIManager.getDefaults().getColor("Panel.background"));
    }
    
    public void setSelectionEnabled(boolean enabled) {
        jttAwardAmount.setRowSelectionAllowed(enabled);
    }
    
    public void setAwardColor(String awardNumber, Color color) {
        awardToColor = awardNumber;
        awardColor = color;
    }
    
    /**
     * Setter for property changeVisible.
     * @param changeVisible New value of property changeVisible.
     */
    public void setChangeVisible(boolean changeVisible) {
        this.changeVisible = changeVisible;
        btnChange.setVisible(changeVisible);
        if(!changeVisible) {
            btnTotal.doClick();       
        }
        //btnChange = btnDistributable;
    }
    
    /**
     * Getter for property changeVisible.
     * @return Value of property changeVisible.
     */
    public boolean isChangeVisible() {
        return changeVisible;
    }
    //Commented with case 4543: Money and end date Ui Changes
    //Bug Fix for moving the header when the table is moved:Start
    /*public void columnAdded(TableColumnModelEvent e) {
    }
    
    public void columnMarginChanged(ChangeEvent e) {
        int width = jttAwardAmount.getColumnModel().getColumn(0).getWidth();
        GridBagLayout layout = (GridBagLayout)pnlHeader.getLayout();
        GridBagConstraints gridBagConstraints = layout.getConstraints(lblHeading1);
        gridBagConstraints.insets = new java.awt.Insets(0, width, 0, 0);
        layout.setConstraints(lblHeading1, gridBagConstraints);
        //        layout.setConstraints(lblHeading2, gridBagConstraints);
        //        layout.removeLayoutComponent(lblHeading1);
        //        pnlHeader.add(lblHeading1, gridBagConstraints);
        //        Dimension d = new Dimension(width + pnlHeader.getWidth(),pnlHeader.getHeight());
        //        pnlHeader.setPreferredSize(d);
        //        pnlHeader.setMinimumSize(d);
        pnlHeader.revalidate();
    }
    
    public void columnMoved(TableColumnModelEvent e) {
    }
    
    public void columnRemoved(TableColumnModelEvent e) {
    }
    
    public void columnSelectionChanged(ListSelectionEvent e) {
    }
    
    public void adjustmentValueChanged(AdjustmentEvent e) {
        int value = horizantalScroll.getValue();
        scrPnHeader.getHorizontalScrollBar().setValue(value);
    }*/
    //Bug Fix for moving the header when the table is moved:End

    //JIRA COEUSQA-2871 - START
    public void setMaxAccountNumberLength(int maxAccountNumberLength){
        this.maxAccountNumberLength = maxAccountNumberLength;
    }
    //JIRA COEUSQA-2871 - END

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JToggleButton btnChange;
    public javax.swing.JToggleButton btnDates;
    public javax.swing.JToggleButton btnDirectInDirect;
    public javax.swing.JToggleButton btnDistributable;
    public javax.swing.JToggleButton btnDistributed;
    public javax.swing.JToggleButton btnTotal;
    public javax.swing.JLabel lblFooter;
    public javax.swing.JLabel lblView;
    public javax.swing.JPanel pnlButton;
    public javax.swing.JScrollPane scrPnAwardAmount;
    // End of variables declaration//GEN-END:variables
    
    /**
     *Inner class TreeTable Model
     */
    class AwardAmountTreeTableModel extends DefaultTreeModel implements TreeTableModel{//extends AbstractTreeTableModel{
        
        /** Holds the tree root */
        private AwardAmountInfoBean root;
        
        private CoeusVector cvAwardAmount;
        
        private AwardAmountInfoBean node;
        
        //will be used to indicate if value has been set or any validation error occured before
        //setting value.
        private boolean valueSet = true;
        
        private final Double ZERO = new Double(0);
        
        /** Holds an instance of the <CODE>AwardHierarchyDataMediator</CODE> */
        private AwardHierarchyDataMediator awardHierarchyDataMediator;
        
        AwardAmountTreeTableModel(CoeusVector cvAwardAmount, AwardAmountInfoBean root) {
            super(new DefaultMutableTreeNode(root));
            this.root = root;
            this.cvAwardAmount = cvAwardAmount;
            awardHierarchyDataMediator = new AwardHierarchyDataMediator();
            awardHierarchyDataMediator.setHierarchyData(cvAwardAmount);
        }
        
        
        
        public void setData(CoeusVector cvAwardAmount) {
            //update actype for last updated
            //taking to consideration the indexes of beans have not changed before and after save.
            //i.e bean @ index 2 will still be @ index 2 after save and fetching data from server.
            AwardAmountInfoBean local, server;
            String acType;
            
            //Bug Fix:1410 Start 3
            //Was giving array index out of bound exception when traversing through
            //next button (on the tool bar)in display mode.
            //Since ac types are not required in display mode , made a check for it.
            if(functionType != 'D'){
                for(int index = 0; index < cvAwardAmount.size(); index++) {
                    local = (AwardAmountInfoBean)this.cvAwardAmount.get(index);
                    server = (AwardAmountInfoBean)cvAwardAmount.get(index);
                    acType = local.getAcType();

                    if(acType != null && acType.equals(UPDATE_ANT_CHANGE)) {
                        server.setAcType(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE);
                    }
                    else if(acType != null && acType.equals(UPDATE_OBL_CHANGE)) {
                        server.setAcType(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE);
                    }
                    else if(acType != null && (
                    //acType.equals(AwardAmountTreeTable.UPDATE_ANT_CHANGE) ||
                    //acType.equals(AwardAmountTreeTable.UPDATE_OBL_CHANGE) ||
                    acType.equals(AwardAmountTreeTable.UPDATE_DATE) ||
                    acType.equals(AwardAmountTreeTable.UPDATE_INDIRECT) ||
                    acType.equals(TypeConstants.UPDATE_RECORD) ||
                    acType.equals(TypeConstants.INSERT_RECORD))) {

                        server.setAcType(UPDATED_LAST_TIME);

                    }
                    else {
                        server.setAcType(acType);
                    }//End if actype
                }//End for
            }
            
            this.cvAwardAmount.removeAllElements();
            this.cvAwardAmount = cvAwardAmount;
            awardHierarchyDataMediator.setHierarchyData(cvAwardAmount);
            for(int index = 0; index < cvAwardAmount.size(); index++) {
                AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)cvAwardAmount.get(index);
                awardHierarchyDataMediator.forceReload(awardAmountInfoBean);
            }
        }
        
        // Added for COEUSQA-3394 : unable to save award transaction type when certain changes are made to award detail tab - Start
        /**
         * Method to get the Modeldata
         * @return cvAwardAmount - CoeusVector 
         */
        public CoeusVector getData(){
            return this.cvAwardAmount;
        }
        
        /**
         * Method to update the model data collection 
         * @param cvAwardAmount - CoeusVector
         */
        public void updateAmountInfo(CoeusVector cvAwardAmount){
            this.cvAwardAmount = cvAwardAmount;
        }
        // Added for COEUSQA-3394 : unable to save award transaction type when certain changes are made to award detail tab - End
        
        /**
         * Returns the child of parent at index index in the parent's child array.
         */
        public Object getChild(Object parent, int index) {
            AwardHierarchyBean awardHierarchyBean =
            awardHierarchyDataMediator.getValue((AwardHierarchyBean)parent);
            //commented for performance reason. the beans got are already sorted.
            //awardHierarchyBean.getChildren().sort(AwardHierarchyTree.MIT_AWARD_NUMBER_FIELD, true);
            node = (AwardAmountInfoBean)awardHierarchyBean.getChildren().get(index);
            return awardHierarchyBean.getChildren().get(index);
        }
        
        public int getIndexOfChild(Object parent, Object child) {
            AwardHierarchyBean awardHierarchyBean =
            awardHierarchyDataMediator.getValue((AwardHierarchyBean)parent);
            awardHierarchyBean.getChildren().sort(AwardHierarchyTree.MIT_AWARD_NUMBER_FIELD, true);
            return awardHierarchyBean.getChildren().indexOf(child);
        }
        
        /** To get the child count of the given parent node
         * Returns the number of children of parent.
         * @param parent takes the AwardHierarchyBean
         */
        public int getChildCount(Object parent) {
            AwardHierarchyBean awardHierarchyBean =
            awardHierarchyDataMediator.getValue((AwardHierarchyBean)parent);
            return awardHierarchyBean.getChildCount();
        }
        
        public int getColumnCount() {
            return visibleColumns.length;
        }
        
        public String getColumnName(int column) {
            //Commented with case 4543: Money and end date Ui Changes
//            if(btnDates.isSelected()){
//                switch (column) {
//                    case 1:
//                        return ANTICIPATED;
//                    case 2:
//                        return OBLIGATED;
//                }
//            }
            return colNames[column];
        }
        
        public Class getColumnClass(int column) {
            return colClass[column];
        }
        
        public boolean isValueSet() {
            return valueSet;
        }
        
        public void resetValue() {
            valueSet = true;
        }
        
        public Object getValueAt(Object node, int column) {
           
            //No need to change column.
            //columns already mapped to visible columns.
            
            AwardAmountInfoBean bean = (AwardAmountInfoBean)node;
            
            //when not editable and final exp date is null. all cols will be empty
            if(bean.getFinalExpirationDate() == null && !editable) {
                return null;
            }
            
            switch (column) {
                case AWARD_AMT_TREE_COL:
                    return bean.getMitAwardNumber();
                case OBL_DISTRIBUTED_COL:
                    return new Double(bean.getAmountObligatedToDate() - bean.getObliDistributableAmount());
                case OBL_DISTRIBUTABLE_COL:
                    return new Double(bean.getObliDistributableAmount());
                case OBL_CHANGE_COL:
                    if(bean.getAcType() != null &&
                    (bean.getAcType().equals(UPDATE_OBL_CHANGE) ||
                    bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                    bean.getAcType().equals(UPDATED_LAST_TIME) ||
                    bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE) ||
                    //Bug Fix Zeros after save and click on same row - START
                    bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE) ||
                    //Bug Fix Zeros after save and click on same row - END
                    bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD) ||
                    bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                    //Bug Fix: 1559 Start 1
                    //|| bean.getAcType().equals(UPDATE_DATE)
                    //Bug Fix: 1559 End 1
                    ) &&
                    (bean.getEntryType() != null && !bean.getEntryType().equals(INDIRECT_ENTRY))) {
                        return new Double(bean.getObligatedChange());
                    }else if(!editable){
                        return new Double(bean.getObligatedChange());
                    }else {
                        return ZERO;
                    }
                case OBL_TOTAL_COL:
                    return new Double(bean.getAmountObligatedToDate());
                case ANT_DISTRIBUTED_COL:
                    return new Double(bean.getAnticipatedTotalAmount() - bean.getAnticipatedDistributableAmount());
                case ANT_DISTRIBUTABLE_COL:
                    return new Double(bean.getAnticipatedDistributableAmount());
                case ANT_CHANGE_COL:
                    if(bean.getAcType() != null &&
                    (bean.getAcType().equals(UPDATE_ANT_CHANGE) ||
                    bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                    bean.getAcType().equals(UPDATED_LAST_TIME) ||
                    bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE) ||
                    //Bug Fix Zeros after save and click on same row - START
                    bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE) ||
                    //Bug Fix Zeros after save and click on same row - END
                    bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD) ||
                    bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                    //Bug Fix: 1559 Start 2
                    //|| bean.getAcType().equals(UPDATE_DATE)
                    //Bug Fix: 1559 End 2
                    ) &&
                    (bean.getEntryType() != null && !bean.getEntryType().equals(INDIRECT_ENTRY))) {
                        return new Double(bean.getAnticipatedChange());
                    }else if(!editable){
                        return new Double(bean.getAnticipatedChange());
                    }else {
                        return ZERO;
                    }
                case ANT_TOTAL_COL:
                    return new Double(bean.getAnticipatedTotalAmount());
                case OBL_EFF_DATE_COL:
                    return bean.getCurrentFundEffectiveDate();
                case OBL_EXP_DATE_COL:
                    return bean.getObligationExpirationDate();
                case FINAL_EXP_COL:
                    return bean.getFinalExpirationDate();
               //3857 -- start
                case DIRECT_OBLIGATED_COL:
                  Double displayValue = ZERO;
                   if(btnChange.isVisible() && btnChange.isSelected()) {
                      if(bean.getAcType() != null &&
                              (bean.getAcType().equals(UPDATE_OBL_CHANGE) ||
                              bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                              bean.getAcType().equals(UPDATED_LAST_TIME) ||
                              bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE) ||
                              bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE) ||
                              bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD) ||
                              bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                              ) &&
                              (bean.getEntryType() != null && !bean.getEntryType().equals(INDIRECT_ENTRY))) {
                          displayValue = new Double(bean.getDirectObligatedChange());
                      }else if(!editable){
                          displayValue = new Double(bean.getDirectObligatedChange());
                      }else {
                          displayValue = ZERO;
                      }                     
                   } else {
                      displayValue = new Double(bean.getDirectObligatedTotal());
                   }
                 
                  return displayValue;
                case INDIRECT_OBLIGATED_COL:
                 
                    if(btnChange.isVisible() && btnChange.isSelected()) {
                        if(bean.getAcType() != null &&
                                (bean.getAcType().equals(UPDATE_OBL_CHANGE) ||
                                bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD) ||
                                bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                                ) &&
                                (bean.getEntryType() != null && !bean.getEntryType().equals(INDIRECT_ENTRY))) {
                            displayValue = new Double(bean.getIndirectObligatedChange());
                        }else if(!editable){
                           displayValue = new Double(bean.getIndirectObligatedChange());
                        }else {
                            displayValue = ZERO;
                        }
                    }else {
                        displayValue = new Double(bean.getIndirectObligatedTotal());
                    }
                    return displayValue;
                case DIRECT_ANTICIPATED_COL:
                    if(btnChange.isVisible() && btnChange.isSelected()) {
                        if(bean.getAcType() != null &&
                                (bean.getAcType().equals(UPDATE_ANT_CHANGE) ||
                                bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD) ||
                                bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                                
                                ) &&
                                (bean.getEntryType() != null && !bean.getEntryType().equals(INDIRECT_ENTRY))) {
                            displayValue =  new Double(bean.getDirectAnticipatedChange());
                        }else if(!editable){
                            displayValue =  new Double(bean.getDirectAnticipatedChange());
                        }else {
                            displayValue =  ZERO;
                        }
                    } else {
                        displayValue = new Double(bean.getDirectAnticipatedTotal());
                    }
                    return displayValue;
                case INDIRECT_ANTICIPATED_COL:
                    if(btnChange.isVisible() && btnChange.isSelected()) {
                        if(bean.getAcType() != null &&
                                (bean.getAcType().equals(UPDATE_ANT_CHANGE) ||
                                bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE) ||
                                bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD) ||
                                bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                                ) &&
                                (bean.getEntryType() != null && !bean.getEntryType().equals(INDIRECT_ENTRY))) {
                            displayValue = new Double(bean.getIndirectAnticipatedChange());
                        }else if(!editable){
                            displayValue = new Double(bean.getIndirectAnticipatedChange());
                        }else {
                            displayValue = ZERO;
                        }
                    } else {
                        displayValue = new Double(bean.getIndirectAnticipatedTotal());
                    }
                    return displayValue;
                     //3857 -- end
            }
            return new Integer(1);
        }
        
        public boolean isCellEditable(Object node, int column) {
            if(column == AWARD_AMT_TREE_COL){
                //should be true to listen to events so as to expand the child records.
                return true;
                //return super.isCellEditable(node, column);
            }else {
                if(editable) {
                    //Modified with case 4543: Money and end date Ui Changes
                    if(btnTotal.isSelected() || btnDistributable.isSelected() || btnDistributed.isSelected()){
                        if(column == DIRECT_OBLIGATED_COL ||
                                column == DIRECT_OBLIGATED_COL ||
                                column == INDIRECT_OBLIGATED_COL ||
                                column == DIRECT_ANTICIPATED_COL ||
                                column == INDIRECT_ANTICIPATED_COL)
                            return false;
                    }
                    //4543 End
                    return colEditable[column];
                }else {
                    return false;
                }
            }
        }
        
        /**
         * Sets the value for node <code>node</code>,
         * at column number <code>column</code>.
         */
        public void setValueAt(Object aValue, Object node, int column) {
            AwardAmountInfoBean bean = (AwardAmountInfoBean)node;
            int selectedRow = jttAwardAmount.getSelectedRow();
            //Added for COEUSDEV-283 : Award Money and End Dates: change ob exp date and press save; last change amount fills in on the line - Start
            //When award row is not modified amount columns are reset to 0.0
            if( bean != null && bean.getAcType() == null){
                bean.setAnticipatedChange(0.0);
                bean.setObligatedChange(0.0);
                bean.setDirectAnticipatedChange(0.0);
                bean.setIndirectAnticipatedChange(0.0);
                bean.setDirectObligatedChange(0.0);
                bean.setIndirectObligatedAmount(0.0);
            }
            //COEUSDEV-283 : End
            double dblVal;
        
            //will be set to false if any validation error occurs
            valueSet = true;
            
            switch (column) {
                
                case OBL_CHANGE_COL:
                    dblVal = Double.parseDouble(aValue.toString());
                    //if same don't update record
                    if((bean.getAcType() == null && dblVal ==0) ||
                    (bean.getAcType() != null &&
                    (bean.getAcType().equals(UPDATE_OBL_CHANGE) ||
                    bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                    bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE) ||
                    bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD)
                    //Bug Fix : 1109 Step 1 - START
                    || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                    //Bug Fix : 1109  Step 1 - END
                    ) &&
                    dblVal == bean.getObligatedChange())) {
                        return ;
                    }
                    
                    if(setOblChange(bean, dblVal)) {
                        bean.setObligatedChange(dblVal);
                        if(bean.getAcType() != null && bean.getAcType().startsWith(UPDATED_LAST_TIME)) {
                            //Updated Last time
                            if(bean.getAcType().equals(UPDATED_LAST_TIME)) {
                                //updated last time and no modifications done
                                bean.setAcType(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE);
                            }else if(bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE)) {
                                //anticipated change already modified
                                bean.setAcType(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD);
                            }
                        }else if(bean.getAcType() == null || bean.getAcType().equals(UPDATE_INDIRECT) || bean.getAcType().equals(UPDATE_DATE)){
                            //First Change
                            bean.setAcType(UPDATE_OBL_CHANGE);
                        }else if(bean.getAcType().equals(UPDATE_ANT_CHANGE)) {
                            //Anticipated Amount already changed.
                            bean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        bean.setEntryType(DIRECT_ENTRY);
                    }else {
                        valueSet = false;
                    }
                    
                    break;
                case ANT_CHANGE_COL:
                    dblVal = Double.parseDouble(aValue.toString());
                    //if same don't update record
                    if((bean.getAcType() == null && dblVal ==0) ||
                    (bean.getAcType() != null &&
                    (bean.getAcType().equals(UPDATE_ANT_CHANGE) ||
                    bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                    bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE) ||
                    bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD)
                    //Bug Fix : 1109 Step 2 - START
                    || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                    //Bug Fix : 1109  Step 2 - END
                    ) &&
                    dblVal == bean.getAnticipatedChange())) {
                        return ;
                    }
                    
                    if(setAntChange(bean, dblVal)) {
                        bean.setAnticipatedChange(dblVal);
                        if(bean.getAcType() != null && bean.getAcType().startsWith(UPDATED_LAST_TIME)) {
                            //Updated last time
                            if(bean.getAcType().equals(UPDATED_LAST_TIME)) {
                                //updated last time and no modifications done
                                bean.setAcType(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE);
                            }else if(bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE)) {
                                //Obligated change already modified
                                bean.setAcType(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD);
                            }
                        }else if(bean.getAcType() == null || bean.getAcType().equals(UPDATE_INDIRECT) || bean.getAcType().equals(UPDATE_DATE)){
                            //First Change
                            bean.setAcType(UPDATE_ANT_CHANGE);
                        }else if(bean.getAcType().equals(UPDATE_OBL_CHANGE)) {
                            //Obligated Amount already changed.
                            bean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        bean.setEntryType(DIRECT_ENTRY);
                    }else {
                        valueSet = false;
                    }
                    break;
                case ANT_TOTAL_COL:
                    bean.setAnticipatedTotalAmount(Double.parseDouble(aValue.toString()));
                    break;
                case OBL_EFF_DATE_COL:
                    String strDate;
                    Date dt;
                    //Validate for Obligation Effective Date
                    try{
                        if(aValue == null || aValue.equals(EMPTY)) {
                            if(bean.getCurrentFundEffectiveDate() == null) {
                                //same no need to update.
                                return ;
                            }
                            bean.setCurrentFundEffectiveDate(null);
                            if(bean.getAcType() == null) {
                                bean.setAcType(UPDATE_DATE);
                            }
                            return ;
                        }//Empty/null Date value
                        strDate = dateUtils.formatDate(aValue.toString(), DATE_SEPARATERS, DATE_FORMAT);
                        if(strDate == null) {
                            
                            //Bug Fix:1108 Start 1
                            //                            awardAmountEditor.cancelCellEditing();
                            //                            awardAmountEditor.txtDate.setText(EMPTY);
                            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_OBL_EFF_DATE));
                            //                            jttAwardAmount.requestFocusInWindow();
                            //                            jttAwardAmount.editCellAt(1,column);
                            setRequestFocusInThread(jttAwardAmount.getSelectedRow(),OBL_EFF_DATE_COL);
                            //Bug Fix:1108 End 1
                            
                            valueSet = false;
                            return ;
                        }
                        dt = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
                    }catch  (ParseException parseException) {
                        
                        //                        //Bug Fix:1108 Start 2
                        //                        awardAm ountEditor.cancelCellEditing();
                        //                        awardAmountEditor.txtDate.setText(EMPTY);
                        //                        setRequestFocusInThread(awardAmountEditor.txtDate);
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_OBL_EFF_DATE));
                        //                        jttAwardAmount.requestFocusInWindow();
                        //                        jttAwardAmount.editCellAt(1,column);
                        
                        setRequestFocusInThread(jttAwardAmount.getSelectedRow(),OBL_EFF_DATE_COL);
                        //Bug Fix:1108 End 2
                        
                        valueSet = false;
                        return ;
                    }
                    //Went thru All Date Validation. Good Date.
                    //check if old and new date is same
                    if(bean.getCurrentFundEffectiveDate() != null && dt.compareTo(bean.getCurrentFundEffectiveDate()) == 0) {
                        //Same no need to update
                        return ;
                    }else{
                        bean.setCurrentFundEffectiveDate(new java.sql.Date(dt.getTime()));
                        bean.setEntryType(DIRECT_ENTRY);
                        if(bean.getAcType() == null) {
                            bean.setAcType(UPDATE_DATE);
                        }
                        setOblAndAntAmount(bean);
                    }
                    break;
                case OBL_EXP_DATE_COL:
                    //Validate for Obligation Expiry Date
                    try{
                        if(aValue == null || aValue.equals(EMPTY)) {
                            if(bean.getObligationExpirationDate() == null) {
                                //Same . no need to update
                                return ;
                            }
                            bean.setObligationExpirationDate(null);
                            if(bean.getAcType() == null) {
                                bean.setAcType(UPDATE_DATE);
                            }
                            return ;
                        }//Empty/null date value
                        strDate = dateUtils.formatDate(aValue.toString(), DATE_SEPARATERS, DATE_FORMAT);
                        if(strDate == null) {
                            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_OBL_EXP_DATE));
                            
                            //Bug Fix:1108 Start 3
                            setRequestFocusInThread(jttAwardAmount.getSelectedRow(),OBL_EXP_DATE_COL);
                            //Bug Fix:1108 End 3
                            
                            valueSet = false;
                            return ;
                        }
                        dt = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
                    }catch (ParseException parseException) {
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_OBL_EXP_DATE));
                        
                        //Bug Fix:1108 Start 4
                        setRequestFocusInThread(jttAwardAmount.getSelectedRow(),OBL_EXP_DATE_COL);
                        //Bug Fix:1108 End 4
                        
                        valueSet = false;
                        return ;
                    }
                    //Went thru All Date Validation. Good Date.
                    //check if old and new date is same
                    if(bean.getObligationExpirationDate() != null && dt.compareTo(bean.getObligationExpirationDate()) == 0) {
                        //same no need to update
                        return ;
                    }else {
                        bean.setObligationExpirationDate(new java.sql.Date(dt.getTime()));
                        bean.setEntryType(DIRECT_ENTRY);
                        if(bean.getAcType() == null) {
                            bean.setAcType(UPDATE_DATE);
                        }
                        setOblAndAntAmount(bean);
                    }
                    break;
                case FINAL_EXP_COL:
                    //Validate for Final Expiry Date
                    try{
                        if(aValue == null || aValue.equals(EMPTY)) {
                            if(bean.getFinalExpirationDate() == null) {
                                //same no need to update
                                return ;
                            }
                            bean.setFinalExpirationDate(null);
                            if(bean.getAcType() == null) {
                                bean.setAcType(UPDATE_DATE);
                            }
                            return ;
                        }
                        strDate = dateUtils.formatDate(aValue.toString(), DATE_SEPARATERS, DATE_FORMAT);
                        if(strDate == null) {
                            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_FINAL_EXP_DATE));
                            
                            //Bug Fix:1108 Start 5
                            setRequestFocusInThread(jttAwardAmount.getSelectedRow(),FINAL_EXP_COL);
                            //Bug Fix:1108 End 5
                            
                            valueSet = false;
                            return ;
                        }
                        dt = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
                    }catch (ParseException parseException) {
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_FINAL_EXP_DATE));
                        
                        //Bug Fix:1108 Start 6
                        setRequestFocusInThread(jttAwardAmount.getSelectedRow(),FINAL_EXP_COL);
                        //Bug Fix:1108 End 6
                        
                        valueSet = false;
                        return ;
                    }
                    //Went thru All Date Validation. Good Date.
                    //check if old and new date is same
                    if(bean.getFinalExpirationDate() != null && dt.compareTo(bean.getFinalExpirationDate()) == 0) {
                        //same no need to update
                        return ;
                    }else {
                        bean.setFinalExpirationDate(new java.sql.Date(dt.getTime()));
                        bean.setEntryType(DIRECT_ENTRY);
                        if(bean.getAcType() == null) {
                            bean.setAcType(UPDATE_DATE);
                        }
                        setOblAndAntAmount(bean);
                    }
                    break ;
                 //3857 -- start
                case DIRECT_OBLIGATED_COL:
                    if(btnTotal.isSelected())
                        return;
                    // Get the enterd value                    
                    dblVal = Double.parseDouble(aValue.toString());                  
                    // Check for the action type , if the action type is any one of the mentioned one and
                    // if the enterd value is same then dont updated with the new record value
                    if((bean.getAcType() == null && dblVal ==0) ||
                            (bean.getAcType() != null &&
                            (bean.getAcType().equals(UPDATE_OBL_CHANGE) ||
                            bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                            bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE) ||
                            bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD)
                            || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                            )&&
                            dblVal == bean.getDirectObligatedChange())) {
                        return ;
                    }
                    if(setDirectObligated(bean, dblVal)) {
                        bean.setDirectObligatedChange(dblVal);                       
                        // set for the total change
                        double totalChange ;
                        
                          totalChange = bean.getDirectObligatedChange()+bean.getIndirectObligatedChange();
                          bean.setObligatedChange(totalChange);
//                         // set the action type to bean
                          //Commented with case 4543: Money and End dates bug fixes
                          //Modified with Case 4497: Problems with Money and End Dates
//                          double totalAmount = bean.getDirectObligatedTotal()+bean.getIndirectObligatedTotal();
//                          bean.setAmountObligatedToDate(totalAmount);
                          //4497 End
                          if(bean.getAcType() != null && bean.getAcType().startsWith(UPDATED_LAST_TIME)) {
                              //Updated Last time
                              if(bean.getAcType().equals(UPDATED_LAST_TIME)) {
                                  //updated last time and no modifications done
                                  bean.setAcType(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE);
                              }else if(bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE)) {
                                  //anticipated change already modified
                                  bean.setAcType(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD);
                              }
                          }else if(bean.getAcType() == null || bean.getAcType().equals(UPDATE_INDIRECT) || bean.getAcType().equals(UPDATE_DATE)){
                              //First Change
                              bean.setAcType(UPDATE_OBL_CHANGE);
                          }else if(bean.getAcType().equals(UPDATE_ANT_CHANGE)) {
                              //Anticipated Amount already changed.
                              bean.setAcType(TypeConstants.UPDATE_RECORD);
                          }
                          bean.setEntryType(DIRECT_ENTRY);
                    }else {
                        valueSet = false;
                    }
                    
                    break;
                case INDIRECT_OBLIGATED_COL:
                     if(btnTotal.isSelected())
                        return;
                      // Get the enterd value        
                     dblVal = Double.parseDouble(aValue.toString());
                    
                     // Check for the action type , if the action type is any one of the mentioned one and
                    // if the enterd value is same then dont updated with the new record value
                     if((bean.getAcType() == null && dblVal ==0) ||
                             (bean.getAcType() != null &&
                             (bean.getAcType().equals(UPDATE_OBL_CHANGE) ||
                             bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                             bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE) ||
                             bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD)
                             //Bug Fix : 1109 Step 1 - START
                             || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                             //Bug Fix : 1109  Step 1 - END
                             )&&
                             dblVal == bean.getIndirectObligatedChange())) {
                         return ;
                     }
                     if(setInDirectObligated(bean, dblVal)) {
                         // set indirect obligated amount to bean
                         bean.setIndirectObligatedAmount(dblVal);
                         double totalChange ;
                         // set for the total obligated change
                         //Commented with case 4543: Money and end date bug fixes
                         //Modified with Case 4497: Problems with Money and End Dates
//                         double totalAmount = bean.getDirectObligatedTotal()+bean.getIndirectObligatedTotal();
//                         bean.setAmountObligatedToDate(totalAmount);
                         //4497 End
                         totalChange = bean.getDirectObligatedChange()+bean.getIndirectObligatedChange();
                         bean.setObligatedChange(totalChange);
                         if(bean.getAcType() != null && bean.getAcType().startsWith(UPDATED_LAST_TIME)) {
                             //Updated Last time
                             if(bean.getAcType().equals(UPDATED_LAST_TIME)) {
                                 //updated last time and no modifications done
                                 bean.setAcType(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE);
                             }else if(bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE)) {
                                 //anticipated change already modified
                                 bean.setAcType(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD);
                             }
                         }else if(bean.getAcType() == null || bean.getAcType().equals(UPDATE_INDIRECT) || bean.getAcType().equals(UPDATE_DATE)){
                             //First Change
                             bean.setAcType(UPDATE_OBL_CHANGE);
                         }else if(bean.getAcType().equals(UPDATE_ANT_CHANGE)) {
                             //Anticipated Amount already changed.
                             bean.setAcType(TypeConstants.UPDATE_RECORD);
                         }
                         bean.setEntryType(DIRECT_ENTRY);
                     }else {
                        valueSet = false;
                    }
                      break;
                case DIRECT_ANTICIPATED_COL:
                     if(btnTotal.isSelected())
                        return;
                    // get the enterd value
                     dblVal = Double.parseDouble(aValue.toString());
                     
                     // Check for the action type , if the action type is any one of the mentioned one and
                    // if the enterd value is same then dont updated with the new record value     
                     if((bean.getAcType() == null && dblVal ==0) ||
                             (bean.getAcType() != null &&
                             (bean.getAcType().equals(UPDATE_ANT_CHANGE) ||
                             bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                             bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE) ||
                             bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD)
                             || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                             )&&
                             dblVal == bean.getDirectAnticipatedChange())) {
                         return ;
                     }

                     if(setDirectAnticipated(bean, dblVal)) {
                         
                         bean.setDirectAnticipatedChange(dblVal);
                         double totalAnticipatedChange;
                         totalAnticipatedChange =bean.getDirectAnticipatedChange() +bean.getIndirectAnticipatedChange();
                         bean.setAnticipatedChange(totalAnticipatedChange);
                         //Commented with case 4543: Money and end dates bug fixes
                         //Modified with Case 4497: Problems with Money and End Dates
//                         double totalAmount = bean.getDirectAnticipatedTotal()+bean.getIndirectAnticipatedTotal();
//                         bean.setAnticipatedTotalAmount(totalAmount);
                         //4497 End
                         if(bean.getAcType() != null && bean.getAcType().startsWith(UPDATED_LAST_TIME)) {
                             //Updated last time
                             if(bean.getAcType().equals(UPDATED_LAST_TIME)) {
                                 //updated last time and no modifications done
                                 bean.setAcType(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE);
                             }else if(bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE)) {
                                 //Obligated change already modified
                                 bean.setAcType(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD);
                             }
                         }else if(bean.getAcType() == null || bean.getAcType().equals(UPDATE_INDIRECT) || bean.getAcType().equals(UPDATE_DATE)){
                             //First Change
                             bean.setAcType(UPDATE_ANT_CHANGE);
                         }else if(bean.getAcType().equals(UPDATE_OBL_CHANGE)) {
                             //Obligated Amount already changed.
                             bean.setAcType(TypeConstants.UPDATE_RECORD);
                         }
                         bean.setEntryType(DIRECT_ENTRY);
                     }else {
                         valueSet = false;
                     }
                     break;
                    
                case INDIRECT_ANTICIPATED_COL:
                     if(btnTotal.isSelected())
                        return;
                    // get the enterd value
                    dblVal = Double.parseDouble(aValue.toString());
                    
                    if((bean.getAcType() == null && dblVal ==0) ||
                            (bean.getAcType() != null &&
                            (bean.getAcType().equals(UPDATE_ANT_CHANGE) ||
                            bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                            bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE) ||
                            bean.getAcType().equals(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD)
                            || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                            )&&
                            dblVal == bean.getIndirectAnticipatedChange())) {
                        return ;
                    }
                    
                    if(setInDirectAnticipated(bean, dblVal)) {
                        
                        bean.setIndirectAnticipatedChange(dblVal);
                        double totalAnticipatedChange;
                        totalAnticipatedChange =bean.getDirectAnticipatedChange() +bean.getIndirectAnticipatedChange();
                        bean.setAnticipatedChange(totalAnticipatedChange);
                        //Commented with case 4543: Money and end date bug fixes
                        //Modified with Case 4497: Problems with Money and End Dates
//                        double totalAmount = bean.getDirectAnticipatedTotal()+bean.getIndirectAnticipatedTotal();
//                        bean.setAnticipatedTotalAmount(totalAmount);
                        //4497:End
                        if(bean.getAcType() != null && bean.getAcType().startsWith(UPDATED_LAST_TIME)) {
                            //Updated last time
                            if(bean.getAcType().equals(UPDATED_LAST_TIME)) {
                                //updated last time and no modifications done
                                bean.setAcType(UPDATED_LAST_TIME + UPDATE_ANT_CHANGE);
                            }else if(bean.getAcType().equals(UPDATED_LAST_TIME + UPDATE_OBL_CHANGE)) {
                                //Obligated change already modified
                                bean.setAcType(UPDATED_LAST_TIME + TypeConstants.UPDATE_RECORD);
                            }
                        }else if(bean.getAcType() == null || bean.getAcType().equals(UPDATE_INDIRECT) || bean.getAcType().equals(UPDATE_DATE)){
                            //First Change
                            bean.setAcType(UPDATE_ANT_CHANGE);
                        }else if(bean.getAcType().equals(UPDATE_OBL_CHANGE)) {
                            //Obligated Amount already changed.
                            bean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        bean.setEntryType(DIRECT_ENTRY);
                    }else {
                        valueSet = false;
                    }
                 
            }
            //Modified with case 4561: Update multiple in money and end dates 
//               ((AbstractTableModel) jttAwardAmount.getModel()).fireTableDataChanged();
            //Added with case 4524:Money and End Dates in BIG Structure
//               setFocusInThread((selectedRow!=-1),selectedRow);
            //4524 End
            //Modified for COEUSDEV-283 : Award Money and End Dates: change ob exp date and press save; last change amount fills in on the line - Start
//            ((AbstractTableModel) jttAwardAmount.getModel()).fireTableRowsUpdated(selectedRow,selectedRow);
            if(selectedRow > -1){
                ((AbstractTableModel) jttAwardAmount.getModel()).fireTableRowsUpdated(selectedRow,selectedRow);
            }
            //COEUSDEV-283 : :End
            //4561 End
        }
        
        /**
         * Use this method when any Date is Modified and the Change amounts have to be set.
         * check if the amount in text and bean are same, else set the bean value to text value from
         * the table.
         */
        private void setOblAndAntAmount(AwardAmountInfoBean bean) {
            Double dblAntChange = (Double)getValueAt(bean, ANT_CHANGE_COL);
            Double dblObliChange = (Double)getValueAt(bean, OBL_CHANGE_COL);
            
            if(bean.getAnticipatedChange() != dblAntChange.doubleValue()) {
                bean.setAnticipatedChange(dblAntChange.doubleValue());
            }
            if(bean.getObligatedChange() != dblObliChange.doubleValue()) {
                bean.setObligatedChange(dblObliChange.doubleValue());
            }
            
        }
        
        private boolean setOblChange(AwardAmountInfoBean bean, double newValue){
            double oblChange, oblTotal, newTotal, oblDistributable, oblDistributed;
            
            if(bean.getAcType() != null &&
                    (bean.getAcType().equals(UPDATE_OBL_CHANGE) ||
                    bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                    bean.getAcType().startsWith(UPDATED_LAST_TIME)
                    //Bug Fix : 1109 Step 3 - START
                    || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                    //Bug Fix : 1109  Step 3 - END
                    )) {
                oblChange = newValue - bean.getObligatedChange();
            }else {
                oblChange = newValue;
            }
            
            oblTotal = bean.getAmountObligatedToDate();
            oblDistributable = bean.getObliDistributableAmount();
            
            double tempOblDistributed = oblTotal - oblDistributable;
            oblDistributed = ((double)Math.round(tempOblDistributed*Math.pow(10.0, 2) )) / 100;
            double tempNewTotal = oblTotal + oblChange;
            newTotal = ((double)Math.round(tempNewTotal*Math.pow(10.0, 2) )) / 100;
            
            if(newTotal < 0) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(OBL_TOT_LT_ZERO));
                int selectedRow = jttAwardAmount.getSelectedRow();
                awardAmountEditor.cancelCellEditing();
                jttAwardAmount.editCellAt(selectedRow, OBL_CHANGE_COL);
                awardAmountEditor.requestFocus();
                return false;
            }
            if(newTotal < oblDistributed) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(OBL_TOT_LT_OBL_DIST));
                return false;
            }
            
            //get parent AwardAmountInfo
            String parentMitNum = bean.getParentMitAwardNumber();
            if(!parentMitNum.equals(ROOT_AWARD_NUM)) {
                double parentOblDistributable;
                
                Equals eqMitAwdNum = new Equals("mitAwardNumber", parentMitNum);
                //Equals eqSeqNum = new Equals("sequenceNumber", new Integer(bean.getSequenceNumber()));
                //And awardNumAndSeqNum = new And(eqMitAwdNum, eqSeqNum);
                AwardAmountInfoBean parentBean = (AwardAmountInfoBean)cvAwardAmount.filter(eqMitAwdNum).get(0); //should always have one element
                parentOblDistributable = parentBean.getObliDistributableAmount();
                if(oblChange > parentOblDistributable) {
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INSUFF_FUNDS));
                    return false;
                }
                parentBean.setObliDistributableAmount(parentOblDistributable - oblChange);
                
                JTree tree = jttAwardAmount.getTree();
                TreePath path = tree.getSelectionPath();
                TreePath parentPath = path.getParentPath();
                int parentRow = jttAwardAmount.getTree().getRowForPath(parentPath);
                ((AbstractTableModel)jttAwardAmount.getModel()).fireTableRowsUpdated(parentRow, parentRow);
                
                //set entry type as I(Indirect) since this change is indirect to parent bean
                //only if this bean was not modified earlier.
                //i.e Direct entry has more prominence over Indirect entry.
                if(parentBean.getAcType() == null) {
                    parentBean.setEntryType(INDIRECT_ENTRY);
                    parentBean.setAcType(UPDATE_INDIRECT);
                    
                    //Bug Fix : 1181 - START
                    parentBean.setAnticipatedChange(0);
                    parentBean.setObligatedChange(0);
                    //Bug Fix : 1181 - END
                }
                
            }//End if for parent award num
            
            bean.setAmountObligatedToDate(newTotal);
            bean.setObliDistributableAmount(oblDistributable + oblChange);
            
            return true ;
        }//End setOblChange
      
        private boolean setAntChange(AwardAmountInfoBean bean, double newValue) {
            double antChange, newTotal, antTotal, antDistributable, antDistributed;
            
            if(bean.getAcType() != null &&
            (bean.getAcType().equals(UPDATE_ANT_CHANGE) ||
            bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
            bean.getAcType().startsWith(UPDATED_LAST_TIME)
            //Bug Fix : 1109 Step 4 - START
            || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
            //Bug Fix : 1109  Step 4 - END
            )) {
                antChange = newValue - bean.getAnticipatedChange();
            }else {
                antChange = newValue;
            }
            
            antTotal = bean.getAnticipatedTotalAmount();
            antDistributable = bean.getAnticipatedDistributableAmount();
            
            double tempAntDistributed = antTotal - antDistributable;
            antDistributed = (double)Math.round (tempAntDistributed*Math.pow(10.0, 2) ) / 100;
            double tempNewTotal = antTotal + antChange;
            newTotal = (double)Math.round (tempNewTotal*Math.pow(10.0, 2) ) / 100;
            if(newTotal < 0) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ANT_TOT_LT_ZERO));
                return false;
            }
            if(newTotal < antDistributed) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ANT_TOT_LT_ANT_DIST));
                return false;
            }
            
            //get parent AwardAmountInfo
            String parentMitNum = bean.getParentMitAwardNumber();
            if(! parentMitNum.equals(ROOT_AWARD_NUM)) {
                double parentAntDistributable;
                
                Equals eqMitAwdNum = new Equals("mitAwardNumber", parentMitNum);
                //Equals eqSeqNum = new Equals("sequenceNumber", new Integer(bean.getSequenceNumber()));
                //And awardNumAndSeqNum = new And(eqMitAwdNum, eqSeqNum);
                AwardAmountInfoBean parentBean = (AwardAmountInfoBean)cvAwardAmount.filter(eqMitAwdNum).get(0); //should always have one element
                parentAntDistributable = parentBean.getAnticipatedDistributableAmount();
                if(antChange > parentAntDistributable) {
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INSUFF_FUNDS));
                    return false;
                }
                parentBean.setAnticipatedDistributableAmount(parentAntDistributable - antChange);
                
                //update renderer
                TreePath parentPath = jttAwardAmount.getTree().getSelectionPath().getParentPath();
                int parentRow = jttAwardAmount.getTree().getRowForPath(parentPath);
                ((AbstractTableModel)jttAwardAmount.getModel()).fireTableRowsUpdated(parentRow, parentRow);
                
                //set entry type as I(Indirect) since this change is indirect to parent bean
                //only if this bean was not modified earlier.
                //i.e Direct entry has more prominence over Indirect entry.
                
                //Bug Fix: 1663 Start
                //if(parentBean.getEntryType() == null) {
                //    parentBean.setEntryType(INDIRECT_ENTRY);
                //}
                if(parentBean.getAcType() == null) {
                    parentBean.setEntryType(INDIRECT_ENTRY);
                    parentBean.setAcType(UPDATE_INDIRECT);
                }
                //Bug Fix: 1663 End
                
            }//End if for parent award num
            
            bean.setAnticipatedTotalAmount(newTotal);
            bean.setAnticipatedDistributableAmount(antDistributable + antChange);
            
            return true ;
        }//End setAntChange
        
////////////////////////////////////////////////////////////////////////////////////////////////////////////////        
        //3857 -- start       
        private boolean  setDirectObligated(AwardAmountInfoBean bean, double newValue) {
           
            
            double directObligatedChange, oblTotal, newTotal, oblDistributable, oblDistributed;
            if(bean.getAcType() != null &&
                    (bean.getAcType().equals(UPDATE_OBL_CHANGE) ||
                    bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                    bean.getAcType().startsWith(UPDATED_LAST_TIME)
                    || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                    )) {
                directObligatedChange = newValue - bean.getDirectObligatedChange();
            }else {
                directObligatedChange = newValue;
            }
            
            oblTotal = bean.getAmountObligatedToDate();
            oblDistributable = bean.getObliDistributableAmount();
            
            double tempOblDistributed = oblTotal - oblDistributable;
            oblDistributed = ((double)Math.round(tempOblDistributed*Math.pow(10.0, 2) )) / 100;
            
            double tempNewTotal = oblTotal + directObligatedChange;
            newTotal = ((double)Math.round(tempNewTotal*Math.pow(10.0, 2) )) / 100;
            
            if(newTotal < 0) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(OBL_TOT_LT_ZERO));
                int selectedRow = jttAwardAmount.getSelectedRow();
                awardAmountEditor.cancelCellEditing();
                jttAwardAmount.editCellAt(selectedRow, OBL_CHANGE_COL);
                awardAmountEditor.requestFocus();
                return false;
            }
            if(newTotal < oblDistributed) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(OBL_TOT_LT_OBL_DIST));
                return false;
            }
            
            //get parent AwardAmountInfo
            String parentMitNum = bean.getParentMitAwardNumber();
            if(!parentMitNum.equals(ROOT_AWARD_NUM)) {
                double parentOblDistributable;                
                Equals eqMitAwdNum = new Equals("mitAwardNumber", parentMitNum);               
                AwardAmountInfoBean parentBean = (AwardAmountInfoBean)cvAwardAmount.filter(eqMitAwdNum).get(0); //should always have one element
                parentOblDistributable = parentBean.getObliDistributableAmount();
                if(directObligatedChange > parentOblDistributable) {
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INSUFF_FUNDS));
                    return false;
                }
                parentBean.setObliDistributableAmount(parentOblDistributable - directObligatedChange);                
                JTree tree = jttAwardAmount.getTree();
                TreePath path = tree.getSelectionPath();
                //Modified for COEUSDEV-283 : Award Money and End Dates: change ob exp date and press save; last change amount fills in on the line - Start
               //Checks for treepath is not null 
                if(path != null){
                    TreePath parentPath = path.getParentPath();
                    int parentRow = jttAwardAmount.getTree().getRowForPath(parentPath);
                    ((AbstractTableModel)jttAwardAmount.getModel()).fireTableRowsUpdated(parentRow, parentRow);
                }
                //COEUSDEV-283 : End
                //set entry type as I(Indirect) since this change is indirect to parent bean
                //only if this bean was not modified earlier.
                //i.e Direct entry has more prominence over Indirect entry.
                if(parentBean.getAcType() == null) {
                    parentBean.setEntryType(INDIRECT_ENTRY);
                    parentBean.setAcType(UPDATE_INDIRECT);
                    parentBean.setAnticipatedChange(0);
                    parentBean.setObligatedChange(0);
                }
                
            }//End if for parent award num
            
            bean.setAmountObligatedToDate(newTotal);
            bean.setObliDistributableAmount(oblDistributable + directObligatedChange);
            
            double directObligatedTotal = bean.getDirectObligatedTotal() + directObligatedChange;
            bean.setDirectObligatedTotal(directObligatedTotal);

            return true ;
        }
        
        private boolean  setInDirectObligated(AwardAmountInfoBean bean, double newValue) {
            double indirectObligatedChange, oblTotal, newTotal, oblDistributable, oblDistributed;
            
            if(bean.getAcType() != null &&
                    (bean.getAcType().equals(UPDATE_OBL_CHANGE) ||
                    bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                    bean.getAcType().startsWith(UPDATED_LAST_TIME)
                    || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                    )) {
                indirectObligatedChange = newValue - bean.getIndirectObligatedChange();
            }else {
                indirectObligatedChange = newValue;
            }
            
            oblTotal = bean.getAmountObligatedToDate();
            oblDistributable = bean.getObliDistributableAmount();
            
            double tempOblDistributed = oblTotal - oblDistributable;
            oblDistributed = ((double)Math.round(tempOblDistributed*Math.pow(10.0, 2) )) / 100;
            double tempNewTotal = oblTotal + indirectObligatedChange;
            newTotal = ((double)Math.round(tempNewTotal*Math.pow(10.0, 2) )) / 100;
            
            if(newTotal < 0) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(OBL_TOT_LT_ZERO));
                int selectedRow = jttAwardAmount.getSelectedRow();
                awardAmountEditor.cancelCellEditing();
                jttAwardAmount.editCellAt(selectedRow, OBL_CHANGE_COL);
                awardAmountEditor.requestFocus();
                return false;
            }
            if(newTotal < oblDistributed) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(OBL_TOT_LT_OBL_DIST));
                return false;
            }
            
            //get parent AwardAmountInfo
            String parentMitNum = bean.getParentMitAwardNumber();
            if(!parentMitNum.equals(ROOT_AWARD_NUM)) {
                double parentOblDistributable;
                
                Equals eqMitAwdNum = new Equals("mitAwardNumber", parentMitNum);
                AwardAmountInfoBean parentBean = (AwardAmountInfoBean)cvAwardAmount.filter(eqMitAwdNum).get(0); //should always have one element
                parentOblDistributable = parentBean.getObliDistributableAmount();
                if(indirectObligatedChange > parentOblDistributable) {
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INSUFF_FUNDS));
                    return false;
                }
                parentBean.setObliDistributableAmount(parentOblDistributable - indirectObligatedChange);
                
                JTree tree = jttAwardAmount.getTree();
                TreePath path = tree.getSelectionPath();
                TreePath parentPath = path.getParentPath();
                int parentRow = jttAwardAmount.getTree().getRowForPath(parentPath);
                ((AbstractTableModel)jttAwardAmount.getModel()).fireTableRowsUpdated(parentRow, parentRow);
                
                //set entry type as I(Indirect) since this change is indirect to parent bean
                //only if this bean was not modified earlier.
                //i.e Direct entry has more prominence over Indirect entry.
                if(parentBean.getAcType() == null) {
                    parentBean.setEntryType(INDIRECT_ENTRY);
                    parentBean.setAcType(UPDATE_INDIRECT);
                    parentBean.setAnticipatedChange(0);
                    parentBean.setObligatedChange(0);
                }
                
            }//End if for parent award num
            
            bean.setAmountObligatedToDate(newTotal);
            bean.setObliDistributableAmount(oblDistributable + indirectObligatedChange);
            
            double indirectObligatedTotal = bean.getIndirectObligatedTotal() + indirectObligatedChange;
            bean.setIndirectObligatedTotal(indirectObligatedTotal);
            
            return true ;
        }
        
        private boolean setDirectAnticipated(AwardAmountInfoBean bean, double newValue) {
            double directAnticipatedChange, newTotal, antTotal, antDistributable, antDistributed;
            
            if(bean.getAcType() != null &&
                    (bean.getAcType().equals(UPDATE_ANT_CHANGE) ||
                    bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                    bean.getAcType().startsWith(UPDATED_LAST_TIME)
                    || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                    )) {
                directAnticipatedChange = newValue - bean.getDirectAnticipatedChange();
            }else {
                directAnticipatedChange = newValue;
            }
            
            antTotal = bean.getAnticipatedTotalAmount();
            antDistributable = bean.getAnticipatedDistributableAmount();
            
            double tempAntDistributed = antTotal - antDistributable;
            antDistributed = (double)Math.round(tempAntDistributed*Math.pow(10.0, 2) ) / 100;
            double tempNewTotal = antTotal + directAnticipatedChange;
            newTotal = (double)Math.round(tempNewTotal*Math.pow(10.0, 2) ) / 100;
            if(newTotal < 0) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ANT_TOT_LT_ZERO));
                return false;
            }
            if(newTotal < antDistributed) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ANT_TOT_LT_ANT_DIST));
                return false;
            }
            
            //get parent AwardAmountInfo
            String parentMitNum = bean.getParentMitAwardNumber();
            if(! parentMitNum.equals(ROOT_AWARD_NUM)) {
                double parentAntDistributable;
                
                Equals eqMitAwdNum = new Equals("mitAwardNumber", parentMitNum);
              
                AwardAmountInfoBean parentBean = (AwardAmountInfoBean)cvAwardAmount.filter(eqMitAwdNum).get(0); //should always have one element
                parentAntDistributable = parentBean.getAnticipatedDistributableAmount();
                if(directAnticipatedChange > parentAntDistributable) {
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INSUFF_FUNDS));
                    return false;
                }
                parentBean.setAnticipatedDistributableAmount(parentAntDistributable - directAnticipatedChange);
                
                //update renderer
                TreePath parentPath = jttAwardAmount.getTree().getSelectionPath().getParentPath();
                int parentRow = jttAwardAmount.getTree().getRowForPath(parentPath);
                ((AbstractTableModel)jttAwardAmount.getModel()).fireTableRowsUpdated(parentRow, parentRow);
                
                //set entry type as I(Indirect) since this change is indirect to parent bean
                //only if this bean was not modified earlier.
                //i.e Direct entry has more prominence over Indirect entry.
              
                if(parentBean.getAcType() == null) {
                    parentBean.setEntryType(INDIRECT_ENTRY);
                    parentBean.setAcType(UPDATE_INDIRECT);
                }
                
            }//End if for parent award num
            
            bean.setAnticipatedTotalAmount(newTotal);
            bean.setAnticipatedDistributableAmount(antDistributable + directAnticipatedChange);
            
            double directAnticipatedTotal = bean.getDirectAnticipatedTotal() + directAnticipatedChange;
            bean.setDirectAnticipatedTotal(directAnticipatedTotal);
            
            
            return true ;
        }
        
        private boolean setInDirectAnticipated(AwardAmountInfoBean bean, double newValue) {
            double indirectAnticipatedChange, newTotal, antTotal, antDistributable, antDistributed;
            
            if(bean.getAcType() != null &&
                    (bean.getAcType().equals(UPDATE_ANT_CHANGE) ||
                    bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ||
                    bean.getAcType().startsWith(UPDATED_LAST_TIME)
                    || bean.getAcType().equals(TypeConstants.INSERT_RECORD)
                    )) {
                indirectAnticipatedChange = newValue - bean.getIndirectAnticipatedChange();
            }else {
                indirectAnticipatedChange = newValue;
            }
            
            antTotal = bean.getAnticipatedTotalAmount();
            antDistributable = bean.getAnticipatedDistributableAmount();
            
            double tempAntDistributed = antTotal - antDistributable;
            antDistributed = (double)Math.round(tempAntDistributed*Math.pow(10.0, 2) ) / 100;
            double tempNewTotal = antTotal + indirectAnticipatedChange;
            newTotal = (double)Math.round(tempNewTotal*Math.pow(10.0, 2) ) / 100;
          
            if(newTotal < 0) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ANT_TOT_LT_ZERO));
                return false;
            }
           
            if(newTotal < antDistributed) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(ANT_TOT_LT_ANT_DIST));
                return false;
            }
            
            //get parent AwardAmountInfo
            String parentMitNum = bean.getParentMitAwardNumber();
            if(! parentMitNum.equals(ROOT_AWARD_NUM)) {
                double parentAntDistributable;
                
                Equals eqMitAwdNum = new Equals("mitAwardNumber", parentMitNum);
                AwardAmountInfoBean parentBean = (AwardAmountInfoBean)cvAwardAmount.filter(eqMitAwdNum).get(0); //should always have one element
                parentAntDistributable = parentBean.getAnticipatedDistributableAmount();
                if(indirectAnticipatedChange > parentAntDistributable) {
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INSUFF_FUNDS));
                    return false;
                }
                parentBean.setAnticipatedDistributableAmount(parentAntDistributable - indirectAnticipatedChange);
                
                //update renderer
                TreePath parentPath = jttAwardAmount.getTree().getSelectionPath().getParentPath();
                int parentRow = jttAwardAmount.getTree().getRowForPath(parentPath);
                ((AbstractTableModel)jttAwardAmount.getModel()).fireTableRowsUpdated(parentRow, parentRow);
                
                //set entry type as I(Indirect) since this change is indirect to parent bean
                //only if this bean was not modified earlier.
                //i.e Direct entry has more prominence over Indirect entry.
              
                if(parentBean.getAcType() == null) {
                    parentBean.setEntryType(INDIRECT_ENTRY);
                    parentBean.setAcType(UPDATE_INDIRECT);
                }
                
            }//End if for parent award num
            
            bean.setAnticipatedTotalAmount(newTotal);
            bean.setAnticipatedDistributableAmount(antDistributable + indirectAnticipatedChange);
         
            double indirectAnticipatedTotal = bean.getIndirectAnticipatedTotal() + indirectAnticipatedChange;
            bean.setIndirectAnticipatedTotal(indirectAnticipatedTotal);
            
            return true ;
        }
 
        // 3857  --- end
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
        
        
        public Object getRoot() {
            return root;
        }
        
        public void setRoot(AwardAmountInfoBean root) {
            this.root = root;
        }
        
        public boolean isLeaf(Object node) {
            return getChildCount(node) == 0;
        }
        
        //Bug Fix:Performance Issue (Out of memory) Start 4
        public void cleanUp(){
            root = null;
            cvAwardAmount = null;
            node = null;
            awardHierarchyDataMediator.cleanUp();
            awardHierarchyDataMediator = null;
        }
        //Bug Fix:Performance Issue (Out of memory) End 4
        
    }//End Inner Class Tree Table Model
    
    class AwardAmountRenderer extends DefaultTableCellRenderer {
        
        private DollarCurrencyTextField dollarCurrencyTextField;
        private CoeusTextField coeusTextField;
        private static final String DEFAULT_CHANGE_VAL = ".00";
        
        private JLabel txtEmpty;
        
        AwardAmountRenderer() {
            dollarCurrencyTextField = new DollarCurrencyTextField();
            dollarCurrencyTextField.setisNegativeAllowed(true);
            dollarCurrencyTextField.setBorder(border);
            
            DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
            decimalFormat.setMinimumIntegerDigits(0);
            decimalFormat.setMaximumIntegerDigits(10);
            
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(2);
            
            decimalFormat.setDecimalSeparatorAlwaysShown(true);
            coeusTextField = new CoeusTextField();
            FormattedDocument formattedDocument = new FormattedDocument(decimalFormat,coeusTextField);
            formattedDocument.setNegativeAllowed(true);
            coeusTextField.setDocument(formattedDocument);
            coeusTextField.setHorizontalAlignment(JFormattedTextField.RIGHT);
            coeusTextField.setBorder(border);
            
            txtEmpty = new JLabel();
            txtEmpty.setBackground(UIManager.getDefaults().getColor("Panel.background"));
            txtEmpty.setBorder(border);
            
            setBorder(border);
            setFont(CoeusFontFactory.getNormalFont());
        }
        
        public void setRendererBackground(Color bgColor) {
            coeusTextField.setBackground(bgColor);
            dollarCurrencyTextField.setBackground(bgColor);
            setBackground(bgColor);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int col = visibleColumns[column];
            String text = EMPTY;
            
            //if editable and first row set foreground color as blue
            /*if(editable && row == 0) {
                setForeground(Color.blue);
                dollarCurrencyTextField.setForeground(Color.blue);
                coeusTextField.setForeground(Color.blue);
            }else {
                setForeground(Color.black);
                dollarCurrencyTextField.setForeground(Color.black);
                coeusTextField.setForeground(Color.black);
            }*/
            
            //rowToColor will be initialized to -1 initially and will
            //be set to valid value by tree cell renderer
            if(rowToColor != -1 && rowToColor == row) {
                setForeground(Color.blue);
                dollarCurrencyTextField.setForeground(Color.blue);
                coeusTextField.setForeground(Color.blue);
            }else {
                setForeground(Color.black);
                dollarCurrencyTextField.setForeground(Color.black);
                coeusTextField.setForeground(Color.black);
            }
            
            TableModel tableModel = jttAwardAmount.getModel();
            
            Object finalExpDate = tableModel.getValueAt(row, FINAL_EXP_COL);
            
            if(isSelected) {
                setBackground(Color.yellow);
                dollarCurrencyTextField.setBackground(Color.yellow);
                coeusTextField.setBackground(Color.yellow);
            }else {
                 if(tableModel.isCellEditable(row,visibleColumns[column])) {
                    setBackground(Color.white);
                    dollarCurrencyTextField.setBackground(Color.white);
                    coeusTextField.setBackground(Color.white);
                }else {
                    setBackground(UIManager.getDefaults().getColor("Panel.background"));
                    dollarCurrencyTextField.setBackground(UIManager.getDefaults().getColor("Panel.background"));
                    coeusTextField.setBackground(UIManager.getDefaults().getColor("Panel.background"));
                }
            }
            
            switch (col) {
                case AWARD_AMT_TREE_COL:
                    break;
                case OBL_CHANGE_COL:                 
                case DIRECT_ANTICIPATED_COL:                
                case DIRECT_OBLIGATED_COL:
                case INDIRECT_ANTICIPATED_COL:                
                case INDIRECT_OBLIGATED_COL:                  
                case ANT_CHANGE_COL:
                    setHorizontalAlignment(JLabel.RIGHT);
                    if(value == null) {
                        text = DEFAULT_CHANGE_VAL;
                    }else {
                        text = value.toString();
                    }
                    
                    if((!editable) && (finalExpDate == null ||
                    finalExpDate.equals(EMPTY))) {
                        return txtEmpty;
                    }
                    
                    //bug fix : display mode color
                    coeusTextField.setText(text);
                    if(editable){
                        return coeusTextField;
                    }else {
                        text = coeusTextField.getText();
                    }
                    break;
                case OBL_DISTRIBUTED_COL:
                    
                case OBL_DISTRIBUTABLE_COL:
                    
                case OBL_TOTAL_COL:
                    
                case ANT_DISTRIBUTED_COL:
                    
                case ANT_DISTRIBUTABLE_COL:
                    
                case ANT_TOTAL_COL:
                    setHorizontalAlignment(JLabel.RIGHT);
                    
                    if((!editable) && (finalExpDate == null ||
                    finalExpDate.equals(EMPTY))) {
                        return txtEmpty;
                    }
                    
                    double dblVal;
                    if(value == null){
                        dblVal = 0;
                    }else{
                        dblVal = Double.parseDouble(value.toString());
                    }
                    
                    dollarCurrencyTextField.setValue(dblVal);
                    if(editable) {
                        return dollarCurrencyTextField;
                    }else {
                        text = dollarCurrencyTextField.getText();
                    }
                    break;
                case OBL_EFF_DATE_COL:
                    
                case OBL_EXP_DATE_COL:
                    
                case FINAL_EXP_COL:
                    if((!editable) && (finalExpDate == null ||
                    finalExpDate.equals(EMPTY))) {
                        return txtEmpty;
                    }
                    
                    setHorizontalAlignment(JLabel.LEFT);
                    if(value == null || value.equals(EMPTY)) {
                        text = EMPTY;
                    }else {
                        text = dateUtils.formatDate(value.toString(), DATE_FORMAT);
                    }
                    break;
              
            }
            setText(text);
            return this;
        }
        
        //Bug Fix:Performance Issue (Out of memory) Start 1
        public void cleanUp(){
            dollarCurrencyTextField = null;
            coeusTextField = null;
            txtEmpty = null;
        }
        //Bug Fix:Performance Issue (Out of memory) End 1
        
    }//End Inner Class AwardAmountRenderer
    
    
    class AwardAmountEditor extends javax.swing.AbstractCellEditor implements TableCellEditor {
        
        private CoeusTextField coeusTextField;
        private DecimalFormat decimalFormat;
        private CoeusTextField txtDate;
        
        private int column;
        
        AwardAmountEditor() {
            decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
            decimalFormat.setMinimumIntegerDigits(0);
            decimalFormat.setMaximumIntegerDigits(10);
            
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(2);
            
            decimalFormat.setDecimalSeparatorAlwaysShown(true);
            coeusTextField = new CoeusTextField();
            FormattedDocument formattedDocument = new FormattedDocument(decimalFormat,coeusTextField);
            formattedDocument.setNegativeAllowed(true);
            coeusTextField.setDocument(formattedDocument);
            
            
            coeusTextField.setHorizontalAlignment(JFormattedTextField.RIGHT);
            coeusTextField.setBorder(border);
            
            txtDate = new CoeusTextField();
            txtDate.setBorder(border);
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case OBL_CHANGE_COL:
                case DIRECT_ANTICIPATED_COL:
                
                case DIRECT_OBLIGATED_COL:
                case INDIRECT_ANTICIPATED_COL:
                
                case INDIRECT_OBLIGATED_COL:
                case ANT_CHANGE_COL:
                    return coeusTextField.getText().replaceAll(",", "");
                case OBL_EFF_DATE_COL:
                    
                case OBL_EXP_DATE_COL:
                    
                case FINAL_EXP_COL:
                    return txtDate.getText().trim();
               
                
            }
            return null;
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            int col = visibleColumns[column];
            this.column = col;
            switch (col) {
                case OBL_CHANGE_COL:                
                case DIRECT_ANTICIPATED_COL:
                case DIRECT_OBLIGATED_COL:
                case INDIRECT_ANTICIPATED_COL:
                case INDIRECT_OBLIGATED_COL:
                case ANT_CHANGE_COL:
                    coeusTextField.setText(value.toString());
                    return coeusTextField;
                case OBL_EFF_DATE_COL:
                    
                case OBL_EXP_DATE_COL:
                    
                case FINAL_EXP_COL:
                    if(value == null || value.equals(EMPTY)) {
                        txtDate.setText(EMPTY);
                    }else {
                        txtDate.setText(dateUtils.formatDate(value.toString(), SIMPLE_DATE_FORMAT));
                    }
                    return txtDate;
            }
            return null;
        }
        
        public void requestFocus() {
            switch (column) {
                case OBL_CHANGE_COL:
                case DIRECT_ANTICIPATED_COL:
                
                case DIRECT_OBLIGATED_COL:
                case INDIRECT_ANTICIPATED_COL:
                
                case INDIRECT_OBLIGATED_COL:
                case ANT_CHANGE_COL:
                    SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            coeusTextField.requestFocus();
                        }
                    });
                case OBL_EFF_DATE_COL:
                    
                case OBL_EXP_DATE_COL:
                    
                case FINAL_EXP_COL:
                    SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            txtDate.requestFocus();
                        }
                    });
            }
        }
        
        public void requestFocus(int column) {
            switch (column) {
                case OBL_CHANGE_COL:
                case DIRECT_ANTICIPATED_COL:
                
                case DIRECT_OBLIGATED_COL:
                case INDIRECT_ANTICIPATED_COL:
                
                case INDIRECT_OBLIGATED_COL:
                case ANT_CHANGE_COL:
                    SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            coeusTextField.requestFocus();
                        }
                    });
                case OBL_EFF_DATE_COL:
                    
                case OBL_EXP_DATE_COL:
                    
                case FINAL_EXP_COL:
                    SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            txtDate.requestFocus();
                        }
                    });
            }
        }
        
        //Bug Fix:Performance Issue (Out of memory) Start 2
        public void cleanUp(){
            coeusTextField = null;
            decimalFormat = null;
            txtDate = null;
        }
        //Bug Fix:Performance Issue (Out of memory) End 2
        
    }//End Inner Class AwardAmountEditor
    
    
    
    class AmountTreeCellRenderer extends JLabel implements TreeCellRenderer {
        
        private AwardAmountInfoBean awardAmountInfoBean;
        private ImageIcon green, yellow, blue, red;
        private String label;
        
        AmountTreeCellRenderer() {
            green = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.GREEN));
            yellow = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.YELLOW));
            blue = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.BLUE));
            red = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.RED));
            //JIRA COEUSQA 2871 - START
            if(maxAccountNumberLength > 0){//increase the label size as per account number
                this.setPreferredSize(new Dimension(200+(maxAccountNumberLength*10), jttAwardAmount.getRowHeight()));
            }else{
            //Bug Fix : 1037 - START
            this.setPreferredSize(new Dimension(200, jttAwardAmount.getRowHeight()));
            //Bug Fix : 1037 - END
            }
            //JIRA COEUSQA 2871 - END
        }
        
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            
            int level = 1;
            TreePath rowPath = tree.getPathForRow(row);
            if(rowPath != null) {
                level = rowPath.getPathCount();
            }
            awardAmountInfoBean = (AwardAmountInfoBean)value;
            
            if(awardColor != null && awardToColor.equals(awardAmountInfoBean.getMitAwardNumber())) {
                setForeground(awardColor);
                rowToColor = row;
            }else {
                setForeground(Color.black);
            }
            
            //set icon
            int statusCode = awardAmountInfoBean.getStatusCode();
            switch (statusCode) {
                case 1:
                    setIcon(green);
                    break;
                case 3:
                    setIcon(yellow);
                    break;
                case 6:
                    setIcon(blue);
                    break;
                default:
                    setIcon(red);
            }
            
            label = " [ "+level+" ] "+awardAmountInfoBean.getMitAwardNumber();
            if(awardAmountInfoBean.getAccountNumber() != null && !awardAmountInfoBean.getAccountNumber().trim().equals(EMPTY)) {
                label = label + " : " + awardAmountInfoBean.getAccountNumber();
            }
            
            setText(label);
            
            //resizing column size on the fly
            //NOTE : has problem comsumes 100% CPU since renderer is internally called
                /*int prefWidth = jttAwardAmount.getColumnModel().getColumn(AWARD_AMT_TREE_COL).getPreferredWidth();
                if(prefWidth != (level * NODE_DIFF_WIDTH) && ((level * NODE_DIFF_WIDTH)) >= TREE_COL_WIDTH){
                    prefWidth = level * NODE_DIFF_WIDTH;
                    jttAwardAmount.getColumnModel().getColumn(AWARD_AMT_TREE_COL).setPreferredWidth(prefWidth);
                }
                 */
          return this;
        }
        
    }//End AmountTreeCellRenderer
    
    //Bug Fix:1108 Start 7
    public  void setRequestFocusInThread(final int selrow , final int selcol){
        final int columnSelected = getVisibleColumnIndex(selcol);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Added for COEUSDEV-283 : Award Money and End Dates: change ob exp date and press save; last change amount fills in on the line - Start
                if(selrow < 0 ){
                    return;
                }
                //COEUSDEV-283 : End
                jttAwardAmount.requestFocusInWindow();
                jttAwardAmount.changeSelection( selrow, columnSelected, true, false);
                jttAwardAmount.setRowSelectionInterval(selrow, selrow);
            }
        });
    }
    
    private int getVisibleColumnIndex(int columnSelectedIndex){
        for (int index = 0 ; index < visibleColumns.length ; index++ ){
            if(visibleColumns[index] == columnSelectedIndex){
                return index;
            }
        }
        return -1;
    }
    //Bug Fix:1108 End 7
    
    //Bug Fix:Performance Issue (Out of memory) Start 3
    public void cleanUp(){
        dateUtils = null;
        border = null;
        simpleDateFormat = null;
        coeusMessageResources = null;
        rootAwardAmount = null;
        
        if(cvAwardAmount != null){
            for(int index  = 0; index<cvAwardAmount.size();index++){
                AwardAmountInfoBean amountInfoBean = (AwardAmountInfoBean)cvAwardAmount.get(index);
                amountInfoBean = null;
            }
            cvAwardAmount = null;
        }
        
//        horizantalScroll.removeAdjustmentListener(this);
//        horizantalScroll = null;
//        jttAwardAmount.getColumnModel().removeColumnModelListener(this);
        jttAwardAmount.cleanUp();
        jttAwardAmount = null;
        awardAmountTreeTableModel.cleanUp();
        awardAmountTreeTableModel = null;
        awardAmountRenderer.cleanUp();
        awardAmountRenderer = null;
        awardAmountEditor.cleanUp();
        awardAmountEditor = null;
        awardAmountColumnModel = null;
    }
    //Bug Fix:Performance Issue (Out of memory) End  3
    
     //#CAse3857 -- start
    // Swaps the position of lableHeader1 and sets the haeder value
    // based on the wheather direct/indirect button is selected
    /*private void setLableHeader1(String headerValue) {
        lblHeading1.setText(headerValue);
        tmpHeader1 = headerValue;
        setLableHeader(isDirectIndirectSelected);
    }
    
    // Swaps the position of lableHeader2 and sets the haeder value
    // based on the wheather direct/indirect button is selected
    private void setLableHeader2(String headerValue) {
        lblHeading2.setText(headerValue);
        tmpHeader2 = headerValue;
        setLableHeader(isDirectIndirectSelected);
    }*/
    
     /**
     * check wheather ENABLE_AWD_ANT_OBL_DIRECT_INDIRECT_COST is enabled or not
     * @return true if ENABLE_AWD_ANT_OBL_DIRECT_INDIRECT_COST is 1 , false otherwise
     */
    public boolean  getEnableDirectInDirectCost(){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/coeusFunctionsServlet";
        final String PARAMETER = "ENABLE_AWD_ANT_OBL_DIRECT_INDIRECT_COST";
        String value = "0";
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setDataObject("GET_PARAMETER_VALUE");
        Vector vecParameter = new Vector();
        vecParameter.add(PARAMETER);
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder !=null && responder.isSuccessfulResponse()){
            value =(String) responder.getDataObject();
        }
        if(value.equals("0"))
            return false;
        else 
            return true;
       
    }
   
    // initializes the variables
    private void initializeVariables() {
        //Modified with case 4543: Money and end date Ui Changes
        String OBLI_DISTRIBUTED = "<html>Obligated<br>Distributed</html>";
        String OBLI_DISTRIBUTABLE = "<html>Obligated<br>Distributable</html>";
        String OBLI_CHANGE = "<html>Obligated<br>Change</html>";
        String TOTAL_OBLIGATED = "<html>Obligated<br>Total</html>";
        String ANTI_DISTRIBUTED = "<html>Anticipated<br>Distributed</html>";
        String ANTI_DISTRIBUTABLE = "<html>Anticipated<br>Distributable</html>";
        String ANTI_CHANGE = "<html>Anticipated<br>Change</html>";
        String TOTAL_ANTICIPATED = "<html>Anticipated<br>Total</html>";
        String OBLI_EFF_DATE = "<html>Obligation<br>Effective Date</html>";
        String OBLI_EXP_DATE = "<html>Obligation<br>Expiration Date</html>";
        String FINAL_EXP_DATE = "<html>Final<br>Expiration Date</html>";
        
        SET_DITRECT_INDIRECT = getEnableDirectInDirectCost();
        if(SET_DITRECT_INDIRECT) {
            
            colNames =  new String[]{EMPTY, OBLI_DISTRIBUTED , OBLI_DISTRIBUTABLE , OBLI_CHANGE , TOTAL_OBLIGATED ,
                                            ANTI_DISTRIBUTED , ANTI_DISTRIBUTABLE , ANTI_CHANGE , TOTAL_ANTICIPATED ,
                                            OBLI_EFF_DATE , OBLI_EXP_DATE , FINAL_EXP_DATE , EMPTY ,
                                            DIRECT_OBLIGATED_CHANGE, INDIRECT_OBLIGATED_CHANGE,
                                            DIRECT_ANTICIPATED_CHANGE, INDIRECT_ANTICIPATED_CHANGE};
//            colNames= new String[] {" ", OBLIGATED, OBLIGATED, OBLIGATED, OBLIGATED,
//            ANTICIPATED, ANTICIPATED, ANTICIPATED, ANTICIPATED,
//            "Oblg. eff", "Oblg. exp", "Final exp", " ", DIRECT_OBLIGATED, INDIRECT_OBLIGATED,
//            DIRECT_ANTICIPATED, INDIRECT_ANTICIPATED};
            
            colClass = new Class[]{TreeTableModel.class, Double.class, Double.class, Double.class, Double.class,
            Double.class, Double.class, Double.class, Double.class,
            Date.class, Date.class, Date.class, String.class, Double.class, Double.class, Double.class, Double.class};
            
            colEditable =new boolean[]  {false, false, false, false, false, false, false, false,
            false, true, true, true, false,true,true,true,true};
            
            colVisible =new boolean[]  {true, false, false, true, false, false, false, true,
            false, true, true, true, false,false,false,false,false};
            
            visibleColumns = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,13,14,15,16};
            
            tableColumns = new TableColumn[colNames.length];
            btnDirectInDirect.setVisible(true);
        } else {
             btnDirectInDirect.setVisible(false);
            colNames= new String[] {EMPTY , OBLI_DISTRIBUTED , OBLI_DISTRIBUTABLE , OBLI_CHANGE , TOTAL_OBLIGATED ,
                                            ANTI_DISTRIBUTED , ANTI_DISTRIBUTABLE , ANTI_CHANGE , TOTAL_ANTICIPATED ,
                                            OBLI_EFF_DATE , OBLI_EXP_DATE , FINAL_EXP_DATE, EMPTY};
            
            colClass = new Class[] {TreeTableModel.class, Double.class, Double.class, Double.class, Double.class,
            Double.class, Double.class, Double.class, Double.class,
            Date.class, Date.class, Date.class, String.class};
            
            colEditable =new boolean[] {false, false, false, true, false, false, false, true,
            false, true, true, true, false };
            
            colVisible =new boolean[]   {true, false, false, true, false, false, false, true,
            false, true, true, true, false };
            
            visibleColumns = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
            
            tableColumns = new TableColumn[colNames.length];
        }
    }


    /*
     * Return true , if the direct/indirect button is selected
     * otherwise return false.
     */
    public boolean isIsDirectIndirectSelected() {
        return isDirectIndirectSelected;
    }
    
   /*
    * Set the lable header value and sets the value for is DirectInDirect Selected.
    */
    public void setIsDirectIndirectSelected(boolean isDirectIndirectSelected) {
//        setLableHeader(isDirectIndirectSelected);
        this.isDirectIndirectSelected = isDirectIndirectSelected;
    }
    
    // Swaps the position and sets the haeder value
    // based on direct indirect selection/
    /*private void setLableHeader(boolean isDirectIndirectSelected) {
        if(isDirectIndirectSelected && btnDates.isSelected()) {
            lableHeader3.setText(tmpHeader1);
            lableHeader4.setText(tmpHeader2);
            lblHeading1.setText("");
            lblHeading2.setText("");
        } else {
            lblHeading1.setText(tmpHeader1);
            lblHeading2.setText(tmpHeader2);
            lableHeader3.setText("");
            lableHeader4.setText("");
        }
    }*/
    //#Case3857 -- end
}//End AwardAmountTreeTable

class EmptyRenderer extends JLabel implements TableCellRenderer {
    
    public EmptyRenderer() {
        setOpaque(true);
        setBackground(UIManager.getDefaults().getColor("Panel.background"));
    }
    
    /**
     *  Returns the component used for drawing the cell.  This method is
     *  used to configure the renderer appropriately before drawing.
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}//End EmptyRenderer

