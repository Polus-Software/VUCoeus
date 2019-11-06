/*
 * EmailValidator.java
 * 
 * Format validation for email addresses
 * 
 * @version: 1.0
 * @author: Jill McAfee, Vanderbilt University Office of Research
 * @created: July 11, 2011
 */

package edu.vanderbilt.coeus.validate;

import java.util.regex.Matcher;  
import java.util.regex.Pattern;  

public class EmailValidator {
	
	private String emailAddress;
	private static final String EMAIL_PATTERN = "^[A-Za-z0-9\\._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
	
	public EmailValidator(String emailAddress) {
		this.emailAddress = emailAddress;
	}
      
	private boolean isValidEmailAddress(){  
		Pattern pattern = Pattern.compile(EMAIL_PATTERN,Pattern.CASE_INSENSITIVE);  
		Matcher matcher = pattern.matcher(emailAddress);  
		return matcher.matches();  
	}
	
	public boolean validateEmail() {  
		EmailValidator vea = new EmailValidator(emailAddress);  
		return vea.isValidEmailAddress();
	} 
	
	public String errorMessage() {
		String msg = new String(emailAddress + " is not a valid email address. Please enter a valid email address");
		return msg;
	}
}  
