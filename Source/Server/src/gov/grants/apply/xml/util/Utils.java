/*
 * Created on Jul 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.grants.apply.xml.util;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author Brian Husted
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Utils {
	
	
	
	public static boolean isWellFormedXml( String xml )  {
		 ByteArrayInputStream stream = null;
		 boolean wellFormed = true;
			try {
				stream = new ByteArrayInputStream(xml.getBytes());
			
			DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
			dfactory.setNamespaceAware(true);
			
			dfactory.newDocumentBuilder().parse(stream);
			}catch( Throwable t ){ wellFormed = false; }
			
		return wellFormed;
		
	}

}
