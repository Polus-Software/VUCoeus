//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1;


/**
 * Java content class for SummaryLineItem element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/forms/schemas/SF424A-V1.0.xsd line 280)
 * <p>
 * <pre>
 * &lt;element name="SummaryLineItem">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{http://apply.grants.gov/forms/SF424A-V1.0}CFDANumber" minOccurs="0"/>
 *           &lt;group ref="{http://apply.grants.gov/forms/SF424A-V1.0}BudgetAmountGroup"/>
 *         &lt;/sequence>
 *         &lt;attribute ref="{http://apply.grants.gov/forms/SF424A-V1.0}activityTitle use="required""/>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface SummaryLineItem
    extends javax.xml.bind.Element, gov.grants.apply.forms.sf424a_v1.SummaryLineItemType
{


}
