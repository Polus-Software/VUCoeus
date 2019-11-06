/*
 * XMLGeneratorTxnBean.java
 *
 * Created on September 19, 2003, 2:27 PM
 */

package edu.mit.coeus.utils.xml.generator;

import java.util.* ;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import java.sql.Timestamp;

public class XMLGeneratorTxnBean
{
     // instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    private TransactionMonitor transMon; 
    // holds the userId for the logged in user
    private String userId;
    
    
    /** Creates a new instance of XMLGeneratorTxnBean */
    public XMLGeneratorTxnBean()
    {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    public HashMap getPreviousAndNextSchedule(String committeeId, String scheduleId) throws DBException
    {
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap hashSchedule = null ; 
        param.add(new Parameter("AW_COMMITTEE_ID",
                                DBEngineConstants.TYPE_STRING, committeeId)) ;
        param.addElement(new Parameter("AW_SCHEDULE_ID",
                 DBEngineConstants.TYPE_STRING, scheduleId)) ;
                
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_PREVnNeXT_SCHEDULE (  << AW_COMMITTEE_ID >>, << AW_SCHEDULE_ID >>,   <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }
        else
        {
            throw new DBException("DB instance is not available");
        }
        
        if (result.size()>0)
        {
            hashSchedule = (HashMap)result.get(0) ;
        }
   
        return hashSchedule ;
       
    }
     //ele addition - 11/07/03
    public Integer  getMinuteSortId(String scheduleId, String entryNumber) throws DBException
    {
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap hashResult = null ; 
        Integer sortId = null;
        
        param.add(new Parameter("AW_SCHEDULE_ID",
                                DBEngineConstants.TYPE_STRING, scheduleId)) ;
        param.addElement(new Parameter("AW_ENTRY_NUMBER",
                 DBEngineConstants.TYPE_INT, entryNumber)) ;
                
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_MINUTE_SORT_ID ( << AW_SCHEDULE_ID >>, << AW_ENTRY_NUMBER >>,  <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }
        else 
        {
            throw new DBException("DB instance is not available");
        }
        
        if (result.size()>0)
        { 
            hashResult = (HashMap)result.elementAt(0) ;
            sortId = new Integer(String.valueOf(hashResult.get("SORT_ID")));
        }
  
        return sortId ;
       
    }

     //ele addition - 11/21/03
    public String  getVotingComments(String protocolNumber, String scheduleId) throws DBException
    {
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap hashResult = null ; 
        String votingComments = null;
       
        param.add(new Parameter("AW_PROTOCOL_NUMBER",
                                DBEngineConstants.TYPE_STRING, protocolNumber)) ;
        param.add(new Parameter("AW_SCHEDULE_ID",
                                DBEngineConstants.TYPE_STRING, scheduleId)) ;
       
                
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_VOTING_COMMENTS ( << AW_PROTOCOL_NUMBER >>, << AW_SCHEDULE_ID >>,  <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }
        else
        {
            throw new DBException("DB instance is not available");
        }
        
        if (result.size()>0)
        {
            hashResult = (HashMap)result.elementAt(0) ;
            votingComments = String.valueOf(hashResult.get("VOTING_COMMENTS"));
        }
        return votingComments;
    }
    
    public int getParentSubmissionNumber(String protocolNumber, int submissionNumber) throws DBException, CoeusException
    {
        int parentSubmissionNumber = 0 ;
        Vector param= new Vector();
        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,protocolNumber));
        param.add(new Parameter("AI_SUBMISSION_NUMBER",
                 DBEngineConstants.TYPE_INT,new Integer(submissionNumber).toString()));
        
        Vector result = new Vector();
        HashMap nextNumRow = new HashMap();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER CURRENTSUBNUM>> = call fn_get_parent_submission ( "
                        +" << AS_PROTOCOL_NUMBER >> , << AI_SUBMISSION_NUMBER >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            parentSubmissionNumber = Integer.parseInt(nextNumRow.get("CURRENTSUBNUM").toString()) ;
        }
        return parentSubmissionNumber ;
        
    }
    
    //added by ele 9/2/04 for renewal letter
    //gets the next 5 schedules after today's date
    public Vector getNextScheduleDates(String committeeId)
       throws CoeusException, DBException
    {
        Vector vDates = new Vector();
        HashMap hashDates = null;
        String maxNumber = "5";  //number of schedules to return
        
        Vector result = new Vector(3,2);
        HashMap row = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("COMMITTEE_ID",
            DBEngineConstants.TYPE_STRING,committeeId));
        param.addElement(new Parameter("MAX_NUMBER",
            DBEngineConstants.TYPE_STRING,maxNumber));
        if(dbEngine !=null){
           
            result = dbEngine.executeRequest("Coeus",
            "call GetNextSchedules ( <<COMMITTEE_ID>> , <<MAX_NUMBER>> , "
            + " <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        int vecSize = result.size();
         
        if (vecSize >0){
         
            for(int i=0;i<vecSize;i++){
                hashDates = new HashMap();
                row = (HashMap) result.elementAt(i);
                  
                hashDates.put("SCHEDULE_NUMBER", 
                     row.get("ROWNUM".toString()));
                hashDates.put("SCHEDULE_DATE", 
                   new java.sql.Date ( ((Timestamp) row.get("SCHEDULED_DATE")).getTime()));
                               
                vDates.add(hashDates);
            }    
        }    
        return vDates;
    }
    
}
