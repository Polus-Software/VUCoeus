package edu.ucsd.coeus.personalization;

import edu.mit.coeus.utils.CoeusOptionPane;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import edu.ucsd.coeus.personalization.coeusforms.Popup;
import edu.ucsd.coeus.personalization.view.AbstractView;
import edu.ucsd.coeus.personalization.view.ReportDialog;

public class PopUpListener implements MouseListener {

    private Popup _popups[];
    private Component _comp;
    private boolean clickaction1 = false;
    private boolean clickaction2 = false;
    private boolean clickaction3 = false;
    private String url3 = "";
    private boolean hoveraction1 = false;
    private boolean hoveraction2 = false;
    private AbstractView _aview;  //View reference for which this popup is bound to

    public PopUpListener(AbstractView aview, Popup[] popups, Component comp) {
        super();
        this._popups = popups;
        this._comp = comp;
        this._aview = aview;
        for (int i = 0; i < popups.length; i++) {
            if (i == 0 && popups[0].getAction().equals("onclick")) {
                clickaction1 = true;
            }
            if (i == 0 && popups[0].getAction().equals("hover")) {
                hoveraction1 = true;
            }
            if (i == 1 && popups[1].getAction().equals("onclick")) {
                clickaction2 = true;
            }
            if (i == 1 && popups[1].getAction().equals("hover")) {
                hoveraction2 = true;
            }
        }
    }

    public PopUpListener(AbstractView aview, Component comp, String url) {
        super();
        this._comp = comp;
        this._aview = aview;
        clickaction3 = true;
        this.url3 = url;
    }

    public void mouseClicked(MouseEvent e) {
        if (clickaction3) {
            ClientUtils.openURL(url3);
        }
        if (clickaction1) {
            String dispcont = _popups[0].getContent();
            if (_popups[0].getTarget().equals("browser") && _popups[0].getCtype().equals("url")) {
                ClientUtils.openURL(dispcont);
            } else if (_popups[0].getTarget().equals("browser") && _popups[0].getCtype().equals("param_url")) {
                parseAndOpenGenURL(dispcont);
            } else {
                if (_popups[0].getCtype().equals("xsl")) {
                    dispcont = _aview.generateReport(dispcont, _popups[0].getBeanmap());
                }
                ReportDialog.showReportDialog(_comp, "Context sensitive Report", dispcont, true, true);
            }

        }
        if (clickaction2) {
            String dispcont = _popups[1].getContent();
            if (_popups[1].getTarget().equals("browser") && _popups[1].getCtype().equals("url")) {
                ClientUtils.openURL(dispcont);
            } else if (_popups[0].getTarget().equals("browser") && _popups[0].getCtype().equals("param_url")) {
                parseAndOpenGenURL(dispcont);
            } else {
                if (_popups[0].getCtype().equals("xsl")) {
                    dispcont = _aview.generateReport(dispcont, _popups[0].getBeanmap());
                }
                ReportDialog.showReportDialog(_comp, "Context sensitive Report", dispcont, true, true);
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
        _comp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (hoveraction1) {
            ReportDialog.showReportDialog(_comp, "Context sensitive Help", _popups[0].getContent(), true, false);
        }
        if (hoveraction2) {
            ReportDialog.showReportDialog(_comp, "Context sensitive Help", _popups[1].getContent(), true, false);
        }
    }

    public void mouseExited(MouseEvent e) {
        _comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (hoveraction1 || hoveraction2) {
            ReportDialog.closeDialog();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void parseAndOpenGenURL(String purl) {
        String akey = _aview.getActiveKey();
        if (akey != null) {
            String aurl = purl.replaceAll("#currentUniqueID#", akey);
            ClientUtils.openURL(aurl);
        } else {
            CoeusOptionPane.showErrorDialog("Unique ID not found");
        }
    }
}
