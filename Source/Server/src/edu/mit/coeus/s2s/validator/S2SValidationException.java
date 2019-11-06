/*
 * S2SValidationException.java
 *
 * Created on December 27, 2004, 12:14 PM
 */

package edu.mit.coeus.s2s.validator;

import edu.mit.coeus.exception.CoeusException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author  geot
 */
public class S2SValidationException extends Exception {
    public static final int CONFIRMATION = 3;
    public static final int WARNING = 0;
    public static final int ERROR = 1;
    public static final int FATAL_ERROR = 2;
    private ArrayList errors;
    private ArrayList errMsgList;
    private boolean fatalErrFlag;
    private boolean errorFlag;
    private boolean warningFlag;
    private boolean confFlag;
    private String mainError;
    private String confirmMsg;
    private String oppSchemaUrl;
    private String oppInstrUrl;
    
    /**
     * Creates a new instance of <code>S2SValidationException</code> without detail message.
     */
    public S2SValidationException() {
        this("");
    }
    
    /**
     * Creates a new instance of <code>S2SValidationException</code> without detail message.
     */
    public S2SValidationException(Throwable ex) {
        super(ex);
        errors = new ArrayList();
        mainError = ex.getMessage();
    }
    
    /**
     * Constructs an instance of <code>S2SValidationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public S2SValidationException(String msg) {
        super(msg);
        errors = new ArrayList();
        mainError = msg;
    }
    public java.util.ArrayList getErrors() {
        return errors;
    }
    /**
     * Getter for property errors.
     * @return Value of property errors.
     */
    public java.util.ArrayList getMessages(int mode) {
        switch (mode){
            case(CONFIRMATION):
                if(!confFlag) return null;
                break;
            case(ERROR):
                if(!errorFlag) return null;
                break;
            case(FATAL_ERROR):
                if(!fatalErrFlag) return null;
                break;
            case(WARNING):
                if(!warningFlag) return null;
                break;
        }
        ArrayList tempErrList = new ArrayList();
        for(int i=0;i<errors.size();i++){
            ErrorBean err = (ErrorBean)errors.get(i);
            if(err.getSeverity()==mode){
                tempErrList.add(err);
            }
        }
        return tempErrList;
    }
    /**
     * Setter for property errors.
     * @param errors New value of property errors.
     */
    public void addError(Object msgObj,int seve) {
        addError(msgObj,seve,null);
    }
    public void addError(Object msgObj,int seve,String ns) {
        fatalErrFlag = (fatalErrFlag || (seve==FATAL_ERROR));
        errorFlag = (errorFlag || (seve==ERROR));
        warningFlag = (warningFlag || (seve==WARNING));
        confFlag = (confFlag || (seve==CONFIRMATION));
//        if(errMsgList==null) errMsgList=new ArrayList();
//        errMsgList.add(msg);
        errors.add(new ErrorBean(msgObj,seve,ns));
    }
    public class ErrorBean implements Serializable{
        private Object msgObj;
        private String ns;
        private int sev;
        public ErrorBean(Object msgObj,int severity){
            this(msgObj,severity,null);
        }
        public ErrorBean(Object msgObj,int severity,String ns){
            this.msgObj = msgObj;
            this.sev = severity;
            this.ns = ns;
        }
        
        /**
         * Getter for property msg.
         * @return Value of property msg.
         */
        public String getMsg() {
            return msgObj.toString();
        }
        
        /**
         * Setter for property msg.
         * @param msg New value of property msg.
         */
        public void setMsg(Object msgObj) {
            this.msgObj = msgObj;
        }
        
        /**
         * Getter for property sev.
         * @return Value of property sev.
         */
        public int getSeverity() {
            return sev;
        }
        
        /**
         * Setter for property sev.
         * @param sev New value of property sev.
         */
        public void setSeverity(int sev) {
            this.sev = sev;
        }
        
        public String toString(){
            return "<tr><td>"+getMsg()+"</td></tr>";
        }
        
        /**
         * Getter for property msgObj.
         * @return Value of property msgObj.
         */
        public java.lang.Object getMsgObj() {
            return msgObj;
        }
        
        /**
         * Setter for property msgObj.
         * @param msgObj New value of property msgObj.
         */
        public void setMsgObj(java.lang.Object msgObj) {
            this.msgObj = msgObj;
        }
        
        /**
         * Getter for property ns.
         * @return Value of property ns.
         */
        public java.lang.String getNs() {
            return ns;
        }
        
        /**
         * Setter for property ns.
         * @param ns New value of property ns.
         */
        public void setNs(java.lang.String ns) {
            this.ns = ns;
        }
        
    }
    public String getMessage(){
        return mainError;
    }
    public String getHtmlOutput(){
        String htmlOutput = "<html><table border=\"1\"><tr><td><b>"+mainError+"</b></td></tr>"+
                            errors.toString().replace(',',' ').replace('[',' ').replace(']',' ')+
                            "</table></html>";
        return htmlOutput;
    }
    
    /**
     * Getter for property mainError.
     * @return Value of property mainError.
     */
    public java.lang.String getMainError() {
        return mainError;
    }
    
    /**
     * Setter for property mainError.
     * @param mainError New value of property mainError.
     */
    public void setMainError(java.lang.String mainError) {
        this.mainError = mainError;
    }
    
    /**
     * Getter for property confirmMsg.
     * @return Value of property confirmMsg.
     */
    public java.lang.String getConfirmMsg() {
        return confirmMsg;
    }
    
    /**
     * Setter for property confirmMsg.
     * @param confirmMsg New value of property confirmMsg.
     */
    public void setConfirmMsg(java.lang.String confirmMsg) {
        this.confirmMsg = confirmMsg;
    }
    
    /**
     * Getter for property oppSchemaUrl.
     * @return Value of property oppSchemaUrl.
     */
    public java.lang.String getOppSchemaUrl() {
        return oppSchemaUrl;
    }
    
    /**
     * Setter for property oppSchemaUrl.
     * @param oppSchemaUrl New value of property oppSchemaUrl.
     */
    public void setOppSchemaUrl(java.lang.String oppSchemaUrl) {
        this.oppSchemaUrl = oppSchemaUrl;
    }
    
    /**
     * Getter for property oppInstrUrl.
     * @return Value of property oppInstrUrl.
     */
    public java.lang.String getOppInstrUrl() {
        return oppInstrUrl;
    }
    
    /**
     * Setter for property oppInstrUrl.
     * @param oppInstrUrl New value of property oppInstrUrl.
     */
    public void setOppInstrUrl(java.lang.String oppInstrUrl) {
        this.oppInstrUrl = oppInstrUrl;
    }
    
}
