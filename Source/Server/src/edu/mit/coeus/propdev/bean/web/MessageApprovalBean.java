package edu.mit.coeus.propdev.bean.web;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Hashtable;
/* case #748 begin */
import java.util.HashMap;
/* case #748 end */
import java.util.Vector;
import java.util.Date;
import java.sql.Timestamp;


import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;

/* case #748 comment begin */
/*import edu.mit.coeus.bean.MesgApprMapsDetailsBean;
import edu.mit.coeus.bean.MesgApprStatusDetailsBean;
import edu.mit.coeus.bean.ApprovalRightsBean;*/
/* case #748 comment end */

/**
 * Component for accessing and updating a given user's messages.
 */
public class MessageApprovalBean implements Serializable
{   /**
     * Singleton instance of DBEngineImpl.
     */
    private DBEngineImpl dbEngine;


    /* case #748 comment begin */
    /*public MessageApprovalBean() throws DBException, Exception
    {   dbEngine = new DBEngineImpl();
    }*/
    /* case #748 comment end */

    /* case #748 begin */
    /**
     * No argument constructor.
     */
    public MessageApprovalBean()
    {
        dbEngine = new DBEngineImpl();
    }
    /* case #748 end */

    /**
     * Call stored procedure which accesses OSP$INBOX and OSP$MESSAGE tables.
     * For each HashMap element of the Vector that is returned, if openedFlag
     * is true, construct a MessageBean.  Put MessageBean objects into resolvedMessages
     * Vector.
     * @param userId
     * @return resolvedMessages Vector
     */
    /* case #748 Update method to throw only DBException */
    public Vector getApprovalMaps(String proposalNumber) throws DBException
    {
        //DBEngineImpl.executeRequest takes as parameters String ServiceName, String SQLCommand,
        //String DataSource, Vector paramList.


        //System.out.println("Inside getApprovalMaps >>>>");
        Vector approvalMaps = new Vector();
        Vector results = new Vector();
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        if(dbEngine!=null)
        {   results = dbEngine.executeRequest
            ("Coeus", "call GET_PROPAPPROVALMAPS( <<PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> ) ",
            "Coeus", param);
        }
        else
        {   throw new DBException("error.database.instance.null");
        }

        for(int k=0;k<results.size();k++){
            /* case #748 comment begin */
            //Hashtable approvalMapsRow = (Hashtable)results.elementAt(k);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap approvalMapsRow = (HashMap)results.elementAt(k);
            /* case #748 end */
            MesgApprMapsDetailsBean mapDetails = new MesgApprMapsDetailsBean();
            mapDetails.setDescription(approvalMapsRow.get("DESCRIPTION").toString());
            mapDetails.setProposalNumber(approvalMapsRow.get("PROPOSAL_NUMBER").toString());
            mapDetails.setMapId(Integer.parseInt(approvalMapsRow.get("MAP_ID").toString()));
            mapDetails.setParentMapId(Integer.parseInt(approvalMapsRow.get("PARENT_MAP_ID").toString()));
            mapDetails.setLevelNumber(Integer.parseInt(approvalMapsRow.get("LEVEL").toString()));
            char approvalStatus = approvalMapsRow.get("APPROVAL_STATUS").toString().charAt(0);
            String imageName = null;
            switch(approvalStatus) {
              case 'A':
                imageName = "approved.gif";
                break;
              case 'R':
                imageName = "reject.gif";
                break;
              case 'T':
                imageName = "tobesub.gif";
                break;
              case 'P':
                imageName = "inprogress.gif";
                break;
            }
            mapDetails.setBitMap(imageName);
            mapDetails.setApprovalStatus(approvalStatus);
            approvalMaps.add(mapDetails);
        }
        //System.out.println("End of getApprovalMaps >>>>");

        return approvalMaps;
    }

    /* case #748 Method updated to throw only DBException*/
    public int getMapId(String proposalNumber, String userID, Vector resultMaps)
                                              throws DBException
    {

        /*
          Get map id for status = 'Waiting for approval' based on user id
        */
        //System.out.println("Inside getMapId >>>>");
        Vector results = new Vector();
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("USER_ID", "String", userID));
        if(dbEngine!=null)
        {   results = dbEngine.executeRequest
            ("Coeus", "call GET_WAIT_APPRVL_MAPID( <<PROPOSAL_NUMBER>> , <<USER_ID>> , <<OUT RESULTSET rset>> ) ",
            "Coeus", param);
        }
        else
        {   throw new DBException("error.database.instance.null");
        }

        //System.out.println("db execute complete -------->");

        int levelNumber = 0;
        int mapID = -1;
        int recLevelNumber = 0;
        int topLevelMapId = -1;
        int aprvStatusMapId = -1;
        for(int k=0;k<results.size();k++){
            /* case #748 comment begin */
            //Hashtable approvalMapsRow = (Hashtable)results.elementAt(k);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap approvalMapsRow = (HashMap)results.elementAt(k);
            /* case #748 end */
            mapID = Integer.parseInt(approvalMapsRow.get("MAP_ID").toString());
            for(int j=0;j<resultMaps.size();j++){
                MesgApprMapsDetailsBean mesgMapsDetailsBean = (MesgApprMapsDetailsBean)resultMaps.elementAt(j);
                int resultMapId = mesgMapsDetailsBean.getMapId();
                if(resultMapId == mapID) {
                  recLevelNumber = mesgMapsDetailsBean.getLevelNumber();
                }
                if(recLevelNumber > levelNumber) {
                  mapID = resultMapId;
                  levelNumber = recLevelNumber;
                }
            }
        }
        if(mapID < 0 ){
          for(int j=0;j<resultMaps.size();j++){
                MesgApprMapsDetailsBean mesgMapsDetailsBean = (MesgApprMapsDetailsBean)resultMaps.elementAt(j);
                int resultMapId = mesgMapsDetailsBean.getMapId();
                char approvalStatus = mesgMapsDetailsBean.getApprovalStatus();
                if(aprvStatusMapId < 0 && approvalStatus == 'P') {
                  aprvStatusMapId = resultMapId;
                }
                if(j==0) {
                  topLevelMapId = resultMapId;
                }

                if(resultMapId == mapID) {
                  recLevelNumber = mesgMapsDetailsBean.getLevelNumber();
                }
                if(recLevelNumber > levelNumber) {
                  mapID = resultMapId;
                  levelNumber = recLevelNumber;
                }
          }

          if(aprvStatusMapId > 0) {
            mapID = aprvStatusMapId;
          }else {
            mapID = topLevelMapId;
          }
        }

        //System.out.println("End of getMapId >>>> " + mapID);

        return mapID;
    }

    /* case #748 Method updated to throw only DBException */
    public Vector getApprovalStatus(String proposalNumber, String mapID, String userID)
                                          throws DBException
    {
        //DBEngineImpl.executeRequest takes as parameters String ServiceName, String SQLCommand,
        //String DataSource, Vector paramList.

        //System.out.println("Inside getApprovalStatus >>>>");
        Vector approvalStatus = new Vector();
        Vector results = new Vector();
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("MAP_ID", "String", mapID));
        param.addElement(new Parameter("USER_ID", "String", userID));
        if(dbEngine!=null)
        {   results = dbEngine.executeRequest
            ("Coeus", "call GET_PROPAPPROVAL_STATUS( <<PROPOSAL_NUMBER>> , <<MAP_ID>> , <<USER_ID>> , <<OUT RESULTSET rset>> ) ",
            "Coeus", param);
        }
        else
        {   throw new DBException("error.database.instance.null");
        }

        int prevLevelNumber = 0;
        for(int k=0;k<results.size();k++){
            /* case #748 comment begin */
            //Hashtable approvalStatusRow = (Hashtable)results.elementAt(k);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap approvalStatusRow = (HashMap)results.elementAt(k);
            /* case #748 end */
            MesgApprStatusDetailsBean statusDetails = new MesgApprStatusDetailsBean();
            statusDetails.setDescription(approvalStatusRow.get("MAPS_DESCRIPTION").toString());
            statusDetails.setProposalNumber(approvalStatusRow.get("PROPOSAL_NUMBER").toString());
            statusDetails.setMapId(Integer.parseInt(approvalStatusRow.get("MAP_ID").toString()));
            statusDetails.setParentMapId(Integer.parseInt(approvalStatusRow.get("PARENT_MAP_ID").toString()));
            /* case #748 comment begin */
            //statusDetails.setUserName(approvalStatusRow.get("USER_NAME").toString());
            /* case #748 comment end */
            /* case #748 begin */
            statusDetails.setUserName((String)approvalStatusRow.get("USER_NAME"));
            /* case #748 end */
            statusDetails.setApprovalStatus(approvalStatusRow.get("APPROVAL_STATUS_TEXT").toString());
            statusDetails.setUserId(approvalStatusRow.get("USER_ID").toString());

            statusDetails.setRowNumber(k);

            statusDetails.setUpdateTimeStamp((Timestamp)approvalStatusRow.get("UPDATE_TIMESTAMP"));
            statusDetails.setUpdateUser(approvalStatusRow.get("UPDATE_USER").toString());

            int levelNumber = Integer.parseInt(approvalStatusRow.get("LEVEL_NUMBER").toString());
            int stopNumber = Integer.parseInt(approvalStatusRow.get("STOP_NUMBER").toString());
            /* case #748 comment begin */
            //String primaryApprover = approvalStatusRow.get("PRIMARY_APPROVER_FLAG").toString();
            /* case #748 comment end */
            /* case #748 begin */
            String primaryApprover = (String)approvalStatusRow.get("PRIMARY_APPROVER_FLAG");
            /* case #748 end */
            int indexLevel = 0;
            if(prevLevelNumber != levelNumber) {
              //System.out.println("inside IF block (level) --> " + levelNumber + " previous = " + prevLevelNumber);
              statusDetails.setSequentialStopNumber(levelNumber);
              prevLevelNumber = levelNumber;
            }

            if(primaryApprover.equalsIgnoreCase("Y")) {
              indexLevel = 1;
            }else {
              indexLevel = 2;
            }

            statusDetails.setIndexLevel(indexLevel);
            statusDetails.setLevelNumber(levelNumber);
            statusDetails.setStopNumber(stopNumber);
            statusDetails.setBypassApproval(Integer.parseInt(approvalStatusRow.get("BYPASS_RIGHT").toString()));

            char approverType = primaryApprover.charAt(0);
            char statusOfApproval = approvalStatusRow.get("APPROVAL_STATUS").toString().charAt(0);
            String statusImageName = null;
            String approverImageName = null;
            //check for approver - "Primary" or "Alternate"
            switch(approverType) {
              case 'Y': //primary approver
                approverImageName = "primary.gif";
                break;
              case 'N': //alternate approver
                approverImageName = "alternate.gif";
                break;
            }

            switch(statusOfApproval) {
              case 'B':
                statusImageName = "bypass.gif";
                break;
              case 'O':
                statusImageName = "altappr.gif";
                break;
              case 'T':
                statusImageName = "tobesub.gif";
                break;
              case 'W':
                statusImageName = "waiting.gif";
                break;
              case 'A':
                statusImageName = "approved.gif";
                break;
              case 'R':
                statusImageName = "reject.gif";
                break;
              case 'J':
                statusImageName = "reject.gif";
                break;
              case 'P':
                statusImageName = "pass.gif";
                break;
              case 'L':
                statusImageName = "pass.gif";
                break;
              case 'D':
                statusImageName = "delegate.gif";
                break;
            }
            statusDetails.setStatusOfApproval(statusOfApproval);
            statusDetails.setApproverImage(approverImageName);
            statusDetails.setStatusImage(statusImageName);
            approvalStatus.add(statusDetails);
        }

        //System.out.println("End of getApprovalStatus >>>>");

        return approvalStatus;
    }

    /* case #748 Method updated to throw only DBException */
    public Vector getApprovalRights(String proposalNumber, String userID)
                                          throws DBException
    {
        //DBEngineImpl.executeRequest takes as parameters String ServiceName, String SQLCommand,
        //String DataSource, Vector paramList.

        System.out.println("Inside getApprovalRights >>>>");
        Vector approvalRights = new Vector();
        Vector results = new Vector();
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("USER_ID", "String", userID));
        if(dbEngine!=null)
        {   results = dbEngine.executeRequest
            ("Coeus", "call GET_PROPAPPROVAL_RIGHTS( <<PROPOSAL_NUMBER>> , <<USER_ID>> , <<OUT INTEGER VIEWROUTING>> , <<OUT INTEGER PROPWAIT>> ) ",
            "Coeus", param);
        }
        else
        {   throw new DBException("error.database.instance.null");
        }

        for(int k=0;k<results.size();k++){
            /* case #748 comment begin */
            //Hashtable approvalRightsRow = (Hashtable)results.elementAt(k);
            /* case #748 comment end */
            /* case #748 begin */
            HashMap approvalRightsRow = (HashMap)results.elementAt(k);
            /* case #748 end */
            ApprovalRightsBean approvalRightsBean = new ApprovalRightsBean();
            approvalRightsBean.setViewRoutingRight(Integer.parseInt(approvalRightsRow.get("VIEWROUTING").toString()));
            approvalRightsBean.setApproveRejectRight(Integer.parseInt(approvalRightsRow.get("PROPWAIT").toString()));
            approvalRightsBean.setProposalNumber(proposalNumber);
            approvalRights.addElement(approvalRightsBean);
        }

        System.out.println("End of getApprovalRights >>>>");

        return approvalRights;
    }
    /* ele adding user id as parameter*/
    public MesgApprStatusDetailsBean getProposalBean(Vector approvalStatusRecs, String userId) throws SQLException, DBException, Exception
    {
        //System.out.println("Start getProposalBean >>>>");
        MesgApprStatusDetailsBean mesgStatusDetailsBean = null;
        for(int k=0;k<approvalStatusRecs.size();k++){
            mesgStatusDetailsBean = (MesgApprStatusDetailsBean)approvalStatusRecs.elementAt(k);
            if((mesgStatusDetailsBean.getStatusOfApproval() == 'W') &&
             (mesgStatusDetailsBean.getUserId().equalsIgnoreCase(userId)))
            {
              break;
            }
        }

        //System.out.println("End of getProposalBean >>>>");

        return mesgStatusDetailsBean;
    }

}
