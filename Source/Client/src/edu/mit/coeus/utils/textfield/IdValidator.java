/*
 * IdValidator.java
 *
 * Created on March 7, 2003, 12:27 PM
 */

package edu.mit.coeus.utils.textfield;

/**
 * This interface has a method which has to be implemented by all the custom 
 * detail text fields for validing the entered text with the values in the database.
 *  
 * @author  ravikanth
 */
public interface IdValidator {
  
    /** Method needs to be implemented by custom detail text fields.
     * @param recordId tring representing the id, whose validity should be checked
     * @return boolean true if the given id is valid else false.
     */
    public boolean validateId();
    
}
