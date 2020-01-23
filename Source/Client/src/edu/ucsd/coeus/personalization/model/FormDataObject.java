package edu.ucsd.coeus.personalization.model;

import java.util.HashMap;

/**
 * 
 * @author rdias
 *
 */
public class FormDataObject {
	
	final private HashMap headerData = new HashMap();  //Header data object (Optional)
	
	//	Local cache with form class name as a key
    final private  HashMap coeusforms = new HashMap();
    
	
	/**
	 * 
	 * @return
	 * This collection holds
	 * Key => Class Name (Unique within application)
	 * Data => HashMap of Fields(key,fieldobj)
	 */
	public HashMap getCoeusforms() {
		return coeusforms;
	}


	public HashMap getHeaderData() {
		return headerData;
	}
	
	
    
	

}
