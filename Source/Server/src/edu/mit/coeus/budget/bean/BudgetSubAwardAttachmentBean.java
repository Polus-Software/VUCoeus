/*
 * BudgetSubAwardAttachmentBean.java
 *
 * Created on September 11, 2006, 12:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/**
 *
 * @author sharathk
 */
public class BudgetSubAwardAttachmentBean extends BudgetBean{
    
    private int subAwardNumber;
    private String contentId;
    private String contentType;
    private byte[] attachment;
    
    /** Creates a new instance of BudgetSubAwardAttachmentBean */
    public BudgetSubAwardAttachmentBean() {
    }

    public int getSubAwardNumber() {
        return subAwardNumber;
    }

    public void setSubAwardNumber(int subAwardNumber) {
        this.subAwardNumber = subAwardNumber;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
        
    public boolean isLike(ComparableBean comparableBean)
    throws CoeusException {
        throw new CoeusException("Do Not Use isLike use QueryEngine.filter instead");
    }

}
