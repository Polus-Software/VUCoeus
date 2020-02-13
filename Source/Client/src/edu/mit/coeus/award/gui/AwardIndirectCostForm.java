/*
 * AwardIndirectCostForm.java
 *
 * Created on May 25, 2004, 5:05 PM
 */

package edu.mit.coeus.award.gui;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.LimitedPlainDocument;

/**
 *
 * @author  arunmp
 */
public class AwardIndirectCostForm extends javax.swing.JPanel {
    
    /** Creates new form AwardIndirectCostForm */
    public AwardIndirectCostForm() {
        initComponents();
		tblTotal.setOpaque(false);
		txtArComments.setDocument(new LimitedPlainDocument(3878));
	}
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
	private void initComponents() {//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;
		
		scrPnAwardIndirectCost = new javax.swing.JScrollPane();
		tblAwardIndirectCost = new javax.swing.JTable(){
			public void changeSelection(int row, int column, boolean toggle, boolean extend){
				super.changeSelection(row, column, toggle, extend);
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						tblAwardIndirectCost.dispatchEvent(new java.awt.event.KeyEvent(
						tblAwardIndirectCost,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
						java.awt.event.KeyEvent.CHAR_UNDEFINED) );
					}
				});
			}
		};
		scrpntxtArComments = new javax.swing.JScrollPane();
		txtArComments = new javax.swing.JTextArea();
		btnOk = new javax.swing.JButton();
		btnCancel = new javax.swing.JButton();
		btnAdd = new javax.swing.JButton();
		btnDelete = new javax.swing.JButton();
		btnRates = new javax.swing.JButton();
		awardHeaderForm1 = new edu.mit.coeus.award.gui.AwardHeaderForm();
		lblComments = new javax.swing.JLabel();
		pnlTotal = new javax.swing.JPanel();
		tblTotal = new javax.swing.JTable();
		
		setLayout(new java.awt.GridBagLayout());
		
		scrPnAwardIndirectCost.setMinimumSize(new java.awt.Dimension(400, 300));
		scrPnAwardIndirectCost.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				scrPnAwardIndirectCostMouseClicked(evt);
			}
		});
		
		tblAwardIndirectCost.setModel(new javax.swing.table.DefaultTableModel(
		new Object [][] {
			{null, null, null, null},
			{null, null, null, null},
			{null, null, null, null},
			{null, null, null, null}
		},
		new String [] {
			"Title 1", "Title 2", "Title 3", "Title 4"
		}
		));
		tblAwardIndirectCost.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				tblAwardIndirectCostMouseClicked(evt);
			}
		});
		
		scrPnAwardIndirectCost.setViewportView(tblAwardIndirectCost);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridheight = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 0.1;
		add(scrPnAwardIndirectCost, gridBagConstraints);
		
		scrpntxtArComments.setMinimumSize(new java.awt.Dimension(22, 500));
		scrpntxtArComments.setPreferredSize(new java.awt.Dimension(10, 500));
		txtArComments.setFont(CoeusFontFactory.getNormalFont());
		txtArComments.setLineWrap(true);
		txtArComments.setRows(10);
		txtArComments.setWrapStyleWord(true);
		scrpntxtArComments.setViewportView(txtArComments);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weighty = 0.1;
		add(scrpntxtArComments, gridBagConstraints);
		
		btnOk.setFont(CoeusFontFactory.getLabelFont());
		btnOk.setMnemonic('O');
		btnOk.setText("OK");
		btnOk.setMaximumSize(new java.awt.Dimension(80, 25));
		btnOk.setMinimumSize(new java.awt.Dimension(80, 25));
		btnOk.setPreferredSize(new java.awt.Dimension(80, 25));
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnOkActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		add(btnOk, gridBagConstraints);
		
		btnCancel.setFont(CoeusFontFactory.getLabelFont());
		btnCancel.setMnemonic('C');
		btnCancel.setText("Cancel");
		btnCancel.setMaximumSize(new java.awt.Dimension(80, 25));
		btnCancel.setMinimumSize(new java.awt.Dimension(80, 25));
		btnCancel.setPreferredSize(new java.awt.Dimension(80, 25));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		add(btnCancel, gridBagConstraints);
		
		btnAdd.setFont(CoeusFontFactory.getLabelFont());
		btnAdd.setMnemonic('A');
		btnAdd.setText("Add");
		btnAdd.setMaximumSize(new java.awt.Dimension(80, 25));
		btnAdd.setMinimumSize(new java.awt.Dimension(80, 25));
		btnAdd.setPreferredSize(new java.awt.Dimension(80, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		add(btnAdd, gridBagConstraints);
		
		btnDelete.setFont(CoeusFontFactory.getLabelFont());
		btnDelete.setMnemonic('D');
		btnDelete.setText("Delete");
		btnDelete.setMaximumSize(new java.awt.Dimension(80, 25));
		btnDelete.setMinimumSize(new java.awt.Dimension(80, 25));
		btnDelete.setPreferredSize(new java.awt.Dimension(80, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		add(btnDelete, gridBagConstraints);
		
		btnRates.setFont(CoeusFontFactory.getLabelFont());
		btnRates.setMnemonic('R');
		btnRates.setText("Rates");
		btnRates.setMaximumSize(new java.awt.Dimension(80, 25));
		btnRates.setMinimumSize(new java.awt.Dimension(80, 25));
		btnRates.setPreferredSize(new java.awt.Dimension(80, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(25, 5, 0, 0);
		add(btnRates, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(awardHeaderForm1, gridBagConstraints);
		
		lblComments.setFont(CoeusFontFactory.getLabelFont());
		lblComments.setText("Comments");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
		add(lblComments, gridBagConstraints);
		
		tblTotal.setFont(CoeusFontFactory.getLabelFont());
		tblTotal.setModel(new javax.swing.table.DefaultTableModel(
		new Object [][] {
			
		},
		new String [] {
			
		}
		));
		tblTotal.setFocusable(false);
		tblTotal.setGridColor(new java.awt.Color(204, 204, 204));
		tblTotal.setRowHeight(25);
		tblTotal.setRowMargin(2);
		tblTotal.setRowSelectionAllowed(false);
		tblTotal.setSelectionBackground(new java.awt.Color(204, 204, 204));
		tblTotal.setShowHorizontalLines(false);
		tblTotal.setShowVerticalLines(false);
		tblTotal.setEnabled(false);
		tblTotal.setOpaque(false);
		pnlTotal.add(tblTotal);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 0.1;
		add(pnlTotal, gridBagConstraints);
		
	}//GEN-END:initComponents

	private void tblAwardIndirectCostMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAwardIndirectCostMouseClicked
		// TODO add your handling code here:
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                tblAwardIndirectCost.dispatchEvent(new java.awt.event.KeyEvent(
                tblAwardIndirectCost,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                java.awt.event.KeyEvent.CHAR_UNDEFINED) );
            }
        });
	}//GEN-LAST:event_tblAwardIndirectCostMouseClicked

	private void scrPnAwardIndirectCostMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrPnAwardIndirectCostMouseClicked
		// TODO add your handling code here:
	}//GEN-LAST:event_scrPnAwardIndirectCostMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnOkActionPerformed
    

	// Variables declaration - do not modify//GEN-BEGIN:variables
	public edu.mit.coeus.award.gui.AwardHeaderForm awardHeaderForm1;
	public javax.swing.JButton btnAdd;
	public javax.swing.JButton btnCancel;
	public javax.swing.JButton btnDelete;
	public javax.swing.JButton btnOk;
	public javax.swing.JButton btnRates;
	public javax.swing.JLabel lblComments;
	public javax.swing.JPanel pnlTotal;
	public javax.swing.JScrollPane scrPnAwardIndirectCost;
	public javax.swing.JScrollPane scrpntxtArComments;
	public javax.swing.JTable tblAwardIndirectCost;
	public javax.swing.JTable tblTotal;
	public javax.swing.JTextArea txtArComments;
	// End of variables declaration//GEN-END:variables
    
	
}
