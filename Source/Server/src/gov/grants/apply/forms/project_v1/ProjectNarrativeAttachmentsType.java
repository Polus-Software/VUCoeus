//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.project_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/forms/schemas/Project-V1.0.xsd line 19)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Attachments" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachmentGroupMin1Max100DataType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}FormVersionDataType" fixed="1.0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProjectNarrativeAttachmentsType {


    /**
     * Gets the value of the formVersion property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link java.lang.String}
     */
    java.lang.String getFormVersion();

    /**
     * Sets the value of the formVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link java.lang.String}
     */
    void setFormVersion(java.lang.String value);

    /**
     * Gets the value of the attachments property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType}
     */
    gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType getAttachments();

    /**
     * Sets the value of the attachments property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType}
     */
    void setAttachments(gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType value);

}
