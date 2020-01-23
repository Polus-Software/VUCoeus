package edu.ucsd.coeus.personalization.command;

//Invoker
//Actions can be registered here and executed later at any time
public class ActionControl {
	  CommandI command;

	  public void setCommand(CommandI c) {
	    command = c;
	  }

	  public void processActions() {
	    command.execute();
	  }	

}
