/*
 * PanelRow.java
 *
 * Created on January 14, 2004, 6:27 PM
 */

package edu.mit.coeus.routing.gui;
//import java.util.Vector;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import javax.swing.ImageIcon;
import java.awt.Color; 
import java.awt.Font;
import java.awt.Dimension;
import edu.mit.coeus.propdev.bean.ProposalApprovalBean; 

//For Bug Fix:723 
import  javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
//For Bug Fix:723 

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */
public class RoutingPanelRow extends javax.swing.JPanel {
    
    //private ProposalApprovalBean proposalApprovalBean ;
    private RoutingDetailsBean routingDetailsBean ;
    private ImageIcon imageIcon;
    private int alignment;
    private Color color;
    private Font font; 
    
//    private Dimension userIDDimension = new Dimension(150, 20);
//    private Dimension userNameDimension = new Dimension(100, 20);
//    private Dimension statusDimension = new Dimension(120, 20);
//    private Dimension timeStampDimension = new Dimension(90, 20);
      
    //Modified for Case#4602 -Proposal routing window too small  - Start
//    private Dimension userIDDimension = new Dimension(120, 20);
//    private Dimension userNameDimension = new Dimension(100, 20);
//    private Dimension statusDimension = new Dimension(120, 20);
//    private Dimension timeStampDimension = new Dimension(80, 20); 
    
    private Dimension userIDDimension = new Dimension(180, 20);
    private Dimension userNameDimension = new Dimension(150, 20);
    private Dimension statusDimension = new Dimension(120, 20);
    private Dimension timeStampDimension = new Dimension(150, 20);    
    //Case#4602 - End
    
       
        
    /** Creates new form PanelRow */
    public RoutingPanelRow() {
        initComponents();
    }
    
    /** Creates new form PanelRow */
    public RoutingPanelRow(boolean containsSequentialData) {
        
        if(containsSequentialData == false) {
            //Modified for Case#4602 -Proposal routing window too small  - Start
//            userIDDimension = new Dimension(100, 20);
//            userNameDimension = new Dimension(100, 20);
//            statusDimension = new Dimension(70, 20);
//            timeStampDimension = new Dimension(70, 20);
            userIDDimension = new Dimension(160, 20);
            userNameDimension = new Dimension(130, 20);
            statusDimension = new Dimension(70, 20);
            timeStampDimension = new Dimension(130, 20);
            //Case#4602 - End
        }
            
            
        initComponents();
    }
    
    public void setRowIcon(ImageIcon imageIcon) {
        lblUserID.setIcon(imageIcon);
    }
    
    public ImageIcon getRowIcon() {
        return imageIcon;
    }
     
    public void setUserID(String userId,int alignment,Color color,Font font ) {
       this.alignment = alignment;
       this.color = color;
       this.font= font;
       lblUserID.setText(userId);
       
       //Commented for Bug Fix:723
       //lblUserID.setHorizontalAlignment(alignment);
       
       lblUserID.setForeground(color);
       lblUserID.setFont(font);
       
       //Bug Fix:723 Start 
       if(alignment == JLabel.CENTER) {
           GridBagLayout layout = (GridBagLayout)this.getLayout();
           GridBagConstraints gridBagConstraints = layout.getConstraints(lblUserID);
           gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
           layout.removeLayoutComponent(lblUserID);
           add(lblUserID, gridBagConstraints);
       }else{
           GridBagLayout layout = (GridBagLayout)this.getLayout();
           GridBagConstraints gridBagConstraints = layout.getConstraints(lblUserName);
           gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
           layout.removeLayoutComponent(lblUserName);
           add(lblUserName, gridBagConstraints);
       }
       //Bug Fix:723 End
       
    }
    
    
    public void setUserName(String userName) {
       lblUserName.setText(userName);
    }
     
    public void setApprovalStatus(String approvalStatus) {
       lblStatus.setText(approvalStatus);
    }
    
    public void setTimeStamp(String timeStamp) {
       lblTimeStamp.setText(timeStamp);
    }
    
    public int getUserIdAlignment() {
        return alignment;
    }
    
    public Color getLabelColor() {
     return color;   
    }
    
    public Font getLabelFont() {
     return font;   
    }
    
    public void setAlignment(int column,int aligment) {
        
       if(column == 0){
           lblUserID.setHorizontalAlignment(aligment);
       }
       if(column == 1){
           lblUserName.setHorizontalAlignment(aligment);
       }
       if(column == 2){
           lblStatus.setHorizontalAlignment(aligment);
       }
       if(column == 3){
           lblTimeStamp.setHorizontalAlignment(aligment);
       }
    }
   
    public void showStatus(boolean show){
        if( show ){
            lblStatus.setMaximumSize(statusDimension);
            lblStatus.setMinimumSize(statusDimension);
            lblStatus.setPreferredSize(statusDimension);
            lblUserName.setBorder(null);
        }else{
            lblStatus.setMaximumSize(new Dimension(0,0));
            lblStatus.setMinimumSize(new Dimension(0,0));
            lblStatus.setPreferredSize(new Dimension(0,0));
            lblUserName.setBorder(new javax.swing.border.EmptyBorder(0, 8, 0, 0));
        }
        
    }
    
     /** Getter for property routingDetailsBean.
     * @return Value of property routingDetailsBean.
     *
     */
    public RoutingDetailsBean getRoutingDetailsBean() {
        return routingDetailsBean;
    }
    
    /** Setter for property routingDetailsBean.
     * @param routingDetailsBean New value of property routingDetailsBean.
     *
     */
    public void setRoutingDetailsBean(RoutingDetailsBean routingDetailsBean) {
        this.routingDetailsBean = routingDetailsBean;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblUserID = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblTimeStamp = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        //lblUserID.setIcon(new javax.swing.ImageIcon("")); // JM 5-21-2015 this throws nullpointerexception in 1.8
        lblUserID.setMaximumSize(userIDDimension);
        lblUserID.setMinimumSize(userIDDimension);
        lblUserID.setPreferredSize(userIDDimension);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblUserID, gridBagConstraints);

        lblUserName.setBackground(new java.awt.Color(255, 255, 255));
        lblUserName.setMaximumSize(userNameDimension);
        lblUserName.setMinimumSize(userNameDimension);
        lblUserName.setPreferredSize(userNameDimension);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblUserName, gridBagConstraints);

        lblStatus.setMaximumSize(statusDimension);
        lblStatus.setMinimumSize(statusDimension);
        lblStatus.setPreferredSize(statusDimension);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblStatus, gridBagConstraints);

        lblTimeStamp.setBackground(new java.awt.Color(255, 255, 255));
        lblTimeStamp.setMaximumSize(timeStampDimension);
        lblTimeStamp.setMinimumSize(timeStampDimension);
        lblTimeStamp.setPreferredSize(timeStampDimension);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(lblTimeStamp, gridBagConstraints);

    }//GEN-END:initComponents

   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel lblStatus;
    public javax.swing.JLabel lblTimeStamp;
    public javax.swing.JLabel lblUserID;
    public javax.swing.JLabel lblUserName;
    // End of variables declaration//GEN-END:variables
    
}
