/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.gui.menu;

import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.birt.BirtReport;
import edu.mit.coeus.utils.birt.bean.BirtConstants;
import edu.mit.coeus.utils.birt.bean.BirtReportBean;
import edu.mit.coeus.utils.birt.bean.ReportTypeBean;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author sharathk
 */
public class CoeusReportMenu extends CoeusMenu implements ActionListener {

    private CoeusMenu coeusMenu;

    public CoeusReportMenu() {
    }

    public JMenu getMenu() {
        java.util.Vector reportChildren = new java.util.Vector();
        coeusMenu = new CoeusMenu("Report", null, reportChildren, true, true);
        try {
            BirtReport birtReport = new BirtReport();
            List<ReportTypeBean> lstReportType = birtReport.getReportType();
            ReportTypeBean bean;
            Integer reportTypeCode = null;
            if (lstReportType != null) {
                for (int index = 0; index < lstReportType.size(); index++) {
                    bean = (ReportTypeBean) lstReportType.get(index);
                    if (bean.getModuleBaseWindow().equalsIgnoreCase(BirtConstants.GLOBAL_MENU_CLASS)) {
                        reportTypeCode = new Integer(bean.getTypeCode());
                        break;
                    }
                }
            }
            Map map = birtReport.getMenu(reportTypeCode, BirtConstants.GLOBAL_MENU_CLASS);
            List<BirtReportBean> lstReports = (List) map.get(BirtConstants.REPORT_TYPES);

            if (lstReports != null) {
                BirtReportBean birtReportBean;
                JMenuItem menuItem;
                for (int index = 0; index < lstReports.size(); index++) {
                    birtReportBean = (BirtReportBean) lstReports.get(index);
                    menuItem = new JMenuItem(birtReportBean.getReportLabel());
                    menuItem.setActionCommand("" + birtReportBean.getReportId());
                    menuItem.addActionListener(this);
                    coeusMenu.add(menuItem);
                }
            }
        } catch (Exception exception) {
            CoeusOptionPane.showErrorDialog(exception.getMessage() == null ? "Null" : exception.getMessage());
        }
        coeusMenu.setMnemonic('R');
        return coeusMenu;
    }

    public void removeMenuItem() {
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        BirtReport birtReport = new BirtReport();
        birtReport.displayReport(menuItem.getActionCommand(), null, null, null);
    }
}
