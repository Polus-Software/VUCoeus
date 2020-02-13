/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.services;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.award.beans.AwardSummaryPrintBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author midhunmk
 */
public class GetAwardSummaryDetailsForPrint {

     public static GetAwardSummaryDetailsForPrint instance=null;

     public static GetAwardSummaryDetailsForPrint getInstance() {
        if (instance == null) {
            instance = new GetAwardSummaryDetailsForPrint();
        }
        return instance;
    }
     public  GetAwardSummaryDetailsForPrint(){

    }

      public Map getAwardSummaryDetails(HttpServletRequest request,String mitAwardNumber)
      {
          Map awardSummaryMap=new HashMap();
         Vector awardDetails=new CoeusVector();
          //to collect all the details from the database
          try{
                WebTxnBean txnBean=new WebTxnBean();
                HashMap parameter=new HashMap();
                parameter.put("awardNumber",mitAwardNumber);
                Hashtable tmp = (Hashtable) txnBean.getResults(request,"getAwardPrintSummary", parameter);
                awardDetails= (Vector) tmp.get("getAwardPrintSummary");
                if(awardDetails!=null&&awardDetails.size()>0)
                {
                AwardSummaryPrintBean awardSummaryPrintBean =(AwardSummaryPrintBean)awardDetails.get(0);
                //collect the rest of the information from the database.
                //investigator  details
                tmp = (Hashtable) txnBean.getResults(request,"getAwardPrintSummaryInvs", parameter);
                awardDetails= (Vector) tmp.get("getAwardPrintSummaryInvs");
                //the vector will contain the bean collection of AwardSummaryInvestigatorBean
                if(awardDetails!=null){awardSummaryPrintBean.setInvestigators(awardDetails);}

                //the keyperson details from the database.
                tmp = (Hashtable) txnBean.getResults(request,"getAwardPrintSummaryKeyperson", parameter);
                awardDetails= (Vector) tmp.get("getAwardPrintSummaryKeyperson");
                //the vector will contain the bean collection of AwardSummaryInvestigatorBean
                if(awardDetails!=null){awardSummaryPrintBean.setKeypersons(awardDetails);}

                //to collect all the unit list details
                tmp = (Hashtable) txnBean.getResults(request,"getAwardPrintSummaryUnits", parameter);
                awardDetails= (Vector) tmp.get("getAwardPrintSummaryUnits");
                //the vector will contain the bean collection of AwardSummaryInvestigatorBean
                if(awardDetails!=null){awardSummaryPrintBean.setUnits(awardDetails);}
                //load to the hash map
                awardSummaryMap.put("AwardDetails", awardSummaryPrintBean);
                }//end of if condition
                else
                    awardSummaryMap=null;
          }
           catch(Exception e){
            e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"GetAwardForPrint","getAwardSummaryDetails()");

        }
        
          return awardSummaryMap;
      }

}
