
/*
 * @(#) NarrativesBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.propdev.bean.web;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Hashtable;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import java.util.Vector;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;

public class NarrativesBean implements Serializable
{   /**
     * Instance of DBEngine.  Use its methods for retrieving and updating data.
     */
    private DBEngineImpl dbEngine;

    /**
     * No argument constructor
     * @throws DBException
     */
    public NarrativesBean()
    {
      dbEngine = new DBEngineImpl();
    }
    private String[] moduleTitle;

    private String[] moduleStatus;

    private String[] sourceModuleNumber;

    private String[] pdfModuleNumber;

    private int[] narrativeViewRight;

    private int narrativeSize;

    public int getNarrativeSize()
    {
      return narrativeSize;
    }

    public String getModuleTitle(int index)
    {   return moduleTitle[index];
    }

    public String getModuleStatus(int index)
    {   return moduleStatus[index];
    }

    public String[] getModuleStatus()
    {   return moduleStatus;
    }

    public String getSourceModuleNumber(int index)
    {   return sourceModuleNumber[index];
    }

    public String getPdfModuleNumber(int index)
    {   return pdfModuleNumber[index];
    }

    public int getNarrativeViewRight(int index)
    {   return narrativeViewRight[index];
    }

    /**
     * Get all narratives for a given proposal.
     * Call stored procedure which accesses OSP$NARRATIV,OSP$NARRATIVE_PDF and OSP$NARRATIVE_SOURCE tables.
     * Set the bean's attributes with narrative data for the given proposal.
     * @param proposalNumber
     */
    /* Method updated to throw only DBException */
    public void getNarrativesInfo(String proposalNumber, String userId) throws DBException
    {
        Vector results = new Vector();
        Vector param = new Vector();

        param.addElement(new Parameter("AW_PROPNO", "String", proposalNumber));
        param.addElement(new Parameter("AW_USER_ID", "String", userId));
        results = dbEngine.executeRequest
            ("Coeus", "call get_narrative_info ( <<AW_PROPNO>> , <<AW_USER_ID>> ,  <<OUT RESULTSET rset>> ) ",
            "Coeus", param);

        narrativeSize =  results.size();
        if ( narrativeSize > 0)
        {
          moduleTitle = new String[narrativeSize];
          moduleStatus = new String[narrativeSize];
          sourceModuleNumber = new String[narrativeSize];
          pdfModuleNumber = new String[narrativeSize];
          narrativeViewRight = new int[narrativeSize];
          /* case #748 comment begin */
          //Hashtable narrativeAttributes;
          /* case #748 comment end */
          /* case #748 begin */
          HashMap narrativeAttributes;
          /* case #748 end */
          for(int msgCount = 0; msgCount < narrativeSize; msgCount++)
          {
            narrativeAttributes = (HashMap)results.get(msgCount);
            this.moduleTitle[msgCount] =
                (String)narrativeAttributes.get("MODULE_TITLE");
            moduleStatus[msgCount] =
                (String)narrativeAttributes.get("MODULE_STATUS");
            sourceModuleNumber[msgCount] =
                narrativeAttributes.get("SOURCE_MODULE_NUMBER") == null ? null :
                narrativeAttributes.get("SOURCE_MODULE_NUMBER").toString();
            pdfModuleNumber[msgCount] =
                narrativeAttributes.get("PDF_MODULE_NUMBER") == null ? null :
                narrativeAttributes.get("PDF_MODULE_NUMBER").toString();
            narrativeViewRight[msgCount] =
                Integer.parseInt(narrativeAttributes.get("VIEW_RIGHT").toString());
            System.out.println("narrative right ========> " + narrativeViewRight[msgCount]);
          }
        }
    }
  }



