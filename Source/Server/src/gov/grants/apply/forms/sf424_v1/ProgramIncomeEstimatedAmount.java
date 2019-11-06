//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424_v1;


/**
 * Java content class for ProgramIncomeEstimatedAmount element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/SF424-V1.0.xsd line 304)
 * <p>
 * <pre>
 * &lt;element name="ProgramIncomeEstimatedAmount" type="{http://apply.grants.gov/system/Global-V1.0}DecimalMin1Max14Places2Type"/>
 * </pre>
 * 
 */
public interface ProgramIncomeEstimatedAmount
    extends javax.xml.bind.Element
{


    /**
     * Decimal - Min length 1, max length 14, 2 places after decimal
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getValue();

    /**
     * Decimal - Min length 1, max length 14, 2 places after decimal
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setValue(java.math.BigDecimal value);

}
