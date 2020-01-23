/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GetSummaryGrantsBean.java,v 1.1.2.12 2007/09/18 18:23:05 cvs Exp $
 * $Log: GetSummaryGrantsBean.java,v $
 * Revision 1.1.2.12  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.11  2007/09/06 19:29:45  cvs
 * Fiscal Year Delineation: tweak server-side code to reflect GUI changes
 *
 * Revision 1.1.2.10  2007/09/05 19:08:58  cvs
 * Server-side modifications for Fiscal Year Delineation
 *
 * Revision 1.1.2.9  2007/08/02 19:19:05  cvs
 * Enforce Snippet integrity in PDF output
 *
 * Revision 1.1.2.8  2007/07/31 13:36:19  cvs
 * Add Annual Reports server-side code
 * Move many methods to base class Reporting Base Bean
 *
 * Revision 1.1.2.7  2007/07/02 17:26:22  cvs
 * Correct count anomaly with fiscal year-related issues.
 *
 * Revision 1.1.2.6  2007/03/27 15:19:20  cvs
 * Modify code to work with Award FnA Distributions
 *
 * Revision 1.1.2.5  2007/03/16 14:22:19  cvs
 * Fix bug on coeus url from Summary of Active Grants drill down
 *
 * Revision 1.1.2.4  2007/02/22 20:05:23  cvs
 * Add support for drilldowns to Reports of Awards by Department
 *
 * Revision 1.1.2.3  2007/02/16 19:32:14  cvs
 * Correct misspelling of string Extramural
 *
 * Revision 1.1.2.2  2007/01/22 23:07:33  cvs
 * Changed Anticipated Amounts to Obligated Amounts
 * Support Fiscal Year
 *
 * Revision 1.1.2.1  2007/01/19 19:33:22  cvs
 * Add Summary Grants support
 *
 *
 */
/*
 * @(#)GetSummaryGrantsBean.java 
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
import com.lowagie.text.Paragraph;
import com.lowagie.text.PageSize;
import com.lowagie.text.Image;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.fop.apps.FOPException;

// Case 4122: Upgrade Stylevision  - Start
//import org.apache.fop.render.mif.fonts.TimesBold;
// Case 4122: Upgrade Stylevision  - End

import org.apache.poi.hssf.usermodel.*;

import edu.umdnj.coeus.reporting.bean.UMDNJSQLUtilities;

//import org.apache.poi.hssf.model.Workbook;

public class GetSummaryGrantsBean extends ReportingBaseBean
{
    private double runningTotalIndividualAward;
    private double runningTotalAward;

    private String UnitName = "";
    
    private UMDNJSQLUtilities pobj = null;

    private Vector grantsvector = null;

    /** Creates a new instance of GetSummaryGrantsBean */
    public GetSummaryGrantsBean() 
    {
	super();
        fiscalYear = 0;
        Initialization();
    }
    
    public GetSummaryGrantsBean(String UnitName, String FiscalYearType, String FiscalYear)
    {
        super();
        this.UnitName = UnitName;
        this.FiscalYearType = FiscalYearType;
        fiscalYear = 0;
        if (FiscalYear != null && FiscalYear.length() > 0)
            InitializeFiscalYear(FiscalYearType,FiscalYear);
        Initialization();
    }
    
    private void Initialization()
    {
        pobj = new UMDNJSQLUtilities();
    }
    
    public String getSpecificHTMLOutput()
    {
        return null;
        
    }
    
    
    public void getSpecificPDFOutput(HttpServletResponse res)
         throws ServletException, IOException
    {
        Vector result = pobj.getSummaryGrants(UnitName);
        grantsvector = new Vector();
        runningTotalIndividualAward = 0;
	runningTotalAward = 0;

        if (result != null && result.size() > 0)
        {
            String UnitSchool = getUnitSchool(UnitName);
            Document document = new Document(PageSize.LETTER);
	    width = document.getPageSize().width();
            try 
            {
                HashMap row = null;
                int listSize = 0;
                if (result != null)
                    listSize = result.size();
                if (listSize > 0)
                {
                    res.setContentType("application/pdf");
                    PdfWriter.getInstance(document, res.getOutputStream());
                    
 	            document.open();
                    PdfPTable maintable  = new PdfPTable(2);
	            maintable.getDefaultCell().setBorder(0);
	            maintable.setHorizontalAlignment(0);
	            maintable.setTotalWidth(width - 72);
	            maintable.setLockedWidth(true);

                    for (int inum = 0; inum < listSize; inum++)
                    {           
                        row = (HashMap)result.elementAt(inum);
                        if (handleFiscalDate(row)==true)
                            continue;                        
		        GrantsObject object = populateCurrentGrantsObject(row);
	                grantsvector.addElement(object);
                    }
 	            FormatHeader(maintable);
                    FormatLeftSideComponent(maintable,grantsvector);
                    FormatRightSideComponent(maintable,grantsvector);
                    document.add(maintable);  
                }
            }
            catch(DocumentException de) {
                de.printStackTrace();
                System.err.println("document: " + de.getMessage());
            }
            document.close();
       }
       else
       {
           res.setContentType("text/html");
           PrintWriter writer = res.getWriter();
           writer.println("<html><body>");
           String UnitSchool = getUnitSchool(UnitName);
           writer.print(UnitSchool + " does not have grants");
           writer.println("</body></html>"); 
       }
    }

    ////////////////////////////////////////////////////////////////////////
    // METHOD TO CREATE PDF HEADER INFORMATION
    private void FormatHeader(PdfPTable table)
    	throws DocumentException
    {
        String UnitSchool = getUnitSchool(UnitName);
        Paragraph a = new Paragraph();
        
        String strtitle = "\n\n\n"+UnitSchool+"\n\nActive Extramural Grants and Contracts\n\nSummary Listing";
        if (fiscalYear > 0)
        {
            String stryear = " for Fiscal Year " + fiscalYear;
            strtitle += stryear;
        }
        strtitle += "\n\n\n\n";
        a.add(new Chunk(strtitle,
        		FontFactory.getFont(FontFactory.TIMES, 14, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setColspan(2);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setNoWrap(true);
        cell1.setBorderColor(Color.white);
        table.addCell(cell1);
        
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 14, Font.BOLD, Color.WHITE)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(2);
        cell2.setNoWrap(true);
        cell2.setBorderColor(Color.WHITE);
        cell2.setBackgroundColor(Color.WHITE);
        table.addCell(cell2);
        
    }
    
    private void FormatLeftSideComponent(PdfPTable maintable, Vector grantsvector)
    	throws DocumentException
    {
       PdfPTable table  = new PdfPTable(2);
       table.setHorizontalAlignment(0);
       table.setTotalWidth(260);
       table.setLockedWidth(true);
       FormatDepartmentHeader(table);
       int listSize = grantsvector.size();
       int ititlechanged = 0;
       runningTotalAward = 0;
       runningTotalIndividualAward = 0;
       
       for (int num = 0; num < listSize; num++)
       {
           GrantsObject obj = (GrantsObject)grantsvector.elementAt(num);
           String mit_award_number = obj.getAccountNumber();
           double totalcost = 0;
           double directcost = obj.getDirectCost();
           double indirectcost = obj.getIndirectCost();
           totalcost = directcost + indirectcost;

           runningTotalAward += totalcost;
           GrantsObject prevobj = null;
           if (num > 0)
              prevobj = (GrantsObject)grantsvector.elementAt(num-1);
           String strtitle, prevstrtitle = "";
           strtitle = obj.getUnitName();
           if (prevobj != null)
               prevstrtitle = prevobj.getUnitName();
           else
               prevstrtitle = strtitle;

           runningTotalIndividualAward += totalcost;

           if (num == 0 || strtitle.compareTo(prevstrtitle)!=0)
           {
              if (num != 0)
              {
                 ititlechanged++;
                 displayDepartmentTotals(table,prevstrtitle,runningTotalIndividualAward);
	         runningTotalIndividualAward = 0;
              }
           }
           if (num == listSize-1)
                 displayDepartmentTotals(table,prevstrtitle,runningTotalIndividualAward);
       }
       // There is only one department
       if (ititlechanged==0)
       {
          GrantsObject obj = (GrantsObject)grantsvector.elementAt(0);
          String strtitle = obj.getUnitName();
          displayDepartmentTotals(table,strtitle,runningTotalIndividualAward);
       }
       displayDepartmentFinalTotals(table,runningTotalAward);

       PdfPCell cell1a = new PdfPCell(table);
       cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
       cell1a.setNoWrap(true);
       cell1a.setBorderColor(Color.white);
       maintable.addCell(cell1a);
    }

    private void FormatDepartmentHeader(PdfPTable table)
    	throws DocumentException
    {
        Paragraph a1 = new Paragraph();
        a1.add(new Chunk("Department",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(true);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        a2.add(new Chunk("Amount",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
        
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.WHITE)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(2);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.WHITE);
        table.addCell(cell2);
    }

    private void displayDepartmentTotals(PdfPTable table,String strtitle, double runningTotalIndividualAward)
    	throws DocumentException
    {
    	int index = strtitle.indexOf("-"+UnitName);
    	if (index > 0)
    		strtitle = strtitle.substring(0,index);

        String host = "";
        try {
            host = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL);
        }
        catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
        if (host == null)
           host = "/coeus/";

        Paragraph a1 = new Paragraph();
        Chunk achunk = new Chunk(strtitle, FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK));
        StringBuffer strbufurl = new StringBuffer(host+"GetGrantsByDeptServlet?UnitName=");
        strbufurl.append(UnitName);
        strbufurl.append("&DeptName=");
        strbufurl.append(URLEncoder.encode(strtitle));
        strbufurl.append("&Format=PDF");
        if (fiscalYear > 0)
        {
            strbufurl.append("&FiscalYear=");
            strbufurl.append(fiscalYear);
            strbufurl.append("&FiscalYearType=");
            strbufurl.append(FiscalYearType);
        }
        achunk.setAnchor(strbufurl.toString());
        a1.add(achunk);                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(false);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        a2.add(new Chunk(getCurrencyFormattedNumber(runningTotalIndividualAward),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
    }

    private void displayDepartmentFinalTotals(PdfPTable table, double runningTotalAward)
    	throws DocumentException
    {
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.WHITE)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(2);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.WHITE);
        table.addCell(cell2);

        Paragraph a1 = new Paragraph();
        a1.add(new Chunk("Total",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(true);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        a2.add(new Chunk(getCurrencyFormattedNumber(runningTotalAward),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
        
    }

    private void FormatRightSideComponent(PdfPTable maintable, Vector grantsvector)
    	throws DocumentException
    {
        PdfPTable rightmaintable  = new PdfPTable(1);
        rightmaintable.setHorizontalAlignment(0);
        rightmaintable.setTotalWidth(260);
        rightmaintable.setLockedWidth(true);

        FormatClassificationTable(rightmaintable,grantsvector);
        FormatFederalTable(rightmaintable,grantsvector);
        FormatNonFederalTable(rightmaintable,grantsvector);

        maintable.addCell(rightmaintable);
    }

    private void FormatClassificationTable(PdfPTable rightmaintable,Vector grantsvector)
    	throws DocumentException
    {
       runningTotalIndividualAward = 0;
       runningTotalAward = 0;

       PdfPTable table  = new PdfPTable(3);
       table.setHorizontalAlignment(0);
       table.setTotalWidth(260);
       table.setLockedWidth(true);
       FormatClassificationHeader(table);
       int listSize = grantsvector.size();

       int iClinicalTrial, iResearch, iService, iTraining;
       double dClinicalTrial, dResearch, dService, dTraining;
       int subsize = 0;

       iClinicalTrial = iResearch = iService = iTraining = 0;
       dClinicalTrial = dResearch = dService = dTraining = 0;

       for (int num = 0; num < listSize; num++)
       {
           GrantsObject obj = (GrantsObject)grantsvector.elementAt(num);

           String activity = obj.getActivityTypeDescription();
           String accountNumber = obj.getAccountNumber();
           double totalcost = 0;
           boolean bfiscaldate = false;

           totalcost = obj.getDirectCost() + obj.getIndirectCost();
           if (fiscalYear > 0)
           {
              Timestamp canddate = obj.getAwardStartDateTimestamp();
              if (canddate != null)
              {
                if (canddate.getTime() >= begindate.getTime() || 
                   canddate.getTime() <= enddate.getTime())
                                bfiscaldate = true;
              }
           }

           if (activity.indexOf("Organized Research")>-1)
           {
              if (bfiscaldate == true)
              {
                 iResearch++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iResearch++;
                    subsize++;
                 }
              }
              else
              {
                 dResearch += totalcost;
                 runningTotalAward += totalcost;
              }
           }
           else
           if (activity.indexOf("Clinical Trial")>-1)
           {
              if (bfiscaldate == true)
              {
                 iClinicalTrial++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iClinicalTrial++;
                    subsize++;
                 }
              }
              else
              {
                 dClinicalTrial += totalcost;
                 runningTotalAward += totalcost;
              }
           }
           else
           if (activity.indexOf("Instruction")>-1)
           {
              if (bfiscaldate == true)
              {
                 iTraining++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iTraining++;
                    subsize++;
                 }
              }
              else
              {
                 dTraining += totalcost;
                 runningTotalAward += totalcost;
              }
           }
           else
           {
              if (bfiscaldate == true)
              {
                 iService++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iService++;
                    subsize++;
                 }
              }
              else
              {
                 dService += totalcost;
                 runningTotalAward += totalcost;
              }
           }
 
       }
       displayClassificationTotals(table,"Clinical Trial",iClinicalTrial,dClinicalTrial);
       displayClassificationTotals(table,"Research Grants/R & D",iResearch,dResearch);
       displayClassificationTotals(table,"Service",iService,dService);
       displayClassificationTotals(table,"Training",iTraining,dTraining);

       displayClassificationFinalTotals(table,subsize,runningTotalAward);

       PdfPCell cell1a = new PdfPCell(table);
       cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
       cell1a.setNoWrap(true);
       cell1a.setBorderColor(Color.white);
       rightmaintable.addCell(cell1a);
    }

    private void FormatClassificationHeader(PdfPTable table)
    	throws DocumentException
    {
        Paragraph a1 = new Paragraph();
        a1.add(new Chunk("Classification",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(true);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        a2.add(new Chunk("Count",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
        
        Paragraph a3 = new Paragraph();
        a3.add(new Chunk("Total",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1c = new PdfPCell(a3);
        cell1c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1c.setNoWrap(true);
        cell1c.setBorderColor(Color.white);
        table.addCell(cell1c);
        
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.WHITE)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(3);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.WHITE);
        table.addCell(cell2);
    }

    private void displayClassificationTotals(PdfPTable table, String strtitle, int iCount, double dTotal)
    	throws DocumentException
    {
        Paragraph a1 = new Paragraph();
        a1.add(new Chunk(strtitle,
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(true);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        StringBuffer strbuf = new StringBuffer("");
        strbuf.append(iCount);
        a2.add(new Chunk(strbuf.toString(),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
        
        Paragraph a3 = new Paragraph();
        a3.add(new Chunk(getCurrencyFormattedNumber(dTotal),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cell1c = new PdfPCell(a3);
        cell1c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1c.setNoWrap(true);
        cell1c.setBorderColor(Color.white);
        table.addCell(cell1c);
    }

    private void displayClassificationFinalTotals(PdfPTable table, int itotal, double dtotal)
    	throws DocumentException
    {
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.WHITE)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(3);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.WHITE);
        table.addCell(cell2);

        Paragraph a1 = new Paragraph();
        a1.add(new Chunk("Total",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(true);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        StringBuffer strbuf = new StringBuffer("");
        strbuf.append(itotal);
        a2.add(new Chunk(strbuf.toString(),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
        
        Paragraph a3 = new Paragraph();
        a3.add(new Chunk(getCurrencyFormattedNumber(dtotal),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1c = new PdfPCell(a3);
        cell1c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1c.setNoWrap(true);
        cell1c.setBorderColor(Color.white);
        table.addCell(cell1c);
        
        Paragraph c = new Paragraph();
        c.add(new Chunk("\n\n\n",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.WHITE)));
        PdfPCell cell3 = new PdfPCell(c);
        cell3.setColspan(3);
        cell3.setNoWrap(true);
        cell3.setBorderColor(Color.WHITE);
        cell3.setBackgroundColor(Color.WHITE);
        table.addCell(cell3);
    }

    private void FormatFederalTable(PdfPTable rightmaintable,Vector grantsvector)
    	throws DocumentException
    {
       runningTotalIndividualAward = 0;
       runningTotalAward = 0;

       PdfPTable table  = new PdfPTable(3);
       table.setHorizontalAlignment(0);
       table.setTotalWidth(260);
       table.setLockedWidth(true);
       FormatFederalHeader(table);
       int listSize = grantsvector.size();
       int subsize = 0;

       int iClinicalTrial, iResearch, iService, iTraining;
       double dClinicalTrial, dResearch, dService, dTraining;

       iClinicalTrial = iResearch = iService = iTraining = 0;
       dClinicalTrial = dResearch = dService = dTraining = 0;

       for (int num = 0; num < listSize; num++)
       {
           GrantsObject obj = (GrantsObject)grantsvector.elementAt(num);
           String sponsortype = obj.getSponsorTypeDescription();
           String accountNumber = obj.getAccountNumber();

           if (sponsortype.indexOf("Federal")<0)
              continue;

           String activity = obj.getActivityTypeDescription();
           double totalcost = 0;
           boolean bfiscaldate = false;

           totalcost = obj.getDirectCost() + obj.getIndirectCost();
           if (fiscalYear > 0)
           {
              Timestamp canddate = obj.getAwardStartDateTimestamp();
              if (canddate != null)
              {
                if (canddate.getTime() >= begindate.getTime() || 
                   canddate.getTime() <= enddate.getTime())
                                bfiscaldate = true;
              }
           }

           if (activity.indexOf("Organized Research")>-1)
           {
              if (bfiscaldate == true)
              {
                 iResearch++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iResearch++;
                    subsize++;
                 }
              }
              else
              {
                 dResearch += totalcost;
                 runningTotalAward += totalcost;
              }
           }
           else
           if (activity.indexOf("Clinical Trial")>-1)
           {
              if (bfiscaldate == true)
              {
                 iClinicalTrial++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iClinicalTrial++;
                    subsize++;
                 }
              }
              else
              {
                 dClinicalTrial += totalcost;
                 runningTotalAward += totalcost;
              }
           }
           else
           if (activity.indexOf("Instruction")>-1)
           {
              if (bfiscaldate == true)
              {
                 iTraining++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iTraining++;
                    subsize++;
                 }
              }
              else
              {
                 dTraining += totalcost;
                 runningTotalAward += totalcost;
              }
           }
           else
           {
              if (bfiscaldate == true)
              {
                 iService++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iService++;
                    subsize++;
                 }
              }
              else
              {
                 dService += totalcost;
                 runningTotalAward += totalcost;
              }
           }
 
       }
       if (iClinicalTrial > 0)
          displayFederalTotals(table,"Clinical Trial",iClinicalTrial,dClinicalTrial);
       if (iResearch > 0)
          displayFederalTotals(table,"Research Grants/R & D",iResearch,dResearch);
       if (iService > 0)
          displayFederalTotals(table,"Service",iService,dService);
       if (iTraining > 0)
          displayFederalTotals(table,"Training",iTraining,dTraining);

       displayFederalFinalTotals(table,subsize,runningTotalAward);

       PdfPCell cell1a = new PdfPCell(table);
       cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
       cell1a.setNoWrap(true);
       cell1a.setBorderColor(Color.white);
       rightmaintable.addCell(cell1a);
    }

    private void FormatFederalHeader(PdfPTable table)
    	throws DocumentException
    {
        Paragraph a1 = new Paragraph();
        a1.add(new Chunk("Federal",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(true);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        a2.add(new Chunk("Count",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
        
        Paragraph a3 = new Paragraph();
        a3.add(new Chunk("Total",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1c = new PdfPCell(a3);
        cell1c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1c.setNoWrap(true);
        cell1c.setBorderColor(Color.white);
        table.addCell(cell1c);
        
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.WHITE)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(3);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.WHITE);
        table.addCell(cell2);
    }

    private void displayFederalTotals(PdfPTable table, String strtitle, int iCount, double dTotal)
    	throws DocumentException
    {
        Paragraph a1 = new Paragraph();
        a1.add(new Chunk(strtitle,
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(true);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        StringBuffer strbuf = new StringBuffer("");
        strbuf.append(iCount);
        a2.add(new Chunk(strbuf.toString(),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
        
        Paragraph a3 = new Paragraph();
        a3.add(new Chunk(getCurrencyFormattedNumber(dTotal),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cell1c = new PdfPCell(a3);
        cell1c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1c.setNoWrap(true);
        cell1c.setBorderColor(Color.white);
        table.addCell(cell1c);
    }

    private void displayFederalFinalTotals(PdfPTable table, int itotal, double dtotal)
    	throws DocumentException
    {
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.WHITE)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(3);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.WHITE);
        table.addCell(cell2);

        Paragraph a1 = new Paragraph();
        a1.add(new Chunk("Total",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(true);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        StringBuffer strbuf = new StringBuffer("");
        strbuf.append(itotal);
        a2.add(new Chunk(strbuf.toString(),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
        
        Paragraph a3 = new Paragraph();
        a3.add(new Chunk(getCurrencyFormattedNumber(dtotal),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1c = new PdfPCell(a3);
        cell1c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1c.setNoWrap(true);
        cell1c.setBorderColor(Color.white);
        table.addCell(cell1c);
        
        Paragraph c = new Paragraph();
        c.add(new Chunk("\n\n\n",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.WHITE)));
        PdfPCell cell3 = new PdfPCell(c);
        cell3.setColspan(3);
        cell3.setNoWrap(true);
        cell3.setBorderColor(Color.WHITE);
        cell3.setBackgroundColor(Color.WHITE);
        table.addCell(cell3);
    }

    private void FormatNonFederalTable(PdfPTable rightmaintable,Vector grantsvector)
    	throws DocumentException
    {
       runningTotalIndividualAward = 0;
       runningTotalAward = 0;

       PdfPTable table  = new PdfPTable(3);
       table.setHorizontalAlignment(0);
       table.setTotalWidth(260);
       table.setLockedWidth(true);
       FormatNonFederalHeader(table);
       int listSize = grantsvector.size();

       int iClinicalTrial, iResearch, iService, iTraining;
       double dClinicalTrial, dResearch, dService, dTraining;
       int subsize = 0;

       iClinicalTrial = iResearch = iService = iTraining = 0;
       dClinicalTrial = dResearch = dService = dTraining = 0;

       for (int num = 0; num < listSize; num++)
       {
           GrantsObject obj = (GrantsObject)grantsvector.elementAt(num);
           String sponsortype = obj.getSponsorTypeDescription();
           String accountNumber = obj.getAccountNumber();

           if (sponsortype.indexOf("Federal")>-1)
              continue;

           String activity = obj.getActivityTypeDescription();
           double totalcost = 0;
           boolean bfiscaldate = false;

           totalcost = obj.getDirectCost() + obj.getIndirectCost();
           if (fiscalYear > 0)
           {
              Timestamp canddate = obj.getAwardStartDateTimestamp();
              if (canddate != null)
              {
                if (canddate.getTime() >= begindate.getTime() || 
                   canddate.getTime() <= enddate.getTime())
                                bfiscaldate = true;
              }
           }

           if (activity.indexOf("Organized Research")>-1)
           {
              if (bfiscaldate == true)
              {
                 iResearch++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iResearch++;
                    subsize++;
                 }
              }
              else
              {
                 dResearch += totalcost;
                 runningTotalAward += totalcost;
              }
           }
           else
           if (activity.indexOf("Clinical Trial")>-1)
           {
              if (bfiscaldate == true)
              {
                 iClinicalTrial++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iClinicalTrial++;
                    subsize++;
                 }
              }
              else
              {
                 dClinicalTrial += totalcost;
                 runningTotalAward += totalcost;
              }
           }
           else
           if (activity.indexOf("Instruction")>-1)
           {
              if (bfiscaldate == true)
              {
                 iTraining++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iTraining++;
                    subsize++;
                 }
              }
              else
              {
                 dTraining += totalcost;
                 runningTotalAward += totalcost;
              }
           }
           else
           {
              if (bfiscaldate == true)
              {
                 iService++;
                 subsize++;
              }
              if (accountNumber.indexOf("-001")>-1)
              {
                 if (fiscalYear == 0)
                 {
                    iService++;
                    subsize++;
                 }
              }
              else
              {
                 dService += totalcost;
                 runningTotalAward += totalcost;
              }
           }
 
       }
       if (iClinicalTrial > 0)
          displayNonFederalTotals(table,"Clinical Trial",iClinicalTrial,dClinicalTrial);
       if (iResearch > 0)
          displayNonFederalTotals(table,"Research Grants/R & D",iResearch,dResearch);
       if (iService > 0)
          displayNonFederalTotals(table,"Service",iService,dService);
       if (iTraining > 0)
          displayNonFederalTotals(table,"Training",iTraining,dTraining);

       displayNonFederalFinalTotals(table,subsize,runningTotalAward);

       PdfPCell cell1a = new PdfPCell(table);
       cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
       cell1a.setNoWrap(true);
       cell1a.setBorderColor(Color.white);
       rightmaintable.addCell(cell1a);
    }

    private void FormatNonFederalHeader(PdfPTable table)
    	throws DocumentException
    {
        Paragraph a1 = new Paragraph();
        a1.add(new Chunk("Non-Federal",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(true);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        a2.add(new Chunk("Count",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
        
        Paragraph a3 = new Paragraph();
        a3.add(new Chunk("Total",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1c = new PdfPCell(a3);
        cell1c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1c.setNoWrap(true);
        cell1c.setBorderColor(Color.white);
        table.addCell(cell1c);
        
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.WHITE)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(3);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.WHITE);
        table.addCell(cell2);
    }

    private void displayNonFederalTotals(PdfPTable table, String strtitle, int iCount, double dTotal)
    	throws DocumentException
    {
        Paragraph a1 = new Paragraph();
        a1.add(new Chunk(strtitle,
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(true);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        StringBuffer strbuf = new StringBuffer("");
        strbuf.append(iCount);
        a2.add(new Chunk(strbuf.toString(),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
        
        Paragraph a3 = new Paragraph();
        a3.add(new Chunk(getCurrencyFormattedNumber(dTotal),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cell1c = new PdfPCell(a3);
        cell1c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1c.setNoWrap(true);
        cell1c.setBorderColor(Color.white);
        table.addCell(cell1c);
    }

    private void displayNonFederalFinalTotals(PdfPTable table, int itotal, double dtotal)
    	throws DocumentException
    {
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.WHITE)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(3);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.WHITE);
        table.addCell(cell2);

        Paragraph a1 = new Paragraph();
        a1.add(new Chunk("Total",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1a = new PdfPCell(a1);
        cell1a.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1a.setNoWrap(true);
        cell1a.setBorderColor(Color.white);
        table.addCell(cell1a);
        
        Paragraph a2 = new Paragraph();
        StringBuffer strbuf = new StringBuffer("");
        strbuf.append(itotal);
        a2.add(new Chunk(strbuf.toString(),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1b = new PdfPCell(a2);
        cell1b.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1b.setNoWrap(true);
        cell1b.setBorderColor(Color.white);
        table.addCell(cell1b);
        
        Paragraph a3 = new Paragraph();
        a3.add(new Chunk(getCurrencyFormattedNumber(dtotal),
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1c = new PdfPCell(a3);
        cell1c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell1c.setNoWrap(true);
        cell1c.setBorderColor(Color.white);
        table.addCell(cell1c);
        
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

		Object totalbegindatecell = row.get("START_DATE");
                Timestamp canddate = getTimeStampCellValue(totalbegindatecell);
		grantsobj.setAwardBeginDate(getCellValue(totalbegindatecell).trim());    	
		grantsobj.setAwardStartDateTimestamp(canddate);    	

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
         
    public void getSpecificXLSOutput(HttpServletResponse res)
         throws ServletException, IOException
    {
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
            Object totalbegindatecell = row.get("START_DATE");
            Timestamp canddate = getTimeStampCellValue(totalbegindatecell);
            if (canddate != null)
            {
                if (canddate.getTime() < begindate.getTime() || 
                   canddate.getTime() > enddate.getTime())
                                bfound = true;
            }
         }
         return bfound;
    }
    
    
    public String getSpecificCSVOutput()
    {
        return null;
    }
    
}


