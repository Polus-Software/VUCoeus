/*
 * SubmissionDetailsBean.java
 *
 * Created on March 27, 2003, 12:40 PM
 */

package edu.mit.coeus.iacuc.bean;

import java.util.* ;

public class SubmissionDetailsBean implements java.io.Serializable
{
    
    private Vector vecSubmissionDetails ;
    private String protocolNumber;
    //holds sequence number
    private Integer sequenceNumber;
    // holds the schedule id
    private String scheduleId;
    
    //holds submission number
    private Integer submissionNumber;
    
    private Vector vecReviewers ;
    
    /** Creates a new instance of SubmissionDetailsBean */
    public SubmissionDetailsBean()
    {
    }
    
    
    public String getProtocolNumber()
    {
        return protocolNumber ;
    }
    
    public void setProtocolNumber(String protocolNumber)
    {
        this.protocolNumber = protocolNumber ;
    }
    
    
    public Integer getSequenceNumber()
    {
        return sequenceNumber ;
    }
    
    public void setSequenceNumber(Integer sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber ;
    }
    
    public Integer getSubmissionNumber()
    {
        return submissionNumber ;
    }
    
    public void setSubmissionNumber(Integer submissionNumber)
    {
        this.submissionNumber = submissionNumber ;
    }
    
    
    public String getScheduleId()
    {
        return scheduleId ;
    }
    
    public void setScheduleId(String scheduleId )
    {
        this.scheduleId = scheduleId ;
    }
        
    public Vector getSubmissionDetails()
    {
        return vecSubmissionDetails ;
    }
    
    public void setSubmissionDetails(Vector vecSubmissionDetails)
    {
        this.vecSubmissionDetails = vecSubmissionDetails ;
    }
    
    public void setSelectedReviewers(Vector vecReviewers)
    {
        this.vecReviewers = vecReviewers ;
    }
    public Vector getSelectedReviewers()
    {
        return vecReviewers ;
    }
    
}
