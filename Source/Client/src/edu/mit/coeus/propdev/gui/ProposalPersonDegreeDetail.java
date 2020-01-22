/*
 * ProposalPersonDegreeDetail.java
 *
 * Created on April 1, 2003, 10:21 AM
 */

package edu.mit.coeus.propdev.gui;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
/**
 *
 * @author  Raghunath
 */
public class ProposalPersonDegreeDetail extends javax.swing.JComponent {
    
    /** Creates new form ProposalPersonDegreeDetail */
    public ProposalPersonDegreeDetail() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlSchoolContainer = new javax.swing.JPanel();
        pnlSchool = new javax.swing.JPanel();
        txtSponsorCode = new edu.mit.coeus.utils.CoeusTextField();
        cmdIdCode = new edu.mit.coeus.utils.CoeusComboBox();
        txtDegree1 = new edu.mit.coeus.utils.CoeusTextField();
        lblName = new javax.swing.JLabel();
        lblIdCode = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        pnlDegreeInfo = new javax.swing.JPanel();
        cmbDegreeType = new edu.mit.coeus.utils.CoeusComboBox();
        lblDegreeType = new javax.swing.JLabel();
        txtGraduationDate = new edu.mit.coeus.utils.CoeusTextField();
        lblGraduationDate = new javax.swing.JLabel();
        lblDegree = new javax.swing.JLabel();
        lblFieldOfStudy = new javax.swing.JLabel();
        lblSpecialization = new javax.swing.JLabel();
        txtDegree = new edu.mit.coeus.utils.CoeusTextField();
        txtFieldOfStudy = new edu.mit.coeus.utils.CoeusTextField();
        txtSpecialization = new edu.mit.coeus.utils.CoeusTextField();

        setLayout(new java.awt.GridBagLayout());

        pnlSchoolContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlSchoolContainer.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)), "School", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 11), new java.awt.Color(255, 51, 51)));
        pnlSchoolContainer.setPreferredSize(new java.awt.Dimension(550, 90));
        pnlSchoolContainer.setMinimumSize(new java.awt.Dimension(550, 90));
        pnlSchoolContainer.setMaximumSize(new java.awt.Dimension(550, 90));
        pnlSchool.setLayout(new java.awt.GridBagLayout());

        pnlSchool.setPreferredSize(new java.awt.Dimension(530, 60));
        pnlSchool.setMinimumSize(new java.awt.Dimension(530, 60));
        pnlSchool.setMaximumSize(new java.awt.Dimension(530, 60));
        txtSponsorCode.setPreferredSize(new java.awt.Dimension(175, 23));
        txtSponsorCode.setMaximumSize(new java.awt.Dimension(175, 23));
        txtSponsorCode.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSchool.add(txtSponsorCode, gridBagConstraints);

        cmdIdCode.setPreferredSize(new java.awt.Dimension(175, 20));
        cmdIdCode.setMinimumSize(new java.awt.Dimension(175, 20));
        cmdIdCode.setMaximumSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSchool.add(cmdIdCode, gridBagConstraints);

        txtDegree1.setPreferredSize(new java.awt.Dimension(175, 23));
        txtDegree1.setMaximumSize(new java.awt.Dimension(175, 23));
        txtDegree1.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 7, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSchool.add(txtDegree1, gridBagConstraints);

        lblName.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 7, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlSchool.add(lblName, gridBagConstraints);

        lblIdCode.setText("Id Code:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 28, 0, 0);
        pnlSchool.add(lblIdCode, gridBagConstraints);

        lblId.setText("ID:");
        lblId.setPreferredSize(new java.awt.Dimension(19, 17));
        lblId.setMinimumSize(new java.awt.Dimension(19, 17));
        lblId.setMaximumSize(new java.awt.Dimension(19, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 90, 0, 0);
        pnlSchool.add(lblId, gridBagConstraints);

        pnlSchoolContainer.add(pnlSchool);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(pnlSchoolContainer, gridBagConstraints);

        pnlDegreeInfo.setLayout(new java.awt.GridBagLayout());

        pnlDegreeInfo.setPreferredSize(new java.awt.Dimension(625, 115));
        pnlDegreeInfo.setMinimumSize(new java.awt.Dimension(625, 115));
        pnlDegreeInfo.setMaximumSize(new java.awt.Dimension(625, 115));
        cmbDegreeType.setPreferredSize(new java.awt.Dimension(175, 20));
        cmbDegreeType.setMinimumSize(new java.awt.Dimension(175, 20));
        cmbDegreeType.setMaximumSize(new java.awt.Dimension(175, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 6, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDegreeInfo.add(cmbDegreeType, gridBagConstraints);

        lblDegreeType.setText("Degree Type:");
        lblDegreeType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDegreeType.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 6, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlDegreeInfo.add(lblDegreeType, gridBagConstraints);

        txtGraduationDate.setPreferredSize(new java.awt.Dimension(175, 23));
        txtGraduationDate.setMaximumSize(new java.awt.Dimension(175, 23));
        txtGraduationDate.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 6, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlDegreeInfo.add(txtGraduationDate, gridBagConstraints);

        lblGraduationDate.setText("Graduation Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 17, 6, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlDegreeInfo.add(lblGraduationDate, gridBagConstraints);

        lblDegree.setText("Degree:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlDegreeInfo.add(lblDegree, gridBagConstraints);

        lblFieldOfStudy.setText("Field Of Study:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlDegreeInfo.add(lblFieldOfStudy, gridBagConstraints);

        lblSpecialization.setText("Specialization:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlDegreeInfo.add(lblSpecialization, gridBagConstraints);

        txtDegree.setPreferredSize(new java.awt.Dimension(175, 23));
        txtDegree.setMaximumSize(new java.awt.Dimension(175, 23));
        txtDegree.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 6, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDegreeInfo.add(txtDegree, gridBagConstraints);

        txtFieldOfStudy.setPreferredSize(new java.awt.Dimension(175, 23));
        txtFieldOfStudy.setMaximumSize(new java.awt.Dimension(175, 23));
        txtFieldOfStudy.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 6, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDegreeInfo.add(txtFieldOfStudy, gridBagConstraints);

        txtSpecialization.setPreferredSize(new java.awt.Dimension(175, 23));
        txtSpecialization.setMaximumSize(new java.awt.Dimension(175, 23));
        txtSpecialization.setMinimumSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 6, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDegreeInfo.add(txtSpecialization, gridBagConstraints);

        add(pnlDegreeInfo, new java.awt.GridBagConstraints());

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblName;
    private edu.mit.coeus.utils.CoeusTextField txtGraduationDate;
    private javax.swing.JLabel lblSpecialization;
    private javax.swing.JLabel lblDegree;
    private javax.swing.JLabel lblFieldOfStudy;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblIdCode;
    private edu.mit.coeus.utils.CoeusTextField txtDegree;
    private edu.mit.coeus.utils.CoeusTextField txtDegree1;
    private javax.swing.JLabel lblGraduationDate;
    private javax.swing.JLabel lblDegreeType;
    private javax.swing.JPanel pnlSchool;
    private edu.mit.coeus.utils.CoeusComboBox cmdIdCode;
    private javax.swing.JPanel pnlSchoolContainer;
    private edu.mit.coeus.utils.CoeusTextField txtSponsorCode;
    private edu.mit.coeus.utils.CoeusTextField txtSpecialization;
    private edu.mit.coeus.utils.CoeusComboBox cmbDegreeType;
    private edu.mit.coeus.utils.CoeusTextField txtFieldOfStudy;
    private javax.swing.JPanel pnlDegreeInfo;
    // End of variables declaration//GEN-END:variables
    
}