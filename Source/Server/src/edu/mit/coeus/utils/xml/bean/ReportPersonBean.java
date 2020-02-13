/*
 * ReportPersonBean.java
 *
 * Created on October 16, 2003, 12:16 PM
 */

package edu.mit.coeus.utils.xml.bean;


public class ReportPersonBean extends edu.mit.coeus.irb.bean.PersonInfoFormBean
{
    private String middleName ;
    private String salutation ;
    private String degree ;
    private String school ;
    private String yearGraduated ;
    private String citizenship ;
    private String primaryTitle ;
    private String homeUnitName ;
    private String homeUnitNumber ;
    private String facultyFlag ;
    private String employeeFlag ;
    
    /** Creates a new instance of ReportPersonBean */
    public ReportPersonBean()
    {
    }

    /** Getter for property citizenship.
     * @return Value of property citizenship.
     */
    public java.lang.String getCitizenship()
    {
        return citizenship;
    }    
    
    /** Setter for property citizenship.
     * @param citizenship New value of property citizenship.
     */
    public void setCitizenship(java.lang.String citizenship)
    {
        this.citizenship = citizenship;
    }    
    
    /** Getter for property degree.
     * @return Value of property degree.
     */
    public java.lang.String getDegree()
    {
        return degree;
    }
    
    /** Setter for property degree.
     * @param degree New value of property degree.
     */
    public void setDegree(java.lang.String degree)
    {
        this.degree = degree;
    }
    
    /** Getter for property employeeFlag.
     * @return Value of property employeeFlag.
     */
    public java.lang.String getEmployeeFlag()
    {
        return employeeFlag;
    }
    
    /** Setter for property employeeFlag.
     * @param employeeFlag New value of property employeeFlag.
     */
    public void setEmployeeFlag(java.lang.String employeeFlag)
    {
        this.employeeFlag = employeeFlag;
    }
    
    /** Getter for property facultyFlag.
     * @return Value of property facultyFlag.
     */
    public java.lang.String getFacultyFlag()
    {
        return facultyFlag;
    }
    
    /** Setter for property facultyFlag.
     * @param facultyFlag New value of property facultyFlag.
     */
    public void setFacultyFlag(java.lang.String facultyFlag)
    {
        this.facultyFlag = facultyFlag;
    }
    
    /** Getter for property homeUnitName.
     * @return Value of property homeUnitName.
     */
    public java.lang.String getHomeUnitName()
    {
        return homeUnitName;
    }
    
    /** Setter for property homeUnitName.
     * @param homeUnitName New value of property homeUnitName.
     */
    public void setHomeUnitName(java.lang.String homeUnitName)
    {
        this.homeUnitName = homeUnitName;
    }
    
    /** Getter for property homeUnitNumber.
     * @return Value of property homeUnitNumber.
     */
    public java.lang.String getHomeUnitNumber()
    {
        return homeUnitNumber;
    }
    
    /** Setter for property homeUnitNumber.
     * @param homeUnitNumber New value of property homeUnitNumber.
     */
    public void setHomeUnitNumber(java.lang.String homeUnitNumber)
    {
        this.homeUnitNumber = homeUnitNumber;
    }
    
    /** Getter for property middleName.
     * @return Value of property middleName.
     */
    public java.lang.String getMiddleName()
    {
        return middleName;
    }
    
    /** Setter for property middleName.
     * @param middleName New value of property middleName.
     */
    public void setMiddleName(java.lang.String middleName)
    {
        this.middleName = middleName;
    }
    
    /** Getter for property primaryTitle.
     * @return Value of property primaryTitle.
     */
    public java.lang.String getPrimaryTitle()
    {
        return primaryTitle;
    }
    
    /** Setter for property primaryTitle.
     * @param primaryTitle New value of property primaryTitle.
     */
    public void setPrimaryTitle(java.lang.String primaryTitle)
    {
        this.primaryTitle = primaryTitle;
    }
    
    /** Getter for property salutation.
     * @return Value of property salutation.
     */
    public java.lang.String getSalutation()
    {
        return salutation;
    }
    
    /** Setter for property salutation.
     * @param salutation New value of property salutation.
     */
    public void setSalutation(java.lang.String salutation)
    {
        this.salutation = salutation;
    }
    
    /** Getter for property school.
     * @return Value of property school.
     */
    public java.lang.String getSchool()
    {
        return school;
    }
    
    /** Setter for property school.
     * @param school New value of property school.
     */ 
    public void setSchool(java.lang.String school)
    {
        this.school = school;
    }
    
    /** Getter for property yearGraduated.
     * @return Value of property yearGraduated.
     */
    public java.lang.String getYearGraduated()
    {
        return yearGraduated;
    }
    
    /** Setter for property yearGraduated.
     * @param yearGraduated New value of property yearGraduated.
     */
    public void setYearGraduated(java.lang.String yearGraduated)
    {
        this.yearGraduated = yearGraduated;
    }
    
}
