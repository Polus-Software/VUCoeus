/*
 * Debug.java
 * 
 * Client side debugger
 * 
 * @version: 1.0
 * @author: Jill McAfee, Vanderbilt University Office of Research
 * @created: July 13, 2011
 */

package edu.vanderbilt.coeus.utils;

import edu.mit.coeus.utils.*;

public class Debug {
	private String message;

	public Debug() {
		//default constructor
	}
	
	public Debug(String msg) {
		this.message = msg;
	}
	
	public void showMessage() {
		CoeusOptionPane.showInfoDialog(message);
	}
	
	public void setMessage(String msg) {
		this.message = msg;
	}
	
	
}
