
/*
 * ValidRatesController.java
 *
 * Created on April 21, 2004, 2:39 PM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.gui.ValidRatesForm;
import edu.mit.coeus.bean.ValidRatesBean;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;


/**
 * Copyright (c) Massachusetts Institute of Technology
 * * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * * All rights reserved.
 * @author arunmp
 */
public class ValidIDCRatesPopUpController extends AwardController implements
ActionListener , MouseListener{
    private ValidRatesForm validRatesForm;
    private CoeusDlgWindow dlgValidRates;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private char functionType;
    private static final int WIDTH = 430;
    private static final int HEIGHT = 170;
    private static final String WINDOW_TITLE  =  "Valid IDC Rates";
    private CoeusVector cvValidRatesData;
    private ValidRatesBean validRatesBean;
    private QueryEngine queryEngine;
    private ValidRatesTableModel validRatesTableModel;
    private static final int ON_CAMPUS = 0;
    private static final int OFF_CAMPUS = 1;
    private static final int ADJUSTMENT_KEY = 2;
    private static final String EMPTY_STRING = "";
    
    //variables for sorting purpose. will be true if last sort was Ascending
    //else will be false.
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    
    /**
	 * Creates a new instance of ValidRatesController
	 * @param awardBaseBean AwardBaseBean
	 */
    public ValidIDCRatesPopUpController(AwardBaseBean awardBaseBean) {
        super(awardBaseBean);
        this.mdiForm = mdiForm;
        queryEngine = QueryEngine.getInstance();
        validRatesTableModel = new ValidRatesTableModel();
        postInitComponents();
        setFormData(null);
        registerComponents();
        setTableEditors();
    }
    
	/**
	 * registering the components before initializing
	 */	
    private void postInitComponents() {
        validRatesForm = new ValidRatesForm();
        dlgValidRates = new CoeusDlgWindow(mdiForm);
        dlgValidRates.setResizable(false);
        dlgValidRates.setModal(true);
        dlgValidRates.getContentPane().add(validRatesForm);
        dlgValidRates.setFont(CoeusFontFactory.getLabelFont());
        dlgValidRates.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgValidRates.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgValidRates.getSize();
        dlgValidRates.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgValidRates.addWindowListener(new WindowAdapter(){
            //             public void windowOpened(WindowEvent we) {
            //             }
            
            public void windowClosing(WindowEvent we){
                
                dlgValidRates.dispose();
                
            }
        });
    }
    
	/**
	 * Displaying the dialog window
	 */	
    public void display() {
        dlgValidRates.setVisible(true);
    }
    
	/**
	 * formatting the fieds and decide the view of form in different modes
	 */	
    public void formatFields() {
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            validRatesForm.btnClose.setEnabled(true);
            validRatesForm.tblValidRates.setEnabled(false);
        }
    }
    
	/**
	 * This method will get the controlled UI(form)
	 * @return java.awt.Component
	 */	
    public Component getControlledUI() {
        return validRatesForm;
    }
    
	/**
	 * Getting the form data
	 * @return Object
	 */	
    public Object getFormData() {
        return null;
    }
    
	/**
	 * Registering all the GUI components
	 */	
    public void registerComponents() {
        Component[] components = { validRatesForm.btnClose,validRatesForm.tblValidRates};
        validRatesForm.btnClose.addActionListener(this);
        validRatesTableModel = new ValidRatesTableModel();
        validRatesForm.tblValidRates.setModel(validRatesTableModel);
        validRatesForm.tblValidRates.getTableHeader().addMouseListener(this);
        
        
        dlgValidRates.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
        
    }
    
	/**
	 * Setting up the default focus on load
	 */	
    private void requestDefaultFocus(){
        validRatesForm.btnClose.requestFocusInWindow();
    }
    
    
	/**
	 * Saving the form data
	 */	
    public void saveFormData() {
    }
    
	/**
	 * setting the vector value to the table
	 * @param data Object
	 */	
    public void setFormData(Object data) {
        validRatesForm.tblValidRates.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        validRatesForm.tblValidRates.getTableHeader().setReorderingAllowed(false);
        RowData();
        //((DefaultTableModel)validRatesForm.tblValidRates.getModel()).setDataVector(RowData());
        //getValidRatesColumnNames());
        dlgValidRates.setTitle(WINDOW_TITLE);
        
    }
	/**
	 * Getting the valid rates column names
	 * @return Vector
	 */	
    private Vector getValidRatesColumnNames(){
        
        Vector ValidRatesHeaders = new Vector();
        ValidRatesHeaders.add( "On Campus Rate");
        ValidRatesHeaders.add( "Off Campus Rate" );
        ValidRatesHeaders.add( "Adjustment Key" );
        
        return ValidRatesHeaders ;
    }
    
    
	/**
	 * Getting the values from the vector
	 * @return Vector
	 */	
    private Vector RowData() {
        Vector vecRowData;
        Equals rateClass;
        CoeusVector cvData = new CoeusVector();
        try{
            cvValidRatesData = queryEngine.getDetails(
            queryKey, ValidRatesBean.class);
            cvValidRatesData.sort("onCampusRate");
            //ValidRatesBean validRatesBean = (ValidRatesBean)cvValidRatesData.get()
            rateClass = new Equals("rateClassType",new Character('O'));
            cvValidRatesData = cvValidRatesData.filter(rateClass );
            
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
        if( cvValidRatesData != null ){
            for( int index = 0; index < cvValidRatesData.size(); index++ ){
                validRatesBean = (ValidRatesBean)cvValidRatesData.get(index);
                vecRowData = new Vector();
                vecRowData.add(EMPTY + validRatesBean.getOnCampusRate());
                vecRowData.add(EMPTY + validRatesBean.getOffCampusRate());
                vecRowData.add(validRatesBean.getAdjustmentKey());
                cvData.add(vecRowData);
            }
        }
        return cvData;
    }
    
	/**
	 * validate method
	 * @return boolean
	 * @throws coeusGUIException
	 */	
    public boolean validate() throws CoeusUIException {
        return false;
    }
    
	/**
	 * action performed method
	 * @param actionEvent ActionEvent
	 */	
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(validRatesForm.btnClose)) {
            dlgValidRates.dispose();
        }
    }
    
	/**
	 * mouse clicked
	 * @param mouseEvent MouseEvent
	 */	
    public void mouseClicked(MouseEvent mouseEvent) {
        int size = validRatesForm.tblValidRates.getRowCount();
        Point clickedPoint = mouseEvent.getPoint();
        int xPosition = (int)clickedPoint.getX();
        int columnIndex = validRatesForm.tblValidRates.getColumnModel().getColumnIndexAtX(xPosition);
        switch (columnIndex) {
            case ON_CAMPUS:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                    cvValidRatesData.sort("onCampusRate", false);
                    sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                    cvValidRatesData.sort("onCampusRate", true);
                    sortCodeAsc = true;
                }
				validRatesTableModel.fireTableDataChanged();
                break;
            case OFF_CAMPUS:
                if(sortDescAsc){
                    cvValidRatesData.sort("offCampusRate",false);
                    sortDescAsc = false;
                }else {
                    cvValidRatesData.sort("offCampusRate",true);
                    sortDescAsc = true;
                }
				validRatesTableModel.fireTableDataChanged();
                break;
            case ADJUSTMENT_KEY:
                if(sortDescAsc){
                    cvValidRatesData.sort("adjustmentKey",false);
                    sortDescAsc = false;
                }else {
                    cvValidRatesData.sort("adjustmentKey",true);
                    sortDescAsc = true;
                }
				validRatesTableModel.fireTableDataChanged();
                break;
                
        }//End Switch
    }
    
	/**
	 * mouse entered
	 * @param e MouseEvent
	 */	
    public void mouseEntered(MouseEvent e) {
    }
    
	/**
	 * mouse exited
	 * @param e MouseEvent
	 */	
    public void mouseExited(MouseEvent e) {
    }
    
	/**
	 * mouse pressed
	 * @param e MouseEvent
	 */	
    public void mousePressed(MouseEvent e) {
    }
    
	/**
	 * Mouse released
	 * @param e MouseEvent
	 */	
    public void mouseReleased(MouseEvent e) {
    }
    
	/**
	 * Setting the
	 */	
    private void setTableEditors(){
        validRatesForm.tblValidRates.setRowHeight(22);
        TableColumn column =  validRatesForm.tblValidRates.getColumnModel().getColumn(ON_CAMPUS);
        column.setMinWidth(115);
        column.setPreferredWidth(115);
        column.setResizable(true);
       
        column =  validRatesForm.tblValidRates.getColumnModel().getColumn(OFF_CAMPUS);
        column.setMinWidth(115);
        column.setPreferredWidth(115);
        column.setResizable(true);
        
        column =  validRatesForm.tblValidRates.getColumnModel().getColumn(ADJUSTMENT_KEY);
        column.setMinWidth(115);
        column.setPreferredWidth(115);
        column.setResizable(true);
    }
    
	/**
	 * This is an inner class which represents the table model
	 */	
    class ValidRatesTableModel extends AbstractTableModel {
        
        String colNames[] = {"On Campus rate" , "Off Campus Rate" , "Adjustment Key"};
        Class[] colTypes = new Class [] {String.class , String.class ,String.class};
        
		/**
		 * Getting the column count
		 * @return int
		 */		
        public int getColumnCount() {
            return colNames.length;
        }
        
		/**
		 * Setting the vector values
		 * @param cvValidRatesData CoeusVector
		 */		
        public void setData(CoeusVector cvValidRatesData){
            cvValidRatesData = cvValidRatesData;
        }
        
		/**
		 * Getting the column class
		 * @param col int
		 * @return Class
		 */		
        public Class getColumnClass(int col){
            return colTypes[col];
        }
		/**
		 * Getting the column name
		 * @param col int
		 * @return String
		 */		
        public String getColumnName(int col){
            return colNames[col];
        }
        
		/**
		 * getting the row count
		 * @return int
		 */		
        public int getRowCount() {
            if(cvValidRatesData== null){
                return 0;
            }else{
                return cvValidRatesData.size();
            }
        }
        
		/**
		 * get value method
		 * @param rowIndex int
		 * @param columnIndex int
		 * @return Object
		 */		
        public Object getValueAt(int rowIndex, int columnIndex) {
            ValidRatesBean  bean = (ValidRatesBean)cvValidRatesData.get(rowIndex);
            switch(columnIndex){
                case ON_CAMPUS:
                    return new Double(bean.getOnCampusRate());
                case OFF_CAMPUS:
                    return new Double(bean.getOffCampusRate());
                case ADJUSTMENT_KEY:
                    return bean.getAdjustmentKey();
            }
            return EMPTY_STRING;
        }
        
		/**
		 * Setting up the value in the respective columns
		 * @param value Object
		 * @param row int
		 * @param col int
		 */		
        public void setValueAt(Object value, int row, int col){
            ValidRatesBean validBean = (ValidRatesBean)cvValidRatesData.get(row);
            switch(col){
                case ON_CAMPUS:
                    double onCampus = Double.parseDouble(value.toString());
                    if( onCampus != validBean.getOnCampusRate()) {
                        validBean.setOnCampusRate(onCampus);
                    }
                    break;
                case OFF_CAMPUS:
                    double offCampus = Double.parseDouble(value.toString());
                    if( offCampus != validBean.getOffCampusRate()) {
                        validBean.setOffCampusRate(offCampus);
                    }
                    break;
                case ADJUSTMENT_KEY:
                    if (!value.toString().trim().equals(validBean.getAdjustmentKey().trim())) {
                        validBean.setAdjustmentKey(value.toString());
                    }
                    break;
            }
        }
        
    }
    
	/**
	 * To initialize all the instance variable to null
	 */
	public void cleanUp() {
		validRatesForm = null;
		dlgValidRates = null;
		cvValidRatesData = null;
		validRatesBean = null;
		validRatesTableModel = null;
	}
}