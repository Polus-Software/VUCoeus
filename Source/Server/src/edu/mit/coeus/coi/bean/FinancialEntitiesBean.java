/*
 * @(#)FinancialEntitiesBean.java 1.0 3/27/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.Hashtable;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import java.util.LinkedList;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.UtilFactory;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */
/**
 * This class holds all the entities of a user using the EntityDetailsBean bean.
 *
 * @version 1.0 March 27, 2002, 5:58 PM
 * @author  Anil Nandakumar
 */
public class FinancialEntitiesBean {
    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    /*
     *  Initialize bean with a particular person id. ie, All methods
     *  in this class always belong to a person.
     */
    private String personId;
    /**
     * Creates new FinancialEntitiesBean.
     * @param String person id
     */
    public FinancialEntitiesBean(String personId){
        this.personId = personId;
        //dbEngine = DBEngineImpl.getInstance();
        dbEngine = new DBEngineImpl();
    }

    /**
     *  This method is used to get all financial entity disclosure details for a particular person.
     *  This method will throw an exception if the person id for this bean is null.
     *  To fetch the data, it uses dw_get_person_fin_int_disc procedure.
     *  @return Vector vector of EntityDetailsBean instance for a person
     *  @exception DBException
     *  @exception CoeusException
     */

    public Vector getFinancialEntities() throws DBException,CoeusException{
        if(personId==null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        Vector financialEntities = new Vector(3,2);
        param.addElement(new Parameter("PERSON_ID","String",personId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                "call get_person_fin_int_disc ( <<PERSON_ID>> , <<OUT RESULTSET rset>> )",
                "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int k=0;k<result.size();k++){
            /*case #748 comment begin */
            //Hashtable financialEntityRow = (Hashtable)result.elementAt(k);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap financialEntityRow = (HashMap)result.elementAt(k);
            /* case #748 end */
            EntityDetailsBean entityDetails = new EntityDetailsBean();
            entityDetails.setPersonReType
              ((String)financialEntityRow.get("REL_TYPE_DESCRIPTION"));
            entityDetails.setLastUpdate
              ((Timestamp)financialEntityRow.get("UPDATE_TIMESTAMP"));
            entityDetails.setName((String)financialEntityRow.get("ENTITY_NAME"));
            entityDetails.setStatus(financialEntityRow.get("STATUS_CODE").toString());
            entityDetails.setNumber((String)financialEntityRow.get("ENTITY_NUMBER"));
            entityDetails.setSeqNumber(financialEntityRow.get("SEQUENCE_NUMBER").toString());
            financialEntities.add(entityDetails);
        }
        return financialEntities;
    }
}