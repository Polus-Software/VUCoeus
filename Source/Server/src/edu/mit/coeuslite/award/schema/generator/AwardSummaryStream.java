/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.schema.generator;

import com.lowagie.text.Document;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.ReportBaseStream;
import edu.mit.coeus.xml.generator.ReportReaderConstants;
import edu.mit.coeuslite.award.beans.AwardSummaryInvestigatorBean;
import edu.mit.coeuslite.award.beans.AwardSummaryPrintBean;
import edu.mit.coeuslite.award.beans.AwardSummaryUnitBean;
import edu.mit.coeuslite.award.print.awardsummary.AwardSummaryRoot;
import edu.mit.coeuslite.award.print.awardsummary.AwardSummaryRootType.AnticipatedAmountType;
import edu.mit.coeuslite.award.print.awardsummary.AwardSummaryRootType.AwardEffectiveDateType;
import edu.mit.coeuslite.award.print.awardsummary.AwardSummaryRootType.FinalExpirationDateType;
import edu.mit.coeuslite.award.print.awardsummary.AwardSummaryRootType.ObligatedAmountType;
import edu.mit.coeuslite.award.print.awardsummary.AwardSummaryRootType.ObligationEffectiveDateType;
import edu.mit.coeuslite.award.print.awardsummary.AwardSummaryRootType.ObligationExpirationDateType;
import edu.mit.coeuslite.award.print.awardsummary.impl.AwardSummaryRootImpl;
import edu.mit.coeuslite.award.print.awardsummary.impl.AwardSummaryRootTypeImpl.AnticipatedAmountTypeImpl;
import edu.mit.coeuslite.award.print.awardsummary.impl.AwardSummaryRootTypeImpl.AwardEffectiveDateTypeImpl;
import edu.mit.coeuslite.award.print.awardsummary.impl.AwardSummaryRootTypeImpl.FinalExpirationDateTypeImpl;
import edu.mit.coeuslite.award.print.awardsummary.impl.AwardSummaryRootTypeImpl.ObligatedAmountTypeImpl;
import edu.mit.coeuslite.award.print.awardsummary.impl.AwardSummaryRootTypeImpl.ObligationEffectiveDateTypeImpl;
import edu.mit.coeuslite.award.print.awardsummary.impl.AwardSummaryRootTypeImpl.ObligationExpirationDateTypeImpl;
import edu.mit.coeuslite.award.print.awardsummary.Investigatortype;
import edu.mit.coeuslite.award.print.awardsummary.impl.InvestigatortypeImpl;
import edu.mit.coeuslite.award.print.awardsummary.Unittype;
import edu.mit.coeuslite.award.print.awardsummary.impl.UnittypeImpl;
import edu.mit.coeuslite.award.services.GetAwardSummaryDetailsForPrint;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import org.apache.axis.types.PositiveInteger;

/**
 *
 * @author midhunmk
 */
public class AwardSummaryStream extends ReportBaseStream{
 @Override
    public Object getObjectStream(Hashtable params) throws DBException, CoeusException {
       return getObjectStreamDocuument(params);
    }


 
    
   // @Override
  //  public Document getStream(Hashtable params) throws DBException, CoeusException {
   //     return (Document) super.getStream(params);

   // }


    private Object getObjectStreamDocuument(Hashtable params){
        //String personID = (String) params.get(ReportReaderConstants.PERSONID);
        
        Map awardSummaryMap=(HashMap)params.get("AwardDetails");
        AwardSummaryRoot awardSummaryRoot=new AwardSummaryRootImpl();

try
{

           AwardSummaryPrintBean awardSummary;
            awardSummary=(AwardSummaryPrintBean)(awardSummaryMap.get("AwardDetails"));
            //get all the details from the bean an distribute using the jaxb class ....

            if(awardSummary!=null)
            {
                String amtValue;
                awardSummaryRoot.setAccountNumber(awardSummary.getAccountNumber());
                awardSummaryRoot.setAccountType(awardSummary.getAccountType());
                awardSummaryRoot.setActivityType(awardSummary.getActivityType());
                awardSummaryRoot.setAwardNumber(awardSummary.getMitAwardNumber());
                awardSummaryRoot.setAwardType(awardSummary.getAwardType());
                String text=" matching records";
                if(awardSummary.getSposorAwardNumber()!=null)
                {text=" ("+String.valueOf(awardSummary.getSpsrAwdNumcount())+text+")";
                text=awardSummary.getSposorAwardNumber()+text;
                }
                else
                {text="No Details";}
                awardSummaryRoot.setSponsorAwardNumber(text);
                awardSummaryRoot.setSponsorName(awardSummary.getSponsorName());
                awardSummaryRoot.setSponsorNumber(awardSummary.getSponsorNumber());
                awardSummaryRoot.setStatus(awardSummary.getAwardStatus());
                awardSummaryRoot.setTitle(awardSummary.getAwardTitle());

                awardSummaryRoot.setApprovedEquipmentFlag(
                        ((awardSummary.getApprovedEquipment()).compareTo("N0")==0)? false:true);
                awardSummaryRoot.setApprovedForeignTripFlag(
                        ((awardSummary.getApprovedForeginTrip()).compareTo("N0")==0)? false:true);
                awardSummaryRoot.setApprovedSubContractFlag(
                        ((awardSummary.getApprovedSubContract()).compareTo("N0")==0)? false:true);
                awardSummaryRoot.setCostSharingFlag(
                        ((awardSummary.getCostSharing()).compareTo("N0")==0)? false:true);
                awardSummaryRoot.setIndirectCostFlag(
                        ((awardSummary.getIndirectCost()).compareTo("N0")==0)? false:true);
                awardSummaryRoot.setPaymentScheduleFlag(
                        ((awardSummary.getPaymentSchedule()).compareTo("N0")==0)? false:true);
                awardSummaryRoot.setTransferSponsorFlag(
                        ((awardSummary.getTransferSponsor()).compareTo("N0")==0)? false:true);
                java.util.Calendar check1=new GregorianCalendar();
                java.util.Calendar check2=new GregorianCalendar();
                java.util.Calendar check3=new GregorianCalendar();
                java.util.Calendar check4=new GregorianCalendar();

               if(awardSummary.getFinalExpirationDates()!=null)
               {//checking the null
               check1.setTime(awardSummary.getFinalExpirationDates());
               FinalExpirationDateType dateType1=new FinalExpirationDateTypeImpl();
               dateType1.getDate().add(check1);
               awardSummaryRoot.setFinalExpirationDate(dateType1);
               }
                 if(awardSummary.getObligationEffectiveDates()!=null)
               {//checking the null
               ObligationEffectiveDateType dateType2=new ObligationEffectiveDateTypeImpl();
               check2.setTime(awardSummary.getObligationEffectiveDates());
               dateType2.getDate().add(check2);
               awardSummaryRoot.setObligationEffectiveDate(dateType2);
                 }
                 if(awardSummary.getObligationExpirationDates()!=null)
               {//checking the null
               ObligationExpirationDateType dateType3=new ObligationExpirationDateTypeImpl();
               check3.setTime(awardSummary.getObligationExpirationDates());
               dateType3.getDate().add(check3);
               awardSummaryRoot.setObligationExpirationDate(dateType3);
                 }
                 if(awardSummary.getAwardEffectiveDates()!=null)
               {//checking the null
               AwardEffectiveDateType dateType4=new AwardEffectiveDateTypeImpl();
               check4.setTime(awardSummary.getAwardEffectiveDates());
               dateType4.getDate().add(check4);
               awardSummaryRoot.setAwardEffectiveDate(dateType4);
                 }
                if(awardSummary.getAnticipatedAmounts()!=null)
                {
                 amtValue= java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(awardSummary.getAnticipatedAmounts());
              AnticipatedAmountType amtType1=new AnticipatedAmountTypeImpl();
               amtType1.getAmount().add(amtValue);
               awardSummaryRoot.setAnticipatedAmount(amtType1);
                }
                 if(awardSummary.getObligatedAmounts()!=null)
                {
                   amtValue= java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(awardSummary.getObligatedAmounts());
               ObligatedAmountType amtType2=new ObligatedAmountTypeImpl();
               amtType2.getAmount().add(amtValue);
               awardSummaryRoot.setObligatedAmount(amtType2);
                 }

                 if(awardSummary.getObligatedTotalDirect()!=null)
                {amtValue= java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(awardSummary.getObligatedTotalDirect());
                    awardSummaryRoot.setObligatedTotalDirect(amtValue);}

                if(awardSummary.getObligatedTotalIndirect()!=null)
                {amtValue= java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(awardSummary.getObligatedTotalIndirect());
                    awardSummaryRoot.setObligatedTotalIndirect(amtValue);}

                if(awardSummary.getObligatedDistributableAmount()!=null)
                {amtValue= java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(awardSummary.getObligatedDistributableAmount());
                 awardSummaryRoot.setObligatedDistributableAmount(amtValue);}

                if(awardSummary.getAnitcipatedTotalDirect()!=null)
                {amtValue= java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(awardSummary.getObligatedDistributableAmount());
                 awardSummaryRoot.setAnticipatedTotalDirect(amtValue);}

                if(awardSummary.getAnitcipatedTotalIndirect()!=null)
                {amtValue= java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(awardSummary.getObligatedDistributableAmount());
                 awardSummaryRoot.setAnticipatedTotalIndirect(amtValue);}

                if(awardSummary.getAnitcipatedDistributableAmount()!=null)
                {amtValue= java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US).format(awardSummary.getObligatedDistributableAmount());
                 awardSummaryRoot.setAnticipatedDistributableAmount(amtValue);}


              String personId="",unitid="";
               Investigatortype invType;
               //iteration for the investigators.
               AwardSummaryInvestigatorBean awardSummaryInvestigatorBean;
               Vector investigators=awardSummary.getInvestigators();
               if(investigators!=null&&investigators.size()>0)//null check for the investigators
               {
                   for( int i=0;i<investigators.size();i++)
                   {
                       awardSummaryInvestigatorBean=(AwardSummaryInvestigatorBean)investigators.get(i);
                       if(awardSummaryInvestigatorBean!=null)//checking the data null
                       {
                           if((personId.compareTo(awardSummaryInvestigatorBean.getFullName())!=0))//remove duplication

                           {invType=new InvestigatortypeImpl();
                           personId=awardSummaryInvestigatorBean.getFullName();
                           invType.setInvestigatorName(awardSummaryInvestigatorBean.getFullName());
                           invType.setPIFlag(
                                  (((awardSummaryInvestigatorBean.getPiFlag()).compareTo("Y")==0)?true:false));
                           awardSummaryRoot.getInvestigators().add(invType);
                           }
                       }
                   }
               }// end of investigators list populating

                  //iteration for the keypserons.

               Vector keyPersons=awardSummary.getKeypersons();
               if(keyPersons!=null&&keyPersons.size()>0)//null check for the keypseron
               {
                   for( int i=0;i<keyPersons.size();i++)
                   {
                       awardSummaryInvestigatorBean=(AwardSummaryInvestigatorBean)keyPersons.get(i);
                       if(awardSummaryInvestigatorBean!=null)//checking the data null
                       {
                           if((personId.compareTo(awardSummaryInvestigatorBean.getFullName())!=0))//remove duplication

                           {invType=new InvestigatortypeImpl();
                           personId=awardSummaryInvestigatorBean.getFullName();
                           invType.setInvestigatorName(awardSummaryInvestigatorBean.getFullName());
                           invType.setPIFlag(
                                  (((awardSummaryInvestigatorBean.getPiFlag()).compareTo("Y")==0)?true:false));
                           awardSummaryRoot.getKeyPersons().add(invType);
                           }
                       }
                   }
               }// end of keyperson list populating

               //for the unit populate
              Unittype unitType;
              AwardSummaryUnitBean awardSummaryUnitBean;
              Vector units=awardSummary.getUnits();
              if(units!=null&&units.size()>0)
              {
                  for(int i=0;i<units.size();i++)
                  {
                      awardSummaryUnitBean=(AwardSummaryUnitBean)units.get(i);
                      if(awardSummaryUnitBean!=null)
                      {//checking the null
                          if(unitid.compareTo(awardSummaryUnitBean.getUnitNumber())!=0)
                          {//eliminating the duplication
                              unitid=awardSummaryUnitBean.getUnitNumber();
                              unitType=new UnittypeImpl();
                              unitType.setUnitNumber(unitid);
                              unitType.setLeadUnitFlag(
                                      ((awardSummaryUnitBean.getLeadFlag().compareTo("Y")==0)?true:false));
                              awardSummaryRoot.getUnits().add(unitType);
                          }
                      }
                  }//end of for loop

              }//end of outer if loop
               //end of unit populate


               
            }//end of if


}//end of try block

       catch(Exception e){
            e.printStackTrace();
        }



        return awardSummaryRoot;
    }
}
