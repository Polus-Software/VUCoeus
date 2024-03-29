/*
 * ProtocolStream.java
 *
 * Created on November 21, 2003, 4:03 PM
 */

package edu.mit.coeus.utils.xml.generator;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import java.util.* ;
import java.math.BigInteger;

import edu.mit.coeus.irb.bean.* ;
import edu.mit.coeus.utils.xml.bean.schedule.ObjectFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.utils.xml.bean.schedule.* ; 
import edu.mit.coeus.utils.DateUtils;

 
public class ProtocolStream
{
    ObjectFactory objFactory ;
    ProtocolDataTxnBean protocolDataTxnBean ;
    ProtocolType protocol ;
    
    /** Creates a new instance of ProtocolStream */
    public ProtocolStream(ObjectFactory objFactory)
    {
        this.objFactory = objFactory ;
        protocolDataTxnBean = new ProtocolDataTxnBean() ;
    }
    
    // this method is used to get complete protocol data
    public ProtocolType getProtocol(String protocolId, int sequenceNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        protocol = objFactory.createProtocol() ;  // always use CreateProtocol not createProtocolType method as createProtocolTYpe method will not add <Protocol> tag to the o/p       
        
        // get all possible info abt a procol and build the xml
        ProtocolInfoBean protocolInfoBean  = protocolDataTxnBean.getProtocolInfo(protocolId, sequenceNumber);

        // add protocolmaster data to protocol
        protocol.setProtocolMasterData(getProtocolMasterData(protocolId, sequenceNumber)) ;
        
        // create personStream variable
        PersonStream personStream = new PersonStream(objFactory) ; 
               
        addInvestigators(protocolInfoBean, personStream) ;
        addKeyStudyPerson(protocolInfoBean, personStream) ;
        addCorrespondent(protocolInfoBean, personStream) ;
        addResearchArea(protocolInfoBean, personStream) ;
        addFundingSource(protocolInfoBean, personStream) ;
        addVulnerableSubject(protocolInfoBean, personStream) ;
        addSpecialReview(protocolInfoBean, personStream) ;
        addSubmissionDetails(protocolInfoBean, personStream) ;
        //Added for Case 2176 - Risk level category - Start
        addRiskLevels(protocolInfoBean, personStream);
        //Added for Case 2176 - Risk level category - End        
        return protocol ;
    }
    
    
    // this method is used to get complete protocol data for a particular submission
    public ProtocolType getProtocol(String protocolId, int sequenceNumber, int submissionNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        protocol = objFactory.createProtocol() ;  // always use CreateProtocol not createProtocolType method as createProtocolTYpe method will not add <Protocol> tag to the o/p       
        
        // get all possible info abt a procol and build the xml
        ProtocolInfoBean protocolInfoBean  = protocolDataTxnBean.getProtocolInfo(protocolId, sequenceNumber);

        // add protocolmaster data to protocol
        protocol.setProtocolMasterData(getProtocolMasterData(protocolId, sequenceNumber)) ;
        
        // create personStream variable
        PersonStream personStream = new PersonStream(objFactory) ; 
               
        addInvestigators(protocolInfoBean, personStream) ;
        addKeyStudyPerson(protocolInfoBean, personStream) ;
        addCorrespondent(protocolInfoBean, personStream) ;
        addResearchArea(protocolInfoBean, personStream) ;
        addFundingSource(protocolInfoBean, personStream) ;
        addVulnerableSubject(protocolInfoBean, personStream) ;
        addSpecialReview(protocolInfoBean, personStream) ;
        addSubmissionDetails(protocolInfoBean, submissionNumber, personStream, "Yes") ;
        // prps start - feb 17 2004
        // reiterate again with the parent submission number
        XMLGeneratorTxnBean xmlBean = new XMLGeneratorTxnBean() ;
        int parentSubmissionNumber = xmlBean.getParentSubmissionNumber(protocolInfoBean.getProtocolNumber(), submissionNumber ) ;
        addSubmissionDetails(protocolInfoBean, parentSubmissionNumber, personStream, "No") ;
        // prps end - feb 17 2004
        //Added for Case 2176 - Risk level category - Start
        addRiskLevels(protocolInfoBean, personStream);
        //Added for Case 2176 - Risk level category - End        
        
        return protocol ;
    }       
    
    
    // This method is used to get just the master data
    public ProtocolMasterDataType getProtocolMasterData(String protocolId, int sequenceNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        ProtocolMasterDataType protocolMaster = objFactory.createProtocolMasterDataType() ;
        
        if (protocolId == null)
        {
            System.out.println("** protocolId is null **") ;
            return protocolMaster ;
        }    
        
        ProtocolInfoBean protocolInfoBean  = protocolDataTxnBean.getProtocolInfo(protocolId, sequenceNumber);
        
        
        if (protocolInfoBean.getProtocolNumber() == null) System.out.println("** getProtocolNumber is null **") ;
        protocolMaster.setProtocolNumber(protocolInfoBean.getProtocolNumber()) ;
        
        protocolMaster.setSequenceNumber(new BigInteger(String.valueOf(protocolInfoBean.getSequenceNumber()))) ;
                
        if (protocolInfoBean.getTitle() == null) System.out.println("** getTitle is null **") ;
        protocolMaster.setProtocolTitle(protocolInfoBean.getTitle()) ;

        if (protocolInfoBean.getApplicationDate() != null)
        {     
             protocolMaster.setApplicationDate(convertDateStringToCalendar(protocolInfoBean.getApplicationDate().toString())) ;  
        } 
        else
        {
            System.out.println("** getApplicationDate is null **") ;
        }    
        try
        {
            protocolMaster.setProtocolStatusCode(new BigInteger(String.valueOf(protocolInfoBean.getProtocolStatusCode()))) ;
        }
        catch(Exception ex)
        {
            System.out.println("** getProtocolStatusCode is null **") ;
        }
        
        if (protocolInfoBean.getProtocolStatusDesc() == null) System.out.println("** getProtocolStatusDesc() is null **") ;
        protocolMaster.setProtocolStatusDesc(protocolInfoBean.getProtocolStatusDesc()) ;
        
        try
        {
            protocolMaster.setProtocolTypeCode(new BigInteger(String.valueOf(protocolInfoBean.getProtocolTypeCode()))) ;
        }
        catch(Exception ex)
        {
            System.out.println("** getProtocolTypeCode is null **") ;
        }
        
        if (protocolInfoBean.getProtocolTypeDesc() == null) System.out.println("** getProtocolTypeDesc() is null **") ;
        protocolMaster.setProtocolTypeDesc(protocolInfoBean.getProtocolTypeDesc()) ;


        if (protocolInfoBean.getDescription() != null)
        {
            protocolMaster.setProtocolDescription(protocolInfoBean.getDescription()) ;
        }    

        if (protocolInfoBean.getApprovalDate() != null)
        {
            protocolMaster.setApprovalDate(convertDateStringToCalendar(protocolInfoBean.getApprovalDate().toString())) ;  
        }    
        
        //Added for COEUSQA-3499 Add Last Approval Date to IRB schema -Start
        if (protocolInfoBean.getLastApprovalDate() != null) {
            protocolMaster.setLastApprovalDate(convertDateStringToCalendar(protocolInfoBean.getLastApprovalDate().toString())) ;
        }
        //COEUSQA-3499 -End
        
        if (protocolInfoBean.getExpirationDate() != null)
        {
            protocolMaster.setExpirationDate(convertDateStringToCalendar(protocolInfoBean.getExpirationDate().toString())) ;  
        }    

        protocolMaster.setBillableFlag(protocolInfoBean.isBillableFlag()) ;

        if (protocolInfoBean.getFDAApplicationNumber() != null)
        {
            protocolMaster.setFdaApplicationNumber(protocolInfoBean.getFDAApplicationNumber()) ;
        }    

        if (protocolInfoBean.getRefNum_1() != null)
        {
             protocolMaster.setRefNumber1(protocolInfoBean.getRefNum_1()) ;
        }    

        if (protocolInfoBean.getRefNum_2() != null)
        {
             protocolMaster.setRefNumber2(protocolInfoBean.getRefNum_2()) ;
        }    
 
         return protocolMaster ;
    }

    
    private void addInvestigators(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
        // get the investigators for this protocol
        if (protocolInfoBean.getProtocolNumber() == null)
        {
             System.out.println("** addInvestigators : getProtocolNumber is null **") ;
        }    
        if(protocolInfoBean.getSequenceNumber() <= 0)
        {    
            System.out.println("** addInvestigators : getSequenceNumber is null **") ;
        }
        
        Vector vecInvestigator = protocolDataTxnBean.getProtocolInvestigators(protocolInfoBean.getProtocolNumber(),
                                                                              protocolInfoBean.getSequenceNumber()) ;
        if (vecInvestigator != null)
        {    
            for (int vecCount = 0 ; vecCount < vecInvestigator.size() ; vecCount++)
               {
                   ProtocolInvestigatorsBean protocolInvestigatorsBean = 
                   (ProtocolInvestigatorsBean) vecInvestigator.get(vecCount) ;
                       protocol.getInvestigator().add(personStream.getInvestigator(protocolInvestigatorsBean)) ; 
               }// end for    
        }// end if  vecInvestigators       
        else
        {
               System.out.println("** addInvestigators : vecInvestigators is null **") ;
        }    
     
    }
    
    
    private void addKeyStudyPerson(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {    
        // get KeyStudyperson for this protocol
        if (protocolInfoBean.getProtocolNumber() == null)
        {
             System.out.println("** addKeyStudyPerson : getProtocolNumber is null **") ;
        }    
        if(protocolInfoBean.getSequenceNumber() <= 0)
        {    
            System.out.println("** addKeyStudyPerson : getSequenceNumber is null **") ;
        }
        
            Vector vecKeyStudyPerson = protocolDataTxnBean.getProtocolKeyPersonList(protocolInfoBean.getProtocolNumber(),
                                                                                    protocolInfoBean.getSequenceNumber()) ;
            if (vecKeyStudyPerson != null)
            {    
               for (int vecCount = 0 ; vecCount < vecKeyStudyPerson.size() ; vecCount++)
               {
                   KeyStudyPersonType keyStudyPerson = objFactory.createKeyStudyPersonType() ;
                   ProtocolKeyPersonnelBean protocolKeyStudyPersonBean = 
                   (ProtocolKeyPersonnelBean) vecKeyStudyPerson.get(vecCount) ;
                    protocol.getKeyStudyPerson().add(personStream.getKeyStudyPersonnel(protocolKeyStudyPersonBean)) ;
               }// end for    
           }// end if  vecKeyStudyPerson
               
     
    }
    
    
    private void addCorrespondent(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
            // get correspondent for this protocol
            if (protocolInfoBean.getProtocolNumber() == null)
            {
                 System.out.println("** addCorrespondent : getProtocolNumber is null **") ;
            }    
            if(protocolInfoBean.getSequenceNumber() <= 0)
            {    
                System.out.println("** addCorrespondent : getSequenceNumber is null **") ;
            }
            Vector vecCorrespondent = protocolDataTxnBean.getProtocolCorrespondents(protocolInfoBean.getProtocolNumber(),
                                                                                    protocolInfoBean.getSequenceNumber()) ;
            if (vecCorrespondent != null)
            {    
               for (int vecCount = 0 ; vecCount < vecCorrespondent.size() ; vecCount++)
               {
                   CorrespondentType correspondent = objFactory.createCorrespondentType() ;
                   ProtocolCorrespondentsBean protocolCorrespondentsBean = 
                   (ProtocolCorrespondentsBean) vecCorrespondent.get(vecCount) ;
                    protocol.getCorrespondent().add(personStream.getCorrespondent(protocolCorrespondentsBean)) ;

               }// end for    
           }// end if  vecCorrespondent
     
    }
    
    
    private void addResearchArea(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
            // get Research Area for this protocol
            if (protocolInfoBean.getProtocolNumber() == null)
            {
                 System.out.println("** addResearchArea : getProtocolNumber is null **") ;
            }    
            if(protocolInfoBean.getSequenceNumber() <= 0)
            {    
                System.out.println("** addResearchArea : getSequenceNumber is null **") ;
            }
            Vector vecResearchArea = protocolDataTxnBean.getProtocolResearchArea(protocolInfoBean.getProtocolNumber(),
                                                                                protocolInfoBean.getSequenceNumber()) ;
            ResearchAreaStream researchAreaStream = new ResearchAreaStream(objFactory) ;
            if (vecResearchArea != null)
            {
                for (int vecCount=0 ; vecCount < vecResearchArea.size() ; vecCount++)
                {
                    ProtocolReasearchAreasBean protocolReasearchAreasBean = 
                                    (ProtocolReasearchAreasBean) vecResearchArea.get(vecCount) ;
                    protocol.getResearchArea().add(researchAreaStream.getResearchArea(protocolReasearchAreasBean, "Protocol")) ;
                }// end for    
            } // end if vecResearchArea    
            else
            {
                System.out.println("** addResearchArea : vecResearchArea is null **") ;
            }
    }
    
    private void addFundingSource(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        //case 1522 start
         int fundingSourceTypeCode;
         String fundingSourceName, fundingSourceCode;
        //case 1522 end
         
       // get Funding Source for this protocol 
        
            if (protocolInfoBean.getProtocolNumber() == null)
            {
                 System.out.println("** addFundingSource : getProtocolNumber is null **") ;
            }    
            if(protocolInfoBean.getSequenceNumber() <= 0)
            {    
                System.out.println("** addFundingSource : getSequenceNumber is null **") ;
            }
            Vector vecFundingSource = protocolDataTxnBean.getProtocolFundingSources(protocolInfoBean.getProtocolNumber(),
                                                                                protocolInfoBean.getSequenceNumber()) ;
                                                                     
           
          
            
            if (vecFundingSource != null)
            {
                for (int vecCount=0 ; vecCount < vecFundingSource.size() ; vecCount++)
                {
                 
                    ProtocolType.FundingSourceType fundingSource = objFactory.createProtocolTypeFundingSourceType() ;
                    ProtocolFundingSourceBean protocolFundingSourceBean =
                                                (ProtocolFundingSourceBean) vecFundingSource.get(vecCount) ;
                     /*case 1522 - add funding source name start comment out
                    //Reqd           
                    if (protocolFundingSourceBean.getFundingSource() == null) System.out.println("** addFundingSource : getFundingSource() is null **") ;
                    fundingSource.setFundingSourceName(protocolFundingSourceBean.getFundingSource()) ; //prps check number is added instead of name
                    
                    //Reqd
                    if (protocolFundingSourceBean.getFundingSourceTypeDesc() == null) System.out.println("** addFundingSource : getFundingSourceTypeDesc() is null **") ;
                    fundingSource.setTypeOfFundingSource(protocolFundingSourceBean.getFundingSourceTypeDesc()) ;
                    
                    protocol.getFundingSource().add(fundingSource) ;
                  * case 1522 end comment out*/
                  /* case 1522 start new code */
                     
                      fundingSourceCode = protocolFundingSourceBean.getFundingSource();
                      fundingSourceTypeCode = protocolFundingSourceBean.getFundingSourceTypeCode() ;
                      fundingSourceName = getFundingSourceNameForType(fundingSourceTypeCode, fundingSourceCode);
                       
                      fundingSource.setFundingSourceName(fundingSourceName) ; 
                    
                      if (protocolFundingSourceBean.getFundingSourceTypeDesc() == null) 
                           System.out.println("** addFundingSource : getFundingSourceTypeDesc() is null **") ;
                        fundingSource.setTypeOfFundingSource(protocolFundingSourceBean.getFundingSourceTypeDesc()) ;
                    
                     protocol.getFundingSource().add(fundingSource) ;
                  /* case 1522 end new code */
                } // end for   
            }  // end if vecFundingSource  
                  
                  
           
    }
    
    //added for case 1522
        private String getFundingSourceNameForType(int sourceType, String sourceCode)
         throws CoeusException, DBException {
        String name=null;
        if (sourceType ==  1){
            // get sponsor name
            SponsorMaintenanceDataTxnBean sponsorTxnBean
                = new SponsorMaintenanceDataTxnBean();
          
            SponsorMaintenanceFormBean sponsorBean
                = sponsorTxnBean.getSponsorMaintenanceDetails(sourceCode);
                if (sponsorBean !=null) {
                    name = sponsorBean.getName();
                }
              
        } else if (sourceType ==  2){
            // get unit name
            UnitDataTxnBean unitTxnBean = new UnitDataTxnBean();
          
                UnitDetailFormBean unitBean
                    = unitTxnBean.getUnitDetails(sourceCode);
                if (unitBean!=null){
                    name = unitBean.getUnitName();
            }
        } else {
            // other
            name = sourceCode;
        }
        return name;
    }
        
    private void addVulnerableSubject(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
            // get VulnerableSubject for this protocol
        if (protocolInfoBean.getProtocolNumber() == null)
            {
                 System.out.println("** addVulnerableSubject : getProtocolNumber is null **") ;
            }    
            if(protocolInfoBean.getSequenceNumber() <= 0)
            {    
                System.out.println("** addVulnerableSubject : getSequenceNumber is null **") ;
            }
            Vector vecVulnerableSubject = protocolDataTxnBean.getProtocolVulnerableSubList(protocolInfoBean.getProtocolNumber(),
                                                                                protocolInfoBean.getSequenceNumber()) ;
            if (vecVulnerableSubject != null)
            {
                for (int vecCount=0 ; vecCount < vecVulnerableSubject.size() ; vecCount++)
                {
                    VulnerableSubjectType vulnerableSubject = objFactory.createVulnerableSubjectType() ;
                    ProtocolVulnerableSubListsBean protocolVulnerableSubListsBean =
                                                (ProtocolVulnerableSubListsBean) vecVulnerableSubject.get(vecCount) ;
                
                    try
                    {
                        //Protocol Enhancment - Subjects Count can be null Start
                        //vulnerableSubject.setVulnerableSubjectCount(new BigInteger(String.valueOf(protocolVulnerableSubListsBean.getSubjectCount()))) ;
                        if(protocolVulnerableSubListsBean.getSubjectCount() == null){
                            vulnerableSubject.setVulnerableSubjectCount(new BigInteger("0")) ;
                        }else{
                            vulnerableSubject.setVulnerableSubjectCount(new BigInteger(String.valueOf(protocolVulnerableSubListsBean.getSubjectCount()))) ;
                        }
                        //Protocol Enhancment - Subjects Count can be null End
                    }
                    catch(Exception vecSubCount)
                    {
                        // do nothing so that this tag doesnt get added to the xml generated
                        System.out.println("** addVulnerableSubject : setVulnerableSubjectCount is null **") ;
                    }
                    
                    try 
                    {
                        vulnerableSubject.setVulnerableSubjectTypeCode(new BigInteger(String.valueOf(protocolVulnerableSubListsBean.getVulnerableSubjectTypeCode()))) ;
                    }
                    catch(Exception vecSubTypCode)
                    {
                         // do nothing so that this tag doesnt get added to the xml generated
                        System.out.println("** addVulnerableSubject : setVulnerableSubjectTypeCode is null **") ;
                    }
                    if (protocolVulnerableSubListsBean.getVulnerableSubjectTypeDesc() == null) System.out.println("** addVulnerableSubject : getProtocolNumber is null **") ;
                    vulnerableSubject.setVulnerableSubjectTypeDesc(protocolVulnerableSubListsBean.getVulnerableSubjectTypeDesc()) ;
                    
                    protocol.getVulnerableSubject().add(vulnerableSubject) ;
                } // end for    
            } // end if vecVulnerableSubject
    }


    private void addSpecialReview(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
         // get Special Review for this protocol
        if (protocolInfoBean.getProtocolNumber() == null)
            {
                 System.out.println("** addSpecialReview : getProtocolNumber is null **") ;
            }    
            if(protocolInfoBean.getSequenceNumber() <= 0)
            {    
                System.out.println("** addSpecialReview : getSequenceNumber is null **") ;
            }
            Vector vecSpecialReview = protocolDataTxnBean.getProtocolSpecialReview(protocolInfoBean.getProtocolNumber(),
                                                                                protocolInfoBean.getSequenceNumber()) ;
            if (vecSpecialReview != null)
            {
                for (int vecCount=0 ; vecCount < vecSpecialReview.size() ; vecCount++)
                {
                    SpecialReviewType specialReview = objFactory.createSpecialReviewType() ;
                    ProtocolSpecialReviewFormBean specialReviewBean =
                                                (ProtocolSpecialReviewFormBean) vecSpecialReview.get(vecCount) ;
                
                    java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
                    if (specialReviewBean.getApplicationDate() != null)
                    {    
                        specialReview.setSpecialReviewApplicationDate(convertDateStringToCalendar(specialReviewBean.getApplicationDate().toString())) ;  
                    }
                    else
                    { // insert blank date
                         specialReview.setSpecialReviewApplicationDate(calDate) ;
                    }
                    
                    if (specialReviewBean.getApprovalDate() != null)
                    {    
                        specialReview.setSpecialReviewApprovalDate(convertDateStringToCalendar(specialReviewBean.getApprovalDate().toString())) ;  
                    }
                    else
                    { // insert blank date
                         specialReview.setSpecialReviewApprovalDate(calDate) ;
                    }

                    try
                    {
                        specialReview.setSpecialReviewApprovalTypeCode(new BigInteger(String.valueOf(specialReviewBean.getApprovalCode()))) ;
                    }
                    catch(Exception sratc)
                    {
                         // do nothing so that this tag doesnt get added to the xml generated
                        System.out.println("** addSpecialReview : getApprovalCode is null **") ;
                    }
                    
                    if (specialReviewBean.getApprovalDescription() == null) System.out.println("** addSpecialReview : getApprovalDescription() is null **") ;
                    specialReview.setSpecialReviewApprovalTypeDesc(specialReviewBean.getApprovalDescription()) ;
                    
                    specialReview.setSpecialReviewComments(specialReviewBean.getComments()) ;
                    try
                    {
                        specialReview.setSpecialReviewNumber(new BigInteger(String.valueOf(specialReviewBean.getSpecialReviewNumber()))) ;
                    }
                    catch(Exception srn)
                    {
                         // do nothing so that this tag doesnt get added to the xml generated
                        System.out.println("** addSpecialReview : getSpecialReviewNumber is null **") ;
                        
                    }
                    specialReview.setSpecialReviewProtocolNumber(specialReviewBean.getProtocolNumber()) ;
                    try
                    {
                        specialReview.setSpecialReviewTypeCode(new BigInteger(String.valueOf(specialReviewBean.getSpecialReviewCode()))) ; 
                    }
                    catch(Exception srtc)
                    {
                         // do nothing so that this tag doesnt get added to the xml generated
                        System.out.println("** addSpecialReview : getSpecialReviewCode is null **") ;
                    }

                    if (specialReviewBean.getSpecialReviewDescription() == null) System.out.println("** addSpecialReview : getSpecialReviewDescription() is null **") ;
                    specialReview.setSpecialReviewTypeDesc(specialReviewBean.getSpecialReviewDescription()) ;
                                                            
                    protocol.getSpecialReview().add(specialReview) ;
                } //end for   
            
            } // end if vecSpecialReview    
    }
    
    // get a particular submission detail for a protocol
    private void addSubmissionDetails(ProtocolInfoBean protocolInfoBean, int submissionNumber, PersonStream personStream, String currentFlag) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
          // get submission details for this protocol
        System.out.println("** submissionNumber is " + submissionNumber +" **") ;
        
        ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean() ;
        ProtocolSubmissionInfoBean submissionInfoBean = 
                                   submissionTxnBean.getSubmissionForSubmissionNumber(protocolInfoBean.getProtocolNumber(), submissionNumber ) ;
        //added by ele 1/13/04
          XMLGeneratorTxnBean xmlGeneratorTxnBean = new XMLGeneratorTxnBean() ;
         
           if (protocolInfoBean.getProtocolNumber() == null)
           {
            System.out.println("** addSubmissionDetails : getProtocolNumber() is null **") ;
           }    
                ProtocolType.SubmissionsType submission = objFactory.createProtocolTypeSubmissionsType() ;                            
                SubmissionDetailsType submissionDetail = objFactory.createSubmissionDetailsType() ;
                
                try
                {
                    submissionDetail.setAbstainerCount(new BigInteger(String.valueOf(submissionInfoBean.getAbstainerCount()))) ;
                }
                catch(Exception ac)
                {
                         // do nothing so that this tag doesnt get added to the xml generated
                }
                try
                {
                submissionDetail.setNoVote(new BigInteger(String.valueOf(submissionInfoBean.getNoVoteCount()))) ;
                }
                catch(Exception nv)
                {
                         // do nothing so that this tag doesnt get added to the xml generated
                }
                
                if (submissionInfoBean.getProtocolNumber() == null) System.out.println("** addSubmissionDetails : getProtocolNumber() is null **") ;
                submissionDetail.setProtocolNumber(submissionInfoBean.getProtocolNumber()) ;
                try
                {
                    submissionDetail.setProtocolReviewTypeCode(new BigInteger(String.valueOf(submissionInfoBean.getProtocolReviewTypeCode()))) ;
                }
                catch(Exception prtc)
                {
                         // do nothing so that this tag doesnt get added to the xml generated
                    System.out.println("** addSubmissionDetails : getProtocolReviewTypeCode() is null **") ;
                }
                
                if (submissionInfoBean.getProtocolReviewTypeDesc() == null) System.out.println("** addSubmissionDetails : getProtocolReviewTypeDesc()() is null **") ;
                submissionDetail.setProtocolReviewTypeDesc(submissionInfoBean.getProtocolReviewTypeDesc()) ;
                //start jenlu change 11/21/05
                // get the reviewers for this protocol. the vector is a vector of protocolReviewerInfoBeans         
                Vector vecReviewers = submissionTxnBean.getProtocolReviewers(submissionInfoBean.getProtocolNumber(),
                submissionInfoBean.getSequenceNumber(), submissionInfoBean.getSubmissionNumber() ) ;
                   
                if (vecReviewers != null)
                {    
                    for (int vecCount = 0 ; vecCount < vecReviewers.size() ; vecCount++)
                    {
                        ProtocolReviewerInfoBean protocolReviewerInfoBean = (ProtocolReviewerInfoBean) vecReviewers.get(vecCount) ;
                        submissionDetail.getProtocolReviewer().add(personStream.getReviewer(protocolReviewerInfoBean));
                    }
                }    
                

                    
                //end jenlu change 11/21/05
                
                submissionDetail.setSubmissionComments(submissionInfoBean.getComments()) ;
                
                java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
                if (submissionInfoBean.getSubmissionDate() != null)
                {    
                    submissionDetail.setSubmissionDate(convertDateStringToCalendar(submissionInfoBean.getSubmissionDate().toString())) ;
                }
                else
                {
                    System.out.println("** addSubmissionDetails : getSubmissionDate() is null **") ;
                    submissionDetail.setSubmissionDate(calDate) ;
                }    
                
                try
                {
                    submissionDetail.setSubmissionNumber(new BigInteger(String.valueOf(submissionInfoBean.getSubmissionNumber()))) ;
                }
                catch(Exception sn)
                {
                         // do nothing so that this tag doesnt get added to the xml generated
                    System.out.println("** addSubmissionDetails : getSubmissionNumber() is null **") ;
                }
                try
                {
                    submissionDetail.setSubmissionStatusCode(new BigInteger(String.valueOf(submissionInfoBean.getSubmissionStatusCode()))) ;
                }
                catch(Exception stc)
                {
                         // do nothing so that this tag doesnt get added to the xml generated
                         System.out.println("** addSubmissionDetails : getProtocolNumber() is null **") ;
                }
                
                if (submissionInfoBean.getSubmissionStatusDesc() == null) System.out.println("** addSubmissionDetails : getSubmissionStatusDesc() is null **") ;
                submissionDetail.setSubmissionStatusDesc(submissionInfoBean.getSubmissionStatusDesc()) ;
                
                try
                {
                    submissionDetail.setSubmissionTypeCode(new BigInteger(String.valueOf(submissionInfoBean.getSubmissionTypeCode()))) ;
                }
                catch(Exception stc)
                {
                         // do nothing so that this tag doesnt get added to the xml generated
                    System.out.println("** addSubmissionDetails : getSubmissionTypeCode() is null **") ;
                }
                
                if (submissionInfoBean.getSubmissionTypeDesc() == null) System.out.println("** addSubmissionDetails : getSubmissionTypeDesc() is null **") ;
                submissionDetail.setSubmissionTypeDesc(submissionInfoBean.getSubmissionTypeDesc()) ;
                
                try
                {
                    submissionDetail.setSubmissionTypeQualifierCode(new BigInteger(String.valueOf(submissionInfoBean.getSubmissionQualTypeCode()))) ;
                }
                catch(Exception stqc)
                {
                    // do nothing so that this tag doesnt get added to the xml generated
                }
                    
                //start eleanor change 1/13/04  
        //        submissionDetail.setSubmissionTypeQualifierDesc(submissionInfoBean.getSubmissionQualTypeDesc()) ;
        //        submissionDetail.setVotingComments(submissionInfoBean.getVotingComments()) ;
                 
                xmlGeneratorTxnBean = new XMLGeneratorTxnBean() ;
                String votingComments =  xmlGeneratorTxnBean.getVotingComments(submissionInfoBean.getProtocolNumber(),submissionInfoBean.getScheduleId());
                submissionDetail.setVotingComments(votingComments) ;
       
               //end eleanor change

                    
                //eleanor start - sept-10-04 - add the action info
                SubmissionDetailsType.ActionTypeType actionTypeInfo = objFactory.createSubmissionDetailsTypeActionTypeType();
                actionTypeInfo.setActionId(new BigInteger(String.valueOf(submissionInfoBean.getActionId())));
                actionTypeInfo.setActionTypeCode(new BigInteger(String.valueOf(submissionInfoBean.getActionTypeCode())));                 
                actionTypeInfo.setActionTypeDescription(submissionInfoBean.getActionTypeDesc());  
                actionTypeInfo.setActionDate(convertDateStringToCalendar(submissionInfoBean.getActionDate().toString()));
                actionTypeInfo.setActionComments(submissionInfoBean.getActionComments());
                  
                try
                {
                     submissionDetail.setActionType(actionTypeInfo);
                }
                catch (Exception at)
                {
                  // do nothing so that this tag doesnt get added to the xml generated
                }
                //eleanor end - sept-10-04
          
                try
                {
                    submissionDetail.setYesVote(new BigInteger(String.valueOf(submissionInfoBean.getYesVoteCount()))) ;
                }
                catch(Exception yv)
                {
                         // do nothing so that this tag doesnt get added to the xml generated
                } 
    
                //prps start  - feb 13 2004
                // add the checklist info
                SubmissionCheckListStream submissionCheckListStream = new SubmissionCheckListStream (objFactory) ;
                SubmissionDetailsType.SubmissionChecklistInfoType submissionChecklistInfo  
                                =  submissionCheckListStream.getSubmissionCheckList(submissionInfoBean.getProtocolNumber(), 
                                                                                submissionInfoBean.getSequenceNumber(), submissionInfoBean.getSubmissionNumber()) ;
                 if (submissionChecklistInfo != null)
                 {    
                    submissionDetail.setSubmissionChecklistInfo(submissionChecklistInfo) ;
                 }   
                
                   
                submission.setCurrentSubmissionFlag(currentFlag) ;
                //prps end - feb 13 2004
                
                submission.setSubmissionDetails(submissionDetail) ;
                
                // add Minutes to protocol
                   ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean() ;
//                   Vector  vecMinutes =  scheduleMaintenanceTxnBean.getMinutes(submissionInfoBean.getScheduleId()) ;
                   Vector  vecMinutes =  scheduleMaintenanceTxnBean.getMinutesForSubmission(submissionInfoBean.getProtocolNumber(), submissionInfoBean.getSubmissionNumber());
                   if (vecMinutes != null) {
                       
                       MinutesStream minutesStream = new MinutesStream(objFactory);
                       // Modified for COEUSQA-3046 : An incomplete review comment should not appear in the correspondence to the PI - Start
                       // Only completed reviewer's review comments will be printed.
//                       Vector vecMinuteStream = minutesStream.getProtocolMinutes(vecMinutes, submissionInfoBean.getScheduleId(),submissionInfoBean.getProtocolNumber(), submissionInfoBean.getSubmissionNumber()) ;
                       Vector vecReviewComments = new Vector();
                       CoeusVector cvReviewComments = new CoeusVector();
                       Vector vecNonCompleteReviewComm = new Vector();
                       if(vecReviewers != null && !vecReviewers.isEmpty()){
                           for(Object reviewerDetails : vecReviewers){
                               ProtocolReviewerInfoBean reviewerInfomation = (ProtocolReviewerInfoBean)reviewerDetails;
                               String reviewerPersonId = reviewerInfomation.getPersonId();
                               if(reviewerInfomation.isReviewComplete() && !vecMinutes.isEmpty()){
                                   for(Object minutesDetails : vecMinutes){
                                       MinuteEntryInfoBean minuteEntryDetails = (MinuteEntryInfoBean)minutesDetails;
                                       String creatorPersonId = minuteEntryDetails.getPersonId();
                                       // Checks the reviewer personId and review comments personId(personid will be comments creator id) are same
                                       if(reviewerPersonId.equals(creatorPersonId)){
                                           vecReviewComments.add(minuteEntryDetails);
                                       } 
                                   }
                              // Added for COEUSQA-3194 : bug with XML/XSD changes with review comments  - Start
                               }else if(!vecMinutes.isEmpty()){
                                   for(Object minutesDetails : vecMinutes){
                                       MinuteEntryInfoBean minuteEntryDetails = (MinuteEntryInfoBean)minutesDetails;
                                       String creatorPersonId = minuteEntryDetails.getPersonId();
                                       // Checks the reviewer personId and review comments personId(personid will be comments creator id) are same
                                       // Checks whether the comments added by admin(Minute Entry Type Code == 3)
                                        if(reviewerPersonId.equals(creatorPersonId) && minuteEntryDetails.getMinuteEntryTypeCode() != 3){                                       
                                           vecNonCompleteReviewComm.add(minuteEntryDetails);
                                       }
                                   }
                               }
                               // Removes all the comments in the original collection for which the review is not completed
                               vecMinutes.removeAll(vecNonCompleteReviewComm);
                               // Added for COEUSQA-3194 : bug with XML/XSD changes with review comments  - END
                           }
                           
                       }

                        // Modified for COEUSQA-3194 : bug with XML/XSD changes with review comments  - Start
                       // Vector will have only the Reviewer complete comments and admin comments(MinuteEntryCode = 3)
//                       Vector vecMinuteStream = minutesStream.getProtocolMinutes(vecReviewComments,
//                               submissionInfoBean.getScheduleId(),
//                               submissionInfoBean.getProtocolNumber(),
//                               submissionInfoBean.getSubmissionNumber());
                       Vector vecMinuteStream = minutesStream.getProtocolMinutes(vecMinutes,
                               submissionInfoBean.getScheduleId(),
                               submissionInfoBean.getProtocolNumber(),
                               submissionInfoBean.getSubmissionNumber());
                        // Modified for COEUSQA-3194 : bug with XML/XSD changes with review comments  - END
                       
                       // Modified for COEUSQA-3046 : An incomplete review comment should not appear in the correspondence to the PI - End
                       if (vecMinuteStream.size() >0)
                       {
                            submission.getMinutes().addAll(vecMinuteStream) ;
                       }
                    }// end if vecMinutes
                
                
                // add committee details
                if (submissionInfoBean.getCommitteeId() == null || submissionInfoBean.getCommitteeId().equals("") ) 
                {
                    System.out.println("** addSubmissionDetails : CommitteeId is null (optional) **") ;
                }
                else
                {    // add committee details
                      String committeeId = submissionInfoBean.getCommitteeId() ;
                      CommitteeStream committeeStream = new CommitteeStream(objFactory) ;
                      CommitteeMasterDataType committeeMasterData = committeeStream.getCommitteeMasterData(committeeId) ;
                      submission.setCommitteeMasterData(committeeMasterData) ;

                      // add committee member details 
                      MembershipTxnBean membersTxnBean = new MembershipTxnBean() ;
                      Vector vecMembers = membersTxnBean.getMembershipListCurrent(committeeId) ;
                      if (vecMembers!= null)
                      {
                        for (int memCount=0 ; memCount< vecMembers.size() ; memCount++)
                        {
                            CommitteeMembershipDetailsBean membershipBean = 
                                                (CommitteeMembershipDetailsBean) vecMembers.get(memCount) ;
                           // add committeeMember
                            submission.getCommitteeMember().add(committeeStream.getCommitteeMember(membershipBean)) ;
                        } // end for vecMembers
                      } // end if    
                }    
                
                // add Schedule details
                if (submissionInfoBean.getScheduleId() == null || submissionInfoBean.getScheduleId().equals(""))
                {
                    System.out.println("** addSubmissionDetails : ScheduleId is null **") ;
                }
                else
                {    
                    // set current schedule details
                    String currScheduleId = submissionInfoBean.getScheduleId() ;
                    String committeeId = submissionInfoBean.getCommitteeId() ;
                    
                    ScheduleStream scheduleStream = new ScheduleStream(objFactory) ;
                    ScheduleMasterDataType currentSchedule = scheduleStream.getScheduleMasterData(currScheduleId) ;
                    submission.setScheduleMasterData(currentSchedule) ;

                     // check for previous and next schedule
                    //ele change 1/13 - declare bean above
        //             XMLGeneratorTxnBean xmlGeneratorTxnBean = new XMLGeneratorTxnBean() ;
                    HashMap hashSchedule = xmlGeneratorTxnBean.getPreviousAndNextSchedule(committeeId, currScheduleId) ;
                    
                    if (hashSchedule.get("PREVIOUS_ID")!= null)
                    {
                        String previousId = hashSchedule.get("PREVIOUS_ID").toString() ;
                        ScheduleMasterDataType prevSchedule = scheduleStream.getScheduleMasterData(previousId) ;
                        ProtocolType.SubmissionsType.PrevScheduleType prevScheduleType = objFactory.createProtocolTypeSubmissionsTypePrevScheduleType() ;
                        prevScheduleType.setScheduleMasterData(prevSchedule) ;
                        
                        submission.setPrevSchedule(prevScheduleType) ;
                    }
                    
                    if (hashSchedule.get("NEXT_ID")!= null)
                    {
                        String nextId = hashSchedule.get("NEXT_ID").toString() ;
                        ScheduleMasterDataType nextSchedule = scheduleStream.getScheduleMasterData(nextId) ;
                        ProtocolType.SubmissionsType.NextScheduleType nextScheduleType = objFactory.createProtocolTypeSubmissionsTypeNextScheduleType() ;
                        nextScheduleType.setScheduleMasterData(nextSchedule) ;
                        
                        submission.setNextSchedule(nextScheduleType) ;
                    }
                    
                }    
                
                
                protocol.getSubmissions().add(submission) ;
              
              
    
    }
    
    // changed this method for case 1474. Instead of getting all submissions for a protocol, if there is
    // no current submission, then just get the last submission
    private void addSubmissionDetails(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
          // get last submission details for this protocol   
        
           SubmissionDetailsTxnBean submissionDetailsTxnBean = new SubmissionDetailsTxnBean() ;
           if (protocolInfoBean.getProtocolNumber() == null)
           {
            System.out.println("** addSubmissionDetails : getProtocolNumber() is null **") ;
           }    
           Vector vecSubmissionDetails = submissionDetailsTxnBean.getDataSubmissionDetails(protocolInfoBean.getProtocolNumber()) ;
           if (vecSubmissionDetails != null && vecSubmissionDetails.size() > 0  )
           {
             int lastSub = vecSubmissionDetails.size() - 1;
              
             ProtocolSubmissionInfoBean lastSubmissionInfoBean =
                            (ProtocolSubmissionInfoBean) vecSubmissionDetails.get(lastSub) ;
                   
       //    for (int vecCount=0 ; vecCount < vecSubmissionDetails.size() ; vecCount++  )
       //    {
             ProtocolSubmissionInfoBean submissionInfoBean =
                         (ProtocolSubmissionInfoBean) vecSubmissionDetails.get(lastSub) ;
       //                                  (ProtocolSubmissionInfoBean) vecSubmissionDetails.get(vecCount) ;
                
             ProtocolType.SubmissionsType submission = objFactory.createProtocolTypeSubmissionsType() ;                            
             SubmissionDetailsType submissionDetail = objFactory.createSubmissionDetailsType() ;
                
              try
                {
                submissionDetail.setAbstainerCount(new BigInteger(String.valueOf(submissionInfoBean.getAbstainerCount()))) ;
                }
              catch(Exception ac)
                {
                // do nothing so that this tag doesnt get added to the xml generated
                }
              try
                {
                submissionDetail.setNoVote(new BigInteger(String.valueOf(submissionInfoBean.getNoVoteCount()))) ;
                }
              catch(Exception nv)
                {
                         // do nothing so that this tag doesnt get added to the xml generated
                }
                
              if (submissionInfoBean.getProtocolNumber() == null) System.out.println("** addSubmissionDetails : getProtocolNumber() is null **") ;
              submissionDetail.setProtocolNumber(submissionInfoBean.getProtocolNumber()) ;              
              
              try
                {
                   submissionDetail.setProtocolReviewTypeCode(new BigInteger(String.valueOf(submissionInfoBean.getProtocolReviewTypeCode()))) ;
                }
              catch(Exception prtc)
                {
                    // do nothing so that this tag doesnt get added to the xml generated
                    System.out.println("** addSubmissionDetails : getProtocolReviewTypeCode() is null **") ;
                }
                
              if (submissionInfoBean.getProtocolReviewTypeDesc() == null) System.out.println("** addSubmissionDetails : getProtocolReviewTypeDesc()() is null **") ;
              submissionDetail.setProtocolReviewTypeDesc(submissionInfoBean.getProtocolReviewTypeDesc()) ;              
              //start jenlu change 11/21/05
              ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean() ; 
                  // get the reviewers for this protocol. the vector is a vector of protocolReviewerInfoBeans         
                    Vector vecReviewers = protocolSubmissionTxnBean.getProtocolReviewers(submissionInfoBean.getProtocolNumber(),
                    submissionInfoBean.getSequenceNumber(), submissionInfoBean.getSubmissionNumber() ) ;                    
                    if (vecReviewers != null)
                    {    
                           for (int vecCount = 0 ; vecCount < vecReviewers.size() ; vecCount++)
                           {
//                            ProtocolReviewerType protocolReviewer = objFactory.createProtocolReviewerType();
                                ProtocolReviewerInfoBean protocolReviewerInfoBean = (ProtocolReviewerInfoBean) vecReviewers.get(vecCount) ;
                                submissionDetail.getProtocolReviewer().add(personStream.getReviewer(protocolReviewerInfoBean));
                           }   
                    }
              //end jenlu change 11/21/05
              submissionDetail.setSubmissionComments(submissionInfoBean.getComments()) ;
                
              java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
              if (submissionInfoBean.getSubmissionDate() != null)
               {    
                  submissionDetail.setSubmissionDate(convertDateStringToCalendar(submissionInfoBean.getSubmissionDate().toString())) ;
               }
              else
               {
                 System.out.println("** addSubmissionDetails : getSubmissionDate() is null **") ;
                 submissionDetail.setSubmissionDate(calDate) ;
               }    
                
              try
                {
                  submissionDetail.setSubmissionNumber(new BigInteger(String.valueOf(submissionInfoBean.getSubmissionNumber()))) ;
                }
              catch(Exception sn)
                {
                     // do nothing so that this tag doesnt get added to the xml generated
                    System.out.println("** addSubmissionDetails : getSubmissionNumber() is null **") ;
                }
              try
                {
                   submissionDetail.setSubmissionStatusCode(new BigInteger(String.valueOf(submissionInfoBean.getSubmissionStatusCode()))) ;
                }
              catch(Exception stc)
                {
                    // do nothing so that this tag doesnt get added to the xml generated
                    System.out.println("** addSubmissionDetails : getProtocolNumber() is null **") ;
                }
                
              if (submissionInfoBean.getSubmissionStatusDesc() == null) System.out.println("** addSubmissionDetails : getSubmissionStatusDesc() is null **") ;
              submissionDetail.setSubmissionStatusDesc(submissionInfoBean.getSubmissionStatusDesc()) ;
                
              try
                {
                  submissionDetail.setSubmissionTypeCode(new BigInteger(String.valueOf(submissionInfoBean.getSubmissionTypeCode()))) ;
                }
              catch(Exception stc)
                {
                  // do nothing so that this tag doesnt get added to the xml generated
                  System.out.println("** addSubmissionDetails : getSubmissionTypeCode() is null **") ;
                }
                
              if (submissionInfoBean.getSubmissionTypeDesc() == null) System.out.println("** addSubmissionDetails : getSubmissionTypeDesc() is null **") ;
              submissionDetail.setSubmissionTypeDesc(submissionInfoBean.getSubmissionTypeDesc()) ;
                
              try
               {
                submissionDetail.setSubmissionTypeQualifierCode(new BigInteger(String.valueOf(submissionInfoBean.getSubmissionQualTypeCode()))) ;
               }
              catch(Exception stqc)
               {
                   // do nothing so that this tag doesnt get added to the xml generated
               }
                    
              submissionDetail.setSubmissionTypeQualifierDesc(submissionInfoBean.getSubmissionQualTypeDesc()) ;
                
              submissionDetail.setVotingComments(submissionInfoBean.getVotingComments()) ;
                
              try
                {
                  submissionDetail.setYesVote(new BigInteger(String.valueOf(submissionInfoBean.getYesVoteCount()))) ;
                }
              catch(Exception yv)
                {
                  // do nothing so that this tag doesnt get added to the xml generated
                } 
                
                              
                //eleanor start - sept-10-04 - add the action info
                SubmissionDetailsType.ActionTypeType actionTypeInfo = objFactory.createSubmissionDetailsTypeActionTypeType();
                actionTypeInfo.setActionId(new BigInteger(String.valueOf(submissionInfoBean.getActionId())));
                actionTypeInfo.setActionTypeCode(new BigInteger(String.valueOf(submissionInfoBean.getActionTypeCode())));                 
                actionTypeInfo.setActionTypeDescription(submissionInfoBean.getActionTypeDesc());  
                actionTypeInfo.setActionDate(convertDateStringToCalendar(submissionInfoBean.getActionDate().toString()));
                actionTypeInfo.setActionComments(submissionInfoBean.getActionComments());
                  
                try
                {
                     submissionDetail.setActionType(actionTypeInfo);
                }
                catch (Exception at)
                {
                  // do nothing so that this tag doesnt get added to the xml generated
                }
                //eleanor end - sept-10-04
                    
                //prps start  - feb 17 2004
                // add the checklist info
                SubmissionCheckListStream submissionCheckListStream = new SubmissionCheckListStream (objFactory) ;
                SubmissionDetailsType.SubmissionChecklistInfoType submissionChecklistInfo  
                                =  submissionCheckListStream.getSubmissionCheckList(submissionInfoBean.getProtocolNumber(), 
                                                                                submissionInfoBean.getSequenceNumber(), submissionInfoBean.getSubmissionNumber()) ;
                 if (submissionChecklistInfo != null)
                 {    
                    submissionDetail.setSubmissionChecklistInfo(submissionChecklistInfo) ;
                 }   
                
                submission.setCurrentSubmissionFlag("No") ; 
                //prps end - feb 17 2004
               
    
                submission.setSubmissionDetails(submissionDetail) ;
                
                // add Minutes to protocol
                   ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean() ;
                   Vector  vecMinutes =  scheduleMaintenanceTxnBean.getMinutes(submissionInfoBean.getScheduleId()) ;
                   if (vecMinutes != null)
                   {
                        MinutesStream minutesStream = new MinutesStream(objFactory) ;
                        for (int minLoop = 0 ; minLoop < vecMinutes.size() ; minLoop++ )
                        {
                           MinuteEntryInfoBean minuteEntryInfoBean = (MinuteEntryInfoBean) vecMinutes.get(minLoop) ;
                           // the minute entries should be of type ProtocolType  
                           if (minuteEntryInfoBean.getProtocolNumber() != null)
                           {
                           if (minuteEntryInfoBean.getProtocolNumber().equals(protocolInfoBean.getProtocolNumber()))
                           { 
                               // add each minute to protocolSubmission
                               submission.getMinutes().add(minutesStream.getMinute(minuteEntryInfoBean, submissionInfoBean.getScheduleId())) ;
                            }// end if
                          } // end if
                        } // end for
                    }// end if vecMinutes

                
                
                // add committee details
                if (submissionInfoBean.getCommitteeId() == null || submissionInfoBean.getCommitteeId().equals("") )
                {
                    System.out.println("** addSubmissionDetails : CommitteeId is null (optional) **") ;
                }    
                else
                {    // add committee details
                      String committeeId = submissionInfoBean.getCommitteeId() ;
                      CommitteeStream committeeStream = new CommitteeStream(objFactory) ;
                      CommitteeMasterDataType committeeMasterData = committeeStream.getCommitteeMasterData(committeeId) ;
                      submission.setCommitteeMasterData(committeeMasterData) ;

                      // add committee member details 
                      MembershipTxnBean membersTxnBean = new MembershipTxnBean() ;
                      Vector vecMembers = membersTxnBean.getMembershipListCurrent(committeeId) ;
                      if (vecMembers!= null)
                      {
                        for (int memCount=0 ; memCount< vecMembers.size() ; memCount++)
                        {
                            CommitteeMembershipDetailsBean membershipBean = 
                                                (CommitteeMembershipDetailsBean) vecMembers.get(memCount) ;
                           // add committeeMember
                            submission.getCommitteeMember().add(committeeStream.getCommitteeMember(membershipBean)) ;
                        } // end for vecMembers
                      } // end if    
                }   
                
                // add Schedule details
                if (submissionInfoBean.getScheduleId() == null || submissionInfoBean.getScheduleId().equals("") )
                {
                    System.out.println("** addSubmissionDetails : ScheduleId is null (Optional)**") ;
                }
                else
                {    
                    // set current schedule details
                    String currScheduleId = submissionInfoBean.getScheduleId() ;
                    String committeeId = submissionInfoBean.getCommitteeId() ;
                    
                    ScheduleStream scheduleStream = new ScheduleStream(objFactory) ;
                    ScheduleMasterDataType currentSchedule = scheduleStream.getScheduleMasterData(currScheduleId) ;
                    submission.setScheduleMasterData(currentSchedule) ;

                     // check for previous and next schedule
                  
                    XMLGeneratorTxnBean xmlGeneratorTxnBean = new XMLGeneratorTxnBean() ;
                    HashMap hashSchedule = xmlGeneratorTxnBean.getPreviousAndNextSchedule(committeeId, currScheduleId) ;
                    
                    if (hashSchedule.get("PREVIOUS_ID")!= null)
                    {
                        String previousId = hashSchedule.get("PREVIOUS_ID").toString() ;
                        ScheduleMasterDataType prevSchedule = scheduleStream.getScheduleMasterData(previousId) ;
                        ProtocolType.SubmissionsType.PrevScheduleType prevScheduleType = objFactory.createProtocolTypeSubmissionsTypePrevScheduleType() ;
                        prevScheduleType.setScheduleMasterData(prevSchedule) ;
                        
                        submission.setPrevSchedule(prevScheduleType) ;
                    }
                    
                    if (hashSchedule.get("NEXT_ID")!= null)
                    {
                        String nextId = hashSchedule.get("NEXT_ID").toString() ;
                        ScheduleMasterDataType nextSchedule = scheduleStream.getScheduleMasterData(nextId) ;
                        ProtocolType.SubmissionsType.NextScheduleType nextScheduleType = objFactory.createProtocolTypeSubmissionsTypeNextScheduleType() ;
                        nextScheduleType.setScheduleMasterData(nextSchedule) ;
                        
                        submission.setNextSchedule(nextScheduleType) ;
                    }
                }
                
                protocol.getSubmissions().add(submission) ;
      //        }// end for 
                
           }// end if vecSubmissionDetails    
    
    }
    
    
    
    
    public Calendar convertDateStringToCalendar(String dateStr)
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
    //Added for Case 2176 - Risk level category - Start
     /**
     * Method used to get list of all risk level category
     * <li>To fetch the data, it uses 'GET_PROTOCOL_RISK_LEVELS'
     *
     * @param protocolInfoBean
     * @param personStream
     * @return void
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException Coeus Exception
     * @exception CoeusException JAXB Exception
     */
    private void addRiskLevels(ProtocolInfoBean protocolInfoBean, PersonStream personStream) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        
         int fundingSourceTypeCode;
         String fundingSourceName, fundingSourceCode;
         ProtocolRiskLevelBean protocolRiskLevelBean = null;
                  
         CoeusVector cvRiskLevels = protocolDataTxnBean.getProtocolRiskLevels(protocolInfoBean.getProtocolNumber());
         Vector vecRiskCodes =  protocolDataTxnBean.getRiskLevels();
         HashMap hmRiskCodes = new HashMap();
         if(vecRiskCodes !=null && vecRiskCodes.size() > 0){
             for(int index = 0; index < vecRiskCodes.size() ; index++){
                 ComboBoxBean comboxBoxBean = (ComboBoxBean) vecRiskCodes.get(index);
                 hmRiskCodes.put(comboxBoxBean.getCode(),comboxBoxBean.getDescription());
             }
             
         }
         
         if(cvRiskLevels !=null && cvRiskLevels.size() > 0){
             for(int index =0; index < cvRiskLevels.size(); index++ ){
                 RiskLevelsType rishLevelsType = objFactory.createRiskLevelsType();
                 protocolRiskLevelBean = (ProtocolRiskLevelBean) cvRiskLevels.get(index);
                 if (protocolRiskLevelBean.getRiskLevelCode() !=null && hmRiskCodes != null){
                     rishLevelsType.setRiskLevelCode(new BigInteger(protocolRiskLevelBean.getRiskLevelCode()));
                     rishLevelsType.setRiskLevelDescription(hmRiskCodes.get(protocolRiskLevelBean.getRiskLevelCode()).toString());
                 }
                
                 
                 
                 if(protocolRiskLevelBean.getComments() !=null){
                     rishLevelsType.setComments(protocolRiskLevelBean.getComments());
                 }
                 
                 //Set DateAssigned
                 Calendar dateAssigned = null;
                 if (protocolRiskLevelBean.getDateAssigned()!=null) {
                     dateAssigned = Calendar.getInstance();
                     dateAssigned.setTime(protocolRiskLevelBean.getDateAssigned());
                     rishLevelsType.setDateAssigned(dateAssigned);
                 }
                 //Set DateUpdated
                 Calendar dateUpdated = null;
                 if (protocolRiskLevelBean.getDateUpdated()!=null) {
                     dateUpdated = Calendar.getInstance();
                     dateUpdated.setTime(protocolRiskLevelBean.getDateUpdated());
                     rishLevelsType.setDateUpdated(dateUpdated);
                 }
                 // Status
                if( protocolRiskLevelBean.getStatus() !=null){
                     if(protocolRiskLevelBean.getStatus().equalsIgnoreCase("A")){
                        rishLevelsType.setStatus("Active");
                     }else if(protocolRiskLevelBean.getStatus().equalsIgnoreCase("I")){
                        rishLevelsType.setStatus("Inactive");
                     }
                     
                }
                  // UpdateUser
                if( protocolRiskLevelBean.getUpdateUser() !=null){
                     rishLevelsType.setUpdateUser(protocolRiskLevelBean.getUpdateUser());
                }
                                
                 //Set UpdateTimestamp
                 Calendar updateTimeStamp = null;
                 if (protocolRiskLevelBean.getUpdateTimestamp()!=null) {
                     updateTimeStamp = Calendar.getInstance();
                     updateTimeStamp.setTime(protocolRiskLevelBean.getUpdateTimestamp());
                     rishLevelsType.setUpdateTimestamp(updateTimeStamp);
                 }
                 protocol.getRiskLevels().add(rishLevelsType);
                   
               }               
           }           
    }    
    //Added for Case 2176 - Risk level category - End
}
