//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.04 at 04:16:35 EST 
//


package gov.grants.apply.forms.performancesite_1_4_v1;


/**
 * Java content class for PerformanceSite_1_4 element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PerformanceSite_1_4-V1.4.xsd line 8)
 * <p>
 * <pre>
 * &lt;element name="PerformanceSite_1_4">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="PrimarySite" type="{http://apply.grants.gov/forms/PerformanceSite_1_4-V1.4}SiteLocationDataType"/>
 *           &lt;element name="OtherSite" type="{http://apply.grants.gov/forms/PerformanceSite_1_4-V1.4}SiteLocationDataType" maxOccurs="29" minOccurs="0"/>
 *           &lt;element name="AttachedFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.4" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface PerformanceSite14
    extends javax.xml.bind.Element, gov.grants.apply.forms.performancesite_1_4_v1.PerformanceSite14Type
{


}
