/*
 * ScreenFocusTraversalPolicy.java
 *
 * Created on September 12, 2003, 3:01 PM
 */

package edu.mit.coeus.utils;

import java.awt.*;

/**
 *
 * @author  ravikanth
 */



public class ScreenFocusTraversalPolicy extends DefaultFocusTraversalPolicy {
    Component components[];
    /** Creates a new instance of ScreenFocusTraversalPolicy */
    public ScreenFocusTraversalPolicy(Component[] screenComponents) {
        this.components = screenComponents;
    }
    
    public Component getComponentAfter(Container container, Component component) {
        int compIndx = -1;
        for(int count = 0; count < components.length; count++) {
            if(component.equals(components[count])) {
                compIndx = count;
                continue;
            }
            if( components[count].isEnabled() && compIndx != -1 && count > compIndx ) {
                return components[count];
            }
        }
        //no component is enabled after the given component, so scan thru the previous
        // components.
        for( int count = 0; count < compIndx ; count++ ){
            if( components[count].isEnabled() ){
                return components[count];
            }
        }
        //other than the given component, no other components are enabled 
        return component;
    }
    
    public Component getComponentBefore(Container container, Component component) {
        int compIndx = -1;
        for(int count = components.length-1; count >= 0; count--) {
            if(component.equals(components[count])) {
                compIndx = count;
                continue;
            }
            if( components[count].isEnabled() && compIndx != -1 && count < compIndx ) {
                return components[count];
            }
        }
        //no component is enabled before the given component, so scan thru the next
        // components.
        for( int count = components.length - 1; count > compIndx ; count-- ){
            if( components[count].isEnabled() ){
                return components[count];
            }
        }
        //other than the given component, no other components are enabled 
        return component;
    }
    
    public Component getDefaultComponent(Container container) {
        if(components[0].isEnabled()){
            return components[0];
        }else{
            return getComponentAfter(container, components[0]);
        }
        //return components[0];
    }
    
    public Component getFirstComponent(Container container) {
        if(components[0].isEnabled()){
            return components[0];
        }else{
            return getComponentAfter(container, components[0]);
        }
        
//        return components[0];
    }
    
    public Component getLastComponent(Container container) {
        if(components[components.length-1].isEnabled()){
            return components[components.length-1];
        }else{
            return getComponentBefore(container, components[components.length-1]);
        }
        
//        return components[components.length-1];
    }
    
}

