/*
 * @(#)SponsorTextField.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on March 7, 2003, 7:51 PM
 */

package edu.mit.coeus.utils.textfield;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.text.Document;

import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusOptionPane;

/**
 * This class is used to have a text field specific to Sponsor details, which will show
 * the detail window for the given id on double click and also used to validate the
 * entered text with the database.
 * @author  ravikanth
 */
public class SponsorTextField extends CoeusTextField implements IdValidator {
    
    private static final String SERVLET_NAME = "spMntServlet";
    private static final char REQUEST_TYPE = 'D';
    
    private GenerateDetailWindow detailWindow;
    private String detailId;
    
    /** Creates a new instance of SponsorTextField */
    public SponsorTextField() {
        super();
        init();
    }
    
    /**
     * This will construct the SponsorTextField with defult text
     * @param text String representing the default value
     */
    public SponsorTextField(String text){
        super(text);
        init();
    }
    
    /**
     * This will construct the SponsorTextField with defult length
     * @param columns integer which represents the length allowed to type in
     * this textfield.
     */
    public SponsorTextField(int columns){
        super(columns);
        init();
    }
    
    /**
     * This will construct the SponsorTextField with defult text & length
     * @param text String representing the default value
     * @param columns integer which represents the length allowed to type in
     * this textfield.
     */
    public SponsorTextField(String text, int columns){
        super(text,columns);
        init();
    }
    
    /**
     * This will construct the SponsorTextField with defult document, text & length
     * @param doc Document for this textfield.
     * @param text String representing the default value
     * @param columns integer which represents the length allowed to type in
     * this textfield.
     */
    public SponsorTextField(Document doc, String text, int columns){
        super(doc,text, columns);
        init();
    }
    
    /**
     * This method is used to store the id of the displayed details
     * @param id String representing the Id (primary key of the detail window).
     */
    public void setDetailId(String id){
        this.detailId = id;
    }
    
    /**
     * This method is used to get the Id associated with this text field.
     * @return String representing the Id (primary key of the detail window).
     */
    public String getDetailId(){
        return detailId;
    }
    /**
     * This method is used to add the mouse listener which will show the details
     * associated with the detailId stored in this claas on double click event
     * of the mouse
     */
    private void init(){
        detailWindow = new GenerateDetailWindow();
        addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me){
                if (me.getClickCount() == 2) {
                    try{
                        detailWindow.showSponsorDetails(detailId);
                    } catch ( Exception ex){
                        CoeusOptionPane.showInfoDialog(ex.getMessage());
                    }
                }
            }
        });
        
    }
    
    /**
     * This method is used to validate the entered text against the database
     * @return boolean true if the given id is valid, else false.
     */
    public boolean validateId(){
        return detailWindow.validate(detailId,SERVLET_NAME,REQUEST_TYPE);
    }
}
