/**
 * 
 */
package edu.mit.coeus.s2s.generator;

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.bean.UserAttachedS2SFormAttachmentBean;
import edu.mit.coeus.s2s.formattachment.FormAttachmentExtractService;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.dbengine.DBException;

/**
 * @author geo
 *
 */
public class UserAttachedFormStreamGenerator extends S2SBaseStream {

	/* (non-Javadoc)
	 * @see edu.mit.coeus.s2s.generator.S2SBaseStream#getStream(java.util.HashMap)
	 */
	@Override
	public Object getStream(HashMap ht) throws JAXBException, CoeusException,
			DBException {
		String proposalNumber = (String)ht.get("PROPOSAL_NUMBER");
		String namespace = (String)ht.get("NAMESPACE");
		String formname = (String)ht.get("FORM_NAME");
    	FormAttachmentExtractService extractService = new FormAttachmentExtractService();
    	Object jaxbObject;
		try {
			jaxbObject = extractService.getJaxbObjectForNamespace(proposalNumber, namespace, formname);
		} catch (S2SValidationException e) {
			throw new CoeusException(e.getMessage());
		}
    	addAttachments(proposalNumber,namespace);
    	return jaxbObject;
	}

	private void addAttachments(String proposalNumber, String namespace) throws DBException, CoeusException {
		FormAttachmentExtractService extractService = new FormAttachmentExtractService();
		List<UserAttachedS2SFormAttachmentBean> userAttachedFormAtatchments = extractService.getAttachments(proposalNumber,namespace);
		for (UserAttachedS2SFormAttachmentBean userAttachedS2SFormAttachmentBean : userAttachedFormAtatchments) {
            Attachment att = new Attachment();
            att.setContent(userAttachedS2SFormAttachmentBean.getAttachment());
            att.setContentId(userAttachedS2SFormAttachmentBean.getContentId());
            att.setContentType(userAttachedS2SFormAttachmentBean.getContentType());
            att.setFileName(userAttachedS2SFormAttachmentBean.getFilename());
            addAttachment(userAttachedS2SFormAttachmentBean.getContentId(), att);
		}
	}
	
}
