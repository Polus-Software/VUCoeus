package edu.mit.coeus.bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Hashtable;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import java.util.Vector;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;

/**
 * Component for changing a user password in the Coeus database.
 * Provides a method which uses the old password to authenticate the user, as
 * well as a method to change the password for a user who has already been authenticated,
 * for example, via x509 certificate.
 */
public class ChangePasswordBean implements Serializable
{
    /*
     *  Singleton instance of DBEngineImpl
     */
    private DBEngineImpl dbEngine;

    /**
     * No argument constructor for ChangePasswordBean
     * Get a singleton instance of DBEngineImpl.
     * @throws DBException
     * @throws Exception
     */
    public ChangePasswordBean()
    {   dbEngine = new DBEngineImpl();
    }

    /**
     * Change password with only the new password value.  User has already
     * authenticated with x509 certificate.
     * @param username
     * @param newPassword
     * @return
     * @throws DBException
     */
    public void changePassword(String userId, String newPassword)
        throws  CoeusException, DBException
    {   Vector result = new Vector();
        Vector param = new Vector();
        param.addElement(new Parameter("AS_USER", "String", userId));
        param.addElement(new Parameter("AS_PASSWORD", "String", newPassword));

        /* Call the stored procedure to change the password.     */
         result = dbEngine.executeFunctions("Coeus",
        "{ << OUT INTEGER RSET >> = call fn_change_password( << AS_USER >> , << AS_PASSWORD >> ) }",
            param);
            /* Check that the stored procedure is returning success.  Throw
             * CoeusException if the stored procedure returns failure.
             */
             /* case #748 comment begin  */
            //Hashtable resultRow = (Hashtable)result.get(0);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap resultRow = (HashMap)result.get(0);
            /* case #748 end */
            System.out.println("before resultRow.get('rset')");
            String rset = (String)resultRow.get("RSET");
            int rsetInt = Integer.parseInt(rset);
            System.out.println("changePassword stored proc return value: "+rsetInt);
            if(rsetInt < 0)
            { throw new CoeusException("exceptionCode.error.stored.procedure.failure");
            }
    }
}