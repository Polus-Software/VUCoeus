/*
 * SubcontractCloseoutForm.java
 *
 * Created on September 6, 2003, 12:22 PM
 */

package edu.mit.coeus.subcontract.gui;

import edu.mit.coeus.gui.*;
/**
 *
 * @author  nadhgj
 */
public class SubcontractCloseoutForm extends javax.swing.JPanel {
    
    /** Creates new form SubcontractCloseoutForm */
    public SubcontractCloseoutForm() {
        initComponents();
    }
    
    
    public javax.swing.JTable getCloseoutTableInstance(){
        return tblCloseout;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
	private void initComponents() {//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;
		
		lblComments = new javax.swing.JLabel();
		jcrPnlCloseout = new javax.swing.JScrollPane();
		tblCloseout = new javax.swing.JTable(){
			public void changeSelection(int row, int column, boolean toggle, boolean extend){
				super.changeSelection(row, column, toggle, extend);
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						getCloseoutTableInstance().dispatchEvent(new java.awt.event.KeyEvent(
						getCloseoutTableInstance() ,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
						java.awt.event.KeyEvent.CHAR_UNDEFINED) );
					}
				});
			}
		};
		jcrPnlComments = new javax.swing.JScrollPane();
		txtComments = new javax.swing.JTextArea();
		btnAdd = new javax.swing.JButton();
		btnDelete = new javax.swing.JButton();
		
		setLayout(new java.awt.GridBagLayout());
		
		lblComments.setFont(CoeusFontFactory.getLabelFont());
		lblComments.setText("Comments:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(lblComments, gridBagConstraints);
		
		jcrPnlCloseout.setMinimumSize(new java.awt.Dimension(910, 380));
		jcrPnlCloseout.setPreferredSize(new java.awt.Dimension(910, 380));
		jcrPnlCloseout.setRequestFocusEnabled(false);
		tblCloseout.setFont(CoeusFontFactory.getNormalFont());
		tblCloseout.setModel(new javax.swing.table.DefaultTableModel(
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
		tblCloseout.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				tblCloseoutMouseClicked(evt);
			}
		});
		
		jcrPnlCloseout.setViewportView(tblCloseout);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridheight = 8;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(jcrPnlCloseout, gridBagConstraints);
		
		jcrPnlComments.setMinimumSize(new java.awt.Dimension(910, 135));
		jcrPnlComments.setPreferredSize(new java.awt.Dimension(910, 135));
		txtComments.setFont(CoeusFontFactory.getNormalFont());
		txtComments.setLineWrap(true);
		txtComments.setWrapStyleWord(true);
		jcrPnlComments.setViewportView(txtComments);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
		add(jcrPnlComments, gridBagConstraints);
		
		btnAdd.setFont(CoeusFontFactory.getLabelFont());
		btnAdd.setMnemonic('A');
		btnAdd.setText("Add");
		btnAdd.setMinimumSize(new java.awt.Dimension(70, 26));
		btnAdd.setPreferredSize(new java.awt.Dimension(70, 26));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 5);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		add(btnAdd, gridBagConstraints);
		
		btnDelete.setFont(CoeusFontFactory.getLabelFont());
		btnDelete.setMnemonic('D');
		btnDelete.setText("Delete");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 5);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		add(btnDelete, gridBagConstraints);
		
	}//GEN-END:initComponents

    private void tblCloseoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCloseoutMouseClicked
        // Add your handling code here:
        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                tblCloseout.dispatchEvent(new java.awt.event.KeyEvent(
                tblCloseout ,java.awt.event.KeyEvent.KEY_PRESSED,0,0,java.awt.event.KeyEvent.VK_F2,
                java.awt.event.KeyEvent.CHAR_UNDEFINED) );
            }
        });
    }//GEN-LAST:event_tblCloseoutMouseClicked
    
    
	// Variables declaration - do not modify//GEN-BEGIN:variables
	public javax.swing.JButton btnAdd;
	public javax.swing.JButton btnDelete;
	public javax.swing.JScrollPane jcrPnlCloseout;
	public javax.swing.JScrollPane jcrPnlComments;
	public javax.swing.JLabel lblComments;
	public javax.swing.JTable tblCloseout;
	public javax.swing.JTextArea txtComments;
	// End of variables declaration//GEN-END:variables
    
}
