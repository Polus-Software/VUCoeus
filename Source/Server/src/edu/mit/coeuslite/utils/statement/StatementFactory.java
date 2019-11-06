/*
 * StatementFactory.java
 *
 * Created on April 22, 2005, 4:06 PM
 */

package edu.mit.coeuslite.utils.statement;

import edu.mit.coeus.exception.CoeusException;
import java.io.IOException;
import javax.servlet.ServletContext;

/**
 *
 * @author  sharathk
 */
public class StatementFactory {
    
    /** Creates a new instance of StatementFactory */
    public StatementFactory() {
    }
    
    public StatementType getStatementType(Statement statement) throws IOException, CoeusException{
        StatementType statementType = null;
        if(statement.getType().equals(StatementConstants.PROCEDURE)) {
            statementType = new Procedure(statement);
        }else if(statement.getType().equals(StatementConstants.FUNCTION)) {
            statementType = new Function(statement);
        }else if(statement.getType().equals(StatementConstants.SQL)) {
            statementType = new SQL(statement);
        }
        return statementType;
    }
    
}
