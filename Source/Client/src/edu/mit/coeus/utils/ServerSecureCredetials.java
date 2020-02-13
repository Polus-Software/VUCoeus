/*
 * ServerSecureCredetials.java
 *
 * Created on February 22, 2007, 11:11 AM
 */

package edu.mit.coeus.utils;

/**
 *
 * @author  geot
 */
public class ServerSecureCredetials {
    private static String serverSecureKey;
    //JIRA COEUSQA 2527 - START
    private static String sessionId;
    //JIRA COEUSQA 2527 - END
    public static void setServerSecureKey(String key){
        if(key!=null && serverSecureKey==null){
            serverSecureKey = key;
        }
    }
    public static String getServerSecureKey(){
        return serverSecureKey;
    }

    /**
     * @return the sessionId
     */
    public static String getSessionId() {
        return sessionId;
    }

    /**
     * @param aSessionId the sessionId to set
     */
    public static void setSessionId(String aSessionId) {
        if(aSessionId!=null && sessionId==null){
            sessionId = aSessionId;
        }
    }
    
}
