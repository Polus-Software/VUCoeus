/*
 * SubcontactingBudgetBean.java
 *
 * Created on January 4, 2005, 3:58 PM
 */

package edu.mit.coeus.award.bean;

/**
 *
 * @author  nadhgj
 */
public class SubcontactingBudgetBean extends AwardBaseBean {
    
    private double largeBusinessGoal;
    
    private double smallBusinessGoal;
    
    private double womanOwnedGoal;
    
    private double sDBGoal;
    
    private double hubZoneGoal;
    
    private double veterenOwnedGoal;
    
    private double hBCUGoal;
    
    private double sDVeterenOwnedGoal;
    
    private String comments;

    //coeusdev-1081 start
    private double ancitNoCbsbaGoal;
    private double ancitNoSbGoal;
    //coeusdev-1081 end
    
    /** Creates a new instance of SubcontactingBudgetBean */
    public SubcontactingBudgetBean() {
    }
    
    /** Getter for property largeBusinessGoal.
     * @return Value of property largeBusinessGoal.
     *
     */
    public double getLargeBusinessGoal() {
        return largeBusinessGoal;
    }
    
    /** Setter for property largeBusinessGoal.
     * @param largeBusinessGoal New value of property largeBusinessGoal.
     *
     */
    public void setLargeBusinessGoal(double largeBusinessGoal) {
        this.largeBusinessGoal = largeBusinessGoal;
    }
    
    /** Getter for property smallBusinessGoal.
     * @return Value of property smallBusinessGoal.
     *
     */
    public double getSmallBusinessGoal() {
        return smallBusinessGoal;
    }
    
    /** Setter for property smallBusinessGoal.
     * @param smallBusinessGoal New value of property smallBusinessGoal.
     *
     */
    public void setSmallBusinessGoal(double smallBusinessGoal) {
        this.smallBusinessGoal = smallBusinessGoal;
    }
    
    /** Getter for property womanOwnedGoal.
     * @return Value of property womanOwnedGoal.
     *
     */
    public double getWomanOwnedGoal() {
        return womanOwnedGoal;
    }
    
    /** Setter for property womanOwnedGoal.
     * @param womanOwnedGoal New value of property womanOwnedGoal.
     *
     */
    public void setWomanOwnedGoal(double womanOwnedGoal) {
        this.womanOwnedGoal = womanOwnedGoal;
    }
    
    /** Getter for property sDBGoal.
     * @return Value of property sDBGoal.
     *
     */
    public double getSDBGoal() {
        return sDBGoal;
    }
    
    /** Setter for property sDBGoal.
     * @param sDBGoal New value of property sDBGoal.
     *
     */
    public void setSDBGoal(double sDBGoal) {
        this.sDBGoal = sDBGoal;
    }
    
    /** Getter for property hubZoneGoal.
     * @return Value of property hubZoneGoal.
     *
     */
    public double getHubZoneGoal() {
        return hubZoneGoal;
    }
    
    /** Setter for property hubZoneGoal.
     * @param hubZoneGoal New value of property hubZoneGoal.
     *
     */
    public void setHubZoneGoal(double hubZoneGoal) {
        this.hubZoneGoal = hubZoneGoal;
    }
    
    /** Getter for property veterenOwnedGoal.
     * @return Value of property veterenOwnedGoal.
     *
     */
    public double getVeterenOwnedGoal() {
        return veterenOwnedGoal;
    }
    
    /** Setter for property veterenOwnedGoal.
     * @param veterenOwnedGoal New value of property veterenOwnedGoal.
     *
     */
    public void setVeterenOwnedGoal(double veterenOwnedGoal) {
        this.veterenOwnedGoal = veterenOwnedGoal;
    }
    
    /** Getter for property hBCUGoal.
     * @return Value of property hBCUGoal.
     *
     */
    public double getHBCUGoal() {
        return hBCUGoal;
    }
    
    /** Setter for property hBCUGoal.
     * @param hBCUGoal New value of property hBCUGoal.
     *
     */
    public void setHBCUGoal(double hBCUGoal) {
        this.hBCUGoal = hBCUGoal;
    }
    
    /** Getter for property sDVeterenOwnedGoal.
     * @return Value of property sDVeterenOwnedGoal.
     *
     */
    public double getSDVetOwnedGoal() {
        return sDVeterenOwnedGoal;
    }
    
    /** Setter for property sDVeterenOwnedGoal.
     * @param sDVeterenOwnedGoal New value of property sDVeterenOwnedGoal.
     *
     */
    public void setSDVetOwnedGoal(double sDVeterenOwnedGoal) {
        this.sDVeterenOwnedGoal = sDVeterenOwnedGoal;
    }
    
    
    /** Getter for property comments.
     * @return Value of property comments.
     *
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     *
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }

    //coeusdev-1081 start
    public double getAncitNoCbsbaGoal() {
        return ancitNoCbsbaGoal;
    }

    public void setAncitNoCbsbaGoal(double ancitNoCbsbaGoal) {
        this.ancitNoCbsbaGoal = ancitNoCbsbaGoal;
    }

    public double getAncitNoSbGoal() {
        return ancitNoSbGoal;
    }

    public void setAncitNoSbGoal(double ancitNoSbGoal) {
        this.ancitNoSbGoal = ancitNoSbGoal;
    }

    //coeusdev-1081 end
    
}
