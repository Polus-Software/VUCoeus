//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.13 at 02:14:23 EST 
//


package edu.mit.coeus.utils.xml.bean.award;


/**
 * Java content class for CommentDetailsType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/temp/jaxb1.0.4/bin/AwardNotice.xsd line 138)
 * <p>
 * <pre>
 * &lt;complexType name="CommentDetailsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AwardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SequenceNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="CommentCode" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PrintChecklist" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface CommentDetailsType {


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
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getComments();

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setComments(java.lang.String value);

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
     * Gets the value of the printChecklist property.
     * 
     */
    boolean isPrintChecklist();

    /**
     * Sets the value of the printChecklist property.
     * 
     */
    void setPrintChecklist(boolean value);

    /**
     * Gets the value of the commentCode property.
     * 
     */
    int getCommentCode();

    /**
     * Sets the value of the commentCode property.
     * 
     */
    void setCommentCode(int value);

}