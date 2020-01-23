/*
 * ResearchAndRelProjectStream.java
 *
 * Created on Mar 15, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;
 
     
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.OrgAssurancesTypeStream;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;
import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.xml.bean.proposal.bean.AbstractBean;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPersonBean;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;

import edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator.KeyPersonTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.ProposalPersonTypeStream;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;

import java.util.Vector;
 

 
public class ResearchAndRelProjectStream
{
    ObjectFactory objFactory;
    edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory;
    edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory;
    
    private ResearchAndRelatedProjectType researchAndRelProj;
    private ProposalDevelopmentTxnBean proposalDevTxnBean;
    private ProposalActionTxnBean proposalActionTxnBean;
    private ProposalPersonFormBean propPersonBean;
    private ProposalKeyPersonFormBean propKeyPersonBean ; 
    private ProposalDevelopmentFormBean proposalBean;
    private ProposalPrintingTxnBean proposalPrintingTxnBean;
    private OrganizationMaintenanceDataTxnBean orgTxnBean;
    private OrganizationMaintenanceFormBean orgBean;
    private OrganizationMaintenanceFormBean perfOrgBean;
    private AbstractBean abstractBean;
    private ProposalPersonBean proposalPersonBean;
    private RolodexDetailsBean orgRolodexBean, perfOrgRolodexBean;
    private RolodexMaintenanceDataTxnBean rolodexTxnBean;
    
    /** Creates a new instance of ResearchAndRelProjectStream */
    public ResearchAndRelProjectStream(ObjectFactory objFactory,  
                            edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory,
                            edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory)
    {  
        this.objFactory = objFactory ;
        this.rarObjFactory = rarObjFactory;
        this.commonObjFactory = commonObjFactory;
        
        orgTxnBean = new OrganizationMaintenanceDataTxnBean();
        proposalActionTxnBean = new ProposalActionTxnBean();
        proposalPrintingTxnBean = new ProposalPrintingTxnBean();
        proposalDevTxnBean = new ProposalDevelopmentTxnBean() ;
        rolodexTxnBean = new RolodexMaintenanceDataTxnBean();
    }
    
    public ResearchAndRelatedProjectType getResearchAndRelProj(String propNumber,
                                                               String sponsor)
               throws CoeusException, DBException, javax.xml.bind.JAXBException
    {     
        //proposal info
        proposalBean = proposalDevTxnBean.getProposalDevelopmentDetails(propNumber);
        //organization info
        //start changes case 2406
//        orgBean = orgTxnBean.getOrganizationMaintenanceDetails(proposalBean.getOrganizationId());      
        //performing organization info
//        perfOrgBean = orgTxnBean.getOrganizationMaintenanceDetails(proposalBean.getPerformingOrganizationId());
        orgBean = getOrgData(propNumber);
        perfOrgBean = getPerfOrgData(propNumber);
        //end changes case 2406
        //organization contact info (rolodex) 
        String rolodexId = Integer.toString(orgBean.getContactAddressId()); 
        orgRolodexBean = rolodexTxnBean.getRolodexMaintenanceDetails(rolodexId);
        //performing organization contact info (rolodex)  
        String perfRolodexId = Integer.toString(perfOrgBean.getContactAddressId()); 
        perfOrgRolodexBean = rolodexTxnBean.getRolodexMaintenanceDetails(perfRolodexId);
     
        
        ResearchCoverPageType researchCoverPage;
        researchAndRelProj = objFactory.createResearchAndRelatedProject();
                         // always use Createxxx not createxxxType method - createxxxType
                         // method will not add <xxx> tag to the o/p     
           
      
        int version = proposalPrintingTxnBean.getVersion(propNumber);
      
        /**ResearchCoverPage
        */    
        
        ResearchCoverPageStream researchCoverPageStream =
                    new ResearchCoverPageStream(objFactory,rarObjFactory,commonObjFactory);
        researchCoverPage = researchCoverPageStream.getResearchCoverPage
                           (proposalBean,
                            orgBean,
                            perfOrgBean,
                            orgRolodexBean,
                            perfOrgRolodexBean, 
                            version);
        
                        
        researchAndRelProj.setResearchCoverPage(researchCoverPage);
     
        /**Project description
         */
        ProjectDescriptionStream projectDescStream = new ProjectDescriptionStream(objFactory,rarObjFactory);
        researchAndRelProj.setProjectDescription(projectDescStream.getProjectDescription(proposalBean,
                                                                       orgBean));
        
        /**BudgetSummary
         */
        //first check for budget
         if (version > 0) {
            BudgetSummaryStream budgetSummaryStream = 
               new BudgetSummaryStream(objFactory,rarObjFactory);
            researchAndRelProj.setBudgetSummary(budgetSummaryStream.getBudgetSummary(propNumber,
                                                            version, sponsor, orgBean));
        }
        
        /**Org Assurances
         */
        OrgAssurancesTypeStream orgAssStream = new OrgAssurancesTypeStream(rarObjFactory,commonObjFactory);
         
        researchAndRelProj.setOrgAssurances(orgAssStream.getOrgAssurances(orgBean));
     
        /**NSF extension - abstracts
         */
         Vector vecAbstracts = proposalPrintingTxnBean.getAbstracts(propNumber,version);
        //vecAbstracts is a vector of AbstractBeans
         AbstractStream abstractStream = new AbstractStream(objFactory);
      
        if (vecAbstracts != null) {
            for (int i = 0; i < vecAbstracts.size() ; i++) {
                abstractBean = (AbstractBean) vecAbstracts.get(i);
                researchAndRelProj.getAbstract().add(abstractStream.getAbstract(propNumber,abstractBean));
            }
        }
         
         /**NSF extension - proposal persons
         */
         Vector vecProposalPersons = proposalPrintingTxnBean.getProposalPersons(propNumber, version);
        //vecProposalPersons is a vector of ProposalPersonBeans
         ProposalPersonTypeStream proposalPersonStream = 
             new ProposalPersonTypeStream(rarObjFactory,commonObjFactory);
      
         if (vecProposalPersons != null) {
            for (int i = 0; i < vecProposalPersons.size() ; i++) {
                proposalPersonBean = (ProposalPersonBean) vecProposalPersons.get(i);
                //cowusqa-2420 start
                //researchAndRelProj.getProposalPerson().add(proposalPersonStream.getProposalPerson(proposalPersonBean));
                researchAndRelProj.getProposalPerson().add(proposalPersonStream.getProposalPerson(propNumber,proposalPersonBean));
                //cowusqa-2420 end
            }
        }
          
         /**Key persons
         */
          KeyPersonTypeStream keyPersonTypeStream = new KeyPersonTypeStream(objFactory,rarObjFactory,commonObjFactory);
        
          //add all investigators as key persons
          //get proposal lead unit
          String leadUnitNum = proposalActionTxnBean.getOwnedByUnit(propNumber);
          String piFlag;
          ProposalInvestigatorFormBean invBean ;
          ProposalPersonFormBean invPersonBean;
          Vector vecInvestigators =  proposalDevTxnBean.getProposalInvestigatorDetails(propNumber);
        
          //changes made for case 2229 - multipis
          
          Equals equalsPI = new Equals("principleInvestigatorFlag", true);
          Equals notEqualsPI = new Equals("principleInvestigatorFlag",false);
          Equals equalsMultipi = new Equals("multiPIFlag",true);
          NotEquals notPI = new NotEquals("principleInvestigatorFlag", true);
          NotEquals notMulti = new NotEquals("multiPIFlag",true);
          And multipi = new And(notEqualsPI, equalsMultipi);
          And coInvests = new And(notPI, notMulti);
          
          if (vecInvestigators != null) {  
             CoeusVector cvInvestigators = new CoeusVector();
             CoeusVector cvCoInvestigators = new CoeusVector();
             CoeusVector cvMultipis = new CoeusVector();
             CoeusVector cvPI = new CoeusVector();
             CoeusVector cvPIandCoPis = new CoeusVector();  
             cvInvestigators.addAll(vecInvestigators);
             cvPI = cvInvestigators;
             //cvPI holds all investigators - 
             //need to have PI first, followed by others sorted by name 
             cvPI = cvPI.filter(equalsPI);  //this gets the PI only
             cvMultipis = cvInvestigators.filter(multipi); // this gets all multipis
             cvCoInvestigators = cvInvestigators.filter(coInvests); //this gets co-investigators
             cvPIandCoPis.addAll(cvPI);
             cvCoInvestigators.sort("personName");
             cvMultipis.sort("personName");
             cvPIandCoPis.addAll(cvMultipis);
             cvPIandCoPis.addAll(cvCoInvestigators);
            
             for (int vecCount = 0 ; vecCount < cvPIandCoPis.size() ; vecCount++){
                   invBean = (ProposalInvestigatorFormBean)cvPIandCoPis.get(vecCount) ;
                   //get a person bean for the investigator
                   
                   if (invBean.isPrincipleInvestigatorFlag() || invBean.isMultiPIFlag()) {
                       piFlag="Y";}
                   else {
                       piFlag="N";}
                   invPersonBean = proposalDevTxnBean.getProposalPersonDetails(propNumber, invBean.getPersonId());
                   if(invPersonBean==null) continue;
                   researchAndRelProj.getKeyPerson().add(keyPersonTypeStream.getKeyPerson
                      (invPersonBean,leadUnitNum,orgRolodexBean,piFlag));                    
               }   
            }   else {
                //no investigators
                researchAndRelProj.getKeyPerson().add(keyPersonTypeStream.getKeyPerson
                   ("XXXXXXXXX",leadUnitNum));
            }
          
         //add other key persons
          
         Vector vecKeyPersons = proposalDevTxnBean.getProposalKeyPersonDetails(propNumber);
      
         //vecKeyPersons is a vector of ProposalKeyPersonFormBeans                                                                            protocolInfoBean.getSequenceNumber()) ;
         
         if (vecKeyPersons != null) {   
              CoeusVector cvKeyPersons = new CoeusVector();
              cvKeyPersons.addAll(vecKeyPersons);
              
              cvKeyPersons.sort("personName");
              for (int vecCount = 0 ; vecCount < cvKeyPersons.size() ; vecCount++) {
                  propKeyPersonBean = (ProposalKeyPersonFormBean) cvKeyPersons.get(vecCount);
                  propKeyPersonBean.getPersonName();
                  propPersonBean = proposalDevTxnBean.getProposalPersonDetails(propNumber,propKeyPersonBean.getPersonId());
                  
                  researchAndRelProj.getKeyPerson().add(keyPersonTypeStream.getKeyPerson
                            (propKeyPersonBean,propPersonBean,orgRolodexBean));
              }  
         }
         
         
             
        /*nsf previous award number  
         */
        researchAndRelProj.setNSFPreviousAwardNumber(proposalPrintingTxnBean.getNSFPreviousAward(propNumber));
       
        /*nsf project duration  
         */
        researchAndRelProj.setNSFProjectDuration(new BigInteger
          (String.valueOf(proposalPrintingTxnBean.getNSFProjectDuration(propNumber))));
        
        //nih application extension
        researchAndRelProj.setNihInventions(proposalPrintingTxnBean.getInventions(propNumber));
        
        /*nih application extension - nih grant number
         */
        HashMap hashGrantNumber = proposalPrintingTxnBean.getNihGrant(propNumber);
        
        /*nih extension - project start dt
         */
        Date startDt = proposalPrintingTxnBean.getProjectStartDt(propNumber);
        if (startDt != null) {
            researchAndRelProj.setTotalProjectStartDt(convertDateStringToCalendar(startDt.toString()));
          }
        Date endDt = proposalPrintingTxnBean.getProjectEndDt(propNumber);
        if (endDt != null) {
          researchAndRelProj.setTotalProjectEndDt(convertDateStringToCalendar(
                endDt.toString()));
          }
        researchAndRelProj.setNihActivityCode( (String) hashGrantNumber.get("ACTIVITYCODE"));
        researchAndRelProj.setNihPriorGrantNumber((String) hashGrantNumber.get("GRANTNUMBER"));
        researchAndRelProj.setNihApplicationTypeCode((String) hashGrantNumber.get("APPLICATIONTYPECODE"));
    
        return researchAndRelProj ;
    }
    
    private OrganizationMaintenanceFormBean getOrgData(String propNumber)
            throws  CoeusException,DBException{
        HashMap hmOrg = proposalPrintingTxnBean.getOrganizationID(propNumber,"O");
        String orgID = null;
        if (hmOrg!= null && hmOrg.get("ORGANIZATION_ID") != null){
               orgID = hmOrg.get("ORGANIZATION_ID").toString();           
        }
        return orgTxnBean.getOrganizationMaintenanceDetails(orgID);
    }
    
      private OrganizationMaintenanceFormBean getPerfOrgData(String propNumber)
            throws CoeusException,DBException{
        HashMap hmPerfOrg = proposalPrintingTxnBean.getOrganizationID(propNumber, "P");
        String perfOrgID = null;
        if (hmPerfOrg!= null && hmPerfOrg.get("ORGANIZATION_ID") != null){
               perfOrgID = hmPerfOrg.get("ORGANIZATION_ID").toString();
        }
        return orgTxnBean.getOrganizationMaintenanceDetails(perfOrgID);
    }
    
    /*end case 2406*/
      
     private Calendar convertDateStringToCalendar(String dateStr)
    {
        java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
        DateUtils dtUtils = new DateUtils();
        if (dateStr != null)
        {    
            if (dateStr.indexOf('-')!= -1)
            { // if the format obtd is YYYY-MM-DD
              dateStr = dtUtils.formatDate(dateStr,"MM/dd/yyyy");
            }    
            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
                        Integer.parseInt(dateStr.substring(0,2)) - 1,
                        Integer.parseInt(dateStr.substring(3,5))) ;
            
            return calDate ;
        }
        return null ;
     }   
}
