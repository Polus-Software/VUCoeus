/*
 * URLOpener.java
 *
 * Created on March 4, 2005, 10:47 AM
 */

package edu.mit.coeus.gui;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.applet.AppletContext;
// JM 5-18-2012 added for Mac exception
import java.lang.reflect.Method;
//import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author  geot
 */
public class URLOpener {
    
    /** Creates a new instance of URLOpener */
    private URLOpener() {
    }
    /*
     *Method to open the url in browser. 
     *@param url could be Relative URL of the file which created on the server document root.
     *or the complete url starts from <code>http://</code>
     */
    public synchronized static void openUrl(String url) throws CoeusException{
        String convertedUrl = url.replace('\\', '/') ; // this is fix for Mac
        try{
            URL completeUrl = new URL( url.startsWith("http") == true ?
                convertedUrl : CoeusGuiConstants.CONNECTION_URL + convertedUrl );
            openUrl(completeUrl);    
        }catch(Exception mux){
            System.out.println("Exception while opening url");
            mux.printStackTrace();
            throw new CoeusException(mux.getMessage());
        }
    }
    
     /*
     *Method to open the url in browser. 
     *@param url URL of the file.
     *or the complete url starts from <code>http://</code>
     */
    public synchronized static void openUrl(URL url) throws CoeusException {
        AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        // we will have to use jnlp library in order to showDocument when Applet
        // is converted to application, as in application appletContext will be null.
        try{
            if (coeusContxt != null){
                coeusContxt.showDocument( url, "_blank" );
            }else{
                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)
                javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
// JM 7-7-2011 changed this call to avoid post-virus failure of showDocument                
                //bs.showDocument( url );
                openUrlFromSystem(url.toString());
// END
            }
        }catch (javax.jnlp.UnavailableServiceException unavailableServiceException) {
            //TRy to open url in IExplore
            try{
                openUrlFromSystem(url.toString());
            }catch (Exception exception) {
                throw new CoeusException(exception.getMessage());
            }
        }catch(Exception mux){
            System.out.println("Exception while opening url");
            mux.printStackTrace();
            throw new CoeusException(mux.getMessage());
        }
    }
    
    private static void openUrlFromSystem(String url)throws Exception {
        String osName = System.getProperty("os.name");
        
        if (osName.startsWith("Mac OS")) {
        	// JM 10-12-2011 fix for Mac error
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] {String.class});
            openURL.invoke(null, new Object[] {url});
            //throw new CoeusException("Mac Systems not Supported as of now");
        } else if (osName.startsWith("Windows"))
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        else { //assume Unix or Linux
            String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++)
                if (Runtime.getRuntime().exec(
                    new String[] {"which", browsers[count]}).waitFor() == 0)
                    browser = browsers[count];
            if (browser == null)
                throw new Exception("Could not find web browser");
            else
                Runtime.getRuntime().exec(new String[] {browser, url});
        }
    }

    
}
