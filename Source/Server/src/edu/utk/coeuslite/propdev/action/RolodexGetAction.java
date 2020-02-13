/*
 * RolodexGetAction.java
 *
 * Created on February 12, 2008, 1:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
//import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
//import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.SearchModuleBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
//import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.bean.MenuBean;
//import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import java.util.Map;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Collections;

//import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.utils.dbengine.DBException;
//import edu.mit.coeus.utils.dbengine.DBEngineImpl;
//import edu.mit.coeus.utils.dbengine.DBEngineConstants;
//import edu.mit.coeus.utils.dbengine.Parameter;
//import edu.mit.coeus.utils.dbengine.ProcReqParameter;
//import edu.mit.coeus.utils.dbengine.TransactionMonitor;
//import edu.mit.coeus.utils.CoeusVector;
//import java.sql.Timestamp;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.bean.AddressBean;

/**
 *
 * @author nandkumarsn
 */
public class RolodexGetAction extends ProposalBaseAction {

    private static final String GET_ROLODEX_FORM_DATA  = "/getRolodexInfo";
    private static final String SAVE_NEW_ROLODEX_ENTRY = "/rolodexInfoSave";
    //private static final String GET_ROLODEX_INFO_DATA = "/getRolodexInfo"; // May remove later.
    //private static final String ADD_ROLODEX_INFO_DATA = "/addRolodexInfo"; // May remove later.
//    private static final String CREATE_RIGHT           = "CREATE_ROLODEX";
//    private DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
//    private static final String DEFAULT_CREATION_DESCRIPTION = "Creating Rolodex Entry";
//    private static final String DEFAULT_ORGANIZATION_ID = "DEFAULT_ORGANIZATION_ID";
    public static final String EMPTY_STRING = "";
//    private static final String AC_TYPE="acType";
//    private static final String AC_TYPE_INSERT = "I";
//    private static final String STATUS_CODE    = "1";
//    private Timestamp dbTimestamp;
//    private static final String UPDATE_TIMESTAMP = "pdSpTimestamp";
//    private static final String ROLODEX_ID       = "rolodexId";
//    private static final String UPDATE_ROLODEX   = "addUpdRolodex";
//    private static final String ADD_NEW_ROLODEX_ENTRY_CODE = "P777";
//    private static final String DEFAULT_CREATION_STATUS_DESC = "In Progress";
//    private static final String MODIFY_ANY_PROPOSAL_RIGHT = "MODIFY_ANY_PROPOSAL";
     //Stores the RIGHT ID which is required for checking user rights to modify proposal
//    private static final String MODIFY_PROPOSAL_RIGHT="MODIFY_PROPOSAL";
     //Stores the RIGHT ID which is required for checking user rights to modify narrative
//    private static final String MODIFY_NARRATIVE_RIGHT="MODIFY_NARRATIVE";
      //Stores the RIGHT ID which is required for checking user rights to modify budget
//    private static final String MODIFY_BUDGET_RIGHT="MODIFY_BUDGET";
    
//    private static final String VIEW_ANY_PROPOSAL_RIGHT="VIEW_ANY_PROPOSAL";
    //Stores the RIGHT ID which is required for checking user rights to view proposal
//    private final String VIEW_RIGHT="VIEW_PROPOSAL";
    // Submit for Approval Mode
    private static String strMode;
    private static final String NEWRECORD = "newRecord";
    // Add to Rolodex Role
    private static final String IS_ADD_ROLODEX = "ADD_ROLODEX";
    
//    private RolodexDetailsBean rldxDetails;
//    private DBEngineImpl dbEngine;
   
    /** Creates a new instance of RolodexGetAction */
    public RolodexGetAction() {
    }
    // First method that executes when the form loads.
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        session.setAttribute("previousRecord", NEWRECORD);        
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;       
        HashMap hmRequiredDetails = new HashMap();
        hmRequiredDetails.put(ActionMapping.class,actionMapping);
        hmRequiredDetails.put(DynaValidatorForm.class,dynaForm);
        
        ActionForward actionForward = getRolodexData(hmRequiredDetails, request);//
        getProposalMenus(request);
//        String subHeaderId =  request.getParameter("SUBHEADER_ID");
        enableDisableMenus(request);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.ADD_NEW_ROLODEX_ENTRY_CODE); 
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request);
        if(request.getParameter("page")!=null && !request.getParameter("page").equals(EMPTY_STRING)){
          actionForward = actionMapping.findForward("success"); 
        }
        String dataModified = request.getParameter("dataModified");
        if(dataModified != null && !dataModified.equals(EMPTY_STRING) && dataModified.equals("Y")){
            request.setAttribute("dataModified", "modified");
        }
        return actionForward;
}

    /** This method will identify what the request is and from which path it comes 
     *  and navigates to the respective ActionForward
     *  @returns ActionForward object
     */
    private ActionForward getRolodexData(HashMap hmRequiredDetails,
            HttpServletRequest request)throws Exception{  

            String navigator = EMPTY_STRING;
//            WebTxnBean webTxnBean = new WebTxnBean();
            ActionForward actionForward = null;
            ActionMapping actionMapping = (ActionMapping)hmRequiredDetails.get(ActionMapping.class);
           // Here we are going to determine what the request contains and call
           // the applicable action matching the request.
            if(actionMapping.getPath().equals(GET_ROLODEX_FORM_DATA)){
                navigator = addNewRolodexPerson(hmRequiredDetails, request);
                actionForward = actionMapping.findForward(navigator);
            } else if(actionMapping.getPath().equals(SAVE_NEW_ROLODEX_ENTRY)){
                navigator = addNewRolodexPerson(hmRequiredDetails, request);
                actionForward = actionMapping.findForward(navigator);
            }
            
            return actionForward;
    }    
    
    /**
     * This method is to add new Rolodex Entry Information
     * @param dynaForm
     * @throws Exception
     */
        
    private String addNewRolodexPerson(HashMap hmRequiredDetails, HttpServletRequest request) throws Exception{
        String navigator    = "success";
        ActionMessages actionMessages = new ActionMessages();
        PersonInfoBean personInfoBean = null;
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        //rldxDetails = new RolodexDetailsBean(); // May not be used (remove durring cleanup).
        // *********************************************************************
        // * The gettingSponsorAddress parameter is used to tell us they       *
        // * picked a sponsor using the search.                                *
        // *********************************************************************        
        String whatAboutTheSponsor = request.getParameter("getSponsorAddress");
        // *********************************************************************
        // * The addressBean is used to hold the sponsor address!              *
        // *********************************************************************
        AddressBean addressBean = new AddressBean(); 
        // *********************************************************************
        // * The coeusFunctions.getRolodexAddress(sponsorRolodexId)            * 
        // * is used to get sponsor address!                                   *
        // *********************************************************************
        CoeusFunctions coeusFunctions = new CoeusFunctions(); 
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userName = EMPTY_STRING;
        // *********************************************************************
        // User information intialization
        // *********************************************************************
        if(userInfoBean!=null && userInfoBean.getUserId()!=null){
            userName     = userInfoBean.getUserId();
            userInfoBean = userDetailsBean.getUserInfo(userName.trim());
            personInfoBean = userDetailsBean.getPersonInfo(userInfoBean.getPersonId(), false);
            String loggedinUser = personInfoBean.getFullName();
            session.setAttribute("createUser", loggedinUser);
            session.setAttribute("InvUser", loggedinUser);
        }
        // *********************************************************************
        // Checking to see if the user can add rolodex entries.
        // *********************************************************************
        String createUser  =  userInfoBean.getUserId();
        String ownedByUnit =  userInfoBean.getUnitNumber();
        boolean canAddToRolodex = isUserHasRight(request, ownedByUnit, createUser);       
        if(canAddToRolodex){
            session.setAttribute("maintainRolodex", "OK");
            actionMessages.add("AddRolodexInformation", new ActionMessage("newRolodexEntryMessage.AddRolodexInformation"));
            saveMessages(request, actionMessages);
        } else {
            session.setAttribute("maintainRolodex", "NO");
            actionMessages.add("AddRolodexInformationRight", new ActionMessage("newRolodexEntryMessage.AddRolodexInformationRight"));
            saveMessages(request, actionMessages);
        }
        // *********************************************************************
        // Setting up to use the Dynavalidator form.
        // *********************************************************************
        DynaValidatorForm dynaForm = (DynaValidatorForm)hmRequiredDetails.get(DynaValidatorForm.class);
//        HashMap hmHeaderData = new HashMap();
        // *********************************************************************
        // Populate the drop downs in the form.
        // *********************************************************************
        Hashtable htRolodexDetail = (Hashtable)webTxnBean.getResults(request, "getNewRolodexEntry", null);
        Vector vecCountryList     = (Vector)htRolodexDetail.get("getRolodexCountryList"); // Populate the Country Dropdown
        Vector vecStateList       = (Vector)htRolodexDetail.get("getRolodexStateList");   // Populate the State   Dropdown
        //Added for Case#3789 -Add a New Rolodex Entry using CoeusLite interface - Start
            Map hmStateCountry = new HashMap();            
            if(vecCountryList != null && vecCountryList.size() >0){
                for(int cnt = 0; cnt < vecCountryList.size(); cnt++){
                    edu.mit.coeuslite.utils.ComboBoxBean cmbCountryBean =
                            (edu.mit.coeuslite.utils.ComboBoxBean)vecCountryList.get(cnt);
                    String country = cmbCountryBean.getCode();
                    // 4039: Editing the person details in Coeus Lite and the State box is not sorted - Start
//                    Map hmStates = new HashMap();
                    Map hmStates = new LinkedHashMap();
                    // 4039: Editing the person details in Coeus Lite and the State box is not sorted - End
                    if(vecStateList != null && vecStateList.size() > 0){
                        for(int state = 0 ; state < vecStateList.size(); state++){
                            DynaValidatorForm dynaStateForm =
                                    (DynaValidatorForm)vecStateList.get(state);                            
                            if(dynaStateForm.get("country") != null
                                    && country.equals(dynaStateForm.get("country"))){
                                String stateCode = (String)dynaStateForm.get("stateCode");
                                String stateDescription = (String)dynaStateForm.get("stateDescription");
                                hmStates.put(stateCode,stateDescription);
                            }
                        }
                    }
                    hmStateCountry.put(country,hmStates);
                }
            }        
        session.setAttribute("countryList", vecCountryList);
//        session.setAttribute("stateList",   vecStateList);
        session.setAttribute("validCountryStateList",hmStateCountry);
        //Added for Case#3789 -Add a New Rolodex Entry using CoeusLite interface-End
        // *********************************************************************
        // Submitted form processing data received.
        // *********************************************************************
                
        String firstName    = (String)request.getParameter("firstName");
        
        if(firstName!=null && !firstName.equals(EMPTY_STRING)){
            dynaForm.set("firstName", firstName);
        } else {
            dynaForm.set("firstName", EMPTY_STRING);
        }

        String middleName   = (String)request.getParameter("middleName");

        if(middleName!=null && !middleName.equals(EMPTY_STRING)){        
            dynaForm.set("middleName", middleName);
        } else {
            dynaForm.set("middleName", EMPTY_STRING);
        }
            
        String lastName     = (String)request.getParameter("lastName");
        
        if(lastName!=null && !lastName.equals(EMPTY_STRING)){        
             dynaForm.set("lastName", lastName);
        } else {
             dynaForm.set("lastName", EMPTY_STRING);            
        }
        
        String suffix       = (String)request.getParameter("suffix");

        if(suffix!=null && !suffix.equals(EMPTY_STRING)){        
            dynaForm.set("suffix", suffix);
        } else {
            dynaForm.set("suffix", EMPTY_STRING);            
        }
        
        String prefix       = (String)request.getParameter("prefix");
        
        if(prefix!=null && !prefix.equals(EMPTY_STRING)){        
             dynaForm.set("prefix", prefix);
        } else {
             dynaForm.set("prefix", EMPTY_STRING);            
        }
        
        String title        = (String)request.getParameter("title");
        
        if(title!=null && !title.equals(EMPTY_STRING)){        
             dynaForm.set("title", title);
        } else {
             dynaForm.set("title", EMPTY_STRING);            
        }
        
        String sponsor      = (String)request.getParameter("sponsor");
        
        if(sponsor!=null && !sponsor.equals(EMPTY_STRING)){        
            dynaForm.set("sponsor", sponsor);
            String sponsorRolodexId = Integer.toString(getRolodexIdInSponsor(request, sponsor));
            addressBean = coeusFunctions.getRolodexAddress(sponsorRolodexId);
        } else {
            dynaForm.set("sponsor", EMPTY_STRING);            
        }
        
        String organization = (String)request.getParameter("organization");

        if(organization!=null && !organization.equals(EMPTY_STRING)){        
            dynaForm.set("organization", organization);
        } else {
            dynaForm.set("organization", EMPTY_STRING);            
        }

        String address1     = (String)request.getParameter("address1");

        if(address1!=null && !address1.equals(EMPTY_STRING) && whatAboutTheSponsor==null){        
            dynaForm.set("address1", address1);
        } else {
            if(sponsor!=null && !sponsor.equals(EMPTY_STRING) && whatAboutTheSponsor!=null && !whatAboutTheSponsor.equals(EMPTY_STRING)){
                dynaForm.set("address1", EMPTY_STRING); // First clear any previous content.
                address1 = addressBean.getAddressLine_1();
                if(address1!=null && !address1.equals(EMPTY_STRING)){
                    dynaForm.set("address1", address1);
                } else {
                    dynaForm.set("address1", EMPTY_STRING);
                }
            } else {
                dynaForm.set("address1", EMPTY_STRING);
            }
        }
        
        String address2     = (String)request.getParameter("address2");

        if(address2!=null && !address2.equals(EMPTY_STRING) && whatAboutTheSponsor==null){        
            dynaForm.set("address2", address2);
        } else {
            if(sponsor!=null && !sponsor.equals(EMPTY_STRING) && whatAboutTheSponsor!=null && !whatAboutTheSponsor.equals(EMPTY_STRING)){
                dynaForm.set("address2", EMPTY_STRING); // First clear any previous content.
                address2 = addressBean.getAddressLine_2();
                if(address2!=null && !address2.equals(EMPTY_STRING)){
                    dynaForm.set("address2", address2);
                } else {
                    dynaForm.set("address2", EMPTY_STRING);
                }
            } else {
                dynaForm.set("address2", EMPTY_STRING);
            }
        }
        
        String address3     = (String)request.getParameter("address3");

        if(address3!=null && !address3.equals(EMPTY_STRING) && whatAboutTheSponsor==null){        
            dynaForm.set("address3", address3);
        } else {
            if(sponsor!=null && !sponsor.equals(EMPTY_STRING) && whatAboutTheSponsor!=null && !whatAboutTheSponsor.equals(EMPTY_STRING)){
                dynaForm.set("address3", EMPTY_STRING); // First clear any previous content.
                address3 = addressBean.getAddressLine_3();
                if(address3!=null && !address3.equals(EMPTY_STRING)){
                    dynaForm.set("address3", address3);
                } else {
                    dynaForm.set("address3", EMPTY_STRING);
                }
            } else {
                dynaForm.set("address3", EMPTY_STRING);
            }
        }

        String city         = (String)request.getParameter("city");
        
        if(city!=null && !city.equals(EMPTY_STRING) && whatAboutTheSponsor==null){        
            dynaForm.set("city", city);
        } else {
            if(sponsor!=null && !sponsor.equals(EMPTY_STRING) && whatAboutTheSponsor!=null && !whatAboutTheSponsor.equals(EMPTY_STRING)){
                dynaForm.set("city", EMPTY_STRING); // First clear any previous content.
                city = addressBean.getCity();
                if(city!=null && !city.equals(EMPTY_STRING)){
                    dynaForm.set("city", city);
                } else {
                    dynaForm.set("city", EMPTY_STRING);
                }
            } else {
                dynaForm.set("city", EMPTY_STRING);
            }
        }
        
        String state        = (String)request.getParameter("state");

        if(state!=null && !state.equals(EMPTY_STRING) && whatAboutTheSponsor==null){        
            dynaForm.set("state", state);
        } else {
            if(sponsor!=null && !sponsor.equals(EMPTY_STRING) && whatAboutTheSponsor!=null && !whatAboutTheSponsor.equals(EMPTY_STRING)){
                dynaForm.set("state", EMPTY_STRING); // First clear any previous content.
                state = addressBean.getStateCode();
                if(state!=null && !state.equals(EMPTY_STRING)){
                    dynaForm.set("state", state);
                } else {
                    dynaForm.set("state", EMPTY_STRING);
                }
            } else {
                dynaForm.set("state", EMPTY_STRING);
            }
        }
        
        String postalCode   = (String)request.getParameter("postalCode");

        if(postalCode!=null && !postalCode.equals(EMPTY_STRING) && whatAboutTheSponsor==null){        
            dynaForm.set("postalCode", postalCode);
        } else {
            if(sponsor!=null && !sponsor.equals(EMPTY_STRING) && whatAboutTheSponsor!=null && !whatAboutTheSponsor.equals(EMPTY_STRING)){
                dynaForm.set("postalCode", EMPTY_STRING); // First clear any previous content.
                postalCode = addressBean.getPostalCode();
                if(postalCode!=null && !postalCode.equals(EMPTY_STRING)){
                    dynaForm.set("postalCode", postalCode);
                } else {
                    dynaForm.set("postalCode", EMPTY_STRING);
                }
            } else {
                dynaForm.set("postalCode", EMPTY_STRING);
            }
        }
        
        String county       = (String)request.getParameter("county");

        if(county!=null && !county.equals(EMPTY_STRING) && whatAboutTheSponsor==null){        
            dynaForm.set("county", county);
        } else {
            if(sponsor!=null && !sponsor.equals(EMPTY_STRING) && whatAboutTheSponsor!=null && !whatAboutTheSponsor.equals(EMPTY_STRING)){
                dynaForm.set("county", EMPTY_STRING); // First clear any previous content.
                postalCode = addressBean.getCounty();
                if(county!=null && !county.equals(EMPTY_STRING)){
                    dynaForm.set("county", county);
                } else {
                    dynaForm.set("county", EMPTY_STRING);
                }
            } else {
                dynaForm.set("county", EMPTY_STRING);
            }
        }
        
        String country      = (String)request.getParameter("country");

        if(country!=null && !country.equals(EMPTY_STRING) && whatAboutTheSponsor==null){        
            dynaForm.set("country", country);
        } else {
            if(sponsor!=null && !sponsor.equals(EMPTY_STRING) && whatAboutTheSponsor!=null && !whatAboutTheSponsor.equals(EMPTY_STRING)){
                dynaForm.set("country", EMPTY_STRING); // First clear any previous content.
                country = addressBean.getCountryCode();
                if(country!=null && !country.equals(EMPTY_STRING)){
                    dynaForm.set("country", country);
                } else {
                    dynaForm.set("country", EMPTY_STRING);
                }
            } else {
                dynaForm.set("country", EMPTY_STRING);
            }
        }
        
        String phone        = (String)request.getParameter("phone");

        if(phone!=null && !phone.equals(EMPTY_STRING) && whatAboutTheSponsor==null){        
            dynaForm.set("phone", phone);
        } else {
            if(sponsor!=null && !sponsor.equals(EMPTY_STRING) && whatAboutTheSponsor!=null && !whatAboutTheSponsor.equals(EMPTY_STRING)){
                dynaForm.set("phone", EMPTY_STRING); // First clear any previous content.
                phone = addressBean.getPhoneNumber();
                if(phone!=null && !phone.equals(EMPTY_STRING)){
                    dynaForm.set("phone", phone);
                } else {
                    dynaForm.set("phone", EMPTY_STRING);
                }
            } else {
                dynaForm.set("phone", EMPTY_STRING);
            }
        }
        
        String email        = (String)request.getParameter("email");

        if(email!=null && !email.equals(EMPTY_STRING)){        
            dynaForm.set("email", email);
        } else {
            dynaForm.set("email", email);            
        }
        
        String fax          = (String)request.getParameter("fax");

        if(fax!=null && !fax.equals(EMPTY_STRING)){        
            dynaForm.set("fax", fax);
        } else {
            dynaForm.set("fax", EMPTY_STRING);            
        }
        
        String comments     = (String)request.getParameter("comments");

        if(comments!=null && !comments.equals(EMPTY_STRING)){        
            dynaForm.set("comments", comments);
        } else {
            dynaForm.set("comments", comments);            
        }
        
        return navigator;
    }
    

   /** 
     * To set the selected status for the Proposal Menus
     */
    private void setSelectedStatusMenu(String menuCode, HttpServletRequest request){
        String submitForApproval = "P012";
        Vector menuItemsVector  = null;
        HttpSession session = request.getSession();
        menuItemsVector=(Vector)session.getAttribute("proposalMenuItemsVector");
        Vector modifiedVector = new Vector();
        for (int index=0; index<menuItemsVector.size();index++) {
            MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
            String menuId = meanuBean.getMenuId();
            if (menuId.equals(menuCode)) {
                meanuBean.setSelected(true);
            } else {
                if(getMode(session).equalsIgnoreCase("display") && menuId.equals(submitForApproval)){
                    meanuBean.setVisible(false);
                } else if(menuId.equals(submitForApproval) && (!meanuBean.isVisible())){
                        meanuBean.setVisible(true);    
                }
                meanuBean.setSelected(false);
            }
            modifiedVector.add(meanuBean);
        }
        setMode("");
        session.setAttribute("proposalMenuItemsVector", modifiedVector);
    } 

    
     /**
     *This method returns unit name for particular unit number not sure if we will use this
     */
    private String getUnitName(String unitNumber, HttpServletRequest request) throws Exception{
        HashMap hmUnitNumber = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        String unitDesc = "";
        hmUnitNumber.put("ownedUnit",unitNumber);
        Hashtable htUnitDesc = 
            (Hashtable)webTxnBean.getResults(request,"getUnitDesc",hmUnitNumber);
        HashMap hmUnitDesc = (HashMap)htUnitDesc.get("getUnitDesc");
        if(hmUnitDesc!= null && hmUnitDesc.size() > 0){
            unitDesc = (String)hmUnitDesc.get("RetVal");
        }
        return unitDesc;
    }

    /* Getter and setter methods for mode
     *
     */
    private String getMode (HttpSession session){
        strMode = (String)session.getAttribute("mode"+session.getId());
        strMode = (strMode == null)?"":strMode;
        return strMode;
    }

    private void  setMode (String strMode){
        strMode = strMode;
    }
    
    /** This method will notify for the acquiring and releasing the Lock
     *based on the way the locks are opened.
     *It will check whether the proposal is opened through search or list
     *Based on the conditions it will acquire the lock and release the lock
     *If it locked then it will prepare the locking messages
     *@param UserInfoBean, ProposalNumber(Current)
     *@throws Exception
     *@returns boolean is locked or not
     */
    private boolean prepareLock(UserInfoBean userInfoBean, String proposalNumber, 
        HttpServletRequest request) throws Exception{
        boolean isSuccess = true;
        HttpSession session = request.getSession();
//        WebTxnBean webTxnBean = new WebTxnBean();
        String mode = (String)session.getAttribute("mode"+session.getId());
        SearchModuleBean moduleBean = (SearchModuleBean)session.getAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
        LockBean lockBean = null;
        LockBean sessionLockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
    
        // If the action is from search window
        if(moduleBean!= null && !moduleBean.getOldModuleNumber().equals(EMPTY_STRING)){
            if(!moduleBean.getModuleNumber().equals(moduleBean.getOldModuleNumber())){
                // If the existing proposal number is not in DISPLAY MODE, release the lcok
                if(!moduleBean.getOldMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    lockBean = getLockingBean(userInfoBean, moduleBean.getOldModuleNumber(), request);
                    releaseLock(lockBean,request);
                    session.removeAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
                    session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(false));
                }
                
            }
                moduleBean.setMode(getMode(mode));
            // If the current Proposal number is in MODIFY MODE then lock it
                if(!moduleBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    lockBean = getLockingBean(userInfoBean,moduleBean.getModuleNumber(),request);
                    boolean isLocked = isLockExists(lockBean, lockBean.getModuleKey());
                    boolean isSessionRowLocked = false;
                    Object  isRowLocked = session.getAttribute(
                        CoeusLiteConstants.RECORD_LOCKED+session.getId());
                    if(isRowLocked!= null){
                        isSessionRowLocked = ((Boolean)isRowLocked).booleanValue();
                    }
                    /** Make server call and get the locked data. Check for the unit
                     *number. If the unit number!= 00000000 then assume that
                     *it is lokced by the coeus premium and show the message
                     */
                    LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
                    // If the current proposal is locked by other user, then show the message
                    // else lock it
                    if(!isLocked ){
                        /** Check if the same record is locked or not. If not 
                         *then only show the message else discard it
                         */
                        if(!isSessionRowLocked || !serverDataBean.getSessionId().equals(lockBean.getSessionId())){
                            showLockingMessage(lockBean.getModuleNumber(), request);
                            isSuccess = false;
                        }// End if for lockeed record in the session
                    }else{// If the record is not locked then go ahead and lock it
                        lockModule(lockBean, request);
                        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(true));
                    }
                }
            
        }else{
            // Proposal opened from list
            lockBean = getLockingBean(userInfoBean, proposalNumber,request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
            if(isLockExists) {
                if(serverDataBean!=null && !lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                    isLockExists = false;
                }
            }
            /** check whether lock exists or not. If not and the mode of the 
             *propsoal is not disaply then lock the proposal else show the message
             */
            if(isLockExists && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)) {
                lockModule(lockBean, request);
                session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(true));
            }else{
                if(sessionLockBean == null && !lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE)){
                    showLockingMessage(lockBean.getModuleNumber(), request);
                    isSuccess = false;
                } else if(sessionLockBean!=null && serverDataBean!=null) {
                    if(!lockBean.getSessionId().equals(serverDataBean.getSessionId())) {
                        showLockingMessage(lockBean.getModuleNumber(), request);
                        isSuccess = false;                        
                    }
                }
            }
        }
        session.removeAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+session.getId());
        return isSuccess;
        
    }

    
   /** Prepare the Locking messages when other or same user locked
     *the same module number. Make server call to get the message for the
     *locked user
     *@param String moduleNumber
     *@throws Exception
     */
    private void showLockingMessage(String moduleNumber, HttpServletRequest request) throws Exception{
        String lockId = CoeusLiteConstants.PROP_DEV_LOCK_STR+moduleNumber;
        HttpSession session = request.getSession();        
//        WebTxnBean webTxnBean = new WebTxnBean();
        LockBean serverLockedBean = getLockedData(lockId,request);
        if(serverLockedBean!= null){
            serverLockedBean.setModuleKey("Development Proposal");
            serverLockedBean.setModuleNumber(moduleNumber);
            String lockUserId = serverLockedBean.getUserId();
            UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
            String lockUserName = userTxnBean.getUserName(lockUserId);
            String acqLock = "acquired_lock";
            ActionMessages messages = new ActionMessages();
            messages.add("acqLock", new ActionMessage(acqLock,
                lockUserName,serverLockedBean.getModuleKey(),
                    serverLockedBean.getModuleNumber()));
            //End
            saveMessages(request, messages);
            session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        }
    }
    
    /**
     *This method is used to check the Add Rolodex Person Info Rights
     * @param  HttpServletRequest
     * @param ownedByUnit
     * @param createUser
     * @return canAddRolodexPerson
     * @throws Exception
     */

    private boolean isUserHasRight(HttpServletRequest request, String ownedByUnit, String createUser)throws Exception{
        boolean canAddRolodexPerson = false;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put("createUser" , createUser);
        hmData.put("ownedByUnit", ownedByUnit);
        hmData.put("ownerRight" , IS_ADD_ROLODEX);
        Hashtable htAddRolodexPerson =
        (Hashtable)webTxnBean.getResults(request, "isUserAddRolodexRight", hmData);
        HashMap hmAddRolodexPerson = (HashMap)htAddRolodexPerson.get("isUserAddRolodexRight");
        if(hmAddRolodexPerson !=null && hmAddRolodexPerson.size()>0){
            int canView = Integer.parseInt(hmAddRolodexPerson.get("retValue").toString());
            if(canView == 1){
                canAddRolodexPerson = true ;
            }
        }
        return canAddRolodexPerson;
    }
    
    private int getRolodexIdInSponsor(HttpServletRequest request, String sponsorCode)throws Exception{
        int rolodexIdFromSponsor = 0;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put("sponsorCode" , sponsorCode);
        Hashtable htRolodexIdFromSponsor =
        (Hashtable)webTxnBean.getResults(request, "getRolodexIdInSponsor", hmData);
        HashMap hmRolodexIdFromSponsor = (HashMap)htRolodexIdFromSponsor.get("getRolodexIdInSponsor");
        if(hmRolodexIdFromSponsor !=null && hmRolodexIdFromSponsor.size()>0){
            rolodexIdFromSponsor = Integer.parseInt(hmRolodexIdFromSponsor.get("sponsorRolodexId").toString());
        }
        return rolodexIdFromSponsor;
    }
}