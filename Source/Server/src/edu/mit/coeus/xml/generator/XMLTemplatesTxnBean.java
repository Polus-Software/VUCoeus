/*
 * XMLTemplatesTxnBean.java
 *
 * Created on September 7, 2004, 2:45 PM
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import edu.mit.coeus.sponsormaint.bean.SponsorTemplateBean;

/**
 *
 * @author  geot
 */
public class XMLTemplatesTxnBean {
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    /** Creates a new instance of XMLTemplatesTxnBean */
    public XMLTemplatesTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    public byte[] getPropPrintTemplate(Hashtable htParams)
            throws CoeusException,DBException{
        SponsorMaintenanceDataTxnBean sponMainTxnBean = new SponsorMaintenanceDataTxnBean();
        SponsorTemplateBean tmplBean = sponMainTxnBean.getPageCLOBData(htParams.get("SPONSOR_CODE").toString(),
                                      Integer.parseInt(htParams.get("PACKAGE_NUMBER").toString()),
                                      Integer.parseInt(htParams.get("PAGE_NUMBER").toString()));
                                      
        return tmplBean.getFormTemplate();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
