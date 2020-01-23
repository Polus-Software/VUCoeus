/*
 * @(#)ParameterBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.codetable.bean;

import java.io.Serializable;

/**
 * Component for storing and accessing information about a parameter for a stored
 * procedure associated with a code table.
 */

public class ParameterBean implements Serializable
{
  /**
   * Name of the parameter.
   */
  private String name;

  /**
   * Data type for this parameter.
   */
  private String dataType;

  /**
   * No argument constructor.
   */
  public ParameterBean()
  {
  }

  /**
   * Constructor initializes name, dataType, direction, qualifier and columnName.
   * @param name
   * @param dataType
   */
  public ParameterBean(String name, String dataType)
  {
    this.name = name;
    this.dataType = dataType;
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
   * Get name.
   */
  public String getName()
  {
    return name;
  }


  /**
   * Set name.
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

}