/*
 * @(#)UserMaintDataTxnBean.java 1.0 4/14/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 09-AUG-2007
 * by Leena
 */
package edu.mit.coeus.user.bean;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import edu.mit.coeus.bean.RoleInfoBean;
import edu.mit.coeus.bean.RoleRightInfoBean;
import edu.mit.coeus.bean.UnitUserRolesMaintenanceFormBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.UserRolesInfoBean;
//import java.sql.SQLException;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
//import edu.mit.coeus.propdev.bean.ProposalUnitFormBean;
import edu.mit.is.service.authorization.Authorization;
import edu.mit.is.service.authorization.AuthorizationsIterator;
import edu.mit.is.service.authorization.Factory;
import edu.mit.is.service.authorization.Function;
import edu.mit.is.service.authorization.FunctionType;
import edu.mit.is.service.authorization.Person;
import edu.mit.is.service.authorization.Qualifier;

/**
 * This class is used to get user maintenance information.
 *
 * @version 1.0 July 10, 2003, 10:54 AM
 * @author Prasanna Kumar
 *
 */
public class UserMaintDataTxnBean {
	private DBEngineImpl dbEngine;

	/** Creates new UserDetailsBean */
	public UserMaintDataTxnBean() {
		dbEngine = new DBEngineImpl();
	}

	/**
	 * This method has been written to change the password for the given user
	 * Here userName and password are the inputs
	 * 
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public boolean changePassword(String userName, String password) throws CoeusException, DBException {

		boolean pwdCheck;

		Vector param = new Vector();
		Vector result = new Vector();
		int number = 0;
		param.add(new Parameter("USER_ID", "String", userName));
		param.add(new Parameter("PASSWORD", "String", password));
		// calling function
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER PWD_STATUS>> = " + "call FN_CHG_PASSWORD(  << USER_ID >>, << PASSWORD >> ) } ",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			number = Integer.parseInt(rowParameter.get("PWD_STATUS").toString());
		}
		if (number == 1) {
			pwdCheck = true;
		} else {
			pwdCheck = false;
		}
		return pwdCheck;
	}

	/**
	 * Check if the given unit number is valid To fetch the data, it uses the
	 * function, fn_is_valid_unit_number.
	 * 
	 * @param String
	 *            containing unitNumber
	 * @return boolean value, true if unit number is valid
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public boolean checkUnitIsValid(String unitNumber) throws CoeusException, DBException {
		boolean validUnit = false;
		Vector param = new Vector();
		Vector result = new Vector();
		int exist = 0;
		param.add(new Parameter("AS_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, unitNumber));

		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER ll_count>> = " + " call fn_is_valid_unit_number(<< AS_UNIT_NUMBER >>) }", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			exist = Integer.parseInt(rowParameter.get("ll_count").toString());
		}
		if (exist > 0) {
			validUnit = true;
		}
		return validUnit;
	}
	// Added for COEUSQA-1692_User Access - Maintenance_end

	/**
	 * This method has been written to check whether user has logged in first
	 * time.
	 * 
	 * @param userId
	 *            is the input
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public boolean firstTimeLoginCheck(String userId) throws CoeusException, DBException {
		boolean loginCheck;
		Vector param = new Vector();
		Vector result = new Vector();
		int number = 0;
		param.add(new Parameter("USER_ID", "String", userId.toUpperCase()));
		// calling function
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER LOGIN_CHECK>> = " + "call FN_FIRST_TIME_LOGIN_CHECK(  << USER_ID >> ) } ", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			number = Integer.parseInt(rowParameter.get("LOGIN_CHECK").toString());
		}
		if (number == 1) {
			loginCheck = true;
		} else {
			loginCheck = false;
		}
		return loginCheck;
	}

	// Added for Coeus 4.3 : PT ID 2232 - Custom Roles - start
	/**
	 * Gets all the rights
	 * 
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @return Vector of RoleRightInfoBean instances
	 */
	public Vector getAllRights() throws DBException, CoeusException {
		Vector result = null;
		Vector vecRights = null;
		Vector param = new Vector();
		HashMap rightsHashMap = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_ALL_RIGHTS( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		if (result != null) {
			vecRights = new Vector();
			RoleRightInfoBean roleRightInfoBean = null;
			for (int rowIndex = 0; rowIndex < result.size(); rowIndex++) {
				rightsHashMap = (HashMap) result.elementAt(rowIndex);
				roleRightInfoBean = new RoleRightInfoBean();
				roleRightInfoBean.setRightId(rightsHashMap.get("RIGHT_ID").toString());
				roleRightInfoBean.setDescription(rightsHashMap.get("DESCRIPTION").toString());
				roleRightInfoBean
						.setDescendFlag(rightsHashMap.get("DESCEND_FLAG").toString().equals("Y") ? true : false);
				roleRightInfoBean.setRightType(((String) rightsHashMap.get("RIGHT_TYPE")).trim().charAt(0));
				vecRights.add(roleRightInfoBean);
			}
		}
		return vecRights;
	}

	/**
	 * This method used to get all Roles for the given User Id
	 * <li>To fetch the data, it uses Authorization API method
	 * getAuthorizations().
	 *
	 * @return Vector of unit number and unit name
	 * @param userId
	 *            User Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API
	 */
	public Vector getAllRolesForUser(String userId)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);

		Function function = null;
		Qualifier qualifier = null;
		FunctionType functionType = null;
		Person person = (Person) myAuthFactory.newPerson(userId);
		RoleInfoBean roleInfoBean = new RoleInfoBean();
		Person authperson = (Person) myAuthFactory.getUserInfo(person);

		AuthorizationsIterator myIterator;
		myIterator = (AuthorizationsIterator) myAuthFactory.getAuthorizations(person, function, qualifier, false,
				false);

		Authorization authorization;

		Vector result = new Vector();
		if (myIterator != null) {
			while (myIterator.hasNext()) {
				roleInfoBean = new RoleInfoBean();
				authorization = (Authorization) myIterator.next();
				properties = authorization.getProperties();

				qualifier = (Qualifier) properties.get("QUALIFIER");
				function = (Function) properties.get("FUNCTION");
				functionType = (FunctionType) function.getFunctionType();
				roleInfoBean.setRoleId(Integer.parseInt((String) function.getKey()));
				roleInfoBean.setRoleName(function.getDescription());
				roleInfoBean.setUnitNumber((String) qualifier.getKey());
				roleInfoBean.setUnitName(qualifier.getDescription());
				roleInfoBean.setRoleType(functionType.getDescription().charAt(0));
				roleInfoBean.setStatus(((String) properties.get("STATUS")).charAt(0));
				result.add(roleInfoBean);
			}
		}
		return result;
	}

	/**
	 * This method used to get all Units in which the User has rights To fetch
	 * the data, it uses the Procedure get_all_units_for_user.
	 *
	 * @return Vector containing UnitUserRolesMaintenanceFormBean
	 * @param userId
	 *            User Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getAllUnitsForUser(String userId) throws DBException, CoeusException {

		Vector param = new Vector();
		Vector result = new Vector();
		param.addElement(new Parameter("USER_ID", DBEngineConstants.TYPE_STRING, userId));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call get_all_units_for_user ( <<USER_ID>>, <<OUT RESULTSET rset>> ) ", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector vecUserUnits = new Vector();
		if (listSize > 0) {
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				UnitUserRolesMaintenanceFormBean unitUserRolesMaintenanceFormBean = new UnitUserRolesMaintenanceFormBean();
				HashMap rolesRow = (HashMap) result.elementAt(rowIndex);
				unitUserRolesMaintenanceFormBean.setUnitNumber((String) rolesRow.get("UNIT_NUMBER"));
				unitUserRolesMaintenanceFormBean.setUnitName(rolesRow.get("UNIT_NAME").toString());
				vecUserUnits.add(unitUserRolesMaintenanceFormBean);
			}
		}
		return vecUserUnits;
	}

	/**
	 * This method used to get merge Roles and User Roles for the given Unit
	 * Number.
	 *
	 * @return Vector of UserRolesInfoBean
	 * @param unitNumber
	 *            to get all User Roles
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API
	 */
	public Vector getAllUserRolesForUnit(String unitNumber)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {

		Vector userRoles = new Vector();
		Vector roles = null;
		Vector users = null;

		RoleInfoBean roleInfoBean;
		UserRolesInfoBean userRolesInfoBean;
		// UserRolesInfoBean userRolesChildren ;

		roles = getRolesForUnit(unitNumber);
		int intRoleId = 0;
		Hashtable hshUsers = getUserRolesForUnit(unitNumber);

		if (roles != null && roles.size() > 0) {
			for (int rolesRowIndex = 0; rolesRowIndex < roles.size(); rolesRowIndex++) {
				userRolesInfoBean = new UserRolesInfoBean(); // Roles
				roleInfoBean = (RoleInfoBean) roles.elementAt(rolesRowIndex);
				intRoleId = roleInfoBean.getRoleId();
				if (hshUsers.containsKey(Integer.toString(intRoleId))) {
					users = (Vector) hshUsers.get(Integer.toString(intRoleId));
					userRolesInfoBean.setIsRole(true);
					userRolesInfoBean.setHasChildren(true);
					userRolesInfoBean.setRoleBean(roleInfoBean);
					userRolesInfoBean.setUsers(users);
					// COEUSDEV-221 User Maintenance window does not save in one
					// try - Start
					if (users != null && users.size() > 0) {
						int size = users.size();
						for (int index = 0; index < size; index++) {
							UserRolesInfoBean userRolesinfBean = (UserRolesInfoBean) users.get(index);
							userRolesinfBean.setRoleBean(roleInfoBean);
						}
					}
					// COEUSDEV-221 User Maintenance window does not save in one
					// try - End
					userRoles.addElement(userRolesInfoBean); // Add roles to
																// Vector
					/*
					 * for(int usersRowIndex =
					 * 0;usersRowIndex<users.size();usersRowIndex++){
					 * userRolesChildren =
					 * (UserRolesInfoBean)users.elementAt(usersRowIndex);
					 * userRoles.addElement(userRolesChildren); //Add roles to
					 * Vector }
					 */
				} else {
					userRolesInfoBean.setRoleBean(roleInfoBean);
					userRolesInfoBean.setIsRole(true);
					userRolesInfoBean.setHasChildren(false);
					userRoles.addElement(userRolesInfoBean); // Add roles to
																// Vector
				}
			}
		}
		return userRoles;
	}

	/**
	 * This method used to get all Roles assigned for the given User under the
	 * given Unit Number
	 * <li>To fetch the data, it uses the Procedure GET_ASSIGNED_USER_ROLES.
	 *
	 * @return Vector containing RoleInfoBean
	 * @param userId
	 *            User Id
	 * @param unitNumber
	 *            Unit Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getAssignedRoles(String userId, String unitNumber) throws DBException, CoeusException {
		Vector param = new Vector();
		Vector result = new Vector();
		RoleInfoBean roleInfoBean = null;
		param.addElement(new Parameter("USER_ID", "String", userId));
		param.addElement(new Parameter("UNIT_NUMBER", "String", unitNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_ASSIGNED_USER_ROLES ( <<USER_ID>> , <<UNIT_NUMBER>> , <<OUT RESULTSET rset>> ) ", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector assignedRoles = new Vector();
		if (listSize > 0) {
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				roleInfoBean = new RoleInfoBean();
				HashMap rolesRow = (HashMap) result.elementAt(rowIndex);
				roleInfoBean.setRoleId(Integer.parseInt(rolesRow.get("ROLE_ID").toString()));
				roleInfoBean.setRoleName((String) rolesRow.get("ROLE_NAME"));
				roleInfoBean.setRoleType(((String) rolesRow.get("ROLE_TYPE")).charAt(0));
				roleInfoBean.setUnitNumber((String) rolesRow.get("UNIT_NUMBER"));
				roleInfoBean.setDescend(rolesRow.get("DESCEND_FLAG") == null ? false
						: (rolesRow.get("DESCEND_FLAG").toString().equalsIgnoreCase("Y") ? true : false));
				roleInfoBean.setStatus(((String) rolesRow.get("STATUS_FLAG")).charAt(0));
				roleInfoBean.setUpdateUser((String) rolesRow.get("UPDATE_USER"));
				roleInfoBean.setUpdateTimestamp((java.sql.Timestamp) rolesRow.get("UPDATE_TIMESTAMP"));

				assignedRoles.addElement(roleInfoBean);
			}
		}
		return assignedRoles;
	}

	/**
	 * This method used to get top level unit number for the dept unit number
	 * <li>To fetch the data, it uses the procedure FN_GET_CAMPUS_FOR_DEPT.
	 *
	 * @return String of Unit Number
	 * @param unitNumber
	 *            Unit Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available. Bug Fix
	 *                #1699
	 */
	public String getCampusForDept(String unitNumber) throws DBException, CoeusException {

		String topLevelUnitNumber = "";
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AW_UNIT_NUMBER", "String", unitNumber));
		// calling stored function
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT STRING UNIT_NUMBER>> = " + "call FN_GET_CAMPUS_FOR_DEPT( << AW_UNIT_NUMBER >> ) } ",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			topLevelUnitNumber = (String) rowParameter.get("UNIT_NUMBER");
		}
		return topLevelUnitNumber;
	}

	/**
	 * This method used to get Roles for the given Unit Number
	 * <li>To fetch the data, it uses the Authorization API method
	 * getAuthorizations().
	 *
	 * @return Hashtable containing Role Id and UserInfoBean
	 * @param delegatedTo
	 *            Delegeted To
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getDelegatedTo(String delegatedTo) throws DBException, CoeusException {
		Vector param = new Vector();
		Vector result = new Vector();
		UserDelegationsBean userDelegationsBean = null;
		param.addElement(new Parameter("DELEGATED_BY", "String", delegatedTo));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_DELEGATED_TO ( <<DELEGATED_BY>> , <<OUT RESULTSET rset>> ) ", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector delegations = null;
		// String strRoleId = null;
		if (listSize > 0) {
			delegations = new Vector(3, 2);
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				userDelegationsBean = new UserDelegationsBean();
				HashMap delegationsRow = (HashMap) result.elementAt(rowIndex);
				userDelegationsBean.setDelegatedBy((String) delegationsRow.get("DELEGATED_BY"));
				userDelegationsBean.setDelegatedTo((String) delegationsRow.get("DELEGATED_TO"));
				userDelegationsBean.setEffectiveDate(delegationsRow.get("EFFECTIVE_DATE") == null ? null
						: new Date(((Timestamp) delegationsRow.get("EFFECTIVE_DATE")).getTime()));
				userDelegationsBean.setEndDate(delegationsRow.get("END_DATE") == null ? null
						: new Date(((Timestamp) delegationsRow.get("END_DATE")).getTime()));
				userDelegationsBean.setStatus(((String) delegationsRow.get("STATUS")).charAt(0));
				// Added for Case#3682 - Enhancements related to Delegations -
				// Start
				userDelegationsBean.setDelegationID(Integer.parseInt(delegationsRow.get("DELEGATION_ID").toString()));
				// Added for Case#3682 - Enhancements related to Delegations -
				// End
				userDelegationsBean.setUpdateUser((String) delegationsRow.get("UPDATE_USER"));
				userDelegationsBean.setUpdateTimestamp((java.sql.Timestamp) delegationsRow.get("UPDATE_TIMESTAMP"));
				delegations.addElement(userDelegationsBean);
			}
		}
		return delegations;
	}

	/**
	 * This method used to get Roles for the given Unit Number
	 * <li>To fetch the data, it uses the Authorization API method
	 * getAuthorizations().
	 *
	 * @return Hashtable containing Role Id and UserInfoBean
	 * @param delegatedBy
	 *            Delegated By
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getDelegations(String delegatedBy) throws DBException, CoeusException {
		Vector param = new Vector();
		Vector result = new Vector();
		UserDelegationsBean userDelegationsBean = null;
		param.addElement(new Parameter("DELEGATED_BY", "String", delegatedBy));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_DELEGATIONS ( <<DELEGATED_BY>> , <<OUT RESULTSET rset>> ) ", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector delegations = null;
		// String strRoleId = null;
		if (listSize > 0) {
			delegations = new Vector(3, 2);
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				userDelegationsBean = new UserDelegationsBean();
				HashMap delegationsRow = (HashMap) result.elementAt(rowIndex);
				userDelegationsBean.setUserName((String) delegationsRow.get("USER_NAME"));
				userDelegationsBean.setDelegatedBy((String) delegationsRow.get("DELEGATED_BY"));
				userDelegationsBean.setDelegatedTo((String) delegationsRow.get("DELEGATED_TO"));
				userDelegationsBean.setEffectiveDate(delegationsRow.get("EFFECTIVE_DATE") == null ? null
						: new Date(((Timestamp) delegationsRow.get("EFFECTIVE_DATE")).getTime()));
				userDelegationsBean.setEndDate(delegationsRow.get("END_DATE") == null ? null
						: new Date(((Timestamp) delegationsRow.get("END_DATE")).getTime()));
				userDelegationsBean.setStatus(((String) delegationsRow.get("STATUS")).charAt(0));
				// Added for Case#3682 - Enhancements related to Delegations -
				// Start
				userDelegationsBean.setDelegationID(Integer.parseInt(delegationsRow.get("DELEGATION_ID").toString()));
				// Added for Case#3682 - Enhancements related to Delegations -
				// End
				userDelegationsBean.setUpdateUser((String) delegationsRow.get("UPDATE_USER"));
				userDelegationsBean.setUpdateTimestamp((java.sql.Timestamp) delegationsRow.get("UPDATE_TIMESTAMP"));
				delegations.addElement(userDelegationsBean);
			}
		}
		return delegations;
	}

	/**
	 * This method used to check is it ok to Delegate
	 * <li>To fetch the data, it uses the function FN_OK_TO_DELEGATE.
	 *
	 * @return integer indicating whether it can be Delegated or not
	 * @param userDelegationsBean
	 *            UserDelegationsBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public int getIsOkToDelegate(UserDelegationsBean userDelegationsBean) throws DBException, CoeusException {

		int number = 0;
		// boolean blnExist = false;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("DELEGATED_BY", DBEngineConstants.TYPE_STRING, userDelegationsBean.getDelegatedBy()));
		param.add(new Parameter("DELEGATED_TO", DBEngineConstants.TYPE_STRING, userDelegationsBean.getDelegatedTo()));
		param.add(new Parameter("EFFECTIVE_DATE", DBEngineConstants.TYPE_DATE, userDelegationsBean.getEffectiveDate()));

		// calling stored function
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER NUMBER>> = "
							+ "call FN_OK_TO_DELEGATE( << DELEGATED_BY >>, <<DELEGATED_TO>>, <<EFFECTIVE_DATE>> ) } ",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			number = Integer.parseInt(rowParameter.get("NUMBER").toString());
		}
		return number;
	}

	/**
	 * This method used to check whether the given User exist in any Unit
	 * <li>To fetch the data, it uses the function FN_USER_EXISTS.
	 *
	 * @return int Count of Unit Numbers where the given User present
	 * @param userId
	 *            User Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public boolean getIsUserExist(String userId) throws DBException, CoeusException {

		int number = 0;
		boolean blnExist = false;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("USER_ID", "String", userId));
		// calling stored function
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER NUMBER>> = " + "call FN_USER_EXISTS(  << USER_ID >> ) } ", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			number = Integer.parseInt(rowParameter.get("NUMBER").toString());
		}
		if (number > 0) {
			blnExist = true;
		}
		return blnExist;
	}

	/**
	 * Gets the next sequence for the role id
	 * 
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @return int next role id.
	 */
	public int getNextRoleId() throws DBException, CoeusException {
		int roleId = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		// calling stored function
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus", "{ <<OUT INTEGER ROLE_ID>> = " + "call FN_GET_ROLE_ID() } ",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			roleId = Integer.parseInt(rowParameter.get("ROLE_ID").toString());
		}
		return roleId;
	}

	/**
	 * This method used to get all not assigned Roles for the given User under
	 * the given Unit Number
	 * <li>To fetch the data, it uses the Procedure GET_NOT_ASSIGNED_USER_ROLES.
	 *
	 * @return Vector containing RoleInfoBean
	 * @param userId
	 *            User Id
	 * @param unitNumber
	 *            Unit Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getNotAssignedRoles(String userId, String unitNumber) throws DBException, CoeusException {
		Vector param = new Vector();
		Vector result = new Vector();
		RoleInfoBean roleInfoBean = null;
		param.addElement(new Parameter("USER_ID", "String", userId));
		param.addElement(new Parameter("UNIT_NUMBER", "String", unitNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_NOT_ASSIGNED_USER_ROLES ( <<USER_ID>> , <<UNIT_NUMBER>> , <<OUT RESULTSET rset>> ) ",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector notAssignedRoles = new Vector();
		if (listSize > 0) {
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				roleInfoBean = new RoleInfoBean();
				HashMap rolesRow = (HashMap) result.elementAt(rowIndex);
				roleInfoBean.setUserId(userId);
				roleInfoBean.setRoleId(Integer.parseInt(rolesRow.get("ROLE_ID").toString()));
				roleInfoBean.setRoleName((String) rolesRow.get("ROLE_NAME"));
				roleInfoBean.setRoleType(((String) rolesRow.get("ROLE_TYPE")).charAt(0));
				roleInfoBean.setUnitNumber((String) rolesRow.get("UNIT_NUMBER"));
				roleInfoBean.setDescend(rolesRow.get("DESCEND_FLAG") == null ? false
						: (rolesRow.get("DESCEND_FLAG").toString().equalsIgnoreCase("Y") ? true : false));
				roleInfoBean.setStatus(((String) rolesRow.get("STATUS_FLAG")).charAt(0));
				roleInfoBean.setUpdateUser((String) rolesRow.get("UPDATE_USER"));
				roleInfoBean.setUpdateTimestamp((java.sql.Timestamp) rolesRow.get("UPDATE_TIMESTAMP"));

				notAssignedRoles.addElement(roleInfoBean);
			}
		}
		return notAssignedRoles;
	}

	/**
	 * This method used to get Person Info for the given Person Id
	 * <li>To fetch the data, it uses the Procedure GET_PERSON_INFO.
	 *
	 * @return Vector Person Info
	 * @param personId
	 *            Person Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getPersonInfo(String personId) throws DBException, CoeusException {
		Vector param = new Vector();
		Vector result = new Vector();
		param.addElement(new Parameter("PERSON_ID", "String", personId));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_PERSON_INFO ( <<PERSON_ID>> , "
							+ "<< OUT STRING PERSON_NAME >> ,  << OUT STRING TITLE >> , "
							+ "<< OUT STRING FLAG >> , << OUT STRING UNITHOME >> ) ",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		Vector vctPersonInfo = new Vector();
		if (!result.isEmpty()) {
			HashMap personInfo = (HashMap) result.elementAt(0);
			vctPersonInfo.addElement(personInfo.get("PERSON_NAME"));
			vctPersonInfo.addElement(personInfo.get("TITLE"));
			vctPersonInfo.addElement(personInfo.get("FLAG"));
			vctPersonInfo.addElement(personInfo.get("UNITHOME"));
			vctPersonInfo.addElement(personInfo.get("STATUS"));
		}
		return vctPersonInfo;
	}

	/**
	 * This method is used to get User Preferences for the given User.
	 * 
	 * @param userId
	 *            String User Id
	 * @return Vector of UserPreferencesBean.
	 * @exception CoeusException
	 *                raised if dbEngine is null.
	 * @exception DBException
	 *                raised from the server side.
	 */
	public Vector getPreferenceVariablesNotAssigned(String userId) throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		UserPreferencesBean userPreferencesBean = null;
		param.addElement(new Parameter("USER_ID", "String", userId));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_PREF_VARS_NOT_ASSIGNED ( <<USER_ID>> , <<OUT RESULTSET rset>> ) ", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector userPreferences = null;
		if (listSize > 0) {
			userPreferences = new Vector(3, 2);
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				userPreferencesBean = new UserPreferencesBean();
				HashMap preferencesRow = (HashMap) result.elementAt(rowIndex);
				userPreferencesBean.setUserId((String) preferencesRow.get("USER_ID"));
				userPreferencesBean.setVarValue((String) preferencesRow.get("VALUE"));
				userPreferencesBean.setUpdateUser((String) preferencesRow.get("UPDATE_USER"));
				userPreferencesBean.setUpdateTimestamp((java.sql.Timestamp) preferencesRow.get("UPDATE_TIMESTAMP"));
				userPreferencesBean.setVariableName((String) preferencesRow.get("VARIABLE_NAME"));
				userPreferencesBean.setVarDescription((String) preferencesRow.get("DESCRIPTION"));
				userPreferencesBean.setVarDataType((String) preferencesRow.get("DATA_TYPE"));
				userPreferencesBean.setVarDefaultValue((String) preferencesRow.get("DEFAULT_VALUE"));
				userPreferences.addElement(userPreferencesBean);
			}
		}
		return userPreferences;
	}

	public String getProtocolLeadUnit(String ProtocolNumber, Integer sequenceNumber)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		Vector param = new Vector();
		Vector result = new Vector();
		String leadUnit = "";
		param.add(new Parameter("AW_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, ProtocolNumber));
		param.add(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER, sequenceNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_PROTOCOL_LEAD_UNIT(<<AW_PROTOCOL_NUMBER>>,<<AW_SEQUENCE_NUMBER>>,<<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			leadUnit = rowParameter.get("UNIT_NUMBER").toString();
		}

		return leadUnit;
	}

	/**
	 * Get the role details given the role id
	 * 
	 * @param roleId
	 *            role id of the role
	 * @return RoleInfoBean
	 */
	public RoleInfoBean getRole(String roleId) throws DBException, CoeusException {
		Vector param = new Vector();
		Vector result = new Vector();
		RoleInfoBean roleInfoBean = new RoleInfoBean();
		param.add(new Parameter("ROLE_ID", DBEngineConstants.TYPE_STRING, roleId));
		// calling stored procedure
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call GET_ROLE_DETAILS(<<ROLE_ID>>, <<OUT RESULTSET rset>>)",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowMap = (HashMap) result.get(0);
			roleInfoBean.setRoleId(Integer.parseInt(rowMap.get("ROLE_ID").toString()));
			roleInfoBean.setRoleName(rowMap.get("ROLE_NAME").toString());
			roleInfoBean.setRoleDesc(rowMap.get("DESCRIPTION").toString());
			roleInfoBean.setRoleType(rowMap.get("ROLE_TYPE").toString().trim().charAt(0));
			roleInfoBean.setDescend(rowMap.get("DESCEND_FLAG").toString().equals("Y") ? true : false);
			roleInfoBean.setStatus(rowMap.get("STATUS_FLAG").toString().trim().charAt(0));
			roleInfoBean.setUnitNumber(rowMap.get("OWNED_BY_UNIT").toString());
			roleInfoBean.setUnitName(rowMap.get("UNIT_NAME").toString());
			roleInfoBean.setUpdateTimestamp((Timestamp) rowMap.get("UPDATE_TIMESTAMP"));
			roleInfoBean.setUpdateUser(rowMap.get("UPDATE_USER").toString());
		}
		return roleInfoBean;
	}
	// Added for Coeus 4.3 : PT ID 2232 - Custom Roles - end

	/**
	 * This method used to get Roles for the given Unit Number
	 * <li>To fetch the data, it uses the Authorization API method
	 * getAuthorizations().
	 *
	 * @return Vector of unit number and unit name
	 * @param roleId
	 *            Role Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API.
	 */
	public Vector getRoleRightsForRole(String roleId)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);
		Function function = (Function) myAuthFactory.newFunction("ROLE", roleId);

		AuthorizationsIterator myIterator;
		myIterator = (AuthorizationsIterator) myAuthFactory.getAuthorizations(null, function, null, false, false);
		Authorization authorization;
		FunctionType functionType;

		RoleRightInfoBean roleRightInfoBean;
		Vector result = new Vector();
		if (myIterator != null) {
			while (myIterator.hasNext()) {
				roleRightInfoBean = new RoleRightInfoBean();
				authorization = (Authorization) myIterator.next();
				properties = authorization.getProperties();
				function = (Function) properties.get("FUNCTION");
				functionType = (FunctionType) function.getFunctionType();

				roleRightInfoBean.setRightId(function.getKey().toString());
				roleRightInfoBean.setDescription(function.getDescription());
				roleRightInfoBean.setRightType(functionType.getKeyword().charAt(0));

				roleRightInfoBean.setDescendFlag(functionType.getDescription() == null ? false
						: (functionType.getDescription().toString().equalsIgnoreCase("y") ? true : false));
				roleRightInfoBean.setRoleId(Integer.parseInt(roleId));
				roleRightInfoBean.setUpdateUser((String) properties.get("UPDATE_USER"));
				roleRightInfoBean.setUpdateTimestamp((java.sql.Timestamp) properties.get("UPDATE_TIMESTAMP"));
				result.add(roleRightInfoBean);
			}
		}
		return result;
	}

	/**
	 * This method used to get Roles for the given Unit Number
	 * <li>To fetch the data, it uses the getAuthorizations() of Authorization
	 * API.
	 *
	 * @return Vector of RoleInfoBean
	 * @param unitNumber
	 *            to get Roles for this Unit
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API.
	 */
	public Vector getRolesForUnit(String unitNumber)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);
		Function function = null;
		// Person person = null;
		Qualifier qualifier = (Qualifier) myAuthFactory.newQualifier("UNIT", unitNumber);
		qualifier.setDescription("ROLES");

		AuthorizationsIterator myIterator;
		myIterator = (AuthorizationsIterator) myAuthFactory.getAuthorizations(null, function, qualifier, false, false);

		Authorization authorization;

		RoleInfoBean roleInfoBean;
		FunctionType functionType;
		Vector result = new Vector();
		if (myIterator != null) {
			while (myIterator.hasNext()) {
				roleInfoBean = new RoleInfoBean();
				authorization = (Authorization) myIterator.next();
				properties = authorization.getProperties();
				function = (Function) properties.get("FUNCTION");
				qualifier = (Qualifier) properties.get("QUALIFIER");

				functionType = (FunctionType) function.getFunctionType();

				roleInfoBean.setRoleId(Integer.parseInt(function.getKey().toString()));
				roleInfoBean.setRoleName((String) properties.get("ROLE_NAME"));
				roleInfoBean.setRoleDesc(function.getDescription());
				roleInfoBean.setRoleType(functionType.getDescription().charAt(0));
				roleInfoBean.setStatus(((String) properties.get("STATUS_FLAG")).charAt(0));
				roleInfoBean.setDescend(properties.get("DESCEND_FLAG") == null ? false
						: (properties.get("DESCEND_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				roleInfoBean.setUnitNumber((String) qualifier.getKey());
				roleInfoBean.setUpdateUser((String) properties.get("UPDATE_USER"));
				roleInfoBean.setUpdateTimestamp((java.sql.Timestamp) properties.get("UPDATE_TIMESTAMP"));
				result.add(roleInfoBean);
			}
		}
		return result;
	}

	// Added for COEUSQA-1692_User Access - Maintenance_start
	/**
	 * This method used to get all Roles assigned for the given User under the
	 * given Unit Number To fetch the data, it uses the Procedure
	 * get_user_roles_in_unit.
	 *
	 * @return Vector containing UnitUserRolesMaintenanceFormBean
	 * @param userId
	 *            User Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getUnitUserRoles(String userId, String unitNumber) throws DBException, CoeusException {
		Vector result = new Vector();
		Vector param = new Vector();
		DepartmentPersonTxnBean departmentTxnBean = new DepartmentPersonTxnBean();
		Vector vecUnitUserRoles = new Vector();
		Vector vecRoles = new Vector();
		param.addElement(new Parameter("AW_USER_ID", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, unitNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call get_user_roles_for_unit ( <<AW_USER_ID>>,<<AW_UNIT_NUMBER>>, <<OUT RESULTSET rset>> ) ",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();

		if (listSize > 0) {
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				RoleInfoBean roleInfoBean = new RoleInfoBean();
				HashMap rolesRow = (HashMap) result.elementAt(rowIndex);
				roleInfoBean.setRoleId(Integer.parseInt(rolesRow.get("ROLE_ID").toString()));
				roleInfoBean.setRoleName((String) rolesRow.get("ROLE_NAME"));
				roleInfoBean.setRoleType(((String) rolesRow.get("ROLE_TYPE")).charAt(0));
				roleInfoBean.setStatus(((String) rolesRow.get("STATUS_FLAG")).charAt(0));
				roleInfoBean.setDescend(rolesRow.get("DESCEND_FLAG") == null ? false
						: (rolesRow.get("DESCEND_FLAG").toString().equalsIgnoreCase("Y") ? true : false));
				roleInfoBean.setUnitNumber((String) rolesRow.get("UNIT_NUMBER"));
				roleInfoBean.setUnitName(departmentTxnBean.getUnitName(roleInfoBean.getUnitNumber()));
				roleInfoBean.setUserId((String) rolesRow.get("USER_ID"));
				roleInfoBean.setUpdateTimestamp((java.sql.Timestamp) rolesRow.get("UPDATE_TIMESTAMP"));
				vecRoles.add(roleInfoBean);
			}
		}
		return vecRoles;
	}

	/**
	 * This method used to get User Info for the given User Id
	 * <li>To fetch the data, it uses Authorization API method getUserInfo().
	 *
	 * @return Vector of unit number and unit name
	 * @param userId
	 *            User Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API
	 */
	public UserInfoBean getUser(String userId)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		// java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);
		Person person = (Person) myAuthFactory.newPerson(userId);
		UserInfoBean userInfoBean = new UserInfoBean();
		Person authperson = (Person) myAuthFactory.getUserInfo(person);
		if (person != null && authperson != null) {
			userInfoBean.setUserId((String) authperson.getKey());
			userInfoBean.setUserName(authperson.getName());
			// userInfoBean.setUserId((String)authperson.getKerberosName());
			userInfoBean.setPersonId((String) authperson.getProperty("PERSON_ID"));
			userInfoBean.setNonEmployee(authperson.getProperty("NON_MIT_PERSON_FLAG") == null ? false
					: (authperson.getProperty("NON_MIT_PERSON_FLAG").toString().equalsIgnoreCase("y") ? true : false));
			userInfoBean.setStatus(authperson.getStatusCode().charAt(0));
			// Added on March 18, 2004
			userInfoBean.setAw_Status(userInfoBean.getStatus());
			userInfoBean.setUnitName((String) authperson.getProperty("UNIT_NAME"));
			userInfoBean.setUnitNumber((String) authperson.getProperty("UNIT_NUMBER"));
			userInfoBean.setUpdateUser((String) authperson.getProperty("UPDATE_USER"));
			userInfoBean.setUpdateTimestamp((java.sql.Timestamp) authperson.getProperty("UPDATE_TIMESTAMP"));
		}
		return userInfoBean;
	}

	// Coeus Enhancement case #1799 start
	/**
	 * This method returns the user Email Id
	 * 
	 * @param userId
	 *            is the input
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @return type is String
	 */
	public String getUserEMail(String userId) throws CoeusException, DBException {
		// boolean loginCheck;
		Vector param = new Vector();
		Vector result = new Vector();
		String emailID = "";
		param.add(new Parameter("USER_ID", "String", userId.toUpperCase()));
		// calling function
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT STRING ls_ret>> = " + "call fn_get_current_user_email(  << USER_ID >> ) } ", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			emailID = rowParameter.get("ls_ret").toString();
		}

		return emailID;
	}

	/**
	 * This method is used to check whether the User has any OSP right
	 * <li>To fetch the data, it uses Authorization API method isAuthorized().
	 *
	 * @return boolean True/False
	 * @param userId
	 *            User Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API
	 **/
	public boolean getUserHasAnyOSPRight(String userId)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		// java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);

		FunctionType functionType = (FunctionType) myAuthFactory.newFunctionType("OSP", "OSP Rights");
		Person person = (Person) myAuthFactory.newPerson(userId);
		Qualifier qualifier = null;

		boolean success;
		success = myAuthFactory.isAuthorized(person, functionType, qualifier);

		return success;
	}

	// Added for COEUSQA-1724 : IACUC module - parent case - Start
	/**
	 * This method is used to check whether the User has IACUC Protocol right
	 * <li>To fetch the data, it uses Authorization API method isAuthorized().
	 *
	 * @return Vector of unit number and unit name
	 * @param userId
	 *            User Id
	 * @param rightId
	 *            Right Id
	 * @param protocolNumber
	 *            Protocol Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API
	 */
	public boolean getUserHasIACUCProtocolRight(String userId, String rightId, String protocolNumber)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);

		Person person = (Person) myAuthFactory.newPerson(userId);
		Function function = (Function) myAuthFactory.newFunction("RIGHT_ID", rightId);
		Qualifier qualifier = (Qualifier) myAuthFactory.newQualifier("IACUC_PROTOCOL", protocolNumber);

		boolean success;
		success = myAuthFactory.isAuthorized(person, function, qualifier);

		return success;
	}
	// COEUSQA-1724 :End

	/**
	 * This method is used to check whether the User has OSP right
	 * <li>To fetch the data, it uses Authorization API method isAuthorized().
	 *
	 * @return boolean True/False
	 * @param userId
	 *            User Id
	 * @param rightId
	 *            Right Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API
	 */
	public boolean getUserHasOSPRight(String userId, String rightId)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		// java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);

		FunctionType functionType = (FunctionType) myAuthFactory.newFunctionType("OSP", "OSP Rights");
		Person person = (Person) myAuthFactory.newPerson(userId);
		Function function = (Function) myAuthFactory.newFunction("RIGHT_ID", rightId);

		boolean success;
		success = myAuthFactory.isAuthorized(person, function, functionType);

		return success;
	}

	/**
	 * This method is used to check whether the User has Proposal right
	 * <li>To fetch the data, it uses Authorization API method isAuthorized().
	 *
	 * @return Vector of unit number and unit name
	 * @param userId
	 *            User Id
	 * @param proposalNumber
	 *            Proposal Number
	 * @param rightId
	 *            Right Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API
	 **/
	public boolean getUserHasProposalRight(String userId, String protocolNumber, String rightId)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		// java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);

		Person person = (Person) myAuthFactory.newPerson(userId);
		Function function = (Function) myAuthFactory.newFunction("RIGHT_ID", rightId);
		Qualifier qualifier = (Qualifier) myAuthFactory.newQualifier("PROPOSAL", protocolNumber);

		boolean success;
		success = myAuthFactory.isAuthorized(person, function, qualifier);

		return success;
	}

	/**
	 * This method is used to check whether the User has Protocol right
	 * <li>To fetch the data, it uses Authorization API method isAuthorized().
	 *
	 * @return Vector of unit number and unit name
	 * @param userId
	 *            User Id
	 * @param rightId
	 *            Right Id
	 * @param protocolNumber
	 *            Protocol Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API
	 */
	public boolean getUserHasProtocolRight(String userId, String rightId, String protocolNumber)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		// java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);

		Person person = (Person) myAuthFactory.newPerson(userId);
		Function function = (Function) myAuthFactory.newFunction("RIGHT_ID", rightId);
		Qualifier qualifier = (Qualifier) myAuthFactory.newQualifier("PROTOCOL", protocolNumber);

		boolean success;
		success = myAuthFactory.isAuthorized(person, function, qualifier);

		return success;
	}

	/**
	 * This method is used to check whether the User has OSP right
	 * <li>To fetch the data, it uses Authorization API method isAuthorized().
	 *
	 * @return Vector of unit number and unit name
	 * @param userId
	 *            User Id
	 * @param rightId
	 *            Right Id
	 * @param unitNumber
	 *            Unit Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API
	 */
	public boolean getUserHasRight(String userId, String rightId, String unitNumber)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		// java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);

		Person person = (Person) myAuthFactory.newPerson(userId);
		Function function = (Function) myAuthFactory.newFunction("RIGHT_ID", rightId);
		Qualifier qualifier = (Qualifier) myAuthFactory.newQualifier("UNIT_NUMBER", unitNumber);

		boolean success;
		success = myAuthFactory.isAuthorized(person, function, qualifier);

		return success;
	}

	/**
	 * This method is used to check whether the User has right in any Unit
	 * <li>To fetch the data, it uses Authorization API method isAuthorized().
	 *
	 * @return Vector of unit number and unit name
	 * @param userId
	 *            User Id
	 * @param rightId
	 *            Right Id
	 * @param unitNumber
	 *            Unit Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API
	 */
	public boolean getUserHasRightInAnyUnit(String userId, String rightId)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		// java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);

		Person person = (Person) myAuthFactory.newPerson(userId);
		Function function = (Function) myAuthFactory.newFunction("RIGHT_ID", rightId);
		Qualifier qualifier = null;

		boolean success;
		success = myAuthFactory.isAuthorized(person, function, qualifier);

		return success;
	}

	/**
	 * This method is used to check whether the User has Role
	 * <li>To fetch the data, it uses Authorization API method isAuthorized().
	 *
	 * @return Vector of unit number and unit name
	 * @param userId
	 *            User Id
	 * @param roleId
	 *            Role Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API
	 */
	public boolean getUserHasRole(String userId, int roleId)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		// java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);

		Person person = (Person) myAuthFactory.newPerson(userId);
		Function function = (Function) myAuthFactory.newFunction("ROLE_ID", "" + roleId);
		FunctionType functionType = (FunctionType) myAuthFactory.newFunctionType("ROLE", "IRB ROLE");
		function.setFunctionType(functionType);
		Qualifier qualifier = null;

		boolean success;
		success = myAuthFactory.isAuthorized(person, function, qualifier);

		return success;
	}

	/**
	 * This method used to get User Name for the given User Id
	 * <li>To fetch the data, it uses the function FN_GET_USER_NAME.
	 *
	 * @return String UserName
	 * @param userId
	 *            User Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public String getUserName(String userId) throws DBException, CoeusException {

		String userName = null;
		// boolean blnExist = false;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("USER_ID", "String", userId));
		// calling stored function
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT STRING USER_NAME>> = " + "call FN_GET_USER_NAME(  << USER_ID >> ) } ", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			userName = (String) rowParameter.get("USER_NAME");
		}
		if (userName == null || userName.equals("")) {
			userName = userId;
		}
		return userName;
	}

	/*
	 * Added for case # 4229: Email Notification not checking user preferences
	 * -start
	 */
	/**
	 * This method is used to get the user Preference Value for the given User.
	 * 
	 * @param userId
	 *            String User Id
	 * @param userId
	 *            String preference
	 * @return String of preference.
	 * @exception CoeusException
	 *                raised if dbEngine is null.
	 * @exception DBException
	 *                raised from the server side.
	 */
	public String getUserPreference(String userId, String preference) {

		java.util.Vector userPreferences = new Vector();
		String result = null;
		try {
			userPreferences = getUserPreferences(userId);
			// 4363: Notification rules not working
			// Provided Null check for userPreferences. If there are no User
			// Preferences,
			// userPreferences becomes null.
			if (userPreferences != null && userPreferences.size() > 0) {
				int listSize = userPreferences.size();
				for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
					UserPreferencesBean preferencesRow = (UserPreferencesBean) userPreferences.elementAt(rowIndex);
					if (preferencesRow.getVariableName().equalsIgnoreCase(preference)) {
						result = preferencesRow.getVarValue();
						break;
					}
				}
			}
		} catch (Exception ex) {
			result = null;
			UtilFactory.log(ex.getMessage(), ex, "UserMaintDataTxnBean", "getUserPreference");
		}
		return result;
	}

	/*
	 * Added for case # 4229: Email Notification not checking user preferences
	 * -end
	 */
	/**
	 * This method is used to get User Preferences for the given User.
	 * 
	 * @param userId
	 *            String User Id
	 * @return Vector of UserPreferencesBean.
	 * @exception CoeusException
	 *                raised if dbEngine is null.
	 * @exception DBException
	 *                raised from the server side.
	 */
	public Vector getUserPreferences(String userId) throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		UserPreferencesBean userPreferencesBean = null;
		param.addElement(new Parameter("USER_ID", "String", userId));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_USER_PREFERENCES ( <<USER_ID>> , <<OUT RESULTSET rset>> ) ", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector userPreferences = null;
		if (listSize > 0) {
			userPreferences = new Vector(3, 2);
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				userPreferencesBean = new UserPreferencesBean();
				HashMap preferencesRow = (HashMap) result.elementAt(rowIndex);
				userPreferencesBean.setUserId((String) preferencesRow.get("USER_ID"));
				userPreferencesBean.setVarValue((String) preferencesRow.get("VALUE"));
				userPreferencesBean.setUpdateUser((String) preferencesRow.get("UPDATE_USER"));
				userPreferencesBean.setUpdateTimestamp((java.sql.Timestamp) preferencesRow.get("UPDATE_TIMESTAMP"));
				userPreferencesBean.setVariableName((String) preferencesRow.get("VARIABLE_NAME"));
				userPreferencesBean.setVarDescription((String) preferencesRow.get("DESCRIPTION"));
				userPreferencesBean.setVarDataType((String) preferencesRow.get("DATA_TYPE"));
				userPreferencesBean.setVarDefaultValue((String) preferencesRow.get("DEFAULT_VALUE"));
				userPreferences.addElement(userPreferencesBean);
			}
		}
		return userPreferences;
	}

	// Coeus Enhancement case #1799 end
	// Main method for testing the bean
	/*
	 * public static void main(String args[]) { UserMaintDataTxnBean txnData =
	 * new UserMaintDataTxnBean();
	 * 
	 * //ProtocolSpecialReviewFormBean naBean = new
	 * ProtocolSpecialReviewFormBean(); //ProtocolRolesFormBean raBean = new
	 * ProtocolRolesFormBean(); try{ boolean success = false; Vector vct = new
	 * Vector(3,2); UserInfoBean userInfoBean ; /* String userName =
	 * txnData.getUserName("coeus"); System.out.println("User Name :"+userName);
	 */
	// boolean pwdCheck = txnData.changePassword("coeus", "coeus");
	// System.out.println("Password status "+pwdCheck);
	/*
	 * UserInfoBean userInfoBean = new UserInfoBean(); assignedRoles =
	 * 
	 * if(vct != null){ for(int i=0;i<vct.size();i++){ userInfoBean =
	 * (UserInfoBean) vct.elementAt(i); System.out.println("User Id : "
	 * +userInfoBean.getUserId()); System.out.println("Non MIT : "
	 * +userInfoBean.isNonEmployee()); } } else{ System.out.println("Is null");
	 * }
	 */

	/*
	 * UserMaintRoleBean userMaintRoleBean = new UserMaintRoleBean();
	 * userMaintRoleBean.setAcType("I"); userMaintRoleBean.setDescendFlag('Y');
	 * userMaintRoleBean.setUserId("popeye"); userMaintRoleBean.setRoleId(3);
	 * userMaintRoleBean.setUnitNumber("000001");
	 * userMaintRoleBean.setUpdateTimestamp(txnUpd.dbTimestamp);
	 * userMaintRoleBean.setUpdateUser(txnUpd.userId);
	 * vct.addElement(userMaintRoleBean); success =
	 * txnUpd.addUpdDeleteUserRoles(vct);
	 */
	/*
	 * Vector vct = (Vector) txnData.getDelegatedTo("coeus"); if(vct!=null){
	 * System.out.println("Size : "+vct.size()); }else{ System.out.println(
	 * "Is null"); }
	 */
	/*
	 * userDelegationsBean = (UserDelegationsBean)vct.elementAt(0);
	 * System.out.println("User Name : " +userDelegationsBean.getUserName());
	 * System.out.println("Delegated By : "
	 * +userDelegationsBean.getDelegatedBy()); System.out.println(
	 * "Delegated To : "+userDelegationsBean.getDelegatedTo());
	 * System.out.println("Eff Date : "
	 * +userDelegationsBean.getEffectiveDate()); System.out.println(
	 * "End Date : " +userDelegationsBean.getEndDate()); System.out.println(
	 * "Status : "+userDelegationsBean.getStatus()); System.out.println(
	 * "Update User : "+userDelegationsBean.getUpdateUser());
	 * System.out.println("Update Time Stamp : "
	 * +userDelegationsBean.getUpdateTimestamp());
	 * 
	 * userDelegationsBean.setAcType("U"); //userDelegationsBean.setStatus('P');
	 * System.out.println("Before Update"); success =
	 * txnUpd.addUpdDeleteDelegations(userDelegationsBean); System.out.println(
	 * "Updated success : " +success);
	 * 
	 * if(success) System.out.println("successfully inserted"); else
	 * System.out.println("exception while insert");
	 */

	/*
	 * }catch(Exception e){ e.printStackTrace(); } }
	 */
	/*
	 * public static void main(String args[]) { try{ UserMaintDataTxnBean u= new
	 * UserMaintDataTxnBean(); String s = u.getUserEMail("coeus");
	 * System.out.println(s); } catch(CoeusException e) {} catch(DBException e)
	 * {}
	 * 
	 * 
	 * }
	 */

	/**
	 * This method used to get all the Role details required in User Details
	 * screen of UserMaintenance module
	 *
	 * @return Vector containing Assigned Roles and Not assigned Roles
	 * @param userId
	 *            User Id
	 * @param unitNumber
	 *            Unit Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API.
	 */
	public Vector getUserRolesDetails(String userId, String unitNumber)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		Vector vctUserRolesDetails = new Vector();
		Vector vctAssignedRoles = new Vector();
		Vector vctNotAssignedRoles = new Vector();

		UserInfoBean userInfoBean = null;
		// Get User Details for this User
		userInfoBean = getUser(userId);

		// Get all assigned Roles for this User
		vctAssignedRoles = getAssignedRoles(userId, unitNumber);
		// Get all not assigned Roles for this User
		vctNotAssignedRoles = getNotAssignedRoles(userId, unitNumber);

		vctUserRolesDetails.addElement(userInfoBean);
		vctUserRolesDetails.addElement(vctAssignedRoles);
		vctUserRolesDetails.addElement(vctNotAssignedRoles);

		return vctUserRolesDetails;
	}

	/**
	 * This method used to get Roles for the given Unit Number
	 * <li>To fetch the data, it uses the Authorization API method
	 * getAuthorizations().
	 *
	 * @return Hashtable containing Role Id and UserInfoBean
	 * @param unitNumber
	 *            Unit Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Hashtable getUserRolesForUnit(String unitNumber) throws DBException, CoeusException {
		Vector param = new Vector();
		Vector result = new Vector();
		UserInfoBean userInfoBean = null;
		UserRolesInfoBean userRolesInfoBean = null;
		Hashtable hshUserRoles = new Hashtable();
		param.addElement(new Parameter("UNIT_NUMBER", "String", unitNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_USER_ROLES_FOR_UNIT ( <<UNIT_NUMBER>> , <<OUT RESULTSET rset>> ) ", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector users = null;
		String strRoleId = null;
		if (listSize > 0) {
			users = new Vector(3, 2);
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				userInfoBean = new UserInfoBean();
				HashMap usersRow = (HashMap) result.elementAt(rowIndex);
				strRoleId = usersRow.get("ROLE_ID").toString();
				userInfoBean.setUserId((String) usersRow.get("USER_ID"));
				userInfoBean.setUserName((String) usersRow.get("USER_NAME"));
				userInfoBean.setUnitNumber((String) usersRow.get("UNIT_NUMBER"));
				userInfoBean.setStatus(((String) usersRow.get("STATUS")).charAt(0));
				// Added on March 18, 2004
				userInfoBean.setAw_Status(userInfoBean.getStatus());
				userInfoBean.setDescendFlag(((String) usersRow.get("DESCEND_FLAG")).charAt(0));
				userInfoBean.setUpdateUser((String) usersRow.get("UPDATE_USER"));
				userInfoBean.setUpdateTimestamp((java.sql.Timestamp) usersRow.get("UPDATE_TIMESTAMP"));

				userRolesInfoBean = new UserRolesInfoBean();
				userRolesInfoBean.setUserBean(userInfoBean);
				userRolesInfoBean.setIsRole(false);
				userRolesInfoBean.setHasChildren(false);

				if (hshUserRoles.containsKey(strRoleId)) {
					users = (Vector) hshUserRoles.get(strRoleId);
					users.addElement(userRolesInfoBean);
					hshUserRoles.put(strRoleId, users);
					users = new Vector(3, 2);
				} else {
					users.addElement(userRolesInfoBean);
					hshUserRoles.put(strRoleId, users);
					users = new Vector(3, 2);
				}
			}
		}
		return hshUserRoles;
	}

	/**
	 * This method used to get Roles for the given Unit Number
	 * <li>To fetch the data, it uses the Authorization API method
	 * getAuthorizations().
	 *
	 * @return Vector of unit number and unit name
	 * @param unitNumber
	 *            Unit Number
	 * @param roleId
	 *            Role Id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getUsersForRole(int roleId, String unitNumber) throws DBException, CoeusException {
		Vector param = new Vector();
		Vector result = new Vector();
		UserInfoBean userInfoBean = null;
		param.addElement(new Parameter("ROLE_ID", "int", new Integer(roleId).toString()));
		param.addElement(new Parameter("UNIT_NUMBER", "String", unitNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_USERS_FOR_ROLE ( <<ROLE_ID>> , <<UNIT_NUMBER>> , <<OUT RESULTSET rset>> ) ", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector users = null;
		if (listSize > 0) {
			users = new Vector(3, 2);
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				userInfoBean = new UserInfoBean();
				HashMap usersRow = (HashMap) result.elementAt(rowIndex);
				userInfoBean.setUserId((String) usersRow.get("USER_ID"));
				userInfoBean.setUserName((String) usersRow.get("USER_NAME"));
				userInfoBean.setUnitNumber((String) usersRow.get("UNIT_NUMBER"));
				userInfoBean.setStatus(((String) usersRow.get("STATUS")).charAt(0));
				// Added on March 18, 2004
				userInfoBean.setAw_Status(userInfoBean.getStatus());
				userInfoBean.setUpdateUser((String) usersRow.get("UPDATE_USER"));
				userInfoBean.setUpdateTimestamp((java.sql.Timestamp) usersRow.get("UPDATE_TIMESTAMP"));
				users.addElement(userInfoBean);
			}
		}
		return users;
	}

	/**
	 * This method used to get Users for the given Unit Number
	 * <li>To fetch the data, it uses the getAuthorizations() method of
	 * Authorization API.
	 *
	 * @return Vector of UserInfoBean
	 * @param unitNumber
	 *            to get all Users
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @exception org.okip.service.shared.api.Exception
	 *                if any exception in Authorization API.
	 *
	 */

	public Vector getUsersForUnit(String unitNumber)
			throws DBException, CoeusException, org.okip.service.shared.api.Exception {
		java.util.Properties properties;
		Factory myAuthFactory = (Factory) org.okip.service.shared.api.FactoryManager
				.getFactory("org.okip.service.authorization.api", "edu.mit.is.service.authorization", null);
		Function function = null;
		Person person = null;
		Qualifier qualifier = (Qualifier) myAuthFactory.newQualifier("UNIT", unitNumber);
		qualifier.setDescription("USERS");

		AuthorizationsIterator myIterator;
		myIterator = (AuthorizationsIterator) myAuthFactory.getAuthorizations(null, function, qualifier, false, false);

		Authorization authorization;

		UserInfoBean userInfoBean;
		Vector result = new Vector();
		if (myIterator != null) {
			while (myIterator.hasNext()) {
				userInfoBean = new UserInfoBean();
				authorization = (Authorization) myIterator.next();
				properties = authorization.getProperties();
				person = (Person) properties.get("PERSON");
				qualifier = (Qualifier) properties.get("QUALIFIER");

				userInfoBean.setUserId(person.getKerberosName());
				userInfoBean.setUserName(person.getName());
				userInfoBean.setNonEmployee(properties.get("NON_MIT_PERSON_FLAG") == null ? false
						: (properties.get("NON_MIT_PERSON_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				userInfoBean.setStatus(person.getStatusCode().charAt(0));
				// Added on March 18, 2004
				userInfoBean.setAw_Status(userInfoBean.getStatus());
				userInfoBean.setUnitNumber((String) qualifier.getKey());
				userInfoBean.setUnitName(((String) properties.get("UNIT_NAME")));
				userInfoBean.setPersonId((String) properties.get("PERSONID"));
				userInfoBean.setUpdateUser(((String) properties.get("UPDATE_USER")));
				userInfoBean.setUpdateTimestamp(((java.sql.Timestamp) properties.get("UPDATE_TIMESTAMP")));
				result.add(userInfoBean);
			}
		}
		return result;
	}

	/**
	 * This method used to get unit number and unit name for userId and rightId
	 * <li>To fetch the data, it uses the procedure DW_GET_UNITS_FOR_USER_RIGHT.
	 *
	 * @return int Count of Unit Map Info
	 * @param userId
	 *            User Id
	 * @param unitNumber
	 *            Unit Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public int getUserUnitMapInfo(String userId, String unitNumber) throws DBException, CoeusException {

		int number = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("USER_ID", "String", userId));
		param.add(new Parameter("UNIT_NUMBER", "String", unitNumber));
		// calling stored function
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus", "{ <<OUT INTEGER NUMBER>> = "
					+ "call FN_GET_USER_UNIT_MAPINFO(  << USER_ID >> , << UNIT_NUMBER >> ) } ", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			number = Integer.parseInt(rowParameter.get("NUMBER").toString());
		}
		return number;
	}

	// Added for COEUSQA-2037 : Software allows you to delete an investigator
	// who is assigned credit in the credit split window - start
	/*
	 * Method to check investigator has credit split either addsToHundred or not
	 * addsToHundred
	 * 
	 * @param moduleCode
	 * 
	 * @param moduleItemKey
	 * 
	 * @param moduleItemKeySequence
	 * 
	 * @param personId
	 * 
	 * @param isAddsToHundred - 'Y' OR 'N'
	 * 
	 * @return hasCreditSplit;
	 */
	public boolean isInvHasCreditSplit(String moduleCode, String moduleItemKey, int moduleItemKeySequence,
			String personId, String isAddsToHundred) {
		boolean hasCreditSplit = false;
		try {
			Vector param = new Vector();
			int hasCredit = 0;
			HashMap rowMap = null;
			Vector result = new Vector();
			param = new Vector();
			param.add(new Parameter("AS_MODULE_CODE", DBEngineConstants.TYPE_INT, new Integer(moduleCode).toString()));
			param.add(new Parameter("AS_MODULE_ITEM_KEY", DBEngineConstants.TYPE_STRING, moduleItemKey));
			param.addElement(new Parameter("AS_MODULE_ITEM_KEY_SEQUENCE", DBEngineConstants.TYPE_INT,
					new Integer(moduleItemKeySequence).toString()));
			param.add(new Parameter("AS_PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
			param.add(new Parameter("AS_ADDS_TO_HUNDRED", DBEngineConstants.TYPE_STRING, isAddsToHundred));

			if (dbEngine != null) {
				result = dbEngine.executeFunctions("Coeus",
						"{<<OUT INTEGER HAS_CREDIT_SPLIT>> = call FN_INV_HAS_CREDIT_SPLIT( "
								+ " << AS_MODULE_CODE >>, << AS_MODULE_ITEM_KEY >>, << AS_MODULE_ITEM_KEY_SEQUENCE >>, << AS_PERSON_ID >> , << AS_ADDS_TO_HUNDRED >>)}",
						param);
			} else {
				throw new CoeusException("db_exceptionCode.1000");
			}
			if (!result.isEmpty()) {
				rowMap = (HashMap) result.elementAt(0);
				hasCredit = Integer.parseInt(rowMap.get("HAS_CREDIT_SPLIT").toString());
			}
			// When function return 1, then investigator has credit split in
			// adds to hundred column
			if (hasCredit == 1) {
				hasCreditSplit = true;
			}
		} catch (Exception ex) {
			UtilFactory.log(ex.getMessage(), ex, "UserMaintDataTxnBean", "");
		}
		return hasCreditSplit;
	}

	/*
	 * Method to check investigator unit has credit split either addsToHundred
	 * or not addsToHundred
	 * 
	 * @param moduleCode
	 * 
	 * @param moduleItemKey
	 * 
	 * @param moduleItemKeySequence
	 * 
	 * @param personId
	 * 
	 * @param unitNumber
	 * 
	 * @param isAddsToHundred - 'Y' OR 'N'
	 * 
	 * @return hasCreditSplit;
	 */
	public boolean isInvUnitHasCreditSplit(String moduleCode, String moduleItemKey, int moduleItemKeySequence,
			String personId, String unitNumber, String isAddsToHundred) {
		boolean hasCreditSplit = false;
		try {
			Vector param = new Vector();
			int hasCredit = 0;
			HashMap rowMap = null;
			Vector result = new Vector();
			param = new Vector();
			param.add(new Parameter("AS_MODULE_CODE", DBEngineConstants.TYPE_INT, new Integer(moduleCode).toString()));
			param.add(new Parameter("AS_MODULE_ITEM_KEY", DBEngineConstants.TYPE_STRING, moduleItemKey));
			param.addElement(new Parameter("AS_MODULE_ITEM_KEY_SEQUENCE", DBEngineConstants.TYPE_INT,
					new Integer(moduleItemKeySequence).toString()));
			param.add(new Parameter("AS_PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
			param.add(new Parameter("AS_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, unitNumber));
			param.add(new Parameter("AS_ADDS_TO_HUNDRED", DBEngineConstants.TYPE_STRING, isAddsToHundred));

			if (dbEngine != null) {
				result = dbEngine.executeFunctions("Coeus",
						"{<<OUT INTEGER HAS_CREDIT_SPLIT>> = call FN_INV_UNIT_HAS_CREDIT_SPLIT( "
								+ " << AS_MODULE_CODE >>, << AS_MODULE_ITEM_KEY >>, << AS_MODULE_ITEM_KEY_SEQUENCE >>, << AS_PERSON_ID >> , << AS_UNIT_NUMBER >> , << AS_ADDS_TO_HUNDRED >>)}",
						param);
			} else {
				throw new CoeusException("db_exceptionCode.1000");
			}
			if (!result.isEmpty()) {
				rowMap = (HashMap) result.elementAt(0);
				hasCredit = Integer.parseInt(rowMap.get("HAS_CREDIT_SPLIT").toString());
			}
			// When function return 1, then investigator has credit split in
			// adds to hundred column
			if (hasCredit == 1) {
				hasCreditSplit = true;
			}
		} catch (Exception ex) {
			UtilFactory.log(ex.getMessage(), ex, "UserMaintDataTxnBean", "");
		}
		return hasCreditSplit;
	}
	// Added for COEUSQA-2037 : Software allows you to delete an investigator
	// who is assigned credit in the credit split window - End

	public boolean updateFirstTimeLoginUser(String userId) throws CoeusException, DBException {
		boolean updStatus;

		Vector param = new Vector();
		Vector result = new Vector();
		int number = 0;
		param.add(new Parameter("USER_ID", "String", userId.toUpperCase()));
		// calling function
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER UPDATE_STATUS>> = " + "call FN_UPD_FIRST_TIME_LOGIN(  << USER_ID >> ) } ", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			number = Integer.parseInt(rowParameter.get("UPDATE_STATUS").toString());
		}
		if (number == 0) {
			updStatus = true;
		} else {
			updStatus = false;
		}
		return updStatus;

	}

	public boolean userHasReportRight(String userId, String rightId) throws CoeusException, DBException {
		boolean hasRight = false;

		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AW_USER_ID", "String", userId.toUpperCase()));
		param.add(new Parameter("AW_RIGHT_ID", "String", rightId));

		// calling function
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ << OUT INTEGER RET >> = call FN_USER_HAS_REPORT_RIGHT ( << AW_USER_ID >> , << AW_RIGHT_ID >>) }",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		} //
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			String returnValue = rowParameter.get("RET").toString();
			if (returnValue.equals("1")) {
				hasRight = true;
			} else {
				hasRight = false;
			} //
		} //
		return hasRight;
	}//
}
