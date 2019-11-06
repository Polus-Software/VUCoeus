/*
 * ProposalPrintingTxnBean.java
 *
 * Created on April 21, 2004, 11:27 AM
 */


package edu.mit.coeus.utils.xml.bean.proposal.bean;

import java.util.* ;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.CategoryCostTypeConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.query.*;
import java.lang.Integer;
import java.math.*;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.DecimalFormat;

 

public class ProposalPrintingTxnBean
{
     // instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
     
    
    /** Creates a new instance of ProposalPrintingTxnBean */
    public ProposalPrintingTxnBean()
    {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    
    public int getVersion (String proposalNumber)
       throws CoeusException, DBException {
             
    int version = 0;
       
    Vector param = new Vector();
       
    param.addElement(new Parameter("VERSION", 
          DBEngineConstants.TYPE_INT, Integer.toString(version), 
          DBEngineConstants.DIRECTION_OUT));
    param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
        
    HashMap row = null;
    Vector result = new Vector();
                
    if(dbEngine !=null){
        result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER VERSION>>=call fn_Get_Version( <<PROPOSAL_NUMBER>> ) }", param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          version = Integer.parseInt(row.get("VERSION").toString());  
        }                
     return version;  
    }
           
    
    /** nih application extensions
     *  priorGrantNumber, nihActivityCode, nihApplicationTypeCode, inventions
     */
    public HashMap getNihGrant (String proposalNumber)
        throws CoeusException, DBException
    {
     String grantNumber;
     String activityCode;
     String appTypeCode;
     HashMap hashGrant = new HashMap();
     
      Vector result = new Vector(3,2);
      HashMap row = null;
      Vector param = new Vector();
      
       param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){ 
            result = dbEngine.executeRequest("Coeus",
            "call  GetNIHAward  ( <<PROPOSAL_NUMBER>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       
         int vecSize = result.size();
         //should just be one row
         if (vecSize >0){
           
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);
                //if NULL return Empty String
                grantNumber =  row.get("AWARD_NUMBER")== null ? "" : row.get("AWARD_NUMBER").toString();
                activityCode = row.get("ACTIVITY_CODE") == null ? "" :row.get("ACTIVITY_CODE").toString();
                appTypeCode = row.get("AWARD_TYPE") == null ? "" : row.get("AWARD_TYPE").toString();
                
                hashGrant.put("GRANTNUMBER",grantNumber);
                hashGrant.put("ACTIVITYCODE", activityCode);
                hashGrant.put("APPLICATIONTYPECODE", appTypeCode);
            }
        } 
         return hashGrant;
        
    }
  
  public String getInventions(String proposalNumber)
    throws CoeusException, DBException
  {
      String inventions = "X";
     
      Vector param = new Vector();
      HashMap row = null;
      Vector result = new Vector();
                       
      param.addElement(new Parameter("INVENTIONS", 
              DBEngineConstants.TYPE_STRING, inventions, 
              DBEngineConstants.DIRECTION_OUT));
   
      param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
  
       if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING INVENTIONS>>=call FnGetInventions( " + 
              " <<PROPOSAL_NUMBER>> ) }",
                param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          inventions = row.get("INVENTIONS").toString();  
        }                
      
   
     return inventions;  
    }
      
  public Date getProjectStartDt (String proposalNumber)
    throws CoeusException, DBException
    {
     
      Date startDt = null;
      
      Vector result = new Vector(3,2);
      HashMap row = null;
      Vector param = new Vector();
      
       param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
            "call  getTotalProjStartDt ( <<PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
      
        int vecSize = result.size();
         //should just be one row
        if (vecSize >0){
           for(int i=0;i<vecSize;i++){
              row = (HashMap) result.elementAt(i);          
              startDt = row.get("AWARD_EFFECTIVE_DATE")== null ? null :
                            new Date( ((Timestamp) row.get("AWARD_EFFECTIVE_DATE")).getTime());
                      
            }
        }
      return startDt;
    }
    
   public Date getProjectEndDt (String proposalNumber)
    throws CoeusException, DBException
    {
     
      Date endDt = null;
      
      Vector result = new Vector(3,2);
      HashMap row = null;
      Vector param = new Vector();
      
       param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
            "call  getTotalProjEndDt ( <<PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
      
        int vecSize = result.size();
         //should just be one row
        if (vecSize >0){
           for(int i=0;i<vecSize;i++){
              row = (HashMap) result.elementAt(i);          
              endDt = row.get("FINAL_EXPIRATION_DATE")== null ? null :
                            new Date( ((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime());
                      
            }
        }
      return endDt;
    }
    
    /** nsf extension
     *returns HashMap with total sal and wages, and total fringes
     */
    public HashMap getTotalSalaryAndWages (String proposalNumber, int version)
        throws CoeusException, DBException
    {
        BigDecimal salaryAndWages = new BigDecimal("0");
        BigDecimal fringe = new BigDecimal("0");
        HashMap hashTotal = new HashMap();
        
        
        Vector result = new Vector(3,2);
        HashMap row = null;
        Vector param = new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
        if(dbEngine !=null){
           
            result = dbEngine.executeRequest("Coeus",
            "call  getNSFTotalSalAndFringe ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        int vecSize = result.size();
         //should just be one row
        if (vecSize >0){
           
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);          
                salaryAndWages = new BigDecimal( row.get("SALARY").toString());
                fringe = new BigDecimal (row.get("FRINGE").toString());
                hashTotal.put("SALARY",salaryAndWages); 
                hashTotal.put("FRINGE", fringe);
            }
        }
        
        return hashTotal;
    }
    
 
   /** nsf extension
     *gets H4 answer (lobbying by investigators)
     */
    public String getH4LobbyQuestion (String proposalNumber)
        throws CoeusException, DBException
    {
        
        int count = 0;
        String answer;
        
        Vector result = new Vector(3,2);
        HashMap row = null;
        Vector param = new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
           
            result = dbEngine.executeRequest("Coeus",
            "call  GetH4LobbyQuestion ( <<PROPOSAL_NUMBER>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        int vecSize = result.size();
         //should just be one row
        if (vecSize >0){
           
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);          
                count = Integer.parseInt( row.get("COUNT").toString());             
            }
        }
        
        if (count > 0) {
            answer = "Y";}
        else {
            answer = "N";
        };
            
        return answer;
    }
    
 
   
    //nsf extension
    public Vector getAbstracts(String proposalNumber, int version)
       throws CoeusException, DBException
    {
        CoeusVector vecAbstractBeans = null;
        AbstractBean abstractBean = new AbstractBean();
        
         
        Vector result = new Vector(3,2);
        HashMap abstractRow = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
           
            result = dbEngine.executeRequest("Coeus",
            "call GetAbstracts ( <<PROPOSAL_NUMBER>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        int vecSize = result.size();
        vecAbstractBeans = new CoeusVector(); 
        if (vecSize >0){
           
            for(int i=0;i<vecSize;i++){
                abstractBean = new AbstractBean();
                abstractRow = (HashMap) result.elementAt(i);
                  
                abstractBean.setAbstractType
                     (Integer.parseInt(abstractRow.get("ABSTRACT_TYPE_CODE").toString())) ;
                abstractBean.setAbstractDescription(abstractRow.get("DESCRIPTION").toString());
                abstractBean.setAbstractText(abstractRow.get("ABSTRACT").toString());
                       
                vecAbstractBeans.add(abstractBean);
            }

        }
        
        //add budget justification as an abstract
        result = new Vector(3,2);
        abstractRow = null;
        param= new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,Integer.toString(version)));
   
        if(dbEngine !=null){
           
            result = dbEngine.executeRequest("Coeus",
            "call GetBudgetJustification ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        vecSize = result.size();
         
        if (vecSize >0){
           
            for(int i=0;i<vecSize;i++){
                abstractBean = new AbstractBean();
                abstractRow = (HashMap) result.elementAt(i);
                abstractBean.setAbstractType(Integer.parseInt("99")) ;
                abstractBean.setAbstractDescription("BUDGET JUSTIFICATION");
             
                String strJust;
                if (abstractRow.get("BUDGET_JUSTIFICATION") == null){
                    strJust = " ";
                } else {
                    strJust = abstractRow.get("BUDGET_JUSTIFICATION").toString();
                }
                 
                abstractBean.setAbstractText(strJust);
              
            }
            vecAbstractBeans.add(abstractBean);
        }
        
        
        return vecAbstractBeans;
    }
    
     /** getProposalPersons - returns vector of ProposalPersonBeans   
     *    - added as part of NSF extensions
      */
    public CoeusVector getProposalPersons (String proposalNumber,int version)
        throws CoeusException, DBException
    {  ProposalPersonBean proposalPersonBean;
       CoeusVector vecPropPersons = null;
       String personId;
       vecPropPersons = new CoeusVector();
       
        Vector result = new Vector(3,2);
        HashMap personsRow = null;
        Vector param = new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
           
            result = dbEngine.executeRequest("Coeus",
            "call GetProposalPersons ( <<PROPOSAL_NUMBER>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        int vecSize = result.size();
        
        if (vecSize >0){
            vecPropPersons = new CoeusVector();
            for(int i=0;i<vecSize;i++){
                proposalPersonBean = new ProposalPersonBean();
                personsRow = (HashMap) result.elementAt(i);
                
                personId = personsRow.get("PERSON_ID").toString();
                proposalPersonBean.setPersonId(personId);
                proposalPersonBean.setLastName(personsRow.get("LAST_NAME").toString());
                proposalPersonBean.setFirstName(personsRow.get("FIRST_NAME").toString() == null? " " :
                   personsRow.get("FIRST_NAME").toString());
                proposalPersonBean.setMiddleName(personsRow.get("MIDDLE_NAME").toString() == null? " " :
                    personsRow.get("MIDDLE_NAME").toString());
                proposalPersonBean.setPhone(personsRow.get("OFFICE_PHONE").toString() == null? " " :
                    personsRow.get("OFFICE_PHONE").toString());
                proposalPersonBean.setDegree(personsRow.get("DEGREE").toString() == null ? "Unknown" :
                     personsRow.get("DEGREE").toString());
                proposalPersonBean.setDob(personsRow.get("DATE_OF_BIRTH").toString() == null ? " ":
                    personsRow.get("DATE_OF_BIRTH").toString());
                proposalPersonBean.setEmail(personsRow.get("EMAIL_ADDRESS").toString() == null ? " ":
                    personsRow.get("EMAIL_ADDRESS").toString() );
                proposalPersonBean.setRole(personsRow.get("PROJECT_ROLE").toString() == null ? " ":
                    personsRow.get("PROJECT_ROLE").toString());
                //start addition for ssn
                proposalPersonBean.setSsn(personsRow.get("SSN").toString() == null ? " ":
                    personsRow.get("SSN").toString());
                //start case 1675
                //     proposalPersonBean.setPercentEffort(new BigDecimal(Integer.parseInt(
                //     personsRow.get("PERCENTAGE_EFFORT").toString())));
                proposalPersonBean.setPercentEffort(new BigDecimal(
                       personsRow.get("PERCENTAGE_EFFORT") == null ? "0" :   
                       personsRow.get("PERCENTAGE_EFFORT").toString()));         
//                //end case 1675
                //CHANGES FOR MAY 2006 - NIH NOW WANTS PERSON MONTHS INSTEAD OF PERCENT EFFORT.
                 proposalPersonBean.setFundingMonths(new BigDecimal (personsRow.get("CALENDAR_MONTHS") == null ? "0" :
                        personsRow.get("CALENDAR_MONTHS").toString()));
                if (personsRow.get("CALENDAR_MONTHS") != null)
                   
                proposalPersonBean.setAcademicFundingMonths(new BigDecimal (personsRow.get("ACADEMIC_MONTHS") == null ? "0" :
                        personsRow.get("ACADEMIC_MONTHS").toString()));         
                 proposalPersonBean.setSummerFundingMonths(new BigDecimal (personsRow.get("SUMMER_MONTHS") == null ? "0" :
                        personsRow.get("SUMMER_MONTHS").toString()));         
             
                // for each person, get salary and effort
                     
                CoeusVector vecPropSal = null;
                vecPropSal = new CoeusVector();
       
                Vector resultSal = new Vector(3,2);
                HashMap salRow = null;
                Vector paramSal = new Vector();
        
                paramSal.addElement(new Parameter("PROPOSAL_NUMBER",
                     DBEngineConstants.TYPE_STRING,proposalNumber));
                paramSal.addElement(new Parameter("VERSION_NUMBER", 
                     DBEngineConstants.TYPE_INT,Integer.toString(version)));
                paramSal.addElement(new Parameter("PERSON_ID", 
                     DBEngineConstants.TYPE_STRING,personId));
                
                if(dbEngine !=null){
           
                    resultSal = dbEngine.executeRequest("Coeus",
                        "call GetPropPersonSal ( <<PROPOSAL_NUMBER>> ,<<VERSION_NUMBER>> , <<PERSON_ID>> , " 
                        + " <<OUT RESULTSET rset>> )", "Coeus", paramSal);
                }else{
                        throw new CoeusException("db_exceptionCode.1000");
                }
         
                int vecSalSize = resultSal.size();
         
                if (vecSalSize >0){
                    vecPropSal = new CoeusVector();
                    for(int j=0; j < vecSalSize; j++){
                        salRow = (HashMap) resultSal.elementAt(j);
                  
//                        proposalPersonBean.setFundingMonths(new BigDecimal(
//                             (salRow.get("MONTHS").toString())));
                        proposalPersonBean.setRequestedCost(new BigDecimal(
                             (salRow.get("SALARY_REQUESTED") == null ? "0" :
                              salRow.get("SALARY_REQUESTED").toString())));         
                    }
                }
                
                vecPropPersons.add(proposalPersonBean);
            }        
        }
             
       return vecPropPersons;       
    }
    
    /** getNSFPreviousAward - NSF extension
     *
     */
    public String getNSFPreviousAward(String proposalNumber)
        throws CoeusException, DBException
    {
        String award = null;
        
        Vector result = new Vector(3,2);
        HashMap row = null;
        Vector param = new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
           
            result = dbEngine.executeRequest("Coeus",
            "call  GetNSFPrevAward ( <<PROPOSAL_NUMBER>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        int vecSize = result.size();
         
        if (vecSize >0){
           
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);
                
                award = row.get("AWARD").toString();
            }
        }
        
        return award;
    }
    
    /** getNSFProjectDuration - NSF extension
     *
     */
    public int getNSFProjectDuration(String proposalNumber)
        throws CoeusException, DBException
    {
        int duration = 0;
         Vector result = new Vector(3,2);
        HashMap row = null;
        Vector param = new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
           
            result = dbEngine.executeRequest("Coeus",
            "call  GetNSFDuration ( <<PROPOSAL_NUMBER>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        int vecSize = result.size();
         
        if (vecSize >0){
           
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);
                
                duration = Integer.parseInt(row.get("DURATION").toString());
            }
        }
         
        return duration;
    }
    
      public CoeusVector getAllNSFSeniorPersonnel (String proposalNumber,int version)
            throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
      /** get all Senior Personnel in the entire budget - NSF extension
        */
        
        CoeusVector vecSeniors = new CoeusVector();
        CoeusVector vecSeniorsWithEffort = new CoeusVector();
        
        vecSeniors = getSeniorPersonnel(proposalNumber,0);
        
        /*  vecSeniors is a vector containing NSFSeniorPersonnelBeans for all periods         
        *  get the effort and funding for all periods for each person */
            
         NSFSeniorPersonnelBean nsfSeniorPersonnelBean = new NSFSeniorPersonnelBean();
         NSFSeniorPersonnelBean nsfSeniorPersonnelBeanWithEffort = new NSFSeniorPersonnelBean();
         String personId;
 
         int countSeniors = vecSeniors.size();
                          
         if (countSeniors > 0){
             for(int srIndex=0; srIndex < countSeniors; srIndex++){
                //for each Senior person, get the effort for all periods , denoted by period =0
                nsfSeniorPersonnelBean = (NSFSeniorPersonnelBean) vecSeniors.elementAt(srIndex);
                personId = nsfSeniorPersonnelBean.getPersonId();
                nsfSeniorPersonnelBeanWithEffort = getEffort(proposalNumber, version,
                                                0, personId, nsfSeniorPersonnelBean);
                //add to  vector of seniors for this period
                vecSeniorsWithEffort.add(srIndex, nsfSeniorPersonnelBeanWithEffort); 
                }
             }
        
       return vecSeniorsWithEffort;
      }
     
    public String getPersonOrg (String proposalNumber, String personId)
           throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        String org = "  ";
         
       Vector param = new Vector();
       
       param.addElement(new Parameter("ORGANIZATION", 
              DBEngineConstants.TYPE_STRING, org, DBEngineConstants.DIRECTION_OUT));
   
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
       param.addElement(new Parameter("PERSON_ID",
                     DBEngineConstants.TYPE_STRING,  personId ));
       HashMap row = null;
       Vector result = new Vector();
                
       if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING ORGANIZATION>>=call GetPersonOrg( " + 
              " <<PROPOSAL_NUMBER>> ,<<PERSON_ID>>) }",
                param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          org = (String) row.get("ORGANIZATION");  
        }                

        return org;
    }
    
   
    /** get CoreBudgetTotal info
     *  returns HashMap with  total cost, cost sharing, total direct, total indirect
     */
        
    public HashMap getCoreBudgetTotal(String proposalNumber, int version)
        throws CoeusException, DBException
    {
        BigDecimal totalCost = new BigDecimal("0");
        BigDecimal totalDirectCost = new BigDecimal("0");
        BigDecimal totalIndirectCost = new BigDecimal("0");
        BigDecimal totalCostSharing = new BigDecimal("0");
        HashMap hashTotal = new HashMap();
        
        Vector result = new Vector(3,2);
        HashMap row = null;
        Vector param = new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
        if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  get_tot_bud_info_for_prnt ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        int vecSize = result.size();
         //should just be one row
        if (vecSize >0){
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);          
                totalCost = new BigDecimal( row.get("TOTAL_COST").toString());
                totalIndirectCost = new BigDecimal (row.get("TOTAL_INDIRECT_COST").toString());
                totalDirectCost = new BigDecimal (row.get("TOTAL_DIRECT_COST").toString());
                totalCostSharing = new BigDecimal (row.get("COST_SHARING_AMOUNT").toString());
          
                hashTotal.put("TOTAL_COST",totalCost); 
                hashTotal.put("TOTAL_INDIRECT_COST", totalIndirectCost);
                hashTotal.put("TOTAL_DIRECT_COST",totalDirectCost); 
                hashTotal.put("COST_SHARING_AMOUNT", totalCostSharing);
            }
        }
        
        return hashTotal;
    }
    
    
    /** getModularQuestion
     *
     */
     public boolean getModularQuestion (String proposalNumber, int versionNumber)
        throws CoeusException, DBException
    {
        
        int count = 0;
        String answer="N";
        
        Vector result = new Vector(3,2);
        HashMap row = null;
        Vector param = new Vector();
        
        param.addElement(new Parameter("MODULAR_BUDGET_FLAG", 
              DBEngineConstants.TYPE_STRING, answer, 
              DBEngineConstants.DIRECTION_OUT));
        param.addElement(new Parameter("PROPOSAL_NUMBER",
             DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement( new Parameter("VERSION_NUMBER", 
             DBEngineConstants.TYPE_INT, ( Integer.toString(versionNumber))));
        
       
         if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING MODULAR_BUDGET_FLAG>>=call GetModularQuestion( " + 
              " <<PROPOSAL_NUMBER>> ,<<VERSION_NUMBER>> ) }",
                param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          answer = row.get("MODULAR_BUDGET_FLAG").toString();  
        }    
      
        if (answer.equals ("Y")){
            return true;
        }else return false;
     
    }
    
     
    /**  getBudgetPrintingInfo - returns vector of BudgetPeriodPrintingBeans
     *
     */
   
    public CoeusVector getBudgetPrintingInfo(String proposalNumber,int version,
                                             String sponsor)
        throws CoeusException , DBException
    {   int  budgetPeriod;
        String personId;
        CoeusVector vecCosts = null;
        vecCosts = new CoeusVector();
        NSFOtherPersonnelBean  otherPersonnelBean = null;
        NSFSeniorPersonnelBean  seniorPersonnelPeriodBean = null;
        //holds vector of NSF Senior personnel in proposal in a period
        CoeusVector vecSeniors = null;
        //holds vector of NSF Senior personnel with effort in a period 
        CoeusVector vecSeniorsWithEffort = null;  
             
      /** get vector of CostBeans -  containing cost information 
      *  for budget categories (e.g. equipment, travel) for final (or latest) version 
      *  in all budget periods...and add to queryEngine
      */
      
       vecCosts = getSpecificCost(proposalNumber, version, sponsor);
       String key  = proposalNumber;
       Hashtable hashCosts = new Hashtable();
       hashCosts.put(CostBean.class, vecCosts);
       
       queryEngine.addDataCollection(key,  hashCosts);

     
     
        /* get basic period information.  
         * one row for each period in final version (or latest version if no final)
        */
        BudgetPeriodPrintingBean budgetPeriodBean = new BudgetPeriodPrintingBean();
        CoeusVector vecBudgetPeriodBeans = null;
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
          
        if(dbEngine !=null){
           
            result = dbEngine.executeRequest("Coeus",
            "call GET_BUD_PERIODS_FOR_PRNT ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        //get period information
        int budPeriod = result.size();
         
        if (budPeriod >0){
                //case 1751 - get nih sponsor code
               String nihCode = getNIHSponsorCode();
              
            vecBudgetPeriodBeans = new CoeusVector();
            for(int rowIndex=0;rowIndex<budPeriod;rowIndex++){
                budgetPeriodBean = new BudgetPeriodPrintingBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                
                budgetPeriod = Integer.parseInt(budgetRow.get(
                    "BUDGET_PERIOD") == null ? "0" : budgetRow.get(
                    "BUDGET_PERIOD").toString()); 
                        
                /** set budgetPeriodBean fields from database call
                 */
               
                budgetPeriodBean.setFinalVersionFlag(budgetRow.get("FINAL_VERSION_FLAG").toString());                  
               
                budgetPeriodBean.setProposalNumber(proposalNumber);
               
                budgetPeriodBean.setBudgetPeriod(budgetPeriod);                  
                
                budgetPeriodBean.setVersion(version);
               
                budgetPeriodBean.setStartDate(
                    budgetRow.get("START_DATE") == null ? null
                            :new Date( ((Timestamp) budgetRow.get(
                                "START_DATE")).getTime()));
               
                budgetPeriodBean.setEndDate(
                    budgetRow.get("END_DATE") == null ? null
                            :new Date( ((Timestamp) budgetRow.get(
                                "END_DATE")).getTime()));  
                
                budgetPeriodBean.setPeriodCostsTotal(
                    Double.parseDouble(budgetRow.get(
                    "TOTAL_COST") == null ? "0" : budgetRow.get(
                    "TOTAL_COST").toString()));                
             
                budgetPeriodBean.setPeriodDirectCostsTotal(
                    Double.parseDouble(budgetRow.get(
                    "TOTAL_DIRECT_COST") == null ? "0" : budgetRow.get(
                    "TOTAL_DIRECT_COST").toString()));  
                   
                budgetPeriodBean.setIndirectCostsTotal(
                    Double.parseDouble(budgetRow.get(
                    "TOTAL_INDIRECT_COST") == null ? "0" : budgetRow.get(
                    "TOTAL_INDIRECT_COST").toString()));                                                    
               
                budgetPeriodBean.setCostSharingAmt(
                    Double.parseDouble(budgetRow.get(
                    "COST_SHARING_AMOUNT") == null ? "0" : budgetRow.get(
                    "COST_SHARING_AMOUNT").toString()));                                                                        
              
                budgetPeriodBean.setUnderrecoveryAmt(
                    Double.parseDouble(budgetRow.get(
                    "UNDERRECOVERY_AMOUNT") == null ? "0" : budgetRow.get(
                    "UNDERRECOVERY_AMOUNT").toString()));  
        
                /** get modular budget amount
                */
           
                Vector modParam= new Vector();
                double modAmount = 0;
        
                /* call stored procedure to get modular amount 
                */
       
                modParam.addElement( new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, proposalNumber));
                modParam.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
                modParam.addElement(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
      
                HashMap resultRow = null;
                Vector modResult = new Vector();
        
                if(dbEngine !=null){
                   modResult = dbEngine.executeRequest("Coeus",
                  "call GetModularAmount ( <<PROPOSAL_NUMBER>>  ,<<VERSION_NUMBER>> , "
                  + " <<BUDGET_PERIOD>> , <<OUT RESULTSET rset>> )", "Coeus", modParam);           
                     
                 }else{
                    throw new CoeusException("db_exceptionCode.1000");
                 }    
                 
                 if (modResult.size() > 0 ) {
                     resultRow = (HashMap) modResult.elementAt(0);
                     modAmount = Double.parseDouble(resultRow.get("MODAMOUNT") == null ? "0" :
                                 resultRow.get("MODAMOUNT").toString());
                 }
                
                     
                budgetPeriodBean.setModularPeriodAmount(modAmount);
               
                /** get Consortium costs - (subcontracts) for this period
                 *
                */  
                Vector result2 = new Vector(3,2);
                HashMap budgetRow2 = null;
                Vector param2 = new Vector();
                 
                param2.add(new Parameter("PROPOSAL_NUMBER",
                     DBEngineConstants.TYPE_STRING,proposalNumber));
                param2.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
                param2.add(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT,Integer.toString(budgetPeriod)));
                param2.addElement(new Parameter("SPONSOR",
                     DBEngineConstants.TYPE_STRING,sponsor));
          
                if(dbEngine !=null){
           
                  result2 = dbEngine.executeRequest("Coeus",
                  "call GET_CONSORTIUM_COSTS ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>> , "
                  + " <<BUDGET_PERIOD>> , <<SPONSOR>> , <<OUT RESULTSET rset>> )",
                     "Coeus", param2);
                }else{
                    throw new CoeusException("db_exceptionCode.1000");
                 }
              
                 int recCount2 = result2.size();
                 if (recCount2 > 0){
                   //THERE SHOULD BE ONLY ONE ROW RETURNED, WHICH IS THE SUM OF THE
                   //CONSORTIUM COSTS FOR THIS PERIOD
               
                    budgetRow2 = (HashMap) result2.elementAt(0);
                   
               //    budgetPeriodBean.setConsortiumDirectCosts(
                     budgetPeriodBean.setConsortiumTotalCosts(
                      Double.parseDouble(budgetRow2.get(
                      "CONSORTIUM") == null ? "0" : budgetRow2.get(
                      "CONSORTIUM").toString()));
                 }
                 
               
                    
              /** consortium indirect - this is F and A for the subcontractor, which
                  is a direct cost for us -
              */
                 
                Vector resultInd = new Vector(3,2);
                HashMap budgetRowInd = null;
                Vector paramInd = new Vector();
                 
                paramInd.add(new Parameter("PROPOSAL_NUMBER",
                     DBEngineConstants.TYPE_STRING,proposalNumber));
                paramInd.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
                paramInd.add(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT,Integer.toString(budgetPeriod)));
                paramInd.addElement(new Parameter("SPONSOR",
                     DBEngineConstants.TYPE_STRING,sponsor));
          
                if(dbEngine !=null){
           
                  resultInd = dbEngine.executeRequest("Coeus",
                  "call GET_CONSORTIUM_FANDA_COSTS ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>> , "
                  + " <<BUDGET_PERIOD>> , <<SPONSOR>> , <<OUT RESULTSET rset>> )",
                     "Coeus", param2);
                }else{
                    throw new CoeusException("db_exceptionCode.1000");
                 }
              
                 int recCountInd = resultInd.size();
                 if (recCountInd > 0){
                  
                    budgetRowInd = (HashMap) resultInd.elementAt(0);
                   
                    budgetPeriodBean.setConsortiumIndirectCosts(Double.parseDouble(budgetRowInd.get(
                      "CONSORTIUM") == null ? "0" : budgetRowInd.get(
                      "CONSORTIUM").toString()));
                 }
                 
               
                 budgetPeriodBean.setConsortiumDirectCosts(budgetPeriodBean.getConsortiumTotalCosts() -
                                                           budgetPeriodBean.getConsortiumIndirectCosts());
                 
              
               /**nonconsortium direct cost subtotal 
                *this is sum of direct costs minus  consortium (subcontract) costs  	
              */
              budgetPeriodBean.setNonConsortiumDirectCostSubtotal(
                                      budgetPeriodBean.getPeriodDirectCostsTotal() - 
                                      budgetPeriodBean.getConsortiumTotalCosts());

         
               /** equipmentCosts -  filter the collection of costBeans
                * in the queryEngine for this period, and cost type
                */
                                     
                Equals equalsEquip = new Equals("budgetCategoryCode", 
                                                CategoryCostTypeConstants.EQUIPMENT);                            
                Equals equalsPeriod = new Equals("budgetPeriod", new Integer(budgetPeriod ));
                And equip = new And(equalsEquip, equalsPeriod);
              
      //         CoeusVector cvEquipment = queryEngine.getActiveData(key,  CostBean.class, equip);   
                CoeusVector cvEquipment = vecCosts.filter(equip );                   
                //cvEquipment is a vector of equipment CostBeans
                budgetPeriodBean.setEquipmentCosts(cvEquipment);                     
                  
                /** equipCoeusVectorment total/
                */
                double totEquip = cvEquipment.sum("cost");
                budgetPeriodBean.setEquipmentTotal(totEquip);
          
                /**travel costs
                */
                Equals equalsTravel = new Equals("budgetCategoryCode", CategoryCostTypeConstants.TRAVEL);
                //necessary for nsf
                Equals equalsForeignTravel = new Equals("budgetCategoryCode", CategoryCostTypeConstants.FOREIGNTRAVEL);
                
                Or travel = new Or(equalsTravel, equalsForeignTravel);
                And periodTravel = new And (travel,equalsPeriod);
                
                CoeusVector cvTravel = queryEngine.getActiveData(key, CostBean.class, periodTravel);
                //cvTravel is a vector of travel CostBeans
                budgetPeriodBean.setTravelCosts(cvTravel);

                /** travel cost total/
                */
                double travelCosts = cvTravel.sum("cost");
                budgetPeriodBean.setTravelTotal(travelCosts);
          
     
                /** participant costs - for nsf
                 * or patient costs for nih
                */
              
                //if sponsor is NIH , we want INPATIENT and OUTPATIENT
                //otherwise , we want participant stuff
                
      
                Equals equalsPartStipends = new Equals("budgetCategoryCode", 
                                CategoryCostTypeConstants.PARTICIPANT_STIPENDS);
                Equals equalsPartTravel = new Equals("budgetCategoryCode", 
                                CategoryCostTypeConstants.PARTICIPANT_TRAVEL);
                Equals equalsPartOther = new Equals("budgetCategoryCode", 
                                CategoryCostTypeConstants.PARTICIPANT_OTHER);
                Equals equalsPartSub = new Equals("budgetCategoryCode",
                                CategoryCostTypeConstants.PARTICIPANT_SUB);
                Equals equalsInpatient = new Equals("budgetCategoryCode", 
                                CategoryCostTypeConstants.INPATIENT);
                Equals equalsOutpatient = new Equals("budgetCategoryCode", 
                                CategoryCostTypeConstants.OUTPATIENT);
                
                Or stipendsOrTravel = new Or (equalsPartStipends, equalsPartTravel);
                Or stipendsOrTravelOrOther = new Or (stipendsOrTravel, equalsPartOther);
                Or stipendsOrTravelOrOtherOrSub = new Or (stipendsOrTravelOrOther, equalsPartSub);
                        
                Or inpatientOrOutpatient = new Or(equalsInpatient,  equalsOutpatient);
                And partCosts;
                
                if (sponsor.equals(nihCode)){
                    partCosts = new And (inpatientOrOutpatient, equalsPeriod);
                }else { //NSF and others
                 //   partCosts = new And (stipendsOrTravelOrOther, equalsPeriod);   
                      partCosts = new And (stipendsOrTravelOrOtherOrSub, equalsPeriod); 
                }   
                CoeusVector cvPart = queryEngine.getActiveData(key, CostBean.class, partCosts);
                budgetPeriodBean.setParticipantCosts(cvPart);
              
                               
               /** participant or patient cost total/
               */
                double ppCosts = cvPart.sum("cost");
                budgetPeriodBean.setParticipantPatientTotal(ppCosts);
            
      
               /** otherDirect costs - everything except equipment , travel,
               *   participant costs, inpatent or outpatient
               */
                
             
                NotEquals notEquip = new NotEquals("budgetCategoryCode", 
                         CategoryCostTypeConstants.EQUIPMENT);
                NotEquals notTravel = new NotEquals("budgetCategoryCode", 
                         CategoryCostTypeConstants.TRAVEL);
                NotEquals notForTravel = new NotEquals("budgetCategoryCode", 
                         CategoryCostTypeConstants.FOREIGNTRAVEL);
                NotEquals notPartStipends = new NotEquals("budgetCategoryCode", 
                         CategoryCostTypeConstants.PARTICIPANT_STIPENDS);
                NotEquals notPartTravel = new NotEquals("budgetCategoryCode", 
                         CategoryCostTypeConstants.PARTICIPANT_TRAVEL);
                NotEquals notPartOther = new NotEquals("budgetCategoryCode", 
                         CategoryCostTypeConstants.PARTICIPANT_OTHER);
                NotEquals notPartSub = new NotEquals("budgetCategoryCode",
                         CategoryCostTypeConstants.PARTICIPANT_SUB);
                NotEquals notInpatient = new NotEquals("budgetCategoryCode", 
                         CategoryCostTypeConstants.INPATIENT);
                NotEquals notOutpatient = new NotEquals("budgetCategoryCode", 
                         CategoryCostTypeConstants.OUTPATIENT);
                
                And notAnyTravel = new And(notTravel, notForTravel);
                And notEquipOrTravel = new And (notEquip, notAnyTravel);
                And notEquipOrTravelOrPartStip = new And (notEquipOrTravel, notPartStipends);
                And notEquipOrTravelOrStipOrPartTravel = 
                                        new And (notEquipOrTravelOrPartStip, notPartTravel);
               
                And notEquipOrTravelOrStip = new And(notEquipOrTravelOrStipOrPartTravel, notPartOther);
                And notEquipOrTravelOrStipOrSub = new And(notEquipOrTravelOrStip, notPartSub);
                
        //      And notEquipOrTravelOrStipOrInpatient = new And (notEquipOrTravelOrStip, notInpatient);
                And notEquipOrTravelOrStipOrInpatient = new And (notEquipOrTravelOrStipOrSub, notInpatient);
                And notEquipOrTravelOrStipOrPatient = new And (notEquipOrTravelOrStipOrInpatient, notOutpatient);
                
                And periodOtherDirect = new And (notEquipOrTravelOrStipOrPatient,equalsPeriod);
                CoeusVector cvOtherDirect =
                     queryEngine.getActiveData(key, CostBean.class, periodOtherDirect);
                
          
                //cvOtherDirect is a vector of otherDirect CostBeans
                budgetPeriodBean.setOtherDirectCosts(cvOtherDirect);
                
                
                /** other direct cost total/
                */
                double totOtherCosts = cvOtherDirect.sum("cost");
                budgetPeriodBean.setOtherDirectTotal(totOtherCosts);
                 
                //NSF extension - nsf total other direct costs
                // to get total costs, get the vector of beans in the queryEngine 
                   
                Equals equalsOtherType = new Equals("categoryType",
                                CategoryCostTypeConstants.NSF_OTHER_DIRECT);
                And allOtherDirect = new And (equalsOtherType, equalsPeriod);
                CoeusVector cvAllOther = queryEngine.getActiveData(key, CostBean.class, allOtherDirect);
                System.out.println("size of vector of all other direct is " + cvAllOther.size());
                
                double totalNSFOtherDirectCost = cvAllOther.sum("cost");
                budgetPeriodBean.setNSFTotalOtherDirectCosts(totalNSFOtherDirectCost);
                         
                
                /** salary and wages
                */
               CoeusVector cvSalary = this.getBudgetPersonsInfo(proposalNumber, version, budgetPeriod);
               budgetPeriodBean.setSalaryAndWages(cvSalary);
               
             
                /** total salary and wages and fringe
                */
                double total = cvSalary.sum("total");
                 System.out.println("total  is" + total);
             
                 budgetPeriodBean.setTotalSalaryAndWages( total);
            
                /** total salary requestedgetBudgetPersonsInfo
                 *  this is total salary and wages
                */
                 double totSalReq = cvSalary.sum("salaryRequested");
                 System.out.println("total salary and wages is" + totSalReq);
            
                 budgetPeriodBean.setTotSalaryRequested(totSalReq);
             
                /**total fringe
                 */
                double totFringe = cvSalary.sum("fringe");
                 budgetPeriodBean.setTotFringe(totFringe);
                     
                /** indirect cost details
                 */
  //              budgetPeriodBean.setIndirectCostDetails(this.getIndirectCostDetails(proposalNumber));
                 
              
               /** NSF extension - get senior personnel for this period
                */ 
               vecSeniors = new CoeusVector();
               vecSeniorsWithEffort = new CoeusVector();
               
               vecSeniors = getSeniorPersonnel (proposalNumber,budgetPeriod);
              /*  vecSeniors is a vector of SeniorPersonnelBeans containing personIds and names
               *  get the effort and funding for this period for each person */
            
               int countSeniors = vecSeniors.size();
                          
               if (countSeniors > 0){
                for(int srIndex=0; srIndex < countSeniors; srIndex++){
                    //for each Senior person, get the effort for this period
                    seniorPersonnelPeriodBean = (NSFSeniorPersonnelBean) vecSeniors.elementAt(srIndex);
                    personId = seniorPersonnelPeriodBean.getPersonId();
                    seniorPersonnelPeriodBean = getEffort(proposalNumber, version, 
                                                  budgetPeriod, personId, seniorPersonnelPeriodBean);
                    //add to  vector of seniors for this period
                    vecSeniorsWithEffort.add(srIndex, seniorPersonnelPeriodBean); 
                }
             }
             //vecSeniorsWithEffort now has complete seniorPersonnelBeans for this period
            
             budgetPeriodBean.setNSFSeniorPersonnel(vecSeniorsWithEffort);
             
             /** NSF extension - get other personnel. 
              *  
             */ 
             otherPersonnelBean = new NSFOtherPersonnelBean();
             otherPersonnelBean = getOtherPersonnel(proposalNumber, version, budgetPeriod, sponsor);
            
             budgetPeriodBean.setNSFOtherPersonnel(otherPersonnelBean);
             
           
             vecBudgetPeriodBeans.add(budgetPeriodBean);
             
            }
        }
        return vecBudgetPeriodBeans;
        
    }
    
    /*case 2406*/
    public HashMap getOrganizationID(String propNumber,String orgType)
            throws CoeusException, DBException {
   
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
               "call s2sPackage.getOrganization( <<PROPOSAL_NUMBER>> ,<<ORG_TYPE>> , "
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
           
    
    /** get Organizational contact person
     */
    public DepartmentPersonFormBean getOrgContactPerson (String proposalNumber)
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
                "call GET_ORG_CONTACT ( <<PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> )",         
                                               "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }   
                  
         DepartmentPersonFormBean orgContactBean = new DepartmentPersonFormBean();
             
         int listSize = result.size();
         if (listSize > 0){
           for(int index=0; index < listSize; index++){
                row = (HashMap)result.elementAt(index);
                orgContactBean.setPersonId( (String) row.get("PERSON_ID"));
                orgContactBean.setPrimaryTitle( (String) row.get("PRIMARY_TITLE") == null ?
                         " " : (String) row.get("PRIMARY_TITLE")) ;
                orgContactBean.setEmailAddress( (String) row.get("EMAIL_ADDRESS")== null ?
                         " " : (String) row.get("EMAIL_ADDRESS")) ;
                orgContactBean.setFirstName((String) row.get("FIRST_NAME")== null ?
                         " " : (String) row.get("FIRST_NAME")) ;
                orgContactBean.setFullName((String) row.get("FULL_NAME")== null ?
                         " " : (String) row.get("FULL_NAME")) ;
                orgContactBean.setLastName((String) row.get("LAST_NAME")== null ?
                         " " : (String) row.get("LAST_NAME")) ;
                orgContactBean.setMiddleName((String) row.get("MIDDLE_NAME")== null ?
                         " " : (String) row.get("MIDDLE_NAME")) ;
                orgContactBean.setOfficePhone((String) row.get("OFFICE_PHONE")== null ?
                         " " : (String) row.get("OFFICE_PHONE")) ;
                orgContactBean.setOfficeLocation((String) row.get("OFFICE_LOCATION")== null ?
                         " " : (String) row.get("OFFICE_LOCATION")) ;
                 // changes added on mar 23, 2006
                if (row.get("ADDRESS_LINE_1") != null) orgContactBean.setAddress1(row.get("ADDRESS_LINE_1").toString());
                if (row.get("ADDRESS_LINE_2") != null)orgContactBean.setAddress2(row.get("ADDRESS_LINE_2").toString());
                if (row.get("ADDRESS_LINE_3") != null) orgContactBean.setAddress3(row.get("ADDRESS_LINE_3").toString());
                orgContactBean.setCity((String) row.get("CITY") == null ?
                        "Unknown" : (String) row.get("CITY"));
                if (row.get("STATE") != null ) orgContactBean.setState(row.get("STATE").toString());
                orgContactBean.setCountryCode((String) row.get("COUNTRY_CODE") == null ?
                        "Unknown" : (String) row.get("COUNTRY_CODE"));
                orgContactBean.setPostalCode((String) row.get("POSTAL_CODE") == null ?
                        "Unknown" : (String) row.get("POSTAL_CODE"));
                
               
           }
         }
         
     return orgContactBean;
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
    
    /** getOtherPersonnel - NSF Extension - returns NSFOtherPersonnelBean
    */
    private NSFOtherPersonnelBean getOtherPersonnel (String proposalNumber,
                                            int version, int budgetPeriod, String sponsor)
       throws CoeusException, DBException
    {  
       NSFOtherPersonnelBean otherPersonnelBean = new NSFOtherPersonnelBean();
       //initialize bean
       otherPersonnelBean.setClericalCount(new BigInteger("0"));
       otherPersonnelBean.setClericalFunds(new BigDecimal("0"));
       otherPersonnelBean.setGradCount(new BigInteger("0"));
       otherPersonnelBean.setGradFunds(new BigDecimal("0"));
       otherPersonnelBean.setOtherCount(new BigInteger("0"));
       otherPersonnelBean.setOtherFunds(new BigDecimal("0"));
       otherPersonnelBean.setOtherProfCount(new BigInteger("0"));
       otherPersonnelBean.setOtherProfFunds(new BigDecimal("0"));
       otherPersonnelBean.setPostDocCount(new BigInteger("0"));
       otherPersonnelBean.setPostDocFunds(new BigDecimal("0"));
       otherPersonnelBean.setUnderGradCount(new BigInteger("0"));
       otherPersonnelBean.setUnderGradFunds(new BigDecimal("0"));
       otherPersonnelBean.setOtherLAFunds(new BigDecimal("0"));
       
       //changes made to add cents to budget form for nsf
       BigDecimal salary = new BigDecimal("0");
       int count = 0;
       BigDecimal cost = new BigDecimal("0");
       
       salary = getOtherPersonSalary(proposalNumber, version,  budgetPeriod, "01-Graduates",sponsor);
       count = getOtherPersonCount(proposalNumber, version,  budgetPeriod, "01-Graduates",sponsor);
       otherPersonnelBean.setGradFunds(salary);
       otherPersonnelBean.setGradCount(new BigInteger(String.valueOf(count)));
       
       salary = getOtherPersonSalary(proposalNumber, version,  budgetPeriod, "01-PostDocs",sponsor);
       count = getOtherPersonCount(proposalNumber, version,  budgetPeriod, "01-PostDocs",sponsor);
       otherPersonnelBean.setPostDocFunds(salary);
       otherPersonnelBean.setPostDocCount(new BigInteger(String.valueOf(count)));
       
       salary = getOtherPersonSalary(proposalNumber, version,  budgetPeriod, "01-Other Profs",sponsor);
       count = getOtherPersonCount(proposalNumber, version,  budgetPeriod, "01-Other Profs",sponsor);
       otherPersonnelBean.setOtherProfFunds(salary);
       otherPersonnelBean.setOtherProfCount(new BigInteger(String.valueOf(count)));
       
       salary = getOtherPersonSalary(proposalNumber, version,  budgetPeriod, "01-Undergrads",sponsor);
       count = getOtherPersonCount(proposalNumber, version,  budgetPeriod, "01-Undergrads",sponsor);
       otherPersonnelBean.setUnderGradFunds(salary);
       otherPersonnelBean.setUnderGradCount(new BigInteger(String.valueOf(count)));
       
       salary = getOtherPersonSalary(proposalNumber, version,  budgetPeriod, "01-Secretarial",sponsor);
       count = getOtherPersonCount(proposalNumber, version,  budgetPeriod, "01-Secretarial",sponsor);
       otherPersonnelBean.setClericalFunds(salary);
       otherPersonnelBean.setClericalCount(new BigInteger(String.valueOf(count)));
       
       salary = getOtherPersonSalary(proposalNumber, version,  budgetPeriod,  "01-Other",sponsor);
       count = getOtherPersonCount(proposalNumber, version,  budgetPeriod,  "01-Other",sponsor);
       otherPersonnelBean.setOtherFunds(salary);
       otherPersonnelBean.setOtherCount(new BigInteger(String.valueOf(count)));
   
       cost = getMrLA(proposalNumber,version,budgetPeriod);
       otherPersonnelBean.setOtherLAFunds(cost);
       
       return otherPersonnelBean;
        
        
    }
            
   
     /**************************************************
      * getOtherPersonSalary
      * replaced call to function fnGetOtherPersonnel with call to procedure
      * getOtherPersonnelSal in order to get back a decimal rather than integer
      *
      */
     private BigDecimal getOtherPersonSalary(String proposalNumber,int version,
                                        int budgetPeriod,String category, String sponsor)
                  throws CoeusException, DBException
    {
       BigDecimal salary = new BigDecimal("0");
       Vector result = null;
       Vector param = new Vector();
       HashMap row = null;
       
   
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
       param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
       param.addElement(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
       param.addElement(new Parameter("CATEGORY",
                     DBEngineConstants.TYPE_STRING,  category ));
       param.addElement(new Parameter("SPONSOR",
                     DBEngineConstants.TYPE_STRING,  sponsor ));
    
   
        
       if(dbEngine !=null){
          result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
                "call getOtherPersonnelSal ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>> , "
                  + " <<BUDGET_PERIOD>> , <<CATEGORY>> , <<SPONSOR>> ,  <<OUT RESULTSET rset>> )",         
                                               "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }    
          
         int listSize = result.size();
         if (listSize > 0){
           for(int index=0; index < listSize; index++){
                row = (HashMap)result.elementAt(index);
              
                salary = new BigDecimal ( row.get("SALARY") == null ? "0" :
                                          row.get("SALARY").toString());
           }
         }
                
     return salary;  
    }
    
      private int getOtherPersonCount(String proposalNumber,int version,
                                        int budgetPeriod,String category, String sponsor)
                         throws CoeusException, DBException
    {
       int count = 0;    
       
       Vector param = new Vector();
       
       param.addElement(new Parameter("COUNT", 
              DBEngineConstants.TYPE_INT, Integer.toString(count), DBEngineConstants.DIRECTION_OUT));
     
       param.add( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
       param.add(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
       param.add(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
       param.add(new Parameter("CATEGORY",
                     DBEngineConstants.TYPE_STRING,  category ));
       param.add(new Parameter("SPONSOR", 
                     DBEngineConstants.TYPE_STRING, sponsor ));
                 
       
       HashMap otherPersonsRow = null;
       Vector result = new Vector();
        
       if(dbEngine !=null){
    
           
            result = dbEngine.executeFunctions("Coeus",
              "{<<OUT INTEGER COUNT>>=call fnGetCountOtherPersonnel( " + 
              " <<PROPOSAL_NUMBER>> ,<<VERSION_NUMBER>> , <<BUDGET_PERIOD>> ,<<CATEGORY>> , <<SPONSOR>> )}",
                param);      
             
   
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       
        if(!result.isEmpty()){
          otherPersonsRow = (HashMap)result.elementAt(0);
          count = Integer.parseInt(otherPersonsRow.get("COUNT").toString());
      
        }         
       
     return count;  
    }
    
      
    /**************************
     * getMrLA
     * replaced call to function fnGetOMrLA with call to procedure
     *  getMrLA in order to get back a decimal rather than integer
     *
     */

      private BigDecimal getMrLA(String proposalNumber,int version, int budgetPeriod)
                         throws CoeusException, DBException
    {      

       BigDecimal mrLA = new BigDecimal("0");
       Vector param = new Vector();
                 
       
    
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
       param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
       param.addElement(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
      
        HashMap row = null;
        Vector result = new Vector();
                
       if(dbEngine !=null){
    
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call getMrLA ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>> , "
                  + " <<BUDGET_PERIOD>> , <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        
  
         int listSize = result.size();
         if (listSize > 0){
           for(int index=0; index < listSize; index++){
                row = (HashMap)result.elementAt(index);     
                mrLA = new BigDecimal ( row.get("MRLA") == null ? "0" :
                                          row.get("MRLA").toString());
           }
         }
        
    
      return mrLA;  
    }
       
    /** getSeniorPersonInfo - returns vector of NSFSeniorPersonnelBeans
     *  with senior personnel list in the proposal for each period.
     * if period = 0, then get everyone for all periods
    */
    private CoeusVector getSeniorPersonnel (String proposalNumber, int budgetPeriod)
       throws CoeusException, DBException
    {  NSFSeniorPersonnelBean  seniorPersonnelBean = null;
       CoeusVector vecSeniors = null;
       vecSeniors = new CoeusVector();
       String personId;
      
       double months = 0.0;
       double fundsRequested = 0.0;
        
       
       Vector result = null;
       Vector param = new Vector();
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
       param.addElement(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
   
       HashMap seniorPersonsRow = null;
       if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GetNsfSeniorPersonel ( <<PROPOSAL_NUMBER>> , <<BUDGET_PERIOD>> , <<OUT RESULTSET rset>> )", 
                             "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       
        int listSize = result.size();
        if (listSize > 0){
           
           
         
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                seniorPersonsRow = (HashMap)result.elementAt(rowIndex);
                seniorPersonnelBean = new NSFSeniorPersonnelBean();
                seniorPersonnelBean.setRowNumber(rowIndex);
                seniorPersonnelBean.setBudgetPeriod(budgetPeriod);
                seniorPersonnelBean.setName((String)(seniorPersonsRow.get("FULL_NAME")));           
                personId = (String)(seniorPersonsRow.get("PERSON_ID"));
                seniorPersonnelBean.setPersonId(personId);
                seniorPersonnelBean.setTitle((String)(seniorPersonsRow.get("PROJECT_ROLE")));
                //initialization
                seniorPersonnelBean.setFundsRequested(formatCost(fundsRequested));
                seniorPersonnelBean.setAcademicMonthsFunded(formatCost(months));
                seniorPersonnelBean.setCalendarMonthsFunded(formatCost(months));
                seniorPersonnelBean.setSummerMonthsFunded(formatCost(months));
                vecSeniors.add(seniorPersonnelBean);
            }           
             
            } 
       return vecSeniors ;       
    }
       
   
   /** getEffort 
    *  for this person,in this period, get the calendar months effort
   */
    private NSFSeniorPersonnelBean getEffort
                  (String proposalNumber, int version,  int budgetPeriod, 
                   String personId, NSFSeniorPersonnelBean seniorPersonnelBean)
       throws CoeusException, DBException
       
    {    NSFSeniorPersonnelBean personBean = null;
         double calendarMonths = 0.0;
         double summerMonths = 0.0;
         double acadMonths = 0.0;
         double fundsRequested = 0.0;
         double months = 0.0;
         
         String periodType;
         
        Vector result = null;
        Vector param = new Vector();
        param.addElement( new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
        param.addElement(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
        param.addElement(new Parameter("PERSON_ID",
                     DBEngineConstants.TYPE_STRING,  personId ));
                 
        HashMap effortRow = null;
        if(dbEngine !=null){
             result = new Vector(3,2);
             result = dbEngine.executeRequest("Coeus",
                    "call GetNsfSrPersonnelEffort ( <<PROPOSAL_NUMBER>> , "+
                            " <<VERSION_NUMBER>> , <<BUDGET_PERIOD>> , " +
                            "<<PERSON_ID>> , <<OUT RESULTSET rset>> )", 
                             "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }   
                  
         //stored procedure can return multiple rows for this person grouped
         //by period type (calendar, summer, academic, cycle). 
                      
         int listSize = result.size();
         if (listSize > 0){
           for(int index=0; index < listSize; index++){
                effortRow = (HashMap)result.elementAt(index);
                periodType = (String) effortRow.get("PERIOD_TYPE");
                months = Double.parseDouble(effortRow.get("MONTHS") == null ? "0" :
                                            effortRow.get("MONTHS").toString());
                                            
                if (periodType.equals ("AP")) {
                    acadMonths = acadMonths + months ;
                } else if (periodType.equals ("SP")) {
                    summerMonths = summerMonths + months;
                } else {
                    calendarMonths = calendarMonths + months;
                }
                fundsRequested = fundsRequested + Double.parseDouble(effortRow.get( "SALARY_REQUESTED") == null ? "0" :
                                                      effortRow.get("SALARY_REQUESTED").toString());
                      
            
            }
          }
          personBean = new NSFSeniorPersonnelBean ();
          personBean.setName(seniorPersonnelBean.getName());
          personBean.setPersonId(seniorPersonnelBean.getPersonId());
          personBean.setRowNumber(seniorPersonnelBean.getRowNumber());
          personBean.setTitle(seniorPersonnelBean.getTitle());
          personBean.setCalendarMonthsFunded(formatCost(calendarMonths));
          personBean.setAcademicMonthsFunded(formatCost(acadMonths));
          personBean.setSummerMonthsFunded(formatCost(summerMonths)); 
          personBean.setFundsRequested(formatCost(fundsRequested));
              
           
          return personBean;    
    }     
                  
  
    
    /** getBudgetPersonsInfo - returns vector of SalaryAndWagesBeans
     * 
     */ 
    
    private CoeusVector getBudgetPersonsInfo(String proposalNumber, int version, int budgetPeriod)
       throws CoeusException, DBException
    {   SalaryAndWagesBean salaryAndWagesBean;
        
        //additions for nih change in may 2006
        String strMonths = "0";
        double fundingMonths = 0;
        HashMap hmMonths = new HashMap();
        //end additions
          
        CoeusVector vecBudgetPersons = null;
        vecBudgetPersons = new CoeusVector();
       // keep the stored procedure result in a vector
        Vector result = null;
       // keep the parameters for the stored procedure in a vector
        Vector param = new Vector();
        param.add(new Parameter("AS_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING, proposalNumber)) ;
	param.add(new Parameter("AI_VERSION",
                                DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
        param.add(new Parameter("AI_PERIOD",
                                DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
   
       HashMap budgetPersonsRow = null;
       if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call getBudPersonSal ( <<AS_PROPOSAL_NUMBER>> , <<AI_VERSION>> , <<AI_PERIOD>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       /*case 2695 - multiple pis for nih. if sponsor is nih, if person is investigator, make the 
        *role = PI
        **/
       int isNIH = getIsNIH(proposalNumber);
       int isInv = 0;
       String role ="";
       
        String fullName;
        int listSize = result.size();
        if (listSize > 0){
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                budgetPersonsRow = (HashMap)result.elementAt(rowIndex);
                salaryAndWagesBean = new SalaryAndWagesBean();
                fullName = (String)(budgetPersonsRow.get("FULLNAME") == null ? " " :
                             budgetPersonsRow.get("FULLNAME")) ;
                salaryAndWagesBean.setFullName( fullName );
                salaryAndWagesBean.setLastName((String)
                            (budgetPersonsRow.get("LASTNAME") == null ? fullName :
                             budgetPersonsRow.get("LASTNAME")) );
                salaryAndWagesBean.setFirstName((String)
                            (budgetPersonsRow.get("FIRSTNAME") == null ? " " :
                             budgetPersonsRow.get("FIRSTNAME")));
                salaryAndWagesBean.setMiddleName((String)
                            budgetPersonsRow.get("MIDDLENAME"));
                salaryAndWagesBean.setPersonId((String)
                            budgetPersonsRow.get("PERSONID"));
                /*case 2695*/
                role = (String)(budgetPersonsRow.get("PROJECTROLE")== null ? " " :
                                budgetPersonsRow.get("PROJECTROLE"));
                if (isNIH == 1){
                    //check if investigator, pi, or multipi. changes for case 2229
                    isInv = getIsInv(proposalNumber, budgetPersonsRow.get("PERSONID")== null ? "0" :
                                                     budgetPersonsRow.get("PERSONID").toString());
                    if ((isInv == 1) || (isInv ==2)) {
                         role = "Principal Investigator";
                    }else if (isInv == 3){
                         role = "Co-Investigator";
                    }
                 
                }
                
                salaryAndWagesBean.setRole(role);
                         
                salaryAndWagesBean.setAppointmentType((String)
                            (budgetPersonsRow.get("APPOINTMENTTYPE") == null ? " " :
                             budgetPersonsRow.get("APPOINTMENTTYPE")));
                salaryAndWagesBean.setAppointmentMonths(
                         Double.parseDouble(budgetPersonsRow.get("APPOINTMENTTYPE") == null ? "0" :
                                               budgetPersonsRow.get("APPOINTMENTTYPE").toString())); 
         //       salaryAndWagesBean.setFundingMonths(this.getFundingMonths(proposalNumber,version,
         //                        (String) budgetPersonsRow.get("PERSONID")));
                CoeusVector cvMonths = getFundingMonths(proposalNumber,version,(String) budgetPersonsRow.get("PERSONID"));
                //initialize
                salaryAndWagesBean.setFundingMonths(0.0);
                salaryAndWagesBean.setAcademicFundingMonths(0.0);
                salaryAndWagesBean.setSummerFundingMonths(0.0);
                
                for (int i=0; i< cvMonths.size(); i++  ){
                   hmMonths = (HashMap) cvMonths.get(i);
                   strMonths = (hmMonths.get("MONTHS") == null ? "0" :
                                 hmMonths.get("MONTHS").toString()); 
                   fundingMonths = (Double.parseDouble(strMonths));
                   if (hmMonths.get("PERIOD_TYPE").toString().equals("AP")) {
                       salaryAndWagesBean.setAcademicFundingMonths(fundingMonths);
                   }else if (hmMonths.get("PERIOD_TYPE").toString().equals("SP")) {
                       salaryAndWagesBean.setSummerFundingMonths(fundingMonths);
                   }else salaryAndWagesBean.setFundingMonths(fundingMonths);
                }
      

                      
                //hard coding fulltime question
                salaryAndWagesBean.setFullTime(true);
                                  
                salaryAndWagesBean.setPercentEffort( Integer.parseInt(
                                              (budgetPersonsRow.get("EFFORT") == null ? "0" :
                                               budgetPersonsRow.get("EFFORT").toString()))); 
                salaryAndWagesBean.setCalculationBase(Double.parseDouble(
                                              (budgetPersonsRow.get("BASESALARY") == null ? "0" :
                                               budgetPersonsRow.get("BASESALARY").toString())));
                salaryAndWagesBean.setSalaryRequested(Double.parseDouble(
                                              (budgetPersonsRow.get("SALARYREQUESTED") == null ? "0" :
                                               budgetPersonsRow.get("SALARYREQUESTED").toString())));
                salaryAndWagesBean.setFringe( Double.parseDouble(
                                              (budgetPersonsRow.get("FRINGEBENEFITS") == null ? "0" :
                                               budgetPersonsRow.get("FRINGEBENEFITS").toString())));
                salaryAndWagesBean.setTotal(Double.parseDouble(
                                             (budgetPersonsRow.get("TOTAL") == null ? "0" :
                                              budgetPersonsRow.get("TOTAL").toString())));
             
                vecBudgetPersons.add(salaryAndWagesBean);
            }
        }
               
        return vecBudgetPersons ;
 
    }
  /* added for case 2695 
    getIsNIH
    returns 0 if not nih  
    returns 1 if nih  
   */
    private int getIsNIH(String proposalNumber)
      throws CoeusException, DBException
    {
    
     int isNIH = 0;
       
    Vector param = new Vector();
       
    param.addElement(new Parameter("ISNIH", 
          DBEngineConstants.TYPE_INT, Integer.toString(isNIH), 
          DBEngineConstants.DIRECTION_OUT));
    param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
    param.addElement( new Parameter("SPONSOR_GROUP",
          DBEngineConstants.TYPE_STRING, "NIH"));
        
    HashMap row = null;
    Vector result = new Vector();
                
    if(dbEngine !=null){
        result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER ISNIH>>=call fn_in_sponsor_group( <<PROPOSAL_NUMBER>> , <<SPONSOR_GROUP>> ) }", param);  
                         
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          isNIH = Integer.parseInt(row.get("ISNIH").toString());  
        }    
      
    return isNIH;
    }
         
    /* changes made for case 2229 - multi pis
    getIsINV 
    returns 1 if pd/pi,  2 if multi-pi, 3 if co-i,  0 if not an investigator
   */
    private int getIsInv (String proposalNumber, String personID)
      throws CoeusException, DBException
    {
    
     int isInv = 0;
       
    Vector param = new Vector();
       
    param.addElement(new Parameter("ISINV", 
          DBEngineConstants.TYPE_INT, Integer.toString(isInv), 
          DBEngineConstants.DIRECTION_OUT));
    param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
    param.addElement( new Parameter("PERSON_ID",
          DBEngineConstants.TYPE_STRING, personID));
        
    HashMap row = null;
    Vector result = new Vector();
                
    if(dbEngine !=null){
        result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER ISINV>>=call fn_is_inv( <<PROPOSAL_NUMBER>> , <<PERSON_ID>> ) }", param);  
                         
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          isInv = Integer.parseInt(row.get("ISINV").toString());  
        }    
      
    return isInv;
    }
         
    
     /** getFundingMonths - for a person FOR PERIOD 1 (NIH only cares about period 1)
     *  this was changed to call a procedure rather than a function in order to get the
      * summer, academic, and calendar months - as required by the NIH change in May, 2006
      * returns vector of HashMaps containing summer,calendar, and academic months for this person
     */
     
//    private double getFundingMonths(String proposalNumber, int version, String personId )
     private CoeusVector getFundingMonths(String proposalNumber, int version, String personId )
       throws CoeusException, DBException
    {
       
 //     double fundingMonths = 0;
//      String strMonths = "0";
      String periodType;
      HashMap hmMonths = null;
      
      CoeusVector cvMonths = null;
      cvMonths = new CoeusVector();
      Vector param = new Vector();
      HashMap row = null;
      Vector result = new Vector();
                       
      param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
      param.addElement( new Parameter("VERSION_NUMBER", 
          DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
      param.addElement( new Parameter("PERSON_ID",
          DBEngineConstants.TYPE_STRING, personId));
  
       if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
             "call get_Funding_Months( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>> , <<PERSON_ID>>," + 
              " <<OUT RESULTSET rset>> )", "Coeus" ,  param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }   
      
        int listSize = result.size();
        if (listSize > 0){
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                row = (HashMap)result.elementAt(rowIndex);
                cvMonths.add(row);
//                strMonths = (row.get("MONTHS") == null ? "0" :
//                       row.get("MONTHS").toString()); 
//                fundingMonths = (Double.parseDouble(strMonths));
//                periodType = row.get("PERIOD_TYPE").toString();
//                
            }
        }
      
   return cvMonths;
   //   return fundingMonths;  
    }
      
    
     /** getIndirectCostDetails - returns vector of IndirectCostDetailBeans
     *  case 1655 - obsolete - replaced by call to s2stxnBean
     */
       private CoeusVector getIndirectCostDetails(String proposalNumber)
       throws CoeusException, DBException
       {
        CoeusVector vecIndirectCosts = new CoeusVector();
        IndirectCostDetailBean indirectCostDetailBean = new IndirectCostDetailBean();
        indirectCostDetailBean.setBaseAmount(Double.parseDouble("0.0"));
        indirectCostDetailBean.setRate(Double.parseDouble("0.0"));
         vecIndirectCosts.add(indirectCostDetailBean);
        return vecIndirectCosts ;
        
    }
       
     /** getCostBean - returns vector of CostBeans - 
      *  costBean contains cost information 
      *  for a BUDGET CATEGORY for all budget periods IN VERSION
      * 
     */
       private CoeusVector getSpecificCost(String proposalNumber, int version, String sponsor)
       throws CoeusException, DBException
       {
        CostBean costBean = new CostBean();;
    
        CoeusVector vecCostBeans = new CoeusVector();    
       
       // keep the stored procedure result in a vector
        Vector result = null;
       // keep the parameters for the stored procedure in a vector
        Vector param = new Vector();
                               
	param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
            
        param.addElement(new Parameter("AS_SPONSOR", 
             DBEngineConstants.TYPE_STRING,sponsor));
        HashMap directCostRow = null;
                 
       if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call getCostsForPrinting ( <<AS_PROPOSAL_NUMBER>> , <<VERSION_NUMBER>> , "
                + "<<AS_SPONSOR>> , " +   "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                directCostRow = (HashMap)result.elementAt(rowIndex);
                costBean = new CostBean();
               
                costBean.setCost(
                     Double.parseDouble(directCostRow.get("COST") == null ? "0" :
                                               directCostRow.get("COST").toString()));        
                costBean.setVersionNumber(
                    Integer.parseInt(directCostRow.get("VERSION_NUMBER").toString()));
                                 
                costBean.setBudgetPeriod(
                    Integer.parseInt(directCostRow.get("BUDGETPERIOD").toString()));
               
                costBean.setBudgetCategoryCode((String)directCostRow.get("BUDGETCATEGORYCODE"));
              
                costBean.setBudgetCategoryDesc((String)directCostRow.get("BUDGETCATEGORYDESC") );
                
                costBean.setDescription((String)directCostRow.get("DESCRIPTION"));
           
                costBean.setCategoryType((String)directCostRow.get("CATEGORYTYPE"));
                vecCostBeans.add(costBean);
            }
        }
    
        return vecCostBeans ;
       
    }
       
    // start addition for case 1655 to get Program Income
    /** getProgramIncome - returns vector of ProgramIncomeBeans 
      * 
    */ 
       public CoeusVector getProgramIncome(String proposalNumber, int version, int budgetPeriod)
       throws CoeusException, DBException
       {
          CoeusVector cvProgramIncome = null;
          ProgramIncomeBean programIncomeBean = new ProgramIncomeBean();
     
 
           Vector progParam= new Vector();
           BigDecimal amount = new BigDecimal ("0");
           String source;
          
           progParam.addElement( new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, proposalNumber));
           progParam.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
           progParam.addElement(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
      
           HashMap progRow = null;
                Vector progResult = new Vector();
        
           if(dbEngine !=null){
              progResult = dbEngine.executeRequest("Coeus",
                 "call getProgIncomeForPrint ( <<PROPOSAL_NUMBER>>  ,<<VERSION_NUMBER>> , "
                 + " <<BUDGET_PERIOD>> , <<OUT RESULTSET rset>> )", "Coeus", progParam);           
                     
           }else{
              throw new CoeusException("db_exceptionCode.1000");
           }    
                 
           cvProgramIncome = new CoeusVector();
           if (progResult.size() > 0 ) {
            
              for (int j = 0; j < progResult.size(); j++){          
                  programIncomeBean = new ProgramIncomeBean();
                  progRow = (HashMap) progResult.elementAt(j);
                  amount = new BigDecimal( progRow.get("AMOUNT") == null ? "0" :
                                          progRow.get("AMOUNT").toString());
                  source = progRow.get("DESCRIPTION") == null ? "Unknown" :
                                    progRow.get("DESCRIPTION").toString();
                                    
                  programIncomeBean.setSource(source);
                  programIncomeBean.setAmount(amount   );
                  
                  cvProgramIncome.add(programIncomeBean);
               }   
            }       
       return cvProgramIncome;
       }  
       
       //case 1751 - get nih sponsor code
       private String getNIHSponsorCode()  throws CoeusException, DBException {
           
          String nihCode=""; //MIT default
          Vector paramNIH = new Vector();
               
          String nihParameter = "NIH_SPONSOR_CODE";
                
          paramNIH.addElement(new Parameter("PARAMETER_VALUE", 
                DBEngineConstants.TYPE_STRING, nihCode, 
                    DBEngineConstants.DIRECTION_OUT));
          paramNIH.addElement( new Parameter("PARAMETER",
                DBEngineConstants.TYPE_STRING, nihParameter));
        
          HashMap row = null;
          Vector resultNIH = new Vector();
                
          if(dbEngine !=null){
                resultNIH = dbEngine.executeFunctions("Coeus",
                "{ <<OUT STRING PARAMETER_VALUE>>=call get_parameter_value( <<PARAMETER>> ) }",
                paramNIH);  
          
          }else{
              throw new CoeusException("db_exceptionCode.1000");
          }      
                
          if(!resultNIH.isEmpty()){
               row = (HashMap)resultNIH.elementAt(0);
               if (row.get("PARAMETER_VALUE") == null){   
                  
                   throw new CoeusException("exceptionCode.90500");  
               } else nihCode = row.get("PARAMETER_VALUE").toString();  
          }else {
              throw new CoeusException("exceptionCode.90500");
          }
          
          return nihCode;
        }
        
       public String getCognizantAgency (String propNumber)
           throws CoeusException, DBException {
       Vector param = new Vector();  
       String agency = "Unknown";
       param.addElement(new Parameter("AGENCY", 
              DBEngineConstants.TYPE_STRING, agency, DBEngineConstants.DIRECTION_OUT));
   
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
      
        HashMap row = null;
        Vector result = new Vector();
                
       if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING AGENCY>>=call s2sPackage.fn_get_cog_agency( " + 
              " <<PROPOSAL_NUMBER>>  ) }",
                param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          agency = row.get("AGENCY").toString();  
        }                
        
         
         return agency;
     }
   
       public String getDHHSAgreement (String organizationID)
            throws CoeusException, DBException  {
     
          String dhhs = "0"; //MIT Default
          Vector paramDHHS = new Vector();
               
          String dhhsParameter = "DHHS_AGREEMENT";
                
          paramDHHS.addElement(new Parameter("PARAMETER_VALUE", 
                DBEngineConstants.TYPE_STRING, dhhs, 
                    DBEngineConstants.DIRECTION_OUT));
          paramDHHS.addElement( new Parameter("PARAMETER",
                DBEngineConstants.TYPE_STRING, dhhsParameter));
        
          HashMap row = null;
          Vector result = new Vector();
                
          if(dbEngine !=null){
                result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT STRING PARAMETER_VALUE>>=call get_parameter_value( <<PARAMETER>> ) }",
                paramDHHS);  
          
          }else{
              throw new CoeusException("db_exceptionCode.1000");
          }      
                
          if(!result.isEmpty()){
               row = (HashMap)result.elementAt(0);
               if (row.get("PARAMETER_VALUE") == null){   
                  
                   throw new CoeusException("exceptionCode.90500");  
               } else dhhs = row.get("PARAMETER_VALUE").toString();  
          }else {
              throw new CoeusException("exceptionCode.90500");
          }
          
          return dhhs;
        }

        private BigDecimal formatCost (double dblCost) throws CoeusException {
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         
         return bdCost; 
   }      

}
  
