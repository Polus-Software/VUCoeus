/*
 * CoeusClientAuthFactory.java
 *
 * Created on August 24, 2006, 2:12 PM
  * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
*/

package edu.mit.coeus.user.auth;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.auth.bean.AuthXMLNodeBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 *
 * @author  Geo Thomas
 */
public class ClientAuthServiceFactory {
    private static CoeusAuthService clientAuthService;
    /** Creates a new instance of CoeusClientAuthFactory */
    private ClientAuthServiceFactory() {
    }
    public static synchronized CoeusAuthService getInstance(AuthXMLNodeBean authBean) throws CoeusException{
        if(clientAuthService!=null) return clientAuthService;
        try{
            Class authClass = Class.forName(authBean.getClientClass());
            CoeusAuthService clientAuthService = (CoeusAuthService)authClass.newInstance();
            Class[] initParamTypes = {Properties.class};
            Method initMeth = authClass.getMethod("init", initParamTypes);
            Properties[] propArr = {authBean.getAuthProps()};
            initMeth.invoke(clientAuthService, propArr);
            return clientAuthService;
        }catch(NoSuchMethodException ex){
            ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }catch(InvocationTargetException ex){
            ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }catch(IllegalAccessException ex){
            ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }catch(InstantiationException ex){
            ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }
    }
}
