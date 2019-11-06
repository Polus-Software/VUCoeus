/*
 * CheckList.java
 *
 * Created on August 29, 2003, 7:21 PM
 */

package edu.mit.coeus.iacuc.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;

import edu.mit.coeus.bean.CheckListBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ObjectCloner;
/** Displays the CheckList for Expedited and Exempt
 * @author sharathk
 */
public class CheckList extends javax.swing.JComponent {

    private CheckListTableModel checkListTableModel;
    private CheckListMediator checkListMediator;
    private CoeusDlgWindow dialog;

    private Frame owner;
    private boolean modal;
    private int retValue = -1;

    private int WIDTH = 800;
    private int HEIGHT = 550;

    public static final int OK = 1;
    public static final int CANCEL = 0;
    
    public static final int CHECKBOX_COLUMN = 0;
    // modified by ravi to display code also - START
    public static final int CODE_COLUMN = 1;
    public static final int DESCRIPTION_COLUMN = 2;
    // modified by ravi - END
    
    private static final int CHECKBOX_COLUMN_WIDTH = 20; // 50
    private static final int CODE_COLUMN_WIDTH = 30;
    private static final int DESCRIPTION_COLUMN_WIDTH = 646;
    
    private static final int ROW_HEIGHT = 90;
    
    private boolean editable = true;
    
    private static final String TITLE = "Check List";
    
    private StringBuffer sbCodes = new StringBuffer();
    private boolean saveRequired;
    private CoeusMessageResources messageResources = CoeusMessageResources.getInstance();
    private Vector oldData;
    /** Creates new form CheckList */
    private CheckList() {
        initComponents();
        postInitComponents();        
    }

    /** Creates new form CheckList
     * @param owner owner of the Dialog
     * @param modal modality
     */
    public CheckList(Frame owner, boolean modal) {
        dialog = new CoeusDlgWindow(owner, modal);
        initComponents();
        postInitComponents();
    }
    
    /** Creates new form CheckList
     * @param owner owner of the Dialog
     * @param modal modality
     */    
    public CheckList(Dialog owner, boolean modal) {
        dialog = new CoeusDlgWindow(owner, modal);
        initComponents();
        postInitComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnCheckList = new javax.swing.JScrollPane();
        tblCheckList = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblCurrentSelection = new javax.swing.JLabel();
        txtArCodes = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        scrPnCheckList.setMinimumSize(new java.awt.Dimension(400, 275));
        scrPnCheckList.setPreferredSize(new java.awt.Dimension(400, 275));
        tblCheckList.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnCheckList.setViewportView(tblCheckList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 2);
        add(scrPnCheckList, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(75, 24));
        btnOk.setMinimumSize(new java.awt.Dimension(75, 24));
        btnOk.setPreferredSize(new java.awt.Dimension(75, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(75, 24));
        btnCancel.setMinimumSize(new java.awt.Dimension(75, 24));
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(btnCancel, gridBagConstraints);

        lblCurrentSelection.setFont(CoeusFontFactory.getLabelFont());
        lblCurrentSelection.setText("Current Selection : ");
        lblCurrentSelection.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblCurrentSelection, gridBagConstraints);

        txtArCodes.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtArCodes.setEditable(false);
        txtArCodes.setFont(CoeusFontFactory.getNormalFont());
        txtArCodes.setLineWrap(true);
        txtArCodes.setWrapStyleWord(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(txtArCodes, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void postInitComponents() {
        
        java.awt.Component[] component = {tblCheckList,btnOk,btnCancel};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(component);
        setFocusTraversalPolicy(traversalPolicy);
        setFocusCycleRoot(true);
        
        //Creating instances
        checkListTableModel = new CheckListTableModel();
        checkListMediator = new CheckListMediator();

        //Setting Up Table
        tblCheckList.setModel(checkListTableModel);
        tblCheckList.setFont(CoeusFontFactory.getNormalFont());
        // modified by ravi for not showing the table header - START
        tblCheckList.setTableHeader(null);
        //tblCheckList.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        //tblCheckList.getTableHeader().setReorderingAllowed(false);
        // modified by ravi - END
        tblCheckList.getColumnModel().getColumn(CHECKBOX_COLUMN).setPreferredWidth(CHECKBOX_COLUMN_WIDTH);
        tblCheckList.getColumnModel().getColumn(CHECKBOX_COLUMN).setMaxWidth(CHECKBOX_COLUMN_WIDTH);

        tblCheckList.getColumnModel().getColumn(CODE_COLUMN).setPreferredWidth(CODE_COLUMN_WIDTH);
        tblCheckList.getColumnModel().getColumn(CODE_COLUMN).setMaxWidth(CODE_COLUMN_WIDTH);

        tblCheckList.getColumnModel().getColumn(DESCRIPTION_COLUMN).setPreferredWidth(DESCRIPTION_COLUMN_WIDTH);
        
        //tblCheckList.setRowHeight(ROW_HEIGHT);
        tblCheckList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
        //tblCheckList.setShowHorizontalLines( false );
        //tblCheckList.setShowGrid( false );
        tblCheckList.setShowVerticalLines( false );
        tblCheckList.setRowSelectionAllowed( false );
        tblCheckList.setCellSelectionEnabled( false );
        //DescriptionEditor descriptionEditor = new DescriptionEditor();
        DescriptionRenderer descriptionRenderer = new DescriptionRenderer();
        tblCheckList.setRowMargin(5);
        tblCheckList.setDefaultRenderer(String.class, descriptionRenderer );
        //tblCheckList.getColumnModel().getColumn(DESCRIPTION_COLUMN).setCellRenderer(descriptionRenderer);
        //tblCheckList.getColumnModel().getColumn(DESCRIPTION_COLUMN).setCellEditor(descriptionEditor);
        //Registering Components with Event Listeners
        btnCancel.addActionListener(checkListMediator);
        btnOk.addActionListener(checkListMediator);
        dialog.addWindowListener(checkListMediator);
        dialog.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    performWindowClosing();
                }
        });
        //Preparing Dialog to Display.
        dialog.getContentPane().add(this);
        dialog.setTitle(TITLE);
        dialog.setResizable(false);
        dialog.setSize(WIDTH, HEIGHT);
                
    }
    private void requestDefaultFocusForComponent(){
        if( tblCheckList.getRowCount() > 0 ) {
            tblCheckList.requestFocusInWindow();
        }else{
            btnCancel.requestFocusInWindow();
        }
    }
    /** For Testing Purpose Only
     */
    /*public static void main(java.lang.String[] params) {
        CheckList cl = new CheckList(new Frame(), true);

        Vector data = new Vector();

        CheckListBean clb = new CheckListBean(1,"Desc");
        clb.setIsChecked(true);
        data.add(clb);

        CheckListBean clb2 = new CheckListBean(2,"Desc");
        clb2.setIsChecked(false);
        data.add(clb2);

        cl.setFormData(data);

      //  JFrame frame = new JFrame();
      //  frame.getContentPane().add(cl);
      //  frame.setSize(400, 300);
      //  frame.setVisible(true);
      //  int i = cl.display();
      //  System.out.println("i = " + i);
    }*/

    /** sets the Form Data.
     * @param data Vector of CheckListBean
     */    
    public void setFormData(Vector data) {
        try{
            oldData = data;
            checkListTableModel.setDataVector((Vector)ObjectCloner.deepCopy(data));
            checkListTableModel.fireTableDataChanged();
        }catch(Exception e){
            checkListTableModel.setDataVector(data);
            checkListTableModel.fireTableDataChanged();
        }
        updateSelectedCodes();
        if( data != null ) {
            for( int rowCount = 0; rowCount < data.size(); rowCount++) {
                String descValue = ((CheckListBean)data.get(rowCount)).getDescription();
                int totalRows = (int)Math.ceil(descValue.toString().trim().length()/100.0) ;
                if( totalRows > 0 ){
                    tblCheckList.setRowHeight(rowCount,(15 * totalRows) + 5 );
                }else{
                    tblCheckList.setRowHeight(rowCount,32);
                }
            }
        }
        saveRequired = false;
    }
    public boolean isSaveRequired(){
        return saveRequired;
    }
    /** returns form Data.
     * @return form Data
     */    
    public Vector getFormData() {
        return checkListTableModel.getDataVector();
    }
    
    /** returns Data depending on the checked flag.
     * @param checked return values depends on this flag.
     * @return Vector of CheckList.
     */    
    public Vector getData(boolean checked) {
        Vector data, retData;
        data = getFormData();
        retData = new Vector();
        for(int index = 0; index < data.size(); index++) {
            if(((CheckListBean)data.get(index)).isChecked() == checked) {
                retData.add(data.get(index));
            }
        }
        return retData;
    }
    
    /** displays the dialog.
     * @return retuns OK or CANCEL depending on button click.
     */    
    public int display() {
        tblCheckList.requestFocusInWindow();
        if( !editable ) {
            tblCheckList.setBackground((Color)UIManager.getDefaults().get("Panel.background"));
        }else{
            tblCheckList.setBackground(Color.white);
        }
        dialog.setLocation(CoeusDlgWindow.CENTER);
        //dialog.setVisible(true);
        dialog.show();
        return retValue;
    }
    
    /** sets this dialog as editable/non-editable.
     * @param editable editable
     */    
    public void setEditable(boolean editable) {
        this.editable = editable;
        btnOk.setEnabled(editable);
        checkListTableModel.setEditable(editable);
    }
    
    /** sets the Title for the dialog.
     * @param title title
     */    
    public void setTitle(String title) {
        dialog.setTitle(title);
    }
    
    private void updateSelectedCodes(){
        sbCodes.delete(0,sbCodes.length());
        int codesInRow = 0;
        int rowCount = checkListTableModel.getRowCount();
        for( int indx = 0 ; indx < rowCount ; indx++ ) {
            Boolean checked = (Boolean) checkListTableModel.getValueAt(
                                            indx,CheckList.CHECKBOX_COLUMN);
            if( checked.booleanValue() ) {
                codesInRow++;
                String code = (String) checkListTableModel.getValueAt(
                                            indx, CheckList.CODE_COLUMN);
                sbCodes.append(code + " ");
            }
        }
        txtArCodes.setText( sbCodes.toString() );
        int reqHeight = (int)Math.ceil(txtArCodes.getText().trim().length()/100.0) ;
        if( reqHeight > 0 ){
            txtArCodes.setPreferredSize(new Dimension( 
                txtArCodes.getWidth(),22 + ( 10 * reqHeight )));
        }else{
            txtArCodes.setMinimumSize(new Dimension(txtArCodes.getWidth(),22));
        }
    }

    void performWindowClosing(){
        if( saveRequired ) {
            int selection = CoeusOptionPane.SELECTION_CANCEL;
            selection = CoeusOptionPane.showQuestionDialog(
                    messageResources.parseMessageKey("user_details_exceptionCode.2547"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.SELECTION_YES);
            if(selection == CoeusOptionPane.SELECTION_NO) {
                saveRequired = false;
                retValue = CANCEL;
                dialog.setVisible(false);
                return;
            }else if ( selection == CoeusOptionPane.SELECTION_YES ) {
                oldData = getFormData();
                retValue = OK;
                dialog.setVisible(false);
            }
        }else{
            retValue = CANCEL;
            dialog.setVisible(false);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblCurrentSelection;
    public javax.swing.JScrollPane scrPnCheckList;
    public javax.swing.JTable tblCheckList;
    public javax.swing.JTextArea txtArCodes;
    // End of variables declaration//GEN-END:variables

    /** Listens to Events for CheckList.
     */    
    private class CheckListMediator extends WindowAdapter implements ActionListener{
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if(source.equals(btnCancel)) {
                //Event Handling for Cancel
                performWindowClosing();
            }else if(source.equals(btnOk)) {
                oldData = getFormData();
                retValue = OK;
                dialog.setVisible(false);
                
            }//Event Handling for OK
        }//actionPerformed(ActionEvent actionEvent)
        public void windowActivated(WindowEvent we) {
            requestDefaultFocusForComponent();
        }
        public void windowClosing(WindowEvent windowEvent) {
            performWindowClosing();
        }
        

    }//End CheckListMediator

    class CheckListTableModel extends AbstractTableModel {

        private String colNames[] = {"Status","Code","Description"};
        private Class colTypes[] = {Boolean.class, String.class,String.class};
        private boolean editable = true;
        private Vector data; //Collection of CheckListBean

        CheckListTableModel() {
        }

        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            if(columnIndex == CheckList.CHECKBOX_COLUMN) {
                ((CheckListBean)data.get(rowIndex)).setIsChecked(((Boolean)value).booleanValue());
                updateSelectedCodes();
                saveRequired = true;
            }
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if(columnIndex == CheckList.CHECKBOX_COLUMN){
                return editable;
            }else {
                return false;
            }
        }

        public int getColumnCount() {
            return colNames.length;
        }

        public int getRowCount() {
            if(data == null) return 0;
            return data.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if(columnIndex == CheckList.CHECKBOX_COLUMN){
                return new Boolean(((CheckListBean)data.get(rowIndex)).isChecked());
            }else if( columnIndex == CheckList.CODE_COLUMN ) {
                return ((CheckListBean)data.get(rowIndex)).getCheckListCode();
            }
            
            String descValue = ((CheckListBean)data.get(rowIndex)).getDescription();
            return descValue;
        }

        public String getColumnName(int columnIndex) {
            return colNames[columnIndex];
        }

        public Class getColumnClass(int columnIndex) {
            return colTypes[columnIndex];
        }

        public void setDataVector(Vector data) {
            this.data = data;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public Vector getDataVector() {
            return data;
        }

    }//End CheckListTableModel

    class DescriptionRenderer implements TableCellRenderer{
        private JTextArea txtArDescription;
        private JScrollPane scrPnDescription;
        private JLabel lblCode;
        DescriptionRenderer() {
            txtArDescription = new JTextArea();
            txtArDescription.setLineWrap(true);
            txtArDescription.setEditable(false);
            txtArDescription.setWrapStyleWord(true);
            txtArDescription.setFont(CoeusFontFactory.getNormalFont());
            txtArDescription.setOpaque(false);
//            if( !editable ) {
//                txtArDescription.setBackground((Color)UIManager.getDefaults().get("Panel.background"));
//                txtArDescription.setDisabledTextColor(Color.black);
//            }else{
//                txtArDescription.setBackground(Color.white);
//            }
            lblCode = new JLabel();
            lblCode.setFont(CoeusFontFactory.getNormalFont());
        } 
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if( column == CheckList.DESCRIPTION_COLUMN ){
                txtArDescription.setText(value.toString().trim());
                return txtArDescription;
            }
            lblCode.setText( value.toString().trim());
            return lblCode;
        }


    }//End DescriptionRenderer    
}//End CheckList

/** Table Model for CheckList.
 */


/*class DescriptionEditor  extends AbstractCellEditor implements TableCellEditor{
    private JTextArea txtArDescription;
    private JScrollPane scrPnDescription;
    private Object value;
    
    DescriptionEditor() {
        //super(new JComboBox());
        txtArDescription = new JTextArea();
        txtArDescription.setLineWrap(true);
        txtArDescription.setEditable(false);
        txtArDescription.setWrapStyleWord(true);
        txtArDescription.setFont(CoeusFontFactory.getNormalFont());
        scrPnDescription = new JScrollPane(txtArDescription);
        scrPnDescription.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnDescription.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }
    
    public java.awt.Component getTableCellEditorComponent(JTable jTable, Object value, boolean isSelected, int row, int column) {
        this.value = value;
        txtArDescription.setText(value.toString().trim());
        return scrPnDescription;
    }
       
    public int getClickCountToStart() {
        return 1;
    }
    
    public boolean stopCellEditing() {
        return true;
    }
       
    public Object getCellEditorValue() {
        return value;
    }
    
}//End DescriptionEditor*/

