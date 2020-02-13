package edu.ucsd.coeus.personalization.view;

import edu.mit.coeus.propdev.gui.ProposalDetailForm;
import edu.ucsd.coeus.personalization.coeusforms.Dataset;

/**
 * 
 * @author rdias - UCSD
 *
 */
public class DevProposalView extends ModuleView {
    private ProposalDetailForm plWndCtrl;



    @Override
    public String getActiveKey() {
    	plWndCtrl = (ProposalDetailForm)super.getBaseControllerRef();
    	if (plWndCtrl != null)
    		return(plWndCtrl.proposalID);    	
        return "";
    }

	
	/**
	 * Uses MIT QueryEngine to load the bean and populate  data for stylesheet
	 */		
    public String generateReport(String style, Dataset d) {
    	plWndCtrl = (ProposalDetailForm)super.getBaseControllerRef();
    	String queryKey = plWndCtrl.proposalID;
    	return super.getContextReport(style, d, queryKey);
    }

    

}
