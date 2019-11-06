/*
 * AwardReportingReqStream.java
 *
 * Created on November 4, 2005, 8:38 AM
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.award.bean.AwardReportReqBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.xml.bean.reportingReq.*;
import java.util.*;
import javax.xml.bind.JAXBException;

/**
 *
 * @author  jenlu
 */
public class AwardReportingReqStream extends ReportBaseStream{
    private ObjectFactory objFactory;
    private CoeusXMLGenrator xmlGenerator;
    private int selRow;
    private CoeusVector cvTableData;
    private CoeusVector cvGRPData;
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.reportingReq";
 
    
    /** Creates a new instance of AwardReportingReqStream */
    public AwardReportingReqStream() {
        objFactory = new ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();
    }
    
    public org.w3c.dom.Document getStream(Hashtable params) throws CoeusException {
        selRow = Integer.parseInt(params.get("SEL_ROW").toString());
        cvGRPData = (CoeusVector)params.get("GRP");
        cvTableData = (CoeusVector)params.get("DATA");
        AwardReportingRequirementType  reportReqType = getAwardReportingRequirement();
        return xmlGenerator.marshelObject(reportReqType,packageName);
    }
    
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        selRow = Integer.parseInt(params.get("SEL_ROW").toString());
        cvGRPData = (CoeusVector)params.get("GRP");
        cvTableData = (CoeusVector)params.get("DATA");
        AwardReportingRequirementType  reportReqType = getAwardReportingRequirement();
        return reportReqType;
    }
    
    private AwardReportingRequirementType getAwardReportingRequirement()throws CoeusXMLException{
        AwardReportingRequirementType awardReportingRequirementType = null;
        try{
            awardReportingRequirementType =  objFactory.createAwardReportingRequirement();
            awardReportingRequirementType.getReportingReqs().addAll(getReportingRequirements());
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());
            awardReportingRequirementType.setCurrentDate(currentDate);
        
        }catch(JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"AwardReportingReqStream","getStream()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return awardReportingRequirementType;
    }
    private Vector getReportingRequirements()throws CoeusXMLException{
        Vector vcReportingReqs = new Vector();
        AwardReportReqBean currentBean;
        ReportingRequirement reportingRequirement;
        int groupSize;
        if (selRow >= 0 ) {
            groupSize = selRow + 1;
        }else{ 
            groupSize = cvGRPData==null?0:cvGRPData.size();
            selRow = 0;    
        }
        try{
        for(int index = selRow ; index < groupSize; index++){
            reportingRequirement = objFactory.createReportingRequirement();
            currentBean = (AwardReportReqBean)cvGRPData.get(index);
            reportingRequirement.setPrincipleInvestigatorName(UtilFactory.convertNull(currentBean.getPrincipleInvestigator()));
            reportingRequirement.setReportClass(UtilFactory.convertNull(currentBean.getReportClassDescription()));
            reportingRequirement.setReportType(UtilFactory.convertNull(currentBean.getReportDescription()));
            reportingRequirement.setFrequency(UtilFactory.convertNull(currentBean.getFrequencyDescription()));
            reportingRequirement.setFrequencyBase(UtilFactory.convertNull(currentBean.getFrequencyBaseDescription()));
            reportingRequirement.setCopyOSP(UtilFactory.convertNull(currentBean.getOspDistributionDescription()));
            reportingRequirement.setBaseDate(UtilFactory.convertNull(currentBean.getFrequencyBaseDate()));
            
            Equals equalsPrincipleInvestigator,equalsReportClass,equalsReportType,equalsFrequency,
                   equalsFrequencyBase,equalsStatus,equalsBaseDate,equalsCopyOSP;
            equalsPrincipleInvestigator = new Equals("principleInvestigator",currentBean.getPrincipleInvestigator());
            equalsReportClass  = new Equals("reportClassDescription",currentBean.getReportClassDescription());
            equalsReportType  = new Equals("reportDescription",currentBean.getReportDescription());
            equalsFrequency = new Equals("frequencyDescription",currentBean.getFrequencyDescription());
            equalsFrequencyBase = new Equals("frequencyBaseDescription",currentBean.getFrequencyBaseDescription());
            equalsBaseDate = new Equals("frequencyBaseDate",currentBean.getFrequencyBaseDate());
            equalsCopyOSP = new Equals("ospDistributionDescription",currentBean.getOspDistributionDescription());
            And eqAll = new And(new And(new And(new And(equalsPrincipleInvestigator, equalsReportClass),new And(equalsReportType, 
                         equalsFrequency)), new And(equalsFrequencyBase,equalsBaseDate)),equalsCopyOSP);            

            CoeusVector cvDetailData;
            int detailSize;
            cvDetailData = cvTableData.filter(eqAll);
            detailSize = cvDetailData==null?0:cvDetailData.size();
            Vector vcReportingDetails = new Vector();
            AwardReportReqBean currentDetailBean;
            for (int detailIndex = 0 ; detailIndex < detailSize; detailIndex++){
                ReportingRequirementDetail reportingRequirementDetail = objFactory.createReportingRequirementDetail();
                currentDetailBean = (AwardReportReqBean)cvDetailData.get(detailIndex);
                reportingRequirementDetail.setAwardNo(UtilFactory.convertNull(currentDetailBean.getMitAwardNumber()));
                reportingRequirementDetail.setUnitNo(UtilFactory.convertNull(currentDetailBean.getUnitNumber()));
                reportingRequirementDetail.setUnitName(UtilFactory.convertNull(currentDetailBean.getUnitName()));
                reportingRequirementDetail.setStatus(UtilFactory.convertNull(currentDetailBean.getReportStatusDescription()));
                reportingRequirementDetail.setContact(UtilFactory.convertNull(currentDetailBean.getContact()));
                reportingRequirementDetail.setAddress(UtilFactory.convertNull(currentDetailBean.getAddress()));
                reportingRequirementDetail.setCopies(currentDetailBean.getNumberOfCopies());
                reportingRequirementDetail.setOverdueNo(currentDetailBean.getOverdueCounter());
                reportingRequirementDetail.setComments(UtilFactory.convertNull(currentDetailBean.getComments()));
                reportingRequirementDetail.setPersonName(UtilFactory.convertNull(currentDetailBean.getFullName()));
                reportingRequirementDetail.setDueDate(UtilFactory.convertNull(currentDetailBean.getStringDueDate()));
                reportingRequirementDetail.setActivityDate(UtilFactory.convertNull(currentDetailBean.getStringActivityDate()));
                
                vcReportingDetails.addElement(reportingRequirementDetail);
            }
                reportingRequirement.getReportingReqDetails().addAll(vcReportingDetails);
                vcReportingReqs.addElement(reportingRequirement);    
        }
        }catch(JAXBException jaxbEx){
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"AwardReportingReqStream","getStream()");
            throw new CoeusXMLException(jaxbEx.getMessage());
        }
        return vcReportingReqs;
    }

}
