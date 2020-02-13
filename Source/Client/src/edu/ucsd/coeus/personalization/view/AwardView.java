package edu.ucsd.coeus.personalization.view;

import edu.mit.coeus.award.controller.AwardBaseWindowController;
import edu.ucsd.coeus.personalization.coeusforms.Dataset;

public class AwardView extends ModuleView  {
	private AwardBaseWindowController awWndCtrl;
    

	/**
	 * Uses MIT QueryEngine to load the bean and populate  data for stylesheet
	 */		
    public String generateReport(String style, Dataset d) {
    	awWndCtrl = (AwardBaseWindowController)super.getBaseControllerRef();
    	String queryKey = awWndCtrl.getQueryKey();  
    	return super.getContextReport(style, d, queryKey);
    }    


    @Override
    public String getActiveKey() {
        if (awWndCtrl != null) return awWndCtrl.getQueryKey();
        return "";
    }

	
    
    

}
