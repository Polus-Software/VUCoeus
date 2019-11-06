/*
 * SubcontractTxnBean.java
 */
package edu.vanderbilt.coeus.subcontract.bean;

import java.util.Vector;
import java.util.HashMap;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.exception.CoeusException;
//import edu.vanderbilt.coeus.subcontract.bean.SubcontractPersonBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;


public class SubcontractTxnBean {
    private DBEngineImpl dbEngine;

    public SubcontractTxnBean(){
        dbEngine = new DBEngineImpl();
    }
    
    /**
     *  Gets person information given person Id
     *  @param personId String person Id
     *  @return PersonBean PersonBean
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public CoeusVector getPerson(String personId)
            throws CoeusException, DBException{

        Vector param = new Vector();
        Vector result = new Vector();
        
        param.addElement(new Parameter("PERSON_ID","String",personId));
        
        if (dbEngine!=null) {
            result = dbEngine.executeRequest("Coeus", "call dw_get_person (  <<PERSON_ID>> , <<OUT RESULTSET rset>> )   ", "Coeus", param);
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        CoeusVector personInfo = new CoeusVector();
        DepartmentPersonFormBean personBean = new DepartmentPersonFormBean();
        HashMap personRow = new HashMap();
        if (result != null && !result.isEmpty()) {
            personRow = (HashMap)result.elementAt(0);
            
            personBean.setPersonId((String)personRow.get("PERSON_ID"));
            personBean.setLastName((String)personRow.get("LAST_NAME"));
            personBean.setFirstName((String)personRow.get("FIRST_NAME"));
            personBean.setMiddleName((String)personRow.get("MIDDLE_NAME"));
            personBean.setFullName((String)personRow.get("FULL_NAME"));
            personBean.setPriorName((String)personRow.get("PRIOR_NAME"));
            personBean.setUserName((String)personRow.get("USER_NAME"));
            personBean.setEmailAddress((String)personRow.get("EMAIL_ADDRESS"));
            personBean.setOfficeLocation((String)personRow.get("OFFICE_LOCATION"));
            personBean.setOfficePhone((String)personRow.get("OFFICE_PHONE"));
            personBean.setSecOfficeLocation((String)personRow.get("SECONDRY_OFFICE_LOCATION"));
            personBean.setSecOfficePhone((String)personRow.get("SECONDRY_OFFICE_PHONE"));
            personBean.setDirDept((String)personRow.get("DIRECTORY_DEPARTMENT"));
            personBean.setPrimaryTitle((String)personRow.get("DIRECTORY_TITLE"));
            personBean.setDirTitle((String)personRow.get("DIRECTORY_TITLE"));
            personBean.setHomeUnit((String)personRow.get("HOME_UNIT"));
            personBean.setAddress1((String)personRow.get("ADDRESS_LINE_1"));
            personBean.setAddress2((String)personRow.get("ADDRESS_LINE_2"));
            personBean.setAddress3((String)personRow.get("ADDRESS_LINE_3"));
            personBean.setCity((String)personRow.get("CITY"));
            personBean.setCounty((String)personRow.get("COUNTY"));
            personBean.setState((String)personRow.get("STATE"));
            personBean.setPostalCode((String)personRow.get("POSTAL_CODE"));
            personBean.setCountryCode((String)personRow.get("COUNTRY_CODE"));
            personBean.setFaxNumber((String)personRow.get("FAX_NUMBER"));
            personBean.setStatus((String)personRow.get("STATUS"));
        }
        
        personInfo.add(personBean);
        
        return personInfo;
    }
}
