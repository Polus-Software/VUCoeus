//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.09 at 02:29:50 CDT 
//


package gov.grants.apply.forms.planned_report_v1_0;


/**
 * Java content class for PlannedReport_EthnicCategoryDataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Petitforms/schema/PlannedReport-V1.0.xsd line 35)
 * <p>
 * <pre>
 * &lt;complexType name="PlannedReport_EthnicCategoryDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Female" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_RacialCategoryDataType"/>
 *         &lt;element name="Male" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_RacialCategoryDataType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PlannedReportEthnicCategoryDataType {


    /**
     * Gets the value of the male property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType}
     */
    gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType getMale();

    /**
     * Sets the value of the male property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType}
     */
    void setMale(gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType value);

    /**
     * Gets the value of the female property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType}
     */
    gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType getFemale();

    /**
     * Sets the value of the female property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType}
     */
    void setFemale(gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType value);

}
