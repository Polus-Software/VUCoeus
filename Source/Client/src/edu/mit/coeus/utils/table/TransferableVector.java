/*
 * TransferableVector.java
 *
 * Created on July 18, 2003, 2:38 PM
 */

package edu.mit.coeus.utils.table;

import java.util.Vector;
import java.awt.datatransfer.*;

/** A Transferable which implements the capability required to transfer a Vector.
 * This Transferable properly supports all flavors.
 *
 * @author sharathk
 */
public class TransferableVector implements Transferable {
    
    DataFlavor dataFlavor[] = {new DataFlavor(Vector.class,"Vector")};
    
    Vector rowToTransfer;
    
    TransferableVector(Vector rowToTransfer) {
        this.rowToTransfer = rowToTransfer;
    }
    
    /** Returns the Transferable's data as Vector.
     * @param dataFlavor the requested flavor for the data
     * @throws UnsupportedFlavorException if the DataFlovor is not supported.
     * @throws IOException if an IOException occurs while retrieving the data.
     * @return the data in the requested flavor, as outlined above
     *
     */
    public Object getTransferData(java.awt.datatransfer.DataFlavor dataFlavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        return rowToTransfer;
    }
    
    /** Returns an array of flavors in which this Transferable can provide the data. DataFlavor.stringFlavor is properly supported.
     * @return an array of length one.
     *
     */
    public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
        return dataFlavor;
    }
    
    /** Returns whether the requested flavor is supported by this Transferable.
     * @param dataFlavor the requested flavor for the data
     * @return true if flavor is equal to DataFlavor.
     */
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor dataFlavor) {
        /*System.out.println(dataFlavor.getClass());
        if(dataFlavor.equals(this)) {
            return true;
        }*/
        return true;
    }
    
}

