/*
 * SignatureTypeStream.java
 *
 * Created on March 2, 2004, 4:33 PM
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

/**
 *
 * @author  ele
 */
import java.util.* ;


import edu.mit.coeus.propdev.bean.* ;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.DateUtils;

public class SignatureTypeStream {
    
    ObjectFactory objFactory ;
    SignatureType signatureType;
   
    /** Creates a new instance of SignatureTypeStream */
    public SignatureTypeStream(ObjectFactory objFactory) 
    {
        this.objFactory = objFactory ;
    }
    
   
    public SignatureType getSignatureInfo(RolodexDetailsBean rolodexBean) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
         signatureType = objFactory.createSignatureType();
        //hard coding signature authentication and date
         signatureType.setSignatureAuthentication("unknown");
    //     signatureType.setSignatureDate(convertDateStringToCalendar("01/01/1900"));
          signatureType.setSignatureDate(getTodayDate());    
         return signatureType;
    }
    
    //overloaded for piBean
      public SignatureType getSignatureInfo(ProposalPersonFormBean pIPersonBean) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
         signatureType = objFactory.createSignatureType();
        //hard coding signature authentication and date
         signatureType.setSignatureAuthentication("unknown");
     //    signatureType.setSignatureDate(convertDateStringToCalendar("01/01/1900"));
         signatureType.setSignatureDate(getTodayDate());   
         return signatureType;
    }
    
      private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
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
