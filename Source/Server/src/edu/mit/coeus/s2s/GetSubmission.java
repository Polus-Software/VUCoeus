/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s;

import java.io.IOException;

import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.bean.SubmissionInfoBean;
import edu.mit.coeus.s2s.v2.GetSubmissionV2;
import edu.mit.coeus.utils.UtilFactory;
/**
 *
 * @author farsana
 */
public abstract class GetSubmission {
    /** Creates a new instance of GetApplication */
    protected GetSubmission(){
    }
    public static GetSubmission getInstance(){
    	try {
		return new GetSubmissionV2();
			
		} catch (Exception e) {
			UtilFactory.log(e.getMessage(),e,"SubmissionEngine", "getInstance");
			return new GetSubmissionV2();
		}

    }
    public abstract SubmissionInfoBean[] getSubmissionList(S2SHeader headerParam) throws Exception;
    
}
