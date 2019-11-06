/*
 * SubAward10Stream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.budget.bean.BudgetSubAwardTxnBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.validator.BindingFileReader;
import edu.mit.coeus.s2s.validator.BindingInfoBean;
import edu.mit.coeus.s2s.validator.S2SErrorMessages;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.s2s.validator.S2SValidator;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import gov.grants.apply.forms.rr_subawardbudget_v1_2.RRSubawardBudgetType;
import gov.grants.apply.forms.rr_budget_v1_1.RRBudget;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
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
public class SubAwardStream_V1_2 extends SubAwardStream {
    private static final String FORM_VERSION = "1.2";
    private static final String BUDGET_NODE_PKG_NAME = "gov.grants.apply.forms.rr_budget_v1_1";
    
    private gov.grants.apply.forms.rr_subawardbudget_v1_2.ObjectFactory subObjFactory;
    /** Creates a new instance of SubAward10Stream */
    public SubAwardStream_V1_2() {
        subObjFactory = new gov.grants.apply.forms.rr_subawardbudget_v1_2.ObjectFactory();
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
            /*Poor way of coding, but dont want to use reflection APIs just to create 10 attX methods
             *Schema allows to attach only 10 att names, but can attach any number of att xml files,
             *so, not restring the xmls, but restring att names
             *
             */
            while(it.hasNext()){
                String cidKey = (String)it.next();
                attList.add(map.get(cidKey));
                switch(++attIndex){
                    case 1:
                        subAwd.setATT1(cidKey);
                        break;
                    case 2:
                        subAwd.setATT2(cidKey);
                        break;
                    case 3:
                        subAwd.setATT3(cidKey);
                        break;
                    case 4:
                        subAwd.setATT4(cidKey);
                        break;
                    case 5:
                        subAwd.setATT5(cidKey);
                        break;
                    case 6:
                        subAwd.setATT6(cidKey);
                        break;
                    case 7:
                        subAwd.setATT7(cidKey);
                        break;
                    case 8:
                        subAwd.setATT8(cidKey);
                        break;
                    case 9:
                        subAwd.setATT9(cidKey);
                        break;
                    case 10:
                        subAwd.setATT10(cidKey);
                        break;
                }
                
            }
            subAwd.setBudgetAttachments(att);
             //JIRA Coeusdev 751 - START
            if (attList == null || attList.isEmpty()) {
                try {
                    S2SValidator.addCustError((String) params.get("PROPOSAL_NUMBER"), S2SErrorMessages.getProperty("SubAward10_30Stream_V1_2.min_attachment"));
                } catch (IOException ioEx) {
                    S2SValidator.addCustError((String) params.get("PROPOSAL_NUMBER"), "Could Not read Property 'SubAward10_30Stream_V1_2.min_attachment' in S2SErrorMessages.properties");
                }
            }
            //JIRA Coeusdev 751 - END
            return subAwd;
        }catch(JAXBException jxbEx){
            UtilFactory.log(jxbEx.getMessage(),jxbEx,"SubAward10Stream","getRRSubawardBudget");
//            jxbEx.printStackTrace();
            throw new CoeusException(jxbEx.getMessage());
        }
    }
    protected javax.xml.bind.Element prepareSubAwardObj(BudgetSubAwardBean subAwdBean) throws CoeusException,JAXBException,DBException{
        String subAwdXML = new String(subAwdBean.getSubAwardXML());
        subAwdXML = replaceChangedNamespaces(subAwdXML);
        Document subAwdFormsDoc = Converter.string2Dom(subAwdXML);
        Element subAwdFormsElement = subAwdFormsDoc.getDocumentElement();
        NodeList subAwdNodeList = subAwdFormsElement.getElementsByTagNameNS("http://apply.grants.gov/forms/RR_Budget-V1-1","RR_Budget");
        if(subAwdNodeList.getLength()==0){
            try{
                String err1 = S2SErrorMessages.getProperty("s2sSubawardBudgetV1-2_10000");
                String err2 = S2SErrorMessages.getProperty("s2sSubawardBudgetV1-2_10001");
                String err = err1 + "for organization "+subAwdBean.getOrganizationName()+err2;
                S2SValidator.addCustError(subAwdBean.getProposalNumber(), err);
            }catch(IOException ex){
                UtilFactory.log("Just a warning : can be ignored",ex,"SubAwardStream_V1_2", "prepapreSubAwardObj");
                S2SValidator.addCustError(subAwdBean.getProposalNumber(), "Not able to extract XML");
            }
            return null;
        }
        
//            throw new CoeusException("Not able to extract budget information from XML file " +
//                        "for organization "+subAwdBean.getOrganizationName()+
//                        "\n Puredge form used to fill budget data should be RR_Budget-V1.1.xfd");
        Node subAwdNode = subAwdNodeList.item(0);
        byte[] subAwdNodeBytes = null;
//        try{
            subAwdNodeBytes = Converter.doc2bytes(Converter.node2Dom(subAwdNode));
//        }catch(S2SValidationException ex){
//            UtilFactory.log(ex.getMessage(),ex, "SubAwardStream_V1_2", "prepareSubAwardObj");
//            throw new CoeusException(ex.getMessage());
//        }
        JAXBContext jaxbContext = JAXBContext.newInstance(BUDGET_NODE_PKG_NAME);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        InputStream bgtIS = new ByteArrayInputStream(subAwdNodeBytes);
        try{
//            return (RRBudget)um.unmarshal(bgtIS);
            return (RRBudget)um.unmarshal(subAwdNode);
        }catch(JAXBException ex){
            UtilFactory.log(ex.getMessage(),ex, "SubAwardStream_V1_2", "prepareSubAwardObj");
            throw new CoeusException(ex.getMessage());
        }
    }
    protected String getNameSpace() {
        return "http://apply.grants.gov/forms/RR_Budget-V1.1";
    }
    
    public static void main(String args[]) throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance("gov.grants.apply.forms.rr_subawardbudget_v1_1");
        HashMap m = new HashMap();
        m.put("PROPOSAL_NUMBER", "00001768");
        SubAwardStream_V1_2 s = new SubAwardStream_V1_2();
        Object obj = s.getStream(m);
        javax.xml.bind.Marshaller mar = jaxbContext.createMarshaller();
        mar.marshal(obj,System.out);
    }

    private String replaceChangedNamespaces(String subAwdXML) throws CoeusException {
        Hashtable bindings = BindingFileReader.getBindings();
        BindingInfoBean bindInfo = (BindingInfoBean) bindings.get("http://apply.grants.gov/forms/RR_Budget-V1.1");
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
}
