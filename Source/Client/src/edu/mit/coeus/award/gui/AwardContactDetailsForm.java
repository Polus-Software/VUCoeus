/*
 * AwardContactDetailsForm.java
 *
 * Created on April 26, 2004, 11:25 AM
 */

package edu.mit.coeus.award.gui;

import edu.mit.coeus.admin.bean.AwardTemplateContactsBean;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.event.Controller;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import javax.swing.text.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.award.bean.AwardBaseBean.*;
import edu.mit.coeus.exception.*;
import javax.swing.event.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.gui.CoeusSearch;



/**
 *
 * @author  ajaygm
 */
public class AwardContactDetailsForm extends javax.swing.JComponent {
    AwardContactDetailsBean awardContactDetailsBean;
    
    String EMPTY_STRING = "";
    
    public AwardContactDetailsForm(){
        initComponents();
    }
    
    public void setFormData(AwardContactDetailsBean
    awardContactDetailsBean) {
        CoeusVector cvContactDetails = new CoeusVector();
        String name;
        if( awardContactDetailsBean == null ) {
            resetData();
            return ;
        }
        String firstName = awardContactDetailsBean.getFirstName() == null ?EMPTY_STRING:awardContactDetailsBean.getFirstName() ;
        if ( firstName.length() > 0) {
            String suffix = (awardContactDetailsBean.getSuffix() == null ?EMPTY_STRING:awardContactDetailsBean.getSuffix());
            String prefix = (awardContactDetailsBean.getPrefix() == null ?EMPTY_STRING:awardContactDetailsBean.getPrefix());
            String middleName = (awardContactDetailsBean.getMiddleName() == null ?EMPTY_STRING:awardContactDetailsBean.getMiddleName());
            //Bug Fix:1505 Start
            name = (awardContactDetailsBean.getLastName()+" "+
            suffix+", "+ prefix+" "+ firstName+" "+
            middleName).trim();
            txtName.setText(name);
        } else {
            txtName.setText(awardContactDetailsBean.getOrganization());
        }
        
        
        //txtName.setText(name);
        txtAddress1.setText(awardContactDetailsBean.getAddress1());
        txtAddress2.setText(awardContactDetailsBean.getAddress2());
        txtAddress3.setText(awardContactDetailsBean.getAddress3());
        txtCity.setText(awardContactDetailsBean.getCity());
        lblSponsorName.setText(awardContactDetailsBean.getSponsorName());
        txtCountry.setText(awardContactDetailsBean.getCountryName());
        txtCounty.setText(awardContactDetailsBean.getCounty());
        txtEMail.setText(awardContactDetailsBean.getEmailAddress());
        txtFax.setText(awardContactDetailsBean.getFaxNumber());
        txtOrganisation.setText(awardContactDetailsBean.getOrganization());
        txtPhone.setText(awardContactDetailsBean.getPhoneNumber());
        txtPostalCode.setText(awardContactDetailsBean.getPostalCode());
        if(awardContactDetailsBean.getRolodexId() == -1 || awardContactDetailsBean.getRolodexId() == 0){
            txtRolodexID.setText(EMPTY_STRING);
        }else {
            txtRolodexID.setText(EMPTY_STRING + awardContactDetailsBean.getRolodexId());
        }
        txtState.setText(awardContactDetailsBean.getStateName());
        //txtState.setText(awardContactDetailsBean.getStateName());
        txtSponsor.setText(awardContactDetailsBean.getSponsorCode());
        txtArComments.setText(awardContactDetailsBean.getComments());
        txtArComments.setCaretPosition(0);
        //System.out.println(txtArComments.getText());
    }
    
    /**
     * Method to set the Tmeplate Contact Details Data  to the Form
     * @param AwardTemplateContactsBean
     */    
    public void setTemplateData(AwardTemplateContactsBean awardTemplateContactsBean) {
        CoeusVector cvContactDetails = new CoeusVector();
        String name;
        if( awardTemplateContactsBean == null ) {
            resetData();
            return ;
        }
        String firstName = awardTemplateContactsBean.getFirstName() == null ?EMPTY_STRING:awardTemplateContactsBean.getFirstName() ;
        if ( firstName.length() > 0) {
            String suffix = (awardTemplateContactsBean.getSuffix() == null ?EMPTY_STRING:awardTemplateContactsBean.getSuffix());
            String prefix = (awardTemplateContactsBean.getPrefix() == null ?EMPTY_STRING:awardTemplateContactsBean.getPrefix());
            String middleName = (awardTemplateContactsBean.getMiddleName() == null ?EMPTY_STRING:awardTemplateContactsBean.getMiddleName());
            name = (awardTemplateContactsBean.getLastName()+
            suffix+", "+ prefix+" "+ firstName+" "+
            middleName).trim();
            txtName.setText(name);
        } else {
            txtName.setText(awardTemplateContactsBean.getOrganization());
        }
        
        
        //txtName.setText(name);
        txtAddress1.setText(awardTemplateContactsBean.getAddress1());
        txtAddress2.setText(awardTemplateContactsBean.getAddress2());
        txtAddress3.setText(awardTemplateContactsBean.getAddress3());
        txtCity.setText(awardTemplateContactsBean.getCity());
        lblSponsorName.setText(awardTemplateContactsBean.getSponsorName());
        txtCountry.setText(awardTemplateContactsBean.getCountryName());
        txtCounty.setText(awardTemplateContactsBean.getCounty());
        txtEMail.setText(awardTemplateContactsBean.getEmailAddress());
        txtFax.setText(awardTemplateContactsBean.getFaxNumber());
        txtOrganisation.setText(awardTemplateContactsBean.getOrganization());
        txtPhone.setText(awardTemplateContactsBean.getPhoneNumber());
        txtPostalCode.setText(awardTemplateContactsBean.getPostalCode());
        if(awardTemplateContactsBean.getRolodexId() == -1 || awardTemplateContactsBean.getRolodexId() == 0){
            txtRolodexID.setText(EMPTY_STRING);
        }else {
            txtRolodexID.setText(EMPTY_STRING + awardTemplateContactsBean.getRolodexId());
        }
        txtState.setText(awardTemplateContactsBean.getStateName());
        //txtState.setText(awardContactDetailsBean.getStateName());
        txtSponsor.setText(awardTemplateContactsBean.getSponsorCode());
        txtArComments.setText(awardTemplateContactsBean.getComments());
        txtArComments.setCaretPosition(0);
        //System.out.println(txtArComments.getText());
    }
    
    
    private void resetData() {
        
        txtName.setText(EMPTY_STRING);
        txtRolodexID.setText(EMPTY_STRING);
        txtSponsor.setText(EMPTY_STRING);
        txtOrganisation.setText(EMPTY_STRING);
        lblSponsorName.setText(EMPTY_STRING);
        txtAddress1.setText(EMPTY_STRING);
        txtAddress2.setText(EMPTY_STRING);
        txtAddress3.setText(EMPTY_STRING); 
        txtCity.setText(EMPTY_STRING);
        txtCountry.setText(EMPTY_STRING);
        txtCounty.setText(EMPTY_STRING);
        txtEMail.setText(EMPTY_STRING);
        txtFax.setText(EMPTY_STRING);
        txtPhone.setText(EMPTY_STRING);
        txtState.setText(EMPTY_STRING);
        txtArComments.setText(EMPTY_STRING);
        txtPostalCode.setText(EMPTY_STRING);
        
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblName = new javax.swing.JLabel();
        lblRolodexID = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblOrganisation = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        txtName = new edu.mit.coeus.utils.CoeusTextField();
        txtSponsor = new edu.mit.coeus.utils.CoeusTextField();
        txtOrganisation = new edu.mit.coeus.utils.CoeusTextField();
        txtRolodexID = new edu.mit.coeus.utils.CoeusTextField();
        lblSponsorName = new javax.swing.JLabel();
        txtAddress1 = new edu.mit.coeus.utils.CoeusTextField();
        txtAddress2 = new edu.mit.coeus.utils.CoeusTextField();
        txtAddress3 = new edu.mit.coeus.utils.CoeusTextField();
        lblCity = new javax.swing.JLabel();
        lblCounty = new javax.swing.JLabel();
        lblState = new javax.swing.JLabel();
        lblPostalCode = new javax.swing.JLabel();
        txtCity = new edu.mit.coeus.utils.CoeusTextField();
        txtState = new edu.mit.coeus.utils.CoeusTextField();
        txtCounty = new edu.mit.coeus.utils.CoeusTextField();
        txtPostalCode = new edu.mit.coeus.utils.CoeusTextField();
        lblCountry = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        lblEMail = new javax.swing.JLabel();
        lblFax = new javax.swing.JLabel();
        txtCountry = new edu.mit.coeus.utils.CoeusTextField();
        txtEMail = new edu.mit.coeus.utils.CoeusTextField();
        txtPhone = new edu.mit.coeus.utils.CoeusTextField();
        txtFax = new edu.mit.coeus.utils.CoeusTextField();
        lblComments = new javax.swing.JLabel();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        lblLastUpdate = new javax.swing.JLabel();
        txtLastUpdate = new javax.swing.JTextField();
        lblUpdateUser = new javax.swing.JLabel();
        txtUpdateUser = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(531, 305));
        setPreferredSize(new java.awt.Dimension(531, 305));
        lblName.setFont(CoeusFontFactory.getLabelFont());
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblName.setText("Name: ");
        lblName.setMaximumSize(new java.awt.Dimension(35, 16));
        lblName.setMinimumSize(new java.awt.Dimension(35, 16));
        lblName.setPreferredSize(new java.awt.Dimension(35, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(lblName, gridBagConstraints);

        lblRolodexID.setFont(CoeusFontFactory.getLabelFont());
        lblRolodexID.setText("Rolodex Id: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
        add(lblRolodexID, gridBagConstraints);

        lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        lblSponsor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsor.setText("Sponsor: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblSponsor, gridBagConstraints);

        lblOrganisation.setFont(CoeusFontFactory.getLabelFont());
        lblOrganisation.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOrganisation.setText("Organization: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblOrganisation, gridBagConstraints);

        lblAddress.setFont(CoeusFontFactory.getLabelFont());
        lblAddress.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAddress.setText("Address: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblAddress, gridBagConstraints);

        txtName.setEditable(false);
        txtName.setMinimumSize(new java.awt.Dimension(450, 20));
        txtName.setPreferredSize(new java.awt.Dimension(450, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(txtName, gridBagConstraints);

        txtSponsor.setEditable(false);
        txtSponsor.setMinimumSize(new java.awt.Dimension(132, 20));
        txtSponsor.setPreferredSize(new java.awt.Dimension(132, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtSponsor, gridBagConstraints);

        txtOrganisation.setEditable(false);
        txtOrganisation.setMinimumSize(new java.awt.Dimension(650, 21));
        txtOrganisation.setPreferredSize(new java.awt.Dimension(650, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtOrganisation, gridBagConstraints);

        txtRolodexID.setEditable(false);
        txtRolodexID.setDragEnabled(true);
        txtRolodexID.setMinimumSize(new java.awt.Dimension(20, 20));
        txtRolodexID.setPreferredSize(new java.awt.Dimension(125, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(txtRolodexID, gridBagConstraints);

        lblSponsorName.setMinimumSize(new java.awt.Dimension(200, 20));
        lblSponsorName.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 0);
        add(lblSponsorName, gridBagConstraints);

        txtAddress1.setEditable(false);
        txtAddress1.setMinimumSize(new java.awt.Dimension(650, 21));
        txtAddress1.setPreferredSize(new java.awt.Dimension(650, 21));


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtAddress1, gridBagConstraints);

        txtAddress2.setEditable(false);
        txtAddress2.setMinimumSize(new java.awt.Dimension(650, 21));
        txtAddress2.setPreferredSize(new java.awt.Dimension(650, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtAddress2, gridBagConstraints);

        txtAddress3.setEditable(false);
        txtAddress3.setMinimumSize(new java.awt.Dimension(650, 21));
        txtAddress3.setPreferredSize(new java.awt.Dimension(650, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtAddress3, gridBagConstraints);

        lblCity.setFont(CoeusFontFactory.getLabelFont());
        lblCity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCity.setText("City: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblCity, gridBagConstraints);

        lblCounty.setFont(CoeusFontFactory.getLabelFont());
        lblCounty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCounty.setText("County: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblCounty, gridBagConstraints);

        lblState.setFont(CoeusFontFactory.getLabelFont());
        lblState.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblState.setText("State/Province: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblState, gridBagConstraints);

        lblPostalCode.setFont(CoeusFontFactory.getLabelFont());
        lblPostalCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPostalCode.setText("Postal Code: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblPostalCode, gridBagConstraints);

        txtCity.setEditable(false);
        txtCity.setMinimumSize(new java.awt.Dimension(200, 21));
        txtCity.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtCity, gridBagConstraints);

        txtState.setEditable(false);
        txtState.setMinimumSize(new java.awt.Dimension(200, 21));
        txtState.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtState, gridBagConstraints);

        txtCounty.setEditable(false);
        txtCounty.setMinimumSize(new java.awt.Dimension(200, 21));
        txtCounty.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtCounty, gridBagConstraints);

        txtPostalCode.setEditable(false);
        txtPostalCode.setMinimumSize(new java.awt.Dimension(200, 21));
        txtPostalCode.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtPostalCode, gridBagConstraints);

        lblCountry.setFont(CoeusFontFactory.getLabelFont());
        lblCountry.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCountry.setText("Country: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblCountry, gridBagConstraints);

        lblPhone.setFont(CoeusFontFactory.getLabelFont());
        lblPhone.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPhone.setText("Phone: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblPhone, gridBagConstraints);

        lblEMail.setFont(CoeusFontFactory.getLabelFont());
        lblEMail.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEMail.setText("E Mail: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblEMail, gridBagConstraints);

        lblFax.setFont(CoeusFontFactory.getLabelFont());
        lblFax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFax.setText("Fax: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblFax, gridBagConstraints);

        txtCountry.setEditable(false);
        txtCountry.setMinimumSize(new java.awt.Dimension(200, 21));
        txtCountry.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtCountry, gridBagConstraints);

        txtEMail.setEditable(false);
        txtEMail.setMinimumSize(new java.awt.Dimension(200, 21));
        txtEMail.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtEMail, gridBagConstraints);

        txtPhone.setEditable(false);
        txtPhone.setMinimumSize(new java.awt.Dimension(200, 21));
        txtPhone.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtPhone, gridBagConstraints);

        txtFax.setEditable(false);
        txtFax.setMinimumSize(new java.awt.Dimension(200, 21));
        txtFax.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(txtFax, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblComments.setText("Comments: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblComments, gridBagConstraints);

        scrPnComments.setForeground(new java.awt.Color(204, 204, 204));
        scrPnComments.setMinimumSize(new java.awt.Dimension(650, 55));
        scrPnComments.setPreferredSize(new java.awt.Dimension(650, 55));
        scrPnComments.setRequestFocusEnabled(false);
        txtArComments.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtArComments.setEditable(false);
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setForeground(new java.awt.Color(0, 51, 51));
        txtArComments.setLineWrap(true);
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 5, 0);
        add(scrPnComments, gridBagConstraints);

        lblLastUpdate.setFont(CoeusFontFactory.getLabelFont());
        lblLastUpdate.setText("Last Update: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(lblLastUpdate, gridBagConstraints);

        txtLastUpdate.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtLastUpdate.setEnabled(false);
        txtLastUpdate.setPreferredSize(new java.awt.Dimension(140, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(txtLastUpdate, gridBagConstraints);

        lblUpdateUser.setFont(CoeusFontFactory.getLabelFont());
        lblUpdateUser.setText("Update User: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(lblUpdateUser, gridBagConstraints);

        txtUpdateUser.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtUpdateUser.setEnabled(false);
        txtUpdateUser.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(txtUpdateUser, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel lblAddress;
    public javax.swing.JLabel lblCity;
    public javax.swing.JLabel lblComments;
    public javax.swing.JLabel lblCountry;
    public javax.swing.JLabel lblCounty;
    public javax.swing.JLabel lblEMail;
    public javax.swing.JLabel lblFax;
    public javax.swing.JLabel lblLastUpdate;
    public javax.swing.JLabel lblName;
    public javax.swing.JLabel lblOrganisation;
    public javax.swing.JLabel lblPhone;
    public javax.swing.JLabel lblPostalCode;
    public javax.swing.JLabel lblRolodexID;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorName;
    public javax.swing.JLabel lblState;
    public javax.swing.JLabel lblUpdateUser;
    public javax.swing.JScrollPane scrPnComments;
    public edu.mit.coeus.utils.CoeusTextField txtAddress1;
    public edu.mit.coeus.utils.CoeusTextField txtAddress2;
    public edu.mit.coeus.utils.CoeusTextField txtAddress3;
    public javax.swing.JTextArea txtArComments;
    public edu.mit.coeus.utils.CoeusTextField txtCity;
    public edu.mit.coeus.utils.CoeusTextField txtCountry;
    public edu.mit.coeus.utils.CoeusTextField txtCounty;
    public edu.mit.coeus.utils.CoeusTextField txtEMail;
    public edu.mit.coeus.utils.CoeusTextField txtFax;
    public javax.swing.JTextField txtLastUpdate;
    public edu.mit.coeus.utils.CoeusTextField txtName;
    public edu.mit.coeus.utils.CoeusTextField txtOrganisation;
    public edu.mit.coeus.utils.CoeusTextField txtPhone;
    public edu.mit.coeus.utils.CoeusTextField txtPostalCode;
    public edu.mit.coeus.utils.CoeusTextField txtRolodexID;
    public edu.mit.coeus.utils.CoeusTextField txtSponsor;
    public edu.mit.coeus.utils.CoeusTextField txtState;
    public javax.swing.JTextField txtUpdateUser;
    // End of variables declaration//GEN-END:variables
    
}
