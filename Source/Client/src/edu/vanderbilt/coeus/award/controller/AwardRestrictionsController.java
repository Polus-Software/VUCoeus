/**
 * AwardRestrictionsController.java
 * 
 * Controller for award restrictions tab
 *
 * @created	September 26, 2014
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.award.controller;

import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.vanderbilt.coeus.award.AwardRestrictionsTableModel;
import edu.vanderbilt.coeus.award.bean.AwardRestrictionsBean;
import edu.vanderbilt.coeus.award.gui.AwardRestrictionsForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import edu.mit.coeus.exception.CoeusClientException;

public class AwardRestrictionsController extends Controller implements ActionListener, TypeConstants {
	
    private AwardRestrictionsTableModel awardRestrictionsTableModel;
    protected AwardRestrictionsForm awardRestrictionsForm;
    private CoeusVector cvRestrictions, cvDeleted;
    public Vector vecRestrictionCodes, vecRestrictionMaintainers;
    public JComboBox cmbStatus, cmbRestriction, cmbAssignedUserName;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();

    private int tabIndex;
    public AwardBaseBean awardBaseBean;
    private int selectedRow;
    
    private boolean isEnabled = false;
    private boolean isMaintainer = false;
    private char functionType;

    private static final char GET_AWARD_RESTRICTIONS = 'A';
    private static final char UPD_AWARD_RESTRICTIONS = 'B';
    private static final char GET_AWARD_RESTRICTION_CODES = 'C';
    private static final char GET_RESTRICTION_MAINTAINERS = 'D';
    private static final String SERVLET = "/AwardServlet";

    private static final String GET_USER_HAS_ROLE = "GET_USER_PROPOSAL_RIGHTS";
    private static final String USER_SERVLET = "/coeusFunctionsServlet";
    
    private static final String ROLE = "547";
    
    public AwardRestrictionsController(AwardBaseBean awardBaseBean,char functionType) {
    	this.awardBaseBean = awardBaseBean;
        setFunctionType(functionType);
        initComponents();
        registerComponents();
    }
    
    public void initComponents() {
        cvRestrictions = new CoeusVector();
        cvDeleted = new CoeusVector();
        vecRestrictionCodes = new Vector();
        vecRestrictionMaintainers = new Vector();
    	awardRestrictionsForm = new AwardRestrictionsForm();
        try {
			cvRestrictions = getDataFromServer();
		} catch (CoeusClientException e) {
			System.out.println("Could not get restrictions data for award " + awardBaseBean.getMitAwardNumber());
			e.printStackTrace();
		}
        
        try {
        	vecRestrictionCodes = getRestrictionCodes();
		} catch (CoeusClientException e) {
			System.out.println("Could not get restriction type list for award " + awardBaseBean.getMitAwardNumber());
			e.printStackTrace();
		}
        
        try {
        	vecRestrictionMaintainers = getRestrictionMaintainers();
		} catch (CoeusClientException e) {
			System.out.println("Could not get restriction maintainers for award " + awardBaseBean.getMitAwardNumber());
			e.printStackTrace();
		}
        
        /* Is user a maintainer? */
		try {
			isMaintainer = isUserRestrictionsMaintainer(mdiForm.getUserId());
		} catch (CoeusClientException e) {
			e.printStackTrace();
		}
    }
    
    public void registerComponents() {
        awardRestrictionsTableModel = new AwardRestrictionsTableModel(cvRestrictions,getFunctionType());
        awardRestrictionsTableModel.vecRestrictionCodes = vecRestrictionCodes;
        awardRestrictionsTableModel.vecRestrictionMaintainers = vecRestrictionMaintainers;
        
        /* set function type based on role */
    	char tabStatusMode = TypeConstants.DISPLAY_MODE;
        if (isTabEnabled()) {
        	tabStatusMode = TypeConstants.MODIFY_MODE;
       	}
        
        awardRestrictionsForm.tblRestrictions.setModel(awardRestrictionsTableModel);
 
        /* JM 9-4-2015 added to set font color based on status for all visible columns */
    	edu.vanderbilt.coeus.gui.CustomTableCellRenderer renderer;

    	renderer = new edu.vanderbilt.coeus.gui.CustomTableCellRenderer(AwardRestrictionsTableModel.RESTRICTION_DESC,null,Color.YELLOW, false, true, false, tabStatusMode);
    	awardRestrictionsForm.tblRestrictions.getColumnModel().getColumn(AwardRestrictionsTableModel.RESTRICTION_DESC).setCellRenderer(renderer);
    	
    	renderer = new edu.vanderbilt.coeus.gui.CustomTableCellRenderer(AwardRestrictionsTableModel.ACTION_DATE,null,Color.YELLOW, false, true, false, tabStatusMode);
    	awardRestrictionsForm.tblRestrictions.getColumnModel().getColumn(AwardRestrictionsTableModel.ACTION_DATE).setCellRenderer(renderer);
    	
    	renderer = new edu.vanderbilt.coeus.gui.CustomTableCellRenderer(AwardRestrictionsTableModel.DUE_DATE,null,Color.YELLOW, false, true, false, tabStatusMode);
    	awardRestrictionsForm.tblRestrictions.getColumnModel().getColumn(AwardRestrictionsTableModel.DUE_DATE).setCellRenderer(renderer);

    	renderer = new edu.vanderbilt.coeus.gui.CustomTableCellRenderer(AwardRestrictionsTableModel.STATUS,null,Color.YELLOW, false, true, false, tabStatusMode);
    	awardRestrictionsForm.tblRestrictions.getColumnModel().getColumn(AwardRestrictionsTableModel.STATUS).setCellRenderer(renderer);

    	renderer = new edu.vanderbilt.coeus.gui.CustomTableCellRenderer(AwardRestrictionsTableModel.ASSIGNED_USER_NAME,null,Color.YELLOW, false, true, false, tabStatusMode);
    	awardRestrictionsForm.tblRestrictions.getColumnModel().getColumn(AwardRestrictionsTableModel.ASSIGNED_USER_NAME).setCellRenderer(renderer);
    	/* JM END */
    	
    	awardRestrictionsForm.vecRestrictionCodes = vecRestrictionCodes;
    	awardRestrictionsForm.vecRestrictionMaintainers = vecRestrictionMaintainers;
    	awardRestrictionsForm.formatTable();
    	
        setDefaultFocusInWindow();
    	
    	ItemListener itemListener = new ItemListener() {
    		public void itemStateChanged(ItemEvent itemEvent) {
    			//int state = itemEvent.getStateChange();
    		}
    	};
    	awardRestrictionsForm.cmbRestriction.addItemListener(itemListener);
    	awardRestrictionsForm.cmbMaintainers.addItemListener(itemListener);
    	
    	ListSelectionModel list = awardRestrictionsForm.tblRestrictions.getSelectionModel();
    	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	awardRestrictionsForm.tblRestrictions.setRowSelectionAllowed(false);
    	awardRestrictionsForm.tblRestrictions.setColumnSelectionAllowed(false);
        awardRestrictionsForm.tblRestrictions.setCellSelectionEnabled(true);
    	ListSelectionListener listSelectionListener = new TableListListener();
        list.addListSelectionListener(listSelectionListener);

        setTabStatus();
        
        setTableTabTraversal();
        
        awardRestrictionsForm.btnAdd.addActionListener(this);
        awardRestrictionsForm.btnDelete.addActionListener(this);
        
        /* Listen for changes in comments field */
        CommentsDocumentListener documentListener = new CommentsDocumentListener();
        awardRestrictionsForm.txtComments.getDocument().addDocumentListener(documentListener);

        setSaveRequired(false);
    }
    
    public void setTabStatus() {
        /* Set tab to enabled or not based on role */
        isEnabled = isTabEnabled();
        AwardRestrictionsTableModel model = (AwardRestrictionsTableModel) awardRestrictionsForm.tblRestrictions.getModel();
        awardRestrictionsForm.btnAdd.setEnabled(isEnabled);
        awardRestrictionsForm.btnDelete.setEnabled(isEnabled);
        awardRestrictionsForm.txtComments.setEnabled(isEnabled);
        awardRestrictionsForm.tblRestrictions.setEnabled(isEnabled);
        
        if (awardRestrictionsForm.tblRestrictions.getRowCount() <= 0 ) {
            awardRestrictionsForm.btnDelete.setEnabled(false);
        }
        else {
            awardRestrictionsForm.tblRestrictions.setRowSelectionInterval(0,0);
        }
        
        if (isEnabled) {
        	awardRestrictionsForm.tblRestrictions.setBackground(Color.white);
            awardRestrictionsForm.tblRestrictions.setSelectionBackground(Color.white);
            awardRestrictionsForm.tblRestrictions.setSelectionForeground(Color.black);
        }
        else {
            Color bgListColor = (Color) UIManager.getDefaults().get("Panel.background");
            awardRestrictionsForm.txtComments.setBackground(bgListColor);
            awardRestrictionsForm.tblRestrictions.setBackground(bgListColor);
            awardRestrictionsForm.tblRestrictions.setSelectionBackground(bgListColor);
            awardRestrictionsForm.tblRestrictions.setSelectionForeground(Color.BLACK);
            model.setEditable(false);
        }
    }
    
    public void setTableTabTraversal() {
    	awardRestrictionsForm.tblRestrictions.setFocusTraversalKeysEnabled(true);
    	
    	awardRestrictionsForm.tblRestrictions.addKeyListener(new KeyAdapter() {
    		public void keyReleased(KeyEvent e) {
    			if (e.getKeyCode() == KeyEvent.VK_TAB) {
    				awardRestrictionsForm.tblRestrictions.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    				
    				int row = awardRestrictionsForm.tblRestrictions.getSelectedRow();
    				int col = awardRestrictionsForm.tblRestrictions.getSelectedColumn();
    				int rowCount = awardRestrictionsForm.tblRestrictions.getRowCount();
    				
    				//awardRestrictionsForm.tblRestrictions.getCellEditor().stopCellEditing();
    				
    				if (col > awardRestrictionsForm.ASSIGNED_USER_NAME) {
    					col = 0;
    					row++;
    					if (row == rowCount) {
    						row = 0;
    					}

    					awardRestrictionsForm.tblRestrictions.changeSelection(row,col,false,false);
    					awardRestrictionsForm.tblRestrictions.requestFocus();
    				}
    				
    			}
    		}
    	});
    	
    	awardRestrictionsForm.tblRestrictions.addMouseListener(new MouseListener() {
    		public void mousePressed(MouseEvent e) {
    				awardRestrictionsForm.tblRestrictions.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    		}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// do nothing
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// do nothing
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// do nothing
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// do nothing
			}
    	});
    }
    
    class CommentsDocumentListener implements DocumentListener {
    	public void changedUpdate(DocumentEvent e) {
    	    // do nothing
    	}
    	public void removeUpdate(DocumentEvent e) {
    	    changeMade();
    	}
    	public void insertUpdate(DocumentEvent e) {
    	    changeMade();
    	}
    	
    	private void changeMade() {
	        selectedRow = awardRestrictionsForm.tblRestrictions.getSelectedRow();
	        if (selectedRow > -1) {
		        AwardRestrictionsBean bean = (AwardRestrictionsBean) cvRestrictions.get(selectedRow);
		        bean.setComments(awardRestrictionsForm.txtComments.getText().replaceAll("[\n\r]", " "));
		        cvRestrictions.set(selectedRow, bean);
		        setSaveRequired(true);
	        }
    	}
    }
    
    class TableListListener implements ListSelectionListener {
		@Override
        public void valueChanged(ListSelectionEvent e) {
            selectedRow = awardRestrictionsForm.tblRestrictions.getSelectedRow();
            if (selectedRow > -1) {
            	AwardRestrictionsBean bean = (AwardRestrictionsBean) cvRestrictions.get(selectedRow);
            	if (bean.getComments() != null) {
            		awardRestrictionsForm.txtComments.setText(bean.getComments());
            	}
            	else {
            		awardRestrictionsForm.txtComments.setText("");
            	}
            }
		}
    }
    
    /* To set the default focus */
    public void setDefaultFocusInWindow() {
        if (getFunctionType() == CoeusGuiConstants.DISPLAY_MODE) {
            awardRestrictionsForm.btnAdd.requestFocusInWindow();
        }
        else {
            if (awardRestrictionsForm.tblRestrictions.getRowCount() > 0) {
            	awardRestrictionsForm.tblRestrictions.requestFocusInWindow();
            }
        }
    }

    private CoeusVector getDataFromServer() throws CoeusClientException {
    	cvRestrictions = new CoeusVector();
    	
    	Vector dataObjects = new Vector();
    	dataObjects.add(awardBaseBean.getMitAwardNumber());
    	
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_AWARD_RESTRICTIONS);
        requesterBean.setDataObjects(dataObjects);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObject() != null) {
        	cvRestrictions = (CoeusVector) responderBean.getDataObject(); 
        }
        else {
        	throw new CoeusClientException("Cannot retrieve award restrictions data.",CoeusClientException.ERROR_MESSAGE);
        }
		return cvRestrictions;
    }
    
    public Vector getRestrictionCodes() throws CoeusClientException {
    	vecRestrictionCodes = new Vector();
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_AWARD_RESTRICTION_CODES);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObject() != null) {
        	vecRestrictionCodes = (Vector) responderBean.getDataObject();        	
        }
        else {
        	throw new CoeusClientException("Cannot retrieve award restriction types.",CoeusClientException.ERROR_MESSAGE);
        }
 		return vecRestrictionCodes;
    }
    
    public Vector getRestrictionMaintainers() throws CoeusClientException {
    	vecRestrictionMaintainers = new Vector();
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_RESTRICTION_MAINTAINERS);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObject() != null) {
        	vecRestrictionMaintainers = (Vector) responderBean.getDataObject();        	
        }
        else {
        	throw new CoeusClientException("Cannot retrieve restriction maintainers.",CoeusClientException.ERROR_MESSAGE);
        }
 		return vecRestrictionMaintainers;
    }
    
    public boolean isUserRestrictionsMaintainer(String userId) throws CoeusClientException {
    	int userHasRole = 0;
    	boolean hasRole = false;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(GET_USER_HAS_ROLE);
        Vector data = new Vector();
        data.add(userId.toUpperCase());
        data.add(ROLE);
        requesterBean.setDataObjects(data);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + USER_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getId() != null) {
        	userHasRole = Integer.parseInt((String) responderBean.getId());           	
        }
        else {
        	throw new CoeusClientException("Cannot retrieve user role information.",CoeusClientException.ERROR_MESSAGE);
        }
        if (userHasRole == 1) {
        	hasRole = true;
        }
        else {
        	hasRole = false;
        }
        return hasRole;
    }
    
    public void updateAwardRestrictions(CoeusVector data) throws CoeusClientException {
    	AwardRestrictionsBean bean = new AwardRestrictionsBean();
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(UPD_AWARD_RESTRICTIONS);
        requesterBean.setDataObject(data);
        
        /* Add deleted beans */
        if (cvDeleted.size() > 0) {
        	for (int c=0; c < cvDeleted.size(); c++) {
        		bean = (AwardRestrictionsBean) cvDeleted.get(c);
            	
        		if (bean.getRestrictionTypeCode() != null) {
        			data.add(bean);
        		}
        	}
        }
        
        for (int c=0; c < cvRestrictions.size(); c++) {
        	bean = (AwardRestrictionsBean) cvRestrictions.get(c);
        }
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObject() != null) {
        	// do nothing - it worked!        	
        }
        else {
        	throw new CoeusClientException("Cannot update award restrictions.",CoeusClientException.ERROR_MESSAGE);
        }
        
        /* This is needed to unset the ACTYPES so stuff doesn't get added twice */
        bean = new AwardRestrictionsBean();
        cvRestrictions.clear();
        cvRestrictions = getDataFromServer();
        
        for (int c=0; c < cvRestrictions.size(); c++) {
        	bean = (AwardRestrictionsBean) cvRestrictions.get(c);
        	bean.setAcType(null);
        	cvRestrictions.set(c, bean);
        }
        cvDeleted = new CoeusVector();
        awardRestrictionsTableModel.setData(cvRestrictions);
        
    	awardRestrictionsForm.updateUI();
    	setSaveRequired(false);
    }

    public void display() {

    }
    
    public boolean isTabEnabled(){
    	boolean editable = false;
        
        /* Does user have maintain permissions */
        boolean isMaintainer = false;
		try {
			isMaintainer = isUserRestrictionsMaintainer(mdiForm.getUserId());
		} catch (CoeusClientException e) {
			e.printStackTrace();
		}

        if ((getFunctionType() == TypeConstants.MODIFY_MODE) && (isMaintainer)) {
        	editable = true;
       	}
        
        return editable;
    }
    
    public Component getControlledUI() {
        return awardRestrictionsForm;
    }
    
    public Vector getFormData() {
    	return cvRestrictions;
    }
    
    public void saveFormData() {
    	awardRestrictionsForm.tblRestrictions.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    	if (awardRestrictionsForm.tblRestrictions.isEditing()) {
    		awardRestrictionsForm.tblRestrictions.getCellEditor().stopCellEditing();
    	}
    	
    	AwardRestrictionsBean bean = new AwardRestrictionsBean();
    	cvRestrictions = awardRestrictionsTableModel.getData();
    	for (int c=0; c < cvRestrictions.size(); c++) {
    		bean = (AwardRestrictionsBean) cvRestrictions.get(c);
    		bean.setUpdateUser(getLoginUsername());
    		if (bean.getAcType() == null) {
    			bean.setAcType(UPDATE_RECORD);
    		}
    		cvRestrictions.set(c, bean);
    		setSaveRequired(true);
    	}

    	if (isSaveRequired()) {
	    	try {
				updateAwardRestrictions(cvRestrictions);
			} 
	    	catch (CoeusClientException e) {
				System.out.println("Unable to update award restrictions");
				e.printStackTrace();
			}
    	}
    	awardRestrictionsForm.updateUI();
        setSaveRequired(false);
    }
    
    public void setFormData(Object restrictionsData) {

    }
    
    /**
     * Method to validate UI entries
     * @return boolean true if validation passed successfully
     */
    public boolean validate() {
    	boolean validated = false;
    	
        if (awardRestrictionsForm.tblRestrictions.getRowCount() > 0) {
        	cvRestrictions = awardRestrictionsTableModel.getData();
        	AwardRestrictionsBean bean = new AwardRestrictionsBean();
        	
        	boolean restrictionEmpty;
        	boolean statusEmpty;
        	boolean actionEmpty;
        	boolean dueEmpty;
        	boolean userEmpty;
        	
        	for (int c=0; c < cvRestrictions.size(); c++) {
        		validated = false;
            	restrictionEmpty = false;
            	statusEmpty = false;
            	actionEmpty = false;
            	dueEmpty = false;
            	userEmpty = false;
				String message = "";
        		
        		bean = (AwardRestrictionsBean) cvRestrictions.get(c);
        		if (bean.getRestrictionTypeCode() == null || bean.getRestrictionTypeCode() < 1) {
        			restrictionEmpty = true;
					message = "Restriction type must be selected";
        		}
        		if (bean.getStatus() == null) {
        			statusEmpty = true;
					message = "Status must be selected";
        		}
        		if (bean.getActionDate() == null || bean.getActionDate().toString().trim().isEmpty()) {
        			actionEmpty = true;
					message = "Action date cannot be empty";
        		}
        		if (bean.getDueDate() == null || bean.getDueDate().toString().trim().isEmpty()) {
        			dueEmpty = true;
					message = "Due date cannot be empty";
        		}
        		if (bean.getAssignedUser() == null) {
        			userEmpty = true;
					message = "Assigned user must be selected";
        		}
		        
		        if (message.length() > 0) {
		        	validated = false;
			        CoeusOptionPane.showErrorDialog(message);
			        break;
		        }
		        else {
		        	validated = true;
			    }
        	}
        }
    	else {
    		validated = true;
    	}
        
        return validated;
    }

    /* Show the alert messages */
    public void showErrorMessage(String mesg) throws CoeusUIException {
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(tabIndex);
        throw coeusUIException;        
    }

    public void setTabIndex(int tabIx) {
    	tabIndex = tabIx;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source.equals(awardRestrictionsForm.btnAdd)){
            performAdd();
        }
        else if(source.equals(awardRestrictionsForm.btnDelete)){
            performDelete();
        }
    }
    
    /* Get max restriction number */
    private int getMaxRestrictionNumber() {
    	int max = 0;
    	Integer restNum = 0;
    	Integer lastRestNum = 0;
    	AwardRestrictionsBean bean = new AwardRestrictionsBean();
    	for (int m=0; m < cvRestrictions.size(); m++) {
    		if (m > 0) {
    			lastRestNum = restNum;
    		}
    		bean = (AwardRestrictionsBean) cvRestrictions.get(m);
    		restNum = bean.getAwardRestrictionNumber();
    		max = Math.max(restNum, lastRestNum);
    	}
    	return max;
    }

    /* Add a restriction */
    private void performAdd() {
    	awardRestrictionsForm.updateUI();
    	int currRows = awardRestrictionsTableModel.getRowCount();
    	AwardRestrictionsBean bean = new AwardRestrictionsBean();
    	bean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
    	bean.setSequenceNumber(awardBaseBean.getSequenceNumber());
    	bean.setAwardRestrictionNumber(getMaxRestrictionNumber() + 1);
    	bean.setAcType(INSERT_RECORD);
    	cvRestrictions.add(bean);
    	awardRestrictionsTableModel.fireTableRowsInserted(currRows, currRows + 1);
    	
    	awardRestrictionsForm.tblRestrictions.setRowSelectionInterval(currRows, currRows);
    	awardRestrictionsForm.txtComments.setText("");
    	
    	awardRestrictionsForm.updateUI();
    	
    	setSaveRequired(true);
    }
       
    /* Delete a restriction */
    private void performDelete() {
    	int delRow = awardRestrictionsForm.tblRestrictions.getSelectedRow();
    	
    	int selectedOption = showDeleteConfirmMessage("Are you sure you want to delete this award restriction?");
    	
        if (selectedOption == CoeusOptionPane.SELECTION_YES) {
	    	if (selectedRow > -1) {
		    	AwardRestrictionsBean bean = new AwardRestrictionsBean();
		    	bean = (AwardRestrictionsBean) cvRestrictions.get(delRow);
		    	
		    	bean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
		    	bean.setSequenceNumber(awardBaseBean.getSequenceNumber());
		    	bean.setComments(null);
		    	bean.setAcType(DELETE_RECORD);
		    	cvDeleted.add(bean);
		    	cvRestrictions.remove(delRow);
		    	awardRestrictionsTableModel.fireTableRowsDeleted(delRow, delRow);
		    	awardRestrictionsForm.txtComments.setText(null);
		    	awardRestrictionsForm.updateUI();
		    	
        		setSaveRequired(true);
	    	}
        }

    }
    
    private String getLoginUsername() {
    	String loginUserName = CoeusGuiConstants.getMDIForm().getUserName();
    	return loginUserName;
    }
    
    /**
     * Displays the message passed to this method as a Confirmation Message.
     * @return selectedOption
     */
    private int showDeleteConfirmMessage(String msg){
        int selectedOption = CoeusOptionPane.showQuestionDialog(msg,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
        return  selectedOption;
    }
    
    @Override
    public void formatFields() {
    	// do nothing
    }

 }