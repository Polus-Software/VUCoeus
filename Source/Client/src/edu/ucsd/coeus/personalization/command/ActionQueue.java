package edu.ucsd.coeus.personalization.command;

import java.util.Vector;

public class ActionQueue {
	//An HashMap is not suitable here because a queue could have same action repeated
	private Vector actionList, argList;
	
	public ActionQueue() {
		this.actionList = new Vector();
		this.argList = new Vector();
	}
	
	public void queueAction(String actionName) {
		actionList.add(actionName);
		argList.add(new ActionArgument());
	}
	
	public void queueAction(String actionName, ActionArgument actionArg) {
		actionList.add(actionName);
		argList.add(actionArg);		
	}

	public Vector getActionList() {
		return actionList;
	}

	public Vector getArgList() {
		return argList;
	}
	
	public void resetQueue() {
		actionList.removeAllElements();
		argList.removeAllElements();
	}
	
	public boolean isEmpty() {
		return actionList.isEmpty();
	}

}
