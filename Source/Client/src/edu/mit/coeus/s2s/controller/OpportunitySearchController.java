/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.BlockingGlassPane;
import edu.mit.coeus.s2s.bean.DBOpportunityInfoBean;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.s2s.bean.ProcessGrantsSubmission;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.gui.OpportunitySearch;
import edu.mit.coeus.s2s.validator.UniqueSchemaNotFoundException;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author sharathk
 */
public class OpportunitySearchController implements ActionListener {

    private OpportunitySearch opportunitySearch = new OpportunitySearch();
    private CoeusDlgWindow dlgOppSearch;
    private CoeusAppletMDIForm mdiForm;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;
    private static final String s2sServlet = CoeusGuiConstants.CONNECTION_URL + "/S2SServlet";
    private OpportunityInfoBean oppInfoBean;

    public OpportunitySearchController(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        registerComponents();
        postInitComponents();
    }

    public Component getControlledUI() {
        return opportunitySearch;
    }

    /** registers GUI Components with event Listeners.
     */
    public void registerComponents() {
        opportunitySearch.btnOk.addActionListener(this);
        opportunitySearch.btnCancel.addActionListener(this);
    }

    private void postInitComponents() {
        dlgOppSearch = new CoeusDlgWindow(mdiForm, true);
        dlgOppSearch.getContentPane().add(getControlledUI());
        dlgOppSearch.setTitle("Opportunity Search");
        dlgOppSearch.setFont(CoeusFontFactory.getLabelFont());
        dlgOppSearch.setModal(true);
        dlgOppSearch.setResizable(false);
        //dlgOppSearch.setSize(WIDTH,HEIGHT);
        dlgOppSearch.getRootPane().setDefaultButton(opportunitySearch.btnOk);
        dlgOppSearch.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgOppSearch.getSize();
        dlgOppSearch.setLocation(screenSize.width / 2 - (dlgSize.width / 2), screenSize.height / 2 - (dlgSize.height / 2));

        dlgOppSearch.addEscapeKeyListener(
                new AbstractAction("escPressed") {

                    public void actionPerformed(ActionEvent ae) {
                        performCancelAction();
                        return;
                    }
                });
        dlgOppSearch.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgOppSearch.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                performCancelAction();
                return;
            }

            public void windowOpened(WindowEvent we) {
                opportunitySearch.txtcfdaNum.requestFocusInWindow();
            }
        });

        java.awt.Component[] compo = {opportunitySearch.txtcfdaNum, opportunitySearch.txtOpportunityId, opportunitySearch.btnOk, opportunitySearch.btnCancel};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(compo);
        opportunitySearch.setFocusTraversalPolicy(traversalPolicy);
        opportunitySearch.setFocusCycleRoot(true);
    }

    public OpportunityInfoBean display() {
        dlgOppSearch.setVisible(true);
        dlgOppSearch.requestFocusInWindow();
        return oppInfoBean;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            dlgOppSearch.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (e.getSource().equals(opportunitySearch.btnCancel)) {
                performCancelAction();
            } else if (e.getSource().equals(opportunitySearch.btnOk)) {
                String cfdaNum = opportunitySearch.txtcfdaNum.getText().trim();
                String opportunityId = opportunitySearch.txtOpportunityId.getText().trim();
                if(cfdaNum.length()<2 && opportunityId.length() < 1){
                    //atleast one entry is a must
                    CoeusOptionPane.showInfoDialog(CoeusMessageResources.getInstance().parseMessageKey("s2ssubdetfrm_exceptionCode.1012"));
                    return;
                }
                searchOpportunity(cfdaNum, opportunityId);
            }
        } catch (Exception err) {
            err.printStackTrace();
            CoeusOptionPane.showInfoDialog(CoeusMessageResources.getInstance().parseMessageKey(err.getMessage()));
        } finally {
            dlgOppSearch.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void searchOpportunity(String cfdaNum, String opportunityId)throws CoeusException {
        S2SHeader s2SHeader = new S2SHeader();
        s2SHeader.setCfdaNumber(cfdaNum == null || cfdaNum.trim().length() < 2 ? null : cfdaNum);
        s2SHeader.setOpportunityId(opportunityId);

        ArrayList oppList = null;
        RequesterBean request = new RequesterBean();
        request.setDataObject(s2SHeader);
        request.setFunctionType(S2SConstants.GET_OPPORTUNITY_LIST);
        AppletServletCommunicator comm = new AppletServletCommunicator();
        comm.setConnectTo(s2sServlet);
        comm.setRequest(request);
        comm.send();
        ResponderBean response = comm.getResponse();

        if (response.hasResponse()) {
            oppList = (ArrayList) response.getDataObject();
        }
        if(oppList == null || oppList.size() == 0) {
            CoeusOptionPane.showInfoDialog(CoeusMessageResources.getInstance().parseMessageKey("s2ssubdetfrm_exceptionCode.1010"));
            return;
        }
        Vector syncOppList = new Vector(oppList.size());
        syncOppList.addAll(oppList);
        int size = oppList.size();
        OpportunitySelectionController oppSelCntlr =
                new OpportunitySelectionController(CoeusGuiConstants.getMDIForm());
        oppSelCntlr.setFormData(syncOppList);
        oppSelCntlr.setOppHeader(s2SHeader);
        oppSelCntlr.setSubmissionTitle(s2SHeader.getSubmissionTitle());
        performCancelAction();
        oppSelCntlr.display();
        //selectedOpportunity
        if (oppSelCntlr.isSaveNContinue()) {
            oppInfoBean = oppSelCntlr.getSltdOpportunity();
        } else {
            oppInfoBean = null;
        }
    }

    private void performCancelAction() {
        dlgOppSearch.dispose();
    }
}
