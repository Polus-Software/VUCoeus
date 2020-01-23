/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.utils.birt;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ComboBoxBean;

import edu.mit.coeus.utils.birt.bean.BirtConstants;
import edu.mit.coeus.utils.birt.bean.BirtParameterBean;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import java.util.List;
import javax.swing.JCheckBox;

/**
 *
 * @author sharathk
 */
public class BirtReportGUIBuilder implements ActionListener {

    private static int INSET = 2;
    private String reportTypes[] = {BirtConstants.PDF, BirtConstants.EXCEL, BirtConstants.HTML};
    private List parameterList;
    private List lstComponents;
    private JComboBox reportTypeCombo;
    private Map map = new HashMap();
    private String reportId;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private CoeusDlgWindow reportWindow;

    public BirtReportGUIBuilder() {
        reportWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), "Report Parameter Input", true);
    }

    public JPanel buildReportGui(String reportId, List parameterList) throws Exception {
        this.parameterList = parameterList;
        this.reportId = reportId;
        lstComponents = new ArrayList();
        JPanel reportPanel = new JPanel();

        reportPanel.setLayout(new GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints;

        ComboBoxBean comboBoxBean;
        Map values;
        BirtParameterBean parameterBean;
        JLabel label;
        int guiIndex = 0;
        for (int index = 0; index < parameterList.size(); index++, guiIndex++) {
            parameterBean = (BirtParameterBean) parameterList.get(index);
            // JM 5-30-2012 uncommented to suppress display of hidden parameters
            if(parameterBean.isHidden()) continue; //Do not paint component if hidden @todo remove comment
            //create label
            label = new JLabel(parameterBean.getPromptText());
            label.setFont(CoeusFontFactory.getLabelFont());
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = guiIndex;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints.insets = new java.awt.Insets(INSET, INSET, INSET, INSET);
            reportPanel.add(label, gridBagConstraints);

            //grid constants for the user Component
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = guiIndex;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 0.1;
            gridBagConstraints.insets = new java.awt.Insets(INSET, INSET, INSET, INSET);

            //if (!map.containsKey(parameterBean.getId()) && (parameterBean.getValues() == null || parameterBean.getValues().size() > 1)) {
            if(parameterBean.getDataType() == BirtConstants.BOOLEAN && !parameterBean.isHidden()) {
                //Display Checkbox
                JCheckBox checkbox = new JCheckBox();
                checkbox.setToolTipText(parameterBean.getHelp());
                reportPanel.add(checkbox, gridBagConstraints);
                lstComponents.add(checkbox);
            }else if (parameterBean.getControlType() == BirtConstants.TYPE_COMBO_BOX) {
                JComboBox combobox = new JComboBox();
                combobox.setToolTipText(parameterBean.getHelp());
                values = (HashMap) parameterBean.getValues();
                String defaultValue = parameterBean.getDefaultValue();
                ComboBoxBean selectedComboBoxBean = null;
                if (values != null && values.size() > 0) {
                    Iterator iterator = values.keySet().iterator();
                    Object objKey, objValue;
                    while (iterator.hasNext()) {
                        objKey = iterator.next();
                        objValue = values.get(objKey);
                        comboBoxBean = new ComboBoxBean(objKey.toString(), objValue.toString());
                        combobox.addItem(comboBoxBean);
                        if(defaultValue != null){
                            if(comboBoxBean.getCode().equalsIgnoreCase(defaultValue)){
                               combobox.setSelectedItem(comboBoxBean);
                            }
                        }
                    }
//                        for (int valueIndex = 0; valueIndex < parameterBean.getValues().size(); valueIndex++) {
//                            values = (HashMap) parameterBean.getValues().get(valueIndex);
//
//                            combobox.addItem(comboBoxBean);
//                        }
                }
                reportPanel.add(combobox, gridBagConstraints);
                combobox.setEditable(!parameterBean.isHidden());
                lstComponents.add(combobox);
//                } else if (parameterBean.getControlType() == (ReportConstants.LIST)) {
//                    Vector vec = new Vector();
//                    if (parameterBean.getValues() != null && parameterBean.getValues().size() > 0) {
//                        for (int valueIndex = 0; valueIndex < parameterBean.getValues().size(); valueIndex++) {
//                            comboBoxBean = (ComboBoxBean) parameterBean.getValues().get(valueIndex);
//                            vec.add(comboBoxBean);
//                        }
//                    }
//                    JList list = new JList(vec);
//                    list.setVisibleRowCount(4);
//                    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//                    JScrollPane scrollPane = new JScrollPane(list);
//
//                    reportPanel.add(scrollPane, gridBagConstraints);
//                    lstComponents.add(list);
//                } else if (parameterBean.getDisplay().equalsIgnoreCase(ReportConstants.DATE)) {
//                    JTextField textField = new JTextField();
//                    reportPanel.add(textField, gridBagConstraints);
//                    lstComponents.add(textField);

            } else if (parameterBean.getControlType() == BirtConstants.TYPE_TEXT) {
                JTextField textField = new JTextField();
                textField.setText(parameterBean.getDefaultValue());
                textField.setToolTipText(parameterBean.getHelp());
                reportPanel.add(textField, gridBagConstraints);
                textField.setEnabled(!parameterBean.isHidden());
                textField.setDisabledTextColor(Color.BLACK);
                lstComponents.add(textField);
            }
        // }//map doesn't contain parameter Id (i.e. user selectable parameter)
//            else {
//                if (map.containsKey(parameterBean.getId())) {
//                    //Parameter Set by program
//                    label = new JLabel(map.get(parameterBean.getId()).toString());
//                } else {
//                    //Parameter bean contains only one select value
//                    comboBoxBean = (ComboBoxBean) parameterBean.getValues().get(0);
//                    label = new JLabel(comboBoxBean.getDescription());
//                    Object obj;
//                    if (parameterBean.getDisplay().equals(ReportConstants.DATE)) {
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
//                        obj = simpleDateFormat.parse(comboBoxBean.getCode());
//                    } else {
//                        obj = comboBoxBean.getCode();
//                    }
//                    map.put(parameterBean.getId(), obj);
//
//                }
//                reportPanel.add(label, gridBagConstraints);
//                parameterList.remove(index);
//                index = index - 1;
//            }

        }//End For parameters

        //Add Report type Combo Box
        JLabel lblReportType = new JLabel("Report Format");
        lblReportType.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = guiIndex + 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(INSET, INSET, INSET, INSET);
        reportPanel.add(lblReportType, gridBagConstraints);

        reportTypeCombo = new JComboBox();
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = guiIndex + 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(INSET, INSET, INSET, INSET);
        for (int reportIndex = 0; reportIndex < reportTypes.length; reportIndex++) {
            reportTypeCombo.addItem(reportTypes[reportIndex]);
        }
        reportPanel.add(reportTypeCombo, gridBagConstraints);

        //Add Display report button
        JButton btnPrint = new JButton("Print");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = guiIndex + 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.insets = new java.awt.Insets(INSET, INSET, INSET, INSET);
        reportPanel.add(btnPrint, gridBagConstraints);

        btnPrint.addActionListener(this);

        return reportPanel;
    }

    public void displayReportGui(String reportId, List parameterList) {
        try {
            JLabel header = new JLabel("Report Parameters", SwingConstants.CENTER);
            header.setFont(CoeusFontFactory.getLabelFont());
            reportWindow.getContentPane().add(header, BorderLayout.NORTH);
            reportWindow.getContentPane().add(buildReportGui(reportId, parameterList), BorderLayout.CENTER);
            reportWindow.pack();
            int minWidth = 400, minHeight = 100;
            if (reportWindow.getWidth() < minWidth || reportWindow.getHeight() < minHeight) {
                reportWindow.setSize(reportWindow.getWidth() < minWidth ? minWidth : reportWindow.getWidth(), reportWindow.getHeight() < minHeight ? minHeight : reportWindow.getHeight());
            }

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dlgSize = reportWindow.getSize();
            reportWindow.setLocation(screenSize.width / 2 - (dlgSize.width / 2), screenSize.height / 2 - (dlgSize.height / 2));

            reportWindow.setVisible(true);
            reportWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        /*JFrame frame = new JFrame(reportBean.getTitle());
        frame.getContentPane().add(new JLabel(reportBean.getDescription(), SwingConstants.CENTER), BorderLayout.NORTH);
        frame.getContentPane().add(buildReportGui(reportBean), BorderLayout.CENTER);
        //frame.setSize(400, 300);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
         */

        } catch (Exception exception) {
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        BirtParameterBean parameterBean;
        Map parameterMap = new HashMap(parameterList.size());
        for (int index = 0; index < parameterList.size(); index++) {
            parameterBean = (BirtParameterBean) parameterList.get(index);
            if (parameterBean.isHidden()) {
                parameterMap.put(parameterBean.getName(), parameterBean.getDefaultValue());
                continue;
            }
            if(parameterBean.getDataType() == BirtConstants.BOOLEAN && !parameterBean.isHidden()) {
                JCheckBox checkBox = (JCheckBox) lstComponents.get(index);
                Boolean bool = new Boolean(checkBox.isSelected());
                parameterMap.put(parameterBean.getName(), bool);
            }else if (parameterBean.getControlType() == BirtConstants.TYPE_COMBO_BOX) {
                JComboBox comboBox = (JComboBox) lstComponents.get(index);
                ComboBoxBean bean = (ComboBoxBean) comboBox.getSelectedItem();
                if (bean == null) {
                    if (parameterBean.isRequired()) {
                        CoeusOptionPane.showErrorDialog(CoeusMessageResources.getInstance().parseMessageKey("reportMaintenanceCode.1007"));
                        comboBox.requestFocus();
                    }
                    return;
                }
                int selIndex = comboBox.getSelectedIndex();
                Map map = parameterBean.getValues();
                Object keys[] = map.keySet().toArray();
                Object selObject = keys[selIndex];
                try {
                    selObject = getValue(parameterBean.getDataType(), selObject.toString());
                } catch (CoeusException coeusException) {
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                    comboBox.requestFocus();
                    return;
                }
                parameterMap.put(parameterBean.getName(), selObject);
            //parameterMap.put(parameterBean.getName(), bean.getCode());
//            } else if (parameterBean.getDisplay().equalsIgnoreCase(ReportConstants.LIST)) {
//                JList jList = (JList) lstComponents.get(index);
//                ComboBoxBean bean = (ComboBoxBean) jList.getSelectedValue();
//                if(bean == null) {
//                    CoeusOptionPane.showErrorDialog("Select an item");
//                    jList.requestFocus();
//                    return ;
//                }
//                parameterMap.put(parameterBean.getId(), bean.getCode());
//            } else if (parameterBean.getDisplay().equalsIgnoreCase(ReportConstants.DATE)) {
//                JTextField textField = (JTextField) lstComponents.get(index);
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
//                try {
//                    Date date = simpleDateFormat.parse(textField.getText());
//                    parameterMap.put(parameterBean.getId(), date);
//                } catch (ParseException parseException) {
//                    //parameterMap.put(parameterBean.getId(), textField.getText());
//                    CoeusOptionPane.showErrorDialog("Input Date in MM/dd/yyy format only");
//                    textField.requestFocus();
//                    return ;
//                }

            } else if (parameterBean.getControlType() == BirtConstants.TYPE_TEXT) {
                JTextField textField = (JTextField) lstComponents.get(index);
                if (textField.getText().trim().length() == 0) {
                    if (parameterBean.isRequired()) {
                        CoeusOptionPane.showErrorDialog(CoeusMessageResources.getInstance().parseMessageKey("reportMaintenanceCode.1008"));
                        textField.requestFocus();
                    }
                    return;
                }
                Object value = null;
                try {
                    value = getValue(parameterBean.getDataType(), textField.getText().trim());
                } catch (CoeusException coeusException) {
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                    textField.requestFocus();
                    return;
                }
                parameterMap.put(parameterBean.getName(), value);
            }
        }//End For

        //Report type
        map.put(BirtConstants.REPORT_TYPE, reportTypeCombo.getSelectedItem());

        map.put(DocumentConstants.READER_CLASS, BirtConstants.READER_CLASS);
        map.put(BirtConstants.REPORT_ID, reportId);
        map.put(BirtConstants.PARAMETERS, parameterMap);

        RequesterBean request = new RequesterBean();
        DocumentBean documentBean = new DocumentBean();
        documentBean.setParameterMap(map);

        request.setDataObject(documentBean);
        request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);

        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response != null) {
            Map mapResult = (Map) response.getDataObject();
            String url = (String) mapResult.get(DocumentConstants.DOCUMENT_URL);
            try {
                URL templateURL = new URL(url);
                URLOpener.openUrl(templateURL);
                reportWindow.dispose();
            } catch (Exception exception) {
                exception.printStackTrace();
                CoeusOptionPane.showErrorDialog(exception.getMessage());
            }
        }

    }//End ActionPerformed

    public Object getValue(int dataType, String value) throws CoeusException {
        Object retValue = null;
        if (dataType == BirtConstants.DECIMAL || dataType == BirtConstants.FLOAT) {
            try {
                retValue = new Double(value);
            } catch (NumberFormatException nfe) {
                CoeusException coeusException = new CoeusException(CoeusMessageResources.getInstance().parseMessageKey("reportMaintenanceCode.1009"));
                throw coeusException;
            }
        } else if (dataType == BirtConstants.DATE_TIME) {
            try {
                retValue = simpleDateFormat.parse(value);
            } catch (ParseException parseEx) {
                CoeusException coeusException = new CoeusException(CoeusMessageResources.getInstance().parseMessageKey("reportMaintenanceCode.1010"));
                throw coeusException;
            }
        }else if (dataType == BirtConstants.DATE){
            try {
                //retValue = simpleDateFormat.parse(value);
                retValue = new java.sql.Date(simpleDateFormat.parse(value).getTime());
            } catch (ParseException parseEx) {
                CoeusException coeusException = new CoeusException(CoeusMessageResources.getInstance().parseMessageKey("reportMaintenanceCode.1010"));
                throw coeusException;
            }
        }else {
            retValue = value;
        }
        return retValue;
    }

    public void setParameterValue(String key, Object value) {
        map.put(key, value);
    }
}
