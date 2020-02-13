/*
 * SearchColumnIndex.java
 *
 * Created on August 28, 2006, 2:28 PM
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.search.bean.DisplayBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.gui.CoeusSearch;
import java.util.Vector;

/**
 *
 * @author  chandrashekara
 */
public class SearchColumnIndex {
    
    /** Creates a new instance of SearchColumnIndex */
    public SearchColumnIndex() {
    }
    
    /** get the column index for the given search with the column name
     *index. get the column index by using DisplayBean
     *@param CoeusSearch, columnName
     *@return column Index
     */
    public int getSearchColumnIndex(CoeusSearch search, String indexName){
        int columnIndex= -1;
        try{
         SearchInfoHolderBean holderBean = search.getSearchInfoHolderBean();
            Vector vecDisplayData = holderBean.getDisplayList();
            if(vecDisplayData!= null && vecDisplayData.size() > 0){
                for(int index = 0; index < vecDisplayData.size() ; index++){
                    DisplayBean bean = (DisplayBean)vecDisplayData.get(index);
                    if(bean.getName().equals(indexName)){
                        columnIndex = index;
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
            return columnIndex;
    }
    
}
