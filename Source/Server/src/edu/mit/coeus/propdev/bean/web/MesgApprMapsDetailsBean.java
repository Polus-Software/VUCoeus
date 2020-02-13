/*
 * @(#)MesgApprMapsDetailsBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean.web;

import edu.mit.coeus.utils.UtilFactory;

/**
 *
 */
public class MesgApprMapsDetailsBean {


    String mapsDescription;
    String proposalNumber;
    int mapId;
    int levelNumber;
    int parentMapId;
    String bitMapName;
    char approvalStatus;

    /** Creates new MesgApprMapsDetailsBean
     */
    public MesgApprMapsDetailsBean() {
    }

    public int getMapId(){
        //System.out.println("in bean detail get mapd id * " + this.mapId);
        return this.mapId;
    }

    public void setMapId(int MapId){
        //System.out.println("in bean detail set mapd id * " + MapId);
        this.mapId = MapId;
    }

    public int getLevelNumber(){
        return this.levelNumber;
    }

    public void setLevelNumber(int LevelNumber){
        this.levelNumber = LevelNumber;
    }

    public int getParentMapId(){
        return this.parentMapId;
    }

    public void setApprovalStatus(char ApprovalStatus){
        this.approvalStatus = ApprovalStatus;
    }

    public char getApprovalStatus(){
        return this.approvalStatus;
    }

    public void setParentMapId(int ParentMapId){
        this.parentMapId = ParentMapId;
    }

    /**
     * gets the Proposal Approval Maps Description
     * @return description
     */
    public String getDescription(){
        //System.out.println("in bean detail get * " + this.mapsDescription);
        return this.mapsDescription;
    }

    /**
     * gets the Proposal Approval Maps proposal number
     * @return proposal#
     */
    public String getProposalNumber(){
        return this.proposalNumber;
    }

    /**
     * sets the Proposal Approval Maps Description
     *
     */
    public void setDescription(String description){
        //System.out.println("in bean detail set * " + description);
        this.mapsDescription = description;
    }

    public void setBitMap(String BitMapName){
        //System.out.println("in bean detail set BMP * " + bitMapName);
        this.bitMapName = BitMapName;
    }

    public String getBitMap(){
        //System.out.println("in bean detail get BMP * " + this.bitMapName);
        return this.bitMapName;
    }

    /**
     * sets the Proposal Approval Maps proposal number
     *
     */
    public void setProposalNumber(String proposalnumber ){
        this.proposalNumber = proposalnumber;
    }
}

