//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for ResearchAndRelatedProject element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 551)
 * <p>
 * <pre>
 * &lt;element name="ResearchAndRelatedProject">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}ProjectDescription"/>
 *           &lt;sequence>
 *             &lt;element name="KeyPerson" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}KeyPersonType"/>
 *           &lt;/sequence>
 *           &lt;element ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}OrgAssurances"/>
 *           &lt;element ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}BudgetSummary"/>
 *           &lt;element ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}ResearchCoverPage"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface ResearchAndRelatedProject
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.proposal.rar.ResearchAndRelatedProjectType
{


}
