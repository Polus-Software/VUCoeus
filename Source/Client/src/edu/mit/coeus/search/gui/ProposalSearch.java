/*
 * ProposalSearch.java
 *
 * Created on March 14, 2003, 3:39 PM
 */

package edu.mit.coeus.search.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.BorderLayout;


import javax.swing.JPanel;
import javax.swing.JButton;

import java.util.Vector;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.brokers.*;


/**
 * This class is used to show Coeus search window with all loggedin user roles
 * as selected. This is an extension of CoeusSearch component. <REMCLAUSE> of 
 * Search.XML is used to have custom query for proposal. Addition of selected role
 * numbers to the query in xml <REMCLAUSE> will take place in Find button's 
 * overloaded actionPerformed method.
 * @author  ravikanth
 */
public class ProposalSearch extends CoeusSearch {
    
    RolesPanel rolesPanel;
    SearchInfoHolderBean searchBean;
    private final char GET_ROLES_FOR_USER = 'G';
    CoeusAppletMDIForm parent;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
   
    /** Creates a new instance of ProposalSearch */
    public ProposalSearch() {
    }

    /**
     *  Three argument constructor. It will intialize the class variables and 
     *  create the empty result table as well.
     *  @param parent frame
     *  @param request identifier
     *  @param request type
     */
    public ProposalSearch(Component parentFrame,String searchReq,int reqType) 
        throws Exception{
            super(parentFrame, searchReq, reqType );
            this.parent = (CoeusAppletMDIForm)parentFrame;
            coeusMessageResources = CoeusMessageResources.getInstance();
            rolesPanel = new RolesPanel();
    }
    /**
     * Method used to get the existing search panel and add roles panel for 
     * Proposal search.
     * @return custom CoeusSearch panel with roles details.
     */
    protected JPanel getSearchPanel() throws Exception{
        JPanel pnlSearch = super.getSearchPanel();
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.add(rolesPanel);
        pnlSearch.add(southPanel,BorderLayout.SOUTH);
        rolesPanel.setRolesList(
            getRolesForUser(CoeusGuiConstants.getMDIForm().getUserName()));
        return pnlSearch;
    }

    //suporting method to get the roles for the loggedin user's home unit number.
    private Vector getRolesForUser(String userId) throws Exception{
        Vector roles = new Vector();

        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_ROLES_FOR_USER);
        requester.setUserName(userId);
        // prepare url for search result servlet
        String SEARCH_CONNECTION_URL = CoeusGuiConstants.CONNECTION_URL
            +CoeusGuiConstants.PROPOSAL_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(
            SEARCH_CONNECTION_URL, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(!response.isSuccessfulResponse()){
            throw new Exception(response.getMessage());
        }
        roles = (Vector)response.getDataObjects().elementAt(0);
        return roles;
    }
    
    /** 
     * Overridden actionPerformed method for Find button which will add all the 
     * selected roles
     */
    
    public void actionPerformed(java.awt.event.ActionEvent ae){
        Object source = ae.getSource();
        if(source instanceof javax.swing.JButton){
            if(((JButton)source).getName().equals("SearchWindowFind")){
                searchBean = super.getSearchInfoHolder();
                String clause = searchBean.getRemClause();
                int roleIndex = -1;
                Vector selectedRoles = rolesPanel.getRolesList();
                if(selectedRoles != null && selectedRoles.size() > 0 ){
                    if(clause != null){
                        StringBuffer remClause = new StringBuffer(clause);
                        String tempClause = remClause.toString();
                        roleIndex = tempClause.indexOf("()");
                        // insert all the role id in between () so that it will form
                        // query.
                        if(roleIndex != -1){
                            roleIndex++;
                            StringBuffer roleIds = new StringBuffer();
                            for(int selIndex = 0; selIndex < selectedRoles.size();
                                selIndex++){
                                ComboBoxBean bean = (ComboBoxBean)selectedRoles.elementAt(selIndex);
                                    
                                roleIds.append("\'"+ bean.getCode()+"\',");
                            }
                            roleIds.deleteCharAt(roleIds.length()-1);
                            remClause.insert(roleIndex,roleIds.toString());
                            
                            // replacing hardcoded userid with loggedin user id.
                            
                            String newRemClause = Utils.replaceString(
                                remClause.toString(),"COEUS",parent.getUserName().toUpperCase().trim());
                            searchBean.setRemClause(newRemClause);
                        }
                    }
                    super.actionPerformed(ae);
                    // resetting the remclause to original one after performing search
                    searchBean.setRemClause(clause);
                }else{
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey( "protoRoles_exceptionCode.2600")
                    );
                }
            }else{
                super.actionPerformed(ae);
            }
        }
    }
}
