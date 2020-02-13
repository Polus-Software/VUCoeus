/*
 * ModuleDataBean.java
 *
 * Created on September 20, 2006, 7:10 PM
 */

package edu.mit.coeus.questionnaire.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.ComboBoxBean;
import java.sql.Timestamp;

/**
 *
 * @author  chandrashekara
 */
public class ModuleDataBean extends ComboBoxBean implements BaseBean{
    private String updateUser;
    private Timestamp updateTimestamp;
    private String acType;
    /** Creates a new instance of ModuleDataBean */
    public ModuleDataBean() {
        super();
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
    
}
