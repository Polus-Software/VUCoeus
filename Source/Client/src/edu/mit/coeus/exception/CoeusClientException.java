/*
 * CoeusClientException.java
 *
 * Created on July 29, 2003, 2:41 PM
 */

package edu.mit.coeus.exception;

/** Used on the Client Side to Propagate Proper Messages
 * thru CoeusOptionPane.
 *
 * Defaults:
 * id = EMPTY
 * messageType = WARNING_MESSAGE
 * messageKey = null
 *
 * @author sharathk
 */
public class CoeusClientException extends java.lang.Exception {
    /**
     * Used to flag that there was a fatal error.
     */
    public static final int ERROR_MESSAGE = 0;
    /**
     * Used to flag an information message.
     */
    public static final int INFORMATION_MESSAGE = 1;
    /**
     * Used to flag an warning message.
     */
    public static final int WARNING_MESSAGE = 2;
    
    /** Character that separates Message Key and Id
     */    
    private static final char SEPARATOR = '.';
    
    private static final String EMPTY = "";
    
    /**
     * Stores the id.
     */
    private String id = EMPTY;
    /**
     * Stores the messageKey.
     */
    private String messageKey = null;
    /**
     * Stores the messageType.
     */
    private int messageType = WARNING_MESSAGE;
    
    /** Stores Exception.
     */    
    private Exception exception;
    
    /**
     * Creates a new instance of <code>CoeusClientException</code> without detail message.
     */
    public CoeusClientException() {
    }
    
    /** Creates an instance of <CODE>CoeusClientException</CODE> which wraps the Exception.
     * @param exception exception to wrap.
     */    
    public CoeusClientException(Exception exception) {
        this.exception = exception;
    }
    
    /** Constructs an instance of <code>CoeusClientException</code> with the specified detail message.
     * @param messageKey message Key.
     */
    public CoeusClientException(String messageKey) {
        setMessageKey(messageKey);
    }
    
    /** Creates new instance of <CODE>CoeusClientException</CODE>
     * This Constructor is for backward compatibility.
     * i.e to support id as int.
     * @param id this is for backward compatibility.
     * this id will be converted and stored as string.
     * @param messageKey message key.
     * @param messageType Possible values are:
     * ERROR_MESSAGE
     * INFORMATION_MESSAGE
     * WARNING_MESSAGE
     */
    public CoeusClientException(int id, String messageKey, int messageType) {
        this(messageKey, messageType);
        this.id = EMPTY + id;
    }
    
     /** creates a new instance of <CODE>CoeusClientException</CODE>
      * @param messageKey message key.
      * @param messageType Possible values are:
      * ERROR_MESSAGE
      * INFORMATION_MESSAGE
      * WARNING_MESSAGE
      */     
    public CoeusClientException(String messageKey, int messageType) {
        //check for message types. if not matching set to error Message.
        if(messageType == ERROR_MESSAGE
        || messageType == INFORMATION_MESSAGE
        || messageType == WARNING_MESSAGE) {

            this.messageType = messageType;
        }
        setMessageKey(messageKey);
    }
    // added by manoj to handle action messages
    
     /** creates a new instance of <CODE>CoeusClientException</CODE>
      * @param CoeusException contains the message type and value
      * ERROR_MESSAGE
      * INFORMATION_MESSAGE
      * WARNING_MESSAGE
      */     
    
    public CoeusClientException(CoeusException ex) {
        //check for message types. if not matching set to error Message.
        this.messageType = ex.getMessageType();
        this.messageKey = ex.getMessage();
    }
    
    /** Getter for property id.
     * @return Value of property id.
     */
    public String getId() {
        return id;
    }
    
    /** Getter for property messageKey.
     * @return Value of property messageKey.
     */
    public java.lang.String getMessageKey() {
        return messageKey;
    }
    
    /** Getter for property messageType.
     * @return Value of property messageType.
     */
    public int getMessageType() {
        return messageType;
    }
    
    /** Getter for property exception.
     * @return Value of property exception.
     */
    public java.lang.Exception getException() {
        return exception;
    }

    /** Setter for property messageKey.
     * @param messageKey New value of property messageKey.
     */
    private void setMessageKey(java.lang.String messageKey) {
        int separatorIndex = messageKey.indexOf(SEPARATOR);
        if(separatorIndex > 0) {
            this.messageKey = messageKey.substring(0, separatorIndex);
            this.id = messageKey.substring(separatorIndex + 1);
        } else {
            this.messageKey = messageKey;
        }
    }
    
    /**
     */    
    public String getMessage() {
        if(exception == null) {
            return messageKey + SEPARATOR + id;
        } else {
            return exception.getMessage();
        }
    }
    
    
    //For Testing Purpose Only
    public static void main(String s[]) {
        CoeusClientException cce;
        cce = new CoeusClientException(new Exception("he he he"));
        cce = new CoeusClientException("Ha Ha Ha");
        //cce = new CoeusClientException("Ha Ha Ha.007");
        System.out.println(cce.getMessage());
    }
    
}
