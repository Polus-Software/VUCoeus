//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424_v1;


/**
 * Java content class for RevisionCode1 element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/SF424-V1.0.xsd line 352)
 * <p>
 * <pre>
 * &lt;element name="RevisionCode1" type="{http://apply.grants.gov/forms/SF424-V1.0}RevisionCodeType"/>
 * </pre>
 * 
 */
public interface RevisionCode1
    extends javax.xml.bind.Element
{


    /**
     * A - Increase Award
     * 
     * B - Decrease Award
     * 
     * C - Increase Duration
     * 
     * D - Decrease Duration
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getValue();

    /**
     * A - Increase Award
     * 
     * B - Decrease Award
     * 
     * C - Increase Duration
     * 
     * D - Decrease Duration
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setValue(java.lang.String value);

}
