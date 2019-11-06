/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeus.budget.xml;

import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import java.util.Map;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;

/**
 *
 * @author sharathk
 */
public interface BudgetSubawardXmlModifier {

    public BudgetSubAwardBean updateXML(byte xmlContents[], Map fileMap, BudgetSubAwardBean budgetSubAwardBean)throws Exception;
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    public Vector getSubAwardPeriodDetails(Document document, BudgetSubAwardBean budgetSubAwardBean)throws TransformerException, DBException,CoeusException;
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
    
}
