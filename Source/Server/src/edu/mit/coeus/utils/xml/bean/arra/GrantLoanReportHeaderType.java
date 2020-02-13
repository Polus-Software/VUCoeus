//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * A data type representing the report header information for a report on a grant or a loan.
 * Java content class for GrantLoanReportHeaderType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Extension/recoveryrr-ec.xsd line 177)
 * <p>
 * <pre>
 * &lt;complexType name="GrantLoanReportHeaderType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:us:gov:recoveryrr-ext}ReportHeaderType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}GrantLoanAwardCategory"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface GrantLoanReportHeaderType
    extends edu.mit.coeus.utils.xml.bean.arra.ReportHeaderType
{


    /**
     * Gets the value of the grantLoanAwardCategory property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.arra.GrantLoanAwardCategory}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.AwardCategoryType}
     */
    edu.mit.coeus.utils.xml.bean.arra.AwardCategoryType getGrantLoanAwardCategory();

    /**
     * Sets the value of the grantLoanAwardCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.arra.GrantLoanAwardCategory}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.AwardCategoryType}
     */
    void setGrantLoanAwardCategory(edu.mit.coeus.utils.xml.bean.arra.AwardCategoryType value);

}
