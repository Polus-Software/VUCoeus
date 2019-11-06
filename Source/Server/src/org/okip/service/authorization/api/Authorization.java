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
 * $Source: /cvs/oki/tech/src/org/okip/service/authorization/api/Authorization.java,v $
 */

/**
 * Authorization properties indicate what Agent can do a Function in a Qualifier context.
 * <p>
 * Licensed under the {@link org.okip.service.ApiLicense MIT OKI&#153; API Definition License}.
 * <p>
 * @see Function
 * @see FunctionType
 * @see Qualifier
 * @see QualifierType
 * @see Agent
 * @see GrantPrivilege
 * @see Factory
 * @version $Name:  $ / $Revision:   1.0  $ / $Date:   Jan 14 2003 15:29:02  $
 *
 */
public interface Authorization
extends java.io.Serializable {

  /**
   * Method getEffectiveDate
   *
   *
   * @return java.sql.Timestamp
   *
   * @throws AuthorizationException
   *
   */
  java.sql.Timestamp getEffectiveDate()
  throws AuthorizationException;

  /**
   * Method getExpirationDate
   *
   *
   * @return java.sql.Timestamp
   *
   * @throws AuthorizationException
   *
   */
  java.sql.Timestamp getExpirationDate()
  throws AuthorizationException;

  /**
   * Method getFunction
   *
   *
   * @return Function
   *
   * @throws AuthorizationException
   *
   */
  Function getFunction()
  throws AuthorizationException;

  /**
   * Method getModifiedBy
   *
   *
   * @return String
   *
   * @throws AuthorizationException
   *
   */
  String getModifiedBy()
  throws AuthorizationException;

  /**
   * Method getModifiedDate
   *
   *
   * @return java.sql.Timestamp
   *
   * @throws AuthorizationException
   *
   */
  java.sql.Timestamp getModifiedDate()
  throws AuthorizationException;

  /**
   * Method getQualifier
   *
   *
   * @return Qualifier
   *
   * @throws AuthorizationException
   *
   */
  Qualifier getQualifier()
  throws AuthorizationException;

  /**
   * Method getAgent
   *
   *
   * @return Agent
   *
   * @throws AuthorizationException
   *
   */
  Agent getAgent()
  throws AuthorizationException;

  /**
   * Method isActiveNow
   *
   *
   * @return boolean
   *
   * @throws AuthorizationException
   *
   */
  boolean isActiveNow()
  throws AuthorizationException;

  /**
   * Method isImplied
   *
   * Some Authorizations are explicitly stored in
   * the Authorizations DB and others are implied.
   * Use this method to determine if the Authorization
   * is implied.
   *
   * @return boolean
   *
   * @throws AuthorizationException
   *
   */
   boolean isImplied()
   throws AuthorizationException;

}
