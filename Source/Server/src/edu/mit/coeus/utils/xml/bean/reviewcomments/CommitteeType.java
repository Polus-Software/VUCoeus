//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.04 at 03:49:17 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.reviewcomments;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Coeus%204.4/Printing/correspondenceTemplates/schema/irb.xsd line 8)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://irb.mit.edu/irbnamespace}CommitteeMasterData"/>
 *         &lt;element ref="{http://irb.mit.edu/irbnamespace}CommitteeMember" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://irb.mit.edu/irbnamespace}ResearchArea" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://irb.mit.edu/irbnamespace}Schedule" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface CommitteeType {


    /**
     * Gets the value of the ResearchArea property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ResearchArea property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResearchArea().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.reviewcomments.ResearchArea}
     * {@link edu.mit.coeus.utils.xml.bean.reviewcomments.ResearchAreaType}
     * 
     */
    java.util.List getResearchArea();

    /**
     * Gets the value of the Schedule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Schedule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSchedule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.reviewcomments.Schedule}
     * {@link edu.mit.coeus.utils.xml.bean.reviewcomments.ScheduleType}
     * 
     */
    java.util.List getSchedule();

    /**
     * Gets the value of the CommitteeMember property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the CommitteeMember property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCommitteeMember().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.reviewcomments.CommitteeMember}
     * {@link edu.mit.coeus.utils.xml.bean.reviewcomments.CommitteeMemberType}
     * 
     */
    java.util.List getCommitteeMember();

    /**
     * Gets the value of the committeeMasterData property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.reviewcomments.CommitteeMasterDataType}
     *     {@link edu.mit.coeus.utils.xml.bean.reviewcomments.CommitteeMasterData}
     */
    edu.mit.coeus.utils.xml.bean.reviewcomments.CommitteeMasterDataType getCommitteeMasterData();

    /**
     * Sets the value of the committeeMasterData property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.reviewcomments.CommitteeMasterDataType}
     *     {@link edu.mit.coeus.utils.xml.bean.reviewcomments.CommitteeMasterData}
     */
    void setCommitteeMasterData(edu.mit.coeus.utils.xml.bean.reviewcomments.CommitteeMasterDataType value);

}