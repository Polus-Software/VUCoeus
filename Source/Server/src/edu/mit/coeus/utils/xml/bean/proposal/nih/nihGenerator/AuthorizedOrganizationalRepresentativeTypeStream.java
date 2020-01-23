/*
 * AuthorizedOrganizationalRepresentativeTypeStream.java
 *
 * Created on March 2, 2004, 5:46 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

/**
 *
 * @author  ele
 */

import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.xml.bean.proposal.common.commonGenerator.ContactInfoTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.PersonFullNameTypeStream;

public class AuthorizedOrganizationalRepresentativeTypeStream {
    ObjectFactory objFactory ;
    edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory;
    edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory;
    
    AuthorizedOrganizationalRepresentativeType authOrgRepType;
 
    /** Creates a new instance of AuthorizedOrganizationalRepresentativeTypeStream */
    public AuthorizedOrganizationalRepresentativeTypeStream(ObjectFactory objFactory,
                edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory,
                 edu.mit.coeus.utils.xml.bean.proposal.common.ObjectFactory commonObjFactory)  
    {
        this.objFactory = objFactory ;  
        this.commonObjFactory = commonObjFactory;
        this.rarObjFactory = rarObjFactory;
    }
    
    
    public AuthorizedOrganizationalRepresentativeType getAuthRepInfo(RolodexDetailsBean  rolodexBean,
                    DepartmentPersonFormBean authRepPersonBean) 
         throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
         authOrgRepType = objFactory.createAuthorizedOrganizationalRepresentativeType();
 
         authOrgRepType.setPositionTitle(authRepPersonBean.getPrimaryTitle() );
         
         PersonFullNameTypeStream personFullNameTypeStream
            = new PersonFullNameTypeStream(rarObjFactory);
         authOrgRepType.setName(personFullNameTypeStream.getPersonFullNameTypeInfo(authRepPersonBean));
    
         ContactInfoTypeStream contactInfoTypeStream
            = new ContactInfoTypeStream(commonObjFactory);
        
         authOrgRepType.setContactInformation(contactInfoTypeStream.getContactInfo(authRepPersonBean,
                                                                       rolodexBean));
        
         SignatureTypeStream signatureTypeStream
            = new SignatureTypeStream(objFactory);
      
         authOrgRepType.setOrganizationalOfficialSignature(signatureTypeStream.getSignatureInfo(rolodexBean));
         
         return authOrgRepType;
    }
    
}
