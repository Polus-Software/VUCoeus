//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * A name of an organization.
 * Java content class for OrganizationName element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Subset/niem/niem-core/2.0/niem-corec.xsd line 243)
 * <p>
 * <pre>
 * &lt;element name="OrganizationName">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;maxLength value="55"/>
 *   &lt;/restriction>
 * &lt;/element>
 * </pre>
 * 
 */
public interface OrganizationName
    extends javax.xml.bind.Element
{


    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getValue();

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setValue(java.lang.String value);

    /**
     * This property is used to control <a href="http://www.w3.org/TR/2001/REC-xmlschema-0-20010502/#Nils">the xsi:nil feature</a> of W3C XML Schema. 
     * Setting this property to true will cause the output to be &lt;{0} xsi:nil="true" /> regardless of the values of the other properties.
     * 
     */
    boolean isNil();

    /**
     * Passing <code>true</code> will generate xsi:nil in the XML outputThis property is used to control <a href="http://www.w3.org/TR/2001/REC-xmlschema-0-20010502/#Nils">the xsi:nil feature</a> of W3C XML Schema. 
     * Setting this property to true will cause the output to be &lt;{0} xsi:nil="true" /> regardless of the values of the other properties.
     * 
     */
    void setNil(boolean value);

}