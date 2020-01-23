/*
 * @(#)AllCodeTablesBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.codetable.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * Response object for passing information about code table structure to the GUI.
 * hashAllCodeTableStructure is a HashMap containing code table names and corresponding
 * CodeTableStructureBean objects.
 * @see edu.mit.coeus.codetable.bean.CodeTableStructureBean.
 */
public class AllCodeTablesBean implements Serializable
{
  /**
   * HashMap containing code table names and corresponding CodeTableStructureBean objects.
   * CodeTableStructureBean objects contains information about code table structure,
   * such as how many columns in the table, display name for the table, and stored procedures
   * associated with the table.
   * @see edu.mit.coeus.codetable.bean.CodeTableStructureBean.
   */
  private HashMap hashAllCodeTableStructure;
  
  /**
   * hashAllGroupNameList containing a list of group names.
   */
  private HashMap hashAllGroupNameList;

  /**
   * List of all code tables.
   */
  private String[] tableNameList;

  /**
   * No argument constructor.
   */
  public void AllCodeTablesBean()
  {}

  /**
   * Get hashAllCodeTableStructure.
   */
  public HashMap getHashAllCodeTableStructure()
  {
    return hashAllCodeTableStructure;
  }

  /**
   * Set hashAllCodeTableStructure.
   * @param hashAllCodeTableStructure
   */
  public void setHashAllCodeTableStructure(HashMap hashAllCodeTableStructure)
  {
    this.hashAllCodeTableStructure = hashAllCodeTableStructure;
  }

  /**
   * Get hashAllGroupNameList.
   */
  
  public HashMap getHashAllGroupNameList()
  {
    return hashAllGroupNameList;
  }

  /**
   * Set hashAllGroupNameList.
   * @param hashAllGroupNameList
   */
  public void setHashAllGroupNameList(HashMap hashAllGroupNameList)
  {
    this.hashAllGroupNameList = hashAllGroupNameList;
  }

    
  /**
   * Get list of all code table names.
   * hashAllCodeTableStructure has key-value pairs of table name and corresponding
   * CodeTableStructureBean.  Get key set for hashAllCodeTableStructure and iterate
   * through, putting the values in a String array.
   */
  public String[] getTableNameList()
  {
    try
    {
        if(hashAllCodeTableStructure != null)
        {
          int tableListLength = hashAllCodeTableStructure.size();
          if(tableListLength < 1)
          {
            tableNameList = new String[]{""};
          }
          else
          {
            int nameCount = 0;
            tableNameList = new String[tableListLength];
            while( nameCount < tableListLength )  
            {    
               TableStructureBean tableStructureBean = (TableStructureBean) hashAllCodeTableStructure.get(new Integer(nameCount)) ;
               //tableNameList[nameCount] = tableStructureBean.getActualName() ;
               tableNameList[nameCount] = tableStructureBean.getDisplayName() ;
               nameCount++;
            }
                       
          }
        }
    }catch(Exception exp)
    {
        exp.printStackTrace();
    }
    
    return tableNameList;
  }

  
   /**
   * Get list of all code table names.
   * hashAllCodeTableStructure has key-value pairs of table name and corresponding
   * CodeTableStructureBean.  Get key set for hashAllCodeTableStructure and iterate
   * through, putting the values in a String array.
   */
  public Vector getTableNameList(String groupName)
  {
    Vector tableNameList = new Vector() ;  
    try
    {
        if(hashAllCodeTableStructure != null)
        {
          int tableListLength = hashAllCodeTableStructure.size();
          
            int nameCount = 0;
            for (int loopCount=0 ; loopCount < tableListLength ; loopCount++)  
            {    
               TableStructureBean tableStructureBean = (TableStructureBean) hashAllCodeTableStructure.get(new Integer(loopCount)) ;
               if (tableStructureBean.getGroupName().equals(groupName))
               {    
                tableNameList.add(nameCount, tableStructureBean.getDisplayName()) ;
                nameCount++ ;
               }
            }
          }
    }catch(Exception exp)
    {
        exp.printStackTrace();
    }
    
    return tableNameList;
  }

  
}