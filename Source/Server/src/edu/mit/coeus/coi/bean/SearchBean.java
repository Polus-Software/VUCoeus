/*
 * @(#)SearchBean.java 1.0 3/25/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.coi.bean;

import java.util.Vector;
import java.util.Hashtable;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import java.util.LinkedList;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.bean.PersonInfoBean;
/* CASE #734 Comment Begin */
//import edu.mit.coeus.coi.exception.CoeusException;
/* CASE #734 Comment End */
/* CASE #734 Begin */
import edu.mit.coeus.exception.CoeusException;
import java.math.BigDecimal;
/* CASE #734 End */

/**
 *
 * This class is for doing different searches for COI module. All search method will return
 * a vector which has a collection bean instances for that particular search result.
 *
 * @version 1.0 March 25, 2002, 12:08 PM
 * @author  Geo Thomas
 */
public class SearchBean {

        /*
         *  Singleton instance of a dbEngine
         */
    private DBEngineImpl dbEngine;

        /*
         *  Initialize search bean with a particular person id. ie, All search methods
         *  in this class are always belongs to a person.
         */
    private String personId;

//    private UtilFactory UtilFactory;

    /** Creates new SearchBean.
     * Contructor with one argument
     * @param String person id
     */
    public SearchBean( String personId ){
//        UtilFactory = new UtilFactory();
        this.personId = personId;
        //dbEngine = DBEngineImpl.getInstance();
        dbEngine = new DBEngineImpl();
    }

    /**
     *  This method is used to get all disclosures details for any particular search criterea.
     *  This method will also throw an exception if the person id for this bean is null.
     *  To fetch the data, it uses dw_get_coi_list_for_query procedure.
     *  @param String statusCode
     *  @param String appliesTo
     *  @param String awaPropNumber
     *  @param String type
     *  @return vector
     *  @exception DBException
     */

    public Vector getCOIDisclosures( String personId,String personName,
                                        String statusCode , String appliesTo ,
                                        String awaPropNumber , String type )
                    throws DBException{
        Vector result = new Vector( 3 , 2 );
        Vector coiDisclosures = new Vector( 3 , 2 );
        StringBuffer searchQry = new StringBuffer( "" );
        StringBuffer searchQry2 = new StringBuffer( "" );

        // ************** get records - award ***************************
        //select clause
        searchQry.append( "SELECT DISCL.COI_DISCLOSURE_NUMBER DISCLNO, ");
        searchQry.append( " DISCL.COI_DISCLOSURE_NUMBER || '(' || DISCL.DISCLOSURE_TYPE || ')' COI_DISCLOSURE_NUMBER, ");
        searchQry.append( " DISCL.MODULE_ITEM_KEY, DISCL.MODULE_CODE, " );
        searchQry.append( " MODULE.DESCRIPTION AS Module, DISCL.PERSON_ID, ");
        searchQry.append( " DECODE(DISCL.DISCLOSURE_TYPE, 'I', 'Initial', 'A', 'Annual')  AS DISCLOSURE_TYPE,  ");
        searchQry.append( " DISCL.UPDATE_TIMESTAMP, ");
        searchQry.append( " DISCL.UPDATE_USER, SC.COI_DISCLOSURE_STATUS_CODE, ");
        searchQry.append( " STATUS.DESCRIPTION AS COI_STATUS, SC.COI_REVIEWER_CODE, REV.DESCRIPTION AS REVIEWER, " );
        searchQry.append( " SPDETAIL.TITLE AS TITLE, SPDETAIL.SPONSOR_NAME AS SPONSOR_NAME, " );
        searchQry.append( " DECODE(SPDETAIL.SPONSOR_CODE,  null, ' ', SPONSOR_CODE) || ' - '  || SPDETAIL.SPONSOR_NAME SPONSOR" );

        //from clause
        searchQry.append( " FROM OSP$INV_COI_DISCLOSURE DISCL, OSP$COEUS_MODULE MODULE,   ");
        searchQry.append( "OSP$INV_COI_DISC_STATUS_CHANGE SC, 	" );
        if( personName != null && !personName.equals( "" ) ) {
            searchQry.append( "OSP$PERSON PERSON, ");
        }
        searchQry.append( " OSP$COI_DISCLOSURE_STATUS STATUS, OSP$COI_REVIEWER REV, " );
        searchQry.append( " (select OSP$AWARD_HEADER.MIT_AWARD_NUMBER MIT_AWARD_NUMBER, ");
        searchQry.append( " OSP$AWARD_HEADER.TITLE  title,osp$sponsor.sponsor_code sponsor_code, ");
        searchQry.append( " osp$sponsor.sponsor_name sponsor_name ");
        searchQry.append( " from OSP$AWARD_HEADER,osp$award,osp$award_status, osp$sponsor ");
        searchQry.append( " where OSP$AWARD_HEADER.SEQUENCE_NUMBER IN ");
        searchQry.append( "      ( SELECT MAX(A.SEQUENCE_NUMBER) ");
        searchQry.append( "             FROM OSP$AWARD_HEADER A ");
        searchQry.append( "            WHERE A.MIT_AWARD_NUMBER = OSP$AWARD_HEADER.MIT_AWARD_NUMBER) ");
        searchQry.append( " and OSP$AWARD.MIT_AWARD_NUMBER = OSP$AWARD_HEADER.MIT_AWARD_NUMBER ");
        searchQry.append( " and OSP$AWARD.SEQUENCE_NUMBER = OSP$AWARD_HEADER.SEQUENCE_NUMBER ");
        searchQry.append( " and osp$award_status.status_code = osp$award.status_code ");
        searchQry.append( " AND osp$award.sponsor_code = osp$sponsor.sponsor_code) SPDETAIL ");

        //where clause
        searchQry.append( " where discl.coi_disclosure_number = sc.coi_disclosure_number   " );
        searchQry.append( " and (sc.sequence_number = (select max(sequence_number) " );
        searchQry.append( " from osp$inv_coi_disc_status_change sc2  ");
        searchQry.append( " where sc2.coi_disclosure_number = sc.coi_disclosure_number) )  " );
        //JIRA COEUSQA-1569 - START 1
        searchQry.append("and to_number(discl.coi_disclosure_number) = " +
                " (select max(to_number(discl2.coi_disclosure_number)) " +
                " from OSP$INV_COI_DISCLOSURE discl2 " +
                " where discl2.module_code = 1 and " +
                " discl.module_item_key = discl2.module_item_key and " +
                " discl.person_id = discl2.person_id) ");
        //JIRA COEUSQA-1569 - END 1

        /* CASE #1374 Begin */
        searchQry.append( " and discl.module_code = 1");
        /* CASE #1374 End */
        searchQry.append( " and discl.module_code = module.module_code  " );
        searchQry.append( " AND SC.COI_DISCLOSURE_STATUS_CODE = STATUS.COI_DISCLOSURE_STATUS_CODE " );
        searchQry.append( " AND SC.COI_REVIEWER_CODE = REV.COI_REVIEWER_CODE " );

        if( personId != null && !personId.equals( "" ) ) {
            searchQry.append( " AND DISCL.PERSON_ID = " + UtilFactory.checkNull( personId ) );
        }
        if( personName != null && !personName.equals( "" ) ) {
            searchQry.append( " AND PERSON.PERSON_ID = DISCL.PERSON_ID " );
            searchQry.append( " AND UPPER(PERSON.FULL_NAME) like "+
                        UtilFactory.checkNull("%"+personName+"%"));
        }
        if( type != null && !type.equals( "" ) && !type.equalsIgnoreCase( "all" ) ) {
            searchQry.append( " AND UPPER(DISCL.DISCLOSURE_TYPE) =" + UtilFactory.checkNull( type ) );
        }
        if( appliesTo != null && !appliesTo.equals( "" ) && !appliesTo.equalsIgnoreCase( "all" ) ) {
            searchQry.append( " AND DISCL.MODULE_CODE =" + UtilFactory.checkNull( appliesTo ) );
        }
        if( awaPropNumber != null && !awaPropNumber.equals( "" ) && !awaPropNumber.equalsIgnoreCase( "all" ) ) {
            searchQry.append( " AND DISCL.MODULE_ITEM_KEY =" + UtilFactory.checkNull( awaPropNumber ) );
        }
        if( statusCode != null && !statusCode.equals( "" ) && !statusCode.equalsIgnoreCase( "all" ) ) {
            searchQry.append( " AND SC.COI_DISCLOSURE_STATUS_CODE =" + statusCode );
        }
        searchQry.append( " AND DISCL.MODULE_ITEM_KEY = SPDETAIL.MIT_AWARD_NUMBER " );


        searchQry2.append( " UNION " );

        // ************** get records - proposal ***************************
        //select clause
        searchQry2.append( " SELECT DISCL.COI_DISCLOSURE_NUMBER DISCLNO, DISCL.COI_DISCLOSURE_NUMBER || '(' || DISCL.DISCLOSURE_TYPE || ')' COI_DISCLOSURE_NUMBER, DISCL.MODULE_ITEM_KEY, DISCL.MODULE_CODE, " );
        searchQry2.append( " MODULE.DESCRIPTION AS Module, DISCL.PERSON_ID, DECODE(DISCL.DISCLOSURE_TYPE, 'I', ");
        searchQry2.append( "'Initial', 'A', 'Annual')  AS DISCLOSURE_TYPE,  DISCL.UPDATE_TIMESTAMP, ");
        searchQry2.append( " DISCL.UPDATE_USER, SC.COI_DISCLOSURE_STATUS_CODE, ");
        searchQry2.append( " STATUS.DESCRIPTION AS COI_STATUS, SC.COI_REVIEWER_CODE, REV.DESCRIPTION AS REVIEWER, " );
        searchQry2.append( " SPDETAIL.TITLE AS TITLE, SPDETAIL.SPONSOR_NAME AS SPONSOR_NAME, " );
        searchQry2.append( " DECODE(SPDETAIL.SPONSOR_CODE,  null, ' ', SPONSOR_CODE) || ' - '  || SPDETAIL.SPONSOR_NAME SPONSOR" );

        //from clause
        searchQry2.append( " FROM OSP$INV_COI_DISCLOSURE DISCL, OSP$COEUS_MODULE MODULE,   OSP$INV_COI_DISC_STATUS_CHANGE SC, 	" );
        if( personName != null && !personName.equals( "" ) ) {
            searchQry2.append( "OSP$PERSON PERSON, ");
        }
        searchQry2.append( " OSP$COI_DISCLOSURE_STATUS STATUS, OSP$COI_REVIEWER REV, " );
        searchQry2.append( " osp$proposal_log SPDETAIL ");

        //where clause
        searchQry2.append( " where discl.coi_disclosure_number = sc.coi_disclosure_number   " );
        searchQry2.append( " AND  DISCL.MODULE_CODE = 2    " );
        searchQry2.append( " and (sc.sequence_number = (select max(sequence_number) " );
        searchQry2.append( " from osp$inv_coi_disc_status_change sc2  where sc2.coi_disclosure_number = sc.coi_disclosure_number) )  " );
        searchQry2.append( " and discl.module_code = module.module_code  " );
        searchQry2.append( " AND SC.COI_DISCLOSURE_STATUS_CODE = STATUS.COI_DISCLOSURE_STATUS_CODE " );
        searchQry2.append( " AND SC.COI_REVIEWER_CODE = REV.COI_REVIEWER_CODE " );
        searchQry2.append( " and substr(DISCL.MODULE_ITEM_KEY, 1, 1) = 'D' " );
        if( personId != null && !personId.equals( "" ) ) {
            searchQry2.append( " AND DISCL.PERSON_ID = " + UtilFactory.checkNull( personId ) );
        }
        if( personName != null && !personName.equals( "" ) ) {
            searchQry2.append( " AND PERSON.PERSON_ID = DISCL.PERSON_ID " );
            searchQry2.append( " AND UPPER(PERSON.FULL_NAME) like "+
                        UtilFactory.checkNull("%"+personName+"%"));
        }
        if( type != null && !type.equals( "" ) && !type.equalsIgnoreCase( "all" ) ) {
            searchQry2.append( " AND UPPER(DISCL.DISCLOSURE_TYPE) =" + UtilFactory.checkNull( type ) );
        }
        if( appliesTo != null && !appliesTo.equals( "" ) && !appliesTo.equalsIgnoreCase( "all" ) ) {
            searchQry2.append( " AND DISCL.MODULE_CODE =" + UtilFactory.checkNull( appliesTo ) );
        }
        if( awaPropNumber != null && !awaPropNumber.equals( "" ) && !awaPropNumber.equalsIgnoreCase( "all" ) ) {
            searchQry2.append( " AND DISCL.MODULE_ITEM_KEY =" + UtilFactory.checkNull( awaPropNumber ) );
        }
        if( statusCode != null && !statusCode.equals( "" ) && !statusCode.equalsIgnoreCase( "all" ) ) {
            searchQry2.append( " AND SC.COI_DISCLOSURE_STATUS_CODE =" + statusCode );
        }
        searchQry2.append( " AND DISCL.MODULE_ITEM_KEY = SPDETAIL.PROPOSAL_NUMBER " );

		searchQry2.append( " UNION " );

        // ************** get records - Institute Proposal ***************************
        //select clause
        searchQry2.append( " SELECT DISCL.COI_DISCLOSURE_NUMBER DISCLNO, DISCL.COI_DISCLOSURE_NUMBER || '(' || DISCL.DISCLOSURE_TYPE || ')' COI_DISCLOSURE_NUMBER, DISCL.MODULE_ITEM_KEY, DISCL.MODULE_CODE, " );
        searchQry2.append( " MODULE.DESCRIPTION AS Module, DISCL.PERSON_ID, DECODE(DISCL.DISCLOSURE_TYPE, 'I', ");
        searchQry2.append( "'Initial', 'A', 'Annual')  AS DISCLOSURE_TYPE,  DISCL.UPDATE_TIMESTAMP, ");
        searchQry2.append( " DISCL.UPDATE_USER, SC.COI_DISCLOSURE_STATUS_CODE, ");
        searchQry2.append( " STATUS.DESCRIPTION AS COI_STATUS, SC.COI_REVIEWER_CODE, REV.DESCRIPTION AS REVIEWER, " );
        searchQry2.append( " PROPOSAL.TITLE AS TITLE, SP.SPONSOR_NAME AS SPONSOR_NAME, " );
        searchQry2.append( " SP.SPONSOR_CODE || ' - '  || SP.SPONSOR_NAME SPONSOR" );

        //from clause
        searchQry2.append( " FROM OSP$INV_COI_DISCLOSURE DISCL, OSP$COEUS_MODULE MODULE,   OSP$INV_COI_DISC_STATUS_CHANGE SC, 	" );
        if( personName != null && !personName.equals( "" ) ) {
            searchQry2.append( "OSP$PERSON PERSON, ");
        }
        searchQry2.append( " OSP$COI_DISCLOSURE_STATUS STATUS, OSP$COI_REVIEWER REV, " );
        searchQry2.append( " osp$proposal proposal, OSP$SPONSOR SP ");

        //where clause
        searchQry2.append( " where discl.coi_disclosure_number = sc.coi_disclosure_number   " );
        searchQry2.append( " AND  DISCL.MODULE_CODE = 2    " );
        searchQry2.append( " and (sc.sequence_number = (select max(sequence_number) " );
        searchQry2.append( " from osp$inv_coi_disc_status_change sc2  where sc2.coi_disclosure_number = sc.coi_disclosure_number) )  " );
        //JIRA COEUSQA-1569 - START 2
        searchQry2.append(" and to_number(discl.coi_disclosure_number) = " +
                " (select max(to_number(discl2.coi_disclosure_number)) " +
                " from OSP$INV_COI_DISCLOSURE discl2 " +
                " where discl2.module_code = 2 and " +
                " discl.module_item_key = discl2.module_item_key and " +
                " discl.person_id = discl2.person_id) ");
        //JIRA COEUSQA-1569 - END 2
        searchQry2.append( " and discl.module_code = module.module_code  " );
        searchQry2.append( " AND SC.COI_DISCLOSURE_STATUS_CODE = STATUS.COI_DISCLOSURE_STATUS_CODE " );
        searchQry2.append( " AND SC.COI_REVIEWER_CODE = REV.COI_REVIEWER_CODE " );
        if( personId != null && !personId.equals( "" ) ) {
            searchQry2.append( " AND DISCL.PERSON_ID = " + UtilFactory.checkNull( personId ) );
        }
        if( personName != null && !personName.equals( "" ) ) {
            searchQry2.append( " AND PERSON.PERSON_ID = DISCL.PERSON_ID " );
            searchQry2.append( " AND UPPER(PERSON.FULL_NAME) like "+
                        UtilFactory.checkNull("%"+personName+"%"));
        }
        if( type != null && !type.equals( "" ) && !type.equalsIgnoreCase( "all" ) ) {
            searchQry2.append( " AND UPPER(DISCL.DISCLOSURE_TYPE) =" + UtilFactory.checkNull( type ) );
        }
        if( appliesTo != null && !appliesTo.equals( "" ) && !appliesTo.equalsIgnoreCase( "all" ) ) {
            searchQry2.append( " AND DISCL.MODULE_CODE =" + UtilFactory.checkNull( appliesTo ) );
        }
        if( awaPropNumber != null && !awaPropNumber.equals( "" ) && !awaPropNumber.equalsIgnoreCase( "all" ) ) {
            searchQry2.append( " AND DISCL.MODULE_ITEM_KEY =" + UtilFactory.checkNull( awaPropNumber ) );
        }
        if( statusCode != null && !statusCode.equals( "" ) && !statusCode.equalsIgnoreCase( "all" ) ) {
            searchQry2.append( " AND SC.COI_DISCLOSURE_STATUS_CODE =" + statusCode );
        }
        searchQry2.append( " AND DISCL.MODULE_ITEM_KEY = PROPOSAL.PROPOSAL_NUMBER " );
        searchQry2.append( " AND PROPOSAL.SEQUENCE_NUMBER = (SELECT MAX(P2.SEQUENCE_NUMBER) " );
        searchQry2.append( "   FROM OSP$PROPOSAL P2 " );
        searchQry2.append( "   WHERE PROPOSAL.PROPOSAL_NUMBER = P2.PROPOSAL_NUMBER) " );
        searchQry2.append( " AND PROPOSAL.SPONSOR_CODE = SP.SPONSOR_CODE " );

        //searchQry.append( " ORDER BY COI_DISCLOSURE_STATUS_CODE " );
        /* CASE #306:  Make the order descending.  */
        searchQry2.append(" ORDER BY DISCLNO DESC ");

        //System.out.println(searchQry.toString());
        //System.out.println(searchQry2.toString());
        //UtilFactory.log(searchQry.toString(), null, "", "getCOIDislcosures");
        //UtilFactory.log(searchQry2.toString(), null, "", "getCOIDisclosures");        
        Vector param = new java.util.Vector();
        param.addElement( new Parameter( "QUERY" , "String" , searchQry.toString() ) );
        param.addElement( new Parameter( "QUERY2" , "String" , searchQry2.toString() ) );
        if( dbEngine != null ) {
            result = dbEngine.executeRequest( "Coeus" ,
                "call dw_get_coi_list_for_query2 ( <<QUERY>> , <<QUERY2>> , <<OUT RESULTSET rset>> )" ,
                "Coeus" , param );
        } else {
            throw new DBException( "exceptionCode.10001" );
        }
        for( int i = 0 ; i < result.size() ; i++ ) {
            HashMap disclHeaderRow = ( HashMap ) result.elementAt( i );
            DisclosureHeaderBean disclosureHeader = new DisclosureHeaderBean();
            disclosureHeader.setDisclosureNo( (String)disclHeaderRow.get( "DISCLNO" ) );
            disclosureHeader.setStatus( (String)disclHeaderRow.get( "COI_STATUS" ) );
            disclosureHeader.setAppliesTo( (String)disclHeaderRow.get( "MODULE" ) );
            disclosureHeader.setKeyNumber( (String)disclHeaderRow.get( "MODULE_ITEM_KEY" ) );
            disclosureHeader.setDisclType( (String)disclHeaderRow.get( "DISCLOSURE_TYPE" ) );
            disclosureHeader.setReviewer( (String)disclHeaderRow.get( "REVIEWER" ) );
            disclosureHeader.setUpdatedDate
              ( ( java.sql.Timestamp )disclHeaderRow.get( "UPDATE_TIMESTAMP" ) );
            disclosureHeader.setTitle( (String)disclHeaderRow.get( "TITLE" ) );
            disclosureHeader.setSponsor( (String)disclHeaderRow.get( "SPONSOR" ) );
            /* CASE #1400 Begin */
            disclosureHeader.setSponsorName( (String)disclHeaderRow.get( "SPONSOR_NAME" ));
            //System.out.println("sponsorName in SearchBean: "+disclosureHeader.getSponsorName());
            /* CASE #1400 End */
            disclosureHeader.setDisclNo
                      ( (String)disclHeaderRow.get( "COI_DISCLOSURE_NUMBER" ) );
            disclosureHeader.setUpdateUser((String)disclHeaderRow.get( "UPDATE_USER" ));
            disclosureHeader.setModuleCode(((BigDecimal)disclHeaderRow.get( "MODULE_CODE" )).toString());
            coiDisclosures.add( disclosureHeader );
        }
        return coiDisclosures;
    }

    /* CASE #1374 */
    //Method updated to 
    /** This method used for default display of disclosures for Review Existing
     * Disclosures page.  Display only disclosures for Pending Proposals and
     * Current Awards, filtered by only the most recent disclosure for each
     * proposal or award. 
     *
     * @param personId Person ID for person associated with disclosures
     * @return Vector of HashMap objects.  Rows of disclosure info.
     * @throws DBException
     */
    public Vector getInitialCOIDisclosures(String personId) throws DBException{
        Vector result = new Vector( 3 , 2 );
        Vector coiDisclosures = new Vector( 3 , 2 );
        Vector param = new java.util.Vector();
        param.addElement( new Parameter( "AW_PERSON_ID" , 
            DBEngineConstants.TYPE_STRING , personId ) );
        if( dbEngine != null ) {
            result = dbEngine.executeRequest( "Coeus" , 
                "call get_default_discl_for_per ( <<AW_PERSON_ID>> , " +
                "<<OUT RESULTSET rset>> )" , "Coeus" , param );
        } else {
            throw new DBException( "exceptionCode.10001" );
        }
        //System.out.println("result.size(): "+result.size());
        for( int i = 0 ; i < result.size() ; i++ ) {
            HashMap disclHeaderRow = ( HashMap ) result.elementAt( i );
            DisclosureHeaderBean disclosureHeader = new DisclosureHeaderBean();
            disclosureHeader.setDisclosureNo( (String)disclHeaderRow.get( "DISCLNO" ) );
            disclosureHeader.setStatus( (String)disclHeaderRow.get( "COI_STATUS" ) );
            disclosureHeader.setAppliesTo( (String)disclHeaderRow.get( "MODULE" ) );
            disclosureHeader.setKeyNumber( (String)disclHeaderRow.get( "MODULE_ITEM_KEY" ) );
            disclosureHeader.setDisclType( (String)disclHeaderRow.get( "DISCLOSURE_TYPE" ) );
            disclosureHeader.setReviewer( (String)disclHeaderRow.get( "REVIEWER" ) );
            disclosureHeader.setUpdatedDate
              ( ( java.sql.Timestamp )disclHeaderRow.get( "UPDATE_TIMESTAMP" ) );
            disclosureHeader.setTitle( (String)disclHeaderRow.get( "TITLE" ) );
            disclosureHeader.setSponsor( (String)disclHeaderRow.get( "SPONSOR" ) );
            /* CASE #1400 Begin */
            disclosureHeader.setSponsorName( (String)disclHeaderRow.get( "SPONSOR_NAME" ));
            //System.out.println("sponsorName in SearchBean: "+disclosureHeader.getSponsorName());
            /* CASE #1400 End */            
            disclosureHeader.setDisclNo
                      ( (String)disclHeaderRow.get( "COI_DISCLOSURE_NUMBER" ) );
            coiDisclosures.add( disclosureHeader );
        }
        return coiDisclosures;
    }
    /* CASE #1374 End */
    /* CASE #1374 Comment Begin */
    /*public Vector getInitialCOIDisclosures(String personId) throws DBException{
        System.out.println("personId: "+personId);
        Vector result = new Vector( 3 , 2 );
        Vector coiDisclosures = new Vector( 3 , 2 );
        StringBuffer searchQry = new StringBuffer( "" );
        StringBuffer searchQry2 = new StringBuffer( "" );

        // ************** get records - award ***************************
        //select clause for awards
        searchQry.append( "SELECT DISCL.COI_DISCLOSURE_NUMBER DISCLNO, ");
        searchQry.append(" DISCL.COI_DISCLOSURE_NUMBER || '(' || DISCL.DISCLOSURE_TYPE || ')' COI_DISCLOSURE_NUMBER, ");
        searchQry.append(" DISCL.MODULE_ITEM_KEY, DISCL.MODULE_CODE, " );
        searchQry.append( " MODULE.DESCRIPTION AS Module, DISCL.PERSON_ID, DECODE(DISCL.DISCLOSURE_TYPE, 'I', ");
        searchQry.append( "'Initial', 'A', 'Annual')  AS DISCLOSURE_TYPE,  DISCL.UPDATE_TIMESTAMP, ");
        searchQry.append( " DISCL.UPDATE_USER, SC.COI_DISCLOSURE_STATUS_CODE, ");
        searchQry.append( " STATUS.DESCRIPTION AS COI_STATUS, SC.COI_REVIEWER_CODE, REV.DESCRIPTION AS REVIEWER, " );
        searchQry.append( " SPDETAIL.TITLE AS TITLE, SPDETAIL.SPONSOR_NAME AS SPONSOR_NAME, " );
        searchQry.append( " DECODE(SPDETAIL.SPONSOR_CODE,  null, ' ', SPONSOR_CODE) || ' - '  || SPDETAIL.SPONSOR_NAME SPONSOR" );

        //from clause for awards
        searchQry.append( " FROM OSP$INV_COI_DISCLOSURE DISCL, OSP$COEUS_MODULE MODULE,   ");
        searchQry.append( " OSP$INV_COI_DISC_STATUS_CHANGE SC, 	" );
        searchQry.append( " OSP$COI_DISCLOSURE_STATUS STATUS, OSP$COI_REVIEWER REV, " );
        searchQry.append( " (select OSP$AWARD_HEADER.MIT_AWARD_NUMBER MIT_AWARD_NUMBER, ");
        searchQry.append( " OSP$AWARD_HEADER.TITLE  title,osp$sponsor.sponsor_code sponsor_code, ");
        searchQry.append( " osp$sponsor.sponsor_name sponsor_name ");
        searchQry.append( " from OSP$AWARD_HEADER,osp$award,osp$award_status, osp$sponsor ");
        searchQry.append( " where OSP$AWARD_HEADER.SEQUENCE_NUMBER IN ");
        searchQry.append( "      ( SELECT MAX(A.SEQUENCE_NUMBER) ");
        searchQry.append( "             FROM OSP$AWARD_HEADER A ");
        searchQry.append( "            WHERE A.MIT_AWARD_NUMBER = OSP$AWARD_HEADER.MIT_AWARD_NUMBER) ");
        searchQry.append( " and OSP$AWARD.MIT_AWARD_NUMBER = OSP$AWARD_HEADER.MIT_AWARD_NUMBER ");
        searchQry.append( " and OSP$AWARD.SEQUENCE_NUMBER = OSP$AWARD_HEADER.SEQUENCE_NUMBER ");
        /* CASE #1046 Begin 
        searchQry.append( "and osp$award.STATUS_CODE in (1,3,6)" );
        /* CASE #1046 End 
        searchQry.append( " and osp$award_status.status_code = osp$award.status_code ");
        searchQry.append( " AND osp$award.sponsor_code = osp$sponsor.sponsor_code) SPDETAIL ");

        //where clause for awards
        /* CASE #1046 Begin 
        searchQry.append( " where discl.person_id = "+ UtilFactory.checkNull( personId ) );
        /* CASE #1046 End 
        searchQry.append( " and discl.coi_disclosure_number = sc.coi_disclosure_number " );
        /* CASE #1046 Begin 
        searchQry.append( " and discl.module_code = 1 ");
        /* CASE #1046 End 
        searchQry.append( " and (sc.sequence_number = (select max(sequence_number) " );
        searchQry.append( " from osp$inv_coi_disc_status_change sc2  where sc2.coi_disclosure_number = sc.coi_disclosure_number) )  " );
        /* CASE #1046 Begin 
        searchQry.append( " and to_number(discl.coi_disclosure_number) = ");
        searchQry.append(" (select max(to_number(discl2.coi_disclosure_number))");
        searchQry.append(" from OSP$INV_COI_DISCLOSURE discl2 ");
        searchQry.append("  where discl2.module_code = 1 and ");
        searchQry.append(" discl.module_item_key = discl2.module_item_key and ");
        searchQry.append(" discl.person_id = discl2.person_id) ");
        /* CASE #1046 End 
        searchQry.append( " and discl.module_code = module.module_code  " );
        searchQry.append( " AND SC.COI_DISCLOSURE_STATUS_CODE = STATUS.COI_DISCLOSURE_STATUS_CODE " );
        /* CASE #1271 Begin 
        searchQry2.append( " AND SC.COI_DISCLOSURE_STATUS_CODE not in (202) ");
        /* CASE #1271 End 
        searchQry.append( " AND SC.COI_REVIEWER_CODE = REV.COI_REVIEWER_CODE " );
        searchQry.append( " AND DISCL.MODULE_ITEM_KEY = SPDETAIL.MIT_AWARD_NUMBER " );


        searchQry2.append( " UNION " );

        // ************** get records - proposal log***************************
        //select clause for proposal_log
        searchQry2.append( " SELECT DISCL.COI_DISCLOSURE_NUMBER DISCLNO, DISCL.COI_DISCLOSURE_NUMBER || '(' || DISCL.DISCLOSURE_TYPE || ')' COI_DISCLOSURE_NUMBER, DISCL.MODULE_ITEM_KEY, DISCL.MODULE_CODE, " );
        searchQry2.append( " MODULE.DESCRIPTION AS Module, DISCL.PERSON_ID, DECODE(DISCL.DISCLOSURE_TYPE, 'I', ");
        searchQry2.append( "'Initial', 'A', 'Annual')  AS DISCLOSURE_TYPE,  DISCL.UPDATE_TIMESTAMP, ");
        searchQry2.append( " DISCL.UPDATE_USER, SC.COI_DISCLOSURE_STATUS_CODE, ");
        searchQry2.append( " STATUS.DESCRIPTION AS COI_STATUS, SC.COI_REVIEWER_CODE, REV.DESCRIPTION AS REVIEWER, " );
        searchQry2.append( " SPDETAIL.TITLE AS TITLE, SPDETAIL.SPONSOR_NAME AS SPONSOR_NAME, " );
        searchQry2.append( " DECODE(SPDETAIL.SPONSOR_CODE,  null, ' ', SPONSOR_CODE) || ' - '  || SPDETAIL.SPONSOR_NAME SPONSOR" );

        //from clause for proposal_log
        searchQry2.append( " FROM OSP$INV_COI_DISCLOSURE DISCL, OSP$COEUS_MODULE MODULE,   OSP$INV_COI_DISC_STATUS_CHANGE SC, 	" );
        searchQry2.append( " OSP$COI_DISCLOSURE_STATUS STATUS, OSP$COI_REVIEWER REV, " );
        searchQry2.append( " osp$proposal_log SPDETAIL ");

        //where clause for proposal_log
        /* CASE #1046 Begin 
        searchQry2.append( " where discl.person_id = "+ UtilFactory.checkNull( personId ) );
        /* CASE #1046 End 
        searchQry2.append( " and discl.coi_disclosure_number = sc.coi_disclosure_number   " );
        searchQry2.append( " AND  DISCL.MODULE_CODE = 2    " );
        searchQry2.append( " and (sc.sequence_number = (select max(sequence_number) " );
        searchQry2.append( " from osp$inv_coi_disc_status_change sc2  ");
        searchQry2.append( " where sc2.coi_disclosure_number = sc.coi_disclosure_number) )  " );
        searchQry2.append( " and discl.module_code = module.module_code  " );
        searchQry2.append( " AND SC.COI_DISCLOSURE_STATUS_CODE = STATUS.COI_DISCLOSURE_STATUS_CODE " );
        /* CASE #1271 Begin 
        searchQry2.append( " AND SC.COI_DISCLOSURE_STATUS_CODE not in (202) ");
        /* CASE #1271 End 
        searchQry2.append( " AND SC.COI_REVIEWER_CODE = REV.COI_REVIEWER_CODE " );
        searchQry2.append( " and substr(DISCL.MODULE_ITEM_KEY, 1, 1) = 'D' " );

        searchQry2.append( " AND DISCL.MODULE_ITEM_KEY = SPDETAIL.PROPOSAL_NUMBER " );

        searchQry2.append( " UNION " );

        // ************** get records - Institute Proposal ***************************
        //select clause for institute proposal
        searchQry2.append( " SELECT DISCL.COI_DISCLOSURE_NUMBER DISCLNO, ");
        searchQry2.append( " DISCL.COI_DISCLOSURE_NUMBER ");
        searchQry2.append( " || '(' || DISCL.DISCLOSURE_TYPE || ')' COI_DISCLOSURE_NUMBER, ");
        searchQry2.append( " DISCL.MODULE_ITEM_KEY, DISCL.MODULE_CODE, " );
        searchQry2.append( " MODULE.DESCRIPTION AS Module, DISCL.PERSON_ID, DECODE(DISCL.DISCLOSURE_TYPE, 'I', ");
        searchQry2.append( "'Initial', 'A', 'Annual')  AS DISCLOSURE_TYPE,  DISCL.UPDATE_TIMESTAMP, ");
        searchQry2.append( " DISCL.UPDATE_USER, SC.COI_DISCLOSURE_STATUS_CODE, ");
        searchQry2.append( " STATUS.DESCRIPTION AS COI_STATUS, SC.COI_REVIEWER_CODE, REV.DESCRIPTION AS REVIEWER, " );
        searchQry2.append( " PROPOSAL.TITLE AS TITLE, SP.SPONSOR_NAME AS SPONSOR_NAME, " );
        searchQry2.append( " SP.SPONSOR_CODE || ' - '  || SP.SPONSOR_NAME SPONSOR" );

        //from clause for institute proposal
        searchQry2.append( " FROM OSP$INV_COI_DISCLOSURE DISCL, OSP$COEUS_MODULE MODULE,   ");
        searchQry2.append( " OSP$INV_COI_DISC_STATUS_CHANGE SC, 	" );
        searchQry2.append( " OSP$COI_DISCLOSURE_STATUS STATUS, OSP$COI_REVIEWER REV, " );
        searchQry2.append( " osp$proposal proposal, OSP$SPONSOR SP ");

        //where clause for institute proposal
        /* CASE #1046 Begin 
        searchQry2.append( " where discl.person_id = "+ UtilFactory.checkNull( personId ) );
        /* CASE #1046 End 
        searchQry2.append( " and discl.coi_disclosure_number = sc.coi_disclosure_number   " );
        searchQry2.append( " AND  DISCL.MODULE_CODE = 2    " );
        searchQry2.append( " and (sc.sequence_number = (select max(sequence_number) " );
        searchQry2.append( " from osp$inv_coi_disc_status_change sc2  ");
        searchQry2.append( " where sc2.coi_disclosure_number = sc.coi_disclosure_number) )  " );
        /* CASE #1046 Begin 
        searchQry2.append( " and to_number(discl.coi_disclosure_number) = " );
        searchQry2.append( " (select max(to_number(discl2.coi_disclosure_number))" );
        searchQry2.append( "  from OSP$INV_COI_DISCLOSURE discl2 ");
        searchQry2.append( " where discl2.module_code = 2 and ");
        searchQry2.append( " discl.module_item_key = discl2.module_item_key and ");
        searchQry2.append( " discl.person_id = discl2.person_id) " );
        /* CASE #1046 End 
        searchQry2.append( " and discl.module_code = module.module_code  " );
        searchQry2.append( " AND SC.COI_DISCLOSURE_STATUS_CODE = STATUS.COI_DISCLOSURE_STATUS_CODE " );
        /* CASE #1271 Begin 
        searchQry2.append( " AND SC.COI_DISCLOSURE_STATUS_CODE not in (202) ");
        /* CASE #1271 End 
        searchQry2.append( " AND SC.COI_REVIEWER_CODE = REV.COI_REVIEWER_CODE " );
        searchQry2.append( " AND DISCL.MODULE_ITEM_KEY = PROPOSAL.PROPOSAL_NUMBER " );
        searchQry2.append( " AND PROPOSAL.SEQUENCE_NUMBER = (SELECT MAX(P2.SEQUENCE_NUMBER) " );
        searchQry2.append( "   FROM OSP$PROPOSAL P2 " );
        searchQry2.append( "   WHERE PROPOSAL.PROPOSAL_NUMBER = P2.PROPOSAL_NUMBER) " );
        /* CASE #1046 Begin 
        searchQry2.append(" and proposal.status_code = 1 " );
        /* CASE #1046 End 
        searchQry2.append( " AND PROPOSAL.SPONSOR_CODE = SP.SPONSOR_CODE " );

        //searchQry.append( " ORDER BY COI_DISCLOSURE_STATUS_CODE " );
        /* CASE #306:  Make the order descending.  
        searchQry2.append(" ORDER BY DISCLNO DESC ");

        UtilFactory.log(searchQry.toString(), null, "", "getInitialCOIDisclosures()");
        UtilFactory.log(searchQry2.toString(), null, "", "getInitialCOIDisclosures()");
        //System.out.println(searchQry2.toString());
        Vector param = new java.util.Vector();
        param.addElement( new Parameter( "QUERY" , "String" , searchQry.toString() ) );
        param.addElement( new Parameter( "QUERY2" , "String" , searchQry2.toString() ) );
        if( dbEngine != null ) {
            result = dbEngine.executeRequest( "Coeus" , "call dw_get_coi_list_for_query2 ( <<QUERY>> , <<QUERY2>> , <<OUT RESULTSET rset>> )" , "Coeus" , param );
            //result = dbEngine.executeRequest( "Coeus" , "call dw_get_coi_list_for_query ( <<QUERY>> , <<OUT RESULTSET rset>> )" , "Coeus" , param );
        } else {
            throw new DBException( "exceptionCode.10001" );
        }
        //System.out.println("result.size(): "+result.size());
        for( int i = 0 ; i < result.size() ; i++ ) {
            HashMap disclHeaderRow = ( HashMap ) result.elementAt( i );
            DisclosureHeaderBean disclosureHeader = new DisclosureHeaderBean();
            disclosureHeader.setDisclosureNo( (String)disclHeaderRow.get( "DISCLNO" ) );
            disclosureHeader.setStatus( (String)disclHeaderRow.get( "COI_STATUS" ) );
            disclosureHeader.setAppliesTo( (String)disclHeaderRow.get( "MODULE" ) );
            disclosureHeader.setKeyNumber( (String)disclHeaderRow.get( "MODULE_ITEM_KEY" ) );
            disclosureHeader.setDisclType( (String)disclHeaderRow.get( "DISCLOSURE_TYPE" ) );
            disclosureHeader.setReviewer( (String)disclHeaderRow.get( "REVIEWER" ) );
            disclosureHeader.setUpdatedDate
              ( ( java.sql.Timestamp )disclHeaderRow.get( "UPDATE_TIMESTAMP" ) );
            disclosureHeader.setTitle( (String)disclHeaderRow.get( "TITLE" ) );
            disclosureHeader.setSponsor( (String)disclHeaderRow.get( "SPONSOR" ) );
            disclosureHeader.setDisclNo
                      ( (String)disclHeaderRow.get( "COI_DISCLOSURE_NUMBER" ) );
            coiDisclosures.add( disclosureHeader );
        }
        return coiDisclosures;
    }*/
    /* CASE #1046 End */
    /* CASE #1374 Comment End */

    /**
     *  This overloaded method populates the list box meant to retrieve the disclosure status for the disclosure search screen.
     *  This method will also throw an exception if the person id for this bean is null.
     *  To fetch the data, it uses a procedure dw_get_coi_disclosure_status
     *  @return ArrayList
     *  @exception DBException
     *  @exception CoeusException
     *
     *	 @author RYK
     */
    public ArrayList getDisclosureStatus() throws DBException , CoeusException{
        if( personId == null ) {
            throw new CoeusException( "exceptionCode.20001" );
        }
        Vector result = new Vector( 3 , 2 );
        Vector param = new Vector();
        ArrayList disclStats = new ArrayList();
        ComboBoxBean selectAllDiscl = new ComboBoxBean("All", "All");
        disclStats.add(selectAllDiscl);
        if( dbEngine != null ) {
            result = dbEngine.executeRequest( "Coeus" ,
                "call dw_get_coi_disclosure_status ( <<OUT RESULTSET rset>> )" ,
                "Coeus" , param );
        } else {
            throw new DBException( "exceptionCode.10001" );
        }
        for( int i = 0 ; i < result.size() ; i++ ) {
            /* case #748 comment begin */
            //Hashtable disclStatusRow = ( Hashtable ) result.elementAt( i );
            /* case #748 comment end */
            /* case #748 begin */
            HashMap disclStatusRow = ( HashMap ) result.elementAt( i );
            /* case #748 end */
            ComboBoxBean comboBoxBean  = new ComboBoxBean(
                disclStatusRow.get( "DESCRIPTION" ).toString(),
                disclStatusRow.get( "COI_DISCLOSURE_STATUS_CODE" ).toString() );
            disclStats.add( comboBoxBean );
        }
        return disclStats;
    }
    /**
     *  The method used to do the person search. This will make the search query with the
     *  passed parameters. Execute the search query and populate the resultset into a vector
     *  which has instances of PersonInfo Bean.
     *  @ param String last name
     *  @ param String first name
     *  @ param String full name
     *  @ param String user name
     *  @ param String email
     *  @ param String directory title
     *  @ param String office location
     *  @ param String home unit
     *  @ param String unit name
     *  @ return Vector list of <code>PersonInfo</code> bean instances
     *  @ exception
     */
    public Vector getPersonDetails(String lastName,String firstName,String fullName,
                                    String userName,String email,String dirTitle,
                                    String offLocation,String offPhone,String homeUnit,String unitName)
            throws DBException{
        //to store the search result for the person
        Vector personSearchRes = new Vector(3,2);
        StringBuffer searchQry = new StringBuffer( "" );

        searchQry.append("SELECT OSP$PERSON.PERSON_ID,OSP$PERSON.LAST_NAME,OSP$PERSON.FIRST_NAME,");
        searchQry.append("OSP$PERSON.PRIOR_NAME,OSP$PERSON.FULL_NAME,OSP$PERSON.USER_NAME,");
        searchQry.append("OSP$PERSON.EMAIL_ADDRESS,OSP$PERSON.HOME_UNIT,");
        searchQry.append("OSP$UNIT.UNIT_NAME,OSP$PERSON.DIRECTORY_TITLE,");
        searchQry.append("OSP$PERSON.OFFICE_LOCATION,OSP$PERSON.OFFICE_PHONE,");
        searchQry.append("OSP$PERSON.SECONDRY_OFFICE_LOCATION,OSP$PERSON.SECONDRY_OFFICE_PHONE");
        searchQry.append(" FROM OSP$PERSON,OSP$UNIT WHERE OSP$PERSON.HOME_UNIT = OSP$UNIT.UNIT_NUMBER(+)");
        if(lastName!=null && !lastName.equals("")){
            searchQry.append(" AND (UPPER(OSP$PERSON.LAST_NAME) = " + UtilFactory.checkNull(lastName) + ")");
        }
        if(firstName!=null && !firstName.equals("")){
            searchQry.append(" AND (UPPER(OSP$PERSON.FIRST_NAME) = " + UtilFactory.checkNull(firstName) + ")");
        }
        if(fullName!=null && !fullName.equals("")){
            searchQry.append(" AND (UPPER(OSP$PERSON.FULL_NAME) = " + UtilFactory.checkNull(fullName) + ")");
        }
        if(userName!=null && !userName.equals("")){
            searchQry.append(" AND (UPPER(OSP$PERSON.USER_NAME) = " + UtilFactory.checkNull(userName) + ")");
        }
        if(email!=null && !email.equals("")){
            searchQry.append(" AND (UPPER(OSP$PERSON.EMAIL_ADDRESS) = " + UtilFactory.checkNull(email) + ")");
        }
        if(dirTitle!=null && !dirTitle.equals("")){
            searchQry.append(" AND (UPPER(OSP$PERSON.DIRECTORY_TITLE) = " + UtilFactory.checkNull(dirTitle) + ")");
        }
        if(offLocation!=null && !offLocation.equals("")){
            searchQry.append(" AND (UPPER(OSP$PERSON.OFFICE_LOCATION) = " + UtilFactory.checkNull(offLocation) + ")");
        }
        if(offPhone!=null && !offPhone.equals("")){
            searchQry.append(" AND (UPPER(OSP$PERSON.OFFICE_PHONE) = " + UtilFactory.checkNull(offPhone) + ")");
        }
        if(homeUnit!=null && !homeUnit.equals("")){
            searchQry.append(" AND (UPPER(OSP$PERSON.HOME_UNIT) = " + UtilFactory.checkNull(homeUnit) + ")");
            //searchQry.append(" AND OSP$PERSON.HOME_UNIT = OSP$UNIT.UNIT_NUMBER");
        }
        if(unitName!=null && !unitName.equals("")){
            searchQry.append(" AND (UPPER(OSP$UNIT.UNIT_NAME) = " + UtilFactory.checkNull(unitName) + ")");
            //do not add if this join already added to the query in the previous condition.
          /*  if(homeUnit==null || homeUnit.equals("")){
                searchQry.append(" AND OSP$PERSON.HOME_UNIT = OSP$UNIT.UNIT_NUMBER");
            }
           */
        }
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("QUERY","String",searchQry.toString()));
        Vector result = new Vector(3,2);
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", "call dw_get_person_list_for_query("+
                    "<<QUERY>>,<<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        int resultSize = result.size();
        for(int index=0;index<resultSize;index++){
            /* case #748 comment begin */
            //Hashtable htPerSearchRes = (Hashtable)result.elementAt(index);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap htPerSearchRes = (HashMap)result.elementAt(index);
            /* case #748 end */
            PersonInfoBean personInfo = new PersonInfoBean();
            personInfo.setPersonID((String)htPerSearchRes.get("PERSON_ID"));
            personInfo.setLastName((String)htPerSearchRes.get("LAST_NAME"));
            personInfo.setFirstName((String)htPerSearchRes.get("FIRST_NAME"));
            personInfo.setPriorName((String)htPerSearchRes.get("PRIOR_NAME"));
            personInfo.setFullName((String)htPerSearchRes.get("FULL_NAME"));
            personInfo.setUserName((String)htPerSearchRes.get("USER_NAME"));
            personInfo.setEmail((String)htPerSearchRes.get("EMAIL_ADDRESS"));
            personInfo.setHomeUnit((String)htPerSearchRes.get("HOME_UNIT"));
            personInfo.setUnitName((String)htPerSearchRes.get("UNIT_NAME"));
            personInfo.setDirTitle((String)htPerSearchRes.get("DIRECTORY_TITLE"));
            personInfo.setOffLocation((String)htPerSearchRes.get("OFFICE_LOCATION"));
            personInfo.setOffPhone((String)htPerSearchRes.get("OFFICE_PHONE"));
            personInfo.setSecOffLoc((String)htPerSearchRes.get("SECONDRY_OFFICE_LOCATION"));
            personInfo.setSecOffPhone((String)htPerSearchRes.get("SECONDRY_OFFICE_PHONE"));
            personSearchRes.addElement(personInfo);
        }
        return personSearchRes;
    }

    //Main method used for testing
    /*public static void main(String args[])throws DBException{
        SearchBean bean = new SearchBean("1111");
        //bean.getPersonDetails("smith","","","","","","","","","");
        bean.getInitialCOIDisclosures("900053523");
    }*/
}