/*
 * S2SErrorHandler.java
 *
 * Created on January 27, 2005, 11:13 AM
 */

package edu.mit.coeus.s2s.validator;

import edu.mit.coeus.s2s.bean.FormInfoBean;

/**
 *
 * @author  geot
 */
public interface S2SErrorHandler {
    
    public void schemaMessage(String message, int severity);
    public void opportunityMessage(S2SValidationException s2sValidationEx);
    public void mappingMessage(FormInfoBean formInfo,String message);
    public void exception(S2SValidationException s2sValidationEx);
}
