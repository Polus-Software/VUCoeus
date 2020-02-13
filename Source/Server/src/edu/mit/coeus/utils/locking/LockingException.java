/*
 * LockingException.java
 *
 * Created on August 5, 2004, 6:11 PM
 */

package edu.mit.coeus.utils.locking;

import edu.mit.coeus.exception.CoeusException;
/**
 *
 * @author  shivakumarmj
 */
public class LockingException extends CoeusException{
    
    private String errorMessage;    
    private int errorId;
    private LockingBean lockingBean;
    
    /** Creates a new instance of LockingException */
    public LockingException() {
    }
    
    public LockingException(String msg) {      
        super(msg);
        this.errorMessage = msg;
    }
    
    /** Getter for property lockingBean.
     * @return Value of property lockingBean.
     *
     */
    public edu.mit.coeus.utils.locking.LockingBean getLockingBean() {
        return lockingBean;
    }    
    
    /** Setter for property lockingBean.
     * @param lockingBean New value of property lockingBean.
     *
     */
    public void setLockingBean(edu.mit.coeus.utils.locking.LockingBean lockingBean) {
        this.lockingBean = lockingBean;
    }
    
    public int getErrorId() {
        int index=0;
        if(errorMessage!=null && ((index =  errorMessage.indexOf("exceptionCode"))!=-1)){
            try{
                errorId = Integer.parseInt(errorMessage.substring((index+"exceptionCode".length()+1),errorMessage.length()));
            }catch(java.lang.NumberFormatException ex){}
        }
        return errorId;
    }
    
    
//    public String toString(){        
//        return "Sorry, this award no. is in use..";
//    }    
    
    /** Getter for property errorMessage.
     * @return Value of property errorMessage.
     *
     */
    public java.lang.String getErrorMessage() {
        return errorMessage;
    }
    
    /** Setter for property errorMessage.
     * @param errorMessage New value of property errorMessage.
     *
     */
    public void setErrorMessage(java.lang.String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * @param args the command line arguments
     */
    
    
}
