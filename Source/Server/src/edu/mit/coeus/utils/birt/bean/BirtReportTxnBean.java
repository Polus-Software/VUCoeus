/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.utils.birt.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author sharathk
 */
public class BirtReportTxnBean {

    private DBEngineImpl dbEngine;
    private String userId;

    /** Creates new UserDetailsBean */
    public BirtReportTxnBean(String userId) {
        this();
        this.userId = userId;
    }

    public BirtReportTxnBean(){
        dbEngine = new DBEngineImpl();
    }
    /**
     * Retreives all report details from OSP$CUST_REPORT_DETAILS
     * @return list of BirtReportBeans
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public List getAllReports() throws CoeusException, DBException {
        List result, lstReports = null;

        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ALL_CUST_REPORTS ( <<OUT RESULTSET rset>> )", "Coeus", null);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount = result.size();
        if (recCount > 0) {
            Map rowData;
            lstReports = new ArrayList();
            for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
                rowData = (HashMap) result.get(rowIndex);
                lstReports.add(getBean(rowData));
            }
        }
        return lstReports;
    }

    /**
     * returns List of BirtReportBeans for this report type
     * @param type Report Type
     * @return list of BirtReportBeans
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public List getReportForType(int type) throws CoeusException, DBException {
        List lstReports = null;
        Vector result = new Vector();
        Vector param = new Vector();
        param.addElement(new Parameter("REPORT_TYPE", "int", "" + type));
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    //"call GET_REPORT_DETAILS_FOR_TYPE (<<REPORT_TYPE>> ,<<OUT RESULTSET rset>> )", "Coeus", param);
                    "call GET_CUST_REPORT_DET_FOR_TYPE (<<REPORT_TYPE>> ,<<OUT RESULTSET rset>> )", "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount = result.size();
        HashMap rowData;
        lstReports = new ArrayList();
        if (recCount > 0) {
            for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
                rowData = (HashMap) result.get(rowIndex);
                lstReports.add(getBean(rowData));
            }
        }
        return lstReports;
    }

    /**
     * Retreives all report types from OSP$REPORT_TYPE_CODE
     * @return List of ReportTypeBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public List getReportTypes() throws CoeusException, DBException {
        List lstReportType = new ArrayList();
        Vector result = new Vector();
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    //"call GET_REPORT_TYPES ( <<OUT RESULTSET rset>> )", "Coeus", null);
                    "call GET_CUST_REPORT_TYPES ( <<OUT RESULTSET rset>> )", "Coeus", null);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount = result.size();
        ReportTypeBean bean;
        HashMap rowData;
        if (recCount > 0) {
            for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
                bean = new ReportTypeBean();
                rowData = (HashMap) result.get(rowIndex);
                bean.setTypeCode(((BigDecimal)rowData.get("REPORT_TYPE_CODE")).intValue());
                bean.setTypeDescription((String) rowData.get("REPORT_TYPE_DESC"));
                //bean.setModuleBaseWindow((String) rowData.get("MODULE_BASE_WINDOW"));
                lstReportType.add(bean);
            }
        }

        return lstReportType;
    }

    public List getReportTypesForBaseWindow() throws CoeusException, DBException{
         List lstReportType = new ArrayList();
        Vector result = new Vector();
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    "call GET_REPORT_TYPES_FOR_BASE_WIN ( <<OUT RESULTSET rset>> )", "Coeus", null);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount = result.size();
        ReportTypeBean bean;
        HashMap rowData;
        if (recCount > 0) {
            for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
                bean = new ReportTypeBean();
                rowData = (HashMap) result.get(rowIndex);
                bean.setTypeCode(((BigDecimal)rowData.get("REPORT_TYPE_CODE")).intValue());
                //bean.setTypeDescription((String) rowData.get("REPORT_TYPE_DESC"));
                bean.setModuleBaseWindow((String) rowData.get("MODULE_BASE_WINDOW"));
                lstReportType.add(bean);
            }
        }

        return lstReportType;
    }

    /**
     * List of report rights from OSP$RIGHTS whose right code = T
     * @return List of ComboBoxBeans with Right ID and Description
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public List getReportRights() throws CoeusException, DBException {
        List lstReportRights = new ArrayList();
        Vector result = new Vector();
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    "call GET_REPORT_RIGHTS ( <<OUT RESULTSET rset>> )", "Coeus", null);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount = result.size();
        ComboBoxBean bean;
        HashMap rowData;
        if (recCount > 0) {
            for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
                bean = new ComboBoxBean();
                rowData = (HashMap) result.get(rowIndex);
                bean.setCode(rowData.get("RIGHT_ID").toString());
                bean.setDescription((String) rowData.get("DESCRIPTION"));
                lstReportRights.add(bean);
            }
        }
        return lstReportRights;
    }

    /**
     * returns BirtReportBean for this reportId (does not include Report Template bytes)
     * @param reportId
     * @return BirtReportBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public BirtReportBean getReport(int reportId) throws CoeusException, DBException {
        Vector result = new Vector();
        Vector param = new Vector();
        param.addElement(new Parameter("REPORT_ID", "INTEGER", new Integer(reportId)));
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    //"call GET_REPORT_DETAILS (<<REPORT_ID>> ,<<OUT RESULTSET rset>> )", "Coeus", param);
                    "call GET_CUST_REPORT_DETAILS (<<REPORT_ID>> ,<<OUT RESULTSET rset>> )", "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount = result.size();
        HashMap rowData;
        BirtReportBean bean = null;
        if (recCount > 0) {
            for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
                rowData = (HashMap) result.get(rowIndex);
                bean = getBean(rowData);
            }
        }
        return bean;
    }

    /**
     * returns the report template bytes for this report Id
     * @param reportId
     * @return ByteArrayOutputStream which contains report template bytes
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public ByteArrayOutputStream getReportTemplate(int reportId) throws CoeusException, DBException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        StringBuffer sqlReportTemplate = new StringBuffer();
        sqlReportTemplate.append("SELECT REPORT_DESIGN ");
        sqlReportTemplate.append(" FROM  OSP$CUST_REPORT_DETAILS WHERE");
        sqlReportTemplate.append(" REPORT_ID = <<REPORT_ID>> ");

        Vector param = new Vector();
        param.addElement(new Parameter("REPORT_ID", "int", ""+reportId));

        Vector result = new Vector();
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus", sqlReportTemplate.toString(), "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0) {
            HashMap mapRow = (HashMap) result.elementAt(0);
            Object object = mapRow.get("REPORT_DESIGN");
            if (object instanceof ByteArrayOutputStream) {
                byteArrayOutputStream = (ByteArrayOutputStream) object;
            }
        }
        return byteArrayOutputStream;
    }

    /**
     * inserts bean into OSP$CUST_REPORT_DETAILS
     * @param bean BirtReportBean to insert
     * @return BirtReportBean with timestamp and report Id populated.
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public BirtReportBean insertReport(BirtReportBean bean) throws CoeusException, DBException {
        Timestamp timestamp = (new CoeusFunctions()).getDBTimestamp();
        bean.setReportId(getMaxReportId() + 1);
        bean.setUpdateTimestamp(timestamp);
        bean.setUpdateUser(userId);
        bean.setDesignUpdateTimestamp(timestamp);
        bean.setDesignUpdateUser(userId);

        ProcReqParameter procReqParameter = getInsertProc(bean);
        List procList = new ArrayList();
        procList.add(procReqParameter);
        executeProcs(procList);
        return bean;
    }

    /**
     * Updates BirtReportBean into OSP$CUST_REPORT_DETAILS
     * @param bean to update
     * @return BirtReportBean with updated timestamp
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public BirtReportBean updateReport(BirtReportBean bean) throws CoeusException, DBException {
        Timestamp timestamp = (new CoeusFunctions()).getDBTimestamp();
        bean.setUpdateTimestamp(timestamp);
        bean.setUpdateUser(userId);
        if (bean.getReport() != null) {
            bean.setDesignUpdateTimestamp(timestamp);
            bean.setDesignUpdateUser(userId);
        }
        ProcReqParameter procReqParameter = getUpdateProc(bean);
        List procList = new ArrayList();
        procList.add(procReqParameter);
        executeProcs(procList);
        return bean;
    }

    /**
     * update Report Template Only
     * @param bean to update
     * @return BirtReportBean with updated timestamp
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public BirtReportBean updateReportTemplate(BirtReportBean bean) throws CoeusException, DBException {
        Timestamp timestamp = (new CoeusFunctions()).getDBTimestamp();
        bean.setDesignUpdateTimestamp(timestamp);
        bean.setDesignUpdateUser(userId);
        ProcReqParameter procReqParameter = getTemplateUpdateProc(bean);
        List procList = new ArrayList();
        procList.add(procReqParameter);
        executeProcs(procList);
        return bean;
    }

    /**
     * Deletes the report bean passed from the database table OSP$CUST_REPORT_DETAILS
     * @param bean to delete
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public void deleteReport(BirtReportBean bean) throws CoeusException, DBException {
        ProcReqParameter procReqParameter = getDeleteProc(bean);
        List procList = new ArrayList();
        procList.add(procReqParameter);
        executeProcs(procList);
    }

    /**
     * execute proceedures
     * @param sqlList List of sql, procedure calls
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public void executeProcs(List sqlList) throws CoeusException, DBException {
        if (dbEngine != null) {
            java.sql.Connection conn = null;
            try {
                conn = dbEngine.beginTxn();
                for (int index = 0; index < sqlList.size(); index++) {
                    dbEngine.batchSQLUpdate((ProcReqParameter) sqlList.get(index), conn);
                }
                dbEngine.commit(conn);
            } catch (Exception sqlEx) {
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            } finally {
                dbEngine.endTxn(conn);
            }
        //dbEngine.endTxn(conn);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
    }

    /**
     * generate ProcReqParamater for BirtReportBean
     * @param birtReportBean for which ProcReqParamater has to be generated.
     * @return ProcReqParameter
     * @throws edu.mit.coeus.utils.dbengine.DBException if any exception occurs
     */
    private ProcReqParameter getInsertProc(BirtReportBean birtReportBean) throws CoeusException, DBException {
        StringBuffer sqlInsertReport = new StringBuffer();

        sqlInsertReport.append("insert into OSP$CUST_REPORT_DETAILS(");
        sqlInsertReport.append(" REPORT_ID, ");
        sqlInsertReport.append(" REPORT_LABEL, ");
        sqlInsertReport.append(" REPORT_DESCRIPTION, ");
        sqlInsertReport.append(" REPORT_TYPE_CODE, ");
        sqlInsertReport.append(" RIGHT_REQUIRED, ");
        sqlInsertReport.append(" REPORT_DESIGN, ");
        sqlInsertReport.append(" DESIGN_FILE_NAME, ");
        sqlInsertReport.append(" UPDATE_TIMESTAMP, ");
        sqlInsertReport.append(" UPDATE_USER, ");
        sqlInsertReport.append(" DESIGN_UPDATE_USER, ");
        sqlInsertReport.append(" DESIGN_UPDATE_TIMESTAMP ) ");
        sqlInsertReport.append(" VALUES (");
        sqlInsertReport.append(" <<REPORT_ID>> , ");
        sqlInsertReport.append(" <<REPORT_LABEL>> , ");
        sqlInsertReport.append(" <<REPORT_DESCRIPTION>> , ");
        sqlInsertReport.append(" <<REPORT_TYPE_CODE>> , ");
        sqlInsertReport.append(" <<RIGHT_REQUIRED>> , ");
        sqlInsertReport.append(" <<REPORT_DESIGN>> , ");
        sqlInsertReport.append(" <<DESIGN_FILE_NAME>> , ");
        sqlInsertReport.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlInsertReport.append(" <<UPDATE_USER>> , ");
        sqlInsertReport.append(" <<DESIGN_UPDATE_USER>> , ");
        sqlInsertReport.append(" <<DESIGN_UPDATE_TIMESTAMP>> ) ");

        Vector param = setReportParams(birtReportBean);

        ProcReqParameter procInsertReportDetails = new ProcReqParameter();
        procInsertReportDetails.setDSN("Coeus");
        procInsertReportDetails.setParameterInfo(param);
        procInsertReportDetails.setSqlCommand(sqlInsertReport.toString());
        return procInsertReportDetails;
    }

    /**
     * generate ProcReqParamater for BirtReportBean for uploading report template
     * @param birtReportBean
     * @return ProcReqParameter
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    private ProcReqParameter getTemplateUpdateProc(BirtReportBean birtReportBean) throws CoeusException, DBException {
        StringBuffer sqlUpdateTemplate = new StringBuffer();
        sqlUpdateTemplate.append("update OSP$CUST_REPORT_DETAILS set");
        sqlUpdateTemplate.append(" REPORT_DESIGN  = ");
        sqlUpdateTemplate.append(" <<REPORT_DESIGN>> , ");
        sqlUpdateTemplate.append(" DESIGN_FILE_NAME = ");
        sqlUpdateTemplate.append(" <<DESIGN_FILE_NAME>> , ");
        sqlUpdateTemplate.append(" DESIGN_UPDATE_TIMESTAMP = ");
        sqlUpdateTemplate.append(" <<DESIGN_UPDATE_TIMESTAMP>> , ");
        sqlUpdateTemplate.append(" DESIGN_UPDATE_USER = ");
        sqlUpdateTemplate.append(" <<DESIGN_UPDATE_USER>> ");
        sqlUpdateTemplate.append(" WHERE ");
        sqlUpdateTemplate.append(" REPORT_ID = ");
        sqlUpdateTemplate.append(" <<REPORT_ID>> ");
        sqlUpdateTemplate.append(" AND DESIGN_UPDATE_TIMESTAMP = ");
        sqlUpdateTemplate.append(" <<AW_DESIGN_UPDATE_TIMESTAMP>>  ");

        Vector param = setReportParams(birtReportBean);

        ProcReqParameter procUpdateReportTemplate = new ProcReqParameter();
        procUpdateReportTemplate.setDSN("Coeus");
        procUpdateReportTemplate.setParameterInfo(param);
        procUpdateReportTemplate.setSqlCommand(sqlUpdateTemplate.toString());
        return procUpdateReportTemplate;
    }

    /**
     * generate ProcReqParamater for BirtReportBean
     * @param birtReportBean for which ProcReqParamater has to be generated.
     * @return ProcReqParameter
     * @throws edu.mit.coeus.utils.dbengine.DBException if any exception occurs
     */
    private ProcReqParameter getUpdateProc(BirtReportBean birtReportBean) throws CoeusException, DBException {
        StringBuffer sqlUpdateReport = new StringBuffer();

        sqlUpdateReport.append("update OSP$CUST_REPORT_DETAILS set");
        sqlUpdateReport.append(" REPORT_LABEL = ");
        sqlUpdateReport.append(" <<REPORT_LABEL>> , ");
        sqlUpdateReport.append(" REPORT_DESCRIPTION = ");
        sqlUpdateReport.append(" <<REPORT_DESCRIPTION>> , ");
        sqlUpdateReport.append(" REPORT_TYPE_CODE  = ");
        sqlUpdateReport.append(" <<REPORT_TYPE_CODE>> , ");
        sqlUpdateReport.append(" RIGHT_REQUIRED = ");
        sqlUpdateReport.append(" <<RIGHT_REQUIRED>> , ");
        sqlUpdateReport.append(" UPDATE_TIMESTAMP  = ");
        sqlUpdateReport.append(" <<UPDATE_TIMESTAMP>> , ");
        if (birtReportBean.getReport() != null) {
            sqlUpdateReport.append(" REPORT_DESIGN  = ");
            sqlUpdateReport.append(" <<REPORT_DESIGN>> , ");
            sqlUpdateReport.append(" DESIGN_FILE_NAME = ");
            sqlUpdateReport.append(" <<DESIGN_FILE_NAME>> , ");
            sqlUpdateReport.append(" DESIGN_UPDATE_TIMESTAMP = ");
            sqlUpdateReport.append(" <<DESIGN_UPDATE_TIMESTAMP>> , ");
            sqlUpdateReport.append(" DESIGN_UPDATE_USER = ");
            sqlUpdateReport.append(" <<DESIGN_UPDATE_USER>> , ");
        }
        sqlUpdateReport.append(" UPDATE_USER = ");
        sqlUpdateReport.append(" <<UPDATE_USER>> ");
        sqlUpdateReport.append(" WHERE ");
        sqlUpdateReport.append(" REPORT_ID = ");
        sqlUpdateReport.append(" <<REPORT_ID>> ");
        sqlUpdateReport.append(" AND UPDATE_TIMESTAMP = ");
        sqlUpdateReport.append(" <<AW_UPDATE_TIMESTAMP>>  ");

        Vector param = setReportParams(birtReportBean);

        ProcReqParameter procUpdateReportDetails = new ProcReqParameter();
        procUpdateReportDetails.setDSN("Coeus");
        procUpdateReportDetails.setParameterInfo(param);
        procUpdateReportDetails.setSqlCommand(sqlUpdateReport.toString());
        return procUpdateReportDetails;
    }

    private ProcReqParameter getDeleteProc(BirtReportBean birtReportBean) throws CoeusException, DBException {
        StringBuffer sqlDeleteReport = new StringBuffer();
        sqlDeleteReport.append("delete OSP$CUST_REPORT_DETAILS ");
        sqlDeleteReport.append(" WHERE ");
        sqlDeleteReport.append(" REPORT_ID = ");
        sqlDeleteReport.append(" <<REPORT_ID>> ");
        sqlDeleteReport.append(" AND UPDATE_TIMESTAMP = ");
        sqlDeleteReport.append(" <<UPDATE_TIMESTAMP>>  ");
        sqlDeleteReport.append(" AND DESIGN_UPDATE_TIMESTAMP = ");
        sqlDeleteReport.append(" <<DESIGN_UPDATE_TIMESTAMP>>  ");

        Vector param = setReportParams(birtReportBean);

        ProcReqParameter procUpdateReportDetails = new ProcReqParameter();
        procUpdateReportDetails.setDSN("Coeus");
        procUpdateReportDetails.setParameterInfo(param);
        procUpdateReportDetails.setSqlCommand(sqlDeleteReport.toString());
        return procUpdateReportDetails;
    }

    private int getMaxReportId() throws CoeusException, DBException {
        Vector result = new Vector();
        int maxReportId = 0;
        if (dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER MAX_REPORT_ID>> = call FN_GET_NEXT_CUST_REPORT_ID( ) }", null);

        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            HashMap row = (HashMap) result.elementAt(0);
            String reportId = row.get("MAX_REPORT_ID").toString();
            maxReportId = Integer.parseInt(reportId);
        }
        return maxReportId;
    }

    private Vector setReportParams(BirtReportBean birtReportBean) throws DBException {
        Vector param = new Vector();
        param.addElement(new Parameter("REPORT_ID", "int", ""+birtReportBean.getReportId()));
        param.addElement(new Parameter("REPORT_LABEL", "String", birtReportBean.getReportLabel()));
        param.addElement(new Parameter("REPORT_DESCRIPTION", "String", birtReportBean.getReportDescription()));
        param.addElement(new Parameter("REPORT_TYPE_CODE", "int", "" + birtReportBean.getReportTypeCode()));
        param.addElement(new Parameter("RIGHT_REQUIRED", "String", birtReportBean.getRight()));
        if (birtReportBean.getReport() != null) {
            param.addElement(new Parameter("REPORT_DESIGN", DBEngineConstants.TYPE_BLOB, birtReportBean.getReport()));
        }
        param.addElement(new Parameter("DESIGN_FILE_NAME", "String", birtReportBean.getReportName()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP", "Timestamp", birtReportBean.getUpdateTimestamp()));
        param.addElement(new Parameter("UPDATE_USER", "String", birtReportBean.getUpdateUser()));
        param.addElement(new Parameter("DESIGN_UPDATE_USER", "String", birtReportBean.getDesignUpdateUser()));
        param.addElement(new Parameter("DESIGN_UPDATE_TIMESTAMP", "Timestamp", birtReportBean.getDesignUpdateTimestamp()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", "Timestamp", birtReportBean.getAwUpdateTimestamp()));
        param.addElement(new Parameter("AW_DESIGN_UPDATE_TIMESTAMP", "Timestamp", birtReportBean.getAwDesignUpdateTimestamp()));
        return param;
    }

    /**
     * returns BirtReportBean from Database Row Data from OSP$CUST_REPORT_DETAILS
     * @param rowData
     * @return BirtReportBean
     */
    private BirtReportBean getBean(Map rowData) {
        BirtReportBean birtReportBean = new BirtReportBean();
        birtReportBean.setReportId(((BigDecimal) rowData.get("REPORT_ID")).intValue());
        birtReportBean.setReportLabel((String) rowData.get("REPORT_LABEL"));
        birtReportBean.setReportDescription((String) rowData.get("REPORT_DESCRIPTION"));
        if (rowData.get("REPORT_TYPE_CODE") != null) {
            birtReportBean.setReportTypeCode(((BigDecimal) rowData.get("REPORT_TYPE_CODE")).intValue());
        }
        birtReportBean.setReportTypeDesc((String) rowData.get("REPORT_TYPE_DESC"));
        birtReportBean.setRight((String) rowData.get("RIGHT_REQUIRED"));
        birtReportBean.setReportName((String) rowData.get("DESIGN_FILE_NAME"));
        birtReportBean.setUpdateTimestamp((Timestamp) rowData.get("UPDATE_TIMESTAMP"));
        birtReportBean.setUpdateUser((String) rowData.get("UPDATE_USER"));
        birtReportBean.setDesignUpdateTimestamp((Timestamp) rowData.get("DESIGN_UPDATE_TIMESTAMP"));
        birtReportBean.setDesignUpdateUser((String) rowData.get("DESIGN_UPDATE_USER"));
        return birtReportBean;
    }


    public Map getParameterValueCodes(String baseWindow) throws DBException, CoeusException {
        return getParameterValueCodes(baseWindow, "GET_CUST_RPT_PARAM_VALUE_CODES");
    }

     public Map getParameterValueCodes(String baseWindow, String procedure) throws DBException, CoeusException {
        HashMap valueCodes = new HashMap();
        Vector result = new Vector();
        Vector param = new Vector();
        //param.addElement(new Parameter("REPORT_ID", "INTEGER", new Integer(reportId)));
        param.addElement(new Parameter("BASE_WINDOW", "String", baseWindow));
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    //"call GET_REPORT_PARAM_VALUE_CODES (<<REPORT_ID>> ,<<OUT RESULTSET rset>> )", "Coeus", param);
                    "call " + procedure + " (<<BASE_WINDOW>> ,<<OUT RESULTSET rset>> )", "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount = result.size();
        HashMap rowData;
        if (recCount > 0) {
            for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
                rowData = (HashMap) result.get(rowIndex);
                valueCodes.put(rowData.get("PARAMETER_NAME"), rowData.get("JAVA_STATEMENTS"));
            }
        }
        return valueCodes;
    }

    public Map getMenuParameterValueCodes(String baseWindow) throws DBException, CoeusException, IOException{
        Map paramValueCodes = getParameterValueCodes(baseWindow, "GET_CUST_RPT_UNIT_FOR_AUTH");
        return paramValueCodes;
    }

    public List mapValueCodes(List beanList, Map valueCodes) {
        if(valueCodes != null && !valueCodes.isEmpty()) {
            BirtParameterBean bean;
            for(int index=0; index<beanList.size(); index++) {
                bean = (BirtParameterBean)beanList.get(index);
                if(valueCodes.containsKey(bean.getName())){
                    bean.setParameterValueCode((String)valueCodes.get(bean.getName()));
                }
            }
        }
        return beanList;
    }

    public boolean hasRight(){
        boolean hasRight = false;

        return hasRight;
    }


}
