/*
 * PersonContactInfoForm.java
 *
 * Created on May 3, 2005, 2:38 PM
 */

/* PMD check performed, and commented unused imports and variables on 16-MAY-2007
 * by Leena
 */

package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.propdev.bean.PersonEditableColumnsFormBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.AutoCompleteCoeusCombo;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
//case 3109 start
import java.awt.Color;
//cse 3109 end
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
//import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author  ajaygm
 */
public class PersonContactInfoForm extends javax.swing.JComponent implements ItemListener {
    
    private char functionType;
    private char moduleType;
    private boolean canMaintain;
    private CoeusMessageResources coeusMessageResources;
    private DepartmentPersonFormBean departmentPersonFormBean;
    private static final char DEPARTMENT_PERSON_MODULE_CODE = 'D';
    private static final char PROPOSAL_PERSON_MODULE_CODE = 'P';
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
    //used to denote the form is taken from the Personnel module of the Maintain Menu
    private static final char PERSONNEL_MODULE_CODE = 'N';
    private final String USA = "USA";
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
    private Vector vecEditableColumnNames;
//    private Vector vecStateData;
    private Vector vecCountryData;
    private char STATE_COUNTRY_DATA = 'P';
    private AutoCompleteCoeusCombo cmbState;
    
    private final String PERSON_SERVLET = "/personMaintenanceServlet";
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + PERSON_SERVLET;
    private AppletServletCommunicator comm;
    private boolean saveRequired;
    // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - Start
    private static final char GET_STATE_COUNRTY_DATA_FOR_ALL = 'I';
    private HashMap hmStateCountryInfo;
    // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - End
    /*public PersonContactInfoForm(){
        initComponents();
    }*/
    
    // JM 02-26-2013 need loginUserName
    private boolean canModifyAllFields;
    // JM END
    
    /** Creates new form PersonContactInfoForm */
    // JM 2-27-2013 new instantiating method including canModifyAllFields;
    // logic broken into new instantiate method
    public PersonContactInfoForm(char functionType, char moduleType, boolean maintaintab, DepartmentPersonFormBean personBean, boolean canModifyAllFields) {
    	this.canModifyAllFields = canModifyAllFields;
    	instantiate(functionType, moduleType, maintaintab, personBean);
    }
    
    public PersonContactInfoForm(char functionType, char moduleType, boolean maintaintab, DepartmentPersonFormBean personBean) {
    	instantiate(functionType, moduleType, maintaintab, personBean);
    }
    
    public void instantiate(char functionType, char moduleType, boolean maintaintab, DepartmentPersonFormBean departmentPersonFormBean) {
        this.functionType = functionType;
        this.moduleType = moduleType;
        this.canMaintain = maintaintab;
        this.departmentPersonFormBean = departmentPersonFormBean;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        initCustomComponents();
        //case 3786 start
//        registerComponents();
        setFormData(departmentPersonFormBean);
        registerComponents();
        //case 3786 end
        
    	// JM 02-26-2013 set editable based on permissions
        if ((moduleType == PERSONNEL_MODULE_CODE) || (moduleType == DEPARTMENT_PERSON_MODULE_CODE)) {
	        Component[] fields = {txtAddress1,txtAddress2,txtAddress3,
	                txtCity,txtCounty,cmbState,
	                txtPostalCode,cmbCountry,txtFax,txtPager,
	                txtMobile};
	        for (int f=0; f < fields.length; f++) {
	        	Component currField = fields[f];
	        	currField.setEnabled(canModifyAllFields);
	        }
        }
    }
    // JM END
    
    //     public static void main(String s[]){
    //        JFrame frame = new JFrame("Contact Info");
    //        PersonContactInfoForm personContactInfoForm = new PersonContactInfoForm();
    //        frame.getContentPane().add(personContactInfoForm);
    //        frame.setSize(610, 340);
    //        frame.show();
    //     }
    
//     added for #2697 - start - 27/12/2006
    public void setFocusTraversal(){
        // Case :#3176 - ScreenFocusTraversalPolicy.getComponentAfter(..) does not work with compound components - Start
        // Condition to check for ProposalPersonnel or Personnel
        // If Personnel then State Combo-box is passed as an Traversal Component
        // Else State Combo-Box editor component TextFields is passed as an Traversal Component
        Component stateComp = null;
        if(moduleType == PERSONNEL_MODULE_CODE){
            stateComp = cmbState;
        }else{
            stateComp = cmbState.getEditor().getEditorComponent();
        }
        
        // Modified for Case :#3176
        //Modified to pass Window relavent component for focus
//        Component[] comp = {txtAddress1,txtAddress2,txtAddress3,
//        txtCity,txtCounty,cmbState.getEditor().getEditorComponent(),
//        txtPostalCode,cmbCountry,txtFax,txtPager,
//        txtMobile,txtERACommonsUserName};
//        
        Component[] comp = {txtAddress1,txtAddress2,txtAddress3,
        txtCity,txtCounty,stateComp,
        txtPostalCode,cmbCountry,txtFax,txtPager,
        txtMobile,txtERACommonsUserName};
        // Case :#3176 - End
        
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
    }
//    added for #2697 - end - 27/12/2006
    private void registerComponents(){
//     added for #2697 - start - 27/12/2006
//        Component[] comp = {txtAddress1,txtAddress2,txtAddress3,
//        txtCity,txtCounty,cmbState.getEditor().getEditorComponent(),
//        txtPostalCode,cmbCountry,txtFax,txtPager,
//        txtMobile,txtERACommonsUserName};
//
//        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
//        setFocusTraversalPolicy(traversal);
//        setFocusCycleRoot(true);
//    added for #2697 - end - 27/12/2006
        cmbCountry.addItemListener(this);
        
        
    }
    
    public void setFormData(Object obj){
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //To set the populate the country and state comboboxes
        setDataToCombo();
        if(departmentPersonFormBean == null || functionType == TypeConstants.ADD_MODE){
            return ;
        }
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - end
        txtAddress1.setText(checkForNull(departmentPersonFormBean.getAddress1()));
        txtAddress2.setText(checkForNull(departmentPersonFormBean.getAddress2()));
        txtAddress3.setText(checkForNull(departmentPersonFormBean.getAddress3()));
        txtCity.setText(checkForNull(departmentPersonFormBean.getCity()));
        txtCounty.setText(checkForNull(departmentPersonFormBean.getCounty()));
        txtPostalCode.setText(checkForNull(departmentPersonFormBean.getPostalCode()));
        txtFax.setText(checkForNull(departmentPersonFormBean.getFaxNumber()));
        txtPager.setText(checkForNull(departmentPersonFormBean.getPagerNumber()));
        txtMobile.setText(checkForNull(departmentPersonFormBean.getMobilePhNumber()));
        txtERACommonsUserName.setText(checkForNull(departmentPersonFormBean.getEraCommonsUsrName()));
        //Commented for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //Moved to the top of the function to populate the country state combo box in add mode
        //setDataToCombo();
        //Commented for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        
        //        if(moduleType == DEPARTMENT_PERSON_MODULE_CODE){
        //            populateStateCombo();
        //        }
        
        //        if(departmentPersonFormBean.getState() != null){
        //            String strState = departmentPersonFormBean.getState();
        //            for(int index = 0 ; index < vecStateData.size() ; index++){
        //                ComboBoxBean stateBean = (ComboBoxBean)vecStateData.get(index);
        //                if(strState.equals(stateBean.getDescription())){
        //                    cmbState.setSelectedItem(stateBean);
        //                }//End of inner if
        //            }//End of for
        //        }//end of if
        
        
        if(departmentPersonFormBean.getCountryCode() != null &&
                departmentPersonFormBean.getCountryCode().trim().length()>0){
            String countryCode = departmentPersonFormBean.getCountryCode();
            for(int index = 0 ; index < vecCountryData.size() ; index++){
                ComboBoxBean countryBean = (ComboBoxBean)vecCountryData.get(index);
                if(countryCode.equals(countryBean.getCode())){
                    cmbCountry.setSelectedItem(countryBean);
                }//End of inner if
            }//End of for
                       // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - Start
//            if(countryCode.equalsIgnoreCase("USA")){
//                populateStateCombo();
//                if(departmentPersonFormBean.getState() != null){
//                    String strState = departmentPersonFormBean.getState();
//                    for(int index = 0 ; index < vecStateData.size() ; index++){
//                        ComboBoxBean stateBean = (ComboBoxBean)vecStateData.get(index);
//                        if(strState.equals(stateBean.getCode())){
//                            cmbState.setSelectedItem(stateBean);
//                        }//End of inner if
//                    }//End of for
//                }//end of if
//            }else{
//                if(departmentPersonFormBean.getState()!=null &&
//                        departmentPersonFormBean.getState().trim().length()>0){
//                    ComboBoxBean cmbStateBean = new ComboBoxBean("-1",departmentPersonFormBean.getState());
//                    cmbState.addItem(cmbStateBean);
//                    cmbState.setSelectedIndex(1);
//                }
//            }
            populateStateCombo();
            if(departmentPersonFormBean.getCountryCode() != null){
                boolean selected = false;
                String state = departmentPersonFormBean.getState();
                String country = departmentPersonFormBean.getCountryCode();
                Vector vecStateData = fetchStatesForSelectedCountry(country);
                for(int index = 0 ; index < vecStateData.size() ; index++){
                    ComboBoxBean stateBean = (ComboBoxBean)vecStateData.get(index);
                    if(stateBean.getCode().equalsIgnoreCase(state) || stateBean.getDescription().equalsIgnoreCase(state)){
                        cmbState.setSelectedItem(stateBean);
                        selected = true;
                    }
                }
                if(!selected){
                    if(departmentPersonFormBean.getState() != null && departmentPersonFormBean.getState().trim().length()>0 ){
                        ComboBoxBean cmbStateBean = new ComboBoxBean("-1",departmentPersonFormBean.getState());
                        cmbState.addItem(cmbStateBean);
                        cmbState.setSelectedItem(cmbStateBean);
                    }else if(vecStateData != null && vecStateData.size() >0 ){
                        ComboBoxBean stateBean = (ComboBoxBean)vecStateData.get(0);
                        cmbState.setSelectedItem(stateBean);
                    }else{
                        ComboBoxBean emptyBean = new ComboBoxBean("-1","");
                        cmbState.setSelectedItem(emptyBean);
                    }
                }
            }
            // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - End
            
        }else{
            cmbCountry.removeAllItems();
            ComboBoxBean emptyBean = new ComboBoxBean("","");
            
            vecCountryData.add(0, emptyBean);
            for(int index = 0 ; index < vecCountryData.size(); index++){
                ComboBoxBean cmbCountryBean = (ComboBoxBean)vecCountryData.get(index);
                cmbCountry.addItem(cmbCountryBean);
            }
            
            if(departmentPersonFormBean.getState() != null &&
                    departmentPersonFormBean.getState().trim().length()>0){
                ComboBoxBean statebean = new ComboBoxBean("-1",departmentPersonFormBean.getState());
                cmbState.addItem(statebean);
                cmbState.setSelectedItem(statebean);
            }
        }
        
        
        departmentPersonFormBean.addPropertyChangeListener(
                new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent pce){
                if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                    saveRequired = true;
                }
                if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                    saveRequired = true;
                }
                if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                    if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                        saveRequired = true;
                    }
                }
            }
        });
    }//end of setFormData
    
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString().trim();
    }
    
    public void formatFields(){
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //If module type is PERSONNEL then disable fields in display mode
        if(moduleType == PERSONNEL_MODULE_CODE){
            if(functionType == TypeConstants.DISPLAY_MODE){
                enableFields(false);
            }
        }//Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
        else  if( moduleType == DEPARTMENT_PERSON_MODULE_CODE ){
            enableFields(false);
        }else if(moduleType == PROPOSAL_PERSON_MODULE_CODE){
            enableFields(false);
            //Modified for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
            if(functionType != TypeConstants.DISPLAY_MODE && canMaintain){//COEUSQA-2293 : End
//            if(canMaintain){
                setEnabledComponents();
            }
        }
//    added for #2697 - start - 27/12/2006
        setFocusTraversal();
//    added for #2697 - end - 27/12/2006
    }
    
    private void enableFields(boolean value){
        txtAddress1.setEditable(value);
        txtAddress2.setEditable(value);
        txtAddress3.setEditable(value);
        txtCity.setEditable(value);
        txtCounty.setEditable(value);
        txtERACommonsUserName.setEditable(value);
        txtFax.setEditable(value);
        txtMobile.setEditable(value);
        txtPager.setEditable(value);
        txtPostalCode.setEditable(value);
        cmbState.setEnabled(value);
        //case 3109 start
        if (value == false){
            cmbState.setBackground(new Color(212,208,200));
            cmbState.setEditable(value);
        }
        //case 3109 end 
        cmbCountry.setEnabled(value);
        
        //Added for case 2697: Tabbing Problem in Proposal Person Details - start
        //For the tab traversal it checks whether the component is enabled or not
        txtAddress1.setEnabled(value);
        txtAddress2.setEnabled(value);
        txtAddress3.setEnabled(value);
        txtCity.setEnabled(value);
        txtCounty.setEnabled(value);
        txtERACommonsUserName.setEnabled(value);
        txtFax.setEnabled(value);
        txtMobile.setEnabled(value);
        txtPager.setEnabled(value);
        txtPostalCode.setEnabled(value);
        //Added for case 2697: Tabbing Problem in Proposal Person Details - start
    }
    
    private void setEnabledComponents(){
        if( vecEditableColumnNames != null ){
            int size = vecEditableColumnNames.size();
            
            PersonEditableColumnsFormBean personEditableColumnsFormBean = null;
            
            for( int index = 0 ; index < size ; index++ ){
                personEditableColumnsFormBean = (PersonEditableColumnsFormBean)vecEditableColumnNames.elementAt(index);
                
                String colName = personEditableColumnsFormBean.getColumnName();
                //Modifed for case 2697: Tabbing Problem in Proposal Person Details - start
                //For the tab traversal it checks whether the component is enabled or not
                if(colName != null){
                    if(colName.equalsIgnoreCase("ADDRESS_LINE_1")){
                        txtAddress1.setEditable(true);
                        txtAddress1.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("ADDRESS_LINE_2")){
                        txtAddress2.setEditable(true);
                        txtAddress2.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("ADDRESS_LINE_3")){
                        txtAddress3.setEditable(true);
                        txtAddress3.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("CITY")){
                        txtCity.setEditable(true);
                        txtCity.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("COUNTY")){
                        txtCounty.setEditable(true);
                        txtCounty.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("STATE")){
                        cmbState.setEditable(true);
                        cmbState.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("POSTAL_CODE")){
                        txtPostalCode.setEditable(true);
                        txtPostalCode.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("COUNTRY_CODE")){
                        cmbCountry.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("FAX_NUMBER")){
                        txtFax.setEditable(true);
                        txtFax.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("PAGER_NUMBER")){
                        txtPager.setEditable(true);
                        txtPager.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("MOBILE_PHONE_NUMBER")){
                        txtMobile.setEditable(true);
                        txtMobile.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("ERA_COMMONS_USER_NAME")){
                        txtERACommonsUserName.setEditable(true);
                        txtERACommonsUserName.setEnabled(true);
                    }//End of inner if
                }//End of if(colName != null)
                //Modifed for case 2697: Tabbing Problem in Proposal Person Details - end
            }//End of for
        }else{
            enableFields(false);
        }
    }
    private void setDataToCombo(){
        Vector vecStateAndCountryData = getStateAndCountryData();
        if(vecStateAndCountryData != null){
            // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - Start
//            vecStateData = (Vector)vecStateAndCountryData.get(0);
            hmStateCountryInfo = (HashMap)vecStateAndCountryData.get(0);
            // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - End
            vecCountryData = (Vector)vecStateAndCountryData.get(1);
            for(int index = 0 ; index < vecCountryData.size(); index ++){
                ComboBoxBean cmbCountryBean = (ComboBoxBean)vecCountryData.get(index);
                cmbCountry.addItem(cmbCountryBean);
            }
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
            //To set the default values for the country combo to 'United States'
            ComboBoxBean comboBoxBean = new ComboBoxBean(USA,"United States");
            cmbCountry.setSelectedItem(comboBoxBean);
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
            
           //#Case 3897 -- start added to set Default value for the state combo.
            if(this.functionType == TypeConstants.ADD_MODE) {
                cmbState.removeAllItems();
                populateStateCombo();
            }         
            //#Case 39897 -- end

        }
    }
    
    private void populateStateCombo(){
        // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - Start
//        for(int index = 0 ; index < vecStateData.size(); index ++){
//            ComboBoxBean cmbStateBean = (ComboBoxBean)vecStateData.get(index);
//            cmbState.addItem(cmbStateBean);
//        }
        
        String selectedCountry =  ((ComboBoxBean)cmbCountry.getSelectedItem()).getCode();
        Vector statesForCountry = fetchStatesForSelectedCountry(selectedCountry);
        cmbState.removeAllItems();
        if(statesForCountry != null && statesForCountry.size() >0){
            int stateCount = statesForCountry.size();
            for(int stateIndex=0;stateIndex<stateCount;stateIndex++){
                ComboBoxBean stateData = (ComboBoxBean)statesForCountry.elementAt(stateIndex);
                if(stateData!=null)
                    cmbState.addItem(stateData);
            }
        }
        // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - End
    }
    
    // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - Start
    private Vector fetchStatesForSelectedCountry(String selectedCounty){
        Vector statesForCountry = new Vector();
        if(hmStateCountryInfo != null){
            statesForCountry = (Vector) hmStateCountryInfo.get(selectedCounty);
        }
        return statesForCountry;
    }
    // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - End
    
    private CoeusVector getStateAndCountryData(){
        RequesterBean request = new RequesterBean();
        CoeusVector cvDataFromSrv = new CoeusVector();
        // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - Start
//        request.setFunctionType(STATE_COUNTRY_DATA);
        request.setFunctionType(GET_STATE_COUNRTY_DATA_FOR_ALL);
        // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - End
        comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        try{
            if(response.hasResponse()){
                cvDataFromSrv =(CoeusVector)response.getDataObject();
            }
        }catch (CoeusException ce){
            ce.printStackTrace();
        }
        
        return cvDataFromSrv;
    }
    
    public boolean validateData(){
        // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - Start
//        ComboBoxBean cmbCountryBean = (ComboBoxBean)cmbCountry.getSelectedItem();
//        if(cmbCountryBean.getCode().equalsIgnoreCase("USA")){
//         //#3897 -- start
//         //   String strState = ((JTextField)cmbState.getEditor().getEditorComponent()).getText();
//            String strState =  cmbState.getSelectedItem().toString();
//        //#3897 -- end
//            if(strState != null && strState.trim().equals("")){
//                cmbState.getEditor().getEditorComponent().requestFocusInWindow();
//                CoeusOptionPane.showInfoDialog(
//                        coeusMessageResources.parseMessageKey("roldxMntDetFrm_exceptionCode.1105"));
//                cmbState.getEditor().getEditorComponent().requestFocusInWindow();
//                return false;
//            }
//        }
        boolean validStateCode = false;
        Vector vecStatesForCountry = fetchStatesForSelectedCountry(((ComboBoxBean)cmbCountry.getSelectedItem()).getCode());
        if(vecStatesForCountry != null && vecStatesForCountry.size() > 0){
            String strState =  cmbState.getSelectedItem().toString();
            
            for(int index = 0 ; index < vecStatesForCountry.size() ; index++){
                ComboBoxBean stateBean = (ComboBoxBean)vecStatesForCountry.get(index);
                if(stateBean.getCode().equalsIgnoreCase(strState) || stateBean.getDescription().equalsIgnoreCase(strState)){
                    validStateCode = true;
                }
            }
            
            if(strState == null || strState.trim().equals("") || !validStateCode){
                cmbState.getEditor().getEditorComponent().requestFocusInWindow();
                CoeusOptionPane.showInfoDialog("Invalid state. Please select a valid state");

                cmbState.getEditor().getEditorComponent().requestFocusInWindow();
                return false;
            }
        }
        // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - End
        return true;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblAdderss1 = new javax.swing.JLabel();
        lblAddress2 = new javax.swing.JLabel();
        lblAddress3 = new javax.swing.JLabel();
        txtAddress1 = new edu.mit.coeus.utils.CoeusTextField();
        txtAddress2 = new edu.mit.coeus.utils.CoeusTextField();
        txtAddress3 = new edu.mit.coeus.utils.CoeusTextField();
        lblCity = new javax.swing.JLabel();
        lblCounty = new javax.swing.JLabel();
        lblState = new javax.swing.JLabel();
        lblPostalCode = new javax.swing.JLabel();
        txtCity = new edu.mit.coeus.utils.CoeusTextField();
        txtCounty = new edu.mit.coeus.utils.CoeusTextField();
        cmbCountry = new edu.mit.coeus.utils.CoeusComboBox();
        txtPostalCode = new edu.mit.coeus.utils.CoeusTextField();
        lblCountry = new javax.swing.JLabel();
        lblFax = new javax.swing.JLabel();
        lblPager = new javax.swing.JLabel();
        lblMobile = new javax.swing.JLabel();
        txtFax = new edu.mit.coeus.utils.CoeusTextField();
        txtPager = new edu.mit.coeus.utils.CoeusTextField();
        txtMobile = new edu.mit.coeus.utils.CoeusTextField();
        lblERACommonsUserName = new javax.swing.JLabel();
        txtERACommonsUserName = new edu.mit.coeus.utils.CoeusTextField();
        lblDummy = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        lblAdderss1.setFont(CoeusFontFactory.getLabelFont());
        lblAdderss1.setText("Address 1: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblAdderss1, gridBagConstraints);

        lblAddress2.setFont(CoeusFontFactory.getLabelFont());
        lblAddress2.setText("Address 2: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblAddress2, gridBagConstraints);

        lblAddress3.setFont(CoeusFontFactory.getLabelFont());
        lblAddress3.setText("Address 3: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblAddress3, gridBagConstraints);

        txtAddress1.setDocument(new LimitedPlainDocument(80));
        txtAddress1.setFont(CoeusFontFactory.getNormalFont());
        txtAddress1.setMaximumSize(new java.awt.Dimension(375, 20));
        txtAddress1.setMinimumSize(new java.awt.Dimension(375, 20));
        txtAddress1.setPreferredSize(new java.awt.Dimension(375, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 8);
        add(txtAddress1, gridBagConstraints);

        txtAddress2.setDocument(new LimitedPlainDocument(80));
        txtAddress2.setFont(CoeusFontFactory.getNormalFont());
        txtAddress2.setMaximumSize(new java.awt.Dimension(375, 20));
        txtAddress2.setMinimumSize(new java.awt.Dimension(375, 20));
        txtAddress2.setPreferredSize(new java.awt.Dimension(375, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        add(txtAddress2, gridBagConstraints);

        txtAddress3.setDocument(new LimitedPlainDocument(80));
        txtAddress3.setFont(CoeusFontFactory.getNormalFont());
        txtAddress3.setMaximumSize(new java.awt.Dimension(375, 20));
        txtAddress3.setMinimumSize(new java.awt.Dimension(375, 20));
        txtAddress3.setPreferredSize(new java.awt.Dimension(375, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        add(txtAddress3, gridBagConstraints);

        lblCity.setFont(CoeusFontFactory.getLabelFont());
        lblCity.setText("City: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblCity, gridBagConstraints);

        lblCounty.setFont(CoeusFontFactory.getLabelFont());
        lblCounty.setText("County: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblCounty, gridBagConstraints);

        lblState.setFont(CoeusFontFactory.getLabelFont());
        lblState.setText("State/ Province Name: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblState, gridBagConstraints);

        lblPostalCode.setFont(CoeusFontFactory.getLabelFont());
        lblPostalCode.setText("Postal Code: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblPostalCode, gridBagConstraints);

        txtCity.setDocument(new LimitedPlainDocument(30));
        txtCity.setFont(CoeusFontFactory.getNormalFont());
        txtCity.setMinimumSize(new java.awt.Dimension(150, 20));
        txtCity.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(txtCity, gridBagConstraints);

        txtCounty.setDocument(new LimitedPlainDocument(30));
        txtCounty.setFont(CoeusFontFactory.getNormalFont());
        txtCounty.setMinimumSize(new java.awt.Dimension(150, 20));
        txtCounty.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        add(txtCounty, gridBagConstraints);

        cmbCountry.setMinimumSize(new java.awt.Dimension(150, 19));
        cmbCountry.setPreferredSize(new java.awt.Dimension(150, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(cmbCountry, gridBagConstraints);

        txtPostalCode.setDocument(new LimitedPlainDocument(15));
        txtPostalCode.setFont(CoeusFontFactory.getNormalFont());
        txtPostalCode.setMinimumSize(new java.awt.Dimension(150, 20));
        txtPostalCode.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        add(txtPostalCode, gridBagConstraints);

        lblCountry.setFont(CoeusFontFactory.getLabelFont());
        lblCountry.setText("Country: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblCountry, gridBagConstraints);

        lblFax.setFont(CoeusFontFactory.getLabelFont());
        lblFax.setText("Fax: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblFax, gridBagConstraints);

        lblPager.setFont(CoeusFontFactory.getLabelFont());
        lblPager.setText("Pager: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblPager, gridBagConstraints);

        lblMobile.setFont(CoeusFontFactory.getLabelFont());
        lblMobile.setText("Mobile: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblMobile, gridBagConstraints);

        txtFax.setDocument(new LimitedPlainDocument(20));
        txtFax.setFont(CoeusFontFactory.getNormalFont());
        txtFax.setMinimumSize(new java.awt.Dimension(150, 20));
        txtFax.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        add(txtFax, gridBagConstraints);

        txtPager.setDocument(new LimitedPlainDocument(20));
        txtPager.setFont(CoeusFontFactory.getNormalFont());
        txtPager.setMinimumSize(new java.awt.Dimension(150, 20));
        txtPager.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(txtPager, gridBagConstraints);

        txtMobile.setDocument(new LimitedPlainDocument(20));
        txtMobile.setFont(CoeusFontFactory.getNormalFont());
        txtMobile.setMinimumSize(new java.awt.Dimension(150, 20));
        txtMobile.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        add(txtMobile, gridBagConstraints);

        lblERACommonsUserName.setFont(CoeusFontFactory.getLabelFont());
        lblERACommonsUserName.setText("Agency Credentials: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblERACommonsUserName, gridBagConstraints);

        txtERACommonsUserName.setDocument(new LimitedPlainDocument(20));
        txtERACommonsUserName.setFont(CoeusFontFactory.getNormalFont());
        txtERACommonsUserName.setMinimumSize(new java.awt.Dimension(150, 20));
        txtERACommonsUserName.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(txtERACommonsUserName, gridBagConstraints);

        lblDummy.setMaximumSize(new java.awt.Dimension(100, 20));
        lblDummy.setMinimumSize(new java.awt.Dimension(100, 20));
        lblDummy.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        add(lblDummy, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Getter for property vecEditableColumnNames.
     * @return Value of property vecEditableColumnNames.
     */
    public Vector getVecEditableColumnNames() {
        return vecEditableColumnNames;
    }
    
    /**
     * Setter for property vecEditableColumnNames.
     * @param vecEditableColumnNames New value of property vecEditableColumnNames.
     */
    public void setVecEditableColumnNames(Vector vecEditableColumnNames) {
        this.vecEditableColumnNames = vecEditableColumnNames;
    }
    
    public void getFormData(){
        //Modifies for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //Included the PERSONNEL module also
        if(( canMaintain && moduleType == PROPOSAL_PERSON_MODULE_CODE) ||(
                moduleType == PERSONNEL_MODULE_CODE)){
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
            departmentPersonFormBean.setAddress1( txtAddress1.getText().trim().length() == 0 ? null
                    : txtAddress1.getText().trim());
            departmentPersonFormBean.setAddress2( txtAddress2.getText().trim().length() == 0 ? null
                    : txtAddress2.getText().trim() );
            departmentPersonFormBean.setAddress3( txtAddress3.getText().trim().length() == 0 ? null
                    : txtAddress3.getText().trim() );
            departmentPersonFormBean.setCity( txtCity.getText().trim().length() == 0 ? null
                    : txtCity.getText().trim() );
            departmentPersonFormBean.setCounty( txtCounty.getText().trim().length() == 0 ? null
                    : txtCounty.getText().trim() );
            departmentPersonFormBean.setPostalCode( txtPostalCode.getText().trim().length() == 0 ? null
                    : txtPostalCode.getText().trim() );
            departmentPersonFormBean.setFaxNumber( txtFax.getText().trim().length() == 0 ? null
                    : txtFax.getText().trim() );
            departmentPersonFormBean.setPagerNumber( txtPager.getText().trim().length() == 0 ? null
                    : txtPager.getText().trim() );
            departmentPersonFormBean.setMobilePhNumber( txtMobile.getText().trim().length() == 0 ? null
                    : txtMobile.getText().trim() );
            departmentPersonFormBean.setEraCommonsUsrName( txtERACommonsUserName.getText().trim().length() == 0 ? null
                    : txtERACommonsUserName.getText().trim());
             
            ComboBoxBean cmbCountryBean = (ComboBoxBean)cmbCountry.getSelectedItem();
            departmentPersonFormBean.setCountryCode( cmbCountryBean.getCode().trim().length() == 0 ? null
                    : cmbCountryBean.getCode());
            
            ComboBoxBean cmbStateBean = (ComboBoxBean)cmbState.getSelectedItem();
            // 4467: If you put a country other than US, the state drop down should display the 'states' for the country -  Start
//            if(cmbCountryBean.getCode().equalsIgnoreCase("USA")){
//                if(!cmbStateBean.getCode().equals("-1")){
//                    departmentPersonFormBean.setState(cmbStateBean.getCode());
//                }
//            }else{
//                JTextField txtState = (JTextField)cmbState.getEditor().getEditorComponent();
//                departmentPersonFormBean.setState( txtState.getText().trim().length() == 0 ? null
//                        : txtState.getText().trim() );
//            }
            //Case 4467 - START
            if(cmbStateBean == null || cmbStateBean.getCode().equals("-1") || cmbStateBean.getCode().equals("")){
                JTextField txtState = (JTextField)cmbState.getEditor().getEditorComponent();
                departmentPersonFormBean.setState( txtState.getText().trim().length() == 0 ? null: txtState.getText().trim() );
            } else{
                  departmentPersonFormBean.setState(cmbStateBean.getCode());
            }
            //Case 4467 - END
            // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - End
        }
    }
    
    public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
        if(itemEvent.getStateChange() == itemEvent.DESELECTED){
            return ;
        }
        Object source = itemEvent.getSource();
        
        if(source.equals(cmbCountry)){  
            // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - Start
//            ComboBoxBean comboCountryBean = (ComboBoxBean)cmbCountry.getSelectedItem();
//            if(!comboCountryBean.getCode().trim().equals("USA")){
//                ComboBoxBean emptyBean = new ComboBoxBean("","");
//                cmbState.removeAllItems();
//                cmbState.addItem(emptyBean);
//            }else{
//                cmbState.removeAllItems();
//                populateStateCombo();
//            }
//        }
            populateStateCombo();
        }
        // 4467: If you put a country other than US, the state drop down should display the 'states' for the country - End
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private edu.mit.coeus.utils.CoeusComboBox cmbCountry;
    private javax.swing.JLabel lblAdderss1;
    private javax.swing.JLabel lblAddress2;
    private javax.swing.JLabel lblAddress3;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblCountry;
    private javax.swing.JLabel lblCounty;
    private javax.swing.JLabel lblDummy;
    private javax.swing.JLabel lblERACommonsUserName;
    private javax.swing.JLabel lblFax;
    private javax.swing.JLabel lblMobile;
    private javax.swing.JLabel lblPager;
    private javax.swing.JLabel lblPostalCode;
    private javax.swing.JLabel lblState;
    private edu.mit.coeus.utils.CoeusTextField txtAddress1;
    private edu.mit.coeus.utils.CoeusTextField txtAddress2;
    private edu.mit.coeus.utils.CoeusTextField txtAddress3;
    private edu.mit.coeus.utils.CoeusTextField txtCity;
    private edu.mit.coeus.utils.CoeusTextField txtCounty;
    private edu.mit.coeus.utils.CoeusTextField txtERACommonsUserName;
    private edu.mit.coeus.utils.CoeusTextField txtFax;
    private edu.mit.coeus.utils.CoeusTextField txtMobile;
    private edu.mit.coeus.utils.CoeusTextField txtPager;
    private edu.mit.coeus.utils.CoeusTextField txtPostalCode;
    // End of variables declaration//GEN-END:variables
    
    private void initCustomComponents(){
        DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel();
        cmbState = new AutoCompleteCoeusCombo(defaultComboBoxModel);
        cmbState.setAutoCompleteOnFocusLost(false);
        //To set the cmbState editable
        // # Case 3897 -- start ---  Made the stated combo editabality as false, since user can enter dummy data
        cmbState.setEditable(false);
        //#Case 3897 -- end
        java.awt.GridBagConstraints gridBagConstraints =
                new java.awt.GridBagConstraints();
        cmbState.setMinimumSize(new java.awt.Dimension(150, 19));
        cmbState.setPreferredSize(new java.awt.Dimension(150, 19));
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(cmbState, gridBagConstraints);
        
        final JTextField txtState =(JTextField) cmbState.getEditor().getEditorComponent();
        txtState.addFocusListener( new FocusAdapter(){
            public void focusGained( FocusEvent focusEvt ){
                if( !focusEvt.isTemporary()){
                    txtState.selectAll();
                }
            }
            public void focusLost( FocusEvent focusEvt ){
                if( !focusEvt.isTemporary()){
                    String txtEntered = cmbState.getEditor().getItem().toString();
                    checkForCode(txtEntered);
                }
            }
        });
        
        txtAddress1.setDocument(new LimitedPlainDocument(80));
        txtAddress2.setDocument(new LimitedPlainDocument(80));
        txtAddress3.setDocument(new LimitedPlainDocument(80));
        txtCity.setDocument(new LimitedPlainDocument(30));
        txtCounty.setDocument(new LimitedPlainDocument(30));
        txtPostalCode.setDocument(new LimitedPlainDocument(15));
        txtFax.setDocument(new LimitedPlainDocument(20));
        txtPager.setDocument(new LimitedPlainDocument(20));
        txtMobile.setDocument(new LimitedPlainDocument(20));
        // Increased the size from 12 to 20.
        txtERACommonsUserName.setDocument(new LimitedPlainDocument(20));
        
        txtState.setDocument(new LimitedPlainDocument(30));
    }//End initCustomComponents
    
    private void checkForCode(String txtEntered){
        boolean inside = false;
        // 4467: If you put a country other than US, the state drop down should display the 'states' for the country
        Vector vecStateData = fetchStatesForSelectedCountry(((ComboBoxBean)cmbCountry.getSelectedItem()).getCode());
        if(vecStateData != null && vecStateData.size()>0){
            for(int index = 0; index < vecStateData.size();index++){
                ComboBoxBean stateComboBean = (ComboBoxBean)vecStateData.get(index);
                inside = true;
                if(txtEntered.equalsIgnoreCase(stateComboBean.getCode()) ||
                        txtEntered.equalsIgnoreCase(stateComboBean.getDescription())){
                    //cmbState.getEditor().setItem(stateComboBean.getDescription());
                    cmbState.setSelectedItem(stateComboBean);
                    return ;
                }
            }
        }//End of if
        
        if(inside){
            ComboBoxBean comboBean = new ComboBoxBean("",txtEntered);
            cmbState.setSelectedItem(comboBean);
            
        }
    }
    
    /**
     * Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /**
     * Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
//End checkForCode
}
