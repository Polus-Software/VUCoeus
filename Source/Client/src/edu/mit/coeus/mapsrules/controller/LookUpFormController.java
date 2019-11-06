/*
 * LookUpValueController.java
 *
 * Created on November 7, 2005, 12:31 PM
 */

/* PMD check performed, and commented unused imports and variables on 12-JULY-2010
 * by Satheesh Kumar
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.departmental.bean.DepartmentBudgetFormBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusTableWindow;
import edu.mit.coeus.gui.CostElementsLookupWindow;
import edu.mit.coeus.mapsrules.bean.BusinessRuleFuncArgsBean;
import edu.mit.coeus.mapsrules.controller.RuleController;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.OtherLookupBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.Utils;
import java.util.HashMap;
import java.util.Vector;

/**
 * Empty constructor
 * @author chandrashekara
 */
public class LookUpFormController extends RuleController{
     private CoeusAppletMDIForm mdiForm;
    private BusinessRuleFuncArgsBean businessRuleFuncArgsBean;
    private static final String W_PERSON_SELECT = "W_PERSON_SELECT";
    private static final String W_ARG_CODE_TBL = "W_ARG_CODE_TBL";
    private static final String W_SELECT_COST_ELEMENT = "W_SELECT_COST_ELEMENT";
    private static final String W_ROLODEX_SELECT = "W_ROLODEX_SELECT";
    private static final String W_UNIT_SELECT ="W_UNIT_SELECT";
    private static final String w_arg_value_list = "w_arg_value_list";
    //Added for case 2785 Routing enhancement - start
    private static final String W_ORGANIZATION_SELECT = "W_ORGANIZATION_SELECT";
    private static final String W_SPONSOR_SELECT = "W_SPONSOR_SELECT";
    private static final String W_RESEARCH_AREA_SELECT = "W_RESEARCH_AREA_SELECT";
    //Added for case 2785 Routing enhancement - end
    private static final String COST_ELEMENT_LOOKUP_TITLE = "Cost Elements";
    private static final String SERVLET = "/BudgetMaintenanceServlet";
    // Added for IACUC Business implementation - Start
    private static final String IACUC_MODULE_RESEARCH_AREA_SEARCH_WINDOW = "W_IACUC_RESEARCH_AREA_SELECT";
    // Added for IACUC Business implementation - End
    
    /** Creates a new instance of LookUpValueController */
    public LookUpFormController() {
    }
    
    /**
     * Constructor of lookup form
     * @param mdiForm object of parent window
     * @param businessRuleFuncArgsBean Business Function Argument bean
     */    
     public LookUpFormController(CoeusAppletMDIForm mdiForm, BusinessRuleFuncArgsBean businessRuleFuncArgsBean) {
        this.mdiForm = mdiForm;
        this.businessRuleFuncArgsBean = businessRuleFuncArgsBean;
    }
    
     /**
      * Method to display window
      */     
    public void display() {
    }
    
    /**
     * Method to enabling , disabling fields
     */    
    public void formatFields() {
    }
    
    /**
     * Method to get Controled UI associate with it
     * @return object of component
     */    
    public java.awt.Component getControlledUI() {
        return null;
    }
    
    /**
     * Method to get Form data
     * @return Object
     */    
    public Object getFormData() {
        return businessRuleFuncArgsBean;
    }
    
    /**
     * Method to register component
     */    
    public void registerComponents() {
    }
    
    /**
     * Method to save form data
     * @throws CoeusException If exception occurs
     */    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    /**
     * Method to set form data
     * @param data object
     * @throws CoeusException if Exception occurs
     */    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {

    }
    /**
     * This method will check for the depending on the window name open the corresponding
     * windows  lookup windows, search screens and other related windows
     * @param data object for getLook up window
     * @throws Exception If exception occurs
     */
    public void setLookUpData(Object data) throws Exception{
        /** Bug fix #2013. This is comparing againist "equalsIgnoreCase()" rather
         * "equals()". For the decision controls, it is changed from, equals()
         *to equalIgnoreCase
         */
        if(businessRuleFuncArgsBean.getWindowName().equalsIgnoreCase(W_PERSON_SELECT)){
            CoeusSearch searchWindow = null;
            searchWindow = new CoeusSearch( mdiForm, "PERSONSEARCH",
            CoeusSearch.TWO_TABS ) ;
            searchWindow.showSearchWindow();
            HashMap selectedValues = searchWindow.getSelectedRow();
            if(selectedValues != null){
                String personId = checkForNull(selectedValues.get("PERSON_ID"));
                String fullName = checkForNull(selectedValues.get("FULL_NAME"));
                businessRuleFuncArgsBean.setValue(personId);
                //businessRuleFuncArgsBean.setRuleFuncDescription(fullName);
                businessRuleFuncArgsBean.setRuleExpDescription(fullName);
                if(businessRuleFuncArgsBean.getAcType()
                        != null && businessRuleFuncArgsBean.getAcType()
                                        .equals(TypeConstants.INSERT_RECORD)){
                }else{
                    businessRuleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }else if(businessRuleFuncArgsBean.getWindowName().equalsIgnoreCase(W_SELECT_COST_ELEMENT)){
            costElementLookUp();
        }else if(businessRuleFuncArgsBean.getWindowName().equalsIgnoreCase(W_ROLODEX_SELECT)){
            CoeusSearch searchWindow = null;
            
            searchWindow = new CoeusSearch( mdiForm, CoeusGuiConstants.ROLODEX_SEARCH,
            CoeusSearch.TWO_TABS ) ;
            searchWindow.showSearchWindow();
            
            HashMap selectedValues = searchWindow.getSelectedRow();
            if(selectedValues != null){
                String roleId = checkForNull(selectedValues.get("ROLODEX_ID"));
                String lastName = checkForNull(selectedValues.get("LAST_NAME"));
                businessRuleFuncArgsBean.setValue(roleId);
                //businessRuleFuncArgsBean.setRuleFuncDescription(lastName);
                businessRuleFuncArgsBean.setRuleExpDescription(lastName);
                if(businessRuleFuncArgsBean.getAcType()
                        != null && businessRuleFuncArgsBean.getAcType()
                                        .equals(TypeConstants.INSERT_RECORD)){
                }else{
                    businessRuleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
            
        }else if(businessRuleFuncArgsBean.getWindowName().equalsIgnoreCase(W_UNIT_SELECT)){
            CoeusSearch unitSearch = null;
            
            unitSearch = new CoeusSearch( mdiForm,"LEADUNITSEARCH",
            CoeusSearch.TWO_TABS ) ;
            unitSearch.showSearchWindow();
            
            HashMap selectedValues = unitSearch.getSelectedRow();
            if(selectedValues != null){
                String unitID = checkForNull(selectedValues.get( "UNIT_NUMBER" ));
                String unitName = checkForNull(selectedValues.get( "UNIT_NAME" ));
                businessRuleFuncArgsBean.setValue(unitID);
                //businessRuleFuncArgsBean.setRuleFuncDescription(unitName);
                businessRuleFuncArgsBean.setRuleExpDescription(unitName);
                if(businessRuleFuncArgsBean.getAcType()
                        != null && businessRuleFuncArgsBean.getAcType()
                                        .equals(TypeConstants.INSERT_RECORD)){
                }else{
                    businessRuleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }
        //Added for case 2785 Routing enhancement - start
        else if(businessRuleFuncArgsBean.getWindowName().equalsIgnoreCase(W_ORGANIZATION_SELECT)){
            CoeusSearch coeusSearch = new CoeusSearch(
                        CoeusGuiConstants.getMDIForm(), "ORGANIZATIONSEARCH", 1);
            coeusSearch.showSearchWindow();
            HashMap orgSelected = coeusSearch.getSelectedRow();
            if (orgSelected != null && !orgSelected.isEmpty() ) {
                String orgName = Utils.convertNull(orgSelected.get(
                                                    "ORGANIZATION_NAME"));
                String orgId = Utils.convertNull(orgSelected.get("ORGANIZATION_ID"));
                businessRuleFuncArgsBean.setValue(orgId);
                businessRuleFuncArgsBean.setRuleExpDescription(orgName);
                if(businessRuleFuncArgsBean.getAcType()
                        != null && businessRuleFuncArgsBean.getAcType()
                                        .equals(TypeConstants.INSERT_RECORD)){
                }else{
                    businessRuleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        } else if(businessRuleFuncArgsBean.getWindowName().equalsIgnoreCase(W_SPONSOR_SELECT)){
            CoeusSearch coeusSearch = new CoeusSearch(
                        CoeusGuiConstants.getMDIForm(), "sponsorSearch", 1);
            coeusSearch.showSearchWindow();
            HashMap hmSponsor = coeusSearch.getSelectedRow();
            if(hmSponsor !=null){
                String stSponsorCode=Utils.convertNull(hmSponsor.get("SPONSOR_CODE"));;
                String stSponsorName=Utils.convertNull(hmSponsor.get("SPONSOR_NAME"));
                businessRuleFuncArgsBean.setValue(stSponsorCode);
                businessRuleFuncArgsBean.setRuleExpDescription(stSponsorName);
                if(businessRuleFuncArgsBean.getAcType()
                        != null && businessRuleFuncArgsBean.getAcType()
                                        .equals(TypeConstants.INSERT_RECORD)){
                }else{
                    businessRuleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        } else if(businessRuleFuncArgsBean.getWindowName().equalsIgnoreCase(W_RESEARCH_AREA_SELECT)){
            CoeusSearch coeusSearch = new CoeusSearch(
                        CoeusGuiConstants.getMDIForm(), "AORSEARCH", CoeusSearch.TWO_TABS);
            coeusSearch.showSearchWindow();
            HashMap hmAreaOfResearch = coeusSearch.getSelectedRow();
            if(hmAreaOfResearch !=null){
                String researchAreaCode=Utils.convertNull(hmAreaOfResearch.get("RESEARCH_AREA_CODE"));;
                String descriptionName=Utils.convertNull(hmAreaOfResearch.get("DESCRIPTION"));
                businessRuleFuncArgsBean.setValue(researchAreaCode);
                businessRuleFuncArgsBean.setRuleExpDescription(descriptionName);
                if(businessRuleFuncArgsBean.getAcType()
                        != null && businessRuleFuncArgsBean.getAcType()
                                        .equals(TypeConstants.INSERT_RECORD)){
                }else{
                    businessRuleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }
        // Added for IACUC Business implementation - Start
        else if(businessRuleFuncArgsBean.getWindowName().equalsIgnoreCase(IACUC_MODULE_RESEARCH_AREA_SEARCH_WINDOW)){
            CoeusSearch coeusSearch = new CoeusSearch(
                    CoeusGuiConstants.getMDIForm(), "IACUC_AORSEARCH", CoeusSearch.TWO_TABS);
            coeusSearch.showSearchWindow();
            HashMap hmAreaOfResearch = coeusSearch.getSelectedRow();
            if(hmAreaOfResearch !=null){
                String researchAreaCode=Utils.convertNull(hmAreaOfResearch.get("RESEARCH_AREA_CODE"));;
                String descriptionName=Utils.convertNull(hmAreaOfResearch.get("DESCRIPTION"));
                businessRuleFuncArgsBean.setValue(researchAreaCode);
                businessRuleFuncArgsBean.setRuleExpDescription(descriptionName);
                if(!TypeConstants.INSERT_RECORD.equals(businessRuleFuncArgsBean.getAcType())){
                    businessRuleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }
        // Added for IACUC Business implementation - End
        //Added for case 2785 Routing enhancement - end
        else if(businessRuleFuncArgsBean.getWindowName().equalsIgnoreCase(w_arg_value_list)){
            String windowTitle = "Lookup Values";
            String colNames[] = {"Value","Description"};
            String colValue = EMPTY_STRING;
            if(businessRuleFuncArgsBean.getArgumentName() != null){
                Vector vecLookupdata = getLookupValuesFromDB(businessRuleFuncArgsBean.getArgumentName(),businessRuleFuncArgsBean.getWindowName());
                OtherLookupBean otherLookupBean =
                new OtherLookupBean(windowTitle, vecLookupdata, colNames);
                showLookupSearchWindow(otherLookupBean, businessRuleFuncArgsBean.getWindowName(), vecLookupdata, colValue, 0);
            }
        }else if(businessRuleFuncArgsBean.getWindowName().equalsIgnoreCase(W_ARG_CODE_TBL)){
            if(businessRuleFuncArgsBean.getArgumentName() != null){
                String colNames[] = {"Code","Description"};
                String colValue = EMPTY_STRING;
                String windowTitle = "Lookup Values for - "+businessRuleFuncArgsBean.getArgumentName();//.toUpperCase();
                Vector vecLookupdata = getLookupValuesFromDB(businessRuleFuncArgsBean.getArgumentName(), businessRuleFuncArgsBean.getWindowName());
                OtherLookupBean otherLookupBean =
                new OtherLookupBean(windowTitle, vecLookupdata, colNames);
                showLookupSearchWindow(otherLookupBean, businessRuleFuncArgsBean.getWindowName(), vecLookupdata, colValue, 0);
            }
        }
    }
    /** Get the lookup data for the DW_GET_ARG_VALUE_LIST and DW_GET_ARG_CODE_TBL_NEW
     */
    private Vector getLookupValuesFromDB(String lookUpArgument, String lookUpWindow) throws Exception{
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
            RequesterBean request = new RequesterBean();
            Vector serverLookUpDataObject = null;
            if(lookUpWindow.equalsIgnoreCase(w_arg_value_list)){
                request.setDataObject("DW_GET_ARG_VALUE_LIST");
            }else if(lookUpWindow.equalsIgnoreCase(W_ARG_CODE_TBL)){
                request.setDataObject("DW_GET_ARG_CODE_TBL_NEW");
            }
            request.setId(lookUpArgument);
            AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    serverLookUpDataObject = (Vector)response.getDataObject();
                }else{
                    throw new Exception(response.getMessage());
                }
                
            }
            return serverLookUpDataObject;
        }
    
    /**
     * Helper method to display the Lookup Window when the Lookup button is Pressed
     */
    private void showLookupSearchWindow(OtherLookupBean otherLookupBean,
    String lookUpWindow, Vector vecLookupdata, String columnValue,int selectedRow){
        ComboBoxBean comboBoxBean = null;
        if(otherLookupBean != null){
            CoeusTableWindow coeusTableWindow =
                            new CoeusTableWindow(otherLookupBean);
            int selRow = otherLookupBean.getSelectedInd();
            if(vecLookupdata != null && selRow != -1){
                comboBoxBean = (ComboBoxBean)vecLookupdata.elementAt(selRow);
                if(comboBoxBean != null){
                    String code = (String)comboBoxBean.getCode();
                    String desc = (String)comboBoxBean.getDescription();
                    businessRuleFuncArgsBean.setValue(code);
                   // businessRuleFuncArgsBean.setRuleFuncDescription(desc);
                    businessRuleFuncArgsBean.setRuleExpDescription(desc);
                    if(businessRuleFuncArgsBean.getAcType()
                        != null && businessRuleFuncArgsBean.getAcType()
                                        .equals(TypeConstants.INSERT_RECORD)){
                     }else{
                        businessRuleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }
        }
    }
    
    /** Get the CVost element look up data when the action is performed
     */
    private void costElementLookUp() throws CoeusClientException{
        CostElementsLookupWindow costElementsLookupWindow = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType('I');
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(! response.isSuccessfulResponse()) {
           throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
        
        Vector vecCostElements = response.getDataObjects();
        DepartmentBudgetFormBean departmentBudgetFormBean;
        ComboBoxBean comboBoxBean;
        Vector vecComboBoxBean = new Vector();
        
        for(int index = 0; index < vecCostElements.size(); index++) {
            departmentBudgetFormBean = (DepartmentBudgetFormBean)vecCostElements.get(index);
            comboBoxBean = new ComboBoxBean(departmentBudgetFormBean.getCostElement(), departmentBudgetFormBean.getDescription());
            vecComboBoxBean.add(comboBoxBean);
        }
        
        String colNames[] = {"Code","Description"};
        OtherLookupBean otherLookupBean = new OtherLookupBean(COST_ELEMENT_LOOKUP_TITLE, vecComboBoxBean, colNames);
        costElementsLookupWindow = new CostElementsLookupWindow(otherLookupBean);
        
        //Check button click - OK or Cancel
        if(otherLookupBean.getSelectedInd() == -1) return ;
        
        //Get Selected Row for Cost Elements
        int selectedRow = costElementsLookupWindow.getDisplayTable().getSelectedRow();
        if(selectedRow == -1) return ;
        
        departmentBudgetFormBean = (DepartmentBudgetFormBean)vecCostElements.get(selectedRow);
        businessRuleFuncArgsBean.setValue(departmentBudgetFormBean.getCostElement());
       // businessRuleFuncArgsBean.setRuleFuncDescription(departmentBudgetFormBean.getDescription());
        businessRuleFuncArgsBean.setRuleExpDescription(departmentBudgetFormBean.getDescription());
        if(businessRuleFuncArgsBean.getAcType()
                    != null && businessRuleFuncArgsBean.getAcType()
                                .equals(TypeConstants.INSERT_RECORD)){
        }else{
            businessRuleFuncArgsBean.setAcType(TypeConstants.UPDATE_RECORD);
        }
    }
    
    /**
     * Method to validate form data
     * @throws CoeusUIException If exception occurs
     * @return boolean true or false based on condition
     */    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    /**Method for checking null */
     private String checkForNull( Object value ){
        return (value == null) ? EMPTY_STRING : value.toString();
     }
    
}