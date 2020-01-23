/*
 * @(#)CoeusAppletMDIForm.java 1.0 03/04/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

import java.util.Vector;

import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.CoeusGuiConstants;

/**
 * <CODE>CoeusSearchButton</CODE> is a customizable search button component. This
 * will be used for Coeus Search like Coeus Find Person, Find Rolodex, 
 * Find Sponsor, Find Unit and so on, with Captions/Icon displayed on it. This
 * component will allow the user to choose specific Font Type, Mnemonic and 
 * provide set of accessor method to obtain search results.  
 *
 * @version :1.0 March 04, 2003, 14:30 PM
 * @author  Subramanya
 */
public class CoeusSearchButton extends JButton {
    
    //holds the search button type.
    private String strSearchType = null;

    //holds the search result selection mode
    private int selectionType = CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION;
    
    //holds the result set collection
    private static Vector resultSet = null;       
    
    //holds the search/Magnify Icon Button width
    private static final int WIDTH = 26;
    
    //holds the search/Magnify Icon Button height
    private static final int HEIGHT = 20;
    
    
    /** 
     * Creates a new instance of CoeusSearchButton. 
     * @param lblName Search Button Text/Name can be null if Icon specified.
     * @param icnSearch Search Button Icon can be null if Name specified.
     * @param mneType Mnemonic Type of Search Button.
     * @param strType search type as string variable like "PERSON", "ROLODEX"
     * @param selType int value specify the selection Type.     
     */
    public CoeusSearchButton( String lblName, Icon icnSearch, char mneType, 
                                            String strType, int selType ) {                                                            
            setIcon( icnSearch );            
            new CoeusSearchButton(lblName, mneType, strType, selType );
    }
   
    /** 
     * Creates a new instance of CoeusSearchButton. 
     * @param lblName Search Button Text/Name can be null if Icon specified.     
     * @param mneType Mnemonic Type of Search Button.
     * @param strType search type as string variable like "PERSON", "ROLODEX"
     * @param selType int value specify the selection Type.     
     */
    public CoeusSearchButton( String lblName, char mneType, String strType, 
                                                                int selType ) {                                                            
            setMnemonic( mneType );
            new CoeusSearchButton(lblName, strType, selType );
    }
    
    /** 
     * Creates a new instance of CoeusSearchButton. 
     * @param lblName Search Button Text/Name can be null if Icon specified.
     * @param strType search type as string variable like "PERSON", "ROLODEX"
     * @param selType int value specify the selection Type.     
     */
    public CoeusSearchButton( String lblName, String strType, int selType ) {
        
            setText( lblName );       
            new CoeusSearchButton( strType, selType );
    }
    
    
    /** 
     * Creates a new instance of CoeusSearchButton.     
     * @param icnSearch Search Button Icon can be null if Name specified.     
     * @param strType search type as string variable like "PERSON", "ROLODEX"
     * @param selType int value specify the selection Type.     
     */
    public CoeusSearchButton( Icon icnSearch, String strType, int selType ) {
        
            setIcon( icnSearch );               
            setSize( this.WIDTH, this.HEIGHT );
            new CoeusSearchButton( strType, selType );
    }

    
    
    /** 
     * Creates a new instance of CoeusSearchButton.         
     * @param strType search type as string variable like "PERSON", "ROLODEX"
     * @param selType int value specify the selection Type.     
     */
    public CoeusSearchButton( String strType, int selType ) {
    
            this.strSearchType = strType;
            this.selectionType = selType;   
            try{                
                setFont( CoeusFontFactory.getLabelFont() );            
            }catch( Exception srchError ){
                srchError.printStackTrace();
            }                       
    }

    /** 
     * Creates a new instance of CoeusSearchButton.     
     */
    public CoeusSearchButton() {
    }
    
        
    /**
     * This method is used to fetch the Search Button Type.
     * @return String search Type.
     */
    public String getSearchType(){
        return this.strSearchType;
    }
    
    
    /**
     * This method is used to set the Search Button Type.
     * @param srchType set the search Type.
     */
    public void setSearchType( String srchType ){
        this.strSearchType = srchType;
    }

    /**
     * This method is used to fetch the Search Result Selection Type.
     * @return int SelectionType single or mutiple.
     */
    public int getSelectionType(){
        return this.selectionType;
    }
    
    
    /**
     * This method is used to set the Search Button Selection Type.
     * @param selType set the selection type of search result set window .
     */
    public void setSelectionType( int selType ){
        this.selectionType = selType ;
    }
    
    /**
     * This Method is used to fetch the result set of the search selection.
     * It return a collection object ( vector ) which contain HashMap elements
     * of the resultent.
     * @return Vector collection of result set data
     */
    public Vector getSearchResultSet(){           
        return resultSet;
    }
   
    /**
     * This Method is responsible for Coeus Search Window creation and obtaining
     * result set data from respective search window. The search window opened
     * will be of type modal. The search window opened will be based on 
     * the search window Type and searcj result set selection Type.
     * @return Vector collection of Hash Map elements where in each element
     * represent each row data selected.
     */
    public Vector performSerach() {
        try{
            CoeusSearch searchWindow = new CoeusSearch( CoeusGuiConstants.getMDIForm(),
                                            this.strSearchType, 
                                            this.selectionType ) ;  
            searchWindow.showSearchWindow();  
            resultSet = searchWindow.getMultipleSelectedRows();               
            
        }catch( Exception srchError ){
            srchError.printStackTrace();
        }
        return resultSet;
    }
   
}
