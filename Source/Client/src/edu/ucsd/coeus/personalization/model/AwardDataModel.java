package edu.ucsd.coeus.personalization.model;

import java.util.Vector;

import edu.mit.coeus.award.bean.AwardAmountInfoBean;
import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.QueryEngine;

/**
 * Used for context sensitive award view
 * @author rdias
 *
 */
public class AwardDataModel {
    private static final String SERVLET = "/AwardMaintenanceServlet";
    private static final char UCSD_DELTA_REPORTS = '^';
    private static final char GET_AWARD_HIERARCHY = 'A';
    private static final char MAX_ACC_SEQ_NUMBER = 'm';
    private static final char GET_AWARD_HIERARCHY_SUMMARY = 'B';
	private QueryEngine queryEngine = QueryEngine.getInstance();
    private AwardBaseBean awardBaseBean;
    private AwardBean awardBean;
	
	
	final private static AwardDataModel uniqueInstance = new AwardDataModel();

	private AwardDataModel() {
	}
	
    public static AwardDataModel getInstance() {
		return uniqueInstance;
	}	    
    
    /**
     * Load an award sequence into query engine
     * This is specialize for personalization to show context sensitive reports
     */    
    public CoeusVector loadAwardData(String awardno, int seqno) throws CoeusException {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(UCSD_DELTA_REPORTS);
        AwardBean awardBeanToServer = new AwardBean();
        awardBeanToServer.setMitAwardNumber(awardno);
        awardBeanToServer.setSequenceNumber(seqno);
        requesterBean.setDataObject(awardBeanToServer);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            CoeusOptionPane.showErrorDialog("Unable to connect to coeus server");
            return null;
        }
        if(responderBean.hasResponse()) {
        	Object dobject = responderBean.getDataObject();
        	if (dobject != null && dobject instanceof CoeusVector) {
        		CoeusVector awardData = (CoeusVector)responderBean.getDataObject();
        		return awardData;
        	}
        }else {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
        }
        return null;
    }
    
    public CoeusVector loadAwardTree(String mitAwardNumber) throws CoeusException {
        CoeusVector cvData = new CoeusVector();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_AWARD_HIERARCHY);
        requester.setDataObject(mitAwardNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.hasResponse()) {
            Vector vecData = response.getDataObjects();
            cvData = (CoeusVector)vecData.get(0);
            return cvData;
        }else {
        	CoeusOptionPane.showErrorDialog("Error connecting to the server");
        }
        return new CoeusVector();
    }
    
    public AwardAmountInfoBean loadAwardTreeSummary(String mitAwardNumber) throws CoeusException {
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_AWARD_HIERARCHY_SUMMARY);
        requester.setDataObject(mitAwardNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.hasResponse()) {
        	AwardAmountInfoBean infobean =(AwardAmountInfoBean)response.getDataObject();
            return infobean;
        }else {
        	CoeusOptionPane.showErrorDialog("Error connecting to the server");
        }
        return null;
    }
    
    public int getMaxAmountSeq(String mitAwardNumber, int seqnumber) throws CoeusException {
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(MAX_ACC_SEQ_NUMBER);
        AwardBean awardBeanToServer = new AwardBean();
        awardBeanToServer.setMitAwardNumber(mitAwardNumber);
        awardBeanToServer.setSequenceNumber(seqnumber);
        requester.setDataObject(awardBeanToServer);
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.hasResponse()) {
        	Integer amtseq =(Integer)response.getDataObject();
            return amtseq.intValue();
        }else {
        	CoeusOptionPane.showErrorDialog("Error connecting to the server");
        }
        return 0;
    }
        
    /**
     * To solve memory issue
     * @param queryKey
     */
    public void removeAward(String queryKey) {
    	queryEngine.removeDataCollection(queryKey);	
    }
    
    
    
	

}
