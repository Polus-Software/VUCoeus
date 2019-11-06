package edu.ucsd.coeus.personalization.command;

import java.util.ArrayList;

/**
 * 
 * @author rdias
 *
 */
//All arguments related to specific action
//With data appropriate for that component or Action
//Arguments could be data beans, hash list, map, list etc
public class ActionArgument {
    private ArrayList argsdata = new ArrayList(); //ArrayList of collection objects
    boolean noarg = true;                         //Defaults to noargs
    private String ID = "";
    private Object headerData;                    //Optional. Example tab name
    
    public ActionArgument() {
    	super();
    }
    
	public ActionArgument(String id) {
		super();
		ID = id;
	}
	
	public ActionArgument(String id, Object header) {
		super();
		ID = id;
		headerData = header;
	}
	

	public boolean isNoarg() {
		return noarg;
	}

	public String getID() {
		return ID;
	}

	public Object getHeaderData() {
		return headerData;
	}

	public void addActionArg(Object o) {
		argsdata.add(o);
		noarg = false;
    }
    
    public ArrayList getActionArg() {
    	return this.argsdata;
    }

}
