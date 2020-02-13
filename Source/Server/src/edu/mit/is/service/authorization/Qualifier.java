package edu.mit.is.service.authorization;

public class Qualifier
implements org.okip.service.authorization.api.Qualifier {
  private static String                                     QUALIFIER_ID =
    "QUALIFIER_ID";
  private static String                                     QUALIFIER_CODE =
    "QUALIFIER_CODE";
  private static String                                     QUALIFIER_DESCRIPTION =
    "QUALIFIER_NAME";
  private static String                                     QUALIFIER_TYPE =
    "QUALIFIER_TYPE";
  private static String                                     HAS_CHILD      =
    "HAS_CHILD";

  private Factory factory   =
    null;

  /**
   * Method getFactory
   *
   *
   * @return
   *
   */
  public Factory getFactory() {
    return this.factory;
  }

  protected void setFactory(
          Factory factory) {
    this.factory = factory;
  }

  private java.util.Map properties = null;

  /**
   * Method getProperties
   *
   *
   * @return java.util.Map
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
        catch (Exception ex) {}
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
   * Method getId
   *
   *
   * @return java.io.Serializable
   *
   */
  public java.io.Serializable getId() {
    return (java.io.Serializable) getProperty(QUALIFIER_ID);
  }

  /**
   * Method setId
   *
   *
   * @param java.io.Serializable id
   *
   */
  public void setId(java.io.Serializable id) {
    setProperty(QUALIFIER_ID, id);
  }

  /**
   * Method getKey
   *
   *
   * @return java.io.Serializable
   *
   */
  public java.io.Serializable getKey() {
    return (java.io.Serializable) getProperty(QUALIFIER_CODE);
  }

  /**
   * Method setKey
   *
   *
   * @param java.io.Serializable key
   *
   */
  public void setKey(java.io.Serializable key) {
    setProperty(QUALIFIER_CODE, key);
  }

  /**
   * Method getDisplayName
   *
   *
   * @return String displayName
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
   * @return String description
   *
   */
  public String getDescription() {
    return (String) getProperty(QUALIFIER_DESCRIPTION);
  }

  /*protected void setDescription(String description) {
    setProperty(QUALIFIER_DESCRIPTION, description);
  }*/
  //Modified above code
  public void setDescription(String description) {
    setProperty(QUALIFIER_DESCRIPTION, description);
  }

  /**
   * Method getQualifierType
   *
   *
   * @return QualifierType
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
   * @param QualifierType
   *
   */
  public void setQualifierType(
          org.okip.service.authorization.api.QualifierType qualifierType) {
    setProperty(QUALIFIER_TYPE, qualifierType);
  }

  /**
   * Method isChildOf
   *
   * @param qualifier
   *
   * @return boolean
   *
   */
  public boolean isChildOf(org.okip.service.authorization.api.Qualifier parent)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED);
  }

  /**
   * Method getChildren
   *
   *
   * @return QualifiersIterator
   *
   */
  public org.okip.service.authorization.api.QualifiersIterator getChildren()
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED);
  }

  /**
   * Method getParents
   *
   *
   * @return QualifiersIterator
   *
   */
  public org.okip.service.authorization.api.QualifiersIterator getParents()
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED);
  }

  /**
   * Method getHasChild
   *
   *
   * @return boolean
   *
   */
  public boolean getHasChild() {
    if(null != getProperty(HAS_CHILD)) {
      return ((Boolean) getProperty(HAS_CHILD)).booleanValue();
    }
    return false;
  }

  protected void setHasChild(boolean hasChild) {
    setProperty(HAS_CHILD, new Boolean(hasChild));
  }

  private boolean foundInAuthorization = false;

  /**
   * Method isFoundInAuthorization
   *
   *
   * @return boolean
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean isFoundInAuthorization()
  throws org.okip.service.authorization.api.AuthorizationException {
    if (!this.foundInAuthorization) {
      this.foundInAuthorization = (isIdInAuthorizations()
                                      || isCodeAndTypeInAuthorizations());
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
    if (!isIdInAuthorizations()) {
      isCodeAndTypeInAuthorizations();
    }
  }

  /**
   * Constructor Qualifier
   *
   *
   * @param factory
   * @param properties
   *
   */
  public Qualifier(Factory factory,
                   java.util.Properties properties) {
    setFactory(factory);
    setProperties(properties);
  }

  /**
   * Constructor Qualifier
   *
   *
   * @param factory
   * @param code
   * @param description
   * @param qualifierType
   *
   */
  public Qualifier(
          Factory factory,
          java.io.Serializable code, String description,
          org.okip.service.authorization.api.QualifierType qualifierType) {

    properties = new java.util.Properties();
    setFactory(factory);
    setKey(code);
    setDescription(description);
    setQualifierType(qualifierType);
  }

  /**
   * Constructor Qualifier
   *
   *
   * @param factory
   * @param id
   * @param code
   * @param description
   * @param qualifierType
   *
   */
  public Qualifier(
          Factory factory, java.io.Serializable id,
          java.io.Serializable code, String description,
          org.okip.service.authorization.api.QualifierType qualifierType) {

    properties = new java.util.Properties();
    setFactory(factory);
    setId(id);
    //setKey(code);
    setKey(id);   //Sabari
    setDescription(description);
    setQualifierType(qualifierType);
  }

  protected Qualifier(
          Factory factory,
          java.io.Serializable code, String description,
          org.okip.service.authorization.api.QualifierType qualifierType,
          boolean hasChild) {
    setFactory(factory);
    setKey(code);
    setDescription(description);
    setQualifierType(qualifierType);
    setHasChild(hasChild);
  }

  /**
   * Method delete
   *
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
   /**
    * Sabari
    * Not needed in Coeus
    */
  public void delete()
  throws org.okip.service.authorization.api.AuthorizationException {
  /*
    if ((null != getFactory())
            && (null != getFactory().getConnection())
            && ((null != this.properties.get(QUALIFIER_ID))
                || ((null != this.properties.get(QUALIFIER_TYPE))
                    && (null != this.properties.get(QUALIFIER_CODE))))) {
      Connection connection =
        getFactory().getConnection();

      Object[]                                 args       = new Object[3];

      args[0] =
        ((QualifierType) this.properties.get(
          QUALIFIER_TYPE)).getType();
      args[1] = this.properties.get(QUALIFIER_CODE);
      args[2] = ((null != getFactory().getOwner().getProxy(Person.class))
                 ? ((Person) getFactory().getOwner().getProxy(Person.class)).getKerberosName()
                 : null);

      int[] columnTypes = new int[4];

      columnTypes[0] = java.sql.DatabaseMetaData.procedureColumnIn;
      columnTypes[1] = java.sql.DatabaseMetaData.procedureColumnIn;
      columnTypes[2] = java.sql.DatabaseMetaData.procedureColumnIn;
      columnTypes[3] = java.sql.DatabaseMetaData.procedureColumnOut;

      int[] dataTypes = new int[4];

      dataTypes[0] = java.sql.Types.VARCHAR;
      dataTypes[1] = java.sql.Types.VARCHAR;
      dataTypes[2] = java.sql.Types.VARCHAR;
      dataTypes[3] = java.sql.Types.VARCHAR;

      connection.callProcedure("AUTH_SP_DELETE_QUAL", args, columnTypes,
                               dataTypes);
      setFactory(null);
      setId(null);
      setKey(null);
      setDescription(null);
      setQualifierType(null);
      setHasChild(false);

      return;
    }

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDQUALIFIER);
      */
  }

  /**
   * Method update
   *
   *
   * @param newQualifierCode
   * @param newDescription
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void update(String newQualifierCode, String newDescription)
  throws org.okip.service.authorization.api.AuthorizationException {

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDQUALIFIER);
  }

  /**
   * Method addParent
   *
   *
   * @param parent
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void addParent(org.okip.service.authorization.api.Qualifier parent)
  throws org.okip.service.authorization.api.AuthorizationException {

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDQUALIFIER);
  }

  /**
   * Method removeParent
   *
   *
   * @param parent
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void removeParent(org.okip.service.authorization.api.Qualifier parent)
  throws org.okip.service.authorization.api.AuthorizationException {

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDQUALIFIER);
  }

  /**
   * Method changeParent
   *
   *
   * @param oldParent
   * @param newParent
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void changeParent(org.okip.service.authorization.api.Qualifier oldParent,
                       org.okip.service.authorization.api.Qualifier newParent)
  throws org.okip.service.authorization.api.AuthorizationException {


    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDQUALIFIER);
  }

  /**
   * Method create
   *
   *
   * @param factory
   * @param qualifierType
   * @param qualifierCode
   * @param qualifierDescription
   * @param parent
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public static org.okip.service.authorization.api.Qualifier create(
          Factory factory,
          org.okip.service.authorization.api.QualifierType qualifierType,
          String qualifierCode, String qualifierDescription,
          org.okip.service.authorization.api.Qualifier parent)
  throws org.okip.service.authorization.api.AuthorizationException {

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDQUALIFIER);
  }

  private static String getQualifierCode(
          Factory factory,
          String qualifierId)
  throws org.okip.service.authorization.api.AuthorizationException {

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDQUALIFIER);
  }

  /**
   * Method getRootQualifier
   *
   *
   * @param factory
   * @param qualifierType
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
   /**
    * Sabari
    * Not required for Coeus
    */
  public static org.okip.service.authorization.api.Qualifier getRootQualifier(
          Factory factory,
          org.okip.service.authorization.api.QualifierType qualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDQUALIFIER);
  }

  /**
   * Sabari
   * Don't need this method
   */
  private static org.okip.service.authorization.api.Qualifier getQualifier(
          Factory factory,
          java.io.Serializable id,
          String qualifierCode,
          org.okip.service.authorization.api.QualifierType qualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {
  /*
    if ((null != factory) && (null != factory.getConnection())
            && (null != qualifierType) && (null != qualifierCode)) {
      Connection connection =
        factory.getConnection();
      Table      table      = connection.executeQuery(
        new String(
          "select" + " q.qualifier_id, q.qualifier_code, q.qualifier_name,"
          + " q.qualifier_type, q.has_child" + " from qualifier q" + " where"
          + " q.qualifier_type = '"
          + ((QualifierType) qualifierType).getType()
          + "'" + " and q.qualifier_code = '" + qualifierCode + "'"));

      if (null != table) {
        java.util.Properties properties = getPropertiesFromTable(factory,
                                            table);

        if (null != properties) {
          org.okip.service.authorization.api.Qualifier qualifier =
            new Qualifier(factory,
              properties);

          return qualifier;
        }
      }
    }

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDQUALIFIER);
      */
      return null;
  }

  /**
   * Sabari
   * Need coeus impl
   */
  private static org.okip.service.authorization.api.Qualifier getQualifier(
          Factory factory,
          java.io.Serializable id)
  throws org.okip.service.authorization.api.AuthorizationException {

  /*
    if ((null != factory) && (null != factory.getConnection())
            && (null != id)) {
      Connection connection =
        factory.getConnection();
      Table      table      =
        connection.executeQuery(
          new String(
            "select" + " q.qualifier_id, q.qualifier_code, q.qualifier_name,"
            + " q.qualifier_type, q.has_child" + " from qualifier q"
            + " where" + " q.qualifier_id = '" + id + "'"));

      if (null != table) {
        java.util.Properties properties = getPropertiesFromTable(factory,
                                            table);

        if (null != properties) {
          org.okip.service.authorization.api.Qualifier qualifier =
            new Qualifier(factory,
              properties);

          return qualifier;
        }
      }
    }
*/

    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.INVALIDQUALIFIER);
  }

  /**
   * Sabari
   * not needed in Coeus
   */
  private boolean isIdInAuthorizations()
  throws org.okip.service.authorization.api.AuthorizationException {
    return false;
  }
/**
   * Sabari
   * not needed in Coeus
   */

  private boolean isCodeAndTypeInAuthorizations()
  throws org.okip.service.authorization.api.AuthorizationException {
      return false;
  }
/***
 * Commented by sabari
 */
 /*
  private static java.util.Properties getPropertiesFromTable(
          Factory factory,
          Table table)
  throws org.okip.service.authorization.api.AuthorizationException {
    java.util.Properties properties = new java.util.Properties();

    if (null != table) {
      Rows rows = table.getRows();

      if (rows.hasMoreRows()) {
        Object[] row         = rows.nextRow();
        String[] columnNames = rows.getColumnNames();

        for (int i = 0; columnNames.length != i; i++) {
          if ((null != columnNames[i]) && (null != row[i])
                  &&!(columnNames[i].equals(""))
                  &&!(columnNames[i].equalsIgnoreCase("NULL"))) {
            if (columnNames[i].trim().equals(QUALIFIER_TYPE)) {
              properties.put(
                columnNames[i],
                new QualifierType(
                  factory, (String) row[i], (String) null));
            }
            else if (columnNames[i].trim().equals(HAS_CHILD)) {
              properties.put(columnNames[i],
                             new Boolean("Y".equals((String) row[i])));
            }
            else {
              properties.put(columnNames[i], row[i]);
            }
          }
        }
      }
    }

    return properties;
  }
  */
}
