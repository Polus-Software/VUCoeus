//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_personaldata_1_2_v1_2;


/**
 * Java content class for RR_PersonalData_1_2 element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/RR_PersonalData_1_2-V1.2.xsd line 13)
 * <p>
 * <pre>
 * &lt;element name="RR_PersonalData_1_2">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="ProjectDirector" type="{http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2}DirectorType"/>
 *           &lt;element name="Co-ProjectDirector" type="{http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2}DirectorType" maxOccurs="4" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.2" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface RRPersonalData12
    extends javax.xml.bind.Element, gov.grants.apply.forms.rr_personaldata_1_2_v1_2.RRPersonalData12Type
{


}