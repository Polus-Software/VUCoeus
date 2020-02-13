/*
 * Converter.java
 *
 * Created on August 22, 2006, 4:28 PM
 */

package edu.mit.coeus.ws.utils;

import edu.mit.coeus.utils.UtilFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author  geot
 */
public class Converter {
    
    /** Creates a new instance of Converter */
    public Converter() {
    }
    public static Object wrapBean(Class cl,Object getObj) throws Exception{
        Field[] flds = cl.getDeclaredFields();
        Method[] meths = cl.getMethods();
        Class[] params = new Class[1];
        Object setObj = cl.newInstance();
        for(int i=0;i<meths.length;i++){
            if(!meths[i].getName().startsWith("s")) continue;
//            params[i] = flds[i].getType();
            try{
                Method setMeth = meths[i];
                Class[] setParamTypes = setMeth.getParameterTypes();
                String getMethName = "g"+setMeth.getName().substring(1);
                Method getMeth = getObj.getClass().getMethod(getMethName, null);
                Object[] obj = new Object[1];
                
                if(getMeth.getReturnType().equals(char.class) && 
                    setParamTypes[0].equals(String.class)){
                    Object tObj = getMeth.invoke(getObj, null);
                    obj[0] = ""+tObj;
                }else if((getMeth.getReturnType().equals(java.sql.Timestamp.class)||
                            getMeth.getReturnType().equals(java.sql.Date.class)) && 
                        setParamTypes[0].equals(java.util.Calendar.class)){
                    Object tObj = getMeth.invoke(getObj, null);
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()));
                    cal.setTimeInMillis(((java.sql.Timestamp)tObj).getTime());
                    obj[0] = cal;
                }else{
                    obj[0]=getMeth.invoke(getObj, null);
                }
                setMeth.invoke(setObj, obj);
            }catch(Exception ex){
                //do nothing... just copy whatever matches, its just a utility
//                ex.printStackTrace();
            }
        }
        return setObj;
    }
}
