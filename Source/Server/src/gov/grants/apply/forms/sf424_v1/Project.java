//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424_v1;


/**
 * Java content class for Project element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/SF424-V1.0.xsd line 309)
 * <p>
 * <pre>
 * &lt;element name="Project">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}ProjectTitle"/>
 *           &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}Location" minOccurs="0"/>
 *           &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}ProposedStartDate"/>
 *           &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}ProposedEndDate"/>
 *           &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}CongressionalDistrict" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface Project
    extends javax.xml.bind.Element, gov.grants.apply.forms.sf424_v1.ProjectType
{


}
