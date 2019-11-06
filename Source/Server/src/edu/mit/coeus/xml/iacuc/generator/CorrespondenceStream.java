/*
 * CorrespondenceStream.java
 *
 * Created on November 24, 2003, 4:28 PM
 */

package edu.mit.coeus.xml.iacuc.generator;

import edu.mit.coeus.iacuc.bean.ProtocolActionsBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.xml.iacuc.* ;
import edu.mit.coeus.utils.UtilFactory;
import java.util.TimeZone;


public class CorrespondenceStream {
    ObjectFactory objFactory ;
    
    /** Creates a new instance of CorrespondenceStream */
    public CorrespondenceStream(ObjectFactory objFactory) {
        this.objFactory = objFactory ;
    }
    
    
    public CorrespondenceType getCorrespondence(ProtocolActionsBean actionBean) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        String protocolId = actionBean.getProtocolNumber() ;
        int submissionNumber = actionBean.getSubmissionNumber();
        String committeeId  = actionBean.getCommitteeId() ;
        int sequenceNumber = actionBean.getSequenceNumber() ;
        
        CorrespondenceType correspondence = objFactory.createCorrespondence();
         
        //java.util.GregorianCalendar currDate = new java.util.GregorianCalendar();
        java.util.Calendar cal = java.util.Calendar.getInstance(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()));
        
        correspondence.setCurrentDate(cal);
        
        // add protocol details
        ProtocolStream protocolStream = new ProtocolStream(objFactory) ;
        if (submissionNumber <= 0) {
            correspondence.setProtocol(protocolStream.getProtocolType(protocolId, sequenceNumber)) ;
        } else {
            correspondence.setProtocol(protocolStream.getProtocolType(protocolId, sequenceNumber, submissionNumber)) ;
        }
        return correspondence ;
        
    }
    
}
