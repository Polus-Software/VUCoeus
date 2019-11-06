//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.26 at 12:29:58 PM IST 
//


package edu.mit.coeus.utils.xml.bean.schedule;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/VSS/SVN/coeusSource/Branches/4.5/Printing/correspondenceTemplates/schema/irb.xsd line 457)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://irb.mit.edu/irbnamespace}ProtocolSummary"/>
 *         &lt;element ref="{http://irb.mit.edu/irbnamespace}SubmissionDetails"/>
 *         &lt;element ref="{http://irb.mit.edu/irbnamespace}Minutes" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProtocolSubmissionType {


    /**
     * Gets the value of the Minutes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Minutes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMinutes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.schedule.MinutesType}
     * {@link edu.mit.coeus.utils.xml.bean.schedule.Minutes}
     * 
     */
    java.util.List getMinutes();

    /**
     * Gets the value of the submissionDetails property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.SubmissionDetailsType}
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.SubmissionDetails}
     */
    edu.mit.coeus.utils.xml.bean.schedule.SubmissionDetailsType getSubmissionDetails();

    /**
     * Sets the value of the submissionDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.SubmissionDetailsType}
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.SubmissionDetails}
     */
    void setSubmissionDetails(edu.mit.coeus.utils.xml.bean.schedule.SubmissionDetailsType value);

    /**
     * Gets the value of the protocolSummary property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.ProtocolSummary}
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.ProtocolSummaryType}
     */
    edu.mit.coeus.utils.xml.bean.schedule.ProtocolSummaryType getProtocolSummary();

    /**
     * Sets the value of the protocolSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.ProtocolSummary}
     *     {@link edu.mit.coeus.utils.xml.bean.schedule.ProtocolSummaryType}
     */
    void setProtocolSummary(edu.mit.coeus.utils.xml.bean.schedule.ProtocolSummaryType value);

}
