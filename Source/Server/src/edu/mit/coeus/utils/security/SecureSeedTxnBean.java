/*
 * SecureSeedTxnBean.java
 *
 * Created on February 16, 2007, 5:36 PM
 */

package edu.mit.coeus.utils.security;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  geot
 */
public class SecureSeedTxnBean {
    private static final String SERVLET_SECURE_SEED = "SERVLET_SECURE_SEED";
    private DBEngineImpl dbEngineImpl;
    /** Creates a new instance of SecureSeedTxnBean */
    public SecureSeedTxnBean() {
        dbEngineImpl = new DBEngineImpl();
    }
    public void insertSecureSeed() throws DBException,CoeusException{
        insertSecureSeed(SERVLET_SECURE_SEED);
    }
    public void insertSecureSeed(String key) throws DBException,CoeusException{
        if(getServerSecureSeedValue(key)!=null) return;
        byte[] randSecKey = CoeusCipher.getRandomSecureSeed();
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();                     
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        param.addElement(new Parameter("KEY_NAME",
            DBEngineConstants.TYPE_STRING,SERVLET_SECURE_SEED));
        param.addElement(new Parameter("KEY_VALUE",
            DBEngineConstants.TYPE_BLOB,randSecKey));
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING,"Coeus"));        
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));                
        StringBuffer strqry = new StringBuffer("insert into OSP$SERVER_SECURITY ");
        strqry.append(" (KEY_NAME,KEY_VALUE,UPDATE_USER,UPDATE_TIMESTAMP ) values");
        strqry.append(" ( << KEY_NAME >> ,");
        strqry.append(" << KEY_VALUE >> ,");
        strqry.append(" << UPDATE_USER >> ,");        
        strqry.append(" << UPDATE_TIMESTAMP >> ) ");

        if (dbEngineImpl != null){
            dbEngineImpl.executePreparedQuery("Coeus",strqry.toString(),param);
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
    
    public String getServerSecureSeedValue(String keyName)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        
        param.addElement(new Parameter("KEY_NAME",
            DBEngineConstants.TYPE_STRING,keyName));
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest("Coeus",
            "call GET_SERVER_SEC_KEY_VALUE( <<KEY_NAME>>" +
                        " , <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        //Vector messageList = null;
        if(listSize > 0){
            row = (HashMap)result.elementAt(0);
            Object seed = row.get("KEY_VALUE");
//            if(seed instanceof ByteArrayInputStream){
//                return ((ByteArrayInputStream)seed).read(
//            }
            return seed.toString();
        }else{
            return null;
        }
        
    }
    
    
    
    public static void main(String args[]) throws Exception{
        new SecureSeedTxnBean().insertSecureSeed();
    }
}
