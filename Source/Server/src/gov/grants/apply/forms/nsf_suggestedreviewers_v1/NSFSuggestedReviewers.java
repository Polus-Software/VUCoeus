//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.nsf_suggestedreviewers_v1;


/**
 * Java content class for NSF_SuggestedReviewers element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Coeus/S2S/build/cust/NSF_SuggestedReviewers-V1.0.xsd line 17)
 * <p>
 * <pre>
 * &lt;element name="NSF_SuggestedReviewers">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="SuggestedReviewers" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="4000"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="ReviewersNotToInclude" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="4000"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}FormVersionDataType" fixed="1.0" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface NSFSuggestedReviewers
    extends javax.xml.bind.Element, gov.grants.apply.forms.nsf_suggestedreviewers_v1.NSFSuggestedReviewersType
{


}