package edu.ucsd.coeus.personalization;

import java.util.Hashtable;
import java.util.Iterator;

import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.ucsd.coeus.ServerUtils;
import edu.ucsd.coeus.personalization.formaccess.CoeusFormsSecureDocument;

public class ProposalFormSecurePolicy extends BaseFormSecurePolicy {
	
	

	public ProposalFormSecurePolicy(CoeusFormsSecureDocument fsecure,
			RequesterBean requesterBean, ResponderBean responderBean) {
		super(fsecure, requesterBean, responderBean);
	}
	
	@Override
	public String getPolicyModule() {
		return "proposal";
	}
	
	@Override
/**
    private static final char NEW_MODE = 'N';
    private static final char MODIFY_MODE = 'M';
    private static final char NEW_ENTRY_MODE = 'E';
    private static final char DISPLAY_MODE = 'D';
 */
	public String getCorrectMode(RequesterBean requesterBean) {
		char ftype = requesterBean.getFunctionType();
		if (ftype == 'M') return "U";
		else if (ftype == 'N' || ftype == 'E') return "N";
		return "D";		
	}

	@Override
	public void bindModuleVariable(ResponderBean responderBean) {
		Object proposalobj = responderBean.getDataObject();
		if (proposalobj instanceof  Hashtable) {
			Hashtable proposaldata = (Hashtable)responderBean.getDataObject();
            CoeusVector cvData = (CoeusVector)proposaldata.get(InstituteProposalBean.class);
            if (cvData == null || cvData.size() == 0) return;
			InstituteProposalBean proposalBean = (InstituteProposalBean)cvData.elementAt(0);
			if (proposalBean != null) {
				super.currentUniqueID = proposalBean.getProposalNumber();
				super.currentStatus = String.valueOf(proposalBean.getStatusCode());
				super.creator = proposalBean.getUpdateUser();
			}
		}
	}

	@Override
	public String getModuleBeanData(String attr, String beannm) {
		Hashtable proposaldata = (Hashtable)responderBean.getDataObject();
        CoeusVector cvData = (CoeusVector)proposaldata.get(InstituteProposalBean.class);
        if (cvData == null || cvData.size() == 0) return "";
        for (Iterator iterator = cvData.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object.getClass().getName().equals(beannm)) {
				return ServerUtils.getBeanVarDataByMeth(object,attr);
			}			
		}
        return "";
	}
	

}
