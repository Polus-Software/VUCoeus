//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih;


/**
 * Java content class for ApplicantOrganizationType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/nihschema.xsd line 86)
 * <p>
 * <pre>
 * &lt;complexType name="ApplicantOrganizationType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}ApplicantOrganizationType">
 *       &lt;sequence>
 *         &lt;element name="OrganizationClassification">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CategoryCode" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *                   &lt;element name="SubCategoryCode" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ApplicantOrganizationType
    extends edu.mit.coeus.utils.xml.bean.proposal.rar.ApplicantOrganizationType
{


    /**
     * Gets the value of the organizationClassification property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.OrganizationClassificationType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.OrganizationClassificationType getOrganizationClassification();

    /**
     * Sets the value of the organizationClassification property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.OrganizationClassificationType}
     */
    void setOrganizationClassification(edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.OrganizationClassificationType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/nihschema.xsd line 91)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="CategoryCode" type="{http://www.w3.org/2001/XMLSchema}token"/>
     *         &lt;element name="SubCategoryCode" type="{http://www.w3.org/2001/XMLSchema}token"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface OrganizationClassificationType {


        /**
         * Gets the value of the subCategoryCode property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getSubCategoryCode();

        /**
         * Sets the value of the subCategoryCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setSubCategoryCode(java.lang.String value);

        /**
         * Gets the value of the categoryCode property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCategoryCode();

        /**
         * Sets the value of the categoryCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCategoryCode(java.lang.String value);

    }

}