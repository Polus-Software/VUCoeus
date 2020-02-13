/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/PIBean.java,v 1.1.2.2 2007/01/30 20:22:44 cvs Exp $
 * $Log: PIBean.java,v $
 * Revision 1.1.2.2  2007/01/30 20:22:44  cvs
 * Add servlet and GUI support for PI delimiter
 *
 * Revision 1.1.2.1  2007/01/29 20:45:21  cvs
 * Add servlet support for PI delimiter
 *
 *
 */
/*
 * @(#)PIBean.java 
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
 * Description: PIBean bean.
 * 
 */

package edu.umdnj.coeus.reporting.bean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import java.io.*;

public class PIBean
        implements CoeusBean, java.io.Serializable
{
	private String personId;
	private String lastName;
	private String firstName;
	private String fullName;
        private java.sql.Timestamp updateTimestamp;
        private String updateUser;
        private String acType;

	public PIBean()
	{
	   personId = "";
	   lastName = "";
	   firstName = "";
	   fullName = "";
	}
	

	public String getPersonID()
	{
		return personId;
        }
		
	public String getLastName()
	{
		return lastName;
        }
		
	public String getFirstName()
	{
		return firstName;
        }
		
	public String getFullName()
	{
		return fullName;
        }
		
	public void setPersonID(String str)
	{
		personId = str;
	}
		
	public void setLastName(String str)
	{
		lastName = str;
	}
		
	public void setFirstName(String str)
	{
		firstName = str;
	}
		
	public void setFullName(String str)
	{
		fullName = str;
	}
		
        public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) 
        {
                this.updateTimestamp = updateTimestamp;
        }
        
        public java.sql.Timestamp getUpdateTimestamp() 
        {
               return updateTimestamp;
        }
        
        public java.lang.String getUpdateUser() 
        {
              return updateUser;
        }

        public void setUpdateUser(java.lang.String updateUser) 
        {
              this.updateUser = updateUser;
        }

        public java.lang.String getAcType() 
        {
              return acType;
        }
        
        public void setAcType(java.lang.String acType) 
        {
              this.acType = acType;
        }    
        
        public boolean isLike(ComparableBean comparableBean)
        {
              return true;
        }

}