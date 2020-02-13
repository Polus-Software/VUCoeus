/*
 * AddValidFrequencyBasis.java
 *
 * Created on November 23, 2004, 5:51 PM
 */

package edu.mit.coeus.admin.gui;

import edu.mit.coeus.gui.CoeusFontFactory;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.admin.bean.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;


import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;
import javax.swing.table.TableModel;

/**
 *
 * @author  chandrashekara
 */
public class AddValidFrequencyBasisForm extends javax.swing.JComponent implements ActionListener{
    private static final String EMPTY_STRING="";
    private String frequecyCode;
    private String frequencyName;
    private String frequencyBaseCode;
    private CoeusAppletMDIForm mdiForm;
    private static final int CODE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN=1;
    private CoeusDlgWindow dlgAddValidFrequency;
    private CoeusVector cvFrequencyBasis;
    private AddValidFrequencyTableModel addValidFrequencyTableModel;
    private static final int WIDTH = 570;
    private static final int HEIGHT = 290;
    
    /*Bug Fix:N002*/
    private int clicked = 1;
    
    /*Bug Fix:N002*/
    private  static final int OK_CLICKED = 0;
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL + "/AdminMaintenanceServlet";
    
    private CoeusVector cvData;
    
    private CoeusVector cvFrequencyAddData;
    
    private ValidFrequencyBean validFrequencyBean;
    
    private static final int ADD_FREQUENCY_DATA = 4;
    
    private CoeusVector cvFilteredFrequencyData;
    
    private MultipleTableColumnSorter sorter;
    
    /** Creates new form AddValidFrequencyBasis */
    public AddValidFrequencyBasisForm(CoeusAppletMDIForm mdiForm,String frequencyName,
    String frequecyCode,CoeusVector cvFrequencyBasis) {
        this.mdiForm = mdiForm;
        this.frequencyName = frequencyName;
        this.frequecyCode = frequecyCode;
        this.frequencyBaseCode =frequencyBaseCode;
        this.cvFrequencyBasis = cvFrequencyBasis;
        cvData = new CoeusVector();
        cvFrequencyAddData = new CoeusVector();
        initComponents();
        registerComponents();
        setFormData();
        setTableEditors();
        postInitComponents();
        
    }
    
    
    /**
     * Registers listener and other components
     */
    private void registerComponents(){
        Component[] component = {btnOk,btnCancel};
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        setFocusTraversalPolicy(policy);
        setFocusCycleRoot(true);
        
        addValidFrequencyTableModel = new AddValidFrequencyTableModel();
        tblAddFrequency.setModel(addValidFrequencyTableModel);
        btnCancel.addActionListener(this);
        btnOk.addActionListener(this);
    }
    
    /*to set the form data by getting the data from the server*/
    private void setFormData(){
        
        lblFrequencyValue.setText(frequencyName);
        
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('C');
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CONNECTION_STRING);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null){
        }
        
        Hashtable cvNewData = (Hashtable)responderBean.getDataObject();
        cvData = (CoeusVector)cvNewData.get(new Integer(ADD_FREQUENCY_DATA));
        filterAddValidFrequencyData();
        
    }
    
    /*this nethod is to filter the records which are in the main valid Frequency table
     from all the table records*/
    private  void filterAddValidFrequencyData(){
        
        if(cvFrequencyBasis == null || cvFrequencyBasis.size() <= 0 ){
            cvFilteredFrequencyData = cvData;
        }else{
            for(int i=0;i<cvFrequencyBasis.size();i++){
                String code = ((FrequencyBaseBean)cvFrequencyBasis.elementAt(i)).getCode();
                for(int j=0;j<cvData.size();j++){
                    if(((ValidFrequencyBean)cvData.elementAt(j)).getFrequenctBaseCode().equals(code)){
                        cvData.remove(j);
                        cvFilteredFrequencyData = cvData;
                    }
                }
            }
            
        }
        
        /*if the record with the frequency code -1 exists then remove it from the table vector*/
        String code = "-1";
        for(int i=0;i<cvFilteredFrequencyData.size();i++){
            if(((ValidFrequencyBean)cvFilteredFrequencyData.elementAt(i)).getFrequenctBaseCode().equals(code)){
                cvFilteredFrequencyData.remove(i);
                cvFilteredFrequencyData = cvFilteredFrequencyData;
            }
        }
        
    }
    
    /** Specifies the Modal window */
    private void postInitComponents(){
        dlgAddValidFrequency = new CoeusDlgWindow(mdiForm);
        dlgAddValidFrequency.getContentPane().add(this);
        dlgAddValidFrequency.setTitle("Add Valid Frequency and Frequency Basis");
        dlgAddValidFrequency.setFont(CoeusFontFactory.getLabelFont());
        dlgAddValidFrequency.setModal(true);
        dlgAddValidFrequency.setResizable(false);
        dlgAddValidFrequency.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAddValidFrequency.getSize();
        dlgAddValidFrequency.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgAddValidFrequency.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgAddValidFrequency.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAddValidFrequency.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgAddValidFrequency.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    /*to set the focus to the ok button on opening*/
    private void setWindowFocus(){
        btnOk.requestFocusInWindow();
    }
    
    /*to dispose the window on the click of the cancel button*/
    private void performCancelAction(){
        dlgAddValidFrequency.setVisible(false);
    }
    
    /*to display the form*/
    public int display(){
        dlgAddValidFrequency.setVisible(true);
        /*Bug Fix:N002*/
        return clicked;
    }
    
    /*to set the column data*/
    private void setTableEditors(){
        tblAddFrequency.setRowHeight(22);
        tblAddFrequency.setAutoResizeMode(tblAddFrequency.AUTO_RESIZE_OFF);
        JTableHeader tableHeader = tblAddFrequency.getTableHeader();
        if( sorter == null ) {
            sorter = new MultipleTableColumnSorter((TableModel)tblAddFrequency.getModel());
            sorter.setTableHeader(tblAddFrequency.getTableHeader());
            tblAddFrequency.setModel(sorter);
        }
        
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblAddFrequency.setSelectionMode(
        DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        TableColumn column = tblAddFrequency.getColumnModel().getColumn(CODE_COLUMN);
        column.setPreferredWidth(80);
        column.setResizable(true);
        
        
        column = tblAddFrequency.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        column.setPreferredWidth(380);
        column.setResizable(true);
        
    }
    
    /*An inner class for the table model*/
    public class AddValidFrequencyTableModel extends AbstractTableModel{
        
        // represents the column names of the columns of table
        private String[] colName = {"Code","Description"};
        
        // represents the column class of the fields of table
        private Class[] colClass = {String.class,String.class};
        
        /* If the cell is editable,return true else return false*/
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        /**
         * To get the column count
         * @return int
         **/
        public int getColumnCount() {
            return colName.length;
        }
        
        /**
         * To get the column count
         * @param col int
         * @return String
         **/
        public String getColumnName(int col){
            return colName[col];
        }
        
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        
        /**
         * To get the row count
         * @return int
         **/
        public int getRowCount() {
            return cvFilteredFrequencyData.size();
        }
        
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int row, int col) {
            ValidFrequencyBean validFrequencyBean = (ValidFrequencyBean)cvFilteredFrequencyData.get(row);
            switch(col){
                case CODE_COLUMN:
                    return validFrequencyBean.getFrequenctBaseCode();
                case DESCRIPTION_COLUMN:
                    return validFrequencyBean.getDescription();
            }
            return EMPTY_STRING;
        }
        
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblFrequency = new javax.swing.JLabel();
        lblFrequencyValue = new javax.swing.JLabel();
        jcrPnAddFrequency = new javax.swing.JScrollPane();
        tblAddFrequency = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblFrequency.setFont(CoeusFontFactory.getLabelFont());
        lblFrequency.setText("Frequency:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 0, 0);
        add(lblFrequency, gridBagConstraints);

        lblFrequencyValue.setFont(CoeusFontFactory.getLabelFont());
        lblFrequencyValue.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 20, 0, 0);
        add(lblFrequencyValue, gridBagConstraints);

        jcrPnAddFrequency.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Frequency Basis:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        jcrPnAddFrequency.setMinimumSize(new java.awt.Dimension(475, 250));
        jcrPnAddFrequency.setPreferredSize(new java.awt.Dimension(475, 250));
        tblAddFrequency.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jcrPnAddFrequency.setViewportView(tblAddFrequency);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 0, 0);
        add(jcrPnAddFrequency, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(73, 26));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 26));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 5, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents
    
    /*the action performed on the click of the buttons*/
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnOk)){
            dlgAddValidFrequency.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            /*Bug Fix:N002*/
            clicked = OK_CLICKED;
            performCopyAction();
            dlgAddValidFrequency.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }else if(source.equals(btnCancel)){
            dlgAddValidFrequency.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            dlgAddValidFrequency.dispose();
            dlgAddValidFrequency.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
    /*action performed on the click of  the OK button*/
    public CoeusVector performCopyAction(){
        int rows[] = tblAddFrequency.getSelectedRows();
        CoeusVector cvSelectedData = new CoeusVector();
        for (int i = 0; i < rows.length; i++) {
            validFrequencyBean = (ValidFrequencyBean)cvFilteredFrequencyData.get(rows[i]);
            if (validFrequencyBean != null) {
                cvSelectedData.add(validFrequencyBean);
            }
            
        }
        dlgAddValidFrequency.dispose();
        return cvSelectedData;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JScrollPane jcrPnAddFrequency;
    public javax.swing.JLabel lblFrequency;
    public javax.swing.JLabel lblFrequencyValue;
    public javax.swing.JTable tblAddFrequency;
    // End of variables declaration//GEN-END:variables
    
}
