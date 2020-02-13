/*
 * @(#)AuthorizationTxnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import edu.mit.coeus.utils.query.AuthorizationOperator;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.is.service.authorization.*;
import edu.mit.coeus.bean.AuthorizationBean;
import edu.mit.coeus.exception.CoeusException;
/**
 * The class is used to Perform Authorization checks.
 * It calls Authorization API based on the AuthorizationOperator passed.
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on June 19, 2004, 12:26 PM
 */

public class AuthorizationTxnBean {
    /*
     * Creates new AuthorizationTxnBean 
     */
    
    public AuthorizationTxnBean() {
    }    
    
    /**
     * @param authorizationOperator AuthorizationOperator
     * @return boolean true if Authorization is success else false
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     * @exception org.okip.service.shared.api.Exception if any exception in Authorization API. 
     */    
    public boolean getResult(AuthorizationOperator authorizationOperator) 
        throws DBException,CoeusException,org.okip.service.shared.api.Exception{
        boolean success = false;
        AuthorizationBean authorizationBean = (AuthorizationBean)authorizationOperator.getBaseBean();
        Factory myAuthFactory =
        (Factory) org.okip.service.shared.api.FactoryManager.getFactory(
                "org.okip.service.authorization.api",
                "edu.mit.is.service.authorization", null );
        Person person = null;
        Function function = null;
        Qualifier qualifier = null;
        FunctionType functionType = null;            

        if(authorizationBean.getFunction()!=null &&
            authorizationBean.getQualifierType()!=null){ 
            //Check for Right in Given Unit
            person = (Person) myAuthFactory.newPerson(authorizationBean.getPerson());
            function =  (Function) myAuthFactory.newFunction (authorizationBean.getFunctionType(), authorizationBean.getFunction());
            qualifier = (Qualifier) myAuthFactory.newQualifier(authorizationBean.getQualifierType(), authorizationBean.getQualifier());            

            success = myAuthFactory.isAuthorized(person, function, qualifier);

        }else if(authorizationBean.getFunction()!=null &&
            authorizationBean.getQualifier()==null &&
            authorizationBean.getFunctionType()!=null){
            //Check for Right in any Unit
                
            person = (Person) myAuthFactory.newPerson(authorizationBean.getPerson());
            function =  (Function) myAuthFactory.newFunction (authorizationBean.getFunctionType(), authorizationBean.getFunction());
            qualifier = null;            

            success = myAuthFactory.isAuthorized(person, function, qualifier);
        }else if(authorizationBean.getFunctionType()!=null &&
            authorizationBean.getFunction() == null && 
            authorizationBean.getQualifier() == null){

            functionType = (FunctionType)myAuthFactory.newFunctionType("OSP","");
            person = (Person) myAuthFactory.newPerson(authorizationBean.getPerson());
            qualifier = null;

            success = myAuthFactory.isAuthorized( person, functionType, qualifier);

        }else if(authorizationBean.getFunction()!=null &&
            authorizationBean.getFunctionType()!=null && 
            authorizationBean.getQualifier()==null){

            functionType = (FunctionType)myAuthFactory.newFunctionType("OSP","");
            person = (Person) myAuthFactory.newPerson(authorizationBean.getPerson());
            function =  (Function) myAuthFactory.newFunction (authorizationBean.getFunctionType(), authorizationBean.getFunction());

            success = myAuthFactory.isAuthorized( person, function, functionType);                
        }
        return success;        
    }
}