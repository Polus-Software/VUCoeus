/*
 * ProcedureBean.java
 *
 * Created on April 19, 2005, 8:30 PM
 */

package edu.mit.coeuslite.utils.bean;

import edu.mit.coeus.bean.BaseBean;
import java.util.Vector;

/**
 *
 * @author  bijosht
 */
public class ProcedureBean implements BaseBean,java.io.Serializable{
    
    private String procedureId;
    private String procedureName;
    private Vector procparameterVector;

    
    /** Creates a new instance of ProcedureBean */
    public ProcedureBean() {
    }
    
    /**
     * Getter for property procedureId.
     * @return Value of property procedureId.
     */
    public java.lang.String getProcedureId() {
        return procedureId;
    }    
 
    /**
     * Setter for property procedureId.
     * @param procedureId New value of property procedureId.
     */
    public void setProcedureId(java.lang.String procedureId) {
        this.procedureId = procedureId;
    }    
    
    /**
     * Getter for property procedureName.
     * @return Value of property procedureName.
     */
    public java.lang.String getProcedureName() {
        return procedureName;
    }
    
    /**
     * Setter for property procedureName.
     * @param procedureName New value of property procedureName.
     */
    public void setProcedureName(java.lang.String procedureName) {
        this.procedureName = procedureName;
    }
    
    /**
     * Getter for property procparameterVector.
     * @return Value of property procparameterVector.
     */
    public Vector getProcparameterVector() {
        return procparameterVector;
    }
    
    /**
     * Setter for property procparameterVector.
     * @param procparameterVector New value of property procparameterVector.
     */
    public void setProcparameterVector(Vector procparameterVector) {
        this.procparameterVector = procparameterVector;
    }
    
}
