package edu.ucsd.coeus.personalization.view;

import edu.mit.coeus.instprop.controller.IPCustomElementsController;
import edu.mit.coeus.instprop.controller.InstituteProposalBaseWindowController;
import edu.mit.coeus.instprop.gui.InstituteProposalListForm;
import edu.mit.coeus.utils.TypeConstants;
import edu.ucsd.coeus.personalization.coeusforms.Dataset;

/**
 * 
 * @author rdias - UCSD
 *
 */
public class InstituteProposalView extends ModuleView {
    private InstituteProposalBaseWindowController plWndCtrl;
    private InstituteProposalListForm instituteProposalListForm;


    /**
     * Row level security on search screen
     */
    public void cmdEnableOverWrite() {
    	instituteProposalListForm = (InstituteProposalListForm)super.getListControllerRef();
    	instituteProposalListForm.btnCorrectProposal.setEnabled(true);
    	instituteProposalListForm.mnuItmCorrectProposal.setEnabled(true);    	
    }
    
    public void cmdDisableOverWrite() {
    	instituteProposalListForm = (InstituteProposalListForm)super.getListControllerRef();    	
    	instituteProposalListForm.btnCorrectProposal.setEnabled(false);
    	instituteProposalListForm.mnuItmCorrectProposal.setEnabled(false);    	
    }

  	@Override
    public String getActiveKey() {
        InstituteProposalBaseWindowController pctrl = (InstituteProposalBaseWindowController)super.getBaseControllerRef();        
        if (pctrl != null) return pctrl.getProposalNumber();
        return "";
    }

    @Override
    public char getEditMode() {
        InstituteProposalBaseWindowController pctrl = (InstituteProposalBaseWindowController)super.getBaseControllerRef();
        if (pctrl != null) return pctrl.getFunctionType();
        return TypeConstants.DISPLAY_MODE;
    }


    public IPCustomElementsController getIPCustomElementsController() {
        InstituteProposalBaseWindowController pctrl = (InstituteProposalBaseWindowController)super.getBaseControllerRef();
        if (pctrl != null) return pctrl.getipCustomElementsController();
        return null;
    }
	
	/**
	 * Uses MIT QueryEngine to load the bean and populate  data for stylesheet
	 */		
    public String generateReport(String style, Dataset d) {
    	plWndCtrl = (InstituteProposalBaseWindowController)super.getBaseControllerRef();
    	String queryKey = plWndCtrl.getQueryKey();  
    	return super.getContextReport(style, d, queryKey);
    }

    

}
