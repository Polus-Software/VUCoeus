/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.schema.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.ReportBaseStream;
import edu.mit.coeuslite.award.beans.AwardHierarchyPrintBean;
import edu.mit.coeuslite.award.print.awardhierarchy.AwardDetails;
import edu.mit.coeuslite.award.print.awardhierarchy.AwardRootNode;
import edu.mit.coeuslite.award.print.awardhierarchy.impl.AwardDetailsImpl;
import edu.mit.coeuslite.award.print.awardhierarchy.impl.AwardRootNodeImpl;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;


/**
 *
 * @author midhunmk
 */
public class AwardHierarchyStream extends ReportBaseStream{

 @Override
    public Object getObjectStream(Hashtable params) throws DBException, CoeusException {
       return getObjectStreamDocuument(params);
    }
 

    private Object getObjectStreamDocuument(Hashtable params) {
        
        boolean parentAwardFlag=true;
        HashMap awardHierarchy= (HashMap)params.get("AwardHierarchy");
        Vector listOfAwards=(Vector)awardHierarchy.get("AwardHierarchy");
        AwardHierarchyPrintBean awardHierarchyPrintBean;
        AwardRootNode awardRootNode=new AwardRootNodeImpl();
        AwardDetails awardDetails;
        awardRootNode.setAwardDate(new GregorianCalendar());
        java.util.Calendar check1=new GregorianCalendar();
        if(listOfAwards!=null&&listOfAwards.size()>0){
            for (Iterator <AwardHierarchyPrintBean>it = listOfAwards.iterator();it.hasNext();) {
                awardHierarchyPrintBean = it.next();
                if(parentAwardFlag){
                    if(awardHierarchyPrintBean.getAccountNumber()!=null){
                        awardRootNode.setMITAccountNumber(awardHierarchyPrintBean.getAccountNumber());}
                    if(awardHierarchyPrintBean.getPIName()!=null){
                        awardRootNode.setAwardPI(awardHierarchyPrintBean.getPIName());}    
                    parentAwardFlag=false;
                }
                awardDetails=new AwardDetailsImpl();
                if(awardHierarchyPrintBean.getAccountNumber()!=null){
                awardDetails.setAccountNumber(awardHierarchyPrintBean.getAccountNumber());}
                if(awardHierarchyPrintBean.getAccountTypeDesc()!=null){
                awardDetails.setAccountType(awardHierarchyPrintBean.getAccountTypeDesc());}
                awardDetails.setAwardNumber(awardHierarchyPrintBean.getMitAwardNumber());
                if(awardHierarchyPrintBean.getFinalExpDate()!=null){
                    check1.setTime(awardHierarchyPrintBean.getFinalExpDate());
                    awardDetails.setFinalExpirationDate(check1);
                }
                if(awardHierarchyPrintBean.getLeadUnit()!=null){
                awardDetails.setLeadUnit(awardHierarchyPrintBean.getLeadUnit());}
                if(awardHierarchyPrintBean.getPIName()!=null){
                awardDetails.setPI(awardHierarchyPrintBean.getPIName());}
                
                awardDetails.setSponsorCode(awardHierarchyPrintBean.getSponsorCode());
                awardDetails.setStatus(awardHierarchyPrintBean.getStatusDesc());
                if(awardHierarchyPrintBean.getTotalAntiAmount()!=null){
                awardDetails.setTotalAnticipated(awardHierarchyPrintBean.getTotalAntiAmount());}
                if(awardHierarchyPrintBean.getTotalObliAmount()!=null){
                awardDetails.setTotalObligated(awardHierarchyPrintBean.getTotalObliAmount());}
                
               
                for(int i=1;i<awardHierarchyPrintBean.getLevelNumber();i++)
                    {awardDetails.getAwardHierarchy().add(" ");}
                                                
                awardRootNode.getAwardItems().add(awardDetails);
            }
       
          }
        return awardRootNode;


}

  
}
