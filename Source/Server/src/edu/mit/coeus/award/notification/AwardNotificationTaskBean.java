/*
 * AwardNotificationTaskBean.java
 *
 * Created on August 29, 2008, 4:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.award.notification;

import edu.mit.coeus.utils.scheduler.TaskBean;

/**
 *
 * @author noorula
 */
public class AwardNotificationTaskBean implements TaskBean{
    private String taskId;
    private long pollingInterval;
    private long delayToStart;
    //private int reportClassCode;
    private int period;
    private int actionCode;
    private String logFileName;
    private int moduleCode;
    private boolean overdue;

    private Integer reportClass;
    private Integer reportTypeCode;
    private Integer frequencyCode;
    private Integer freqBaseCode;
    private Integer ospDistCode;

    /**
     * Creates a new instance of AwardNotificationTaskBean
     */
    public AwardNotificationTaskBean() {
    }

    public String getTaskId() {
        return taskId;
    }

    public long getDelayToStart() {
        return delayToStart;
    }

    public long getPollingInterval() {
        return pollingInterval;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * Setter for property pollInterval.
     * @param pollInterval New value of property pollInterval.
     */
    public void setPollingInterval(java.lang.String pollingInterval) {
        this.pollingInterval = pollingInterval!=null&&pollingInterval.trim().length()>0?
                                Long.parseLong(pollingInterval):0;
    }

    public void setDelayToStart(java.lang.String delayToStart) {
        this.delayToStart = delayToStart!=null&&delayToStart.trim().length()>0?
                                Long.parseLong(delayToStart):0;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    /*public int getReportClassCode() {
        return reportClassCode;
    }

    public void setReportClassCode(java.lang.String reportClassCode) {
        this.reportClassCode = reportClassCode!=null&&reportClassCode.trim().length()>0?
                                Integer.parseInt(reportClassCode):0;
    }*/

    public int getPeriod() {
        return period;
    }

    public void setPeriod(java.lang.String period) {
        this.period = period!=null&&period.trim().length()>0?
                                Integer.parseInt(period):0;
    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(java.lang.String actionCode) {
        this.actionCode = actionCode!=null&&actionCode.trim().length()>0?
                                Integer.parseInt(actionCode):0;
    }
    public int getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(int moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    public boolean isOverdue() {
        return overdue;
    }
    
    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    /**
     * @return the reportClass
     */
    public Integer getReportClass() {
        return reportClass;
    }

    /**
     * @param reportClass the reportClass to set
     */
    public void setReportClass(Integer reportClass) {
        this.reportClass = reportClass;
    }

    /**
     * @return the reportTypeCode
     */
    public Integer getReportTypeCode() {
        return reportTypeCode;
    }

    /**
     * @param reportTypeCode the reportTypeCode to set
     */
    public void setReportTypeCode(Integer reportTypeCode) {
        this.reportTypeCode = reportTypeCode;
    }

    /**
     * @return the frequencyCode
     */
    public Integer getFrequencyCode() {
        return frequencyCode;
    }

    /**
     * @param frequencyCode the frequencyCode to set
     */
    public void setFrequencyCode(Integer frequencyCode) {
        this.frequencyCode = frequencyCode;
    }

    /**
     * @return the freqBaseCode
     */
    public Integer getFreqBaseCode() {
        return freqBaseCode;
    }

    /**
     * @param freqBaseCode the freqBaseCode to set
     */
    public void setFreqBaseCode(Integer freqBaseCode) {
        this.freqBaseCode = freqBaseCode;
    }

    /**
     * @return the ospDistCode
     */
    public Integer getOspDistCode() {
        return ospDistCode;
    }

    /**
     * @param ospDistCode the ospDistCode to set
     */
    public void setOspDistCode(Integer ospDistCode) {
        this.ospDistCode = ospDistCode;
    }

   
}
