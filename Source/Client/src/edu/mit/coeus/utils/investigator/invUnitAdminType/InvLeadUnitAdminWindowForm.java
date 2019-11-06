/*
 * InvLeadUnitAdminWindowForm.java
 *
 * Created on October 17, 2006, 12:07 PM
 */

package edu.mit.coeus.utils.investigator.invUnitAdminType;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.unit.bean.UnitAdministratorBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author  tarique
 */
public class InvLeadUnitAdminWindowForm extends javax.swing.JComponent 
                                                    implements ActionListener {
    private static final int PERSON_NAME = 1;
    private static final int PERSON_TYPE = 0 ;
    private static final String EMPTY_STRING = "";
    private InvLeadUnitAdminWindowModel invLeadUnitAdminWindowModel;
    private InvLeadUnitAdminWindowRenderer invLeadUnitAdminWindowRenderer;
    private CoeusVector cvData;
    private CoeusVector cvAdminTypeCode;
    private CoeusMessageResources coeusMessageResources;
    private String leadUnit;
    private CoeusDlgWindow dlgAdminType;
    private boolean clicked;
    /** Creates new form InvLeadUnitAdminWindowForm */
    public InvLeadUnitAdminWindowForm(String leadUnit) throws CoeusException{
        this.leadUnit = leadUnit;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        registerComponents();
        postInitComponents();
        CoeusVector cvServerData = (CoeusVector)getLeadAdminData();
        cvData = (CoeusVector)cvServerData.get(0);
        cvAdminTypeCode = (CoeusVector)cvServerData.get(1);
        if(cvAdminTypeCode == null){
              cvAdminTypeCode = new CoeusVector();
        }
        invLeadUnitAdminWindowModel.setData(cvData);
        invLeadUnitAdminWindowModel.fireTableDataChanged();
        if( tblUnitAdmin.getRowCount() > 0 ){
            tblUnitAdmin.setRowSelectionInterval(0, 0);
        }
    }
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        invLeadUnitAdminWindowModel = new InvLeadUnitAdminWindowModel();
        invLeadUnitAdminWindowRenderer = new InvLeadUnitAdminWindowRenderer();
        tblUnitAdmin.setModel(invLeadUnitAdminWindowModel);        
       
        setTableEditors();
    }
    public void display() {
        dlgAdminType.setVisible(true);
    }
     /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        
        dlgAdminType = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm());
        dlgAdminType.setResizable(false);
        dlgAdminType.setModal(true);
        dlgAdminType.getContentPane().add(this);
        dlgAdminType.setFont(CoeusFontFactory.getLabelFont());
        dlgAdminType.setSize(400, 330);
        dlgAdminType.setTitle("Administrators for "+leadUnit);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAdminType.getSize();
        dlgAdminType.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
         dlgAdminType.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        
        dlgAdminType.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgAdminType.dispose();
            }
        });
       
        dlgAdminType.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAdminType.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
                 dlgAdminType.dispose();
             }
        });
    }
    /**
     *Method for setting table
     */
    private void setTableEditors(){
        
        tblUnitAdmin.setRowHeight(22);
        tblUnitAdmin.setShowHorizontalLines(false);
        tblUnitAdmin.setShowVerticalLines(false);
        tblUnitAdmin.setRowSelectionAllowed(true);
        tblUnitAdmin.setColumnSelectionAllowed(false);
        tblUnitAdmin.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = tblUnitAdmin.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
        header.setReorderingAllowed(false);
        
        tblUnitAdmin.setOpaque(false);
        tblUnitAdmin.setSelectionMode(
                        DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        int []columnSize = {140,240};
        TableColumn column;
        column = tblUnitAdmin.getColumnModel().getColumn(0);
        column.setMinWidth(20);
        column.setPreferredWidth(columnSize[1]);
        column.setCellRenderer(invLeadUnitAdminWindowRenderer);
        
        column = tblUnitAdmin.getColumnModel().getColumn(1);
        column.setMinWidth(25);
        column.setPreferredWidth(columnSize[1]);
        column.setCellRenderer(invLeadUnitAdminWindowRenderer);
        
        
        
       
    }
    private List getLeadAdminData() throws CoeusException{
        CoeusVector cvMainData = null;
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        final String connectTo = CoeusGuiConstants.CONNECTION_URL +"/unitServlet";
        
        request.setDataObject(leadUnit);
        request.setFunctionType('K');
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
               cvMainData = (CoeusVector)response.getDataObject();
                //cvData = (CoeusVector)cvMainData.get(0);
            }else{
                throw new CoeusException(response.getMessage());
            }
        }else{
            throw new CoeusException(response.getMessage());
        }
        
        return cvMainData;
    }
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
//        if(source.equals( btnOk )) {
//            setClicked(true);
//            dlgAdminType.dispose();
//        }else if(source.equals( btnCancel)) {
//            dlgAdminType.dispose();
//        }
    }   
    /**
     *  Method to get the selected rows as vector ( used for multiple selection )
     * @return Vector collection of selected row where in each element has Hashmap instance.
     * @throws Exception
     */
    public Vector getMultipleSelectedRows() throws Exception{
        int selectedIndices[] = tblUnitAdmin.getSelectedRows();
        if( selectedIndices.length == 0 ){
            throw new Exception(coeusMessageResources.parseMessageKey(
            "searchResultWin_exceptionCode.1109"));
        }
        Vector multipleSelectedRow = new Vector();
        for( int i = 0; i < selectedIndices.length; i++ ){
            UnitAdministratorBean bean = (UnitAdministratorBean)cvData.get(selectedIndices[i]);
            multipleSelectedRow.addElement(bean );
        }
        return multipleSelectedRow;
    }
    /**
     * Class for table model
     */    
    class InvLeadUnitAdminWindowModel extends AbstractTableModel{
        /** String array for column names*/        
        String colNames[] = {"Admin Type" ,"Name"};
        /** Class column Type arrays */        
        Class[] colTypes = new Class [] {String.class ,String.class};
        CoeusVector cvData ;
        
        /**
         * method to set data in table model
         * @param cvAdminType vector object
         */        
        public void setData(CoeusVector cvData) {
            this.cvData = cvData;
        }
        
        /**
         * Method to get value
         * @param rowIndex row
         * @param columnIndex column
         * @return Object
         */        
        public Object getValueAt(int rowIndex, int columnIndex) {
            UnitAdministratorBean personInfoBean = (UnitAdministratorBean)cvData.get(rowIndex);
            switch(columnIndex) {
                case PERSON_TYPE :
                    int adminTypeCode = personInfoBean.getUnitAdminTypeCode();
                    CoeusVector filteredVector = cvAdminTypeCode.filter(
                    new Equals("code", ""+adminTypeCode));
                    if(filteredVector!=null && filteredVector.size() > 0){
                        ComboBoxBean comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                        return comboBoxBean;
                    }else{
                        return new ComboBoxBean("","");
                    }
                
                case PERSON_NAME:
                    String personName = personInfoBean.getPersonName() == null ?EMPTY_STRING
                                    : personInfoBean.getPersonName();
                    return personName;
            }
            return EMPTY_STRING;
            
        }
        /**
         * Method to check cell is editable or not
         * @param row specify the row
         * @param column specify in which column
         * @return boolean
         */        
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        
        /**
         * Method which count the number of column
         * @return integer value
         */        
        public int getColumnCount() {
           return colNames.length;
        }
        
        /**
         * Method which return column class type
         * @param columnIndex specify which column
         * @return Class
         */        
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        /**
         * Method to get column name
         * @param column specify which column
         * @return String
         */        
        public String getColumnName(int column) {
            return colNames[column];
        }
        /**
         * Method which return row count
         * @return integer value for count
         */        
        public int getRowCount() {
            return (cvData == null ? 0 : cvData.size());
            
        }
    }
    
    /*Renderer for the table columns*/
    /**
     * Class for cell renderer
     */    
    class InvLeadUnitAdminWindowRenderer extends DefaultTableCellRenderer{
        private JLabel lblCellComponent;
        /**
         * Constructor for cell renderer
         */        
        public InvLeadUnitAdminWindowRenderer() {
           lblCellComponent=new JLabel();
           lblCellComponent.setOpaque(true);
           
        }
        
        /**
         * Method which return the component
         * @param table object
         * @param value of the cell
         * @param isSelected cell is selected or not
         * @param hasFocus cell has got focus or not
         * @param row of the table
         * @param column of the column
         * @return Component
         */        
        public java.awt.Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus, int row, int column){
            
            switch(column) {
                case PERSON_TYPE :                    
                case PERSON_NAME:
                    lblCellComponent.setBackground(Color.WHITE);
                    lblCellComponent.setForeground(java.awt.Color.black);
                    if(isSelected){
                        lblCellComponent.setBackground(java.awt.Color.YELLOW);
                        lblCellComponent.setForeground(java.awt.Color.black);
                    }
                    value = ( value == null ? EMPTY_STRING : value );
                    lblCellComponent.setText(value.toString().trim());
                    return lblCellComponent;
                    
            }
            return null;
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnUnitAdmin = new javax.swing.JScrollPane();
        tblUnitAdmin = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        scrPnUnitAdmin.setMinimumSize(new java.awt.Dimension(500, 300));
        scrPnUnitAdmin.setPreferredSize(new java.awt.Dimension(500, 300));
        tblUnitAdmin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        scrPnUnitAdmin.setViewportView(tblUnitAdmin);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(scrPnUnitAdmin, gridBagConstraints);

    }//GEN-END:initComponents

    /**
     * Getter for property clicked.
     * @return Value of property clicked.
     */
    public boolean isClicked() {
        return clicked;
    }    

    /**
     * Setter for property clicked.
     * @param clicked New value of property clicked.
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }    
     
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrPnUnitAdmin;
    private javax.swing.JTable tblUnitAdmin;
    // End of variables declaration//GEN-END:variables
    
}
