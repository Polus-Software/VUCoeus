package edu.mit.coeus.bean;

import java.util.*;
import edu.mit.coeus.exception.CoeusException;

/**
 *
 */
public interface CoeusBean extends ComparableBean{
    
    /**
     *
     * @param acType
     */
    public void setAcType(String acType);
    /**
     *
     * @return
     */
    public String getAcType();
    /**
     *
     * @param userId
     */
    public void setUpdateUser(String userId);
    /**
     *
     * @return
     */
    public String getUpdateUser();
    
    public java.sql.Timestamp getUpdateTimestamp();
    
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp);
    
    //public abstract boolean isLike(CoeusBean coeusBean) throws CoeusException;
} // end CoeusBean
