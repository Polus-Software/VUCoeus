/*
 * ProposalRatePanelBean.java
 *
 * Created on October 17, 2003, 3:36 PM
 */

package edu.mit.coeus.budget.gui;


import javax.swing.JPanel;
import java.awt.event.*;

import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.budget.bean.InstituteRatesBean;
import edu.mit.coeus.budget.bean.ProposalRatesBean;
import edu.mit.coeus.utils.DateUtils;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */
public class ProposalRateInstPanelBean extends JPanel implements FocusListener,KeyListener {
    
    javax.swing.JLabel lblBlankSpace = new javax.swing.JLabel();
    javax.swing.JLabel lblFiscalYear = new javax.swing.JLabel();
    javax.swing.JLabel lblOnOffCampus = new javax.swing.JLabel();
    javax.swing.JLabel lblActivityType = new javax.swing.JLabel();
    javax.swing.JLabel lblStartDate= new javax.swing.JLabel();
    javax.swing.JLabel lblInstituteRate = new javax.swing.JLabel();
    public CurrencyField txtApplicableRates = new CurrencyField();
    
    String fiscalYear;
    String OnOffCampus;
    String activityType;
    String startDate;
    String instituteRate;
    String applicableRates;
    
    boolean isFieldEditable = true;
    String panelIdentityKey;
    boolean isBeanUpdated = false;
    String textRateValue;
    private final String ONFLAG = "On";
    private final String OFFFLAG = "Off";
    DateUtils dateUtils = new DateUtils();
    
    
    /** Creates a new instance of ProposalRatePanelBean
     * @param bean Object a Bean instance
     */
    public ProposalRateInstPanelBean(Object bean) {
        setUpPanel();
        
        
        String displayDate;
        CurrencyField rateValue = new CurrencyField();
        
        
        if(bean instanceof InstituteRatesBean) {
            InstituteRatesBean instituteRatesBean = (InstituteRatesBean) bean;
            lblFiscalYear.setText(instituteRatesBean.getFiscalYear());
            if(instituteRatesBean.isOnOffCampusFlag()) {
                lblOnOffCampus.setText(ONFLAG);
            }
            else
                lblOnOffCampus.setText(OFFFLAG);
            
            lblActivityType.setText(instituteRatesBean.getActivityTypeDescription());
            displayDate = dateUtils.formatDate(instituteRatesBean.getStartDate().toString(), "dd MMM yyyy");
            
            lblStartDate.setText(displayDate);
            String instRates = instituteRatesBean.getInstituteRate()+"";
            rateValue.setText(instRates);
            lblInstituteRate.setText(rateValue.getText());
            txtApplicableRates.setText(instRates);
            
            textRateValue = instRates;
            
            panelIdentityKey = (
            instituteRatesBean.getRateClassCode()+
            instituteRatesBean.getRateTypeCode()+
            instituteRatesBean.getFiscalYear()+
            instituteRatesBean.isOnOffCampusFlag()+
            instituteRatesBean.getStartDate()+
            instituteRatesBean.getActivityCode()+
            instituteRatesBean.getInstituteRate()
            );
        }
        
        if(bean instanceof ProposalRatesBean) {
            ProposalRatesBean proposalRatesBean = (ProposalRatesBean) bean;
            lblFiscalYear.setText(proposalRatesBean.getFiscalYear());
            
            if(proposalRatesBean.isOnOffCampusFlag()) {
                lblOnOffCampus.setText(ONFLAG);
            }
            else
                lblOnOffCampus.setText(OFFFLAG);
            
            lblActivityType.setText(proposalRatesBean.getActivityTypeDescription());
            displayDate = dateUtils.formatDate(proposalRatesBean.getStartDate().toString(), "dd MMM yyyy");
            
            lblStartDate.setText(displayDate);
            rateValue.setText(proposalRatesBean.getInstituteRate()+"");
            lblInstituteRate.setText(rateValue.getText());
            txtApplicableRates.setText(proposalRatesBean.getApplicableRate()+"");
            textRateValue = proposalRatesBean.getApplicableRate()+"";
            
            panelIdentityKey = (
            proposalRatesBean.getRateClassCode()+
            proposalRatesBean.getRateTypeCode()+
            proposalRatesBean.getFiscalYear()+
            proposalRatesBean.isOnOffCampusFlag()+
            proposalRatesBean.getStartDate()+
            proposalRatesBean.getActivityCode()+
            proposalRatesBean.getInstituteRate()
            );
            
            
        }
        
        fiscalYear = lblFiscalYear.getText();
        OnOffCampus = lblOnOffCampus.getText();
        activityType = lblActivityType.getText();
        startDate = lblStartDate.getText();
        instituteRate = lblInstituteRate.getText();
        applicableRates = txtApplicableRates.getText();
        
        //====Action listener for detecting the change in the Textfield
        txtApplicableRates.addFocusListener(this);
        txtApplicableRates.addKeyListener(this);
        
    }
    
    /** Focus Gained
     * @param e Object FocusEvent
     */    
    public void focusGained(java.awt.event.FocusEvent e) {
        textRateValue = txtApplicableRates.getText();
        
    }
    
    /** Focus Lost
     * @param e Object FocusEvent
     */    
    public void focusLost(java.awt.event.FocusEvent e) {
        if(!textRateValue.equals(txtApplicableRates.getText())) {
            isBeanUpdated = true;
        }
    }
    
    /** Key Typed
     * @param source Object KeyEvent object
     */    
    public void keyTyped(KeyEvent source ) {
        
        char charTyped = source.getKeyChar();
        if(!textRateValue.equals(txtApplicableRates.getText()+charTyped)) {
            isBeanUpdated = true;
        }
    }
    
    /** Key Pressed
     * @param source Object KeyEvent object
     */    
    public void keyPressed(KeyEvent source ) {
        
        char charTyped = source.getKeyChar();
        if(!textRateValue.equals(txtApplicableRates.getText()+charTyped)) {
            isBeanUpdated = true;
        }
    }
    
    /** Key Released
     * @param source Object KeyEvent object
     */    
    public void keyReleased(KeyEvent source ) {
    }
    
    
    /** To Reset the Applicable rates with Institute Rates */    
    public void reset() {
        lblFiscalYear.setText(fiscalYear);
        lblOnOffCampus.setText(OnOffCampus);
        lblActivityType.setText(activityType);
        lblStartDate.setText(startDate);
        lblInstituteRate.setText(instituteRate);
        txtApplicableRates.setText(instituteRate);
        if(!textRateValue.equals(txtApplicableRates.getText())) {
            isBeanUpdated = true;
        }
    }
    
    /** To check is the bean being Updated
     * @return if <true> updated
     */    
    public boolean isBeanUpdated() {
        return isBeanUpdated;
    }
    
    /** To Set the Field to be enabled or disabled
     * @param disable boolean
     */    
    public void setFieldOption(boolean disable) {
        isFieldEditable = !disable;
        if(isFieldEditable)
            txtApplicableRates.setEnabled(true);
        else {
            txtApplicableRates.setEnabled(false);
            txtApplicableRates.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        }
    }
    
    /** Panel identity Key to Indentify the Panel
     * @return String panelKey
     */    
    public String getPanelIdentityKey() {
        return panelIdentityKey;
    }
    
    /** Get the Applicable Rates
     * @return String applicable Rates
     */    
    public String getApplicableRate() {
        return txtApplicableRates.getText();
    }
    
    /** set Applicable rates
     * @param rateValue String Applicable rate value
     */    
    public void setApplicableRate(String rateValue) {
        txtApplicableRates.setText(rateValue);
    }
    
    /** get Institute Rate
     * @return String Institute Rate
     */    
    public String getInstituteRate() {
        return lblInstituteRate.getText();
    }
    
    /** Method called to setUp Panle with the Bean value */    
    public void setUpPanel() {
        
        //==
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        this.setLayout(new java.awt.GridBagLayout());
        this.setMaximumSize(new java.awt.Dimension(650, 20));
        this.setMinimumSize(new java.awt.Dimension(650, 20));
        this.setPreferredSize(new java.awt.Dimension(650, 20));
        
        lblBlankSpace.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        lblBlankSpace.setMaximumSize(new java.awt.Dimension(70, 30));
        lblBlankSpace.setMinimumSize(new java.awt.Dimension(70, 30));
        lblBlankSpace.setPreferredSize(new java.awt.Dimension(70, 30));
        lblBlankSpace.setForeground(lblBlankSpace.getBackground());
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        this.add(lblBlankSpace, gridBagConstraints);
        
        lblFiscalYear.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        lblFiscalYear.setMaximumSize(new java.awt.Dimension(70, 30));
        lblFiscalYear.setMinimumSize(new java.awt.Dimension(70, 30));
        lblFiscalYear.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        this.add(lblFiscalYear, gridBagConstraints);
        
        lblOnOffCampus.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        lblOnOffCampus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblOnOffCampus.setMaximumSize(new java.awt.Dimension(40, 30));
        lblOnOffCampus.setMinimumSize(new java.awt.Dimension(40, 30));
        lblOnOffCampus.setPreferredSize(new java.awt.Dimension(40, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        this.add(lblOnOffCampus, gridBagConstraints);
        
        lblActivityType.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        
        lblActivityType.setMaximumSize(new java.awt.Dimension(150, 30));
        lblActivityType.setMinimumSize(new java.awt.Dimension(150, 30));
        lblActivityType.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        this.add(lblActivityType, gridBagConstraints);
        
        lblStartDate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        lblStartDate.setMaximumSize(new java.awt.Dimension(70, 30));
        lblStartDate.setMinimumSize(new java.awt.Dimension(70, 30));
        lblStartDate.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        this.add(lblStartDate, gridBagConstraints);
        
        lblInstituteRate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        lblInstituteRate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        
        lblInstituteRate.setMaximumSize(new java.awt.Dimension(40, 30));
        lblInstituteRate.setMinimumSize(new java.awt.Dimension(40, 30));
        lblInstituteRate.setPreferredSize(new java.awt.Dimension(40, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        this.add(lblInstituteRate, gridBagConstraints);
        
        txtApplicableRates.setColumns(10);
        txtApplicableRates.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        
        txtApplicableRates.setMaximumSize(new java.awt.Dimension(50, 30));
        txtApplicableRates.setMinimumSize(new java.awt.Dimension(50, 30));
        txtApplicableRates.setPreferredSize(new java.awt.Dimension(110, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        
        
        
        this.add(txtApplicableRates, gridBagConstraints);
        
    }
    
    //==
}
