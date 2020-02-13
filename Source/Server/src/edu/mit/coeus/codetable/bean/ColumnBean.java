/*
 * @(#)ColumnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.codetable.bean;

import java.io.Serializable;
import java.util.* ;

/**
 * Component for storing and accessing information about a column in a code table.
 * Contains information pertaining to the columnName, the data type, and the number of
 * characters to allocate in the GUI for this column's value.
 */
public class ColumnBean implements Serializable
{
  /**
   * Name of the column.
   */
  private String columnName;

  private String displayName ;
  /**
   * Data type for this column.
   */
  private String dataType;

  /**
   * Number of characters to allocate in the GUI for the value which will be stored
   * in this column.  GUI will validate that the number of characters entered does
   * not exceed this number.
   */
  private int displaySize;

  private String identifier ;
  
  private boolean visible ;
  
  private boolean editable ;
  
  private String qualifier ;
  
  private boolean canbenull ;

  
  private HashMap hashOptions = null ;
  
  private String defaultValue ;
  
  //Added with COEUSQA-2667:User interface for setting up question details for procedure categories
  private int maxLength ;
  //COEUSQA-2667:End
  
  /**
   * No argument constructor.
   */
  public ColumnBean()
  {
  }

  /**
   * Set columnName.
   * @param columnName
   */
  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }

  /**
   * Get columnName.
   */
  public String getColumnName()
  {
    return columnName;
  }


  
  public void setDisplayName(String displayName)
  {
    this.displayName = displayName ;
  }

  public String getDisplayName()
  {
    return displayName;
  }
  
  /**
   * Set dataType.
   * @param dataType
   */
  public void setDataType(String dataType)
  {
    this.dataType = dataType;
  }

  /**
   * Get dataType.
   */
  public String getDataType()
  {
    return dataType;
  }

  /**
   * Set displaySize.
   * @param displaySize
   */
  public void setDisplaySize(int displaySize)
  {
    this.displaySize = displaySize;
  }

  /**
   * Get displaySize.
   */
  public int getDisplaySize()
  {
    return displaySize;
  }
  
  
  public void setColIdentifier(String identifier)
  {
    this.identifier = identifier;
  }

  /**
   * Get columnName.
   */
  public String getColIdentifier()
  {
    return identifier;
  }
  
  public boolean getColumnVisible()
  {
    return visible ;
  }
  
  public void setColumnVisible(boolean visible)
  {
    this.visible = visible ;
  }
  
  public boolean getColumnEditable()
  {
    return editable ;
  }
  
  public void setColumnEditable(boolean editable)
  {
    this.editable = editable ;
  }
  
  
  public String getQualifier()
  {
    return qualifier ;
  }
  
  public void setQualifier(String qualifier)
  {
    this.qualifier = qualifier ;
  }
  
  public boolean  getColumnCanBeNull()
  {
    return canbenull ;
  }
  
  public void setColumnCanBeNull(boolean canbenull)
  {
    this.canbenull = canbenull ;
  }
   
  public HashMap getOptions()
  {
    return hashOptions ;
  }
  
  public void setOptions(HashMap hashOptions)
  {
    this.hashOptions = hashOptions ; 
  }
  
  public String getDefaultValue()
  {
    return defaultValue ;
  }
  
  public void setDefaultValue(String defaultValue)
  {
    this.defaultValue = defaultValue ;
  }
  
  //Added with COEUSQA-2667:User interface for setting up question details for procedure categories
  public int getMaxLength() {
      return maxLength;
  }
  
  public void setMaxLength(int maxLength) {
      this.maxLength = maxLength;
  }
  //COEUSQA-2667:End
  
}