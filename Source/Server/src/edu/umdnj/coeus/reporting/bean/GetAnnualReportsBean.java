/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GetAnnualReportsBean.java,v 1.1.2.10 2007/09/18 18:23:05 cvs Exp $
 * $Log: GetAnnualReportsBean.java,v $
 * Revision 1.1.2.10  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.9  2007/09/04 14:57:03  cvs
 * Bug: Annual Report find out discrepancy with PI names and PI totals (Regeane)
 *
 * Revision 1.1.2.8  2007/08/22 14:52:40  cvs
 * Align results using comparison of actual unit names algorithm
 *
 * Revision 1.1.2.7  2007/08/20 19:48:26  cvs
 * Annual Reports: compare actual unit names rather than finding index of unit names
 *
 * Revision 1.1.2.6  2007/08/16 18:11:45  cvs
 * 1. Add support for sorting by PI and Department
 * 2. Move sorting methods to parent class
 *
 * Revision 1.1.2.5  2007/08/13 17:21:25  cvs
 * Add departmental graphs by proposal status
 *
 * Revision 1.1.2.4  2007/08/06 14:48:55  cvs
 * Use Start Date as delimiting criteria per Regeane request
 *
 * Revision 1.1.2.3  2007/08/02 19:19:05  cvs
 * Enforce Snippet integrity in PDF output
 *
 * Revision 1.1.2.2  2007/08/01 18:14:25  cvs
 * Add Annual Reports detail reports and PI reports
 *
 * Revision 1.1.2.1  2007/07/31 13:36:19  cvs
 * Add Annual Reports server-side code
 * Move many methods to base class Reporting Base Bean
 *
 *
 */
/*
 * @(#)GetAnnualReportsBean.java 
 * Copyright (c) University of Medicine and Dentistry of New Jersey
 * 1 World's Fair Drive, Somerset, New Jersey 08873
 * All rights reserved.
 * 
 * Author: Romerl Elizes
 *
 * Description: Annual Reports Bean.
 * 
 */


package edu.umdnj.coeus.reporting.bean;


import java.util.Vector;
import java.util.HashMap;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.transform.TransformerException;
import java.net.URLEncoder;
import java.io.*;

// MIT imports
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.budget.report.ReportGenerator;

// iText imports
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Image;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.fop.apps.FOPException;
//4122-upgrade stylevision start
//import org.apache.fop.render.mif.fonts.TimesBold;
//4122-upgrade stylevision end
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

public class GetAnnualReportsBean extends ReportingBaseBean
{
    private double runningTotalIndividualAward;
    private double runningTotalAward;

    private int runningTotalDirectCost;
    private int runningTotalIndirectCost;
    private int runningTotalCost;

    private int grandTotalDirectCost;
    private int grandTotalIndirectCost;
    private int grandTotalCost;

    private String UnitName = "";
    private String DeptName = "";
    

    private UMDNJSQLUtilities pobj = null;

    private Vector grantsvector = null;

    /** Creates a new instance of GetAnnualReportsBean */
    public GetAnnualReportsBean() 
    {
	super();
        fiscalYear = 0;
        Initialization();
    }
    
    public GetAnnualReportsBean(String UnitName, String FiscalYearType, String FiscalYear)
    {
        super();
        this.UnitName = UnitName;
        this.FiscalYearType = FiscalYearType;
        this.FiscalYear = FiscalYear;
        fiscalYear = 0;
        InitializeFiscalYear(FiscalYearType,FiscalYear);
        Initialization();
    }
    
    private void Initialization()
    {
        pobj = new UMDNJSQLUtilities();
    }
    
    public void getSpecificPDFOutput(HttpServletResponse res)
         throws ServletException, IOException
    {
        Vector result = pobj.getAllGrants(UnitName);
        Vector fields = pobj.getUnits(UnitName);
        grantsvector = new Vector();
        runningTotalIndividualAward = 0;
	runningTotalAward = 0;

        if (fields != null && fields.size() > 0 &&
            result != null && result.size() > 0)
        {
            DeptName = getUnitSchool(UnitName);
            if (DeptName.length() == 0)
            {
              HashMap arow = (HashMap)result.elementAt(0);
              Object str = arow.get("UNIT_NAME");
              DeptName = (String)str;
            }
            Document document = new Document(PageSize.LETTER.rotate(),36,36,36,36);
            Vector fieldVector = new Vector();
	    width = document.getPageSize().width();
            try 
            {
                HashMap row = null;
                int listSize = result.size();
                res.setContentType("application/pdf");
                PdfWriter writer = PdfWriter.getInstance(document, res.getOutputStream());
                writer.setPageEvent(this);
                                  
 	        document.open();

                for (int inum = 0; inum < listSize; inum++)
                {           
                    row = (HashMap)result.elementAt(inum);
		    GrantsObject object = populateCurrentGrantsObject(row);
	            grantsvector.addElement(object);
                }
                for (int inum = 0; inum < fields.size(); inum++)
                {
                    HashMap arow = (HashMap)fields.elementAt(inum);
                    Object arowcell = arow.get("UNIT_NAME");
                    String strtitle = (String)getCellValue(arowcell);
                    int index = strtitle.indexOf("-"+UnitName);
                    if (index > 0)
                        strtitle = strtitle.substring(0,index);                    
                    fieldVector.addElement(strtitle);
                }

                DisplayComparisonChart(fieldVector,grantsvector,document);
     		document.newPage();

                DisplaySummaryChart(grantsvector,"",document);
     		document.newPage();

                DisplayAwardsByDepartment(fieldVector,grantsvector,document);
     		document.newPage();

                DisplayAwardsByPI(grantsvector,document);
                document.close();
            }
            catch(DocumentException de) {
                de.printStackTrace();
                System.err.println("document: " + de.getMessage());
            }
            catch(IOException de) {
                de.printStackTrace();
                System.err.println("document: " + de.getMessage());
            }
       }
       else
       {
           res.setContentType("text/html");
           PrintWriter writer = res.getWriter();
           writer.println("<html><body>");
           writer.print(UnitName + " does not have grants");
           writer.println("</body></html>"); 
       }
    }

    
    private boolean handleFiscalDate(HashMap row)
    {
        boolean bfound = false;
        Object mitawardnocell = row.get("MIT_AWARD_NUMBER");
        String mitaward = getCellValue(mitawardnocell);
        if (mitaward.indexOf("-001")>-1)
            return bfound;
        
        if (fiscalYear > 0)
        {
            Object totalenddatecell = row.get("END_DATE");
            Timestamp canddate = getTimeStampCellValue(totalenddatecell);
            if (canddate != null)
            {
                if (canddate.getTime() < begindate.getTime() || 
                   canddate.getTime() > enddate.getTime())
                                bfound = true;
            }
         }
         return bfound;
    }
    
    private void DisplayComparisonChart(
                       Vector fieldVector,
                       Vector grantsvector,
                       Document document)
          throws DocumentException, IOException
    {
        int endFiscalYear = fiscalYear;
        int beginFiscalYear = fiscalYear - 2;
        if (beginFiscalYear < 1998)
           beginFiscalYear = 1998;

        int totalFiscalYears = endFiscalYear - beginFiscalYear + 1;
	Vector beginTimestamps = new Vector();
	Vector endTimestamps = new Vector();
        Vector fiscalYearStrings = new Vector();
        int currFiscalYear = beginFiscalYear;

        // Populate vectors of Fiscal Year        
        for (int inum = 0; inum < totalFiscalYears; inum++)
        {
            Timestamp begintime = getFiscalYearBeginTimestamp(currFiscalYear);
            Timestamp endtime = getFiscalYearEndTimestamp(currFiscalYear);
	    beginTimestamps.addElement(begintime);
	    endTimestamps.addElement(endtime);
            Integer iobj = new Integer(currFiscalYear);
            String strval = "FY" + iobj.toString();
            fiscalYearStrings.addElement(strval);
            currFiscalYear++;
        }

        // Create array that will be used to store comparison chart values
        double dvalue [][] = new double[totalFiscalYears][fieldVector.size()];
        for (int inum = 0; inum < totalFiscalYears; inum++)
            for (int jnum = 0; jnum < fieldVector.size(); jnum++)
                dvalue[inum][jnum] = 0;
     

       // Traverse the grants vector to place values in correct order
       for (int jnum = 0; jnum < grantsvector.size(); jnum++)
       {
           GrantsObject grantsobj = (GrantsObject)grantsvector.elementAt(jnum);
           String mitaward = grantsobj.getAccountNumber();
           if (mitaward.indexOf("-001")>-1)
              continue;

           Timestamp candtime = grantsobj.getAwardStartDateTimestamp();
           for (int inum = 0; inum < totalFiscalYears; inum++)
           {
               Timestamp begintime = (Timestamp)beginTimestamps.elementAt(inum);   
               Timestamp endtime = (Timestamp)endTimestamps.elementAt(inum);   
               if (candtime != null &&
                   candtime.getTime() >= begintime.getTime() &&
                   candtime.getTime() <= endtime.getTime())
               {
                   double val1 = grantsobj.getDirectCost();
                   double val2 = grantsobj.getIndirectCost();
                   String unitname = grantsobj.getUnitName();
                   int index = unitname.indexOf("-"+UnitName);
                   if (index > 0)
                       unitname = unitname.substring(0,index);                    
                   double bsum = val1 + val2;
    
                   for (int knum = 0; knum < fieldVector.size(); knum++)
                   {
                       String strcand = (String)fieldVector.elementAt(knum);
 
                       if (unitname.compareTo(strcand)==0)
                          dvalue [inum][knum] += bsum;
                   }
               }
           }
       }  
       // Create comparison chart based on created values
       AnnualReportsGraphObj graphObj = new AnnualReportsGraphObj();
       graphObj.setTitle("Comparison Chart of All "+DeptName+" Departments for\nFiscal Years "+beginFiscalYear+"-"+endFiscalYear);
       graphObj.saveRawImage("A",fiscalYearStrings,fieldVector,dvalue,"US Dollars","Department",true,"BAR");

       synchronized(this)
       {
                Image testimg = Image.getInstance(graphObj.getCurrentFile());
                document.add(testimg);
       }
    }

    private double DisplaySummaryChart(
                       Vector grantsvector,
                       String inunitname,
                       Document document)
          throws DocumentException, IOException
    {
        int endFiscalYear = fiscalYear;
        int beginFiscalYear = fiscalYear - 6;
        if (beginFiscalYear < 1998)
           beginFiscalYear = 1998;
        double dollarvalue = 0;

        int totalFiscalYears = endFiscalYear - beginFiscalYear + 1;
	Vector beginTimestamps = new Vector();
	Vector endTimestamps = new Vector();
        Vector fiscalYearStrings = new Vector();
        int currFiscalYear = beginFiscalYear;
        Vector fiscalYearTitle = new Vector();
        fiscalYearTitle.addElement("Fiscal Years");

        // Populate vectors of Fiscal Year        
        for (int inum = 0; inum < totalFiscalYears; inum++)
        {
            Timestamp begintime = getFiscalYearBeginTimestamp(currFiscalYear);
            Timestamp endtime = getFiscalYearEndTimestamp(currFiscalYear);
	    beginTimestamps.addElement(begintime);
	    endTimestamps.addElement(endtime);
            Integer iobj = new Integer(currFiscalYear);
            String strval = "FY" + iobj.toString();
            fiscalYearStrings.addElement(strval);
            currFiscalYear++;
        }

        // Create array that will be used to summary chart values
        double dvalue [][] = new double[1][totalFiscalYears];
        for (int inum = 0; inum < totalFiscalYears; inum++)
                dvalue[0][inum] = 0;
     
       // Traverse the grants vector to place values in correct order
       for (int jnum = 0; jnum < grantsvector.size(); jnum++)
       {
            GrantsObject grantsobj = (GrantsObject)grantsvector.elementAt(jnum);
            String mitaward = grantsobj.getAccountNumber();
            if (mitaward.indexOf("-001")>-1)
               continue;

            Timestamp candtime = grantsobj.getAwardStartDateTimestamp();
            for (int inum = 0; inum < totalFiscalYears; inum++)
            {
                Timestamp begintime = (Timestamp)beginTimestamps.elementAt(inum);   
                Timestamp endtime = (Timestamp)endTimestamps.elementAt(inum);   
                if (candtime != null &&
                    (candtime.getTime() >= begintime.getTime() &&
                    candtime.getTime() <= endtime.getTime()))
                {
                   double val1 = grantsobj.getDirectCost();
                   double val2 = grantsobj.getIndirectCost();
                   String unitname = grantsobj.getUnitName();
                   int index = unitname.indexOf("-"+UnitName);
                   if (index > 0)
                      unitname = unitname.substring(0,index);                    
                   double bsum = val1 + val2;

                   if (inunitname.length()==0)
                   {
                      dvalue[0][inum] += bsum;
                      dollarvalue += bsum;
                   }
                   else
                   if (unitname.compareTo(inunitname)==0)
                   {
                      dvalue[0][inum] += bsum;
                      dollarvalue += bsum;
                   }
                }
            }
        }

       if (dollarvalue == 0)
          return dollarvalue;

       // Create summary chart based on created values
       AnnualReportsGraphObj graphObj = new AnnualReportsGraphObj(inunitname);
       if (inunitname.length()==0)
          graphObj.setTitle(DeptName+" All Departments \nResearch Awards Total Dollars FY"+beginFiscalYear+"-FY"+endFiscalYear);
       else
          graphObj.setTitle(DeptName+" - "+inunitname+" Department\nResearch Awards Total Dollars FY"+beginFiscalYear+"-FY"+endFiscalYear);
       graphObj.saveRawImage("B",fiscalYearTitle,fiscalYearStrings,dvalue,"US Dollars","Fiscal Year",false,"BAR");

       synchronized(this)
       {
                Image testimg = Image.getInstance(graphObj.getCurrentFile());
                document.add(testimg);
       }
       return dollarvalue;
    }

    private void DisplayAwardsByDepartment(Vector fieldVector,
                              Vector grantsvector,
                              Document document)
          throws DocumentException, IOException
    {
    	grandTotalDirectCost = 0;
    	grandTotalIndirectCost = 0;
    	grandTotalCost = 0;

        // sort candvector by PI
        Vector candvector = sortIncomingVectorByPI(grantsvector);

        for (int inum = 0; inum < fieldVector.size(); inum++)
        {
            String strtitle = (String)fieldVector.elementAt(inum);
            double dollarvalue = DisplaySummaryChart(candvector,strtitle,document);
            if (dollarvalue == 0) continue;
            if (inum < fieldVector.size()-1)
               document.newPage();
            DisplayDetailReport(grantsvector,strtitle,document);
            if (inum < fieldVector.size()-1)
               document.newPage();
        }
        document.newPage();
        displayDDRGrandTotals(document);
    }


    private void DisplayDetailReport(
                       Vector grantsvector,
                       String inunitname,
                       Document document)
          throws DocumentException, IOException
    {
       PdfPTable table  = new PdfPTable(9);
       float[] widths = {0.1f, 0.15f, 0.15f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
       double dollarvalues = 0;
       table.setWidths(widths);
       table.getDefaultCell().setBorder(0);
       table.setHorizontalAlignment(0);
       table.setTotalWidth(width - 72);
       table.setLockedWidth(true);
       table.setHeaderRows(5);
       FormatDDRHeader(table,inunitname);

       runningTotalDirectCost = 0;
       runningTotalIndirectCost = 0;
       runningTotalCost = 0;

       Vector candvector = sortIncomingVectorByPI(grantsvector);

       for (int jnum = 0; jnum < candvector.size(); jnum++)
       {
            GrantsObject grantsobj = (GrantsObject)candvector.elementAt(jnum);

            String unitname = grantsobj.getUnitName();
            int index = unitname.indexOf("-"+UnitName);
            if (index > 0)
                unitname = unitname.substring(0,index);                    

            if (unitname.compareTo(inunitname)!=0)
               continue;

            String mitaward = grantsobj.getAccountNumber();
            if (mitaward.indexOf("-001")>-1)
               continue;

            Timestamp candtime = grantsobj.getAwardStartDateTimestamp();
            Timestamp begintime = getFiscalYearBeginTimestamp(fiscalYear);
            Timestamp endtime = getFiscalYearEndTimestamp(fiscalYear);
            if (candtime != null &&
                (candtime.getTime() >= begintime.getTime() &&
                candtime.getTime() <= endtime.getTime()))
            {
                displayDDRGrantsObjContents(table,grantsobj);            
            }
       }
       displayDDRTotals(table);
       document.add(table);
   }

    private void FormatDDRHeader(PdfPTable table, String inunitname)
    	throws DocumentException
    {
        String strhead = DeptName + " Fiscal Year " + fiscalYear + " Awards";
        FormatHeaderCell(table,strhead,Color.white,Color.white,Element.ALIGN_LEFT,12,9);

        FormatHeaderCell(table,"",Color.white,Color.black,Element.ALIGN_LEFT,9,9);

        FormatHeaderCell(table,"ORSP Number\n",Color.white,Color.white,Element.ALIGN_LEFT,9,1);
        FormatHeaderCell(table,"PI Name\nProject Title",Color.white,Color.white,Element.ALIGN_LEFT,9,1);
        FormatHeaderCell(table,"Sponsor Name",Color.white,Color.white,Element.ALIGN_LEFT,9,2);
        FormatHeaderCell(table,"Award Period\nStart Date",Color.white,Color.white,Element.ALIGN_CENTER,9,1);
        FormatHeaderCell(table,"Award Period\nEnd Date",Color.white,Color.white,Element.ALIGN_CENTER,9,1);
        FormatHeaderCell(table,"Award Period\nDirect Cost",Color.white,Color.white,Element.ALIGN_CENTER,9,1);
        FormatHeaderCell(table,"Award Period\nIndirect Cost",Color.white,Color.white,Element.ALIGN_CENTER,9,1);
        FormatHeaderCell(table,"Award Period\nTotal Cost",Color.white,Color.white,Element.ALIGN_CENTER,9,1);

        FormatHeaderCell(table,"",Color.white,Color.black,Element.ALIGN_LEFT,9,9);

        if (inunitname.length() > 0)
        {
           FormatDataCell(table,"Department:",Color.white,Color.white,Element.ALIGN_LEFT,Font.BOLD,9,1);
           FormatDataCell(table,inunitname,Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,9,8);
        }
        FormatDataCell(table," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,12,9);
    }

    private void displayDDRGrantsObjContents(PdfPTable table,GrantsObject grantsobj)
    	throws DocumentException
    {
        if (grantsobj.getAwardBeginDate().length()==0 && 
                grantsobj.getAwardEndDate().length()==0)
            return;

        runningTotalDirectCost += grantsobj.getDirectCost();
        runningTotalIndirectCost += grantsobj.getIndirectCost();
        double currentTotalCost = grantsobj.getDirectCost() + grantsobj.getIndirectCost();

    	grandTotalDirectCost += grantsobj.getDirectCost();
    	grandTotalIndirectCost += grantsobj.getIndirectCost();
    	grandTotalCost += currentTotalCost;

        runningTotalCost += grantsobj.getDirectCost() + grantsobj.getIndirectCost();

        PdfPTable subtable = new PdfPTable(9);		
        float[] widths = {0.1f, 0.15f, 0.15f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
        subtable.setWidths(widths);
        subtable.getDefaultCell().setBorder(0);
        subtable.setHorizontalAlignment(0);
        subtable.setTotalWidth(width - 72);
        subtable.setLockedWidth(true);

        int pdfcellfontsize = 9;

	String accountnumber = grantsobj.getAccountNumber();
        int dashindex = accountnumber.indexOf('-');
        if (dashindex > -1)
           accountnumber = accountnumber.substring(0,dashindex);

        FormatDataCell(subtable,accountnumber,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,pdfcellfontsize,1);
        FormatDataCell(subtable,grantsobj.getInvestigatorName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,pdfcellfontsize,1);
        FormatDataCell(subtable,grantsobj.getSponsorName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,pdfcellfontsize,2);
        FormatDataCell(subtable,grantsobj.getAwardBeginDate(),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,pdfcellfontsize,1);
        FormatDataCell(subtable,grantsobj.getAwardEndDate(),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,pdfcellfontsize,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getDirectCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,pdfcellfontsize,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getIndirectCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,pdfcellfontsize,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(currentTotalCost),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,pdfcellfontsize,1);

        FormatDataCell(subtable,"",Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,pdfcellfontsize,1);
        FormatDataCell(subtable,grantsobj.getProjectTitle(),Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,pdfcellfontsize,8);
        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,9);


        PdfPCell cell11 = new PdfPCell(subtable);
        cell11.setBorderColor(Color.white);
        cell11.setColspan(9);
        cell11.setNoWrap(true);
        table.addCell(cell11);
    }

    private void displayDDRTotals(PdfPTable table)
    {
        Paragraph a = new Paragraph();
        a.add(new Chunk("",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setBorderColor(Color.white);
        cell1.setColspan(6);
        cell1.setNoWrap(true);
        table.addCell(cell1);

        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setBorderColor(Color.white);
        cell2.setColspan(3);
        cell2.setNoWrap(true);
        table.addCell(cell2);

        Paragraph c = new Paragraph();
        a.add(new Chunk(" ",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell3 = new PdfPCell(c);
        cell3.setBorderColor(Color.white);
        cell3.setColspan(6);
        cell3.setNoWrap(true);
        table.addCell(cell3);

        Paragraph e = new Paragraph();
        e.add(new Chunk(getCurrencyFormattedNumber(runningTotalDirectCost),
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell5 = new PdfPCell(e);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell5.setBorderColor(Color.white);
        cell5.setBackgroundColor(Color.lightGray);
        cell5.setNoWrap(true);
        table.addCell(cell5);

        Paragraph f = new Paragraph();
        f.add(new Chunk(getCurrencyFormattedNumber(runningTotalIndirectCost),
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell6 = new PdfPCell(f);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell6.setBorderColor(Color.white);
        cell6.setBackgroundColor(Color.lightGray);
        cell6.setNoWrap(true);
        table.addCell(cell6);

        Paragraph g = new Paragraph();
        g.add(new Chunk(getCurrencyFormattedNumber(runningTotalCost),
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell7 = new PdfPCell(g);
        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell7.setBorderColor(Color.white);
        cell7.setBackgroundColor(Color.lightGray);
        cell7.setNoWrap(true);
        table.addCell(cell7);
    }

    private void displayDDRGrandTotals(Document document)
    	throws DocumentException
    {
        PdfPTable table  = new PdfPTable(9);
        float[] widths = {0.1f, 0.15f, 0.15f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
        double dollarvalues = 0;
        table.setWidths(widths);
        table.getDefaultCell().setBorder(0);
        table.setHorizontalAlignment(0);
        table.setTotalWidth(width - 72);
        table.setLockedWidth(true);
        table.setHeaderRows(5);
        FormatDDRHeader(table,"");

        int pdffontsize = 10;
        Paragraph a = new Paragraph();
        a.add(new Chunk("Grand Total",
        		FontFactory.getFont(FontFactory.HELVETICA, pdffontsize, Font.BOLD, Color.BLACK)));
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setBorderColor(Color.white);
        cell1.setColspan(6);
        cell1.setNoWrap(true);
        table.addCell(cell1);

        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, pdffontsize, Font.PLAIN, Color.BLACK)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setBorderColor(Color.white);
        cell2.setColspan(3);
        cell2.setNoWrap(true);
        table.addCell(cell2);

        Paragraph c = new Paragraph();
        a.add(new Chunk(" ",
        		FontFactory.getFont(FontFactory.HELVETICA, pdffontsize, Font.PLAIN, Color.BLACK)));
        PdfPCell cell3 = new PdfPCell(c);
        cell3.setBorderColor(Color.white);
        cell3.setColspan(6);
        cell3.setNoWrap(true);
        table.addCell(cell3);

        Paragraph e = new Paragraph();
        e.add(new Chunk(getCurrencyFormattedNumber(grandTotalDirectCost),
        		FontFactory.getFont(FontFactory.HELVETICA, pdffontsize, Font.PLAIN, Color.BLACK)));
        PdfPCell cell5 = new PdfPCell(e);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell5.setBorderColor(Color.white);
        cell5.setBackgroundColor(Color.lightGray);
        cell5.setNoWrap(true);
        table.addCell(cell5);

        Paragraph f = new Paragraph();
        f.add(new Chunk(getCurrencyFormattedNumber(grandTotalIndirectCost),
        		FontFactory.getFont(FontFactory.HELVETICA, pdffontsize, Font.PLAIN, Color.BLACK)));
        PdfPCell cell6 = new PdfPCell(f);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell6.setBorderColor(Color.white);
        cell6.setBackgroundColor(Color.lightGray);
        cell6.setNoWrap(true);
        table.addCell(cell6);

        Paragraph g = new Paragraph();
        g.add(new Chunk(getCurrencyFormattedNumber(grandTotalCost),
        		FontFactory.getFont(FontFactory.HELVETICA, pdffontsize, Font.PLAIN, Color.BLACK)));
        PdfPCell cell7 = new PdfPCell(g);
        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell7.setBorderColor(Color.white);
        cell7.setBackgroundColor(Color.lightGray);
        cell7.setNoWrap(true);
        table.addCell(cell7);

        document.add(table);
    }



    private void DisplayAwardsByPI(
                              Vector grantsvector,
                              Document document)
          throws DocumentException, IOException
    {
       // traverse grantsvector to get rid of non-related fiscal years.
       Vector candvector = new Vector();
       for (int jnum = 0; jnum < grantsvector.size(); jnum++)
       {
            GrantsObject grantsobj = (GrantsObject)grantsvector.elementAt(jnum);

            String mitaward = grantsobj.getAccountNumber();
            if (mitaward.indexOf("-001")>-1)
               continue;

            Timestamp candtime = grantsobj.getAwardStartDateTimestamp();
            Timestamp begintime = getFiscalYearBeginTimestamp(fiscalYear);
            Timestamp endtime = getFiscalYearEndTimestamp(fiscalYear);
            if (candtime != null &&
                (candtime.getTime() >= begintime.getTime() &&
                candtime.getTime() <= endtime.getTime()))
            {
                candvector.addElement(grantsobj);
            }
       }

       // sort candvector by PI
       candvector = sortIncomingVectorByPI(candvector);

       PdfPTable table  = new PdfPTable(7);
       float[] widths = { 0.15f,0.15f,0.1f,0.1f,0.1f,0.1f,0.1f };
       table.setWidths(widths);
       table.getDefaultCell().setBorder(0);
       table.setHorizontalAlignment(0);
       table.setTotalWidth(width - 72);
       table.setLockedWidth(true);
       table.setHeaderRows(5);
       FormatDPIHeader(table);

       int numawards = 0;
       Vector tempVector = new Vector();

       int pichanged = 0;

       for (int inum = 0; inum < candvector.size(); inum++)
       {
           GrantsObject obj1 =  (GrantsObject)candvector.elementAt(inum);
           GrantsObject obj2 = null;
           String currPI = obj1.getInvestigatorName().trim();
           String prevPI = "";
           if (inum > 0)
           {
              obj2 =  (GrantsObject)candvector.elementAt(inum-1);
              prevPI = obj2.getInvestigatorName();
           }
           else
              prevPI = obj1.getInvestigatorName().trim();

           if (currPI.compareTo(prevPI)==0)
              pichanged = 0;  
           else
           {
              pichanged++;
              if (tempVector.size() > 0)
              {
                 displayDPIContents(table,tempVector);
                 tempVector = new Vector();
              }
           }
           tempVector.addElement(obj1);
       }
       if (pichanged == 0 && tempVector.size() > 0)
          displayDPIContents(table,tempVector);
       document.add(table);
    }

    private void displayDPIContents(PdfPTable table,Vector tempVector)
    	throws DocumentException
    {
       double totaldirectcost = 0;      
       double totalindirectcost = 0;      
       double totalcost = 0;

       PdfPTable subtable  = new PdfPTable(7);
       float[] widths = { 0.15f,0.15f,0.1f,0.1f,0.1f,0.1f,0.1f };
       subtable.setWidths(widths);
       subtable.getDefaultCell().setBorder(0);
       subtable.setHorizontalAlignment(0);
       subtable.setTotalWidth(width - 72);
       subtable.setLockedWidth(true);

       int pdfcellfontsize = 9;
       String investigatorname = "";
       
       for (int inum = 0; inum < tempVector.size(); inum++)
       {
           GrantsObject grantsobj = (GrantsObject)tempVector.elementAt(inum);
           if (inum == 0) 
           {
              FormatDataCell(subtable,grantsobj.getInvestigatorName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,pdfcellfontsize,7);
              investigatorname = grantsobj.getInvestigatorName();
           }
           FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,pdfcellfontsize,7);
           FormatDataCell(subtable,grantsobj.getSponsorName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,pdfcellfontsize,1);
           FormatDataCell(subtable,grantsobj.getUnitName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,pdfcellfontsize,1);
           FormatDataCell(subtable,grantsobj.getAwardBeginDate(),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,pdfcellfontsize,1);
           FormatDataCell(subtable,grantsobj.getAwardEndDate(),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,pdfcellfontsize,1);
           FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getDirectCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,pdfcellfontsize,1);
           FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getIndirectCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,pdfcellfontsize,1);
           double currentTotalCost = grantsobj.getDirectCost()+grantsobj.getIndirectCost();
           FormatDataCell(subtable,getCurrencyFormattedNumber(currentTotalCost),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,pdfcellfontsize,1);
           totaldirectcost += grantsobj.getDirectCost();      
           totalindirectcost += grantsobj.getIndirectCost();      
           totalcost += currentTotalCost;
       }

       String summarytitle = "Summary for ";
       summarytitle += investigatorname;
       summarytitle += "(";
       summarytitle += tempVector.size();
       summarytitle += " detail record";
       if (tempVector.size()>1) summarytitle += "s";
       summarytitle += ")";

       FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,pdfcellfontsize,7);
       FormatDataCell(subtable,summarytitle,Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,pdfcellfontsize-1,7);
       FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,pdfcellfontsize,3);
       FormatDataCell(subtable,"Sum",Color.white,Color.white,Element.ALIGN_LEFT,Font.BOLD,pdfcellfontsize,1);
       FormatDataCell(subtable,getCurrencyFormattedNumber(totaldirectcost),Color.white,Color.white,Element.ALIGN_CENTER,Font.BOLD,pdfcellfontsize,1);
       FormatDataCell(subtable,getCurrencyFormattedNumber(totalindirectcost),Color.white,Color.white,Element.ALIGN_CENTER,Font.BOLD,pdfcellfontsize,1);
       FormatDataCell(subtable,getCurrencyFormattedNumber(totalcost),Color.white,Color.white,Element.ALIGN_CENTER,Font.BOLD,pdfcellfontsize,1);
       FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,pdfcellfontsize,7);
       
       PdfPCell cell11 = new PdfPCell(subtable);
       cell11.setBorderColor(Color.white);
       cell11.setColspan(7);
       cell11.setNoWrap(true);
       table.addCell(cell11);
    }

    private void FormatDPIHeader(PdfPTable table)
    	throws DocumentException
    {
        String strhead = "Fiscal Year " + fiscalYear + "\nPI in Alphabetical Order";
        FormatHeaderCell(table,strhead,Color.white,Color.white,Element.ALIGN_LEFT,12,7);

        FormatHeaderCell(table,"",Color.white,Color.black,Element.ALIGN_LEFT,9,7);

        FormatHeaderCell(table,"Sponsor Name\nProject Title",Color.white,Color.white,Element.ALIGN_LEFT,9,1);
        FormatHeaderCell(table,"Department",Color.white,Color.white,Element.ALIGN_LEFT,9,1);
        FormatHeaderCell(table,"Start Date",Color.white,Color.white,Element.ALIGN_CENTER,9,1);
        FormatHeaderCell(table,"End Date",Color.white,Color.white,Element.ALIGN_CENTER,9,1);
        FormatHeaderCell(table,"Direct Cost",Color.white,Color.white,Element.ALIGN_CENTER,9,1);
        FormatHeaderCell(table,"Indirect Cost",Color.white,Color.white,Element.ALIGN_CENTER,9,1);
        FormatHeaderCell(table,"Total Cost",Color.white,Color.white,Element.ALIGN_CENTER,9,1);

        FormatHeaderCell(table,"",Color.white,Color.black,Element.ALIGN_LEFT,9,7);

        FormatDataCell(table," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,12,7);
    }


    ////////////////////////////////////////////////////////////////////////
    // METHOD TO POPULATE TEMPORARY GRANTS OBJECT
    private GrantsObject populateCurrentGrantsObject(HashMap row)
    {
     		GrantsObject grantsobj = new GrantsObject();

        	Object mitawardnocell = row.get("MIT_AWARD_NUMBER");
		grantsobj.setAccountNumber(getCellValue(mitawardnocell).trim());

        	Object unitnamecell = row.get("UNIT_NAME");
		grantsobj.setUnitName(getCellValue(unitnamecell).trim());

		Object investigatorcell = row.get("PERSON_NAME");
		grantsobj.setInvestigatorName(getCellValue(investigatorcell));

		Object titlecell = row.get("TITLE");
		grantsobj.setProjectTitle(getCellValue(titlecell));

		Object begindatecell = row.get("CURRENT_FUND_EFFECTIVE_DATE");
		grantsobj.setBeginProjectDate(getCellValue(begindatecell).trim());

		Object enddatecell = row.get("OBLIGATION_EXPIRATION_DATE");
		grantsobj.setEndProjectDate(getCellValue(enddatecell).trim());
		
		Object totalamountcell = row.get("ANTICIPATED_TOTAL_AMOUNT");
		grantsobj.setProjectTotalCost(getNumericCellValue(totalamountcell));

		Object sponsorcell = row.get("SPONSOR_NAME");
		grantsobj.setSponsorName(getCellValue(sponsorcell).trim());

		Object sponsortypecell = row.get("SPONSOR_TYPE_DESCRIPTION");
		grantsobj.setSponsorTypeDescription(getCellValue(sponsortypecell).trim());

		Object activitytypecell = row.get("ACTIVITY_TYPE_DESCRIPTION");
		grantsobj.setActivityTypeDescription(getCellValue(activitytypecell).trim());

		Object awardtypecell = row.get("AWARD_TYPE_DESCRIPTION");
		grantsobj.setAwardTypeDescription(getCellValue(awardtypecell).trim());

		Object totalstartdatecell = row.get("START_DATE");
                Timestamp canddate1 = getTimeStampCellValue(totalstartdatecell);
                grantsobj.setAwardBeginDate(getCellValue(totalstartdatecell).trim());
		grantsobj.setAwardStartDateTimestamp(canddate1);    	

		Object totalenddatecell = row.get("END_DATE");
                Timestamp canddate2 = getTimeStampCellValue(totalenddatecell);
		grantsobj.setAwardEndDate(getCellValue(totalenddatecell).trim());    	
		grantsobj.setAwardEndDateTimestamp(canddate2);    	

		Object directcostcell = row.get("DIRECT_COST");
		double val1 = getNumericCellValue(directcostcell);
		grantsobj.addDirectCost(val1);
		
		Object indirectcostcell = row.get("INDIRECT_COST");
		double val2 = getNumericCellValue(indirectcostcell);
		grantsobj.addIndirectCost(val2);

		return grantsobj;
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

    public void onEndPage(PdfWriter writer, Document document) 
    {
   	   float width = document.getPageSize().width();
           float[] widths = {0.5f, 0.5f};

           try {
               Rectangle page = document.getPageSize();
               PdfPTable foot = new PdfPTable(2);
               foot.setWidths(widths);
               foot.getDefaultCell().setBorder(0);
               foot.setHorizontalAlignment(0);
               foot.setTotalWidth(width - 72);
               foot.setLockedWidth(true);
	   
               Color backgroundcolor = Color.black;
               FormatSubfooterCell(foot,"",backgroundcolor,1,Element.ALIGN_LEFT);
               FormatSubfooterCell(foot,"",backgroundcolor,1,Element.ALIGN_LEFT);

               java.util.Date currdate = new java.util.Date();
               SimpleDateFormat formatter = new SimpleDateFormat("EEEEEEEEE,MMMMMMMMMMMMM dd, yyyy");
               String strcurdate = formatter.format(currdate);

               int iIndex = writer.getPageNumber();
               backgroundcolor = Color.white;
               FormatSubfooterCell(foot,strcurdate,backgroundcolor,1,Element.ALIGN_LEFT);
               FormatSubfooterCell(foot,iIndex+" ",backgroundcolor,1,Element.ALIGN_RIGHT);

               foot.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(),
                  writer.getDirectContent());
           }
           catch (Exception e) {
               throw new ExceptionConverter(e);
           }
    }

}


