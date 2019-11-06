package edu.ucsd.coeus.personalization.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.ucsd.coeus.personalization.CObservable;
import edu.ucsd.coeus.personalization.CObserver;
import edu.ucsd.coeus.personalization.ClientUtils;
import edu.ucsd.coeus.personalization.PersonalizationDataDocument;
import edu.ucsd.coeus.personalization.XmlData;
import edu.ucsd.coeus.personalization.PersonalizationDataDocument.PersonalizationData;
import edu.ucsd.coeus.personalization.authforms.Action;
import edu.ucsd.coeus.personalization.authforms.Aform;
import edu.ucsd.coeus.personalization.authforms.Argument;
import edu.ucsd.coeus.personalization.authforms.AuthFormsDocument;
import edu.ucsd.coeus.personalization.authforms.AuthFormsDocument.AuthForms;
import edu.ucsd.coeus.personalization.authforms.Value;
import edu.ucsd.coeus.personalization.coeusforms.Boolean;
import edu.ucsd.coeus.personalization.coeusforms.Cfield;
import edu.ucsd.coeus.personalization.coeusforms.Cform;
import edu.ucsd.coeus.personalization.coeusforms.CoeusFormsDocument;
import edu.ucsd.coeus.personalization.coeusforms.CoeusFormsDocument.CoeusForms;
import edu.ucsd.coeus.personalization.command.ActionArgument;
import edu.ucsd.coeus.personalization.command.ActionQueue;

/**
 * 
 * @author rdias - UCSD
 *
 */
//This is our model where all the xml data is stored at runtime
public class FormAttrModel implements CObservable {
    
	final private static FormAttrModel uniqueInstance = new FormAttrModel();
	final private static FormDataObject persndataobj = new FormDataObject();
	private ArrayList observers;
	private String targetController = "";
	private ActionQueue targetActions;
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/PersonalizationServlet";
    private AuthFormsDocument authform = null;

    private FormAttrModel() {
    	observers = new ArrayList();
	}
	
    public static FormAttrModel getInstance() {
		return uniqueInstance;
	}	    

	protected static FormDataObject getSecdataobj() {
		return persndataobj;
	}

	public void update(Observable obs, Object obj) {
		System.out.println("Received update: Object type: " + obj.toString());
	}
	
	public void registerObserver(CObserver o) {
		observers.add(o);
	}
	
	public void removeObserver(CObserver o) {
		int i = observers.indexOf(o);
		if (i >= 0) {
			observers.remove(i);
		}
	}
	
	public void notifyObservers() {
		for (int i = 0; i < observers.size(); i++) {
			CObserver observer = (CObserver)observers.get(i);
			observer.update(targetController, targetActions);
		}
	}
	
	public ActionArgument getComponentBeans(String formID, String[] types) {
		String tabname = (String)persndataobj.getHeaderData().get((String)formID);
		ActionArgument actionarg;
		if (tabname != null)
			actionarg = new ActionArgument(formID,tabname);
		else 
			actionarg = new ActionArgument(formID);
		HashMap fieldcache = (HashMap)persndataobj.getCoeusforms().get((String)formID);
		if (fieldcache == null)
			return null;		
		for (Iterator it=fieldcache.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry entry = (Map.Entry)it.next();
			Object value = entry.getValue();
			Cfield field = (Cfield) value;
			for (int i = 0; i < types.length; i++) {
				if (field.getType().equals(types[i])) {
					actionarg.addActionArg(field);
				}				
			}
		}		
		return actionarg;
	}
	
	/**
	 * Temp work around until external namespace can be used in authforms
	 * @param sfield
	 * @return
	 */
	private Cfield FieldCopy(edu.ucsd.coeus.personalization.authforms.Cfield sfield) {
		Cfield tfield = Cfield.Factory.newInstance();	
		if (sfield.isSetDefault()) tfield.setDefault(sfield.getDefault());
		if (sfield.isSetDisabled())
			tfield.setDisabled(Boolean.Enum.forInt(sfield.getDisabled().intValue()));
		if (sfield.isSetName())
			tfield.setName(sfield.getName());
		if (sfield.isSetTooltip())
			tfield.setTooltip(sfield.getTooltip());
		if (sfield.isSetUrl()) {
			tfield.setUrl(sfield.getUrl());
		}
		tfield.setVarname(sfield.getVarname());
		tfield.setType(sfield.getType());
		if (sfield.isSetAttr()) {
			edu.ucsd.coeus.personalization.authforms.Attr srcattr = sfield.getAttr();
			edu.ucsd.coeus.personalization.coeusforms.Attr tarattr = tfield.addNewAttr();
			if (srcattr.isSetBgcolor()) {
				tarattr.setBgcolor(srcattr.getBgcolor());
			}
			if (srcattr.isSetFgcolor()) {
				tarattr.setFgcolor(srcattr.getFgcolor());
			}	
		}
		return tfield;
	}
	
	/**
	 * 
	 * @param formID
	 * @return
	 */
	public ActionArgument getAuthComponentBeans(String formID, ActionArgument actionarg) {
		if (authform == null) {
			return null;
		}
		AuthForms aforms = authform.getAuthForms();
		Aform aform[] = aforms.getAformArray();
		for (int i = 0; i < aform.length; i++) {
			if (aform[i].getClassname() != null && 
					aform[i].getClassname().equals(formID)) {
				edu.ucsd.coeus.personalization.authforms.Cfield fields[] = aform[i].getCfieldArray();
				for (int j = 0; j < fields.length; j++) {
					actionarg.addActionArg(FieldCopy(fields[j]));
				}				
			}
		}
		return actionarg;
	}
	
	public boolean isTabEditable(String formID,String uniqueID) {
		boolean tabeditable = true;
		if (authform == null) {
			return true;
		}
		AuthForms aforms = authform.getAuthForms();
		Aform aform[] = aforms.getAformArray();
		for (int i = 0; i < aform.length; i++) {
			if (!uniqueID.equals(aform[i].getUniqueID())) continue;			
			edu.ucsd.coeus.personalization.authforms.Action actions[] = aform[i].getActionArray();
			for (int j = 0; j < actions.length; j++) {
				if (!actions[j].getActionname().equals("disableTab")) continue;
				Argument[] args = actions[j].getArgumentArray();
				if (args.length > 0) {
					Value[] values = args[0].getValueArray();
					if (values.length > 0 && values[0].getKeyindx().equals("tabclass") &&
						values[0].getStringValue().equals(formID)) {
						tabeditable = false;
					}
				}
			}				
		}
		return tabeditable;
	}
	
	
	
	public ActionArgument getEditableBeans(String formID) {
		String [] types = {"edu.mit.coeus.utils.CoeusTextField","edu.mit.coeus.utils.DollarCurrencyTextField",
				"edu.mit.coeus.utils.CoeusComboBox"};
		return getComponentBeans(formID, types);
	}

	public ActionArgument getMenuToolsBeans(String formID) {
		String [] types = {"edu.mit.coeus.gui.menu.CoeusMenuItem","edu.mit.coeus.gui.menu.CoeusMenu",
		"edu.mit.coeus.gui.toolbar.CoeusToolBarButton"};
		return getComponentBeans(formID, types);
	}
	
	public ActionArgument getMenuBeans(String formID) {
		String [] types = {"edu.mit.coeus.gui.menu.CoeusMenuItem","edu.mit.coeus.gui.menu.CoeusMenu",
		"edu.mit.coeus.gui.toolbar.CoeusToolBarButton"};
		return getComponentBeans(formID, types);
	}	
	
	public ActionArgument getToolBeans(String formID) {
		String [] types = {"edu.mit.coeus.gui.toolbar.CoeusToolBarButton"};
		return getComponentBeans(formID, types);
	}	
		
	public boolean isModuleEditable(String modname, String uniqueID) {
		boolean modedit = true;
		if (authform == null) return modedit;
    	try { 
    		AuthForms aforms = authform.getAuthForms();
    		Aform allforms[] = aforms.getAformArray();
    		for (int i=0; i < allforms.length; i++) {
    			if (modname.equals(allforms[i].getModule()) &&
    					uniqueID.equals(allforms[i].getUniqueID())) {
    				Action actions[] = allforms[i].getActionArray();
    				for (int j = 0; j < actions.length; j++) {
						if (actions[j].getActionname().equals("disableEdit")) {
							modedit = false;
						}
						else if (actions[j].getActionname().equals("enableEdit")) {
							modedit = true;
						}						
					}
    			}
    		}
    	} catch (Exception e) {
    		CoeusOptionPane.showErrorDialog("Exception while binding authXML to XMLBean type");
    		ClientUtils.logger.error("Exception while binding authXML to XMLBean type", e);                
    	}
		return modedit;
	}
	
	
	/**
	 * 
	 * @param fieldArray
	 * @return
	 * This collection holds
	 * Key => varname
	 * Data => Field Object
	 */
    private HashMap getFieldCache(Cfield[] fieldArray) {
        HashMap fieldCache = new HashMap(fieldArray.length);
        for (int j=0; j < fieldArray.length; j++) {
            fieldCache.put(fieldArray[j].getVarname(), fieldArray[j]); //Cache the field object with field name as the key
        }
        return fieldCache;
    }
	
    /**
     * Bind the incoming XML to xmlbeans and then cache it
     * @param coeusXML The coeusXML to set.
     * Does not deal with container at the moment.
     * This could be done latter
     */
    public void setCoeusXML(CoeusFormsDocument fvDoc) {
    	try { // Bind the incoming XML to an XMLBeans type.
    		CoeusForms fv =  fvDoc.getCoeusForms();
    		Cform[] formArray =  fv.getCformArray();
    		for (int i=0; i < formArray.length; i++) {
    			//Add non container fields
    			Cfield[] fieldArray = formArray[i].getCfieldArray();
    			persndataobj.getCoeusforms().put(formArray[i].getClassname(), getFieldCache(fieldArray));
    			if (ClientUtils.isNotBlank(formArray[i].getTabname())) {
    				persndataobj.getHeaderData().put(formArray[i].getClassname(), formArray[i].getTabname());
    			}
    		}
    	} catch (Exception e) {
    		CoeusOptionPane.showErrorDialog("Exception while binding coeusXML to XMLBean type");
    		ClientUtils.logger.error("Exception while binding coeusXML to XMLBean type", e);                
    	}
    }
    
    public void setAuthXML(AuthFormsDocument authDoc) {
    	authform = null;
    	if (authDoc == null || authDoc.isNil() ) {
    		ClientUtils.logger.debug("No Auth XML");
    		return;
    	}
    	authform = authDoc;
    }    	    
    
    private void showErrors(ArrayList errorList) {
		for (int x = 0; x < errorList.size(); x++) {
			XmlError error = (XmlError) errorList.get(x);
			ClientUtils.logger.error("\n");
			ClientUtils.logger.error("Message: " + error.getMessage() + "\n");
			ClientUtils.logger.error("Location of invalid XML: "
					+ error.getCursorLocation().xmlText() + "\n");
		}
	}	
    
    public void refreshPersonalizationXML() {
        RequesterBean requester = new RequesterBean();
        requester.setId("PERSONALIZE_XML");
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        try {
			if(response != null && response.hasResponse() ){
				setPersonalizationXML(response.getPersnXMLObject());
			} else {
				ClientUtils.logger.error("Failed to get personalized xml from server");
			}
		} catch (CoeusException e) {
			ClientUtils.logger.error(e);
		}
    }
    
  //for debugging purpose
    private void showXML(AuthFormsDocument authDoc) {
//		if (authDoc != null) {
//			XmlOptions printOptions = new XmlOptions();
//			printOptions.setSavePrettyPrint();
//			System.out.println("DEBUG:\n" + authDoc.xmlText(printOptions));
//		}    	
    }
    
    public void setAccessXml(Object _personalizationXML) {
		if (_personalizationXML == null
				|| !(_personalizationXML instanceof PersonalizationDataDocument)) {
			return;
		}
		try {
			PersonalizationDataDocument persndoc = (PersonalizationDataDocument) _personalizationXML;
			PersonalizationData persndata = persndoc.getPersonalizationData();
			XmlData[] xmldataarry = persndata.getXmlDataArray();
			for (int i = 0; i < xmldataarry.length; i++) {
				XmlObject xobj = XmlObject.Factory.parse(xmldataarry[i].toString());
				if (xobj instanceof AuthFormsDocument) {
					if (xobj.validate()) { // Valid AuthFormsDocument
						setAuthXML((AuthFormsDocument) xobj);
					} else {
						ClientUtils.logger.error("Invalid XML file object. form access xml file");
					}
					showXML((AuthFormsDocument) xobj);
				}
			}
		} catch (XmlException e) {
			CoeusOptionPane.showErrorDialog("Error binding PersonalizationXML to XMLBean type");
			ClientUtils.logger.error("Error binding PersonalizationXML to XMLBean type", e);
		} catch (Exception e) {CoeusOptionPane.showErrorDialog("Exception while binding PersonalizationXML to XMLBean type");
			ClientUtils.logger.error("Exception while binding PersonalizationXML to XMLBean type",e);
		}
	}
	
    public  void setPersonalizationXML(Object _personalizationXML) {
    	XmlOptions validateOptions = new XmlOptions();
    	ArrayList errorList = new ArrayList();
    	validateOptions.setErrorListener(errorList);
    	if (_personalizationXML == null || !(_personalizationXML instanceof PersonalizationDataDocument)) {
    		return;
    	}
    	try {
    		PersonalizationDataDocument persndoc = (PersonalizationDataDocument)_personalizationXML;
    		PersonalizationData persndata = persndoc.getPersonalizationData();
    		XmlData[] xmldataarry = persndata.getXmlDataArray();
    		for (int i=0; i < xmldataarry.length; i++) {
    			XmlObject xobj = XmlObject.Factory.parse(xmldataarry[i].toString());
    			if (xobj instanceof CoeusFormsDocument) {
    				if (xobj.validate()) {  //Valid CoeusFormsDocument
    					setCoeusXML((CoeusFormsDocument)xobj); 
    				} else {
    					CoeusOptionPane.showErrorDialog("Coeus personalization xml file is invalid");
    					ClientUtils.logger.error("Invalid XML file object. Required field xml file");
    					showErrors(errorList);
    				}
    			}
    		}
    	} catch (XmlException e) {
    		CoeusOptionPane.showErrorDialog("Error binding PersonalizationXML to XMLBean type");
    		ClientUtils.logger.error("Error binding PersonalizationXML to XMLBean type", e);
    	} catch (Exception e) {
    		CoeusOptionPane.showErrorDialog("Exception while binding PersonalizationXML to XMLBean type");
    		ClientUtils.logger.error("Exception while binding PersonalizationXML to XMLBean type", e);                
    	}
    }
	

}

