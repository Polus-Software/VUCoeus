/*
 * CoreStateReceiptQualifiersTypeStream.java
 *
 * Created on March 2, 2004, 6:02 PM
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

public class CoreStateReceiptQualifiersTypeStream {
    ObjectFactory objFactory ;
    CoreStateReceiptQualifiersType coreStateReceiptQualType;
    
    /** Creates a new instance of CoreStateReceiptQualifiersTypeStream */
    public CoreStateReceiptQualifiersTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
       
    }
    
    public CoreStateReceiptQualifiersType getCoreStateReceiptQual(String propNumber) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
         coreStateReceiptQualType = objFactory.createCoreStateReceiptQualifiersType();
         coreStateReceiptQualType.setStateReceiptDate(convertDateStringToCalendar("10/17/2005"));
         return coreStateReceiptQualType;
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
