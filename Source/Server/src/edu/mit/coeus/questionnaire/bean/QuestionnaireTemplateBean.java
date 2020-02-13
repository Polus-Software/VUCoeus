/*
 * QuestionnaireTemplateBean.java
 *
 * Created on February 6, 2009, 12:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.questionnaire.bean;

/**
 *
 * @author keerthyjayaraj
 */
public class QuestionnaireTemplateBean extends QuestionnaireMaintainaceBaseBean{
    
    private String templateFileName;
    private byte[] templateFileBytes;
    
    /** Creates a new instance of QuestionnaireTemplateBean */
    public QuestionnaireTemplateBean() {
    }

    public byte[] getTemplateFileBytes() {
        return templateFileBytes;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public void setTemplateFileBytes(byte[] templateFileBytes) {
        this.templateFileBytes = templateFileBytes;
    }

    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }

}
