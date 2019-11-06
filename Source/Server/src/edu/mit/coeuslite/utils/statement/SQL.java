/*
 * SQL.java
 *
 * Created on April 22, 2005, 12:24 PM
 */

package edu.mit.coeuslite.utils.statement ;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author  sharathk
 */
public class SQL extends AbstractStatement{
    private Statement statement;
    
    /** Creates a new instance of SQL */
    public SQL(Statement statement) {
        this.statement = statement;
    }
    
    public ProcReqParameter execute(Object values, String userId) throws edu.mit.coeus.exception.CoeusException {
        try{
            List maps = statement.getStatementMapList();
            Vector param;
            ProcReqParameter procReqParameter = null;
            
            String sql = statement.getValue();
            sql = sql.replaceAll("[{]","<<");
            sql = sql.replaceAll("[}]",">>");
                       
            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            param = getParams(maps, values, userId);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql);
            
            return procReqParameter;
        }catch (Exception exception) {
            throw new CoeusException(exception.getMessage());
        }
    }
    
}
