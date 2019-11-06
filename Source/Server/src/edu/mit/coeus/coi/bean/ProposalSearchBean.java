/*
 * @(#)ProposalSearchBean.java 1.0 3/30/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import java.sql.SQLException;
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
 *
 * This class is for doing Proposal search in the COI module. All search methods will return
 * a vector which has a collection bean instance for that particular search result.
 *
 * @version 1.0 March 30, 2002, 4:03 PM
 * @author  Anil Nandakumar
 */
public class ProposalSearchBean {
    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    /*
     *  Initialize search bean with a particular person id. ie, All search methods
     *  in this class always belongs to a person.
     */
    private String personId;
    /** Creates new ProposalSearchBean.
     * Contructor with one argument
     * @param String personId
     */
    public ProposalSearchBean(String personId){
        this.personId = personId;
        //dbEngine = DBEngineImpl.getInstance();
        dbEngine = new DBEngineImpl();
    }
    /**
     *  This method is used to get all Proposal Details for a particular search criterea.
     *  This method will also throw an exception if the person id for this bean is null.
     *  <li>To fetch the data, it uses dw_get_inst_props_for_query procedure.
     *  @param String Person Name
     *  @param String Proposal Number
     *  @param String Account Number
     *  @param String Title
     *  @param String Unit Number
     *  @param String Unit Name
     *  @param String Investigator
     *  @param String Sponsor Code
     *  @param String Spon Name
     *  @param String Proposal Type
     *  @param String Status
     *  @return Vector vector of <code>ProposalDetailsBean</code> instance
     *  @exception DBException
     */
    public Vector getProposalDetails(String personName, String proposalNo,
                                    String accountNo,String title,String unitNo,
                                    String unitName,String investigator,String sponsorCode,
                                    String sponName,String proposalType,String status)
            throws DBException{
        Vector result = new Vector(3,2);
        Vector proposals = new Vector(3,2);
        StringBuffer searchQry = new StringBuffer("");
        searchQry.append(" SELECT  OSP$PROPOSAL.PROPOSAL_NUMBER, ");
        searchQry.append(" OSP$PROPOSAL.PROPOSAL_TYPE_CODE,  ");
        searchQry.append(" OSP$PROPOSAL.CURRENT_ACCOUNT_NUMBER,  ");
        searchQry.append(" OSP$PROPOSAL.SPONSOR_CODE,  ");
        searchQry.append(" OSP$SPONSOR.SPONSOR_NAME, ");
        searchQry.append(" OSP$PROPOSAL_INVESTIGATORS.PERSON_NAME,  ");
        searchQry.append(" OSP$PROPOSAL.STATUS_CODE, ");
        searchQry.append(" OSP$PROPOSAL.TITLE,  ");
        searchQry.append(" OSP$PROPOSAL.SEQUENCE_NUMBER,  ");
        searchQry.append(" OSP$PROPOSAL_UNITS.UNIT_NUMBER,  ");
        searchQry.append(" OSP$UNIT.UNIT_NAME ");
        searchQry.append(" FROM OSP$PROPOSAL, OSP$SPONSOR, OSP$PROPOSAL_INVESTIGATORS, OSP$PROPOSAL_UNITS, OSP$UNIT  ");
        searchQry.append(" WHERE (OSP$PROPOSAL.SPONSOR_CODE = OSP$SPONSOR.SPONSOR_CODE (+))  ");
        searchQry.append(" AND (OSP$PROPOSAL.SEQUENCE_NUMBER IN  (  ");
        searchQry.append(" SELECT MAX(A.SEQUENCE_NUMBER)  ");
        searchQry.append(" FROM OSP$PROPOSAL A  ");
        searchQry.append(" WHERE A.PROPOSAL_NUMBER =OSP$PROPOSAL.PROPOSAL_NUMBER ))  ");
        searchQry.append(" AND (OSP$PROPOSAL.PROPOSAL_NUMBER = OSP$PROPOSAL_UNITS.PROPOSAL_NUMBER)  ");
        searchQry.append(" AND (OSP$PROPOSAL_UNITS.LEAD_UNIT_FLAG = 'Y')   ");
        searchQry.append(" AND (OSP$PROPOSAL_UNITS.SEQUENCE_NUMBER IN  (  ");
        searchQry.append(" SELECT MAX(B.SEQUENCE_NUMBER)  ");
        searchQry.append(" FROM OSP$PROPOSAL_UNITS B  ");
        searchQry.append(" WHERE B.PROPOSAL_NUMBER =OSP$PROPOSAL_UNITS.PROPOSAL_NUMBER ))  ");
        searchQry.append(" AND (OSP$PROPOSAL_UNITS.UNIT_NUMBER = OSP$UNIT.UNIT_NUMBER )  ");
        searchQry.append(" AND (OSP$PROPOSAL.PROPOSAL_NUMBER = OSP$PROPOSAL_INVESTIGATORS.PROPOSAL_NUMBER)  ");
        searchQry.append(" AND (OSP$PROPOSAL_INVESTIGATORS.PRINCIPAL_INVESTIGATOR_FLAG = 'Y')   ");
        searchQry.append(" AND (OSP$PROPOSAL_INVESTIGATORS.SEQUENCE_NUMBER IN  (  ");
        searchQry.append(" SELECT MAX(C.SEQUENCE_NUMBER)  ");
        searchQry.append(" FROM OSP$PROPOSAL_INVESTIGATORS C  ");
        searchQry.append(" WHERE C.PROPOSAL_NUMBER =OSP$PROPOSAL_INVESTIGATORS.PROPOSAL_NUMBER))   ");
        searchQry.append(" AND (OSP$PROPOSAL.PROPOSAL_NUMBER  IN  ");
        searchQry.append(" (SELECT DISTINCT PROPOSAL.PROPOSAL_NUMBER  ");
        //FROM CLAUSE BEGINS HERE
        searchQry.append(" FROM OSP$PROPOSAL PROPOSAL ");
        if(investigator!=null && !investigator.equals("")){
            searchQry.append(" ,OSP$PROPOSAL_INVESTIGATORS  PI  ");
        }
        if(sponName!=null && !sponName.equals("")){
            searchQry.append(" ,OSP$SPONSOR SPONSOR   ");
        }
        if((unitName!=null && !unitName.equals("")) || (unitNo!=null && !unitNo.equals(""))){
            searchQry.append(" ,OSP$PROPOSAL_UNITS PU  ");
        }
        if(unitName!=null && !unitName.equals("")){
            searchQry.append(" ,OSP$UNIT UNIT  ");
        }
        //WHERE CLAUSE BEGINS HERE
        searchQry.append(" WHERE  ");
        searchQry.append(" (PROPOSAL.SEQUENCE_NUMBER = (  ");
        searchQry.append(" SELECT MAX(P1.SEQUENCE_NUMBER)   ");
        searchQry.append(" FROM OSP$PROPOSAL P1  ");
        searchQry.append(" WHERE PROPOSAL.PROPOSAL_NUMBER = P1.PROPOSAL_NUMBER )) ");
        if(investigator!=null && !investigator.equals("")){
            searchQry.append(" AND  (PROPOSAL.PROPOSAL_NUMBER = PI.PROPOSAL_NUMBER)  ");
            searchQry.append(" AND (PI.SEQUENCE_NUMBER IN (SELECT MAX(PI1.SEQUENCE_NUMBER)  ");
            searchQry.append(" FROM  OSP$PROPOSAL_INVESTIGATORS PI1 WHERE PI1.PROPOSAL_NUMBER = PI.PROPOSAL_NUMBER ))   ");
            searchQry.append(" AND (UPPER(PI.PERSON_NAME) LIKE  " + UtilFactory.checkNull(investigator) + ")");
        }
        if(sponName!=null && !sponName.equals("")){
            searchQry.append(" AND  ( PROPOSAL.SPONSOR_CODE = SPONSOR.SPONSOR_CODE) ");
            searchQry.append(" AND (UPPER(SPONSOR.SPONSOR_NAME) LIKE  " + UtilFactory.checkNull(sponName) + ")");
        }
        if((unitName!=null && !unitName.equals("")) || (unitNo!=null && !unitNo.equals(""))){
            searchQry.append(" AND  ( PROPOSAL.PROPOSAL_NUMBER = PU.PROPOSAL_NUMBER)   ");
            searchQry.append(" AND ( PU.SEQUENCE_NUMBER IN  ( SELECT MAX(PU1.SEQUENCE_NUMBER)   ");
            searchQry.append(" FROM OSP$PROPOSAL_UNITS PU1  WHERE PU1.PROPOSAL_NUMBER =PU.PROPOSAL_NUMBER ))   ");
        }
        if(unitName!=null && !unitName.equals("")){
            searchQry.append(" AND ( PU.UNIT_NUMBER = UNIT.UNIT_NUMBER )  ");
            searchQry.append(" AND (UPPER(UNIT.UNIT_NAME) LIKE " + UtilFactory.checkNull("%"+unitName+"%")+ ")");
        }
        if(unitNo!=null && !unitNo.equals("")){
            searchQry.append(" AND (PU.UNIT_NUMBER = " + UtilFactory.checkNull(unitNo) + ")");
        }
        if(accountNo!=null && !accountNo.equals("")){
            searchQry.append(" AND (PROPOSAL.CURRENT_ACCOUNT_NUMBER = " + UtilFactory.checkNull(accountNo) + ")");
        }
        if(proposalNo!=null && !proposalNo.equals("")){
            searchQry.append(" AND PROPOSAL.PROPOSAL_NUMBER =  " + UtilFactory.checkNull(proposalNo));
        }
        if(title!=null && !title.equals("")){
            searchQry.append(" AND (UPPER(PROPOSAL.TITLE) LIKE  " + UtilFactory.checkNull("%"+title+"%")+")");
        }
        if(sponsorCode!=null && !sponsorCode.equals("")){
            searchQry.append(" AND (PROPOSAL.SPONSOR_CODE =  " + UtilFactory.checkNull(sponsorCode) + ")");
        }
        if(proposalType!=null && !proposalType.equals("")){
            searchQry.append(" AND (PROPOSAL.PROPOSAL_TYPE_CODE =  " + UtilFactory.checkNull(proposalType) + ")");
        }
        if(status!=null && !status.equals("")){
            searchQry.append(" AND (PROPOSAL.STATUS_CODE =  " + UtilFactory.checkNull(status) + ")");
        }
        searchQry.append(" )) " );
        Vector param= new java.util.Vector();
        param.addElement(new Parameter("QUERY","String",searchQry.toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_inst_props_for_query ( <<QUERY>> , <<OUT RESULTSET rset>> )",
              "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int i=0;i<result.size();i++){
            /* case #748 comment begin */
            //Hashtable proposalRow = (Hashtable)result.elementAt(i);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap proposalRow = (HashMap)result.elementAt(i);
            /* case #748 end */
            ProposalDetailsBean proposalDetails = new ProposalDetailsBean();
            proposalDetails.setProposalNumber
                (proposalRow.get("PROPOSAL_NUMBER") == null ? null :
                proposalRow.get("PROPOSAL_NUMBER").toString());
            proposalDetails.setAccountNumber(
                proposalRow.get("CURRENT_ACCOUNT_NUMBER") == null ? null :
                proposalRow.get("CURRENT_ACCOUNT_NUMBER").toString());
            proposalDetails.setTitle((String)proposalRow.get("TITLE"));
            proposalDetails.setUnitNumber(
                proposalRow.get("UNIT_NUMBER") == null ? null :
                proposalRow.get("UNIT_NUMBER").toString());
            proposalDetails.setUnitName((String)proposalRow.get("UNIT_NAME"));
            proposalDetails.setInvestigatorName((String)proposalRow.get("PERSON_NAME"));
            proposalDetails.setSponsorCode(
                proposalRow.get("SPONSOR_CODE") == null ? null :
                proposalRow.get("SPONSOR_CODE").toString());
            proposalDetails.setSponsorName((String)proposalRow.get("SPONSOR_NAME"));
            proposalDetails.setStatusCode(
                proposalRow.get("STATUS_CODE") == null ? null :
                proposalRow.get("STATUS_CODE").toString());
            proposalDetails.setProposalType(
                proposalRow.get("PROPOSAL_TYPE_CODE") == null ? null :
                proposalRow.get("PROPOSAL_TYPE_CODE").toString());
            proposals.add(proposalDetails);
        }
        return proposals;
    }
    /**
     *  This method populates the list box meant to retrieve the Proposal Status for the Proposal search screen.
     *  To fetch the data, it uses a simple database fetch query.
     *  @return HashMap
     *  @exception DBException
     */
    /* CASE #748 */
    //Update method to return HashMap instead of Hashtable.
    public HashMap getProposalStatus() throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        /* case #748 comment begin */
        //Hashtable proposalStatus = new Hashtable();
        /* case #748 comment end */
        /* case #748 begin */
        HashMap proposalStatus = new HashMap();
        /* case #748 end */
        if(dbEngine!=null){
            //result = dbEngine.executeRequest("Coeus", "select * from osp$proposal_status order by proposal_status_code asc", "Coeus", param);
            result = dbEngine.executeRequest("Coeus",
              "call dw_get_proposal_status ( <<OUT RESULTSET rset>> )",
              "Coeus", param);

        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int i=0;i<result.size();i++){
            /* case #748 comment begin  */
            //Hashtable proposalRow = (Hashtable)result.elementAt(i);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap proposalRow = (HashMap)result.elementAt(i);
            /* case #748 end */
            proposalStatus.put(proposalRow.get("PROPOSAL_STATUS_CODE").toString(),
                                    (String)proposalRow.get("DESCRIPTION"));
        }
        return proposalStatus;
    }
    /**
     *  This method populates the list box meant to retrieve the Proposal Type for the Proposal search screen.
     *  To fetch the data, it uses a simple database fetch query.
     *  @return HashMap
     *  @exception DBException
     */
    /* CASE #748 */
    // Update method to return HashMap instead of Hashtable.
    public HashMap getProposalType() throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        /* case #748 comment begin */
        //Hashtable proposalTypes = new Hashtable();
        /* case #748 comment end */
        /* case #748 begin */
        HashMap proposalTypes = new HashMap();
        /* case #748 end */
        if(dbEngine!=null){
            //result = dbEngine.executeRequest("Coeus", "SELECT * FROM osp$proposal_type ORDER BY proposal_type_code asc", "Coeus", param);
            result = dbEngine.executeRequest("Coeus",
                "call get_proposal_type_list_dw ( <<OUT RESULTSET rset>> )",
                "Coeus", param);
        }else{
            throw new DBException("exceptionCode.10001");
        }
        for(int i=0;i<result.size();i++){
            /* case #748 comment begin */
            //Hashtable proposalTypeRow = (Hashtable)result.elementAt(i);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap proposalTypeRow = (HashMap)result.elementAt(i);
            /* case #748 end */
            proposalTypes.put(proposalTypeRow.get("PROPOSAL_TYPE_CODE").toString(),
                (String)proposalTypeRow.get("DESCRIPTION"));
        }
        return proposalTypes;
    }

      /**
       *  This method populates the list box meant to retrieve the Proposal Type for the Proposal search screen.
       *  To fetch the data, it uses a simple database fetch query.
       *  @return Vector
       *  @exception DBException
       */
	public Vector getProposalTypes() throws DBException{
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		Vector proposalTypes = new Vector();
		if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                "call get_proposal_type_list_dw ( <<OUT RESULTSET rset>> )",
                "Coeus", param);
			//result = dbEngine.executeRequest("Coeus", "SELECT * FROM osp$proposal_type ORDER BY proposal_type_code asc", "Coeus", param);
		}else{
			throw new DBException("exceptionCode.10001");
		}
		for(int i=0;i<result.size();i++){
                    /* case #748 comment begin */
                    //Hashtable proposalTypeRow = (Hashtable)result.elementAt(i);
                    /* case #748 comment end */
                    /* case #748 begin */
                    HashMap proposalTypeRow = (HashMap)result.elementAt(i);
                    /* case #748 end */
                    //push the code and description to ComboBoxBean
		    ComboBoxBean comboBoxBean =
                        new ComboBoxBean(
                        proposalTypeRow.get("PROPOSAL_TYPE_CODE").toString(),
                        (String)proposalTypeRow.get("DESCRIPTION"));
		    proposalTypes.add(comboBoxBean);
		}
		return proposalTypes;
    }

    //Main method for testing
    /*public static void main(String args[]) throws Exception{
        ProposalSearchBean bean = new ProposalSearchBean("1111");
        bean.getProposalDetails("","56356","","","","","","","","","");
        System.out.println("done");
    }*/
}