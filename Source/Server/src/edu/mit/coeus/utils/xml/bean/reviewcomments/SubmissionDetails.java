//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.04 at 03:49:17 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.reviewcomments;


/**
 * Java content class for SubmissionDetails element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Coeus%204.4/Printing/correspondenceTemplates/schema/irb.xsd line 749)
 * <p>
 * <pre>
 * &lt;element name="SubmissionDetails">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="ProtocolNumber">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="SubmissionNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *           &lt;element name="SubmissionTypeCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *           &lt;element name="SubmissionTypeDesc">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="SubmissionTypeQualifierCode" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *           &lt;element name="SubmissionTypeQualifierDesc" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="ProtocolReviewTypeCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *           &lt;element name="ProtocolReviewTypeDesc">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="SubmissionStatusCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *           &lt;element name="SubmissionStatusDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="SubmissionDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *           &lt;element name="SubmissionComments" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2000"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="YesVote" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *           &lt;element name="NoVote" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *           &lt;element name="AbstainerCount" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *           &lt;element name="VotingComments" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2000"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element ref="{http://irb.mit.edu/irbnamespace}ProtocolReviewer" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="SubmissionChecklistInfo" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="ChecklistCodesFormatted" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                     &lt;element name="Checklists" maxOccurs="unbounded">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="ChecklistCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                               &lt;element name="ChecklistDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="ActionType" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="ActionId" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                     &lt;element name="ActionTypeCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                     &lt;element name="ActionTypeDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                     &lt;element name="ActionDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                     &lt;element name="ActionComments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface SubmissionDetails
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.reviewcomments.SubmissionDetailsType
{


}
