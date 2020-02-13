/*
 * NasaOtherProjectInformationTxnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.bean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


/**
 *
 * @author  jenlu
 */
public class NasaOtherProjectInformationTxnBean {
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
    /** Creates a new instance of NasaOtherProjectInformationTxnBean */
    public NasaOtherProjectInformationTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    
    public HashMap getHistoric (String propNumber)
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
               "call s2sNasaOtherProjectInforPkg.get_historic( <<PROPOSAL_NUMBER>> , "
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
    
    public HashMap getProjectInternationalParticipation (String propNumber)
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
               "call s2sNasaOtherProjectInforPkg.get_pro_internation_partici( <<PROPOSAL_NUMBER>> , "
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
    
    public HashMap getPersonInfo (String propNumber, String personId, boolean nonMitFlag)
        throws CoeusException, DBException {
     
        if(propNumber==null) 
                throw new CoeusXMLException("Proposal Number is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        String mitPerson = "TRUE";
        if (nonMitFlag) mitPerson = "FALSE";
        
        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
        param.addElement(new Parameter("PERSON_ID",
                   DBEngineConstants.TYPE_STRING,personId));
        param.addElement(new Parameter("MIT_PERSON",
                   DBEngineConstants.TYPE_STRING,mitPerson));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sNasaOtherProjectInforPkg.get_person_info( <<PROPOSAL_NUMBER>> , <<PERSON_ID>>, <<MIT_PERSON>> , "
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
     
    
     /*****************************************************
     *added for case 3135
     *  getPersons returns ArrayList containing two vectors
     *  first vector is the first numPersons;  second vector is the remaining people
     ***************************************/
       
     public ArrayList getPersons( String propNumber, int numPersons)
       throws CoeusException, DBException {  
        ArrayList listInvestigators = new ArrayList();
        String personId = null;
        KeyPersonBean keyPersonBean;
        CoeusVector vecInvestigators = new CoeusVector();
       
        Vector result = null;
        Vector param = new Vector();
        param.add(new Parameter("AS_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING, propNumber)) ;
        
        HashMap coInvRow = null;
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call s2sNasaOtherProjectInforPkg.get_persons ( <<AS_PROPOSAL_NUMBER>>  , "
                +    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
          for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                coInvRow = (HashMap)result.elementAt(rowIndex);
                keyPersonBean = new KeyPersonBean();
                personId = (String) coInvRow.get("PERSON_ID");
                keyPersonBean.setPersonId(personId);
                keyPersonBean.setSortId( Integer.parseInt(coInvRow.get("SORT_ID").toString()));
   
                keyPersonBean.setFirstName((String) 
			(coInvRow.get("FIRST_NAME") == null ? "Unknown" : coInvRow.get("FIRST_NAME")));
                keyPersonBean.setLastName((String)
                        (coInvRow.get("LAST_NAME") == null ? "Unknown" : coInvRow.get("LAST_NAME")) );
                keyPersonBean.setMiddleName((String)
                        (coInvRow.get("MIDDLE_NAME") == null ? "Unknown" : coInvRow.get("MIDDLE_NAME")) );
                keyPersonBean.setRole((String)
                       (coInvRow.get("PROJECT_ROLE") == null ? "Unknown":coInvRow.get("PROJECT_ROLE")) );
                String nonMITKeyPersonFlag = (String)coInvRow.get("NON_MIT_PERSON");
                if(nonMITKeyPersonFlag == null || nonMITKeyPersonFlag.equals("N")){
                    keyPersonBean.setNonMITPersonFlag(false);
                }   
                else{
                    keyPersonBean.setNonMITPersonFlag(true);
                }           
                vecInvestigators.add(keyPersonBean);
          }
        }
       
        CoeusVector cvAllPersons = new CoeusVector();
        cvAllPersons.addAll(vecInvestigators);
        CoeusVector cvNKeyPersons = getNKeyPersons(vecInvestigators,true,numPersons);
        CoeusVector cvExtraPersons = getNKeyPersons(cvAllPersons,false,numPersons);

      
 
        listInvestigators.add(cvNKeyPersons);
        listInvestigators.add(cvExtraPersons);
        return listInvestigators;
    }
    
     
   /*************************************************************************
    need to limit the number of key persons to n and remove duplicates
    * returns vector of first n people or vector of extra people
   **************************************************************************/

     private CoeusVector getNKeyPersons ( CoeusVector cvKeyPersons, boolean firstN, int n)
      throws CoeusException, DBException {
     
        CoeusVector cvExtraPersons = new CoeusVector();
        KeyPersonBean keyPersonBean, previousKeyPersonBean;
       
       //  get the first n sort ids
        
        String[] fieldNames = {"personId", "sortId"};
        cvKeyPersons.sort(fieldNames, true);
       
        int cvSize = cvKeyPersons.size();
        
        for (int i = cvSize-1 ; i > 0; i--){
          keyPersonBean =   (KeyPersonBean)(cvKeyPersons.get(i));
          previousKeyPersonBean = (KeyPersonBean)(cvKeyPersons.get(i-1));
          if (keyPersonBean.getPersonId().equals(previousKeyPersonBean.getPersonId())){
              cvKeyPersons.removeElementAt(i);
          }
        }
        
        cvKeyPersons.sort("sortId");
        cvSize = cvKeyPersons.size();
        
        if (firstN) {
             if (cvSize <= n){
                 return cvKeyPersons;
             }else {
                 //remove extras
                for (int i = cvSize-1; i > n-1 ; i--){
                   cvKeyPersons.removeElementAt(i);
                }
                return cvKeyPersons;
             }
           
        } else {
            //return extra people
            if (cvSize <=n){
                cvExtraPersons = null;
            }else {
                for (int i = cvSize-1; i > n-1 ; i--){
                    cvExtraPersons.add(cvKeyPersons.get(i));
                }
            }
            return cvExtraPersons;
        }
    }
    
}
