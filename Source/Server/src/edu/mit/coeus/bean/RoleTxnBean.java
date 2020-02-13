/*
 * @(#)RoleTxnBean.java 1.0 04/17/03 2:59 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 14-AUG-2007
 * by Leena
 */
package edu.mit.coeus.bean;

//import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.HashMap;

import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.exception.CoeusException;

/**
 * This class provides the methods for performing all procedure executions for
 * a roles.Various methods are used to fetch the roles details from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the
 * database interaction.
 *
 * @version 1.0 on April 17, 2003, 2:59 PM
 * @author  Mukundan C
 */

public class RoleTxnBean {
    
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    /** Creates a new instance of RoleTxnBean */
    public RoleTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    
    /**
     * Method used to get roles right list from OSP$RIGHTS and
     * OSP$ROLE_RIGHTS for a given role Id .
     * <li>To fetch the data, it uses DW_GET_ROLE_RIGHTS_FOR_ROLE procedure.
     *
     * @param roleId get list of Roles details for this id
     * @return Vector map of Roles data is set of roleRightInfoBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProposalRightRole(int roleId)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        RoleRightInfoBean roleRightInfoBean = null;
        HashMap rightRoleRow = null;
        param.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,new Integer(roleId).toString()));
        
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ROLE_RIGHTS_FOR_ROLE(<<ROLE_ID>> ,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector rightRoleList = null;
        if (listSize > 0){
            rightRoleList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                roleRightInfoBean = new RoleRightInfoBean();
                rightRoleRow = (HashMap)result.elementAt(rowIndex);
                roleRightInfoBean.setRightId( (String)
                rightRoleRow.get("RIGHT_ID"));
                roleRightInfoBean.setRoleId(
                        Integer.parseInt(rightRoleRow.get("ROLE_ID").toString()));
                //Modified for Coeus 4.3 PT ID 2232 - Custom Roles - start
                //Given name for the descend flag column in th procedure
                /*roleRightInfoBean.setDescendFlag(
                        rightRoleRow.get("DESCEND_FLAG") == null ? false :
                            (rightRoleRow.get("DESCEND_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));*/
                roleRightInfoBean.setDescendFlag(
                        rightRoleRow.get("ROLE_RIGHT_DESCEND_FLAG") == null ? false :
                            (rightRoleRow.get("ROLE_RIGHT_DESCEND_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                roleRightInfoBean.setDescription( (String)
                rightRoleRow.get("DESCRIPTION"));
                roleRightInfoBean.setRightType(
                        rightRoleRow.get("RIGHT_TYPE").toString().charAt(0));
                /*roleRightInfoBean.setRoleDescendFlag(
                        rightRoleRow.get("DESCEND_FLAG_1") == null ? false :
                            (rightRoleRow.get("DESCEND_FLAG_1").toString()
                            .equalsIgnoreCase("y") ? true :false));*/
                roleRightInfoBean.setRoleDescendFlag(
                        rightRoleRow.get("ROLE_DESCEND_FLAG") == null ? false :
                            (rightRoleRow.get("ROLE_DESCEND_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                roleRightInfoBean.setUpdateTimestamp((Timestamp)rightRoleRow.get("UPDATE_TIMESTAMP"));
                //Modified for Coeus 4.3 PT ID 2232 - Custom Roles - end
                rightRoleList.add(roleRightInfoBean);
            }
        }
        return rightRoleList;
    }
    
}
