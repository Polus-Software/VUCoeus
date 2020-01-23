/*
 * CoeusReportGroupBean.java
 *
 * Created on December 19, 2005, 12:46 PM
 */

package edu.mit.coeus.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author  geot
 */
public class CoeusReportGroupBean implements Serializable{
    private String groupName;
    private String display;
    private String streamGenerator;
    private String jaxbPkgName;
    private Map reports;
    /** Creates a new instance of CoeusReportGroupBean */
    public CoeusReportGroupBean() {
    }
    public CoeusReportGroupBean(String name,String display) {
        this.groupName = name;
        this.display = display;
    }
    
    /**
     * Getter for property streamGenerator.
     * @return Value of property streamGenerator.
     */
    public java.lang.String getStreamGenerator() {
        return streamGenerator;
    }
    
    /**
     * Setter for property streamGenerator.
     * @param streamGenerator New value of property streamGenerator.
     */
    public void setStreamGenerator(java.lang.String streamGenerator) {
        this.streamGenerator = streamGenerator;
    }
    
    /**
     * Getter for property jaxbPkgName.
     * @return Value of property jaxbPkgName.
     */
    public java.lang.String getJaxbPkgName() {
        return jaxbPkgName;
    }
    
    /**
     * Setter for property jaxbPkgName.
     * @param jaxbPkgName New value of property jaxbPkgName.
     */
    public void setJaxbPkgName(java.lang.String jaxbPkgName) {
        this.jaxbPkgName = jaxbPkgName;
    }
    
    /**
     * Getter for property groupName.
     * @return Value of property groupName.
     */
    public java.lang.String getGroupName() {
        return groupName;
    }
    
    /**
     * Setter for property groupName.
     * @param groupName New value of property groupName.
     */
    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }
    
    /**
     * Getter for property display.
     * @return Value of property display.
     */
    public java.lang.String getDisplay() {
        return display;
    }
    
    /**
     * Setter for property display.
     * @param display New value of property display.
     */
    public void setDisplay(java.lang.String display) {
        this.display = display;
    }
    
    /**
     * Getter for property reports.
     * @return Value of property reports.
     */
    public java.util.Map getReports() {
        return reports;
    }
    
    /**
     * Setter for property reports.
     * @param reports New value of property reports.
     */
    public void setReports(java.util.Map reports) {
        this.reports = reports;
    }
    
    /**
     * add report.
     * @param reports New value of property reports.
     */
    public void addReport(String id,String dispVal,String sg,String templ,String pkg,String multiple,String footer) {
        Report rep = new Report();
        rep.setId(id);
        rep.setJaxbpkgname(pkg==null||pkg.equals("")?this.jaxbPkgName:pkg);
        rep.setStreamgenerator(sg==null||sg.equals("")?this.streamGenerator:sg);
        rep.setTemplate(templ);
        rep.setDispValue(dispVal);
        if(multiple!=null && !multiple.equals("")) rep.setMultiple(new Boolean(multiple).booleanValue());
        if(footer!=null && !footer.equals("")) rep.setFooter(new Boolean(footer).booleanValue());
        if(reports==null) reports = new LinkedHashMap();
        reports.put(id,rep);
    }
    
    /**
     * add report.
     * @param reports New value of property reports.
     */
    public void addReport(String id,String dispVal,String templ) {
        Report rep = new Report();
        rep.setId(id);
        rep.setTemplate(templ);
        rep.setDispValue(dispVal);
        rep.setJaxbpkgname(jaxbPkgName);
        rep.setStreamgenerator(streamGenerator);
        this.reports.put(id,rep);
    }
    /**
     * Getter for property reports.
     * @return Value of property reports.
     */
    public Report getReport(String id) {
        return (Report)reports.get(id);
    }
    
    public class Report implements Serializable{
        private String id;
        private String streamgenerator;
        private String template;
        private String jaxbpkgname;
        private String dispValue;
        private boolean multiple;
        private boolean footer=true;
        
        /**
         * Getter for property id.
         * @return Value of property id.
         */
        public java.lang.String getId() {
            return id;
        }
        
        /**
         * Setter for property id.
         * @param id New value of property id.
         */
        public void setId(java.lang.String id) {
            this.id = id;
        }
        
        /**
         * Getter for property streamgenerator.
         * @return Value of property streamgenerator.
         */
        public java.lang.String getStreamgenerator() {
            return streamgenerator;
        }
        
        /**
         * Setter for property streamgenerator.
         * @param streamgenerator New value of property streamgenerator.
         */
        public void setStreamgenerator(java.lang.String streamgenerator) {
            this.streamgenerator = streamgenerator;
        }
        
        /**
         * Getter for property template.
         * @return Value of property template.
         */
        public java.lang.String getTemplate() {
            return template;
        }
        
        /**
         * Setter for property template.
         * @param template New value of property template.
         */
        public void setTemplate(java.lang.String template) {
            this.template = template;
        }
        
        /**
         * Getter for property jaxbpkgname.
         * @return Value of property jaxbpkgname.
         */
        public java.lang.String getJaxbpkgname() {
            return jaxbpkgname;
        }
        
        /**
         * Setter for property jaxbpkgname.
         * @param jaxbpkgname New value of property jaxbpkgname.
         */
        public void setJaxbpkgname(java.lang.String jaxbpkgname) {
            this.jaxbpkgname = jaxbpkgname;
        }
        
        /**
         * Getter for property dispValue.
         * @return Value of property dispValue.
         */
        public java.lang.String getDispValue() {
            return dispValue;
        }
        
        /**
         * Setter for property dispValue.
         * @param dispValue New value of property dispValue.
         */
        public void setDispValue(java.lang.String dispValue) {
            this.dispValue = dispValue;
        }
        
        /**
         * Getter for property multiple.
         * @return Value of property multiple.
         */
        public boolean isMultiple() {
            return multiple;
        }
        
        /**
         * Setter for property multiple.
         * @param multiple New value of property multiple.
         */
        public void setMultiple(boolean multiple) {
            this.multiple = multiple;
        }
        
        /**
         * Getter for property footer.
         * @return Value of property footer.
         */
        public boolean isFooter() {
            return footer;
        }
        
        /**
         * Setter for property footer.
         * @param footer New value of property footer.
         */
        public void setFooter(boolean footer) {
            this.footer = footer;
        }
        
    }
    
}
