/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.rates.controller;

import edu.mit.coeus.rates.bean.InstituteRatesBean;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.query.QueryEngine;

/**
 * RatesController.java
 * Created on August 17, 2004, 11:01 AM
 * @author  Vyjayanthi
 */
public abstract class RatesController extends Controller {
    
    public String queryKey;
    public InstituteRatesBean instituteRatesBean;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    public final static String EMPTY = "";
    private QueryEngine queryEngine;
    protected CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    /** To identify the Rates related data in the Query Engine */
    protected static final String RATES = "Rates";
    
    /** Creates a new instance of RatesController */
    public RatesController() {
    }
    
    /** Creates a new instance of RatesController
     * creates the Key for the query engine from the instituteRatesBean.
     * @param instituteRatesBean institute rates bean
     */
    public RatesController(InstituteRatesBean instituteRatesBean) {
        if(instituteRatesBean != null && instituteRatesBean.getUnitNumber() != null) {
            this.instituteRatesBean = instituteRatesBean;
            queryKey = RATES + instituteRatesBean.getUnitNumber();
        }
        queryEngine = QueryEngine.getInstance();
    }
    
    /** Getter for property queryKey.
     * @return Value of property queryKey.
     *
     */
    public String getQueryKey() {
        return queryKey;
    }
    
    /** To set the query key
     * @param instituteRatesBean institute rates bean
     */
    public void prepareQueryKey(InstituteRatesBean instituteRatesBean) {
        queryKey = RATES + instituteRatesBean.getUnitNumber();
    }
    /**
     * To set all the instance variable to null.
     **/
    public abstract void cleanUp();
}
