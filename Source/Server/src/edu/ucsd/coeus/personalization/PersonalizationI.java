package edu.ucsd.coeus.personalization;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;

public interface PersonalizationI {
	
	public void setLocalizationXML(ResponderBean responderBean,RequesterBean requesterBean);	
	public void setAwardAccessXML(ResponderBean responderBean,RequesterBean requesterBean);
	public void setProposalAccessXML(ResponderBean responderBean,RequesterBean requesterBean);

}
