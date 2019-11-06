/*
 * @(#)NSFDeviationAuthorizationV1_1Stream.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import edu.mit.coeus.utils.xml.bean.proposal.bean.AbstractBean;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;

import gov.grants.apply.forms.nsf_deviationauthorization_v1_1.*;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Vector;
import javax.xml.bind.JAXBException;
 
/**
 * @author  jenlu
 * Class for generating the object stream for grants.gov NSF Deviation Authorization. 
 * It uses jaxb classes which have been created under gov.grants.apply package. 
 */
 
 public class NSFDeviationAuthorizationV1_1Stream extends S2SBaseStream{ 
     
    private gov.grants.apply.forms.nsf_deviationauthorization_v1_1.ObjectFactory objFactory;

    private String propNumber = null;
    private Vector propAbstracts = null;
    private UtilFactory utilFactory;
    
    //Abstract types
    private static final int DEVIATION_AUTHORIZATION = 15;
  
 
   
    /** Creates a new instance of NSFDeviationAuthorizationV1_1Stream */
    public NSFDeviationAuthorizationV1_1Stream(){
        objFactory = 
            new gov.grants.apply.forms.nsf_deviationauthorization_v1_1.ObjectFactory();
     
     } 
   
  
    private NSFDeviationAuthorizationType getNSFDeviationAuthorization() 
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
        NSFDeviationAuthorizationType nsfDeviationAuthorization = null;
        try{
            nsfDeviationAuthorization = 
				objFactory.createNSFDeviationAuthorization();
             
            if(propNumber == null){
             throw new CoeusException("Proposal number not found.");
            }
            propAbstracts = getAbstractsForProposal(propNumber);
            String devAuth = getAbstractText(propNumber, DEVIATION_AUTHORIZATION);
            if(devAuth != null){
                nsfDeviationAuthorization.setDeviationAuthorization(devAuth);
            }
            
            nsfDeviationAuthorization.setFormVersion("1.1");
            
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,
                "NSFDeviationAuthorizationV_1Stream", "getNSFDeviationAuthorization()");
            throw new CoeusXMLException(jaxbEx.getMessage());
       }

       return nsfDeviationAuthorization;
    }
    
    private Vector getAbstractsForProposal(String propNumber) throws DBException, CoeusException{
        ProposalPrintingTxnBean proposalPrintingTxn = new ProposalPrintingTxnBean();
        propAbstracts = proposalPrintingTxn.getAbstracts(propNumber, 1);
        return propAbstracts;
    }
      
    private String getAbstractText(String propNumber, int abstractType) throws DBException, CoeusException{
        String abstractText = null;
        if(propAbstracts == null){
            return null;
        }
        for(int abstractCnt = 0; abstractCnt < propAbstracts.size(); abstractCnt++){
            AbstractBean abstractBean = (AbstractBean)propAbstracts.get(abstractCnt);
            if(abstractBean.getAbstractType() == abstractType){
                abstractText = abstractBean.getAbstractText();
                break;
            }
        }
        return abstractText;
    }
    


    
    public Object getStream(HashMap hm) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)hm.get("PROPOSAL_NUMBER");
        return getNSFDeviationAuthorization();
    }    
 
 }   
    
