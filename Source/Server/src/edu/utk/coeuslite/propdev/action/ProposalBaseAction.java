/*
 * ProposalBaseAction.java
 *
 * Created on April 17, 2006, 12:22 PM
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleConditionsBean;
import edu.mit.coeus.mapsrules.bean.RulesTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalHierarchyTxnBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;


/**
 *
 * @author  vinayks
 */
public abstract class ProposalBaseAction extends CoeusBaseAction {
    
     /**
     * Proposal Status code values
     */
    public static final int PROPOSAL_IN_PROGRESS = 1;
    public static final int PROPOSAL_APPROVAL_IN_PROGRESS = 2;
    public static final int PROPOSAL_REJECTED = 3;
    public static final int PROPOSAL_APPROVED = 4;
    public static final int PROPOSAL_SUBMITTED = 5;
    public static final int PROPOSAL_POST_SUB_APPROVAL = 6;
    public static final int PROPOSAL_POST_SUB_REJECTION = 7;
    public static final String EMPTY_STRING = "";
    public static final String USER = "user";
    protected static final String fileName = "ProposalMessages";
    private static final String PROPOSAL_MENU_ITEMS ="proposalApprovalMenuItemsVector";  
    //Code commented for Case#2785 - Protocol Routing
//    public static final String VIEW_ROUTING = "AV_VIEWROUTING";
//    public static final String PROPWAIT = "AV_PROPWAIT";    
    private static final String PROPOSAL_SUB_HEADER="proposalSubHeader";
    private static final String XML_MENU_PATH = "/edu/utk/coeuslite/propdev/xml/ProposalMenu.xml";
    private static final String XML_SUB_MENU_PATH="/edu/utk/coeuslite/propdev/xml/ProposalSubMenu.xml";
    //Added for the case # COEUSQA-1675- Ability to delete proposal development proposal - Start
    private final String DELETE_ANY_PROPOSAL = "DELETE_ANY_PROPOSAL";
    private final String DELETE_PROPOSAL = "DELETE_PROPOSAL";
    //Added for the case # COEUSQA-1675- Ability to delete proposal development proposal - End
    /** Creates a new instance of ProposalBaseAction */
    public ProposalBaseAction() {
    }
    
  public abstract ActionForward performExecute(ActionMapping actionMapping,
    ActionForm actionForm, HttpServletRequest request,
    HttpServletResponse response) throws Exception; 
  
  
  //to prepare the update timestamp.
     public Timestamp prepareTimeStamp() throws Exception{
        Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        return dbTimestamp;
    }
     
     /**
     *This method returns sponsor name for a particular sponsor code
     */ 
   public String getSponsorDetails(HttpServletRequest request, 
        String sponsorCode)throws Exception{  
            
        HashMap hmSponsorData = new HashMap();
        hmSponsorData.put("sponsorCode",sponsorCode);
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm dynaSponsorForm = null ;
        String sponsorName = EMPTY_STRING;
        try {
            Hashtable htSponsorData =
                (Hashtable)webTxnBean.getResults(request, "getSponsor", hmSponsorData);
            Vector vecSponsorData = (Vector)htSponsorData.get("getSponsor");
            if(vecSponsorData!=null && vecSponsorData.size() > 0){
               dynaSponsorForm = (DynaValidatorForm)vecSponsorData.get(0);
               sponsorName = (String)dynaSponsorForm.get("sponsorName");
            }
        }catch(Exception dbEx){
              UtilFactory.log("FROM COEUS LITE : "+dbEx.getMessage(),dbEx,"ProposalBaseAction","getSponsorDetails");
        }
        
        return sponsorName;
    }
   
     public String getUserName(HttpServletRequest request ,
        String userId) throws Exception{     
            
        UserInfoBean userInfoBean = null;
        PersonInfoBean personInfoBean = null;
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        userInfoBean  = userDetailsBean.getUserInfo(userId.trim());
        String userName = EMPTY_STRING ;
        if (userInfoBean != null && userInfoBean.getUserId()!=null){            
            personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getPersonId(),false);
            if(personInfoBean!=null){
                userName = personInfoBean.getFullName();
            }
        }
        return userName;
    }
  //-----------------------------------------------------------------------------------
     //MURALI
  //-----------------------------------------------------------------------------------
     
        /**
    * To check the Validation for proposal
    * Validation is done for all the sub modules of Proposal as well.
    * @throws Exception
    * @return Vector. Conatins the objects from DynaValidatorForm
    */
    public Vector checkValidationProposal(String proposalNumber, int budgetVersion,
                                          DynaValidatorForm dynaValidatorForm, HttpSession session) throws Exception{
        UserInfoBean userInfoBean   = (UserInfoBean)session.getAttribute(USER);
        String userId               = userInfoBean.getUserId();
//        String unitNumber           = userInfoBean.getUnitNumber();
        EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
        
//        ProposalActionTxnBean proposalTxnBean = new ProposalActionTxnBean();
//        return getDynaData(proposalTxnBean.proposalValidation(proposalNumber,headerBean.getLeadUnitNumber(),userId), dynaValidatorForm);
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        //Modified with case 2158 : Budget Validations
               
//        return getDynaData(routingTxnBean.validateForRouting("3", proposalNumber, "0", "1", headerBean.getLeadUnitNumber(), userId), dynaValidatorForm);
        return getDynaData(routingTxnBean.validateForRouting(ModuleConstants.PROPOSAL_DEV_MODULE_CODE,0, proposalNumber,budgetVersion, 1, headerBean.getLeadUnitNumber(), userId), dynaValidatorForm);
        //2158:End
    }
    
      /**
    * To perform validations at budget level
    * @throws Exception
    * @return Vector. Conatins the objects from DynaValidatorForm
    */
    public Vector checkValidationBudget(String proposalNumber,int budgetVersion,
                                          DynaValidatorForm dynaValidatorForm, HttpServletRequest request) throws Exception{
        HttpSession session         = request.getSession();
        UserInfoBean userInfoBean   = (UserInfoBean)session.getAttribute(USER);
        String userId               = userInfoBean.getUserId();
        //COEUSDEV-159 Lite - cost share warning -won't allow complete status in lite
        getProposalHeader(request);
        //COEUSDEV-159 End
        EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        return getDynaData(routingTxnBean.validateForRouting(ModuleConstants.PROPOSAL_DEV_MODULE_CODE,ModuleConstants.PROPOSAL_DEV_BUDGET_SUB_MODULE,
                                proposalNumber, budgetVersion, 1, headerBean.getLeadUnitNumber(), userId), dynaValidatorForm);
    }
    
     /**
    * To find the budget version to validate
    * @throws Exception
    * @return 1000 if there is no budget to validate
    *         1001 if no budget version is marked final
    *         budgetCount - The version to be validated
    */
    public int getBudgetVersionToValidate(String proposalNumber) throws Exception{
        
        ProposalDevelopmentTxnBean txnBean = new ProposalDevelopmentTxnBean();
        int budgetCount = txnBean.getProposalHasBudget(proposalNumber);
        if(budgetCount == 0){
            return 1000;
        }else{
            BudgetSubAwardTxnBean subAward = new BudgetSubAwardTxnBean();
            budgetCount = subAward.getBudgetFinalVersion(proposalNumber);
            if(budgetCount == 0){
                return 1001;
            }else{
                return budgetCount;
            }
        }
    }
    //2158 End
    /**
    * To process the vector obtained from calling the PLSQL function with the DynaValidatorForm
    * @throws Exception
    * @return Vector. Conatins the objects from DynaValidatorForm
    */
    private Vector getDynaData(Vector vecValidationProposal, DynaValidatorForm  dynaValidatorForm) throws Exception{
        Vector dynaVector = new Vector();
        Vector vecErrors = new Vector();
        if(vecValidationProposal!= null && vecValidationProposal.size() > 0){
            for(int index = 0; index < vecValidationProposal.size(); index++){
                BusinessRuleBean businessRulesBean = (BusinessRuleBean)vecValidationProposal.get(index);
                Vector vecRulesConditions = businessRulesBean.getBusinessRuleConditions();
                if(vecRulesConditions != null && vecRulesConditions.size() > 0){
                    for(int count = 0; count < vecRulesConditions.size(); count++){
                        BeanUtilsBean copyBean = new BeanUtilsBean();
                        DynaBean dynaData = (DynaBean)dynaValidatorForm.getDynaClass().newInstance();
                        copyBean.copyProperties(dynaData,businessRulesBean);
                        BusinessRuleConditionsBean conditionBean = 
                                (BusinessRuleConditionsBean)vecRulesConditions.get(count);
                        dynaData.set("description", conditionBean.getUserMessage());
                        if(businessRulesBean.getRuleCategory().equals("E")){
                            vecErrors.addElement(dynaData);
                        } else {
                            dynaVector.addElement(dynaData);
                        }
                    }
                } else {
                    BeanUtilsBean copyBean = new BeanUtilsBean();
                    DynaBean dynaData = (DynaBean)dynaValidatorForm.getDynaClass().newInstance();
                    copyBean.copyProperties(dynaData,businessRulesBean);
                    if(businessRulesBean.getRuleCategory().equals("E")){
                        vecErrors.addElement(dynaData);
                    } else {
                        dynaVector.addElement(dynaData);
                    }
                }
            }
        }
        vecErrors.addAll(dynaVector);
        return  vecErrors;
     }

    /**
    * To get all the keys and values from ProposalMessages.Properties file so that
     * it can be used by other action classes wherever necessary
    * @throws Exception
    * @return Object conatining all the keys and values from ProposalMessages.Properties file
    * */
    public Object getObjectsFromBundle() throws Exception {
        java.util.Properties  properties = null ;
        java.util.PropertyResourceBundle prb =(java.util.PropertyResourceBundle)
                                              java.util.ResourceBundle.getBundle(new String(fileName));
        if (null != prb) {
            properties          = new java.util.Properties();
            Enumeration keys    = prb.getKeys();
            while (keys.hasMoreElements()) {
                String key = (String) (keys.nextElement());
                properties.setProperty(key, (String) prb.handleGetObject(key));
            }
        }
        return properties;
    }
    public String getValuesFromUserBean(HttpSession session, String argument) throws Exception{
        EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
        UserInfoBean userInfoBean   = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId               = userInfoBean.getUserId();
        String unitNumber           = headerBean.getUnitNumber();
        if (argument.equalsIgnoreCase(USER)){
            return userId;
        }
        else{
            return unitNumber;
        }

    }
    
    //Code commented for Case#2785 - Protocol Routing - starts
    /**
     * Method to get the approve proposal rights 
     * @throws Exception
     * @return HashMap
     */    
//    protected HashMap getPropApprovalRights(HttpServletRequest request)throws Exception{
//        HttpSession session = request.getSession();
//        WebTxnBean webTxnBean = new WebTxnBean();
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        String userId = userInfoBean.getUserId();
//        HashMap hmPropApprovalRights = new HashMap();
//        hmPropApprovalRights.put("routingNumber",session.getAttribute("routingNumber"+session.getId()));
//        hmPropApprovalRights.put("moduleItemKey",(String)session.getAttribute("proposalNumber"+session.getId()));
//        hmPropApprovalRights.put("module_itemSequence", "0");
//        hmPropApprovalRights.put("moduleCode", "3");
//        hmPropApprovalRights.put("userId",userId);
//        Hashtable htPropApprovalRights =
//        (Hashtable)webTxnBean.getResults(request, "getProposalApprovalRights" , hmPropApprovalRights);
//        HashMap hmApprovalRights = (HashMap)htPropApprovalRights.get("getProposalApprovalRights");
//        return hmApprovalRights;
//    }
    
    /** Manufacture the LockBean based on the parameter passed by the specific module
     *say, Propsoal, Protocol, Budget etc.
     *@param UserInfoBean, Proposal number
     *@returns LockBean
     *@throws Exception
     */
    protected LockBean getLockingBean(UserInfoBean userInfoBean, String proposalNumber, HttpServletRequest request) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.PROP_DEV_LOCK_STR+proposalNumber);
        String mode = (String)request.getSession().getAttribute("mode"+request.getSession().getId());
        mode = getMode(mode);
        lockBean.setMode(mode);
        lockBean.setModuleKey(CoeusLiteConstants.PROPOSAL_MODULE);
        lockBean.setModuleNumber(proposalNumber);
        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
        lockBean.setUnitNumber(UNIT_NUMBER);
        lockBean.setUserId(userInfoBean.getUserId());
        lockBean.setUserName(userInfoBean.getUserName());
        lockBean.setSessionId(request.getSession().getId());
        return lockBean;
    }
    
    protected String getMode(String mode) throws Exception{
        if(mode!= null && !mode.equals(EMPTY_STRING)){
            if(mode.equalsIgnoreCase("display")){
                mode = CoeusLiteConstants.DISPLAY_MODE;
            }
        }else{
            mode = CoeusLiteConstants.MODIFY_MODE;
        }
        
        return mode;
    }
    
    protected void enableDisableMenus(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        //Code added for Case#2785 - Protocol Routing - starts
        String proposalNumber = (String)request.getSession().getAttribute(
                "proposalNumber"+request.getSession().getId());
        HashMap hmMap = getApprovalRights(request, "3", proposalNumber, "0");
        String avViewRouting =(String) hmMap.get(VIEW_ROUTING);
        //Code added for Case#2785 - Protocol Routing - ends
        Vector menuData =(Vector) session.getAttribute("proposalMenuItemsVector");
        EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
        String statusCode = null;
        boolean isVisible = false;
        //Added for the case # COEUSQA-1675- Ability to delete proposal development proposal - Start
        boolean isAuthrisedToDeleteProp = false;
        int  canDelete = -1;
        String propInHierarchy = EMPTY_STRING;
        //Added for the case # COEUSQA-1675- Ability to delete proposal development proposal - end
        if(headerBean!= null){
            statusCode = headerBean.getProposalStatusCode();
            //Added for the case # COEUSQA-1675- Ability to delete proposal development proposal - Start
            ProposalDevelopmentTxnBean proposalDataTxnBean = new ProposalDevelopmentTxnBean();
            isAuthrisedToDeleteProp =  getDeleteRights(proposalNumber, request, headerBean);
            canDelete = proposalDataTxnBean.checkCanDeleteProposal(proposalNumber);
            propInHierarchy = headerBean.getIsHierarchy();
            //Added for the case # COEUSQA-1675- Ability to delete proposal development proposal - End
//            if(statusCode!= null){
//                if(statusCode.equals("1") || statusCode.equals("3")){
//                    isVisible = true;
//                }
//            }
        }
        
        if(menuData!= null && menuData.size() > 0){
            for(int index = 0; index < menuData.size(); index++){
                MenuBean menuBean = (MenuBean)menuData.get(index);
                if(menuBean.getMenuId()!=null && menuBean.getMenuId().equals("P015") ) {
                    if(statusCode!=null){
                        //COEUSQA-1433 Allow Recall for Routing - Start
                        //if(statusCode.equals("1") || statusCode.equals("3")){
                        if(statusCode.equals("1") || statusCode.equals("8")){
                        //COEUSQA-1433 Allow Recall for Routing - End
                            menuBean.setVisible(false);
                        }else{
                            if(avViewRouting!=null && !avViewRouting.equals(EMPTY_STRING)){
                                if(avViewRouting.equals("0")){
                                    if(statusCode.equals("3"))
                                    {
                                     menuBean.setVisible(true);
                                    }
                                    else{
                                    menuBean.setVisible(false);
                                    }
                                    
                                }
                                else{
                                    menuBean.setVisible(true);
                                }
                            }
                        }
                    }else{
                         menuBean.setVisible(false);
                    }
                }
                //Added for the case # COEUSQA-1675- Ability to delete proposal development proposal - Start/
                // Checking whether the status of propsal is Pending in progress, have right to deltele and can delte the proposal, then menu 
                // is set to visible
                else if(menuBean.getMenuId()!=null && menuBean.getMenuId().equals("P026") ) {
                    if( statusCode!=null && statusCode.equals("1") && isAuthrisedToDeleteProp &&  canDelete == 0 && !propInHierarchy.equals("Y")){
                        menuBean.setVisible(true);
                    }else{
                        menuBean.setVisible(false);
                    }
                    
                }
                
                //Added for the case # COEUSQA-1675- Ability to delete proposal development proposal - End
            }
        }    
        session.setAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS, menuData);
        
    }
    
     /** Read the save status for the given proposal number
     *@throws Exception
     */
    protected void readSavedStatus(HttpServletRequest request) throws Exception{
        Map hmSavedData =null;
        Hashtable htReqData =null;
        HashMap hmMenuData = null;
       //COEUSQA-4090 :Added to refresh  menu 
        refreshQuestionnaireMenus(request);
       //COEUSQA-4090 :Added to refresh  menu 
        String proposalNumber = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
        Vector menuData= (Vector)request.getSession().getAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        
           WebTxnBean webTxnBean = new WebTxnBean();

           try {
                if(menuData!= null && menuData.size() > 0){
                    hmSavedData = new HashMap();
                    htReqData = new Hashtable();
                    hmMenuData =new HashMap();
                    MenuBean dataBean = null;
                    String menuId = EMPTY_STRING;
                    String strValue = EMPTY_STRING;
                    boolean isDynamic = false;
                    HashMap hmReturnData= null;
                    for(int index = 0; index < menuData.size(); index++){
                        dataBean = (MenuBean)menuData.get(index);
                        /**Checkk for the dynamically created menu's. For example
                         *Questionnaire Menu. the dynamicId specifies the dynamic menu ids
                         *generated. At present it gets the dynamic Id for the questionnaire
                         *Menu and makes server call to show the saved questionnaire menu
                         */
                        if(dataBean.getDynamicId()!= null && !dataBean.getDynamicId().equals(EMPTY_STRING)){
                            menuId =dataBean.getDynamicId();
                            isDynamic = true;
                        }else{
                            menuId = dataBean.getMenuId();
                            isDynamic = false;
                        }
                        hmMenuData.put("proposalNumber", proposalNumber);
                        hmMenuData.put("menuId", menuId);
                        if(isDynamic){
                            // Added with CoeusQA2313: Completion of Questionnaire for Submission
                            HashMap hmQuestionnaire = new HashMap();
                            QuestionnaireAnswerHeaderBean headerBean = (QuestionnaireAnswerHeaderBean)dataBean.getUserObject();
                            hmQuestionnaire.put("moduleCode",headerBean.getModuleItemCode());
                            hmQuestionnaire.put("subModuleCode",new Integer(headerBean.getApplicableSubmoduleCode()));
                            hmQuestionnaire.put("menuId", menuId);
                            hmQuestionnaire.put("moduleItemKey",proposalNumber);
                            hmQuestionnaire.put("moduleItemKeySequence",0);
                            // CoeusQA2313: Completion of Questionnaire for Submission - End
                            htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedQuestionnaireData", hmQuestionnaire);
                            hmReturnData = (HashMap)htReqData.get("getSavedQuestionnaireData");
                        }else{
                            htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedProposalMenuData", hmMenuData);
                            hmReturnData = (HashMap)htReqData.get("getSavedProposalMenuData");
                        }
                        if(hmReturnData!=null) {
                            strValue = (String)hmReturnData.get("AV_SAVED_DATA");
                            int value = Integer.parseInt(strValue);
                            if(value == 1){
                                dataBean.setDataSaved(true);
                            }else if(value == 0){
                                dataBean.setDataSaved(false);
                            }
                        }

                    }
                    request.getSession().removeAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
                    request.getSession().setAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS, menuData);
                }
           }catch(Exception dbEx){
              UtilFactory.log("FROM COEUS LITE : "+dbEx.getMessage(),dbEx,"ProposalBaseAction","readSavedStatus");
        }
    }
    
     /** To read the proposal Menus from the XML file speciofied for the 
     *Proposal
     */
    protected void getProposalMenus(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector proposalMenuItemsVector  = null;
        ReadXMLData readXMLData = new ReadXMLData();        
        proposalMenuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        HashMap hmData = null ;
        //Code commented for Questionnaire enhancement case#2946
        //To read the Menu items every time it is loading.
        //if (proposalMenuItemsVector == null || proposalMenuItemsVector.size()==0) {
            hmData = new HashMap();
            proposalMenuItemsVector = readXMLData.readXMLDataForMenu(XML_MENU_PATH);
            hmData.put(ModuleDataBean.class , new Integer(3));
            hmData.put(SubModuleDataBean.class , new Integer(0));
            hmData.put("link" , "/getQuestionnaire.do");
            hmData.put("actionFrom" ,"DEV_PROPOSAL");
            //Modified for Case#3941 -Questionnaire answers missing after deleting the module - Start
            String moduleItemKey = (String)session.getAttribute("proposalNumber"+session.getId());
            moduleItemKey = moduleItemKey == null ? EMPTY_STRING : moduleItemKey;
            hmData.put("moduleItemKey",moduleItemKey);
            hmData.put("moduleSubItemKey","0");//case 2158
            //Modified for Case#3941 -Questionnaire answers missing after deleting the module - End
            proposalMenuItemsVector = getQuestionnaireMenuData(proposalMenuItemsVector,request ,hmData);            
            session.setAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS, proposalMenuItemsVector);
        //}
        Vector vecProposalSubMenuHeader = null;
        vecProposalSubMenuHeader = (Vector)session.getAttribute(PROPOSAL_SUB_HEADER);
        if(vecProposalSubMenuHeader == null || vecProposalSubMenuHeader.size()==0){
            vecProposalSubMenuHeader = readXMLData.readXMLDataForSubHeader(XML_SUB_MENU_PATH);
            session.setAttribute(PROPOSAL_SUB_HEADER,vecProposalSubMenuHeader);
        }
    }
    /** This method will be invoked when the proposal is trying to open in 
     *any given mode except new mode. This method will say whether the given
     *proposal number is in hierarchy or not and also, whether the proposal is
     *parent or child if and only if it is in hierarchy
     *@param request, proposalNumber
     *@throws Exception
     */
    protected Map getParentProposalData(HttpServletRequest request, String proposalNumber) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap mapData = new HashMap();
        mapData.put("proposalNumber", proposalNumber);
        ProposalHierarchyTxnBean txnBean = new ProposalHierarchyTxnBean();
        HashMap hmData = txnBean.getParentProposalData(proposalNumber);
        return hmData;
    }
    
    /** This method is used to update the child or parent proposal sync flag
     *in OSP$EPS_PROPOSAL_HIERARCHY. This will be called only when the 
     *proposal is in hierarchy which is modified and saved
     *@param, Request, proposalNumber
     *throws Exception
     */
    protected void updateSyncFlag(HttpServletRequest request, String proposalNumber) throws Exception{
        HttpSession session = request.getSession();
        final String flag = "P";
        try {
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            WebTxnBean webTxnBean = new WebTxnBean();
            Map mapInput = new HashMap();
            mapInput.put("proposalNumber",proposalNumber);
            mapInput.put("userId",userInfoBean.getUserId());
            mapInput.put("flag", flag);
            webTxnBean.getResults(request,"updateSyncFlag", mapInput);
        } catch(Exception ex) {
             UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"ProposalBaseAction","updateSyncFlag");
        }
    }
    
    protected void updateChildProposalStatus(HttpServletRequest request, String proposalNumber) throws Exception{
        HttpSession session = request.getSession();
        final String flag = "P";
        try {
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            WebTxnBean webTxnBean = new WebTxnBean();
            String userId = userInfoBean.getUserId();
            Map mapInput = new HashMap();
            mapInput.put("proposalNumber",proposalNumber);
            mapInput.put("userId",userId);
            webTxnBean.getResults(request,"updateChildProposalStatus", mapInput);
        }catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"ProposalBaseAction","updateChildProposalStatus");
        }
    }
    
    /** Update the child proposal status if the parent proposal is submitting
     */
    protected void updateChildStatus(HttpServletRequest request, String proposalNumber)
    throws Exception{
        Map hmHierarchyData = getParentProposalData(request,proposalNumber);
        boolean isHierarchy = ((Boolean)hmHierarchyData.get("IN_HIERARCHY")).booleanValue();
        boolean isParent = ((Boolean)hmHierarchyData.get("IS_PARENT")).booleanValue();
        if(isHierarchy && isParent){
            updateChildProposalStatus(request,proposalNumber);
        }
    }
    
    /** Update the sync flag if and only if the proposal is in hierarchy
     *@param request and proposalNumber
     *@thorws Exception
     */
    protected void updateProposalSyncFlags(HttpServletRequest request, String proposalNumber)
    throws Exception{
        Map hmHierarchyData = getParentProposalData(request,proposalNumber);
        boolean isHierarchy = ((Boolean)hmHierarchyData.get("IN_HIERARCHY")).booleanValue();
        if(isHierarchy){
            updateSyncFlag(request,proposalNumber);
        }
    }
    
     /*
      * Get the S2S Attribute match for Grants.gov
      *@ param proposal number
      *@ return if it match return 1 otherwise 0
      *@ throws Exception
      */
    public String getS2SAttrMatch(String proposalNumber, HttpServletRequest request)
    throws Exception {
        String s2SAttrMatch = null;
        HashMap hmProposalNumber = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmProposalNumber.put("proposalNumber",proposalNumber);
        try {
            Hashtable htS2SDesc =
            (Hashtable)webTxnBean.getResults(request,"checkS2SAttr",hmProposalNumber);
            HashMap hmS2SDesc = (HashMap)htS2SDesc.get("checkS2SAttr");
            if(hmS2SDesc !=null && hmS2SDesc.size()>0){
                s2SAttrMatch = hmS2SDesc.get("LL_COUNT").toString();
            }
        }catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"ProposalBaseAction","getS2SAttrMatch");
        }
        return s2SAttrMatch;
    }
    
    protected void getProposalHeader(HttpServletRequest request)
    throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        String proposalNo = (String) session.getAttribute("proposalNumber"+session.getId());
        Hashtable htPropData = new Hashtable();
        htPropData.put("proposalNumber", proposalNo );
        try {
            htPropData = (Hashtable)webTxnBean.getResults(request,"getProposalHeaderData",htPropData);
            Vector vecProposalHeader = (Vector)htPropData.get("getProposalHeaderData");
            if(proposalNo!= null && !proposalNo.equals(EMPTY_STRING)){
                if(vecProposalHeader!=null && vecProposalHeader.size()>0) {
                    session.setAttribute("epsProposalHeaderBean",(EPSProposalHeaderBean)vecProposalHeader.get(0));
                }
            }else{
                /**while creating a new proposal,the bean is removed from session
                 */
                session.removeAttribute("epsProposalHeaderBean");
            }
        }catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"ProposalBaseAction","getProposalHeader");
        }
    }
    //Added for the case # COEUSQA-1675- Ability to delete proposal development proposal - Start
    /**
     * To get the delete rights of the user for the given proposal number
     * @param String proposalNumber
     * @param HttpServletRequest request
     * @param EPSProposalHeaderBean headerBean
     * @throws Exception
     */
    private boolean getDeleteRights(String proposalNumber, HttpServletRequest request, EPSProposalHeaderBean headerBean) 
    throws DBException, CoeusException, org.okip.service.shared.api.Exception {
        HttpSession session = request.getSession();
        boolean isAuthorisedToDelete = false;
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId               = userInfoBean.getUserId();
        isAuthorisedToDelete   =   txnData.getUserHasProposalRight(userId,proposalNumber, DELETE_PROPOSAL);
        //If no rights check at Unit level right
        if(!isAuthorisedToDelete){
            isAuthorisedToDelete = txnData.getUserHasRight(userId, DELETE_ANY_PROPOSAL, headerBean.getLeadUnitNumber());
        }
        return isAuthorisedToDelete;
    }
    //Added for the case # COEUSQA-1675- Ability to delete proposal development proposal - End
    protected boolean checkPropPersonCertificationComplete(HttpServletRequest request,String moduleItemKey,String prop_personId) throws Exception
  {
            PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
              WebTxnBean webTxnBean = new WebTxnBean();
              Hashtable htCertInv=new Hashtable();
              HashMap  hmCertInv=new HashMap();
              HashMap ppcmap=new HashMap();
              ppcmap.put("AV_MODULE_ITEM_CODE",3);
              ppcmap.put("AV_MODULE_SUB_ITEM_CODE",6);
              ppcmap.put("AV_MODULE_ITEM_KEY",moduleItemKey);
              ppcmap.put("AV_MODULE_SUB_ITEM_KEY",prop_personId); 
              ppcmap.put("AV_PERSONID",prop_personId);
           htCertInv= (Hashtable)webTxnBean.getResults(request, "fnGetPpcCompleteFlag", ppcmap);
           hmCertInv= (HashMap)htCertInv.get("fnGetPpcCompleteFlag");
           if(hmCertInv!=null){
           Object obj=hmCertInv.get(("isCertified").toString());
           if(obj.equals("1") && personInfoBean!=null && personInfoBean.getPersonID().equalsIgnoreCase(prop_personId)){               
               return true;
           }else{
               return false;
           }
           }else{
               return false;
           }
  }
      protected boolean getProposalPersonCertifyrightsForUser(String proposalNumber, HttpServletRequest request)throws Exception{
            HttpSession session = request.getSession();
            WebTxnBean webTxnBean = new WebTxnBean();
            boolean returnValue = false;
            HashMap hmRights = new HashMap();
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
            String userId = userInfoBean.getUserId();
            hmRights.put("userId",userId);
            hmRights.put("proposalNumber",proposalNumber);
            String rightId  = EMPTY_STRING;
            DynaValidatorForm  dynaForm = null;
            Hashtable htRightsDetail =
            (Hashtable)webTxnBean.getResults(request,"getPropRightsForUser" ,hmRights);
            if(htRightsDetail !=null && htRightsDetail.size()>0){
                Vector vecRightsDetails = (Vector)htRightsDetail.get("getPropRightsForUser");
                if(vecRightsDetails !=null && vecRightsDetails.size()>0){
                    for(int index=0; index < vecRightsDetails.size(); index++){
                        dynaForm = (DynaValidatorForm)vecRightsDetails.get(index);
                        rightId = (String) dynaForm.get("rightId");
                         if(rightId !=null && rightId.equals("MAINTAIN_PERSON_CERTIFICATION")){                      
                             returnValue = true;
                            break;
                        }
                    }
                }
             }


      //checking  MAINTAIN_DEPT_PERSONNEL_CERT right in PPC  for certification.
    HashMap hmMap=new  HashMap();
    hmMap.put("proposalNumber",proposalNumber);
    WebTxnBean webTxn = new WebTxnBean();
    String leadunit=null;
    Hashtable hTable=(Hashtable)webTxn.getResults(request,"getProposalDetail",hmMap);
    Vector right = (Vector) hTable.get("getProposalDetail");
    if(right != null && right.size()>0){
       for(int j=0;j< right.size();j++){
            DynaValidatorForm dynForm=(DynaValidatorForm)right.get(j);
            leadunit = dynForm.get("ownedByUnit").toString();
       }
    }
    RulesTxnBean ruleTxnBean=new RulesTxnBean();
    boolean  certifyightExist =ruleTxnBean.isUserHasRight(userId,leadunit,"MAINTAIN_DEPT_PERSONNEL_CERT");
    if(certifyightExist==true) {     
        returnValue = true;
    }
      // checking  VIEW_DEPT_PERSNL_CERTIFN right in PPC  for viewing certification questionnaires.
    HashMap hmp=new HashMap();
    String unit = null;
    hmp.put("proposalNumber",proposalNumber);
    Hashtable htTabl= (Hashtable)webTxn.getResults(request,"getProposalDetail",hmp);
    Vector propDetail = (Vector) htTabl.get("getProposalDetail");
    if(propDetail != null && propDetail.size() >0){
    for(int i=0;i<propDetail.size();i++) {
        DynaValidatorForm form=(DynaValidatorForm)propDetail.get(i);
        unit = form.get("ownedByUnit").toString();
    }

    }
    boolean  rightExist = ruleTxnBean.isUserHasRight(userId,unit,"VIEW_DEPT_PERSNL_CERTIFN");
     if(rightExist==true){
        returnValue = true;
    }
     
     return returnValue;
    }
   
  //COEUSQA-3951
    public boolean checkS2SSubmissionType(String proposalNumber , HttpServletRequest request){
    return isS2SSubTypIs_3(proposalNumber,request);
    }

    private boolean isS2SSubTypIs_3(String proposalNumber , HttpServletRequest request){
    boolean isS2SSubTyp_3 = true;
                try{
                    HashMap hms2s = new HashMap();
                    WebTxnBean webTxnBean = new WebTxnBean();
                    hms2s.put("proposalNumber",proposalNumber);                            
                    Hashtable hts2s = (Hashtable)webTxnBean.getResults(request,"fnCheckPropS2SSubTyp",hms2s);
                    HashMap hmUnitDesc = (HashMap)hts2s.get("fnCheckPropS2SSubTyp");
                    if(hmUnitDesc != null && !hmUnitDesc.isEmpty()){
                        int retval = Integer.parseInt(hmUnitDesc.get("returnValue").toString());
                        if(retval == 0 ){
                            isS2SSubTyp_3 = false;
                        }
                    }
                }catch(Exception e){
                    return  false;
                }
               return isS2SSubTyp_3;


    }
    
    //COEUSQA-3951
   /**
     * COEUSQA-4090
     */
    protected void refreshQuestionnaireMenus(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector proposalMenuItemsVector  = null;               
        proposalMenuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        HashMap hmData = null ;   
            hmData = new HashMap();            
            hmData.put(ModuleDataBean.class , new Integer(3));
            hmData.put(SubModuleDataBean.class , new Integer(0));
            hmData.put("link" , "/getQuestionnaire.do");
            hmData.put("actionFrom" ,"DEV_PROPOSAL");            
            String moduleItemKey = (String)session.getAttribute("proposalNumber"+session.getId());
            moduleItemKey = moduleItemKey == null ? EMPTY_STRING : moduleItemKey;
            hmData.put("moduleItemKey",moduleItemKey);
            hmData.put("moduleSubItemKey","0");            
            proposalMenuItemsVector = getRefershQuestionnaireMenuData(proposalMenuItemsVector,request ,hmData);            
            session.setAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS, proposalMenuItemsVector);      
       
    }
    private synchronized Vector getRefershQuestionnaireMenuData(Vector vecMenuData, HttpServletRequest request , HashMap hmData) throws Exception{
        PersonInfoBean personInfo = (PersonInfoBean)request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String proposalQnrMenuId = "";
        Vector vecMenuItems = new Vector();
        int moduleItemCode = ((Integer)hmData.get(ModuleDataBean.class)).intValue();
         proposalQnrMenuId = "P021";
        int moduleSubItemCode = ((Integer)hmData.get(SubModuleDataBean.class)).intValue();
        String actionFrom = (String)hmData.get("actionFrom");
        QuestionnaireHandler questHandler = 
                new QuestionnaireHandler(personInfo.getUserName());
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(moduleItemCode);
        questionnaireModuleObject.setModuleSubItemCode(moduleSubItemCode);
        questionnaireModuleObject.setModuleItemKey((String)hmData.get("moduleItemKey"));
        questionnaireModuleObject.setModuleSubItemKey((String)hmData.get("moduleSubItemKey"));
        Vector data = (Vector)questHandler.getQuestionnaireDetails(questionnaireModuleObject);
        if(vecMenuData!=null && vecMenuData.size()>0){
            for(int count = 0 ; count < vecMenuData.size() ; count++){
                MenuBean menuBeanData = (MenuBean) vecMenuData.get(count);
                if(menuBeanData.getMenuId().equals(proposalQnrMenuId)){
                    if(data!= null && data.size() > 0){ 
                        vecMenuItems.add(menuBeanData);
                        for(int index = 0; index < data.size(); index++){
                            edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean qnrHeaderBean =
                                    (edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean)data.get(index);
                            MenuBean menuBean = new MenuBean();
                            menuBean.setUserObject(qnrHeaderBean);
                            menuBean.setDataSaved(false);
                            String questionnaireLabel = qnrHeaderBean.getLabel();
                            if(questionnaireLabel == null){
                                questionnaireLabel = EMPTY_STRING+qnrHeaderBean.getQuestionnaireId();
                                qnrHeaderBean.setLabel(questionnaireLabel);
                            }
                            menuBean.setFieldName(questionnaireLabel);
                            menuBean.setVisible(true);
                            int queryString = qnrHeaderBean.getQuestionnaireId();

                            StringBuffer sbLink = new StringBuffer("/getQuestionnaire.do?questionnaireId=");
                            sbLink.append(queryString);
                            sbLink.append("&menuId=");
                            sbLink.append(proposalQnrMenuId);
                            sbLink.append(index);
                            sbLink.append("&actionFrom=");
                            sbLink.append(actionFrom);
                            sbLink.append("&questionaireLabel=");
                            sbLink.append(qnrHeaderBean.getLabel());
                            sbLink.append("&apSubModuleCode=");
                            sbLink.append(String.valueOf(qnrHeaderBean.getApplicableSubmoduleCode()));
                            sbLink.append("&apModuleItemKey=");
                            sbLink.append(qnrHeaderBean.getApplicableModuleItemKey());
                            sbLink.append("&apModuleSubItemKey=");
                            sbLink.append(String.valueOf(qnrHeaderBean.getApplicableModuleSubItemKey()));
                            sbLink.append("&completed=");
                            sbLink.append(qnrHeaderBean.getQuestionnaireCompletionFlag());
                            menuBean.setMenuLink(sbLink.toString());                            
                            menuBean.setMenuId(proposalQnrMenuId+index);
                            menuBean.setGroup(menuBeanData.getGroup());
                            menuBean.setDynamicId(EMPTY_STRING+qnrHeaderBean.getQuestionnaireId());
                            menuBean.setMenuName(qnrHeaderBean.getLabel());
                            menuBean.setSelected(false);
                            vecMenuItems.addElement(menuBean);
                        }
                    }
                } else {
                  
                    if(!menuBeanData.getMenuId().startsWith(proposalQnrMenuId)){
                       vecMenuItems.add(menuBeanData);
                    }
                }
            }
        }
        return vecMenuItems;
    }
//COEUSQA-4090
}

    
