/*
 * @(#)UnitHierarchyBean.java 
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
 * Description: Bean for data retrieval and population to GUI.
 * 
 */
package edu.umdnj.coeus.reporting.bean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import java.io.*;

public class UnitHierarchyBean
        implements CoeusBean, java.io.Serializable
{
  private String unitNumber;
  private String parentUnitNumber;
  private String hasChildren;
  private String unitName;
  private java.sql.Timestamp updateTimestamp;
  private String updateUser;
  private String acType;

  public void setUnitNumber(String unitNumber) 
  { 
    this.unitNumber = unitNumber; 
  }

  public void setParentUnitNumber(String parentUnitNumber) 
  { 
    this.parentUnitNumber = parentUnitNumber; 
  }

  public void setHasChildren(String hasChildren) 
  { 
    this.hasChildren = hasChildren; 
  }

  public void setUnitName(String unitName) 
  { 
    this.unitName = unitName; 
  }


  public String getUnitNumber() 
  { 
    return unitNumber; 
  }

  public String getParentUnitNumber() 
  { 
    return parentUnitNumber; 
  }

  public String getHasChildren() 
  { 
    return hasChildren; 
  }

  public String getUnitName() 
  { 
    return unitName; 
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
