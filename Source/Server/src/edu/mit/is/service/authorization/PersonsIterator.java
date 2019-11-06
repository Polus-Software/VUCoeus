package edu.mit.is.service.authorization;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import java.util.HashMap;
import java.util.Vector;


public class PersonsIterator
implements org.okip.service.authorization.api.AgentsIterator{
//  protected Rows                rows         =    null; //Sabari
  protected Factory factory =
    null;
  private Vector personList = new Vector();
  int currentIndex = 0;
  
  /**
   * Method hasNext
   *
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
   /**
    * Sabari
    * Need Coeus impl
    */
  public boolean hasNext()
  throws org.okip.service.authorization.api.AuthorizationException {
      
      if ((null == personList) || (personList.size() == 0)) {
          return false;
      } else if (currentIndex >= personList.size()) {
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
  public org.okip.service.authorization.api.Agent next()
  throws org.okip.service.authorization.api.AuthorizationException {
      
      if (null != personList) {
          if (currentIndex >= personList.size() ) {
              return null;
          } else {
              currentIndex++;
              return (Person) personList.get(currentIndex - 1);
          }
          
      }
      
      return null;
  }

  /**
   * Method getWhoCanDo
   *
   *
   * @param factory
   * @param function
   * @param isActiveNow
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */

   /**
    * Sabari
    * need not implement
    */

  public static org.okip.service.authorization.api.AgentsIterator getWhoCanDo(
          Factory factory,
          org.okip.service.authorization.api.Function function,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {

  //  return getPeopleInternal(factory, function, null, isActiveNow, true);

  return null;
  }

  /**
   * Method getWhoCanDo
   *
   *
   * @param factory
   * @param function
   * @param qualifier
   * @param isActiveNow
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */

   /**
    * Sabari
    * not needed in Coeus
    */
  public static org.okip.service.authorization.api.AgentsIterator getWhoCanDo(
          Factory factory,
          org.okip.service.authorization.api.Function function,
          org.okip.service.authorization.api.Qualifier qualifier,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
      return getPeopleInternal(
          factory,
          function,
          qualifier,
          isActiveNow,
          false);
  }

  /**
   * Method getWhoCanDo
   *
   *
   * @param factory
   * @param qualifier
   * @param isActiveNow
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
   /**
    * Sabari
    * Not needed in COeus
    */
  public static org.okip.service.authorization.api.AgentsIterator getWhoCanDo(
          Factory factory,
          org.okip.service.authorization.api.Qualifier qualifier,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
  /**
    return getPeopleInternal(factory, null, qualifier, isActiveNow,
                             true);
  */
  return null;
  }

  /**
   * Method getWhoCanDo
   *
   *
   * @param factory
   * @param functionType
   * @param isActiveNow
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
   /**
    * Sabari
    * need not implement
    */
  public static org.okip.service.authorization.api.AgentsIterator getWhoCanDo(
          Factory factory,
          org.okip.service.authorization.api.FunctionType functionType,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
  /*
    return getPeopleInternal(factory, (String) null,
      ((edu.mit.is.service.authorization.FunctionType)  functionType).getKeyword(), (Object) null,
      (String) null, (String) null,
      isActiveNow
        ? "Y"
        : "N", (String) null);
*/
 return null;
  }

  /**
   * Method getWhoCanDo
   *
   *
   * @param factory
   * @param functionType
   * @param qualifier
   * @param isActiveNow
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */

  public static org.okip.service.authorization.api.AgentsIterator getWhoCanDo(
          Factory factory,
          org.okip.service.authorization.api.FunctionType functionType,
          org.okip.service.authorization.api.Qualifier qualifier,
          boolean isActiveNow)
  throws org.okip.service.authorization.api.AuthorizationException {
    return getPeopleInternal(factory, (String) null,
      ((edu.mit.is.service.authorization.FunctionType) functionType).getKeyword(),
      (Object) null,
      (String) qualifier.getKey(),
      ((edu.mit.is.service.authorization.QualifierType) qualifier.getQualifierType()).getKeyword(),
      isActiveNow
        ? "Y"
        : "N", (String) null);
  }

  /**
   * Method getPeopleInternal
   *
   *
   * @param factory
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
   /**
    * Sabari
    * Don't need this
    */
  private static org.okip.service.authorization.api.AgentsIterator getPeopleInternal(
          Factory factory,
          org.okip.service.authorization.api.Function function,
          org.okip.service.authorization.api.Qualifier qualifier,
          boolean isActiveNow,
          boolean expandQualifiers)
  throws org.okip.service.authorization.api.AuthorizationException {
      DBEngineImpl dbEngine = new DBEngineImpl();
      
      FunctionType functionType;
      QualifierType qualifierType;
      boolean checkingForRole = false;
      String    functionTypeKey = "";
      String    qualifierTypeKey= "";
      String    userID;
      Person    person;
      
      
      
      Vector result = new Vector();
      Vector param = new Vector();
      Vector perList = new Vector();
      
      functionType = (FunctionType) function.getFunctionType();
      if (null != functionType) {
          functionTypeKey = functionType.getKeyword();
          if (functionTypeKey.equalsIgnoreCase("ROLE")) {
              checkingForRole = true;
          }
      }
      
      if (null!= qualifier) {
          qualifierType = (QualifierType) qualifier.getQualifierType();
          if (qualifierType == null) {
              //Assume that the qualifier is a unit
              qualifierTypeKey = "UNIT";
          } else {
              qualifierTypeKey = qualifierType.getKeyword();
          }
      }
      
      try {
          if (qualifier != null) {
              if (qualifierTypeKey.equals("PROPOSAL")) {
                  //Qualifier is Proposal
                  if (checkingForRole == true) {
                      //Looking for a Specific role in proposal
                      param.addElement(new Parameter("AW_PROPOSAL_NUMBER", "String", (String) qualifier.getKey()));
                      param.addElement(new Parameter("AW_ROLE_ID", "String", (String) function.getKey()));
                      
                      if(dbEngine!=null) {
                          result = dbEngine.executeRequest("Coeus",
                          "call dw_get_users_for_prop_role( <<AW_PROPOSAL_NUMBER>> , <<AW_ROLE_ID>>, <<OUT RESULTSET rset>> )", 
                          "Coeus", param);
                          
                          int rowCount = result.size();
                          if (rowCount > 0) {
                              PersonsIterator persons = new PersonsIterator();
                              
                              for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                                  HashMap resultSet;
                                  resultSet = (HashMap) result.get(rowIndex);
                                  userID = resultSet.get("USER_ID").toString();
                                  person = (Person) factory.newPerson(userID);
                                  
                                  person.setName(resultSet.get("USER_NAME").toString());
                                  person.setDeptCode(resultSet.get("UNIT_NUMBER").toString());
                                  person.setDeptName(resultSet.get("UNIT_NAME").toString());
                                  person.setStatusCode(resultSet.get("STATUS").toString());
                                  perList.add(person);
                              }
                              
                              persons.setPersonList(perList);
                              return persons;
                          } else {
                              //Stored procedure returned no rows
                              return null;
                          }
                      }
                  } else {
                      //Looking for a specific right
                  }
              } else {
                  //Qualifier is Unit
                  if (checkingForRole == true) {
                      //Looking for a Specific role
                      param.addElement(new Parameter("AW_ROLE_ID", "String", (String) function.getKey()));
                      param.addElement(new Parameter("AW_UNIT_NUMBER", "String", (String) qualifier.getKey()));
                      
                      if(dbEngine!=null) {
                          result = dbEngine.executeRequest("Coeus",
                          "call dw_get_users_for_role( <<AW_ROLE_ID>> , <<AW_UNIT_NUMBER>>, <<OUT RESULTSET rset>> )", 
                          "Coeus", param);
                          
                          int rowCount = result.size();
                          if (rowCount > 0) {
                              PersonsIterator persons = new PersonsIterator();
                              
                              for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                                  HashMap resultSet;
                                  resultSet = (HashMap) result.get(rowIndex);
                                  userID = resultSet.get("USER_ID").toString();
                                  person = (Person) factory.newPerson(userID);
                                  person.setName(resultSet.get("USER_NAME").toString());
                                  person.setStatusCode(resultSet.get("DESCEND_FLAG").toString());
                                  perList.add(person);
                              }
                              
                              persons.setPersonList(perList);
                              return persons;
                          } //end if result.size
                      }
                  } else {
                      //Looking for a specific right
                  }
              }
          } else {
              //Qualifier is NULL - Looking for a Role or right anywhere in the sysyem
              if (checkingForRole == true) {
                  //Looking for a Specific role
              } else {
                  //Looking for a specific right
              }
          }
      } catch (Exception e) {
          throw new org.okip.service.authorization.api.AuthorizationException(
            e.getMessage());
      }
    return null;
  }

  /**
   * Sabari
   * Don;t need this
   */
  private static org.okip.service.authorization.api.AgentsIterator getPeopleInternal(
          Factory factory,
          String functionName,
          String functionType,
          Object qualifierId,
          String qualifierCode,
          String qualifierType,
          String isActiveNowStr,
          String expandQualifiersStr)
  throws org.okip.service.authorization.api.AuthorizationException {
    return null;
  }

   public void setPersonList(Vector perList)  {
      personList = perList;
  }
   
   public int getPersonCount() {
       if (null != personList) {
           return personList.size();
       } else  {
           return 0;
       }
   }

}

