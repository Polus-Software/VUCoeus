/*
 * @(#)RequestTxnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */


package edu.mit.coeus.codetable.bean;

import java.io.Serializable;
//import java.util.Vector;
import java.util.*;

/**
 * Component for passing GUI request for code table structures, as well as code
 * table retrieves and updates.
 */
public class RequestTxnBean implements Serializable
{
  /**
   * Action that the bean is requesting the servlet to take.
   * Valid values include "LIST_TABLES", "GET_DATA" and "MODIFY_DATA".
   */
  private String action;

  /**
   * Vector containing a Hashtable object for each row to be updated.
   * Each Hashtable contains key value pairs of parameter name and parameter value.
   */
  private Vector rowsToModify;

  /**
   * StoredProcedureBean object for select or update operation being requested.
   */
  private StoredProcedureBean storedProcedureBean;

 // this vector will have the paramater values reqd for the dependency check stored procedure  
  private HashMap  hashDependencyCheck ;
  
  //this will pass the table name (osp$person or osp$eps_proposal) for getColumnforTables();
  private String tableName ;

  private Vector selectProcParameters = new Vector() ;
  
  // Added for coeus4.3 enhancements
  private String procedureName;
  
  /**
   * No argument constructor.
   */
  public RequestTxnBean()
  {
  }

  /**
   * Get storedProcedureBean.
   * @return storedProcedureBean
   */
  public StoredProcedureBean getStoredProcedureBean()
  {
    return storedProcedureBean;
  }

  /**
   * Set storedProcedureBean.
   * @param storedProcedureBean
   */
  public void setStoredProcedureBean(StoredProcedureBean storedProcedureBean)
  {
    this.storedProcedureBean = storedProcedureBean;
  }

  /**
   * Set action.
   * @param action
   */
  public void setAction(String action)
  {
    this.action = action;
  }

  /**
   * Get action.
   * @return action
   */
  public String getAction()
  {
    return action;
  }

  
  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }

  
  public String getTableName()
  {
    return tableName;
  
  }
  
  /**
   * Set rowsToModify
   * @param rowsToModify
   */
  public void setRowsToModify(Vector rowsToModify)
  {
    this.rowsToModify = rowsToModify;
  }

  /**
   * Get rowsToModify
   * @return rowsToModify
   */
  public Vector getRowsToModify()
  {
    return rowsToModify;
  }
  
  
  public void setDependencyCheck(HashMap hashDependencyCheck)
  {
    this.hashDependencyCheck = hashDependencyCheck ;
  }
  
  public HashMap getDependencyCheck()
  {
    return hashDependencyCheck ;
  }
  
  public Vector getSelectProcParameters()
  {
     return selectProcParameters ;
  }
  
  public void setSelectProcParameters(Vector selectProcParameters)
  {
    this.selectProcParameters = selectProcParameters ;
  }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }
  
}