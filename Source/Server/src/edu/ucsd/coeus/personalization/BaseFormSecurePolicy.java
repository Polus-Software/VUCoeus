package edu.ucsd.coeus.personalization;

import java.text.DateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.Utils;
import edu.ucsd.coeus.ServerUtils;
import edu.ucsd.coeus.personalization.authforms.Action;
import edu.ucsd.coeus.personalization.authforms.Aform;
import edu.ucsd.coeus.personalization.authforms.Argument;
import edu.ucsd.coeus.personalization.authforms.Attr;
import edu.ucsd.coeus.personalization.authforms.AuthFormsDocument;
import edu.ucsd.coeus.personalization.authforms.Boolean;
import edu.ucsd.coeus.personalization.authforms.Cfield;
import edu.ucsd.coeus.personalization.authforms.Value;
import edu.ucsd.coeus.personalization.authforms.AuthFormsDocument.AuthForms;
import edu.ucsd.coeus.personalization.formaccess.And;
import edu.ucsd.coeus.personalization.formaccess.Cform;
import edu.ucsd.coeus.personalization.formaccess.CoeusFormsSecureDocument;
import edu.ucsd.coeus.personalization.formaccess.Condition;
import edu.ucsd.coeus.personalization.formaccess.Expr;
import edu.ucsd.coeus.personalization.formaccess.Expression;
import edu.ucsd.coeus.personalization.formaccess.Exprtype;
import edu.ucsd.coeus.personalization.formaccess.Opertype;
import edu.ucsd.coeus.personalization.formaccess.Or;
import edu.ucsd.coeus.personalization.formaccess.CoeusFormsSecureDocument.CoeusFormsSecure;

/**
 * 
 * @author rdias. UCSD
 *
 */
public abstract class BaseFormSecurePolicy {
	
	protected ResponderBean responderBean = null;
	private RequesterBean requesterBean = null;
	private CoeusFormsSecureDocument fsecure = null;	
	private AuthFormsDocument authform = null;
	/* Default Variables used in the policy form */
	protected String creatorUnit;
	protected String creator;
	protected String currentUser;
	protected String currentUnit;
	protected String currentDate;
	protected String currentMode = "N";
	protected String currentUniqueID = "unknown";
	protected String currentStatus;
	
	public abstract void bindModuleVariable(ResponderBean responderBean);
	public abstract String getModuleBeanData(String attr, String beannm);
	public abstract String getPolicyModule();
	public abstract String getCorrectMode(RequesterBean requesterBean);

	public BaseFormSecurePolicy(CoeusFormsSecureDocument fsecure,
			RequesterBean requesterBean, ResponderBean responderBean) {
		super();
		this.fsecure = fsecure;
		this.requesterBean = requesterBean;
		this.responderBean = responderBean;
		initVariables();
	}
	
	protected void initVariables() {
		if (requesterBean != null) {
			currentUser = requesterBean.getUserName();
			currentMode = getCorrectMode(requesterBean);
		}
		currentDate = ServerUtils.sdf.format(new Date());
		if (responderBean != null) {
			bindModuleVariable(responderBean);
		}
	}
	
	protected void bindVariablesOnDemand(String varname) {
		if (varname.equals("$currentUnit") && currentUnit == null) {
			currentUnit = LoadVariables.getUnit(currentUser);
		}
		if (varname.equals("$creatorUnit") && creatorUnit == null) {
			creatorUnit = LoadVariables.getUnit(creator);
		}
	}
	
	/**
	 * TODO: If too many variables then use hash map
	 * @param variable
	 * @return
	 * 
	 */
	protected String getCurrentVariable(String variable) {
		bindVariablesOnDemand(variable);
		if (variable.equals("$creatorUnit"))
			return creatorUnit;
		else if (variable.equals("$creator"))
			return creator;
		else if (variable.equals("$currentUser"))
			return currentUser;
		else if (variable.equals("$currentUnit"))
			return currentUnit;
		else if (variable.equals("$currentDate"))
			return currentDate;
		else if (variable.equals("$currentUniqueID"))
			return currentUniqueID;
		else if (variable.equals("$currentStatus"))
			return currentStatus;
		else if (variable.equals("$currentMode"))
			return currentMode;		
		return "";
	}
	
	protected String getExprData(Expr expr) {
		String expdata = "";
		String givenval = expr.getValue();
		if (expr.getType() == Exprtype.CONSTANT) {
			return givenval;
		}
		if (expr.getType() == Exprtype.BEANDATA) {
			expdata = getModuleBeanData(givenval,expr.getClass1());
		}
		if (expr.getType() == Exprtype.VARIABLE) {
			expdata = getCurrentVariable(givenval);
		}
		if (expdata != null) expdata.trim();
		return expdata;
	}
	
	protected boolean compareExpression(Expr expr1, Expr expr2, Opertype.Enum oper) {
		String lhs = getExprData(expr1);
		String rhs = getExprData(expr2);
		int result = -1;
		if (lhs != null && rhs != null)
			result = lhs.compareTo(rhs);
		else
			return false;
		if (oper == Opertype.EQUAL) {
			return (result == 0?true:false);
		}
		if (oper == Opertype.NOT_EQUAL) {
			return (result != 0?true:false);
		}						
		if (oper == Opertype.LESS_THAN) {
			return (result < 0?true:false);
		} 
		if (oper == Opertype.GREATER_THAN) {
			return (result > 0?true:false);
		}
		return false;
	}
	
	/* The schema requires both AND and OR to have at least one condition */
	protected boolean processOrConditions(Condition conditions[]) {
		boolean orresult = false;
		for (int j = 0; j < conditions.length; j++) {
			Condition condition = conditions[j];
			Expr[] expr = condition.getExprArray();
			orresult = orresult || compareExpression(expr[0],expr[1],condition.getOper());
			if (orresult) {//In case of OR if any expression is true then break
				break;
			}			
		}	
		return orresult;
	}
	
	protected boolean processAndConditions(Condition conditions[]) {
		boolean andresult = true;
		for (int j = 0; j < conditions.length; j++) {
			Condition condition = conditions[j];
			Expr[] expr = condition.getExprArray();
			andresult = andresult && compareExpression(expr[0],expr[1],condition.getOper());
			if (!andresult) {//In case of AND if any expression is false then break
				break;
			}			
		}	
		return andresult;
	}
	
	protected boolean processOneCondition(Condition conditions[]) {
		if (conditions.length > 1) return false;
		Condition condition = conditions[0];
		Expr[] expr = condition.getExprArray();
		return compareExpression(expr[0],expr[1],condition.getOper());
	}	
	
	protected boolean processExpression(Expression expression) {
		boolean exp_ALL = false;
		boolean and_outer = true;
		boolean or_outer = false;			
		And ands[] = expression.getAndArray();
		if (ands != null && ands.length > 0) {
			for (int a = 0; a < ands.length; a++) {
				and_outer = and_outer && processAndConditions(ands[a].getConditionArray());
				if (!and_outer) break;
			}
			exp_ALL = and_outer;			
		}			
		Or ors[] = expression.getOrArray();
		if (ors != null && ors.length > 0) {
			for (int o = 0; o < ors.length; o++) {
				or_outer = or_outer || processOrConditions(ors[o].getConditionArray());
				if (or_outer) break;
			}
			exp_ALL = or_outer;
		}
		if (ors.length == 0 && ands.length == 0) {
			exp_ALL = processOneCondition(expression.getConditionArray());
		}
		if (ands.length > 0 && ors.length > 0) {//Both exists
			exp_ALL = or_outer && and_outer;
		}		
		return exp_ALL;		
	}
	
	protected void copyActions(Cform cform, Aform aform) {
		edu.ucsd.coeus.personalization.formaccess.Action actions[] =
			cform.getActionArray();
		for (int i = 0; i < actions.length; i++) {
			Action taraction = aform.addNewAction();
			taraction.setActionname(actions[i].getActionname());
			edu.ucsd.coeus.personalization.formaccess.Argument args[] =
				actions[i].getArgumentArray();
			for (int j = 0; j < args.length; j++) {
				Argument tararg = taraction.addNewArgument();
				edu.ucsd.coeus.personalization.formaccess.Value values[] =
					args[j].getValueArray();
				for (int k = 0; k < values.length; k++) {
					Value tarval = tararg.addNewValue();
					tarval.setKeyindx(values[k].getKeyindx());
					tarval.setStringValue(values[k].getStringValue());
				}
			}
		}
	}

	private String parseURL(String urlstr) {
		String constr = urlstr;		
		try {
			 StringTokenizer st = new StringTokenizer(urlstr,"#");
			 while (st.hasMoreTokens()) {
				 String ctoken = st.nextToken();
				 int pos = ctoken.indexOf('.');
				 if (ctoken.startsWith("@") && ctoken.endsWith("@") && pos >= 1) {
					 String beanclass = ctoken.substring(1, pos);
					 String beanattr = ctoken.substring(pos+1,ctoken.length() - 1);
					 String expdata = getModuleBeanData(beanattr,beanclass);
					 if (expdata != null) {
						 expdata = expdata.trim();
						 String varnm = "#" + ctoken + "#";
						 constr = Utils.replaceString(constr, varnm, expdata);
					 }
				 }
			 }		
		} catch (Exception e) {
			UtilFactory.log("Url format error", e, "BaseFormSecurePolicy", "parseURL");
		}
		return constr;
	}		
	
	protected void copyFields(Cform cform, Aform aform) {
		edu.ucsd.coeus.personalization.formaccess.Cfield cfields[] =
			cform.getCfieldArray();
		for (int i = 0; i < cfields.length; i++) {
			Cfield tarfield = aform.addNewCfield();
			tarfield.setType(cfields[i].getType());
			tarfield.setVarname(cfields[i].getVarname());			
			if (cfields[i].isSetDefault()) {
				String defstr = cfields[i].getDefault();
				if (defstr != null && defstr.startsWith("$"))
					defstr = getCurrentVariable(defstr);	
				tarfield.setDefault(defstr);				
			}
			if (cfields[i].isSetDisabled())
				tarfield.setDisabled(Boolean.Enum.forInt(cfields[i].getDisabled().intValue()));
			if (cfields[i].isSetName())
				tarfield.setName(cfields[i].getName());
			if (cfields[i].isSetTooltip())
				tarfield.setTooltip(cfields[i].getTooltip());
			if (cfields[i].isSetUrl()) {
				String urlstr = cfields[i].getUrl();
				UtilFactory.log("URL IS: " + urlstr);
				if (urlstr != null && urlstr.indexOf("$currentUniqueID") > 0) {
					String cuniqueID = getCurrentVariable("$currentUniqueID");
					urlstr = Utils.replaceString(urlstr, "$currentUniqueID", cuniqueID);
				}
				if (urlstr != null && urlstr.indexOf("$currentUser") > 0) {
					String cuser = getCurrentVariable("$currentUser");
					urlstr = Utils.replaceString(urlstr, "$currentUser", cuser);
				}
				String furl = parseURL(urlstr);
				UtilFactory.log(furl);
				tarfield.setUrl(furl);
			}
			if (cfields[i].isSetAttr()) {
				edu.ucsd.coeus.personalization.formaccess.Attr srcattr = cfields[i].getAttr();
				Attr tarattr = tarfield.addNewAttr();
				if (srcattr.isSetBgcolor()) {
					tarattr.setBgcolor(srcattr.getBgcolor());
				}
				if (srcattr.isSetFgcolor()) {
					tarattr.setFgcolor(srcattr.getFgcolor());
				}	
			}
		}
	}
	
	public AuthFormsDocument getAccessXml() {
		if (fsecure == null) return null;
		if (responderBean == null) {
			return null;
		}
		authform = AuthFormsDocument.Factory.newInstance();
		if (authform != null && authform.isNil()) return null;
		AuthForms fdoc = authform.addNewAuthForms();
		CoeusFormsSecure secforms = fsecure.getCoeusFormsSecure();
		Cform forms[] = secforms.getCformArray();
		boolean dataexists = false;
		for (int i = 0; i < forms.length; i++) {
			Cform currentform = forms[i];
			if (!currentform.getModule().equals(getPolicyModule()))
				continue;
			boolean exp_result = processExpression(currentform.getExpression());
			if (exp_result) {
				createAuthForm(currentform, fdoc);
				dataexists = true;
			}
		}
		authform.setAuthForms(fdoc);
		debugVar();
		if (!dataexists) return null;
		return authform;
	}
	
	
	/**
	 * If expression is true then create authform to send it to client
	 * @param cform
	 * @return
	 */
	protected void createAuthForm(Cform cform, AuthForms fdoc) {
		Aform aform = fdoc.addNewAform();
		aform.setClassname(cform.getClassname());
		aform.setUniqueID(currentUniqueID);
		aform.setModule(cform.getModule());
		copyActions(cform, aform);
		copyFields(cform, aform);
	}
	
	public void debugVar() {
		StringBuffer sb = new StringBuffer();
	    sb.append("creatorUnit: " + creatorUnit + "\n");
	    sb.append("creator: " + creator  + "\n");
	    sb.append("currentUser: " + currentUser  + "\n");
	    sb.append("currentUnit: " + currentUnit  + "\n");
	    sb.append("currentDate: " + currentDate  + "\n");
	    sb.append("currentUniqueID: " + currentUniqueID  + "\n");
	    sb.append("currentStatus: " + currentStatus + "\n");
	    sb.append("currentMode: " + currentMode + "\n");	    
	    UtilFactory.log(sb.toString());
	}
	

}
