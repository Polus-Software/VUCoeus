package gov.grants.apply.valueObject;

import java.io.Serializable;

/**
 * This is a value object class of opportunity list information.
 * 
 * @author Feng Qian
 *
 */
public class GetOpportunityListInformation
    implements Serializable
{

	private String opportunity_id;
	private String opportunity_title;
	private String cfda_number;
	private String competition_id;
	private String schema_url;
	private String instruction_url;
	private String opening_date;
	private String closing_date;

	//====================================
	// Bean Getters
	//====================================
	/**
	 * @return the string of the opportunity ID.
	 */
	public String getOpportunity_id()
	{
		return opportunity_id;
	}
	/**
	 * @return the string of the opportunity title.
	 */
	public String getOpportunity_title()
	{
		return opportunity_title;
	}

	/**
	 * @return the string of the CFDA number.
	 */
	public String getCfda_number()
	{
		return cfda_number;
	}
	/**
	 * @return the string of the competition ID.
	 */
	public String getCompetition_id()
	{
		return competition_id;
	}
	/**
	 * @return the string of the schema url.
	 */
	public String getSchema_url()
	{
		return schema_url;
	}
	/**
	 * @return the string of the instruction url.
	 */
	public String getInstruction_url() {
		return instruction_url;
	}

	/**
	 * @return the string of the opening date.
	 */
	public String getOpening_date() {
		return opening_date;
	}

	/**
	 * @return the string of the closing date.
	 */
	public String getClosing_date() {
		return closing_date;
	}
	//=====================================
	// Bean Setters
	//=====================================
	/**
	 * @param value the string of the opportunity ID.
	 */

	public void setOpportunity_id(String value) {
		this.opportunity_id = value;
	}
	/**
	 * @param value the stirng of opportunity title.
	 */
	public void setOpportunity_title(String value) {
		this.opportunity_title = value;
	}
	/**
	 * @param value the string of the CFDA number.
	 */
	public void setCfda_number(String value) {
		this.cfda_number = value;
	}
	/**
	 * @param value the string of the competition ID.
	 */
	public void setCompetition_id(String value) {
		this.competition_id = value;
	}
	/**
	 * @param value the string of the schema url.
	 */
	public void setSchema_url(String value) {
		this.schema_url = value;
	}
	/**
	 * @param value the string of the instrucation url.
	 */
	public void setInstruction_url(String value) {
		this.instruction_url = value;
	}
	/**
	 * @param value the string of the opening date.
	 */
	public void setOpening_date(String value) {
		this.opening_date = value;
	}
	/**
	 * @param value the string of the closing date.
	 */
	public void setClosing_date(String value) {
		this.closing_date = value;
	}
}
