/*
 * ProjectDescriptionStream.java
 * Created on Mar 12,2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

import java.util.Vector;
import edu.mit.coeus.propdev.bean.*;
   
      
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.*;
import java.math.BigInteger;

public class ProjectDescriptionStream
{
    ObjectFactory objFactory;
    edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory;
    
    ProjectDescriptionType projectDescription ;
     
    /** Creates a new instance of ProjectDescriptionStream */
    public ProjectDescriptionStream(ObjectFactory objFactory,
                edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory)
    {  
        this.objFactory = objFactory ;
        this.rarObjFactory = rarObjFactory;
        
    } 
    
    public ProjectDescriptionType getProjectDescription(ProposalDevelopmentFormBean proposalBean ,
                               OrganizationMaintenanceFormBean orgBean)
           throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
       
         projectDescription = objFactory.createProjectDescription();
                         // always use Createxxx not createxxxType method - createxxxType
                         // method will not add <xxx> tag to the o/p     
                
                  
         /**human subject
          */
         HumanSubjectsTypeStream humanSubjStream = new HumanSubjectsTypeStream(objFactory);
         projectDescription.setHumanSubject(humanSubjStream.getHumanSubjInfo(proposalBean,orgBean));
      
         /**animal subject
          */
         AnimalSubjectTypeStream animalSubjStream = new AnimalSubjectTypeStream(rarObjFactory);
         projectDescription.setAnimalSubject(animalSubjStream.getAnimalSubjInfo(proposalBean,orgBean));
       
         /**project survey
          */
         ProjectSurveyStream projectSurveyStream = new ProjectSurveyStream(rarObjFactory);
         projectDescription.setProjectSurvey(projectSurveyStream.getProjectSurvey(proposalBean));
           
         /**project summary
          */
        
         DescriptionBlockTypeStream descriptionBlockTypeStream = new DescriptionBlockTypeStream(rarObjFactory);
         projectDescription.setProjectSummary
           (descriptionBlockTypeStream.getDescriptionBlock(proposalBean,"summary",true));
       
         /** facilities
          */
         projectDescription.setFacilitiesDescription
            (descriptionBlockTypeStream.getDescriptionBlock(proposalBean, "facilities",false));
         
         /** equipment
          */
         projectDescription.setEquipmentDescription
            (descriptionBlockTypeStream.getDescriptionBlock(proposalBean, "equipment",false));
           
         /**references
          */
         projectDescription.setReferences
            (descriptionBlockTypeStream.getDescriptionBlock(proposalBean, "references",true));
       
         /** activity type
          */
        ProjectDescriptionType.ActivityTypeType activityType =
           objFactory.createProjectDescriptionTypeActivityTypeType();
        activityType.setActivityTypeDesc(proposalBean.getProposalActivityTypeDesc());
        activityType.setActivityTypeCode(new BigInteger( Integer.toString(proposalBean.getProposalActivityTypeCode())));
        projectDescription.setActivityType(activityType);
        
         return projectDescription;
            
    }
}
