/*
 * LockObservable.java
 *
 * Created on September 11, 2003, 9:03 PM
 */

package edu.mit.coeus.utils;

/**
 * This class is used to notify the observers in display mode that this particular record has been 
 * locked by the logged in user and not to check further in the server before modifying
 * other details of the same record .
 * @author  ravikanth
 */
public class LockObservable extends java.util.Observable {
    private int lockStatus;
    /** Creates a new instance of BaseWindowObservable */
    public LockObservable() {
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
    
    /** Getter for property lockStatus.
     * @return Value of property lockStatus.
     *
     */
    public int getLockStatus() {
        return lockStatus;
    }
    
    /** Setter for property lockStatus.
     * @param lockStatus New value of property lockStatus.
     *
     */
    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
    }
    
}
