//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_performancesite_v1;


/**
 * Java content class for RR_PerformanceSite element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/RR_PerformanceSite-V1.0.xsd line 20)
 * <p>
 * <pre>
 * &lt;element name="RR_PerformanceSite">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="PrimarySite" type="{http://apply.grants.gov/forms/RR_PerformanceSite-V1.0}SiteLocationDataType"/>
 *           &lt;element name="OtherSite" type="{http://apply.grants.gov/forms/RR_PerformanceSite-V1.0}SiteLocationDataType" maxOccurs="7" minOccurs="0"/>
 *           &lt;element name="AttachedFile" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}FormVersionDataType" fixed="1.0" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface RRPerformanceSite
    extends javax.xml.bind.Element, gov.grants.apply.forms.rr_performancesite_v1.RRPerformanceSiteType
{


}
