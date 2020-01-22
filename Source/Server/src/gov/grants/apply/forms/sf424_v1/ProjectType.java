//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/SF424-V1.0.xsd line 310)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}ProjectTitle"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}Location" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}ProposedStartDate"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}ProposedEndDate"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}CongressionalDistrict" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProjectType {


    /**
     * Gets the value of the proposedStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getProposedStartDate();

    /**
     * Sets the value of the proposedStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setProposedStartDate(java.util.Calendar value);

    /**
     * Gets the value of the congressionalDistrict property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCongressionalDistrict();

    /**
     * Sets the value of the congressionalDistrict property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCongressionalDistrict(java.lang.String value);

    /**
     * Gets the value of the projectTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProjectTitle();

    /**
     * Sets the value of the projectTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProjectTitle(java.lang.String value);

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getLocation();

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setLocation(java.lang.String value);

    /**
     * Gets the value of the proposedEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getProposedEndDate();

    /**
     * Sets the value of the proposedEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setProposedEndDate(java.util.Calendar value);

}