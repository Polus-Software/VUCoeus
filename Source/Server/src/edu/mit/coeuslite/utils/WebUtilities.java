/*
 * WebIndicator.java
 *
 * Created on May 17, 2005, 3:29 PM
 */

package edu.mit.coeuslite.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author  chandrashekara
 */
public class WebUtilities {
    private static final String EMPTY_STRING = "";
    /** Creates a new instance of WebIndicator */
    public WebUtilities() {
    }
    
    /** Check the indicator.
     */
    public Map processIndicator(Map map,String indicatorKey, boolean allDeleted){
        Set keys =  map.keySet();
        String value = EMPTY_STRING;
            value = (String)map.get(indicatorKey);
          if(allDeleted){
              value = "N1";
          }else {
              value = "P1";
          }
        map.put(indicatorKey,value);  
        return map;
}
}
