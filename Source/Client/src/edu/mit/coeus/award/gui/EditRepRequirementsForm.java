/*
 * EditRepRequirementsForm.java
 *
 * Created on July 14, 2004, 2:26 PM
 */

package edu.mit.coeus.award.gui;

import javax.swing.*;

import edu.mit.coeus.gui.*;

import edu.mit.coeus.utils.CoeusGuiConstants;


/**
 *
 * @author  ajaygm
 */
public class EditRepRequirementsForm extends javax.swing.JComponent {
    
    /** Creates new form EditRepRequirementsForm */
    public EditRepRequirementsForm() {
        initComponents();
    }
    
    public static void main(String s[]){
        JFrame frame = new JFrame("Edit reporting Requirements");
        EditRepRequirementsForm editRepRequirementsForm = new EditRepRequirementsForm();
        frame.getContentPane().add(editRepRequirementsForm);
        frame.setSize(575, 225);
        frame.show();
     }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
	private void initComponents() {//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;
		
		lblStatus = new javax.swing.JLabel();
		lblActivityDate = new javax.swing.JLabel();
		lblPerson = new javax.swing.JLabel();
		lblComments = new javax.swing.JLabel();
		lblOverdueCounter = new javax.swing.JLabel();
		cmbStatus = new edu.mit.coeus.utils.CoeusComboBox();
		txtActivityDate = new edu.mit.coeus.utils.CoeusTextField();
		txtPerson = new edu.mit.coeus.utils.CoeusTextField();
		txtOverdueCounter = new edu.mit.coeus.utils.CoeusTextField();
		scrPnCOmments = new javax.swing.JScrollPane();
		txtArComments = new javax.swing.JTextArea();
		btnOK = new javax.swing.JButton();
		btnCancel = new javax.swing.JButton();
		btnPersonSearch = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		
		setLayout(new java.awt.GridBagLayout());
		
		lblStatus.setFont(CoeusFontFactory.getLabelFont());
		lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblStatus.setText("Status: ");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
		add(lblStatus, gridBagConstraints);
		
		lblActivityDate.setFont(CoeusFontFactory.getLabelFont());
		lblActivityDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblActivityDate.setText("Activity Date: ");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(lblActivityDate, gridBagConstraints);
		
		lblPerson.setFont(CoeusFontFactory.getLabelFont());
		lblPerson.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblPerson.setText("Person: ");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(lblPerson, gridBagConstraints);
		
		lblComments.setFont(CoeusFontFactory.getLabelFont());
		lblComments.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblComments.setText("Comments: ");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
		add(lblComments, gridBagConstraints);
		
		lblOverdueCounter.setFont(CoeusFontFactory.getLabelFont());
		lblOverdueCounter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblOverdueCounter.setText("Overdue Counter: ");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(lblOverdueCounter, gridBagConstraints);
		
		cmbStatus.setMinimumSize(new java.awt.Dimension(400, 19));
		cmbStatus.setPreferredSize(new java.awt.Dimension(320, 19));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
		add(cmbStatus, gridBagConstraints);
		
		txtActivityDate.setFont(CoeusFontFactory.getNormalFont());
		txtActivityDate.setMinimumSize(new java.awt.Dimension(150, 21));
		txtActivityDate.setPreferredSize(new java.awt.Dimension(150, 21));
		txtActivityDate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtActivityDateActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		add(txtActivityDate, gridBagConstraints);
		
		txtPerson.setFont(CoeusFontFactory.getNormalFont());
		txtPerson.setMinimumSize(new java.awt.Dimension(240, 21));
		txtPerson.setPreferredSize(new java.awt.Dimension(300, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(txtPerson, gridBagConstraints);
		
		txtOverdueCounter.setFont(CoeusFontFactory.getNormalFont());
		txtOverdueCounter.setMinimumSize(new java.awt.Dimension(150, 21));
		txtOverdueCounter.setPreferredSize(new java.awt.Dimension(100, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(txtOverdueCounter, gridBagConstraints);
		
		txtArComments.setFont(CoeusFontFactory.getNormalFont());
		txtArComments.setLineWrap(true);
		scrPnCOmments.setViewportView(txtArComments);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 7;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
		add(scrPnCOmments, gridBagConstraints);
		
		btnOK.setFont(CoeusFontFactory.getLabelFont());
		btnOK.setMnemonic('O');
		btnOK.setText("OK");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
		add(btnOK, gridBagConstraints);
		
		btnCancel.setFont(CoeusFontFactory.getLabelFont());
		btnCancel.setMnemonic('C');
		btnCancel.setText("Cancel");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
		add(btnCancel, gridBagConstraints);
		
		btnPersonSearch.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.SEARCH_ICON)));
		btnPersonSearch.setMinimumSize(new java.awt.Dimension(23, 23));
		btnPersonSearch.setPreferredSize(new java.awt.Dimension(23, 23));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(btnPersonSearch, gridBagConstraints);
		
		jLabel1.setMinimumSize(new java.awt.Dimension(73, 10));
		jLabel1.setPreferredSize(new java.awt.Dimension(73, 10));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jLabel1, gridBagConstraints);
		
	}//GEN-END:initComponents

    private void txtActivityDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtActivityDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtActivityDateActionPerformed
    
    
	// Variables declaration - do not modify//GEN-BEGIN:variables
	public javax.swing.JButton btnCancel;
	public javax.swing.JButton btnOK;
	public javax.swing.JButton btnPersonSearch;
	public edu.mit.coeus.utils.CoeusComboBox cmbStatus;
	public javax.swing.JLabel jLabel1;
	public javax.swing.JLabel lblActivityDate;
	public javax.swing.JLabel lblComments;
	public javax.swing.JLabel lblOverdueCounter;
	public javax.swing.JLabel lblPerson;
	public javax.swing.JLabel lblStatus;
	public javax.swing.JScrollPane scrPnCOmments;
	public edu.mit.coeus.utils.CoeusTextField txtActivityDate;
	public javax.swing.JTextArea txtArComments;
	public edu.mit.coeus.utils.CoeusTextField txtOverdueCounter;
	public edu.mit.coeus.utils.CoeusTextField txtPerson;
	// End of variables declaration//GEN-END:variables
    
}