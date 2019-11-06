package edu.ucsd.coeus.personalization.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.mit.coeus.utils.DateUtils;
import edu.ucsd.coeus.personalization.coeusforms.Value;
import edu.ucsd.coeus.personalization.command.ActionArgument;

public class ControllerHelper {
	
	private ControllerHelper() {}
	
	protected static DateUtils dateUtils = new DateUtils();
    public static final String OVERWRITE_LABELS = "cmdOverWriteLabelProp";
    public static final String OVERWRITE_COMP = "cmdOverWriteCompProp";    
    public static final String OVERWRITE_LABELS_INTABS = "cmdOverWriteLabelInTab";    
    public static final String OVERWRITE_LIST_COMP = "cmdOverWriteListComp";
    public static final String OVERWRITE_TABNAME = "cmdOverWriteTabName";
    public static final String DISABLE_TAB = "cmdDisableTab";
    public static final String VALIDATE_FIELDS = "cmdValidateFields";
    public static final String OVERWRITE_TOOLBAR = "cmdOverWriteToolProp";
    public static final String OVERWRITE_MENUBAR = "cmdOverWriteMenuProp";
    public static final String OVERWRITE_MAITAINMENUBAR = "cmdOverWriteMaintMenu";
    
    

	
 	/**
	 * Convert action argument to boolean
	 * If only boolean is in the argument
	 * @return
	 */
	public static boolean getBooleanValue(ActionArgument actArg) {
		boolean argvalue = false;
		if (actArg != null && !actArg.isNoarg()) {
			ArrayList arglist = actArg.getActionArg();
		    for (Iterator iter = arglist.iterator(); iter.hasNext();) {
				Object args = (Object) iter.next();
				if (args instanceof Boolean) {
					argvalue = ((Boolean)args).booleanValue();
				}
			}
		}
		return argvalue;
	}

}
