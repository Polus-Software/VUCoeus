//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.system.globallibrary_v1;


/**
 * TS194 v4020: N9 Segment (Heading), Position 0600
 * 
 * N901(Reference Identification Qualifier)/N902(Reference Identification)
 * Java content class for FederalID element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/system/schemas/GlobalLibrary-V1.0.xsd line 59)
 * <p>
 * <pre>
 * &lt;element name="FederalID" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max30Type"/>
 * </pre>
 * 
 */
public interface FederalID
    extends javax.xml.bind.Element
{


    /**
     * String - Min length 1, max length 30
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getValue();

    /**
     * String - Min length 1, max length 30
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setValue(java.lang.String value);

}
