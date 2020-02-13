/*
 * IS2SDataTxn.java
 *
 * Created on January 6, 2005, 4:16 PM
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author  geot
 */
public interface IS2SDataTxn {
    public ArrayList getSubmissionList(S2SHeader params) throws Exception;
    public ArrayList getSelectedOptionalForms(S2SHeader params) throws Exception;
    public OpportunityInfoBean getLocalOpportunity(S2SHeader params) throws Exception;
    public ApplicationInfoBean getSubmissionDetails(S2SHeader params) throws Exception;
    public IS2SSubmissionData getSubmissionData(S2SHeader params) throws Exception;
    public boolean addUpdDeleteSubmissionDetails(ApplicationInfoBean infoBean) throws Exception;
}
