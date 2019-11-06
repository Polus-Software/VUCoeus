/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeus.propdev.gui;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.mit.coeus.bean.KeyPersonBean;
import edu.mit.coeus.bean.KeyPersonUnitBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.gui.event.BeanUpdatedListener;
import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.keyperson.KeyPersonController;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author midhunmk
 */
public class ProposalKeyPersonController extends KeyPersonController implements BeanUpdatedListener{


    private QueryEngine queryEngine;
    private String proposalNumber;
    private int sequenceNumber;
    private CoeusVector cvKeyPerson;
    private CoeusVector cvUnit;
    private String queryKey;
    private static int KEY_PERSON_TAB_INDEX = 8;

    private static final char NEW_ENTRY_MODE = 'E';
    private static final char COPIED_MODE = 'C';
    public  static final char NEW_CHILD_COPIED = 'P';
    //ppc certify flag change starts
    private boolean keyPersonCerifyParam;

    public void setKeyPersonCerifyParam(boolean keyPersonCerifyParam) {
        this.keyPersonCerifyParam = keyPersonCerifyParam;
    }
    
    /* JM 9-10-2015 local variable for functionType */
    public char functionType;
    /* JM END */
    
    //ppc certify flag change ends
    
    /**
     * Creates a new instance of AwardKeyPersonController
     *
     * @param AwardBaseBean awardBaseBean
     * @param String quesryKey
     * @param char functionType
     */
    public ProposalKeyPersonController(String propnum,Vector kPCollection,char functionType,boolean kpCertFlag) {
        super(propnum, CoeusGuiConstants.PROPOSAL_MODULE);
        this.proposalNumber=propnum;
        //this.sequenceNumber=seq;
        this.queryKey = propnum;
        registerEvents();
        setFunctionType(functionType);
        
        /* JM 9-10-2015 local variable for functionType */
        this.functionType = functionType;
        /* JM END */
        
        queryEngine = QueryEngine.getInstance();
        //queryEngine.addCollection(queryKey, KeyPersonUnitBean.class,(CoeusVector)kPCollection);
        //need to process the units under a person list to query engine.
        convertVectorToBean((CoeusVector)kPCollection);
        
        cvKeyPerson = null;
        keyPersonCerifyParam=kpCertFlag;
        setFormData();
    }

    /**
     * Updates the Query Engine with the modified InstituteProposalKeyPersonBeans
     * @return void
     */
    public void saveFormData() {
        if( isDataChanged() ) {
           // try{
                Equals eqAllData = new Equals("proposalNumber",proposalNumber);
                
                 if( getFunctionType() ==ProposalBaseWindow.NEW_MODE){
                    //deleting all the investigators and units in query engine for new mode
                    //bcoz we may replace the data what we got from query engine with the new data.
                    //So the instance in query engine should be replaced with the new one.
                    
                    queryEngine.removeData(queryKey,ProposalKeyPersonFormBean.class,eqAllData);
                    queryEngine.removeData(queryKey,KeyPersonBean.class,eqAllData);
                    queryEngine.removeData(queryKey,KeyPersonUnitBean.class,eqAllData);
//                }else{hjkhjk
                 }
                
                cvKeyPerson = (CoeusVector)getFormData();
                if( cvKeyPerson != null ) {
                    String keyPersonAcType = null;
                    CoeusVector cvUnitQueryEngine= new CoeusVector();

                    for(int i=0;i<cvKeyPerson.size();i++)
                    { KeyPersonBean keyPerBeantmp = (KeyPersonBean)cvKeyPerson.get(i);
                      //updateKeyperson(keyPerBeantmp.getPersonId(),keyPerBeantmp.getKeyPersonsUnits());
                      CoeusVector tmp=keyPerBeantmp.getKeyPersonsUnits();
                      if(tmp!=null){cvUnitQueryEngine.addAll(tmp);}
                    }
                    CoeusVector cvlist=new CoeusVector();
                    for(int i=0; i<cvUnitQueryEngine.size();i++)
                    {
                        KeyPersonUnitBean kPUB=(KeyPersonUnitBean)cvUnitQueryEngine.get(i);
                        String unitAcType = kPUB.getAcType();
                               if(unitAcType!=null)
                               {if(!cvlist.contains(kPUB)){
                                    kPUB.setProposalNumber(proposalNumber);
                                    kPUB.setSequenceNumber(sequenceNumber);
                                   // kPUB.setAw_UnitNumber(unitBean.getUnitNumber());
                                  // kPUB.setAw_PersonId(unitBean.getPersonId());
                                  if(kPUB.getAcType()!=null)
                                  {cvlist.add(kPUB);}}}
                    }
                     queryEngine.addCollection(queryKey, KeyPersonUnitBean.class, cvlist);
                    int keyPersonCount = cvKeyPerson.size();
                    CoeusVector lastkpdata=new CoeusVector();
                    for( int indx = 0; indx < keyPersonCount; indx++) {
                        KeyPersonBean keyPerBean = (KeyPersonBean)cvKeyPerson.get(indx);
                        if(keyPerBean!=null){
                        ProposalKeyPersonFormBean  proposalKeyPerBean =
                                new ProposalKeyPersonFormBean (keyPerBean);
                         proposalKeyPerBean.setProposalNumber(proposalNumber);
                          proposalKeyPerBean.setSequenceNumber(sequenceNumber);
                          proposalKeyPerBean.setAw_PersonId(keyPerBean.getPersonId());
if(keyPerBean.getAcType()!=null){
                        lastkpdata.add(proposalKeyPerBean);}}
//                        if( INSERT_RECORD.equals( keyPerBean.getAcType() ) ){
//                            proposalKeyPerBean.setProposalNumber(proposalNumber);
//                            proposalKeyPerBean.setSequenceNumber(sequenceNumber);
//                            proposalKeyPerBean.setAw_PersonId(keyPerBean.getPersonId());
//                        }else{
//                            if( keyPerBean instanceof ProposalKeyPersonFormBean  ){
//                                ProposalKeyPersonFormBean  oldKeyPerBean =
//                                        (ProposalKeyPersonFormBean )keyPerBean;
//                                proposalKeyPerBean.setProposalNumber(oldKeyPerBean.getProposalNumber());
//                                proposalKeyPerBean.setSequenceNumber(oldKeyPerBean.getSequenceNumber());
//                            }
////                            else{
////                                continue;
////                            }
//                        }

//                        if(proposalKeyPerBean.getAcType()!= null){
//                            if(proposalKeyPerBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
//                                //First delete the existing data and then insert the same. This is
//                                //required since primary keys can be modified
//                                proposalKeyPerBean.setAcType(TypeConstants.DELETE_RECORD);
//                            queryEngine.delete(queryKey, proposalKeyPerBean);
//                                proposalKeyPerBean.setAcType(TypeConstants.INSERT_RECORD);
//                              queryEngine.insert(queryKey, proposalKeyPerBean);
//                            }else if(proposalKeyPerBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
//                                proposalKeyPerBean.setAcType(TypeConstants.DELETE_RECORD);
//                               queryEngine.delete(queryKey, proposalKeyPerBean);
//                            }else if(proposalKeyPerBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
//                                proposalKeyPerBean.setAcType(TypeConstants.INSERT_RECORD);
//                              queryEngine.insert(queryKey, proposalKeyPerBean);
//                            }
//                        }
                    }
                    if(lastkpdata.size()>0){
                     queryEngine.addCollection(queryKey,ProposalKeyPersonFormBean.class, lastkpdata);}
                }
            }
            //catch(CoeusException ce ) {ce.printStackTrace();}
       // }
    }

    /**
     * Gets the IP Key Person data from Query Engine and sets data to Key Person Form
     * @return void
     */
    public void setFormData(){
        try{
            cvKeyPerson = queryEngine.executeQuery(queryKey,KeyPersonBean.class,
                    CoeusVector.FILTER_ACTIVE_BEANS);
            cvUnit=queryEngine.executeQuery(queryKey,KeyPersonUnitBean.class,
                    CoeusVector.FILTER_ACTIVE_BEANS);
            if(keyPersonCerifyParam){
                super.setModuleName(CoeusGuiConstants.PROPOSAL_MODULE);}
            else{
                super.setModuleName(CoeusGuiConstants.AWARD_MODULE);}
            super.setModuleNumber(proposalNumber);
            super.setSeqNo(String.valueOf(sequenceNumber));
            super.setKeyPersonTabIndex(KEY_PERSON_TAB_INDEX);
            super.setFormData(cvKeyPerson);
            super.setDataChanged(false);
            
            /* JM 9-10-2015 set functionType for key person tab */
            super.setFunctionType(functionType);
            /* JM END */
            
        }catch(CoeusException ce ) {
            ce.printStackTrace();
            super.setFormData(null);
        }
    }

    

    /**
     * Checks all the required fields are filled with appropriate data.
     * @return boolean true if validation passed successfully
     */
    public boolean validate() throws CoeusUIException {
        try{
            return super.validate();
        }catch(CoeusUIException ex){
            CoeusOptionPane.showWarningDialog(ex.getMessage());
            return false;
        }
    }

    /**
     * Getter for property queryKey.
     * @return Value of property queryKey.
     */
    public java.lang.String getQueryKey() {
        return queryKey;
    }

    /**
     * Setter for property queryKey.
     * @param queryKey New value of property queryKey.
     */
    public void setQueryKey(java.lang.String queryKey) {
        this.queryKey = queryKey;
    }


    public void refresh(){
        if(isDataChanged() ) {
            setFormData();
            setDataChanged(false);
        }
    }

    public Component getControlledUI(){

        return super.getControlledUI();
    }



    

    public void beanUpdated(BeanEvent beanEvent) {
        if( beanEvent.getBean().getClass().equals(ProposalKeyPersonFormBean.class) ){
            setDataChanged(true);
            refresh();
        }
    }

    private void registerEvents() {
        addBeanUpdatedListener(this, ProposalKeyPersonFormBean.class);
    }
void convertVectorToBean(CoeusVector cvkeytmp)
{queryEngine.removeDataCollection(queryKey);
    // the function which convert the vector to the corresponding beans.
    if(cvkeytmp!=null){
     Equals eqAllData = new Equals("proposalNumber",proposalNumber);
//    queryEngine.removeData(queryKey,ProposalKeyPersonFormBean.class,eqAllData);
//    queryEngine.removeData(queryKey,KeyPersonUnitBean.class,eqAllData);
    
    CoeusVector kps=new CoeusVector();
    CoeusVector kpunit=new CoeusVector();
    CoeusVector cvunitstmp;
    int kpcount=cvkeytmp.size();
    if(kpcount>0)
    {
        // to load the query engine.
        for(int i=0;i<kpcount;i++)
        {
       
        KeyPersonBean ktmp=(KeyPersonBean)cvkeytmp.get(i);
        if(ktmp!=null)
        {
          if(ktmp instanceof ProposalKeyPersonFormBean)
          {
           ProposalKeyPersonFormBean pktmp=(ProposalKeyPersonFormBean)cvkeytmp.get(i);
           ktmp=new KeyPersonBean((KeyPersonBean)pktmp);
          }
          ktmp.setAcType(null);
          kps.add(ktmp);
          cvunitstmp=ktmp.getKeyPersonsUnits();
          if(cvunitstmp!=null)
          {
        for(int j=0;j<cvunitstmp.size();j++)
        {
        KeyPersonUnitBean kutmp=(KeyPersonUnitBean)(ktmp.getKeyPersonsUnits().get(j));
        if(kutmp!=null){ kpunit.add(kutmp);}

        }//end of the inner for loop
          }
        }
        }//end of the outer for loop for persons

    }
   
//   queryEngine.removeData(queryKey,ProposalKeyPersonFormBean.class,eqAllData);
//   queryEngine.removeData(queryKey,KeyPersonBean.class,eqAllData);
 //queryEngine.removeData(queryKey,KeyPersonUnitBean.class,eqAllData);
    queryEngine.addCollection(queryKey, KeyPersonUnitBean.class, kpunit);
    queryEngine.addCollection(queryKey, KeyPersonBean.class, kps);
    setDataChanged(false);
}
}

  public  Vector getFormUnitData() {

        try {
            return (Vector) queryEngine.getDetails(queryKey,KeyPersonUnitBean.class);
        } catch (CoeusException ex) {
            Logger.getLogger(ProposalKeyPersonController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
   
   public  Vector getFormkpData() {


       //for adding the details to the prop key person table
       //for that we have to use addpropperson and deletepropperson


        try {
            CoeusVector forpropperson=queryEngine.getDetails(queryKey,ProposalKeyPersonFormBean.class);
            
            return (Vector) forpropperson;
        } catch (CoeusException ex) {
            Logger.getLogger(ProposalKeyPersonController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

