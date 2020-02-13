/*
 * @(#) PersonDegreeForm.java  1.0
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.departmental.bean.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import java.text.*;
import edu.mit.coeus.propdev.bean.ProposalPerDegreeFormBean;

/** <CODE>PersonDegreeForm</CODE> is a dialog modal window which display
 * all the educational information for a selected person and can be used to
 * <CODE>add/modify/display</CODE> the educational details of a person.
 * This class is instantiated in <CODE>PersonBaseWindow</CODE>.
 *
 * @author  Raghunath P.V.
 * @version: 1.0
 */
public class PersonDegreeForm extends javax.swing.JComponent implements TypeConstants, ActionListener{ 
    
    private Vector vecDegreeData;
    private Vector vecDegreeTypeCode;
    private Vector vecSchoolCode;
    private Vector vecDeletedDegreeData;
    private String personId;
    private String proposalId;
    private CoeusDlgWindow dlgParentComponent;
    private CoeusAppletMDIForm mdiForm;
    private char functionType;
    //private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    private boolean saveRequired;
    private String personName;
    private String loginName;
    
    //private int modifyProposalPerson;
    private int canModifyProposal;
    //private boolean modifyProposalDegree;
    private boolean maintainPerson;
    //private boolean modifyProposalDegree;
    private boolean canMaintainPersonDegree;
    private boolean isDegreeMaintained;
    
    private boolean canMainOtherByLoginUser;
    private boolean userCanMaintainSelectedPerson;
    private boolean userCanMaintainUnit;
    
    private PersonDegreeDetail personDegreeDetail;
    private DepartmentPerDegreeFormBean departmentPerDegreeFormBean;
    private static final String SAVE_DEGREE_DETAILS = "saveDegreeDetails";
    private static final String GET_DEGREE_DETAILS_FOR_PERSON = "getDegreeDetails";
    private static final char GET_PROPOSAL_DEGREE_DETAILS_FOR_PERSON = 'K';
    private static final char SAVE_PROPOSAL_DEGREE_DETAILS = 'a';
    private DateUtils dtUtils;
    private SimpleDateFormat dtFormat;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private String moduleCode;
    
    /*Bug Fix*/
    /*to set the person name to the title of the dialog instaed of the person id*/
    private String name;
    
    /** Creates new form PersonDegreeForm */
    public PersonDegreeForm() {
       // initComponents();
    }
    
    /**
     *  Constructor which instantiates the PersonDegreeForm
     *  @param perName a String sets the person name as title to the JDialog Frame
     *  @param loginName a String represents the login name 
     *  @param functionType is a Char variable which specifies the mode in which the
     *  form will be displayed.
     *  <B>'A'</B> specifies that the form is in Add Mode
     *  <B>'M'</B> specifies that the form is in Modify Mode
     *  <B>'D'</B> specifies that the form is in Display Mode
     */
    public PersonDegreeForm(char functionType, String module) {
        
        this.functionType = functionType;
        this.moduleCode = module;
    }
    
    public void showDegreeForm(){
        initComponents();       
                
        //Start Amit 11/17/2003
        java.awt.Component[] components = {tblPersonDegreeInfo, btnOk, btnCancel, btnAdd, btnModify, btnDelete };        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        //End Amit
        
        mdiForm = CoeusGuiConstants.getMDIForm();
        this.loginName = mdiForm.getUserName();

        vecDeletedDegreeData = new Vector();
        dtUtils = new DateUtils();
        dtFormat = new SimpleDateFormat("MM/dd/yyyy");
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        if (moduleCode.equalsIgnoreCase(CoeusGuiConstants.PERSON_MODULE)) {
            //System.out.println(" in PERSON_MODULE");
            lblProposalNo.setVisible(false);
            lblProposalValue.setVisible(false);
            lblFullName.setText(name);
            if(personName.equalsIgnoreCase(loginName)){
                this.canMainOtherByLoginUser = true;
            }
            if(personName != null && personName.length() > 0 ){
                
                this.vecDegreeData = (Vector)getPersonDegreeDetailsfromDB(personName);
            }
        } else if (moduleCode.equalsIgnoreCase(CoeusGuiConstants.PROPOSAL_MODULE)) {
            lblProposalValue.setText(proposalId);
            lblFullName.setText(personName);
            if(personId != null && personId.length() > 0 ){
                this.vecDegreeData = (Vector)getProposalPersonDegreeDetailsfromDB();
            }
        }  
        
        setListenersForButtons();
        formatFields();
        setFormData();
        setTableEditors();
        if( tblPersonDegreeInfo != null && tblPersonDegreeInfo.getRowCount() > 0 ){
            tblPersonDegreeInfo.setRowSelectionInterval(0, 0);
        }else{
            // If no Data is there then set the delete button to disable mode.
            btnDelete.setEnabled(false);
            btnModify.setEnabled(false);
        }
        
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
                "Degree Details for "+name, true);
            
        dlgParentComponent.getContentPane().add(this);
        dlgParentComponent.pack();
        dlgParentComponent.setResizable(false);
        
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
            //Method to set the focus either to table or cancel buttons...
            public void windowOpened(WindowEvent evnt){
                    if(tblPersonDegreeInfo.getRowCount()>=1)
                    {
                        tblPersonDegreeInfo.setRequestFocusEnabled(true);
                        tblPersonDegreeInfo.requestFocus();
                    }
                    else
                    {
                        btnCancel.setRequestFocusEnabled(true);
                        btnCancel.requestFocus();
                    }        
            }
            public void windowClosing(WindowEvent we){
                 performWindowClosing();
                 return;
            }
            });
       dlgParentComponent.show();
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing 
     * the window it checks the saveRequired flag and the function type. 
     * If the saveRequired is true then it saves the educational details to the 
     * database else dispose this JDialog.
     */
    private void performWindowClosing(){
        
        int option = JOptionPane.NO_OPTION;
        if(functionType != DISPLAY_MODE){
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
                    if(moduleCode == CoeusGuiConstants.PERSON_MODULE){
                        savePersonDegreeDetails();
                    }else if(moduleCode == CoeusGuiConstants.PROPOSAL_MODULE){
                        saveProposalPersonDegreeDetails();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }else if(option == JOptionPane.NO_OPTION){
                saveRequired = false;
                dlgParentComponent.dispose();
            }
        }else{
            dlgParentComponent.dispose();
        }
    }
    
    // This method adds Listensers to all the buttons in the form
    
    private void setListenersForButtons(){
        
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        btnCancel.addActionListener(this);
        btnModify.addActionListener(this);
        btnOk.addActionListener(this);
    }
    
    /**
        This method is used to Enable or Disable the Buttons
        depending on the function Type. If the functionType is Display 
        then it sets the Add, Delete, FindPerson, FindRolodex JButtons to disable.
    */
    private void formatFields(){
        
        if(moduleCode == CoeusGuiConstants.PERSON_MODULE){
            if(canMainOtherByLoginUser || userCanMaintainSelectedPerson || userCanMaintainUnit){
                isDegreeMaintained = true;
            }else{
                isDegreeMaintained = false;
                functionType = DISPLAY_MODE;
            }
        }else if(moduleCode == CoeusGuiConstants.PROPOSAL_MODULE){
            if(!maintainPerson){
                functionType = DISPLAY_MODE;
            }
        }
        
        if( moduleCode == CoeusGuiConstants.PERSON_MODULE ){
            if((! isDegreeMaintained ) || (functionType == DISPLAY_MODE)){
              
                //code to gray out the person degree form...
                tblPersonDegreeInfo.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
                tblPersonDegreeInfo.setSelectionBackground((Color) UIManager.getDefaults().get("Panel.background"));
                tblPersonDegreeInfo.setSelectionForeground(Color.black);

                btnAdd.setEnabled(false);
                btnDelete.setEnabled(false);
                btnOk.setEnabled(false);
                btnModify.setEnabled(false);
            }else{
                btnAdd.setEnabled(true);
                btnDelete.setEnabled(true);
                btnOk.setEnabled(true);
                btnModify.setEnabled(true);
            }
            
        }else if(moduleCode == CoeusGuiConstants.PROPOSAL_MODULE){
              //Modified for COEUSDEV-149 :Proposal Persons - Degree Details - Approval In Progress - Start
              //Degree Information screen is editable only when proposal is in 'PendingInProgress' with user has MODIFY_PROPOSAL right
//            if(( maintainPerson ) || (functionType != DISPLAY_MODE)){
              if(functionType != DISPLAY_MODE){//COEUSDEV-149 : END                
                btnAdd.setEnabled(true);
                btnDelete.setEnabled(true);
                btnOk.setEnabled(true);
                btnModify.setEnabled(true);
            }else{
                btnAdd.setEnabled(false);
                btnDelete.setEnabled(false);
                btnOk.setEnabled(false);
                btnModify.setEnabled(false);
            }
        }
    }
    
    /** 
     * Method to set the data in the JTable.
     * This method sets the data which is available in vecDegreeData Vector to JTable.
     */    
    private void setFormData(){
        
        //System.out.println("in setFormData");
        Vector vcDataPopulate = new Vector();
        Vector vcData = null;
        
        if((vecDegreeData != null) &&
                        (vecDegreeData.size() > 0)){
                            
            int degreeSize = vecDegreeData.size();
            
            for(int index = 0; index < degreeSize; index++){
                            
                departmentPerDegreeFormBean = (DepartmentPerDegreeFormBean)
                                                    vecDegreeData.get(index);
                
                String degreeCode = departmentPerDegreeFormBean.getDegreeCode();
                //String gradDate = ((Date)departmentPerDegreeFormBean.getGraduationDate()).toString();
                //Modified for case 3679 -Graduation Date was not changed to YYYY - start
//                String graduationDate = ((Date)departmentPerDegreeFormBean.getGraduationDate()).toString();
//                String formatDate = dtUtils.formatDate(graduationDate.toString(),"dd-MMM-yyyy");
                int graduationYear = -1;
                if(departmentPerDegreeFormBean.getGraduationDate()!=null){
                    graduationYear = ((Date)departmentPerDegreeFormBean.getGraduationDate()).getYear();
                }
                String formatDate = "";
                if(graduationYear == -1){
                    formatDate = "";
                }else{
                    formatDate = Integer.toString(graduationYear+1900);
                }
                //Modified for case 3679 -Graduation Date was not changed to YYYY - end
                String degree = departmentPerDegreeFormBean.getDegree();
                String fieldOfStudy = departmentPerDegreeFormBean.getFieldOfStudy();
                String specialization = departmentPerDegreeFormBean.getSpecialization();
                String school = departmentPerDegreeFormBean.getSchool();
                String schoolIdCode = departmentPerDegreeFormBean.getSchoolIdCode();
                String schoolId = departmentPerDegreeFormBean.getSchoolId();
                String personId = departmentPerDegreeFormBean.getPersonId();
                String degreeDescription = departmentPerDegreeFormBean.getDegreeDescription();

                vcData= new Vector();
                
                vcData.addElement(degreeDescription == null ? "" : degreeDescription);
                vcData.addElement(formatDate == null ? "" : formatDate);
                vcData.addElement(degree == null ? "" : degree);
                vcData.addElement(school == null ? "" : school);
                vcData.addElement(personId == null ? "" : personId);
                vcData.addElement(degreeCode == null ? "" : degreeCode);
                vcData.addElement(fieldOfStudy == null ? "" : fieldOfStudy);
                vcData.addElement(schoolIdCode == null ? "" : schoolIdCode);
                vcData.addElement(schoolId == null ? "" : schoolId);
                vcData.addElement(specialization == null ? "" : specialization);
                
                vcDataPopulate.addElement(vcData);
            }
                ((DefaultTableModel)tblPersonDegreeInfo.getModel()).setDataVector(
                                            vcDataPopulate,getColumnNames());
                
                ((DefaultTableModel)tblPersonDegreeInfo.getModel()).fireTableDataChanged();
        }
    }
    
    /**
     * This method is used to get the Column Names of Degree Table
     * table data.
     * @return Vector collection of column names of Degree Table.
     */
    
    private Vector getColumnNames(){
        Enumeration enumColNames = tblPersonDegreeInfo.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }

    /* This method is used to set the cell editors to the columns, 
       set the column width to each individual column, disable the column 
       resizable feature to the JTable, setting single selection mode to the 
       JTable */
    
    private void setTableEditors(){
        
        tblPersonDegreeInfo.getTableHeader().setResizingAllowed(true);
        tblPersonDegreeInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = tblPersonDegreeInfo.getColumnModel().getColumn(0);
        column.setMinWidth(200);
        
        
        column = tblPersonDegreeInfo.getColumnModel().getColumn(1);
        column.setMinWidth(125);
        
        
        column = tblPersonDegreeInfo.getColumnModel().getColumn(2);
        column.setMinWidth(175);
        
        
        column = tblPersonDegreeInfo.getColumnModel().getColumn(3);
        column.setMinWidth(175);
        
        int size = tblPersonDegreeInfo.getColumnModel().getColumnCount();
        
        for(int index = 4 ; index < size; index++){
            
            column = tblPersonDegreeInfo.getColumnModel().getColumn(index);
            column.setMinWidth(0);
            column.setMaxWidth(0);
            column.setPreferredWidth(0);
        }
        
        JTableHeader header = tblPersonDegreeInfo.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        header.setFont(CoeusFontFactory.getLabelFont());
        tblPersonDegreeInfo.setRowHeight(24);
        
        tblPersonDegreeInfo.setOpaque(false);
        tblPersonDegreeInfo.setShowVerticalLines(false);
        tblPersonDegreeInfo.setShowHorizontalLines(false);
        tblPersonDegreeInfo.setSelectionMode(
                            DefaultListSelectionModel.SINGLE_SELECTION);
    }
    
    /**
     * This method connects the servlet and gets the educational details for the selected person.
     * @return Vector, Containing all the educational information for a person.
     */
    private Vector getPersonDegreeDetailsfromDB(String personName){
        
        Vector personDegreeData = null;
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/personMaintenanceServlet";                                                                
        RequesterBean request = new RequesterBean();
        request.setId(personName);
        request.setFunctionType(functionType);
        request.setDataObject(GET_DEGREE_DETAILS_FOR_PERSON);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
       
        if (response!=null){
            if (response.isSuccessfulResponse()){
                Vector vecFromServer = (Vector)response.getDataObject();
                if(vecFromServer != null){
                    vecDegreeTypeCode = (Vector)vecFromServer.elementAt(0);
                    vecSchoolCode = (Vector)vecFromServer.elementAt(1);
                    personDegreeData = (Vector)vecFromServer.elementAt(2);
                    userCanMaintainSelectedPerson = ((Boolean)vecFromServer.elementAt(3)).booleanValue();
                    userCanMaintainUnit = ((Boolean)vecFromServer.elementAt(4)).booleanValue();
                }
                personId = (String)response.getId();
            }
        }
        return personDegreeData;
    }

    /**
     * This method connects the servlet and gets the educational details for the selected person.
     * @return Vector, Containing all the educational information for a person.
     */
    private Vector getProposalPersonDegreeDetailsfromDB(){
        
        Vector personDegreeData = null;
        //System.out.println("proposalId is >>>>"+proposalId);
        //System.out.println("personId is >>>>"+personId);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.PROPOSAL_SERVLET;
        RequesterBean request = new RequesterBean();
        Vector vecRequestData = new Vector();
        vecRequestData.addElement(proposalId);
        vecRequestData.addElement(personId);
        request.setDataObject(vecRequestData);
        request.setFunctionType(GET_PROPOSAL_DEGREE_DETAILS_FOR_PERSON);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
       
        if (response!=null){
            if (response.isSuccessfulResponse()){
                Vector vecFromServer = (Vector)response.getDataObject();
                if(vecFromServer != null){
                    vecDegreeTypeCode = (Vector)vecFromServer.elementAt(0);
                    vecSchoolCode = (Vector)vecFromServer.elementAt(1);
                    personDegreeData = (Vector)vecFromServer.elementAt(2);
                }
            }
        }
        return personDegreeData;
    }
    
    /** This Method is used to get the functionType
     * @return a fuctionType like 'A','D','M'.
     */
    
    public char getFunctionType(){
        return functionType;
    }

    /** This Method is used to set the functionType
     * @param fType is a Char data like 'A','D','M'.
     */
    
    public void setFunctionType(char fType){
        this.functionType = fType;
    }
    
    /** This method is used to determine whether the data to be saved or not.
     * @return true if the modifications done, false otherwise.
     */
    
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    /** This method is used to set true or false to the saveRequired member variable
     * @param save is a boolean variable to be set to saveRequired variable.
     */
    
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlProposalDescriptionContainer = new javax.swing.JPanel();
        pnlProposalDescription = new javax.swing.JPanel();
        lblProposalNo = new javax.swing.JLabel();
        lblDegreeInfo = new javax.swing.JLabel();
        lblProposalValue = new javax.swing.JLabel();
        lblFullName = new javax.swing.JLabel();
        pnlProposalPersondegreeInfo = new javax.swing.JPanel();
        scrPnPersonDegreeInfo = new javax.swing.JScrollPane();
        tblPersonDegreeInfo = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        pnlProposalDescriptionContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlProposalDescription.setLayout(new java.awt.GridBagLayout());

        lblProposalNo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblProposalNo.setText("Proposal Number: ");
        lblProposalNo.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlProposalDescription.add(lblProposalNo, gridBagConstraints);

        lblDegreeInfo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblDegreeInfo.setText("Educational degree info for  ");
        lblDegreeInfo.setFont(CoeusFontFactory.getLabelFont());
        lblDegreeInfo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        pnlProposalDescription.add(lblDegreeInfo, gridBagConstraints);

        lblProposalValue.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblProposalValue.setText("jLabel1");
        lblProposalValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblProposalValue.setFont(CoeusFontFactory.getNormalFont());
        lblProposalValue.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pnlProposalDescription.add(lblProposalValue, new java.awt.GridBagConstraints());

        lblFullName.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblFullName.setText("jLabel1");
        lblFullName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblFullName.setFont(CoeusFontFactory.getNormalFont());
        lblFullName.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlProposalDescription.add(lblFullName, gridBagConstraints);

        pnlProposalDescriptionContainer.add(pnlProposalDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlProposalDescriptionContainer, gridBagConstraints);

        pnlProposalPersondegreeInfo.setPreferredSize(new java.awt.Dimension(700, 300));
        pnlProposalPersondegreeInfo.setMinimumSize(new java.awt.Dimension(700, 300));
        pnlProposalPersondegreeInfo.setMaximumSize(new java.awt.Dimension(700, 300));
        scrPnPersonDegreeInfo.setBorder(new javax.swing.border.EtchedBorder());
        scrPnPersonDegreeInfo.setPreferredSize(new java.awt.Dimension(685, 285));
        scrPnPersonDegreeInfo.setMinimumSize(new java.awt.Dimension(685, 285));
        scrPnPersonDegreeInfo.setMaximumSize(new java.awt.Dimension(685, 285));
        tblPersonDegreeInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Degree Type", "Graduation Date", "Degree", "School", "PersonId", "DegreeCode", "FieldOfStudy", "School_ID_Code", "SchoolId", "Specialization"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPersonDegreeInfo.setFont(CoeusFontFactory.getNormalFont());
        tblPersonDegreeInfo.setRowHeight(20);
        tblPersonDegreeInfo.setShowVerticalLines(false);
        tblPersonDegreeInfo.setPreferredScrollableViewportSize(null);
        tblPersonDegreeInfo.setShowHorizontalLines(false);
        tblPersonDegreeInfo.setOpaque(false);
        scrPnPersonDegreeInfo.setViewportView(tblPersonDegreeInfo);

        pnlProposalPersondegreeInfo.add(scrPnPersonDegreeInfo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(pnlProposalPersondegreeInfo, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnOk.setMnemonic('o');
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.setPreferredSize(new java.awt.Dimension(81, 27));
        btnOk.setMaximumSize(new java.awt.Dimension(81, 27));
        btnOk.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(81, 27));
        btnCancel.setMaximumSize(new java.awt.Dimension(81, 27));
        btnCancel.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 17, 2);
        pnlButtons.add(btnCancel, gridBagConstraints);

        btnAdd.setMnemonic('A');
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setText("Add");
        btnAdd.setPreferredSize(new java.awt.Dimension(81, 27));
        btnAdd.setMaximumSize(new java.awt.Dimension(81, 27));
        btnAdd.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnAdd, gridBagConstraints);

        btnModify.setMnemonic('M');
        btnModify.setFont(CoeusFontFactory.getLabelFont());
        btnModify.setText("Modify");
        btnModify.setPreferredSize(new java.awt.Dimension(81, 27));
        btnModify.setMaximumSize(new java.awt.Dimension(81, 27));
        btnModify.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnModify, gridBagConstraints);

        btnDelete.setMnemonic('D');
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setText("Delete");
        btnDelete.setPreferredSize(new java.awt.Dimension(81, 27));
        btnDelete.setMaximumSize(new java.awt.Dimension(81, 27));
        btnDelete.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        add(pnlButtons, gridBagConstraints);

    }//GEN-END:initComponents

    /**
     * This method is invoked whenever the button is Hit.
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object actionSource = actionEvent.getSource();

        if(actionSource.equals( btnAdd )){
            try{
            PersonDegreeDetail personDegreeDetail = 
                new PersonDegreeDetail(personId, ADD_MODE, dlgParentComponent, null, vecDegreeTypeCode, vecSchoolCode);
            
            if(personDegreeDetail.isOkCancelCheckFlag()){
                
                saveRequired = true;
                DepartmentPerDegreeFormBean newDepartmentPerDegreeFormBean =
                                                    personDegreeDetail.getFormData();
            
                if(newDepartmentPerDegreeFormBean != null){
                    
                    Vector newRowData = new Vector();
                    String personId = newDepartmentPerDegreeFormBean.getPersonId();
                    //holds the degree code;
                    String degreeCode = newDepartmentPerDegreeFormBean.getDegreeCode();
                    //holds the degree code description;
                    String degreeCodeDescription = newDepartmentPerDegreeFormBean.getDegreeDescription();
                    //holds the graduation date
                    //Date graduationDate = newDepartmentPerDegreeFormBean.getGraduationDate();
                    //Modified for case 3679 -Graduation Date was not changed to YYYY - start
                     int graduationYear = -1;
                    if(newDepartmentPerDegreeFormBean.getGraduationDate()!= null){
                        graduationYear = ((Date)newDepartmentPerDegreeFormBean.getGraduationDate()).getYear();
                    }
                     String formatDate = "0";
                     if(graduationYear == -1){
                        formatDate = "";
                     }else{
                        formatDate = Integer.toString(graduationYear+1900);
                     }
                    
//                    String graduationDate = ((Date)newDepartmentPerDegreeFormBean.getGraduationDate()).toString();
//                    String formatDate = dtUtils.formatDate(graduationDate.toString(),"dd-MMM-yyyy");
                    //Modified for case 3679 -Graduation Date was not changed to YYYY - start
                    //holds the degree
                    String degree = newDepartmentPerDegreeFormBean.getDegree();
                    //holds the field of study
                    String fieldofStudy = newDepartmentPerDegreeFormBean.getFieldOfStudy();
                    //holds the specialization
                    String specialization = newDepartmentPerDegreeFormBean.getSpecialization();
                    //holds the school
                    String school = newDepartmentPerDegreeFormBean.getSchool();
                    //holds the school id code
                    String schoolIdCode = newDepartmentPerDegreeFormBean.getSchoolIdCode();
                    //holds the school id code description
                    //String schoolIdCodeDescription = newDepartmentPerDegreeFormBean.getSchoolIdCode();
                    //holds the school id
                    String schoolId = newDepartmentPerDegreeFormBean.getSchoolId();
                    //holds account type
                    String acType = newDepartmentPerDegreeFormBean.getAcType();
                    
                    newRowData.addElement(degreeCodeDescription);
                    newRowData.addElement(formatDate);
                    newRowData.addElement(degree);
                    newRowData.addElement(school);
                    newRowData.addElement("");
                    newRowData.addElement("");
                    newRowData.addElement("");
                    newRowData.addElement("");
                    newRowData.addElement("");
                    newRowData.addElement("");

                    if(vecDegreeData != null){
                        vecDegreeData.addElement(newDepartmentPerDegreeFormBean);
                    }else{
                        vecDegreeData = new Vector();
                        vecDegreeData.addElement(newDepartmentPerDegreeFormBean);
                    }

                    ((DefaultTableModel)tblPersonDegreeInfo.getModel()).addRow( newRowData );
                    ((DefaultTableModel)tblPersonDegreeInfo.getModel()).fireTableDataChanged();
                    //lastSelectedRow = lastRow;
                }
                int newRowCount = tblPersonDegreeInfo.getRowCount() - 1;
                if(newRowCount >= 0){
                    btnModify.setEnabled(true);
                    btnDelete.setEnabled(true);
                    tblPersonDegreeInfo.setRowSelectionInterval( newRowCount, newRowCount );
                    tblPersonDegreeInfo.scrollRectToVisible(tblPersonDegreeInfo.getCellRect(
                            newRowCount ,0, true));
                }
            }
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(actionSource.equals( btnModify )){
            
            int selectedRow = tblPersonDegreeInfo.getSelectedRow();
            if(selectedRow != -1){
                DepartmentPerDegreeFormBean personDegreeFormBean = null;
                if(vecDegreeData != null){
                    personDegreeFormBean = (DepartmentPerDegreeFormBean)vecDegreeData.elementAt(selectedRow);
                    if(personDegreeFormBean != null){
                        PersonDegreeDetail personDegreeDetail = 
                            new PersonDegreeDetail(personId, MODIFY_MODE, dlgParentComponent, personDegreeFormBean, vecDegreeTypeCode, vecSchoolCode);
                        if(personDegreeDetail.isOkCancelCheckFlag()){
                            saveRequired = true;
                            DepartmentPerDegreeFormBean modifyDepartmentPerDegreeFormBean =
                                personDegreeDetail.getFormData();

                            if(modifyDepartmentPerDegreeFormBean != null){

                                String personId = modifyDepartmentPerDegreeFormBean.getPersonId();
                                //holds the degree code;
                                String degreeCode = modifyDepartmentPerDegreeFormBean.getDegreeCode();
                                //holds the degree code description;
                                String degreeCodeDescription = modifyDepartmentPerDegreeFormBean.getDegreeDescription();
                                //holds the graduation date
                                //Date graduationDate = modifyDepartmentPerDegreeFormBean.getGraduationDate();
                                //Modified for case 3679 -Graduation Date was not changed to YYYY - start
//                                String graduationDate = ((Date)modifyDepartmentPerDegreeFormBean.getGraduationDate()).toString();
//                                String formatDate = dtUtils.formatDate(graduationDate.toString(),"dd-MMM-yyyy");
                                int graduationYear = -1;
                                if(modifyDepartmentPerDegreeFormBean.getGraduationDate()!= null){
                                    graduationYear = ((Date)modifyDepartmentPerDegreeFormBean.getGraduationDate()).getYear();
                                }
                                 String formatDate = "0";
                                 if(graduationYear == -1){
                                    formatDate = "";
                                 }else{
                                    formatDate = Integer.toString(graduationYear+1900);
                                 }
                                //Modified for case 3679 -Graduation Date was not changed to YYYY - start
                                //holds the degree
                                String degree = modifyDepartmentPerDegreeFormBean.getDegree();
                                //holds the field of study
                                String fieldofStudy = modifyDepartmentPerDegreeFormBean.getFieldOfStudy();
                                //holds the specialization
                                String specialization = modifyDepartmentPerDegreeFormBean.getSpecialization();
                                //holds the school
                                String school = modifyDepartmentPerDegreeFormBean.getSchool();
                                //holds the school id code
                                String schoolIdCode = modifyDepartmentPerDegreeFormBean.getSchoolIdCode();
                                //holds the school id code description
                                //String schoolIdCodeDescription = modifyDepartmentPerDegreeFormBean.getSchoolIdCode();
                                //holds the school id
                                String schoolId = modifyDepartmentPerDegreeFormBean.getSchoolId();
                                //holds account type
                                String acType = modifyDepartmentPerDegreeFormBean.getAcType();
                                
                                vecDegreeData.setElementAt(modifyDepartmentPerDegreeFormBean,selectedRow);
                                
                                ((DefaultTableModel)tblPersonDegreeInfo.getModel()
                                    ).setValueAt(degreeCodeDescription,selectedRow,0);
                                ((DefaultTableModel)tblPersonDegreeInfo.getModel()
                                    ).setValueAt(formatDate,selectedRow,1);
                                ((DefaultTableModel)tblPersonDegreeInfo.getModel()
                                    ).setValueAt(degree,selectedRow,2);
                                ((DefaultTableModel)tblPersonDegreeInfo.getModel()
                                    ).setValueAt(school,selectedRow,3);
                            }
                        }
                    }
                }
            }
        }else if(actionSource.equals( btnDelete )){
            deletePersonDegreeDetails();
        }else if(actionSource.equals( btnOk )){
            if(moduleCode == CoeusGuiConstants.PERSON_MODULE){
                savePersonDegreeDetails();
            }else if(moduleCode == CoeusGuiConstants.PROPOSAL_MODULE){
                saveProposalPersonDegreeDetails();
            }
        }else if(actionSource.equals( btnCancel )){
            performWindowClosing();
        }
    }
    
    /**
     * This method deletes a row in the JTable 
     * and sets the Actype of corresponding bean in Vector to D.
     */
    private void deletePersonDegreeDetails(){
        
        int totalRows = tblPersonDegreeInfo.getRowCount();
        if (totalRows > 0) {
            int selectedRow = tblPersonDegreeInfo.getSelectedRow();
            if (selectedRow != -1) {
                int selectedOption = CoeusOptionPane.
                                    showQuestionDialog(
                                    "Do you want to delete this row?",
                                    CoeusOptionPane.OPTION_YES_NO, 
                                    CoeusOptionPane.DEFAULT_YES);
                if (0 == selectedOption) {
                    DepartmentPerDegreeFormBean depPerDegreeFormBean = null; 
                    saveRequired = true;

                    if(vecDegreeData != null){
                        depPerDegreeFormBean = 
                            (DepartmentPerDegreeFormBean) vecDegreeData.get( selectedRow );
                    }

                    if( (depPerDegreeFormBean.getAcType() != null )){
                        if( ! depPerDegreeFormBean.getAcType().equalsIgnoreCase(INSERT_RECORD) ) {
                            depPerDegreeFormBean.setAcType( DELETE_RECORD );
                            vecDeletedDegreeData.addElement( depPerDegreeFormBean );
                        }
                    }else{
                        depPerDegreeFormBean.setAcType( DELETE_RECORD );
                        vecDeletedDegreeData.addElement( depPerDegreeFormBean );
                    }
                    
                    vecDegreeData.removeElementAt( selectedRow ); 

                    ((DefaultTableModel)
                    tblPersonDegreeInfo.getModel()).removeRow(selectedRow);

                    ((DefaultTableModel)
                    tblPersonDegreeInfo.getModel()).fireTableDataChanged();

                    int newRowCount = tblPersonDegreeInfo.getRowCount();
                    if(newRowCount == 0){
                        btnDelete.setEnabled(false);
                        btnModify.setEnabled(false);
                    }else if (newRowCount > selectedRow) {
                        (tblPersonDegreeInfo.getSelectionModel())
                            .setSelectionInterval(selectedRow,
                                selectedRow);
                    } else {
                        tblPersonDegreeInfo.setRowSelectionInterval( newRowCount - 1,
                                       newRowCount -1 ); 
                        tblPersonDegreeInfo.scrollRectToVisible( tblPersonDegreeInfo.getCellRect(
                                        newRowCount - 1 ,
                                        0, true));
                    }
                }

            }else{
                CoeusOptionPane.
                            showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                    "protoFndSrcFrm_exceptionCode.1057"));
            }
        }
    }

    private void saveProposalPersonDegreeDetails(){
        if(functionType == DISPLAY_MODE){
            //Do Nothing
        }else{
            if(!isSaveRequired()){
            }else{
                Vector vecDegreeDataFromTable = getPersonDegreeData();
                Vector vecProposalDegreePersons = setProposalNumber(vecDegreeDataFromTable);
                //System.out.println("Degree Details ");
                printDegreeBeans(vecProposalDegreePersons);
                String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.PROPOSAL_SERVLET;
                RequesterBean request = new RequesterBean();
                request.setFunctionType(SAVE_PROPOSAL_DEGREE_DETAILS);
                request.setDataObject(vecProposalDegreePersons);
                AppletServletCommunicator comm = new AppletServletCommunicator(
                                                            connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
                if (response!=null){
                    if (response.isSuccessfulResponse()){
                        dlgParentComponent.dispose();
                    }
                }
            }
        }
        dlgParentComponent.dispose();
    }
    
    private void printDegreeBeans(Vector vecDegreeDataVector){
        //System.out.println("Degree Details are :");
        //System.out.println("********************");
        if(vecDegreeDataVector != null){
            int size = vecDegreeDataVector.size();
            for(int index = 0 ; index < size; index ++){
                ProposalPerDegreeFormBean cBean = (ProposalPerDegreeFormBean)vecDegreeDataVector.elementAt(index);
                if(cBean != null){
                    //System.out.println("ProposalNumber is "+cBean.getProposalNumber());
                    //System.out.println("PersonId is "+cBean.getPersonId());
                    //System.out.println("ActYpe is "+cBean.getAcType());
                    //System.out.println("Degree Code is "+cBean.getDegreeCode());
                    //System.out.println("Degree is "+cBean.getDegree());
                    //System.out.println("Graduationdate Code is "+cBean.getGraduationDate());
                    //System.out.println("UpdateTimeStamp is "+cBean.getUpdateTimestamp());
                    //System.out.println("Update User is "+cBean.getUpdateUser());
                }else{
                    //System.out.println("cBean is NULL");
                }
            }
        }
    }
    private Vector setProposalNumber(Vector vecDegreeDataFromTable){
        
        Vector vecPersons = null;
        DepartmentPerDegreeFormBean depBean = null;
        if(vecDegreeDataFromTable != null){
            vecPersons = new Vector();
            for(int index = 0; index < vecDegreeDataFromTable.size(); index++){
                depBean = (DepartmentPerDegreeFormBean)vecDegreeDataFromTable.elementAt(index);
                if(depBean != null){
                    ProposalPerDegreeFormBean pDegreeBean = new ProposalPerDegreeFormBean(depBean);
                    pDegreeBean.setProposalNumber(proposalId);
                    vecPersons.addElement(pDegreeBean);
                }
            }
        }
        return vecPersons;
    }
    
    /**
     * This method saves the educational details of a person to database.
     * This connects the servlet and performs the saving operation.
     */
    
    private void savePersonDegreeDetails(){
        if(functionType == DISPLAY_MODE){
            //Do Nothing
        }else{
            if(!isSaveRequired()){
            }else{
                Vector vecDegreeDataFromTable = getPersonDegreeData();
                String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/personMaintenanceServlet";
                RequesterBean request = new RequesterBean();
                request.setFunctionType(SAVE_RECORD);
                //request.setId(personId);
                request.setId(SAVE_DEGREE_DETAILS);
                request.setDataObject(vecDegreeDataFromTable);
                AppletServletCommunicator comm = new AppletServletCommunicator(
                                                            connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
                if (response!=null){
                    if (response.isSuccessfulResponse()){
                        dlgParentComponent.dispose();
                    }
                }
            }
        }
        dlgParentComponent.dispose();
    }
    /**
     * This method get the all modified educational details of a person.
     */
    private Vector getPersonDegreeData(){
        
        if(vecDeletedDegreeData != null){
            int delSize = vecDeletedDegreeData.size();
            DepartmentPerDegreeFormBean departmentPerDegreeBean = null;
            for(int index = 0; index < delSize; index++){
                departmentPerDegreeBean = (DepartmentPerDegreeFormBean)vecDeletedDegreeData.get(index);
                vecDegreeData.insertElementAt(departmentPerDegreeBean,index);
            }
        }
        //printBean();
        return vecDegreeData;
    }
    
    // Helper method which prints all the bean data.
    private void printBean(){
        
        if(vecDegreeData != null){
            int size = vecDegreeData.size();
            DepartmentPerDegreeFormBean dBean = null;
            for(int index = 0; index < size; index++){
                dBean = (DepartmentPerDegreeFormBean)vecDegreeData.elementAt(index);
                if(dBean != null){
                    
                    //System.out.println("Degree Code is "+dBean.getDegreeCode());
                    //System.out.println("Graduation Date is "+dBean.getGraduationDate());
                    //System.out.println("SchoolName is "+dBean.getSchool());
                    //System.out.println("SchoolId is "+dBean.getSchoolId());
                    //System.out.println("SchoolId Code is "+dBean.getSchoolIdCode());
                    //System.out.println("Degree is "+dBean.getDegree());
                    //System.out.println("TimeStamp is "+dBean.getUpdateTimestamp());
                    //System.out.println("User is "+dBean.getUpdateUser());
                    //System.out.println("Ac Type is "+dBean.getAcType());
                }
            }
        }
    }

    /** Getter for property proposalId.
     * @return Value of property proposalId.
     */
    public java.lang.String getProposalId() {
        return proposalId;
    }
    
    /** Setter for property proposalId.
     * @param proposalId New value of property proposalId.
     */
    public void setProposalId(java.lang.String proposalId) {
        this.proposalId = proposalId;
    }
    
    /** Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /** Getter for property canModifyProposal.
     * @return Value of property canModifyProposal.
     */
    public int getCanModifyProposal() {
        return canModifyProposal;
    }
    
    /** Setter for property canModifyProposal.
     * @param canModifyProposal New value of property canModifyProposal.
     */
    public void setCanModifyProposal(int canModifyProposal) {
        this.canModifyProposal = canModifyProposal;
        if(canModifyProposal == 1){
            maintainPerson = true;
        }
    }
    
    /** Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /** Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
    /*Bug Fix - the getter and setter methods*/
    /*to set the person name to the title of the dialog instaed of the person id*/
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblProposalNo;
    private javax.swing.JPanel pnlProposalDescriptionContainer;
    private javax.swing.JScrollPane scrPnPersonDegreeInfo;
    private javax.swing.JPanel pnlProposalDescription;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnOk;
    private javax.swing.JTable tblPersonDegreeInfo;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JLabel lblFullName;
    private javax.swing.JLabel lblProposalValue;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnModify;
    private javax.swing.JButton btnCancel;
    private javax.swing.JPanel pnlProposalPersondegreeInfo;
    private javax.swing.JLabel lblDegreeInfo;
    // End of variables declaration//GEN-END:variables
    
}
