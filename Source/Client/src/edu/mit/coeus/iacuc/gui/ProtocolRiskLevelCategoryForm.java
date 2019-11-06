/*
 * @(#)ProtocolRiskLevelCategoryForm.java 1.0 04/033/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.iacuc.bean.ProtocolRiskLevelBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusButton;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author leenababu
 */
public class ProtocolRiskLevelCategoryForm extends javax.swing.JComponent implements ActionListener{
    
    private String protocolNumber;
    private Vector vecRiskLevels;
    private CoeusVector cvProtRiskLevels;
    private CoeusVector cvFilteredProtRiskLevels;
    
    private RiskLevelTableModel riskLevelTableModel;
    private RiskLevelCellRenderer riskLevelCellRenderer;
    private RiskLevelCellEditor riskLevelCellEditor;
    private HashMap hmRiskCategory;
    private DateUtils dateUtils = new DateUtils();
    
    public CoeusButton btnShowAll;
    public JTable tblRiskLevelCategory;
    
    private int RISK_COL = 0;
    private int DATE_ASSIGNED_COL = 1;
    private int DATE_UPDATED_COL = 2;
    private int STATUS_COL = 3;
    private int COMMENTS_COL = 4;
    private int COMMENTS_ICON_COL = 5;
    
    /*field to denote whether data is fetched from database or not*/
    private boolean dataFetched;
    private char functionType;
    private final String PROTOCOL_SERVLET = "/IacucProtocolServlet";
    private static final char GET_RISK_LEVELS = '2';
    private ImageIcon imgIcnComments = new ImageIcon(getClass().getClassLoader().getResource(
            CoeusGuiConstants.JUSTIFIED));
    
    /** Creates a new instance of ProtocolRiskLevelCategoryForm */
    public ProtocolRiskLevelCategoryForm(char functionType, String protocolNumber) {
        this.functionType = functionType;
        this.protocolNumber = protocolNumber;
        initComponents();
        postInitComponents();
    }
    
    /**
     * Initialises the gui components
     */
    public void initComponents(){
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        
        btnShowAll = new CoeusButton();
        btnShowAll.setText("Show All");
        btnShowAll.setPreferredSize(new java.awt.Dimension(110, 25));
        btnShowAll.setMaximumSize(new java.awt.Dimension(110, 25));
        btnShowAll.setMinimumSize(new java.awt.Dimension(110, 25));
        btnShowAll.setMnemonic('a');
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 3, 0, 3);
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        add(btnShowAll, gridBagConstraints);
        
        tblRiskLevelCategory = new JTable();
        tblRiskLevelCategory.setModel(new DefaultTableModel(
                new Object[][]{},
                new Object [] {"Risk Level", "Date Assigned", "Date Updated","Status", "Comments", " "}
        ));
        JScrollPane scrPnRiskLevels = new JScrollPane();
        scrPnRiskLevels.setMinimumSize(new Dimension(860, 500));
        scrPnRiskLevels.setMaximumSize(new Dimension(860, 500));
        scrPnRiskLevels.setPreferredSize(new Dimension(860, 500));
        scrPnRiskLevels.setViewportView(tblRiskLevelCategory);
        scrPnRiskLevels.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EtchedBorder(), "Risk Levels",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                CoeusFontFactory.getLabelFont()));
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        add(scrPnRiskLevels, gridBagConstraints);
        
    }
    
    /**
     * Set the renderer, editors and model for the gui components
     */
    public void postInitComponents(){
        riskLevelTableModel = new RiskLevelTableModel();
        tblRiskLevelCategory.setModel(riskLevelTableModel);
        
        setTableEditors();
        
        tblRiskLevelCategory.getTableHeader().setReorderingAllowed(false);
        tblRiskLevelCategory.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblRiskLevelCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblRiskLevelCategory.setRowHeight(20);
        
        btnShowAll.addActionListener(this);
    }
    
    /**
     * Set the table editors and renderes
     */
    public void setTableEditors(){
        riskLevelCellRenderer = new RiskLevelCellRenderer();
        riskLevelCellEditor = new RiskLevelCellEditor();
        
        TableColumn tableColumn = tblRiskLevelCategory.getColumnModel().getColumn(RISK_COL);
        tableColumn.setPreferredWidth(180);
        tableColumn.setCellRenderer(riskLevelCellRenderer);
        
        tableColumn = tblRiskLevelCategory.getColumnModel().getColumn(DATE_ASSIGNED_COL);
        tableColumn.setPreferredWidth(60);
        tableColumn.setCellRenderer(riskLevelCellRenderer);
        
        tableColumn = tblRiskLevelCategory.getColumnModel().getColumn(DATE_UPDATED_COL);
        tableColumn.setPreferredWidth(60);
        tableColumn.setCellRenderer(riskLevelCellRenderer);
        
        tableColumn = tblRiskLevelCategory.getColumnModel().getColumn(STATUS_COL);
        tableColumn.setPreferredWidth(45);
        tableColumn.setCellRenderer(riskLevelCellRenderer);
        
        tableColumn = tblRiskLevelCategory.getColumnModel().getColumn(COMMENTS_COL);
        tableColumn.setPreferredWidth(270);
        tableColumn.setCellRenderer(riskLevelCellRenderer);
        
        tableColumn = tblRiskLevelCategory.getColumnModel().getColumn(COMMENTS_ICON_COL);
        tableColumn.setMinWidth(20);
        tableColumn.setMaxWidth(20);
        tableColumn.setPreferredWidth(20);
        tableColumn.setCellRenderer(riskLevelCellRenderer);
        tableColumn.setCellEditor(riskLevelCellEditor);
    }
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source.equals(btnShowAll)){
            if(btnShowAll.getText().equals("Show All")){
                btnShowAll.setText("Show Active");
                populateRiskLevels(false);
            }else{
                btnShowAll.setText("Show All");
                populateRiskLevels(true);
            }
            
        }
    }
    /**
     * Sets the form data
     */
    public void setFormData(){
        dataFetched = true;
        if(protocolNumber != null){
            if( protocolNumber.indexOf( 'A' ) != -1 ||  protocolNumber.indexOf( 'R' ) != -1 ) {
                protocolNumber = protocolNumber.substring(0,10);
            }
            Vector vecServerData = getFormData();
            if(vecServerData !=null && vecServerData.size()==2){
                cvProtRiskLevels = (CoeusVector)vecServerData.get(0);
                vecRiskLevels = (Vector)vecServerData.get(1);
                hmRiskCategory = new HashMap();
                
                //Populate the hashmap hmRiskCategory with the risk level categories
                //with code as key and value the description
                if(vecRiskLevels != null){
                    ComboBoxBean comboBoxBean = null;
                    for(int i=0; i < vecRiskLevels.size(); i++){
                        comboBoxBean = (ComboBoxBean)vecRiskLevels.get(i);
                        hmRiskCategory.put(comboBoxBean.getCode(), comboBoxBean.getDescription());
                    }
                }
                
            }
            populateRiskLevels(true);
        }
    }
    
    /**
     *Populate the risk level table
     */
    public void populateRiskLevels(boolean active){
        if(active){
            if(cvProtRiskLevels!=null){
                Equals equalsActive = new Equals("status", "A");
                cvFilteredProtRiskLevels =
                        cvProtRiskLevels.filter(equalsActive);
            }
        }else{
            cvFilteredProtRiskLevels = cvProtRiskLevels;
        }
        if(cvFilteredProtRiskLevels != null){
            riskLevelTableModel.setTableData(cvFilteredProtRiskLevels);
            riskLevelTableModel.fireTableDataChanged();
        }else{
            riskLevelTableModel.setTableData(new CoeusVector());
            riskLevelTableModel.fireTableDataChanged();
        }
        setTableEditors();
        if(tblRiskLevelCategory.getRowCount() > 0 ) {
            tblRiskLevelCategory.requestFocusInWindow();
            tblRiskLevelCategory.setRowSelectionInterval(0, 0);
            tblRiskLevelCategory.setColumnSelectionInterval(1,1);
        }
    }
    
    /**
     * Get the form data from the server
     */
    public Vector getFormData(){
        String connectTo =CoeusGuiConstants.CONNECTION_URL
                + PROTOCOL_SERVLET;
        Vector vecServerData = null;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_RISK_LEVELS);
        requesterBean.setDataObject(protocolNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        
        ResponderBean responderBean = comm.getResponse();
        try {
            if(responderBean != null && responderBean.hasResponse()){
                vecServerData = responderBean.getDataObjects();
            }
        } catch (CoeusException ex) {
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
        return vecServerData;
    }
    public class RiskLevelTableModel extends DefaultTableModel{
        
        public String[] columnNames = {"Risk Level", "Date Assigned", "Date Updated", "Status", "Comments", ""};
        public CoeusVector cvRiskLevelforTable = new CoeusVector();
        
        public void setTableData(CoeusVector cvRiskLevels){
            cvRiskLevelforTable = cvRiskLevels;
        }
        
        public boolean isCellEditable(int row, int column){
            if(column == COMMENTS_ICON_COL){
                return true;
            }
            return false;
        }
        public void setValueAt(Object aValue, int row, int column) {
        }
        public Object getValueAt(int row, int column) {
            ProtocolRiskLevelBean protocolRiskLevelBean =
                    (ProtocolRiskLevelBean)cvRiskLevelforTable.get(row);
            if(column == RISK_COL){
                return protocolRiskLevelBean.getRiskLevelCode();
            }else if(column == DATE_ASSIGNED_COL){
                return protocolRiskLevelBean.getDateAssigned();
            }else if(column == DATE_UPDATED_COL){
                return protocolRiskLevelBean.getDateUpdated();
            }else if(column == COMMENTS_COL){
                return protocolRiskLevelBean.getComments();
            }else if(column == STATUS_COL){
                return protocolRiskLevelBean.getStatus();
            }
            return "";
        }
        
        public String getColumnName(int column) {
            return columnNames[column];
        }
        
        public int getRowCount(){
            if(cvRiskLevelforTable!=null){
                return cvRiskLevelforTable.size();
            }
            return 0;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }
        
    }
    public class RiskLevelCellRenderer extends DefaultTableCellRenderer{
        private JLabel lblText;
        private JButton btnComments;
        Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        public RiskLevelCellRenderer(){
            lblText = new JLabel();
            btnComments = new JButton(imgIcnComments);
            lblText.setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if(column == COMMENTS_ICON_COL){
                return btnComments;
            }else{
                if(isSelected){
                    lblText.setBackground(java.awt.Color.YELLOW);
                }else{
                    lblText.setBackground(bgListColor);
                }
                if(value!=null && !value.toString().trim().equals("")){
                    
                    if(column == RISK_COL){
                        lblText.setText((String)hmRiskCategory.get(value.toString()));
                        
                    }else if(column == DATE_ASSIGNED_COL || column == DATE_UPDATED_COL){
                        lblText.setText(dateUtils.formatDate(
                                value.toString(),"dd-MMM-yyyy"));
                        
                    }else if(column == STATUS_COL){
                        if(value.toString().equals("A")){
                            lblText.setText("Active");
                        }else{
                            lblText.setText("Inactive");
                        }
                        
                    }else{
                        lblText.setText(value.toString());
                    }
                }else{
                    lblText.setText("");
                }
            }
            return lblText;
        }
        
    }
    
    public class RiskLevelCellEditor extends DefaultCellEditor implements ActionListener{
        
        private JButton btnComments;
        private edu.mit.coeus.utils.CommentsForm commentsForm;
        public RiskLevelCellEditor(){
            super(new JComboBox());
            btnComments = new JButton(imgIcnComments);
            btnComments.addActionListener(this);
            
            commentsForm = new edu.mit.coeus.utils.CommentsForm("Comments");
        }
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if(column == COMMENTS_ICON_COL){
                return btnComments;
            }
            return btnComments;
        }
        
        public void actionPerformed(ActionEvent e){
            int selectedRow = tblRiskLevelCategory.getSelectedRow();
            if(selectedRow != -1){
                ProtocolRiskLevelBean protocolRiskLevelBean =
                        (ProtocolRiskLevelBean)cvFilteredProtRiskLevels.get(selectedRow);
                commentsForm.setData(protocolRiskLevelBean.getComments());
                commentsForm.display();
                tblRiskLevelCategory.getCellEditor(selectedRow, COMMENTS_ICON_COL).stopCellEditing();
            }
        }
    }
    
    
    public boolean isDataFetched() {
        return dataFetched;
    }
    
    public void setDataFetched(boolean dataFetched) {
        this.dataFetched = dataFetched;
    }

    public char getFunctionType() {
        return functionType;
    }
}
