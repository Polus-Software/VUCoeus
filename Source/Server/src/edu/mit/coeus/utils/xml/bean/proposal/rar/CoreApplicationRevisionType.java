//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for CoreApplicationRevisionType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 66)
 * <p>
 * <pre>
 * &lt;complexType name="CoreApplicationRevisionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ActionCode" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="ActionDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface CoreApplicationRevisionType {


    /**
     * SF424 item 8.  Describes the type of revision action requested with this submission.  Possible actions are:  Increase Award, Decrease Award, Increase Duration, Decrease Duration, or Other.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getActionCode();

    /**
     * SF424 item 8.  Describes the type of revision action requested with this submission.  Possible actions are:  Increase Award, Decrease Award, Increase Duration, Decrease Duration, or Other.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setActionCode(java.lang.String value);

    /**
     * SF424 item 8.  If Revision Action is *other*, this provides description of the action being requested.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getActionDescription();

    /**
     * SF424 item 8.  If Revision Action is *other*, this provides description of the action being requested.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setActionDescription(java.lang.String value);

}
