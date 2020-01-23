package edu.ucsd.coeus.personalization;

import java.util.Hashtable;

import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.ucsd.coeus.ServerUtils;
import edu.ucsd.coeus.personalization.formaccess.CoeusFormsSecureDocument;

public class AwardFormSecurePolicy extends BaseFormSecurePolicy {
	
	

	public AwardFormSecurePolicy(CoeusFormsSecureDocument fsecure,
			RequesterBean requesterBean, ResponderBean responderBean) {
		super(fsecure, requesterBean, responderBean);
	}
	
	@Override
	public String getPolicyModule() {
		return "award";
	}
	
	@Override
	/**
    //Award Modes
    private static final char NEW_MODE = 'N';
    private static final char MODIFY_MODE = 'M';
    private static final char NEW_ENTRY_MODE = 'E';
    private static final char NEW_CHILD_MODE = 'C';
    private static final char NEW_CHILD_COPIED_MODE = 'P';
    private static final char DISPLAY_MODE = 'D';	 * 
    private static final char GET_AWARD_DATA = 'C';
	 */
	public String getCorrectMode(RequesterBean requesterBean) {
		char ftype = requesterBean.getFunctionType();
		if(ftype == 'C'){
            Hashtable hshGetAwardData = (Hashtable)requesterBean.getDataObject();
            AwardBean awardBean = (AwardBean)hshGetAwardData.get(AwardBean.class);
            if(awardBean.getMode()== 'N'){
            	return "N";
            }
            if(awardBean.getMode()== 'M'){
            	return "U";
            }            
		}
		return "D";
	}

	@Override
	public void bindModuleVariable(ResponderBean responderBean) {
		Object awardobj = responderBean.getDataObject();
		if (awardobj instanceof  Hashtable) {
			Hashtable awarddata = (Hashtable)responderBean.getDataObject();
			AwardBean awardBean = (AwardBean)awarddata.get(AwardBean.class);
			if (awardBean != null) {
				super.currentUniqueID = awardBean.getMitAwardNumber();
				super.currentStatus = String.valueOf(awardBean.getStatusCode());
				super.creator = awardBean.getUpdateUser();
			}
		}
	}

	@Override
	public String getModuleBeanData(String attr, String beannm) {
            Object awardobj = responderBean.getDataObject();
            if (awardobj instanceof Hashtable) {
                Hashtable awarddata = (Hashtable)awardobj;
                AwardBean awardBean = (AwardBean)awarddata.get(AwardBean.class);
                
		if (awardBean == null) return "";
		String getterMethod = "get" + beannm.substring(0,1).toUpperCase() + beannm.substring(1);
		Object getterObject = ServerUtils.getGetterObject(awardBean,getterMethod);
		if (getterObject != null) {
			return ServerUtils.getBeanVarDataByMeth(getterObject,attr);
		}
            }
		return "";		
	}
	

}
