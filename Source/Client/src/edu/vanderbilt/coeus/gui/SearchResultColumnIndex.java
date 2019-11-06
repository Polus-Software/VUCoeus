/*
 * SearchResultColumnIndex.java
 * 
 * Handler for search result column indexes
 * 
 * @version:	1.0
 * @author:		Jill McAfee, Vanderbilt University Office of Research
 * @created:	December 20, 2012
 */
package edu.vanderbilt.coeus.gui;

import java.io.InputStream;
import java.util.Properties;

public class SearchResultColumnIndex {
    private Properties properties;
    private static final String PROPERTIES_FILE_NAME = "SearchResultColumns.properties";
    private static final String SEARCH_RESULT_COLUMNS = 
    	"/edu/vanderbilt/coeus/resources/" + PROPERTIES_FILE_NAME;

    /**
     * Constructor
     */
    public SearchResultColumnIndex() {
        init();
    }

    /**
     * Create and load an Input Stream for reading the properties file.
     */
    private void init() {
    	try {
            InputStream inputStream = getClass().getResourceAsStream(SEARCH_RESULT_COLUMNS);  
            properties = new Properties();  
            properties.load(inputStream);  

        } catch (Exception fne) {
            System.err.println("Can't read the " + PROPERTIES_FILE_NAME + ". " +
                    "Make sure " + PROPERTIES_FILE_NAME + " is in the CLASSPATH.");
            System.err.println(fne);
        }
    }
    
    /**
     * This method is used to parse the message key.
     * @param messageKey String representing the key in properties file.
     * @return int column Index for value corresponding to the messageKey
     */
    public int parseMessageKey(String messageKey) {
        int columnIndex = -1;
        
        try {
        	columnIndex = Integer.parseInt(properties.getProperty(messageKey));
        } catch (Exception fne) {
            System.err.println("Cannot parse properties file.");		
        }
        
        return columnIndex;    
    }
    
    public static int getSearchResultColumnIndex(String columnName) {
    	SearchResultColumnIndex searchResultColumnIndex = new SearchResultColumnIndex();
    	int columnIndex = searchResultColumnIndex.parseMessageKey(columnName);
    	return columnIndex;
    }
}