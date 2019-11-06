/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.actions;

import edu.mit.coeus.award.bean.AwardListBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.search.bean.DisplayBean;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeuslite.award.formbeans.AllAwardRecordFormBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.print.attribute.HashAttributeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author indhulekha
 */
public class AllAwardRecordAction extends AwardBaseAction {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private static final String AWARD_SUB_HEADER="awardSubHeader";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
   public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
         Hashtable htAwardList =null ;
        HttpSession session = request.getSession();
        DBEngineImpl dbEngine = null;
        Vector displayLabels = new Vector();

        displayLabels.add(new DisplayBean("AWARD_NUMBER","Award Number","","true","String",""));
        displayLabels.add(new DisplayBean("TITLE","Title","","true","String",""));
        displayLabels.add(new DisplayBean("ACCOUNT_NUMBER","Account Number","","true","String",""));
        displayLabels.add(new DisplayBean("STATUS","Status","","true","String",""));
        displayLabels.add(new DisplayBean("PI","PI","","true","String",""));
        displayLabels.add(new DisplayBean("LEAD_UNIT","Lead Unit","","true","String",""));
        displayLabels.add(new DisplayBean("SPONSOR","Sponsor","","true","String",""));
        displayLabels.add(new DisplayBean("SPONSOR_CODE","Sponsor Code","","true","String",""));
        displayLabels.add(new DisplayBean("SPONSOR_AWARD_NUMBER","Sponsor Award Number","","true","String",""));
        displayLabels.add(new DisplayBean("START_DATE","Start Date","","true","String",""));
        displayLabels.add(new DisplayBean("END_DATE","End Date","","true","String",""));
        displayLabels.add(new DisplayBean("ANTICIPATED_TOTAL_AMOUNT","Anticipated Total Amount","","true","String",""));
        displayLabels.add(new DisplayBean("ANT_DISTRIBUTABLE_AMOUNT","Anticipated Distributable Amount","","true","String",""));
        displayLabels.add(new DisplayBean("OBLI_DISTRIBUTABLE_AMOUNT","Obligated Distributable Amount","","true","String",""));

        String listType = request.getParameter("searchName");
        String searchType = request.getParameter("type");
        session.setAttribute("type", searchType);
        session.removeAttribute("awardList");
        session.removeAttribute("childHierarchy");
        session.removeAttribute("awardColumnNames");
        session.removeAttribute("widthList");
        session.removeAttribute("childHierarchy");
        //PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute("person");
        PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = personInfoBean.getPersonID();
         getAwardMenus(request);
         getAwardInfoMenus(request);
        String coeusHeaderId =  request.getParameter("Menu_Id");
        if(coeusHeaderId!=null) {
            setSelectedCoeusHeaderPath(coeusHeaderId, request);
        }
        String subHeaderId =  request.getParameter("SUBHEADER_ID");
        Vector headerData = (Vector) session.getAttribute(AWARD_SUB_HEADER);
        if(subHeaderId!=null) {
           headerData = readSelectedPath(subHeaderId, headerData);
           session.setAttribute(AWARD_SUB_HEADER, headerData);
        }

        try{

        //    htAwardList = (Hashtable)getSearchResult(request,listType,personId);

             dbEngine = new DBEngineImpl();
            Vector result = new Vector(3,2);
            Vector param= new Vector();

             String parent = "";
                     WebTxnBean webTxnBean = new WebTxnBean();

                     Hashtable parentList = (Hashtable) webTxnBean.getResults(request, "getParentAward",null);
                   HashMap parentMap=  (HashMap) parentList.get("getParentAward");

                    if(parentMap!=null && !parentMap.isEmpty()){
                        parent=(String) parentMap.get("parentAward");
                        if(parent != null) {
                            parent = parent.trim();
                        }
                    }
                   Integer level = Integer.parseInt(parent);

           UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
           String userId = (String)userInfoBean.getUserId();

           if(listType !=null && listType.equals("AWARD_SEARCH")) {
               param = new Vector();
            param.addElement(new Parameter("AV_PERSON_ID",
                    DBEngineConstants.TYPE_STRING,personId));
            param.addElement(new Parameter("AV_USERID",
                    DBEngineConstants.TYPE_STRING,userId));
            param.addElement(new Parameter("AV_LEVEL",
                    DBEngineConstants.TYPE_INTEGER,null));
            param.addElement(new Parameter("AV_FLAG",
                    DBEngineConstants.TYPE_STRING,"AWARD_SEARCH"));

            if(dbEngine!=null){
                result = dbEngine.executeRequest("Coeus",
                        "call GET_ALL_AWARDS_FOR_PERSON( "
                        +"<<AV_PERSON_ID>>,<<AV_USERID>>,<<AV_LEVEL>>,<<AV_FLAG>>, <<OUT RESULTSET rset>> )", "Coeus", param);
            }
            else{
                throw new DBException("DB instance is not available");
            }
           }else if(listType !=null && listType.equals("ALL_ACTIVE_AWARDS")) {
                param = new Vector();
                param.addElement(new Parameter("AV_PERSON_ID",
                        DBEngineConstants.TYPE_STRING,personId));
                param.addElement(new Parameter("AV_USERID",
                        DBEngineConstants.TYPE_STRING,userId));
                param.addElement(new Parameter("AV_LEVEL",
                        DBEngineConstants.TYPE_INTEGER,null));
                param.addElement(new Parameter("AV_FLAG",
                        DBEngineConstants.TYPE_STRING,"ALL_ACTIVE_AWARDS"));

                if(dbEngine!=null){
                    result = dbEngine.executeRequest("Coeus",
                            "call GET_ALL_AWARDS_FOR_PERSON( "
                            +"<<AV_PERSON_ID>>,<<AV_USERID>>,<<AV_LEVEL>>,<<AV_FLAG>>, <<OUT RESULTSET rset>> )", "Coeus", param);
                }
                else{
                    throw new DBException("DB instance is not available");
                }

            }else if(listType !=null && listType.equals("ALL_PARENT_AWARD_SEARCH")){
                 param = new Vector();
                param.addElement(new Parameter("AV_PERSON_ID",
                        DBEngineConstants.TYPE_STRING,personId));
                param.addElement(new Parameter("AV_USERID",
                        DBEngineConstants.TYPE_STRING,userId));
                param.addElement(new Parameter("AV_LEVEL",
                        DBEngineConstants.TYPE_INTEGER,level));
                param.addElement(new Parameter("AV_FLAG",
                        DBEngineConstants.TYPE_STRING,"ALL_PARENT_AWARD_SEARCH"));

                if(dbEngine!=null){
                    result = dbEngine.executeRequest("Coeus",
                            "call GET_ALL_AWARDS_FOR_PERSON( "
                            +"<<AV_PERSON_ID>>,<<AV_USERID>>,<<AV_LEVEL>>,<<AV_FLAG>>, <<OUT RESULTSET rset>> )", "Coeus", param);
                }
                else{
                    throw new DBException("DB instance is not available");
                }
            } else if(listType !=null && listType.equals("ALL_ACTIVE_PARENT_AWARD")) {
                 param = new Vector();
                param.addElement(new Parameter("AV_PERSON_ID",
                        DBEngineConstants.TYPE_STRING,personId));
                param.addElement(new Parameter("AV_USERID",
                        DBEngineConstants.TYPE_STRING,userId));
                param.addElement(new Parameter("AV_LEVEL",
                        DBEngineConstants.TYPE_INTEGER,level));
                param.addElement(new Parameter("AV_FLAG",
                        DBEngineConstants.TYPE_STRING,"ALL_ACTIVE_PARENT_AWARD"));

                if(dbEngine!=null){
                    result = dbEngine.executeRequest("Coeus",
                            "call GET_ALL_AWARDS_FOR_PERSON( "
                            +"<<AV_PERSON_ID>>,<<AV_USERID>>,<<AV_LEVEL>>,<<AV_FLAG>>, <<OUT RESULTSET rset>> )", "Coeus", param);
                }
                else{
                    throw new DBException("DB instance is not available");
                }
            }

            WebTxnBean webTxn = new WebTxnBean();

            AwardListBean allAward =new AwardListBean();
            Vector list = new Vector();
            Vector awardListDisplay = new Vector();
            ArrayList awardLst = new ArrayList();
            HashMap widthList = new HashMap();

            Hashtable awardList = (Hashtable) webTxn.getResults(request, "getAwardListDisplay", null);
            list = (Vector) awardList.get("getAwardListDisplay");
           if (list != null && list.size() > 0) {
               Iterator itr = list.iterator();

               while(itr.hasNext()) {
                   allAward = (AwardListBean) itr.next();
                   awardLst.add(allAward.getFieldName().trim());
                   widthList.put(allAward.getFieldName().trim(), allAward.getSize());
               }
           }

        for(int i = 0; i < awardLst.size(); i++) {
            String fieldValue = awardLst.get(i).toString();

            Iterator itr1 = displayLabels.iterator();

            while(itr1.hasNext()) {
                DisplayBean dispBean = (DisplayBean)itr1.next();
                if(dispBean.getName().equals(fieldValue)) {
                    awardListDisplay.add(dispBean);
                }


            }
        }

        session.setAttribute("awardColumnNames", awardListDisplay);
        session.setAttribute("widthList", widthList);
        Vector resultList = filterAwardSearchResult(result, awardLst);


            if((listType !=null && (listType.equals("AWARD_SEARCH"))) ||(listType != null && listType.equals("ALL_ACTIVE_AWARDS")) ) {
                createParentChildTree(resultList,request);
            } else if(listType !=null && (listType.equals("ALL_PARENT_AWARD_SEARCH")) ||
                    listType !=null && (listType.equals("ALL_ACTIVE_PARENT_AWARD"))){
                    request.setAttribute("awardList", resultList);
            }
           // session.setAttribute("awardColumnNames", columnData);
                    
        }catch(CoeusException ex){
            request.setAttribute("awardList",new Vector());
             return actionMapping.findForward("success");
        }
            return actionMapping.findForward("success");
    }


    private Hashtable getSearchResult(HttpServletRequest request,String searchName,String personId)
            throws CoeusSearchException,CoeusException,DBException,Exception{

            HttpSession session = request.getSession();
            UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
            String userId = (String)userInfoBean.getUserId();


            Hashtable searchResult = null ;
            SearchInfoHolderBean searchInfoHolder = null;
            ProcessSearchXMLBean processSearchXML =null;

            processSearchXML = new ProcessSearchXMLBean("",searchName);

            searchInfoHolder = processSearchXML.getSearchInfoHolder();
            SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
            Vector criteriaList = searchInfoHolder.getCriteriaList();

            if(criteriaList == null) {
                criteriaList = new Vector();
                searchInfoHolder.setCriteriaList(criteriaList);
            }

            String unitNumber = "";
            String parent = "";

            WebTxnBean webTxnBean = new WebTxnBean();


             Hashtable parentList = (Hashtable) webTxnBean.getResults(request, "getParentAward",null);
               HashMap parentMap=  (HashMap) parentList.get("getParentAward");

                if(parentMap!=null && !parentMap.isEmpty()){
                    parent=(String) parentMap.get("parentAward");

                    if(parent != null) {
                        parent = parent.trim();
                    }
                }

            HashMap hmData = new HashMap();
            hmData.put("personId", personId);
             Hashtable roleList = (Hashtable)webTxnBean.getResults(request, "getUserRoles", hmData);
            Vector roles = (Vector)roleList.get("getUserRoles");
            AllAwardRecordFormBean formBean = new AllAwardRecordFormBean();
            int count = 0;

            if(roles != null) {
                Iterator itr = roles.iterator();

                while(itr.hasNext()) {
                    formBean = (AllAwardRecordFormBean)itr.next();
                    if(formBean.getRoleId() != null) {
                        count++;
                    }
                }

                if(count != 0) {
                     HashMap hmData1 = new HashMap();
                     hmData1.put("personId", personId);
                    Hashtable activeList = (Hashtable) webTxnBean.getResults(request, "getUserUnitNumber",hmData1);
                    HashMap unitMap=  (HashMap) activeList.get("getUserUnitNumber");
                    if(unitMap!=null && !unitMap.isEmpty()){
                        unitNumber=(String) unitMap.get("unit");
                    }
                }
            }

            if(searchName !=null && (searchName.equals("AWARD_SEARCH")) || searchName!= null && (searchName.equals("ALL_PARENT_AWARD_SEARCH"))){
                    String clause = searchInfoHolder.getRemClause();
                    StringBuffer remClause = new StringBuffer(clause);
                    String newRemClause = "";
                  String criteriaForUnit="osp$award.mit_award_number in ("+
"(select mit_award_number as col1 from osp$award_investigators where person_id='"+personId+"'"+
" union "+
"select mit_award_number as col1 from osp$award_units where unit_number='"+unitNumber+"'))";
String criteriaForPerson="osp$award.mit_award_number in ("+
"(select mit_award_number as col1 from osp$award_investigators where person_id='"+personId+"'))";
                if(unitNumber.length() > 0) {
                    newRemClause = Utils.replaceString( remClause.toString(),"COEUS", criteriaForUnit);
                }else {
                    newRemClause = Utils.replaceString( remClause.toString(),"COEUS", criteriaForPerson);
                }
                searchInfoHolder.setRemClause(newRemClause);

                if(searchName.equals("ALL_PARENT_AWARD_SEARCH")) {
                    remClause =  new StringBuffer(searchInfoHolder.getRemClause());
                    newRemClause =  remClause.append(" (FN_AWARD_LEVEL_FOR_SEARCH(osp$award.mit_award_number)="+parent+") ORDER BY osp$award.mit_award_number ASC").toString();
                }
                    searchInfoHolder.setRemClause(newRemClause);
            }

            if(searchName !=null && (searchName.equals("ALL_ACTIVE_PARENT_AWARD")) ||searchName !=null && (searchName.equals("ALL_ACTIVE_AWARDS")) ) {
                String status = "";                
                Hashtable activeList = (Hashtable) webTxnBean.getResults(request, "getActiveAwardList",null);
               HashMap statusMap=  (HashMap) activeList.get("getActiveAwardList");

                if(statusMap!=null && !statusMap.isEmpty()){
                    status=(String) statusMap.get("status");
                }

                String[] statusList = status.split(",");
                 String clause = searchInfoHolder.getRemClause();
                StringBuffer remClause = new StringBuffer(clause);
               String newRemClause = "";
String criteriaForUnit="osp$award.mit_award_number in ("+
"(select mit_award_number as col1 from osp$award_investigators where person_id='"+personId+"'"+
" union "+
"select mit_award_number as col1 from osp$award_units where unit_number='"+unitNumber+"'))";
String criteriaForPerson="osp$award.mit_award_number in ("+
"(select mit_award_number as col1 from osp$award_investigators where person_id='"+personId+"'))";
                if(unitNumber.length() > 0) {
                    newRemClause = Utils.replaceString( remClause.toString(),"COEUS", criteriaForUnit);
                }else {
                    newRemClause = Utils.replaceString( remClause.toString(),"COEUS", criteriaForPerson);
                }
                
                searchInfoHolder.setRemClause(newRemClause);

                remClause =  new StringBuffer(searchInfoHolder.getRemClause());
                remClause.append("and osp$award_status.status_code in (");
                for(int i = 0; i < statusList.length; i++) {
                    if(i < statusList.length - 1) {
                    remClause.append(statusList[i]+",");
                    }
                    else {
                         remClause.append(statusList[i]);
                    }
                }
                if(searchName !=null && (searchName.equals("ALL_ACTIVE_PARENT_AWARD"))) {
                     remClause.append(") and (FN_AWARD_LEVEL_FOR_SEARCH(osp$award.mit_award_number)="+parent+") ");
                }
                else if(searchName !=null && (searchName.equals("ALL_ACTIVE_AWARDS"))){
                    remClause.append(")");
                }
                remClause.append(" ORDER BY osp$award.mit_award_number ASC");

                searchInfoHolder.setRemClause(remClause.toString());
            }


            String awardListName = searchInfoHolder.getDisplayLabel();
            session.setAttribute("awardName", awardListName);
            searchResult = searchExecution.executeSearchQuery();
            searchResult.put("displayLable", searchInfoHolder.getDisplayLabel());

            return searchResult;
    }

    private void createParentChildTree(Vector data,HttpServletRequest request){
        HttpSession session = request.getSession();
        Iterator itr = data.iterator();
        Vector parentHierarchy = new Vector();
        Vector childHierarchy = new Vector();
        String parentAwardNumber = "";
        String rootAward = "";
        String awardNumber = "";
        boolean isParentPresent = false;
        while(itr.hasNext()) {
            HashMap dataMap = (HashMap)itr.next();
            String root = dataMap.get("ROOT_AWARD").toString();
            parentAwardNumber = dataMap.get("PARENT_MIT_AWARD_NUMBER").toString();
            if(dataMap.containsKey("AWARD_NUMBER")) {
                awardNumber = dataMap.get("AWARD_NUMBER").toString();
                awardNumber = awardNumber.substring(0,awardNumber.indexOf("-"));
            }
            isParentPresent = false;
            if(parentHierarchy != null && parentHierarchy.size() > 0) {
                for(int i=0; i < parentHierarchy.size(); i++) {
                    HashMap resMap = (HashMap)parentHierarchy.get(i);
                    String p_awd_num = resMap.get("AWARD_NUMBER").toString();
                    if(p_awd_num.equals(parentAwardNumber)) {
                        isParentPresent = true;
                        break;
                    }
                }
            }

            if(parentAwardNumber.equals("000000-000")){
                parentHierarchy.add(dataMap);
            }
            else if(!isParentPresent && !parentAwardNumber.equals("000000-000")){
                parentHierarchy.add(dataMap);
            }
            else {
               childHierarchy.add(dataMap); 
            }

          rootAward = awardNumber;
        }

       request.setAttribute("awardList", parentHierarchy);
       request.setAttribute("childHierarchy", childHierarchy);

    }

      private Vector filterAwardSearchResult(Vector data, ArrayList displayList) {
        Iterator itr = data.iterator();
        Map map = new HashMap();
         Map resultMap = new HashMap();
        Vector reslist = new Vector();
        String fieldName = "";

        while(itr.hasNext()) {
            resultMap = new HashMap();
            map = (HashMap)itr.next();
            for(int i = 0; i < displayList.size(); i++) {
                fieldName = displayList.get(i).toString();
                if(map.containsKey(fieldName)) {
                    resultMap.put(fieldName, map.get(fieldName));
                }
             }

            if(map.containsKey("ROOT_AWARD")) {
                resultMap.put("ROOT_AWARD", map.get("ROOT_AWARD"));
            }
            if(map.containsKey("PARENT_MIT_AWARD_NUMBER")) {
                resultMap.put("PARENT_MIT_AWARD_NUMBER", map.get("PARENT_MIT_AWARD_NUMBER"));
            }
 
             reslist.add(resultMap);
      }

        return reslist;
    }
}