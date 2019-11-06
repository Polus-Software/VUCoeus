/*
 * TransferableUserRoleData.java
 *
 * Created on April 10, 2003, 7:56 PM
 */

package edu.mit.coeus.utils.tree;

import java.awt.datatransfer.*;
import java.io.IOException;

import edu.mit.coeus.bean.UserRolesInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import java.util.Vector;

/**
 *
 * @author  ravikanth
 */
public class TransferableUserRoleData implements Transferable {
    
    final public static DataFlavor ROLE_FLAVOR = new DataFlavor(
                                UserRolesInfoBean.class, "UserRolesInfoBean");

    final public static DataFlavor USER_FLAVOR = new DataFlavor(
                                UserInfoBean.class, "UserInfoBean");
    final public static DataFlavor MULTIPLE_USERS_FLAVOR = new DataFlavor ( Vector.class, " Vector of Transferables " );
    
    private final static int ROLE = 0;
    private final static int USER = 1;
    private final static int MULTIPLE_USERS = 2;
    
    static DataFlavor flavors[] = {ROLE_FLAVOR,USER_FLAVOR, MULTIPLE_USERS_FLAVOR};
    
    private UserRolesInfoBean data;
    private DataFlavor selectedFlavor;
    private Vector multipleRecords;
    
    /** Creates a new instance of TransferableUserRoleData */
    public TransferableUserRoleData(UserRolesInfoBean transferData) {
        this.data = transferData;
        this.selectedFlavor = ROLE_FLAVOR;
    }

    public TransferableUserRoleData ( UserInfoBean transferData ){
        this.data = new UserRolesInfoBean();
        data.setUserBean(transferData);
        this.selectedFlavor = USER_FLAVOR;
    }
    
    public TransferableUserRoleData ( Vector multipleDragged ){
        this.multipleRecords = multipleDragged;
        this.selectedFlavor = MULTIPLE_USERS_FLAVOR;
    }

    public Object getTransferData(java.awt.datatransfer.DataFlavor dataFlavor) 
        throws UnsupportedFlavorException, IOException {
            //Chnage
                if (selectedFlavor.equals(flavors[ROLE])) { 
                    return data;
                }else if( selectedFlavor.equals(flavors[USER]) ){
                    return data.getUserBean();

                }else if ( selectedFlavor.equals ( flavors[ MULTIPLE_USERS] ) ) {
                    return multipleRecords;
                }else{
                    throw new UnsupportedFlavorException(dataFlavor);
                }
           /* }else{
                throw new UnsupportedFlavorException(dataFlavor);
            }*/
    }
    
   
    
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

     public boolean isDataFlavorSupported( DataFlavor flavor ) {
       boolean returnValue = false;
       for ( int indx = 0, count = flavors.length; indx < count ; indx++ ) {
         if (flavor.equals( flavors[ indx ] ) ) {
           returnValue = true;
           break;
         }
       }
       return returnValue;
     }
    
    
    /** Getter for property selectedFlavor.
     * @return Value of property selectedFlavor.
     */
    public DataFlavor getSelectedFlavor() {
        return selectedFlavor;
    }    
    
    /** Setter for property selectedFlavor.
     * @param selectedFlavor New value of property selectedFlavor.
     */
    public void setSelectedFlavor(DataFlavor selectedFlavor) {
        this.selectedFlavor = selectedFlavor;
    }
    
    
}
