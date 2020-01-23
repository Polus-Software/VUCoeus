/*
 * AreaSearchAction.java
 *
 * Created on March 9, 2005, 4:32 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.irb.bean.AreaOfResearchTreeNodeBean;
import edu.mit.coeus.irb.bean.AreaOfResearchTxnBean;
import edu.mit.coeuslite.irb.form.AreaSearchForm;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;

/**
 *
 * @author  shijiv
 */
public class AreaSearchAction extends ProtocolBaseAction {
    
    private static final String CONSTANT="000000";
    //Removing instance variable case# 2960
    //private Vector resultreeViewector
    private static final String IMAGE_PATH="/coeusliteimages";
    
    
    /** Creates a new instance of AreaSearchAction */
    public AreaSearchAction()  {
       
    }
    
    public ActionForward performExecute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException,Exception {
        TreeView treeView=new TreeView();
        Hashtable htNodes= new Hashtable();
        Hashtable htCodes= new Hashtable(); 
        String strSearchType = request.getParameter("type");       
        String strForm = request.getParameter("form");
        HttpSession session = request.getSession();        
        //Modified for instance variable case#2960.
        //createTree(treeView,htNodes,htCodes);
        Vector resultreeViewector = createTree(treeView,htNodes,htCodes);
        String imgFolder=request.getContextPath()+IMAGE_PATH;
        treeView.setImagesUrl(imgFolder);
        treeView.setContextUrl(request.getContextPath());        
            if (strSearchType.equalsIgnoreCase("area")) {
                if (resultreeViewector!=null && resultreeViewector.size()>0) {
                    session.setAttribute("areas",null);
                    session.setAttribute("areas",treeView);
                    request.setAttribute("type",strSearchType);
                    request.setAttribute("form", strForm);
                }
            } 

       return mapping.findForward("areaResearch");
    }
     
    /** Build the tree of the Area of research
     *@ TreeView
     *@ param Hashtable contains all the nodes details
     *@ param contains the values of the nodes
     *Builds the tree depending upon the bean value
     */
     
     private Vector createTree(TreeView treeView,Hashtable htNodes,Hashtable htCodes) throws Exception{
         AreaOfResearchTxnBean aorb = new AreaOfResearchTxnBean();
         //Modified for instance variable case#2960.
         //resultreeViewector = aorb.getResearchHierarchyDetails();
         Vector resultreeViewector = aorb.getResearchHierarchyDetails();
         for(int i=0;i<resultreeViewector.size();i++) {
             AreaOfResearchTreeNodeBean areaOfResearchTreeNodeBean=(AreaOfResearchTreeNodeBean)resultreeViewector.get(i);
             String code=areaOfResearchTreeNodeBean.getParentResearchAreaCode();
             if(!htCodes.contains(code)) {
                 if(code.equals(CONSTANT)) {
                     treeView.add(treeView.createNode(areaOfResearchTreeNodeBean.getResearchAreaCode()+" - "+areaOfResearchTreeNodeBean.getRADescription()));
                 }
             }
             int count=0;
             Vector vcParentNodes= new Vector();
             if(htCodes.containsKey(code)) {
                 String parentCode=code;
                 while(htCodes.containsKey(parentCode)) {
                     vcParentNodes.add(parentCode);
                     parentCode=(String)htCodes.get(parentCode);
                     count++;
                 }
             }
             if(htNodes.containsKey(code)) {
                 String desc=(String)htNodes.get(code);
                 int index=0,index1=0;
                 String parCode=null;
                 String parDesc=null;
                 switch(count) {
                     case 1:
                         index=treeView.getParentNodeIndex(treeView.createNode(code+" - "+desc),1,0);
                         treeView.nodes.item(index).add(treeView.createNode(areaOfResearchTreeNodeBean.getResearchAreaCode()+" - "+areaOfResearchTreeNodeBean.getRADescription()));
                         break;
                     case 2:
                         index=0;
                         parCode=(String)vcParentNodes.get(vcParentNodes.size()-2);
                         parDesc=(String)htNodes.get(parCode);
                         index1=treeView.getParentNodeIndex(treeView.createNode(parCode+" - "+parDesc),2,0);
                         treeView.nodes.item(index).childNodes.item(index1).add(treeView.createNode(areaOfResearchTreeNodeBean.getResearchAreaCode()+" - "+areaOfResearchTreeNodeBean.getRADescription()));
                         break;
                     case 3:
                         index=0;
                         parCode=(String)vcParentNodes.get(vcParentNodes.size()-2);
                         parDesc=(String)htNodes.get(parCode);
                         index1=treeView.getParentNodeIndex(treeView.createNode(parCode+" - "+parDesc),2,0);
                         parCode=(String)vcParentNodes.get(vcParentNodes.size()-3);
                         parDesc=(String)htNodes.get(parCode);
                         int index2=treeView.getParentNodeIndex(treeView.createNode(parCode+" - "+parDesc),3,index1);
                         treeView.nodes.item(index).childNodes.item(index1).childNodes.item(index2).add(treeView.createNode(areaOfResearchTreeNodeBean.getResearchAreaCode()+" - "+areaOfResearchTreeNodeBean.getRADescription()));
                         break;
                 }
             }
             
             if(areaOfResearchTreeNodeBean.hasChildren()) {
                 htCodes.put(areaOfResearchTreeNodeBean.getResearchAreaCode(),code);
                 htNodes.put(areaOfResearchTreeNodeBean.getResearchAreaCode(),areaOfResearchTreeNodeBean.getRADescription());
             }
         }
         //Modified for instance variable case#2960.
         return resultreeViewector;         
     }
     
     public void cleanUp() {
     }
     
}
