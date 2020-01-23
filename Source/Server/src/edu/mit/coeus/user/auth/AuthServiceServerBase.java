/*
 * AuthServiceServerBase.java
 *
 * Created on August 25, 2006, 11:28 AM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.user.auth;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.SessionConstants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author  Geo Thomas
 */
public abstract class AuthServiceServerBase implements CoeusAuthService{
    private AuthResponseListener lsnr;
    private Hashtable resData;
    /** Creates a new instance of ServerAuth */
    public AuthServiceServerBase() {
    }
    public void setResponse(String userId) throws CoeusException{
        try{
            UserMaintDataTxnBean userMaintDataTxnBean  = new UserMaintDataTxnBean();
            resData = new Hashtable();
            resData.put(KeyConstants.USER_ID, userId);
            resData.put(KeyConstants.USER_INFO, userMaintDataTxnBean.getUser(userId));
            boolean firstTimeLoginCheck = userMaintDataTxnBean.firstTimeLoginCheck(userId);
            resData.put(KeyConstants.FIRST_TIME_LOGIN, new Boolean(firstTimeLoginCheck));
            if(getResponseListener()!=null) getResponseListener().respond(resData);
            UtilFactory.log("User "+ userId + " authenticated and logged in");
        }catch(org.okip.service.shared.api.Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"UserIdAuthService", "authenticate");
            throw new CoeusException(ex.getMessage());
        }catch(DBException ex){
            UtilFactory.log(ex.getMessage(),ex,"UserIdAuthService", "authenticate");
            throw new CoeusException(ex.getMessage());
        }
    }
    private Hashtable ht;
    public void addParam(Object key,Object value){
        if(ht==null) ht = new Hashtable();
        ht.put(key, value);
        setParams(ht);
    }
    public final void addResponseListener(AuthResponseListener lsnr){
        this.lsnr = lsnr;
    }
    public AuthResponseListener getResponseListener(){
        return lsnr;
    }
    public void setResponseForWeb(String userId) throws CoeusException{
        UserDetailsBean userDetails = new UserDetailsBean();
        try{
            PersonInfoBean personInfo = userDetails.getPersonInfo(userId);
            UserInfoBean userInfoBean = userDetails.getUserInfo(userId);
            HashMap map = new HashMap();
            if(personInfo.getPersonID()!=null) map.put(SessionConstants.LOGGED_IN_PERSON, personInfo);
            if(userInfoBean.getUserId()!=null) map.put(SessionConstants.USER, userInfoBean);
            if(getResponseListener()!=null) getResponseListener().respond(map);
        }catch(DBException ex){
            UtilFactory.log(ex.getMessage(),ex, "SSLAuthService", "authenticate");
            throw new CoeusException(ex.getMessage());
        }
    }
    
}
