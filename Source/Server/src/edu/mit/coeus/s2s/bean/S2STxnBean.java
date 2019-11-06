package edu.mit.coeus.s2s.bean;
import java.util.* ;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.generator.stream.bean.ExAttQueryParams;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.query.*;
import java.lang.Integer;
import java.math.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
  
public class S2STxnBean
{
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
   
    ArrayList keyPersonList ;
    BudgetSummaryDataBean budgetSummaryBean;
    String propNumber;
    int    version;
    int    totCountOtherPersonnel = 0;
    BigDecimal totalDomesticTravel = new BigDecimal("0");
    BigDecimal totalForeignTravel = new BigDecimal("0");
    BigDecimal cumTotalEquipFund = new BigDecimal("0");
    BigDecimal bdZero = new BigDecimal("0");

    //start add costSaring for fedNonFedBudget repport
    BigDecimal totalDomesticTravelNonFund = new BigDecimal("0");
    BigDecimal totalForeignTravelNonFund = new BigDecimal("0");
    BigDecimal cumTotalEquipNonFund = new BigDecimal("0");
    //end add costSaring for fedNonFedBudget repport

    
    /** Creates a new instance of S2STxnBean */
    public S2STxnBean()
    {
        
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
        budgetSummaryBean = new BudgetSummaryDataBean();
    }
    
  public BudgetSummaryDataBean getBudgetInfo(String proposalNumber)
    throws CoeusException, DBException
  {
    CoeusVector vecBudgetPeriodBeans ;
    keyPersonList = new ArrayList();
    propNumber = proposalNumber;
   
    version = getVersion(propNumber); 
    getAgency();
    vecBudgetPeriodBeans = getPeriods ();
    budgetSummaryBean.setBudgetPeriods(vecBudgetPeriodBeans);
    getTotals(vecBudgetPeriodBeans);
   
    return budgetSummaryBean;
      
  }
  
  public int getVersion(String propNumber) 
      throws CoeusException, DBException {
      
      //version is global scope variable
                    
     version = 0;
       
     Vector param = new Vector();
       
     param.addElement(new Parameter("VERSION", 
          DBEngineConstants.TYPE_INT, Integer.toString(version), 
          DBEngineConstants.DIRECTION_OUT));
     param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
        
     HashMap row = null;
     Vector result = new Vector();
                
     if(dbEngine !=null){
        result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER VERSION>> = call s2sPackage.fn_Get_Version( <<PROPOSAL_NUMBER>> ) }", param);  
         
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
         version = Integer.parseInt(row.get("VERSION").toString());   
        }                
       return version;
    }
     
            
     /** get summary budget for all periods
     */
        
    private void getTotals(CoeusVector vecBudgetPeriodBeans)
        throws CoeusException, DBException
    {
        
        BigDecimal totalCost = new BigDecimal("0");
        BigDecimal totalDirectCost = new BigDecimal("0");
        BigDecimal totalIndirectCost = new BigDecimal("0");
        BigDecimal totalCostSharing = new BigDecimal("0");
        
        Vector result = new Vector(3,2);
        HashMap row = null;
        Vector param = new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,propNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,Integer.toString(version)));
    
        if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  s2sPackage.get_tot_bud_info ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>> , "
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
        
                budgetSummaryBean.setCumTotalCosts(totalCost);
                budgetSummaryBean.setCumTotalIndirectCosts(totalIndirectCost);
                budgetSummaryBean.setCumTotalDirectCosts(totalDirectCost);   
                
                //start add costSaring for fedNonFedBudget repport
                budgetSummaryBean.setCumTotalCostSharing(totalCostSharing);               
                //end add costSaring for fedNonFedBudget repport

               }
        }
        //start add costSaring for fedNonFedBudget repport
        row = null;
        param = new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,propNumber));
    
        if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  s2sRRFedNonFedBudgetPkg.get_tot_Fed_NonFed_costsharing ( <<PROPOSAL_NUMBER>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        vecSize = result.size();
        
        if (vecSize >0){
            row = (HashMap) result.elementAt(0); 
            budgetSummaryBean.setCumTotalDirectCostSharing(new BigDecimal( row.get("TOTAL_DIRECT_COST_SHARING") == null? "0" : row.get("TOTAL_DIRECT_COST_SHARING").toString()));
            budgetSummaryBean.setCumTotalIndirectCostSharing(new BigDecimal( row.get("TOTAL_INDIRECT_COST_SHARING") == null? "0" :row.get("TOTAL_INDIRECT_COST_SHARING").toString()));
        }
        HashMap hmTotalPersFunds = null;
        BigDecimal totPersFunds = new BigDecimal("0");
        BigDecimal totPersNonFunds = new BigDecimal("0");
        hmTotalPersFunds = getTotalPersonnelFunds();
        if (hmTotalPersFunds != null && hmTotalPersFunds.size() > 0 ){
            totPersFunds = new BigDecimal(hmTotalPersFunds.get("TOTAL_FUNDS") == null? "0" : hmTotalPersFunds.get("TOTAL_FUNDS").toString());
            System.out.println("....totPersFunds is " + totPersFunds);
            
            totPersNonFunds =new BigDecimal(hmTotalPersFunds.get("TOTAL_NON_FUNDS") == null? "0" : hmTotalPersFunds.get("TOTAL_NON_FUNDS").toString());
        } 
        budgetSummaryBean.setCumTotalNonFundsForPersonnel(totPersNonFunds);
        budgetSummaryBean.setCumDomesticTravelNonFund(totalDomesticTravelNonFund);
        budgetSummaryBean.setCumForeignTravelNonFund(totalForeignTravelNonFund);
        budgetSummaryBean.setCumTravelNonFund(totalDomesticTravelNonFund.add(totalForeignTravelNonFund));
//        BigDecimal totPersFunds = getTotalPersonnelFunds();
        //end add costSaring for fedNonFedBudget repport
        budgetSummaryBean.setCumTotalFundsForPersonnel(totPersFunds);
        
        budgetSummaryBean.setCumDomesticTravel(totalDomesticTravel);
        budgetSummaryBean.setCumForeignTravel(totalForeignTravel);
        budgetSummaryBean.setCumTravel(totalDomesticTravel.add(totalForeignTravel));
                
        //sum up other direct costs from period beans
        BudgetPeriodDataBean budgetPeriodBean = new BudgetPeriodDataBean();
        OtherDirectCostBean otherDirectCostBean = new OtherDirectCostBean();
        CoeusVector cvOtherDirectCost = new CoeusVector();  
        CoeusVector cvOtherCosts = new CoeusVector();
        
        //initialize sums
        BigDecimal cumAlterations = new BigDecimal("0");
        BigDecimal cumConsultants = new BigDecimal("0");
        BigDecimal cumMaterials = new BigDecimal("0");
        BigDecimal cumPubs = new BigDecimal("0");
        BigDecimal cumSubAward = new BigDecimal("0");
        BigDecimal cumComputer = new BigDecimal("0");
        BigDecimal cumEquipRental = new BigDecimal("0");
        BigDecimal cumAll = new BigDecimal("0");
        BigDecimal cumOtherType1 = new BigDecimal("0");
        BigDecimal cumPartStipends = new BigDecimal("0");
        BigDecimal cumPartSubsistence = new BigDecimal("0");
        BigDecimal cumPartTuition = new BigDecimal("0");
        BigDecimal cumPartOther = new BigDecimal("0");
        BigDecimal cumPartTravel = new BigDecimal("0");
        int cumParticipantCount = 0;
        //start add costSaring for fedNonFedBudget repport
        BigDecimal cumAlterationsCostSharing = new BigDecimal("0");
        BigDecimal cumConsultantsCostSharing = new BigDecimal("0");
        BigDecimal cumMaterialsCostSharing = new BigDecimal("0");
        BigDecimal cumPubsCostSharing = new BigDecimal("0");
        BigDecimal cumSubAwardCostSharing = new BigDecimal("0");
        BigDecimal cumComputerCostSharing = new BigDecimal("0");
        BigDecimal cumEquipRentalCostSharing = new BigDecimal("0");
        BigDecimal cumAllCostSharing = new BigDecimal("0");
        BigDecimal cumOtherType1CostSharing = new BigDecimal("0");
        BigDecimal cumPartStipendsCostSharing = new BigDecimal("0");
        BigDecimal cumPartSubsistenceCostSharing= new BigDecimal("0");
        BigDecimal cumPartTuitionCostSharing = new BigDecimal("0");
        BigDecimal cumPartOtherCostSharing = new BigDecimal("0");
        BigDecimal cumPartTravelCostSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        
        //for each period bean, add the sums
                //addition on march 7
        if (vecBudgetPeriodBeans != null){
         for (int i = 0; i < vecBudgetPeriodBeans.size(); i++){
           budgetPeriodBean = (BudgetPeriodDataBean) vecBudgetPeriodBeans.get(i);
           cvOtherDirectCost = budgetPeriodBean.getOtherDirectCosts();
           otherDirectCostBean = (OtherDirectCostBean) cvOtherDirectCost.get(0);
           cumAlterations = cumAlterations.add(otherDirectCostBean.getAlterations());
           cumConsultants = cumConsultants.add(otherDirectCostBean.getconsultants());
           cumMaterials = cumMaterials.add(otherDirectCostBean.getmaterials());
           cumPubs = cumPubs.add(otherDirectCostBean.getpublications());
           cumSubAward = cumSubAward.add(otherDirectCostBean.getsubAwards());
           cumComputer = cumComputer.add(otherDirectCostBean.getcomputer());
           cumEquipRental = cumEquipRental.add(otherDirectCostBean.getEquipRental());
           cumAll = cumAll.add(otherDirectCostBean.gettotalOtherDirect());
           
           cumPartStipends = cumPartStipends.add(
                 otherDirectCostBean.getPartStipends()==null? bdZero : otherDirectCostBean.getPartStipends());
           cumPartTravel = cumPartTravel.add(
                otherDirectCostBean.getPartTravel()==null? bdZero : otherDirectCostBean.getPartTravel());
           cumPartSubsistence = cumPartSubsistence.add(
                otherDirectCostBean.getPartSubsistence()==null? bdZero : otherDirectCostBean.getPartSubsistence());
           cumPartTuition = cumPartTuition.add(
                 otherDirectCostBean.getPartTuition()==null? bdZero : otherDirectCostBean.getPartTuition());
           cumPartOther = cumPartOther.add(
                otherDirectCostBean.getPartOther()==null? bdZero : otherDirectCostBean.getPartOther());
           cumParticipantCount = cumParticipantCount + (otherDirectCostBean.getParticpantTotalCount() == 0?
              0 : otherDirectCostBean.getParticpantTotalCount());
           
           //start add costSaring for fedNonFedBudget repport
           cumAlterationsCostSharing = cumAlterationsCostSharing.add(otherDirectCostBean.getAlterationsCostSharing());
           cumConsultantsCostSharing = cumConsultantsCostSharing.add(otherDirectCostBean.getConsultantsCostSharing());
           cumMaterialsCostSharing = cumMaterialsCostSharing.add(otherDirectCostBean.getMaterialsCostSharing());
           cumPubsCostSharing = cumPubsCostSharing.add(otherDirectCostBean.getPublicationsCostSharing());
           cumSubAwardCostSharing = cumSubAwardCostSharing.add(otherDirectCostBean.getSubAwardsCostSharing());
           cumComputerCostSharing = cumComputerCostSharing.add(otherDirectCostBean.getComputerCostSharing());
           cumEquipRentalCostSharing = cumEquipRentalCostSharing.add(otherDirectCostBean.getEquipRentalCostSharing());
           cumAllCostSharing = cumAllCostSharing.add(otherDirectCostBean.getTotalOtherDirectCostSharing());
           
           cumPartStipendsCostSharing = cumPartStipendsCostSharing.add(
                 otherDirectCostBean.getPartStipendsCostSharing()==null? bdZero : otherDirectCostBean.getPartStipendsCostSharing());
           cumPartTravelCostSharing = cumPartTravelCostSharing.add(
                otherDirectCostBean.getPartTravelCostSharing()==null? bdZero : otherDirectCostBean.getPartTravelCostSharing());
           cumPartSubsistenceCostSharing = cumPartSubsistenceCostSharing.add(
                 otherDirectCostBean.getPartSubsistenceCostSharing()==null? bdZero : otherDirectCostBean.getPartSubsistenceCostSharing());
           cumPartTuitionCostSharing = cumPartTuitionCostSharing.add(
                 otherDirectCostBean.getPartTuitionCostSharing()==null? bdZero : otherDirectCostBean.getPartTuitionCostSharing());
         
           cumPartOtherCostSharing = cumPartOtherCostSharing.add(
                otherDirectCostBean.getPartOtherCostSharing()==null? bdZero : otherDirectCostBean.getPartOtherCostSharing());
           //end add costSaring for fedNonFedBudget repport
         
           //sum the costs of the other kinds of direct cost
           //i am lumping all others together in one bunch (could be up to 3 types of others)
           HashMap hmOthers = new HashMap();
           cvOtherCosts = otherDirectCostBean.getOtherCosts();
           for (int ii = 0; ii < cvOtherCosts.size(); ii++){
               hmOthers = (HashMap)cvOtherCosts.get(ii);
               cumOtherType1 = cumOtherType1.add(new BigDecimal(hmOthers.get("Cost").toString()));
               //start add costSaring for fedNonFedBudget repport
               cumOtherType1CostSharing = cumOtherType1CostSharing.add(new BigDecimal(hmOthers.get("CostSharing").toString()));
               //end add costSaring for fedNonFedBudget repport
           }     
         }
        } //end if null
        budgetSummaryBean.setpartOtherCost(cumPartOther);
        budgetSummaryBean.setpartStipendCost(cumPartStipends);
        budgetSummaryBean.setpartTravelCost(cumPartTravel);
        budgetSummaryBean.setPartSubsistence(cumPartSubsistence);
        budgetSummaryBean.setPartTuition(cumPartTuition);
        budgetSummaryBean.setparticipantCount(cumParticipantCount);
        //start add costSaring for fedNonFedBudget repport
        budgetSummaryBean.setPartOtherCostSharing(cumPartOtherCostSharing);
        budgetSummaryBean.setPartStipendCostSharing(cumPartStipendsCostSharing);
        budgetSummaryBean.setPartTravelCostSharing(cumPartTravelCostSharing);
        budgetSummaryBean.setPartSubsistenceCostSharing(cumPartSubsistenceCostSharing);
        budgetSummaryBean.setPartTuitionCostSharing(cumPartTuitionCostSharing);
        //end add costSaring for fedNonFedBudget repport  
               
        OtherDirectCostBean summaryOtherDirectCostBean = new OtherDirectCostBean();
        summaryOtherDirectCostBean.setAlterations(cumAlterations);
        summaryOtherDirectCostBean.setcomputer(cumComputer);
        summaryOtherDirectCostBean.setconsultants(cumConsultants);
        summaryOtherDirectCostBean.setmaterials(cumMaterials);
        summaryOtherDirectCostBean.setpublications(cumPubs);
        summaryOtherDirectCostBean.setsubAwards(cumSubAward);
        summaryOtherDirectCostBean.setEquipRental(cumEquipRental);
        summaryOtherDirectCostBean.settotalOtherDirect(cumAll);
        
        summaryOtherDirectCostBean.setPartStipends(cumPartStipends);
        summaryOtherDirectCostBean.setPartTravel(cumPartTravel);
        summaryOtherDirectCostBean.setPartSubsistence(cumPartSubsistence);
        summaryOtherDirectCostBean.setPartTuition(cumPartTuition);
        summaryOtherDirectCostBean.setPartOther(cumPartOther);
        summaryOtherDirectCostBean.setParticipantTotal(cumPartStipends.add(cumPartTravel.add(cumPartOther.add(cumPartSubsistence.add(cumPartTuition)))));//COEUSQA-4172
        summaryOtherDirectCostBean.setParticipantTotalCount(cumParticipantCount);
        //start add costSaring for fedNonFedBudget repport
        summaryOtherDirectCostBean.setAlterationsCostSharing(cumAlterationsCostSharing);
        summaryOtherDirectCostBean.setComputerCostSharing(cumComputerCostSharing);
        summaryOtherDirectCostBean.setConsultantsCostSharing(cumConsultantsCostSharing);
        summaryOtherDirectCostBean.setMaterialsCostSharing(cumMaterialsCostSharing);
        summaryOtherDirectCostBean.setPublicationsCostSharing(cumPubsCostSharing);
        summaryOtherDirectCostBean.setSubAwardsCostSharing(cumSubAwardCostSharing);
        summaryOtherDirectCostBean.setEquipRentalCostSharing(cumEquipRentalCostSharing);
        summaryOtherDirectCostBean.setTotalOtherDirectCostSharing(cumAllCostSharing);
        
        summaryOtherDirectCostBean.setPartStipendsCostSharing(cumPartStipendsCostSharing);
        summaryOtherDirectCostBean.setPartTravelCostSharing(cumPartTravelCostSharing);
        summaryOtherDirectCostBean.setPartTuitionCostSharing(cumPartTuitionCostSharing);
        summaryOtherDirectCostBean.setPartSubsistenceCostSharing(cumPartSubsistenceCostSharing);
        summaryOtherDirectCostBean.setPartOtherCostSharing(cumPartOtherCostSharing);
        summaryOtherDirectCostBean.setParticipantTotalCostSharing(cumPartStipendsCostSharing.add(cumPartTravelCostSharing.add(cumPartOtherCostSharing.add(cumPartTuitionCostSharing.add(cumPartSubsistenceCostSharing)))));
        //end add costSaring for fedNonFedBudget repport
        //create a vector of other costs (i have aggregated as many as 3 other types into one type for now
        CoeusVector cvAllOthers  = new CoeusVector();
        HashMap hmAllOthers =  new HashMap();
        hmAllOthers.put("Cost", cumOtherType1);
        //start add costSaring for fedNonFedBudget repport
        hmAllOthers.put("CostSharing", cumOtherType1CostSharing);
        //end add costSaring for fedNonFedBudget repport
        cvAllOthers.add(hmAllOthers);
        summaryOtherDirectCostBean.setOtherCosts(cvAllOthers);
        
        CoeusVector cvCumOtherDirectCost = new CoeusVector(); //all periods
        cvCumOtherDirectCost.add(summaryOtherDirectCostBean);
        budgetSummaryBean.setOtherDirectCosts(cvCumOtherDirectCost);
       
      
        budgetSummaryBean.setCumEquipmentFunds(cumTotalEquipFund);
        //start add costSaring for fedNonFedBudget repport
        budgetSummaryBean.setCumEquipmentNonFunds(cumTotalEquipNonFund);
        //end add costSaring for fedNonFedBudget repport
        
        //hard coding
        budgetSummaryBean.setCumFee(new BigDecimal ("0"));
        //cost sharing will go in fed thingie
     
        //sum up sr personnel funds from period beans
        budgetPeriodBean = new BudgetPeriodDataBean();
        BigDecimal totSrFunds = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        BigDecimal totSrNonFunds = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        //addition march 7
        if (vecBudgetPeriodBeans != null){
         for (int i = 0; i< vecBudgetPeriodBeans.size(); i++){
            budgetPeriodBean = (BudgetPeriodDataBean) vecBudgetPeriodBeans.get(i);
             
            System.out.println(" in loop at line 396, totSrFunds is - before " + totSrFunds);
            totSrFunds = totSrFunds.add(budgetPeriodBean.getTotalFundsKeyPersons());
            //start add costSaring for fedNonFedBudget repport
            System.out.println(" in loop at line 396, totSrFunds is - after " + totSrFunds);
            
            totSrNonFunds = totSrNonFunds.add(budgetPeriodBean.getTotalNonFundsKeyPersons());
            //end add costSaring for fedNonFedBudget repport
         }
        }
        
        budgetSummaryBean.setCumTotalFundsForSrPersonnel(totSrFunds);
        //start add costSaring for fedNonFedBudget repport
        budgetSummaryBean.setCumTotalNonFundsForSrPersonnel(totSrNonFunds);
        //end add costSaring for fedNonFedBudget repport
        
        //other personnel
        //funds for other personnel will be difference between total pers funds and sr pers funds
        System.out.println(" before subtracting, totPersFunds is " + totPersFunds + " totSrfunds is " + totSrFunds);
        
        budgetSummaryBean.setCumTotalFundsForOtherPersonnel(totPersFunds.subtract(totSrFunds));  
        //start add costSaring for fedNonFedBudget repport
        budgetSummaryBean.setCumTotalNonFundsForOtherPersonnel(totPersNonFunds.subtract(totSrNonFunds));
        //end add costSaring for fedNonFedBudget repport
        
        //total number was obtained in getOtherPersonnel
        budgetSummaryBean.setCumNumOtherPersonnel(new BigInteger(Integer.toString(totCountOtherPersonnel)));
    }
    
    
    //start add costSaring for fedNonFedBudget repport
//     private BigDecimal getTotalPersonnelFunds()
//        throws CoeusException, DBException
    private HashMap getTotalPersonnelFunds()
        throws CoeusException, DBException
    //end add costSaring for fedNonFedBudget repport   
    {
        //change to procedure call instead of function
        System.out.println("in getTotalPersonnelFunds");
        
       int total = 0;
       BigDecimal bdTotal = new BigDecimal("0");
       //start add costSaring for fedNonFedBudget repport
       BigDecimal bdTotalCostSharing = new BigDecimal("0");
       HashMap hmTotalPersonnelFunds = new HashMap();
       //end add costSaring for fedNonFedBudget repport
       Vector result = new Vector(3,2);
          
       HashMap resultRow = null;
       Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
    
    
       if(dbEngine !=null){

           result = dbEngine.executeRequest("Coeus",
             "call s2sPackage.get_tot_person_funds(<<PROPOSAL_NUMBER>> , " 
             + " <<VERSION_NUMBER>> , <<OUT RESULTSET rset>>)",
                "Coeus", param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
       int vecSize = result.size();
       if (vecSize >0) {
           //should just be one row
         
               resultRow = (HashMap) result.elementAt(0);  
               bdTotal = new BigDecimal(resultRow.get("TOTAL_FUNDS") == null ? "0" :
                                        resultRow.get("TOTAL_FUNDS") .toString());  
               
               System.out.println("after call to get tot person funds, totalfunds is " + bdTotal);
               
               //start add costSaring for fedNonFedBudget repport
               bdTotalCostSharing = new BigDecimal(resultRow.get("TOTAL_NON_FUNDS") == null ? "0" :
                                        resultRow.get("TOTAL_NON_FUNDS") .toString()); 
               //end add costSaring for fedNonFedBudget repport           
        }                
     //start add costSaring for fedNonFedBudget repport
     hmTotalPersonnelFunds.put("TOTAL_FUNDS",bdTotal);
     hmTotalPersonnelFunds.put("TOTAL_NON_FUNDS",bdTotalCostSharing);
     return hmTotalPersonnelFunds;
//     return bdTotal; 
     //end add costSaring for fedNonFedBudget repport   
    }
    
    
    /** getPeriods
     *  this will set the BudgetPeriod vectors in budgetSummaryBean
     *  one row for each period in final version (or latest version if no final)
     */
    private CoeusVector getPeriods ()
        throws CoeusException, DBException
    {
        int numPeriods = 0;
        int budgetPeriod = 0;
     
     
        BudgetPeriodDataBean budgetPeriodBean = new BudgetPeriodDataBean();
        CoeusVector vecBudgetPeriodBeans = null;
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,propNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
          
        if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call s2sPackage.get_bud_periods ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
              + " <<OUT RESULTSET rset>> )",
              "Coeus", param);
            
        } else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       
        
        numPeriods = result.size();
        if (numPeriods >0){
            
            vecBudgetPeriodBeans = new CoeusVector();
              
            //call stored procedure to get basic period information
            
            for(int rowIndex=0; rowIndex < numPeriods; rowIndex++){
                BigInteger otherPersonnelCount = new BigInteger("0");
                BigDecimal otherPersonnelTotalFunds = new BigDecimal("0");
                //start add costSaring for fedNonFedBudget repport
                BigDecimal otherPersonnelTotalNonFunds = new BigDecimal("0");
                //end add costSaring for fedNonFedBudget repport

                budgetPeriodBean = new BudgetPeriodDataBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);               
                budgetPeriod = Integer.parseInt(budgetRow.get(
                    "BUDGET_PERIOD") == null ? "0" : budgetRow.get(
                    "BUDGET_PERIOD").toString());                              
                budgetPeriodBean.setFinalVersionFlag(budgetRow.get("FINAL_VERSION_FLAG").toString());                  
                budgetPeriodBean.setProposalNumber(propNumber);               
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
                budgetPeriodBean.setTotalCosts(new BigDecimal(
                       budgetRow.get("TOTAL_COST") == null ? "0" :
                       budgetRow.get("TOTAL_COST").toString()));                  
                budgetPeriodBean.setDirectCostsTotal(new BigDecimal (
                        budgetRow.get("TOTAL_DIRECT_COST") == null ? "0" :
                        budgetRow.get("TOTAL_DIRECT_COST").toString()));   
               //added for rounding problem
               budgetPeriodBean.setTotalIndirectCost(new BigDecimal (
                        budgetRow.get("TOTAL_INDIRECT_COST").toString()));
               
               //start add costSaring for fedNonFedBudget repport
                budgetPeriodBean.setCostSharingAmount(new BigDecimal (
                        budgetRow.get("COST_SHARING_AMOUNT") == null ? "0" :
                        budgetRow.get("COST_SHARING_AMOUNT").toString()));
                budgetPeriodBean.setTotalDirectCostSharing(new BigDecimal (
                        budgetRow.get("TOTAL_DIRECT_COST_SHARING") == null ? "0" :
                        budgetRow.get("TOTAL_DIRECT_COST_SHARING").toString()));
                budgetPeriodBean.setTotalIndirectCostSharing(new BigDecimal (
                        budgetRow.get("TOTAL_INDIRECT_COST_SHARING") == null ? "0" :
                        budgetRow.get("TOTAL_INDIRECT_COST_SHARING").toString()));                        
               //end add costSaring for fedNonFedBudget repport
                
                budgetPeriodBean.setCognizantFedAgency(getAgency());
            
                //indirect cost
                IndirectCostBean indirectCostBean = new IndirectCostBean();
                indirectCostBean = getInDirectCosts(propNumber,budgetPeriod,version,budgetPeriodBean);
           
                //indirectCost bean has detail beans - maximum of 4
                                                     
                budgetPeriodBean.setIndirectCosts(indirectCostBean);
              
            
                //equipment
                CoeusVector cvEquipment = new CoeusVector(); 
                cvEquipment = getEquipment(budgetPeriod);
                //cvEquipment is vector of equipment beans - really only one element
                budgetPeriodBean.setEquipment(cvEquipment);
                
                //other direct costs
                CoeusVector cvOtherDirectCost = new CoeusVector();
                cvOtherDirectCost = getOtherDirectCosts(budgetPeriod,"S2S");
         
                //cvOtherDirectCost has one element, which is an OtherDirectCostBean
                
                budgetPeriodBean.setOtherDirectCosts(cvOtherDirectCost);
             
                //travel
                OtherDirectCostBean otherDirectCostBean = new OtherDirectCostBean();
                otherDirectCostBean = (OtherDirectCostBean) cvOtherDirectCost.get(0);
                budgetPeriodBean.setDomesticTravelCost(otherDirectCostBean.getDomTravel());
                totalDomesticTravel = totalDomesticTravel.add(otherDirectCostBean.getDomTravel());
                budgetPeriodBean.setForeignTravelCost(otherDirectCostBean.getForeignTravel());
                totalForeignTravel = totalForeignTravel.add(otherDirectCostBean.getForeignTravel());
                budgetPeriodBean.setTotalTravelCost(otherDirectCostBean.getTotTravel());
                //start add costSaring for fedNonFedBudget repport
                budgetPeriodBean.setDomesticTravelCostSharing(otherDirectCostBean.getDomTravelCostSharing());
                totalDomesticTravelNonFund = totalDomesticTravelNonFund.add(otherDirectCostBean.getDomTravelCostSharing());
                budgetPeriodBean.setForeignTravelCostSharing(otherDirectCostBean.getForeignTravelCostSharing());
               // totalForeignTravelNonFund = totalForeignTravelNonFund.add(otherDirectCostBean.getForeignTravel());
               //coeusqa-2779
                totalForeignTravelNonFund = totalForeignTravelNonFund.add(otherDirectCostBean.getForeignTravelCostSharing());
                budgetPeriodBean.setTotalTravelCostSharing(otherDirectCostBean.getTotTravelCostSharing());
                //end add costSaring for fedNonFedBudget repport
              
                //participants
                budgetPeriodBean.setpartOtherCost(otherDirectCostBean.getPartOther() ==null? bdZero:
                                                  otherDirectCostBean.getPartOther());
                budgetPeriodBean.setpartStipendCost(otherDirectCostBean.getPartStipends()==null? bdZero:
                                                    otherDirectCostBean.getPartStipends());
                budgetPeriodBean.setpartTravelCost(otherDirectCostBean.getPartTravel()==null? bdZero:
                                                   otherDirectCostBean.getPartTravel());
                budgetPeriodBean.setPartSubsistence(otherDirectCostBean.getPartSubsistence()==null? bdZero:
                                                   otherDirectCostBean.getPartSubsistence());
                budgetPeriodBean.setPartTuition(otherDirectCostBean.getPartTuition()==null? bdZero:
                                                   otherDirectCostBean.getPartTuition());
                budgetPeriodBean.setparticipantCount(otherDirectCostBean.getParticpantTotalCount());
                
                //start add costSaring for fedNonFedBudget repport
                budgetPeriodBean.setPartOtherCostSharing(otherDirectCostBean.getPartOtherCostSharing() ==null? bdZero:
                                                  otherDirectCostBean.getPartOtherCostSharing());
                budgetPeriodBean.setPartStipendCostSharing(otherDirectCostBean.getPartStipendsCostSharing()==null? bdZero:
                                                    otherDirectCostBean.getPartStipendsCostSharing());
                budgetPeriodBean.setPartTravelCostSharing(otherDirectCostBean.getPartTravelCostSharing()==null? bdZero:
                                                   otherDirectCostBean.getPartTravelCostSharing());
                budgetPeriodBean.setPartTuitionCostSharing(otherDirectCostBean.getPartTuitionCostSharing()==null? bdZero:
                                                   otherDirectCostBean.getPartTuitionCostSharing());
                budgetPeriodBean.setPartSubsistenceCostSharing(otherDirectCostBean.getPartSubsistenceCostSharing()==null? bdZero:
                                                   otherDirectCostBean.getPartSubsistenceCostSharing());
                //end add costSaring for fedNonFedBudget repport
                
                //key persons - 
                CoeusVector cvKeyPersons = new CoeusVector();
                CoeusVector cvExtraPersons = new CoeusVector();;
                KeyPersonBean keyPersonBean = new KeyPersonBean();
                BigDecimal totalKeyPersonSum = new BigDecimal("0");
                BigDecimal totalAttKeyPersonSum = new BigDecimal("0");
                CompensationBean compensationBean = new CompensationBean();
                //start add costSaring for fedNonFedBudget repport
                BigDecimal totalKeyPersonSumCostSharing = new BigDecimal("0");
                BigDecimal totalAttKeyPersonSumCostSharing = new BigDecimal("0");
                //end add costSaring for fedNonFedBudget repport
             
//              keyPersonList = getKeyPersons(budgetPeriod, propNumber);
                //the RRBudget form has 8 people on the page.
                keyPersonList = getKeyPersons(budgetPeriod, propNumber, 8);
                if (keyPersonList.size() > 0) cvKeyPersons = (CoeusVector) keyPersonList.get(0);
                if (keyPersonList.size() > 1)cvExtraPersons = (CoeusVector) keyPersonList.get(1);
                
                budgetPeriodBean.setKeyPersons(cvKeyPersons);
                budgetPeriodBean.setExtraKeyPersons(cvExtraPersons);
           
               //  sum the funds 
                if (cvKeyPersons != null){
                    for (int i=0;i<cvKeyPersons.size();i++){
                     keyPersonBean = (KeyPersonBean) cvKeyPersons.get(i);
                     System.out.println("period is " + budgetPeriod + " person info is " +
                             keyPersonBean.getPersonId() + " base salary: " + keyPersonBean.getBaseSalary() +
                             " req sal: " + keyPersonBean.getRequestedSalary() + 
                             " funds req: " + keyPersonBean.getFundsRequested());
                     totalKeyPersonSum = totalKeyPersonSum.add(
                                keyPersonBean.getFundsRequested());
                     System.out.println("totalKeyPersonSum: " + totalKeyPersonSum);
                     
                     //start add costSaring for fedNonFedBudget repport
                      totalKeyPersonSumCostSharing = totalKeyPersonSumCostSharing.add(
                                keyPersonBean.getNonFundsRequested());
                     //end add costSaring for fedNonFedBudget repport
                    }
                }
                
                if (cvExtraPersons != null){
                    for (int i=0;i<cvExtraPersons.size();i++){
                       keyPersonBean = (KeyPersonBean) cvExtraPersons.get(i);
                       totalAttKeyPersonSum = totalAttKeyPersonSum.add(
                                keyPersonBean.getFundsRequested());
                       //start add costSaring for fedNonFedBudget repport
                       totalAttKeyPersonSumCostSharing = totalAttKeyPersonSumCostSharing.add(
                                keyPersonBean.getNonFundsRequested());
                       //end add costSaring for fedNonFedBudget repport
                     }
                }
            
         
                budgetPeriodBean.setTotalFundsKeyPersons(totalKeyPersonSum.add(totalAttKeyPersonSum));
                budgetPeriodBean.setTotalFundsAttachedKeyPersons(totalAttKeyPersonSum);
                //start add costSaring for fedNonFedBudget repport
                budgetPeriodBean.setTotalNonFundsKeyPersons(totalKeyPersonSumCostSharing.add(totalAttKeyPersonSumCostSharing));
                budgetPeriodBean.setTotalNonFundsAttachedKeyPersons(totalAttKeyPersonSumCostSharing);
                //end add costSaring for fedNonFedBudget repport
                
                System.out.println("after setting budget per bean, totalFundsKeyPersons is " +
                        budgetPeriodBean.getTotalFundsKeyPersons());
                
                //otherPersonnel
                CoeusVector cvOtherPersonnel = new CoeusVector();
                OtherPersonnelBean otherPersonnelBean = new OtherPersonnelBean();
         
                cvOtherPersonnel = getOtherPersonnel(budgetPeriod);
                //cvOtherPersonnel is vector of OtherPersonnelBeans
                budgetPeriodBean.setOtherPersonnel(cvOtherPersonnel);
                
            
                for (int vecIndex=0;vecIndex<cvOtherPersonnel.size();vecIndex++){
                    otherPersonnelBean = (OtherPersonnelBean) cvOtherPersonnel.get(vecIndex);
                    otherPersonnelCount = otherPersonnelCount.add(
                        otherPersonnelBean.getNumberPersonnel());
                    otherPersonnelTotalFunds = otherPersonnelTotalFunds.add(
                                otherPersonnelBean.getCompensation().getFundsRequested());
                    //start add costSaring for fedNonFedBudget repport
                    otherPersonnelTotalNonFunds = otherPersonnelTotalNonFunds.add(
                                otherPersonnelBean.getCompensation().getNonFundsRequested());
                    //end add costSaring for fedNonFedBudget repport
                }
                budgetPeriodBean.setTotalOtherPersonnelFunds(otherPersonnelTotalFunds);
                budgetPeriodBean.setOtherPersonnelTotalNumber(otherPersonnelCount);
     
                budgetPeriodBean.setTotalCompensation(
                    otherPersonnelTotalFunds.add(totalKeyPersonSum).add(totalAttKeyPersonSum));
                //start add costSaring for fedNonFedBudget repport
                budgetPeriodBean.setTotalOtherPersonnelNonFunds(otherPersonnelTotalNonFunds);     
                budgetPeriodBean.setTotalCompensationCostSharing(
                    otherPersonnelTotalNonFunds.add(totalKeyPersonSumCostSharing).add(totalAttKeyPersonSumCostSharing));
                //end add costSaring for fedNonFedBudget repport 
               vecBudgetPeriodBeans.add(budgetPeriodBean);
 
            }           
          
        }
      
        return vecBudgetPeriodBeans;

        
    }
    
    private CoeusVector getEquipment(int budgetPeriod)
       throws CoeusException, DBException {
           
        CoeusVector cvEquipBean = new CoeusVector();
        String sponsor = "S2S";
        BigDecimal totalEquipFund = new BigDecimal("0");
        BigDecimal totalExtraEquipFund = new BigDecimal("0");
        BigDecimal equipCost = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        BigDecimal totalEquipNonFund = new BigDecimal("0");
        BigDecimal totalExtraEquipNonFund = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        
        Vector result = null;
        Vector param = new Vector();
                               
        param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,propNumber));
        param.addElement(new Parameter("AI_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
        param.addElement(new Parameter("AI_PERIOD",
            DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
        param.addElement(new Parameter("AS_SPONSOR", 
             DBEngineConstants.TYPE_STRING,sponsor));
        HashMap equipRow = null;
                 
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call s2sPackage.getEquipment ( <<AS_PROPOSAL_NUMBER>> , <<AI_VERSION_NUMBER>> , "
                + "<<AI_PERIOD>> , <<AS_SPONSOR>> , " +   "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector cvExtraEquipment=new CoeusVector();
        if (listSize > 0){
            
            CostBean equipCostBean = new CostBean();;
            CoeusVector cvCostBeans = new CoeusVector();   
     
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                equipRow = (HashMap)result.elementAt(rowIndex);
                equipCostBean = new CostBean();
             
                equipCostBean.setBudgetPeriod(Integer.parseInt(equipRow.get("BUDGETPERIOD").toString()));
                equipCost = new BigDecimal(equipRow.get("COST").toString());
                equipCostBean.setCost(equipCost); 
                totalEquipFund = totalEquipFund.add(equipCost);
                equipCostBean.setCategory((String)equipRow.get("BUDGETCATEGORYDESC"));
                equipCostBean.setCategoryType((String)equipRow.get("CATEGORYTYPE"));             
                equipCostBean.setDescription((String)equipRow.get("DESCRIPTION"));
                //start add costSaring for fedNonFedBudget repport
                equipCostBean.setCostSharing(new BigDecimal(equipRow.get("COST_SHARING") == null ? "0" :
                                                    equipRow.get("COST_SHARING").toString()));
                totalEquipNonFund = totalEquipNonFund.add(equipCostBean.getCostSharing());                                    
                //end add costSaring for fedNonFedBudget repport
                cvCostBeans.add(equipCostBean);
            }
            
            //cvCostBeans is vector of costBeans containing equipment items
            
            EquipmentBean equipBean = new EquipmentBean();
            CostBean extraBean = new CostBean();
            
            if (cvCostBeans.size() > 10){
                 for (int j = cvCostBeans.size()-1; j > 9 ; j--){
                    cvExtraEquipment.add(cvCostBeans.get(j));
                    extraBean = (CostBean)cvCostBeans.get(j);    
                    totalExtraEquipFund = totalExtraEquipFund.add(extraBean.getCost());
                    //start add costSaring for fedNonFedBudget repport
                    totalExtraEquipNonFund = totalExtraEquipNonFund.add(extraBean.getCostSharing());
                    //end add costSaring for fedNonFedBudget repport
                    cvCostBeans.removeElementAt(j);
                 }
                equipBean.setExtraEquipmentList(cvExtraEquipment);    
                equipBean.setTotalExtraFund(totalExtraEquipFund);
                //start add costSaring for fedNonFedBudget repport
                equipBean.setTotalExtraNonFund(totalExtraEquipNonFund);
                //end  add costSaring for fedNonFedBudget repport
            }
           
            equipBean.setEquipmentList(cvCostBeans);
            equipBean.setTotalFund(totalEquipFund);
            cumTotalEquipFund = cumTotalEquipFund.add(totalEquipFund);
            //start add costSaring for fedNonFedBudget repport
            equipBean.setTotalNonFund(totalEquipNonFund);
            cumTotalEquipNonFund = cumTotalEquipNonFund.add(totalEquipNonFund);
            //end add costSaring for fedNonFedBudget repport
          
            cvEquipBean = new CoeusVector();
            cvEquipBean.add(equipBean);
        }
          return cvEquipBean;
    }
    
   
    
  
   /*************************************************************************
    need to limit the number of key persons to n and remove duplicates
    * returns vector of first n people or vector of extra people
    * (changed june,2006 for expanded key person form - added argument n for number
    * of key persons allowed)
   **************************************************************************/
//    private CoeusVector getEightKeyPersons ( CoeusVector cvKeyPersons, boolean firstN, int n)
     private CoeusVector getNKeyPersons ( CoeusVector cvKeyPersons, boolean firstN, int n)
      throws CoeusException, DBException {
     
        CoeusVector cvExtraPersons = new CoeusVector();
        KeyPersonBean keyPersonBean, previousKeyPersonBean;
       
     
       // change for sort_id - get the first n sort ids
        

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
    
    /*****************************************************
     *getInvestAndKeyPersons returns ArrayList containing two vectors
      first vector is the first numKeyPersons;  second vector is the remaining people
      This differs from getKeyPersons because this just gets the investigators
      and the key persons as defined on the key person tab in the proposal - 
      does not include budget people.
     * changes made in June,2006 to allow for expandedKeyPerson form - 40 people;
     * added numKeyPersons argument
     */
    
    
     public ArrayList getInvestAndKeyPersons( String propNumber, int numKeyPersons)
       throws CoeusException, DBException {  
        ArrayList listKeyPersons = new ArrayList();
        String personId = null;
        KeyPersonBean keyPersonBean;
        CoeusVector vecKeyPersons = new CoeusVector();
       
        Vector result = null;
        Vector param = new Vector();
        param.add(new Parameter("AS_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING, propNumber)) ;
        
        HashMap keyPersonsRow = null;
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call s2sPackage.get_inv_and_key_persons ( <<AS_PROPOSAL_NUMBER>>  , "
                +    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
          for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                keyPersonsRow = (HashMap)result.elementAt(rowIndex);
                keyPersonBean = new KeyPersonBean();
                personId = (String) keyPersonsRow.get("PERSON_ID");
                keyPersonBean.setPersonId(personId);
                keyPersonBean.setSortId( Integer.parseInt(keyPersonsRow.get("SORT_ID").toString()));
   
                keyPersonBean.setFirstName((String) 
			(keyPersonsRow.get("FIRST_NAME") == null ? "Unknown" : keyPersonsRow.get("FIRST_NAME")));
                keyPersonBean.setLastName((String)
                        (keyPersonsRow.get("LAST_NAME") == null ? "Unknown" : keyPersonsRow.get("LAST_NAME")) );
                keyPersonBean.setMiddleName((String)
                        (keyPersonsRow.get("MIDDLE_NAME") == null ? "Unknown" : keyPersonsRow.get("MIDDLE_NAME")) );
                keyPersonBean.setRole((String)
                       (keyPersonsRow.get("PROJECT_ROLE") == null ? "Unknown":keyPersonsRow.get("PROJECT_ROLE")) );
                String nonMITKeyPersonFlag = (String)keyPersonsRow.get("NON_MIT_PERSON");
                if(nonMITKeyPersonFlag == null || nonMITKeyPersonFlag.equals("N")){
                    keyPersonBean.setNonMITPersonFlag(false);
                }   
                else{
                    keyPersonBean.setNonMITPersonFlag(true);
                }           
                vecKeyPersons.add(keyPersonBean);
          }
        }
       
        CoeusVector cvAllPersons = new CoeusVector();
        cvAllPersons.addAll(vecKeyPersons);
        CoeusVector cvNKeyPersons = getNKeyPersons(vecKeyPersons,true,numKeyPersons);
        CoeusVector cvExtraPersons = getNKeyPersons(cvAllPersons,false,numKeyPersons);
//      CoeusVector cv8KeyPersons = getEightKeyPersons(vecKeyPersons,true);
//      CoeusVector cvExtraPersons = getEightKeyPersons(cvAllPersons,false);
    
      
        
//        listKeyPersons.add(cv8KeyPersons);
        listKeyPersons.add(cvNKeyPersons);
        listKeyPersons.add(cvExtraPersons);
        return listKeyPersons;
    }
    
    
   /*
    * getKeyPersons returns ArrayList containing two vectors
    * first vector is the first 8 key persons
    * second vector is the remaining people
    */
       public ArrayList getKeyPersons(int budgetPeriod, String propNumber,int numKeyPersons)
       throws CoeusException, DBException {  
        ArrayList listKeyPersons = new ArrayList();
        String personId = null;
        KeyPersonBean keyPersonBean;
        CompensationBean compensationBean;
        CoeusVector vecKeyPersons = new CoeusVector();
       
        Vector result = null;
        Vector param = new Vector();
        param.add(new Parameter("AS_PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING, propNumber)) ;
        param.addElement(new Parameter("AI_PERIOD",
            DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
        param.addElement(new Parameter("AI_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
     
        HashMap keyPersonsRow = null;
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call s2sPackage.get_key_persons ( <<AS_PROPOSAL_NUMBER>> , <<AI_PERIOD>> , "
                + "<<AI_VERSION_NUMBER>> ,  " +   "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
          for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                keyPersonsRow = (HashMap)result.elementAt(rowIndex);
                keyPersonBean = new KeyPersonBean();
                personId = (String) keyPersonsRow.get("PERSON_ID");
                keyPersonBean.setPersonId(personId);

                if(keyPersonsRow.get("SORT_ID")!=null){
                    keyPersonBean.setSortId (Integer.parseInt (keyPersonsRow.get("SORT_ID").toString()));
                }
                keyPersonBean.setFirstName((String) 
			(keyPersonsRow.get("FIRST_NAME") == null ? "Unknown" : keyPersonsRow.get("FIRST_NAME")));
                keyPersonBean.setLastName((String)
                        (keyPersonsRow.get("LAST_NAME") == null ? "Unknown" : keyPersonsRow.get("LAST_NAME")) );
                keyPersonBean.setMiddleName((String)
                        (keyPersonsRow.get("MIDDLE_NAME") == null ? "Unknown" : keyPersonsRow.get("MIDDLE_NAME")) );
                keyPersonBean.setRole((String)
                       (keyPersonsRow.get("PROJECT_ROLE") == null ? "Unknown":keyPersonsRow.get("PROJECT_ROLE")) );
           System.out.println("name is " + keyPersonBean.getLastName() + " sort id is " + keyPersonBean.getSortId());
                vecKeyPersons.add(keyPersonBean);
          }
        }
        //now vecKeyPersons may have more than n people.
        //we want to limit it to the PI, then investigators, then key persons, then budget people
   
        
        CoeusVector cvAllPersons = new CoeusVector();
        cvAllPersons.addAll(vecKeyPersons);
        CoeusVector cvNKeyPersons = getNKeyPersons(vecKeyPersons,true,numKeyPersons);
        CoeusVector cvExtraPersons = getNKeyPersons(cvAllPersons,false,numKeyPersons);
    
  
        //now get compensation for each key person
        
          for (int j=0;j<cvNKeyPersons.size();j++){
           keyPersonBean = (KeyPersonBean)cvNKeyPersons.get(j);
          compensationBean = getCompensation(keyPersonBean.getPersonId(),budgetPeriod, 
                                                                   propNumber);
           keyPersonBean.setAcademicMonths (compensationBean.getAcademicMonths()) ;
           keyPersonBean.setCalendarMonths(compensationBean.getCalendarMonths());
           keyPersonBean.setSummerMonths(compensationBean.getSummerMonths());
           keyPersonBean.setBaseSalary(compensationBean.getBaseSalary());
           keyPersonBean.setRequestedSalary(compensationBean.getRequestedSalary());
           keyPersonBean.setFundsRequested(compensationBean.getFundsRequested());
           keyPersonBean.setFringe(compensationBean.getFringe());
           //start add costSaring for fedNonFedBudget repport
           keyPersonBean.setCostSharingAmount(compensationBean.getCostSharingAmount());
           keyPersonBean.setNonFundsRequested(compensationBean.getNonFundsRequested());
           keyPersonBean.setFringeCostSharing(compensationBean.getFringeCostSharing());
           //end add costSaring for fedNonFedBudget repport
        }
           
        if (cvExtraPersons != null){
             for (int j=0;j<cvExtraPersons.size();j++){
                keyPersonBean = (KeyPersonBean)cvExtraPersons.get(j);
                compensationBean = getCompensation(keyPersonBean.getPersonId(),
                                                        budgetPeriod, propNumber);
           
                keyPersonBean.setAcademicMonths (compensationBean.getAcademicMonths()) ;
                keyPersonBean.setCalendarMonths(compensationBean.getCalendarMonths());
                keyPersonBean.setSummerMonths(compensationBean.getSummerMonths());
                keyPersonBean.setBaseSalary(compensationBean.getBaseSalary());
                keyPersonBean.setRequestedSalary(compensationBean.getRequestedSalary());
                keyPersonBean.setFundsRequested(compensationBean.getFundsRequested());
                keyPersonBean.setFringe(compensationBean.getFringe());  
                //start add costSaring for fedNonFedBudget repport
                keyPersonBean.setCostSharingAmount(compensationBean.getCostSharingAmount());
                keyPersonBean.setNonFundsRequested(compensationBean.getNonFundsRequested());
                keyPersonBean.setFringeCostSharing(compensationBean.getFringeCostSharing());
                //end add costSaring for fedNonFedBudget repport
             }        
        }
        
//        listKeyPersons.add(cv8KeyPersons);
        listKeyPersons.add(cvNKeyPersons);
        listKeyPersons.add(cvExtraPersons);
        return listKeyPersons;
    }
    
       /*  case 2801 - get base salary that is for earliest effective date that is not summer or tmp
        *  added call to get base salary separately  to take care of the case where
        * key person might have different job codes or different effective dates. use the base salary for the
        *  person with ealiest effective date that is not summer or temp, unless that's all there is
         */
    private CompensationBean getCompensation(String personId, int budgetPeriod, 
                                                               String propNumber)
      throws CoeusException, DBException{

     
     CompensationBean compensationBean = new CompensationBean();
      
      String periodType ;
      String strMonths = "0";
      BigDecimal months = new BigDecimal("0");
      BigDecimal summerMonths = new BigDecimal("0");
      BigDecimal academicMonths = new BigDecimal("0");
      BigDecimal calendarMonths = new BigDecimal("0");
      BigDecimal totalSal = new BigDecimal("0");
      BigDecimal fringe = new BigDecimal("0");
      BigDecimal baseSal = new BigDecimal("0");
      
      BigDecimal totalSalCostSharing = new BigDecimal("0");
      BigDecimal fringeCostSharing = new BigDecimal("0");
          
      //start addition case 2801
     
      int baseAmount = 0;
        BigDecimal bdBaseAmount = new BigDecimal("0");
   
        Vector param = new Vector();
       
        param.addElement(new Parameter("AMOUNT", 
              DBEngineConstants.TYPE_INT, Integer.toString(baseAmount), DBEngineConstants.DIRECTION_OUT));
        param.addElement( new Parameter("PROPOSAL_NUMBER",
              DBEngineConstants.TYPE_STRING, propNumber));
         param.addElement( new Parameter("AI_VERSION", 
          DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
        param.addElement( new Parameter("PERSON_ID",
          DBEngineConstants.TYPE_STRING, personId));
        HashMap row = null;
        Vector result = new Vector();          
      
        if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER AMOUNT>>=call s2sPackage.fn_get_base_salary( " + 
              " <<PROPOSAL_NUMBER>> , <<AI_VERSION>> , <<PERSON_ID>> ) }",
                param);  
          
           
           
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        
         if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          baseAmount = Integer.parseInt(row.get("AMOUNT").toString());  
        }                
       
        bdBaseAmount = new BigDecimal(baseAmount);
        bdBaseAmount = bdBaseAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
      
      //end case 2801 addition
      
    
      
      Vector param1 = new Vector();
      HashMap row1 = null;
      Vector result1 = new Vector();
                       
      param1.addElement( new Parameter("AS_PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
      param1.addElement( new Parameter("AI_VERSION", 
          DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
      param1.addElement ( new Parameter ("AI_PERIOD", 
          DBEngineConstants.TYPE_INT, (Integer.toString(budgetPeriod))));
      param1.addElement( new Parameter("PERSON_ID",
          DBEngineConstants.TYPE_STRING, personId));
  
      HashMap compensationRow = null;
       if(dbEngine !=null){
            result1 = new Vector(3,2);
             
            result1 = dbEngine.executeRequest("Coeus",
            "call s2sPackage.get_salary_and_months( <<AS_PROPOSAL_NUMBER>> , " +
                      "<<AI_VERSION>> , <<AI_PERIOD>> , <<PERSON_ID>> ,"+
                            "<<OUT RESULTSET rset>> )", "Coeus", param1);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result1.size();
        
        //stored procedure returns one row for each period type, with months and salary
        //period types are CC (calendar), SP (summer) , AP (academic), CY (cycle)
        
        if (listSize > 0){
           for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                compensationRow = (HashMap)result1.elementAt(rowIndex);
                compensationBean = new CompensationBean();
                periodType = (String) compensationRow.get("PERIOD_TYPE");
                
                strMonths = compensationRow.get("MONTHS").toString();
                BigDecimal tmpVal = new BigDecimal( strMonths);
                months = tmpVal.setScale(2,BigDecimal.ROUND_HALF_UP);
  /*    case 2801 commented out      
                 String strBaseSal =   compensationRow.get("BASE_SALARY")==null?
                                null:compensationRow.get("BASE_SALARY").toString();
                 if(strBaseSal!=null){
                    tmpVal = new BigDecimal(strBaseSal);
                    baseSal = tmpVal.setScale(2,BigDecimal.ROUND_HALF_UP);
                 }
  */        
                if (periodType.equals("AP")) {
                    academicMonths = academicMonths.add(months);
                            
                } else if (periodType.equals("SP")) {
                   summerMonths = summerMonths.add(months); 
                            
                }else {
                   calendarMonths = calendarMonths.add(months);
                }
                String strTotalSal = totalSal.toString();  //debug
                totalSal = totalSal.add(new BigDecimal (compensationRow.get("SALARY_REQUESTED").toString()));
                strTotalSal = totalSal.toString();  //debug
                strTotalSal = strTotalSal;
               
               
                totalSalCostSharing = totalSalCostSharing.add(new BigDecimal(compensationRow.get("COST_SHARING_AMOUNT") == null ? "0": compensationRow.get("COST_SHARING_AMOUNT").toString()));
                
              }
           
     }
     compensationBean.setAcademicMonths(academicMonths);
     compensationBean.setCalendarMonths(calendarMonths);
     compensationBean.setSummerMonths(summerMonths);
     compensationBean.setRequestedSalary(totalSal);
     compensationBean.setBaseSalary(bdBaseAmount) ;
    
     compensationBean.setCostSharingAmount(totalSalCostSharing);
   
  
     HashMap hmPF = null;
     hmPF = getPersonFringe(version,budgetPeriod,personId);
     if (hmPF != null && hmPF.size() > 0) {
         fringe = new BigDecimal(hmPF.get("FRINGE").toString());
         fringeCostSharing = new BigDecimal(hmPF.get("FRINGE_COST_SHARING").toString());
         compensationBean.setFringe(fringe);
         compensationBean.setFundsRequested(totalSal.add(fringe));
         compensationBean.setFringeCostSharing(fringeCostSharing);
         compensationBean.setNonFundsRequested(totalSalCostSharing.add(fringeCostSharing ));
     }
    
     
     return compensationBean;
    }
       
         
   
    public HashMap getCFDA (String proposalNumber)
        throws CoeusException, DBException
    {
        String CFDANumber = "";
        String activityTitle = "";
        HashMap hmCFDA = new HashMap();
        Vector param = new Vector();
        
        param.addElement( new Parameter("PROPOSAL_NUMBER",
              DBEngineConstants.TYPE_STRING, proposalNumber));
        HashMap row = null;
        Vector result = new Vector();
         
        if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  s2sPackage.getCFDA ( <<PROPOSAL_NUMBER>>  , "
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
                if (row.get("CFDA") != null)
                    CFDANumber = row.get("CFDA").toString();  
                if (row.get("ACTIVITY_TITLE") != null)
                  activityTitle = row.get("ACTIVITY_TITLE").toString();
            }
        }
        
        
        hmCFDA.put("CFDA",CFDANumber);
        hmCFDA.put("ACTIVITY_TITLE",activityTitle);
      
        return hmCFDA;
    }
    public BigDecimal getProjectIncome (String proposalNumber)
        throws CoeusException, DBException
    {
      int amount = 0;
      BigDecimal bdAmount = new BigDecimal("0");
      
      Vector param = new Vector();
       
      param.addElement(new Parameter("AMOUNT", 
              DBEngineConstants.TYPE_INT, Integer.toString(amount), DBEngineConstants.DIRECTION_OUT));
      param.addElement( new Parameter("PROPOSAL_NUMBER",
              DBEngineConstants.TYPE_STRING, proposalNumber));
      HashMap row = null;
      Vector result = new Vector();
           
      
      if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER AMOUNT>>=call s2sPackage.fn_get_project_income( " + 
              " <<PROPOSAL_NUMBER>> ) }",
                param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        
      if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          amount = Integer.parseInt(row.get("AMOUNT").toString());  
        }                
       
        bdAmount = new BigDecimal(amount);
     return bdAmount;  
    }
    
//start add costSaring for fedNonFedBudget repport   
//      private BigDecimal getPersonFringe( int version, int budgetPeriod, String personId)
//                         throws CoeusException, DBException
    private HashMap getPersonFringe( int version, int budgetPeriod, String personId)
                         throws CoeusException, DBException
//end add costSaring for fedNonFedBudget repport
    {
       //replace by procedure call instead of function
       int fringe = 0;
       BigDecimal bdFringe = new BigDecimal("0");
       //start add costSaring for fedNonFedBudget repport
       BigDecimal bdFringeCostSharing = new BigDecimal("0");
       HashMap hmPersonFringe = new HashMap();
       //end add costSaring for fedNonFedBudget repport
        
       Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
       param.addElement(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
       param.addElement(new Parameter("PERSON_ID",
                     DBEngineConstants.TYPE_STRING,  personId ));
    
        HashMap fringeRow = null;
        Vector result = new Vector();
        
       if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
             "call s2sPackage.get_fringe(<<PROPOSAL_NUMBER>> , " 
             + " <<VERSION_NUMBER>> , <<BUDGET_PERIOD>> ,<<PERSON_ID>>, <<OUT RESULTSET rset>>)",
                "Coeus", param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }    
        
        
        int vecSize = result.size();
         //should just be one row
        if (vecSize >0){
            for(int i=0;i<vecSize;i++){
                fringeRow = (HashMap) result.elementAt(i);          
                if (fringeRow.get("FRINGE") != null) {
                    bdFringe = new BigDecimal(fringeRow.get("FRINGE").toString());  
                }
                //start add costSaring for fedNonFedBudget repport
                if (fringeRow.get("FRINGE_COST_SHARING") != null) {
                    bdFringeCostSharing = new BigDecimal(fringeRow.get("FRINGE_COST_SHARING").toString());  
                }
                //end add costSaring for fedNonFedBudget repport
            }
        }
        //start add costSaring for fedNonFedBudget repport
        hmPersonFringe.put("FRINGE",bdFringe);
        hmPersonFringe.put("FRINGE_COST_SHARING",bdFringeCostSharing);      
        return hmPersonFringe;  
//     return bdFringe;
        //end add costSaring for fedNonFedBudget repport      
    }
    
       
      
    // getOtherPersonnel returns vector of OtherPersonnelBeans
     private CoeusVector  getOtherPersonnel(int budgetPeriod)
            throws CoeusException, DBException {
               
     String category, personnelType, role;
//     String sponsor = "S2S";
     CoeusVector cvOtherPersonnel = new CoeusVector();
     OtherPersonnelBean otherPersonnelBean = new OtherPersonnelBean();
        
     category = "01-Graduates" ;
     personnelType = "Grad";
     role = "Graduate Students";
     otherPersonnelBean = getOtherPersonnelDetails (budgetPeriod,category,personnelType, role);
     cvOtherPersonnel.add(otherPersonnelBean);
                               
     category = "01-PostDocs" ;
     personnelType = "PostDoc";
     role = "Post Doctoral Associates";
     otherPersonnelBean = getOtherPersonnelDetails (budgetPeriod,category,personnelType, role);
     cvOtherPersonnel.add(otherPersonnelBean);
    
      category = "01-Undergrads" ;
     personnelType = "UnderGrad";
     role = "Undergraduate Students";
     otherPersonnelBean = getOtherPersonnelDetails (budgetPeriod,category,personnelType, role);
     cvOtherPersonnel.add(otherPersonnelBean);
    
     category = "01-Secretarial" ;
     personnelType = "Sec";
     role = "Secretarial / Clerical";
     otherPersonnelBean = getOtherPersonnelDetails (budgetPeriod,category,personnelType, role);
     cvOtherPersonnel.add(otherPersonnelBean);
     
     category = "01-Other" ;
     personnelType = "Other";
     role = "Other";
     otherPersonnelBean = getOtherPersonnelDetails (budgetPeriod,category,personnelType, role);
     cvOtherPersonnel.add(otherPersonnelBean);
    
     category = "01-Other Profs" ;
     personnelType = "Other Professionals";
     role = "Other Professionals";
     otherPersonnelBean = getOtherPersonnelDetails (budgetPeriod,category,personnelType, role);
     cvOtherPersonnel.add(otherPersonnelBean);

     category = "LASALARIES" ;
     personnelType = "Allocated Admin Support";
     role = "Allocated Admin Support";
     otherPersonnelBean = getOtherPersonnelDetails (budgetPeriod,category,personnelType, role);
     cvOtherPersonnel.add(otherPersonnelBean);
    
     return cvOtherPersonnel;
     }
     
     private OtherPersonnelBean getOtherPersonnelDetails (int budgetPeriod,
                            String category, String personnelType,String role)
        throws CoeusException , DBException {
                                
     OtherPersonnelBean otherPersonnelBean = new OtherPersonnelBean();
     int  count;    
     BigDecimal bdSalary , bdFringe;
     BigDecimal bdFunds = new BigDecimal("0");
     HashMap hmMonths = null;
     
     //start add costSaring for fedNonFedBudget repport
     bdSalary = new BigDecimal("0");
     bdFringe = new BigDecimal("0");
     BigDecimal bdSalaryCostSharing = new BigDecimal("0");
     BigDecimal bdFringeCostSharing = new BigDecimal("0");
     BigDecimal bdNonFunds = new BigDecimal("0");
     HashMap hmOtherPersonSalary = null;
     HashMap hmOtherPersonFringe = null;
     hmOtherPersonSalary = getOtherPersonSalary(budgetPeriod, category);
     if (hmOtherPersonSalary != null && hmOtherPersonSalary.size() > 0) {
         bdSalary = new BigDecimal(hmOtherPersonSalary.get("SALARY").toString());
         bdSalaryCostSharing = new BigDecimal(hmOtherPersonSalary.get("SALARY_COST_SHARING").toString());
     }
     
     hmOtherPersonFringe = getOtherPersonFringe(budgetPeriod , category);
     if (hmOtherPersonFringe != null && hmOtherPersonFringe.size() > 0) {
         bdFringe = new BigDecimal(hmOtherPersonFringe.get("OTHER_FRINGE").toString());
         bdFringeCostSharing = new BigDecimal(hmOtherPersonFringe.get("OTHER_FRINGE_COSTSHARING").toString());
     }
     
     bdNonFunds =  bdSalaryCostSharing.add(bdFringeCostSharing) ;
     
//     bdSalary = getOtherPersonSalary(budgetPeriod, category);
//
//
//     bdFringe = getOtherPersonFringe(budgetPeriod , category);
     //end add costSaring for fedNonFedBudget repport
     bdFunds =  bdSalary.add(bdFringe) ;
     
     hmMonths = getOtherPersonMonths(budgetPeriod, category);
      
     
     count = getOtherPersonCount( budgetPeriod, category);
     totCountOtherPersonnel = totCountOtherPersonnel + count;
     otherPersonnelBean.setNumberPersonnel(new BigInteger(Integer.toString(count)));
     otherPersonnelBean.setPersonnelType(personnelType);
     otherPersonnelBean.setRole(role);
             
     CompensationBean compensationBean = new CompensationBean();
    
     //not sure that we need base salary
     compensationBean.setBaseSalary(new BigDecimal("0"));
     compensationBean.setFringe(bdFringe);
     compensationBean.setFundsRequested(bdFunds);
     compensationBean.setRequestedSalary(bdSalary);
     compensationBean.setSummerMonths(new BigDecimal(hmMonths.get("SP").toString()));
     compensationBean.setAcademicMonths(new BigDecimal(hmMonths.get("AP").toString()));
     compensationBean.setCalendarMonths(new BigDecimal(hmMonths.get("CC").toString()));
   
     //start add costSaring for fedNonFedBudget repport
     compensationBean.setFringeCostSharing(bdFringeCostSharing);
     compensationBean.setNonFundsRequested(bdNonFunds);
     compensationBean.setCostSharingAmount(bdSalaryCostSharing);
     //end add costSaring for fedNonFedBudget repport

     otherPersonnelBean.setCompensation(compensationBean);
   
     return otherPersonnelBean;
     }
     
          /////////////////////////////////////
     // getSubmissionType
     /////////////////////////////////////
     public HashMap getSubmissionType(String propNumber)
            throws CoeusException, DBException
     {
       Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       
       HashMap row = null;
       Vector result = new Vector(3,2);
      
       if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
             "call s2sPackage.getSubmissionType( <<PROPOSAL_NUMBER>> , "
                + " <<OUT RESULTSET rset>>)","Coeus",param);
                
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
           
         int vecSize = result.size();
     
         if (vecSize >0){
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);  
            }
        }
        return row;
         
     }
     
      /////////////////////////////////////
     // getSF424SubmissionType
     /////////////////////////////////////
     public HashMap getSF424SubmissionType(String propNumber)
            throws CoeusException, DBException
     {
       Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       
       HashMap row = null;
       Vector result = new Vector(3,2);
      
       if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
             "call s2sPackage.getSF424SubmissionType( <<PROPOSAL_NUMBER>> , "
                + " <<OUT RESULTSET rset>>)","Coeus",param);
                
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
           
         int vecSize = result.size();
     
         if (vecSize >0){
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);  
            }
        }
        return row;
         
     }
     
     /////////////////////////////////////
     // getSF424RevisionType
     /////////////////////////////////////
     public HashMap getSF424RevisionType(String propNumber)
            throws CoeusException, DBException
     {
       Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       
       HashMap row = null;
       Vector result = new Vector(3,2);
      
       if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
             "call s2sPackage.getSF424RevisionType( <<PROPOSAL_NUMBER>> , "
                + " <<OUT RESULTSET rset>>)","Coeus",param);
                
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
           
         int vecSize = result.size();
     
         if (vecSize >0){
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);  
            }
        }
        return row;
         
     }
     
      /////////////////////////////////////
     // getApplicationType
     /////////////////////////////////////
     public HashMap getApplicationType(String propNumber)
            throws CoeusException, DBException
     {
       Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       
       HashMap row = null;
       Vector result = new Vector(3,2);
      
       if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
             "call s2sPackage.getApplicationType( <<PROPOSAL_NUMBER>> , "
                + " <<OUT RESULTSET rset>>)","Coeus",param);
                
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
           
         int vecSize = result.size();
     
         if (vecSize >0){
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);  
            }
        }
        return row;
         
     }
     
     /////////////////////////////////////
     // getSF424ApplicationType
     /////////////////////////////////////
     public HashMap getSF424ApplicationType(String propNumber)
            throws CoeusException, DBException
     {
       Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       
       HashMap row = null;
       Vector result = new Vector(3,2);
      
       if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
             "call s2sPackage.getSF424ApplicationType( <<PROPOSAL_NUMBER>> , "
                + " <<OUT RESULTSET rset>>)","Coeus",param);
                
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
           
         int vecSize = result.size();
     
         if (vecSize >0){
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);  
            }
        }
        return row;
         
     }
     
     ///////////////////////////////////////////////
     // getOtherPersonMonths 
     //  added for case 1924
     ///////////////////////////////////////////////
     
     private HashMap getOtherPersonMonths(int budgetPeriod, String category)
            throws CoeusException, DBException
     {
      HashMap hmMonths = new HashMap();
      BigDecimal bdMonths = new BigDecimal("0");
      BigDecimal bdSummerMonths = new BigDecimal("0");
      BigDecimal bdCalendarMonths = new BigDecimal("0");
      BigDecimal bdAcademicMonths = new BigDecimal("0");
      BigDecimal bdCycleMonths = new BigDecimal("0");
   
    
      Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
       param.addElement(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
       param.addElement(new Parameter("CATEGORY",
                     DBEngineConstants.TYPE_STRING,  category ));
  
       HashMap row = null;
       Vector result = new Vector(3,2);
      
       if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
             "call s2sPackage.getOtherPersonnelMonths( <<PROPOSAL_NUMBER>> ,<<VERSION_NUMBER>> , "
                + " <<BUDGET_PERIOD>> ,<<CATEGORY>>, <<OUT RESULTSET rset>>)","Coeus",param);
                
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
           
         int vecSize = result.size();
     
         if (vecSize >0){
            for(int i=0;i<vecSize;i++){
                row = (HashMap) result.elementAt(i);  
                bdMonths = new BigDecimal(row.get("MONTHS") == null ? "0" :
                                      row.get("MONTHS").toString());  
               System.out.println("period type is" + row.get("PERIOD_TYPE").toString());
               
                if (row.get("PERIOD_TYPE").toString().equals("AP"))
                    bdAcademicMonths = bdMonths;
                if (row.get("PERIOD_TYPE").toString().equals( "SP"))
                    bdSummerMonths = bdMonths;
                if (row.get("PERIOD_TYPE").toString().equals("CC") )
                    bdCalendarMonths = bdMonths;
                if (row.get("PERIOD_TYPE").toString().equals("CY"))        
                    bdCycleMonths = bdMonths;
            }
        }
        
        bdCalendarMonths = bdCalendarMonths.add(bdCycleMonths);
        hmMonths.put("AP",bdAcademicMonths);
        hmMonths.put("SP",bdSummerMonths);
        hmMonths.put("CC",bdCalendarMonths);
        
      
      return hmMonths;
     
     }
     
     //changed this to a procedure instead of a function, 
     //start add costSaring for fedNonFedBudget repport
//      private BigDecimal getOtherPersonSalary( int budgetPeriod,String category)
//                         throws CoeusException, DBException
      private HashMap getOtherPersonSalary( int budgetPeriod,String category)
                         throws CoeusException, DBException
    //end add costSaring for fedNonFedBudget repport
                         
    {
       BigDecimal salary = new BigDecimal("0");
       //start add costSaring for fedNonFedBudget repport
       BigDecimal salaryCostSharing = new BigDecimal("0");
       HashMap hmOtherPersonSalary = new HashMap();
       //end add costSaring for fedNonFedBudget repport
       
       Vector param = new Vector();
       
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
       param.addElement(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
       param.addElement(new Parameter("CATEGORY",
                     DBEngineConstants.TYPE_STRING,  category ));
  
        HashMap otherPersonsRow = null;
        Vector result = new Vector(3,2);
                
       if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
             "call s2sPackage.getOtherPersonnelSal( <<PROPOSAL_NUMBER>> ,<<VERSION_NUMBER>> , "
                + " <<BUDGET_PERIOD>> ,<<CATEGORY>>, <<OUT RESULTSET rset>>)","Coeus",param);
                
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        int vecSize = result.size();
         //should just be one row
        if (vecSize >0){
          
                otherPersonsRow = (HashMap) result.elementAt(0);    
                salary = new BigDecimal(otherPersonsRow.get("SALARY") == null ? "0" :
                                        otherPersonsRow.get("SALARY").toString()); 
               //start add costSaring for fedNonFedBudget repport
               salaryCostSharing = new BigDecimal(otherPersonsRow.get("SALARY_COST_SHARING") == null ? "0" :
                                        otherPersonsRow.get("SALARY_COST_SHARING").toString()); 
              //end add costSaring for fedNonFedBudget repport
                                        
           
        }     
     //start add costSaring for fedNonFedBudget repport
     hmOtherPersonSalary.put("SALARY", salary);
     hmOtherPersonSalary.put("SALARY_COST_SHARING", salaryCostSharing);
     return hmOtherPersonSalary;
//     return salary; 
    //end add costSaring for fedNonFedBudget repport        
    }
    
      
     //start add costSaring for fedNonFedBudget repport
//       private BigDecimal getOtherPersonFringe( int budgetPeriod,String category)
//
//                         throws CoeusException, DBException
       private HashMap getOtherPersonFringe( int budgetPeriod,String category)
                         throws CoeusException, DBException                  
    //end add costSaring for fedNonFedBudget repport                     
    {
   
        BigDecimal fringe = new BigDecimal("0");     
       //start add costSaring for fedNonFedBudget repport
       BigDecimal fringeCostSharing = new BigDecimal("0");
       HashMap hmOtherPersonFringe = new HashMap();
       //end add costSaring for fedNonFedBudget repport
       
       Vector param = new Vector();
       
    
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.addElement(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
       param.addElement(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
       param.addElement(new Parameter("CATEGORY",
                     DBEngineConstants.TYPE_STRING,  category ));
    
        HashMap otherPersonsRow = null;
        Vector result = new Vector();
                
        System.out.println("...calling get_other_pers_fringe, period is " + budgetPeriod + 
                 " category is " + category);
       if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
             "call s2sPackage.get_Other_Pers_Fringe( <<PROPOSAL_NUMBER>> ,<<VERSION_NUMBER>> , "
             + " <<BUDGET_PERIOD>> ,<<CATEGORY>> , <<OUT RESULTSET rset>>)","Coeus", param);  
     
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        int vecSize = result.size();
        //should just be one row
        if (vecSize >0) {
        
            otherPersonsRow = (HashMap) result.elementAt(0);
            fringe = new BigDecimal(otherPersonsRow.get("OTHER_FRINGE") == null ? "0" :
                                        otherPersonsRow.get("OTHER_FRINGE").toString());  
            
            //start add costSaring for fedNonFedBudget repport
            fringeCostSharing = new BigDecimal(otherPersonsRow.get("OTHER_FRINGE_COSTSHARING") == null ? "0" :
                                        otherPersonsRow.get("OTHER_FRINGE_COSTSHARING").toString()); 
            //end add costSaring for fedNonFedBudget repport
        } 
     //start add costSaring for fedNonFedBudget repport
     hmOtherPersonFringe.put("OTHER_FRINGE", fringe);
     hmOtherPersonFringe.put("OTHER_FRINGE_COSTSHARING", fringeCostSharing);
     return hmOtherPersonFringe;   
//     return fringe;  
     //end add costSaring for fedNonFedBudget repport 
    }
    
       
 private int getOtherPersonCount( int budgetPeriod,String category)
                         throws CoeusException, DBException
    {
       int count = 0;    
       
       Vector param = new Vector();
       
       param.addElement(new Parameter("COUNT", 
              DBEngineConstants.TYPE_INT, Integer.toString(count), DBEngineConstants.DIRECTION_OUT));
     
       param.add( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.add(new Parameter("VERSION_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
       param.add(new Parameter("BUDGET_PERIOD",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
       param.add(new Parameter("CATEGORY",
                     DBEngineConstants.TYPE_STRING,  category ));
     //  param.add(new Parameter("SPONSOR", 
     //                DBEngineConstants.TYPE_STRING, sponsor ));
                 
       
       HashMap otherPersonsRow = null;
       Vector result = new Vector();
        
       if(dbEngine !=null){
    
           
            result = dbEngine.executeFunctions("Coeus",
              "{<<OUT INTEGER COUNT>>=call s2sPackage.fnGetCountOtherPersonnel( " + 
              " <<PROPOSAL_NUMBER>> ,<<VERSION_NUMBER>> , <<BUDGET_PERIOD>> ,<<CATEGORY>> )}",
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
    
    /*
     *  This method is used to delete proposal information from the temporary table
     *  used in getting indirect cost rates.
     * 
     * obsolete now that rate and base table are permanent *
   

    public void cleanUpTempTble(String propNumber)
        throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        int reslt = 0;
        
               
        param.add(new Parameter("RESLT", 
              DBEngineConstants.TYPE_INT, Integer.toString(reslt), DBEngineConstants.DIRECTION_OUT));
   
       
        param.add(new Parameter("AV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,propNumber));
        if(dbEngine!=null){
       

            result = dbEngine.executeFunctions("Coeus",




             "{ <<OUT INTEGER RESLT>>= call pkg_oh_rate_and_base.fn_cleanup( " + 
              " <<AV_PROPOSAL_NUMBER>> ) }", param);  
 
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
 

        return ;
    }
*/

    
  /** getInDirectCosts 
   * 
  */
       public IndirectCostBean getInDirectCosts(String propNumber, int budgetPeriod, int version,
                                               BudgetPeriodDataBean  budgetPeriodBean)
       throws CoeusException, DBException
       {
        
        //    cleanUpTempTble(propNumber);
        
       
       CoeusVector cvIndirectCostDetails = new CoeusVector();
       IndirectCostDetailBean indirectCostDetailBean = new IndirectCostDetailBean();
       IndirectCostBean indirectCostBean = new IndirectCostBean();
       BigDecimal totalIndirectCosts = new BigDecimal("0");
       //start add costSaring for fedNonFedBudget repport
       BigDecimal totalIndirectCostSharing = new BigDecimal("0");
       //end add costSaring for fedNonFedBudget repport
       
        Vector result = null;
        Vector param = new Vector();
                               
   	param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,propNumber));
        param.addElement(new Parameter("AI_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
        param.addElement(new Parameter("AI_PERIOD",
            DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
       
        HashMap resultRow = null;
                 
       if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call pkg_oh_rate_and_base.get_oh_rate_and_base_for_per ( <<AS_PROPOSAL_NUMBER>> , <<AI_VERSION_NUMBER>> , "
                + "<<AI_PERIOD>> , " +   "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        System.out.println("size of returned set from base and rate is " + result.size());
       
        
         if (listSize > 0){            
                for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                       resultRow = (HashMap)result.elementAt(rowIndex);
                       indirectCostDetailBean = new IndirectCostDetailBean();
                       indirectCostDetailBean.setBase(new BigDecimal(resultRow.get("BASE_COST").toString()));    
                       indirectCostDetailBean.setRate(new BigDecimal(resultRow.get("APPLIED_RATE").toString()));
                       indirectCostDetailBean.setFunds(new BigDecimal(resultRow.get("CALCULATED_COST").toString()));
                       //start add costSaring for fedNonFedBudget repport
                       indirectCostDetailBean.setBaseCostSharing(new BigDecimal (resultRow.get("BASE_COST_SHARING") == null ? "0" :
                                                    resultRow.get("BASE_COST_SHARING").toString()));
                       indirectCostDetailBean.setCostSharing(new BigDecimal (resultRow.get("CALCULATED_COST_SHARING") == null ? "0" :
                                                    resultRow.get("CALCULATED_COST_SHARING").toString()));
                       totalIndirectCostSharing = totalIndirectCostSharing.add(indirectCostDetailBean.getCostSharing()); 
                       //end add costSaring for fedNonFedBudget repport
                       totalIndirectCosts = totalIndirectCosts.add(new BigDecimal(resultRow.get("CALCULATED_COST").toString()));
                       indirectCostDetailBean.setCostType(resultRow.get("DESCRIPTION").toString());
                       // indirectCostDetailBean.setCostType(resultRow.get("RATE_CLASS_CODE").toString());
                 
                       cvIndirectCostDetails.add(indirectCostDetailBean);        
                  }
      
                }
              
            //adjust for rounding problem
            BigDecimal bdAdjustment = new BigDecimal("0");
            BigDecimal totIndirectFromPeriodBean = budgetPeriodBean.getTotalIndirectCost();
            int difference = totIndirectFromPeriodBean.compareTo(totalIndirectCosts);
            /*for debug
            String testBeanValue = totIndirectFromPeriodBean.toString();
            String testVectValue = totalIndirectCosts.toString();
            end for debug */
            if (difference != 0){
                bdAdjustment = totIndirectFromPeriodBean.subtract(totalIndirectCosts);   
                String strBdAdj = bdAdjustment.toString();  //debug
                //get first element in cvIndirectCostDetails
                if (cvIndirectCostDetails.size() > 0){
                    IndirectCostDetailBean adjustmentBean = (IndirectCostDetailBean) cvIndirectCostDetails.get(0);
                    BigDecimal indCost = adjustmentBean.getFunds();
                    String strIndCost = indCost.toString();  //debug
                    BigDecimal newCost = indCost.add(bdAdjustment);
                    String strNewCost = newCost.toString();  //debug
                    adjustmentBean.setFunds(newCost);
                    cvIndirectCostDetails.set(0,adjustmentBean);
                    IndirectCostDetailBean testBean = (IndirectCostDetailBean) cvIndirectCostDetails.get(0);
                    BigDecimal bdAfter = testBean.getFunds();
                    String strAfter = bdAfter.toString();  //debug
                    totalIndirectCosts = totalIndirectCosts.add(bdAdjustment);
                }
            }
                
          
            
          indirectCostBean.setIndirectCostDetails(cvIndirectCostDetails);
          indirectCostBean.setTotalIndirectCosts(totalIndirectCosts);
          
          //start add costSaring for fedNonFedBudget repport
          indirectCostBean.setTotalIndirectCostSharing(totalIndirectCostSharing);
          //end add costSaring for fedNonFedBudget repport
     
         return indirectCostBean;
       }
       
  /** getInDirectCosts 
   * overloaded method for call from proposal printing.
  */
       public IndirectCostBean getInDirectCosts(String propNumber, int budgetPeriod, int version)
       throws CoeusException, DBException
       {
      
       CoeusVector cvIndirectCostDetails = new CoeusVector();
       IndirectCostDetailBean indirectCostDetailBean = new IndirectCostDetailBean();
       IndirectCostBean indirectCostBean = new IndirectCostBean();
       BigDecimal totalIndirectCosts = new BigDecimal("0");
       
        Vector result = null;
        Vector param = new Vector();
                               
   	param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,propNumber));
        param.addElement(new Parameter("AI_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
        param.addElement(new Parameter("AI_PERIOD",
            DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
       
        HashMap resultRow = null;
                 
       if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call pkg_oh_rate_and_base.get_oh_rate_and_base_for_per ( <<AS_PROPOSAL_NUMBER>> , <<AI_VERSION_NUMBER>> , "
//              "call pkg_oh_rate_and_base.get_rates_and_bases ( <<AS_PROPOSAL_NUMBER>> , <<AI_VERSION_NUMBER>> , "
                    + "<<AI_PERIOD>> , " +   "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        System.out.println("size of returned set from base and rate is " + result.size());
       
        
         if (listSize > 0){            
             for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                       resultRow = (HashMap)result.elementAt(rowIndex);
                       indirectCostDetailBean = new IndirectCostDetailBean();
                       indirectCostDetailBean.setBase(new BigDecimal(resultRow.get("BASE_COST").toString()));    
                       indirectCostDetailBean.setRate(new BigDecimal(resultRow.get("APPLIED_RATE").toString()));
                       indirectCostDetailBean.setFunds(new BigDecimal(resultRow.get("CALCULATED_COST").toString()));
                       totalIndirectCosts = totalIndirectCosts.add(new BigDecimal(resultRow.get("CALCULATED_COST").toString()));
                       indirectCostDetailBean.setCostType(resultRow.get("DESCRIPTION").toString());
                       // indirectCostDetailBean.setCostType(resultRow.get("RATE_CLASS_CODE").toString());
                 
                       cvIndirectCostDetails.add(indirectCostDetailBean);        
                  }
      
             }                
           
          indirectCostBean.setIndirectCostDetails(cvIndirectCostDetails);
          indirectCostBean.setTotalIndirectCosts(totalIndirectCosts);      
      
         return indirectCostBean;
       }
       
 /** getOtherDirectCosts - returns vector of OtherDirectCostBeans (will only be one element) 
   * 
  */
       private CoeusVector getOtherDirectCosts( int budgetPeriod, String sponsor)
       throws CoeusException, DBException
       {
        sponsor = "S2S";
        CostBean costBean = new CostBean();;
        OtherDirectCostBean otherDirectCostBean = new OtherDirectCostBean();
        CoeusVector cvCostBeans = new CoeusVector();   
        CoeusVector cvOtherDirectCostBean = new CoeusVector();
     
       
        Vector result = null;
        Vector param = new Vector();
                               
   	param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,propNumber));
        param.addElement(new Parameter("AI_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT, ( Integer.toString(version))));
        param.addElement(new Parameter("AI_PERIOD",
            DBEngineConstants.TYPE_INT, ( Integer.toString(budgetPeriod))));
        param.addElement(new Parameter("AS_SPONSOR", 
             DBEngineConstants.TYPE_STRING,sponsor));
        HashMap directCostRow = null;
                 
       if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call s2sPackage.getCostsForPrinting ( <<AS_PROPOSAL_NUMBER>> , <<AI_VERSION_NUMBER>> , "
                + "<<AI_PERIOD>> , <<AS_SPONSOR>> , " +   "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
           
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                directCostRow = (HashMap)result.elementAt(rowIndex);
                costBean = new CostBean();
             
                costBean.setBudgetPeriod(Integer.parseInt(directCostRow.get("BUDGETPERIOD").toString()));
                costBean.setCost(new BigDecimal(directCostRow.get("COST").toString()));  
                //start add costSaring for fedNonFedBudget repport
                costBean.setCostSharing(new BigDecimal(directCostRow.get("COST_SHARING") == null ? "0" :
                                                    directCostRow.get("COST_SHARING").toString()));
                //end add costSaring for fedNonFedBudget repport
                costBean.setCategory((String)directCostRow.get("BUDGETCATEGORYDESC"));
                costBean.setCategoryType((String)directCostRow.get("CATEGORYTYPE")); 
                costBean.setQuantity(Integer.parseInt(directCostRow.get("QUANTITY").toString()));
              
                cvCostBeans.add(costBean);
            }
        }
    
         
       //now we have a vector of costBeans for the period. each element in the vector is a 
       // cost bean containing the cost and the budget category
       // aggregate into otherDirectCostBean, adding it to a vector, but this is really unnecssary, since 
        // there will only be one element
        BigDecimal totalOtherDirect = new BigDecimal("0");
        BigDecimal cost = new BigDecimal("0");
        BigDecimal totalTravelCost = new BigDecimal("0");
        BigDecimal totalParticipantCost = new BigDecimal("0");
        BigDecimal totalPartSubsistCost = new BigDecimal("0");  //change for participant costs
        BigDecimal totalPartTuitionCost = new BigDecimal("0");  //change for participant costs")
        int totalParticipantCount = 0;
        //start add costSaring for fedNonFedBudget repport
        BigDecimal totalOtherDirectCostSharing = new BigDecimal("0");
        BigDecimal costSharing = new BigDecimal("0");
        BigDecimal totalTravelCostSharing = new BigDecimal("0");
        BigDecimal totalParticipantCostSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport

        //filter cvCostBeans
        Equals equalsMaterials = new Equals ("category", "Materials and Supplies");             
        Equals equalsPubs = new Equals ("category", "Publication Costs");             
        Equals equalsConsultants = new Equals ("category","Consultant Costs");
        Equals equalsComputer = new Equals ("category","Computer Services");
        Equals equalsSubcontract = new Equals ("category","Subcontract");
        Equals equalsEquipRental = new Equals ("category","Equipment Rental");
        Equals equalsAlterations = new Equals ("category","Alterations");
        Equals equalsDomesticTravel = new Equals ("category","Domestic Travel");
        Equals equalsForeignTravel = new Equals ("category","Foreign Travel");
        Equals equalsOther = new Equals ("category","Other Direct Costs");
        Equals equalsPartTravel = new Equals ("category","Participant Travel");
        Equals equalsPartStipends = new Equals ("category","Participant Stipends");
        Equals equalsPartTuition = new Equals ("category","Participant Tuition");  //addition for participant cost
        Equals equalsPartSubsistence = new Equals ("category","Participant Subsistence"); //addition for participant cost
        Equals equalsPartOther = new Equals ("category","Participant Other");
   
        
        NotEquals notMaterials = new NotEquals("category", "Materials and Supplies"); 
        NotEquals notPubs = new NotEquals ("category", "Publication Costs");             
        NotEquals notConsultants = new NotEquals ("category","Consultant Costs");
        NotEquals notComputer = new NotEquals ("category","Computer Services");
        NotEquals notSubcontract = new NotEquals ("category","Subcontract");
        NotEquals notEquipRental = new NotEquals ("category","Equipment Rental");
        NotEquals notAlterations = new NotEquals ("category","Alterations");
        NotEquals notDomTravel = new NotEquals ("category","Domestic Travel");
        NotEquals notForTravel = new NotEquals ("category","Foreign Travel");
        NotEquals notOther = new NotEquals ("category","Other Direct Costs");      
        NotEquals notPartTravel = new NotEquals ("category","Participant Travel");
        NotEquals notPartStipends = new NotEquals ("category","Participant Stipends");
        NotEquals notPartTuition = new NotEquals ("category","Participant Tuition");  //addition for participant cost
        NotEquals notPartSubsistence = new NotEquals ("category","Participant Subsistence"); //addition for participant cost
        NotEquals notPartOther = new NotEquals ("category","Participant Other");
     
        And other1 = new And (notMaterials, notPubs);
        And other2 = new And( other1,  notConsultants);
        And other3 = new And (other2, notComputer);
        And other4 = new And (other3, notSubcontract);
        And other5 = new And (other4, notEquipRental);
        And other6 = new And (other5, notAlterations);
        And other7 = new And (other6, notDomTravel );
        And other8 = new And (other7, notForTravel);
        And other9 = new And (other8, notPartTravel);
        And other10= new And (other9, notPartStipends);
        And other11= new And (other10,notPartOther);
        And other12 = new And (other11,notPartTuition);   
        And other13 = new And (other12,notPartSubsistence);
        And other = new And (other13, notOther);
         
      
     
        CoeusVector cvMaterials = cvCostBeans.filter(equalsMaterials);
        cost= new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
         for (int i = 0; i < cvMaterials.size(); i++){
            CostBean material = (CostBean) cvMaterials.get(i);
            cost = cost.add(material.getCost());
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(material.getCostSharing());
            //end add costSaring for fedNonFedBudget repport
        }      
        otherDirectCostBean.setmaterials(cost);
        totalOtherDirect = totalOtherDirect.add(cost);
        //start add costSaring for fedNonFedBudget repport
        otherDirectCostBean.setMaterialsCostSharing(costSharing);
        totalOtherDirectCostSharing = totalOtherDirectCostSharing.add(costSharing);
        //end add costSaring for fedNonFedBudget repport
            
        CoeusVector cvConsultants = cvCostBeans.filter(equalsConsultants);
            
        cost= new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i = 0; i < cvConsultants.size(); i++){
            CostBean consultant = (CostBean) cvConsultants.get(i);
            cost = cost.add(consultant.getCost());
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(consultant.getCostSharing());
            //end add costSaring for fedNonFedBudget repport
        }
        otherDirectCostBean.setconsultants(cost);
         totalOtherDirect = totalOtherDirect.add(cost);
         //start add costSaring for fedNonFedBudget repport
        otherDirectCostBean.setConsultantsCostSharing(costSharing);
        totalOtherDirectCostSharing = totalOtherDirectCostSharing.add(costSharing);
        //end add costSaring for fedNonFedBudget repport
         
        CoeusVector cvPubs = cvCostBeans.filter(equalsPubs);
             System.out.println("size of cvPubs is " + cvPubs.size());
  
        cost= new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i = 0; i < cvPubs.size(); i++){
           CostBean pubs = (CostBean) cvPubs.get(i);
           cost = cost.add(pubs.getCost());
           //start add costSaring for fedNonFedBudget repport
           costSharing = costSharing.add(pubs.getCostSharing());
           //end add costSaring for fedNonFedBudget repport
        }
        otherDirectCostBean.setpublications(cost);
        totalOtherDirect = totalOtherDirect.add(cost);
        //start add costSaring for fedNonFedBudget repport
        otherDirectCostBean.setPublicationsCostSharing(costSharing);
        totalOtherDirectCostSharing = totalOtherDirectCostSharing.add(costSharing);
        //end add costSaring for fedNonFedBudget repport
      
        CoeusVector cvComputers = cvCostBeans.filter(equalsComputer);
        cost= new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i = 0; i < cvComputers.size(); i++){
            CostBean computers = (CostBean) cvComputers.get(i);
            cost = cost.add(computers.getCost());
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(computers.getCostSharing());
            //end add costSaring for fedNonFedBudget repport
        }
        otherDirectCostBean.setcomputer(cost);
        totalOtherDirect = totalOtherDirect.add(cost);
        //start add costSaring for fedNonFedBudget repport
        otherDirectCostBean.setComputerCostSharing(costSharing);
        totalOtherDirectCostSharing = totalOtherDirectCostSharing.add(costSharing);
        //end add costSaring for fedNonFedBudget repport
        
        CoeusVector cvAlterations = cvCostBeans.filter(equalsAlterations);
         
        cost = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i = 0; i < cvAlterations.size(); i++){
                CostBean alterations = (CostBean) cvAlterations.get(i);
                cost = cost.add(alterations.getCost());
                //start add costSaring for fedNonFedBudget repport
                costSharing = costSharing.add(alterations.getCostSharing());
                //end add costSaring for fedNonFedBudget repport
         }
        otherDirectCostBean.setAlterations(cost);
        totalOtherDirect = totalOtherDirect.add(cost);
        //start add costSaring for fedNonFedBudget repport
        otherDirectCostBean.setAlterationsCostSharing(costSharing);
        totalOtherDirectCostSharing = totalOtherDirectCostSharing.add(costSharing);
        //end add costSaring for fedNonFedBudget repport
      
        CoeusVector cvSubcontract = cvCostBeans.filter(equalsSubcontract);
        cost = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i = 0; i < cvSubcontract.size(); i++){
            CostBean subcontracts = (CostBean) cvSubcontract.get(i);
            cost = cost.add(subcontracts.getCost());
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(subcontracts.getCostSharing());
           //end add costSaring for fedNonFedBudget repport
         }
        otherDirectCostBean.setsubAwards(cost);
        totalOtherDirect = totalOtherDirect.add(cost);
        //start add costSaring for fedNonFedBudget repport
        otherDirectCostBean.setSubAwardsCostSharing(costSharing);
        totalOtherDirectCostSharing = totalOtherDirectCostSharing.add(costSharing);
        //end add costSaring for fedNonFedBudget repport
       
        CoeusVector cvEquipRental = cvCostBeans.filter(equalsEquipRental);
        cost = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i = 0; i < cvEquipRental.size(); i++){
            CostBean equipRental = (CostBean) cvEquipRental.get(i);
            cost = cost.add(equipRental.getCost());
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(equipRental.getCostSharing());
            //end add costSaring for fedNonFedBudget repport
         }
        otherDirectCostBean.setEquipRental(cost);
        totalOtherDirect = totalOtherDirect.add(cost);
        //start add costSaring for fedNonFedBudget repport
        otherDirectCostBean.setEquipRentalCostSharing(costSharing);
        totalOtherDirectCostSharing = totalOtherDirectCostSharing.add(costSharing);
        //end add costSaring for fedNonFedBudget repport
        
        CoeusVector cvDomesticTravel = cvCostBeans.filter(equalsDomesticTravel);
        cost = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i = 0; i < cvDomesticTravel.size(); i++) {
            CostBean domTravel = (CostBean) cvDomesticTravel.get(i);
            cost = cost.add(domTravel.getCost());
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(domTravel.getCostSharing());
            //end add costSaring for fedNonFedBudget repport
        }
        otherDirectCostBean.setDomTravel(cost);
        totalTravelCost = totalTravelCost.add(cost);
        //don't add to total other direct cost, because travel is not part of other direct cost
        //start add costSaring for fedNonFedBudget repport
        otherDirectCostBean.setDomTravelCostSharing(costSharing);
        totalTravelCostSharing = totalTravelCostSharing.add(costSharing);
        //end add costSaring for fedNonFedBudget repport
        
        CoeusVector cvForeignTravel = cvCostBeans.filter(equalsForeignTravel);
        cost = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i = 0; i < cvForeignTravel.size(); i++) {
            CostBean foreignTravel = (CostBean) cvForeignTravel.get(i);
            cost = cost.add(foreignTravel.getCost());
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(foreignTravel.getCostSharing());
            //end add costSaring for fedNonFedBudget repport
        }
        otherDirectCostBean.setForeignTravel(cost);
        totalTravelCost = totalTravelCost.add(cost);
        //don't add to total other direct cost, because travel is not part of other direct cost
        //start add costSaring for fedNonFedBudget repport
        otherDirectCostBean.setForeignTravelCostSharing(costSharing);
        totalTravelCostSharing = totalTravelCostSharing.add(costSharing);
        
        otherDirectCostBean.setTotTravelCostSharing(totalTravelCostSharing);
        //end add costSaring for fedNonFedBudget repport
        
        otherDirectCostBean.setTotTravel(totalTravelCost);
        
        CoeusVector cvParticipantStipends = cvCostBeans.filter(equalsPartStipends);
        //cvCostBeans is a vector  containing costBeans for participant stipends
        cost = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget report
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i=0 ; i<cvParticipantStipends.size();i++) {
            CostBean participantStipend = (CostBean) cvParticipantStipends.get(i);
            cost = cost.add(participantStipend.getCost());      
            totalParticipantCost = totalParticipantCost.add(cost);
            totalParticipantCount = totalParticipantCount + participantStipend.getQuantity();
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(participantStipend.getCostSharing());
            totalParticipantCostSharing = totalParticipantCostSharing.add(costSharing);
            //end add costSaring for fedNonFedBudget repport
        }
         otherDirectCostBean.setPartStipends(cost);
        //start add costSaring for fedNonFedBudget repport
         otherDirectCostBean.setPartStipendsCostSharing(costSharing);
        //end add costSaring for fedNonFedBudget repport
         
        CoeusVector cvParticipantTravel = cvCostBeans.filter(equalsPartTravel);
        cost = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i = 0; i< cvParticipantTravel.size();i++) {
            CostBean participantTravel = (CostBean) cvParticipantTravel.get(i);
            cost = cost.add(participantTravel.getCost());
            totalParticipantCost = totalParticipantCost.add(cost);
            totalParticipantCount = totalParticipantCount + participantTravel.getQuantity();
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(participantTravel.getCostSharing());
            totalParticipantCostSharing = totalParticipantCostSharing.add(costSharing);
            //end add costSaring for fedNonFedBudget repport
        }
         otherDirectCostBean.setPartTravel(cost);
         //start add costSaring for fedNonFedBudget repport
         otherDirectCostBean.setPartTravelCostSharing(costSharing);
        //end add costSaring for fedNonFedBudget repport
        
         //start addition for participant cost change
        CoeusVector cvParticipantTuition = cvCostBeans.filter(equalsPartTuition);
        cost = new BigDecimal("0");
        costSharing = new BigDecimal("0");
        for (int i = 0; i< cvParticipantTuition.size();i++) {
            CostBean participantTuition = (CostBean) cvParticipantTuition.get(i);
            cost = cost.add(participantTuition.getCost());
            totalParticipantCost = totalParticipantCost.add(cost);
            totalParticipantCount = totalParticipantCount + participantTuition.getQuantity();
            
            costSharing = costSharing.add(participantTuition.getCostSharing());
            totalParticipantCostSharing = totalParticipantCostSharing.add(costSharing);
        }
         otherDirectCostBean.setPartTuition(cost);
         otherDirectCostBean.setPartTuitionCostSharing(costSharing);
         
         
        CoeusVector cvParticipantSubsistence = cvCostBeans.filter(equalsPartSubsistence);
        cost = new BigDecimal("0");
        costSharing = new BigDecimal("0");
        for (int i = 0; i< cvParticipantSubsistence.size();i++) {
            CostBean participantSubsistence = (CostBean) cvParticipantSubsistence.get(i);
            cost = cost.add(participantSubsistence.getCost());
            totalParticipantCost = totalParticipantCost.add(cost);
            totalParticipantCount = totalParticipantCount + participantSubsistence.getQuantity();
            
            costSharing = costSharing.add(participantSubsistence.getCostSharing());
            totalParticipantCostSharing = totalParticipantCostSharing.add(costSharing);
        }
         otherDirectCostBean.setPartSubsistence(cost);
         otherDirectCostBean.setPartSubsistenceCostSharing(costSharing);
         
         
         
         //end addition for participant cost change
         
        CoeusVector cvParticipantOther = cvCostBeans.filter(equalsPartOther);
        //cvCostBeans is a vector (of size 1) containing costBean for participant other
        cost = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i=0; i< cvParticipantOther.size() ;i++) {
            CostBean participantOther = (CostBean) cvParticipantOther.get(i);
            cost = cost.add(participantOther.getCost());
            totalParticipantCost = totalParticipantCost.add(cost);
            totalParticipantCount = totalParticipantCount + participantOther.getQuantity();
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(participantOther.getCostSharing());
            totalParticipantCostSharing = totalParticipantCostSharing.add(costSharing);
            //end add costSaring for fedNonFedBudget repport
        }
        otherDirectCostBean.setPartOther(cost);
       
        otherDirectCostBean.setParticipantTotal(totalParticipantCost);
        otherDirectCostBean.setParticipantTotalCount(totalParticipantCount);
        
        //start add costSaring for fedNonFedBudget repport
        otherDirectCostBean.setPartOtherCostSharing(costSharing);         
        otherDirectCostBean.setParticipantTotalCostSharing(totalParticipantCostSharing);
        //end add costSaring for fedNonFedBudget repport
        
        CoeusVector cvOtherCostDetails = new CoeusVector();
        HashMap hmOtherCostDetails = new HashMap();
        
        CoeusVector cvOthers = cvCostBeans.filter(equalsOther);
        cost = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        //cvOthers is vector of cost beans containing other direct costs
        for (int i = 0; i < cvOthers.size(); i++){
            CostBean otherDirect = (CostBean) cvOthers.get(i);
            cost = cost.add(otherDirect.getCost());
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(otherDirect.getCostSharing());
            //end add costSaring for fedNonFedBudget repport
        }
        totalOtherDirect = totalOtherDirect.add(cost);
        hmOtherCostDetails.put("Cost",cost.toString());
         hmOtherCostDetails.put("Description","Other Direct Costs");
         //start add costSaring for fedNonFedBudget repport
         totalOtherDirectCostSharing = totalOtherDirectCostSharing.add(costSharing);
         hmOtherCostDetails.put("CostSharing",costSharing.toString());
         //end add costSaring for fedNonFedBudget repport
         cvOtherCostDetails.add(hmOtherCostDetails);
         
        CoeusVector cvAllOthers = cvCostBeans.filter(other);
        //cvAllOthers is vector of cost beans containing all other direct costs
        cost = new BigDecimal("0");
        //start add costSaring for fedNonFedBudget repport
        costSharing = new BigDecimal("0");
        //end add costSaring for fedNonFedBudget repport
        for (int i = 0; i < cvAllOthers.size(); i++){
            CostBean allOthers = (CostBean) cvAllOthers.get(i);
            cost = cost.add(allOthers.getCost());
            //start add costSaring for fedNonFedBudget repport
            costSharing = costSharing.add(allOthers.getCostSharing());
            //end add costSaring for fedNonFedBudget repport
        }
        totalOtherDirect = totalOtherDirect.add(cost);
         hmOtherCostDetails = new HashMap();
        hmOtherCostDetails.put("Cost",cost.toString());
        hmOtherCostDetails.put("Description","All Other Costs");
        //start add costSaring for fedNonFedBudget repport
         totalOtherDirectCostSharing = totalOtherDirectCostSharing.add(costSharing);
         hmOtherCostDetails.put("CostSharing",costSharing.toString());
         //end add costSaring for fedNonFedBudget repport
        cvOtherCostDetails.add(hmOtherCostDetails);
        
        otherDirectCostBean.setOtherCosts(cvOtherCostDetails);
        
        otherDirectCostBean.settotalOtherDirect(totalOtherDirect);
        
        //start add costSaring for fedNonFedBudget repport
        otherDirectCostBean.setTotalOtherDirectCostSharing(totalOtherDirectCostSharing);
        //end add costSaring for fedNonFedBudget repport        
                 
        cvOtherDirectCostBean.add(otherDirectCostBean);
        return cvOtherDirectCostBean;
    }
       
     public HashMap getNarrativeInfo(String proposalNumber, int moduleNumber)
     throws CoeusException , DBException{
         
        int narrativeTypeCode=0;
	String moduleTitle="";    
        String description="";
        
         Vector param = new Vector();
         param.addElement( new Parameter("PROPOSAL_NUMBER",
               DBEngineConstants.TYPE_STRING, proposalNumber));
         param.addElement(new Parameter("MODULE_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(moduleNumber))));
         HashMap row = null;
         Vector result = new Vector();   
         
         if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  s2sPackage.getNarrativeType ( <<PROPOSAL_NUMBER>> , <<MODULE_NUMBER>> , "
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
                narrativeTypeCode = Integer.parseInt(row.get("NARRATIVE_TYPE_CODE").toString());  
                if (row.get("MODULE_TITLE") != null)
                  moduleTitle = row.get("MODULE_TITLE").toString();
                description = row.get("DESCRIPTION").toString();
            }
        }
        HashMap hmNarrative = new HashMap();
        hmNarrative.put("NARRATIVE_TYPE_CODE",Integer.toString(narrativeTypeCode));
        hmNarrative.put("MODULE_TITLE",moduleTitle);
        hmNarrative.put("DESCRIPTION",description);
        
        return hmNarrative;
     }
    
      public int getNarrativeType (String proposalNumber, int moduleNumber)
          throws CoeusException , DBException{
               Vector param = new Vector();
       int narrativeType = 0;
       
       param.addElement(new Parameter("NARRATIVE_TYPE_CODE", 
              DBEngineConstants.TYPE_INT, (Integer.toString(narrativeType)), DBEngineConstants.DIRECTION_OUT));
   
       param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
      
       param.addElement(new Parameter("MODULE_NUMBER",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(moduleNumber))));
      
    
        HashMap row = null;
        Vector result = new Vector();
                
       if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING NARRATIVE_TYPE>>=call s2sPackage.fn_get_Narrative_Type( " + 
              " <<PROPOSAL_NUMBER>> ,<<MODULE_NUMBER>> ) }",
                param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          //case 2284 start
//          narrativeType = Integer.parseInt(row.get("NARRATIVE_TYPE").toString());  
          narrativeType = Integer.parseInt(row.get("NARRATIVE_TYPE") == null? "0" :row.get("NARRATIVE_TYPE").toString());  
          //case 2284 end
        }                
        
         
     return narrativeType;  
    }       
   
     
     
     public String  fn_get_division (String department)
            throws CoeusException , DBException{
       Vector param = new Vector();
       String division = null;
       param.addElement(new Parameter("DIVISION", 
              DBEngineConstants.TYPE_STRING, division, DBEngineConstants.DIRECTION_OUT));
   
       param.addElement( new Parameter("DEPARTMENT",
          DBEngineConstants.TYPE_STRING, department));
      
       
    
        HashMap row = null;
        Vector result = new Vector();
                
       if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING DIVISION>>=call s2sPackage.fn_get_division( " + 
              " <<DEPARTMENT>>  ) }",
                param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          division = row.get("DIVISION").toString();  
        }                
        
         
       return division;
     }
      
     private String getAgency() throws CoeusException, DBException{
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
        private BigDecimal formatCost (double dblCost) throws CoeusException {
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         
         return bdCost; 
   }  
        
   public String getNSFChecklistAnswer(String propNumber, int questionId)
                                            throws DBException, CoeusException{
       Vector param = new Vector();
       String answer = "Yes";
       Vector result = new Vector();
       HashMap row = null;
       param.addElement(new Parameter("ANSWER", 
              DBEngineConstants.TYPE_STRING, answer, DBEngineConstants.DIRECTION_OUT));  
       param.addElement( new Parameter("AS_PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       param.addElement( new Parameter("AS_QUESTION_ID",
          DBEngineConstants.TYPE_INT, Integer.toString(questionId))); 
       if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING ANSWER>> = call fn_get_nsf_checklist_answer( " + 
              " <<AS_PROPOSAL_NUMBER>>, <<AS_QUESTION_ID>> ) }",
                param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        } 
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          answer = (String)row.get("ANSWER");  
        }                
         return answer;       
       
   }
   
   public HashMap  getEOStateReview(String propNumber) throws DBException, CoeusException{
       Vector param = new Vector();
       HashMap hmStateReview = new HashMap();
       String answer = "";
       Timestamp reviewDate = null;
       Vector result = new Vector();
       HashMap row = null;
  
       param.addElement( new Parameter("AS_PROPOSAL_NUMBER", 
                                DBEngineConstants.TYPE_STRING, propNumber));
         if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  s2sPackage.get_eo_state_review ( <<AS_PROPOSAL_NUMBER>>, "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        } 
       
         //should just be one row
        if (result.size() >0){
            hmStateReview = (HashMap)result.get(0);
        }
        return hmStateReview;
   }
   
   /** 
    *getIsNIHSponsor
    * returns 1 if sponsor or prime sponsor is NIH
    * else returns 0
    * added for case 2695
    */
   
   public int getIsNIHSponsor (String propNumber)
      throws DBException, CoeusException{
    int isNih = 0;
    
      Vector param = new Vector();
      Vector result = new Vector();
      HashMap row = null;
      
               
   
      param.addElement(new Parameter("ISNIH", 
          DBEngineConstants.TYPE_INT, Integer.toString(isNih), 
          DBEngineConstants.DIRECTION_OUT));
      param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
      param.addElement( new Parameter("SPONSOR_GROUP",
          DBEngineConstants.TYPE_STRING, "NIH"));
       
       if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER ISNIH>> = call FN_IN_SPONSOR_GROUP( " +
              " <<PROPOSAL_NUMBER>>, <<SPONSOR_GROUP>> ) }",
                param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        } 
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
      
          isNih  = Integer.parseInt(row.get("ISNIH").toString());  
        }                
         return isNih;       
       
   }
   
   /**
    * getFederalId
    *   function returns -1 if no federal id
    */
   public String getFederalId (String propNumber)
        throws DBException, CoeusException{
     
      Vector param = new Vector();
      String federalId = "";
      Vector result = new Vector();
      HashMap row = null;
      param.addElement(new Parameter("FEDERALID", 
            DBEngineConstants.TYPE_STRING, federalId, DBEngineConstants.DIRECTION_OUT));  
      param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, propNumber));
       



       if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING FEDERALID>> = call s2sPackage.GET_FEDERAL_ID( " + 
              " <<PROPOSAL_NUMBER>> ) }",
                param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        } 
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          federalId = (String)row.get("FEDERALID");  
        }                
         return federalId; 

     }   
            
   /** getNSFDegree - case 1721
    *  arguments:  degree_code
    *  returns:    NSF degree, which is a concatenation of degree_code and description
    *              from osp$degree_type table.
    */
     public String getNSFDegree(String as_degree_code)
                               throws DBException, CoeusException{
       Vector param = new Vector();
       String degree = "";
       Vector result = new Vector();
       HashMap row = null;
       param.addElement(new Parameter("DEGREE", 
              DBEngineConstants.TYPE_STRING, degree, DBEngineConstants.DIRECTION_OUT));  
       param.addElement( new Parameter("DEGREE_CODE",
          DBEngineConstants.TYPE_STRING, as_degree_code));
       
       if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING DEGREE>> = call FN_GET_NSF_DEGREE( " + 
              " <<DEGREE_CODE>> ) }",
                param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        } 
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          degree = (String)row.get("DEGREE");  
        }                
         return degree;       
       
   }
     
     /* start additions for getting modular budget amounts */
   
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
             "call s2sPackage.getModBudIDC(<<PROPOSAL_NUMBER>> , " 
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
             "call s2sPackage.getModBudTot(<<PROPOSAL_NUMBER>> , " 
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
             "call s2sPackage.getModBudCostShare(<<PROPOSAL_NUMBER>> , " 
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
    
   
     /* end additions for getting modular budget amounts */
     
   private String userId;
   public boolean deleteAutoGenNarrativeType(String propNumber,int[] narrTypes)
   throws CoeusException,DBException{
       int size = narrTypes.length;
       if(dbEngine!=null){
           boolean success = false;
           Vector procedures = new Vector(2);
           Vector pdfProcedures = new Vector(2);
           String proposalNumber = "";
           Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
           Connection conn = null;
           try{
               conn = dbEngine.beginTxn();
               for(int attIndex = 0;attIndex<size;attIndex++){
                   int narrTypeCode = narrTypes[attIndex];
                   ProposalNarrativeFormBean proposalNarrativeFormBean = new ProposalNarrativeFormBean();
                   proposalNarrativeFormBean.setProposalNumber(propNumber);
                   proposalNarrativeFormBean.setNarrativeTypeCode(narrTypeCode);
                   proposalNarrativeFormBean.setAcType("D");
                   //For deleting all attachments already inserted in last validation
                   procedures.add(getNarrativeProc(proposalNarrativeFormBean,dbTimestamp));
                   dbEngine.executeStoreProcs(procedures,conn);
               }
           }catch (Exception dbEx){
               UtilFactory.log(dbEx.getMessage(),dbEx,"S2STxnBean", "insertAutoGenNarrativeDetails");
               try{
                   if(conn!=null){ 
                       conn.rollback();
                   }
               }catch(SQLException sqlEx){
                   //do nothing
                   UtilFactory.log("Warning, not an execution stopper... Could ignore it",
                   sqlEx,"BudgetYearDataTypeStream", "insertAutoGenNarrativeDetails");
               }
               throw new CoeusException(dbEx.getMessage());
           }finally{
               dbEngine.endTxn(conn);
           }
       }else{
           throw new CoeusException("db_exceptionCode.1000");
       }
       
       return true;
   }
   
   /* start additions for box 5 on rrsf424 - prop contact person */
     public HashMap getContactType (String propNumber)
      throws CoeusException, DBException {
     
         
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2spackage.getContactType( <<PROPOSAL_NUMBER>> , "
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
               "call s2spackage.getContactPerson( <<PROPOSAL_NUMBER>> , <<CONTACT_TYPE>> , "
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
    
     /* end additions for box 5 */
     
     /*start addition for box 15 */
       public HashMap getLeadUnit (String propNumber)
         throws CoeusException, DBException {
             
           
        Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
       
        if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPackage.getLeadUnit( <<PROPOSAL_NUMBER>>  , "
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
      
     
     /*end addition for box 15 */
       
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
           
       
    /* get_specRev_app_status
     * added for case 3110 - 
     */
    public HashMap get_specRev_app_code(String propNumber, int spRevType, int approvalCode)
         throws CoeusException, DBException {
        
         Vector result = null;
        Vector param= new Vector();

        HashMap row = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
        param.addElement(new Parameter("SP_REV_TYPE",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(spRevType))));
        param.addElement(new Parameter("APPROVAL_CODE",
                     DBEngineConstants.TYPE_INT, ( Integer.toString(approvalCode))));
        
    if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPackage.getSpRevApprovCode( <<PROPOSAL_NUMBER>>  , <<SP_REV_TYPE>> , <<APPROVAL_CODE>> , "
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
    
     /* getExemptionNumbers
     * added for case 3110 - 
     */
    public Vector getExemptionNumbers(String protocolNumber)
         throws CoeusException, DBException {
        
        String exemptionNumbers = null;
        Vector result = null;
        Vector param= new Vector();
     
        HashMap row = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                       DBEngineConstants.TYPE_STRING,protocolNumber));
        
    if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sPackage.getExemptionNumbers( <<PROTOCOL_NUMBER>>  , "
                                + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
       
            

        return result;
             
      }
    
     
    public int insertAutoGenNarrativeDetails(ProposalNarrativeFormBean proposalNarrativeFormBean,
                    ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean,
                    boolean deleteFlag)
            throws CoeusException,DBException{
        int moduleNumber = 0;
        if(dbEngine!=null){
            boolean success = false;
            Vector procedures = new Vector(2);
            Vector pdfProcedures = new Vector(2);
            String proposalNumber = "";
            Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
            int narrTypeCode = -1;
            Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                proposalNarrativeFormBean.setAcType("I");
                procedures.add(getNarrativeProc(proposalNarrativeFormBean,dbTimestamp));
                ProposalNarrativeTxnBean propNarrTxnBean = new ProposalNarrativeTxnBean("Coeus");
                Vector result = dbEngine.executeStoreProcs(procedures,conn);
                if(!result.isEmpty()){
                    /*
                     *index=1, if there are two procs.
                     */
                    
                    HashMap moduleRow = (HashMap)(result.get(0));
                        //                    Vector moduleRes = (Vector)result.get(resultIndex);
                        //                    HashMap moduleRow = (HashMap)moduleRes.get(0);
                        moduleNumber = Integer.parseInt(
                        moduleRow.get("MODULE_NUMBER").toString());
                        int moduleSeqNumber = Integer.parseInt(
                        moduleRow.get("MODULE_SEQUENCE_NUMBER").toString());
                        proposalNarrativePDFSourceBean.setModuleNumber(moduleNumber);
                        pdfProcedures.add(propNarrTxnBean.updateProposalNarrativePDF(
                        proposalNarrativePDFSourceBean,dbTimestamp));
                }
                if(!pdfProcedures.isEmpty()){
                    dbEngine.batchSQLUpdate(pdfProcedures,conn);
                }
            }catch (Exception dbEx){
                UtilFactory.log(dbEx.getMessage(),dbEx,"S2STxnBean", "insertAutoGenNarrativeDetails");
                try{
                    if(conn!=null){
                        conn.rollback();
                    }
                }catch(SQLException sqlEx){
                    //do nothing
                    UtilFactory.log("Warning, not an execution stopper... Could ignore it",
                    sqlEx,"BudgetYearDataTypeStream", "insertAutoGenNarrativeDetails");
                }
                throw new CoeusException(dbEx.getMessage());
            }finally{
                dbEngine.endTxn(conn);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
                
        return moduleNumber;
    }
    public boolean deleteAutoGenNarratives(String proposalNumber)
            throws CoeusException,DBException{
        int success = 0;
        if(dbEngine!=null){
             Vector param = new Vector();
             param.addElement( new Parameter("PROPOSAL_NUMBER",
                  DBEngineConstants.TYPE_STRING, proposalNumber));
            
            Vector result = dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER SUCCESS>> = call s2sPackage.FN_DELETE_NARR_AUTO_GEN_ATTS( <<PROPOSAL_NUMBER>> ) }", param);  
            if(!result.isEmpty()){
                HashMap row = (HashMap)result.elementAt(0);
                success = Integer.parseInt(row.get("SUCCESS").toString());  
            }                
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return success==1;
    }

    /**
     *  This Method is used to Insert/Delete auto generated Proposal Narrative details data.
     *
     *  @param proposalNarratives contains vector of Proposal Narrative details.
     *  @return boolean this holds true for successfull insert/delete or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
   public boolean insertAutoGenNarrativeDetails(Vector extraAttachments)
   throws CoeusException,DBException{
       //    public boolean insertAutoGenNarrativeDetails(ProposalNarrativeFormBean proposalNarrativeFormBean,
       //                    ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean,
       //                    boolean deleteFlag)
       //            throws CoeusException,DBException{
       int size = extraAttachments.size();
       if(dbEngine!=null){
           boolean success = false;
           Vector procedures = new Vector(2);
           Vector pdfProcedures = new Vector(2);
           String proposalNumber = "";
           Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
           int narrTypeCode = -1;
           Connection conn = null;
           try{
               conn = dbEngine.beginTxn();
               for(int attIndex = 0;attIndex<size;attIndex++){
                   ExAttQueryParams attParam = (ExAttQueryParams)extraAttachments.get(attIndex);
                   ProposalNarrativeFormBean proposalNarrativeFormBean = attParam.getPropNarrBean();
                   ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = attParam.getPropNarrPdfBean();
                   boolean deleteFlag = false;
                   
//                   if(narrTypeCode != proposalNarrativeFormBean.getNarrativeTypeCode()){
//                       deleteFlag = true;
//                       narrTypeCode = proposalNarrativeFormBean.getNarrativeTypeCode();
//                       proposalNarrativeFormBean.setAcType("D");
//                       //For deleting all attachments already inserted in last validation
//                       procedures.add(getNarrativeProc(proposalNarrativeFormBean,dbTimestamp));
//                   }
                   proposalNarrativeFormBean.setAcType("I");
                   procedures.add(getNarrativeProc(proposalNarrativeFormBean,dbTimestamp));
                   ProposalNarrativeTxnBean propNarrTxnBean = new ProposalNarrativeTxnBean("Coeus");
                   Vector result = dbEngine.executeStoreProcs(procedures,conn);
                   procedures.removeAllElements();
                   if(!result.isEmpty()){
                    /*
                     *index=1, if there are two procs.
                     */
                       HashMap moduleRow = deleteFlag?(HashMap)((Vector)result.get(1)).get(0):
                           (HashMap)result.get(0);
                           //                    Vector moduleRes = (Vector)result.get(resultIndex);
                           //                    HashMap moduleRow = (HashMap)moduleRes.get(0);
                       int moduleNumber = Integer.parseInt(
                       moduleRow.get("MODULE_NUMBER").toString());
                       int moduleSeqNumber = Integer.parseInt(
                       moduleRow.get("MODULE_SEQUENCE_NUMBER").toString());
                       proposalNarrativePDFSourceBean.setModuleNumber(moduleNumber);
                       pdfProcedures.add(propNarrTxnBean.updateProposalNarrativePDF(
                            proposalNarrativePDFSourceBean,dbTimestamp));
                   }
               }
               if(!pdfProcedures.isEmpty()){
                    dbEngine.batchSQLUpdate(pdfProcedures,conn);
               }
           }catch (Exception dbEx){
               UtilFactory.log(dbEx.getMessage(),dbEx,"S2STxnBean", "insertAutoGenNarrativeDetails");
               try{
                   if(conn!=null){ 
                       conn.rollback();
                   }
               }catch(SQLException sqlEx){
                   //do nothing
                   UtilFactory.log("Warning, not an execution stopper... Could ignore it",
                   sqlEx,"BudgetYearDataTypeStream", "insertAutoGenNarrativeDetails");
               }
               throw new CoeusException(dbEx.getMessage());
           }finally{
               dbEngine.endTxn(conn);
           }
       }else{
           throw new CoeusException("db_exceptionCode.1000");
       }
       return true;
    }    
    private ProcReqParameter getNarrativeProc(ProposalNarrativeFormBean proposalNarrativeFormBean,
                                                Timestamp dbTimestamp) throws DBException{
        String proposalNumber = proposalNarrativeFormBean.getProposalNumber();
        Vector paramProposalOther= new Vector();
        paramProposalOther.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeFormBean.getProposalNumber()));
        paramProposalOther.addElement(new Parameter("MODULE_TITLE",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeFormBean.getModuleTitle()));
        paramProposalOther.addElement(new Parameter("MODULE_STATUS_CODE",
        DBEngineConstants.TYPE_STRING,
        new Character(proposalNarrativeFormBean.getModuleStatusCode()).toString()));
        paramProposalOther.addElement(new Parameter("CONTACT_NAME",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeFormBean.getContactName()));
        paramProposalOther.addElement(new Parameter("PHONE_NUMBER",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeFormBean.getPhoneNumber()));
        paramProposalOther.addElement(new Parameter("EMAIL_ADDRESS",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeFormBean.getEmailAddress()));
        paramProposalOther.addElement(new Parameter("COMMENTS",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeFormBean.getComments()));
        //added for Coeus enhancements case:1624  start 1
        paramProposalOther.addElement(new Parameter("NARRATIVE_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+proposalNarrativeFormBean.getNarrativeTypeCode()));
        //added for Coeus enhancements case:1624  end 1
        paramProposalOther.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,"Coeus"));
        paramProposalOther.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramProposalOther.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        proposalNarrativeFormBean.getAcType()));
        paramProposalOther.addElement(new Parameter("MODULE_NUMBER",
        DBEngineConstants.TYPE_INTEGER,
        ""+proposalNarrativeFormBean.getModuleNumber(),DBEngineConstants.DIRECTION_OUT));
        paramProposalOther.addElement(new Parameter("MODULE_SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INTEGER,
        ""+proposalNarrativeFormBean.getModuleSequenceNumber(),DBEngineConstants.DIRECTION_OUT));
        
        StringBuffer sqlProposalOther = new StringBuffer(
        "call S2SPACKAGE.UPD_PROP_NARR_AUTO_GEN_PDF(");
        sqlProposalOther.append(" <<PROPOSAL_NUMBER>> , ");
        sqlProposalOther.append(" <<MODULE_TITLE>> , ");
        sqlProposalOther.append(" <<MODULE_STATUS_CODE>> , ");
        sqlProposalOther.append(" <<CONTACT_NAME>> , ");
        sqlProposalOther.append(" <<PHONE_NUMBER>> , ");
        sqlProposalOther.append(" <<EMAIL_ADDRESS>> , ");
        sqlProposalOther.append(" <<COMMENTS>> , ");
        sqlProposalOther.append(" <<NARRATIVE_TYPE_CODE>> , ");
        sqlProposalOther.append(" <<UPDATE_USER>> , ");
        sqlProposalOther.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProposalOther.append(" <<AC_TYPE>> , ");
        sqlProposalOther.append(" <<MODULE_NUMBER>> , ");
        sqlProposalOther.append(" <<MODULE_SEQUENCE_NUMBER>> ) ");
        
        ProcReqParameter procProposalOther  = new ProcReqParameter();
        procProposalOther.setDSN("Coeus");
        procProposalOther.setParameterInfo(paramProposalOther);
        procProposalOther.setSqlCommand(sqlProposalOther.toString());
        return procProposalOther;
    }
    
    /**
     * Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }
    
    /**
     * Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    
    public boolean checkRightToPrint(String userId,String proposalNumber, String formName) throws DBException,CoeusException{
     
     int hasRight = 0;
       
     Vector param = new Vector();
       
     param.addElement(new Parameter("HAS_RIGHT", 
          DBEngineConstants.TYPE_INT, Integer.toString(hasRight), 
          DBEngineConstants.DIRECTION_OUT));
     param.addElement( new Parameter("USER_ID",
          DBEngineConstants.TYPE_STRING, userId));
     param.addElement( new Parameter("PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
     param.addElement( new Parameter("FORM_NAME",
          DBEngineConstants.TYPE_STRING, formName));
        
     HashMap row = null;
     Vector result = new Vector();
                
     if(dbEngine !=null){
        result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER HAS_RIGHT>> = call fn_check_right_to_print_s2s( <<USER_ID>>,<<PROPOSAL_NUMBER>>,<<FORM_NAME>> ) }", param);  
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
         hasRight = Integer.parseInt(row.get("HAS_RIGHT").toString());   
        }                
       return hasRight==1;

    }
    
    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
    /**
     *  This Method is used to fetch the division value for the person.
     *
     *  @param proposalNumber contains proposal number.
     *  @param personId.
     *  @return division.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String  getPropPersonDivision (String proposalNumber, String personId)
            throws CoeusException , DBException{
       Vector param = new Vector();
       String division = null;       
       param.addElement( new Parameter("AV_PROPOSAL_NUMBER",
          DBEngineConstants.TYPE_STRING, proposalNumber));
       param.addElement( new Parameter("AV_PERSON_ID",
          DBEngineConstants.TYPE_STRING, personId));
    
        HashMap row = null;
        Vector result = new Vector();
                
       if(dbEngine !=null){
           result = dbEngine.executeRequest("Coeus",
            "call  GET_EPS_PROP_PERSON_DIVISION ( <<AV_PROPOSAL_NUMBER>> , <<AV_PERSON_ID>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param); 
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          if(row.get("DIVISION")!=null){
            division = row.get("DIVISION").toString();  
          }
        }                
       return division;
     }
    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
    
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
    /**
     *  This Method is used to fetch the division value for the person.
     *
     *  @param String contains proposal number.
     *  @param int contains the version number
     *  @param int contains the budget period
     *  @param String contains the person id
     *  @return String contains the base salary of the person
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getBaseSalaryForPeriod(String proposalNumber, int version, int period, String personId)
    throws CoeusException , DBException{
        Vector param = new Vector();
        String baseSalary = null;
        param.addElement( new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement( new Parameter("AV_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT, version));
        param.addElement( new Parameter("AV_BUDGET_PERIOD",
                DBEngineConstants.TYPE_INT, period));
        param.addElement( new Parameter("AV_PERSON_ID",
                DBEngineConstants.TYPE_STRING, personId));
        
        HashMap row = null;
        Vector result = new Vector();
        
        if(dbEngine !=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING AMOUNT>>=call FN_GET_BASE_SALARY_FOR_PERIOD( " +
                    " <<AV_PROPOSAL_NUMBER>> , <<AV_VERSION_NUMBER>> , <<AV_BUDGET_PERIOD>> , <<AV_PERSON_ID>> ) }",
                    param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            if(row.get("AMOUNT") != null){
                baseSalary = (String)row.get("AMOUNT");
            }
        }
        return baseSalary;
    }
    //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
}
  

