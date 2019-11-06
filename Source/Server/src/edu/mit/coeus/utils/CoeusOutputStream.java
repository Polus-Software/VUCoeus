/*
 * CoeusOutputStream.java
 *
 * Created on June 15, 2007, 1:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author sharathk
 */
public class CoeusOutputStream extends OutputStream{
    
    /** Creates a new instance of CoeusOutputStream */
    public CoeusOutputStream() {
    }
    
    public void write(int b) throws IOException {
        //At Any Cost Do Not use
        //System.out Statements here. This class is basically used to supress this statement.
        //Hence if you write System.out statement it would go to endless loop and throw
        //Stack Overflow.
    }

}
