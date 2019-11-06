/*
 * DetectPluginApplet.java
 *
 * Created on October 27, 2004, 10:37 AM
 * @author coeus-dev-team
 */

import java.awt.*;

/**
 * Applet used to detect Java version on client machine. * 
 */
public class DetectPluginApplet extends java.applet.Applet {
    
    /** Initialization method that will be called after the applet is loaded
     *  into the browser.
     */
    public void init() {
        add(new Label("DetectPluginApplet")); 
        //System.out.println("inside init method");
    }
    
    public String getJavaVersion(){
        String javaVersion = System.getProperty("java.version");
        System.out.println("javaVersion in applet: "+javaVersion);
        return javaVersion;
    }
}
