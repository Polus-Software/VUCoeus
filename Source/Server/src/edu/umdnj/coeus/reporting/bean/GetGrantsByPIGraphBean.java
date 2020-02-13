/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GetGrantsByPIGraphBean.java,v 1.1.2.14 2007/09/18 18:23:05 cvs Exp $
 * $Log: GetGrantsByPIGraphBean.java,v $
 * Revision 1.1.2.14  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.13  2007/08/16 18:11:45  cvs
 * 1. Add support for sorting by PI and Department
 * 2. Move sorting methods to parent class
 *
 * Revision 1.1.2.12  2007/07/31 13:36:19  cvs
 * Add Annual Reports server-side code
 * Move many methods to base class Reporting Base Bean
 *
 * Revision 1.1.2.11  2007/03/27 15:19:20  cvs
 * Modify code to work with Award FnA Distributions
 *
 * Revision 1.1.2.10  2007/03/22 15:03:23  cvs
 * Check for Proper Award Numbers and Fiscal Year delineations
 *
 * Revision 1.1.2.9  2007/03/21 20:05:15  cvs
 * Address title truncation
 *
 * Revision 1.1.2.8  2007/02/28 19:29:17  cvs
 * Modify graphs to accept current values of APP_HOME_URL and COEUS_HOME directories
 *
 * Revision 1.1.2.7  2007/02/16 16:04:07  cvs
 * Add randomized string to insure unique image creation
 *
 * Revision 1.1.2.6  2007/02/16 14:57:17  cvs
 * Add drilldown support to PI Grant graphs
 *
 * Revision 1.1.2.5  2007/02/13 19:45:43  cvs
 * Support headless in graphs
 *
 * Revision 1.1.2.4  2007/02/08 18:14:13  cvs
 * Add Support for Top 10 functionality
 *
 * Revision 1.1.2.3  2007/02/06 17:06:45  cvs
 * Add servlet support for PI Graphs GUI
 *
 * Revision 1.1.2.2  2007/02/06 15:43:11  cvs
 * Correct problem with pi mismatch offset by 1. Alos added support for Sort by total cost.
 *
 * Revision 1.1.2.1  2007/02/05 20:18:23  cvs
 * Add servlet support for PI Graphs
 *
 *
 */
/*
 * @(#)GetGrantsByPIGraphBean.java 
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
 * Description: Reporting Bean to communicate with Oracle DB.
 * 
 */


package edu.umdnj.coeus.reporting.bean;


import java.util.Vector;

import java.util.HashMap;

import java.util.Hashtable;

import java.sql.Date;

import java.sql.Timestamp;

import java.awt.Color;

import java.awt.Font;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.xml.transform.TransformerException;

import java.io.*;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;

import edu.mit.coeus.utils.dbengine.DBException;

import edu.mit.coeus.utils.dbengine.Parameter;

import edu.mit.coeus.utils.dbengine.DBEngineConstants;

import edu.mit.coeus.utils.CoeusVector;

import edu.mit.coeus.utils.UtilFactory;

import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.budget.report.ReportGenerator;

// Coeus MIT imports
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import org.apache.axis.wsdl.symbolTable.Type;

import edu.umdnj.coeus.reporting.bean.UMDNJSQLUtilities;

import java.awt.Color;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.block.EmptyBlock;
import org.jfree.chart.imagemap.ImageMapUtilities;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.title.TextTitle;


public class GetGrantsByPIGraphBean extends ReportingBaseBean
{

    private int runningTotalDirectCost;
    private int runningTotalIndirectCost;
    private int runningTotalCost;
    private int numawards;

    private String UnitName = "";
    private String DeptName = "";
    private boolean bNIH = false;
    private String Format = "PNG";
    
    private UMDNJSQLUtilities pobj = null;

    private GrantsObject grantsobj = null;
    private Vector someVector = null;
    private int BARWIDTH = 13;

    /** Creates a new instance of GetGrantsByPIGraphBean */
    public GetGrantsByPIGraphBean() 
    {
	super();
        Initialization();
    }
    
    public GetGrantsByPIGraphBean(String UnitName, String FiscalYear)
    {
        super();
        this.UnitName = UnitName;
        if (FiscalYear != null && FiscalYear.length() > 0)
            InitializeFiscalYear(FiscalYear);
        Initialization();
    }
    
    private void Initialization()
    {
        pobj = new UMDNJSQLUtilities();
    }

    public void getOutput(HttpServletResponse res,String Format)
         throws ServletException, IOException
    {
        Vector result = pobj.getGrantsByPI(UnitName);
        if (result != null && result.size() > 0)
        {
            DeptName = getUnitSchool(UnitName);
            if (DeptName.length() == 0)
            {
              HashMap arow = (HashMap)result.elementAt(0);
              Object str = arow.get("UNIT_NAME");
              DeptName = (String)str;
            }
           
            HashMap row = null;
            int listSize = result.size();
            int subtitlechanged = 0;
            grantsobj = null;
            someVector = new Vector();
            String strtitle = "";
            String strid = "";

            for (int inum = 0; inum < listSize; inum++)
            {           
                row = (HashMap)result.elementAt(inum);
                HashMap prevrow = null;
                if (inum > 0)
                   prevrow = (HashMap)result.elementAt(inum-1);
                String prevstrtitle = "";
                Object cell = row.get("FULL_NAME");
                Object cell2 = row.get("PERSON_ID");
                Object prevcell = null;
               
                strtitle = ((String)cell).trim();
                if (cell2 != null)
                    strid = ((String)cell2).trim();
                
                if (prevrow != null)
                {
                   prevcell = prevrow.get("FULL_NAME");
                   prevstrtitle = ((String)prevcell).trim();
                }
                else
                   prevstrtitle = strtitle;
                            
                FormatRecord(row);
            	
                if (inum == 0 || strtitle.compareTo(prevstrtitle)!=0)
                {
                   if (strtitle.compareTo(prevstrtitle)!=0)
                      subtitlechanged++;
                   if (inum != 0)
                   { 
                      Object prevcellid = prevrow.get("PERSON_ID");
                      if (prevcellid != null)
                        strid = ((String)prevcellid).trim();
                      displayTotals(prevstrtitle,strid);
                      runningTotalDirectCost = 0;
                      runningTotalIndirectCost = 0;
                      runningTotalCost = 0;
                      numawards = 0;
                   }
                }
            }
            if (subtitlechanged==0)
              displayAwardContents();
            displayTotals(strtitle,strid);
            callChartCreationRoutine(res,Format);
       }
       else
       {
           res.setContentType("text/html");
           PrintWriter writer = res.getWriter();
           writer.println("<html><body>");
           writer.print(UnitName + " does not have grants.");
           writer.println("</body></html>"); 
       }
    }


    private void FormatRecord(HashMap row)
    {
        	Object cell = row.get("MIT_AWARD_NUMBER");
        	String straccount = ((String)cell).trim();
        	int index1 = straccount.indexOf("-001");
        	if (index1 > 0)
        	{
                        displayAwardContents();
        		straccount = straccount.substring(0,index1);
        		grantsobj = new GrantsObject(straccount);    
        		populateCurrentGrantsObject(row);
        	}
        	else
        	{
        		handleCostValues(row);
        	}
    }
    
    
    private void populateCurrentGrantsObject(HashMap row)
    {
        Object begindatecell  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
        Object enddatecell  = row.get("OBLIGATION_EXPIRATION_DATE");
        Object totalamountcell  = row.get("ANTICIPATED_TOTAL_AMOUNT");
        Object titlecell  = row.get("TITLE");
        Object sponsorcell  = row.get("SPONSOR_NAME");
        Object investigatorcell  = row.get("FULL_NAME"); 
        Object totalbegindatecell = row.get("START_DATE");
        Object unitnamecell = row.get("UNIT_NAME");
    	String strtitle = getCellValue(unitnamecell).trim();

    	int index = strtitle.indexOf("-"+UnitName);
    	if (index > 0)
    		strtitle = strtitle.substring(0,index);

	grantsobj.setBeginProjectDate(getCellValue(begindatecell).trim());
	grantsobj.setEndProjectDate(getCellValue(enddatecell).trim());
	grantsobj.setProjectTotalCost(getNumericCellValue(totalamountcell));
	grantsobj.setProjectTitle(getCellValue(titlecell).trim());
	grantsobj.setSponsorName(getCellValue(sponsorcell).trim());
	grantsobj.setInvestigatorName(getCellValue(investigatorcell).trim());
 	grantsobj.setUnitName(strtitle);
        if (fiscalYear == 0)
        {
            grantsobj.setAwardBeginDate(getCellValue(totalbegindatecell).trim());    	
            grantsobj.setTotalCost(getNumericCellValue(totalamountcell));
        }
}

	private void handleCostValues(HashMap row)
	{
		if (grantsobj == null)
			return;
                
                if (fiscalYear > 0)
                {
                    Object totalbegindatecell = row.get("START_DATE");
                    Timestamp canddate = getTimeStampCellValue(totalbegindatecell);                   
                    if (canddate != null)
                    {
                        if (canddate.getTime() < begindate.getTime() || 
                                canddate.getTime() > enddate.getTime())
                                return;
                        grantsobj.setAwardBeginDate(getCellValue(totalbegindatecell).trim());    	
                    }
                }
      		else
		{
                    Object totalbegindatecell = row.get("START_DATE");
		    if (grantsobj.getAwardBeginDate() == null || grantsobj.getAwardBeginDate().length()==0)
                       grantsobj.setAwardBeginDate(getCellValue(totalbegindatecell).trim());    	
		}
                
		Object directcostcell = row.get("DIRECT_COST");
		double val1 = getNumericCellValue(directcostcell);
		grantsobj.addDirectCost(val1);
		
		Object indirectcostcell = row.get("INDIRECT_COST");
		double val2 = getNumericCellValue(indirectcostcell);
		grantsobj.addIndirectCost(val2);
                
	}

        private String getUnitSchool(String UnitName)
        {
            String strCand = "";
            if (UnitName.compareTo("DS")==0)
                strCand = "New Jersey Dental School";
            
            else if (UnitName.compareTo("NJMS") == 0)
                strCand = "New Jersey Medical School";
            else if (UnitName.compareTo("RWJ") == 0)
                strCand = "Robert Wood Johnson Medical School";
            else if (UnitName.compareTo("SOM") == 0)
                strCand = "School of Osteopathic Medicine";
            else if (UnitName.compareTo("SHRP") == 0)
                strCand = "School of Health-Related Professions";
            else if (UnitName.compareTo("SPH") == 0)
                strCand = "School of Public Health";
            else if (UnitName.compareTo("SN") == 0)
                strCand = "School of Nursing";
            return strCand;
        }
         
    
    public void setNIH(String NIH)
    {
        if (NIH != null && NIH.compareTo("true")==0)
            bNIH = true;
        else
            bNIH = false;
        if (pobj != null)
            pobj.setNIH(bNIH);
    }
    
    public void setSort(String val)
    {
        Sort = val;
    }
    
    public void setFormat(String val)
    {
        Format = val;
    }

    public void setInvestigator(String str)
    {
        if (str != null && str.length() > 0)
            pobj.setInvestigator(str);
    }
    
    private void displayAwardContents()
    {
       if (grantsobj != null)
       {
          grantsobj.setTotalCost(grantsobj.getDirectCost()+grantsobj.getIndirectCost());
          runningTotalDirectCost += grantsobj.getDirectCost();
          runningTotalIndirectCost += grantsobj.getIndirectCost();
          runningTotalCost += grantsobj.getTotalCost();
          numawards++;
       }
    }

    private void displayTotals(String strtitle, String strid)
    {
        PIGraphObject obj = new PIGraphObject();
        obj.setPiName(strtitle);
        obj.setEmployeeNumber(strid);
        obj.setNumGrants(numawards);
        obj.setTotalCost(runningTotalCost);
        someVector.addElement(obj);
    }

    private CategoryDataset createDataSet1()
    {
        String series = "Award Amounts";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Vector testVector = someVector;
        if (Sort.compareTo("amount")==0 || Sort.compareTo("top10")==0)
        {
            testVector = sortIncomingVectorByTotalCost(someVector);
            someVector = testVector;
        }
        
        for (int inum = 0; inum < testVector.size(); inum++)
        {
            PIGraphObject obj = (PIGraphObject)testVector.elementAt(inum);
            dataset.addValue(obj.getTotalCost(),series,obj.getPiName());
        }
        return dataset;

    }

    private CategoryDataset createDataSet2()
    {
        String series = "Number of Awards";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Vector testVector = someVector;
        if (Sort.compareTo("amount")==0 || Sort.compareTo("top10")==0)
            testVector = sortIncomingVectorByTotalCost(someVector);
        
        for (int inum = 0; inum < testVector.size(); inum++)
        {
            PIGraphObject obj = (PIGraphObject)testVector.elementAt(inum);
            dataset.addValue(obj.getNumGrants(),series,obj.getPiName());
        }
        return dataset;

    }

    private JFreeChart createChart(CategoryDataset dataset) 
    {
       String strtitle = "Award Breakdown by Investigator for "+ DeptName;
       if (fiscalYear > 0)
           strtitle += " for Fiscal Year "+fiscalYear;
       if (Sort.compareTo("top10")==0)
           strtitle += "\nTop 10 Performers";
        // create the chart...
            JFreeChart chart = ChartFactory.createBarChart(
            strtitle,        // chart title
            "Primary Investigators",               // domain axis label
            "Award Amounts",                  // range axis label
            dataset,         // data
            PlotOrientation.VERTICAL,
            false,                    // include legend
            true,                     // tooltips?
            true                     // URL generator?  Not required...
        );

        TextTitle texttitle = new TextTitle(strtitle);
        texttitle.setExpandToFitSpace(true);
        chart.setTitle(texttitle);
        
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

        BarRenderer renderer = new BarRenderer();
        renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        renderer.setItemURLGenerator(new MyDrillDownGenerator1());

        plot.setRenderer(0,renderer);
        
        CategoryDataset dataset2 = createDataSet2();
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
        final ValueAxis axis2 = new NumberAxis("Number of Awards");
        plot.setRangeAxis(1, axis2);

        LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        plot.setRenderer(1, renderer2);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        
        LegendTitle legend1 = new LegendTitle(plot.getRenderer(0));
        legend1.setMargin(new RectangleInsets(2, 2, 2, 2));
        legend1.setBorder(new BlockBorder());
        
        LegendTitle legend2 = new LegendTitle(plot.getRenderer(1));
        legend2.setMargin(new RectangleInsets(2, 2, 2, 2));
        legend2.setBorder(new BlockBorder());
        
        BlockContainer container = new BlockContainer(new BorderArrangement());
        container.add(legend1, RectangleEdge.LEFT);
        container.add(legend2, RectangleEdge.RIGHT);
        container.add(new EmptyBlock(2000, 0));
        CompositeTitle legends = new CompositeTitle(container);
        legends.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(legends);

        // OPTIONAL CUSTOMISATION COMPLETED.
        return chart;
    }

    private void callChartCreationRoutine(HttpServletResponse res,String Format)
         throws ServletException, IOException
    {
       String directory = CoeusProperties.getProperty(CoeusPropertyKeys.COEUS_HOME);
       if (directory == null)
           directory = "c:/dev/tools/Tomcat 5.0.28/webapps/coeus";
       directory += "/temp";

        CategoryDataset dataset = createDataSet1();
        JFreeChart chart = null;
        chart = createChart(dataset);

        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();
        writer.println("<html><body>");
        
        int numPI = someVector.size();
        if (Sort.compareTo("top10")==0)
            numPI = 10;
        int iwidth = numPI * BARWIDTH;
        if (iwidth < 800) iwidth = 800;

	try {
              ChartRenderingInfo info = new ChartRenderingInfo(
                      new StandardEntityCollection());
              String imgmap = "img00" + UnitName + fiscalYear + Sort;
              String fileext = ".png";
              double srand = Math.random() * 1000;
              if (Format.compareTo("JPEG")==0)
                  fileext = ".jpg";
              String tempFile = directory + "/" + imgmap + srand + fileext;
              File file1 = new File(tempFile);
              if (Format.compareTo("JPEG")==0)
                ChartUtilities.saveChartAsJPEG(file1, chart, iwidth, 500, info);
              else
                ChartUtilities.saveChartAsPNG(file1, chart, iwidth, 500, info);
              String directory2 = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL);
              if (directory2 == null)
                  directory2 = "/coeus";
              String urldir = directory2 + "/temp/" + imgmap + srand + fileext;
              ImageMapUtilities.writeImageMap(writer, imgmap, info);
              writer.println("<img src=\""+urldir+"\" "
                           + "width=\""+iwidth+"\" height=\"500\" usemap=\"#"+imgmap+"\" alt=\""+urldir+"\"/><p>");
	}
        catch(IOException ex) { 
		ex.printStackTrace(System.out);
	}

        writer.println("</body></html>");
        writer.close();

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
            String servletstr = host + "GetGrantsByPIServlet?Investigator=";
            // currentseries is a workaround for a limitation with JFreeChart.
            PIGraphObject obj = (PIGraphObject)someVector.elementAt(category);
            String PInumber = obj.getEmployeeNumber();
            servletstr += URLEncoder.encode(PInumber);
            servletstr += "&UnitName=";
            servletstr += UnitName;
            servletstr += "&Format=PDF";
            if (fiscalYear > 0)
            {
                servletstr += "&FiscalYear=";
                servletstr += fiscalYear;
                servletstr += "&FiscalYearType=STATE";
            }
            return servletstr;
        }
        
    }

}


