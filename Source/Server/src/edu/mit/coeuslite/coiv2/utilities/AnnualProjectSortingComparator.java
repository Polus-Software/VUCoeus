/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.utilities;

import edu.mit.coeuslite.coiv2.beans.CoiAnnualPersonProjectDetails;
import java.util.Comparator;

/**
 *
 * @author Mr Lijo
 */
public class AnnualProjectSortingComparator implements Comparator{

    public int compare(Object o1, Object o2) {

		CoiAnnualPersonProjectDetails second=(CoiAnnualPersonProjectDetails)o1;
		CoiAnnualPersonProjectDetails first=(CoiAnnualPersonProjectDetails)o2;
                 int value = first.getCoiProjectId().compareTo(second.getCoiProjectId());
		return value;
    }

}


