/*
 * @(#)NSFSuggestedReviewersV1_1Stream.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;
import edu.mit.coeus.utils.xml.bean.proposal.bean.AbstractBean;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;

import gov.grants.apply.forms.nsf_suggestedreviewers_v1_1.*;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * @author  jenlu
 * Class for generating the object stream for grants.gov NSF Suggested Reviewers.
 * It uses jaxb classes which have been created under gov.grants.apply package.
 */

 public class NSFSuggestedReviewersV1_1Stream extends S2SBaseStream{
    private gov.grants.apply.forms.nsf_suggestedreviewers_v1_1.ObjectFactory objFactory;

    private String propNumber = null;
    private UtilFactory utilFactory;
    private Vector propAbstracts = null;

    //Abstract types
    private static final int SUGGESTED_REVIEWERS = 12;
    private static final int REVIEWERS_NOT_TO_INCLUDE = 14;



    /** Creates a new instance of NSFSuggestedReviewersV1_1Stream */
    public NSFSuggestedReviewersV1_1Stream(){
        objFactory = new gov.grants.apply.forms.nsf_suggestedreviewers_v1_1.ObjectFactory();

     }


    private NSFSuggestedReviewersType getNSFSuggestedReviewers()
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
        NSFSuggestedReviewersType nsfSuggestedReviewers = null;
         try{

             nsfSuggestedReviewers = objFactory.createNSFSuggestedReviewers();

             if(propNumber == null){
                 throw new CoeusException("Proposal number not found.");
             }
            propAbstracts = getAbstractsForProposal(propNumber);
            String suggRev = getAbstractText(propNumber, SUGGESTED_REVIEWERS);
            if(suggRev != null){
                nsfSuggestedReviewers.setSuggestedReviewers(suggRev);
            }
            String doNotInclude = getAbstractText(propNumber, REVIEWERS_NOT_TO_INCLUDE);
            if(doNotInclude != null){
                nsfSuggestedReviewers.setReviewersNotToInclude(doNotInclude);
            }
            nsfSuggestedReviewers.setFormVersion("1.1");


        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"NSFSuggestedReviewersV1_1Stream",
                                                        "getNSFSuggestedReviewers()");
            throw new CoeusXMLException(jaxbEx.getMessage());
       }

       return nsfSuggestedReviewers;
    }

    private Vector getAbstractsForProposal(String propNumber)
                                        throws DBException, CoeusException{
        ProposalPrintingTxnBean proposalPrintingTxn = new ProposalPrintingTxnBean();
        propAbstracts = proposalPrintingTxn.getAbstracts(propNumber, 1);
        return propAbstracts;
    }

    private String getAbstractText(String propNumber, int abstractType)
                                            throws DBException, CoeusException{
        String abstractText = null;
        if(propAbstracts == null){
            return null;
        }
        for(int abstractCnt = 0; abstractCnt < propAbstracts.size(); abstractCnt++){
            AbstractBean abstractBean = (AbstractBean)propAbstracts.get(abstractCnt);
            if(abstractBean.getAbstractType() == abstractType){
                System.out.println("found abstract type: "+abstractType);
                abstractText = abstractBean.getAbstractText();
                break;
            }
        }
        return abstractText;
    }




    public Object getStream(HashMap hm) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)hm.get("PROPOSAL_NUMBER");
        return getNSFSuggestedReviewers();
    }

 }

