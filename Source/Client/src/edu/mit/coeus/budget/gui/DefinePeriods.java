/*
 * DefinePeriods.java
 *
 * Created on September 5, 2003, 9:45 AM
 */

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.CoeusGuiConstants;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.table.TableCellRenderer;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author  chandrashekara
 */
public class DefinePeriods extends javax.swing.JComponent {//implements ActionListener{
    
    private boolean modal;
    private Frame parent;
    private JDialog dlgDefinePeriods;
    private static final int WIDTH = 405;
    private static final int HEIGHT = 320;
    
    /** Creates new form DefinePeriods */
    public DefinePeriods(Frame parent, boolean modal) {
        this.parent = parent;
        this.modal = modal;
        initComponents();
        postInitComponents();
//        setupListener();
//        setTableEditors();      // Sets the Table Editors
//        setFormData();          // sets the form data and table initial values
//        setInitialValues();
        
    }
    
    public DefinePeriods(){
    initComponents();
    postInitComponents();
    }
   private void postInitComponents(){

        dlgDefinePeriods = new CoeusDlgWindow(parent,modal);
        dlgDefinePeriods.getContentPane().add(this);
        dlgDefinePeriods.pack();
        dlgDefinePeriods.setResizable(false);
        dlgDefinePeriods.setTitle("Define Periods");
        dlgDefinePeriods.setFont(CoeusFontFactory.getLabelFont());
        dlgDefinePeriods.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgDefinePeriods.getSize();
        dlgDefinePeriods.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgDefinePeriods.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke){
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
                    dlgDefinePeriods.dispose();
                }
            }
        });

    }
   
   public void display(){
   dlgDefinePeriods.show();
   }
   
//    public void setupListener(){
//        
//        btnOk.addActionListener(this);
//        btnCancel.addActionListener(this);
//        btnDefault.addActionListener(this);
//        btnAdd.addActionListener(this);
//        btnInsert.addActionListener(this);
//        btnDelete.addActionListener(this);
//    }
//    
//    public void actionPerformed(ActionEvent ae){
//  
//        Object source = ae.getSource();
//        if(source==btnAdd){
//        
//        }
//  }
//    
//    private void setTableEditors()throws Exception{
//        
//        TableColumn column = tblPeriod.getColumnModel().getColumn(0);
//        column.setMinWidth(25);
//        column.setMaxWidth(25);
//        column.setHeaderRenderer(new EmptyHeaderRenderer());
//        column.setCellRenderer(new IconRenderer());
//        column.setPreferredWidth(25);
//        
//        JTableHeader header = tblPeriod.getTableHeader();
//        header.setResizingAllowed(true);
//        header.setFont(CoeusFontFactory.getLabelFont());
//        header.setReorderingAllowed(false);
//        tblPeriod.setRowHeight(24);
//        
//        column = tblPeriod.getColumnModel().getColumn(1);
//        column.setMinWidth(25);
//        column.setMaxWidth(450);
//        column.setPreferredWidth(150);
//        column.setResizable(false);
//     
//        
//        
//        column = tblPeriod.getColumnModel().getColumn(2);
//        column.setMinWidth(25);
//        column.setMaxWidth(450);
//        column.setPreferredWidth(150);
//        column.setResizable(false);
//        column.setCellEditor(new GetDateEditor(10));
//        
//        column = tblPeriod.getColumnModel().getColumn(3);
//        column.setMinWidth(25);
//        column.setMaxWidth(450);
//        column.setPreferredWidth(150);
//        column.setResizable(false);
//        column.setCellEditor(new GetDateEditor(10));
//    
//        
//        
//    }//End setTableEditor
//    
//    static class EmptyHeaderRenderer extends JList implements TableCellRenderer {
//        /**
//         * Default constructor to set the default foreground/background
//         * and border properties of this renderer for a cell.
//         */
//            EmptyHeaderRenderer() {
//            setOpaque(true);
//            setForeground(UIManager.getColor("TableHeader.foreground"));
//            setBackground(UIManager.getColor("TableHeader.background"));
//            setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
//            ListCellRenderer renderer = getCellRenderer();
//            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
//            setCellRenderer(renderer);
//        }
//            
//            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable jTable, Object obj, boolean param, boolean param3, int param4, int param5) {
//            }
//            
//    }
//            
//    static class IconRenderer  extends DefaultTableCellRenderer {
//
//        /** This holds the Image Icon of Hand Icon
//         */
//        private final ImageIcon HAND_ICON =
//        new ImageIcon(getClass().getClassLoader().getResource(
//        CoeusGuiConstants.HAND_ICON));
//        private final ImageIcon EMPTY_ICON = null;
//        /** Default Constructor*/
//        IconRenderer() {
//        }
//     /**
//         * An overridden method to render the component(icon) in cell.
//         * foreground/background for this cell and Font too.
//         *
//         * @param table  the JTable that is asking the renderer to draw;
//         * can be null
//         * @param value  the value of the cell to be rendered. It is up to the
//         * specific renderer to interpret and draw the value. For example,
//         * if value is the string "true", it could be rendered as a string or
//         * it could be rendered as a check box that is checked. null is a
//         * valid value
//         * @param isSelected  true if the cell is to be rendered with the
//         * selection highlighted; otherwise false
//         * @param hasFocus if true, render cell appropriately. For example,
//         * put a special border on the cell, if the cell can be edited, render
//         * in the color used to indicate editing
//         * @param row the row index of the cell being drawn. When drawing the
//         * header, the value of row is -1
//         * @param column  the column index of the cell being drawn
//         * @return Component
//         *
//         * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object,
//         * boolean, boolean, int, int)
//         */
//        public Component getTableCellRendererComponent(JTable table,
//        Object value, boolean isSelected, boolean hasFocus, int row,
//        int column) {
//            
//            setText((String)value);
//            setOpaque(false);
//            /* if row is selected the place the icon in this cell wherever this
//               renderer is used. */
//            if( isSelected ){
//                setIcon(HAND_ICON);
//            }else{
//                setIcon(EMPTY_ICON);
//            }
//            return this;
//        }
//        
//    }//End Icon Rendering inner class
//
//    /*
//     * Inner class to set the editor for date columns/cells.
//     */
// public class GetDateEditor extends DefaultCellEditor  implements TableCellEditor{
//     private JTextField txtDateComp = new JTextField();
//     int len;
//     String startDate;
//     int selectedRow=0;
//     
//    // private String colName;
//    // private static final String DATE_SEPARATERS = ":/.,|-";
//    // private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
//     txtDateComp.setDocument(new java.awt.LimitedPlainDocument(len));
//     
//     public GetDateEditor(int len){
//        super(new JTextField());
//         this.len = len;
//          txtDateComp.setFont(CoeusFontFactory.getNormalFont());
//            txtDateComp.addFocusListener(new java.awt.event.FocusAdapter(){
//
//                public void focusLost(java.awt.event.FocusEvent focusEvent){
//                    if( !focusEvent.isTemporary() ) {
//                        stopCellEditing();
//                    }
//                }
//            });
//         
//     }
//     
//     
// 
// 
// }
   
        
        
        
        
        
        /** This method is called from within the constructor to
         * initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnDefault = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnInsert = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        scrPnPeriods = new javax.swing.JScrollPane();
        tblPeriod = new javax.swing.JTable();
        lblStartDate = new javax.swing.JLabel();
        lblEndDate = new javax.swing.JLabel();
        txtStartDate = new javax.swing.JTextField();
        txtEndDate = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(380, 275));
        setPreferredSize(new java.awt.Dimension(380, 275));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(btnCancel, gridBagConstraints);

        btnDefault.setFont(CoeusFontFactory.getLabelFont());
        btnDefault.setMnemonic('D');
        btnDefault.setText("Default");
        btnDefault.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(btnDefault, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(btnAdd, gridBagConstraints);

        btnInsert.setFont(CoeusFontFactory.getLabelFont());
        btnInsert.setMnemonic('I');
        btnInsert.setText("Insert");
        btnInsert.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(btnInsert, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        add(btnDelete, gridBagConstraints);

        scrPnPeriods.setBorder(new javax.swing.border.EtchedBorder());
        scrPnPeriods.setMinimumSize(new java.awt.Dimension(300, 200));
        scrPnPeriods.setPreferredSize(new java.awt.Dimension(300, 200));
        tblPeriod.setFont(CoeusFontFactory.getLabelFont());
        tblPeriod.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Period", "Start Date", "End Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPeriod.setToolTipText("");
        scrPnPeriods.setViewportView(tblPeriod);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        add(scrPnPeriods, gridBagConstraints);

        lblStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblStartDate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblStartDate.setText("Budget Start Date");
        lblStartDate.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblStartDate, gridBagConstraints);

        lblEndDate.setFont(CoeusFontFactory.getLabelFont());
        lblEndDate.setText("Budget End Date");
        lblEndDate.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblEndDate, gridBagConstraints);

        txtStartDate.setMinimumSize(new java.awt.Dimension(140, 25));
        txtStartDate.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 37);
        add(txtStartDate, gridBagConstraints);

        txtEndDate.setMinimumSize(new java.awt.Dimension(140, 25));
        txtEndDate.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        add(txtEndDate, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField txtStartDate;
    private javax.swing.JTextField txtEndDate;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnInsert;
    private javax.swing.JLabel lblEndDate;
    private javax.swing.JLabel lblStartDate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDefault;
    private javax.swing.JScrollPane scrPnPeriods;
    private javax.swing.JTable tblPeriod;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnCancel;
    // End of variables declaration//GEN-END:variables

public static void main(String args[]){

    JFrame frame = new javax.swing.JFrame();
    java.awt.Container container = frame.getContentPane();
    JButton btn = new javax.swing.JButton("Open Window");
    frame.setSize(200,100);
    frame.setVisible(true);
    container.add(btn);
    btn.addActionListener(new java.awt.event.ActionListener(){
    public void actionPerformed(ActionEvent e){
        DefinePeriods d = new DefinePeriods();
        d.display();
       }
    });
}


}
