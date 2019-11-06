/*
 * @(#)SequenceLogic.java August 6, 2003, 12:15 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;
import edu.mit.coeus.utils.TypeConstants;
import java.util.Vector;
import edu.mit.coeus.utils.CoeusVector;
/*
 * This class contains implementation of Sequence Number Logic. 
 * Note : This class should be called only when some data is changed in Detail table.
 * SequenceLogic.java
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on August 6, 2003, 12:15 PM
 */

public class SequenceLogic {
    
    private IBaseDataBean iBaseDataBean; 
    private Vector vctDetails; 
    private boolean generateSequence;
    
    /** Creates a new instance of SequenceLogic */
    public SequenceLogic(IBaseDataBean iBaseDataBean, boolean generateSequence) {
        if(generateSequence){
            iBaseDataBean.setSequenceNumber(iBaseDataBean.getSequenceNumber()+1);
            iBaseDataBean.setAcType("I");
        }
        this.iBaseDataBean = iBaseDataBean;
        this.generateSequence = generateSequence;
    }
    
    public IBaseDataBean processParent(){
        return iBaseDataBean;
    }
    
    public Vector processDetails(Vector vctChildBeans){
        Vector vctFilteredData = new Vector();
        IBaseDataBean childBean;
        if(vctChildBeans!=null){
            if(generateSequence){
                for(int row = 0;row < vctChildBeans.size();row++){
                    childBean = (IBaseDataBean)vctChildBeans.elementAt(row);
                    if(childBean.getAcType()==null
                        || !(childBean.getAcType().equalsIgnoreCase("D"))){
                        childBean.setAcType("I");
                        childBean.setSequenceNumber(this.iBaseDataBean.getSequenceNumber());
                        vctFilteredData.addElement(childBean);
                    }                    
                }
            }else{
                for(int row = 0;row < vctChildBeans.size();row++){
                    childBean = (IBaseDataBean)vctChildBeans.elementAt(row);
                    //If Bean is not marked for deletion then check for Sequence Number of Parent & Child.
                    //If there is a mismatch then make the action type as Insert
                    //Modified for COEUSQA-2714 In the Alternative Search in IACUC-start
                    if( !TypeConstants.DELETE_RECORD.equals(childBean.getAcType())){
                    //Modified for COEUSQA-2714 In the Alternative Search in IACUC-End
                        if(childBean.getSequenceNumber() != this.iBaseDataBean.getSequenceNumber()){
                            childBean.setAcType("I");
                            childBean.setSequenceNumber(this.iBaseDataBean.getSequenceNumber());
                        }
                    }
                    //Add only Beans that are modified
                    if( childBean.getAcType() != null ){
                        vctFilteredData.addElement( childBean );
                    }
                }            
            }
        }
        return vctFilteredData;
    }
    
    /** Getter for property iBaseDataBean.
     * @return Value of property iBaseDataBean.
     */
    public edu.mit.coeus.bean.IBaseDataBean getParentDataBean() {
        return iBaseDataBean;
    }
    
    /** Setter for property iBaseDataBean.
     * @param iBaseDataBean New value of property iBaseDataBean.
     */
    private void setParentDataBean(edu.mit.coeus.bean.IBaseDataBean iBaseDataBean) {
        this.iBaseDataBean = iBaseDataBean;
    }
    
    /** Getter for property generateSequence.
     * @return Value of property generateSequence.
     */
    public boolean isGenerateSequence() {
        return generateSequence;
    }
    
    /** Setter for property generateSequence.
     * @param generateSequence New value of property generateSequence.
     */
    private void setGenerateSequence(boolean generateSequence) {
        this.generateSequence = generateSequence;
    }
    
    /*
     * This method is used to set the Children Beans Actype to "I" if there is a match
     * between Parent and Child records sequence number. 
     * This will copy partial(only modified) or full data based on the parameter passed
     *
     * @return CoeusVector
     * @param vctChildBeans CoeusVector
     * @param copyAllOrModifiedChildren boolean
     */
    public CoeusVector processDetails(CoeusVector vctChildBeans, boolean copyAllOrModifiedChildren){
        CoeusVector vctFilteredData = new CoeusVector();
        IBaseDataBean childBean;
        if(vctChildBeans!=null){
            if(generateSequence){
                for(int row = 0;row < vctChildBeans.size();row++){
                    childBean = (IBaseDataBean)vctChildBeans.elementAt(row);
                    if(childBean.getAcType()==null
                        || !(childBean.getAcType().equalsIgnoreCase("D"))){
                        childBean.setAcType("I");
                        childBean.setSequenceNumber(this.iBaseDataBean.getSequenceNumber());
                        vctFilteredData.addElement(childBean);
                    }                    
                }
            }else{
                for(int row = 0;row < vctChildBeans.size();row++){
                    childBean = (IBaseDataBean)vctChildBeans.elementAt(row);
                    //If Bean is not marked for deletion then check for Sequence Number of Parent & Child.
                    //If there is a mismatch then make the action type as Insert
                    
                    if( childBean.getAcType() == null 
                            || (!childBean.getAcType().equalsIgnoreCase("D"))){
                        if(childBean.getSequenceNumber() != this.iBaseDataBean.getSequenceNumber()){
                            childBean.setAcType("I");
                            childBean.setSequenceNumber(this.iBaseDataBean.getSequenceNumber());
                        }
                    }else if(childBean.getAcType() != null && childBean.getAcType().equalsIgnoreCase("D")){
                        if(childBean.getSequenceNumber() != this.iBaseDataBean.getSequenceNumber()){
                            childBean.setAcType("D");                           
                        }
                    }
                    //Add only Beans that are modified
                    if( childBean.getAcType() != null ){
                        vctFilteredData.addElement( childBean );
                    }
                }
            }
        }
        return vctFilteredData;
    }    
}
