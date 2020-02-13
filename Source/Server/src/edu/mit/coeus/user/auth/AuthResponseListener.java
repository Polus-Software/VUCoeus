/*
 * ResponseListener.java
 *
 * Created on August 28, 2006, 6:12 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.user.auth;

import edu.mit.coeus.exception.CoeusException;

/**
 *
 * @author  Geo Thomas
 */
public interface AuthResponseListener {
    
    public void respond(Object res) throws CoeusException;
}
