/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.utils.birt.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.birt.bean.BirtConstants;
import edu.mit.coeus.utils.birt.bean.BirtReportBean;
import edu.mit.coeus.utils.birt.bean.ReportTypeBean;
import edu.mit.coeus.utils.birt.gui.ReportDetails;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Controller for Adding, Modifying and viewing BirtReport Details.
 * @author sharathk
 */
public class ReportDetailsController extends Controller implements ActionListener {

    private ReportDetails reportDetails = new ReportDetails();
    private String title;
    private static int WIDTH = 600;
    private static int HEIGHT = 300;
    private JFileChooser fileChooser = null;
    private CoeusDlgWindow dlgWindow = null;
    private File reportFile;
    private List lstRights,  lstReportTypes;
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + "/BirtServlet";
    private char mode;
    private boolean fileUpdated;
    private BirtReportBean reportBean;
    private CoeusMessageResources coeusMessageResources;
    private static final String DATE_FORMAT = "dd-MMM-yyyy hh:mm:ss a";
    private static final String EMPTY_STING = "";
    private static final String NO_RIGHT_CHECK_REQUIRED="NOT_REQUIRED";

    public ReportDetailsController() {
        init();
        registerComponents();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }

    private void init() {
        dlgWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), "", true);
        dlgWindow.setResizable(false);
        dlgWindow.setFont(CoeusFontFactory.getLabelFont());
        dlgWindow.getContentPane().add(reportDetails);
        dlgWindow.setSize(WIDTH, HEIGHT);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgWindow.getSize();
        dlgWindow.setLocation(screenSize.width / 2 - (dlgSize.width / 2),
                screenSize.height / 2 - (dlgSize.height / 2));


        fileUpdated = false;
    }

    @Override
    public Component getControlledUI() {
        return reportDetails;
    }

    @Override
    public void setFormData(Object data) throws CoeusException {
        Map map = (Map) data;
        lstRights = (List) map.get(BirtConstants.RIGHTS);
        lstReportTypes = (List) map.get(BirtConstants.REPORT_TYPES);

        if (map.containsKey(BirtConstants.REPORT_BEAN)) {
            reportBean = (BirtReportBean) map.get(BirtConstants.REPORT_BEAN);
            reportDetails.txtId.setText(""+reportBean.getReportId());
            reportDetails.txtLabel.setText(reportBean.getReportLabel());
            reportDetails.txtArDescription.setText(reportBean.getReportDescription());
            reportDetails.txtFile.setText(reportBean.getReportName());

        }
        setTimestamps();

        reportDetails.cmbRight.removeAllItems();
        ComboBoxBean comboBoxBean;
        reportDetails.cmbRight.addItem(NO_RIGHT_CHECK_REQUIRED);
        for (int index = 0; index < lstRights.size(); index++) {
            comboBoxBean = (ComboBoxBean) lstRights.get(index);
            reportDetails.cmbRight.addItem(comboBoxBean.getDescription());
            if (reportBean != null && comboBoxBean.getCode().equalsIgnoreCase(reportBean.getRight())) {
                reportDetails.cmbRight.setSelectedIndex(index+1);
            }
        }

        //populate Report Types combo
        reportDetails.cmbType.removeAllItems();
        ReportTypeBean reportTypeBean;
        for (int index = 0; index < lstReportTypes.size(); index++) {
            reportTypeBean = (ReportTypeBean) lstReportTypes.get(index);
            reportDetails.cmbType.addItem(reportTypeBean.getTypeDescription());
            if (reportBean != null && reportTypeBean.getTypeCode() == reportBean.getReportTypeCode()) {
                reportDetails.cmbType.setSelectedIndex(index);
            }
        }
        formatFields();

    }

    private void setTimestamps() {
        reportDetails.txtTimestamp.setText(reportBean == null || reportBean.getUpdateTimestamp() == null ? EMPTY_STING : DateUtils.formatDate(reportBean.getUpdateTimestamp(), DATE_FORMAT));
        reportDetails.txtUpdateUser.setText(reportBean == null ? EMPTY_STING : reportBean.getUpdateUser());

        reportDetails.txtTemplateTimestamp.setText(reportBean == null || reportBean.getDesignUpdateTimestamp() == null ? EMPTY_STING : DateUtils.formatDate(reportBean.getDesignUpdateTimestamp(), DATE_FORMAT));
        reportDetails.txtTemplateUpdateUser.setText(reportBean == null ? EMPTY_STING : reportBean.getDesignUpdateUser());
    }

    @Override
    public Object getFormData() {
        BirtReportBean bean = new BirtReportBean();
        bean.setReportId(reportBean == null ? -1 : reportBean.getReportId());
        bean.setReportLabel(reportDetails.txtLabel.getText());
        bean.setReportDescription(reportDetails.txtArDescription.getText());
        if (fileUpdated) {
            try {
                FileInputStream is = new FileInputStream(reportFile);
                byte reportByte[] = new byte[is.available()];
                is.read(reportByte);
                is.close();
                bean.setReport(reportByte);
                bean.setReportName(reportFile.getName());
            } catch (Exception exception) {
                exception.printStackTrace();
                CoeusOptionPane.showErrorDialog(exception.getMessage());
            }
        }
        int rightIndex = reportDetails.cmbRight.getSelectedIndex() - 1; //Since we added Right not Required
        if (rightIndex != -1) {
            ComboBoxBean rightsBean = (ComboBoxBean) lstRights.get(rightIndex);
            bean.setRight(rightsBean.getCode());
        }else{
            //Right not Required
            bean.setRight(null);
        }

        int typeIndex = reportDetails.cmbType.getSelectedIndex();
        if (typeIndex != -1) {
            ReportTypeBean reportTypeBean = (ReportTypeBean) lstReportTypes.get(typeIndex);
            bean.setReportTypeCode(reportTypeBean.getTypeCode());
            bean.setReportTypeDesc(reportTypeBean.getTypeDescription());
        }
        
        if (mode != TypeConstants.ADD_MODE) {
            bean.setAwUpdateTimestamp(reportBean.getUpdateTimestamp());
            bean.setAwDesignUpdateTimestamp(reportBean.getDesignUpdateTimestamp());
            bean.setUpdateUser(reportBean.getUpdateUser());

            bean.setUpdateTimestamp(reportBean.getUpdateTimestamp());
            bean.setDesignUpdateTimestamp(reportBean.getDesignUpdateTimestamp());
            bean.setDesignUpdateUser(reportBean.getDesignUpdateUser());
        }
        return bean;
    }

    @Override
    public void formatFields() {
        boolean enable = getMode() == TypeConstants.DISPLAY_MODE ? false : true;

        reportDetails.btnFileChooser.setEnabled(enable);
        reportDetails.txtId.setEnabled(false);
        reportDetails.txtId.setDisabledTextColor(Color.BLACK);
        reportDetails.txtFile.setEnabled(false);
        reportDetails.txtFile.setDisabledTextColor(Color.BLACK);
        reportDetails.txtLabel.setEnabled(enable);
        reportDetails.txtLabel.setDisabledTextColor(Color.BLACK);
        reportDetails.txtArDescription.setEnabled(enable);
        reportDetails.txtArDescription.setDisabledTextColor(Color.BLACK);
        reportDetails.txtArDescription.setOpaque(enable);
        reportDetails.cmbRight.setEnabled(enable);
        reportDetails.cmbType.setEnabled(enable);

        reportDetails.btnSave.setEnabled(enable);
        //Disable Download in New Mode
        reportDetails.btnDownload.setEnabled(getMode() != TypeConstants.ADD_MODE);
    }

    @Override
    public boolean validate() {
        if (reportDetails.txtLabel.getText().trim().length() == 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("reportMaintenanceCode.1002"));
            reportDetails.txtLabel.requestFocus();
            return false;
        } else if (reportDetails.txtArDescription.getText().trim().length() == 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("reportMaintenanceCode.1003"));
            reportDetails.txtArDescription.requestFocus();
            return false;
        } else if (mode == TypeConstants.ADD_MODE && !fileUpdated) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("reportMaintenanceCode.1004"));
            reportDetails.txtFile.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void registerComponents() {
        reportDetails.btnFileChooser.addActionListener(this);
        reportDetails.btnDownload.addActionListener(this);
        reportDetails.btnSave.addActionListener(this);
        reportDetails.btnCancel.addActionListener(this);

        dlgWindow.addEscapeKeyListener(new AbstractAction("escPressed") {
            public void actionPerformed(ActionEvent ae) {
                performCloseOperation();
                return;
            }
        });

        //dlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DISPOSE_ON_CLOSE);
        dlgWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                performCloseOperation();
            }
        });
    }

    @Override
    public void saveFormData() throws CoeusException {
        try {
            dlgWindow.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

            BirtReportBean bean = (BirtReportBean) getFormData();
            RequesterBean request = new RequesterBean();
            ResponderBean response = null;
            if (isReportUpdated(bean)) {
                request.setFunctionType(BirtConstants.SAVE_REPORT_DETAILS);
            } else if (fileUpdated) {
                request.setFunctionType(BirtConstants.SAVE_REPORT_TEMPLATE);
            } else {
                //Nothing to Save
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("reportMaintenanceCode.1001"));
                return;
            }

            if (mode == TypeConstants.ADD_MODE) {
                bean.setAcType(TypeConstants.INSERT_RECORD);
            } else if (mode == TypeConstants.MODIFY_MODE) {
                bean.setAcType(TypeConstants.UPDATE_RECORD);
            }

            request.setDataObject(bean);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            response = comm.getResponse();
            if (response != null && response.hasResponse()) {
                reportBean = (BirtReportBean) response.getDataObject();
                setTimestamps();
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setBean(reportBean);
                beanEvent.setObject(reportBean);
                if (mode == TypeConstants.ADD_MODE) {
                    mode = TypeConstants.MODIFY_MODE;
                    reportDetails.txtId.setText(""+reportBean.getReportId());
                    reportDetails.btnDownload.setEnabled(true);
                    fireBeanAdded(beanEvent);
                } else if (mode == TypeConstants.MODIFY_MODE) {
                    fireBeanUpdated(beanEvent);
                }
                fileUpdated = false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            //CoeusOptionPane.showErrorDialog(exception.getMessage());
            throw new CoeusException(exception.getMessage());
        } finally {
            dlgWindow.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        }
    }

    @Override
    public void display() {
        dlgWindow.setTitle(getTitle());

        dlgWindow.setVisible(true);
        dlgWindow.requestFocusInWindow();
    }

    /**
     * Action to be performed while performing a close operation
     */
    public void performCloseOperation() {
        if (mode != TypeConstants.DISPLAY_MODE && (isReportUpdated((BirtReportBean) getFormData()) || fileUpdated)) {
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("saveConfirmCode.1002"), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_CANCEL);
            if (selection == CoeusOptionPane.SELECTION_YES) {
                try {
                    if (validate()) {
                        saveFormData();
                        dlgWindow.dispose();
                    }
                } catch (CoeusException coeusException) {
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                }
            } else if (selection == CoeusOptionPane.SELECTION_NO) {
                dlgWindow.dispose();
            }
        } else {
            dlgWindow.dispose();
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            Object source = e.getSource();
            if (source.equals(reportDetails.btnFileChooser)) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new ReportFileFilter());
                }
                fileChooser.setSelectedFile(new File(""));
                int selection = fileChooser.showOpenDialog(dlgWindow);
                if (selection == JFileChooser.APPROVE_OPTION) {
                    reportFile = fileChooser.getSelectedFile();
                    reportDetails.txtFile.setText(reportFile.getAbsolutePath());
                    fileUpdated = true;
                }
            } else if (source.equals(reportDetails.btnSave)) {
                if (validate()) {
                    saveFormData();
                    dlgWindow.dispose();
                }
            } else if (source.equals(reportDetails.btnCancel)) {
                performCloseOperation();
            } else if (source.equals(reportDetails.btnDownload)) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new ReportFileFilter());
                }
                File saveDefault = new File(reportBean.getReportName());
                fileChooser.setSelectedFile(saveDefault);
                int selection = fileChooser.showSaveDialog(dlgWindow);
                if (selection == JFileChooser.APPROVE_OPTION) {
                    File saveTo = fileChooser.getSelectedFile();
                    RequesterBean request = new RequesterBean();
                    request.setDataObject(reportBean.getReportId());
                    request.setFunctionType(BirtConstants.DOWNLOAD_REPORT);
                    AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
                    comm.send();
                    ResponderBean response = comm.getResponse();
                    if (response != null) {
                        byte fileBytes[] = (byte[]) response.getDataObject();
                        if (fileBytes != null) {
                            FileOutputStream fos = new FileOutputStream(saveTo);
                            fos.write(fileBytes);
                            fos.close();
                        }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }

    private boolean isReportUpdated(BirtReportBean localBean) {
        boolean beanUpdated = true;
        if (reportBean != null && reportBean.getReportLabel().equalsIgnoreCase(localBean.getReportLabel()) &&
                reportBean.getReportDescription().equalsIgnoreCase(localBean.getReportDescription()) &&
                reportBean.getReportTypeCode() == localBean.getReportTypeCode() &&
                //bug Fix:throws null pointer if one of the rights is null - START
                (reportBean.getRight() == null && localBean.getRight() == null ||
                (reportBean.getRight()!=null && localBean.getRight() != null && reportBean.getRight().equalsIgnoreCase(localBean.getRight()))
                )) {
                //bug Fix:throws null pointer if one of the rights is null - END
            beanUpdated = false;
        }
        return beanUpdated;
    }

    public void reset() {
        fileUpdated = false;
        reportBean = null;
        reportDetails.txtId.setText(EMPTY_STING);
        reportDetails.txtLabel.setText(EMPTY_STING);
        reportDetails.txtArDescription.setText(EMPTY_STING);
        reportDetails.txtFile.setText(EMPTY_STING);

        if (reportDetails.cmbType.getSelectedIndex() != -1) {
            reportDetails.cmbType.setSelectedIndex(0);
            reportDetails.cmbRight.setSelectedIndex(0);
        }
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the mode
     */
    public char getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(char mode) {
        this.mode = mode;
    }
}

class ReportFileFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        if (file.isDirectory() || file.getName().endsWith("rptdesign")) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public String getDescription() {
        return "Birt Report File";
    }
}