/*
 * MultiCampusUserTxnBean.java
 *
 * Created on January 26, 2007, 11:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.user.auth.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author geot
 */
public class MultiCampusUserTxnBean {
    private DBEngineImpl dbEngine;
    /** Creates a new instance of MultiCampusUserTxnBean */
    public MultiCampusUserTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    public MultiCampusUserInfoBean getMultiCampusUserInfoBean(String campusUserId,String campusCode) 
                    throws DBException,CoeusException {
        
        Vector param= new Vector();
        Vector result = new Vector();
        param.addElement(new Parameter("CAMPUS_USER_ID","String",campusUserId));
        param.addElement(new Parameter("CAMPUS_CODE","String",campusCode));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_MULTI_CAMPUS_PERSON_MAP ( <<CAMPUS_USER_ID>> , <<CAMPUS_CODE>> ,"+
            " <<OUT RESULTSET rset>> ) ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        MultiCampusUserInfoBean multiCampusUser = null;
        if (!result.isEmpty()){
            HashMap userInfoMap = (HashMap)result.elementAt(0);   
            multiCampusUser = new MultiCampusUserInfoBean();
            multiCampusUser.setMultiCampusPersonId((String)userInfoMap.get("PERSON_ID"));
            multiCampusUser.setUserId((String)userInfoMap.get("USER_ID"));
            multiCampusUser.setCampusCode((String)userInfoMap.get("CAMPUS_CODE"));
            multiCampusUser.setCampusUserId((String)userInfoMap.get("CAMPUS_USER_ID"));
            multiCampusUser.setCampusPersonId((String)userInfoMap.get("CAMPUS_PERSON_ID"));
            multiCampusUser.setUpdateTimestamp((Timestamp)userInfoMap.get("UPDATE_TIMESTAMPE"));
            multiCampusUser.setUpdateUser((String)userInfoMap.get("UPDATE_USER"));
        }
        return multiCampusUser;
    }
}
