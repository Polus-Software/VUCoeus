/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

/**
 *
 * @author 
 */
public class CoiMenuBean {

//private String menuName;
private boolean questDataSaved;
private boolean finEntDataSaved;
private boolean prjDataSaved;
private boolean noteDataSaved;
private boolean attachDataSaved;
private boolean certDataSaved;
private boolean assignViewerDataSaved;
private boolean reviewCompleteSaved;
private boolean travelDataSaved;

    public boolean isTravelDataSaved() {
        return travelDataSaved;
    }

    public void setTravelDataSaved(boolean travelDataSaved) {
        this.travelDataSaved = travelDataSaved;
    }

    public boolean isReviewCompleteSaved() {
        return reviewCompleteSaved;
    }

    public void setReviewCompleteSaved(boolean reviewCompleteSaved) {
        this.reviewCompleteSaved = reviewCompleteSaved;
    }
    public boolean isAssignViewerDataSaved() {
        return assignViewerDataSaved;
    }

    public void setAssignViewerDataSaved(boolean assignViewerDataSaved) {
        this.assignViewerDataSaved = assignViewerDataSaved;
    }
    public boolean isAttachDataSaved() {
        return attachDataSaved;
    }

    public void setAttachDataSaved(boolean attachDataSaved) {
        this.attachDataSaved = attachDataSaved;
    }

    public boolean isCertDataSaved() {
        return certDataSaved;
    }

    public void setCertDataSaved(boolean certDataSaved) {
        this.certDataSaved = certDataSaved;
    }

    public boolean isFinEntDataSaved() {
        return finEntDataSaved;
    }

    public void setFinEntDataSaved(boolean finEntDataSaved) {
        this.finEntDataSaved = finEntDataSaved;
    }

    public boolean isNoteDataSaved() {
        return noteDataSaved;
    }

    public void setNoteDataSaved(boolean noteDataSaved) {
        this.noteDataSaved = noteDataSaved;
    }

    public boolean isPrjDataSaved() {
        return prjDataSaved;
    }

    public void setPrjDataSaved(boolean prjDataSaved) {
        this.prjDataSaved = prjDataSaved;
    }

    public boolean isQuestDataSaved() {
        return questDataSaved;
    }

    public void setQuestDataSaved(boolean questDataSaved) {
        this.questDataSaved = questDataSaved;
    }

}
