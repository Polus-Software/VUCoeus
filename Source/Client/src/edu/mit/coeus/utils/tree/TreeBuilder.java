/*
 * @(#)TreeBuilder.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

    package edu.mit.coeus.utils.tree;


        import java.awt.Container;
        import java.awt.BorderLayout;
        import javax.swing.JTree;
        import javax.swing.JScrollPane;
        import javax.swing.JFrame;

        import java.util.Hashtable;
        import java.util.Vector;
        import java.util.StringTokenizer;
        import java.util.Iterator;

        import java.io.FileReader;
        import java.io.BufferedReader;
        import java.io.IOException;

        
        /**
        * This Generalized class used to build tree structure from the DataBase.
        * This class is mainly used to constuct the Tree Structured from 
        * the respective Data Bean instance ( data bean inhertied by ITreeNodeInfo
        * Interface) to support Acessor Method as well as Modifier Methods.
        * Using this instance we can get & set the Tree Node ID, parentNode ID, 
        * TreeNode Description. AddNewChild Routine.
        *
        * @author Subramanya
        * @version 1.0 Septermber 20, 2002, 7.40PM
        */
        public class TreeBuilder{

            /*holds the collection of tree Node data Beans. 
             *Each Bean is specific to user defined Object
             */
            private Vector hierarchyData;
            
            //holds the RootID of the Tree
            private String rootId;
            
            /**
             * Construct the Tree Builder Instance.
             * @param hierarchyData The Data bean Object Collection of Tree Node
             * @param rootId        RootNode ID
             */
            public TreeBuilder( Vector hierarchyData,String rootId ) {

                this.hierarchyData = hierarchyData;
                this.rootId = rootId;
            }
            
            
            /**
            * From any Application or Component just invoke this Method to
            * get JTree Object. 
            * @return   HierarchyNode DefaultMutable Tree Node Contains the 
            *                         Complete Tree with in this Node.  
            */
            public HierarchyNode getHierarchyRoot(){
                
                return buildTreeFromVector();
            }

            
            /*supporting method to consturuct display tree Node from 
             *the data vector
             */
            private HierarchyNode buildTreeFromVector(){

                Hashtable data = constructHashData( hierarchyData, rootId );
                ITreeNodeInfo rootNode = ( ITreeNodeInfo )data.
                                                            get(rootId.trim());   
                HierarchyNode completeTree = new HierarchyNode(
                                                rootNode.getRelativeName());                
                completeTree.add( constructTree( rootNode.getAllChildrens(), 
                                                                    rootNode ));  
                return completeTree;
            }

            
            /*supporting method construct Tree by making recursive call of
             *user defined nested data object.
             */
            private HierarchyNode constructTree( Vector hierarchy, 
                                            ITreeNodeInfo parentNode ) {
                HierarchyNode node = new HierarchyNode( parentNode );
                HierarchyNode child = null;
                ITreeNodeInfo nodeSpecifier = null;
                if(hierarchy != null) { //Added for Bug fix #1824
                    for(int indx = 0; indx < hierarchy.size(); indx ++ ) {
                        
                        nodeSpecifier = ( ITreeNodeInfo ) hierarchy.elementAt( indx );
                        Vector childNode = nodeSpecifier.getAllChildrens();
                        if ( childNode!= null && childNode.size() >= 1 ){
                            child = constructTree( childNode, nodeSpecifier );
                        }else{
                            child = new HierarchyNode( nodeSpecifier ); // Ie Leaf
                            child.setUserObject( nodeSpecifier );
                        }
                        node.add( child );
                    }
                }
              return( node );
        }
        
        
        //supportine method to construct Hashtable Data from Vector data
        private Hashtable constructHashData( Vector unitHObjects, String rootID ){
            
            Hashtable nodeInfo = constructParentChildHash(unitHObjects, rootID);
            ITreeNodeInfo compNodeElement = null;
            ITreeNodeInfo parentNode = null;
            
            for( int indx = 0; indx < unitHObjects.size(); indx ++ ){                
                compNodeElement = ( ITreeNodeInfo ) unitHObjects.get( indx );                
                
                if( compNodeElement != null && compNodeElement.
                              getParentNodeID().equalsIgnoreCase( rootID )){                                  
                    parentNode = ( ITreeNodeInfo ) nodeInfo.get(rootID);                    
                    parentNode.addNewChild( compNodeElement );                    
                }else if( compNodeElement != null  ){
                    
                    parentNode = ( ITreeNodeInfo ) nodeInfo.get(
                                        compNodeElement.getParentNodeID());                    
                    if( parentNode!=null ){                        
                        parentNode.addNewChild( compNodeElement );                        
                    }                    
                }
            }                
            return nodeInfo;        
        }
        
        
        //supporting method to construct all parental node data objects.
        private Hashtable constructParentChildHash( Vector unitHObjects,        
                                                                String rootID ){
                                                                    
            Hashtable parentNodes = new Hashtable();
            ITreeNodeInfo compNodeElement = null;
            
            for( int indx = 0; indx < unitHObjects.size(); indx ++ ){                
                    compNodeElement = ( ITreeNodeInfo ) unitHObjects.get( indx );                    
                if( compNodeElement.hasChildren() ){                
                    parentNodes.put( compNodeElement.getNodeID(),                    
                                                        compNodeElement );
                }                    
            }            
            try{
                parentNodes.put( rootID , getRootNode( unitHObjects, rootID ) );
            }catch( Exception rootNd ){                
                rootNd.printStackTrace();                
            }            
            return parentNodes;            
        }
        
        
        //supporting method to get the root Node data object.
        private ITreeNodeInfo getRootNode( Vector allNodes, String rootId )
                                                              throws Exception{
         
            ITreeNodeInfo rootNode = null;            
            for( int indx = 0; indx < allNodes.size(); indx++ ){                
                if( (( ITreeNodeInfo )allNodes.get(indx)).getNodeID().
                                                    equalsIgnoreCase(rootId)){
                    rootNode = ( ITreeNodeInfo )allNodes.get(indx);
                    break;
                }
            }
            if( rootNode == null){                
                    throw ( new Exception("Root Node ID is Invalid") );                    
            }            
            return rootNode ;
        }       
    }