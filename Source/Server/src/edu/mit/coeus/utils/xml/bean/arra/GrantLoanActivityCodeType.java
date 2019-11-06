//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * Enumeration for grant or loan activity codes.
 * Java content class for GrantLoanActivityCodeType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Subset/recoveryrr-facet.xsd line 15893)
 * <p>
 * <pre>
 * &lt;complexType name="GrantLoanActivityCodeType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;urn:us:gov:recoveryrr-facet>GrantLoanActivityCodeSimpleType">
 *       &lt;attGroup ref="{http://niem.gov/niem/structures/2.0}SimpleObjectAttributeGroup"/>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface GrantLoanActivityCodeType {


    /**
     * Enumeration for grant or loan activity codes.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getValue();

    /**
     * Enumeration for grant or loan activity codes.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setValue(java.lang.String value);

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getId();

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setId(java.lang.String value);

    /**
     * Gets the value of the Metadata property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Metadata property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadata().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.Object}
     * 
     */
    java.util.List getMetadata();

    /**
     * Gets the value of the LinkMetadata property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the LinkMetadata property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkMetadata().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.Object}
     * 
     */
    java.util.List getLinkMetadata();

}
