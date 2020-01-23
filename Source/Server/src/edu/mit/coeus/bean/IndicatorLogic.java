/*
 * @(#)IndicatorLogic.java 1.0 04/16/03 7:59 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import java.util.Vector;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.*;
/**
 * This class provide method for setting the indicator logic
 * processLogic is the methods which returns the string which holds the indicator
 * logic string
 *
 * @version 1.0 on April 16, 2003, 7:59 PM
 * @author  Mukundan C
 */

public class IndicatorLogic {
    
    private static final String MODIFIED = "M";
    private static final String NOT_MODIFIED = "NM";
    private static final String NOT_PRESENT = "NP";
    //private static final boolean IS_ALL_DELETES = ;
    
    public IndicatorLogic() {
    }
    
    /**
     * This method is used to set the indicator logic for the in the base table
     * if any changes relates to the child table.
     * Vector childactype provides the information that about the child table
     * record modification/insertion or deletions 
     * String dataStatus  tells the information that whether child table is modified
     * for particular module or notmodified or data not persent for that parent
     * table.
     * 
     * @param Vetcor childAcTypes will holds the actype fpr child table
     * @param String dataStatus holds the childs modified or not modified string
     *
     */
    public String processLogic(Vector childAcTypes, String dataStatus) {
        String indicator = "N0";
        if(dataStatus != null){
            if(dataStatus.trim().equalsIgnoreCase(MODIFIED)){
                
                if (childAcTypes != null) {
                    int size = childAcTypes.size();
                    boolean isAllDeletes = true;
                    for (int index = 0; index < size; index++) {
                        String childAcType = (String) childAcTypes.elementAt(index);
                        // commented by ravi on 23/6/03 to check for null value in acType
                        // if (!childAcType.equalsIgnoreCase("D")) {
                          //  isAllDeletes = false;
                           // break;
                        if( childAcType == null || !childAcType.equalsIgnoreCase("D") ){
                            isAllDeletes = false;
                            break;
                        }
                    }
                 
                    //if all the actype is "D" means all the records have been deleted
                    if( isAllDeletes) {
                        indicator = "N1";
                    }else{
                     //if any one record is with actype "I" or "U" 
                        indicator = "P1";
                    }
                }
            }else{
                // if the data is present for the parent record but not modified
                if(dataStatus.trim().equalsIgnoreCase(NOT_MODIFIED)){
                    indicator = "P0";
                }/* if the data not present for the parent record or if child record 
                    not created for the parent */
                else if(dataStatus.trim().equalsIgnoreCase(NOT_PRESENT)){
                    indicator = "N0";
                }
            }
        }
        return indicator;
    }
    
    public String processLogic(String dataStatus, boolean isAllDeletes) {
        String indicator = "N0";
        if(dataStatus != null){
            if(dataStatus.trim().equalsIgnoreCase(MODIFIED)){
                //if all the actype is "D" means all the records have been deleted
                if( isAllDeletes) {
                    indicator = "N1";
                }else{
                 //if any one record is with actype "I" or "U" 
                    indicator = "P1";
                }
            }else{
                // if the data is present for the parent record but not modified
                if(dataStatus.trim().equalsIgnoreCase(NOT_MODIFIED)){
                    indicator = "P0";
                }/* if the data not present for the parent record or if child record 
                    not created for the parent */
                else if(dataStatus.trim().equalsIgnoreCase(NOT_PRESENT)){
                    indicator = "N0";
                }
            }
        }
        return indicator;
    }
    
    /**
     * This method is used to set the indicator logic for the parent table
     * if any changes relates to the child table based on the Actype values 
     * of child records. 
     * This method uses CoeusVector and will return indicators like P0, P1, N0 or N1
     * P0 - Child records are Present not modified
     * P1 - Child records are present and atleast one record is modified/inserted
     * N0 - Child records are not present i.e., not modified in this operation
     * N1 - Child records are not present i.e., all are in deleted state
     *
     * @param childRecords CoeusVector of Child records
     * @return String containing indicator like  P0,P1,N0 or N1
     **/    
    public String processLogic(CoeusVector childRecords){
        String indicator = "";
        CoeusVector filteredChild = null;
        Equals acTypeEquals = null;
        
        if(childRecords != null && childRecords.size() > 0){
            //if there are records
            acTypeEquals = new Equals("acType", null);
            filteredChild = childRecords.filter(acTypeEquals);
            if(filteredChild!=null && filteredChild.size() > 0){
                if(filteredChild.size() == childRecords.size()){
                    //There are records but nothing is modified
                    indicator = "P0";
                }else{
                    //There are some records which are not modified and
                    //some which has acType as null
                    indicator = "P1";
                }
            }else{
                //There are some records modified/deleted
                acTypeEquals = new Equals("acType", "D");
                filteredChild = childRecords.filter(acTypeEquals);
                if(filteredChild!=null && filteredChild.size() > 0){
                    if(filteredChild.size() == childRecords.size()){
                        //All records are deleted
                        indicator = "N1";
                    }else{
                        //Few records are deleted not all
                        indicator = "P1";
                    }
                }else{
                    //No records are deleted but may be added/modified
                    indicator = "P1";
                }
            }
        }else{
            //If no records is present
            indicator = "N0";
        }
        return indicator;
    }
    /*
    public static void main(String args[]){
        try{            
            edu.mit.coeus.award.bean.AwardTxnBean awardTxnBean = new edu.mit.coeus.award.bean.AwardTxnBean();
            CoeusVector childRecords = awardTxnBean.getAwardComments("000466-001");
            IndicatorLogic indicatorLogic = new IndicatorLogic();
            ((edu.mit.coeus.award.bean.AwardBaseBean)childRecords.elementAt(0)).setAcType("U");
            ((edu.mit.coeus.award.bean.AwardBaseBean)childRecords.elementAt(1)).setAcType("I");
            childRecords = null;
            String indicator = indicatorLogic.processLogic(childRecords);
            System.out.println("Indicator : "+indicator);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }    
    */
}//end of class
