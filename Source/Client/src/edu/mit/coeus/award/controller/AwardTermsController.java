/*
 * AwardTermsController.java
 *
 * Created on May 3, 2004, 12:21 PM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.AwardConstants;
import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardTermsBean;
import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.award.gui.AwardTermsForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
// JM 5-14-2012 added to fix scrolling issues
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
// END JM
import java.net.URL;
import java.util.HashMap;
import java.util.Observer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;



/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class AwardTermsController extends AwardController implements 
ActionListener, ListSelectionListener,MouseListener, BeanUpdatedListener {

	private CoeusVector cvTermsName;
    private CoeusVector cvAllTermsDetails;
    private CoeusVector cvAwardTermsDetails;
	private CoeusVector cvAddTermDetails;
	
	private AwardTermsForm awardTermsForm;
		
	private static final String CONFIRM_SYNC = "awardTerms_exceptionCode.1301";
	
	private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
	
	private static final int IMAGE_COLUMN = 0;
    private static final int TEXT_COLUMN = 1;
	
	private static final int CODE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
	
	private String termsKey = EMPTY;
	
	private QueryEngine queryEngine;
	private Equals eqTerms;
	private AwardTermsBean awardTermsBean;
	
	private CoeusMessageResources  coeusMessageResources;
	
	private AwardTermsDetailsTableModel awardTermsDetailsTableModel;
	private AwardTermsTableModel awardTermsTableModel;
	private AwardTermsTableCellRenderer awardTermsTableCellRenderer;
	
	private static final Color PANEL_BACKGROUND_COLOR = 
        (Color) UIManager.getDefaults().get("Panel.background");
	
	private int lastSelectedRow;
	private int prevSelectedRow;
	
	private String selectedValue = null;
	
	private static final String SELECT_A_ROW="Please select a row to delete";
//    private static final String DELETE_CONFIRMATION = "budgetPersons_exceptionCode.1305";
	private static final String DELETE_CONFIRMATION = "Are you sure you want to remove this row?";
   
        private static final String firstName = "Applicable";
	private	static final String lastName = "Terms";
	private static final String termNames [] = {AwardConstants.EQUIPMENT_APPROVAL,
							AwardConstants.INVENTION,
							AwardConstants.OTHER_REQUIREMENT,
							AwardConstants.PROPERTY,AwardConstants.PUBLICATION,
							AwardConstants.REFERENCED_DOCUMENTS,
							AwardConstants.RIGHTS_IN_DATA,
							AwardConstants.SUBCONTRACT_APPROVAL,
							AwardConstants.TRAVEL};                                            
        //Bug Fix:Performance Issue (Out of memory) Start 1
        private JScrollPane jscrPn;  
        //Bug Fix:Performance Issue (Out of memory) End 1
        
        //Added for COEUSQA-2434 : Award Sync - Error syncing terms - misleading error message 	- Start
        private static final String CANNOT_SYNC_DELETE_ONLY_CLAUSE = "awardTerms_exceptionCode.1312";
        //COEUSQA-2434 : End
        
	/** 
	 * Creates a new instance of AwardTermsController
	 * @param awardBaseBean AwardBaseBean
	 * @param functionType char
	 */
	public AwardTermsController(AwardBaseBean awardBaseBean,char functionType ) {
            super(awardBaseBean);
            coeusMessageResources = CoeusMessageResources.getInstance();
            queryEngine = QueryEngine.getInstance();
            awardTermsForm = new AwardTermsForm();
            setFunctionType(functionType);
            registerComponents();
            setColumnData();
            //formatFields();
            setFormData(awardBaseBean);
            
        }
	
	/**
	 * Display
	 * @return void
	 **/
	public void display() {
	}
	/**
	 * Format fields
	 * @return void
	 **/
	public void formatFields() {
		// if it is in display mode disabling the buttons.
		if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
                    awardTermsForm.btnAdd.setEnabled(false);
                    awardTermsForm.btnDelete.setEnabled(false);
                    awardTermsForm.btnSync.setEnabled(false);
                    awardTermsForm.btnAddSync.setEnabled(false);//2796
                    awardTermsForm.btnDelSync.setEnabled(false);//2796
                    awardTermsForm.tblTermDetails.setBackground((Color) UIManager.
                            getDefaults().get("Panel.background"));
                }else if(!awardBaseBean.isParent()){
                    awardTermsForm.btnAddSync.setEnabled(false);//2796
                    awardTermsForm.btnDelSync.setEnabled(false);//2796
                }
	}
	
	/**
	 * To get the controlled UI
	 * @return java.awt.Component
	 **/
	public Component getControlledUI() {
            
            //Bug Fix:Performance Issue (Out of memory) Start 2
            //return awardTermsForm;
            //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
            //jscrPn = new JScrollPane(awardTermsForm);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
            return jscrPn;
            //Bug Fix:Performance Issue (Out of memory) End 2
	}
	/**
	 * To get the form data
	 * @return Object
	 **/
	public Object getFormData()	{
		return awardTermsForm;
	}
	/**
	  * Registering the components
	  * @return void
	  **/
	public void registerComponents() {
            //awardTermsForm = new AwardTermsForm();
            awardTermsDetailsTableModel = new AwardTermsDetailsTableModel();
            awardTermsTableModel = new AwardTermsTableModel();
            awardTermsTableCellRenderer = new AwardTermsTableCellRenderer();
            
            awardTermsForm.btnAdd.addActionListener(this);
            awardTermsForm.btnDelete.addActionListener(this);
            awardTermsForm.btnSync.addActionListener(this);
            //2796 : Sync to Parent - Start
            awardTermsForm.btnAddSync.addActionListener(this);
            awardTermsForm.btnDelSync.addActionListener(this);
            //2796 End
            awardTermsForm.tblTermDetails.addMouseListener(this);
            awardTermsForm.tblTermName.getSelectionModel().setSelectionMode(
                                            ListSelectionModel.SINGLE_SELECTION);
            awardTermsForm.tblTermName.getSelectionModel().addListSelectionListener(this);
            awardTermsForm.tblTermName.setModel(awardTermsTableModel);
            awardTermsForm.tblTermDetails.setModel(awardTermsDetailsTableModel);
            addBeanUpdatedListener(this, AwardDetailsBean.class);
            //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
            jscrPn = new JScrollPane(awardTermsForm);
            // JM 4-10-2012 add listener to pass control to outer pane for scrolling
            jscrPn.addMouseWheelListener(new MouseWheelListener() {
                public void mouseWheelMoved(MouseWheelEvent e) {
                	jscrPn.getParent().dispatchEvent(e);
                }
            });
            //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
            //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
            //Disables the Last Update and Update User components
             awardTermsForm.pnlUpdateDetails.setVisible(false);
             //COEUSQA-1456 : End
        }
        
    /** Method to perform some action when the beanUpdated event is triggered
     * here it sets the <CODE>refreshRequired</CODE> flag
     * @param beanEvent takes the beanEvent */
    public void beanUpdated(BeanEvent beanEvent) {
        if( beanEvent.getSource().getClass().equals(OtherHeaderController.class) ){
            if( beanEvent.getBean().getClass().equals(AwardDetailsBean.class)){
                setRefreshRequired(true);
            }
        }
    }

    /**
	 * Method to garbage collect the added listeners
     */
    public void cleanUp() {
		// set all the instance variable to null and it will called during garbage collection
        //Bug Fix:Performance Issue (Out of memory) Start 3
        jscrPn.remove(awardTermsForm);
        jscrPn = null;
        //Bug Fix:Performance Issue (Out of memory) End 3
		cvTermsName = null;
		cvAllTermsDetails = null;
		cvAwardTermsDetails = null;
		cvAddTermDetails = null;
		awardTermsForm = null;
		termsKey = null;
		eqTerms = null;
		awardTermsBean = null;
		awardTermsDetailsTableModel = null;
		awardTermsTableModel = null;
		awardTermsTableCellRenderer = null;
		selectedValue = null;
		
		removeBeanUpdatedListener(this, AwardDetailsBean.class);
		
	}

		/**
          * Setting up the column data
          * @return void
          **/
		private void setColumnData(){
      
			JTableHeader tableHeader = awardTermsForm.tblTermDetails.getTableHeader();
			tableHeader.setReorderingAllowed(false);
			tableHeader.setFont(CoeusFontFactory.getLabelFont());
			
			//for the single selection
			awardTermsForm.tblTermDetails.setSelectionMode(
							DefaultListSelectionModel.SINGLE_SELECTION);
			tableHeader.addMouseListener(new ColumnHeaderListener());
			
        	TableColumn column = awardTermsForm.tblTermName.getColumnModel().getColumn(IMAGE_COLUMN);
			column.setMinWidth(20);
			column.setMaxWidth(20);
			column.setPreferredWidth(20);
			column.setResizable(false);
			column.setCellRenderer(awardTermsTableCellRenderer);
			column.setHeaderRenderer(new EmptyHeaderRenderer());
			awardTermsForm.tblTermName.setRowHeight(20);		
			column = awardTermsForm.tblTermName.getColumnModel().getColumn(TEXT_COLUMN);
			column.setMinWidth(100);
			column.setMaxWidth(250);
			column.setPreferredWidth(180);
			column.setResizable(false);
			column.setCellRenderer(awardTermsTableCellRenderer);
			column.setHeaderRenderer(new EmptyHeaderRenderer());
			
			awardTermsForm.tblTermName.setShowGrid(false);
			awardTermsForm.tblTermName.setShowVerticalLines(false);
			awardTermsForm.tblTermName.setShowHorizontalLines(false);
			awardTermsForm.tblTermName.setOpaque(false);
			awardTermsForm.tblTermName.setBackground(PANEL_BACKGROUND_COLOR);
			
			column = awardTermsForm.tblTermDetails.getColumnModel().getColumn(CODE_COLUMN);
			awardTermsForm.tblTermDetails.setRowHeight(22);
			column.setMinWidth(50);
			column.setPreferredWidth(50);
			column.setResizable(true);
			
			column = awardTermsForm.tblTermDetails.getColumnModel().
													getColumn(DESCRIPTION_COLUMN);
			awardTermsForm.tblTermDetails.setRowHeight(22);
			column.setMinWidth(300);
			column.setPreferredWidth(575);
			column.setResizable(true);
			awardTermsForm.tblTermDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
									
		}
	/**
	 * save form data
	 * @return void
	 */
	public void saveFormData() {
		
	}
	
	/**
	 * To display the fields on sorting order depends on the code
	 */	
	private void sortOnDisplay() {
		if (cvAwardTermsDetails != null && cvAwardTermsDetails.size() > 0) {
			cvAwardTermsDetails.sort("termsCode", true);
		}
    }
	
	/** This class will sort the column values in ascending and descending order
     * based on number of clicks. 
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","termsCode" },
            {"1","termsDescription" }
            
       };
        boolean sort =true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */        
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                 // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
               // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvAwardTermsDetails != null && cvAwardTermsDetails.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvAwardTermsDetails).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    awardTermsDetailsTableModel.fireTableRowsUpdated(
                                            0, awardTermsDetailsTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
	
	/**
	 * To set the form data
	 * @param awardBaseBean Object
	 * @return void
	 **/
	public void setFormData(Object baseBean) {
            //Bug Fix:1410 Start
            //changed the argument of the setFormData from 
            //awardBaseBean to base bean.
            
            if(baseBean != null){
                this.awardBaseBean = (AwardBaseBean)baseBean;
                prepareQueryKey(awardBaseBean);
            }
            //Bug Fix:1410 End
            
		cvTermsName = new CoeusVector();
		cvAllTermsDetails = new CoeusVector();
        cvAwardTermsDetails = new CoeusVector();
	    cvAddTermDetails = new CoeusVector();
		// on load set the first row as selected.
		awardTermsForm.tblTermName.setRowSelectionInterval(0,0);
				
	}
	/**
	 * validate method
	 * @return boolean
	 * @throws edu.mit.coeus.exception.CoeusUIException
	 **/
        //Modified with case 2796: Sync To Parent
	public boolean validate() throws CoeusUIException {
            /*
             * 1. Equipment Approval    2. Invention        3. other
             * 4. Property              5. Publication      6. Referenced Documents
             * 7. Rights in Data         8. Subcontract     9. Travel
             */
            String termValue,termKey;
            try {
                for(int index = 0; index < termNames.length; index++) {
                    termValue = termNames[index];
                    termKey   = (String) AwardConstants.awardTerms.get(termValue);
                    //checking for mit award number != null and actype != null & acType != D
                    NotEquals notEqAwardNumber = new NotEquals("mitAwardNumber",null);
                    NotEquals notACType = new NotEquals("acType",TypeConstants.DELETE_RECORD);
                    Equals acType = new Equals("acType",null);
                    Or checkAcType = new Or(notACType,acType);
                    And newAnd = new And(notEqAwardNumber,checkAcType);
                    CoeusVector cvValidateTerms = queryEngine.executeQuery(queryKey,termKey, newAnd);
                    if (cvValidateTerms == null || cvValidateTerms.size() <= 0) {
                       
                        int errorIndex =  index;
                        // Modified for COEUSQA-3718 : Unknown error when saving award with one report - Start
//                        if (errorIndex > 9) {
//                            CoeusOptionPane.showInfoDialog(coeusMessageResources.
//                                    parseMessageKey("awardTerms_exceptionCode.13"+errorIndex+""));
//                        } else {
//                            CoeusOptionPane.showInfoDialog(coeusMessageResources.
//                                    parseMessageKey("awardTerms_exceptionCode.130"+errorIndex+""));
//                        }
                        if (errorIndex > 9) {
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.
                                    parseMessageKey("awardTerms_exceptionCode.14"+errorIndex+""));
                        } else {
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.
                                    parseMessageKey("awardTerms_exceptionCode.140"+errorIndex+""));
                        }
                        // Modified for COEUSQA-3718 : Unknown error when saving award with one report - End                        
                        awardTermsForm.tblTermName.setRowSelectionInterval(index,index);
                        return false;
                    }
                }
            } catch (CoeusException exception) {
                exception.printStackTrace();
            }
            return true;
        }
	//2796 End
	/** This method will refresh the form with the modified data
	 * @return void
	 */
	 
    public void refresh(){
        if (!isRefreshRequired())
			return ;
        setFormData(null);
        setSaveRequired(true);
        awardTermsForm.tblTermName.clearSelection();
        if( awardTermsForm.tblTermName.getRowCount() > 0 ){
            awardTermsForm.tblTermName.setRowSelectionInterval(0, 0);
        }
        setRefreshRequired(false);
    }
	
	/** This method will specify the action performed
	 * @param actionEvent ActionEvent
	 * @return void
	 */
	public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            //Modified with case 2796: Sync to parent
            if (source.equals(awardTermsForm.btnAdd)) {
                performAddOperation(false);
            } else if (source.equals(awardTermsForm.btnDelete)) {
                performDeleteOperation(false);
            } else if (source.equals(awardTermsForm.btnSync)) {
                performSyncOperation();
            } else if (source.equals(awardTermsForm.btnAddSync)) {
                performAddAndSync();
            }else if (source.equals(awardTermsForm.btnDelSync)) {
                performDeleteAndSync();
            }
            //2796 End
        }
        
        //Methods added with case 2796:Sync to Parent 
        private void performAddAndSync(){
            //COEUSDEV 253:Add Fabe and CS to sync Screen
            //Modified for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved
//            HashMap target = showSyncTargetWindow(true,AwardConstants.ADD_SYNC);
            HashMap target = showSyncTargetWindow(true,AwardConstants.TERMS_SYNC,AwardConstants.ADD_SYNC);
            //COEUSDEV-416 : End 
            if(target!=null){
                performAddOperation(true);
                int selRow = awardTermsForm.tblTermName.getSelectedRow();
                selectedValue = termNames[selRow];
                termsKey = (String) AwardConstants.awardTerms.get(selectedValue);
                if(setSyncFlags(termsKey,true,target)){
                    saveAndSyncAward(termsKey);
                }
            }
        }
        
        private void performDeleteAndSync(){
            
            int selRow = awardTermsForm.tblTermName.getSelectedRow();
            selectedValue = termNames[selRow];
            termsKey = (String) AwardConstants.awardTerms.get(selectedValue);
            if(validateBeforeDelete(selRow)){
                //COEUSDEV 253:Add Fabe and CS to sync Screen
                //Modified for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved
//                HashMap target = showSyncTargetWindow(true,AwardConstants.DELETE_SYNC);
                HashMap target = showSyncTargetWindow(true,AwardConstants.TERMS_SYNC,AwardConstants.DELETE_SYNC);
                //COEUSDEV-416 : End
                if(target!=null){
                    performDeleteOperation(true);
                    
                    if(setSyncFlags(termsKey,true,target)){
                        saveAndSyncAward(termsKey);
                    }
                }
            }
        }
        
        private boolean validateBeforeDelete(int selRow){
            
            int rowIndex = awardTermsForm.tblTermDetails.getSelectedRow();
            if ( rowIndex == -1) {
                CoeusOptionPane.showErrorDialog(SELECT_A_ROW);
                return false;
            }
                String termValue,termKey;
            try {
                for(int index = 0; index < termNames.length; index++) {
                    termValue = termNames[index];
                    termKey   = (String) AwardConstants.awardTerms.get(termValue);
                    //checking for mit award number != null and actype != null & acType != D
                    NotEquals notEqAwardNumber = new NotEquals("mitAwardNumber",null);
                    And newAnd = new And(notEqAwardNumber,CoeusVector.FILTER_ACTIVE_BEANS);
                    CoeusVector cvValidateTerms = queryEngine.executeQuery(queryKey,termKey,newAnd);
                    if ((cvValidateTerms.size() == 0)
                    || (index == selRow && cvValidateTerms.size() == 1)){
                        //Modified for COEUSQA-2434 : Award Sync - Error syncing terms - misleading error message - Start
//                        int errorIndex =  index + 3;
//                        if (errorIndex > 9) {
//                            CoeusOptionPane.showInfoDialog(coeusMessageResources.
//                                    parseMessageKey("awardTerms_exceptionCode.13"+errorIndex+""));
//                        } else {
//                            CoeusOptionPane.showInfoDialog(coeusMessageResources.
//                                    parseMessageKey("awardTerms_exceptionCode.130"+errorIndex+""));
//                        }
                           CoeusOptionPane.showInfoDialog(coeusMessageResources.
                                    parseMessageKey(CANNOT_SYNC_DELETE_ONLY_CLAUSE));
                           //COEUSQA-2434 : end
                        awardTermsForm.tblTermName.setRowSelectionInterval(index,index);
                        return false;
                    }
                }
            } catch (CoeusException exception) {
                exception.printStackTrace();
            }
            return true;
        }
        
	/** This method will specify the action performed during the selection change
	 * @param listSelectionEvent ListSelectionEvent
	 * @return void
	 */
	public void valueChanged(ListSelectionEvent listSelectionEvent) {
	/*	
		if (prevSelectedRow != -1) {
			if (cvAwardTermsDetails != null) {
				selectedValue = termNames[prevSelectedRow];
				termsKey = (String) AwardConstants.awardTerms.get(selectedValue);
				for (int index = 0; index < cvAwardTermsDetails.size(); index++) {
					AwardTermsBean awardTermsBean = (AwardTermsBean)cvAwardTermsDetails.get(index);
					if (awardTermsBean.getAcType() != null) {
						try {
							if(awardTermsBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
								queryEngine.insert(queryKey,termsKey,awardTermsBean); 
							} else if(awardTermsBean.getAcType().equals(TypeConstants.DELETE_RECORD)) {
								queryEngine.delete(queryKey,termsKey,awardTermsBean);
							}
						} catch (CoeusException exception) {
							exception.printStackTrace();
						}
					}
				}
			}
			
		}
		*/
		int selRow = awardTermsForm.tblTermName.getSelectedRow();
		if( selRow == -1 ) return ;
        selectedValue = termNames[selRow];
		String labelName = firstName  +" "+ selectedValue +" "+ lastName;
		awardTermsForm.lblApplicable.setText(labelName);
		termsKey = (String) AwardConstants.awardTerms.get(selectedValue);
		try {
			Equals eqAwardNo = new Equals("mitAwardNumber", 
											awardBaseBean.getMitAwardNumber());
			//NotEquals notEqDelete = new NotEquals("acType", TypeConstants.DELETE_RECORD);
			And awardNoAndneDelete = new And(eqAwardNo, CoeusVector.FILTER_ACTIVE_BEANS);
			
			cvAllTermsDetails = queryEngine.getDetails(queryKey, termsKey);
			
			cvAwardTermsDetails = (CoeusVector) ObjectCloner.deepCopy(
				cvAllTermsDetails.filter(awardNoAndneDelete));
			
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		//prevSelectedRow = selRow;
		// while displaying sort it based on the code
		sortOnDisplay();
		awardTermsDetailsTableModel.setData(cvAwardTermsDetails);
		awardTermsDetailsTableModel.fireTableDataChanged();
		
	}
	/**
	 * This method will specify the actions during Add operation
         * Added parameter SyncRequired with case 2796:Sync To Parent
	 * @return void
	 */
	private void performAddOperation(boolean syncRequired) {
		//Filtering for the values to be displayed in the Add screen
		Equals eqAwardNumber = new Equals("mitAwardNumber",null);
		Equals eqACType = new Equals("acType",TypeConstants.DELETE_RECORD);
		Or awardNumberOrAcType = new Or(eqAwardNumber,eqACType);
		int selRow = awardTermsForm.tblTermName.getSelectedRow();
		selectedValue = termNames[selRow];
		String termsKey = (String) AwardConstants.awardTerms.get(selectedValue);
		try {
			cvAllTermsDetails = queryEngine.getDetails(queryKey, termsKey);
		} catch (CoeusException exception) {
			exception.printStackTrace();
		}
		cvAddTermDetails = cvAllTermsDetails.filter(awardNumberOrAcType);
		try {
			awardTermsForm.setCursor( new Cursor(Cursor.WAIT_CURSOR));
			AwardAddTermsController awardAddTermsController = new
				AwardAddTermsController(mdiForm, queryKey, selectedValue);
			awardAddTermsController.setFormData(cvAddTermDetails);

			if (cvAwardTermsDetails != null && awardAddTermsController != null) {
				//getting the selected values
				CoeusVector cvVal = awardAddTermsController.display();
				if (cvVal != null && cvVal.size() > 0) {
					cvAwardTermsDetails.addAll(cvVal);
					awardTermsDetailsTableModel.setData(cvAwardTermsDetails);
					awardTermsDetailsTableModel.fireTableDataChanged();

					for (int index = 0; index < cvVal.size(); index++) {
						AwardTermsBean awardTermsBean = (AwardTermsBean)cvVal.get(index);
                                                //Added with Case 2796: Sync to parent
                                                if(awardTermsBean.getAcType()!=null){
                                                    awardTermsBean.setSyncRequired(syncRequired);
                                                }
                                                //2796 End
						//queryEngine.setData(queryKey,termsKey,awardTermsBean);
						Equals eqTermsCode = new Equals("termsCode", new Integer(awardTermsBean.getTermsCode()));
						queryEngine.removeData(queryKey,termsKey,eqTermsCode);
						queryEngine.addData(queryKey,termsKey, awardTermsBean);
					}
					int selected = awardTermsForm.tblTermName.getSelectedRow();
					awardTermsTableModel.fireTableRowsUpdated(selected,selected);

				}
			}
		}
		finally{
				awardTermsForm.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ));
			}
			
	}
	/**
	 * This method will specify the actions during Delete operation
	 * @return void
	 */
	private void performDeleteOperation(boolean syncRequired) {
            int rowIndex = awardTermsForm.tblTermDetails.getSelectedRow();
            if (rowIndex == -1) {
                CoeusOptionPane.showErrorDialog(SELECT_A_ROW);
                return;
            }else if (rowIndex >= 0) {
                String mesg = DELETE_CONFIRMATION;
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(mesg),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    
                    
                    AwardTermsBean awardTermsBean = (AwardTermsBean)cvAwardTermsDetails.get(rowIndex);
                    
                    if (awardTermsBean.getAcType() == null) {
                        awardTermsBean.setAcType(TypeConstants.DELETE_RECORD);
                        awardTermsBean.setSyncRequired(syncRequired);//2796
                    } else if (awardTermsBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
                        awardTermsBean.setAcType(null);
                        awardTermsBean.setMitAwardNumber(null);
                        awardTermsBean.setSequenceNumber(-1);
                    }
                    //queryEngine.setData(queryKey,termsKey,awardTermsBean);
                    Equals eqTermsCode = new Equals("termsCode", new Integer(awardTermsBean.getTermsCode()));
                    queryEngine.removeData(queryKey,termsKey,eqTermsCode);
                    queryEngine.addData(queryKey,termsKey, awardTermsBean);
                    cvAwardTermsDetails.removeElementAt(rowIndex);
                    
                    awardTermsDetailsTableModel.fireTableRowsDeleted(rowIndex,rowIndex);
                    int selected = awardTermsForm.tblTermName.getSelectedRow();
                    awardTermsTableModel.fireTableRowsUpdated(selected,selected);
                }
            }
        }
	/**
	 * This method will specify the actions during Sync operation
	 * @return void
	 */
	private void performSyncOperation(){
        int option = CoeusOptionPane.showQuestionDialog(
			 coeusMessageResources.parseMessageKey(CONFIRM_SYNC), 
				CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
        
        switch( option ){
            case (JOptionPane.YES_OPTION ):
                //Call the Sync Terms of the AwardController
                if ( syncTerms(EMPTY, getTemplateCode()) ){
                    setFormData(null);
                    setSaveRequired(true);
                    awardTermsForm.tblTermName.clearSelection();
                    if( awardTermsForm.tblTermName.getRowCount() > 0 ){
                        awardTermsForm.tblTermName.setRowSelectionInterval(0, 0);
                    }
                }
                break;
            case (JOptionPane.NO_OPTION ):
                break;
            default:
                break;
        }
    }
	/**
	 * Actions on click of mouse
	 * @param mouseEvent java.awt.event.MouseEvent
	 * @return void
	 **/
	public void mouseClicked(MouseEvent mouseEvent) {
            if (mouseEvent.getClickCount() == 2 && getFunctionType() != TypeConstants.DISPLAY_MODE) {
                performAddOperation(false);//2796
            }
        }
	public void mouseEntered(MouseEvent e)
		{
		}
		
	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}
	
  /**
   * This is an inner class represents the table model for the Terms
   * screen table
   **/
   public class AwardTermsDetailsTableModel extends AbstractTableModel {
           
        // represents the column names of the table
        private String colName[] = {"Code","Description"};
        // represents the column class of the fields of table        
        private Class colClass[] = {String.class, String.class};   
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col) {
            return colClass[col];
        }
		
		/**
         * To check whether the table cell is editable or not
         * @param row int
		 * @param col int
         * @return boolean
         **/
		 public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * To get the column count of the table
         * @return int
         **/
        public int getColumnCount() {
             return colName.length;
        }
        
        /**
         * To get the row count of the table
         * @return int
         **/
        public int getRowCount() {
            if (cvAwardTermsDetails == null){
                return 0;
            } else {
                return cvAwardTermsDetails.size();
            }
        }
        
        /**
         * To set the data for the model.
         * @param cvAwardTermsDetails CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvAwardTermsDetails) {
            cvAwardTermsDetails = cvAwardTermsDetails;
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            //have to change to the value from bean
			AwardTermsBean awardTermsBean = (AwardTermsBean)cvAwardTermsDetails.get(rowIndex);
			if (awardTermsBean != null) {
				switch(columnIndex) {
					case CODE_COLUMN:
						return ""+awardTermsBean.getTermsCode();
					case DESCRIPTION_COLUMN:
						return awardTermsBean.getTermsDescription();
				}
			}
			return EMPTY;
		}
		
        /**
         * To set the value in the table 
         * @param value Object
         * @param row int
         * @param col int
         * @return void
         **/
        public void setValueAt(Object value, int row, int col) {
            //have to set value in bean
        }
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
     }
	
	/**
   * This is an inner class represents the table model for the Terms
   * screen table
   **/
   public class AwardTermsTableModel extends AbstractTableModel {
           
        // represents the column names of the table
        private String colName[] = {EMPTY,EMPTY};
        // represents the column class of the fields of table        
        private Class colClass[] = {ImageIcon.class, String.class};   
		
		/**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col) {
            return colClass[col];
        }
		
		/**
         * To check whether the table cell is editable or not
         * @param row int
		 * @param col int
         * @return boolean
         **/
		 public boolean isCellEditable(int row, int col){
            return false;
        }
		 
        /**
         * To get the column count of the table
         * @return int
         **/
        public int getColumnCount() {
             return colName.length;
        }
        
        /**
         * To get the row count of the table
         * @return int
         **/
        public int getRowCount() {
          
			return termNames.length;
        }
        
        /**
         * To set the data for the model.
         * @param cvBudgetTableData CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvTermsName) {
            cvTermsName = cvTermsName;
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            		
            switch(columnIndex) {
                case IMAGE_COLUMN:
                    return EMPTY;
                case TEXT_COLUMN:
				    return termNames[rowIndex];
            }
            return EMPTY;
        }
            
        /**
         * To set the value in the table 
         * @param value Object
         * @param row int
         * @param col int
         * @return void
         **/
        public void setValueAt(Object value, int row, int col) {
            //have to set value in bean
			
		}
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
     }
   
   /**
    * This is an inner class represents the table cell renderer for the Award For Budget
    * screen table
    **/
    public class AwardTermsTableCellRenderer extends DefaultTableCellRenderer {
        		
		URL emptyPageUrl = getClass().getClassLoader().getResource(CoeusGuiConstants.NEW_ICON);
        URL fillPageUrl = getClass().getClassLoader().getResource(CoeusGuiConstants.DATA_ICON);
        private JLabel lblIcon;
        ImageIcon EMPTY_PAGE_ICON, FILL_PAGE_ICON;
            
        /**
         * Default Constructor
         **/
        public AwardTermsTableCellRenderer() {
			
            lblIcon = new JLabel();            
        }
        /**
         * To get the table cell editor component
         * @param table javax.swing.JTable
         * @param value Object
         * @param isSelected boolean
         * @param row int
         * @param column int
         * @return java.awt.Component
         **/
       public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            switch(column){
                case IMAGE_COLUMN:
                    if(emptyPageUrl != null) {
                        EMPTY_PAGE_ICON = new ImageIcon(emptyPageUrl);
                        lblIcon.setIcon(EMPTY_PAGE_ICON);
                    }
                    
                    if(fillPageUrl != null) {
                        FILL_PAGE_ICON = new ImageIcon(fillPageUrl);
                        lblIcon.setIcon(FILL_PAGE_ICON);
                    }
					
					selectedValue = termNames[row];
					
					String termsKey = (String) AwardConstants.awardTerms.get(selectedValue);
					CoeusVector cvFilteredAwardTerms = new CoeusVector();
					CoeusVector cvAllTermsDetails = new CoeusVector();
					try {
						Equals eqAwardNo = new Equals("mitAwardNumber", 
														awardBaseBean.getMitAwardNumber());
						And awardNoAndneDelete = new And(eqAwardNo, CoeusVector.FILTER_ACTIVE_BEANS);
						cvAllTermsDetails = queryEngine.getDetails(queryKey, termsKey);
						cvFilteredAwardTerms = (CoeusVector) ObjectCloner.deepCopy(
							cvAllTermsDetails.filter(awardNoAndneDelete));

					} catch (CoeusException coeusException) {
						coeusException.printStackTrace();
					} catch (Exception exception) {
						exception.printStackTrace();
					}
                    if(cvFilteredAwardTerms != null && cvFilteredAwardTerms.size() > 0) {
                        lblIcon.setIcon(FILL_PAGE_ICON);
                   } else {
                        lblIcon.setIcon(EMPTY_PAGE_ICON);
                    }
                    return lblIcon;
					
                case TEXT_COLUMN:
                    
            }
            return super.getTableCellRendererComponent(table, value,
										isSelected, hasFocus, row, column);
        }
    }// End of AwardTermsTableCellRenderer class..............
	/**
     * Inner class which is used to provide empty header for the Icon Column.
     */
    
    class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        /**
         * Default constructor to set the default foreground/background
         * and border properties of this renderer for a cell.
         */
        EmptyHeaderRenderer() {
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int column){
            return this;
        }
    }
}
