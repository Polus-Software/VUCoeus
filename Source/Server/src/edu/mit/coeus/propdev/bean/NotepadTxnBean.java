/*
 * @(#)NotepadTxnBean.java 1.0 03/10/03 9:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.irb.bean.*;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusVector;

import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Comparator;
import java.util.TreeSet;
import java.sql.Timestamp;
import java.sql.Date;

import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.award.bean.AwardTxnBean;

/**
 * This class provides the methods for performing all procedure executions for
 * NotePad module. Various methods are used to fetch and update Notepad details.
 *
 * All methods are used <code>DBEngineImpl</code> singleton instance for the
 * database interaction.
 *
 * @version 1.0 on March 24, 2004, 9:50 AM
 * @author  Prasanna Kumar K
 */

public class NotepadTxnBean implements TypeConstants{
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    // holds the userId for the logged in user
    private String userId;
    private TransactionMonitor transMon;
    private Timestamp dbTimestamp;
    
    //Added for the COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    private static final String FUNDING_SRC_TYPE_FOR_DEV_PROP = "4";
    private static final String FUNDING_SRC_TYPE_FOR_IP = "5";
    private static final String FUNDING_SRC_TYPE_FOR_AWARD = "6";
    //Added for the COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
    /** Creates a new instance of NotepadTxnBean */
    public NotepadTxnBean(String userId){
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    /** Creates a new instance of NotepadTxnBean */
    public NotepadTxnBean(){
        dbEngine = new DBEngineImpl();
    }
    
    /**
     * This method populates the notes for the Proposal Development in a vector.
     *
     * To fetch the data, it uses the procedure DW_GET_ALL_PROP_NOTEPAD.
     *
     * @return Vector of NotepadBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalDevelopmentNotes(String proposalNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vecNotes = null;
        HashMap notesRow = null;
        NotepadBean notepadBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, proposalNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_ALL_PROP_NOTEPAD ( <<PROPOSAL_NUMBER>> , "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int notesCount =result.size();
        if (notesCount >0){
            vecNotes = new CoeusVector();
            for(int rowIndex=0;rowIndex<notesCount;rowIndex++){
                notepadBean = new NotepadBean();
                notesRow = (HashMap) result.elementAt(rowIndex);
                notepadBean.setProposalAwardNumber(
                (String)notesRow.get("PROPOSAL_NUMBER"));
                notepadBean.setEntryNumber(
                Integer.parseInt(notesRow.get(
                "ENTRY_NUMBER") == null ? "0" : notesRow.get(
                "ENTRY_NUMBER").toString()));
                notepadBean.setComments(
                (String)notesRow.get("COMMENTS"));
                notepadBean.setRestrictedView(
                notesRow.get("RESTRICTED_VIEW") == null ? false
                :(notesRow.get("RESTRICTED_VIEW").toString()
                .equalsIgnoreCase("y") ? true :false));
                notepadBean.setUpdateTimestamp(
                (Timestamp)notesRow.get("UPDATE_TIMESTAMP"));
                notepadBean.setUpdateUser(
                (String)notesRow.get("UPDATE_USER"));
                /*
                 * UserId to UserName Enhancement - Start
                 * Added new property to get username
                 */
                if(notesRow.get("USERNAME") != null) {
                    notepadBean.setUpdateUserName((String)notesRow.get("USERNAME"));
                } else {
                    notepadBean.setUpdateUserName((String)notesRow.get("UPDATE_USER"));
                }
                //UserId to UserName Enhancement - End
                notepadBean.setUserName(
                (String)notesRow.get("USER_NAME"));
                vecNotes.add(notepadBean);
            }
        }
        return vecNotes;
    }
    
    /**
     * This method populates the notes for the Institute Proposal in a vector.
     *
     * To fetch the data, it uses the procedure DW_GET_INSTPROP_NOTEPAD.
     *
     * @return Vector of NotepadBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getInstituteProposalNotes(String instituteProposalNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vecNotes = null;
        HashMap notesRow = null;
        NotepadBean notepadBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("INSTITUTE_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, instituteProposalNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_INSTPROP_NOTEPAD ( <<INSTITUTE_PROPOSAL_NUMBER>> , "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int notesCount =result.size();
        if (notesCount >0){
            vecNotes = new CoeusVector();
            for(int rowIndex=0;rowIndex<notesCount;rowIndex++){
                notepadBean = new NotepadBean();
                notesRow = (HashMap) result.elementAt(rowIndex);
                notepadBean.setProposalAwardNumber(
                (String)notesRow.get("PROPOSAL_NUMBER"));
                notepadBean.setEntryNumber(
                Integer.parseInt(notesRow.get(
                "ENTRY_NUMBER") == null ? "0" : notesRow.get(
                "ENTRY_NUMBER").toString()));
                notepadBean.setComments(
                (String)notesRow.get("COMMENTS"));
                notepadBean.setRestrictedView(
                notesRow.get("RESTRICTED_VIEW") == null ? false
                :(notesRow.get("RESTRICTED_VIEW").toString()
                .equalsIgnoreCase("y") ? true :false));
                notepadBean.setUpdateTimestamp(
                (Timestamp)notesRow.get("UPDATE_TIMESTAMP"));
                notepadBean.setUpdateUser(
                (String)notesRow.get("UPDATE_USER"));
                // 3180:Notepad - By not populated - Start
                if(notesRow.get("USERNAME") != null) {
                    notepadBean.setUpdateUserName((String)notesRow.get("USERNAME"));
                } else {
                    notepadBean.setUpdateUserName((String)notesRow.get("UPDATE_USER"));
                }
                // 3180:Notepad - By not populated - End
                notepadBean.setUserName(
                (String)notesRow.get("USER_NAME"));
                vecNotes.add(notepadBean);
            }
        }
        return vecNotes;
    }
    
    
    /**
     * This method populates the notes for a Award in a vector.
     *
     * To fetch the data, it uses the procedure DW_GET_AWARD_NOTEPAD.
     *
     * @return Vector of NotepadBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardNotes(String awardNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vecNotes = null;
        HashMap notesRow = null;
        NotepadBean notepadBean = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, awardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_NOTEPAD ( <<PROPOSAL_NUMBER>> , "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int notesCount =result.size();
        if (notesCount >0){
            vecNotes = new CoeusVector();
            for(int rowIndex=0;rowIndex<notesCount;rowIndex++){
                notepadBean = new NotepadBean();
                notesRow = (HashMap) result.elementAt(rowIndex);
                notepadBean.setProposalAwardNumber(
                (String)notesRow.get("MIT_AWARD_NUMBER"));
                notepadBean.setEntryNumber(
                Integer.parseInt(notesRow.get(
                "ENTRY_NUMBER") == null ? "0" : notesRow.get(
                "ENTRY_NUMBER").toString()));
                notepadBean.setComments(
                (String)notesRow.get("COMMENTS"));
                notepadBean.setRestrictedView(
                notesRow.get("RESTRICTED_VIEW") == null ? false
                :(notesRow.get("RESTRICTED_VIEW").toString()
                .equalsIgnoreCase("y") ? true :false));
                notepadBean.setUpdateTimestamp(
                (Timestamp)notesRow.get("UPDATE_TIMESTAMP"));
                notepadBean.setUpdateUser(
                (String)notesRow.get("UPDATE_USER"));
                // 3180:Notepad - By not populated - Start
                if(notesRow.get("USERNAME") != null) {
                    notepadBean.setUpdateUserName((String)notesRow.get("USERNAME"));
                } else {
                    notepadBean.setUpdateUserName((String)notesRow.get("UPDATE_USER"));
                }
                // 3180:Notepad - By not populated - End
                notepadBean.setUserName(
                (String)notesRow.get("USER_NAME"));
                vecNotes.add(notepadBean);
            }
        }
        return vecNotes;
    }
    
    /**
     *  This method used get max Dev Proposal Notes Entry Number for the given Proposal number
     *  <li>To fetch the data, it uses the function FN_GET_MAX_DP_NOTES_ENT_NUM.
     *
     *  @return int max entry number for the Proposal number
     *  @param String Proposal number
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getMaxProposalNotesEntryNumber(String proposalNumber)
    throws CoeusException, DBException {
        int entryNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER ENTRY_NUMBER>> = "
            +" call FN_GET_MAX_DP_NOTES_ENT_NUM(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            entryNumber = Integer.parseInt(rowParameter.get("ENTRY_NUMBER").toString());
        }
        return entryNumber;
    }
    
    /**
     *  Method used to update/insert all the NOTES of a Proposal Development.
     *  To update the data, it uses DW_UPD_PROP_NOTEPAD procedure.
     *
     *  @param vctNotePad Vector of NotepadBean
     *  @return boolean true if updated successfully else false
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdProposalDevelopmentNotepad(CoeusVector vctNotePad)
    throws CoeusException, DBException{
        
        Vector paramNotepad = new Vector();
        Vector procedures = new Vector(5,3);
        NotepadBean notepadBean = null;
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean success = false;
        if(vctNotePad!=null && vctNotePad.size() > 0){
            //Get Max Entry Number from DB
            notepadBean = (NotepadBean)vctNotePad.elementAt(0);
            int maxEntryNumber = getMaxProposalNotesEntryNumber(notepadBean.getProposalAwardNumber());
            /*For Bug Fix:1648 to show the last element first start*/
            for(int row = vctNotePad.size()-1; row >=0; row--){
                /*Bug Fix:1648 end*/
                notepadBean = (NotepadBean)vctNotePad.elementAt(row);
                if(notepadBean!=null && notepadBean.getAcType()!=null){
                    paramNotepad = new Vector();
                    paramNotepad.addElement(new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, notepadBean.getProposalAwardNumber()));
                    //If Insert mode set the next Entry Number
                    if(notepadBean.getAcType().equalsIgnoreCase("I")){
                        maxEntryNumber = maxEntryNumber + 1;
                        paramNotepad.addElement(new Parameter("ENTRY_NUMBER",
                        DBEngineConstants.TYPE_INT, ""+maxEntryNumber));
                    }else{
                        paramNotepad.addElement(new Parameter("ENTRY_NUMBER",
                        DBEngineConstants.TYPE_INT, ""+notepadBean.getEntryNumber()));
                    }
                    paramNotepad.addElement(new Parameter("COMMENTS",
                    DBEngineConstants.TYPE_STRING,notepadBean.getComments()));
                    paramNotepad.addElement(new Parameter("RESTRICTED_VIEW",
                    DBEngineConstants.TYPE_STRING, notepadBean.isRestrictedView() ? "Y": "N"));
                    paramNotepad.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
                    paramNotepad.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
                    paramNotepad.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING,notepadBean.getUpdateUser()));
                    paramNotepad.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,notepadBean.getUpdateTimestamp()));
                    paramNotepad.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING, notepadBean.getAcType()));
                    
                    StringBuffer sqlNotepad = new StringBuffer(
                    "call DW_UPD_PROP_NOTEPAD(");
                    sqlNotepad.append(" <<PROPOSAL_NUMBER>> , ");
                    sqlNotepad.append(" <<ENTRY_NUMBER>> , ");
                    sqlNotepad.append(" <<COMMENTS>> , ");
                    sqlNotepad.append(" <<RESTRICTED_VIEW>> , ");
                    sqlNotepad.append(" <<UPDATE_USER>> , ");
                    sqlNotepad.append(" <<UPDATE_TIMESTAMP>> , ");
                    sqlNotepad.append(" <<AW_UPDATE_USER>> , ");
                    sqlNotepad.append(" <<AW_UPDATE_TIMESTAMP>> , ");
                    sqlNotepad.append(" <<AC_TYPE>> )");
                    
                    ProcReqParameter procNotepad  = new ProcReqParameter();
                    procNotepad.setDSN(DSN);
                    procNotepad.setParameterInfo(paramNotepad);
                    procNotepad.setSqlCommand(sqlNotepad.toString());
                    
                    procedures.add(procNotepad);
                }
            }
        }
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    
    /**
     *  Method used to update/insert all the NOTES of a Institute Proposal.
     *  To update the data, it uses DW_UPD_PROPOSAL_NOTEPAD procedure.
     *
     *  @param vctNotePad Vector of NotepadBean
     *  @return boolean true if updated successfully else false
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdInstituteProposalNotepad(CoeusVector vctNotePad)
    throws CoeusException, DBException{
        
        Vector paramNotepad = new Vector();
        Vector procedures = new Vector(5,3);
        NotepadBean notepadBean = null;
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean success = false;
        if(vctNotePad!=null && vctNotePad.size() > 0){
            //Get Max Entry Number from DB
            InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
            notepadBean = (NotepadBean)vctNotePad.elementAt(0);
            int maxEntryNumber = instituteProposalTxnBean.getMaxInstProposalNotesEntryNumber(notepadBean.getProposalAwardNumber());
            /*For Bug Fix:1648 to show the last element first start*/
            for(int row = vctNotePad.size()-1; row >=0; row--){
                /*end Bug Fix:1648 */
                notepadBean = (NotepadBean)vctNotePad.elementAt(row);
                if(notepadBean!=null && notepadBean.getAcType()!=null){
                    paramNotepad = new Vector();
                    paramNotepad.addElement(new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, notepadBean.getProposalAwardNumber()));
                    //If Insert mode set the next Entry Number
                    if(notepadBean.getAcType().equalsIgnoreCase("I")){
                        maxEntryNumber = maxEntryNumber + 1;
                        paramNotepad.addElement(new Parameter("ENTRY_NUMBER",
                        DBEngineConstants.TYPE_INT, ""+maxEntryNumber));
                    }else{
                        paramNotepad.addElement(new Parameter("ENTRY_NUMBER",
                        DBEngineConstants.TYPE_INT, ""+notepadBean.getEntryNumber()));
                    }
                    paramNotepad.addElement(new Parameter("COMMENTS",
                    DBEngineConstants.TYPE_STRING,notepadBean.getComments()));
                    paramNotepad.addElement(new Parameter("RESTRICTED_VIEW",
                    DBEngineConstants.TYPE_STRING, notepadBean.isRestrictedView() ? "Y": "N"));
                    paramNotepad.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
                    paramNotepad.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
                    paramNotepad.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING,notepadBean.getUpdateUser()));
                    paramNotepad.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,notepadBean.getUpdateTimestamp()));
                    paramNotepad.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING, notepadBean.getAcType()));
                    
                    StringBuffer sqlNotepad = new StringBuffer(
                    "call DW_UPD_PROPOSAL_NOTEPAD(");
                    sqlNotepad.append(" <<PROPOSAL_NUMBER>> , ");
                    sqlNotepad.append(" <<ENTRY_NUMBER>> , ");
                    sqlNotepad.append(" <<COMMENTS>> , ");
                    sqlNotepad.append(" <<RESTRICTED_VIEW>> , ");
                    sqlNotepad.append(" <<UPDATE_USER>> , ");
                    sqlNotepad.append(" <<UPDATE_TIMESTAMP>> , ");
                    sqlNotepad.append(" <<AW_UPDATE_USER>> , ");
                    sqlNotepad.append(" <<AW_UPDATE_TIMESTAMP>> , ");
                    sqlNotepad.append(" <<AC_TYPE>> )");
                    
                    ProcReqParameter procNotepad  = new ProcReqParameter();
                    procNotepad.setDSN(DSN);
                    procNotepad.setParameterInfo(paramNotepad);
                    procNotepad.setSqlCommand(sqlNotepad.toString());
                    
                    procedures.add(procNotepad);
                }
            }
        }
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    /**
     *  Method used to update/insert all the NOTES of a Award.
     *  To update the data, it uses DW_UPD_AWARD_NOTEPAD procedure.
     *
     *  @param vctNotePad Vector of NotepadBean
     *  @return boolean true if updated successfully else false
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdAwardNotepad(CoeusVector vctNotePad)
    throws CoeusException, DBException{
        
        Vector paramNotepad = new Vector();
        Vector procedures = new Vector(5,3);
        NotepadBean notepadBean = null;
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean success = false;
        if(vctNotePad!=null && vctNotePad.size() > 0){
            //Get Max Entry Number from DB
            AwardTxnBean awardTxnBean = new AwardTxnBean();
            notepadBean = (NotepadBean)vctNotePad.elementAt(0);
            int maxEntryNumber = awardTxnBean.getMaxInstAwardNotesEntryNumber(notepadBean.getProposalAwardNumber());
            /*For Bug Fix:1648 to show the last element first start*/
            for(int row = vctNotePad.size()-1; row >=0; row--){
                /*end Bug:1648*/
                notepadBean = (NotepadBean)vctNotePad.elementAt(row);
                if(notepadBean!=null && notepadBean.getAcType()!=null){
                    //maxEntryNumber = maxEntryNumber + 1;
                    paramNotepad = new Vector();
                    paramNotepad.addElement(new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, notepadBean.getProposalAwardNumber()));
                    //If Insert mode set the next Entry Number
                    if(notepadBean.getAcType().equalsIgnoreCase("I")){
                        maxEntryNumber = maxEntryNumber + 1;
                        paramNotepad.addElement(new Parameter("ENTRY_NUMBER",
                        DBEngineConstants.TYPE_INT, ""+maxEntryNumber));
                    }else{
                        paramNotepad.addElement(new Parameter("ENTRY_NUMBER",
                        DBEngineConstants.TYPE_INT, ""+notepadBean.getEntryNumber()));
                    }
                    paramNotepad.addElement(new Parameter("COMMENTS",
                    DBEngineConstants.TYPE_STRING,notepadBean.getComments()));
                    paramNotepad.addElement(new Parameter("RESTRICTED_VIEW",
                    DBEngineConstants.TYPE_STRING, notepadBean.isRestrictedView() ? "Y": "N"));
                    paramNotepad.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
                    paramNotepad.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
                    paramNotepad.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING,notepadBean.getUpdateUser()));
                    paramNotepad.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,notepadBean.getUpdateTimestamp()));
                    paramNotepad.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING, notepadBean.getAcType()));
                    
                    StringBuffer sqlNotepad = new StringBuffer(
                    "call DW_UPD_AWARD_NOTEPAD(");
                    sqlNotepad.append(" <<PROPOSAL_NUMBER>> , ");
                    sqlNotepad.append(" <<ENTRY_NUMBER>> , ");
                    sqlNotepad.append(" <<COMMENTS>> , ");
                    sqlNotepad.append(" <<RESTRICTED_VIEW>> , ");
                    sqlNotepad.append(" <<UPDATE_USER>> , ");
                    sqlNotepad.append(" <<UPDATE_TIMESTAMP>> , ");
                    sqlNotepad.append(" <<AW_UPDATE_USER>> , ");
                    sqlNotepad.append(" <<AW_UPDATE_TIMESTAMP>> , ");
                    sqlNotepad.append(" <<AC_TYPE>> )");
                    
                    ProcReqParameter procNotepad  = new ProcReqParameter();
                    procNotepad.setDSN(DSN);
                    procNotepad.setParameterInfo(paramNotepad);
                    procNotepad.setSqlCommand(sqlNotepad.toString());
                    
                    procedures.add(procNotepad);
                }
            }
        }
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    //Coeus Enhancement Case # 1799 start
    public void addUpdNotepad(CoeusVector vctNotePad) throws CoeusException,DBException{
        
        
        if(vctNotePad != null && vctNotePad.size()!=0) {
            CoeusVector awardNotepads = new CoeusVector();
            CoeusVector propDevNotepads = new CoeusVector();
            CoeusVector instPropNotepads= new CoeusVector();
            for(int index= 0;index<vctNotePad.size();index++){
                NotepadBean bean = (NotepadBean)vctNotePad.get(index);
                //Modified for the COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //if(bean.getNotePadType()== "4") {
                if(FUNDING_SRC_TYPE_FOR_DEV_PROP.equals(bean.getNotePadType())) {
                    propDevNotepads.add(bean);
                } else
                    //if(bean.getNotePadType()== "5") {
                    if(FUNDING_SRC_TYPE_FOR_IP.equals(bean.getNotePadType())) {
                    instPropNotepads.add(bean);
                    } else
                        //if(bean.getNotePadType()== "6") {
                        if(FUNDING_SRC_TYPE_FOR_AWARD.equals(bean.getNotePadType())){
                    awardNotepads.add(bean);
                        }
                //Modified for the COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB -
            }
            if(awardNotepads != null && awardNotepads.size()!=0) {
                addUpdAwardNotepad(awardNotepads);
            }
            if(instPropNotepads != null && instPropNotepads.size()!=0) {
                addUpdInstituteProposalNotepad(instPropNotepads);
                
                
            }
            if(propDevNotepads != null && propDevNotepads.size()!=0) {
                addUpdProposalDevelopmentNotepad(propDevNotepads);
            }
        }
    }
    //Coeus Enhancement Case # 1799 end
    
}//end of class