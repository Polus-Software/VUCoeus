/**
 * AwardRestrictionsTableModel.java
 * 
 * Table model for award restrictions table
 *
 * @created	September 29, 2014
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.award;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.Color;
import java.sql.Date;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.TypeConstants;
import edu.vanderbilt.coeus.award.bean.AwardRestrictionsBean;
import edu.vanderbilt.coeus.award.gui.AwardRestrictionsForm;


public class AwardRestrictionsTableModel extends AbstractTableModel {
    
	protected AwardRestrictionsForm form;
    private CoeusVector cvRestrictions;
    public Vector vecRestrictionCodes, vecRestrictionMaintainers;
    private String EMPTY_STRING = "";
    private SimpleDateFormat dateFormat2, slashFormat2, dashFormat2, dateFormat4, slashFormat4, dashFormat4;
    
    /* Columns */
    public static final int SEQUENCE = 0;
    public static final int RESTRICTION_DESC = 1;
    public static final int DUE_DATE = 2;
    public static final int ACTION_DATE = 3;
    public static final int STATUS = 4;
    public static final int ASSIGNED_USER_NAME = 5;
    /* Columns below not shown */
    private static final int ASSIGNED_USER = 6;
    private static final int RESTRICTION_TYPE_CODE = 7;
    private static final int RESTRICTION_NUMBER = 8;
    private static final int COMMENTS = 9;
    
    /* Column names */
	private String[] columnTitles = new String[] {"Seq","Restriction Type","Due Date","Action Date",
		"Status","Assigned User","Assigned User ID","Restriction Type Code","Restriction Number","Comments"};

	/* Column classes */
    private Class columnClasses[] = {Integer.class, String.class,String.class,String.class,
    		String.class,String.class,String.class,Integer.class,Integer.class,String.class};

    private boolean editable;
    private char functionType;

    public AwardRestrictionsTableModel(CoeusVector cvRestrictions,char functionType) {
    	this.cvRestrictions = cvRestrictions;
    	this.functionType = functionType;
    	vecRestrictionCodes = new Vector();
        slashFormat4 = new SimpleDateFormat("MM/dd/yyyy");
        dashFormat4 = new SimpleDateFormat("MM-dd-yyyy");
        dateFormat4 = new SimpleDateFormat("dd-MMM-yyyy");
        slashFormat2 = new SimpleDateFormat("MM/dd/yy");
        dashFormat2 = new SimpleDateFormat("MM-dd-yy");
        dateFormat2 = new SimpleDateFormat("dd-MMM-yy");
    }
    
    public CoeusVector getData() {
    	return cvRestrictions;
    }
    
    public int getColumnCount() {
    	return columnTitles.length;
    }
    
    public int getRowCount() {
    	return cvRestrictions.size();
    }
    
    public String getColumnName(int column) {
    	  return columnTitles[column];
    }
    

    public Object getValueAt(int row, int column) {
		AwardRestrictionsBean bean = (AwardRestrictionsBean) cvRestrictions.get(row);
        switch(column) {
	        case SEQUENCE:
	        	return bean.getSequenceNumber();
	        case RESTRICTION_DESC:
	            return bean.getRestrictionTypeDescription();
	        case DUE_DATE:
	            if (bean.getDueDate() != null && !bean.getDueDate().toString().trim().isEmpty()) {
			        String fdate = dateFormat4.format(bean.getDueDate());
	                return fdate;
	            }
	            else {
	            	return null;
	            }
	        case ACTION_DATE:
	            if (bean.getActionDate() != null && !bean.getActionDate().toString().trim().isEmpty()) {
			        String fdate = dateFormat4.format(bean.getActionDate());
	                return fdate;
	            }
	            else {
	            	return null;
	            }
	        case STATUS:
	            return bean.getStatus();
	        case ASSIGNED_USER_NAME:
	            return bean.getAssignedUserName();
	        case ASSIGNED_USER:
	            return bean.getAssignedUser();
	        case RESTRICTION_TYPE_CODE:
	            return bean.getRestrictionTypeCode();
	        case RESTRICTION_NUMBER:
	        	return bean.getAwardRestrictionNumber();
	        case COMMENTS:
	        	return bean.getComments();
        }
        return EMPTY_STRING;
      }

    public Class getColumnClass(int column) {
  	  return columnClasses[column];
    }
    
    /* Checks to see which fields are editable */
    public boolean isCellEditable(int row,int col) {
    	if (functionType == CoeusGuiConstants.DISPLAY_MODE) {
    		return false;
    	}
    	else {
	    	if (col == SEQUENCE || col == ASSIGNED_USER || col == RESTRICTION_TYPE_CODE || 
	    			col == RESTRICTION_NUMBER || col == COMMENTS) {
	    		return false;
	    	}
	        else {
	        	return true;
	        }
    	}
    }
    
    public void setData(CoeusVector cvRestrictions) {
    	this.cvRestrictions = cvRestrictions;
    	
    }

    public void setValueAt(Object value, int row, int column) {
    	ComboBoxBean comboBoxBean = new ComboBoxBean();
    	AwardRestrictionsBean bean = (AwardRestrictionsBean) cvRestrictions.get(row);
        java.sql.Date sDate = null;
        int ix = 0;
    	if (value != null && !value.toString().isEmpty()) {
    		switch(column) {
		    	case SEQUENCE:
		    		bean.setSequenceNumber((Integer) value); 
		        	break;
		        case RESTRICTION_DESC:
	        		if (value.getClass() == ComboBoxBean.class) {
			        	comboBoxBean = (ComboBoxBean) value;
			        	ix = (Integer) vecRestrictionCodes.indexOf(comboBoxBean.getCode());
			            bean.setRestrictionTypeDescription(comboBoxBean.getDescription());
			            bean.setRestrictionTypeCode(Integer.parseInt(comboBoxBean.getCode()));
		        	}
		            break;
		        case DUE_DATE:
		        	sDate = processDate(value.toString(),row,column);
	        		bean.setDueDate(sDate);
	                break;
		        case ACTION_DATE:
		        	sDate = processDate(value.toString(),row,column);
	        		bean.setActionDate(sDate);
	                break;
		        case STATUS:
	        		bean.setStatus((String) value);
		            break;
		        case ASSIGNED_USER_NAME:
		        	if (value.getClass() == ComboBoxBean.class) {
			        	comboBoxBean = (ComboBoxBean) value;
			        	ix = (Integer) vecRestrictionMaintainers.indexOf(comboBoxBean.getCode());
			            bean.setAssignedUserName(comboBoxBean.getDescription());
			            bean.setAssignedUser(comboBoxBean.getCode());
		        	}
		            break;
		        case ASSIGNED_USER:
		            bean.setAssignedUser((String) value);
		            break;
		        case RESTRICTION_TYPE_CODE:
		            // set above in description
		            break;
		        case RESTRICTION_NUMBER:
		            bean.setAwardRestrictionNumber((Integer) value);
		            break;
		        case COMMENTS:
	        		bean.setComments((String) value);
		        	break;
	        }
	        fireTableCellUpdated(row, column);
    	}
    }
    
    private java.sql.Date processDate(String value,int row,int col) {
        java.sql.Date sDate = null;
        java.util.Date uDate = null;
    	try {
            if (value != null && value != "Error") {
            	
            	if (value.toString().matches(".*[a-zA-Z].*")) {
    	        	//System.out.println("Value has letters");
    	            try { 
    	            	uDate = dateFormat2.parse(value.toString()); 
    	            }  
    	            catch (NumberFormatException e) { 
    	            	uDate = dateFormat4.parse(value.toString()); 
    	            }  
            	}
            	else if (value.toString().contains("/")) {
    	        	//System.out.println("Value has slashes");	                		
    	            try { 
    	            	uDate = slashFormat2.parse(value.toString()); 
    	            }  
    	            catch (NumberFormatException e) { 
    	            	uDate = slashFormat4.parse(value.toString()); 
    	            }  
            	}
            	else if (value.toString().contains("-")) {
    	        	//System.out.println("Value has dashes");
    	            try { 
    	            	uDate = dashFormat2.parse(value.toString()); 
    	            }  
    	            catch (NumberFormatException e) { 
    	            	uDate = dashFormat4.parse(value.toString()); 
    	            }  
            	}
            	else if (value.toString().trim().equals(EMPTY_STRING)) {
    	        	//System.out.println("Value is EMPTY_STRING");
            		uDate = null;
            	}
            	else {
            		//System.out.println("Value falls through the cracks");
            	}
            	if (uDate != null) {
            		sDate = new java.sql.Date(uDate.getTime());
            	}
            	else {
            		sDate = null;
            	}
        	}
            else {
            	sDate = null;
            }
    	}
		catch (ParseException e) {
			System.out.println("Could not convert String to Date in AwardRestrictionsTableModel");
			form.tblRestrictions.changeSelection(row,col,false,false);
			form.tblRestrictions.setValueAt(sDate, row, col);
			form.tblRestrictions.requestFocus();
		}
    	return sDate;
    }

    public boolean isEditable() {
        return editable;
    }
    
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
}
