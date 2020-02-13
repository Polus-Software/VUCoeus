/*
 * BudgetProposalRateAction.java
 *
 * Created on May 26, 2006, 9:05 AM
 */

package edu.wmc.coeuslite.budget.action;

//import edu.mit.coeus.budget.bean.BudgetDetailBean;
//import edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
//import edu.mit.coeus.budget.bean.BudgetPeriodBean;
//import edu.mit.coeus.budget.bean.CostElementsBean;
//import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
//import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.TypeConstants;

//import edu.mit.coeus.utils.query.And;
//import edu.mit.coeus.utils.query.Equals;
//import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
//import edu.mit.coeuslite.utils.ComboBoxBean;
//import edu.mit.coeuslite.utils.DateUtils;
//import edu.mit.coeuslite.utils.IRBSessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.util.*;
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import org.apache.commons.beanutils.BeanUtilsBean;
//import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
//import org.apache.struts.config.FormBeanConfig;
//import org.apache.struts.config.ModuleConfig;
//import org.apache.struts.action.DynaValidatorFormClass ;
//import org.apache.struts.util.RequestUtils;

import edu.wmc.coeuslite.budget.bean.ApplicableRateList ;

//import org.apache.struts.action.DynaValidatorForm ;

import edu.mit.coeus.utils.CoeusFunctions ;
import edu.mit.coeus.utils.TypeConstants ;
import edu.mit.coeus.bean.UserInfoBean ;
//import edu.mit.coeus.budget.calculator.BudgetCalculator ; 
import edu.mit.coeus.budget.bean.* ;
import edu.mit.coeus.utils.query.* ;
//import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
//import org.apache.struts.action.ActionErrors;


public class BudgetProposalRateAction extends BudgetBaseAction{
//    private ActionForward actionForward = null;
//    private WebTxnBean webTxnBean ;
//    private HttpServletRequest request;
//    private HttpServletResponse response;
//    private ActionMapping actionMapping;
//    private HashMap hmProposalBudgetData = null;
    private static final String EMPTY_STRING = "";
    private static final String GET_LINEITEM_DETAILS = "/getLineItemDetails";
    private static final String GET_BUDGET_DATA = "/budgetSummary";
    private static final String GET_EQUIPMENT_DATA = "/getBudgetEquipment";
    private static final int VERSION_NUMBER  = -1;
//    private ActionMessages actionMessages; 
//    private HttpSession session;
    private static final String BUDGET_MENU_ITEMS ="budgetMenuItemsVector";
    private static final String XML_PATH = "/edu/wmc/coeuslite/budget/xml/BudgetMenu.xml";
    // removing instance variables -case # 2960 - start
//    private DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
    // removing instance variables -case # 2960 - end
    private static final int MAKE_DEFAULT_PERIOD = 1;  
    
    /* prps start */
    
    private static final String GET_PROPOSAL_RATE = "/getBudgetProposalRate";
    
//     private ApplicableRateList applicableRateList ; -- Commented for removing instance variable -CASE 2960
   // private CoeusDynaBeansList applicableRateList ;
    
//    private ProposalBudgetHeaderBean headerBean ; -- Commented for removing instance variable -CASE 2960
    /* prps end */
    //COEUSQA-1689 Role Restrictions for Budget Rates - Start
    private static final String MODIFY_PROPOSAL_RATE = "modifyProposalRates";
    private static final String IN_PROGRESS = "In Progress";
    private static final String REJECTED = "Rejected";
    //COEUSQA-1689 Role Restrictions for Budget Rates - End
    
/**
     * Method to perform action
     * @param actionMapping instance of ActionMapping
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @param response instance of Response
     * @throws Exception if exception occur
     * @return instance of ActionForward
     */    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request,HttpServletResponse response) throws Exception {
//        this.actionMapping  = actionMapping;
//        this.request = request;
//        this.response =response;
//        this.session = request.getSession();
        
       
        ApplicableRateList applicableRateList = (ApplicableRateList) actionForm ;// modified after removing instance variable -CASE 2960
       // test
       // applicableRateList = (CoeusDynaBeansList) actionForm ;
        java.util.ArrayList newList = ( java.util.ArrayList)applicableRateList.getList() ;
        
        //for(int index=0; index < newList.size(); index++) {
            //System.out.println("** Existing data " +  newList.get(index).toString()) ;
        //}
        HttpSession session = request.getSession();
        HashMap hmRequiredDetails = new HashMap();
        hmRequiredDetails.put(ActionMapping.class,actionMapping);
        hmRequiredDetails.put(DynaValidatorForm.class, applicableRateList);
        HashMap hmProposalBudgetData = new HashMap();
        // Required data to be pushed into the HashMap - End
        ActionForward actionForward = getSelectedProposalBudgetData(hmProposalBudgetData,hmRequiredDetails,request,applicableRateList);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.BUDGET_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.BUDGET_PROPOSAL_RATES_CODE);
        setSelectedMenuList(request, mapMenuList);
        return actionForward;
    }    
    

    /**
     * This method will identify which request is comes from which path and
     * navigates to the respective ActionForward
     * @returns ActionForward object
     * @param hmProposalBudgetData
     * @param hmRequiredDetails
     * @throws Exception
     * @return
     */
    private ActionForward getSelectedProposalBudgetData(HashMap hmProposalBudgetData,
        HashMap hmRequiredDetails, HttpServletRequest request,ApplicableRateList applicableRateList)throws Exception{
        String navigator = EMPTY_STRING;
        WebTxnBean webTxnBean = new WebTxnBean();
        ActionForward actionForward = null;
        ActionMapping actionMapping = (ActionMapping)hmRequiredDetails.get(ActionMapping.class);
       /* prps start*/
//       if((actionMapping.getPath().equals(GET_PROPOSAL_RATE)))
//       {
           navigator = getBudgetProposalRate(hmProposalBudgetData,hmRequiredDetails,request,applicableRateList);
           actionForward = actionMapping.findForward(navigator);
//       } /* prps end */
        

        return actionForward;
    }
    
    /* prps start */
    
    
    
    private String getBudgetProposalRate(HashMap hmProposalBudgetData, 
                            HashMap hmRequiredDetails, HttpServletRequest request,ApplicableRateList applicableRateList) throws Exception
    {
        Hashtable htBudgetSummaryData ;
        Vector vecBudgetInfo ;
        String navigator = "success";
        ActionMessages actionMessages = new ActionMessages();
        HttpSession session = request.getSession(); 
        WebTxnBean webTxnBean = new WebTxnBean();
        
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        //DynaValidatorForm dynaForm = (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
              
        String proposalNumber = headerBean.getProposalNumber() ; //"00000120" ;
        int versionNumber = headerBean.getVersionNumber() ;
        //System.out.println(" ** proposal number received in action **" + proposalNumber) ;
        if(proposalNumber == null || proposalNumber.trim().equals(EMPTY_STRING)){
            actionMessages.add( "Proposal Number Required" , new ActionMessage(
            "error.ProposalNumberRequired" ) );
            saveMessages(request, actionMessages);
             navigator = "exception";
             return navigator;
        }
        
        hmProposalBudgetData.put("proposalNumber", proposalNumber);
        hmProposalBudgetData.put("versionNumber", new Integer(versionNumber));
        
        //COEUSQA-1689 Role Restrictions for Budget Rates - Start
        String modeValue = (String)session.getAttribute("mode"+session.getId());        
        //To check whether the value is set to "display" else if set the respective value to the variable
        if(modeValue == null || modeValue.trim().length()==0){
            if(isProposalInHierarchy(request)){
                session.setAttribute("mode"+session.getId(), "display");
            }else{
                session.setAttribute("mode"+session.getId(), "");
                String hasModifyValue = (String)session.getAttribute("hasRights");
                if(!hasModifyValue.equals("true")){
                    session.setAttribute("mode"+session.getId(), "display");
                }
                if(!IN_PROGRESS.equals(headerBean.getProposalStatus())
                        && !REJECTED.equals(headerBean.getProposalStatus())){
                    session.setAttribute("mode"+session.getId(), "display");                    
                }
            }
        }
        boolean hasRight = false;
        //To check whether the user has the rights to modify proposal rates
        hasRight = checkPropsalRatesRight(request,proposalNumber);
        if(!hasRight){
            session.setAttribute(MODIFY_PROPOSAL_RATE,"norights");
        }else{
            session.setAttribute(MODIFY_PROPOSAL_RATE,"");
        }
        //COEUSQA-1689 Role Restrictions for Budget Rates - End
                
        Hashtable htPropData = (Hashtable)webTxnBean.getResults(request,"isValidPropDevNoNBudget",hmProposalBudgetData);
        HashMap validData = (HashMap)htPropData.get("isValidPropDevNoNBudget");
        int valid = Integer.parseInt((String)validData.get("isValid"));
        if(valid == 100){
            actionMessages.add( "InvalidProposal" , new ActionMessage(
            "error.invalidProposalNumber" ) );
            saveMessages(request, actionMessages);
             navigator = "exception";
             return navigator;
        }else if(valid == 200){
            actionMessages.add( "Budget Does not Exist" , new ActionMessage(
            "error.budgetDoesnotExist") );
            saveMessages(request, actionMessages);
            navigator = "exception";
            return navigator;
        }
        
        // check if this action was called with any other parameter
        // sync button calls this action with parameter sync
        
        if (request.getParameter("operation")== null) // first time proposa rates screen is opened operation parameter is null
        {
            // call the transaction id or statement id. If you call the trasaction then
            // automatically the statements listed for that transaction is executed
            // since on this case there is just one statement in the transaction just call statement id 
            // if you have more than one statements then you have to call transaction id
            // Hash table is returned when transaction is executed.
            // The rows of this hash table will vectors (vectors with data retrieved when the
            // statements are executed )
            htBudgetSummaryData = (Hashtable)webTxnBean.getResults(request,"getBudgetProposalRateData",hmProposalBudgetData);
            vecBudgetInfo = (Vector)htBudgetSummaryData.get("getBudgetProposalRateData");
            if(vecBudgetInfo!= null && vecBudgetInfo.size() > 0)
                {    
                    
                    List listPropRate = new ArrayList();
                    for (int i = 0; i<vecBudgetInfo.size(); i++)
                     {  
                         DynaValidatorForm tempDynaForm = (DynaValidatorForm)vecBudgetInfo.get(i) ;
                         //System.out.println (" (Coeus Vector) On_off campus_wmc " + tempDynaForm.get("onOffCampusFlag_wmc") 
                         //                   + " - " + tempDynaForm.get("awOnOffCampusFlag_wmc") ) ; 
                         listPropRate.add(tempDynaForm);
                     } 
                            
                     applicableRateList.setList(listPropRate); 
                     
                        // this copy will reflect any changes made by the user
                        session.setAttribute("applicableRateList", applicableRateList);
                     
                }  
                getBudgetMenus(request);
        }
        else
        {   
             // reset button function - Reset sets all applicable rates back to institute rate
            
            if (request.getParameter("operation").equals("RESET"))
            {  
                //System.out.println(" ** RESET (Dynavalidator)Operation requested **" ) ;
                
                // get the list currently displayed on JSP
                java.util.ArrayList resetList = ( java.util.ArrayList)applicableRateList.getList() ;
                List dataList = new ArrayList () ;
                    for(int index=0; index < resetList.size(); index++)
                    {     
                        DynaValidatorForm dynaFormReset = (DynaValidatorForm)resetList.get(index);
                        //System.out.println("** Reseting data Applicable Rate From " +  dynaFormReset.get("applicableRate").toString() 
                        //                    + " To " +  dynaFormReset.get("instituteRate").toString()) ;
                        
                         dynaFormReset.set("applicableRate_wmc" , dynaFormReset.get("instituteRate_wmc")) ; 
                         dataList.add(dynaFormReset);
                    }
                
                applicableRateList.setList(dataList) ;
                session.setAttribute("applicableRateList", applicableRateList);
                  
                getBudgetMenus(request);
            }
            // sync button function
            if (request.getParameter("operation").equals("SYNC") && isLockPresent(request))
            {
                //System.out.println(" ** SYNC Operation requested **" ) ;
                
                // get proposal  details first
                String EPSUnitNumber = "" ;
                int activityTypeCode = 0 ; 
                
                hmProposalBudgetData.put("proposalNumber", proposalNumber);
                
                Hashtable htEPSProposalData = (Hashtable)webTxnBean.getResults(request,"getEPSProposalDetailsData",hmProposalBudgetData);
                Vector vecEPSProposalData = (Vector)htEPSProposalData.get("getEPSProposalDetailsData");
                if(vecEPSProposalData!= null && vecEPSProposalData.size() > 0)
                {
                     DynaValidatorForm dynaFormEPS = (DynaValidatorForm)vecEPSProposalData.get(0);
                     EPSUnitNumber =  dynaFormEPS.get("unitNumber").toString() ;
                     activityTypeCode = ((Integer)dynaFormEPS.get("activityTypeCode")).intValue() ;
                }
             
                // get top level unit number     
                hmProposalBudgetData.put("unitNumber", EPSUnitNumber);
                
                Hashtable htToplevelUnitData = (Hashtable)webTxnBean.getResults(request,"getTopLevelUnitData",hmProposalBudgetData);
                HashMap hm = (HashMap)htToplevelUnitData.get("getTopLevelUnitData");
                String topLevelUnitNumber = hm.get("UNIT_NUMBER").toString();
               
                // get institutes rates for the activity type and toplevel unit
                hmProposalBudgetData.put("unitNumber", topLevelUnitNumber) ;
                hmProposalBudgetData.put("activityTypeCode", new Integer(activityTypeCode)) ;
            
                Hashtable htInstituteRateData = (Hashtable)webTxnBean.getResults(request,"getInstituteRateData",hmProposalBudgetData);
                Vector vecInstituteRate = (Vector)htInstituteRateData.get("getInstituteRateData");
                
                
                if(vecInstituteRate!= null && vecInstituteRate.size() > 0)
                {
                    // filter data to display rates applicable for the project period
                    Vector vecFilterBudgetInfo = filterBasedOnProjectPeriod(vecInstituteRate,request) ;
                
                    //System.out.println ("*** Institute Rate rows " +  vecInstituteRate.size()
                    //                 + "  After Filtering " + vecFilterBudgetInfo.size() + " ***" ) ;
                    
                    // sync data comes from another table which does not have proposal number and version number
                    // so add that to these dynabeans and send it to JSP
                    List syncList = new ArrayList();
                    if(vecFilterBudgetInfo!= null && vecFilterBudgetInfo.size() > 0)
                    {
                        for (int idx=0 ; idx < vecFilterBudgetInfo.size(); idx++)
                        {
                          DynaValidatorForm syncDynaForm = (DynaValidatorForm)vecFilterBudgetInfo.get(idx);
                          syncDynaForm.set("proposalNumber_wmc", proposalNumber) ;
                          syncDynaForm.set("versionNumber_wmc", new Integer(versionNumber)) ;
                          syncDynaForm.set("awProposalNumber_wmc", proposalNumber) ;
                          syncDynaForm.set("awVersionNumber_wmc", new Integer(versionNumber)) ;
                          syncList.add(syncDynaForm);
                       }
                    }  
                    applicableRateList.setList(syncList); 
                    // this copy will reflect any changes made by the user
                    session.setAttribute("applicableRateList", applicableRateList);
                }
              
                getBudgetMenus(request);
            }
            // save function
            if (request.getParameter("operation").equals("SAVE") && isLockPresent(request))
            {
                //System.out.println(" ** SAVE Operation requested **" ) ;
                
                // get current data from database
            ArrayList oldList = new ArrayList() ;  // Use this list to copy data retrieved from database  (Databse returns Vector i need List for my function) 
            htBudgetSummaryData = (Hashtable)webTxnBean.getResults(request,"getBudgetProposalRateData",hmProposalBudgetData);
            vecBudgetInfo = (Vector)htBudgetSummaryData.get("getBudgetProposalRateData");
                if(vecBudgetInfo!= null && vecBudgetInfo.size() > 0)  
                {
                    for(int i=0; i < vecBudgetInfo.size(); i++)
                    {     
                        DynaValidatorForm dynaFormOld = (DynaValidatorForm)vecBudgetInfo.get(i);
                        //System.out.println("** Data from DB " +  dynaFormOld.get("applicableRate").toString()) ;
                        oldList.add(dynaFormOld) ;
                    }
                }
                
              // get the list which will have the modified data.
                 java.util.ArrayList newList = ( java.util.ArrayList)applicableRateList.getList() ;
                
                    for(int index=0; index < newList.size(); index++)
                    {     
                         DynaValidatorForm dynaFormNew = (DynaValidatorForm)newList.get(index);  
                        //System.out.println("** New data " +  dynaFormNew.get("applicableRate_wmc").toString()) ;
                    }
                
                        //System.out.println("**** Actual Data which goes to database *****") ;
                                
                        ArrayList dbList = prepareDataForSave(oldList, newList) ;
                        
                        // This will save proposal rates to the proposal rates table
                        saveProposalRatesToDatabase(dbList,request);
                       
                        //System.out.println("**** Data saved to database *****") ;
                        
                        // This will fetch the data from these tables and calculate and re-saves data 
                       navigator = calculateAndSaveBudget(request) ;   
                       
                       if (navigator.equals("success"))
                       {
                           navigator = "showSummary" ; // This should take user back to page where they accessed Proposal rates screen
                        }
                       
                       
                        
            }
         }// end else    
        
        return navigator;
    }

     // initializeBudgetCalculator calls getBudgetData they are in BudgetBaseAction
    // calculateAllPeriods is also from BudgetBaseAction
      private String calculateAndSaveBudget(HttpServletRequest request) throws Exception{
          HttpSession session = request.getSession();
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)session.getAttribute("budgetInfoBean");
        //Hashtable htBudgetData = queryEngine.getDataCollection("000001522") ; // queryKey);
        //Added for removing instance variable -case # 2960 - start
        String queryKey = budgetInfoBean.getProposalNumber()+budgetInfoBean.getVersionNumber();
        //Added for removing instance variable - case # 2960- end        
        Hashtable htCalculatedData = null;
        htCalculatedData = calculateAllPeriods(initializeBudgetCalculator(budgetInfoBean), budgetInfoBean);
        String navigator  = saveBudget(htCalculatedData,request,queryKey);
       
        return navigator ;
    }
    
      private String saveBudget(Hashtable htBudgetData, HttpServletRequest request,
            String queryKey) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        edu.mit.coeus.budget.bean.BudgetUpdateTxnBean budgetUpdateTxnBean = new edu.mit.coeus.budget.bean.BudgetUpdateTxnBean(userId);
        boolean isUpdated = budgetUpdateTxnBean.addUpdDeleteBudget(htBudgetData);  
        if(isUpdated){
            navigator = "success";
            removeQueryEngineCollection(queryKey);
        }
        return navigator;
    }
      
      
private boolean compare(DynaValidatorForm firstDynaForm, DynaValidatorForm secondDynaForm ) 
{
   if (
   (firstDynaForm.get("rateClassCode_wmc").toString()).equals(secondDynaForm.get("rateClassCode_wmc").toString())
  && (firstDynaForm.get("rateTypeCode_wmc").toString()).equals(secondDynaForm.get("rateTypeCode_wmc").toString())
  && (firstDynaForm.get("fiscalYear_wmc").toString()).equals(secondDynaForm.get("fiscalYear_wmc").toString())
  && (firstDynaForm.get("onOffCampusFlag_wmc").toString()).equals(secondDynaForm.get("onOffCampusFlag_wmc").toString())
  && (firstDynaForm.get("activityTypeCode_wmc").toString()).equals(secondDynaForm.get("activityTypeCode_wmc").toString())
  && (firstDynaForm.get("startDate_wmc").toString()).equals(secondDynaForm.get("startDate_wmc").toString()))
    return true;
  else
    return false ;
      
}
    
private DynaValidatorForm findInTheList(ArrayList searchList, DynaValidatorForm lookupDynaForm )
{
    for (int idx = 0 ; idx < searchList.size() ; idx++)
    {
        DynaValidatorForm tempDynaForm = (DynaValidatorForm) searchList.get(idx) ;
        if (compare(tempDynaForm, lookupDynaForm))
        {
            return tempDynaForm ;
        }   
    }    
    return null ;
    
}

private ArrayList prepareDataForSave(ArrayList oldList, ArrayList newList)
{
    // compare Each row of New with old list
    // if missing New is missing in the list then it is an insert
    // if there is match then compare applicable rates, if it has changed then it is an Update
    ArrayList dbList = new ArrayList() ;
    
    for (int idx=0 ; idx < newList.size(); idx++)
    {
        DynaValidatorForm newDynaForm = (DynaValidatorForm) newList.get(idx) ;
        DynaValidatorForm matchDynaForm = findInTheList(oldList, newDynaForm) ;
        if (matchDynaForm == null)
        { // new bean does not exist in old list so it is an insert
            newDynaForm.set("acType_wmc", "I") ;
           
            dbList.add(newDynaForm) ;
        }
        else
        { // match found, check if the applicable rate has changed
            if ((matchDynaForm.get("applicableRate_wmc").toString()).equals(newDynaForm.get("applicableRate_wmc").toString()))
            { // nothing has changed ignore this row
                
                
            }
            else
            {   //update new bean to database
                newDynaForm.set("acType_wmc", "U") ;
               
                // Set the update timestamp of new to old
                // There will be a mismatch between old and new timetsamp because they both
                // are from different table. Since i'm updating propsal rates table i need to make 
                // sure i'm updating the correct row. Since old is retrirved from the database (proposal rates table) you can
                // use that timestamp
                newDynaForm.set("awUpdateTimestamp_wmc", matchDynaForm.get("awUpdateTimestamp_wmc")) ;
                dbList.add(newDynaForm) ;
            }    
        }    
    
    }    
    
    //compare each row of old with New
    // if missing then it is a delete
    
    for (int idx=0 ; idx < oldList.size(); idx++)
    {
        DynaValidatorForm oldDynaForm = (DynaValidatorForm) oldList.get(idx) ;
        DynaValidatorForm matchDynaForm = findInTheList(newList, oldDynaForm) ;
        if (matchDynaForm == null)
        { // old bean does not exist in new list then it is delete
                oldDynaForm.set("acType_wmc", "D") ;
                dbList.add(oldDynaForm) ;
        }
    }   
    
    return dbList ;
}


public void saveProposalRatesToDatabase(ArrayList dbList,HttpServletRequest request)throws Exception
{
    CoeusFunctions coeusFunctions = new CoeusFunctions();
    java.sql.Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
    HttpSession session = request.getSession();
    WebTxnBean webTxnBean = new WebTxnBean();
    UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
    String userId = userInfoBean.getUserId().toUpperCase() ;
        
    for (int idx=0 ; idx < dbList.size(); idx++)
    {
        DynaValidatorForm saveDynaForm = (DynaValidatorForm) dbList.get(idx) ;
        //System.out.println ("*** Data saved to the Database ***") ;
        
        saveDynaForm.set("updateTimestamp_wmc", dbTimestamp.toString()) ;  
        saveDynaForm.set("updateUser_wmc", userId) ; 

       /* System.out.println(" ********* Coeus Vector ***********" ) ;
                    System.out.println("proposalNumber_wmc " + saveDynaForm.get("proposalNumber_wmc")) ;
                    System.out.println("versionNumber_wmc " + saveDynaForm.get("versionNumber_wmc")) ;
                    System.out.println("rateClassCode_wmc " + saveDynaForm.get("rateClassCode_wmc")) ; 
                    System.out.println("rateTypeCode_wmc " + saveDynaForm.get("rateTypeCode_wmc")) ; 
                    System.out.println("fiscalYear_wmc " + saveDynaForm.get("fiscalYear_wmc")) ;    
                    System.out.println("onOffCampusFlag_wmc " + saveDynaForm.get("onOffCampusFlag_wmc")) ;   
                    System.out.println("activityTypeCode_wmc " + saveDynaForm.get("activityTypeCode_wmc")) ; 
                    System.out.println("startDate_wmc " + saveDynaForm.get("startDate_wmc")) ;
                    System.out.println("applicableRate_wmc " + saveDynaForm.get("applicableRate_wmc")) ;    
                    System.out.println("instituteRate_wmc " + saveDynaForm.get("instituteRate_wmc")) ;    
                    System.out.println("updateTimestamp_wmc " + saveDynaForm.get("updateTimestamp_wmc")) ; 
                    System.out.println("updateUser_wmc " + saveDynaForm.get("updateUser_wmc")) ;  
                    System.out.println("rateClassDescription_wmc " + saveDynaForm.get("rateClassDescription_wmc")) ; 
                    System.out.println("rateTypeDescription_wmc " + saveDynaForm.get("rateTypeDescription_wmc")) ; 
                    System.out.println("activityTypeDescription_wmc " + saveDynaForm.get("activityTypeDescription_wmc")) ;    
                   
                    System.out.println("awProposalNumber_wmc " + saveDynaForm.get("awProposalNumber_wmc")) ;  
                    System.out.println("awVersionNumber_wmc " + saveDynaForm.get("awVersionNumber_wmc")) ;  
                    System.out.println("awRateClassCode_wmc " + saveDynaForm.get("awRateClassCode_wmc")) ;  
                    System.out.println("awRateTypeCode_wmc " + saveDynaForm.get("awRateTypeCode_wmc")) ;  
                    System.out.println("awFiscalYear_wmc " + saveDynaForm.get("awFiscalYear_wmc")) ;  
                    System.out.println("awOnOffCampusFlag_wmc " + saveDynaForm.get("awOnOffCampusFlag_wmc")) ;  
                    System.out.println("awActivityTypeCode_wmc " + saveDynaForm.get("awActivityTypeCode_wmc")) ;  
                    System.out.println("awStartDate_wmc " + saveDynaForm.get("awStartDate_wmc")) ;
                    System.out.println("awUpdateTimestamp_wmc " + saveDynaForm.get("awUpdateTimestamp_wmc")) ;  
                    System.out.println("acType_wmc " + saveDynaForm.get("acType_wmc")) ;

                    System.out.println("** User id : " + userId) ;
                    System.out.println("** DB Timestamp : " + dbTimestamp) ;*/

                   // add or update or delete Proposal rates depending on acType
                  Hashtable htUpdInvestigator = (Hashtable)webTxnBean.getResults(request, "updateProposalRate", saveDynaForm);
    }  


}



/** To read the Budget Menus from the XML file speciofied for the 
     *Budget
     */
    protected void getBudgetMenus(HttpServletRequest request){
        Vector budgetMenuItemsVector  = null;
        ReadXMLData readXMLData = new ReadXMLData();
        HttpSession session = request.getSession();
        budgetMenuItemsVector = (Vector) session.getAttribute(BUDGET_MENU_ITEMS);
        if (budgetMenuItemsVector == null || budgetMenuItemsVector.size()==0) {
            budgetMenuItemsVector = readXMLData.readXMLDataForMenu(XML_PATH);
            session.setAttribute(BUDGET_MENU_ITEMS, budgetMenuItemsVector);
        }
    }
        
    
    private Vector filterBasedOnProjectPeriod(Vector vecInstituteRate,HttpServletRequest request)
    {
        DynaValidatorForm syncDynaForm_out ;
        DynaValidatorForm syncDynaForm_in ;
        Vector vecReturn = new Vector () ;
        HashMap lessThanList = new HashMap () ;
        
        HttpSession session = request.getSession();
        ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)session.getAttribute("ProposalBudgetHeaderBean");
        
            if(vecInstituteRate!= null && vecInstituteRate.size() > 0)
                {
                   for (int idx_out=0 ; idx_out < vecInstituteRate.size(); idx_out++)
                    {
                        for (int idx_in=0 ; idx_in < vecInstituteRate.size(); )
                        {
                            syncDynaForm_out = (DynaValidatorForm)vecInstituteRate.get(idx_out);
                            syncDynaForm_in = (DynaValidatorForm)vecInstituteRate.get(idx_in);
                            if ( checkForMatch(syncDynaForm_out, syncDynaForm_in ))                                
                            {
                               // if syncDynaForm_out.start_date is between teh proposal start and end date
                               // then add it to the List (say listWithinPeriod)
                               // if syncDynaForm_out.start_date is less than start date then
                               // add it to a listLessThan or hash. Every time you add this check to see if the 
                               // value in hash is greater than teh one you are adding now. You must
                               // keep only the latest start date (latest of all less than startdate entries)
                               // Now check in the listWithin to see if it has an rc-rt-campusflag entries
                               // which has start date same as proposal Start date. If it exists then there is
                               // no need to add include this row in the final set (remove it from listLessThan). 
                               // if syncDynaForm_out.start_date  is greater than proposal end date 
                               // then skip this row.  
                                // finally merge listWithInperiod and listLessThan
                                
                                
                                //the value 0 if the argument is a Date equal to this Date; 
                               // a value less than 0 if the argument is a Date after this Date; 
                                //and a value greater than 0 if the argument is a Date before this Date. 
                                if (((((java.sql.Date)syncDynaForm_in.get("startDate_wmc")).compareTo(headerBean.getProposalStartDate())> 0 )) // >
                                && ((((java.sql.Date)syncDynaForm_in.get("startDate_wmc")).compareTo( headerBean.getProposalEndDate())<= 0)))  // <=
                                {       // You need  not create a HashMap ListWithInPeriod as all these rows should be included, 
                                        // so directly add it to teh return vector
                                        vecReturn.add(syncDynaForm_in) ;
                                }
                                vecInstituteRate.remove(idx_in);
                                
                                // (syncDynaForm_out.get("startDate") < headerBean.getProposalStartDate()
                                if (((java.sql.Date)syncDynaForm_in.get("startDate_wmc")).compareTo(headerBean.getProposalStartDate())<= 0)
                                {
                                    String keyStr = syncDynaForm_in.get("rateClassCode_wmc")+ "-" 
                                                  + syncDynaForm_in.get("rateTypeCode_wmc") + "-" 
                                                  + syncDynaForm_in.get("onOffCampusFlag_wmc")  ;
                                        if (lessThanList.size() == 0 )
                                        {    // if nothing exists add this row
                                            lessThanList.put(keyStr, syncDynaForm_in ) ;
                                        }
                                        else
                                        { // if no entry of the this rc-rt-campusflag exist then add the entry
                                            if (lessThanList.get(keyStr) != null )
                                            {
                                                DynaValidatorForm tempSync = (DynaValidatorForm)lessThanList.get(keyStr);
                                                if (((java.sql.Date)tempSync.get("startDate_wmc")).compareTo((java.sql.Date)syncDynaForm_in.get("startDate_wmc")) < 0)
                                                {
                                                    lessThanList.remove(keyStr) ;
                                                    lessThanList.put(keyStr, syncDynaForm_in ) ;
                                                    
                                                }
                                            }    
                                            else 
                                            {
                                                lessThanList.put(keyStr, syncDynaForm_in ) ;
                                            }   
                                        }    
                                }
                               
                            }else {
                                idx_in = idx_in + 1;
                            }
                                                
                      
                   } // end for In
     
            }// end for Out
                   
          // merge vecReturn with LessThan          
             if (lessThanList.size() > 0)
             {    
//                 Iterator itLessThanList = lessThanList.keySet().iterator() ;
//                 if (itLessThanList != null)
//                 {    
//                    while(itLessThanList.hasNext())
//                    {
//                       vecReturn.add (lessThanList.get(itLessThanList.next())) ;  
//                    } 
//                 }
                   vecReturn.addAll(lessThanList.values());
             }    
                   
         }// end if
        
        return vecReturn ;
        
    }
    
    // This function checks if the rateclass, rate type  and onoffcampus flag are same for both
    private boolean checkForMatch (DynaValidatorForm syncDynaForm_out, 
        DynaValidatorForm syncDynaForm_in )
    {
        if (
            (syncDynaForm_out.get("rateClassCode_wmc").toString().equals(
                                syncDynaForm_in.get("rateClassCode_wmc").toString())
            )
            &&
            (syncDynaForm_out.get("rateTypeCode_wmc").toString().equals(
                                syncDynaForm_in.get("rateTypeCode_wmc").toString())
            )
            &&
            (syncDynaForm_out.get("onOffCampusFlag_wmc").toString().equals(
                                syncDynaForm_in.get("onOffCampusFlag_wmc").toString())
            )
            )                    
           {
           
               return true ;
               
           }
    return false ;
    }
    
    
    
    /*  test start - this test failed */
//    public CoeusVector getFilteredVector(CoeusVector vecRateBean , ProposalBudgetHeaderBean headerBean) {
//        Hashtable rateCode = new Hashtable();
//        CoeusVector completeVector = new CoeusVector();
//        CoeusVector vecEachRateBean;
//        if(vecRateBean != null ) {
//            
//            for(int filterIndex=0; filterIndex< vecRateBean.size();filterIndex ++) {
//                BudgetInfoBean filterDynaValidatorForm = (BudgetInfoBean ) vecRateBean.get(filterIndex) ;
//                
//                int rateClassCode = filterDynaValidatorForm.getOhRateClassCode() ;
//                int rateTypeCode = filterDynaValidatorForm.getOhRateTypeCode() ;
//                
//                if(!rateCode.contains((rateClassCode+"-"+rateTypeCode))) {
//                    
//                    Equals equalRateClassCodeObj  =  new Equals("rateClassCode",new Integer(rateClassCode));
//                    Equals equalRateTypeCodeObj  =  new Equals("rateTypeCode",new Integer(rateTypeCode));
//                    And equalRateClassAndType = new And(equalRateClassCodeObj,equalRateTypeCodeObj);
//                    
//                    vecEachRateBean = vecRateBean.filter(equalRateClassAndType);
//                    CoeusVector vecRateBeanOnThePeriods = getRatesOnThePeriod(vecEachRateBean,headerBean);
//                    completeVector.addAll(vecRateBeanOnThePeriods);
//                    
//                    CoeusVector vecRateBeanWithInPeriod = getRatesWithInPeriod(vecEachRateBean,headerBean);
//                    completeVector.addAll(vecRateBeanWithInPeriod);
//                    
//                    rateCode.put((rateClassCode+"-"+rateTypeCode),(rateClassCode+"-"+rateTypeCode));
//                }
//                
//            }
//            
//        }
//        
//        return completeVector;
//    }
//    
//
//
//
//
//
//public CoeusVector getRatesOnThePeriod(CoeusVector vecRateBean,ProposalBudgetHeaderBean headerBean) {
//        
//        long longTime = headerBean.getProposalStartDate().getTime();
//        Equals equalsStartDate = new Equals("startDate", new java.sql.Date(longTime));
//        LesserThan lesserThanStartDate =  new LesserThan("startDate", new java.sql.Date(longTime));
//        Or equalsOrlesserThanStartDate = new Or(equalsStartDate, lesserThanStartDate);
//        Equals equalsOnCampus = new Equals("onOffCampusFlag", true);
//        Equals equalsOffCampus = new Equals("onOffCampusFlag", false);
//        And equalsOrlesserThanStartDateAndEqualsOnCampus = new And(equalsOrlesserThanStartDate, equalsOnCampus);
//        And equalsOrlesserThanStartDateAndEqualsOffCampus = new And(equalsOrlesserThanStartDate, equalsOffCampus);
//        CoeusVector lessThanOrEqOffCampusVector;
//        CoeusVector lessThanOrEqOnCampusVector;
//        CoeusVector combinedRates = new CoeusVector();
//        
//        lessThanOrEqOffCampusVector = vecRateBean.filter(equalsOrlesserThanStartDateAndEqualsOffCampus);
//        if(lessThanOrEqOffCampusVector != null && lessThanOrEqOffCampusVector.size() > 0) {
//            lessThanOrEqOffCampusVector.sort("startDate",false);
//            combinedRates.add(lessThanOrEqOffCampusVector.get(0));
//        }
//        
//        lessThanOrEqOnCampusVector = vecRateBean.filter(equalsOrlesserThanStartDateAndEqualsOnCampus);
//        if(lessThanOrEqOnCampusVector != null && lessThanOrEqOnCampusVector.size() > 0) {
//            lessThanOrEqOnCampusVector.sort("startDate",false);
//            combinedRates.add(lessThanOrEqOnCampusVector.get(0));
//        }
//        
//        combinedRates.sort("startDate", true);
//        return combinedRates;
//        
//    }
//
//
//
//public CoeusVector getRatesWithInPeriod(CoeusVector vecRateBean, ProposalBudgetHeaderBean headerBean) {
//        
//        long longTime = headerBean.getProposalStartDate().getTime();
//        GreaterThan greaterThanStartDateObj =  new GreaterThan("startDate",new java.sql.Date(longTime));
//        longTime = headerBean.getProposalEndDate().getTime();
//        LesserThan lesserThanEndDateObj =  new LesserThan("startDate",new java.sql.Date(longTime));
//        Equals equalsEndDateObj = new Equals("startDate",new java.sql.Date(longTime));
//        Or lsEqualEndDate = new Or(equalsEndDateObj,lesserThanEndDateObj);
//        And dateGreaterAndEquals = new And(greaterThanStartDateObj,lsEqualEndDate);
//        CoeusVector vecFiltered = vecRateBean.filter(dateGreaterAndEquals);
//        return vecFiltered;
//    }
//    
    
    /* test end */
    
    /**
     * To check the lock is present for the current proposal number.
     * @param request
     * @throws Exception
     * @return boolean
     */    
    private boolean isLockPresent(HttpServletRequest request)throws Exception {
        boolean isPresent = true;
        HttpSession session = request.getSession() ;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.BUDGET_LOCK_STR+lockBean.getModuleNumber(), request);
        if(isLockExists || !lockBean.getSessionId().equals(lockData.getSessionId())) {
            String errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
            isPresent = false;
        }
        return isPresent;
    }    

}
