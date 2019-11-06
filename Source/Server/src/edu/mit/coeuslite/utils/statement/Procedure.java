/*
 * Procedure.java
 *
 * Created on April 22, 2005, 12:24 PM
 */

package edu.mit.coeuslite.utils.statement;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author  sharathk
 */
public class Procedure extends AbstractStatement{
    private Statement statement;
    /** Creates a new instance of Procedure */
    public Procedure(Statement statement) {
        this.statement = statement;
    }
    
    public ProcReqParameter execute(Object values, String userId)throws CoeusException{
        try{
            List maps = statement.getStatementMapList();
            Vector param;
            boolean hasOutput = false;
            param = getParams(maps, values, userId);
            
            StringBuffer sql = new StringBuffer( "call "+statement.getName()+"(");
            if(maps != null && maps.size() > 0) {
                sql.append(getSqlParams());
            }
            
            if(statement.getStatementOutputList()!= null && statement.getStatementOutputList().size() > 0) {
                hasOutput = true;
            }
            
            if(hasOutput) {
                if(maps != null && maps.size() > 0) {
                    sql.append(" , ");
                }
                List output = statement.getStatementOutputList();
                sql.append(getOutputParams(output));
            }
            sql.append(" ) ");
            
            /*if(hasOutput){
                Vector result = null;
                DBEngineImpl dbEngine = new DBEngineImpl();
                if(dbEngine !=null){
                    result = dbEngine.executeRequest(SERVICE_NAME, sql.toString(), DSN, param);
                }
                return result;
            }*/
            
            ProcReqParameter procReqParameter = null;
            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
            
            return procReqParameter;
        }catch (Exception exception) {
            throw new CoeusException(exception.getMessage());
        }
    }
    
}
