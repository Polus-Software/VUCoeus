/*
 * CostElementsComparator.java
 *
 * Created on May 28, 2003, 10:13 PM
 */

package edu.mit.coeus.bean;

import java.util.*;
import edu.mit.coeus.customelements.bean.*;

/**
 *
 * @author  Raghunath
 */
public class CustomElementsComparator implements Comparator{
    
    public int compare( Object bean1, Object bean2 ){
        
        if( (bean1 != null ) && ( bean2 != null ) ){
            
            if( ( bean1 instanceof CustomElementsInfoBean )
                    && ( bean2 instanceof CustomElementsInfoBean ) ) {
                        
                CustomElementsInfoBean firstBean, secondBean;
                firstBean = ( CustomElementsInfoBean ) bean1;
                secondBean = ( CustomElementsInfoBean ) bean2;
                
                return firstBean.getColumnName().compareToIgnoreCase(
                    secondBean.getColumnName());
            }
        }
        return 0;
    }
}
