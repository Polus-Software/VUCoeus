package edu.mit.is.service.authorization;

public class Function
implements org.okip.service.authorization.api.Function {


  private final static String FUNCTION_KEY = "FUNCTION_KEY";
  private final static String FUNCTION_DESCRIPTION = "FUNCTION_DESCRIPTION";
  private final static String FUNCTION_TYPE = "FUNCTION_TYPE";
  private final static String QUALIFIER_TYPE = "QUALIFIER_TYPE";
  private final static String PRIMARY_AUTHORIZABLE = "PRIMARY_AUTHORIZABLE";
  private final static String MODIFIED_BY = "MODIFIED_BY";
  private final static String MODIFIED_DATE = "MODIFIED_DATE";

  private Factory factory = null;

  /**
   * Method getFactory
   *
   *
   * @return
   *
   */
  public Factory getFactory() {
    return factory;
  }

  protected void setFactory(Factory factory) {
    this.factory = factory;
  }

  private java.util.Map properties = null;

  /**
   * Method getProperties
   *
   *
   * @return
   *
   */
  public java.util.Map getProperties() {
    return this.properties;
  }

  private void setProperties(java.util.Map properties) {
    this.properties = properties;
  }

  protected Object getProperty(String propertyName) {
    if (null != this.properties) {
      Object obj = this.properties.get(propertyName);

      if (null == obj) {
        try {
          if (isFoundInAuthorization()) {
            obj = this.properties.get(propertyName);
          }
        }
        catch (org.okip.service.authorization.api.AuthorizationException ex) {}
      }

      return obj;
    }

    return null;
  }

  protected void setProperty(String propertyName, Object property) {
    if (null == this.properties) {
      this.properties = new java.util.Properties();
    }

    if ((null != this.properties) && (null != propertyName)
            && (null != property)) {
      this.properties.put(propertyName, property);
    }
  }

  /**
   * Method getKey
   *
   *
   * @return java.io.Serializable
   *
   */
  public java.io.Serializable getKey() {
    return (java.io.Serializable) getProperty(FUNCTION_KEY);
  }

  /**
   * Method setKey
   *
   *
   * @param key
   *
   */
  public void setKey(java.io.Serializable key) {
    setProperty(FUNCTION_KEY, key);
  }

  /**
   * Method getDisplayName
   *
   *
   * @return displayName
   *
   */
  public String getDisplayName() {
    return (String) getKey();
  }

  /**
   * Method setDisplayName
   *
   *
   * @param String displayName
   *
   */
  public void setDisplayName(String displayName) {
  }

  /**
   * Method getDescription
   *
   *
   * @return
   *
   */
  public String getDescription() {
    return (String) getProperty(FUNCTION_DESCRIPTION);
  }

  public void setDescription(String description) {
    setProperty(FUNCTION_DESCRIPTION, description);
  }

  /**
   * Method getFunctionType
   *
   *
   * @return
   *
   */
  public org.okip.service.authorization.api.FunctionType getFunctionType() {
    return (org.okip.service.authorization.api.FunctionType) getProperty(
      FUNCTION_TYPE);
  }

  /**
   * Method setFunctionType
   *
   *
   * @param functionType
   *
   */
  public void setFunctionType(
          org.okip.service.authorization.api.FunctionType functionType) {
    setProperty(FUNCTION_TYPE, functionType);
  }

  /**
   * Method getQualifierType
   *
   *
   * @return
   *
   */
  public org.okip.service.authorization.api.QualifierType getQualifierType() {
    return (org.okip.service.authorization.api.QualifierType) getProperty(
      QUALIFIER_TYPE);
  }

  /**
   * Method setQualifierType
   *
   *
   * @param qualifierType
   *
   */
  public void setQualifierType(
          org.okip.service.authorization.api.QualifierType qualifierType) {
    setProperty(QUALIFIER_TYPE, qualifierType);
  }

  // Y(es) N(o) D(eptartment)

  /**
   * Method getPrimaryAuthorizable
   *
   *
   * @return
   *
   */
  public String getPrimaryAuthorizable() {
    return (String) getProperty(PRIMARY_AUTHORIZABLE);
  }

  protected void setPrimaryAuthorizable(String primaryAuthorizable) {
    setProperty(PRIMARY_AUTHORIZABLE, primaryAuthorizable);
  }

  /**
   * Method getModifiedBy
   *
   *
   * @return
   *
   */
  public String getModifiedBy() {
    return (String) getProperty(MODIFIED_BY);
  }

  protected void setModifiedBy(String modifiedBy) {
    setProperty(MODIFIED_BY, modifiedBy);
  }

  /**
   * Method getModifiedDate
   *
   *
   * @return
   *
   */
  public java.sql.Timestamp getModifiedDate() {
    return (java.sql.Timestamp) getProperty(MODIFIED_DATE);
  }

  protected void setModifiedDate(java.sql.Timestamp modifiedDate) {
    setProperty(MODIFIED_DATE, modifiedDate);
  }

  private boolean foundInAuthorization = false;

  /**
   * Method isFoundInAuthorization
   *
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean isFoundInAuthorization()
  throws org.okip.service.authorization.api.AuthorizationException {
    if (!this.foundInAuthorization) {
      this.foundInAuthorization = (isIdInAuthorization()
                                      || isNameAndTypeInAuthorization());
    }

    return this.foundInAuthorization;
  }

  /**
   * Method refresh
   *
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void refresh()
  throws org.okip.service.authorization.api.AuthorizationException {
    if (!isIdInAuthorization()) {
      isNameAndTypeInAuthorization();
    }
  }

  /**
   * Constructor Function
   *
   *
   * @param factory
   * @param properties
   *
   */
  public Function(Factory factory,
                  java.util.Properties properties) {
    setFactory(factory);
    setProperties(properties);
  }

  /**
   * Constructor Function
   *
   *
   * @param factory
   * @param name
   * @param description
   * @param functionType
   * @param qualifierType
   *
   */
  public Function(
          Factory factory,
          java.io.Serializable key, String description,
          org.okip.service.authorization.api.FunctionType functionType,
          org.okip.service.authorization.api.QualifierType qualifierType) {

    properties = new java.util.Properties();
    setFactory(factory);
    setKey(key);
    setDescription(description);
    setFunctionType(functionType);
    setQualifierType(qualifierType);
  }

  /**
   * Constructor Function
   *
   *
   * @param factory
   * @param id
   * @param name
   * @param description
   * @param functionType
   * @param qualifierType
   *
   */
  public Function(
          Factory factory,
          java.io.Serializable key, String displayName, String description,
          org.okip.service.authorization.api.FunctionType functionType,
          org.okip.service.authorization.api.QualifierType qualifierType) {

    properties = new java.util.Properties();
    setFactory(factory);
    setKey(key);
    setDisplayName(displayName);
    setDescription(description);
    setFunctionType(functionType);
    setQualifierType(qualifierType);
  }

  protected Function(
          Factory factory,
          java.io.Serializable key,
          String displayName,
          String description,
          org.okip.service.authorization.api.FunctionType functionType,
          org.okip.service.authorization.api.QualifierType qualifierType,
          String primaryAuthorizable, String modifiedBy,
          java.sql.Timestamp modifiedDate) {
    setFactory(factory);
    setKey(key);
    setDisplayName(displayName);
    setDescription(description);
    setFunctionType(functionType);
    setQualifierType(qualifierType);
    setPrimaryAuthorizable(primaryAuthorizable);
    setModifiedBy(modifiedBy);
    setModifiedDate(modifiedDate);
  }

  /**
   * Method delete
   *
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void delete()
  throws org.okip.service.authorization.api.AuthorizationException {
  /* Starting Comment
         Sabari
         Need to implement a delete function.
         Coeus don;t need a delete function
    try {
      Factory factory =  getFactory();



      if ((null != factory) && (null != factory.getConnection())) {
        //Connection connection = factory.getConnection(); //Sabari
        Object[] args = new Object[3];

        args[0] = this.properties.get(FUNCTION_KEY);
        args[1] = (null == this.properties.get(FUNCTION_TYPE))
                  ? ""
                  : ((FunctionType) this.properties.get(
                    FUNCTION_TYPE)).getKeyword();
        args[2] = ((null != getFactory().getOwner().getProxy(Person.class))
                 ? ((Person) getFactory().getOwner().getProxy(Person.class)).getKerberosName()
                 : null);

        int[] columnTypes = new int[3];

        columnTypes[0] = java.sql.DatabaseMetaData.procedureColumnIn;
        columnTypes[1] = java.sql.DatabaseMetaData.procedureColumnIn;
        columnTypes[2] = java.sql.DatabaseMetaData.procedureColumnIn;

        int[] dataTypes = new int[3];

        dataTypes[0] = java.sql.Types.VARCHAR;
        dataTypes[1] = java.sql.Types.VARCHAR;
        dataTypes[2] = java.sql.Types.VARCHAR;

        connection.callProcedure("AUTHORIZATIONAPI_DELETE_FUNCTION", args,
                                 columnTypes, dataTypes);
        setFactory(null);
        setKey(null);
        setDescription(null);
        setFunctionType(null);
        setQualifierType(null);
        setModifiedBy(null);
        setModifiedDate(null);

        return;
      }


    }

    catch (org.okip.service.authorization.api.AuthorizationException ex) {
      throw new org.okip.service.authorization.api.AuthorizationException(
        ex.getMessage());
    }


    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDFUNCTION);

      Comment End
    Sabari*/

  }

  /**
   * Method update
   *
   *
   * @param newName
   * @param newDescription
   * @param newFunctionType
   * @param newQualifierType
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void update(
          String newName,
          String newDescription,
          org.okip.service.authorization.api.FunctionType newFunctionType,
          org.okip.service.authorization.api.QualifierType newQualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {


  /**
   * Sabari
   * Need coeus implementation
   */
   /*
    try {
      Factory factory = getFactory();

      if ((null != factory) && (null != factory.getConnection())
              && (null != this.properties.get(FUNCTION_KEY))) {
        Connection connection = factory.getConnection();
        Object[] args = new Object[7];

        args[0] = (String) this.getKey();
        args[1] =
          ((FunctionType) this.properties.get(
            FUNCTION_TYPE)).getKeyword();
        args[2] = (null != newName)
                  ? newName
                  : this.properties.get(FUNCTION_KEY);
        args[3] = (null != newDescription)
                  ? newDescription
                  : this.properties.get(FUNCTION_DESCRIPTION);
        args[4] = (null != newFunctionType)
                  ? ((FunctionType)newFunctionType).getKeyword()
                  : ((null != this.properties.get(FUNCTION_TYPE))
                     ? ((FunctionType) this.properties.get(
                       FUNCTION_TYPE)).getKeyword()
                     : null);
        args[5] = (null != newQualifierType)
                  ? ((QualifierType)newQualifierType).getKeyword()
                  : ((null != this.properties.get(QUALIFIER_TYPE))
                     ? ((QualifierType) this.properties.get(
                       QUALIFIER_TYPE)).getKeyword()
                     : null);
        args[6] = ((null != getFactory().getOwner().getProxy(Person.class))
                 ? ((Person) getFactory().getOwner().getProxy(Person.class)).getKerberosName()
                 : null);

        int[] columnTypes = new int[9];

        columnTypes[0] = java.sql.DatabaseMetaData.procedureColumnIn;
        columnTypes[1] = java.sql.DatabaseMetaData.procedureColumnIn;
        columnTypes[2] = java.sql.DatabaseMetaData.procedureColumnIn;
        columnTypes[3] = java.sql.DatabaseMetaData.procedureColumnIn;
        columnTypes[4] = java.sql.DatabaseMetaData.procedureColumnIn;
        columnTypes[5] = java.sql.DatabaseMetaData.procedureColumnIn;
        columnTypes[6] = java.sql.DatabaseMetaData.procedureColumnIn;
        columnTypes[7] = java.sql.DatabaseMetaData.procedureColumnOut;
        columnTypes[8] = java.sql.DatabaseMetaData.procedureColumnOut;

        int[] dataTypes = new int[9];

        dataTypes[0] = java.sql.Types.VARCHAR;
        dataTypes[1] = java.sql.Types.VARCHAR;
        dataTypes[2] = java.sql.Types.VARCHAR;
        dataTypes[3] = java.sql.Types.VARCHAR;
        dataTypes[4] = java.sql.Types.VARCHAR;
        dataTypes[5] = java.sql.Types.VARCHAR;
        dataTypes[6] = java.sql.Types.VARCHAR;
        dataTypes[7] = java.sql.Types.VARCHAR;
        dataTypes[8] = java.sql.Types.VARCHAR;

        Table table =
          connection.callProcedure("AUTHORIZATIONAPI_UPDATE_FUNCTION", args,
                                   columnTypes, dataTypes);

        if (null != table) {
          setKey((java.io.Serializable)args[2]);
          setDescription((String) args[3]);

          FunctionType functionType =
            new FunctionType(factory,
              (String) args[4], null);

          setFunctionType(functionType);

          QualifierType qualifierType =
            new QualifierType(factory,
              (String) args[5], (String) null);

          setQualifierType(qualifierType);

          Rows rows = table.getRows();

          if (rows.hasMoreRows()) {
            Object[] row = rows.nextRow();

            setModifiedBy((String) row[0]);
            setModifiedDate(factory.createSqlTimestamp((String) row[1]));
          }

          return;
        }
      }
    }
    catch (org.okip.service.authorization.api.AuthorizationException ex) {
      throw new org.okip.service.authorization.api.AuthorizationException(
        ex.getMessage());
    }

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDFUNCTION);
      */
  }

  /**
   * Method create
   *
   *
   * @param factory
   * @param name
   * @param description
   * @param functionType
   * @param qualifierType
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
    /**
   * Method getFunction
   *
   *
   * @param factory
   * @param functionName
   * @param functionType
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public static org.okip.service.authorization.api.Function getFunction(
          Factory factory,
          String functionName,
          org.okip.service.authorization.api.FunctionType functionType)
  throws org.okip.service.authorization.api.AuthorizationException {

  /**
   * Sabari
   * Might need a new Coeus impl
   */
   /*
    try {
      if ((null != factory) && (null != factory.getConnection())) {
        Connection connection =
          factory.getConnection();
        Table      table      =
          connection.executeQuery(
            getFunctionSQL(functionName, ((FunctionType)functionType).getKeyword()));

        if (null != table) {
          java.util.Properties properties = getPropertiesFromTable(factory,
                                              table);

          if (null != properties) {
            Function function =
              new Function(factory,
                properties);

            return function;
          }
        }
        else {
          if (! ((FunctionType)functionType).isFoundInAuthorization()) {
            throw new org.okip.service.authorization.api.AuthorizationException(
              org.okip.service.authorization.api.AuthorizationException.FUNCTIONTYPENOTFOUNDINAUTHORIZATION);
          }
        }
      }
    }
    catch (org.okip.service.authorization.api.AuthorizationException ex) {
      throw new org.okip.service.authorization.api.AuthorizationException(
        ex.getMessage());
    }

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDFUNCTION);
  */
  Function function = null;
  return function;
  }

  private static String getFunctionSQL(String functionName,
                                       String functionType) {
    String sql = "select";

    sql +=
      " function_id, function_name, function_description, function_category,";
    sql += " modified_by, modified_date, qualifier_type";
    sql += " from function";
    sql += " where function_name = '" + functionName + "'";
    sql += " and function_category = '" + functionType + "'";

    return sql;
  }

  private boolean isIdInAuthorization()
  throws org.okip.service.authorization.api.AuthorizationException {
  /**
   * Sabari
   * Might not need this function for Coeus
   */
   /*
    Factory factory =
      getFactory();

    if ((null != factory) && (null != factory.getConnection())
            && (null != this.properties.get(FUNCTION_KEY))) {
      Connection connection =
        factory.getConnection();
      Table      table      =
        connection.executeQuery(
          new String(
            "select function_id," + " function_name, function_description,"
            + " function_category, qualifier_type,"
            + " primary_authorizable, modified_by," + " modified_date"
            + " from function" + " where function_id = '"
            + this.properties.get(FUNCTION_KEY) + "'"));

      if (null != table) {
        java.util.Properties properties = getPropertiesFromTable(factory,
                                            table);

        if (null != properties) {
          setProperties(properties);

          return true;
        }
      }
    }

    */
    return false;

  }

  /**
   * Sabari
   * Do not need this method
   */
  private boolean isNameAndTypeInAuthorization()
  throws org.okip.service.authorization.api.AuthorizationException {
  /*
    Factory factory =
      getFactory();

    if ((null != factory) && (null != factory.getConnection())
            && (null != this.properties.get(FUNCTION_KEY))
            && (null != this.properties.get(FUNCTION_TYPE))
            && (null
                != ((FunctionType) this.properties.get(
                  FUNCTION_TYPE)).getKeyword())) {
      Connection connection =
        factory.getConnection();
      Table      table      = connection.executeQuery(
        new String(
          "select" + " function_id, function_name, function_description, "
          + " function_category, qualifier_type, "
          + " primary_authorizable, modified_by, " + " modified_date "
          + " from function " + " where function_name = '"
          + this.properties.get(FUNCTION_KEY) + "'"
          + " and function_category = '"
          + ((FunctionType) this.properties.get(FUNCTION_TYPE)).getKeyword()
          + "'"));

      if (null != table) {
        java.util.Properties properties = getPropertiesFromTable(factory,
                                            table);

        if (null != properties) {
          setProperties(properties);

          return true;
        }
      }
    }

    */
    return false;

  }
/*
  private static java.util.Properties getPropertiesFromTable(
          Factory factory,
          Table table)
  throws org.okip.service.authorization.api.AuthorizationException {
    if (null != table) {
      Rows rows = table.getRows();

      if (rows.hasMoreRows()) {
        Object[]             row         = rows.nextRow();
        String[]             columnNames = rows.getColumnNames();

        java.util.Properties properties  = new java.util.Properties();

        for (int i = 0; columnNames.length != i; i++) {
          if ((null != columnNames[i]) && (null != row[i])
                  &&!(columnNames[i].equals(""))
                  &&!(columnNames[i].equalsIgnoreCase("NULL"))) {
            if (columnNames[i].trim().equals("FUNCTION_TYPE")) {
              properties.put(
                columnNames[i],
                new FunctionType(
                  factory, (String) row[i], (String) null));
            }
            else if (columnNames[i].trim().equals("QUALIFIER_TYPE")) {
              properties.put(
                columnNames[i],
                new QualifierType(
                  factory, (String) row[i], (String) null));
            }
            else {
              properties.put(columnNames[i], row[i]);
            }
          }
        }

        return properties;
      }
    }

    return null;

  }
  */

}
