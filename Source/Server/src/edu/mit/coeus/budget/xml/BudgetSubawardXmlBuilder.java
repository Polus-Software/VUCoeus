/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.budget.xml;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.XfaForm;
import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.s2s.validator.BindingFileReader;
import edu.mit.coeus.s2s.validator.BindingInfoBean;
import java.io.ByteArrayInputStream;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author sharathk
 */
public class BudgetSubawardXmlBuilder {

    private static final String XFA_NS = "http://www.xfa.org/schema/xfa-data/1.0/";
    private static final String TRAIN_BUDGET = "http://apply.grants.gov/forms/PHS398_TrainingSubawardBudget-V1.0";
    //coeusqa-3941 start
    private static final String TRAIN_BUDGET2 = "http://apply.grants.gov/forms/PHS398_TrainingSubawardBudget_2_0-V2.0";
    //couesqa-3941 end
    //coeusqa-4050 start
    private static final String RR_SUBAWARD_BUDGET30_1_3 = "http://apply.grants.gov/forms/RR_SubawardBudget30_1_3-V1.3";
    //couesqa-4050 end
    //coeusqa-4051 start
    private static final String RR_SUBAWARD_BUDGET_1_3 = "http://apply.grants.gov/forms/RR_SubawardBudget_1_3-V1.3";
    //couesqa-4051 end
    private static final String RR_SUBAWARD_BUDGET_10_10 = "http://apply.grants.gov/forms/RR_SubawardBudget10_10-V1.2";
    private static final String RR_FNF_SUBAWARD_BUDGET_10_10 = "http://apply.grants.gov/forms/RR_FedNonFed_SubawardBudget10_10-V1.2";
    private static final String RR_SUBAWARD_BUDGET_10_30 = "http://apply.grants.gov/forms/RR_SubawardBudget10_30-V1.2";
    private static final String RR_FNF_SUBAWARD_BUDGET_10_30 = "http://apply.grants.gov/forms/RR_FedNonFed_SubawardBudget10_30-V1.2";
    private static final String RR_SUBAWARD_BUDGET_10_30_V13 = "http://apply.grants.gov/forms/RR_SubawardBudget10_30_1_3-V1.3";
    private static final String RR_SUBAWARD_BUDGET_10_10_V13 = "http://apply.grants.gov/forms/RR_SubawardBudget10_10_1_3-V1.3";

    /* JM 10-24-2016 new October 2016 GG forms */
    private static final String RR_SUBAWARD_BUDGET_1_4 = "http://apply.grants.gov/forms/RR_SubawardBudget_1_4-V1.4";
    private static final String RR_SUBAWARD_BUDGET_30_1_4 = "http://apply.grants.gov/forms/RR_SubawardBudget30_1_4-V1.4";    
    private static final String RR_SUBAWARD_BUDGET_10_10_V14 = "http://apply.grants.gov/forms/RR_SubawardBudget10_10_1_4-V1.4";
    private static final String RR_SUBAWARD_BUDGET_10_30_V14 = "http://apply.grants.gov/forms/RR_SubawardBudget10_30_1_4-V1.4";
    private static final String RR_FNF_SUBAWARD_BUDGET_V1_3 = "http://apply.grants.gov/forms/RR_FedNonFed_SubawardBudget_1_3-V1.3";
    private static final String RR_FNF_SUBAWARD_BUDGET_30_V1_3 = "http://apply.grants.gov/forms/RR_FedNonFed_SubawardBudget30_1_3-V1.3";
    /* JM END */
    
    public BudgetSubAwardBean getXMLFromPDF(PdfReader reader,  Map fileMap, BudgetSubAwardBean budgetSubAwardBean) throws Exception {
        //PdfReader reader = new PdfReader(pdfByte);

        XfaForm xfaForm = reader.getAcroFields().getXfa();
        Node domDocument = xfaForm.getDomDocument();
        Element documentElement = ((Document) domDocument).getDocumentElement();

        Element datasetsElement = (Element) documentElement.getElementsByTagNameNS(XFA_NS, "datasets").item(0);
        Element dataElement = (Element) datasetsElement.getElementsByTagNameNS(XFA_NS, "data").item(0);
        Element grantApplicationElement = (Element) dataElement.getChildNodes().item(0);

        byte[] serializedXML = XfaForm.serializeDoc(grantApplicationElement);

        DocumentBuilderFactory domParserFactory = DocumentBuilderFactory.newInstance();
        // 4122: Upgrade Stylevision - End
        domParserFactory.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder domParser = domParserFactory.newDocumentBuilder();
        domParserFactory.setIgnoringElementContentWhitespace(true);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedXML);
        org.w3c.dom.Document document = domParser.parse(byteArrayInputStream);
        byteArrayInputStream.close();
        String namespace = null, formname = null;
        if (document != null) {
            Element element = document.getDocumentElement();
            namespace = element.getNamespaceURI();
            formname = element.getNodeName();
            //namespace = node.getNodeValue();
            BindingInfoBean bindingInfoBean = BindingFileReader.get(namespace);
            if(bindingInfoBean != null) {
                formname = bindingInfoBean.getFormName();
            }
            budgetSubAwardBean.setNamespace(namespace);
            budgetSubAwardBean.setFormName(formname);
        }
        BudgetSubAwardBean retBudgetSubAwardBean = null;
        BudgetSubawardXmlModifier xmlMod = null;
        if(namespace.equalsIgnoreCase(TRAIN_BUDGET)){
            xmlMod = new BudgetSubawardTrainBudgetImpl();
        }else if(namespace.equalsIgnoreCase(TRAIN_BUDGET2)){
            xmlMod = new BudgetSubawardTrainBudgetImpl2();
        }else if(namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET_10_10) || namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET_10_30)){
            xmlMod = new BudgetSubawardXmlExtract(false, "RR_Budget10", "http://apply.grants.gov/forms/RR_Budget10-V1.1");
        }else if(namespace.equalsIgnoreCase(RR_FNF_SUBAWARD_BUDGET_10_10) || namespace.equalsIgnoreCase(RR_FNF_SUBAWARD_BUDGET_10_30)){
            xmlMod = new BudgetSubawardXmlExtract(false, "RR_FedNonFedBudget10", "http://apply.grants.gov/forms/RR_FedNonFedBudget10-V1.1");
        }else if(namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET_10_30_V13)  ) {
            xmlMod = new BudgetSubawardXmlExtract(false, "RR_Budget10_1_3","http://apply.grants.gov/forms/RR_Budget10_1_3-V1.3");
        }else if(namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET30_1_3) || namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET30_1_3)
                || namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET_1_3) || namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET_1_3) ){
            xmlMod = new BudgetSubawardXmlExtract(false, "RR_Budget_1_3", "http://apply.grants.gov/forms/RR_Budget_1_3-V1.3");
        }else if(namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET_10_10_V13)  ) {
            xmlMod = new BudgetSubawardXmlExtract(false, "RR_Budget10_1_3","http://apply.grants.gov/forms/RR_Budget10_1_3-V1.3");
        }
        /* JM 10-24-2016 October 2016 GG forms */
        else if(namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET_1_4) || namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET_30_1_4)  ) {
        	xmlMod = new BudgetSubawardXmlExtract(false, "RR_Budget_1_4","http://apply.grants.gov/forms/RR_Budget_1_4-V1.4");        
        }
        else if(namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET_10_10_V14) || namespace.equalsIgnoreCase(RR_SUBAWARD_BUDGET_10_30_V14)  ) {
        	xmlMod = new BudgetSubawardXmlExtract(false, "RR_Budget10_1_4","http://apply.grants.gov/forms/RR_Budget10_1_4-V1.4");        
        }
        else if(namespace.equalsIgnoreCase(RR_FNF_SUBAWARD_BUDGET_V1_3) || namespace.equalsIgnoreCase(RR_FNF_SUBAWARD_BUDGET_30_V1_3)  ) {
        	xmlMod = new BudgetSubawardXmlExtract(false, "RR_FedNonFedBudget_1_2","http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2");        
        }
        /* JM END */
        
        else {
            //old code
            xmlMod = new BudgetSubawardXmlModifierImpl();
        }
        retBudgetSubAwardBean = xmlMod.updateXML(serializedXML, fileMap, budgetSubAwardBean);
        return retBudgetSubAwardBean;
    }



    //public abstract byte[] modifyXml(byte[] xml);

    //public abstract byte[] modifyXml(PdfReader pdfReader);
}
