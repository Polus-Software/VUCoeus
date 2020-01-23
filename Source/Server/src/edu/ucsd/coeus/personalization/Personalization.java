package edu.ucsd.coeus.personalization;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.xmlbeans.XmlException;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.ucsd.coeus.ServerUtils;
import edu.ucsd.coeus.personalization.PersonalizationDataDocument.PersonalizationData;
import edu.ucsd.coeus.personalization.authforms.AuthFormsDocument;
import edu.ucsd.coeus.personalization.coeusforms.CoeusFormsDocument;
import edu.ucsd.coeus.personalization.formaccess.CoeusFormsSecureDocument;

/**
 * 
 * @author rdias ACT-UCSD
 *
 */
public class Personalization implements PersonalizationI {
	
    final static String coeusFormsXML = "/coeus_forms.xml";
    final static String coeusAuthXML = "/coeus_forms_policy.xml";
    private static CoeusFormsDocument fvDoc = null;
    private static CoeusFormsSecureDocument avDoc = null;

    private final static Personalization uniqueInstance = new Personalization();

    private Personalization() {
    }

    public static Personalization getInstance() {
        return uniqueInstance;
    }

    private void loadcoeusFormXML() {
        InputStream stream = null;
        URL xmlurl = getClass().getResource(coeusFormsXML);
        try {
            stream = xmlurl.openStream();
        } catch (IOException io) {
            System.err.println("Failed to open xml stream");
            UtilFactory.log("Failed to open coeus_fields.xml stream", io,
                    "LocalizationXML", "loadXML");
            return;
        } catch (Exception e) { //Ok to ignore probably the file does not exists
            //UtilFactory.log("Failed to open coeus_fields.xml file");
        	if (stream != null) {
        		try {
					stream.close();
				} catch (IOException e1) {
				}
        	}
            return;
        }
        if (stream != null) {
            try { // Bind the incoming XML to an XMLBeans type.
                fvDoc = CoeusFormsDocument.Factory.parse(stream);
            } catch (XmlException e) {
                UtilFactory.log("Failed to open coeus_forms.xml", e,
                        "LocalizationXML", "coeusFormsXML");
            } catch (IOException e) {
                UtilFactory.log("Failed to open coeus_forms.xml", e,
                        "LocalizationXML", "coeusFormsXML");
            }
    		try {
				stream.close();
			} catch (IOException e1) {
			}
        } else {
            UtilFactory.log("Failed to open coeus_forms.xml", null,
                    "LocalizationXML", "coeusFormsXML");
        }
    }
    
    private void loadcoeusAuthXML() {
        InputStream stream = null;
        URL xmlurl = getClass().getResource(coeusAuthXML);
        try {
            stream = xmlurl.openStream();
        } catch (IOException io) {
            System.err.println("Failed to open xml stream");
            UtilFactory.log("Failed to open form_secure_policy.xml stream", io,
                    "LocalizationXML", "loadXML");
            return;
        } catch (Exception e) { //Ok to ignore probably the file does not exists
            //UtilFactory.log("Failed to open coeus_fields.xml file");
        	if (stream != null) {
        		try {
					stream.close();
				} catch (IOException e1) {
				}
        	}
            return;
        }
        if (stream != null) {
            try { // Bind the incoming XML to an XMLBeans type.
            	avDoc = CoeusFormsSecureDocument.Factory.parse(stream);
				if (!avDoc.validate()) {
	                UtilFactory.log("ERROR: form_secure_policy.xml is not valid.");					
				}            	
            } catch (XmlException e) {
                UtilFactory.log("Failed to open form_secure_policy.xml", e,
                        "LocalizationXML", "coeusFormsXML");
            } catch (IOException e) {
                UtilFactory.log("Failed to open coeus_forms.xml", e,
                        "LocalizationXML", "coeusFormsXML");
            }
    		try {
				stream.close();
			} catch (IOException e1) {
			}
        } else {
            UtilFactory.log("Failed to open form_secure_policy.xml", null,
                    "LocalizationXML", "coeusFormsXML");
        }
    }
       
    /**
     * 
     * @param responderBean
     * @param userinfobean
     * @param functionType
     */
    public void setLocalizationXML(ResponderBean responderBean, RequesterBean requesterBean) {    	
        if (fvDoc == null || !ServerUtils.getCacheXMLFlag())
        	loadcoeusFormXML();
        PersonalizationDataDocument secdoc = PersonalizationDataDocument.Factory.newInstance();
        PersonalizationData perdata = secdoc.addNewPersonalizationData();
        if (fvDoc != null && !fvDoc.isNil()) {
        	XmlData xsecdata = perdata.addNewXmlData();
        	xsecdata.set(fvDoc);
        }
        if (secdoc != null && !secdoc.isNil()) {
        	responderBean.setPersnXMLObject(secdoc);        	
        }
    }
    
    protected void setAccessXML(ResponderBean responderBean, 
    		RequesterBean requesterBean, BaseFormSecurePolicy fsp) {    	
        AuthFormsDocument authform = fsp.getAccessXml();        
        PersonalizationDataDocument secdoc = PersonalizationDataDocument.Factory.newInstance();
        PersonalizationData perdata = secdoc.addNewPersonalizationData();
        if (authform != null && !authform.isNil()) {
        	XmlData xsecdata = perdata.addNewXmlData();
        	xsecdata.set(authform);
        }                        
        if (secdoc != null && !secdoc.isNil()) {
        	responderBean.setPersnXMLObject(secdoc);        	
        }
    } 
    
    public void setAwardAccessXML(ResponderBean responderBean, 
    		RequesterBean requesterBean) {  
        if (avDoc == null || !ServerUtils.getCacheXMLFlag())
        	loadcoeusAuthXML();            	
        AwardFormSecurePolicy fsp = new AwardFormSecurePolicy(avDoc,requesterBean,responderBean);
        setAccessXML(responderBean,requesterBean,fsp);
    }
    
    public void setProposalAccessXML(ResponderBean responderBean, 
    		RequesterBean requesterBean) {  
        if (avDoc == null || !ServerUtils.getCacheXMLFlag())
        	loadcoeusAuthXML();            	
        ProposalFormSecurePolicy fsp = new ProposalFormSecurePolicy(avDoc,requesterBean,responderBean);
        setAccessXML(responderBean,requesterBean,fsp);
    }
    
    
    
	

}
