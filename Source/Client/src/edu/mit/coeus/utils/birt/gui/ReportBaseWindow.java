/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.utils.birt.gui;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.InternalFrameListener;

/**
 *
 * @author sharathk
 */
public class ReportBaseWindow extends CoeusInternalFrame implements InternalFrameListener{

    private CoeusAppletMDIForm mdiForm;

    // Menu items for organization
    public CoeusMenuItem mnuItmAddReport,  mnuItmModifyReport,  mnuItmDisplayReport,  mnuItmDeleteReport;
    public CoeusToolBarButton btnAddReport,  btnModifyReport,  btnDeleteReport,  btnDisplayReport, btnClose;
    public JTable tblReports;
    public JScrollPane scrollPane;

    // unique frame name
    private String frameName;

    public ReportBaseWindow(String frameTitle, CoeusAppletMDIForm mdiForm) {
        super(frameTitle, mdiForm, LIST_MODE);
        this.mdiForm = mdiForm;
        this.frameName = frameTitle;
        init();
    }

    private void init() {
        //tblReports
        tblReports = new JTable();

        getContentPane().setLayout(new BorderLayout());
        scrollPane = new JScrollPane(tblReports);
        getContentPane().add(scrollPane);

        // associate the menu to the frame
        setFrameMenu(reportEditMenu());
        setFrameToolBar(reportToolBar());
        setFrame(frameName);
        mdiForm.putFrame(CoeusGuiConstants.REPORT_FRAME_TITLE, this);
        // add this frame component with mdi form.
        mdiForm.getDeskTopPane().add(this);
        setVisible(true);
    }


    /**
     * Constructs Report ToolBar.
     *
     * @returns JToolBar Report Toolbar
     */
    private JToolBar reportToolBar() {
        JToolBar toolbar = new JToolBar();

        btnAddReport = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)), null, "Add Report");
        btnModifyReport = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)), null, "Modify Report");
        btnDisplayReport = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)), null, "Display Report");
        btnDeleteReport = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)), null, "Delete Report");
        btnClose = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)), null, "Close");

        toolbar.add(btnAddReport);
        toolbar.add(btnModifyReport);
        toolbar.add(btnDisplayReport);
        toolbar.add(btnDeleteReport);
        toolbar.add(btnClose);

        toolbar.setFloatable(false);
        return toolbar;
    }

    /**
     * constructs Report menu
     *
     * @return CoeusMenu organization edit menu
     */
    private CoeusMenu reportEditMenu() {
        CoeusMenu mnuReport = null;
        Vector fileChildren = new Vector();
        mnuItmAddReport = new CoeusMenuItem("Add", null, true, true);
        mnuItmAddReport.setMnemonic('A');

        mnuItmModifyReport = new CoeusMenuItem("Modify", null, true, true);
        mnuItmModifyReport.setMnemonic('M');

        mnuItmDisplayReport = new CoeusMenuItem("Display", null, true, true);
        mnuItmDisplayReport.setMnemonic('P');

        mnuItmDeleteReport = new CoeusMenuItem("Delete", null, true, true);
        mnuItmDeleteReport.setMnemonic('D');

        fileChildren.add(mnuItmAddReport);
        fileChildren.add(mnuItmModifyReport);
        fileChildren.add(mnuItmDisplayReport);
        fileChildren.add(mnuItmDeleteReport);

        mnuReport = new CoeusMenu("Edit", null, fileChildren, true, true);
        mnuReport.setMnemonic('E');
        return mnuReport;
    }

    @Override
    public void saveActiveSheet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveAsActiveSheet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
