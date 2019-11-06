/*
 * Function.java
 *
 * Created on April 22, 2005, 12:24 PM
 */

package edu.mit.coeuslite.utils.statement;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author  sharathk
 */
public class Function extends AbstractStatement{
    private Statement statement;
    
    /** Creates a new instance of Function */
    public Function(Statement statement) {
        this.statement = statement;
    }
    
    public ProcReqParameter execute(Object values, String userId)throws CoeusException{
        try{
            List maps = statement.getStatementMapList();
            Vector param;
            ProcReqParameter procReqParameter = null;
            
            List output  = statement.getStatementOutputList();
            
            StringBuffer sql = new StringBuffer( "{ "+getOutputParams(output)+" = call "+statement.getName()+" ( ");
            StatementMap statementMap;
            param = getParams(maps, values, userId);
            if(maps != null && maps.size() > 0) {
                sql.append(getSqlParams());
            }
            
            sql.append(" ) }");
            
            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            param = getParams(maps, values, userId);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
            
            return procReqParameter;
        }catch (Exception exception) {
            throw new CoeusException(exception.getMessage());
        }
    }
}
