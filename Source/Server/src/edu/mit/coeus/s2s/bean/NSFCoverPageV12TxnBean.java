/*
 * NSFCoverPageV12TxnBean.java 
 */

package edu.mit.coeus.s2s.bean;


import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  jenlu
 */
public class NSFCoverPageV12TxnBean {
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
   
    String propNumber;
    
    /** Creates a new instance of SF424V2TxnBean */
    public NSFCoverPageV12TxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    
   
    
    public HashMap getS2sOpportunity (String propNumber)
        throws CoeusException, DBException {
     
        if(propNumber==null) 
                throw new CoeusXMLException("Proposal Number is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sSF424V2Pkg.getS2sOpportunity( <<PROPOSAL_NUMBER>> , "
                                + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
    }
 //add for nsfCoverPageV13- coeusdev-370
 /** getQuestionnaireAnswers
 *
 */
 public CoeusVector getQuestionnaireAnswers(String proposalNumber, int questionnaireID, int module_code)
     throws CoeusException , DBException{


         CoeusVector cvQuestions = new CoeusVector();

         Vector param = new Vector();

         param.addElement( new Parameter("MODULE_ITEM_KEY",
               DBEngineConstants.TYPE_STRING, proposalNumber));
         param.addElement(new Parameter("QUESTIONNAIRE_ID",
               DBEngineConstants.TYPE_INT, ( Integer.toString(questionnaireID))));
         param.addElement(new Parameter("MODULE_ITEM_CODE",
               DBEngineConstants.TYPE_INT, ( Integer.toString(module_code))));
         HashMap row = null;
         Vector result = new Vector();

         if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus",
            "call  get_ans_for_a_questionnaire" +
                    " ( <<MODULE_ITEM_KEY>> , <<QUESTIONNAIRE_ID>> , <<MODULE_ITEM_CODE>>, " +
                    " <<OUT RESULTSET rset>> )",  "Coeus", param);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int vecSize = result.size();

        if (vecSize >0){
            for(int i=0;i<vecSize;i++){
               row = (HashMap)result.elementAt(i);
               cvQuestions.add(row);
            }
        }

        return cvQuestions;


 }
      /** get Authorized rep
     */
    public DepartmentPersonFormBean getAuthorizedRep (String proposalNumber)
        throws CoeusException, DBException
    {
      
        Vector result = null;
        Vector param = new Vector();
        param.addElement( new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, proposalNumber));
                  
        HashMap row = null;
        if(dbEngine !=null){
             result = new Vector(3,2);
             result = dbEngine.executeRequest("Coeus",
                "call GET_AUTHORIZED_SIGNER ( <<PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> )",         
                                               "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }   
                  
         DepartmentPersonFormBean authRepBean = new DepartmentPersonFormBean();
             
         int listSize = result.size();
         if (listSize > 0){
           for(int index=0; index < listSize; index++){
                row = (HashMap)result.elementAt(index);
               
                authRepBean.setPersonId( (String) row.get("PERSON_ID"));
                authRepBean.setHomeUnit((String) row.get("HOME_UNIT"));  //CASE 2911
                authRepBean.setPrimaryTitle( (String) row.get("PRIMARY_TITLE")== null ?
                         " " : (String) row.get("PRIMARY_TITLE")) ;

                         authRepBean.setDirTitle((String) row.get("PRIMARY_TITLE")== null ?
                         " " : (String) row.get("PRIMARY_TITLE")) ;
                authRepBean.setEmailAddress( (String) row.get("EMAIL_ADDRESS")== null ?
                         " " : (String) row.get("EMAIL_ADDRESS")) ;
                authRepBean.setFirstName((String) row.get("FIRST_NAME")== null ?
                         " " : (String) row.get("FIRST_NAME")) ;
                authRepBean.setFullName((String) row.get("FULL_NAME")== null ?
                         " " : (String) row.get("FULL_NAME")) ;
                authRepBean.setLastName((String) row.get("LAST_NAME")== null ?
                         " " : (String) row.get("LAST_NAME")) ;
                authRepBean.setMiddleName((String) row.get("MIDDLE_NAME")== null ?
                         " " : (String) row.get("MIDDLE_NAME")) ;
                authRepBean.setOfficePhone((String) row.get("OFFICE_PHONE")== null ?
                         " " : (String) row.get("OFFICE_PHONE")) ;
                authRepBean.setOfficeLocation((String) row.get("OFFICE_LOCATION")== null ?
                         " " : (String) row.get("OFFICE_LOCATION")) ;
                /* CASE #1778 Begin */
                authRepBean.setFaxNumber((String) row.get("FAX_NUMBER")== null ? 
                        " " : (String) row.get("FAX_NUMBER")) ; 
                /* CASE #1778 End */        
                /* addition mar 20-21,2006 */
                authRepBean.setAddress1((String) row.get("ADDRESS_LINE_1")== null ? 
                         " " : (String) row.get("ADDRESS_LINE_1"));
                /* start case 2911 */
                authRepBean.setAddress2((String) row.get("ADDRESS_LINE_2")== null ? 
                         " " : (String) row.get("ADDRESS_LINE_2"));
                authRepBean.setAddress3((String) row.get("ADDRESS_LINE_3")== null ? 
                         " " : (String) row.get("ADDRESS_LINE_3"));
                 /* end case 2911 */
                authRepBean.setCity((String) row.get("CITY")== null ? 
                         " " : (String) row.get("CITY")); 
                authRepBean.setCountryCode((String) row.get("COUNTRY_CODE")== null ? 
                         " " : (String) row.get("COUNTRY_CODE"));
                authRepBean.setDirDept((String) row.get("DEPARTMENT")== null ?
                         " " : (String) row.get("DEPARTMENT"));
                /* end addition mar 20-21 */
                /* start addtion mar 23 */
                authRepBean.setPostalCode((String) row.get("POSTAL_CODE") == null ?
                        " " : (String) row.get("POSTAL_CODE"));
                authRepBean.setState((String) row.get("STATE") == null ?
                        " " : (String) row.get("STATE"));

           }
         }
         
     return authRepBean;
    }
}
