/*
 * ResearchAreaStream.java
 *
 * Created on November 24, 2003, 5:14 PM
 */

/* PMD check performed, and commented unused imports and variables on 10-OCT-2010
 * by George J Nirappeal
 */

package edu.mit.coeus.xml.iacuc.generator;


import edu.mit.coeus.iacuc.bean.CommitteeResearchAreasBean;
import edu.mit.coeus.iacuc.bean.CommitteeTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolReasearchAreasBean;

import edu.mit.coeus.xml.iacuc.ObjectFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.xml.iacuc.* ;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class ResearchAreaStream {
    ObjectFactory objFactory ;
    
    /** Creates a new instance of ResearchAreaStream */
    public ResearchAreaStream(ObjectFactory objFactory) {
        this.objFactory = objFactory ;
    }
    
    public ResearchAreaType getResearchArea(Object reasearchAreasBean, String  type) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        ResearchAreaType researchArea = objFactory.createResearchAreaType() ;
        if (type.equalsIgnoreCase("Protocol")) {
            ProtocolReasearchAreasBean protocolReasearchAreasBean
                    = (ProtocolReasearchAreasBean)reasearchAreasBean ;
            researchArea.setResearchAreaCode(protocolReasearchAreasBean.getResearchAreaCode()) ;
            researchArea.setResearchAreaDescription(protocolReasearchAreasBean.getResearchAreaDescription()) ;
            
           //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
            if(protocolReasearchAreasBean.getUpdateTimestamp() != null){
                researchArea.setUpdateTimestamp(formatDate(protocolReasearchAreasBean.getUpdateTimestamp()));
            }
           //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end  
            
            researchArea.setUpdateUser(protocolReasearchAreasBean.getUpdateUser());
        } else { // committee
            CommitteeResearchAreasBean committeeResearchAreasBean
                    = (CommitteeResearchAreasBean)reasearchAreasBean ;
            researchArea.setResearchAreaCode(committeeResearchAreasBean.getAreaOfResearchCode()) ;
            researchArea.setResearchAreaDescription(committeeResearchAreasBean.getAreaOfResearchDescription()) ;
           
            //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
            if(committeeResearchAreasBean.getUpdateTimestamp() != null){
                researchArea.setUpdateTimestamp(formatDate(committeeResearchAreasBean.getUpdateTimestamp()));
            }
            
            researchArea.setUpdateUser(committeeResearchAreasBean.getUpdateUser());
            //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end  
        }
        
        return researchArea ;
    }
    
    
    private Vector getProtocolResearchArea(Vector vecResearchArea) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        Vector vecResearchAreaStream = new Vector() ;
        
        // get Research Area for this protocol
        if (vecResearchArea != null) {
            for (int vecCount=0 ; vecCount < vecResearchArea.size() ; vecCount++) {
                ProtocolReasearchAreasBean protocolReasearchAreasBean =
                        (ProtocolReasearchAreasBean) vecResearchArea.get(vecCount) ;
                vecResearchAreaStream.add(getResearchArea(protocolReasearchAreasBean, "Protocol")) ;
            }// end for
        } // end if vecResearchArea
        
        return vecResearchAreaStream ;
    }
    
    
    public Vector getCommitteeResearchArea(String committeeId) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        CommitteeTxnBean committeeTxnBean = new CommitteeTxnBean() ;
        Vector vecResearchAreaForCommittee = new Vector() ;
        Vector vecResearchArea = committeeTxnBean.getCommitteeResearchAreas(committeeId) ;
        if (vecResearchArea != null) {
            for (int vecCount=0 ; vecCount < vecResearchArea.size() ; vecCount++) {
                CommitteeResearchAreasBean committeeResearchAreasBean =
                        (CommitteeResearchAreasBean) vecResearchArea.get(vecCount) ;
                vecResearchAreaForCommittee.add(getResearchArea(committeeResearchAreasBean, "Committee")) ;
            }// end for
        }
        
        return vecResearchAreaForCommittee ;
    }
    
   //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start 
     /**
     *  Returns java.util.Calendar type object. Creates a calendar type object and set it time to date which is 
     *  passed to the method. Return the Calendar type object.
     *  @param date Date.
     *  @return calendar Calendar.
     */
    
    private Calendar formatDate(final Date date){
        Calendar calendar =null;
        if(date != null){
            calendar = calendar.getInstance();
            calendar.setTime(date);
        }
        
        return calendar;
    }
  //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end 
    
}
