package edu.ucsd.coeus.personalization.view;

import edu.ucsd.coeus.personalization.coeusforms.Dataset;


/**
 * @author rdias
 * A report listener should be implemented by the view
 * since it is only needed when a report is invoked
 * data binding is heavy and hence it is delayed
  */
public interface ReportListener {
	
   /**
     * Invoked when the report window is opened
     */
    public void reportOpened(Dataset r, String style);
    
    /**
     * Invoked when the report window is closed
     * Data cleanup
     */
    public void reportClosed();

    
	

}
