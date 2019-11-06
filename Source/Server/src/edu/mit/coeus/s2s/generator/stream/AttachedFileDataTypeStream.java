/*
 * AttachedFileDataTypeStream.java
 *
 * Created on August 19, 2005, 11:08 AM
 */

package edu.mit.coeus.s2s.generator.stream;

import edu.mit.coeus.s2s.Attachment;
import gov.grants.apply.system.attachments_v1.AttachedFileDataType;

/**
 *
 * @author  geot
 */
public class AttachedFileDataTypeStream {
    
    /** Creates a new instance of AttachedFileDataTypeStream */
    private AttachedFileDataTypeStream() {
    }
    
    /**
     *@deprecated since 4.2.1
     */
    public static String addExtension(String filename){
        /*
         *Fix #2838
         *Commented by Geo
         *This method is not required anymore because, extension is handled by another utility,
         * which supports all known mime types.
         *Not deleting this method is to support backward compatibility
         * Commenting this block and sending the file name as it is.
         */
        if (filename==null )
            return "";
        if(filename.indexOf('.')==-1){ 
                                //set to pdf if nothing has set
            filename+=".pdf";
        }
        return filename;
    }
}
