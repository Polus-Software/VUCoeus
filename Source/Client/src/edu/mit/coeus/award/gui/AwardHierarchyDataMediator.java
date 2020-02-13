/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.gui;

import java.util.Hashtable;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.query.*;


/**
 * AwardHierarchyDataMediator.java
 * Created on March 31, 2004, 12:18 PM
 * @author  Vyjayanthi
 */
public class AwardHierarchyDataMediator {
    
    /** Holds the key value pairs for the hierarchy data */
    private Hashtable htAwardHierarchy = new Hashtable();
    
    /** Holds an instance of <CODE>AwardHierarchyBean</CODE> */
    private AwardHierarchyBean awardHierarchyBean;
    
    /** Holds the entire collection of AwardHieararchyBeans */
    private CoeusVector cvHierarchyData;
    
    /** Holds the child beans for a given parent */
    private CoeusVector cvChildren = new CoeusVector();
    
    /** Creates a new instance of AwardHierarchyDataMediator */
    public AwardHierarchyDataMediator() {
    }
    
    /** Method to return the bean for the given key which is the mit award number
     * @param awardHierarchyBean holds the mit award number
     * @return awardHierarchyBean
     */
    public AwardHierarchyBean getValue(AwardHierarchyBean awardHierarchyBean){
        if( !htAwardHierarchy.containsKey(awardHierarchyBean.getMitAwardNumber()) ){
            awardHierarchyBean = addBeanData(awardHierarchyBean);
        }else{
            awardHierarchyBean = (AwardHierarchyBean)htAwardHierarchy.get(awardHierarchyBean.getMitAwardNumber());
        }
        return awardHierarchyBean;
    }
    
    /** To set the child count and children properties of the bean
     * @param awardHierarchyBean 
     * @return awardHierarchyBean the complete bean with child count and child data
     */
    private AwardHierarchyBean addBeanData(AwardHierarchyBean awardHierarchyBean){    
        CoeusVector cvChildData = new CoeusVector();
        String mitAwardNumber = awardHierarchyBean.getMitAwardNumber();
        Equals eqParentMitAwardNumber = new Equals(
        AwardHierarchyTree.PARENT_MIT_AWARD_NUMBER_FIELD, mitAwardNumber);
        cvChildren = cvHierarchyData.filter(eqParentMitAwardNumber);
        if(cvChildren == null || cvChildren.size() == 0) {
            awardHierarchyBean.setChildCount(0);
        }else{
            awardHierarchyBean.setChildCount(cvChildren.size());
            cvChildData.addAll(cvChildren);
        }
        awardHierarchyBean.setChildren(cvChildData);
        htAwardHierarchy.put(mitAwardNumber, awardHierarchyBean);
        return awardHierarchyBean;
    }

    /** To construct the entire hashtable from cvHierarchyData
     */
    public void setValues(){
        for( int index = 0; index < cvHierarchyData.size(); index++ ){
            awardHierarchyBean = (AwardHierarchyBean)cvHierarchyData.get(index);
            if( !htAwardHierarchy.containsKey(awardHierarchyBean.getMitAwardNumber()) ){
                addBeanData(awardHierarchyBean);
            }
        }
    }
    
    /** Setter for property cvHierarchyData.
     * @param cvHierarchyData New value of property cvHierarchyData.
     *
     */
    public void setHierarchyData(edu.mit.coeus.utils.CoeusVector cvHierarchyData) {
        this.cvHierarchyData = cvHierarchyData;
    }
    
    /** To update the awardHierarchyBean with the new child count and 
     * children properties, when awards are copied from Award Copy Screen
     * @param awardHierarchyBean
     */
    public void forceReload(AwardHierarchyBean awardHierarchyBean) {
        addBeanData(awardHierarchyBean);
    }
    
    //Bug Fix:Performance Issue (Out of memory) Start 1
    public void cleanUp(){
        htAwardHierarchy = null;
        awardHierarchyBean = null;   
        cvHierarchyData = null;
        cvChildren = null;
    }
    //Bug Fix:Performance Issue (Out of memory) End 1
}
