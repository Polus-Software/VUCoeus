/*
 * NodeParser.java
 *
 * Created on October 4, 2006, 4:01 PM
 */

package edu.mit.coeus.utils.scheduler;

/**
 *
 * @author  geot
 */
public interface NodeReader {
    public TaskBean[] read(org.w3c.dom.Element element);
    public void setLogFileName(String logFile);
}
