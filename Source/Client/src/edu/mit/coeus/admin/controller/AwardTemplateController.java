/*
 * AwardTemplateController.java
 *
 * Created on December 15, 2004, 3:37 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.admin.bean.TemplateBaseBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  ajaygm
 */
public abstract class AwardTemplateController extends Controller{
    
    public TemplateBaseBean templateBaseBean;
    
    public String queryKey;
    
    public final static String EMPTY = "";
    
    private CoeusSearch sponsorSearch;
    private String sponsorName, sponsorCode;
    
    private static final String SPONSOR_SEARCH = "sponsorSearch";
    private static final String SPONSOR_CODE = "SPONSOR_CODE";
    private static final String SPONSOR_NAME = "SPONSOR_NAME";
    
    public static final int CANCEL_CLICKED = 0;
    public static final int OK_CLICKED = 1;
    
    //To get sponsor name when code is entered from rolodex servlet.
    private static final String ROLODEX_SERVLET = "/rolMntServlet";
    private static final char GET_SPONSOR_NAME = 'S';
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    private static final String INACTIVE_STATUS = "I";
    private static final String ACTIVE_STATUS = "A";
   //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
    
    /** Creates a new instance of AwardTemplateController */
    public AwardTemplateController() {
    }
    
    /*Creates a new instance of AdminController 
     *Creates the Key for the query engine from the award base bean.
     *@param TemplateBaseBean award Base Bean.
     */
    public AwardTemplateController(TemplateBaseBean templateBaseBean) {
         if(templateBaseBean!= null) {
            this.templateBaseBean = templateBaseBean;
            queryKey = ""+templateBaseBean.getTemplateCode();
        }
//        queryEngine = QueryEngine.getInstance();
    }
    
    /**
     * Getter for property queryKey.
     * @return Value of property queryKey.
     */
    public java.lang.String getQueryKey() {
        return queryKey;
    }
    
    public void prepareQueryKey(TemplateBaseBean templateBaseBean) {
        queryKey = ""+templateBaseBean.getTemplateCode();
    }
 
     /** displays sponsor search.
     * returns OK_CLICKED if OK button was Clicked.
     * else returns CANCEL_CLICKED if Cancel button was clicked.
     * @throws Exception if any error occurs.
     * @return OK_CLICKED if OK button was Clicked.
     * else returns CANCEL_CLICKED if Cancel button was clicked.
     */    
    public int sponsorSearch()throws Exception {
        //Do Lazy initialization as every subclass of this need not search for Sponsor.
        if(sponsorSearch == null) {
            sponsorSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), SPONSOR_SEARCH, CoeusSearch.TWO_TABS);
        }
        
        sponsorSearch.showSearchWindow();
        HashMap selectedRow = sponsorSearch.getSelectedRow();
        if(selectedRow == null || selectedRow.isEmpty()) {
            return CANCEL_CLICKED;
        }
        sponsorCode = selectedRow.get(SPONSOR_CODE).toString();
        sponsorName = selectedRow.get(SPONSOR_NAME).toString();
        return OK_CLICKED;
        
    }
    
    /** returns searched sponsor code
     * @return searched sponsor code
     */    
    public String getSponsorCode() {
        return sponsorCode;
    }
    
    /** returns searched sponsor code
     * @return returns searched sponsor code
     */    
    public String getSponsorName() {
        return sponsorName;
    }
    
    /** contacts the server and fetches the Sponsor name for the sponsor code.
     * returns "" if sponsor code is invalid.
     * @return sponsor name
     * @param sponsorCode sponsor code for which sponsor name has to be retrieved.
     * @throws CoeusException if cannot contact server or if server error occurs.
     */    
    public String getSponsorName(String sponsorCode)throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_SPONSOR_NAME);
        requesterBean.setDataObject(sponsorCode);

        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();

        if(!responderBean.isSuccessfulResponse()){
            throw new CoeusException(responderBean.getMessage(), 1);
        }
        
        //Got data from server. return sponsor name.
        //sponsor name = EMPTY if not found.
//        if(responderBean.getDataObject() == null) {
//            return EMPTY;
//        }
        
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        //if(responderBean.getDataObject() == null) return EMPTY;
        //String sponsorName = responderBean.getDataObject().toString();
        Vector vecData =  new Vector();
        String sponsorStatus = "";
        sponsorName = "";
        if (responderBean.isSuccessfulResponse()){
            vecData = (Vector)responderBean.getDataObjects();
            if(vecData != null && !vecData.isEmpty()){
                sponsorStatus = (String)vecData.get(1);
                if(ACTIVE_STATUS.equals(sponsorStatus)){
                    sponsorName = (String)vecData.get(0);
                }
            }
        } else {
            throw new CoeusException(responderBean.getMessage());
        }
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        
        return sponsorName;
    }
    
    
    /** contacts the server and fetches the Sponsor name for the sponsor code.
     * returns "" if sponsor code is invalid.
     * @return sponsor name
     * @param sponsorCode sponsor code for which sponsor name has to be retrieved.
     * @throws CoeusException if cannot contact server or if server error occurs.
     */
    public String getSponsorNameForCode(String sponsorCode)throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_SPONSOR_NAME);
        requesterBean.setDataObject(sponsorCode);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(!responderBean.isSuccessfulResponse()){
            throw new CoeusException(responderBean.getMessage(), 1);
        }
        
        Vector vecData =  new Vector();
        String sponsorStatus = "";
        sponsorName = "";
        if (responderBean.isSuccessfulResponse()){
            vecData = (Vector)responderBean.getDataObjects();
            if(vecData != null && !vecData.isEmpty()){
                sponsorName = (String)vecData.get(0);
            }
        } else {
            throw new CoeusException(responderBean.getMessage());
        }
        return sponsorName;
    }
    
    public void cleanUp(){
        
    }
}//End of Class
