/*
 * @(#)DisclosureDetailsBean.java 1.0 3/26/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;

import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
/**
 *
 * This class provides the methods for performing all the procedure executions for
 * a COIDisclosure. Various methods are used to fetch the COI Disclosure information
 * from the Database. All methods are used DBEngineImpl singleton instance for the database interaction.
 *
 * @version 1.0 March 26, 2002, 10:08 AM
 * @author  Geo Thomas
 */
public class PendingDisclosuresBean {
    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;

    /**
     * No argument constructor.
     */
    public PendingDisclosuresBean(){
        dbEngine = new DBEngineImpl();
    }

    /**
     *  This method is used to get disclosures details for a particular person.
     *  It will throw an exception if the person id is not attached with this bean.
     *  <li>To fetch the data, it uses dw_get_coi_disclheader procedure.
     *  @param String COI DisclosureNumber
     *  @return Vector Vector of DisclosureHeaderBean objects
     *  @exception DBException
     *  @exception CoeusException
     *
     */
    public Vector getDiscForTempProp() throws DBException, CoeusException{
        Vector result = new Vector();
        Vector param= new Vector();
        Vector disclosures = new Vector();
        DisclosureHeaderBean disclHeader = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                "call get_disc_for_temp_prop ( <<OUT RESULTSET rset>> )   ",
                "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int rowCount=0; rowCount<result.size(); rowCount++){
            HashMap disclRow = (HashMap)result.elementAt(rowCount);
            disclHeader = new DisclosureHeaderBean();
            disclHeader.setName((String)disclRow.get("PI_NAME"));
            disclHeader.setDisclosureNo((String)disclRow.get("DISCLNO"));
            disclHeader.setPersonId((String)disclRow.get("PI_ID"));
            disclHeader.setKeyNumber((String)disclRow.get("MODULE_ITEM_KEY"));
            disclHeader.setDisclType((String)disclRow.get("DISCLOSURE_TYPE"));
            disclHeader.setTitle((String)disclRow.get("TITLE"));
            disclHeader.setSponsor((String)disclRow.get("SPONSOR_NAME"));
            disclHeader.setUpdatedDate((java.sql.Timestamp)disclRow.get("UPDATE_TIMESTAMP"));
            disclHeader.setUpdateUser((String)disclRow.get("UPDATE_USER"));

            disclosures.add(disclHeader);
        }
        return disclosures;
    }
  }