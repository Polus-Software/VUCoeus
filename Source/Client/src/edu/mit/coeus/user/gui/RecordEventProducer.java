/*
 * RecordEventProducer.java
 *
 * Created on July 15, 2003, 10:09 AM
 */

package edu.mit.coeus.user.gui;

/**
 *
 * @author  senthilar
 */
public interface RecordEventProducer {
    
    public void registerListener(RecordListener recordListener);
    
}
