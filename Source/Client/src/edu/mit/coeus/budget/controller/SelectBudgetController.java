/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 04-MAY-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.budget.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.AppointmentsBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetModularBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetPersonSyncBean;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import edu.mit.coeus.budget.gui.CopyBudgetForm;
import edu.mit.coeus.budget.gui.CostElementMessageForm;
import edu.mit.coeus.budget.gui.SelectBudgetForm;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalHierarchyBean;
//import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.propdev.gui.ProposalDetailForm;
import edu.mit.coeus.routing.gui.RoutingValidationChecksForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UserUtils;
import edu.mit.coeus.utils.query.Equals;

/**
 * Controls Select Budget Form. SelectBudgetController.java
 * 
 * @author Vyjayanthi Created on September 29, 2003, 4:58 PM
 */
public class SelectBudgetController extends Controller implements TypeConstants, ActionListener, ListSelectionListener {

	// SelectBudgetHeaderRenderer - Start
	class SelectBudgetHeaderRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

		private JLabel label;

		SelectBudgetHeaderRenderer() {
			label = new JLabel();
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setFont(CoeusFontFactory.getLabelFont());
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			label.setText(value.toString());
			return label;
		}

	}
	// SelectBudgetHeaderRenderer - End

	/** Holds the right id */
	// private String rightId;

	// Select Budget Renderer - Start
	class SelectBudgetRenderer extends DefaultTableCellRenderer {

		SelectBudgetRenderer() {
		}

		@Override
		public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			if (column == 2 || column == 3) {
				setHorizontalAlignment(JLabel.CENTER);
			} else {
				setHorizontalAlignment(JLabel.RIGHT);
			}
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}// Select Budget Renderer - End

	// Inner Class Table Model - Start
	class SelectBudgetTableModel extends AbstractTableModel {

		String colNames[] = { EMPTY_STRING, "Version", "Start Date", "End Date", "Total Cost", "Final" };

		Class[] types = new Class[] { Object.class, Integer.class, String.class, String.class, String.class,
				Boolean.class };

		private Vector dataBean;
		private DateUtils dtUtils;
		private BudgetInfoBean budgetBean = null;

		SelectBudgetTableModel() {
			this.dtUtils = new DateUtils();
		}

		@Override
		public Class getColumnClass(int columnIndex) {
			return types[columnIndex];
		}

		@Override
		public int getColumnCount() {
			return colNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return colNames[column];
		}

		@Override
		public int getRowCount() {
			if (dataBean == null)
				return 0;
			return dataBean.size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			// construct table data from vector of budgetinfo beans
			budgetBean = (BudgetInfoBean) dataBean.get(row);
			String tempData;
			switch (column) {
			case HAND_ICON_COLUMN:
				return EMPTY_STRING;
			case VERSION_COLUMN:
				return new Integer(budgetBean.getVersionNumber());
			case START_DATE_COLUMN:
				return (dtUtils.formatDate(budgetBean.getStartDate().toString(), REQUIRED_DATEFORMAT));
			case END_DATE_COLUMN:
				return (dtUtils.formatDate(budgetBean.getEndDate().toString(), REQUIRED_DATEFORMAT));
			case TOTAL_COST_COLUMN:
				tempData = String.valueOf(budgetBean.getTotalCost());
				dollarData.setText(tempData);
				return dollarData.getText();
			case FINAL_COLUMN:
				return new Boolean(budgetBean.isFinalVersion());
			}
			return EMPTY_STRING;
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (getFunctionType() == DISPLAY_MODE) {
				/* in display mode return all columns as non-editable */
				return false;
			} else {
				if (col == FINAL_COLUMN) {
					// Bug fix case id : 2694 starts
					// boolean value = isHierarchy();
					// if(getFunctionType() != DISPLAY_MODE && value &&
					// isStatusComplete){
					// return false;
					// }else
					// Bug fix case id : 2694 ends
					if (selectBudgetForm.cmbStatus.getSelectedIndex() != STATUS_COMPLETE && hasRights) {
						// final flag column is editable
						return true;
					}
					return false;
				} else {
					// all other colums are not editable
					return false;
				}
			}

		}

		public void setData(Vector dataBean) {
			this.dataBean = dataBean;
		}

		/**
		 * This method sets the selected Final version for the selected row and
		 * resets for all other rows. The value of selected final version label
		 * is also set here.
		 * 
		 * @param set
		 *            takes true or false
		 * @param row
		 *            takes the current row
		 * @param col
		 *            takes the current column
		 */
		private void setResetRelativeFlag(boolean set, int row, int col) {
			newBudgetInfoBean = (BudgetInfoBean) budgetInfo.get(row);
			newBudgetInfoBean.setAcType(UPDATE_RECORD);
			newBudgetInfoBean.setFinalVersion(set);
			newFinalVersion = newBudgetInfoBean.getVersionNumber();
			setSaveRequired(true);
			selectBudgetForm.lblFinalVerValue.setText(String.valueOf(newFinalVersion));
			newFinalVersion = newFinalVersion - 1;
			for (int index = 0; index < budgetInfo.size(); index++) {
				if (index == newFinalVersion)
					continue;
				oldBudgetInfoBean = (BudgetInfoBean) budgetInfo.get(index);
				oldBudgetInfoBean.setFinalVersion(!set);
			}
			fireTableCellUpdated(row, col);
			fireTableDataChanged();
			selectBudgetForm.tblBudgetVersion.setRowSelectionInterval(row, row);
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			int confirm = 0;
			if (col == FINAL_COLUMN) {
				// If setting Final version as true for any row, then reset for
				// all other rows as false
				boolean set = ((Boolean) value).booleanValue();
				if (set) { // for checking
					if (hasFinalVersion || !(selectBudgetForm.lblFinalVerValue.getText().equals(EMPTY_STRING))) {
						message = coeusMessageResources.parseMessageKey(NEW_FINAL_VERSION);
						confirm = CoeusOptionPane.showQuestionDialog(message, CoeusOptionPane.OPTION_YES_NO,
								CoeusOptionPane.DEFAULT_NO);
						switch (confirm) {
						case (JOptionPane.NO_OPTION):
							break;
						case (JOptionPane.YES_OPTION):
							finalVersionModified = true;
							finalVersionChecked = true;
							setResetRelativeFlag(set, row, col);
							break;
						}
						selectBudgetForm.tblBudgetVersion.requestFocusInWindow();
					} else {
						message = coeusMessageResources.parseMessageKey(MODIFY_FINAL_VERSION);
						confirm = CoeusOptionPane.showQuestionDialog(message, CoeusOptionPane.OPTION_YES_NO,
								CoeusOptionPane.DEFAULT_NO);
						switch (confirm) {
						case (JOptionPane.NO_OPTION):
							break;
						case (JOptionPane.YES_OPTION):
							finalVersionModified = true;
							finalVersionChecked = true;
							setResetRelativeFlag(set, row, col);
							break;
						}
						selectBudgetForm.tblBudgetVersion.requestFocusInWindow();
					}
				} else { // for unchecking
					message = coeusMessageResources.parseMessageKey(MODIFY_FINAL_VERSION);
					confirm = CoeusOptionPane.showQuestionDialog(message, CoeusOptionPane.OPTION_YES_NO,
							CoeusOptionPane.DEFAULT_NO);
					switch (confirm) {
					case (JOptionPane.NO_OPTION):
						break;
					case (JOptionPane.YES_OPTION):
						finalVersionModified = true;
						finalVersionChecked = false;
						newBudgetInfoBean = (BudgetInfoBean) budgetInfo.get(row);
						newBudgetInfoBean.setAcType(UPDATE_RECORD);
						newBudgetInfoBean.setFinalVersion(set);
						newFinalVersion = newBudgetInfoBean.getVersionNumber();
						selectBudgetForm.lblFinalVerValue.setText(EMPTY_STRING);
						fireTableCellUpdated(row, col);
						selectBudgetForm.tblBudgetVersion.setRowSelectionInterval(row, row);
						setSaveRequired(true);
						break;
					}
					selectBudgetForm.tblBudgetVersion.requestFocusInWindow();
				}
			}
		}

	}// Inner Class Table Model - END

	// private final String RIGHT_ID = "MODIFY_BUDGET";
	private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL + "/BudgetMaintenanceServlet";

	private static final String EMPTY_STRING = "";

	private static final int HAND_ICON_COLUMN = 0;

	private static final int VERSION_COLUMN = 1;

	private static final int START_DATE_COLUMN = 2;

	private static final int END_DATE_COLUMN = 3;

	private static final int TOTAL_COST_COLUMN = 4;

	private static final int FINAL_COLUMN = 5;

	private static final int STATUS_INCOMPLETE = 0;

	private static final int STATUS_COMPLETE = 1;

	private static final int STATUS_NONE = 2;

	private static final String INCOMPLETE = "I";

	private static final String COMPLETE = "C";

	private static final String NONE = "N";

	private static final String VAL_INCOMPLETE = "Incomplete";

	private static final String VAL_COMPLETE = "Complete";

	private static final String VAL_NONE = "(None)";

	private static final String REQUIRED_DATEFORMAT = "dd MMM yyyy";

	private static final String SELECT_FINAL_VERSION = "budgetSelect_exceptionCode.1051";

	private static final String NEW_FINAL_VERSION = "budgetSelect_exceptionCode.1052";

	private static final String MODIFY_FINAL_VERSION = "budgetSelect_exceptionCode.1053";

	private static final String UNDERRECOVERY_NOT_EQUALS_TOTAL_UNDERRECOVERY = "budgetSelect_exceptionCode.1054";

	private static final String COST_SHARING_NOT_EQUALS_TOTAL_COST_SHARING = "budgetSelect_exceptionCode.1055";

	private static final String DISTRIBUTE_UNDERRECOVERY = "budgetSelect_exceptionCode.1056";

	/** Holds the connection string */
	// private String connectionURL = CoeusGuiConstants.CONNECTION_URL;

	private static final String CREATE_COST_SHARING = "budgetSelect_exceptionCode.1057";

	private static final String BUDGET_NOT_SAVED = "budgetSelect_exceptionCode.1058";

	private static final String SELECT_A_VERSION = "budgetSelect_exceptionCode.1059";

	/** Holds an instance of <CODE>ProposalDetailAdminForm</CODE> */
	// private ProposalDetailAdminForm proposalDetailAdminForm;

	private static final String BUDGET_LOCKED = "The budget for this proposal is locked. Do you want to open budget in Display Mode?";

	private static final String NO_MODULAR_BUDGET = "budget_summary_modular_budget_exceptionCode.1120";

	private static final String INCOMPLETE_MODULAR_BUDGET = "budget_summary_modular_budget_exceptionCode.1121";

	private static final String ERRKEY_NONE_NOT_ALLOWED = "budgetSummary_exceptionCode.1121";

	private static final char GET_MODULAR_BUDGET_DATA = 'b';

	private static final char GET_BUD_PER_FOR_PROP_IN_NEW_MODE = 'g';

	private static final char UPDATE_PERSONS = 'e';

	private static final char VALIDATION_CHECKS = 'N';

	private static final String MSGKEY_VALIDATION_WARNINGS = "budgetSelect_exceptionCode.1060";

	// Added for COEUSQA-3309 Inactive Appointment Type and Period Types error
	// validation alert required in Budget - start
	private static final char GET_APPOINTMENT_AND_PERIOD_DETAILS = '6';

	private static final char GET_BUDGET_INFO_FOR_COPY = 'w';

	// COEUSQA-1535-Access to institutionally maintained salaries in proposal
	// budget - Start
	private static final String HIERARCHY_SERVLET = "/ProposalHierarchyServlet";

	private static final char CHECK_VIEW_INSTITUTIONAL_SALARIES_RIGHT = 'U';
	// COEUSQA-1535-Access to institutionally maintained salaries in proposal
	// budget - End

	/** Holds the proposal number */
	private String proposalId;

	/**
	 * Holds the <CODE>ProposalDevelopmentFormBean</CODE> proposal start date
	 */
	private Date proposalStartDate;
	/** Holds the <CODE>ProposalDevelopmentFormBean</CODE> proposal end date */
	private Date proposalEndDate;

	/** Holds the new version number */
	private int newVersionNumber;

	// Case #1801: Parameterize Under-recovery and cost-sharing distribution end

	/**
	 * Holds the row number of <CODE>tblBudgetVersion</CODE> corresponding to
	 * the final version number if it exists in the database
	 */
	private int existingFinalVersionRow;
	/** Holds the final version number if it exists in the database */
	private int existingFinalVersion;
	/** Holds the current final version number */
	private int currentFinalVersion;
	/** Holds the new final version number */
	private int newFinalVersion = 0;

	/** Holds the selected version number */
	private int selectedVersion;
	/** Flag that specifies whether the budget status is modified */
	private boolean statusModified = false;

	/** Flag that specifies whether the budget final version is modified */
	private boolean finalVersionModified = false;
	/**
	 * Flag that specifies whether the budget final version is checked or
	 * unchecked while modifying final version
	 */
	private boolean finalVersionChecked;

	/**
	 * Flag that indicates if the user has right to modify the budget status
	 */
	private boolean hasRights = false;
	/**
	 * Flag to check if any of the budget version in the database is finalised
	 */
	private boolean hasFinalVersion;
	/** Flag to check if the selected budget version has cost sharing */
	private boolean hasCostSharing;
	/** Flag to check if the selected budget version has underrecovery */
	private boolean hasUnderRecovery;
	/** Flag to check if the user has invoked <CODE>saveFormData</CODE> */
	private boolean saveClicked = false;
	/**
	 * Flag that denotes if the form is just loaded. This flag is used to
	 * deactivate the <CODE>itemStateChanged</CODE> event of
	 * <CODE>cmbStatus</CODE> on form load
	 */
	private boolean initialState = false;

	/** Flag to check if the user has selected Complete as the budget status */
	private boolean completeSelected = false;
	/** Holds an instance of ListSelectionModel */
	private ListSelectionModel budgetSelectionModel;
	/**
	 * Holds all the budget data for a given budget version and proposal number
	 */
	private BudgetInfoBean budgetInfoBean;

	/** Holds an instance of <CODE>BudgetInfoBean</CODE> */
	private BudgetInfoBean oldBudgetInfoBean;
	/** Holds an instance of <CODE>BudgetInfoBean</CODE> */
	private BudgetInfoBean newBudgetInfoBean;
	/** Holds all the <CODE>BudgetInfoBean</CODE> for a given proposal number */
	private CoeusVector budgetInfo;

	/** Holds an instance of <CODE>DollarCurrencyTextField</CODE> */
	private DollarCurrencyTextField dollarData;
	/** Holds an instance of <CODE>SelectBudgetRenderer</CODE> */
	private SelectBudgetRenderer selectBudgetRenderer;
	/** Holds an instance of <CODE>SelectBudgetHeaderRenderer</CODE> */
	private SelectBudgetHeaderRenderer selectBudgetHeaderRenderer;

	/**
	 * Holds CoeusMessageResources instance used for reading message Properties.
	 */
	private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();

	/** Holds the parsed message */
	private String message = EMPTY_STRING;
	/** Holds an instance of <CODE>SelectBudgetForm</CODE> */
	private SelectBudgetForm selectBudgetForm;
	/** Holds the Unit Number of the proposal */
	private String unitNumber;
	/** Holds the Activity Type Code of the proposal */
	private int activityTypeCode;
	/**
	 * Holds the value '0' if no final version present '1' if any final version
	 * exits
	 */
	private int hasFinal = 0;
	/** Holds a copy of the vector <CODE>budgetInfo</CODE> */
	private Vector dataBean;
	/** Holds an instance of <CODE>SelectBudgetTableModel</CODE> */
	private SelectBudgetTableModel selectBudgetTableModel;
	/** Holds an instance of <CODE>ProposalDevelopmentFormBean</CODE> */
	private ProposalDevelopmentFormBean propDevFormBean;
	/** Holds an instance of <CODE>ProposalDevelopmentFormBean</CODE> */
	private ProposalDevelopmentFormBean modifiedPropDevFormBean;
	/** Holds the value of underrecovery amount */
	private double underRecoveryAmount;
	/** Holds the value of cost sharing amount */
	private double costSharingAmount;
	/**
	 * Holds the budget status present in
	 * <CODE>ProposalDevelopmentFormBean</CODE>
	 */
	private String budgetStatus;
	/** Holds the actual value of budget status */
	private String budgetStatusValue;
	private Vector vecBudgetData;
	/** Holds an instance of the mdiForm */
	private CoeusAppletMDIForm mdiForm;
	/** Holds true if any Budget is open for Editing */
	private boolean maxEditingWindowsOpen = false;
	/** Holds true if Budgets for Display has reached the maximum */
	private boolean maxDisplayedWindowsOpen = false;

	// Case #1801 : Parameterize Under-recovery and cost-sharing distribution
	// start
	/**
	 * Holds true if FORCE_UNDER_RECOVERY_DISTRIBUTION in parameter table is 1
	 */
	private boolean validateUndrRecovr = false;
	/** Holds true if FORCE_COST_SHARING_DISTRIBUTION in parameter table is 1 */

	private boolean validateCostSharing = false;
	// Added by Shiji for right checking : step 1 : start
	private boolean isOpenedFromPropListWindow;
	private final char GET_BUDGETS = 'd';
	// right checking : step 1 : end
	// Added for COEUSQA-2546- Two users can modify a budget at same time.
	// Locking not working -Start
	private boolean budgetLockUpdated = false;
	// Added for COEUSQA-2546- End
	// private String unitNumber;
	private final char GET_ALL_BUDGETS = 'A';
	private final char UPDATE_STATUS_FINAL_VERSION = 'C';
	// private final char USER_HAS_PROP_RIGHT = 'E';
	private final char RELEASE_BUDGET_LOCK = 'O';
	private boolean parentProposal;

	// 2158 End

	private boolean hierarchy;
	private ProposalHierarchyBean proposalHierarchyBean;
	private String parentPropNo = "";
	private final char GET_APPOINTMENTS_FOR_PERSON = 'D';
	private boolean isStatusComplete;

	private boolean hierarchyMode;
	// Added with case 2158: Budget Validations - Start
	boolean hasBudget = false;

	private boolean allow_budget_save = false;

	private Vector vecCEMessages;

	private boolean OkAction = false;
	// Added for COEUSQA-3309 Inactive Appointment Type and Period Types error
	// validation alert required in Budget - end

	/** Creates a new instance of SelectBudgetController */
	public SelectBudgetController() {
		dollarData = new DollarCurrencyTextField();
		selectBudgetTableModel = new SelectBudgetTableModel();
		this.mdiForm = CoeusGuiConstants.getMDIForm();
		selectBudgetForm = new SelectBudgetForm(mdiForm, true);
		registerComponents();
	}

	/**
	 * This method triggers all actions based on the event occured
	 * 
	 * @param actionEvent
	 *            takes the actionEvent
	 */
	@Override
	public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
		Object source = actionEvent.getSource();
		if (source.equals(selectBudgetForm.btnOk)) {
			saveClicked = true;
			OkAction = true;
			saveFormData();
			// Added for COEUSQA-2546- Two users can modify a budget at same
			// time. Locking not working -Start
			// If budget is not locked during the updation of Incomplete status,
			// release the lock.
			if (!budgetLockUpdated) {
				try {
					releaseBudgetLock();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
			OkAction = false;
			// Added for COEUSQA-2546- End
			// if(isParentProposal()){
			// super.adjustBudgetDates();
			// }
		} else if (source.equals(selectBudgetForm.btnCancel)) {
			performWindowClosing();
		} else if (source.equals(selectBudgetForm.btnNew)) {
			selectBudgetForm.dlgSelectBudgetForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
			selectBudgetForm.dlgSelectBudgetForm.getGlassPane().setVisible(true);
			if (isSaveRequired()) {
				saveFormData();
				if (statusModified)
					propDevFormBean = modifiedPropDevFormBean;
			}
			BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
			budgetInfoBean.setProposalNumber(proposalId);
			budgetInfoBean.setVersionNumber(newVersionNumber);
			budgetInfoBean.setUnitNumber(unitNumber);
			budgetInfoBean.setActivityTypeCode(activityTypeCode);
			java.sql.Date propStDate = new java.sql.Date(proposalStartDate.getTime());
			java.sql.Date propEndDate = new java.sql.Date(proposalEndDate.getTime());
			budgetInfoBean.setStartDate(propStDate);
			budgetInfoBean.setEndDate(propEndDate);
			// Added for case#3654 - Third option 'Default' in the campus
			// dropdown
			budgetInfoBean.setDefaultIndicator(true);
			/**
			 * sync the Propsoal Persons with the Budget Persons in New mode
			 * Case Id #1784 - start
			 */
			CoeusVector cvBudgetPersonData = getAllPropPersons(budgetInfoBean, TypeConstants.ADD_MODE);
			// End - Case Id 1784
			BudgetBaseWindowController budgetBaseWindowController = new BudgetBaseWindowController(
					"Create Budget for Proposal " + proposalId + ", Version " + newVersionNumber, budgetInfoBean,
					TypeConstants.ADD_MODE, propDevFormBean);
			budgetBaseWindowController.setBudgetPersonData(cvBudgetPersonData);
			// budgetBaseWindowController.setProposalDetailAdminForm(proposalDetailAdminForm);
			budgetBaseWindowController.setFunctionType(TypeConstants.ADD_MODE);
			/**
			 * Case #1801 :Parameterize Under-recovery and cost-sharing
			 * distribution Start 3
			 */
			budgetBaseWindowController.setForceUnderRecovery(this.validateUndrRecovr);
			budgetBaseWindowController.setForceCostSharing(this.validateCostSharing);
			/**
			 * Case #1801 :Parameterize Under-recovery and cost-sharing
			 * distribution End 3
			 */

			if (isHierarchy()) {
				// Added by chandra - start
				budgetBaseWindowController.setHierarcy(isHierarchy());
				budgetBaseWindowController.setParentProposal(isParentProposal());
				budgetBaseWindowController.setProposalHierarchyBean(getProposalHierarchyBean());
				budgetBaseWindowController.setParentPropNo(getParentPropNo());
				// Added by chandra - end
			}

			budgetBaseWindowController.display();
			selectBudgetForm.dlgSelectBudgetForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
			selectBudgetForm.dlgSelectBudgetForm.getGlassPane().setVisible(false);
			selectBudgetForm.dlgSelectBudgetForm.dispose();
		} else if (source.equals(selectBudgetForm.btnModify)) {
			if (isSaveRequired()) {
				saveFormData();
				if (statusModified)
					propDevFormBean = modifiedPropDevFormBean;
			}
			if (selectBudgetForm.tblBudgetVersion.getSelectedRowCount() == 0) {
				message = coeusMessageResources.parseMessageKey(SELECT_A_VERSION);
				CoeusOptionPane.showInfoDialog(message);
				return;
			}
			selectBudgetForm.dlgSelectBudgetForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
			selectBudgetForm.dlgSelectBudgetForm.getGlassPane().setVisible(true);

			BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
			budgetInfoBean = (BudgetInfoBean) budgetInfo.get(selectBudgetForm.tblBudgetVersion.getSelectedRow());
			budgetInfoBean.setUnitNumber(unitNumber);
			budgetInfoBean.setActivityTypeCode(activityTypeCode);

			/**
			 * Code for the synching the proposal persons with the Budget
			 * Persons - start It is an enhancement for Case Id #1784
			 */
			// syncPropPersonWithBudgetPerson(proposalId,selectedVersion,budgetInfoBean);
			CoeusVector cvBudgetPersonData = getAllPropPersons(budgetInfoBean, getFunctionType());
			try {
				if (cvBudgetPersonData != null && cvBudgetPersonData.size() > 0) {
					saveBudgetPersons(cvBudgetPersonData);
				}
			} catch (CoeusException exception) {
				exception.printStackTrace();
				CoeusOptionPane.showErrorDialog(exception.getMessage());
			}
			// Proposal persons sync with the budget Persons - End

			BudgetBaseWindowController budgetBaseWindowController = new BudgetBaseWindowController(
					"Modify Budget for Proposal " + proposalId + ", Version " + selectedVersion, budgetInfoBean,
					TypeConstants.MODIFY_MODE, propDevFormBean);
			// budgetBaseWindowController.setProposalDetailAdminForm(proposalDetailAdminForm);
			budgetBaseWindowController.setFunctionType(TypeConstants.MODIFY_MODE);
			/**
			 * Case #1801 :Parameterize Under-recovery and cost-sharing
			 * distribution Start 4
			 */
			budgetBaseWindowController.setForceUnderRecovery(this.validateUndrRecovr);
			budgetBaseWindowController.setForceCostSharing(this.validateCostSharing);
			/**
			 * Case #1801 :Parameterize Under-recovery and cost-sharing
			 * distribution End 4
			 */

			if (isHierarchy()) {
				// Added by chandra - start
				// Code added for bug fix case#3183
				budgetBaseWindowController.setHierarchyMode(hierarchyMode);
				budgetBaseWindowController.setHierarcy(isHierarchy());
				budgetBaseWindowController.setParentProposal(isParentProposal());
				budgetBaseWindowController.setProposalHierarchyBean(getProposalHierarchyBean());
				budgetBaseWindowController.setParentPropNo(getParentPropNo());
				// Added by chandra - end
			}

			budgetBaseWindowController.display();
			selectBudgetForm.dlgSelectBudgetForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
			selectBudgetForm.dlgSelectBudgetForm.getGlassPane().setVisible(false);
			selectBudgetForm.dlgSelectBudgetForm.dispose();
			// Added for Case#2341 - Recalculate Budget if Project dates change
			// For syncing budget rates with project rates
			budgetBaseWindowController.syncToProposalDates();
		} else if (source.equals(selectBudgetForm.btnDisplay)) {
			if (isSaveRequired()) {
				saveFormData();
				if (statusModified)
					propDevFormBean = modifiedPropDevFormBean;
			}
			if (selectBudgetForm.tblBudgetVersion.getSelectedRowCount() == 0) {
				message = coeusMessageResources.parseMessageKey(SELECT_A_VERSION);
				CoeusOptionPane.showInfoDialog(message);
				return;
			}
			selectBudgetForm.dlgSelectBudgetForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
			selectBudgetForm.dlgSelectBudgetForm.getGlassPane().setVisible(true);
			BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
			budgetInfoBean = (BudgetInfoBean) budgetInfo.get(selectBudgetForm.tblBudgetVersion.getSelectedRow());
			budgetInfoBean.setUnitNumber(unitNumber);
			budgetInfoBean.setActivityTypeCode(activityTypeCode);

			BudgetBaseWindowController budgetBaseWindowController = new BudgetBaseWindowController(
					"Display Budget for Proposal " + proposalId + ", Version " + selectedVersion, budgetInfoBean,
					TypeConstants.DISPLAY_MODE, propDevFormBean);
			budgetBaseWindowController.setFunctionType(TypeConstants.DISPLAY_MODE);
			// Code commented for bug fix case#3183
			// To open modular budget for parent proposal in display mode
			// budgetBaseWindowController.setHierarchyMode(hierarchyMode);
			if (getFunctionType() == TypeConstants.MODIFY_MODE) {
				budgetBaseWindowController.setReleaseLock(true);
			}

			// Added by chandra - start
			if (isHierarchy()) {
				budgetBaseWindowController.setHierarcy(isHierarchy());
				budgetBaseWindowController.setParentProposal(isParentProposal());
				budgetBaseWindowController.setProposalHierarchyBean(getProposalHierarchyBean());
				budgetBaseWindowController.setParentPropNo(getParentPropNo());
			}
			// Added by chandra - end

			// if(isParentProposal()){
			// super.adjustBudgetDates();
			// }

			budgetBaseWindowController.display();
			selectBudgetForm.dlgSelectBudgetForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
			selectBudgetForm.dlgSelectBudgetForm.getGlassPane().setVisible(false);
			selectBudgetForm.dlgSelectBudgetForm.dispose();

		} else if (source.equals(selectBudgetForm.btnCopy)) {
			if (isSaveRequired()) {
				saveFormData();
				if (statusModified)
					propDevFormBean = modifiedPropDevFormBean;
			}
			if (selectBudgetForm.tblBudgetVersion.getSelectedRowCount() == 0) {
				message = coeusMessageResources.parseMessageKey(SELECT_A_VERSION);
				CoeusOptionPane.showInfoDialog(message);
				return;
			}
			selectBudgetForm.dlgSelectBudgetForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
			selectBudgetForm.dlgSelectBudgetForm.getGlassPane().setVisible(true);
			BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
			budgetInfoBean = (BudgetInfoBean) budgetInfo.get(selectBudgetForm.tblBudgetVersion.getSelectedRow());
			budgetInfoBean.setUnitNumber(unitNumber);
			budgetInfoBean.setActivityTypeCode(activityTypeCode);

			CopyBudgetForm budgetCopy = new CopyBudgetForm(CoeusGuiConstants.getMDIForm(), true, selectedVersion,
					budgetInfoBean, propDevFormBean);
			budgetCopy.setNewVersionNumber(newVersionNumber);
			// budgetCopy.setProposalDetailAdminForm(proposalDetailAdminForm);
			boolean okClicked = budgetCopy.display();
			selectBudgetForm.dlgSelectBudgetForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
			selectBudgetForm.dlgSelectBudgetForm.getGlassPane().setVisible(false);
			if (okClicked) {
				// Release the lock
				try {
					releaseBudgetLock();
					selectBudgetForm.dlgSelectBudgetForm.dispose();
				} catch (CoeusClientException coeusClientException) {
					coeusClientException.printStackTrace();
				}
			}
		}

	}

	/**
	 * displays the Form which is being controlled.
	 */
	@Override
	public void display() {
		selectBudgetForm.dlgSelectBudgetForm.show();
	}

	/**
	 * This method displays the list of Inactive elements
	 */
	public void displayCENotAvailableMessages() {
		CostElementMessageForm costElementMessageForm = new CostElementMessageForm();
		costElementMessageForm.setFormData(vecCEMessages);
		costElementMessageForm.display();
	}
	// Added for COEUSQA-3309 Inactive Appointment Type and Period Types error
	// validation alert required in Budget - end

	/**
	 * Method to check if the user has rights to modify budget status
	 * 
	 * @return 0 if user does not have rights 1 if user has rights to modify
	 *         budget status
	 * @param rightId
	 *            takes a right id
	 * @param proposalId
	 *            takes a proposal id
	 */
	/*
	 * private int userHasRights( String proposalId, String rightId ){
	 * this.proposalId = proposalId; this.rightId = rightId; boolean
	 * success=false; Vector userRight = new Vector();
	 * userRight.addElement(proposalId); userRight.addElement(rightId);
	 * 
	 * RequesterBean requester = new RequesterBean(); requester.setFunctionType(
	 * USER_HAS_PROP_RIGHT ); requester.setId(proposalId);
	 * requester.setDataObjects(userRight);
	 * 
	 * String connectTo = connectionURL + conURL; AppletServletCommunicator comm
	 * = new AppletServletCommunicator(connectTo, requester); comm.send();
	 * ResponderBean response = comm.getResponse(); int right = 0; if (
	 * response!=null ){ success=true; right = Integer.parseInt(
	 * response.getDataObject().toString() ); } return right; }
	 */

	/**
	 * perform field formatting. enabling, disabling components depending on the
	 * function type.
	 */
	@Override
	public void formatFields() {
		if (isHierarchy()) {
			isStatusComplete = isBudgetComplete();
		}
		if (hasRights) {
			selectBudgetForm.cmbStatus.setEnabled(true);
		} else {
			selectBudgetForm.cmbStatus.setEnabled(false);
		}
		if (getFunctionType() == MODIFY_MODE) {
			if (selectBudgetForm.tblBudgetVersion.getRowCount() > 0) {
				selectBudgetForm.btnOk.setEnabled(true);
				selectBudgetForm.btnDisplay.setEnabled(true);
			} else {
				// If no budget versions exist
				selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_NONE);
				if (!maxEditingWindowsOpen && getFunctionType() == TypeConstants.DISPLAY_MODE) {
					selectBudgetForm.btnNew.setEnabled(false);
				} else {
					selectBudgetForm.btnNew.setEnabled(true);
				}
				selectBudgetForm.btnCopy.setEnabled(false);
				selectBudgetForm.btnDisplay.setEnabled(false);
				selectBudgetForm.btnModify.setEnabled(false);
				selectBudgetForm.btnOk.setEnabled(false);
			}
			if (hasRights && !(selectBudgetForm.cmbStatus.getSelectedItem().toString().equals(VAL_COMPLETE))) {
				// selectBudgetForm.btnOk.setEnabled(false);
				// selectBudgetForm.btnDisplay.setEnabled(false);
				if (!maxEditingWindowsOpen && getFunctionType() == TypeConstants.DISPLAY_MODE) {
					selectBudgetForm.btnNew.setEnabled(false);
				} else {
					selectBudgetForm.btnNew.setEnabled(true);
				}
				selectBudgetForm.btnModify.setEnabled(true);
				selectBudgetForm.btnCopy.setEnabled(true);
			}
			if (!hasRights || selectBudgetForm.cmbStatus.getSelectedItem().toString().equals(VAL_COMPLETE)) {
				selectBudgetForm.btnOk.setEnabled(true);
				selectBudgetForm.btnDisplay.setEnabled(true);
				selectBudgetForm.btnNew.setEnabled(false);
				selectBudgetForm.btnCopy.setEnabled(false);
				selectBudgetForm.btnModify.setEnabled(false);
			} else {
				initialState = false;
			}
		}
		if (getFunctionType() == DISPLAY_MODE) {
			selectBudgetForm.cmbStatus.setEnabled(false);
			selectBudgetForm.btnDisplay.setEnabled(true);
			selectBudgetForm.btnNew.setEnabled(false);
		}
		if (selectBudgetForm.tblBudgetVersion.getRowCount() == 0) {
			selectBudgetForm.btnCopy.setEnabled(false);
			selectBudgetForm.btnModify.setEnabled(false);
			selectBudgetForm.cmbStatus.setEnabled(false);
			selectBudgetForm.btnDisplay.setEnabled(false);
		}

		if (getFunctionType() != DISPLAY_MODE && isParentProposal()) {
			selectBudgetForm.btnCopy.setEnabled(false);
			// Code commented and added for bugfix case#3183 - starts
			// enable modify button for the parent proposal
			// selectBudgetForm.btnModify.setEnabled(false);
			if (budgetStatus == null || !budgetStatus.equals(COMPLETE)) {
				selectBudgetForm.btnModify.setEnabled(true);
			}
			// Code commented and added for bugfix case#3183 - ends
			selectBudgetForm.btnNew.setEnabled(false);
			hierarchyMode = true;
		}

		if (getFunctionType() != DISPLAY_MODE && !isParentProposal() && isStatusComplete) {
			selectBudgetForm.btnCopy.setEnabled(false);
			selectBudgetForm.btnModify.setEnabled(true);
			selectBudgetForm.cmbStatus.setEnabled(false);
			selectBudgetForm.btnOk.setEnabled(false);
			selectBudgetForm.btnModify.setEnabled(false);
			selectBudgetForm.btnNew.setEnabled(false);
			selectBudgetForm.btnDisplay.setEnabled(true);
			selectBudgetForm.btnCancel.setEnabled(true);
			hierarchyMode = true;
		}
	}

	/**
	 * This is to get all the proposal persons to Update budget persons when
	 * Budget is trying to open in NEW_MODE.
	 * 
	 * @param proposalNumber,
	 *            Version Number @ returns HashMap contains valid Persons to
	 * fetch the data and Invalid persons where necessary data has to be filled
	 * @throws CoeusException
	 *             Case Id #1784
	 */
	private HashMap getAllProposalPerForBudgetPersons(BudgetInfoBean budgetInfoBean, char functionType)
			throws CoeusException {
		HashMap proposalPersonData = null;
		Vector data = new Vector();
		data.add(0, budgetInfoBean.getProposalNumber());
		data.add(1, new Integer(budgetInfoBean.getVersionNumber()));
		data.add(2, new Character(functionType));
		RequesterBean requester = new RequesterBean();
		requester.setFunctionType(GET_BUD_PER_FOR_PROP_IN_NEW_MODE);
		requester.setDataObjects(data);
		AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
		comm.send();
		ResponderBean response = comm.getResponse();
		if (response.isSuccessfulResponse()) {
			proposalPersonData = (HashMap) response.getDataObject();
		} else {
			throw new CoeusException(response.getMessage(), 1);
		}
		return proposalPersonData;
	}// End getAllProposalPerForBudgetPersons...

	/**
	 * This method will sync the Propsoal Persons with the Budget Persons In New
	 * Mode Case Id #1784
	 */
	private CoeusVector getAllPropPersons(BudgetInfoBean budgetInfoBean, char functionType) {
		HashMap personData = null;
		CoeusVector budgetPersonData = new CoeusVector();
		CoeusVector cvValidPersons = null;
		CoeusVector cvInvalidPersons = null;
		CoeusVector validData = null;
		// Include Rolodex in Budget Persons - Enhancement - START - 1
		CoeusVector cvValidPersonBeans = null, cvInvalidPersonBeans = null;
		// Include Rolodex in Budget Persons - Enhancement - END - 1
		try {
			/**
			 * Get all the proposal persons data which contains valid budget
			 * persons and invalid budget persons
			 */
			personData = getAllProposalPerForBudgetPersons(budgetInfoBean, functionType);
			if (personData != null) {
				cvValidPersons = (CoeusVector) personData.get("VALID_PERSONS");
				cvInvalidPersons = (CoeusVector) personData.get("INVALID_PERSONS");
				// Include Rolodex in Budget Persons - Enhancement - START - 2
				cvValidPersonBeans = (CoeusVector) personData.get("VALID_PERSON_BEANS");
				cvInvalidPersonBeans = (CoeusVector) personData.get("INVALID_PERSON_BEANS");
				// Include Rolodex in Budget Persons - Enhancement - END - 2
			}

			// Get all the valid persons and then prepare budgetPersons data
			if (cvValidPersons != null && cvValidPersons.size() > 0) {
				for (int index = 0; index < cvValidPersons.size(); index++) {
					String personId = (String) cvValidPersons.get(index);
					String fullName = getPersonFullName(personId);
					String personStatus = getPersonStatus(personId);
					// Added for Case#2918 - Use of Salary Anniversary Date for
					// calculating inflation in budget development module -
					// Start
					Equals eqPersonId = new Equals("personId", personId);
					CoeusVector cvfilterValidPersonBeans = cvValidPersonBeans.filter(eqPersonId);
					CoeusVector cvBudgetPerons = getBudgetPersonsData(personId, fullName, budgetInfoBean,
							cvfilterValidPersonBeans);
					// Added for Case#2918 - Use of Salary Anniversary Date for
					// calculating inflation in budget development module - End

					budgetPersonData.addAll(cvBudgetPerons);
				}
			}
			// Enhancement 2033
			if (cvInvalidPersons != null && cvInvalidPersons.size() > 0) {
				edu.mit.coeus.budget.gui.SyncPropPersonForBudget syncBudgetPersons = null;
				// syncBudgetPersons = new
				// edu.mit.coeus.budget.gui.SyncPropPersonForBudget(mdiForm,cvInvalidPersons,budgetInfoBean);
				syncBudgetPersons = new edu.mit.coeus.budget.gui.SyncPropPersonForBudget(mdiForm, cvInvalidPersonBeans,
						budgetInfoBean);
				syncBudgetPersons.display();
				validData = syncBudgetPersons.getFormData();
				if (validData != null && validData.size() > 0) {
					budgetPersonData.addAll(validData);
				}
			}

		} catch (CoeusException exception) {
			exception.printStackTrace();
			CoeusOptionPane.showErrorDialog(exception.getMessage());
		}
		return budgetPersonData;
	}

	// Modified by shiji for Right Checking : step 4 : start
	private Vector getBudgetInfo() throws CoeusClientException {
		Vector inDataObjects = new Vector();
		RequesterBean requester = new RequesterBean();
		requester.setFunctionType(GET_BUDGETS);
		requester.setId(proposalId);

		// Set the flag whether to lock the budget or not depending on the open
		// Editing Budgets
		// and the function type if budget is opened from Proposal Detail
		requester.setDataObject(new Boolean(!maxEditingWindowsOpen && getFunctionType() != TypeConstants.DISPLAY_MODE));
		inDataObjects.add(0, new Boolean(isOpenedFromPropListWindow));
		inDataObjects.add(1, unitNumber);
		if (getFunctionType() == TypeConstants.DISPLAY_MODE) {
			inDataObjects.add(2, new Boolean(true));
			// inDataObjects.add(1,unitNumber);
		} else {
			inDataObjects.add(2, new Boolean(false));
		}
		requester.setDataObjects(inDataObjects);
		AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
		comm.send();
		ResponderBean response = comm.getResponse();
		Vector budgetData = new Vector();
		if (response.isSuccessfulResponse()) {
			budgetData = response.getDataObjects();
			hasRights = ((Boolean) budgetData.get(2)).booleanValue();
			boolean hasOnlyViewRight = ((Boolean) budgetData.elementAt(5)).booleanValue();
			if (!maxEditingWindowsOpen && hasRights && getFunctionType() != TypeConstants.DISPLAY_MODE
					&& !hasOnlyViewRight) {
				setFunctionType(TypeConstants.MODIFY_MODE);
			} else {
				setFunctionType(TypeConstants.DISPLAY_MODE);
			}
		} else {
			if (response.isLocked()) {
				int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(BUDGET_LOCKED),
						CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
				switch (option) {
				case JOptionPane.YES_OPTION:
					budgetData = response.getDataObjects();
					setFunctionType(TypeConstants.DISPLAY_MODE);
					break;
				case JOptionPane.NO_OPTION:
					throw new CoeusClientException("");
				}
			} else {
				throw new CoeusClientException(response.getMessage());
			}

		}
		return budgetData;
	}
	// Right Checking : step 4 : end

	// Added for COEUSQA-3309 Inactive Appointment Type and Period Types error
	// validation alert required in Budget - start
	/**
	 *
	 * Get the budget details from the server
	 * 
	 * @return cvBudgetData
	 * @throws CoeusException
	 *             if exception
	 */
	private CoeusVector getBudgetInfoForProposal() throws CoeusClientException {
		CoeusVector cvBudgetData = null;
		final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL + "/BudgetMaintenanceServlet";
		RequesterBean requester = new RequesterBean();
		requester.setFunctionType(GET_BUDGET_INFO_FOR_COPY);
		requester.setId(proposalId);
		edu.mit.coeus.utils.AppletServletCommunicator comm = new edu.mit.coeus.utils.AppletServletCommunicator(
				CONNECTION_STRING, requester);
		comm.send();
		ResponderBean response = comm.getResponse();
		if (response.isSuccessfulResponse()) {
			cvBudgetData = (CoeusVector) response.getDataObject();
		} else {
			throw new CoeusClientException(response.getMessage(), CoeusClientException.ERROR_MESSAGE);
		}
		return cvBudgetData;
	}

	/**
	 * This method will set the Proposal Persons with the Budget Persons In New
	 * Mode Case Id #1784
	 */
	private CoeusVector getBudgetPersonsData(String personId, String personName, BudgetInfoBean budgetInfoBean,
			CoeusVector cvValidPersonBeans) throws CoeusException {
		CoeusVector cvBudgetPersons = new CoeusVector();
		CoeusVector cvData = getPersonInfo(personId);
		BudgetPersonsBean budgetPersonsBean = null;
		AppointmentsBean appointmentsBean = null;
		BudgetPersonSyncBean budgetPersonSyncBean = null;
		if (cvData != null && cvData.size() > 0) {
			appointmentsBean = (AppointmentsBean) cvData.get(0);
			budgetPersonsBean = new BudgetPersonsBean();
			budgetPersonsBean.setStatus(getPersonStatus(personId));
			budgetPersonsBean.setProposalNumber(budgetInfoBean.getProposalNumber());

			budgetPersonsBean.setAw_ProposalNumber(budgetInfoBean.getProposalNumber());

			budgetPersonsBean.setVersionNumber(budgetInfoBean.getVersionNumber());
			budgetPersonsBean.setAw_VersionNumber(budgetInfoBean.getVersionNumber());

			budgetPersonsBean.setPersonId(personId);
			budgetPersonsBean.setAw_PersonId(personId);
			budgetPersonsBean.setFullName(personName);

			budgetPersonsBean.setJobCode(appointmentsBean.getJobCode());
			budgetPersonsBean.setAw_JobCode(appointmentsBean.getJobCode());
			budgetPersonsBean.setAppointmentType(appointmentsBean.getAppointmentType());
			budgetPersonsBean.setAw_AppointmentType(appointmentsBean.getAppointmentType());

			budgetPersonsBean.setEffectiveDate(budgetInfoBean.getStartDate());
			budgetPersonsBean.setAw_EffectiveDate(budgetInfoBean.getStartDate());
			// Bug Fix #1908
			// COEUSQA-1535-Access to institutionally maintained salaries in
			// proposal budget - Start
			// budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
			// budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
			if (hasRightToViewInstitutionalSalaries(budgetInfoBean, personId)) {
				budgetPersonsBean.setCalculationBase(appointmentsBean.getSalary());
				budgetPersonsBean.setAw_CalculationBase(appointmentsBean.getSalary());
			} else {
				budgetPersonsBean.setCalculationBase(0.00);
				budgetPersonsBean.setAw_CalculationBase(0.00);
			}
			// COEUSQA-1535-Access to institutionally maintained salaries in
			// proposal budget - End
			// Added for Case#2918 - Use of Salary Anniversary Date for
			// calculating inflation in budget development module - start
			if (cvValidPersonBeans != null && cvValidPersonBeans.size() > 0) {
				budgetPersonSyncBean = (BudgetPersonSyncBean) cvValidPersonBeans.get(0);
				budgetPersonsBean.setSalaryAnniversaryDate(budgetPersonSyncBean.getSalaryAnniversaryDate());
				budgetPersonsBean.setAw_SalaryAnniversaryDate(budgetPersonSyncBean.getSalaryAnniversaryDate());
			}
			// Added for Case#2918 - Use of Salary Anniversary Date for
			// calculating inflation in budget development module - end
			budgetPersonsBean.setAcType(TypeConstants.INSERT_RECORD);
			cvBudgetPersons.addElement(budgetPersonsBean);
		}
		return cvBudgetPersons;
	}

	/**
	 * An overridden method of the controller
	 * 
	 * @return selectBudgetForm returns the controlled form component
	 */
	@Override
	public Component getControlledUI() {
		return selectBudgetForm;
	}

	/**
	 * returns the form data
	 * 
	 * @return returns the form data
	 */
	@Override
	public Object getFormData() {
		return selectBudgetForm;
	}

	/**
	 * Method to fetch the proposal details and Inactive Types
	 * 
	 * @param proposalNumber
	 * @ returns boolean value
	 */
	private boolean getInactiveAppAndPeriodTypes(String proposalNumber, int versionNumber) {
		Vector inactivePeriodType = new Vector();
		Vector inactiveAppointmentType = new Vector();
		String message;
		MessageFormat formatter = new MessageFormat("");
		vecCEMessages = new Vector();
		boolean periodFlag = false;
		boolean appointmentTypeFlag = false;
		Vector dataObject = new Vector();
		final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL + "/BudgetMaintenanceServlet";
		RequesterBean requester = new RequesterBean();
		dataObject.add(proposalNumber);
		dataObject.add(versionNumber);
		requester.setFunctionType(GET_APPOINTMENT_AND_PERIOD_DETAILS);
		requester.setDataObjects(dataObject);
		edu.mit.coeus.utils.AppletServletCommunicator comm = new edu.mit.coeus.utils.AppletServletCommunicator(
				CONNECTION_STRING, requester);
		comm.send();
		ResponderBean response = comm.getResponse();
		if (response != null && response.isSuccessfulResponse()) {
			inactivePeriodType = (Vector) response.getDataObjects().get(0);
			inactiveAppointmentType = (Vector) response.getDataObjects().get(1);
		}
		// It holds the inactive periodtypes
		if (inactivePeriodType != null && inactivePeriodType.size() > 0) {
			for (int index = 0; index < inactivePeriodType.size(); index++) {
				String periodType = (String) inactivePeriodType.get(index);
				message = formatter.format(coeusMessageResources.parseMessageKey("budgetSelect_exceptionCode.1071"),
						periodType);
				vecCEMessages.add(message);
			}
			// if there are any inactive Period types then set the flag
			// periodFlag as true
			periodFlag = true;
		}
		// It holds the inactive appointtype types
		if (inactiveAppointmentType != null && inactiveAppointmentType.size() > 0) {
			for (int index = 0; index < inactiveAppointmentType.size(); index++) {
				String appointmentType = (String) inactiveAppointmentType.get(index);
				message = formatter.format(coeusMessageResources.parseMessageKey("budgetSelect_exceptionCode.1072"),
						appointmentType);
				vecCEMessages.add(message);
			}
			// if there are any inactive Appointment types then set the flag
			// appointmentTypeFlag as true
			appointmentTypeFlag = true;
		}
		// if there are inactive elements then it returns the the value as true
		if (periodFlag || appointmentTypeFlag) {
			return true;
		}
		return false;
	}

	/**
	 * Get the Budget Period Data and Modular Budget Data for the Modular Budget
	 * Validation while ItemChangeListener is fired to Budget status as Complete
	 * 
	 * @param BudgeInfoBean
	 *            budgeInfoBean
	 * @returns HashMap containing 2 vectors of BudgetPeriods and Modular Budget
	 * @throws CoeusClinetExceptioon
	 *             Case Id 1626
	 */
	private HashMap getModularData(BudgetInfoBean budgetInfoBean) throws CoeusClientException {
		HashMap hmModularData = null;
		Vector clinetData = new Vector();
		final String BUDGET_PERSONS = "/BudgetMaintenanceServlet";
		final String connectTo = CoeusGuiConstants.CONNECTION_URL + BUDGET_PERSONS;
		RequesterBean requester = new RequesterBean();
		ResponderBean responder = null;
		clinetData.add(0, budgetInfoBean.getProposalNumber());
		clinetData.add(1, new Integer(budgetInfoBean.getVersionNumber()));
		requester.setFunctionType(GET_MODULAR_BUDGET_DATA);
		requester.setDataObjects(clinetData);
		AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
		comm.send();
		responder = comm.getResponse();
		if (responder.isSuccessfulResponse()) {
			hmModularData = (HashMap) responder.getDataObject();
		} else {
			throw new CoeusClientException(responder.getMessage());
		}
		return hmModularData;
	}// getModularData...

	/**
	 * Getter for property parentPropNo.
	 * 
	 * @return Value of property parentPropNo.
	 */
	public java.lang.String getParentPropNo() {
		return parentPropNo;
	}

	/**
	 * Get the person full Name for the selected person,this value is required
	 * to display the header of the table. @ param person Id as input to get the
	 * person Name @ returns full name of the person for the given person Id.
	 */
	private String getPersonFullName(String personId) throws CoeusException {
		final String BUDGET_PERSONS = "/BudgetMaintenanceServlet";
		final String connectTo = CoeusGuiConstants.CONNECTION_URL + BUDGET_PERSONS;
		String fullName = EMPTY_STRING;
		RequesterBean requester = new RequesterBean();
		ResponderBean responder = null;
		requester.setFunctionType('c');
		requester.setDataObject(personId);

		AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);

		comm.send();
		responder = comm.getResponse();
		if (responder.isSuccessfulResponse()) {
			fullName = (String) responder.getDataObject();
		} else {
			throw new CoeusException(responder.getMessage());
		}

		return fullName;
	}// End getPersonFullName...

	/**
	 * Get the Persons Appointment Type. If more than one Appointment Type
	 * generate Another form to select. Making a server side call.
	 */

	public CoeusVector getPersonInfo(String personId) throws CoeusException {
		final String BUDGET_PERSONS = "/BudgetMaintenanceServlet";
		final String connectTo = CoeusGuiConstants.CONNECTION_URL + BUDGET_PERSONS;
		CoeusVector vctAppointments = null;
		ResponderBean responder = null;
		RequesterBean requester = new RequesterBean();
		requester.setFunctionType('D');
		requester.setDataObject(personId);

		AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);

		comm.send();
		responder = comm.getResponse();
		if (responder.isSuccessfulResponse()) {
			vctAppointments = (CoeusVector) responder.getDataObject();

		}
		return vctAppointments;
	}

	/**
	 * @param personId
	 * @return
	 */
	private String getPersonStatus(String personId) throws CoeusException {
		final String BUDGET_PERSONS = "/BudgetMaintenanceServlet";
		final String connectTo = CoeusGuiConstants.CONNECTION_URL + BUDGET_PERSONS;
		String status = EMPTY_STRING;
		RequesterBean requester = new RequesterBean();
		ResponderBean responder = null;
		requester.setFunctionType('c');
		requester.setDataObject(personId);

		AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);

		comm.send();
		responder = comm.getResponse();
		if (responder.isSuccessfulResponse()) {
			status = (String) responder.getDataObject();
		} else {
			throw new CoeusException(responder.getMessage());
		}

		return status;

	}

	/**
	 * Getter for property proposalHierarchyBean.
	 * 
	 * @return Value of property proposalHierarchyBean.
	 */
	public edu.mit.coeus.propdev.bean.ProposalHierarchyBean getProposalHierarchyBean() {
		return proposalHierarchyBean;
	}

	/**
	 * Getter for property proposalId.
	 * 
	 * @return Value of property proposalId.
	 */
	public java.lang.String getProposalId() {
		return proposalId;
	}

	// 2158 End
	// COEUSQA-1535-Access to institutionally maintained salaries in proposal
	// budget - Start
	/**
	 * 
	 * To check if user has the rights to view institutional salaries
	 * 
	 * @return boolean value for right
	 * @throws CoeusException
	 *             if exception
	 */
	protected boolean hasRightToViewInstitutionalSalaries(BudgetInfoBean budgetInfoBean, String appointmentPersonId)
			throws CoeusException {
		String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
		RequesterBean request = new RequesterBean();
		Boolean hasRight = null;
		CoeusVector cvDataToServer = new CoeusVector();
		// To set the proposal number, function type
		cvDataToServer.add(budgetInfoBean.getProposalNumber());
		cvDataToServer.add(false);
		cvDataToServer.add(budgetInfoBean);
		cvDataToServer.add(appointmentPersonId);
		request.setDataObject(cvDataToServer);
		request.setFunctionType(CHECK_VIEW_INSTITUTIONAL_SALARIES_RIGHT);
		AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
		comm.send();
		ResponderBean response = comm.getResponse();
		if (response != null && response.isSuccessfulResponse()) {
			hasRight = (Boolean) response.getDataObject();
		} else {
			throw new CoeusException(response.getMessage());
		}
		return hasRight;
	}
	// COEUSQA-1535-Access to institutionally maintained salaries in proposal
	// budget - End

	/**
	 * Check whether When budget status is set to Complete in a parent proposal,
	 * application should check if all of the child budgets have status of
	 * ‘Complete’ and they all have a budget version marked Final. Before
	 * marking a parent proposal as complete, make sure application performs a
	 * sync of the parent budget with all child proposals.
	 */
	private boolean isBudgetComplete() {
		final String HIERARCHY_SERVLET = "/ProposalHierarchyServlet";
		final char IS_COMPLETE = 'Q';
		final String connectTo = CoeusGuiConstants.CONNECTION_URL + HIERARCHY_SERVLET;
		RequesterBean requester = new RequesterBean();
		ResponderBean responder = null;
		boolean success = false;
		try {
			requester.setFunctionType(IS_COMPLETE);
			requester.setDataObject(getProposalId());
			AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
			comm.send();
			responder = comm.getResponse();
			if (responder.isSuccessfulResponse()) {
				success = ((Boolean) responder.getDataObject()).booleanValue();
			} else {
				throw new CoeusException(responder.getMessage());
			}
		} catch (CoeusException exception) {
			exception.printStackTrace();
			CoeusOptionPane.showErrorDialog(exception.getMessage());
		}
		return success;
	}

	/**
	 * This method is used to check if a budget for a given proposal is open in
	 * the given mode or not.
	 * 
	 * @param refId
	 *            refId - the title of the budget window
	 * @param mode
	 *            mode of Form open.
	 * @param displayMessage
	 *            if true displays error messages else doesn't.
	 * @return true if Budget window is open, false otherwise
	 */
	private boolean isBudgetWindowOpen(String refId, char mode, boolean displayMessage) throws CoeusClientException {
		boolean duplicate = false;
		boolean editingBudgets = false;
		int displayedBudgets = 0;
		try {
			duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.BUDGET_FRAME_TITLE, refId, mode);
		} catch (Exception e) {
			// Exception occured. Record may be already opened in requested mode
			// or if the requested mode is edit mode and application is already
			// editing any other record.
			duplicate = true;
			if (displayMessage) {
				int newBudgets = mdiForm.getFrameCount(CoeusGuiConstants.BUDGET_FRAME_TITLE, TypeConstants.ADD_MODE);

				int modifyBudgets = mdiForm.getFrameCount(CoeusGuiConstants.BUDGET_FRAME_TITLE,
						TypeConstants.MODIFY_MODE);

				if (newBudgets == 1 || modifyBudgets == 1) {
					editingBudgets = true;
				}

				displayedBudgets = mdiForm.getFrameCount(CoeusGuiConstants.BUDGET_FRAME_TITLE,
						TypeConstants.DISPLAY_MODE);

				if (editingBudgets) {
					setFunctionType(TypeConstants.DISPLAY_MODE);
					maxEditingWindowsOpen = true;
				} else {
					setFunctionType(TypeConstants.MODIFY_MODE);
				}

				if (displayedBudgets == 2) {
					maxDisplayedWindowsOpen = true;
				}
			}
			// try to get the requested frame which is already opened
			CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.BUDGET_FRAME_TITLE, refId);
			if (frame != null) {
				try {
					frame.setSelected(true);
					frame.setVisible(true);
					throw new CoeusClientException(EMPTY_STRING);
				} catch (PropertyVetoException propertyVetoException) {

				}
			}

			if (editingBudgets && displayedBudgets == 2) {
				throw new CoeusClientException("budget_common_exceptionCode.1010");
			}

			if (maxEditingWindowsOpen) {
				return false;
			}

			return true;
		}
		return false;
	}

	/**
	 * Getter for property hierarchy.
	 * 
	 * @return Value of property hierarchy.
	 */
	public boolean isHierarchy() {
		// return false;
		return hierarchy;
	}

	/**
	 * Getter for property parentProposal.
	 * 
	 * @return Value of property parentProposal.
	 */
	@Override
	public boolean isParentProposal() {
		// return false;
		return parentProposal;
	}

	// Method added with case 2158: Budget Validations - Start
	/**
	 * This method performs validation checks added for budget sub module
	 * validations are perfomed on the budget currently selected as final This
	 * method performs checks and displays any errors/warnings encountered
	 * 
	 * @return true if the budget has validation errors
	 */
	private boolean performValidationChecks() {

		selectBudgetForm.dlgSelectBudgetForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
		selectBudgetForm.dlgSelectBudgetForm.getGlassPane().setVisible(true);
		boolean hasError = false;
		Vector inputVector = new Vector();
		Vector dataObjects = null;
		String leadUnitNumber = propDevFormBean.getOwnedBy();
		inputVector.add(new Integer(ModuleConstants.PROPOSAL_DEV_MODULE_CODE));// modulecode
		inputVector.add(proposalId);// moduleitemkey
		inputVector.add(new Integer(selectBudgetForm.lblFinalVerValue.getText()));// moduleitemkeysequence
		inputVector.add(leadUnitNumber);// unit no
		inputVector.add(new Integer(1));// approval sequence
		inputVector.add(new Integer(ModuleConstants.PROPOSAL_DEV_BUDGET_SUB_MODULE));// sub
																						// module
																						// code
																						// :
																						// 1
																						// for
																						// budget
		String connectTo = CoeusGuiConstants.CONNECTION_URL + "/RoutingServlet";
		RequesterBean request = new RequesterBean();
		request.setFunctionType(VALIDATION_CHECKS);
		request.setDataObjects(inputVector);
		AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
		comm.send();
		ResponderBean response = comm.getResponse();
		if (response != null && response.isSuccessfulResponse()) {
			dataObjects = response.getDataObjects();
		}
		selectBudgetForm.dlgSelectBudgetForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		selectBudgetForm.dlgSelectBudgetForm.getGlassPane().setVisible(false);
		if (dataObjects != null && dataObjects.size() > 0) {
			RoutingValidationChecksForm valChecks = new RoutingValidationChecksForm(CoeusGuiConstants.getMDIForm(),
					dataObjects, 3, proposalId);
			valChecks.display();
			if (valChecks.isError()) {
				hasError = true;
				selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_INCOMPLETE);
			} else {
				int option = CoeusOptionPane.showQuestionDialog(
						coeusMessageResources.parseMessageKey(MSGKEY_VALIDATION_WARNINGS),
						CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
				if (option == JOptionPane.NO_OPTION) {
					selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_INCOMPLETE);
					hasError = true;
				}
			}
		}
		return hasError;
	}

	/**
	 * This method is used to perform the Window closing operation. Before
	 * closing the window it checks the saveRequired flag and the function type.
	 * If the saveRequired is true then it saves the details to the database
	 * else dispose this JDialog.
	 */
	private void performWindowClosing() {

		int option = JOptionPane.NO_OPTION;
		if (getFunctionType() != DISPLAY_MODE) {
			if (isSaveRequired()) {
				option = CoeusOptionPane.showQuestionDialog(
						coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
						CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
				switch (option) {
				case (JOptionPane.YES_OPTION):
					try {
						saveClicked = true;
						saveFormData();
					} catch (Exception e) {
						e.getMessage();
					}
					break;
				case (JOptionPane.NO_OPTION):
					// Release the budget lock before closing
					try {
						releaseBudgetLock();
						selectBudgetForm.dlgSelectBudgetForm.dispose();
					} catch (CoeusClientException coeusClientException) {
						coeusClientException.printStackTrace();
					}
					break;
				case (JOptionPane.CANCEL_OPTION):
					selectBudgetForm.dlgSelectBudgetForm.setVisible(true);
					break;
				}
			} else {
				// Release the budget lock before closing
				try {
					releaseBudgetLock();
					selectBudgetForm.dlgSelectBudgetForm.dispose();
				} catch (CoeusClientException coeusClientException) {
					coeusClientException.printStackTrace();
				}
			}
		} else {
			selectBudgetForm.dlgSelectBudgetForm.dispose();
		}
	}

	/** This method is used to set the listeners to the components. */
	@Override
	public void registerComponents() {

		/** Setting table model for the table */
		selectBudgetForm.tblBudgetVersion.setModel(selectBudgetTableModel);
		selectBudgetForm.tblBudgetVersion.setSelectionForeground(Color.BLACK);

		/** Code for focus traversal - start */
		java.awt.Component[] components = { selectBudgetForm.cmbStatus, selectBudgetForm.btnOk,
				selectBudgetForm.btnCancel, selectBudgetForm.btnNew, selectBudgetForm.btnModify,
				selectBudgetForm.btnDisplay, selectBudgetForm.btnCopy };
		ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy(components);
		selectBudgetForm.setFocusTraversalPolicy(traversePolicy);
		selectBudgetForm.setFocusCycleRoot(true);

		/** Code for focus traversal - end */

		selectBudgetForm.btnOk.addActionListener(this);
		selectBudgetForm.btnCancel.addActionListener(this);
		selectBudgetForm.btnNew.addActionListener(this);
		selectBudgetForm.btnModify.addActionListener(this);
		selectBudgetForm.btnDisplay.addActionListener(this);
		selectBudgetForm.btnCopy.addActionListener(this);

		budgetSelectionModel = selectBudgetForm.tblBudgetVersion.getSelectionModel();
		budgetSelectionModel.addListSelectionListener(this);
		budgetSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectBudgetForm.tblBudgetVersion.setSelectionModel(budgetSelectionModel);
		if (selectBudgetForm.tblBudgetVersion.getRowCount() > 0) {
			selectBudgetForm.btnDisplay.setEnabled(true);
		}

		selectBudgetForm.dlgSelectBudgetForm.addEscapeKeyListener(new AbstractAction("escPressed") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				performWindowClosing();
			}
		});

		selectBudgetForm.dlgSelectBudgetForm.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				performWindowClosing();
				return;
			}
		});

		selectBudgetForm.dlgSelectBudgetForm.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				requestDefaultFocus();
			}
		});

	}// End Register Components

	/**
	 * Releases the budget lock
	 */
	private void releaseBudgetLock() throws CoeusClientException {
		RequesterBean requester = new RequesterBean();
		requester.setFunctionType(RELEASE_BUDGET_LOCK);
		requester.setDataObject(proposalId);
		AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
		comm.send();
		ResponderBean response = comm.getResponse();
		if (!response.isSuccessfulResponse()) {
			throw new CoeusClientException(response.getMessage());
		}
	}

	/**
	 * To set the default focus for the components depending on the function
	 * type
	 */
	private void requestDefaultFocus() {
		if (getFunctionType() == MODIFY_MODE) {
			if (selectBudgetForm.btnOk.isEnabled()) {
				selectBudgetForm.btnOk.requestFocusInWindow();
			} else {
				if (selectBudgetForm.btnNew.isEnabled()) {
					selectBudgetForm.btnNew.requestFocusInWindow();
				} else {
					selectBudgetForm.btnCancel.requestFocusInWindow();
				}
			}
		} else {
			selectBudgetForm.btnCancel.requestFocusInWindow();
		}
	}

	/**
	 * Communicate with the server to update the data obtained from the Proposal
	 * Persons.Call DW_UPDATE_P_BUDGET_PERSONS for updating the data Case Id
	 * 1784
	 */
	private boolean saveBudgetPersons(CoeusVector budgetPersonData) throws CoeusException {
		final String BUDGET_PERSONS = "/BudgetMaintenanceServlet";
		final String connectTo = CoeusGuiConstants.CONNECTION_URL + BUDGET_PERSONS;
		RequesterBean requester = new RequesterBean();
		ResponderBean responder = null;
		requester.setFunctionType(UPDATE_PERSONS);
		requester.setDataObject(budgetPersonData);
		AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
		comm.send();
		responder = comm.getResponse();
		boolean success = false;
		if (responder.isSuccessfulResponse()) {
			success = ((Boolean) responder.getDataObject()).booleanValue();
		} else {
			throw new CoeusException(responder.getMessage());
		}
		return success;
	}

	/**
	 * saves the Form Data.
	 */
	@Override
	public void saveFormData() {
		// Added for COEUSQA-2546- Two users can modify a budget at same time.
		// Locking not working -Start
		budgetLockUpdated = false;
		CoeusVector cvBudgetData = new CoeusVector();
		if (!isSaveRequired()) {
			// Added by chandra to release the lock.
			// To fix the bug #1093
			// Start - 2nd August 2004
			// Added for COEUSQA-3309 Inactive Appointment Type and Period Types
			// error validation alert required in Budget - start
			// If final version exist for budget then while saving check for the
			// inactive types
			// get the budget details for the proposal
			String proposalNumber = null;
			int versionNumber = 0;
			try {
				cvBudgetData = getBudgetInfoForProposal();
			} catch (CoeusClientException ex) {
				ex.printStackTrace();
			}
			if (cvBudgetData != null && cvBudgetData.size() > 0) {
				BudgetInfoBean budgetBean = null;
				boolean isFinalFlag = false;
				for (int index = 0; index < cvBudgetData.size(); index++) {
					budgetBean = (BudgetInfoBean) cvBudgetData.get(index);
					isFinalFlag = budgetBean.isFinalVersion();
					// get the proposalnumber and versionNumber for the final
					// budget
					if (isFinalFlag) {
						versionNumber = budgetBean.getVersionNumber();
						proposalNumber = budgetBean.getProposalNumber();
						break;
					}
				}
			}

			// Get the inactive Appointment Type and Period Type details
			// if allow_budget_save returns true means budget holds inactive
			// Appointment Type and Period Type details
			allow_budget_save = getInactiveAppAndPeriodTypes(proposalNumber, versionNumber);
			if (allow_budget_save && (hasFinalVersion == true || finalVersionChecked == true)) {
				if (vecCEMessages != null && vecCEMessages.size() > 0) {
					displayCENotAvailableMessages();
				}
			}
			// If allow_budget_save is true then allow to save
			if (!allow_budget_save) {
				try {
					releaseBudgetLock();
					// Added for COEUSQA-2546- Two users can modify a budget at
					// same time. Locking not working -Start
					// If Lock has been released, update the variable
					// "budgetLockUpdated" to true.
					budgetLockUpdated = true;
					// Added for COEUSQA-2546- End
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				// End by chandra #1093 - 2nd August 2004
				selectBudgetForm.dlgSelectBudgetForm.dispose();
				return;
			}
		}
		boolean budgetSaved = updateBudgetData();
		// setSaveRequired(false);
		if (budgetSaved == true) {
			setSaveRequired(false);
		} else {
			setSaveRequired(true);
			allow_budget_save = false;
		}

		// Added for COEUSQA-3309 Inactive Appointment Type and Period Types
		// error validation alert required in Budget - end
		if (budgetSaved && saveClicked) {
			selectBudgetForm.dlgSelectBudgetForm.dispose();
		}

	}

	/**
	 * This method is used to set the form data
	 * 
	 * @throws CoeusClientException
	 */
	public void setFormData() throws CoeusClientException {

		if (isBudgetWindowOpen(proposalId, getFunctionType(), true)) {
			return;
		}

		vecBudgetData = getBudgetInfo();
		if (vecBudgetData != null && vecBudgetData.size() > 0) {
			this.budgetInfo = (CoeusVector) vecBudgetData.elementAt(0);
			this.propDevFormBean = (ProposalDevelopmentFormBean) vecBudgetData.elementAt(1);
			/**
			 * Case #1801 :Parameterize Under-recovery and cost-sharing
			 * distribution Start2
			 */
			this.validateCostSharing = ((Boolean) vecBudgetData.elementAt(3)).booleanValue();
			this.validateUndrRecovr = ((Boolean) vecBudgetData.elementAt(4)).booleanValue();
			/**
			 * Case #1801 :Parameterize Under-recovery and cost-sharing
			 * distribution End2
			 */

			budgetStatus = propDevFormBean.getBudgetStatus();
			this.proposalStartDate = propDevFormBean.getRequestStartDateInitial();
			this.proposalEndDate = propDevFormBean.getRequestEndDateInitial();

			this.propDevFormBean = propDevFormBean;
			this.unitNumber = propDevFormBean.getOwnedBy();
			this.activityTypeCode = propDevFormBean.getProposalActivityTypeCode();

			if (budgetInfo != null && budgetInfo.size() > 0) {
				hasBudget = true;
				newVersionNumber = budgetInfo.size() + 1;
				selectBudgetTableModel.setData(budgetInfo);
				selectBudgetRenderer = new SelectBudgetRenderer();
				setPanelData(budgetInfo);
				initialState = true;
				setItemStateChangedListener();
			} else {
				newVersionNumber = 1;
			}
			selectBudgetHeaderRenderer = new SelectBudgetHeaderRenderer();
			setTableEditors();
			setLabelValues();

			// Proposal Hierarchy Enhancment Start
			// gnprh commented for Propsoal Hierarchy start
			if (isOpenedFromPropListWindow) {
				// Get the Propsoal Hierarchy data
				HashMap hierarchyMap = (HashMap) vecBudgetData.elementAt(6);
				// Get the Proposal Hierarchy Tree data
				this.proposalHierarchyBean = (ProposalHierarchyBean) vecBudgetData.elementAt(7);

				/**
				 * Check if the selected proposal is in Hierarchy. If it is in
				 * Hierarchy then check for the parent and then set the values
				 */
				// gnprh
				if (hierarchyMap != null) {
					boolean inHierarchy = ((Boolean) hierarchyMap.get("IN_HIERARCHY")).booleanValue();
					boolean isParent = false;
					if (inHierarchy) {
						setHierarchy(inHierarchy);
						setParentPropNo((String) hierarchyMap.get("PARENT_PROPOSAL"));
						isParent = ((Boolean) hierarchyMap.get("IS_PARENT")).booleanValue();
						if (isParent) {
							setParentProposal(isParent);
							// updateData();
						}
					}
				}
			}
			// Proposal Hierarchy Enhancment End
			formatFields();

		}
	}

	/**
	 * This method is used to set the form data specified in <CODE> data </CODE>
	 * 
	 * @param data
	 *            data to set to the form
	 */
	@Override
	public void setFormData(Object data) {
		/*
		 * this.proposalDetailAdminForm = (ProposalDetailAdminForm)data; Vector
		 * budgetDataFormBean = new Vector(); budgetDataFormBean =
		 * getBudgetInfo(proposalId); if( budgetDataFormBean != null &&
		 * budgetDataFormBean.size() > 0 ){ this.budgetInfo = (Vector)
		 * budgetDataFormBean.elementAt(0); this.propDevFormBean =
		 * (ProposalDevelopmentFormBean) budgetDataFormBean.elementAt(1);
		 * budgetStatus = propDevFormBean.getBudgetStatus();
		 * this.proposalStartDate =
		 * propDevFormBean.getRequestStartDateInitial(); this.proposalEndDate =
		 * propDevFormBean.getRequestEndDateInitial();
		 * 
		 * this.propDevFormBean = propDevFormBean; this.unitNumber =
		 * propDevFormBean.getOwnedBy(); this.activityTypeCode =
		 * propDevFormBean.getProposalActivityTypeCode(); int userRights =
		 * userHasRights( proposalId, RIGHT_ID ); if ( userRights == 1 )
		 * hasRights = true; if( budgetInfo != null && budgetInfo.size() > 0 ){
		 * newVersionNumber = budgetInfo.size() + 1;
		 * selectBudgetTableModel.setData(budgetInfo); selectBudgetRenderer =
		 * new SelectBudgetRenderer(); setPanelData(budgetInfo); initialState =
		 * true; setItemStateChangedListener(); }else{ newVersionNumber = 1; }
		 * selectBudgetHeaderRenderer = new SelectBudgetHeaderRenderer();
		 * setTableEditors(); setLabelValues(); formatFields(); }
		 */
	}

	/**
	 * Setter for property hierarchy.
	 * 
	 * @param hierarchy
	 *            New value of property hierarchy.
	 */
	public void setHierarchy(boolean hierarchy) {
		this.hierarchy = hierarchy;
	}

	/**
	 * Method to get vector of <CODE>BudegetInfoBean</CODE>
	 * 
	 * @return returns a vector of BudgetInfoBeans
	 * @param proposalId
	 *            takes a proposalId
	 */
	// Commented by for right checking : step 2 : start
	/*
	 * private Vector getBudgetInfo( ) throws CoeusClientException {
	 * RequesterBean requester = new RequesterBean(); requester.setFunctionType(
	 * GET_ALL_BUDGETS ); requester.setId(proposalId);
	 * 
	 * //Set the flag whether to lock the budget or not depending on the open
	 * Editing Budgets //and the function type if budget is opened from Proposal
	 * Detail requester.setDataObject(new Boolean( !maxEditingWindowsOpen &&
	 * getFunctionType() != TypeConstants.DISPLAY_MODE));
	 * 
	 * AppletServletCommunicator comm = new
	 * AppletServletCommunicator(CONNECTION_STRING, requester); comm.send();
	 * ResponderBean response = comm.getResponse(); Vector budgetData = new
	 * Vector(); if ( response.isSuccessfulResponse() ){ budgetData =
	 * (Vector)response.getDataObjects(); hasRights =
	 * ((Boolean)budgetData.get(2)).booleanValue(); if( !maxEditingWindowsOpen
	 * && hasRights && getFunctionType() != TypeConstants.DISPLAY_MODE){
	 * setFunctionType(TypeConstants.MODIFY_MODE); }else{
	 * setFunctionType(TypeConstants.DISPLAY_MODE); } }else{ if(
	 * response.isLocked() ){ int option = CoeusOptionPane.showQuestionDialog(
	 * coeusMessageResources.parseMessageKey(BUDGET_LOCKED),
	 * CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES); switch(
	 * option ){ case JOptionPane.YES_OPTION: budgetData =
	 * (Vector)response.getDataObjects();
	 * setFunctionType(TypeConstants.DISPLAY_MODE); break; case
	 * JOptionPane.NO_OPTION: throw new CoeusClientException(""); } }else{ throw
	 * new CoeusClientException(response.getMessage()); }
	 * 
	 * } return budgetData; }
	 */
	// right checking : step 2 : end

	// Added by shiji for Right Checking : step 3 : start
	public void setIsBudgetOpenedFromPropList(boolean isFromPropList) {
		this.isOpenedFromPropListWindow = isFromPropList;
	}

	/** Setting the listener for cmbStatus of the selectBudgetForm */
	public void setItemStateChangedListener() throws CoeusClientException {
		selectBudgetForm.cmbStatus.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					if (getFunctionType() == MODIFY_MODE) {
						if (selectBudgetForm.cmbStatus.getSelectedIndex() == STATUS_COMPLETE) {
							if (initialState) {
								// If the form is just loaded dont do anything
								initialState = false;
								return;
							}
							if (selectBudgetForm.lblFinalVerValue.getText().equals(EMPTY_STRING)) {
								// If no final version is selected, diplay
								// message
								message = coeusMessageResources.parseMessageKey(SELECT_FINAL_VERSION);
								CoeusOptionPane.showInfoDialog(message);
								selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_INCOMPLETE);
								return;
							} else {
								currentFinalVersion = Integer.parseInt(selectBudgetForm.lblFinalVerValue.getText());
								/**
								 * get the bean from the vector corresponding to
								 * the <CODE>currentFinalVersion</CODE>
								 */
								budgetInfoBean = (BudgetInfoBean) budgetInfo.get(--currentFinalVersion);
								hasUnderRecovery = budgetInfoBean.getHasCostSharingDistribution();
								underRecoveryAmount = budgetInfoBean.getUnderRecoveryAmount();
								if (hasUnderRecovery) {
									if (underRecoveryAmount != budgetInfoBean.getTotalIDCRateDistribution()) {
										// case #2456
										// Code commented for case#2938 -
										// Proposal Hierarchy enhancement
										// To display the error message for
										// Parent proposal
										// if(!isParentProposal()){
										message = coeusMessageResources
												.parseMessageKey(UNDERRECOVERY_NOT_EQUALS_TOTAL_UNDERRECOVERY);
										CoeusOptionPane.showInfoDialog(message);
										selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_INCOMPLETE);
										return;
										// }
									}
								} else if (underRecoveryAmount != budgetInfoBean.getTotalIDCRateDistribution()) {
									// Case #1801 :Parameterize Under-recovery
									// and cost-sharing distribution Start7
									if (validateUndrRecovr && underRecoveryAmount > 0.00) {
										// case #2456
										// Code commented for case#2938 -
										// Proposal Hierarchy enhancement
										// To display the error message for
										// Parent proposal
										// if(!isParentProposal()){
										message = coeusMessageResources.parseMessageKey(DISTRIBUTE_UNDERRECOVERY);
										CoeusOptionPane.showInfoDialog(message);
										selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_INCOMPLETE);
										return;
										// }
									} // Case #1801 :Parameterize Under-recovery
										// and cost-sharing distribution End 7
								}
								hasCostSharing = budgetInfoBean.getHasCostSharingDistribution();
								costSharingAmount = budgetInfoBean.getCostSharingAmount();
								if (hasCostSharing) {
									if (costSharingAmount != budgetInfoBean.getTotalCostSharingDistribution()) {
										if (validateCostSharing) {// Parameterize
																	// Under-recovery
																	// and
																	// cost-sharing
																	// distribution
																	// Start 5
											// case #2456
											// Code commented for case#2938 -
											// Proposal Hierarchy enhancement
											// To display the error message for
											// Parent proposal
											// if(!isParentProposal()){
											message = coeusMessageResources
													.parseMessageKey(COST_SHARING_NOT_EQUALS_TOTAL_COST_SHARING);
											CoeusOptionPane.showInfoDialog(message);
											selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_INCOMPLETE);
											return;
											// }
										} // Parameterize Under-recovery and
											// cost-sharing distribution End5
									}
								} else if (costSharingAmount != budgetInfoBean.getTotalCostSharingDistribution()) {
									// Case #1801 :Parameterize Under-recovery
									// and cost-sharing distribution Start6
									if (validateCostSharing) {
										// case #2456
										// Code commented for case#2938 -
										// Proposal Hierarchy enhancement
										// To display the error message for
										// Parent proposal
										// if(!isParentProposal()){
										message = coeusMessageResources.parseMessageKey(CREATE_COST_SHARING);
										CoeusOptionPane.showInfoDialog(message);
										selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_INCOMPLETE);
										return;
										// }
									} // Case #1801:Parameterize Under-recovery
										// and cost-sharing distribution End6
								}
								// Code modified for case#2938 - Proposal
								// Hierarchy enhancement
								if (!isBudgetComplete()) {
									if (isParentProposal()) {
										message = "Please make all child proposal budget status as Complete";
										CoeusOptionPane.showInfoDialog(message);
										selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_INCOMPLETE);
										return;
									}
								}
								// Added for 2158: Run validation checks on
								// changing the budget status to Complete
								if (performValidationChecks()) {
									return;
								}
								// 2158 End
								if (selectBudgetForm.cmbStatus.getSelectedItem().toString() != budgetStatusValue) {
									statusModified = true;
									setSaveRequired(true);
									propDevFormBean.setAcType(UPDATE_RECORD);
									propDevFormBean.setBudgetStatus(COMPLETE);
								}
								completeSelected = true;
								selectBudgetForm.btnOk.setEnabled(true);
								selectBudgetForm.btnDisplay.setEnabled(true);
								selectBudgetForm.btnNew.setEnabled(false);
								selectBudgetForm.btnCopy.setEnabled(false);
								selectBudgetForm.btnModify.setEnabled(false);
							}

							/**
							 * Case Id 1626 - Check for the modular budget
							 * validation Start
							 */
							HashMap modularData = null;
							CoeusVector cvBudgetPeriods = null;
							CoeusVector cvModularBudget = null;
							try {
								if (budgetInfoBean.isBudgetModularFlag()) {
									// Get the HashMap of BudgetPeriods and
									// ModularBudget
									modularData = getModularData(budgetInfoBean);
									cvBudgetPeriods = (CoeusVector) modularData.get(BudgetPeriodBean.class);
									cvModularBudget = (CoeusVector) modularData.get(BudgetModularBean.class);
									if (cvModularBudget != null && cvModularBudget.size() > 0) {
										if (cvBudgetPeriods != null && cvBudgetPeriods.size() > 0) {
											if (cvModularBudget.size() != cvBudgetPeriods.size()) {
												CoeusOptionPane.showInfoDialog(coeusMessageResources
														.parseMessageKey(INCOMPLETE_MODULAR_BUDGET));
												selectBudgetForm.cmbStatus.setSelectedItem(VAL_INCOMPLETE);
												// Code added for case#2938 -
												// Proposal Hierarchy
												// enhancement
												// To set the budget status data
												// to INCOMPLETE
												propDevFormBean.setBudgetStatus(INCOMPLETE);
												if (!isParentProposal()) {
													selectBudgetForm.btnCopy.setEnabled(true);
												}
												selectBudgetForm.btnModify.setEnabled(true);
												return;
											}
										}
									} else {
										// If no Modular Budget data available
										CoeusOptionPane.showInfoDialog(
												coeusMessageResources.parseMessageKey(NO_MODULAR_BUDGET));
										selectBudgetForm.cmbStatus.setSelectedItem(VAL_INCOMPLETE);
										// Code added for case#2938 - Proposal
										// Hierarchy enhancement
										// To set the budget status data to
										// INCOMPLETE
										propDevFormBean.setBudgetStatus(INCOMPLETE);
										if (!isParentProposal()) {
											selectBudgetForm.btnCopy.setEnabled(true);
										}
										selectBudgetForm.btnModify.setEnabled(true);
										return;
									}
								}
							} catch (CoeusClientException ex) {
								ex.printStackTrace();
								CoeusOptionPane.showErrorDialog(ex.getMessage());
							} // End of Case Id 1626
						} else if (selectBudgetForm.cmbStatus.getSelectedIndex() == STATUS_INCOMPLETE) {
							// enable final version flag
							// hasFinalVersion = true;
							if (selectBudgetForm.cmbStatus.getSelectedItem().toString() != budgetStatusValue) {
								statusModified = true;
								setSaveRequired(true);
								propDevFormBean.setAcType(UPDATE_RECORD);
								propDevFormBean.setBudgetStatus(INCOMPLETE);
								selectBudgetForm.btnModify.setEnabled(true);
							}
							// Code added for case#2938 - Proposal Hierarchy
							// enhancement
							// To set the budget status data to INCOMPLETE
							else {
								propDevFormBean.setBudgetStatus(INCOMPLETE);
								selectBudgetForm.btnModify.setEnabled(true);
							}
							if (!isHierarchy() || !isParentProposal()) {
								selectBudgetForm.btnOk.setEnabled(true);
								selectBudgetForm.btnDisplay.setEnabled(true);
								selectBudgetForm.btnNew.setEnabled(true);
								selectBudgetForm.btnCopy.setEnabled(true);
								selectBudgetForm.btnModify.setEnabled(true);
								selectBudgetForm.cmbStatus.setEnabled(true);
							}
							if (completeSelected) {
								selectBudgetForm.tblBudgetVersion.setRowSelectionInterval(0, 0);
								completeSelected = false;
							}
						} else if (selectBudgetForm.cmbStatus.getSelectedIndex() == STATUS_NONE) {

							if (hasBudget) {
								CoeusOptionPane
										.showInfoDialog(coeusMessageResources.parseMessageKey(ERRKEY_NONE_NOT_ALLOWED));
								selectBudgetForm.cmbStatus.setSelectedItem(VAL_INCOMPLETE);
								propDevFormBean.setBudgetStatus(INCOMPLETE);
								return;
							}
							if (selectBudgetForm.cmbStatus.getSelectedItem().toString() != budgetStatusValue) {
								statusModified = true;
								setSaveRequired(true);
								propDevFormBean.setAcType(UPDATE_RECORD);
								propDevFormBean.setBudgetStatus(NONE);
							}
							// Code added for case#2938 - Proposal Hierarchy
							// enhancement
							// To set the budget status data to NONE.
							else {
								propDevFormBean.setBudgetStatus(NONE);
							}

							if (isHierarchy() && isParentProposal()) {
								selectBudgetForm.btnOk.setEnabled(true);
								selectBudgetForm.btnDisplay.setEnabled(true);
								selectBudgetForm.btnNew.setEnabled(false);
								selectBudgetForm.btnCopy.setEnabled(false);
								// Code commented and added for bugfix case#3183
								// - starts
								// enable modify button for the parent proposal
								// selectBudgetForm.btnModify.setEnabled(false);
								selectBudgetForm.btnModify.setEnabled(true);
								// Code commented and added for bugfix case#3183
								// - ends
								selectBudgetForm.cmbStatus.setEnabled(true);
							} else {
								selectBudgetForm.btnOk.setEnabled(true);
								selectBudgetForm.btnDisplay.setEnabled(true);
								if (!maxEditingWindowsOpen) {
									selectBudgetForm.btnNew.setEnabled(true);
								}
								selectBudgetForm.btnCopy.setEnabled(true);
								selectBudgetForm.btnModify.setEnabled(true);
							}

							if (completeSelected) {
								selectBudgetForm.tblBudgetVersion.setRowSelectionInterval(0, 0);
								completeSelected = false;
							}
						}
					}
				}
			}
		});
	}

	/** Sets the values for all the labels and combo box */
	private void setLabelValues() {
		String code = propDevFormBean.getSponsorCode();// ProposalDetailAdminForm.SPONSOR_CODE;
		String desc = propDevFormBean.getSponsorName();// ProposalDetailAdminForm.SPONSOR_DESCRIPTION;
		String sponsorValue = EMPTY_STRING;
		if (desc != null) {
			sponsorValue = code + " : " + desc;
		}
		selectBudgetForm.lblProposalValue.setText(proposalId);
		selectBudgetForm.lblSponsorValue.setText(sponsorValue);

		if (hasFinal > 0) {
			selectBudgetForm.lblFinalVerValue.setText(String.valueOf(existingFinalVersion));
		} else {
			selectBudgetForm.lblFinalVerValue.setText(EMPTY_STRING);
		}

		if (budgetStatus == null)
			return;
		if (budgetStatus.equals(INCOMPLETE)) {
			budgetStatusValue = VAL_INCOMPLETE;
			selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_INCOMPLETE);
		} else if (budgetStatus.equals(COMPLETE)) {
			budgetStatusValue = VAL_COMPLETE;
			selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_COMPLETE);
		} else if (budgetStatus.equals(NONE)) {
			budgetStatusValue = VAL_NONE;
			selectBudgetForm.cmbStatus.setSelectedIndex(STATUS_NONE);
			if (!maxEditingWindowsOpen && getFunctionType() == TypeConstants.DISPLAY_MODE) {
				selectBudgetForm.btnNew.setEnabled(false);
			} else {
				selectBudgetForm.btnNew.setEnabled(true);
			}
			selectBudgetForm.btnCopy.setEnabled(false);
			selectBudgetForm.btnDisplay.setEnabled(false);
			selectBudgetForm.btnModify.setEnabled(false);
			selectBudgetForm.btnOk.setEnabled(false);
		}

	}

	/**
	 * Sets the panel data
	 * 
	 * @param dataBean
	 *            vector of budgetInfoBeans
	 */
	private void setPanelData(Vector dataBean) {
		this.dataBean = dataBean;
		BudgetInfoBean budgetBean = null;
		boolean hasFinalFlag = false;
		for (int index = 0; index < dataBean.size(); index++) {
			budgetBean = (BudgetInfoBean) dataBean.get(index);
			hasFinalFlag = budgetBean.isFinalVersion();
			if (hasFinalFlag) {
				hasFinal++;
				existingFinalVersionRow = index;
				existingFinalVersion = existingFinalVersionRow + 1;
				break;
			}
		}
		if (hasFinal > 0) {
			hasFinalVersion = true;
			selectBudgetForm.tblBudgetVersion.setRowSelectionInterval(existingFinalVersionRow, existingFinalVersionRow);
		} else {
			selectBudgetForm.tblBudgetVersion.setRowSelectionInterval(0, 0);
		}
		updateBudgetDetails(dataBean);
	}

	/**
	 * Setter for property parentPropNo.
	 * 
	 * @param parentPropNo
	 *            New value of property parentPropNo.
	 */
	public void setParentPropNo(java.lang.String parentPropNo) {
		this.parentPropNo = parentPropNo;
	}

	/**
	 * Setter for property parentProposal.
	 * 
	 * @param parentProposal
	 *            New value of property parentProposal.
	 */
	@Override
	public void setParentProposal(boolean parentProposal) {
		this.parentProposal = parentProposal;
		super.setParentProposal(this.parentProposal);
	}

	/**
	 * Setter for property proposalHierarchyBean.
	 * 
	 * @param proposalHierarchyBean
	 *            New value of property proposalHierarchyBean.
	 */
	public void setProposalHierarchyBean(edu.mit.coeus.propdev.bean.ProposalHierarchyBean proposalHierarchyBean) {
		this.proposalHierarchyBean = proposalHierarchyBean;
	}

	/**
	 * Setter for property proposalId.
	 * 
	 * @param proposalId
	 *            New value of property proposalId.
	 */
	public void setProposalId(java.lang.String proposalId) {
		this.proposalId = proposalId;
	}

	/** Method to set the Table Editors like column editor and icon renderer. */
	private void setTableEditors() {
		JTableHeader header = selectBudgetForm.tblBudgetVersion.getTableHeader();
		header.setReorderingAllowed(false);
		header.setResizingAllowed(false);

		// Set icon renderer to the first column of the Budget table
		TableColumn column = selectBudgetForm.tblBudgetVersion.getColumnModel().getColumn(0);
		column.setPreferredWidth(30);
		column.setMaxWidth(30);
		column.setMinWidth(30);
		column.setHeaderRenderer(new EmptyHeaderRenderer());
		column.setCellRenderer(new IconRenderer());

		column = selectBudgetForm.tblBudgetVersion.getColumnModel().getColumn(1);
		column.setMinWidth(50);
		column.setMaxWidth(60);
		column.setPreferredWidth(60);

		column = selectBudgetForm.tblBudgetVersion.getColumnModel().getColumn(2);
		column.setMinWidth(80);
		column.setMaxWidth(80);
		column.setPreferredWidth(80);
		column.setCellRenderer(selectBudgetRenderer);

		column = selectBudgetForm.tblBudgetVersion.getColumnModel().getColumn(3);
		column.setMinWidth(80);
		column.setMaxWidth(80);
		column.setPreferredWidth(80);
		column.setCellRenderer(selectBudgetRenderer);

		column = selectBudgetForm.tblBudgetVersion.getColumnModel().getColumn(4);
		column.setMinWidth(100);
		column.setMaxWidth(120);
		column.setPreferredWidth(100);
		column.setCellRenderer(selectBudgetRenderer);

		column = selectBudgetForm.tblBudgetVersion.getColumnModel().getColumn(5);
		column.setPreferredWidth(45);
		column.setMaxWidth(45);
		column.setMinWidth(45);

		for (int index = 0; index < selectBudgetTableModel.getColumnCount(); index++) {
			column = selectBudgetForm.tblBudgetVersion.getColumnModel().getColumn(index);
			column.setHeaderRenderer(selectBudgetHeaderRenderer);
		}

	}

	public void setUnitNumber(String leadUnit) {
		this.unitNumber = leadUnit;
	}

	// Right Checking : step 3 : end
	/**
	 * Supporting method for saving the Form Data.
	 */
	private boolean updateBudgetData() {

		int newVersion = 0;
		boolean success = false;
		RequesterBean requester = new RequesterBean();
		Vector vecUpdatedBudget = new Vector();
		CoeusVector vecUpdatedBeans = new CoeusVector();
		String newFinalVer = selectBudgetForm.lblFinalVerValue.getText();
		String proposalNumber = null;
		int versionNumber = 0;
		boolean save_budget_modified_version = false;
		boolean newFinal = false;

		if (finalVersionModified) {

			newFinal = newBudgetInfoBean.isFinalVersion();

			if (newFinalVer != EMPTY_STRING) {
				newVersion = Integer.parseInt(newFinalVer);
				newVersion = newVersion - 1;
				for (int index = 0; index < budgetInfo.size(); index++) {
					if (index != newVersion)
						continue;
					newBudgetInfoBean = (BudgetInfoBean) budgetInfo.get(index);
					newBudgetInfoBean.setFinalVersion(newFinal);
					budgetInfo.set(index, newBudgetInfoBean);
					break;
				}
			}

			if (hasFinalVersion) {
				if (finalVersionChecked) {
					if (existingFinalVersionRow != newVersion) {
						vecUpdatedBeans.addElement(budgetInfo.get(existingFinalVersionRow));
						vecUpdatedBeans.addElement(budgetInfo.get(newVersion));
					}
				} else {
					vecUpdatedBeans.addElement(budgetInfo.get(existingFinalVersionRow));
				}
			} else {
				vecUpdatedBeans.addElement(budgetInfo.get(newVersion));
			}
		}

		/**
		 * If final version is modified vecUpdatedBudget contains budgetInfoBean
		 * as the first element, otherwise it contains an empty vector
		 */
		/**
		 * Added by chandra. Update the budget status in the detail form
		 * whenever the user makes the status as complete. check if the parent
		 * or detail window is opened. If opened update immediately otherwise
		 * save to the database dire4ctly. Bug Fix #576 & #972 - start 30th June
		 * 2004
		 */
		String status = propDevFormBean.getBudgetStatus();
		CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, proposalId);
		if (frame != null) {
			if (frame.isEnabled()) {
				if (status != null) {
					if (status.trim().equals(COMPLETE)) {
						((ProposalDetailForm) frame).setBudgetStatusCode(COMPLETE);
					} else if (status.trim().equals(INCOMPLETE)) {
						((ProposalDetailForm) frame).setBudgetStatusCode(INCOMPLETE);
					} else if (status.trim().equals(NONE)) {
						((ProposalDetailForm) frame).setBudgetStatusCode(NONE);
					}
				}
			}
		}
		// End chandra - end 30th June 2004
		vecUpdatedBudget.addElement(vecUpdatedBeans);
		vecUpdatedBudget.addElement(propDevFormBean);
		// Added for COEUSQA-3309 Inactive Appointment Type and Period Types
		// error validation alert required in Budget - start
		if (vecUpdatedBeans != null && vecUpdatedBeans.size() > 0) {
			BudgetInfoBean budgetBean = null;
			boolean isFinalFlag = false;
			for (int index = 0; index < vecUpdatedBeans.size(); index++) {
				budgetBean = (BudgetInfoBean) vecUpdatedBeans.get(index);
				isFinalFlag = budgetBean.isFinalVersion();
				// get the proposalnumber and versionNumber for the final budget
				if (isFinalFlag) {
					versionNumber = budgetBean.getVersionNumber();
					proposalNumber = budgetBean.getProposalNumber();
					// break it if proposal budget have final version
					break;
				}
			}
		}

		// get proposalnumber and versionnumber to list out the inactive types
		if (proposalNumber == null || versionNumber == 0) {
			CoeusVector cvBudgetData = new CoeusVector();
			try {
				cvBudgetData = getBudgetInfoForProposal();
			} catch (CoeusClientException ex) {
				ex.printStackTrace();
			}
			if (cvBudgetData != null && cvBudgetData.size() > 0) {
				BudgetInfoBean budgetBean = null;
				boolean isFinalFlag = false;
				for (int index = 0; index < cvBudgetData.size(); index++) {
					budgetBean = (BudgetInfoBean) cvBudgetData.get(index);
					isFinalFlag = budgetBean.isFinalVersion();
					// get the proposalnumber and versionNumber for the final
					// budget
					if (isFinalFlag) {
						versionNumber = budgetBean.getVersionNumber();
						proposalNumber = budgetBean.getProposalNumber();
						break;
					}
				}
			}
		}

		// Get the inactive Appointment Type and Period Type details
		// if allow_budget_save returns true means budget holds inactive
		// Appointment Type and Period Type details
		save_budget_modified_version = getInactiveAppAndPeriodTypes(proposalNumber, versionNumber);
		if (save_budget_modified_version && finalVersionChecked == true) {
			if (vecCEMessages != null && vecCEMessages.size() > 0) {
				displayCENotAvailableMessages();
			}
			save_budget_modified_version = false;
		} else {
			// if getInactiveAppAndPeriodTypes returns false then there are no
			// inactive types
			// and set save_budget_modified_version as true to save the budget
			save_budget_modified_version = true;
		}

		// Hard stop should display only when ok button is clicked
		// finalversion is checked and trying to change only the status then if
		// there are inactive types then it should give hardstop
		if (OkAction) {
			if (finalVersionModified && newFinal) {
				if (save_budget_modified_version && ("I".equals(status) || "C".equals(status)) && !allow_budget_save) {
					if (vecCEMessages != null && vecCEMessages.size() > 0) {
						displayCENotAvailableMessages();
						save_budget_modified_version = false;
					} else {
						save_budget_modified_version = true;
					}
				}
			}

			if (!finalVersionModified && hasFinalVersion) {
				if (save_budget_modified_version && ("I".equals(status) || "C".equals(status)) && !allow_budget_save) {
					if (vecCEMessages != null && vecCEMessages.size() > 0) {
						displayCENotAvailableMessages();
						save_budget_modified_version = false;
					} else {
						save_budget_modified_version = true;
					}
				}
			}
		}

		// Added for COEUSQA-3309 Inactive Appointment Type and Period Types
		// error validation alert required in Budget - end
		if (save_budget_modified_version && !allow_budget_save) {
			requester.setFunctionType(UPDATE_STATUS_FINAL_VERSION);
			requester.setId(proposalId);
			requester.setDataObjects(vecUpdatedBudget);
			// String connectTo = connectionURL + conURL;
			AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
			comm.send();

			ResponderBean response = comm.getResponse();
			if (response != null) {
				if (response.isSuccessfulResponse()) {
					success = true;
					if (statusModified) {
						modifiedPropDevFormBean = (ProposalDevelopmentFormBean) response.getDataObject();
						// proposalDetailAdminForm.setProposalDevelopmentFormBean(modifiedPropDevFormBean);
					}
				} else {
					message = coeusMessageResources.parseMessageKey(BUDGET_NOT_SAVED);
					CoeusOptionPane.showErrorDialog(message);
				}
			}
		}
		// if flag is not checked then hard stop is not required and return the
		// value for allow_budget_save as true
		allow_budget_save = true;
		return success;
	}

	/**
	 * Supporting method for setPanelData
	 * 
	 * @param dataBean
	 *            vector of budgetInfoBeans
	 */
	private void updateBudgetDetails(Vector budgetData) {

		int selectedRow = selectBudgetForm.tblBudgetVersion.getSelectedRow();

		String version = selectBudgetForm.tblBudgetVersion.getValueAt(selectedRow, 1) == null ? EMPTY_STRING
				: selectBudgetForm.tblBudgetVersion.getValueAt(selectedRow, 1).toString();
		selectedVersion = Integer.parseInt(version);
		if (version != null && selectedRow >= 0) {

			BudgetInfoBean budgetBean = (BudgetInfoBean) budgetData.get(selectedRow);
			selectBudgetForm.txtDirectCost.setValue(budgetBean.getTotalDirectCost());
			selectBudgetForm.txtIndirectCost.setValue(budgetBean.getTotalIndirectCost());
			selectBudgetForm.txtCostSharing.setValue(budgetBean.getCostSharingAmount());
			selectBudgetForm.txtUnderRecovery.setValue(budgetBean.getUnderRecoveryAmount());
			selectBudgetForm.txtResidualFunds.setValue(budgetBean.getResidualFunds());
			selectBudgetForm.txtOHRateType.setText(budgetBean.getOhRateClassDescription());
			selectBudgetForm.txtArComments.setText(budgetBean.getComments());
			selectBudgetForm.txtArComments.setCaretPosition(0);
			selectBudgetForm.txtLastUpdated.setText(CoeusDateFormat.format(budgetBean.getUpdateTimestamp().toString()));
			// selectBudgetForm.txtUpdatedUser.setText(
			// budgetBean.getUpdateUser() );
			/*
			 * UserID to UserName Enhancement - Start Added UserUtils class to
			 * change userid to username
			 */
			selectBudgetForm.txtUpdatedUser.setText(UserUtils.getDisplayName(budgetBean.getUpdateUser()));
			// UserId to UserName Enhancement - End
		}
	}

	/**
	 * validate the form data/Form and returns true if validation is through
	 * else returns false.
	 * 
	 * @throws CoeusUIException
	 *             if some exception occurs or some validation fails.
	 * @return true if validation is through else returns false.
	 */
	@Override
	public boolean validate() throws CoeusUIException {
		return true;
	}

	/**
	 * This method sets the panel data based on the valueChanged of
	 * listSelectionEvent
	 * 
	 * @param listSelectionEvent
	 *            takes the listSelectionEvent
	 */
	@Override
	public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
		Object source = listSelectionEvent.getSource();
		int selectedRow = selectBudgetForm.tblBudgetVersion.getSelectedRow();
		if ((source.equals(budgetSelectionModel)) && (selectedRow >= 0) && (budgetInfo != null)) {
			String version = selectBudgetForm.tblBudgetVersion.getValueAt(selectedRow, 1) == null ? EMPTY_STRING
					: selectBudgetForm.tblBudgetVersion.getValueAt(selectedRow, 1).toString();
			selectedVersion = Integer.parseInt(version);
			if (version != null) {
				BudgetInfoBean budgetRow = null;
				budgetRow = (BudgetInfoBean) budgetInfo.get(selectedRow);
				selectBudgetForm.txtDirectCost.setValue(budgetRow.getTotalDirectCost());
				selectBudgetForm.txtIndirectCost.setValue(budgetRow.getTotalIndirectCost());
				selectBudgetForm.txtCostSharing.setValue(budgetRow.getCostSharingAmount());
				selectBudgetForm.txtUnderRecovery.setValue(budgetRow.getUnderRecoveryAmount());
				selectBudgetForm.txtResidualFunds.setValue(budgetRow.getResidualFunds());
				selectBudgetForm.txtOHRateType.setText(budgetRow.getOhRateClassDescription());
				selectBudgetForm.txtArComments.setText(budgetRow.getComments());
				selectBudgetForm.txtArComments.setCaretPosition(0);
				selectBudgetForm.txtLastUpdated
						.setText(CoeusDateFormat.format(budgetRow.getUpdateTimestamp().toString()));
				// selectBudgetForm.txtUpdatedUser.setText(
				// budgetRow.getUpdateUser() );
				/*
				 * UserID to UserName Enhancement - Start Added UserUtils class
				 * to change userid to username
				 */
				// selectBudgetForm.txtUpdatedUser.setText(
				// budgetRow.getUpdateUser() );
				selectBudgetForm.txtUpdatedUser.setText(UserUtils.getDisplayName(budgetRow.getUpdateUser()));
				// UserId to UserName Enhancement - End
			}
		}
	}

}// End Controller
