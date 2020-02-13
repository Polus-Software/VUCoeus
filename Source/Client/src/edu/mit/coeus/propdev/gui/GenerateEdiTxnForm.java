/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;


/** Generate EDI Transaction Form
 * GenerateEdiTxnForm.java
 * @author  Vyjayanthi
 * Created on January 12, 2004, 3:00 PM
 */

public class GenerateEdiTxnForm extends javax.swing.JComponent
implements ActionListener{
    
    private CoeusDlgWindow dlgGenerateEdiTxn;
    private Frame parent;
    private boolean modal;// = true;
    private String proposalNumber;
    private CoeusAppletMDIForm mdiForm;
    /** Holds the connection string */
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
                                             "/ProposalActionServlet";
    
    private static final String TITLE = "EDI transaction for the proposal ";
    
    private static final int HEIGHT = 440;
    private static final int WIDTH = 440;
    
    private EdiTransactionTableModel ediTxnTableModel;
    private EdiTransactionRenderer ediTxnRenderer;
    
    /** Creates new form GenerateEdiTxnForm */
    public GenerateEdiTxnForm(CoeusAppletMDIForm mdiForm,boolean modal,String proposalNumber) {
        this.mdiForm = mdiForm;
        this.modal = modal;
        this.proposalNumber = proposalNumber;
        initComponents();
        postInitComponents();
    }    
    
    /** This method is called from within the constructor to
     * initialize the form. */
    private void postInitComponents(){
        dlgGenerateEdiTxn = new CoeusDlgWindow(mdiForm, modal);
        dlgGenerateEdiTxn.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgGenerateEdiTxn.getContentPane().add(this);
        dlgGenerateEdiTxn.setResizable(false);
        dlgGenerateEdiTxn.setTitle(TITLE + proposalNumber);
        dlgGenerateEdiTxn.setFont(CoeusFontFactory.getLabelFont());
        dlgGenerateEdiTxn.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgGenerateEdiTxn.getSize();
        dlgGenerateEdiTxn.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        btnGenerateTxn.addActionListener(this);
        btnErrorLog.addActionListener(this);
        btnClose.addActionListener(this);
        
        ediTxnTableModel = new EdiTransactionTableModel();
        ediTxnRenderer = new EdiTransactionRenderer();
        
        /** Setting table model for the table */
        tblFiles.setModel(ediTxnTableModel);
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { txtDateAckRecd, rdBtnProduction, 
                rdBtnTest, rdBtnDevelopment, tblFiles, btnGenerateTxn, btnErrorLog, btnClose};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        
        dlgGenerateEdiTxn.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgGenerateEdiTxn.dispose();
            }
        });
        dlgGenerateEdiTxn.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgGenerateEdiTxn.addWindowListener(new WindowAdapter(){
             public void windowActivated(WindowEvent we) {
                requestDefaultFocusOnComponent();
            }
            public void windowClosing(WindowEvent we){
               dlgGenerateEdiTxn.dispose();
            }
        });
        
    }    
    
     /** To set the default focus for the specified component
     */
    private void requestDefaultFocusOnComponent(){
        btnGenerateTxn.requestFocus();
    }
    
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    private void setFormData(Object data){
//        ediTxnTableModel.setData(vecDataBean);
    }
    
    public void display(){
        dlgGenerateEdiTxn.setVisible(true);
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(btnGenerateTxn) ){
            performGenerateTxn();
        }else if( source.equals(btnErrorLog) ){
            performErrorLog();
        }else if( source.equals(btnClose) ){
            performWindowClosing();
        }
    }
    
    private void performGenerateTxn(){
        CoeusOptionPane.showInfoDialog("Functionality not implemeneted");
    }
    
    private void performErrorLog(){
        CoeusOptionPane.showInfoDialog("Functionality not implemeneted");
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag.
     * If the saveRequired is true then it saves the details to the
     * database else dispose this screen.
     */
    private void performWindowClosing(){
        dlgGenerateEdiTxn.dispose();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        btngrpEnvironment = new javax.swing.ButtonGroup();
        pnlTransaction = new javax.swing.JPanel();
        lblTransactionSet = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblDateTransGen = new javax.swing.JLabel();
        lblDateAckRecd = new javax.swing.JLabel();
        txtTransactionSet = new javax.swing.JTextField();
        txtTransStatus = new javax.swing.JTextField();
        txtDateTransGen = new javax.swing.JTextField();
        txtDateAckRecd = new javax.swing.JTextField();
        pnlEnvironment = new javax.swing.JPanel();
        rdBtnProduction = new javax.swing.JRadioButton();
        rdBtnTest = new javax.swing.JRadioButton();
        rdBtnDevelopment = new javax.swing.JRadioButton();
        txtProduction = new javax.swing.JTextField();
        txtTest = new javax.swing.JTextField();
        txtDevelopment = new javax.swing.JTextField();
        scrPnFiles = new javax.swing.JScrollPane();
        tblFiles = new javax.swing.JTable();
        btnGenerateTxn = new javax.swing.JButton();
        btnErrorLog = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        txtStatus = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        pnlTransaction.setLayout(new java.awt.GridBagLayout());

        pnlTransaction.setBorder(new javax.swing.border.EtchedBorder());
        pnlTransaction.setMinimumSize(new java.awt.Dimension(300, 105));
        pnlTransaction.setPreferredSize(new java.awt.Dimension(300, 105));
        lblTransactionSet.setFont(CoeusFontFactory.getLabelFont());
        lblTransactionSet.setText("Transaction Set:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 3);
        pnlTransaction.add(lblTransactionSet, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 3);
        pnlTransaction.add(lblStatus, gridBagConstraints);

        lblDateTransGen.setFont(CoeusFontFactory.getLabelFont());
        lblDateTransGen.setText("Date Trans. Gen.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 3);
        pnlTransaction.add(lblDateTransGen, gridBagConstraints);

        lblDateAckRecd.setFont(CoeusFontFactory.getLabelFont());
        lblDateAckRecd.setText("Date Ack. Recd.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 3);
        pnlTransaction.add(lblDateAckRecd, gridBagConstraints);

        txtTransactionSet.setEditable(false);
        txtTransactionSet.setFont(CoeusFontFactory.getNormalFont());
        txtTransactionSet.setMinimumSize(new java.awt.Dimension(40, 20));
        txtTransactionSet.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 3);
        pnlTransaction.add(txtTransactionSet, gridBagConstraints);

        txtTransStatus.setEditable(false);
        txtTransStatus.setFont(CoeusFontFactory.getNormalFont());
        txtTransStatus.setMinimumSize(new java.awt.Dimension(120, 20));
        txtTransStatus.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 3);
        pnlTransaction.add(txtTransStatus, gridBagConstraints);

        txtDateTransGen.setEditable(false);
        txtDateTransGen.setFont(CoeusFontFactory.getNormalFont());
        txtDateTransGen.setMinimumSize(new java.awt.Dimension(120, 20));
        txtDateTransGen.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 3);
        pnlTransaction.add(txtDateTransGen, gridBagConstraints);

        txtDateAckRecd.setFont(CoeusFontFactory.getNormalFont());
        txtDateAckRecd.setMinimumSize(new java.awt.Dimension(120, 20));
        txtDateAckRecd.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 3);
        pnlTransaction.add(txtDateAckRecd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(pnlTransaction, gridBagConstraints);

        pnlEnvironment.setLayout(new java.awt.GridBagLayout());

        pnlEnvironment.setBorder(new javax.swing.border.TitledBorder(null, "Environment", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlEnvironment.setFont(CoeusFontFactory.getLabelFont());
        rdBtnProduction.setFont(CoeusFontFactory.getLabelFont());
        rdBtnProduction.setText("Production");
        btngrpEnvironment.add(rdBtnProduction);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlEnvironment.add(rdBtnProduction, gridBagConstraints);

        rdBtnTest.setFont(CoeusFontFactory.getLabelFont());
        rdBtnTest.setText("Test");
        btngrpEnvironment.add(rdBtnTest);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlEnvironment.add(rdBtnTest, gridBagConstraints);

        rdBtnDevelopment.setFont(CoeusFontFactory.getLabelFont());
        rdBtnDevelopment.setText("Development");
        btngrpEnvironment.add(rdBtnDevelopment);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlEnvironment.add(rdBtnDevelopment, gridBagConstraints);

        txtProduction.setEditable(false);
        txtProduction.setMinimumSize(new java.awt.Dimension(300, 20));
        txtProduction.setPreferredSize(new java.awt.Dimension(300, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEnvironment.add(txtProduction, gridBagConstraints);

        txtTest.setEditable(false);
        txtTest.setMinimumSize(new java.awt.Dimension(300, 20));
        txtTest.setPreferredSize(new java.awt.Dimension(300, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEnvironment.add(txtTest, gridBagConstraints);

        txtDevelopment.setEditable(false);
        txtDevelopment.setMinimumSize(new java.awt.Dimension(300, 20));
        txtDevelopment.setPreferredSize(new java.awt.Dimension(300, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEnvironment.add(txtDevelopment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(pnlEnvironment, gridBagConstraints);

        scrPnFiles.setMinimumSize(new java.awt.Dimension(22, 150));
        scrPnFiles.setPreferredSize(new java.awt.Dimension(22, 150));
        tblFiles.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tblFiles.setFont(CoeusFontFactory.getNormalFont());
        tblFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblFiles.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnFiles.setViewportView(tblFiles);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(scrPnFiles, gridBagConstraints);

        btnGenerateTxn.setFont(CoeusFontFactory.getLabelFont());
        btnGenerateTxn.setMnemonic('G');
        btnGenerateTxn.setText("Generate Txn.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnGenerateTxn, gridBagConstraints);

        btnErrorLog.setFont(CoeusFontFactory.getLabelFont());
        btnErrorLog.setMnemonic('E');
        btnErrorLog.setText("Error Log");
        btnErrorLog.setMaximumSize(new java.awt.Dimension(113, 26));
        btnErrorLog.setMinimumSize(new java.awt.Dimension(113, 26));
        btnErrorLog.setPreferredSize(new java.awt.Dimension(113, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        add(btnErrorLog, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(113, 26));
        btnClose.setMinimumSize(new java.awt.Dimension(113, 26));
        btnClose.setPreferredSize(new java.awt.Dimension(113, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 5);
        add(btnClose, gridBagConstraints);

        txtStatus.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(txtStatus, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnErrorLog;
    public javax.swing.JButton btnGenerateTxn;
    public javax.swing.ButtonGroup btngrpEnvironment;
    public javax.swing.JLabel lblDateAckRecd;
    public javax.swing.JLabel lblDateTransGen;
    public javax.swing.JLabel lblStatus;
    public javax.swing.JLabel lblTransactionSet;
    public javax.swing.JPanel pnlEnvironment;
    public javax.swing.JPanel pnlTransaction;
    public javax.swing.JRadioButton rdBtnDevelopment;
    public javax.swing.JRadioButton rdBtnProduction;
    public javax.swing.JRadioButton rdBtnTest;
    public javax.swing.JScrollPane scrPnFiles;
    public javax.swing.JTable tblFiles;
    public javax.swing.JTextField txtDateAckRecd;
    public javax.swing.JTextField txtDateTransGen;
    public javax.swing.JTextField txtDevelopment;
    public javax.swing.JTextField txtProduction;
    public javax.swing.JTextField txtStatus;
    public javax.swing.JTextField txtTest;
    public javax.swing.JTextField txtTransStatus;
    public javax.swing.JTextField txtTransactionSet;
    // End of variables declaration//GEN-END:variables
    
//    public static void main(String s[]){
//        JFrame frame = new JFrame();
//        frame.getContentPane().add(new GenerateEdiTxnForm());
//        frame.setSize(440, 440);
//        frame.show();
//    }

    //Inner Class Table Model - Start
    class EdiTransactionTableModel extends AbstractTableModel{
        
        private Vector vecDataBean;
        private DateUtils dtUtils;
        
        String colNames[] = {
            };

        Class[] types = new Class [] {
            Object.class, Object.class, Object.class, String.class, String.class, String.class };
            
        EdiTransactionTableModel(){
            this.dtUtils = new DateUtils();
        }
            
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            if( vecDataBean == null ) return 0;
            return vecDataBean.size();
        }
        
        public Object getValueAt(int row, int column) {
            return new String();
        }
        
        /** Setter for property vecDataBean.
         * @param vecDataBean New value of property vecDataBean.
         *
         */
        public void setData(java.util.Vector vecDataBean) {
            this.vecDataBean = vecDataBean;
        }
        
    }
    //Inner class Table Model - End
    
    //EdiTransaction Renderer - Start
    class EdiTransactionRenderer extends DefaultTableCellRenderer {

        EdiTransactionRenderer() {
        }
        
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if( column == 2 || column == 3 ) {
                setHorizontalAlignment(JLabel.CENTER);
            }else{
                setHorizontalAlignment(JLabel.RIGHT);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }//EdiTransaction Renderer - End
}
