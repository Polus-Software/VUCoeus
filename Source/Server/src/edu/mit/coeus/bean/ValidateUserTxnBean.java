/*
 * ValidateUserTxnBean.java
 *
 * Created on May 7, 2003, 2:11 PM
 */

package edu.mit.coeus.bean;

import java.sql.SQLException;
import java.util.* ;

import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.exception.CoeusException;

public class ValidateUserTxnBean
{
      // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    
    
    /** Creates a new instance of ValidateUserTxnBean */
    public ValidateUserTxnBean()
    {
        dbEngine = new DBEngineImpl();
    }
    
    
    public boolean isThisUserValidUser(String userId)
    {
      try
       {
        Vector result = null;
        HashMap nextNumRow = null;
        Vector param= new Vector();
        param.addElement(new Parameter("AS_USER_ID",
                    DBEngineConstants.TYPE_STRING, userId));

        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER SUCCESS>> = call fn_is_valid_coeus_user( "
            + " << AS_USER_ID >> )}", param) ;
        }
        
        if(!result.isEmpty())
        {
            nextNumRow = (HashMap)result.elementAt(0);
            int success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
            if (success == 1)
            {
                return true ;
            }    
                
        }
        
      }catch(Exception ex)
      {
        System.out.println("***  Error in validating user ***") ;  
        ex.printStackTrace() ;
        return false ;
      }  
        
        return false ;
    }
    
    
}
