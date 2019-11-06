package edu.ucsd.coeus.personalization;

import edu.ucsd.coeus.personalization.command.ActionArgument;
import edu.ucsd.coeus.personalization.command.ActionQueue;

public interface CObserver {
	public void update(String actionName, ActionArgument actarg);
	public void update(String controllerName, ActionQueue actarg);

}