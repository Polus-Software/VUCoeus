/*
 * PrepareNotification.java
 *
 * Created on May 24, 2007, 2:47 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 15-JULY-2010
 * by Johncy M John
 */

package edu.mit.coeus.mailaction.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author talarianand
 */
public class PrepareNotification {
    
    private static final String EMPTY_STRING = "";
        
    /** Creates a new instance of PrepareNotification */
    public PrepareNotification() {
    }
  
    
   
    /**
     * This method is used to fetch the person information for an action. This
     * method will give all the roles and corresponding person information for
     * a specific action.
     * @param MailActionInfoBean which will have actionid, moduleid, module item
     * and module item sequence
     * @return CoeusVector which will have all the roles and corresponding person
     * information.
     */
    
    public CoeusVector fetchPersonInfo(MailActionInfoBean mailInfoBean) throws CoeusException, DBException {
        CoeusVector vecPersonInfo = new CoeusVector();
        MailActionTxnBean mailTxnBean = new MailActionTxnBean();
        vecPersonInfo = mailTxnBean.fetchRolePerson(mailInfoBean);
        return vecPersonInfo;
    }
    
   
    
    /**
     * Is used to fetch the system generated attachment
     * @param HashMap which will contain protocolnumber, actionid, correspondence code
     * @param HttpServletRequest object
     * @return path where the document has been generated.
     */
    public String fetchAttachment(HashMap hmData, HttpServletRequest request) throws Exception {
        String url = EMPTY_STRING;
        String protoNum = EMPTY_STRING;
        int actionId = 0;
        int correspCode = 0;
        //Added for COEUSQA-1724: Email Notification For All Actions In IACUC
        int moduleCode = 0;
        byte[] fileData = null;
        edu.mit.coeus.irb.bean.ProtocolDataTxnBean irbDataTxnBean = null;
        edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean iacucDataTxnBean = null;
        if(hmData != null && hmData.size() > 0) {
            protoNum = (String) hmData.get("protoNum");
            if(hmData.get("actionId") != null) {
                actionId = Integer.parseInt(hmData.get("actionId").toString());
            }
            if(hmData.get("correspId") != null) {
                correspCode = Integer.parseInt(hmData.get("correspId").toString());
            }
            //Added for COEUSQA-1724: Email Notification For All Actions In IACUC
              if(hmData.get("moduleCode") != null) {
                moduleCode = Integer.parseInt(hmData.get("moduleCode").toString());
            } 
        }
        HttpSession session = request.getSession();
        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE) {
            irbDataTxnBean= new edu.mit.coeus.irb.bean.ProtocolDataTxnBean();
            fileData = irbDataTxnBean.getSpecificCorrespondencePDF(protoNum, correspCode, actionId);
        } else if (moduleCode == ModuleConstants.IACUC_MODULE_CODE){
            iacucDataTxnBean= new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
            fileData = iacucDataTxnBean.getSpecificCorrespondencePDF(protoNum, correspCode, actionId);
        }
        if(fileData == null) {
            return url;
        }
        
        CoeusConstants.SERVER_HOME_PATH = session.getServletContext().getRealPath("/");
        String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config
        
        String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
        File reportDir = new File(filePath);
        if(!reportDir.exists()){
            reportDir.mkdirs();
        }
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
        File reportFile = new File(reportDir + File.separator + "CorresReport"+dateFormat.format(new Date())+".pdf");
        FileOutputStream fos = new FileOutputStream(reportFile);
        fos.write( fileData,0,fileData.length );
        fos.close();
        //Using Syetem Dependent File.Seperator
        //url = filePath + "\\" + reportFile.getName();
        url = filePath + File.separator + reportFile.getName();
        return url;
    }
   
}
