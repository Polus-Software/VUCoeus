/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GetSubmittedProposalsBean.java,v 1.1.2.12 2007/09/18 18:23:05 cvs Exp $
 * $Log: GetSubmittedProposalsBean.java,v $
 * Revision 1.1.2.12  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.11  2007/08/16 15:30:09  cvs
 * Add support for Headers per page
 *
 * Revision 1.1.2.10  2007/08/02 19:19:05  cvs
 * Enforce Snippet integrity in PDF output
 *
 * Revision 1.1.2.9  2007/06/11 15:39:13  cvs
 * Make sure that Sponsor Name and Project Title have nowrap = FALSE
 *
 * Revision 1.1.2.8  2007/05/16 14:28:06  cvs
 * Address out of mem error: separate table into tables of 500 records each
 *
 * Revision 1.1.2.7  2007/02/14 16:34:38  cvs
 * Check for empty reports
 *
 * Revision 1.1.2.6  2007/01/18 21:26:53  cvs
 * Add support for wider PDF tables
 *
 * Revision 1.1.2.5  2007/01/17 15:26:35  cvs
 * Add CSV Support
 *
 * Revision 1.1.2.4  2006/12/14 16:46:55  cvs
 * Correct date filter bug with begin and end Timestamps
 *
 * Revision 1.1.2.3  2006/12/13 20:57:33  cvs
 * Added date filter support for styled PDF of GetSubmittedProposals retrieval
 *
 * Revision 1.1.2.2  2006/12/12 20:08:31  cvs
 * Added styled PDF support for GetSubmittedProposals retrieval
 *
 * Revision 1.1.2.1  2006/12/11 20:51:41  cvs
 * Added support for GetSubmittedProposals retrieval
 *
 *
 *
 */
/*
 * @(#)GetSubmittedProposalsBean.java 
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

public class GetSubmittedProposalsBean extends ReportingBaseBean
{
    private int runningTotalProjectAmount;
    private int runningTotalDirectCost;
    private int runningTotalIndirectCost;
    private int runningTotalCost;

    private String SchoolName = "";
    private int year = 0;
    private int month = 0;
    private int iDateFilter = 0;
    private Timestamp begindate = null;
    private Timestamp enddate = null;
    
    private UMDNJSQLUtilities pobj = null;


    /** Creates a new instance of GetSubmittedProposalsBean */
    public GetSubmittedProposalsBean() 
    {
	super();
        Initialization();
    }
    
    public GetSubmittedProposalsBean(String SchoolName, String DateFilter)
    {
        super();
        this.SchoolName = SchoolName;
        if (DateFilter != null && DateFilter.length() > 0)
            InitializeDateFilter(DateFilter);
        Initialization();
    }
    
    private void Initialization()
    {
        pobj = new UMDNJSQLUtilities();
        runningTotalProjectAmount = 0;
        runningTotalDirectCost = 0;
        runningTotalIndirectCost = 0;
        runningTotalCost = 0;
    }
    
    public String getSpecificHTMLOutput()
    {
        Vector result = pobj.getSubmittedProposals(SchoolName);
        StringBuffer buf = new StringBuffer("");
        if (result != null && result.size() > 0)
        {
            HashMap row = null;
            int listSize = 0;
            if (result != null)
               listSize = result.size();
             if (listSize > 0)
             {
                buf.append("<h2>Detailed Submitted Proposals for "+SchoolName+"</h2>");
		buf.append("<table border=1>");	
                buf.append("<tr>");
                buf.append(printObj("<b><u>PROPOSAL_NUMBER</u></b>"));
                buf.append(printObj("<b><u>SUBMITTED_DATE</u></b>"));
                buf.append(printObj("<b><u>SEQUENCE_NUMBER</u></b>"));
                buf.append(printObj("<b><u>PROPOSAL_TYPE_CODE</u></b>"));
                buf.append(printObj("<b><u>PROPOSAL_TYPE_DESCRIPTION</u></b>"));
                buf.append(printObj("<b><u>CURRENT_ACCOUNT_NUMBER</u></b>"));
                buf.append(printObj("<b><u>TITLE</u></b>"));
                buf.append(printObj("<b><u>SPONSOR_CODE</u></b>"));
                buf.append(printObj("<b><u>SPONSOR_NAME</u></b>"));
                buf.append(printObj("<b><u>REQUESTED_START_DATE_INITIAL</u></b>"));
                buf.append(printObj("<b><u>REQUESTED_START_DATE_TOTAL</u></b>"));
                buf.append(printObj("<b><u>REQUESTED_END_DATE_INITIAL</u></b>"));
                buf.append(printObj("<b><u>REQUESTED_END_DATE_TOTAL	</u></b>"));
                buf.append(printObj("<b><u>TOTAL_DIRECT_COST_INITIAL</u></b>"));
                buf.append(printObj("<b><u>TOTAL_DIRECT_COST_TOTAL</u></b>"));
                buf.append(printObj("<b><u>TOTAL_INDIRECT_COST_INITIAL</u></b>"));
                buf.append(printObj("<b><u>TOTAL_INDIRECT_COST_TOTAL</u></b>"));
                buf.append(printObj("<b><u>PI_NAME</u></b>"));
                buf.append(printObj("<b><u>LEAD_UNIT</u></b>"));
                buf.append(printObj("<b><u>ACTIVITY_TYPE_CODE</u></b>"));
                buf.append(printObj("<b><u>ACTIVITY_TYPE_DESCRIPTION</u></b>"));
                buf.append("</tr>");
            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                    buf.append("<tr>");
                    row=(HashMap)result.elementAt(rowNum); 
                    Object str1 = row.get("PROPOSAL_NUMBER");
                    Object str2 = row.get("SUBMITTED_DATE");
                    Object str3 = row.get("SEQUENCE_NUMBER");
                    Object str4 = row.get("PROPOSAL_TYPE_CODE");
                    Object str5 = row.get("PROPOSAL_TYPE_DESCRIPTION");
                    Object str6 = row.get("CURRENT_ACCOUNT_NUMBER");
                    Object str7 = row.get("TITLE");
                    Object str8 = row.get("SPONSOR_CODE");
                    Object str9 = row.get("SPONSOR_NAME");
                    Object str10 = row.get("REQUESTED_START_DATE_INITIAL");
                    Object str11 = row.get("REQUESTED_START_DATE_TOTAL");
                    Object str12 = row.get("REQUESTED_END_DATE_INITIAL");
                    Object str13 = row.get("REQUESTED_END_DATE_TOTAL");
                    Object str14 = row.get("TOTAL_DIRECT_COST_INITIAL");
                    Object str15 = row.get("TOTAL_DIRECT_COST_TOTAL");
                    Object str16 = row.get("TOTAL_INDIRECT_COST_INITIAL");
                    Object str17 = row.get("TOTAL_INDIRECT_COST_TOTAL");
                    Object str18 = row.get("PI_NAME");
                    Object str19 = row.get("LEAD_UNIT");
                    Object str20 = row.get("ACTIVITY_TYPE_CODE");
                    Object str21 = row.get("ACTIVITY_TYPE_DESCRIPTION");
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
                        buf.append(printObj(str15));
                        buf.append(printObj(str16));
                        buf.append(printObj(str17));
                        buf.append(printObj(str18));
                        buf.append(printObj(str19));
                        buf.append(printObj(str20));
                        buf.append(printObj(str21));
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
        Vector result = pobj.getSubmittedProposals(SchoolName);
        if (result != null && result.size() > 0)
        {
            Document document = new Document(PageSize.A4.rotate());
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
 
                    PdfPTable table  = new PdfPTable(11);
                    table.getDefaultCell().setBorder(0);
                    table.setHorizontalAlignment(0);
                    table.setTotalWidth(width - 72);
                    table.setLockedWidth(true);
                    table.setHeaderRows(6);
                    FormatHeader(table);
                    
	    ////////////////////////////////////////////////////////////////////////
            // ITERATE OVER EACH EXCEL ROW
            for (int inum = 0; inum < listSize; inum++)
            {           
                row = (HashMap)result.elementAt(inum);
                if (iDateFilter != 0)
                {
                    Object comparedatecell = row.get("SUBMITTED_DATE");
                    Timestamp canddate = getTimeStampCellValue(comparedatecell); 
                    if (canddate != null)
                    {
                        if (canddate.getTime() >= begindate.getTime() &&
                                canddate.getTime() < enddate.getTime())
                            FormatRecord(row, table);
                    }
                }
                else
                {
                	/*******************************************
                	 * RCE - 5/16/07
                	 * This circumvents an OutOfMemoryError when records exceeds sizes
                	 * of 500. A new table is created for every iteration of 500 records.
                	 */
                	if (inum %500 == 0 && inum != 0)
                	{
                		document.add(table);
				document.newPage();
                        	table  = new PdfPTable(11);
                        	table.getDefaultCell().setBorder(0);
                        	table.setHorizontalAlignment(0);
                        	table.setTotalWidth(width - 72);
                        	table.setLockedWidth(true);
                        	table.setHeaderRows(6);
                        	FormatHeader(table);
                	}
                    FormatRecord(row,table);
                }
            }

	    FormatFooter(table);                

            document.add(table);
            table.setWidthPercentage(100);
          }
         document.close();
       }
        catch(DocumentException de) 
        {
            de.printStackTrace();
            System.err.println("document: " + de.getMessage());
        }
    	catch ( IOException ex ) 
    	{
            ex.printStackTrace();
        }
    	catch (java.lang.OutOfMemoryError ex)
    	{
    		ex.printStackTrace();
    	}
      }
      else
      {
          res.setContentType("text/html");
          PrintWriter writer = res.getWriter();
          writer.println("<html><body>");
          String UnitSchool = getUnitSchool(SchoolName);
          writer.print(UnitSchool + " does not have grants");
          writer.println("</body></html>"); 
      }

    }

    ////////////////////////////////////////////////////////////////////////
    // METHOD TO CREATE PDF HEADER INFORMATION
    public void FormatHeader(PdfPTable table)
    	throws DocumentException
    {
        Paragraph a = new Paragraph();
        a.add(new Chunk(SchoolName+" - Proposals Submitted to ORSP",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setColspan(11);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setNoWrap(true);
        cell1.setBorderColor(Color.white);
        table.addCell(cell1);

        Paragraph b1 = new Paragraph();
        b1.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.WHITE)));
        PdfPCell cellb1 = new PdfPCell(b1);
        cellb1.setColspan(11);
        cellb1.setNoWrap(true);
        cellb1.setBackgroundColor(Color.WHITE);
        cellb1.setBorderColor(Color.WHITE);
        table.addCell(cellb1);
        
        
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(11);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.BLACK);
        table.addCell(cell2);
        
        Paragraph c1 = new Paragraph();
        c1.add(new Chunk("Submission\nDate to ORSP",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc1 = new PdfPCell(c1);
        cellc1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc1.setNoWrap(true);
        cellc1.setBorderColor(Color.white);
        table.addCell(cellc1);

        Paragraph c2 = new Paragraph();
        c2.add(new Chunk("ORSP Number\n",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc2 = new PdfPCell(c2);
        cellc2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc2.setNoWrap(true);
        cellc2.setBorderColor(Color.white);
        table.addCell(cellc2);
        
        Paragraph c3 = new Paragraph();
        c3.add(new Chunk("PI Name\nProject Title\nSponsor Name",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc3 = new PdfPCell(c3);
        cellc3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellc3.setColspan(2);
        cellc3.setNoWrap(true);
        cellc3.setBorderColor(Color.white);
        table.addCell(cellc3);

        Paragraph c4 = new Paragraph();
        c4.add(new Chunk("Total\nProject Period\n(Start-End Date)",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc4 = new PdfPCell(c4);
        cellc4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc4.setNoWrap(true);
        cellc4.setBorderColor(Color.white);
        table.addCell(cellc4);

        Paragraph c5 = new Paragraph();
        c5.add(new Chunk("Total Project\nAmount",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc5 = new PdfPCell(c5);
        cellc5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc5.setNoWrap(true);
        cellc5.setBorderColor(Color.white);
        table.addCell(cellc5);

        Paragraph c6 = new Paragraph();
        c6.add(new Chunk("Period\nStart Date",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc6 = new PdfPCell(c6);
        cellc6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc6.setNoWrap(true);
        cellc6.setBorderColor(Color.white);
        table.addCell(cellc6);

        Paragraph c7 = new Paragraph();
        c7.add(new Chunk("Period\nEnd Date",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc7 = new PdfPCell(c7);
        cellc7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc7.setNoWrap(true);
        cellc7.setBorderColor(Color.white);
        table.addCell(cellc7);

        Paragraph c8 = new Paragraph();
        c8.add(new Chunk("Requested\nPeriod\nDirect Cost",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc8 = new PdfPCell(c8);
        cellc8.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc8.setNoWrap(true);
        cellc8.setBorderColor(Color.white);
        table.addCell(cellc8);

        Paragraph c9 = new Paragraph();
        c9.add(new Chunk("Requested\nPeriod\nIndirect Cost",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc9 = new PdfPCell(c9);
        cellc9.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc9.setNoWrap(true);
        cellc9.setBorderColor(Color.white);
        table.addCell(cellc9);

        Paragraph c10 = new Paragraph();
        c10.add(new Chunk("Requested\nPeriod\nTotal Cost",
        		FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, Color.BLACK)));
        PdfPCell cellc10 = new PdfPCell(c10);
        cellc10.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc10.setNoWrap(true);
        cellc10.setBorderColor(Color.white);
        table.addCell(cellc10);

        Paragraph d = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));
        PdfPCell celld = new PdfPCell(d);
        celld.setColspan(11);
        celld.setNoWrap(true);
        celld.setBackgroundColor(Color.BLACK);
        table.addCell(celld);

        Paragraph d1 = new Paragraph();
        d1.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.WHITE)));
        PdfPCell celld1 = new PdfPCell(d1);
        celld1.setColspan(11);
        celld1.setNoWrap(true);
        celld1.setBackgroundColor(Color.WHITE);
        celld1.setBorderColor(Color.WHITE);
        table.addCell(celld1);
        
        Paragraph e = new Paragraph();
        e.add(new Chunk("New/Competing",
        		FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        PdfPCell celle = new PdfPCell(e);
        celle.setColspan(7);
        celle.setHorizontalAlignment(Element.ALIGN_LEFT);
        celle.setNoWrap(true);
        celle.setBackgroundColor(Color.lightGray);
        celle.setBorderColor(Color.lightGray);
        table.addCell(celle);

        Paragraph f = new Paragraph();
        f.add(new Chunk("",
        		FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        PdfPCell cellf = new PdfPCell(f);
        cellf.setColspan(4);
        cellf.setNoWrap(true);
        cellf.setBackgroundColor(Color.white);
        cellf.setBorderColor(Color.white);
        table.addCell(cellf);
    }
    
    ////////////////////////////////////////////////////////////////////////
    // METHOD TO PROCESS NON-HEADER RECORD
    private void FormatRecord(HashMap row, PdfPTable table)
    	throws DocumentException
    {
        PdfPTable subtable  = new PdfPTable(11);
        subtable.getDefaultCell().setBorder(0);
        subtable.setHorizontalAlignment(0);
        subtable.setTotalWidth(width - 72);
        subtable.setLockedWidth(true);

        // FIRST ROW
	Object cellSUBMITTED_DATE   = row.get("SUBMITTED_DATE");
	String strSUBMITTED_DATE = getCellValue(cellSUBMITTED_DATE).trim();
        FormatDataCell(subtable,strSUBMITTED_DATE,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,1);

	Object cellPROPOSAL_NUMBER   = row.get("PROPOSAL_NUMBER");
	String strPROPOSAL_NUMBER = getCellValue(cellPROPOSAL_NUMBER).trim();
        FormatDataCell(subtable,strPROPOSAL_NUMBER,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,1);

	Object cellPI_NAME    = row.get("PI_NAME");
	String strPI_NAME = getCellValue(cellPI_NAME).trim();
        FormatDataCell(subtable,strPI_NAME,Color.white,Color.white,Element.ALIGN_LEFT,Font.BOLD,7,2);

	Object cellREQUESTED_START_DATE_INITIAL = row.get("REQUESTED_START_DATE_INITIAL");
	Object cellREQUESTED_END_DATE_INITIAL = row.get("REQUESTED_END_DATE_INITIAL");
	String strREQUESTED_START_DATE_INITIAL = getCellValue(cellREQUESTED_START_DATE_INITIAL).trim();
	String strREQUESTED_END_DATE_INITIAL  = getCellValue(cellREQUESTED_END_DATE_INITIAL).trim();
        String strOutDates = strREQUESTED_START_DATE_INITIAL + "-" + strREQUESTED_END_DATE_INITIAL;
        FormatDataCell(subtable,strOutDates,Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,7,1);

	Object cellTOTAL_DIRECT_COST_TOTAL = row.get("TOTAL_DIRECT_COST_TOTAL");
	Object cellTOTAL_INDIRECT_COST_TOTAL = row.get("TOTAL_INDIRECT_COST_TOTAL");
	double dTOTAL_DIRECT_COST_TOTAL    = getNumericCellValue(cellTOTAL_DIRECT_COST_TOTAL);
	double dTOTAL_INDIRECT_COST_TOTAL  = getNumericCellValue(cellTOTAL_INDIRECT_COST_TOTAL);
        double dProjectCost = dTOTAL_DIRECT_COST_TOTAL + dTOTAL_INDIRECT_COST_TOTAL;
	runningTotalProjectAmount += dProjectCost;
        FormatDataCell(subtable,getCurrencyFormattedNumber(dProjectCost),Color.white,Color.white,Element.ALIGN_RIGHT,Font.PLAIN,7,1);

	Object cellREQUESTED_START_DATE_TOTAL = row.get("REQUESTED_START_DATE_TOTAL");
	String strREQUESTED_START_DATE_TOTAL = getCellValue(cellREQUESTED_START_DATE_TOTAL).trim();
        FormatDataCell(subtable,strREQUESTED_START_DATE_TOTAL,Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,7,1);

	Object cellREQUESTED_END_DATE_TOTAL = row.get("REQUESTED_END_DATE_TOTAL");
	String strREQUESTED_END_DATE_TOTAL = getCellValue(cellREQUESTED_END_DATE_TOTAL).trim();
        FormatDataCell(subtable,strREQUESTED_END_DATE_TOTAL,Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,7,1);

	Object cellTOTAL_DIRECT_COST_INITIAL = row.get("TOTAL_DIRECT_COST_INITIAL");
	double dTOTAL_DIRECT_COST_INITIAL = getNumericCellValue(cellTOTAL_DIRECT_COST_INITIAL);
	runningTotalDirectCost += dTOTAL_DIRECT_COST_INITIAL;
        FormatDataCell(subtable,getCurrencyFormattedNumber(dTOTAL_DIRECT_COST_INITIAL),Color.white,Color.white,Element.ALIGN_RIGHT,Font.PLAIN,7,1);

	Object cellTOTAL_INDIRECT_COST_INITIAL = row.get("TOTAL_INDIRECT_COST_INITIAL");
	double dTOTAL_INDIRECT_COST_INITIAL = getNumericCellValue(cellTOTAL_INDIRECT_COST_INITIAL);
	runningTotalIndirectCost += dTOTAL_INDIRECT_COST_INITIAL;
        FormatDataCell(subtable,getCurrencyFormattedNumber(dTOTAL_INDIRECT_COST_INITIAL),Color.white,Color.white,Element.ALIGN_RIGHT,Font.PLAIN,7,1);

	double dTotalCost = dTOTAL_INDIRECT_COST_INITIAL + dTOTAL_DIRECT_COST_INITIAL;
	runningTotalCost += dTotalCost;
        FormatDataCell(subtable,getCurrencyFormattedNumber(dTotalCost),Color.white,Color.white,Element.ALIGN_RIGHT,Font.PLAIN,7,1);

        // SECOND ROW
        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.BOLD,7,1);

        FormatDataCell(subtable,"",Color.white,Color.white,Element.ALIGN_LEFT,Font.BOLD,7,1);

	Object cellTITLE     = row.get("TITLE");
	String strTITLE = getCellValue(cellTITLE).trim();
        FormatDataCell(subtable,strTITLE,Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,7,9);

        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.BOLD,7,2);

	Object cellSPONSOR_NAME   = row.get("SPONSOR_NAME");
	String strSPONSOR_NAME = getCellValue(cellSPONSOR_NAME).trim();
        FormatDataCell(subtable,strSPONSOR_NAME,Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,7,9);

        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_LEFT,Font.BOLD,7,11);

        PdfPCell cell11 = new PdfPCell(subtable);
        cell11.setBorderColor(Color.white);
        cell11.setColspan(11);
        cell11.setNoWrap(true);
        table.addCell(cell11);
    }

        
    private void FormatFooter(PdfPTable table)
    	throws DocumentException
    {
        Paragraph a = new Paragraph();
        a.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.WHITE)));
        PdfPCell cella = new PdfPCell(a);
        cella.setColspan(11);
        cella.setNoWrap(true);
        cella.setBackgroundColor(Color.WHITE);
        cella.setBorderColor(Color.WHITE);
        table.addCell(cella);

        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.WHITE)));
        PdfPCell cellb = new PdfPCell(b);
        cellb.setNoWrap(true);
        cellb.setBackgroundColor(Color.WHITE);
        cellb.setBorderColor(Color.WHITE);
        table.addCell(cellb);

        Paragraph c = new Paragraph();
        c.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.PLAIN, Color.BLACK)));
        PdfPCell cellc = new PdfPCell(c);
        cellc.setColspan(10);
        cellc.setNoWrap(true);
        cellc.setBackgroundColor(Color.BLACK);
        cellc.setBorderColor(Color.BLACK);
        table.addCell(cellc);

        Paragraph d = new Paragraph();
        d.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.WHITE)));
        PdfPCell celld = new PdfPCell(d);
        celld.setNoWrap(true);
        celld.setBackgroundColor(Color.WHITE);
        celld.setBorderColor(Color.WHITE);
        table.addCell(celld);

        Paragraph d1 = new Paragraph();
        d1.add(new Chunk("Subtotals for:",
        		FontFactory.getFont(FontFactory.TIMES, 7, Font.PLAIN, Color.BLACK)));                    
        PdfPCell celld1 = new PdfPCell(d1);
        celld1.setHorizontalAlignment(Element.ALIGN_LEFT);
        celld1.setNoWrap(true);
        celld1.setBorderColor(Color.white);
        table.addCell(celld1);


        Paragraph e = new Paragraph();
        e.add(new Chunk("New/Competing",
        		FontFactory.getFont(FontFactory.TIMES, 7, Font.BOLD, Color.BLACK)));                    
        PdfPCell celle = new PdfPCell(e);
        celle.setColspan(2);
        celle.setHorizontalAlignment(Element.ALIGN_LEFT);
        celle.setNoWrap(true);
        celle.setBorderColor(Color.white);
        table.addCell(celle);

        Paragraph f = new Paragraph();
        f.add(new Chunk("Total Requested Project Amount:",
        		FontFactory.getFont(FontFactory.TIMES, 7, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cellf = new PdfPCell(f);
        cellf.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellf.setNoWrap(true);
        cellf.setBorderColor(Color.white);
        table.addCell(cellf);

        Paragraph g = new Paragraph();
        g.add(new Chunk(getCurrencyFormattedNumber(runningTotalProjectAmount),
        		FontFactory.getFont(FontFactory.TIMES, 7, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cellg = new PdfPCell(g);
        cellg.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellg.setNoWrap(true);
        cellg.setBorderColor(Color.white);
        table.addCell(cellg);

        Paragraph g1 = new Paragraph();
        g1.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 7, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cellg1 = new PdfPCell(g1);
        cellg1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellg1.setNoWrap(true);
        cellg1.setBackgroundColor(Color.white);
        cellg1.setBorderColor(Color.white);
        table.addCell(cellg1);

        Paragraph h = new Paragraph();
        h.add(new Chunk("Period Totals:",
        		FontFactory.getFont(FontFactory.TIMES, 7, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cellh = new PdfPCell(h);
        cellh.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellh.setNoWrap(true);
        cellh.setBorderColor(Color.white);
        table.addCell(cellh);

        Paragraph i = new Paragraph();
        i.add(new Chunk(getCurrencyFormattedNumber(runningTotalDirectCost),
        		FontFactory.getFont(FontFactory.TIMES, 7, Font.PLAIN, Color.BLACK)));                    
        PdfPCell celli = new PdfPCell(i);
        celli.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celli.setNoWrap(true);
        celli.setBorderColor(Color.white);
        table.addCell(celli);

        Paragraph j = new Paragraph();
        j.add(new Chunk(getCurrencyFormattedNumber(runningTotalIndirectCost),
        		FontFactory.getFont(FontFactory.TIMES, 7, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cellj = new PdfPCell(j);
        cellj.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellj.setNoWrap(true);
        cellj.setBorderColor(Color.white);
        table.addCell(cellj);

        Paragraph k = new Paragraph();
        k.add(new Chunk(getCurrencyFormattedNumber(runningTotalCost),
        		FontFactory.getFont(FontFactory.TIMES, 7, Font.PLAIN, Color.BLACK)));                    
        PdfPCell cellk = new PdfPCell(k);
        cellk.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellk.setNoWrap(true);
        cellk.setBorderColor(Color.white);
        table.addCell(cellk);
    }


        private String getUnitSchool(String SchoolName)
        {
            String strCand = "";
            if (SchoolName.compareTo("DS")==0)
                strCand = "New Jersey Dental School";
            
            else if (SchoolName.compareTo("NJMS") == 0)
                strCand = "New Jersey Medical School";
            else if (SchoolName.compareTo("RWJ") == 0)
                strCand = "Robert Wood Johnson Medical School";
            else if (SchoolName.compareTo("SOM") == 0)
                strCand = "School of Osteopathic Medicine";
            else if (SchoolName.compareTo("SHRP") == 0)
                strCand = "School of Health-Related Professions";
            else if (SchoolName.compareTo("SPH") == 0)
                strCand = "School of Public Health";
            else if (SchoolName.compareTo("SN") == 0)
                strCand = "School of Nursing";
            return strCand;
        }
         
    public void getSpecificXLSOutput(HttpServletResponse res)
         throws ServletException, IOException
    {
        Vector result = pobj.getSubmittedProposals(SchoolName);
        if (result != null && result.size() > 0)
        {
            HashMap dbrow = null;
	    HSSFWorkbook wb = new HSSFWorkbook();
            String UnitSchool = getUnitSchool(SchoolName);
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
			cell.setCellValue("PROPOSAL_NUMBER");
                cell = row.createCell((short)1); 
			cell.setCellValue("SUBMITTED_DATE");
                cell = row.createCell((short)2); 
			cell.setCellValue("SEQUENCE_NUMBER");
                cell = row.createCell((short)3); 
			cell.setCellValue("PROPOSAL_TYPE_CODE");
                cell = row.createCell((short)4); 
			cell.setCellValue("PROPOSAL_TYPE_DESCRIPTION");
                cell = row.createCell((short)5); 
			cell.setCellValue("CURRENT_ACCOUNT_NUMBER");
                cell = row.createCell((short)6); 
			cell.setCellValue("TITLE");
                cell = row.createCell((short)7); 
			cell.setCellValue("SPONSOR_CODE");
                cell = row.createCell((short)8); 
			cell.setCellValue("SPONSOR_NAME");
                cell = row.createCell((short)9); 
			cell.setCellValue("REQUESTED_START_DATE_INITIAL");
                cell = row.createCell((short)10); 
			cell.setCellValue("REQUESTED_START_DATE_TOTAL");
                cell = row.createCell((short)11); 
			cell.setCellValue("REQUESTED_END_DATE_INITIAL");
                cell = row.createCell((short)12); 
			cell.setCellValue("REQUESTED_END_DATE_TOTAL");
                cell = row.createCell((short)13); 
			cell.setCellValue("TOTAL_DIRECT_COST_INITIAL");
                cell = row.createCell((short)14); 
			cell.setCellValue("TOTAL_DIRECT_COST_TOTAL");
                cell = row.createCell((short)15); 
			cell.setCellValue("TOTAL_INDIRECT_COST_INITIAL");
                cell = row.createCell((short)16); 
			cell.setCellValue("TOTAL_INDIRECT_COST_TOTAL");
                cell = row.createCell((short)17); 
			cell.setCellValue("PI_NAME");
                cell = row.createCell((short)18); 
			cell.setCellValue("LEAD_UNIT");
                cell = row.createCell((short)19); 
			cell.setCellValue("ACTIVITY_TYPE_CODE");
                cell = row.createCell((short)20); 
			cell.setCellValue("ACTIVITY_TYPE_DESCRIPTION");

                     //Make Headers BOLD

           	cell.setCellStyle(headerStyle);
            int listSize = result.size();
            
            for (int i=0; i < listSize; i++) 
            {
                dbrow=(HashMap)result.elementAt(i); 
                row = sheet.createRow((short)i + 1); //+1 since First Row is Header Row

                    Object str1 = dbrow.get("PROPOSAL_NUMBER");
                    Object str2 = dbrow.get("SUBMITTED_DATE");
                    Object str3 = dbrow.get("SEQUENCE_NUMBER");
                    Object str4 = dbrow.get("PROPOSAL_TYPE_CODE");
                    Object str5 = dbrow.get("PROPOSAL_TYPE_DESCRIPTION");
                    Object str6 = dbrow.get("CURRENT_ACCOUNT_NUMBER");
                    Object str7 = dbrow.get("TITLE");
                    Object str8 = dbrow.get("SPONSOR_CODE");
                    Object str9 = dbrow.get("SPONSOR_NAME");
                    Object str10 = dbrow.get("REQUESTED_START_DATE_INITIAL");
                    Object str11 = dbrow.get("REQUESTED_START_DATE_TOTAL");
                    Object str12 = dbrow.get("REQUESTED_END_DATE_INITIAL");
                    Object str13 = dbrow.get("REQUESTED_END_DATE_TOTAL");
                    Object str14 = dbrow.get("TOTAL_DIRECT_COST_INITIAL");
                    Object str15 = dbrow.get("TOTAL_DIRECT_COST_TOTAL");
                    Object str16 = dbrow.get("TOTAL_INDIRECT_COST_INITIAL");
                    Object str17 = dbrow.get("TOTAL_INDIRECT_COST_TOTAL");
                    Object str18 = dbrow.get("PI_NAME");
                    Object str19 = dbrow.get("LEAD_UNIT");
                    Object str20 = dbrow.get("ACTIVITY_TYPE_CODE");
                    Object str21 = dbrow.get("ACTIVITY_TYPE_DESCRIPTION");

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
                    addToXLSCell(str15,14,row);
                    addToXLSCell(str16,15,row);
                    addToXLSCell(str17,16,row);
                    addToXLSCell(str18,17,row);
                    addToXLSCell(str19,18,row);
                    addToXLSCell(str20,19,row);
                    addToXLSCell(str21,20,row);
            }

            wb.write(out);
            out.close();
       }
       else
       {
           res.setContentType("text/html");
           PrintWriter writer = res.getWriter();
           writer.println("<html><body>");
           String UnitSchool = getUnitSchool(SchoolName);
           writer.print(UnitSchool + " does not have grants");
           writer.println("</body></html>"); 
       }

    }    
    public String getSpecificCSVOutput()
    {
        Vector result = pobj.getSubmittedProposals(SchoolName);
        StringBuffer buf = new StringBuffer("");
        if (result != null && result.size() > 0)
        {
            HashMap row = null;
            int listSize = 0;
            if (result != null)
               listSize = result.size();
             if (listSize > 0)
             {
                buf.append(printPlainObj("PROPOSAL_NUMBER"));
                buf.append(",");
                buf.append(printPlainObj("SUBMITTED_DATE"));
                buf.append(",");
                buf.append(printPlainObj("SEQUENCE_NUMBER"));
                buf.append(",");
                buf.append(printPlainObj("PROPOSAL_TYPE_CODE"));
                buf.append(",");
                buf.append(printPlainObj("PROPOSAL_TYPE_DESCRIPTION"));
                buf.append(",");
                buf.append(printPlainObj("CURRENT_ACCOUNT_NUMBER"));
                buf.append(",");
                buf.append(printPlainObj("TITLE"));
                buf.append(",");
                buf.append(printPlainObj("SPONSOR_CODE"));
                buf.append(",");
                buf.append(printPlainObj("SPONSOR_NAME"));
                buf.append(",");
                buf.append(printPlainObj("REQUESTED_START_DATE_INITIAL"));
                buf.append(",");
                buf.append(printPlainObj("REQUESTED_START_DATE_TOTAL"));
                buf.append(",");
                buf.append(printPlainObj("REQUESTED_END_DATE_INITIAL"));
                buf.append(",");
                buf.append(printPlainObj("REQUESTED_END_DATE_TOTAL"));
                buf.append(",");
                buf.append(printPlainObj("TOTAL_DIRECT_COST_INITIAL"));
                buf.append(",");
                buf.append(printPlainObj("TOTAL_DIRECT_COST_TOTAL"));
                buf.append(",");
                buf.append(printPlainObj("TOTAL_INDIRECT_COST_INITIAL"));
                buf.append(",");
                buf.append(printPlainObj("TOTAL_INDIRECT_COST_TOTAL"));
                buf.append(",");
                buf.append(printPlainObj("PI_NAME"));
                buf.append(",");
                buf.append(printPlainObj("LEAD_UNIT"));
                buf.append(",");
                buf.append(printPlainObj("ACTIVITY_TYPE_CODE"));
                buf.append(",");
                buf.append(printPlainObj("ACTIVITY_TYPE_DESCRIPTION"));
                buf.append("\n");
                
                for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
                    row=(HashMap)result.elementAt(rowNum); 
                    Object str1 = row.get("PROPOSAL_NUMBER");
                    Object str2 = row.get("SUBMITTED_DATE");
                    Object str3= row.get("SEQUENCE_NUMBER");
                    Object str4 = row.get("PROPOSAL_TYPE_CODE");
                    Object str5 = row.get("PROPOSAL_TYPE_DESCRIPTION");
                    Object str6 = row.get("CURRENT_ACCOUNT_NUMBER");
                    Object str7 = row.get("TITLE");
                    Object str8 = row.get("SPONSOR_CODE");
                    Object str9 = row.get("SPONSOR_NAME");
                    Object str10 = row.get("REQUESTED_START_DATE_INITIAL");
                    Object str11 = row.get("REQUESTED_START_DATE_TOTAL");
                    Object str12 = row.get("REQUESTED_END_DATE_INITIAL");
                    Object str13 = row.get("REQUESTED_END_DATE_TOTAL");
                    Object str14 = row.get("TOTAL_DIRECT_COST_INITIAL");
                    Object str15 = row.get("TOTAL_DIRECT_COST_TOTAL");
                    Object str16 = row.get("TOTAL_INDIRECT_COST_INITIAL");
                    Object str17 = row.get("TOTAL_INDIRECT_COST_TOTAL");
                    Object str18 = row.get("PI_NAME");
                    Object str19 = row.get("LEAD_UNIT");
                    Object str20 = row.get("ACTIVITY_TYPE_CODE");
                    Object str21 = row.get("ACTIVITY_TYPE_DESCRIPTION");
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
                        buf.append(",");
                        buf.append(printPlainObj(str15));
                        buf.append(",");
                        buf.append(printPlainObj(str16));
                        buf.append(",");
                        buf.append(printPlainObj(str17));
                        buf.append(",");
                        buf.append(printPlainObj(str18));
                        buf.append(",");
                        buf.append(printPlainObj(str19));
                        buf.append(",");
                        buf.append(printPlainObj(str20));
                        buf.append(",");
                        buf.append(printPlainObj(str21));
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
    
    private void InitializeDateFilter(String DateFilter)
    {
        iDateFilter = 0;
        try {
            int aindex = DateFilter.indexOf("-");
            
            // code snippet to handle year-only intances
            if (aindex < 0)
            {
                iDateFilter = Integer.parseInt(DateFilter);
                String strDateBegin = DateFilter + "-01-01 00:00:00.000000000";
                String strDateEnd = DateFilter + "-12-31 11:59:59.999999999";
                begindate = Timestamp.valueOf(strDateBegin);
                enddate = Timestamp.valueOf(strDateEnd);
            }
            else
            { 
                String strMonth = DateFilter.substring(0,aindex);
                String strYear = DateFilter.substring(aindex+1);
                iDateFilter = Integer.parseInt(strYear);
                int beginmonth = Integer.parseInt(strMonth);
                int endmonth = beginmonth + 1;
                int beginyear = Integer.parseInt(strYear);
                int endyear = beginyear;
                if (beginmonth == 12)
                {
                    endmonth = 1;
                    endyear = beginyear + 1;
                }
                     
                /* Note: There appears to be a bug with TimeStamp using valueOf string.
                 * Bug found in JDK 1.4.2_13. I have resorted to using the old Timestamp
                 * constructor. Although the constructor is deprecated, it functions as 
                 * it is supposed to. - R. Elizes 12/14/06
                 */
                begindate = new Timestamp(beginyear-1900,beginmonth,1,0,0,0,0);
                enddate = new Timestamp(endyear-1900,endmonth,1,0,0,0,0);
            }
        }
        catch (NumberFormatException ex) {
            System.out.println("NumberFormatException : Fiscal Year "+DateFilter+" is not recognized.");
            ex.printStackTrace(System.out);
        }
        catch (IllegalArgumentException ex) {
            System.out.println("IllegalArgumentException: Fiscal Year "+DateFilter+" is not recognized.");
            ex.printStackTrace(System.out);
        }
    }
    
    
}


