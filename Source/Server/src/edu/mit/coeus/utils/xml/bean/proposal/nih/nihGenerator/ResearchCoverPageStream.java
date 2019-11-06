/*
 * ResearchCoverPageStream.java
 *
 * Created on Feb 27, 2004
 */
 
package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

import java.util.Vector;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.rolodexmaint.bean.*;

import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.*;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.s2s.generator.stream.bean.RRPerformanceSiteTxnBean;
import edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSiteType;
import java.util.ArrayList;

public class ResearchCoverPageStream
{
    ObjectFactory objFactory;
    edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory;
    edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory;
     
    ResearchCoverPageType researchCoverPage ;
   
    ProposalPrintingTxnBean propPrintingTxnBean;
   
    
    ProposalDevelopmentFormBean propDevFormBean;
    DepartmentPersonFormBean orgContactPersonBean, authRepPersonBean;
    
      
    /** Creates a new instance of ResearchCoverPageStream */
    public ResearchCoverPageStream(ObjectFactory objFactory,
                         edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory,
                         edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory)
    {   
        this.objFactory = objFactory ;
        this.rarObjFactory = rarObjFactory;
        this.commonObjFactory = commonObjFactory;
         
        propPrintingTxnBean = new ProposalPrintingTxnBean();
       
        
    }
    
  
      public ResearchCoverPageType getResearchCoverPage
                       (ProposalDevelopmentFormBean propDevFormBean, 
                        OrganizationMaintenanceFormBean orgBean,
                        OrganizationMaintenanceFormBean perfOrgBean,
                        RolodexDetailsBean orgRolodexBean, 
                        RolodexDetailsBean perfOrgRolodexBean,
                        int version)                         
        throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
       String propNumber = propDevFormBean.getProposalNumber();
       
       researchCoverPage = objFactory.createResearchCoverPage();
                         // always use Createxxx not createxxxType method - createxxxType
                         // method will not add <xxx> tag to the o/p     
         
       
         // get proposal details 
             
         //get the organization contact bean (rolodex) from the database
         String rolodexId = Integer.toString(orgBean.getContactAddressId()); 
         //get the administrative contact for the proposal
         orgContactPersonBean = propPrintingTxnBean.getOrgContactPerson(propNumber); 
         //get authorized rep
         authRepPersonBean = propPrintingTxnBean.getAuthorizedRep(propNumber);
          
         /**submission category
          */
         CoreSubmissionCategoryTypeStream coreSubCatTypeStream 
               = new CoreSubmissionCategoryTypeStream(rarObjFactory);
         researchCoverPage.setSubmissionCategory(coreSubCatTypeStream.getCoreSubCategory(propDevFormBean));
         System.out.println("ResearchCoverPageStream - after setSubmissionCategory");
          
         /**applicant category
          */
         CoreApplicationCategoryTypeStream coreAppCatTypeStream
                = new CoreApplicationCategoryTypeStream(rarObjFactory); 
         researchCoverPage.setApplicationCategory(coreAppCatTypeStream.getCoreAppCategory(propDevFormBean));
        
         /**applicant submission qualifiers
          */
         CoreApplicantSubmissionQualifiersTypeStream coreAppSubQualTypeStream
            = new CoreApplicantSubmissionQualifiersTypeStream(rarObjFactory);
         researchCoverPage.setApplicantSubmissionQualifiers(coreAppSubQualTypeStream.getCoreAppSubQual(propDevFormBean));
     
         /**Fed agency receipt qualifiers
          */
         CoreFederalAgencyReceiptQualifiersTypeStream coreFedReceiptQualStream
            = new CoreFederalAgencyReceiptQualifiersTypeStream(rarObjFactory);
         researchCoverPage.setFederalAgencyReceiptQualifiers(coreFedReceiptQualStream.getCoreFedAgencyRecQual(propNumber));
      
         /** state receipt qualifiers
          */
         CoreStateReceiptQualifiersTypeStream coreStateRecQualStream
                = new CoreStateReceiptQualifiersTypeStream(rarObjFactory);
         researchCoverPage.setStateReceiptQualifiers(coreStateRecQualStream.getCoreStateReceiptQual(propNumber));
    
         /** state intergovernmental
          */
         CoreStateIntergovernmentalReviewTypeStream coreStateInterRevStream
                = new CoreStateIntergovernmentalReviewTypeStream(rarObjFactory);
         researchCoverPage.setStateIntergovernmentalReview(coreStateInterRevStream.getCoreStateIntergovReview(propNumber));
          
          /**Federal Debt Delinquency Questions
          */
         CoreFederalDebtDelinquencyQuestionsTypeStream coreFedDebtDelStream
            = new CoreFederalDebtDelinquencyQuestionsTypeStream(rarObjFactory);
         researchCoverPage.setFederalDebtDelinquencyQuestions(coreFedDebtDelStream.getCoreFedDebtQuestions(propNumber));
         
          /**project dates
          */
         CoreProjectDatesTypeStream coreProjectsDatesStream
            = new CoreProjectDatesTypeStream(rarObjFactory);
         researchCoverPage.setProjectDates(coreProjectsDatesStream.getCoreProjectDates(propDevFormBean));
         
         /**budget totals
         */
         CoreBudgetTotalsTypeStream coreBudTotTypeStream = new CoreBudgetTotalsTypeStream(rarObjFactory);
         researchCoverPage.setBudgetTotals(coreBudTotTypeStream.getBudgetTotals(propNumber,version));
          
         /**project title
          */     
         if (propDevFormBean.getTitle() == null){
              researchCoverPage.setProjectTitle("Unknown");
         }else researchCoverPage.setProjectTitle( propDevFormBean.getTitle());
         
         
         /** other agency questions
          */
         OtherAgencyQuestionsTypeStream otherAgencyQuestionsStream
            = new OtherAgencyQuestionsTypeStream(rarObjFactory);
         researchCoverPage.setOtherAgencyQuestions(
                 otherAgencyQuestionsStream.getOtherAgencyQuestions( propDevFormBean));      
         
         /**applicant organization - this is the nih applicant organization type
          */
         ApplicantOrganizationTypeStream appOrgTypeStream 
                = new ApplicantOrganizationTypeStream(objFactory,rarObjFactory,commonObjFactory);
         
         researchCoverPage.setApplicantOrganization(appOrgTypeStream.getNihApplicantOrgTypeInfo(orgBean,
                                            orgRolodexBean,orgContactPersonBean));
     
         /** Primary project site (performing organization
          */
      
       
         ProjectSiteTypeStream projSiteTypeStream =
                new ProjectSiteTypeStream(rarObjFactory,commonObjFactory);
         researchCoverPage.setPrimaryProjectSite(projSiteTypeStream.getProjectSite(perfOrgBean,perfOrgRolodexBean));
         
         /** alternate project sites
          */ 
         ProjectSiteType alternateSite;
         ArrayList alternateSites = projSiteTypeStream.getAlternateProjectSites(propNumber);
         if (alternateSites != null) {
             for (int i = 0; i < alternateSites.size(); i++){
                 alternateSite = (ProjectSiteType) alternateSites.get(i);
                 researchCoverPage.getAlternateProjectSites().add(alternateSite);
             }
         }
        
         /**principal investigator
          */
         ProgramDirectorPrincipalInvestigatorTypeStream piStream
            = new ProgramDirectorPrincipalInvestigatorTypeStream(objFactory,rarObjFactory,commonObjFactory);
      
         researchCoverPage.setProgramDirectorPrincipalInvestigator
               (piStream.getPIInfo(propNumber,orgRolodexBean, propDevFormBean));
               //case 1350 - added propDevFormBean argument
         
          
         /** funding opportunities
          */
         FundingOpportunityDetailsStream fundingOppStream
             = new FundingOpportunityDetailsStream(objFactory);
         researchCoverPage.setFundingOpportunityDetails(fundingOppStream.getFundingOppInfo(propDevFormBean));
         
         /**auth org rep
          */
         AuthorizedOrganizationalRepresentativeTypeStream authOrgRepTypeStream
                = new AuthorizedOrganizationalRepresentativeTypeStream(objFactory,rarObjFactory,commonObjFactory);
         researchCoverPage.setAuthorizedOrganizationalRepresentative(
                          authOrgRepTypeStream.getAuthRepInfo(orgRolodexBean,authRepPersonBean));
          
     
        return researchCoverPage ;
    }
    
  
}
