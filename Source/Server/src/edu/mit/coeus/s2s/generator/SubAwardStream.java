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
import edu.mit.coeus.s2s.validator.S2SErrorMessages;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.s2s.validator.S2SValidator;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import gov.grants.apply.forms.rr_subawardbudget_v1.RRSubawardBudgetType;
import gov.grants.apply.forms.rr_budget_v1.RRBudget;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 *
 * @author  geot
 * Created on June 1, 2006, 11:44 AM
 */
public class SubAwardStream extends S2SBaseStream {
    protected static final String FORM_VERSION = "1.0";
    private static final String BUDGET_NODE_PKG_NAME = "gov.grants.apply.forms.rr_budget_v1";
    protected HashMap params; //coeusdev 751. access params for user friendly messages
    private gov.grants.apply.forms.rr_subawardbudget_v1.ObjectFactory subObjFactory;
    protected String proposalNumber;
    
    /** Creates a new instance of SubAward10Stream */
    public SubAwardStream() {
        subObjFactory = new gov.grants.apply.forms.rr_subawardbudget_v1.ObjectFactory();
    }
    
    public Object getStream(java.util.HashMap hm) throws edu.mit.coeus.exception.CoeusException,
                                    edu.mit.coeus.utils.dbengine.DBException {
        this.params = hm;
        proposalNumber = (String)params.get("PROPOSAL_NUMBER");
        return getRRSubawardBudget();
    }
    protected Object getRRSubawardBudget() throws CoeusException,DBException{
        try{
            RRSubawardBudgetType subAwd = subObjFactory.createRRSubawardBudget();
            RRSubawardBudgetType.BudgetAttachmentsType att = subObjFactory.createRRSubawardBudgetTypeBudgetAttachmentsType();
            subAwd.setFormVersion(FORM_VERSION);
            
            List attList = att.getRRBudget();
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
                        subAwd.setATT1(cidKey);
                        break;
                    case(2):
                        subAwd.setATT2(cidKey);
                        break;
                    case(3):
                        subAwd.setATT3(cidKey);
                        break;
                    case(4):
                        subAwd.setATT4(cidKey);
                        break;
                    case(5):
                        subAwd.setATT5(cidKey);
                        break;
                    case(6):
                        subAwd.setATT6(cidKey);
                        break;
                    case(7):
                        subAwd.setATT7(cidKey);
                        break;
                    case(8):
                        subAwd.setATT8(cidKey);
                        break;
                    case(9):
                        subAwd.setATT9(cidKey);
                        break;
                    case(10):
                        subAwd.setATT10(cidKey);
                        break;
                }
//                if(attIndex==1) subAwd.setATT1(cidKey);
//                if(attIndex==2) subAwd.setATT2(cidKey);
//                if(attIndex==3) subAwd.setATT3(cidKey);
//                if(attIndex==4) subAwd.setATT4(cidKey);
//                if(attIndex==5) subAwd.setATT5(cidKey);
//                if(attIndex==6) subAwd.setATT6(cidKey);
//                if(attIndex==7) subAwd.setATT7(cidKey);
//                if(attIndex==8) subAwd.setATT8(cidKey);
//                if(attIndex==9) subAwd.setATT9(cidKey);
//                if(attIndex==10) subAwd.setATT10(cidKey);
                attList.add(map.get(cidKey));
            }
            subAwd.setBudgetAttachments(att);
            return subAwd;
        }catch(JAXBException jxbEx){
            UtilFactory.log(jxbEx.getMessage(),jxbEx,"SubAward10Stream","prepareSubAwardObj");
//            jxbEx.printStackTrace();
            throw new CoeusException(jxbEx.getMessage());
        }
    }
//    protected RRSubawardBudgetType.BudgetAttachmentsType getSubAwardNode() 
//            throws JAXBException,CoeusException,DBException{
//        
//        att.getRRBudget().addAll();
//        return att;
//    }
    protected javax.xml.bind.Element prepareSubAwardObj(BudgetSubAwardBean subAwdBean) throws CoeusException,JAXBException,DBException{
        String subAwdXML = new String(subAwdBean.getSubAwardXML());
        Document subAwdFormsDoc = Converter.string2Dom(subAwdXML);
        Element subAwdFormsElement = subAwdFormsDoc.getDocumentElement();
        NodeList subAwdNodeList = subAwdFormsElement.getElementsByTagNameNS("http://apply.grants.gov/forms/RR_Budget-V1.0","RR_Budget");
        if(subAwdNodeList.getLength()==0){
            try{
                String err1 = S2SErrorMessages.getProperty("s2sSubawardBudgetV1-2_10000");
                String err2 = S2SErrorMessages.getProperty("s2sSubawardBudget_10002");
                String err = err1 + " for organization "+subAwdBean.getOrganizationName()+" "+err2;
                S2SValidator.addCustError(subAwdBean.getProposalNumber(), err);
            }catch(IOException ex){
                UtilFactory.log("Just a warning : can be ignored",ex,"SubAwardStream_V1_2", "prepapreSubAwardObj");
                S2SValidator.addCustError(subAwdBean.getProposalNumber(), "Not able to extract XML");
            }
            return null;
        }
        Node subAwdNode = subAwdNodeList.item(0);
        System.out.println("");
        byte[] subAwdNodeBytes = null;
//        try{
            subAwdNodeBytes = Converter.doc2bytes(Converter.node2Dom(subAwdNode));
//        }catch(S2SValidationException ex){
//            throw new CoeusException(ex.getMessage());
//        }
        JAXBContext jaxbContext = JAXBContext.newInstance(BUDGET_NODE_PKG_NAME);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        InputStream bgtIS = new ByteArrayInputStream(subAwdNodeBytes);
        RRBudget rrBudget = (RRBudget)um.unmarshal(bgtIS);
        return (RRBudget)rrBudget;
    }
    protected static final String prepareAttName(final BudgetSubAwardBean subAwdBean){
        return subAwdBean.getOrganizationName()+subAwdBean.getProposalNumber()+
                subAwdBean.getVersionNumber()+subAwdBean.getSubAwardNumber();
    }
    protected Map getSubAwardStreams() throws CoeusException,DBException,JAXBException{
//        Vector streams = new Vector();
        //call txn bean and get the list
        BudgetSubAwardTxnBean subAwdTxn = new BudgetSubAwardTxnBean();
        List subAwrdXmls = null;
        List attList = null;
        try{
            subAwrdXmls = subAwdTxn.getBudgetSubAwardXMLForFinalVersion(proposalNumber,getNameSpace());
            int version = -1;
            if(subAwrdXmls != null) {
                int subawardNumber[] = new int[subAwrdXmls.size()];
                BudgetSubAwardBean budgetSubAwardBean = null;
                for(int index=0; index < subAwrdXmls.size(); index++) {
                    budgetSubAwardBean = (BudgetSubAwardBean)subAwrdXmls.get(index);
                    subawardNumber[index] = budgetSubAwardBean.getAwSubAwardNumber();
                }
                if(budgetSubAwardBean != null) {
                    version = budgetSubAwardBean.getVersionNumber();
                }

                attList = subAwdTxn.getAttachments(proposalNumber);
                //remove attachments for other subaward number
                BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean;
                int tempSubAwdNum;
                if(attList != null){
                    attachmentLoop:for(int index=0; index < attList.size(); index++){
                        budgetSubAwardAttachmentBean = (BudgetSubAwardAttachmentBean)attList.get(index);
                        tempSubAwdNum = budgetSubAwardAttachmentBean.getSubAwardNumber();
                        for(int subAwdIndex = 0; subAwdIndex < subawardNumber.length; subAwdIndex++){
                            if(tempSubAwdNum == subawardNumber[subAwdIndex]) {
                                continue attachmentLoop;
                            }
                        }
                        //Delete this attachment
                        attList.remove(index);
                        index = index-1;
                    }
                }
            }
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
            char[] xmlBts= subAwdBean.getSubAwardXML();
            if(xmlBts!=null && xmlBts.length>0){
//                System.out.println("xml is not null "+(i++));
//                String xml = new String(xmlBts);
//                System.out.println(xml);
//                RRBudget rrBudget = prepareSubAwardObj(xmlBts);
                javax.xml.bind.Element subAwdElement = prepareSubAwardObj(subAwdBean);
                if(subAwdElement!=null)
                    cIdxmlMap.put(prepareAttName(subAwdBean), subAwdElement);
//                streams.add(rrBudget);
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
        JAXBContext jaxbContext = JAXBContext.newInstance("gov.grants.apply.forms.rr_subawardbudget_v1");
        HashMap m = new HashMap();
        m.put("PROPOSAL_NUMBER", "00001768");
        SubAwardStream s = new SubAwardStream();
        Object obj = s.getStream(m);
        javax.xml.bind.Marshaller mar = jaxbContext.createMarshaller();
        mar.marshal(obj,System.out);
    }

    protected String getNameSpace() {
        return "http://apply.grants.gov/forms/RR_Budget-V1.0";
    }
}
