/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.utils.birt;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.utils.birt.bean.BirtReportBean;
import edu.mit.coeus.utils.birt.bean.ReportTypeBean;
import edu.mit.coeus.utils.birt.bean.BirtConstants;
import edu.mit.coeus.utils.birt.controller.MenuActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author sharathk
 */
public class BirtReportMenuBuilder {

    private static BirtReportMenuBuilder birtReportMenuBuilder = null;
    private Map<String, Integer> mapping = new HashMap<String, Integer>();
    private JMenu reportMenu = null;
    private Map<String, List> cache = new HashMap<String, List>();

    private BirtReportMenuBuilder(CoeusAppletMDIForm mdiForm) {
        //Set Report Menu
        int menuCount = mdiForm.getJMenuBar().getMenuCount();
        for (int index = 0; index < menuCount; index++) {
            JMenu menu = mdiForm.getJMenuBar().getMenu(index);
            if (menu.getName().equals("Report")) {
                reportMenu = menu;
                break;
            }
        }
        setMapping();
    }

    public static BirtReportMenuBuilder getInstance(CoeusAppletMDIForm mdiForm) {
        if (birtReportMenuBuilder == null) {
            birtReportMenuBuilder = new BirtReportMenuBuilder(mdiForm);
        }

        return birtReportMenuBuilder;
    }

    private void setMapping() {
        BirtReport report = new BirtReport();
        List<ReportTypeBean> lstReportType = report.getReportType();
        setMapping(lstReportType);
    }

    public void setMapping(List<ReportTypeBean> lstReportType) {
        ReportTypeBean bean;
        if (lstReportType != null) {
            mapping.clear();
            for (int index = 0; index < lstReportType.size(); index++) {
                bean = (ReportTypeBean) lstReportType.get(index);
                mapping.put(bean.getModuleBaseWindow(), new Integer(bean.getTypeCode()));
            }
        }
    }

    private List<JMenuItem> getMenu(CoeusInternalFrame coeusInternalFrame) {
        List lstMenus = null;
        String key = coeusInternalFrame.getClass().getName();
        if (cache.containsKey(key)) {
            lstMenus = cache.get(key);
        } else {
            //Fetch From Server
            if (mapping.containsKey(key)) {
                BirtReport report = new BirtReport();
                Map values = report.getMenu(mapping.get(key), key);
                if(values != null) {
                List lstReports = (List)values.get(BirtConstants.REPORT_TYPES);
                    if (lstReports != null && lstReports.size() > 0) {
                        lstMenus = createMenu(values, coeusInternalFrame);
                    }
                }
                cache.put(key, lstMenus);
            }
        }
        return lstMenus;
    }

    private List createMenu(Map values, CoeusInternalFrame coeusInternalFrame) {
        List lstReport = (List)values.get(BirtConstants.REPORT_TYPES);
        Map paramValues = (Map)values.get(BirtConstants.PARAMETERS);
        List<JMenuItem> list = new ArrayList<JMenuItem>();
        BirtReportBean bean;
        JMenuItem menuItem;
        MenuActionListener menuActionListener = new MenuActionListener(coeusInternalFrame, paramValues, lstReport);
        for (int index = 0; index < lstReport.size(); index++) {
            bean = (BirtReportBean) lstReport.get(index);
            menuItem = new JMenuItem(bean.getReportLabel());
            menuItem.setActionCommand(""+bean.getReportId());
            menuItem.addActionListener(menuActionListener);
            list.add(menuItem);
        }
        return list;
    }

    public void setReportMenu(CoeusInternalFrame coeusInternalFrame) {
        //Insert Birt Reports Related to this Window
        List<JMenuItem> lstMenus = getMenu(coeusInternalFrame);
        if (lstMenus == null) {
            return;
        }
        for (int index = 0; index < lstMenus.size(); index++) {
            reportMenu.add(lstMenus.get(index));
        }
    }

    /**
     * Call this method when Internal frame is Minimized.
     * Don't remove from cache
     * @param coeusInternalFrame
     */
    public void hideReportMenu(CoeusInternalFrame coeusInternalFrame) {
        //Remove Birt Reports Related to this window
        List<JMenuItem> lstMenus = getMenu(coeusInternalFrame);
        if (lstMenus == null) {
            return;
        }
        for (int index = 0; index < lstMenus.size(); index++) {
            reportMenu.remove(lstMenus.get(index));
        }
    }

    /**
     * Call this method when internal Frame is Closed
     * remove from cache
     * @param coeusInternalFrame
     */
    public void removeReportMenu(CoeusInternalFrame coeusInternalFrame) {
        String key = coeusInternalFrame.getClass().getName();
        if (cache.containsKey(key)) {
            cache.remove(key);
        }
    }

}
