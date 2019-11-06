/*
 * UnitHierarchyDataTxnBean.java
 *
 * Created on August 27, 2002, 8:33 PM
 */

package edu.mit.coeus.unit.bean;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Iterator;

import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.budget.bean.RateClassBean;
import edu.mit.coeus.budget.bean.RateTypeBean;
import edu.mit.coeus.budget.bean.InstituteLARatesBean;
import edu.mit.coeus.utils.CoeusVector;
import java.sql.Date;
import edu.mit.coeus.user.bean.UserDelegationsBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
//Added for unit hierarchy enhancement start 1 by tarique
import edu.mit.coeus.utils.ComboBoxBean;
//Added for unit hierarchy enhancement end 1 by tarique

/**
 *
 * @author  geo
 * @version
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling.
 * * @modified by ele
 * @date 05-2-04
 * Description : added getUnitDetailsWithMajorSubdivision  used for printing

 */
public class UnitDataTxnBean {
    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    private String userId;

    /** Creates new UnitHierarchyDataTxnBean */
    public UnitDataTxnBean() {
        dbEngine = new DBEngineImpl();
    }

    /** Creates new UnitHierarchyDataTxnBean */
    public UnitDataTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        //new UnitDataTxnBean();
    }

    /**
     *  Method used to get all the details of a Unit.
     *  <li>To fetch the data, it uses dw_get_unit_detail procedure.
     *  @param String Unit Number
     *  @return UnitDetailBean
     *  @exception DBException
     *  @exception CoeusException
     *
     */
    public UnitDetailFormBean getUnitDetails(String unitNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        UnitDetailFormBean unitDetail = null;
        param.addElement(new Parameter("UNIT_NUMBER","String",unitNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_unit_detail_new ( <<UNIT_NUMBER>> , <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            unitDetail = new UnitDetailFormBean();
            HashMap unitDetailRow = (HashMap)result.elementAt(0);
            unitDetail.setUnitNumber( (String) unitDetailRow.get("UNIT_NUMBER"));
            unitDetail.setUnitName( (String) unitDetailRow.get("UNIT_NAME"));
            unitDetail.setAdminOfficerId( (String) unitDetailRow.get("ADMINISTRATIVE_OFFICER"));
            unitDetail.setAdminOfficerName( (String) unitDetailRow.get("ADMINISTRATIVE_OFFICER_NAME"));
            unitDetail.setUnitHeadId( (String) unitDetailRow.get("UNIT_HEAD"));
            unitDetail.setUnitHeadName( (String) unitDetailRow.get("UNIT_HEAD_NAME"));
            unitDetail.setDeanVpId( (String) unitDetailRow.get("DEAN_VP"));
            unitDetail.setDeanVpName( (String) unitDetailRow.get("DEAN_VP_NAME"));
            unitDetail.setOtherIndToNotifyId( (String) unitDetailRow.get("OTHER_INDIVIDUAL_TO_NOTIFY"));
            unitDetail.setOtherIndToNotifyName( (String) unitDetailRow.get("OTHER_IND_TO_NOTIFY_NAME"));
            unitDetail.setOspAdminId( (String) unitDetailRow.get("OSP_ADMINISTRATOR"));
            unitDetail.setOspAdminName( (String) unitDetailRow.get("OSP_ADMINISTRATOR_NAME"));
            unitDetail.setUpdateTimestamp(unitDetailRow.get("UPDATE_TIMESTAMP").toString());
            unitDetail.setUpdateUser( (String) unitDetailRow.get("UPDATE_USER"));

            /* JM 7-14-2015 added status */
            unitDetail.setStatus( (String) unitDetailRow.get("STATUS"));
            /* JM END */
            
            //added for unit detail - organization search - start 1
            unitDetail.setOrganizationId((String)unitDetailRow.get("ORGANIZATION_ID"));
            String organisationName = getOrganisationName((String)unitDetailRow.get("ORGANIZATION_ID"));
            if(organisationName == null || organisationName.trim().equals("")){
                organisationName = "";
            }
            unitDetail.setOrganizationName(organisationName);
            //added for unit detail - organization search - end 1

            // adding the parent name
            unitDetail.setParentUnitNumber(
                   ((UnitHierarchyFormBean) getHierarchyNode(unitNumber)).getParentNodeID() );
            unitDetail.setParentUnitName(getUnitName(unitDetail.getParentUnitNumber()));
            
        }
        return unitDetail;
    }
    
     /**
     *  Method used to get all the details of a Unit including MajorSubdivision
     *  <li>To fetch the data, it uses  get_major_subdivision procedure
     *  @param String Unit Number
     *  @return UnitDetailBean
     *  @exception DBException
     *  @exception CoeusException
     *
     */
        public UnitDetailFormBean getUnitDetailsWithMajorSubdivision(String unitNumber)
            throws CoeusException, DBException{
       

        UnitDetailFormBean unitDetail = getUnitDetails(unitNumber);
        String unitName;
       
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("UNITNUMBER","String",unitNumber.trim()));

            
        //calling stored procedure
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_major_subdivision( << UNITNUMBER >> , << OUT STRING MAJSUBUNITNAME >> ) ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){

            HashMap rowName = (HashMap)result.elementAt(0);
            unitName = (String) rowName.get("MAJSUBUNITNAME");
            unitDetail.setMajorSubdivisionUnitName(unitName);
        }
        return unitDetail;
    }
      
    /* JM 7-23-2015 custom get hierarchy method */
    /**
     *  Custom method used to get the tree details of Unit hierarchy FOR ACTIVE UNITS ONLY
     *  @return collection of UnitHierarchyBean
     *  @exception DBException
     *
     */
    public Vector getCustomUnitHierarchyDetails() throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        if (dbEngine!=null) {
             result = dbEngine.executeRequest("Coeus",
            "call vu_get_unit_hierarchy ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int treeSize = result.size();
        Vector unitTreeNodes = new Vector(3,2);
        for(int rowIndex=0;rowIndex<treeSize;rowIndex++){
            UnitHierarchyFormBean unitHierarchy = new UnitHierarchyFormBean();
            HashMap unitHierarchyRow = (HashMap) result.elementAt(rowIndex);
            unitHierarchy.setNodeID((String) unitHierarchyRow.get("UNIT_NUMBER"));
            unitHierarchy.setParentNodeID((String) unitHierarchyRow.get("PARENT_UNIT_NUMBER"));
            unitHierarchy.setChildrenFlag((String) unitHierarchyRow.get("HAS_CHILDREN_FLAG"));
            unitHierarchy.setUpdateTimestamp(unitHierarchyRow.get("UPDATE_TIMESTAMP").toString());
            unitHierarchy.setUpdateUser((String) unitHierarchyRow.get("UPDATE_USER"));
            unitHierarchy.setNodeName((String) unitHierarchyRow.get("UNIT_NAME"));
            unitHierarchy.setStatus((String) unitHierarchyRow.get("STATUS"));
            
            unitTreeNodes.addElement(unitHierarchy);
        }
        return unitTreeNodes;
    }
    /* JM END */
        
    /**
     *  Method used to get the tree details of Unit hierarchy.
     *  <li>To fetch the data, it uses get_unit_hierarchy_dw procedure.
     *  @return collection of UnitHierarchyBean
     *  @exception DBException
     *
     */
    public Vector getUnitHierarchyDetails() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        if(dbEngine!=null){
             result = dbEngine.executeRequest("Coeus",
            "call get_unit_hierarchy ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int treeSize = result.size();
        Vector unitTreeNodes = new Vector(3,2);
        for(int rowIndex=0;rowIndex<treeSize;rowIndex++){
            UnitHierarchyFormBean unitHierarchy = new UnitHierarchyFormBean();
            HashMap unitHierarchyRow = (HashMap)result.elementAt(rowIndex);
            //System.out.println("hierachy row=>"+unitHierarchyRow);
            unitHierarchy.setNodeID( (String) unitHierarchyRow.get("UNIT_NUMBER"));
            unitHierarchy.setParentNodeID( (String) unitHierarchyRow.get("PARENT_UNIT_NUMBER"));
            unitHierarchy.setChildrenFlag( (String) unitHierarchyRow.get("HAS_CHILDREN_FLAG"));
            unitHierarchy.setUpdateTimestamp(unitHierarchyRow.get("UPDATE_TIMESTAMP").toString());
            unitHierarchy.setUpdateUser( (String) unitHierarchyRow.get("UPDATE_USER"));
            unitHierarchy.setNodeName( (String) unitHierarchyRow.get("UNIT_NAME"));
            //System.out.println(unitHierarchy.hasChildren());

            unitTreeNodes.addElement(unitHierarchy);
        }
        return unitTreeNodes;
    }

    /**
     *  This method is used to check person details is valid name or not ,then get the id
     *  title and homeunit for the name.
     *  <li>To fetch the data, it uses get_person_info_name procedure.
     *
     *  @param fullName String
     *  @return resultData Vector
     *  @exception DBException,SQLException,Exception
     */
    public String getPersonID(String fullName) throws CoeusException, DBException{
        Vector resultData = new Vector();
        Vector param= new Vector();
        Vector result = new Vector();
        //System.out.println(" inside the Txn Bean >>>>>"+fullName);
        String personID = "";
        String directoryTitle = "";
        String facultyFlag = "";
        String homeUnit = "";

        param.add(new Parameter("FULLNAME","String",fullName.trim()));

        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_person_info_name( << FULLNAME >> , "+
                    "<< OUT STRING PERSONID >> ,  << OUT STRING TITLE >> , "+
                    "<< OUT STRING FLAG >> , << OUT STRING UNITHOME >> ) ",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowPerson = (HashMap)result.elementAt(0);
            personID = (String)rowPerson.get("PERSONID");
/*            directoryTitle = rowPerson.get("TITLE").toString();
            facultyFlag = rowPerson.get("FLAG").toString();
            homeUnit = rowPerson.get("UNITHOME").toString();
 */
        }
        return personID;
    }

     /**
     *  This method is used to get unit name for the given unit number
     *  <li>To fetch the data, it uses get_unit_name procedure.
     *
     *  @param unitNumber String
     *  @exception DBException,SQLException,Exception
     */
    public void checkDuplicateUnitNumber(String unitNumber)
            throws DBException,CoeusException{
        Vector param= new Vector();
        Vector result = new Vector();

        String unitName = null;
        param.add(new Parameter("UNITNUMBER","String",unitNumber.trim()));

        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_unit_name( << UNITNUMBER >> , << OUT STRING UNITNAME >> ) ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            throw new CoeusException("Unit Number "+ unitNumber +
                " is already existing. Please enter a different Unit Number");
        }
    }
    
  
    //Added for unit hierarchy enhancement start 2 by tarique
    /**
	 * This method is used to get the UnitAdminData.
	 * The stored procedure used is GET_UNIT_ADMINISTRATORS
	 * @return CoeusVector
	 * @exception DBException if any error during database transaction.
         * @exception CoeusException if the instance of dbEngine is not available.
	 */
    
    public CoeusVector getAdminData(String unitNumber) throws CoeusException, DBException{
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap hmAdminType = null;
        UnitAdministratorBean unitAdministratorBean = null;
        CoeusVector cvAdminData = null;
        param.addElement(new Parameter("AV_UNIT_NUMBER",
        DBEngineConstants.TYPE_STRING,unitNumber.trim()));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_UNIT_ADMINISTRATORS ( << AV_UNIT_NUMBER >>,<<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0){
            cvAdminData = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                unitAdministratorBean = new UnitAdministratorBean();
                hmAdminType = (HashMap)result.elementAt(index);
                unitAdministratorBean.setUnitNumber((String)hmAdminType.get("UNIT_NUMBER"));
                int adminTypeCode = Integer.parseInt(hmAdminType.get("UNIT_ADMINISTRATOR_TYPE_CODE").toString());
                unitAdministratorBean.setUnitAdminTypeCode(adminTypeCode);
                unitAdministratorBean.setAdministrator((String)hmAdminType.get("ADMINISTRATOR"));// Person Id
                unitAdministratorBean.setPersonName((String)hmAdminType.get("FULL_NAME"));
                unitAdministratorBean.setUpdateTimestamp((Timestamp)hmAdminType.get("UPDATE_TIMESTAMP"));
                unitAdministratorBean.setUpdateUser((String)hmAdminType.get("UPDATE_USER"));
                
                unitAdministratorBean.setAwUnitNumber(unitAdministratorBean.getUnitNumber());
                unitAdministratorBean.setAwUnitAdminTypeCode(unitAdministratorBean.getUnitAdminTypeCode());
                unitAdministratorBean.setAwAdministrator(unitAdministratorBean.getAdministrator());
                cvAdminData.addElement(unitAdministratorBean);
            }
        }
        return cvAdminData;
    }
    
    /**
	 * This method is used to get the Unit Admin Type Code.
	 * The stored procedure used is GET_UNIT_ADMINISTRATOR_TYPE
	 * @return CoeusVector
	 * @exception DBException if any error during database transaction.
         * @exception CoeusException if the instance of dbEngine is not available.
	 */
   
    public CoeusVector getAdminTypeCode() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        CoeusVector types = new CoeusVector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_UNIT_ADMINISTRATOR_TYPE  ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            HashMap adminTypeRow = (HashMap)result.elementAt(i);
            types.addElement(new ComboBoxBean(adminTypeRow.get(
            "UNIT_ADMINISTRATOR_TYPE_CODE").toString(),
            adminTypeRow.get("DESCRIPTION").toString()));
        }
        return types;
    }
    //Added for unit hierarchy enhancement end 2 by tarique

     /**
     *  This method is used to get parent unit name for the given unit number
     *  <li>To fetch the data, it uses get_unit_name procedure.
     *
     *  @param unitNumber String
     *  @return unitname String
     *  @exception DBException,SQLException,Exception
     */
    public String getUnitName(String unitNumber)
            throws DBException,CoeusException{
        Vector param= new Vector();
        Vector result = new Vector();

        String unitName = null;
        //Added for ISSUEID #1554 Message box popup does not Close- Funding Source - start
        if(unitNumber != null) {
            unitNumber = unitNumber.trim();
        }
        param.add(new Parameter("UNITNUMBER","String",unitNumber));
        //Added for ISSUEID #1554 Message box popup does not Close- Funding Source - end
        
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_unit_name( << UNITNUMBER >> , << OUT STRING UNITNAME >> ) ",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){

            HashMap rowName = (HashMap)result.elementAt(0);
            unitName = (String) rowName.get("UNITNAME");
        }
        return unitName;
    }

    /**
     *  Method used to update/insert/delete all the details of a Unit.
     *  <li>To fetch the data, it uses dw_get_unit_detail procedure.
     *  @param String Unit Number
     *  @return UnitDetailBean
     *  @exception DBException
     *  @exception CoeusException
     *
     */
    public boolean addUpdDelUnitDetails(Hashtable addUpdateNodes)
            throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        Vector entireProcs = new Vector(5,3);
        boolean success = false;

        Hashtable newNodes = (Hashtable)addUpdateNodes.get("NEW_NODES");
        Hashtable updateNodes = (Hashtable)addUpdateNodes.get("UPDATE_NODES");

        entireProcs.addAll(packInsertProcedureParams( newNodes ));
        entireProcs.addAll(packProcedureParams(updateNodes,"U"));

        if(dbEngine==null){
            throw new CoeusException("db_exceptionCode.1000");
        }
        dbEngine.executeStoreProcs(entireProcs);
        success = true;
        return success;
    }

    public Vector packProcedureParams(Hashtable nodes,String acType)
            throws DBException,CoeusException{

        Vector param = null;
        Vector paramHierarchy = null;
        Vector procedures = new Vector(5,3);

        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();

        Enumeration nodeEnum = nodes.keys();
        while(nodeEnum.hasMoreElements()){
            String unitNumber = (String)nodeEnum.nextElement();
            UnitHierarchyFormBean unitHierarchyBean =
                (UnitHierarchyFormBean)nodes.get(unitNumber);

            //Case #2310 Start 1
            /*UnitDetailFormBean unitDetail = validateUnitData(
                    unitHierarchyBean.getUnitDetail());*/
            /*
             *Checking the unitdetail is null or not to avoid nullpointerexception
             * Geo on 06-Sep-2006
             */
//            UnitDetailFormBean unitDetail = validateUsrId(unitHierarchyBean.getUnitDetail());
            
            UnitDetailFormBean unitDetail = unitHierarchyBean.getUnitDetail();
            //Case #2310 End 1
            
            param = new Vector();
            paramHierarchy = new Vector();
            if(unitDetail!=null){
                //Calling validate method if unitdetail is not null
                validateUsrId(unitDetail);
                
                param.addElement(new Parameter("UNIT_NUMBER","String",unitNumber));
                param.addElement(new Parameter("UNIT_NAME","String",unitDetail.getUnitName()));
                param.addElement(new Parameter("ADMIN_OFFICER","String",unitDetail.getAdminOfficerId()));
                param.addElement(new Parameter("OSP_ADMIN","String",unitDetail.getOspAdminId()));
                param.addElement(new Parameter("UNIT_HEAD","String",unitDetail.getUnitHeadId()));
                param.addElement(new Parameter("DEAN_VP","String",unitDetail.getDeanVpId()));
                param.addElement(new Parameter("OTHER_IND_TO_NOTIFY","String",unitDetail.getOtherIndToNotifyId()));
                param.addElement(new Parameter("UPDATE_TIMESTAMP","Timestamp",dbTimestamp));
                param.addElement(new Parameter("UPDATE_USER","String",userId));                
                
                //case id - 2593 - Premium - Maintain Unit - Organization Entry - start
                if(unitDetail.getOrganizationName().equals("")){
                    param.addElement(new Parameter("ORGANIZATION_ID","String",""));
                }else{
                    //added for unit detail - organization search - start 2
                    param.addElement(new Parameter("ORGANIZATION_ID","String",unitDetail.getOrganizationId()));
                    //added for unit detail - organization search - end 2                    
                }
                //case id - 2593 - Premium - Maintain Unit - Organization Entry - end              
                
                /* JM 7-14-2015 added status */
                param.addElement(new Parameter("STATUS","String",unitDetail.getStatus()));   
                /* JM END */
                
                param.addElement(new Parameter("W_UNIT_NUMBER","String",unitNumber));
                param.addElement(new Parameter("W_UPDATE_TIMESTAMP","Timestamp",unitDetail.getUpdateTimestamp()));
                param.addElement(new Parameter("AC_TYPE","String",acType));

                StringBuffer sqlCommand = new StringBuffer("call dw_update_unit (");
                sqlCommand.append(" <<UNIT_NUMBER>> , ");
                sqlCommand.append(" <<UNIT_NAME>> , ");
                sqlCommand.append(" <<ADMIN_OFFICER>> , ");
                sqlCommand.append(" <<OSP_ADMIN>> , ");
                sqlCommand.append(" <<UNIT_HEAD>> , ");
                sqlCommand.append(" <<DEAN_VP>> , ");
                sqlCommand.append(" <<OTHER_IND_TO_NOTIFY>> , ");
                sqlCommand.append(" <<UPDATE_TIMESTAMP>> , ");
                sqlCommand.append(" <<UPDATE_USER>> , ");
                //added for unit detail - organization search - start 3
                sqlCommand.append(" <<ORGANIZATION_ID>> , ");
                //added for unit detail - organization search - end 3
                /* JM 7-14-2015 added status */
                sqlCommand.append(" <<STATUS>> , ");                
                /* JM END */
                sqlCommand.append(" <<W_UNIT_NUMBER>> , ");
                sqlCommand.append(" <<W_UPDATE_TIMESTAMP>> , ");
                sqlCommand.append(" <<AC_TYPE>> )");

                ProcReqParameter procUnitDetail  = new ProcReqParameter();
                procUnitDetail.setDSN("Coeus");
                procUnitDetail.setParameterInfo(param);
                procUnitDetail.setSqlCommand(sqlCommand.toString());

                procedures.addElement(procUnitDetail);
            }

            paramHierarchy.addElement(new Parameter("UNIT_NUMBER","String",
                unitNumber));
            paramHierarchy.addElement(new Parameter("PARENT_UNIT_NUMBER","String",
                unitHierarchyBean.getParentNodeID()));
            paramHierarchy.addElement(new Parameter("HAS_CHILDREN_FLAG",
                "String",unitHierarchyBean.hasChildren()?"Y":"N"));
            paramHierarchy.addElement(new Parameter("UPDATE_USER",
                "String",userId));
            paramHierarchy.addElement(new Parameter("UPDATE_TIMESTAMP","Timestamp",
                dbTimestamp));
            paramHierarchy.addElement(new Parameter("W_UNIT_NUMBER",
                "String",unitNumber));
            paramHierarchy.addElement(new Parameter("W_UPDATE_TIMESTAMP",
                "Timestamp",unitHierarchyBean.getUpdateTimestamp()));
            paramHierarchy.addElement(new Parameter("AC_TYPE","String",acType));
            StringBuffer sqlCommandHierarchy = new StringBuffer(
                    "call dw_update_unit_hierarchy (");
            sqlCommandHierarchy.append(" <<UNIT_NUMBER>> , ");
            sqlCommandHierarchy.append(" <<PARENT_UNIT_NUMBER>> , ");
            sqlCommandHierarchy.append(" <<HAS_CHILDREN_FLAG>> , ");
            sqlCommandHierarchy.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlCommandHierarchy.append(" <<UPDATE_USER>> , ");
            sqlCommandHierarchy.append(" <<W_UNIT_NUMBER>> , ");
            sqlCommandHierarchy.append(" <<W_UPDATE_TIMESTAMP>> , ");
            sqlCommandHierarchy.append(" <<AC_TYPE>> )");

            ProcReqParameter procUnitHierarchy  = new ProcReqParameter();
            procUnitHierarchy.setDSN("Coeus");
            procUnitHierarchy.setParameterInfo(paramHierarchy);
            procUnitHierarchy.setSqlCommand(sqlCommandHierarchy.toString());

            procedures.addElement(procUnitHierarchy);
        }
        return procedures;
    }

    public Vector packInsertProcedureParams( Hashtable nodes )
            throws DBException,CoeusException{

        Vector param = null;
        Vector paramHierarchy = null;
        Vector procedures = new Vector(5,3);
        String acType = "I";
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();

        Iterator nodeEnum = nodes.keySet().iterator();

       while( nodeEnum.hasNext() ){

            String unitNumber = (String)nodeEnum.next();
            UnitHierarchyFormBean unitHierarchyBean =
                (UnitHierarchyFormBean)nodes.get(unitNumber);

            //This check to order the parent nodes in order like grand-parent,
            //parent, child etc. to the order of procedure execution against db.
            if( ! nodes.containsKey( unitHierarchyBean.getParentNodeID() ) ){

             nodes.remove( unitNumber );
             nodeEnum = nodes.keySet().iterator();

            //Case #2310 Start 2
            /*UnitDetailFormBean unitDetail = validateUnitData(
                    unitHierarchyBean.getUnitDetail());*/
             
             UnitDetailFormBean unitDetail = validateUsrId(unitHierarchyBean.getUnitDetail());
            //Case #2310 End 2
            
            param = new Vector();
            paramHierarchy = new Vector();
            if(unitDetail!=null){
                param.addElement(new Parameter("UNIT_NUMBER","String",unitNumber));
                param.addElement(new Parameter("UNIT_NAME","String",unitDetail.getUnitName()));
                param.addElement(new Parameter("ADMIN_OFFICER","String",unitDetail.getAdminOfficerId()));
                param.addElement(new Parameter("OSP_ADMIN","String",unitDetail.getOspAdminId()));
                param.addElement(new Parameter("UNIT_HEAD","String",unitDetail.getUnitHeadId()));
                param.addElement(new Parameter("DEAN_VP","String",unitDetail.getDeanVpId()));
                param.addElement(new Parameter("OTHER_IND_TO_NOTIFY","String",unitDetail.getOtherIndToNotifyId()));
                param.addElement(new Parameter("UPDATE_TIMESTAMP","Timestamp",dbTimestamp));
                param.addElement(new Parameter("UPDATE_USER","String",userId));
                //case id - 2593 - Premium - Maintain Unit - Organization Entry - start
                if(unitDetail.getOrganizationName().equals("")){
                    param.addElement(new Parameter("ORGANIZATION_ID","String",""));
                }else{
                    //added for unit detail - organization search - start 4
                    param.addElement(new Parameter("ORGANIZATION_ID","String",unitDetail.getOrganizationId()));
                    //added for unit detail - organization search - end 4                    
                }
                
                /* JM 7-14-2015 added status */
                param.addElement(new Parameter("STATUS","String",unitDetail.getStatus()));   
                /* JM END */
                
                //case id - 2593 - Premium - Maintain Unit - Organization Entry - end
                param.addElement(new Parameter("W_UNIT_NUMBER","String",unitNumber));
                param.addElement(new Parameter("W_UPDATE_TIMESTAMP","Timestamp",unitDetail.getUpdateTimestamp()));
                param.addElement(new Parameter("AC_TYPE","String",acType));

                StringBuffer sqlCommand = new StringBuffer("call dw_update_unit (");
                sqlCommand.append(" <<UNIT_NUMBER>> , ");
                sqlCommand.append(" <<UNIT_NAME>> , ");
                sqlCommand.append(" <<ADMIN_OFFICER>> , ");
                sqlCommand.append(" <<OSP_ADMIN>> , ");
                sqlCommand.append(" <<UNIT_HEAD>> , ");
                sqlCommand.append(" <<DEAN_VP>> , ");
                sqlCommand.append(" <<OTHER_IND_TO_NOTIFY>> , ");
                sqlCommand.append(" <<UPDATE_TIMESTAMP>> , ");
                sqlCommand.append(" <<UPDATE_USER>> , ");
                //added for unit detail - organization search - start 5
                sqlCommand.append(" <<ORGANIZATION_ID>> , ");
                //added for unit detail - organization search - end 5
                /* JM 7-14-2015 added status */
                sqlCommand.append(" <<STATUS>> , ");                
                /* JM END */
                sqlCommand.append(" <<W_UNIT_NUMBER>> , ");
                sqlCommand.append(" <<W_UPDATE_TIMESTAMP>> , ");
                sqlCommand.append(" <<AC_TYPE>> )");

                ProcReqParameter procUnitDetail  = new ProcReqParameter();
                procUnitDetail.setDSN("Coeus");
                procUnitDetail.setParameterInfo(param);
                procUnitDetail.setSqlCommand(sqlCommand.toString());

                procedures.addElement(procUnitDetail);
            }

            paramHierarchy.addElement(new Parameter("UNIT_NUMBER","String",
                unitNumber));
            paramHierarchy.addElement(new Parameter("PARENT_UNIT_NUMBER","String",
                unitHierarchyBean.getParentNodeID()));
            paramHierarchy.addElement(new Parameter("HAS_CHILDREN_FLAG",
                "String",unitHierarchyBean.hasChildren()?"Y":"N"));
            paramHierarchy.addElement(new Parameter("UPDATE_USER",
                "String",userId));
            paramHierarchy.addElement(new Parameter("UPDATE_TIMESTAMP","Timestamp",
                dbTimestamp));
            paramHierarchy.addElement(new Parameter("W_UNIT_NUMBER",
                "String",unitNumber));
            paramHierarchy.addElement(new Parameter("W_UPDATE_TIMESTAMP",
                "Timestamp",unitHierarchyBean.getUpdateTimestamp()));
            paramHierarchy.addElement(new Parameter("AC_TYPE","String",acType));
            StringBuffer sqlCommandHierarchy = new StringBuffer(
                    "call dw_update_unit_hierarchy (");
            sqlCommandHierarchy.append(" <<UNIT_NUMBER>> , ");
            sqlCommandHierarchy.append(" <<PARENT_UNIT_NUMBER>> , ");
            sqlCommandHierarchy.append(" <<HAS_CHILDREN_FLAG>> , ");
            sqlCommandHierarchy.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlCommandHierarchy.append(" <<UPDATE_USER>> , ");
            sqlCommandHierarchy.append(" <<W_UNIT_NUMBER>> , ");
            sqlCommandHierarchy.append(" <<W_UPDATE_TIMESTAMP>> , ");
            sqlCommandHierarchy.append(" <<AC_TYPE>> )");

            ProcReqParameter procUnitHierarchy  = new ProcReqParameter();
            procUnitHierarchy.setDSN("Coeus");
            procUnitHierarchy.setParameterInfo(paramHierarchy);
            procUnitHierarchy.setSqlCommand(sqlCommandHierarchy.toString());

            procedures.addElement(procUnitHierarchy);
            }
        }
        return procedures;
    }


    /**
     *  Method used to validate the unit data which comes with Unit detail form bean
     *  Once its through with all the validations, it will return the UnitDetailFormBean
     *  with all the person names which required for the display purpose on the
     *  UnitDetail screen.
     *  @param UnitDetailFormBean instance
     *  @return UnitDetailFormBean instance
     *  @exception CoeusException
     *  @exception DBException
     */
    public UnitDetailFormBean validateUnitData(
                                    UnitDetailFormBean unitDetail)
            throws CoeusException,DBException{
        if(unitDetail==null)
            return null;
        unitDetail.setAdminOfficerId(validateName(
                    unitDetail.getAdminOfficerName()));
        unitDetail.setUnitHeadId(validateName(unitDetail.getUnitHeadName()));
        unitDetail.setDeanVpId(validateName(unitDetail.getDeanVpName()));
        unitDetail.setOtherIndToNotifyId(validateName(
                unitDetail.getOtherIndToNotifyName()));
        unitDetail.setOspAdminId(validateName(unitDetail.getOspAdminName()));
        return unitDetail;
    }

    /*
     *  Method used to check whether the name is valid or not.
     */
    private String validateName(String personName) throws DBException,
            CoeusException{
        if(personName==null || personName.trim().equals("")){
            return null;
        }
        String personId = getPersonID(personName);
        final String TOO_MANY = "TOO_MANY";
        if ( personId ==null){
            throw new CoeusException("uDtTxn_exceptionCode.1015");
        }else if (personId.equalsIgnoreCase(TOO_MANY)){
            throw new CoeusException("uDtTxn_exceptionCode.1016");
        }
        return personId;
    }
     /**
     *  Method used to get all the details of a Unit.
     *  <li>To fetch the data, it uses dw_get_unit_detail procedure.
     *  @param String Unit Number
     *  @return UnitDetailBean
     *  @exception DBException
     *  @exception CoeusException
     *
     */
    public UnitHierarchyFormBean getHierarchyNode(String unitNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        UnitHierarchyFormBean hierarchyNode = new UnitHierarchyFormBean();
        param.addElement(new Parameter("UNIT_NUMBER","String",unitNumber.toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_unit_hierarchy_node ( <<UNIT_NUMBER>> , <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap unitHierarchyRow = (HashMap)result.elementAt(0);
            hierarchyNode.setNodeID( (String)
            unitHierarchyRow.get("UNIT_NUMBER"));
            hierarchyNode.setNodeName( (String)
            unitHierarchyRow.get("UNIT_NAME"));
            hierarchyNode.setParentNodeID( (String)
            unitHierarchyRow.get("PARENT_UNIT_NUMBER"));
            hierarchyNode.setChildrenFlag( (String)
            unitHierarchyRow.get("HAS_CHILDREN_FLAG"));
            hierarchyNode.setUpdateTimestamp(unitHierarchyRow.get("UPDATE_TIMESTAMP").toString());
            hierarchyNode.setUpdateUser( (String)unitHierarchyRow.get("UPDATE_USER"));
        }
        return hierarchyNode;
    }

    /**
     *  Method used to update/insert/delete all the details of a Unit.
     *  <li>To fetch the data, it uses dw_get_unit_detail procedure.
     *  @param String Unit Number
     *  @return UnitDetailBean
     *  @exception DBException
     *  @exception CoeusException
     *
     */
    public boolean addUpdDelUnitHierarchy(Hashtable hierarchyNodes,
            char acType) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector procedures = new Vector(5,3);
        Vector paramHierarchy = null;

        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        Enumeration enums = hierarchyNodes.keys();
        while(enums.hasMoreElements()){
            paramHierarchy = new Vector();
            String unitNumber = (String)enums.nextElement();
            UnitHierarchyFormBean unitHierarchy =
                        (UnitHierarchyFormBean)hierarchyNodes.get(unitNumber);
            paramHierarchy.addElement(new Parameter("UNIT_NUMBER","String",unitNumber));
            paramHierarchy.addElement(new Parameter("PARENT_UNIT_NUMBER","String",unitHierarchy.getParentNodeID()));
            paramHierarchy.addElement(new Parameter("HAS_CHILDREN_FLAG","String",unitHierarchy.hasChildren()?"Y":"N"));
            paramHierarchy.addElement(new Parameter("UPDATE_TIMESTAMP","Date",dbTimestamp));
            paramHierarchy.addElement(new Parameter("UPDATE_USER","String",userId));
            paramHierarchy.addElement(new Parameter("W_UNIT_NUMBER","String",unitNumber));
            paramHierarchy.addElement(new Parameter("W_UPDATE_TIMESTAMP","Date",unitHierarchy.getUpdateTimestamp()));
            paramHierarchy.addElement(new Parameter("AC_TYPE","String",new Character(acType).toString()));

            StringBuffer sqlCommandHierarchy = new StringBuffer("call dw_update_unit_hierarchy (");
            sqlCommandHierarchy.append(" <<UNIT_NUMBER>> , ");
            sqlCommandHierarchy.append(" <<PARENT_UNIT_NUMBER>> , ");
            sqlCommandHierarchy.append(" <<HAS_CHILDREN_FLAG>> , ");
            sqlCommandHierarchy.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlCommandHierarchy.append(" <<UPDATE_USER>> , ");
            sqlCommandHierarchy.append(" <<W_UNIT_NUMBER>> , ");
            sqlCommandHierarchy.append(" <<W_UPDATE_TIMESTAMP>> , ");
            sqlCommandHierarchy.append(" <<AC_TYPE>> )");

            ProcReqParameter procUnitHierarchy  = new ProcReqParameter();
            procUnitHierarchy.setDSN("Coeus");
            procUnitHierarchy.setParameterInfo(paramHierarchy);
            procUnitHierarchy.setSqlCommand(sqlCommandHierarchy.toString());
            procedures.addElement(procUnitHierarchy);
        }

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    /**
     *  This method gets all Rate Class List
     *
     *  To fetch the data, it uses the procedure GET_RATE_CLASS_LIST.
     *
     *  @return Vector collection of ComboBox beans
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getRateClassList() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap row = null;
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_RATE_CLASS_LIST ( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector coeusVector = null;
        if (listSize > 0){
            coeusVector = new CoeusVector();
            RateClassBean rateClassBean = null;
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                row = (HashMap)result.elementAt(rowIndex);
                rateClassBean = new RateClassBean();
                rateClassBean.setCode(row.get("RATE_CLASS_CODE") == null ? 
                        "" : row.get("RATE_CLASS_CODE").toString());
                rateClassBean.setDescription(row.get("DESCRIPTION") == null ? 
                        "" : row.get("DESCRIPTION").toString());   
                rateClassBean.setRateClassType(row.get("RATE_CLASS_TYPE") == null ? 
                        "" : row.get("RATE_CLASS_TYPE").toString());                
                coeusVector.addElement(rateClassBean);
            }
        }
        return coeusVector;
    }
    
    /**
     *  This method gets all Rate Type List
     *
     *  To fetch the data, it uses the procedure DW_GET_RATE_TYPE_LIST.
     *
     *  @return Vector collection of ComboBox beans
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getRateTypeList() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap row = null;
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_RATE_TYPE_LIST ( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector coeusVector = null;
        if (listSize > 0){
            coeusVector = new CoeusVector();
            RateTypeBean rateTypeBean = null;
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                row = (HashMap)result.elementAt(rowIndex);
                rateTypeBean = new RateTypeBean();
                rateTypeBean.setRateClassCode(row.get("RATE_CLASS_CODE") == null ? 
                        0 : Integer.parseInt(row.get("RATE_CLASS_CODE").toString()));
                rateTypeBean.setCode(row.get("RATE_TYPE_CODE") == null ? 
                        "" : row.get("RATE_TYPE_CODE").toString());
                rateTypeBean.setDescription(row.get("DESCRIPTION") == null ? 
                        "" : row.get("DESCRIPTION").toString());                        
                coeusVector.addElement(rateTypeBean);
            }
        }
        return coeusVector;
    }    
    
    /** This method get all Institute LA Rates for the given Unit
     *
     * To fetch the data, it uses the procedure DW_GET_INSTITUTE_LA_RATES.
     *
     * @return CoeusVector CoeusVector
     * @param unitNumber String 
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.  
     */
    public CoeusVector getAllInstituteLARatesForUnit(String unitNumber) 
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("UNIT_NUMBER",
            DBEngineConstants.TYPE_STRING, unitNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_INSTITUTE_LA_RATES ( << UNIT_NUMBER >>, "
            +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        InstituteLARatesBean instituteLARatesBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                instituteLARatesBean = new InstituteLARatesBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                instituteLARatesBean.setUnitNumber(unitNumber);
                instituteLARatesBean.setRateClassCode(
                    Integer.parseInt(budgetRow.get(
                    "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                    "RATE_CLASS_CODE").toString()));
                instituteLARatesBean.setRateTypeCode(
                    Integer.parseInt(budgetRow.get(
                    "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                    "RATE_TYPE_CODE").toString()));
                instituteLARatesBean.setFiscalYear(
                    (String)budgetRow.get("FISCAL_YEAR"));               
                instituteLARatesBean.setStartDate(
                    budgetRow.get("START_DATE") == null ? null
                            :new Date( ((Timestamp) budgetRow.get(
                                "START_DATE")).getTime()));
                instituteLARatesBean.setOnOffCampusFlag(
                    budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                    budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
                instituteLARatesBean.setInstituteRate(
                    Double.parseDouble(budgetRow.get(
                    "RATE") == null ? "0" : budgetRow.get(
                    "RATE").toString()));                                    
                instituteLARatesBean.setUpdateTimestamp(
                    (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                instituteLARatesBean.setUpdateUser(
                    (String)budgetRow.get("UPDATE_USER"));
                coeusVector.addElement(instituteLARatesBean);
            }
        }
       return coeusVector;
    }                    
    
    /** This method get Unit Delegations for the given Unit
     *
     * To fetch the data, it uses the procedure DW_GET_UNIT_DELEGATIONS
     *
     * @return CoeusVector CoeusVector
     * @param unitNumber String 
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.  
     */
    public CoeusVector getUnitDelegations(String unitNumber) 
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap row = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("UNIT_NUMBER",
            DBEngineConstants.TYPE_STRING, unitNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_UNIT_DELEGATIONS ( << UNIT_NUMBER >>, "
            +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        UserDelegationsBean userDelegationsBean = null;
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        String delegatedToUserId = "";
        String delegatedToName="";
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                userDelegationsBean = new UserDelegationsBean();
                row = (HashMap) result.elementAt(rowIndex);                
                userDelegationsBean.setDelegatedTo(
                    (String)row.get("DELEGATED_TO"));                
                userDelegationsBean.setAw_DelegatedTo(
                    (String)row.get("DELEGATED_TO"));                
                delegatedToUserId = (String)row.get("DELEGATED_TO");
                //Get User Name for Delegated To User Id
                if(delegatedToUserId!=null){
                    delegatedToName = userMaintDataTxnBean.getUserName(delegatedToUserId);
                }else{
                    delegatedToName = "";
                }                    
                userDelegationsBean.setDelegatedToName(delegatedToName);
                userDelegationsBean.setDelegatedBy(
                    (String)row.get("DELEGATED_BY"));
                userDelegationsBean.setAw_DelegatedBy(
                    (String)row.get("DELEGATED_BY"));                
                userDelegationsBean.setUserName(
                    (String)row.get("DELEGATED_BY_NAME"));
                userDelegationsBean.setEffectiveDate(
                    row.get("EFFECTIVE_DATE") == null ? null
                            :new Date( ((Timestamp) row.get(
                                "EFFECTIVE_DATE")).getTime()) );                
                userDelegationsBean.setAw_EffectiveDate(
                    userDelegationsBean.getEffectiveDate());                
                userDelegationsBean.setStatus(((String)row.get("STATUS")).charAt(0));                            
                userDelegationsBean.setAw_Status(((String)row.get("STATUS")).charAt(0));                                            
                userDelegationsBean.setEndDate(
                    row.get("END_DATE") == null ? null
                            :new Date( ((Timestamp) row.get(
                                "END_DATE")).getTime()) );                                
                
                userDelegationsBean.setUpdateUser((String)row.get("UPDATE_USER"));
                userDelegationsBean.setUpdateTimestamp((java.sql.Timestamp)row.get("UPDATE_TIMESTAMP"));                                              
                coeusVector.addElement(userDelegationsBean);
            }
        }
       return coeusVector;
    }
    
    // JM 06-12-2013 method to retrieve contract admin
    /** This method get the contract administrator for the unit
     *  @param unitNumber String
     *  @return UnitDetailFormBean UnitDetailFormBean
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public UnitDetailFormBean getUnitContractAdministrator(String unitNumber) throws CoeusException, DBException{
        Vector resultData = new Vector();
        Vector param= new Vector();
        Vector result = new Vector();

        param.add(new Parameter("UNIT_NUMBER", 
            DBEngineConstants.TYPE_STRING, unitNumber));

        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call VU_GET_UNIT_CONTRACT_ADMIN( << UNIT_NUMBER >> , "+
                    "<< OUT STRING PERSON_ID >> , << OUT STRING PERSON_NAME >> ) ",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        UnitDetailFormBean unitDetailFormBean = null;
        if(!result.isEmpty()){
            unitDetailFormBean = new UnitDetailFormBean();
            HashMap rowPerson = (HashMap)result.elementAt(0);
            unitDetailFormBean.setUnitNumber(unitNumber);
            unitDetailFormBean.setOspAdminId((String)rowPerson.get("PERSON_ID"));
            unitDetailFormBean.setOspAdminName((String)rowPerson.get("PERSON_NAME"));
            
        }
        return unitDetailFormBean;
    }   
    // END JM
    
    /**
     *  This method is used to get OSP Administrator Name and Id for the given Unit Number
     *  <li>To fetch the data, it uses GET_UNIT_ADMINISTRATOR procedure.
     *
     *  @param unitNumber String
     *  @return UnitDetailFormBean UnitDetailFormBean
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public UnitDetailFormBean getUnitOSPAdministrator(String unitNumber) throws CoeusException, DBException{
        Vector resultData = new Vector();
        Vector param= new Vector();
        Vector result = new Vector();

        param.add(new Parameter("UNIT_NUMBER", 
            DBEngineConstants.TYPE_STRING, unitNumber));

        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_UNIT_ADMINISTRATOR( << UNIT_NUMBER >> , "+
                    "<< OUT STRING PERSON_ID >> , << OUT STRING PERSON_NAME >> ) ",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        UnitDetailFormBean unitDetailFormBean = null;
        if(!result.isEmpty()){
            unitDetailFormBean = new UnitDetailFormBean();
            HashMap rowPerson = (HashMap)result.elementAt(0);
            unitDetailFormBean.setUnitNumber(unitNumber);
            unitDetailFormBean.setOspAdminId((String)rowPerson.get("PERSON_ID"));
            unitDetailFormBean.setOspAdminName((String)rowPerson.get("PERSON_NAME"));
            
        }
        return unitDetailFormBean;
    }    
    
    //Case #2310 Start 3
    private UnitDetailFormBean validateUsrId(UnitDetailFormBean unitDetail){
        if(unitDetail!= null){
            if(checkForNull(unitDetail.getAdminOfficerName())){
                unitDetail.setAdminOfficerId(null);
            }

            if(checkForNull(unitDetail.getUnitHeadName())){
                unitDetail.setUnitHeadId(null);
            }

            if(checkForNull(unitDetail.getDeanVpName())){
                unitDetail.setDeanVpId(null);
            }

            if(checkForNull(unitDetail.getOtherIndToNotifyName())){
                unitDetail.setOtherIndToNotifyId(null);
            }

            if(checkForNull(unitDetail.getOspAdminName())){
                unitDetail.setOspAdminId(null);
            }
        }
        
        return unitDetail;
    }
    
    private boolean checkForNull(String personName){
        
        if(personName==null || personName.trim().equals("")){
            return true;
        }else{
            return false;
        }
    }
    //Case #2310 End 3
    //added for unit detail - organization search - start 5
    public String getOrganisationName(String strOrgId)
         throws CoeusException, DBException {
        String strOrgName = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("as_organization_id",
                                DBEngineConstants.TYPE_STRING,strOrgId));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING ORGNAME>> = "
            +" call fn_get_organization_name(<< as_organization_id >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            strOrgName = (String)rowParameter.get("ORGNAME");
        }
        return strOrgName;
        
    }
    
    public String getOrganisationId(String strOrgName)
         throws CoeusException, DBException {
        String strOrgId = "";
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AV_ORGANIZATION_NAME",
                                DBEngineConstants.TYPE_STRING,strOrgName));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING ORGID >> = "
            +" call FN_GET_ORG_NAME_FOR_ID(<< AV_ORGANIZATION_NAME >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            strOrgId = (String)rowParameter.get("ORGID");
        }
        return strOrgId;
        
    }
    //added for unit detail - organization search - end 5
    
    public static void main(String[] args){
        try{
            UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
            UnitDetailFormBean unitDetailFormBean = unitDataTxnBean.getUnitOSPAdministrator("151021");
            if(unitDetailFormBean!=null){
                System.out.println("Unit Osp Admin Name :"+unitDetailFormBean.getOspAdminName());                
                System.out.println("Unit Osp Admin Id :"+unitDetailFormBean.getOspAdminId());                
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    // Added for COEUSQA-1725 : Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    /**
     * This method get all Unit formualted cost
     * To fetch the data, it uses the procedure GET_UNIT_FORMULATED_COST.
     * @param unitNumber
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @return vctResultSet - CoeusVector
     */
    public CoeusVector getUnitFormulatedCosts(String unitNumber)
    throws DBException, CoeusException{
        Vector result = new Vector();
        CoeusVector vctResultSet = null;
        
        Vector vecParam= new Vector();
        vecParam.addElement(new Parameter("AV_UNIT_NUMBER", DBEngineConstants.TYPE_STRING,unitNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_UNIT_FORMULATED_COST ( <<AV_UNIT_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", vecParam);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        UnitFormulatedCostBean unitFormulatedCostBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    unitFormulatedCostBean = new UnitFormulatedCostBean();
                    HashMap hmUnitFormulatedCost = (HashMap) result.elementAt(rowIndex);
                    unitFormulatedCostBean.setFormulatedCode(Integer.parseInt(hmUnitFormulatedCost.get("FORMULATED_CODE") == null ? "0"
                            : hmUnitFormulatedCost.get("FORMULATED_CODE").toString()));
                    unitFormulatedCostBean.setFormulatedCodeDescription((String)hmUnitFormulatedCost.get("FORMULATED_CODE_DESCRIPTION"));
                    unitFormulatedCostBean.setUnitCost(Double.parseDouble(hmUnitFormulatedCost.get("UNIT_COST") == null ? "0"
                            : hmUnitFormulatedCost.get("UNIT_COST").toString()));
                    unitFormulatedCostBean.setUnitNumber((String)hmUnitFormulatedCost.get("UNIT_NUMBER"));
                    unitFormulatedCostBean.setUpdateTimestamp((Timestamp)hmUnitFormulatedCost.get("UPDATE_TIMESTAMP"));
                    unitFormulatedCostBean.setUpdateUser((String)hmUnitFormulatedCost.get("UPDATE_USER"));
                    unitFormulatedCostBean.setAwFormulatedCode(Integer.parseInt(hmUnitFormulatedCost.get("FORMULATED_CODE") == null ? "0"
                            : hmUnitFormulatedCost.get("FORMULATED_CODE").toString()));
                    unitFormulatedCostBean.setAwUnitNumber((String)hmUnitFormulatedCost.get("UNIT_NUMBER"));
                    unitFormulatedCostBean.setAwUpdateTimestamp((Timestamp)hmUnitFormulatedCost.get("UPDATE_TIMESTAMP"));
                    unitFormulatedCostBean.setAwUpdateUser((String)hmUnitFormulatedCost.get("UPDATE_USER"));
                    
                    vctResultSet.addElement(unitFormulatedCostBean);
                }
            }
        }
        return vctResultSet;
    }
    
    // Added for COEUSQA-1725 : Extend the functionality of Lab Allocation in proposal development budgeting. - Start
}