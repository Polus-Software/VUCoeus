/*
 * SubAwardCostSharingForm.java
 *
 * Created on July 26, 2011, 2:14 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
 

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.budget.bean.BudgetSubAwardDetailBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.organization.gui.EmptyHeaderRenderer;
import edu.mit.coeus.organization.gui.IconRenderer;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author  maharajap
 */
public class SubAwardCostSharingForm extends javax.swing.JPanel {
    
    /** CoeusDlgWindow instance */
    public CoeusDlgWindow dlgSubAwardCostSharingDistributionForm;
    /** variable for modal */
    boolean modal = true;
    /** ParentForm instance of this Dialog */
    Component parentForm;
    /** String for title */
    private String title = "";
    
    private final int ORGANIZATION_HAND_ICON_COL = 0;
    private final int ORGANIZATION_NAME_COL = 1;
    private final int ORGANIZATION_SUBAWARD_COL = 2;
    
    private final int ORGANIZATION_HAND_ICON_WIDTH = 25;
    private final int ORGANIZATION_NAME_COL_WIDTH = 150;
    
    private final int COST_SHARING_BUDGET_PERIOD_COL = 0;
    private final int COST_SHARING_PROJECT_YEAR_COL = 1;
    private final int COST_SHARING_AMOUNT_COL = 2;
    
    private final int COST_SHARING_BUDGET_PERIOD_COL_WIDTH = 50;
    private final int COST_SHARING_PROJECT_YEAR_COL_WIDTH = 70;
    private final int COST_SHARING_AMOUNT_COL_WIDTH = 100;
    
    
    private OrganizationTableModel organizationTableModel;
    private SubAwardCostSharingModel subAwardCostSharingModel;
    
    private OrganizationRenderer organizationRenderer;
    private CostSharingRenderer costSharingRenderer;
    
    private HashMap hmCostSharingData;
    private CoeusVector vecSubAwardOrganizationList;
    private CoeusVector cvSubAwardCostSharingList;
    private java.awt.Color bgPanelColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
    
    /** Creates new form subAwardCostSharingForm */
    public SubAwardCostSharingForm() {
        this.parentForm = CoeusGuiConstants.getMDIForm();
        this.modal = true;
        initComponents();
        postInitComponent();
    }
    
    /** post intialisation of CoeusDialog */
    public void postInitComponent(){
        
        dlgSubAwardCostSharingDistributionForm = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),title,modal);
        dlgSubAwardCostSharingDistributionForm.getContentPane().add(this);
        dlgSubAwardCostSharingDistributionForm.pack();
        dlgSubAwardCostSharingDistributionForm.setResizable(false);
        dlgSubAwardCostSharingDistributionForm.setFont(CoeusFontFactory.getLabelFont());
        dlgSubAwardCostSharingDistributionForm.setVisible(false);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        dlgSubAwardCostSharingDistributionForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
    }
    
    /**
     * Sets the column, model and other properties for the table.
     */
    public void setTableProperties(){
        organizationRenderer = new OrganizationRenderer();
        costSharingRenderer = new CostSharingRenderer();
        tblOrganizations.setSelectionBackground(bgPanelColor );
        tblOrganizations.setSelectionForeground(Color.black);
        tblOrganizations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
        tblOrganizations.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblOrganizations.getTableHeader().setPreferredSize(new Dimension(0,22));
        
        tblSubAwardCostSharing.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblOrganizations.getTableHeader().setPreferredSize(new Dimension(0,22));
        
        organizationTableModel = new OrganizationTableModel();
        tblOrganizations.setModel(organizationTableModel);
        
        TableColumn tableColumn = tblOrganizations.getColumnModel().getColumn(ORGANIZATION_HAND_ICON_COL);
        tableColumn.setHeaderRenderer(new EmptyHeaderRenderer());
        tableColumn.setPreferredWidth(ORGANIZATION_HAND_ICON_WIDTH);
        tableColumn.setMaxWidth(ORGANIZATION_HAND_ICON_WIDTH);
        tableColumn.setMinWidth(ORGANIZATION_HAND_ICON_WIDTH);
        tableColumn.setCellRenderer(organizationRenderer);
        
        tableColumn = tblOrganizations.getColumnModel().getColumn(ORGANIZATION_NAME_COL);
        tableColumn.setPreferredWidth(ORGANIZATION_NAME_COL_WIDTH);
        tableColumn.setMinWidth(ORGANIZATION_NAME_COL_WIDTH);
        tableColumn.setCellRenderer(organizationRenderer);
        
        subAwardCostSharingModel = new SubAwardCostSharingModel();
        tblSubAwardCostSharing.setModel(subAwardCostSharingModel);
   
        tableColumn = tblSubAwardCostSharing.getColumnModel().getColumn(COST_SHARING_BUDGET_PERIOD_COL);
        tableColumn.setPreferredWidth(COST_SHARING_BUDGET_PERIOD_COL_WIDTH);
        tableColumn.setMinWidth(COST_SHARING_BUDGET_PERIOD_COL_WIDTH);
        tableColumn.setCellRenderer(costSharingRenderer);
        
        tableColumn = tblSubAwardCostSharing.getColumnModel().getColumn(COST_SHARING_PROJECT_YEAR_COL);
        tableColumn.setPreferredWidth(COST_SHARING_PROJECT_YEAR_COL_WIDTH);
        tableColumn.setMinWidth(COST_SHARING_PROJECT_YEAR_COL_WIDTH);
        tableColumn.setCellRenderer(costSharingRenderer);
        
        tableColumn = tblSubAwardCostSharing.getColumnModel().getColumn(COST_SHARING_AMOUNT_COL);
        tableColumn.setPreferredWidth(COST_SHARING_AMOUNT_COL_WIDTH);
        tableColumn.setMinWidth(COST_SHARING_AMOUNT_COL_WIDTH);
        tableColumn.setCellRenderer(costSharingRenderer);
        
        tblOrganizations.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() ==1){
                    //invoke Organization display
                    int selectedOrganizationRow = tblOrganizations.getSelectedRow();
                    if(selectedOrganizationRow !=-1){
                        BudgetSubAwardDetailBean budgetSubAwardDetailBean = (BudgetSubAwardDetailBean)vecSubAwardOrganizationList.get(selectedOrganizationRow);
                        int subAwardNumber = budgetSubAwardDetailBean.getSubAwardNumber();
                        cvSubAwardCostSharingList = (CoeusVector)hmCostSharingData.get(subAwardNumber);
                        subAwardCostSharingModel.fireTableDataChanged();
                    }
                }
            }
        });
    }
    
    /**
     * Sets the form data.
     */
    public void setFormData(CoeusVector vecSubAwardOrganizationList, HashMap hmCostSharingData){
        this.vecSubAwardOrganizationList = vecSubAwardOrganizationList;
        this.hmCostSharingData = hmCostSharingData;
        setTableProperties();
        if(tblOrganizations.getRowCount() > 0){
            tblOrganizations.setRowSelectionInterval(0,0);
            if(vecSubAwardOrganizationList!=null && vecSubAwardOrganizationList.size()>0){
                BudgetSubAwardDetailBean budgetSubAwardDetailBean = (BudgetSubAwardDetailBean)vecSubAwardOrganizationList.get(0);
                int subAwardNumber = budgetSubAwardDetailBean.getSubAwardNumber();
                cvSubAwardCostSharingList = (CoeusVector)hmCostSharingData.get(subAwardNumber);
                subAwardCostSharingModel.fireTableDataChanged();
            }
        }
        display();
    }    
    
    /**
     * Sets the title for the window.
     */
    public void setTitle(String proposalNumber, int versionNumber){
        dlgSubAwardCostSharingDistributionForm.setTitle(CoeusGuiConstants.SUB_AWARD_COST_SHARING_TITLE+proposalNumber+", Version"+versionNumber);
    }
    
    /**
     * To display the cost sharing distribution window.
     */
    public void display(){
        dlgSubAwardCostSharingDistributionForm.setVisible(true);
    }
    
    /**
     * To dispose the cost sharing window.
     */
    public void close(){
        dlgSubAwardCostSharingDistributionForm.dispose();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOrganizations = new javax.swing.JTable();
        lblCostSharingDistributionList = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSubAwardCostSharing = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Organization", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        jPanel1.setMaximumSize(new java.awt.Dimension(375, 200));
        jPanel1.setMinimumSize(new java.awt.Dimension(375, 200));
        jPanel1.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(310, 135));
        jScrollPane1.setOpaque(false);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(310, 135));
        tblOrganizations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblOrganizations.setShowHorizontalLines(false);
        tblOrganizations.setShowVerticalLines(false);
        jScrollPane1.setViewportView(tblOrganizations);

        jPanel1.add(jScrollPane1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jPanel1, gridBagConstraints);
        jPanel1.getAccessibleContext().setAccessibleDescription("");

        lblCostSharingDistributionList.setFont(CoeusFontFactory.getLabelFont());
        lblCostSharingDistributionList.setText("Sub Award Cost Sharing List:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblCostSharingDistributionList, gridBagConstraints);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(320, 150));
        jScrollPane2.setOpaque(false);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(320, 150));
        jScrollPane2.setViewport(null);
        tblSubAwardCostSharing.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblSubAwardCostSharing.setShowHorizontalLines(false);
        tblSubAwardCostSharing.setShowVerticalLines(false);
        jScrollPane2.setViewportView(tblSubAwardCostSharing);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(jScrollPane2, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(83, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(83, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(83, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 2, 0, 4);
        add(btnClose, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    public class OrganizationRenderer extends DefaultTableCellRenderer{
        private JLabel lblOrganization;
        private JLabel lblHandIcon;
        /* Hand icon which is used to diplay in the rendered column of selected row */
        private ImageIcon handIcon ;
        
        /* Empty icon i.e null for all rows which are not selected */
        private ImageIcon emptyIcon;
        
        public OrganizationRenderer(){
            lblOrganization = new JLabel();
            lblHandIcon = new JLabel();
            handIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.HAND_ICON));
            lblOrganization.setOpaque(true);
            lblHandIcon.setOpaque(true);
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(column == ORGANIZATION_HAND_ICON_COL){
                if( isSelected ){
                    lblHandIcon.setIcon(handIcon);
                    lblHandIcon.setBackground(bgPanelColor);
                }else{
                    lblHandIcon.setIcon(emptyIcon);
                    lblHandIcon.setBackground(bgPanelColor);
                }
                return lblHandIcon;
            }else{
                lblOrganization.setText(value.toString());
                lblOrganization.setBackground(bgPanelColor);
                lblOrganization.setForeground(Color.black);
                return lblOrganization;
            }
        }
    }    
    
    /**
     * This class is used as table model for the table tblLocation
     */
    public class OrganizationTableModel extends AbstractTableModel{
        private String[] columnNames = {"","Organization Name"};
        
        public Object getValueAt(int row, int col){
            BudgetSubAwardDetailBean budgetSubAwardDetailBean = (BudgetSubAwardDetailBean)vecSubAwardOrganizationList.get(row);
            if(col== ORGANIZATION_NAME_COL){
                if(budgetSubAwardDetailBean.getOrganizationName() != null){
                    return budgetSubAwardDetailBean.getOrganizationName();
                }else{
                    return CoeusGuiConstants.EMPTY_STRING;
                }
            }else if(col == ORGANIZATION_SUBAWARD_COL){
                return budgetSubAwardDetailBean.getSubAwardNumber();
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return vecSubAwardOrganizationList.size();
        }
        
        public String getColumnName(int col){
            return columnNames[col];
        }
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
    }
    
    /**
     * This class is used as the table model for the table tblSubAwardCostSharing
     */
     public class SubAwardCostSharingModel extends AbstractTableModel{
        private String[] columnNames = {"Period","Project Year","Amount"};
        
        public Object getValueAt(int row, int col){
            BudgetSubAwardDetailBean budgetSubAwardDetailBean = (BudgetSubAwardDetailBean) cvSubAwardCostSharingList.get(row);
            
            if(col == COST_SHARING_BUDGET_PERIOD_COL){
                return budgetSubAwardDetailBean.getBudgetPeriod();
            } else if(col == COST_SHARING_PROJECT_YEAR_COL){
                if(budgetSubAwardDetailBean.getPeriodStartDate()!=null){
                    StringBuffer periodYear = new StringBuffer(budgetSubAwardDetailBean.getPeriodStartDate().toString());
                    periodYear = new StringBuffer(periodYear.substring(0,periodYear.indexOf("-")));
                    if(periodYear != null){
                        return periodYear;
                    }else{
                        return CoeusGuiConstants.EMPTY_STRING;
                    }
                }
            } else if(col == COST_SHARING_AMOUNT_COL){
                return new Double(budgetSubAwardDetailBean.getCostSharingAmount());
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if(cvSubAwardCostSharingList !=null){
                return cvSubAwardCostSharingList.size();
            }
            return 0;
        }
        
        public String getColumnName(int col){
            return columnNames[col];
        }
        
        public boolean isCellEditable(int row, int column){
            return false;
        }
    }
     
     public class CostSharingRenderer extends DefaultTableCellRenderer{
        private JLabel lblOrganization;
        private DollarCurrencyTextField dollarCurrencyTextField;
        
        public CostSharingRenderer(){
            lblOrganization = new JLabel();
            lblOrganization.setOpaque(true);
            
            dollarCurrencyTextField = new DollarCurrencyTextField(12,JTextField.RIGHT,true);
            dollarCurrencyTextField.setHorizontalAlignment(DollarCurrencyTextField.RIGHT);
            dollarCurrencyTextField.setBackground(bgPanelColor);
            dollarCurrencyTextField.setBorder(null);
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(column == COST_SHARING_AMOUNT_COL){
                dollarCurrencyTextField.setValue(new Double(value.toString()).doubleValue());
                return dollarCurrencyTextField;
            }else{
                lblOrganization.setText(value.toString());
                return lblOrganization;
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel lblCostSharingDistributionList;
    public javax.swing.JTable tblOrganizations;
    public javax.swing.JTable tblSubAwardCostSharing;
    // End of variables declaration//GEN-END:variables
    
}