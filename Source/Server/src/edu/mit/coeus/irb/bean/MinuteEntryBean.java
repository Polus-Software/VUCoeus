/*
 * @(#)MinuteEntryBean.java 11/27/02 5:09 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.irb.bean;

import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.UtilFactory;

/**
 * This class used for populating the combobox with one more extra column for
 * sorting. This class extends ComboBoxBean.
 *
 * @author  Mukundan C
 * @version 1.0
 * Created on November 27, 2002, 5:09 PM
 */

public class MinuteEntryBean extends ComboBoxBean {
    // holds the sort id
     private String sortId;
     
    /** Creates a new instance of MinuteEntryBean */
    public MinuteEntryBean() {
    }
 
    /** Constructor with parameters 
     *
     * @param code for the look up form
     * @param sortId for the sorting the data
     * @param description description for the code
     */
    public MinuteEntryBean(String code,String sortId,String description) {
        super(code,description);
        this.sortId = sortId;
    }
            
    /**
     *  This method gets the sort id
     *  @return String sortId
     */
    public String getSortId() {
        return sortId;
    }
    /**
     *  This method sets the sort id
     *  @param sortId String
     */
    public void setSortId(String sortId) {
        this.sortId = sortId;
    }
            
    
}
