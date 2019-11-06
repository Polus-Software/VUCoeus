/**
  $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GraphingBean.java,v 1.1.2.10 2007/01/24 16:30:56 cvs Exp $
  $Log: GraphingBean.java,v $
  Revision 1.1.2.10  2007/01/24 16:30:56  cvs
  Add support for Graph Award Types

  Revision 1.1.2.9  2007/01/23 20:45:36  cvs
  Add support for Graph Activity Types

  Revision 1.1.2.8  2007/01/23 18:13:12  cvs
  Add GUI support for Graph Sponsor Types

  Revision 1.1.2.7  2007/01/03 20:55:20  cvs
  Work on Activity Type report 2

  Revision 1.1.2.6  2006/12/28 20:37:20  cvs
   Add support for grants by sponsor type graph

  Revision 1.1.2.5  2006/12/14 18:17:48  cvs
  Add sample graph for Number of Proposals by Department

  Revision 1.1.2.4  2006/12/08 19:46:45  cvs
  Add support for JFreeChart charting engine

  Revision 1.1.2.3  2006/11/30 13:51:04  cvs
  Add styled PDF functionality based on iText tool

  Revision 1.1.2.2  2006/11/20 20:50:24  cvs
  Add TDG engine support for demo purposes

  Revision 1.1.2.1  2006/11/20 14:17:02  cvs
  Subclass ReportingBaseBean to two child classes for easy inheritance of methods

*
*/
/*
 * @(#)GraphingBean.java 
 *
 * Copyright (c) University of Medicine and Dentistry of New Jersey
 *
 * 1 World's Fair Drive, Somerset, New Jersey 08873
 *
 * All rights reserved.
 *
 * 
 * Author: Romerl Elizes
 *
 * Description: Graphing Bean to communicate with Oracle DB.
 * 
 */


package edu.umdnj.coeus.reporting.bean;

import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.Color;
import javax.xml.transform.TransformerException;
import java.io.*;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.exception.CoeusException;

// JFREECHART imports
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.imagemap.ImageMapUtilities;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.TableOrder;



import edu.umdnj.coeus.reporting.bean.UMDNJSQLUtilities;


public class GraphingBean extends ReportingBaseBean
{

    protected double[][] grapharray = null;

    Vector primaryresult = null;
    Vector typesresult = null;
    Vector primarytypes = null;
    String charttitle = "";

    /** Creates a new instance of GraphingBean */
    private String School = "";

    private UMDNJSQLUtilities pobj = null;

    public GraphingBean() 
    {
	super();
        pobj = new UMDNJSQLUtilities();
    }        


    public void SampleGraph(
                 String strGraph, 
                 String strFormat, 
                 HttpServletResponse res, 
                 String strType, 
                 String strSchool,
                 String strChartTitle)
         throws ServletException, IOException, CoeusException, DBException
    {
        School = "";
        Vector result = null;
        primaryresult = null;
        typesresult = null;

        Vector param=new Vector();
        String strout = "";
        String sqlstatement = "";
        GraphObj obj = new GraphObj();
        
        this.School = strSchool;
        this.charttitle = strChartTitle;

        if (strGraph.compareTo("GraphFour")==0)
        {
           primaryresult = pobj.getQueryResults(School,"GET_UMDNJ_GRAPH_SPONSORS");
           typesresult = pobj.getQueryResults("DW_GET_SPONSOR_TYPE");
           createGraphArray();
           createGraph(strFormat,"Sponsor",School,res);
        }
        else
        if (strGraph.compareTo("GraphFive")==0)
        {
           primaryresult = pobj.getQueryResults(School,"GET_UMDNJ_GRAPH_ACTIVITIES");
           typesresult = pobj.getQueryResults("DW_GET_ACTIVITY_TYPE");
           createGraphArray();
           createGraph(strFormat,"Activity",School,res);
        }
        else
        if (strGraph.compareTo("GraphSix")==0)
        {
           primaryresult = pobj.getQueryResults(School,"GET_UMDNJ_GRAPH_AWARDS");
           typesresult = pobj.getQueryResults("DW_GET_AWARD_TYPE");
           createGraphArray();
           createGraph(strFormat,"Award",School,res);
        }
    }
    
    private void createGraph(
                    String strFormat,
                    String strType,
                    String strSchool, 
                    HttpServletResponse res)
        throws ServletException, IOException
    {
       String directory = CoeusProperties.getProperty(CoeusPropertyKeys.COEUS_HOME);
       if (directory == null)
           directory = "c:/dev/tools/Tomcat 5.0.28/webapps/coeus";
       directory += "/temp";
   
       HashMap row = null;
       int listSize = typesresult.size();

       primarytypes = new Vector();
       for (int rowNum = 0; rowNum < listSize; rowNum++)
       {
           row=(HashMap)typesresult.elementAt(rowNum);
           Object obj = row.get("DESCRIPTION");
           String strval = getCellValue(obj);
           primarytypes.addElement(strval);
       }

       listSize = primaryresult.size();
       for (int rowNum = 0; rowNum < listSize; rowNum++)
       {
           row=(HashMap)primaryresult.elementAt(rowNum);
           Object obj1 = row.get("PARENT_UNIT_NUMBER");
           Object obj2 = row.get("DESCRIPTION");
           Object obj3 = row.get("NUM_AWARDS");
           String strval1 = getCellValue(obj1);
           String strval2 = getCellValue(obj2);
           double dval = getNumericCellValue(obj3);
           assignArray(strval1,strval2,dval);
       }

       res.setContentType("text/html");
       PrintWriter writer = res.getWriter();
       writer.println("<html><body>");

       HandleIndividualGraph(strType,writer,directory);
       writer.println("</body></html>");
       writer.close();
    }


    protected void assignArray(
                       String strval1,
                       String strval2, 
                       double dval)
    {
       int xindex = 0;
       int yindex = -1;

       for (int inum = 0; inum < primarytypes.size(); inum++)
       {
           String strcmp = (String)primarytypes.elementAt(inum);
           if (strval2.compareTo(strcmp)==0)
              yindex = inum;
 
       }
       if (yindex != -1)
          grapharray[xindex][yindex] += dval;
    }

    public void HandleIndividualGraph(
                       String Type,
                       PrintWriter writer,
                       String directory)
    {
           DefaultCategoryDataset dataset = new DefaultCategoryDataset();
           String series1 = "Number of Awards";
           int ilen = typesresult.size();

           for (int ynum = 0; ynum < ilen; ynum++)
           {
               String strtitle = (String)primarytypes.elementAt(ynum);
               dataset.addValue(grapharray[0][ynum],series1,strtitle);
                   
           }
           JFreeChart chart = null;
           
           CategoryAxis3D categoryAxis = new CategoryAxis3D(Type+" Types");
           ValueAxis valueAxis = new NumberAxis3D("Number of Awards");
           BarRenderer3D renderer = new BarRenderer3D();
           renderer.setToolTipGenerator(
                   new StandardCategoryToolTipGenerator());
           if (Type.compareTo("Sponsor")==0)
                renderer.setItemURLGenerator(new MyDrillDownGenerator1());
           else
           if (Type.compareTo("Activity")==0)
                renderer.setItemURLGenerator(new MyDrillDownGenerator2());
           else
           if (Type.compareTo("Award")==0)
                renderer.setItemURLGenerator(new MyDrillDownGenerator3());
           
           CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, 
                   valueAxis, renderer);
           plot.setOrientation(PlotOrientation.VERTICAL);
           chart = new JFreeChart(charttitle, JFreeChart.DEFAULT_TITLE_FONT, 
                   plot, false);
           chart.setBackgroundPaint(Color.lightGray);

           CategoryAxis domainAxis = plot.getDomainAxis();
           domainAxis.setCategoryLabelPositions(
           CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
           );
           
           try {
              ChartRenderingInfo info = new ChartRenderingInfo(
                      new StandardEntityCollection());
              String imgmap = "img00";
              double srand = Math.random() * 1000;
              String tempFile = directory + "/" + imgmap + srand +".png";
              File file1 = new File(tempFile);
              ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);
              String directory2 = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL);
              if (directory2 == null)
                  directory2 = "/coeus";
              String urldir = directory2 + "/temp/" + imgmap + srand +".png";
         
              ImageMapUtilities.writeImageMap(writer, imgmap, info);
              writer.println("<img src=\""+urldir+"\" "
                           + "width=\"600\" height=\"400\" usemap=\"#"+imgmap+"\" alt=\""+urldir+"\"/><p>");
          }
          catch(IOException ex) { 
       	     ex.printStackTrace(System.out);
          }
    }


    class MyDrillDownGenerator1 implements CategoryURLGenerator 
    {

        public String generateURL(CategoryDataset dataset, int series, int category) 
        {
            String host = "";
            try {
                host = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL);
            }
            catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
            if (host == null)
                  host = "/coeus/";
            
            String servletstr = host + "GetGrantsBySponsorServlet?UnitName=";
            // currentseries is a workaround for a limitation with JFreeChart.
            servletstr += School;
            servletstr += "&SponsorType=";
            servletstr += (String)primarytypes.elementAt(category);
            servletstr += "&Format=PDF";
            return servletstr;
        }
        
    }

    class MyDrillDownGenerator2 implements CategoryURLGenerator 
    {

        public String generateURL(CategoryDataset dataset, int series, int category) 
        {
            String host = "";
            try {
                host = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL);
            }
            catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
            if (host == null)
                  host = "/coeus/";

            String servletstr = host + "GetGrantsByActivityTypeServlet?UnitName=";
            // currentseries is a workaround for a limitation with JFreeChart.
            servletstr += School;
            servletstr += "&ActivityType=";
            servletstr += (String)primarytypes.elementAt(category);
            servletstr += "&Format=PDF";
            return servletstr;
        }
        
    }

    class MyDrillDownGenerator3 implements CategoryURLGenerator 
    {

        public String generateURL(CategoryDataset dataset, int series, int category) 
        {
            String host = "";
            try {
                host = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL);
            }
            catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
            if (host == null)
                  host = "/coeus/";
            String servletstr = host+ "GetGrantsByAwardTypeServlet?UnitName=";
            // currentseries is a workaround for a limitation with JFreeChart.
            servletstr += School;
            servletstr += "&AwardType=";
            
            servletstr += (String)primarytypes.elementAt(category);
            servletstr += "&Format=PDF";
            return servletstr;
        }
        
    }


    private void createGraphArray()
    {
       grapharray = new double [1][typesresult.size()];
       for (int inum = 0; inum < typesresult.size(); inum++)
           grapharray[0][inum] = 0;
    }

}
