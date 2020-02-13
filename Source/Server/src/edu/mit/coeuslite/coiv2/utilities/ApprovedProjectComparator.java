/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.utilities;

import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import java.util.Comparator;

/**
 *
 * @author twinkle
 */
public class ApprovedProjectComparator implements Comparator{


    public int compare(Object o1, Object o2) {

		CoiDisclosureBean second=(CoiDisclosureBean)o1;
		CoiDisclosureBean first=(CoiDisclosureBean)o2;
                 int value = first.getUpdateTimestamp().compareTo(second.getUpdateTimestamp());
		return value;
    }

}


