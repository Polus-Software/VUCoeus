/*
 * CoeusOutputPlugIn.java
 *
 * Created on June 15, 2007, 2:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils;

import java.io.PrintStream;
import javax.servlet.ServletException;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 *
 * @author sharathk
 */
public class CoeusOutputPlugIn implements PlugIn{
    
    /**
     * Creates a new instance of CoeusOutputPlugIn
     */
    public CoeusOutputPlugIn() {
    }
    
    /**
     * <p>Receive notification that the specified module is being
     * started up.</p>
     *
     * @param servlet ActionServlet that is managing all the
     *  modules in this web application
     * @param config ModuleConfig for the module with which
     *  this plug-in is associated
     *
     * @exception ServletException if this <code>PlugIn</code> cannot
     *  be successfully initialized
     */
    public void init(ActionServlet servlet, ModuleConfig config) throws ServletException {
        CoeusOutputStream coeusOutputStream = new CoeusOutputStream();
        System.setOut(new PrintStream(coeusOutputStream));
    }
    
    /**
     * <p>Receive notification that our owning module is being
     * shut down.</p>
     */
    public void destroy() {
    }
    
    public void setOutput(String output){
        
    }
    
}
