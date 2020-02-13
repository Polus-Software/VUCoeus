/*
 * @(#)ITreeNodeInfo.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.utils.tree;


import java.util.Vector;

    /**
     * This interface is implemented by all bean class for constructing 
     * customized Tree using TreeBuilder utility class. 
     *
     * @author  subramanya
     * @version 1.0 Created on September 20, 2002, 4:13 PM
     */
    public interface ITreeNodeInfo {

           /** get the Node ID for the Tree(unique no.)        
            * @return String represent the Tree Node ID.
            */
           public String getNodeID(); 
           
           /**get the parent Node ID for the Tree.
            * @return String represent the Parent Node ID.
            */
           public String getParentNodeID();
           
           /**adds the new child to the tree.
            * @param child ITreeNodeInfo instance
            * @return boolean True if sucessfully added else false
            */
           public boolean addNewChild( ITreeNodeInfo child );
           
           /**set child  status
            * @param childrenFlag Sting value.
            */
           public void setChildrenFlag( String childrenFlag );
           
           /** SEts the parent Node id.
            * @param parentNodeID String value.
            */
           public void setParentNodeID( String parentNodeID );

           /** true:if Children exists else false.
            * @return boolean represent the children flag.
            */
           public boolean hasChildren();
           
           /** all children (set of Bean Object) 
            * @return Vector collection of all children Node Data Object.
            */
           public Vector getAllChildrens();
           
           /** get the description of Node
            * @return String represent the Node Description.
            */
           public String getNodeDescription();
           
           /** user friendly name. Ex:- 100001: All Area Of Researsh
            * @return String represent the Node ID concatenated with Node Desc.
            */
           public String getRelativeName();
           
    }