/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.budget.bean.BudgetSubAwardAttachmentBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.utils.dbengine.DBException;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import gov.grants.apply.forms.phs398_trainingsubawardbudget_v1.impl.*;

/**
 *
 * @author sharathk
 */
public class PHS398TrainingSubAwardBudgStream extends S2SBaseStream {

    protected static final String FORM_VERSION = "1.0";
    private static final String TRAIN_SUB_AWD_BUDGET_NODE_PKG_NAME = "gov.grants.apply.forms.phs398_trainingsubawardbudget_v1";
    private static final String NAMESPACE = "http://apply.grants.gov/forms/PHS398_TrainingSubawardBudget-V1.0";

    @Override
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException {
        try {
            //retreive all xml documents for the namespace
            //http://apply.grants.gov/forms/PHS398_TrainingSubawardBudget-V1.0
            BudgetSubAwardTxnBean subAwdTxn = new BudgetSubAwardTxnBean();
            String propNum = (String) ht.get("PROPOSAL_NUMBER");
            List subAwrdXmls = subAwdTxn.getBudgetFedNonFedSubAwardXMLForFinalVersion(propNum, NAMESPACE);
            List attList = subAwdTxn.getAttachments(propNum);
            String attName[] = new String[10];//max 10

            //Retreive Training budgets from XMLs (max 10 Training budget)
            BudgetSubAwardBean subAwdBean;
            char[] xmlBts;
            String subAwdXML;
            Document trainSubAwdDoc;
            Node trainingBudget[] = new Node[10];
            int trainingBudgetIndex = 0;
            int firstDocTrainingNodes = 0;

            DocumentBuilderFactory domParserFactory = DocumentBuilderFactory.newInstance();

            domParserFactory.setNamespaceAware(true);
            javax.xml.parsers.DocumentBuilder domParser = domParserFactory.newDocumentBuilder();
            domParserFactory.setIgnoringElementContentWhitespace(true);
            NodeList nodeList;
            toploop:
            for (int index = 0; index < subAwrdXmls.size(); index++) {
                subAwdBean = (BudgetSubAwardBean) subAwrdXmls.get(index);
                xmlBts = subAwdBean.getSubAwardXML();
                subAwdXML = new String(subAwdBean.getSubAwardXML());
                ByteArrayInputStream bais = new ByteArrayInputStream(subAwdXML.getBytes());
                subAwdXML = null;
                trainSubAwdDoc = domParser.parse(bais);

                nodeList = trainSubAwdDoc.getElementsByTagName("PHS398_TrainingBudget:PHS398_TrainingBudget");
                if (nodeList != null && nodeList.getLength() > 0) {
                    if (index == 0) {
                        firstDocTrainingNodes = nodeList.getLength();
                    }
                    for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); nodeIndex++) {
                        trainingBudget[trainingBudgetIndex] = nodeList.item(nodeIndex);
                        trainingBudgetIndex = trainingBudgetIndex + 1;
                        if (trainingBudgetIndex > 10) {
                            break toploop;
                        }
                    }//End for nodeList
                }//End if nodeList != null
                if(index < 10) {
                    attName[index] = prepareAttName(subAwdBean);
                }
            }//End for subAwdXML

            //Prepare XML (take first doc and append training budgets)
            subAwdBean = (BudgetSubAwardBean) subAwrdXmls.get(0);
            xmlBts = subAwdBean.getSubAwardXML();
            subAwdXML = new String(subAwdBean.getSubAwardXML());
            ByteArrayInputStream bais = new ByteArrayInputStream(subAwdXML.getBytes());
            subAwdXML = null;
            trainSubAwdDoc = domParser.parse(bais);
            Node budAttachments = trainSubAwdDoc.getElementsByTagName("ns1:BudgetAttachments").item(0);
            for (int index = firstDocTrainingNodes; index < trainingBudget.length; index++) {
                if (trainingBudget[index] != null) {
                    Node nodeToAdd = trainSubAwdDoc.importNode(trainingBudget[index], true);
                    budAttachments.appendChild(nodeToAdd);
                }
            }


            JAXBContext jc = JAXBContext.newInstance(TRAIN_SUB_AWD_BUDGET_NODE_PKG_NAME);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            //Object object = unmarshaller.unmarshal(new File("C:/Temp/subaward/extract_PHS398_append.xml"));
            Object object = unmarshaller.unmarshal(trainSubAwdDoc);
            
            //Attachments
            if (attList != null) {
                Iterator it = attList.iterator();
                int i = 0;
                while (it.hasNext()) {
                    BudgetSubAwardAttachmentBean subAwdAttBean = (BudgetSubAwardAttachmentBean) it.next();
                    Attachment att = new Attachment();
                    att.setContent(subAwdAttBean.getAttachment());
                    att.setContentId(subAwdAttBean.getContentId());
                    att.setContentType(subAwdAttBean.getContentType());
                    addAttachment(att.getContentId(), att);
                }
            }

            //Class className = object.getClass();
            PHS398TrainingSubawardBudgetImpl trainSubAward = (PHS398TrainingSubawardBudgetImpl)object;
            //List trainBudget = trainSubAward.getBudgetAttachments().getPHS398TrainingBudget();
            //Iterator it = trainBudget.iterator();


            int attIndex = 0;
            String cidKey = null;
            while(attIndex<10){
                cidKey = attName[attIndex];
                switch(++attIndex){
                    case 1:
                        trainSubAward.setATT1(cidKey);
                        break;
                    case 2:
                        trainSubAward.setATT2(cidKey);
                        break;
                    case 3:
                        trainSubAward.setATT3(cidKey);
                        break;
                    case 4:
                        trainSubAward.setATT4(cidKey);
                        break;
                    case 5:
                        trainSubAward.setATT5(cidKey);
                        break;
                    case 6:
                        trainSubAward.setATT6(cidKey);
                        break;
                    case 7:
                        trainSubAward.setATT7(cidKey);
                        break;
                    case 8:
                        trainSubAward.setATT8(cidKey);
                        break;
                    case 9:
                        trainSubAward.setATT9(cidKey);
                        break;
                    case 10:
                        trainSubAward.setATT10(cidKey);
                        break;

                }
            }
            //trainSubAward.setBudgetAttachments(att);

            //javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
            //transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            //FileOutputStream fos = new FileOutputStream("C:/Temp/subaward/extract_PHS398_modified_code.xml");
            //javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(fos);
            //javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(trainSubAwdDoc);
            //transformer.transform(source, result);

            return object;
        } catch (Exception ex) {
            throw new CoeusException(ex);
        }
    }

    protected static final String prepareAttName(final BudgetSubAwardBean subAwdBean){
        return subAwdBean.getOrganizationName()+subAwdBean.getProposalNumber()+
                subAwdBean.getVersionNumber()+subAwdBean.getSubAwardNumber();
    }

    public static void main(String s) {
        try {
            PHS398TrainingSubAwardBudgStream stream = new PHS398TrainingSubAwardBudgStream();
            Object object = stream.getStream(null);
            System.out.println("Done");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
