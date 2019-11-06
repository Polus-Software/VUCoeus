/*
 * @(#)RiskLevelForm.java 1.0 04/03/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.iacuc.bean.ProtocolRiskLevelBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author  leenababu
 */
public class RiskLevelForm extends CoeusDlgWindow implements ActionListener, ListSelectionListener {
    private int RISK_COL = 0;
    private int DATE_ASSIGNED_COL = 1;
    private int DATE_UPDATED_COL = 2;
    private int STATUS_COL = 3;
    private int COMMENTS_COL = 4;
    private int COMMENTS_ICON_COL = 5;
    
    private String protocolNumber;
    private CoeusVector cvActiveProtocolRiskLevels;
    private Vector vecRiskLevelCategories;
    private HashMap hmRiskCategory;
    
    private boolean saveRequired = false;
    private boolean okButtonClicked = false;
    private DateUtils dateUtils = new DateUtils();
    private ImageIcon imgIcnComments = new ImageIcon(getClass().getClassLoader().getResource(
            CoeusGuiConstants.JUSTIFIED));
    
    private final String PROTOCOL_SERVLET = "/IacucProtocolServlet";
    private static final char GET_RISK_LEVELS = '2';
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private String DELETE_RISK_LEVEL = "risklevel_exceptionCode.1001";
    
    /** Creates new form RiskLevelForm */
    public RiskLevelForm(String protocolNumber, boolean modal) {
        super(CoeusGuiConstants.getMDIForm(), modal);
        this.protocolNumber = protocolNumber;
        initComponents();
        postInitComponents();
    }
    
    /**
     * Set the properties, listeners to the gui components
     */
    public void postInitComponents(){
        Component[] components = {btnOk, btnCancel, btnAdd, btnModify, btnDelete};
        ScreenFocusTraversalPolicy screenFocusTraversalPolicy =
                new ScreenFocusTraversalPolicy(components);
        setFocusTraversalPolicy(screenFocusTraversalPolicy);
        setFocusCycleRoot(true);
        
        setTitle("Risk Levels");
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        btnModify.addActionListener(this);
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        
        RiskLevelCellEditor riskLevelCellEditor = new RiskLevelCellEditor();
        RiskLevelCellRenderer riskLevelCellRenderer = new RiskLevelCellRenderer();
        RiskLevelTableModel riskLevelTableModel = new RiskLevelTableModel();
        tblRiskCategory.setModel(riskLevelTableModel);
        
        TableColumn tableColumn = tblRiskCategory.getColumnModel().getColumn(RISK_COL);
        tableColumn.setPreferredWidth(150);
        tableColumn.setCellRenderer(riskLevelCellRenderer);
        
        tableColumn = tblRiskCategory.getColumnModel().getColumn(DATE_ASSIGNED_COL);
        tableColumn.setPreferredWidth(120);
        tableColumn.setCellRenderer(riskLevelCellRenderer);
        
        tableColumn = tblRiskCategory.getColumnModel().getColumn(DATE_UPDATED_COL);
        tableColumn.setPreferredWidth(120);
        tableColumn.setCellRenderer(riskLevelCellRenderer);
        
        tableColumn = tblRiskCategory.getColumnModel().getColumn(STATUS_COL);
        tableColumn.setPreferredWidth(70);
        tableColumn.setCellRenderer(riskLevelCellRenderer);
        
        tableColumn = tblRiskCategory.getColumnModel().getColumn(COMMENTS_COL);
        tableColumn.setPreferredWidth(150);
        
        tableColumn = tblRiskCategory.getColumnModel().getColumn(COMMENTS_ICON_COL);
        tableColumn.setMinWidth(20);
        tableColumn.setMaxWidth(20);
        tableColumn.setPreferredWidth(20);
        tableColumn.setCellRenderer(riskLevelCellRenderer);
        tableColumn.setCellEditor(riskLevelCellEditor);
        
        tblRiskCategory.getTableHeader().setReorderingAllowed(false);
        tblRiskCategory.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblRiskCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblRiskCategory.getSelectionModel().addListSelectionListener(this);
        tblRiskCategory.setRowHeight(20);
        
        setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCloseAction();
            }
        });
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCloseAction();
            }
        });
    }
    
    /**
     *Set the data to the form components
     */
    public void setFormData(CoeusVector cvProtRiskLevels ){
        if(cvProtRiskLevels == null){
            filterActiveRiskLevelData(getFormData());
        }else{
            Vector vecServerData = getFormData();
            if(vecServerData !=null && vecServerData.size()==2){
                vecRiskLevelCategories = (Vector)vecServerData.get(1);
                populateRiskLevelMap();
                cvActiveProtocolRiskLevels = cvProtRiskLevels;
            }
        }
        if(tblRiskCategory.getRowCount() > 0 ) {
            tblRiskCategory.setRowSelectionInterval(0, 0);
            tblRiskCategory.setColumnSelectionInterval(1,1);
        }else{
            btnDelete.setEnabled(false);
            btnModify.setEnabled(false);
        }
    }
    
    /**
     * Get the risk levels from the database
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
    
    /**
     * Populate the hashmap hmRiskCategory with the risk level categories
     * with code as key and value the description
     */
    public void populateRiskLevelMap(){
        hmRiskCategory = new HashMap();
        
        if(vecRiskLevelCategories != null){
            ComboBoxBean comboBoxBean = null;
            for(int i=0; i < vecRiskLevelCategories.size(); i++){
                comboBoxBean = (ComboBoxBean)vecRiskLevelCategories.get(i);
                hmRiskCategory.put(comboBoxBean.getCode(), comboBoxBean.getDescription());
            }
        }
        
    }
    
    /**
     * Filter out the active risk levels
     */
    public void filterActiveRiskLevelData(Vector vecServerData){
        if(vecServerData !=null && vecServerData.size()==2){
            CoeusVector cvProtocolRiskLevels = (CoeusVector)vecServerData.get(0);
            vecRiskLevelCategories = (Vector)vecServerData.get(1);
            populateRiskLevelMap();
            
            if(cvProtocolRiskLevels!=null){
                Equals equalsActive = new Equals("status", "A");
                cvActiveProtocolRiskLevels =
                        cvProtocolRiskLevels.filter(equalsActive);
            }
        }
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e){
        Object source =  e.getSource();
        if(source.equals(btnAdd)){
            AddRiskLevelForm addRiskLevelForm = new AddRiskLevelForm(TypeConstants.ADD_MODE, null, vecRiskLevelCategories);
            addRiskLevelForm.setExistingRiskLevels(cvActiveProtocolRiskLevels);
            addRiskLevelForm.display();
            if(addRiskLevelForm.isSaveRequired()){
                saveRequired = true;
                if(cvActiveProtocolRiskLevels == null){
                    cvActiveProtocolRiskLevels = new CoeusVector();
                }
                cvActiveProtocolRiskLevels.add(addRiskLevelForm.getProtocolRiskLevelBean());
                ((RiskLevelTableModel)tblRiskCategory.getModel()).fireTableDataChanged();
                if(tblRiskCategory.getRowCount()>0){
                    tblRiskCategory.setRowSelectionInterval(0, 0);
                    tblRiskCategory.setColumnSelectionInterval(1,1);
                }
            }
        }else if(source.equals(btnModify)){
            int selectedRow = tblRiskCategory.getSelectedRow();
            if(selectedRow!=-1){
                ProtocolRiskLevelBean protRiskLevelBean =
                        (ProtocolRiskLevelBean)cvActiveProtocolRiskLevels.get(selectedRow);
                AddRiskLevelForm addRiskLevelForm =
                        new AddRiskLevelForm(TypeConstants.MODIFY_MODE, protRiskLevelBean, vecRiskLevelCategories);
                addRiskLevelForm.setExistingRiskLevels(cvActiveProtocolRiskLevels);
                addRiskLevelForm.display();
                if(addRiskLevelForm.isSaveRequired()){
                    saveRequired = true;
                    ((RiskLevelTableModel)tblRiskCategory.getModel()).fireTableDataChanged();
                }
            }
        }else if(source.equals(btnDelete)){
            int selectedRow = tblRiskCategory.getSelectedRow();
            if(selectedRow != -1){
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(DELETE_RISK_LEVEL),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                if( selectedOption == JOptionPane.YES_OPTION ){
                    cvActiveProtocolRiskLevels.remove(selectedRow);
                    ((RiskLevelTableModel)tblRiskCategory.getModel()).fireTableDataChanged();
                }
            }
        }else if(source.equals(btnOk)){
            okButtonClicked = true;
            this.dispose();
        }if(source.equals(btnCancel)){
            performCloseAction();
        }
    }
    
    /**
     * Performs the operation while closing the window
     */
    public void performCloseAction(){
        if(saveRequired){
            int   option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_YES);
            if(option == JOptionPane.YES_OPTION){
                okButtonClicked = true;
                this.dispose();
            }else if(option == JOptionPane.NO_OPTION){
                saveRequired = false;
                this.dispose();
            }
        }else{
            this.dispose();
        }
    }
    
    public void valueChanged(ListSelectionEvent e) {
        int selectedRow = tblRiskCategory.getSelectedRow();
        if(selectedRow != -1){
            btnModify.setEnabled(true);
            ProtocolRiskLevelBean protRiskLevelBean = (ProtocolRiskLevelBean)cvActiveProtocolRiskLevels.get(selectedRow);
            if(protRiskLevelBean != null){
                if(protRiskLevelBean.getAcType() == null 
                        || protRiskLevelBean.getAcType().equals("") 
                        || protRiskLevelBean.getAcType().equals("U")){
                    btnDelete.setEnabled(false);
                }else{
                    btnDelete.setEnabled(true);
                }
            }
        }else{
            btnModify.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }
    
    /**
     * Displays the window
     */
    public void display(){
        setLocation(CoeusDlgWindow.CENTER);
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * This class is used as the table model for the table tblRiskCategory
     */
    public class RiskLevelTableModel extends AbstractTableModel{
        
        public String[] columnNames = {"Risk Level", "Date Assigned", "Date Updated", "Status", "Comments", ""};
        
        public void RiskLevelTableModel(){
        
        }
        public Object getValueAt(int row, int column) {
            ProtocolRiskLevelBean protocolRiskLevelBean =
                    (ProtocolRiskLevelBean)cvActiveProtocolRiskLevels.get(row);
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
            if(cvActiveProtocolRiskLevels!=null){
                return cvActiveProtocolRiskLevels.size();
            }
            return 0;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }
        
        public boolean isCellEditable(int row, int column){
            if(column == COMMENTS_ICON_COL){
                return true;
            }else{
                return false;
            }
        }
        public void setValueAt(Object value, int row, int column){
            
        }
    }
    
    /**
     * This class is used as the cell renderer for the table tblRiskCategory
     */
    public class RiskLevelCellRenderer extends DefaultTableCellRenderer{
        private JLabel lblText;
        private JButton btnComments;
        public RiskLevelCellRenderer(){
            lblText = new JLabel();
            btnComments = new JButton(imgIcnComments);
            lblText.setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
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
                }
            }else{
                lblText.setText("");
            }
            
            if(column == COMMENTS_ICON_COL){
                return btnComments;
            }
            
            if(isSelected){
                lblText.setBackground(table.getSelectionBackground());
                lblText.setForeground(Color.WHITE);
            }else{
                lblText.setBackground(table.getBackground());
                lblText.setForeground(Color.BLACK);
            }
            return lblText;
        }
        
    }
    
    /**
     * This class is used as the cell editor for the table tblRiskCategory
     */
    public class RiskLevelCellEditor extends DefaultCellEditor implements
            ActionListener{
        
        private JButton btnComments;
        private edu.mit.coeus.utils.CommentsForm commentsForm;
        public RiskLevelCellEditor(){
            super(new JComboBox());
            btnComments = new JButton(imgIcnComments);
            btnComments.addActionListener(this);
            
            commentsForm = new edu.mit.coeus.utils.CommentsForm("Comments");
        }
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return btnComments;
        }
        
        public void actionPerformed(ActionEvent e){
            int selectedRow = tblRiskCategory.getSelectedRow();
            if(selectedRow != -1){
                ProtocolRiskLevelBean protocolRiskLevelBean =
                        (ProtocolRiskLevelBean)cvActiveProtocolRiskLevels.get(selectedRow);
                commentsForm.setData(protocolRiskLevelBean.getComments());
                commentsForm.display();
                tblRiskCategory.getCellEditor().stopCellEditing();
            }
        }
    }
    
    public Vector getVecRiskLevelCategories() {
        return vecRiskLevelCategories;
    }
    
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    public CoeusVector getProtocolRiskLevels(){
        return cvActiveProtocolRiskLevels;
    }
    
    public boolean isOkButtonClicked() {
        return okButtonClicked;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        scrPnRiskLevels = new javax.swing.JScrollPane();
        tblRiskCategory = new javax.swing.JTable();
        btnAdd = new edu.mit.coeus.utils.CoeusButton();
        btnDelete = new edu.mit.coeus.utils.CoeusButton();
        btnModify = new edu.mit.coeus.utils.CoeusButton();
        btnCancel = new edu.mit.coeus.utils.CoeusButton();
        btnOk = new edu.mit.coeus.utils.CoeusButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Assign Risk Levels");
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Risk Levels", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        jPanel1.setMaximumSize(new java.awt.Dimension(600, 300));
        jPanel1.setMinimumSize(new java.awt.Dimension(600, 300));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 300));
        scrPnRiskLevels.setMaximumSize(new java.awt.Dimension(580, 280));
        scrPnRiskLevels.setMinimumSize(new java.awt.Dimension(580, 280));
        scrPnRiskLevels.setPreferredSize(new java.awt.Dimension(580, 280));
        scrPnRiskLevels.setViewportView(tblRiskCategory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(scrPnRiskLevels, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        getContentPane().add(jPanel1, gridBagConstraints);

        btnAdd.setMnemonic('a');
        btnAdd.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 5);
        getContentPane().add(btnAdd, gridBagConstraints);

        btnDelete.setMnemonic('d');
        btnDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        getContentPane().add(btnDelete, gridBagConstraints);

        btnModify.setMnemonic('m');
        btnModify.setText("Modify");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        getContentPane().add(btnModify, gridBagConstraints);

        btnCancel.setMnemonic('c');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        getContentPane().add(btnCancel, gridBagConstraints);

        btnOk.setMnemonic('o');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        getContentPane().add(btnOk, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.utils.CoeusButton btnAdd;
    public edu.mit.coeus.utils.CoeusButton btnCancel;
    public edu.mit.coeus.utils.CoeusButton btnDelete;
    public edu.mit.coeus.utils.CoeusButton btnModify;
    public edu.mit.coeus.utils.CoeusButton btnOk;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JScrollPane scrPnRiskLevels;
    public javax.swing.JTable tblRiskCategory;
    // End of variables declaration//GEN-END:variables
    
}
