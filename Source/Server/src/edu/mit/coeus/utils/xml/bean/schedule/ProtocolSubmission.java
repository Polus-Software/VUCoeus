//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.26 at 12:29:58 PM IST 
//


package edu.mit.coeus.utils.xml.bean.schedule;


/**
 * Java content class for ProtocolSubmission element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/VSS/SVN/coeusSource/Branches/4.5/Printing/correspondenceTemplates/schema/irb.xsd line 456)
 * <p>
 * <pre>
 * &lt;element name="ProtocolSubmission">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{http://irb.mit.edu/irbnamespace}ProtocolSummary"/>
 *           &lt;element ref="{http://irb.mit.edu/irbnamespace}SubmissionDetails"/>
 *           &lt;element ref="{http://irb.mit.edu/irbnamespace}Minutes" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface ProtocolSubmission
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.schedule.ProtocolSubmissionType
{


}