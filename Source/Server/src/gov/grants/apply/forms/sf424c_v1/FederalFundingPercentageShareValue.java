//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424c_v1;


/**
 * Java content class for FederalFundingPercentageShareValue element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/forms/schemas/SF424C-V1.0.xsd line 73)
 * <p>
 * <pre>
 * &lt;element name="FederalFundingPercentageShareValue" type="{http://apply.grants.gov/system/Global-V1.0}IntegerMin1Max3Type"/>
 * </pre>
 * 
 */
public interface FederalFundingPercentageShareValue
    extends javax.xml.bind.Element
{


    /**
     * Integer - Min length 1, max length 3
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getValue();

    /**
     * Integer - Min length 1, max length 3
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setValue(java.math.BigInteger value);

}
