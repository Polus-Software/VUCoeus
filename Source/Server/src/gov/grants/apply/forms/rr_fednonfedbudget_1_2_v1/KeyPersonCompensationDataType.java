//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.01 at 08:55:08 CDT 
//


package gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1;


/**
 * Java content class for KeyPersonCompensationDataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://trainingapply.grants.gov/apply/forms/schemas/RR_FedNonFedBudget_1_2-V1.2.xsd line 142)
 * <p>
 * <pre>
 * &lt;complexType name="KeyPersonCompensationDataType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2}SectACompensationDataType">
 *       &lt;sequence>
 *         &lt;element name="BaseSalary" type="{http://apply.grants.gov/system/Global-V1.0}DecimalMin1Max14Places2Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface KeyPersonCompensationDataType
    extends gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.SectACompensationDataType
{


    /**
     * Gets the value of the baseSalary property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getBaseSalary();

    /**
     * Sets the value of the baseSalary property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setBaseSalary(java.math.BigDecimal value);

}
