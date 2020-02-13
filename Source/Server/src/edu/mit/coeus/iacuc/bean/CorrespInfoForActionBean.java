/*
 * CorrespInfoForAction.java
 *
 * Created on March 27, 2003, 8:33 PM
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;
import java.io.ByteArrayOutputStream;
/**
 *
 * @author  Geo
 */
public class CorrespInfoForActionBean {
    
    private int correspTypeCode;
    private String committeeId;
    private int scheduleId;
    private ByteArrayOutputStream template;
    
    /** Creates a new instance of CorrespInfoForAction */
    public CorrespInfoForActionBean() {
    }
    /**
     *  Set the correspondence type code
     */
    public void setCorrespTypeCode(int corresptypeCode){
        this.correspTypeCode = correspTypeCode;
    }
    /**
     *Get the correspondence type code
     */
    public int getCorrespTypeCode(){
        return correspTypeCode;
    }
    
    /**
     *  Set the committee id
     */
    public void setCommitteeId(String committeeId){
        this.committeeId = committeeId;
    }
    /**
     *Get the committeeId
     */
    public String getCommitteeId(){
        return committeeId;
    }
    /**
     *  Set the scheduleId
     */
    public void setScheduleId(int scheduleId){
        this.scheduleId = scheduleId;
    }
    /**
     *Get the scheduleId
     */
    public int getScheduleId(){
        return scheduleId;
    }
    /**
     *  Set the template
     */
    public void setTemplate(ByteArrayOutputStream template){
        this.template = template;
    }
    /**
     *Get the template
     */
    public ByteArrayOutputStream getTemplate(){
        return template;
    }
}
