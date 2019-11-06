//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.13 at 02:14:23 EST 
//


package edu.mit.coeus.utils.xml.bean.award;


/**
 * Java content class for AmountInfoType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/temp/jaxb1.0.4/bin/AwardNotice.xsd line 169)
 * <p>
 * <pre>
 * &lt;complexType name="AmountInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AwardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SequenceNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="AmountSequenceNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="AnticipatedTotalAmtModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AnticipatedTotalAmt" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="AnticipatedDistributableAmtModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AnticipatedDistributableAmt" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="FinalExpirationDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="FinalExpDateModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CurrentFundEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="CurrentFundEffectiveDateModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AmtObligatedToDate" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="AmtObligatedToDateModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ObligatedDistributableAmtModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ObligatedDistributableAmt" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="ObligationExpirationDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="ObligationExpDateModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransactionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransactionDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="AnticipatedChange" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="ObligatedChange" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="EntryType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EOMProcess" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TreeLevel" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="TotalStartDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="TotalEndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="TotalObligatedAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="TotalDistributableAmount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="ChildGrantTotal" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="RemainingAnticipatedAmt" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="ObligatedChangeDirect" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="ObligatedChangeIndirect" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="ObligatedTotalDirect" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="ObligatedTotalIndirect" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="AnticipatedChangeDirect" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="AnticipatedChangeIndirect" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="AnticipatedTotalDirect" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="AnticipatedTotalIndirect" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="EnableAwdAntOblDirectIndirectCost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface AmountInfoType {


    /**
     * Gets the value of the finalExpirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getFinalExpirationDate();

    /**
     * Sets the value of the finalExpirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setFinalExpirationDate(java.util.Calendar value);

    /**
     * Gets the value of the entryType property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEntryType();

    /**
     * Sets the value of the entryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEntryType(java.lang.String value);

    /**
     * Gets the value of the anticipatedTotalAmt property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getAnticipatedTotalAmt();

    /**
     * Sets the value of the anticipatedTotalAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setAnticipatedTotalAmt(java.math.BigDecimal value);

    /**
     * Gets the value of the obligatedChangeIndirect property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getObligatedChangeIndirect();

    /**
     * Sets the value of the obligatedChangeIndirect property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setObligatedChangeIndirect(java.math.BigDecimal value);

    /**
     * Gets the value of the anticipatedDistributableAmt property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getAnticipatedDistributableAmt();

    /**
     * Sets the value of the anticipatedDistributableAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setAnticipatedDistributableAmt(java.math.BigDecimal value);

    /**
     * Gets the value of the obligationExpirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getObligationExpirationDate();

    /**
     * Sets the value of the obligationExpirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setObligationExpirationDate(java.util.Calendar value);

    /**
     * Gets the value of the transactionDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getTransactionDate();

    /**
     * Sets the value of the transactionDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setTransactionDate(java.util.Calendar value);

    /**
     * Gets the value of the obligatedChangeDirect property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getObligatedChangeDirect();

    /**
     * Sets the value of the obligatedChangeDirect property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setObligatedChangeDirect(java.math.BigDecimal value);

    /**
     * Gets the value of the transactionId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getTransactionId();

    /**
     * Sets the value of the transactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setTransactionId(java.lang.String value);

    /**
     * Gets the value of the finalExpDateModified property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFinalExpDateModified();

    /**
     * Sets the value of the finalExpDateModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFinalExpDateModified(java.lang.String value);

    /**
     * Gets the value of the anticipatedChange property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getAnticipatedChange();

    /**
     * Sets the value of the anticipatedChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setAnticipatedChange(java.math.BigDecimal value);

    /**
     * Gets the value of the childGrantTotal property.
     * 
     */
    double getChildGrantTotal();

    /**
     * Sets the value of the childGrantTotal property.
     * 
     */
    void setChildGrantTotal(double value);

    /**
     * Gets the value of the totalDistributableAmount property.
     * 
     */
    double getTotalDistributableAmount();

    /**
     * Sets the value of the totalDistributableAmount property.
     * 
     */
    void setTotalDistributableAmount(double value);

    /**
     * Gets the value of the sequenceNumber property.
     * 
     */
    int getSequenceNumber();

    /**
     * Sets the value of the sequenceNumber property.
     * 
     */
    void setSequenceNumber(int value);

    /**
     * Gets the value of the obligatedTotalDirect property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getObligatedTotalDirect();

    /**
     * Sets the value of the obligatedTotalDirect property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setObligatedTotalDirect(java.math.BigDecimal value);

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAccountNumber();

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAccountNumber(java.lang.String value);

    /**
     * Gets the value of the totalStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getTotalStartDate();

    /**
     * Sets the value of the totalStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setTotalStartDate(java.util.Calendar value);

    /**
     * Gets the value of the totalEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getTotalEndDate();

    /**
     * Sets the value of the totalEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setTotalEndDate(java.util.Calendar value);

    /**
     * Gets the value of the obligatedChange property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getObligatedChange();

    /**
     * Sets the value of the obligatedChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setObligatedChange(java.math.BigDecimal value);

    /**
     * Gets the value of the amtObligatedToDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getAmtObligatedToDate();

    /**
     * Sets the value of the amtObligatedToDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setAmtObligatedToDate(java.math.BigDecimal value);

    /**
     * Gets the value of the anticipatedDistributableAmtModified property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAnticipatedDistributableAmtModified();

    /**
     * Sets the value of the anticipatedDistributableAmtModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAnticipatedDistributableAmtModified(java.lang.String value);

    /**
     * Gets the value of the currentFundEffectiveDateModified property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCurrentFundEffectiveDateModified();

    /**
     * Sets the value of the currentFundEffectiveDateModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCurrentFundEffectiveDateModified(java.lang.String value);

    /**
     * Gets the value of the amountSequenceNumber property.
     * 
     */
    int getAmountSequenceNumber();

    /**
     * Sets the value of the amountSequenceNumber property.
     * 
     */
    void setAmountSequenceNumber(int value);

    /**
     * Gets the value of the eomProcess property.
     * 
     */
    boolean isEOMProcess();

    /**
     * Sets the value of the eomProcess property.
     * 
     */
    void setEOMProcess(boolean value);

    /**
     * Gets the value of the treeLevel property.
     * 
     */
    int getTreeLevel();

    /**
     * Sets the value of the treeLevel property.
     * 
     */
    void setTreeLevel(int value);

    /**
     * Gets the value of the enableAwdAntOblDirectIndirectCost property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEnableAwdAntOblDirectIndirectCost();

    /**
     * Sets the value of the enableAwdAntOblDirectIndirectCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEnableAwdAntOblDirectIndirectCost(java.lang.String value);

    /**
     * Gets the value of the obligatedTotalIndirect property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getObligatedTotalIndirect();

    /**
     * Sets the value of the obligatedTotalIndirect property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setObligatedTotalIndirect(java.math.BigDecimal value);

    /**
     * Gets the value of the remainingAnticipatedAmt property.
     * 
     */
    double getRemainingAnticipatedAmt();

    /**
     * Sets the value of the remainingAnticipatedAmt property.
     * 
     */
    void setRemainingAnticipatedAmt(double value);

    /**
     * Gets the value of the currentFundEffectiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getCurrentFundEffectiveDate();

    /**
     * Sets the value of the currentFundEffectiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setCurrentFundEffectiveDate(java.util.Calendar value);

    /**
     * Gets the value of the amtObligatedToDateModified property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAmtObligatedToDateModified();

    /**
     * Sets the value of the amtObligatedToDateModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAmtObligatedToDateModified(java.lang.String value);

    /**
     * Gets the value of the obligatedDistributableAmtModified property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getObligatedDistributableAmtModified();

    /**
     * Sets the value of the obligatedDistributableAmtModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setObligatedDistributableAmtModified(java.lang.String value);

    /**
     * Gets the value of the anticipatedChangeIndirect property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getAnticipatedChangeIndirect();

    /**
     * Sets the value of the anticipatedChangeIndirect property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setAnticipatedChangeIndirect(java.math.BigDecimal value);

    /**
     * Gets the value of the totalObligatedAmount property.
     * 
     */
    double getTotalObligatedAmount();

    /**
     * Sets the value of the totalObligatedAmount property.
     * 
     */
    void setTotalObligatedAmount(double value);

    /**
     * Gets the value of the obligationExpDateModified property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getObligationExpDateModified();

    /**
     * Sets the value of the obligationExpDateModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setObligationExpDateModified(java.lang.String value);

    /**
     * Gets the value of the awardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAwardNumber();

    /**
     * Sets the value of the awardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAwardNumber(java.lang.String value);

    /**
     * Gets the value of the anticipatedTotalIndirect property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getAnticipatedTotalIndirect();

    /**
     * Sets the value of the anticipatedTotalIndirect property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setAnticipatedTotalIndirect(java.math.BigDecimal value);

    /**
     * Gets the value of the anticipatedTotalDirect property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getAnticipatedTotalDirect();

    /**
     * Sets the value of the anticipatedTotalDirect property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setAnticipatedTotalDirect(java.math.BigDecimal value);

    /**
     * Gets the value of the anticipatedTotalAmtModified property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAnticipatedTotalAmtModified();

    /**
     * Sets the value of the anticipatedTotalAmtModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAnticipatedTotalAmtModified(java.lang.String value);

    /**
     * Gets the value of the anticipatedChangeDirect property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getAnticipatedChangeDirect();

    /**
     * Sets the value of the anticipatedChangeDirect property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setAnticipatedChangeDirect(java.math.BigDecimal value);

    /**
     * Gets the value of the obligatedDistributableAmt property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getObligatedDistributableAmt();

    /**
     * Sets the value of the obligatedDistributableAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setObligatedDistributableAmt(java.math.BigDecimal value);

}
