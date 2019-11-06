/*
 * MinutesStream.java
 *
 * Created on November 24, 2003, 4:47 PM
 */

package edu.mit.coeus.utils.xml.generator;

import java.util.* ;
import java.math.BigInteger;

import edu.mit.coeus.irb.bean.* ;
import edu.mit.coeus.utils.xml.bean.schedule.ObjectFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.schedule.* ; 
import edu.mit.coeus.utils.DateUtils;

public class MinutesStream
{

    ObjectFactory objFactory ;
    
    /** Creates a new instance of MinutesStream */
    public MinutesStream(ObjectFactory objFactory)
    {
        this.objFactory = objFactory ;
    }
    
    public MinutesType getMinute(MinuteEntryInfoBean minuteEntryInfoBean, String scheduleId) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
       if (scheduleId == null) System.out.println("** getMinute : scheduleId is null **") ; 
       
       MinutesType minute = objFactory.createMinutesType() ; 
       minute.setScheduleId(scheduleId) ; 
       try
       {
        minute.setEntryNumber(new BigInteger(String.valueOf(minuteEntryInfoBean.getEntryNumber()))) ;
       }
       catch(Exception ex)
       {
        System.out.println("** getMinute : getEntryNumber is null **") ; 
       }
       try
       {
        minute.setEntryTypeCode(new BigInteger(String.valueOf(minuteEntryInfoBean.getMinuteEntryTypeCode()))) ;
       }
       catch(Exception ex)
       {
        System.out.println("** getMinute : getMinuteEntryTypeCode is null **") ; 
       } 
       
       if (minuteEntryInfoBean.getMinuteEntryTypeDesc() == null)  System.out.println("** getMinute : getMinuteEntryTypeDesc is null **") ; 
       minute.setEntryTypeDesc(minuteEntryInfoBean.getMinuteEntryTypeDesc()) ;

       minute.setProtocolContingencyCode(minuteEntryInfoBean.getProtocolContingencyCode() != null ?minuteEntryInfoBean.getProtocolContingencyCode(): null) ; 

       if (minuteEntryInfoBean.getMinuteEntry() == null)  System.out.println("** getMinute : getMinuteEntry is null **") ; 
       minute.setMinuteEntry(minuteEntryInfoBean.getMinuteEntry()) ;
       
       try
       {
          minute.setPrivateCommentFlag(minuteEntryInfoBean.isPrivateCommentFlag()) ;
       }
       catch(Exception prvFlag)
       {
          // do nothing so that this tag doesnt get added to the xml generated
           System.out.println("** getMinute : isPrivateCommentFlag is null **") ; 
       }
       //Added for case COEUSQA-2593	 Non-final comments appear minutes pdf begin
       if(minuteEntryInfoBean.isFinalFlag()){
           minute.setFinalFlag("Y");
       
       }else{
            minute.setFinalFlag("N");
       }
       //Added for case COEUSQA-2593	 Non-final comments appear minutes pdf end.
       //ele change 11/25/03
       //protocol number is used to store OtherItemDesc (action) 
       if (minuteEntryInfoBean.getProtocolNumber() != null)
         { 
             if (minuteEntryInfoBean.getOtherItemDesc() != null)
                {    
                minute.setProtocolNumber(minuteEntryInfoBean.getOtherItemDesc());
                }
             else
             {
                minute.setProtocolNumber(minuteEntryInfoBean.getProtocolNumber()) ;
             }
         }
             
       return minute ;
    }
    
    public Vector getSCheduleMinutes(Vector vecMinutes, String scheduleId)  throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        Vector vecMinutesStream = new Vector () ;
        if (vecMinutes != null)
        {
            for (int minLoop = 0 ; minLoop < vecMinutes.size() ; minLoop++ )
            {
               MinuteEntryInfoBean minuteEntryInfoBean = (MinuteEntryInfoBean) vecMinutes.get(minLoop) ;
               
               // the minute entries should not be of type ProtocolType, as the ones with type as
               // ProtocolType will be added to the ProtocolSubmission 
               if (minuteEntryInfoBean.getMinuteEntryTypeCode()!= 3) // not of type protocol
               {
                    MinutesType scheduleMinutes = getMinute(minuteEntryInfoBean, scheduleId) ;
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
    
    
    public Vector getProtocolMinutes(Vector vecMinutes, String scheduleId, String protocolId, int submissionNumber)  throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
       Vector vecMinutesStream = new Vector () ;
    
       if (vecMinutes != null)
       {
           //MinutesStream minutesStream = new MinutesStream(objFactory) ;
            for (int minLoop = 0 ; minLoop < vecMinutes.size() ; minLoop++ )
            {
               MinuteEntryInfoBean minuteEntryInfoBean = (MinuteEntryInfoBean) vecMinutes.get(minLoop) ;
               // the minute entries should be of type ProtocolType  
               if (minuteEntryInfoBean.getProtocolNumber() != null)
               {
               if (minuteEntryInfoBean.getProtocolNumber().equals(protocolId) 
                    && minuteEntryInfoBean.getSubmissionNumber() == submissionNumber)
               { 
//                   MinutesType protocolMinutes = getMinute(minuteEntryInfoBean, scheduleId) ; 
                   MinutesType protocolMinutes = getMinute(minuteEntryInfoBean, minuteEntryInfoBean.getScheduleId()) ; 
                   // add each minute to protocolSubmission
                   vecMinutesStream.add(protocolMinutes) ;
                }// end if
              } // end if
            } // end for
        }// end if vecMinutes
    
       return vecMinutesStream ;
    }
    
    
}
