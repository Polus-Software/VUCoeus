/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.search.gui;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;

import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;

/**
 * NewInstituteProposalSearch.java
 * Created on May 25, 2004, 3:13 PM
 * @author  Vyjayanthi
 */
public class NewInstituteProposalSearch extends CoeusSearch 
implements ActionListener{
    
    /** Holds CoeusMessageResources instance for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = 
            CoeusMessageResources.getInstance();
    
    private static final String LOG_STATUS_CODE = "LOG_STATUS_CODE";
    private static final String PENDING = "P";
    private static final String SELECT_PENDING_PROPOSALS = 
            "instPropLog_exceptionCode.1404";
    
    /** Creates a new instance of NewInstituteProposalSearch
     *  @param parent frame
     *  @param request identifier
     *  @param request type
     */
    public NewInstituteProposalSearch(Component parentFrame, String searchReq,
    int reqType) throws Exception{
        super(parentFrame, searchReq, reqType);
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        try{
            Object source = actionEvent.getSource();
            if(source instanceof javax.swing.JButton){
                if(((JButton)source).getName().equals("SearchWindowOK")){
                    SearchResultWindow searchResWindow = super.getResultWindow();
                    if(searchResWindow==null || searchResWindow.getSelectedRow()==null
                        || searchResWindow.getSelectedRow().isEmpty()){
                        throw new Exception(coeusMessageResources.parseMessageKey(
                                            "search_exceptionCode.1119"));
                    }
                    HashMap resultData = searchResWindow.getSelectedRow();
                    String status = (String)resultData.get(LOG_STATUS_CODE);
                    if( !status.equalsIgnoreCase(PENDING)){
                        CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(SELECT_PENDING_PROPOSALS));
                        return ;
                    }
                    super.actionPerformed(actionEvent);
                }else{
                    super.actionPerformed(actionEvent);
                }
            }
        }catch(Exception ex){
            CoeusOptionPane.showInfoDialog(ex.getMessage());
        }
    }
    
}
