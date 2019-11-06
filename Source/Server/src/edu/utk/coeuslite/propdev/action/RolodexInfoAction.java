/*
 * RolodexInfoAction.java
 *
 * Created on February 12, 2008, 1:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;

//import edu.mit.coeus.bean.RoleInfoBean;
//import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
//import edu.mit.coeus.bean.UserRolesInfoBean;
//import edu.mit.coeus.propdev.bean.ProposalDevelopmentUpdateTxnBean;
//import edu.mit.coeus.propdev.bean.ProposalLocationFormBean;
//import edu.mit.coeus.propdev.bean.ProposalUserRoleFormBean;
//import edu.mit.coeus.utils.CoeusConstants;
//import edu.mit.coeus.utils.DateUtils;
//import edu.mit.coeus.utils.TypeConstants;
//import edu.mit.coeuslite.utils.CoeusLiteConstants;
//import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
//import java.util.Vector;
//import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import org.apache.commons.beanutils.BeanUtilsBean;
//import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
//import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
//import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
//import java.util.Map;

/**
 *
 * @author nandkumarsn
 */
public class RolodexInfoAction extends ProposalBaseAction {
    private static final String EMPTY_STRING = "";
    private static final String AC_TYPE_INSERT = "I";
    private static final String IS_ADD_ROLODEX = "ADD_ROLODEX";
//    private static final String STATUS_CODE = "1";
//    private static final String CREATION_STATUS_CODE = "1";
    private Timestamp dbTimestamp;
//    private static final String DEFAULT_ORGANIZATION_ID = "DEFAULT_ORGANIZATION_ID";
//    private static final String AC_TYPE_UPDATE="U";
//    private static final String DEFAULT_CREATION_STATUS_DESC = "In Progress";
//    private static final String OWNED_BY_UNIT = "000001";
    private static final String SPONSOR_ADDRESS_FLAG = "N";
//    private static final String SUCCESSFUL = "success";
    
    /** Creates a new instance of RolodexInfoAction */
    public RolodexInfoAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
//        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm addNewRolodexEntryForm = (DynaValidatorForm)actionForm;
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        DateUtils dtUtils = new DateUtils();
        String acType       = AC_TYPE_INSERT;
//        String firstName    = (String)addNewRolodexEntryForm.get("firstName");
//        String lastName     = (String)addNewRolodexEntryForm.get("lastName");
//        String middleName   = (String)addNewRolodexEntryForm.get("middleName");
//        String suffix       = (String)addNewRolodexEntryForm.get("suffix");
//        String prefix       = (String)addNewRolodexEntryForm.get("prefix");
//        String title        = (String)addNewRolodexEntryForm.get("title");
//        String sponsor      = (String)addNewRolodexEntryForm.get("sponsor");
//        String organization = (String)addNewRolodexEntryForm.get("organization");
//        String address1     = (String)addNewRolodexEntryForm.get("address1");
//        String address2     = (String)addNewRolodexEntryForm.get("address2");
//        String address3     = (String)addNewRolodexEntryForm.get("address3");
//        String city         = (String)addNewRolodexEntryForm.get("city");
//        String state        = (String)addNewRolodexEntryForm.get("state");
//        String postalCode   = (String)addNewRolodexEntryForm.get("postalCode");
//        String county       = (String)addNewRolodexEntryForm.get("county");
//        String country      = (String)addNewRolodexEntryForm.get("country");
//        String phone        = (String)addNewRolodexEntryForm.get("phone");
//        String email        = (String)addNewRolodexEntryForm.get("email");
//        String fax          = (String)addNewRolodexEntryForm.get("fax");
//        String comments     = (String)addNewRolodexEntryForm.get("comments");
//        
//        if(country.equals("US") || country.equals("USA")){
//        
//        }
        
        
        if(acType!=null && !acType.equals(EMPTY_STRING)){
            if(acType.equals(AC_TYPE_INSERT)){
                addNewRolodexPerson(request,addNewRolodexEntryForm);
            }else{
                    String errMsg = "Error inserting record!";
                    ActionMessages messages = new ActionMessages();
                    messages.add("errMsg", new ActionMessage(errMsg,"Data insert error","edu.coeus.utk.propdev.action.RolodexInfoAction"));
                    saveMessages(request, messages);
            }
        }

//        HashMap hmProposalHeader =  new HashMap();
        if(webTxnBean == null){
            webTxnBean = new WebTxnBean();
        }


        readSavedStatus(request);        
        return actionMapping.findForward("success");
    }
    
    /*This method is to add a new proposal  */
    /**
     * This method is to add new Rolodex Person Information
     * @param request
     * @param dynaForm
     * @throws Exception
     */
    private void addNewRolodexPerson(HttpServletRequest request,DynaValidatorForm dynaForm) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        boolean isSaveRequired = true ;
        DynaValidatorForm addNewRolodexEntryForm = (DynaValidatorForm)dynaForm;
        ActionMessages actionMessages = new ActionMessages();
        HashMap   hmRequiredDetails = new HashMap();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String createUser  =  userInfoBean.getUserId();
        String ownedByUnit = userInfoBean.getUnitNumber(); 

        boolean canAddToRolodex = isUserHasRight(request, ownedByUnit, createUser);
        
// *****************************************************************************
// ** If user cannot add to the rolodex we will protect the form fields.      **
// *****************************************************************************
        if(canAddToRolodex){
            session.setAttribute("maintainRolodex", "OK");
        }

        String acType       = AC_TYPE_INSERT; // We only want to insert from this page!
        
        String firstName    = (String)addNewRolodexEntryForm.get("firstName");
        String lastName     = (String)addNewRolodexEntryForm.get("lastName");
        String middleName   = (String)addNewRolodexEntryForm.get("middleName");
        String suffix       = (String)addNewRolodexEntryForm.get("suffix");
        String prefix       = (String)addNewRolodexEntryForm.get("prefix");
        String title        = (String)addNewRolodexEntryForm.get("title");
        String sponsor      = (String)addNewRolodexEntryForm.get("sponsor");
        String organization = (String)addNewRolodexEntryForm.get("organization");
// *****************************************************************************
// ** Grey screen currently requires organization before a save.              **
// *****************************************************************************
        if(organization == null || organization.equals(EMPTY_STRING)){
            isSaveRequired = false;
            actionMessages.add("isNotValidOrganization", new ActionMessage("addNewRolodexEntry.isNotValidOrganization"));
            saveMessages(request, actionMessages);
        }

        String address1     = (String)addNewRolodexEntryForm.get("address1");
        String address2     = (String)addNewRolodexEntryForm.get("address2");
        String address3     = (String)addNewRolodexEntryForm.get("address3");
        String city         = (String)addNewRolodexEntryForm.get("city");
        String state        = (String)addNewRolodexEntryForm.get("state");
        String postalCode   = (String)addNewRolodexEntryForm.get("postalCode");
        String county       = (String)addNewRolodexEntryForm.get("county");
        String country      = (String)addNewRolodexEntryForm.get("country");
        
        if(country=="US" || country=="USA"){
            if(state == null || state.equals(EMPTY_STRING)){
                isSaveRequired = false;
                actionMessages.add("isNotValidState", new ActionMessage("addNewRolodexEntry.isNotValidState"));
                saveMessages(request, actionMessages);            
            }
        }

        String phone        = (String)addNewRolodexEntryForm.get("phone");
        String email        = (String)addNewRolodexEntryForm.get("email");
        String fax          = (String)addNewRolodexEntryForm.get("fax");
        String comments     = (String)addNewRolodexEntryForm.get("comments");
    
        /*Modified for the case #4160 -saving the Rolodex entry from Lite creates 2 entries in the Rolodex table -Start  */   
        //Hashtable htNextRolodexNumber = (Hashtable)webTxnBean.getResults(request, "getNewRolodexEntry", hmRequiredDetails);
        //String rolodexId    = (String)((HashMap)htNextRolodexNumber.get("getNextRolodexId")).get("nextRolodexId");
        
        Hashtable ht = new Hashtable();
        // To get next rolodex id
        ht = (Hashtable)webTxnBean.getResults(request, "getNextRolodexId", hmRequiredDetails);
        HashMap hm = (HashMap)ht.get("getNextRolodexId");
        String rolodexId = (String)hm.get("nextRolodexId");
        
        /*Modified for the case #4160 -saving the Rolodex entry from Lite creates 2 entries in the Rolodex table -End  */   
        dbTimestamp = prepareTimeStamp();

        dynaForm.set("updateTimestamp", dbTimestamp.toString());

        dynaForm.set("createUser", createUser);
        
        dynaForm.set("firstName"   ,firstName);
        dynaForm.set("lastName"    ,lastName);
        dynaForm.set("middleName"  ,middleName);
        dynaForm.set("suffix"      ,suffix);
        dynaForm.set("prefix"      ,prefix);
        dynaForm.set("title"       ,title);
        dynaForm.set("sponsor"     ,sponsor);
        dynaForm.set("organization",organization);
        dynaForm.set("address1"    ,address1);
        dynaForm.set("address2"    ,address2);
        dynaForm.set("address3"    ,address3);
        dynaForm.set("city"        ,city);
        dynaForm.set("state"       ,state);
        dynaForm.set("postalCode"  ,postalCode);
        dynaForm.set("county"      ,county);
        dynaForm.set("country"     ,country);
        dynaForm.set("phone"       ,phone);
        dynaForm.set("email"       ,email);
        dynaForm.set("fax"         ,fax);
        dynaForm.set("comments"    ,comments);
        dynaForm.set("ownedByUnit" ,ownedByUnit);
        dynaForm.set("sponsorAddressFlag", SPONSOR_ADDRESS_FLAG);
        dynaForm.set("rolodexId"   ,rolodexId);
        dynaForm.set("acType"      ,acType);

        // *********************************************************************
        // * If everything is OK then we want to insert the record.            *
        // *********************************************************************

        isSaveRequired=true;
        
        if(organization == null || organization.equals(EMPTY_STRING)){            
            isSaveRequired=false;
            actionMessages.add("organizationRequired", new ActionMessage("rolodexInfo.error.organization"));
            saveMessages(request, actionMessages);                        
        }        
        
        boolean isValidSponsor = true;
        if(sponsor != null && !sponsor.equals(EMPTY_STRING)){
            //Indicates sponsor code is present, so validate
            isValidSponsor = isValidSponsor(request, sponsor);
        }
        
        if(!isValidSponsor && canAddToRolodex){
            isSaveRequired=false;
            actionMessages.add("RolodexInformationBadSponsor", new ActionMessage("newRolodexEntryMessage.RolodexInformationBadSponsor"));
            saveMessages(request, actionMessages);
        }
        
        if(isSaveRequired){

            webTxnBean.getResults(request, "addUpdRolodex" , dynaForm);   
            
            request.setAttribute("dataSaved", "true");
            session.setAttribute("lastRolodexId", rolodexId);
            session.setAttribute("createTimestamp",dbTimestamp.toString());
            request.setAttribute("updateTimestamp",dbTimestamp.toString());
            String updUser =  userInfoBean.getUserId();
            String updUserFullName = getUserName(request , updUser);
            session.setAttribute("updUser" , updUserFullName);
/*
 *******************************************************************************
 * Clear the form fields after a successful insert so user can add multiple    *
 * records.                                                                    *
 *******************************************************************************
 */            
            if(canAddToRolodex){
                actionMessages.add("RolodexInformationAdded", new ActionMessage("newRolodexEntryMessage.RolodexInformationAdded"));
                saveMessages(request, actionMessages);

                actionMessages.add("AddRolodexInformationNextEntry", new ActionMessage("newRolodexEntryMessage.AddRolodexInformationNextEntry"));
                saveMessages(request, actionMessages);
            } else {
                session.setAttribute("maintainRolodex", "NO");
                actionMessages.add("AddRolodexInformationRight", new ActionMessage("newRolodexEntryMessage.AddRolodexInformationRight"));
                saveMessages(request, actionMessages);
            }

            dynaForm.set("firstName"   ,EMPTY_STRING);
            dynaForm.set("lastName"    ,EMPTY_STRING);
            dynaForm.set("middleName"  ,EMPTY_STRING);
            dynaForm.set("suffix"      ,EMPTY_STRING);
            dynaForm.set("prefix"      ,EMPTY_STRING);
            dynaForm.set("title"       ,EMPTY_STRING);
            dynaForm.set("sponsor"     ,EMPTY_STRING);
            dynaForm.set("organization",EMPTY_STRING);
            dynaForm.set("address1"    ,EMPTY_STRING);
            dynaForm.set("address2"    ,EMPTY_STRING);
            dynaForm.set("address3"    ,EMPTY_STRING);
            dynaForm.set("city"        ,EMPTY_STRING);
            dynaForm.set("state"       ,EMPTY_STRING);
            dynaForm.set("postalCode"  ,EMPTY_STRING);
            dynaForm.set("county"      ,EMPTY_STRING);
            dynaForm.set("country"     ,EMPTY_STRING);
            dynaForm.set("phone"       ,EMPTY_STRING);
            dynaForm.set("email"       ,EMPTY_STRING);
            dynaForm.set("fax"         ,EMPTY_STRING);
            dynaForm.set("comments"    ,EMPTY_STRING);
            dynaForm.set("ownedByUnit" ,ownedByUnit);
            dynaForm.set("sponsorAddressFlag", SPONSOR_ADDRESS_FLAG);
            dynaForm.set("rolodexId"   ,EMPTY_STRING);
            dynaForm.set("acType"      ,acType);
        }
    }
    
 
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
    
    //Commented and Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    /*private boolean isValidSponsor(HttpServletRequest request, String sponsorCode)throws Exception{
        boolean isValidSponsor = true;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap   hmSponsorCodeDetail = new HashMap();
        hmSponsorCodeDetail.put("sponsorCode" , sponsorCode);
        Hashtable htSponsorName = (Hashtable)webTxnBean.getResults(request, "getSponsorName", hmSponsorCodeDetail);
        String sponsorName = (String)((HashMap)htSponsorName.get("getSponsorName")).get("ls_name");
        if(sponsorName == null || sponsorName.equals("")){
            isValidSponsor = false;
        }else{
            sponsorName = sponsorName.trim();
        }
        return isValidSponsor;
    }*/
    
    /**
     * This method is to check whether the entered sponsor number is valid or not
     * @param sponsorCode
     * @throws Exception 
     * @return
     */
    private boolean isValidSponsor(HttpServletRequest request,String sponsorCode) throws Exception{
        HashMap hmSponsorCode = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmSponsorCode.put("sponsorCode" , sponsorCode);
        Hashtable htSponsorCode =
                (Hashtable)webTxnBean.getResults(request, "isValidSponsorCode" , hmSponsorCode );
        HashMap hmValidSponsor = (HashMap)htSponsorCode.get("isValidSponsorCode");
        String isValidSponsor = (String)hmValidSponsor.get("isValid");
        if(isValidSponsor!=null && sponsorCode.equals(isValidSponsor)){
            return true ;
        }else{
            return false ;
        }
    }
    //Commented and Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
}  