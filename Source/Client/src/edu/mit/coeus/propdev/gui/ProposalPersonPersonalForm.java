/*
 * ProposalPersonPersonalForm.java
 *
 * Created on May 17, 2003, 2:47 PM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;

import java.awt.Color;
/**
 *
 * @author  Raghunath
 */
public class ProposalPersonPersonalForm extends javax.swing.JComponent implements TypeConstants {
    
    /** Creates new form ProposalPersonPersonalForm */
    public ProposalPersonPersonalForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblDateOfBirth = new javax.swing.JLabel();
        lblAge = new javax.swing.JLabel();
        lblAgeForFiscalYear = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        lblRace = new javax.swing.JLabel();
        lblEducationLevel = new javax.swing.JLabel();
        lblMajor = new javax.swing.JLabel();
        lblSchool = new javax.swing.JLabel();
        lblYearGraduated = new javax.swing.JLabel();
        lblDegree = new javax.swing.JLabel();
        lblIdProvided = new javax.swing.JLabel();
        lblIdVerified = new javax.swing.JLabel();
        lblCitizenShip = new javax.swing.JLabel();
        lblVisaCode = new javax.swing.JLabel();
        lblVisaType = new javax.swing.JLabel();
        lblVisaRenewalDate = new javax.swing.JLabel();
        chkHasVisa = new javax.swing.JCheckBox();
        lblHasVisa = new javax.swing.JLabel();
        txtGender = new edu.mit.coeus.utils.CoeusTextField();
        txtEducationalLevel = new edu.mit.coeus.utils.CoeusTextField();
        txtYearGraduated = new edu.mit.coeus.utils.CoeusTextField();
        txtIdProvided = new edu.mit.coeus.utils.CoeusTextField();
        txtCitizenship = new edu.mit.coeus.utils.CoeusTextField();
        txtVisaCode = new edu.mit.coeus.utils.CoeusTextField();
        txtRenevalDate = new edu.mit.coeus.utils.CoeusTextField();
        txtDOB = new edu.mit.coeus.utils.CoeusTextField();
        txtRace = new edu.mit.coeus.utils.CoeusTextField();
        txtMajor = new edu.mit.coeus.utils.CoeusTextField();
        txtDegree = new edu.mit.coeus.utils.CoeusTextField();
        txtIdVerified = new edu.mit.coeus.utils.CoeusTextField();
        txtVisaType = new edu.mit.coeus.utils.CoeusTextField();
        txtAgeByFiscal = new edu.mit.coeus.utils.CoeusTextField();
        txtAge = new edu.mit.coeus.utils.CoeusTextField();
        txtSchool = new edu.mit.coeus.utils.CoeusTextField();

        setLayout(new java.awt.GridBagLayout());

        lblDateOfBirth.setText("Date Of Birth:");
        lblDateOfBirth.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblDateOfBirth, gridBagConstraints);

        lblAge.setText("Age:");
        lblAge.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblAge, gridBagConstraints);

        lblAgeForFiscalYear.setText("Age By  Fiscal Year:");
        lblAgeForFiscalYear.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblAgeForFiscalYear, gridBagConstraints);

        lblGender.setText("Gender:");
        lblGender.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblGender, gridBagConstraints);

        lblRace.setText("Race:");
        lblRace.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblRace, gridBagConstraints);

        lblEducationLevel.setText("Education Level:");
        lblEducationLevel.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblEducationLevel, gridBagConstraints);

        lblMajor.setText("Major:");
        lblMajor.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblMajor, gridBagConstraints);

        lblSchool.setText("School:");
        lblSchool.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblSchool, gridBagConstraints);

        lblYearGraduated.setText("Year Graduated:");
        lblYearGraduated.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblYearGraduated, gridBagConstraints);

        lblDegree.setText("Degree:");
        lblDegree.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblDegree, gridBagConstraints);

        lblIdProvided.setText("Id Provided:");
        lblIdProvided.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblIdProvided, gridBagConstraints);

        lblIdVerified.setText("Id Verified:");
        lblIdVerified.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblIdVerified, gridBagConstraints);

        lblCitizenShip.setText("Citizenship:");
        lblCitizenShip.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblCitizenShip, gridBagConstraints);

        lblVisaCode.setText("Visa Code:");
        lblVisaCode.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblVisaCode, gridBagConstraints);

        lblVisaType.setText("Visa Type:");
        lblVisaType.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblVisaType, gridBagConstraints);

        lblVisaRenewalDate.setText("Visa Renewal Date:");
        lblVisaRenewalDate.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblVisaRenewalDate, gridBagConstraints);

        chkHasVisa.setForeground(new java.awt.Color(0, 0, 0));
        chkHasVisa.setBackground(new java.awt.Color(204, 204, 204));
        chkHasVisa.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(chkHasVisa, gridBagConstraints);

        lblHasVisa.setText("Has Visa: ");
        lblHasVisa.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblHasVisa, gridBagConstraints);

        txtGender.setPreferredSize(new java.awt.Dimension(145, 20));
        txtGender.setMaximumSize(new java.awt.Dimension(145, 20));
        txtGender.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtGender, gridBagConstraints);

        txtEducationalLevel.setPreferredSize(new java.awt.Dimension(145, 20));
        txtEducationalLevel.setMaximumSize(new java.awt.Dimension(145, 20));
        txtEducationalLevel.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtEducationalLevel, gridBagConstraints);

        txtYearGraduated.setPreferredSize(new java.awt.Dimension(145, 20));
        txtYearGraduated.setMaximumSize(new java.awt.Dimension(145, 20));
        txtYearGraduated.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtYearGraduated, gridBagConstraints);

        txtIdProvided.setPreferredSize(new java.awt.Dimension(145, 20));
        txtIdProvided.setMaximumSize(new java.awt.Dimension(145, 20));
        txtIdProvided.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtIdProvided, gridBagConstraints);

        txtCitizenship.setPreferredSize(new java.awt.Dimension(145, 20));
        txtCitizenship.setMaximumSize(new java.awt.Dimension(145, 20));
        txtCitizenship.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtCitizenship, gridBagConstraints);

        txtVisaCode.setPreferredSize(new java.awt.Dimension(145, 20));
        txtVisaCode.setMaximumSize(new java.awt.Dimension(145, 20));
        txtVisaCode.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtVisaCode, gridBagConstraints);

        txtRenevalDate.setDocument(new LimitedPlainDocument(12));
        txtRenevalDate.setPreferredSize(new java.awt.Dimension(145, 20));
        txtRenevalDate.setMaximumSize(new java.awt.Dimension(145, 20));
        txtRenevalDate.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtRenevalDate, gridBagConstraints);

        txtDOB.setDocument(new LimitedPlainDocument(12));
        txtDOB.setPreferredSize(new java.awt.Dimension(145, 20));
        txtDOB.setMaximumSize(new java.awt.Dimension(145, 20));
        txtDOB.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtDOB, gridBagConstraints);

        txtRace.setPreferredSize(new java.awt.Dimension(145, 20));
        txtRace.setMaximumSize(new java.awt.Dimension(145, 20));
        txtRace.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtRace, gridBagConstraints);

        txtMajor.setPreferredSize(new java.awt.Dimension(145, 20));
        txtMajor.setMaximumSize(new java.awt.Dimension(145, 20));
        txtMajor.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtMajor, gridBagConstraints);

        txtDegree.setPreferredSize(new java.awt.Dimension(145, 20));
        txtDegree.setMaximumSize(new java.awt.Dimension(145, 20));
        txtDegree.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtDegree, gridBagConstraints);

        txtIdVerified.setPreferredSize(new java.awt.Dimension(145, 20));
        txtIdVerified.setMaximumSize(new java.awt.Dimension(145, 20));
        txtIdVerified.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtIdVerified, gridBagConstraints);

        txtVisaType.setPreferredSize(new java.awt.Dimension(145, 20));
        txtVisaType.setMaximumSize(new java.awt.Dimension(145, 20));
        txtVisaType.setMinimumSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtVisaType, gridBagConstraints);

        txtAgeByFiscal.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtAgeByFiscal.setPreferredSize(new java.awt.Dimension(45, 20));
        txtAgeByFiscal.setMaximumSize(new java.awt.Dimension(45, 20));
        txtAgeByFiscal.setMinimumSize(new java.awt.Dimension(45, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(txtAgeByFiscal, gridBagConstraints);

        txtAge.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtAge.setPreferredSize(new java.awt.Dimension(45, 20));
        txtAge.setMaximumSize(new java.awt.Dimension(45, 20));
        txtAge.setMinimumSize(new java.awt.Dimension(45, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(txtAge, gridBagConstraints);

        txtSchool.setMaximumSize(new java.awt.Dimension(392, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(txtSchool, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private edu.mit.coeus.utils.CoeusTextField txtRace;
    private edu.mit.coeus.utils.CoeusTextField txtRenevalDate;
    private javax.swing.JLabel lblMajor;
    private javax.swing.JLabel lblVisaType;
    private javax.swing.JLabel lblEducationLevel;
    private edu.mit.coeus.utils.CoeusTextField txtCitizenship;
    private javax.swing.JLabel lblCitizenShip;
    private javax.swing.JLabel lblIdVerified;
    private edu.mit.coeus.utils.CoeusTextField txtAgeByFiscal;
    private javax.swing.JLabel lblDegree;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblAgeForFiscalYear;
    private javax.swing.JLabel lblSchool;
    private edu.mit.coeus.utils.CoeusTextField txtEducationalLevel;
    private edu.mit.coeus.utils.CoeusTextField txtDegree;
    private javax.swing.JLabel lblDateOfBirth;
    private edu.mit.coeus.utils.CoeusTextField txtVisaType;
    private edu.mit.coeus.utils.CoeusTextField txtGender;
    private edu.mit.coeus.utils.CoeusTextField txtSchool;
    private javax.swing.JCheckBox chkHasVisa;
    private edu.mit.coeus.utils.CoeusTextField txtYearGraduated;
    private javax.swing.JLabel lblIdProvided;
    private javax.swing.JLabel lblYearGraduated;
    private edu.mit.coeus.utils.CoeusTextField txtIdVerified;
    private javax.swing.JLabel lblVisaCode;
    private javax.swing.JLabel lblVisaRenewalDate;
    private edu.mit.coeus.utils.CoeusTextField txtDOB;
    private javax.swing.JLabel lblHasVisa;
    private javax.swing.JLabel lblAge;
    private edu.mit.coeus.utils.CoeusTextField txtAge;
    private javax.swing.JLabel lblRace;
    private edu.mit.coeus.utils.CoeusTextField txtVisaCode;
    private edu.mit.coeus.utils.CoeusTextField txtIdProvided;
    private edu.mit.coeus.utils.CoeusTextField txtMajor;
    // End of variables declaration//GEN-END:variables
    
}