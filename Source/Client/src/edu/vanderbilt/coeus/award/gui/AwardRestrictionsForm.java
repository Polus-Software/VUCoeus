package edu.vanderbilt.coeus.award.gui;

/**
 * AwardRestrictionsForm.java
 * 
 * Award restrictions gui
 *
 * @created	September 29, 2014
 * @author	Jill McAfee, Vanderbilt University
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.GridBagConstraints;
import java.util.Vector;

import edu.vanderbilt.coeus.gui.CoeusToolTip;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;


public class AwardRestrictionsForm extends javax.swing.JComponent {
	
    public JButton btnAdd;
    public JButton btnDelete;
    public JScrollPane jcrPnRestrictions, jcrComments;
    public JPanel pnlButtons, pnlRestrictions, pnlLabel;
    public JTable tblRestrictions;
    public JComboBox cmbStatus, cmbRestriction, cmbMaintainers;
    public JTextArea txtComments;
    public JLabel lblComments;

    public Vector vecRestrictionCodes, vecRestrictionMaintainers;

    public final int SEQUENCE = 0;
    public final int RESTRICTION_DESC = 1;
    public final int DUE_DATE = 2;
    public final int ACTION_DATE = 3;
    public final int STATUS = 4;
    public final int ASSIGNED_USER_NAME = 5;
    public final int ASSIGNED_USER = 6;
    public final int RESTRICTION_TYPE_CODE = 7;
    public final int RESTRICTION_NUMBER = 8;
    public final int COMMENTS = 9;
    
	private static final int PANE_WIDTH = 730;
	private static final int PANE_HEIGHT = 680;
	
	private static final int SCROLL_PANE_WIDTH = 700;
	private static final int SCROLL_PANE_HEIGHT = 330;
	private static final int COMMENTS_SCROLL_PANE_HEIGHT = 230;
	private static final int LABEL_HEIGHT = 30;
	private static final int LABEL_PANEL_HEIGHT = 34;

	private static final int TXTAREA_HEIGHT = 210;
	private static final int TXTAREA_WIDTH = 680;
	
	private static final String ADD_BUTTON = "Add";
	private static final String DELETE_BUTTON = "Delete";
	private static final int BUTTON_WIDTH = 90;
	
    private static final int SEQUENCE_COLUMN_WIDTH = 50;
    private static final int RESTRICTION_DESC_COLUMN_WIDTH = 160;
    private static final int DATE_COLUMN_WIDTH = 100;
    private static final int STATUS_COLUMN_WIDTH = 100;
    private static final int ASSIGNED_USER_NAME_COLUMN_WIDTH = 160;
    private static final int HIDDEN_COLUMN_WIDTH = 0;
    
    public boolean saveRequired;
	
    
    public AwardRestrictionsForm() {
        initComponents();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlButtons = new JPanel();
        pnlLabel = new JPanel();
        btnAdd = new JButton();
        btnDelete = new JButton();
        jcrPnRestrictions = new JScrollPane();
	    jcrComments = new JScrollPane();
        pnlRestrictions = new JPanel();
        lblComments = new JLabel("Comments: ");
        txtComments = new JTextArea();
        cmbStatus = new JComboBox();
        cmbStatus.setFont(CoeusFontFactory.getNormalFont());
        cmbRestriction = new JComboBox();
        cmbRestriction.setFont(CoeusFontFactory.getNormalFont());
        cmbMaintainers = new JComboBox();
        cmbMaintainers.setFont(CoeusFontFactory.getNormalFont());
        
        tblRestrictions = new JTable(){
        	@Override
        	public TableCellEditor getCellEditor(int row, int column) {
        	   Object value = super.getValueAt(row, column);
        	   if (value != null) {
        		   if (column == 1) {
        			   JComboBox cmb = createRestrictionsComboBox();
        			   cmb.getModel().setSelectedItem(value.toString());
        			   return new DefaultCellEditor(cmb);
        		   }
        		   else if (column == STATUS) {
        			   JComboBox cmb = createStatusComboBox();
        			   cmb.getModel().setSelectedItem(value.toString());
        			   return new DefaultCellEditor(cmb);
        		   }
        		   else if (column == ASSIGNED_USER_NAME) {
        			   JComboBox cmb = createMaintainersComboBox();
        			   cmb.getModel().setSelectedItem(value.toString());
        			   return new DefaultCellEditor(cmb);
        		   }
        		   else {
        			   return getDefaultEditor(value.getClass());
        		   }
        	   	}
        	   	return super.getCellEditor(row, column);
    		}
        };
        

	    setRequestFocusEnabled(false);
	    setLayout(new java.awt.GridBagLayout());
	    
	    /* Buttons panel */
	    pnlButtons.setLayout(new java.awt.GridBagLayout());
	
	    btnAdd.setFont(CoeusFontFactory.getLabelFont());
	    btnAdd.setMnemonic('A');
	    btnAdd.setText(ADD_BUTTON);
	    btnAdd.setMaximumSize(new Dimension(BUTTON_WIDTH, 26));
	    btnAdd.setMinimumSize(new Dimension(BUTTON_WIDTH, 26));
	    btnAdd.setPreferredSize(new Dimension(BUTTON_WIDTH, 26));
	    gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new Insets(0, 0, 4, 0);
	    gridBagConstraints.weightx = 1.0;
	    pnlButtons.add(btnAdd, gridBagConstraints);
	
	    btnDelete.setFont(CoeusFontFactory.getLabelFont());
	    btnDelete.setMnemonic('D');
	    btnDelete.setText(DELETE_BUTTON);
	    btnDelete.setMaximumSize(new Dimension(BUTTON_WIDTH, 26));
	    btnDelete.setMinimumSize(new Dimension(BUTTON_WIDTH, 26));
	    btnDelete.setPreferredSize(new Dimension(BUTTON_WIDTH, 26));
	    gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 1;
	    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
	    gridBagConstraints.insets = new Insets(0, 0, 4, 0);
	    gridBagConstraints.weightx = 1.0;
	    pnlButtons.add(btnDelete, gridBagConstraints);
	
	    gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = 1;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
	    gridBagConstraints.weightx = 0.4;
	    gridBagConstraints.weighty = 1.0;
	    gridBagConstraints.insets = new Insets(10, 10, 10, 10);
	    add(pnlButtons, gridBagConstraints);
	    
	    /* Comments */
	    txtComments.setMaximumSize(new Dimension(TXTAREA_WIDTH, TXTAREA_HEIGHT));
	    txtComments.setMinimumSize(new Dimension(TXTAREA_WIDTH, TXTAREA_HEIGHT));
	    txtComments.setPreferredSize(new Dimension(TXTAREA_WIDTH, TXTAREA_HEIGHT));
	    txtComments.setLineWrap(true);
	    txtComments.setWrapStyleWord(true);
        
	    jcrComments.setMaximumSize(new Dimension(SCROLL_PANE_WIDTH, COMMENTS_SCROLL_PANE_HEIGHT));
	    jcrComments.setMinimumSize(new Dimension(SCROLL_PANE_WIDTH, COMMENTS_SCROLL_PANE_HEIGHT));
	    jcrComments.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, COMMENTS_SCROLL_PANE_HEIGHT));
        jcrComments.setViewportView(txtComments);
	    
	    pnlRestrictions.setBorder(BorderFactory.createTitledBorder(null, "Restrictions", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 11)));
	    pnlRestrictions.setMaximumSize(new Dimension(PANE_WIDTH, PANE_HEIGHT));
	    pnlRestrictions.setMinimumSize(new Dimension(PANE_WIDTH, PANE_HEIGHT));
	    pnlRestrictions.setPreferredSize(new Dimension(PANE_WIDTH, PANE_HEIGHT));
	    
	    jcrPnRestrictions.setMaximumSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
	    jcrPnRestrictions.setMinimumSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
	    jcrPnRestrictions.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, SCROLL_PANE_HEIGHT));
	    jcrPnRestrictions.setViewportView(tblRestrictions);
	    
	    gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
	    pnlRestrictions.add(jcrPnRestrictions);
	    
	    lblComments.setFont(CoeusFontFactory.getLabelFont());
	    lblComments.setHorizontalAlignment(SwingConstants.LEFT);
	    lblComments.setVerticalAlignment(SwingConstants.BOTTOM);
	    lblComments.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, LABEL_HEIGHT));
	    pnlLabel.setMaximumSize(new Dimension(SCROLL_PANE_WIDTH, LABEL_PANEL_HEIGHT));
	    pnlLabel.setMinimumSize(new Dimension(SCROLL_PANE_WIDTH, LABEL_PANEL_HEIGHT));
	    pnlLabel.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, LABEL_PANEL_HEIGHT));
	    pnlLabel.add(lblComments);
	    
	    gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 1;
	    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
	    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
	    pnlRestrictions.add(pnlLabel);
	    
	    gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 2;
	    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
	    pnlRestrictions.add(jcrComments);
		
	    gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
	    add(pnlRestrictions,gridBagConstraints);
	    
    }

    public void formatTable() {
        JTableHeader tableHeader = tblRestrictions.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        tblRestrictions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblRestrictions.setRowHeight(24);
        tblRestrictions.setShowHorizontalLines(false);
        tblRestrictions.setShowVerticalLines(false);
        tblRestrictions.setOpaque(false);
        tblRestrictions.setBackground(Color.WHITE);
        tblRestrictions.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
        
        TableColumn column = tblRestrictions.getColumnModel().getColumn(SEQUENCE);
        column.setMaxWidth(SEQUENCE_COLUMN_WIDTH);
        column.setMinWidth(SEQUENCE_COLUMN_WIDTH);
        column.setPreferredWidth(SEQUENCE_COLUMN_WIDTH);
        column.setResizable(false);

        column = tblRestrictions.getColumnModel().getColumn(RESTRICTION_DESC);
        column.setMinWidth(RESTRICTION_DESC_COLUMN_WIDTH);
        column.setPreferredWidth(RESTRICTION_DESC_COLUMN_WIDTH);
        column.setResizable(false);
        cmbRestriction = createRestrictionsComboBox();
        column.setCellEditor(new DefaultCellEditor(cmbRestriction));
        
        column = tblRestrictions.getColumnModel().getColumn(DUE_DATE);
        column.setMinWidth(DATE_COLUMN_WIDTH);
        column.setPreferredWidth(DATE_COLUMN_WIDTH);
        column.setResizable(true);
        column.setCellEditor(new DateEditor());
        
        column = tblRestrictions.getColumnModel().getColumn(ACTION_DATE);
        column.setMinWidth(DATE_COLUMN_WIDTH);
        column.setPreferredWidth(DATE_COLUMN_WIDTH);
        column.setResizable(true);
        column.setCellEditor(new DateEditor());
        
        column = tblRestrictions.getColumnModel().getColumn(STATUS);
        column.setMaxWidth(STATUS_COLUMN_WIDTH);
        column.setMinWidth(STATUS_COLUMN_WIDTH);
        column.setPreferredWidth(STATUS_COLUMN_WIDTH);
        column.setResizable(false);
        cmbStatus = createStatusComboBox();
        column.setCellEditor(new DefaultCellEditor(cmbStatus));
        
        column = tblRestrictions.getColumnModel().getColumn(ASSIGNED_USER_NAME);
        column.setMaxWidth(ASSIGNED_USER_NAME_COLUMN_WIDTH);
        column.setMinWidth(ASSIGNED_USER_NAME_COLUMN_WIDTH);
        column.setPreferredWidth(ASSIGNED_USER_NAME_COLUMN_WIDTH);
        column.setResizable(false);
        cmbMaintainers = createMaintainersComboBox();
        column.setCellEditor(new DefaultCellEditor(cmbMaintainers));
        
        column = tblRestrictions.getColumnModel().getColumn(ASSIGNED_USER);
        column.setMaxWidth(HIDDEN_COLUMN_WIDTH);
        column.setMinWidth(HIDDEN_COLUMN_WIDTH);
        column.setPreferredWidth(HIDDEN_COLUMN_WIDTH);
        column.setResizable(false);
        
        column = tblRestrictions.getColumnModel().getColumn(RESTRICTION_TYPE_CODE);
        column.setMaxWidth(HIDDEN_COLUMN_WIDTH);
        column.setMinWidth(HIDDEN_COLUMN_WIDTH);
        column.setPreferredWidth(HIDDEN_COLUMN_WIDTH);
        column.setResizable(false);
        
        column = tblRestrictions.getColumnModel().getColumn(RESTRICTION_NUMBER);
        column.setMaxWidth(HIDDEN_COLUMN_WIDTH);
        column.setMinWidth(HIDDEN_COLUMN_WIDTH);
        column.setPreferredWidth(HIDDEN_COLUMN_WIDTH);
        column.setResizable(false);
        
        column = tblRestrictions.getColumnModel().getColumn(COMMENTS);
        column.setMaxWidth(HIDDEN_COLUMN_WIDTH);
        column.setMinWidth(HIDDEN_COLUMN_WIDTH);
        column.setPreferredWidth(HIDDEN_COLUMN_WIDTH);
        column.setResizable(false);
        
        validate();
        updateUI();
    }
    
    
    /* Create restrictions combo box */
	public JComboBox createRestrictionsComboBox() {
		JComboBox comboBox = new JComboBox();
		ComboBoxBean bean = new ComboBoxBean();
	    for (int v=0; v < vecRestrictionCodes.size(); v++) {
	    	bean = (ComboBoxBean) vecRestrictionCodes.get(v);
	    	comboBox.addItem(bean);
	    }
	    return comboBox;
	}
	
	/* Create maintainers combo box */
	public JComboBox createMaintainersComboBox() {
		JComboBox comboBox = new JComboBox();
		ComboBoxBean bean = new ComboBoxBean();
	    for (int v=0; v < vecRestrictionMaintainers.size(); v++) {
	    	bean = (ComboBoxBean) vecRestrictionMaintainers.get(v);
	    	comboBox.addItem(bean);
	    }
	    return comboBox;
	}
	
	/* Create status combo box */
	public JComboBox createStatusComboBox() {
		String[] statuses = { "Pending", "Lifted" };
		JComboBox comboBox = new JComboBox(statuses);
	    return comboBox;
	}
	
    /*
     * Inner class to set the editor for date columns/cells.
     */
    class DateEditor extends AbstractCellEditor implements TableCellEditor, MouseListener {
        
        private String colName;
        private static final String DATE_SEPARATERS = ":/.,|-";
        private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
        private CoeusTextField dateComponent = new CoeusTextField();
        private String stDateValue;
        private int selectedRow;
        private int selectedColumn;
        boolean temporary;
        
        DateEditor() {
            ((JTextField)dateComponent).setFont(CoeusFontFactory.getNormalFont());
            
            dateComponent.addMouseListener(this);
            dateComponent.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent fe) {
                    temporary = false;
                }
                public void focusLost(FocusEvent fe) {
                    if (!fe.isTemporary()) {
                        if(!temporary) {
                            stopCellEditing();
                        }
                    }
                }
            });
        }
        
        private void validateEditorComponent(){
            temporary = true;
            String formattedDate = null;
            String editingValue = (String) getCellEditorValue();
            if (editingValue != null && editingValue.trim().length() > 0) {
                /* Validate date field */
                formattedDate = new DateUtils().formatDate(editingValue,
                        DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                if (formattedDate == null) {
                    formattedDate = new DateUtils().restoreDate(editingValue, DATE_SEPARATERS);
                    if (formattedDate == null || formattedDate.equals(editingValue)) {
                        CoeusOptionPane.showErrorDialog("Please enter valid date");
                        dateComponent.setText(stDateValue);
                    }
                }
                else {
                    dateComponent.setText(formattedDate);
                    if (!editingValue.equals(stDateValue)) {
                        setModel(formattedDate);
                    }
                }
            }
            if (((editingValue == null ) || (editingValue.trim().length()== 0 )) &&
                    (stDateValue != null) && (stDateValue.trim().length()>= 0 )) {
                saveRequired = true;
                setModel(null);
            }
        }
        

        private void setModel(String formatDate) {
        }

        public Component getTableCellEditorComponent(JTable table,Object value,
                boolean isSelected,int row,int column) {
            JTextField tfield = (JTextField) dateComponent;
            tfield.setText((String)value);
            dateComponent = (CoeusTextField) tfield;
            return dateComponent;
        }
        
        public boolean stopCellEditing() {
            validateEditorComponent();
            return super.stopCellEditing();
        }
        

        public Object getCellEditorValue() {
            return ((JTextField)dateComponent).getText();
        }
        
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
        
        public int getClickCountToStart(){
            return 1;
        }
        
        public void mouseClicked(MouseEvent mouseEvent) {
        }
        
        public void mouseEntered(MouseEvent e) {
        }
        
        public void mouseExited(MouseEvent e) {
        }
        
        public void mousePressed(MouseEvent e) {
        }
        
        public void mouseReleased(MouseEvent e) {
        }
        
    }
}

