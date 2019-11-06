package edu.ucsd.coeus.personalization;

import org.okip.service.shared.api.Exception;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.dbengine.DBException;

public class LoadVariables {
	
	
	/* Given user name get his unit */
	public static String getUnit(String username) {
		UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
		UserInfoBean usrbean = null;
		try {
			usrbean = userMaintDataTxnBean.getUser(username);
		} catch (DBException e) {
		      UtilFactory.log("Failed to get user unit", e, "LoadVariables", "getUnit");						
		} catch (CoeusException e) {
			UtilFactory.log("Failed to get user unit", e, "LoadVariables", "getUnit");
		} catch (Exception e) {
			UtilFactory.log("Failed to get user unit", e, "LoadVariables", "getUnit");
		}
		if (usrbean != null)
			return usrbean.getUnitNumber();
		return "";
	}

}
