package edu.mit.coeus.brokers;

import javax.swing.*;
import java.util.Vector;
import java.awt.*;

/**
 * This class is used to represent the Component Bean which will hold the 
 * JInternal Frame instance.
 * 
 * @version 1.0
 */
public class ComponentBean implements  java.io.Serializable {

    JInternalFrame comp;

    /**
     * Constructs a Component Bean.
     */
    public ComponentBean(){
    }
    
    /**
     * This method set the Component.
     * @param comp JInternalFrame instance
     */
    public void setComponent(JInternalFrame comp){
        this.comp = comp;
    }
    
    /**
     * This method get the Component.
     * @param JInternalFrame component of this bean.
     */
    public JInternalFrame getComponent(){
        return comp;
    }
}