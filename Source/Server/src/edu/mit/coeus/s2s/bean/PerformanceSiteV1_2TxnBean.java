/*
 * @(#)PerformanceSiteV1_2TxnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.s2s.bean;

import java.util.* ;
import java.sql.Connection;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;


  
public class PerformanceSiteV1_2TxnBean{
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
    private Connection conn = null;
    
    public PerformanceSiteV1_2TxnBean(){
        
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    

    //getOtherSiteList - returns arrayList of other Sites (not primary site)
    public ArrayList getOtherSiteList(String proposalNumber)
                                    throws CoeusException , DBException{
        ArrayList otherSiteList = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call s2sPerfSitesV12Pkg.get_perf_sites( <<PROPOSAL_NUMBER>> , "
            +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         int listSize = result.size();
        if (listSize > 0){
            otherSiteList = new ArrayList();
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
             row = (HashMap)result.elementAt(rowIndex);
              otherSiteList.add(row);
            }
        }

        return otherSiteList;
    }
    

//getCongDistrict
//  arguments:  siteNumber - site number             
//  returns : first congressional district for site - there can be multiple cong districts
//            but schema allows only one
 public HashMap getCongDistrict(String propNumber, int siteNumber)
         throws CoeusException, DBException {
   
   if(propNumber==null)
        throw new CoeusXMLException("Proposal Number is Null");
   Vector result = null;
   Vector param= new Vector();
    
   HashMap row = null;
   param.addElement(new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING,propNumber));

   param.addElement(new Parameter("SITE_NUMBER",
           DBEngineConstants.TYPE_INT,Integer.toString(siteNumber)));
    
  
    if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPerfSitesV12Pkg.getCongDist( <<PROPOSAL_NUMBER>>  , <<SITE_NUMBER>> , "
                                + "   <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
    }
        
//GET individual question
  public HashMap getIndivYNQ (String orgID)
           throws CoeusException, DBException {
    Vector result = null;
    HashMap row = null;
    Vector param= new Vector();
    
    param.addElement(new Parameter("ORGANIZATION_ID",
                       DBEngineConstants.TYPE_STRING,orgID));

    if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPerfSitesV12Pkg.getIndivYNQ( <<ORGANIZATION_ID>> , "
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
    
  
      
// gets performing organization id  for proposal
    public HashMap getPerfOrgId(String propNumber)
            throws CoeusException, DBException {
    Vector result = null;
    Vector param= new Vector();
    
    HashMap row = null;
    param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));

    if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPerfSitesV12Pkg.getPerfOrgId( <<PROPOSAL_NUMBER>> , "
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
    
    // gets performing organization id  for proposal
    public HashMap getPerfOrgSiteNumber (String propNumber)
            throws CoeusException, DBException {
    Vector result = null;
    Vector param= new Vector();
    
    HashMap row = null;
    param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));

    if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPerfSitesV12Pkg.getPerfOrgSiteNum( <<PROPOSAL_NUMBER>> , "
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
    
    
  public HashMap getDunsNumber(String orgId)
            throws CoeusException, DBException {
    Vector result = null;
    Vector param= new Vector();
    
    HashMap row = null;
    param.addElement(new Parameter("ORGANIZATION_ID",
                       DBEngineConstants.TYPE_STRING,orgId));

    if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPerfSitesV12Pkg.getDunsNumber( <<ORGANIZATION_ID>> , "
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
    
        
      public HashMap getStateName (String stateCode)
        throws CoeusException, DBException {
     
        if(stateCode==null) 
                throw new CoeusXMLException("State code is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("STATE_CODE",
                       DBEngineConstants.TYPE_STRING,stateCode));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPerfSitesV12Pkg.getStateName( <<STATE_CODE>> , "
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
    //COEUSQA-3228  
      public HashMap getStateName (String countryCode,String stateCode)
        throws CoeusException, DBException {     
        if(stateCode == null){ 
                throw new CoeusXMLException("State code is Null");   
        }
        if(countryCode == null){ 
                throw new CoeusXMLException("Country code is Null");    
        }
        Vector result = null;
        Vector param= new Vector();
        HashMap row = null;
        param.addElement(new Parameter("COUNTRY_CODE",
               DBEngineConstants.TYPE_STRING,countryCode));
        param.addElement(new Parameter("STATE_CODE",
                       DBEngineConstants.TYPE_STRING,stateCode));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPerfSitesV12Pkg.getStateName(<<COUNTRY_CODE>>, <<STATE_CODE>> , "
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
    //COEUSQA-3228      
     public HashMap getCountryName (String countryCode)
        throws CoeusException, DBException {
     
        if(countryCode==null) 
                throw new CoeusXMLException("Country code is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("COUNTRY_CODE",
                       DBEngineConstants.TYPE_STRING,countryCode));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPerfSitesV12Pkg.getCountryName( <<COUNTRY_CODE>> , "
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
}
  

