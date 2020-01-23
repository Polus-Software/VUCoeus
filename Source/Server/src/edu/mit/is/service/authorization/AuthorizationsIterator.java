package edu.mit.is.service.authorization;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import java.util.HashMap;
import java.util.Vector;


public class AuthorizationsIterator
implements org.okip.service.authorization.api.AuthorizationsIterator {
    
    private Vector authorizationList = new Vector();
    int currentIndex = 0;
    
    private static final String   ALGORITHM_SIMPLE =   "SIMPLE";
  private static final String   ALGORITHM_F_WITH_Q =  "F_WITH_Q";
  private static final String   ALGORITHM_WHO_CAN_DO =   "WHO_CAN_DO";
  private static final String   GETAUTHORIZATIONSQUERYSQL = "ROLESAPI_GET_SQL_AUTH1";
  private static final String   DONT_EXPAND_FUNCTIONS =  "N";
  private static final String   DONT_EXPAND_QUALIFIERS = "N";

  protected Factory factory =   null;

  /**
   * Method hasNext
   *
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public boolean hasNext()
  throws org.okip.service.authorization.api.AuthorizationException {
      
      if ((null == authorizationList) || (authorizationList.size() == 0)) {
          return false;
      } else if (currentIndex >= authorizationList.size()) {
          return false;
      }
      else {
          return true;
      }
      
  }

  /**
   * Method next
   *
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public org.okip.service.authorization.api.Authorization next()
  throws org.okip.service.authorization.api.AuthorizationException {
      
      if (null != authorizationList) {
          if (currentIndex >= authorizationList.size() ) {
              return null;
          } else {
              currentIndex++;
              return (Authorization) authorizationList.get(currentIndex - 1);
          }
          
      }
      return null;
  }

  private boolean hideImpliedAuthorizations = false ;

  /**
   * Method hideImpliedAuthorizations
   *
   */
  public void hideImpliedAuthorizations() {
    this.hideImpliedAuthorizations = true;
  }

  /**
   * Method showImpliedAuthorizations
   *
   */
  public void showImpliedAuthorizations() {
    this.hideImpliedAuthorizations = false;
  }

  /**
   * Method getAuthorizations
   *
   *
   * @param Factory
   * @param Person
   * @param FunctionType
   * @param boolean - isActiveNow
   * @param int - start
   * @param int - number
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public static org.okip.service.authorization.api.AuthorizationsIterator getAuthorizations(
    Factory factory,
    org.okip.service.authorization.api.Agent person,
    org.okip.service.authorization.api.FunctionType functionType,
    boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
    return getAuthorizationsInternal
    (factory, ALGORITHM_SIMPLE,
      ((Person)person).getKerberosName(),
      (String) null, (String) null,
      (java.io.Serializable) null, (String) null, (String) null,
      (isActiveNow ? "Y" : "N"), "Y" );
  }

  /**
   * Given a Function and a Qualifier
   * returns an enumeration of all AuthorizationsIterator that would allow people
   * to do the Function with Qualifier. This method differs from the simple
   * form of getAuthorizations in that this method looks for any Authorization
   * that permits doing the Function with the Qualifier even if the Authorization's
   * Qualifier happens to be a parent of this Qualifier argument.
   */
  public static org.okip.service.authorization.api.AuthorizationsIterator getAuthorizationsForDoingFWithQ(
    Factory factory,
    org.okip.service.authorization.api.Function function,
    org.okip.service.authorization.api.Qualifier qualifier,
    boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
    return getAuthorizationsInternal(factory, ALGORITHM_F_WITH_Q,
      null, function, qualifier, isActiveNow,
      true);
  }

  /**
   * Given a FunctionType and a Qualifier
   * returns an enumeration of all AuthorizationsIterator that would allow people
   * to do Functions in the FunctionType with Qualifier. This method differs
   * from getAuthorizations in that this method looks for any Authorization
   * that permits doing the Function with the Qualifier even if the
   * Authorization's Qualifier happens to be a parent of the Qualifier argument.
   */
  public static org.okip.service.authorization.api.AuthorizationsIterator getAuthorizationsForDoingFWithQ(
    Factory factory,
    org.okip.service.authorization.api.FunctionType functionType,
    org.okip.service.authorization.api.Qualifier qualifier,
    boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
    return getAuthorizationsInternal(factory, ALGORITHM_F_WITH_Q,
      (String) null,
      (String) null,
      (String) ((null == functionType)
        ? null
        : ((FunctionType)functionType).getKeyword()),
      (java.io.Serializable) ((null == qualifier)
        ? null
        : ((Qualifier) qualifier).getId()),
      (String) ((null == qualifier)
        ? null
        : (String) qualifier.getKey()),
      (String) ((null == qualifier)
        ? null
        : ((null == qualifier.getQualifierType())
          ? null
          : ((QualifierType)qualifier.getQualifierType()).getKeyword())),
      (String) (isActiveNow
        ? "Y"
        : "N"),
      (String) DONT_EXPAND_QUALIFIERS);
  }

  /**
   * Given at a Person, a Function, and a Qualifier
   * returns an enumeration of matching authorizations.
   */
  public static org.okip.service.authorization.api.AuthorizationsIterator getAuthorizations(
    Factory factory,
    org.okip.service.authorization.api.Agent person,
    org.okip.service.authorization.api.Function function,
    org.okip.service.authorization.api.Qualifier qualifier,
    boolean isActiveNow,
    boolean expandQualifiers)
  throws org.okip.service.authorization.api.AuthorizationException {
    return getAuthorizationsInternal(factory, ALGORITHM_SIMPLE,
        person, function, qualifier,
        isActiveNow,
        expandQualifiers);
  }

  /**
   * Given at a Person, a FunctionType, and a Qualifier
   * returns an enumeration of matching authorizations.
   */
  public static org.okip.service.authorization.api.AuthorizationsIterator getAuthorizations(
    Factory factory,
    org.okip.service.authorization.api.Agent person,
    org.okip.service.authorization.api.FunctionType functionType,
    org.okip.service.authorization.api.Qualifier qualifier,
    boolean isActiveNow,
    boolean expandQualifiers)
  throws org.okip.service.authorization.api.AuthorizationException {
    return getAuthorizationsInternal(
	  factory,
	  ALGORITHM_SIMPLE,
      (String) ((null == person)
        ? null
        : ((Person) person).getKerberosName()),
      (String) null,
      (String) ((null == functionType)
        ? null
        : ((FunctionType)functionType).getKeyword()),
      (java.io.Serializable) ((null == qualifier)
        ? null
        : ((Qualifier) qualifier).getId()),
      (String) ((null == qualifier)
        ? null
        : (String) qualifier.getKey()),
      (String) ((null == qualifier)
        ? null
        : ((null == qualifier.getQualifierType())
          ? null
          : ((QualifierType)qualifier.getQualifierType()).getKeyword())),
      (String) (isActiveNow
        ? "Y"
        : "N"),
      (expandQualifiers
        ? "Y"
        : "N"));
  }

  /**
   *
   * @param factory
   * @param isActiveNow
   * @param expandQualifiers
   * @return an enumeration of matching authorizations
   * (should return 1 authorization, unless user has requested
   * expansion by Functions or Qualifiers).
   */
  public static org.okip.service.authorization.api.AuthorizationsIterator getAuthorizations(
    Factory factory,
    boolean isActiveNow,
    boolean expandQualifiers)
  throws org.okip.service.authorization.api.AuthorizationException {
    return getAuthorizationsInternal(factory, ALGORITHM_SIMPLE,
        null, null, null,
        isActiveNow,
        expandQualifiers);
  }

  private static org.okip.service.authorization.api.AuthorizationsIterator getAuthorizationsInternal(
  Factory factory,
  String algorithm,
  org.okip.service.authorization.api.Agent person,
  org.okip.service.authorization.api.Function function,
  org.okip.service.authorization.api.Qualifier qualifier,
  boolean isActiveNow,
  boolean expandQualifiers)
  throws org.okip.service.authorization.api.AuthorizationException {
      
      DBEngineImpl dbEngine = new DBEngineImpl();
      String userID = "";
      String qualifierTypeKey = "";
      String descendFlag = "";
      
      Vector result = new Vector();
      Vector param = new Vector();
      Vector authList = new Vector();
      
      Function authFunction;
      FunctionType  authFunctionType;
      Qualifier authQualifier = null;
      Person authPerson;
      QualifierType qualifierType;
     
      try {
          if ((person != null) && (function == null) && (qualifier == null)) {
              //Looking for all authorizations for a person in the entire system
              if (dbEngine != null) {
                  param.addElement(new Parameter("AW_USER_ID", "String", (String) person.getKey()));
                  result = dbEngine.executeRequest("Coeus",
                  "call GET_ALL_ROLES_FOR_USER ( << AW_USER_ID >> , <<OUT RESULTSET rset>> )",
                  "Coeus", param);
                  int rowCount = result.size();
                  
                  if (rowCount > 0) {
                      AuthorizationsIterator authorizations = new AuthorizationsIterator();
                      
                      for (int rowIndex =0; rowIndex < rowCount; rowIndex++) {
                          HashMap resultSet;
                          resultSet = (HashMap) result.get(rowIndex);
                          
                          authFunction = (Function) factory.newFunction("ROLE", resultSet.get("ROLE_ID").toString());
                          authFunction.setDescription(resultSet.get("ROLE_NAME").toString());
                          authFunctionType = (FunctionType) factory.newFunctionType("ROLE_TYPE", resultSet.get("ROLE_TYPE").toString());
                          authFunction.setFunctionType(authFunctionType);
                          authQualifier = (Qualifier) factory.newQualifier("UNIT_NUMBER", resultSet.get("UNIT_NUMBER").toString());
                          authQualifier.setDescription(resultSet.get("UNIT_NAME").toString());
                          java.util.Properties properties    = new java.util.Properties();
                          
                          properties.put("AUTHORIZATION_ID", "");
                          properties.put("AGENT", person);
                          properties.put("FUNCTION", authFunction);
                          properties.put("QUALIFIER", authQualifier);
                          properties.put("STATUS", resultSet.get("STATUS_FLAG").toString());
                          authList.add(new Authorization(factory, properties));
                      }
                      authorizations.setAuthList(authList);
                      return authorizations;
                  } 
              }
          } else if ((person == null) && (function == null) && (qualifier != null)) {
              //Need to get a list of all authorizations in a qualifier
              qualifierType = (QualifierType) qualifier.getQualifierType();
              
              if (qualifierType == null) {
                  qualifierTypeKey = "UNIT";
              } else {
                  qualifierTypeKey = qualifierType.getKeyword();
              }
              
              String strQualifierDesc ; 
              strQualifierDesc = qualifier.getDescription();
              
             
              if (qualifierTypeKey.equalsIgnoreCase("UNIT")){
                  if(strQualifierDesc.equalsIgnoreCase("USER_ROLES")){
                      if (dbEngine != null) {
                          param.addElement(new Parameter("AW_UNIT_NUMBER", "String", (String) qualifier.getKey()));                  

                          result = dbEngine.executeRequest("Coeus",
                          "call dw_get_user_roles_for_unit ( << AW_UNIT_NUMBER >> , <<OUT RESULTSET rset>> )",
                          "Coeus", param);

                          int rowCount = result.size();
                          if (rowCount > 0) {
                              AuthorizationsIterator authorizations = new AuthorizationsIterator();

                              for (int rowIndex =0; rowIndex < rowCount; rowIndex++) {
                                  HashMap resultSet;
                                  resultSet = (HashMap) result.get(rowIndex);

                                  authPerson = (Person) factory.newPerson(resultSet.get("USER_ID").toString());
                                  authPerson.setName(resultSet.get("USER_NAME").toString());
                                  authPerson.setStatusCode(resultSet.get("STATUS").toString());

                                  authFunction = (Function) factory.newFunction("ROLE", resultSet.get("ROLE_ID").toString());
                                  authFunction.setDescription(resultSet.get("ROLE_NAME").toString());

                                  descendFlag = resultSet.get("DESCEND_FLAG").toString();

                                  if (descendFlag.equalsIgnoreCase("Y")) {
                                      authFunctionType = (FunctionType) factory.newFunctionType(resultSet.get("ROLE_TYPE").toString(), "DESCEND");
                                  } else {
                                      authFunctionType = (FunctionType) factory.newFunctionType(resultSet.get("ROLE_TYPE").toString(), "");
                                  }

                                  authFunction.setFunctionType(authFunctionType);

                                  authQualifier = (Qualifier) factory.newQualifier("UNIT", resultSet.get("UNIT_NUMBER").toString());

                                  java.util.Properties properties    = new java.util.Properties();

                                  properties.put("AUTHORIZATION_ID", "");
                                  properties.put("PERSON", authPerson);
                                  properties.put("FUNCTION", authFunction);
                                  properties.put("QUALIFIER", authQualifier);
                                  authList.add(new Authorization(factory, properties));
                              }
                              authorizations.setAuthList(authList);
                              return authorizations;

                          } else {
                              return null;
                          }
                      }
                  } else if(strQualifierDesc.equalsIgnoreCase("USERS")){ 
                      //To get all Users for the given Unit. - Prasanna
                      if (dbEngine != null) {
                          param.addElement(new Parameter("AW_UNIT_NUMBER", "String", (String) qualifier.getKey()));                  

                          result = dbEngine.executeRequest("Coeus",
                          "call GET_USERS_FOR_UNIT ( << AW_UNIT_NUMBER >> , <<OUT RESULTSET rset>> )",
                          "Coeus", param);

                          int rowCount = result.size();
                          String nonMITPerson = "";
                          java.sql.Timestamp timeStamp;  
                          String strUser;  
                          String strUnitName = "";
                          if (rowCount > 0) {
                              AuthorizationsIterator authorizations = new AuthorizationsIterator();

                              for (int rowIndex =0; rowIndex < rowCount; rowIndex++) {
                                  HashMap resultSet;
                                  resultSet = (HashMap) result.get(rowIndex);

                                  authPerson = (Person) factory.newPerson(resultSet.get("USER_ID").toString());
                                  if(resultSet.get("USER_NAME") != null) {
                                    authPerson.setName(resultSet.get("USER_NAME").toString());
                                  }
                                  if(resultSet.get("STATUS") != null) {
                                    authPerson.setStatusCode(resultSet.get("STATUS").toString());
                                  }

                                  if(resultSet.get("UNIT_NUMBER") != null) {
                                    authQualifier = (Qualifier) factory.newQualifier("UNIT", resultSet.get("UNIT_NUMBER").toString());
                                  }
                                  if(resultSet.get("UNIT_NAME") != null) {
                                    strUnitName = (String)resultSet.get("UNIT_NAME");
                                  }

                                  java.util.Properties properties    = new java.util.Properties();

                                  if(resultSet.get("NON_MIT_PERSON_FLAG") != null) {
                                    nonMITPerson = (String)resultSet.get("NON_MIT_PERSON_FLAG");
                                  }
                                  timeStamp = (java.sql.Timestamp)resultSet.get("UPDATE_TIMESTAMP");
                                  strUser = (String)resultSet.get("UPDATE_USER");

                                  properties.put("AUTHORIZATION_ID", "");
                                  properties.put("PERSON", authPerson);
                                  properties.put("QUALIFIER", authQualifier);
                                  properties.put("PERSONID",resultSet.get("PERSON_ID")==null ? "" : resultSet.get("PERSON_ID").toString());
                                  properties.put("NON_MIT_PERSON_FLAG", nonMITPerson);
                                  properties.put("UNIT_NAME", strUnitName);
                                  properties.put("UPDATE_TIMESTAMP", timeStamp);
                                  properties.put("UPDATE_USER", strUser);

                                  authList.add(new Authorization(factory, properties));
                              }
                              authorizations.setAuthList(authList);
                              return authorizations;

                          } else {
                              return null;
                          }
                      }                      
                  } else if(strQualifierDesc.equalsIgnoreCase("ROLES")){
                      //To get all Roles for the given Unit. - Prasanna
                      if (dbEngine != null) {
                          param.addElement(new Parameter("AW_UNIT_NUMBER", "String", (String) qualifier.getKey()));                  

                          result = dbEngine.executeRequest("Coeus",
                          "call GET_ROLES_FOR_UNIT ( << AW_UNIT_NUMBER >> , <<OUT RESULTSET rset>> )",
                          "Coeus", param);

                          int rowCount = result.size();
                          String statusFlag;
                          java.sql.Timestamp timeStamp;
                          String strUser;
                          String strRoleType;
                          if (rowCount > 0) {
                              AuthorizationsIterator authorizations = new AuthorizationsIterator();

                              for (int rowIndex =0; rowIndex < rowCount; rowIndex++) {
                                  HashMap resultSet;
                                  resultSet = (HashMap) result.get(rowIndex);
                                  authFunction = (Function) factory.newFunction("ROLE", resultSet.get("ROLE_ID").toString());
                                  authFunction.setDescription(resultSet.get("DESCRIPTION").toString());
                                  authQualifier = (Qualifier) factory.newQualifier("UNIT", (String) qualifier.getKey());
                                  strRoleType = resultSet.get("ROLE_TYPE").toString();
                                  authFunctionType = (FunctionType) factory.newFunctionType("ROLE_TYPE", resultSet.get("ROLE_TYPE").toString());
                                  authFunction.setFunctionType(authFunctionType);                                                            
                                  
                                  descendFlag = (String)resultSet.get("DESCEND_FLAG");
                                  statusFlag = (String)resultSet.get("STATUS_FLAG");
                                  timeStamp = (java.sql.Timestamp)resultSet.get("UPDATE_TIMESTAMP");
                                  strUser = (String)resultSet.get("UPDATE_USER");
                                  
                                  java.util.Properties properties    = new java.util.Properties();
                                    
                                  properties.put("AUTHORIZATION_ID", "");
                                  properties.put("FUNCTION", authFunction);
                                  properties.put("QUALIFIER", authQualifier);
                                  properties.put("ROLE_NAME", resultSet.get("ROLE_NAME").toString());
                                  properties.put("DESCEND_FLAG", descendFlag);
                                  properties.put("STATUS_FLAG", statusFlag);
                                  properties.put("UPDATE_TIMESTAMP", timeStamp);
                                  properties.put("UPDATE_USER", strUser);
                                  

                                  authList.add(new Authorization(factory, properties));
                              }
                              authorizations.setAuthList(authList);
                              return authorizations;
                          } else {
                              return null;
                          }
                      }                      
                  }
                  
              } else if (qualifierTypeKey.equalsIgnoreCase("PROPOSAL")) {
                  if (dbEngine != null) {
                      param.addElement(new Parameter("AW_PROPOSAL_NUMBER", "String", (String) qualifier.getKey()));                  
                  
                      result = dbEngine.executeRequest("Coeus",
                      "call get_all_auth_for_proposal ( << AW_PROPOSAL_NUMBER >> , <<OUT RESULTSET rset>> )",
                      "Coeus", param);

                      int rowCount = result.size();
                      
                      if (rowCount > 0) {
                          AuthorizationsIterator authorizations = new AuthorizationsIterator();
                          
                          for (int rowIndex =0; rowIndex < rowCount; rowIndex++) {
                              HashMap resultSet;
                              resultSet = (HashMap) result.get(rowIndex);
                              
                              authPerson = (Person) factory.newPerson(resultSet.get("USER_ID").toString());
                              authPerson.setName(resultSet.get("USER_NAME").toString());
                              authPerson.setStatusCode(resultSet.get("STATUS").toString());
                              
                              authFunction = (Function) factory.newFunction("ROLE", resultSet.get("ROLE_ID").toString());
                              authFunction.setDescription(resultSet.get("ROLE_NAME").toString());
                              
                              authQualifier = (Qualifier) factory.newQualifier("PROPOSAL", resultSet.get("PROPOSAL_NUMBER").toString());
                              
                              java.util.Properties properties    = new java.util.Properties();
                                                           
                              properties.put("AUTHORIZATION_ID", "");
                              properties.put("PERSON", authPerson);
                              properties.put("FUNCTION", authFunction);
                              properties.put("QUALIFIER", authQualifier);
                              authList.add(new Authorization(factory, properties));
                          }
                          authorizations.setAuthList(authList);
                          return authorizations;
                          
                      } else {
                          return null;
                      }
                  }
              } else {
                  throw new org.okip.service.authorization.api.AuthorizationException(
                    org.okip.service.authorization.api.AuthorizationException.UNIMPLEMENTED);
              }
          } else if ((person == null) && (function != null) && (qualifier == null)) {
               //To Get all Role Rights for the given Role Id - Prasanna
              authFunctionType = (FunctionType) function.getFunctionType();
              String functionTypeKey = authFunctionType.getKeyword();
              java.sql.Timestamp timeStamp;  
              String strUser;
              //Looking for Rights for the Given Role
              if (functionTypeKey.equalsIgnoreCase("ROLE")) {              
                  if (dbEngine != null) {
                      param.addElement(new Parameter("AW_ROLE_ID", "String", (String) function.getKey()));
                      result = dbEngine.executeRequest("Coeus",
                      "call DW_GET_ROLE_RIGHTS_FOR_ROLE ( << AW_ROLE_ID >> , <<OUT RESULTSET rset>> )",
                      "Coeus", param);
                      int rowCount = result.size();
                      if (rowCount > 0) {
                          AuthorizationsIterator authorizations = new AuthorizationsIterator();
                          for (int rowIndex =0; rowIndex < rowCount; rowIndex++) {
                              HashMap resultSet;
                              resultSet = (HashMap) result.get(rowIndex);

                              authFunction = (Function) factory.newFunction("RIGHT", resultSet.get("RIGHT_ID").toString());
                              descendFlag = resultSet.get("DESCEND_FLAG").toString();
                              timeStamp = (java.sql.Timestamp)resultSet.get("UPDATE_TIMESTAMP");
                              strUser = (String)resultSet.get("UPDATE_USER");
                              authFunction.setDescription(resultSet.get("DESCRIPTION").toString());
                              
                              if (descendFlag.equalsIgnoreCase("Y")) {
                                  authFunctionType = (FunctionType) factory.newFunctionType(resultSet.get("RIGHT_TYPE").toString(), "Y");
                              } else {
                                  authFunctionType = (FunctionType) factory.newFunctionType(resultSet.get("RIGHT_TYPE").toString(), "N");
                              }
                              
                              authFunction.setFunctionType(authFunctionType);                              

                              //authQualifier = (Qualifier) factory.newQualifier("UNIT", resultSet.get("UNIT_NAME").toString());

                              java.util.Properties properties    = new java.util.Properties();

                              properties.put("AUTHORIZATION_ID", "");
                              //properties.put("AGENT", person);
                              properties.put("FUNCTION", authFunction);
                              properties.put("UPDATE_USER",strUser);
                              properties.put("UPDATE_TIMESTAMP",timeStamp);
                              
                              //properties.put("QUALIFIER", authQualifier);
                              authList.add(new Authorization(factory, properties));
                          }
                          authorizations.setAuthList(authList);
                          return authorizations;
                      } 

                  }
              }
          }
      } catch (Exception e) {
          throw new org.okip.service.authorization.api.AuthorizationException(
          e.getMessage());
      }
      return null;
  }

  private static org.okip.service.authorization.api.AuthorizationsIterator getAuthorizationsInternal(
  Factory factory,
  String algorithm, String kerberosName,
  String functionName, String functionType,
  java.io.Serializable qualifierId, String qualifierCode, String qualifierType,
  String isActiveNowStr,
  String expandQualifiersStr)
  throws org.okip.service.authorization.api.AuthorizationException {
    throw new org.okip.service.authorization.api.AuthorizationException(
    org.okip.service.authorization.api.AuthorizationException.UNIMPLEMENTED);
  }

  protected static org.okip.service.authorization.api.AuthorizationsIterator getAuthorizations(
    Factory factory,
    String sqlQuery)
  throws org.okip.service.authorization.api.AuthorizationException {
  
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.UNIMPLEMENTED);
  }
 
  public void setAuthList(Vector authList)  {
      authorizationList = authList;
  }
   
   public int getAuthorizationCount() {
       if (null != authorizationList) {
           return authorizationList.size();
       } else  {
           return 0;
       }
   }
}
