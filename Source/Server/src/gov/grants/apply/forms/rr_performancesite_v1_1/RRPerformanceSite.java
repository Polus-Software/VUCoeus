//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_performancesite_v1_1;


/**
 * Java content class for RR_PerformanceSite element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/RR_PerformanceSite-V1.1.xsd line 7)
 * <p>
 * <pre>
 * &lt;element name="RR_PerformanceSite">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="PrimarySite" type="{http://apply.grants.gov/forms/RR_PerformanceSite-V1-1}SiteLocationDataType"/>
 *           &lt;element name="OtherSite" type="{http://apply.grants.gov/forms/RR_PerformanceSite-V1-1}SiteLocationDataType" maxOccurs="7" minOccurs="0"/>
 *           &lt;element name="AttachedFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.1" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface RRPerformanceSite
    extends javax.xml.bind.Element, gov.grants.apply.forms.rr_performancesite_v1_1.RRPerformanceSiteType
{


}