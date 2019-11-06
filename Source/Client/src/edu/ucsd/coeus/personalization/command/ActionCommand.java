package edu.ucsd.coeus.personalization.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

import edu.ucsd.coeus.personalization.CObservable;
import edu.ucsd.coeus.personalization.CObserver;
import edu.ucsd.coeus.personalization.ClientUtils;
import edu.ucsd.coeus.personalization.view.AbstractView;


public class ActionCommand implements CommandI, CObservable {
	Vector actionNamelist, actionArgumentlist;
	private ArrayList observers;
    AbstractView moduleView;
    private String currentAction;
    private ActionArgument actionArg;
    

    public ActionCommand () {
		    this(null,null,null);
    }
	 
    public ActionCommand ( ActionQueue actionQueue,Object ref, String modulename){
    	this.actionNamelist = actionQueue.getActionList();
    	this.actionArgumentlist = actionQueue.getArgList();
    	moduleView = AbstractView.getViewRef(modulename);
    	if (moduleView != null)
    		moduleView.addFormReferences(ref);
    	this.observers = new ArrayList();
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
			observer.update(currentAction, actionArg);
		}
	}
    

	public void execute() {
		if (moduleView == null) {
			return;
		}
		String actionname;
		ActionArgument actionargument;
		for (int i = 0; i < actionNamelist.size(); i++) {
			actionname = (String) (actionNamelist.get(i));
			actionargument = (ActionArgument) ((actionArgumentlist.get(i)));
			moduleView.setActionArgument(actionargument);
			try {
				Class pclass = moduleView.getClass();
				this.currentAction = actionname;
				//System.out.println("Action is: " + actionname + "=" + pclass.getName());
				Method method = pclass.getMethod(actionname, new Class[] {});
				Object result = method.invoke(moduleView, new Object[] {});
				if (result != null) {
					//Add return param to actionarg
					this.actionArg = new ActionArgument();
					this.actionArg.addActionArg(result);
				}
				System.out.println("Action complete: " + actionname);
				notifyObservers();
			} catch (Throwable e) {
				System.err.println(e);
				e.printStackTrace();
			}
		} 
	}
	
	

}
