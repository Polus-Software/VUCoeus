/*
 * KerberosAuthService.java
 *
 * Created on August 29, 2006, 3:12 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.user.auth;

import edu.mit.coeus.bean.LoginBean;
import edu.mit.coeus.bean.ValidateUserTxnBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import java.io.IOException;
import java.util.Hashtable;

/**
 *
 * @author  Geo Thomas
 */
public class KerberosAuthService extends AuthServiceServerBase{
    private Hashtable data;
    /** Creates a new instance of KerberosAuthService */
    public KerberosAuthService() {
    }
    
    public boolean authenticate() throws CoeusException {
        ValidateUserTxnBean validUserBean = new ValidateUserTxnBean() ;
        RequesterBean requester = (RequesterBean)data.get(RequesterBean.class.getName());
        String kerbUserId = (String)requester.getDataObject();
        if (validUserBean.isThisUserValidUser(kerbUserId)) {
            setResponse(kerbUserId) ;
            return true;
        }else{
            throw new CoeusException("exceptionCode.100004");
        }
    }
    public void init(java.util.Properties props) {
    }
    
    public void setParams(java.util.Hashtable data) {
        this.data = data;
    }
    
}
