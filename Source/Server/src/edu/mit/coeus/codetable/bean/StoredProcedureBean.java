/*
 * @(#)StoredProcedureBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.codetable.bean;

import java.io.Serializable;
import java.util.Vector;

/**
 * Component for storing and accessing information about a given stored procedure,
 * associated with one or more code tables.  Members include the stored procedure name,
 * the type of operation, and parameters taken by the stored proceure.
 */
public class StoredProcedureBean implements Serializable
{

  /**
   * Is this a select or an update operation?  Valid values include SELECT and UPDATE.
   */
  private String operation;

  /**
   * Actual name of the stored procedure.
   */
  private String name;

  /**
   * Vector of ParameterBean objects for this stored procedure.
   * @see edu.mit.coeus.codetable.bean.ParameterBean
   */
  private Vector vectParameters;

  /**
   * No argument constructor.
   */
  public StoredProcedureBean()
  {
  }

  /**
   * Get operation.
   */
  public String getOperation()
  {
    return operation;
  }

  /**
   * Set operation.
   * @param operation
   */
  public void setOperation(String operation)
  {
    this.operation = operation;
  }

  /**
   * Set name.
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Get name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set vectParameters.
   * @param vectParameters
   */
  public void setVectParameters(Vector vectParameters)
  {
    this.vectParameters = vectParameters;
  }

  /**
   * Get vectParameters.
   */
  public Vector getVectParameters()
  {
    return vectParameters;
  }
}