package edu.ucsd.coeus.personalization;

import edu.mit.coeus.award.bean.AwardCustomDataBean;
import edu.mit.coeus.award.controller.OtherHeaderController;
import edu.mit.coeus.instprop.bean.InstituteProposalCustomDataBean;
import edu.mit.coeus.instprop.controller.IPCustomElementsController;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.customelements.CustomElementsForm;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.ucsd.coeus.personalization.view.AbstractView;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Iterator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class FieldListener  implements FocusListener    {
	private JTextField _comp;
	private AbstractView _aview;  //View reference for which this field is bound to
    private String _module;
    private String _columnnm;

	public FieldListener(AbstractView aview, String module, String columnnm, JTextField comp) {
		super();
		this._comp = comp;
		this._aview = aview;
        this._module = module;
        this._columnnm = columnnm;
		}

    
    public void focusGained(FocusEvent e) {
    }


    public void focusLost(FocusEvent e) {
        if (_module.equalsIgnoreCase("proposal"))
            bindDataToIPCustom(_comp.getText());
        if (_module.equalsIgnoreCase("award"))
            bindDataToAWCustom(_comp.getText());

    }

    public void bindDataToIPCustom(String textstr) {
        if (ClientUtils.isBlank(textstr)) textstr = "";
        IPCustomElementsController cctrl = _aview.getIPCustomElementController();
        CustomElementsForm cform = (CustomElementsForm) cctrl.getControlledUI();
        bindData(textstr, cform.getTable());
        cform.setSaveRequired(true);
	    QueryEngine queryEngine;
        queryEngine = QueryEngine.getInstance();
        try {
            CoeusVector dcol = queryEngine.getDetails(_aview.getActiveKey(), InstituteProposalCustomDataBean.class);
            for (Iterator it = dcol.iterator(); it.hasNext();) {
                InstituteProposalCustomDataBean cbean = (InstituteProposalCustomDataBean)it.next();
                if (cbean.getColumnName().equals(_columnnm)) {
                    if (cbean.getColumnValue() == null) { //New value this is insert
                        cbean.setAcType(TypeConstants.INSERT_RECORD);
                    } else {
                        cbean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                   cbean.setColumnValue(textstr);
                   queryEngine.update(_aview.getActiveKey(), cbean);
                }
            }
        } catch (Exception ex) {
            ClientUtils.logger.error(ex);
        }
    }

    public void bindDataToAWCustom(String textstr) {
        if (ClientUtils.isBlank(textstr)) textstr = "";
        OtherHeaderController cctrl = _aview.getAwCustomElementController();
        CustomElementsForm cform = (CustomElementsForm) cctrl.getControlledUI();
        bindData(textstr, cform.getTable());
        cform.setSaveRequired(true);
	    QueryEngine queryEngine;
        queryEngine = QueryEngine.getInstance();
        try {
            CoeusVector dcol = queryEngine.getDetails(_aview.getActiveKey(), AwardCustomDataBean.class);
            for (Iterator it = dcol.iterator(); it.hasNext();) {
                AwardCustomDataBean cbean = (AwardCustomDataBean)it.next();
                if (cbean.getColumnName().equals(_columnnm)) {
                    if (cbean.getColumnValue() == null) { //New value this is insert
                        cbean.setAcType(TypeConstants.INSERT_RECORD);
                    } else {
                        cbean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                   cbean.setColumnValue(textstr);
                   queryEngine.update(_aview.getActiveKey(), cbean);
                }
            }
        } catch (Exception ex) {
            ClientUtils.logger.error(ex);
        }
    }

    private void bindData(String txtstr, JTable atable) {
        int rowCount = atable.getRowCount();
        if(rowCount > 0){
            for(int inInd = 0; inInd < rowCount ;inInd++){
                String fieldid = (String)((DefaultTableModel) atable.getModel()).getValueAt(inInd,9);
                if (fieldid.equals(_columnnm)) {
                    ((DefaultTableModel)atable.getModel()).setValueAt(txtstr, inInd, 1);
                    ((DefaultTableModel)atable.getModel()).fireTableDataChanged();
                    break;
                }
            }
        }
    }


}
