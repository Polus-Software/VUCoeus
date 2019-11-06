
package edu.mit.coeuslite.award.actions;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;


public abstract class AwardBaseAction extends CoeusBaseAction {

 
    private static final String AWARD_SUB_HEADER="awardSubHeader";
    private static final String XML_SUB_MENU_PATH="/edu/mit/coeuslite/award/xml/awardSubMenu.xml";
    private static final String XML_INFO_MENU_PATH="/edu/mit/coeuslite/award/xml/awardMenu.xml";
    private final String DELETE_ANY_PROPOSAL = "DELETE_ANY_PROPOSAL";
    private final String DELETE_PROPOSAL = "DELETE_PROPOSAL";
    private static final String AWARD_INFO_HEADER="awardInfoHeader";
  
    public AwardBaseAction() {
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
        String sponsorName = EMPTY_STRING;

        try {
            hmSponsorData.put("sponsorCode",sponsorCode);
            WebTxnBean webTxnBean = new WebTxnBean();
            DynaValidatorForm dynaSponsorForm = null ;

            Hashtable htSponsorData =
                (Hashtable)webTxnBean.getResults(request, "getSponsor", hmSponsorData);
            Vector vecSponsorData = (Vector)htSponsorData.get("getSponsor");
            if(vecSponsorData!=null && vecSponsorData.size() > 0){
               dynaSponsorForm = (DynaValidatorForm)vecSponsorData.get(0);
               sponsorName = (String)dynaSponsorForm.get("sponsorName");
        }
        } catch(Exception dbEx){
              UtilFactory.log("FROM COEUS LITE : "+dbEx.getMessage(),dbEx,"AwardBaseAction","getSponsorDetails");

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
        try {
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
                        if(statusCode.equals("1") || statusCode.equals("3")){
                            menuBean.setVisible(false);
                        }else{
                            if(avViewRouting!=null && !avViewRouting.equals(EMPTY_STRING)){
                                if(avViewRouting.equals("0")){
                                    menuBean.setVisible(false);
                                }else{
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
        }catch(Exception Ex){
              UtilFactory.log("FROM COEUS LITE : "+Ex.getMessage(),Ex,"AwardBaseAction","enableDisableMenus");

        }

    }

     /** Read the save status for the given proposal number
     *@throws Exception
     */
    protected void readSavedStatus(HttpServletRequest request) throws Exception{
        Map hmSavedData =null;
        Hashtable htReqData =null;
        HashMap hmMenuData = null;
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
                        htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedQuestionnaireData", hmMenuData);
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
              UtilFactory.log("FROM COEUS LITE : "+dbEx.getMessage(),dbEx,"AwardBaseAction","readSavedStatus");

        }
    }

     
    protected void getAwardMenus(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector proposalMenuItemsVector  = null;
        ReadXMLData readXMLData = new ReadXMLData();
        proposalMenuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        HashMap hmData = null ;
        Vector vecAwardSubMenuHeader = null;
        vecAwardSubMenuHeader = (Vector)session.getAttribute(AWARD_SUB_HEADER);
        if(vecAwardSubMenuHeader == null || vecAwardSubMenuHeader.size()==0){
            vecAwardSubMenuHeader = readXMLData.readXMLDataForSubHeader(XML_SUB_MENU_PATH);
            session.setAttribute(AWARD_SUB_HEADER,vecAwardSubMenuHeader);
        }
    }

     protected void getAwardInfoMenus(HttpServletRequest request) throws Exception{

         HttpSession session = request.getSession();
        Vector awardInfoItemsVector  = null;
        ReadXMLData readXMLData = new ReadXMLData();

        HashMap hmData = null ;

        awardInfoItemsVector = (Vector)session.getAttribute(AWARD_INFO_HEADER);
        if(awardInfoItemsVector == null || awardInfoItemsVector.size()==0){
            awardInfoItemsVector = readXMLData.readXMLDataForMenu(XML_INFO_MENU_PATH);
            session.setAttribute(AWARD_INFO_HEADER,awardInfoItemsVector);
        }
    }


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
        }catch(Exception dbEx){
              UtilFactory.log("FROM COEUS LITE : "+dbEx.getMessage(),dbEx,"AwardBaseAction","getS2SAttrMatch");
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
        }catch(Exception dbEx){
              UtilFactory.log("FROM COEUS LITE : "+dbEx.getMessage(),dbEx,"AwardBaseAction","getProposalHeader");
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
    throws Exception, CoeusException, org.okip.service.shared.api.Exception {
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
}


