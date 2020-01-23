/*
 * SubModuleDataBean.java
 *
 * Created on September 20, 2006, 7:12 PM
 */

package edu.mit.coeus.questionnaire.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.ComboBoxBean;
import java.sql.Timestamp;

/**
 *
 * @author  chandrashekara
 */
public class SubModuleDataBean extends ComboBoxBean implements BaseBean{
    private int moduleCode;
    private String updateUser;
    private Timestamp updateTimestamp;
    private String acType;
    /** Creates a new instance of SubModuleDataBean */
    public SubModuleDataBean() {
        super();
    }
    
    /**
     * Getter for property moduleCode.
     * @return Value of property moduleCode.
     */
    public int getModuleCode() {
        return moduleCode;
    }
    
    /**
     * Setter for property moduleCode.
     * @param moduleCode New value of property moduleCode.
     */
    public void setModuleCode(int moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
      public boolean isLike(ComparableBean comparableBean)
    throws CoeusException{
        return true;
    }
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    
//        public boolean equals(Object obj) {
//        if(super.equals(obj)){
//            SubModuleDataBean subModuleDataBean = (SubModuleDataBean)obj;
//            if(subModuleDataBean.getModuleCode() == getModuleCode()){
//                return true;
//            }else{
//                return false;
//            }
//        }else{
//            return false;
//        }
//    }    
      
      
//       public boolean equals(Object obj) {
//           SubModuleDataBean subModuleDataBean = (SubModuleDataBean)obj;
//           if(subModuleDataBean.getCode() == getCode() &&
//           subModuleDataBean.getModuleCode() == getModuleCode()){
//               return true;
//           }else{
//               return false;
//           }
//       }
       
       public boolean equals(CoeusBean coeusBean) throws CoeusException{
           return isLike(coeusBean);
       }
    
    
}
