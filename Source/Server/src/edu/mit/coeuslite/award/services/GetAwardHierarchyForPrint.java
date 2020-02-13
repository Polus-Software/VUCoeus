/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.services;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.award.beans.AwardHierarchyPrintBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author midhunmk
 */
public class GetAwardHierarchyForPrint {

    public GetAwardHierarchyForPrint()
    {
    }
    public static GetAwardHierarchyForPrint instance=null;

     public static GetAwardHierarchyForPrint getInstance() {
        if (instance == null) {
            instance = new GetAwardHierarchyForPrint();
        }
        return instance;
    }



public Map getAwardHierarchy(HttpServletRequest request,String mitAwardNumber)
      {
         Map awardHierarchyMap=new HashMap();
         Vector awardHierarchyDetails=null;
         Vector awardDetails=null;
         Vector resultVector=new Vector();
         
          //to collect all the hierarchy details from the database
          try{
                WebTxnBean txnBean=new WebTxnBean();
                HashMap parameter=new HashMap();
                parameter.put("awardNumber",mitAwardNumber);
                Hashtable tmpHierarchy = (Hashtable) txnBean.getResults(request,"getAwardPrintHierarchy", parameter);
                awardHierarchyDetails= (Vector) tmpHierarchy.get("getAwardPrintHierarchy");
                if(awardHierarchyDetails!=null&&awardHierarchyDetails.size()>0)
                {
                    AwardHierarchyPrintBean awardHierarchyPrintBean,awardDetailsbean;
                    for (Iterator<AwardHierarchyPrintBean> it = awardHierarchyDetails.iterator(); it.hasNext();) {
                        awardHierarchyPrintBean = it.next();
                        awardDetails=null;
                        parameter.clear();
                        parameter.put("awardNumber", awardHierarchyPrintBean.getMitAwardNumber());
                        tmpHierarchy= (Hashtable) txnBean.getResults(request,"getAwardPrintDetails", parameter);
                        awardDetails= (Vector) tmpHierarchy.get("getAwardPrintDetails");
                        
                        if(awardDetails!=null){ 
                        //merging the details
                        awardDetailsbean=(AwardHierarchyPrintBean)awardDetails.get(0);
                        awardHierarchyPrintBean.setAccountNumber(awardDetailsbean.getAccountNumber());
                        awardHierarchyPrintBean.setAccountTypeCode(awardDetailsbean.getAccountTypeCode());
                        awardHierarchyPrintBean.setAccountTypeDesc(awardDetailsbean.getAccountTypeDesc());
                        awardHierarchyPrintBean.setFinalExpDate(awardDetailsbean.getFinalExpDate());
                        awardHierarchyPrintBean.setLeadUnit(awardDetailsbean.getLeadUnit());
                        awardHierarchyPrintBean.setPIName(awardDetailsbean.getPIName());
                        awardHierarchyPrintBean.setSponsorCode(awardDetailsbean.getSponsorCode());
                        awardHierarchyPrintBean.setStatusCode(awardDetailsbean.getStatusCode());
                        awardHierarchyPrintBean.setStatusDesc(awardDetailsbean.getStatusDesc());
                        awardHierarchyPrintBean.setTotalAntiAmount(awardDetailsbean.getTotalAntiAmount());
                        awardHierarchyPrintBean.setTotalObliAmount(awardDetailsbean.getTotalObliAmount());
                        resultVector.add(awardHierarchyPrintBean);                         
                        }
                    }
                }
          }
          catch(Exception ex)
          {
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex,"getAwardPrintHierarchy","getAwardHierarchy()");
          }
          awardHierarchyMap.put("AwardHierarchy", resultVector);
           return awardHierarchyMap;
           }

}
