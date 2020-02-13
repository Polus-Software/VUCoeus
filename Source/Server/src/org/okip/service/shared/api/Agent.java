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
 * $Source: /cvs/oki/tech/src/org/okip/service/shared/api/Agent.java,v $
 */

/**
 * General use Agent.
 *<p>
 * Licensed under the {@link org.okip.service.ApiLicense MIT OKI&#153; API Definition License}.
 *
 * @version $Name:  $ / $Revision:   1.0  $ / $Date:   Jan 14 2003 15:29:06  $
*/

public class Agent
implements java.io.Serializable {
  private String name = null;
  private org.okip.service.shared.api.Agent proxy = null;

  /**
   * Get the name of this Agent.
   *
   *
   * @return name
   *
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set the name of this Agent.
   *
   *
   * @return name
   *
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Add a proxy of this Agent.
   *
   * The proxy property allows this agent to add proxy Agent
   * classes from various APIs.
   *
   * @param proxyClass
   *
   */
  public final void addProxy(org.okip.service.shared.api.Agent proxy) {
    org.okip.service.shared.api.Agent test = this;
    while(null != test) {
      if(test.equals(proxy)) {
        return;
      }
      if(null != test.getProxy()) {
        test = test.getProxy();
      }
      else {
        test.setProxy(proxy);
        return;
      }
    }
  }

  /**
   * Remove a proxy of this Agent.
   *
   * The proxy property allows this agent to remove proxy Agent
   * classes from various APIs.
   *
   * @param proxyClass
   *
   */
  public final void removeProxy(org.okip.service.shared.api.Agent proxy) {
    if(null != proxy) {
      org.okip.service.shared.api.Agent test = this;
      while(null != test) {
        if(proxy.equals(test.getProxy())) {
          test.setProxy(test.getProxy().getProxy());
          return;
        }
        test = test.getProxy();
      }
    }
  }

  /**
   * Get the proxy of this Agent.
   *
   * The proxy property allows an agent to link Agent
   * classes from various APIs.
   *
   * @return proxy
   *
   */
  public final org.okip.service.shared.api.Agent getProxy() {
    return this.proxy;
  }
  

  /**
   * Get the proxy of this Agent for a given proxy class.
   *
   * The proxy property allows an agent to link Agent
   * classes from various APIs. A specific proxy is
   * selected using the class of the Agent to be returned.
   *
   * @param proxyClass
   * @return proxy
   *
   */
  public final org.okip.service.shared.api.Agent getProxy(Class proxyClass) {
    if(null != proxyClass) {
      org.okip.service.shared.api.Agent test = this;
      while ((null != test) && !proxyClass.isInstance(test)) {
      test = test.getProxy();
      }
      return test;
    }
    return null;
  }

  /**
   * Set the proxy of this Agent.
   *
   * The proxy property allows this agent to set proxy Agent
   * from various APIs.
   *
   * @param proxyClass
   *
   */
  public final void setProxy(org.okip.service.shared.api.Agent proxy) {
    this.proxy = proxy;
  }

}
