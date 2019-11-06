/*
 * @(#)ScheduleMaintenanceForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.irb.gui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.GridBagConstraints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.Time;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;

import edu.mit.coeus.irb.bean.ScheduleDetailsBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
/** This class is used for maintaining schedules. Here all the details like
 * maximum number of protocols can be reviewed for a schedule etc., can be
 * specified. This class can be called from <CODE>CommitteeScheduleDetailsForm</CODE> in
 * <CODE>Modify/Display</CODE> mode.
 *
 *
 * @author ravikanth
 */
public class ScheduleMaintenanceForm extends javax.swing.JComponent {

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    
    /** Default constructor which creates new <CODE>ScheduleMaintenanceForm</CODE>.
     */
    public ScheduleMaintenanceForm() {
    }

    /** Constructor which constructs <CODE>ScheduleMaintenanceForm</CODE> with specified data
     * and specified mode.
     *
     * @param functionType character which specifies that the mode in which the form
     * has to be shown.
     * 'M' specifies Modify mode
     * 'D' specifies Display mode where all fields are not editable.
     * @param scheduleDetailsBean which will have the data to be displayed
     * in the screen.
     * @param mdiForm reference of <CODE>CoeusAppletMDIForm</CODE>.
     */
    public ScheduleMaintenanceForm(char functionType,
                                   ScheduleDetailsBean scheduleDetailsBean,
                                   CoeusAppletMDIForm mdiForm){
        this.mdiForm = mdiForm;
        this.scheduleDetailsBean = scheduleDetailsBean;
        this.functionType = functionType;
    }

    /**
     * This method is used to set the available schedule status information.
     *
     * @param scheduleStatus Vector which consists of all available status
     * information.
     */
    public void setScheduleStatus(Vector scheduleStatus){
        this.scheduleStatus = scheduleStatus;
    }

    /**
     * This method is used to set whether the data is to be saved or not.
     * @param save boolean true if any modifications have been done and are not
     * saved, else  false.
     */

    public void setSaveRequired(boolean save){
        this.saveRequired = save;
    }

    /**
     * This method is called from within the createScheduleMaintenanceForm() to
     * initialize the form.
     */
    private void initComponents(){
        GridBagConstraints gridBagConstraints;

        lblScheduleCommitteeId = new JLabel();
        lblCommitteeId = new JLabel();
        lblScheduleCommitteeName = new JLabel();
        lblCommitteeName = new JLabel();
        lblScheduleDate = new JLabel();
        txtScheduleDate = new CoeusTextField();
        lblStatus = new JLabel();
        cmbStatus = new JComboBox();
        lblMeetingDate = new JLabel();
        txtMeetingDate = new CoeusTextField();
        lblMaxProtocols = new JLabel();
        txtMaxProtocols = new CoeusTextField();
        lblDeadLine = new JLabel();
        txtDeadLine = new CoeusTextField();
        lblPlace = new JLabel();
        txtPlace = new CoeusTextField();
        lblTime = new JLabel();
        txtTime = new CoeusTextField();
        lblComments = new JLabel();
        scrPnComments = new JScrollPane();
        txtArComments = new JTextArea();
        lblAgendaProduction = new JLabel();
        txtAgendaProduction = new CoeusTextField();
        lblStartTime = new JLabel();
        txtStartTime = new CoeusTextField();
        lblEndTime = new JLabel();
        txtEndTime = new CoeusTextField();
        // 3282: Reviewer view in Lite - Start
        lblViewInLite =  new CoeusLabel();
        chkViewInLite = new JCheckBox();
        // 3282: Reviewer view in Lite - End
        setLayout(new java.awt.GridBagLayout());

        setToolTipText("");
        lblScheduleCommitteeId.setFont(CoeusFontFactory.getLabelFont());
        lblScheduleCommitteeId.setHorizontalAlignment(SwingConstants.RIGHT);
        lblScheduleCommitteeId.setText("Committee ID : ");
        lblScheduleCommitteeId.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblScheduleCommitteeId, gridBagConstraints);

        lblCommitteeId.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(lblCommitteeId, gridBagConstraints);

        lblScheduleCommitteeName.setFont(CoeusFontFactory.getLabelFont());
        lblScheduleCommitteeName.setHorizontalAlignment(SwingConstants.LEFT);
        lblScheduleCommitteeName.setText("Committee Name : ");
        lblScheduleCommitteeName.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblScheduleCommitteeName, gridBagConstraints);

        lblCommitteeName.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeName.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(lblCommitteeName, gridBagConstraints);
       
        lblScheduleDate.setFont(CoeusFontFactory.getLabelFont());
        lblScheduleDate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblScheduleDate.setText("Schedule Date : ");
        lblScheduleDate.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblScheduleDate, gridBagConstraints);

        txtScheduleDate.setDocument(new LimitedPlainDocument(11));
        txtScheduleDate.setFont(CoeusFontFactory.getNormalFont());
        txtScheduleDate.setPreferredSize(new java.awt.Dimension(100, 20));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(txtScheduleDate, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setHorizontalAlignment(SwingConstants.RIGHT);
        lblStatus.setText("Status : ");
        lblStatus.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblStatus, gridBagConstraints);

        cmbStatus.setMinimumSize(new java.awt.Dimension(31, 20));
        cmbStatus.setPreferredSize(new java.awt.Dimension(120, 20));
        cmbStatus.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(cmbStatus, gridBagConstraints);

        lblMeetingDate.setFont(CoeusFontFactory.getLabelFont());
        lblMeetingDate.setText("Meeting Date : ");
        lblMeetingDate.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblMeetingDate, gridBagConstraints);

        txtMeetingDate.setDocument(new LimitedPlainDocument(11));
        txtMeetingDate.setFont(CoeusFontFactory.getNormalFont());
        txtMeetingDate.setPreferredSize(new java.awt.Dimension(120, 20));
        if(functionType != 'D'){
            txtMeetingDate.addFocusListener(new CustomFocusAdapter());
            txtMeetingDate.setInputVerifier(new DataVerifier());
        }
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(txtMeetingDate, gridBagConstraints);

        lblMaxProtocols.setFont(CoeusFontFactory.getLabelFont());
        lblMaxProtocols.setText("Max Protocols : ");
        lblMaxProtocols.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblMaxProtocols, gridBagConstraints);

        txtMaxProtocols.setDocument(
            new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
        txtMaxProtocols.setFont(CoeusFontFactory.getNormalFont());
        txtMaxProtocols.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(txtMaxProtocols, gridBagConstraints);

        lblDeadLine.setFont(CoeusFontFactory.getLabelFont());
        lblDeadLine.setText("Deadline : ");
        lblDeadLine.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblDeadLine, gridBagConstraints);

        txtDeadLine.setDocument(new LimitedPlainDocument(11));
        txtDeadLine.setFont(CoeusFontFactory.getNormalFont());
        txtDeadLine.setPreferredSize(new java.awt.Dimension(120, 20));
        if(functionType != 'D'){
            txtDeadLine.addFocusListener(new CustomFocusAdapter());
            txtDeadLine.setInputVerifier(new DataVerifier());
        }
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(txtDeadLine, gridBagConstraints);

        lblPlace.setFont(CoeusFontFactory.getLabelFont());
        lblPlace.setText("Place : ");
        lblPlace.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblPlace, gridBagConstraints);

        txtPlace.setDocument(new LimitedPlainDocument(200));
        txtPlace.setFont(CoeusFontFactory.getNormalFont());
        txtPlace.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        txtPlace.setCaretPosition(0);
        add(txtPlace, gridBagConstraints);

        lblTime.setFont(CoeusFontFactory.getLabelFont());
        lblTime.setText("Time : ");
        lblTime.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblTime, gridBagConstraints);

        txtTime.setDocument(new LimitedPlainDocument(5));
        txtTime.setFont(CoeusFontFactory.getNormalFont());
        txtTime.setPreferredSize(new java.awt.Dimension(120, 20));
        txtTime.setInputVerifier(new DataVerifier());
        //txtTime.addFocusListener(new CustomFocusAdapter());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(txtTime, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setText("Comments : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblComments, gridBagConstraints);

        scrPnComments.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrPnComments.setPreferredSize(new java.awt.Dimension(118, 80));
        txtArComments.setDocument(new LimitedPlainDocument(2000));
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        // Added by chandra
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        // End Chandra
        scrPnComments.setViewportView(txtArComments);
		if(functionType != 'D'){ //Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004 - start
            txtArComments.setInputVerifier(new DataVerifier());
        } // end

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(scrPnComments, gridBagConstraints);

        lblAgendaProduction.setFont(CoeusFontFactory.getLabelFont());
        lblAgendaProduction.setText("Agenda Generation : ");
        lblAgendaProduction.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblAgendaProduction, gridBagConstraints);

        txtAgendaProduction.setDocument(new LimitedPlainDocument(11));
        txtAgendaProduction.setFont(CoeusFontFactory.getNormalFont());
        txtAgendaProduction.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(txtAgendaProduction, gridBagConstraints);

        lblStartTime.setFont(CoeusFontFactory.getLabelFont());
        lblStartTime.setText("Start Time : ");
        lblStartTime.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblStartTime, gridBagConstraints);

        txtStartTime.setDocument(new LimitedPlainDocument(5));
        txtStartTime.setFont(CoeusFontFactory.getNormalFont());
        txtStartTime.setPreferredSize(new java.awt.Dimension(100, 20));
        txtStartTime.setInputVerifier(new DataVerifier());
        //txtStartTime.addFocusListener(new CustomFocusAdapter());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(txtStartTime, gridBagConstraints);

        lblEndTime.setFont(CoeusFontFactory.getLabelFont());
        lblEndTime.setText("End Time : ");
        lblEndTime.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(lblEndTime, gridBagConstraints);

        txtEndTime.setDocument(new LimitedPlainDocument(5));
        txtEndTime.setFont(CoeusFontFactory.getNormalFont());
        txtEndTime.setPreferredSize(new java.awt.Dimension(120, 20));
        txtEndTime.setInputVerifier(new DataVerifier());
        //txtEndTime.addFocusListener(new CustomFocusAdapter());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(txtEndTime, gridBagConstraints);
        // Added by chandra 12/09/2003 
        
        // 3282: Reviewer view in Lite - Start
        lblViewInLite.setFont(CoeusFontFactory.getLabelFont());
        lblViewInLite.setPreferredSize(new java.awt.Dimension(150, 20));
        lblStatus.setHorizontalAlignment(SwingConstants.RIGHT);
        lblViewInLite.setText("Available to Reviewers : ");
        lblViewInLite.setHorizontalTextPosition(SwingConstants.RIGHT);
        lblViewInLite.setHorizontalAlignment(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(lblViewInLite, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(chkViewInLite, gridBagConstraints);
        chkViewInLite.setInputVerifier(new DataVerifier());
        // 3282: Reviewer view in Lite - End

        java.awt.Component[] components = {cmbStatus,txtPlace,txtTime,
        txtMaxProtocols,txtDeadLine,txtMeetingDate,txtStartTime, txtEndTime,txtArComments};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);

    }

    /** This is used to set the default focus to the first field in Schedule
     * Maintenance screen on opening this window.
     */
    public void setFocusToType() {
        cmbStatus.requestFocus();
    }

    /*
    private void cmbStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbStatusItemStateChanged
        saveRequired = true;
    }//GEN-LAST:event_cmbStatusItemStateChanged
    */

    /** This method creates the <CODE>ScheduleMaintenanceForm</CODE> by initializing all the
     * components with the data, if present. And also it will set the enabled
     * status for every component depending on the <CODE>functionType</CODE>.
     * @return JComponent <CODE>ScheduleMaintenanceForm</CODE> after intializing with data.
     */

    public JComponent createScheduleMaintenanceForm(){
        initComponents();
        setFormData();
        
        formatFields();
        coeusMessageResources = CoeusMessageResources.getInstance();
        return this;
    }
    /** This method is used to set the <CODE>ScheduleDetailsBean</CODE> whose data will be
     * used to display in the form fields.
     *
     * @param scheduleDetailsBean which consists of data to be shown in the form.
     */
    public void setScheduleDetails(ScheduleDetailsBean scheduleDetailsBean){
        this.scheduleDetailsBean = scheduleDetailsBean;
    }

    /** This method is used to set the form fields with the data specified in
     * <CODE>scheduleDetailsBean</CODE>.
     */
    public void setFormData(){
        /* Setting the Available schedule status info to combobox*/
        if(scheduleStatus!=null && scheduleStatus.size()>0){
            cmbStatus.setModel(
            (new CoeusComboBox(scheduleStatus,false)).getModel());
        }

        if(scheduleDetailsBean!=null){
            /* if data is present set it to form fields */
            lblCommitteeId.setText((scheduleDetailsBean.getCommitteeId() == null)
                    ? "" : scheduleDetailsBean.getCommitteeId());
            lblCommitteeName.setText((scheduleDetailsBean.getCommitteeName()
                    == null)
                    ? "" : scheduleDetailsBean.getCommitteeName());
            if(scheduleDetailsBean.getScheduleDate()!=null){
                String date = null;
                date = dtUtils.formatDate(
                        scheduleDetailsBean.getScheduleDate().toString(),
                        "dd-MMM-yyyy");
                txtScheduleDate.setText(date == null ? "" :date);
            }

            if(scheduleDetailsBean.getMeetingDate()!=null){
                String date=null;
                date = dtUtils.formatDate(
                        scheduleDetailsBean.getMeetingDate().toString(),
                        "dd-MMM-yyyy");
                txtMeetingDate.setText(date == null ? "" :date);
            }

            txtMaxProtocols.setText(""+scheduleDetailsBean.getMaxProtocols());

            if(scheduleDetailsBean.getProtocolSubDeadLine()!=null){
                String date=null;
                date = dtUtils.formatDate(
                        scheduleDetailsBean.getProtocolSubDeadLine().toString(),
                        "dd-MMM-yyyy");
                txtDeadLine.setText(date == null ? "" :date);
            }

            if(scheduleDetailsBean.getLastAgendaProdRevDate()!=null){
                String date=null;
                date = dtUtils.formatDate(
                        scheduleDetailsBean.getLastAgendaProdRevDate().toString(),
                        "dd-MMM-yyyy");
                txtAgendaProduction.setText(date == null ? "" :date);
            }

            txtPlace.setText((scheduleDetailsBean.getScheduledPlace() == null)
                    ? "" : scheduleDetailsBean.getScheduledPlace());
            txtTime.setText(convertTimeToString(
                    scheduleDetailsBean.getScheduledTime()));
            txtStartTime.setText(convertTimeToString(
                    scheduleDetailsBean.getMeetingStartTime()));
            txtEndTime.setText(convertTimeToString(
                    scheduleDetailsBean.getMeetingEndTime()));
            txtArComments.setText((scheduleDetailsBean.getComments() == null )
                    ? "" : scheduleDetailsBean.getComments());
            ComboBoxBean comboBean = new ComboBoxBean();
            comboBean.setCode(new Integer(
                    scheduleDetailsBean.getScheduleStatusCode()).toString());
            comboBean.setDescription(scheduleDetailsBean.getScheduleStatusDesc());

            if(functionType =='D'){
                /* In display mode add only the present schedule status info */
                Vector cTypes = new Vector();
                cTypes.addElement(comboBean);
                cmbStatus.setModel(
                        (new CoeusComboBox(cTypes,false)).getModel());
                cmbStatus.setSelectedItem(comboBean);
            }else{
                for(int typeRow=0;typeRow<cmbStatus.getItemCount();typeRow++){
                    if((((ComboBoxBean)cmbStatus.getItemAt(typeRow)).toString())
                            .equals(comboBean.toString())){
                        cmbStatus.setSelectedIndex(typeRow);
                    }
                }
            }
        }
        // 3282: Reviewer View in Lite
        chkViewInLite.setSelected(scheduleDetailsBean.isViewInLite());
 
        saveRequired = false;
    }

    /** This method is used to fetch the data from the form fields into
     * <CODE>ScheduleDetailsBean</CODE>.
     *
     * @return <CODE>ScheduleDetailsBean</CODE> consists of data which are in form fields.
     * And also it will set the <CODE>AcType</CODE> to 'U' for the bean if any modifications
     * has been done.
     * @throws Exception if any data is invalid or mandatory fields don't have
     * any values.
     */
    public ScheduleDetailsBean getFormData() throws Exception {
        boolean modified = false;
        //Added by sharath (Bug Fix : Infinite Confirm Dialog)
        //If focus is on any Date field. Date cannot be parsed.
        txtArComments.grabFocus();
        //Added by sharath (Bug Fix : Infinite Confirm Dialog)
        
        String statusCode =
                ((ComboBoxBean)cmbStatus.getModel().getSelectedItem()).getCode();
        String statusDesc = ((ComboBoxBean)
                cmbStatus.getModel().getSelectedItem()).getDescription();

        if(!(scheduleDetailsBean.getScheduleStatusDesc().equals(statusDesc))){
            modified = true;
        }
        
        if(scheduleDetailsBean.getMeetingDate()!=null){
            
            if(!dtUtils.formatDate(
                    scheduleDetailsBean.getMeetingDate().toString(),
                    "dd-MMM-yyyy").equals((txtMeetingDate.getText()==null)
                            ?"":txtMeetingDate.getText())){
                modified = true;
            }
        }else if( txtMeetingDate.getText() != null && txtMeetingDate.getText().length() > 0 ){
            modified = true;
        }
        if(scheduleDetailsBean.getMaxProtocols()
                != Integer.parseInt(((txtMaxProtocols.getText()== null)
                        ||(txtMaxProtocols.getText().trim().length() == 0))
                                ?"0":txtMaxProtocols.getText())){
            modified = true;
        }
        if(scheduleDetailsBean.getProtocolSubDeadLine()!=null){
            if(!dtUtils.formatDate(
                    scheduleDetailsBean.getProtocolSubDeadLine().toString(),
                    "dd-MMM-yyyy").equals(txtDeadLine.getText()==null?""
                        :txtDeadLine.getText())){
                modified = true;
            }
        }
        if(scheduleDetailsBean.getLastAgendaProdRevDate()!= null){
            if(!dtUtils.formatDate(
                    scheduleDetailsBean.getLastAgendaProdRevDate().toString(),
                    "dd-MMM-yyyy").equals(txtAgendaProduction.getText()==null?""
                        :txtAgendaProduction.getText())){
                modified = true;
            }
        }
        if(!((scheduleDetailsBean.getScheduledPlace()==null?""
                :scheduleDetailsBean.getScheduledPlace()).equals(
                    txtPlace.getText()==null?"":txtPlace.getText()))){
            modified = true;
        }
        if(!(convertTimeToString(scheduleDetailsBean.getScheduledTime()).equals(
                txtTime.getText()==null?"":txtTime.getText()))){
            modified = true;
        }
        if(!(convertTimeToString(
                scheduleDetailsBean.getMeetingStartTime()).equals(
                txtStartTime.getText()==null?"":txtStartTime.getText()))){
            modified = true;
        }
        if(!(convertTimeToString(scheduleDetailsBean.getMeetingEndTime()).equals(
                txtEndTime.getText()==null?"":txtEndTime.getText()))){
            modified = true;
        }
        if(!((scheduleDetailsBean.getComments() == null ? ""
                : scheduleDetailsBean.getComments()).equals(
                    txtArComments.getText()==null?"":txtArComments.getText()))){
            modified = true;
        }
        if(modified){
            saveRequired = true;
            scheduleDetailsBean.setAcType("U");
        }
        scheduleDetailsBean.setScheduleStatusCode(Integer.parseInt(statusCode));
        scheduleDetailsBean.setScheduleStatusDesc(statusDesc);
        
        if((txtMeetingDate.getText() != null)
                && (txtMeetingDate.getText().trim().length()>0)){
            
            String actualDate = dtUtils.restoreDate(txtMeetingDate.getText(),"/-:,");
            java.sql.Date meetingDt = null;
            if(actualDate != null && !txtMeetingDate.getText().equalsIgnoreCase(actualDate) ){
                 meetingDt = new java.sql.Date( ((java.util.Date)dtFormat.parse( actualDate )).getTime() );
            }
            if(meetingDt != null){
                scheduleDetailsBean.setMeetingDate(meetingDt);
            }else{
                scheduleDetailsBean.setMeetingDate(null);
            }
//            scheduleDetailsBean.setMeetingDate(
//                new java.sql.Date(dtFormat.parse(
//                        dtUtils.restoreDate(txtMeetingDate.getText(),
//                                "/-:,")).getTime()));
        }else{
            scheduleDetailsBean.setMeetingDate(null);
        }
        if((txtMaxProtocols.getText()!=null )
                && (txtMaxProtocols.getText().trim().length()>0)){
            scheduleDetailsBean.setMaxProtocols(
                    Integer.parseInt(txtMaxProtocols.getText()));
        }
        // Added by Chandra. Bug file - IRB-systemTestingDL-01.xls - Bug No.15
        else{
            scheduleDetailsBean.setMaxProtocols(0);
        }// Bug Fixed
        if((txtDeadLine.getText()!=null)
                && (txtDeadLine.getText().trim().length()>0)){
                    
//            scheduleDetailsBean.setProtocolSubDeadLine(
//                    new java.sql.Date(dtFormat.parse(
//                            dtUtils.restoreDate(txtDeadLine.getText(),
//                                    "/-:,")).getTime()));
            String actualDate = dtUtils.restoreDate(txtDeadLine.getText(),"/-:,");
            java.sql.Date deadLineDt = null;
            if(actualDate != null && !txtDeadLine.getText().equalsIgnoreCase(actualDate) ){
                 deadLineDt = new java.sql.Date( ((java.util.Date)dtFormat.parse( actualDate )).getTime() );
            }
            if(deadLineDt != null){
                scheduleDetailsBean.setProtocolSubDeadLine(deadLineDt);
            }else{
                scheduleDetailsBean.setProtocolSubDeadLine(null);
            }
        }else{
            scheduleDetailsBean.setProtocolSubDeadLine(null);
        }
        if((txtAgendaProduction.getText()!=null)
                && (txtAgendaProduction.getText().trim().length()>0)){
            scheduleDetailsBean.setLastAgendaProdRevDate(
                    new java.sql.Date(dtFormat.parse(
                            dtUtils.restoreDate(txtAgendaProduction.getText(),
                                    "/-:,")).getTime()));
        }else{
            scheduleDetailsBean.setLastAgendaProdRevDate(null);
        }
        if(txtPlace.getText()!=null){
            scheduleDetailsBean.setScheduledPlace(txtPlace.getText());
        }else{
            scheduleDetailsBean.setScheduledPlace(null);
        }
        /* convert HH:mm value of text filed to HH:mm:ss format by adding 00
           to seconds value */
        if((txtTime.getText()!=null)
                && (txtTime.getText().trim().length()>0)){
            scheduleDetailsBean.setScheduledTime(
                    Time.valueOf(txtTime.getText()+":00"));
        }else{
            scheduleDetailsBean.setScheduledTime(null);
        }
        if((txtStartTime.getText()!=null)
                && (txtStartTime.getText().trim().length()>0)){
            scheduleDetailsBean.setMeetingStartTime(
                    Time.valueOf(txtStartTime.getText()+":00"));
        }else{
            scheduleDetailsBean.setMeetingStartTime(null);
        }
        if((txtEndTime.getText()!=null)
                && (txtEndTime.getText().trim().length()>0)){
            scheduleDetailsBean.setMeetingEndTime(
                    Time.valueOf(txtEndTime.getText()+":00"));
        }else{
            scheduleDetailsBean.setMeetingEndTime(null);
        }
        if((txtArComments.getText()!=null)
                && (txtArComments.getText().trim().length()>0)){
            scheduleDetailsBean.setComments(txtArComments.getText());
        }else{
            scheduleDetailsBean.setComments(null);
        }
        // 3282: Reviewer view in Lite - Start
        scheduleDetailsBean.setViewInLite(chkViewInLite.isSelected());
        // 3282: Reviewer view in Lite - End
        return scheduleDetailsBean;

    }


    /** This method converts the given Time object to String in HH:mm format
     * @param time Time object which is to be converted.
     * @return String which represents the time in HH:mm format.
     */
    public String convertTimeToString(Time time){
        GregorianCalendar gCal = new GregorianCalendar();
        String strTime = "";
        if(time!=null){
            /* if time is present then convert it to HH:mm format */
            gCal.setTime(time);
            String minutes = "";
            if(gCal.get(Calendar.MINUTE)<=9){
                /* If minutes is a single digit append 0 before that */
                minutes = "0"+gCal.get(Calendar.MINUTE);
            }else{
                minutes = ""+gCal.get(Calendar.MINUTE);
            }
            strTime = gCal.get(Calendar.HOUR_OF_DAY)+":"+minutes;
        }
        return strTime;

    }

    /** This method is used to set the enable/disable status for form fields
     * depending on the value of <CODE>functionType</CODE>.
     */
    public void formatFields(){
        boolean enabled = functionType == 'M'?true:false;
        txtScheduleDate.setEditable(false);
        txtMaxProtocols.setEditable(enabled);
        txtAgendaProduction.setEditable(false);
        cmbStatus.setEnabled(enabled);
        txtMeetingDate.setEditable(enabled);
        txtDeadLine.setEditable(enabled);
        txtPlace.setEditable(enabled);
        txtTime.setEditable(enabled);
        txtArComments.setEditable(enabled);
        txtStartTime.setEditable(enabled);
        txtEndTime.setEditable(enabled);
        if(enabled){
            txtArComments.setOpaque(true);
            txtPlace.requestFocus();
        }else{
            txtArComments.setOpaque(false);
        }
        txtPlace.setCaretPosition(0);
        txtArComments.setCaretPosition(0);
        // 3282: Reviewer View in Lite
        chkViewInLite.setEnabled(enabled);
    }

    /** This method is used to set the <CODE>functionType</CODE>.
     * @param mode character which specifies the mode in which the screen has to be shown.
     */
    public void setFunctionType(char mode){
        this.functionType = mode;
    }

    /** This method is used to determine whether the data is to be saved or not.
     * @return true if any modifications have been done and are not
     * saved, else false.
     */
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {
                cmbStatus.requestFocusInWindow();            
        }
    }    
    //End Amit  
        
    /**
     * Internal class which is used to listen to focus events of start date
     * and end date fields. On focusGained the date will be restored to
     * MM/dd/yyyy, and on focusLost the date will be formatted to dd-MMM-yyyy.
     */
    private class CustomFocusAdapter extends FocusAdapter{
        private CoeusTextField txtField = null;
        private String strData = "";
        private String focusDate = "";
        private boolean temporary = false;
        /**
         * This method is used when focus is gained to the text component.
         * If there is valid date object in dd-MMM-yyyy format it will convert it
         * to MM/dd/yyyy format whenever any date field gets focus.
         */
        public void focusGained(FocusEvent fe){
            txtField = (CoeusTextField)fe.getSource();
            if ( (txtField.getText() != null)
                    &&  (!txtField.getText().trim().equals("")) ) {
                if(((txtField.equals(txtMeetingDate))
                        || (txtField.equals(txtDeadLine))) ){
                    if(!strData.equals(txtField.getText())){
                        focusDate = dtUtils.restoreDate(
                                txtField.getText(),"/-:,");
                        txtField.setText(focusDate);
                    }
                }
            }
        }
//        public void focusLost(FocusEvent fe){
//            txtField = (CoeusTextField)fe.getSource();
//            /* check whether the focus lost is temporary or permanent*/
//            //Uncommented by sharath
//            //Bug Fix Time Focus Lost
//            temporary = fe.isTemporary(); 
//            
//            String editingValue = null;
//            if ( (txtField.getText() != null)
//                    && (!txtField.getText().trim().equals("")) && (!temporary)){
//                 
//                /* If focus lost is permanent then convert the date specified to
//                 * dd-MMM-yyyy format.
//                 */
//                strData = txtField.getText();
//                if((txtField.equals(txtMeetingDate)
//                        || (txtField.equals(txtDeadLine))) ){
//                    /** if the event dispatched component is of date field then
//                     *  try to format it to dd-MMM-yyyy. if not able to format
//                     * diplay error message.
//                     */
//                    String convertedDate = dtUtils.formatDate(txtField.getText(),
//                            "/-:," , "dd-MMM-yyyy");
//                    if (convertedDate==null){
//                        temporary = true;
//                        CoeusOptionPane.showErrorDialog(
//                                coeusMessageResources.parseMessageKey(
//                                        "memMntFrm_exceptionCode.1048") );
//                        saveRequired = true;
//                        txtField.requestFocusInWindow();
//                    }else {
//                        focusDate = txtField.getText();
//                        txtField.setText(convertedDate);
//                        temporary = false;
//                    }
//                }
//                else{
//                    if((txtField.equals(txtTime))
//                            || (txtField.equals(txtStartTime))
//                            || (txtField.equals(txtEndTime))){
//                        /**
//                         * if event dispatched component is of time field then
//                         * check for validity fo data in HH:mm format
//                         */
//                        String convertedTime;
//                        
//                        //Added by sharath (Bug Fix : Focus Lost for Time Validation)
//                        if(txtField.getText().trim().indexOf(':') < 0 && txtField.getText().trim().length() <= 2) {
//                            txtField.setText(txtField.getText().trim()+":00");
//                        }
//                        //Added by sharath (Bug Fix : Focus Lost for Time Validation)
//                        
//                        convertedTime = dtUtils.formatTime(txtField.getText());
//                        if (convertedTime==null){
//                            temporary = true;
//                            CoeusOptionPane.showErrorDialog(
//                                coeusMessageResources.parseMessageKey(
//                                        "schdGenFrm_exceptionCode.1085"));
//                            //Added by sharath (Bug Fix : Focus Lost for Time Validation)
//                            txtField.setText("");
//                            txtField.requestFocusInWindow();
//                            //Added by sharath (Bug Fix : Focus Lost for Time Validation)
//                            
//                            saveRequired = true;
//                        }else {
//                            txtField.setText(convertedTime);
//                        }
//                    }
//                }
//            }
//        }
    };

    /** This method is used to check for validity of data in form fields.
     *
     * @return true if all the data is valid, else false.
     * @throws Exception if any of the fields contains invalid data.
     */
    public boolean validateFormData() throws Exception,CoeusUIException{

        if((txtDeadLine.getText()== null)
                || (txtDeadLine.getText().trim().length() == 0)){
            /* Protocol submission deadline doesn't have any value */
            log(coeusMessageResources.parseMessageKey(
                    "schdMntFrm_exceptionCode.1086"));
            txtDeadLine.requestFocus();
            return false;
        }else{
                java.util.Date scheduleDate =
                    dtFormat.parse(dtUtils.restoreDate(txtScheduleDate.getText(),
                            "/:-,"));
                java.util.Date deadLineDate =
                    dtFormat.parse(dtUtils.restoreDate(txtDeadLine.getText(),
                            "/:-,"));
                if(scheduleDate.compareTo(deadLineDate)<0){
                    /* Schedule Date is earlier than Protocol submission deadline */
                    log(coeusMessageResources.parseMessageKey(
                                "schdMntFrm_exceptionCode.1087"));
                    txtDeadLine.requestFocus();
                    return false;
                }
            
            }
            
            if((txtStartTime.getText()!= null) && (txtEndTime.getText()!= null)){
                if((txtStartTime.getText().trim().length()>0)
                        && (txtEndTime.getText().trim().length()>0)){
                    Time startTime = Time.valueOf(txtStartTime.getText()+":00");
                    Time endTime = Time.valueOf(txtEndTime.getText()+":00");
                    if(endTime.before(startTime)){
                        log(coeusMessageResources.parseMessageKey(
                                    "schdMntFrm_exceptionCode.1088"));
                        return false;
                    }
                }
            }
        return true;
    }
    /** This method is used to throw the exception with the given message.
     * @param mesg String which specifies the description of the exception.
     * @throws Exception specified by message.
     */
    public void log(String mesg) throws CoeusUIException {
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(0);
        throw coeusUIException;
    }

    public class DataVerifier extends InputVerifier {
        public DataVerifier(){}
        public boolean verify(JComponent field){
			if(field instanceof JTextArea){ //Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004 - start
				if(scheduleDetailsBean.getComments() == null) {
					scheduleDetailsBean.setComments("");
				}
					if(!scheduleDetailsBean.getComments().equals(((JTextArea)field).getText())) {
						saveRequired = true;
					}
				return true;
			}//end
                        // 3282: Reviewer View in Lite - End
                        if(field instanceof JCheckBox){
                            scheduleDetailsBean.setAcType("U");
                            saveRequired = true;
                            return true;
                        }
                        // 3282: Reviewer View in Lite - End         
            JTextField txtField = (JTextField)field;
                if((txtField.equals(txtMeetingDate)
                        || (txtField.equals(txtDeadLine))) ){
                    /** if the event dispatched component is of date field then
                     *  try to format it to dd-MMM-yyyy. if not able to format
                     * diplay error message.
                     */
                    if( "".equals(txtField.getText().trim())) {
                        return true;
                    }
                    String convertedDate = dtUtils.formatDate(txtField.getText(),
                            "/-:," , "dd-MMM-yyyy");
                    if (convertedDate==null){
                        Runnable runnable = new Runnable(){
                            public void run(){
                                CoeusOptionPane.showErrorDialog(
                                        coeusMessageResources.parseMessageKey(
                                                "memMntFrm_exceptionCode.1048") );
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                        saveRequired = true;
                        //txtField.requestFocusInWindow();
                        return false;
                    }else {
                        txtField.setText(convertedDate);
                    }
                }else{
                    if((txtField.equals(txtTime))
                            || (txtField.equals(txtStartTime))
                            || (txtField.equals(txtEndTime))){
                        /**
                         * if event dispatched component is of time field then
                         * check for validity fo data in HH:mm format
                         */
                        String convertedTime;
                        if( txtField.getText() != null && txtField.getText().trim().length() > 0 ){
                            //Added by sharath (Bug Fix : Focus Lost for Time Validation)
                            if(txtField.getText().trim().indexOf(':') < 0 && txtField.getText().trim().length() <= 2) {
                                txtField.setText(txtField.getText().trim()+":00");
                            }
                            //Added by sharath (Bug Fix : Focus Lost for Time Validation)

                            convertedTime = dtUtils.formatTime(txtField.getText());
                            if (convertedTime==null){
                                Runnable runnable = new Runnable(){
                                    public void run(){
                                        CoeusOptionPane.showErrorDialog(
                                                coeusMessageResources.parseMessageKey(
                                                        "schdGenFrm_exceptionCode.1085") );
                                    }
                                };
                                SwingUtilities.invokeLater(runnable);
                                //Added by sharath (Bug Fix : Focus Lost for Time Validation)
                                txtField.setText("");
                                //txtField.requestFocusInWindow();
                                //Added by sharath (Bug Fix : Focus Lost for Time Validation)
                                saveRequired = true;
                                return false;
                            }else {
                                txtField.setText(convertedTime);
                            }
                        }
                    }
                }
//            String strDate = ((JTextField)field).getText();
//            String convertedDate = dtUtils.formatDate(((JTextField)field).getText(), 
//                                        "/-:," ,"dd-MMM-yyyy");
//            if (convertedDate==null ){
//                   CoeusOptionPane.showErrorDialog("Please Enter a valid date");                
//                return false;
//            }
//            ((JTextField)field).setText(convertedDate);            
            return true;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField txtStartTime;
    private javax.swing.JLabel lblAgendaProduction;
    private javax.swing.JLabel lblScheduleDate;
    private javax.swing.JLabel lblScheduleCommitteeName;
    private javax.swing.JLabel lblStartTime;
    private javax.swing.JTextField txtMeetingDate;
    private javax.swing.JTextField txtEndTime;
    private javax.swing.JTextField txtPlace;
    private javax.swing.JTextField txtAgendaProduction;
    private javax.swing.JTextField txtScheduleDate;
    private javax.swing.JLabel lblMeetingDate;
    private javax.swing.JLabel lblEndTime;
    private javax.swing.JLabel lblPlace;
    private javax.swing.JLabel lblMaxProtocols;
    private javax.swing.JTextField txtMaxProtocols;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblCommitteeId;
    private javax.swing.JTextField txtTime;
    private javax.swing.JTextArea txtArComments;
    private javax.swing.JScrollPane scrPnComments;
    private javax.swing.JLabel lblCommitteeName;
    private javax.swing.JLabel lblDeadLine;
    private javax.swing.JLabel lblScheduleCommitteeId;
    private javax.swing.JLabel lblComments;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JTextField txtDeadLine;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration//GEN-END:variables
    private char functionType;
    private ScheduleDetailsBean scheduleDetailsBean;
    private Vector scheduleStatus;
    private DateUtils dtUtils = new DateUtils();
    private java.text.SimpleDateFormat dtFormat =
            new java.text.SimpleDateFormat("MM/dd/yyyy");
    private boolean saveRequired = false;
    private CoeusAppletMDIForm mdiForm;
    // 3282: Reviewer view in Lite - Start
    private JLabel lblViewInLite;
    private JCheckBox chkViewInLite;
    // 3282: Reviewer view in Lite - End
}
