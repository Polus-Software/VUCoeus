/**
 * ProposalCentersBean.java
 * 
 * Used to hold IP centers data
 *
 * @created	April 12, 2012
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.instprop.bean;

import java.util.Date;
import edu.mit.coeus.instprop.bean.InstituteProposalBaseBean;

public class ProposalCentersBean extends InstituteProposalBaseBean {
    
	private String centerNum;
	private Boolean baseCenter;
	private String centerDesc;
	private String initiateMode;
	private String awardNum;
	private Integer awardSeq;
	private String sponsorAward;
    private String instPropNum;
    private String devPropNum;
    private Date createDate;
    private Date processDate;    
    
    /**
     *	Default Constructor
     */
    public ProposalCentersBean(){
    }
    
    public String getCenterNum() {
        return centerNum;
    }
    
    public void setCenterNum(String centerNum) {
        this.centerNum = centerNum;
    }
    
    public Boolean getBaseCenter() {
        return baseCenter;
    }
    
    public void setBaseCenter(Boolean baseCenter) {
   		this.baseCenter = baseCenter;
    }

    public String getCenterDesc() {
        return centerDesc;
    }
    
    public void setCenterDesc(String centerDesc) {
        this.centerDesc = centerDesc;
    }
    
    public String getInitiateMode() {
        return initiateMode;
    }
    
    public void setInitiateMode(String initiateMode) {
        this.initiateMode = initiateMode;
    }
    
    public String getAwardNum() {
        return awardNum;
    }
    
    public void setAwardNum(String awardNum) {
        this.awardNum = awardNum;
    }    

    public Integer getAwardSeq() {
        return awardSeq;
    }
    
    public void setAwardSeq(Integer awardSeq) {
        this.awardSeq = awardSeq;
    }    
    
    public String getSponsorAward() {
    	return sponsorAward;
    }

    public void setSponsorAward(String sponsorAward) {
    	this.sponsorAward = sponsorAward;
    }
    
    public String getInstPropNum() {
        return instPropNum;
    }
    
    public void setInstPropNum(String instPropNum) {
        this.instPropNum = instPropNum;
    }
    
    public String getDevPropNum() {
        return devPropNum;
    }
    
    public void setDevPropNum(String devPropNum) {
        this.devPropNum = devPropNum;
    }
    
    public Date getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public Date getProcessDate() {
        return processDate;
    }
    
    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }
 
}    
