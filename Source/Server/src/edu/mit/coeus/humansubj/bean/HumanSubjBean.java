
/*
 * @(#) HumanSubjBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.humansubj.bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;

public class HumanSubjBean implements Serializable
{   /**
     * Instance of DBEngine.  Use its methods for retrieving and updating data.
     */
    private DBEngineImpl dbEngine;

    /**
     * No argument constructor
     * @throws DBException
     */
    public HumanSubjBean()
    {
      dbEngine = new DBEngineImpl();
    }

    /*
    declare arrays for all the attributes (columns) in the osp$person_training
    table.  Since this is a display-only JSP page, make all attributes Strings
    */
    private String[] fullName;
    private String[] trainingNumber;
    private String[] description;
    private String[] dateRequestedDisplayString;
    private String[] dateSubmittedDisplayString;
    private String[] dateAcknowledgedDisplayString;
    private String[] followupDateDisplayString;
    private String[] score;
    private String[] unitName;

    private int trainingListSize;

    public int getTrainingListSize()
    {
      return trainingListSize;
    }


    public String getFullName(int index)
    {   return fullName[index];
    }

     public String[] getFullName()
    {   return fullName;
    }


    public String getTrainingNumber(int index)
    {   return trainingNumber[index];
    }

    public String getDescription(int index)
    {   return description[index];
    }

    public String getDateRequestedDisplayString(int index)
    {   return dateRequestedDisplayString[index];
    }

     public String getDateSubmittedDisplayString(int index)
    {   return dateSubmittedDisplayString[index];
    }

    public String getDateAcknowledgeDisplayString(int index)
    {   return dateAcknowledgedDisplayString[index];
    }

    public String getFollowupDateDisplayString(int index)
    {   return followupDateDisplayString[index];
    }

    public String getScore(int index)
    {   return score[index];
    }

    public String getUnitName(int index)
    {   return unitName[index];
    }
    /**
     * Get list of all persons.
     * Call stored procedure which accesses OSP$PERSON_TRAINING,OSP$PERSON tables.
     * Set the bean's attributes with Data
     */
    public void getHumanSubjInfo() throws SQLException, Exception
    {
      try
      {
        Vector results = new Vector();
        Vector param = new Vector();

        results = dbEngine.executeRequest
            ("Coeus", "call get_trainees ( <<OUT RESULTSET rset>> ) ",
            "Coeus", param);

        trainingListSize =  results.size();
        System.out.println("No of rows:" + trainingListSize);
        if ( trainingListSize > 0)
        {
           //create all the attribues - before we just declared them
            fullName = new String[trainingListSize];
            trainingNumber = new String[trainingListSize];
            description = new String[trainingListSize];
            dateRequestedDisplayString = new String[trainingListSize];
            dateSubmittedDisplayString = new String[trainingListSize];
            score = new String[trainingListSize];
            unitName = new String[trainingListSize];

            /** the dbengine returns a vector (results) . vector has a row number
            * and an object.  the dbengine always puts a hashtable in the object
            * part of the vector
            * declare a hashtable variable that will hold the vector object
            */
            HashMap  traineeAttributes;

            for(int rowCount = 0; rowCount < trainingListSize; rowCount++)
             {
              /**
               * in this loop, get each object from the results vector and save
               *  in the hashtable variable we created
               */
              traineeAttributes = (HashMap)results.get(rowCount);
              /**
               * hashtable is a key value pair. the key part is always the database
               * column name.
               * get each value and save in the bean attribute
               */
              this.fullName[rowCount] = (String)traineeAttributes.get("FULL_NAME");
              trainingNumber[rowCount] = traineeAttributes.get("TRAINING_NUMBER").toString();
              description[rowCount] = (String)traineeAttributes.get("DESCRIPTION");
              dateRequestedDisplayString[rowCount] = 
                    traineeAttributes.get("DATE_REQUESTED") == null ? null :
                    traineeAttributes.get("DATE_REQUESTED").toString();
              dateSubmittedDisplayString[rowCount] = 
                    traineeAttributes.get("DATE_SUBMITTED") == null ? null :
                    traineeAttributes.get("DATE_SUBMITTED").toString();
              score[rowCount] = (String)traineeAttributes.get("SCORE");
              unitName[rowCount] = (String)traineeAttributes.get("UNIT_NAME");

             }
        }
     }
      catch(Exception e){ e.printStackTrace(); }
     }
    }



