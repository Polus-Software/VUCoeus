package org.okip.service.authorization.api;



/*
   Copyright (c) 2002 Massachusetts Institute of Technology

   This work, including any software, documents, or other related items
   (the "Work"), is being provided by the copyright holder(s) subject to
   the terms of the MIT OKI(TM) API Definition License. By obtaining,
   using and/or copying this Work, you agree that you have read,
   understand, and will comply with the following terms and conditions of
   the MIT OKI(TM) API Definition License:

   You may use, copy, and distribute unmodified versions of this Work for
   any purpose, without fee or royalty, provided that you include the
   following on ALL copies of the Work that you make or distribute:

    *  The full text of the MIT OKI(TM) API Definition License in a
       location viewable to users of the redistributed Work.

    *  Any pre-existing intellectual property disclaimers, notices, or
       terms and conditions. If none exist, a short notice similar to the
       following should be used within the body of any redistributed
       Work: "Copyright (c) 2002 Massachusetts Institute of Technology. All
       Rights Reserved."

   You may modify or create Derivatives of this Work only for your
   internal purposes. You shall not distribute or transfer any such
   Derivative of this Work to any location or any other third party. For
   purposes of this license, "Derivative" shall mean any derivative of
   the Work as defined in the United States Copyright Act of 1976, such
   as a translation or modification.

   THIS WORK PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
   IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
   CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
   TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE WORK
   OR THE USE OR OTHER DEALINGS IN THE WORK.

   The name and trademarks of copyright holder(s) and/or MIT may NOT be
   used in advertising or publicity pertaining to the Work without
   specific, written prior permission. Title to copyright in the Work and
   any associated documentation will at all times remain with the
   copyright holders.

*/

/*
 * $Source: /cvs/oki/tech/src/org/okip/service/authorization/api/Factory.java,v $
 */

/**
Factory allows the application developer to create Authorization
objects in the Authorization database, to get Authorization objects from the Authorization
database given selection criteris, to ask questions of Authorization such as
what Agent can do a Function in a Qualifier context.
<P>
 * Licensed under the {@link org.okip.service.ApiLicense MIT OKI&#153; API Definition License}.
<p>
The key objects in Authorization are Authorization, Function, Agent, and
and Qualifier.  There are some other objects in the authorizations API that play
supporting roles.  They are FunctionType and QualifierType.
<P>
There are two methods to create Authorizations. One uses method uses
Agent, Function, and Qualifier.  The other adds effective date and
expiration date.  For the method without the dates the effective date
is today and there is no expiration date.
<P>
There are methods for getting Authorizations:
<pre>
   (1) getAuthorizations

       This method is overloaded to allow for combinations
       of filtering arguments to find matching Authorizations.

   (2) getAuthorizationsForDoingFWithQ

       given a Function and a Qualifier, finds a list of all
       Authorizations that would permit a agent to do the
       Function with the Qualifier. This method is overloaded
       so that Authorizations for Functions within a FunctionType
       can be found. This uses a different algorithm than
       getAuthorizations.

       Note that the Qualifier in any found Authorization
       might be a parent of the Qualifier given as an argument.

</pre>
 * <p>
 * @see Agent
 * @see Authorization
 * @see AuthorizationsIterator
 * @see Function
 * @see FunctionsIterator
 * @see FunctionType
 * @see AgentsIterator
 * @see Qualifier
 * @see QualifiersIterator
 * @see QualifierType
 * @version $Name:  $ / $Revision:   1.0  $ / $Date:   Jan 14 2003 15:29:02  $
 */

public interface Factory
extends org.okip.service.shared.api.Factory {

  /**
   * Method getDatabaseSymbolicNames
   *
   *
   * @return String[]
   *
   * @throws AuthorizationException
   *
   */
  String[] getDatabaseSymbolicNames()
  throws AuthorizationException;

  /**
   * Method getDatabaseSymbolicName
   *
   *
   * @return String
   *
   * @throws AuthorizationException
   *
   */
  String getDatabaseSymbolicName()
  throws AuthorizationException;

  /**
   * Method setDatabaseSymbolicName
   *
   *
   *
   * @param int - index into the Database symbolic names array
   *              0 indicates the default Database syn
   *
   * @throws AuthorizationException
   *
   */
  void setDatabaseSymbolicName(int index)
  throws AuthorizationException;

  /**
   * Method createAuthorization
   *
   * creates a new Authorization in the Authorizations DB for a Agent
   * doing a Function with a Qualifier and having Grant rights
   * and ability to doFunction.
   *
   *
   * @param agent
   * @param function
   * @param qualifier
   * @param effectiveDate
   * @param expirationDate
   *
   * @return Authorization
   *
   * @throws AuthorizationException
   *
   */
  Authorization createAuthorization(Agent agent, Function function,
                                    Qualifier qualifier,
                                    java.sql.Timestamp effectiveDate,
                                    java.sql.Timestamp expirationDate)
  throws AuthorizationException;

  /**
   * Method createAuthorization
   *
   * creates a new Authorization in the authorizations DB for a Agent
   * doing a Function with a Qualifier and having Grant rights
   * and ability to doFunction. Uses current date/time as the
   * effectiveDate and doesn't set an exiration date.
   *
   *
   * @param agent
   * @param function
   * @param qualifier
   *
   * @return Authorization
   *
   * @throws AuthorizationException
   *
   */
  Authorization createAuthorization(Agent agent, Function function,
                                    Qualifier qualifier)
  throws AuthorizationException;

  /**
   * Method createFunction
   *
   *
   * @param key
   * @param displayName
   * @param description
   * @param functionType
   * @param qualifierType
   *
   * @return Function
   *
   * @throws AuthorizationException
   *
   */
  Function createFunction(java.io.Serializable key, String displayName, String description,
                          FunctionType functionType,
                          QualifierType qualifierType)
  throws AuthorizationException;

  /**
   * Method createFunctionType
   *
   *
   *
   * @param keyword
   * @param description
   *
   * @return FunctionType
   *
   * @throws AuthorizationException
   *
   */
  FunctionType createFunctionType(String keyword, String description)
  throws AuthorizationException;

  /**
   * Method createRootQualifier
   *
   * creates a new Qualifier in the Authorizations DB that has no parent.
   * This is different from making a new instance of a Qualifier object locally
   * as the Qualifier will be inserted into the Authorizations DB.
   *
   *
   * @param key
   * @param displayName
   * @param description
   * @param qualifierType
   * @param qualifierCode
   *
   * @return Qualifier
   *
   * @throws AuthorizationException
   *
   */
  Qualifier createRootQualifier(java.io.Serializable key, String displayName, String description,
    QualifierType qualifierType)
  throws AuthorizationException;

  /**
   * Method createQualifier
   *
   * creates a new Qualifier in the Authorizations DB. This is different
   * from making a new instance of a Qualifier object locally
   * as the Qualifier will be inserted into the Authorizations DB.
   *
   *
   * @param key
   * @param displayName
   * @param description
   * @param qualifierType
   * @param qualifierCode
   * @param parent - may not be null
   *
   * @return Qualifier
   *
   * @throws AuthorizationException
   *
   */
  Qualifier createQualifier(java.io.Serializable key, String displayName, String description,
    QualifierType qualifierType,
    Qualifier parent)
  throws AuthorizationException;

  /**
   * Method createQualifierType
   *
   * creates a new QualifierType in the Authorizations DB. This is different
   * from making a new instance of a QualifierType object locally
   * as the QualifierType will be inserted into the Authorizations DB.
   *
   *
   * @param String keyword
   * @param String description
   *
   * @return QualifierType
   *
   * @throws AuthorizationException
   *
   */
  QualifierType createQualifierType(String keyword, String description)
  throws AuthorizationException;

  /**
   * Method deleteAuthorization
   *
   * deletes an existing authorization in the Authorizations DB.
   *
   *
   * @param authorization
   *
   * @throws AuthorizationException
   *
   */
  void deleteAuthorization(Authorization authorization)
  throws AuthorizationException;

  /**
   * Method deleteFunction
   *
   *
   *
   * @param function
   *
   * @throws AuthorizationException
   *
   */
  void deleteFunction(Function function)
  throws AuthorizationException;

  /**
   * Method deleteFunctionType
   *
   *
   *
   * @param functionType
   *
   * @throws AuthorizationException
   *
   */
  void deleteFunctionType(FunctionType functionType)
  throws AuthorizationException;

  /**
   * Method deleteQualifier
   *
   *
   *
   * @param qualifier
   *
   * @throws AuthorizationException
   *
   */
  void deleteQualifier(Qualifier qualifier)
  throws AuthorizationException;

  /**
   * Method deleteQualifierType
   *
   *
   *
   * @param qualifierType
   *
   * @throws AuthorizationException
   *
   */
  void deleteQualifierType(QualifierType qualifierType)
  throws AuthorizationException;

  /**
   * Method updateAuthorization
   *
   * updates one or more fields in an existing authorization
   * in the Authorizations DB.
   *
   *
   * @param authorization
   * @param agent
   * @param function
   * @param qualifier
   * @param effectiveDate
   * @param expirationDate
   *
   * @throws AuthorizationException
   *
   */
  void updateAuthorization(Authorization authorization, Agent agent,
                           Function function, Qualifier qualifier,
                           java.sql.Timestamp effectiveDate,
                           java.sql.Timestamp expirationDate)
  throws AuthorizationException;

  /**
   * Method updateFunction
   *
   * updates one or more fields in an existing function in the Authorizations DB.
   *
   *
   * @param function
   * @param newDisplayName
   * @param newDescription
   * @param newFunctionType
   * @param newQualifierType
   *
   * @throws AuthorizationException
   *
   */
  void updateFunction(Function function, String newDisplayName,
                      String newDescription,
                      FunctionType newFunctionType,
                      QualifierType newQualifierType)
  throws AuthorizationException;

  /**
   * Method updateFunctionType
   *
   *
   *
   * @param functionType
   * @param newDescription
   *
   * @throws AuthorizationException
   *
   */
  void updateFunctionType(FunctionType functionType,
                              String newDescription)
  throws AuthorizationException;

  /**
   * Method updateQualifier
   *
   * updates one or more fields in an existing qualifier in the Authorizations DB.
   *
   *
   * @param qualifier
   * @param newDisplayName
   * @param newDescription
   *
   * @throws AuthorizationException
   *
   */
  void updateQualifier(Qualifier qualifier, String newDisplayName,
                       String newDescription)
  throws AuthorizationException;

  /**
   * Method updateQualifierType
   *
   * updates Description in an existing qualifierType in the Authorizations DB.
   *
   *
   * @param qualifierType
   * @param newDescription
   *
   * @throws AuthorizationException
   *
   */
  void updateQualifierType(QualifierType qualifierType, String newDescription)
  throws AuthorizationException;

  /**
   * Method refreshFunction
   *
   * refreshs Function properties from a changing Authorizations DB.
   *
   *
   * @param function
   *
   * @throws AuthorizationException
   *
   */
  void refreshFunction(Function function)
  throws AuthorizationException;

  /**
   * Method refreshFunctionType
   *
   *
   *
   * @param functionType
   *
   * @throws AuthorizationException
   *
   */
  void refreshFunctionType(FunctionType functionType)
  throws AuthorizationException;

  /**
   * Method refreshAgent
   *
   * refresh properties from a changing Authorizations DB.
   *
   *
   * @param agent
   *
   * @throws AuthorizationException
   *
   */
  void refreshAgent(Agent agent)
  throws AuthorizationException;

  /**
   * Method refreshQualifier
   *
   * refreshes properties from a changing Authorizations DB.
   *
   *
   * @param qualifier
   *
   * @throws AuthorizationException
   *
   */
  void refreshQualifier(Qualifier qualifier)
  throws AuthorizationException;

  /**
   * Method refreshQualifierType
   *
   * refreshes QualifierType properties from a changing Authorizations DB.
   *
   *
   * @param qualifierType
   *
   * @throws AuthorizationException
   *
   */
  void refreshQualifierType(QualifierType qualifierType)
  throws AuthorizationException;

  /**
   * Method isAuthorized
   *
   * given a Agent, Function, and Qualifier
   * returns true if the Agent is authorized to perform
   * the given Function with the given Qualifier.
   *
   *
   * @param agent
   * @param function
   * @param qualifier
   *
   * @return boolean
   *
   * @throws AuthorizationException
   *
   */
  boolean isAuthorized(Agent agent, Function function, Qualifier qualifier)
  throws AuthorizationException;

  /**
   * Method isAuthorized
   *
   * given a Agent, FunctionType, and Qualifier
   * returns true if the Agent is authorized to perform
   * at least one Function in the FunctionType with the Qualifier.
   *
   *
   * @param agent
   * @param functionType
   * @param qualifier
   *
   * @return boolean
   *
   * @throws AuthorizationException
   *
   */
  boolean isAuthorized(Agent agent, FunctionType functionType,
                       Qualifier qualifier)
  throws AuthorizationException;

  /**
   * Method getFunctionTypes
   *
   *
   * @return FunctionType[]
   *
   * @throws AuthorizationException
   *
   */
  FunctionType[] getFunctionTypes()
  throws AuthorizationException;

  /**
   * Method getFunctionsOfType
   *
   *
   *
   * @param functionType
   *
   * @return FunctionsIterator
   *
   * @throws AuthorizationException
   *
   */
  FunctionsIterator getFunctionsOfType(FunctionType functionType)
  throws AuthorizationException;

  /**
   * Method getFunction
   *
   * It may or may not exist in the Authorizations DB.
   *
   *
   * @param FunctionType
   * @param java.io.Serializable key
   *
   * @return Function or null
   *
   * @throws AuthorizationException
   *
   */
  Function getFunction(FunctionType functionType, java.io.Serializable key)
  throws AuthorizationException;

  /**
   * Method getFunction
   *
   * It may or may not exist in the Authorizations DB.
   *
   *
   * @param functionTypeKeyword
   * @param key
   *
   * @return Function or null
   *
   * @throws AuthorizationException
   *
   */
  Function getFunction(String functionTypeKeyword, java.io.Serializable key)
  throws AuthorizationException;

  /**
   * Method getFunctionType
   *
   * It may or may not exist in the Authorizations DB.
   *
   *
   * @param functionTypeKeyword
   *
   * @return String
   *
   * @throws AuthorizationException
   *
   */
  FunctionType getFunctionType(String functionTypeKeyword)
  throws AuthorizationException;

  /**
   * Method getAgent
   *
   * The agent may or may not exist in the Authorizations DB.
   *
   * @param key
   *
   * @return Agent or null
   *
   * @throws AuthorizationException
   *
   */
  Agent getAgent(java.io.Serializable key)
  throws AuthorizationException;

  /**
   * Method getQualifierTypes
   *
   * returns Qualifier types available in the Authorizations DB.
   *
   * @return QualifierType[]
   *
   * @throws AuthorizationException
   *
   */
  QualifierType[] getQualifierTypes()
  throws AuthorizationException;

  /**
   * Method getRootQualifier
   *
   * given a Qualifier Type returns the qualifier
   * that is the root of the tree of qualifiers of this type.
   *
   *
   * @param qualifierType
   *
   * @return Qualifier
   *
   * @throws AuthorizationException
   *
   */
  Qualifier getRootQualifier(QualifierType qualifierType)
  throws AuthorizationException;

  /**
   * Method getQualifierChildren
   *
   * given an existing qualifier
   * returns a list of its child qualifiers.
   *
   *
   * @param qualifier
   *
   * @return QualifiersIterator
   *
   * @throws AuthorizationException
   *
   */
  QualifiersIterator getQualifierChildren(Qualifier qualifier)
  throws AuthorizationException;

  /**
   * Method getQualifier
   *
   * The instance may or may not exist in the Authorizations DB.
   *
   *
   * @param qualifierType
   * @param key
   *
   * @return Qualifier or null
   *
   * @throws AuthorizationException
   *
   */
  Qualifier getQualifier(QualifierType qualifierType, java.io.Serializable key)
  throws AuthorizationException;

  /**
   * Method getQualifier
   *
   * The instance may or may not exist in the Authorizations DB.
   *
   *
   * @param qualifierTypeKeyword
   * @param key
   *
   * @return Qualifier or null
   *
   * @throws AuthorizationException
   *
   */
  Qualifier getQualifier(String qualifierTypeKeyword, java.io.Serializable key)
  throws AuthorizationException;

  /**
   * Method getQualifier
   *
   * The instance may or may not exist in the Authorizations DB.
   *
   *
   * @param java.io.Serializable key
   *
   * @return Qualifier or null
   *
   * @throws AuthorizationException
   *
   */
  Qualifier getQualifier(java.io.Serializable key)
  throws AuthorizationException;

  /**
   * Method getQualifierType
   *
   * The instance may or may not exist in the Authorizations DB.
   *
   *
   * @param String qualifierTypeKeyword
   *
   * @return QualifierType or null
   *
   * @throws AuthorizationException
   *
   */
  QualifierType getQualifierType(String qualifierTypeKeyword)
  throws AuthorizationException;

  /**
   * Method getWhoCanDo
   *
   * given a Function and a Qualifier
   * returns an enumeration of all agents allowed
   * to do the Function with the Qualifier.
   *
   *
   * @param function
   * @param qualifier
   * @param isActiveNow
   *
   * @return AgentsIterator
   *
   * @throws AuthorizationException
   *
   */
  AgentsIterator getWhoCanDo(Function function, Qualifier qualifier,
                              boolean isActiveNow)
  throws AuthorizationException;

  /**
   * Method getWhoCanDo
   *
   * given a Function
   * returns an enumeration of all agents at least one
   * Authorization for the Function where the Qualifier
   * is not considered.
   *
   *
   * @param function
   * @param isActiveNow
   *
   * @return AgentsIterator
   *
   * @throws AuthorizationException
   *
   */
  AgentsIterator getWhoCanDo(Function function, boolean isActiveNow)
  throws AuthorizationException;

  /**
   * Method getWhoCanDo
   *
   * given a FunctionType
   * returns an enumeration of all agents with at least one
   * Authorization for at least one Function in this
   * FunctionType where the Qualifier
   * is not considered.
   *
   *
   * @param functionType
   * @param isActiveNow
   *
   * @return AgentsIterator
   *
   * @throws AuthorizationException
   *
   */
  AgentsIterator getWhoCanDo(FunctionType functionType,
                              boolean isActiveNow)
  throws AuthorizationException;

  /**
   * Method getWhoCanDo
   *
   * given a FunctionType and a Qualifier
   * returns an enumeration of all agents with at least one
   * Authorization for at least one Function in this
   * FunctionType for the Qualifier.
   *
   *
   * @param functionType
   * @param qualifier
   * @param isActiveNow
   *
   * @return AgentsIterator
   *
   * @throws AuthorizationException
   *
   */
  AgentsIterator getWhoCanDo(FunctionType functionType,
                              Qualifier qualifier, boolean isActiveNow)
  throws AuthorizationException;

  /**
   * Method getWhoCanDo
   *
   * given a Qualifier
   * returns an enumeration of all agents with at least one
   * Authorization for at least one Function with the Qualifier.
   *
   *
   * @param qualifier
   * @param isActiveNow
   *
   * @return AgentsIterator
   *
   * @throws AuthorizationException
   *
   */
  AgentsIterator getWhoCanDo(Qualifier qualifier, boolean isActiveNow)
  throws AuthorizationException;

  /**
   * Method getAuthorizationsForDoingFWithQ
   *
   * given a Function and a Qualifier
   * returns an enumeration of all Authorizations that would allow agents
   * to do the Function with Qualifier. This method differs from the simple
   * form of getAuthorizations in that this method looks for any Authorization
   * that permits doing the Function with the Qualifier even if the Authorization's
   * Qualifier happens to be a parent of this Qualifier argument.
   *
   *
   * @param function
   * @param qualifier
   * @param isActiveNow
   *
   * @return AuthorizationsIterator
   *
   * @throws AuthorizationException
   *
   */
  AuthorizationsIterator getAuthorizationsForDoingFWithQ(Function function,
          Qualifier qualifier, boolean isActiveNow)
  throws AuthorizationException;

  /**
   * Method getAuthorizationsForDoingFWithQ
   *
   * given a FunctionType and a Qualifier
   * returns an enumeration of all Authorizations that would allow agents
   * to do Functions in the FunctionType with Qualifier. This method differs
   * from getAuthorizations in that this method looks for any Authorization
   * that permits doing the Function with the Qualifier even if the
   * Authorization's Qualifier happens to be a parent of the Qualifier argument.
   *
   *
   * @param functionType
   * @param qualifier
   * @param isActiveNow
   *
   * @return Authorizations
   *
   * @throws AuthorizationException
   *
   */
  AuthorizationsIterator getAuthorizationsForDoingFWithQ(
    FunctionType functionType, Qualifier qualifier,
    boolean isActiveNow)
  throws AuthorizationException;

  /**
   * Method getAuthorizations
   *
   * given a Agent, a Function, and a Qualifier (at least
   * one of these must be non-null) returns an enumeration
   * of matching authorizations.
   *
   *
   *
   * @param agent
   * @param function
   * @param qualifier
   * @param isActiveNow
   * @param expandQualifiers
   *
   * @return AuthorizationsIterator
   *
   * @throws AuthorizationException
   *
   */
  AuthorizationsIterator getAuthorizations(Agent agent, Function function,
                                           Qualifier qualifier,
                                           boolean isActiveNow,
                                           boolean expandQualifiers)
  throws AuthorizationException;

  /**
   * Method getAuthorizations
   *
   * given a Agent, a FunctionType, and a Qualifier
   * (either Agent or Qualifier must be non-null)
   * returns an enumeration of matching authorizations.
   * The authorizations must be for Functions within the
   * given FunctionType.
   *
   *
   * @param agent
   * @param functionType
   * @param qualifier
   * @param isActiveNow
   * @param expandQualifiers
   *
   * @return AuthorizationsIterator
   *
   * @throws AuthorizationException
   *
   */
  AuthorizationsIterator getAuthorizations(Agent agent,
                                           FunctionType functionType,
                                           Qualifier qualifier,
                                           boolean isActiveNow,
                                           boolean expandQualifiers)
  throws AuthorizationException;

}
