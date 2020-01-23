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
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import gov.grants.apply.forms.rr_subawardbudget_v1_1.RRSubawardBudgetType;
import gov.grants.apply.forms.rr_budget_v1.RRBudget;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
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


/**
 *
 * @author  geot
 * Created on June 1, 2006, 11:44 AM
 */
public class SubAwardStream_V1_1 extends SubAwardStream {
    private static final String FORM_VERSION = "1.1";
    private HashMap params;
    private gov.grants.apply.forms.rr_subawardbudget_v1_1.ObjectFactory subObjFactory;
    /** Creates a new instance of SubAward10Stream */
    public SubAwardStream_V1_1() {
        subObjFactory = new gov.grants.apply.forms.rr_subawardbudget_v1_1.ObjectFactory();
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
            return subAwd;
        }catch(JAXBException jxbEx){
            UtilFactory.log(jxbEx.getMessage(),jxbEx,"SubAward10Stream","getRRSubawardBudget");
//            jxbEx.printStackTrace();
            throw new CoeusException(jxbEx.getMessage());
        }
    }
    
    public static void main(String args[]) throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance("gov.grants.apply.forms.rr_subawardbudget_v1_1");
        HashMap m = new HashMap();
        m.put("PROPOSAL_NUMBER", "00001768");
        SubAwardStream_V1_1 s = new SubAwardStream_V1_1();
        Object obj = s.getStream(m);
        javax.xml.bind.Marshaller mar = jaxbContext.createMarshaller();
        mar.marshal(obj,System.out);
    }
}
