/*
 * MultiCampusClientAuthService.java
 *
 * Created on January 24, 2007, 12:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.user.auth;

import edu.mit.coeus.bean.LoginBean;
import edu.mit.coeus.user.auth.bean.MultiCampusLoginBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.user.auth.bean.AuthXMLNodeBean;
import edu.mit.coeus.user.auth.gui.MultiCampusLoginForm;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ComboBoxBean;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author geot
 */
public class MultiCampusClientAuthService  extends ClientAuthServiceHelper implements ActionListener{
    private MultiCampusLoginForm loginForm;
    private boolean loginSuccess;
    private CoeusMessageResources msgRes;
    /** Creates a new instance of MultiCampusClientAuthService */
    public MultiCampusClientAuthService() {
        msgRes = CoeusMessageResources.getInstance();
    }
    public void actionPerformed(ActionEvent actionEvent){
        Object source = actionEvent.getSource();
        try {
            if(source.equals(loginForm.btnOk)){
                loginForm.btnOk.setEnabled(false);
                loginForm.btnCancel.setEnabled(false);
                loginForm.txtUserId.setEditable(false);
                loginForm.password.setEditable(false);
                loginForm.cmbCampus.setEnabled(false);
                performAuthenticate();
                loginForm.setVisible(false);
            }else if(source.equals(loginForm.btnCancel)){
                System.exit(0);
            }
        } catch (CoeusException ex) {
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }finally{
            loginForm.btnOk.setEnabled(true);
            loginForm.btnCancel.setEnabled(true);
            loginForm.txtUserId.setEditable(true);
            loginForm.password.setEditable(true);
            loginForm.cmbCampus.setEnabled(true);
        }
        
    }
    public boolean performAuthenticate() throws edu.mit.coeus.exception.CoeusException {
        String userId = loginForm.txtUserId.getText();
        String password = new String(loginForm.password.getPassword());
        String campusCode = ((ComboBoxBean)loginForm.cmbCampus.getSelectedItem()).getCode();
        if ( userId!=null && userId.trim().equals("") ){
            throw new CoeusException(msgRes.parseMessageKey("coeusApplet_exceptionCode.1163"));
        }else if (password!=null &&  password.trim().equals("") ){
            throw new CoeusException(msgRes.parseMessageKey("coeusApplet_exceptionCode.1164"));
        }
        Hashtable data = getParams();
        String loginMode = (String)data.get("LOGIN_MODE");
        MultiCampusLoginBean loginBean = new MultiCampusLoginBean(userId,password,campusCode);
        RequesterBean request = new RequesterBean();
        request.setId(loginMode);
        request.setDataObject(loginBean);
        return connectAndValidate(request);
    }
    private Vector getCampusList(){
        AuthXMLNodeBean authNode = (AuthXMLNodeBean)getParams().get(AuthXMLNodeBean.class);
        List campusList = (ArrayList)authNode.getOtherNodes();
        int lstSize = campusList==null?0:campusList.size();
        Vector comboBoxList = new Vector(lstSize);
        ComboBoxBean emptyCombo = new ComboBoxBean("","");
        loginForm.cmbCampus.addItem(emptyCombo);
        for(int i=0;i<lstSize;i++){
            ComboBoxBean combo = new ComboBoxBean();
            Map attrs = (Map)campusList.get(i);
            if(attrs!=null){
                combo.setCode(attrs.get("code").toString());
                combo.setDescription(attrs.get("value").toString());
                comboBoxList.add(combo);
                loginForm.cmbCampus.addItem(combo);
            }
        }
        return comboBoxList;
    }
    public boolean authenticate() throws edu.mit.coeus.exception.CoeusException {
        Properties props = getProps();
        loginForm = new MultiCampusLoginForm();
        loginForm.btnOk.addActionListener(this);
        loginForm.btnCancel.addActionListener(this);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = loginForm.getSize();
        loginForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
                                screenSize.height/2 - (dlgSize.height/2));

        Vector campusList = getCampusList();
        loginForm.cmbCampus.setMaximumRowCount(campusList.size());
        loginForm.pack();
        loginForm.setVisible(true);
        return true; //no significance
    }
    
    
}
