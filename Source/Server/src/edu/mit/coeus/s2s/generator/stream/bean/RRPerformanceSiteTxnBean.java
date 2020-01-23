/*
 * @(#)RRPerformanceSiteTxnBean.java May 5,2005
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.s2s.generator.stream.bean;

import java.util.* ;
import java.sql.Connection;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;


  
public class RRPerformanceSiteTxnBean{
    private DBEngineImpl dbEngine;
    private TransactionMonitor transMon; 
    QueryEngine queryEngine;
    private Connection conn = null;
    
    /** Creates a new instance of RRPerformanceSiteTxnBean */
    public RRPerformanceSiteTxnBean(){
        
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        queryEngine = QueryEngine.getInstance();
    }
    
      
     /*case 2406*/
    public HashMap getOrganizationID(String propNumber,String orgType)
            throws CoeusException, DBException {
   
    Vector result = null;
    Vector param= new Vector();
    
    HashMap row = null;
    param.addElement(new Parameter("PROPOSAL_NUMBER",
                       DBEngineConstants.TYPE_STRING,propNumber));
    param.addElement(new Parameter("ORG_TYPE",
                       DBEngineConstants.TYPE_STRING, orgType));
    if(dbEngine !=null){
           result = new Vector(3,2);
           result = dbEngine.executeRequest("Coeus",
               "call s2sRRPerfSites_V11Pkg.getOrganization( <<PROPOSAL_NUMBER>> ,<<ORG_TYPE>> , "
                                + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
                throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            row = (HashMap)result.elementAt(0);
        }

        return row;
    }
    
    /**
     * changed for case 2406
     * Method used to get proposal location list from  OSP$EPS_PROP_SITES  for
     * a given proposalnumber .
     * <li>To fetch the data, it uses get_prop_location_list procedure.
     * change for 2406: instead of returning an arraylist with the rolodex bean,
     * return an arraylist containing a HashMap.
     * One key will be 'rolodexbean' and will contain the rolodex bean. 
     * Second key will be cong district and will contain the string.
     * @param proposalNumber get list of proposalLoctionList for this id
     * @return Vector map of Proposal Location data is set of
     * proposalLocationFormBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ArrayList getOtherSiteList(String proposalNumber)
                                    throws CoeusException , DBException{
        ArrayList otherSiteList = null;
        
        HashMap hmOtherSite = new HashMap();
        String  congDistricts = null;
        
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap locationProposalListRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_prop_location_list( <<PROPOSAL_NUMBER>> , "
            +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        StringBuffer strBffr ;
        if(listSize >0){
            otherSiteList = new ArrayList();
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                RolodexDetailsBean rolodexDetails = new RolodexDetailsBean();
                locationProposalListRow = (HashMap)result.elementAt(rowIndex);
                rolodexDetails.setOrganization((String)
                    (locationProposalListRow.get("ORGANIZATION") == null ?
                    locationProposalListRow.get("LOCATION"):
                    locationProposalListRow.get("ORGANIZATION")));
                       
                      
                rolodexDetails.setAddress1((String)
                    locationProposalListRow.get("ADDRESS_LINE_1") );
                rolodexDetails.setAddress2((String)
                    locationProposalListRow.get("ADDRESS_LINE_2") );
                rolodexDetails.setCity((String)
                    locationProposalListRow.get("CITY") );
                rolodexDetails.setCounty((String)
                    locationProposalListRow.get("COUNTY") );
                rolodexDetails.setState((String)
                    locationProposalListRow.get("STATE") );
                rolodexDetails.setPostalCode((String)
                    locationProposalListRow.get("POSTAL_CODE") );
                rolodexDetails.setCountry((String)
                    locationProposalListRow.get("COUNTRY_CODE") );
//                otherSiteList.add(rolodexDetails);
                hmOtherSite = new HashMap();
                hmOtherSite.put("rolodexBean", rolodexDetails);
                congDistricts = (String) locationProposalListRow.get("CONGDIST");
                hmOtherSite.put("congDist", congDistricts);
                otherSiteList.add(hmOtherSite);
            }
        }
        return otherSiteList;
    }
    
  
}
