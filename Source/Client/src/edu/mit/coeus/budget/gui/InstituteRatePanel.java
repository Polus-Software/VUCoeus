/*
 * RatePanel.java
 *
 * Created on October 17, 2003, 7:05 PM
 */

package edu.mit.coeus.budget.gui;
import edu.mit.coeus.utils.CurrencyField;
import javax.swing.JPanel;

/**
 *
 * @author  ranjeeva
 */
public class InstituteRatePanel extends JPanel {
    
    javax.swing.JLabel lblBlankSpace = new javax.swing.JLabel();
    javax.swing.JLabel lblFiscalYear = new javax.swing.JLabel();
    javax.swing.JLabel lblOnOffCampus = new javax.swing.JLabel();
    javax.swing.JLabel lblActivityType = new javax.swing.JLabel();
    javax.swing.JLabel lblStartDate = new javax.swing.JLabel();
    javax.swing.JLabel lblInstituteRate = new javax.swing.JLabel();
    CurrencyField txtApplicableRates = new CurrencyField();
    
    boolean enableField = true;
    
    String panelIdentityKey;
    
    public String getPanelIdentityKey() {
        return panelIdentityKey;
    }
    
    public String getApplicableRate() {
        return txtApplicableRates.getText();
    }
    
    public void setApplicableRate(String rateValue) {
        txtApplicableRates.setText(rateValue);
    }
    
    public String getInstituteRate() {
        return lblInstituteRate.getText();
    }
     
    public InstituteRatePanel() {
    }
    /*
    java.awt.Dimension panelMaximumSize = new java.awt.Dimension(650, 20);
    java.awt.Dimension panelManimumSize = new java.awt.Dimension(650, 20);
    
    java.awt.Dimension panelPreferredSize = new java.awt.Dimension(650, 20);
    java.awt.Dimension lblBlankSpaceMaxSize = new java.awt.Dimension(70, 30);
    java.awt.Dimension lblBlankSpaceMinSize = new java.awt.Dimension(70, 30);
    java.awt.Dimension lblBlankSpacePreferredSize = new java.awt.Dimension(70, 30);
    java.awt.Dimension lblFiscalYearMaxSize = new java.awt.Dimension(70, 30);
    java.awt.Dimension lblFiscalYearMinSize = new java.awt.Dimension(70, 30);
    java.awt.Dimension lblFiscalYearPreferredSize = new java.awt.Dimension(70, 30);
    java.awt.Dimension lblOnOffCampusMaxiSize = new java.awt.Dimension(40, 30);
    java.awt.Dimension lblOnOffCampusMinSize = new java.awt.Dimension(40, 30);
    java.awt.Dimension lblOnOffCampusPreferredSize = new java.awt.Dimension(40, 30);
    
    java.awt.Dimension lblActivityTypeMaxSize  = new java.awt.Dimension(150, 30);
    java.awt.Dimension lblActivityTypeMinSize  = new java.awt.Dimension(150, 30);
    java.awt.Dimension lblActivityTypePreferredSize  = new java.awt.Dimension(150, 30);
    java.awt.Dimension lblStartDateMaxSize  = new java.awt.Dimension(70, 30);
    java.awt.Dimension lblStartDateMinSize  = new java.awt.Dimension(70, 30);
    java.awt.Dimension lblStartDatePreferredSize  = new java.awt.Dimension(70, 30);
    
    java.awt.Dimension lblInstituteRateMaxSize  = new java.awt.Dimension(40, 30);
    java.awt.Dimension lblInstituteRateMinSize  = new java.awt.Dimension(40, 30);
    java.awt.Dimension lblInstituteRatePreferredSize  = new java.awt.Dimension(40, 30);
    
    java.awt.Dimension txtApplicableRatesMaxSize  = new java.awt.Dimension(50, 30);
    java.awt.Dimension txtApplicableRatestMinSize  = new java.awt.Dimension(50, 30);
    java.awt.Dimension txtApplicableRatesPreferredSize  = new java.awt.Dimension(110, 30);
    
    */
    
    public InstituteRatePanel(String[] instRateValues, boolean enableField) {
        
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
        
        //adding values to TextField
        if(instRateValues != null && instRateValues.length >= 6) {
            panelIdentityKey = instRateValues[6];
            
            lblFiscalYear.setText(instRateValues[0]);    //"2003");
            lblOnOffCampus.setText(instRateValues[1]); //"Off");
            lblActivityType.setText(instRateValues[2]); //"Instruction and Department");
            lblStartDate.setText(instRateValues[3]);    //"1 July 2003");
            lblInstituteRate.setText(instRateValues[4]); //"34.56");
            txtApplicableRates.setText(instRateValues[5]);  //"56.67");
            //to enable and siable the Fields
            if(enableField)
                txtApplicableRates.setEnabled(true);
            else {
                txtApplicableRates.setEnabled(false);
                txtApplicableRates.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            }
            
            //lblBlankSpace.setText(instRateValues[6]);
        }
        
        this.add(txtApplicableRates, gridBagConstraints);
        
        // here to set the values to the Panel components
        //instRateValues =  new String[6];
        //instRateValues[0] = "2000";
        // instRateValues[1] = "Off";
        //instRateValues[2] = "Activitycode";
        // instRateValues[3] = "1 July 2003";
        // instRateValues[4] = "33.45";
        // instRateValues[5] = "34.5";
        
        
        
        
        
    }//end constructor
}//end of RatePanel class

