/*
 * RRSF424V11TxnBean.java
 *
 * Created on June 15 20, 2006
 */

package edu.mit.coeus.s2s.bean;


import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  ele
 */
public class RRSF424V11TxnBean {
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
   
    String propNumber;
    
    /** Creates a new instance of RRSF424V11TxnBean */
    public RRSF424V11TxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    
    public HashMap getApplicantType (int orgTypeCode)
        throws CoeusException, DBException {
     
       
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("ORGANIZATION_TYPE_CODE",
                       DBEngineConstants.TYPE_INT,""+orgTypeCode));
            
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sRRSF424_V11Pkg.getApplicantType( <<ORGANIZATION_TYPE_CODE>> , "
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
    
    
     /*case 2406*/
    public HashMap getOrganizationID(String propNumber,String orgType)
            throws CoeusException, DBException {
   
    if(propNumber==null)
        throw new CoeusXMLException("Proposal Number is Null");
    Vector result = null;
    Vector param= new Vector();
    
    HashMap row = null;
    param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    param.addElement(new Parameter("ORG_TYPE",
                       DBEngineConstants.TYPE_STRING, orgType));
    if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sRRSF424_V11Pkg.getOrganization( <<PROPOSAL_NUMBER>> ,<<ORG_TYPE>> , "
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
        
      /*case 2406*/
    public HashMap getCongDistrict(String propNumber, String orgID, int locType)
            throws CoeusException, DBException {
   

        if(propNumber==null)
        throw new CoeusXMLException("Proposal Number is Null");
    Vector result = null;
    Vector param= new Vector();
    
    HashMap row = null;
    param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
     param.addElement(new Parameter("ORGANIZATION_ID",
                       DBEngineConstants.TYPE_STRING,orgID));
     param.addElement(new Parameter("LOCATION_TYPE",
                       DBEngineConstants.TYPE_INT, ( Integer.toString(locType))));
   
    if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sRRSF424_V11Pkg.getCongDist( <<PROPOSAL_NUMBER>>  , <<ORGANIZATION_ID>> , <<LOCATION_TYPE>> , "
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
        
    
    //start case 3331
    
      public HashMap getAreasAffected (String propNumber)
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
               "call s2sRRSF424_V11Pkg.getAreasAffected( <<PROPOSAL_NUMBER>> , "
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
    
    
    //end case 3331
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
               "call s2sRRSF424_V11Pkg.getStateName( <<STATE_CODE>> , "
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
               "call s2sRRSF424_V11Pkg.getCountryName( <<COUNTRY_CODE>> , "
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
      
  public HashMap getIDCForModBudget(String propNumber, int version)
        throws CoeusException, DBException
   {
 
       int total = 0;
    
       BigDecimal bdIDC = new BigDecimal("0");
       HashMap hmCosts = new HashMap();
       
       Vector result = new Vector(3,2);
          
       HashMap resultRow = null;
       Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
    
    
       if(dbEngine !=null){

           result = dbEngine.executeRequest("Coeus",
             "call s2sRRSF424_V11Pkg.getModBudIDC(<<PROPOSAL_NUMBER>> , " 
             + " <<VERSION_NUMBER>> , <<OUT RESULTSET rset>>)",
                "Coeus", param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
       int vecSize = result.size();
       if (vecSize >0) {
               resultRow = (HashMap) result.elementAt(0);  
                 
               bdIDC = new BigDecimal(resultRow.get("IDC") == null ? "0" :
                                      resultRow.get("IDC") .toString()); 
                       
        }                
     
   
     hmCosts.put("IDC",bdIDC);
     return hmCosts;

    }
    
      
    public HashMap getTotForModBudget(String propNumber, int version)
        throws CoeusException, DBException
   {
 
       int total = 0;
    
       BigDecimal bdTot = new BigDecimal("0");
       HashMap hmCost = new HashMap();
       
       Vector result = new Vector(3,2);
          
       HashMap resultRow = null;
       Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
    
    
       if(dbEngine !=null){

           result = dbEngine.executeRequest("Coeus",
             "call s2sRRSF424_V11Pkg.getModBudTot(<<PROPOSAL_NUMBER>> , " 
             + " <<VERSION_NUMBER>> , <<OUT RESULTSET rset>>)",
                "Coeus", param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
       int vecSize = result.size();
       if (vecSize >0) {
               resultRow = (HashMap) result.elementAt(0);  
                 
               bdTot = new BigDecimal(resultRow.get("TOTAL_COST") == null ? "0" :
                                      resultRow.get("TOTAL_COST") .toString()); 
                       
        }                
     
   
     hmCost.put("TOTAL_COST",bdTot);
     return hmCost;

    }
    
     public HashMap getCostShareForModBudget(String propNumber, int version)
        throws CoeusException, DBException
   {
 
       int total = 0;
    
       BigDecimal bdCostShare = new BigDecimal("0");
       HashMap hmCost = new HashMap();
       
       Vector result = new Vector(3,2);
          
       HashMap resultRow = null;
       Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
    
    
       if(dbEngine !=null){

           result = dbEngine.executeRequest("Coeus",
             "call s2sRRSF424_V11Pkg.getModBudCostShare(<<PROPOSAL_NUMBER>> , " 
             + " <<VERSION_NUMBER>> , <<OUT RESULTSET rset>>)",
                "Coeus", param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
       int vecSize = result.size();
       if (vecSize >0) {
               resultRow = (HashMap) result.elementAt(0);  
                 
               bdCostShare = new BigDecimal(resultRow.get("COST_SHARE") == null ? "0" :
                                      resultRow.get("COST_SHARE") .toString()); 
                       
        }                
     
   
     hmCost.put("COST_SHARE",bdCostShare);
     return hmCost;

    }
    
     //added for coeusqa-2035
      public HashMap getCostShareAmt(String propNumber, int version)
        throws CoeusException, DBException
   {

       int total = 0;

       BigDecimal bdCostShare = new BigDecimal("0");
       HashMap hmCost = new HashMap();

       Vector result = new Vector(3,2);

       HashMap resultRow = null;
       Vector param = new Vector();

       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));


       if(dbEngine !=null){

           result = dbEngine.executeRequest("Coeus",
             "call s2sRRSF424_V11Pkg.getCostShareAmt(<<PROPOSAL_NUMBER>> , "
             + " <<VERSION_NUMBER>> , <<OUT RESULTSET rset>>)",
                "Coeus", param);

        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       int vecSize = result.size();
       if (vecSize >0) {
               resultRow = (HashMap) result.elementAt(0);

               bdCostShare = new BigDecimal(resultRow.get("COST_SHARE_AMT") == null ? "0" :
                                      resultRow.get("COST_SHARE_AMT") .toString());

        }


     hmCost.put("COST_SHARE_AMT",bdCostShare);
     return hmCost;

    }

    /**********************************
     ** getUnitContactPerson
     ***********************************/
      public HashMap getUnitContactPerson (String propNumber)
        throws CoeusException, DBException {
     
        if(propNumber==null) 
                throw new CoeusXMLException("Proposal number is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sRRSF424_V11Pkg.getUnitContactPerson( <<PROPOSAL_NUMBER>> , "
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
      
     public HashMap getContactType (String propNumber)
      throws CoeusException, DBException {
     
        if(propNumber==null) 
                throw new CoeusXMLException("Proposal number is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sRRSF424_V11Pkg.getContactType( <<PROPOSAL_NUMBER>> , "
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
     
     public HashMap getContactPerson(String propNumber, String contactType)
       throws CoeusException, DBException {
            if(propNumber==null) 
                throw new CoeusXMLException("Proposal number is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
        param.addElement(new Parameter("CONTACT_TYPE", 
                       DBEngineConstants.TYPE_STRING,contactType));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sRRSF424_V11Pkg.getContactPerson( <<PROPOSAL_NUMBER>> , <<CONTACT_TYPE>> , "
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
    
      public HashMap getLeadUnit (String propNumber)
         throws CoeusException, DBException {
             
              if(propNumber==null) 
                throw new CoeusXMLException("Proposal number is Null");   
        
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
       
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sRRSF424_V11Pkg.getLeadUnit( <<PROPOSAL_NUMBER>>  , "
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

      //added for coeusqa-3344
      public HashMap getISNIH (String propNumber)
         throws CoeusException, DBException {

              if(propNumber==null)
                throw new CoeusXMLException("Proposal number is Null");

        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));


        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sRRSF424_V11Pkg.getISNIH( <<PROPOSAL_NUMBER>>  , "
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


