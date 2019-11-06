/*
 * @(#)CoeusSearchException.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on July 13, 2002, 3:19 PM
 * @author  Geo Thomas
 * @version 1.0
 */
package edu.mit.coeus.search.exception;
/*
 *  Exception class for the Coeus Search.
 */
public class CoeusSearchException extends java.lang.Exception {

    String msg = null;
    /**
     * Creates new <code>CoeusSearchException</code> without detail message.
     */
    public CoeusSearchException() {
    }


    /**
     * Constructs an <code>CoeusSearchException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CoeusSearchException(String msg) {
        super(msg);
        this.msg = msg;
    }
    /**
     *  The method used to get the exception message
     *  @return exception message
     */
    public String getMessage(){
        return msg;
    }
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return string representation of message 
     */
    public String toString(){
        return msg;
    }
}


