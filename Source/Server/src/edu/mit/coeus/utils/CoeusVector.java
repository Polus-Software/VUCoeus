/*
 * @(#)ProtocolDataTxnBean.java 1.0 10/24/02 10:37 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * CoeusVector.java
 *
 * Created on September 30, 2003, 12:55 PM
 */

/* Case#2835 - unused fields in CoeusVector
 * PMD check performed, and commented unused imports and variables on 09-MAY-2008
 * printStackTrace code commented
 * by Noorul
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.bean.CoeusBean;
import java.util.Vector;
import java.lang.reflect.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.utils.query.*;
import java.math.BigDecimal;
import java.math.BigInteger;
//Unused imports commented for Case#2835 - unused fields in CoeusVector - starts
//import java.text.Collator;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//Unused imports commented for Case#2835 - unused fields in CoeusVector - ends

/** This class extends Vector and is used in Budget module to compare two beans
 * of similar type. It contains methods to sort and filter.
 * @author Prasanna Kumar K
 */

    

public class CoeusVector extends Vector{
    
    //Unused code commented for Case#2835 - unused fields in CoeusVector - starts
//    private Collator collator;
//    private List sortingColumns;
//    private transient DateUtils dtUtils = new DateUtils();
//    private java.text.SimpleDateFormat dtFormat
//        = new java.text.SimpleDateFormat("MM/dd/yyyy");
//    private boolean sortOrder = true;
    //Unused code commented for Case#2835 - unused fields in CoeusVector - ends
    /** Operator which filters active beans(i.e not deleted beans) */    
    public static Operator FILTER_ACTIVE_BEANS = new Or(new Equals("acType", null),new NotEquals("acType",TypeConstants.DELETE_RECORD));

    /** returns CoeusVector containing all ComparableBean instances like queryBean.
     * @param queryBean bean which is being querried.
     * @throws CoeusException if cannot call isLike
     * @return CoeusVector containing all ComparableBean instances like queryBean.
     */
    public CoeusVector isLike(ComparableBean queryBean)throws CoeusException{
        CoeusVector queryResult = new CoeusVector();
        ComparableBean  comparableBean = null;
        for(int element = 0; element < this.size() ; element++){
            comparableBean = (ComparableBean)this.elementAt(element);
            if(comparableBean == null) {
                continue;
            }
            if(comparableBean.isLike(queryBean)){
                queryResult.addElement(comparableBean);
            }
        }
        return queryResult;
    }
    
    /** returns CoeusVector containing all ComparableBean instances not like queryBean.
     * @param queryBean bean which is being querried.
     * @throws CoeusException if cannot call isLike
     * @return CoeusVector containing all ComparableBean instances not like queryBean.
     */    
    public CoeusVector isNotLike(ComparableBean queryBean)throws CoeusException{
        CoeusVector queryResult = new CoeusVector();
        ComparableBean  comparableBean = null;
        for(int element = 0; element < this.size() ; element++){
            comparableBean = (ComparableBean)this.elementAt(element);
            if(comparableBean == null) {
                continue;
            }
            if(! comparableBean.isLike(queryBean)){
                queryResult.addElement(comparableBean);
            }
        }
        return queryResult;
    }
    
    /** Overridden method for sorting. By default it will sort in ascending order.
     * sorts the CoeusVector by the fieldName in ascending or descending order.
     * Note: the field Object should be of Comparable type.
     * @param fieldName field which is used to sort the bean.
     * @return boolean indicating whether the sort is completed successfully or not.
     */
    public boolean sort(String fieldName) {
        return sort(fieldName, true);
    }
    
    /**
     * sorts the CoeusVector by the fieldName in ascending or descending order.
     * Note: the field Objects should be of Comparable type.
     * @param fieldName field which is used to sort the bean.
     * @param ascending if true sorting is done in ascending order,
     * else sorting is done in descending order.
     * @return boolean indicating whether the sort is completed successfully or not.
     */    
    public boolean sort(String fieldName, boolean ascending) {
        return sort(fieldName, ascending, false);
    }
    
    /**
     * sorts the CoeusVector by the fieldName in ascending or descending order.
     * Note: the field Object should be of Comparable type.
     * @return boolean indicating whether the sort is completed successfully or not.
     * @param ignoreCase use only when comparing strings. as default implementation uses case sensitive comparison.
     * @param fieldName field which is used to sort the bean.
     * @param ascending if true sorting is done in ascending order,
     * else sorting is done in descending order.
     */
    public boolean sort(String fieldName, boolean ascending, boolean ignoreCase) {
        BaseBean current, next;
        int compareValue = 0;
        Field field = null;
        Method method = null;
        if (this.size() == 0) {
            return false;
        }
        Class dataClass = get(0).getClass();
        
        try{
            field = dataClass.getDeclaredField(fieldName);
            if(! field.isAccessible()) {
                //String methodName = "get" + (fieldName.charAt(0)+"").toUpperCase()+ fieldName.substring(1);
                //System.out.println(methodName);
                //method = dataClass.getMethod(methodName, null);
                throw new NoSuchFieldException();
            }
        }catch (NoSuchFieldException noSuchFieldException) {
            //noSuchFieldException.printStackTrace();
            //field not available. Use method invokation.
            try{
                String methodName = "get" + (fieldName.charAt(0)+"").toUpperCase()+ fieldName.substring(1);
                //System.out.println(methodName);
                method = dataClass.getMethod(methodName, null);
            }catch (NoSuchMethodException noSuchMethodException) {
                //noSuchMethodException.printStackTrace();
                return false;
            }
        }
        
        for(int index = 0; index < size()-1; index++) {
            for(int nextIndex = index+1; nextIndex < size(); nextIndex++){
                current  = (BaseBean)get(index);
                next = (BaseBean)get(nextIndex);
                //Check if current and next implements Comparable else can't compare.
                //so return without comparing.May be we can have an exception for this purpose.
                try{
                    if(field != null && field.isAccessible()) {
                        Comparable thisObj = (Comparable)field.get(current);
                        Comparable otherObj = (Comparable)field.get(next);
                        if (thisObj == null) {
                            compareValue = -1;
                        } else if (otherObj == null) {
                            compareValue = 1;
                        } else {
                            if(thisObj instanceof String && ignoreCase) {
                                compareValue = ((String)thisObj).compareToIgnoreCase((String)otherObj);
                            }else {
                                compareValue = thisObj.compareTo(otherObj);
                            }
                        }
                    }
                    else{
                        //Object obj1 = method.invoke(current, null);
                        //Object obj2 = method.invoke(next, null);
                        
                        Comparable thisObj = (Comparable)method.invoke(current, null);
                        Comparable otherObj = (Comparable)method.invoke(next, null);
                        if (thisObj == null) {
                            compareValue = -1;
                        } else if (otherObj == null) {
                            compareValue = 1;
                        } else {
                            if(thisObj instanceof String && ignoreCase) {
                                compareValue = ((String)thisObj).compareToIgnoreCase((String)otherObj);
                            }else {
                                compareValue = thisObj.compareTo(otherObj);
                            }
                        }
                        //compareValue = ((Comparable)method.invoke(current, null)).compareTo((Comparable)method.invoke(next, null));
                    }
                }catch (IllegalAccessException illegalAccessException) {
                    //illegalAccessException.printStackTrace();
                    return false;
                }catch (InvocationTargetException invocationTargetException) {
                    //invocationTargetException.printStackTrace();
                    return false;
                }
                
                if(ascending && compareValue > 0) {
                    BaseBean temp = (BaseBean)get(index);
                    set(index, get(nextIndex));
                    set(nextIndex, temp);
                }else if(! ascending && compareValue < 0) {
                    BaseBean temp = (BaseBean)get(index);
                    set(index, get(nextIndex));
                    set(nextIndex, temp);
                }
                
            }//End For - Inner
        }//End For - Outer
        return true; //sort completed successfully
    }//End Sort
    
       
    /** sorts the CoeusVector by the fieldNames in ascending or descending order.
     * Note: the field Objects should be of Comparable type.
     * @param fieldNames fields which is used to sort the bean.
     * @param ascending if true sorting is done in ascending order,
     * else sorting is done in descending order.
     * @return boolean indicating whether the sort is completed successfully or not.
     * Modified code for the better performance and proper sorting.
     *Added by Sharath and Nadh
     */    
    public boolean sort(String fieldNames[], boolean ascending) {
        boolean[] asce = new boolean[1];
        asce[0] = ascending;
        return sort(fieldNames, asce);
    }
    
    /** sorts the CoeusVector by the fieldNames in ascending or descending order.
     * Note: the field Objects should be of Comparable type.
     * @param fieldNames fields which is used to sort the bean.
     * @param ascending if true sorting is done in ascending order,
     * else sorting is done in descending order.
     * @return boolean indicating whether the sort is completed successfully or not.
     * Modified code for the better performance and proper sorting.
     *Added by Sharath and Nadh
     */    
    public boolean sort(String fieldNames[], boolean ascending[]) {
        int index, nextIndex;
        BaseBean current, other;
        //Unused code commented for Case#2835 - unused fields in CoeusVector
//        boolean swap, lastColumnEquals;
        boolean swap;
        Operator operator;
        
        for (index=1; index < size(); index++) {
            current = (BaseBean)get(index);
            nextIndex = index;
            
            Operator relOperator[] = getRelationalOperators(fieldNames, current, ascending);
            Operator eqOperator[] = getEqualsOperators(fieldNames, current);
            
            while(nextIndex > 0) {
                other = (BaseBean)get(nextIndex-1);
                swap = false;
                for(int operatorIndex = 0; operatorIndex < relOperator.length; operatorIndex++) {
                    operator = relOperator[operatorIndex];
                    if(operator.getResult(other)) {
                        swap = true;
                        break;
                    }else {
                        //check with equals
                        operator = eqOperator[operatorIndex];
                        if(operator.getResult(other)) {
                            continue;
                        }else {
                            break;
                        }
                    }
                }//End For
                
                if(swap) { 
                    set(nextIndex, get(nextIndex-1));
                    nextIndex = nextIndex - 1;
                }else {
                    break;
                }//End if-else
            }//End while
            set(nextIndex, current);
        }
        return true;
    }
    
    
    /** For a given fields, it will compare the values in the value objects
     * and returns an array of relational operator for the given fields
     */
    private Operator[] getRelationalOperators(String field[], BaseBean baseBean, boolean ascending[]) {
        //Make all Relational Operators.
        RelationalOperator relationalOperator[] = new RelationalOperator[field.length];
        boolean isascending = !ascending[0];
        for(int index = 0; index < field.length; index++) {
            if(ascending.length>1)
                isascending = !ascending[index];
            
            if(! isascending) {
                GreaterThan gt = new GreaterThan(field[index], (Comparable)getFieldValue(field[index], baseBean));
                relationalOperator[index] = gt;
            }else if(isascending) {
                LesserThan lt = new LesserThan(field[index], (Comparable)getFieldValue(field[index], baseBean));
                relationalOperator[index] = lt;
            }
        }
        return relationalOperator;
    }
    /** For a given fields, it will compare the values in the value objects
     * and returns an array of equal operator for the given fields
     */
    private Operator[] getEqualsOperators(String field[], BaseBean baseBean) {
        //Make all Equals Operators.
        Equals equals[] = new Equals[field.length];
            for(int index = 0; index < equals.length; index++) {
                Equals eq = new Equals(field[index], (Comparable)getFieldValue(field[index], baseBean));
                equals[index] = eq;
            }
        return equals;
    }
    
    /** filters coeusvector which contains only those beans which satisfies the operator condition.
     * @param operator Operator.
     * @return coeusvector which contains only those beans which satisfies the operator condition.
     */
    public CoeusVector filter(Operator operator) {
        CoeusVector filterResult = new CoeusVector();
        if (this.size() > 0) {
            BaseBean  baseBean = null;
            for(int index = 0; index < this.size() ; index++){
                baseBean = (BaseBean)this.elementAt(index);
                if(operator.getResult(baseBean) == true){
                    filterResult.addElement(baseBean);
                }
            }
        }
        return filterResult;
    }
    
    /** calculates the sum of the field in this CoeusVector.
     * @param fieldName field of bean whose sum has to be calculated.
     * @return sum.
     */    
    public double sum(String fieldName) {
        return sum(fieldName, null, null);
    }//End Sum

    /** calculates the sum of the Objects of the specified field in this CoeusVector.
     * @param fieldName field of bean whose sum has to be calculated.
     * @return sum as BigDecimal Object.
     */    
    public BigDecimal sumObjects(String fieldName) {
        return new BigDecimal(sum(fieldName, null, null));
    }//End Sum
    
    /** calculates the sum of the field in this CoeusVector.
     * @param fieldName field of bean whose sum has to be calculated.
     * @param arg argument for the getter method of field if it takes any argumnt,
     * else can be null.
     * @param value value for the argument, else can be null.
     * @return returns sum.
     */    
    public double sum(String fieldName, Class arg, Object value) {
        if(size() == 0) {
            return 0;
        }
        
        BaseBean current;
        Field field = null;
        Method method = null;
        Class dataClass = get(0).getClass();
        double sum = 0;
        
        try{
            field = dataClass.getDeclaredField(fieldName);
            
            Class fieldClass = field.getType();
            String fieldTypeName = fieldClass.getName();
            if(! (fieldClass.equals(Integer.class) ||
            fieldClass.equals(Long.class) ||
            fieldClass.equals(Double.class) ||
            fieldClass.equals(Float.class) ||
            fieldClass.equals(BigDecimal.class) ||
            fieldClass.equals(BigInteger.class) ||
            fieldTypeName.equalsIgnoreCase("int") ||
            fieldTypeName.equalsIgnoreCase("long") ||
            fieldTypeName.equalsIgnoreCase("float") ||
            fieldTypeName.equalsIgnoreCase("double") )) {
                throw new UnsupportedOperationException("Data Type not numeric");
            }
            
            if(! field.isAccessible()) {
                throw new NoSuchFieldException();
            }
        }catch (NoSuchFieldException noSuchFieldException) {
            try{
                String methodName = "get" + (fieldName.charAt(0)+"").toUpperCase()+ fieldName.substring(1);
                if(arg != null) {
                    Class args[]  = {arg};
                    method = dataClass.getMethod(methodName, args);
                }else {
                    method = dataClass.getMethod(methodName, null);
                }
            }catch (NoSuchMethodException noSuchMethodException) {
                //noSuchMethodException.printStackTrace();
            }
        }
        
        
        
        for(int index = 0; index < size(); index++) {
            current  = (BaseBean)get(index);
            
            try{
                if(field != null && field.isAccessible()) {
                    sum = sum + Double.parseDouble(((Comparable)field.get(current)).toString());
                }
                else{
                    if(value != null) {
                        Object values[] = {value};
                        sum = sum + Double.parseDouble(((Comparable)method.invoke(current, values)).toString());
                    }else {
                        sum = sum + Double.parseDouble(((Comparable)method.invoke(current, null)).toString());
                    }
                    
                }
            }catch (IllegalAccessException illegalAccessException) {
                //illegalAccessException.printStackTrace();
            }catch (InvocationTargetException invocationTargetException) {
                //invocationTargetException.printStackTrace();
            }
        }
        return sum;
    }
    
    /** calculates the sum of the field in this CoeusVector.
     * @param fieldName field of bean whose sum has to be calculated.
     * @param operator to get filrtered vector on which sum will be called.
     * @return returns sum.
     */    
    public double sum(String fieldName, Operator operator) {
        return filter(operator).sum(fieldName);
    }
    
    /** returns the field value in the base bean for the specified field.
     * @param fieldName fieldname whose value has to be got.
     * @param baseBean Bean containing the field.
     * @return value of the field.
     */    
    private Object getFieldValue(String fieldName, BaseBean baseBean) {
        Field field = null;
        Method method = null;
        Class dataClass = baseBean.getClass();
        Object value = null;
        
        try{
            field = dataClass.getDeclaredField(fieldName);
            if(! field.isAccessible()) {
                throw new NoSuchFieldException();
            }
        }catch (NoSuchFieldException noSuchFieldException) {
            try{
                String methodName = "get" + (fieldName.charAt(0)+"").toUpperCase()+ fieldName.substring(1);
                method = dataClass.getMethod(methodName, null);
            }catch (NoSuchMethodException noSuchMethodException) {
                //noSuchMethodException.printStackTrace();
            }
        }
        
        try{
            if(field != null && field.isAccessible()) {
                value = field.get(baseBean);
            }
            else{
                value = method.invoke(baseBean, null);
            }
        }catch (IllegalAccessException illegalAccessException) {
            //illegalAccessException.printStackTrace();
        }catch (InvocationTargetException invocationTargetException) {
            //invocationTargetException.printStackTrace();
        }
        return value;
    }
    
    //Unused code commented for Case#2835 - unused fields in CoeusVector - starts
//    /** returns multiple field sort operator for the fields in the base bean in
//     * either ascending or descending order.
//     * @param field fields to sort.
//     * @param baseBean base bean containing fields.
//     * @param ascending sorting order.
//     * true - ascending
//     * false - descending
//     * @return multiple field sort Operator
//     */    
//    private Operator getMultipleFieldSortOperator(String field[], BaseBean baseBean, boolean ascending) {
//        int fields = field.length;
//        Or multipleFieldSortOperator;
//        
//        And andArray[] = new And[fields - 1];
//        
//        //Make all Relational Operators.
//        RelationalOperator relationalOperator[] = new RelationalOperator[fields];
//        for(int index = 0; index < fields; index++) {
//            if(! ascending) {
//                GreaterThan gt = new GreaterThan(field[index], (Comparable)getFieldValue(field[index], baseBean));
//                relationalOperator[index] = gt;
//            }else if(ascending) {
//                LesserThan lt = new LesserThan(field[index], (Comparable)getFieldValue(field[index], baseBean));
//                relationalOperator[index] = lt;
//            }
//        }
//        
//        //Generate Equals
//        for(int index = 1; index < fields; index++) {
//            Equals equals[] = new Equals[index];
//            for(int count = 0; count < index; count++) {
//                Equals eq = new Equals(field[count], (Comparable)getFieldValue(field[count], baseBean));
//                equals[count] = eq;
//            }
//            
//            //Combine Equals and make And Operators
//            And and = null;
//            boolean even = (equals.length % 2) == 0;
//            for(int count = 0; count < equals.length; count++) {
//                if(equals.length == 1) {
//                    and = new And(equals[count], relationalOperator[index]);
//                    andArray[index - 1] = and;
//                    break;
//                }else if(even && count == index - 1) {
//                    and = new And(and, relationalOperator[index]);
//                    andArray[index - 1] = and;
//                    break;
//                }else if(!even && count == index - 1) {
//                    and = new And(and, relationalOperator[index]);
//                    andArray[index - 1] = and;
//                    break;
//                }else {
//                    and = new And(equals[count], equals[count + 1]);
//                }
//                
//            }//End For count
//        }//End For index
//        
//        //Combine Greater Than and Equals and make Or operators
//        multipleFieldSortOperator = new Or(relationalOperator[0], andArray[0]);
//        for(int index = 1; index < andArray.length; index++) {
//            multipleFieldSortOperator = new Or(multipleFieldSortOperator, andArray[index]);
//        }
//        
//        return multipleFieldSortOperator;
//    }
    //Unused code commented for Case#2835 - unused fields in CoeusVector - ends
    
    /**
     *  Overridden method of super class, java.util.Vector.
     * @return string representation of this.
     */
    public String toString(){
        return super.toString();
    }
    
    /**
     * Method to update the bean property in the collection based on the Operator
     * @param beanClass Example : CoeusBean.Class
     * @param fieldName - this have to be the property name defined in the bean class
     * @param parameterType - DataType of the value parameter If int -- DataType.getClass(DataType.INT)
     * @param value - value to be updated to the property
     * @param whereCondition 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return 
     */
    public boolean setUpdate( Class beanClass, String fieldName, Class parameterType, Object value, Operator whereCondition) throws CoeusException {
        
        if(size() == 0) return false;
        
        CoeusBean current;
        Field field = null;
        Method method = null;
        Class dataClass = beanClass;
        
        try{
            field = dataClass.getField(fieldName);
            
            //Field Not Available So cannot Set Values. just return.
            if(field == null) throw new CoeusException("Field Not Available"); ;
            
            if(! field.isAccessible()) {
                throw new NoSuchFieldException();
            }
        }catch (NoSuchFieldException noSuchFieldException) {
            try{
                String methodName = "set" + (fieldName.charAt(0)+"").toUpperCase()+ fieldName.substring(1);
                Class args[] = {parameterType};
                method = dataClass.getMethod(methodName, args);
            }catch (NoSuchMethodException noSuchMethodException) {
                throw new CoeusException(noSuchMethodException.getMessage());
            }
        }
        
        Object parameters[] = {value};
        try{
            for(int index = 0; index < size(); index++) {
                current  = (CoeusBean)get(index);
                boolean canUpdateField = false;
                if(whereCondition == null){
                    canUpdateField = true;
                }else if(whereCondition.getResult(current) == true){
                        canUpdateField = true;    
                }
                
                if(canUpdateField){
                    if(field != null && field.isAccessible()) {
                        field.set(current, value);
                    } else{
                        method.invoke(current, parameters);
                    }
                    
                    set(index, current);
                }

            }
            return true;
        }catch (IllegalAccessException illegalAccessException) {
            throw new CoeusException(illegalAccessException.getMessage());
        }catch (InvocationTargetException invocationTargetException) {
            throw new CoeusException(invocationTargetException.getMessage());
        }
        
    }
    
    
    
}//End Class
