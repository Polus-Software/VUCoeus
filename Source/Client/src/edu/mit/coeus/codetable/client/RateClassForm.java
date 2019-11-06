/*
 * RateClassForm.java
 *
 * Created on December 9, 2011, 12:08 PM
 *//** Copyright (c) Massachusetts Institute of Technology
  * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
  * All rights reserved.
  */

package edu.mit.coeus.codetable.client;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.calculator.bean.RateClassBaseExclusionBean;
import edu.mit.coeus.budget.calculator.bean.RateClassBaseInclusionBean;
import edu.mit.coeus.rates.bean.RateClassBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.rates.bean.RateTypeBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author  manjunathabn
 */
public class RateClassForm extends CoeusDlgWindow implements ActionListener,ItemListener {
    
    private char GET_RATE_CLASS_TYPE_DATA = 'R';
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
            "/RatesMaintenanceServlet";
    private CoeusVector cvRateClassList = null;
    private CoeusVector cvRateTypeList = null;
    private CoeusVector cvRateClassInclusionsList = null;
    private CoeusVector cvRateClassExclusionsList = null;
    private static int ROW_COUNT = 0;
    private static int COLUMN_COUNT = 2;
    private CoeusVector cvFilteredRateClass = new CoeusVector();
    private CoeusVector cvFilteredRateClassInclusions = new CoeusVector();
    private CoeusVector cvFilteredRateClassExclusions = new CoeusVector();
    private static String LINE_ITEM_COST = "Line Item Cost";
    private static String EMPTY_STRING ="";
    private static String RATE_CLASS_COLUMN = "Rate Class";
    private static String RATE_TYPE_COLUMN = "Rate Type";
    private static int ROW_HEIGHT = 22;
    private static int COLUMN_WIDTH = 72;
    private static String RATE_CLASS_CODE = "rateClassCode";
    private static String RATE_TYPE_CODE = "rateTypeCode";
    private static final int WIDTH = 740;
    private static final int HEIGHT = 305;
    
    /**
     * Creates new form RateClassForm
     * @param parent
     * @param title
     */
    public RateClassForm(Frame parent, String title) {
        super(CoeusGuiConstants.getMDIForm(),title);
        initComponents();
        this.setSize(WIDTH,HEIGHT);
        registerComponents();
        setTableEditors();
        setFormData();
       
    }
    
    
    public void registerComponents() {
        cmbRateType.addItemListener(this);
        cmbRateClass.addItemListener(this);
    }
    
    
    /*To set the renderers and editors to the table*/
    private void setTableEditors(){
        tblRateInclusion.setRowHeight(ROW_HEIGHT);
        tblRateInclusion.setShowHorizontalLines(false);
        tblRateInclusion.setShowVerticalLines(false);
        JTableHeader tableInclusionHeader = tblRateInclusion.getTableHeader();
        tableInclusionHeader.setReorderingAllowed(false);
        tableInclusionHeader.setFont(CoeusFontFactory.getLabelFont());
        tblRateInclusion.setOpaque(false);
        TableColumn column = tblRateInclusion.getColumnModel().getColumn(0);
        column.setPreferredWidth(COLUMN_WIDTH);
        column = tblRateInclusion.getColumnModel().getColumn(1);
        column.setPreferredWidth(COLUMN_WIDTH);
        column.setResizable(false);
        
        tblRateExclusion.setRowHeight(ROW_HEIGHT);
        tblRateExclusion.setShowHorizontalLines(false);
        tblRateExclusion.setShowVerticalLines(false);
        JTableHeader tableExclusionHeader = tblRateExclusion.getTableHeader();
        tableExclusionHeader.setReorderingAllowed(false);
        tableExclusionHeader.setFont(CoeusFontFactory.getLabelFont());
        tblRateExclusion.setOpaque(false);
        TableColumn columnExclusion = tblRateExclusion.getColumnModel().getColumn(0);
        columnExclusion.setPreferredWidth(COLUMN_WIDTH);
        columnExclusion = tblRateExclusion.getColumnModel().getColumn(1);
        columnExclusion.setPreferredWidth(COLUMN_WIDTH);
        columnExclusion.setResizable(false);
    }
    
    /**
     * Sets the Form Data
     */
    private void setFormData(){
        fetchRatesData();
        
        cmbRateClass.setModel(new DefaultComboBoxModel(cvRateClassList));
        
        if(cvRateTypeList != null && cvRateTypeList.size() > 0){
            RateClassBean rateClassBean = (RateClassBean)cvRateClassList.get(0);
            int code = Integer.parseInt(rateClassBean.getCode());
            Equals rt = new Equals( RATE_CLASS_CODE , new Integer(code) );
            cvFilteredRateClass.add(EMPTY_STRING);
            for(Object rateTypeList : cvRateTypeList.filter(rt)) {
                RateTypeBean rateTypeBean = (RateTypeBean)rateTypeList;
                cvFilteredRateClass.add(rateTypeBean.getDescription());
            }
            cmbRateType.setModel(new DefaultComboBoxModel(cvFilteredRateClass));
        }
        setInclusionTableData();
        setExclusionTableData();
    }
    
    /**
     * Sets the Table Data
     * @param cvFilteredTableData
     * @param resultTable
     * @param type
     */
    private void setTableData(CoeusVector cvFilteredTableData, JTable resultTable, String type) {
        Vector vctResult = new Vector();
        if(cvFilteredTableData != null && !cvFilteredTableData.isEmpty()) {
            for(Object obj : cvFilteredTableData){
                CoeusVector cvFilteredBaseData = new CoeusVector();
                Equals rtClass = null;
                Equals rtType = null;
                if("Inclusion".equals(type)){
                    RateClassBaseInclusionBean rateClassBaseInclusionBean = (RateClassBaseInclusionBean)obj;
                    rtClass = new Equals( RATE_CLASS_CODE , rateClassBaseInclusionBean.getRateClassCodeIncl());
                    if(rateClassBaseInclusionBean.getRateClassCodeIncl() == 0 ){
                        cvFilteredBaseData.addElement(LINE_ITEM_COST);
                    } else{
                        cvFilteredBaseData.addElement(cvRateClassList.filter(rtClass).toString().replaceAll("\\[","").replaceAll("\\]",""));
                    }
                    rtType = new Equals( RATE_TYPE_CODE , rateClassBaseInclusionBean.getRateTypeCodeIncl());
                } else {
                    RateClassBaseExclusionBean rateClassBaseExclusionBean = (RateClassBaseExclusionBean)obj;
                    rtClass = new Equals( RATE_CLASS_CODE , rateClassBaseExclusionBean.getRateClassCodeExcl());
                    
                    if(rateClassBaseExclusionBean.getRateClassCodeExcl() == 0 ){
                        cvFilteredBaseData.addElement(LINE_ITEM_COST);
                    } else{
                        cvFilteredBaseData.addElement(cvRateClassList.filter(rtClass).toString().replaceAll("\\[","").replaceAll("\\]",""));
                    }
                    rtType = new Equals( RATE_TYPE_CODE , rateClassBaseExclusionBean.getRateTypeCodeExcl());
                }
                
                And rtclassType = new And(rtClass , rtType);
                cvFilteredBaseData.addElement(cvRateTypeList.filter(rtclassType).toString().replaceAll("\\[","").replaceAll("\\]",""));
                vctResult.add(cvFilteredBaseData);
            }
            
            ROW_COUNT = vctResult.size();
            DefaultTableModel tblModel = new DefaultTableModel(ROW_COUNT,COLUMN_COUNT) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            resultTable.setModel(tblModel);
            resultTable.getColumnModel().getColumn(0).setHeaderValue(RATE_CLASS_COLUMN);
            resultTable.getColumnModel().getColumn(1).setHeaderValue(RATE_TYPE_COLUMN);
            
            for(int rowIndex=0;rowIndex<vctResult.size();rowIndex++) {
                CoeusVector cvTableData = (CoeusVector)vctResult.get(rowIndex);
                for(int columnIndex=0;columnIndex<cvTableData.size();columnIndex++) {
                    if(columnIndex == 0){
                        resultTable.setValueAt(cvTableData.get(columnIndex),rowIndex,0);
                    }
                    if(columnIndex == 1){
                        resultTable.setValueAt(cvTableData.get(columnIndex),rowIndex,1);
                    }
                }
            }
        } else {
            setEmptyTableData(resultTable);
        }
    }
    
    /**
     * Sets the Inclusion Table Data
     *
     */
    private void setInclusionTableData() {
        ComboBoxBean comboBeanClass = new ComboBoxBean();
        if(cvRateClassInclusionsList != null && !cvRateClassInclusionsList.isEmpty()) {
            comboBeanClass = (ComboBoxBean)cmbRateClass.getSelectedItem();
            int rateClassCode = Integer.parseInt(comboBeanClass.getCode());
            int rateTypeCode = 0;
            if( (cmbRateType.getSelectedItem() != null) && !("".equals(cmbRateType.getSelectedItem())) ){
                for(Object rateTypeList : cvRateTypeList){
                    RateTypeBean rateTypeBean = (RateTypeBean)rateTypeList;
                    if(cmbRateType.getSelectedItem().equals(rateTypeBean.getDescription())){
                        rateTypeCode = Integer.parseInt(rateTypeBean.getCode());
                    }
                }
            }
            Equals rtClass = new Equals( RATE_CLASS_CODE , new Integer(rateClassCode) );
            Equals rtType = new Equals( RATE_TYPE_CODE , new Integer(rateTypeCode) );
            
            And rtclassType = new And(rtClass , rtType);
            if(rateTypeCode > 0) {
                cvFilteredRateClassInclusions = cvRateClassInclusionsList.filter(rtclassType);
            }else {
                cvFilteredRateClassInclusions = cvRateClassInclusionsList.filter(rtClass);
            }            
            cvFilteredRateClassInclusions.sort("rateClassCodeIncl");
            
            setTableData(cvFilteredRateClassInclusions, tblRateInclusion, "Inclusion");
            
        } else {
            setEmptyTableData(tblRateInclusion);
        }
    }
        
    /**
     * Sets Exclusion the Table Data
     *
     */
    private void setExclusionTableData() {
        ComboBoxBean comboBean = new ComboBoxBean();
        if(cvRateClassExclusionsList != null && !cvRateClassExclusionsList.isEmpty()) {
            comboBean = (ComboBoxBean)cmbRateClass.getSelectedItem();
            int code = Integer.parseInt(comboBean.getCode());
            
            int rateTypeCode = 0;
            if( (cmbRateType.getSelectedItem() != null) && !("".equals(cmbRateType.getSelectedItem())) ){
                for(Object rateTypeList : cvRateTypeList){
                    RateTypeBean rateTypeBean = (RateTypeBean)rateTypeList;
                    if(cmbRateType.getSelectedItem().equals(rateTypeBean.getDescription())){
                        rateTypeCode = Integer.parseInt(rateTypeBean.getCode());
                    }
                }
            }
            Equals rtClass = new Equals( RATE_CLASS_CODE , new Integer(code) );
            Equals rtType = new Equals( RATE_TYPE_CODE , new Integer(rateTypeCode) );
            And rtclassType = new And(rtClass , rtType);
            
            if(rateTypeCode > 0) {
                cvFilteredRateClassExclusions = cvRateClassExclusionsList.filter(rtclassType);
            }else {
                cvFilteredRateClassExclusions = cvRateClassExclusionsList.filter(rtClass);
            }
            cvFilteredRateClassExclusions.sort("rateClassCodeExcl");
            
            setTableData(cvFilteredRateClassExclusions, tblRateExclusion, "Exclusion");
        } else{
            setEmptyTableData(tblRateExclusion);
        }
    }    
       
    /**
     * Sets the Empty Data to Table
     * @param resultTable
     */
    private void setEmptyTableData(JTable resultTable) {
        ROW_COUNT = 0;
        DefaultTableModel tblModel = new DefaultTableModel(ROW_COUNT,COLUMN_COUNT) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable.setModel(tblModel);
        resultTable.getColumnModel().getColumn(0).setHeaderValue(RATE_CLASS_COLUMN);
        resultTable.getColumnModel().getColumn(1).setHeaderValue(RATE_TYPE_COLUMN);
    }
    
    /**
     * Method to display the form
     */
    public void showWindow(){
        Dimension screenSize;
        Dimension dlgSize;
        screenSize =Toolkit.getDefaultToolkit().getScreenSize();
        dlgSize = getSize();
        setLocation(screenSize.width / 2 - (dlgSize.width / 2),
                screenSize.height / 2 - (dlgSize.height / 2));
        this.setVisible(true);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        pnlRateClass = new javax.swing.JPanel();
        lblRateClassCode = new javax.swing.JLabel();
        cmbRateClass = new javax.swing.JComboBox();
        lblRateTypeCode = new javax.swing.JLabel();
        cmbRateType = new javax.swing.JComboBox();
        pnlInclusionExclusion = new javax.swing.JPanel();
        lblBaseInclusion = new javax.swing.JLabel();
        scrPnInclusion = new javax.swing.JScrollPane();
        tblRateInclusion = new javax.swing.JTable();
        lblBaseExclusion = new javax.swing.JLabel();
        scrPnExclusion = new javax.swing.JScrollPane();
        tblRateExclusion = new javax.swing.JTable();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(740, 305));
        setModal(true);
        setResizable(false);
        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setMinimumSize(new java.awt.Dimension(740, 300));
        pnlMain.setPreferredSize(new java.awt.Dimension(740, 305));
        pnlRateClass.setLayout(new java.awt.GridBagLayout());

        pnlRateClass.setMaximumSize(new java.awt.Dimension(710, 18));
        pnlRateClass.setMinimumSize(new java.awt.Dimension(725, 310));
        pnlRateClass.setPreferredSize(new java.awt.Dimension(725, 308));
        lblRateClassCode.setFont(CoeusFontFactory.getLabelFont());
        lblRateClassCode.setText("Calculation Base for Rate Class :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 23, 0);
        pnlRateClass.add(lblRateClassCode, gridBagConstraints);

        cmbRateClass.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));
        cmbRateClass.setMinimumSize(new java.awt.Dimension(170, 18));
        cmbRateClass.setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 23, 4);
        pnlRateClass.add(cmbRateClass, gridBagConstraints);

        lblRateTypeCode.setFont(CoeusFontFactory.getLabelFont());
        lblRateTypeCode.setText("Calculation Base for Rate Type :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 23, 0);
        pnlRateClass.add(lblRateTypeCode, gridBagConstraints);

        cmbRateType.setMinimumSize(new java.awt.Dimension(130, 18));
        cmbRateType.setPreferredSize(new java.awt.Dimension(130, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 23, 20);
        pnlRateClass.add(cmbRateType, gridBagConstraints);

        pnlInclusionExclusion.setLayout(new java.awt.GridBagLayout());

        pnlInclusionExclusion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlInclusionExclusion.setMinimumSize(new java.awt.Dimension(590, 200));
        pnlInclusionExclusion.setPreferredSize(new java.awt.Dimension(590, 200));
        lblBaseInclusion.setFont(CoeusFontFactory.getLabelFont());
        lblBaseInclusion.setText("Included in Base :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlInclusionExclusion.add(lblBaseInclusion, gridBagConstraints);

        scrPnInclusion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrPnInclusion.setMinimumSize(new java.awt.Dimension(350, 160));
        scrPnInclusion.setPreferredSize(new java.awt.Dimension(350, 160));
        tblRateInclusion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"0", null},
                {"Lab Allocation - Salaries", null}
            },
            new String [] {
                "Rate Class Code", "Rate Type Code"
            }
        ));
        tblRateInclusion.setMinimumSize(new java.awt.Dimension(148, 16));
        tblRateInclusion.setPreferredSize(new java.awt.Dimension(148, 96));
        scrPnInclusion.setViewportView(tblRateInclusion);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 6);
        pnlInclusionExclusion.add(scrPnInclusion, gridBagConstraints);

        lblBaseExclusion.setFont(CoeusFontFactory.getLabelFont());
        lblBaseExclusion.setText("Excluded from Base :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        pnlInclusionExclusion.add(lblBaseExclusion, gridBagConstraints);

        scrPnExclusion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrPnExclusion.setMinimumSize(new java.awt.Dimension(350, 160));
        scrPnExclusion.setPreferredSize(new java.awt.Dimension(350, 160));
        tblRateExclusion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Lab Allocation - Salaries", "EB on LA"}
            },
            new String [] {
                "Rate Class Code", "Rate Type Code"
            }
        ));
        tblRateExclusion.setPreferredSize(new java.awt.Dimension(125, 96));
        scrPnExclusion.setViewportView(tblRateExclusion);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 0);
        pnlInclusionExclusion.add(scrPnExclusion, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlRateClass.add(pnlInclusionExclusion, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlMain.add(pnlRateClass, gridBagConstraints);

        getContentPane().add(pnlMain, new java.awt.GridBagConstraints());

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbRateClass;
    private javax.swing.JComboBox cmbRateType;
    private javax.swing.JLabel lblBaseExclusion;
    private javax.swing.JLabel lblBaseInclusion;
    private javax.swing.JLabel lblRateClassCode;
    private javax.swing.JLabel lblRateTypeCode;
    private javax.swing.JPanel pnlInclusionExclusion;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlRateClass;
    private javax.swing.JScrollPane scrPnExclusion;
    private javax.swing.JScrollPane scrPnInclusion;
    private javax.swing.JTable tblRateExclusion;
    private javax.swing.JTable tblRateInclusion;
    // End of variables declaration//GEN-END:variables
    
    
    /**
     * Get the rates data from the server
     */
    private void fetchRatesData() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_RATE_CLASS_TYPE_DATA);
        AppletServletCommunicator appletServletCommunicator = new
                AppletServletCommunicator(CONNECTION_STRING, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean.isSuccessfulResponse()) {
            Hashtable ratesData = (Hashtable)responderBean.getDataObject();
            cvRateClassList = (CoeusVector)ratesData.get(KeyConstants.RATE_CLASS_DATA);
            cvRateTypeList = (CoeusVector)ratesData.get(KeyConstants.RATE_TYPE_DATA);
            cvRateClassInclusionsList = (CoeusVector)ratesData.get("RATE_CLASS_INCLUSIONS");
            cvRateClassExclusionsList = (CoeusVector)ratesData.get("RATE_CLASS_EXCLUSIONS");
        }else {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return;
        }
    }
    
    public void actionPerformed(ActionEvent e) {
    }
    
    public void itemStateChanged(ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
        if(source.equals(cmbRateClass)){
            setRateTypeData();
        }
        if(source.equals(cmbRateType)){
            setInclusionTableData();
            setExclusionTableData();
        }
    }
    
    /**
     * Sets the Rate Table Data
     */
    public void setRateTypeData(){
        CoeusVector cvTemp = new CoeusVector();
        ComboBoxBean comboBean = new ComboBoxBean();
        comboBean = (ComboBoxBean)cmbRateClass.getSelectedItem();
        int code = Integer.parseInt(comboBean.getCode());
        Equals rate = new Equals(RATE_CLASS_CODE , new Integer(code));
        cvTemp.add(EMPTY_STRING);
        for(Object rateTypeList : cvRateTypeList.filter(rate)) {
            RateTypeBean rateTypeBean = (RateTypeBean)rateTypeList;
            cvTemp.add(rateTypeBean.getDescription());
        }
        cmbRateType.setModel(new DefaultComboBoxModel(cvTemp));
        
        setInclusionTableData();
        setExclusionTableData();
    }
    
}
