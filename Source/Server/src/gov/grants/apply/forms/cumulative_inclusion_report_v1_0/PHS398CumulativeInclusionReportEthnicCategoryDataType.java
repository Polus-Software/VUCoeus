//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.11 at 11:55:52 CDT 
//


package gov.grants.apply.forms.cumulative_inclusion_report_v1_0;


/**
 * Java content class for PHS398_CumulativeInclusionReport_EthnicCategoryDataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Petitforms/schema/PHS398_CumulativeInclusionReport-V1.0.xsd line 24)
 * <p>
 * <pre>
 * &lt;complexType name="PHS398_CumulativeInclusionReport_EthnicCategoryDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Female" type="{http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0}PHS398_CumulativeInclusionReport_RacialCategoryDataType"/>
 *         &lt;element name="Male" type="{http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0}PHS398_CumulativeInclusionReport_RacialCategoryDataType"/>
 *         &lt;element name="UnknownGender" type="{http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0}PHS398_CumulativeInclusionReport_RacialCategoryDataType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PHS398CumulativeInclusionReportEthnicCategoryDataType {


    /**
     * Gets the value of the unknownGender property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType}
     */
    gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType getUnknownGender();

    /**
     * Sets the value of the unknownGender property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType}
     */
    void setUnknownGender(gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType value);

    /**
     * Gets the value of the male property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType}
     */
    gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType getMale();

    /**
     * Sets the value of the male property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType}
     */
    void setMale(gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType value);

    /**
     * Gets the value of the female property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType}
     */
    gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType getFemale();

    /**
     * Sets the value of the female property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType}
     */
    void setFemale(gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType value);

}