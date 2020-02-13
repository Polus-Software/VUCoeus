/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.birt.controller;

import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.utils.birt.BirtReport;
import edu.mit.coeus.utils.birt.bean.BirtReportBean;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.JMenuItem;

/**
 * This class Listens to Birt Report Menu Clicked Events and
 * and displays Birt GUI, with the help of BirtReport.
 * @author sharathk
 */
public class MenuActionListener implements ActionListener{

    private CoeusInternalFrame cif;
    private Map paramValues;
    private List lstReport;

    public MenuActionListener(CoeusInternalFrame cif, Map paramValues, List lstReport) {
        this.cif = cif;
        this.paramValues = paramValues;
        this.lstReport = lstReport;
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        if (menuItem != null) {
            BirtReport birtReport = new BirtReport();
            BirtReportBean bean = null;
            for(int index=0;index<lstReport.size(); index++){
                bean = (BirtReportBean) lstReport.get(index);
                if(bean.getReportId() == Integer.parseInt(menuItem.getActionCommand())){
                    break;
                }
            }
            birtReport.displayReport(menuItem.getActionCommand(), paramValues, cif, bean);
        }
    }
}
