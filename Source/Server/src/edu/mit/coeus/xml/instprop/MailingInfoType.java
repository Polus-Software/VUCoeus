//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.27 at 09:21:38 AM IST 
//


package edu.mit.coeus.xml.instprop;


/**
 * Java content class for mailingInfoType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/VSS/SVN/coeusSource/Branches/4.5/Printing/schemas/instituteProposal.xsd line 133)
 * <p>
 * <pre>
 * &lt;complexType name="mailingInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deadlineDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="deadlineType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mailByOSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mailType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mailAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numberCopies" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mailToPerson" type="{}personType" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface MailingInfoType {


    /**
     * Gets the value of the mailAccount property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getMailAccount();

    /**
     * Sets the value of the mailAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setMailAccount(java.lang.String value);

    /**
     * Gets the value of the mailType property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getMailType();

    /**
     * Sets the value of the mailType property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setMailType(java.lang.String value);

    /**
     * Gets the value of the deadlineDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getDeadlineDate();

    /**
     * Sets the value of the deadlineDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setDeadlineDate(java.util.Calendar value);

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
     * Gets the value of the mailToPerson property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.xml.instprop.PersonType}
     */
    edu.mit.coeus.xml.instprop.PersonType getMailToPerson();

    /**
     * Sets the value of the mailToPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.xml.instprop.PersonType}
     */
    void setMailToPerson(edu.mit.coeus.xml.instprop.PersonType value);

    /**
     * Gets the value of the mailByOSP property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getMailByOSP();

    /**
     * Sets the value of the mailByOSP property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setMailByOSP(java.lang.String value);

    /**
     * Gets the value of the numberCopies property.
     * 
     */
    int getNumberCopies();

    /**
     * Sets the value of the numberCopies property.
     * 
     */
    void setNumberCopies(int value);

    /**
     * Gets the value of the deadlineType property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDeadlineType();

    /**
     * Sets the value of the deadlineType property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDeadlineType(java.lang.String value);

}
