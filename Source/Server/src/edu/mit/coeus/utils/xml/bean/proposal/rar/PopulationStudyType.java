//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 594)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudyTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HispanicFemaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="HispanicMaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="NotHispanicFemaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="NotHispanicMaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="AlaskaFemaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="AlaskaMaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="AsianFemaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="AsianMaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="HawaiianFemaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="HawaiianMaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="BlackFemaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="BlackMaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="WhiteFemaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *         &lt;element name="WhiteMaleCount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PopulationStudyType {


    /**
     * Gets the value of the alaskaFemaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getAlaskaFemaleCount();

    /**
     * Sets the value of the alaskaFemaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setAlaskaFemaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the hawaiianMaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getHawaiianMaleCount();

    /**
     * Sets the value of the hawaiianMaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setHawaiianMaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the notHispanicFemaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getNotHispanicFemaleCount();

    /**
     * Sets the value of the notHispanicFemaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setNotHispanicFemaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the notHispanicMaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getNotHispanicMaleCount();

    /**
     * Sets the value of the notHispanicMaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setNotHispanicMaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the asianMaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getAsianMaleCount();

    /**
     * Sets the value of the asianMaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setAsianMaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the whiteMaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getWhiteMaleCount();

    /**
     * Sets the value of the whiteMaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setWhiteMaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the hispanicMaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getHispanicMaleCount();

    /**
     * Sets the value of the hispanicMaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setHispanicMaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the blackMaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getBlackMaleCount();

    /**
     * Sets the value of the blackMaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setBlackMaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the asianFemaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getAsianFemaleCount();

    /**
     * Sets the value of the asianFemaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setAsianFemaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the hispanicFemaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getHispanicFemaleCount();

    /**
     * Sets the value of the hispanicFemaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setHispanicFemaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the studyTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getStudyTitle();

    /**
     * Sets the value of the studyTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setStudyTitle(java.lang.String value);

    /**
     * Gets the value of the whiteFemaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getWhiteFemaleCount();

    /**
     * Sets the value of the whiteFemaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setWhiteFemaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the alaskaMaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getAlaskaMaleCount();

    /**
     * Sets the value of the alaskaMaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setAlaskaMaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the blackFemaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getBlackFemaleCount();

    /**
     * Sets the value of the blackFemaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setBlackFemaleCount(java.math.BigInteger value);

    /**
     * Gets the value of the hawaiianFemaleCount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getHawaiianFemaleCount();

    /**
     * Sets the value of the hawaiianFemaleCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setHawaiianFemaleCount(java.math.BigInteger value);

}