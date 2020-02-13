package edu.vanderbilt.coeus.utils;

import java.util.*;

public class CoeusComparator {

    public static Map sortByComparator(Map unsortMap) {
   	 
        List list = new LinkedList(unsortMap.entrySet());
 
        //sort list based on comparator
        Collections.sort(list, new Comparator() {
             public int compare(Object o1, Object o2) {
	           return ((Comparable) ((Map.Entry) (o1)).getValue())
	           .compareTo(((Map.Entry) (o2)).getValue());
             }
        });
        // reverse the order
        Collections.reverse(list);
 
        //put sorted list into map again
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
		     Map.Entry entry = (Map.Entry)it.next();
		     sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
    }	
	
}
