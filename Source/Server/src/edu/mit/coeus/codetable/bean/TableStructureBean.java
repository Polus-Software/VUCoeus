/*
 * @(#)TableStructureBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.codetable.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;


/**
 * Component for storing information about a given code table in the database.
 * Contains all pertinent information about the code table, including a HashMap
 * of ColumnBean objects.
 */
public class TableStructureBean implements Serializable
{
  /**
   * Actual code table name.
   */
  private String actualName;

  /**
   * Name to be displayed by GUI for this code table.
   */
  private String displayName;

  /**
   * Description of the code table.
   */
  private String description;

  //private int primaryKeyIndex ;
  
  private Vector vecPrimaryKeyIndex ;
  
  private int userIndex ;
  
  //private int duplicateIndex ;
  private Vector vecDuplicateKeyIndex ;
  
  
  /**
   * Select and update stored procedures associated with this code table.
   * HashMap of StoredProcedureBean objects associated with this table.
   * Key-value pairs of stored procedure operation type ("SELECT" or "UPDATE")
   * and their corresponding stored procedures.
   * @see edu.mit.coeus.codetable.bean.StoredProcedureBean.
   */
  private HashMap hashStoredProceduresForThisTable;

  /**
   * Are the column ID's for this table auto-generated or are they editable by the user.
   */
  private boolean idAutoGenerated;

  /**
   * Number of columns in the table.
   */
  private int numColumns;

  /**
   * HashMap of column names and corresponding ColumnBean objects.
   * @see edu.mit.coeus.codetable.bean.ColumnBean.
   */
  private HashMap hashTableColumns;

  /**
   * Vector of column names for this table, in the order in which they should
   * be displayed.
   */
  private Vector vectColumnName;

  private HashMap hashDependency ;
  
  // string to hold the form name, to be displayed for special case tables 
  private String formComponent ;
    
  private String groupName ;
  
  
  
  /**
   * No parameter constructor.
   */
  public TableStructureBean()
  {
  }

  /**
   * Set actualName.
   * @param actualName
   */
  public void setActualName(String actualName)
  {
    this.actualName = actualName;
  }

  /**
   * Get actualName.
   * @return
   */
  public String getActualName()
  {
    return actualName;
  }

  /**
   * Set hashTableColumns.
   * @param tableColumns
   */
  public void setHashTableColumns(HashMap hashTableColumns)
  {
    this.hashTableColumns = hashTableColumns;
  }

  /**
   * Get hashTableColumns.
   */
  public HashMap getHashTableColumns()
  {
    return hashTableColumns;
  }

  /**
   * Set description for this code table.
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Get description for this code table.
   */
  public String getDescription()
  {
    return description;
  }


  /**
   * Set name to be displayed by GUI for this code table.
   * @param displayName
   */
  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  /**
   * Get name to be displayed by GUI for this code table.
   */
  public String getDisplayName()
  {
    return displayName;
  }

  /**
   * Set hashStoredProceduresForThisTable.
   * @param hashStoredProceduresForThisTable
   */
  public void setHashStoredProceduresForThisTable(HashMap hashStoredProceduresForThisTable)
  {
    this.hashStoredProceduresForThisTable = hashStoredProceduresForThisTable;
  }

  /**
   * Get hashStoredProceduresForThisTable.
   */
  public HashMap getHashStoredProceduresForThisTable()
  {
    return hashStoredProceduresForThisTable;
  }


  /**
   * Set idAutoGenerated.
   * @param idAutoGenerated
   */
  public void setIdAutoGenerated(boolean idAutoGenerated)
  {
    this.idAutoGenerated = idAutoGenerated;
  }

  /**
   * Is column ID automatically generated for this code table?
   */
  public boolean isIdAutoGenerated()
  {
    return idAutoGenerated;
  }

  /**
   * Set number of columns for this code table.
   * @param numColumns
   */
  public void setNumColumns(int numColumns)
  {
    this.numColumns = numColumns;
  }

  /**
   * Get number of columns in this code table.
   */
  public int getNumColumns()
  {
    return numColumns;
  }

  /**
   * Set vectColumnName.
   * @param vectColumnName
   */
  public void setVectColumnName(Vector vectColumnName)
  {
    this.vectColumnName = vectColumnName;
  }

  /**
   * Get vectColumnName.
   * @return vectColumnName
   */
  public Vector getVectColumnName()
  {
    return vectColumnName;
  }

  public Vector getPrimaryKeyIndex()
  {
    return vecPrimaryKeyIndex ;
  }
  
  
  public int getPrimaryKeyIndex(int element)
  {
    return  Integer.parseInt(vecPrimaryKeyIndex.get(element).toString()) ; 
  }
  
  public void setPrimaryKeyIndex(Vector vecPrimaryKeyIndex)
  {
    this.vecPrimaryKeyIndex = vecPrimaryKeyIndex ;
  }
  
  
  public void setUserIndex(int userIndex)
  {
    this.userIndex = userIndex ;
  }
  
  public int getUserIndex()
  {
    return userIndex ;
  }
  
   
  public void setDuplicateIndex(Vector vecDuplicateKeyIndex)
  {
    this.vecDuplicateKeyIndex = vecDuplicateKeyIndex ;
  }
  
  public Vector getDuplicateIndex()
  {
    return vecDuplicateKeyIndex ;
  }
  
  public int getDuplicateIndex(int element)
  {
    return  Integer.parseInt(vecDuplicateKeyIndex.get(element).toString()) ; 
  }
  
  public HashMap getHashTableDependency()
  {
    return hashDependency ;
  }
  
  public void setHashTableDependency(HashMap hashDependency)
  {
    this.hashDependency = hashDependency ;
  }
  
  public String getFormComponent()
  {
    return formComponent ;
  }
  
  public void SetFormComponent(String formComponent)
  {
    this.formComponent = formComponent ;
  }
  
  public String getGroupName()
  {
    return this.groupName ;
  }
  
  public void setGroupName(String groupName)
  {
      this.groupName = groupName ;
  }
  
  public String toString() 
  {
        return this.displayName ;
  }
}