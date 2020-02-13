/*
 * ProtocolAlternativeSearchBean.java
 *
 * Created on March 20, 2010, 11:43 AM
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.IBaseDataBean;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;
/**
 *
 * @author sreenathv
 */
public class ProtocolAlternativeSearchBean  implements java.io.Serializable, IBaseDataBean{
    
    private  String protocolNumber;
    private int sequenceNumber;
    private String acType;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    
    
    private int alternativeSearchId;
    private int awAlternativeSearchId;
    private java.sql.Date searchDate;
    private String databaseSeartched;
    private int databaseSearchedCode;    
    private String yearsSearched;
    private String keyWordsSearched;
    private String comments; 
    
    private PropertyChangeSupport propertySupport;
    private static final String SEARCH_DATE = "searchDate";
    private static final String DTATBASE_SEARCHED = "databaseSeartched";
    private static final String DTATBASE_SEARCHED_CODE = "databaseSearchedCode";
    private static final String YEAR_SEARCHED = "yearsSearched";
    private static final String KEYWORD_SEARCHED = "keyWordsSearched";
    private static final String COMMENTS = "comments";
    //Added for COEUSQA-2714 In the Alternative Search in IACUC-Start
    private Vector chkDatabaseSearchedCode;
    private Vector chkDatabaseOldSearchedCode;   
    //Added for COEUSQA-2714 In the Alternative Search in IACUC-End
    /** Creates a new instance of ProtocolAlternativeSearchBean */
    public ProtocolAlternativeSearchBean() {
         propertySupport = new PropertyChangeSupport(this);
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public java.sql.Date getSearchDate() {
        return searchDate;
    }  
    public void setSearchDate(java.sql.Date newsearchDate) {
         java.sql.Date oldSeaschDate = searchDate;
        this.searchDate = newsearchDate;
        propertySupport.firePropertyChange(SEARCH_DATE,oldSeaschDate, searchDate);
    }

    public String getDatabaseSeartched() {
        return databaseSeartched;
    }

    public void setDatabaseSeartched(String newDatabaseSeartched) {
        String oldDatabaseSearched = databaseSeartched;
        this.databaseSeartched = newDatabaseSeartched;
        propertySupport.firePropertyChange(DTATBASE_SEARCHED,oldDatabaseSearched, databaseSeartched);
    }

    public int getDatabaseSearchedCode() {
        return databaseSearchedCode;
    }

    public void setDatabaseSearchedCode(int newDatabaseSearchedCode) {
        int oldDatabaseSearchedCode = databaseSearchedCode;
        this.databaseSearchedCode = newDatabaseSearchedCode;
        propertySupport.firePropertyChange(DTATBASE_SEARCHED_CODE,oldDatabaseSearchedCode, databaseSearchedCode);
    }

    public String getYearsSearched() {
        return yearsSearched;
    }

    public void setYearsSearched(String newYearsSearched) {
        String oldYearsSearched = yearsSearched;
        this.yearsSearched = newYearsSearched;
        propertySupport.firePropertyChange(YEAR_SEARCHED,oldYearsSearched, yearsSearched);
    }

    public String getKeyWordsSearched() {
        return keyWordsSearched;
    }

    public void setKeyWordsSearched(String newKeyWordsSearched) {
        String oldKeyWordsSearched = keyWordsSearched;
        this.keyWordsSearched = newKeyWordsSearched;
        propertySupport.firePropertyChange(KEYWORD_SEARCHED,oldKeyWordsSearched,keyWordsSearched);
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String newComments) {
        String oldComments = comments;
        this.comments = newComments;
        propertySupport.firePropertyChange(COMMENTS,oldComments,comments);
    }

    public int getAlternativeSearchId() {
        return alternativeSearchId;
    }

    public void setAlternativeSearchId(int alternativeSearchId) {
        this.alternativeSearchId = alternativeSearchId;
    }

    public int getAwAlternativeSearchId() {
        return awAlternativeSearchId;
    }

    public void setAwAlternativeSearchId(int awAlternativeSearchId) {
        this.awAlternativeSearchId = awAlternativeSearchId;
    }
   
    /**
    * Method used to add propertychange listener to the fields
    * @param listener PropertyChangeListener
    */
   public void addPropertyChangeListener(PropertyChangeListener listener) {
       propertySupport.addPropertyChangeListener(listener);
   }
   
   /**
    * Method used to remove propertychange listener
    * @param listener PropertyChangeListener
    */
   public void removePropertyChangeListener(PropertyChangeListener listener) {
       propertySupport.removePropertyChangeListener(listener);
   }

   //Added for COEUSQA-2714 In the Alternative Search in IACUC-start
    /**
     * Method used to get chkDatabaseSearchedCode
     * @return chkDatabaseSearchedCode
     */
    public Vector getChkDatabaseSearchedCode() {
        return chkDatabaseSearchedCode;
    }

    /**
     * Method used to set chkDatabaseSearchedCode
     * @param chkDatabaseSearchedCode
     */
    public void setChkDatabaseSearchedCode(Vector chkDatabaseSearchedCode) {
        this.chkDatabaseSearchedCode = chkDatabaseSearchedCode;
    }

    /**
     * Method used to get chkDatabaseOldSearchedCode
     * @return chkDatabaseOldSearchedCode
     */
    public Vector getChkDatabaseOldSearchedCode() {
        return chkDatabaseOldSearchedCode;
    }

     /**
     * Method used to set chkDatabaseOldSearchedCode
     * @param chkDatabaseSearchedCode
     */
    public void setChkDatabaseOldSearchedCode(Vector chkDatabaseOldSearchedCode) {
        this.chkDatabaseOldSearchedCode = chkDatabaseOldSearchedCode;
    }
   //Added for COEUSQA-2714 In the Alternative Search in IACUC-end
    
}
