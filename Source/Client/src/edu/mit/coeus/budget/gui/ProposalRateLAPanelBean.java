/*
 * ProposalRateLAPanelBean.java
 *
 * Created on October 17, 2003, 11:45 PM
 */

package edu.mit.coeus.budget.gui;

import javax.swing.JPanel;
import java.awt.event.*;
import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.budget.bean.InstituteLARatesBean;
import edu.mit.coeus.budget.bean.ProposalLARatesBean;
import edu.mit.coeus.utils.DateUtils;


/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */
public class ProposalRateLAPanelBean extends JPanel implements FocusListener,KeyListener{
    
    javax.swing.JLabel lblBlankSpace = new javax.swing.JLabel();
    javax.swing.JLabel lblFiscalYear = new javax.swing.JLabel();
    javax.swing.JLabel lblOnOffCampus = new javax.swing.JLabel();
    javax.swing.JLabel lblStartDate= new javax.swing.JLabel();
    javax.swing.JLabel lblInstituteRate = new javax.swing.JLabel();
    public CurrencyField txtApplicableRates = new CurrencyField();
    
    String fiscalYear;
    String OnOffCampus;
    String startDate;
    String instituteRate;
    String applicableRates;
    
    boolean isFieldEditable = true;
    String panelIdentityKey;
    
    DateUtils dateUtils = new DateUtils();
    boolean isBeanUpdated = false;
    String textRateValue;
    private final String ONFLAG = "On";
    private final String OFFFLAG = "Off";

    
    /** Creates a new instance of ProposalRatePanelBean
     * @param bean Object Bean instance
     */
    public ProposalRateLAPanelBean(Object bean) {
        setUpPanel();
        String displayDate;
        CurrencyField rateValue = new CurrencyField();
        if(bean instanceof InstituteLARatesBean) {
            InstituteLARatesBean instituteLARatesBean = (InstituteLARatesBean) bean;
            lblFiscalYear.setText(instituteLARatesBean.getFiscalYear());
            if(instituteLARatesBean.isOnOffCampusFlag()) {
                lblOnOffCampus.setText(ONFLAG);
            }
            else
                lblOnOffCampus.setText(OFFFLAG);
            
            displayDate = dateUtils.formatDate(instituteLARatesBean.getStartDate().toString(), "dd MMM yyyy");
            
            lblStartDate.setText(displayDate);
            String instRate = instituteLARatesBean.getInstituteRate()+"";
            
            rateValue.setText(instRate);
            lblInstituteRate.setText(rateValue.getText());
            txtApplicableRates.setText(instRate);   
            textRateValue = instRate;
            
            panelIdentityKey = (
            instituteLARatesBean.getRateClassCode()+
            instituteLARatesBean.getRateTypeCode()+
            instituteLARatesBean.getFiscalYear()+
            instituteLARatesBean.isOnOffCampusFlag()+
            instituteLARatesBean.getStartDate()+
            instituteLARatesBean.getInstituteRate()
            );
            
        }
        
        if(bean instanceof ProposalLARatesBean) {
            ProposalLARatesBean proposalLARatesBean = (ProposalLARatesBean) bean;
            lblFiscalYear.setText(proposalLARatesBean.getFiscalYear());
            
            if(proposalLARatesBean.isOnOffCampusFlag()) {
                lblOnOffCampus.setText(ONFLAG);
            }
            else
                lblOnOffCampus.setText(OFFFLAG);
            
            displayDate = dateUtils.formatDate(proposalLARatesBean.getStartDate().toString(), "dd MMM yyyy");
            lblStartDate.setText(displayDate);
            
            rateValue.setText(proposalLARatesBean.getInstituteRate()+"");
            lblInstituteRate.setText(rateValue.getText());
            txtApplicableRates.setText(proposalLARatesBean.getApplicableRate()+"");
            textRateValue = proposalLARatesBean.getApplicableRate()+"";
            
            panelIdentityKey = (
            proposalLARatesBean.getRateClassCode()+
            proposalLARatesBean.getRateTypeCode()+
            proposalLARatesBean.getFiscalYear()+
            proposalLARatesBean.isOnOffCampusFlag()+
            proposalLARatesBean.getStartDate()+
            proposalLARatesBean.getInstituteRate()
            );
            
        }
        
        fiscalYear = lblFiscalYear.getText();
        OnOffCampus = lblOnOffCampus.getText();
        startDate = lblStartDate.getText();
        instituteRate = lblInstituteRate.getText();
        applicableRates = txtApplicableRates.getText();
        //====Action listener for detecting the change in the Textfield
        txtApplicableRates.addFocusListener(this);
        txtApplicableRates.addKeyListener(this);
        
    }
    
    public void focusGained(java.awt.event.FocusEvent e) {
        textRateValue = txtApplicableRates.getText();
        
    }
    public void focusLost(java.awt.event.FocusEvent e) {
        if(!textRateValue.equals(txtApplicableRates.getText())) {
            isBeanUpdated = true;
        }
    }
    
    public void keyTyped(KeyEvent source ) {
        char charTyped = source.getKeyChar();
        if(!textRateValue.equals(txtApplicableRates.getText()+charTyped)) {
            isBeanUpdated = true;
        }
    }
    
    public void keyPressed(KeyEvent source ) {
        
        char charTyped = source.getKeyChar();
        if(!textRateValue.equals(txtApplicableRates.getText()+charTyped)) {
            isBeanUpdated = true;
        }
        
        
        //if(source.getKeyCode() == VK_TAB)
        //java.awt.event.KeyEvent.getKeyText(KeyEvent.VK_UP VK_DOWN  VK_TAB)
        //if(keyEventSource .getSource().equals()
    }
    
    public void keyReleased(KeyEvent source ) {
    }
    
    /** To Reset the Applicable rates with Institute Rates */    
    public void reset() {
        lblFiscalYear.setText(fiscalYear);
        lblOnOffCampus.setText(OnOffCampus);
        lblStartDate.setText(startDate);
        lblInstituteRate.setText(instituteRate);
        txtApplicableRates.setText(instituteRate);
        if(!textRateValue.equals(txtApplicableRates.getText())) {
            isBeanUpdated = true;
        }
    }
    
    /** To Check is the bean being updated
     * @return boolean if <true> updated
     */    
    public boolean isBeanUpdated() {
        return isBeanUpdated;
    }
    
    /** to set the textfield eanble or disable based on function type
     * @param disable boolean if <true> diabled
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
    
    /** get the PanelIdentityKey
     * @return String panelKey
     */    
    public String getPanelIdentityKey() {
        return panelIdentityKey;
    }
    
    /** get the Applicable rates
     * @return String
     */    
    public String getApplicableRate() {
        return txtApplicableRates.getText();
    }
    
    /** set Applicable rates
     * @param rateValue String applicable Rates
     */    
    public void setApplicableRate(String rateValue) {
        txtApplicableRates.setText(rateValue);
    }
    
    /** get Institute rates
     * @return String Institute rate
     */    
    public String getInstituteRate() {
        return lblInstituteRate.getText();
    }
    
    //==
    
    /** Method to set up Panel with Bean values */    
    public void setUpPanel() {
        
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        
        this.setLayout(new java.awt.GridBagLayout());
        this.setMaximumSize(new java.awt.Dimension(650, 20));
        this.setMinimumSize(new java.awt.Dimension(650, 20));
        this.setPreferredSize(new java.awt.Dimension(650, 20));
        
        lblBlankSpace.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        lblBlankSpace.setMaximumSize(new java.awt.Dimension(90, 30));
        lblBlankSpace.setMinimumSize(new java.awt.Dimension(90, 30));
        lblBlankSpace.setPreferredSize(new java.awt.Dimension(90, 30));
        lblBlankSpace.setForeground(lblBlankSpace.getBackground());
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        this.add(lblBlankSpace, gridBagConstraints);
        
        lblFiscalYear.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        
        lblFiscalYear.setMaximumSize(new java.awt.Dimension(90, 30));
        lblFiscalYear.setMinimumSize(new java.awt.Dimension(90, 30));
        lblFiscalYear.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        this.add(lblFiscalYear, gridBagConstraints);
        
        lblOnOffCampus.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        
        
        lblOnOffCampus.setMaximumSize(new java.awt.Dimension(70, 30));
        lblOnOffCampus.setMinimumSize(new java.awt.Dimension(70, 30));
        lblOnOffCampus.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        this.add(lblOnOffCampus, gridBagConstraints);
        
        
        lblStartDate.setMaximumSize(new java.awt.Dimension(75, 30));
        lblStartDate.setMinimumSize(new java.awt.Dimension(75, 30));
        lblStartDate.setPreferredSize(new java.awt.Dimension(75, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        this.add(lblStartDate, gridBagConstraints);
        
        lblInstituteRate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        lblInstituteRate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInstituteRate.setMaximumSize(new java.awt.Dimension(60, 30));
        lblInstituteRate.setMinimumSize(new java.awt.Dimension(60, 30));
        lblInstituteRate.setPreferredSize(new java.awt.Dimension(60, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        this.add(lblInstituteRate, gridBagConstraints);
        
        txtApplicableRates.setColumns(10);
        txtApplicableRates.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        
        txtApplicableRates.setMaximumSize(new java.awt.Dimension(50, 30));
        txtApplicableRates.setMinimumSize(new java.awt.Dimension(50, 30));
        txtApplicableRates.setPreferredSize(new java.awt.Dimension(110, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        
        lblStartDate.setFont(edu.mit.coeus.gui.CoeusFontFactory.getNormalFont());
        
        
        
        this.add(txtApplicableRates, gridBagConstraints);
        
    }
    
    //==
}
