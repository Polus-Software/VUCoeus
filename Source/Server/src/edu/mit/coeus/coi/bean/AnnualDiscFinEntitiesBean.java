/*
 * @(#)AnnualDisclFinEntitiesBean.java 1.0 6/4/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.coi.bean;

import java.sql.SQLException;
import java.util.Vector;
import java.util.HashMap;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.exception.CoeusException;


/**
 * <code>AnnualDisclFinEntitiesBean</code> is a class to get all
 * the Pending Financial Entities for a particular person.
 *
 * @version 1.0 June 4,2002
 * @author Phaneendra Kumar.
 */
public class AnnualDiscFinEntitiesBean {

    /*
     * Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;

    /*
     * Initialize bean with a particular person id. ie, All methods
     * in this class always belong to a person.
     */
    private String personId;

    /**
     * Creates new AnuualDiscFinEntities.
     */
    public AnnualDiscFinEntitiesBean(){
        dbEngine = new DBEngineImpl();
    }

    /**
     * Creates new AnuualDiscFinEntities.
     *
     * @param person id
     */
    public AnnualDiscFinEntitiesBean(String personId){
        this();
        this.personId = personId;
    }

    /**
     * Sets the person id
     *
     * @param person id
     */
    public void setPersonId(String personId){
        this.personId = personId;
    }

    /**
     * Gets the person id
     *
     * @return person id
     */
    public String getPersonId(){
        return personId;
    }

    /**
     * The method used to get all pending financial entity disclosure
     * details of a particular person for Annual Disclosure.
     * This method will throw an exception if the person id for this bean is
     * null.
     * <li>To fetch the data, it uses dw_get_annual_disc_entities procedure.
     *
     * @return Vector collection of <code>EntityDetailsBean</code>
     * instances
     *
     * @exception DBException
     * @exception CoeusException
     */
    public Vector getAnnualDiscEntities() throws DBException,CoeusException{
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        Vector finEntities = new Vector(3,2);
        param.addElement(new Parameter("PERSON_ID","String",personId.toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_annual_disc_entities ( <<PERSON_ID>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int cntFinEntities=0;cntFinEntities<result.size();cntFinEntities++){
            HashMap PendingFinEntities = (HashMap)result.elementAt(cntFinEntities);
            EntityDetailsBean entityDetails = new EntityDetailsBean();
            entityDetails.setName((String)PendingFinEntities.get("ENTITY_NAME"));
            entityDetails.setNumber((String)PendingFinEntities.get("ENTITY_NUMBER"));
            /* CASE #410 Begin */
            entityDetails.setAnnDisclUpdated((String)PendingFinEntities.get("UPDATED"));
            /* CASE #410 End */
            /* CASE #410 Comment Begin */
            /*entityDetails.setLastUpdate(
                    (java.sql.Timestamp)PendingFinEntities.get("UPDATE_TIMESTAMP"));
            entityDetails.setStatus(PendingFinEntities.get("STATUS_CODE").toString());
            entityDetails.setSeqNumber(PendingFinEntities.get("SEQUENCE_NUMBER").toString());
            */
            /* CASE #410 Comment End */
            finEntities.add(entityDetails);
        }
        return finEntities;
    }

    /* CASE #912 Begin */
    /**
     * Check if this person has one or more active financial entities disclosed.
     * Call fn_check_person_has_active_fe.
     * @return Does person have active financial entities?
     * @throws DBException
     */
    public boolean checkPersonHasActiveFE() throws DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        String retCode = null;
        //calling stored function
        if(dbEngine!=null){
            param.addElement(new Parameter("AW_PERSON_ID","String",personId));
            StringBuffer strBuffSql = new StringBuffer("");
            strBuffSql.append("{ << OUT STRING RET_CODE >> = ");
            strBuffSql.append("call fn_check_person_has_active_fe ( ");
            strBuffSql.append("<< AW_PERSON_ID >> ) }");
            result = dbEngine.executeFunctions("Coeus", strBuffSql.toString(),
                param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if(result!=null && !result.isEmpty()){
            HashMap disclCheck = (HashMap)result.elementAt(0);
            retCode = disclCheck.get("RET_CODE").toString();
        }
        if(Integer.parseInt(retCode) != 1){
            return false;
        }
        return true;
    }
    /* CASE #912 End */

}