/*
 * PropdevService.java
 *
 * Created on August 22, 2006, 9:09 AM
 */

package edu.mit.coeus.ws.utils;

import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.ws.utils.bean.InboxPtlBean;
import edu.mit.coeus.ws.utils.Converter;
import java.util.Vector;

/**
 *
 * @author  geot
 */
public class Utility {
    
    /** Creates a new instance of PropdevService */
    public Utility() {
    }
    
    public InboxPtlBean[] getInbox(String userId) throws Exception{
//        Vector v = new Vector();
        UtilFactory.log("In getInbox of UtilityService port");
        try{
        ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
        Vector v = proposalActionTxnBean.getInboxForUser(userId);
        int size = v==null?0:v.size();
        for(int i=size-1;i>=0;i--){
            InboxBean ib = (InboxBean)v.get(i);
            if(ib.getModuleCode()!=3){
                v.remove(i);
            }
        }
        int maxSize = v==null?0:(v.size()>25?25:v.size());
        InboxPtlBean[] ipb = new InboxPtlBean[maxSize];
        for(int i=0;i<maxSize;i++){
            InboxBean ib = (InboxBean)v.get(i);
            ipb[i] = (InboxPtlBean)Converter.wrapBean(InboxPtlBean.class,ib);
            ipb[i].setMessage(ib.getMessageBean().getMessage());
            //url = http://coeus-dev.mit.edu/coeus42
            String linkURL = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL)+
                                "displayProposal.do?proposalNo="+ib.getProposalNumber();
            ipb[i].setLinkURL(linkURL);
            ipb[i].setStatusCode(ib.getCreationStatus());
            ipb[i].setStatus(ib.getCreationStatusDescription());
            ipb[i].setSubject(ib.getSubjectDescription());
        }
        UtilFactory.log("getInbox of UtilityService port gets called and returned "+ipb.length +" rows");
        return ipb;
        }catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex,"Utility", "getInbox");
            throw ex;
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        new Utility().getInbox("ele");
        
    }
    
}
