//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.11.22 at 02:53:02 PM IST 
//


package edu.mit.coeus.utils.xml.bean.questionnaire;


/**
 * Java content class for QuestionInfoType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/questionnaire%20printing/Questionnaire.xsd line 48)
 * <p>
 * <pre>
 * &lt;complexType name="QuestionInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="QuestionId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="QuestionNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Question" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AnswerDataType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AnswerMaxLength" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ParentQuestionNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="QuestionSeqNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ConditionFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Condition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ConditionValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AnswerInfo" type="{}AnswerInfoType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface QuestionInfoType {


    /**
     * Gets the value of the answerMaxLength property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAnswerMaxLength();

    /**
     * Sets the value of the answerMaxLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAnswerMaxLength(java.lang.String value);

    /**
     * Gets the value of the conditionFlag property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getConditionFlag();

    /**
     * Sets the value of the conditionFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setConditionFlag(java.lang.String value);

    /**
     * Gets the value of the parentQuestionNumber property.
     * 
     */
    int getParentQuestionNumber();

    /**
     * Sets the value of the parentQuestionNumber property.
     * 
     */
    void setParentQuestionNumber(int value);

    /**
     * Gets the value of the conditionValue property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getConditionValue();

    /**
     * Sets the value of the conditionValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setConditionValue(java.lang.String value);

    /**
     * Gets the value of the questionSeqNumber property.
     * 
     */
    int getQuestionSeqNumber();

    /**
     * Sets the value of the questionSeqNumber property.
     * 
     */
    void setQuestionSeqNumber(int value);

    /**
     * Gets the value of the condition property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCondition();

    /**
     * Sets the value of the condition property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCondition(java.lang.String value);

    /**
     * Gets the value of the questionId property.
     * 
     */
    int getQuestionId();

    /**
     * Sets the value of the questionId property.
     * 
     */
    void setQuestionId(int value);

    /**
     * Gets the value of the AnswerInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the AnswerInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnswerInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link edu.mit.coeus.utils.xml.bean.questionnaire.AnswerInfoType}
     * 
     */
    java.util.List getAnswerInfo();

    /**
     * Gets the value of the question property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getQuestion();

    /**
     * Sets the value of the question property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setQuestion(java.lang.String value);

    /**
     * Gets the value of the answerDataType property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAnswerDataType();

    /**
     * Sets the value of the answerDataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAnswerDataType(java.lang.String value);

    /**
     * Gets the value of the questionNumber property.
     * 
     */
    int getQuestionNumber();

    /**
     * Sets the value of the questionNumber property.
     * 
     */
    void setQuestionNumber(int value);

}
