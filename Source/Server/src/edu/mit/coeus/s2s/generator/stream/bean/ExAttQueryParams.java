/*
 * ExAttQueryParam.java
 *
 * Created on August 12, 2005, 2:10 PM
 */

package edu.mit.coeus.s2s.generator.stream.bean;

import edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;

/**
 *
 * @author  geot
 */
public class ExAttQueryParams{
    private ProposalNarrativeFormBean propNarrBean;
    private ProposalNarrativePDFSourceBean propNarrPdfBean;
    private boolean firstPeriodFlag;
    public ExAttQueryParams(ProposalNarrativeFormBean propNarrBean,
                            ProposalNarrativePDFSourceBean propNarrPdfBean,
                            boolean firstPeriod){
        this.propNarrBean = propNarrBean;
        this.propNarrPdfBean = propNarrPdfBean;
        this.firstPeriodFlag = firstPeriod;
    }
    public ExAttQueryParams(ProposalNarrativeFormBean propNarrBean,
                            ProposalNarrativePDFSourceBean propNarrPdfBean){
        this.propNarrBean = propNarrBean;
        this.propNarrPdfBean = propNarrPdfBean;
    }
    
    /**
     * Getter for property propNarrBean.
     * @return Value of property propNarrBean.
     */
    public edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean getPropNarrBean() {
        return propNarrBean;
    }
    
    /**
     * Setter for property propNarrBean.
     * @param propNarrBean New value of property propNarrBean.
     */
    public void setPropNarrBean(edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean propNarrBean) {
        this.propNarrBean = propNarrBean;
    }
    
    /**
     * Getter for property propNarrPdfBean.
     * @return Value of property propNarrPdfBean.
     */
    public edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean getPropNarrPdfBean() {
        return propNarrPdfBean;
    }
    
    /**
     * Setter for property propNarrPdfBean.
     * @param propNarrPdfBean New value of property propNarrPdfBean.
     */
    public void setPropNarrPdfBean(edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean propNarrPdfBean) {
        this.propNarrPdfBean = propNarrPdfBean;
    }
    
    /**
     * Getter for property firstPeriodFlag.
     * @return Value of property firstPeriodFlag.
     */
    public boolean isFirstPeriodFlag() {
        return firstPeriodFlag;
    }
    
    /**
     * Setter for property firstPeriodFlag.
     * @param firstPeriodFlag New value of property firstPeriodFlag.
     */
    public void setFirstPeriodFlag(boolean firstPeriodFlag) {
        this.firstPeriodFlag = firstPeriodFlag;
    }
    
}

