/*
 * TemplateAddRecpController.java
 *
 * Created on December 21, 2004, 7:50 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.bean.AwardTemplateContactsBean;
import edu.mit.coeus.admin.bean.TemplateBaseBean;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;

/**
 *
 * @author  ajaygm
 */
public class TemplateAddRecpController extends AwardTemplateController
implements ActionListener,ListSelectionListener {
    
    /*Creates an instance of contacts bean used for populating the table*/
//    private AwardContactDetailsBean awardContactDetailsBean = new AwardContactDetailsBean();
//    private TemplateContactBean templateContactBean = new TemplateContactBean();
    private AwardTemplateContactsBean awardTemplateContactsBean = new AwardTemplateContactsBean();
    
    
    /*Holds an instace of the form*/
    private AwardHeaderForm awardHeaderForm = new AwardHeaderForm();
    
    /**
     * Instance of the Dialog
     */ 
    private CoeusDlgWindow dlgAwardAddRecipientsForm;
    
    /**
     * To create an instance of MDIform
     */ 
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /**
     * Instance of Coeus Message Resources
     */ 
    private CoeusMessageResources coeusMessageResources;

    /*Holds an instance of form*/
    private AwardAddRecipientsForm awardAddRecipientsForm;
    
    /*Listener*/
    private BaseWindowObservable observable  = new BaseWindowObservable();
    
    /**
     * Instance of Query Engine
     */
    private QueryEngine queryEngine;
    
    /*CoeusVector For setting seq no and award no*/
    private CoeusVector cvAwardDetailsBean;
    
    /*CoeusVectors of contacts bean */
    private CoeusVector cvContactType;
    private CoeusVector cvAwardContactBean;

    /*Contains the persons selected*/
    private CoeusVector cvSelectedPersons = new CoeusVector();

    private char functionType;    
    
    private ListSelectionModel contactsSelectionModel;
    
    /*For setting dimentions*/
    private static final String WINDOW_TITLE = "Add Recipients";
    private static final int WIDTH = 610;
    private static final int HEIGHT = 375;
    
    private static final String EMPTY = "";
    
    private static final java.awt.Color  PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");

    /*For setting the column index*/
    private static final int HAND_ICON_COLUMN = 0;
    private static final int CONTACT_TYPE_COLUMN = 1;
    private static final int NAME_COLUMN = 2;
    
    /*Table model editor and renderer*/ 
    private AwardAddRecipientsTableModel awardAddRecipientsTableModel;
    private AwardAddRecipientsRenderer awardAddRecipientsRenderer;
    private IconRenderer iconRenderer;
    private EmptyHeaderRenderer emptyHeaderRenderer;
    // Case# 3294:Erroneous message when adding contact to Report Type - Start
    // Variable Renamed and Changed the Exception Message.
//    private static final String SELECT_ROW = "search_exceptionCode.1119";
    private static final String SELECT_CONTACT = "awardReports_exceptionCode.1259";
    // Case# 3294:Erroneous message when adding contact to Report Type - End
    private static final String SELECT_CONTACT_WITH_CONTACT_TYPE = "awardAddRecipients_exceptionCode.1701";
    
    
    /** Creates a new instance of TemplateAddRecpController */
    public TemplateAddRecpController(TemplateBaseBean templateBaseBean , char funType) {
        super(templateBaseBean);
        queryEngine = QueryEngine.getInstance ();
        coeusMessageResources = CoeusMessageResources.getInstance();
        this.functionType = funType;
        setFunctionType(funType);
        
        observable.setFunctionType(funType);
        
        awardAddRecipientsForm = new AwardAddRecipientsForm();
        
        awardAddRecipientsTableModel = new AwardAddRecipientsTableModel();
        awardAddRecipientsRenderer = new AwardAddRecipientsRenderer();
        iconRenderer = new IconRenderer();
        emptyHeaderRenderer = new EmptyHeaderRenderer();
        
        awardAddRecipientsForm.tblContactList.setModel(awardAddRecipientsTableModel);
        registerComponents();
        setFormData(null);
        setTableEditors();
        postInitComponents();
    }
    
    /**
     * Registers the Observer
     */    
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
  
    /**
     * Creates a dialog instance and adds the forms to the dialog.
     */    
    public void postInitComponents(){
        dlgAwardAddRecipientsForm = new CoeusDlgWindow(mdiForm);
        dlgAwardAddRecipientsForm.setResizable(false);
        dlgAwardAddRecipientsForm.setModal(true);
        dlgAwardAddRecipientsForm.getContentPane().add(awardAddRecipientsForm);
        
        dlgAwardAddRecipientsForm.setTitle(WINDOW_TITLE);
        dlgAwardAddRecipientsForm.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardAddRecipientsForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardAddRecipientsForm.getSize();
        dlgAwardAddRecipientsForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
         dlgAwardAddRecipientsForm.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        dlgAwardAddRecipientsForm.pack();
    }
    
    
    /**
     * Requests for the default focus when dialog is opened
     */    
    public void requestDefaultFocus(){    
        awardAddRecipientsForm.btnOK.requestFocus();
    }
    
    /**
     * Displays the dialog
     */    
    public void display() {
        dlgAwardAddRecipientsForm.setVisible(true);
    }
    
    /**
     * Performs field formatting like enabling,
     * disabling components depending on the function type.
     */    
    public void formatFields() {
    }
    
    /** returns the Component which is being controlled by this Controller.
     * @return Component which is being controlled by this Controller.
     */   
    public Component getControlledUI() {
        return awardAddRecipientsForm;
    }
    
    /** Returns the form data
     * @return the form data
     */
    public Object getFormData() {
        return null;
    }
    
    /* Registers GUI Components with event Listeners. */
    public void registerComponents() {
        
        awardAddRecipientsForm.btnOK.addActionListener (this);
        awardAddRecipientsForm.btnCancel.addActionListener(this);
        awardAddRecipientsForm.btnRolodex.addActionListener(this);
        
        contactsSelectionModel = awardAddRecipientsForm.tblContactList.getSelectionModel();
        contactsSelectionModel.addListSelectionListener(this);
        awardAddRecipientsForm.tblContactList.setSelectionModel(contactsSelectionModel);
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { awardAddRecipientsForm.btnOK,
        awardAddRecipientsForm.btnCancel,awardAddRecipientsForm.btnRolodex,
        awardAddRecipientsForm.tblContactList
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardAddRecipientsForm.setFocusTraversalPolicy(traversePolicy);
        awardAddRecipientsForm.setFocusCycleRoot(true);
         
        /** Code for focus traversal - end */
    }
    
    /* Saves the Form Data. */
    public void saveFormData() {
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        /*Create an instance of Coeus Vector to get
         *the Sponsor Award Number ,Award Number*/
        cvAwardDetailsBean = new CoeusVector();   
        cvAwardContactBean = new CoeusVector();
        cvContactType = new CoeusVector();
        try{
            /*Commented as not required*/
            /*For getting Award Details Bean to set Award No, Seq No, Sponsor No*/
//            cvAwardDetailsBean = queryEngine.executeQuery (
//            queryKey,AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            /*For getting Award Details Bean to setting data to table*/
            cvAwardContactBean = queryEngine.executeQuery (
            queryKey,AwardTemplateContactsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
           
            
            cvContactType = queryEngine.getDetails(queryKey, KeyConstants.CONTACT_TYPES );
            NotEquals neNull = new NotEquals("description", null);
            NotEquals neEmpty = new NotEquals("description", " ");
            And emptyAndNull  = new And(neNull, neEmpty);
            cvContactType = cvContactType.filter(emptyAndNull);
            cvContactType.sort("description");
        }catch (CoeusException coeusException){
            coeusException.printStackTrace ();
        }
        /*Commented as not required*/
//        AwardDetailsBean awardDetailsBean = (AwardDetailsBean)cvAwardDetailsBean.get(0);
//        awardHeaderForm.setFormData (awardDetailsBean);
        
        
        awardAddRecipientsTableModel.setData(cvAwardContactBean);
        
         if( cvAwardContactBean != null && cvAwardContactBean.size() > 0){
            awardAddRecipientsForm.tblContactList.setRowSelectionInterval(0,0);
            awardTemplateContactsBean = (AwardTemplateContactsBean)cvAwardContactBean.get(0);
            awardAddRecipientsForm.awardContactDetailsForm.setTemplateData(awardTemplateContactsBean);
        }else if(cvAwardContactBean == null) {
            awardAddRecipientsForm.awardContactDetailsForm.setTemplateData(awardTemplateContactsBean);
        }
     }
    
    /** validate the form data/Form and returns true if validation is through
     * else returns false.
     * @throws CoeusUIException CoeusUIException if some exception occurs or some validation fails.
     * @return boolean if<CODE>true</CODE> validation is through
     */
    public boolean validate() throws CoeusUIException {
        return true;
    }
    
    /** actionPerfomed method for the Buttons
     * @param actionEvent ActionEvent object
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        
        if(source.equals(awardAddRecipientsForm.btnOK)){
            if(awardAddRecipientsForm.tblContactList.getSelectedRow () == -1){
                CoeusOptionPane.showInfoDialog (coeusMessageResources.parseMessageKey (SELECT_CONTACT));
            }else if(getSelectedPersons ()){
                observable.notifyObservers(cvSelectedPersons);
            }
        }else if(source.equals(awardAddRecipientsForm.btnCancel)){
            observable.notifyObservers(EMPTY);
        }else if(source.equals(awardAddRecipientsForm.btnRolodex)){
            performRolodexSearch();
        }
    }
    
    private void setTableEditors(){
        awardAddRecipientsForm.tblContactList.setRowHeight(22);
        awardAddRecipientsForm.tblContactList.setShowHorizontalLines(false);
        awardAddRecipientsForm.tblContactList.setShowVerticalLines(false);
        
        JTableHeader header = awardAddRecipientsForm.tblContactList.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
        header.setReorderingAllowed(false);
        
        awardAddRecipientsForm.tblContactList.setOpaque(false);
        awardAddRecipientsForm.tblContactList.setSelectionMode(
        DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        
        TableColumn column = awardAddRecipientsForm.tblContactList.getColumnModel().getColumn(HAND_ICON_COLUMN);
        
        column.setMaxWidth(30);
        column.setMinWidth(30);
        column.setPreferredWidth(30);
        column.setCellRenderer(iconRenderer);
        column.setHeaderRenderer(emptyHeaderRenderer);
        
        column = awardAddRecipientsForm.tblContactList.getColumnModel().getColumn(1);
        column.setPreferredWidth(50);
        column.setMinWidth(50);
        column.setCellRenderer(awardAddRecipientsRenderer);
        header.setReorderingAllowed(false);
        
        column = awardAddRecipientsForm.tblContactList.getColumnModel().getColumn(2);
        column.setPreferredWidth(80);
        column.setPreferredWidth(80);
        column.setCellRenderer(awardAddRecipientsRenderer);
        header.setReorderingAllowed(false);
    }

    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if( !listSelectionEvent.getValueIsAdjusting() ){
            int selectedRow = awardAddRecipientsForm.tblContactList.getSelectedRow();
            if (selectedRow != -1) {
                AwardTemplateContactsBean awardTemplateContactsBean =
                (AwardTemplateContactsBean)cvAwardContactBean.get(selectedRow);
                awardAddRecipientsForm.awardContactDetailsForm.setTemplateData(awardTemplateContactsBean);
            }
        }
    }
    
    
    
    public class AwardAddRecipientsRenderer extends DefaultTableCellRenderer{
        
        public AwardAddRecipientsRenderer() {
            BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray);
            setBorder(bevelBorder);
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus, int row, int column){
            
            switch(column) {
                case HAND_ICON_COLUMN:
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;

                case CONTACT_TYPE_COLUMN:
                    setText(value.toString());
                       if(isSelected){
                            setBackground(new java.awt.Color(10,36,106));
                            setForeground (Color.WHITE);
                        }else{
                            setBackground(Color.WHITE);
                            setForeground (Color.BLACK);
                        }
                                
                    return this;
                case NAME_COLUMN:
                    setText(value.toString());
                    if(isSelected){
                            setBackground(new java.awt.Color(10,36,106));
                            setForeground (Color.WHITE);
                        }else{
                            setForeground (Color.BLACK);
                            setBackground(PANEL_BACKGROUND_COLOR);
                        }
                    return this;
            }
            return null;
        }
    }
    
    public class AwardAddRecipientsTableModel extends AbstractTableModel{
        private CoeusVector cvfilteredVector;
        String contactType;
        
        String colNames[] = {"","Contact Type", "Name/Organization"};
        Class colTypes[] = {ImageIcon.class , String.class, String.class};
       
        public boolean isCellEditable(int row, int column) {
                return false;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
         public void setData(CoeusVector cvAwardContactBean) {
            cvAwardContactBean = cvAwardContactBean;
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public int getRowCount() {
            if( cvAwardContactBean == null ){
                return 0;
            }else{
            return cvAwardContactBean.size();
        }
            
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            AwardTemplateContactsBean contactDetailsBean = (AwardTemplateContactsBean)cvAwardContactBean.get(rowIndex);
            switch(columnIndex) {
                case HAND_ICON_COLUMN:
                    return EMPTY;
                case CONTACT_TYPE_COLUMN:
                    int contactCode = contactDetailsBean.getContactTypeCode();
                    cvfilteredVector = cvContactType.filter(
                    new Equals("code", ""+contactCode));
                    if(cvfilteredVector != null && cvfilteredVector.size () > 0) {
                        ComboBoxBean comboBoxBean = (ComboBoxBean)cvfilteredVector.get (0);
                        contactType = comboBoxBean.getDescription ().toString ();
                        return contactType;
                    }else {
                        return EMPTY;
                    }
                    
                case NAME_COLUMN:
                    String rolodexName;
                    String firstName = (contactDetailsBean.getFirstName() == null ?EMPTY:contactDetailsBean.getFirstName());
                    if ( firstName.length() > 0) {
                        String suffix = (contactDetailsBean.getSuffix() == null ?EMPTY:contactDetailsBean.getSuffix());
                        String prefix = (contactDetailsBean.getPrefix() == null ?EMPTY:contactDetailsBean.getPrefix());
                        String middleName = (contactDetailsBean.getMiddleName() == null ?EMPTY:contactDetailsBean.getMiddleName());
                        rolodexName = (contactDetailsBean.getLastName() + " "+
                        suffix+", "+ prefix+" "+ firstName+" "+
                        middleName).trim();
                        
                    } else {
                        rolodexName = checkForNull(contactDetailsBean.getOrganization());
                    }
                    return rolodexName;
            }
            return EMPTY;
            
        }
        

        public void setValueAt(Object value, int row, int column){
            AwardTemplateContactsBean awardTemplateContactsBean = (AwardTemplateContactsBean)cvAwardContactBean.get(row);
            if(column == CONTACT_TYPE_COLUMN) {
                if(value==null || value.toString().equals(EMPTY)) {
                    return ;
                }
                ComboBoxBean comboBoxBean = (ComboBoxBean)cvContactType.filter(new Equals("description", value.toString())).get(0);
                int contactCode = Integer.parseInt(comboBoxBean.getCode());
                
                if( contactCode != awardTemplateContactsBean.getContactTypeCode() ){
                    awardTemplateContactsBean.setContactTypeCode(contactCode);
                    
                } else if(column == NAME_COLUMN) {
                    if(awardTemplateContactsBean.getFirstName()!= null &&
                    awardTemplateContactsBean.getFirstName().equals(value.toString())) {
                        awardTemplateContactsBean.setFirstName(value.toString().trim());
                    } else if( awardTemplateContactsBean.getFirstName().trim().equals(EMPTY)) {
                        awardTemplateContactsBean.setOrganization(value.toString().trim());
                    }
                }
            }
        }
    }
    
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();
    }
    
    /** This is Iconrendere to display HAND icon for the selected row in the table
     */
   public class IconRenderer  extends DefaultTableCellRenderer {
        
        
        /** This holds the Image Icon of Hand Icon
         */
        private final ImageIcon HAND_ICON =
        new ImageIcon(getClass().getClassLoader().getResource(
        CoeusGuiConstants.HAND_ICON));
        private final ImageIcon EMPTY_ICON = null;
        /** Default Constructor*/
        IconRenderer() {
            
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row,
        int column) {
            
            setText((String)value);
            setOpaque(false);
            /* if row is selected the place the icon in this cell wherever this
               renderer is used. */
            int arr[] = awardAddRecipientsForm.tblContactList.getSelectedRows ();
            
            int length = arr.length;
            
            if( isSelected ){
                if(row == arr[length-1]){
                    setIcon(HAND_ICON);
                }else{
                    setIcon(EMPTY_ICON);
                }
            }else{
                
                setIcon(EMPTY_ICON);
            }
            return this;
        }
    }//End Icon Rendering inner class
    
    
    /**
     * Inner class which is used to provide empty header for the Icon Column.
     */
    
    public class EmptyHeaderRenderer extends JList implements TableCellRenderer {
        /**
         * Default constructor to set the default foreground/background
         * and border properties of this renderer for a cell.
         */
        EmptyHeaderRenderer() {
            setOpaque(true);
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setBackground(UIManager.getColor("TableHeader.background"));
            setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
            ListCellRenderer renderer = getCellRenderer();
            ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
            setCellRenderer(renderer);
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value,boolean isSelected, boolean hasFocus, int row, int column){
            return this;
        }
    }
    
    private void performRolodexSearch() {
        CoeusSearch proposalSearch = null;
        Vector vecRolodex = null;
        HashMap rolodexData = null;
        //dlgAwardAddRecipientsForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        try{
            awardAddRecipientsForm.btnRolodex.getParent().setCursor (new Cursor(Cursor.WAIT_CURSOR));
            proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),
            CoeusGuiConstants.ROLODEX_SEARCH,
            CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
            proposalSearch.showSearchWindow();
            vecRolodex = proposalSearch.getMultipleSelectedRows();
        }catch( Exception err ){
            err.printStackTrace();
        }
        finally{
            awardAddRecipientsForm.btnRolodex.getParent ().setCursor (new Cursor(Cursor.DEFAULT_CURSOR));
        }
        if ( vecRolodex != null ){
            int index = 0;
            for(  index = 0; index < vecRolodex.size(); index++ ){
                rolodexData = (HashMap)vecRolodex.get(index) ;
                if( rolodexData == null || rolodexData.isEmpty() ){
                    continue;
                }
                setRolodexData(rolodexData);
                /*Commented for Rep Req*/
                //observable.notifyObservers(cvSelectedPersons);
            }       
            /*Added for rep req*/ //check
            observable.notifyObservers(cvSelectedPersons);
        }
    }
    
    /*For serring the data form contact bean to award report terms bean*/
    private void setRolodexData(HashMap rolodexData ){
        String rolodexID = checkForNull(rolodexData.get( "ROLODEX_ID" ));
        String firstName = checkForNull(rolodexData.get( "FIRST_NAME" ));
        String lastName = checkForNull(rolodexData.get( "LAST_NAME" ));
        String middleName = checkForNull(rolodexData.get( "MIDDLE_NAME" ));
        String preffix = checkForNull(rolodexData.get( "PREFIX" ));
        String suffix = checkForNull(rolodexData.get( "SUFFIX" ));
        String title = checkForNull(rolodexData.get( "TITLE" ));
        String sponsorCode = checkForNull(rolodexData.get( "SPONSOR_CODE" ));
        String sponsorName = checkForNull(rolodexData.get( "SPONSOR_NAME" ));
        String organization = checkForNull(rolodexData.get( "ORGANIZATION" ));
        String address1 = checkForNull(rolodexData.get( "ADDRESS_LINE_1" ));
        String address2 = checkForNull(rolodexData.get( "ADDRESS_LINE_2" ));
        String address3 = checkForNull(rolodexData.get( "ADDRESS_LINE_3" ));
        String county = checkForNull(rolodexData.get( "COUNTY" ));
        String city = checkForNull(rolodexData.get( "CITY" ));
        String state = checkForNull(rolodexData.get( "STATE" ));
        String postalCode = checkForNull(rolodexData.get( "POSTAL_CODE" ));
        String countryCode = checkForNull(rolodexData.get( "COUNTRY_CODE" ));
        String emailAddress = checkForNull(rolodexData.get( "EMAIL_ADDRESS" ));
        String faxNumber = checkForNull(rolodexData.get( "FAX_NUMBER" ));
        String phoneNumber = checkForNull(rolodexData.get( "PHONE_NUMBER" ));
        String comments = checkForNull(rolodexData.get( "COMMENTS" ));
        String rolodexName = null;
        if ( firstName.length() > 0) {
            rolodexName = ( lastName + " "+suffix +", "+ preffix
            +" "+firstName +" "+ middleName ).trim();
        } else {
            rolodexName = checkForNull(rolodexData.get("ORGANIZATION"));
        }
        
        AwardTemplateContactsBean contactDetails = new AwardTemplateContactsBean();
        contactDetails.setRolodexId(Integer.parseInt(rolodexID));
        contactDetails.setFirstName(firstName);
        contactDetails.setLastName(lastName);
        contactDetails.setPrefix(preffix);
        contactDetails.setSuffix(suffix);
        contactDetails.setMiddleName(middleName);
        contactDetails.setTitle(title);
        contactDetails.setSponsorCode(sponsorCode);
        contactDetails.setSponsorName(sponsorName);
        contactDetails.setOrganization(organization);
        contactDetails.setAddress1(address1);
        contactDetails.setAddress2(address2);
        contactDetails.setAddress3(address3);
        contactDetails.setCounty(county);
        contactDetails.setCity(city);
        contactDetails.setState(state);
        contactDetails.setPostalCode(postalCode);
        contactDetails.setCountryCode(countryCode);
        contactDetails.setEmailAddress(emailAddress);
        contactDetails.setPhoneNumber(phoneNumber);
        contactDetails.setFaxNumber(faxNumber);
        contactDetails.setComments(comments);

        cvSelectedPersons.add(contactDetails);
    }
    
    /*Gets the selected persons*/
    public boolean getSelectedPersons(){
        int selRows [] = awardAddRecipientsForm.tblContactList.getSelectedRows();
        for(int index = 0; index< selRows.length ; index++){
            AwardTemplateContactsBean contactDetailsBean = new AwardTemplateContactsBean();
	    contactDetailsBean  = (AwardTemplateContactsBean)cvAwardContactBean.elementAt (selRows[index]);
	    if(contactDetailsBean.getContactTypeCode () == 0){
                CoeusOptionPane.showErrorDialog (coeusMessageResources.parseMessageKey 
                (SELECT_CONTACT_WITH_CONTACT_TYPE));
                cvSelectedPersons.removeAllElements ();
                return false;
            }
            /*Added since Contact Type was not set*/
            //Start
            if(contactDetailsBean.getContactTypeDescription() == null){
                int contactCode = contactDetailsBean.getContactTypeCode();
                CoeusVector cvfilteredVector = cvContactType.filter(new Equals("code", ""+contactCode));
                if(cvfilteredVector != null && cvfilteredVector.size() > 0) {
                    ComboBoxBean comboBoxBean = (ComboBoxBean)cvfilteredVector.get(0);
                    String contactType = comboBoxBean.getDescription().toString();
                    contactDetailsBean.setContactTypeDescription(contactType);
                }
            }//end
            cvSelectedPersons.add(contactDetailsBean);
        }
        return true;
    }
    
    /*Method to dispose the dialog*/
    public void dispose(){
        dlgAwardAddRecipientsForm.dispose();
    }
    /** Method to clean objects */
    public void cleanUp() {
//        awardContactDetailsBean = null;
        awardTemplateContactsBean = null;
        awardHeaderForm = null;
        dlgAwardAddRecipientsForm = null;
        mdiForm = null;
        coeusMessageResources = null;
        awardAddRecipientsForm = null;
        observable = null;
        queryEngine = null;
        contactsSelectionModel = null;
        cvAwardDetailsBean = null;
        cvContactType = null;
        cvAwardContactBean = null;
        cvSelectedPersons = null;
        awardAddRecipientsTableModel = null;
        awardAddRecipientsRenderer = null;
        iconRenderer = null;
        emptyHeaderRenderer = null;
    }
          
    /**
     * Getter for property cvSelectedPersons.
     * @return Value of property cvSelectedPersons.
     */
//    public edu.mit.coeus.utils.CoeusVector getSelectedPersons () {
//        return cvSelectedPersons;
//    }    
    
}//End Class
