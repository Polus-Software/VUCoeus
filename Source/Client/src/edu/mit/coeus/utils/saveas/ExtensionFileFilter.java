/*
 * ExtensionFileFilter.java
 *
 * Created on May 19, 2003, 11:38 AM
 */

package edu.mit.coeus.utils.saveas;

import java.io.File;
import java.util.Hashtable;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author  senthilar
 */
public class ExtensionFileFilter extends FileFilter{
    
    private String description;
    
    private Hashtable filters = null;
    
    /** Creates a new instance of ExtensionFileFilter */
    public ExtensionFileFilter() {
        filters = new Hashtable();
        setDescription("undefined");
    }
    
    public void addExtension(String extension) {
        filters.put(extension.toLowerCase(),this);
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean accept(File f) {
        if ( f != null ) {
            if ( f.isDirectory() ) {
                return true;
            }
            String extension = getExtension( f );
            if ( extension != null && filters.get( extension ) != null ) {
                return true;
            }
        }
        return false;
    }
    
    public String getExtension(File f) {
        if ( f != null ) {
             String filename = f.getName();
             int i = filename.lastIndexOf(".");
             if ( i>0 && i<filename.length()-1 ) {
                 return filename.substring( i+1 ).toLowerCase();
             }
        }
        return null;
    }
}