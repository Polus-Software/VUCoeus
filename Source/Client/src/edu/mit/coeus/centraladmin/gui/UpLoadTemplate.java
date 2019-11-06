/*
 * UpLoadTemplate.java
 *
 * Created on December 24, 2004, 4:02 PM
 */

package edu.mit.coeus.centraladmin.gui;

import edu.mit.coeus.bean.CoeusTypeBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.subcontract.bean.RTFFormBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author  surekhan
 */
public class UpLoadTemplate extends javax.swing.JComponent implements ActionListener{
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusDlgWindow dlgupLoadTemplate; 
    // Modified for COEUSQA-1412 Subcontract Module changes - Start
//    private static final int WIDTH = 450;
//    private static final int HEIGHT = 130;
    private static final int WIDTH = 475; 
    private static final int HEIGHT = 140;
    // Modified for COEUSQA-1412 Subcontract Module changes - End
    private static final String WINDOW_TITLE = "Upload Template";
    private static final String CANCEL_CONFIRMATION = "adminAward_exceptionCode.1353";
    private boolean fileSelected = false; /* To check whether file has been selected or not */
    
    private CoeusFileChooser fileChooser;
    private byte [] templateData = null;
    private CoeusMessageResources coeusMessageResources;
    private static final String EMPTY_STRING = "";
    private RTFFormBean rtfFormBean;
    public final int OK_CLICKED = 1;
    public final int CANCEL_CLICKED = 0;
    private int clicked;
    private CoeusVector cvData;
    private boolean actionFlag;
    private boolean acType;
    private boolean clickMode;
    private boolean dataChanged;
    private CoeusVector cvChangeVector;
    private boolean modifyAction;
    private CoeusVector cvModifyVector;
    private int LOAD;
    // Added for COEUSQA-1412 Subcontract Module changes - Start
    private final char GET_SUBCONTRACT_TEMPLATE_TYPES = '1';
    private CoeusVector cvTemplateTypes;
    private static final String GET_SERVLET = "/SubcontractMaintenenceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private final int FDP_SPONSOR_ATTACHMENT_TYPE = 2;
    private final int FDP_TEMPLATE_ATTACHMENT_TYPE = 3;
    private final int FDP_TEMPLATE_TYPE = 4;
    // Added for COEUSQA-1412 Subcontract Module changes - End
    
    /** Creates new form UpLoadTemplate */
    public UpLoadTemplate(CoeusAppletMDIForm mdiForm,CoeusVector cvData) {
        this.mdiForm = mdiForm;
        this.cvData = cvData;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        txtTemplate.setBackground(Color.WHITE);
        txtId.setDocument(new LimitedPlainDocument(30));
        txtDescription.setDocument(new LimitedPlainDocument(50));
        postInitComponents();
    }
    
     public UpLoadTemplate(CoeusAppletMDIForm mdiForm,CoeusVector cvData , boolean mode) {
        this.mdiForm = mdiForm;
        this.cvData = cvData;
        clickMode = mode;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        txtTemplate.setBackground(Color.WHITE);
        txtId.setDocument(new LimitedPlainDocument(30));
        txtDescription.setDocument(new LimitedPlainDocument(50));
        postInitComponents();
        dlgupLoadTemplate.setTitle("Add Template");
    }
     
     public UpLoadTemplate(CoeusAppletMDIForm mdiForm,CoeusVector cvData , boolean mode ,CoeusVector cvTempData ){
        this.mdiForm = mdiForm;
        this.cvData = cvTempData;
        cvModifyVector = new CoeusVector();
        cvModifyVector = cvData;
        cvChangeVector = new CoeusVector();
        cvChangeVector = cvTempData;
        modifyAction = mode;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        txtTemplate.setBackground(Color.WHITE);
        txtId.setDocument(new LimitedPlainDocument(30));
        txtDescription.setDocument(new LimitedPlainDocument(50));
        postInitComponents();
        dlgupLoadTemplate.setTitle("Modify Template");
        
    }
    
     /*to set the form data*/
    public void setFormData(boolean action , int value){
        actionFlag = action;
        LOAD = value;
        // Added for COEUSQA-1412 Subcontract Module changes - Start
        cvTemplateTypes = getSubcontractTemplateTypes();
        if(cvTemplateTypes != null && !cvTemplateTypes.isEmpty()){
            cmbTemplateType.setModel(new DefaultComboBoxModel(cvTemplateTypes));
        }
        // Added for COEUSQA-1412 Subcontract Module changes - End

        if(cvData!= null && cvData.size() > 0 && !action){
            if(value == 1){
                RTFFormBean bean = (RTFFormBean)cvData.get(0);
                txtDescription.setText(bean.getDescription());
                txtId.setText(bean.getFormId());
                txtTemplate.setText(bean.getFormDescription());
                // Modified for COEUSQA-1412 Subcontract Module changes - Start
//                dlgupLoadTemplate.setSize(450, 130);                
                CoeusVector cvFiltered = cvTemplateTypes.filter(
                        new Equals("code",CoeusGuiConstants.EMPTY_STRING+ bean.getTemplateTypeCode()));
                if(cvFiltered != null && !cvFiltered.isEmpty()){
                    ComboBoxBean cmbBean = (ComboBoxBean)cvFiltered.get(0);
                    cmbTemplateType.setSelectedItem(cmbBean);
                }
                
                dlgupLoadTemplate.setSize(475, 140);
                // Modified for COEUSQA-1412 Subcontract Module changes - End
            }else{
                RTFFormBean bean = (RTFFormBean)cvModifyVector.get(0);
                txtDescription.setText(bean.getDescription());
                txtId.setText(bean.getFormId());
                txtTemplate.setText(bean.getDescription());
                // Modified for COEUSQA-1412 Subcontract Module changes - Start
//                dlgupLoadTemplate.setSize(380, 100);
                CoeusVector cvFiltered = cvTemplateTypes.filter(
                        new Equals("code",CoeusGuiConstants.EMPTY_STRING+ bean.getTemplateTypeCode()));
                if(cvFiltered != null && !cvFiltered.isEmpty()){
                    ComboBoxBean cmbBean = (ComboBoxBean)cvFiltered.get(0);
                    cmbTemplateType.setSelectedItem(cmbBean);
                }
                dlgupLoadTemplate.setSize(415, 115);
                // Modified for COEUSQA-1412 Subcontract Module changes - End
            }
        }else{
            txtDescription.setText(EMPTY_STRING);
            txtId.setText(EMPTY_STRING);
            txtTemplate.setText(EMPTY_STRING);
        }

    }
    
    /*to display the form*/
    public int display(){
        setRequestFocusInThread(txtId);
        dlgupLoadTemplate.show();
        return clicked;
    }
    
    /*to initiate the components*/
    private void postInitComponents() {
        dlgupLoadTemplate = new CoeusDlgWindow(mdiForm);
        dlgupLoadTemplate.setResizable(false);
        dlgupLoadTemplate.setModal(true);
        dlgupLoadTemplate.getContentPane().add(this);
        dlgupLoadTemplate.setFont(CoeusFontFactory.getLabelFont());
        dlgupLoadTemplate.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgupLoadTemplate.setSize(WIDTH, HEIGHT);
        dlgupLoadTemplate.setTitle(WINDOW_TITLE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgupLoadTemplate.getSize();
        dlgupLoadTemplate.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        txtTemplate.setEditable(false);
        dlgupLoadTemplate.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                close();
                return;
            }
        });
        
        dlgupLoadTemplate.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                close();
                return;
            }
        });
        
        dlgupLoadTemplate.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
        
        
    }
    
    /*to set the default focus*/
    private void setWindowFocus(){
        btnCancel.requestFocusInWindow();
    }
    
    /*to register the components*/
    public void registerComponents(boolean mode){
        btnCancel.addActionListener(this);
        btnOK.addActionListener(this);
        btnBrowse.addActionListener(this);
        if(!mode){
            if(txtId.isEditable() && txtDescription.isEditable()){
                java.awt.Component[] component = {txtId,txtDescription,btnBrowse,btnOK,btnCancel};
                ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
                setFocusTraversalPolicy(policy);
                setFocusCycleRoot(true);
            }else{
                java.awt.Component[] component = {btnOK,btnCancel,btnBrowse};
                ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
                setFocusTraversalPolicy(policy);
                setFocusCycleRoot(true);
            }
        }else{
            java.awt.Component[] component = {txtId,txtDescription,btnOK,btnCancel};
            ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
            setFocusTraversalPolicy(policy);
            setFocusCycleRoot(true);
        }
    }
    
    /*the action performed on the click of the browse button*/
    private void performBrowseAction(){
        fileChooser = new CoeusFileChooser(dlgupLoadTemplate); /* Creating an instance of fileChooser */
         //Added for case #2345 start 
        // Modified for COEUSQA-1412 Subcontract Module changes - Start
//        String fileExtension[] = {"xsl","xml"};
//        fileChooser.setSelectedFileExtension(fileExtension);
      ComboBoxBean comboBoxBean = (ComboBoxBean)cmbTemplateType.getSelectedItem();
        if(comboBoxBean != null){
            int templateTypeCode = Integer.parseInt(comboBoxBean.getCode());
            if(templateTypeCode == FDP_SPONSOR_ATTACHMENT_TYPE
                    || templateTypeCode == FDP_TEMPLATE_ATTACHMENT_TYPE
                    || templateTypeCode == FDP_TEMPLATE_TYPE){
                String fileExtension[] = {"xsl"};
                fileChooser.setSelectedFileExtension(fileExtension);
            }else{
                String fileExtension[] = {"xsl","xml"};
                fileChooser.setSelectedFileExtension(fileExtension);
            }
        }else{
            String fileExtension[] = {"xsl","xml"};
            fileChooser.setSelectedFileExtension(fileExtension);
        }
        // Modified for COEUSQA-1412 Subcontract Module changes - End
        
        //Added for case #2345 end
        fileChooser.showFileChooser();             /*   Opening a file Chooser */
        if(fileChooser.isFileSelected()){          /*   Checking whether a file is selected or not*/
            //modified = true;
            String fileName = fileChooser.getSelectedFile();
            if(fileName != null && !fileName.trim().equals("")){
                int index = fileName.lastIndexOf('.');
                if(index != -1 && index != fileName.length()){
                    String extension = fileName.substring(index+1,fileName.length());
                    if(extension != null && (extension.equalsIgnoreCase("xml")  || extension.equalsIgnoreCase("xsl"))){
                        templateData = fileChooser.getFile();   /*  Getting the Selected File Data*/
                        setFileSelected(true);                      /*  Setting File is Selected */
                        File file = fileChooser.getFileName();
                        txtTemplate.setText(""+file.getName());  /* Getting the Selected File Name*/
                    }else{
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                        "correspType_exceptionCode.1011"));
                        templateData = null;        /*  Setting file data as null */
                        setFileSelected(false);       /*  Setting file is not selected */
                    }
                }else{
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                    "correspType_exceptionCode.1012"));
                    templateData = null;        /*  Setting file data as null */
                    setFileSelected(false);       /*  Setting file is not selected */
                }
            }
        }else{ /* File is not selected*/
            templateData = null;        /*  Setting file data as null */
            setFileSelected(false);       /*  Setting file is not selected */
        }
    }
    
    /*the action performed on the click of the OK button*/
    private void performOkAction(){
        if(LOAD == 1){
            setRTFBeanData();
            dlgupLoadTemplate.dispose();
            clicked = OK_CLICKED;
        }else{
            if(actionFlag){
                if(validations()){
                    setRTFBeanData();
                    dlgupLoadTemplate.dispose();
                    clicked = OK_CLICKED;
                }
            }else{
                if(validations()){
                    setRTFBeanData();
                    dlgupLoadTemplate.dispose();
                    clicked = OK_CLICKED;
                }
            }
        }
    }
    
    /*the action on the click of the close button*/
    private void close() {
        try{
            if(isDatachanged()){
                String mesg = coeusMessageResources.parseMessageKey(CANCEL_CONFIRMATION);
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg+"  "),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    performOkAction();
                } else if(selectedOption == CoeusOptionPane.OPTION_OK_CANCEL){
                    dlgupLoadTemplate.setVisible(false);
                }
            }else{
                dlgupLoadTemplate.setVisible(false);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /*the validations to be performed*/
    private  boolean validations(){
        if(txtId.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog("Please enter the Form Id. ");
            setRequestFocusInThread(txtId);
            txtId.setCaretPosition(0);
            return false;
        }
        if(txtDescription.getText()==null || txtDescription.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog("Please enter the description. ");
            setRequestFocusInThread(txtDescription);
            txtId.setCaretPosition(0);
            return false;
        }
        if(txtTemplate.getText().trim().equals(EMPTY_STRING) || txtTemplate.getText() == null){
            CoeusOptionPane.showInfoDialog("Please select the template. ");
            setRequestFocusInThread(btnBrowse);
            return false;
        }
        if(modifyAction){
            cvData = cvChangeVector;
            String formId = txtId.getText().trim();
            Equals eqFormId = new Equals("formId",formId);
            RTFFormBean bean = (RTFFormBean)cvModifyVector.get(0);
            if(bean.getFormId().equals(formId)) {
                return true;
            }else{
                if(cvData!= null && cvData.size() >0){
                    CoeusVector cvDuplicate  = cvData.filter(eqFormId);
                    if(cvDuplicate.size()>0){
                        CoeusOptionPane.showErrorDialog("Form Id "+formId + " already exists. ");
                        setRequestFocusInThread(txtId);
                        return false;
                    }
                }
            }
        }else{
            String formId = txtId.getText().trim();
            Equals eqFormId = new Equals("formId",formId);
            if(cvData!= null && cvData.size() >0){
                CoeusVector cvDuplicate  = cvData.filter(eqFormId);
                if(cvDuplicate.size()>0){
                    CoeusOptionPane.showErrorDialog("Form Id "+formId + " already exists. ");
                    setRequestFocusInThread(txtId);
                    return false;
                }
            }
        }
        return true;
    }
    
    /*to set the data to the bean*/
    public RTFFormBean setRTFBeanData(){
        RTFFormBean bean = null;
        if(cvData!= null && cvData.size()>0){
            if(!clickMode){
                bean = (RTFFormBean)cvData.get(0);
                bean.setFormId(txtId.getText().trim());
                bean.setDescription(txtDescription.getText().trim());
                //bean.setFormDescription(txtTemplate.getText().trim());
                bean.setForm(templateData);
                // Added for COEUSQA-1412 Subcontract Module changes - Start
                ComboBoxBean templateType = (ComboBoxBean)cmbTemplateType.getSelectedItem();
                bean.setTemplateTypeCode(Integer.parseInt(templateType.getCode()));
                // Added for COEUSQA-1412 Subcontract Module changes - End

                return bean;
            }else{
                bean = new RTFFormBean();
                bean.setFormId(txtId.getText().trim());
                bean.setAw_Form_Id(txtId.getText().trim());
                bean.setDescription(txtDescription.getText().trim());
               // bean.setFormDescription(txtTemplate.getText().trim());
                bean.setForm(templateData);
                // Added for COEUSQA-1412 Subcontract Module changes - Start
                ComboBoxBean templateType = (ComboBoxBean)cmbTemplateType.getSelectedItem();
                bean.setTemplateTypeCode(Integer.parseInt(templateType.getCode()));
                // Added for COEUSQA-1412 Subcontract Module changes - End

                return bean;
            }
        }else{
            bean = new RTFFormBean();
            bean.setFormId(txtId.getText().trim());
            bean.setAw_Form_Id(txtId.getText().trim());
            bean.setDescription(txtDescription.getText().trim());
            bean.setFormDescription(txtTemplate.getText().trim());
            bean.setForm(templateData);
            // Added for COEUSQA-1412 Subcontract Module changes - Start
            ComboBoxBean templateType = (ComboBoxBean)cmbTemplateType.getSelectedItem();
            bean.setTemplateTypeCode(Integer.parseInt(templateType.getCode()));
            // Added for COEUSQA-1412 Subcontract Module changes - End

            return bean;
        }
        //return bean;
    }
    
    /** Supporting method which will be used for the focus lost for date
     *fields. This will be fired when the request focus for the specified
     *date field is invoked
     */
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    /*to check whether the data is changed*/
    public boolean isDatachanged(){
        if(modifyAction){
            RTFFormBean bean = (RTFFormBean)cvModifyVector.get(0);
            if((!bean.getFormId().equals(txtId.getText().trim()))){
                dataChanged = true;
            }
        }else{
            if(cvData == null || cvData.size() < 0){
                dataChanged = true;
            }else{
                RTFFormBean bean = (RTFFormBean)cvData.get(0);
                if((!bean.getFormId().equals(txtId.getText().trim()))){
                    dataChanged = true;
                }else if((!bean.getDescription().equals(txtDescription.getText().trim()))){
                    dataChanged = true;
                }
            }
        }
        return dataChanged;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblId = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        lblTemplate = new javax.swing.JLabel();
        btnBrowse = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtId = new edu.mit.coeus.utils.CoeusTextField();
        txtDescription = new edu.mit.coeus.utils.CoeusTextField();
        txtTemplate = new edu.mit.coeus.utils.CoeusTextField();
        lblTemplateType = new javax.swing.JLabel();
        cmbTemplateType = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(475, 130));
        setPreferredSize(new java.awt.Dimension(475, 130));
        lblId.setFont(CoeusFontFactory.getLabelFont());
        lblId.setText("ID: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(lblId, gridBagConstraints);

        lblDescription.setFont(CoeusFontFactory.getLabelFont()
        );
        lblDescription.setText("Description: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(lblDescription, gridBagConstraints);

        lblTemplate.setFont(CoeusFontFactory.getLabelFont());
        lblTemplate.setText("Template: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(lblTemplate, gridBagConstraints);

        btnBrowse.setFont(CoeusFontFactory.getLabelFont());
        btnBrowse.setMnemonic('B');
        btnBrowse.setText("Browse");
        btnBrowse.setMaximumSize(new java.awt.Dimension(75, 23));
        btnBrowse.setMinimumSize(new java.awt.Dimension(75, 25));
        btnBrowse.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
        add(btnBrowse, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont()
        );
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMaximumSize(new java.awt.Dimension(73, 25));
        btnOK.setMinimumSize(new java.awt.Dimension(73, 25));
        btnOK.setPreferredSize(new java.awt.Dimension(73, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
        add(btnCancel, gridBagConstraints);

        txtId.setFont(CoeusFontFactory.getNormalFont());
        txtId.setMinimumSize(new java.awt.Dimension(210, 22));
        txtId.setPreferredSize(new java.awt.Dimension(210, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(txtId, gridBagConstraints);

        txtDescription.setFont(CoeusFontFactory.getNormalFont());
        txtDescription.setMinimumSize(new java.awt.Dimension(210, 22));
        txtDescription.setPreferredSize(new java.awt.Dimension(210, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(txtDescription, gridBagConstraints);

        txtTemplate.setFont(CoeusFontFactory.getNormalFont());
        txtTemplate.setMinimumSize(new java.awt.Dimension(285, 22));
        txtTemplate.setPreferredSize(new java.awt.Dimension(285, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(txtTemplate, gridBagConstraints);

        lblTemplateType.setFont(CoeusFontFactory.getLabelFont());
        lblTemplateType.setText("Template Type: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(lblTemplateType, gridBagConstraints);

        cmbTemplateType.setFont(CoeusFontFactory.getNormalFont());
        cmbTemplateType.setMinimumSize(new java.awt.Dimension(210, 22));
        cmbTemplateType.setPreferredSize(new java.awt.Dimension(210, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        add(cmbTemplateType, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
   /*the actions performed on the click of the buttons*/
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            dlgupLoadTemplate.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if(source.equals(btnCancel)){
                close();
            }else if(source.equals(btnBrowse)){
                performBrowseAction();
            }else if(source.equals(btnOK)){
                performOkAction();
            }
        }finally{
            dlgupLoadTemplate.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
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
    
    // Added for COEUSQA-1412 Subcontract Module changes - Start
    /**
     * Method to get subcontract template types
     * @return cvSubTemplateTypes - CoeusVector
     */
    private CoeusVector getSubcontractTemplateTypes(){
        CoeusVector cvSubTemplateTypes = null;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_SUBCONTRACT_TEMPLATE_TYPES);
        AppletServletCommunicator appletServletCommunicator =
                new AppletServletCommunicator(connect, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responder = appletServletCommunicator.getResponse();
        if(responder.isSuccessfulResponse()){
            cvSubTemplateTypes = (CoeusVector)responder.getDataObject();
        }
        return cvSubTemplateTypes;
    }
    // Added for COEUSQA-1412 Subcontract Module changes - End
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnBrowse;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOK;
    public javax.swing.JComboBox cmbTemplateType;
    public javax.swing.JLabel lblDescription;
    public javax.swing.JLabel lblId;
    public javax.swing.JLabel lblTemplate;
    public javax.swing.JLabel lblTemplateType;
    public edu.mit.coeus.utils.CoeusTextField txtDescription;
    public edu.mit.coeus.utils.CoeusTextField txtId;
    public edu.mit.coeus.utils.CoeusTextField txtTemplate;
    // End of variables declaration//GEN-END:variables
    
}
