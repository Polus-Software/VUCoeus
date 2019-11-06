/*
 * ResearchAreaStream.java
 *
 * Created on November 24, 2003, 5:14 PM
 */

package edu.mit.coeus.utils.xml.generator;


import java.util.* ;
import java.math.BigInteger;

import edu.mit.coeus.irb.bean.* ;
import edu.mit.coeus.utils.xml.bean.schedule.ObjectFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.schedule.* ; 
import edu.mit.coeus.utils.DateUtils ;

public class ResearchAreaStream
{
    ObjectFactory objFactory ;
    
    /** Creates a new instance of ResearchAreaStream */
    public ResearchAreaStream(ObjectFactory objFactory)
    {
        this.objFactory = objFactory ;
    }
    
    public ResearchAreaType getResearchArea(Object reasearchAreasBean, String  type) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        ResearchAreaType researchArea = objFactory.createResearchAreaType() ;
        if (type.equalsIgnoreCase("Protocol"))
        {   
            ProtocolReasearchAreasBean protocolReasearchAreasBean
                    = (ProtocolReasearchAreasBean)reasearchAreasBean ;
            researchArea.setResearchAreaCode(protocolReasearchAreasBean.getResearchAreaCode()) ;
            researchArea.setResearchAreaDescription(protocolReasearchAreasBean.getResearchAreaDescription()) ;
        }
        else
        { // committee
            CommitteeResearchAreasBean committeeResearchAreasBean
                 = (CommitteeResearchAreasBean)reasearchAreasBean ;
            researchArea.setResearchAreaCode(committeeResearchAreasBean.getAreaOfResearchCode()) ;
            researchArea.setResearchAreaDescription(committeeResearchAreasBean.getAreaOfResearchDescription()) ;
        }    
        return researchArea ;
    }
    
    
    private Vector getProtocolResearchArea(Vector vecResearchArea) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        Vector vecResearchAreaStream = new Vector() ;
        
            // get Research Area for this protocol
            if (vecResearchArea != null)
            {
                for (int vecCount=0 ; vecCount < vecResearchArea.size() ; vecCount++)
                {
                    ProtocolReasearchAreasBean protocolReasearchAreasBean = 
                                    (ProtocolReasearchAreasBean) vecResearchArea.get(vecCount) ;
                    vecResearchAreaStream.add(getResearchArea(protocolReasearchAreasBean, "Protocol")) ;
                }// end for    
            } // end if vecResearchArea  
        
        return vecResearchAreaStream ; 
    }
    
    
    public Vector getCommitteeResearchArea (String committeeId) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        CommitteeTxnBean committeeTxnBean = new CommitteeTxnBean() ;
        Vector vecResearchAreaForCommittee = new Vector() ;
        Vector vecResearchArea = committeeTxnBean.getCommitteeResearchAreas(committeeId) ;
        if (vecResearchArea != null)
        {    
           for (int vecCount=0 ; vecCount < vecResearchArea.size() ; vecCount++)
            {
                CommitteeResearchAreasBean committeeResearchAreasBean = 
                                (CommitteeResearchAreasBean) vecResearchArea.get(vecCount) ;
                vecResearchAreaForCommittee.add(getResearchArea(committeeResearchAreasBean, "Committee")) ;
            }// end for    
        }    
        
        return vecResearchAreaForCommittee ;
    }
 
   
}
