//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.04 at 03:49:17 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.reviewcomments;


/**
 * Java content class for CommitteeMemberRole element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Coeus%204.4/Printing/correspondenceTemplates/schema/irb.xsd line 100)
 * <p>
 * <pre>
 * &lt;element name="CommitteeMemberRole">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="MemberRoleCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *           &lt;element name="MemberRoleDesc">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="MemberRoleStartDt" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *           &lt;element name="MemberRoleEndDt" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface CommitteeMemberRole
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.reviewcomments.CommitteeMemberRoleType
{


}
