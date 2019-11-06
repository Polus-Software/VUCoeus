/*
 * @(#)DepartmentPersonUpdateTxnBean.java 1.0 03/17/03 10:25 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 24-MAY-2007
 * by Leena
 */
package edu.mit.coeus.departmental.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.CoeusFunctions;

import java.util.Vector;
import java.sql.Timestamp;

import edu.mit.coeus.utils.CoeusVector;

/**
 * This class provides the methods for performing modify/insert and delete
 * procedure executions for a Department person others tab functionality. Various
 * methods are used to modify/insert the data for "Departmentperson"
 * from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the
 * database interaction.
 *
 * @version 1.0 March 17, 2003, 10:25 AM
 * @author  Mukundan C
 *
 */

public class DepartmentPersonUpdateTxnBean {
    
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    // holds the charater for modify
//    private static final String MODIFY = "U";
    // holds the charater for insert
//    private static final String INSERT = "I";
    // holds the userId for the logged in user
    private String userId;
    private Timestamp dbTimestamp;
    
    
    //private DepartmentPersonUpdateTxnBean departmentPersonUpdateTxnBean;
    /** Creates a new instance of DepartmentPersonUpdateTxnBean */
    public DepartmentPersonUpdateTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    /**
     * Creates new DepartmentPersonUpdateTxnBean .
     * @param userId String Loggedin userid
     */
    public DepartmentPersonUpdateTxnBean(String userId) {
        this.userId = userId;
        //dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        dbEngine = new DBEngineImpl();
    }
    
    /**
     *  Method used to modify/insert all the details of a Department person details
     *  for person others.
     *
     *  @param Vector of departement person form bean
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean mainAddUpdPersonOther( Vector vecPersonOthers
            )  throws CoeusException,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if ((vecPersonOthers != null) && (vecPersonOthers.size() > 0)){
            int atLength = vecPersonOthers.size();
            for(int rowIndex=0; rowIndex<atLength; rowIndex++){
                PersonCustomElementsInfoBean departmentOthersFormBean =
                        (PersonCustomElementsInfoBean)vecPersonOthers.elementAt(rowIndex);
                procedures.add(addUpdPersonOther(departmentOthersFormBean));
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
     *  Method used to update/insert all the details of a Department person others
     *  details for person.
     *
     *  @param PersonCustomElementsInfoBean details of others details.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if any error during database transaction.
     */
    public ProcReqParameter addUpdPersonOther( PersonCustomElementsInfoBean
            departmentOthersFormBean)  throws DBException{
        
        Vector paramPersonOther= new Vector();
        //dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //departmentPersonUpdateTxnBean = new DepartmentPersonUpdateTxnBean(userId);
        
        paramPersonOther.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                departmentOthersFormBean.getPersonId()));
        paramPersonOther.addElement(new Parameter("COLUMN_NAME",
                DBEngineConstants.TYPE_STRING,
                departmentOthersFormBean.getColumnName()));
        paramPersonOther.addElement(new Parameter("COLUMN_VALUE",
                DBEngineConstants.TYPE_STRING,
                departmentOthersFormBean.getColumnValue()));
        paramPersonOther.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramPersonOther.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPersonOther.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                departmentOthersFormBean.getPersonId()));
        paramPersonOther.addElement(new Parameter("AW_COLUMN_NAME",
                DBEngineConstants.TYPE_STRING,
                departmentOthersFormBean.getColumnName()));
        paramPersonOther.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                departmentOthersFormBean.getUpdateTimestamp()));
        paramPersonOther.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                departmentOthersFormBean.getAcType()));
        
        StringBuffer sqlPersonOther = new StringBuffer(
                "call DW_UPDATE_PERSON_CUSTOM_DATA(");
        sqlPersonOther.append(" <<PERSON_ID>> , ");
        sqlPersonOther.append(" <<COLUMN_NAME>> , ");
        sqlPersonOther.append(" <<COLUMN_VALUE>> , ");
        sqlPersonOther.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlPersonOther.append(" <<UPDATE_USER>> , ");
        sqlPersonOther.append(" <<AW_PERSON_ID>> , ");
        sqlPersonOther.append(" <<AW_COLUMN_NAME>> , ");
        sqlPersonOther.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlPersonOther.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procPersonOther  = new ProcReqParameter();
        procPersonOther.setDSN(DSN);
        procPersonOther.setParameterInfo(paramPersonOther);
        procPersonOther.setSqlCommand(sqlPersonOther.toString());
        
        return procPersonOther;
        
    }
    
    /**
     *  Method used to modify/insert all the details of a Department person details
     *  for person others.
     *
     *  @param Vector of departement person form bean
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean mainAddUpdPersonBio( Vector vecPersonBio
            )  throws CoeusException,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        Vector insertPDFProcedures = new Vector(3,2);
        Vector insertSourceProcedures = new Vector(3,2);
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
        
//        if ((vecPersonBio != null) && (vecPersonBio.size() > 0)){
//            int atLength = vecPersonBio.size();
//            for(int rowIndex=0; rowIndex<atLength; rowIndex++){
//                DepartmentBioPersonFormBean departmentBioPersonFormBean =
//                            (DepartmentBioPersonFormBean)vecPersonBio.elementAt(rowIndex);
//                if(departmentBioPersonFormBean!=null && departmentBioPersonFormBean.getAcType()!=null){
//                    procedures.add(addUpdPersonBio(departmentBioPersonFormBean));
//                }
//            }
//        }
        byte[] fileData = null;
        //Now Update Blob data - start
        DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean = null;
        DepartmentBioSourceFormBean departmentBioSourceFormBean = null;
        for(int rowIndex=0; rowIndex<vecPersonBio.size(); rowIndex++){
            DepartmentBioPersonFormBean departmentBioPersonFormBean =
                    (DepartmentBioPersonFormBean)vecPersonBio.elementAt(rowIndex);
            if(departmentBioPersonFormBean!=null && departmentBioPersonFormBean.getAcType()!=null){
                procedures.add(addUpdPersonBio(departmentBioPersonFormBean));
                //Update PDF Blob
                
                if(departmentBioPersonFormBean.getDepartmentBioPDFPersonFormBean()!=null &&
                        departmentBioPersonFormBean.getDepartmentBioPDFPersonFormBean().getAcType()!=null){
                    departmentBioPDFPersonFormBean = departmentBioPersonFormBean.getDepartmentBioPDFPersonFormBean();
                    if(departmentBioPDFPersonFormBean.getAcType().equalsIgnoreCase("I")){
                        departmentBioPDFPersonFormBean.setBioNumber(departmentBioPersonFormBean.getBioNumber());
                        insertPDFProcedures.add(addPersonBioPDF(departmentBioPDFPersonFormBean));
                    }else if(departmentBioPDFPersonFormBean.getAcType().equalsIgnoreCase("U")){
                        //Get the data to be Updated
                        fileData = departmentBioPDFPersonFormBean.getFileBytes();
                        //Added for case 3685 - Remove Word icons - start
                        String fileName = departmentBioPDFPersonFormBean.getFileName();
                        //Added for case 3685 - Remove Word icons - end
                        //Added for case 4007 - Icon based on mime type - start
                        String mimeType = departmentBioPDFPersonFormBean.getMimeType();
                        //4007 - end
                        
                        //Get data from server
                        departmentBioPDFPersonFormBean = departmentPersonTxnBean.getPersonBioPDF(departmentBioPDFPersonFormBean);
                        //If Update delete and Insert
                        departmentBioPDFPersonFormBean.setAcType("D");
                        procedures.add(addUpdPersonBioPDF(departmentBioPDFPersonFormBean));
                        
                        //Now insert
                        departmentBioPDFPersonFormBean.setAcType("I");
                        departmentBioPDFPersonFormBean.setFileBytes(fileData);
                        //Added for case 3685 - Remove Word icons - start
                        departmentBioPDFPersonFormBean.setFileName(fileName);
                        //Added for case 3685 - Remove Word icons - end
                        departmentBioPDFPersonFormBean.setMimeType(mimeType);//4007
                        insertPDFProcedures.add(addPersonBioPDF(departmentBioPDFPersonFormBean));
                    }
                }
                //Update Word Blob
                if(departmentBioPersonFormBean.getDepartmentBioSourceFormBean()!=null &&
                        departmentBioPersonFormBean.getDepartmentBioSourceFormBean().getAcType()!=null){
                    departmentBioSourceFormBean = departmentBioPersonFormBean.getDepartmentBioSourceFormBean();
                    if(departmentBioSourceFormBean.getAcType().equalsIgnoreCase("I")){
                        departmentBioSourceFormBean.setBioNumber(departmentBioPersonFormBean.getBioNumber());
                        insertSourceProcedures.add(addPersonBioSource(departmentBioSourceFormBean));
                    }else if(departmentBioSourceFormBean.getAcType().equalsIgnoreCase("U")){
                        fileData = departmentBioSourceFormBean.getFileBytes();
                        //Get data from server
                        departmentBioSourceFormBean = departmentPersonTxnBean.getPersonBioSource(departmentBioSourceFormBean);
                        
                        //If Update delete and Insert
                        departmentBioSourceFormBean.setAcType("D");
                        
                        procedures.add(addUpdPersonBioSource(departmentBioSourceFormBean));
                        
                        //Now insert
                        departmentBioSourceFormBean.setAcType("I");
                        departmentBioSourceFormBean.setFileBytes(fileData);
                        insertSourceProcedures.add(addPersonBioSource(departmentBioSourceFormBean));
                    }
                }
            }
        }
        
        if(dbEngine!=null){
            java.sql.Connection conn = null;
            try{
                //Begin a new Transaction
                conn = dbEngine.beginTxn();
                //Update other data
                dbEngine.executeStoreProcs(procedures, conn);
                //Update Bio Source PDF Data
                if(insertPDFProcedures!=null && insertPDFProcedures.size() > 0){
                    dbEngine.batchSQLUpdate(insertPDFProcedures, conn);
                }
                //Update Bio Source Blob Data
                if(insertSourceProcedures!=null && insertSourceProcedures.size() > 0){
                    dbEngine.batchSQLUpdate(insertSourceProcedures, conn);
                }
                //End Txn
                dbEngine.endTxn(conn);
            }catch(Exception ex){
                dbEngine.rollback(conn);
                throw new DBException(ex);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        //Now Update Blob data - end
        
        success = true;
        return success;
    }
    
    /**
     *  Method used to update/insert all the details of a Departement person biography.
     *  <li>To fetch the data, it uses DW_UPDATE_PER_BIO procedure.
     *
     *  @param DepartmentBioPersonFormBean this bean contains data for
     *  insert/modifying the department person biography details.
     *  @return boolean true for successful insert/modify
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
    public ProcReqParameter addUpdPersonBio( DepartmentBioPersonFormBean
            departmentBioPersonFormBean)  throws CoeusException ,DBException{
//        boolean success = false;
//        Vector procedures = new Vector(5,3);
        Vector paramPersonBio= new Vector();
        
        paramPersonBio.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                departmentBioPersonFormBean.getPersonId()));
        paramPersonBio.addElement(new Parameter("BIO_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+departmentBioPersonFormBean.getBioNumber()));
        paramPersonBio.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING,
                departmentBioPersonFormBean.getDescription()));
        paramPersonBio.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPersonBio.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramPersonBio.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                departmentBioPersonFormBean.getPersonId()));
        paramPersonBio.addElement(new Parameter("AW_BIO_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+departmentBioPersonFormBean.getBioNumber()));
        paramPersonBio.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                departmentBioPersonFormBean.getUpdateTimestamp()));
        paramPersonBio.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                departmentBioPersonFormBean.getAcType()));
        //Case 2793:NOW Person Maintainer - Uploading documents
        paramPersonBio.addElement(new Parameter("DOC_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                String.valueOf(departmentBioPersonFormBean.getDocumentTypeCode())));
        //2793 End
        StringBuffer sqlPersonBio = new StringBuffer(
                "call UPDATE_PER_BIO(");
        sqlPersonBio.append(" <<PERSON_ID>> , ");
        sqlPersonBio.append(" <<BIO_NUMBER>> , ");
        sqlPersonBio.append(" <<DESCRIPTION>> , ");
        sqlPersonBio.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlPersonBio.append(" <<UPDATE_USER>> , ");
        sqlPersonBio.append(" <<DOC_TYPE_CODE>> , ");//2793
        sqlPersonBio.append(" <<AW_PERSON_ID>> , ");
        sqlPersonBio.append(" <<AW_BIO_NUMBER>> , ");
        sqlPersonBio.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlPersonBio.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procPersonBio  = new ProcReqParameter();
        procPersonBio.setDSN(DSN);
        procPersonBio.setParameterInfo(paramPersonBio);
        procPersonBio.setSqlCommand(sqlPersonBio.toString());
        
        return procPersonBio;
    }
    
    /**
     *  Method used to update/insert all the details of a Departement person biographyPDF.
     *  <li>To fetch the data, it uses DW_UPDATE_PER_BIO_PDF procedure.
     *
     *  @param DepartmentBioPDFPersonFormBean this bean contains data for
     *  insert/modifying the department person biography details.
     *  @return boolean true for successful insert/modify
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
    public ProcReqParameter addUpdPersonBioPDF( DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean)
    throws CoeusException ,DBException{
        Vector paramPersonBioPDF= new Vector();
        
        paramPersonBioPDF.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                departmentBioPDFPersonFormBean.getPersonId()));
        paramPersonBioPDF.addElement(new Parameter("BIO_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+departmentBioPDFPersonFormBean.getBioNumber()));
        paramPersonBioPDF.addElement(new Parameter("FILE_NAME",
                DBEngineConstants.TYPE_STRING,
                departmentBioPDFPersonFormBean.getFileName()));
        paramPersonBioPDF.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPersonBioPDF.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramPersonBioPDF.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                departmentBioPDFPersonFormBean.getPersonId()));
        paramPersonBioPDF.addElement(new Parameter("AW_BIO_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+departmentBioPDFPersonFormBean.getBioNumber()));
        paramPersonBioPDF.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                departmentBioPDFPersonFormBean.getUpdateTimestamp()));
        paramPersonBioPDF.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                departmentBioPDFPersonFormBean.getAcType()));
        
        StringBuffer sqlPersonBioPDF = new StringBuffer(
                "call DW_UPDATE_PER_BIO_PDF(");
        sqlPersonBioPDF.append(" <<PERSON_ID>> , ");
        sqlPersonBioPDF.append(" <<BIO_NUMBER>> , ");
        sqlPersonBioPDF.append(" <<FILE_NAME>> , ");
        sqlPersonBioPDF.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlPersonBioPDF.append(" <<UPDATE_USER>> , ");
        sqlPersonBioPDF.append(" <<AW_PERSON_ID>> , ");
        sqlPersonBioPDF.append(" <<AW_BIO_NUMBER>> , ");
        sqlPersonBioPDF.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlPersonBioPDF.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procPersonBioPDF  = new ProcReqParameter();
        procPersonBioPDF.setDSN(DSN);
        procPersonBioPDF.setParameterInfo(paramPersonBioPDF);
        procPersonBioPDF.setSqlCommand(sqlPersonBioPDF.toString());
        
        return procPersonBioPDF;
    }
    
    /**
     *  Method used to update/insert all the details of a Departement person biographySource.
     *  <li>To fetch the data, it uses DW_UPDATE_PER_BIO_SOURCE procedure.
     *
     *  @param DepartmentBioSourceFormBean this bean contains data for
     *  insert/modifying the department person biography details.
     *  @return boolean true for successful insert/modify
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
    public ProcReqParameter addUpdPersonBioSource( DepartmentBioSourceFormBean
            departmentBioSourceFormBean)  throws CoeusException ,DBException{
//        boolean success = false;
//        Vector procedures = new Vector(5,3);
        Vector paramPersonBioSource= new Vector();
        
        paramPersonBioSource.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                departmentBioSourceFormBean.getPersonId()));
        paramPersonBioSource.addElement(new Parameter("BIO_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+departmentBioSourceFormBean.getBioNumber()));
        paramPersonBioSource.addElement(new Parameter("SOURCE_EDITOR",
                DBEngineConstants.TYPE_STRING,
                departmentBioSourceFormBean.getSourceEditor()));
        paramPersonBioSource.addElement(new Parameter("FILE_NAME",
                DBEngineConstants.TYPE_STRING,
                departmentBioSourceFormBean.getFileName()));
        paramPersonBioSource.addElement(new Parameter("INPUT_TYPE",
                DBEngineConstants.TYPE_STRING,
                new Character(departmentBioSourceFormBean.getInputType()).toString()));
        paramPersonBioSource.addElement(new Parameter("PLATFORM_TYPE",
                DBEngineConstants.TYPE_STRING,
                new Character(departmentBioSourceFormBean.getPlatformType()).toString()));
        paramPersonBioSource.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPersonBioSource.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramPersonBioSource.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                departmentBioSourceFormBean.getPersonId()));
        paramPersonBioSource.addElement(new Parameter("AW_BIO_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+departmentBioSourceFormBean.getBioNumber()));
        paramPersonBioSource.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                departmentBioSourceFormBean.getUpdateTimestamp()));
        paramPersonBioSource.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                departmentBioSourceFormBean.getAcType()));
        
        StringBuffer sqlPersonBioSource = new StringBuffer(
                "call DW_UPDATE_PER_BIO_SOURCE(");
        sqlPersonBioSource.append(" <<PERSON_ID>> , ");
        sqlPersonBioSource.append(" <<BIO_NUMBER>> , ");
        sqlPersonBioSource.append(" <<SOURCE_EDITOR>> , ");
        sqlPersonBioSource.append(" <<FILE_NAME>> , ");
        sqlPersonBioSource.append(" <<INPUT_TYPE>> , ");
        sqlPersonBioSource.append(" <<PLATFORM_TYPE>> , ");
        sqlPersonBioSource.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlPersonBioSource.append(" <<UPDATE_USER>> , ");
        sqlPersonBioSource.append(" <<AW_PERSON_ID>> , ");
        sqlPersonBioSource.append(" <<AW_BIO_NUMBER>> , ");
        sqlPersonBioSource.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlPersonBioSource.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procPersonBioSource  = new ProcReqParameter();
        procPersonBioSource.setDSN(DSN);
        procPersonBioSource.setParameterInfo(paramPersonBioSource);
        procPersonBioSource.setSqlCommand(sqlPersonBioSource.toString());
        return procPersonBioSource;
    }
    
    /**
     *  Method used to modify/insert all the details of a Department person degree
     *  details for person degree.
     *
     *  @param Vector of departement person Degree form bean
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean mainAddUpdDepartmentPersonDegree( Vector vecDepartmentPersonDegree
            )  throws CoeusException,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        if ((vecDepartmentPersonDegree != null) && (vecDepartmentPersonDegree.size() > 0)){
            int atLength = vecDepartmentPersonDegree.size();
            for(int rowIndex=0; rowIndex<atLength; rowIndex++){
                DepartmentPerDegreeFormBean departmentPerDegreeFormBean =
                        (DepartmentPerDegreeFormBean)vecDepartmentPersonDegree.elementAt(rowIndex);
                procedures.add(addUpdDepartmentPersonDegree(departmentPerDegreeFormBean));
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
     *  Method used to update/insert all the details of a Departement person Degree.
     *  <li>To fetch the data, it uses DW_UPDATE_PS_DEGREE procedure.
     *
     *  @param DepartmentPerDegreeFormBean this bean contains data for
     *  insert/modifying the department person degree details.
     *  @return boolean true for successful insert/modify
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
    public ProcReqParameter addUpdDepartmentPersonDegree( DepartmentPerDegreeFormBean
            departmentPerDegreeFormBean)  throws CoeusException ,DBException{
//        boolean success = false;
//        Vector procedures = new Vector(5,3);
        Vector paramPersonDegree= new Vector();
        
        paramPersonDegree.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getPersonId()));
        paramPersonDegree.addElement(new Parameter("DEGREE_CODE",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getDegreeCode()));
        paramPersonDegree.addElement(new Parameter("GRADUATION_DATE",
                DBEngineConstants.TYPE_DATE,
                departmentPerDegreeFormBean.getGraduationDate()));
        paramPersonDegree.addElement(new Parameter("DEGREE",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getDegree()));
        paramPersonDegree.addElement(new Parameter("FIELD_OF_STUDY",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getFieldOfStudy()));
        paramPersonDegree.addElement(new Parameter("SPECIALIZATION",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getSpecialization()));
        paramPersonDegree.addElement(new Parameter("SCHOOL",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getSchool()));
        paramPersonDegree.addElement(new Parameter("SCHOOL_ID_CODE",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getSchoolIdCode()));
        paramPersonDegree.addElement(new Parameter("SCHOOL_ID",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getSchoolId()));
        paramPersonDegree.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramPersonDegree.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramPersonDegree.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getPersonId()));
        paramPersonDegree.addElement(new Parameter("AW_DEGREE_CODE",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getAwDegreeCode()));
        paramPersonDegree.addElement(new Parameter("AW_DEGREE",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getAwDegree()));
        paramPersonDegree.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                departmentPerDegreeFormBean.getUpdateTimestamp()));
        paramPersonDegree.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                departmentPerDegreeFormBean.getAcType()));
        
        StringBuffer sqlPersonDegree = new StringBuffer(
                "call DW_UPDATE_PS_DEGREE(");
        sqlPersonDegree.append(" <<PERSON_ID>> , ");
        sqlPersonDegree.append(" <<DEGREE_CODE>> , ");
        sqlPersonDegree.append(" <<GRADUATION_DATE>> , ");
        sqlPersonDegree.append(" <<DEGREE>> , ");
        sqlPersonDegree.append(" <<FIELD_OF_STUDY>> , ");
        sqlPersonDegree.append(" <<SPECIALIZATION>> , ");
        sqlPersonDegree.append(" <<SCHOOL>> , ");
        sqlPersonDegree.append(" <<SCHOOL_ID_CODE>> , ");
        sqlPersonDegree.append(" <<SCHOOL_ID>> , ");
        sqlPersonDegree.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlPersonDegree.append(" <<UPDATE_USER>> , ");
        sqlPersonDegree.append(" <<AW_PERSON_ID>> , ");
        sqlPersonDegree.append(" <<AW_DEGREE_CODE>> , ");
        sqlPersonDegree.append(" <<AW_DEGREE>> , ");
        sqlPersonDegree.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlPersonDegree.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procPersonDegree  = new ProcReqParameter();
        procPersonDegree.setDSN(DSN);
        procPersonDegree.setParameterInfo(paramPersonDegree);
        procPersonDegree.setSqlCommand(sqlPersonDegree.toString());
        
        return procPersonDegree;
    }
    
    /**
     * This method is used to insert Person Bio PDF Doc into database.
     * @param departmentBioPDFPersonFormBean DepartmentBioPDFPersonFormBean
     *
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public ProcReqParameter addPersonBioPDF(DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean)
    throws CoeusException, DBException {
//        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
//        boolean isUpdated = false;
        byte[] fileData = null;
//        Vector procedures = new Vector();
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        ProcReqParameter procPersonDegree = null;
        if(departmentBioPDFPersonFormBean!=null && departmentBioPDFPersonFormBean.getAcType()!=null){
            fileData = departmentBioPDFPersonFormBean.getFileBytes();
            if(fileData!=null){
                String statement = "";
                if(departmentBioPDFPersonFormBean.getAcType().equalsIgnoreCase("I")){
                    parameter.addElement(new Parameter("PERSON_ID",
                            DBEngineConstants.TYPE_STRING, departmentBioPDFPersonFormBean.getPersonId()));
                    parameter.addElement(new Parameter("BIO_NUMBER",
                            DBEngineConstants.TYPE_INT, ""+departmentBioPDFPersonFormBean.getBioNumber()));
                    parameter.addElement(new Parameter("FILE_NAME",
                            DBEngineConstants.TYPE_STRING, departmentBioPDFPersonFormBean.getFileName()));
                    parameter.addElement(new Parameter("MIME_TYPE",
                            DBEngineConstants.TYPE_STRING, departmentBioPDFPersonFormBean.getMimeType()));
                    parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
                            DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
                    parameter.addElement(new Parameter("UPDATE_USER",
                            DBEngineConstants.TYPE_STRING, userId));
                    parameter.addElement(new Parameter("BIO_PDF",
                            DBEngineConstants.TYPE_BLOB, departmentBioPDFPersonFormBean.getFileBytes()));
                    
                    statement = "INSERT INTO OSP$PERSON_BIO_PDF (PERSON_ID, BIO_NUMBER, FILE_NAME, MIME_TYPE ,UPDATE_TIMESTAMP, UPDATE_USER, BIO_PDF) "+
                            " VALUES( <<PERSON_ID>> , <<BIO_NUMBER>> , <<FILE_NAME>> ,<<MIME_TYPE>>, <<UPDATE_TIMESTAMP>> , <<UPDATE_USER>>, <<BIO_PDF>> )";
                    
                    procPersonDegree = new ProcReqParameter();
                    procPersonDegree.setDSN(DSN);
                    procPersonDegree.setParameterInfo(parameter);
                    procPersonDegree.setSqlCommand(statement.toString());
                }
            }
        }
        return procPersonDegree;
    }
    
    /**
     * This method is used to insert Person Bio PDF Doc into database.
     * @param departmentBioPDFPersonFormBean DepartmentBioPDFPersonFormBean
     *
     * @return boolean true if the insertion is success, else false.
     * @exception CoeusException raised if dbEngine is Null.
     * @exception DBException raised during db transactional error.
     */
    public ProcReqParameter addPersonBioSource(DepartmentBioSourceFormBean departmentBioSourceFormBean)
    throws CoeusException, DBException {
//        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
//        boolean isUpdated = false;
        byte[] fileData = null;
//        Vector procedures = new Vector();
        ProcReqParameter procPersonDegree = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        if(departmentBioSourceFormBean!=null && departmentBioSourceFormBean.getAcType()!=null){
            fileData = departmentBioSourceFormBean.getFileBytes();
            if(fileData!=null){
                String statement = "";
                if(departmentBioSourceFormBean.getAcType().equalsIgnoreCase("I")){
                    parameter.addElement(new Parameter("PERSON_ID",
                            DBEngineConstants.TYPE_STRING, departmentBioSourceFormBean.getPersonId()));
                    parameter.addElement(new Parameter("BIO_NUMBER",
                            DBEngineConstants.TYPE_INT, ""+departmentBioSourceFormBean.getBioNumber()));
                    parameter.addElement(new Parameter("FILE_NAME",
                            DBEngineConstants.TYPE_STRING, departmentBioSourceFormBean.getFileName()));
                    parameter.addElement(new Parameter("SOURCE_EDITOR",
                            DBEngineConstants.TYPE_STRING, departmentBioSourceFormBean.getSourceEditor()));
                    parameter.addElement(new Parameter("INPUT_TYPE",
                            DBEngineConstants.TYPE_STRING, ""+departmentBioSourceFormBean.getInputType()));
                    parameter.addElement(new Parameter("PLATFORM_TYPE",
                            DBEngineConstants.TYPE_STRING, ""+departmentBioSourceFormBean.getPlatformType()));
                    parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
                            DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
                    parameter.addElement(new Parameter("UPDATE_USER",
                            DBEngineConstants.TYPE_STRING, userId));
                    parameter.addElement(new Parameter("BIO_SOURCE",
                            DBEngineConstants.TYPE_BLOB, departmentBioSourceFormBean.getFileBytes()));
                    
                    statement = "INSERT INTO OSP$PERSON_BIO_SOURCE (PERSON_ID, BIO_NUMBER, FILE_NAME, SOURCE_EDITOR, INPUT_TYPE, PLATFORM_TYPE, UPDATE_TIMESTAMP, UPDATE_USER, BIO_SOURCE ) "+
                            " VALUES( <<PERSON_ID>> , <<BIO_NUMBER>>, <<FILE_NAME>>, <<SOURCE_EDITOR>>, <<INPUT_TYPE>>, <<PLATFORM_TYPE>>, <<UPDATE_TIMESTAMP>>, <<UPDATE_USER>>, <<BIO_SOURCE>> )";
                    
                    procPersonDegree  = new ProcReqParameter();
                    procPersonDegree.setDSN(DSN);
                    procPersonDegree.setParameterInfo(parameter);
                    procPersonDegree.setSqlCommand(statement.toString());
                }
            }
        }
        return procPersonDegree;
    }
    
    /**
     *  Method used to update/delete all Proposal Narrative PDFs.
     *  To update the data, it uses DW_UPD_PROP_APPR_COMMENTS procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdatePersonBioPDF(DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean)
    throws CoeusException, DBException{
        
//        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        Vector vctInsertPDFProcs = new Vector(3,2);
        boolean success = false;
        if(departmentBioPDFPersonFormBean!=null && departmentBioPDFPersonFormBean.getAcType()!=null){
            if(departmentBioPDFPersonFormBean.getAcType().equalsIgnoreCase("I")){
                vctInsertPDFProcs.add(addPersonBioPDF(departmentBioPDFPersonFormBean));
            }else if(departmentBioPDFPersonFormBean.getAcType().equalsIgnoreCase("U")){
                //Get the data to be Updated
                byte[] fileData = departmentBioPDFPersonFormBean.getFileBytes();
                
                //Get data from server
                DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                departmentBioPDFPersonFormBean = departmentPersonTxnBean.getPersonBioPDF(departmentBioPDFPersonFormBean);
                
                //If Update delete and Insert
                departmentBioPDFPersonFormBean.setAcType("D");
                
                procedures.add(addUpdPersonBioPDF(departmentBioPDFPersonFormBean));
                //Now insert
                departmentBioPDFPersonFormBean.setAcType("I");
                departmentBioPDFPersonFormBean.setFileBytes(fileData);
                vctInsertPDFProcs.add(addPersonBioPDF(departmentBioPDFPersonFormBean));
//                isUpdate = addPersonBioPDF(departmentBioPDFPersonFormBean);
            }
            if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    //Begin a transaction
                    conn = dbEngine.beginTxn();
                    //Update other data
                    if(procedures!=null && procedures.size() > 0){
                        dbEngine.executeStoreProcs(procedures, conn);
                    }
                    //Insert PDF data
                    if(vctInsertPDFProcs!=null && vctInsertPDFProcs.size() > 0){
                        dbEngine.batchSQLUpdate(vctInsertPDFProcs, conn);
                    }
                    //End Txn
                    dbEngine.endTxn(conn);
                }catch(Exception ex){
                    dbEngine.rollback(conn);
                    throw new DBException(ex);
                }
                success = true;
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        return success;
    }
    
    /**
     *  Method used to update/delete all Person Bio Word Docs.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdatePersonBioSource(DepartmentBioSourceFormBean departmentBioSourceFormBean)
    throws CoeusException, DBException{
        
//        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        Vector vctInsertSourceProcs = new Vector(3,2);
        boolean success = false;
        if(departmentBioSourceFormBean!=null && departmentBioSourceFormBean.getAcType()!=null){
            if(departmentBioSourceFormBean.getAcType().equalsIgnoreCase("I")){
                vctInsertSourceProcs.add(addPersonBioSource(departmentBioSourceFormBean));
            }else if(departmentBioSourceFormBean.getAcType().equalsIgnoreCase("U")){
                byte[] fileData = departmentBioSourceFormBean.getFileBytes();
                //Get data from server
                DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                departmentBioSourceFormBean = departmentPersonTxnBean.getPersonBioSource(departmentBioSourceFormBean);
                
                //If Update delete and Insert
                departmentBioSourceFormBean.setAcType("D");
                
                procedures.add(addUpdPersonBioSource(departmentBioSourceFormBean));
                
                //Now insert
                departmentBioSourceFormBean.setAcType("I");
                departmentBioSourceFormBean.setFileBytes(fileData);
//                    isUpdate = addPersonBioSource(departmentBioSourceFormBean);
                vctInsertSourceProcs.add(addPersonBioSource(departmentBioSourceFormBean));
            }
            if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    //Begin a transaction
                    conn = dbEngine.beginTxn();
                    //Update other data
                    if(procedures!=null && procedures.size() > 0){
                        dbEngine.executeStoreProcs(procedures, conn);
                    }
                    //Insert PDF data
                    if(vctInsertSourceProcs!=null && vctInsertSourceProcs.size() > 0){
                        dbEngine.batchSQLUpdate(vctInsertSourceProcs, conn);
                    }
                    //End Txn
                    dbEngine.endTxn(conn);
                }catch(Exception ex){
                    dbEngine.rollback(conn);
                    throw new DBException(ex);
                }
                success = true;
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            
        }
        return success;
    }
    
    /**
     *  Method used to update/insert all the details of a Departement person Training.
     *  <li>To fetch the data, it uses DW_UPDATE_PERSON_TRAINING procedure.
     *
     *  @param DepartmentPersonTrainingBean this bean contains data for
     *  insert/modifying the department person training details.
     *  @return boolean true for successful insert/modify
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
    public boolean addUpdDepartmentPersonTraining( CoeusVector personTraining )
    throws CoeusException ,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        Vector param = new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if(personTraining!=null && personTraining.size() > 0){
            DepartmentPersonTrainingBean departmentPersonTrainingBean = null;
            DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
            departmentPersonTrainingBean = (DepartmentPersonTrainingBean)personTraining.elementAt(0);
            int maxTrainingNumber = departmentPersonTxnBean.getMaxPersonTrainingNumber(departmentPersonTrainingBean.getPersonId());
            
            for(int row = 0; row < personTraining.size(); row++){
                departmentPersonTrainingBean = (DepartmentPersonTrainingBean)personTraining.elementAt(row);
                if(departmentPersonTrainingBean!=null && departmentPersonTrainingBean.getAcType()!=null){
                    param = new Vector();
                    param.addElement(new Parameter("PERSON_ID",
                            DBEngineConstants.TYPE_STRING,
                            departmentPersonTrainingBean.getPersonId()));
                    //If insert mode assign next available training number
                    if(departmentPersonTrainingBean.getAcType().equalsIgnoreCase("I")){
                        maxTrainingNumber = maxTrainingNumber + 1;
                        param.addElement(new Parameter("TRAINING_NUMBER",
                                DBEngineConstants.TYPE_INT,
                                ""+maxTrainingNumber));
                    }else{
                        param.addElement(new Parameter("TRAINING_NUMBER",
                                DBEngineConstants.TYPE_INT,
                                ""+departmentPersonTrainingBean.getTrainingNumber()));
                    }
                    param.addElement(new Parameter("TRAINING_CODE",
                            DBEngineConstants.TYPE_INT,
                            ""+departmentPersonTrainingBean.getTrainingCode()));
                    param.addElement(new Parameter("DATE_REQUESTED",
                            DBEngineConstants.TYPE_DATE,
                            departmentPersonTrainingBean.getDateRequested()));
                    param.addElement(new Parameter("DATE_SUBMITTED",
                            DBEngineConstants.TYPE_DATE,
                            departmentPersonTrainingBean.getDateSubmitted()));
                    param.addElement(new Parameter("DATE_ACKNOWLEDGED",
                            DBEngineConstants.TYPE_DATE,
                            departmentPersonTrainingBean.getDateAcknowledged()));
                    param.addElement(new Parameter("FOLLOWUP_DATE",
                            DBEngineConstants.TYPE_DATE,
                            departmentPersonTrainingBean.getFollowUpDate()));
                    param.addElement(new Parameter("SCORE",
                            DBEngineConstants.TYPE_STRING,
                            departmentPersonTrainingBean.getScore()));
                    param.addElement(new Parameter("COMMENTS",
                            DBEngineConstants.TYPE_STRING,
                            departmentPersonTrainingBean.getComments()));
                    param.addElement(new Parameter("UPDATE_TIMESTAMP",
                            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
                    param.addElement(new Parameter("UPDATE_USER",
                            DBEngineConstants.TYPE_STRING, userId));
                    param.addElement(new Parameter("AW_PERSON_ID",
                            DBEngineConstants.TYPE_STRING,
                            departmentPersonTrainingBean.getPersonId()));
                    param.addElement(new Parameter("AW_TRAINING_NUMBER",
                            DBEngineConstants.TYPE_INT,
                            ""+departmentPersonTrainingBean.getAw_TrainingNumber()));
                    param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                            DBEngineConstants.TYPE_TIMESTAMP,
                            departmentPersonTrainingBean.getUpdateTimestamp()));
                    param.addElement(new Parameter("AC_TYPE",
                            DBEngineConstants.TYPE_STRING,
                            departmentPersonTrainingBean.getAcType()));
                    
                    StringBuffer sqlPersonDegree = new StringBuffer(
                            "call DW_UPDATE_PERSON_TRAINING(");
                    sqlPersonDegree.append(" <<PERSON_ID>> , ");
                    sqlPersonDegree.append(" <<TRAINING_NUMBER>> , ");
                    sqlPersonDegree.append(" <<TRAINING_CODE>> , ");
                    sqlPersonDegree.append(" <<DATE_REQUESTED>> , ");
                    sqlPersonDegree.append(" <<DATE_SUBMITTED>> , ");
                    sqlPersonDegree.append(" <<DATE_ACKNOWLEDGED>> , ");
                    sqlPersonDegree.append(" <<FOLLOWUP_DATE>> , ");
                    sqlPersonDegree.append(" <<SCORE>> , ");
                    sqlPersonDegree.append(" <<COMMENTS>> , ");
                    sqlPersonDegree.append(" <<UPDATE_TIMESTAMP>> , ");
                    sqlPersonDegree.append(" <<UPDATE_USER>> , ");
                    sqlPersonDegree.append(" <<AW_PERSON_ID>> , ");
                    sqlPersonDegree.append(" <<AW_TRAINING_NUMBER>> , ");
                    sqlPersonDegree.append(" <<AW_UPDATE_TIMESTAMP>> , ");
                    sqlPersonDegree.append(" <<AC_TYPE>> )");
                    
                    ProcReqParameter procReqParameter  = new ProcReqParameter();
                    procReqParameter.setDSN(DSN);
                    procReqParameter.setParameterInfo(param);
                    procReqParameter.setSqlCommand(sqlPersonDegree.toString());
                    
                    procedures.add(procReqParameter);
                }
            }
            if(dbEngine!=null){
                dbEngine.executeStoreProcs(procedures);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            success = true;
        }
        
        return success;
    }
    
    /**
     *  Method used to update/insert all the details of a Person .
     *  <li>To fetch the data, it uses DW_UPDATE_PERSON procedure.
     *
     *  @param DepartmentPersonFormBean this bean contains data for
     *  insert/modifying the department person training details.
     *  @return boolean true for successful insert/modify
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
    public boolean addUpdPerson( DepartmentPersonFormBean  departmentPersonFormBean )
    throws CoeusException ,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        Vector param = new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        if(departmentPersonFormBean!=null && departmentPersonFormBean.getAcType()!=null){
            param = new Vector();
            param.addElement(new Parameter("PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getPersonId()));
            param.addElement(new Parameter("SSN",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getSsn()));
            param.addElement(new Parameter("LAST_NAME",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getLastName()));
            param.addElement(new Parameter("FIRST_NAME",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getFirstName()));
            param.addElement(new Parameter("MIDDLE_NAME",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getMiddleName()));
            param.addElement(new Parameter("FULL_NAME",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getFullName()));
            param.addElement(new Parameter("PRIOR_NAME",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getPriorName()));
            param.addElement(new Parameter("USER_NAME",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getUserName()));
            param.addElement(new Parameter("EMAIL_ADDRESS",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getEmailAddress()));
            param.addElement(new Parameter("DATE_OF_BIRTH",
                    DBEngineConstants.TYPE_DATE,
                    departmentPersonFormBean.getDateOfBirth()));
            param.addElement(new Parameter("AGE",
                    DBEngineConstants.TYPE_INT,
                    ""+departmentPersonFormBean.getAge()));
            param.addElement(new Parameter("AGE_BY_FISCAL_YEAR",
                    DBEngineConstants.TYPE_INT,
                    ""+departmentPersonFormBean.getAgeByFiscalYear()));
            param.addElement(new Parameter("GENDER",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getGender()));
            param.addElement(new Parameter("RACE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getRace()));
            param.addElement(new Parameter("EDUCATION_LEVEL",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getEduLevel()));
            param.addElement(new Parameter("DEGREE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getDegree()));
            param.addElement(new Parameter("MAJOR",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getMajor()));
            param.addElement(new Parameter("IS_HANDICAPPED",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getHandicap() ? "Y" : "N"));
            param.addElement(new Parameter("HANDICAP_TYPE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getHandiCapType()));
            param.addElement(new Parameter("IS_VETERAN",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getVeteran() ? "Y" : "N"));
            param.addElement(new Parameter("VETERAN_TYPE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getVeteranType()));
            param.addElement(new Parameter("VISA_CODE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getVisaCode()));
            param.addElement(new Parameter("VISA_TYPE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getVisaType()));
            param.addElement(new Parameter("VISA_RENEWAL_DATE",
                    DBEngineConstants.TYPE_DATE,
                    departmentPersonFormBean.getVisaRenDate()));
            param.addElement(new Parameter("HAS_VISA",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getHasVisa() ? "Y" : "N"));
            param.addElement(new Parameter("OFFICE_LOCATION",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getOfficeLocation()));
            param.addElement(new Parameter("OFFICE_PHONE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getOfficePhone()));
            param.addElement(new Parameter("SECONDRY_OFFICE_LOCATION",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getSecOfficeLocation()));
            param.addElement(new Parameter("SECONDRY_OFFICE_PHONE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getSecOfficePhone()));
            param.addElement(new Parameter("SCHOOL",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getSchool()));
            param.addElement(new Parameter("YEAR_GRADUATED",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getYearGraduated()));
            param.addElement(new Parameter("DIRECTORY_DEPARTMENT",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getDirDept()));
            param.addElement(new Parameter("SALUTATION",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getSaltuation()));
            param.addElement(new Parameter("COUNTRY_OF_CITIZENSHIP",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getCountryCitizenship()));
            param.addElement(new Parameter("PRIMARY_TITLE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getPrimaryTitle()));
            param.addElement(new Parameter("DIRECTORY_TITLE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getDirTitle()));
            param.addElement(new Parameter("HOME_UNIT",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getHomeUnit()));
            param.addElement(new Parameter("IS_FACULTY",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getFaculty() ? "Y" : "N"));
            param.addElement(new Parameter("IS_GRADUATE_STUDENT_STAFF",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getGraduateStudentStaff() ? "Y" : "N"));
            param.addElement(new Parameter("IS_RESEARCH_STAFF",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getResearchStaff() ? "Y" : "N"));
            param.addElement(new Parameter("IS_SERVICE_STAFF",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getServiceStaff() ? "Y" : "N"));
            param.addElement(new Parameter("IS_SUPPORT_STAFF",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getSupportStaff() ? "Y" : "N"));
            param.addElement(new Parameter("IS_OTHER_ACCADEMIC_GROUP",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getOtherAcademicGroup() ? "Y" : "N"));
            param.addElement(new Parameter("IS_MEDICAL_STAFF",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getMedicalStaff() ? "Y" : "N"));
            param.addElement(new Parameter("VACATION_ACCURAL",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getVacationAccural() ? "Y" : "N"));
            param.addElement(new Parameter("IS_ON_SABBATICAL",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getOnSabbatical() ? "Y" : "N"));
            param.addElement(new Parameter("ID_PROVIDED",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getProvided()));
            param.addElement(new Parameter("ID_VERIFIED",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getVerified()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            param.addElement(new Parameter("AW_PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getAWPersonId()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                    departmentPersonFormBean.getUpdateTimestamp()));
            
            //Case #1602 Added for Person Enhancement - Contact Info: Start 1
            param.addElement(new Parameter("ADDRESS_LINE_1",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getAddress1()));
            param.addElement(new Parameter("ADDRESS_LINE_2",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getAddress2()));
            param.addElement(new Parameter("ADDRESS_LINE_3",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getAddress3()));
            param.addElement(new Parameter("CITY",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getCity()));
            param.addElement(new Parameter("COUNTY",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getCounty()));
            param.addElement(new Parameter("STATE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getState()));
            param.addElement(new Parameter("POSTAL_CODE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getPostalCode()));
            param.addElement(new Parameter("COUNTRY_CODE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getCountryCode()));
            param.addElement(new Parameter("FAX_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getFaxNumber()));
            param.addElement(new Parameter("PAGER_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getPagerNumber()));
            param.addElement(new Parameter("MOBILE_PHONE_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getMobilePhNumber()));
            param.addElement(new Parameter("ERA_COMMONS_USER_NAME",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getEraCommonsUsrName()));
            
            //Case #1602 Added for Person Enhancement - Contact Info: End 1
            
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
            //Added status column to denote the status of a person
            param.addElement(new Parameter("STATUS",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getStatus()));
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
            param.addElement(new Parameter("SALARY_ANNIVERSARY_DATE",
                DBEngineConstants.TYPE_DATE,
                departmentPersonFormBean.getSalaryAnniversaryDate()));
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
            
            param.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                    departmentPersonFormBean.getAcType()));
            
            //Case #1602 Commented for Person Enhancement - Contact Info: Start 2
            //Created new update Procedure
            /*StringBuffer sqlPersonDegree = new StringBuffer(
                                            "call DW_UPDATE_PERSON(");*/
            StringBuffer sqlPersonDegree = new StringBuffer(
                    "call UPDATE_PERSON(");
            //Case #1602 Person Enhancement - Contact Info: End 2
            
            sqlPersonDegree.append(" <<PERSON_ID>> , ");
            sqlPersonDegree.append(" <<SSN>> , ");
            sqlPersonDegree.append(" <<LAST_NAME>> , ");
            sqlPersonDegree.append(" <<FIRST_NAME>> , ");
            sqlPersonDegree.append(" <<MIDDLE_NAME>> , ");
            sqlPersonDegree.append(" <<FULL_NAME>> , ");
            sqlPersonDegree.append(" <<PRIOR_NAME>> , ");
            sqlPersonDegree.append(" <<USER_NAME>> , ");
            sqlPersonDegree.append(" <<EMAIL_ADDRESS>> , ");
            sqlPersonDegree.append(" <<DATE_OF_BIRTH>> , ");
            sqlPersonDegree.append(" <<AGE>> , ");
            sqlPersonDegree.append(" <<AGE_BY_FISCAL_YEAR>> , ");
            sqlPersonDegree.append(" <<GENDER>> , ");
            sqlPersonDegree.append(" <<RACE>> , ");
            sqlPersonDegree.append(" <<EDUCATION_LEVEL>> , ");
            sqlPersonDegree.append(" <<DEGREE>> , ");
            sqlPersonDegree.append(" <<MAJOR>> , ");
            sqlPersonDegree.append(" <<IS_HANDICAPPED>> , ");
            sqlPersonDegree.append(" <<HANDICAP_TYPE>> , ");
            sqlPersonDegree.append(" <<IS_VETERAN>> , ");
            sqlPersonDegree.append(" <<VETERAN_TYPE>> , ");
            sqlPersonDegree.append(" <<VISA_CODE>> , ");
            sqlPersonDegree.append(" <<VISA_TYPE>> , ");
            sqlPersonDegree.append(" <<VISA_RENEWAL_DATE>> , ");
            sqlPersonDegree.append(" <<HAS_VISA>> , ");
            sqlPersonDegree.append(" <<OFFICE_LOCATION>> , ");
            sqlPersonDegree.append(" <<OFFICE_PHONE>> , ");
            sqlPersonDegree.append(" <<SECONDRY_OFFICE_LOCATION>> , ");
            sqlPersonDegree.append(" <<SECONDRY_OFFICE_PHONE>> , ");
            sqlPersonDegree.append(" <<SCHOOL>> , ");
            sqlPersonDegree.append(" <<YEAR_GRADUATED>> , ");
            sqlPersonDegree.append(" <<DIRECTORY_DEPARTMENT>> , ");
            sqlPersonDegree.append(" <<SALUTATION>> , ");
            sqlPersonDegree.append(" <<COUNTRY_OF_CITIZENSHIP>> , ");
            sqlPersonDegree.append(" <<PRIMARY_TITLE>> , ");
            sqlPersonDegree.append(" <<DIRECTORY_TITLE>> , ");
            sqlPersonDegree.append(" <<HOME_UNIT>> , ");
            sqlPersonDegree.append(" <<IS_FACULTY>> , ");
            sqlPersonDegree.append(" <<IS_GRADUATE_STUDENT_STAFF>> , ");
            sqlPersonDegree.append(" <<IS_RESEARCH_STAFF>> , ");
            sqlPersonDegree.append(" <<IS_SERVICE_STAFF>> , ");
            sqlPersonDegree.append(" <<IS_SUPPORT_STAFF>> , ");
            sqlPersonDegree.append(" <<IS_OTHER_ACCADEMIC_GROUP>> , ");
            sqlPersonDegree.append(" <<IS_MEDICAL_STAFF>> , ");
            sqlPersonDegree.append(" <<VACATION_ACCURAL>> , ");
            sqlPersonDegree.append(" <<IS_ON_SABBATICAL>> , ");
            sqlPersonDegree.append(" <<ID_PROVIDED>> , ");
            sqlPersonDegree.append(" <<ID_VERIFIED>> , ");
            sqlPersonDegree.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlPersonDegree.append(" <<UPDATE_USER>> , ");
            
            //Case #1602 Added for Person Enhancement - Contact Info: Start 3
            sqlPersonDegree.append(" <<ADDRESS_LINE_1>> , ");
            sqlPersonDegree.append(" <<ADDRESS_LINE_2>> , ");
            sqlPersonDegree.append(" <<ADDRESS_LINE_3>> , ");
            sqlPersonDegree.append(" <<CITY>> , ");
            sqlPersonDegree.append(" <<COUNTY>> , ");
            sqlPersonDegree.append(" <<STATE>> , ");
            sqlPersonDegree.append(" <<POSTAL_CODE>> , ");
            sqlPersonDegree.append(" <<COUNTRY_CODE>> , ");
            sqlPersonDegree.append(" <<FAX_NUMBER>> , ");
            sqlPersonDegree.append(" <<PAGER_NUMBER>> , ");
            sqlPersonDegree.append(" <<MOBILE_PHONE_NUMBER>> , ");
            sqlPersonDegree.append(" <<ERA_COMMONS_USER_NAME>> , ");
            //Case #1602 Added for Person Enhancement - Contact Info: End 3
            
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
            //Added status column to denote the active status of a person
            sqlPersonDegree.append(" <<STATUS>> , ");
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
            sqlPersonDegree.append(" <<SALARY_ANNIVERSARY_DATE>> , ");
            sqlPersonDegree.append(" <<AW_PERSON_ID>> , ");
            sqlPersonDegree.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sqlPersonDegree.append(" <<AC_TYPE>> )");
            
            ProcReqParameter procReqParameter  = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sqlPersonDegree.toString());
            
            procedures.add(procReqParameter);
            
            if(dbEngine!=null){
                dbEngine.executeStoreProcs(procedures);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        success = true;
        
        return success;
    }
    
    
    
    public static void main(String args[]){
        try{
            boolean flag = false;
//            Vector vct = new Vector();
            DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
            DepartmentPersonUpdateTxnBean departmentPersonUpdateTxnBean =
                    new DepartmentPersonUpdateTxnBean("COEUS");
            DepartmentPersonFormBean  departmentPersonFormBean = departmentPersonTxnBean.getPersonDetails("987654321");
            departmentPersonFormBean.setAcType("U");
            departmentPersonFormBean.setAWPersonId(departmentPersonFormBean.getPersonId());
            boolean success = departmentPersonUpdateTxnBean.addUpdPerson(departmentPersonFormBean);
            System.out.println("Updated :"+success);
         /*
         String path = "C:\\test1.doc";
          
         java.io.File file = new java.io.File(path);
         java.io.FileInputStream fis = new java.io.FileInputStream(file);
         byte fileBytes[] = new byte[(int)file.length()];
         fis.read(fileBytes);
         fis.close();
         DepartmentBioSourceFormBean departmentBioSourceFormBean = new DepartmentBioSourceFormBean();
          
         departmentBioSourceFormBean.setPersonId("900020765");
         departmentBioSourceFormBean.setBioNumber(2);
          
         departmentBioSourceFormBean = departmentPersonTxnBean.getPersonBioSource(departmentBioSourceFormBean);
          
         departmentBioSourceFormBean.setFileBytes(fileBytes);
         departmentBioSourceFormBean.setAcType("U");
         boolean isUpdate = departmentPersonUpdateTxnBean.addUpdatePersonBioSource(departmentBioSourceFormBean);
          */
            
            System.out.println("Update : "+flag);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
