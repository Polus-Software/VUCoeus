/* 
 * @(#)ProposalPerDegreeFormBean.java 1.0 04/01/03 5:29 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.departmental.bean.DepartmentPerDegreeFormBean;
import java.beans.*;

/**
 * The class used to hold the information of <code>Proposal Degree</code>
 * which extends DepartmentPerDegreeFormBean.
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on April 1, 2003, 5:29 PM
 */

public class ProposalPerDegreeFormBean extends DepartmentPerDegreeFormBean
                                    implements java.io.Serializable {
          
    //holds the proposal number
    private String proposalNumber;                                  
    
    /**
     * Creates new ProposalPerDegreeFormBean
     */
    public ProposalPerDegreeFormBean(){
        
    }
    
    
    /** Copy Constructor to copy DepartmentPerDegreeFormBean */
    public ProposalPerDegreeFormBean(DepartmentPerDegreeFormBean departmentPerDegreeFormBean) {
        
        setPersonId(departmentPerDegreeFormBean.getPersonId());
        setDegreeCode(departmentPerDegreeFormBean.getDegreeCode());
        setGraduationDate(departmentPerDegreeFormBean.getGraduationDate());
        setDegree(departmentPerDegreeFormBean.getDegree());
        setFieldOfStudy(departmentPerDegreeFormBean.getFieldOfStudy());
        setSpecialization(departmentPerDegreeFormBean.getSpecialization());
        setSchool(departmentPerDegreeFormBean.getSchool());
        setSchoolIdCode(departmentPerDegreeFormBean.getSchoolIdCode());
        setSchoolId(departmentPerDegreeFormBean.getSchoolId());
        setDegreeDescription(departmentPerDegreeFormBean.getDegreeDescription());
        setSchoolDescription(departmentPerDegreeFormBean.getSchoolDescription());
        setAwDegreeCode(departmentPerDegreeFormBean.getAwDegreeCode());
        setAwDegree(departmentPerDegreeFormBean.getAwDegree());      
        setUpdateUser(departmentPerDegreeFormBean.getUpdateUser());
        setUpdateTimestamp(departmentPerDegreeFormBean.getUpdateTimestamp());
        setAcType(departmentPerDegreeFormBean.getAcType());

    }
    
    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
}
