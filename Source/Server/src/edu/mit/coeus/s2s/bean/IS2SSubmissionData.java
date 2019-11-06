/*
 * IS2SSubmissionData.java
 *
 * Created on January 6, 2005, 3:54 PM
 */

package edu.mit.coeus.s2s.bean;

import gov.grants.apply.system.footer_v1.GrantSubmissionFooterType;
import gov.grants.apply.system.header_2_0_v2.Header20Type;
import gov.grants.apply.system.header_v1.GrantSubmissionHeaderType;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author  geot
 */
public interface IS2SSubmissionData {
    public S2SHeader getParams();
    public GrantSubmissionHeaderType getHeader();
    public Header20Type getHeader20(); 
    public GrantSubmissionFooterType getFooter();
    public OpportunityInfoBean getLocalOpportunity();
    public List getSelectedOptionalForms();
}
