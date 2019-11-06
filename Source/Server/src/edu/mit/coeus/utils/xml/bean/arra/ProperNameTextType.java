//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * A data type for a word or phrase by which a person or thing is known, referred, or addressed.
 * Java content class for ProperNameTextType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Subset/niem/niem-core/2.0/niem-corec.xsd line 94)
 * <p>
 * <pre>
 * &lt;complexType name="ProperNameTextType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://niem.gov/niem/niem-core/2.0>TextType">
 *       &lt;attribute ref="{http://niem.gov/niem/niem-core/2.0}formCode"/>
 *       &lt;attribute ref="{http://niem.gov/niem/niem-core/2.0}scriptCode"/>
 *       &lt;attribute ref="{http://niem.gov/niem/niem-core/2.0}transliterationCode"/>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProperNameTextType
    extends edu.mit.coeus.utils.xml.bean.arra.TextType
{


    /**
     * Gets the value of the scriptCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getScriptCode();

    /**
     * Sets the value of the scriptCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setScriptCode(java.lang.String value);

    /**
     * Gets the value of the transliterationCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getTransliterationCode();

    /**
     * Sets the value of the transliterationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setTransliterationCode(java.lang.String value);

    /**
     * Gets the value of the formCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFormCode();

    /**
     * Sets the value of the formCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFormCode(java.lang.String value);

}
