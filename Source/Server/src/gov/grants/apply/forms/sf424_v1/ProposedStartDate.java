//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424_v1;


/**
 * Java content class for ProposedStartDate element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/SF424-V1.0.xsd line 332)
 * <p>
 * <pre>
 * &lt;element name="ProposedStartDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 * </pre>
 * 
 */
public interface ProposedStartDate
    extends javax.xml.bind.Element
{


    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getValue();

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setValue(java.util.Calendar value);

}
