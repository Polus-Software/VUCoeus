//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.30 at 02:44:18 EDT 
//


package gov.grants.apply.forms.phs_coverletter_v1_2;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS_CoverLetter_1_2-V1.2.xsd line 7)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CoverLetterFile">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CoverLetterFilename" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.2" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PHSCoverLetter12Type {


    /**
     * Gets the value of the coverLetterFile property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs_coverletter_v1_2.PHSCoverLetter12Type.CoverLetterFileType}
     */
    gov.grants.apply.forms.phs_coverletter_v1_2.PHSCoverLetter12Type.CoverLetterFileType getCoverLetterFile();

    /**
     * Sets the value of the coverLetterFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs_coverletter_v1_2.PHSCoverLetter12Type.CoverLetterFileType}
     */
    void setCoverLetterFile(gov.grants.apply.forms.phs_coverletter_v1_2.PHSCoverLetter12Type.CoverLetterFileType value);

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
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS_CoverLetter_1_2-V1.2.xsd line 10)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="CoverLetterFilename" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface CoverLetterFileType {


        /**
         * Gets the value of the coverLetterFilename property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
         */
        gov.grants.apply.system.attachments_v1.AttachedFileDataType getCoverLetterFilename();

        /**
         * Sets the value of the coverLetterFilename property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
         */
        void setCoverLetterFilename(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

    }

}
