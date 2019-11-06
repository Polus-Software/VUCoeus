/*
 * @(#)AreaOfResearchTxnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 18-OCT-2010
 * by Johncy M John
 */

package edu.mit.coeus.iacuc.bean;

import java.util.Vector;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Enumeration; 
import java.sql.Timestamp;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.exception.CoeusException;

/**
 * This class is used to represent area of research transaction routines with
 * DBEngine. This includes Build/ Add/ Modify/ Save Area of Research tree structure.
 *
 * @author  Subramanya
 * @version 1.0 September 23, 2002, 7:50 PM
 */
public class AreaOfResearchTxnBean {
    
    // Singleton instance of a dbEngine     
    private DBEngineImpl dbEngine;

    // holds the UserID ( Ex: Logind UserID )
    private String userId;
    
    /** 
     * Creates new AreaOfResearchTxnBean 
     * default constructor with no param 
     */
    public AreaOfResearchTxnBean() {       
        dbEngine = new DBEngineImpl();        
    }
    
    /**
     * Creates new AreaOfResearchTxnBean by accepting PersonID/PersonName.
     * @param userId  String represent the User ID
     */
    public AreaOfResearchTxnBean( String userId ) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }
    
    /**  Method used to get all the details of a Research Area Node(Data bean).
     *  <li>To fetch the data, it uses GET_RESEARCH_AREA procedure.
     * @return AreaOfResearchTreeNodeBean Tree Node Data bean.
     * @param resAreaCode ResearchAreaCode
     * @throws CoeusException if dbEngine is Null.
     * @throws DBException  while performing db operations
     */
    public AreaOfResearchTreeNodeBean getAreaOfResearchDetails( 
                    String resAreaCode ) throws CoeusException, DBException{

        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();        
        // holds the data Object of display AreaOfResearch Tree Node.
        AreaOfResearchTreeNodeBean researchAreaTreeNodeDetail = 
                               new AreaOfResearchTreeNodeBean("", "", false);
        param.addElement( new Parameter( "RESEARCH_AREA_CODE", "String",
						resAreaCode.toString() ));
        if( dbEngine!=null ){
            result = dbEngine.executeRequest( "Coeus",
            "call GET_AC_RESEARCH_AREA_LIST ( <<RESEARCH_AREA_CODE>> ,"+ 
            "<<OUT RESULTSET rset>> )", "Coeus", param );
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        //constuct vector from db result set.
        if( !result.isEmpty() ){
            //Hashtable reseachEntryHash = ( Hashtable )result.elementAt(0);
            HashMap reseachEntryHash = ( HashMap )result.elementAt(0);
            researchAreaTreeNodeDetail.setResearchAreaCode(
             (String)
            reseachEntryHash.get("RESEARCH_AREA_CODE"));
            researchAreaTreeNodeDetail.setRADescription(
             (String)
            reseachEntryHash.get("DESCRIPTION"));
            researchAreaTreeNodeDetail.setParentResearchAreaCode(
             (String)
            reseachEntryHash.get("PARENT_RESEARCH_AREA_CODE"));
            researchAreaTreeNodeDetail.setChildrenFlag(
             (String)
            reseachEntryHash.get("HAS_CHILDREN_FLAG"));
            researchAreaTreeNodeDetail.setUpdateTimestamp(
                reseachEntryHash.get("UPDATE_TIMESTAMP").toString());
            researchAreaTreeNodeDetail.setUpdateUser(
             (String)
            reseachEntryHash.get("UPDATE_USER"));
        }
        return researchAreaTreeNodeDetail;
    }


    /**  Method used to get the tree details of Research Area Tree.
     *  <li>To fetch the data, it uses GET_RESEARCH_AREA_LIST procedure.
     * @return Vector collection of AreaOfResearchTreeNodeBean
     * @throws CoeusException dbEngine is Null.
     * @exception DBException  while performing DB Transaction
     */
    public Vector getResearchHierarchyDetails() throws CoeusException, DBException{

        Vector result = new Vector(3,2);
        Vector param= new Vector();
        if(dbEngine!=null){
             //Execute the  DB Procedure and Stores the result in Vector.
             result = dbEngine.executeRequest("Coeus",
            "call GET_AC_RESEARCH_AREA_LIST ( <<OUT RESULTSET rset>> )", 
            "Coeus", param );
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        int treeSize = result.size();
        Vector researchTreeNodes = new Vector(3,2);
        AreaOfResearchTreeNodeBean researchHierarchyTreeData = null;
        //Hashtable researchHierarchyRow = null;
        HashMap researchHierarchyRow = null;
        //Traverse through all the Result Set Records and constructs The
        //Are of Research Data Bean and puts into Vector.
        
        for( int rowIndex=0; rowIndex < treeSize; rowIndex++ ){
            researchHierarchyTreeData = new AreaOfResearchTreeNodeBean("","",
                                                                        false);
            //researchHierarchyRow = ( Hashtable ) result.elementAt( rowIndex );
            researchHierarchyRow = ( HashMap ) result.elementAt( rowIndex );
            researchHierarchyTreeData.setResearchAreaCode(
             (String)
            researchHierarchyRow.get("RESEARCH_AREA_CODE"));
            researchHierarchyTreeData.setParentResearchAreaCode(
             (String)
            researchHierarchyRow.get("PARENT_RESEARCH_AREA_CODE"));
            researchHierarchyTreeData.setChildrenFlag(
                 (String)
                researchHierarchyRow.get("HAS_CHILDREN_FLAG"));
            researchHierarchyTreeData.setUpdateTimestamp(
                researchHierarchyRow.get("UPDATE_TIMESTAMP").toString());
            researchHierarchyTreeData.setUpdateUser(
             (String)
            researchHierarchyRow.get("UPDATE_USER"));
            researchHierarchyTreeData.setRADescription(
             (String)
            researchHierarchyRow.get("DESCRIPTION"));
            researchTreeNodes.addElement( researchHierarchyTreeData );
        }        
        return researchTreeNodes;        
    }

    
    /**  Method used to update/insert all the details of a Research Area Tree.
     *  <li>To fetch the data, it uses UPD_RESEARCH_AREA procedure.
     * @return boolean status of the db call. true - successful else false.
     * @param hierarchyNodes represent the Hashtable data of TreeNode data Bean.
     * @param acType gives information on type of Operation. 'I' Insert,
     *  'U' update
     * @throws CoeusException if dbEngine is Null.
     * @exception DBException  while performing db Transactions
     */
    public boolean addUpdDelAreaOfResearchData( Hashtable hierarchyNodes,
                              char acType) throws CoeusException, DBException{

        Vector procedures = new Vector(5,3);
        Vector paramHierarchy = null;        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();            
        boolean success = false;
        Enumeration enums = hierarchyNodes.keys();
        String resCode = null;
        AreaOfResearchTreeNodeBean modAORBean = null;
        
        //Traverse through the Hashtable obtained from the client End having
        //Modified/Add Node (AreaofResearch Data Bean) and construct the 
        //Parameter Object (db understandable form ). 
        while( enums.hasMoreElements() ){            
            paramHierarchy = new Vector();
            resCode = ( String ) enums.nextElement();             
            modAORBean = ( AreaOfResearchTreeNodeBean )
                                                  hierarchyNodes.get( resCode );            
            //one - one mapping to DB fields/Attributes
            paramHierarchy.addElement( new Parameter("AV_RESEARCH_AREA_CODE",
                                       DBEngineConstants.TYPE_STRING,
                                       modAORBean.getNodeID()) );            
            paramHierarchy.addElement( new Parameter(
                  "AV_PARENT_RESEARCH_AREA_CODE", DBEngineConstants.TYPE_STRING,
                  modAORBean.getParentNodeID()));            
            paramHierarchy.addElement( new Parameter("AV_HAS_CHILDREN_FLAG", 
                                       DBEngineConstants.TYPE_STRING, 
                                       modAORBean.hasChildren()? "Y": "N") );            
            paramHierarchy.addElement( new Parameter( "AV_DESCRIPTION", 
                                        DBEngineConstants.TYPE_STRING,
                                        modAORBean.getNodeDescription()));   
            paramHierarchy.addElement( new Parameter("AV_UPDATE_USER", 
                                       DBEngineConstants.TYPE_STRING, 
                                       userId ));            
            paramHierarchy.addElement( new Parameter("AV_UPDATE_TIMESTAMP",
                                            DBEngineConstants.TYPE_TIMESTAMP, 
                                            dbTimestamp));                        
            //for where caluse 
            paramHierarchy.addElement( new Parameter("AW_RESEARCH_AREA_CODE",
                                      DBEngineConstants.TYPE_STRING, resCode ));            
            paramHierarchy.addElement( new Parameter("AW_UPDATE_USER", 
                                       DBEngineConstants.TYPE_STRING, 
                                       modAORBean.getUpdateUser() ));            
            paramHierarchy.addElement( new Parameter("AW_UPDATE_TIMESTAMP",
                               DBEngineConstants.TYPE_TIMESTAMP, 
                               modAORBean.getUpdateTimestamp()));                                                                  
            paramHierarchy.addElement( new Parameter("AC_TYPE", 
                                            DBEngineConstants.TYPE_STRING,
                                            new Character(acType).toString() ));            
            
            //constructing the SQL Statement to be executed against DB.
            StringBuffer sqlCommandHierarchy = 
                        new StringBuffer("call UPDATE_AC_RESEARCH_AREA (");
            sqlCommandHierarchy.append(" <<AV_RESEARCH_AREA_CODE>> , ");
            sqlCommandHierarchy.append(" <<AV_PARENT_RESEARCH_AREA_CODE>> , ");
            sqlCommandHierarchy.append(" <<AV_HAS_CHILDREN_FLAG>> , ");
            sqlCommandHierarchy.append(" <<AV_DESCRIPTION>> , ");
            sqlCommandHierarchy.append(" <<AV_UPDATE_USER>> , ");
            sqlCommandHierarchy.append(" <<AV_UPDATE_TIMESTAMP>> , ");
            sqlCommandHierarchy.append(" <<AW_RESEARCH_AREA_CODE>> , ");  
            sqlCommandHierarchy.append(" <<AW_UPDATE_USER>> , ");
            sqlCommandHierarchy.append(" <<AW_UPDATE_TIMESTAMP>> , ");                     
            sqlCommandHierarchy.append(" <<AC_TYPE>> )");
            
            ProcReqParameter nodeInfo  = new ProcReqParameter();
            nodeInfo.setDSN("Coeus");
            nodeInfo.setParameterInfo( paramHierarchy );
            nodeInfo.setSqlCommand( sqlCommandHierarchy.toString() );
            procedures.addElement( nodeInfo );
        }
        
        // SETS the execute procedure flag. True if Successful: else False
        if( dbEngine!=null ){           
            dbEngine.executeStoreProcs( procedures );
            success = true;
        }else{
            throw new CoeusException("db_exceptionCode.1000");            
        }        
        return success;        
    }
    
    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
    /** The following method has been written to check whether a dependency exists in the table. 
     * @param AreaOfResearchTreeNodeBean instance 
     * @exception CoeusException if the instance of dbEngine is not available.
     * @exception DBException if any error during database transaction.
     * @return type is int (contains error code if dependency exists, else returns 1)
     */    
     public int checkCanDeleteResearchArea(AreaOfResearchTreeNodeBean areaOfResearchBean)
        throws CoeusException, DBException {
        int canDelete = 0;
        Vector param = new Vector();;
        Vector result = new Vector();
        param.add(new Parameter("AW_RESEARCH_AREA_CODE",
            DBEngineConstants.TYPE_STRING, areaOfResearchBean.getResearchAreaCode().trim()));
        if(dbEngine!=null){
        result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER ll_Ret>> = "
                +" call FN_CAN_DELETE_AC_RESEARCH_AREA(<< AW_RESEARCH_AREA_CODE >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }   
        if(!result.isEmpty()){
            HashMap hmRowCount = (HashMap)result.elementAt(0);
            canDelete = Integer.parseInt(hmRowCount.get("ll_Ret").toString());
        }
        return canDelete;                
    }
     
     //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
     /** The following method has been written to check whether selected Research_Area exists in table. 
     * @param String Research Area Code 
     * @exception CoeusException if the instance of dbEngine is not available.
     * @exception DBException if any error during database transaction.
     * @return type is int (returns 1 if research code exists, else 0)
     */    
     public int checkResearchAreaExists(String researchAreaCode)
        throws CoeusException, DBException {
        int isExists = 0;
        Vector param = new Vector();;
        Vector result = new Vector();
        param.add(new Parameter("AW_RESEARCH_AREA_CODE",
            DBEngineConstants.TYPE_STRING, researchAreaCode.trim()));
        if(dbEngine!=null){
        result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT INTEGER ll_Ret>> = "
                +" call FN_AC_RESEARCH_AREA_EXISTS(<< AW_RESEARCH_AREA_CODE >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }   
        if(!result.isEmpty()){
            HashMap hmRowCount = (HashMap)result.elementAt(0);
            isExists = Integer.parseInt(hmRowCount.get("ll_Ret").toString());
        }
        return isExists;                
    }
}