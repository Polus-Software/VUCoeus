/*
 * SubAward10Stream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.budget.bean.BudgetSubAwardAttachmentBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardTxnBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.validator.BindingFileReader;
import edu.mit.coeus.s2s.validator.BindingInfoBean;
import edu.mit.coeus.s2s.validator.S2SErrorMessages;
import edu.mit.coeus.s2s.validator.S2SValidator;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import gov.grants.apply.forms.rr_fednonfedbudget_v1_1.RRFedNonFedBudget;
import java.util.Hashtable;

/**
 *
 * @author  geot
 * Created on June 1, 2006, 11:44 AM
 */
public class RRFedNonFedSubawardStreamV1_2 extends S2SBaseStream {
    protected static final String FORM_VERSION = "1.2";
    private static final String BUDGET_NODE_PKG_NAME = "gov.grants.apply.forms.rr_fednonfedbudget_v1_1";
    private HashMap params;
    private gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.ObjectFactory fedNonfedSubObjFactory;
    
    /** Creates a new instance of SubAward10Stream */
    public RRFedNonFedSubawardStreamV1_2() {
        fedNonfedSubObjFactory = new gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.ObjectFactory();
    }
    
    public Object getStream(java.util.HashMap hm) throws edu.mit.coeus.exception.CoeusException,
                                    edu.mit.coeus.utils.dbengine.DBException {
        this.params = hm;
        return getRRFedNonFedSubawardBudget();
    }
    protected Object getRRFedNonFedSubawardBudget() throws CoeusException,DBException{
    	UtilFactory.log("Trying to create RRFedNonFed subaward");
        try{
            RRFedNonFedSubawardBudgetType fedNonFedSubAward = fedNonfedSubObjFactory.createRRFedNonFedSubawardBudget();
            UtilFactory.log("Created RRFedNonFed subaward");
            RRFedNonFedSubawardBudgetType.BudgetAttachmentsType attachment = fedNonfedSubObjFactory.createRRFedNonFedSubawardBudgetTypeBudgetAttachmentsType();
            UtilFactory.log("RRFedNonFed attachments complete");
            fedNonFedSubAward.setFormVersion(FORM_VERSION);
            
            List attList = attachment.getRRFedNonFedBudget();
            Map map = getSubAwardStreams();
            Iterator it = map.keySet().iterator();
            int attIndex = 0;
            /*
             *Schema allows to attach only 10 att names, but can attach any number of att xml files,
             *so, not restring the xmls, but restring att names
             *
             */
            while(it.hasNext()){
                ++attIndex;
                String cidKey = (String)it.next();
                switch(attIndex){
                    case(1):
                        fedNonFedSubAward.setATT1(cidKey);
                        break;
                    case(2):
                        fedNonFedSubAward.setATT2(cidKey);
                        break;
                    case(3):
                        fedNonFedSubAward.setATT3(cidKey);
                        break;
                    case(4):
                        fedNonFedSubAward.setATT4(cidKey);
                        break;
                    case(5):
                        fedNonFedSubAward.setATT5(cidKey);
                        break;
                    case(6):
                        fedNonFedSubAward.setATT6(cidKey);
                        break;
                    case(7):
                        fedNonFedSubAward.setATT7(cidKey);
                        break;
                    case(8):
                        fedNonFedSubAward.setATT8(cidKey);
                        break;
                    case(9):
                        fedNonFedSubAward.setATT9(cidKey);
                        break;
                    case(10):
                        fedNonFedSubAward.setATT10(cidKey);
                        break;
                }
                attList.add(map.get(cidKey));
            }
            fedNonFedSubAward.setBudgetAttachments(attachment);
            //JIRA Coeusdev 751 - START
            if (attList == null || attList.isEmpty()) {
                try {
                    S2SValidator.addCustError((String) params.get("PROPOSAL_NUMBER"), S2SErrorMessages.getProperty("RRFedNonFedSubaward30StreamV1_2.min_attachment"));
                } catch (IOException ioEx) {
                    S2SValidator.addCustError((String) params.get("PROPOSAL_NUMBER"), "Could Not read Property 'RRFedNonFedSubaward30StreamV1_2.min_attachment' in S2SErrorMessages.properties");
                }
            }
            //JIRA Coeusdev 751 - END
            return fedNonFedSubAward;
        }catch(JAXBException jxbEx){
            UtilFactory.log(jxbEx.getMessage(),jxbEx,"FedNonFedSubAward10Stream","prepareSubAwardObj");
            jxbEx.printStackTrace();
            throw new CoeusException(jxbEx.getMessage());
        }
    }
    private String replaceChangedNamespaces(String subAwdXML) throws CoeusException {
        Hashtable bindings = BindingFileReader.getBindings();
        BindingInfoBean bindInfo = (BindingInfoBean) bindings.get("http://apply.grants.gov/forms/RR_FedNonFedBudget-V1.1");
        String tempSubXml = subAwdXML;
        if (bindInfo != null && bindInfo.isNsChanged()) {
            int in = tempSubXml.indexOf(bindInfo.getNameSpace());
            if (in != -1) {
                tempSubXml = Converter.replaceAll(tempSubXml, "\"" + bindInfo.getNameSpace() + "\"", "\"" + bindInfo.getCgdNameSpace() + "\"");
            }
        }
        subAwdXML = tempSubXml;
        return subAwdXML;
    }
    
    protected javax.xml.bind.Element prepareFedNonFedSubAwardObj(BudgetSubAwardBean subAwdBean) throws CoeusException,JAXBException,DBException{
        String fedNonFedSubAwdXML = new String(subAwdBean.getSubAwardXML());
        UtilFactory.log("prepareFedNonFedSubAwardObj :: got subaward XML");
        fedNonFedSubAwdXML = replaceChangedNamespaces(fedNonFedSubAwdXML);
        Document fedNonFedSubAwdFormsDoc = Converter.string2Dom(fedNonFedSubAwdXML);
        Element fedNonFedSubAwdFormsElement = fedNonFedSubAwdFormsDoc.getDocumentElement();
        NodeList fedNonFedSubAwdNodeList = fedNonFedSubAwdFormsElement.getElementsByTagNameNS("http://apply.grants.gov/forms/RR_FedNonFedBudget-V1-1","RR_FedNonFedBudget");
        if(fedNonFedSubAwdNodeList.getLength()==0){
        	UtilFactory.log("node length is 0");
            try{
            	UtilFactory.log("ErrorS!");
                String err1 = S2SErrorMessages.getProperty("s2sFedNonFedSubawardBudgetV1-2_10000");
                String err2 = S2SErrorMessages.getProperty("s2sFedNonFedSubawardBudget_10002");
                String err = err1 + " for organization "+subAwdBean.getOrganizationName()+". "+err2;
                S2SValidator.addCustError(subAwdBean.getProposalNumber(), err);
            }catch(IOException ex){
                UtilFactory.log("Just a warning : will be ignored",ex,"FedNonFedSubAwardStream_V1_2", "prepapreFedNonFedSubAwardObj");
                S2SValidator.addCustError(subAwdBean.getProposalNumber(), "Not able to extract XML");
            }
            return null;
        }
        Node subAwdNode = fedNonFedSubAwdNodeList.item(0);
        byte[] fedNonFedSubAwdNodeBytes = null;
        fedNonFedSubAwdNodeBytes = Converter.doc2bytes(Converter.node2Dom(subAwdNode));
        JAXBContext jaxbContext = JAXBContext.newInstance(BUDGET_NODE_PKG_NAME);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        UtilFactory.log("Unmarshaller created");
        InputStream bgtIS = new ByteArrayInputStream(fedNonFedSubAwdNodeBytes);
        UtilFactory.log("bytestream created");
        RRFedNonFedBudget rrBudget = (RRFedNonFedBudget)um.unmarshal(bgtIS);
        UtilFactory.log("Ready to return");
        return (RRFedNonFedBudget)rrBudget;
    }
    protected static final String prepareAttName(final BudgetSubAwardBean subAwdBean){
        return subAwdBean.getOrganizationName()+subAwdBean.getProposalNumber()+
                subAwdBean.getVersionNumber()+subAwdBean.getSubAwardNumber();
    }
    protected Map getSubAwardStreams() throws CoeusException,DBException,JAXBException{
        //call txn bean and get the list
        BudgetSubAwardTxnBean subAwdTxn = new BudgetSubAwardTxnBean();
        List subAwrdXmls = null;
        List attList = null;
        try{
            String propNum = (String)params.get("PROPOSAL_NUMBER");
            subAwrdXmls = subAwdTxn.getBudgetSubAwardXMLForFinalVersion(propNum,getNamespace());
            attList = subAwdTxn.getAttachments(propNum);
        }catch(SQLException sEx){
            sEx.printStackTrace();
            throw new CoeusException(sEx.getMessage());
        }catch(IOException sEx){
            sEx.printStackTrace();
            throw new CoeusException(sEx.getMessage());
        }
        Iterator it = subAwrdXmls.iterator();
        int i=0;
        LinkedHashMap cIdxmlMap = new LinkedHashMap();
        while(it.hasNext()){
            BudgetSubAwardBean subAwdBean = (BudgetSubAwardBean)it.next();
            char[] xmlBts = subAwdBean.getSubAwardXML();
            if(xmlBts!=null && xmlBts.length>0){
                javax.xml.bind.Element subAwdElement = prepareFedNonFedSubAwardObj(subAwdBean);
                if(subAwdElement!=null)
                    cIdxmlMap.put(prepareAttName(subAwdBean), subAwdElement);
            }
        }
        addSubAwdAttachments(attList);
        return cIdxmlMap;
    }
    private void addSubAwdAttachments(List attList) throws CoeusException{
        if(attList==null) return;
        Iterator it = attList.iterator();
        int i=0;
        while(it.hasNext()){
            BudgetSubAwardAttachmentBean subAwdAttBean = (BudgetSubAwardAttachmentBean)it.next();
            Attachment att = new Attachment();
            att.setContent(subAwdAttBean.getAttachment());
            att.setContentId(subAwdAttBean.getContentId());
            att.setContentType(subAwdAttBean.getContentType());
            addAttachment(att.getContentId(), att);
        }
    }
    public static void main(String args[]) throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance("gov.grants.apply.forms.rr_fednonfedbudget_v1_1");
        HashMap m = new HashMap();
        m.put("PROPOSAL_NUMBER", "00001768");
        SubAwardStream s = new SubAwardStream();
        Object obj = s.getStream(m);
        javax.xml.bind.Marshaller mar = jaxbContext.createMarshaller();
        mar.marshal(obj,System.out);
    }

    private String getNamespace() {
        return "http://apply.grants.gov/forms/RR_FedNonFedBudget-V1.1";
    }
}
