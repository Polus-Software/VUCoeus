package edu.mit.is.service.authorization;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import java.util.HashMap;
import java.util.Vector;


public class FunctionsIterator
implements org.okip.service.authorization.api.FunctionsIterator {

  protected Factory factory = null;
  private Vector functionList = new Vector();
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
  public boolean hasNext()
  throws org.okip.service.authorization.api.AuthorizationException {
      if ((null == functionList) || (functionList.size() == 0)) {
          return false;
      } else if (currentIndex >= functionList.size()) {
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
  public org.okip.service.authorization.api.Function next()
  throws org.okip.service.authorization.api.AuthorizationException {
 
        
      if (null != functionList) {
          if (currentIndex >= functionList.size() ) {
              return null;
          } else {
              currentIndex++;
              return (Function) functionList.get(currentIndex - 1);
          }
          
      }
      return null;
  }

  /**
   * Method getFunctionsOfType
   *
   *
   * @param factory
   * @param functionType
   *
   * @return
   *
   * @throws org.okip.service.authorization.api.AuthorizationException
   *
   */
  public static org.okip.service.authorization.api.FunctionsIterator getFunctionsOfType(
  Factory factory,
  org.okip.service.authorization.api.FunctionType functionType)
  throws org.okip.service.authorization.api.AuthorizationException {
      
      DBEngineImpl dbEngine = new DBEngineImpl();
      String functionTypeKey = "";
      String typeKey;
      String descendFlag = "";
      
      Vector result = new Vector();
      Vector param = new Vector();
      Vector funList = new Vector();
      
      Function function;
      FunctionType fType;
      
      
      if (functionType == null) {
          throw new org.okip.service.authorization.api.AuthorizationException(
          "Function type cannot be NULL");
      }
      
      functionTypeKey = functionType.getKeyword();
      
      try {
          if (functionTypeKey.equalsIgnoreCase("ALL")) {
              //get all rights in the system
              if (dbEngine != null) {
                  result = dbEngine.executeRequest("Coeus",
                  "call dw_get_all_rights ( <<OUT RESULTSET rset>> )",
                  "Coeus", param);
                  
                  int rowCount = result.size();
                  if (rowCount > 0) {
                      FunctionsIterator functions = new FunctionsIterator();
                      
                      for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                          HashMap resultSet;
                          resultSet = (HashMap) result.get(rowIndex);
                          
                          function = (Function) factory.newFunction(resultSet.get("RIGHT_ID").toString());
                          
                          function.setDescription(resultSet.get("DESCRIPTION").toString());
                          descendFlag = resultSet.get("DESCEND_FLAG").toString();
                          if (descendFlag.equalsIgnoreCase("Y")) {
                              fType = (FunctionType) factory.newFunctionType(resultSet.get("RIGHT_TYPE").toString(), "DESCEND");
                          } else {
                              fType = (FunctionType) factory.newFunctionType(resultSet.get("RIGHT_TYPE").toString(), "");
                          }
                          
                          function.setFunctionType(fType);
                          funList.add(function);
                      }
                      
                      functions.setFunctionList(funList);
                      return functions;
                  } else {
                      //Stored procedure returned no rows
                      return null;
                  }
                  
              }
          } else if (functionTypeKey.equalsIgnoreCase("ROLE")) {
              //Looking for all rights in a role
              if (dbEngine != null) {
                  String roleId = functionType.getDescription();
                  if (roleId == null) {
                      throw new org.okip.service.authorization.api.AuthorizationException(
                            "Role ID cannot be NULL.  Set role ID in FunctionType description");
                  }
                  
                  param.addElement(new Parameter("AW_ROLE_ID", "String", roleId));     
                  
                  result = dbEngine.executeRequest("Coeus",
                  "call get_all_rights_for_role ( << AW_ROLE_ID >>, <<OUT RESULTSET rset>> )",
                  "Coeus", param);
                  
                  int rowCount = result.size();
                  if (rowCount > 0) {
                      FunctionsIterator functions = new FunctionsIterator();
                      
                      for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                          HashMap resultSet;
                          resultSet = (HashMap) result.get(rowIndex);
                          
                          function = (Function) factory.newFunction(resultSet.get("RIGHT_ID").toString());
                          
                          function.setDescription(resultSet.get("DESCRIPTION").toString());
                          descendFlag = resultSet.get("DESCEND_FLAG").toString();
                          if (descendFlag.equalsIgnoreCase("Y")) {
                              fType = (FunctionType) factory.newFunctionType(resultSet.get("RIGHT_TYPE").toString(), "DESCEND");
                          } else {
                              fType = (FunctionType) factory.newFunctionType(resultSet.get("RIGHT_TYPE").toString(), "");
                          }
                          
                          function.setFunctionType(fType);
                          funList.add(function);
                      }
                      
                      functions.setFunctionList(funList);
                      return functions;
                  } else {
                      //Stored procedure returned no rows
                      return null;
                  }
              }
          } else {
              //get all rights of a particular type
              if (dbEngine != null) {
                  param.addElement(new Parameter("aw_right_type", "String", functionTypeKey));     
                  
                  result = dbEngine.executeRequest("Coeus",
                  "call get_all_rights_of_type ( << aw_right_type >>, <<OUT RESULTSET rset>> )",
                  "Coeus", param);
                  
                  int rowCount = result.size();
                  if (rowCount > 0) {
                      FunctionsIterator functions = new FunctionsIterator();
                      
                      for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                          HashMap resultSet;
                          resultSet = (HashMap) result.get(rowIndex);
                          
                          function = (Function) factory.newFunction(resultSet.get("RIGHT_ID").toString());
                          
                          function.setDescription(resultSet.get("DESCRIPTION").toString());
                          descendFlag = resultSet.get("DESCEND_FLAG").toString();
                          if (descendFlag.equalsIgnoreCase("Y")) {
                              fType = (FunctionType) factory.newFunctionType(resultSet.get("RIGHT_TYPE").toString(), "DESCEND");
                          } else {
                              fType = (FunctionType) factory.newFunctionType(resultSet.get("RIGHT_TYPE").toString(), "");
                          }
                          
                          function.setFunctionType(fType);
                          funList.add(function);
                      }
                      
                      functions.setFunctionList(funList);
                      return functions;
                  } else {
                      //Stored procedure returned no rows
                      return null;
                  }
                  
              }
          }
      } catch (Exception e) {
          throw new org.okip.service.authorization.api.AuthorizationException(
          e.getMessage());
      }
      
      return null;
  }

  protected static org.okip.service.authorization.api.FunctionsIterator getFunctionsIterator(
          Factory factory,
          String sqlQuery)
  throws org.okip.service.authorization.api.AuthorizationException {
 
    throw new org.okip.service.authorization.api.AuthorizationException(
      org.okip.service.authorization.api.AuthorizationException.UNIMPLEMENTED);
  }
  
   public void setFunctionList(Vector funList)  {
      functionList = funList;
  }
   
   public int getFunctionCount() {
       if (null != functionList) {
           return functionList.size();
       } else  {
           return 0;
       }
   }

}
