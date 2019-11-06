/*
 * @(#) PersonDegreeDetail.java  1.0
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 14-MAY-2007
 * by Leena
 */

package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.gui.*;
//import edu.mit.coeus.utils.CoeusGuiConstants;
//import edu.mit.coeus.utils.CoeusComboBox;
//import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
//import javax.swing.event.*;
import java.util.*;
import edu.mit.coeus.departmental.bean.*;
//import edu.mit.coeus.utils.ComboBoxBean;
//import edu.mit.coeus.gui.CoeusMessageResources;
import java.text.*;

/** <CODE>PersonDegreeDetail</CODE> is a dialog object which display
 * all the biographical information for a selected person and can be used to
 * <CODE>add/modify/display</CODE> the biographical details details.
 * This class is instantiated in <CODE>PersonBaseWindow</CODE>.
 *
 * @author  Raghunath P.V.
 * @version:
 */

public class PersonDegreeDetail extends javax.swing.JComponent implements ActionListener, TypeConstants{
    
    //holds the Parent window component.
    private CoeusDlgWindow dlgParentComponent;
//    private CoeusAppletMDIForm mdiForm;
    private String title;
    private String personId;
    private char functionType;
    private boolean saveRequired;
    private DepartmentPerDegreeFormBean departmentPerDegreeFormBean;
    private Vector departmentCodes;
    private Vector schoolIdCodes;
    //private boolean dataChanged;
    private boolean okCancelCheckFlag;
    private String focusDate;
    private DateUtils dtUtils;
    private SimpleDateFormat dtFormat;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /** Creates new form PersonDegreeDetail */
    public PersonDegreeDetail() {
    }
    
    /**
     *  Constructor which instantiates the PersonDegreeDetail
     *  @param personId a String sets the personId of selected Person
     *  @param functionType is a Char variable which specifies the mode in which the
     *  form will be displayed.
     *  <B>'A'</B> specifies that the form is in Add Mode
     *  <B>'M'</B> specifies that the form is in Modify Mode
     *  <B>'D'</B> specifies that the form is in Display Mode
     *  @param dlgPrnt a reference of parent JDialog
     *  @param degreeBean a DepartmentPerDegreeFormBean which contains all the degree information to display
     *  @param deptTypeCodes a Vector containing all the lookup values for the Degree Type Combo
     *  @param schoolCodes a Vector containing all the lookup values for the Science Code  Combo
     */
    public PersonDegreeDetail( String personId, char functionType, CoeusDlgWindow dlgPrnt, DepartmentPerDegreeFormBean degreeBean,
            Vector deptTypeCodes, Vector schoolCodes ) {
        
        dtUtils = new DateUtils();
        dtFormat = new SimpleDateFormat("MM/dd/yyyy");
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        initComponents();
        
        //Start Amit 11/17/2003
        java.awt.Component[] components = {cmbDegreeType, txtGraduationDate, txtDegree,
        txtFieldOfStudy, txtSpecialization, txtSchoolName, cmdIdCode, txtId};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        //End Amit
        
        this.dlgParentComponent = dlgPrnt;
        this.departmentPerDegreeFormBean = degreeBean;
        this.departmentCodes = deptTypeCodes;
        this.schoolIdCodes = schoolCodes;
        this.personId = personId;
        this.functionType = functionType;
        this.title = "Person Degree Detail";
        setListeners();
        
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
                title, true);
        dlgParentComponent.getContentPane().add(createWindow());
        dlgParentComponent.pack();
        
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgParentComponent.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performWindowClosing();
            }
        });
        dlgParentComponent.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent we){
                //Commented for Coeus4.3 enhancement - 2356
                //Since the focus always get into the first component after 
                //validation done for other components. So moved out of the windowActivated function
                //setDefaultFocusForComponent();
            }
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }
        });
        //Added for Coeus 4.3 enhancement
        //The function is commented in windowActivated(), and is added here.
        setDefaultFocusForComponent();
        dlgParentComponent.show();
    }
    private void setDefaultFocusForComponent(){
        cmbDegreeType.requestFocusInWindow();
    }
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag and the function type.
     * If the saveRequired is true then it saves the educational details to the
     * database else dispose this JDialog.
     */
    
    public void performWindowClosing(){
        
        int option = JOptionPane.NO_OPTION;
        if(functionType != DISPLAY_MODE){
            setSaveRequiredChanged();
            if(isSaveRequired()){
                option
                        = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                        "saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
            }
            if(option == JOptionPane.YES_OPTION){
                try{
                    
                    okCancelCheckFlag = true;
                    setDegreeInfo();
                    //setDataToBean();
                    //DepartmentPerDegreeFormBean depBean = getFormData();
                    dlgParentComponent.dispose();
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }else if(option == JOptionPane.NO_OPTION){
                okCancelCheckFlag = false;
                saveRequired = false;
                dlgParentComponent.dispose();
            }
        }else{
            okCancelCheckFlag = false;
            dlgParentComponent.dispose();
        }
    }
    
    /**
     * Method to set the data to the Form
     * This method sets the data which is available in departmentPerDegreeFormBean to the form components
     * and it populates the Combo components with the data available in corresponding vectors.
     */
    private JComponent createWindow(){
        
        populateDepartmentTypeCodes();
        populateSchoolIdCodes();
        //setListeners();
        
        if(departmentPerDegreeFormBean != null){
            
//            String personId = departmentPerDegreeFormBean.getPersonId();
            String degreeCode = departmentPerDegreeFormBean.getDegreeCode();
            //String graduationDate = ((Date)departmentPerDegreeFormBean.getGraduationDate()).toString();
            //Added for case 3649 - Graduation Date was not changed to YYYY - start
            int graduationYear = -1;
            if(departmentPerDegreeFormBean.getGraduationDate()!=null){
                graduationYear = ((Date)departmentPerDegreeFormBean.getGraduationDate()).getYear();
            }
            //Added for case 3649 - Graduation Date was not changed to YYYY - end
            String degree = departmentPerDegreeFormBean.getDegree();
            String fieldofStudy = departmentPerDegreeFormBean.getFieldOfStudy();
            String specialization = departmentPerDegreeFormBean.getSpecialization();
            String school = departmentPerDegreeFormBean.getSchool();
            String schoolIdCode = departmentPerDegreeFormBean.getSchoolIdCode();
            String schoolId = departmentPerDegreeFormBean.getSchoolId();
            
            //cmbDegreeType.setSelectedItem(degreeCode == null ? "" : degreeCode);
            
            ComboBoxBean comboBean = new ComboBoxBean();
            comboBean.setCode(degreeCode);
            comboBean.setDescription(departmentPerDegreeFormBean.getDegreeDescription());
            
            if(functionType =='D'){
                cmbDegreeType.addItem(comboBean);
            }else{
                for(int typeRow = 0; typeRow < cmbDegreeType.getItemCount();
                typeRow++){
                    if(((ComboBoxBean)cmbDegreeType.getItemAt(
                            typeRow)).toString().equals(comboBean.toString())){
                        cmbDegreeType.setSelectedIndex(typeRow);
                    }
                }
            }
            
            //cmdIdCode.setSelectedItem(schoolIdCode == null ? "" : schoolIdCode);
            ComboBoxBean comboBean1 = new ComboBoxBean();
            comboBean1.setCode(schoolIdCode);
            comboBean1.setDescription(departmentPerDegreeFormBean.getSchoolDescription());
            
            if(functionType =='D'){
                cmdIdCode.addItem(comboBean1);
            }else{
                for(int typeRow = 0; typeRow < cmdIdCode.getItemCount();
                typeRow++){
                    if(((ComboBoxBean)cmdIdCode.getItemAt(
                            typeRow)).toString().equals(comboBean1.toString())){
                        cmdIdCode.setSelectedIndex(typeRow);
                    }
                }
            }
            
            txtDegree.setText(degree == null ? "" : degree);
            txtFieldOfStudy.setText(fieldofStudy == null ? "" : fieldofStudy);
            //String formatDate = dtUtils.formatDate(graduationDate.toString(),"dd-MMM-yyyy");
            //txtGraduationDate.setText(formatDate == null ? "" : formatDate);
            //Added for case 3649 - Graduation Date was not changed to YYYY - start
            if(graduationYear == -1){
                txtGraduationDate.setText("");
            }else{
                txtGraduationDate.setText(Integer.toString(graduationYear+1900));
            }
            //Added for case 3649 - Graduation Date was not changed to YYYY - end
            txtSchoolName.setText(school == null ? "" : school);
            txtSpecialization.setText(specialization == null ? "" : specialization);
            txtId.setText(schoolId == null ? "" : schoolId);
            //customPropertyListener.setDataChanged(false);
        }
        
        return this;
    }
    
    // This method adds Listensers to all the buttons in the form
    
    private void setListeners(){
        
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        //Commented for Coeus 4.3 enhancement - start
        //Validation is not done at focusLost, only when the data is saved
        //txtGraduationDate.addFocusListener(new CustomFocusAdapter());
        //Commented for Coeus 4.3 enhancement - start
        
        //txtDegree.addPropertyChangeListener(customPropertyListener);
        //txtDegree.addPropertyChangeListener("text",customPropertyListener);
        //txtFieldOfStudy.addPropertyChangeListener(customPropertyListener);
        //txtId.addPropertyChangeListener(customPropertyListener);
        //txtSchoolName.addPropertyChangeListener(customPropertyListener);
        //txtSpecialization.addPropertyChangeListener(customPropertyListener);
        /*
         *Date stDate = dtFormat.parse(
                    dtUtils.restoreDate(txtStartDate.getText(),"/-:,"),
                            new ParsePosition(0));
         **/
        
        cmbDegreeType.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie){
                //saveRequired = true;
            }
        });
        
        cmdIdCode.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie){
                //saveRequired = true;
            }
        });
    }
    
    // This method populates the Department Type ComboBox
    private void populateDepartmentTypeCodes(){
        if(departmentCodes != null){
            int size = departmentCodes.size();
            for(int index = 0; index < size; index++){
                ComboBoxBean cBean = (ComboBoxBean)departmentCodes.elementAt(index);
                cmbDegreeType.addItem(cBean);
            }
        }
    }
    
    // This method sets the saveRequired true if any values are changed in the Form components
    
    private void setSaveRequiredChanged(){
        
        if(functionType == ADD_MODE){
            saveRequired = true;
        }
        ComboBoxBean degreeCodeBean = (ComboBoxBean)cmbDegreeType.getSelectedItem();
        String strDegreeCode = degreeCodeBean.getCode().trim();
        
        ComboBoxBean schoolIdCodeBean = (ComboBoxBean)cmdIdCode.getSelectedItem();
        String strSchoolIdCode = schoolIdCodeBean.getCode().trim();
        
        String gradFormattedDate = (txtGraduationDate.getText() == null) ? "" : ("1-Jan-"+txtGraduationDate.getText());
        //String strGradDate = dtUtils.restoreDate(gradFormattedDate,"/:-,");
        
        String strDegree = (txtDegree.getText() == null) ? "" : txtDegree.getText();
        String strFieldOfStudy = (txtFieldOfStudy.getText() == null) ? "" : txtFieldOfStudy.getText();
        String strSpecialization = (txtSpecialization.getText() == null) ? "" : txtSpecialization.getText();
        String strSchoolName = (txtSchoolName.getText() == null) ? "" : txtSchoolName.getText();
        String strSchoolId = (txtId.getText() == null) ? "" : txtId.getText();
        
        if(departmentPerDegreeFormBean != null){
            
            String degreeCode = departmentPerDegreeFormBean.getDegreeCode();
            degreeCode = degreeCode == null ? "" : degreeCode;
            String graduationDate = ((Date)departmentPerDegreeFormBean.getGraduationDate()).toString();
            graduationDate = graduationDate == null ? "" : graduationDate;
            
            String convertedDate = dtUtils.formatDate(graduationDate,"dd-MMM-yyyy");
            
            String degree = departmentPerDegreeFormBean.getDegree();
            degree = degree == null ? "" : degree;
            String fieldofStudy = departmentPerDegreeFormBean.getFieldOfStudy();
            fieldofStudy = fieldofStudy == null ? "" : fieldofStudy;
            String specialization = departmentPerDegreeFormBean.getSpecialization();
            specialization = specialization == null ? "" : specialization;
            String school = departmentPerDegreeFormBean.getSchool();
            school = school == null ? "" : school;
            String schoolIdCode = departmentPerDegreeFormBean.getSchoolIdCode();
            schoolIdCode = schoolIdCode == null ? "" : schoolIdCode;
            String schoolId = departmentPerDegreeFormBean.getSchoolId();
            schoolId = schoolId == null ? "" : schoolId;
            
            if(!strDegreeCode.equalsIgnoreCase(degreeCode)){
                saveRequired = true;
            }
            if(!gradFormattedDate.equalsIgnoreCase(convertedDate)){
                saveRequired = true;
            }
            if(!strDegree.equalsIgnoreCase(degree)){
                saveRequired = true;
            }
            
            if(!strFieldOfStudy.equalsIgnoreCase(fieldofStudy)){
                saveRequired = true;
            }
            if(!strSpecialization.equalsIgnoreCase(specialization)){
                saveRequired = true;
            }
            if(!strSchoolName.equalsIgnoreCase(school)){
                saveRequired = true;
            }
            if(!strSchoolIdCode.equalsIgnoreCase(schoolIdCode)){
                saveRequired = true;
            }
            if(!strSchoolId.equalsIgnoreCase(schoolId)){
                saveRequired = true;
            }
        }
    }
    
    // this method gets the DepartmentPerDegreeFormBean
    public DepartmentPerDegreeFormBean getFormData(){
        //DepartmentPerDegreeFormBean dBean = setDataToBean();
        return departmentPerDegreeFormBean;
    }
    
    /** This method is used to show the alert messages to the user.
     * @param mesg is a message to alert the user.
     * @throws Exception is the <CODE>Exception</CODE> to throw in the client side.
     */
    
    private void errorMessage(String mesg) throws Exception{
        throw new Exception(mesg);
    }
    
    // This method populates the school id codes in the SchoolCode Combobox
    
    private void populateSchoolIdCodes(){
        if(schoolIdCodes != null){
            int size = schoolIdCodes.size();
            for(int index = 0; index < size; index++){
                ComboBoxBean cBean = (ComboBoxBean)schoolIdCodes.elementAt(index);
                cmdIdCode.addItem(cBean);
            }
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlContainer = new javax.swing.JPanel();
        pnlDegreeInfo = new javax.swing.JPanel();
        cmbDegreeType = new edu.mit.coeus.utils.CoeusComboBox();
        lblDegreeType = new javax.swing.JLabel();
        txtGraduationDate = new edu.mit.coeus.utils.CoeusTextField();
        lblGraduationDate = new javax.swing.JLabel();
        lblDegree = new javax.swing.JLabel();
        lblFieldOfStudy = new javax.swing.JLabel();
        lblSpecialization = new javax.swing.JLabel();
        txtDegree = new edu.mit.coeus.utils.CoeusTextField();
        txtFieldOfStudy = new edu.mit.coeus.utils.CoeusTextField();
        txtSpecialization = new edu.mit.coeus.utils.CoeusTextField();
        pnlSchoolContainer = new javax.swing.JPanel();
        pnlSchool = new javax.swing.JPanel();
        txtId = new edu.mit.coeus.utils.CoeusTextField();
        cmdIdCode = new edu.mit.coeus.utils.CoeusComboBox();
        txtSchoolName = new edu.mit.coeus.utils.CoeusTextField();
        lblName = new javax.swing.JLabel();
        lblIdCode = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        pnlContainer.setLayout(new java.awt.GridBagLayout());

        pnlDegreeInfo.setLayout(new java.awt.GridBagLayout());

        pnlDegreeInfo.setPreferredSize(new java.awt.Dimension(562, 120));
        pnlDegreeInfo.setMinimumSize(new java.awt.Dimension(562, 120));
        pnlDegreeInfo.setMaximumSize(new java.awt.Dimension(562, 120));
        cmbDegreeType.setMaximumSize(new java.awt.Dimension(225, 20));
        cmbDegreeType.setMinimumSize(new java.awt.Dimension(225, 20));
        cmbDegreeType.setPreferredSize(new java.awt.Dimension(225, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 6, 0);
        pnlDegreeInfo.add(cmbDegreeType, gridBagConstraints);

        lblDegreeType.setText("Degree Type:");
        lblDegreeType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDegreeType.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 6, 0);
        pnlDegreeInfo.add(lblDegreeType, gridBagConstraints);

        txtGraduationDate.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
        txtGraduationDate.setMaximumSize(new java.awt.Dimension(125, 23));
        txtGraduationDate.setMinimumSize(new java.awt.Dimension(125, 23));
        txtGraduationDate.setPreferredSize(new java.awt.Dimension(125, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 6, 3);
        pnlDegreeInfo.add(txtGraduationDate, gridBagConstraints);

        lblGraduationDate.setFont(CoeusFontFactory.getLabelFont());
        lblGraduationDate.setText("Year Graduated:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 17, 6, 0);
        pnlDegreeInfo.add(lblGraduationDate, gridBagConstraints);

        lblDegree.setText("Degree:");
        lblDegree.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlDegreeInfo.add(lblDegree, gridBagConstraints);

        lblFieldOfStudy.setText("Field Of Study:");
        lblFieldOfStudy.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlDegreeInfo.add(lblFieldOfStudy, gridBagConstraints);

        lblSpecialization.setText("Specialization:");
        lblSpecialization.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        pnlDegreeInfo.add(lblSpecialization, gridBagConstraints);

        txtDegree.setDocument(new LimitedPlainDocument(80));
        txtDegree.setMaximumSize(new java.awt.Dimension(175, 23));
        txtDegree.setMinimumSize(new java.awt.Dimension(175, 23));
        txtDegree.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 6, 3);
        pnlDegreeInfo.add(txtDegree, gridBagConstraints);

        txtFieldOfStudy.setDocument(new LimitedPlainDocument(80));
        txtFieldOfStudy.setMaximumSize(new java.awt.Dimension(175, 23));
        txtFieldOfStudy.setMinimumSize(new java.awt.Dimension(175, 23));
        txtFieldOfStudy.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 6, 3);
        pnlDegreeInfo.add(txtFieldOfStudy, gridBagConstraints);

        txtSpecialization.setDocument(new LimitedPlainDocument(80));
        txtSpecialization.setMaximumSize(new java.awt.Dimension(175, 23));
        txtSpecialization.setMinimumSize(new java.awt.Dimension(175, 23));
        txtSpecialization.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 6, 3);
        pnlDegreeInfo.add(txtSpecialization, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlContainer.add(pnlDegreeInfo, gridBagConstraints);

        pnlSchoolContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlSchoolContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "School", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 11), new java.awt.Color(255, 51, 51)));
        pnlSchoolContainer.setPreferredSize(new java.awt.Dimension(550, 90));
        pnlSchoolContainer.setMinimumSize(new java.awt.Dimension(550, 90));
        pnlSchoolContainer.setMaximumSize(new java.awt.Dimension(550, 90));
        pnlSchool.setLayout(new java.awt.GridBagLayout());

        pnlSchool.setPreferredSize(new java.awt.Dimension(530, 60));
        pnlSchool.setMinimumSize(new java.awt.Dimension(530, 60));
        pnlSchool.setMaximumSize(new java.awt.Dimension(530, 60));
        txtId.setDocument(new LimitedPlainDocument(20));
        txtId.setMaximumSize(new java.awt.Dimension(125, 23));
        txtId.setMinimumSize(new java.awt.Dimension(125, 23));
        txtId.setPreferredSize(new java.awt.Dimension(125, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        pnlSchool.add(txtId, gridBagConstraints);

        cmdIdCode.setMaximumSize(new java.awt.Dimension(225, 20));
        cmdIdCode.setMinimumSize(new java.awt.Dimension(225, 20));
        cmdIdCode.setPreferredSize(new java.awt.Dimension(225, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        pnlSchool.add(cmdIdCode, gridBagConstraints);

        txtSchoolName.setDocument(new LimitedPlainDocument(50));
        txtSchoolName.setMaximumSize(new java.awt.Dimension(175, 23));
        txtSchoolName.setMinimumSize(new java.awt.Dimension(175, 23));
        txtSchoolName.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 7, 0);
        pnlSchool.add(txtSchoolName, gridBagConstraints);

        lblName.setText("Name:");
        lblName.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 7, 0);
        pnlSchool.add(lblName, gridBagConstraints);

        lblIdCode.setText("Id Code:");
        lblIdCode.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 28, 0, 0);
        pnlSchool.add(lblIdCode, gridBagConstraints);

        lblId.setText("ID:");
        lblId.setFont(CoeusFontFactory.getLabelFont());
        lblId.setPreferredSize(new java.awt.Dimension(19, 17));
        lblId.setMinimumSize(new java.awt.Dimension(19, 17));
        lblId.setMaximumSize(new java.awt.Dimension(19, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 90, 0, 0);
        pnlSchool.add(lblId, gridBagConstraints);

        pnlSchoolContainer.add(pnlSchool);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlContainer.add(pnlSchoolContainer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(pnlContainer, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(73, 27));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 27));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        pnlButtons.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 0, 0);
        add(pnlButtons, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    // This method is invoked when ever a button fires action event
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        try{
            Object actionSource = actionEvent.getSource();
            if(actionSource.equals( btnOk )){
                okCancelCheckFlag = true;
                //departmentPerDegreeFormBean dBean = getFormData();
                setSaveRequiredChanged();
                if((isSaveRequired()) && (functionType != DISPLAY_MODE) ){
                    setDegreeInfo();
                    dlgParentComponent.dispose();
                }
                dlgParentComponent.dispose();
            }else if(actionSource.equals( btnCancel )){
                performWindowClosing();
            }
            
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
            //setVisible(true);
        }
    }
    
    // This method validates the Form Components and sets the data which is
    //in form components to Bean.
    
    private void setDegreeInfo() throws Exception{
        
        if(functionType == 'D'){
            //do nothing
        }else {
            if(validateComponents()){
                setDataToBean();
                //DepartmentPerDegreeFormBean depBean = getFormData();
            }
        }
    }
    
    // This method sets the form data to the bean.
    
    private void setDataToBean(){
        
        ComboBoxBean degreeCodeBean = (ComboBoxBean)cmbDegreeType.getSelectedItem();
        String degreeCode = degreeCodeBean.getCode();
        String degreeCodeDesc = degreeCodeBean.getDescription();
        
        ComboBoxBean schoolIdCodeBean = (ComboBoxBean)cmdIdCode.getSelectedItem();
        String schoolIdCode = schoolIdCodeBean.getCode();
        String schoolIDDesc = schoolIdCodeBean.getDescription();
        //Modified for Coeus 4.3 enhancement- 2356
//        String gradFormattedDate = txtGraduationDate.getText();
        String strGradDate = "01/01/"+txtGraduationDate.getText().trim();
        java.sql.Date gradDate = null;
        
        try{
            gradDate = new java.sql.Date(dtFormat.parse(strGradDate).getTime());
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        
        String degree = txtDegree.getText();
        String fieldOfStudy = txtFieldOfStudy.getText();
        String specialization = txtSpecialization.getText();
        String schoolName = txtSchoolName.getText();
        String schoolId = txtId.getText();
        
        if(functionType == ADD_MODE){
            
            saveRequired = true;
            departmentPerDegreeFormBean = new DepartmentPerDegreeFormBean();
            
            departmentPerDegreeFormBean.setPersonId(personId);
            
            departmentPerDegreeFormBean.setDegreeCode(degreeCode);
            departmentPerDegreeFormBean.setDegreeDescription(degreeCodeDesc);
            departmentPerDegreeFormBean.setGraduationDate(gradDate);
            departmentPerDegreeFormBean.setDegree(degree);
            departmentPerDegreeFormBean.setFieldOfStudy(fieldOfStudy);
            departmentPerDegreeFormBean.setSpecialization(specialization);
            departmentPerDegreeFormBean.setSchool(schoolName);
            departmentPerDegreeFormBean.setSchoolIdCode(schoolIdCode);
            departmentPerDegreeFormBean.setSchoolDescription(schoolIDDesc);
            
            departmentPerDegreeFormBean.setSchoolId(schoolId);
            departmentPerDegreeFormBean.setAcType(INSERT_RECORD);
            //departmentPerDegreeFormBean = bean;
            //return departmentPerDegreeFormBean;
            
        }else if(functionType == MODIFY_MODE){
            
            departmentPerDegreeFormBean.setDegreeCode(degreeCode);
            departmentPerDegreeFormBean.setDegreeDescription(degreeCodeDesc);
            departmentPerDegreeFormBean.setGraduationDate(gradDate);
            departmentPerDegreeFormBean.setDegree(degree);
            departmentPerDegreeFormBean.setFieldOfStudy(fieldOfStudy);
            departmentPerDegreeFormBean.setSpecialization(specialization);
            departmentPerDegreeFormBean.setSchool(schoolName);
            departmentPerDegreeFormBean.setSchoolIdCode(schoolIdCode);
            departmentPerDegreeFormBean.setSchoolDescription(schoolIDDesc);
            departmentPerDegreeFormBean.setSchoolId(schoolId);
            if( (departmentPerDegreeFormBean.getAcType() != null )){
                if(( ! departmentPerDegreeFormBean.getAcType().equalsIgnoreCase(INSERT_RECORD) )) {
                    departmentPerDegreeFormBean.setAcType(UPDATE_RECORD);
                }
            }else{
                departmentPerDegreeFormBean.setAcType(UPDATE_RECORD);
            }
            //return departmentPerDegreeFormBean;
        }else{
            //return null;
        }
    }
    
    /** Getter for property okCancelCheckFlag.
     * @return Value of property okCancelCheckFlag.
     */
    public boolean isOkCancelCheckFlag() {
        return okCancelCheckFlag;
    }
    
    /** Setter for property okCancelCheckFlag.
     * @param okCancelCheckFlag New value of property okCancelCheckFlag.
     */
    public void setOkCancelCheckFlag(boolean okCancelCheckFlag) {
        this.okCancelCheckFlag = okCancelCheckFlag;
    }
    
    /** Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    // this method validates the form components with the Mandatory checkings applicable
    private boolean validateComponents() throws Exception{
        
        if(!validateDegreeType()){
            return false;
        }else if(!validateGraduationDate()){
            return false;
        }else if(!validateDegree()){
            return false;
        }else if(!validateFieldOfStudy()){
            return false;
        }else if(!validateSpecialization()){
            return false;
        }else if(!validateSchoolName()){
            return false;
        }else if(!validateSchoolIdCode()){
            return false;
        }else{
            return validateID();
        }
    }
    
    /**
     * This method validates the DegreeType combo box
     * If data is not selected this method shows the error
     * message to the user with appropriate message.
     */
    private boolean validateDegreeType() throws Exception{
        
        boolean valid = true;
        ComboBoxBean degreeCodeBean = (ComboBoxBean)cmbDegreeType.getSelectedItem();
        String degreeCode = degreeCodeBean.getCode();
        if((degreeCode == null) || (degreeCode.trim().length() <= 0)){
            valid = false;
            errorMessage("'Degree Code' field cannot be Null, Please select a Degree Code.");
            cmbDegreeType.requestFocus();
        }
        return valid;
    }
    
    /**
     * This method validates the Graduation date text field component
     * If data is not entered this method shows the error
     * message to the user with appropriate message.
     */
    private boolean validateGraduationDate() throws Exception{
        boolean valid = true;
        //Commented for Coeus 4.3 enhancement -PT ID – 2356 - start
        //Instead of date user need to enter a valid year.
//        String graduationDate = txtGraduationDate.getText();
//        if((graduationDate == null) || (graduationDate.trim().length() <= 0)){
//            valid = false;
//            errorMessage("'Graduation Date' field cannot be Null.");
//            txtGraduationDate.requestFocus();
//        }
        //Commented for Coeus 4.3 enhancement -PT ID – 2356 - end
        //Added for Coeus 4.3 enhancement - PT ID 2356 - start
        //To validate the user entered year value of the format yyyy between 1900 -9999
        String message = "person_personal_exceptionCode.1100";
        if(txtGraduationDate.getText().trim().equals("")){
            valid = false;
            message = "person_personal_exceptionCode.1101";
        }else{
            String year = txtGraduationDate.getText().trim();
            if(year.length()>4){
                valid = false;
            }else{
                try{
                    int intYear = Integer.parseInt(year);
                    if(intYear<1900){
                        valid = false;
                    }
                }catch(NumberFormatException e){
                    valid = false;
                }
            }
        }
        if(!valid){
            txtGraduationDate.requestFocus();
            throw new Exception(coeusMessageResources.parseMessageKey(message));
        }
        //Added for Coeus 4.3 enhancement - PT ID 2356 - end
        return valid;
    }
    
    /**
     * This method validates the Degree text field component
     * If data is not entered this method shows the error
     * message to the user with appropriate message.
     */
    private boolean validateDegree() throws Exception{
        boolean valid = true;
        String degree = txtDegree.getText();
        if((degree == null) || (degree.trim().length() <= 0)){
            valid = false;
            errorMessage("'Degree' field cannot be Null.");
            txtDegree.requestFocus();
        }
        return valid;
    }
    
    // No validation provided for Field Of Study Textfield
    private boolean validateFieldOfStudy(){
        return true;
    }
    
    // No validation provided for Specialization Textfield
    private boolean validateSpecialization(){
        return true;
    }
    // No validation provided for SchoolName Textfield
    private boolean validateSchoolName(){
        return true;
    }
    // No validation provided for SchoolIdCode Textfield
    private boolean validateSchoolIdCode(){
        return true;
    }
    // No validation provided for ID Textfield
    private boolean validateID(){
        return true;
    }
    
    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private edu.mit.coeus.utils.CoeusComboBox cmbDegreeType;
    private edu.mit.coeus.utils.CoeusComboBox cmdIdCode;
    private javax.swing.JLabel lblDegree;
    private javax.swing.JLabel lblDegreeType;
    private javax.swing.JLabel lblFieldOfStudy;
    private javax.swing.JLabel lblGraduationDate;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblIdCode;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblSpecialization;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JPanel pnlDegreeInfo;
    private javax.swing.JPanel pnlSchool;
    private javax.swing.JPanel pnlSchoolContainer;
    private edu.mit.coeus.utils.CoeusTextField txtDegree;
    private edu.mit.coeus.utils.CoeusTextField txtFieldOfStudy;
    private edu.mit.coeus.utils.CoeusTextField txtGraduationDate;
    private edu.mit.coeus.utils.CoeusTextField txtId;
    private edu.mit.coeus.utils.CoeusTextField txtSchoolName;
    private edu.mit.coeus.utils.CoeusTextField txtSpecialization;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Internal class which is used to listen to focus events of start date
     * and end date fields. On focusGained the date will be restored to
     * MM/dd/yyyy, and on  the date will be formatted to dd-MMM-yyyy.
     */
    private class CustomFocusAdapter extends FocusAdapter{
        JTextField txtFld = null;
        String strData = "";
        boolean temporary = false;
        
        public void focusGained(FocusEvent fe){
            txtFld = (JTextField)fe.getSource();
            if(fe.getSource().equals(txtGraduationDate)){
                if ( (txtFld.getText() != null)
                && (!txtFld.getText().trim().equals("")) ) {
                    if(!strData.equals(txtFld.getText()) && !temporary){
                        focusDate = dtUtils.restoreDate(txtFld.getText(),"/-:,");
                        txtFld.setText(focusDate);
                    }
                }
            }
        }
        public void focusLost(FocusEvent fe){
            txtFld = (JTextField)fe.getSource();
            /* check whether the focus lost is temporary or permanent*/
            temporary = fe.isTemporary();
            
//            String editingValue = null;
            if ( (txtFld.getText() != null)
            &&  (!txtFld.getText().trim().equals("")) && (!temporary)) {
                strData = txtFld.getText();
                
                if(fe.getSource().equals(txtGraduationDate)){
                    String convertedDate = dtUtils.formatDate(txtFld.getText(),
                            "/-:," , "dd-MMM-yyyy");
                    if (convertedDate==null){
                        CoeusOptionPane.showErrorDialog( "Item "+
                                txtFld.getText() + " does not pass "
                                +"validation test" );
                        txtFld.requestFocus();
                        temporary = true;
                    }else {
                        focusDate = txtFld.getText();
                        txtFld.setText(convertedDate);
                        temporary = false;
                    }
                } else{
                    String convertedTime = dtUtils.formatTime(txtFld.getText());
                    if (convertedTime==null){
                        CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                "schdGenFrm_exceptionCode.1085"));
                        txtFld.requestFocus();
                    }else {
                        txtFld.setText(convertedTime);
                    }
                }
            }
        }
    };
}
