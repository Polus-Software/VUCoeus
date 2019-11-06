/*
 * ProposalUserRoleMaintainance.java
 *
 * Created on February 20, 2004, 3:06 PM
 */

package edu.mit.coeus.propdev.gui;

/** /**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
* @author chandru
*/

import javax.swing.*;
import javax.swing.tree.TreePath;

import java.util.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.tree.TransferableUserRoleData;
import edu.mit.coeus.utils.tree.UserRoleNode;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.roles.*;
import edu.mit.coeus.utils.CoeusVector;

public class ProposalUserRoleMaintainance extends UserRolesMaintenance {
    
    private CoeusVector cvNarrativeUsers;
    private CoeusVector cvNarrativeModules;
    private String proposalNumber;
    private Vector userAndRoles;
    private CoeusVector cvRoleRights;
    
    /** Creates a new instance of ProposalUserRoleMaintainance */
    public ProposalUserRoleMaintainance(String moduleNumber, 
        Vector userAndRoleDetails, char funcType, CoeusVector cvNarrativeUsers, CoeusVector cvNarrativeModules,CoeusVector cvRoleRights )  {
            super(moduleNumber,userAndRoleDetails,funcType);
            this.userAndRoles = (Vector)userAndRoleDetails.elementAt(1);
            this.cvNarrativeUsers = cvNarrativeUsers;
            this.cvNarrativeModules = cvNarrativeModules;
            this.proposalNumber = moduleNumber;
            this.cvRoleRights = cvRoleRights;
            
    }
     public JComponent showUserRolesMaintenance(CoeusDlgWindow parentComponent ) throws Exception{
         super.callInitComponents();
         rolesTree = new ProposalUserRoleTree(cvNarrativeUsers, cvNarrativeModules, proposalNumber);         
         
         rolesTree.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
         rolesTree.setDropable(true);
         rolesTree.setFont(CoeusFontFactory.getNormalFont());
         rolesTree.setMoveNode(true);
         rolesTree.setUserAndRoles(userAndRoles);
         rolesTree.setRoleRights(cvRoleRights);
         
         scrPnRoles.setViewportView(rolesTree);
         JComponent component = super.showComponents(parentComponent);
         return component;
     }  

     
     public CoeusVector getNarrativeUsers(){
        cvNarrativeUsers = rolesTree.getNarrativeUsers();
        return cvNarrativeUsers;
     }
}
