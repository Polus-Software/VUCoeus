/**
 * ProposalCentersForm.java
 * 
 * GUI for IP centers tab
 *
 * @created	April 12, 2012
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.instprop.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.vanderbilt.coeus.instprop.bean.ProposalCentersBean;

public class ProposalCentersForm extends javax.swing.JPanel {
	
    public String txtAwardSeq;
    public String txtInstProp;
    public String txtInstPropSeq;
    public String txtDevProp;
    //public String txtBaseCenterNum;
    public String txtCenterNum;
    public JCheckBox chkBaseCenter;
        
    private JPanel pnlCenterList;
    private JScrollPane scrPnCenterList;
    public JTable tblCenterList;
    private CoeusVector cvCenters;
    
    // Columns returned for centers
    private static final int CENTER_NUMBER = 0;
    private static final int BASE_CENTER = 1;
    private static final int CENTER_DESC = 2;
    private static final int INITIATE_MODE = 3;
    private static final int AWARD_NUMBER = 4;
    private static final int AWARD_SEQUENCE = 5;
    private static final int SPONSOR_AWARD = 6;
    private static final int INST_PROP_NUMBER = 7;
    private static final int DEV_PROP_NUMBER = 8;
    private static final int CREATE_DATE = 9;
    private static final int PROCESS_DATE = 10;

    // Edit here to include columns in results
    private Integer[] colsToInclude = new Integer[] {AWARD_SEQUENCE,SPONSOR_AWARD,CENTER_NUMBER,CENTER_DESC,BASE_CENTER};
	private String[] colNames = new String[]{"Award Seq.", "Sponsor Award #", "Center #",  "Center Desc", "Base Center"};

    public ProposalCentersForm() {
    	//constructor
    }	
	
    /** Creates new form ProposalCenters */
	public ProposalCentersForm(CoeusVector cvCenters) {
    	this.cvCenters = cvCenters;
        initComponents();
    }
	
	private void initComponents() {
		GridBagConstraints gridBagConstraints;
		
		pnlCenterList = new JPanel(new FlowLayout(FlowLayout.LEFT));
		scrPnCenterList = new JScrollPane();
		tblCenterList = new JTable();
		chkBaseCenter = new JCheckBox();
		TableColumn column;
		
    	Integer[] colWidths = new Integer[] {30,120,70,250,30};
		
		setLayout(new GridBagLayout());
		
        scrPnCenterList.setBorder(BorderFactory.createTitledBorder(null, "IP Center Numbers", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 11)));
        scrPnCenterList.setMaximumSize(new java.awt.Dimension(800, 500));
        scrPnCenterList.setMinimumSize(new java.awt.Dimension(800, 500));
        scrPnCenterList.setPreferredSize(new java.awt.Dimension(800, 500));
        
        DefaultTableModel model = new DefaultTableModel();
        model.setDataVector(getDataRows(), getColNames());
        tblCenterList.setModel(model);
        tblCenterList.setEnabled(false);
        tblCenterList.setBackground(UIManager.getDefaults().getColor("Panel.background"));
    	tblCenterList.setFont(CoeusFontFactory.getNormalFont());
        tblCenterList.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
    	tblCenterList.setRowHeight(22);
        for (int i=0; i < tblCenterList.getColumnCount(); i++) {
        	column = tblCenterList.getColumnModel().getColumn(i);
        	if (column.getHeaderValue() == "Center Desc") {
            	column.setResizable(true);      		
        	}
        	if (column.getHeaderValue() == "Base Center") {
        		column.setCellRenderer(tblCenterList.getDefaultRenderer(Boolean.class));
        	}
        	column.setPreferredWidth(colWidths[i]);
    	}
    	scrPnCenterList.setViewportView(tblCenterList);
		pnlCenterList.add(scrPnCenterList);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.weighty = 0.5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		add(pnlCenterList, gridBagConstraints);
	}

	/* Methods to build table */
    private Vector getColNames() {
    	List colList = Arrays.asList(colNames);
    	Vector vColNames = new Vector(colList);
    	return vColNames;
    }
    
    private Vector getDataRows() {
    	Vector data = new Vector();
    	ProposalCentersBean bean = null;
    	int col;
    	if (cvCenters != null) {
	    	for (int row = 0; row < cvCenters.size(); row++) {
	    		Vector rowData = new Vector();
	    		col = 0;
	    		for (Integer selCol : colsToInclude) {
	    			if (selCol == BASE_CENTER) {
	    				rowData.add(col, (Boolean) getValueAt(row,selCol));
	    			}
	    			else {
	    				rowData.add(col, (String) getValueAt(row,selCol));
	    			}
	    			col++;
	    		}
	    		data.add(rowData);
	    	}
    	}
    	return data;
    }
    
	public Object getValueAt(int rowIndex, int columnIndex) {
		ProposalCentersBean bean = (ProposalCentersBean) cvCenters.get(rowIndex);
        switch(columnIndex){
        case CENTER_NUMBER:
            return bean.getCenterNum();
        case BASE_CENTER:
            return bean.getBaseCenter();
        case CENTER_DESC:
            return bean.getCenterDesc();
        case INITIATE_MODE:
            return bean.getInitiateMode();
        case AWARD_NUMBER:
            return bean.getAwardNum();
        case AWARD_SEQUENCE:
        	if (bean.getAwardNum() == null) {
        		return null;
        	}
        	else {
        		return bean.getAwardSeq().toString();
        	}
        case SPONSOR_AWARD:
            return bean.getSponsorAward();
        case INST_PROP_NUMBER:
            return bean.getInstPropNum();
        case DEV_PROP_NUMBER:
            return bean.getDevPropNum();
        case CREATE_DATE:
            if (bean.getCreateDate() != null) {
                return bean.getCreateDate().toString();
            }
        case PROCESS_DATE:
            if(bean.getProcessDate() != null) {
                return bean.getProcessDate().toString();
            }
        }
        return null;
	}
	
    public static void main(String s[]){
        JFrame frame = new JFrame("IP Centers");
        ProposalCentersForm proposalCentersForm = new ProposalCentersForm();
        frame.getContentPane().add(proposalCentersForm);
        frame.setSize(1100, 620);
        frame.show();
     }
}
