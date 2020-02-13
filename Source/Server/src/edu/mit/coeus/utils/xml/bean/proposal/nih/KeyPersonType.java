//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih;


/**
 * Java content class for KeyPersonType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/nihschema.xsd line 64)
 * <p>
 * <pre>
 * &lt;complexType name="KeyPersonType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}KeyPersonType">
 *       &lt;sequence>
 *         &lt;element ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace}AccountIdentifier" minOccurs="0"/>
 *         &lt;element ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace}RoleOnProject"/>
 *         &lt;element name="OrganizationDuns" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}DUNSType" minOccurs="0"/>
 *         &lt;element name="NIHBiographicalSketch" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace}KeyPersonBiosketchType"/>
 *         &lt;element name="Degree" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="3" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface KeyPersonType
    extends edu.mit.coeus.utils.xml.bean.proposal.rar.KeyPersonType
{


    /**
     * Gets the value of the roleOnProject property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRoleOnProject();

    /**
     * Sets the value of the roleOnProject property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRoleOnProject(java.lang.String value);

    /**
     * Gets the value of the Degree property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Degree property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDegree().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String}
     * 
     */
    java.util.List getDegree();

    /**
     * Gets the value of the accountIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAccountIdentifier();

    /**
     * Sets the value of the accountIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAccountIdentifier(java.lang.String value);

    /**
     * Gets the value of the nihBiographicalSketch property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.nih.KeyPersonBiosketchType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.nih.KeyPersonBiosketchType getNIHBiographicalSketch();

    /**
     * Sets the value of the nihBiographicalSketch property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.nih.KeyPersonBiosketchType}
     */
    void setNIHBiographicalSketch(edu.mit.coeus.utils.xml.bean.proposal.nih.KeyPersonBiosketchType value);

    /**
     * Gets the value of the organizationDuns property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOrganizationDuns();

    /**
     * Sets the value of the organizationDuns property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOrganizationDuns(java.lang.String value);

}
