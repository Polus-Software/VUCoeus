/*
 * PollingInfoBean.java
 *
 * Created on October 2, 2006, 10:22 AM
 */

package edu.mit.coeus.s2s.util.polling;

import edu.mit.coeus.utils.scheduler.TaskBean;

/**
 *
 * @author  geot
 */
public class PollingInfoBean implements TaskBean{
    private String taskId;
    private long pollingInterval;
    //Code modified for case#3856 - Report Notification
    //private long delayToStart;
    private long delayToStart = -1;
    private long mailInterval;
    private StatusBean[] statuses;
    private MailInfoBean[] mailInfoList;
    private boolean terminate;
    private int stopPollInterval;
    private String logFileName;
    
    /** Creates a new instance of PollingInfoBean */
    public PollingInfoBean() {
    }
    
    public StatusBean createStatusBeanInstance(){
        return new StatusBean();
    }
    
    public MailInfoBean createMailInfoBeanInstance(){
        return new MailInfoBean();
    }
    
    /**
     * Getter for property grpId.
     * @return Value of property grpId.
     */
    public java.lang.String getTaskId() {
        return taskId;
    }
    
    /**
     * Setter for property grpId.
     * @param grpId New value of property grpId.
     */
    public void setTaskId(java.lang.String taskId) {
        this.taskId = taskId;
    }
    
    /**
     * Getter for property statuses.
     * @return Value of property statuses.
     */
    public edu.mit.coeus.s2s.util.polling.PollingInfoBean.StatusBean[] getStatuses() {
        return this.statuses;
    }
    
    /**
     * Setter for property statuses.
     * @param statuses New value of property statuses.
     */
    public void setStatuses(edu.mit.coeus.s2s.util.polling.PollingInfoBean.StatusBean[] statuses) {
        this.statuses = statuses;
    }
    
    /**
     * Getter for property mailInfo.
     * @return Value of property mailInfo.
     */
    public MailInfoBean[] getMailInfoList() {
        return mailInfoList;
    }
    
    
    /**
     * Setter for property pollInterval.
     * @param pollInterval New value of property pollInterval.
     */
    public void setPollingInterval(java.lang.String pollingInterval) {
        this.pollingInterval = pollingInterval!=null&&pollingInterval.trim().length()>0?
                                Long.parseLong(pollingInterval):0;
    }
    
    public long getPollingInterval() {
        return this.pollingInterval;
    }
    
    /**
     * Getter for property mailInterval.
     * @return Value of property mailInterval.
     */
    public long getMailInterval() {
        return mailInterval;
    }
    
    /**
     * Setter for property mailInterval.
     * @param mailInterval New value of property mailInterval.
     */
    public void setMailInterval(java.lang.String mailInterval) {
        this.mailInterval = mailInterval!=null&&mailInterval.trim().length()>0?
                            Long.parseLong(mailInterval):0;
    }
    
    /**
     * Getter for property terminate.
     * @return Value of property terminate.
     */
    public boolean isTerminate() {
        return terminate;
    }

    /**
     * Setter for property terminate.
     * @param terminate New value of property terminate.
     */
    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }

    /**
     * Getter for property stopPollInterval.
     * @return Value of property stopPollInterval.
     */
    public int getStopPollInterval() {
        return stopPollInterval;
    }

    /**
     * Setter for property stopPollInterval.
     * @param stopPollInterval New value of property stopPollInterval.
     */
    public void setStopPollInterval(String stopPollInterval) {
        this.stopPollInterval = (stopPollInterval!=null && 
                                    stopPollInterval.trim().length()>0)?
                                        Integer.parseInt(stopPollInterval):0;
        if(this.stopPollInterval>0) setTerminate(true);
    }
    
    public long getDelayToStart() {
        return delayToStart;
    }
    
    public String getLogFileName() {
        return logFileName;
    }    
    
    /**
     * Setter for property logFileName.
     * @param logFileName New value of property logFileName.
     */
    public void setLogFileName(java.lang.String logFileName) {
        this.logFileName = logFileName;
    }    
    
    /**
     * Setter for property delayToStart.
     * @param delayToStart New value of property delayToStart.
     */
    public void setDelayToStart(long delayToStart) {
        this.delayToStart = delayToStart;
    }
    
    /**
     * Setter for property mailInfoList.
     * @param mailInfoList New value of property mailInfoList.
     */
    public void setMailInfoList(edu.mit.coeus.s2s.util.polling.PollingInfoBean.MailInfoBean[] mailInfoList) {
        this.mailInfoList = mailInfoList;
    }
    
    class StatusBean{
        private int code;
        private String value;
        public StatusBean(){
        }
        public StatusBean(String code,String value){
            this.code = Integer.parseInt(code);
            this.value = value;
        }
        
        /**
         * Getter for property code.
         * @return Value of property code.
         */
        public int getCode() {
            return code;
        }
        
        /**
         * Setter for property code.
         * @param code New value of property code.
         */
        public void setCode(String code) {
            this.code = (code!=null&&code.trim().length()>0)?
                        Integer.parseInt(code):0;
        }
        
        /**
         * Getter for property value.
         * @return Value of property value.
         */
        public java.lang.String getValue() {
            return value;
        }
        
        /**
         * Setter for property value.
         * @param value New value of property value.
         */
        public void setValue(java.lang.String value) {
            this.value = value;
        }
        
        
    }
    
    class MailInfoBean{
        private String to;
        private String from;
        private String cc;
        private String bcc;
        private String message;
        private String subject;
        private String footer;
        private String dunsNumber;
        public MailInfoBean(){
        }
        /**
         * Getter for property to.
         * @return Value of property to.
         */
        public java.lang.String getTo() {
            return to;
        }
        
        /**
         * Setter for property to.
         * @param to New value of property to.
         */
        public void setTo(java.lang.String to) {
            this.to = to;
        }
        
        /**
         * Getter for property from.
         * @return Value of property from.
         */
        public java.lang.String getFrom() {
            return from;
        }
        
        /**
         * Setter for property from.
         * @param from New value of property from.
         */
        public void setFrom(java.lang.String from) {
            this.from = from;
        }
        
        /**
         * Getter for property cc.
         * @return Value of property cc.
         */
        public java.lang.String getCc() {
            return cc;
        }
        
        /**
         * Setter for property cc.
         * @param cc New value of property cc.
         */
        public void setCc(java.lang.String cc) {
            this.cc = cc;
        }
        
        /**
         * Getter for property bcc.
         * @return Value of property bcc.
         */
        public java.lang.String getBcc() {
            return bcc;
        }
        
        /**
         * Setter for property bcc.
         * @param bcc New value of property bcc.
         */
        public void setBcc(java.lang.String bcc) {
            this.bcc = bcc;
        }
        
        /**
         * Getter for property message.
         * @return Value of property message.
         */
        public java.lang.String getMessage() {
            return message;
        }
        
        /**
         * Setter for property message.
         * @param message New value of property message.
         */
        public void setMessage(java.lang.String message) {
            this.message = message;
        }
        
        /**
         * Getter for property subject.
         * @return Value of property subject.
         */
        public java.lang.String getSubject() {
            return subject;
        }
        
        /**
         * Setter for property subject.
         * @param subject New value of property subject.
         */
        public void setSubject(java.lang.String subject) {
            this.subject = subject;
        }
        
        /**
         * Getter for property footer.
         * @return Value of property footer.
         */
        public java.lang.String getFooter() {
            return footer;
        }
        
        /**
         * Setter for property footer.
         * @param footer New value of property footer.
         */
        public void setFooter(java.lang.String footer) {
            this.footer = footer;
        }
        
        /**
         * Getter for property dunsNumber.
         * @return Value of property dunsNumber.
         */
        public java.lang.String getDunsNumber() {
            return dunsNumber;
        }
        
        /**
         * Setter for property dunsNumber.
         * @param dunsNumber New value of property dunsNumber.
         */
        public void setDunsNumber(java.lang.String dunsNumber) {
            this.dunsNumber = dunsNumber;
        }
        
    }
}
