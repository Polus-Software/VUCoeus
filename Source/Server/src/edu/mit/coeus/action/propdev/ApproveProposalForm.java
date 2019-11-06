/*
 * @(#)ApproveProposalForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.action.propdev;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.mit.coeus.utils.UtilFactory;

/**
 *
 */
public class ApproveProposalForm extends ActionForm {


    String proposalNumber;
    String comments;
    String actionMode;
    int mapId;
    int levelNumber;
    int stopNumber;
    Object updateTimeStamp;
    String updateUser;
    String userId;
    String principalInvestigator;
    String sponsorCode;
    String sponsorName;
    String title;

    /** Creates new ApproveProposalForm
     */
    public ApproveProposalForm() {
    }

    public String getPrincipalInvestigator()
    {   return principalInvestigator;
    }

    public void setPrincipalInvestigator(String principalInvestigator)
    {   this.principalInvestigator = principalInvestigator;
    }

    public String getSponsorCode()
    {   return this.sponsorCode;
    }

    public void setSponsorCode(String sponsorCode)
    {   this.sponsorCode = sponsorCode;
    }

    public String getSponsorName()
    {   return this.sponsorName;
    }

    public void setSponsorName(String sponsorName)
    {   this.sponsorName = sponsorName;
    }

    public String getTitle()
    {  System.out.println("in bean getTitle ---> " + this.title);
       return this.title;
    }

    public void setTitle(String title)
    {   this.title = title;
        System.out.println("in bean setTitle ---> " + title);
    }

    public int getMapId(){
        return this.mapId;
    }

    public void setMapId(int MapId){
        this.mapId = MapId;
    }

    public int getLevelNumber(){
        return this.levelNumber;
    }

    public void setLevelNumber(int LevelNumber){
        this.levelNumber = LevelNumber;
    }

    public Object getUpdateTimeStamp(){
        return this.updateTimeStamp;
    }

    public void setUpdateTimeStamp(Object UpdateTimeStamp){
        this.updateTimeStamp = UpdateTimeStamp;
    }

    public int getStopNumber(){
        return this.stopNumber;
    }

    public void setStopNumber(int StopNumber){
        this.stopNumber = StopNumber;
    }

    public String getProposalNumber(){
        return this.proposalNumber;
    }

    public void setProposalNumber(String proposalnumber ){
        this.proposalNumber = proposalnumber;
    }

    public String getComments(){
        return this.comments;
    }

    public void setComments(String Comments ){
        this.comments = Comments;
    }

    public String getActionMode(){
        return this.actionMode;
    }

    public void setActionMode(String ActionMode ){
        this.actionMode = ActionMode;
    }

    public String getUpdateUser(){
        return this.updateUser;
    }

    public void setUpdateUser(String UpdateUser ){
        this.updateUser = UpdateUser;
    }

    public String getUserId(){
        return this.userId;
    }

    public void setUserId(String UserId ){
        this.userId = UserId;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
      this.proposalNumber = null;
      this.comments = null;
      this.actionMode = null;
      this.mapId = 0;
      this.levelNumber = 0;
      this.stopNumber = 0;
      this.updateTimeStamp = null;
      this.updateUser = null;
      this.userId = null;
      this.sponsorCode= null;
      this.sponsorName=null;
      this.title=null;
      this.principalInvestigator=null;
    }

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        /*If the user is rejecting the proposal, a comment is required. */
        /* CASE #748 Comment Begin */
        /*if(actionMode.equalsIgnoreCase("Reject")){
          if (comments == null || comments.trim().length() < 1) {
            errors.add("comments", new ActionError("error.comment.required"));
          }
        }*/
        /* CASE #748 Comment End */
        /* CASE #748 Begin */
        if(actionMode != null && actionMode.equalsIgnoreCase("Reject")){
          if (comments == null || comments.trim().length() < 1) {
            errors.add("comments", new ActionError("error.comment.required"));
          }
        }
        /* cASE #748 End */
        return errors;

    }

}

