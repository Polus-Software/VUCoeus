/*
 * ProtocolDetailFlags.java
 *
 * Created on August 6, 2003, 3:56 PM
 */

package edu.mit.coeus.irb.bean;

/**
 *
 * @author  prasanna
 */
public class ProtocolDetailChangeFlags implements java.io.Serializable{
    
    //Flags for each Detail table to implement Sequence Number enhancement
    private boolean isLocationChanged = false;
    private boolean isInvestigatorChanged = false;
    private boolean isKeyStudyChanged = false;
    private boolean isCorrespondentsChanged = false;
    private boolean isAORChanged = false;
    private boolean isFundingSourceChanged = false;
    private boolean isSubjectsChanged = false;
    private boolean isSpecialRevChanged = false;
    private boolean isNotesChanged = false;
    private boolean isCustomDataChanged = false;
    private boolean isRolesChanged = false;
    private boolean isReferencesChanged = false;    
    private boolean isRelatedProjectsChanged = false;    
    
    //Flag for Protocol Maintenance
    private boolean isProtocolMaintChanged = false;
    
    /** Creates a new instance of ProtocolDetailFlags */
    public ProtocolDetailChangeFlags() {

    }    
    
    /** Getter for property isLocationChanged.
     * @return Value of property isLocationChanged.
     */
    public boolean getIsLocationChanged() {
        return isLocationChanged;
    }
    
    /** Setter for property isLocationChanged.
     * @param isLocationChanged New value of property isLocationChanged.
     */
    public void setIsLocationChanged(boolean isLocationChanged) {
        this.isLocationChanged = isLocationChanged;
    }
    
    /** Getter for property isInvestigatorChanged.
     * @return Value of property isInvestigatorChanged.
     */
    public boolean getIsInvestigatorChanged() {
        return isInvestigatorChanged;
    }
    
    /** Setter for property isInvestigatorChanged.
     * @param isInvestigatorChanged New value of property isInvestigatorChanged.
     */
    public void setIsInvestigatorChanged(boolean isInvestigatorChanged) {
        this.isInvestigatorChanged = isInvestigatorChanged;
    }
    
    /** Getter for property isKeyStudyChanged.
     * @return Value of property isKeyStudyChanged.
     */
    public boolean getIsKeyStudyChanged() {
        return isKeyStudyChanged;
    }
    
    /** Setter for property isKeyStudyChanged.
     * @param isKeyStudyChanged New value of property isKeyStudyChanged.
     */
    public void setIsKeyStudyChanged(boolean isKeyStudyChanged) {
        this.isKeyStudyChanged = isKeyStudyChanged;
    }
    
    /** Getter for property isCorrespondentsChanged.
     * @return Value of property isCorrespondentsChanged.
     */
    public boolean getIsCorrespondentsChanged() {
        return isCorrespondentsChanged;
    }
    
    /** Setter for property isCorrespondentsChanged.
     * @param isCorrespondentsChanged New value of property isCorrespondentsChanged.
     */
    public void setIsCorrespondentsChanged(boolean isCorrespondentsChanged) {
        this.isCorrespondentsChanged = isCorrespondentsChanged;
    }
    
    /** Getter for property isAORChanged.
     * @return Value of property isAORChanged.
     */
    public boolean getIsAORChanged() {
        return isAORChanged;
    }
    
    /** Setter for property isAORChanged.
     * @param isAORChanged New value of property isAORChanged.
     */
    public void setIsAORChanged(boolean isAORChanged) {
        this.isAORChanged = isAORChanged;
    }
    
    /** Getter for property isFundingSourceChanged.
     * @return Value of property isFundingSourceChanged.
     */
    public boolean getIsFundingSourceChanged() {
        return isFundingSourceChanged;
    }
    
    /** Setter for property isFundingSourceChanged.
     * @param isFundingSourceChanged New value of property isFundingSourceChanged.
     */
    public void setIsFundingSourceChanged(boolean isFundingSourceChanged) {
        this.isFundingSourceChanged = isFundingSourceChanged;
    }
    
    /** Getter for property isSubjectsChanged.
     * @return Value of property isSubjectsChanged.
     */
    public boolean getIsSubjectsChanged() {
        return isSubjectsChanged;
    }
    
    /** Setter for property isSubjectsChanged.
     * @param isSubjectsChanged New value of property isSubjectsChanged.
     */
    public void setIsSubjectsChanged(boolean isSubjectsChanged) {
        this.isSubjectsChanged = isSubjectsChanged;
    }
    
    /** Getter for property isSpecialRevChanged.
     * @return Value of property isSpecialRevChanged.
     */
    public boolean getIsSpecialRevChanged() {
        return isSpecialRevChanged;
    }
    
    /** Setter for property isSpecialRevChanged.
     * @param isSpecialRevChanged New value of property isSpecialRevChanged.
     */
    public void setIsSpecialRevChanged(boolean isSpecialRevChanged) {
        this.isSpecialRevChanged = isSpecialRevChanged;
    }
    
    /** Getter for property isNotesChanged.
     * @return Value of property isNotesChanged.
     */
    public boolean getIsNotesChanged() {
        return isNotesChanged;
    }
    
    /** Setter for property isNotesChanged.
     * @param isNotesChanged New value of property isNotesChanged.
     */
    public void setIsNotesChanged(boolean isNotesChanged) {
        this.isNotesChanged = isNotesChanged;
    }
    
    /** Getter for property isCustomDataChanged.
     * @return Value of property isCustomDataChanged.
     */
    public boolean getIsCustomDataChanged() {
        return isCustomDataChanged;
    }
    
    /** Setter for property isCustomDataChanged.
     * @param isCustomDataChanged New value of property isCustomDataChanged.
     */
    public void setIsCustomDataChanged(boolean isCustomDataChanged) {
        this.isCustomDataChanged = isCustomDataChanged;
    }
    
    /** Getter for property isRolesChanged.
     * @return Value of property isRolesChanged.
     */
    public boolean getIsRolesChanged() {
        return isRolesChanged;
    }
    
    /** Setter for property isRolesChanged.
     * @param isRolesChanged New value of property isRolesChanged.
     */
    public void setIsRolesChanged(boolean isRolesChanged) {
        this.isRolesChanged = isRolesChanged;
    }
    
    /** Getter for property isReferencesChanged.
     * @return Value of property isReferencesChanged.
     */
    public boolean getIsReferencesChanged() {
        return isReferencesChanged;
    }
    
    /** Setter for property isReferencesChanged.
     * @param isReferencesChanged New value of property isReferencesChanged.
     */
    public void setIsReferencesChanged(boolean isReferencesChanged) {
        this.isReferencesChanged = isReferencesChanged;
    }    
    
    /** Getter for property isProtocolMaintChanged.
     * @return Value of property isProtocolMaintChanged.
     */
    public boolean getIsProtocolMaintChanged() {
        return isProtocolMaintChanged;
    }
    
    /** Setter for property isProtocolMaintChanged.
     * @param isProtocolMaintChanged New value of property isProtocolMaintChanged.
     */
    public void setIsProtocolMaintChanged(boolean isProtocolMaintChanged) {
        this.isProtocolMaintChanged = isProtocolMaintChanged;
    }    
    
    /** Getter for property isRelatedProjectsChanged.
     * @return Value of property isRelatedProjectsChanged.
     */
    public boolean getIsRelatedProjectsChanged() {
        return isRelatedProjectsChanged;
    }
    
    /** Setter for property isRelatedProjectsChanged.
     * @param isRelatedProjectsChanged New value of property isRelatedProjectsChanged.
     */
    public void setIsRelatedProjectsChanged(boolean isRelatedProjectsChanged) {
        this.isRelatedProjectsChanged = isRelatedProjectsChanged;
    }
}
