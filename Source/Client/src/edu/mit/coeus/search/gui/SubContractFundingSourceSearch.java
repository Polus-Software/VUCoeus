/*
 * SubContractFundingSourceSearch.java
 *
 * Created on September 9, 2004, 11:09 AM
 */

package edu.mit.coeus.search.gui;

import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.search.gui.SearchResultWindow;
import edu.mit.coeus.subcontract.bean.SubContractFundingSourceBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;

import java.awt.Component;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class SubContractFundingSourceSearch extends CoeusSearch implements ActionListener {
	
	private CoeusMessageResources coeusMessageResources;
	private static final String GET_SERVLET = "/SubcontractMaintenenceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
	private static final char GET_SUBCONTRACT_AWARD_INFO = 'D';
	private boolean isOKClicked = false;
	private CoeusVector vecSelectedAwards = new CoeusVector();
	
	/** Creates a new instance of SubContractFundingSourceSearch */
	public SubContractFundingSourceSearch() {
	}
	
	/**
	 * Constructor
	 * @param parentFrame Component
	 * @param searchReq String
	 * @param reqType int
	 * @throws Exception Exception
	 */	
	public SubContractFundingSourceSearch(Component parentFrame, String searchReq,
						 int reqType) throws Exception {
        super(parentFrame, searchReq, reqType);
    }
	
	
	/**
	 * Getting the search panel made the method available to subclasses ,
     * so that it can be overriden.
	 * @return JPanel
	 * @throws Exception Exception
	 */	
    protected JPanel getSearchPanel() throws Exception {
		JPanel newSearchPanel = new JPanel(new BorderLayout());
		newSearchPanel.add(new JLabel("          "),BorderLayout.NORTH);
		newSearchPanel.add(new JLabel("Any award may be chosen as a funding source."),BorderLayout.CENTER);
		JPanel searchPanel = super.getSearchPanel();
		newSearchPanel.add(searchPanel,BorderLayout.SOUTH);
		return newSearchPanel;
    }
	
	/** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        try{
            Object source = actionEvent.getSource();
			SubContractFundingSourceBean subContractFundingSourceBean = null;
            if(source instanceof javax.swing.JButton){
                if(((JButton)source).getName().equals("SearchWindowOK")){
                    SearchResultWindow searchResWindow = super.getResultWindow();
                    if(searchResWindow==null || searchResWindow.getSelectedRow()==null
                        || searchResWindow.getSelectedRow().isEmpty()){
                        throw new Exception(coeusMessageResources.parseMessageKey(
                                            "search_exceptionCode.1119"));
                    }
					Vector vecSelectedRows = searchResWindow.getMultipleSelectedRows();
					
					if (vecSelectedRows != null && !vecSelectedRows.isEmpty()) {
						for (int index = 0; index < vecSelectedRows.size(); index++) {
							HashMap awardSelected = (HashMap)vecSelectedRows.get(index);
							
							if (awardSelected != null && !awardSelected.isEmpty() ) {
								subContractFundingSourceBean = new SubContractFundingSourceBean();
								String mitAwardNumber = Utils.convertNull(awardSelected.get(
								"MIT_AWARD_NUMBER"));
								int awardIndex = mitAwardNumber.lastIndexOf("-001");
								if (awardIndex < 0) {
									int choice = CoeusOptionPane.showQuestionDialog("The award you have selected " + mitAwardNumber + " is not " +
										"the top level award in the hierarchy. Do you want to select this award as a " +
											"funding source for the subcontract.",CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
								    if (choice == javax.swing.JOptionPane.YES_OPTION) {
										subContractFundingSourceBean = getSubContractAwardInfo(mitAwardNumber);
										if (subContractFundingSourceBean == null) {
											CoeusOptionPane.showInfoDialog("No Funding Source Details are available for "+mitAwardNumber);
										}
									} else {
										return;
									}
								} else {
									subContractFundingSourceBean = getSubContractAwardInfo(mitAwardNumber);
									if (subContractFundingSourceBean == null) {
										CoeusOptionPane.showInfoDialog("No Funding Source Details are available for "+mitAwardNumber);									}
									}
								}
								
							if (subContractFundingSourceBean != null) {
									subContractFundingSourceBean.setAcType(TypeConstants.INSERT_RECORD);
									vecSelectedAwards.addElement(subContractFundingSourceBean);
								} 
							}
						}
						super.actionPerformed(actionEvent);
						isOKClicked = true;
					}else{
						super.actionPerformed(actionEvent);
					}
				}
			}catch(Exception ex){
				CoeusOptionPane.showInfoDialog(ex.getMessage());
			}
		}
	
	/** To get the subcontract amount info for the given mit award number
     * @param mitAwardNumber holds the proposal number
     */
    private SubContractFundingSourceBean getSubContractAwardInfo(String mitAwardNumber) 
		throws CoeusClientException {
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_SUBCONTRACT_AWARD_INFO);
        requester.setDataObject(mitAwardNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return (SubContractFundingSourceBean)response.getDataObject();
        }else {
            throw new CoeusClientException(response.getMessage());
        }
	}
	
	/**
	 * Checking for OK button clicked or not.
	 * @return boolean
	 */	
	public boolean isOKClicked() {
		return isOKClicked;
	}
	
	/**
	 * getting the selected Award
	 * @return CoeusVector
	 */	
	public CoeusVector getSelectedAwards() {
		return vecSelectedAwards;
	}
}
