/*
 * Created on Apr 13, 2006
 * 
 * 
 * */
package edu.ucsd.coeus.personalization;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Wrapper Logger for Coeus client.
 * @author Robert
 *
 */
public class ClientLogger  {

	public  void debug(String msg, Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter outerror = new PrintWriter(writer);
        e.printStackTrace(outerror);
        outerror.close();
        System.out.println(msg + "\n" + writer.toString());
    }

    public  void debug(String msg) {
        System.out.println(msg + "\n" );        
    }
    
    public  void error(String msg, Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter outerror = new PrintWriter(writer);
        e.printStackTrace(outerror);
        outerror.close();
        System.out.println(msg + "\n" + writer.toString());        
    }
    
    public  void error(Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter outerror = new PrintWriter(writer);
        e.printStackTrace(outerror);
        outerror.close();
        System.out.println( writer.toString());
    }
    

    public  void error(String msg) {
        System.out.println(msg + "\n");    	
    }
    
    public  void warn(String msg, Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter outerror = new PrintWriter(writer);
        e.printStackTrace(outerror);
        outerror.close();
        System.out.println(msg + "\n" + writer.toString());        
    }

    public  void warn(String msg) {
        System.out.println(msg + "\n");    	
    }
    
    

}
