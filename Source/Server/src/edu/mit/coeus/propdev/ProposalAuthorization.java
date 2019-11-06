/*
 * ProposalAuthorization.java
 *
 * Created on June 18, 2009, 7:10 PM
 */

package edu.mit.coeus.propdev;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.dbengine.DBException;

/**
 *
 * @author sreenathv
 */
public class ProposalAuthorization {
    
    private static final String VIEW_ANY_PROPOSAL = "VIEW_ANY_PROPOSAL";
    private static final String MODIFY_ANY_PROPOSAL = "MODIFY_ANY_PROPOSAL";
    
    private static final String MODIFY_PROPOSAL = "MODIFY_PROPOSAL";
    private static final String VIEW_PROPOSAL = "VIEW_PROPOSAL";
    private static final String CREATE_PROPOSAL = "CREATE_PROPOSAL";
    /** Creates a new instance of ProposalAuthorization */
    public ProposalAuthorization() {
    }
    
    /**
     * Method used to check if a person has proper right to view Development Prioposal.
     * @param userId 
     * @param proposalNumber
     * @return boolean true if the user has proper right
     * @throws CoeusException
     * @throws DBException
     * @throws org.okip.service.shared.api.Exception
     */
    public boolean canViewProposal(String userId, String proposalNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception{
        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
        String leadUnit = proposalDevelopmentTxnBean.getProposalLeadUnit(proposalNumber);
        return canViewProposal(userId,  proposalNumber,  leadUnit);
    }
    
    /**
     * Method used to check if a person has proper right to view Development Prioposal.
     * @param userId 
     * @param proposalNumber
     * @param unitNumber - The Lead Unit of the deveopment proposal
     * @return boolean true if the user has proper right
     * @throws CoeusException
     * @throws DBException
     * @throws org.okip.service.shared.api.Exception
     */
    public boolean canViewProposal(String userId, String proposalNumber, String unitNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception{
        boolean isViewable = false;
        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
        UserMaintDataTxnBean userMainDataTxnBean = new UserMaintDataTxnBean();
        if(unitNumber != null && !"".equals(unitNumber)){
            
            isViewable = userMainDataTxnBean.getUserHasProposalRight(userId, proposalNumber, VIEW_PROPOSAL);
            if(!isViewable){
                isViewable = userMainDataTxnBean.getUserHasProposalRight(userId, proposalNumber, MODIFY_PROPOSAL);
                if(!isViewable){
                    isViewable = userMainDataTxnBean.getUserHasRight(userId, VIEW_ANY_PROPOSAL, unitNumber);
                    if(!isViewable){
                        isViewable = userMainDataTxnBean.getUserHasRight(userId, MODIFY_ANY_PROPOSAL, unitNumber);
                        if(!isViewable){
                            isViewable = userMainDataTxnBean.getUserHasRight(userId, CREATE_PROPOSAL, unitNumber);
                        }
                    }
                }
            }
        }
        return isViewable;
    }
}
