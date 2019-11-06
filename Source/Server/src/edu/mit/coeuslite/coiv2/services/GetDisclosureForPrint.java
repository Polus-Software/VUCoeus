//

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.services;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.beans.CoiAnnualProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean;
import edu.mit.coeuslite.coiv2.formbeans.Coiv2Notes;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mr
 */
public class GetDisclosureForPrint {

    public static final String ENTITIES="ENTITIES";
    public static final String NOTES="NOTES";
    public static final String ATTACHMENTS="ATTACHMENTS";
    public static final String QUESTIONNAIRE="QUESTIONNAIRE";
    public static final String PROJECTS="PROJECTS";
    public static final String PERSON="PERSON";


    public static GetDisclosureForPrint instance=null;


     public static GetDisclosureForPrint getInstance() {
        if (instance == null) {
            instance = new GetDisclosureForPrint();
        }
        return instance;
    }

    public  GetDisclosureForPrint(){

    }

    public Map getADisclosure(HttpServletRequest request,String diclosureNumber,String sequenceNumber,String personID,int moduleCode)throws Exception{

        Map disclosureMap=new HashMap();
        CoiNotesService coiNotesService=CoiNotesService.getInstance();
        try{

            if(moduleCode==1){
               disclosureMap.put(ENTITIES, getawardProject(request,diclosureNumber,sequenceNumber,personID));
               // disclosureMap.put(PROJECTS, getawardProject(request,diclosureNumber,sequenceNumber,personID));
            }else if(moduleCode==2){
                disclosureMap.put(ENTITIES, getProposalProject(request,diclosureNumber,sequenceNumber,personID));
            }else if(moduleCode==3){
                disclosureMap.put(ENTITIES, getProtocolProject(request,diclosureNumber,sequenceNumber,personID));
            }else if(moduleCode==4){
                disclosureMap.put(ENTITIES, getIcProtocolProject(request,diclosureNumber,sequenceNumber,personID));
            }else if(moduleCode==7){
                disclosureMap.put(ENTITIES, getMiscellaneousProject(request,diclosureNumber,sequenceNumber,personID));
            }else if(moduleCode==5 || moduleCode==6){
                disclosureMap.put(ENTITIES, getAnnualProject(request,diclosureNumber,sequenceNumber,personID));
            }
            else if(moduleCode==8){
                  int seqNumber = Integer.parseInt(sequenceNumber);
                  String module=moduleCode+"";
                disclosureMap.put(ENTITIES, getAllTravel(request,diclosureNumber,seqNumber,module));
            }


            //adding reporter details
            //getPersonDetails(request,personID);
            disclosureMap.put(PERSON,getPersonDetails(request,personID));
            //adding reporter details

            Coiv2Notes coiNoteBean=new Coiv2Notes();
            coiNoteBean.setCoiDisclosureNumber(diclosureNumber);
            coiNoteBean.setCoiSequenceNumber(sequenceNumber);

            coiNotesService.getCoiNotesAsperCondition(coiNoteBean, request, null);
            disclosureMap.put(NOTES,request.getAttribute("disclosureNotesData"));


            CoiAttachmentService attachmentService=CoiAttachmentService.getInstance();
            disclosureMap.put(ATTACHMENTS, attachmentService.getUploadDocumentForDisclosure(diclosureNumber, Integer.parseInt(sequenceNumber)));
            disclosureMap.put(QUESTIONNAIRE, getQuestionnaire(request,diclosureNumber,sequenceNumber,personID));



        }catch(Exception e){
        e.printStackTrace();
        UtilFactory.log(e.getMessage(),e,"GetDisclosureForPrint","getADisclosure()");

        }

        return disclosureMap;
    }




         private Vector getProposalProject(HttpServletRequest request,String diclosureNumber,String sequenceNumber,String personID)throws Exception{

               Vector proposalProject=new Vector();
               Vector finEntDet=new Vector();
               Vector checkTitle=new Vector();
               try{
                WebTxnBean txnBean=new WebTxnBean();
                HashMap parameter=new HashMap();

                    Vector finEntDetIntegrated=new Vector();
                    Vector finEntDetNonIntegrated=new Vector();
                    parameter.put("coiDisclosureNumber",diclosureNumber);
                    parameter.put("sequenceNumber", new Integer(sequenceNumber));
                    parameter.put("personId", personID);

                 Hashtable finEntForDiscl = (Hashtable) txnBean.getResults(request,"getFinacialEntityForViewDiscl", parameter);
                 finEntDetIntegrated = (Vector) finEntForDiscl.get("getFinacialEntityForViewDiscl");
                 if(finEntDetIntegrated!= null && finEntDetIntegrated.size()>0){
                           checkTitle=getNoTitleBeanRemoved(finEntDetIntegrated);
                             if(checkTitle!=null && !checkTitle.isEmpty()){
                                              finEntDet.addAll(checkTitle);
                                              }
                           //finEntDet.addAll(finEntDetIntegrated);
                 }
                 checkTitle=new Vector();
                Hashtable finEntForDiscl1 = (Hashtable) txnBean.getResults(request, "getIntegratedFinacialViewEntity", parameter);
                finEntDetNonIntegrated  = (Vector) finEntForDiscl1.get("getIntegratedFinacialViewEntity");
                if(finEntDetNonIntegrated!= null && finEntDetNonIntegrated.size()>0)
                {
                           checkTitle=getNoTitleBeanRemoved(finEntDetNonIntegrated);

                             if(checkTitle!=null && !checkTitle.isEmpty()){
                   finEntDet.addAll(checkTitle);
               }
         //    finEntDet.addAll(finEntDetNonIntegrated);
           }
       // institute proposal start         
             checkTitle=new Vector();
               Hashtable htfinEntForInstProp= (Hashtable) txnBean.getResults(request, "getIntInstPropViewCoiv2", parameter);
               Vector  finEntForInstProp = (Vector) htfinEntForInstProp.get("getIntInstPropViewCoiv2"); 
                if(finEntForInstProp!= null && finEntForInstProp.size()>0)
                {
                           checkTitle=getNoTitleBeanRemoved(finEntForInstProp);
                           if(checkTitle!=null && !checkTitle.isEmpty()){
                             finEntDet.addAll(checkTitle);
                            }         
                }
       // institute proposal start     
                
        }catch(Exception e){
            e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"GetDisclosureForPrint","getProposalProject()");

        }
        return   finEntDet;
    }




    private  Vector getawardProject(HttpServletRequest request,String diclosureNumber,String sequenceNumber,String personID)throws Exception{
        Vector awardProject=new Vector();
        Vector checkTitle=new Vector();
         Vector finEntDet=new Vector();
        try{

            WebTxnBean txnBean=new WebTxnBean();
                HashMap parameter=new HashMap();
                parameter.put("coiDisclosureNumber",diclosureNumber);
                parameter.put("sequenceNumber", new Integer(sequenceNumber));
                parameter.put("personId", personID);
                Hashtable awards=(Hashtable)txnBean.getResults(request,"getFinacialEntityForAwardViewDiscl" , parameter);
                awardProject=(Vector)awards.get("getFinacialEntityForAwardViewDiscl");

                if(awardProject!= null && awardProject.size()>0){
                checkTitle=getNoTitleBeanRemoved(awardProject);
               if(checkTitle!=null && !checkTitle.isEmpty()){
                   finEntDet.addAll(checkTitle);
               }
                }
                
        }catch(Exception e){
         e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"GetDisclosureForPrint","getawardProject()");


        }
        return  finEntDet;
    }


  private Vector getPersonDetails(HttpServletRequest request,String personID)throws Exception{
      WebTxnBean webTxn=new WebTxnBean();
     Map  hmData = new HashMap();
      hmData.put("personId", personID);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

        }

      return personDatas;
  }

    private  Vector getProtocolProject(HttpServletRequest request,String diclosureNumber,String sequenceNumber,String personID)throws Exception{
        Vector protocolProject=new Vector();
        Vector checkTitle=new Vector();
         Vector finEntDet=new Vector();
        try{

             WebTxnBean txnBean=new WebTxnBean();
                HashMap parameter=new HashMap();
                parameter.put("coiDisclosureNumber",diclosureNumber);
                parameter.put("sequenceNumber", new Integer(sequenceNumber));
                parameter.put("personId", personID);
                Hashtable protocols=(Hashtable)txnBean.getResults(request,"getFinacialEntityForProtoViewDiscl" , parameter);
                protocolProject=(Vector)protocols.get("getFinacialEntityForProtoViewDiscl");

                if(protocolProject!= null && protocolProject.size()>0){
                checkTitle=getNoTitleBeanRemoved(protocolProject);
               if(checkTitle!=null && !checkTitle.isEmpty()){
                   finEntDet.addAll(checkTitle);
               }
                }
        }catch(Exception e){
       e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"GetDisclosureForPrint","getProtocolProject()");


        }
        return   finEntDet;
    }

    private  Vector getIcProtocolProject(HttpServletRequest request,String diclosureNumber,String sequenceNumber,String personID)throws Exception{
        Vector protocolProject=new Vector();
        Vector checkTitle=new Vector();
         Vector finEntDet=new Vector();
        try{

             WebTxnBean txnBean=new WebTxnBean();
                HashMap parameter=new HashMap();
                parameter.put("coiDisclosureNumber",diclosureNumber);
                parameter.put("sequenceNumber", new Integer(sequenceNumber));
                parameter.put("personId", personID);
                Hashtable protocols=(Hashtable)txnBean.getResults(request,"getFinacialEntityForICProtoViewDiscl" , parameter);
                protocolProject=(Vector)protocols.get("getFinacialEntityForICProtoViewDiscl");

                if(protocolProject!= null && protocolProject.size()>0){
                checkTitle=getNoTitleBeanRemoved(protocolProject);
               if(checkTitle!=null && !checkTitle.isEmpty()){
                   finEntDet.addAll(checkTitle);
               }
                }
        }catch(Exception e){
       e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"GetDisclosureForPrint","getIcProtocolProject()");


        }
        return   finEntDet;
    }



    private  Vector getMiscellaneousProject(HttpServletRequest request,String diclosureNumber,String sequenceNumber,String personID)throws Exception{

        Vector otherProject=new Vector();
        try{

            HashMap hmData = new HashMap();
            hmData.put("coiDisclosureNumber", diclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personID);

            WebTxnBean webTxn=new WebTxnBean();
            Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForOhterDiscl", hmData);
            otherProject = (Vector) finEntForDiscl.get("getFinacialEntityForOhterDiscl");

        }catch(Exception e){
         e.printStackTrace();
          UtilFactory.log(e.getMessage(),e,"GetDisclosureForPrint","getMiscellaneousProject()");


        }
        return   otherProject;
    }


 private  Vector getAnnualProject(HttpServletRequest request,String diclosureNumber,String sequenceNumber,String personID)throws Exception{

        Vector annualProject=new Vector();
        Vector proposalProject=new Vector();
        Vector checkTitle=new Vector();
        Vector finEntDet=new Vector();
        Vector finEntDetIntegrated=new Vector();
        Vector finEntDetNonIntegrated=new Vector();
        Vector finEntDetProto=new Vector();
        Vector finEntDetAward=new Vector();
        Vector finEntDetIcProto=new Vector();
        try{

            HashMap hmData = new HashMap();
            hmData.put("coiDisclosureNumber", diclosureNumber);
            hmData.put("sequenceNumber", Integer.parseInt(sequenceNumber));
            hmData.put("personId", personID);


            WebTxnBean webTxn=new WebTxnBean();
           
             Hashtable finEntForProposalDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForViewDiscl", hmData);
                finEntDetIntegrated = (Vector) finEntForProposalDiscl.get("getFinacialEntityForViewDiscl");
               if(finEntDetIntegrated!= null && finEntDetIntegrated.size()>0){

               checkTitle=getNoTitleBeanRemoved(finEntDetIntegrated);
               if(checkTitle!=null && !checkTitle.isEmpty()){
                   finEntDet.addAll(checkTitle);
               }
               }

               checkTitle=new Vector();

                Hashtable finEntForProposalDisclIntegrated = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialViewEntity", hmData);
                finEntDetNonIntegrated = (Vector) finEntForProposalDisclIntegrated.get("getIntegratedFinacialViewEntity");
                 if(finEntDetNonIntegrated!= null && finEntDetNonIntegrated.size()>0){
                     checkTitle=getNoTitleBeanRemoved(finEntDetNonIntegrated);

               if(checkTitle!=null && !checkTitle.isEmpty()){
                   finEntDet.addAll(checkTitle);
               }

                 }
            // Institute Proposal  start 
                 checkTitle=new Vector();
               Hashtable htfinEntForInstProp= (Hashtable) webTxn.getResults(request, "getIntInstPropViewCoiv2", hmData);
               Vector  finEntForInstProp = (Vector) htfinEntForInstProp.get("getIntInstPropViewCoiv2"); 
               if(finEntForInstProp!= null && finEntForInstProp.size()>0){
                   checkTitle=getNoTitleBeanRemoved(finEntForInstProp);
                    if(checkTitle!=null && !checkTitle.isEmpty()){
                      finEntDet.addAll(checkTitle);
                    }               
               }
             // Institute Proposal  end       
                 
                 
                //count = finEntDet.size();
                 checkTitle=new Vector();

                Hashtable finEntForProtoDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoViewDiscl", hmData);
                finEntDetProto = (Vector) finEntForProtoDiscl.get("getFinacialEntityForProtoViewDiscl");
                 if(finEntDetProto!= null && finEntDetProto.size()>0){
                     checkTitle=getNoTitleBeanRemoved(finEntDetProto);
                 if(checkTitle!=null && !checkTitle.isEmpty()){
                   finEntDet.addAll(checkTitle);
                 }
                                          //finEntDet.addAll(finEntDetProto);
                 }
                 checkTitle=new Vector();
                // count1 = finEntDetProto.size();
                Hashtable finEntForAwardDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardViewDiscl", hmData);
                finEntDetAward = (Vector) finEntForAwardDiscl.get("getFinacialEntityForAwardViewDiscl");
                if(finEntDetAward!= null && finEntDetAward.size()>0){
                    checkTitle=getNoTitleBeanRemoved(finEntDetAward);
                 if(checkTitle!=null && !checkTitle.isEmpty()){
                   finEntDet.addAll(checkTitle);
                 }
                           // finEntDet.addAll(finEntDetAward);
                }
               
                 checkTitle=new Vector();
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoViewDiscl", hmData);
                finEntDetIcProto = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoViewDiscl");
                 if(finEntDetIcProto!= null && finEntDetIcProto.size()>0){
                    checkTitle=getNoTitleBeanRemoved(finEntDetIcProto);
                 if(checkTitle!=null && !checkTitle.isEmpty()){
                   finEntDet.addAll(checkTitle);
                 }                           
                }




        }catch(Exception e){
         e.printStackTrace();
          UtilFactory.log(e.getMessage(),e,"GetDisclosureForPrint","getAnnualProject()");


        }
        return   finEntDet;
    }







    private  Vector getQuestionnaire(HttpServletRequest request,String diclosureNumber,String sequenceNumber,String personID)throws Exception{
        Vector questionnaire=new Vector();
        
        try{



             WebTxnBean txnBean=new WebTxnBean();
             HttpSession session=request.getSession();
                HashMap parameter=new HashMap();                
                parameter.put("coiDisclosureNumber",diclosureNumber);
                parameter.put("sequenceNumber", new Integer(sequenceNumber));
                parameter.put("personId", personID);
                session.removeAttribute("qnranswerd");
                Hashtable questions=(Hashtable)txnBean.getResults(request,"getQnsAns" , parameter);
                questionnaire=(Vector)questions.get("getQnsAns");   
               
        }catch(Exception e){


        }
        return   questionnaire;
    }

private Vector getNoTitleBeanRemoved(Vector lVector){

    Vector lSortedVector=new Vector();
    for (int i=0;i<lVector.size();i++){
        CoiAnnualProjectEntityDetailsBean coiProjectEntityDetailsBean = (CoiAnnualProjectEntityDetailsBean) lVector.get(i);
        if(coiProjectEntityDetailsBean!=null && coiProjectEntityDetailsBean.getCoiProjectTitle()!=null && !coiProjectEntityDetailsBean.getCoiProjectTitle().equals("")){
            lSortedVector.add(coiProjectEntityDetailsBean);
        }
    }

    return lSortedVector;
}
 private Vector getAllTravel(HttpServletRequest request,String disclosureNumber, Integer seqNumber,String moduleCode) throws Exception {
      HttpSession session = request.getSession();
      String projectCount="";
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();     
        Vector travelUserDet = null;
        int module=Integer.parseInt(moduleCode);
        try{
        if(module==8){
            int eventType=8;
        CoiProjectEntityDetailsBean coiDiscl = new CoiProjectEntityDetailsBean();
      
        hmData.put("disclosureNumber", disclosureNumber);
        request.setAttribute("disclosureNumber", disclosureNumber);

        hmData.put("sequenceNumber",seqNumber);
                request.setAttribute("sequenceNumber", seqNumber);

        hmData.put("eventType",eventType);
        Hashtable tvlUserRole = (Hashtable) webTxn.getResults(request, "getAllTvlData", hmData);
        travelUserDet = (Vector) tvlUserRole.get("getAllTvlData");
        request.setAttribute("DiscViewTravel", true);
         if (travelUserDet != null && travelUserDet.size() > 0) {             
             for(int i=0;i<travelUserDet.size();i++){
             coiDiscl = (CoiProjectEntityDetailsBean)travelUserDet.get(i);
             
             }             
            projectCount = projectCount + travelUserDet.size();
            request.setAttribute("travelList", travelUserDet);
            session.setAttribute("travelList", travelUserDet);
            
          }}}catch(Exception e){
              e.printStackTrace();
              UtilFactory.log(e.getMessage(),e,"GetDisclosureForPrint","getAllTravel()");
          }

      return travelUserDet;
      }


}
