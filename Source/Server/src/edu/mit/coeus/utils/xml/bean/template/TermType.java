//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.2-b15-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.05.13 at 09:45:36 EDT 
//


package edu.mit.coeus.utils.xml.bean.template;


/**
 * Java content class for termType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/TEMP/template.xsd line 56)
 * <p>
 * <pre>
 * &lt;complexType name="termType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TermDetails" type="{}termDetailsType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface TermType {


    /**
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDescription();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDescription(java.lang.String value);

    /**
     * Gets the value of the TermDetails property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the TermDetails property.
     * 
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTermDetails().add(newItem);
     * </pre>
     * 
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.template.TermDetailsType}
     * 
     */
    java.util.List getTermDetails();

}