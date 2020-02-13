/**
 * AwardProposalFeedTxnBean.java
 * 
 * Feeds data from institute proposal to award when IP is awarded
 *
 * @created	September 26, 2013
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.award.bean;


import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.instprop.bean.InstituteProposalInvestigatorBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.instprop.bean.InvestigatorUnitAdminTypeBean;
import edu.mit.coeus.propdev.bean.UnitMapBean;
import edu.mit.coeus.propdev.bean.UnitMapDetailsBean;
import java.io.ByteArrayOutputStream;
import java.util.Vector;
import java.util.HashMap;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Connection;
import java.util.Comparator;

public class AwardProposalFeedTxnBean {
    
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    private Connection conn = null;
    private static final String DSN = "Coeus";
    private TransactionMonitor transactionMonitor;
    
    /** Creates a new instance of AwardProposalFeedTxnBean */
    public AwardProposalFeedTxnBean() {
        dbEngine = new DBEngineImpl();
        transactionMonitor = TransactionMonitor.getInstance();
    }
    
    /* Get investigators */
    public CoeusVector getInvestigatorsAndUnits (String proposalNumber) 
    	throws CoeusException, DBException {

    	CoeusVector cvInvestigatorsAndUnits = new CoeusVector();
    	
    	edu.mit.coeus.instprop.bean.InstituteProposalTxnBean txnBean = 
    		new edu.mit.coeus.instprop.bean.InstituteProposalTxnBean();
    	cvInvestigatorsAndUnits = txnBean.getInstituteProposalInvestigators(proposalNumber);
    	
    	return cvInvestigatorsAndUnits;
    }
 }
