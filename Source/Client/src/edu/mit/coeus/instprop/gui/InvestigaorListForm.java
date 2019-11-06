/*
 * InvestigaorListForm.java
 *
 * Created on November 24, 2004, 10:33 AM
 */
/*
 * PMD check performed, and commented unused imports and variables on 19-APR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.instprop.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.instprop.bean.InstituteProposalKeyPersonBean;
import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.instprop.bean.InstituteProposalInvestigatorBean;
import edu.mit.coeus.departmental.gui.CurrentAndPendingReportDetailForm;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import java.util.HashSet;

import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author  chandrashekara
 */
public class InvestigaorListForm extends javax.swing.JComponent implements ActionListener{
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgInvestigator;
    private QueryEngine queryEngine;
    private String queryKey;
    private InvestigatorTableModel investigatorTableModel;
    private final String PERSON_SERVLET = "/personMaintenanceServlet";
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + PERSON_SERVLET;
    private static final char GET_REPORT_DATA = 'K'; 
    private static final String SELECT_PERSON_FOR_REPORT="currentPendingReport_exceptionCode.1100";
    private CoeusMessageResources coeusMessageResources;
    private static final String NO_PERSONS = "currentPendingReport_exceptionCode.1101"; 
    
    private static final String  EMPTY_STRING = "";
    private static final int HEIGHT = 250;
    //Modified for COEUSDEV-151 : Current&Pending >Select Investigator screen: cropped Cancel button - Start
//    private static final int WIDTH = 470;
    private static final int WIDTH = 490;
    //COEUSDEV - 151 : END
    private static final int NAME_COLUMN=0;
    private static final int PI_COLUMN=1;
    
    private CoeusVector cvInvestigatorData;
    //Added for Case #2371 start 1
    private boolean openFromPropDev;
    //Added for Case #2371 end 1
    /** Creates new form InvestigaorListForm */
    public InvestigaorListForm(CoeusAppletMDIForm mdiForm,String queryKey) throws CoeusException{
        this.mdiForm = mdiForm;
        this.queryKey = queryKey;
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        registerComponents();
        //Commented for case #2371 start 2
       // setFormData();
        //Commented for case #2371 end 2
        setColumnData();
        postInitComponents();
        //Commented for case #2371 start 3
        //display();
        //Commented for case #2371 end 3
    }
    /** Register all the components and then add the listener and set the focus
     *traversal policy
     */
    private void registerComponents(){
        investigatorTableModel = new InvestigatorTableModel();
        tblInvestigators.setModel(investigatorTableModel);
        btnCancel.addActionListener(this);
        btnOK.addActionListener(this);
        
        java.awt.Component[] components = {
            btnCancel,btnOK
        };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
    }
    /** Get the data from the server and then set to the component
     */
    public void setFormData() throws CoeusException{
        cvInvestigatorData = new CoeusVector();
        CoeusVector cvKeyPersonData = new CoeusVector();
        cvInvestigatorData = queryEngine.executeQuery(
            queryKey,InstituteProposalInvestigatorBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        cvKeyPersonData = queryEngine.executeQuery(
            queryKey,InstituteProposalKeyPersonBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        
        if(cvKeyPersonData != null && cvKeyPersonData.size() > 0){
            cvInvestigatorData.addAll(cvKeyPersonData);
        }
        CoeusVector cvAllData = removeDuplicateData(cvInvestigatorData);
        
        Object [] bothInvestigatorAndKeyPerson = (Object [])cvAllData.get(1);
        CoeusVector cvCurrentData = (CoeusVector)cvAllData.get(0);
        String personName="";
        InstituteProposalInvestigatorBean bean=null;
        InstituteProposalKeyPersonBean keyPersonDetails=null;
        int counter = bothInvestigatorAndKeyPerson.length;
        for(Object currentData : cvCurrentData){
            if(currentData instanceof InstituteProposalInvestigatorBean){
                bean = (InstituteProposalInvestigatorBean)currentData;
                personName = bean.getPersonName();
            }else if(currentData instanceof InstituteProposalKeyPersonBean){
                keyPersonDetails = (InstituteProposalKeyPersonBean)currentData;
                personName = keyPersonDetails.getPersonName();
            }
            for(int start=0; start<counter;start++){
                if(bothInvestigatorAndKeyPerson[start].equals(personName)){
                    if(bean!=null){
                        bean.setBothPIAndKeyPerson(true);
                    }else if(keyPersonDetails!=null){
                        keyPersonDetails.setBothPIAndKeyPerson(true);
                    }
                }
            }
            bean = null;
            keyPersonDetails = null;
        }
        
        cvInvestigatorData = cvCurrentData;       
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        investigatorTableModel.setData(cvInvestigatorData);
    }
    /** Added for Case #2371 start 4
     */
    public void setFormData(Object data) throws CoeusException{
        cvInvestigatorData = new CoeusVector();
        Vector vecInvData = (Vector)data;
        cvInvestigatorData.addAll(vecInvData);
        investigatorTableModel.setData(cvInvestigatorData);
    } 
    //Added for case #2371 end 4
    /** Set the table header and set the properticies to it.
     *specifies the column width and height and width
     */
     private void setColumnData(){
        JTableHeader tableHeader = tblInvestigators.getTableHeader();
        //tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        tblInvestigators.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblInvestigators.setRowHeight(22);
        tblInvestigators.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tblInvestigators.setShowHorizontalLines(false);
        tblInvestigators.setShowVerticalLines(false);
        tblInvestigators.setOpaque(false);
        tblInvestigators.setSelectionMode(  DefaultListSelectionModel.SINGLE_SELECTION);
        tblInvestigators.setRowSelectionAllowed(true);
        
        TableColumn column = tblInvestigators.getColumnModel().getColumn(NAME_COLUMN);
        column.setPreferredWidth(303);
        column.setResizable(true);
        
        column = tblInvestigators.getColumnModel().getColumn(PI_COLUMN);
        column.setPreferredWidth(50);
        column.setResizable(true);
        
    }
    
    /** Specifies the Modal window */
    private void postInitComponents() {
        dlgInvestigator = new CoeusDlgWindow(mdiForm);
        dlgInvestigator.getContentPane().add(this);
        dlgInvestigator.setTitle("Select Investigator/Key Person");
        dlgInvestigator.setFont(CoeusFontFactory.getLabelFont());
        dlgInvestigator.setModal(true);
        dlgInvestigator.setResizable(false);
        dlgInvestigator.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgInvestigator.getSize();
        dlgInvestigator.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgInvestigator.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                 performCancelAction();
                return;
            }
        });
        dlgInvestigator.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgInvestigator.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                 performCancelAction();
                return;
            }
        });
        
        dlgInvestigator.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    // Set the focus on the window when it is loaded
    private void setWindowFocus(){
        btnCancel.requestFocusInWindow();
    }
    // clsoe the modal window when user clicks on cancel button
    private void performCancelAction(){
        dlgInvestigator.dispose();
    }
    
    /** Display the modal window only if the data about the persons exists
     *else show the respective message
     */
    public void display(){
        if(cvInvestigatorData!= null && cvInvestigatorData.size()>0){
            dlgInvestigator.setVisible(true);
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_PERSONS));
            dlgInvestigator.setVisible(false);
        }
    }
    /** Listener for the corresponding buttons
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source  = actionEvent.getSource();
        try{
            dlgInvestigator.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if(source.equals(btnOK)){
                performOKAction();
            }else if(source.equals(btnCancel)){
                performCancelAction();
                
            }
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }
        finally{
            dlgInvestigator.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }    
    /** Once user selects the row and clicks ok then populate the Current&Pending 
     *Support report else popup the respective message
     */
    private void performOKAction() throws CoeusClientException{
        int selRow = tblInvestigators.getSelectedRow();
        if(selRow==-1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_PERSON_FOR_REPORT));
            dlgInvestigator.setVisible(true);
        }else{
          showCurrentAndPendingDetails();
        }
    }
    /** Show the Current&PendingSupportReport form get the data by making server 
     *call and populate the window
     */
    private void showCurrentAndPendingDetails() throws CoeusClientException{
        try{
            CurrentAndPendingReportDetailForm  currentAndPendingReportDetailForm;
            int selectedRow = tblInvestigators.getSelectedRow();
            if(selectedRow!= -1){
                //Modified for case #2371 start 5
                String personId = null;
                boolean investigatorAndKeyPerson = false;
                if(isOpenFromPropDev()){
                    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                    //ProposalInvestigatorFormBean selBean = (ProposalInvestigatorFormBean)cvInvestigatorData.get(selectedRow);
                    //personId = selBean.getPersonId();
                    Object invesKeyPersonObject = cvInvestigatorData.get(selectedRow);
                    if(invesKeyPersonObject instanceof ProposalInvestigatorFormBean){
                        ProposalInvestigatorFormBean selBean = (ProposalInvestigatorFormBean)invesKeyPersonObject;
                        personId = selBean.getPersonId();
                        investigatorAndKeyPerson = selBean.isBothPIAndKeyPerson();
                    }else if(invesKeyPersonObject instanceof ProposalKeyPersonFormBean){
                        ProposalKeyPersonFormBean keyPersonDetails = (ProposalKeyPersonFormBean)invesKeyPersonObject;
                        personId = keyPersonDetails.getPersonId();
                        investigatorAndKeyPerson = keyPersonDetails.isBothPIAndKeyPerson();
                    }
                    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                }else{
                    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                    //InstituteProposalInvestigatorBean selBean = (InstituteProposalInvestigatorBean)cvInvestigatorData.get(selectedRow);
                    //personId = selBean.getPersonId();
                    Object invesKeyPersonObject = cvInvestigatorData.get(selectedRow);
                    if(invesKeyPersonObject instanceof InstituteProposalInvestigatorBean){
                        InstituteProposalInvestigatorBean selBean = (InstituteProposalInvestigatorBean)invesKeyPersonObject;
                        personId = selBean.getPersonId();
                        investigatorAndKeyPerson = selBean.isBothPIAndKeyPerson();
                    }else if(invesKeyPersonObject instanceof InstituteProposalKeyPersonBean){
                        InstituteProposalKeyPersonBean keyPersonDetails = (InstituteProposalKeyPersonBean)invesKeyPersonObject;
                        personId = keyPersonDetails.getPersonId();
                        investigatorAndKeyPerson = keyPersonDetails.isBothPIAndKeyPerson();
                    }
                    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                }
                //Modified for case #2371 end 5
                String personName = (String)tblInvestigators.getValueAt(selectedRow, NAME_COLUMN);
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                //Hashtable reportingData = getCurrentPendingData(personId);
                Hashtable reportingData = getCurrentPendingData(personId, investigatorAndKeyPerson);
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                // Check if the internalFrame is opened, if yes then pass the data..
                // No need to initialize the form components again
                if( (currentAndPendingReportDetailForm = (CurrentAndPendingReportDetailForm)mdiForm.getFrame(
                CoeusGuiConstants.CURRENT_AND_PENDING_SUPPORT))!= null ){
                    if( currentAndPendingReportDetailForm.isIcon() ){
                        currentAndPendingReportDetailForm.setIcon(false);
                    }
                    currentAndPendingReportDetailForm.setHeaderValue(personName);
                    currentAndPendingReportDetailForm.setReportData(reportingData);
                    currentAndPendingReportDetailForm.setFormData();
                    currentAndPendingReportDetailForm.setSelected( true );
                    dlgInvestigator.setVisible(false);
                    return;
                }
                currentAndPendingReportDetailForm = new CurrentAndPendingReportDetailForm(CoeusGuiConstants.CURRENT_AND_PENDING_SUPPORT, mdiForm);
                currentAndPendingReportDetailForm.initComponents();
                currentAndPendingReportDetailForm.setHeaderValue(personName);
                currentAndPendingReportDetailForm.setReportData(reportingData);
                currentAndPendingReportDetailForm.setFormData();
                dlgInvestigator.setVisible(false);
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_PERSON_FOR_REPORT));
                dlgInvestigator.setVisible(true);
            }
        }catch (CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
            CoeusOptionPane.showDialog(coeusClientException);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    
    // Making server call to get the current and Pending report details
    private Hashtable getCurrentPendingData(String personId , boolean investigatorAndKeyPerson) throws CoeusClientException{
        Hashtable data=null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(GET_REPORT_DATA);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
        //requesterBean.setDataObject(personId);
        CoeusVector cvPersonData = new CoeusVector();
        cvPersonData.addElement(personId);
        cvPersonData.addElement(investigatorAndKeyPerson);
        requesterBean.setDataObjects(cvPersonData);
        //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.isSuccessfulResponse()) {
                data = (Hashtable)responderBean.getDataObject();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
                
            }
        }
        return data;
    }// end getCurrentPendingData(String personId)
    
    /** Customized table model.Extends the AbstarctTableModel and overrides basic 
     *methods.
     *This Inner class provides the methods to hold the data
     */
    public class InvestigatorTableModel extends AbstractTableModel{
        private String[] colName = {"Name","PI"};
        private Class[] colClass = {String.class,Boolean.class};
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public int getColumnCount() {
            return colName.length;
        }
        
        public String getColumnName(int col){
            return colName[col];
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public void setData(CoeusVector cvInvestigatorData){
            cvInvestigatorData = cvInvestigatorData;
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            if(cvInvestigatorData==null){
                return 0;
            }else{
                return cvInvestigatorData.size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            //Modified for case #2371 start 6
            if(isOpenFromPropDev()){
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                Object invesKeyPersonObject = cvInvestigatorData.get(row);
                if(invesKeyPersonObject instanceof ProposalInvestigatorFormBean){
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                    ProposalInvestigatorFormBean bean =
                            (ProposalInvestigatorFormBean)cvInvestigatorData.get(row);
                    switch(col){
                        case NAME_COLUMN:
                            return bean.getPersonName();
                        case PI_COLUMN:
                            return new Boolean(bean.isPrincipleInvestigatorFlag());
                    }
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                }
                else if(invesKeyPersonObject instanceof ProposalKeyPersonFormBean){
                    ProposalKeyPersonFormBean keyPersonDetails = (ProposalKeyPersonFormBean)cvInvestigatorData.get(row);
                    switch(col){
                        case NAME_COLUMN:
                            return keyPersonDetails.getPersonName();
                        case PI_COLUMN:
                            return false;
                    }
                }
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
            }else{
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                Object invesKeyPersonObject = cvInvestigatorData.get(row);
                if(invesKeyPersonObject instanceof InstituteProposalInvestigatorBean){
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
                    InstituteProposalInvestigatorBean bean =
                            (InstituteProposalInvestigatorBean)cvInvestigatorData.get(row);
                    switch(col){
                        case NAME_COLUMN:
                            return bean.getPersonName();
                        case PI_COLUMN:
                            return new Boolean(bean.isPrincipalInvestigatorFlag());
                    }
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
                }else if(invesKeyPersonObject instanceof InstituteProposalKeyPersonBean){
                    InstituteProposalKeyPersonBean bean =
                            (InstituteProposalKeyPersonBean)cvInvestigatorData.get(row);
                    switch(col){
                        case NAME_COLUMN:
                            return bean.getPersonName();
                        case PI_COLUMN:
                            return false;
                    }
                }
                //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
            }
            //Modified for case #2371 end 6
            return EMPTY_STRING;
        }
    }// End of TableModel
    //Added for Case #2371 start 7
    /**
     * Getter for property openFromPropDev.
     * @return Value of property openFromPropDev.
     */
    public boolean isOpenFromPropDev() {
        return openFromPropDev;
    }
    
    /**
     * Setter for property openFromPropDev.
     * @param openFromPropDev New value of property openFromPropDev.
     */
    public void setOpenFromPropDev(boolean openFromPropDev) {
        this.openFromPropDev = openFromPropDev;
    }
    //Added for Case #2371 end 7
    
   //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    /** This method is used to remove the duplicate data before displaying the person names for selection     
     * @param CoeusVector vecInvData
     * @return CoeusVector after duplicate data is removed
     */
    public CoeusVector removeDuplicateData(CoeusVector vecInvData){
       HashSet hsBothInvestigatorAndKeyPerson = new HashSet();
       CoeusVector cvAllData = new CoeusVector();
       for(int mainIndex=0;mainIndex < vecInvData.size();mainIndex++){
           Object mainInvesKeyPersonObject = vecInvData.get(mainIndex);
           String mainPersonName="";
           boolean principalInvestigator = false;
           int counter = 0;
           //To check whether the bean is investigator or key person
           if(mainInvesKeyPersonObject instanceof InstituteProposalInvestigatorBean){
               InstituteProposalInvestigatorBean bean = (InstituteProposalInvestigatorBean)vecInvData.get(mainIndex);
               mainPersonName = bean.getPersonName();
               principalInvestigator = bean.isPrincipalInvestigatorFlag();
           }else if(mainInvesKeyPersonObject instanceof InstituteProposalKeyPersonBean){
               InstituteProposalKeyPersonBean keyPersonDetails = (InstituteProposalKeyPersonBean)vecInvData.get(mainIndex);
               mainPersonName = keyPersonDetails.getPersonName();
               principalInvestigator = false;
           }
           //To loop through the objects in the vector
           for(int index=0;index < vecInvData.size();index++){
               Object invesKeyPersonObject = vecInvData.get(index);
               String personName = CoeusGuiConstants.EMPTY_STRING;
               String keyPersonName = CoeusGuiConstants.EMPTY_STRING;
               //To check whether the bean is investigator or key person
               if(invesKeyPersonObject instanceof InstituteProposalInvestigatorBean){
                   InstituteProposalInvestigatorBean bean = (InstituteProposalInvestigatorBean)vecInvData.get(index);
                   personName = bean.getPersonName();
               }else if(invesKeyPersonObject instanceof InstituteProposalKeyPersonBean){
                   InstituteProposalKeyPersonBean keyPersonDetails = (InstituteProposalKeyPersonBean)vecInvData.get(index);
                   keyPersonName = personName = keyPersonDetails.getPersonName();
               }
               //To check whether the person is principal investigator
               if(mainPersonName!= CoeusGuiConstants.EMPTY_STRING && mainPersonName.equals(personName) && !principalInvestigator){
                   //If the person is not a investigator increment the counter
                   counter++;
                   //Add the person to the Set to remove the duplicate entry
                   hsBothInvestigatorAndKeyPerson.add(mainPersonName);
               }else if(mainPersonName!= CoeusGuiConstants.EMPTY_STRING && mainPersonName.equals(personName) && principalInvestigator){
                   //If the person is investigator increment the counter
                   counter++;
                   //Add the person to the Set to remove the duplicate entry
                   if(keyPersonName!= CoeusGuiConstants.EMPTY_STRING && personName.equals(keyPersonName)){
                        hsBothInvestigatorAndKeyPerson.add(mainPersonName);
                   }
                   if(counter > 1){                       
                       vecInvData.removeElementAt(index);
                       counter--;
                   }
               }
           }
           if(counter > 1){
               vecInvData.removeElementAt(mainIndex);
           }
       }
       Object bothInvesAndKeyPer [] = hsBothInvestigatorAndKeyPerson.toArray();
       cvAllData.add(vecInvData);
       cvAllData.add(bothInvesAndKeyPer);
       return cvAllData;
   }
   //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnInvestigators = new javax.swing.JScrollPane();
        tblInvestigators = new javax.swing.JTable();
        btnOK = new edu.mit.coeus.utils.CoeusButton();
        btnCancel = new edu.mit.coeus.utils.CoeusButton();
        lblHeader = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        scrPnInvestigators.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrPnInvestigators.setMinimumSize(new java.awt.Dimension(357, 250));
        scrPnInvestigators.setPreferredSize(new java.awt.Dimension(357, 250));
        tblInvestigators.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scrPnInvestigators.setViewportView(tblInvestigators);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(scrPnInvestigators, gridBagConstraints);

        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMinimumSize(new java.awt.Dimension(72, 25));
        btnOK.setPreferredSize(new java.awt.Dimension(72, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 0, 0);
        add(btnOK, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMaximumSize(new java.awt.Dimension(47, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(72, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(72, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 11, 0, 0);
        add(btnCancel, gridBagConstraints);

        lblHeader.setFont(CoeusFontFactory.getLabelFont());
        lblHeader.setText("Select an Investigator/Key Person to generate Support Report");
        lblHeader.setMaximumSize(new java.awt.Dimension(355, 14));
        lblHeader.setMinimumSize(new java.awt.Dimension(355, 14));
        lblHeader.setPreferredSize(new java.awt.Dimension(355, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblHeader, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private edu.mit.coeus.utils.CoeusButton btnCancel;
    private edu.mit.coeus.utils.CoeusButton btnOK;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JScrollPane scrPnInvestigators;
    private javax.swing.JTable tblInvestigators;
    // End of variables declaration//GEN-END:variables
    
}
