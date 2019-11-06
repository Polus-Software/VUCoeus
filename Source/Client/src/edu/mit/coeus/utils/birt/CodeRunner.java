/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.utils.birt;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author sharathk
 */
public class CodeRunner {

    private String line[];
    private Map<String, Object> objects = new HashMap<String, Object>();
    private Map<String, Class> objectTypes = new HashMap<String, Class>();
    private String strValue;

    public void analyzeAndRun(String code, Object internalFrame) throws Exception {
        Controller controller = CoeusGuiConstants.getMDIForm().getController(internalFrame);
        if(internalFrame != null) objects.put("gui", internalFrame);
        if (controller != null) {
            objects.put("controller", controller);
            objects.put("this", controller);
        } else if(internalFrame != null){
            objects.put("this", internalFrame);
        }

        String strLines[] = code.split(";");

        for (int index = 0; index < strLines.length; index++) {
            analyzeLine(strLines[index].replaceAll(",", ", "));
        }
    }

    public String getValue() {
        return strValue;
    }

    public Object getValue(String parameterName) {
        if (objects.containsKey(parameterName)) {
            return objects.get(parameterName);
        } else {
            return null;
        }
    }

    private void analyzeLine(String line) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, CoeusException {
        if (line.trim().length() == 0) {
            return;
        }
        StringTokenizer tokenizer = new StringTokenizer(line);

        List<String> lstTokens = new ArrayList<String>();
        String token, callReturnHolder = null;
        int index = 0, eqIndex = -1;
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            if (token.equalsIgnoreCase("=")) {
                //populate object types before = sign
                eqIndex = index;
                callReturnHolder = lstTokens.get(lstTokens.size() - 1);
                if ((lstTokens.size() - 2) >= 0) {
                    objectTypes.put(callReturnHolder, getClassType(lstTokens.get(lstTokens.size() - 2)));
                }
            }
            lstTokens.add(token);
            //System.out.println(token);
            index++;
        }

        token = lstTokens.get(eqIndex + 1);
        int paramBeginIndex = token.indexOf("(");
        int dotIndex = -1;
        if (paramBeginIndex != -1) {
            dotIndex = token.substring(0, paramBeginIndex).lastIndexOf(".");
        }

        String instanceName, methodName;
        if (dotIndex == -1 && paramBeginIndex == -1) {
            //Variable assignment
            Object value = null;
            try {
                value = getVariableValue(token, objectTypes.get(callReturnHolder));
                if (value == null) {
                    value = getInstance(token);
                }
            } catch (NumberFormatException nfe) {
                value = getInstance(token);
            }
            objects.put(callReturnHolder, value);
            strValue = value == null ? null : value.toString();
            return;
        } else if (dotIndex != -1) {
            instanceName = token.substring(0, dotIndex);
            Object instance = getInstance(instanceName);
            methodName = token.substring(dotIndex + 1, paramBeginIndex);
        } else {
            //No instance
            instanceName = "this";
            methodName = token.substring(0, paramBeginIndex);
        }

        String parameters[];

        if (token.indexOf(")") != -1) {
            //No Parameters or one parameter
            if (token.indexOf(")") - token.indexOf("(") > 1) {
                //One parameter
                parameters = new String[1];
                parameters[0] = token.substring(token.indexOf("(") + 1, token.indexOf(")"));
            } else {
                parameters = null;
            }
        } else {
            parameters = new String[lstTokens.size() - (eqIndex + 1)];
            int paramIndex = 0;
            for (index = eqIndex + 1; index < lstTokens.size(); index++) {
                token = lstTokens.get(index);
                if (paramIndex == 0) {
                    parameters[paramIndex] = token.substring(paramBeginIndex + 1, token.length() - 1);
                } else {
                    parameters[paramIndex] = token.substring(0, token.length() - 1);
                }
                paramIndex++;
            }
        }//end else
        try {
            //Call method on this instance
            Object value = callMethod(instanceName, methodName, parameters);
            if (callReturnHolder != null) {
                objects.put(callReturnHolder, value);
                if (!objectTypes.containsKey(callReturnHolder)) {
                    objectTypes.put(callReturnHolder, value.getClass());
                }
            }
            strValue = value == null ? null : value.toString();

            boolean method = methodName.equals("getSelectedRow");
            boolean variable = ((callReturnHolder != null && callReturnHolder.equalsIgnoreCase("row")) ||
                    (callReturnHolder != null && callReturnHolder.equalsIgnoreCase("selectedRow")));
            boolean val = strValue != null && strValue.equals("-1");
            //since no rows was selected in the table
            if ((method || variable) && val) {
                throw new CoeusException(CoeusMessageResources.getInstance().parseMessageKey("search_exceptionCode.1119"));
            }
        } catch (InvocationTargetException ex) {
            if (methodName.equals("getSelectedRow") || (callReturnHolder != null && (callReturnHolder.equalsIgnoreCase("row") || callReturnHolder.equalsIgnoreCase("selectedRow")))) {
                //this exception is thrown since no rows was selected in the table
                throw new CoeusException(CoeusMessageResources.getInstance().parseMessageKey("search_exceptionCode.1119"));
            } else {
                throw ex;
            }
        }
    }

    private Object callMethod(String instanceName, String methodName, String params[]) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String token;
        int paramsLength = params == null ? 0 : params.length;
        Class paramClass[] = new Class[paramsLength];
        Object paramObjects[] = new Object[paramsLength];

        for (int index = 0; index < paramsLength; index++) {
            if (params[index].startsWith("\"") && params[index].endsWith("\"")) {
                //String param
                //Remove Quotes from String. i.e. hold the String value without quotes
                paramObjects[index] = params[index].substring(1, params[index].length() - 1);
                paramClass[index] = String.class;
            } else {
                paramObjects[index] = getInstance(params[index]);
                if (params[index].indexOf(".") != -1) {
                    params[index] = params[index].substring(params[index].lastIndexOf(".") + 1);
                }
                paramClass[index] = objectTypes.get(params[index]);
            }
        }

        Object instance = getInstance(instanceName);
        Method method = findMethod(instance.getClass(), methodName, paramClass);//instance.getClass().getDeclaredMethod(methodName, paramClass);
        method.setAccessible(true);
        Object value = method.invoke(instance, paramObjects);
        return value;
    }

    private Class getClassType(String classType) throws ClassNotFoundException {
        Class retClass = null;
        if (classType.equalsIgnoreCase("int")) {
            retClass = int.class;
        } else if (classType.equalsIgnoreCase("char")) {
            retClass = char.class;
        } else if (classType.equalsIgnoreCase("float")) {
            retClass = float.class;
        } else if (classType.equalsIgnoreCase("double")) {
            retClass = double.class;
        } else if (classType.equalsIgnoreCase("String")) {
            retClass = String.class;
        } else if (classType.equalsIgnoreCase("boolean")) {
            retClass = boolean.class;
        } else if (objects.containsKey(classType)) {
            retClass = objects.get(classType).getClass();
        }
        return retClass;
    }

    private Object getInstance(String strInstances) throws NoSuchFieldException, IllegalAccessException {
        Object instance = null;
        if (strInstances.indexOf(".") == -1) {
            instance = getinstance(strInstances, null);
        } else {
            String strInstance[] = strInstances.split(".");
            StringTokenizer tokenizer = new StringTokenizer(strInstances, ".");
            int index = 0;
            String token = null, prevToken = null;
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                if (index == 0) {
                    instance = getinstance(token, null);
                } else {
                    instance = getinstance(token, objects.get(prevToken));
                }
                index++;
                prevToken = token;
            }
        }
        return instance;
    }

    private Object getinstance(String instanceName, Object fromInstance) throws NoSuchFieldException, IllegalAccessException {
        Object instance = null;
        if (fromInstance == null) {
            //use this
            fromInstance = objects.get("this");
        }
        //if (!instanceName.equalsIgnoreCase("this") && fromInstance == null) {
        //    Field field = this.getClass().getDeclaredField(instanceName);
        //    field.setAccessible(true);
        //    instance = field.get(this);
        //} else
        if (objects.containsKey(instanceName)) {
            instance = objects.get(instanceName);
        } else if (!instanceName.equalsIgnoreCase("this")) {
            Field field = findField(fromInstance.getClass(), instanceName);//fromInstance.getClass().getDeclaredField(instanceName);
            field.setAccessible(true);
            instance = field.get(fromInstance);
            objects.put(instanceName, instance);
            objectTypes.put(instanceName, field.getType());
        } else {
            //instance = this;
            instance = objects.get("this");
        }
        return instance;
    }

    private Method findMethod(Class objClass, String methodName, Class paramClass[]) throws NoSuchMethodException {
        Method method = null;
        try {
            method = objClass.getDeclaredMethod(methodName, paramClass);
        } catch (NoSuchMethodException nsme) {
            try {
                method = objClass.getMethod(methodName, paramClass);
            } catch (NoSuchMethodException nsme2) {
                //Search
                method = findMethod(objClass.getDeclaredMethods(), methodName, paramClass);
                if (method == null) {
                    method = findMethod(objClass.getMethods(), methodName, paramClass);
                }
            }
        }
        return method;
    }

    private Method findMethod(Method methods[], String methodName, Class paramClass[]) {
        Method method = null;
        for (int index = 0; index < methods.length; index++) {
            if (methods[index].getName().equals(methodName) &&
                    methods[index].getParameterTypes().length == paramClass.length) {
                //probable method. check for types
                Class classTypes[] = methods[index].getParameterTypes();
                for (int types = 0; types < classTypes.length; types++) {
                    try {
                        paramClass[types].asSubclass(classTypes[types]);
                    } catch (ClassCastException cce) {
                        break;
                    }

                }//End For
                method = methods[index];
                break;
            }//End Method compare
        }//End For
        return method;
    }

    private Field findField(Class objClass, String fieldName) throws NoSuchFieldException {
        Field field = null;
        try {
            field = objClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException nsfe) {
            field = objClass.getField(fieldName);
        }
        return field;
    }

    private Object getVariableValue(String value, Class classType) {
        Object object = null;
        if (classType.equals(int.class)) {
            object = Integer.parseInt(value);
        } else if (classType.equals(char.class) && value.startsWith("'") && value.endsWith("'") && value.trim().length() == 3) {
            object = new Character(value.trim().charAt(1));
        } else if (classType.equals(float.class)) {
            object = Float.parseFloat(value);
        } else if (classType.equals(double.class)) {
            object = Double.parseDouble(value);
        } else if (classType.equals(String.class) && value.startsWith("\"") && value.endsWith("\"")) {
            object = new String(value.substring(1, value.length() - 1));
        } else if (classType.equals(boolean.class) && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))) {
            object = Boolean.parseBoolean(value);
        }
        return object;
    }
}
