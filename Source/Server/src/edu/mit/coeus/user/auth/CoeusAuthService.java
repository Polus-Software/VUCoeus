/*
 * ClientAuthService.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * Created on August 25, 2006, 11:23 AM
 */

package edu.mit.coeus.user.auth;

import edu.mit.coeus.exception.CoeusException;
import java.util.Hashtable;
import java.util.Properties;

/**
 *
 * @author  Geo Thomas
 */
public interface CoeusAuthService {
    public void init(Properties props);
    public void addParam(Object key, Object value);
    public void setParams(Hashtable data);
    public boolean authenticate() throws CoeusException;
    public void addResponseListener(AuthResponseListener lsnr);
}
