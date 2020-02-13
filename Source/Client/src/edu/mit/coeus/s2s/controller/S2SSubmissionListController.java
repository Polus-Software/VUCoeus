/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * S2SSubmissionListController.java
 */
package edu.mit.coeus.s2s.controller;

/**
 *
 * @author  Geo Thomas
 */


import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;

import edu.mit.coeus.exception.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.saveas.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.AuthorizationBean;
import edu.mit.coeus.utils.query.AuthorizationOperator;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.propdev.gui.ProposalDetailForm;
import edu.mit.coeus.s2s.bean.ProcessGrantsSubmission;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.gui.S2SSubmissionList;
import edu.mit.coeus.search.bean.DisplayBean;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/** Controller for Award List. */
public class S2SSubmissionListController implements ActionListener,
VetoableChangeListener, MouseListener{
    
    /** holds Award List instance to be controlled. */
    private S2SSubmissionList s2sSubList;
    
    /** Coeus Serach instance to search Awards. */
    private CoeusSearch coeusSearch;
    
    /** Holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private JTable tblResultsTable;
    
    //Case#2908 - Exports from Search Results Do Not Preserve Data Format  - Start
    private JTable tblResults;
    //Case#2908 - End
    
    private static final String S2S_SEARCH = "S2SSUBMISSIONSEARCH";
    
    private static final String SERVLET = "/S2SServlet";
    
    private static final String AUTH_SERVLET = "/AuthorizationServlet";
    
    private static int S2S_COLUMN = -1;
    
    private static final String FUNC_NOT_IMPL = "Functionality not implemented";
    
    private static final String NO_MORE_S2S_TO_DISPLAY = "There are no more submissions to display";
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    
    /** Creates a new instance of AwardBaseWindowController */
    public S2SSubmissionListController() {
        initComponents();
//        authorizationCheck();
        registerComponents();
    }
    
    private void initComponents() {
        try{
            s2sSubList = new S2SSubmissionList("S2S Submission List", mdiForm);
            coeusSearch = new CoeusSearch(mdiForm, S2S_SEARCH, 0);
            JTable tblResults = coeusSearch.getEmptyResTable();
            s2sSubList.initComponents(tblResults);
        }catch (Exception exception) {
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    private void authorizationCheck() {//needs to change
//        RequesterBean requester;
//        ResponderBean responder;
//        
//        requester = new RequesterBean();
//        Hashtable authorizations = new Hashtable();
//        
//        AuthorizationBean authorizationBean;
//        AuthorizationOperator authorizationOperator;
//        
//        String CREATE_AWARD, MODIFY_AWARD, VIEW_AWARD, MAINTAIN_REPORTING ,MODIFY_SUBCONTRACT , VIEW_SUBCONTRACT;
//        CREATE_AWARD = "CREATE_AWARD";
//        MODIFY_AWARD = "MODIFY_AWARD";
//        VIEW_AWARD = "VIEW_AWARD";
//        MAINTAIN_REPORTING = "MAINTAIN_REPORTING";
//        MODIFY_SUBCONTRACT = "MODIFY_SUBCONTRACT";
//        VIEW_SUBCONTRACT = "VIEW_SUBCONTRACT";
//        
//        // Determine whether user has right to create an award
//        authorizationBean = new AuthorizationBean();
//        authorizationBean.setFunction(CREATE_AWARD);
//        authorizationBean.setFunctionType("OSP");
//        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
//        authorizationOperator = new AuthorizationOperator(authorizationBean);
//        authorizations.put(CREATE_AWARD, authorizationOperator);
//        
//        // Determine whether user has right to modify an award
//        authorizationBean = new AuthorizationBean();
//        authorizationBean.setFunction(MODIFY_AWARD);
//        authorizationBean.setFunctionType("OSP");
//        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
//        authorizationOperator = new AuthorizationOperator(authorizationBean);
//        authorizations.put(MODIFY_AWARD, authorizationOperator);
//        
//        // Determine whether user has right to display an Award
//        authorizationBean = new AuthorizationBean();
//        authorizationBean.setFunction(VIEW_AWARD);
//        authorizationBean.setFunctionType("RIGHT_ID");
//        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
//        authorizationOperator = new AuthorizationOperator(authorizationBean);
//        authorizations.put(VIEW_AWARD, authorizationOperator);
//        
//        authorizationBean = new AuthorizationBean();
//        authorizationBean.setFunction(MAINTAIN_REPORTING);
//        authorizationBean.setFunctionType("OSP");
//        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
//        authorizationOperator = new AuthorizationOperator(authorizationBean);
//        authorizations.put(MAINTAIN_REPORTING, authorizationOperator);
//        
//        // Determine whether user has right to modify an subcontract
//        authorizationBean = new AuthorizationBean();
//        authorizationBean.setFunction(MODIFY_SUBCONTRACT);
//        authorizationBean.setFunctionType("OSP");
//        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
//        authorizationOperator = new AuthorizationOperator(authorizationBean);
//        authorizations.put(MODIFY_SUBCONTRACT, authorizationOperator);
//        
//        // Determine whether user has right to display an subcontract
//        authorizationBean = new AuthorizationBean();
//        authorizationBean.setFunction(VIEW_SUBCONTRACT);
//        authorizationBean.setFunctionType("RIGHT_ID");
//        authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
//        authorizationOperator = new AuthorizationOperator(authorizationBean);
//        authorizations.put(VIEW_SUBCONTRACT, authorizationOperator);
//        
//        requester.setAuthorizationOperators(authorizations);
//        requester.setIsAuthorizationRequired(true);
//        
//        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + AUTH_SERVLET, requester);
//        
//        comm.send();
//        responder = comm.getResponse();
//        if(responder.isSuccessfulResponse()){
//            authorizations = responder.getAuthorizationOperators();
//        }else{
//            CoeusOptionPane.showInfoDialog(responder.getMessage());
//        }
//        
//        createAward = ((Boolean)authorizations.get(CREATE_AWARD)).booleanValue();
//        modifyAward = ((Boolean)authorizations.get(MODIFY_AWARD)).booleanValue();
//        viewAward = ((Boolean)authorizations.get(VIEW_AWARD)).booleanValue();
//        maintainReporting = ((Boolean)authorizations.get(MAINTAIN_REPORTING)).booleanValue();
//        modifySubcontract = ((Boolean)authorizations.get(MODIFY_SUBCONTRACT)).booleanValue();
//        viewSubcontract = ((Boolean)authorizations.get(VIEW_SUBCONTRACT)).booleanValue();
//        
//        awardList.mnuItmNewAward.setEnabled(createAward);
//        awardList.btnAwardNewEntry.setEnabled(createAward);
//        
//        awardList.mnuItmCorrectAward.setEnabled(modifyAward);
//        awardList.btnCorrectAward.setEnabled(modifyAward);
//        
//        awardList.mnuItmDisplayAward.setEnabled(modifyAward);
//        
//        awardList.btnCreateNewAward.setEnabled(modifyAward);
//        awardList.mnuItmNewEntry.setEnabled(modifyAward);
//        
//        awardList.mnuItmDisplayAward.setEnabled(viewAward);
//        awardList.btnDisplayAward.setEnabled(viewAward);
//        
//        awardList.mnuItmGenReportingReq.setEnabled(maintainReporting);
//        
//        
    }
    
    /** displays S2S Search Window. */
    private void showS2SSearch() {
        try {
            coeusSearch.showSearchWindow();
            tblResultsTable = coeusSearch.getSearchResTable();
            //Case#2908 - Exports from Search Results Do Not Preserve Data Format  - Start
            if(tblResultsTable == null){
                tblResults = coeusSearch.getEmptyResTable();
            }else{
                tblResults = tblResultsTable;
            }
            //Case@908 - End
            if(S2S_COLUMN == -1) {
                Hashtable htSelRow = coeusSearch.getSearchResults();
                
                //Bug Fix if ESC Pressed it'll be null.
                if(htSelRow == null) return ;
                
                Vector vecDisplay = (Vector)htSelRow.get("displaylabels");
                DisplayBean displayBean;
                for(int index = 0; index < vecDisplay.size(); index++){
                    displayBean = (DisplayBean)vecDisplay.get(index);
                    if(displayBean.getName() != null && displayBean.getName().equals("MIT_AWARD_NUMBER")) {
                        S2S_COLUMN = index;
                    }
                }
            }
            
            s2sSubList.displayResults(tblResultsTable);
            
            //adding listener for table.
            if(tblResultsTable != null) {
                s2sSubList.tblResults.addMouseListener(this);
                s2sSubList.tblResults.getTableHeader().addMouseListener(this);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    /** Displays Award List. */
    public void display() {
        try{
            mdiForm.putFrame(CoeusGuiConstants.S2S_SUB_LIST_FRAME_TITLE, s2sSubList);
            mdiForm.getDeskTopPane().add(s2sSubList);
            s2sSubList.setSelected(true);
            s2sSubList.setVisible(true);
            showS2SSearch();
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    public void formatFields() {
    }
    
    /** returns the component being controlled.
     * @return component being controlled.
 */
    public Component getControlledUI() {
        return s2sSubList;
    }
    
    /** registers Components with listeners. */
    public void registerComponents() {
        s2sSubList.addVetoableChangeListener(this);
        
        //Adding Listeners for ToolBar Buttons
        s2sSubList.btnClose.addActionListener(this);
        s2sSubList.btnS2SDetails.addActionListener(this);
        s2sSubList.btnSaveAs.addActionListener(this);
        s2sSubList.btnSearch.addActionListener(this);
        s2sSubList.btnSort.addActionListener(this);
        
        s2sSubList.mnuS2SDetails.addActionListener(this);
        s2sSubList.mnuItmSearch.addActionListener(this);
        s2sSubList.mnuDisplayProposal.addActionListener(this);
    }
    
    public void saveFormData() {
    }
    
    public void setFormData(Object data) {
    }
    
    public boolean validate() throws CoeusUIException {
        return false;
    }
    private static BlockingGlassPane blockingGlassPane;    
    public void blockEvents(boolean block) {
        if(blockingGlassPane == null) {
            blockingGlassPane = new BlockingGlassPane();
            CoeusGuiConstants.getMDIForm().setGlassPane(blockingGlassPane);
        }
        blockingGlassPane.block(block);
    }
    
    /** listens to action performed events.
     * @param actionEvent ActionEvent object.
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
                
        try{
            mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            blockEvents(true);
            if(source.equals(s2sSubList.btnS2SDetails)) {
                showSubmissionDetails();
            }else if(source.equals(s2sSubList.btnSaveAs)) {
                saveS2SSubList();
            }else if(source.equals(s2sSubList.btnSort)) {
                showSort();
            }else if(source.equals(s2sSubList.btnClose)) {
                close();
            }else if(source.equals(s2sSubList.btnSearch)||source.equals(s2sSubList.mnuItmSearch)) {
                showS2SSearch();
            }else  if(source.equals(s2sSubList.mnuS2SDetails)){
                showSubmissionDetails();
            }else if(source.equals(s2sSubList.mnuDisplayProposal)){
                displayProposal();
            }else {
                CoeusOptionPane.showInfoDialog(FUNC_NOT_IMPL);
            }
        }catch(Exception ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }finally {
            blockEvents(false);
            mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
    }
    
    private void displayProposal() throws Exception {
        HashMap searchResultRow = coeusSearch.getSelectedRow();
        HashMap params = new HashMap();
        String proposalId = (String)s2sSubList.tblResults.getValueAt(s2sSubList.tblResults.getSelectedRow(),0);
        
        ProposalDetailForm proposalDetailForm;
        if((proposalDetailForm = (ProposalDetailForm)mdiForm.getFrame(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,proposalId)) != null ){
            if( proposalDetailForm.isIcon() ){
                proposalDetailForm.setIcon(false);
            }
            proposalDetailForm.setSelected( true );
            return;
        }
        
        proposalDetailForm = new ProposalDetailForm(TypeConstants.DISPLAY_MODE, proposalId, CoeusGuiConstants.getMDIForm());
        proposalDetailForm.showDialogForm();

    }
    
    /*
     * this method shows the sort window
     * return void
     */
    private void showSort() {
        if(vecSortedData==null) {
            vecSortedData = new Vector();
        }
        SortForm sortForm = new SortForm(s2sSubList.tblResults,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(s2sSubList.tblResults,vecSortedData);
        else
            return;
    }
    
    private boolean closed = false;
    
    /** closes this window. */
    private void close() {
        mdiForm.removeFrame(CoeusGuiConstants.S2S_SUB_LIST_FRAME_TITLE);
        closed = true;
        s2sSubList.doDefaultCloseAction();
    }
    
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if(closed) return ;
        
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
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
    
    public void mouseClicked(MouseEvent mouseEvent) {
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
                newSortedData.addElement(tblResultsTable.getColumnName(column));
                newSortedData.addElement(new Integer(column));
                newSortedData.addElement(new Boolean(status == 1 ? true : false));
                if(vecSortedData == null)
                    vecSortedData = new Vector();
                vecSortedData.removeAllElements();
                vecSortedData.addElement(newSortedData);
            }else {
                vecSortedData = null;
            }
        }
        if(mouseEvent.getSource() instanceof JTable) {
            if(mouseEvent.getClickCount() != 2) return ;
            try{
                showSubmissionDetails();
            }catch (Exception ex){
                ex.printStackTrace();
                CoeusOptionPane.showErrorDialog(ex.getMessage());
            }
        }
    }
    
    public void mouseEntered(MouseEvent mouseEvent) {
    }
    
    public void mouseExited(MouseEvent mouseEvent) {
    }
    
    public void mousePressed(MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(MouseEvent mouseEvent) {
    }
    
    private void saveS2SSubList() {
        //Case#2908 - Exports from Search Results Do Not Preserve Data Format  - Start
        //SaveAsDialog saveAsDialog = new SaveAsDialog(tblResultsTable);
        SaveAsDialog saveAsDialog = new SaveAsDialog(tblResults);
        //Case#2908 - End
    }
    private void showSubmissionDetails() throws CoeusException{
        HashMap searchResultRow = coeusSearch.getSelectedRow();
        HashMap params = new HashMap();
        String proposalNumber = (String)s2sSubList.tblResults.getValueAt(s2sSubList.tblResults.getSelectedRow(),0);
        String opportunityId = (String)s2sSubList.tblResults.getValueAt(s2sSubList.tblResults.getSelectedRow(),3);
        String cfdaNumber = (String)s2sSubList.tblResults.getValueAt(s2sSubList.tblResults.getSelectedRow(),5);
        //JIRA COEUSDEV-1997 - START
        String sponsorCode = (String)s2sSubList.tblResults.getValueAt(s2sSubList.tblResults.getSelectedRow(),13);
        String sponsorName = (String)s2sSubList.tblResults.getValueAt(s2sSubList.tblResults.getSelectedRow(),14);
        //JIRA COEUSDEV-1997 - END
        params.put("PROPOSAL_NUMBER", proposalNumber);
        S2SHeader headerParam = new S2SHeader();
        headerParam.setSubmissionTitle(proposalNumber);
        headerParam.setOpportunityId(opportunityId);
        headerParam.setCfdaNumber(cfdaNumber);
        headerParam.setAgency(sponsorCode + " : " +sponsorName);
        headerParam.setStreamParams(params);

        ProcessGrantsSubmission grantSubmission = new ProcessGrantsSubmission(headerParam);
        grantSubmission.showS2SSubmissionForm();
    }
    
}
