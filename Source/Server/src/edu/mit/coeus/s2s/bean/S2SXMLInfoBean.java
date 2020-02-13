/*
 * S2SXMLInfoBean.java
 *
 * Created on October 19, 2004, 5:30 PM
 */

package edu.mit.coeus.s2s.bean;

import gov.grants.apply.system.footer_v1.GrantSubmissionFooterType;
import gov.grants.apply.system.header_2_0_v2.Header20Type;
import gov.grants.apply.system.header_v1.GrantSubmissionHeaderType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  geot
 */
public class S2SXMLInfoBean  implements IS2SSubmissionData,Serializable{
    private S2SHeader headerParam;
    private GrantSubmissionHeaderType header;
    private Header20Type header20;
    private GrantSubmissionFooterType footer;
    private OpportunityInfoBean localOpportunity;
    private ArrayList selectedOptionalForms;
    /** Creates a new instance of S2SXMLInfoBean */
    public S2SXMLInfoBean() {
    }
    
    /**
     * Getter for property header params.
     * @return Value of property header params.
     */
    public S2SHeader getParams() {
        return headerParam;
    }
    
    /**
     * Setter for property params.
     * @param params New value of property params.
     */
    public void setParams(S2SHeader headerParam) {
        this.headerParam = headerParam;
    }
    
    /**
     * Getter for property header.
     * @return Value of property header.
     */
    public GrantSubmissionHeaderType getHeader() {
        return header;
    }
    
    /**
     * Setter for property header.
     * @param header New value of property header.
     */
    public void setHeader(GrantSubmissionHeaderType header) {
        this.header = header;
    }
    
    /**
     * Getter for property footer.
     * @return Value of property footer.
     */
    public GrantSubmissionFooterType getFooter() {
        return footer;
    }
    
    /**
     * Setter for property footer.
     * @param footer New value of property footer.
     */
    public void setFooter(GrantSubmissionFooterType footer) {
        this.footer = footer;
    }
    
    public OpportunityInfoBean getLocalOpportunity() {
        return this.localOpportunity;
    }
    
    public java.util.List getSelectedOptionalForms() {
        return this.selectedOptionalForms;
    }
    
    /**
     * Setter for property localOpportunity.
     * @param localOpportunity New value of property localOpportunity.
     */
    public void setLocalOpportunity(OpportunityInfoBean localOpportunity) {
        this.localOpportunity = localOpportunity;
    }
    
    /**
     * Setter for property selectedOptionalForms.
     * @param selectedOptionalForms New value of property selectedOptionalForms.
     */
    public void setSelectedOptionalForms(java.util.ArrayList selectedOptionalForms) {
        this.selectedOptionalForms = selectedOptionalForms;
    }
           
    /**
     * Setter for property header20.
     * @param header New value of property header.
     */
    public void setHeader20(Header20Type header20) {
        this.header20 = header20;
    }
    /**
     * Getter for property header20.
     * @return Value of property header.
     */    
    public Header20Type getHeader20() {
        return header20;
    }
    
}
