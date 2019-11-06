/*
 * AwardDetailController.java
 *
 * Created on March 18, 2004, 1:53 PM
 */

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 27-OCT-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeus.award.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author  sharathk
 */

import edu.mit.coeus.award.AwardConstants;
import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardCustomDataBean;
import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.award.bean.AwardHeaderBean;
import edu.mit.coeus.award.bean.ValidBasisPaymentBean;
import edu.mit.coeus.award.gui.AwardDetailForm;
import edu.mit.coeus.award.gui.AwardDetailSyncForm;
import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.gui.event.BeanUpdatedListener;
import edu.mit.coeus.sponsormaint.gui.SponsorMaintenanceForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.StrictEquals;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UserUtils;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;

/** Controller for Award Detail Form. */
public class AwardDetailController extends AwardController
		implements ActionListener, FocusListener, MouseListener, ItemListener, BeanUpdatedListener {

	// Added for COEUSQA-1434 : Add the functionality to set a status on a
	// Sponsor record - start
	private static final String INACTIVE_STATUS = "I";

	// Start Sub Plan Constants
	/** Required. */
	private static final String REQ = "Y";

	/** not required. */
	private static final String NOT_REQ = "N";


	/** required - Description */
	private static final String REQ_DESC = "Yes";

	/** not required - description. */
	private static final String NOT_REQ_DESC = "No";


	// End Sub Plan Constants

	private static final String DATE_SEPARATERS = ":/.,|-";

	private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";

	private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";

	/** Invalid Sponsor Code.Please enter a valid sponsor code. */
	private static final String INVALID_SPONSOR_CODE = "awardDetail_exceptionCode.1051";
	/** Please enter a valid Effective Date. */
	private static final String INVALID_EFFECTIVE_DATE = "awardDetail_exceptionCode.1052";

	/** Please enter a valid Execution Date. */
	private static final String INVALID_EXECUTION_DATE = "awardDetail_exceptionCode.1053";
	/** Please enter a valid Begin Date. */
	private static final String INVALID_BEGIN_DATE = "awardDetail_exceptionCode.1054";
	/** Please select a status. */
	private static final String SELECT_STATUS = "awardDetail_exceptionCode.1055";

	/** Please enter a sponsor award number */
	private static final String ENTER_SPONSOR_AWARD_NUM = "awardDetail_exceptionCode.1056";
	/** Please enter an effective date */
	private static final String ENTER_EFFECTIVE_DATE = "awardDetail_exceptionCode.1057";
	/** Please enter a valid Pre-Award Effective Date. */
	private static final String INVALID_PRE_EFFECTIVE_DATE = "awardDetail_exceptionCode.1058";

	/** Please enter a valid sponsor code. */
	private static final String ENTER_VALID_SPONSOR_CODE = "awardDetail_exceptionCode.1059";
	/** Please select an activity type. */
	private static final String SELECT_ACTIVITY_TYPE = "awardDetail_exceptionCode.1060";

	/**
	 * Malini:12/2/15 Please select review code.
	 */
	private static final String SELECT_REVIEW_CODE = "awardDetail_exceptionCode.1070";

	// Malini:12/2/15 Please select review date.
	private static final String SELECT_REVIEW_DATE = "awardDetail_exceptionCode.1071";

	/** Please select an award type. */
	private static final String SELECT_AWARD_TYPE = "awardDetail_exceptionCode.1061";

	/**
	 * Current values of Award Type and Basis of Payment is an invalid
	 * combination.
	 */
	private static final String INVALID_COMBINATION = "awardDetail_exceptionCode.1062";

	/**
	 * Invalid Pre-Award Authorized Amount. Amount should be < 1,000,000,000.
	 */
	private static final String INVALID_PRE_AWARD_AUTH_AMOUNT = "awardDetail_exceptionCode.1063";

	/**
	 * Pre-Award Effective date is mandatory if Pre-Award Authorized Amount is
	 * Non NULL.
	 */
	private static final String ENTER_PRE_EFFECTIVE_DATE = "awardDetail_exceptionCode.1064";

	/** Please enter the title. */
	private static final String ENTER_TITLE = "awardDetail_exceptionCode.1065";

	/* For Bug Fix:1666 start step:1 start */
	private static final char GET_VALID_SPONSOR_CODE = 'P';

	private static final String ROLODEX_SERVLET = "/rolMntServlet";

	private static final String EMPTY_STRING = "";
	/* step:1 end */

	// Added with COEUSDEV-563:Award Sync to Parent is not triggering SAP feed
	// for child accounts its touching
	// Message:Do you want to create SAP Feed for all the child awards?
	private static final String MSGKEY_SAP_FEED_REQUIRED = "awardDetail_exceptionCode.1069";

	// Malini 12/16/15 added to validate for inconsistent date in both fields
	private static final String ENTER_REVIEW_CODE = "awardDetail_exceptionCode.1070";
	private static final String ENTER_REVIEW_DATE = "awardDetail_exceptionCode.1071";
	private static final String INVALID_REVIEW_DATE = "awardDetail_exceptionCode.1072";

	// Malini
	// JM 6-21-2010 added for txtAccount length customization
	private static final int TXTACCOUNT_LENGTH = 10;
	// END

	// JM 6-21-2011 changed this type from private to protected to allow testing
	// and exception handling
	/** award detail form - which this controls. */
	protected AwardDetailForm awardDetailForm = new AwardDetailForm();
	// END

	/** Award Details Bean */
	private AwardDetailsBean awardDetailsBean;

	/** Award Header Bean. */
	private AwardHeaderBean awardHeaderBean;

	// JM 6-18-2011 added bean for NIH mechanism field
	/** Custom data bean **/
	private AwardCustomDataBean awardCustomDataBean;
	// END

	/** Date utils. */
	private DateUtils dateUtils;

	/** Simple Date Format. */
	private SimpleDateFormat simpleDateFormat;

	/** Message Resource. */
	private CoeusMessageResources coeusMessageResources;
	/** Query Engine instance. */
	private QueryEngine queryEngine;
	/** Components in this GUI. */
	private Component components[];

	/** refresh required flag. */
	private boolean refreshRequired;
	private String status = "";
	// Added for COEUSQA-1434 : Add the functionality to set a status on a
	// Sponsor record - end

	// Bug Fix:Performance Issue (Out of memory) Start 1
	private JScrollPane jscrpn;
	// Bug Fix:Performance Issue (Out of memory) End 1
	private int accountNumberMaxLength = 0;

	// COEUSDEV-563: End
	boolean sponsorChanged = false;

	/**
	 * Creates a new instance of AwardDetailController
	 *
	 * @param awardBaseBean
	 *            award base bean
	 * @param functionType
	 *            function type in which this form is being opened.
	 */
	public AwardDetailController(AwardBaseBean awardBaseBean, char functionType) {
		super(awardBaseBean);
		// JIRA Case COEUSDEV-160, COEUSDEV-177 - START
		jscrpn = new JScrollPane(awardDetailForm);
		// JM 4-10-2012 add listener to pass control to outer pane for scrolling
		jscrpn.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				jscrpn.getParent().dispatchEvent(e);
			}
		});

		// JIRA Case COEUSDEV-160, COEUSDEV-177 - END
		/*
		 * if(functionType == NEW_AWARD) { awardHeaderBean = new
		 * AwardHeaderBean();
		 * awardHeaderBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
		 * awardHeaderBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
		 * }
		 */

		// initComponents();
		// registerComponents();
		// setFunctionType(functionType);
		// setFormData(awardBaseBean);

	}

	/**
	 * listens to action events.
	 *
	 * @param actionEvent
	 *            actionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		Object source = actionEvent.getSource();
		if (source.equals(awardDetailForm.btnSponsor)) {
			displaySponsorSearch();
		}

		// Commented for COEUSQA-1434 : Add the functionality to set a status on
		// a Sponsor record - Start
		// else if(source.equals(awardDetailForm.txtSponsor)) {
		// String sponsorName;
		// try{
		// sponsorName= getSponsorName(awardDetailForm.txtSponsor.getText());
		// if(sponsorName.equals(EMPTY)) {
		// sponsorName = EMPTY;
		// }
		// }catch (CoeusException coeusException) {
		// coeusException.printStackTrace();
		// sponsorName = EMPTY;
		// }
		// awardDetailForm.lblSponsorDesc.setText(sponsorName);
		// }
		// Commented for COEUSQA-1434 : Add the functionality to set a status on
		// a Sponsor record - end

		/*
		 * else if(source.equals(awardDetailForm.chkSubPlan)) { ++subPlanClick;
		 * subPlanClick = subPlanClick % 3;
		 * awardDetailForm.chkSubPlan.setBorderPaintedFlat(false); switch
		 * (subPlanClick) { case UNKNOWN_NUM: //Select Unknown
		 * awardDetailForm.chkSubPlan.setText(UNKNOWN_DESC);
		 * awardDetailForm.chkSubPlan.setBorderPaintedFlat(true);
		 * awardDetailForm.chkSubPlan.setSelected(false);
		 * awardHeaderBean.setSubPlanFlag(UNKNOWN); break; case REQ_NUM:
		 * //Select Required awardDetailForm.chkSubPlan.setText(REQ_DESC);
		 * awardDetailForm.chkSubPlan.setSelected(true);
		 * awardHeaderBean.setSubPlanFlag(REQ); break; case NOT_REQ_NUM:
		 * //Select Not Required
		 * awardDetailForm.chkSubPlan.setText(NOT_REQ_DESC);
		 * awardDetailForm.chkSubPlan.setSelected(false);
		 * awardHeaderBean.setSubPlanFlag(NOT_REQ); break; } }
		 */
	}

	/**
	 * listens to bean updated event.
	 *
	 * @param beanEvent
	 *            beanEvent
	 */
	@Override
	public void beanUpdated(BeanEvent beanEvent) {
		Object source = beanEvent.getSource();
		if (source.equals(this)) {
			return;
		}

		// if event is fired from FundingProposalController/Award Baase Window
		// Controller award has to be set to hold
		if (beanEvent.getBean().getClass().equals(AwardDetailsBean.class)
				&& (beanEvent.getSource().getClass().equals(FundingProposalsController.class)
						|| beanEvent.getSource().getClass().equals(AwardBaseWindowController.class))) {
			AwardDetailsBean bean = (AwardDetailsBean) beanEvent.getBean();
			// 6 = HOLD
			if (bean.getStatusCode() == 6) {
				// bean has to be put on hold
				ComboBoxBean selItem = new ComboBoxBean("6", "Hold");
				awardDetailForm.cmbStatus.setSelectedItem(selItem);
				return;
			} else {
				setRefreshRequired(true);
				refresh();
			}
			return;
		} // Award details bean - funding prop source

		if (beanEvent.getBean().getClass().equals(AwardHeaderBean.class)
				|| beanEvent.getBean().getClass().equals(AwardDetailsBean.class)) {
			setRefreshRequired(true);
		}

	}

	// Bug Fix:Performance Issue (Out of memory) Start 3
	@Override
	public void cleanUp() {
		// Unregstiring Listeners
		awardDetailForm.btnSponsor.removeActionListener(this);
		awardDetailForm.txtSponsor.removeFocusListener(this);
		awardDetailForm.txtSponsor.removeActionListener(this);
		awardDetailForm.txtSponsor.removeMouseListener(this);
		awardDetailForm.txtEffectiveDate.removeFocusListener(this);
		awardDetailForm.txtExecutionDate.removeFocusListener(this);
		awardDetailForm.txtBeginDate.removeFocusListener(this);
		awardDetailForm.txtDfafsNo.removeFocusListener(this);
		awardDetailForm.txtPreAwardEffectiveDate.removeFocusListener(this);
		awardDetailForm.cmbAwardType.removeItemListener(this);
		awardDetailForm.txtCfdaNo.removeFocusListener(this);
		awardDetailsBean = null;
		awardHeaderBean = null;
		dateUtils = null;
		simpleDateFormat = null;
		coeusMessageResources = null;
		components = null;
		jscrpn.remove(awardDetailForm);
		awardDetailForm = null;
		jscrpn = null;
		removeBeanUpdatedListener(this, AwardHeaderBean.class);
		removeBeanUpdatedListener(this, AwardDetailsBean.class);
	}

	/** displays this GUI */
	@Override
	public void display() {
	}

	/** displays sponsor search. */
	public void displaySponsorSearch() {
		try {
			int click = sponsorSearch();
			if (click == CANCEL_CLICKED) {
				return;
			}
			awardDetailForm.txtSponsor.setText(getSponsorCode());
			awardDetailForm.lblSponsorDesc.setText(getSponsorName());

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * listens to focus gained event.
	 *
	 * @param focusEvent
	 *            focusEvent
	 */
	@Override
	public void focusGained(FocusEvent focusEvent) {
		if (focusEvent.isTemporary())
			return;

		Object source = focusEvent.getSource();

		// Bug Fix : 1019 - START
		if (source.equals(awardDetailForm.txtCfdaNo)) {
			awardDetailForm.txtCfdaNo.setCaretPosition(0);
		}
		// Bug Fix : 1019 - END

		if (source.equals(awardDetailForm.txtEffectiveDate)) {
			String effectiveDate = awardDetailForm.txtEffectiveDate.getText();
			effectiveDate = dateUtils.restoreDate(effectiveDate, DATE_SEPARATERS);
			awardDetailForm.txtEffectiveDate.setText(effectiveDate);
		} // End Effective date.
		else if (source.equals(awardDetailForm.txtExecutionDate)) {
			String executionDate;
			executionDate = awardDetailForm.txtExecutionDate.getText().trim();
			executionDate = dateUtils.restoreDate(executionDate, DATE_SEPARATERS);
			awardDetailForm.txtExecutionDate.setText(executionDate);

		} // End Execution date.

		else if (source.equals(awardDetailForm.txtBeginDate)) {
			String beginDate;
			beginDate = awardDetailForm.txtBeginDate.getText();
			beginDate = dateUtils.restoreDate(beginDate, DATE_SEPARATERS);
			awardDetailForm.txtBeginDate.setText(beginDate);
		} // End Begin date.
		else if (source.equals(awardDetailForm.txtPreAwardEffectiveDate)) {
			String effectiveDate;
			effectiveDate = awardDetailForm.txtPreAwardEffectiveDate.getText();
			effectiveDate = dateUtils.restoreDate(effectiveDate, DATE_SEPARATERS);
			awardDetailForm.txtPreAwardEffectiveDate.setText(effectiveDate);
		} // End Pre Award Effective Date

		else if (source.equals(awardDetailForm.txtDfafsNo)) {
			String reviewDate;
			reviewDate = awardDetailForm.txtDfafsNo.getText();
			reviewDate = dateUtils.restoreDate(reviewDate, DATE_SEPARATERS);
			awardDetailForm.txtDfafsNo.setText(reviewDate);
		}

	}

	/**
	 * listsns to focus lost event.
	 *
	 * @param focusEvent
	 *            focusEvent
	 */
	@Override
	public void focusLost(FocusEvent focusEvent) {

		if (focusEvent.isTemporary())
			return;

		Object source = focusEvent.getSource();

		if (source.equals(awardDetailForm.txtSponsor)) {
			String sponsorName = CoeusGuiConstants.EMPTY_STRING;
			try {
				// Modified for COEUSQA-1434 : Add the functionality to set a
				// status on a Sponsor record - Start
				// sponsorName=
				// getSponsorName(awardDetailForm.txtSponsor.getText());
				// if(sponsorName.equals(EMPTY)) {
				// sponsorName = EMPTY;
				// }
				validateSponsorCode();
				// Modified for COEUSQA-1434 : Add the functionality to set a
				// status on a Sponsor record - End
			} catch (CoeusException coeusException) {
				coeusException.printStackTrace();
			}
			// awardDetailForm.lblSponsorDesc.setText(sponsorName);
		} // End Sponsor Focus Lost
		else if (source.equals(awardDetailForm.txtEffectiveDate)) {
			String effectiveDate;
			effectiveDate = awardDetailForm.txtEffectiveDate.getText().trim();

			if (effectiveDate.equals(EMPTY))
				return;

			effectiveDate = dateUtils.formatDate(effectiveDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);

			if (effectiveDate == null) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_EFFECTIVE_DATE));
				setRequestFocusInThread(awardDetailForm.txtEffectiveDate);
				// awardDetailForm.txtEffectiveDate.requestFocusInWindow();
			} else {
				awardDetailForm.txtEffectiveDate.setText(effectiveDate);
			}
		} // End Effective date validation.
		else if (source.equals(awardDetailForm.txtExecutionDate)) {
			String executionDate;
			executionDate = awardDetailForm.txtExecutionDate.getText().trim();

			if (executionDate.equals(EMPTY))
				return;

			executionDate = dateUtils.formatDate(executionDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
			if (executionDate == null) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_EXECUTION_DATE));
				setRequestFocusInThread(awardDetailForm.txtExecutionDate);
				// awardDetailForm.txtExecutionDate.requestFocusInWindow();
			} else {
				awardDetailForm.txtExecutionDate.setText(executionDate);
			}
		} // End Execution date validation.

		else if (source.equals(awardDetailForm.txtBeginDate)) {
			String beginDate;
			beginDate = awardDetailForm.txtBeginDate.getText();

			if (beginDate.equals(EMPTY))
				return;

			beginDate = dateUtils.formatDate(beginDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
			if (beginDate == null) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_BEGIN_DATE));
				setRequestFocusInThread(awardDetailForm.txtBeginDate);
				// awardDetailForm.txtBeginDate.requestFocusInWindow();
			} else {
				awardDetailForm.txtBeginDate.setText(beginDate);
			}
		} // End Begin date validation

		else if (source.equals(awardDetailForm.txtPreAwardEffectiveDate)) {
			String preEffectiveDate;
			preEffectiveDate = awardDetailForm.txtPreAwardEffectiveDate.getText().trim();
			if (!preEffectiveDate.equals(EMPTY)) {
				String strDate1 = dateUtils.formatDate(preEffectiveDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
				if (strDate1 == null) {
					strDate1 = dateUtils.restoreDate(preEffectiveDate, DATE_SEPARATERS);
					if (strDate1 == null || strDate1.equals(preEffectiveDate)) {
						CoeusOptionPane
								.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_PRE_EFFECTIVE_DATE));
						setRequestFocusInThread(awardDetailForm.txtPreAwardEffectiveDate);
						return;
					}
				} else {
					preEffectiveDate = strDate1;
					awardDetailForm.txtPreAwardEffectiveDate.setText(preEffectiveDate);

				}
			}

		} // End Pre Award Effective date validation.

		else if (source.equals(awardDetailForm.txtDfafsNo)) {
			String reviewDate;
			reviewDate = awardDetailForm.txtDfafsNo.getText().trim();
			if (!reviewDate.equals(EMPTY)) {
				String strDate1 = dateUtils.formatDate(reviewDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);

				if (strDate1 == null) {
					CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_REVIEW_DATE));
					awardDetailForm.txtDfafsNo.setText(EMPTY);
					setRequestFocusInThread(awardDetailForm.txtDfafsNo);
					return;

				} else {
					reviewDate = strDate1;
					awardDetailForm.txtDfafsNo.setText(reviewDate);

				}
			}

			else
				return;

		} // End review date validation.

	}

	/** formats components. */
	@Override
	public void formatFields() {
		Color disabledBackground = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");

		// These Fields are always disabled
		Component disabledComponents[] = { awardDetailForm.txtAward, awardDetailForm.txtSequence,
				awardDetailForm.txtLastUpdate, awardDetailForm.txtUpdateUser };
		for (int count = 0; count < disabledComponents.length; count++) {
			disabledComponents[count].setEnabled(false);

			// BUG - FIX:1032 START
			if (disabledComponents[count] instanceof CoeusTextField) {
				((CoeusTextField) disabledComponents[count]).setEditable(false);
			}
			// BUG - FIX:1032 END

			disabledComponents[count].setBackground(disabledBackground);
		}

		// If in display mode disable editable fields else enable.
		boolean enabled = !(getFunctionType() == DISPLAY_MODE);
		// Disable button if in display mode.
		awardDetailForm.btnSponsor.setEnabled(enabled);
		for (int count = 0; count < components.length; count++) {
			components[count].setEnabled(enabled);
			if (enabled && !(components[count] instanceof JCheckBox)) {
				// JM 7-22-2011 removed the following to allow required field
				// highlighting
				// components[count].setBackground(Color.white);
				// END
			} else {
				components[count].setBackground(disabledBackground);
			}

			// BUG - FIX:1032 START
			if (!enabled && (components[count] instanceof JTextField)) {
				((JTextField) components[count]).setEditable(false);
				((JTextField) components[count]).setBackground(disabledBackground);
				((JTextField) components[count]).setDisabledTextColor(Color.black);
			}
			// BUG - FIX:1032 END
		}
		// BUG_FIX START
		if (getFunctionType() == DISPLAY_MODE) {
			awardDetailForm.txtArTitle.setDisabledTextColor(Color.BLACK);
			// Added for case#3687 - ctrl + c does not work in Award module - QA
			// - start
			awardDetailForm.txtArTitle.setEnabled(true);
			awardDetailForm.txtArTitle.setEditable(false);
			// Added for case#3687 - ctrl + c does not work in Award module - QA
			// - end
		}
		// BUG_FIX END
	}

	// Added for Case#3893 - Java 1.5 issues - Start
	/**
	 * returns awardDetailForm
	 *
	 * @return awardDetailForm
	 */
	public AwardDetailForm getAwardDetailForm() {
		return awardDetailForm;
	}
	// Case#3893 - End

	/**
	 * returns controlled UI.
	 *
	 * @return Controlled UI
	 */
	@Override
	public Component getControlledUI() {

		// Bug Fix:Performance Issue (Out of memory) Start 2
		// return awardDetailForm;
		// JIRA Case COEUSDEV-160, COEUSDEV-177 - START
		// jscrpn = new JScrollPane(awardDetailForm);
		// JIRA Case COEUSDEV-160, COEUSDEV-177 - END
		return jscrpn;
		// Bug Fix:Performance Issue (Out of memory) End 2
	}

	/**
	 * returns form data.
	 *
	 * @return returns form data.
	 */
	@Override
	public Object getFormData() {
		return awardBaseBean;
	}

	/**
	 * added for the Bug Fix:1666 for alpha numeric sponsor code start step:3
	 * contacts the server and fetches the valid Sponsor code for the sponsor
	 * code. returns "" if sponsor code is invalid.
	 *
	 * @return sponsor code
	 * @param sponsorCode
	 *            sponsor code for which valid sponsor code has to be retrieved.
	 * @throws CoeusException
	 *             if cannot contact server or if server error occurs.
	 */
	private String getValidSponsorCode(String sponsorCode) throws CoeusException {
		RequesterBean requesterBean = new RequesterBean();
		requesterBean.setFunctionType(GET_VALID_SPONSOR_CODE);
		requesterBean.setDataObject(sponsorCode);

		AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
		appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET);
		appletServletCommunicator.setRequest(requesterBean);
		appletServletCommunicator.send();
		ResponderBean responderBean = appletServletCommunicator.getResponse();

		if (responderBean == null) {
			// Could not contact server.
			throw new CoeusException(COULD_NOT_CONTACT_SERVER);
		} else if (!responderBean.isSuccessfulResponse()) {
			throw new CoeusException(SERVER_ERROR);
		}
		// Got data from server. return sponsor name.
		// sponsor name = EMPTY if not found.
		if (responderBean.getDataObject() == null)
			return EMPTY_STRING;
		String validSponsorCode = responderBean.getDataObject().toString();
		return validSponsorCode;
	}/* end Bug fix:1666 step:3 */

	/** instantiates instance objects and populates comboboxes. */
	@SuppressWarnings("unchecked")
	public void initComponents() {

		queryEngine = QueryEngine.getInstance();
		coeusMessageResources = CoeusMessageResources.getInstance();
		dateUtils = new DateUtils();
		simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
		awardDetailForm.lblSponsorDesc.setText(EMPTY);

		CoeusVector vecStatus, vecNSFCode, vecActivityType, vecAwardType, vecAccountType, vecPriorProcCode;
		try {
			vecStatus = queryEngine.getDetails(queryKey, KeyConstants.AWARD_STATUS);
			vecNSFCode = queryEngine.getDetails(queryKey, KeyConstants.NSF_CODE);

			vecActivityType = queryEngine.getDetails(queryKey, KeyConstants.ACTIVITY_TYPE);

			// Malini:12/14/15
			vecPriorProcCode = queryEngine.getDetails(queryKey, KeyConstants.PROCUREMENT_PRIORITY_CODE);
			// Malini:12/14/15

			// JM 7-12-2012 commented out this sort so stored sort order will be
			// used
			// vecActivityType.sort("description");

			vecAwardType = queryEngine.getDetails(queryKey, KeyConstants.AWARD_TYPE);
			// JM 7-12-2012 commented out this sort so stored sort order will be
			// used
			// vecAwardType.sort("description");

			vecAccountType = queryEngine.getDetails(queryKey, KeyConstants.ACCOUNT_TYPE);

			ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);

			vecStatus.add(0, emptyBean);
			vecNSFCode.add(0, emptyBean);
			vecActivityType.add(0, emptyBean);
			vecAwardType.add(0, emptyBean);
			vecAccountType.add(0, emptyBean);

			// Malini:12/2/15

			vecPriorProcCode.add(0, emptyBean);

			// Malini:12/2/15
			awardDetailForm.cmbStatus.setModel(new DefaultComboBoxModel(vecStatus));
			awardDetailForm.cmbNSFCode.setModel(new DefaultComboBoxModel(vecNSFCode));
			// awardDetailForm.cmbNSFCode.setShowCode(true);

			awardDetailForm.cmbActivityType.setModel(new DefaultComboBoxModel(vecActivityType));
			awardDetailForm.cmbAwardType.setModel(new DefaultComboBoxModel(vecAwardType));
			awardDetailForm.cmbAccountType.setModel(new DefaultComboBoxModel(vecAccountType));

			DefaultComboBoxModel revCodeModel = new DefaultComboBoxModel(vecPriorProcCode);

			// Malini:12/2/15
			awardDetailForm.txtProcPriorCode.setModel(revCodeModel);

			// Malini:12/2/15

			// End

			// Bug Fix:1399&1455 Start 1
			// awardDetailForm.cmbStatus.setShowCode(true);
			// awardDetailForm.cmbActivityType.setShowCode(true);
			// awardDetailForm.cmbAwardType.setShowCode(true);
			// awardDetailForm.cmbAccountType.setShowCode(true);
			// Bug Fix:1399&1455 End 1

		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}

	}

	/**
	 * returns true if refresh required. else returns false.
	 *
	 * @return returns true if refresh required. else returns false.
	 */
	@Override
	public boolean isRefreshRequired() {
		return refreshRequired;
	}

	/**
	 * listens to item state changed event.
	 *
	 * @param itemEvent
	 *            itemEvent
	 */
	@Override
	public void itemStateChanged(ItemEvent itemEvent) {
		BeanEvent beanEvent;
		ComboBoxBean comboBoxBean;
		// listens for Award Type change. Notify Listeners.
		if (itemEvent.getStateChange() == itemEvent.DESELECTED)
			return;

		beanEvent = new BeanEvent();
		beanEvent.setMessageId(AWARD_TYPE_MODIFIED);

		comboBoxBean = (ComboBoxBean) awardDetailForm.cmbAwardType.getSelectedItem();
		if (!comboBoxBean.getCode().equals(EMPTY)) {
			int code = Integer.parseInt(comboBoxBean.getCode());
			awardHeaderBean.setAwardTypeCode(code);
		}
		comboBoxBean = (ComboBoxBean) awardDetailForm.txtProcPriorCode.getSelectedItem();

		if (!comboBoxBean.getCode().equals(EMPTY)) {
			beanEvent.setMessageId(REVIEW_CODE_MODIFIED);
			awardHeaderBean.setProcurementPriorityCode(comboBoxBean.getCode());
			awardHeaderBean.setProcurementPriorityDescription(comboBoxBean.getDescription());

		} else {
			awardHeaderBean.setProcurementPriorityCode(EMPTY);
			awardHeaderBean.setProcurementPriorityDescription(EMPTY);
		}

		beanEvent.setBean(awardHeaderBean);
		beanEvent.setSource(this);
		fireBeanUpdated(beanEvent);
		setFormData(awardDetailForm);
	}

	/**
	 * listens to mouse Clicked event.
	 *
	 * @param mouseEvent
	 *            mouseEvent
	 */
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		/** MDI Form instance */
		CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
		String displaySponsor = "Display Sponsor";

		int clickCount = mouseEvent.getClickCount();
		if (clickCount != 2)
			return;
		// Double Clicked on Sponsor code validate and display sponsor details.
		String sponsorCode, sponsorName = null;
		sponsorCode = awardDetailForm.txtSponsor.getText().trim();
		if (!sponsorCode.equals(EMPTY)) {
			try {
				// Modified for COEUSQA-1434 : Add the functionality to set a
				// status on a Sponsor record - Start
				// sponsorName = getSponsorName(sponsorCode).trim();
				validateSponsorCode();
				sponsorName = awardDetailForm.lblSponsorDesc.getText();
				// Modified for COEUSQA-1434 : Add the functionality to set a
				// status on a Sponsor record - End
			} catch (CoeusException coeusException) {
				coeusException.printStackTrace();
			}
		}
		if (sponsorCode.equals(EMPTY)) {
			// Sponsor Code not Entered. Do nothing
			return;
		} else if (sponsorName == null || sponsorName.equals(EMPTY)) {
			// Wrong Sponsor Code. show Error Message.
			CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
			return;
		}
		// valid sponsor code. display sponsor details.
		SponsorMaintenanceForm frmSponsor = new SponsorMaintenanceForm(DISPLAY_MODE, sponsorCode);
		frmSponsor.showForm(mdiForm, displaySponsor, true);
	}

	/**
	 * listens to mouse Entered event.
	 *
	 * @param mouseEvent
	 *            mouseEvent
	 */
	@Override
	public void mouseEntered(MouseEvent mouseEvent) {
	}

	/**
	 * listens to mouse Exited event.
	 *
	 * @param mouseEvent
	 *            mouseEvent
	 */
	@Override
	public void mouseExited(MouseEvent mouseEvent) {
	}

	/**
	 * listens to mouse Pressed event.
	 *
	 * @param mouseEvent
	 *            mouseEvent
	 */
	@Override
	public void mousePressed(MouseEvent mouseEvent) {
	}

	/**
	 * listens to mouse Released event.
	 *
	 * @param mouseEvent
	 *            mouseEvent
	 */
	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
	}

	/** refreshes the GUI controlled by this. */
	@Override
	public void refresh() {
		if (isRefreshRequired()) {
			setFormData(awardBaseBean);
			setRefreshRequired(false);
		}
	}

	/** registers components with listeners. */
	@Override
	public void registerComponents() {
		// Setting focus traversal - START
		components = new Component[] { awardDetailForm.cmbStatus, awardDetailForm.txtSponsorAward,
				awardDetailForm.txtAccount, awardDetailForm.cmbNSFCode, awardDetailForm.txtModificationNo,
				awardDetailForm.txtEffectiveDate, awardDetailForm.txtBeginDate, awardDetailForm.txtExecutionDate,
				awardDetailForm.txtSponsor, awardDetailForm.btnSponsor, awardDetailForm.cmbActivityType,
				awardDetailForm.cmbAwardType, awardDetailForm.txtCfdaNo, awardDetailForm.cmbAccountType,
				awardDetailForm.txtDfafsNo, awardDetailForm.cmbSubPlan, awardDetailForm.txtProcPriorCode,
				awardDetailForm.txtAuthorizedAmount, awardDetailForm.txtPreAwardEffectiveDate,
				awardDetailForm.txtArTitle };

		ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy(components);
		awardDetailForm.setFocusTraversalPolicy(traversePolicy);
		awardDetailForm.setFocusCycleRoot(true);
		// Setting focus traversal - END

		// setting documents.
		awardDetailForm.txtEffectiveDate.setDocument(new LimitedPlainDocument(11));
		awardDetailForm.txtExecutionDate.setDocument(new LimitedPlainDocument(11));
		awardDetailForm.txtBeginDate.setDocument(new LimitedPlainDocument(11));

		awardDetailForm.txtSponsorAward.setDocument(new LimitedPlainDocument(70));
		// For bug fix #1596
		// JM awardDetailForm.txtAccount.setDocument(new
		// JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC, 7));
		awardDetailForm.txtAccount.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC, TXTACCOUNT_LENGTH)); // JM
																															// updated
																															// value
																															// per
																															// 4.4.2
		awardDetailForm.txtModificationNo.setDocument(new LimitedPlainDocument(10));
		// awardDetailForm.txtSponsor.setDocument(new LimitedPlainDocument(6));

		awardDetailForm.txtSponsor.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC, 6));

		// CFDA Mask Formatter set in the Award Detail Form.
		// Malini
		awardDetailForm.txtProcPriorCode.addItemListener(this);
		awardDetailForm.txtDfafsNo.setDocument(new LimitedPlainDocument(20));
		// Case# 3822: Increase the length of the proposal title to 200
		// characters - Start
		// awardDetailForm.txtArTitle.setDocument(new
		// LimitedPlainDocument(150));
		awardDetailForm.txtArTitle.setDocument(new LimitedPlainDocument(200));
		// Case# 3822: Increase the length of the proposal title to 200
		// characters - End
		// Add items to subplan combobox
		ComboBoxBean comboBoxBean;
		Vector vecSubPlan = new Vector();
		//comboBoxBean = new ComboBoxBean(UNKNOWN, UNKNOWN_DESC);
		//vecSubPlan.addElement(comboBoxBean);

		comboBoxBean = new ComboBoxBean(NOT_REQ, NOT_REQ_DESC);
		vecSubPlan.addElement(comboBoxBean);
		comboBoxBean = new ComboBoxBean(REQ, REQ_DESC);
		vecSubPlan.addElement(comboBoxBean);

		awardDetailForm.cmbSubPlan.setModel(new DefaultComboBoxModel(vecSubPlan));

		awardDetailForm.btnSponsor.addActionListener(this);
		awardDetailForm.txtSponsor.addFocusListener(this);
		awardDetailForm.txtSponsor.addActionListener(this);
		awardDetailForm.txtSponsor.addMouseListener(this);
		awardDetailForm.txtEffectiveDate.addFocusListener(this);
		awardDetailForm.txtExecutionDate.addFocusListener(this);
		awardDetailForm.txtBeginDate.addFocusListener(this);
		awardDetailForm.txtDfafsNo.addFocusListener(this);

		awardDetailForm.txtPreAwardEffectiveDate.addFocusListener(this);

		awardDetailForm.cmbAwardType.addItemListener(this);

		// awardDetailForm.txtProcPriorCode.addFocusListener(this);
		addBeanUpdatedListener(this, AwardHeaderBean.class);
		addBeanUpdatedListener(this, AwardDetailsBean.class);

		// Bug Fix : 1019 - START
		awardDetailForm.txtCfdaNo.addFocusListener(this);
		// Bug Fix : 1019 - END
	}

	/** saves form data to query engine if modified. */
	@Override
	public void saveFormData() {
		// Set focus to other than date component so as to have date in
		// dd/MM/yyy pattern.
		// So that it can be parsed to date object.
		awardDetailForm.cmbStatus.requestFocusInWindow();

		if (isRefreshRequired()) {
			// Data modified.get latest and hold it in instance variable.
			try {
				CoeusVector cvTemp = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
				awardDetailsBean = (AwardDetailsBean) cvTemp.get(0);

				cvTemp = queryEngine.getDetails(queryKey, AwardHeaderBean.class);
				awardHeaderBean = (AwardHeaderBean) cvTemp.get(0);

			} catch (CoeusException coeusException) {
				coeusException.printStackTrace();
			}
		}

		String strDate;
		Date date;
		int code;

		ComboBoxBean comboBoxBean;

		try {

			comboBoxBean = (ComboBoxBean) awardDetailForm.cmbStatus.getSelectedItem();
			if (!comboBoxBean.getCode().equals(EMPTY)) {
				code = Integer.parseInt(comboBoxBean.getCode().trim());
				awardDetailsBean.setStatusCode(code);
				awardDetailsBean.setStatusDescription(comboBoxBean.getDescription());
			}

			if (!awardDetailForm.txtSponsorAward.getText().trim().equals(awardDetailsBean.getSponsorAwardNumber())) {
				awardDetailsBean.setSponsorAwardNumber(awardDetailForm.txtSponsorAward.getText().trim());
			}

			awardDetailsBean.setAccountNumber(awardDetailForm.txtAccount.getText());

			comboBoxBean = (ComboBoxBean) awardDetailForm.cmbNSFCode.getSelectedItem();
			awardDetailsBean.setNsfCode(comboBoxBean.getCode());
			awardDetailsBean.setNsfDescription(comboBoxBean.getDescription());
			awardDetailsBean.setModificationNumber(awardDetailForm.txtModificationNo.getText().trim());

			strDate = awardDetailForm.txtEffectiveDate.getText().trim();
			if (!strDate.equals(EMPTY)) {
				date = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
				awardDetailsBean.setAwardEffectiveDate(new java.sql.Date(date.getTime()));
			} else {
				awardDetailsBean.setAwardEffectiveDate(null);
			}

			strDate = awardDetailForm.txtBeginDate.getText().trim();
			if (!strDate.equals(EMPTY)) {
				date = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
				awardDetailsBean.setBeginDate(new java.sql.Date(date.getTime()));
			} else {
				awardDetailsBean.setBeginDate(null);
			}
			
			//Bug fix Malini
            strDate = awardDetailForm.txtExecutionDate.getText().trim();
            if (!strDate.equals(EMPTY)) {
                   date = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
                   awardDetailsBean.setAwardExecutionDate(new java.sql.Date(date.getTime()));
            } else {
                   awardDetailsBean.setAwardExecutionDate(null);
            }

			/*
			 * modified for the Bug Fix:1666 for alpha numeric sponsor code
			 * start step:2
			 */
			String sponsrCode = getValidSponsorCode(awardDetailForm.txtSponsor.getText());
			awardDetailsBean.setSponsorCode(getValidSponsorCode(awardDetailForm.txtSponsor.getText()));
			awardDetailsBean.setSponsorName(getSponsorName(sponsrCode));
			/* end step:2 */

			// setting values for award header bean
			comboBoxBean = (ComboBoxBean) awardDetailForm.cmbActivityType.getSelectedItem();
			if (!comboBoxBean.getCode().equals(EMPTY)) {
				code = Integer.parseInt(comboBoxBean.getCode());
				awardHeaderBean.setActivityTypeCode(code);
				awardHeaderBean.setActivityTypeDescription(comboBoxBean.getDescription());
			}

			comboBoxBean = (ComboBoxBean) awardDetailForm.cmbAwardType.getSelectedItem();
			if (!comboBoxBean.getCode().equals(EMPTY)) {
				code = Integer.parseInt(comboBoxBean.getCode());
				awardHeaderBean.setAwardTypeCode(code);
				awardHeaderBean.setAwardTypeDescription(comboBoxBean.getDescription());
			}
			// Malini:12/14/15

			comboBoxBean = (ComboBoxBean) awardDetailForm.txtProcPriorCode.getSelectedItem();
			if (!comboBoxBean.getCode().equals(EMPTY) && (!comboBoxBean.getCode().equals("None"))) {

				awardHeaderBean.setProcurementPriorityCode(comboBoxBean.getCode().trim());
				awardHeaderBean.setProcurementPriorityDescription(comboBoxBean.getDescription().trim());

			}

			strDate = awardDetailForm.txtDfafsNo.getText().trim();
			if (!strDate.equals(EMPTY)) {
				awardHeaderBean.setDfafsNumber(strDate);
			} else {
				awardHeaderBean.setDfafsNumber(EMPTY);
			}

			// Malini:12/14/15

			// Remove Period(.) before saving.
			String strCfdaNo = awardDetailForm.txtCfdaNo.getText();
			strCfdaNo = strCfdaNo.substring(0, 2) + strCfdaNo.substring(3);
			awardHeaderBean.setCfdaNumber(strCfdaNo);

			comboBoxBean = (ComboBoxBean) awardDetailForm.cmbAccountType.getSelectedItem();
			if (!comboBoxBean.getCode().equals(EMPTY)) {
				code = Integer.parseInt(comboBoxBean.getCode());
				awardHeaderBean.setAccountTypeCode(code);
				awardHeaderBean.setAccountTypeDescription(comboBoxBean.getDescription());
			}

			comboBoxBean = (ComboBoxBean) awardDetailForm.cmbSubPlan.getSelectedItem();
			awardHeaderBean.setSubPlanFlag(comboBoxBean.getCode());

			try {

				// double preAwardAuthAmt =
				// Double.parseDouble(awardDetailForm.txtAuthorizedAmount.getValue());
				// Changed the datatype from double to Double object
				Double preAwardAuthAmt = new Double(awardDetailForm.txtAuthorizedAmount.getValue());
				awardHeaderBean.setPreAwardAuthorizedAmount(preAwardAuthAmt);
			} catch (NumberFormatException numberFormatException) {
				// set amount to zero
				awardHeaderBean.setPreAwardAuthorizedAmount(null);
			}

			strDate = awardDetailForm.txtPreAwardEffectiveDate.getText().trim();
			if (!strDate.equals(EMPTY)) {
				date = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
				awardHeaderBean.setPreAwardEffectiveDate(new java.sql.Date(date.getTime()));
			} else {
				awardHeaderBean.setPreAwardEffectiveDate(null);
			}

			awardHeaderBean.setTitle(awardDetailForm.txtArTitle.getText());

			// Update only if Data is Changed
			// Check if Data Changed - START
			StrictEquals strictEqualsDetails = new StrictEquals();
			StrictEquals strictEqualsHeader = new StrictEquals();

			AwardDetailsBean qryAwardDetailsBean = new AwardDetailsBean();
			AwardHeaderBean qryAwardHeaderBean = new AwardHeaderBean();

			CoeusVector cvTemp = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
			if (cvTemp != null && cvTemp.size() > 0) {
				qryAwardDetailsBean = (AwardDetailsBean) cvTemp.get(0);
			}

			cvTemp = queryEngine.getDetails(queryKey, AwardHeaderBean.class);
			if (cvTemp != null && cvTemp.size() > 0) {
				qryAwardHeaderBean = (AwardHeaderBean) cvTemp.get(0);
			}

			if (!strictEqualsDetails.compare(awardDetailsBean, qryAwardDetailsBean)) {
				// Data Changed. save to query Engine.
				if (getFunctionType() == CORRECT_AWARD) {
					awardDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
					queryEngine.update(queryKey, awardDetailsBean);
				} else {
					awardDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
					queryEngine.insert(queryKey, awardDetailsBean);
				}
				// Fire Award Details Modified even
				BeanEvent beanEvent = new BeanEvent();
				beanEvent.setSource(this);
				beanEvent.setBean(awardDetailsBean);
				fireBeanUpdated(beanEvent);
			}

			if (!strictEqualsHeader.compare(awardHeaderBean, qryAwardHeaderBean)) {
				// Data Change. save to query Engine.
				if (getFunctionType() == CORRECT_AWARD) {
					awardHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
					queryEngine.update(queryKey, awardHeaderBean);
				} else {
					awardHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
					queryEngine.insert(queryKey, awardHeaderBean);
				}
				// Fire Award Header Modified even
				BeanEvent beanEvent = new BeanEvent();
				beanEvent.setSource(this);
				beanEvent.setBean(awardHeaderBean);
				fireBeanUpdated(beanEvent);

			}
			// Check if Data Changed - END
		} catch (ParseException parseException) {
			// parseException.printStackTrace();
			// one of the Date Validation failed. i.e. Date is being Modified.
			try {
				awardDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
				queryEngine.update(queryKey, awardDetailsBean);
			} catch (CoeusException coeusException) {
				coeusException.printStackTrace();
			}
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}

	}

	/**
	 * sets data to form.
	 *
	 * @param data
	 *            data to be set.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setFormData(Object data) {
		if (data == null || !(data instanceof AwardBaseBean))
			return;

		String lastUpdateFormat = DATE_FORMAT_DISPLAY + " hh:mm a";

		// Set Award number and Sequence Number
		awardBaseBean = (AwardBaseBean) data;
		awardDetailForm.txtAward.setText(awardBaseBean.getMitAwardNumber());
		String seqNum = EMPTY + awardBaseBean.getSequenceNumber();
		awardDetailForm.txtSequence.setText(seqNum);

		try {

			CoeusVector cvTemp = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
			if (cvTemp != null && cvTemp.size() > 0) {
				awardDetailsBean = (AwardDetailsBean) cvTemp.get(0);
			} else {
				awardDetailsBean = new AwardDetailsBean();
				awardDetailsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
				awardDetailsBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
				// Nothing more to set.
				return;
			}

			cvTemp = queryEngine.getDetails(queryKey, AwardHeaderBean.class);
			if (cvTemp != null && cvTemp.size() > 0) {
				awardHeaderBean = (AwardHeaderBean) cvTemp.get(0);
			}

			ComboBoxBean comboBoxBean = new ComboBoxBean();
			comboBoxBean.setCode(EMPTY + awardDetailsBean.getStatusCode());
			comboBoxBean.setDescription(awardDetailsBean.getStatusDescription());
			awardDetailForm.cmbStatus.setSelectedItem(comboBoxBean);

			awardDetailForm.txtSponsorAward.setText(awardDetailsBean.getSponsorAwardNumber());
			// Added for Case#2402 - use a parameter to set the length of the
			// account number throughout app - Start
			CoeusVector cvParameters = queryEngine.executeQuery(queryKey, CoeusParameterBean.class,
					CoeusVector.FILTER_ACTIVE_BEANS);
			if (cvParameters != null && cvParameters.size() > 0) {
				CoeusVector cvFiltered = null;
				// To get the MAX_ACCOUNT_NUMBER_LENGTH parameter
				cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
				if (cvFiltered != null && cvFiltered.size() > 0) {
					CoeusParameterBean parameterBean = (CoeusParameterBean) cvFiltered.get(0);
					accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
				}

			}
			awardDetailForm.txtAccount.setDocument(new JTextFieldFilter(
					(JTextFieldFilter.ALPHA_NUMERIC + JTextFieldFilter.COMMA_HYPHEN_PERIOD), accountNumberMaxLength));
			String accountNumber = awardDetailsBean.getAccountNumber();
			if (accountNumber == null) {
				accountNumber = CoeusGuiConstants.EMPTY_STRING;
				// When the Existing accountNumber size is greater than current
				// number size
				// then existing account is sub string to current size
			} else if (accountNumber.length() > accountNumberMaxLength) {
				accountNumber = accountNumber.substring(0, accountNumberMaxLength);
			}
			awardDetailForm.txtAccount.setText(accountNumber);
			// Case#2402 - End
			comboBoxBean = new ComboBoxBean();
			comboBoxBean.setCode(awardDetailsBean.getNsfCode());
			comboBoxBean.setDescription(awardDetailsBean.getNsfDescription());
			awardDetailForm.cmbNSFCode.setSelectedItem(comboBoxBean);

			awardDetailForm.txtModificationNo.setText(awardDetailsBean.getModificationNumber());

			if (awardDetailsBean.getAwardEffectiveDate() != null) {
				awardDetailForm.txtEffectiveDate.setText(
						dateUtils.formatDate(awardDetailsBean.getAwardEffectiveDate().toString(), DATE_FORMAT_DISPLAY));
			}

			if (awardDetailsBean.getBeginDate() != null) {
				awardDetailForm.txtBeginDate
						.setText(dateUtils.formatDate(awardDetailsBean.getBeginDate().toString(), DATE_FORMAT_DISPLAY));
			}

			if (awardDetailsBean.getAwardExecutionDate() != null) {
				awardDetailForm.txtExecutionDate.setText(
						dateUtils.formatDate(awardDetailsBean.getAwardExecutionDate().toString(), DATE_FORMAT_DISPLAY));
			}

			awardDetailForm.txtSponsor.setText(awardDetailsBean.getSponsorCode());
			awardDetailForm.lblSponsorDesc.setText(awardDetailsBean.getSponsorName());

			if (awardHeaderBean == null) {
				awardHeaderBean = new AwardHeaderBean();
				awardHeaderBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
				awardHeaderBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
				// Nothing more to set.
				return;
			}

			comboBoxBean = new ComboBoxBean();
			comboBoxBean.setCode(EMPTY + awardHeaderBean.getActivityTypeCode());
			comboBoxBean.setDescription(awardHeaderBean.getActivityTypeDescription());
			awardDetailForm.cmbActivityType.setSelectedItem(comboBoxBean);

			// Malini:12/2/15

			comboBoxBean = new ComboBoxBean();
			comboBoxBean.setCode(EMPTY + awardHeaderBean.getProcurementPriorityCode());
			comboBoxBean.setDescription(awardHeaderBean.getProcurementPriorityDescription());
			awardDetailForm.txtProcPriorCode.setSelectedItem(comboBoxBean);
			// End

			comboBoxBean = new ComboBoxBean();
			comboBoxBean.setCode(EMPTY + awardHeaderBean.getAwardTypeCode());
			comboBoxBean.setDescription(awardHeaderBean.getAwardTypeDescription());
			awardDetailForm.cmbAwardType.setSelectedItem(comboBoxBean);

			awardDetailForm.txtCfdaNo.setText(awardHeaderBean.getCfdaNumber());

			comboBoxBean = new ComboBoxBean();
			comboBoxBean.setCode(EMPTY + awardHeaderBean.getAccountTypeCode());
			comboBoxBean.setDescription(awardHeaderBean.getAccountTypeDescription());
			awardDetailForm.cmbAccountType.setSelectedItem(comboBoxBean);

			if (awardHeaderBean.getSubPlanFlag() != null) {
				String subPlan = awardHeaderBean.getSubPlanFlag().trim();
				comboBoxBean = new ComboBoxBean();
				if (subPlan.equals(REQ)) {
					comboBoxBean.setCode(REQ);
					comboBoxBean.setDescription(REQ_DESC);
				} else if (subPlan.equals(NOT_REQ)) {
					comboBoxBean.setCode(NOT_REQ);
					comboBoxBean.setDescription(NOT_REQ_DESC);
				} 
				//Malini:5/1/2017 Unknown option removed from SubPlan combobox
				awardDetailForm.cmbSubPlan.setSelectedItem(comboBoxBean);
			}

			awardDetailForm.txtAuthorizedAmount.setValue(awardHeaderBean.getPreAwardAuthorizedAmount() == null ? 0
					: awardHeaderBean.getPreAwardAuthorizedAmount().doubleValue());

			if (awardHeaderBean.getPreAwardEffectiveDate() != null) {
				awardDetailForm.txtPreAwardEffectiveDate.setText(dateUtils
						.formatDate(awardHeaderBean.getPreAwardEffectiveDate().toString(), DATE_FORMAT_DISPLAY));
			}

			if (awardHeaderBean.getDfafsNumber() != null) {
				awardDetailForm.txtDfafsNo.setText(awardHeaderBean.getDfafsNumber());
			}

			awardDetailForm.txtArTitle.setText(awardHeaderBean.getTitle());
			awardDetailForm.txtArTitle.setCaretPosition(0);

			simpleDateFormat.applyPattern(lastUpdateFormat);
			if (awardDetailsBean.getUpdateTimestamp() != null) {
				awardDetailForm.txtLastUpdate.setText(simpleDateFormat.format(awardDetailsBean.getUpdateTimestamp()));
			}

			simpleDateFormat.applyPattern(SIMPLE_DATE_FORMAT);

			// awardDetailForm.txtUpdateUser.setText(awardDetailsBean.getUpdateUser());
			/*
			 * UserID to UserName Enhancement - Start Added UserUtils class to
			 * change userid to username
			 */
			awardDetailForm.txtUpdateUser.setText(UserUtils.getDisplayName(awardDetailsBean.getUpdateUser()));
			// UserId to UserName Enhancement - End

			// JM 6-18-2011 added NIH Mechanism field
			try {
				CoeusVector columnValues = queryEngine.executeQuery(queryKey, AwardCustomDataBean.class,
						CoeusVector.FILTER_ACTIVE_BEANS);
				for (int index = 0; index < columnValues.size(); index++) {
					awardCustomDataBean = (AwardCustomDataBean) columnValues.get(index);
					if (awardCustomDataBean.getColumnName().contentEquals("NIH MECHANISM")) {
						awardDetailForm.txtMechanism.setText(awardCustomDataBean.getColumnValue());
					}
				} // end for
			} catch (CoeusException ce) {
				ce.printStackTrace();
			}
			// END

		} catch (

		CoeusException coeusException)

		{
			coeusException.printStackTrace();
		}

	}

	/**
	 * sets refresh required flag.
	 *
	 * @param refreshRequired
	 *            refreshRequired flag.
	 */
	@Override
	public void setRefreshRequired(boolean refreshRequired) {
		this.refreshRequired = refreshRequired;
	}

	private void setRequestFocusInThread(final Component component) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				component.requestFocusInWindow();
			}
		});
	}

	// Bug Fix:Performance Issue (Out of memory) End 3
	// Added with Case 2796: Sync To Parent
	/*
	 * This method pops up the sync selection window and saves the award if
	 * required.
	 */
	public void showSyncOptions() {
		// Modified for COEUSDEV-416 : Award Sync to Children - Display proper
		// error message when not syncing because the award is not saved
		// if(validateBeforeSync()){
		if (validateBeforeSync(AwardConstants.DETAIL_SYNC, AwardConstants.SYNC)) {// COEUSDEV-416
																					// :
																					// End
			HashMap hmFieldMap = new HashMap();
			// checking Status
			ComboBoxBean comboBoxBean = (ComboBoxBean) awardDetailForm.cmbStatus.getSelectedItem();
			hmFieldMap.put(KeyConstants.AWARD_STATUS, comboBoxBean.getCode() + " - " + comboBoxBean.getDescription());
			// Checking sponsor Code
			String currentSponsorDesc = awardDetailForm.lblSponsorDesc.getText().trim();
			String currentSponsorCode = awardDetailForm.txtSponsor.getText().trim();
			hmFieldMap.put(KeyConstants.SPONSOR_CODE, currentSponsorCode + " - " + currentSponsorDesc);

			if (!hmFieldMap.isEmpty()) {

				AwardDetailSyncForm syncForm = new AwardDetailSyncForm(hmFieldMap);
				hmFieldMap = syncForm.display();
				if (!hmFieldMap.isEmpty()) {
					// COEUSDEV 253: Add Fabe and Cs to sync selection screen
					hmFieldMap.put(KeyConstants.SYNC_FABE_ACCOUNTS, syncForm.isIncludeFABE());
					hmFieldMap.put(KeyConstants.SYNC_CS_ACCOUNTS, syncForm.isIncludeCS());
					// COEUSDEV 253: End
					// Added for COEUSDEV-563:Award Sync to Parent is not
					// triggering SAP feed for child accounts its touching
					if (isSAPFeedEnabled()) {
						int selectedOption = CoeusOptionPane.showQuestionDialog(
								coeusMessageResources.parseMessageKey(MSGKEY_SAP_FEED_REQUIRED),
								CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
						if (selectedOption == CoeusOptionPane.SELECTION_YES) {
							hmFieldMap.put(KeyConstants.ADD_SAP_FEED_FOR_CHILD_AWARDS, "Y");
						}
					}
					// COEUSDEV-563:End
					awardDetailsBean.setParameter(hmFieldMap);
					awardDetailsBean.setSyncRequired(true);
					awardDetailsBean.setSyncTarget(syncForm.getSelectedSyncTarget());
					saveFormData();
					saveAndSyncAward(AwardDetailsBean.class);
				}
			}
		}
	}

	// 2796 End
	/**
	 * validates the form. returns false if validation fails. else returns true.
	 *
	 * @throws CoeusUIException
	 *             if any exception occurs / validation fails.
	 * @return returns false if validation fails. else returns true.
	 */
	@Override
	public boolean validate() throws CoeusUIException {

		refresh();

		double preAwardMaxAuthAmount = 1000000000;
		ComboBoxBean comboBoxBean;
		if (awardDetailForm.cmbStatus.getSelectedIndex() == 0) {
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_STATUS));
			awardDetailForm.cmbStatus.requestFocusInWindow();
			return false;
		}

		if (awardDetailForm.txtSponsor.getText().trim().equals(EMPTY)) {
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_VALID_SPONSOR_CODE));
			awardDetailForm.txtSponsor.requestFocusInWindow();
			return false;
		} else {
			// Sponsor Code not Empty. check if valid.
			// Commented for COEUSQA-1434 Add the functionality to set a status
			// on a Sponsor record - Start
			// String name;
			// try{
			// name=
			// getSponsorName(awardDetailForm.txtSponsor.getText().trim());
			// }catch (CoeusException coeusException) {
			// coeusException.printStackTrace();
			// name = null;
			// }
			// if(name == null || name.trim().equals(EMPTY)) {
			// CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_VALID_SPONSOR_CODE));
			// awardDetailForm.txtSponsor.requestFocusInWindow();
			// return false;
			// }
			// Commented for COEUSQA-1434 : Add the functionality to set a
			// status on a Sponsor record - end

		}

		// Do All Date and sponsor code Validations First
		String strDate;
		// Date date;

		strDate = awardDetailForm.txtEffectiveDate.getText().trim();
		if (awardDetailForm.txtEffectiveDate.hasFocus()) {
			strDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
		} else {
			strDate = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
		}
		if (strDate == null) {
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_EFFECTIVE_DATE));
			awardDetailForm.txtEffectiveDate.requestFocusInWindow();
			return false;
		} else {
			strDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
			awardDetailForm.txtEffectiveDate.setText(strDate);
		}
		// End Effective Date Validation.

		strDate = awardDetailForm.txtExecutionDate.getText().trim();
		if (awardDetailForm.txtExecutionDate.hasFocus()) {
			strDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
		} else {
			strDate = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
		}
		if (strDate == null) {
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_EXECUTION_DATE));
			awardDetailForm.txtExecutionDate.requestFocusInWindow();
			return false;
		} else {
			strDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
			awardDetailForm.txtExecutionDate.setText(strDate);
		}
		// End Execution Date Validation.

		strDate = awardDetailForm.txtBeginDate.getText().trim();
		if (awardDetailForm.txtBeginDate.hasFocus()) {
			strDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
		} else {
			strDate = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
		}
		if (strDate == null) {
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_BEGIN_DATE));
			awardDetailForm.txtBeginDate.requestFocusInWindow();
			return false;
		} else {
			strDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
			awardDetailForm.txtBeginDate.setText(strDate);
		}
		// End Begin Date Validation.

		String strPreAwardEffectiveDate = awardDetailForm.txtPreAwardEffectiveDate.getText().trim();
		if (!strPreAwardEffectiveDate.equals(EMPTY)) {
			if (awardDetailForm.txtPreAwardEffectiveDate.hasFocus()) {
				strPreAwardEffectiveDate = dateUtils.formatDate(strPreAwardEffectiveDate, DATE_SEPARATERS,
						DATE_FORMAT_DISPLAY);
			} else {
				strPreAwardEffectiveDate = dateUtils.restoreDate(strPreAwardEffectiveDate, DATE_SEPARATERS);
			}
			if (strPreAwardEffectiveDate == null) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_PRE_EFFECTIVE_DATE));
				awardDetailForm.txtPreAwardEffectiveDate.requestFocusInWindow();
				return false;
			} else {
				/*
				 * if(!awardDetailForm.txtPreAwardEffectiveDate.hasFocus()) {
				 * strPreAwardEffectiveDate =
				 * dateUtils.formatDate(strDate,DATE_SEPARATERS,
				 * DATE_FORMAT_DISPLAY); }
				 */
				awardDetailForm.txtPreAwardEffectiveDate.setText(strPreAwardEffectiveDate);
			}
		}

		// Malini:12/15/15
		strDate = awardDetailForm.txtDfafsNo.getText().trim();
		if (!strDate.equals(EMPTY) || strDate != null) {
			if (awardDetailForm.txtDfafsNo.hasFocus()) {
				strDate = dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
			} else {
				strDate = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
			}

			awardDetailForm.txtDfafsNo.setText(strDate);
		}
		ComboBoxBean reviewCodeBean = (ComboBoxBean) awardDetailForm.txtProcPriorCode.getSelectedItem();

		if (reviewCodeBean.getCode().equals("None")) {
			awardDetailForm.txtProcPriorCode.setSelectedItem(new ComboBoxBean(EMPTY, EMPTY));
			awardDetailForm.txtProcPriorCode.setSelectedIndex(0);
			awardDetailForm.txtDfafsNo.setText(EMPTY);

		}

		// End 
		
		//Soujanya - updated the validation condition : 3/24/16

		if((!awardDetailForm.txtDfafsNo.getText().trim().equals(EMPTY))
				&& (awardDetailForm.txtProcPriorCode.getSelectedIndex() == 0 || reviewCodeBean.getCode().equals("None")))
		{
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_REVIEW_CODE));
			awardDetailForm.txtProcPriorCode.requestFocusInWindow();
			return false;
		}
		
		if((awardDetailForm.txtProcPriorCode.getSelectedIndex() != 0 && (!reviewCodeBean.getCode().equals("None")))
				&& (awardDetailForm.txtDfafsNo.getText().trim().equals(EMPTY)))
		{
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_REVIEW_DATE));
			awardDetailForm.txtDfafsNo.requestFocusInWindow();
			return false;
		}
		
		if ((awardDetailForm.txtDfafsNo.getText().trim().equals(EMPTY))
				&& (awardDetailForm.txtProcPriorCode.getSelectedIndex() == 0
						|| reviewCodeBean.getCode().equals("None"))) {
			awardDetailForm.txtProcPriorCode.setSelectedItem(new ComboBoxBean(EMPTY, EMPTY));
			awardDetailForm.txtProcPriorCode.setSelectedIndex(0);
			awardDetailForm.txtDfafsNo.setText(EMPTY);

		}
				
		//Soujanya end
		
		// End Malini:12/15/15

		// end DfafsNum validation

		// Sponsor Award Number and effective date is mandatory if Execution
		// date is filled in.
		if (!awardDetailForm.txtExecutionDate.getText().trim().equals(EMPTY)) {
			if (awardDetailForm.txtSponsorAward.getText().trim().equals(EMPTY)) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_SPONSOR_AWARD_NUM));
				awardDetailForm.txtSponsorAward.requestFocusInWindow();
				return false;
			}
			if (awardDetailForm.txtEffectiveDate.getText().trim().equals(EMPTY)) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_EFFECTIVE_DATE));
				awardDetailForm.txtEffectiveDate.requestFocusInWindow();
				return false;
			}
		}

		if (awardDetailForm.cmbActivityType.getSelectedIndex() == 0) {
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ACTIVITY_TYPE));
			awardDetailForm.cmbActivityType.requestFocusInWindow();
			return false;
		}

		if (awardDetailForm.cmbAwardType.getSelectedIndex() == 0) {
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_AWARD_TYPE));
			awardDetailForm.cmbAwardType.requestFocusInWindow();
			return false;
		}

		ComboBoxBean cbAwardType = (ComboBoxBean) awardDetailForm.cmbAwardType.getSelectedItem();
		int awardType = Integer.parseInt(cbAwardType.getCode());
		int basisCode = awardHeaderBean.getBasisOfPaymentCode();
		if (basisCode > 0) {
			Equals eqAwardType = new Equals("awardTypeCode", new Integer(awardType));
			Equals eqBasisCode = new Equals("code", EMPTY + basisCode);
			And awardTypeAndBasis = new And(eqAwardType, eqBasisCode);
			CoeusVector cvBasis = null;
			try {
				cvBasis = queryEngine.executeQuery(queryKey, ValidBasisPaymentBean.class, awardTypeAndBasis);
			} catch (CoeusException coeusException) {
				coeusException.printStackTrace();
			}
			if (cvBasis == null || cvBasis.size() == 0) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_COMBINATION));
				awardDetailForm.cmbAwardType.requestFocusInWindow();
				return false;
			}
		}

		// Pre-award authorized amount is optional. If entered, must be <
		// 1,000,000,000
		double preAwardAuthAmt = Double.parseDouble(awardDetailForm.txtAuthorizedAmount.getValue());

		if (preAwardAuthAmt != 0) {
			if (preAwardAuthAmt > preAwardMaxAuthAmount) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_PRE_AWARD_AUTH_AMOUNT));
				awardDetailForm.txtAuthorizedAmount.requestFocusInWindow();
				return false;
			}
			// IF Pre-Award Authorized Amount is Non NULL, Pre-Award Effective
			// Date is mandatory.
			if (awardDetailForm.txtPreAwardEffectiveDate.getText().trim().equals(EMPTY)) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_PRE_EFFECTIVE_DATE));
				awardDetailForm.txtPreAwardEffectiveDate.requestFocusInWindow();
				return false;
			}
		}

		if (awardDetailForm.txtArTitle.getText().trim().equals(EMPTY)) {
			CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_TITLE));
			awardDetailForm.txtArTitle.requestFocusInWindow();
			return false;
		}

		return true;
	}

	// Added for COEUSQA-1434 : Add the functionality to set a status on a
	// Sponsor record - Start
	/**
	 * Method used to validate the sponsor code
	 *
	 * @throws edu.mit.coeus.exception.CoeusException
	 */
	private void validateSponsorCode() throws CoeusException {
		String sponsorCode = awardDetailForm.txtSponsor.getText().trim();
		String sponsorName = CoeusGuiConstants.EMPTY_STRING;
		if (!sponsorChanged && !sponsorCode.equals(awardDetailsBean.getSponsorCode())) {
			sponsorChanged = true;
		}
		if (sponsorChanged && !CoeusGuiConstants.EMPTY_STRING.equals(sponsorCode)) {
			sponsorName = getSponsorName(sponsorCode);
		} else if (!CoeusGuiConstants.EMPTY_STRING.equals(sponsorCode)) {
			sponsorName = awardDetailsBean.getSponsorName();
		}

		if (!CoeusGuiConstants.EMPTY_STRING.equals(awardDetailForm.txtSponsor.getText().trim())
				&& CoeusGuiConstants.EMPTY_STRING.equals(sponsorName)) {
			CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
			sponsorName = CoeusGuiConstants.EMPTY_STRING;
			awardDetailForm.txtSponsor.setText(CoeusGuiConstants.EMPTY_STRING);
			awardDetailForm.lblSponsorDesc.setText(sponsorName);
			setRequestFocusInThread(awardDetailForm.txtSponsor);
		} else {
			awardDetailForm.lblSponsorDesc.setText(sponsorName);
		}

	}
	// Added for COEUSQA-1434 : Add the functionality to set a status on a
	// Sponsor record - End

}