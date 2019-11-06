/*
 * @(#)RRPersonalDataStream.java October 19, 2004, 10:12 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.s2s.generator.stream.*;
 
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.bean.S2STxnBean;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;

import gov.grants.apply.forms.rr_personaldata_v1.*;
import gov.grants.apply.system.attachments_v1.*;
import gov.grants.apply.system.global_v1.*;
import gov.grants.apply.system.globallibrary_v1.*;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Vector;
import javax.xml.bind.JAXBException;
 
/**
 * @author  coeus dev team
 * Class for generating the object stream for grants.gov RRPersonalData. It uses jaxb classes
 * which have been created under gov.grants.apply package. Fetch the data 
 * from database and attach with the jaxb beans which have been derived from 
 * RRPersonalData.
 */
 
 public class RRPersonalDataStream extends S2SBaseStream{ 
    private gov.grants.apply.forms.rr_personaldata_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.globallibrary_v1.ObjectFactory globalLibObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;  

    private String propNumber = null;
    private UtilFactory utilFactory;
    
   private static final String COPI = "Co-PD/PI";
  private static final String PDPI = "PD/PI";
   gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
  
 
   
    /** Creates a new instance of RRPersonalDataStream */
    public RRPersonalDataStream(){
        objFactory = new gov.grants.apply.forms.rr_personaldata_v1.ObjectFactory();

        globalLibObjFactory = new gov.grants.apply.system.globallibrary_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
     
     } 
   
  
    private RRPersonalDataType getRRPersonalData() 
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
        RRPersonalDataType RRPersonalData = objFactory.createRRPersonalData();
         try{ 
             
             if(propNumber == null){
                 throw new CoeusException("Proposal number not found.");
             }
            S2STxnBean s2sTxnBean = new S2STxnBean();  
            KeyPersonBean PI = null;
            CoeusVector keyPersons = null;
            ArrayList allKeyPersons = s2sTxnBean.getInvestAndKeyPersons(propNumber,8);
            //allKeyPersons contains two vectors- 1st vector is the first 8 key person
            //2nd vector is the remaining people - all are sorted by sort_id (done in stored procedure)
       
            if(allKeyPersons != null && !allKeyPersons.isEmpty()){
                keyPersons = (CoeusVector)allKeyPersons.get(0);
                if(keyPersons != null && !keyPersons.isEmpty()){
                    PI = (KeyPersonBean)keyPersons.get(0);
                } 
            }
          
            DirectorType director = objFactory.createDirectorType();
            HumanNameDataType humanNameData = 
                            globalLibObjFactory.createHumanNameDataType();
            //addition for printing
//            if(PI == null){
//                throw new CoeusException("exceptionCode.90012");
//            }else {
            if(PI != null){
                humanNameData.setFirstName(PI.getFirstName());
                humanNameData.setLastName(PI.getLastName());
                director.setName(humanNameData);
                RRPersonalData.setProjectDirector(director);
            }
            java.util.List coPIList = RRPersonalData.getCoProjectDirector();
            for(int coPICnt = 0; coPICnt<keyPersons.size(); coPICnt++){
                KeyPersonBean coPI = (KeyPersonBean)keyPersons.get(coPICnt);
                
                  if(coPI.getRole().equals(COPI)) {
                    DirectorType coDirector = objFactory.createDirectorType();
                    HumanNameDataType coHumanNameData = 
                            globalLibObjFactory.createHumanNameDataType();
                    coHumanNameData.setFirstName(coPI.getFirstName());
                    coHumanNameData.setLastName(coPI.getLastName());
                    coDirector.setName(coHumanNameData);
                    coPIList.add(coDirector);
                }
            }
            
            RRPersonalData.setFormVersion("1.0");
        
           
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"RRPersonalDataStream",
                                                        "getRRPersonalData()");
            throw new CoeusXMLException(jaxbEx.getMessage());
       }

       return RRPersonalData;
    }
      
 
    


    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getRRPersonalData();
    }    
 
 }   
    

