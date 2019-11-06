/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.birt.bean;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author sharathk
 */
public class BirtParameterBean implements Serializable{
    private String name;
    private String promptText;
    private String help;
    private String format;
    private int dataType;
    private Map values; //list of COmboBox beans for name, value pair
    private int controlType; //Control Type
    private String defaultValue;
    private boolean hidden;
    private boolean required;
    private String parameterValueCode;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the promptText
     */
    public String getPromptText() {
        return promptText;
    }

    /**
     * @param promptText the promptText to set
     */
    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    /**
     * @return the help
     */
    public String getHelp() {
        return help;
    }

    /**
     * @param help the help to set
     */
    public void setHelp(String help) {
        this.help = help;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the dataType
     */
    public int getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the values
     */
    public Map getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(Map values) {
        this.values = values;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the controlType
     */
    public int getControlType() {
        return controlType;
    }

    /**
     * @param controlType the controlType to set
     */
    public void setControlType(int controlType) {
        this.controlType = controlType;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * @return the allowBlank
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @param allowBlank the allowBlank to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * @return the parameterValueCode
     */
    public String getParameterValueCode() {
        return parameterValueCode;
    }

    /**
     * @param parameterValueCode the parameterValueCode to set
     */
    public void setParameterValueCode(String parameterValueCode) {
        this.parameterValueCode = parameterValueCode;
    }

}
