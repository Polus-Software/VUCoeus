/*
 * ProposalPersonDegreeForm.java
 *
 * Created on April 1, 2003, 8:52 AM
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
public class ProposalPersonDegreeForm extends javax.swing.JComponent {
    
    /** Creates new form ProposalPersonDegreeForm */
    public ProposalPersonDegreeForm() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlProposalDescriptionContainer = new javax.swing.JPanel();
        pnlProposalDescription = new javax.swing.JPanel();
        lblProposalNo = new javax.swing.JLabel();
        lblProposalValue = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblSponsorValue = new javax.swing.JLabel();
        pnlProposalPersondegreeInfo = new javax.swing.JPanel();
        scrPnProposalPersonDegreeInfo = new javax.swing.JScrollPane();
        tblProposalPersonDegreeInfo = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        pnlProposalDescriptionContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlProposalDescriptionContainer.setPreferredSize(new java.awt.Dimension(585, 50));
        pnlProposalDescriptionContainer.setMinimumSize(new java.awt.Dimension(585, 50));
        pnlProposalDescriptionContainer.setMaximumSize(new java.awt.Dimension(585, 50));
        pnlProposalDescription.setLayout(new java.awt.GridBagLayout());

        lblProposalNo.setText("Proposal Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlProposalDescription.add(lblProposalNo, gridBagConstraints);

        lblProposalValue.setText("xxxxxxxx");
        lblProposalValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblProposalValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlProposalDescription.add(lblProposalValue, gridBagConstraints);

        lblSponsor.setText("Sponsor:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlProposalDescription.add(lblSponsor, gridBagConstraints);

        lblSponsorValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSponsorValue.setPreferredSize(new java.awt.Dimension(140, 17));
        lblSponsorValue.setMinimumSize(new java.awt.Dimension(140, 17));
        lblSponsorValue.setMaximumSize(new java.awt.Dimension(140, 17));
        lblSponsorValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlProposalDescription.add(lblSponsorValue, gridBagConstraints);

        pnlProposalDescriptionContainer.add(pnlProposalDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(pnlProposalDescriptionContainer, gridBagConstraints);

        pnlProposalPersondegreeInfo.setPreferredSize(new java.awt.Dimension(610, 400));
        pnlProposalPersondegreeInfo.setMinimumSize(new java.awt.Dimension(610, 400));
        pnlProposalPersondegreeInfo.setMaximumSize(new java.awt.Dimension(610, 400));
        scrPnProposalPersonDegreeInfo.setBorder(new javax.swing.border.EtchedBorder());
        scrPnProposalPersonDegreeInfo.setPreferredSize(new java.awt.Dimension(600, 350));
        scrPnProposalPersonDegreeInfo.setMinimumSize(new java.awt.Dimension(600, 350));
        scrPnProposalPersonDegreeInfo.setMaximumSize(new java.awt.Dimension(600, 350));
        tblProposalPersonDegreeInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DegreeType", "Graduation Date", "Degree", "School"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProposalPersonDegreeInfo.setFont(CoeusFontFactory.getNormalFont());
        tblProposalPersonDegreeInfo.setPreferredSize(new java.awt.Dimension(585, 275));
        tblProposalPersonDegreeInfo.setSelectionForeground(java.awt.Color.black);
        tblProposalPersonDegreeInfo.setRowHeight(20);
        tblProposalPersonDegreeInfo.setShowVerticalLines(false);
        tblProposalPersonDegreeInfo.setMaximumSize(new java.awt.Dimension(585, 275));
        tblProposalPersonDegreeInfo.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tblProposalPersonDegreeInfo.setPreferredScrollableViewportSize(null);
        tblProposalPersonDegreeInfo.setShowHorizontalLines(false);
        tblProposalPersonDegreeInfo.setMinimumSize(new java.awt.Dimension(585, 275));
        tblProposalPersonDegreeInfo.setOpaque(false);
        scrPnProposalPersonDegreeInfo.setViewportView(tblProposalPersonDegreeInfo);

        pnlProposalPersondegreeInfo.add(scrPnProposalPersonDegreeInfo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(pnlProposalPersondegreeInfo, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnOk.setMnemonic('o');
        btnOk.setText("OK");
        btnOk.setPreferredSize(new java.awt.Dimension(81, 27));
        btnOk.setMaximumSize(new java.awt.Dimension(81, 27));
        btnOk.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(81, 27));
        btnCancel.setMaximumSize(new java.awt.Dimension(81, 27));
        btnCancel.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 17, 2);
        pnlButtons.add(btnCancel, gridBagConstraints);

        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setPreferredSize(new java.awt.Dimension(81, 27));
        btnAdd.setMaximumSize(new java.awt.Dimension(81, 27));
        btnAdd.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnAdd, gridBagConstraints);

        btnModify.setMnemonic('M');
        btnModify.setText("Modify");
        btnModify.setPreferredSize(new java.awt.Dimension(81, 27));
        btnModify.setMaximumSize(new java.awt.Dimension(81, 27));
        btnModify.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnModify, gridBagConstraints);

        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setPreferredSize(new java.awt.Dimension(81, 27));
        btnDelete.setMaximumSize(new java.awt.Dimension(81, 27));
        btnDelete.setMinimumSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        pnlButtons.add(btnDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(pnlButtons, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblProposalNo;
    private javax.swing.JPanel pnlProposalDescriptionContainer;
    private javax.swing.JPanel pnlProposalDescription;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblSponsor;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JButton btnAdd;
    private javax.swing.JLabel lblProposalValue;
    private javax.swing.JButton btnModify;
    private javax.swing.JTable tblProposalPersonDegreeInfo;
    private javax.swing.JButton btnCancel;
    private javax.swing.JScrollPane scrPnProposalPersonDegreeInfo;
    private javax.swing.JPanel pnlProposalPersondegreeInfo;
    private javax.swing.JLabel lblSponsorValue;
    // End of variables declaration//GEN-END:variables
    
}