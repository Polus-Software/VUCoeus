/*
 * BaseWindowObservable.java
 *
 * Created on September 11, 2003, 9:03 PM
 */

package edu.mit.coeus.utils;

/**
 * This class is used to notify the base windows to update the corresponding
 * row after data has been successfully saved to the database from detailform. 
 * @author  ravikanth
 */
public class BaseWindowObservable extends java.util.Observable {
    private char functionType;
    
    /** Creates a new instance of BaseWindowObservable */
    public BaseWindowObservable() {
    }
    
    public void notifyObservers() {
        //explicitly specify that the data has been changed
        setChanged();
        super.notifyObservers();
    }
    
    public void notifyObservers(Object obj) {
        //explicitly specify that the data has been changed
        setChanged();
        super.notifyObservers( obj );
    }
    
    /** Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
}
