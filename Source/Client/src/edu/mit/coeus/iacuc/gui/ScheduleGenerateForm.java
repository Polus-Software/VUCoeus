/*
 * @(#)ScheduleGenerateForm.java  
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.iacuc.gui;

import java.awt.event.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.util.*;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.irb.bean.ScheduleFrequencyBean;
import edu.mit.coeus.gui.CoeusMessageResources;


/** <code> ScheduleGenerateForm </code> is a class for generating the Schedules
 * for a Commitee with the given the start and end dates along with the frequency
 * with which the Schedules has to be generated.
 *
 * @author ravikanth
 * @version: 1.0 September 22, 2002
 */

public class ScheduleGenerateForm extends JComponent {
    
    /**
     * Creates new <CODE>ScheduleGenerateForm</CODE> and initializes all the components and
     * fetches the available frequencies and set the data to the combo box.
     */
    public ScheduleGenerateForm() {
        initComponents();
        setScheduleFrequency();
    }
    
    
    /**
     * Creates new form <CODE>ScheduleGenerateForm</CODE> with the given parent dialog,
     * initializes all the components and fetches the available frequencies.
     * @param parentDialog reference to parent Component.
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public ScheduleGenerateForm(Component parentDialog, CoeusAppletMDIForm mdiForm) {
        this.parentDialog = parentDialog;
        this.mdiForm = mdiForm;
        initComponents();
        setScheduleFrequency();
        /* CASE# IRBEN00077 Begin. */
        setCmbDayOfWeek();
        /* CASE# IRBEN00077 End. */
    }
    
    /**
     * This method is used to set the available schedule frequencies 
     * @param frequencies Vector of ComboBoxBean with frequency code and 
     * description which will be used to generate the schedules. 
     */
    private void setScheduleFrequencies(Vector frequencies){
        this.scheduleFrequencies = frequencies;
    }
   
    /**
     * This method is used to fetch the available schedule frequencies
     * @return Vector frequencies used to generate the schedules
     */
    private Vector getScheduleFrequencies(){
        return scheduleFrequencies;
    }
    
    /**
     * This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        GridBagConstraints gridBagConstraints;
        
        pnlMain = new JPanel();
        pnlFields = new JPanel();
        lblStartDate = new JLabel();
        txtStartDate = new JTextField();
        txtStartDate.setDocument(new LimitedPlainDocument(11));
        lblEndDate = new JLabel();
        txtEndDate = new JTextField();
        txtEndDate.setDocument(new LimitedPlainDocument(11));
        lblFrequency = new JLabel();
        cmbFrequency = new JComboBox();
        lblPlace = new JLabel();
        txtPlace = new JTextField();
        txtPlace.setDocument(new LimitedPlainDocument(200));
        lblTime = new JLabel();
        txtTime = new JTextField();
        txtTime.setDocument(new LimitedPlainDocument(11));
        pnlButtons = new JPanel();
        btnGenerate = new JButton();
        btnCancel = new JButton();
        /* CASE# IRBEN00077 begin */
        lblDayOfWeek = new JLabel();
        cmbDayOfWeek = new JComboBox();
        /* CASE# IRBEN00077 end */        
        
        pnlMain.setLayout(new GridBagLayout());
        
        pnlMain.setBorder(new EtchedBorder());
        pnlMain.setPreferredSize(new Dimension(450, 250));
        pnlMain.setMinimumSize(new Dimension(442, 60));
        pnlFields.setLayout(new GridBagLayout());
        
        lblStartDate.setText("Start Date :");
        lblStartDate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblStartDate.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 10, 0, 0);
        pnlFields.add(lblStartDate, gridBagConstraints);
        
        txtStartDate.setFont(CoeusFontFactory.getNormalFont());
        txtStartDate.setText("");
        txtStartDate.addFocusListener(new CustomFocusAdapter());
        /* CASE #IRBEN00077 Begin */
        txtStartDate.setPreferredSize(new Dimension(112,20));
        /* CASE #IRBEN00077 End */
        /* CASE #IRBEN00077 Comment Begin*/
        /* txtStartDate.setPreferredSize(new Dimension(100,20));*/
        /* CASE# IRBEN00077 Comment End. */
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.insets = new Insets(0, 10, 0, 0);
        pnlFields.add(txtStartDate, gridBagConstraints);
        
        lblEndDate.setText("End Date :");
        lblEndDate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblEndDate.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 5, 0, 0);
        pnlFields.add(lblEndDate, gridBagConstraints);
        
        txtEndDate.setFont(CoeusFontFactory.getNormalFont());
        txtEndDate.setText("");
        txtEndDate.addFocusListener(new CustomFocusAdapter());
        txtEndDate.setPreferredSize(new Dimension(100,20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.insets = new Insets(0, 10, 0, 0);
        pnlFields.add(txtEndDate, gridBagConstraints);
        
        lblFrequency.setText("Frequency :");
        lblFrequency.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 10, 0, 0);
        pnlFields.add(lblFrequency, gridBagConstraints);
        
        cmbFrequency.setFont(CoeusFontFactory.getNormalFont());
        /* CASE# IRBEN00077 Begin */
        cmbFrequency.setPreferredSize(new Dimension(112,20));
        /* CASE# IRBEN00077 End. */
        /* CASE# IRBEN00077 Comment Begin*/
        /*cmbFrequency.setPreferredSize(new Dimension(100,20));*/
        /* CASE# IRBEN00077 Comment End */
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.insets = new Insets(10, 10, 0, 0);
        pnlFields.add(cmbFrequency,gridBagConstraints);
        
        lblPlace.setText("Place :");
        lblPlace.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPlace.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 10, 0, 0);
        pnlFields.add(lblPlace, gridBagConstraints);
        
        txtPlace.setFont(CoeusFontFactory.getNormalFont());
        txtPlace.setText("");
        /* CASE# IRBEN00077 Begin */
        txtPlace.setPreferredSize(new Dimension(112,20));
        /* CASE# IRBEN00077 End */
        /* CASE# IRBEN00077 Comment Begin */
        /*txtPlace.setPreferredSize(new Dimension(100,20));*/
        /* CASE# IRBEN00077 Comment End */
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.insets = new Insets(10, 10, 0, 0);
        pnlFields.add(txtPlace, gridBagConstraints);
        
        lblTime.setText("Time :");
        lblTime.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTime.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 5, 0, 0);
        pnlFields.add(lblTime, gridBagConstraints);
        
        txtTime.setFont(CoeusFontFactory.getNormalFont());
        txtTime.setText("");
        txtTime.addFocusListener(new CustomFocusAdapter());
        txtTime.setPreferredSize(new Dimension(100,20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.insets = new Insets(10, 10, 0, 0);
        pnlFields.add(txtTime, gridBagConstraints);
        
        lblDayOfWeek.setText("Day Of Week :");
        lblDayOfWeek.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 10, 0, 0);
        pnlFields.add(lblDayOfWeek, gridBagConstraints);
        
        cmbDayOfWeek.setFont(CoeusFontFactory.getNormalFont());
        cmbDayOfWeek.setPreferredSize(new Dimension(100,20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.insets = new Insets(10, 10, 0, 0);
        pnlFields.add(cmbDayOfWeek,gridBagConstraints);           
        pnlMain.add(pnlFields, new GridBagConstraints());
        pnlButtons.setLayout(new GridBagLayout());
        
        btnGenerate.setFont(CoeusFontFactory.getLabelFont());
        btnGenerate.setText("Generate");
        btnGenerate.setMnemonic('G');
        btnGenerate.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                /* check values for mandatory fields are present or not. */
                /* CASE# IRBEN00077 Comment Begin. */
                /*if((txtStartDate.getText() == null)
                        || (txtStartDate.getText().trim().length() == 0)){
                  CoeusOptionPane.showErrorDialog( 
                        coeusMessageResources.parseMessageKey(
                                "schdGenFrm_exceptionCode.1081") );                      
                }
                else if((txtEndDate.getText() == null) 
                        || (txtEndDate.getText().trim().length() == 0)){
                  CoeusOptionPane.showErrorDialog( 
                        coeusMessageResources.parseMessageKey(
                                "schdGenFrm_exceptionCode.1082") );                                                                      
                }
                else if(cmbFrequency.getSelectedIndex()<0){
                    CoeusOptionPane.showErrorDialog( 
                        coeusMessageResources.parseMessageKey(
                                "schdGenFrm_exceptionCode.1083") );                                         
                    cmbFrequency.requestFocus();
                }
                else{*/
                /* CASE# IRBEN00077 Comment End. */
               /* CASE# IRBEN00077 Begin. */
                if (validateFormData()){
                    /* CASE# IRBEN00077 End. */
                    generateSchedules();
                }
            }
        });
        pnlButtons.add(btnGenerate, new GridBagConstraints());
        
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.setMnemonic('C');
        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                ((CoeusDlgWindow)parentDialog).dispose();
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.insets = new Insets(8, 0, 0, 0);
        pnlButtons.add(btnCancel, gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(0, 8, 25, 0);
        pnlMain.add(pnlButtons, gridBagConstraints);
        setLayout(new BorderLayout());
        add(pnlMain,BorderLayout.CENTER);
        
        // Added by Chandra
        java.awt.Component[] components = {txtStartDate,txtEndDate,txtPlace,txtTime,cmbFrequency,cmbDayOfWeek,btnGenerate,btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        // End Chandra        
    }
    
    /**
     * This method generates the schedules using the values specified in 
     * StartDate, EndDate, cmbFrequency and cmbDayOfWeek.
     */
    private void generateSchedules(){
            
            Date stDate = dtFormat.parse(
                    dtUtils.restoreDate(txtStartDate.getText(),"/-:,"),
                            new ParsePosition(0));
            Date eDate = dtFormat.parse(
                    dtUtils.restoreDate(txtEndDate.getText(),"/-:,"),
                            new ParsePosition(0));
            /* CASE# IRBEN00077 Comment Begin. */
            /* GregorianCalendar genDate = new GregorianCalendar();
            genDate.clear();
            genDate.setTime(stDate);
            Vector generatedSchedule;                        
            if(eDate.compareTo(stDate)< 0){
                CoeusOptionPane.showInfoDialog( 
                        coeusMessageResources.parseMessageKey(
                                "schdGenFrm_exceptionCode.1084") );
                return;
            }
            while(stDate.compareTo(eDate)<=0){
                generatedSchedule = new Vector();
                String strStDate = (genDate.get(Calendar.MONTH)+1)+"/"
                    +genDate.get(Calendar.DATE)+"/"
                    +(genDate.get(Calendar.YEAR));
                // Generate the day of week for schedule date 
                String dayOfWeek = getDayOfWeek(
                        genDate.get(Calendar.DAY_OF_WEEK));
                generatedSchedule.addElement(dtUtils.formatDate(strStDate, 
                        "/-:," , "dd-MMM-yyyy"));
                generatedSchedule.addElement(dayOfWeek);             
                int frequencyCode = Integer.parseInt(((ComboBoxBean)
                        cmbFrequency.getSelectedItem()).getCode());

                switch (frequencyCode){

                    case 3: // yearly
                        genDate.set(Calendar.YEAR,genDate.get(Calendar.YEAR)+1);
                        break;
                    case 4: // monthly
                        genDate.set(Calendar.MONTH,
                                genDate.get(Calendar.MONTH)+1);
                        break;
                    default: //for other than month and Year frequencies
                        int days = getNoOfDays(frequencyCode);
                        int noOfDays = (days == 0 ? 7 :days);
                        genDate.set(Calendar.DATE,genDate.get(Calendar.DATE)
                                +noOfDays);
                        break;             
             }*/
            /* CASE# IRBEN00077 Comment End. */            
            /* CASE# IRBEN00077 Begin. */
            int frequencyCode = Integer.parseInt(((ComboBoxBean)
                    cmbFrequency.getSelectedItem()).getCode());
            int selectedDayOfWeek = 0;
            try{
                selectedDayOfWeek = Integer.parseInt(((ComboBoxBean)
                        cmbDayOfWeek.getSelectedItem()).getCode());
            }catch(java.lang.NumberFormatException e){
                selectedDayOfWeek = 0; // Set to 0. Not Selected.  
            }
            Vector generatedDates = 
               generateDates(stDate, eDate, frequencyCode, selectedDayOfWeek);
            for(int i=0; i<generatedDates.size(); i++){
                Vector generatedSchedule = new Vector();
                Date dateGenDate = (Date)generatedDates.get(i);
                GregorianCalendar genDate = new GregorianCalendar();
                genDate.clear();
                genDate.setTime(dateGenDate);
                String strStDate = (genDate.get(Calendar.MONTH)+1)+"/"
                    +genDate.get(Calendar.DATE)+"/"
                    +(genDate.get(Calendar.YEAR));
                /* Generate the day of week for schedule date */
                String dayOfWeek = getDayOfWeek(
                        genDate.get(Calendar.DAY_OF_WEEK));
                generatedSchedule.addElement(dtUtils.formatDate(strStDate, 
                        "/-:," , "dd-MMM-yyyy"));
                generatedSchedule.addElement(dayOfWeek);
                /* CASE# IRBEN00077 End. */

            /* CASE# IRBEN00077 Comment Begin. */
            /*stDate = dtFormat.parse((genDate.get(Calendar.MONTH)+1)+"/"
                    +genDate.get(Calendar.DATE)+"/"
                    +(genDate.get(Calendar.YEAR)),
                    new ParsePosition(0));
             /* CASE# IRBEN00077 Comment End. */
            
            if(txtPlace.getText()!=null){
                generatedSchedule.addElement(txtPlace.getText());
            }else{
                generatedSchedule.addElement("");
            }
            if(txtTime.getText()!=null){
                generatedSchedule.addElement(txtTime.getText());
            }else{
                generatedSchedule.addElement("");
            }
            generatedSchedules.addElement(generatedSchedule);
        }
        /* dispose this dialog and show generated list dialog. */
        ((CoeusDlgWindow)parentDialog).dispose();
        dlgGenerated = new CoeusDlgWindow(mdiForm,"Generated Schedules",true);
        schedulesListDisplayForm = 
                new SchedulesListDisplayForm(dlgGenerated,
                        generatedSchedules);
        dlgGenerated.getContentPane().add(schedulesListDisplayForm);
        dlgGenerated.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgGenerated.getSize();
        dlgGenerated.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        schedulesListDisplayForm.requestDefaultFocusForComponent();
        dlgGenerated.setVisible(true);
        selectedSchedules = schedulesListDisplayForm.getSelectedSchedules();
        
    }
    
    /**
     * This method is used to get the day of week in String format if it is 
     * given the int between 1 - 7 . 1 represents Sunday, 7 represents Saturday.
     *
     * @param int dayOfWeek which represents the actual day of week
     *
     * @return String Day of Week in String representation corresponding to 
     * dayOfWeek. 
     */
    private String getDayOfWeek(int dayOfWeek){
        String day = "";
        switch (dayOfWeek) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day ="Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        return day;
    }
    
    /**
     * This method is used to fetch the number of days for a frequency
     * @param int frequencyCode which specifies the frequency
     * @return int number of days corresponding to that frequency
     */
    private int getNoOfDays(int frequencyCode) {
        int days =0;
        Vector schFrequencies = getScheduleFrequencies();
        if ((schFrequencies != null) && (schFrequencies.size() > 0)) {
            ScheduleFrequencyBean frqBean = null;
            for(int cnt =0; cnt < schFrequencies.size(); cnt++){
                frqBean = (ScheduleFrequencyBean)schFrequencies.elementAt(cnt);
                if (frqBean.getFrequencyCode() == frequencyCode){
                    days = frqBean.getNoOfDays();
                }
            }
        }
        return days;
    }
    
    /**
     * This method returns the selected schedules from the list of generated 
     * schedules.
     * @return Vector which consists of schedules where each schedule consists 
     * of Sheduled Date, Day of Week, Place and Time.
     */
    public Vector getSelectedSchedules(){
        return selectedSchedules;
    }
    
    /** This method fetches the schedule frequencies from database and sets to
     * <CODE>CoeusComboBox</CODE> which is used to display frequencies.
     */
    public void setScheduleFrequency(){
        Vector schFrequencies = new Vector(5,5);
        
        String connectTo = connectionURL + "/comMntServlet";
        // connect to the database and get the Schedule Frequencies
        RequesterBean request = new RequesterBean();
        request.setFunctionType('F');
        
        AppletServletCommunicator comm = 
                new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                Vector data = (Vector)response.getDataObjects();
                if (data != null ) {
                    schFrequencies = (data.elementAt(0) == null ? new Vector() 
                            : (Vector)data.elementAt(0));
                }
            }
            if(schFrequencies!=null){
                 setScheduleFrequencies(schFrequencies);
                Vector  frequencies = getFrequencies(schFrequencies);
                cmbFrequency.setModel( 
                        (new CoeusComboBox(frequencies,false)).getModel());
            }
        }
    }

    /* CASE# IRBEN00077 Begin.*/
    /* Frequency codes: 
     *Daily - 1
     *Weekly - 2
     *Yearly - 3
     *Monthly - 4
     *BiWeekly - 5
     *BiWeekly - 1rst & 3rd - 6
     *BiWeekly - 2nd & 4th  - 7
     *Monthly - 1rst - 8
     *Monthly - 2nd - 9
     *Monthly - 3rd - 10
     *Monthly - 4th - 11
     */    
    /** Generate dates, based on user entered start date, end date and
     *  selected frequency.
     */
    public Vector generateDates(Date stDate, Date eDate, int frequencyCode, int selectedDayOfWeek){

        int genDayOfWeek = 0;
        Vector generatedDates = new Vector();
        GregorianCalendar gCalStDate = new GregorianCalendar();
        gCalStDate.setTime(stDate);
        GregorianCalendar gCalEDate = new GregorianCalendar();
        gCalEDate.setTime(eDate);
        GregorianCalendar genDate = new GregorianCalendar();
        genDate.setTime(stDate);                  

        while(genDate.before(gCalEDate) || genDate.equals(gCalEDate)){           

            switch(frequencyCode){
                case 1://Daily
                    generatedDates.addElement(genDate.getTime());
                    genDate.add(Calendar.DATE, 1);
                    break;
                case 2://Weekly
                    if(selectedDayOfWeek < 1){
                        generatedDates.addElement(genDate.getTime());
                        genDate.add(Calendar.DATE, 7);
                    }
                    else{  //User has selected a day of the week.
                        genDayOfWeek = genDate.get(Calendar.DAY_OF_WEEK);
                        genDate.set(Calendar.DAY_OF_WEEK, selectedDayOfWeek);
                        if((selectedDayOfWeek - genDayOfWeek)< 0){
                            genDate.add(Calendar.DATE, 7);
                        }
                        if(genDate.before(gCalEDate) || genDate.equals(gCalEDate)){
                            generatedDates.addElement(genDate.getTime());
                        }
                        genDate.add(Calendar.DATE, 7);
                    }
                    break;
                case 3://Yearly
                    generatedDates.addElement(genDate.getTime());
                    genDate.set(Calendar.YEAR,genDate.get(Calendar.YEAR)+1);                      
                    break;
                case 4://Monthly, Day of Week unspecified
                    generatedDates.addElement(genDate.getTime());
                    genDate.set(Calendar.MONTH, 
                            genDate.get(Calendar.MONTH)+1);                       
                    break;
                case 5://BiWeekly
                    if(selectedDayOfWeek < 1){
                        generatedDates.addElement(genDate.getTime());
                        genDate.add(Calendar.DATE, 14);                        
                    }
                    else{ //User has selected a day of the week.
                        genDayOfWeek = genDate.get(Calendar.DAY_OF_WEEK);
                        genDate.set(Calendar.DAY_OF_WEEK, selectedDayOfWeek);
                        if((selectedDayOfWeek - genDayOfWeek)< 0){
                            genDate.add(Calendar.DATE, 7);
                        }
                        if(genDate.before(gCalEDate) || genDate.equals(gCalEDate)){
                            generatedDates.addElement(genDate.getTime());
                        }
                        genDate.add(Calendar.DATE, 14);
                    }
                    break;
                case 6:   //BiWeekly 1rst and 3rd                   
                    genDate.set(Calendar.DAY_OF_MONTH, 1);
                    genDayOfWeek = genDate.get(Calendar.DAY_OF_WEEK);
                    genDate.set(Calendar.DAY_OF_WEEK, selectedDayOfWeek);
                    if((selectedDayOfWeek - genDayOfWeek)< 0){
                        genDate.add(Calendar.DATE, 7);
                    }
                    if((genDate.after(gCalStDate) || genDate.equals(gCalStDate)) && 
                        (genDate.before(gCalEDate) || genDate.equals(gCalEDate))){
                        generatedDates.addElement(genDate.getTime());
                    }
                    genDate.add(Calendar.DATE, 14);
                    if((genDate.after(gCalStDate) || genDate.equals(gCalStDate)) && 
                        (genDate.before(gCalEDate) || genDate.equals(gCalEDate))){
                        generatedDates.addElement(genDate.getTime());
                    }
                    genDate.add(Calendar.MONTH, 1);
                    break;

                case 7:  //BiWeekly 2nd and 4th                    
                    genDate.set(Calendar.DAY_OF_MONTH, 1);
                    genDayOfWeek = genDate.get(Calendar.DAY_OF_WEEK);
                    genDate.set(Calendar.DAY_OF_WEEK, selectedDayOfWeek);
                    if((selectedDayOfWeek - genDayOfWeek)< 0){
                        genDate.add(Calendar.DATE, 7);
                    }
                    genDate.add(Calendar.DATE, 7);
                    if((genDate.after(gCalStDate) || genDate.equals(gCalStDate)) && 
                        (genDate.before(gCalEDate) || genDate.equals(gCalEDate))){
                        generatedDates.addElement(genDate.getTime());
                    }
                    genDate.add(Calendar.DATE, 14);
                    if((genDate.after(gCalStDate) || genDate.equals(gCalStDate)) && 
                        (genDate.before(gCalEDate) || genDate.equals(gCalEDate))){
                        generatedDates.addElement(genDate.getTime());
                    }
                    genDate.add(Calendar.MONTH, 1);
                    break;
                case 8://Monthly - 1rst
                    genDate.set(Calendar.DAY_OF_MONTH, 1);
                    genDayOfWeek = genDate.get(Calendar.DAY_OF_WEEK);
                    genDate.set(Calendar.DAY_OF_WEEK, selectedDayOfWeek);
                    if((selectedDayOfWeek - genDayOfWeek)< 0){
                        genDate.add(Calendar.DATE, 7);
                    }
                    if(genDate.after(gCalStDate) && 
                        (genDate.before(gCalEDate) || genDate.equals(gCalEDate))){
                        generatedDates.addElement(genDate.getTime());                          
                    }
                    genDate.add(Calendar.MONTH, 1);
                    break;                       
                case 9://Monthly - 2nd
                    genDate.set(Calendar.DAY_OF_MONTH, 1);
                    genDayOfWeek = genDate.get(Calendar.DAY_OF_WEEK);
                    genDate.set(Calendar.DAY_OF_WEEK, selectedDayOfWeek);
                    if((selectedDayOfWeek - genDayOfWeek)< 0){
                        genDate.add(Calendar.DATE, 7);
                    }
                    genDate.add(Calendar.DATE, 7);
                    if(genDate.after(gCalStDate) && 
                        (genDate.before(gCalEDate) || genDate.equals(gCalEDate))){
                        generatedDates.addElement(genDate.getTime());
                    }
                    genDate.add(Calendar.MONTH, 1);                        
                    break;
                case 10://Monthly - 3rd
                    genDate.set(Calendar.DAY_OF_MONTH, 1);
                    genDayOfWeek = genDate.get(Calendar.DAY_OF_WEEK);
                    genDate.set(Calendar.DAY_OF_WEEK, selectedDayOfWeek);
                    if((selectedDayOfWeek - genDayOfWeek)< 0){
                        genDate.add(Calendar.DATE, 7);
                    }
                    genDate.add(Calendar.DATE, 14);
                    if(genDate.after(gCalStDate) && 
                        (genDate.before(gCalEDate) || genDate.equals(gCalEDate))){
                        generatedDates.addElement(genDate.getTime());
                    }
                    genDate.add(Calendar.MONTH, 1);                        
                    break;
                case 11://Monthly - 4th
                    genDate.set(Calendar.DAY_OF_MONTH, 1);
                    genDayOfWeek = genDate.get(Calendar.DAY_OF_WEEK);
                    genDate.set(Calendar.DAY_OF_WEEK, selectedDayOfWeek);
                    if((selectedDayOfWeek - genDayOfWeek)< 0){
                        genDate.add(Calendar.DATE, 7);
                    }
                    genDate.add(Calendar.DATE, 21);
                    if(genDate.after(gCalStDate) && 
                        (genDate.before(gCalEDate) || genDate.equals(gCalEDate))){
                        generatedDates.addElement(genDate.getTime());
                    }
                    genDate.add(Calendar.MONTH, 1);                        
                    break;   
            }
        }
        return generatedDates;
    }
    
    /* CASE# IRBEN00077 Begin. */
    /** This method gets the GregorianCalendar int values for the days of
     * the week and sets them in <CODE>CoeusComboBox</CODE> .
     */
    public void setCmbDayOfWeek(){
        Vector daysOfWeek = new Vector();
        daysOfWeek.addElement(new ComboBoxBean("", "Not Selected"));
        for(int i=2; i<8; i++){
            daysOfWeek.addElement(new ComboBoxBean(""+i, getDayOfWeek(i)));
        }
        daysOfWeek.add(new ComboBoxBean(""+1, getDayOfWeek(1)));
        cmbDayOfWeek.setModel(
                        (new CoeusComboBox(daysOfWeek,false)).getModel());
    } 
    /* CASE# IRBEN00077 End. */    
    
    /**
     * This method is used to construct the data for frequency combobox 
     *
     * @param Vector frequencies which are in the form of ScheduleFrequencyBean
     * @return Vector which consists of ComboBoxBeans of frequency code and
     * description
     */
    private Vector getFrequencies(Vector frequencies){
        Vector scheduleTypes = null;
        if (frequencies != null &  frequencies.size() > 0){
            int total = frequencies.size();
            scheduleTypes = new Vector();
            ScheduleFrequencyBean frequencyBean=null;
            for(int cnt =0;cnt < total ;cnt++){
                frequencyBean = (ScheduleFrequencyBean)frequencies.get(cnt);
                scheduleTypes.addElement(
                        new ComboBoxBean(""+frequencyBean.getFrequencyCode(),
                        frequencyBean.getFrequencyDesc().toString()));
            }
            /* CASE# IRBEN00077 Begin. */
            /* Temporary fix before new rows inserted into OSP$COMM_SCHEDULE_FREQUENCY.  */
            /*scheduleTypes.addElement(new ComboBoxBean("6", "BiWeekly- 1st & 3d"));
            scheduleTypes.addElement(new ComboBoxBean("7", "BiWeekly- 2d & 4th"));
            scheduleTypes.addElement(new ComboBoxBean("8", "Monthly- 1st"));
            scheduleTypes.addElement(new ComboBoxBean("9", "Monthly- 2d"));
            scheduleTypes.addElement(new ComboBoxBean("10", "Monthly- 3d"));
            scheduleTypes.addElement(new ComboBoxBean("11", "Monthly- 4th"));  */
            /* CASE# IRBEN00077 End. */
        }
        return scheduleTypes;
    }
    
    /**
     * Internal class which is used to listen to focus events of start date
     * and end date fields. On focusGained the date will be restored to 
     * MM/dd/yyyy, and on focusLost the date will be formatted to dd-MMM-yyyy.
     */
    private class CustomFocusAdapter extends FocusAdapter{
        JTextField txtFld = null;
        String strData = "";
        boolean temporary = false;
        
        public void focusGained(FocusEvent fe){
            txtFld = (JTextField)fe.getSource();
            if(!fe.getSource().equals(txtTime)){
                if ( (txtFld.getText() != null)   
                        && (!txtFld.getText().trim().equals("")) ) {
                    if(!strData.equals(txtFld.getText()) && !temporary){
                        focusDate = dtUtils.restoreDate(txtFld.getText(),"/-:,");
                        txtFld.setText(focusDate);
                    }
                }
            }
        }
        public void focusLost(FocusEvent fe){
            txtFld = (JTextField)fe.getSource();
            /* check whether the focus lost is temporary or permanent*/
            temporary = fe.isTemporary();
            
            String editingValue = null;
            if ( (txtFld.getText() != null) 
                    &&  (!txtFld.getText().trim().equals("")) && (!temporary)) {
                strData = txtFld.getText();
                if(!fe.getSource().equals(txtTime)){
                    String convertedDate = dtUtils.formatDate(txtFld.getText(),
                            "/-:," , "dd-MMM-yyyy");
                    if (convertedDate==null){
                        CoeusOptionPane.showErrorDialog( "Please enter valid date" );
                        // Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE - Start
                        txtFld.setText(CoeusGuiConstants.EMPTY_STRING);
                        // Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE - End
                        txtFld.requestFocus();
                        temporary = true;
                    }else {
                        focusDate = txtFld.getText();
                        txtFld.setText(convertedDate);
                        temporary = false;
                    }
                }
                else{
                    String convertedTime = dtUtils.formatTime(txtFld.getText());
                    if (convertedTime==null){
                        CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                                        "schdGenFrm_exceptionCode.1085"));                        
                        txtFld.requestFocus();
                    }else {
                        txtFld.setText(convertedTime);
                    }
                }
            }
        }
    };

    /* CASE# IRBEN00077 Begin. */
        /** This method is used to check for validity of data in form fields.
     *
     * @return true if all the data is valid, else false.
     */
    public boolean validateFormData() {
        if(cmbFrequency.getSelectedIndex()<0){
            /* Frequency doesn't have a value */
            log( 
                coeusMessageResources.parseMessageKey(
                        "schdGenFrm_exceptionCode.1083") );                                         
            cmbFrequency.requestFocus();
            return false;
        }
        else if(Integer.parseInt(((ComboBoxBean)
                    cmbFrequency.getSelectedItem()).getCode()) > 5){
            if(cmbDayOfWeek.getSelectedIndex()<1){
                /* The selected frequency requires that a day of the week be selected. */
                log(coeusMessageResources.parseMessageKey("schdGenFrm_exceptionCode.1165"));
                cmbDayOfWeek.requestFocus();
                return false;
            }
        }
        if((txtStartDate.getText()== null)
                || (txtStartDate.getText().trim().length() == 0)){
            /* Start Date doesn't have any value */
            log(coeusMessageResources.parseMessageKey("schdGenFrm_exceptionCode.1081"));
            txtStartDate.requestFocus();
            return false;
        }
        else if((txtEndDate.getText()== null)
                || (txtEndDate.getText().trim().length() == 0)){
            /* End Date doesn't have any value */
            log(coeusMessageResources.parseMessageKey("schdGenFrm_exceptionCode.1082"));                    
            txtEndDate.requestFocus();
            return false;
        }        
        else{
            Date startDate = null;
            Date endDate = null;
            try{
                startDate =
                    dtFormat.parse(dtUtils.restoreDate(txtStartDate.getText(),
                            "/:-,"));
                endDate =
                    dtFormat.parse(dtUtils.restoreDate(txtEndDate.getText(),
                            "/:-,"));
            }
            catch(java.text.ParseException pEx){
                pEx.printStackTrace();
            }
            if(endDate.compareTo(startDate)<0){
                /* End Date is earlier than Start Date */
                log(coeusMessageResources.parseMessageKey("schdGenFrm_exceptionCode.1084"));  
                txtEndDate.requestFocus();
                return false;
            }
        }
        return true;
    }
    /* CASE# IRBEN00077 End. */
    
    /* CASE# IRBEN00077 Begin. */
    public void log(String msg){
        CoeusOptionPane.showErrorDialog(msg);
    }
    /* CASE# IRBEN00077 End. */    
    
    public void requestDefaultFocusForComponent(){
        txtStartDate.requestFocusInWindow();
    }
    
    // Variables declaration - do not modify
    /* CASE# IRBEN00077 begin */
    private JLabel lblDayOfWeek;
    private JComboBox cmbDayOfWeek;
    /* CASE# IRBEN00077 end */
    private JPanel pnlMain;
    private JTextField txtEndDate;
    private JPanel pnlFields;
    private JLabel lblFrequency;
    private JLabel lblTime;
    private JTextField txtPlace;
    private JLabel lblStartDate;
    private JTextField txtTime;
    private JLabel lblPlace;
    private JButton btnGenerate;
    private JPanel pnlButtons;
    private JLabel lblEndDate;
    private JComboBox cmbFrequency;
    private JTextField txtStartDate;
    private JButton btnCancel;
    
    // End of variables declaration
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    /* holds the content of text field when focus gained */
    private String focusDate = new String();
    /* used to convert the date values */
    private DateUtils dtUtils = new DateUtils();
    /* holds all the generated schedules */
    Vector generatedSchedules = new Vector(10,10);
    SchedulesListDisplayForm schedulesListDisplayForm=null;
    private Component parentDialog;
    /* holds all the selected schedules */
    private Vector selectedSchedules = new Vector();
    /* used in formatting the dates */
    private SimpleDateFormat dtFormat = 
            new SimpleDateFormat("MM/dd/yyyy");    
    private CoeusDlgWindow dlgGenerated;    
        /* holds all the avaiable schedule frequencies */
    private Vector scheduleFrequencies=null;    
    private CoeusAppletMDIForm mdiForm;        
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
}
