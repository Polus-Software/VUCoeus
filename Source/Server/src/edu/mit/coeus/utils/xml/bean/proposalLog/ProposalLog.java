//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.26 at 04:02:57 PM IST 
//


package edu.mit.coeus.utils.xml.bean.proposalLog;


/**
 * Comment describing your root element
 * Java content class for proposalLog element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/VSS/SVN/coeusSource/Branches/4.5/Printing/schemas/proposalLog.xsd line 5)
 * <p>
 * <pre>
 * &lt;element name="proposalLog">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="proposalNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="proposalTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="PI" type="{}PrincipalInvestigator"/>
 *           &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="proposalType" type="{}proposalType"/>
 *           &lt;element name="leadUnit" type="{}leadUnit"/>
 *           &lt;element name="sponsor" type="{}sponsor" minOccurs="0"/>
 *           &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="updateUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="updateTimeStamp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="createUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="createTimeStamp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="deadlinedate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="mergedWith" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface ProposalLog
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.proposalLog.ProposalLogType
{


}
