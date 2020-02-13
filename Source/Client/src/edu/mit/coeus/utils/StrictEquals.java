/*
 * StrictEquals.java
 *
 * Created on October 29, 2003, 1:32 PM
 */

package edu.mit.coeus.utils;

/**
 *
 * @author  Vyjayanthi
 */
import java.lang.reflect.*;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.BaseBean;

public class StrictEquals {
    
    /** Creates a new instance of StrictEquals */
    public StrictEquals() {
    }
    
    /** Compares all the values in the CoeusBeans
     * Note: Both the beans should be of comparable type.
     * @param compareTo bean
     * @param compareWith bean
     * @return boolean indicating whether both the beans are the same or not.
     */
    public boolean compare(BaseBean compareTo, BaseBean compareWith){
        CoeusVector getMethods = new CoeusVector();
        Class compareToClass = compareTo.getClass();
        Class compareWithClass = compareWith.getClass();        
        if( compareToClass != compareWithClass ) return false;
        Method method = null;
        Method[] methods = compareTo.getClass().getMethods();
        try{
            for (int index = 0; index < methods.length ; index ++){
                if( methods[index].getName().startsWith("isLike")) continue;
                if( methods[index].getName().startsWith("getPrimary")) continue;
                if( methods[index].getName().startsWith("get") || 
                methods[index].getName().startsWith("is") ){
                    getMethods.addElement(methods[index]);
                }
            }
        }catch (SecurityException securityException) {
            securityException.printStackTrace();
        }

        if( getMethods != null && getMethods.size() > 0){
            try{
                for (int index = 0; index < getMethods.size() ; index ++){
                    method = (Method) getMethods.get(index);
                    Object dataCompareTo = method.invoke(compareTo, null);
                    Object dataCompareWith = method.invoke(compareWith, null);

                    if(dataCompareTo == null && dataCompareWith == null) continue;
                    if(dataCompareTo == null) dataCompareTo = "";
                    if(dataCompareWith == null) dataCompareWith = "";
                    /*
                     *Bug Fix #1813
                     *Comparing after removing the spaces, \n, \r chars
                     */
                    //BEGIN FIX
                    if(dataCompareTo instanceof String) 
                        dataCompareTo = dataCompareTo.toString().trim();
                    if(dataCompareWith instanceof String) 
                        dataCompareWith = dataCompareWith.toString().trim();
                    //END FIX
                    if( !( dataCompareTo.equals(dataCompareWith)) ) {
                        return false;
                    }
                }
            }catch (IllegalAccessException illegalAccessException){
                illegalAccessException.printStackTrace();
            }catch (IllegalArgumentException illegalArgumentException){
                illegalArgumentException.printStackTrace();
            }catch (InvocationTargetException invocationTargetException){
                invocationTargetException.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
