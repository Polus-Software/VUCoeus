/*
 * UserAuthAction.java
 *
 * Created on November 1, 2006, 11:41 AM
 */

package edu.mit.coeuslite.utils.auth;

import edu.mit.coeus.user.auth.bean.AuthXMLNodeBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;


/**
 *
 * @author  geot
 */
public class UserSecondaryAuthAction  extends UserAuthAction{

    protected String getLoginMode() throws Exception{
        return CoeusProperties.getProperty(CoeusPropertyKeys.SECONDARY_LOGIN_MODE);
    }
    protected String getAuthenticationActionName() {
        return "/userSecondaryAuthAction.do";
    }

}
