/*
 * MinutesStream.java
 *
 * Created on November 24, 2003, 4:47 PM
 */

/* PMD check performed, and commented unused imports and variables on 10-OCT-2010
 * by George J Nirappeal
 */

package edu.mit.coeus.xml.iacuc.generator;

import edu.mit.coeus.iacuc.bean.MinuteEntryInfoBean;
import java.util.* ;
import java.math.BigInteger;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.generator.XMLGeneratorTxnBean;
import edu.mit.coeus.xml.iacuc.* ;

public class MinutesStream {
    
    ObjectFactory objFactory ;
    
    /** Creates a new instance of MinutesStream */
    public MinutesStream(ObjectFactory objFactory) {
        this.objFactory = objFactory ;
    }
     //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    public MinuteType getMinute(MinuteEntryInfoBean minuteEntryInfoBean, String scheduleId) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        
        MinuteType minute = objFactory.createMinuteType() ;
     //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        minute.setScheduleId(scheduleId) ;
        try {
            minute.setEntryNumber(new BigInteger(String.valueOf(minuteEntryInfoBean.getEntryNumber()))) ;
        } catch(Exception ex) {
        }
        
        try {
            minute.setEntryTypeCode(new BigInteger(String.valueOf(minuteEntryInfoBean.getMinuteEntryTypeCode()))) ;
        } catch(Exception ex) {
        }
      
        minute.setEntryTypeDesc(minuteEntryInfoBean.getMinuteEntryTypeDesc()) ;
        minute.setProtocolContingencyCode(minuteEntryInfoBean.getProtocolContingencyCode() != null ?minuteEntryInfoBean.getProtocolContingencyCode(): null) ;
        minute.setMinuteEntry(minuteEntryInfoBean.getMinuteEntry()) ;
        
        try {
            minute.setPrivateCommentFlag(minuteEntryInfoBean.isPrivateCommentFlag()) ;
        } catch(Exception prvFlag) {
          
        }
        
        //ele change 11/25/03
        //protocol number is used to store OtherItemDesc (action)
        if (minuteEntryInfoBean.getProtocolNumber() != null) {
            if (minuteEntryInfoBean.getOtherItemDesc() != null) {
                minute.setProtocolNumber(minuteEntryInfoBean.getOtherItemDesc());
            } else {
                minute.setProtocolNumber(minuteEntryInfoBean.getProtocolNumber()) ;
            }
        }
        //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start 
        if(minuteEntryInfoBean.getUpdateTimestamp() != null ){
             minute.setUpdateTimestamp(formatDate(minuteEntryInfoBean.getUpdateTimestamp()));   
        }
         //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        minute.setUpdateUser(minuteEntryInfoBean.getUpdateUser());
        return minute ;
    }
    
    public Vector getSCheduleMinutes(Vector vecMinutes, String scheduleId)  throws CoeusException, DBException, javax.xml.bind.JAXBException {
        Vector vecMinutesStream = new Vector() ;
        if (vecMinutes != null) {
            for (int minLoop = 0 ; minLoop < vecMinutes.size() ; minLoop++ ) {
                MinuteEntryInfoBean minuteEntryInfoBean = (MinuteEntryInfoBean) vecMinutes.get(minLoop) ;
                
                // the minute entries should not be of type ProtocolType, as the ones with type as
                // ProtocolType will be added to the ProtocolSubmission
                if (minuteEntryInfoBean.getMinuteEntryTypeCode()!= 3) // not of type protocol
                {
                   //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
                    MinuteType scheduleMinutes = getMinute(minuteEntryInfoBean, scheduleId) ;
                   //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
                    //ele start addition 11/07/03
                    XMLGeneratorTxnBean xmlGeneratorTxnBean = new XMLGeneratorTxnBean() ;
                    Integer sortId =  xmlGeneratorTxnBean.getMinuteSortId(scheduleId,
                            String.valueOf(minuteEntryInfoBean.getEntryNumber()));
                    
                    scheduleMinutes.setEntrySortCode(new BigInteger(String.valueOf(sortId)));
                    //ele end addition 11/07/03
                    
                    // add each minute to schedule
                    vecMinutesStream.add(scheduleMinutes) ;
                }
            } // end for
        }// end if vecMinutes
        
        
        return vecMinutesStream ;
        
    }
    
    
    public Vector getProtocolMinutes(Vector vecMinutes, String scheduleId, String protocolId, int submissionNumber)  throws CoeusException, DBException, javax.xml.bind.JAXBException {
        Vector vecMinutesStream = new Vector() ;
        
        if (vecMinutes != null) {
            //MinutesStream minutesStream = new MinutesStream(objFactory) ;
            for (int minLoop = 0 ; minLoop < vecMinutes.size() ; minLoop++ ) {
                MinuteEntryInfoBean minuteEntryInfoBean = (MinuteEntryInfoBean) vecMinutes.get(minLoop) ;
                // the minute entries should be of type ProtocolType
                if (minuteEntryInfoBean.getProtocolNumber() != null) {
                    if (minuteEntryInfoBean.getProtocolNumber().equals(protocolId)
                    && minuteEntryInfoBean.getSubmissionNumber() == submissionNumber) {
//                   MinutesType protocolMinutes = getMinute(minuteEntryInfoBean, scheduleId) ;
                        //MinutesType protocolMinutes = getMinute(minuteEntryInfoBean, minuteEntryInfoBean.getScheduleId()) ;
                        //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
                         MinuteType protocolMinutes = getMinute(minuteEntryInfoBean, minuteEntryInfoBean.getScheduleId()) ;
                        //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
                        // add each minute to protocolSubmission
                        vecMinutesStream.add(protocolMinutes) ;
                    }// end if
                } // end if
            } // end for
        }// end if vecMinutes
        
        return vecMinutesStream ;
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
