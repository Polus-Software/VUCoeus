/*
 * SubcontractForm.java
 *
 * Created on December 24, 2004, 4:00 PM
 */

package edu.mit.coeus.centraladmin.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.subcontract.bean.RTFFormBean;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusVector;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Cursor;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.table.*;


/**
 *
 * @author  chandrashekara
 */
public class SubcontractForm extends javax.swing.JComponent implements ActionListener {
    private static final String EMPTY_STRING = "";
    // Modified for COEUSQA-1412 Subcontract Module changes - Start
//    private static final int WIDTH = 605;
    private static final int WIDTH = 800;
    // Modified for COEUSQA-1412 Subcontract Module changes - End
    private static final int HEIGHT = 300;
    private static final String WINDOW_TITLE = "Subcontract Forms";
    private final String DATA_SERVLET ="/SubcontractMaintenenceServlet";
    private final String UPDATE_SERVLET ="/CentralAdminMaintenanceServlet";
    private static final char UPDATE_DATA ='E';
    private final String connect = CoeusGuiConstants.CONNECTION_URL+ UPDATE_SERVLET;
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ DATA_SERVLET;
    private static final char GET_DATA ='J';
    private static final char UPDATE_TEMPLATE = 'F'; 
    private static final char GET_TEMPLATE_DATA = 'Z';
    private CoeusAppletMDIForm mdiForm;
    private CoeusMessageResources coeusMessageResources;
    private CoeusDlgWindow dlgSubcontractForm;
    private String selectedTarget;
    private CoeusVector cvData;
    private FormTableModel formTableModel;
    private static final int FORM_ID_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    // Added for COEUSQA-1412 Subcontract Module changes - Start
    private static final int TEMPLATE_TYPE_COLUMN = 2;
    // Added for COEUSQA-1412 Subcontract Module changes - End
    private QueryEngine queryEngine;
    private boolean acType;
    private boolean clickedAction;
    private CoeusFileChooser fileChooser;
    private byte [] templateData = null;
    private boolean fileSelected = false; /* To check whether file has been selected or not */
    private int LOAD_CLICKED = 1;
    private static final String SELECT_ROW = "search_exceptionCode.1119";
    private static final String DELETE_ROW = "budget_common_exceptionCode.1007";
    
    /** Creates new form SubcontractForm */
    public SubcontractForm(CoeusAppletMDIForm mdiForm)  throws CoeusClientException{
        this.mdiForm = mdiForm;
        initComponents();
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        postInitComponents();
        registerComponents();
        setFormData();
        setColumnData();
        
    }
    /*to register the components and set the focus traversal*/
    private void registerComponents(){
        formTableModel = new FormTableModel();
        tblForms.setModel(formTableModel);
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        btnClose.addActionListener(this);
        btnModify.addActionListener(this);
        btnUpLoad.addActionListener(this);
        btnDownLoad.addActionListener(this);
        java.awt.Component[] component = {scrPnForms,btnAdd,btnDelete,btnModify,btnUpLoad,btnDownLoad,btnClose};
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        setFocusTraversalPolicy(policy);
        setFocusCycleRoot(true);
    }
    
    // Sets the form data by making a server call
    private void setFormData() throws CoeusClientException{
        if(cvData== null){
            cvData = new CoeusVector();
        }
        cvData = getFormData();
        formTableModel.setData(cvData);
    }
    
    // Communicate with the server to get the details regarding the form data
    private CoeusVector getFormData() throws CoeusClientException{
        CoeusVector data=null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_DATA);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                data = (CoeusVector)responderBean.getDataObject();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        return data;
    }
    
    // Sets the column width and table header.
    private void setColumnData(){
        JTableHeader tableHeader = tblForms.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        tblForms.setRowHeight(22);
        tblForms.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        tblForms.setOpaque(false);
        tblForms.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column = tblForms.getColumnModel().getColumn(FORM_ID_COLUMN);
        
        column.setPreferredWidth(200);
        column.setResizable(true);
        
        column = tblForms.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        column.setPreferredWidth(295);
        column.setResizable(true);
        
        // Added for COEUSQA-1412 Subcontract Module changes - Start        
        column = tblForms.getColumnModel().getColumn(TEMPLATE_TYPE_COLUMN);
        column.setPreferredWidth(185);
        column.setResizable(true);
        // Added for COEUSQA-1412 Subcontract Module changes - End

    }
    
    // Initialize and create the dialog box and set its properticies
    private void postInitComponents() {
        dlgSubcontractForm = new CoeusDlgWindow(mdiForm,true);
        dlgSubcontractForm.setResizable(false);
        dlgSubcontractForm.setModal(true);
        dlgSubcontractForm.getContentPane().add(this);
        dlgSubcontractForm.setFont(CoeusFontFactory.getLabelFont());
        dlgSubcontractForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgSubcontractForm.setSize(WIDTH, HEIGHT);
        dlgSubcontractForm.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgSubcontractForm.getSize();
        dlgSubcontractForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgSubcontractForm.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                performCloseAction();
                return;
            }
        });
        
        dlgSubcontractForm.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCloseAction();
                return;
            }
        });
        
        dlgSubcontractForm.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    /*the actions performed on the click of the buttons in the window*/
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            dlgSubcontractForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if(source.equals(btnClose)){
                performCloseAction();
            }else if(source.equals(btnDelete)){
                performDeleteAction();
            }else if(source.equals(btnAdd)){
                clickedAction = true;
                performAddAction();
                clickedAction = false;
            }else if(source.equals(btnModify)){
                clickedAction = false;
                performModifyAction();
            }else if(source.equals(btnUpLoad)){
                performLoadAction();
            }else if(source.equals(btnDownLoad)){
                performDownloadAction();
            }
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }
        finally{
            dlgSubcontractForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /*the action performed on the click of the load button*/
    private void performLoadAction() throws CoeusClientException{
        int selRow = tblForms.getSelectedRow();
        String formId = null;
        CoeusVector cvTempData = new CoeusVector();
        if(selRow == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ROW) + "  " );
            return;
        }
        if(selRow!=-1){
            RTFFormBean bean = (RTFFormBean)cvData.get(selRow);
            formId = bean.getFormId();
            Equals eqId = new Equals("formId",bean.getFormId());
            cvTempData = cvData.filter(eqId);
            UpLoadTemplate upLoadTemplate = new UpLoadTemplate(mdiForm,cvTempData);
            upLoadTemplate.txtId.setEditable(false);
            upLoadTemplate.txtDescription.setEditable(false);
            upLoadTemplate.txtTemplate.setText(bean.getFormDescription());
            upLoadTemplate.cmbTemplateType.setEnabled(false);
            upLoadTemplate.registerComponents(false);
            upLoadTemplate.setFormData(clickedAction , LOAD_CLICKED);
            if(upLoadTemplate.display()==1){
                RTFFormBean rtfRomBean = null;
                rtfRomBean = upLoadTemplate.setRTFBeanData();
                rtfRomBean.setAcType("U");
                updateTemplateData(rtfRomBean);
                clickedAction = false;
            }
        }
    }
    
    /*the action performed on the click of the download button*/
    private void performDownloadAction() throws CoeusClientException{
        int selRow = tblForms.getSelectedRow();
        ByteArrayInputStream templateData = null;
        if(selRow == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ROW) + "  ");
            return;
        }
        if(selRow != -1){
            RTFFormBean bean = (RTFFormBean)cvData.get(selRow);
            String formId = bean.getFormId();
            byte[] byteData = getTemplateData(formId);
            fileChooser = new CoeusFileChooser(dlgSubcontractForm); /* Creating an instance of fileChooser */
            String fileName = bean.getDescription();
            // Select the file which has to be renamed
            File selFile = new File(fileName);
            fileChooser.setSelectedFileName(selFile);
            int selection = fileChooser.showSaveDialog(dlgSubcontractForm);
            if(selection == JFileChooser.APPROVE_OPTION) {
                //clicked on Save
                try{
                    File file = fileChooser.getFileName();
                    if(!file.getPath().endsWith(".xsl")){
                        String name = file.getPath();
                        name = name.concat(".xsl");
                        FileOutputStream fileOutputStream = new FileOutputStream(name);
                        fileOutputStream.write(byteData);
                        fileOutputStream.close();
                    }else{
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(byteData);
                        fileOutputStream.close();
                    }
                }catch (FileNotFoundException fileNotFoundException) {
                    CoeusOptionPane.showErrorDialog(fileNotFoundException.getMessage());
                }catch (IOException iOException) {
                    CoeusOptionPane.showErrorDialog(iOException.getMessage());
                }
            }
            
        }
        
    }
    
    /*to get the template for the particular form id*/
    private byte[] getTemplateData(String formId) throws CoeusClientException{
        //        ByteArrayInputStream data = null;
        //String data = null;
        byte [] data = null;
        try{
            RequesterBean requester = new RequesterBean();
            ResponderBean responder = new ResponderBean();
            requester.setFunctionType('Z');
            requester.setDataObject(formId);
            
            AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, requester);
            
            comm.send();
            responder = comm.getResponse();
            if(responder.isSuccessfulResponse()){
                data = (byte [])responder.getDataObject();
                
            }else{
                throw new CoeusClientException(responder.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
            
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return data;
    }
    
    // Displays the form which encapsulated in dialog box
    public void display(){
        if(tblForms.getRowCount() > 0){
        tblForms.setRowSelectionInterval(0,0);
        }
        dlgSubcontractForm.setVisible(true);
    }
    
    
    // While adding  a row check for the duplicate and add a new template
    private void performAddAction() throws CoeusClientException{
        UpLoadTemplate upLoadTemplate = new UpLoadTemplate(mdiForm,cvData, true);
        upLoadTemplate.registerComponents(false);
        upLoadTemplate.setFormData(clickedAction , 0);
        if(upLoadTemplate.display()==1){
            RTFFormBean rtfRomBean = null;
            rtfRomBean = upLoadTemplate.setRTFBeanData();
            rtfRomBean.setAcType("I");
            updateTemplateData(rtfRomBean);
            clickedAction = false;
        }
         
    }
    
    
    // While adding  a row check for the duplicate and add a new template
    private void performModifyAction() throws CoeusClientException{
        int selRow = tblForms.getSelectedRow();
        String formId = null;
        CoeusVector cvTempData = new CoeusVector();
        if(selRow == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ROW) + "  ");
            return;
        }
        if(selRow!=-1){
            RTFFormBean bean = (RTFFormBean)cvData.get(selRow);
            formId = bean.getFormId();
            Equals eqId = new Equals("formId",bean.getFormId());
            cvTempData = cvData.filter(eqId);
            UpLoadTemplate upLoadTemplate = new UpLoadTemplate(mdiForm,cvTempData,true,cvData);         
            upLoadTemplate.btnBrowse.setVisible(false);
            upLoadTemplate.lblTemplate.setVisible(false);
            upLoadTemplate.txtTemplate.setVisible(false);
            upLoadTemplate.registerComponents(true);
            upLoadTemplate.setFormData(clickedAction , 0);
            if(upLoadTemplate.display()==1){
                bean.setFormId(upLoadTemplate.txtId.getText());
                bean.setDescription(upLoadTemplate.txtDescription.getText().trim());
                bean.setFormDescription(upLoadTemplate.txtTemplate.getText().trim());
                // Added for COEUSQA-1412 Subcontract Module changes - Start        
                ComboBoxBean selectedTemplateType =(ComboBoxBean)upLoadTemplate.cmbTemplateType.getSelectedItem();
                if(selectedTemplateType != null){
                    bean.setTemplateTypeCode(Integer.parseInt(selectedTemplateType.getCode()));
                    bean.setTemplateTypeDescription(selectedTemplateType.getDescription());
                }
                // Added for COEUSQA-1412 Subcontract Module changes - End
                bean.setAcType("U");
                updateTemplateData(bean);
            }
        }
    }
    
    
    
    // Update the template data to the database as soon as add operation is performed
    private void updateTemplateData(RTFFormBean rtfRomBean) throws CoeusClientException{
        CoeusVector cvFinalVector = new CoeusVector();
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(UPDATE_TEMPLATE);
        requesterBean.setDataObject(rtfRomBean);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
                cvData = getFormData();
                formTableModel.setData(cvData);
                formTableModel.fireTableRowsInserted(formTableModel.getRowCount()+1, formTableModel.getRowCount()+1);
                
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
    }
    
    // Delete a seletcted row and update to the server
    private void performDeleteAction() throws CoeusClientException{
        int selRow = tblForms.getSelectedRow();
        if(selRow == -1){
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(DELETE_ROW) + "  ");
            return;
        }
        if(selRow!=-1){
            String formId = (String)tblForms.getValueAt(selRow, FORM_ID_COLUMN);
            cvData.remove(selRow);
            formTableModel.fireTableRowsDeleted(selRow, selRow);
            updateData(formId);
        }
    }
    
    // Updates the server as soon as user deletes the selected row
    private void updateData(String formId) throws CoeusClientException{
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(UPDATE_DATA);
        requesterBean.setDataObject(formId);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()){
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
    }
    
    /*to set the default focus in the window*/
    private void setWindowFocus(){
        btnClose.requestFocusInWindow();
    }
    
    /*the action on the click of the close button*/
    private void performCloseAction(){
        dlgSubcontractForm.setVisible(false);
    }
    
    /** An Inner class supports table model for the form.
     *This provides the table model for the table and holds the data
     *of the form
     */
    public class FormTableModel extends AbstractTableModel{
        // Modified for COEUSQA-1412 Subcontract Module changes - Start
//        private String colName[] = {"ID","Description"};
//        private Class colClass[]= {String.class, String.class};
        private String colName[] = {"ID","Description","Template Type"};
        private Class colClass[]= {String.class, String.class, String.class};
        // Modified for COEUSQA-1412 Subcontract Module changes - End
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public int getColumnCount() {
            return colName.length;
        }
        
        public void setData(CoeusVector cvData){
            cvData = cvData;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvData==null){
                return 0;
            }else{
                return cvData.size();
            }
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            RTFFormBean rtfFormBean = (RTFFormBean)cvData.elementAt(rowIndex);
            if (rtfFormBean != null) {
                switch(columnIndex) {
                    case FORM_ID_COLUMN:
                        return rtfFormBean.getFormId();
                    case DESCRIPTION_COLUMN:
                        return rtfFormBean.getDescription();
                    case TEMPLATE_TYPE_COLUMN:
                        return rtfFormBean.getTemplateTypeDescription();
                }
            }
            return EMPTY_STRING;
        }
        
    }
    
    /** This class will sort the column values in ascending and descending order
     * based on number of clicks.
     */
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","formId" },
            {"1","description" },
            {"1","templateTypeDescription" }
        };
        boolean sort =true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvData != null && cvData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvData).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    formTableModel.fireTableRowsUpdated(
                    0, formTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.............
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        scrPnForms = new javax.swing.JScrollPane();
        tblForms = new javax.swing.JTable();
        btnModify = new javax.swing.JButton();
        btnUpLoad = new javax.swing.JButton();
        btnDownLoad = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(790, 400));
        setPreferredSize(new java.awt.Dimension(790, 400));
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(90, 23));
        btnAdd.setMinimumSize(new java.awt.Dimension(90, 23));
        btnAdd.setPreferredSize(new java.awt.Dimension(90, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(90, 23));
        btnDelete.setMinimumSize(new java.awt.Dimension(90, 23));
        btnDelete.setPreferredSize(new java.awt.Dimension(90, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(btnDelete, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(90, 23));
        btnClose.setMinimumSize(new java.awt.Dimension(90, 23));
        btnClose.setPreferredSize(new java.awt.Dimension(90, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(btnClose, gridBagConstraints);

        scrPnForms.setMinimumSize(new java.awt.Dimension(700, 400));
        scrPnForms.setPreferredSize(new java.awt.Dimension(700, 400));
        tblForms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scrPnForms.setViewportView(tblForms);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        add(scrPnForms, gridBagConstraints);

        btnModify.setFont(CoeusFontFactory.getLabelFont());
        btnModify.setMnemonic('M');
        btnModify.setText("Modify");
        btnModify.setMaximumSize(new java.awt.Dimension(90, 23));
        btnModify.setMinimumSize(new java.awt.Dimension(90, 23));
        btnModify.setPreferredSize(new java.awt.Dimension(90, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(btnModify, gridBagConstraints);

        btnUpLoad.setFont(CoeusFontFactory.getLabelFont()
        );
        btnUpLoad.setMnemonic('L');
        btnUpLoad.setText("Load");
        btnUpLoad.setMaximumSize(new java.awt.Dimension(90, 23));
        btnUpLoad.setMinimumSize(new java.awt.Dimension(90, 23));
        btnUpLoad.setPreferredSize(new java.awt.Dimension(90, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(btnUpLoad, gridBagConstraints);

        btnDownLoad.setFont(CoeusFontFactory.getLabelFont());
        btnDownLoad.setMnemonic('w');
        btnDownLoad.setText("Download");
        btnDownLoad.setMaximumSize(new java.awt.Dimension(90, 23));
        btnDownLoad.setMinimumSize(new java.awt.Dimension(90, 23));
        btnDownLoad.setPreferredSize(new java.awt.Dimension(90, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(btnDownLoad, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Getter for property fileSelected.
     * @return Value of property fileSelected.
     */
    public boolean isFileSelected() {
        return fileSelected;
    }    

    /**
     * Setter for property fileSelected.
     * @param fileSelected New value of property fileSelected.
     */
    public void setFileSelected(boolean fileSelected) {
        this.fileSelected = fileSelected;
    }    
     
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDownLoad;
    private javax.swing.JButton btnModify;
    private javax.swing.JButton btnUpLoad;
    private javax.swing.JScrollPane scrPnForms;
    private javax.swing.JTable tblForms;
    // End of variables declaration//GEN-END:variables
    
}
