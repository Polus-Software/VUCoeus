/* 
 * RelaseLockServlet.java
 *
 * Created on December 2, 2002, 12:39 PM
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import javax.servlet.*;
import javax.servlet.http.*;




import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.security.CoeusCipher;
import edu.mit.coeus.utils.security.SecureSeedTxnBean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
/**
 *
 * @author  phani
 * @version
 */
public abstract class CoeusBaseServlet extends HttpServlet {
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
  
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
    public void releaseLock(String refId) throws Exception{
//        if (refId != null) {
//            CoeusFunctions coeusFunctions = new CoeusFunctions();
//            //coeusFunctions.releaseUpdateLock(refId);
//        }
    }
    protected boolean isValidRequest(RequesterBean requester) throws CoeusException{
        try {
            String secureSeed = (String)getServletContext().
                                getAttribute(CoeusCipher.SERVLET_SECURE_SEED);
            if(secureSeed==null)
                secureSeed = new SecureSeedTxnBean().getServerSecureSeedValue(
                                    CoeusCipher.SERVLET_SECURE_SEED);
            boolean success = CoeusCipher.isValidRquest(requester, secureSeed);
            if(!success){
                secureSeed = new SecureSeedTxnBean().getServerSecureSeedValue(
                                    CoeusCipher.SERVLET_SECURE_SEED);
                success = CoeusCipher.isValidRquest(requester, secureSeed);
                if(success){
                    getServletContext().setAttribute(CoeusCipher.SERVLET_SECURE_SEED,
                                                    secureSeed);
                }
            }
            if(!success) throw new CoeusException("Invalid servlet request ");
            return success;
        }catch(Throwable ex){
            UtilFactory.log(ex.getMessage(),ex,"CoeusBaseServlet","validRequest");
            throw new CoeusException("Invalid servlet request : "+ex.getMessage());
        }
    }
}
