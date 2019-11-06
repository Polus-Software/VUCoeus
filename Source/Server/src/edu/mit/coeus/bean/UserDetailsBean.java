/*
 * @(#)UserDetailsBean.java 1.0 4/14/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.bean;

import java.sql.SQLException;
import java.util.Vector;
import java.util.HashMap;

import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalUnitFormBean;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.is.service.authorization.*;
/* CASE #748 Begin */
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
/* CASE #748 End */

/**
 * This class is used to validate the user Information.
 *
 * @version 1.0 April 14, 2002, 2:54 PM
 * @author  Geo Thomas
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling.
 *
 */
public class UserDetailsBean {
    private DBEngineImpl dbEngine;
    /** Creates new UserDetailsBean */
    public UserDetailsBean(){
        dbEngine = new DBEngineImpl();
    }
    /**
     *  This method is used for validate the user. Compare the user id from the certificate
     *  with user_name in the database.
     *  If the user is a valid user, it returns Person ID, else, returns null.
     *  It uses the stored function fn_check_valid_user() to do the validation
     *  @param userID String User ID
     *  @return String Person ID
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public String getPersonID(String userID)
            throws CoeusException,DBException{
        String personId = null;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        param.addElement(new Parameter("USERID","String",userID));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                "{ << OUT STRING PERSON_ID >> = call fn_check_valid_user( << USERID >> ) }",
                param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap personIdRow = (HashMap)result.elementAt(0);
            personId = (String)personIdRow.get("PERSON_ID");
        }
        return personId;
    }

    /**
     *  This method is used for validate the user, to check whether the user has
     *  rights to Create a new Proposal.
     *  If the user has rights, it returns 1, else, returns 0.
     *  It uses the stored function FN_USER_HAS_ROLE() to do the validation
     *  @param userID String User ID
     *  @param userID int Role ID
     *  @return int hasRight
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public int getUserHasRole(String userID, int roleID)
            throws CoeusException,DBException{
        int hasRight = 0;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        param.addElement(new Parameter("USERID","String",userID));
        param.addElement(new Parameter("ROLEID","int",new Integer(roleID).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                "{ << OUT INTEGER HAS_RIGHT >> = call FN_USER_HAS_ROLE( << USERID >>, << ROLEID >> ) }",
                param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap userRoleRow = (HashMap)result.elementAt(0);
            hasRight = Integer.parseInt(userRoleRow.get("HAS_RIGHT").toString());
        }
        return hasRight;
    }

    /**
     *  This method is used for validate the user, to check whether the user has
     *  any OSP rights.
     *  If the user has rights, it returns 1, else, returns 0.
     *  It uses the stored function FN_USER_HAS_ANY_OSP_RIGHT() to do the validation
     *  @param userID String User ID
     *  @return int hasRight
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public int getUserHasAnyOSPRights(String userID)
            throws CoeusException,DBException{
        int hasRight = 0;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        param.addElement(new Parameter("USERID","String",userID));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                "{ << OUT INTEGER HAS_RIGHT >> = call FN_USER_HAS_ANY_OSP_RIGHT( << USERID >> ) }",
                param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap userRoleRow = (HashMap)result.elementAt(0);
            hasRight = Integer.parseInt(userRoleRow.get("HAS_RIGHT").toString());
        }
        return hasRight;
    }

    /**
     *  This method is used to get all the information of a particular person.
     *  After validating the user, this method will be called to get the personal details.
     *  Keep this information in the session for further use.
     *  @param userId String Person ID
     *  @return PersonInfoBean PersonInfo Bean
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public PersonInfoBean getPersonInfo(String userId)
            throws  CoeusException,DBException{
//        String personId = getPersonID(userId);
        Vector param= new Vector();
        Vector result = new Vector();
        PersonInfoBean personInfo = new PersonInfoBean();
        personInfo.setUserId(userId);
        param.addElement(new Parameter("USER_ID","String",userId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", "call get_person_for_user (  <<USER_ID>> , <<OUT RESULTSET rset>> )   ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap personRow = (HashMap)result.elementAt(0);
            personInfo.setPersonID((String)personRow.get("PERSON_ID"));
            personInfo.setFullName((String)personRow.get("FULL_NAME"));
            personInfo.setUserName((String)personRow.get("USER_NAME"));
            personInfo.setDirDept((String)personRow.get("DIRECTORY_DEPARTMENT"));
            personInfo.setDirTitle((String)personRow.get("DIRECTORY_TITLE"));
            personInfo.setFacFlag((String)personRow.get("IS_FACULTY"));
            personInfo.setHomeUnit((String)personRow.get("HOME_UNIT"));
            personInfo.setLastName((String)personRow.get("LAST_NAME"));
            personInfo.setFirstName((String)personRow.get("FIRST_NAME"));
            personInfo.setPriorName((String)personRow.get("PRIOR_NAME"));
            personInfo.setEmail((String)personRow.get("EMAIL_ADDRESS"));
            personInfo.setOffLocation((String)personRow.get("OFFICE_LOCATION"));
            personInfo.setOffPhone((String)personRow.get("OFFICE_PHONE"));
            personInfo.setSecOffLoc((String)personRow.get("SECONDRY_OFFICE_LOCATION"));
            personInfo.setSecOffPhone((String)personRow.get("SECONDRY_OFFICE_PHONE"));
            //added by Vineetha
           // personInfo.setAddress1((String)personRow.get("ADDRESS_LINE_1"));


            /* CASE #748 Begin */
            personInfo.setPendingAnnDisclosure(hasPendingDisclosure(personInfo.getPersonID()));
            /* CASE #748 End */
        }
        return personInfo;
    }

    /* CASE #748 Begin */
    /**
     *  Method overloaded to take personID instead of userId.
     *  This method is used to get all the information of a particular person.
     *  After validating the user, this method will be called to get the personal details.
     *  Keep this information in the session for further use.
     *  @param userId String Person ID
     *  @return PersonInfoBean PersonInfo Bean
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public PersonInfoBean getPersonInfo(String personID, boolean usePersonId)
            throws  CoeusException,DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        PersonInfoBean personInfo = new PersonInfoBean();
        //personInfo.setUserId(userId);
        param.addElement(new Parameter("PERSON_ID","String",personID));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_person ( <<PERSON_ID>> , <<OUT RESULTSET rset>> ) ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap personRow = (HashMap)result.elementAt(0);
            personInfo.setPersonID((String)personRow.get("PERSON_ID"));
            personInfo.setFullName((String)personRow.get("FULL_NAME"));
            personInfo.setUserName((String)personRow.get("USER_NAME"));
            personInfo.setDirDept((String)personRow.get("DIRECTORY_DEPARTMENT"));
            personInfo.setDirTitle((String)personRow.get("DIRECTORY_TITLE"));
            personInfo.setFacFlag((String)personRow.get("IS_FACULTY"));
            personInfo.setHomeUnit((String)personRow.get("HOME_UNIT"));
            personInfo.setLastName((String)personRow.get("LAST_NAME"));
            personInfo.setFirstName((String)personRow.get("FIRST_NAME"));
            personInfo.setPriorName((String)personRow.get("PRIOR_NAME"));
            personInfo.setEmail((String)personRow.get("EMAIL_ADDRESS"));
            personInfo.setOffLocation((String)personRow.get("OFFICE_LOCATION"));
            personInfo.setOffPhone((String)personRow.get("OFFICE_PHONE"));
            personInfo.setSecOffLoc((String)personRow.get("SECONDRY_OFFICE_LOCATION"));
            personInfo.setSecOffPhone((String)personRow.get("SECONDRY_OFFICE_PHONE"));
            /* CASE #748 Begin */
            personInfo.setPendingAnnDisclosure(hasPendingDisclosure(personID));
            /* CASE #748 End */
        }
        //System.out.println("getPersonInfo returns PersonInfoBean with: "+personInfo.getPersonID());
        //System.out.println("getPersonInfo full name: "+personInfo.getFullName());
        //System.out.println("getPersonInfo userName: "+personInfo.getUserName());
        return personInfo;
    }
    /* CASE #748 End */

    /**
     *  This method is used to get all the information of a particular user.
     *  @param userId String User Id
     *  @return UserInfoBean contain userid, unit number, personid.
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public UserInfoBean getUserInfo(String userId)
            throws  CoeusException,DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        UserInfoBean userInfo = new UserInfoBean();
        param.addElement(new Parameter("USER_ID","String",userId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_user (  <<USER_ID>> , <<OUT RESULTSET rset>> )   ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap personRow = (HashMap)result.elementAt(0);
            userInfo.setUserId((String)personRow.get("USER_ID"));
            userInfo.setUserName((String)personRow.get("USER_NAME"));
            userInfo.setUnitNumber((String)personRow.get("UNIT_NUMBER"));
            userInfo.setUnitName((String)personRow.get("UNIT_NAME"));
            userInfo.setPersonId((String)personRow.get("PERSON_ID"));
            userInfo.setNonEmployee(personRow.get(
                            "NON_MIT_PERSON_FLAG") == null ? false :
                          (personRow.get("NON_MIT_PERSON_FLAG").toString()
                                    .equalsIgnoreCase("y") ? true :false));
            userInfo.setStatus(personRow.get("STATUS") == null ? ' '
                : ((String)personRow.get("STATUS")).charAt(0) );
            userInfo.setUserType(personRow.get("USER_TYPE") == null ? ' '
                : ((String)personRow.get("USER_TYPE")).charAt(0) );
        }
        return userInfo;
    }

    /**
     *  This method is used to get all the information of a particular user.
     *  @param userId String User Id
     *  @return UserInfoBean contain userid, unit number, personid.
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public Vector getUserForUnit(String unitNumber)
            throws  CoeusException,DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        UserInfoBean userInfo = null;
        param.addElement(new Parameter("UNIT_NUMBER","String",unitNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_users_for_unit ( <<UNIT_NUMBER>> , <<OUT RESULTSET rset>> ) ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector userUnit = null;
        if (listSize >0){
            userUnit = new Vector(3,2);
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            userInfo = new UserInfoBean();
            HashMap personRow = (HashMap)result.elementAt(rowIndex);
            userInfo.setUserId((String)personRow.get("USER_ID"));
            userInfo.setUserName((String)personRow.get("USER_NAME"));
            userInfo.setUnitNumber((String)personRow.get("UNIT_NUMBER"));
            userInfo.setUnitName((String)personRow.get("UNIT_NAME"));
            userInfo.setPersonId((String)personRow.get("PERSON_ID"));
            userInfo.setNonEmployee(personRow.get(
                            "NON_MIT_PERSON_FLAG") == null ? false :
                          (personRow.get("NON_MIT_PERSON_FLAG").toString()
                                    .equalsIgnoreCase("y") ? true :false));
            userInfo.setStatus(personRow.get("STATUS") == null ? ' '
                : ((String)personRow.get("STATUS")).charAt(0) );
            userInfo.setUserType(personRow.get("USER_TYPE") == null ? ' '
                : ((String)personRow.get("USER_TYPE")).charAt(0) );

            userUnit.addElement(userInfo);
            }
        }
        return userUnit;
    }

    /**
     *  This method used to get unit number and unit name for userId and rightId
     *  <li>To fetch the data, it uses the procedure DW_GET_UNITS_FOR_USER_RIGHT.
     *
     *  @return Vector of unit number and unit name
     *  @param String userId,rightId to get unit number and unit name
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getUnitsForUser(String userId,String rightId)
            throws DBException,CoeusException{
        Vector param= new Vector();
        Vector result = new Vector();
        ComboBoxBean comboBoxBean = null;
        HashMap rowPerson = null;
        param.add(new Parameter("USER_ID","String",userId.trim()));
        param.add(new Parameter("RIGHT_ID","String",rightId.trim()));
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_UNITS_FOR_USER_RIGHT(  << USER_ID >> , << RIGHT_ID >> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector othersList = null;
        if (listSize > 0){
            othersList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
            comboBoxBean = new ComboBoxBean();
            rowPerson = (HashMap)result.elementAt(rowIndex);
            comboBoxBean.setCode( (String)
                            rowPerson.get("UNIT_NUMBER"));
            comboBoxBean.setDescription( (String)
                            rowPerson.get("UNIT_NAME"));

            othersList.add(comboBoxBean);
            }
        }
        return othersList;
    }

    /* CASE #748 Begin */
    /**
     *  Method used to get the privilege of a particular user.
     *  <li>return 1 if the user has the right to view other's Disclosures
     *  <li>return 2 if the user has the right to maintain other's Disclosures
     *  <li>else return 0
     *  @return int right
     *  @exception DBException
     *  @exception CoeusException
     */
    public int getCOIPrivilege(String userId)
      throws DBException,CoeusException, org.okip.service.shared.api.Exception{
        int privilege = 0;
        final String VIEW_RIGHT_STR = "VIEW_CONFLICT_OF_INTEREST";
        final String EDIT_RIGHT_STR = "MAINTAIN_CONFLICT_OF_INTEREST";
        //String strUserID = getUserID(strUserName);
        /* CASE # Comment Begin. */
        /*if(!hasRight(userId,VIEW_RIGHT_STR)){
            return 0;
        }else if(hasRight(userId,EDIT_RIGHT_STR)){
            return 2;
        }else{
            return 1;
        }*/
        /* CASE # Comment End. */
        if( hasOSPRight(userId, EDIT_RIGHT_STR) ){
            return 2;
        }
        else if( hasOSPRight(userId, VIEW_RIGHT_STR)) {
            return 1;
        }
        else{
            return 0;
        }
    }
    /**
     *  Method used to get the right of a particular user.
     *  <li>To fetch the data, it uses FN_USER_HAS_OSP_RIGHT function.
     *  <li>return true if the user has the right
     *  <li>return false if the user does not have the right
     *  @return boolean right
     *  @exception CoeusException
     *  @exception DBException
     */
    private boolean hasOSPRight(String userId, String rightType)
      throws CoeusException, DBException, org.okip.service.shared.api.Exception {
        UserMaintDataTxnBean userMaintTxn = new UserMaintDataTxnBean();
        boolean hasOSPRight = userMaintTxn.getUserHasOSPRight(userId, rightType);
        return hasOSPRight;
    }

    /**
     *  Method used to check whether the person has any pending disclosures.
     *  This method is used to display the annual disclosure tab in the welcome page
     *  <li>To check this, it uses fn_person_has_pending_discl function.
     *  <li>Returns true if the person has any pending disclosures or else, returns false.
     *  @param String ModuleCode
     *  @param String ModuleItemKey
     *  @return boolean Disclosure Status
     *  @exception DBException
     *  @exception CoeusException
     */
    public boolean hasPendingDisclosure(String personId)
            throws CoeusException, DBException{
        String pendingDisclNum = "";
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        if(dbEngine!=null){
            param.addElement(new Parameter("PERSON_ID","String",personId));
            StringBuffer sqlQry = new StringBuffer("{ <<OUT INTEGER PENDING_DISC_COUNT>> = ");
            sqlQry.append("call fn_person_has_pending_discl ( ");
            sqlQry.append("<<PERSON_ID>>  ) }");
            result = dbEngine.executeFunctions("Coeus", sqlQry.toString(), param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            HashMap pendingDisclCountRow = (HashMap)result.elementAt(0);
            pendingDisclNum = pendingDisclCountRow.get("PENDING_DISC_COUNT").toString();
        }
        //System.out.println("hasPendinDisclosure returns : "+(Integer.parseInt(pendingDisclNum)>0));
        return (Integer.parseInt(pendingDisclNum)>0);
    }
    /* CASE #748 End */

    /* CASE #1046 Begin */
    public boolean canViewPendingDisc(String userId)  throws DBException,
            CoeusException, org.okip.service.shared.api.Exception{
        final String APPROVE_PROPOSAL = "APPROVE_PROPOSAL";
        /* CASE #1599 Begin */
        final String VIEW_ALL_PENDING_DISCLOSURES = "VIEW_ALL_PENDING_DISCLOSURES";
        /* CASE #1599 End */
        /* CASE #1599 Comment Begin */
        /*if(hasOSPRight(userId, APPROVE_PROPOSAL)){
            return true;
        }*/
        /* CASE #1599 Comment End */
        /* CASE #1599 Begin */
        //non-OSP persons with the appropriate right can also view pending discl
        boolean hasViewPendingDiscRight = false;
        

       //Commented for Case#3587 - multicampus enhancement  - Start
//        boolean hasApproveProposalRight = false;
//        hasApproveProposalRight = hasOSPRight(userId, APPROVE_PROPOSAL);
        //Case#3587 - End
        UserMaintDataTxnBean userMaintTxn = new UserMaintDataTxnBean();
        hasViewPendingDiscRight = userMaintTxn.getUserHasRightInAnyUnit
                                (userId, VIEW_ALL_PENDING_DISCLOSURES);
        //Modified for Case#3587 - multicampus enhancement  - Start
//        if(hasApproveProposalRight || hasViewPendingDiscRight){//Case#3587 - End
        if(hasViewPendingDiscRight){
            return true;
        }        
        /* CASE #1599 End */
        return false;
    }
    /* CASE #1046 End */
    
    
    //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
     /**
     *  This method is used to get all the person information for a user.
     *  @param userId String Person ID
     *  @return PersonInfoBean PersonInfo Bean
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public PersonInfoBean getPersonInfoForUser(String userId)
            throws  CoeusException,DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        PersonInfoBean personInfo = new PersonInfoBean();
        personInfo.setUserId(userId);
        param.addElement(new Parameter("USER_ID","String",userId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", "call get_person_for_user (  <<USER_ID>> , <<OUT RESULTSET rset>> )   ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap personRow = (HashMap)result.elementAt(0);
            personInfo.setPersonID((String)personRow.get("PERSON_ID"));
            personInfo.setFullName((String)personRow.get("FULL_NAME"));
            personInfo.setUserName((String)personRow.get("USER_NAME"));
            personInfo.setDirDept((String)personRow.get("DIRECTORY_DEPARTMENT"));
            personInfo.setDirTitle((String)personRow.get("DIRECTORY_TITLE"));
            personInfo.setFacFlag((String)personRow.get("IS_FACULTY"));
            personInfo.setHomeUnit((String)personRow.get("HOME_UNIT"));
            personInfo.setLastName((String)personRow.get("LAST_NAME"));
            personInfo.setFirstName((String)personRow.get("FIRST_NAME"));
            personInfo.setPriorName((String)personRow.get("PRIOR_NAME"));
            personInfo.setEmail((String)personRow.get("EMAIL_ADDRESS"));
            personInfo.setOffLocation((String)personRow.get("OFFICE_LOCATION"));
            personInfo.setOffPhone((String)personRow.get("OFFICE_PHONE"));
            personInfo.setSecOffLoc((String)personRow.get("SECONDRY_OFFICE_LOCATION"));
            personInfo.setSecOffPhone((String)personRow.get("SECONDRY_OFFICE_PHONE"));
             personInfo.setAddress1((String)personRow.get("ADDRESS_LINE_1"));
        }
        return personInfo;
    }
    //COEUSQA-2291 : End
}
