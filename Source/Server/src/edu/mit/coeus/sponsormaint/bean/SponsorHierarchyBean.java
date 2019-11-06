/*
 * sponsorHierarchyBean.java
 *
 * Created on November 17, 2004, 10:28 AM
 */

package edu.mit.coeus.sponsormaint.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import java.io.Serializable;
import edu.mit.coeus.exception.CoeusException;
import java.sql.Timestamp;
import java.util.Vector;

/**
 *
 * @author  shivakumarmj
 */
public class SponsorHierarchyBean implements CoeusBean, Serializable{
    
    private String hierarchyName;
    
    private String sponsorCode;
    
    private String sponsorName;
    
    private String levelOne;
    
    private String levelTwo;
    
    private String levelThree;
    
    private String levelFour;
    
    private String levelFive;
    
    private String levelSix;
    
    private String levelSeven;
    
    private String levelEight;
    
    private String levelNine;
    
    private String levelTen;
    
    private String acType = null;
    
    private java.sql.Timestamp updateTimestamp = null;
    
    private String updateUser = null;
    
    private int levelOneSortId;
    
    private int levelTwoSortId;
    
    private int levelThreeSortId;
    
    private int levelFourSortId;
    
    private int levelFiveSortId;
    
    private int levelSixSortId;
    
    private int levelSevenSortId;
    
    private int levelEightSortId;
    
    private int levelNineSortId;
    
    private int levelTenSortId;
    
    private int rowId;
    
    /** Creates a new instance of sponsorHierarchyBean */
    public SponsorHierarchyBean() {
    }   
    
    /** Getter for property hierarchyName.
     * @return Value of property hierarchyName.
     *
     */
    public java.lang.String getHierarchyName() {
        return hierarchyName;
    }    

    /** Setter for property hierarchyName.
     * @param hierarchyName New value of property hierarchyName.
     *
     */
    public void setHierarchyName(java.lang.String hierarchyName) {
        this.hierarchyName = hierarchyName;
    }    
    
    /** Getter for property sponsorCode.
     * @return Value of property sponsorCode.
     *
     */
    public java.lang.String getSponsorCode() {
        return sponsorCode;
    }
    
    /** Setter for property sponsorCode.
     * @param sponsorCode New value of property sponsorCode.
     *
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }   
    
    
    /** Getter for property acType.
     * @return Value of property acType.
     *
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     *
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     *
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     *
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     *
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     *
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }   
    
    
     public boolean equals(Object obj) {
        SponsorHierarchyBean sponsorHierarchyBean = (SponsorHierarchyBean)obj;
        if(sponsorHierarchyBean.getHierarchyName().equals(getHierarchyName()) && 
                sponsorHierarchyBean.getSponsorCode().equals(getSponsorCode())
                 && getRowId() == sponsorHierarchyBean.getRowId()) {
            return true;       
        }else {
            return false;
        }
    }
    
     /** Getter for property levelOne.
      * @return Value of property levelOne.
      *
      */
     public java.lang.String getLevelOne() {
         return levelOne;
     }
     
     /** Setter for property levelOne.
      * @param levelOne New value of property levelOne.
      *
      */
     public void setLevelOne(java.lang.String levelOne) {
         this.levelOne = levelOne;
     }
     
     /** Getter for property levelTwo.
      * @return Value of property levelTwo.
      *
      */
     public java.lang.String getLevelTwo() {
         return levelTwo;
     }
     
     /** Setter for property levelTwo.
      * @param levelTwo New value of property levelTwo.
      *
      */
     public void setLevelTwo(java.lang.String levelTwo) {
         this.levelTwo = levelTwo;
     }
     
     /** Getter for property levelThree.
      * @return Value of property levelThree.
      *
      */
     public java.lang.String getLevelThree() {
         return levelThree;
     }
     
     /** Setter for property levelThree.
      * @param levelThree New value of property levelThree.
      *
      */
     public void setLevelThree(java.lang.String levelThree) {
         this.levelThree = levelThree;
     }
     
     /** Getter for property levelFour.
      * @return Value of property levelFour.
      *
      */
     public java.lang.String getLevelFour() {
         return levelFour;
     }
     
     /** Setter for property levelFour.
      * @param levelFour New value of property levelFour.
      *
      */
     public void setLevelFour(java.lang.String levelFour) {
         this.levelFour = levelFour;
     }
     
     /** Getter for property levelFive.
      * @return Value of property levelFive.
      *
      */
     public java.lang.String getLevelFive() {
         return levelFive;
     }
     
     /** Setter for property levelFive.
      * @param levelFive New value of property levelFive.
      *
      */
     public void setLevelFive(java.lang.String levelFive) {
         this.levelFive = levelFive;
     }
     
     /** Getter for property levelSix.
      * @return Value of property levelSix.
      *
      */
     public java.lang.String getLevelSix() {
         return levelSix;
     }
     
     /** Setter for property levelSix.
      * @param levelSix New value of property levelSix.
      *
      */
     public void setLevelSix(java.lang.String levelSix) {
         this.levelSix = levelSix;
     }
     
     /** Getter for property levelSeven.
      * @return Value of property levelSeven.
      *
      */
     public java.lang.String getLevelSeven() {
         return levelSeven;
     }
     
     /** Setter for property levelSeven.
      * @param levelSeven New value of property levelSeven.
      *
      */
     public void setLevelSeven(java.lang.String levelSeven) {
         this.levelSeven = levelSeven;
     }
     
     /** Getter for property levelEight.
      * @return Value of property levelEight.
      *
      */
     public java.lang.String getLevelEight() {
         return levelEight;
     }
     
     /** Setter for property levelEight.
      * @param levelEight New value of property levelEight.
      *
      */
     public void setLevelEight(java.lang.String levelEight) {
         this.levelEight = levelEight;
     }
     
     /** Getter for property levelNine.
      * @return Value of property levelNine.
      *
      */
     public java.lang.String getLevelNine() {
         return levelNine;
     }
     
     /** Setter for property levelNine.
      * @param levelNine New value of property levelNine.
      *
      */
     public void setLevelNine(java.lang.String levelNine) {
         this.levelNine = levelNine;
     }
     
     /** Getter for property levelTen.
      * @return Value of property levelTen.
      *
      */
     public java.lang.String getLevelTen() {
         return levelTen;
     }
     
     /** Setter for property levelTen.
      * @param levelTen New value of property levelTen.
      *
      */
     public void setLevelTen(java.lang.String levelTen) {
         this.levelTen = levelTen;
     }
     
     /** Getter for property levelOneSortId.
      * @return Value of property levelOneSortId.
      *
      */
     public int getLevelOneSortId() {
         return levelOneSortId;
     }
     
     /** Setter for property levelOneSortId.
      * @param levelOneSortId New value of property levelOneSortId.
      *
      */
     public void setLevelOneSortId(int levelOneSortId) {
         this.levelOneSortId = levelOneSortId;
     }
     
     /** Getter for property levelTwoSortId.
      * @return Value of property levelTwoSortId.
      *
      */
     public int getLevelTwoSortId() {
         return levelTwoSortId;
     }
     
     /** Setter for property levelTwoSortId.
      * @param levelTwoSortId New value of property levelTwoSortId.
      *
      */
     public void setLevelTwoSortId(int levelTwoSortId) {
         this.levelTwoSortId = levelTwoSortId;
     }
     
     /** Getter for property levelThreeSortId.
      * @return Value of property levelThreeSortId.
      *
      */
     public int getLevelThreeSortId() {
         return levelThreeSortId;
     }
     
     /** Setter for property levelThreeSortId.
      * @param levelThreeSortId New value of property levelThreeSortId.
      *
      */
     public void setLevelThreeSortId(int levelThreeSortId) {
         this.levelThreeSortId = levelThreeSortId;
     }
     
     /** Getter for property levelFourSortId.
      * @return Value of property levelFourSortId.
      *
      */
     public int getLevelFourSortId() {
         return levelFourSortId;
     }
     
     /** Setter for property levelFourSortId.
      * @param levelFourSortId New value of property levelFourSortId.
      *
      */
     public void setLevelFourSortId(int levelFourSortId) {
         this.levelFourSortId = levelFourSortId;
     }
     
     /** Getter for property levelFiveSortId.
      * @return Value of property levelFiveSortId.
      *
      */
     public int getLevelFiveSortId() {
         return levelFiveSortId;
     }
     
     /** Setter for property levelFiveSortId.
      * @param levelFiveSortId New value of property levelFiveSortId.
      *
      */
     public void setLevelFiveSortId(int levelFiveSortId) {
         this.levelFiveSortId = levelFiveSortId;
     }
     
     /** Getter for property levelSixSortId.
      * @return Value of property levelSixSortId.
      *
      */
     public int getLevelSixSortId() {
         return levelSixSortId;
     }
     
     /** Setter for property levelSixSortId.
      * @param levelSixSortId New value of property levelSixSortId.
      *
      */
     public void setLevelSixSortId(int levelSixSortId) {
         this.levelSixSortId = levelSixSortId;
     }
     
     /** Getter for property levelSevenSortId.
      * @return Value of property levelSevenSortId.
      *
      */
     public int getLevelSevenSortId() {
         return levelSevenSortId;
     }
     
     /** Setter for property levelSevenSortId.
      * @param levelSevenSortId New value of property levelSevenSortId.
      *
      */
     public void setLevelSevenSortId(int levelSevenSortId) {
         this.levelSevenSortId = levelSevenSortId;
     }
     
     /** Getter for property levelEightSortId.
      * @return Value of property levelEightSortId.
      *
      */
     public int getLevelEightSortId() {
         return levelEightSortId;
     }
     
     /** Setter for property levelEightSortId.
      * @param levelEightSortId New value of property levelEightSortId.
      *
      */
     public void setLevelEightSortId(int levelEightSortId) {
         this.levelEightSortId = levelEightSortId;
     }
     
     /** Getter for property levelNineSortId.
      * @return Value of property levelNineSortId.
      *
      */
     public int getLevelNineSortId() {
         return levelNineSortId;
     }
     
     /** Setter for property levelNineSortId.
      * @param levelNineSortId New value of property levelNineSortId.
      *
      */
     public void setLevelNineSortId(int levelNineSortId) {
         this.levelNineSortId = levelNineSortId;
     }
     
     /** Getter for property levelTenSortId.
      * @return Value of property levelTenSortId.
      *
      */
     public int getLevelTenSortId() {
         return levelTenSortId;
     }
     
     /** Setter for property levelTenSortId.
      * @param levelTenSortId New value of property levelTenSortId.
      *
      */
     public void setLevelTenSortId(int levelTenSortId) {
         this.levelTenSortId = levelTenSortId;
     }
     
     /** Getter for property sponsorName.
      * @return Value of property sponsorName.
      *
      */
     public java.lang.String getSponsorName() {
         return sponsorName;
     }
     
     /** Setter for property sponsorName.
      * @param sponsorName New value of property sponsorName.
      *
      */
     public void setSponsorName(java.lang.String sponsorName) {
         this.sponsorName = sponsorName;
     }
     
     public boolean isLike(ComparableBean comparableBean) throws CoeusException {
         return true;
     }
     
     /** Getter for property rowId.
      * @return Value of property rowId.
      *
      */
     public int getRowId() {
         return rowId;
     }
     
     /** Setter for property rowId.
      * @param rowId New value of property rowId.
      *
      */
     public void setRowId(int rowId) {
         this.rowId = rowId;
     }
     
     
}
