/*
 * XMLFormatter.java
 *
 * Created on January 6, 2005, 11:06 AM
 */

package edu.mit.coeus.s2s.v2.util;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.CFDADetailsBean;
import edu.mit.coeus.s2s.bean.DBOpportunityInfoBean;
import edu.mit.coeus.s2s.bean.FormInfoBean;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.s2s.validator.BindingFileReader;
import edu.mit.coeus.s2s.validator.BindingInfoBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.services.applicantwebservices_v2.GetOpportunitiesResponse;
import gov.grants.apply.services.applicantwebservices_v2.GetOpportunityListResponse;
import gov.grants.apply.system.applicantcommonelements_v1.OpportunityDetails;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author  geot
 */
public class Converter {
    
    /** Creates a new instance of XMLFormatter */
    private Converter() {
    }
    public synchronized static byte[] doc2bytes(Document node)
    throws CoeusException{
        return doc2String(node).getBytes();
    }
    public synchronized static String doc2String(Document node)
    throws CoeusException{
        try {
            //            Element root = node.getDocumentElement();
            //            return root.toString();
            DOMSource domSource = new DOMSource(node);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"S2SValidator", "doc2bytes");
            throw new CoeusException(e.getMessage());
        }
    }
    public synchronized static ArrayList convert2ArrayList(GetOpportunitiesResponse resList){
        if(resList==null || resList.getOpportunityInfo()==null || resList.getOpportunityInfo().isEmpty()) return null;
        int size= resList.getOpportunityInfo().size();
        List<GetOpportunitiesResponse.OpportunityInfo> oppList = resList.getOpportunityInfo();
        ArrayList convList = null;
        for(int i=0;i<size;i++){
            GetOpportunitiesResponse.OpportunityInfo opp = oppList.get(i);
            if(convList == null) convList = new ArrayList();
            convList.add(convert2OpportunityInfoBean(opp));
        }
        return convList;
    }
    public synchronized static ArrayList convert2ArrayList(GetOpportunityListResponse resList) {
        if (resList == null || resList.getOpportunityDetails() == null || resList.getOpportunityDetails().isEmpty()) {
            return null;
        }
        int size = resList.getOpportunityDetails().size();
        List<OpportunityDetails> oppList = resList.getOpportunityDetails();
        ArrayList convList = null;
        for (int i = 0; i < size; i++) {
            OpportunityDetails opp = oppList.get(i);
            if (convList == null) {
                convList = new ArrayList();
            }
            convList.add(convert2OpportunityInfoBean(opp));
        }
        return convList;
    }

    public synchronized static OpportunityInfoBean convert2OpportunityInfoBean(OpportunityDetails opportunityDetails) {
        DBOpportunityInfoBean oppInfoBean = new DBOpportunityInfoBean();
        List<CFDADetailsBean> cFDADetailsList = getCFDADetails(opportunityDetails);
        oppInfoBean.setCfdaDetailsList(cFDADetailsList);
        if(cFDADetailsList != null && !cFDADetailsList.isEmpty()){
            oppInfoBean.setCfdaNumber(toStrIfNotNull(cFDADetailsList.get(0).getNumber()));
        }
        oppInfoBean.setClosingDate(opportunityDetails.getClosingDate() == null ? null
                : new java.sql.Timestamp(opportunityDetails.getClosingDate().toGregorianCalendar().getTimeInMillis()));
        oppInfoBean.setCompetitionId(toStrIfNotNull(opportunityDetails.getCompetitionID()));
        oppInfoBean.setInstructionUrl(toStrIfNotNull(opportunityDetails.getInstructionsURL()));
        oppInfoBean.setOpeningDate(opportunityDetails.getOpeningDate() == null ? null
                : new java.sql.Timestamp(opportunityDetails.getOpeningDate().toGregorianCalendar().getTimeInMillis()));
        oppInfoBean.setOpportunityId(toStrIfNotNull(opportunityDetails.getFundingOpportunityNumber()));
        oppInfoBean.setOpportunityTitle(toStrIfNotNull(opportunityDetails.getFundingOpportunityTitle()));
        oppInfoBean.setSchemaUrl(toStrIfNotNull(opportunityDetails.getSchemaURL()));        
        oppInfoBean.setPackageID(toStrIfNotNull(opportunityDetails.getPackageID()));
        oppInfoBean.setCompetitionTitle(toStrIfNotNull(opportunityDetails.getCompetitionTitle()));
        oppInfoBean.setOfferingAgency(toStrIfNotNull(opportunityDetails.getOfferingAgency()));
        oppInfoBean.setAgencyContactInfo(toStrIfNotNull(opportunityDetails.getAgencyContactInfo()));        
        oppInfoBean.setIsMultiProject(opportunityDetails.isIsMultiProject());
        return oppInfoBean;
    }
    private static List<CFDADetailsBean> getCFDADetails(OpportunityDetails opportunityDetails){   
        List<CFDADetailsBean> liCFDADetailsBean = new ArrayList<CFDADetailsBean>();
        CFDADetailsBean cfdaDetailsBean = new CFDADetailsBean();
        if(opportunityDetails == null){
            return liCFDADetailsBean;
        }
        if(opportunityDetails.getCFDADetails() == null || opportunityDetails.getCFDADetails().isEmpty()){
            return liCFDADetailsBean;
        }
        for(int index = 0; index < opportunityDetails.getCFDADetails().size() ; index++){
            cfdaDetailsBean.setNumber(opportunityDetails.getCFDADetails().get(index).getNumber());
            cfdaDetailsBean.setTitle(opportunityDetails.getCFDADetails().get(index).getTitle());
            liCFDADetailsBean.add(cfdaDetailsBean);
        }        
        return liCFDADetailsBean;
    }
    private static String toStrIfNotNull(Object obj){
        return obj==null?null:obj.toString();
    }
    public synchronized static OpportunityInfoBean convert2OpportunityInfoBean(GetOpportunitiesResponse.OpportunityInfo oppInfoType){
        DBOpportunityInfoBean oppInfoBean = new DBOpportunityInfoBean();
        oppInfoBean.setCfdaNumber(toStrIfNotNull(oppInfoType.getCFDANumber()));
        oppInfoBean.setClosingDate(oppInfoType.getClosingDate()==null?null:
            new java.sql.Timestamp(oppInfoType.getClosingDate().toGregorianCalendar().getTimeInMillis()));
            oppInfoBean.setCompetitionId(toStrIfNotNull(oppInfoType.getCompetitionID()));
            oppInfoBean.setInstructionUrl(toStrIfNotNull(oppInfoType.getInstructionsURL()));
            oppInfoBean.setOpeningDate(oppInfoType.getOpeningDate()==null?null:
                new java.sql.Timestamp(oppInfoType.getOpeningDate().toGregorianCalendar().getTimeInMillis()));
                oppInfoBean.setOpportunityId(toStrIfNotNull(oppInfoType.getFundingOpportunityNumber()));
                oppInfoBean.setOpportunityTitle(toStrIfNotNull(oppInfoType.getFundingOpportunityTitle()));
                oppInfoBean.setSchemaUrl(toStrIfNotNull(oppInfoType.getSchemaURL()));
                return oppInfoBean;
    }
    public static Timestamp convertCal2Timestamp(Calendar cal){
//                UtilFactory.log("Timezone from Grants.gov Webservice => "+cal.getTimeZone().toString());
//                UtilFactory.log("Date "+cal.get(Calendar.YEAR)+
//                                                "-"+(cal.get(Calendar.MONTH)+1)+
//                                                "-"+cal.get(Calendar.DATE)+
//                                                " "+cal.get(Calendar.HOUR)+
//                                                ":"+cal.get(Calendar.MINUTE)+
//                                                ":"+cal.get(Calendar.SECOND)+
//                                                "."+cal.get(Calendar.MILLISECOND));
                
        
        return cal==null?null:Timestamp.valueOf(cal.get(Calendar.YEAR)+
        "-"+appendZero((cal.get(Calendar.MONTH)+1))+
        "-"+appendZero(cal.get(Calendar.DATE))+
        " "+appendZero(cal.get(Calendar.HOUR_OF_DAY))+
        ":"+appendZero(cal.get(Calendar.MINUTE))+
        ":"+appendZero(cal.get(Calendar.SECOND))+
        "."+cal.get(Calendar.MILLISECOND));
    }
    public synchronized static Document string2Dom(String xmlSource)
    throws CoeusException {
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlSource)));
        }catch(SAXException ex){
            UtilFactory.log(ex.getMessage(),ex,"Converter","string2Dom");
            throw new CoeusException(ex.getMessage());
        }catch(ParserConfigurationException ex){
            UtilFactory.log(ex.getMessage(),ex,"Converter","string2Dom");
            throw new CoeusException(ex.getMessage());
        }catch(IOException ex){
            UtilFactory.log(ex.getMessage(),ex,"Converter","string2Dom");
            throw new CoeusException(ex.getMessage());
        }
    }
    public synchronized static Document node2Dom(org.w3c.dom.Node n)
    throws CoeusException {
        try{
            javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer xf = tf.newTransformer();
            javax.xml.transform.dom.DOMResult dr = new javax.xml.transform.dom.DOMResult();
            xf.transform(new javax.xml.transform.dom.DOMSource(n),dr);
            return (Document)dr.getNode();
        }catch(javax.xml.transform.TransformerException ex){
            UtilFactory.log(ex.getMessage(),ex,"Converter","string2Dom");
            throw new CoeusException(ex.getMessage());
        }
    }
    public synchronized static String replaceAll(String str,String strToBeReplaced, String replacement){
        // Create a pattern to match cat
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(strToBeReplaced);
        // Create a matcher with an input string
        java.util.regex.Matcher m = p.matcher(str);
        return m.replaceAll(replacement);
    }
    
    public synchronized static Vector sortForms(List formInfoBeans) throws CoeusException{
        CoeusVector cvForms = new CoeusVector();
        int size = formInfoBeans.size();
        Hashtable bind = BindingFileReader.getBindings();
        for(int i=0;i<size;i++){
            FormInfoBean frm = (FormInfoBean)formInfoBeans.get(i);
            BindingInfoBean bindInfo = (BindingInfoBean)bind.get(frm.getNs());
            if(bindInfo!=null) frm.setSortIndex(bindInfo.getSortIndex());
            cvForms.add(frm);
        }
        String[] srtArr = {"sortIndex","available","include"};
        boolean sorted = cvForms.sort(srtArr,true);
        return cvForms;
    }
    public synchronized static String replaceAllCgdNS(Document doc) throws CoeusException{
        return replaceAllCgdNS(doc,null);
    }
    public synchronized static String replaceAllCgdNS(Document doc,List nsList) throws CoeusException{
        
        String tempAppXml = doc2String(doc);
        Hashtable bindings = BindingFileReader.getBindings();
        Enumeration en = bindings.elements();
        Iterator it = nsList==null?null:nsList.iterator();
        StringBuffer strbfr = new StringBuffer(tempAppXml);
        String appXml = tempAppXml;
        while((nsList==null && en.hasMoreElements())||(nsList!=null&&it.hasNext())){
            BindingInfoBean bindInfo = nsList==null?(BindingInfoBean)en.nextElement():
                                                (BindingInfoBean)bindings.get(it.next());
            if(bindInfo!=null && bindInfo.isNsChanged()){
                int in = tempAppXml.indexOf(bindInfo.getCgdNameSpace());
                if(in!=-1)
                    tempAppXml = Converter.replaceAll(tempAppXml,
                    "\""+bindInfo.getCgdNameSpace()+"\"",
                    "\""+bindInfo.getNameSpace()+"\"");
            }
        }
        tempAppXml = replaceAll(tempAppXml,"\\+00:00","");
        tempAppXml = replaceAll(tempAppXml,"\\"+getLocalTimeZone(),"");
        return tempAppXml;
    }
    private static String getLocalTimeZone(){
        String localTimeZoneId; 
        try {
            localTimeZoneId = CoeusProperties.getProperty(CoeusPropertyKeys.LOCAL_TIMEZONE_ID);
        } catch (IOException ex) {
            localTimeZoneId = "America/New_York";
        }
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(localTimeZoneId));
        int dstOffsetMilli = cal.get(Calendar.DST_OFFSET);
        int zoneOffsetMilli = cal.get(Calendar.ZONE_OFFSET);
        zoneOffsetMilli = cal.getTimeZone().useDaylightTime()?zoneOffsetMilli+dstOffsetMilli:zoneOffsetMilli;
        int zoneOffset = zoneOffsetMilli/(1000*60*60);
        String timezoneId = TimeZone.getTimeZone("GMT"+zoneOffset).getID();
        String offset = timezoneId.substring(timezoneId.length()-6);
        return offset;
    }
    public static void main(String args[]){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Denver")); 
        int dstOffsetMilli = cal.get(Calendar.DST_OFFSET);
        int zoneOffsetMilli = cal.get(Calendar.ZONE_OFFSET);
        zoneOffsetMilli = cal.getTimeZone().useDaylightTime()?zoneOffsetMilli+dstOffsetMilli:zoneOffsetMilli;
        int zoneOffset = zoneOffsetMilli/(1000*60*60);
        String timezoneId = TimeZone.getTimeZone("GMT"+zoneOffset).getID();
        String offset = timezoneId.substring(timezoneId.length()-6);
        System.out.println(timezoneId+" "+offset);
    }
    private static String appendZero(int i) {
        return i<10?"0"+i:""+i;
    }
    
}
