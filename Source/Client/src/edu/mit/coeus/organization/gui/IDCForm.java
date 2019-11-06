/*
 * @(#)IDCForm.java 1.0 8/27/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.gui;

import edu.mit.coeus.organization.bean.IDCRateTypesBean;
import edu.mit.coeus.organization.bean.OrganizationIDCBean;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.CoeusOptionPane;


import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Arrays;
import java.util.Hashtable;

import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.gui.CoeusMessageResources;


/**
 * This class prepares the panel that contain all the form controls for
 * Organization Type form.
 *
 * @version :1.0 August 25, 2002, 1:35 PM
 * @author Guptha K
 * @modified by Sagin
 * @date 24-10-02
 * Description : As part of Java V1.3 compatibility, Replaced all null Vectors
 *               with new Vector() instance.
 *
 */


public class IDCForm extends JPanel {  //implements ListSelectionListener

    JPanel pnlIDC;
    IDCRateTypesBean[] idcRateTypesList;
    char functionType;
    OrganizationIDCBean[] idcData;
    IDCRateTypesBean[] rateTypeData;
    IDCPanel  idcPanel;
    private int prevSelectedRow =-1;
    private DateUtils dateUtils;
    private boolean saveRequired=false;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /**
     * Constructor which instantiates form and populates them with data
     * specified in IDCForm in Organization Module and sets the enabled status
     * for all components depending on the functionType specified.
     * @param functionType Character which specifies the mode in which the
     * form will be shown.
     * 'A' specifies that the form is in Add Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     * @param idcData, a vector which consists of all the details
     * of a Organization.
     * @param rateTypeData, a array which consists of all IDCRateTypesBean.
     */
    
    public IDCForm(char functionType, OrganizationIDCBean[] idcData,
    IDCRateTypesBean[] rateTypeData) {
        this.functionType = functionType;
        this.idcData = idcData;
        this.rateTypeData=rateTypeData;
        //Create the IDCPanel component
        idcPanel = new IDCPanel();
        prepareIDCPanel();
      
        idcPanel.formatFields(functionType);

    }
    
    /** 
     * Method to set the data in the JTable.
     * This method sets the data which is available in idcData Vector into JTable. 
     */
    public void prepareIDCPanel(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        // prepare table of questions
        Vector colNames = new Vector();
        colNames.add("Rate");
        colNames.add("Type");
        colNames.add("Start Date");
        colNames.add("End Date");
        colNames.add("Requested Date");
        colNames.add("IDC Number");
        idcPanel.setColumnNames(colNames);
        Hashtable exp = new Hashtable();
        // get column data
        if (idcData != null) {
            Vector data = new Vector();
            Vector row = null;
            for (int i = 0; i < idcData.length; i++) {
                row = new Vector();
                row.addElement(new Float(idcData[i].getApplicableIdcRate()));
                row.add(getDescription(idcData[i].getIdcRateTypeCode()));
                row.add(CoeusDateFormat.format(idcData[i].getStartDate(),
                "dd-MMM-yyyy"));
                row.add(CoeusDateFormat.format(idcData[i].getEndDate(),
                "dd-MMM-yyyy"));
                row.add(CoeusDateFormat.format(idcData[i].getRequestedDate(),
                "dd-MMM-yyyy"));
                // code
                String idcNumber =
                new Integer(idcData[i].getIdcNumber()).toString();
                row.add(idcNumber);
                String strComments =  (idcData[i].getIdcComment() == null ?"" :
                    idcData[i].getIdcComment());
                    exp.put(new Integer(i+1),strComments);
                    data.addElement(row);
            }
            idcPanel.setDefaultData(data);
        }
        idcPanel.setCommentsInfo(exp);
        Vector idcTypeInfo = new Vector();
        IDCRateTypesBean idcRateInfo;
        if ( (rateTypeData != null) && (rateTypeData.length >0)){
            for (int i=0; i <rateTypeData.length;i++){
                idcRateInfo =(IDCRateTypesBean)rateTypeData[i];
                idcTypeInfo.add(new ComboBoxBean(
                new Integer(idcRateInfo.getIdcRateTypeCode()).toString(),
                idcRateInfo.getDescription()));
            }
        }
        idcPanel.setTypeData(idcTypeInfo);

        this.add(idcPanel.renderPanel());                
    }
    
    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!(functionType == CoeusGuiConstants.DISPLAY_MODE ))
        {
            idcPanel.setDefaultFocusForComponent();
        }        
    }    
    //end Amit       

    /**
     * This method is used to get all the table data of IDCForm.
     * @return Vector, a collection of vector of OrganizationIDCBean beans.
     */
    
    public OrganizationIDCBean[] getFormData(){
        Hashtable exp = idcPanel.getCommentsInfo();
        int idcRows = idcPanel.getIDCTable().getRowCount();
        Vector newIDCData = new Vector();
        /* If there are row is the IdcTable */
        if (idcRows > 0){
            int nextIDCNumber = 0;
            /* If the function Type is "I" then get all the rows
             * and into the vector of Beans
             */
            if ( functionType == 'I') {
                nextIDCNumber = getMaxIDCNumber(new Vector());
                nextIDCNumber++;
                int nextIdcNum = 1;
                for (int rows=0;rows <idcRows ;rows++){
                    OrganizationIDCBean idcInfo = new OrganizationIDCBean();
                    idcInfo.setIdcNumber(nextIdcNum);
                    idcInfo.setApplicableIdcRate(
                    (idcPanel.getIDCTable().getValueAt(rows,0) == null ? 0 :
                        new Float(idcPanel.getIDCTable().getValueAt(rows,
                        0).toString().trim()).floatValue()));
                        idcInfo.setIdcRateTypeCode(
                        getRateTypeCode(idcPanel.getIDCTable().getValueAt(rows,1)
                        == null ? "0" :
                            idcPanel.getIDCTable().getValueAt(rows,
                            1).toString().trim()));
                            idcInfo.setStartDate(
                            (idcPanel.getIDCTable().getValueAt(rows,2) == null ?
                            "" : idcPanel.getIDCTable().getValueAt(rows,
                            2).toString().trim()));
                            idcInfo.setEndDate((idcPanel.getIDCTable().getValueAt(rows,
                            3) == null ? "" : idcPanel.getIDCTable().getValueAt(rows,
                            3).toString().trim()));
                            idcInfo.setRequestedDate(
                            (idcPanel.getIDCTable().getValueAt(rows,4) == null ? ""
                            : idcPanel.getIDCTable().getValueAt(rows,
                            4).toString().trim()));
                            idcInfo.setAcType("I");
                            //get the comments info from the hash table
                            String comments ="";
                            if (exp != null){
                                comments = ( exp.get(new Integer(rows+1)) == null ?
                                "" : (String)exp.get(new Integer(rows+1)) );
                            }
                            idcInfo.setIdcComment(comments);
                            nextIdcNum++;
                            newIDCData.add(idcInfo);
                }//for ends for the Table data
            } else if ( functionType == 'U' ) {
                // if ends for functionType 'I' and starts for 'U'

                //for checking deleted records from the existing information
                Vector deletedRecs = new Vector();
                if (idcData != null) {
                    //for all the data in idcData
                    int idcDataSize = idcData.length;
                    //for all the existing idcInfo
                    for (int idcCount=0;idcCount < idcDataSize ;idcCount++){
                        OrganizationIDCBean idcInfo =
                        (OrganizationIDCBean)idcData[idcCount];
                        boolean found = false;
                        /* for all the rows in table cross check with
                         * the existing idcData
                         */
                        for (int rows =0 ; rows < idcRows ;rows++) {
                            String value = (
                            idcPanel.getIDCTable().getValueAt(rows,5)
                            == null ? "" :
                                idcPanel.getIDCTable().getValueAt(rows,
                                5).toString().trim());
                                if (!value.equals("")) {
                                    if (idcInfo.getIdcNumber() ==
                                    Integer.parseInt(value) ){
                                        found = true;
                                    }
                                }
                        }
                    /* if the row is removed from the existing data then
                     * we need to set acType as 'D'
                     */
                        if (!found) {
                            idcInfo.setAcType("D");
                            String idcNum = new Integer(
                            idcInfo.getIdcNumber()).toString().trim();
                            if (!idcNum.trim().equals("")){
                                deletedRecs.add(idcNum.trim());
                            }
                            idcInfo.setStartDate(new DateUtils().formatDate(
                            idcInfo.getStartDate(),"dd-MMM-yyyy"));
                            idcInfo.setEndDate(new DateUtils().formatDate(
                            idcInfo.getEndDate(),"dd-MMM-yyyy"));
                            idcInfo.setRequestedDate(new DateUtils().formatDate(
                            idcInfo.getRequestedDate(),"dd-MMM-yyyy"));
                            newIDCData.add(idcInfo);
                        }
                    }
                }
                nextIDCNumber = getMaxIDCNumber(deletedRecs);
                nextIDCNumber++;
                //for all the rows in table cross check with the existing idcData
                for (int rows =0 ; rows < idcRows ;rows++) {
                    String value = (idcPanel.getIDCTable().getValueAt(rows,5)
                    == null ? "" : idcPanel.getIDCTable().getValueAt(rows,
                    5).toString().trim());
                    if (!value.equals("")){
                        int idcDataSize = idcData.length;
                        //for all the existing idcInfo
                        for (int idcCount=0;idcCount < idcDataSize ;idcCount++){
                            OrganizationIDCBean idcInfo =
                            (OrganizationIDCBean)idcData[idcCount];

                            if (idcInfo.getIdcNumber() ==
                            Integer.parseInt(value) ){
                                idcInfo.setAcType("U");
                                String comments = idcInfo.getIdcComment();
                                //get the comments info from the hash table
                                if (exp != null){
                                    comments = ( exp.get(new Integer(rows+1))
                                    == null ? idcInfo.getIdcComment() :
                                        (String)exp.get(
                                        new Integer(rows+1)) );
                                }
                                idcInfo.setIdcComment(comments);
                                idcInfo.setApplicableIdcRate(
                                (idcPanel.getIDCTable().getValueAt(rows,0)
                                == null ? 0 : new Float(
                                idcPanel.getIDCTable().getValueAt(rows,
                                0).toString().trim()).floatValue()));
                                idcInfo.setIdcRateTypeCode(getRateTypeCode(
                                idcPanel.getIDCTable().getValueAt(rows,1)
                                == null ? "0" :
                                    idcPanel.getIDCTable().getValueAt(rows,
                                    1).toString().trim()));
                                    idcInfo.setStartDate((
                                    idcPanel.getIDCTable().getValueAt(rows,
                                    2) == null ? "" :
                                        idcPanel.getIDCTable().getValueAt(
                                        rows,2).toString().trim()));
                                        idcInfo.setEndDate(
                                        (idcPanel.getIDCTable().getValueAt(rows,
                                        3) == null ? "" :
                                            idcPanel.getIDCTable().getValueAt(
                                            rows,3).toString().trim()));
                                            idcInfo.setRequestedDate((
                                            idcPanel.getIDCTable().getValueAt(
                                            rows,4) == null ?
                                            "" :
                                                idcPanel.getIDCTable().getValueAt(
                                                rows,
                                                4).toString().trim()));
                                                newIDCData.add(idcInfo);

                            }
                        }
                    }else {
                        OrganizationIDCBean idcRow = new OrganizationIDCBean();
                        idcRow.setIdcNumber(nextIDCNumber);
                        idcRow.setApplicableIdcRate(
                        (idcPanel.getIDCTable().getValueAt(rows,
                        0) == null ? 0 : new Float(
                        idcPanel.getIDCTable().getValueAt(rows,
                        0).toString().trim()).floatValue()));
                        idcRow.setIdcRateTypeCode(
                        getRateTypeCode(idcPanel.getIDCTable().getValueAt(rows,
                        1) == null ? "0" : idcPanel.getIDCTable().getValueAt(rows,
                        1).toString().trim()));
                        idcRow.setStartDate((idcPanel.getIDCTable().getValueAt(rows,
                        2) == null ? "" : idcPanel.getIDCTable().getValueAt(rows,
                        2).toString().trim()));
                        idcRow.setEndDate((idcPanel.getIDCTable().getValueAt(rows,
                        3) == null ? "" : idcPanel.getIDCTable().getValueAt(rows,
                        3).toString().trim()));
                        idcRow.setRequestedDate(
                        (idcPanel.getIDCTable().getValueAt(rows,4) == null ? ""
                        : idcPanel.getIDCTable().getValueAt(rows,
                        4).toString().trim()));
                        //get the comments info from the hash table
                        String comments ="";
                        if (exp != null){
                            comments = ( exp.get(new Integer(rows+1)) == null ?
                            "" : (String)exp.get(new Integer(rows+1)) );
                        }
                        idcRow.setIdcComment(comments);
                        idcRow.setAcType("I");
                        newIDCData.add(idcRow);
                        nextIDCNumber++;
                    }
                } //for ends for the Idc table rows


            }//if ends for functionType is 'U'

            // if ends for rowcount >0
        } else if ((idcData != null) && (idcData.length >0 ) ){
            for (int idcCount=0;idcCount < idcData.length ;idcCount++){
                OrganizationIDCBean idcInfo =
                (OrganizationIDCBean)idcData[idcCount];
                idcInfo.setAcType("D");
                idcInfo.setStartDate(new DateUtils().formatDate(
                idcInfo.getStartDate(),"dd-MMM-yyyy"));
                idcInfo.setEndDate(new DateUtils().formatDate(
                idcInfo.getEndDate(),"dd-MMM-yyyy"));
                idcInfo.setRequestedDate(new DateUtils().formatDate(
                idcInfo.getRequestedDate(),"dd-MMM-yyyy"));
                newIDCData.add(idcInfo);
            }
        }

        OrganizationIDCBean[] newData = new OrganizationIDCBean[newIDCData.size()];
        for (int idcCount=0;idcCount < newIDCData.size() ;idcCount++){
            OrganizationIDCBean newInfo =
            (OrganizationIDCBean) newIDCData.elementAt(idcCount);
            newData[idcCount] = newInfo;
        }
        return newData;


    }


    private int getMaxIDCNumber(Vector delRecs){
        int maxIdc =0;
        int rowCount = idcPanel.getIDCTable().getRowCount();
        int colCount = idcPanel.getIDCTable().getColumnCount();
        for (int rows=0;rows < rowCount;rows++){
            String value = (idcPanel.getIDCTable().getValueAt(rows,5) == null ?
            "" :idcPanel.getIDCTable().getValueAt(rows,5).toString().trim());
            if ((delRecs != null) && (delRecs.size() > 0)) {
                if ( (value != null) && (!value.equals("")) ){
                    for (int count=0;count<delRecs.size();count++){
                        if ( (!value.trim().equals((delRecs.get(count)
                        == null? "" :delRecs.get(count).toString().trim())))
                        && (maxIdc <= new Integer(value).intValue())){
                            maxIdc =new Integer(value).intValue();
                        }
                    }
                }
            }else {
                if ( (value != null) && (!value.equals("")) ){
                    if ( maxIdc <= new Integer(value).intValue()){
                        maxIdc =new Integer(value).intValue();
                    }
                }
            }
        }
        return maxIdc;
    }

    private int getRateTypeCode(String Desc){
        int code =0;
        if ((rateTypeData != null ) && (!Desc.equals("")) ) {
            int dataSize =  rateTypeData.length;
            for (int count =0; count < dataSize;count++){
                if (rateTypeData[count].getDescription().trim().equals(
                Desc.trim()) ) {
                    code = rateTypeData[count].getIdcRateTypeCode();
                }
            }
        }
        return code;
    }

    private String getDescription(int code){
        String Desc ="";
        if ((rateTypeData != null ) && (code != 0) ) {
            int dataSize =  rateTypeData.length;
            for (int count =0; count < dataSize;count++){
                if (rateTypeData[count].getIdcRateTypeCode() == code) {
                    Desc = rateTypeData[count].getDescription().trim();
                }
            }
        }
        return Desc;
    }

    /**
     * Get the Panel of organization Type form. This panel contains all the
     * necessary form controls
     *
     * @return JPanel contains the organization Type form controls
     */
    
    public JPanel getOrganizationIDCPanel() {
        return pnlIDC;
    }

    /**
     * This method is used for client side validations.
     * It validates the imcomplete submission of table values.
     * @ return true if the validation succeed or else false.
     */
    
    public boolean validateData(){
        /* If a button press is the trigger to leave a JTable cell and save the
         * data in model
         */

        idcPanel.stopCellEditing();
        int dataSize = idcPanel.getIDCTable().getRowCount();
        try {
            if (dataSize > 0) {
                for (int rows =0;rows < dataSize;rows++){
                    String value =
                    (idcPanel.getIDCTable().getValueAt(rows,0) == null ?
                    "0.0" : idcPanel.getIDCTable().getValueAt(rows,
                    0).toString().trim());
                    if ( (value.trim().equals("")) ||
                    (new Double(value).doubleValue() == 0.0) ){
                        CoeusOptionPane.showInfoDialog(
                                coeusMessageResources.parseMessageKey(
                                            "orgIDCFrm_exceptionCode.1093"));
                        idcPanel.getIDCTable().getSelectionModel().setSelectionInterval(
                        rows,rows);
                        return false;
                    }else {
                        String startDate =
                        (idcPanel.getIDCTable().getValueAt(rows,
                        2) == null ? "" :
                            idcPanel.getIDCTable().getValueAt(rows,
                            2).toString().trim());
                        String endDate =
                        (idcPanel.getIDCTable().getValueAt(rows,
                        3) == null ? "" :
                            idcPanel.getIDCTable().getValueAt(rows,
                            3).toString().trim());
                        String requestedDate =
                        (idcPanel.getIDCTable().getValueAt(rows,
                        4) == null ? "" :
                            idcPanel.getIDCTable().getValueAt(rows,
                            4).toString().trim());
                        if ( startDate.trim().equals("") &&
                            requestedDate.trim().equals("") ) {
                                    CoeusOptionPane.showInfoDialog(
                                        coeusMessageResources.parseMessageKey(
                                            "orgIDCFrm_exceptionCode.1094"));
                        idcPanel.getIDCTable().getSelectionModel().setSelectionInterval(
                            rows,rows);
                                    return false;
                         }else if (!startDate.trim().equals("") &&
                            endDate.trim().equals("")){
                            CoeusOptionPane.showInfoDialog(
                                coeusMessageResources.parseMessageKey(
                                            "orgIDCFrm_exceptionCode.1095"));
                            idcPanel.getIDCTable().getSelectionModel().setSelectionInterval(
                                rows,rows);
                             return false;
                         }
                    }
                }
            }
        }catch(NumberFormatException nfe) {
            nfe.printStackTrace();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                            "orgIDCFrm_exceptionCode.1093"));
            return false;
        }


        return true;
    }
    
    /**
     * This method is used to determine whether the data is to be saved or not.
     * @returns boolean, true if any modifications have been done else it returns false.
     */
    
    public boolean isSaveRequired(){
        saveRequired = idcPanel.isSaveRequired();
        if ((idcData==null || idcData.length == 0) && !saveRequired){
            return false;
        }
        //added by guptha to set the saveRequired flag
        try{
            for (int i = 0; i < idcData.length; i++) {
                String value = idcPanel.getIDCTable().getValueAt(i,0).toString().trim();
                int rateType = getRateTypeCode(idcPanel.getIDCTable().getValueAt(i,1).toString().trim());
                //value(new Double(value).doubleValue())
                if (idcData[i].getApplicableIdcRate() !=  Float.parseFloat(value) ) {
                    saveRequired=true;
                    break;
                }else if (idcData[i].getIdcRateTypeCode() != rateType ) {
                    saveRequired=true;
                    break;
                }else if (  idcData[i].getStartDate()!=null &&
                            !idcData[i].getStartDate().trim().equals("") &&
                            !CoeusDateFormat.format(idcData[i].getStartDate(),"dd-MMM-yyyy").equals(
                            idcPanel.getIDCTable().getValueAt(i,2).toString().trim()) ) {
                    saveRequired=true;
                    break;
                }else if (  idcData[i].getEndDate()!=null &&
                            !idcData[i].getEndDate().trim().equals("") &&
                            !CoeusDateFormat.format(idcData[i].getEndDate(),"dd-MMM-yyyy").equals(
                            idcPanel.getIDCTable().getValueAt(i,3).toString().trim()) ) {
                    saveRequired=true;
                    break;
                }else if (  idcData[i].getRequestedDate()!=null &&
                            !idcData[i].getRequestedDate().trim().equals("") &&
                            !CoeusDateFormat.format(idcData[i].getRequestedDate(),"dd-MMM-yyyy").equals(
                            idcPanel.getIDCTable().getValueAt(i,4).toString().trim()) ) {

                    saveRequired=true;
                    break;
                }
            }
        }catch (Exception e){
            saveRequired=true;
        }
        return saveRequired;
    }              
}
