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
 * $Source: /cvs/oki/tech/src/org/okip/service/shared/api/FactoryManager.java,v $
 */

/**
 * Manages Factories.
 *<p>
 * Licensed under the {@link org.okip.service.ApiLicense MIT OKI&#153; API Definition License}.
 *
 * @version $Name:  $ / $Revision:   1.0  $ / $Date:   Jan 14 2003 15:29:08  $
*/

public class FactoryManager
implements java.io.Serializable {

  private static final String API = "api" ;
  private static final String FACTORY = ".Factory" ;
  private static final String IMPL = "impl" ;

  /**
   * Get the default Factory.
   *
   *
   * @param apiPackageName
   * @param owner
   *
   * @return default Factory
   *
   * @throws org.okip.service.shared.api.Exception
   *
   */
  public static org.okip.service.shared.api.Factory getDefaultFactory(
          String apiPackageName,
          org.okip.service.shared.api.Agent owner)
  throws org.okip.service.shared.api.Exception {
    if (null != apiPackageName) {
      int index = apiPackageName.indexOf (API);
      if (-1 != index) {
         String implPackageName =
           apiPackageName.substring (0, index) +
           IMPL +
           apiPackageName.substring (index + API.length()) ;
         return getFactory(apiPackageName, implPackageName, owner);
      }
    }
    throw new org.okip.service.shared.api.Exception(
      org.okip.service.shared.api.Exception.FACTORYDIDNOTLOAD);
  }

  /**
   * Get Factory with specified properties.
   *
   *
   * @param apiPackageName
   * @param implPackageName
   * @param owner
   *
   * @return Factory
   *
   * @throws org.okip.service.shared.api.Exception
   *
   */


  public static org.okip.service.shared.api.Factory getFactory(
          String apiPackageName, String implPackageName,
          org.okip.service.shared.api.Agent owner)
  throws org.okip.service.shared.api.Exception {
    try {
      String factoryClassName = new String(implPackageName + FACTORY);

      if (null != factoryClassName) {
        Class                               factoryClass =
          Class.forName(factoryClassName);
        org.okip.service.shared.api.Factory factory      =
          (org.okip.service.shared.api.Factory) factoryClass.newInstance();

        if(null != owner) {
          factory.setOwner(owner);
        }
        else {
          factory.setOwner(new org.okip.service.shared.api.Agent());
        }

        return factory;
      }
      throw new org.okip.service.shared.api.Exception(
        org.okip.service.shared.api.Exception.FACTORYDIDNOTLOAD);
    }
    catch (java.lang.Exception ex) {
	  ex.printStackTrace();
      throw new org.okip.service.shared.api.Exception(ex.getMessage());
    }
  }

  public static java.util.Properties getPropertiesFromPRB(String packageName)
  throws org.okip.service.shared.api.Exception {
    java.util.Properties  properties = null ;
    java.util.PropertyResourceBundle prb =
      (java.util.PropertyResourceBundle) java.util.ResourceBundle.getBundle(
        new String(packageName+FACTORY));

    if (null != prb) {
      properties = new java.util.Properties();
      java.util.Enumeration keys       = prb.getKeys();

      while (keys.hasMoreElements()) {
        String key = (String) (keys.nextElement());

        properties.setProperty(key, (String) prb.handleGetObject(key));
      }
    }
    return properties;
  }

}
