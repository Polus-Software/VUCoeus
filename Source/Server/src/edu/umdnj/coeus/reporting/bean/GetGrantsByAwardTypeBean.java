/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GetGrantsByAwardTypeBean.java,v 1.1.2.15 2007/09/18 18:23:05 cvs Exp $
 * $Log: GetGrantsByAwardTypeBean.java,v $
 * Revision 1.1.2.15  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.14  2007/09/06 19:29:44  cvs
 * Fiscal Year Delineation: tweak server-side code to reflect GUI changes
 *
 * Revision 1.1.2.13  2007/09/05 19:08:58  cvs
 * Server-side modifications for Fiscal Year Delineation
 *
 * Revision 1.1.2.12  2007/08/16 15:30:09  cvs
 * Add support for Headers per page
 *
 * Revision 1.1.2.11  2007/08/02 19:19:05  cvs
 * Enforce Snippet integrity in PDF output
 *
 * Revision 1.1.2.10  2007/07/31 13:36:19  cvs
 * Add Annual Reports server-side code
 * Move many methods to base class Reporting Base Bean
 *
 * Revision 1.1.2.9  2007/07/17 15:59:26  cvs
 * Display reports that total costs of 0 but with valid dates
 *
 * Revision 1.1.2.8  2007/06/11 15:39:13  cvs
 * Make sure that Sponsor Name and Project Title have nowrap = FALSE
 *
 * Revision 1.1.2.7  2007/03/27 17:41:12  cvs
 * Graph: Address issues with Award Type and Activity Type display
 *
 * Revision 1.1.2.6  2007/03/27 15:19:20  cvs
 * Modify code to work with Award FnA Distributions
 *
 * Revision 1.1.2.5  2007/02/16 19:32:14  cvs
 * Correct misspelling of string Extramural
 *
 * Revision 1.1.2.4  2007/02/14 16:34:38  cvs
 * Check for empty reports
 *
 * Revision 1.1.2.3  2007/01/24 16:30:56  cvs
 * Add support for Graph Award Types
 *
 * Revision 1.1.2.2  2007/01/23 18:13:11  cvs
 * Add GUI support for Graph Sponsor Types
 *
 * Revision 1.1.2.1  2007/01/23 15:41:17  cvs
 * Add Award Type Grant support
 *
 *
 */
/*
 * @(#)GetGrantsByAwardTypeBean.java 
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

import edu.mit.coeus.utils.dbengine.DBEngineImpl;

import edu.mit.coeus.utils.dbengine.DBException;

import edu.mit.coeus.utils.dbengine.Parameter;

import edu.mit.coeus.utils.dbengine.DBEngineConstants;

import edu.mit.coeus.utils.CoeusVector;

import edu.mit.coeus.utils.UtilFactory;

import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.budget.report.ReportGenerator;

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
import edu.umdnj.coeus.utilities.UMDNJStrings;

//import org.apache.poi.hssf.model.Workbook;

public class GetGrantsByAwardTypeBean extends ReportingBaseBean
{

    private int runningTotalDirectCost;
    private int runningTotalIndirectCost;
    private int runningTotalCost;

    private String UnitName = "";
    private String AwardType = "";
    private boolean bNIH = false;
    
    private UMDNJSQLUtilities pobj = null;

    private GrantsObject grantsobj = null;

    /** Creates a new instance of GetGrantsByAwardTypeBean */
    public GetGrantsByAwardTypeBean() 
    {
	super();
        fiscalYear = 0;
        Initialization();
    }
    
    public GetGrantsByAwardTypeBean(String UnitName, String FiscalYearType, String FiscalYear)
    {
        super();
        fiscalYear = 0;
        this.UnitName = UnitName;
        this.FiscalYearType = FiscalYearType;
        this.FiscalYear = FiscalYear;
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
        Vector result = pobj.getGrantsByAwardType(UnitName);
        StringBuffer buf = new StringBuffer("");
        if (result != null && result.size() > 0)
        {
            HashMap row = null;
            int listSize = 0;
            if (result != null)
               listSize = result.size();
             if (listSize > 0)
             {
                String UnitSchool = getUnitSchool(UnitName);
                buf.append("<h2>Detailed Award List by Award Type for "+UnitSchool+"</h2>");
		buf.append("<table border=1>");	
                buf.append("<tr>");
                    buf.append(printObj("<b><u>Award Number</u></b>"));
    		    buf.append(printObj("<b><u>Award Type</u></b>"));
                    buf.append(printObj("<b><u>Unit Name</u></b>"));
                    buf.append(printObj("<b><u>Current Effective Date</u></b>"));
                    buf.append(printObj("<b><u>Obligation Expiration Date</u></b>"));
                    buf.append(printObj("<b><u>Anticipated Total Amount</u></b>"));
                    buf.append(printObj("<b><u>Parent Unit</u></b>"));
                    buf.append(printObj("<b><u>Title</u></b>"));
                    buf.append(printObj("<b><u>Sponsor Name</u></b>"));
                    buf.append(printObj("<b><u>Investigator</u></b>")); 
                    buf.append(printObj("<b><u>Start Date</u></b>"));
                    buf.append(printObj("<b><u>End Date</u></b>"));
                    buf.append(printObj("<b><u>Direct Cost</u></b>"));
                    buf.append(printObj("<b><u>Indirect Cost</u></b>"));
                buf.append("</tr>");
            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                    row=(HashMap)result.elementAt(rowNum); 
                    Object str1  = row.get("MIT_AWARD_NUMBER");
                    Object str2  = row.get("DESCRIPTION");
                    Object str3  = row.get("UNIT_NAME");
                    Object str4  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
                    Object str5  = row.get("OBLIGATION_EXPIRATION_DATE");
                    Object str6  = row.get("ANTICIPATED_TOTAL_AMOUNT");
                    Object str7  = row.get("PARENT_UNIT_NUMBER");
                    Object str8  = row.get("TITLE");
                    Object str9  = row.get("SPONSOR_NAME");
                    Object str10  = row.get("PERSON_NAME"); 
                    Object str11 = row.get("START_DATE");
                    Object str12 = row.get("END_DATE");
                    Object str13 = row.get("DIRECT_COST");
                    Object str14 = row.get("INDIRECT_COST");

                    if (fiscalYear > 0)
                    {
                       Object totalbegindatecell = row.get("START_DATE");
                       Timestamp canddate = getTimeStampCellValue(totalbegindatecell);                   
                       if (canddate == null ||
                           canddate.getTime() < begindate.getTime() || 
                           canddate.getTime() > enddate.getTime())
                                  continue;
                    }

                    buf.append("<tr>");
                    buf.append(printObj(str1));
                    buf.append(printObj(str2));
                    buf.append(printObj(str3));
                    buf.append(printObj(str4));
                    buf.append(printObj(str5));
                    buf.append(printObj(str6));
                    buf.append(printObj(str7));
                    buf.append(printObj(str8));
                    buf.append(printObj(str9));
                    buf.append(printObj(str10));
                    buf.append(printObj(str11));
                    buf.append(printObj(str12));
                    buf.append(printObj(str13));
                    buf.append(printObj(str14));
                    buf.append("</tr>");
                }
    

        	buf.append("</table>");
            }
            else
		buf.append(UMDNJStrings.NoRecordsComingBack);
        }
        else
            buf.append(UMDNJStrings.NoRecordsComingBack);
        return buf.toString();
        
    }
    
    
    public void getSpecificPDFOutput(HttpServletResponse res)
         throws ServletException, IOException
    {
        Vector result = pobj.getGrantsByAwardType(UnitName);
        if (result != null && result.size() > 0)
        {
            String UnitSchool = getUnitSchool(UnitName);
            Document document = new Document(PageSize.A4.rotate(),36,36,36,36);
	    width = document.getPageSize().width();
            try 
            {
                HashMap row = null;
                int listSize = 0;
                if (result != null)
                    listSize = result.size();
                if (listSize > 0)
                {
                    int subtitlechanged = 0;
                    grantsobj = null;
                    res.setContentType("application/pdf");
                    PdfWriter.getInstance(document, res.getOutputStream());
                    
                    document.open();
 
                    PdfPTable table  = new PdfPTable(8);

                    float[] widths = {0.15f, 0.15f, 0.15f, 0.15f, 0.1f, 0.1f, 0.1f, 0.1f};
                    table.setWidths(widths);
                    table.getDefaultCell().setBorder(0);
                    table.setHorizontalAlignment(0);
                    table.setTotalWidth(width - 72);
                    table.setLockedWidth(true);

                    for (int inum = 0; inum < listSize; inum++)
                    {           
                            row = (HashMap)result.elementAt(inum);
                            HashMap prevrow = null;
                            if (inum > 0)
                                prevrow = (HashMap)result.elementAt(inum-1);
                            String strtitle, prevstrtitle = "";
                            Object cell = row.get("DESCRIPTION");
                            Object prevcell = null;
                
                            strtitle = ((String)cell).trim();
                            if (prevrow != null)
                            {
                               prevcell = prevrow.get("DESCRIPTION");
                               prevstrtitle = ((String)prevcell).trim();
                            }
                            else
                               prevstrtitle = strtitle;
                            
                            FormatRecord(row, table);
            	
                            if (inum == 0 || strtitle.compareTo(prevstrtitle)!=0)
                            {
                               if (strtitle.compareTo(prevstrtitle)!=0)
                                  subtitlechanged++;
                               if (inum != 0)
                               { 
                                  displayTotals(table);
                                  document.add(table);
                                  document.newPage();
                               }
                               table = new PdfPTable(8);
                               table.setWidths(widths);
                               table.getDefaultCell().setBorder(0);
                               table.setHorizontalAlignment(0);
                               table.setTotalWidth(width - 72);
                               table.setLockedWidth(true);
                               table.setHeaderRows(5);
                               runningTotalDirectCost = 0;
                               runningTotalIndirectCost = 0;
                               runningTotalCost = 0;
                               FormatHeader(row,table);
                            }
                    }
                    if (subtitlechanged==0)
                        displayAwardContents(table);
                    displayTotals(table);
                    document.add(table);
                    table.setWidthPercentage(100);   
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
           writer.print(UnitSchool + " does not have grants.");
           writer.println("</body></html>"); 
       }
    }

    private void FormatHeader(HashMap row, PdfPTable table)
    	throws DocumentException
    {
        String UnitSchool = getUnitSchool(UnitName);
        Paragraph a = new Paragraph();
        String strhead = UnitSchool+"\nResearch, Service, and Training Grants by Award Type\nActive Extramural Grants and Contracts";
        a.add(new Chunk(strhead,
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setColspan(8);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setNoWrap(true);
        cell1.setBorderColor(Color.white);
        table.addCell(cell1);
        
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(8);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.BLACK);
        table.addCell(cell2);
        
        Paragraph c1 = new Paragraph();
        c1.add(new Chunk("ORSP Number\nProject Period\nProject Total Cost",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc1 = new PdfPCell(c1);
        cellc1.setNoWrap(true);
        cellc1.setBorderColor(Color.white);
        table.addCell(cellc1);

        Paragraph c2 = new Paragraph();
        c2.add(new Chunk("PI Name\nProject Title\nUnit Name",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc2 = new PdfPCell(c2);
        cellc2.setNoWrap(true);
        cellc2.setBorderColor(Color.white);
        table.addCell(cellc2);

        Paragraph c3 = new Paragraph();
        c3.add(new Chunk("Sponsor Name",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc3 = new PdfPCell(c3);
        cellc3.setColspan(2);
        cellc3.setNoWrap(true);
        cellc3.setBorderColor(Color.white);
        table.addCell(cellc3);

        Paragraph c5 = new Paragraph();
        c5.add(new Chunk("Award Period\nStart Date",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc5 = new PdfPCell(c5);
        cellc5.setNoWrap(true);
        cellc5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc5.setBorderColor(Color.white);
        table.addCell(cellc5);

        Paragraph c6 = new Paragraph();
        c6.add(new Chunk("Award Period\nDirect Cost",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc6 = new PdfPCell(c6);
        cellc6.setNoWrap(true);
        cellc6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc6.setBorderColor(Color.white);
        table.addCell(cellc6);

        Paragraph c7 = new Paragraph();
        c7.add(new Chunk("Award Period\nIndirect Cost",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc7 = new PdfPCell(c7);
        cellc7.setNoWrap(true);
        cellc7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc7.setBorderColor(Color.white);
        table.addCell(cellc7);

        Paragraph c8 = new Paragraph();
        c8.add(new Chunk("Award Period\nTotal Cost",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, Color.BLACK)));
        PdfPCell cellc8 = new PdfPCell(c8);
        cellc8.setNoWrap(true);
        cellc8.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc8.setBorderColor(Color.white);
        table.addCell(cellc8);

        Paragraph d = new Paragraph();
        d.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));
        PdfPCell cell4 = new PdfPCell(d);
        cell4.setColspan(8);
        cell4.setNoWrap(true);
        cell4.setBackgroundColor(Color.BLACK);
        table.addCell(cell4);
        
        FormatSubHeader(row,table);
    }
    
    private void FormatRecord(HashMap row, PdfPTable table)
    	throws DocumentException
    {
        	Object cell = row.get("MIT_AWARD_NUMBER");
        	String straccount = ((String)cell).trim();
        	int index1 = straccount.indexOf("-001");
        	if (index1 > 0)
        	{
                        displayAwardContents(table);
        		
        		straccount = straccount.substring(0,index1);
        		grantsobj = new GrantsObject(straccount);    
        		populateCurrentGrantsObject(row);
        	}
        	else
        	{
        		handleCostValues(row);
        	}
    }
    
    private void FormatSubHeader(HashMap row, PdfPTable table)
    {
    	Object cell = row.get("DESCRIPTION");
    	String strtitle = ((String)cell).trim();
    	int index = strtitle.indexOf("(Do Not Use)");
    	if (index > 0)
    		strtitle = strtitle.substring(0,index);
    	
        Paragraph a = new Paragraph();
        a.add(new Chunk(strtitle,
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setColspan(8);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setNoWrap(true);
        cell1.setBorderColor(Color.white);
        table.addCell(cell1);
    }
    
    private void populateCurrentGrantsObject(HashMap row)
    {
        Object begindatecell  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
        Object enddatecell  = row.get("OBLIGATION_EXPIRATION_DATE");
        Object totalamountcell  = row.get("ANTICIPATED_TOTAL_AMOUNT");
        Object titlecell  = row.get("TITLE");
        Object sponsorcell  = row.get("SPONSOR_NAME");
        Object investigatorcell  = row.get("PERSON_NAME"); 
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

	private void displayGrantsObjContents(PdfPTable table)
    	throws DocumentException
	{
            if (grantsobj.getAwardBeginDate().length()==0 && 
                    grantsobj.getAwardEndDate().length()==0)
                return;
		
        PdfPTable subtable = new PdfPTable(8);		
        float[] widths = {0.15f, 0.15f, 0.15f, 0.15f, 0.1f, 0.1f, 0.1f, 0.1f};
        subtable.setWidths(widths);
        subtable.getDefaultCell().setBorder(0);
        subtable.setHorizontalAlignment(0);
        subtable.setTotalWidth(width - 72);
        subtable.setLockedWidth(true);

        String formatAccountNumber = grantsobj.getAccountNumber();
        FormatDataCell(subtable,formatAccountNumber,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,1);
        FormatDataCell(subtable,grantsobj.getInvestigatorName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,1);
        FormatDataCell(subtable,grantsobj.getSponsorName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,2);
        FormatDataCell(subtable,grantsobj.getAwardBeginDate(),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getDirectCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getIndirectCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);
        FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getTotalCost()),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,8,1);

        StringBuffer strbufd = new StringBuffer("");
        strbufd.append(getFormattedDate(grantsobj.getBeginProjectDate()));
        strbufd.append(" - ");
        strbufd.append(getFormattedDate(grantsobj.getEndProjectDate()));

        FormatDataCell(subtable,strbufd.toString(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,1);
        FormatDataCell(subtable,grantsobj.getProjectTitle(),Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,8,7);
        FormatDataCell(subtable,getCurrencyFormattedNumber(grantsobj.getProjectTotalCost()),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,1);
        FormatDataCell(subtable,grantsobj.getUnitName(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,7);
        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,8,8);

        PdfPCell cell11 = new PdfPCell(subtable);
        cell11.setBorderColor(Color.white);
        cell11.setColspan(8);
        cell11.setNoWrap(true);
        table.addCell(cell11);
	}

	private void displayTotals(PdfPTable table)
	{
        Paragraph a = new Paragraph();
        a.add(new Chunk("",
        		FontFactory.getFont(FontFactory.HELVETICA, 8, Font.PLAIN, Color.BLACK)));
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setBorderColor(Color.white);
        cell1.setColspan(5);
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
        cell3.setColspan(5);
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
        Vector result = pobj.getGrantsByAwardType(UnitName);
        if (result != null && result.size() > 0)
        {
            HashMap dbrow = null;
	    HSSFWorkbook wb = new HSSFWorkbook();
            String UnitSchool = getUnitSchool(UnitName);
            res.setContentType("application/vnd.ms-excel");
            ServletOutputStream out = res.getOutputStream();
	
	    //setting the sheet name as file name without extension.

	    HSSFSheet sheet = wb.createSheet("Grants for "+UnitSchool);

	    // Create a row and put some cells in it. Rows are 0 based.

	    HSSFRow row = null;

	    HSSFCell cell;        

	    HSSFCellStyle cellStyle = wb.createCellStyle();

	    cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        

	    HSSFFont headerFont = wb.createFont();

	    headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
	    HSSFCellStyle headerStyle = wb.createCellStyle();

	    headerStyle.setFont(headerFont);
            StringBuffer sbuf = new StringBuffer();

            row = sheet.createRow((short)0);

                cell = row.createCell((short)0);
                    cell.setCellValue("Award Number");
                cell = row.createCell((short)1);
                    cell.setCellValue("Award Type");
                cell = row.createCell((short)2);
                    cell.setCellValue("Unit Name");
                cell = row.createCell((short)3);
                    cell.setCellValue("Current Effective Date");
                cell = row.createCell((short)4);
                    cell.setCellValue("Obligation Expiration Date");
                cell = row.createCell((short)5);
                    cell.setCellValue("Anticipated Total Amount");
                cell = row.createCell((short)6);
                    cell.setCellValue("Parent Unit");
                cell = row.createCell((short)7);
                    cell.setCellValue("Title");
                cell = row.createCell((short)8);
                    cell.setCellValue("Sponsor Name");
                cell = row.createCell((short)9);
                    cell.setCellValue("Investigator"); 
                cell = row.createCell((short)10);
                    cell.setCellValue("Start Date");
                cell = row.createCell((short)11);
                    cell.setCellValue("End Date");
                cell = row.createCell((short)12);
                    cell.setCellValue("Direct Cost");
                cell = row.createCell((short)13);
                    cell.setCellValue("Indirect Cost");

                     //Make Headers BOLD

           	cell.setCellStyle(headerStyle);
            int listSize = result.size();
            
	    int currow = 0;
            for (int i=0; i < listSize; i++) 
            {
                dbrow=(HashMap)result.elementAt(i); 

                    Object str1  = dbrow.get("MIT_AWARD_NUMBER");
                    Object str2  = dbrow.get("DESCRIPTION");
                    Object str3  = dbrow.get("UNIT_NAME");
                    Object str4  = dbrow.get("CURRENT_FUND_EFFECTIVE_DATE");
                    Object str5  = dbrow.get("OBLIGATION_EXPIRATION_DATE");
                    Object str6  = dbrow.get("ANTICIPATED_TOTAL_AMOUNT");
                    Object str7  = dbrow.get("PARENT_UNIT_NUMBER");
                    Object str8  = dbrow.get("TITLE");
                    Object str9  = dbrow.get("SPONSOR_NAME");
                    Object str10  = dbrow.get("PERSON_NAME"); 
                    Object str11 = dbrow.get("START_DATE");
                    Object str12 = dbrow.get("END_DATE");
                    Object str13 = dbrow.get("DIRECT_COST");
                    Object str14 = dbrow.get("INDIRECT_COST");

                    if (fiscalYear > 0)
                    {
                       Object totalbegindatecell = dbrow.get("START_DATE");
                       Timestamp canddate = getTimeStampCellValue(totalbegindatecell);                   
                       if (canddate == null ||
                           canddate.getTime() < begindate.getTime() || 
                           canddate.getTime() > enddate.getTime())
                                  continue;
                    }

                row = sheet.createRow((short)currow + 1); //+1 since First Row is Header Row
                    addToXLSCell(str1,0,row);
                    addToXLSCell(str2,1,row);
                    addToXLSCell(str3,2,row);
                    addToXLSCell(str4,3,row);
                    addToXLSCell(str5,4,row);
                    addToXLSCell(str6,5,row);
                    addToXLSCell(str7,6,row);
                    addToXLSCell(str8,7,row);
                    addToXLSCell(str9,8,row);
                    addToXLSCell(str10,9,row);
                    addToXLSCell(str11,10,row);
                    addToXLSCell(str12,11,row);
                    addToXLSCell(str13,12,row);
                    addToXLSCell(str14,13,row);
                    currow++;
            }

            wb.write(out);
            out.close();
       }
       else
       {
           res.setContentType("text/html");
           PrintWriter writer = res.getWriter();
           writer.println("<html><body>");
           String UnitSchool = getUnitSchool(UnitName);
           writer.print(UnitSchool + " does not have grants.");
           writer.println("</body></html>"); 
       }

    }    
    
    public String getSpecificCSVOutput()
    {
        Vector result = pobj.getGrantsByAwardType(UnitName);
        StringBuffer buf = new StringBuffer("");
        if (result != null && result.size() > 0)
        {
            HashMap row = null;
            int listSize = 0;
            if (result != null)
               listSize = result.size();
             if (listSize > 0)
             {
                    buf.append(printPlainObj("Award Number"));
                    buf.append(",");
    		    buf.append(printPlainObj("Award Type"));
                    buf.append(",");
                    buf.append(printPlainObj("Unit Name"));
                    buf.append(",");
                    buf.append(printPlainObj("Current Effective Date"));
                    buf.append(",");
                    buf.append(printPlainObj("Obligation Expiration Date"));
                    buf.append(",");
                    buf.append(printPlainObj("Anticipated Total Amount"));
                    buf.append(",");
                    buf.append(printPlainObj("Parent Unit"));
                    buf.append(",");
                    buf.append(printPlainObj("Title"));
                    buf.append(",");
                    buf.append(printPlainObj("Sponsor Name"));
                    buf.append(",");
                    buf.append(printPlainObj("Investigator")); 
                    buf.append(",");
                    buf.append(printPlainObj("Start Date"));
                    buf.append(",");
                    buf.append(printPlainObj("End Date"));
                    buf.append(",");
                    buf.append(printPlainObj("Direct Cost"));
                    buf.append(",");
                    buf.append(printPlainObj("Indirect Cost"));
                buf.append("\n");
            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                    row=(HashMap)result.elementAt(rowNum); 
                    Object str1  = row.get("MIT_AWARD_NUMBER");
                    Object str2  = row.get("DESCRIPTION");
                    Object str3  = row.get("UNIT_NAME");
                    Object str4  = row.get("CURRENT_FUND_EFFECTIVE_DATE");
                    Object str5  = row.get("OBLIGATION_EXPIRATION_DATE");
                    Object str6  = row.get("ANTICIPATED_TOTAL_AMOUNT");
                    Object str7  = row.get("PARENT_UNIT_NUMBER");
                    Object str8  = row.get("TITLE");
                    Object str9 = row.get("SPONSOR_NAME");
                    Object str10 = row.get("PERSON_NAME"); 
                    Object str11 = row.get("START_DATE");
                    Object str12 = row.get("END_DATE");
                    Object str13 = row.get("DIRECT_COST");
                    Object str14 = row.get("INDIRECT_COST");

                    if (fiscalYear > 0)
                    {
                       Object totalbegindatecell = row.get("START_DATE");
                       Timestamp canddate = getTimeStampCellValue(totalbegindatecell);                   
                       if (canddate == null ||
                           canddate.getTime() < begindate.getTime() || 
                           canddate.getTime() > enddate.getTime())
                                  continue;
                    }

                    buf.append(printPlainObj(str1));
                    buf.append(",");
                    buf.append(printPlainObj(str2));
                    buf.append(",");
                    buf.append(printPlainObj(str3));
                    buf.append(",");
                    buf.append(printPlainObj(str4));
                    buf.append(",");
                    buf.append(printPlainObj(str5));
                    buf.append(",");
                    buf.append(printPlainObj(str6));
                    buf.append(",");
                    buf.append(printPlainObj(str7));
                    buf.append(",");
                    buf.append(printPlainObj(str8));
                    buf.append(",");
                    buf.append(printPlainObj(str9));
                    buf.append(",");
                    buf.append(printPlainObj(str10));
                    buf.append(",");
                    buf.append(printPlainObj(str11));
                    buf.append(",");
                    buf.append(printPlainObj(str12));
                    buf.append(",");
                    buf.append(printPlainObj(str13));
                    buf.append(",");
                    buf.append(printPlainObj(str14));
                    buf.append("\n");
                }
            }
            else
		buf.append(UMDNJStrings.NoRecordsComingBack);
        }
        else
            buf.append(UMDNJStrings.NoRecordsComingBack);
        return buf.toString();
        
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

    public void setAwardType(String AwardType)
    {
        if (AwardType != null && AwardType.length() > 0)
        {
            this.AwardType = AwardType;
            pobj.setAwardType(AwardType);
        }
        else
            this.AwardType = "";
    }
    
    private void displayAwardContents(PdfPTable table)
    	throws DocumentException
    {
       if (grantsobj != null)
       {
          grantsobj.setTotalCost(grantsobj.getDirectCost()+grantsobj.getIndirectCost());
          displayGrantsObjContents(table);
          runningTotalDirectCost += grantsobj.getDirectCost();
          runningTotalIndirectCost += grantsobj.getIndirectCost();
          runningTotalCost += grantsobj.getTotalCost();
       }
    }
}


