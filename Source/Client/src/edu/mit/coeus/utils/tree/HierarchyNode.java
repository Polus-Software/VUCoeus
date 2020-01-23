/*
 * @(#)HierarchyNode.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.utils.tree;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

    /**
     *
     * This class is used to represent the Sepcific Tree Node. It inherits 
     * the DefaultMutableTreeNode class to import the features of MutableTree
     * Node. It is intended to mainly capture the Transfer Data Flavour.
     * @author  geo
     * @updated Subramanya
     * @version 1.1 September 20, 2002, 4:50 PM
     *
     */
    public class HierarchyNode extends javax.swing.tree.DefaultMutableTreeNode
                                                    implements Transferable{

        /**holds the DataFlavour instance to store Node Information.                                                        
         */
        final public static DataFlavor INFO_FLAVOR =
            new DataFlavor(HierarchyNode.class, "Node Information");

        /** DataFlavour elements.
         */
        static DataFlavor flavors[] = {INFO_FLAVOR };

        /** Creates new HierarchyNode  with specific Object as default value
         * @param object instance of data value.
         */
        public HierarchyNode(Object object) {
            super(object);
        }
        
        
        /**
         * This method check for the data flavour support
         * @param df DataFlavor Instance.
         * @return boolean true if supported else false.         
         */
        public boolean isDataFlavorSupported(DataFlavor df) {
            return df.equals(INFO_FLAVOR);
        }

        /**
         * This method is used to get The Transfered Data Object.
         * @param df DataFlavour instance.
         * @return Object Transfered Object instance
         * @throws UnsupportedFlavorException Unsupported
         * @throws IOException IOException
         */
        public Object getTransferData(DataFlavor df)
                            throws UnsupportedFlavorException, IOException {

            if (df.equals(INFO_FLAVOR)) {
                return this;
            }else{
                throw new UnsupportedFlavorException(df);
            }
        }

        /**
         * This method is used to get set of All Transfered instances.
         * @return DataFlavour[] array of transfered instances.
         */
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }
        
    }
