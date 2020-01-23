/*
 * MassChangeForm.java
 *
 * Created on January 18, 2005, 3:39 PM
 */

package edu.mit.coeus.centraladmin.gui;


import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.centraladmin.bean.ModuleBean;
import edu.mit.coeus.centraladmin.bean.PersonTypeBean;
import edu.mit.coeus.departmental.gui.PersonDetailForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author  chandrashekara
 */
public class MassChangeForm extends javax.swing.JComponent
implements ActionListener,ListSelectionListener,ItemListener{
    private static final String EMPTY_STRING = "";
    private static final String WINDOW_TITLE = "Person Mass Change";
    private static final int WIDTH = 670;
    private static final int HEIGHT = 480;
    private static final char GET_MASS_CHANGE_DATA = 'A';
    private static final char GET_LIST_DATA = 'B';
    private static final char ROLODEX_DISPLAY_MODE='V';
    private static final String GET_SERVLET = "/PersonMassChangeServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private CoeusVector moduleData;
    private CoeusVector personTypeData;
    private CoeusVector filterTypeData;
    private ModuleBean moduleBean;
    private PersonTypeBean personTypeBean;
    private ModuleTableModel moduleTableModel;
    private PersonTypeTableModel personTypeTableModel;
    private ModuleTableCellRenderer moduleTableCellRenderer;
    private PersonTypeTableCellRenderer personTypeTableCellRenderer;
    private ModuleTableCellEditor moduleTableCellEditor;
    private PersonTypeTableCellEditor personTypeTableCellEditor;
    private static final int MODULE_CHECK_COL = 0;
    private static final int MODULE_COL = 1;
    private static final int PERSON_TYPE_CHECK_COL = 0;
    private static final int PERSON_TYPE_COL = 1;
    private CoeusDlgWindow dlgMassChange;
    private CoeusAppletMDIForm mdiForm;
    private CoeusVector cvSelectedModules;
    private CoeusMessageResources coeusMessageResources;
    /** Creates new form MassChangeForm */
    public MassChangeForm(CoeusAppletMDIForm mdiForm) throws CoeusException{
        this.mdiForm = mdiForm;
        initComponents();
        registerComponents();
        setColumnData();
        setFormData();
        postInitComponents();
    }
    
    private void registerComponents(){
        Component[] component = {rdBtnEmployee,rdBtnNonEmployee,btnReplaceSearch,btnReplaceClear,rdBtnWithEmployee,rdBtnWithNonEmployee,btnWithSearch,btnWithClear,btnShowList,
                            btnReplace,btnClose};
        
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        this.setFocusTraversalPolicy(policy);
        this.setFocusCycleRoot(true);
        
        ListSelectionModel moduleSelectionModel = tblModule.getSelectionModel();
        moduleSelectionModel.addListSelectionListener(this);
        tblModule.setSelectionModel(moduleSelectionModel);
        
        ListSelectionModel personTypeSelectionModel = tblPersonType.getSelectionModel();
        personTypeSelectionModel.addListSelectionListener(this);
        tblPersonType.setSelectionModel(personTypeSelectionModel);
        
        moduleTableModel = new ModuleTableModel();
        tblModule.setModel(moduleTableModel);
        personTypeTableModel = new PersonTypeTableModel();
        tblPersonType.setModel(personTypeTableModel);
        
        moduleTableCellRenderer = new ModuleTableCellRenderer();
        personTypeTableCellRenderer = new PersonTypeTableCellRenderer();
        moduleTableCellEditor = new ModuleTableCellEditor();
        personTypeTableCellEditor = new PersonTypeTableCellEditor();
        
        // Adding listeners
        btnReplaceClear.addActionListener(this);
        btnReplaceSearch.addActionListener(this);
        btnWithClear.addActionListener(this);
        btnWithSearch.addActionListener(this);
        btnShowList.addActionListener(this);
        btnReplace.addActionListener(this);
        btnClose.addActionListener(this);
        
        txtReplaceID.addMouseListener(new CustomMouseAdapter());
        txtReplaceName.addMouseListener(new CustomMouseAdapter());
        txtWithID.addMouseListener(new CustomMouseAdapter());
        txtWithName.addMouseListener(new CustomMouseAdapter());
        
        rdBtnEmployee.addItemListener(this);
        rdBtnNonEmployee.addItemListener(this);
        rdBtnWithEmployee.addItemListener(this);
        rdBtnWithNonEmployee.addItemListener(this);
        
        // Setting the colors
        if(!txtReplaceID.isEditable()){
            txtReplaceID.setBackground(Color.WHITE);
            txtReplaceID.setForeground(Color.black);
            txtReplaceID.setOpaque(true);
        }
        if(!txtReplaceName.isEditable()){
            txtReplaceName.setBackground(Color.WHITE);
            txtReplaceName.setForeground(Color.black);
            txtReplaceName.setOpaque(true);
        }
        
        if(!txtWithID.isEditable()){
            txtWithID.setBackground(Color.WHITE);
            txtWithID.setForeground(Color.black);
            txtWithID.setOpaque(true);
        }
        if(!txtWithName.isEditable()){
            txtWithName.setBackground(Color.WHITE);
            txtWithName.setForeground(Color.black);
            txtWithName.setOpaque(true);
        }
        
        
    }
    
    private void setColumnData(){
        // Setting header and properticies for the module table
        JTableHeader tableHeader = tblModule.getTableHeader();
        //tableHeader.setVisible(true);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblModule.setRowHeight(22);
        tblModule.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblModule.setShowGrid(false);
        tblModule.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        tblModule.setOpaque(true);
        
        // Setting header and properticies for the personType table
        JTableHeader personTableHeader = tblPersonType.getTableHeader();
        //personTableHeader.setVisible(true);
        personTableHeader.setReorderingAllowed(false);
        personTableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblPersonType.setRowHeight(22);
        tblPersonType.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblPersonType.setShowGrid(false);
        tblPersonType.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        tblPersonType.setOpaque(true);
        // Setting TableColumn propertices for the module table
        TableColumn column = tblModule.getColumnModel().getColumn(MODULE_CHECK_COL);
        column.setPreferredWidth(18);
        column.setCellEditor(moduleTableCellEditor);
        
        column.setCellRenderer(moduleTableCellRenderer);
        
        column= tblModule.getColumnModel().getColumn(MODULE_COL);
        column.setPreferredWidth(290);
        
        // Setting TableColumn propertices for the person Type table
        TableColumn typecolumn = tblPersonType.getColumnModel().getColumn(PERSON_TYPE_CHECK_COL);
        typecolumn.setPreferredWidth(18);
        typecolumn.setCellRenderer(personTypeTableCellRenderer);
        typecolumn.setCellEditor(personTypeTableCellEditor);
        
        typecolumn = tblPersonType.getColumnModel().getColumn(PERSON_TYPE_COL);
        typecolumn.setPreferredWidth(290);
        tblPersonType.setCellSelectionEnabled(false);
        tblPersonType.setOpaque(false);
    }
    
    public void setFormData() throws CoeusException{
        moduleData = new CoeusVector();
        personTypeData = new CoeusVector();
        filterTypeData = new CoeusVector();
        Hashtable massChangeData = getMassChangeData();
        moduleData = (CoeusVector)massChangeData.get(ModuleBean.class);
        personTypeData = (CoeusVector)massChangeData.get(PersonTypeBean.class);
        moduleTableModel.setData(moduleData);
    }
    
    private Hashtable getMassChangeData() throws CoeusException{
        Hashtable htData = null;
        RequesterBean requester;
        ResponderBean responder;
        requester = new RequesterBean();
        requester.setFunctionType(GET_MASS_CHANGE_DATA);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder!= null){
            if(responder.isSuccessfulResponse()){
                htData = (Hashtable)responder.getDataObject();
            }else{
                throw new CoeusException(responder.getMessage(),0);
            }
        }
        return htData;
    }
    
    private Hashtable prepareMassChangeData() {
        Hashtable htData = new Hashtable();
        htData.put("PERSON_ID", txtReplaceID.getText().trim());
        htData.put("PERSON_NAME_OLD", txtReplaceName.getText().trim());
        htData.put("EMPLOYEE", rdBtnEmployee.isSelected() ? "N" : "Y");
        htData.put("PERSON_ID_NEW", txtWithID.getText().trim());
        htData.put("PERSON_NAME_NEW", txtWithName.getText().trim());
        htData.put("EMPLOYEE_NEW", rdBtnWithEmployee.isSelected() ? "N" : "Y");
        Equals operator = new Equals("moduleSelected", true);
        cvSelectedModules = moduleData.filter(operator);
        htData.put(ModuleBean.class,cvSelectedModules);
        operator = new Equals("typeSelected", true);
        htData.put(PersonTypeBean.class, personTypeData.filter(operator));
        return htData;
    }
    
    private Hashtable getMassChangeListData() throws CoeusException{
        Hashtable htData = null;
        RequesterBean requester;
        ResponderBean responder;
        requester = new RequesterBean();
        requester.setFunctionType(GET_LIST_DATA);
        requester.setDataObject(prepareMassChangeData());
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder!= null){
            if(responder.isSuccessfulResponse()){
                htData = (Hashtable)responder.getDataObject();
            }else{
                throw new CoeusException(responder.getMessage(),0);
            }
        }
        return htData;
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        dlgMassChange.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try{
            if(source.equals(btnReplaceClear)){
                replaceClearAction();
            }else if(source.equals(btnReplaceSearch)){
                replaceSearchAction();
            }else if(source.equals(btnWithClear)){
                withClearAction();
            }else if(source.equals(btnWithSearch)){
                withSearchAction();
            }else if(source.equals(btnClose)){
                performCloseAction();
            }else if(source.equals(btnShowList)){
                showList(false);
            }else if(source.equals(btnReplace)){
                showList(true);
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally {
            dlgMassChange.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void showList(boolean showReplace) throws CoeusException{
        if(!validatePerson()) return;
        CoeusVector cvPersonData = new CoeusVector();
        cvPersonData.addElement(txtReplaceID.getText());
        cvPersonData.addElement(txtReplaceName.getText());
        cvPersonData.addElement(txtWithID.getText());
        cvPersonData.addElement(txtWithName.getText());
        Hashtable htData = prepareMassChangeData();
        MassChangeListReplaceWindow massChangeListReplaceWindow =
        new MassChangeListReplaceWindow(cvSelectedModules,htData,cvPersonData,showReplace);
    }
    
    private void replaceClearAction() throws CoeusException{
        txtReplaceID.setText(EMPTY_STRING);
        txtReplaceName.setText(EMPTY_STRING);
        canEnableBtns();
    }
    
    private void replaceSearchAction() throws CoeusException,Exception{
        if(rdBtnEmployee.isSelected()){
            CoeusSearch coeusSearch = new CoeusSearch(
            mdiForm, "PERSONSEARCH", 1);
            coeusSearch.showSearchWindow();
            HashMap personSelected = coeusSearch.getSelectedRow();
            if (personSelected != null && !personSelected.isEmpty() ) {
                
                String fullName = Utils.convertNull(personSelected.get(
                "FULL_NAME"));
                
                String personId=Utils.convertNull(personSelected.get(
                "PERSON_ID"));
                if (personId.length() > 0) {
                    txtReplaceID.setText(Utils.convertNull(personId));
                }
                
                if (fullName.length() > 0) {
                    txtReplaceName.setText(Utils.convertNull(fullName));
                }
            }
        }else if(rdBtnNonEmployee.isSelected()){
            CoeusSearch coeusSearch = new CoeusSearch(
            mdiForm, "ROLODEXSEARCH", 1);
            coeusSearch.showSearchWindow();
            String rolodexName = EMPTY_STRING;
            HashMap rolodexSelected = coeusSearch.getSelectedRow();
            if (rolodexSelected != null && !rolodexSelected.isEmpty() ) {
                String rolodexId = (Utils.convertNull(rolodexSelected.get(
                "ROLODEX_ID")));
                String firstName = Utils.convertNull(rolodexSelected.get(
                "FIRST_NAME"));
                String middleName = Utils.convertNull(rolodexSelected.get(
                "MIDDLE_NAME"));
                String lastName = Utils.convertNull(rolodexSelected.get(
                "LAST_NAME"));
                String prefix = Utils.convertNull(rolodexSelected.get(
                "PREFIX"));
                String suffix = Utils.convertNull(rolodexSelected.get(
                "SUFFIX"));
                if (lastName.length() > 0) {
                    rolodexName = (lastName + " "+ suffix +", "+
                    prefix + " "+ firstName + " "+ middleName).trim();
                }else{
                    rolodexName = Utils.convertNull(rolodexSelected.get("ORGANIZATION"));
                }
                txtReplaceName.setText(Utils.convertNull(rolodexName));
                txtReplaceID.setText(Utils.convert(rolodexId));
            }
        }
        canEnableBtns();
    }
    
    private void withClearAction() throws CoeusException{
        txtWithID.setText(EMPTY_STRING);
        txtWithName.setText(EMPTY_STRING);
        canEnableBtns();
    }
    
    private void withSearchAction() throws CoeusException,Exception{
        if(rdBtnWithEmployee.isSelected()){
            CoeusSearch coeusSearch = new CoeusSearch(
            mdiForm, "PERSONSEARCH", 1);
            coeusSearch.showSearchWindow();
            HashMap personSelected = coeusSearch.getSelectedRow();
            if (personSelected != null && !personSelected.isEmpty() ) {
                
                String fullName = Utils.convertNull(personSelected.get(
                "FULL_NAME"));
                
                String personId=Utils.convertNull(personSelected.get(
                "PERSON_ID"));
                if (personId.length() > 0) {
                    txtWithID.setText(Utils.convertNull(personId));
                }
                
                if (fullName.length() > 0) {
                    txtWithName.setText(Utils.convertNull(fullName));
                }
            }
        }else if(rdBtnWithNonEmployee.isSelected()){
            CoeusSearch coeusSearch = new CoeusSearch(
            mdiForm, "ROLODEXSEARCH", 1);
            coeusSearch.showSearchWindow();
            String rolodexName = EMPTY_STRING;
            HashMap rolodexSelected = coeusSearch.getSelectedRow();
            if (rolodexSelected != null && !rolodexSelected.isEmpty() ) {
                String rolodexId = (Utils.convertNull(rolodexSelected.get(
                "ROLODEX_ID")));
                String firstName = Utils.convertNull(rolodexSelected.get(
                "FIRST_NAME"));
                String middleName = Utils.convertNull(rolodexSelected.get(
                "MIDDLE_NAME"));
                String lastName = Utils.convertNull(rolodexSelected.get(
                "LAST_NAME"));
                String prefix = Utils.convertNull(rolodexSelected.get(
                "PREFIX"));
                String suffix = Utils.convertNull(rolodexSelected.get(
                "SUFFIX"));
                if (lastName.length() > 0) {
                    rolodexName = (lastName + " "+ suffix +", "+
                    prefix + " "+ firstName + " "+ middleName).trim();
                }else{
                    rolodexName = Utils.convertNull(rolodexSelected.get("ORGANIZATION"));
                }
                txtWithName.setText(Utils.convertNull(rolodexName));
                txtWithID.setText(Utils.convert(rolodexId));
            }
        }
        canEnableBtns();
    }
    private int moduleRow;
    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
//        if( !e.getValueIsAdjusting() ){
        if(e.getSource().equals(tblModule.getSelectionModel())) {
            int selectedRow = tblModule.getSelectedRow();
            if (selectedRow != -1) {
                moduleRow = selectedRow;
                filterPersonType(selectedRow);
                // selectModuleFlags(selectedRow);
            }
        }else if(e.getSource().equals(tblPersonType.getSelectionModel())) {
            checkSelPersonType(moduleRow);
        }
    }
    
    public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
        if(source.equals(rdBtnEmployee) || source.equals(rdBtnNonEmployee)) {
            txtReplaceID.setText(EMPTY_STRING);
            txtReplaceName.setText(EMPTY_STRING);
        }else if(source.equals(rdBtnWithEmployee) || source.equals(rdBtnWithNonEmployee)) {
            txtWithID.setText(EMPTY_STRING);
            txtWithName.setText(EMPTY_STRING);
        }
        canEnableBtns();
    }
    
    //    private void selectModuleFlags(int selRow){
    //        boolean checked = ((Boolean)tblModule.getValueAt(selRow, MODULE_CHECK_COL)).booleanValue();
    //        ModuleBean bean = (ModuleBean)moduleData.get(selRow);
    //        String moduleId = bean.getModuleCode();
    //        Equals eqModuleId= new Equals("moduleId",moduleId);
    //        CoeusVector data = personTypeData.filter(eqModuleId);
    //        CoeusVector dataObject = new CoeusVector();
    //        int index=0;
    //        if(data!= null){
    //            // if(!checked){
    //            for(index=0; index < data.size(); index++){
    //                PersonTypeBean personTypeBean = (PersonTypeBean)data.get(index);
    //                personTypeBean.setTypeSelected(!checked);
    //                dataObject.add(personTypeBean);
    //                personTypeTableModel.setData(dataObject);
    //            }
    //
    //            //                personTypeTableModel.fireTableRowsUpdated(personTypeBean,index);
    //            // }
    //        }
    //    }
    
    private void filterPersonType(int selRow){
        ModuleBean bean = (ModuleBean)moduleData.get(selRow);
        String moduleId = bean.getModuleCode();
        Equals eqModuleId= new Equals("moduleId",moduleId);
        filterTypeData = personTypeData.filter(eqModuleId);
        personTypeTableModel.setData(filterTypeData);
    }
    
    private void checkSelPersonType(int row){
//        int row = tblModule.getSelectedRow();
        if (row == -1) return;
        ModuleBean bean = (ModuleBean)moduleData.get(row);
        String moduleId = bean.getModuleCode();
        Equals eqModuleId= new Equals("typeSelected",true);
        if(filterTypeData.filter(eqModuleId).size()<=0) 
            bean.setModuleSelected(false);
        else 
            bean.setModuleSelected(true);
        moduleTableModel.fireTableDataChanged();
    }
    
    // Initialize and create the dialog box and set its properticies
    private void postInitComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        dlgMassChange = new CoeusDlgWindow(mdiForm,true);
        dlgMassChange.setResizable(false);
        dlgMassChange.setModal(true);
        dlgMassChange.getContentPane().add(this);
        dlgMassChange.setFont(CoeusFontFactory.getLabelFont());
        dlgMassChange.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgMassChange.setSize(WIDTH, HEIGHT);
        dlgMassChange.setTitle(WINDOW_TITLE);
        // This method will place the dialog box at the center of the screen
        dlgMassChange.setLocationRelativeTo(mdiForm);
        dlgMassChange.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we) {
                performCloseAction();
            }
        });
        
        dlgMassChange.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                performCloseAction();
            }
        });
        
        dlgMassChange.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
        canEnableBtns();
    }
    
    private void canEnableBtns() {
        boolean enable = true;
        if(EMPTY_STRING.equals(txtReplaceID.getText().trim()) ||
                EMPTY_STRING.equals(txtWithID.getText().trim()))
          enable = false;
        else enable = true;
        btnShowList.setEnabled(enable);
        btnReplace.setEnabled(enable);
    }
    
    private void performCloseAction(){
        dlgMassChange.setVisible(false);
    }
    
    private void setWindowFocus(){
        rdBtnEmployee.requestFocusInWindow();
    }
    
    public void display(){
        if(tblModule.getRowCount() > 0){
            tblModule.addRowSelectionInterval(0,0);
        }
        rdBtnEmployee.setSelected(true);
        rdBtnWithEmployee.setSelected(true);
        dlgMassChange.setVisible(true);
    }
    
    public class ModuleTableModel extends AbstractTableModel{
        private String colNames[] = {"",""};
        private Class cloClass[] = {Boolean.class,String.class};
        
        public boolean isCellEditable(int row, int col){
            if(col==MODULE_CHECK_COL){
                return true;
            }else{
                return false;
            }
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public String getColumnName(int col){
            return colNames[col];
        }
        
        public Class getColumnClass(int col){
            return cloClass[col];
        }
        
        public int getRowCount() {
            if(moduleData==null){
                return 0;
            }else{
                return moduleData.size();
            }
        }
        
        public void setData(CoeusVector moduleData){
            moduleData = moduleData;
        }
        
        public Object getValueAt(int row, int col) {
            ModuleBean bean = (ModuleBean)moduleData.get(row);
            switch(col){
                case MODULE_CHECK_COL:
                    return new Boolean(bean.isModuleSelected());
                case MODULE_COL:
                    return bean.getModuleName();
            }
            return EMPTY_STRING;
        }
        
        public void setValueAt(Object value, int row, int col){
            ModuleBean bean = (ModuleBean)moduleData.get(row);
            switch(col){
                case MODULE_CHECK_COL:
                    if(value != null && ((Boolean)value).booleanValue() != bean.isModuleSelected()) {
                        Boolean required = (Boolean)value;
                        bean.setModuleSelected(required.booleanValue());
                    }
            }
            fireTableDataChanged();
        }
    }
    
    
    public class PersonTypeTableModel extends AbstractTableModel{
        private String colNames[] = {"",""};
        private Class cloClass[] = {Boolean.class,String.class};
        
        public boolean isCellEditable(int row, int col){
            if(col==PERSON_TYPE_CHECK_COL){
                return true;
            }else{
                return false;
            }
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public String getColumnName(int col){
            return colNames[col];
        }
        
        public Class getColumnClass(int col){
            return cloClass[col];
        }
        
        public int getRowCount() {
            if(filterTypeData==null){
                return 0;
            }else{
                return filterTypeData.size();
            }
        }
        
        public void setData(CoeusVector filterTypeData){
            filterTypeData = filterTypeData;
            fireTableDataChanged();
        }
        
        public Object getValueAt(int row, int col) {
            PersonTypeBean personTypeBean = (PersonTypeBean)filterTypeData.get(row);
            switch(col){
                case PERSON_TYPE_CHECK_COL:
                    return new Boolean(personTypeBean.isTypeSelected());
                case PERSON_TYPE_COL:
                    return personTypeBean.getTypeDescription();
            }
            return EMPTY_STRING;
        }
        
        public void setValueAt(Object value, int row, int col){
            PersonTypeBean bean = (PersonTypeBean)filterTypeData.get(row);
            switch(col){
                case PERSON_TYPE_CHECK_COL:
                    if(value != null && ((Boolean)value).booleanValue() != bean.isTypeSelected()) {
                        Boolean required = (Boolean)value;
                        bean.setTypeSelected(required.booleanValue());
                    }
            }
            fireTableDataChanged();
        }
    }
    
    public class ModuleTableCellRenderer extends DefaultTableCellRenderer
    implements TableCellRenderer{
        private JCheckBox chkValue;
        
        public ModuleTableCellRenderer(){
            chkValue = new JCheckBox();
        }
        
        public java.awt.Component getTableCellRendererComponent(
        javax.swing.JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
            switch(column){
                case MODULE_CHECK_COL:
                    chkValue.setSelected(((Boolean)value).booleanValue());
                    return chkValue;
                case MODULE_COL:
                    if(value!= null && (!value.toString().equals(EMPTY_STRING))){
                        setText(value.toString());
                    }
                    
            }
            return chkValue;
        }
    }
    
    public class PersonTypeTableCellRenderer extends DefaultTableCellRenderer
    implements TableCellRenderer{
        private JCheckBox chkValue;
        
        public PersonTypeTableCellRenderer(){
            chkValue = new JCheckBox();
        }
        
        public java.awt.Component getTableCellRendererComponent(
        javax.swing.JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
            switch(column){
                case PERSON_TYPE_CHECK_COL:
                    chkValue.setSelected(((Boolean)value).booleanValue());
                    return chkValue;
                case PERSON_TYPE_COL:
                    if(value!= null && (!value.toString().equals(EMPTY_STRING))){
                        setText(value.toString());
                    }
                    
            }
            return chkValue;
        }
    }
    
    public class ModuleTableCellEditor extends AbstractCellEditor
    implements TableCellEditor,MouseListener,ItemListener{
        private JCheckBox chkComp;
        private int column;
        private int selRow;
        
        public ModuleTableCellEditor(){
            chkComp = new JCheckBox();
            chkComp.addItemListener(this);
            chkComp.addMouseListener(this);
        }
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            selRow = row;
            switch(column){
                case MODULE_CHECK_COL:
                    chkComp.setSelected(((Boolean)value).booleanValue());
                    return chkComp;
            }
            return chkComp;
        }
        public Object getCellEditorValue() {
            switch(column){
                case MODULE_CHECK_COL:
                    return new Boolean(chkComp.isSelected());
            }
            return chkComp;
        }
        // im[plemted to check all the check boxes in the person type
        //once a module is checked.
        public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
//            Object source = mouseEvent.getSource();
//            int selRow = tblModule.getSelectedRow();
//            if(source.equals(chkComp)){
//                boolean checked  = chkComp.isSelected();
//                ModuleBean bean = (ModuleBean)moduleData.get(selRow);
//                String moduleId = bean.getModuleCode();
//                Equals eqModuleId= new Equals("moduleId",moduleId);
//                CoeusVector data = personTypeData.filter(eqModuleId);
//                int index=0;
//                if(data!= null){
//                    CoeusVector dataObject = new CoeusVector();
//                    PersonTypeBean personTypeBean = null;
//                    for(index=0; index < data.size(); index++){
//                        personTypeBean = (PersonTypeBean)data.get(index);
//                        personTypeBean.setTypeSelected(checked);
//                    }
//                    dataObject.add(personTypeBean);
//                    personTypeTableModel.setData(dataObject);
//                }
//            }
        }
        
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }
        
        public void mouseExited(java.awt.event.MouseEvent e) {
        }
        
        public void mousePressed(java.awt.event.MouseEvent e) {
        }
        
        public void mouseReleased(java.awt.event.MouseEvent e) {
        }
        
        public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
            this.stopCellEditing();
            Object source = itemEvent.getSource();
//            int selRow = tblModule.getSelectedRow();
            if(source.equals(chkComp)){
                boolean checked  = chkComp.isSelected();
                ModuleBean bean = (ModuleBean)moduleData.get(selRow);
                String moduleId = bean.getModuleCode();
                Equals eqModuleId= new Equals("moduleId",moduleId);
                CoeusVector data = personTypeData.filter(eqModuleId);
                int index=0;
//                if(data!= null){
//                    CoeusVector dataObject = new CoeusVector();
                    PersonTypeBean personTypeBean = null;
                    for(index=0; index < data.size(); index++){
                        personTypeBean = (PersonTypeBean)data.get(index);
                        personTypeBean.setTypeSelected(checked);
                    }
//                    dataObject.add(personTypeBean);
                    personTypeTableModel.setData(data);
//                }
            }
        }
        
    }
    
    public class PersonTypeTableCellEditor extends AbstractCellEditor
    implements TableCellEditor,ItemListener{
        private JCheckBox chkComp;
        private int column;
        public PersonTypeTableCellEditor(){
            chkComp = new JCheckBox();
            chkComp.addItemListener(this);
            chkComp.setHorizontalAlignment(JCheckBox.LEFT);
        }
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case PERSON_TYPE_CHECK_COL:
                    chkComp.setSelected(((Boolean)value).booleanValue());
                    return chkComp;
            }
            return chkComp;
        }
        public Object getCellEditorValue() {
            switch(column){
                case PERSON_TYPE_CHECK_COL:
                    return new Boolean(chkComp.isSelected());
            }
            return chkComp;
        }
        
        public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
            this.stopCellEditing();
        }
        
    }
    
    private boolean validatePerson() {
        Equals operator = new Equals("moduleSelected", true);
//        CoeusVector cvSelMod = personTypeData.filter(operator);
//        int size = moduleData.filter(operator).size();
        if(moduleData.filter(operator).size() <= 0 ) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("personmasschange_exceptionCode.1002"));
            return false;
        }
        operator = new Equals("typeSelected", true);
        CoeusVector cvSelpersonTypeData = personTypeData.filter(operator);
        if(cvSelpersonTypeData.size() <= 0 ) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("personmasschange_exceptionCode.1003"));
            return false;
        }
        operator = new Equals("replaceType", "R");
        CoeusVector cvFilteredData = cvSelpersonTypeData.filter(operator);
        StringBuffer sb = new StringBuffer();
        boolean valid = true;
        if(rdBtnEmployee.isSelected() || rdBtnWithEmployee.isSelected()) {
            for (int index=0; index<cvFilteredData.size();index++) {
                PersonTypeBean pBean = (PersonTypeBean)cvFilteredData.get(index);
                valid = false;
                String moduleId = pBean.getModuleId();
                Equals eqModuleFilter = new Equals("moduleCode",moduleId);
                CoeusVector cvModule = moduleData.filter(eqModuleFilter);
                ModuleBean currentModuleBean = (ModuleBean) cvModule.get(0);
                String moduleName = currentModuleBean.getModuleName();
                sb.append(moduleName +" "+pBean.getTypeDescription()+" "+ coeusMessageResources.parseMessageKey("personmasschange_exceptionCode.1004")+" Person.\n");
            }
        }
        
        operator = new Equals("replaceType", "P");
        cvFilteredData = cvSelpersonTypeData.filter(operator);
        if(!rdBtnEmployee.isSelected() || !rdBtnWithEmployee.isSelected()) {
            for (int index=0; index<cvFilteredData.size();index++) {
                PersonTypeBean pBean = (PersonTypeBean)cvFilteredData.get(index);
                valid = false;
                String moduleId = pBean.getModuleId();
                Equals eqModuleFilter = new Equals("moduleCode",moduleId);
                CoeusVector cvModule = moduleData.filter(eqModuleFilter);
                ModuleBean currentModuleBean = (ModuleBean) cvModule.get(0);
                String moduleName = currentModuleBean.getModuleName();
                sb.append(moduleName+" "+pBean.getTypeDescription()+" "+coeusMessageResources.parseMessageKey("personmasschange_exceptionCode.1004")+ " Rolodex. \n");
            }
        }
        if (!valid) {
            CoeusOptionPane.showInfoDialog(sb.toString());
            return valid;
        }
        
        if(txtReplaceID.getText().trim().equals(txtWithID.getText().trim())) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("personmasschange_exceptionCode.1001"));
            return false;
        }
        return true;
    }
    
    /**
     * Mouse adapter class which handles double clicks on revewer text
     */
    public class CustomMouseAdapter extends MouseAdapter {
        public void mouseClicked(MouseEvent me){
            Object source = me.getSource();
            if(txtReplaceID.getText().trim().equals(EMPTY_STRING) &&
            txtReplaceName.getText().trim().equals(EMPTY_STRING) &&
            txtWithID.getText().trim().equals(EMPTY_STRING) &&
            txtWithName.getText().trim().equals(EMPTY_STRING)){
                return;
            }
            if( me.getClickCount()!= 2 )return ;
            dlgMassChange.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            String loginUserName = CoeusGuiConstants.getMDIForm().getUserName();
            try{
                if(source.equals(txtReplaceID) || source.equals(txtReplaceName)){
                    if(rdBtnEmployee.isSelected()){
                        
                        new PersonDetailForm(txtReplaceID.getText().trim(),loginUserName,'D');
                    }else if(rdBtnNonEmployee.isSelected()){
                        RolodexMaintenanceDetailForm rolodexMaintenanceDetailForm=new RolodexMaintenanceDetailForm(ROLODEX_DISPLAY_MODE,String.valueOf(txtReplaceID.getText().trim()));
                        rolodexMaintenanceDetailForm.showForm(mdiForm,"Display Rolodex",true);
                    }
                }else if(source.equals(txtWithID) || source.equals(txtWithName)){
                    if(rdBtnWithEmployee.isSelected()){
                        new PersonDetailForm(txtWithID.getText().trim(),loginUserName,'D');
                    }else if(rdBtnWithNonEmployee.isSelected()){
                        RolodexMaintenanceDetailForm rolodexMaintenanceDetailForm=new RolodexMaintenanceDetailForm(ROLODEX_DISPLAY_MODE,String.valueOf(txtWithID.getText().trim()));
                        rolodexMaintenanceDetailForm.showForm(mdiForm,"Display Rolodex",true);
                    }
                }
            }catch ( Exception e) {
                e.printStackTrace();
                CoeusOptionPane.showInfoDialog( e.getMessage() );
            }finally{
                dlgMassChange.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        btnGrpReplace = new javax.swing.ButtonGroup();
        btnGrpWith = new javax.swing.ButtonGroup();
        lblheader = new javax.swing.JLabel();
        pnlReplace = new javax.swing.JPanel();
        rdBtnEmployee = new javax.swing.JRadioButton();
        rdBtnNonEmployee = new javax.swing.JRadioButton();
        lblReplaceId = new javax.swing.JLabel();
        lblReplaceName = new javax.swing.JLabel();
        txtReplaceID = new edu.mit.coeus.utils.CoeusTextField();
        txtReplaceName = new edu.mit.coeus.utils.CoeusTextField();
        btnReplaceSearch = new javax.swing.JButton();
        btnReplaceClear = new javax.swing.JButton();
        pnlWith = new javax.swing.JPanel();
        rdBtnWithEmployee = new javax.swing.JRadioButton();
        rdBtnWithNonEmployee = new javax.swing.JRadioButton();
        lblWithID = new javax.swing.JLabel();
        lblWithName = new javax.swing.JLabel();
        txtWithID = new edu.mit.coeus.utils.CoeusTextField();
        txtWithName = new edu.mit.coeus.utils.CoeusTextField();
        btnWithSearch = new javax.swing.JButton();
        btnWithClear = new javax.swing.JButton();
        pnlChange = new javax.swing.JPanel();
        scrPnChangeModule = new javax.swing.JScrollPane();
        tblModule = new javax.swing.JTable();
        scrPnPersonType = new javax.swing.JScrollPane();
        tblPersonType = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        btnShowList = new javax.swing.JButton();
        btnReplace = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblheader.setFont(CoeusFontFactory.getLabelFont());
        lblheader.setText("This function allows to replace a person with another");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        add(lblheader, gridBagConstraints);

        pnlReplace.setLayout(new java.awt.GridBagLayout());

        pnlReplace.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Replace the Person", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        rdBtnEmployee.setFont(CoeusFontFactory.getLabelFont());
        rdBtnEmployee.setText("Employee");
        btnGrpReplace.add(rdBtnEmployee);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 0);
        pnlReplace.add(rdBtnEmployee, gridBagConstraints);

        rdBtnNonEmployee.setFont(CoeusFontFactory.getLabelFont());
        rdBtnNonEmployee.setText("Non Employee");
        btnGrpReplace.add(rdBtnNonEmployee);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        pnlReplace.add(rdBtnNonEmployee, gridBagConstraints);

        lblReplaceId.setFont(CoeusFontFactory.getLabelFont());
        lblReplaceId.setText("ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        pnlReplace.add(lblReplaceId, gridBagConstraints);

        lblReplaceName.setFont(CoeusFontFactory.getLabelFont());
        lblReplaceName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        pnlReplace.add(lblReplaceName, gridBagConstraints);

        txtReplaceID.setEditable(false);
        txtReplaceID.setMinimumSize(new java.awt.Dimension(160, 19));
        txtReplaceID.setPreferredSize(new java.awt.Dimension(160, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 3, 0);
        pnlReplace.add(txtReplaceID, gridBagConstraints);

        txtReplaceName.setEditable(false);
        txtReplaceName.setMinimumSize(new java.awt.Dimension(400, 19));
        txtReplaceName.setPreferredSize(new java.awt.Dimension(400, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 3, 0);
        pnlReplace.add(txtReplaceName, gridBagConstraints);

        btnReplaceSearch.setFont(CoeusFontFactory.getLabelFont());
        btnReplaceSearch.setMnemonic('e');
        btnReplaceSearch.setText("Search");
        btnReplaceSearch.setMaximumSize(new java.awt.Dimension(75, 24));
        btnReplaceSearch.setMinimumSize(new java.awt.Dimension(75, 24));
        btnReplaceSearch.setPreferredSize(new java.awt.Dimension(75, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlReplace.add(btnReplaceSearch, gridBagConstraints);

        btnReplaceClear.setFont(CoeusFontFactory.getLabelFont());
        btnReplaceClear.setMnemonic('a');
        btnReplaceClear.setText("Clear");
        btnReplaceClear.setMaximumSize(new java.awt.Dimension(75, 24));
        btnReplaceClear.setMinimumSize(new java.awt.Dimension(75, 24));
        btnReplaceClear.setPreferredSize(new java.awt.Dimension(75, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlReplace.add(btnReplaceClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 4);
        add(pnlReplace, gridBagConstraints);

        pnlWith.setLayout(new java.awt.GridBagLayout());

        pnlWith.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "With...", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        rdBtnWithEmployee.setFont(CoeusFontFactory.getLabelFont());
        rdBtnWithEmployee.setText("Employee");
        btnGrpWith.add(rdBtnWithEmployee);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        pnlWith.add(rdBtnWithEmployee, gridBagConstraints);

        rdBtnWithNonEmployee.setFont(CoeusFontFactory.getLabelFont());
        rdBtnWithNonEmployee.setText("Non Employee");
        btnGrpWith.add(rdBtnWithNonEmployee);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        pnlWith.add(rdBtnWithNonEmployee, gridBagConstraints);

        lblWithID.setFont(CoeusFontFactory.getLabelFont());
        lblWithID.setText("ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlWith.add(lblWithID, gridBagConstraints);

        lblWithName.setFont(CoeusFontFactory.getLabelFont());
        lblWithName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlWith.add(lblWithName, gridBagConstraints);

        txtWithID.setEditable(false);
        txtWithID.setMinimumSize(new java.awt.Dimension(160, 19));
        txtWithID.setPreferredSize(new java.awt.Dimension(160, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 1, 0);
        pnlWith.add(txtWithID, gridBagConstraints);

        txtWithName.setEditable(false);
        txtWithName.setMinimumSize(new java.awt.Dimension(400, 19));
        txtWithName.setPreferredSize(new java.awt.Dimension(400, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 0);
        pnlWith.add(txtWithName, gridBagConstraints);

        btnWithSearch.setFont(CoeusFontFactory.getLabelFont());
        btnWithSearch.setMnemonic('r');
        btnWithSearch.setText("Search");
        btnWithSearch.setMaximumSize(new java.awt.Dimension(75, 24));
        btnWithSearch.setMinimumSize(new java.awt.Dimension(75, 24));
        btnWithSearch.setPreferredSize(new java.awt.Dimension(75, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlWith.add(btnWithSearch, gridBagConstraints);

        btnWithClear.setFont(CoeusFontFactory.getLabelFont());
        btnWithClear.setMnemonic('l');
        btnWithClear.setText("Clear");
        btnWithClear.setMaximumSize(new java.awt.Dimension(75, 24));
        btnWithClear.setMinimumSize(new java.awt.Dimension(75, 24));
        btnWithClear.setPreferredSize(new java.awt.Dimension(75, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlWith.add(btnWithClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 4);
        add(pnlWith, gridBagConstraints);

        pnlChange.setLayout(new java.awt.GridLayout(1, 0, 0, 2));

        scrPnChangeModule.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Module", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnChangeModule.setMinimumSize(new java.awt.Dimension(320, 150));
        scrPnChangeModule.setPreferredSize(new java.awt.Dimension(320, 150));
        tblModule.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblModule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scrPnChangeModule.setViewportView(tblModule);

        pnlChange.add(scrPnChangeModule);

        scrPnPersonType.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Person Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrPnPersonType.setMinimumSize(new java.awt.Dimension(325, 150));
        scrPnPersonType.setPreferredSize(new java.awt.Dimension(325, 150));
        tblPersonType.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblPersonType.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scrPnPersonType.setViewportView(tblPersonType);

        pnlChange.add(scrPnPersonType);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        add(pnlChange, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnShowList.setFont(CoeusFontFactory.getLabelFont());
        btnShowList.setMnemonic('S');
        btnShowList.setText("Show List");
        btnShowList.setMaximumSize(new java.awt.Dimension(92, 26));
        btnShowList.setMinimumSize(new java.awt.Dimension(92, 26));
        btnShowList.setPreferredSize(new java.awt.Dimension(92, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlButtons.add(btnShowList, gridBagConstraints);

        btnReplace.setFont(CoeusFontFactory.getLabelFont());
        btnReplace.setMnemonic('p');
        btnReplace.setText("Replace");
        btnReplace.setMinimumSize(new java.awt.Dimension(90, 26));
        btnReplace.setPreferredSize(new java.awt.Dimension(90, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlButtons.add(btnReplace, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(pnlButtons, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMinimumSize(new java.awt.Dimension(90, 26));
        btnClose.setPreferredSize(new java.awt.Dimension(90, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        add(btnClose, gridBagConstraints);

    }//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.ButtonGroup btnGrpReplace;
    public javax.swing.ButtonGroup btnGrpWith;
    public javax.swing.JButton btnReplace;
    public javax.swing.JButton btnReplaceClear;
    public javax.swing.JButton btnReplaceSearch;
    public javax.swing.JButton btnShowList;
    public javax.swing.JButton btnWithClear;
    public javax.swing.JButton btnWithSearch;
    public javax.swing.JLabel lblReplaceId;
    public javax.swing.JLabel lblReplaceName;
    public javax.swing.JLabel lblWithID;
    public javax.swing.JLabel lblWithName;
    public javax.swing.JLabel lblheader;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JPanel pnlChange;
    public javax.swing.JPanel pnlReplace;
    public javax.swing.JPanel pnlWith;
    public javax.swing.JRadioButton rdBtnEmployee;
    public javax.swing.JRadioButton rdBtnNonEmployee;
    public javax.swing.JRadioButton rdBtnWithEmployee;
    public javax.swing.JRadioButton rdBtnWithNonEmployee;
    public javax.swing.JScrollPane scrPnChangeModule;
    public javax.swing.JScrollPane scrPnPersonType;
    public javax.swing.JTable tblModule;
    public javax.swing.JTable tblPersonType;
    public edu.mit.coeus.utils.CoeusTextField txtReplaceID;
    public edu.mit.coeus.utils.CoeusTextField txtReplaceName;
    public edu.mit.coeus.utils.CoeusTextField txtWithID;
    public edu.mit.coeus.utils.CoeusTextField txtWithName;
    // End of variables declaration//GEN-END:variables
    
}
