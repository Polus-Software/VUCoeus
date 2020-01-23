/*
 * @(#)PersonInfoTxnBean.java 1.0 4/14/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;
import java.util.HashMap;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;

/**
 * This class provides the methods for performing all the user validation for
 * person information .
 * All methods are used <code>DBEngineImpl</code> instance for the
 * database interaction.
 *
 * @version 1.0 April 14, 2002, 2:54 PM
 * @author  Mukundan C
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling.
 *
 */
public class PersonInfoTxnBean {
    // holds instance of a dbEngine
    private DBEngineImpl dbEngine;

    /** Creates new PersonInfoTxnBean with no parameter */
    public PersonInfoTxnBean(){
        dbEngine = new DBEngineImpl();
    }

    /**
     *  This method is used to check person details is valid name or not ,then
     *  get the id title and homeunit for the name.
     *  <li>To fetch the data, it uses get_person_info_name procedure.
     *
     *  @param fullName String
     *  @return person id String for the person id.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     *  @exception SQLException Exception
     */
    public String getPersonID(String fullName) throws CoeusException, DBException,
                                            SQLException,Exception {
        Vector resultData = new Vector();
        Vector param= new Vector();
        Vector result = new Vector();
        String personID = "";
        if(fullName==null)
            return "";
        param.add(new Parameter("FULLNAME","String",fullName.trim()));

        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_person_info_name( << FULLNAME >> ,"
            +" << OUT STRING PERSONID >> , << OUT STRING TITLE >> , "
            +" << OUT STRING FLAG >>,<< OUT STRING UNITHOME >>) ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowPerson = (HashMap)result.elementAt(0);
            personID = (String)rowPerson.get("PERSONID");
        }
        return personID;
    }


    /**
     * This method is used to get all the information of a particular person.
     * After validating the user, this method will be called to get the personal
     * details.
     *
     *  @param personId String
     *  @return PersonInfoFormBean PersonInfo Bean
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public PersonInfoFormBean getPersonInfo(String personId)
            throws  CoeusException, DBException{
        if(personId==null){
            throw new DBException("exceptionCode.20001");
        }
        Vector param= new Vector();
        Vector result = new Vector();
        PersonInfoFormBean personInfo = new PersonInfoFormBean();
        param.addElement(new Parameter("PERSON_ID","String",personId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_person (  <<PERSON_ID>> , <<OUT RESULTSET rset>> )   ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap personRow = (HashMap)result.elementAt(0);
            personInfo.setPersonID((String)personRow.get("PERSON_ID"));
            personInfo.setFullName( (String)personRow.get("FULL_NAME"));
            personInfo.setUserName( (String)personRow.get("USER_NAME"));
            personInfo.setDirDept((String) personRow.get(
            "DIRECTORY_DEPARTMENT"));
            personInfo.setDirTitle((String)personRow.get("DIRECTORY_TITLE"));
            //case 1646 start
            personInfo.setDegree((String)personRow.get("DEGREE"));
            personInfo.setSaluation((String)personRow.get("SALUTATION"));
            //case 1646 end
            personInfo.setFacFlag( (String)personRow.get("IS_FACULTY"));
            personInfo.setHomeUnit( (String)personRow.get("HOME_UNIT"));
            personInfo.setLastName( (String)personRow.get("LAST_NAME"));
            personInfo.setFirstName( (String)personRow.get("FIRST_NAME"));
            personInfo.setPriorName( (String)personRow.get("PRIOR_NAME"));
            personInfo.setEmail( (String)personRow.get("EMAIL_ADDRESS"));
            personInfo.setOffLocation( (String)personRow.get(
            "OFFICE_LOCATION"));
            personInfo.setOffPhone( (String)personRow.get("OFFICE_PHONE"));
            personInfo.setSecOffLoc( (String)personRow.get(
            "SECONDRY_OFFICE_LOCATION"));
            personInfo.setSecOffPhone( (String)personRow.get(
            "SECONDRY_OFFICE_PHONE"));
            //Added for Case 2081 - Investigators Address in templates : Bring forward the address of an individual in correspondences - Start   
            personInfo.setAddressLine1((String)personRow.get("ADDRESS_LINE_1"));
            personInfo.setAddressLine2((String)personRow.get("ADDRESS_LINE_2"));
            personInfo.setAddressLine3((String)personRow.get("ADDRESS_LINE_3"));
            personInfo.setCity((String)personRow.get("CITY"));
            personInfo.setState((String)personRow.get("STATE"));
//            personInfo.setCountry((String)personRow.get("COUNTY"));            
            personInfo.setCounty((String)personRow.get("COUNTY"));            
            personInfo.setPostalCode((String)personRow.get("POSTAL_CODE"));
            personInfo.setCountryCode((String)personRow.get("COUNTRY_CODE"));
            personInfo.setFaxNumber((String)personRow.get("FAX_NUMBER"));
            personInfo.setPagerNumber((String)personRow.get("PAGER_NUMBER"));
            personInfo.setMobilePhoneNumber((String)personRow.get("MOBILE_PHONE_NUMBER"));
            //Added for Case 2081 - Investigators Address in templates : Bring forward the address of an individual in correspondences - End   
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - start
            personInfo.setSalaryAnniversaryDate(
                    personRow.get("SALARY_ANNIVERSARY_DATE") == null ? null :new java.sql.Date( 
                    ((Timestamp) personRow.get("SALARY_ANNIVERSARY_DATE")).getTime()));     
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - end
            
        }
        return personInfo;
    }

    /**
     * This method is used to get all the information of a particular person.
     * After validating the personFullName, this method will be called to get
     * the personal  details.
     *
     *  @param fullName String
     *  @return PersonInfoFormBean PersonInfo Bean
     *  @exception DBException if any error during database transaction. 
     *  @exception SQLException,Exception.
     */
     public PersonInfoFormBean getPersonInfoForName(String fullName)
        throws DBException, SQLException,Exception {
           PersonInfoFormBean personInfoFormBean = null;
           String personID =  getPersonID(fullName);
           final String TOO_MANY = "TOO_MANY";

            if (personID != null){
                personInfoFormBean = getPersonInfo(personID);
            }
           return personInfoFormBean;
     }

}