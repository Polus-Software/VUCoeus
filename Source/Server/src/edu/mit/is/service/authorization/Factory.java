package edu.mit.is.service.authorization;

public class Factory
implements org.okip.service.authorization.api.Factory {
  private static final String NODATE = "        ";
  private static final String STATUS_DATE = "STATUS_DATE";
  private static final String DATABASEID = "Authorization";
  private static final String USERNAME = "Username";
  private static final String PASSWORD = "Password";
  private static final String CURRENTDSN = "CurrentDatabaseSymbolicName";
  private static final String COUNTDSN = "CountDatabaseSymbolicNames";
  private static final String DSN = "DatabaseSymbolicName";
  private static final String FORUSER = "ForUser";

  private java.util.Map properties = null;

  protected java.util.Map getProperties() {
    if(null == this.properties) {
      try {
        this.properties =
          org.okip.service.shared.api.FactoryManager.getPropertiesFromPRB(
            getClass().getPackage().getName());
      }
      catch(Exception ex) {
      }
    }
    return this.properties;
  }

  private org.okip.service.shared.api.Agent owner = null;
  /**
   * Return owner of this Factory.
   *
   *
   * @return org.okip.service.shared.api.Agent Owner
   *
   */
  public org.okip.service.shared.api.Agent getOwner() {
    return this.owner;
  }

  /**
   * Set the owner of this Factory.
   *
   *
   * @param Agent Owner
   *
   */
  public void setOwner(org.okip.service.shared.api.Agent owner) {
    this.owner = owner;
  }

  String[] databaseSymbolicNames = null;
  /**
   * Method getDatabaseSymbolicNames
   *
   *
   * @return String[]
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public String[] getDatabaseSymbolicNames()
  throws org.okip.service.authorization.api.AuthorizationException {
    if(null != databaseSymbolicNames) {
      return this.databaseSymbolicNames;
    }
    if(null != getProperties()) {
      String countStr = (String) getProperties().get( COUNTDSN ) ;
      if(null != countStr) {
        int count = Integer.parseInt(countStr) ;
        String[] names = new String[count+1];
        names[0] = (String) getProperties().get(CURRENTDSN);
        for(int i = 0; count != i; i++) {
          String prop = DSN + Integer.toString(i + 1) ;
          names[i+1] = (String) getProperties().get( prop ) ;
        }
        this.databaseSymbolicNames = names;
        return this.databaseSymbolicNames;
      }
    }
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.DATABASESYMBOLICNAMELISTDIDNOTLOAD);
  }

  String databaseSymbolicName = null;
  /**
   * Method getDatabaseSymbolicName
   *
   *
   * @return String
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public String getDatabaseSymbolicName()
  throws org.okip.service.authorization.api.AuthorizationException {
	if(null != this.databaseSymbolicName) {
      return this.databaseSymbolicName;
    }
    if(null == databaseSymbolicNames) {
      getDatabaseSymbolicNames();
    }
    if(null != databaseSymbolicNames) {
      return this.databaseSymbolicNames[0];
    }
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.MISSINGDATABASESYMBOLICNAME);
  }

  /**
   * Method setDatabaseSymbolicName
   *
   *
   * @param index - the index into the Database Symbolic Names array
   *                0 indicates the default name
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void setDatabaseSymbolicName(int index)
  throws org.okip.service.authorization.api.AuthorizationException {
    if(null == databaseSymbolicNames) {
      getDatabaseSymbolicNames();
    }
    if(null != databaseSymbolicNames) {
      if(index < databaseSymbolicNames.length) {
        this.databaseSymbolicName = this.databaseSymbolicNames[index];
        return;
      }
      throw new org.okip.service.authorization.api.AuthorizationException(
        org.okip.service.authorization.api.AuthorizationException.BADINDEXDSN);
    }
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.DATABASESYMBOLICNAMELISTDIDNOTLOAD);
  }

  //private Connection connection = null; Sabari
  /**
   * Method getConnection
   *
   *
   * @return Connection
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
    /**
   * Sabari
   * Need a Coeus impl
   * Might not need this function
   */
/*
  public Connection getConnection()
  throws org.okip.service.authorization.api.AuthorizationException {

    if(null == this.connection) {
      if(null != getProperties()) {
        String username =
          (String) getProperties().get(
            new String(databaseSymbolicName + USERNAME));
        String password =
          (String) getProperties().get(
            new String(databaseSymbolicName + PASSWORD));
        this.connection = Connection.create(
          getDatabaseSymbolicName(), username, password, DATABASEID);
      }
    }


    return this.connection;
  }
*/

  /**
   * Method createRolesDate
   *
   *
   * @param timestamp
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public static String createRolesDate(java.sql.Timestamp timestamp)
  throws org.okip.service.authorization.api.AuthorizationException {
    if (null != timestamp) {
      java.util.GregorianCalendar date = new java.util.GregorianCalendar();

      date.setTime((java.util.Date) timestamp);

      String rolesDate = "";
      int    year      = date.get(java.util.Calendar.YEAR);
      int    day       = date.get(java.util.Calendar.DAY_OF_MONTH);
      int    month     = date.get(java.util.Calendar.MONTH) + 1;

      if (month < 10) {
        rolesDate += "0";
      }

      rolesDate += Integer.toString(month);

      if (day < 10) {
        rolesDate += "0";
      }

      rolesDate += Integer.toString(day) + Integer.toString(year);

      return rolesDate;
    }

    return NODATE;
  }

  /**
   * Method createSqlTimestamp
   *
   *
   * @param rolesDate
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public static java.sql.Timestamp createSqlTimestamp(String rolesDate)
  throws org.okip.service.authorization.api.AuthorizationException {
    if (null != rolesDate) {
      int                         month =
        Integer.parseInt(rolesDate.substring(0, 2)) - 1;
      int                         day   =
        Integer.parseInt(rolesDate.substring(2, 4));
      int                         year  =
        Integer.parseInt(rolesDate.substring(4, 8));
      java.util.GregorianCalendar gc    =
        new java.util.GregorianCalendar(year, month, day);

      return new java.sql.Timestamp(gc.getTime().getTime());
    }

    return null;
  }

  /**
   * Method createAuthorization
   *
   *
   * @param person
   * @param function
   * @param qualifier
   * @param effectiveDate
   * @param expirationDate
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Authorization createAuthorization(
          org.okip.service.authorization.api.Agent person,
          org.okip.service.authorization.api.Function function,
          org.okip.service.authorization.api.Qualifier qualifier,
          java.sql.Timestamp effectiveDate,
          java.sql.Timestamp expirationDate)
  throws org.okip.service.authorization.api.AuthorizationException {
    return Authorization.create(
            this,
            person, function, qualifier, effectiveDate,
            expirationDate);
  }

  /**
   * Method createAuthorization
   *
   *
   * @param person
   * @param function
   * @param qualifier
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Authorization createAuthorization(
          org.okip.service.authorization.api.Agent person,
          org.okip.service.authorization.api.Function function,
          org.okip.service.authorization.api.Qualifier qualifier)
  throws org.okip.service.authorization.api.AuthorizationException {
    return Authorization.create(this,
            person, function, qualifier);
  }

  /**
   * Method deleteAuthorization
   *
   *
   * @param authorization
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void deleteAuthorization(
          org.okip.service.authorization.api.Authorization authorization)
  throws org.okip.service.authorization.api.AuthorizationException {
    ((Authorization) authorization).delete();
  }

  /**
   * Method updateAuthorization
   *
   *
   * @param authorization
   * @param person
   * @param function
   * @param qualifier
   * @param effectiveDate
   * @param expirationDate
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void updateAuthorization(
          org.okip.service.authorization.api.Authorization authorization,
          org.okip.service.authorization.api.Agent person,
          org.okip.service.authorization.api.Function function,
          org.okip.service.authorization.api.Qualifier qualifier,
          java.sql.Timestamp effectiveDate,
          java.sql.Timestamp expirationDate)
  throws org.okip.service.authorization.api.AuthorizationException {
    ((Authorization) authorization).update(
      person, function, qualifier, effectiveDate,
      expirationDate);
  }

  /**
   * Method getWhoCanDo
   *
   *
   * @param function
   * @param qualifier
   * @param isActiveNow
   *
   * @return org.okip.service.authorization.api.AgentsIterator
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.AgentsIterator getWhoCanDo(
          org.okip.service.authorization.api.Function function,
          org.okip.service.authorization.api.Qualifier qualifier,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
    return PersonsIterator.getWhoCanDo(
            this,
            function, qualifier, isActiveNow);
  }

  /**
   * Method getWhoCanDo
   *
   *
   * @param function
   * @param isActiveNow
   *
   * @return org.okip.service.authorization.api.AgentsIterator
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.AgentsIterator getWhoCanDo(
          org.okip.service.authorization.api.Function function,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
    return PersonsIterator.getWhoCanDo(
            this,
            function, isActiveNow);
  }

  /**
   * Method getWhoCanDo
   *
   *
   * @param functionType
   * @param isActiveNow
   *
   * @return org.okip.service.authorization.api.AgentsIterator
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.AgentsIterator getWhoCanDo(
          org.okip.service.authorization.api.FunctionType functionType,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
    return PersonsIterator.getWhoCanDo(
            this,
            functionType, isActiveNow);
  }

  /**
   * Method getWhoCanDo
   *
   *
   * @param functionType
   * @param qualifier
   * @param isActiveNow
   *
   * @return org.okip.service.authorization.api.AgentsIterator
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.AgentsIterator getWhoCanDo(
          org.okip.service.authorization.api.FunctionType functionType,
          org.okip.service.authorization.api.Qualifier qualifier,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
    return PersonsIterator.getWhoCanDo(
            this,
            functionType, qualifier, isActiveNow);
  }

  /* [chs 12/4/2001] This needs changes to the stored procedure to work.
          It is not clear if this should be implemented as it might
          return a large number of PersonsIterator. Jim Repa is considering this. */
  public org.okip.service.authorization.api.AgentsIterator getWhoCanDo (
          org.okip.service.authorization.api.Qualifier qualifier,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
    return PersonsIterator.getWhoCanDo(
            this,
            qualifier, isActiveNow);
  }

  /**
   * Method getAuthorizationsForDoingFWithQ
   *
   *
   * @param function
   * @param qualifier
   * @param isActiveNow
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.AuthorizationsIterator getAuthorizationsForDoingFWithQ(
          org.okip.service.authorization.api.Function function,
          org.okip.service.authorization.api.Qualifier qualifier,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
    return AuthorizationsIterator.getAuthorizationsForDoingFWithQ(
      this,
      function, qualifier, isActiveNow);
  }

  /**
   * Method getAuthorizationsForDoingFWithQ
   *
   *
   * @param functionType
   * @param qualifier
   * @param isActiveNow
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.AuthorizationsIterator getAuthorizationsForDoingFWithQ(
          org.okip.service.authorization.api.FunctionType functionType,
          org.okip.service.authorization.api.Qualifier qualifier,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
    return AuthorizationsIterator.getAuthorizationsForDoingFWithQ(
      this,
      functionType, qualifier, isActiveNow);
  }

  /**
   * Method getAuthorizations
   *
   *
   * @param person
   * @param function
   * @param qualifier
   * @param isActiveNow
   * @param expandQualifiers
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.AuthorizationsIterator getAuthorizations(
          org.okip.service.authorization.api.Agent person,
          org.okip.service.authorization.api.Function function,
          org.okip.service.authorization.api.Qualifier qualifier,
          boolean isActiveNow,
          boolean expandQualifiers)
  throws org.okip.service.authorization.api.AuthorizationException {
    return AuthorizationsIterator.getAuthorizations(
      this,
      person, function, qualifier, isActiveNow,
      expandQualifiers);
  }

  /**
   * Method getAuthorizations
   *
   *
   * @param person
   * @param functionType
   * @param qualifier
   * @param isActiveNow
   * @param expandQualifiers
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.AuthorizationsIterator getAuthorizations(
          org.okip.service.authorization.api.Agent person,
          org.okip.service.authorization.api.FunctionType functionType,
          org.okip.service.authorization.api.Qualifier qualifier,
          boolean isActiveNow,
          boolean expandQualifiers)
  throws org.okip.service.authorization.api.AuthorizationException {
    return AuthorizationsIterator.getAuthorizations(
      this,
      person, functionType, qualifier, isActiveNow,
      expandQualifiers);
  }

  /**
   * Method getAuthorizations
   *
   *
   * @param person
   * @param function
   * @param isActiveNow
   * @param expandQualifiers
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.AuthorizationsIterator getAuthorizations(
          org.okip.service.authorization.api.Agent person,
          org.okip.service.authorization.api.Function function,
          boolean isActiveNow,
          boolean expandQualifiers)
  throws org.okip.service.authorization.api.AuthorizationException {
    return AuthorizationsIterator.getAuthorizations(
      this,
      person, function, null, isActiveNow, expandQualifiers);
  }

  /**
   * Method getAuthorizations
   *
   *
   * @param person
   * @param functionType
   * @param isActiveNow
   * @param expandQualifiers
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.AuthorizationsIterator getAuthorizations(
          org.okip.service.authorization.api.Agent person,
          org.okip.service.authorization.api.FunctionType functionType,
          boolean isActiveNow,
          boolean expandQualifiers)
  throws org.okip.service.authorization.api.AuthorizationException {
    return AuthorizationsIterator.getAuthorizations(
      this,
      person, functionType, null, isActiveNow,
      expandQualifiers);
  }

  /**
   * Method getAuthorizations
   *
   *
   * @param person
   * @param isActiveNow
   * @param expandQualifiers
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.AuthorizationsIterator getAuthorizations(
          org.okip.service.authorization.api.Agent person, boolean isActiveNow,
          boolean expandQualifiers)
  throws org.okip.service.authorization.api.AuthorizationException {
    return AuthorizationsIterator.getAuthorizations(
      this,
      person, (Function)null, (Qualifier)null, isActiveNow, expandQualifiers);
  }

  /**
   * Method isAuthorized
   *
   *
   * @param person
   * @param function
   * @param qualifier
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean isAuthorized(
          org.okip.service.authorization.api.Agent person,
          org.okip.service.authorization.api.Function function,
          org.okip.service.authorization.api.Qualifier qualifier)
  throws org.okip.service.authorization.api.AuthorizationException {

    return Authorization.isAuthorized(
      this,
      person,
      function,
      qualifier);
  }

  /**
   * Method isAuthorized
   *
   * given a Person, FunctionType, and Qualifier
   * returns true if the Person is authorized to perform
   * at least one Function in the FunctionType with the Qualifier.
   *
   * NOT IMPLEMENTED FOR COEUS
   *
   * @param org.okip.service.authorization.api.Agent
   * @param org.okip.service.authorization.api.FunctionType
   * @param org.okip.service.authorization.api.Qualifier
   *
   * @return boolean
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean isAuthorized(
          org.okip.service.authorization.api.Agent person,
          org.okip.service.authorization.api.FunctionType functionType,
          org.okip.service.authorization.api.Qualifier qualifier)
  throws org.okip.service.authorization.api.AuthorizationException {
      
      return Authorization.isAuthorized(
      this,
      person,
      functionType,
      qualifier);
  }
  
  /**
   * Method isAuthorized
   *
   * given a Person, Function and FunctionType
   * returns true if the Person is authorized to perform
   * the given Function in the FunctionType.
   *
   *
   * @param org.okip.service.authorization.api.Agent
   * @param org.okip.service.authorization.api.Function
   *
   * @return boolean
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean isAuthorized(
          org.okip.service.authorization.api.Agent person,
          org.okip.service.authorization.api.Function function,
          org.okip.service.authorization.api.FunctionType functionType)
  throws org.okip.service.authorization.api.AuthorizationException {
      
      return Authorization.isAuthorized(
      this,
      person,
      function,
      functionType);
  }
  
  /**
   * Method newPerson
   *
   *
   * @param key
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Agent newPerson(java.io.Serializable key)
  throws org.okip.service.authorization.api.AuthorizationException {
    return Person.create(this,(String) key);
  }

  /**
   * Method isPersonFoundInAuthorization
   *
   *
   * @param person
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean isPersonFoundInAuthorization(
          org.okip.service.authorization.api.Agent person)
  throws org.okip.service.authorization.api.AuthorizationException {
    return ((Person) person).isFoundInAuthorization();
  }

  /**
   * Method refreshAgent
   *
   *
   * @param person
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void refreshAgent(org.okip.service.authorization.api.Agent person)
  throws org.okip.service.authorization.api.AuthorizationException {
    ((Person) person).refresh();
  }

  /**
   * Method newFunction
   *
   *
   * @param functionType
   * @param name
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Function newFunction(
          org.okip.service.authorization.api.FunctionType functionType,
          String name)
  throws org.okip.service.authorization.api.AuthorizationException {
    return new Function(this, name,
            (String) null, functionType,
            (org.okip.service.authorization.api.QualifierType) null);
  }

  /**
   * Method newFunction
   *
   *
   * @param functionTypeString
   * @param name
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Function newFunction(
          String functionTypeString, String name)
  throws org.okip.service.authorization.api.AuthorizationException {
    FunctionType functionType =
      new FunctionType(this,
        functionTypeString, (String) null);

    return new Function(this, name,
            (String) null, functionType,
            (org.okip.service.authorization.api.QualifierType) null);
  }

  /**
   * Method newFunction
   *
   *
   * @param key
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Function newFunction(java.io.Serializable key)
  throws org.okip.service.authorization.api.AuthorizationException {
    return new Function(this, key,
            (String) null, (String) null,
            (org.okip.service.authorization.api.FunctionType) null,
            (org.okip.service.authorization.api.QualifierType) null);
  }

  /**
   * Method newFunction
   *
   *
   * @param key
   * @param functionType
   * @param displayName
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Function newFunction(java.io.Serializable key,
          org.okip.service.authorization.api.FunctionType functionType,
          String displayName)
  throws org.okip.service.authorization.api.AuthorizationException {
    return new Function(this, key, displayName,
            (String) null, functionType,
            (org.okip.service.authorization.api.QualifierType) null);
  }

  /**
   * Method newFunctionType
   *
   *
   * @param functionTypeString
   * @param description
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.FunctionType newFunctionType(
          String functionTypeString, String description)
  throws org.okip.service.authorization.api.AuthorizationException {
    return new FunctionType(this,
            functionTypeString, description);
  }

  /**
   * Method createFunction
   *
   * NOT IMPLEMENTED FOR COEUS
   *
   *
   * @param key
   * @param displayName
   * @param description
   * @param functionType
   * @param qualifierType
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */

  public org.okip.service.authorization.api.Function createFunction(java.io.Serializable key,
          String displayName,
          String description,
          org.okip.service.authorization.api.FunctionType functionType,
          org.okip.service.authorization.api.QualifierType qualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {
     return null;
  }


  /**
   * Method deleteFunction
   * <P>
   * NOT IMPLEMENTED FOR COEUS
   * </P>
   * @param function
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void deleteFunction(
          org.okip.service.authorization.api.Function function)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method isFunctionFoundInAuthorization
   * <P>
   * NOT IMPLEMENTED IN COEUS
   * </p>
   * @param function
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean isFunctionFoundInAuthorization(
          org.okip.service.authorization.api.Function function)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method refreshFunction
   * <P>
   * NOT IMPLEMENTED IN COEUS
   * </P>
   * @param function
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void refreshFunction(
          org.okip.service.authorization.api.Function function)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method updateFunction
   * <p>
   * NOT IMPLEMENTED IN COEUS
   * </p>
   * @param function
   * @param newName
   * @param newDescription
   * @param newFunctionType
   * @param newQualifierType
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void updateFunction(
          org.okip.service.authorization.api.Function function, String newName,
          String newDescription,
          org.okip.service.authorization.api.FunctionType newFunctionType,
          org.okip.service.authorization.api.QualifierType newQualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method createFunctionType
   * <p>
   * NOT IMPLEMENTED IN COEUS
   * </p>
   *
   * @param name
   * @param description
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.FunctionType createFunctionType(String functionType,
          String description)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method deleteFunctionType
   *
   *
   * @param FunctionType
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void deleteFunctionType(
          org.okip.service.authorization.api.FunctionType functionType)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method isFunctionTypeFoundInAuthorization
   *
   *
   * @param functionType
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean isFunctionTypeFoundInAuthorization(
          org.okip.service.authorization.api.FunctionType functionType)
  throws org.okip.service.authorization.api.AuthorizationException {
    return ((FunctionType) functionType).isFoundInAuthorization();
  }

  /**
   * Method updateFunctionType
   *
   *
   * @param FunctionType
   * @param String
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void updateFunctionType(
          org.okip.service.authorization.api.FunctionType functionType,
          String newDescription)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method refreshFunctionType
   *
   *
   * @param org.okip.service.authorization.api.FunctionType
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void refreshFunctionType(
          org.okip.service.authorization.api.FunctionType functionType)
  throws org.okip.service.authorization.api.AuthorizationException {
    ((FunctionType) functionType).refresh();
  }

  /**
   * Method getFunctionTypes
   *
   *
   * @return org.okip.service.authorization.api.FunctionType[]
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.FunctionType[] getFunctionTypes()
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method getFunctionsOfType
   *
   *
   * @param functionType
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.FunctionsIterator getFunctionsOfType(
          org.okip.service.authorization.api.FunctionType functionType)
  throws org.okip.service.authorization.api.AuthorizationException {
    return
      FunctionsIterator.getFunctionsOfType(
        this, functionType);
  }

  /**
   * Method getFunction
   *
   * It may or may not exist in the Authorizations DB.
   *
   * @param org.okip.service.authorization.api.FunctionType
   * @param java.io.Serializable key
   *
   * @return org.okip.service.authorization.api.Function or null
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Function getFunction(
          org.okip.service.authorization.api.FunctionType functionType,
          java.io.Serializable key)
  throws org.okip.service.authorization.api.AuthorizationException {
    if(null != key) {
      org.okip.service.authorization.api.FunctionsIterator fi =
        FunctionsIterator.getFunctionsOfType(
          this, functionType);
      if(null != fi) {
        while(fi.hasNext()) {
          org.okip.service.authorization.api.Function function = fi.next();
          if(key.equals(function.getKey())) {
            return function;
          }
        }
      }
    }
    return null;
  }

  /**
   * Method getFunction
   *
   * It may or may not exist in the Authorizations DB.
   *
   * @param String
   * @param java.io.Serializable key
   *
   * @return org.okip.service.authorization.api.Function or null
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Function getFunction(
          String functionType,
          java.io.Serializable key)
  throws org.okip.service.authorization.api.AuthorizationException {
    if(null != key) {
      org.okip.service.authorization.api.FunctionsIterator fi =
        FunctionsIterator.getFunctionsOfType(
          this, new FunctionType(this, functionType, null));
      if(null != fi) {
        while(fi.hasNext()) {
          org.okip.service.authorization.api.Function function = fi.next();
          if(key.equals(function.getKey())) {
            return function;
          }
        }
      }
    }
    return null;
  }

  /**
   * Method getFunctionType
   *
   * It may or may not exist in the Authorizations DB.
   *
   * @param String - functionType
   *
   * @return org.okip.service.authorization.api.FunctionType
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.FunctionType getFunctionType(
          String functionType)
  throws org.okip.service.authorization.api.AuthorizationException {
    return new FunctionType(
        this, functionType, null);
  }

  /**
   * Method createQualifier
   *
   *
   * @param key
   * @param displayName
   * @param description
   * @param qualifierType
   * @param parent
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Qualifier createQualifier(
          java.io.Serializable key,
          String displayName,
          String description,
          org.okip.service.authorization.api.QualifierType qualifierType,
          org.okip.service.authorization.api.Qualifier parent)
  throws org.okip.service.authorization.api.AuthorizationException {
    if(null != parent) {
      return Qualifier.create(this,
              qualifierType, (String)key, description, parent);
    }
    throw new org.okip.service.authorization.api.AuthorizationException(
               org.okip.service.authorization.api.AuthorizationException.MANDATORYARGUMENTMISSING);
  }

  /**
   * Method createRootQualifier
   *
   *
   * @param key
   * @param displayName
   * @param description
   * @param qualifierType
   *
   * @return org.okip.service.authorization.api.Qualifier
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Qualifier createRootQualifier(
          java.io.Serializable key,
          String displayName,
          String description,
          org.okip.service.authorization.api.QualifierType qualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {
    return Qualifier.create(this,
            qualifierType, (String)key, description, null);
  }

  /**
   * Method deleteQualifier
   *
   *
   * @param qualifier
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void deleteQualifier(
          org.okip.service.authorization.api.Qualifier qualifier)
  throws org.okip.service.authorization.api.AuthorizationException {
    ((Qualifier) qualifier).delete();
  }

  /**
   * Method isQualifierFoundInAuthorization
   *
   *
   * @param qualifier
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean isQualifierFoundInAuthorization(
          org.okip.service.authorization.api.Qualifier qualifier)
  throws org.okip.service.authorization.api.AuthorizationException {
    return ((Qualifier) qualifier).isFoundInAuthorization();
  }

  /**
   * Method refreshQualifier
   *
   *
   * @param qualifier
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void refreshQualifier(
          org.okip.service.authorization.api.Qualifier qualifier)
  throws org.okip.service.authorization.api.AuthorizationException {
    ((Qualifier) qualifier).refresh();
  }

  /**
   * Method updateQualifier
   *
   *
   * @param qualifier
   * @param newQualifierCode
   * @param newDescription
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void updateQualifier(
          org.okip.service.authorization.api.Qualifier qualifier,
          String newQualifierCode, String newDescription)
  throws org.okip.service.authorization.api.AuthorizationException {
    ((Qualifier) qualifier).update(
      newQualifierCode, newDescription);
  }

  /**
   * Method createQualifierType
   *
   *
   * @param qualifierType
   * @param description
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.QualifierType createQualifierType(
          String qualifierType, String description)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method deleteQualifierType
   *
   *
   * @param QualifierType
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void deleteQualifierType(
          org.okip.service.authorization.api.QualifierType qualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method updateQualifierType
   *
   * updates Description in an existing qualifierType in the Authorizations DB.
   *
   * @param QualifierType
   * @param String
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void updateQualifierType(
          org.okip.service.authorization.api.QualifierType qualifierType,
          String newDescription)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method isQualifierTypeFoundInAuthorization
   *
   *
   * @param qualifierType
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean isQualifierTypeFoundInAuthorization(
          org.okip.service.authorization.api.QualifierType qualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {
    return ((QualifierType) qualifierType).isFoundInAuthorization();
  }

  /**
   * Method refreshQualifierType
   *
   *
   * @param qualifierType
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public void refreshQualifierType(
          org.okip.service.authorization.api.QualifierType qualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {
    ((QualifierType) qualifierType).refresh();
  }

  /**
   * Method getRootQualifier
   *
   *
   * @param qualifierType
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Qualifier getRootQualifier(
          org.okip.service.authorization.api.QualifierType qualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {
    return (org.okip.service.authorization.api.Qualifier) Qualifier.getRootQualifier(
      this, qualifierType);
  }

  /**
   * Method getQualifierChildren
   *
   *
   * @param qualifier
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.QualifiersIterator getQualifierChildren(
          org.okip.service.authorization.api.Qualifier qualifier)
  throws org.okip.service.authorization.api.AuthorizationException {
    return QualifiersIterator.getQualifierChildren(
      this, qualifier);
  }

  /**
   * Method newQualifier
   *
   *
   * @param qualifierType
   * @param qualifierCode
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Qualifier newQualifier(
          org.okip.service.authorization.api.QualifierType qualifierType,
          String qualifierCode)
  throws org.okip.service.authorization.api.AuthorizationException {
    return (org.okip.service.authorization.api.Qualifier) new Qualifier(this,
            qualifierCode, null, qualifierType);
  }

  /**
   * Method newQualifier
   *
   *
   * @param key
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Qualifier newQualifier(java.io.Serializable key)
  throws org.okip.service.authorization.api.AuthorizationException {
    return (org.okip.service.authorization.api.Qualifier) new Qualifier(this, key, null, null, null);
  }

  /**
   * Method newQualifier
   *
   *
   * @param qualifierType
   * @param qualifierCode
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Qualifier newQualifier(
          String qualifierType, String qualifierCode)
  throws org.okip.service.authorization.api.AuthorizationException {
    QualifierType qt =
      new QualifierType(this,
        qualifierType, (String) null);

    return (org.okip.service.authorization.api.Qualifier) new Qualifier(this,
            qualifierCode, null, qt);
  }

  /**
   * Method newQualifierType
   *
   *
   * @param qualifierType
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.QualifierType newQualifierType(
          String qualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {
    return (org.okip.service.authorization.api.QualifierType)
      new QualifierType(this, qualifierType, (String) null);
  }

  /**
   * Method getAgent
   *
   * The person may or may not exist in the Authorizations DB.
   *
   * @param java.io.Serializable key - key
   *
   * @return org.okip.service.authorization.api.Agent or null
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Agent getAgent(java.io.Serializable key)
  throws org.okip.service.authorization.api.AuthorizationException {
    org.okip.service.authorization.api.Agent person = newPerson(key);
    person.refresh();
    return person;
  }

  /**
   * Method getQualifierTypes
   *
   * returns QualifierTypes - available in the Authorizations DB.
   *
   * @return org.okip.service.authorization.api.QualifierType[]
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.QualifierType[] getQualifierTypes()
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method getQualifier
   *
   * The instance may or may not exist in the Authorizations DB.
   *
   * @param org.okip.service.authorization.api.QualifierType
   * @param java.io.Serializable
   *
   * @return org.okip.service.authorization.api.Qualifier or null
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Qualifier getQualifier(
          org.okip.service.authorization.api.QualifierType qualifierType,
          java.io.Serializable qualifierKey)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method getQualifier
   *
   * The instance may or may not exist in the Authorizations DB.
   *
   * @param String
   * @param java.io.Serializable
   *
   * @return org.okip.service.authorization.api.Qualifier or null
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Qualifier getQualifier(
          String qualifierTypeKeyword,
          java.io.Serializable qualifierKey)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method getQualifier
   *
   * The instance may or may not exist in the Authorizations DB.
   *
   * @param java.io.Serializable key
   *
   * @return org.okip.service.authorization.api.Qualifier or null
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Qualifier getQualifier(
          java.io.Serializable key)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method getQualifierType
   *
   * The instance may or may not exist in the Authorizations DB.
   *
   * @param String
   *
   * @return org.okip.service.authorization.api.QualifierType or null
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.QualifierType getQualifierType(
          String qualifierType)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.shared.api.Exception.UNIMPLEMENTED) ;
  }

  /**
   * Method getUserInfo
   *
   * The instance may or may not exist in the Authorizations DB.
   *
   * @param String
   *
   * @return org.okip.service.authorization.api.QualifierType or null
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Agent getUserInfo(
          Person person)
  throws org.okip.service.authorization.api.AuthorizationException {
    return Authorization.getUserInfo(
      this,person );
  }  
  
  /**
   * Method updateUserInfo
   *
   * The instance may or may not exist in the Authorizations DB.
   *
   * @param boolean
   *
   * @return org.okip.service.authorization.api.QualifierType or null
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean updateUserInfo(
          Person person)
  throws org.okip.service.authorization.api.AuthorizationException {
    return Authorization.addUpdDeleteUsers(
      this,person );
  }    
  
  /**
   * Method updateRoleRights
   *
   * The instance may or may not exist in the Authorizations DB.
   *
   * @param boolean
   *
   * @return org.okip.service.authorization.api.QualifierType or null
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean updateRoleRights(
          Function function)
  throws org.okip.service.authorization.api.AuthorizationException {
    return Authorization.addUpdDeleteRoleRights(
      this,function );
  }      
  
}
