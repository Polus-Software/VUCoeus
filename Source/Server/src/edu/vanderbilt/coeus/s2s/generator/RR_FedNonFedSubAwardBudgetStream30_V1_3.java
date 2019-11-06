/**
 * RR_FedNonFedSubAwardBudgetStream30_V1_3.java
 */

package edu.vanderbilt.coeus.s2s.generator;

import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.generator.SubAwardStream;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.validator.BindingFileReader;
import edu.mit.coeus.s2s.validator.BindingInfoBean;
import edu.mit.coeus.s2s.validator.S2SErrorMessages;
import edu.mit.coeus.s2s.validator.S2SValidator;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.RRFedNonFedBudget12;
import gov.grants.apply.forms.rr_fednonfed_subawardbudget30_1_3_v1.ObjectFactory;
import gov.grants.apply.forms.rr_fednonfed_subawardbudget30_1_3_v1.RRFedNonFedSubawardBudget3013;
import gov.grants.apply.forms.rr_fednonfed_subawardbudget30_1_3_v1.RRFedNonFedSubawardBudget3013Type;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RR_FedNonFedSubAwardBudgetStream30_V1_3 extends SubAwardStream {
    private static final String FORM_VERSION = "1.3";
    private static final String BUDGET_NODE_PKG_NAME = "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1";
    private HashMap params;
    private ObjectFactory subObjFactory = new ObjectFactory();

    protected Object getRRSubawardBudget() throws CoeusException, DBException {
        try {
        	RRFedNonFedSubawardBudget3013 subAwd = this.subObjFactory.createRRFedNonFedSubawardBudget3013();
        	RRFedNonFedSubawardBudget3013Type.BudgetAttachmentsType att = this.subObjFactory.createRRFedNonFedSubawardBudget3013TypeBudgetAttachmentsType();
            subAwd.setFormVersion(FORM_VERSION);
            List attList = att.getRRFedNonFedBudget12();
            Map map = this.getSubAwardStreams();
            Iterator it = map.keySet().iterator();
            int attIndex = 0;
            while (it.hasNext()) {
                String cidKey = (String)it.next();
                attList.add(map.get(cidKey));
                switch (++attIndex) {
                    case 1: {
                        subAwd.setATT1(cidKey);
                        break;
                    }
                    case 2: {
                        subAwd.setATT2(cidKey);
                        break;
                    }
                    case 3: {
                        subAwd.setATT3(cidKey);
                        break;
                    }
                    case 4: {
                        subAwd.setATT4(cidKey);
                        break;
                    }
                    case 5: {
                        subAwd.setATT5(cidKey);
                        break;
                    }
                    case 6: {
                        subAwd.setATT6(cidKey);
                        break;
                    }
                    case 7: {
                        subAwd.setATT7(cidKey);
                        break;
                    }
                    case 8: {
                        subAwd.setATT8(cidKey);
                        break;
                    }
                    case 9: {
                        subAwd.setATT9(cidKey);
                        break;
                    }
                    case 10: {
                        subAwd.setATT10(cidKey);
                    }
                    case 11: {
                        subAwd.setATT11(cidKey);
                        break;
                    }
                    case 12: {
                        subAwd.setATT12(cidKey);
                        break;
                    }
                    case 13: {
                        subAwd.setATT13(cidKey);
                        break;
                    }
                    case 14: {
                        subAwd.setATT14(cidKey);
                        break;
                    }
                    case 15: {
                        subAwd.setATT15(cidKey);
                        break;
                    }
                    case 16: {
                        subAwd.setATT16(cidKey);
                        break;
                    }
                    case 17: {
                        subAwd.setATT17(cidKey);
                        break;
                    }
                    case 18: {
                        subAwd.setATT18(cidKey);
                        break;
                    }
                    case 19: {
                        subAwd.setATT19(cidKey);
                        break;
                    }
                    case 20: {
                        subAwd.setATT20(cidKey);
                    }
                    case 21: {
                        subAwd.setATT21(cidKey);
                        break;
                    }
                    case 22: {
                        subAwd.setATT22(cidKey);
                        break;
                    }
                    case 23: {
                        subAwd.setATT23(cidKey);
                        break;
                    }
                    case 24: {
                        subAwd.setATT24(cidKey);
                        break;
                    }
                    case 25: {
                        subAwd.setATT25(cidKey);
                        break;
                    }
                    case 26: {
                        subAwd.setATT26(cidKey);
                        break;
                    }
                    case 27: {
                        subAwd.setATT27(cidKey);
                        break;
                    }
                    case 28: {
                        subAwd.setATT28(cidKey);
                        break;
                    }
                    case 29: {
                        subAwd.setATT29(cidKey);
                        break;
                    }
                    case 30: {
                        subAwd.setATT30(cidKey);
                    }
                }
            }
            subAwd.setBudgetAttachments(att);
            if (attList == null || attList.isEmpty()) {
                try {
                    S2SValidator.addCustError((String) params.get("PROPOSAL_NUMBER"), S2SErrorMessages.getProperty("RR_FedNonFedSubAwardBudgetStream30_V1_3.min_attachment"));
                } catch (IOException ioEx) {
                    S2SValidator.addCustError((String) params.get("PROPOSAL_NUMBER"), "Could Not read Property 'RR_FedNonFedSubAwardBudgetStream30_V1_3.min_attachment' in S2SErrorMessages.properties");
                }
            }
            return subAwd;
        }
        catch (JAXBException jxbEx) {
            UtilFactory.log((String)jxbEx.getMessage(), (Throwable)jxbEx, (String)"RR_FedNonFedSubAwardBudgetStream30_V1_3", (String)"getRRSubawardBudget");
            throw new CoeusException(jxbEx.getMessage());
        }
    }

    protected javax.xml.bind.Element prepareSubAwardObj(BudgetSubAwardBean subAwdBean) throws CoeusException, JAXBException, DBException {
        String subAwdXML = new String(subAwdBean.getSubAwardXML());
        Document subAwdFormsDoc = Converter.string2Dom((String)(subAwdXML = this.replaceChangedNamespaces(subAwdXML)));
        Element subAwdFormsElement = subAwdFormsDoc.getDocumentElement();
        NodeList subAwdNodeList = subAwdFormsElement.getElementsByTagNameNS("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2", "RR_FedNonFedBudget_1_2");
        if (subAwdNodeList.getLength() == 0) {
            try {
                String err1 = S2SErrorMessages.getProperty((String)"s2sSubawardBudget_10000");
                String err2 = S2SErrorMessages.getProperty((String)"RRFedNonFedSubawardBudgetV1-3_10001");
                String err = err1 + " for organization " + subAwdBean.getOrganizationName() + err2;
                S2SValidator.addCustError((String)subAwdBean.getProposalNumber(), (String)err);
            }
            catch (IOException ex) {
                UtilFactory.log((String)"Just a warning : can be ignored", (Throwable)ex, (String)"RR_FedNonFedSubAwardBudgetStream30_V1_3", (String)"prepapreSubAwardObj");
                S2SValidator.addCustError((String)subAwdBean.getProposalNumber(), (String)"Not able to extract XML");
            }
            return null;
        }
        Node subAwdNode = subAwdNodeList.item(0);
        byte[] subAwdNodeBytes = null;
        subAwdNodeBytes = Converter.doc2bytes((Document)Converter.node2Dom((Node)subAwdNode));
        JAXBContext jaxbContext = JAXBContext.newInstance(BUDGET_NODE_PKG_NAME);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        ByteArrayInputStream bgtIS = new ByteArrayInputStream(subAwdNodeBytes);
        try {
            return (RRFedNonFedBudget12) um.unmarshal(subAwdNode);
        }
        catch (JAXBException ex) {
            UtilFactory.log((String)ex.getMessage(), (Throwable)ex, (String)"RR_FedNonFedSubAwardBudgetStream30_V1_3", (String)"prepareSubAwardObj");
            throw new CoeusException(ex.getMessage());
        }
    }

    protected String getNameSpace() {
        return "http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2";
    }

    private String replaceChangedNamespaces(String subAwdXML) throws CoeusException {
        int in;
        Hashtable bindings = BindingFileReader.getBindings();
        BindingInfoBean bindInfo = (BindingInfoBean)bindings.get("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2");
        String tempSubXml = subAwdXML;
        if (bindInfo != null && bindInfo.isNsChanged() && (in = tempSubXml.indexOf(bindInfo.getNameSpace())) != -1) {
            tempSubXml = Converter.replaceAll((String)tempSubXml, (String)("\"" + bindInfo.getNameSpace() + "\""), (String)("\"" + bindInfo.getCgdNameSpace() + "\""));
        }
        subAwdXML = tempSubXml;
        return subAwdXML;
    }
}