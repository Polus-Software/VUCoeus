/*
 * IPReviewDialogForm.java
 *
 * Created on May 11, 2004, 3:24 PM
 */

package edu.mit.coeus.instprop.gui;

/**
 *
 * @author  bijosht
 */
public class IPReviewDialogForm extends javax.swing.JComponent {
    
    /** Creates new form IPReviewDialogForm */
    public IPReviewDialogForm() {
        initComponents();
        postInitComonents();
    }
    private void postInitComonents() {
        ProposalDetailsForm proposalDetailsForm =new ProposalDetailsForm();
        IPReviewForm iPReviewForm = new IPReviewForm();
        //add(proposalDetailsForm);
        //add(iPReviewForm);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        iPReviewForm = new edu.mit.coeus.instprop.gui.IPReviewForm();
        proposalDetailsForm = new edu.mit.coeus.instprop.gui.ProposalDetailsForm();

        setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(iPReviewForm, gridBagConstraints);

        add(proposalDetailsForm, new java.awt.GridBagConstraints());

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.instprop.gui.IPReviewForm iPReviewForm;
    public edu.mit.coeus.instprop.gui.ProposalDetailsForm proposalDetailsForm;
    // End of variables declaration//GEN-END:variables
    
}
