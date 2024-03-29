/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Axis" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.axis.components.net;

import org.apache.axis.AxisProperties;
import org.apache.axis.components.logger.LogFactory;
import org.apache.commons.logging.Log;

import java.util.Hashtable;

/**
 * Class SocketFactoryFactory
 *
 * @author
 * @version %I%, %G%
 */
public class SocketFactoryFactory {

    /** Field log           */
    protected static Log log =
            LogFactory.getLog(SocketFactoryFactory.class.getName());

    /** socket factory */
    private static Hashtable factories = new Hashtable();

    private static final Class classes[] = new Class[] { Hashtable.class };


    static {
        AxisProperties.setClassOverrideProperty(SocketFactory.class,
                                       "axis.socketFactory");

        AxisProperties.setClassDefault(SocketFactory.class,
                                       "org.apache.axis.components.net.DefaultSocketFactory");

        AxisProperties.setClassOverrideProperty(SecureSocketFactory.class,
                                       "axis.socketSecureFactory");

        AxisProperties.setClassDefault(SecureSocketFactory.class,
                                       "org.apache.axis.components.net.JSSESocketFactory");
    }
    
    /**
     * Returns socket factory.<br>
     * Chaged the default SocketFactoryFactory.getFactory method to remove the cache to support the 
     * parameter passing for every request for SocketFactory. <br>
     * It always creates a new instance of SocketFactory instead of maintaining a cache. 
     * This is required for setting the hashtable during the initialization process 
     * 
     * @param protocol Today this only supports "http" & "https".
     * @param attributes
     *
     * @return SocketFactory
     */
    public static synchronized SocketFactory getFactory(String protocol,
                                                        Hashtable attributes) {
//        SocketFactory theFactory = (SocketFactory)factories.get(protocol);
        System.out.println("in new socket factory factory");
        edu.mit.coeus.utils.UtilFactory.log("In overridden SocketFactoryFactory");
        SocketFactory theFactory = null;
//        if (theFactory == null) {
            Object objects[] = new Object[] { attributes };
    
            if (protocol.equalsIgnoreCase("http")) {
                theFactory = (SocketFactory)
                    AxisProperties.newInstance(SocketFactory.class, classes, objects);
            } else if (protocol.equalsIgnoreCase("https")) {
                theFactory = (SecureSocketFactory)
                    AxisProperties.newInstance(SecureSocketFactory.class, classes, objects);
            }
            
//            if (theFactory != null) {
//                factories.put(protocol, theFactory);
//            }
//        }
        return theFactory;
    }
}
