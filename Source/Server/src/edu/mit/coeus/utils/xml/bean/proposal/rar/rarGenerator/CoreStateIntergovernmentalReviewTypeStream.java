/*
 * CoreStateIntergovernmentalReviewTypeStream.java
 *
 * Created on March 2, 2004, 6:03 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 
import edu.mit.coeus.utils.DateUtils;
import java.util.* ;

public class CoreStateIntergovernmentalReviewTypeStream {
    ObjectFactory objFactory ;
    CoreStateIntergovernmentalReviewType coreStateIntergovRevType;
 
    /** Creates a new instance of CoreStateIntergovernmentalReviewTypeStream */
    public CoreStateIntergovernmentalReviewTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
    public CoreStateIntergovernmentalReviewType getCoreStateIntergovReview(String propNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
         coreStateIntergovRevType = objFactory.createCoreStateIntergovernmentalReviewType();
         coreStateIntergovRevType.setReviewAvailabilityDate(convertDateStringToCalendar("04/14/2004"));
         coreStateIntergovRevType.setSubjectToReviewQuestion(true);
         
         return coreStateIntergovRevType;
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
}
