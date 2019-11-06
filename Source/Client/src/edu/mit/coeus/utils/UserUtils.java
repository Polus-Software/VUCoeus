/*
 * UserUtils.java
 *
 * Created on December 28, 2006, 6:09 PM
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusMessageResources;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 *
 * @author  noorula
 */
public class UserUtils {
    
    private static final String USER_SERVLET = "/userMaintenanceServlet";
    private static final String EMPTY_STRING = "";
    private static HashMap objMap = new HashMap();

    /** Creates a new instance of UserUtils */
    public UserUtils() {
    }
    
    /**
     * To get username for the input userId
     * @param userId
     * @throws CoeusException
     * @return string (userName)
     */    
    public static String getDisplayName(String userId){
        String userName = EMPTY_STRING;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('A');
        requester.setDataObject(userId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+USER_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            CoeusOptionPane.showErrorDialog(response.getMessage());
            //throw new CoeusException(response.getMessage());
        }else{
            userName =(String)response.getDataObject();
        }
        return userName;
    }
    
    public static String getUserName(String strUserId) {
        String userName = EMPTY_STRING;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('A');
        requester.setDataObject(strUserId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+USER_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            CoeusOptionPane.showErrorDialog(response.getMessage());
            //throw new CoeusException(response.getMessage());
        }else{
            if(objMap != null) {
                if(objMap.containsKey(strUserId)) {
                    userName =objMap.get(strUserId).toString();
                }
                else {
                    userName = (String)response.getDataObject();
                    objMap.put(strUserId, userName);
                }
            }
        }
        return userName;
    }
    
     //Added for case#3855 - start   
    /*
     * Returns the extension of the fileName.
     * @param fileName - Represents the file name.
     * @return String - type of the file.
     */
    // Check for file name , if the file name not null
    // then get the substring from the last index of '.'
    // if the extension present then return the extension
    // otherwise returns an empty string
    public static String getFileExtension(String fileName) {        
        String fileType = null;
        if(fileName!=null ) {
            int index = fileName.lastIndexOf(".");
            if(index >0)
                fileType = fileName.substring(index+1);
            else 
                fileType = "";
        }
        return fileType;
    }
    
    /*
     * Returns the image icon associated with the file in the property file.
     * @param fileType - Represents the type of the file.
     * @return ImageIcon -ImageIcon associated with the file..
     */
    // Returns the image icon associated with the file from property file ,
    // if no icon associated with the the file in the property file then returns 
    // deafultimage icon from defaultAttachmentIconPath 
    public static ImageIcon getAttachmentIcon(String fileType){
        ImageIcon imageIcon = null;
        if(fileType !=null) {
            String defaultAttachmentIconPath = "images/ebinary_attachment.gif";
            try {
                Properties displayProperty = CoeusMessageResources.getInstance().getDisplayProperties();
                
                String imagePath = displayProperty.getProperty("file.icon."+fileType.toLowerCase(),defaultAttachmentIconPath);
                
                URL imageURL = UserUtils.class.getClassLoader().getResource(imagePath);
             
                imageIcon = new ImageIcon(imageURL);
            } catch (Exception e) {
                imageIcon = new ImageIcon( UserUtils.class.getClass().getResource(defaultAttachmentIconPath));
                e.printStackTrace();
            }
        }
      
        return imageIcon;
    }  
    
      // case#3855 - end
}
