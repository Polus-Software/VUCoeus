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
 * $Source: /cvs/oki/tech/src/org/okip/service/authorization/api/AuthorizationException.java,v $
 */

/**
 * AuthorizationException is the only Exception throw by the Authorization API.
 * <p>
 * Licensed under the {@link org.okip.service.ApiLicense MIT OKI&#153; API Definition License}.
 *
 * @version $Name:  $ / $Revision:   1.0  $ / $Date:   Jan 14 2003 15:29:02  $
 */

public class AuthorizationException
extends org.okip.service.shared.api.Exception {
  public static final String NULLARGUMENT                       =
    "null or missing argument ";
  public static final String MANDATORYARGUMENTMISSING           =
    "Mandatory argument is missing ";
  public static final String QUERYAUTHORIZATIONFAILED           =
    "Query authorization failed ";
  public static final String CREATEAUTHORIZATIONFAILED          =
    "Create authorization failed ";
  public static final String DELETEAUTHORIZATIONFAILED          =
    "Delete authorization failed ";
  public static final String UPDATEAUTHORIZATIONFAILED          =
    "Update authorization failed ";
  public static final String AUTHORIZATIONENUMERATIONFAILED     =
    "Enumeration of authorizations failed ";
  public static final String AUTHORIZATIONFIELDDOESNOTEXIST     =
    "Authorization field does not exist ";

  public static final String FUNCTIONIDALREADYEXISTS            =
    "Function already exists ";
  public static final String INVALIDDATABASE                    =
    "Invalid database ";
  public static final String INVALIDFUNCTION                    =
    "Invalid function ";
  public static final String FUNCTIONNOTFOUNDINAUTHORIZATION   =
    "Function not found in Authorizations ";
  public static final String FUNCTIONTYPENOTFOUNDINAUTHORIZATION    =
    "Function type not found in Authorizations ";
  public static final String FUNCTIONENUMERATIONFAILED          =
    "Enumeration of functions failed ";

  public static final String INVALIDAGENTKEY                   =
    "Invalid agent key ";
  public static final String AGENTNOTFOUNDINAUTHORIZATION              =
    "Agent not found in Authorizations ";
  public static final String AGENTSENUMERATIONFAILED            =
    "Enumeration of agents failed ";

  public static final String INVALIDQUALIFIER                   =
    "Invalid qualifier ";
  public static final String QUALIFIERHASCHILDRENCANNOTDELETE           =
    "Qualifier has children and cannot be deleted ";
  public static final String QUALIFIERMUSTHAVEPARENT           =
    "Qualifier that is not a root Qualifier must have a parent ";
  public static final String QUALIFIERNOTFOUNDINAUTHORIZATION           =
    "Qualifier not found in Authorizations ";
  public static final String QUALIFIERCODENOTFOUNDINAUTHORIZATION       =
    "Qualifier code not found in Authorizations ";
  public static final String QUALIFIERTYPENOTFOUNDINAUTHORIZATION       =
    "Qualifier type not found in Authorizations ";
  public static final String QUALIFIERENUMERATIONFAILED         =
    "Enumeration of qualifiers failed ";

  public static final String DATABASESYMBOLICNAMELISTDIDNOTLOAD =
    "Database symbolic name list did not load ";
  public static final String MISSINGDATABASESYMBOLICNAME        =
    "Current database symbolic name is not set ";
  public static final String BADINDEXDSN        =
    "Index into Database symbolic name list is out of bounds ";

  /**
   * Constructor AuthorizationException
   *
   *
   * @param message
   *
   */
  public AuthorizationException(String message) {
    super(message);
  }

}
