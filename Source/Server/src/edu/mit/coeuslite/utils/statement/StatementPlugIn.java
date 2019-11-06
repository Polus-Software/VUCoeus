/*
 * StatementPlugin.java
 *
 * Created on April 22, 2005, 4:24 PM
 */

package edu.mit.coeuslite.utils.statement;

import java.io.File;
import java.io.FileNotFoundException;
import javax.servlet.ServletException;
import org.apache.struts.action.PlugIn;

/**
 *
 * @author  sharathk
 */
public class StatementPlugIn implements PlugIn{
    private String xmlFile;
    
    /** Creates a new instance of StatementPlugin */
    public StatementPlugIn() {
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
    public void init(org.apache.struts.action.ActionServlet servlet, org.apache.struts.config.ModuleConfig config)throws ServletException {
        String appPath = servlet.getServletContext().getRealPath("");
        String xmlFilePath = appPath+"/"+xmlFile;
        StatementContentHandler statementContentHandler = new StatementContentHandler();
        try{
            statementContentHandler.setXmlFilePath(xmlFilePath);
            servlet.getServletContext().setAttribute(StatementConstants.STATEMENT_HANDLER, statementContentHandler);
        }catch (FileNotFoundException fileNotFoundException) {
            throw new ServletException(fileNotFoundException);
        }
    }
    
    /**
     * <p>Receive notification that our owning module is being
     * shut down.</p>
     */
    public void destroy() {
        System.out.println("Destroy Called");
    }
    
    /**
     * Getter for property xmlFile.
     * @return Value of property xmlFile.
     */
    public java.lang.String getXmlFile() {
        return xmlFile;
    }
    
    /**
     * Setter for property xmlFile.
     * @param xmlFile New value of property xmlFile.
     */
    public void setXmlFile(java.lang.String xmlFile) {
        this.xmlFile = xmlFile;
    }
    
}
