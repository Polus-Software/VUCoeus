/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GrantsObject.java,v 1.1.2.13 2007/10/17 18:09:32 cvs Exp $
 * $Log: GrantsObject.java,v $
 * Revision 1.1.2.13  2007/10/17 18:09:32  cvs
 * Server-side support for Monthly Report
 *
 * Revision 1.1.2.12  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.11  2007/08/13 17:21:25  cvs
 * Add departmental graphs by proposal status
 *
 * Revision 1.1.2.10  2007/08/07 17:55:03  cvs
 * Add Percent Proposal Status Chart
 *
 * Revision 1.1.2.9  2007/08/06 14:48:55  cvs
 * Use Start Date as delimiting criteria per Regeane request
 *
 * Revision 1.1.2.8  2007/07/02 17:26:22  cvs
 * Correct count anomaly with fiscal year-related issues.
 *
 * Revision 1.1.2.7  2007/03/29 16:22:44  cvs
 * Add support for Current Active Grants Report
 *
 * Revision 1.1.2.6  2007/01/22 23:07:33  cvs
 * Changed Anticipated Amounts to Obligated Amounts
 * Support Fiscal Year
 *
 * Revision 1.1.2.5  2007/01/18 16:52:08  cvs
 * Add support for Summary Reports by School retrieval
 *
 * Revision 1.1.2.4  2006/12/28 16:19:35  cvs
 *  Add support for grants by activity type data retrieval
 *
 * Revision 1.1.2.3  2006/12/27 17:52:48  cvs
 *  Add support for grants by sponsor type data retrieval
 *
 * Revision 1.1.2.2  2006/12/07 18:10:57  cvs
 * Assign currency and comma formatting to appropriate double values
 *
 * Revision 1.1.2.1  2006/12/04 20:20:59  cvs
 * Add PDF format report based on SOM model
 *
 *
 */
/*
 * @(#)GrantsObject.java 
 *
 * Copyright (c) University of Medicine and Dentistry of New Jersey
 *
 * 1 World's Fair Drive, Somerset, New Jersey 08873
 *
 * All rights reserved.
 *
 * 
 * Author: Romerl Elizes
 *
 * Description: GrantsObject bean.
 * 
 */

package edu.umdnj.coeus.reporting.bean;

import java.io.*;
import java.sql.Timestamp;

public class GrantsObject
{
	private String accountNumber;
	private String gafaNumber;
	private String beginProjectDate;
	private String endProjectDate;
	private double projectTotalCost;
	private String investigatorName;
	private String projectTitle;
	private String sponsorName;
        private String awardBeginDate;
	private String awardEndDate;
	private double directCost;
	private double indirectCost;
	private double totalCost;
        private String schoolName;
        private String unitName;
        private String sponsorTypeDescription;
        private String activityTypeDescription;
        private String awardTypeDescription;
        private double obligatedChangeAmount;
        private String costElement;
        private Timestamp awardStartDateTimestamp;
	private Timestamp awardEndDateTimestamp;

        private String proposalStatus;
        private String proposalType;
	private double numProjects;
        private boolean bsubcontract;
        private String nopDescription;

	public GrantsObject()
	{
		accountNumber = "";
		gafaNumber = "";
		beginProjectDate = "";
		endProjectDate = "";
		projectTotalCost = 0;
		investigatorName = "";
		projectTitle = "";
		sponsorName = "";
                awardBeginDate = "";
		awardEndDate = "";
		directCost = 0;
		indirectCost = 0;
		totalCost = 0;	
                schoolName = "";
                unitName = "";
                sponsorTypeDescription = "";
                activityTypeDescription = "";
                awardTypeDescription = "";
                proposalStatus = "";
                proposalType = "";
                numProjects = 0;
                bsubcontract = false;
        	nopDescription = "";
	}
	
    public GrantsObject(String str)
	{
    	this();
		accountNumber = str;
	}

	public String getAccountNumber()
	{
		return accountNumber;
    }
		
	public String getGafaNumber()
	{
		return gafaNumber;
    }
		
	public String getBeginProjectDate()
	{
		return beginProjectDate;
    }
		
	public String getEndProjectDate()
	{
		return endProjectDate;
    }
		
	public double getProjectTotalCost()
	{
		return projectTotalCost;
    }
		
	public String getInvestigatorName()
	{
		return investigatorName;
    }
		
	public String getProjectTitle()
	{
		return projectTitle;
    }
		
	public String getSponsorName()
	{
		return sponsorName;
    }
		
	public String getAwardEndDate()
	{
		return awardEndDate;
    }
		
	public String getAwardBeginDate()
	{
		return awardBeginDate;
    }
        
	public double getDirectCost()
	{
		return directCost;
    }
		
	public double getIndirectCost()
	{
		return indirectCost;
    }
		
	public double getTotalCost()
	{
		return totalCost;
        }
        
        public String getSchoolName()
        {
            return schoolName;
        }

        public String getUnitName()
        {
            return unitName;
        }

        public String getSponsorTypeDescription()
        {
            return sponsorTypeDescription;
        }

        public String getActivityTypeDescription()
        {
            return activityTypeDescription;
        }

        public String getAwardTypeDescription()
        {
            return awardTypeDescription;
        }
        
        public double getObligatedChangeAmount()
        {
            return obligatedChangeAmount;
        }
        
        public String getCostElement()
        {
            return costElement;
        }

	public Timestamp getAwardEndDateTimestamp()
        {
            return awardEndDateTimestamp;
        }
        
        public Timestamp getAwardStartDateTimestamp()
        {
            return awardStartDateTimestamp;
        }

	public String getProposalStatus()
	{
		return proposalStatus;
    }
	public String getProposalType()
	{
		return proposalType;
    }
	public boolean isSubContract()
	{
                return bsubcontract;
	}
	public String getNopDescription()
	{
		return nopDescription;
    	}
		
	public void setAccountNumber(String str)
	{
		accountNumber = str;
	}
		
	public void setGafaNumber(String str)
	{
		gafaNumber = str;
	}
		
	public void setBeginProjectDate(String str)
	{
		beginProjectDate = str;
	}
		
	public void setEndProjectDate(String str)
	{
		endProjectDate = str;
	}
		
	public void setProjectTotalCost(double val)
	{
		projectTotalCost = val;
	}
		
	public void setInvestigatorName(String str)
	{
		investigatorName = str;
	}
		
	public void setProjectTitle(String str)
	{
		projectTitle = str;
	}
		
	public void setSponsorName(String str)
	{
		sponsorName = str;
    }
		
	public void setAwardEndDate(String str)
	{
		awardEndDate = str;
    }

 	public void setAwardBeginDate(String str)
	{
		awardBeginDate = str;
    }

	public void setDirectCost(double val)
	{
		directCost = val;
    }
		
	public void setIndirectCost(double val)
	{
		indirectCost = val;
    }
		
	public void setTotalCost(double val)
	{
		totalCost = val;
    }
		
	public void addDirectCost(double val)
	{
		directCost += val;
    }
		
	public void addIndirectCost(double val)
	{
		indirectCost += val;
    }
		
	public void addTotalCost(double val)
	{
		totalCost += val;
        }
        
        public void setSchoolName(String str)
        {
            schoolName = str;
        }

        public void setUnitName(String str)
        {
            unitName = str;
        }

        public void setSponsorTypeDescription(String str) 
        { 
           sponsorTypeDescription = str; 
        }

        public void setActivityTypeDescription(String str) 
        { 
           activityTypeDescription = str; 
        }

        public void setAwardTypeDescription(String str) 
        { 
           awardTypeDescription = str; 
        }
        
        public void setObligatedChangeAmount(double val)
        {
            obligatedChangeAmount = val;
        }
        
        public void setCostElement(String str)
        {
            costElement = str;
        }

	public void setAwardEndDateTimestamp(Timestamp canddate)
        {
            awardEndDateTimestamp = canddate;
        }
        
        public void setAwardStartDateTimestamp(Timestamp canddate)
        {
            awardStartDateTimestamp = canddate;
        }

 	public void setProposalStatus(String str)
	{
            proposalStatus = str;
        }

 	public void setProposalType(String str)
	{
            proposalType = str;
        }

	public void isSubContractFlag(boolean bsubcontract)
	{
            this.bsubcontract = bsubcontract;
	}
		
	public void setNopDescription(String str)
	{
		nopDescription = str;
    	}
}
