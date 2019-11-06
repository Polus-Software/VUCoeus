/*
 * SubcontractListController.java
 *
 * Created on September 1, 2004, 6:49 PM
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.exception.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.saveas.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.subcontract.gui.SubcontractListForm;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.saveas.SaveAsDialog;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.subcontract.gui.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.bean.AuthorizationBean;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;



import java.beans.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;

import edu.mit.coeus.utils.ChangePassword;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  nadhgj
 */
public class SubcontractListController extends SubcontractController 
implements MouseListener,ActionListener,VetoableChangeListener, Observer  {
    
    private SubcontractListForm subcontractListForm;  
    
    /** Coeus Serach instance to search Subcontractss. */
    private CoeusSearch coeusSearch;
    
    private boolean closed = false;
    
    /** Holds the selectedRow in the subcontract List
     */
    private int baseTableRow;
        
    /** Holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private SubcontractBaseWindowController subcontractBaseWindowController;
    private QueryEngine queryEngine = QueryEngine.getInstance();
    private DateUtils dateUtils = new DateUtils();
    //Modified for case#3243
    private static final String REQUIRED_DATEFORMAT = "yyyy/MM/dd";
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private JTable tblResultsTable;
    private static final String AUTH_SERVLET = "/AuthorizationServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL; 
    private static final String SUBCONTRACT_LIST_SEARCH = "SUBCONTRACTSEARCH";
    
    private static final String NO_MORE_SUBCONTRACTS_TO_DISPLAY = "subcontractBasewindow_exceptionCode.1001";
    
    /* JM 3-16-2015 changes to column order */
    private static final int SUBCONTRACT_CODE_COLUMN = 0;
    private static final int SUBCONTRACTOR_NAME = 2;
    private static final int START_DATE = 6; // was 3
    private static final int END_DATE = 7; // was 4
    private static final int SUBAWARD_TYPE = 8; // was 5
    private static final int PURCHASE_ORDER_NUM = 18; // was 6
    private static final int TITLE = 9; // was 7
    private static final int STATUS = 10; // was 8
    private static final int ACCOUNT_NUM = 11; // was 9
    private static final int VENDOR_NUM = 17; // was 10
    private static final int REQUISITIONER = 3; // was 11
    private static final int R_UNIT_NUM = 4; // was 12
    private static final int R_UNIT_NAME = 5; // was 13
    private static final int ARCHIVE_LOCATION = 19; // was 14
    private static final int CLOSEOUT_DATE = 20; // was 15
    private static final int OBLIGATED_AMOUNT = 13; // was 16
    private static final int ANTICIPATED_AMT = 14; // was 17
    private static final int AMOUNT_RELEASED = 15; // was 18
    private static final int AVAILABLE_AMOUNT = 16; // was 19
    private static final int SITE_INVESTIGATOR = 12; // was 20
    /* JM END */
    
    
    private boolean createSubcontract, modifySubcontract, viewSubcontract;
    /* JM 2-26-2015 new role */
    private boolean modifySubcontractDocuments;
    /* JM END */

    private ChangePassword changePassword;
    
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    // 3587: Multi Campus Enahncements - Start
    private static final char CHECK_USER_HAS_MODIFY_RIGHT = 'M';
    private static final String GET_SERVLET = "/SubcontractMaintenenceServlet";
    // 3587: Multi Campus Enahncements - End
    /** Creates a new instance of SubcontractListController */
    public SubcontractListController() throws Exception{
        initComponents();
        authorizationCheck();
        registerComponents();
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        //Added by Nadh to get the column header and its status Start 18-01-2005
        if(mouseEvent.getSource() instanceof JTableHeader) {
            JTableHeader tblHeader = (JTableHeader) mouseEvent.getSource();
            TableColumnModel columnModel = tblHeader.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(mouseEvent.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            int sortStatus = getStatus();
            if(oldCol != column )
                sortStatus = MultipleTableColumnSorter.NOT_SORTED;
            sortStatus = sortStatus + (mouseEvent.isShiftDown() ? -1 : 1);
            sortStatus = (sortStatus + 4) % 3 - 1;
            setStatus(sortStatus);
            oldCol = column;
            if(getStatus()==MultipleTableColumnSorter.ASCENDING || getStatus() == MultipleTableColumnSorter.DESCENDING) {
                Vector newSortedData = new Vector();
                newSortedData.addElement(subcontractListForm.tblResults.getColumnName(column));
                newSortedData.addElement(new Integer(column));
                newSortedData.addElement(new Boolean(status == 1 ? true : false));
                if(vecSortedData == null)
                    vecSortedData = new Vector();
                vecSortedData.removeAllElements();
                vecSortedData.addElement(newSortedData);
            }else {
                vecSortedData = null;
            }
        }//End Nadh
        if(mouseEvent.getSource() instanceof JTable) {
            if(mouseEvent.getClickCount() != 2) return ;
            //Double Clicked on Table. open Subcontract in display Mode.
            displaySubcontract();
        }
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    /**
     * @param
     */    
    public void update(Observable observable, Object arg) {
        
        double obligatedAmount = 0;
        double anticipatedAmount = 0;
        double amountReleased = 0;
        String startDate = EMPTY_STRING;
        String endDate = EMPTY_STRING;
        String closeoutDate = EMPTY_STRING;
        CoeusVector cvData = new CoeusVector();
        //bug fix id 1651: start: step 2
        if(subcontractListForm == null)
            return;
        //bug fix id 1651: end
        //Modified for COEUSQA-2483 : Cannot open subcontract from list window - Start
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        DecimalFormat decimalFormat = (DecimalFormat)numberFormat;
        decimalFormat.applyPattern("###,###.###");
        //COEUSQA-2483 -  End
        int selectedRow = subcontractListForm.tblResults.getSelectedRow();
        SubContractAmountInfoBean subContractAmountInfoBean = new SubContractAmountInfoBean();
        SubContractAmountReleased subContractAmountReleased = new SubContractAmountReleased();
        if (arg instanceof Hashtable ){
            Hashtable subcontractData = (Hashtable)arg;
            cvData = (CoeusVector)subcontractData.get(SubContractBean.class);
            SubContractBean subcontractBean = (SubContractBean)cvData.get(0);
            cvData = (CoeusVector)subcontractData.get(SubContractAmountInfoBean.class);
            cvData.sort("lineNumber", false);
            if(cvData != null && cvData.size() > 0) {
                subContractAmountInfoBean = (SubContractAmountInfoBean)cvData.get(0);
                obligatedAmount = subContractAmountInfoBean.getObligatedAmount();
                anticipatedAmount = subContractAmountInfoBean.getAnticipatedAmount();
            }
            
            
            cvData = (CoeusVector)subcontractData.get(SubContractAmountReleased.class);
            cvData.sort("lineNumber", false);
            if(cvData != null && cvData.size() > 0) {
                subContractAmountReleased = (SubContractAmountReleased)cvData.get(0);
                amountReleased = subContractAmountReleased.getAmountReleased();
            }
            //Code modified for PT ID#3243 - start
            String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, REQUIRED_DATEFORMAT);            
            if(subcontractBean.getStartDate() != null) {
                //COEUSQA-1477 Dates in Search Results - Start
                startDate = dateUtils.parseDateForSearchResults(subcontractBean.getStartDate().toString(), dateFormat );
                //startDate = dateUtils.formatDate(subcontractBean.getStartDate().toString(), dateFormat );
                //COEUSQA-1477 Dates in Search Results - End
            }
            if(subcontractBean.getEndDate() != null) {
                //COEUSQA-1477 Dates in Search Results - Start
                endDate = dateUtils.parseDateForSearchResults(subcontractBean.getEndDate().toString(), dateFormat );
                //endDate = dateUtils.formatDate(subcontractBean.getEndDate().toString(), dateFormat );
                //COEUSQA-1477 Dates in Search Results - End
            }
            if(subcontractBean.getCloseOutDate() != null) {
                //COEUSQA-1477 Dates in Search Results - Start
                closeoutDate = dateUtils.parseDateForSearchResults(subcontractBean.getCloseOutDate().toString(), dateFormat );
                //closeoutDate = dateUtils.formatDate(subcontractBean.getCloseOutDate().toString(), dateFormat );
                //COEUSQA-1477 Dates in Search Results - End
            }
            
            //Code modified for PT ID#3243 - end
            if( ((BaseWindowObservable)observable).getFunctionType() == NEW_SUBCONTRACT ){
                Vector vecTableRow = new Vector();
                vecTableRow.addElement(subcontractBean.getSubContractCode());
                vecTableRow.addElement(subcontractBean.getSubContractId());
                vecTableRow.addElement(subcontractBean.getSubContractorName());
                vecTableRow.addElement(startDate);
                vecTableRow.addElement(endDate);
                vecTableRow.addElement(subcontractBean.getSubAwardTypeDescription());
                vecTableRow.addElement(subcontractBean.getPurchaseOrderNumber());
                vecTableRow.addElement(subcontractBean.getTitle());
                vecTableRow.addElement(subcontractBean.getStatusDescription());
                vecTableRow.addElement(subcontractBean.getAccountNumber());
                vecTableRow.addElement(subcontractBean.getVendorNumber());
                vecTableRow.addElement(subcontractBean.getRequisitionerName());
                vecTableRow.addElement(subcontractBean.getRequisitionerUnit());
                vecTableRow.addElement(subcontractBean.getRequisitionerUnitName());
                vecTableRow.addElement(subcontractBean.getArchiveLocation());
                vecTableRow.addElement(closeoutDate);
                //Modified for COEUSQA-2483 : Cannot open subcontract from list window - Start
//                vecTableRow.addElement(new Double(obligatedAmount).toString());
//                vecTableRow.addElement(new Double(anticipatedAmount).toString());
//                vecTableRow.addElement(new Double(amountReleased).toString());
//                vecTableRow.addElement(new Double(obligatedAmount - amountReleased).toString());
                String fmObligatedAmount = decimalFormat.format(obligatedAmount);
                if("0".equals(fmObligatedAmount)){
                    fmObligatedAmount = "0.0";
                }
                String fmAnticipatedAmount = decimalFormat.format(anticipatedAmount);
                if("0".equals(fmAnticipatedAmount)){
                    fmAnticipatedAmount = "0.0";
                }
                String fmAmountReleased = decimalFormat.format(amountReleased);
                if("0".equals(fmAmountReleased)){
                    fmAmountReleased = "0.0";
                }
                String fmAvailableAmt = decimalFormat.format(obligatedAmount - amountReleased);
                if("0".equals(fmAvailableAmt)){
                    fmAvailableAmt = "0.0";
                }
                vecTableRow.addElement(fmObligatedAmount);
                vecTableRow.addElement(fmAnticipatedAmount);
                vecTableRow.addElement(fmAmountReleased);
                vecTableRow.addElement(fmAvailableAmt);
                //COEUSQA-2483 : End
                
                int lastRow = subcontractListForm.tblResults.getRowCount();
                vecTableRow.addElement( EMPTY_STRING + lastRow);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).insertRow(lastRow, vecTableRow);
                // COEUSDEV - 265 : For input string error message - Start
//                if( lastRow == 0 ) {
//                    subcontractListForm.tblResults.setRowSelectionInterval(0,0);
//                }
                if( lastRow != -1 ) {
                    subcontractListForm.tblResults.setRowSelectionInterval(lastRow,lastRow);
                }
                // COEUSDEV - 265 : For input string error message - End
                baseTableRow = lastRow;
                subcontractListForm.tblResults.scrollRectToVisible(
                    subcontractListForm.tblResults.getCellRect(baseTableRow, SUBCONTRACT_CODE_COLUMN, true));
            //Code commented for PT ID#3243, setting of new values back to list window made mandatory even in ADD mode    
            }//else{
            baseTableRow = subcontractListForm.tblResults.getSelectedRow();
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    subcontractBean.getSubContractorName(), baseTableRow, SUBCONTRACTOR_NAME);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    startDate, baseTableRow, START_DATE);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    endDate, baseTableRow, END_DATE);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    subcontractBean.getSubAwardTypeDescription(), baseTableRow, SUBAWARD_TYPE);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    subcontractBean.getPurchaseOrderNumber(), baseTableRow, PURCHASE_ORDER_NUM);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    subcontractBean.getTitle(), baseTableRow, TITLE);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    subcontractBean.getStatusDescription(), baseTableRow, STATUS);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    subcontractBean.getAccountNumber(), baseTableRow, ACCOUNT_NUM);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    subcontractBean.getVendorNumber(), baseTableRow, VENDOR_NUM);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    subcontractBean.getRequisitionerName(), baseTableRow, REQUISITIONER);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    subcontractBean.getRequisitionerUnit(), baseTableRow, R_UNIT_NUM);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    subcontractBean.getRequisitionerUnitName(), baseTableRow, R_UNIT_NAME);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    subcontractBean.getArchiveLocation(), baseTableRow, ARCHIVE_LOCATION);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
                    closeoutDate, baseTableRow, CLOSEOUT_DATE);
                //Modified for COEUSQA-2483 : Cannot open subcontract from list window - Start
//                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
//                    new Double(subContractAmountInfoBean.getObligatedAmount()).toString(), baseTableRow, OBLIGATED_AMOUNT);
//                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
//                   new Double(subContractAmountInfoBean.getAnticipatedAmount()).toString(), baseTableRow, ANTICIPATED_AMT);
//                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
//                    new Double(subContractAmountReleased.getAmountReleased()).toString(), baseTableRow, AMOUNT_RELEASED);
//                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(
//                    new Double(obligatedAmount - amountReleased).toString(), baseTableRow, AVAILABLE_AMOUNT);
                String fmObligatedAmount = decimalFormat.format(subContractAmountInfoBean.getObligatedAmount());
                if("0".equals(fmObligatedAmount)){
                    fmObligatedAmount = "0.0";
                }
                String fmAnticipatedAmount = decimalFormat.format(subContractAmountInfoBean.getAnticipatedAmount());
                if("0".equals(fmAnticipatedAmount)){
                    fmAnticipatedAmount = "0.0";
                }
                String fmAmountReleased = decimalFormat.format(subContractAmountReleased.getAmountReleased());
                if("0".equals(fmAmountReleased)){
                    fmAmountReleased = "0.0";
                }
                String fmAvailableAmt = decimalFormat.format(obligatedAmount - amountReleased);
                if("0".equals(fmAvailableAmt)){
                    fmAvailableAmt = "0.0";
                }
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(fmObligatedAmount
                        , baseTableRow, OBLIGATED_AMOUNT);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(fmAnticipatedAmount
                        , baseTableRow, ANTICIPATED_AMT);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(fmAmountReleased, 
                        baseTableRow, AMOUNT_RELEASED);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(fmAvailableAmt
                    , baseTableRow, AVAILABLE_AMOUNT);
                ((DefaultTableModel)subcontractListForm.tblResults.getModel()).setValueAt(subcontractBean.getSiteInvestigatorName()
                    , baseTableRow, SITE_INVESTIGATOR);
                //COEUSQA-2483 : End
                int selRow = subcontractListForm.tblResults.getSelectedRow();
                if(  selRow != -1 ) {
                    // 3587: Multi Campus Enahncements - Start
//                    baseTableRow = Integer.parseInt((String)subcontractListForm.tblResults.getValueAt(
//                    selRow,subcontractListForm.tblResults.getColumnCount() -1 ));
                    //Modified for COEUSDEV - 265 : For input string error message - Start
                    //When a new Subcontract is created selected row index is taken, otherwise
                    //index is taken from the last row
//                    String strRow = (String)subcontractListForm.tblResults.getValueAt(
//                            selRow,subcontractListForm.tblResults.getColumnCount() -1 );
//                    if("0.0".equals(strRow)){
//                        baseTableRow = 0;
//                    } else {
//                        baseTableRow = Integer.parseInt(strRow);
//                    }
                    if(((BaseWindowObservable)observable).getFunctionType() == NEW_SUBCONTRACT){
                        baseTableRow = selRow;
                    } else {
                        String strRow = (String)subcontractListForm.tblResults.getValueAt(
                                selRow,subcontractListForm.tblResults.getColumnCount() -1 );
                        //Added for COEUSQA-2483 : Cannot open subcontract from list window  - Start
                        if(strRow != null){
                            strRow = strRow.replace(",","");
                            //Subcontract save issue -Start
                            int totalRows = subcontractListForm.tblResults.getRowCount();
                            //COEUSQA-2483 : End
                            //if(strRow.indexOf(".") >= 0){
                            if(strRow.indexOf(".") >= 0 || totalRows==1){
                            //Subcontract save issue -End
                                baseTableRow = selRow;
                            }else{
                                baseTableRow = Integer.parseInt(strRow);
                            }
                        }
                    }
                    //COEUSDEV-265 : End
                    
                    // 3587: Multi Campus Enahncements - End
                }
            //}            
        }else if(arg.equals("NEXT_SUBCONTRACT")) {
            int totalRows = subcontractListForm.tblResults.getRowCount();
            if((totalRows-1) == selectedRow) {
               CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
               NO_MORE_SUBCONTRACTS_TO_DISPLAY));
               return ;
            }
            selectedRow++;
            CoeusVector cvNewData = new CoeusVector();
            cvNewData.add(subcontractListForm.tblResults.getValueAt(selectedRow, OBLIGATED_AMOUNT).toString());
            cvNewData.add(subcontractListForm.tblResults.getValueAt(selectedRow, ANTICIPATED_AMT).toString());
            cvNewData.add(subcontractListForm.tblResults.getValueAt(selectedRow, AMOUNT_RELEASED).toString());
            cvNewData.add(subcontractListForm.tblResults.getValueAt(selectedRow, AVAILABLE_AMOUNT).toString());
            subcontractListForm.tblResults.setRowSelectionInterval(selectedRow, selectedRow);
            String subcontractCode = subcontractListForm.tblResults.getValueAt(selectedRow, SUBCONTRACT_CODE_COLUMN).toString();
            SubContractBean subContractBean = new SubContractBean();
            subContractBean.setSubContractCode(subcontractCode);
            subcontractBaseWindowController.clearOldInstance();
            subcontractBaseWindowController.setAmounts(cvNewData);
            cvNewData = null;
            setFunctionType(TypeConstants.DISPLAY_MODE);
            subcontractBaseWindowController.performNavigation(subcontractCode);
        }else if(arg.equals("PREVIOUS_SUBCONTRACT")) {
            
             if(selectedRow == 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                NO_MORE_SUBCONTRACTS_TO_DISPLAY));
                return ;
             }
            selectedRow--;
            CoeusVector cvNewData = new CoeusVector();
            cvNewData.add(subcontractListForm.tblResults.getValueAt(selectedRow, OBLIGATED_AMOUNT).toString());
            cvNewData.add(subcontractListForm.tblResults.getValueAt(selectedRow, ANTICIPATED_AMT).toString());
            cvNewData.add(subcontractListForm.tblResults.getValueAt(selectedRow, AMOUNT_RELEASED).toString());
            cvNewData.add(subcontractListForm.tblResults.getValueAt(selectedRow, AVAILABLE_AMOUNT).toString());
            String subcontractCode = subcontractListForm.tblResults.getValueAt(selectedRow, SUBCONTRACT_CODE_COLUMN).toString();
            subcontractListForm.tblResults.setRowSelectionInterval(selectedRow, selectedRow);
            SubContractBean subContractBean = new SubContractBean();
            subContractBean.setSubContractCode(subcontractCode);
            subcontractBaseWindowController.clearOldInstance();
            subcontractBaseWindowController.setAmounts(cvNewData);
            cvNewData = null;
            setFunctionType(TypeConstants.DISPLAY_MODE);
            subcontractBaseWindowController.performNavigation(subcontractCode);
        }
    }
    
    /**
     * initialize controller componentes.
     */
    
    private void initComponents() {
        try{
            subcontractListForm = new 
            SubcontractListForm("Subcontract List", mdiForm);
            coeusSearch = new CoeusSearch(mdiForm, SUBCONTRACT_LIST_SEARCH , 0);
            JTable tblResults = coeusSearch.getEmptyResTable();
            subcontractListForm.initComponents(tblResults);
            
        }catch (Exception exception) {
                
            exception.printStackTrace();
        }
    }
    
    /** displays Subcontract Search Window. */
    private void showSubcontractSearch() {
        try {
           
            coeusSearch.showSearchWindow();
            tblResultsTable = coeusSearch.getSearchResTable();
            subcontractListForm.displayResults(tblResultsTable);
             //Case 2451 start
            vecSortedData = new Vector();
            //Case 2451 end
           // adding listener for table.
            if(tblResultsTable != null) {
                subcontractListForm.tblResults.addMouseListener(this);
                subcontractListForm.tblResults.getTableHeader().addMouseListener(this);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Displays Subcontract List. */
    public void display() {
        try{
            mdiForm.putFrame(CoeusGuiConstants.SUBCONTRACT_FRAME_TITLE, subcontractListForm);
            mdiForm.getDeskTopPane().add(subcontractListForm);
            subcontractListForm.setSelected(true);
            subcontractListForm.setVisible(true);
            showSubcontractSearch();
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return subcontractListForm;
    }
    
    public Object getFormData() {
        return subcontractListForm;
    }
    
    public void saveFormData() {
    }
    
    public void setFormData(Object data) {
    }
    
    public boolean validate() throws CoeusUIException {
        return true;
    }
    
    /** 
     * registers the components with event listeners. 
     */
    
    public void registerComponents() {
        
        subcontractListForm.addVetoableChangeListener(this);
        // Setting listener for the Tool bar buttons
        
        subcontractListForm.btnClose.addActionListener(this);
        subcontractListForm.btnCorrectSubcontract.addActionListener(this);
        subcontractListForm.btnDisplaySubcontract.addActionListener(this);
        subcontractListForm.btnSortSubcontracts.addActionListener(this);
        subcontractListForm.btnNewSubcontract.addActionListener(this);
        subcontractListForm.btnSaveAs.addActionListener(this);
        subcontractListForm.btnSearchSubcontract.addActionListener(this);
        subcontractListForm.btnNewEntry.addActionListener(this);
        
        //Setting the listener for the  file menu items
        
        subcontractListForm.mnuItmChangePassword.addActionListener(this);
        subcontractListForm.mnuItmClose.addActionListener(this);
        subcontractListForm.mnuItmPreferences.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        subcontractListForm.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        subcontractListForm.mnuItmSaveas.addActionListener(this);
        subcontractListForm.mnuItmInbox.addActionListener(this);
        subcontractListForm.mnuItmExit.addActionListener(this);
        subcontractListForm.mnuItmSort.addActionListener(this);
        //Case 2110 Start
        subcontractListForm.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        //Setting the listener for the  edit menu items
        
        subcontractListForm.mnuItmCorrectSubcontract.addActionListener(this);
        subcontractListForm.mnuItmDisplaySubcontract.addActionListener(this);
        subcontractListForm.mnuItmNewEntry.addActionListener(this);
        subcontractListForm.mnuItmNewSubcontract.addActionListener(this);
        
        //Setting the listener for the  tools menu items
        
        subcontractListForm.mnuItmSearch.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        blockEvents(true);
        try{
            if(source.equals(subcontractListForm.mnuItmCorrectSubcontract) ||
            source.equals(subcontractListForm.btnCorrectSubcontract)){
                correctSubcontract();
            }else if(source.equals(subcontractListForm.mnuItmDisplaySubcontract) ||
            source.equals(subcontractListForm.btnDisplaySubcontract)){
                displaySubcontract();
            }else if(source.equals(subcontractListForm.btnSearchSubcontract)
            || source.equals(subcontractListForm.mnuItmSearch)){
                showSubcontractSearch();
            }else if(source.equals(subcontractListForm.mnuItmNewSubcontract) ||
            source.equals(subcontractListForm.btnNewSubcontract)){
                newSubcontract();
            }else if(source.equals(subcontractListForm.btnClose) ||
            source.equals(subcontractListForm.mnuItmClose)){
                close();
            }else if(source.equals(subcontractListForm.mnuItmNewEntry) ||
            source.equals(subcontractListForm.btnNewEntry)){
                newEntry();
            }else if(source.equals(subcontractListForm.mnuItmSaveas) ||
            source.equals(subcontractListForm.btnSaveAs)){
                saveSubcontractList();
            }else if(source.equals(subcontractListForm.mnuItmChangePassword)){
                showChangePassword();
            }else if(source.equals(subcontractListForm.mnuItmPreferences)) {
                showPreference();
            //Added for Case#3682 - Enhancements related to Delegations - Start
            }else if(source.equals(subcontractListForm.mnuItmDelegations)) {
               displayUserDelegation();
            //Added for Case#3682 - Enhancements related to Delegations - End
            }else if(source.equals(subcontractListForm.mnuItmSort) ||
            source.equals(subcontractListForm.btnSortSubcontracts)){
                showSort();
            }else if(source.equals(subcontractListForm.mnuItmExit)){
                exitApplication();//bug fix 1651
            }else if(source.equals(subcontractListForm.mnuItmInbox)){
                showInboxDetails();
            }//Case 2110 Start
            else if(source.equals(subcontractListForm.mnuItmCurrentLocks)){
                showLocksForm();
            }//Case 2110 End
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        blockEvents(false);
    }
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     *Display Delegations window
     */
    private void displayUserDelegation() {
      userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    // Added by surekha to implement the User Preference details
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }// End surekha

    
    /** displays inbox details. */
    private void showInboxDetails() {
        InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame(
            "Inbox" ))!= null ){
                if( inboxDtlForm.isIcon() ){
                    inboxDtlForm.setIcon(false);
                }
                inboxDtlForm.setSelected( true );
                return;
            }
            inboxDtlForm = new InboxDetailForm(mdiForm);
            inboxDtlForm.setVisible(true);
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    
    //Case 2110 Start TO show the Current Locks of LoggedInUser    
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
     //Case 2110 End
    
    // added by Nadh to implement sorting proposals start - 19-01-2004
    /*
     * this method shows the sort window
     * return void
     */
    private void showSort() {
        if(vecSortedData==null) {
            vecSortedData = new Vector();
        }
        SortForm sortForm = new SortForm(subcontractListForm.tblResults,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(subcontractListForm.tblResults,vecSortedData);
        else
            return;
    }// Added by Nadh - end

    // Added by Nadh to implement the change password
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }// End Nadh
    
    /** Opens subcontract in Modify mode
     */
   private void correctSubcontract(){
        baseTableRow = subcontractListForm.tblResults.getSelectedRow();
        if(baseTableRow == -1)return ;
        
        String subcontractCode = subcontractListForm.tblResults.getValueAt(baseTableRow, SUBCONTRACT_CODE_COLUMN).toString();
        
        // For Locking mechanism
        if(isSubcontractOpen(subcontractCode, CoeusGuiConstants.MODIFY_MODE)) {
          return ;
        }
        // 3587: Multi Campus Enahncements - Start
        boolean hasModifyRight = checkUserHasModifyRight(subcontractCode);
        // JM check for new role as well
        if(!hasModifyRight && !modifySubcontractDocuments){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("subcontractBasewindow_exceptionCode.1005"));
            return ;
        } 
        // JM END
        // 3587: Multi Campus Enahncements- End
        SubContractBean subContractBean = new SubContractBean();
        subContractBean.setSubContractCode(subcontractCode);
        try {
        SubcontractBaseWindowController subcontractBaseWindowController = 
            new SubcontractBaseWindowController("Correct Subcontract ", CoeusGuiConstants.MODIFY_MODE, subContractBean,getAmountData(baseTableRow));
        subcontractBaseWindowController.registerObserver(this);
        
        subcontractBaseWindowController.display();
        }catch(CoeusUIException coeusUIException) {
            if(!coeusUIException.getMessage().equals("null.")) {
                  CoeusOptionPane.showDialog(coeusUIException);
            }
        }
        
    }
   
   /** Opens the subcontract in display mode
     */
    private void displaySubcontract(){     
        baseTableRow = subcontractListForm.tblResults.getSelectedRow();
        if(baseTableRow == -1)return ;
        
        String subcontractCode = subcontractListForm.tblResults.getValueAt(baseTableRow, SUBCONTRACT_CODE_COLUMN).toString();
        // For Locking mechanism
        if(isSubcontractOpen(subcontractCode, CoeusGuiConstants.DISPLAY_MODE)) {
          return ;
        }
        SubContractBean subContractBean = new SubContractBean();
        subContractBean.setSubContractCode(subcontractCode);
        
        try{
        subcontractBaseWindowController = 
            new SubcontractBaseWindowController("Display Subcontract ", CoeusGuiConstants.DISPLAY_MODE, subContractBean,getAmountData(baseTableRow));
        subcontractBaseWindowController.registerObserver(this);
        subcontractBaseWindowController.display();
        }catch(CoeusUIException coeusUIException) {
            if(!coeusUIException.getMessage().equals("null.")) {
                  CoeusOptionPane.showDialog(coeusUIException);
            }
        }
        
    }
    
    /**
     * saves subcontract list to the external file
     */
    private void saveSubcontractList() {
        //Modified for Case#2908-Exports from Search Results Do Not Preserve Data Format - Start
        //SaveAsDialog saveAsDialog = new SaveAsDialog(tblResultsTable);
        SaveAsDialog saveAsDialog = new SaveAsDialog(subcontractListForm.tblResults);
        //Case#2908 - End
        
    }
    
    /**
     * Method used to close the application after confirmation.
     */
    public void exitApplication(){
        //start of bug fix id 1651 : step 1
        String message = coeusMessageResources.parseMessageKey(
                                    "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
        }
        //End of bug fix id 1651
    }
    

    /** Opens subcontract in New mode
     */
  
    private void newSubcontract() {
        SubcontractBaseWindow subcontractBaseWindow = null;
        if(isSubcontractOpen(EMPTY_STRING, CoeusGuiConstants.MODIFY_MODE)) {
          return ;
        }
        CoeusVector cvData = new CoeusVector();
        cvData.add(new Double(0));
        cvData.add(new Double(0));
        cvData.add(new Double(0));
        cvData.add(new Double(0));
        
        try{
            SubcontractBaseWindowController subcontractBaseWindowController = 
                new SubcontractBaseWindowController("New Subcontract ", NEW_SUBCONTRACT,null,cvData);
            baseTableRow = subcontractListForm.tblResults.getSelectedRow();
            subcontractBaseWindowController.registerObserver(this);
            subcontractBaseWindowController.display();
        }catch(CoeusUIException coeusUIException) {
            if(!coeusUIException.getMessage().equals("null.")) {
                  CoeusOptionPane.showDialog(coeusUIException);
            }
        }
        cvData = null;
        
    }
    
     /** 
      * closes this window. 
      */
    
    private void close() {
        mdiForm.removeFrame(CoeusGuiConstants.SUBCONTRACT_FRAME_TITLE);
        closed = true;
        //select next Internal Frame.
        subcontractListForm.doDefaultCloseAction();
    }
    
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
         if(closed) return ;
        
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
            cleanUp();
        }
    }
    
    /** Opens subcontract in New Entry mode
     */
    
    private void newEntry() {
        baseTableRow = subcontractListForm.tblResults.getSelectedRow();
        if(baseTableRow == -1)return ;
        
        String subcontractCode = subcontractListForm.tblResults.getValueAt(baseTableRow, SUBCONTRACT_CODE_COLUMN).toString();
        
        if(isSubcontractOpen(subcontractCode, CoeusGuiConstants.MODIFY_MODE)) {
          return ;
        }
        
        // 3587: Multi Campus Enahncements - Start
        boolean hasModifyRight = checkUserHasModifyRight(subcontractCode);
        if(!hasModifyRight){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("subcontractBasewindow_exceptionCode.1005"));
            return ;
        }
        // 3587: Multi Campus Enahncements- End

        SubContractBean subContractBean = new SubContractBean();
        subContractBean.setSubContractCode(subcontractCode);
        
        try{
            SubcontractBaseWindowController subcontractBaseWindowController = new SubcontractBaseWindowController("Subcontract New Entry : ", NEW_ENTRY_SUBCONTRACT, subContractBean,getAmountData(baseTableRow));
            subcontractBaseWindowController.registerObserver(this);
            subcontractBaseWindowController.display();
        }catch(CoeusUIException coeusUIException) {
            if(!coeusUIException.getMessage().equals("null.")) {
                  CoeusOptionPane.showDialog(coeusUIException);
            }
        }
        
        
    }
    
    /** 
     * this method cleans up the not reachble resources 
     * @returns void.
     */
    
    public void cleanUp(){
        subcontractBaseWindowController = null;
        subcontractListForm = null;
        coeusMessageResources = null;
        coeusSearch = null;
        dateUtils = null;
        queryEngine = null;
        tblResultsTable = null;
        //for bug fix id 1651 start : step 3
        //mdiForm = null; 
       //  bug fix id 1651 end
        subContractBean = null;
    }
    /**
     * this method gets amount data from the list window.
     * @param selRow is selected row index.
     * @returns CoeusVector.
     */
    private CoeusVector getAmountData(int selRow) {
        CoeusVector coeusVector = new CoeusVector();

        double obligatedAmt = subcontractListForm.tblResults.getValueAt(selRow, OBLIGATED_AMOUNT).toString()== EMPTY_STRING ? 0
                           : Double.parseDouble(
                subcontractListForm.tblResults.getValueAt(selRow, OBLIGATED_AMOUNT).toString().replace(",",""));

        double releasedAmt = subcontractListForm.tblResults.getValueAt(selRow, AMOUNT_RELEASED).toString()== EMPTY_STRING ? 0 
                           : Double.parseDouble(
                subcontractListForm.tblResults.getValueAt(selRow, AMOUNT_RELEASED).toString().replace(",",""));
        double availableAmt = obligatedAmt - releasedAmt;
        coeusVector.add(new Double(obligatedAmt));
        coeusVector.add(subcontractListForm.tblResults.getValueAt(selRow, ANTICIPATED_AMT).toString()== EMPTY_STRING ? new Double(0).toString() 
                           : subcontractListForm.tblResults.getValueAt(selRow, ANTICIPATED_AMT).toString().replace(",",""));
        coeusVector.add(new Double(releasedAmt));
        coeusVector.add(new Double(availableAmt));
        return coeusVector;
    }
    
    /**
     * this method checks for user rights.
     */
    
    private void authorizationCheck() {
        RequesterBean requester;
        ResponderBean responder;
        
        requester = new RequesterBean();
        Hashtable authorizations = new Hashtable();
        
        AuthorizationBean authorizationBean;
        AuthorizationOperator authorizationOperator;
        String CREATE_SUBCONTRACT = "CREATE_SUBCONTRACT";
        String MODIFY_SUBCONTRACT = "MODIFY_SUBCONTRACT";
        String VIEW_SUBCONTRACT = "VIEW_SUBCONTRACT";
        /* JM 2-24-2015 check for new role */
        String MODIFY_SUBCONTRACT_DOCUMENTS = "MODIFY_SUBCONTRACT_DOCUMENTS";
        /* JM END */
        
        // Determine whether user has right to create an subcontract
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(CREATE_SUBCONTRACT);
        // 3587: Multi Campus Enahncements - Sraer
//        authorizationBean.setFunctionType("OSP");
        authorizationBean.setFunctionType("RIGHT_ID");
        // 3587: Multi Campus Enahncements - End
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(CREATE_SUBCONTRACT, authorizationOperator);
        
        // Determine whether user has right to modify an subcontract
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(MODIFY_SUBCONTRACT);
        // 3587: Multi Campus Enahncements- Start
//        authorizationBean.setFunctionType("OSP");
        authorizationBean.setFunctionType("RIGHT_ID");
        // 3587: Multi Campus Enahncements - End
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(MODIFY_SUBCONTRACT, authorizationOperator);
        
        // Determine whether user has right to display an subcontract
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(VIEW_SUBCONTRACT);
        authorizationBean.setFunctionType("RIGHT_ID");
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(VIEW_SUBCONTRACT, authorizationOperator);
        
        /* JM 2-24-2015 check for new role */
        authorizationBean = new AuthorizationBean();
        authorizationBean.setFunction(MODIFY_SUBCONTRACT_DOCUMENTS);
        authorizationBean.setFunctionType("RIGHT_ID");
        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
        authorizationOperator = new AuthorizationOperator(authorizationBean);
        authorizations.put(MODIFY_SUBCONTRACT_DOCUMENTS, authorizationOperator);
        /* JM END */
        
        requester.setAuthorizationOperators(authorizations);
        requester.setIsAuthorizationRequired(true);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + AUTH_SERVLET, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            authorizations = responder.getAuthorizationOperators();
        }else{
            CoeusOptionPane.showInfoDialog(responder.getMessage());
        }
        
        createSubcontract = ((Boolean)authorizations.get(CREATE_SUBCONTRACT)).booleanValue();
        modifySubcontract = ((Boolean)authorizations.get(MODIFY_SUBCONTRACT)).booleanValue();
        viewSubcontract = ((Boolean)authorizations.get(VIEW_SUBCONTRACT)).booleanValue();
        /* JM get new role */
        modifySubcontractDocuments = ((Boolean)authorizations.get(MODIFY_SUBCONTRACT_DOCUMENTS)).booleanValue();
        
        subcontractListForm.mnuItmNewSubcontract.setEnabled(createSubcontract);
        subcontractListForm.btnNewSubcontract.setEnabled(createSubcontract);
        
        /* JM 2-26-2015 change to check both roles
        subcontractListForm.mnuItmCorrectSubcontract.setEnabled(modifySubcontract);
        subcontractListForm.btnCorrectSubcontract.setEnabled(modifySubcontract);
        subcontractListForm.mnuItmDisplaySubcontract.setEnabled(modifySubcontract);
        subcontractListForm.mnuItmNewEntry.setEnabled(modifySubcontract);
        subcontractListForm.btnNewEntry.setEnabled(modifySubcontract);
        */
        boolean hasModify = false;
        if (modifySubcontract || modifySubcontractDocuments) {
        	hasModify = true;
        }
        
        subcontractListForm.mnuItmCorrectSubcontract.setEnabled(hasModify);
        subcontractListForm.btnCorrectSubcontract.setEnabled(hasModify);
        subcontractListForm.mnuItmDisplaySubcontract.setEnabled(hasModify);
        subcontractListForm.mnuItmNewEntry.setEnabled(hasModify);
        subcontractListForm.btnNewEntry.setEnabled(hasModify);        
        /* JM END */
        
        subcontractListForm.mnuItmDisplaySubcontract.setEnabled(viewSubcontract);
        subcontractListForm.btnDisplaySubcontract.setEnabled(viewSubcontract);
       
        
    }
    
    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public int getStatus() {
        return status;
    }
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    private boolean checkUserHasModifyRight(String subcontractCode) {
        boolean modifyRight = false;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CHECK_USER_HAS_MODIFY_RIGHT);
        requesterBean.setDataObject(subcontractCode);
        
        AppletServletCommunicator appletServletCommunicator = new
                AppletServletCommunicator(connect + GET_SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean != null) {
            if(responderBean.isSuccessfulResponse()) {
                Boolean right = (Boolean) responderBean.getDataObject();
                modifyRight = right.booleanValue();
            }
        }
        return modifyRight;
    }
    // 3587: Multi Campus Enahncements - End
}
