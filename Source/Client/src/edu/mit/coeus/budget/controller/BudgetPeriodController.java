/*
 * BudgetPeriodController.java
 *
 * Created on October 8, 2003, 10:05 AM
 */
/* PMD check performed, and commented unused imports and variables 
 * on 25-JULY-2011 by Satheesh Kumar K N
 */

package edu.mit.coeus.budget.controller;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author sharathk
 */

import edu.mit.coeus.budget.calculator.BudgetPeriodCalculator;
//import edu.mit.coeus.rates.bean.CERatesBean;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.text.*;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.budget.gui.*;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.controller.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.vanderbilt.coeus.utils.CustomFunctions;
import edu.mit.coeus.departmental.bean.DepartmentBudgetFormBean;
import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.propdev.bean.ProposalHierarchyBean;

/** Controller for Budget Period.
 * All Budget Period Functionality, Validation and
 * Event Handling Code for Budget Period is written here.
 */
public class BudgetPeriodController extends Controller
implements FocusListener, BeanUpdatedListener, BeanAddedListener, AdjustmentListener
, TableColumnModelListener, ActionListener
{
    
    /** Holds instance of BudgetPeriodForm which is being controlled by this. */
    private BudgetPeriodForm budgetPeriodForm;

    /** Holds budget detail bean. */
    private BudgetBean budgetBean;
    private BudgetInfoBean budgetInfoBean;
    private QueryEngine queryEngine;
    private String queryKey;
    
    private BudgetPeriodBean budgetPeriodBean;
    private CoeusVector vecBudgetLineItem;
    
    private DateUtils dateUtils;
    
    private BudgetPeriodTableModel budgetPeriodTableModel;
    private BudgetPeriodRenderer budgetPeriodRenderer;
    private BudgetPeriodEditor budgetPeriodEditor;
    
    private BudgetJustifyTableModel budgetJustifyTableModel;
    private BudgetJustifyRenderer budgetJustifyRenderer;
    private BudgetJustifyEditor budgetJustifyEditor;
    
    private BudgetListSelectionListener budgetListSelectionListener;
    
    
    private CostElementsLookupWindow costElementsLookupWindow;
    private BudgetLineItemController budgetLineItemController;
    private PersonnelBudgetDetailController personnelBudgetDetailController;
    private FormulatedCostBudgetLineItemController formulatedCostBudgetLineItemController;
    private CustomizeViewController customizeViewController;
    
    private SimpleDateFormat simpleDateFormat;
    
    /** holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources;
    
    private boolean refreshRequired;
    
    private boolean columnsMoved;
    
    private boolean groupByMode;
    
    
    private static final String EMPTY_STRING = "";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    private static final String ZERO = "0";
    
    private static final  int LINE_COLUMN = 0;
    private static final int COST_ELEMENT_COLUMN = 1;
    private static final int COST_ELEMENT_DESCRIPTION_COLUMN = 2;
    private static final int DESCRIPTION_COLUMN = 3;
    private static final int QUANTITY_COLUMN = 4;
    private static final int COST_COLUMN = 5;
    private static final int START_DATE_COLUMN = 6;
    private static final int END_DATE_COLUMN = 7;
    
    private static final int CATEGORY_COLUMN = 8;
    private static final int UNDERRECOVERY_COLUMN = 9;
    private static final int COST_SHARE_COLUMN = 10;
    private static final int CAMPUS_FLAG_COLUMN = 11;
    
    private static final String ON = "On";
    private static final String OFF = "Off";
    // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
    private DateUtils dtUtils = new DateUtils();
    //3197 - end
    private Class colTypes[] =
    //Modified for Case # 3132 - start
   //Changing quantity field from integer to float
//    {String.class, String.class, String.class, String.class,
//     Integer.class, Double.class, Date.class, Date.class,
//     String.class, Double.class, Double.class, String.class
//    };
    {String.class, String.class, String.class, String.class,
     Double.class, Double.class, Date.class, Date.class,
     String.class, Double.class, Double.class, String.class
    };
    //Modified for Case # 3132 - end
    
    private String colNames[] =
    {"Line","CE","Cost Element Description","Description","Qnty",
     "Cost","Start Date","End Date", "Category", "Underrecovery",
     "Cost Share","Campus"
    };
    
    private boolean colEditable[] =
    {false, true, false, true, true, true, true, true,
     false, false, false, false
    };
    
    private boolean colVisible[] =
    {true, true, true, true, true, true, true, true, 
     false, false, false, false
    };
    private TableColumn tableColumns[] = new TableColumn[colNames.length];
    
    private int visibleColumns[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    
    private static final char PERSONNEL = 'P';

    //Constants for calculation
    private static final int TOTAL_DIRECT_COST = 1;
    private static final int TOTAL_INDIRECT_COST = 2;
    private static final int COST_SHARING_AMOUNT = 3;
    private static final int UNDERRECOVERY = 4;
    
    private static final int LINE_ITEM_ROW_HEIGHT = 22;
    
    /**
     * Start date for the period cannot be earlier than Start date of the budget.
     */
    private static final String PERIOD_START_DATE_BEFORE_BUDGET_START_DATE = "budget_period_exceptionCode.1151";
    
    /**
     * Start date for the period cannot be earlier than End date of the previous period.
     */
    private static final String PERIOD_START_DATE_BEFORE_PREV_PERIOD_END_DATE = "budget_period_exceptionCode.1152";
    
    /**
     * Start date cannot be later than End date.
     */
    private static final String START_DATE_LATER_THAN_END_DATE = "budget_period_exceptionCode.1153";
    
    private static final String START_DATE_CHANGE = "Changing the start date of a budget period will change the start date of individual line items in the period. Do you want to change the start date to ";
    
    /**
     * End date for the period cannot be later than End date of the budget.
     */
    private static final String PERIOD_END_DATE_LATER_THAN_BUDGET_END_DATE = "budget_period_exceptionCode.1154";
    
    /**
     * End date for the period cannot be later than Start date of the next period.
     */
    private static final String PERIOD_END_DATE_LATER_THAN_NEXT_PERIOD_START_DATE = "budget_period_exceptionCode.1155";
    
    /**
     * End date cannot be earlier than Start date.
     */
    private static final String END_DATE_EARLIER_THAN_STRAT_DATE = "budget_period_exceptionCode.1156";
    
    private static final String END_DATE_CHANGE = "Changing the end date of a budget period will change the end date of individual line items in the period. Do you want to change the end date to ";
    
    /**
     * Invalid Start date. Please Input a valid Start date.
     */
    private static final String INVALID_START_DATE = "budget_period_exceptionCode.1157";
    
    /**
     * Invalid End date. Please Input a valid End date.
     */
    private static final String INVALID_END_DATE = "budget_period_exceptionCode.1158";
    
    /**
     * Start date should be between start and end date of the period.
     */
    private static final String START_DATE_BETWEEN_START_END_DATE_OF_PERIOD = "budget_period_exceptionCode.1159";
    
    /**
     * End date should be between start and end date of the period.
     */
    private static final String END_DATE_BETWEEN_START_END_DATE_OF_PERIOD = "budget_period_exceptionCode.1160";
    
    /**
     * Invalid Cost Element. Please input a valid Cost Element
     */
    private static final String INVALID_COST_ELEMENT = "budget_period_exceptionCode.1161";
    
    /**
     * Do you want to delete the current Line Item ?.
     */
    private static final String DELETE_LINE_ITEM = "budget_period_exceptionCode.1162";
    
    /**
     * This line item contains personel budget details. Cannot be deleted.
     */
    private static final String LINE_ITEM_CONTAINS_PERSONNEL_DETAILS = "budget_period_exceptionCode.1163";
    
    /**
     * The selected line item is not of personnel category
     */
    private static final String SELECTED_ITEM_IS_NOT_OF_PERSONNEL_CATEGORY = "budget_period_exceptionCode.1164";
    
    /**
     * Please select a line item.
     */
    private static final String SELECT_LINE_ITEM = "budget_period_exceptionCode.1165";
    
    /**
     * Cannot perform this operation if cost element is not present.
     */
    private static final String COST_ELEMENT_NOT_PRESENT = "budget_period_exceptionCode.1166";
    
    /**
     * This is the last period of the budget. Cannot perform this operation on last period of the budget.
     */
    private static final String CANNOT_PERFORM_THIS_OPERATION_ON_LAST_PERIOD_OF_BUDGET = "budget_period_exceptionCode.1167";
    /**
     * Cannot perform this operation on a line item with personel budget details.
     */
    private static final String CANNOT_PERFORM_THIS_OPERATION_ON_PERSONNEL_LINE_ITEM = "Cannot perform this operation on a line item with personel budget details.";
    
    /**
     * Cost limit for this period is set to 0. Cannot sync a line item cost to zero limit.
     */
    private static final String CANNOT_SYNC_TO_ZERO_LIMIT = "Cost limit for this period is set to 0. Cannot sync a line item cost to zero limit.";
    
    /**
     * Cost limit and total cost for this period is already in sync.
     */
    private static final String TOTAL_COST_ALREADY_IN_SYNC = "Cost limit and total cost for this period is already in sync.";
    
    /**
     * Period total is greater than the cost limit for this period.Do you want to reduce this line item cost to make the total cost same as cost limit
     */
    private static final String REDUCE_LINE_ITEM_COST = "Period total is greater than the cost limit for this period.Do you want to reduce this line item cost to make the total cost same as cost limit";
    
    /**
     * Insufficient amount on the line item to sync with cost limit.
     */
    private static final String  INSUFFICIENT_AMOUNT_TO_SYNC = "Insufficient amount on the line item to sync with cost limit.";
    
    private static final String COST_ELEMENT_LOOKUP_TITLE = "Cost Elements";
    
    private boolean addActionisPerformed;
    private boolean parentProposal;
    private ProposalHierarchyBean proposalHierarchyBean;
    // Added for Bug fix case#2948 - Start 1       
    private int selectedRow;
    // Added for Bug fix case#2948 - End 1        
    private boolean tuitionFeeAutoCalculation = false;
    private static final char GET_COST_ELEMENT_PERIOD = 'x';
    private static final char GET_COST_ELEMENT_RATE = 'z';
//    private static final String COST_ELEMENT = "costElement";
    private CoeusVector cvRatesData;
    //Code added for Case#3472 - Sync to Direct Cost Limit - starts
    //For adding total direct cost limit
    private static final String TOTAL_DIRECT_COST_ALREADY_IN_SYNC = "budget_period_exceptionCode.1168";
    private static final String REDUCE_LINE_ITEM_COST_TO_DIRECT_COST = "budget_period_exceptionCode.1169";
    private static final String  INSUFFICIENT_AMOUNT_TO_SYNC_DIRECT_COST = "budget_period_exceptionCode.1170";
    private static final String CANNOT_SYNC_TO_ZERO_DIRECT_COST_LIMIT = "budget_period_exceptionCode.1171";
    //Code added for Case#3472 - Sync to Direct Cost Limit - ends
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    private static final String LINE_ITEM_FROM_SUBAWARD_CANT_DELETE = "budget_period_exceptionCode.1172";
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    private static final String FORMULATED_COST_LINE_ITEM_CANT_DELETE = "budget_period_exceptionCode.1173";
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
    // Added for COEUSQA-3806 : Formulated Cost Line Items should not be eligible for Sync to Direct or Total Cost functionality in Budget - Start
    private static final String FORMULATED_COST_LINE_ITEM_SYNC_COST_LIMIT = "budget_period_exceptionCode.1174";
    // Added for COEUSQA-3806 : Formulated Cost Line Items should not be eligible for Sync to Direct or Total Cost functionality in Budget - End
    
    /** Creates a new instance of BudgetPeriodController
     * @param budgetBean budget bean
     */
    public BudgetPeriodController() {
    }
    
    public BudgetPeriodController(BudgetBean budgetBean) {
        this.budgetBean = budgetBean;
        cvRatesData = new CoeusVector();
        budgetPeriodForm = new BudgetPeriodForm();
        dateUtils = new DateUtils();
        budgetPeriodTableModel = new BudgetPeriodTableModel();
        budgetJustifyTableModel = new BudgetJustifyTableModel();
        budgetPeriodRenderer = new BudgetPeriodRenderer();
        budgetJustifyRenderer = new BudgetJustifyRenderer();
        budgetJustifyEditor = new BudgetJustifyEditor();
        budgetPeriodEditor = new BudgetPeriodEditor();
        budgetListSelectionListener = new BudgetListSelectionListener();
        
        budgetLineItemController = new BudgetLineItemController();
        personnelBudgetDetailController = new PersonnelBudgetDetailController();
        formulatedCostBudgetLineItemController = new FormulatedCostBudgetLineItemController(getFunctionType());
        customizeViewController = new CustomizeViewController(CoeusGuiConstants.getMDIForm(), true);
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        queryEngine = QueryEngine.getInstance();
        queryKey = budgetBean.getProposalNumber()+budgetBean.getVersionNumber();      
        
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        
        budgetPeriodForm.pnlLineItemCalculatedAmounts.setSaveImmediately(true);
        
        registerComponents();
        setTableKeyTraversal();
    }
    
    /** perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public void formatFields() {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
            budgetPeriodForm.pnlLineItemCalculatedAmounts.tblCalculatedAmounts.setEnabled(false);
        }
        
        budgetPeriodForm.pnlLineItemCalculatedAmounts.setVisible(false);
        
        budgetLineItemController.setFunctionType(getFunctionType());
        personnelBudgetDetailController.setFunctionType(getFunctionType());
        formulatedCostBudgetLineItemController.setFunctionType(getFunctionType());
                
        budgetPeriodForm.txtCostSharing.setEnabled(false);
        budgetPeriodForm.txtDirectCost.setEnabled(false);
        budgetPeriodForm.txtIndirectCost.setEnabled(false);
        budgetPeriodForm.txtUnderrecovery.setEnabled(false);
        budgetPeriodForm.txtTotalCost.setEnabled(false);
        // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
        budgetPeriodForm.txtPeriodMonths.setEnabled(false);
        //3197- end
        
        budgetPeriodForm.txtCostSharing.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        budgetPeriodForm.txtDirectCost.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        budgetPeriodForm.txtIndirectCost.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        budgetPeriodForm.txtUnderrecovery.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        budgetPeriodForm.txtTotalCost.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
       // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
        budgetPeriodForm.txtPeriodMonths.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
       //3197- end  
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
            budgetPeriodForm.txtCostLimit.setEnabled(false);
            budgetPeriodForm.txtTotalCost.setEnabled(false);
            budgetPeriodForm.txtEndDate.setEnabled(false);
            budgetPeriodForm.txtStartDate.setEnabled(false);
            //Code added for Case#3472 - Sync to Direct Cost Limit
            //For adding total direct cost limit
            budgetPeriodForm.txtDirectCostLimit.setEnabled(false);
            
            budgetPeriodForm.txtCostLimit.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            budgetPeriodForm.txtTotalCost.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            budgetPeriodForm.txtEndDate.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            budgetPeriodForm.txtStartDate.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            
            budgetPeriodForm.tblPeriodLineItem.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("TabbedPane.tabAreaBackground"));
            //Code added for Case#3472 - Sync to Direct Cost Limit
            //For adding total direct cost limit
            budgetPeriodForm.txtDirectCostLimit.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("TabbedPane.tabAreaBackground"));
        }
        
        /* JM 5-25-2016 get show calculated amounts parameter */
    	CustomFunctions custom = new CustomFunctions();
    	String[] params = (String[]) custom.getParameterValues("BUDGET_SHOW_CALCULATED_AMTS");
    	boolean showCalculatedAmts = "1".equals(params[0]);
        CustomizeViewForm customizeViewForm = (CustomizeViewForm) customizeViewController.getControlledUI();
    	customizeViewForm.chkShowCalculatedAmts.setSelected(showCalculatedAmts);;
    	budgetPeriodForm.pnlLineItemCalculatedAmounts.setVisible(true); 
    	/* JM END */
        
    }
    
    /** returns the controlled Componenet.
     * @return controlled Component.
     */
    public Component getControlledUI() {
        return budgetPeriodForm;
    }
    
    /** returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return budgetPeriodBean;
    }
    
   
    
    /** registers component with event listeners.
     * sets up the budget period table.
     */
    public void registerComponents() {
        //Code for focus traversal - start
        
        java.awt.Component[] components = { budgetPeriodForm.txtStartDate,
        budgetPeriodForm.txtEndDate,
        budgetPeriodForm.txtCostLimit,
        budgetPeriodForm.txtTotalCost,
        budgetPeriodForm.txtDirectCost,
        budgetPeriodForm.txtIndirectCost,
        budgetPeriodForm.txtUnderrecovery,
        budgetPeriodForm.txtCostSharing,
       // budgetPeriodForm.tblPeriodLineItem 
        //Code added for Case#3472 - Sync to Direct Cost Limit
        //For adding total direct cost limit
        budgetPeriodForm.txtDirectCostLimit,
        budgetPeriodForm.scrPnPeriodLineItem
        };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        budgetPeriodForm.setFocusTraversalPolicy(traversePolicy);
        budgetPeriodForm.setFocusCycleRoot(true);
        //Code for focus traversal - end
        
        //Registering Custom Listeners - Start
        addBeanUpdatedListener(this, BudgetPeriodBean.class);
        addBeanUpdatedListener(this, BudgetDetailBean.class);
        addBeanUpdatedListener(this, BudgetInfoBean.class);
        addBeanAddedListener(this, BudgetDetailBean.class);
        //Registering Custom Listeners - End
        
        ((CustomizeViewForm)customizeViewController.getControlledUI()).btnApply.addActionListener(this);
        ((CustomizeViewForm)customizeViewController.getControlledUI()).btnCancel.addActionListener(this);
        ((CustomizeViewForm)customizeViewController.getControlledUI()).btnOk.addActionListener(this);
        
        budgetPeriodForm.txtCostLimit.setisNegativeAllowed(true);
        budgetPeriodForm.txtCostSharing.setisNegativeAllowed(true);
        budgetPeriodForm.txtDirectCost.setisNegativeAllowed(true);
        budgetPeriodForm.txtIndirectCost.setisNegativeAllowed(true);
        budgetPeriodForm.txtTotalCost.setisNegativeAllowed(true);
        budgetPeriodForm.txtUnderrecovery.setisNegativeAllowed(true);
        
        //Setting up Table
        TableColumn tableColumn = null;
        budgetPeriodForm.tblPeriodLineItem.setModel(budgetPeriodTableModel);
        
        budgetPeriodForm.tblPeriodLineItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        budgetPeriodForm.tblPeriodLineItem.getSelectionModel().addListSelectionListener(budgetListSelectionListener);
        budgetPeriodForm.tblPeriodLineItem.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        //budgetPeriodForm.tblPeriodLineItem.getTableHeader().setReorderingAllowed(false);
        budgetPeriodForm.tblPeriodLineItem.setSelectionBackground(Color.YELLOW);
        budgetPeriodForm.tblPeriodLineItem.setSelectionForeground(Color.BLACK);
        budgetPeriodForm.tblPeriodLineItem.setRowHeight(LINE_ITEM_ROW_HEIGHT);
        
        budgetPeriodForm.tblJustify.setModel(budgetJustifyTableModel);
        budgetPeriodForm.tblJustify.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        budgetPeriodForm.tblJustify.getSelectionModel().addListSelectionListener(budgetListSelectionListener);
        budgetPeriodForm.tblJustify.setRowHeight(budgetPeriodForm.tblPeriodLineItem.getRowHeight());
        tableColumn = budgetPeriodForm.tblJustify.getColumnModel().getColumn(0);
        tableColumn.setCellRenderer(budgetJustifyRenderer);
        tableColumn.setCellEditor(budgetJustifyEditor);
        tableColumn.setHeaderRenderer(new BudgetJustifyRenderer(true));
        
        //setting Table Column
        int colSize[] = {50, 75, 225, 200, 55, 150, 100, 100, 250, 150, 150, 150};
        
        for(int col = 0; col < colSize.length; col++) {
            tableColumn = budgetPeriodForm.tblPeriodLineItem.getColumnModel().getColumn(col);
            //System.out.println(tableColumn.getHeaderValue());
            tableColumn.setPreferredWidth(colSize[col]);
            tableColumn.setCellRenderer(budgetPeriodRenderer);
            tableColumn.setCellEditor(budgetPeriodEditor);
            
            tableColumns[col] = tableColumn;
            
        }
        
        int count = 0;

        for(int index = 0; count < colSize.length; count++) {
            if(colVisible[index] == false) {
                tableColumn = budgetPeriodForm.tblPeriodLineItem.getColumnModel().getColumn(index);
                budgetPeriodForm.tblPeriodLineItem.removeColumn(tableColumn);
            }else {
                index++;
            }
        }
        
        budgetPeriodForm.tblPeriodLineItem.addFocusListener(this);
        //budgetPeriodForm.tblPeriodLineItem.getColumnModel().addColumnModelListener(this);
        budgetPeriodForm.tblPeriodLineItem.getTableHeader().getColumnModel().addColumnModelListener(this);
        
        budgetPeriodForm.txtStartDate.addFocusListener(this);
        budgetPeriodForm.txtEndDate.addFocusListener(this);
        budgetPeriodForm.txtCostLimit.addFocusListener(this);
        budgetPeriodForm.txtDirectCostLimit.addFocusListener(this);
        
        budgetPeriodForm.tblPeriodLineItem.addMouseListener(budgetPeriodEditor);
        //Change Default implementation of table to add Line Item.
      
        
//        budgetPeriodForm.tblPeriodLineItem.addKeyListener(new KeyAdapter() {
//            public void keyPressed(KeyEvent e) {
//                KeyStroke keyStroke = KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers());
//                int keyCode = e.getKeyCode();
//                int modifiers = e.getModifiers();
//                if(keyCode == KeyEvent.VK_A && modifiers == KeyEvent.CTRL_MASK){
//                    addLineItem();
//                }
//            }
//        });
//        
        
        //Adding Adjustment Listeners for Scrollbar
        budgetPeriodForm.scrPnPeriodLineItem.getVerticalScrollBar().addAdjustmentListener(this);
        //Adjustment listener Added
        
        //Bug Fix CTRL+A not working when text field has focus - START
        
        //The Code is commented since it introduced Bug id : 732(see below for bug fix code)
        
        /*ComponentInputMap inputMap = new ComponentInputMap(budgetPeriodEditor.txtCostElement);
        // Add a KeyStroke
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK), "AddLineItem");
        
        budgetPeriodEditor.txtCostElement.setInputMap(JComponent.WHEN_FOCUSED, inputMap);
        // Get an ActionMap from the desired type of component and initialize it
        ActionMap am =  budgetPeriodEditor.txtCostElement.getActionMap();
        am.put("AddLineItem", new AbstractAction("AddLineItem") {
            public void actionPerformed(ActionEvent evt) {
                addLineItem();
            }
        });*/
        
        // commentd code for the bug Fix #1649 and #1650 - start
        //Bug Fix cost element not editable - bug Id : 732 - START
//        budgetPeriodEditor.txtCostElement.addKeyListener(new KeyAdapter() {
//            public void keyPressed(KeyEvent keyEvent) {
//                int keyCode = keyEvent.getKeyCode();
//                int modifiers = keyEvent.getModifiers();
//                if(keyCode == KeyEvent.VK_A && modifiers == KeyEvent.CTRL_MASK){
//                    addLineItem();
//                }
//            }
//        }); - // commentd code for the bug Fix #1649 and #1650 - End
        //Bug Fix cost element not editable - bug Id : 732 - END
        //Bug Fix CTRL+A not working when text field has focus - END
        
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        if(data == null) return ;
        budgetPeriodBean = (BudgetPeriodBean)data;
        
        //if cursor is present in date fields.the format has to be changed.
        //depending on focus. so set the focus to other component.
        budgetPeriodForm.tblPeriodLineItem.requestFocus();
        
        //Setting Budget Details
        budgetPeriodForm.txtStartDate.setText(dateUtils.formatDate(budgetPeriodBean.getStartDate().toString(), DATE_FORMAT));
        budgetPeriodForm.txtEndDate.setText(dateUtils.formatDate(budgetPeriodBean.getEndDate().toString(), DATE_FORMAT));
        budgetPeriodForm.txtCostLimit.setValue(budgetPeriodBean.getTotalCostLimit());
        budgetPeriodForm.txtTotalCost.setValue(budgetPeriodBean.getTotalCost());
        budgetPeriodForm.txtDirectCost.setValue(budgetPeriodBean.getTotalDirectCost());
        budgetPeriodForm.txtIndirectCost.setValue(budgetPeriodBean.getTotalIndirectCost());
        budgetPeriodForm.txtUnderrecovery.setValue(budgetPeriodBean.getUnderRecoveryAmount());
        budgetPeriodForm.txtCostSharing.setValue(budgetPeriodBean.getCostSharingAmount());
        
        // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
        budgetPeriodForm.txtPeriodMonths.setText( budgetPeriodBean.getNoOfPeriodMonths()+"" );
        //3197 - end
        
        //Code added for Case#3472 - Sync to Direct Cost Limit
        //For adding total direct cost limit
        budgetPeriodForm.txtDirectCostLimit.setValue(budgetPeriodBean.getTotalDirectCostLimit());
        //Setting  Budget Line Item Details
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        budgetDetailBean.setProposalNumber(budgetBean.getProposalNumber());
        budgetDetailBean.setVersionNumber(budgetBean.getVersionNumber());
        budgetDetailBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
        if(budgetBean.getUnitNumber()!=null){
            budgetDetailBean.setUnitNumber(budgetBean.getUnitNumber());
        }
        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
        
        try{
            Equals eqNull = new Equals("acType", null);
            NotEquals notEqDel = new NotEquals("acType", TypeConstants.DELETE_RECORD);
            
            Or eqNullOrNotEqDel = new Or(eqNull, notEqDel);
            
            // vecBudgetLineItem = queryEngine.executeQuery(queryKey, BudgetDetailBean.class,eqNullOrNotEqDel);
            vecBudgetLineItem = queryEngine.executeQuery(queryKey, budgetDetailBean);
            vecBudgetLineItem  = vecBudgetLineItem.filter(eqNullOrNotEqDel);
            
            if(groupByMode) {
                vecBudgetLineItem.sort("budgetCategoryCode", false);
                budgetPeriodTableModel.fireTableDataChanged();
                int lastCatCode = -1;
                for(int index = 0; index < vecBudgetLineItem.size(); index++) {
                    budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(index);
                    if(lastCatCode != budgetDetailBean.getBudgetCategoryCode()) {
                        //New Change so increase height.
                        budgetPeriodForm.tblPeriodLineItem.setRowHeight(index, 50);
                        lastCatCode = budgetDetailBean.getBudgetCategoryCode();

                    }
                }
            }else {
                    vecBudgetLineItem.sort("lineItemSequence", true);
            }
            
            
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
            if(vecBudgetLineItem != null && !vecBudgetLineItem.isEmpty()){
                for(Object budgetLineItem : vecBudgetLineItem){
                    BudgetDetailBean lineItemDeatils = (BudgetDetailBean)budgetLineItem;
                    if(lineItemDeatils.getSubAwardNumber() == 0){
                        Operator operator = getPeriodLineItemOperator(lineItemDeatils.getProposalNumber(),
                                lineItemDeatils.getVersionNumber(),lineItemDeatils.getBudgetPeriod(),lineItemDeatils.getLineItemNumber(),true);
                        CoeusVector vecFormualtedCost = queryEngine.executeQuery(queryKey, BudgetFormulatedCostDetailsBean.class, operator);
                        if(vecFormualtedCost != null && !vecFormualtedCost.isEmpty()){
                            lineItemDeatils.setIsFormualtedLineItem(true);
                        }
                    }
                }
            }
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
            budgetPeriodTableModel.setData(vecBudgetLineItem);
            budgetJustifyTableModel.setData(vecBudgetLineItem);
            
            //Bug Fix : Justify button renderer - Start
            if(! groupByMode) {
                budgetPeriodTableModel.fireTableDataChanged();
                budgetJustifyTableModel.fireTableDataChanged();
            }
            //Bug Fix : Justify button renderer - End
            
            budgetInfoBean = (BudgetInfoBean)budgetBean;
            CoeusVector vecBudgetInfo = queryEngine.executeQuery(queryKey, budgetInfoBean);
            budgetInfoBean = (BudgetInfoBean)vecBudgetInfo.get(0);
            
            if(vecBudgetLineItem != null && vecBudgetLineItem.size() > 0) {
                budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(1,1);
                budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(0, 0);
                budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(0);
                budgetPeriodForm.pnlLineItemCalculatedAmounts.setFormData(budgetDetailBean);
            }
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /** validate the form data/Form and returns true if
     * validation is through else returns false.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() {
        try{
            budgetPeriodEditor.stopCellEditing();
            
            if(budgetPeriodForm.txtStartDate.hasFocus() && !validateStartDate()) {
                budgetPeriodForm.txtStartDate.setText(dateUtils.formatDate(budgetPeriodBean.getStartDate().toString(), DATE_FORMAT));
                return false;
            }else if(budgetPeriodForm.txtEndDate.hasFocus() && !validateEndDate()) {
                budgetPeriodForm.txtEndDate.setText(dateUtils.formatDate(budgetPeriodBean.getEndDate().toString(), DATE_FORMAT));
                return false;
            }
            
            Equals eqNull = new Equals("costElement", null);
            Equals eqEmpty = new Equals("costElement", EMPTY_STRING);
            Or nullOrEmpty = new Or(eqNull, eqEmpty);
            queryEngine.removeData(queryKey, BudgetDetailBean.class, nullOrEmpty);
            for(int index = 0; index < vecBudgetLineItem.size(); index++) {
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(index);
                if(budgetDetailBean.getCostElement() == null || budgetDetailBean.getCostElement().equals(EMPTY_STRING)) {
                    vecBudgetLineItem.remove(index);
                    //budgetPeriodTableModel.fireTableRowsDeleted(index, index);
                    //budgetJustifyTableModel.fireTableRowsDeleted(index, index);
                    
                    //Change other beans(which are below this Deleted bean) Line Item Sequence
                    for(int otherIndex = index; otherIndex < vecBudgetLineItem.size(); otherIndex++) {
                        budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(otherIndex);
                        budgetDetailBean.setLineItemSequence(budgetDetailBean.getLineItemSequence() - 1);
                        budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, budgetDetailBean);
                    }
                    //budgetPeriodTableModel.fireTableRowsUpdated(index, vecBudgetLineItem.size());
                    //budgetJustifyTableModel.fireTableRowsUpdated(index, vecBudgetLineItem.size());

                    index--;
                    
                }
            }
            budgetPeriodTableModel.fireTableDataChanged();
            budgetJustifyTableModel.fireTableDataChanged();
        }catch (CoeusException coeusException) {
            //coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
            return false;
        }
        return true;
    }
    
    /** displays the Form which is being controlled.
     */
    public void display() {
        setFocus();
    }
    
    /** saves the Form Data.
     */
    public void saveFormData() {
        if(vecBudgetLineItem != null && vecBudgetLineItem.size() > 0) {
            budgetPeriodEditor.stopCellEditing();
        }

        try{
            //Moved to Validate since every tab click it removes empty rows.
            /*Equals eqNull = new Equals("costElement", null);
            Equals eqEmpty = new Equals("costElement", EMPTY_STRING);
            Or nullOrEmpty = new Or(eqNull, eqEmpty);
            queryEngine.removeData(queryKey, BudgetDetailBean.class, nullOrEmpty);
            for(int index = 0; index < vecBudgetLineItem.size(); index++) {
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(index);
                if(budgetDetailBean.getCostElement() == null || budgetDetailBean.getCostElement().equals(EMPTY_STRING)) {
                    vecBudgetLineItem.remove(index);
                    budgetPeriodTableModel.fireTableRowsDeleted(index, index);
                    budgetJustifyTableModel.fireTableRowsDeleted(index, index);
             
                    //Change other beans(which are below this Deleted bean) Line Item Sequence
                    for(int otherIndex = index; otherIndex < vecBudgetLineItem.size(); otherIndex++) {
                        budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(otherIndex);
                        budgetDetailBean.setLineItemSequence(budgetDetailBean.getLineItemSequence() - 1);
                        budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, budgetDetailBean);
                    }
                    budgetPeriodTableModel.fireTableRowsUpdated(index, vecBudgetLineItem.size());
                    budgetJustifyTableModel.fireTableRowsUpdated(index, vecBudgetLineItem.size());
             
                }
            }*/
            
            //Calculate Budget Period Costs
            //calculate would be called on cell edit. so no need to call again
            /*
            if(vecBudgetLineItem != null && vecBudgetLineItem.size() > 0) {
                calculate();
            }*/
            
            //bug fix : error displayed after frame closed - START
            if(budgetPeriodForm.txtStartDate.hasFocus() || budgetPeriodForm.txtEndDate.hasFocus()) {
                budgetPeriodBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            //bug fix : error displayed after frame closed - END
            
            budgetPeriodBean.setTotalCostLimit(Double.parseDouble(budgetPeriodForm.txtCostLimit.getValue()));
            budgetPeriodBean.setTotalCost(Double.parseDouble(budgetPeriodForm.txtTotalCost.getValue()));
            budgetPeriodBean.setTotalDirectCost(Double.parseDouble(budgetPeriodForm.txtDirectCost.getValue()));
            budgetPeriodBean.setTotalIndirectCost(Double.parseDouble(budgetPeriodForm.txtIndirectCost.getValue()));
            budgetPeriodBean.setUnderRecoveryAmount(Double.parseDouble(budgetPeriodForm.txtUnderrecovery.getValue()));
            budgetPeriodBean.setCostSharingAmount(Double.parseDouble(budgetPeriodForm.txtCostSharing.getValue()));
            //Code added for Case#3472 - Sync to Direct Cost Limit
            //For adding total direct cost limit
            budgetPeriodBean.setTotalDirectCostLimit(Double.parseDouble(budgetPeriodForm.txtDirectCostLimit.getValue()));
            
            //Check if values are changed if changed then update to query engine.
            Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
            CoeusVector vecbudgetPeriod = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, eqBudgetPeriod);
            BudgetPeriodBean queryBudgetPeriodBean;
            if(vecbudgetPeriod!= null && vecbudgetPeriod.size() > 0){
                queryBudgetPeriodBean = (BudgetPeriodBean)vecbudgetPeriod.get(0);
                StrictEquals strictEquals = new StrictEquals();
                if(! strictEquals.compare(queryBudgetPeriodBean, budgetPeriodBean)) {
                    budgetPeriodBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, budgetPeriodBean);
                }
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /** listens for focus gained events.
     * @param focusEvent focus Event.
     */
    public void focusGained(FocusEvent focusEvent) {
        if(focusEvent.isTemporary()) return ;
        
        Object source = focusEvent.getSource();
        String strDate;
        
        if(source.equals(budgetPeriodForm.txtStartDate)) {
            strDate = dateUtils.restoreDate(budgetPeriodForm.txtStartDate.getText(), DATE_SEPARATERS);
            budgetPeriodForm.txtStartDate.setText(strDate);
        }else if(source.equals(budgetPeriodForm.txtEndDate)) {
            strDate = dateUtils.restoreDate(budgetPeriodForm.txtEndDate.getText(), DATE_SEPARATERS);
            budgetPeriodForm.txtEndDate.setText(strDate);
        }
    }
    
    /** listens for focus lost events.
     * @param focusEvent focus event.
     */
    public void focusLost(FocusEvent focusEvent) {
        if(focusEvent.isTemporary()) return ;
        
        Object source = focusEvent.getSource();
        String strDate;
        Date dt;
        try{
            if(source.equals(budgetPeriodForm.txtStartDate)) {
                // 2961: Invalid start date error message in Budget - Start
                // Compare the value in  the Start Date Text box r and the
                // value in the budgetPeriodBean.
                // If both value are equal, do not call validateStartDate()
                SimpleDateFormat dtFormat = new SimpleDateFormat(DATE_FORMAT);      
                // Convert the startdate of the budgetPeriodBean ((which is in 
                // java.sql.Date format) to java.util.Date.
                java.util.Date utilDate = new java.util.Date(budgetPeriodBean.getStartDate().getTime());
                strDate = dtFormat.format(utilDate);
                boolean validDate = false;
                if (strDate.equals(budgetPeriodForm.txtStartDate.getText())){
                    validDate = true;
                }
                
                if( validDate || validateStartDate()) {
//                if(validateStartDate()) {
                    // 2961: Invalid start date error message in Budget - End
                    //Moved inside validation to fix null pointer exception if date text is empty - Start
                    
                    //Check if changed date is same as earlier date
                    // 2961: Invalid start date error message in Budget - Start
                    if(!validDate){
                        // If the value in the StartDate text box and start date of 
                        // budgetPeriodBean are not Equal
                        strDate = dateUtils.formatDate(budgetPeriodForm.txtStartDate.getText(), DATE_SEPARATERS, DATE_FORMAT);
                    }
                    // 2961: Invalid start date error message in Budget - End
                    dt = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                    
                    if(dt.equals(budgetPeriodBean.getStartDate())) {
                        budgetPeriodForm.txtStartDate.setText(dateUtils.formatDate(budgetPeriodBean.getStartDate().toString(), DATE_FORMAT));
                        return ;
                    }
                    //Moved inside validation to fix null pointer exception if date text is empty - End
                    
                    budgetPeriodBean.setStartDate(new java.sql.Date(dt.getTime()));
                    budgetPeriodForm.txtStartDate.setText(strDate);
                    
                    queryEngine.update(queryKey, budgetPeriodBean);
                    
                    BeanEvent beanEvent = new BeanEvent();
                    beanEvent.setBean(budgetPeriodBean);
                    beanEvent.setSource(this);
                    fireBeanUpdated(beanEvent);
                    
                    calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
                    setRefreshRequired(true);
                    refresh();
                    
                }else { //Set Original Date
                    budgetPeriodForm.txtStartDate.setText(dateUtils.formatDate(budgetPeriodBean.getStartDate().toString(), DATE_FORMAT));
                    //budgetPeriodForm.txtStartDate.requestFocus();
                }
                
            }else if(source.equals(budgetPeriodForm.txtEndDate)) {
                
                
                if(validateEndDate()) {
                    
                    //Moved inside validation to fix null pointer exception if date text is empty - Start
                    
                    //Check if changed date is same as earlier date
                    strDate = dateUtils.formatDate(budgetPeriodForm.txtEndDate.getText(), DATE_SEPARATERS, DATE_FORMAT);
                    //dt = simpleDateFormat.parse(budgetPeriodForm.txtEndDate.getText());
                    dt = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                    if(dt.equals(budgetPeriodBean.getEndDate())) {
                        budgetPeriodForm.txtEndDate.setText(dateUtils.formatDate(budgetPeriodBean.getEndDate().toString(), DATE_FORMAT));
                        return ;
                    }
                    //Moved inside validation to fix null pointer exception if date text is empty - End
                    
                    budgetPeriodBean.setEndDate(new java.sql.Date(dt.getTime()));
                    budgetPeriodForm.txtEndDate.setText(strDate);
   
                    queryEngine.update(queryKey, budgetPeriodBean);
                    
                    BeanEvent beanEvent = new BeanEvent();
                    beanEvent.setBean(budgetPeriodBean);
                    beanEvent.setSource(this);
                    fireBeanUpdated(beanEvent);
                    
                    calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
                    setRefreshRequired(true);
                    refresh();
                    
                }else { //Set Original Date
                    budgetPeriodForm.txtEndDate.setText(dateUtils.formatDate(budgetPeriodBean.getEndDate().toString(), DATE_FORMAT));
                    //budgetPeriodForm.txtEndDate.requestFocus();
                }
            }//End focus Lost - End Date
            /**is is code is added to edit the table cell when the focus is lost from the
             *cost limit text box.
             *Bug Fix #1649 - start
             */
            else if(source.equals(budgetPeriodForm.txtCostLimit)){
                if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
                    /**Check if the addLineItem Action is performed. If yes get the
                     *row from vector and set the focus
                     */
                    if(addActionisPerformed){
                            int row = vecBudgetLineItem.size()-1;
                        budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(row,row);
                        budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_ELEMENT_COLUMN,COST_ELEMENT_COLUMN);
                        budgetPeriodForm.tblPeriodLineItem.editCellAt(row,COST_ELEMENT_COLUMN);
                        if(budgetPeriodForm.tblPeriodLineItem.getEditorComponent()!= null){
                            budgetPeriodForm.tblPeriodLineItem.getEditorComponent().requestFocusInWindow();
                        }
                        addActionisPerformed = false;
                    }else{
                        budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(0,0);
                        budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_ELEMENT_COLUMN,COST_ELEMENT_COLUMN);
                        boolean isEditable = budgetPeriodTableModel.isCellEditable(0, COST_ELEMENT_COLUMN);
                        if(!isEditable){
                            budgetPeriodForm.tblPeriodLineItem.editCellAt(0,COST_ELEMENT_COLUMN);
                            if(budgetPeriodForm.tblPeriodLineItem.getEditorComponent()!= null){
                                budgetPeriodForm.tblPeriodLineItem.getEditorComponent().requestFocusInWindow();
                            }
                        }
                    }
                }
//                else{
//                    budgetPeriodForm.txtStartDate.requestFocusInWindow();
//                }
            }// Bug Fix #1649 - End
            else if(source.equals(budgetPeriodForm.txtDirectCostLimit)){
                if(budgetPeriodForm.tblPeriodLineItem.getRowCount() == 0){
                    budgetPeriodForm.txtStartDate.requestFocusInWindow();
                }
            }
        }catch (ParseException parseException) {
            parseException.printStackTrace();
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /** validates start date.
     * @return true if validation succeeds else returns false.
     */
    private boolean validateStartDate() {
        Date dt;
        BudgetPeriodBean tempBudgetPeriodBean, prevBudgetPeriodBean;
        CoeusVector vecTempBudgetPeriodBean;
        String strDate;
        try{
            strDate = dateUtils.formatDate(budgetPeriodForm.txtStartDate.getText(), DATE_SEPARATERS, DATE_FORMAT);
            if(strDate == null) throw new CoeusException();
            
            //dt = simpleDateFormat.parse(budgetPeriodForm.txtStartDate.getText());
            dt = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
            
            //If entered Date and budget period date are same. Then no need to
            //Compare nor validate.
            if(budgetPeriodBean.getStartDate().equals(new java.sql.Date(dt.getTime()))) {
                return true;
            }
            
            if(budgetPeriodBean.getBudgetPeriod() == 1) {
                if(dt.compareTo(budgetInfoBean.getStartDate()) < 0 ) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PERIOD_START_DATE_BEFORE_BUDGET_START_DATE));
                    return false;
                }
            }else{
                tempBudgetPeriodBean = new BudgetPeriodBean();
                tempBudgetPeriodBean.setProposalNumber(budgetBean.getProposalNumber());
                tempBudgetPeriodBean.setVersionNumber(budgetBean.getVersionNumber());
                tempBudgetPeriodBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod() - 1);
                vecTempBudgetPeriodBean = queryEngine.executeQuery(queryKey, tempBudgetPeriodBean);
                
                prevBudgetPeriodBean = (BudgetPeriodBean)vecTempBudgetPeriodBean.get(0);
                
                if(dt.compareTo(prevBudgetPeriodBean.getEndDate()) < 0) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PERIOD_START_DATE_BEFORE_PREV_PERIOD_END_DATE));
                    return false;
                }
                
            }
            
            //Common Validations(i.e. does not depend upon Budget Period)
            if(dt.compareTo(budgetPeriodBean.getEndDate()) > 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(START_DATE_LATER_THAN_END_DATE));
                return false;
            }
            
            if(getFunctionType() == TypeConstants.ADD_MODE || getFunctionType() == TypeConstants.MODIFY_MODE) {
                int selection = CoeusOptionPane.showQuestionDialog(START_DATE_CHANGE + dateUtils.formatDate(budgetPeriodForm.txtStartDate.getText(), DATE_SEPARATERS, DATE_FORMAT), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_YES) {
                    //Change all Line Item Start Date here

                    //Update Query Engine with new dates.
                    Equals eqPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                    
                    Equals eqOldStDate = new Equals("lineItemStartDate", budgetPeriodBean.getStartDate());
                    LesserThan ltNewStDate = new LesserThan("lineItemStartDate", dt);
                    
                    And eqPeriodActiveLI = new And(eqPeriod, CoeusVector.FILTER_ACTIVE_BEANS);
                    Or eqOldStDateOrLtNewStDate = new Or(eqOldStDate, ltNewStDate);
                    
                    And eqPeriodActiveLIAndEqOldStDateOrLtNewStDate = 
                    new And(eqPeriodActiveLI, eqOldStDateOrLtNewStDate);
                    
                    //Update Budget Details Beans
                    queryEngine.setUpdate(queryKey, BudgetDetailBean.class, 
                        "lineItemStartDate", java.sql.Date.class,  new java.sql.Date(dt.getTime()), 
                        eqPeriodActiveLIAndEqOldStDateOrLtNewStDate);
                    
                    //Update Table Data
                    BudgetDetailBean budgetDetailBean;
                    for(int index = 0; index < vecBudgetLineItem.size(); index++) {
                        budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(index);
                        if(eqOldStDateOrLtNewStDate.getResult(budgetDetailBean)) {
                            budgetDetailBean.setLineItemStartDate(new java.sql.Date(dt.getTime()));
                            budgetPeriodTableModel.fireTableCellUpdated(index, START_DATE_COLUMN);
                        }
                    }
                    
                    //Genarate Condition for Budget Personnel Details
                    eqOldStDate = new Equals("startDate", budgetPeriodBean.getStartDate());
                    ltNewStDate = new LesserThan("startDate", dt);
                    
                    eqPeriodActiveLI = new And(eqPeriod, CoeusVector.FILTER_ACTIVE_BEANS);
                    eqOldStDateOrLtNewStDate = new Or(eqOldStDate, ltNewStDate);
                    
                    eqPeriodActiveLIAndEqOldStDateOrLtNewStDate = 
                    new And(eqPeriodActiveLI, eqOldStDateOrLtNewStDate);
                    
                    //Update Budget Personnel Details Beans
                    queryEngine.setUpdate(queryKey, BudgetPersonnelDetailsBean.class, 
                        "startDate", java.sql.Date.class,  new java.sql.Date(dt.getTime()), 
                        eqPeriodActiveLIAndEqOldStDateOrLtNewStDate);

                    return true;
                }else {
                    return false;
                }
            }
            
            //Went thru All Validations - Good Date
            return true;
            
        }catch (ParseException parseException) {
            parseException.printStackTrace();
        }catch (CoeusException coeusException) {
            
        }
        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_START_DATE));
        return false;
    }
    
    /** validates end date.
     * @return true if validation succeeds else returns false.
     */
    private boolean validateEndDate() {
        Date dt;
        BudgetPeriodBean tempBudgetPeriodBean, lastBudgetPeriodBean, nextBudgetPeriodBean;
        CoeusVector vecTempBudgetDetailBean;
        String strDate;
        
        try{
        
            strDate = dateUtils.formatDate(budgetPeriodForm.txtEndDate.getText(), DATE_SEPARATERS, DATE_FORMAT);
            if(strDate == null) throw new CoeusException();
            
            //dt = simpleDateFormat.parse(budgetPeriodForm.txtEndDate.getText());
            dt = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
            
            //If entered Date and budget period date are same. Then no need to
            //Compare nor validate.
            if(budgetPeriodBean.getEndDate().equals(new java.sql.Date(dt.getTime()))) {
                return true;
            }
            
            tempBudgetPeriodBean = new BudgetPeriodBean();
            tempBudgetPeriodBean.setProposalNumber(budgetBean.getProposalNumber());
            tempBudgetPeriodBean.setVersionNumber(budgetBean.getVersionNumber());
            
            vecTempBudgetDetailBean = queryEngine.executeQuery(queryKey, tempBudgetPeriodBean);
            lastBudgetPeriodBean = (BudgetPeriodBean)vecTempBudgetDetailBean.get(vecTempBudgetDetailBean.size() - 1);
            if(budgetPeriodBean.getBudgetPeriod() == lastBudgetPeriodBean.getBudgetPeriod()) {
                //This is the Last Budget Period
                if(dt.compareTo(budgetInfoBean.getEndDate()) > 0) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PERIOD_END_DATE_LATER_THAN_BUDGET_END_DATE));
                    return false;
                }
            }else {
                tempBudgetPeriodBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod() + 1);
                vecTempBudgetDetailBean = queryEngine.executeQuery(queryKey, tempBudgetPeriodBean);
                nextBudgetPeriodBean = (BudgetPeriodBean)vecTempBudgetDetailBean.get(0);
                if(dt.compareTo(nextBudgetPeriodBean.getStartDate()) > 0) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PERIOD_END_DATE_LATER_THAN_NEXT_PERIOD_START_DATE));
                    return false;
                }
            }
            
            //Common Validations(i.e. Doen't depend on Budget Period)
            if(dt.compareTo(budgetPeriodBean.getStartDate()) < 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(END_DATE_EARLIER_THAN_STRAT_DATE));
                return false;
            }
            
            if((getFunctionType() == TypeConstants.ADD_MODE || getFunctionType() == TypeConstants.MODIFY_MODE) &&
            (vecBudgetLineItem!=null && vecBudgetLineItem.size() > 0)){
                int selection = CoeusOptionPane.showQuestionDialog(END_DATE_CHANGE + dateUtils.formatDate(budgetPeriodForm.txtEndDate.getText(), DATE_SEPARATERS, DATE_FORMAT), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_YES) {
                    //Change all Line Item End Date here
                    /*for(int index = 0; index < vecBudgetLineItem.size(); index++) {
                        ((BudgetDetailBean)vecBudgetLineItem.get(index)).setLineItemEndDate(new java.sql.Date(dt.getTime()));
                        budgetPeriodTableModel.fireTableCellUpdated(index, END_DATE_COLUMN);
                    }*/
                    
                    //Update Query Engine with new dates.
                    Equals eqPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
                    
                    Equals eqOldStDate = new Equals("lineItemEndDate", budgetPeriodBean.getEndDate());
                    GreaterThan gtNewStDate = new GreaterThan("lineItemEndDate", dt);
                    
                    And eqPeriodActiveLI = new And(eqPeriod, CoeusVector.FILTER_ACTIVE_BEANS);
                    Or eqOldStDateOrGtNewStDate = new Or(eqOldStDate, gtNewStDate);
                    
                    And eqPeriodActiveLIAndEqOldStDateOrGtNewStDate = 
                    new And(eqPeriodActiveLI, eqOldStDateOrGtNewStDate);
                    
                    //Update Budget Details Beans
                    queryEngine.setUpdate(queryKey, BudgetDetailBean.class, 
                        "lineItemEndDate", java.sql.Date.class,  new java.sql.Date(dt.getTime()), 
                        eqPeriodActiveLIAndEqOldStDateOrGtNewStDate);
                    
                    //Update Table Data
                    BudgetDetailBean budgetDetailBean;
                    for(int index = 0; index < vecBudgetLineItem.size(); index++) {
                        budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(index);
                        if(eqOldStDateOrGtNewStDate.getResult(budgetDetailBean)) {
                            budgetDetailBean.setLineItemEndDate(new java.sql.Date(dt.getTime()));
                            budgetPeriodTableModel.fireTableCellUpdated(index, END_DATE_COLUMN);
                        }
                    }
                    
                    //Genarate Condition for Budget Personnel Details
                    eqOldStDate = new Equals("endDate", budgetPeriodBean.getEndDate());
                    gtNewStDate = new GreaterThan("endDate", dt);
                    
                    eqPeriodActiveLI = new And(eqPeriod, CoeusVector.FILTER_ACTIVE_BEANS);
                    eqOldStDateOrGtNewStDate = new Or(eqOldStDate, gtNewStDate);
                    
                    eqPeriodActiveLIAndEqOldStDateOrGtNewStDate = 
                    new And(eqPeriodActiveLI, eqOldStDateOrGtNewStDate);
                    
                    //Update Budget Personnel Details Beans
                    queryEngine.setUpdate(queryKey, BudgetPersonnelDetailsBean.class, 
                        "endDate", java.sql.Date.class,  new java.sql.Date(dt.getTime()), 
                        eqPeriodActiveLIAndEqOldStDateOrGtNewStDate);
                    
                    return true;
                }else {
                    return false;
                }
            }
            
            //Went thru all Validations. Good Date
            return true;
            
        }catch (ParseException parseException) {
            parseException.printStackTrace();
        }catch (CoeusException coeusException) {
            
        }
        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_END_DATE));
        return false;
    }
    
    public void setFocus(){
        if(getFunctionType()!= TypeConstants.DISPLAY_MODE){
            budgetPeriodForm.txtStartDate.requestFocusInWindow();
            setSaveRequired(false);
        }
    }
    /** adds a line item to the end of the period line item table. */
    public void addLineItem() {
        //System.out.println("Adding Line Item for Budget Period "+budgetPeriodBean.getBudgetPeriod());
        //budgetPeriodEditor.stopCellEditing();
        
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) return ;
        
        //Check if the display is in Groupby Mode
        if(groupByMode) return ;
        
        int row = vecBudgetLineItem.size() + 1;
        
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        budgetDetailBean.setProposalNumber(budgetBean.getProposalNumber());
        budgetDetailBean.setVersionNumber(budgetBean.getVersionNumber());
        budgetDetailBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
        budgetDetailBean.setLineItemNumber(getMaxLineItemNumber() + 1);
        budgetDetailBean.setBasedOnLineItem(0);
        budgetDetailBean.setLineItemSequence(row);
        budgetDetailBean.setLineItemStartDate(budgetPeriodBean.getStartDate());
        budgetDetailBean.setLineItemEndDate(budgetPeriodBean.getEndDate());
        budgetDetailBean.setQuantity(0);
        budgetDetailBean.setCostElement(EMPTY_STRING);
        budgetDetailBean.setCostElementDescription(EMPTY_STRING);
        budgetDetailBean.setApplyInRateFlag(true);
        budgetDetailBean.setLineItemCost(0);
        budgetDetailBean.setCostSharingAmount(0);
        budgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
        //COEUSQA-1693 - Cost Sharing Submission - start
        budgetDetailBean.setSubmitCostSharingFlag(true);
        //COEUSQA-1693 - Cost Sharing Submission - end
        

        
        vecBudgetLineItem.add(budgetDetailBean);
        
        
        
        
        budgetPeriodTableModel.fireTableRowsInserted(row - 1, row - 1);
        budgetJustifyTableModel.fireTableRowsInserted(row - 1, row - 1);
        
        queryEngine.insert(queryKey, budgetDetailBean);
        
        /*try{
            budgetDetailBean = (BudgetDetailBean)queryEngine.executeQuery(queryKey, budgetDetailBean).get(0);
            vecBudgetLineItem.set(row, budgetDetailBean);
        }catch (CoeusException coeusException) {
         
        }*/
        
       budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(row - 1 , row - 1);
       budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_ELEMENT_COLUMN,COST_ELEMENT_COLUMN);
       // Added by chandra to Fix #1650 - start
       budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                budgetPeriodForm.tblPeriodLineItem.getCellRect(
                row-1 ,0, true));
       // Added by chandra to Fix #1650 - End
       budgetPeriodForm.tblPeriodLineItem.editCellAt(row - 1, COST_ELEMENT_COLUMN);
       if(budgetPeriodForm.tblPeriodLineItem.getEditorComponent()!= null){
        budgetPeriodForm.tblPeriodLineItem.getEditorComponent().requestFocusInWindow();
       }
    }
    
    /** inserts a line item above the selected Row. */
    public void insertLineItem() {
        //System.out.println("Inserting Line Item for Budget Period "+budgetPeriodBean.getBudgetPeriod());
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) return ;
        budgetPeriodEditor.stopCellEditing();
        
        //Check if it is in GroupBy Mode
        if(groupByMode) return ;
        
        int row = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
        
        if(row != -1 ){
            
            budgetPeriodEditor.stopCellEditing();
            
            BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
            budgetDetailBean.setProposalNumber(budgetBean.getProposalNumber());
            budgetDetailBean.setVersionNumber(budgetBean.getVersionNumber());
            budgetDetailBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
            budgetDetailBean.setLineItemNumber(getMaxLineItemNumber() + 1);
            budgetDetailBean.setBasedOnLineItem(0);
            budgetDetailBean.setLineItemSequence(row + 1);
            budgetDetailBean.setLineItemStartDate(budgetPeriodBean.getStartDate());
            budgetDetailBean.setLineItemEndDate(budgetPeriodBean.getEndDate());
            budgetDetailBean.setQuantity(0);
            budgetDetailBean.setCostElement(EMPTY_STRING);
            budgetDetailBean.setCostElementDescription(EMPTY_STRING);
            budgetDetailBean.setApplyInRateFlag(true);
            budgetDetailBean.setLineItemCost(0);
            budgetDetailBean.setCostSharingAmount(0);
            //COEUSQA-1693 - Cost Sharing Submission - start
            budgetDetailBean.setSubmitCostSharingFlag(true);
            //COEUSQA-1693 - Cost Sharing Submission - end
            budgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
            
            vecBudgetLineItem.add(row, budgetDetailBean);
            
            budgetPeriodTableModel.fireTableRowsInserted(row, row);
            budgetJustifyTableModel.fireTableRowsInserted(row, row);
            
            budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(row, row);
            budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(1,1);
            
            //Set to Query Engine
            queryEngine.insert(queryKey, budgetDetailBean);
            
            //Change other beans(which are below this newly inserted bean) Line Item Sequence
            try{
                for(int index = row + 1; index < vecBudgetLineItem.size(); index++) {
                    budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(index);
                    budgetDetailBean.setLineItemSequence(budgetDetailBean.getLineItemSequence() + 1);
                    budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, budgetDetailBean);
                }
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
            }
            budgetPeriodTableModel.fireTableRowsUpdated(row + 1, vecBudgetLineItem.size());
            budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
            budgetPeriodForm.tblPeriodLineItem.getCellRect(
            row-1 ,0, true));
            budgetPeriodForm.tblPeriodLineItem.editCellAt(row, COST_ELEMENT_COLUMN);
            budgetPeriodEditor.txtCostElement.requestFocus();
        }else{
            addLineItem();// If there are no row then line item was not adding. This is the fix
        }
    }
    
    /** deletes the selected line item. */
    public void deleteLineItem() {
        if(groupByMode) return ;
        
        //System.out.println("Deleting Line Item for Budget Period "+budgetPeriodBean.getBudgetPeriod());
        int selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
        if(selectedRow < 0) return ;
        
        //Check if this Line Item has Personnel Details.If it has then Can't Delete
        BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
        
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = new BudgetPersonnelDetailsBean();
        budgetPersonnelDetailsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
        budgetPersonnelDetailsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
        budgetPersonnelDetailsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
        budgetPersonnelDetailsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
        try{
            CoeusVector vecBudgetPersonnelDetailsBean = queryEngine.executeQuery(queryKey, budgetPersonnelDetailsBean);
            vecBudgetPersonnelDetailsBean = vecBudgetPersonnelDetailsBean.filter(CoeusVector.FILTER_ACTIVE_BEANS);
            if(vecBudgetPersonnelDetailsBean != null && vecBudgetPersonnelDetailsBean.size() > 0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(LINE_ITEM_CONTAINS_PERSONNEL_DETAILS));
                return ;
            }
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
            if(budgetDetailBean.getSubAwardNumber() > 0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(LINE_ITEM_FROM_SUBAWARD_CANT_DELETE));
                return ;
            }
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
            Operator operator = getPeriodLineItemOperator(budgetDetailBean.getProposalNumber(),
                    budgetDetailBean.getVersionNumber(),budgetDetailBean.getBudgetPeriod(),budgetDetailBean.getLineItemNumber(),true);
            And andFormualtedActiveBeans = new And(operator,CoeusVector.FILTER_ACTIVE_BEANS);
            CoeusVector cvFormualtedCostDetails = queryEngine.executeQuery(queryKey, BudgetFormulatedCostDetailsBean.class,andFormualtedActiveBeans);
            if(cvFormualtedCostDetails != null && !cvFormualtedCostDetails.isEmpty()){
                // Modified for COEUSQA-3626 : Formulated Cost line items cannot be deleted from budget period if expense exists - Start
//                 CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(FORMULATED_COST_LINE_ITEM_CANT_DELETE));
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(FORMULATED_COST_LINE_ITEM_CANT_DELETE),
                        CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                if(selectedOption == CoeusOptionPane.SELECTION_NO){
                    return ;
                }
                deleteLineItemDetails(budgetDetailBean,operator);
                 return ;
                // Modified for COEUSQA-3626 : Formulated Cost line items cannot be deleted from budget period if expense exists - End

            }
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
            //Start Delete Line Item
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(DELETE_LINE_ITEM), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
            if(selection == CoeusOptionPane.SELECTION_NO) return ;
         
            // Modified for COEUSQA-3626 : Formulated Cost line items cannot be deleted from budget period if expense exists - Start
            // Logic is moved to deleteLineItemDetails method
            deleteLineItemDetails(budgetDetailBean,operator);
//            budgetPeriodEditor.stopCellEditing();
//            budgetPeriodEditor.cancelCellEditing();
//            
//            //Selected Yes So Delete Selected Line Item
//            budgetDetailBean.setAcType(TypeConstants.DELETE_RECORD);
//            queryEngine.delete(queryKey, budgetDetailBean);
//            
//            //Set Ac TYpe  = D for Old Budget Detail Cal Amts
//            BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
//            budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
//            budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
//            budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
//            budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
//            CoeusVector vecOldDetails = queryEngine.executeQuery(queryKey, budgetDetailCalAmountsBean);
//            if(vecOldDetails !=null && vecOldDetails.size() > 0) {
//                for(int index = 0; index < vecOldDetails.size(); index++) {
//                    ((CoeusBean)vecOldDetails.get(index)).setAcType(TypeConstants.DELETE_RECORD);
//                    queryEngine.delete(queryKey, (CoeusBean)vecOldDetails.get(index));
//                }
//            }
//            // formualted cost
//            CoeusVector cvFormulatedCost = QueryEngine.getInstance().executeQuery(queryKey, BudgetFormulatedCostDetailsBean.class, operator);            
//            if(cvFormulatedCost !=null && cvFormulatedCost.size() > 0) {
//                for(int index = 0; index < cvFormulatedCost.size(); index++) {
//                    ((BudgetFormulatedCostDetailsBean)cvFormulatedCost.get(index)).setAcType(TypeConstants.DELETE_RECORD);
//                    queryEngine.delete(queryKey, (BudgetFormulatedCostDetailsBean)cvFormulatedCost.get(index));
//                }
//            }
//            
//            vecBudgetLineItem.remove(selectedRow);
//            budgetPeriodTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
//            budgetJustifyTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
//            
//            //Change other beans(which are below this Deleted bean) Line Item Sequence
//            for(int index = selectedRow; index < vecBudgetLineItem.size(); index++) {
//                budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(index);
//                budgetDetailBean.setLineItemSequence(budgetDetailBean.getLineItemSequence() - 1);
//                budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
//                queryEngine.update(queryKey, budgetDetailBean);
//            }
//            budgetPeriodTableModel.fireTableRowsUpdated(selectedRow, vecBudgetLineItem.size());
//            budgetJustifyTableModel.fireTableRowsUpdated(selectedRow, vecBudgetLineItem.size());
//            
//            //Calculate Budget Period Costs
//            calculate();
//            if(vecBudgetLineItem.size() == 0) {
//                budgetPeriodForm.pnlLineItemCalculatedAmounts.setFormData(budgetDetailBean);
//            }else {
//                budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(1,1);
//                budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(0, 0);
//                budgetPeriodForm.tblPeriodLineItem.editCellAt(0,COST_ELEMENT_COLUMN);
//                if(budgetPeriodForm.tblPeriodLineItem.getEditorComponent() != null){
//                    budgetPeriodForm.tblPeriodLineItem.getEditorComponent().requestFocusInWindow();
//                }
//                
//                
//            }
//            
//            if(budgetPeriodForm.tblPeriodLineItem.getRowCount() <1){
//                budgetPeriodForm.txtStartDate.requestFocusInWindow();
//            }
            // Modified for COEUSQA-3626 : Formulated Cost line items cannot be deleted from budget period if expense exists - End
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    // Added for COEUSQA-3626 : Formulated Cost line items cannot be deleted from budget period if expense exists - Start
    /**
     * Method will delete all the details related to line item and then delete the line item and calculte the entire budget
     * @param budgetDetailBean 
     * @param operator 
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    private void deleteLineItemDetails(BudgetDetailBean budgetDetailBean, Operator operator) throws CoeusException{
        int selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
        budgetPeriodEditor.cancelCellEditing();
        
        //Selected Yes So Delete Selected Line Item
        budgetDetailBean.setAcType(TypeConstants.DELETE_RECORD);
        queryEngine.delete(queryKey, budgetDetailBean);
        
        //Set Ac TYpe  = D for Old Budget Detail Cal Amts
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
        budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
        budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
        budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
        budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
        CoeusVector vecOldDetails = queryEngine.executeQuery(queryKey, budgetDetailCalAmountsBean);
        if(vecOldDetails !=null && vecOldDetails.size() > 0) {
            for(int index = 0; index < vecOldDetails.size(); index++) {
                ((CoeusBean)vecOldDetails.get(index)).setAcType(TypeConstants.DELETE_RECORD);
                queryEngine.delete(queryKey, (CoeusBean)vecOldDetails.get(index));
            }
        }
        // formualted cost
        CoeusVector cvFormulatedCost = QueryEngine.getInstance().executeQuery(queryKey, BudgetFormulatedCostDetailsBean.class, operator);
        if(cvFormulatedCost !=null && cvFormulatedCost.size() > 0) {
            for(int index = 0; index < cvFormulatedCost.size(); index++) {
                ((BudgetFormulatedCostDetailsBean)cvFormulatedCost.get(index)).setAcType(TypeConstants.DELETE_RECORD);
                queryEngine.delete(queryKey, (BudgetFormulatedCostDetailsBean)cvFormulatedCost.get(index));
            }
        }
        
        vecBudgetLineItem.remove(selectedRow);
        budgetPeriodTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
        budgetJustifyTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
        
        //Change other beans(which are below this Deleted bean) Line Item Sequence
        for(int index = selectedRow; index < vecBudgetLineItem.size(); index++) {
            budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(index);
            budgetDetailBean.setLineItemSequence(budgetDetailBean.getLineItemSequence() - 1);
            budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
            queryEngine.update(queryKey, budgetDetailBean);
        }
        budgetPeriodTableModel.fireTableRowsUpdated(selectedRow, vecBudgetLineItem.size());
        budgetJustifyTableModel.fireTableRowsUpdated(selectedRow, vecBudgetLineItem.size());
        
        //Calculate Budget Period Costs
        calculate();
        if(vecBudgetLineItem.size() == 0) {
            budgetPeriodForm.pnlLineItemCalculatedAmounts.setFormData(budgetDetailBean);
        }else {
            budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(1,1);
            budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(0, 0);
            budgetPeriodForm.tblPeriodLineItem.editCellAt(0,COST_ELEMENT_COLUMN);
            if(budgetPeriodForm.tblPeriodLineItem.getEditorComponent() != null){
                budgetPeriodForm.tblPeriodLineItem.getEditorComponent().requestFocusInWindow();
            }
            
            
        }
        
        if(budgetPeriodForm.tblPeriodLineItem.getRowCount() <1){
            budgetPeriodForm.txtStartDate.requestFocusInWindow();
        }
    }
    // Added for COEUSQA-3626 : Formulated Cost line items cannot be deleted from budget period if expense exists - End
    
    /** Displays edit line item dialog to edit the selected Line Item. */
    public void editLineItemDetails() throws CoeusException {
        //System.out.println("Editing Line Item for Budget Period "+budgetPeriodBean.getBudgetPeriod());
        int selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
        if(selectedRow < 0) return ;
        
        budgetPeriodEditor.stopCellEditing();
        
        BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
        try{
            budgetDetailBean  = (BudgetDetailBean)ObjectCloner.deepCopy(budgetDetailBean);
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        budgetDetailBean.setIsFormualtedLineItem(canLineItemFormulated(budgetDetailBean));
        budgetLineItemController.setFormData(budgetDetailBean);
        
        if(groupByMode) {
            budgetLineItemController.setFunctionType(TypeConstants.DISPLAY_MODE);
            formulatedCostBudgetLineItemController.setFunctionType(TypeConstants.DISPLAY_MODE);
        }else {
            budgetLineItemController.setFunctionType(getFunctionType());
            formulatedCostBudgetLineItemController.setFunctionType(getFunctionType());
        }
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
        if(budgetDetailBean.isFormualtedLineItem()){
            formulatedCostBudgetLineItemController.setFormData(budgetDetailBean);
            formulatedCostBudgetLineItemController.display();
            queryEngine.update(queryKey,budgetDetailBean);
            budgetPeriodTableModel.getData().set(selectedRow,budgetDetailBean);
            budgetPeriodTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
        }else{
            //Added for Case #3121 - start
            double rate = calculateUnitRates(budgetDetailBean, budgetInfoBean);
            if(rate != 0.0 || tuitionFeeAutoCalculation) {
                budgetLineItemController.enableCostElement(false);
            } else {
                budgetLineItemController.enableCostElement(true);
            }
            //Added for Case #3121 - end
            budgetLineItemController.display();
        }
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
        
       
    }
    
    /** displays cost elment lookup dialog. */
    public void costElementLookup() throws CoeusClientException{
        
        if(groupByMode) return ;
        // Added for Bug fix case#2948 - Start  2              
        // Check if Personnel details exist for the line item. 
        // if yes, validation is perfomed that Cannot change the Cost Element   
        selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
        BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
        try{
            CoeusVector vecExistingBeansBaseWin = null;
            //Checking for Generate Periods
            Equals propNo = new Equals("proposalNumber", budgetDetailBean.getProposalNumber());
            Equals versionNo = new Equals("versionNumber", new Integer(budgetDetailBean.getVersionNumber()));
            Equals periodNo = new Equals("budgetPeriod", new Integer(budgetDetailBean.getBudgetPeriod()));
            
            NotEquals notDelete = new NotEquals("acType", TypeConstants.DELETE_RECORD);
            Equals eqNull = new Equals("acType", null);
            
            And propVersion = new And(propNo, versionNo);
            Or notDeleteOrEqNull = new Or(notDelete, eqNull);
            And periodNotDeleteOrEqNull = new And(periodNo, notDeleteOrEqNull);
            
            And condition = new And(propVersion, periodNotDeleteOrEqNull);
            
            CoeusVector vecBudgetDetailBean = null;
            
            vecBudgetDetailBean = queryEngine.executeQuery(queryKey, BudgetDetailBean.class, condition);
            if(vecBudgetDetailBean!=null && vecBudgetDetailBean.size()>0){
                BudgetDetailBean budgetBean = (BudgetDetailBean) vecBudgetDetailBean.get(0);
                BudgetPersonnelDetailsBean budgetPersonnelDetailsBean =  new BudgetPersonnelDetailsBean();
                budgetPersonnelDetailsBean.setProposalNumber(budgetBean.getProposalNumber());
                budgetPersonnelDetailsBean.setVersionNumber(budgetBean.getVersionNumber());
                budgetPersonnelDetailsBean.setBudgetPeriod(budgetBean.getBudgetPeriod());
                budgetPersonnelDetailsBean.setLineItemNumber(budgetBean.getLineItemNumber());
                
                Equals equalsActype = new Equals("acType",null);
                NotEquals notequalsActype = new NotEquals("acType",TypeConstants.DELETE_RECORD);
                Or actypeBoth = new Or(equalsActype,notequalsActype);
                
                Equals equalsBudgetPeriod = new Equals("budgetPeriod",new Integer(budgetBean.getBudgetPeriod()));
                Equals equalsLineItem = new Equals("lineItemNumber",new Integer(budgetDetailBean.getLineItemNumber()));
                
                And thisPeriodLineItem = new And(equalsBudgetPeriod,equalsLineItem);
                And matchBoth = new And(thisPeriodLineItem,actypeBoth);
                vecExistingBeansBaseWin = queryEngine.executeQuery(queryKey,budgetPersonnelDetailsBean.getClass(),matchBoth);
                if(vecExistingBeansBaseWin !=null && vecExistingBeansBaseWin.size()>0){
                    CoeusOptionPane.showErrorDialog("Personnel details exist for the line item. Cannot change the Cost Element");
                    return; 
                }
            }
        } catch(CoeusException coeusException) {
            coeusException.getMessage();
            return;
        }
        // Added for Bug fix case#2948 - End 2       
        String connectTo = CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType('I');
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(! response.isSuccessfulResponse()) {
           throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
        
        Vector vecCostElements = response.getDataObjects();
        DepartmentBudgetFormBean departmentBudgetFormBean;
        ComboBoxBean comboBoxBean;
        Vector vecComboBoxBean = new Vector();
        
        for(int index = 0; index < vecCostElements.size(); index++) {
            departmentBudgetFormBean = (DepartmentBudgetFormBean)vecCostElements.get(index);
            comboBoxBean = new ComboBoxBean(departmentBudgetFormBean.getCostElement(), departmentBudgetFormBean.getDescription());
            vecComboBoxBean.add(comboBoxBean);
        }
        
        String colNames[] = {"Code","Description"};
        OtherLookupBean otherLookupBean = new OtherLookupBean(COST_ELEMENT_LOOKUP_TITLE, vecComboBoxBean, colNames);
        costElementsLookupWindow = new CostElementsLookupWindow(otherLookupBean);
        
        //Check button click - OK or Cancel
        if(otherLookupBean.getSelectedInd() == -1) return ;
        
        //Get Selected Row for Cost Elements
        int selectedRow = costElementsLookupWindow.getDisplayTable().getSelectedRow();
        if(selectedRow == -1) return ;
        
        departmentBudgetFormBean = (DepartmentBudgetFormBean)vecCostElements.get(selectedRow);
        
        selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
//        BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
        
        try{
            
            //Check if prev Cost element & new Cost Element is Same.
            if(budgetDetailBean.getCostElement().equals(departmentBudgetFormBean.getCostElement())) {
                //Set Cost & Cost Sharing to 0
                BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                CoeusVector vecCalAmtsDetails = queryEngine.executeQuery(queryKey, budgetDetailCalAmountsBean);
                if(vecCalAmtsDetails !=null && vecCalAmtsDetails.size() > 0) {
                    for(int index = 0; index < vecCalAmtsDetails.size(); index++) {
                        budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecCalAmtsDetails.get(index);
                        budgetDetailCalAmountsBean.setCalculatedCost(0);
                        budgetDetailCalAmountsBean.setCalculatedCostSharing(0);
                        budgetDetailCalAmountsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, budgetDetailCalAmountsBean);
                    }
                }
                budgetPeriodForm.pnlLineItemCalculatedAmounts.setFormData(budgetDetailBean);
                return ;
            }
            
            budgetDetailBean.setCostElement(departmentBudgetFormBean.getCostElement());
            budgetDetailBean.setCostElementDescription(departmentBudgetFormBean.getDescription());
            budgetDetailBean.setBudgetCategoryCode(departmentBudgetFormBean.getBudgetCategoryCode());
         //  Case# 2924 begin
         //  UT added codes for OnOffCampusFlag to default to the new OnOffCampusFlag in the OSP$Budget table 
            CoeusVector vecBudgetInfo = queryEngine.executeQuery(queryKey, budgetInfoBean);
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean)vecBudgetInfo.get(0);
            //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - start
            if(budgetInfoBean.isDefaultIndicator()){
                budgetDetailBean.setOnOffCampusFlag(departmentBudgetFormBean.isOnOffCampusFlag());
            }else{
                if(budgetInfoBean.isOnOffCampusFlag())
                    budgetDetailBean.setOnOffCampusFlag(true);
                else
                    budgetDetailBean.setOnOffCampusFlag(false);
            }            
//            budgetDetailBean.setOnOffCampusFlag(budgetInfoBean.isOnOffCampusFlag());
            //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - end
         //   budgetDetailBean.setOnOffCampusFlag(departmentBudgetFormBean.isOnOffCampusFlag());
         //  end of Case# 2924    
            budgetDetailBean.setCategoryType(departmentBudgetFormBean.getCategoryType());
            // Addded for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting.- Start
            budgetDetailBean.setIsFormualtedLineItem(canLineItemFormulated(budgetDetailBean));
            // Addded for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting.- End

            budgetPeriodTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
            budgetJustifyTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
            
            budgetPeriodEditor.cancelCellEditing();
            
            setCalAmounts(budgetDetailBean, departmentBudgetFormBean.getCostElement());
            
            budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
            
            //Added for Case #3121 - start
            TuitionFeeBean tuitionFeeBean = calculateLineItemDates(budgetDetailBean, budgetPeriodBean.getStartDate(), budgetPeriodBean.getEndDate());            
            if(tuitionFeeBean != null) {
                java.sql.Timestamp strTimeStamp = tuitionFeeBean.getStartDate();
                java.sql.Timestamp endTimeStamp = tuitionFeeBean.getEndDate();
                if(strTimeStamp != null && endTimeStamp != null) {
                    budgetDetailBean.setLineItemStartDate(new java.sql.Date(strTimeStamp.getTime()));
                    budgetDetailBean.setLineItemEndDate(new java.sql.Date(endTimeStamp.getTime()));
                }
            }
            double rate = calculateUnitRates(budgetDetailBean, budgetInfoBean);
            if(rate != 0.0 && tuitionFeeAutoCalculation) {
                budgetDetailBean.setLineItemCost(rate);
            }
            //Added for Case #3121 - start
            
            queryEngine.update(queryKey, budgetDetailBean);
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /** sets cal amounts for the budget line item.
     * @param budgetDetailBean budgetDetailBean
     * @param costElement cost element code.
     */
    private void setCalAmounts(BudgetDetailBean budgetDetailBean, String costElement) throws CoeusClientException{
        try{
            //Set Ac TYpe  = D for Old Budget Detail Cal Amts
            BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
            budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
            budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
            budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
            budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
            CoeusVector vecOldDetails = queryEngine.executeQuery(queryKey, budgetDetailCalAmountsBean);
            if(vecOldDetails !=null && vecOldDetails.size() > 0) {
                for(int index = 0; index < vecOldDetails.size(); index++) {
                    ((CoeusBean)vecOldDetails.get(index)).setAcType(TypeConstants.DELETE_RECORD);
                    queryEngine.delete(queryKey, (CoeusBean)vecOldDetails.get(index));
                }
            }
            
            //Set Valid CE Rates
            CoeusVector vecValidRateTypes = getValidCERateTypes(costElement);
            if(vecValidRateTypes == null) vecValidRateTypes = new CoeusVector();
            
            //Check wheather it contains Inflation Rate
            Equals eqInflation = new Equals("rateClassType", RateClassTypeConstants.INFLATION);
            //CoeusVector vecInflation = queryEngine.executeQuery(queryKey, ValidCERateTypesBean.class, eqInflation);
            CoeusVector vecInflation = vecValidRateTypes.filter(eqInflation);
            
            if(vecInflation !=null && vecInflation.size() > 0) {
                budgetDetailBean.setApplyInRateFlag(true);
            }else {
                budgetDetailBean.setApplyInRateFlag(false);
            }
            
            CoeusVector vecBudgetInfo = queryEngine.executeQuery(queryKey, budgetInfoBean);
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean)vecBudgetInfo.get(0);
            
            NotEquals nEqInflation = new NotEquals("rateClassType",RateClassTypeConstants.INFLATION);
            Equals eqOH = new Equals("rateClassType", RateClassTypeConstants.OVERHEAD);
            NotEquals nEqOH = new NotEquals("rateClassType", RateClassTypeConstants.OVERHEAD);
            Equals eqBudgetRateClass = new Equals("rateClassCode",new Integer(budgetInfoBean.getOhRateClassCode()));
            
            And eqOHAndEqBudgetRateClass = new And(eqOH, eqBudgetRateClass);
            Or eqOHAndEqBudgetRateClassOrNEqOH = new Or(eqOHAndEqBudgetRateClass, nEqOH);
            And nEqInflationAndeqOHAndEqBudgetRateClassOrNEqOH = new And(nEqInflation, eqOHAndEqBudgetRateClassOrNEqOH);
            
            vecValidRateTypes = vecValidRateTypes.filter(nEqInflationAndeqOHAndEqBudgetRateClassOrNEqOH);
            
            /** Fix for Defect ID: GNBGT-DEF-004 Starts here **/
            /**
             * Check whether any LA rates are applicable for the home unit, if not 
             * filter out all the LA rates
             */
            CoeusVector cvLARates = queryEngine.getDetails(queryKey, ProposalLARatesBean.class);
            
            if (cvLARates == null || cvLARates.size() == 0) {
                NotEquals neqLA = new NotEquals("rateClassType",RateClassTypeConstants.LAB_ALLOCATION);
                NotEquals neqLASal = new NotEquals("rateClassType", RateClassTypeConstants.LA_WITH_EB_VA);
                And laAndLaSal = new And(neqLA, neqLASal);
                
                vecValidRateTypes = vecValidRateTypes.filter(laAndLaSal);
            }
            
            /** Fix for Defect ID: GNBGT-DEF-004 Ends here **/
            
            //insert to Query Engine
            if(vecValidRateTypes != null && vecValidRateTypes.size() > 0) {
                for(int index = 0; index < vecValidRateTypes.size(); index++) {
                    ValidCERateTypesBean validCERateTypesBean;
                    
                    budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                    validCERateTypesBean = (ValidCERateTypesBean)vecValidRateTypes.get(index);
                    
                    budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                    budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                    budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                    budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                    budgetDetailCalAmountsBean.setRateClassType(validCERateTypesBean.getRateClassType());
                    budgetDetailCalAmountsBean.setRateClassCode(validCERateTypesBean.getRateClassCode());
                    budgetDetailCalAmountsBean.setRateTypeCode(validCERateTypesBean.getRateTypeCode());
                    budgetDetailCalAmountsBean.setRateClassDescription(validCERateTypesBean.getRateClassDescription());
                    budgetDetailCalAmountsBean.setRateTypeDescription(validCERateTypesBean.getRateTypeDescription());
                    budgetDetailCalAmountsBean.setApplyRateFlag(true);
                    budgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, budgetDetailCalAmountsBean);
                }
            }
            
            Equals eqLabAllocSal = new Equals("rateClassType",RateClassTypeConstants.LA_WITH_EB_VA);
            CoeusVector vecLabAllocSal = vecValidRateTypes.filter(eqLabAllocSal);
            
            
            if(vecLabAllocSal != null && vecLabAllocSal.size() > 0) {
                //Has Lab allocation and Salaries Entry (i.e Rate Class Type = Y)
                Equals eqE = new Equals("rateClassType",RateClassTypeConstants.EMPLOYEE_BENEFITS);
                Equals eqV = new Equals("rateClassType",RateClassTypeConstants.VACATION);
                
                CoeusVector vecCalCTypes = queryEngine.executeQuery(queryKey, ValidCalcTypesBean.class, eqE);
                if (vecCalCTypes.size() > 0) {
                    ValidCalcTypesBean validCalcTypesBean = (ValidCalcTypesBean) vecCalCTypes.get(0);
                    if (validCalcTypesBean.getDependentRateClassType().equals(RateClassTypeConstants.LA_WITH_EB_VA)) {
                        budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                        budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                        budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                        budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                        budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                        budgetDetailCalAmountsBean.setRateClassType(validCalcTypesBean.getRateClassType());
                        budgetDetailCalAmountsBean.setRateClassCode(validCalcTypesBean.getRateClassCode());
                        budgetDetailCalAmountsBean.setRateTypeCode(validCalcTypesBean.getRateTypeCode());
                        budgetDetailCalAmountsBean.setRateClassDescription(validCalcTypesBean.getRateClassDescription());
                        budgetDetailCalAmountsBean.setRateTypeDescription(validCalcTypesBean.getRateTypeDescription());
                        budgetDetailCalAmountsBean.setApplyRateFlag(true);
                        budgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, budgetDetailCalAmountsBean);
                    }
                }//End IF Size > 0
                
                vecCalCTypes = queryEngine.executeQuery(queryKey, ValidCalcTypesBean.class, eqV);
                if (vecCalCTypes.size() > 0) {
                    ValidCalcTypesBean validCalcTypesBean = (ValidCalcTypesBean) vecCalCTypes.get(0);
                    if (validCalcTypesBean.getDependentRateClassType().equals(RateClassTypeConstants.LA_WITH_EB_VA)) {
                        budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                        budgetDetailCalAmountsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                        budgetDetailCalAmountsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                        budgetDetailCalAmountsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                        budgetDetailCalAmountsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                        budgetDetailCalAmountsBean.setRateClassType(validCalcTypesBean.getRateClassType());
                        budgetDetailCalAmountsBean.setRateClassCode(validCalcTypesBean.getRateClassCode());
                        budgetDetailCalAmountsBean.setRateTypeCode(validCalcTypesBean.getRateTypeCode());
                        budgetDetailCalAmountsBean.setRateClassDescription(validCalcTypesBean.getRateClassDescription());
                        budgetDetailCalAmountsBean.setRateTypeDescription(validCalcTypesBean.getRateTypeDescription());
                        budgetDetailCalAmountsBean.setApplyRateFlag(true);
                        budgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, budgetDetailCalAmountsBean);
                    }
                }//End IF Size > 0
                
            }//End IF Lab Allocation Salaries
            
            budgetPeriodForm.pnlLineItemCalculatedAmounts.setFormData(budgetDetailBean);
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /** Displays personnel Detail Dialog if this is of personnel category. */
    public void personnelBudget() {
        //Bug Fix: 1652 Start
        budgetPeriodEditor.stopCellEditing();
        //Bug Fix: 1652 End
        
        if(groupByMode) return ;
        
        int selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
        if(selectedRow < 0){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("Please select a personnel line Item. "));
         return ;   
        }
        
        //Check if this Line Item is of Personnel Category.
        BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
        budgetDetailBean.setUnitNumber(budgetBean.getUnitNumber());
        //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
        if(budgetDetailBean.getCategoryType() == PERSONNEL) {
            if(isParentProposal()){
                personnelBudgetDetailController.setParentProposal(isParentProposal());
                personnelBudgetDetailController.setProposalHierarchyBean(getProposalHierarchyBean());
            }
            
            personnelBudgetDetailController.setFormData(budgetDetailBean);
            personnelBudgetDetailController.display();
            calculate();
            /** Bug Fix for CTRL+A
             */
            budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(DESCRIPTION_COLUMN,DESCRIPTION_COLUMN);
            budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
            budgetPeriodForm.tblPeriodLineItem.editCellAt(selectedRow,DESCRIPTION_COLUMN);
            budgetPeriodForm.tblPeriodLineItem.getEditorComponent().requestFocusInWindow();
            
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECTED_ITEM_IS_NOT_OF_PERSONNEL_CATEGORY));
        }
    }
    
    /** apply to later periods. */
    public boolean applyToLaterPeriods() throws CoeusClientException{
        //Commented for Bug Fix  - Apply to later periods should work in new Mode Also.
        //if(getFunctionType() != TypeConstants.MODIFY_MODE) return false;
        
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) return false;
        
        int selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_LINE_ITEM));
            return false;
        }
        BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
        if(budgetDetailBean.getCostElement() == null || budgetDetailBean.getCostElement().equals(EMPTY_STRING)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(COST_ELEMENT_NOT_PRESENT));
            return false;
        }
        try{
            CoeusVector vecPeriodData = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            vecPeriodData.sort("budgetPeriod", true);
            
            if(vecPeriodData == null || vecPeriodData.size() == 0) return false;
            if(budgetPeriodBean.getBudgetPeriod() == vecPeriodData.size()) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANNOT_PERFORM_THIS_OPERATION_ON_LAST_PERIOD_OF_BUDGET));
                return false;
            }
            //Check if all periods are generated. - in Budget base Window Controller
            
            budgetPeriodEditor.stopCellEditing();
            
            int currentPeriod = budgetPeriodBean.getBudgetPeriod();
            int lineItemNumber = budgetDetailBean.getLineItemNumber();
            int totalPeriods = vecPeriodData.size();
            CoeusVector vecBudgetDetails, vecBudgetPersonnelDetails;
            BudgetDetailBean currentBudgetDetailBean = null, nextBudgetDetailBean, newBudgetDetailBean;
            BudgetPeriodBean nextBudgetPeriodBean;
            boolean displayMessage = true;
            int maxLINum = 0;
            int maxSeqNum = 0;
            //get the max line item number > current period
            GreaterThan gtPeriod = new GreaterThan("budgetPeriod", new Integer(currentPeriod));
            CoeusVector vecMaxLI = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, gtPeriod);
            if(vecMaxLI != null && vecMaxLI.size() > 0) {
                vecMaxLI.sort("lineItemNumber", false);
                maxLINum = ((BudgetDetailBean)vecMaxLI.get(0)).getLineItemNumber();
            }
            
            maxLINum = maxLINum + 1;
            
            for(int period = currentPeriod + 1; period <= totalPeriods; period++) {
                double lineItemCost = 0;
                //check if selected line item is present for this period
                Equals eqPeriod = new Equals("budgetPeriod", new Integer(period));
                Equals eqLINumber = new Equals("basedOnLineItem", new Integer(lineItemNumber));
                And eqPeriodAndEqLINumber = new And(eqPeriod, eqLINumber);
                vecBudgetDetails = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, eqPeriodAndEqLINumber);
                
                CoeusVector vecCurrentPeriodDetail = queryEngine.getActiveData(queryKey, BudgetDetailBean.class,
                    new And(
                        new Equals("budgetPeriod", new Integer(period - 1)), 
                        new Equals("lineItemNumber", new Integer(lineItemNumber))
                    ));
                
                if(vecCurrentPeriodDetail != null || vecCurrentPeriodDetail.size() > 0) {
                    currentBudgetDetailBean = (BudgetDetailBean)vecCurrentPeriodDetail.get(0);
                }
                
                //bug fix : 746, 876 - START
                //set the line item cost as current line item cost
                lineItemCost = currentBudgetDetailBean.getLineItemCost();
                //bug fix : 746, 876 - END
                
                //if next period line item is based on same line item then continue with next period.
                if(vecBudgetDetails != null && vecBudgetDetails.size() > 0) {
                    
                    nextBudgetDetailBean = (BudgetDetailBean)vecBudgetDetails.get(0);
                    
//                    vecBudgetPersonnelDetails = queryEngine.getActiveData(queryKey, BudgetPersonnelDetailsBean.class, 
//                    new And(
//                        new Equals("budgetPeriod", new Integer(period - 1)), 
//                        new Equals("lineItemNumber", new Integer(lineItemNumber))
//                    ));
                    
                    vecBudgetPersonnelDetails = queryEngine.getActiveData(queryKey, BudgetPersonnelDetailsBean.class, 
                    new And(
                        new Equals("budgetPeriod", new Integer(period)), 
                        new Equals("lineItemNumber", new Integer(lineItemNumber))
                    ));
                    
                    //if(vecBudgetPersonnelDetails != null && vecBudgetPersonnelDetails.size() > 0) {
                    if(nextBudgetDetailBean.getCategoryType() == PERSONNEL || 
                        (vecBudgetPersonnelDetails != null && vecBudgetPersonnelDetails.size() > 0)) {
                        CoeusOptionPane.showInfoDialog("This line item contains personnel budget details"+
                        " and there is already a line item on period " + period +
                        " based on this line item. \n" + "Cannot apply the changes to later periods.");
                        return false;
                    }
   
                    CoeusVector cvtFormualtedDetails = queryEngine.getActiveData(queryKey, BudgetFormulatedCostDetailsBean.class,eqPeriod);

                    //Display this message only Once.
                    if(displayMessage) {
                        displayMessage = false;
                        int selection = CoeusOptionPane.showQuestionDialog("There is already a line item on period " +
                        period + " based on this line item. \n" +
                        "Do you want to apply changes to existing line items on later periods.",
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                        if(selection == CoeusOptionPane.SELECTION_NO) {
                            return false;
                        }
                    }//End if - display Message
                    
                    //Update line item cost after applying inflation.
                    
                    // Added by chandra/Geo - start 07/06/04
                    // Only if the inflation falg is true, then only perform calculateInflation.
                    // otherwise don't do anything.
                    if(currentBudgetDetailBean.isApplyInRateFlag()){
                        lineItemCost = calculateInflation(currentBudgetDetailBean, nextBudgetDetailBean.getLineItemStartDate());
                    }
                    //end chandra/Geo - 07/06/04 
                    BudgetDetailBean copyBudgetDetailBean = (BudgetDetailBean)ObjectCloner.deepCopy(currentBudgetDetailBean);
                    copyBudgetDetailBean.setBudgetPeriod(nextBudgetDetailBean.getBudgetPeriod());
                    copyBudgetDetailBean.setLineItemNumber(nextBudgetDetailBean.getLineItemNumber());
                    copyBudgetDetailBean.setBasedOnLineItem(currentBudgetDetailBean.getLineItemNumber());
                    copyBudgetDetailBean.setLineItemSequence(nextBudgetDetailBean.getLineItemSequence());
                    copyBudgetDetailBean.setLineItemStartDate(nextBudgetDetailBean.getLineItemStartDate());
                    copyBudgetDetailBean.setLineItemEndDate(nextBudgetDetailBean.getLineItemEndDate());
                    
                    
                    //Check for Cal Amts 
                    if(! copyBudgetDetailBean.getCostElement().equals(nextBudgetDetailBean.getCostElement())) {
                        //Delete old Cal Amts.
                        Equals eqBgtPeriod = new Equals("budgetPeriod", new Integer(nextBudgetDetailBean.getBudgetPeriod()));
                        Equals eqLINum = new Equals("lineItemNumber", new Integer(nextBudgetDetailBean.getLineItemNumber()));
                        And eqPeriodAndEqLINum = new And(eqBgtPeriod, eqLINum);
                        
                        CoeusVector vecCalAmts = queryEngine.getActiveData(queryKey, BudgetDetailCalAmountsBean.class, eqPeriodAndEqLINum);
                        if(vecCalAmts != null && vecCalAmts.size() > 0) {
                            BudgetDetailCalAmountsBean budgetDetailCalAmountsBean;
                            int size = vecCalAmts.size();
                            for(int index = 0; index < size; index++) {
                                budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecCalAmts.get(index);
                                budgetDetailCalAmountsBean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, budgetDetailCalAmountsBean);
                            }//End For - Delete Budget Detail Cal Amts.                        
                        }//End IF - vecCalAmts != null
                        //queryEngine.setUpdate(queryKey, BudgetDetailCalAmountsBean.class, "acType", String.class, TypeConstants.DELETE_RECORD, eqPeriodAndEqLINum);

                        //Add new Cal Amts
                        eqBgtPeriod = new Equals("budgetPeriod", new Integer(currentBudgetDetailBean.getBudgetPeriod()));
                        eqLINum = new Equals("lineItemNumber", new Integer(currentBudgetDetailBean.getLineItemNumber()));
                        eqPeriodAndEqLINum = new And(eqBgtPeriod, eqLINum);
                        CoeusVector vecBudgetDetailCalAmts = queryEngine.getActiveData(queryKey, BudgetDetailCalAmountsBean.class, eqPeriodAndEqLINum);
                        
                        if(vecBudgetDetailCalAmts != null && vecBudgetDetailCalAmts.size() > 0) {
                            BudgetDetailCalAmountsBean budgetDetailCalAmountsBean;
                            int size = vecBudgetDetailCalAmts.size();
                            for(int index = 0; index < size; index++) {
                                budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecBudgetDetailCalAmts.get(index);
                                budgetDetailCalAmountsBean.setBudgetPeriod(nextBudgetDetailBean.getBudgetPeriod());
                                budgetDetailCalAmountsBean.setLineItemNumber(nextBudgetDetailBean.getLineItemNumber());
                                budgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey, budgetDetailCalAmountsBean);
                            }//End For - BudgetDetail Cal Amts Inserting
                        }//End IF - vecBudgetDetailCalAmts !=null
                        
                    }
                    
                    nextBudgetDetailBean = copyBudgetDetailBean;
                    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
                    CoeusVector cvFormualtedDetails  = queryEngine.getActiveData(queryKey, BudgetFormulatedCostDetailsBean.class,
                            new And(
                            new Equals("budgetPeriod", new Integer(period)),
                            new Equals("lineItemNumber", new Integer(copyBudgetDetailBean.getLineItemNumber()))
                            ));
                    if(cvFormualtedDetails != null && !cvFormualtedDetails.isEmpty()){
                        for(Object formulatedCostDetails : cvFormualtedDetails){
                            BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formulatedCostDetails;
                            queryEngine.delete(queryKey,budgetFormulatedCostDetailsBean);
                        }
                    }
                    
                    CoeusVector cvFormulatedCostDetails = queryEngine.getActiveData(queryKey, BudgetFormulatedCostDetailsBean.class,
                            new And(
                            new Equals("budgetPeriod", new Integer(period - 1)),
                            new Equals("lineItemNumber", new Integer(lineItemNumber))
                            ));
                    if(cvFormulatedCostDetails != null && !cvFormulatedCostDetails.isEmpty()){
                        for(Object formulatedCostDetails : cvFormulatedCostDetails){
                            BudgetFormulatedCostDetailsBean newBudgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formulatedCostDetails;
                            newBudgetFormulatedCostDetailsBean.setProposalNumber(nextBudgetDetailBean.getProposalNumber());
                            newBudgetFormulatedCostDetailsBean.setVersionNumber(nextBudgetDetailBean.getVersionNumber());
                            newBudgetFormulatedCostDetailsBean.setBudgetPeriod(nextBudgetDetailBean.getBudgetPeriod());
                            newBudgetFormulatedCostDetailsBean.setLineItemNumber(nextBudgetDetailBean.getLineItemNumber());
                            newBudgetFormulatedCostDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                            queryEngine.insert(queryKey, newBudgetFormulatedCostDetailsBean);
                        }
                    }
                    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
                    
                    
                    
                    nextBudgetDetailBean.setLineItemCost(lineItemCost);
                    //nextBudgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, nextBudgetDetailBean);
                    
                    BeanEvent beanEvent = new BeanEvent();
                    beanEvent.setBean(nextBudgetDetailBean);
                    beanEvent.setSource(this);
                    fireBeanUpdated(beanEvent);
                    
                    lineItemNumber = nextBudgetDetailBean.getLineItemNumber();
                    
                    
                    
                    
                    continue ;
                }//End if - check for next period line item is based on same line item
                
                //Get Max Squence Number if Adding
                Equals eqperiod = new Equals("budgetPeriod", new Integer(period));
                CoeusVector vecMaxSeqNum = queryEngine.getActiveData(queryKey, BudgetDetailBean.class, eqperiod);
                if(vecMaxSeqNum != null && vecMaxSeqNum.size() > 0) {
                    vecMaxSeqNum.sort("lineItemSequence", false);
                    maxSeqNum =  ((BudgetDetailBean)vecMaxSeqNum.get(0)).getLineItemSequence() + 1;
                }else {
                    maxSeqNum = 1; //First Entry
                }
                
                nextBudgetPeriodBean = (BudgetPeriodBean)vecPeriodData.get(period - 1);
                
                newBudgetDetailBean = (BudgetDetailBean)ObjectCloner.deepCopy(currentBudgetDetailBean);
                newBudgetDetailBean.setBudgetPeriod(period);
                newBudgetDetailBean.setLineItemSequence(maxSeqNum);
                newBudgetDetailBean.setLineItemNumber(maxLINum);
                
                lineItemNumber = maxLINum;
                
                //set based on Line Item. if first period to insert then copy line item number from
                //prev budgetDetail Bean .else set max line item number.
                /*if(period == (currentPeriod + 1)) {
                    newBudgetDetailBean.setBasedOnLineItem(budgetDetailBean.getLineItemNumber());
                }else {
                    newBudgetDetailBean.setBasedOnLineItem(maxLINum);
                }*/
                newBudgetDetailBean.setBasedOnLineItem(currentBudgetDetailBean.getLineItemNumber());
                
                
                newBudgetDetailBean.setLineItemStartDate(nextBudgetPeriodBean.getStartDate());
                newBudgetDetailBean.setLineItemEndDate(nextBudgetPeriodBean.getEndDate());
                
                newBudgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
                
                Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(period - 1));
                Equals eqLineItemNo = new Equals("lineItemNumber", new Integer(currentBudgetDetailBean.getLineItemNumber()));
                And eqBudgetPeriodAndEqLineItemNo = new And(eqBudgetPeriod, eqLineItemNo);
                CoeusVector vecBudgetPersonnelDetailsBean = queryEngine.getActiveData(queryKey, BudgetPersonnelDetailsBean.class, eqBudgetPeriodAndEqLineItemNo);
                
                /**
                 *if line item contains personnel line items.
                 *then line item cost will be set to 0.
                 *correct cost will be set during calculation.
                 */
                if(vecBudgetPersonnelDetailsBean != null && vecBudgetPersonnelDetailsBean.size() > 0) {
                    lineItemCost = 0;
                }
                
                //Apply inflation only if line item does not contain personnel line item
                    if (newBudgetDetailBean.isApplyInRateFlag() && (vecBudgetPersonnelDetailsBean == null || vecBudgetPersonnelDetailsBean.size() == 0)) {
                        //Cost Calculation
                        /*
                        Equals eqCe  = new Equals("costElement", newBudgetDetailBean.getCostElement());
                        Equals eqInflation = new Equals("rateClassType","I");
                        And ceAndInflation = new And(eqCe, eqInflation);
                        //Check for inflation for the Cost Element.
                        //Get ValidCERateTypesBean From Server Side.
                        CoeusVector vecValidCERateTypes = getValidCERateTypes(newBudgetDetailBean.getCostElement());
                        CoeusVector vecCE = vecValidCERateTypes.filter(ceAndInflation);//queryEngine.executeQuery(queryKey, ValidCERateTypesBean.class, ceAndInflation);
                        
                        if(vecCE != null && vecCE.size() > 0) {
                            Date startDate, endDate;
                            
                            startDate = currentBudgetDetailBean.getLineItemStartDate();
                            endDate = currentBudgetDetailBean.getLineItemStartDate();
                            
                            ValidCERateTypesBean validCERateTypesBean  = (ValidCERateTypesBean)vecCE.get(0);
                            
                            Equals eqRC = new Equals("rateClassCode", new Integer(validCERateTypesBean.getRateClassCode()));
                            Equals eqRT = new Equals("rateTypeCode", new Integer(validCERateTypesBean.getRateTypeCode()));
                            
                            GreaterThan gtSD = new GreaterThan("startDate", startDate);
                            LesserThan ltED = new LesserThan("startDate", endDate);
                            Equals eqED = new Equals("startDate", endDate);
                            Or ltEDOrEqED = new Or(ltED, eqED);
                            
                            And ltOrEqEDAndGtSD = new And(ltEDOrEqED, gtSD);
                            
                            And rcAndRt = new And(eqRC, eqRT);
                                                        
                            And rcAndRtAndLtOrEqEDAndGtSD = new And(rcAndRt, ltOrEqEDAndGtSD);
                            
                            CoeusVector vecPropInflationRates =  queryEngine.executeQuery(queryKey, ProposalRatesBean.class, rcAndRtAndLtOrEqEDAndGtSD);
                            
                            if(vecPropInflationRates != null && vecPropInflationRates.size() > 0) {
                                //Sort so that the recent date comes first
                                vecPropInflationRates.sort("startDate", false);
                                
                                ProposalRatesBean proposalRatesBean = (ProposalRatesBean)vecPropInflationRates.get(0);
                                double applicableRate = proposalRatesBean.getApplicableRate();
                                lineItemCost = lineItemCost * (100 + applicableRate) / 100;

                            }//End For vecPropInflationRates != null ...
                        }//End If vecCE != null ...
                         */
                        lineItemCost = calculateInflation(currentBudgetDetailBean, newBudgetDetailBean.getLineItemStartDate()); 
                    }//Apply Inflation check ends here
                
                //commented for bug fix : 746
                newBudgetDetailBean.setLineItemCost(lineItemCost);
                //bug fix - 746 - START
                //newBudgetDetailBean.setLineItemCost(currentBudgetDetailBean.getLineItemCost());
                //bug fix - 746 - END
                
              queryEngine.insert(queryKey, newBudgetDetailBean);
                
                //Copy Budget Detail Cal Amts Beans
                CoeusVector vecCalAmts = queryEngine.getActiveData(queryKey, BudgetDetailCalAmountsBean.class, eqBudgetPeriodAndEqLineItemNo);
                if(vecCalAmts == null || vecCalAmts.size() == 0) vecCalAmts = new CoeusVector();
                
                BudgetDetailCalAmountsBean newBudgetDetailCalAmountsBean = null;
                
                for(int calAmtsIndex = 0; calAmtsIndex < vecCalAmts.size(); calAmtsIndex++) {
                    newBudgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecCalAmts.get(calAmtsIndex);
                    newBudgetDetailCalAmountsBean.setBudgetPeriod(nextBudgetPeriodBean.getBudgetPeriod());
                    newBudgetDetailCalAmountsBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                    newBudgetDetailCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, newBudgetDetailCalAmountsBean);
                }//End For Copy Budget Detail Cal Amts Beans
                
                  //Added for Case COEUSDEV -188 - Allow for the generation of project period greater than 12 months -start                
               BudgetPersonnelDetailsBean personnelDetailsBean = null; 
               Date personOldStartDate = null;
               Date personOldEndDate = null;                      
                 //Added for Case COEUSDEV -188 - end
                // Added for COEUSDEV-419 / COEUSQA-2402 Prop dev - generate all periods not copying personnel lines when periods > 12 months
               Date previousPeriodStartDate = null;
               Date previousPeriodEndDate = null;  
               // COESUDEV-419 / COEUSQA-2402  -End
                
                //Copy Personnel Detail Beans
                if(vecBudgetPersonnelDetailsBean == null || vecBudgetPersonnelDetailsBean.size() == 0) vecBudgetPersonnelDetailsBean = new CoeusVector();
                BudgetPersonnelDetailsBean newBudgetPersonnelDetailsBean = null;
                for(int personelIndex = 0; personelIndex < vecBudgetPersonnelDetailsBean.size(); personelIndex++) {
                    newBudgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean)vecBudgetPersonnelDetailsBean.get(personelIndex);
                    newBudgetPersonnelDetailsBean.setBudgetPeriod(nextBudgetPeriodBean.getBudgetPeriod());
                    newBudgetPersonnelDetailsBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                    //Modified for Case COEUSDEV -188 - Allow for the generation of project period greater than 12 months -start    
                    
//                    Date pliStartDate = newBudgetPersonnelDetailsBean.getStartDate();
//                    Calendar calendar= Calendar.getInstance();
//                    calendar.setTime(pliStartDate);
//                    calendar.add(Calendar.YEAR, 1);
//                    pliStartDate = calendar.getTime();
                            
                                       
                    Vector vecBudPersonnelDetailsBean = queryEngine.getActiveData(queryKey, BudgetPersonnelDetailsBean.class, eqBudgetPeriodAndEqLineItemNo);
                    if(vecBudPersonnelDetailsBean !=null && vecBudPersonnelDetailsBean.size() > personelIndex){
                        personnelDetailsBean = (BudgetPersonnelDetailsBean)vecBudPersonnelDetailsBean.get(personelIndex);
                        personOldStartDate = personnelDetailsBean.getStartDate();
                        personOldEndDate = personnelDetailsBean.getEndDate();
                    }                    
//                  Date pliStartDate = dtUtils.dateAdd(Calendar.YEAR, newBudgetPersonnelDetailsBean.getStartDate(), 1);
//                    Date pliStartDate =  getValidPersonDateforPeriod(nextBudgetPeriodBean.getStartDate(), nextBudgetPeriodBean.getEndDate(), personnelDetailsBean.getStartDate(), null, personOldStartDate, personOldEndDate);
                    
                    // Date pliEndDate = dtUtils.dateAdd(Calendar.YEAR, newBudgetPersonnelDetailsBean.getEndDate(), 1);
//                    Date pliEndDate =  getValidPersonDateforPeriod(nextBudgetPeriodBean.getStartDate(), nextBudgetPeriodBean.getEndDate(), null ,personnelDetailsBean.getEndDate(), personOldStartDate, personOldEndDate);
                    
                    // COEUSDEV-419 / COEUSQA-2402  Prop dev - generate all periods not copying personnel lines when periods > 12 months -Start
                    //If Person StartDate and End Date is same as Period Start and End Date then Create the person in new period with same start and end data as new period.
                    Date pliStartDate = null;
                    Date pliEndDate = null;
                     Equals eqPreviousBudgetperiod = new Equals("budgetPeriod", new Integer(period-1));
                    Vector  vecPreviousBudgetPeriodBean = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, eqPreviousBudgetperiod);
                    if(vecPreviousBudgetPeriodBean !=null && vecPreviousBudgetPeriodBean.size() >0 ){
                        BudgetPeriodBean previousBudgetPeriodBean = (BudgetPeriodBean)vecPreviousBudgetPeriodBean.get(0);
                        previousPeriodStartDate = previousBudgetPeriodBean.getStartDate();
                        previousPeriodEndDate = previousBudgetPeriodBean.getEndDate();
                    }
                    if(previousPeriodStartDate.equals(personOldStartDate) && previousPeriodEndDate.equals(personOldEndDate) ){
                        pliStartDate = nextBudgetPeriodBean.getStartDate();
                        pliEndDate = nextBudgetPeriodBean.getEndDate();
                    }else{
                        // If Person StartDate is same as Period Start Date and End is not then create the person in new period with same date of new period and
                        // end date = StartDate + no. of months
                        if(previousPeriodStartDate.equals(personOldStartDate) && !previousPeriodEndDate.equals(personOldEndDate) ){
                            pliStartDate = nextBudgetPeriodBean.getStartDate();
                            pliEndDate =  getPersonEndDateforPeriod(nextBudgetPeriodBean.getStartDate(), personOldStartDate, personOldEndDate, pliStartDate);
                        }else{
                            //Modified for COEUSQA-3422 Budget Persons Line Item Details END DATES match Budget Period END DATES in 
                           //Leap Years using February start -Start
                            // Modified for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
                           // If Person Start and Period Date does not match, using existing logic.
                           pliStartDate =  getValidPersonDateforPeriod(nextBudgetPeriodBean.getStartDate(), nextBudgetPeriodBean.getEndDate(), personnelDetailsBean.getStartDate(), null
                                   , personOldStartDate, personOldEndDate, previousPeriodStartDate , previousPeriodEndDate, null );
                           // Date pliEndDate = dtUtils.dateAdd(Calendar.YEAR, newBudgetPersonnelDetailsBean.getEndDate(), 1);
                           pliEndDate =  getValidPersonDateforPeriod(nextBudgetPeriodBean.getStartDate(), nextBudgetPeriodBean.getEndDate(), null ,personnelDetailsBean.getEndDate()
                           , personOldStartDate, personOldEndDate, previousPeriodStartDate , previousPeriodEndDate, pliStartDate );
                           // Modified for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -end
                          //Modified for COEUSQA-3422  -End  
                        }// end of inner condition else
                    }// end of outer condition else
                    // COEUSDEV-419 / COEUSQA-2402  Prop dev - generate all periods not copying personnel lines when periods > 12 months -End

                    
                    //since start date for line item will be same as start date of period.
                    //while generating periods. we can take period start date.
//                    if(nextBudgetPeriodBean.getStartDate().compareTo(pliStartDate) <= 0) {
                    if(nextBudgetPeriodBean.getStartDate().compareTo(pliStartDate) <= 0 && nextBudgetPeriodBean.getEndDate().compareTo(pliStartDate) >= 0 && pliEndDate.compareTo(nextBudgetPeriodBean.getStartDate()) > 0) {
                        newBudgetPersonnelDetailsBean.setStartDate(new java.sql.Date(pliStartDate.getTime()));
                        //set End Date
//                        Date pliEndDate = newBudgetPersonnelDetailsBean.getEndDate(); 
//                        calendar.setTime(pliEndDate);
//                        calendar.add(Calendar.YEAR, 1);
//                        pliEndDate = calendar.getTime();
                         //Modified for Case COEUSDEV -188 - End    
                        if(nextBudgetPeriodBean.getEndDate().compareTo(pliEndDate) < 0) {
                            pliEndDate = nextBudgetPeriodBean.getEndDate();
                        }
                        newBudgetPersonnelDetailsBean.setEndDate(new java.sql.Date(pliEndDate.getTime()));
                       
                        newBudgetPersonnelDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, newBudgetPersonnelDetailsBean);
                        
                        //Copy Budget Personned Details Cal Amts
                        And conditionAndLINo = new And(eqBudgetPeriodAndEqLineItemNo, 
                            //new Equals("lineItemNumber", new Integer(newBudgetPersonnelDetailsBean.getLineItemNumber())));
                            //Above code is commented b'coz LIne item Number is already defined. set Person Number for the 
                            // for the corresponding budget period and line item number
                            new Equals("personNumber", new Integer(newBudgetPersonnelDetailsBean.getPersonNumber())));
                        
                        CoeusVector vecBudgetPersonnelCalAmountsBean = queryEngine.getActiveData(queryKey, BudgetPersonnelCalAmountsBean.class, conditionAndLINo);
                        if(vecBudgetPersonnelCalAmountsBean == null || vecBudgetPersonnelCalAmountsBean.size() == 0) vecBudgetPersonnelCalAmountsBean = new CoeusVector();
                        BudgetPersonnelCalAmountsBean newBudgetPersonnelCalAmountsBean = null;
                        for(int personnelCalAmtsIndex = 0; personnelCalAmtsIndex < vecBudgetPersonnelCalAmountsBean.size(); personnelCalAmtsIndex++) {
                            newBudgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean)vecBudgetPersonnelCalAmountsBean.get(personnelCalAmtsIndex);
                            newBudgetPersonnelCalAmountsBean.setBudgetPeriod(nextBudgetPeriodBean.getBudgetPeriod());
                            newBudgetPersonnelCalAmountsBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                            
                            newBudgetPersonnelCalAmountsBean.setAcType(TypeConstants.INSERT_RECORD);
                            queryEngine.insert(queryKey, newBudgetPersonnelCalAmountsBean);
                            
                        }
                        
                    }
                   
                }//End For Copy Personnel Detail Beans
                
                // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
                CoeusVector cvFormulatedCostDetails = queryEngine.getActiveData(queryKey, BudgetFormulatedCostDetailsBean.class, eqBudgetPeriodAndEqLineItemNo);
                if(cvFormulatedCostDetails != null && !cvFormulatedCostDetails.isEmpty()){
                    for(Object formulatedCostDetails : cvFormulatedCostDetails){
                        BudgetFormulatedCostDetailsBean newBudgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formulatedCostDetails;
                        newBudgetFormulatedCostDetailsBean.setProposalNumber(nextBudgetPeriodBean.getProposalNumber());
                        newBudgetFormulatedCostDetailsBean.setVersionNumber(nextBudgetPeriodBean.getVersionNumber());
                        newBudgetFormulatedCostDetailsBean.setBudgetPeriod(nextBudgetPeriodBean.getBudgetPeriod());
                        newBudgetFormulatedCostDetailsBean.setLineItemNumber(newBudgetDetailBean.getLineItemNumber());
                        newBudgetFormulatedCostDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, newBudgetFormulatedCostDetailsBean);
                    }
                }
                // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
                
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setBean(newBudgetDetailBean);
                beanEvent.setSource(this);
                fireBeanAdded(beanEvent);
                
            }//End For - Periods
            
            return true;
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
    
    /** Calculates budget total direct cost, total indirect cost, cost sharing and
     * total cost and sets it to the bean and GUI.
     */
    public void calculate() {
        double totalDirectCost, totalIndirectCost, costSharingAmount, underrecovery;
        totalDirectCost = calculatePeriodCost(TOTAL_DIRECT_COST);
        totalIndirectCost = calculatePeriodCost(TOTAL_INDIRECT_COST);
        costSharingAmount = calculatePeriodCost(COST_SHARING_AMOUNT);
        underrecovery = calculatePeriodCost(UNDERRECOVERY);
        
        budgetPeriodBean.setTotalDirectCost(totalDirectCost);
        budgetPeriodBean.setTotalIndirectCost(totalIndirectCost);
        budgetPeriodBean.setCostSharingAmount(costSharingAmount);
        double totCost = totalDirectCost+totalIndirectCost;
        budgetPeriodBean.setTotalCost(totCost);
        //Bug Fix Underrecovery - Start
        budgetPeriodBean.setUnderRecoveryAmount(underrecovery);
        //Bug Fix Underrecovery - End
        
        try{
            //Check if values are changed if changed then update to query engine.
            Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
            CoeusVector vecbudgetPeriod = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, eqBudgetPeriod);
            BudgetPeriodBean queryBudgetPeriodBean = new BudgetPeriodBean();
            queryBudgetPeriodBean = (BudgetPeriodBean)vecbudgetPeriod.get(0);
            /** Added by chandra , If the direct cost and indirect costs are added,
             *then the total cost cost will be varied by 0.01 value, so the 
             *strict Equals condition will fail . Hence the given logic
             */
            double queryCost = queryBudgetPeriodBean.getTotalCost();
            double diffCost = totCost - queryCost;
            diffCost = ((double)Math.round(diffCost* Math.pow(10.0, 2) )) / 100;
            diffCost = Math.abs(diffCost);
            StrictEquals strictEquals = new StrictEquals();
            boolean isSame = strictEquals.compare(queryBudgetPeriodBean, budgetPeriodBean);
            if(isSame ||diffCost < 0.02) {
                return ;
            }
            budgetPeriodBean.setAcType(TypeConstants.UPDATE_RECORD);
            queryEngine.update(queryKey, budgetPeriodBean);
            BeanEvent beanEvent = new BeanEvent();
            beanEvent.setBean(budgetPeriodBean);
            beanEvent.setSource(this);
            fireBeanUpdated(beanEvent);
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        
        budgetPeriodForm.txtDirectCost.setValue(totalDirectCost);
        budgetPeriodForm.txtIndirectCost.setValue(totalIndirectCost);
        budgetPeriodForm.txtCostSharing.setValue(costSharingAmount);
        budgetPeriodForm.txtTotalCost.setValue(totalDirectCost + totalIndirectCost);
        //Bug Fix Underrecovery - Start
        budgetPeriodForm.txtUnderrecovery.setValue(underrecovery);
        //Bug Fix Underrecovery - End
        
    }
    
    /** Calculates period cost.
     * @param type valid Cost types are :
     * TOTAL_DIRECT_COST
     * TOTAL_INDIRECT_COST
     * COST_SHARING_AMOUNT
     * @return calculatedAmount
     */
    public double calculatePeriodCost(int type) {
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
        budgetDetailCalAmountsBean.setProposalNumber(budgetPeriodBean.getProposalNumber());
        budgetDetailCalAmountsBean.setVersionNumber(budgetPeriodBean.getVersionNumber());
        budgetDetailCalAmountsBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
        double calculatedAmount = 0;
        
        try{
            CoeusVector vecBudgetDetailsCalAmts = queryEngine.executeQuery(queryKey, budgetDetailCalAmountsBean);
            
            NotEquals notDelete = new NotEquals("acType",TypeConstants.DELETE_RECORD);
            Equals eqNull = new Equals("acType", null);
            Or notDeleteOrEqNull = new Or(notDelete, eqNull);
            vecBudgetDetailsCalAmts = vecBudgetDetailsCalAmts.filter(notDeleteOrEqNull);
            
            switch (type) {
                case TOTAL_DIRECT_COST:
                    calculatedAmount = vecBudgetDetailsCalAmts.sum("calculatedCost", new NotEquals("rateClassType",RateClassTypeConstants.OVERHEAD)) +
                    vecBudgetLineItem.sum("lineItemCost");
                    calculatedAmount =((double)Math.round(calculatedAmount*Math.pow(10.0, 2) )) / 100;
                    break;
                case TOTAL_INDIRECT_COST:
                    calculatedAmount = vecBudgetDetailsCalAmts.sum("calculatedCost", new Equals("rateClassType",RateClassTypeConstants.OVERHEAD));
                    calculatedAmount =((double)Math.round(calculatedAmount*Math.pow(10.0, 2) )) / 100;
                    break;
                case COST_SHARING_AMOUNT:
                    calculatedAmount = vecBudgetDetailsCalAmts.sum("calculatedCostSharing") +
                    vecBudgetLineItem.sum("costSharingAmount");
                    break;
                case UNDERRECOVERY:
                    calculatedAmount = vecBudgetLineItem.sum("underRecoveryAmount");
                    break;
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        return calculatedAmount;
    }
    
    /** returns valid Cost Element Rate Types from Server.
     * @param costElement cost element for which valid Cost Elements have to be got.
     * @return CoeusVector containing valid Cost Element Rate Types.
     */
    public CoeusVector getValidCERateTypes(String costElement) throws CoeusClientException{
        //getValid CE Rate Types for this Cost Element from server and add to query engine
        String connectTo = CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType('J');
        request.setDataObject(costElement);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
            return (CoeusVector)response.getDataObject();
        }else{
            throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    /** returns max line item number.
     * @return max line item number.
     */
    private int getMaxLineItemNumber() {
        if(vecBudgetLineItem == null || vecBudgetLineItem.size() == 0) return 0;
        CoeusVector vecLineItem = new CoeusVector();
        vecLineItem.addAll(vecBudgetLineItem);
        //vecLineItem = vecLineItem.filter(CoeusVector.FILTER_ACTIVE_BEANS);
        vecLineItem.sort("lineItemNumber", false);
        BudgetDetailBean maxLineItemBean = (BudgetDetailBean)vecLineItem.get(0);
        return maxLineItemBean.getLineItemNumber();
    }
    
    /** refreshes the GUI with new Data. */
    public void refresh() {
       /** Bug Fix 2096 -start
        *Keeping the component to be focused on the table or any other component,
        *to identify the form components are in focueesd, it will identify
        *the form while CTRL+A action is performed
        */
        //Hold the selected Row in a variable so that we can set it back to the same selection after refreshing.
        int selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
        boolean isCellEditable = budgetPeriodTableModel.isCellEditable(selectedRow, COST_ELEMENT_COLUMN);
        if(!isCellEditable){
            budgetPeriodForm.txtStartDate.requestFocusInWindow();
        }else{
        //Set the old selected Row
            if(selectedRow >= 0 && budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0) {
                budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow, selectedRow);
                budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(1,1);
                budgetPeriodForm.tblPeriodLineItem.editCellAt(selectedRow,1);
                Component comp = budgetPeriodForm.tblPeriodLineItem.getEditorComponent();
                if(comp != null ){
                    comp.requestFocusInWindow();
                }
            }
        }
        /**Bug Fix 2096 - End
         */
         if(! refreshRequired) return ;
        //Refreshing Data.
        try{
            Equals eqPropNo = new Equals("proposalNumber", budgetPeriodBean.getProposalNumber());
            Equals eqVersion = new Equals("versionNumber", new Integer(budgetPeriodBean.getVersionNumber()));
            Equals eqPeriod = new Equals("budgetPeriod", new Integer(budgetPeriodBean.getBudgetPeriod()));
            
            And eqPropNoAndEqVersion = new And(eqPropNo, eqVersion);
            And eqPropNoAndEqVersionAndEqPeriod = new And(eqPropNoAndEqVersion, eqPeriod);
            
            CoeusVector data = queryEngine.executeQuery(queryKey, budgetPeriodBean);
//            CoeusVector data = queryEngine.executeQuery(queryKey, BudgetPeriodBean.class, eqPropNoAndEqVersionAndEqPeriod);
            if(data != null ) {
                if(data.size() > 0){
                    if(data.get(0) != null) {
                        budgetPeriodBean = (BudgetPeriodBean)data.get(0);
                        setFormData(budgetPeriodBean);
                    }
                }
            }
            
            
            refreshRequired = false;
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /** listens to bean updated event.
     * @param beanEvent BeanEvent.
     */
    public void beanUpdated(BeanEvent beanEvent) {
        if(beanEvent.getSource().equals(this)) return ;
        
        if(beanEvent.getBean().getClass().equals(BudgetDetailBean.class)) {
            //Update Immediately.
            BudgetDetailBean budgetDetailBean = (BudgetDetailBean)beanEvent.getBean();
            
            //Check if it belongs to same period else return
            if(budgetDetailBean.getBudgetPeriod() != budgetPeriodBean.getBudgetPeriod()) return ;
            
            int selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
            if(selectedRow < 0) return ;
            if(budgetPeriodBean.getBudgetPeriod() == budgetDetailBean.getBudgetPeriod()) {
                //Bug Fix : Apply to later periods applies to first Item - START
                /**Apply to later period updates query engine and fires bean updated.
                 *although this code was here for a long time. the bug didn't appear as we used to refresh 
                 *the screen after this event. so we would get the correct data from query engine.
                 *but since now we added some code to select the first row (Bug Fix 2096 in refresh method), 
                 *we have code to edit first row and this fires cell updated event
                 *which updates the query engine with the wrong data and hence the
                 *first row always gets the item which had to be applied to later period.
                */
                int index = budgetDetailBean.getLineItemSequence();
                selectedRow = index - 1;
                //Bug Fix : Apply to later periods applies to first Item - END
                
                //vecBudgetLineItem.set(index - 1, budgetDetailBean);
                vecBudgetLineItem.set(selectedRow, budgetDetailBean);
                budgetPeriodTableModel.fireTableRowsUpdated(selectedRow ,selectedRow);
                budgetJustifyTableModel.fireTableRowsUpdated(selectedRow ,selectedRow);
             
                budgetPeriodForm.pnlLineItemCalculatedAmounts.setFormData(budgetDetailBean);
            }
            
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item -start
            //After changing the on/offcampusflag detail window the values has to be recalculate whole period.
            calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
            // Added for COEUSQA-1977 Enable On Off Campus radio button for each Personnel Line Item - end
            //Added by chandra . After changing the detail window the values has to be 
            // fired immediately. 30 Sept 2004
            calculate();
            // end chandra 30 Sept 2004
            setRefreshRequired(true);
            refresh();
        }else if(beanEvent.getBean().getClass().equals(BudgetPeriodBean.class) &&
        beanEvent.getSource().getClass().equals(BudgetSummaryController.class) &&
        ((BudgetPeriodBean)beanEvent.getBean()).getBudgetPeriod() == budgetPeriodBean.getBudgetPeriod()) {
            
            budgetPeriodBean = (BudgetPeriodBean)beanEvent.getBean();
            
            budgetPeriodForm.txtCostLimit.setValue(budgetPeriodBean.getTotalCostLimit());
            budgetPeriodForm.txtTotalCost.setValue(budgetPeriodBean.getTotalCost());
            budgetPeriodForm.txtDirectCost.setValue(budgetPeriodBean.getTotalDirectCost());
            budgetPeriodForm.txtIndirectCost.setValue(budgetPeriodBean.getTotalIndirectCost());
            budgetPeriodForm.txtUnderrecovery.setValue(budgetPeriodBean.getUnderRecoveryAmount());
            budgetPeriodForm.txtCostSharing.setValue(budgetPeriodBean.getCostSharingAmount());
            
        }else {
            //Update Later.
            refreshRequired = true;
        }
        
    }
    
    /** sets / unsets refresh required flag.
     * @param refreshRequired refresh required flag.
     */
    public void setRefreshRequired(boolean refreshRequired) {
        this.refreshRequired = refreshRequired;
    }
    
    /** returns a boolean indicating whether refresh required.
     * @return returns a boolean indicating whether refresh required.
     */
    public boolean isRefreshRequired() {
        return refreshRequired;
    }
    
    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        int value = budgetPeriodForm.scrPnPeriodLineItem.getVerticalScrollBar().getValue();
        budgetPeriodForm.scrPnlJustify.getVerticalScrollBar().setValue(value);
    }
    
    protected void finalize() throws Throwable {
        //System.out.println("Calling Finalize");
        removeBeanUpdatedListener(this, BudgetPeriodBean.class);
        removeBeanUpdatedListener(this, BudgetDetailBean.class);
        removeBeanUpdatedListener(this, BudgetInfoBean.class);
        removeBeanAddedListener(this, BudgetDetailBean.class);
        
        personnelBudgetDetailController.cleanUp();
        
        super.finalize();
    }
    
    public void customizeView() {
        boolean selected[] = new boolean[2];
        if(groupByMode){
            selected[1] = true;
        }else {
            selected[0] = true;
        }
        customizeViewController.setRadioButtonState(selected);
        customizeViewController.setSelectedCalculatedAmounts(budgetPeriodForm.pnlLineItemCalculatedAmounts.isVisible());
        
        customizeViewController.formatFields();
        customizeViewController.display();
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        CustomizeViewForm customizeViewForm = (CustomizeViewForm)customizeViewController.getControlledUI();
        if(source.equals(customizeViewForm.btnApply)) {
            applyCustomizedView();
        }else if(source.equals(customizeViewForm.btnCancel)){
            customizeViewController.setVisible(false);
        }else if(source.equals(customizeViewForm.btnOk)) {
            applyCustomizedView();
            customizeViewController.setVisible(false);
        }
    }
    
    private void applyCustomizedView() {
        if(budgetPeriodForm.tblPeriodLineItem.getRowCount() == 0) return ;
        
        CustomizeViewForm customizeViewForm = (CustomizeViewForm)customizeViewController.getControlledUI();
        
        //Check to show/hide cal amts table
        budgetPeriodForm.pnlLineItemCalculatedAmounts.setVisible(customizeViewForm.chkShowCalculatedAmts.isSelected());
        
        JCheckBox components[] = customizeViewController.getCheckBoxComponents();
        //0 th index is item seq no
        int newVisibleColumns[] = new int[customizeViewController.getSelectedCount() + 1];
        newVisibleColumns[0] = 0; // item seq no - always visible in default mode.
        int visibleCount = 1;
        int columns = budgetPeriodForm.tblPeriodLineItem.getColumnCount();
        
        if((! columnsMoved) && groupByMode) {
            //Show Column
            budgetPeriodForm.tblPeriodLineItem.addColumn(tableColumns[0]);
            //Since Column gets appended.Move Column to its original location.
            budgetPeriodForm.tblPeriodLineItem.getTableHeader().getColumnModel().removeColumnModelListener(this);
            budgetPeriodForm.tblPeriodLineItem.moveColumn(
            columns , 0);
            budgetPeriodForm.tblPeriodLineItem.getTableHeader().getColumnModel().addColumnModelListener(this);
            columns = columns + 1;
        }
        
        
        for(int index = 1; index < colNames.length; index++) {
            //setting visible columns
            if(components[index - 1].isSelected()) {
                newVisibleColumns[visibleCount] = index;
                visibleCount++;
            }
            
            //if columns moved then no need to add/delete its done later in same method
            //when all columns get deleted and added orderly.
            if(! columnsMoved) { 
                if(! (components[index - 1].isSelected() == colVisible[index])) {
                    //set column visibility.
                    if(components[index - 1].isSelected()) {
                        //Show Column
                        budgetPeriodForm.tblPeriodLineItem.addColumn(tableColumns[index]);
                        colVisible[index] = true;
                        //Since Column gets appended.Move Column to its original location.
                        budgetPeriodForm.tblPeriodLineItem.getTableHeader().getColumnModel().removeColumnModelListener(this);
                        budgetPeriodForm.tblPeriodLineItem.moveColumn(
                        columns , visibleCount - 1);
                        budgetPeriodForm.tblPeriodLineItem.getTableHeader().getColumnModel().addColumnModelListener(this);
                        columns++;
                    }else{
                        //Hide Column
                        budgetPeriodForm.tblPeriodLineItem.removeColumn(tableColumns[index]);
                        colVisible[index] = false;
                        columns--;
                    }
                }//End IF
            }//End If Columns Moved
        }//End For
        
        //Check If Columns have moved.if moved move it back to its original position.
        if(columnsMoved) {
            budgetPeriodForm.tblPeriodLineItem.getTableHeader().getColumnModel().removeColumnModelListener(this);
            //Column has moved. move to original location
            //budgetPeriodForm.tblPeriodLineItem.moveColumn(
            //index, visibleColumns[index]);
            
            int len = budgetPeriodForm.tblPeriodLineItem.getColumnCount();
            for(int i = 0; i < len; i++) {
                if(visibleColumns.length < i) break;
                budgetPeriodForm.tblPeriodLineItem.removeColumn(tableColumns[visibleColumns[i]]);
            }
            
            //set All columns visibility -> false
            for(int index = 0; index < colVisible.length; index++) {
                colVisible[index] = false;
            }
            
            for(int i = 0; i < newVisibleColumns.length; i++) {
                    budgetPeriodForm.tblPeriodLineItem.addColumn(tableColumns[newVisibleColumns[i]]);
                    colVisible[newVisibleColumns[i]] = true;
            }
            columnsMoved = false;
            budgetPeriodForm.tblPeriodLineItem.getTableHeader().getColumnModel().addColumnModelListener(this);
        }//End If
        
        if(customizeViewForm.rdBtnCategory.isSelected()) {
            groupByMode = true;
            budgetPeriodForm.tblPeriodLineItem.setShowGrid(false);
            budgetPeriodForm.tblPeriodLineItem.setSelectionBackground((Color)UIManager.getDefaults().get("Table.selectionBackground"));
            budgetPeriodForm.tblPeriodLineItem.setSelectionForeground(Color.white);
            budgetPeriodForm.tblPeriodLineItem.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("TabbedPane.tabAreaBackground"));
            //budgetPeriodForm.tblPeriodLineItem.getTableHeader().setReorderingAllowed(false); //Added at end of this block so as to facilitate moving columns.
            budgetPeriodForm.scrPnlJustify.setVisible(false);
                       
            vecBudgetLineItem.sort("budgetCategoryCode", false);
            budgetPeriodTableModel.fireTableDataChanged();
            
            BudgetDetailBean budgetDetailBean;
            int lastCatCode = -1;
            for(int index = 0; index < vecBudgetLineItem.size(); index++) {
                budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(index);
                if(lastCatCode != budgetDetailBean.getBudgetCategoryCode()) {
                    //New Change so increase height.
                    budgetPeriodForm.tblPeriodLineItem.setRowHeight(index, 50);
                    lastCatCode = budgetDetailBean.getBudgetCategoryCode();
                }
            }
            //Hide First Column
            budgetPeriodForm.tblPeriodLineItem.removeColumn(tableColumns[0]);
            visibleColumns = new int[newVisibleColumns.length - 1];
            
            for(int index = 1; index < newVisibleColumns.length; index++) {
                visibleColumns[index - 1] = newVisibleColumns[index];
            }
            
            //setting columns - move cost and quantity to the end
            int colToEnd[] = {COST_COLUMN - 1, QUANTITY_COLUMN - 1}; // - 1  since we're removing one column(i.e first column)
            int cols = visibleColumns.length - 1;
            for(int count = 0; count < colToEnd.length; count++) {
                for(int index = colToEnd[count]; index < cols; index++) {
                    budgetPeriodForm.tblPeriodLineItem.moveColumn(index, index + 1);
                }
            }
            
            budgetPeriodForm.tblPeriodLineItem.getTableHeader().setReorderingAllowed(false);
            
            //Select First Row
            budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(0, 0);
            budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(1,1);
            
            
            return ;
        }else {
            //Check if previously group by mode was selected.
            if(groupByMode) {
                groupByMode = false;
                
                //Sort by line item seq no
                vecBudgetLineItem.sort("lineItemSequence");
            }
            budgetPeriodForm.tblPeriodLineItem.setShowGrid(true);
            budgetPeriodForm.tblPeriodLineItem.setSelectionBackground(Color.yellow);
            budgetPeriodForm.tblPeriodLineItem.setSelectionForeground(Color.black);
            if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
                budgetPeriodForm.tblPeriodLineItem.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("TabbedPane.tabAreaBackground"));
            }else {
                budgetPeriodForm.tblPeriodLineItem.setBackground(Color.white);
            }
            budgetPeriodForm.tblPeriodLineItem.getTableHeader().setReorderingAllowed(true);
            budgetPeriodForm.scrPnlJustify.setVisible(true);
            
        }
        
        visibleColumns = newVisibleColumns;
        budgetPeriodTableModel.fireTableDataChanged();
        
        //Set selection to first row
        budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(0, 0);
        budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(1,1);
        
        
    }//End applyCustomizedView
    
    
    public void columnAdded(TableColumnModelEvent e) {
    }
    
    public void columnMarginChanged(ChangeEvent e) {
    }
    
    public void columnMoved(TableColumnModelEvent tableColumnModelEvent) {
        int fromIndex = tableColumnModelEvent.getFromIndex();
        int toIndex = tableColumnModelEvent.getToIndex();
        if(fromIndex != toIndex) {
            int from = visibleColumns[fromIndex];
            visibleColumns[fromIndex] = visibleColumns[toIndex];
            visibleColumns[toIndex] = from;
        }
        columnsMoved = true;
    }
    
    public void columnRemoved(TableColumnModelEvent e) {
    }
    
    public void columnSelectionChanged(ListSelectionEvent e) {
    }
    
    public String getCategory(BudgetDetailBean budgetDetailBean) throws CoeusException {
        Equals eqCatCode = new Equals("code", EMPTY_STRING + budgetDetailBean.getBudgetCategoryCode());
        CoeusVector vecCatBean = queryEngine.executeQuery(queryKey, BudgetCategoryBean.class, eqCatCode);
        if(vecCatBean != null && vecCatBean.size() > 0) {
            return ((BudgetCategoryBean)vecCatBean.get(0)).getDescription();
        }else {
            return EMPTY_STRING;
        }
    }
    
    public void syncToPeriodCostLimit() {
        System.out.println("Sync to Cost Limit for budget Period : " + budgetPeriodBean.getBudgetPeriod());
        //if the display is in Group by mode do not perform this function
        if(groupByMode) {
            return ;
        }
        
        budgetPeriodBean.setTotalCostLimit(Double.parseDouble(budgetPeriodForm.txtCostLimit.getValue()));
        
        int selectedRow;
        selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
        //If no line item is selected, disp msg "Please select a line item."
        if(selectedRow < 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            SELECT_LINE_ITEM));
            return ;
        }
        
        //If cost element is null disp msg "Cannot perform this operation if cost element is not present."
        if(budgetPeriodTableModel.getValueAt(selectedRow, COST_ELEMENT_COLUMN) == null ||
        budgetPeriodTableModel.getValueAt(selectedRow, COST_ELEMENT_COLUMN).equals(EMPTY_STRING)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            COST_ELEMENT_NOT_PRESENT));
            return ;
        }
        
        BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
        // Added for COEUSQA-3806 : Formulated Cost Line Items should not be eligible for Sync to Direct or Total Cost functionality in Budget - Start
        // If the line item is formualted line item , then sync is not allowed for the line item
        if(budgetDetailBean.isFormualtedLineItem()){
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(FORMULATED_COST_LINE_ITEM_SYNC_COST_LIMIT));
            return;
        }
        // Added for COEUSQA-3806 : Formulated Cost Line Items should not be eligible for Sync to Direct or Total Cost functionality in Budget - End
        Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetDetailBean.getBudgetPeriod()));
        Equals eqLINumber = new Equals("lineItemNumber", new Integer(budgetDetailBean.getLineItemNumber()));
        And eqBudgetPeriodAndEqLINumber = new And(eqBudgetPeriod, eqLINumber);
        
        CoeusVector vecPersonnel = null;
        
        try{
            
            vecPersonnel= queryEngine.executeQuery(queryKey, BudgetPersonnelDetailsBean.class, eqBudgetPeriodAndEqLINumber);
            
            
            //if person_details_flag is "Y", disp msg "Cannot perform this operation on a line item with personel budget details."
            if(vecPersonnel != null && vecPersonnel.size() > 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                CANNOT_PERFORM_THIS_OPERATION_ON_PERSONNEL_LINE_ITEM));
                return ;
            }
            
            //if cost_limit is 0 disp msg "Cost limit for this period is set to 0. Cannot sync a line item cost to zero limit."
            if(budgetPeriodBean.getTotalCostLimit() == 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                CANNOT_SYNC_TO_ZERO_LIMIT));
                //Added for bug fixed for case #2318 start 1
                if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
                        budgetPeriodEditor.stopCellEditing();
                        budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
                        budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
                        budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                    budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
                    }
                //Added for bug fixed for case #2318 end 1
                return ;
            }
            
            saveFormData();
            calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
            setRefreshRequired(true);
            refresh();
            //Added for bug fixed for case #2318 start 
            if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
                budgetPeriodEditor.stopCellEditing();
                budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
                budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
                budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
            }
            //Added for bug fixed for case #2318 end 
            budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
            
            //If total_cost equals total_cost_limit, disp msg "Cost limit and total cost for this period is already in sync."
            double periodTotal = budgetPeriodBean.getTotalCost();
            //periodTotal = (double)Math.round (periodTotal*Math.pow(10.0, 2) ) / 100;
            double costLimit = budgetPeriodBean.getTotalCostLimit();
            //costLimit = (double)Math.round (costLimit*Math.pow(10.0, 2) ) / 100;
            
            if(periodTotal == costLimit) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                TOTAL_COST_ALREADY_IN_SYNC));
                //Added for bug fixed for case #2318 start 2
                if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
                        budgetPeriodEditor.stopCellEditing();
                        budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
                        budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
                        budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                    budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
                    }
                //Added for bug fixed for case #2318 end 2
                return ;
            }
            
            //If total_cost > cost_limit disp msg "Period total is greater than the cost limit for this period.Do you want to reduce this line item cost to make the total cost same as cost limit"
            if(periodTotal > costLimit) {
                int selection = CoeusOptionPane.showQuestionDialog((coeusMessageResources.parseMessageKey(
                REDUCE_LINE_ITEM_COST)), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_NO) {
                    //Added for bug fixed for case #2318 start 3
                    if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
                        budgetPeriodEditor.stopCellEditing();
                        budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
                        budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
                        budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                    budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
                    }
                    //Added for bug fixed for case #2318 end 4
                    return ;
                }
            }//End IF total_cost > cost_limit
            //Continuing with Sync
            
            //Set the Difference as TotalCostLimit minus TotalCost.
            double difference = costLimit - periodTotal;
            //difference = (double)Math.round (difference*Math.pow(10.0, 2) ) / 100;
            double lineItemCost = budgetDetailBean.getLineItemCost();
            //lineItemCost = (double)Math.round (lineItemCost*Math.pow(10.0, 2) ) / 100;
            double multifactor;
            
            //If line_item_cost is 0 then set the value of line_item_cost in line_items to 10000.
            if(lineItemCost == 0) {
                budgetDetailBean.setLineItemCost(10000);
                budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                queryEngine.update(queryKey, budgetDetailBean);
                budgetPeriodTableModel.fireTableCellUpdated(selectedRow, COST_COLUMN);
            }
            
            calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
            
            CoeusVector vecCalAmts = queryEngine.executeQuery(queryKey, BudgetDetailCalAmountsBean.class, eqBudgetPeriodAndEqLINumber);
            
            double totalCost = budgetDetailBean.getLineItemCost() +
            vecCalAmts.sum("calculatedCost", new NotEquals("rateClassType",RateClassTypeConstants.OVERHEAD)) +
            vecCalAmts.sum("calculatedCost", new Equals("rateClassType",RateClassTypeConstants.OVERHEAD));
            
            /*
            double rateClassData = ((double)Math.round(vecCalAmts.sum("calculatedCost", new NotEquals("rateClassType",RateClassTypeConstants.OVERHEAD))*
                        Math.pow(10.0, 2) )) / 100;
            double rateTypeData  = ((double)Math.round(vecCalAmts.sum("calculatedCost", new NotEquals("rateClassType",RateClassTypeConstants.OVERHEAD))*
                        Math.pow(10.0, 2) )) / 100;
            
            double totalCost = budgetDetailBean.getLineItemCost()+rateClassData+rateTypeData;
            totalCost = (double)Math.round (totalCost*Math.pow(10.0, 2) ) / 100;
            */
            /*
            TOTAL_DIRECT_COST:
         
                    calculatedAmount = vecBudgetDetailsCalAmts.sum("calculatedCost", new NotEquals("rateClassType","O")) +
                    vecBudgetLineItem.sum("lineItemCost");
         
            INDIRECT_COST
                    calculatedAmount = vecBudgetDetailsCalAmts.sum("calculatedCost", new Equals("rateClassType","O"));
             */
            
            
            //If the lineItemCost <> 0, set multfactor to TotalCost divided by lineItemCost otherwise multfactor is TotalCost divided by 10000
            if(lineItemCost != 0) {
                multifactor = totalCost / lineItemCost;
                //multifactor = (double)Math.round (multifactor*Math.pow(10.0, 2) ) / 100;
            }else {
                multifactor = totalCost / 10000;
                //multifactor = (double)Math.round (multifactor*Math.pow(10.0, 2) ) / 100;
                budgetDetailBean.setLineItemCost(0);
                budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                queryEngine.update(queryKey, budgetDetailBean);
                calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
                totalCost = 0;
            }
            
            if((totalCost + difference) < 0) {
                CoeusOptionPane.showErrorDialog(INSUFFICIENT_AMOUNT_TO_SYNC);
                //Added for bug fixed for case #2318 start 5
                if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
                    budgetPeriodEditor.stopCellEditing();
                    budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
                    budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
                    budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                    budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
                }
                //Added for bug fixed for case #2318 end 5
                return ;
            }
            
            //Set New Cost
            double newCost = lineItemCost + (difference / multifactor);
            //newCost = (double)Math.round (newCost*Math.pow(10.0, 2) ) / 100;
            budgetDetailBean.setLineItemCost(newCost);
            
            budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
            queryEngine.update(queryKey, budgetDetailBean);
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
        setRefreshRequired(true);
        refresh();
        //Added for bug fixed for case #2318 start 6
        if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
            budgetPeriodEditor.stopCellEditing();
            budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
            budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
            budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                    budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
        }
        //Added for bug fixed for case #2318 end 6
    }
    
    private double calculateInflation(BudgetDetailBean budgetDetailBean, Date endDate) throws CoeusException,CoeusClientException{
        String costElement = budgetDetailBean.getCostElement();
        double lineItemCost = budgetDetailBean.getLineItemCost();
        Date startDate = budgetDetailBean.getLineItemStartDate();
        //Date endDate = budgetDetailBean.getLineItemEndDate();
        
        //Cost Calculation
        Equals eqCe  = new Equals("costElement", costElement);
        Equals eqInflation = new Equals("rateClassType",RateClassTypeConstants.INFLATION);
        And ceAndInflation = new And(eqCe, eqInflation);
        //Check for inflation for the Cost Element.
        //Get ValidCERateTypesBean From Server Side.
        CoeusVector vecValidCERateTypes = getValidCERateTypes(costElement);
        CoeusVector vecCE = vecValidCERateTypes.filter(ceAndInflation);//queryEngine.executeQuery(queryKey, ValidCERateTypesBean.class, ceAndInflation);
        
        if(vecCE != null && vecCE.size() > 0) {
            //Date startDate, endDate;
            
            //startDate = currentBudgetDetailBean.getLineItemStartDate();
            //endDate = currentBudgetDetailBean.getLineItemStartDate();
            
            ValidCERateTypesBean validCERateTypesBean  = (ValidCERateTypesBean)vecCE.get(0);
            
            Equals eqRC = new Equals("rateClassCode", new Integer(validCERateTypesBean.getRateClassCode()));
            Equals eqRT = new Equals("rateTypeCode", new Integer(validCERateTypesBean.getRateTypeCode()));
            
            GreaterThan gtSD = new GreaterThan("startDate", startDate);
            LesserThan ltED = new LesserThan("startDate", endDate);
            Equals eqED = new Equals("startDate", endDate);
            Or ltEDOrEqED = new Or(ltED, eqED);
            
            And ltOrEqEDAndGtSD = new And(ltEDOrEqED, gtSD);
            
            And rcAndRt = new And(eqRC, eqRT);
            
            And rcAndRtAndLtOrEqEDAndGtSD = new And(rcAndRt, ltOrEqEDAndGtSD);
            
            CoeusVector vecPropInflationRates =  queryEngine.executeQuery(queryKey, ProposalRatesBean.class, rcAndRtAndLtOrEqEDAndGtSD);
            
            if(vecPropInflationRates != null && vecPropInflationRates.size() > 0) {
                //Sort so that the recent date comes first
                vecPropInflationRates.sort("startDate", false);
                //Added for COEUSQA-2377 Inflation off campus rate applied no matter the flag setting -start
                // We should always consider the budget detail line item OnOffCampus flag.
//                boolean defaultIndicator = budgetInfoBean.isDefaultIndicator();
                boolean validOnOffCampusFlag = false;
//                if(defaultIndicator){
//                    validOnOffCampusFlag = budgetDetailBean.isOnOffCampusFlag();
//                }else{
//                    validOnOffCampusFlag = budgetInfoBean.isOnOffCampusFlag();
//                }
                validOnOffCampusFlag = budgetDetailBean.isOnOffCampusFlag();
                Equals eqOnOffCampus = new Equals("onOffCampusFlag", validOnOffCampusFlag);
                CoeusVector cvProposalRates = vecPropInflationRates.filter(eqOnOffCampus);//queryEngine.executeQuery(queryKey, ProposalRatesBean.class, eqBreakUpSD);
                ProposalRatesBean proposalRatesBean = (ProposalRatesBean)cvProposalRates.get(0);
                //Added for COEUSQA-2377 - end
                
                //  ProposalRatesBean proposalRatesBean = (ProposalRatesBean)vecPropInflationRates.get(0);
                
//                ProposalRatesBean proposalRatesBean = (ProposalRatesBean)vecPropInflationRates.get(0);
                double applicableRate = proposalRatesBean.getApplicableRate();
                lineItemCost = lineItemCost * (100 + applicableRate) / 100;
                
            }//End For vecPropInflationRates != null ...
        }//End If vecCE != null ...
        return lineItemCost;
    }
    
    public void beanAdded(BeanEvent beanEvent) {
        if(beanEvent.getSource().equals(this)) return ;
        BudgetDetailBean budgetDetailBean = (BudgetDetailBean)beanEvent.getBean();
        budgetPeriodForm.pnlLineItemCalculatedAmounts.setFormData(budgetDetailBean);
         refreshRequired = true;
    }
    
//End Sync to period Cost Limit
    
    //BudgetPeriodTableModel --------------------------------------------------
    /** specifies the methods the Budget Period Table
     * will use to interrogate a data model(Coeus Vector).
     */
    class BudgetPeriodTableModel extends AbstractTableModel {

        private CoeusVector vecBudgetDetailBean;
        private BudgetDetailBean budgetDetailBean;
        TuitionFeeBean tuitionFeeBean = new TuitionFeeBean();
        
        BudgetPeriodTableModel() {
            
        }
        
        /** sets the data to be displayed by the table.
         * @param vecBudgetDetailBean Data to be displayed.
         */
        public void setData(CoeusVector vecBudgetDetailBean) {
            this.vecBudgetDetailBean = vecBudgetDetailBean;
        }
        
        public CoeusVector getData(){
            return vecBudgetDetailBean;
        }
        
        /** returns column count.
         * @return returns column count.
         */
        public int getColumnCount() {
            return visibleColumns.length;
        }
        
        /** returns row count.
         * @return returns row count.
         */
        public int getRowCount() {
            if(vecBudgetDetailBean == null) return 0;
            return vecBudgetDetailBean.size();
        }
        
        /** Returns the value for the cell at column and row.
         * @param row the row whose value is to be queried
         * @param column the column whose value is to be queried
         * @return the value Object at the specified cell
         *
         */
        public Object getValueAt(int row, int column) {
            budgetDetailBean = (BudgetDetailBean)vecBudgetDetailBean.get(row);
            switch (column) {
                case LINE_COLUMN:
                    return new Integer(budgetDetailBean.getLineItemSequence());
                case COST_ELEMENT_COLUMN:
                    return budgetDetailBean.getCostElement();
                case COST_ELEMENT_DESCRIPTION_COLUMN:
                    return budgetDetailBean.getCostElementDescription();
                case DESCRIPTION_COLUMN:
                    return budgetDetailBean.getLineItemDescription();
                case QUANTITY_COLUMN:
//                    return new Integer(budgetDetailBean.getQuantity());
                    return new Double(budgetDetailBean.getQuantity());
                case COST_COLUMN:
                    return new Double(budgetDetailBean.getLineItemCost());
                case START_DATE_COLUMN:
                    return budgetDetailBean.getLineItemStartDate();
                    //return dateUtils.formatDate(budgetDetailBean.getLineItemStartDate().toString(), DATE_FORMAT);
                case END_DATE_COLUMN:
                    return budgetDetailBean.getLineItemEndDate();
                    //return dateUtils.formatDate(budgetDetailBean.getLineItemEndDate().toString(), DATE_FORMAT);
                case CATEGORY_COLUMN:
                    //get From Query Engine
                    try{
                        return getCategory(budgetDetailBean);
                    }catch (CoeusException coeusException) {
                        coeusException.printStackTrace();
                        return EMPTY_STRING;
                    }
                case UNDERRECOVERY_COLUMN:
                    return new Double(budgetDetailBean.getUnderRecoveryAmount());
                case COST_SHARE_COLUMN:
                    return new Double(budgetDetailBean.getCostSharingAmount());
                case CAMPUS_FLAG_COLUMN:
                    if(budgetDetailBean.isOnOffCampusFlag()) {
                        return ON;
                    }else{
                        return OFF;
                    }
            }
            return EMPTY_STRING;
        }
        
        /** Sets the value in the cell at column and row to aValue.
         * @param value the new value.
         * @param row the row whose value is to be changed
         *
         * @param column the column whose value is to be changed
         *
         */
        public void setValueAt(Object value, int row, int column) {
            //System.out.println("Set Value At row : "+row+" column : "+column+" value :"+value);
            budgetDetailBean = (BudgetDetailBean)vecBudgetDetailBean.get(row);
            String strDate;
            
            //Validate before setting value
            switch (column) {
                case START_DATE_COLUMN:
                    Date dt;
                    //Validate for start Date
                    try{
                        strDate = dateUtils.formatDate(value.toString(), DATE_SEPARATERS, DATE_FORMAT);
                        if(strDate == null) throw new CoeusException();
                        
                        //dt = simpleDateFormat.parse(value.toString());
                        dt = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
                        
                        if(dt.compareTo(budgetPeriodBean.getStartDate()) < 0 || dt.compareTo(budgetPeriodBean.getEndDate()) > 0) {
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(START_DATE_BETWEEN_START_END_DATE_OF_PERIOD));
                            return ;
                        }
                        
                        if(dt.compareTo(budgetDetailBean.getLineItemEndDate()) > 0) {
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(START_DATE_LATER_THAN_END_DATE));
                            return ;
                        }
                        if((dt.compareTo(budgetDetailBean.getLineItemStartDate())) == 0) {
                            return;
                        }
                        
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_START_DATE));
                        return ;
                    }catch (CoeusException coeusException) {
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_START_DATE));
                        return ;
                    }
                    //Went thru All Start Date Validation. Good Date.
                    budgetDetailBean.setLineItemStartDate(new java.sql.Date(dt.getTime()));
                    
                    //Added for Case #3121 - start
                    double rate = calculateUnitRates(budgetDetailBean, budgetInfoBean);
                    if(rate != 0.0 && tuitionFeeAutoCalculation) {
                        budgetDetailBean.setLineItemCost(rate);
                    }
                    //Added for Case #3121 - end
                    break;
                case END_DATE_COLUMN:
                    //Validate for End Date
                    try{
                        strDate = dateUtils.formatDate(value.toString(), DATE_SEPARATERS, DATE_FORMAT);
                        if(strDate == null) throw new CoeusException();
                        
                        //dt = simpleDateFormat.parse(value.toString());
                        dt = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
                        
                        if(dt.compareTo(budgetPeriodBean.getStartDate()) < 0 || dt.compareTo(budgetPeriodBean.getEndDate()) > 0) {
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(END_DATE_BETWEEN_START_END_DATE_OF_PERIOD));
                            return ;
                        }
                        
                        if(dt.compareTo(budgetDetailBean.getLineItemStartDate()) < 0) {
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(END_DATE_EARLIER_THAN_STRAT_DATE));
                            return ;
                        }
                        if((dt.compareTo(budgetDetailBean.getLineItemEndDate())) == 0) {
                            return;
                        }
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_END_DATE));
                        return ;
                    }catch (CoeusException coeusException) {
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_END_DATE));
                        return ;
                    }
                    //Went thru All End Date Validation. Good Date.
                    budgetDetailBean.setLineItemEndDate(new java.sql.Date(dt.getTime()));
                    //Added for Case #3121 - start
                    rate = calculateUnitRates(budgetDetailBean, budgetInfoBean);
                    if(rate != 0.0 && tuitionFeeAutoCalculation) {
                        budgetDetailBean.setLineItemCost(rate);
                    }
                    //Added for Case #3121 - end
                    break;
                case COST_ELEMENT_COLUMN:
                    RequesterBean requesterBean = new RequesterBean();
                    if(value == null || value.toString().equals(EMPTY_STRING)) {
                        budgetDetailBean.setCostElement(EMPTY_STRING);
                        budgetDetailBean.setCostElementDescription(EMPTY_STRING);
                        budgetDetailBean.setBudgetCategoryCode(0);
                        budgetDetailBean.setLineItemDescription(EMPTY_STRING);
                        budgetDetailBean.setOnOffCampusFlag(false);
                    }
                    else{
                        //Check if old and modified Cost Elements are same. If Same no need to modify
                        if(budgetDetailBean.getCostElement().equals(value.toString())) return ;
                        requesterBean.setDataObject(value);
                        requesterBean.setFunctionType('F');
                        
                        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET, requesterBean);
                        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET);
                        appletServletCommunicator.setRequest(requesterBean);
                        appletServletCommunicator.send();
                        CostElementsBean costElementsBean = null;
                        
                        ResponderBean responderBean = appletServletCommunicator.getResponse();
                        if(responderBean.isSuccessfulResponse()) {
                            costElementsBean = (CostElementsBean)responderBean.getDataObject();
                            
                            if(costElementsBean == null || costElementsBean.getDescription() == null){
                                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_COST_ELEMENT));
                                return ;
                            }else {
                                budgetDetailBean.setCostElement(value.toString());
                                budgetDetailBean.setCostElementDescription(costElementsBean.getDescription());
                                budgetDetailBean.setBudgetCategoryCode(costElementsBean.getBudgetCategoryCode());
                                budgetDetailBean.setCategoryType(costElementsBean.getCategoryType());
                                // Addded for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting.- Start
                                 try {
                                     budgetDetailBean.setIsFormualtedLineItem(canLineItemFormulated(budgetDetailBean));
                                     // Addded for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting.- End
                                     // Case# 2924 begin
                                     // UT OnOffCampusFlag enhancement - default to the OnOffCampusFlag from the OSP$BUDGET table
                                     
                                     CoeusVector vecBudgetInfo = queryEngine.executeQuery(queryKey, budgetInfoBean);
                                     BudgetInfoBean budgetInfoBean = (BudgetInfoBean)vecBudgetInfo.get(0);
                                     //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - start
                                     if(budgetInfoBean.isDefaultIndicator()){
                                         budgetDetailBean.setOnOffCampusFlag(costElementsBean.isOnOffCampusFlag());
                                     }else{
                                         if(budgetInfoBean.isOnOffCampusFlag())
                                             budgetDetailBean.setOnOffCampusFlag(true);
                                         else
                                             budgetDetailBean.setOnOffCampusFlag(false);
                                     }
                                     //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - end
//                                  budgetDetailBean.setOnOffCampusFlag(budgetInfoBean.isOnOffCampusFlag());
                                     //    budgetDetailBean.setOnOffCampusFlag(costElementsBean.isOnOffCampusFlag());
                                 }catch (CoeusException coeusException) {
                                     coeusException.printStackTrace();
                                 }
                         // end of Case# 2924
                                Equals eqBdgtCatCode = new Equals("code", EMPTY_STRING + costElementsBean.getBudgetCategoryCode());
                                try{
                                CoeusVector  vecBdgtCat = queryEngine.executeQuery(queryKey, BudgetCategoryBean.class, eqBdgtCatCode);
                                if(vecBdgtCat != null && vecBdgtCat.size() > 0) {
                                    BudgetCategoryBean budgetCategoryBean = (BudgetCategoryBean)vecBdgtCat.get(0);
                                    //System.out.println(budgetCategoryBean);
                                    budgetDetailBean.setCategoryType(budgetCategoryBean.getCategoryType());
                                }
                                }catch (CoeusException coeusException) {
                                    coeusException.printStackTrace();
                                }
                                try{
                                    setCalAmounts(budgetDetailBean, value.toString());
                                }catch (CoeusClientException  coeusClientException){
                                    CoeusOptionPane.showDialog(coeusClientException);
                                }
                            }
                            
                        }//End IF Successfull Response
                        else {
                            //Server Error
                        }
                        //Added for Case #3121 - start
                        tuitionFeeBean = calculateLineItemDates(budgetDetailBean, budgetPeriodBean.getStartDate(), budgetPeriodBean.getEndDate());                        
                        
                        // The calculateLineItemDates may add additional BudgetDetails
                        // This would cause the vecBudgetDetailBean to be replaced 
                        // The current budgetDetailBean will not be holding the reference
                        // to the item in the vector any more. So get the bean again.
                        budgetDetailBean = (BudgetDetailBean)vecBudgetDetailBean.get(row);
                        
                        if(tuitionFeeBean != null) {
                            java.sql.Timestamp strTimeStamp = tuitionFeeBean.getStartDate();
                            java.sql.Timestamp endTimeStamp = tuitionFeeBean.getEndDate();
                            if(strTimeStamp != null && endTimeStamp != null) {
                                budgetDetailBean.setLineItemStartDate(new java.sql.Date(strTimeStamp.getTime()));
                                budgetDetailBean.setLineItemEndDate(new java.sql.Date(endTimeStamp.getTime()));
                            }
                        }
                        rate = calculateUnitRates(budgetDetailBean, budgetInfoBean);
                        if(rate != 0.0 && tuitionFeeAutoCalculation) {
                            budgetDetailBean.setLineItemCost(rate);
                        }
                        //Added for Case #3121 - end
                    }
                    break;
                case DESCRIPTION_COLUMN:
                    if(value == null) value = EMPTY_STRING;
                    budgetDetailBean.setLineItemDescription(value.toString());
                    break;
                case QUANTITY_COLUMN:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)) {
                        budgetDetailBean.setQuantity(0.00);
                    }else {
                        //Modified for Case # 3132 - start
                        //Changing quantity field from integer to float
//                        if(budgetDetailBean.getQuantity() == Integer.parseInt(value.toString())) {
//                            return;
//                        }
//                        budgetDetailBean.setQuantity(Integer.parseInt(value.toString()));
                        if(budgetDetailBean.getQuantity() == Double.parseDouble(value.toString())) {
                            return;
                        }
                        budgetDetailBean.setQuantity(Double.parseDouble(value.toString()));
                        //Modified for Case # 3132 - start
                    }
                    //Added for Case #3121 - start
                    rate = calculateUnitRates(budgetDetailBean, budgetInfoBean);
                    if(rate != 0.0 || tuitionFeeAutoCalculation) {
                        budgetDetailBean.setLineItemCost(rate);
                    }
                    //Added for Case #3121 - end                    
              break;
                case COST_COLUMN:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)) {
                        budgetDetailBean.setLineItemCost(0);
                        
                        //Bug Fix : 732 - Starts here
                        budgetDetailBean.setDirectCost(0);
                        //Bug Fix : 732 - Ends here
                    }else {
                        budgetDetailBean.setLineItemCost(Double.parseDouble(value.toString()));
                        
                        //Bug Fix : 732 - Starts here
                        budgetDetailBean.setDirectCost(budgetDetailBean.getLineItemCost());
                        //Bug Fix : 732 - Ends here
                    }
                    break;
            }//End Switch
            
            //Calculate Costs
            calculate();
            
            //Set to Query Engine
            budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
            try{
                queryEngine.set(queryKey, budgetDetailBean);
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
            }
            
            budgetPeriodTableModel.fireTableRowsUpdated(row, row);
        }
        
        /** Returns true if the cell at row and column is editable.
         * Otherwise, setValueAt on the cell will not change the value of that cell.
         * @param row the row whose value to be queried.
         * @param column the column whose value to be queried
         * @return true if the cell is editable.
         * else returns false.
         *
         */
        public boolean isCellEditable(int row, int column) {
            if(getFunctionType() == TypeConstants.DISPLAY_MODE || groupByMode) {
                return false;
            }
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
            // If the line item is from sub award then the row cant be edited
            if(row != -1){
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(row);
                if(budgetDetailBean.getSubAwardNumber() > 0){
                    return false;
                }
            }
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
            if(row != -1  && column != DESCRIPTION_COLUMN) {
                //If contains Personnel Budget Details cannot Edit
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(row);
                
                BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = new BudgetPersonnelDetailsBean();
                budgetPersonnelDetailsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
                budgetPersonnelDetailsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
                budgetPersonnelDetailsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
                budgetPersonnelDetailsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
                try{
                    CoeusVector vecBudgetPersonnelDetailsBean = queryEngine.executeQuery(queryKey, budgetPersonnelDetailsBean);
                    vecBudgetPersonnelDetailsBean = vecBudgetPersonnelDetailsBean.filter(CoeusVector.FILTER_ACTIVE_BEANS);
                    if(vecBudgetPersonnelDetailsBean != null && vecBudgetPersonnelDetailsBean.size() > 0){
                        return false;
                        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
                    }else if(budgetDetailBean != null && budgetDetailBean.isFormualtedLineItem()&&
                            (column == COST_ELEMENT_COLUMN || column == QUANTITY_COLUMN || column == COST_COLUMN)){
                        Operator operator = getPeriodLineItemOperator(budgetDetailBean.getProposalNumber(),
                                budgetDetailBean.getVersionNumber(),budgetDetailBean.getBudgetPeriod(),budgetDetailBean.getLineItemNumber(),true);
                        And andActiveBeans = new And(operator,CoeusVector.FILTER_ACTIVE_BEANS);
                        try {
                            CoeusVector cvFormulatedCost = queryEngine.executeQuery(queryKey, BudgetFormulatedCostDetailsBean.class,andActiveBeans);
                            if(cvFormulatedCost == null || cvFormulatedCost.isEmpty()){
                                return true;
                            }
                        } catch (CoeusException ex) {
                            ex.printStackTrace();
                        }
                        return false;
                    }
                    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
                }catch (CoeusException coeusException) {
                    coeusException.printStackTrace();
                    return false;
                }
                
            }
            //Added for Case #3121 Tuition Fee Auto Calculation - Start
            if(column == COST_COLUMN) {
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(row);
                if(getAutoCalculation(budgetDetailBean)) {
                    double rate = calculateUnitRates(budgetDetailBean, budgetInfoBean);
                    if(rate != 0.0 || tuitionFeeAutoCalculation) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
            //Added for Case #3121 Tuition Fee Auto Calculation - end
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
//            if(row != -1){
//                BudgetDetailBean budgetLineItemDetails = (BudgetDetailBean)vecBudgetLineItem.get(row);
//                if(budgetLineItemDetails != null && budgetLineItemDetails.isFormualtedLineItem()&&
//                        (column == COST_ELEMENT_COLUMN || column == QUANTITY_COLUMN || column == COST_COLUMN)){
//                    Operator operator = getPeriodLineItemOperator(budgetLineItemDetails.getProposalNumber(),
//                            budgetLineItemDetails.getVersionNumber(),budgetLineItemDetails.getBudgetPeriod(),budgetLineItemDetails.getLineItemNumber(),true);
//                    And andActiveBeans = new And(operator,CoeusVector.FILTER_ACTIVE_BEANS);
//                    try {
//                        CoeusVector cvFormulatedCost = queryEngine.executeQuery(queryKey, BudgetFormulatedCostDetailsBean.class,andActiveBeans);
//                        if(cvFormulatedCost == null || cvFormulatedCost.isEmpty()){
//                            return true;
//                        }
//                    } catch (CoeusException ex) {
//                        ex.printStackTrace();
//                    }
//                    return false;
//                }
//                
//            }
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
            return colEditable[column];
        }
        
        /** Returns the name of the column at column.
         * This is used to initialize the table's column header name.
         * @param column the index of the column
         * @return the name of the column
         *
         */
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /** Returns the most specific superclass for all the cell values in the column.
         * This is used by the JTable to set up a default renderer and editor for the column
         * @param column the index of the column
         *
         * @return the common ancestor class of the object values in the model.
         *
         */
        public Class getColumnClass(int column) {
            return colTypes[column];
        }
        
    }//End Class - BudgetPeriodTableModel---------------------------------------
    
    //BudgetPeriodRenderer -----------------------------------------------------
    /** Renderer for the Budget Period Line Items. */
    class BudgetPeriodRenderer extends DefaultTableCellRenderer {
        
        private DollarCurrencyTextField dollarCurrencyTextField;
        
        private Border groupByBorder;
        private Border dollarCurrencyBorder;
        private JLabel lblGroup;
        private JLabel lblValue;
        private JPanel pnlGroup;
        private int lastCatCode = -1;
        private int row;
        private String category = "Category:";
        private String categoryDescription;
        private BudgetDetailBean budgetDetailBean;
        //Added for Case # 3132 - start
        //Changing quantity field from integer to float
        private DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        private CoeusTextField txtQuantity;
//        private CoeusTextField textField;
        //Added for Case # 3132 - end
        
        BudgetPeriodRenderer() {
            dollarCurrencyTextField = new DollarCurrencyTextField();
            dollarCurrencyBorder = new EmptyBorder(0,0,0,0);
            dollarCurrencyTextField.setBorder(dollarCurrencyBorder);
            dollarCurrencyTextField.setHorizontalAlignment(DollarCurrencyTextField.RIGHT);
            
            EmptyBorder emptyBorder = new EmptyBorder(2,2,2,2);
            LineBorder lineBorder = new LineBorder(Color.BLACK, 1);
            groupByBorder = new CompoundBorder(emptyBorder, lineBorder);
            pnlGroup = new JPanel();
            //pnlGroup.setLayout(new BoxLayout(pnlGroup, BoxLayout.Y_AXIS));
            pnlGroup.setLayout(new GridLayout(2, 1));
            lblGroup = new JLabel();
            lblGroup.setFont(CoeusFontFactory.getLabelFont());
            
            lblValue = new JLabel();
            lblValue.setBorder(groupByBorder);
            lblValue.setOpaque(true);
            pnlGroup.add(lblGroup);
            pnlGroup.add(lblValue);
            
            //Added for Case # 3132 - start
            //Changing quantity field from integer to float
            txtQuantity = new CoeusTextField();
            
            decimalFormat.setMinimumIntegerDigits(1);
            decimalFormat.setMaximumIntegerDigits(4);
            
            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setDecimalSeparatorAlwaysShown(true);
            
            CoeusTextField textField = txtQuantity;
            FormattedDocument formattedDocument = new FormattedDocument(decimalFormat, textField);
            formattedDocument.setNegativeAllowed(true);
            textField.setDocument(formattedDocument);
            textField.setBorder(dollarCurrencyBorder);
            textField.setHorizontalAlignment(JLabel.RIGHT);
            textField.setText("0.00");
            //Added for Case # 3132 - end
        }
        
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            setHorizontalAlignment(JLabel.LEFT);
            int actualColumn = visibleColumns[column];
            switch (actualColumn) {
                case QUANTITY_COLUMN:
                    if(value == null || value.toString().equals("0.0")) {
                        value = "0.00";
                    }
//                    textField.setText(value.toString());
                    txtQuantity.setText(value.toString());
                    setHorizontalAlignment(JLabel.RIGHT);
                    if(isSelected){
                        txtQuantity.setBackground(budgetPeriodForm.tblPeriodLineItem.getSelectionBackground());
                        txtQuantity.setForeground(budgetPeriodForm.tblPeriodLineItem.getSelectionForeground());                        
                    } else {
                        txtQuantity.setBackground(budgetPeriodForm.tblPeriodLineItem.getBackground());
                        txtQuantity.setForeground(budgetPeriodForm.tblPeriodLineItem.getForeground());
                    }
                    if(! groupByMode) {
                        return txtQuantity;
                    }
//                    break;
                case UNDERRECOVERY_COLUMN:
                case COST_SHARE_COLUMN:
                case COST_COLUMN:
                    dollarCurrencyTextField.setText(value.toString());
                    if(isSelected){
                        dollarCurrencyTextField.setBackground(budgetPeriodForm.tblPeriodLineItem.getSelectionBackground());
                        dollarCurrencyTextField.setForeground(budgetPeriodForm.tblPeriodLineItem.getSelectionForeground());
                    }
                    else {
                        dollarCurrencyTextField.setBackground(budgetPeriodForm.tblPeriodLineItem.getBackground());
                        dollarCurrencyTextField.setForeground(budgetPeriodForm.tblPeriodLineItem.getForeground());
                    }
                    if(! groupByMode) {
                        return dollarCurrencyTextField;
                    }
                    break;
                case START_DATE_COLUMN:
                case END_DATE_COLUMN:
                    value = dateUtils.formatDate(value.toString(), DATE_FORMAT);
                    break;
            }
            JComponent retComponent;
            retComponent = (JComponent)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            
            //Renderer for group By Mode
            if(groupByMode) {
                if(row > 0) {
                    budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(row - 1);
                    lastCatCode = budgetDetailBean.getBudgetCategoryCode();
                }else {
                    lastCatCode = -1;
                }
                budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(row);
                
                //Checking for Begining of New Category Code.
                if(lastCatCode != budgetDetailBean.getBudgetCategoryCode()) {
                    try{
                        categoryDescription = getCategory(budgetDetailBean);
                    }catch (CoeusException coeusException) {
                        coeusException.printStackTrace();
                    }
                    lblGroup.setText(EMPTY_STRING);
                    actualColumn = visibleColumns[column];
                    switch (actualColumn) {
                        case COST_ELEMENT_COLUMN:
                            lblGroup.setText(category);
                            lblGroup.setForeground(Color.BLACK);
                            break;
                        case COST_ELEMENT_DESCRIPTION_COLUMN:
                            lblGroup.setText(categoryDescription);
                            lblGroup.setForeground(Color.BLUE);
                    }

                    actualColumn = visibleColumns[column];
                    if(actualColumn == COST_COLUMN) {
                        lblValue.setHorizontalAlignment(JLabel.RIGHT);
                        lblValue.setText(dollarCurrencyTextField.getText());
                    }else if(actualColumn == QUANTITY_COLUMN) {
                        lblValue.setHorizontalAlignment(JLabel.RIGHT);
                        lblValue.setText(((JLabel)retComponent).getText());
                    }else{
                        lblValue.setHorizontalAlignment(JLabel.LEFT);
                        lblValue.setText(((JLabel)retComponent).getText());
                    }
                    
                    if(isSelected) {
                        lblValue.setForeground(budgetPeriodForm.tblPeriodLineItem.getSelectionForeground());
                        lblValue.setBackground(budgetPeriodForm.tblPeriodLineItem.getSelectionBackground());
                    }else {
                        lblValue.setForeground(budgetPeriodForm.tblPeriodLineItem.getForeground());
                        lblValue.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("TabbedPane.tabAreaBackground"));
                    }
                    return pnlGroup;
                    
                }//End if check for begining of new Category Code.
                retComponent.setBorder(groupByBorder);
                dollarCurrencyTextField.setBorder(groupByBorder);
            }else {
                dollarCurrencyTextField.setBorder(dollarCurrencyBorder);
            }
            
            //Bug Fix - Cost Element not Displaying $ Sign - Start
            if(actualColumn == COST_COLUMN) {
                return dollarCurrencyTextField;
            }
            //Bug Fix - Cost Element not Displaying $ Sign - End
            
            //Setting Line Item Seq No to Right Align - Start
            if(actualColumn == LINE_COLUMN) {
                ((JLabel)retComponent).setHorizontalAlignment(JLabel.RIGHT);
            }
            //Setting Line Item Seq No to Right Align - End
            
            return retComponent;
        }
        
    }//End Class - BudgetPeriodRenderer ----------------------------------------
    
    //BudgetPeriodEditor--------------------------------------------------------
    class BudgetPeriodEditor extends DefaultCellEditor implements MouseListener,KeyListener {
        
        private int row;
        private JTextField txtCostElement;
        private JTextField txtDescription;
        //Modified for Case # 3132 - start
        //Changing quantity field from integer to float
//        private JTextField txtQuantity;
        private CoeusTextField txtQuantity;
        private DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        //Modified for Case # 3132 - end
        private DollarCurrencyTextField txtCost;
        private JTextField txtDate;
        private int column;
        
        BudgetPeriodEditor() {
            
            super(new JComboBox());
            txtCostElement = new JTextField();
            txtCostElement.setDocument(new LimitedPlainDocument(8));
            
            txtDescription = new JTextField();
            //added for bug fix - Case #2446  - Start
            txtDescription.setDocument(new LimitedPlainDocument(80));
            //added for bug fix - Case #2446  - End        
            
        //Modified for Case # 3132 - start
            //Changing quantity field from integer to float
//            txtQuantity = new JTextField();
//            txtQuantity.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 5));
//            txtQuantity.setHorizontalAlignment(JTextField.RIGHT);
            txtQuantity = new CoeusTextField();
            
            decimalFormat.setMinimumIntegerDigits(1);
            decimalFormat.setMaximumIntegerDigits(4);

            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setDecimalSeparatorAlwaysShown(true);
            
            CoeusTextField textField = txtQuantity;
            FormattedDocument formattedDocument = new FormattedDocument(decimalFormat, textField);
            formattedDocument.setNegativeAllowed(true);
            textField.setDocument(formattedDocument);
            textField.setText("0.00");
            textField.setHorizontalAlignment(JFormattedTextField.RIGHT);
        //Modified for Case # 3132 - end
            
            txtCost = new DollarCurrencyTextField();
            
            txtDate = new JTextField();
            
            registerComponents();
        }
        
        private void registerComponents() {
            txtCostElement.addMouseListener(this);
            txtDescription.addMouseListener(this);
            txtQuantity.addMouseListener(this);
            txtCost.addMouseListener(this);
            txtDate.addMouseListener(this);
            
            txtCostElement.addKeyListener(this);
            txtDescription.addKeyListener(this);
            txtQuantity.addKeyListener(this);
            txtCost.addKeyListener(this);
            txtDate.addKeyListener(this);
            
            
            // Get the focus and add the line item when addLineItem() isinvoked
            budgetPeriodForm.txtCostLimit.addKeyListener(this);
            budgetPeriodForm.txtCostSharing.addKeyListener(this);
            budgetPeriodForm.txtDirectCost.addKeyListener(this);
            budgetPeriodForm.txtIndirectCost.addKeyListener(this);
            budgetPeriodForm.txtTotalCost.addKeyListener(this);
            budgetPeriodForm.txtUnderrecovery.addKeyListener(this);
            budgetPeriodForm.txtEndDate.addKeyListener(this);
            budgetPeriodForm.txtStartDate.addKeyListener(this);
            
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            //for column show/hide and move
            column = visibleColumns[column];
            
            this.column = column;
            this.row = row;
                      
            switch (column) {
                case COST_ELEMENT_COLUMN:
                    txtCostElement.setText(value.toString());
                    return txtCostElement;
                case DESCRIPTION_COLUMN:
                    if(value == null) value = EMPTY_STRING;
                    txtDescription.setText(value.toString());
                    return txtDescription;
                case QUANTITY_COLUMN:
                    if(value.toString().equals("0.0")) {
                        value = "0.00";
                    }
                    txtQuantity.setText(value.toString());
                    return txtQuantity;
                case COST_COLUMN:
                    txtCost.setText(value.toString());
                    if(tuitionFeeAutoCalculation) {
                        txtCost.setEnabled(false);
                    } else {
                        txtCost.setEnabled(true);
                    }
                    return txtCost;
                case START_DATE_COLUMN:
                case END_DATE_COLUMN:
                    txtDate.setText(dateUtils.formatDate(value.toString(), SIMPLE_DATE_FORMAT));
                    return txtDate;
            }
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
        
        public Object getCellEditorValue() {
            switch (column) {
                case COST_ELEMENT_COLUMN:
                    return txtCostElement.getText();
                case DESCRIPTION_COLUMN:
                    return txtDescription.getText();
                case QUANTITY_COLUMN:
                    //Modified for Case # 3132 - start
                    //Changing quantity field from integer to float
//                    return txtQuantity.getText();
                    String quantity = txtQuantity.getText();
                    quantity = quantity.replaceAll(",", "");
                    return new Double(Double.parseDouble(quantity));
                    //Modified for Case # 3132 - end
                case COST_COLUMN:
                    return txtCost.getValue();
                    //return txtCost.getText();
                case START_DATE_COLUMN:
                case END_DATE_COLUMN:
                    return txtDate.getText();
            }
            return super.getCellEditorValue();
        }
        
        public void mouseClicked(MouseEvent mouseEvent) {
            try{
                if(mouseEvent.getClickCount() != 2) return ;
                budgetPeriodEditor.stopCellEditing();

                int row;
                if(mouseEvent.getSource().equals(budgetPeriodForm.tblPeriodLineItem)) {
                    row = budgetPeriodForm.tblPeriodLineItem.rowAtPoint(mouseEvent.getPoint());
                }else {
                    row = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
                }

                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(row);

                if((mouseEvent.getSource().equals(txtCostElement) ||
                budgetPeriodForm.tblPeriodLineItem.columnAtPoint(mouseEvent.getPoint()) == COST_ELEMENT_DESCRIPTION_COLUMN) &&
                (budgetDetailBean.getCostElement() == null || budgetDetailBean.getCostElement().equals(EMPTY_STRING))) {
                    costElementLookup();
                    budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_ELEMENT_COLUMN,COST_ELEMENT_COLUMN);
                    budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(row,row);
                    budgetPeriodForm.tblPeriodLineItem.editCellAt(row,COST_ELEMENT_COLUMN);
                    budgetPeriodForm.tblPeriodLineItem.getEditorComponent().requestFocusInWindow();
                }else{
                    //COEUSQA-3957
                    calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
                    //COEUSQA-3957
                    editLineItemDetails();
                    budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(1,1);
                    budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(row,row);
                    budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_ELEMENT_COLUMN,COST_ELEMENT_COLUMN);
                    budgetPeriodForm.tblPeriodLineItem.editCellAt(row,COST_ELEMENT_COLUMN);
                    if(budgetPeriodForm.tblPeriodLineItem.getEditorComponent()!= null){
                        budgetPeriodForm.tblPeriodLineItem.getEditorComponent().requestFocusInWindow();
                    }
                }
            }catch (CoeusClientException coeusClientException){
                CoeusOptionPane.showDialog(coeusClientException);
            }catch (CoeusException ex) {
                 CoeusOptionPane.showDialog(new CoeusClientException(ex));
            }
            
        }
        
        public void mouseEntered(MouseEvent mouseEvent) {
        }
        
        public void mouseExited(MouseEvent mouseEvent) {
        }
        // Implemented to bring the focus on the editable row and column.
        // start
        public void mousePressed(MouseEvent mouseEvent) {
            int selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
            int selCol = budgetPeriodForm.tblPeriodLineItem.getSelectedColumn();
            if(selectedRow!= -1){
                if(selCol==LINE_COLUMN || selCol==COST_ELEMENT_DESCRIPTION_COLUMN
                ||selCol==CATEGORY_COLUMN ||selCol==UNDERRECOVERY_COLUMN
                || selCol==COST_SHARE_COLUMN){
                    budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(1,1);
                    budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
                    budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_ELEMENT_COLUMN,COST_ELEMENT_COLUMN);
                    budgetPeriodForm.tblPeriodLineItem.editCellAt(selectedRow,COST_ELEMENT_COLUMN);
                    if(budgetPeriodForm.tblPeriodLineItem.getEditorComponent() != null){
                        budgetPeriodForm.tblPeriodLineItem.getEditorComponent().requestFocusInWindow();
                    }
                }
            }
        }// End
        
        public void mouseReleased(MouseEvent mouseEvent) {
        }
        
        /** Added this code to Fix #1649 and #1650 - start
         */
        public void keyPressed(KeyEvent keyEvent) {
            int keyCode = keyEvent.getKeyCode();
            int modifiers = keyEvent.getModifiers();
            if(keyCode == KeyEvent.VK_A && modifiers == KeyEvent.CTRL_MASK){
                addActionisPerformed = true;
                addLineItem();
            }
        }        
        
        public void keyReleased(KeyEvent e) {
        }
        
        public void keyTyped(KeyEvent e) {
        }
        
        // bug Fix #1649 and #1650 - End
    }
    //End Class BudgetPeriodEditor----------------------------------------------
    
    //BudgetJustifyTableModel --------------------------------------------------
    class BudgetJustifyTableModel extends AbstractTableModel {
        
        private CoeusVector vecBudgetDetailBean;
        private BudgetDetailBean budgetDetailBean;
        
        public int getColumnCount() {
            return 1;
        }
        
        public Object getValueAt(int row, int column) {
            budgetDetailBean = (BudgetDetailBean)vecBudgetDetailBean.get(row);
            return budgetDetailBean.getBudgetJustification();
        }
        
        //public Class getColumnClass(int column) {
        //    return String.class;
        //}
        
        public boolean isCellEditable(int row, int column) {
            budgetDetailBean = (BudgetDetailBean)vecBudgetDetailBean.get(row);
            if(budgetDetailBean.getBudgetJustification() == null ||
            budgetDetailBean.getBudgetJustification().trim().equals(EMPTY_STRING)) return false;
            return true;
        }
        
        public int getRowCount() {
            if(vecBudgetDetailBean == null || vecBudgetDetailBean.size() < 1) return 0;
            return vecBudgetDetailBean.size();
        }
        
        public void setData(CoeusVector vecBudgetDetailBean) {
            this.vecBudgetDetailBean = vecBudgetDetailBean;
        }
        
    }//End Class - BudgetJustifyTableModel -------------------------------------
    
    //BudgetJustifyRenderer ----------------------------------------------------
    class BudgetJustifyRenderer extends DefaultTableCellRenderer {
        
        private JButton btnJustify;
        private JLabel lblHeader;
        private ImageIcon imgIcnJustified, imgIcnNotJustified;
        
        BudgetJustifyRenderer() {
            btnJustify = new JButton();
            
            imgIcnJustified = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
            imgIcnNotJustified = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NOT_JUSTIFIED));
            
        }
        
        BudgetJustifyRenderer(boolean header) {
            if(header) lblHeader = new JLabel(" ");
            else btnJustify = new JButton();
            
        }
        
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(lblHeader != null) return lblHeader;
            if(value ==null || value.toString().equals(EMPTY_STRING)) {
                btnJustify.setIcon(imgIcnNotJustified);
            }else {
                btnJustify.setIcon(imgIcnJustified);
            }
            return btnJustify;
        }
        
    }//End Class - BudgetJustifyRenderer ---------------------------------------
    
    //BudgetJustifyEditor ------------------------------------------------------
    class BudgetJustifyEditor extends DefaultCellEditor implements ActionListener{
        
        private JButton btnJustify;
        private JustificationForm justificationForm;
        private String justificationText = EMPTY_STRING;
        private ImageIcon imgIcnJustified, imgIcnNotJustified;
        
        BudgetJustifyEditor() {
            super(new JComboBox());
            btnJustify = new JButton();
            btnJustify.addActionListener(this);
            justificationForm = new JustificationForm();
            justificationForm.setEditable(false);
            
            imgIcnJustified = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
            imgIcnNotJustified = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.NOT_JUSTIFIED));
            
        }
        
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
            if(value != null && value.toString().length() > 0) justificationText = value.toString();
            else justificationText =EMPTY_STRING;
            
            if(value ==null || value.toString().equals(EMPTY_STRING)) {
                btnJustify.setIcon(imgIcnNotJustified);
            }else {
                btnJustify.setIcon(imgIcnJustified);
            }
            
            return btnJustify;
        }
        
        public void actionPerformed(ActionEvent actionEvent) {
            int selectedRow = budgetPeriodForm.tblJustify.getSelectedRow();
            BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
            justificationForm.setJustificationText(budgetDetailBean.getBudgetJustification());
            justificationForm.txtArJustification.setCaretPosition(0);
            justificationForm.display();
        }
        
    }//End Class BudgetJustifyEditor -------------------------------------------
    
    //BudgetListSelectionListener-----------------------------------------------
    class BudgetListSelectionListener implements ListSelectionListener{
        private int selectedRow;
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if(listSelectionEvent.getValueIsAdjusting()) return ;

            int rowCount;
            
            budgetPeriodEditor.stopCellEditing();
            ListSelectionModel lsm =(ListSelectionModel)listSelectionEvent.getSource();
            if(lsm.equals(budgetPeriodForm.tblJustify.getSelectionModel())) {
                selectedRow = budgetPeriodForm.tblJustify.getSelectedRow();
                rowCount = budgetPeriodForm.tblPeriodLineItem.getRowCount();
                budgetJustifyEditor.stopCellEditing();
                if(selectedRow == -1 || selectedRow >= rowCount) return ;
                budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(1,1);
                budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow, selectedRow);
            }else if(lsm.equals(budgetPeriodForm.tblPeriodLineItem.getSelectionModel())){
                selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
                rowCount = budgetPeriodForm.tblJustify.getRowCount();
                budgetPeriodEditor.stopCellEditing();
                if(selectedRow == -1 || selectedRow >= rowCount) return ;
                budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(1,1);
                budgetPeriodForm.tblJustify.setRowSelectionInterval(selectedRow, selectedRow);
            }
            if(selectedRow == -1) return ;
            BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
            budgetPeriodForm.pnlLineItemCalculatedAmounts.setFormData(budgetDetailBean);
            
            
            int selectedColumn = budgetPeriodForm.tblPeriodLineItem.getSelectedColumn();
            if(selectedRow == -1 || selectedColumn == -1) return ;
            budgetPeriodForm.tblPeriodLineItem.setSurrendersFocusOnKeystroke(false);
            if(budgetPeriodForm.tblPeriodLineItem.isEditing()) {
                budgetPeriodEditor.stopCellEditing();
            }
            //boolean edit = budgetPeriodForm.tblPeriodLineItem.editCellAt(selectedRow, selectedColumn);
            //System.out.println(edit);
            
            //budgetPeriodForm.tblPeriodLineItem.getEditorComponent().requestFocus();
            
            //KeyEvent ke = new KeyEvent(budgetPeriodForm.tblPeriodLineItem, 1, 0, KeyEvent.VK_F2, KeyEvent.KEY_PRESSED);
                        
            /*budgetPeriodEditor.getTableCellEditorComponent(budgetPeriodForm.tblPeriodLineItem,
                budgetPeriodTableModel.getValueAt(selectedRow, selectedColumn),
                true, selectedRow, selectedColumn).requestFocus();
             */
           
        }
    }//End Class BudgetListSelectionListener------------------------------------
     int row;
    int column;
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
         javax.swing.InputMap im = budgetPeriodForm.tblPeriodLineItem.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
         
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = budgetPeriodForm.tblPeriodLineItem.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                boolean selectionOut = false;
                    if(rowCount<1){
                    columnCount = 0;
                    row = 0;
                    column=0;
                    budgetPeriodForm.txtStartDate.requestFocusInWindow();
                    return ;
               }
                
                while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    
                    if(row==0 && column==1){
                        selectionOut = true;
                        budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(0,0);
                        budgetPeriodForm.txtStartDate.requestFocusInWindow();
                    }
                    
                    if (row == rowCount) {
                        row = 0;
                    }
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                 if(row==0 && column==1){
                        budgetPeriodForm.txtStartDate.requestFocusInWindow();
                    }
                if(!selectionOut)
                    table.changeSelection(row, column, false, false);
            }
        };
        budgetPeriodForm.tblPeriodLineItem.getActionMap().put(im.get(tab), tabAction);
       
        
        // for the shift+tab action
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = budgetPeriodForm.tblPeriodLineItem.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    column -= 1;
                    if (column <= 0) {
                        column = 7;
                        row -=1;
                    }
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    // Back to where we started, get out.
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                table.changeSelection(row, column, false, false);
                
            }
        };
        budgetPeriodForm.tblPeriodLineItem.getActionMap().put(im.get(shiftTab), tabAction1);
    }
    
    /**
     * Getter for property parentProposal.
     * @return Value of property parentProposal.
     */
    public boolean isParentProposal() {
        return parentProposal;
    }    
    
    /**
     * Setter for property parentProposal.
     * @param parentProposal New value of property parentProposal.
     */
    public void setParentProposal(boolean parentProposal) {
        this.parentProposal = parentProposal;
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
   // Added for Bug fix case#2948 - Start 3     
    /**
     * Getter for property SelectedRow.
     * @return Value of property SelectedRow.
     */
    public int getSelectedRow() {
        return budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
    }
    /**
     * Setter for property SelectedRow.
     * @param SelectedRow New value of property SelectedRow.
     */
    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }
    // Added for Bug fix case#2948 - End 3        
    
    //Case ID#3121 Tuition Fee Auto Calculation - Start
    /**
     * Is used to get the tuition fee periods for a line item.
     * @param BudgetDetailBean, BudgetPeriodBean
     * @return CoeusVector with the required tuition fee detail like start date, end date of CE
     */
    // Changed the method signature for case# 3121
    // parameter budgetDetailBean is not used in the method
    public void getTuitionFeePeriod(Date strDate, Date endDate) {
        //public void getTuitionFeePeriod(BudgetDetailBean budgetDetailBean, Date strDate, Date endDate) {

        if(cvRatesData == null || cvRatesData.size() == 0) {
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(GET_COST_ELEMENT_PERIOD);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET, requesterBean);
            appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responder = appletServletCommunicator.getResponse();
            try {
                if(responder.hasResponse()) {
                    cvRatesData = (CoeusVector) responder.getDataObject();
                }
            }catch(CoeusException ce) {
                ce.printStackTrace();
            }
        }
    }
    
    /**
     * Is used to calculate the start end dates of the line item
     * @param CoeusVector
     * @param StartDate
     * @param EndDate
     * @return tuitionfeebean
     */
    public TuitionFeeBean calculateLineItemDates(BudgetDetailBean budgetDetailBean, Date startDate, Date endDate) {
        TuitionFeeBean tuitionFeeBean = null;
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
        String costElement = budgetDetailBean.getCostElement();
        if(cvRatesData == null || cvRatesData.size() == 0) {
            //Modified for case# 3121 - Tuition fee calcuation - start
            //method signature is changed
            //getTuitionFeePeriod(budgetDetailBean, budgetDetailBean.getLineItemStartDate(), budgetDetailBean.getLineItemEndDate());
            getTuitionFeePeriod(budgetDetailBean.getLineItemStartDate(), budgetDetailBean.getLineItemEndDate());
            //Modified for case# 3121 - Tuition fee calcuation - end
        }
        Equals eqCostElement = new Equals("costElement", costElement);
        CoeusVector cvData = cvRatesData.filter(eqCostElement);
        if(cvData != null && cvData.size() > 0) {
            tuitionFeeBean = (TuitionFeeBean) cvData.get(0);
            int numMonths = tuitionFeeBean.getNumOfMonths();
            int startingMonth = tuitionFeeBean.getStartingMonth();
            
            Period budgetPeriod = new Period(startDate, endDate);
            BudgetPeriodCalculator budgetPeriodCalculator = new BudgetPeriodCalculator();
            //Modified for case# 3121 - To change the period calculation to extend
            //the entire budget period - start
//            Vector vecLineItem = budgetPeriodCalculator.splitLineItemPeriods(
//                    budgetPeriod, startingMonth, numMonths);
            Vector vecLineItem = budgetPeriodCalculator.splitLineItemPeriods(
                startDate, endDate, startingMonth, numMonths);
            //Modified for case# 3121 - To change the period calculation to extend
            //the entire budget period - end
            if(vecLineItem == null || vecLineItem.size() == 0) {
                tuitionFeeBean.setStartDate(new java.sql.Timestamp(startDate.getTime()));
                tuitionFeeBean.setEndDate(new java.sql.Timestamp(endDate.getTime()));
            } else {
                for(int index = 1; index < vecLineItem.size(); index++) {
                    Period lineItem = (Period) vecLineItem.get(index);
                    tuitionFeeBean.setStartDate(new java.sql.Timestamp(lineItem.getStartDate().getTime()));
                    tuitionFeeBean.setEndDate(new java.sql.Timestamp(lineItem.getEndDate().getTime()));
                    addLineItem(tuitionFeeBean);
                }
                Period lineItem = (Period) vecLineItem.get(0);
                tuitionFeeBean.setStartDate(new java.sql.Timestamp(lineItem.getStartDate().getTime()));
                tuitionFeeBean.setEndDate(new java.sql.Timestamp(lineItem.getEndDate().getTime()));
            }
        } else {
            tuitionFeeBean = new TuitionFeeBean();
            tuitionFeeBean.setStartDate(new java.sql.Timestamp(startDate.getTime()));
            tuitionFeeBean.setEndDate(new java.sql.Timestamp(endDate.getTime()));
        }
        return tuitionFeeBean;
    }
    /**
     * Is used to get all the valid cost element rates.
     * @param BudgetDetailBean, BudgetInfoBean, CoeusVector
     * @return TuitionFeeBean
     */
    public double calculateUnitRates(BudgetDetailBean budgetDetailBean, BudgetInfoBean budgetInfoBean) {
        CoeusVector cvUnitRates = null;
        double rate = 0.0;
        String[] category = null;
        boolean autoCalculation = false;
        if(cvRatesData == null || cvRatesData.size() == 0) {
            //Modified for case# 3121 - Tuition fee calcuation - start
            //method signature changed
            //getTuitionFeePeriod(budgetDetailBean, budgetDetailBean.getLineItemStartDate(), budgetDetailBean.getLineItemEndDate());
            getTuitionFeePeriod(budgetDetailBean.getLineItemStartDate(), budgetDetailBean.getLineItemEndDate());
            //Modified for case# 3121 - Tuition fee calcuation - end
        }
        if(cvRatesData != null && cvRatesData.size() > 0) {
            TuitionFeeBean tuitionFeeBean = (TuitionFeeBean) cvRatesData.get(0);
            if(tuitionFeeBean.getAutoCalculation()) {
                String categoryCode = tuitionFeeBean.getCategoryCode();
                if(categoryCode != null) {
                    category = categoryCode.split(",");
                }
            }
            if(category != null && category.length > 0) {
                for(int index = 0; index < category.length; index++) {
                    if(Integer.parseInt(category[index])== budgetDetailBean.getBudgetCategoryCode()) {
                        autoCalculation = true;
                        break;
                    }
                }
            }
        }
        if(autoCalculation) {
            RequesterBean requesterBean = new RequesterBean();
            
//            int qty = budgetDetailBean.getQuantity();
            double qty = budgetDetailBean.getQuantity();
            budgetDetailBean.setQuantity(1);
            Vector vecData = new Vector();
            vecData.addElement(budgetDetailBean);
            vecData.addElement(budgetInfoBean);
            requesterBean.setDataObject(vecData);
            requesterBean.setFunctionType(GET_COST_ELEMENT_RATE);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET, requesterBean);
            appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responder = appletServletCommunicator.getResponse();
            try {
                if(responder.hasResponse()) {
                    cvUnitRates = (CoeusVector) responder.getDataObject();
                }
            }catch(CoeusException ce) {
                ce.printStackTrace();
            } finally {
                budgetDetailBean.setQuantity(qty);
            }
            if(cvUnitRates != null && cvUnitRates.size() > 0) {
                rate = Double.parseDouble((String)cvUnitRates.get(0));
            }
            if(!(rate == 0.0)) {
                tuitionFeeAutoCalculation = true;
                rate = rate * qty;
            } else {
                tuitionFeeAutoCalculation = false;
            }
        } else {
            tuitionFeeAutoCalculation = false;
        }
        return rate;
    }
    
    public boolean getAutoCalculation(BudgetDetailBean budgetDetailBean) {
        boolean autoCalculation = false;
        String[] category = null;
        if(cvRatesData == null || cvRatesData.size() == 0) {
            //Modified for case# 3121 - Tuition fee calcuation - start
            //getTuitionFeePeriod(budgetDetailBean, budgetDetailBean.getLineItemStartDate(), budgetDetailBean.getLineItemEndDate());
            getTuitionFeePeriod(budgetDetailBean.getLineItemStartDate(), budgetDetailBean.getLineItemEndDate());
            //Modified for case# 3121 - Tuition fee calcuation - end
        }
        if(cvRatesData != null && cvRatesData.size() > 0) {
            TuitionFeeBean tuitionFeeBean = (TuitionFeeBean) cvRatesData.get(0);
            if(tuitionFeeBean.getAutoCalculation()) {
                String categoryCode = tuitionFeeBean.getCategoryCode();
                category = categoryCode.split(",");
            }
            if(category != null && category.length > 0) {
                for(int index = 0; index < category.length; index++) {
                    if(Integer.parseInt(category[index])== budgetDetailBean.getBudgetCategoryCode()) {
                        autoCalculation = true;
                        tuitionFeeAutoCalculation = true;
                        break;
                    }//Added COEUSDEV-264 : PD Budget gets "stuck" sometimes - Start
                    else{
                        autoCalculation = false;
                        tuitionFeeAutoCalculation = false;
                    }
                    //COEUSDEV-264 : End 
                }
            }
        }
        return autoCalculation;
    }
    
    public boolean getTuitionFeeAutoCalculation() {
        return tuitionFeeAutoCalculation;
    }
    //Case ID#3121 - End
    /** adds a line item to the end of the period line item table. */
    public void addLineItem(TuitionFeeBean tuitionFeeBean) {
        //System.out.println("Adding Line Item for Budget Period "+budgetPeriodBean.getBudgetPeriod());
        //budgetPeriodEditor.stopCellEditing();
        
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) return ;
        
        //Check if the display is in Groupby Mode
        if(groupByMode) return ;
        
        int selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
        
        BudgetDetailBean budgetCategoryBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
        int categoryCode = budgetCategoryBean.getBudgetCategoryCode();
        char categoryType = budgetCategoryBean.getCategoryType();
        boolean applyInRate = budgetCategoryBean.isApplyInRateFlag();
        //Added for case#3121 -Tuition Fee calculation- start
        //To set the OnOff Campus flag for automatically created line items
        boolean onOffCampusFlag =  budgetCategoryBean.isOnOffCampusFlag();
        //Added for case#3121 -Tuition Fee calculation- end
        
        BudgetDetailBean budgetDetailBean = new BudgetDetailBean();
        budgetDetailBean.setProposalNumber(budgetBean.getProposalNumber());
        budgetDetailBean.setVersionNumber(budgetBean.getVersionNumber());
        budgetDetailBean.setBudgetPeriod(budgetPeriodBean.getBudgetPeriod());
        budgetDetailBean.setLineItemNumber(getMaxLineItemNumber() + 1);
        budgetDetailBean.setBasedOnLineItem(0);
        budgetDetailBean.setLineItemSequence(selectedRow + 1);
        budgetDetailBean.setLineItemStartDate(new java.sql.Date(tuitionFeeBean.getStartDate().getTime()));
        budgetDetailBean.setLineItemEndDate(new java.sql.Date(tuitionFeeBean.getEndDate().getTime()));
        budgetDetailBean.setQuantity(0);
        budgetDetailBean.setCostElement(tuitionFeeBean.getCostElement());
        budgetDetailBean.setCostElementDescription(tuitionFeeBean.getDescription());
        budgetDetailBean.setApplyInRateFlag(applyInRate);
        budgetDetailBean.setLineItemCost(0);
        budgetDetailBean.setCostSharingAmount(0);
        budgetDetailBean.setBudgetCategoryCode(categoryCode);
        budgetDetailBean.setCategoryType(categoryType);
        budgetDetailBean.setAcType(TypeConstants.INSERT_RECORD);
        //Added for case#3121 - Tuition Fee calculation - start
        //To set the OnOff Campus flag for automatically created line items
        budgetDetailBean.setOnOffCampusFlag(onOffCampusFlag);
        //Added for case#3121 -Tuition Fee calculation- end
        //COEUSQA-1693 - Cost Sharing Submission - start
        budgetDetailBean.setSubmitCostSharingFlag(true);
        //COEUSQA-1693 - Cost Sharing Submission - end
        vecBudgetLineItem.add(selectedRow + 1, budgetDetailBean);
         //Added for case# 3121- Amounts not calculated for second line item - start
        try{
            setCalAmounts(budgetDetailBean, tuitionFeeBean.getCostElement());
        }catch (CoeusClientException  coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }
        //Added for case# 3121- Amounts not calculated for second line item - end
        budgetPeriodTableModel.fireTableRowsInserted(selectedRow + 1, selectedRow + 1);
        budgetJustifyTableModel.fireTableRowsInserted(selectedRow + 1, selectedRow + 1);
        
        budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
        budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_ELEMENT_COLUMN,COST_ELEMENT_COLUMN);
        
        queryEngine.insert(queryKey, budgetDetailBean);
       
        try{
            for(int index = selectedRow + 1; index < vecBudgetLineItem.size(); index++) {
                budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(index);
                budgetDetailBean.setLineItemSequence(budgetDetailBean.getLineItemSequence() + 1);
                budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                queryEngine.update(queryKey, budgetDetailBean);
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
       budgetPeriodTableModel.fireTableRowsUpdated(selectedRow + 1, vecBudgetLineItem.size());
       budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                budgetPeriodForm.tblPeriodLineItem.getCellRect(
                selectedRow,0, true));
       budgetPeriodForm.tblPeriodLineItem.editCellAt(selectedRow, COST_ELEMENT_COLUMN);
       if(budgetPeriodForm.tblPeriodLineItem.getEditorComponent()!= null){
        budgetPeriodForm.tblPeriodLineItem.getEditorComponent().requestFocusInWindow();
       }
    }
    
    /*
     * Code added for Case#3472 - Sync to Direct Cost Limit
     * This method is used to sync the period direct cost with the toatl direct cost limit
     */
    public void syncToDirectCostLimit() {
        //if the display is in Group by mode do not perform this function
        if(groupByMode) {
            return ;
        }
        
        budgetPeriodBean.setTotalDirectCostLimit(Double.parseDouble(budgetPeriodForm.txtDirectCostLimit.getValue()));
        
        int selectedRow;
        selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
        //If no line item is selected, disp msg "Please select a line item."
        if(selectedRow < 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    SELECT_LINE_ITEM));
            return ;
        }
        
        //If cost element is null disp msg "Cannot perform this operation if cost element is not present."
        if(budgetPeriodTableModel.getValueAt(selectedRow, COST_ELEMENT_COLUMN) == null ||
                budgetPeriodTableModel.getValueAt(selectedRow, COST_ELEMENT_COLUMN).equals(EMPTY_STRING)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    COST_ELEMENT_NOT_PRESENT));
            return ;
        }
        
        BudgetDetailBean budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
        // Added for COEUSQA-3806 : Formulated Cost Line Items should not be eligible for Sync to Direct or Total Cost functionality in Budget - Start
        // If the line item is formualted line item , then sync is not allowed for the line item
        if(budgetDetailBean.isFormualtedLineItem()){
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(FORMULATED_COST_LINE_ITEM_SYNC_COST_LIMIT));
            return;
        }
        // Added for COEUSQA-3806 : Formulated Cost Line Items should not be eligible for Sync to Direct or Total Cost functionality in Budget - End
        Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetDetailBean.getBudgetPeriod()));
        Equals eqLINumber = new Equals("lineItemNumber", new Integer(budgetDetailBean.getLineItemNumber()));
        And eqBudgetPeriodAndEqLINumber = new And(eqBudgetPeriod, eqLINumber);
        CoeusVector vecPersonnel = null;
        try{
            
            vecPersonnel= queryEngine.executeQuery(queryKey, BudgetPersonnelDetailsBean.class, eqBudgetPeriodAndEqLINumber);
            
            //if person_details_flag is "Y", disp msg "Cannot perform this operation on a line item with personel budget details."
            if(vecPersonnel != null && vecPersonnel.size() > 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                        CANNOT_PERFORM_THIS_OPERATION_ON_PERSONNEL_LINE_ITEM));
                return ;
            }
            
            //if cost_limit is 0 disp msg "Cost limit for this period is set to 0. Cannot sync a line item cost to zero limit."
            if(budgetPeriodBean.getTotalDirectCostLimit() == 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                        CANNOT_SYNC_TO_ZERO_DIRECT_COST_LIMIT));
                if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
                    budgetPeriodEditor.stopCellEditing();
                    budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
                    budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
                    budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                            budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
                }
                return ;
            }
            
            saveFormData();
            calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
            setRefreshRequired(true);
            refresh();
            if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
                budgetPeriodEditor.stopCellEditing();
                budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
                budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
                budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                        budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
            }
            budgetDetailBean = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
            
            //If total_direct_cost equals total_direct_cost_limit, disp msg "Direct cost and total direct cost limit for this period is already in sync."
            double totalDirectCost = budgetPeriodBean.getTotalDirectCost();
            double directCostLimit = budgetPeriodBean.getTotalDirectCostLimit();
            
            if(totalDirectCost == directCostLimit) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    TOTAL_DIRECT_COST_ALREADY_IN_SYNC));
                if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
                    budgetPeriodEditor.stopCellEditing();
                    budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
                    budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
                    budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                            budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
                }
                return ;
            }
            
            //If total_direct_cost > direct_cost_limit disp msg "Period direct cost is greater than the direct cost limit for this period.Do you want to reduce this line item cost to make the direct cost same as direct cost limit"
            if(totalDirectCost > directCostLimit) {
                int selection = CoeusOptionPane.showQuestionDialog((coeusMessageResources.parseMessageKey(
                        REDUCE_LINE_ITEM_COST_TO_DIRECT_COST)), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_NO) {
                    if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
                        budgetPeriodEditor.stopCellEditing();
                        budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
                        budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
                        budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                                budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
                    }
                    return ;
                }
            }
            
            //Continuing with Sync
            //Set the Difference as TotalDirectCostLimit minus DirectCost.
            double difference = directCostLimit - totalDirectCost;
            double lineItemCost = budgetDetailBean.getLineItemCost();
            double multifactor;
            
            //If line_item_cost is 0 then set the value of line_item_cost in line_items to 10000.
            if(lineItemCost == 0) {
                budgetDetailBean.setLineItemCost(10000);
                budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                queryEngine.update(queryKey, budgetDetailBean);
                budgetPeriodTableModel.fireTableCellUpdated(selectedRow, COST_COLUMN);
            }
            
            calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
            
            CoeusVector vecCalAmts = queryEngine.executeQuery(queryKey, BudgetDetailCalAmountsBean.class, eqBudgetPeriodAndEqLINumber);
            
            double totalCost = budgetDetailBean.getLineItemCost() +
                    vecCalAmts.sum("calculatedCost", new NotEquals("rateClassType",RateClassTypeConstants.OVERHEAD));
            //If the lineItemCost <> 0, set multfactor to TotalCost divided by lineItemCost otherwise multfactor is TotalCost divided by 10000
            if(lineItemCost != 0) {
                multifactor = totalCost / lineItemCost;
            }else {
                multifactor = totalCost / 10000;
                budgetDetailBean.setLineItemCost(0);
                budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                queryEngine.update(queryKey, budgetDetailBean);
                calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
                totalCost = 0;
            }
            
            if((totalCost + difference) < 0) {
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(INSUFFICIENT_AMOUNT_TO_SYNC_DIRECT_COST));
                if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
                    budgetPeriodEditor.stopCellEditing();
                    budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
                    budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
                    budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                            budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
                }
                return ;
            }
            
            //Set New Cost
            double newCost = lineItemCost + (difference / multifactor);
            budgetDetailBean.setLineItemCost(newCost);
            
            budgetDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
            queryEngine.update(queryKey, budgetDetailBean);
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        calculate(queryKey, budgetPeriodBean.getBudgetPeriod());
        setRefreshRequired(true);
        refresh();
        if(budgetPeriodForm.tblPeriodLineItem.getRowCount() > 0){
            budgetPeriodEditor.stopCellEditing();
            budgetPeriodForm.tblPeriodLineItem.setRowSelectionInterval(selectedRow,selectedRow);
            budgetPeriodForm.tblPeriodLineItem.setColumnSelectionInterval(COST_COLUMN, COST_COLUMN);
            budgetPeriodForm.tblPeriodLineItem.scrollRectToVisible(
                    budgetPeriodForm.tblPeriodLineItem.getCellRect(selectedRow ,COST_COLUMN, true));
        }
    }    
    
    // Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
    /**
     * This method is used to get the Year value on the Date.
     * @param Date StartDate
     * @return int YEAR value
     */
    private int getPeriodYear(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        return calendar.get(Calendar.YEAR);
    }
     // Modified for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window 
    /**
     * This method is to get valid Person start date or end date based on Period start and end date.
     * @param periodStartDate
     * @param periodEndDate
     * @param startDate
     * @param endDate
     * @param personOldStartDate
     * @param personOldEndDate
     * @param previousPeriodStartDate
     * @param previousPeriodEndDate
     * @return Date (valid start / end Date)
     */
    private Date getValidPersonDateforPeriod(Date periodStartDate, Date periodEndDate, Date startDate, Date endDate,
            Date personOldStartDate,Date personOldEndDate,Date previousPeriodStartDate,
            Date previousPeriodEndDate, Date currentPersonStartDate){
        Vector vecPossibleYears = new Vector();
        Vector vecPersonYear = new Vector();
        Date finalDate = new Date();
        // get period start date month and year
        Calendar startDatecalendar = Calendar.getInstance();
        startDatecalendar.setTime(periodStartDate);
        int periodStartDateMonth = startDatecalendar.get(Calendar.MONTH);
        int periodStartDateYear = startDatecalendar.get(Calendar.YEAR);
        
        // get period end date month and year
        Calendar endDatecalendar = Calendar.getInstance();
        endDatecalendar.setTime(periodEndDate);
        int periodEndDateMonth = endDatecalendar.get(Calendar.MONTH);
        int periodEndDateYear = endDatecalendar.get(Calendar.YEAR);
        
        //Modified for COEUSQA-3422 COEUSQA-3422 Budget Persons Line Item Details END DATES match Budget Period END DATES in
        //Leap Years using February -Start
        Calendar personStrDatecalendar = Calendar.getInstance();
        personStrDatecalendar.setTime(personOldStartDate);
        int personOldStartDateYear = personStrDatecalendar.get(Calendar.YEAR);
        
        Calendar personEndDatecalendar = Calendar.getInstance();
        personEndDatecalendar.setTime(personOldEndDate);
        int personOldEndDateYear = personEndDatecalendar.get(Calendar.YEAR);
        //Modified for COEUSQA-3422 -End
        
        int personStartDateMonth = 0;
        int personStartDateYear = 0;
        if(startDate != null){
            Calendar personStartDatecalendar = Calendar.getInstance();
            personStartDatecalendar.setTime(startDate);
            personStartDateMonth = personStartDatecalendar.get(Calendar.MONTH);
            personStartDateYear = personStartDatecalendar.get(Calendar.YEAR);
            finalDate = startDate;
        }
        
        int personEndDateMonth = 0;
        int personEndDateYear = 0;
        // Added for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
        // Compare previous period end data and previous period person date are same, if both are same assign period end date as person end date.
        if(endDate != null){
            if(previousPeriodEndDate.compareTo(personOldEndDate) == 0){
                endDate = periodEndDate;
            }
            // Added for COEUSQA-3038 - End  
//            Calendar personEndDatecalendar = Calendar.getInstance();
//            personEndDatecalendar.setTime(endDate);
//            personEndDateMonth = personEndDatecalendar.get(Calendar.MONTH);
//            personEndDateYear = personEndDatecalendar.get(Calendar.YEAR);
            finalDate = endDate;
        }
        
        for(int endYear = periodStartDateYear+1; endYear<= periodEndDateYear; endYear++){
            vecPossibleYears.addElement(new Integer(endYear));
        }
        if(vecPossibleYears !=null && vecPossibleYears.size() > 0){
            //If Person Starting Month is with in that period staring year, apply period start year as Person year.
            if( startDate != null && personStartDateMonth >= periodStartDateMonth && personStartDateMonth < 12 ){
                vecPersonYear.add(new Integer(periodStartDateYear));
            }else if( startDate != null){
                vecPersonYear.add(vecPossibleYears.get(0));
            }
            
            //Modified for COEUSQA-3422 Budget Persons Line Item Details END DATES match Budget Period END DATES in
            //Leap Years using February - Start
            //To find valid EndDate, if person pervious Start Date and End Date are falls on the same year,
            // future endDate also has to fall on furture person startDate year.
            if(endDate != null &&  personOldStartDateYear == personOldEndDateYear){
                Calendar personCurrentStartDatecalendar = Calendar.getInstance();
                personCurrentStartDatecalendar.setTime(currentPersonStartDate);
                int personCurrentStartDateYear = personCurrentStartDatecalendar.get(Calendar.YEAR);
                vecPersonYear.add(new Integer(personCurrentStartDateYear));                
            }else if (endDate != null){
                vecPersonYear.add(new Integer(getValidPeriodEndYear(vecPossibleYears, periodStartDate, periodEndDate, personOldStartDate, personOldEndDate)));
            }
            ////Modified for COEUSQA-3422 -End
                      
        } // If it is a single year period.
        else{
            vecPersonYear.add(new Integer(periodStartDateYear));
        }
        
        Calendar calendar = Calendar.getInstance();
        int year = 0;
        String DATE_FORMAT = "yyyy-MM-dd";
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat(DATE_FORMAT);
        if(vecPersonYear !=null && vecPersonYear.size() > 0){
            year = ((Integer) vecPersonYear.get(0)).intValue();
        }
        
         //Modified for COEUSQA-3422 Budget Persons Line Item Details END DATES match Budget Period END DATES in
        //Leap Years using February - Start
        calendar.setTime(finalDate);
        calendar.set(Calendar.YEAR, year);        
        finalDate = calendar.getTime();
        //Modified for COEUSQA-3422 -End
        
        // Added for COEUSDEV-794 : End of the month period date not recognized when the first period is on a Leap year -Start
       // Check whether the person Date is come under leap year, if it is leap year reduce one day.
        if(isDateLeapYear(finalDate)){
            finalDate = adjustDateForLeapYear(finalDate);
         }         
         // Added for COEUSDEV-794 -End        
        
        
        // Added for COEUSDEV-309  Adjust period boundaries and generate all periods is not copiying personnel items correctly -start
        // If it is a single year period, check if person end is beyond the period end date, Apply Period End Date as Person end date.
         if(vecPossibleYears == null || vecPossibleYears.isEmpty() && endDate != null){
            
            if(periodStartDate.compareTo(finalDate) > 0 && periodEndDate.compareTo(finalDate) > 0) {
                 calendar.setTime(periodEndDate);
                 calendar.set(Calendar.YEAR, year);
                 finalDate = calendar.getTime();
            }
        }
        
//        return calendar.getTime();
        return finalDate;
        // Added for case COEUSDEV-309 -end
    }
    
    /*
     * This method is used to check the Valid Period year based on period end date.
     */
    
    private int getValidPeriodEndYear(Vector vecPossibleYears, Date periodStartDate, Date periodEndDate, Date personOldStartDate, Date personOldEndDate){
        int validPersonStartYear = 0;
        int validPersonEndYear = 0;
        HashMap hmValidEndYear = new HashMap();
        
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTime(periodStartDate);
        int periodStartDateMonth = startDateCalendar.get(Calendar.MONTH);
        int periodStartDateYear = startDateCalendar.get(Calendar.YEAR);
        
        //Get Period End Date
        Calendar endDatecalendar = Calendar.getInstance();
        endDatecalendar.setTime(periodEndDate);
        int periodEndDateMonth = endDatecalendar.get(Calendar.MONTH);
        int periodEndDateYear = endDatecalendar.get(Calendar.YEAR);
        
        Calendar personStartDtCalendar = Calendar.getInstance();
        personStartDtCalendar.setTime(personOldStartDate);
        int personOldStartDateMonth = personStartDtCalendar.get(Calendar.MONTH);
        int personOldStartDateYear = personStartDtCalendar.get(Calendar.YEAR);
        
        Calendar personEndDtCalendar = Calendar.getInstance();
        personEndDtCalendar.setTime(personOldEndDate);
        int personOldEndDateMonth = personEndDtCalendar.get(Calendar.MONTH);
        int personOldEndDateYear = personEndDtCalendar.get(Calendar.YEAR);
        
        
        //If Person Starting Month is with in that period staring year, apply period start year as Person year.
        if( periodStartDate != null && personOldStartDateMonth >= periodStartDateMonth &&  periodStartDateMonth < 12 ){
            validPersonStartYear = periodStartDateYear;
        }else{    // Person Starting Month is not with Period Start year, need to check in the next year.
            if (vecPossibleYears != null && vecPossibleYears.size() > 0) {
                validPersonStartYear = ((Integer)vecPossibleYears.get(0)).intValue();
            }
        }
        // Calculate the Number of Days difference in all the Possible years.
        if (vecPossibleYears != null && vecPossibleYears.size() > 0) {
            for(int index =0; index < vecPossibleYears.size(); index++){
                int endYear = ((Integer) vecPossibleYears.get(index)).intValue();
                int daysDiff = dtUtils.calculateDateDiff(2, mergePeriodDate(personOldStartDate, validPersonStartYear) , mergePeriodDate(personOldEndDate, endYear));
                hmValidEndYear.put(new Integer(daysDiff), new Integer(endYear));
            }
        }
        //Get the Number of days difference for Orginal Person Start and End date.
        int personNoOfDaysDiff = dtUtils.calculateDateDiff(2, personOldStartDate,personOldEndDate ) ;
        
        // Based on the number of days in the first period, get the exact value in the possible year hashmap.
        if(hmValidEndYear !=null && hmValidEndYear.size() > 0){
            if (hmValidEndYear.get(new Integer(personNoOfDaysDiff)) != null){
                validPersonEndYear = ((Integer)  hmValidEndYear.get(new Integer(personNoOfDaysDiff))).intValue();
            }else{
                // validPersonEndYear = periodStartDateYear;
                // Added for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
                validPersonEndYear = periodEndDateYear;
                // Added for COEUSQA-3038 - End

            }
        }else{
            validPersonEndYear = periodStartDateYear;
        }
        
        
        
        return validPersonEndYear;
    }
    
    /**
     * This method is to merge the Year with source date.
     * This is used to merge person date with specified year.
     * @param StartDate
     * @param Year
     * @return Date
     */
    private Date mergePeriodDate(Date startDate, int year){
        Calendar calendar = Calendar.getInstance();
        String DATE_FORMAT = "yyyy-MM-dd";
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat(DATE_FORMAT);
        calendar.setTime(startDate);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }
    
    //3197 - End
    //Added for COEUSDEV-419 / COEUSQA-2402  Prop dev - generate all periods not copying personnel lines when periods > 12 months -Start
    /**
     * This method is to used to calculate the End Date of the person. 
     * If Person Start Date is same as Period Start Date and End Date is different.
     * Then End Date should be calculate End Date = (Start Date + No.of Months ( If less than or equal to period 2 enddate Else apply Period 2 End Date))
     * @param periodStartDate
     * @param personOldStartDate
     * @param personOldEndDate
     * @return Date (valid end Date)
     */
    private Date getPersonEndDateforPeriod(Date periodStartDate, Date personOldStartDate, Date personOldEndDate, Date personNewStartDate){
        
          //Get the Number of days difference for Orginal Person Start and End date.
        int personNoOfDaysDiff = dtUtils.calculateDateDiff(5, personOldStartDate,personOldEndDate ) ;
        
        // Added for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
        // Check the period starting year has next to the Leap Year, if so then reduce one day less.
        //if(isPeriodYearNextToLeapYear(periodStartDate, personNoOfDaysDiff)){
        if(isPeriodYearNextToLeapYear(periodStartDate, personNoOfDaysDiff, personOldEndDate, personOldStartDate)){        
            personNoOfDaysDiff = personNoOfDaysDiff-1;
        }
        //Check the Period Year is Leap Year, If it is Leap Year add one more day to the total person days.
        if(isLeapYearTobeConsider(periodStartDate, personNoOfDaysDiff, personNewStartDate )){        
            personNoOfDaysDiff = personNoOfDaysDiff+1;
        }
        
        // Added for COEUSQA-3038 - End
        
         //Adding the Start Date and No.of person days to the person end date.
         Date perEndDate = dtUtils.dateAdd(Calendar.DATE, periodStartDate, personNoOfDaysDiff);
        return perEndDate;
    }
    //Added for COEUSDEV-419 / COEUSQA-2402  Prop dev - generate all periods not copying personnel lines when periods > 12 months -End
   
    // Added for COEUSQA-3038 -CLONE - Leap Year Personnel Budget Details Window -Start
    /**
     * This method to check whether it is a leap year or not.
     * @param periodStartDate
     * @return boolean (if it is leap year, return true)
     */
    private boolean isLeapYear(Date periodStartDate){
        Calendar prdCalendar = Calendar.getInstance();
        GregorianCalendar cal = new GregorianCalendar();
        boolean isLeapYear = false;
        prdCalendar.setTime(periodStartDate);
        int periodYear = prdCalendar.get(Calendar.YEAR);
        if (cal.isLeapYear(periodYear)){
            isLeapYear = true;
        }
        return isLeapYear;
    }
    
    /**
     * This method is to used to check whether the PersonEndDate has to be consider for the leap year or not.
     * @param periodStartDate
     * @param personNoOfDaysDiff
     * @return boolean (if leap year has to be consider, return true)
     */
    
     private boolean isLeapYearTobeConsider(Date periodStartDate, int personNoOfDaysDiff, Date personNewStartDate){
        Calendar stDateCalendar = Calendar.getInstance();
        Calendar personStartDateCalendar = Calendar.getInstance();
        Date perEndDate = dtUtils.dateAdd(Calendar.DATE, periodStartDate, personNoOfDaysDiff);
        boolean isLeepYearTobeConsider = false;
        //check is period start date come under the leap year or not
        
        stDateCalendar.setTime(perEndDate);
        int perEndDateDay = stDateCalendar.get(Calendar.DAY_OF_MONTH);
        int perEndDateMonth = stDateCalendar.get(Calendar.MONTH);
        
        personStartDateCalendar.setTime(personNewStartDate);
        int perStartDateMonth = personStartDateCalendar.get(Calendar.MONTH);
        
        if(isLeapYear(personNewStartDate)){
            //If PersonStartDate Month is greater than March(2), Leep year should not be consider.
            if(perStartDateMonth <= 1  ){
                isLeepYearTobeConsider = true;
            }else{
              isLeepYearTobeConsider = false;  
            }
            if(isLeapYear(perEndDate)){
                if(perEndDateMonth >= 2 && perStartDateMonth <= 1 ){
                    isLeepYearTobeConsider = true;
                }//If PersonEndDate Month is February(1) and Day is more than 28,then Leep year has to be consider.
                else if(perEndDateMonth == 1 & perEndDateDay > 28){
                    isLeepYearTobeConsider = true;
                }else{
                isLeepYearTobeConsider = false;
            } 
            }           
        }else if(isLeapYear(perEndDate)){
            // If PersonEndDate Month is greater than March(2), Leep year has to be consider.
            if(perEndDateMonth >= 2){
                isLeepYearTobeConsider = true;
            }//If PersonEndDate Month is February(1) and Day is more than 28,then Leep year has to be consider.
            else if(perEndDateMonth == 1 & perEndDateDay > 28){
                isLeepYearTobeConsider = true;
            }
        }
                   

        return isLeepYearTobeConsider;
    }
    
    
    /**
     * This method to check whether this period year is next year to the leap year.
     * @param periodStartDate
     * @param personNoOfDaysDiff
     * @return boolean (if it is next year to leap year, return true)
     */
    private boolean isPeriodYearNextToLeapYear(Date periodStartDate , int personNoOfDaysDiff, Date personOldEndDate, Date personOldStartDate){
        
        Calendar personEndDateCalendar = Calendar.getInstance();
        Calendar personStartDateCalendar = Calendar.getInstance();
        GregorianCalendar cal = new GregorianCalendar();
        Calendar stDateCalendar = Calendar.getInstance();
        boolean isLeapYear = false;
        
        Date perEndDate = dtUtils.dateAdd(Calendar.DATE, periodStartDate, personNoOfDaysDiff);
        
        personEndDateCalendar.setTime(personOldEndDate);
        personStartDateCalendar.setTime(personOldStartDate);
        
        int personPreviousEndYear = personEndDateCalendar.get(Calendar.YEAR);
        int personPreviousEndMonth = personEndDateCalendar.get(Calendar.MONTH);
        int personPreviousEndDay = personEndDateCalendar.get(Calendar.DAY_OF_MONTH);
        
        int personPreviousStartYear = personStartDateCalendar.get(Calendar.YEAR);
        int personPreviousStartMonth = personStartDateCalendar.get(Calendar.MONTH);
        int personPreviousStartDay = personStartDateCalendar.get(Calendar.DAY_OF_MONTH);
        
        stDateCalendar.setTime(perEndDate);
        int perEndDateDay = stDateCalendar.get(Calendar.DAY_OF_MONTH);
        int perEndDateMonth = stDateCalendar.get(Calendar.MONTH);
        
        
        if(isLeapYear(personOldStartDate)){
            //If PersonStartDate Month is greater than March(2), Leep year should not be consider.
            if(personPreviousStartMonth <= 1  ){
                isLeapYear = true;
            }else{
              isLeapYear = false;  
            }
            if(isLeapYear(personOldEndDate)){
                if(personPreviousEndMonth >= 2 && personPreviousStartMonth <= 1){
                    isLeapYear = true;
                }//If PersonEndDate Month is February(1) and Day is more than 28,then Leep year has to be consider.
                else if(personPreviousEndMonth == 1 & personPreviousEndDay > 28){
                    isLeapYear = true;
                }else{
                isLeapYear = false;
            } 
            }           
        }else if(isLeapYear(personOldEndDate)){
            // If PersonEndDate Month is greater than March(2), Leep year has to be consider.
            if(personPreviousEndMonth >= 2){
                isLeapYear = true;
            }//If PersonEndDate Month is February(1) and Day is more than 28,then Leep year has to be consider.
            else if(personPreviousEndMonth == 1 & personPreviousEndDay > 28){
                isLeapYear = true;
            }
        }        
        
  
        return isLeapYear;
    }
    
    
    // Added for COEUSDEV-794 : End of the month period date not recognized when the first period is on a Leap year -Start
   /**
     * This method will check, The person date has to be considered for leap year or not.
     * @param personDate
     * @return boolean 
     */
    private boolean isDateLeapYear(Date personDate){
        Calendar stDateCalendar = Calendar.getInstance();
        boolean isLeepYearTobeConsider = false;
        stDateCalendar.setTime(personDate);
        int personDateDay = stDateCalendar.get(Calendar.DAY_OF_MONTH);
        int personDateMonth = stDateCalendar.get(Calendar.MONTH);
        if(isLeapYear(personDate)){
         //If PersonEndDate Month is February(1) and Day is more than 28,then Leep year has to be consider.
            if(personDateMonth == 1 & personDateDay > 28){
                isLeepYearTobeConsider = true;
            }
        }
        return isLeepYearTobeConsider;
    }
    
    /**
     * This method will reduce one day from the person date
     * @param personDate
     * @return Date 
     */
    private Date adjustDateForLeapYear(Date personDate){
        personDate = dtUtils.dateAdd(Calendar.DATE, personDate, -1);
        return personDate;
    }

    //Added for COEUSDEV-794 - End
    
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    /**
     * Method to check whether the line item can be a formulated one based on the formulated cost details for the line item or
     * based on the parmater where the category code is defined  
     * @param costElement 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return canLineItemFormulated
     */
    private boolean canLineItemFormulated(BudgetDetailBean budgetDetailBean) throws CoeusException{
        boolean canLineItemFormulated = false;
        if(budgetDetailBean.getCategoryType() != PERSONNEL && budgetDetailBean.getSubAwardNumber() == 0){
            String costElement = budgetDetailBean.getCostElement();
            Hashtable htData =  QueryEngine.getInstance().getDataCollection(queryKey);
            if(htData != null){
                CoeusVector cvFormulatedTypes = (CoeusVector) htData.get(CoeusConstants.FORMULATED_TYPES);
                if(cvFormulatedTypes != null && !cvFormulatedTypes.isEmpty()){
                    Integer enableFormulatedCostCalc = (Integer) htData.get(CoeusConstants.ENABLE_FORMULATED_COST_CALC);
                    if(enableFormulatedCostCalc.intValue() == 1){
                        String formulatedCostElement = (String) htData.get(CoeusConstants.FORMULATED_COST_ELEMENTS);
                        String[] formulatedCostElem = null;
                        
                        if(formulatedCostElement != null) {
                            formulatedCostElem = formulatedCostElement.split(",");
                        }
                        
                        if(formulatedCostElem != null && formulatedCostElem.length > 0) {
                            for(int index = 0; index < formulatedCostElem.length; index++) {
                                if(costElement.equals(formulatedCostElem[index].trim())) {
                                    canLineItemFormulated = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(!canLineItemFormulated){
                        int selectedRow = budgetPeriodForm.tblPeriodLineItem.getSelectedRow();
                        BudgetDetailBean budgetDetails = (BudgetDetailBean)vecBudgetLineItem.get(selectedRow);
                        Operator operator = getPeriodLineItemOperator(budgetDetails.getProposalNumber(),
                                budgetDetails.getVersionNumber(),budgetDetails.getBudgetPeriod(),budgetDetails.getLineItemNumber(),true);
                        CoeusVector cvFormulatedCost = QueryEngine.getInstance().executeQuery(queryKey, BudgetFormulatedCostDetailsBean.class, operator);
                        if(cvFormulatedCost != null && !cvFormulatedCost.isEmpty()){
                            canLineItemFormulated = true;
                        }
                    }
                }
            }
        }
        return canLineItemFormulated;
    }
    
    /**
     * Method to get the Operator for proposal budget version period or the period line item based on isLineItemOperator
     * @param proposalNumber 
     * @param versionNumber 
     * @param budgetPeriod 
     * @param lineItemNumber 
     * @param isLineItemOperator 
     * @return operator
     */
    private Operator getPeriodLineItemOperator(String proposalNumber, int versionNumber, int budgetPeriod, int lineItemNumber, boolean isLineItemOperator){
        Operator operator = null;
        Equals eqProposalNumber = new Equals("proposalNumber", proposalNumber);
        Equals eqVersionNumber = new Equals("versionNumber", new Integer(versionNumber));
        Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetPeriod));
        Equals eqLineItemNumber = new Equals("lineItemNumber", new Integer(lineItemNumber));
        And andProposalVersion = new And(eqProposalNumber,eqVersionNumber);
        And andBudgetPeriods = new And(andProposalVersion,eqBudgetPeriod);
        if(isLineItemOperator){
            And andBudgetLineItem = new And(andBudgetPeriods,eqLineItemNumber);
            operator = andBudgetLineItem;
        }else{
            operator = andBudgetPeriods;
        }
        eqProposalNumber = null;
        eqVersionNumber = null;
        eqBudgetPeriod = null;
        eqLineItemNumber = null;
        andProposalVersion = null;
        andBudgetPeriods = null;
        return operator;
    }
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
    
}
