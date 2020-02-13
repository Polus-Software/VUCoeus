/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *
 * @author sharathk
 */
public class CoeusRequestWrapper extends HttpServletRequestWrapper {

    public CoeusRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String[] getParameterValues(String parameter) {

        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);

    }

    private String cleanXSS(String value){
        //value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        //value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        //value = value.replaceAll("'", "&#39;");
        //value = value.replaceAll("eval\\((.*)\\)", "");
        //value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        //value = value.replaceAll("script", "");
        
        StringBuffer sb = new StringBuffer(value);
        Pattern p1 = Pattern.compile("<\\S");
        Matcher m1 = p1.matcher(sb);
        int replace = 0;
        while (m1.find(replace)) {
            replace = m1.start();
            sb.replace(replace, replace + 1, "&lt;");
        }

        Pattern p2 = Pattern.compile("\\S>");
        Matcher m2 = p2.matcher(sb);
        replace = 0;
        while (m2.find(replace)) {
            replace = m2.start();
            sb.replace(replace + 1, replace + 2, "&gt;");
        }
        value = sb.toString();

        return value;
    }
}
