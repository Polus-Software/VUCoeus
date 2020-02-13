/*
 * UserMaintenanceRequestHandler.java
 *
 * Created on June 30, 2003, 5:37 PM
 */

package edu.mit.coeus.user;

import java.util.Vector;
import javax.servlet.http.HttpServletResponse;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.user.bean.DataPositions;
/**
 *
 * @author  senthilar
 */
public abstract class UserMaintenanceRequestHandler implements DataPositions, TypeConstants {
    private HttpServletResponse response = null;
    private ObjectOutputStream outputToApplet = null;
    private ObjectInputStream inputFromApplet = null;
    private Vector userRolesInfoBean = null;
    private ResponderBean responder = null;
//    private UtilFactory UtilFactory = new UtilFactory();
    private Vector request = null;
    protected String userID = null;
    protected String unitNumber = null;
    protected String command = null;
    protected String module = null;
    protected String selectedPersonID = null;
    protected Vector dataObjects = new Vector();
    protected Vector data = null;
    protected char functionType = '\0';
    
    /** Creates a new instance of UserMaintenanceRequestHandler */
    public UserMaintenanceRequestHandler() {
        System.out.println("Entering the base handler class now");
    }
    
    /*
    public UserMaintenanceRequestHandler(int id){
        this.id = id;
    }
     */
    
    //public abstract boolean checkForRights();
    
    public abstract void doPerform() throws Exception;
    
    /*
     * The setValues method will used for saving the records/beans values
     * to the database. All classes will server either the SET/GET
     * functions. In case of GET function this method is called.
     * So calling the GET/SET typically depennds on the functionType.
     * The implementation is left abstract to have the subclasses to
     * implement it.
     * @param Vector the vector of beans or records
     */
    public abstract void setValues(Vector vector);
    
    public abstract void fetchValues() throws Exception;
    
    public void setUserID(String userid){
        this.userID = userid;
    }
    
    public String getUserID(){
        return userID;
    }
    
    public Vector getRequestData(){
        return request;
    }
    
    public void setHttpServletResponse(HttpServletResponse response){
        this.response = response;
    }
    
    public HttpServletResponse getHttpServletResponse(){
        return response;
    }
    
    public String getUnitNumber(){
        return unitNumber;
    }
    
    public void setUnitNumber(String unitNumber){
        this.unitNumber = unitNumber;
    }
    
    public String getCommand(){
        return command;
    }
    
    public void setCommand(String command){
        this.command = command;
    }
    
    public String getModule(){
        return module;
    }
    
    public char getFunctionType(){
        return functionType;
    }
    
    public void setFucntionType(char functionType){
        this.functionType = functionType;
    }
    
    public void setModule(String module){
        this.module = module;
    }
    
    public String getSelectedPersonID(){
        return selectedPersonID;
    }
    
    public void setSelectedPersonID(String selectedPersonID){
        this.selectedPersonID = selectedPersonID;
    }
    
    public void setData(Vector data){
        this.data = data;
    }
    
    public Vector getData(){
        return data;
    }
    
    /*
     * The below write methods provide the return objects in particular structure.
     */
    
    public void writeUnitNumber(String unitNumber){
        dataObjects.add(UNIT_NUMBER_POS, unitNumber);
    }
    
    public void writeSelectedPersonID(String selectedPersonID) {
        dataObjects.add(SELECTED_PERSON_POS, selectedPersonID);
    }
    
    public void writeData(Vector data){
        dataObjects.add(DATA_POS, data);
    }
    
    public void writeToClient(){
        try{
            responder = new ResponderBean();
            responder.setDataObjects(dataObjects);
            responder.setResponseStatus(true);
            responder.setMessage(null);
        }catch(Exception e) {
            e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "ProposalMaintenanceServlet", "perform");
            
        } finally {
            try{
                // send the object to applet
                outputToApplet
                = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                "ProposalMaintenanceServlet", "perform");
            }
        }
    }
    
    public void setDataObjects(Vector dataObjects) {
        this.dataObjects = dataObjects;
    }
    
    public Vector getDataObjects()
    {
        return dataObjects;
    }
    
}
