/*
 * @(#)DataBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.codetable.bean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.io.Serializable;

/**
 * Component for passing code table data from the Server to the GUI.
 */
public class DataBean implements Serializable
{
  /**
   * Number of columns in the table.
   */
  private int colCount;

  /**
   * Vector of HashMap objects.  Each HashMap represents one DB row.
   * Each Hashtble object contains key-value pairs for column name
   * and column value.
   */
  private Vector vectData;

  /**
   * Number of rows in the table.
   */
  private int rowCount;

  /**
   * Actual code table name.
   */
  private String tableName;

  /**
   * No argument constructor.
   */
  public DataBean()
  {  }

  /**
   * Get names of all columns in this table, in the order in which they are
   * contained in vectData.
   * @return columnNames
   */
  public String[] getColumnNames()
  {
    String [] columnNames = null;
    if(vectData == null)
    {
      columnNames = new String[]{""};
      return columnNames;
    }
    else if(vectData != null)
    {
        HashMap rowOneData = (HashMap)vectData.get(0);
        int numCols = rowOneData.size();
        columnNames = new String[numCols];
        Set keySet = rowOneData.keySet();
        Iterator it = keySet.iterator();
        int colCnt = 0;
        while(it.hasNext())
        {
          String colName = (String)it.next();
          columnNames[colCnt] = colName;
          colCnt++;
        }
      }
    return columnNames;
  }

  /**
   * Get colCount.
   * @return colCount.
   */
  public int getColCount()
  {
    return colCount;
  }

  /**
   * Set colCount.
   * @param colCount
   */
  public void setColCount(int colCount)
  {
    this.colCount = colCount;
  }

  /**
   * Set rowCount.
   * @param rowCount
   */
  public void setRowCount(int rowCount)
  {
    this.rowCount = rowCount;
  }

  /**
   * Get rowCount.
   * @return rowCount
   */
  public int getRowCount()
  {
    if(vectData != null)
    {
      rowCount = vectData.size();
    }
    return rowCount;
  }

  /**
   * Set tableName.
   * @param tableName
   */
  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }

  /**
   * Get tableName.
   * @return tableName
   */
  public String getTableName()
  {
    return tableName;
  }


  /**
   * Get vectData.
   * @return vectData
   */
  public Vector getVectData()
  {
    return vectData;
  }

  /**
   * Set vectData.
   * @param vectData
   */
  public void setVectData(Vector vectData)
  {
    this.vectData = vectData;
  }

}