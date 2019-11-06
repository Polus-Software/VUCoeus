/*
 * DecoratorBean.java
 *
 * Created on March 18, 2008, 11:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document.decorator;

import java.util.ArrayList;
import java.util.List;

/**
 * All Decorations for a page(header, footer, watermark) is encapsulated in this bean.
 * @author sharathk
 */
public class DecoratorBean {
    
    /**
     * proposal status code for the decoration
     */
    private String status;
    /**
     * document Type
    */
    private String docType;
    /**
     * header decoration collection
     */
    private List header;
    /**
     * footer decoration collection
     */
    private List footer;
    /**
     * watermark decoration collection
     */
    private List watermark;

    public List getHeader() {
        return header;
    }

    public void setHeader(CommonBean commonBean) {
        if(header == null){
            header = new ArrayList(3);
        }
        header.add(commonBean);
    }

    public List getFooter() {
        return footer;
    }

    public void setFooter(CommonBean commonBean) {
        if(footer == null){
            footer = new ArrayList(3);
        }
        footer.add(commonBean);
    }

    public List getWatermark() {
        return watermark;
    }

    public void setWatermark(CommonBean commonBean) {
        if(watermark == null){
            watermark = new ArrayList(3);
        }
        watermark.add(commonBean);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }
    
    
}
