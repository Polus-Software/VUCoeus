/*
 * AwardAddTermsController.java
 *
 * Created on April 21, 2004, 11:23 AM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.award.bean.AwardTermsBean;
import edu.mit.coeus.award.gui.AwardAddTermsForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;

import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ajaygm
 */
public class AwardAddTermsController implements ActionListener  {
    
    /** Holds an instance of <CODE>AwardAddTermsForm</CODE> */
    private AwardAddTermsForm awardAddTermsForm;
    
    /** Holds an instance of <CODE>AwardDetailsBean</CODE> */
    private AwardDetailsBean awardDetailsBean;
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgAwardAddTermsController;
    private QueryEngine queryEngine;
    private CoeusMessageResources coeusMessageResources;
    private AwardAddTermsTableModel awardAddTermsTableModel;
    
	//setting up the width and height of the screen
    private static final int WIDTH = 750;
    private static final int HEIGHT = 550;
	
	//For the code and description column
	private static final int CODE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
	
	// To set the correct title
	private static final String oldTitle = "Other Approval/Notification Requirement";
	private static final String newTitle = "Prior Approval";
    
	//For passing the key related with the selected term
	private String queryKey = "";
	private static final String EMPTY_STRING = "";
	
	//For the table data and for the selected data
	private CoeusVector cvTableData;
	private CoeusVector cvSelectedData;
	//Award term bean
	private AwardTermsBean awardTermsBean;
	
	/**
	 * Constructor
	 * @param mdiForm CoeusAppletMDIForm
	 * @param queryKey String
	 * @param title String
	 **/
    public AwardAddTermsController(CoeusAppletMDIForm mdiForm, String queryKey,
																String title) {
		this.queryKey = queryKey;
        this.mdiForm = mdiForm;
		queryEngine = QueryEngine.getInstance ();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
		postInitComponents(title);
		setColumnData();
		
    }
    /**
	 * To set the components before opening the screen
	 * @param title String
	 * @return void
	 */
     public void postInitComponents(String title){
        dlgAwardAddTermsController = new CoeusDlgWindow(mdiForm);
        dlgAwardAddTermsController.setResizable(false);
        dlgAwardAddTermsController.setModal(true);
        dlgAwardAddTermsController.getContentPane().add(awardAddTermsForm);
		//code for setting the title
		if (title.equals(oldTitle)) {
			title = newTitle;
		}
		// setting up the title and it is getting up from the basewindow
        dlgAwardAddTermsController.setTitle(title);
        dlgAwardAddTermsController.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardAddTermsController.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardAddTermsController.getSize();
        dlgAwardAddTermsController.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
         dlgAwardAddTermsController.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
    }
    /**
	 * To display the dialog window
	 * @return CoeusVector
	 **/   
    public CoeusVector display() {
        dlgAwardAddTermsController.setVisible(true);
		return cvSelectedData;
    }
    /**
	 * To format the fields
	 * @return void
	 */
    public void formatFields() {
    }
    
	/**
	 * To get the controlled form
	 * @return Component
	 */
    public Component getControlledUI() {
         return awardAddTermsForm;
    }
    /**
	 * To get the form data
	 * @return Object
	 */
    public Object getFormData() {
        return awardAddTermsForm;
    }
    /**
	 * registering the components
	 * @return void
	 */
    public void registerComponents() {
        //Add listeners to all the buttons
        awardAddTermsForm = new AwardAddTermsForm();
        awardAddTermsForm.btnOK.addActionListener(this);
        awardAddTermsForm.btnCancel.addActionListener(this);
                
        /** Code for focus traversal - start */
        Component[] components = { awardAddTermsForm.btnOK,
        awardAddTermsForm.btnCancel, awardAddTermsForm.tblDescription
        };
        
        ScreenFocusTraversalPolicy traversePolicy = 
							 new ScreenFocusTraversalPolicy( components );
        awardAddTermsForm.setFocusTraversalPolicy(traversePolicy);
        awardAddTermsForm.setFocusCycleRoot(true);
         
        /** Code for focus traversal - end */
      
        awardAddTermsTableModel = new AwardAddTermsTableModel();
        awardAddTermsForm.tblDescription.setModel(awardAddTermsTableModel);

    }
    /**
	 * setting up the default focus 
	 * @return void
	 */
    public void requestDefaultFocus(){    
       awardAddTermsForm.btnCancel.requestFocus();
    }
	/**
	 * registering the components
	 * @return void
	 */
    public void saveFormData() {
    }
    
	/**
	  * Setting up the column data
	  * @return void
	  **/
	private void setColumnData(){

		JTableHeader tableHeader = awardAddTermsForm.tblDescription.getTableHeader();

		tableHeader.setReorderingAllowed(false);
		tableHeader.setFont(CoeusFontFactory.getLabelFont());
		tableHeader.addMouseListener(new ColumnHeaderListener());
		// setting up the table columns
		TableColumn column = awardAddTermsForm.tblDescription.
									getColumnModel().getColumn(CODE_COLUMN);

		column.setMinWidth(50);
		column.setPreferredWidth(50);
		awardAddTermsForm.tblDescription.setRowHeight(22);
		
		column = awardAddTermsForm.tblDescription.
									getColumnModel().getColumn(DESCRIPTION_COLUMN);
		column.setMinWidth(400);
		column.setPreferredWidth(588);
	}
		
	/**
	 * To set the form data
	 * @param termsData Object
	 * @return void
	 **/
    public void setFormData(Object termsData) {
		
		// get the table data from the vector depends upon the term.
		cvTableData = (CoeusVector)termsData;
                
        CoeusVector cvAwardDetails = new CoeusVector();  
		
        try{
            cvAwardDetails = queryEngine.executeQuery (
            queryKey,AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        } catch (CoeusException coeusException){
            coeusException.printStackTrace ();
        }
		// setting up the data and update the table model.
        awardAddTermsTableModel.setData(cvTableData);
		awardAddTermsTableModel.fireTableDataChanged();
		//getting the award number and sequence number and set the labels.
        awardDetailsBean = (AwardDetailsBean)cvAwardDetails.get(0);
        awardAddTermsForm.lblSponsorAwardNumberValue.setText(
        awardDetailsBean.getSponsorAwardNumber());
        awardAddTermsForm.lblAwardNumberValue.setText(
        awardDetailsBean.getMitAwardNumber());
        awardAddTermsForm.lblSequenceNumberValue.setText(""+
        (awardDetailsBean.getSequenceNumber()));
    }
	
	/**
	 * validate method
	 * @return boolean
	 * @throws edu.mit.coeus.exception.CoeusUIException
	 **/
    public boolean validate() throws CoeusUIException {
		return true;
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
                if(cvTableData != null && cvTableData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvTableData).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    awardAddTermsTableModel.fireTableRowsUpdated(
                                            0, awardAddTermsTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
	
    /**
	 * action performed method
	 */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource ();
        
        if( source.equals(awardAddTermsForm.btnCancel)) {
           dlgAwardAddTermsController.dispose();
        }else if( source.equals(awardAddTermsForm.btnOK) ){
            performUpdateOperation();
        }
    }
	/**
	 * performing the update operation
	 * @return void
	 */
    private void performUpdateOperation(){
		int selectedRows[] = awardAddTermsForm.tblDescription.getSelectedRows();
        if (selectedRows.length == 0) {
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("protoBaseWin_exceptionCode.1051"));
        } else {
			// for storing the selected rows.
			cvSelectedData = new CoeusVector();
			//iterate through each row and set the AC type,Mit Award number and
			// sequence number
			for (int i = 0; i < selectedRows.length; i++) {
				awardTermsBean = (AwardTermsBean) cvTableData.get(selectedRows[i]);
				if (awardTermsBean.getAcType() != null && 
					awardTermsBean.getAcType().equals(TypeConstants.DELETE_RECORD)) {
					awardTermsBean.setAcType(null);
				} else {
					awardTermsBean.setAcType(TypeConstants.INSERT_RECORD);
					awardTermsBean.setMitAwardNumber(awardDetailsBean.getMitAwardNumber());
					awardTermsBean.setSequenceNumber(awardDetailsBean.getSequenceNumber());
				}
				cvSelectedData.add(awardTermsBean);
				
			}
			dlgAwardAddTermsController.dispose();			
        }
    }
	/* 
	 *It's an inner class which specifies the table model 
	 */
    public class AwardAddTermsTableModel extends AbstractTableModel {
         String columnName[] = {"Code","Description"};
         
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
         * To get the column count
         * @return int
         **/
         public int getColumnCount() {
             return columnName.length;
         }
		 /**
          * To get the column count
		  * @param col int
          * @return String
          **/
		  public String getColumnName(int col) {
             return columnName[col];           
         }
         /**
          * To get the row count
		  * @return int
          **/
         public int getRowCount() {
             if (cvTableData == null){
                 return 0;
             } else {
                 return cvTableData.size();
			 }
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
         * To set the  data in the table 
         * @param cvTableData CoeusVector
         * @return void
         **/
         public void setData(CoeusVector cvTableData) {
			 cvTableData = cvTableData;
            
		 }
            
         /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            //getting the bean from the vector and setting up to each column.
			AwardTermsBean awardTermsBean = (AwardTermsBean)cvTableData.elementAt(rowIndex);
			  switch(columnIndex) {
                case CODE_COLUMN:
                    return new Integer(awardTermsBean.getTermsCode());
                case DESCRIPTION_COLUMN:
				    return awardTermsBean.getTermsDescription();
            }
            return EMPTY_STRING;
            
        }
    }
	/**
	 * To initialize all the instance variable to null
	 */
	public void cleanUp() {
		awardAddTermsForm = null;
		awardDetailsBean = null;
		dlgAwardAddTermsController = null;
		awardAddTermsTableModel = null;
		cvTableData = null;
		cvSelectedData = null;
		awardTermsBean = null;
	}
}
