/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.utils.birt.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.BeanAddedListener;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.gui.event.BeanUpdatedListener;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.birt.bean.BirtConstants;
import edu.mit.coeus.utils.birt.bean.BirtReportBean;
import edu.mit.coeus.utils.birt.gui.ReportBaseWindow;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


/**
 *
 * @author sharathk
 */
public class ReportBaseWindowController extends Controller implements ActionListener, MouseListener {

    private ReportBaseWindow reportbaseWindow;
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + "/BirtServlet";
    private ReportTableModel reportTableModel;
    private int columnWidth[] = {75, 100, 100, 250, 200, 200};
    private CoeusMessageResources coeusMessageResources;
    private ReportDetailsController reportDetailsController;

    /**
     * Constructor
     * @param frameTitle
     * @param mdiForm
     */
    public ReportBaseWindowController(String frameTitle, CoeusAppletMDIForm mdiForm) {
        reportbaseWindow = new ReportBaseWindow(frameTitle, mdiForm);
        init();
    }

    private void init() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        List lstReports = getAllReports();
        reportTableModel = new ReportTableModel(lstReports);
        reportbaseWindow.tblReports.setFont(CoeusFontFactory.getNormalFont());

        reportbaseWindow.scrollPane.getViewport().setBackground(Color.white);
        reportbaseWindow.scrollPane.setForeground(Color.white);

        reportbaseWindow.tblReports.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        javax.swing.table.JTableHeader header = reportbaseWindow.tblReports.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(CoeusFontFactory.getLabelFont());

        reportbaseWindow.tblReports.setModel(reportTableModel);

        TableColumn tableColumn;
        for (int index = 0; index < columnWidth.length; index++) {
            tableColumn = reportbaseWindow.tblReports.getColumnModel().getColumn(index);
            tableColumn.setPreferredWidth(columnWidth[index]);
            tableColumn.setMaxWidth(columnWidth[index]);
            tableColumn.setMinWidth(columnWidth[index]);
            tableColumn.setWidth(columnWidth[index]);
        }

        addBeanAddedListener(reportTableModel, edu.mit.coeus.utils.birt.bean.BirtReportBean.class);
        addBeanUpdatedListener(reportTableModel, edu.mit.coeus.utils.birt.bean.BirtReportBean.class);

        reportbaseWindow.addInternalFrameListener(new InternalFrameAdapter() {

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                super.internalFrameClosed(e);
                cleanUp();
            }
        });
    }

    @Override
    protected void cleanUp() {
        super.cleanUp();
        removeBeanAddedListener(reportTableModel, edu.mit.coeus.utils.birt.bean.BirtReportBean.class);
        removeBeanUpdatedListener(reportTableModel, edu.mit.coeus.utils.birt.bean.BirtReportBean.class);
    }
    
    /**
     * Retreives all reports to be displayed in the base window
     * @return List of BirtReportBeans
     */
    private List getAllReports() {
        List lstReports = null;
        try {
            RequesterBean request = new RequesterBean();
            ResponderBean response = null;
            request.setFunctionType(BirtConstants.GET_ALL_REPORTS);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            response = comm.getResponse();
            if (response != null) {
                //Okay
                if (response.getDataObject() instanceof List) {
                    lstReports = (List) response.getDataObject();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        return lstReports;
    }

    @Override
    public Component getControlledUI() {
        return reportbaseWindow;
    }

    @Override
    public void setFormData(Object data) throws CoeusException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getFormData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formatFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean validate() throws CoeusUIException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void registerComponents() {
        reportbaseWindow.btnAddReport.addActionListener(this);
        reportbaseWindow.btnModifyReport.addActionListener(this);
        reportbaseWindow.btnDisplayReport.addActionListener(this);
        reportbaseWindow.btnDeleteReport.addActionListener(this);
        reportbaseWindow.btnClose.addActionListener(this);

        reportbaseWindow.mnuItmAddReport.addActionListener(this);
        reportbaseWindow.mnuItmModifyReport.addActionListener(this);
        reportbaseWindow.mnuItmDisplayReport.addActionListener(this);
        reportbaseWindow.mnuItmDeleteReport.addActionListener(this);

        reportbaseWindow.tblReports.addMouseListener(this);
    }

    @Override
    public void saveFormData() throws CoeusException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void display() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        try {
            CoeusGuiConstants.getMDIForm().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            blockEvents(true);

            if (src.equals(reportbaseWindow.mnuItmAddReport) || src.equals(reportbaseWindow.btnAddReport)) {
                displayReportDetails(BirtConstants.NEW_REPORT, TypeConstants.ADD_MODE, "Add Report", -1);
            } else if (src.equals(reportbaseWindow.mnuItmModifyReport) || src.equals(reportbaseWindow.btnModifyReport)) {
                int selectedRow = reportbaseWindow.tblReports.getSelectedRow();
                if (selectedRow == -1) {
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("defineRules_exceptionCode.1051"));
                } else {
                    //Display Modify Window
                    BirtReportBean bean = reportTableModel.getBean(selectedRow);
                    displayReportDetails(BirtConstants.EDIT_REPORT, TypeConstants.MODIFY_MODE, "Edit Report", bean.getReportId());
                }
            } else if (src.equals(reportbaseWindow.mnuItmDisplayReport) || src.equals(reportbaseWindow.btnDisplayReport)) {
                int selectedRow = reportbaseWindow.tblReports.getSelectedRow();
                if (selectedRow == -1) {
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("defineRules_exceptionCode.1051"));
                } else {
                    //Display Report
                    BirtReportBean bean = reportTableModel.getBean(selectedRow);
                    displayReportDetails(BirtConstants.EDIT_REPORT, TypeConstants.DISPLAY_MODE, "View Report", bean.getReportId());
                }
            } else if (src.equals(reportbaseWindow.mnuItmDeleteReport) || src.equals(reportbaseWindow.btnDeleteReport)) {
                int selectedRow = reportbaseWindow.tblReports.getSelectedRow();
                if (selectedRow == -1) {
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("defineRules_exceptionCode.1054"));
                } else {
                    //Delete Report
                    int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("defineRules_exceptionCode.1055"), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                    if (selection == CoeusOptionPane.SELECTION_YES) {
                        //Delete Report
                        RequesterBean request = new RequesterBean();
                        ResponderBean response = null;
                        request.setFunctionType(BirtConstants.DELETE_REPORT);
                        BirtReportBean bean = reportTableModel.getBean(selectedRow);
                        request.setDataObject(bean);
                        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
                        comm.send();
                        response = comm.getResponse();
                        if (response != null && response.hasResponse()) {
                            //Delete Succesfull
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("customElementExceptionCode.1556"));
                            reportTableModel.deleteRow(selectedRow);
                        }
                    }
                }
            } else if (src.equals(reportbaseWindow.btnClose)) {
                reportbaseWindow.dispose();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        } finally {
            CoeusGuiConstants.getMDIForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            blockEvents(false);
        }
    }

    private void displayReportDetails(char functionType, char mode, String title, int reportId) throws Exception {
        Map map = getData(functionType, reportId);
        if (reportDetailsController == null) {
            reportDetailsController = new ReportDetailsController();
        }
        reportDetailsController.setTitle(title);
        reportDetailsController.setMode(mode);
        reportDetailsController.reset();
        reportDetailsController.setFormData(map);
        reportDetailsController.display();
    }

    private Map getData(char functiontype, int reportId) throws CoeusException {
        Map map = null;
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setFunctionType(functiontype);
        request.setDataObject(new Integer(reportId));
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        response = comm.getResponse();
        if (response != null && response.hasResponse()) {
            map = (Map) response.getDataObject();
        }
        return map;
    }

    public void mouseClicked(MouseEvent e) {
        try {
            CoeusGuiConstants.getMDIForm().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            blockEvents(true);
            if (e.getClickCount() == 2) {
                int selectedRow = reportbaseWindow.tblReports.getSelectedRow();
                if (selectedRow == -1) {
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("defineRules_exceptionCode.1051"));
                } else {
                    //Display Report
                    BirtReportBean bean = reportTableModel.getBean(selectedRow);
                    displayReportDetails(BirtConstants.EDIT_REPORT, TypeConstants.DISPLAY_MODE, "View Report", bean.getReportId());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        } finally {
            CoeusGuiConstants.getMDIForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            blockEvents(false);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
/**
 * TableModel for the base window table displaying the Report Details
 * @author sharathk
 */
class ReportTableModel extends DefaultTableModel implements BeanAddedListener, BeanUpdatedListener {

    private List tableData;
    private String columnNames[] = {"Report Id", "Report Label", "Report Group", "Report Derscription", "Last Update", "Last Design Update"};
    private static final String DATE_FORMAT = "dd-MMM-yyyy hh:mm:ss a";

    ReportTableModel(List tableData) {
        this.tableData = tableData;
    }

    @Override
    public int getRowCount() {
        return tableData == null ? 0 : tableData.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BirtReportBean bean = (BirtReportBean) tableData.get(rowIndex);
        Object retValue = null;
        switch (columnIndex) {
            case 0:
                retValue = bean.getReportId();
                break;
            case 1:
                retValue = bean.getReportLabel();
                break;
            case 2:
                retValue = bean.getReportTypeDesc();
                break;
            case 3:
                retValue = bean.getReportDescription();
                break;
            case 4:
                retValue = DateUtils.formatDate(bean.getUpdateTimestamp(), DATE_FORMAT) + " by " + bean.getUpdateUser();
                break;
            case 5:
                retValue = DateUtils.formatDate(bean.getDesignUpdateTimestamp(), DATE_FORMAT) + " by " + bean.getDesignUpdateUser();
                break;
        }

        return retValue;
    }

    public BirtReportBean getBean(int row) {
        BirtReportBean bean = (BirtReportBean) tableData.get(row);
        return bean;
    }

    public void deleteRow(int row) {
        tableData.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void beanAdded(BeanEvent beanEvent) {
        BirtReportBean bean = (BirtReportBean) beanEvent.getObject();
        tableData.add(bean);
        fireTableRowsInserted(tableData.size(), tableData.size());
    }

    public void beanUpdated(BeanEvent beanEvent) {
        BirtReportBean bean = (BirtReportBean) beanEvent.getObject();
        BirtReportBean localBean;
        for (int index = 0; index < tableData.size(); index++) {
            localBean = (BirtReportBean) tableData.get(index);
            if (localBean.getReportId() == bean.getReportId()) {
                tableData.set(index, bean);
                fireTableRowsUpdated(index, index);
                return;
            }
        }

    }
}