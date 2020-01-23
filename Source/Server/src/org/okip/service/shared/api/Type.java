package org.okip.service.shared.api;
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
 * $Source: /cvs/oki/tech/src/org/okip/service/shared/api/Type.java,v $
 */

/**
 * General Type object, used in communicating between
 * implementations of APIs and applications.
 *<p>
 * Examples include the Authentication Type.
 *<p>
 * Each Type is defined by three Strings, the first being the
 * domain, the second the authority and the third the keyword.
 * The <code>domain</code> String is the name of the domain of the
 * type; generally the subclass of Type will define this string.
 * The <code>authority</code> String is the name of the authority which
 * assigns or defines the keyword Strings.
 *<p>
 * Another way of looking at it is that the authority defines the
 * vocabulary for the domain in which the keyword has meaning.
 *<p>
 * Two Types are equal iff the <code>domain</code>, the
 * <code>authority</code> and <code>keyword</code> are equal.
 *<p>
 * A fourth string, the <code>description</code>, provides for additional
 * implementation-dependent information.
 *<p>
 * A URI String may be associated with the authority, providing a
 * link to further specification of the keyword.
 *<p>
 * An OKI API may return a Type or an array of Types, which give
 * meaning to associated information.
 *<p>
 * For example, the authentication implementation returns an array
 * of Types which informs the application what authentication types
 * are available in the implementation.  The application may choose
 * one Type to be used in authenticating an Agent.  The Agent
 * returned has an associated Type specifying the authentication
 * method used.  In some implementations the subtype may provide
 * information for accessing additional information about the Agent.
 *<p>
 * Licensed under the {@link org.okip.service.ApiLicense MIT OKI&#153; API Definition License}.
 *
 * @version $Name:  $ / $Revision:   1.0  $ / $Date:   Jan 14 2003 15:29:10  $ */

public abstract class Type
implements java.io.Serializable {

    private String domain;
    private String authority;
    private String keyword;
    private String description;

    private String authorityURI = null;

  /*
   * Character used to separate parts in the string representation of
   * this Type.  Default is ';'.
   */
  private char separatorChar = ';';

  /**
   * Construct a Type object for this domain, authority and keyword.
   */
  public Type(String domain, String authority, String keyword) {
    this.domain    = domain;
    this.authority = authority;
    this.keyword   = keyword;
    this.description  = "";
  }

  /**
   * Construct a Type object for this domain, authority, keyword, and description.
   */
  public Type(String domain, String authority, String keyword, String description) {
    this.domain    = domain;
    this.authority = authority;
    this.keyword   = keyword;
    this.description = description;
  }

  /**
   * Construct a Type object with this domain, authorityURI, authority,
   * keyword, and description.
   */
  public Type(String domain, String authorityURI, String authority, String keyword,
              String description) {
    this.domain       = domain;
    this.authorityURI = authorityURI;
    this.authority    = authority;
    this.keyword      = keyword;
    this.description  = description;
  }

  /**
   * Compares the specified Type with this Type for equality.
   *<p>
   * Two Types are equal iff the domain, authority and keyword are equal.
   *
   * @param type2
   *
   * @return true iff the domain, authority and keyword of the specified
   * object are equal to those of this object.
   */
  public boolean equals(Type type2) {
      boolean areEqual = true;

      if (type2 == null)
	  return false;

      if (this.authority == null) {
	  if (type2.authority != null) {
	      return false;
	  }
      } else {
	  areEqual &= this.authority.equals(type2.authority);
      }

      if (this.keyword == null) {
	  if (type2.keyword != null) {
	      return false;
	  }
      } else {
	  areEqual &= this.keyword.equals(type2.keyword);
      }

      if (this.domain == null) {
	  if (type2.domain != null) {
	      return false;
	  }
      } else {
	  areEqual &= this.domain.equals(type2.domain);
      }

      return areEqual;
  }

  /**
   * Compares the specified Type with any Object for equality.
   *<p>
   * Two Types are equal iff the domain, authority and keyword are equal.
   *
   * @param type2
   *
   * @return true iff the domain, authority and keyword of the specified
   * object are equal to those of this object.
   */
  public boolean equals(Object type2) {
      if (! (type2 instanceof Type)) {
	  return false;
      }
      return this.equals((Type) type2);
  }

  /**
   * Hash code.
   *<p>
   * Hash code ignores description field.
   *
   * @param type2
   *
   * @return true iff the domain, authority and keyword of the specified
   * object are equal to those of this object.
   */
    public int hashCode() {
	String s = this.domain + separatorChar
	    + this.authority + separatorChar
	    + this.keyword;
	return s.hashCode();
  }

  /**
   * Return string representation of this type.
   *<p>
   * The String representation of a Type is it's domain, authority,
   * keyword, and description, separated by the separatorChar
   *
   * @return string representation.
   */
  public String toString() {
    return this.domain + separatorChar
           + this.authority + separatorChar
           + this.keyword + separatorChar
           + this.description;
  }

  /**
   * Return the domain part of this Type.
   *<p>
   * The domain is a String representing the domain which
   * gives meaning to the of application of the naming authority and keyword.
   *
   * @return domain part of the Type
   */
  public String getDomain() {
    return this.domain;
  }

  /**
   * Return the authority part of this Type.
   *<p>
   * The authority is a String representing the naming authority which
   * gives meaning to the keyword, representing the vocabulary defining the
   * keyword, or equivalent function.
   *
   * @return authority part of the Type
   */
  public String getAuthority() {
    return this.authority;
  }

  /**
   * Return the keyword part of the Type.
   *<p>
   * The keyword is a String uniquely defining this Type within the
   * scope of the authority.
   *
   * @return keyword part of the Type
   */
  public String getKeyword() {
    return this.keyword;
  }

  /**
   * Return description part of the Type.
   *<p>
   * The description is a String which further qualifies this instance of
   * a Type.  Two Types which differ only by description are still
   * identical.
   *
   * @return description part of the Type
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Return URI for authority of the Type.
   *<p>
   * The authorityURI, if not null, is the URI which may be used to
   * get further information about the authority.
   *
   * @return authority URI of the Type, or null
   */
  public String getAuthorityURI() {
    return this.authorityURI;
  }


  /**
   * Get separator charactor.
   * <p>
   * The separation character is used to separate parts in the string
   * representation of this Type, returned by {@link #toString}.
   * Default is ';'.
   *
   * @return char - separatorChar
   */
  public char getSeparatorChar() {
    return this.separatorChar;
  }

  /**
   * Set separator charactor.
   *
   * @param separatorChar
   */
  public void setSeparatorChar(char separatorChar) {
    this.separatorChar = separatorChar;
  }
}

