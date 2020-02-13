/*
 * ProtocolTxnBean.java
 *
 * Created on March 9, 2005, 2:10 PM
 */

package edu.mit.coeuslite.irb.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author  jinum
 */
public class ProtocolTxnBean {
    
    private DBEngineImpl dbEngine;
    private TransactionMonitor  transMon;
    private String userId;
    private Timestamp dbTimestamp;    
//    private Connection conn = null;
    
    /** Creates a new instance of ProtocolTxnBean */
    public ProtocolTxnBean() {
//        CoeusProperties.setCoeusPropFile("/coeus.properties");
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    /** Creates new <CODE>ProtocolTxnBean</CODE>
     */
    public ProtocolTxnBean(String userId) {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        this.userId = userId;
    }    
    public Vector getProtocolList(String personId, String protocolType)
            throws CoeusException, DBException {
        Vector vecResult = new Vector();
        Vector  param = new Vector();
        Vector procedures = new Vector(5,3);

        param.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING,personId));
        param.addElement(new Parameter("TYPE_PRO", DBEngineConstants.TYPE_STRING,protocolType.trim()));
        StringBuffer strqry = new StringBuffer("call GET_WEB_PROTOCOL_LIST ( ");
        strqry.append(" << AW_PERSON_ID >> ,");
        strqry.append(" << TYPE_PRO >> ,");
        strqry.append("<< OUT RESULTSET rset >> )");
        if (dbEngine != null) {
            vecResult = dbEngine.executeRequest("Coeus",strqry.toString(),"Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return vecResult;
    }
    public Vector getSaveStatus (String protocolNum)
        throws CoeusException, DBException {
        Vector vecResult = new Vector();
        Vector  param = new Vector();
        Vector procedures = new Vector(5,3);

        param.addElement(new Parameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,protocolNum));
        StringBuffer strqry = new StringBuffer("call GET_WEB_PROTO_MENU_CHKLST ( ");
        strqry.append(" << AV_PROTOCOL_NUMBER >> ,");
        strqry.append("<< OUT RESULTSET rset >> )");
        if (dbEngine != null) {
            vecResult = dbEngine.executeRequest("Coeus",strqry.toString(),"Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return vecResult;
    }
    
    public void updateSaveStatus (String protocolNum,String fieldName,String updateUser,String acType) 
        throws CoeusException, DBException{
        Vector vecResult = new Vector();
        Vector  param = new Vector();
        Vector procedures = new Vector(5,3);
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();   
        param.addElement(new Parameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,protocolNum));
        param.addElement(new Parameter("AV_FIELD_NAME", DBEngineConstants.TYPE_STRING,protocolNum));
        param.addElement(new Parameter("AV_SAVED", DBEngineConstants.TYPE_STRING,"Y"));
        param.addElement(new Parameter("AV_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_STRING,dbTimestamp));
        param.addElement(new Parameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,updateUser));   
        param.addElement(new Parameter("AW_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,protocolNum));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_STRING,dbTimestamp));
        param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING,acType));
        
        StringBuffer strqry = new StringBuffer("call UPDATE_MENU_CHECKLIST ( ");
        strqry.append(" << AV_PROTOCOL_NUMBER >> ,");
        strqry.append(" << AV_FIELD_NAME >> ,");
        strqry.append(" << AV_SAVED >> ,");
        strqry.append(" << AV_UPDATE_TIMESTAMP >> ,");
        strqry.append(" << AV_UPDATE_USER >> ,");
        strqry.append(" << AW_PROTOCOL_NUMBER >> ,");
        strqry.append(" << AW_UPDATE_TIMESTAMP >> ,");
        strqry.append(" << AC_TYPE >> ");

        if (dbEngine != null) {
            dbEngine.executeRequest("Coeus",strqry.toString(),"Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        //return vecResult;
        
    }
//    public static void main(String args[]){
//        try{
//            ProtocolTxnBean bean = new ProtocolTxnBean();
//            bean.getProtocolList("900039164", "ALL_PROTOCOLS");
//        }catch(DBException ex){
//            ex.printStackTrace();
//        }catch(CoeusException ex){
//            ex.printStackTrace();
//        }
//    }
}
