package edu.mit.coeus.xml.generator.bean;
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
  
public class SubReportsTxnBean
{
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
       
    private String reportNumber;
    private String reportType;
    private String startDate;
    private String endDate;
    private String awardNumber;
    private BigDecimal hundred = new BigDecimal("100");
    private BigDecimal zero = new BigDecimal("0");
    
     public static final String ARMY = "ARMY";
     public static final String NAVY = "NAVY";
     public static final String AIRFORCE = "AIR FORCE";
     public static final String DLA = "DEFENSE LOGISTICS AGENCY";
     public static final String NASA = "NASA";
     public static final String GSA = "GSA";
     public static final String DOE = "DOE";
     
     public static final String DOD = "DOD";
     public static final String NIH = "NIH";
     public static final String NSF = "NSF";
     public static final String EPA = "EPA";
     public static final String JET = "JET PROPULSION LAB";
     
     public static final String OTHER = "OTHER";
     
   
    

     
    SubcontractingReportsInfoBean subcontractingReportsInfoBean;
   
    
    /** Creates a new instance of SubReportsTxnBean */
    public SubReportsTxnBean()
    {
        
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
       
        subcontractingReportsInfoBean = new SubcontractingReportsInfoBean();
    }
    
  public SubcontractingReportsInfoBean getSubReportsInfo(String reportNumber,String reportType,
                                                           String awardNumber,String startDate, String endDate)
    throws CoeusException, DBException
  {
    subcontractingReportsInfoBean =  new SubcontractingReportsInfoBean();
    this.reportNumber = reportNumber;
    this.reportType = reportType;
    this.awardNumber = awardNumber;
    this.startDate = startDate;
    this.endDate = endDate;
     
    if(reportNumber == "294"){
        subcontractingReportsInfoBean = get294Info();
       
    }else {
        load295Table();
        subcontractingReportsInfoBean = get295Info();
    }
    
     
    return subcontractingReportsInfoBean;
      
  }
  
   private SubcontractingReportsInfoBean get294_295Info() 
    throws CoeusException, DBException {
        //this gets data used by both 294 and 295
    
        SubcontractingReportsInfoBean subInfoBean = new SubcontractingReportsInfoBean();
        
        subInfoBean.setDateSubmitted(getTodayDate());
        
        Vector param = new Vector();
        param.addElement(new Parameter("DUMMY",
            DBEngineConstants.TYPE_STRING, " "));
        HashMap row = null;
        Vector result = new Vector();
          
        if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  subcontracting_rpts_pkg.get_company_info ( <<DUMMY>> ,  "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
       
        if (vecSize >0){
                       
           //vector should just be one row
             row = (HashMap)result.elementAt(0);
             subInfoBean.setCompanyName(row.get("ORGANIZATION_NAME") == null ? " " :
                                row.get("ORGANIZATION_NAME").toString());
             subInfoBean.setCompanyStreetAddress(row.get("ADDRESS") == null ? " " :
                                row.get("ADDRESS").toString());
                                subInfoBean.setCompanyCity("Cambridge");
             subInfoBean.setCompanyCity(row.get("CITY") == null ? " " :
                                row.get("CITY").toString());
             subInfoBean.setCompanyState(row.get("STATE") == null ? " " :
                                row.get("STATE").toString());
             subInfoBean.setCompanyZip(row.get("POSTAL_CODE") == null ? " " :
                                row.get("POSTAL_CODE").toString());
             subInfoBean.setContractorID(row.get("DUNS_NUMBER") == null ? " " :
                                row.get("DUNS_NUMBER").toString());
        }
            
        
       
        
        String fy=" ";     
        param = new Vector();
       
        param.addElement(new Parameter("FISCAL_YEAR", 
              DBEngineConstants.TYPE_STRING, fy, DBEngineConstants.DIRECTION_OUT));
        param.addElement( new Parameter("DUMMY",
              DBEngineConstants.TYPE_STRING, " "));
        row = null;
        result = new Vector();
           
      
        if(dbEngine !=null){
           result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING FISCAL_YEAR>>=call subcontracting_rpts_pkg.fn_get_report_year( " + 
              " <<DUMMY>> ) }",
                param);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          fy = row.get("FISCAL_YEAR").toString();  
        }                
         
        subInfoBean.setReportFY(fy);
        subInfoBean.setReportNumber(reportNumber);
        
        //GET REPORT PERIOD
        String isMarchReport=" ";     
        String dummy = "dummy";
        Vector param2 = new Vector();
       
        param2.addElement(new Parameter("IS_MARCH", 
              DBEngineConstants.TYPE_STRING, isMarchReport, DBEngineConstants.DIRECTION_OUT));
        param2.addElement( new Parameter("AS_ARGUMENT",
              DBEngineConstants.TYPE_STRING, dummy));
        HashMap row2 = null;
        Vector result2 = new Vector();
           
        if(dbEngine !=null){
           result2 = dbEngine.executeFunctions("Coeus",
             "{ <<OUT STRING IS_MARCH>>=call subcontracting_rpts_pkg.fn_get_is_march_rpt( " + 
              " <<AS_ARGUMENT>> ) }",
                param2);  
          
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        
        if(!result2.isEmpty()){
          row2 = (HashMap)result2.elementAt(0);
          isMarchReport = row2.get("IS_MARCH").toString();  
        }                
        boolean isMarch, isSept;
        if (isMarchReport.equals("TRUE")){
            isMarch = true;
            isSept = false;
        } else {
            isMarch = false;
            isSept = true;
        }
        subInfoBean.setIsMarchReport(isMarch);
        subInfoBean.setIsSeptReport(isSept);
        
     
        subInfoBean.setReportType(reportType);
        subInfoBean.setIsPrime(true);
        subInfoBean.setIsSub(false);
        
        //change to remove hard code - case 2902
        Vector param3 = new Vector();
        param3.addElement( new Parameter("DUMMY",
                           DBEngineConstants.TYPE_STRING, dummy));
        HashMap row3 = null;
        Vector result3 = new Vector();
        
         if(dbEngine !=null){   
            result3 = dbEngine.executeRequest("Coeus",
            "call  subcontracting_rpts_pkg.get_official ( <<DUMMY>> ,  "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param3);
     }else{
            throw new CoeusException("db_exceptionCode.1000");
     }
     vecSize = result3.size();
       
     if (vecSize >0){
                       
           //vector should just be one row
             row3 = (HashMap)result3.elementAt(0);
             subInfoBean.setOfficialName(row3.get("NAME") == null ? " " :
                                row3.get("NAME").toString());
  //           subcontractingReportsInfoBean.setOfficialName(row3.get("NAME") == null ? " " :
  //                              row3.get("NAME").toString());
             subInfoBean.setOfficialTitle(row3.get("TITLE") == null ? " " :
                                row3.get("TITLE").toString());
             subInfoBean.setOfficialAreaCode(row3.get("AREA_CODE") == null ? " " :
                                row3.get("AREA_CODE").toString());
             subInfoBean.setOfficialPhone(row3.get("PHONE") == null ? " " :
                                row3.get("PHONE").toString());
      }
     
     /*
        subInfoBean.setOfficialName("Thomas W. Egan");
        subInfoBean.setOfficialTitle("Assistant Director, Office of Sponsored Programs");
        subInfoBean.setOfficialAreaCode("617");
        subInfoBean.setOfficialPhone("253-5460");
        */
        return subInfoBean;
   }
   

  private SubcontractingReportsInfoBean get294Info() 
    throws CoeusException, DBException {
          
        SubcontractingReportsInfoBean subcontractingReportsInfoBean = get294_295Info();
        subcontractingReportsInfoBean.setAwardNumber(awardNumber);
       
        //call stored proc to get prime contract number 
        String primeContractNum = null;
        
        Vector result = new Vector(3,2);
        HashMap row = null;
        Vector param = new Vector();
       
        param.addElement(new Parameter("AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING,awardNumber));
      
        if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  subcontracting_rpts_pkg.get_294_prime_contract_num ( <<AWARD_NUMBER>> ,  "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
       
        if (vecSize >0){
                       
           //vector should just be one row
             row = (HashMap)result.elementAt(0);
             primeContractNum = row.get("PRIME_CONTRACT_NUMBER") == null ? "Unknown" :
                                row.get("PRIME_CONTRACT_NUMBER").toString();
           
             subcontractingReportsInfoBean.setPrimeContractNumber(primeContractNum);
        }
         
        // now get detail info. this will be in a vector (of size 1) containing 
        // a SubcontractingReportsDetailBean
        
        CoeusVector cvDetail = new CoeusVector();
        cvDetail = get294Details(awardNumber);
     
   
        SubcontractingReportsDetailBean subReportDetailBean = new SubcontractingReportsDetailBean();
        subReportDetailBean = (SubcontractingReportsDetailBean)cvDetail.get(0);
      
        //get rest of detail bean  
        String adminActivity = getAdminActivity(awardNumber);    
        subReportDetailBean.setAdministeringActivity(adminActivity);
        if (adminActivity.equals(OTHER)) {
            String sponsor = get294Sponsor(awardNumber);
            subReportDetailBean.setSponsor(sponsor);
        }
      
       //subReportDetailBean.setRemarks( 
        
        CoeusVector cvDetailBeans = new CoeusVector();
        cvDetailBeans.add(subReportDetailBean);
        
        
        subcontractingReportsInfoBean.setDetailInfo(cvDetailBeans);
          
        return subcontractingReportsInfoBean;
  }
        
  ///////////////////////////
  // get294sponsor
  // for 'other' sponsor group
  ///////////////////////////
  private String get294Sponsor(String awardNumber) 
    throws CoeusException, DBException { 
  
     
        String sponsor = " ";
        Vector param = new Vector();
       
        param.addElement(new Parameter("SPONSOR", 
          DBEngineConstants.TYPE_STRING, sponsor, 
          DBEngineConstants.DIRECTION_OUT));
        param.addElement( new Parameter("AWARD_NUMBER",
          DBEngineConstants.TYPE_STRING, awardNumber));
        
        HashMap row = null;
        Vector result = new Vector();
        
       if(dbEngine !=null){
         result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING SPONSOR>>= call subcontracting_rpts_pkg.fn_get_294_sponsor ( <<AWARD_NUMBER>> ) }", param);  
         
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          sponsor = row.get("SPONSOR").toString();   
        }                
        
        return sponsor;
  }
       
  //////////////////////////////
  // load295Table
  ///////////////////////////////
    private void load295Table() throws CoeusException, DBException {
      String retCode = "0";
        Vector param = new Vector();
       
        param.addElement(new Parameter("RET_CODE", 
          DBEngineConstants.TYPE_STRING, retCode, 
          DBEngineConstants.DIRECTION_OUT));
        param.addElement( new Parameter("START_DATE",
          DBEngineConstants.TYPE_STRING, startDate));
        param.addElement( new Parameter("END_DATE",
          DBEngineConstants.TYPE_STRING, endDate));
           
        HashMap row = null;
        Vector result = new Vector();
     
       if(dbEngine !=null){
         result = dbEngine.executeFunctions("Coeus",
         "{ <<OUT STRING RET_CODE>>=call subcontracting_rpts_pkg.fn_pop_sub_exp_cat_by_fy(<<START_DATE>>,<<END_DATE>> ) }", param);  
         
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
          retCode = row.get("RET_CODE").toString();   
        }                
        
       
      return;
  }
    
   ////////////////////////////////////////
  // get295Info
  /////////////////////////////////////////
   private SubcontractingReportsInfoBean get295Info() 
    throws CoeusException, DBException {
        
   SubcontractingReportsInfoBean subcontractingReportsInfoBean = new SubcontractingReportsInfoBean();
   subcontractingReportsInfoBean = get294_295Info();
   
   /*  official and ceo - remove hard codes for case 2902
    */
   
    Vector param = new Vector();
    param.addElement(new Parameter("DUMMY",
            DBEngineConstants.TYPE_STRING, " "));
    HashMap row = null;
    Vector result = new Vector();
          
    if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  subcontracting_rpts_pkg.get_ceo ( <<DUMMY>> ,  "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
     }else{
            throw new CoeusException("db_exceptionCode.1000");
     }
     int vecSize = result.size();
       
     if (vecSize >0){
                       
           //vector should just be one row
             row = (HashMap)result.elementAt(0);
             subcontractingReportsInfoBean.setCeoName(row.get("NAME") == null ? " " :
                                row.get("NAME").toString());
              subcontractingReportsInfoBean.setCeoTitle(row.get("TITLE") == null ? " " :
                                row.get("TITLE").toString());
      }
     
      
//      subcontractingReportsInfoBean.setOfficialTitle('cc');
     
     
            
   
   // now get detail info. this will be in a vector  containing  SubcontractingReportsDetailBeans
    SubcontractingReportsDetailBean detailBean;
    CoeusVector cvDetailBeans = new CoeusVector();
    cvDetailBeans = get295Details();
    if (cvDetailBeans.size() > 0){  
           subcontractingReportsInfoBean.setDetailInfo(cvDetailBeans);
     }
       
   return subcontractingReportsInfoBean;
   }
      
   //////////////////////////////////////
   // get295Details
   //  returns vector of SubcontractingReportsDetailBeans
   // there will be one detail bean for each sponsor group (administering activity)
   ///////////////////////////////////////
   private CoeusVector get295Details() throws CoeusException, DBException {
       CoeusVector cvDetailBeans = new CoeusVector();
       String sponsorGroup = null;
       String sponsorType = "Required";
     
        ///////////////////////////////////
        // call stored proc to get amounts for this sponsor group
        ////////////////////////////////
       SubcontractingReportsDetailBean subDetailBean = new SubcontractingReportsDetailBean();
       sponsorGroup = ARMY;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup,sponsorType);
       subDetailBean.setAdministeringActivity(sponsorGroup);
       //put DetailBean into vector
       cvDetailBeans.add(subDetailBean);
       
       sponsorGroup = NAVY;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
         subDetailBean.setAdministeringActivity(sponsorGroup);
       //put DetailBean into vector
       cvDetailBeans.add(subDetailBean);
       
       sponsorGroup = AIRFORCE;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
         subDetailBean.setAdministeringActivity(sponsorGroup);
       //put DetailBean into vector
       cvDetailBeans.add(subDetailBean);
       
       sponsorGroup = DLA;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
       subDetailBean.setAdministeringActivity(sponsorGroup);
       //put DetailBean into vector
       cvDetailBeans.add(subDetailBean);
       
       sponsorGroup = NASA;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
         subDetailBean.setAdministeringActivity(sponsorGroup);
       //put DetailBean into vector
       cvDetailBeans.add(subDetailBean);
       
       sponsorGroup = GSA;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
         subDetailBean.setAdministeringActivity(sponsorGroup);
       //put DetailBean into vector
       cvDetailBeans.add(subDetailBean);
       
       sponsorGroup = DOE;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
         subDetailBean.setAdministeringActivity(sponsorGroup);
       //put DetailBean into vector
       cvDetailBeans.add(subDetailBean);
       
       //"other" sponsors
       sponsorType = "Other";
       sponsorGroup = DOD;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
       if (subDetailBean != null) {
            subDetailBean.setAdministeringActivity(OTHER);
            subDetailBean.setSponsor(sponsorGroup);
            //put DetailBean into vector
            cvDetailBeans.add(subDetailBean);
       }
       
       sponsorGroup = NIH;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
       if (subDetailBean != null) {
            subDetailBean.setAdministeringActivity(OTHER);
            subDetailBean.setSponsor(sponsorGroup);
            //put DetailBean into vector
            cvDetailBeans.add(subDetailBean);
       }
    
       sponsorGroup = NSF;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
       if (subDetailBean != null) {
            subDetailBean.setAdministeringActivity(OTHER);
            subDetailBean.setSponsor(sponsorGroup);
            //put DetailBean into vector
            cvDetailBeans.add(subDetailBean);
       }
   
       sponsorGroup = EPA;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
       if (subDetailBean != null) {
            subDetailBean.setAdministeringActivity(OTHER);
            subDetailBean.setSponsor(sponsorGroup);
            //put DetailBean into vector
            cvDetailBeans.add(subDetailBean);
       }
       
       sponsorGroup = JET;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
       if (subDetailBean != null) {
            subDetailBean.setAdministeringActivity(OTHER);
            subDetailBean.setSponsor(sponsorGroup);
            //put DetailBean into vector
            cvDetailBeans.add(subDetailBean);  
       }
     
     
       sponsorGroup = OTHER;
       subDetailBean = get295AmountsForSponsorGroup( sponsorGroup, sponsorType);
       if (subDetailBean != null) {
            subDetailBean.setAdministeringActivity(OTHER);
     
            //put DetailBean into vector
            cvDetailBeans.add(subDetailBean);
       }
       
       return cvDetailBeans;
   }
   
   /////////////////////////////////////
   // get295AmountsForSponsorGroup
   // returns SubcontractingReportsDetailBean for SponsorGroup
   ///////////////////////////////////
   private SubcontractingReportsDetailBean get295AmountsForSponsorGroup(String sponsorGroup,
                                                             String sponsorType) 
        throws CoeusException,    DBException {
       
      
       
       CoeusVector cvAmounts = new CoeusVector();
      
       Vector result = new Vector(3,2);
       HashMap row = null;
       Vector param = new Vector();
       
       param.addElement(new Parameter("SPONSOR_GROUP",
            DBEngineConstants.TYPE_STRING,sponsorGroup));
      
       if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  subcontracting_rpts_pkg.get_295_amts ( <<SPONSOR_GROUP>> ,  "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
       
        if (vecSize >0){
           cvAmounts = new CoeusVector();
           
           SubReportsAmountsBean subReportsAmountsBean= new SubReportsAmountsBean();
    
           //vector should just be one row
           for(int i=0;i<vecSize;i++){
             row = (HashMap)result.elementAt(0);
             BigDecimal amt = zero;
             BigDecimal goalAmt = zero;
             BigDecimal totAmt = new BigDecimal(row.get("TOT_AMT").toString());
             
             if (sponsorType.equals("Other") ) {
                if (totAmt.equals(zero) ) {
                  SubcontractingReportsDetailBean emptyBean = new SubcontractingReportsDetailBean();
                  emptyBean = null;
                  return emptyBean;
                }
             }
                        
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("LARGE BUSINESS");
             amt = new BigDecimal(row.get("LARGE_BUS_AMT") == null ? "0" : 
                                  row.get("LARGE_BUS_AMT").toString());
             subReportsAmountsBean.setAmount( amt);
             BigDecimal largeBusPct = getPct(amt,totAmt);
             subReportsAmountsBean.setPercent(largeBusPct);
             cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("SMALL BUSINESS");
             amt = new BigDecimal(row.get("SMALL_BUS_AMT") == null ? "0" :
                                    row.get("SMALL_BUS_AMT").toString());
             subReportsAmountsBean.setAmount(amt);
             if (amt.equals(zero)) {
                 subReportsAmountsBean.setPercent(zero);
             }else {
                subReportsAmountsBean.setPercent(hundred.subtract(largeBusPct));
                cvAmounts.add(subReportsAmountsBean);
             }
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("WOMAN OWNED");
             amt = new BigDecimal(row.get("WOMAN_OWNED_AMT") == null ? "0":
                                row.get("WOMAN_OWNED_AMT").toString());
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setPercent(getPct(amt,totAmt));
              cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("DISADVANTAGED BUSINESS");
             amt = new BigDecimal(row.get("DISADVANTAGED_BUS_AMT")==null ? "0":
                                    row.get("DISADVANTAGED_BUS_AMT").toString());
             subReportsAmountsBean.setAmount(amt);
              subReportsAmountsBean.setPercent(getPct(amt,totAmt));
             cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("HUB");
             amt = new BigDecimal(row.get("HUB_AMT")==null ? "0" :
                                row.get("HUB_AMT").toString());
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setPercent(getPct(amt,totAmt));
             cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("VET");
             amt= new BigDecimal(row.get("VET_AMT")==null ? "0" :
                                row.get("VET_AMT").toString());
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setPercent(getPct(amt,totAmt));
              cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("SDVO");
             amt= new BigDecimal(row.get("SDVO_AMT")==null ? "0" :
                                    row.get("SDVO_AMT").toString());
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setPercent(getPct(amt,totAmt));
             cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("HBCU");
             amt= new BigDecimal(row.get("HBCU_AMT")==null ? "0":
                                row.get("HBCU_AMT").toString());
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setPercent(getPct(amt,totAmt));
             cvAmounts.add(subReportsAmountsBean);
              //now cvAmounts has a collection of subReportsAmountsBeans
             }
        }
        
        //put vector of amountBeans into DetailBean
        SubcontractingReportsDetailBean subDetailBean = new SubcontractingReportsDetailBean();
        subDetailBean.setSponsor(row.get("SPONSOR").toString());
        subDetailBean.setVendorAmounts(cvAmounts);
                     
        return subDetailBean;
   }
   //////////////////////////////////////////////////
   // get294Details
   //  returns vector (of size 1) of SubcontractingReportsDetailBean
   //////////////////////////////////////////////////
   private CoeusVector get294Details(String awardNumber) throws CoeusException, DBException {
       CoeusVector cvDetails  = new CoeusVector();
 
       ///////////////////////////////////
        // call stored proc to get amounts
        ////////////////////////////////
       
       
       CoeusVector cvAmounts = new CoeusVector();
      
       Vector result = new Vector(3,2);
       HashMap row = null;
       Vector param = new Vector();
       
       param.addElement(new Parameter("AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING,awardNumber));
      
       if(dbEngine !=null){   
            result = dbEngine.executeRequest("Coeus",
            "call  subcontracting_rpts_pkg.get_294_amts_and_goals ( <<AWARD_NUMBER>> ,  "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
       
        if (vecSize >0){
           cvAmounts = new CoeusVector();
           
           SubReportsAmountsBean subReportsAmountsBean= new SubReportsAmountsBean();
         
           //vector should just be one row
           for(int i=0;i<vecSize;i++){
             row = (HashMap)result.elementAt(0);
             BigDecimal amt = new BigDecimal("0");
             BigDecimal goalAmt = new BigDecimal("0");
             BigDecimal totAmt = new BigDecimal(row.get("TOT_AMT").toString());
             BigDecimal totGoal = new BigDecimal(row.get("TOTAL_GOAL").toString());
            
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("LARGE BUSINESS");
             amt = new BigDecimal(row.get("LARGE_BUS_AMT") == null ? "0" : 
                                  row.get("LARGE_BUS_AMT").toString());
             goalAmt =new BigDecimal(row.get("LARGE_BUS_GOAL") == null ? "0" :
                                      row.get("LARGE_BUS_GOAL").toString()); 
             subReportsAmountsBean.setAmount( amt);
             subReportsAmountsBean.setGoalAmount(goalAmt);
             BigDecimal largePct = getPct(amt,totAmt);
             subReportsAmountsBean.setPercent(largePct);
             BigDecimal largeGoalPct = getPct(goalAmt,totGoal);
             subReportsAmountsBean.setGoalPercent(largeGoalPct);
             cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("SMALL BUSINESS");
             amt = new BigDecimal(row.get("SMALL_BUS_AMT") == null ? "0" :
                                    row.get("SMALL_BUS_AMT").toString());
             goalAmt = new BigDecimal(row.get("SMALL_BUS_GOAL") == null ? "0" :
                                    row.get("SMALL_BUS_GOAL").toString()); 
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setGoalAmount(goalAmt);
             
             if (amt.equals(zero)) {
                 subReportsAmountsBean.setPercent(zero);
             }else {
                subReportsAmountsBean.setPercent(hundred.subtract(largePct));
                          }
             
             if (goalAmt.equals(zero)) {
                 subReportsAmountsBean.setGoalPercent(zero);
             }else {
                 subReportsAmountsBean.setGoalPercent(hundred.subtract(largeGoalPct));
             }
             
             cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("WOMAN OWNED");
             amt = new BigDecimal(row.get("WOMAN_OWNED_AMT") == null ? "0":
                                row.get("WOMAN_OWNED_AMT").toString());
             goalAmt = new BigDecimal(row.get("WOMAN_OWNED_GOAL") == null ? "0" :
                                    row.get("WOMAN_OWNED_GOAL").toString()); 
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setGoalAmount(goalAmt);
             subReportsAmountsBean.setPercent(getPct(amt,totAmt));
             subReportsAmountsBean.setGoalPercent(getPct(goalAmt,totGoal));
             cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("DISADVANTAGED BUSINESS");
             amt = new BigDecimal(row.get("DISADVANTAGED_BUS_AMT")==null ? "0":
                                    row.get("DISADVANTAGED_BUS_AMT").toString());
             goalAmt=new BigDecimal(row.get("DISADVANTAGED_BUS_GOAL") == null ? "0" :
                                    row.get("DISADVANTAGED_BUS_GOAL").toString()); 
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setGoalAmount(goalAmt);
             subReportsAmountsBean.setPercent(getPct(amt,totAmt));
             subReportsAmountsBean.setGoalPercent(getPct(goalAmt,totGoal));
             cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("HUB");
             amt = new BigDecimal(row.get("HUB_AMT")==null ? "0" :
                                row.get("HUB_AMT").toString());
             goalAmt =new BigDecimal(row.get("HUB_GOAL")== null ? "0":
                                    row.get("HUB_GOAL").toString()); 
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setGoalAmount(goalAmt);
             subReportsAmountsBean.setPercent(getPct(amt,totAmt));
             subReportsAmountsBean.setGoalPercent(getPct(goalAmt,totGoal));
             cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("VET");
             amt= new BigDecimal(row.get("VET_AMT")==null ? "0" :
                                row.get("VET_AMT").toString());
             goalAmt=new BigDecimal(row.get("VET_GOAL")==null ? "0" :
                                    row.get("VET_GOAL").toString()); 
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setGoalAmount(goalAmt);
             subReportsAmountsBean.setPercent(getPct(amt,totAmt));
             subReportsAmountsBean.setGoalPercent(getPct(goalAmt,totGoal));
             cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("SDVO");
             amt= new BigDecimal(row.get("SDVO_AMT")==null ? "0" :
                                    row.get("SDVO_AMT").toString());
             goalAmt=new BigDecimal(row.get("SDV_GOAL")==null ? "0" :
                                    row.get("SDV_GOAL").toString()); 
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setGoalAmount(goalAmt);
             subReportsAmountsBean.setPercent(getPct(amt,totAmt));
             subReportsAmountsBean.setGoalPercent(getPct(goalAmt,totGoal));
             cvAmounts.add(subReportsAmountsBean);
             
             subReportsAmountsBean= new SubReportsAmountsBean();
             subReportsAmountsBean.setVendorType("HBCU");
             amt= new BigDecimal(row.get("HBCU_AMT")==null ? "0":
                                row.get("HBCU_AMT").toString());
             goalAmt =new BigDecimal(row.get("HBCU_GOAL") == null ? "0":
                                row.get("HBCU_GOAL").toString()); 
             subReportsAmountsBean.setAmount(amt);
             subReportsAmountsBean.setGoalAmount(goalAmt);
             subReportsAmountsBean.setPercent(getPct(amt,totAmt));
             subReportsAmountsBean.setGoalPercent(getPct(goalAmt,totGoal));
             cvAmounts.add(subReportsAmountsBean);
              //now cvAmounts has a collection of subReportsAmountsBeans
             }
         
        }
              
        SubcontractingReportsDetailBean subDetailBean = new SubcontractingReportsDetailBean();
        subDetailBean.setVendorAmounts(cvAmounts);
        
      
        ////////////////////////////////////////////////////
        //call stored proc to get agency info which should also 
        // go into subDetailBean
        //////////////////////////////////////////////////////
       
        Vector resulta = new Vector(3,2);
        HashMap rowa = null;
        Vector parama = new Vector();
        
        parama.addElement(new Parameter("AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING,awardNumber));
      
        if(dbEngine !=null){   
            resulta = dbEngine.executeRequest("Coeus",
            "call  subcontracting_rpts_pkg.get_contractor_info ( <<AWARD_NUMBER>> ,  "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", parama);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
       
        //should just be one row
         if(!resulta.isEmpty()){
          rowa = (HashMap)resulta.elementAt(0);
          subDetailBean.setAgencyName(rowa.get("NAME").toString());
          subDetailBean.setAgencyStreetAddress(rowa.get("ADDRESS").toString());
          subDetailBean.setAgencyCity(rowa.get("CITY").toString());
          subDetailBean.setAgencyState(rowa.get("STATE").toString());
          subDetailBean.setAgencyZip(rowa.get("POSTAL_CODE").toString());
         } 
       
      
          cvDetails = new CoeusVector();
          cvDetails.add(subDetailBean);
        return cvDetails;
  }
  
 private BigDecimal getPct(BigDecimal amt, BigDecimal totAmt) throws CoeusException   {
     
  BigDecimal pct = new BigDecimal("0.00");
  try {
      
     pct = amt.divide(totAmt,3, BigDecimal.ROUND_UP);   
     pct = pct.multiply(new BigDecimal("100"));
  } catch ( ArithmeticException e ){
      pct = new BigDecimal("0");
  }
     
     return pct;
 }
  
 
  private String getAdminActivity(String awardNumber) throws CoeusException, DBException {
      
        String adminActivity = null;
        
        Vector param = new Vector();
       
        param.addElement(new Parameter("ADMIN_ACTIVITY", 
          DBEngineConstants.TYPE_STRING, adminActivity, 
          DBEngineConstants.DIRECTION_OUT));
        param.addElement( new Parameter("AWARD_NUMBER",
          DBEngineConstants.TYPE_STRING, awardNumber));
        
        HashMap row = null;
        Vector result = new Vector();
     
          
        
        
       if(dbEngine !=null){
         result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING ADMIN_ACTIVITY>>= call subcontracting_rpts_pkg.fn_get_admin_activity ( <<AWARD_NUMBER>> ) }", param);  
         
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }      
        if(!result.isEmpty()){
          row = (HashMap)result.elementAt(0);
         adminActivity = row.get("ADMIN_ACTIVITY").toString();   
        }                
        
        return adminActivity;
  }
  //   private Calendar getTodayDate() {
     private java.util.Date getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
    //  cal.setTime(today);
    //  return cal;
      return today;
    }
  
}
  

