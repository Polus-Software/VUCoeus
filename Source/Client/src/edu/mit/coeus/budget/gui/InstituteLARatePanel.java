/*
 * InstituteLARatePanel.java
 *
 * Created on October 17, 2003, 8:36 PM
 */

package edu.mit.coeus.budget.gui;
import edu.mit.coeus.utils.CurrencyField;
import javax.swing.JPanel;

/**
 *
 * @author  ranjeeva
 */
public class InstituteLARatePanel extends JPanel {
    
    javax.swing.JLabel lblBlankSpace = new javax.swing.JLabel();
    javax.swing.JLabel lblFiscalYear = new javax.swing.JLabel();
    javax.swing.JLabel lblOnOffCampus = new javax.swing.JLabel();
    javax.swing.JLabel lblStartDate= new javax.swing.JLabel();
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
     
    /** Creates a new instance of InstituteLARatePanel */
    public InstituteLARatePanel(String[] laRateValues, boolean enableField) {
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
        
        if(laRateValues.length >= 5) {
            panelIdentityKey = laRateValues[5];
            lblFiscalYear.setText(laRateValues[0]);  //"2003");
            lblOnOffCampus.setText(laRateValues[1]); //"Off");
            lblStartDate.setText(laRateValues[2]);   //"1 July 2003");
            lblInstituteRate.setText(laRateValues[3]);  //"34.56");
            txtApplicableRates.setText(laRateValues[4]); //"56.67");
            if(enableField)
                txtApplicableRates.setEnabled(true);
            else {
                txtApplicableRates.setEnabled(false);
                txtApplicableRates.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            }
            
            //lblBlankSpace.setText(laRateValues[5]);
        }
        
        this.add(txtApplicableRates, gridBagConstraints);
        
    }
    
}
