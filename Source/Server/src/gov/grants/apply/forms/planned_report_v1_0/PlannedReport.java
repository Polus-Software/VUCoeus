//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.09 at 02:29:50 CDT 
//


package gov.grants.apply.forms.planned_report_v1_0;


/**
 * Java content class for PlannedReport element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Petitforms/schema/PlannedReport-V1.0.xsd line 9)
 * <p>
 * <pre>
 * &lt;element name="PlannedReport">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="Study" maxOccurs="150">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="StudyTitle" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_String1_250DataType"/>
 *                     &lt;element name="DomesticForeignIndicator">
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="Domestic"/>
 *                         &lt;enumeration value="Foreign"/>
 *                       &lt;/restriction>
 *                     &lt;/element>
 *                     &lt;element name="Comments" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_String1_500DataType" minOccurs="0"/>
 *                     &lt;element name="NotHispanic" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_EthnicCategoryDataType"/>
 *                     &lt;element name="Hispanic" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_EthnicCategoryDataType"/>
 *                     &lt;element name="Total" type="{http://apply.grants.gov/forms/PlannedReport-V1.0}PlannedReport_TotalsDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.0" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface PlannedReport
    extends javax.xml.bind.Element, gov.grants.apply.forms.planned_report_v1_0.PlannedReportType
{


}
