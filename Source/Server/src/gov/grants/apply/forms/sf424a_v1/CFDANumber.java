//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1;


/**
 * Java content class for CFDANumber element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/forms/schemas/SF424A-V1.0.xsd line 205)
 * <p>
 * <pre>
 * &lt;element name="CFDANumber" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max15Type"/>
 * </pre>
 * 
 */
public interface CFDANumber
    extends javax.xml.bind.Element
{


    /**
     * String - Min length 1, max length 15
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getValue();

    /**
     * String - Min length 1, max length 15
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setValue(java.lang.String value);

}
