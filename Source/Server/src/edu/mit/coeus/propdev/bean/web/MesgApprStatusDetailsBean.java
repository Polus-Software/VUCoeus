/*
 * @(#)MesgApprStatusDetailsBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean.web;

import edu.mit.coeus.utils.UtilFactory;
import java.util.Date;
import java.sql.Timestamp;

/**
 *
 */
public class MesgApprStatusDetailsBean {


    String mapsDescription;
    String proposalNumber;
    String approvalStatus;
    String approverImage;
    String statusImage;
    int mapId;
    int parentMapId;
    String userId;
    String userName;
    int stopNumber;
    int levelNumber;
    int indexLevel;
    int sequentialStopNumber;
    int bypassApproval;
    Timestamp updateTimeStamp;
    String updateUser;
    int rowNumber;
    char statusOfApproval;


    /** Creates new MesgApprMapsDetailsBean
     */
    public MesgApprStatusDetailsBean() {
    }

    /**
     * gets the Proposal Approval Maps Description
     * @return description
     */
    public String getDescription(){
        //System.out.println("in bean detail <<status>> get * " + this.mapsDescription);
        return this.mapsDescription;
    }

    public String getApprovalStatus(){
        return this.approvalStatus;
    }

    public void setApprovalStatus(String ApprovalStatus){
        this.approvalStatus = ApprovalStatus;
    }

    public int getParentMapId(){
        return this.parentMapId;
    }

    public void setParentMapId(int ParentMapId){
        this.parentMapId = ParentMapId;
    }

    public char getStatusOfApproval(){
        return this.statusOfApproval;
    }

    public void setStatusOfApproval(char StatusOfApproval){
        this.statusOfApproval = StatusOfApproval;
    }

    public int getRowNumber(){
        return this.rowNumber;
    }

    public void setRowNumber(int RowNumber){
        this.rowNumber = RowNumber;
    }

    public int getBypassApproval(){
        return this.bypassApproval;
    }

    public void setBypassApproval(int BypassApproval){
        this.bypassApproval = BypassApproval;
    }

    public int getMapId(){
        return this.mapId;
    }

    public void setMapId(int MapId){
        this.mapId = MapId;
    }

    public String getUserId(){
        return this.userId;
    }

    public void setUserId(String UserId){
        this.userId = UserId;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setUserName(String UserName){
        this.userName = UserName;
    }

    public int getStopNumber(){
        return this.stopNumber;
    }

    public void setStopNumber(int StopNumber){
        this.stopNumber = StopNumber;
    }

    public int getSequentialStopNumber(){
        //System.out.println("in bean detail get S-S-N === " + this.sequentialStopNumber);
        return this.sequentialStopNumber;
    }

    public void setSequentialStopNumber(int SequentialStopNumber){
        //System.out.println("in bean detail set S-S-N === " + SequentialStopNumber);
        this.sequentialStopNumber = SequentialStopNumber;
    }

    public int getLevelNumber(){
        return this.levelNumber;
    }

    public void setLevelNumber(int LevelNumber){
        this.levelNumber = LevelNumber;
    }

    public int getIndexLevel(){
        return this.indexLevel;
    }

    public void setIndexLevel(int IndexLevel){
        this.indexLevel = IndexLevel;
    }

    /**
     * gets the Proposal Approval Maps proposal number
     * @return proposal#
     */
    public String getProposalNumber(){
        return this.proposalNumber;
    }

    public void setApproverImage(String ApproverImage){
        this.approverImage = ApproverImage;
    }

    public String getApproverImage(){
        return this.approverImage;
    }

    public void setStatusImage(String StatusImage){
        this.statusImage = StatusImage;
    }

    public String getStatusImage(){
        //System.out.println("in bean detail get BMP * " + this.statusImage);
        return this.statusImage;
    }

    /**
     * sets the Proposal Approval Maps Description
     *
     */
    public void setDescription(String description){
        //System.out.println("in bean detail set * " + description);
        this.mapsDescription = description;
    }

    /**
     * sets the Proposal Approval Maps proposal number
     *
     */
    public void setProposalNumber(String proposalnumber ){
        this.proposalNumber = proposalnumber;
    }

    public String getUpdateUser(){
        return this.updateUser;
    }

    public void setUpdateUser(String UpdateUser ){
        this.updateUser = UpdateUser;
    }

    public Timestamp getUpdateTimeStamp(){
        return this.updateTimeStamp;
    }

    public void setUpdateTimeStamp(Timestamp UpdateTimeStamp){
        this.updateTimeStamp = UpdateTimeStamp;
    }

}

