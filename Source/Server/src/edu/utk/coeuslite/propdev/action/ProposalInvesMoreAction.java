/*
 * ProposalInvesMoreAction.java
 *
 * Created on November 21, 2006, 6:35 PM
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusDynaFormList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;


/**
 *
 * @author  mohann
 */
public class ProposalInvesMoreAction extends ProposalBaseAction {

    private static final String GET_MORE_DETAILS = "/getMoreDetails";
    private static final String REMOVE_UNIT_DETAILS = "/removeUnitDetails";
    private static final String ADD_UNIT_DETAILS = "/addUnitDetails";
    private static final String ADD_DEGREE_INFO = "/addDegreeInfo";
    private static final String REMOVE_DEGREE_INFO = "/removeDegreeInfo";
    private static final String SAVE_ALL_DETAILS = "/saveAllDetails";
    private static final String GET_PERSONS_CERTIFY = "/getPersonsCertify";
    private static final String SUCCESS = "success";
    private static final String UNIT_DETAILS_ATTRIBUTE = "unitDetailsData";
    private static final String PERSON_DYNA_BEANS_LIST = "personDynaBeansList";
    private static final String AC_TYPE = "acType";
    private static final String EMPTY_STRING = "";
    private static final String YES = "YES";
    private static final String PERSON_ID = "personId";
    private static final String PERSON_NAME = "personName";
    private static final String INV_PERSON_ID  = "InvPersonId";
    private static final String INV_PERSON_NAME  ="InvPersonName";
    private static final String CERTIFY_PERSON = "certifyPerson";
    private static final String PI_FLAG = "piFlag";
    private static final String INV_PI_FLAG = "InvPiFlag";
    private static final String REMOVED_UNITS_DETAILS = "removedUnitsDetails";
    private static final String MODE = "mode";
    private static final String PROPOSAL_NUMBER = "proposalNumber";
    private static final String PROP_INV_TIMESTAMP = "propInvTimestamp";
    private static final String AW_UPDATE_TIMESTAMP = "awUpdateTimestamp";
    private static final String UNIT_NUMBER = "unitNumber";
    private static final String AW_UNIT_NUMBER = "awUnitNumber";
    private static final String UPDATE_PROP_DEV_INV_UNITS = "updatePropdevInvUnits";
    private static final String UPDATE_PROP_DEV_KP_UNITS = "updatePropdevKPUnits";
    private static final String AW_ACTYPE = "awAcType";
    private static final String DEGREE_INDEX = "degreeIndex";
    private static final String REMOVE_DEGREE_DETAILS = "removedDegreeDetails";
    private static final String PROPOSAL_INV_MORE_FORM = "proposalInvMoreForm";
    private static final String UPDATE_TIMESTAMP = "updateTimestamp";
    private static final String GRADUATION_DATE = "graduationDate";
    private static final String UPD_PROP_PERSON_DEGREE_INFO = "updPropPersonDegreeInfo";
    private static final String STATE = "state";
    private static final String ERROR_MSG_ATLEAST_ONE_UNIT = "moreInvPerson.error.atleaseOneUnit";
    private static final String UPD_PROPOSAL_PERSON_DEGREE = "updProposalPersonDegree";
    private static final String UPDATE_PROPSAL_PERSON = "updateProposalPerson";
    private static final String MAINTAIN_PERSON_INFO_RIGHT = "MAINTAIN_PERSON_INFO";
    private static final String IS_USER_HAS_RIGHT = "isUserHasRight";
    private static final String GET_PROP_DEV_INVES_UNITS = "getPropdevInvesUnits";
    private static final String GET_USER_INFO_FOR_PERSONS = "getUserInfoForPersons";
    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
    private static final String DIVISION = "division";
    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
    
    /** Creates a new instance of ProposalInvesMoreAction */
    public ProposalInvesMoreAction() {
    }

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
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        // HttpSession session = request.getSession();
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        ActionForward actionForward = getProposalInvMoreData(actionMapping, request, coeusDynaFormList);
        return actionForward;
    }

    /** This method will identify which request is comes from which path and
     *  navigates to the respective ActionForward
     *  @returns ActionForward object
     */
    private ActionForward getProposalInvMoreData(ActionMapping actionMapping, HttpServletRequest request ,CoeusDynaFormList coeusDynaFormList)throws Exception{

        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(GET_MORE_DETAILS)){
            navigator = getProposalMoreDetails(coeusDynaFormList, request);
        }else if(actionMapping.getPath().equals(ADD_UNIT_DETAILS)){
            navigator =performAddUnitAction(coeusDynaFormList, request);
            request.setAttribute("dataModified", "modified");
        }else if(actionMapping.getPath().equals(REMOVE_UNIT_DETAILS)){
            navigator =performRemoveUnitAction(coeusDynaFormList, request);
            request.setAttribute("dataModified", "modified");
        }else if(actionMapping.getPath().equals(ADD_DEGREE_INFO)){
            navigator =performAddDegreeInfo(coeusDynaFormList, request);
            request.setAttribute("dataModified", "modified");
        }else if(actionMapping.getPath().equals(REMOVE_DEGREE_INFO)){
            navigator =performRemoveDegreeInfo(coeusDynaFormList, request);
            request.setAttribute("dataModified", "modified");
        }else if(actionMapping.getPath().equals(SAVE_ALL_DETAILS)){
            navigator =performSaveAllDetails(coeusDynaFormList, request);
        }else if(actionMapping.getPath().equals(GET_PERSONS_CERTIFY)){
            HttpSession session = request.getSession();
            String personId = request.getParameter(PERSON_ID);
            String personName = request.getParameter(PERSON_NAME);
            session.setAttribute(INV_PERSON_ID, personId);
            session.setAttribute(INV_PERSON_NAME, personName);
            navigator = CERTIFY_PERSON;
        }

        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    /**
     * This method is used for to get Person more Details
     * @param CoeusDynaFormList
     * @param  HttpServletRequest
     * @return navigator
     * @throws Exception
     */
    private String getProposalMoreDetails(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception {
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        String personId = request.getParameter(PERSON_ID);
        String personName = request.getParameter(PERSON_NAME);
        String piFlag = request.getParameter(PI_FLAG);
        if(personId ==null || personId.equals(EMPTY_STRING)){
            personId = (String) session.getAttribute(INV_PERSON_ID);
        }
        if(personName ==null || personName.equals(EMPTY_STRING)){
            personName = (String) session.getAttribute(INV_PERSON_NAME);
        }
        if(piFlag ==null || piFlag.equals(EMPTY_STRING)){
            piFlag = (String) session.getAttribute(INV_PI_FLAG);
        }

        session.removeAttribute(UNIT_DETAILS_ATTRIBUTE);
        session.removeAttribute(PERSON_DYNA_BEANS_LIST);
        session.removeAttribute(REMOVED_UNITS_DETAILS);

        session.setAttribute(INV_PERSON_ID, personId);
        session.setAttribute(INV_PERSON_NAME, personName);
        session.setAttribute(INV_PI_FLAG, piFlag);

        getInvesUnitDetails(personId, coeusDynaFormList, request);
        getProposalPersonsDetails(coeusDynaFormList, request);
        getPersonDegreeInfo(personId, coeusDynaFormList, request);
        getCountryStateCodes(request);
        setSelectedStateType(coeusDynaFormList, request);
        return navigator;
    }
    /**
     * This method is used for to save all the Unit , Persons and Degee Info details
     * @param CoeusDynaFormList
     * @param  HttpServletRequest
     * @return navigator
     * @throws Exception
     */
    private String performSaveAllDetails(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception {
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        boolean isValidUnit = validateAll(coeusDynaFormList , request);
        if(isValidUnit){
            String mode=(String)session.getAttribute(MODE+session.getId());
            if(mode==null || !mode.equalsIgnoreCase("display")) {
                // Check if lock exists or not
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                //                String proposalNumber = (String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
                LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(PROPOSAL_NUMBER+session.getId()), request);
                boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                    saveUnitDetailsData(coeusDynaFormList, request);
                    deleteUnitDetailsData(request);
                    savePersonDetailsData(coeusDynaFormList, request);
                    saveDegreeDetailsData(coeusDynaFormList, request);
                    deleteDegreeDetailsData(request);
                }else{
                    String errMsg = "release_lock_for";
                    ActionMessages messages = new ActionMessages();
                    messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                    saveMessages(request, messages);
                }

            }
            getProposalMoreDetails(coeusDynaFormList, request);
            setSelectedStateType(coeusDynaFormList, request);
        }
        return navigator;
    }

    /**
     * To update all the records to the database
     * @param CoeusDynaFormList
     * @param  HttpServletRequest
     * @throws Exception
     */
    public void saveUnitDetailsData(CoeusDynaFormList coeusDynaFormList,
    HttpServletRequest request)throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        String flag = session.getAttribute("InvPiFlag").toString();
        List lstUnitsData = coeusDynaFormList.getList();
        if(lstUnitsData!=null && lstUnitsData.size()>0) {
            for(int index=0; index<lstUnitsData.size(); index++) {
                DynaActionForm dynaForm = (DynaActionForm) lstUnitsData.get(index);
                dynaForm.set(AW_UPDATE_TIMESTAMP,dynaForm.get(PROP_INV_TIMESTAMP));
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaForm.set(PROP_INV_TIMESTAMP,dbTimestamp.toString());
               // dynaForm.set(AW_UNIT_NUMBER,dynaForm.get(UNIT_NUMBER));
                //If the acType is null or Empty string then it is to be update
                if(dynaForm.get(AC_TYPE)==null || dynaForm.get(AC_TYPE).equals(EMPTY_STRING)) {
                    dynaForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                }
                if (flag.compareTo("KSP")==0)
                {
                       webTxnBean.getResults(request, UPDATE_PROP_DEV_KP_UNITS, dynaForm);
                }
                else
                {
                    webTxnBean.getResults(request, UPDATE_PROP_DEV_INV_UNITS, dynaForm);
                }


            }
        }
    }
    /**
     * To delete all the removed items from database
     * @param  HttpServletRequest
     * @throws Exception
     */
    private void deleteUnitDetailsData(HttpServletRequest request)throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        HttpSession session_new = request.getSession();
        String flag = session_new.getAttribute("InvPiFlag").toString();

        // delete the data from removed Vector
        Vector vecUnitDetails = (Vector) session.getAttribute(REMOVED_UNITS_DETAILS);
        if(vecUnitDetails !=null && vecUnitDetails.size()>0){
            for(int index=0; index < vecUnitDetails.size(); index++){
                DynaActionForm dynaActionForm = (DynaActionForm) vecUnitDetails.get(index);
                dynaActionForm.set(AW_UPDATE_TIMESTAMP,dynaActionForm.get(PROP_INV_TIMESTAMP));
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaActionForm.set(PROP_INV_TIMESTAMP,dbTimestamp.toString());
                dynaActionForm.set(AW_UNIT_NUMBER,dynaActionForm.get(UNIT_NUMBER));
                if(dynaActionForm.get(AW_ACTYPE)!=null && dynaActionForm.get(AW_ACTYPE).equals(TypeConstants.DELETE_RECORD)) {
                    //If the acType is null or Empty string then the record to be deleted from the database.
                    if(dynaActionForm.get(AC_TYPE)==null || dynaActionForm.get(AC_TYPE).equals(EMPTY_STRING)) {
                        dynaActionForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                         if (flag.compareTo("KSP")==0){
                            webTxnBean.getResults(request, UPDATE_PROP_DEV_KP_UNITS, dynaActionForm);
                         }else
                         {
                            webTxnBean.getResults(request, UPDATE_PROP_DEV_INV_UNITS, dynaActionForm);
                         }
                    }
                    vecUnitDetails.remove(index--);
                }

            }
        }
    }

    /**
     * Method to Add the new  Unit Name and Number
     * @param coeusDynaFormList
     * @param HttpServletRequest
     * @return navigator
     * @throws Exception if exception occur
     */
    private String performAddUnitAction(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{

        List arLstUnitData = coeusDynaFormList.getList();
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
        DynaActionForm dynaFormData = coeusDynaFormList.getDynaForm(request,PROPOSAL_INV_MORE_FORM);
        DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        dynaNewBean.set(PROPOSAL_NUMBER,proposalNumber);
        dynaNewBean.set(PERSON_ID,session.getAttribute(INV_PERSON_ID).toString());
        dynaNewBean.set(AC_TYPE, TypeConstants.INSERT_RECORD);
        Timestamp prepareTimestamp = prepareTimeStamp();
        dynaNewBean.set(PROP_INV_TIMESTAMP,prepareTimestamp.toString());

        if(arLstUnitData == null) {
            arLstUnitData = new ArrayList();
        }
        arLstUnitData.add(dynaNewBean);
        session.setAttribute(UNIT_DETAILS_ATTRIBUTE, arLstUnitData);
        coeusDynaFormList.setList(arLstUnitData);
        request.getSession().setAttribute(PERSON_DYNA_BEANS_LIST ,coeusDynaFormList);
        arLstUnitData = null;
        prepareTimestamp = null;
        navigator = SUCCESS;
        return navigator;
    }

    /**
     * This method is used to remove the Unit Info details
     * @param coeusDynaFormList
     * @param HttpServletRequest
     * @return navigator
     * @throws Exception if exception occur
     */
    private String performRemoveUnitAction(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        ActionMessages actionMessages = new ActionMessages();
        List arLstUnitData = coeusDynaFormList.getList();
        String unitIndex = request.getParameter("unitIndex");
        boolean valid = true;
        Vector vecUnitDetails = (Vector) session.getAttribute(REMOVED_UNITS_DETAILS);
        if (vecUnitDetails == null){
            vecUnitDetails= new Vector();
        }
        String piFlag = (String) session.getAttribute(INV_PI_FLAG);
        if( piFlag !=null && (piFlag.equals("N")|| piFlag.equals("Y")) && arLstUnitData.size() ==1  ){
            actionMessages.add(ERROR_MSG_ATLEAST_ONE_UNIT,
            new ActionMessage(ERROR_MSG_ATLEAST_ONE_UNIT));
            saveMessages(request, actionMessages);
            valid = false;
        }
        if(arLstUnitData !=null && arLstUnitData.size() > 1 && unitIndex !=null && !unitIndex.equals(EMPTY_STRING)){
            if(Integer.parseInt(unitIndex) <= arLstUnitData.size()){
                DynaActionForm dynaActionForm = (DynaActionForm) arLstUnitData.get(Integer.parseInt(unitIndex));
                dynaActionForm.set(AW_ACTYPE,TypeConstants.DELETE_RECORD);
                vecUnitDetails.addElement(dynaActionForm);
                arLstUnitData.remove(Integer.parseInt(unitIndex));
            }
        }else if(arLstUnitData.size() == 1 && Integer.parseInt(unitIndex) == 0 && piFlag.equals("KSP")){  //for remove Unit - Key Study Person  start
           if(Integer.parseInt(unitIndex) <= arLstUnitData.size()){
                DynaActionForm dynaActionForm = (DynaActionForm) arLstUnitData.get(Integer.parseInt(unitIndex));
                dynaActionForm.set(AW_ACTYPE,TypeConstants.DELETE_RECORD);
                vecUnitDetails.addElement(dynaActionForm);
                arLstUnitData.remove(Integer.parseInt(unitIndex));
           }
        }                                                                                                //for remove Unit - Key Study Person  end
        session.setAttribute(UNIT_DETAILS_ATTRIBUTE, arLstUnitData);
        coeusDynaFormList.setList(arLstUnitData);
        session.setAttribute(REMOVED_UNITS_DETAILS, vecUnitDetails);
        request.getSession().setAttribute(PERSON_DYNA_BEANS_LIST ,coeusDynaFormList);
        navigator = SUCCESS;
        return navigator;
    }
    /**
     * This method is used to remove the Degree Info details
     * @param coeusDynaFormList
     * @param HttpServletRequest
     * @return navigator
     * @throws Exception if exception occur
     */
    private String performRemoveDegreeInfo(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        List arLstDegreeData = coeusDynaFormList.getBeanList();
        String degreeIndex = request.getParameter(DEGREE_INDEX);
        Vector vecDegeeDetails = (Vector) session.getAttribute(REMOVE_DEGREE_DETAILS);
        if (vecDegeeDetails == null){
            vecDegeeDetails= new Vector();
        }
        if(arLstDegreeData !=null && arLstDegreeData.size() >0 && degreeIndex !=null && !degreeIndex.equals(EMPTY_STRING)){
            if(Integer.parseInt(degreeIndex) <= arLstDegreeData.size()){
                DynaActionForm dynaActionForm = (DynaActionForm) arLstDegreeData.get(Integer.parseInt(degreeIndex));
                dynaActionForm.set(AW_ACTYPE,TypeConstants.DELETE_RECORD);
                vecDegeeDetails.addElement(dynaActionForm);
                arLstDegreeData.remove(Integer.parseInt(degreeIndex));
            }
        }
        session.setAttribute(UNIT_DETAILS_ATTRIBUTE, arLstDegreeData);
        coeusDynaFormList.setBeanList(arLstDegreeData);
        session.setAttribute(REMOVE_DEGREE_DETAILS, vecDegeeDetails);
        request.getSession().setAttribute(PERSON_DYNA_BEANS_LIST ,coeusDynaFormList);
        navigator = SUCCESS;
        return navigator;
    }

    /**
     * To update all the Degree details to the database
     * @param coeusDynaFormList
     * @param HttpServletRequest
     * @return navigator
     * @throws Exception if exception occur
     */
    public void saveDegreeDetailsData(CoeusDynaFormList coeusDynaFormList,
    HttpServletRequest request)throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        List lstDegreesData = coeusDynaFormList.getBeanList();
        if(lstDegreesData!=null && lstDegreesData.size()>0) {
            for(int index=0; index<lstDegreesData.size(); index++) {
                DynaActionForm dynaForm = (DynaActionForm) lstDegreesData.get(index);
                dynaForm.set(AW_UPDATE_TIMESTAMP,dynaForm.get(UPDATE_TIMESTAMP));
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
                String gradDate = (String) dynaForm.get(GRADUATION_DATE);
                String graduationDate = "01-01-"+gradDate;
                DateUtils dtUtils = new DateUtils();
                graduationDate = dtUtils.formatDate(graduationDate,":/.,|-","MM/dd/yyyy");
                dynaForm.set(GRADUATION_DATE,graduationDate);
                //If the acType is null or Empty string then it is to be update
                if(dynaForm.get(AC_TYPE)==null || dynaForm.get(AC_TYPE).equals(EMPTY_STRING)) {
                    dynaForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                }
                webTxnBean.getResults(request, UPD_PROP_PERSON_DEGREE_INFO, dynaForm);
            }

        }
    }

     /**
     * To delete all the removed Degree details from the database
     * @param HttpServletRequest
     * @throws Exception if exception occur
     */
    private void deleteDegreeDetailsData(HttpServletRequest request)throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        // delete the data from removed Vector
        Vector vecDegeeDetails = (Vector) session.getAttribute(REMOVE_DEGREE_DETAILS);
        if(vecDegeeDetails !=null && vecDegeeDetails.size()>0){
            for(int index=0; index < vecDegeeDetails.size(); index++){
                DynaActionForm dynaActionForm = (DynaActionForm) vecDegeeDetails.get(index);
                dynaActionForm.set(AW_UPDATE_TIMESTAMP,dynaActionForm.get(UPDATE_TIMESTAMP));
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaActionForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
                if(dynaActionForm.get(AW_ACTYPE)!=null && dynaActionForm.get(AW_ACTYPE).equals(TypeConstants.DELETE_RECORD)) {
                    //If the acType is null or Empty string then the record to be deleted from the database.
                    if(dynaActionForm.get(AC_TYPE)==null || dynaActionForm.get(AC_TYPE).equals(EMPTY_STRING)) {
                        dynaActionForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                        webTxnBean.getResults(request, UPD_PROPOSAL_PERSON_DEGREE, dynaActionForm);
                    }
                    vecDegeeDetails.remove(index--);
                }
            }
        }
    }


    /**
     * This method is used to save all the person details info
     * @param CoeusDynaFormList
     * @param HttpServletRequest
     * @throws Exception
     */
    private void savePersonDetailsData(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        List lstPersonsData = coeusDynaFormList.getInfoList();
        if(lstPersonsData!=null && lstPersonsData.size()>0) {
            for(int index=0; index<lstPersonsData.size(); index++) {
                DynaActionForm dynaForm = (DynaActionForm) lstPersonsData.get(index);
                String state = request.getParameter(STATE);
                dynaForm.set(AW_UPDATE_TIMESTAMP,dynaForm.get(UPDATE_TIMESTAMP));
                Timestamp dbTimestamp = prepareTimeStamp();
                String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
                dynaForm.set(STATE,state);
                dynaForm.set(PROPOSAL_NUMBER,proposalNumber);
                dynaForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
                dynaForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
                if(EMPTY_STRING.equals(dynaForm.get(DIVISION))){
                    dynaForm.set(DIVISION," ");
                }
                //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
                webTxnBean.getResults(request, UPDATE_PROPSAL_PERSON, dynaForm);
            }
        }
    }

    /**
     *This method is used for to get Proposal Person more Details
     * @param CoeusDynaFormList
     * @param  HttpServletRequest
     * @return navigator
     * @throws Exception
     */

    private String getProposalPersonsDetails(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception {
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        String homeUnit = EMPTY_STRING;
        String personId = request.getParameter(PERSON_ID);
        boolean isUserCanMaintainUnitRight = false;
        boolean isUserCanMaintainPersonRight = false;
        if(personId !=null && !personId.equals(EMPTY_STRING)){
            homeUnit = getHomeUnit(request, personId);
            if(homeUnit != null){
                UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
                String userId = (String)userInfoBean.getUserId().toUpperCase();
                isUserCanMaintainUnitRight = isUserHasRight(request, homeUnit, userId);
            }

            Vector vecUserInfoData =  getUserInfoForPerson(personId,request);
            if(vecUserInfoData !=null && vecUserInfoData.size() >0){
                String id = (String) vecUserInfoData.elementAt(0);
                String userUnitNumber = (String)  vecUserInfoData.elementAt(1);
                if(userUnitNumber != null){
                    isUserCanMaintainPersonRight = isUserHasRight(request, userUnitNumber, id);
                }
            }
            getProposalPersonData(personId ,coeusDynaFormList,request);
            getPersonEditableColumns(request);
        }//end of personId if
        session.setAttribute("PAGE_MODE", "Person Details");
        return navigator;
    }


    /**
     *This method is used for to get home unit Details
     * @param personId
     * @param  HttpServletRequest
     * @return navigator
     * @throws Exception
     */
    private String getHomeUnit(HttpServletRequest request, String personId) throws Exception {
        String homeUnit = EMPTY_STRING;
        if(personId !=null && !personId.equals(EMPTY_STRING)){
            DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
            homeUnit = departmentPersonTxnBean.getHomeUnit(personId);
        }//end of personId
        return homeUnit;
    }

    /**
     *This method is used to check the Maintain Person Info Rights
     * @param personId
     * @param UserId
     * @param  HttpServletRequest
     * @return navigator
     * @throws Exception
     */
    private boolean isUserHasRight(HttpServletRequest request, String homeUnit, String userId )throws Exception{
        boolean canMaintain = false;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put("userId",userId);
        hmData.put(UNIT_NUMBER,homeUnit);
        hmData.put("rightId",MAINTAIN_PERSON_INFO_RIGHT);
        Hashtable htMaintainPerson =
        (Hashtable)webTxnBean.getResults(request, IS_USER_HAS_RIGHT, hmData);
        HashMap hmMaintainPerson = (HashMap)htMaintainPerson.get(IS_USER_HAS_RIGHT);
        if(hmMaintainPerson !=null && hmMaintainPerson.size()>0){
            int canView = Integer.parseInt(hmMaintainPerson.get("retValue").toString());
            if(canView == 1){
                canMaintain = true ;
            }
        }
        return canMaintain;
    }

     /*This method gets the Proposal Investigator Units from get_prop_investigator_units
      * @param proposalNumber
      * @param personId
      * @throws Exception
      * @return Vector vecQuestDetails
      */
    private void getInvesUnitDetails(String personId, CoeusDynaFormList coeusDynaFormList, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
        hmData.put(PROPOSAL_NUMBER, proposalNumber);
        hmData.put(PERSON_ID, personId);
        Hashtable htUnitDetails =
        (Hashtable)webTxnBean.getResults(request, GET_PROP_DEV_INVES_UNITS, hmData);
        List lstUnitDetails = (Vector)htUnitDetails.get(GET_PROP_DEV_INVES_UNITS);
         if(lstUnitDetails !=null && lstUnitDetails.size() >0 ){
            for(int index=0 ; index < lstUnitDetails.size(); index++){
                DynaActionForm dynaActionForm = (DynaActionForm) lstUnitDetails.get(index);
                String unitNo = (String) dynaActionForm.get(UNIT_NUMBER);
                dynaActionForm.set(AW_UNIT_NUMBER,dynaActionForm.get(UNIT_NUMBER));
            }
         }
            coeusDynaFormList.setList(lstUnitDetails);

          /**
           * code for get unit number in the detail page for keyperson
           */
         Hashtable hkUnitDetails = (Hashtable)webTxnBean.getResults(request, "getPropdevKeypersnUnits", hmData);
        List kplstUnitDetails = (Vector)hkUnitDetails.get("getPropdevKeypersnUnits");
         if(kplstUnitDetails !=null && kplstUnitDetails.size() >0 ){
            for(int index=0 ; index < kplstUnitDetails.size(); index++){
                DynaActionForm dynaActionForm = (DynaActionForm) kplstUnitDetails.get(index);
                String kpunitNo = (String) dynaActionForm.get(UNIT_NUMBER);
                dynaActionForm.set(AW_UNIT_NUMBER,dynaActionForm.get(UNIT_NUMBER));
            }

            coeusDynaFormList.setList(kplstUnitDetails);
         }


        session.setAttribute(PERSON_DYNA_BEANS_LIST , coeusDynaFormList);
    }

    /*This method gets the user details for particular person
     * @param proposalNumber
     * @param personId
     * @throws Exception
     * @return Vector vecQuestDetails
     */
    private Vector getUserInfoForPerson(String personId , HttpServletRequest request)throws Exception{
        Vector vecUserInfo = new Vector();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put(PERSON_ID, personId);
        Hashtable htUserInfoDetails =
        (Hashtable)webTxnBean.getResults(request, GET_USER_INFO_FOR_PERSONS, hmData);
        HashMap hmUserInfoDetails = (HashMap) htUserInfoDetails.get(GET_USER_INFO_FOR_PERSONS);
        if(hmUserInfoDetails !=null && hmUserInfoDetails.size()>0){
            vecUserInfo.addElement(hmUserInfoDetails.get("as_user_id"));
            vecUserInfo.addElement(hmUserInfoDetails.get("as_unit_num"));
        }
        return vecUserInfo;
    }

    /**
     * This method is used to person details using proposal number and person id
     * @param personId
     * @param CoeusDynaFormList
     * @throws Exception
     */
    private void getProposalPersonData(String personId, CoeusDynaFormList coeusDynaFormList, HttpServletRequest request)throws Exception {

        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
        hmData.put(PROPOSAL_NUMBER, proposalNumber);
        hmData.put(PERSON_ID, personId);
        Hashtable htUnitDetails =
        (Hashtable)webTxnBean.getResults(request, "getPropPersonForOne", hmData);
        List vecPersonDetails = (Vector)htUnitDetails.get("getPropPersonForOne");
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
        if(vecPersonDetails!=null){
            DynaActionForm dynaForm = (DynaActionForm)vecPersonDetails.get(0);
            String divisionValue = (String) dynaForm.get("division");
            if(divisionValue == null){
                S2STxnBean s2STxnBean = new S2STxnBean();
                if(dynaForm.get("homeUnit")!=null){
                    divisionValue = s2STxnBean.fn_get_division(dynaForm.get("homeUnit").toString());
                    dynaForm.set("division",divisionValue);
                }
            }
        }
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
        coeusDynaFormList.setInfoList(vecPersonDetails);
        session.setAttribute(PERSON_DYNA_BEANS_LIST , coeusDynaFormList);
    }

    /**
     * This method is used to get all the Editable columns list
     * @param personId
     * @param CoeusDynaFormList
     * @throws Exception
     */

    private void getPersonEditableColumns(HttpServletRequest request)throws Exception {

        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htUnitDetails =
        (Hashtable)webTxnBean.getResults(request, "getPersonEditableColumns", null);
        Vector vecUnitDetails = (Vector)htUnitDetails.get("getPersonEditableColumns");
        HashMap hmEditColumns = new HashMap();
        if(vecUnitDetails !=null && vecUnitDetails.size()>0){
            for(int index=0; index < vecUnitDetails.size() ; index++){
                DynaActionForm dynaForm = (DynaActionForm)vecUnitDetails.get(index);
                String columnName =(String) dynaForm.get("columnName");
                if(columnName !=null && columnName.equals("FULL_NAME")){
                    hmEditColumns.put("fullName", YES);
                }else if(columnName !=null && columnName.equals("USER_NAME")){
                    hmEditColumns.put("userName", YES);
                }else if(columnName !=null && columnName.equals("EMAIL_ADDRESS")){
                    hmEditColumns.put("emailAddress", YES);
                }else if(columnName !=null && columnName.equals("OFFICE_PHONE")){
                    hmEditColumns.put("officePhone", YES);
                }else if(columnName !=null && columnName.equals("PRIMARY_TITLE")){
                    hmEditColumns.put("primaryTitle", YES);
                }else if(columnName !=null && columnName.equals("DIRECTORY_TITLE")){
                    hmEditColumns.put("directoryTitle", YES);
                }else if(columnName !=null && columnName.equals("HOME_UNIT")){
                    hmEditColumns.put("homeUnit", YES);
                }else if(columnName !=null && columnName.equals("SCHOOL")){
                    hmEditColumns.put("school", YES);
                }else if(columnName !=null && columnName.equals("ERA_COMMONS_USER_NAME")){
                    hmEditColumns.put("eraCommonsUserName", YES);
                }else if(columnName !=null && columnName.equals("FAX_NUMBER")){
                    hmEditColumns.put("faxNumber", YES);
                }else if(columnName !=null && columnName.equals("PAGER_NUMBER")){
                    hmEditColumns.put("pagerNumber", YES);
                }else if(columnName !=null && columnName.equals("MOBILE_PHONE_NUMBER")){
                    hmEditColumns.put("mobilePhoneNumber", YES);
                }else if(columnName !=null && columnName.equals("OFFICE_LOCATION")){
                    hmEditColumns.put("officeLocation", YES);
                }else if(columnName !=null && columnName.equals("SECONDRY_OFFICE_LOCATION")){
                    hmEditColumns.put("secondaryOfficeLocation", YES);
                }else if(columnName !=null && columnName.equals("ADDRESS_LINE_1")){
                    hmEditColumns.put("addressLine1", YES);
                }else if(columnName !=null && columnName.equals("ADDRESS_LINE_2")){
                    hmEditColumns.put("addressLine2", YES);
                }else if(columnName !=null && columnName.equals("ADDRESS_LINE_3")){
                    hmEditColumns.put("addressLine3", YES);
                }else if(columnName !=null && columnName.equals("CITY")){
                    hmEditColumns.put("city", YES);
                }else if(columnName !=null && columnName.equals("COUNTY")){
                    hmEditColumns.put("county", YES);
                }else if(columnName !=null && columnName.equals("STATE")){
                    hmEditColumns.put("state", YES);
                }else if(columnName !=null && columnName.equals("POSTAL_CODE")){
                    hmEditColumns.put("postalCode", YES);
                }else if(columnName !=null && columnName.equals("COUNTRY_CODE")){
                    hmEditColumns.put("country", YES);
                }
                //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
                else if(columnName !=null && columnName.equals("DIVISION")){
                    hmEditColumns.put("division", YES);
                }
                //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
            }
        }
        session.setAttribute("personEditableColumns" , hmEditColumns);
    }

    /**
     * This method is used to Degree Info using proposal number and person id
     * @param personId
     * @param CoeusDynaFormList
     * @throws Exception
     */
    private void getPersonDegreeInfo(String personId, CoeusDynaFormList coeusDynaFormList, HttpServletRequest request)throws Exception {

        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
        hmData.put(PROPOSAL_NUMBER, proposalNumber);
        hmData.put(PERSON_ID, personId);
        Hashtable htDegreeDetails =
        (Hashtable)webTxnBean.getResults(request, "getProposalDegreeForOnePS", hmData);
        List lstDegreeDetails = (Vector)htDegreeDetails.get("getProposalDegreeForOnePS");
        Hashtable htDegreeTypes =
        (Hashtable)webTxnBean.getResults(request, "getDegreeTypes", hmData);
        Vector vecDegreeDetails = (Vector)htDegreeTypes.get("getDegreeTypes");
        coeusDynaFormList.setBeanList(lstDegreeDetails);
        session.setAttribute(PERSON_DYNA_BEANS_LIST , coeusDynaFormList);
        session.setAttribute("degreeTypes",vecDegreeDetails);
    }

    /**
     * Method to Add the new  Degree Details
     * @return String
     * @param dynaForm instance of DynaValidator form
     * @throws Exception if exception occur
     */
    private String performAddDegreeInfo(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception{

        List arLstDegreeData = coeusDynaFormList.getBeanList();
        if(arLstDegreeData ==null){
            arLstDegreeData = new ArrayList();
        }
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
        DynaActionForm dynaFormData = coeusDynaFormList.getDynaForm(request,PROPOSAL_INV_MORE_FORM);
        DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        dynaNewBean.set(PROPOSAL_NUMBER,proposalNumber);
        dynaNewBean.set(PERSON_ID,session.getAttribute(INV_PERSON_ID).toString());
        dynaNewBean.set(AC_TYPE, TypeConstants.INSERT_RECORD);
        Timestamp prepareTimestamp = prepareTimeStamp();
        dynaNewBean.set(UPDATE_TIMESTAMP,prepareTimestamp.toString());
        dynaNewBean.set(GRADUATION_DATE,"0000");
        arLstDegreeData.add(dynaNewBean);
        session.setAttribute(UNIT_DETAILS_ATTRIBUTE, arLstDegreeData);
        coeusDynaFormList.setBeanList(arLstDegreeData);
        request.getSession().setAttribute(PERSON_DYNA_BEANS_LIST ,coeusDynaFormList);
        arLstDegreeData = null;
        prepareTimestamp = null;
        navigator = SUCCESS;
        return navigator;
    }

    /**
     * This method to validate the unitNumber and Name
     * @param CoeusDynaFormList
     * @param request
     * @throws Exception if exception occur
     */
    private boolean validateAll(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) throws Exception {
        Map hmNumber  = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        ActionMessages actionMessages = new ActionMessages();
        String count;
        boolean valid = true;
        List arLstUnitData = coeusDynaFormList.getList();
        String piFlag = (String) session.getAttribute(INV_PI_FLAG);
        if( piFlag !=null && (piFlag.equals("N")|| piFlag.equals("Y")) && arLstUnitData.isEmpty() ){
            actionMessages.add(ERROR_MSG_ATLEAST_ONE_UNIT,
            new ActionMessage(ERROR_MSG_ATLEAST_ONE_UNIT));
            saveMessages(request, actionMessages);
            valid = false;
        }
        if(arLstUnitData !=null && arLstUnitData.size() >0 ){
            for(int index=0 ; index < arLstUnitData.size(); index++){
                DynaActionForm dynaActionForm = (DynaActionForm) arLstUnitData.get(index);
                String unitNo = (String) dynaActionForm.get(UNIT_NUMBER);
                if(unitNo !=null &&  !unitNo.equals(EMPTY_STRING)){
                    hmNumber.put(UNIT_NUMBER, unitNo.trim());
                    hmNumber =(Hashtable)webTxnBean.getResults(request,"getUnitDescription",hmNumber);
                    hmNumber = (HashMap)hmNumber.get("getUnitDescription");
                    count = (String)hmNumber.get("RetVal");
                    if(count == null){
                        actionMessages.add("moreInvPerson.error.invalidUnitNo",
                        new ActionMessage("moreInvPerson.error.invalidUnitNo", unitNo));
                        saveMessages(request, actionMessages);
                        valid = false;
                        break;
                    }
                }else {
                    actionMessages.add("moreInvPerson.error.unitRequired",
                    new ActionMessage("moreInvPerson.error.unitRequired", unitNo));
                    saveMessages(request, actionMessages);
                    valid = false;
                    break;
                }
                //check for duplicate unit no
                int unitCount =0;
                for(int index1=0 ; index1 < arLstUnitData.size(); index1++){
                    DynaActionForm dynaUnitForm = (DynaActionForm) arLstUnitData.get(index1);
                    String unitNum = (String) dynaUnitForm.get(UNIT_NUMBER);
                    if(unitNo !=null && unitNum !=null && unitNo.equals(unitNum) ){
                        unitCount++;
                    }
                }
                if(unitCount > 1){
                    actionMessages.add("moreInvPerson.error.unitDuplicate",
                    new ActionMessage("moreInvPerson.error.unitDuplicate", unitNo));
                    saveMessages(request, actionMessages);
                    valid = false;
                    break;
                }
            }
        }
        List lstDegreeDetails = coeusDynaFormList.getBeanList();
        if(lstDegreeDetails !=null && lstDegreeDetails.size() >0 ){
            for(int indexDeg=0 ; indexDeg < lstDegreeDetails.size(); indexDeg++){
                DynaActionForm dynaForm = (DynaActionForm) lstDegreeDetails.get(indexDeg);
                String degreeCode =(String) dynaForm.get("degreeCode");
                String degree = (String) dynaForm.get("degree");
                String gradDate = (String)dynaForm.get(GRADUATION_DATE);
                if(degreeCode ==null || degreeCode.equals(EMPTY_STRING)){
                    actionMessages.add("moreInvPerson.error.degreeCodeRequired",
                    new ActionMessage("moreInvPerson.error.degreeCodeRequired"));
                    saveMessages(request, actionMessages);
                    valid = false;
                    break;
                }
                if(degree ==null || degree.equals(EMPTY_STRING)){
                    actionMessages.add("moreInvPerson.error.degreeRequired",
                    new ActionMessage("moreInvPerson.error.degreeRequired"));
                    saveMessages(request, actionMessages);
                    valid = false;
                    break;
                }
                if(gradDate == null || gradDate.equals(EMPTY_STRING)){
                    actionMessages.add("moreInvPerson.error.gradDateRequired",
                    new ActionMessage("moreInvPerson.error.gradDateRequired"));
                    saveMessages(request, actionMessages);
                    valid = false;
                    break;
                }
                if(gradDate != null && !gradDate.equals(EMPTY_STRING)){
                    try{
                        int gradYear = Integer.parseInt(gradDate);
                    }catch(NumberFormatException e){
                        actionMessages.add("moreInvPerson.error.invalidGradDate",
                        new ActionMessage("moreInvPerson.error.invalidGradDate",gradDate));
                        saveMessages(request, actionMessages);
                        valid = false;
                        break;
                    }
                }

                int degreeCount =0;
                for(int index1=0 ; index1 < lstDegreeDetails.size(); index1++){
                    DynaActionForm dynaDegForm = (DynaActionForm) lstDegreeDetails.get(index1);
                    String strDegreeCode =(String) dynaDegForm.get("degreeCode");
                    String strDegree = (String) dynaDegForm.get("degree");
                    String strGradDate = (String)dynaDegForm.get(GRADUATION_DATE);
                    if(strDegreeCode !=null && strDegree !=null && strGradDate !=null && degreeCode.equals(strDegreeCode) &&
                    degree.equals(strDegree)  &&  gradDate.equals(strGradDate)  ){
                        degreeCount++;
                    }
                }
                if(degreeCount > 1){
                    actionMessages.add("moreInvPerson.error.degreeDuplicate",
                    new ActionMessage("moreInvPerson.error.degreeDuplicate"));
                    saveMessages(request, actionMessages);
                    valid = false;
                    break;
                }
            }
        }

        return valid;
    }
    /**
     * To get the Country and State codes
     * @param request
     * @throws Exception
     */
    private void getCountryStateCodes(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCountryDetails =
        (Hashtable)webTxnBean.getResults(request, "getCountryCodes", null);
        Vector vecCouDetails = (Vector)htCountryDetails.get("getCountryCodes");
        Hashtable htStateDetails =
        (Hashtable)webTxnBean.getResults(request, "getStateCodes", null);
        Vector vecCountryDetails = (Vector)htStateDetails.get("getStateCodes");
        HashMap hmCombinedStateCodesTypes = new HashMap();
        if(vecCountryDetails !=null && vecCountryDetails.size()>0){
            for(int index=0; index<vecCountryDetails.size();index++){
                DynaActionForm dynaActionForm = (DynaActionForm)vecCountryDetails.get(index);
                String countryTypeCode = (String)dynaActionForm.get("countryCode");
                // 4039: Editing the person details in Coeus Lite and the State box is not sorted -Start
                // HashMap hmStateTypeLevels = new HashMap();
                LinkedHashMap hmStateTypeLevels = new LinkedHashMap();
                // 4039: Editing the person details in Coeus Lite and the State box is not sorted - End
                for(int revIndex=0; revIndex < vecCountryDetails.size(); revIndex++){
                    DynaActionForm dynaForm = (DynaActionForm)vecCountryDetails.get(revIndex);
                    String subCountryCode = (String)dynaForm.get("countryCode");
                    String stateTypeCode = (String)dynaForm.get("stateCode");
                    String stateTypeDesc = (String)dynaForm.get("description");
                    if(countryTypeCode.equals(subCountryCode)){
                        hmStateTypeLevels.put(stateTypeCode, stateTypeDesc);
                        hmCombinedStateCodesTypes.put(countryTypeCode,hmStateTypeLevels);
                    }
                }
            }
            session.setAttribute("validCountryStateCodes", hmCombinedStateCodesTypes);
            session.setAttribute("getCountryTypes", vecCouDetails);
        }
    }

    /**
     * This method is used to set the State Type list based on country type
     * @param CoeusDynaFormList
     * @throws Exception
     */
    private void setSelectedStateType(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        String state = request.getParameter("state");
        String countryCode = EMPTY_STRING;
        String stateCode = EMPTY_STRING;
        List lstPersonsData = coeusDynaFormList.getInfoList();
        if(lstPersonsData!=null && lstPersonsData.size()>0) {
            DynaActionForm dynaForm = (DynaActionForm) lstPersonsData.get(0);
            countryCode = (String) dynaForm.get("country");
            stateCode = (String) dynaForm.get(STATE);
        }
        String country = request.getParameter("dynaFormInfo[0].country");
        String stateTypeCode = EMPTY_STRING;
        if(state ==null){
            state = stateCode;
        }
        if(country ==null){
            country = countryCode;
        }
        List lstReviewType = (List) coeusDynaFormList.getInfoList();
        if(lstReviewType !=null && lstReviewType.size()>0){
            // based of Review Type Description take the Review Type Code and set it the form.
            DynaActionForm dynaForm = (DynaActionForm)lstReviewType.get(0);
            HashMap hmCombinedReviewTypes = (HashMap) session.getAttribute("validCountryStateCodes");
            HashMap hmReviewTypes = new HashMap();
            if((country !=null && !country.equals(EMPTY_STRING)) && (hmCombinedReviewTypes !=null && hmCombinedReviewTypes.size() > 0) && (state !=null && !state.equals(EMPTY_STRING))){
                hmReviewTypes   = (HashMap) hmCombinedReviewTypes.get(country);
                if(hmReviewTypes !=null && hmReviewTypes.size()>0){
                    Set setRevTypes =  hmReviewTypes.keySet(); ;
                    Iterator iterator = setRevTypes.iterator();
                    while(iterator.hasNext()){
                        Object key = iterator.next();
                        String value = (String)hmReviewTypes.get(key);
                        if(state.equals(value)){
                            stateTypeCode = key.toString();
                            break;
                        }
                    }
                }
                if(stateTypeCode !=null && !stateTypeCode.equals(EMPTY_STRING)){
                    dynaForm.set("state", stateTypeCode);
                }
                session.setAttribute("stateCodesData", hmReviewTypes);
                coeusDynaFormList.setInfoList(lstReviewType);
                session.setAttribute(PERSON_DYNA_BEANS_LIST, coeusDynaFormList);
            }

        }//end of if loop
    }
}
