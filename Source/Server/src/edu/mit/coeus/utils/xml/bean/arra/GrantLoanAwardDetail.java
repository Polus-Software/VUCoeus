//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * Detail information for a grant or loan award.
 * Java content class for GrantLoanAwardDetail element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Extension/recoveryrr-ec.xsd line 523)
 * <p>
 * <pre>
 * &lt;element name="GrantLoanAwardDetail" type="{urn:us:gov:recoveryrr-ext}GrantLoanAwardDetailType"/>
 * </pre>
 * 
 */
public interface GrantLoanAwardDetail
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.arra.GrantLoanAwardDetailType
{


    /**
     * This property is used to control <a href="http://www.w3.org/TR/2001/REC-xmlschema-0-20010502/#Nils">the xsi:nil feature</a> of W3C XML Schema. 
     * Setting this property to true will cause the output to be &lt;{0} xsi:nil="true" /> regardless of the values of the other properties.
     * 
     */
    boolean isNil();

    /**
     * Passing <code>true</code> will generate xsi:nil in the XML outputThis property is used to control <a href="http://www.w3.org/TR/2001/REC-xmlschema-0-20010502/#Nils">the xsi:nil feature</a> of W3C XML Schema. 
     * Setting this property to true will cause the output to be &lt;{0} xsi:nil="true" /> regardless of the values of the other properties.
     * 
     */
    void setNil(boolean value);

}
