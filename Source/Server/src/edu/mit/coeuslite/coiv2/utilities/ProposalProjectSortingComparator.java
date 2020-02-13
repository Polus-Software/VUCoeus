/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.utilities;

import edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails;
import java.util.Comparator;

/**
 *
 * @author Mr Lijo
 */
public class ProposalProjectSortingComparator implements Comparator{

    public int compare(Object o1, Object o2) {

		CoiPersonProjectDetails second=(CoiPersonProjectDetails)o1;
		CoiPersonProjectDetails first=(CoiPersonProjectDetails)o2;
                 int value = first.getCoiProjectId().compareTo(second.getCoiProjectId());
		return value;
    }

}


