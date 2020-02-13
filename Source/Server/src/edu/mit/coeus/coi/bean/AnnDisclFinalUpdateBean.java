/*
 * @(#)AnnDisclFinalUpdateBean.java 1.0 6/9/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import java.sql.SQLException;
import java.util.Vector;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
/* CASE #734 End */

/**
 * <code>AnnDisclFinalUpdateBean</code> is a class to update all the Pending
 * annual disclosures for a person in the <code>coeus</code> database.
 *
 * @version 1.0 June 9,2002
 * @author Phaneendra Kumar.
 */
public class AnnDisclFinalUpdateBean {

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
     * Creates new AnnDisclFinalUpdateBean.
     *
     * @param String person id
     */
    public AnnDisclFinalUpdateBean(String personId ){
        this.personId = personId;
        dbEngine = new DBEngineImpl();
    }

    /**
     *  The method used to update the status to <code>PI Reviewed</code> for
     *  all pending annual disclosures for a person.  The method will
     *  return a collection with exactly one instance of
     *  <code>AnnDisclosureErrorBean</code>, with entity
     *  number attribute of <code>AnnDisclosureErrorBean</code> set to 1 for
     *  successful update, or 0 or an entity number for error cases.
     *  The method checks whether
     *  all the records for disclosures with status Pending Annual Disclosure
     *  have got the status of either <code>No Confilct</code> or
     *  <code>PI Identified Conflict</code>. If yes, it will update the status
     *  of those records to PI Reviewed and entity number of AnnDisclosureErrorBean
     *  will be 1.  Else if there are no records with status Pending
     *  Annual Disclosure, entity number will be 0.  Else if there
     *  are records with status Pending Annual Disclosure
     *  where status of one or more finanical entity is not <code>No Conflict</code>
     *  or <code>PI Identified Conflict </code>, entity number will be the first
     *  financial entity number (alphabetical by entity name)
     *  for which the error condition exists.
     *  The method will throw an exception if the person id for this bean is
     *  null.
     *
     *  @return a collection, containing one instance of <code>AnnDisclosureErrorBean</code>.
     *  @exception DBException
     *  @exception CoeusException
     */
    public Vector updateAllAnnualDisclosures(String updateUser) throws DBException, CoeusException{
        if(personId == null){
            throw new CoeusException("exceptionCode.20001");
        }
        Vector result = new Vector(3,2);
        Vector parameters= new java.util.Vector();
        Vector errors =  new java.util.Vector();
        parameters.addElement(new Parameter("PERSON_ID", "String", personId.trim()));
        /* CASE #406 Begin */
        parameters.addElement(new Parameter("UPDATE_USER", "String", updateUser));
        /* CASE #406 End */
        if(dbEngine!=null){
            /* CASE #406 Comment Begin */
            //result = dbEngine.executeRequest("Coeus", "call finalize_annual_disclosure( "+
                    //"<<PERSON_ID>> , <<OUT RESULTSET rset>> )", "Coeus", parameters);
            /* CASE #406 Comment Edn */
            /* CASE #406 Begin */
            result = dbEngine.executeFunctions("Coeus",
              "{ <<OUT STRING ENTITY_NUMBER >> = call fn_finalize_annual_disclosure ( <<PERSON_ID>>, <<UPDATE_USER>> ) }",
              parameters);
            /* CASE #406 End */

        }else{
            throw new DBException("exceptionCode.10001");
        }

        if (result != null) {
            for(int cntResult=0;cntResult<result.size();cntResult++){
                AnnDisclosureErrorBean  annualDisclError = new AnnDisclosureErrorBean();
                /* case #748 comment begin */
                //Hashtable disclosuresInfo = (Hashtable)result.elementAt(cntResult);
                /* case #748 comment end */
                /* case #748 begin */
                HashMap disclosuresInfo = (HashMap)result.elementAt(cntResult);
                /* case #748 end */
                int entityNumber = Integer.parseInt(disclosuresInfo.get("ENTITY_NUMBER").toString());
                if ( entityNumber == 0 ) {
                    annualDisclError.setEntityNumber(disclosuresInfo.get("ENTITY_NUMBER").toString());
                    annualDisclError.setErrorMessage("Annual Disclosures complete.");
                    errors.add(annualDisclError);
                }
                /* CASE #406 Begin */
                else if ( entityNumber == 1 ) {
                    annualDisclError.setEntityNumber(disclosuresInfo.get("ENTITY_NUMBER").toString());
                    annualDisclError.setErrorMessage("No Error");
                    errors.add(annualDisclError);
                }
                /* CASE #406 End */
                else {
                    annualDisclError.setEntityNumber(disclosuresInfo.get("ENTITY_NUMBER").toString());
                    /* CASE #406 Comment Begin */
                    //annualDisclError.setEntityName(disclosuresInfo.get("ENTITY_NAME").toString());
                    /* CASE #406 Comment End */
                    annualDisclError.setErrorMessage("Annual Disclosures not complete.");
                    errors.add(annualDisclError);
                }
            }
        }
        /* CASE #406 Comment Begin */
        /*
        else {
            AnnDisclosureErrorBean  annualDisclError = new AnnDisclosureErrorBean();
            annualDisclError.setEntityNumber("0");
            annualDisclError.setErrorMessage("Annual Disclosures complete");
            errors.add(annualDisclError);
        }*/
        /* CASE #406 Comment End */
        return errors;
    }

    /* CASE #406 Begin */
    /**
     *  The method used to check whether a person's annual disclosures are
     *  ready to be updated before the user is shown the page with the submit
     *  annual disclosures button.  The method will
     *  return a collection with exactly one instance of
     *  <code>AnnDisclosureErrorBean</code>, with entity
     *  number attribute of <code>AnnDisclosureErrorBean</code> set to
     *  1 for success or 0 or an entity number for error cases.
     *  The method checks whether
     *  all the records for disclosures with status Pending Annual Disclosure
     *  have got the status of either <code>No Confilct</code> or
     *  <code>PI Identified Conflict</code>. If yes, entity number will be
     *  set to 1.  Else if
     *  there are no records with status Pending Annual Disclosure, entity number will
     *  be set to 0.  Else if there are records with status Pending Annual Disclosure
     *  where status of one or more finanical entity is not <code>No Conflict</code>
     *  or <code>PI Identified Conflict </code>, entity number will be the first
     *  financial entity number (alphabetical by entity name)
     *  for which the error condition exists.
     *  The method will throw an exception if the person id for this bean is
     *  null.
     *
     *  @return a collection, which has one instance of <code>AnnDisclosureErrorBean</code>.
     *  @exception DBException
     *  @exception CoeusException
     */
    public Vector checkAnnualDisclComplete() throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector parameters = new Vector(3,2);
        Vector errors = new Vector(3,2);
        parameters.addElement(new Parameter("PERSON_ID","String",personId.trim()));
        if(dbEngine!=null){
            /* CASE #406 Comment Begin */
            //result = dbEngine.executeRequest("Coeus", "call fn_check_ann_discl_complete ( "+
                    //"<<PERSON_ID>> , <<OUT RESULTSET rset>> )", "Coeus", parameters);
            /* CASE #406 Comment End */
            /* CASE #406 Begin */
            result = dbEngine.executeFunctions("Coeus",
              "{ <<OUT STRING ENTITY_NUMBER >> = call fn_check_ann_discl_complete ( <<PERSON_ID>> ) }",
              parameters);
            /* CASE #406 End */
        }else{
            throw new DBException("exceptionCode.10001");
        }
        if (result != null) {
            for(int cntResult=0;cntResult<result.size();cntResult++){
                AnnDisclosureErrorBean  annualDisclError = new AnnDisclosureErrorBean();
                /* case #748 comment begin */
                //Hashtable disclosuresInfo = (Hashtable)result.elementAt(cntResult);
                /* case #748 comment end */
                /* case #748 begin */
                HashMap disclosuresInfo = (HashMap)result.elementAt(cntResult);
                /* case #748 end */
                int entityNumber = Integer.parseInt(disclosuresInfo.get("ENTITY_NUMBER").toString());
                if( entityNumber == 1){
                    annualDisclError.setEntityNumber(disclosuresInfo.get("ENTITY_NUMBER").toString());
                    annualDisclError.setErrorMessage("No Errors");
                    errors.add(annualDisclError);
                }
                else if ( entityNumber == 0 ) {
                    annualDisclError.setEntityNumber(disclosuresInfo.get("ENTITY_NUMBER").toString());
                    annualDisclError.setErrorMessage("Annual Disclosures complete.");
                    errors.add(annualDisclError);
                }else {
                    //annualDisclError.setDisclosureNumber
                        //((String)disclosuresInfo.get("COI_DISCLOSURE_NUMBER"));
                    annualDisclError.setEntityNumber(disclosuresInfo.get("ENTITY_NUMBER").toString());
                    annualDisclError.setErrorMessage("Annual Disclosures not complete.");
                    errors.add(annualDisclError);
                }
            }
        }
        return errors;
    }
    /* CASE #406 End */
}