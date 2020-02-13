/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.utils;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author sharathk
 */
public class AuthenticationLogger {

    private static final String ENABLE_AUTHENTICATION_LOG = "ENABLE_AUTHENTICATION_LOG";

    public void logon(String userId, String ipAddress, String sessionId) {
        try {
            String value = CoeusProperties.getProperty(ENABLE_AUTHENTICATION_LOG);
            if (value != null && ( value.trim().equalsIgnoreCase("true") || value.trim().equalsIgnoreCase("yes") )) {//COEUSQA-3996
                //Store Id, Timestamp, SessionId
                DBEngineImpl dBEngineImpl = new DBEngineImpl();
                StringBuffer sqlBuffer = new StringBuffer();
                sqlBuffer.append("insert into OSP$AUTHENTICATION_LOG(");
                sqlBuffer.append(" USER_ID, ");
                sqlBuffer.append(" IP_ADDRESS,");
                sqlBuffer.append(" SESSION_ID,");
                sqlBuffer.append(" LOGON_TIMESTAMP  ) ");
                sqlBuffer.append(" VALUES (");
                sqlBuffer.append(" <<USER_ID>> , ");
                sqlBuffer.append(" <<IP_ADDRESS>> , ");
                sqlBuffer.append(" <<SESSION_ID>> , ");
                sqlBuffer.append(" <<LOGON_TIMESTAMP>> ) ");

                Timestamp timestamp;

                timestamp = (new CoeusFunctions()).getDBTimestamp();

                Vector param = new Vector();

                param.addElement(new Parameter("USER_ID", "String", userId));
                param.addElement(new Parameter("IP_ADDRESS", "String", ipAddress));
                param.addElement(new Parameter("SESSION_ID", "String", sessionId));
                param.addElement(new Parameter("LOGON_TIMESTAMP", "Timestamp", timestamp));

                ProcReqParameter procReqParameter = new ProcReqParameter();
                procReqParameter.setDSN("Coeus");
                procReqParameter.setParameterInfo(param);
                procReqParameter.setSqlCommand(sqlBuffer.toString());

                dBEngineImpl.sqlUpdate(procReqParameter);
            }
        } catch (Exception exception) {
            UtilFactory.log(exception.getMessage(), exception, "AuthenticationLogger", "logon()");
        }
    }

    public void logout(String sessionId) {
        try {
            String value = CoeusProperties.getProperty(ENABLE_AUTHENTICATION_LOG);
            if (value != null && (value.trim().equalsIgnoreCase("true") || value.trim().equalsIgnoreCase("yes"))) {
                DBEngineImpl dBEngineImpl = new DBEngineImpl();
                StringBuffer sqlBuffer = new StringBuffer();
                sqlBuffer.append("Update OSP$AUTHENTICATION_LOG");
                sqlBuffer.append(" SET  ");
                sqlBuffer.append(" LOGOUT_TIMESTAMP =");
                sqlBuffer.append(" <<LOGOUT_TIMESTAMP>> ");
                sqlBuffer.append(" WHERE SESSION_ID = <<SESSION_ID>>");

                Timestamp timestamp;
                timestamp = (new CoeusFunctions()).getDBTimestamp();

                Vector param = new Vector();

                param.addElement(new Parameter("LOGOUT_TIMESTAMP", "Timestamp", timestamp));
                param.addElement(new Parameter("SESSION_ID", "String", sessionId));

                ProcReqParameter procReqParameter = new ProcReqParameter();
                procReqParameter.setDSN("Coeus");
                procReqParameter.setParameterInfo(param);
                procReqParameter.setSqlCommand(sqlBuffer.toString());

                dBEngineImpl.sqlUpdate(procReqParameter);
            }
        } catch (Exception exception) {
            UtilFactory.log(exception.getMessage(), exception, "AuthenticationLogger", "logout()");
        }
    }
}
