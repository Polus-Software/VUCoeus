/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.services;

import edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Mr Lijo
 */
public class CoiProjectService {

    private static CoiProjectService instance = null;
   private CoiProjectService() {
    }
    public static CoiProjectService getInstance() {
        if (instance == null) {
            instance = new CoiProjectService();
        }
        return instance;
    }

      public Integer getModulecode(String diclosureNumber, Integer sequenceNumber, HttpServletRequest request) throws IOException, Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        Vector moduleCodeVector = null;
        Integer moduleCode = null;
        CoiProjectEntityDetailsBean moduleCodeBean = new CoiProjectEntityDetailsBean();
        hmData.put("coiDisclosureNumber", diclosureNumber);
        hmData.put("sequenceNumber", sequenceNumber);
        Hashtable moduleCodeHashtable = (Hashtable) webTxnBean.getResults(request, "getModuleCodeForDiscl", hmData);
        moduleCodeVector = (Vector) moduleCodeHashtable.get("getModuleCodeForDiscl");
        if (moduleCodeVector != null && moduleCodeVector.size() > 0) {
            moduleCodeBean = (CoiProjectEntityDetailsBean) moduleCodeVector.get(0);
            moduleCode = (Integer) moduleCodeBean.getModuleCode();
        }
        return moduleCode;
    }

   public void removeProjectAndDetails(String disclNumber, int seNumber, String moduleItemKey, String transcationId, HttpServletRequest request) throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        
        hmData.put("coiDisclosureNumber", disclNumber);
        hmData.put("sequenceNumber", seNumber);
        hmData.put("moduleItemKey", moduleItemKey);
        webTxnBean.getResults(request, transcationId, hmData);

    }
}
