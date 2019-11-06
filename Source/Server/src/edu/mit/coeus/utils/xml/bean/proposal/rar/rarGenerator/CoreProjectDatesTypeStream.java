/*
 * CoreProjectDatesTypeStream.java
 *
 * Created on March 2, 2004, 6:04 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator;

/**
 *
 * @author  ele
 */
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.utils.xml.bean.proposal.rar.* ; 
import edu.mit.coeus.utils.DateUtils;
import java.util.* ;

public class CoreProjectDatesTypeStream {
    
    ObjectFactory objFactory ;
    CoreProjectDatesType coreProjectDatesType;
 
    /** Creates a new instance of CoreProjectDatesTypeStream */
    public CoreProjectDatesTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
    public CoreProjectDatesType getCoreProjectDates(ProposalDevelopmentFormBean propBean) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
         coreProjectDatesType = objFactory.createCoreProjectDatesType();
       //requestDates for whole thing aren't in the bean...  
         coreProjectDatesType.setProjectStartDate(convertDateStringToCalendar(propBean.getRequestStartDateInitial().toString()));
         coreProjectDatesType.setProjectEndDate(convertDateStringToCalendar(propBean.getRequestEndDateInitial().toString()));
  
         return coreProjectDatesType;
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
