/**
 * $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GetSpecReviewProposalsBean.java,v 1.1.2.8 2007/09/18 18:23:05 cvs Exp $
 * $Log: GetSpecReviewProposalsBean.java,v $
 * Revision 1.1.2.8  2007/09/18 18:23:05  cvs
 * Modify code for GAFA and START_DATE handling
 *
 * Revision 1.1.2.7  2007/08/16 15:30:09  cvs
 * Add support for Headers per page
 *
 * Revision 1.1.2.6  2007/08/02 19:19:05  cvs
 * Enforce Snippet integrity in PDF output
 *
 * Revision 1.1.2.5  2007/06/11 15:39:13  cvs
 * Make sure that Sponsor Name and Project Title have nowrap = FALSE
 *
 * Revision 1.1.2.4  2007/02/14 16:34:38  cvs
 * Check for empty reports
 *
 * Revision 1.1.2.3  2007/02/08 14:23:24  cvs
 * Fix duplicate proposal number found by Therese
 *
 * Revision 1.1.2.2  2007/02/07 20:04:26  cvs
 * Modifications per Therese recommendations
 *
 * Revision 1.1.2.1  2007/02/06 20:51:48  cvs
 * Add support for Special Review Proposal Reports
 *
 *
 */
/*
 * @(#)GetSpecReviewProposalsBean.java 
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

public class GetSpecReviewProposalsBean extends ReportingBaseBean
{
    private int runningTotalProjectAmount;
    private int runningTotalDirectCost;
    private int runningTotalIndirectCost;
    private int runningTotalCost;
    private Vector specVector;

    private String SchoolName = "";
    private int year = 0;
    private int month = 0;
    private int iDateFilter = 0;
    private Timestamp begindate = null;
    private Timestamp enddate = null;
    
    private int globalTableFont = 8;
    
    private UMDNJSQLUtilities pobj = null;


    /** Creates a new instance of GetSpecReviewProposalsBean */
    public GetSpecReviewProposalsBean() 
    {
	super();
        Initialization();
    }
    
    public GetSpecReviewProposalsBean(String SchoolName, String DateFilter)
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
        Vector result = pobj.getSpecReviewProposals(SchoolName);
        StringBuffer buf = new StringBuffer("");
        if (result != null && result.size() > 0)
        {
            HashMap row = null;
            int listSize = 0;
            if (result != null)
               listSize = result.size();
             if (listSize > 0)
             {
                buf.append("<h2>Proposals for Pending Special Reviews for "+SchoolName+"</h2>");
		buf.append("<table border=1>");	
                buf.append("<tr>");
                buf.append(printObj("<b><u>PROPOSAL_NUMBER</u></b>"));
                buf.append(printObj("<b><u>SUBMITTED_DATE</u></b>"));
                buf.append(printObj("<b><u>SEQUENCE_NUMBER</u></b>"));
                buf.append(printObj("<b><u>PROPOSAL_TYPE_CODE</u></b>"));
                buf.append(printObj("<b><u>PROPOSAL_TYPE_DESCRIPTION</u></b>"));
//JM 5-27-2011 change account to center per 4.4.2
                buf.append(printObj("<b><u>CENTER_NUMBER</u></b>"));
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
                buf.append(printObj("<b><u>STATUS_CODE</u></b>"));
                buf.append(printObj("<b><u>APPROVAL_TYPE_CODE</u></b>"));
                buf.append(printObj("<b><u>PROTOCOL_NUMBER</u></b>"));
                buf.append(printObj("<b><u>APPLICATION_DATE</u></b>"));
                buf.append(printObj("<b><u>DESCRIPTION</u></b>"));
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
                    Object str20 = row.get("STATUS_CODE");
                    Object str21 = row.get("APPROVAL_TYPE_CODE");
                    Object str22 = row.get("PROTOCOL_NUMBER");
                    Object str23 = row.get("APPLICATION_DATE");
                    Object str24 = row.get("DESCRIPTION");
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
                        buf.append(printObj(str22));
                        buf.append(printObj(str23));
                        buf.append(printObj(str24));
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
        Vector result = pobj.getSpecReviewProposals(SchoolName);
        if (result != null && result.size() > 0)
        {
            specVector = new Vector();
            Document document = new Document(PageSize.LETTER.rotate());
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
 
                    PdfPTable table  = new PdfPTable(9);
                    table.getDefaultCell().setBorder(0);
                    table.setHorizontalAlignment(0);
                    table.setTotalWidth(width - 72);
                    table.setLockedWidth(true);
                    table.setHeaderRows(5);
                    FormatHeader(table);                
	    ////////////////////////////////////////////////////////////////////////
            // ITERATE OVER EACH HASHMAP ROW
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
                        {
                            int curnum = addToSpecVector(row,inum,result);
                            inum = curnum;
                        }
                    }
                }
                else
                {
                    int curnum = addToSpecVector(row,inum,result);
                    inum = curnum;
                }
            }

            for (int jnum = 0; jnum < specVector.size(); jnum++)
            {
                SpecReviewObject obj = (SpecReviewObject)specVector.elementAt(jnum);
                FormatRecord(obj,table);
            }


            document.add(table);
            table.setWidthPercentage(100);
          }
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
    	document.close();
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
        a.add(new Chunk(SchoolName+" - Proposals for Pending Special Reviews",
        		FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD, Color.BLACK)));                    
        PdfPCell cell1 = new PdfPCell(a);
        cell1.setColspan(9);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setNoWrap(true);
        cell1.setBorderColor(Color.white);
        table.addCell(cell1);

        Paragraph b1 = new Paragraph();
        b1.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.WHITE)));
        PdfPCell cellb1 = new PdfPCell(b1);
        cellb1.setColspan(9);
        cellb1.setNoWrap(true);
        cellb1.setBackgroundColor(Color.WHITE);
        cellb1.setBorderColor(Color.WHITE);
        table.addCell(cellb1);
        
        
        Paragraph b = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));
        PdfPCell cell2 = new PdfPCell(b);
        cell2.setColspan(9);
        cell2.setNoWrap(true);
        cell2.setBackgroundColor(Color.BLACK);
        table.addCell(cell2);
        
        Paragraph c1 = new Paragraph();
        c1.add(new Chunk("Submission\nDate",
        		FontFactory.getFont(FontFactory.HELVETICA, globalTableFont, Font.BOLD, Color.BLACK)));
        PdfPCell cellc1 = new PdfPCell(c1);
        cellc1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellc1.setNoWrap(true);
        cellc1.setBorderColor(Color.white);
        table.addCell(cellc1);

        Paragraph c2 = new Paragraph();
        c2.add(new Chunk("Proposal Number\n",
        		FontFactory.getFont(FontFactory.HELVETICA, globalTableFont, Font.BOLD, Color.BLACK)));
        PdfPCell cellc2 = new PdfPCell(c2);
        cellc2.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellc2.setNoWrap(true);
        cellc2.setBorderColor(Color.white);
        table.addCell(cellc2);
        
        Paragraph c3 = new Paragraph();
        c3.add(new Chunk("PI Name\nProject Title\nSponsor Name",
        		FontFactory.getFont(FontFactory.HELVETICA, globalTableFont, Font.BOLD, Color.BLACK)));
        PdfPCell cellc3 = new PdfPCell(c3);
        cellc3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellc3.setColspan(2);
        cellc3.setNoWrap(true);
        cellc3.setBorderColor(Color.white);
        table.addCell(cellc3);

        Paragraph c4 = new Paragraph();
        c4.add(new Chunk("Proposal Status",
        		FontFactory.getFont(FontFactory.HELVETICA, globalTableFont, Font.BOLD, Color.BLACK)));
        PdfPCell cellc4 = new PdfPCell(c4);
        cellc4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc4.setNoWrap(true);
        cellc4.setBorderColor(Color.white);
        table.addCell(cellc4);

        Paragraph c5a = new Paragraph();
        c5a.add(new Chunk("Special Review\nDescription",
        		FontFactory.getFont(FontFactory.HELVETICA, globalTableFont, Font.BOLD, Color.BLACK)));
        PdfPCell cellc5a = new PdfPCell(c5a);
        cellc5a.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc5a.setNoWrap(true);
        cellc5a.setBorderColor(Color.white);
        table.addCell(cellc5a);

        Paragraph c5 = new Paragraph();
        c5.add(new Chunk("Special Review Status",
        		FontFactory.getFont(FontFactory.HELVETICA, globalTableFont, Font.BOLD, Color.BLACK)));
        PdfPCell cellc5 = new PdfPCell(c5);
        cellc5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc5.setNoWrap(true);
        cellc5.setBorderColor(Color.white);
        table.addCell(cellc5);

        Paragraph c6 = new Paragraph();
        c6.add(new Chunk("Protocol Number",
        		FontFactory.getFont(FontFactory.HELVETICA, globalTableFont, Font.BOLD, Color.BLACK)));
        PdfPCell cellc6 = new PdfPCell(c6);
        cellc6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc6.setNoWrap(true);
        cellc6.setBorderColor(Color.white);
        table.addCell(cellc6);

        Paragraph c7 = new Paragraph();
        c7.add(new Chunk("Application Date",
        		FontFactory.getFont(FontFactory.HELVETICA, globalTableFont, Font.BOLD, Color.BLACK)));
        PdfPCell cellc7 = new PdfPCell(c7);
        cellc7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellc7.setNoWrap(true);
        cellc7.setBorderColor(Color.white);
        table.addCell(cellc7);

        Paragraph d = new Paragraph();
        b.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.BLACK)));
        PdfPCell celld = new PdfPCell(d);
        celld.setColspan(9);
        celld.setNoWrap(true);
        celld.setBackgroundColor(Color.BLACK);
        table.addCell(celld);

        Paragraph d1 = new Paragraph();
        d1.add(new Chunk("",
        		FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD, Color.WHITE)));
        PdfPCell celld1 = new PdfPCell(d1);
        celld1.setColspan(9);
        celld1.setNoWrap(true);
        celld1.setBackgroundColor(Color.WHITE);
        celld1.setBorderColor(Color.WHITE);
        table.addCell(celld1);
        
    }
    
    ////////////////////////////////////////////////////////////////////////
    // METHOD TO PROCESS NON-HEADER RECORD
    private void FormatRecord(SpecReviewObject obj, PdfPTable table)
    	throws DocumentException
    {
        PdfPTable subtable  = new PdfPTable(9);
        subtable.getDefaultCell().setBorder(0);
        subtable.setHorizontalAlignment(0);
        subtable.setTotalWidth(width - 72);
        subtable.setLockedWidth(true);

        FormatDataCell(subtable,obj.getSubmissiondate(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,globalTableFont,1);
        FormatDataCell(subtable,obj.getProposalnumber(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,globalTableFont,1);
        FormatDataCell(subtable,obj.getPiname(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,globalTableFont,2);

        Paragraph d = new Paragraph();
        String strStatus = "Pending";
        String strSTATUS_CODE = obj.getProjectstatus();

        if (strSTATUS_CODE.compareTo("2")==0)
        {
            strStatus = "FUNDED";
            d.add(new Chunk(strStatus,
        		FontFactory.getFont(FontFactory.TIMES, globalTableFont, Font.BOLD, Color.RED)));                    
        }
        else
            d.add(new Chunk(strStatus,
        		FontFactory.getFont(FontFactory.TIMES, globalTableFont, Font.BOLD, Color.BLACK)));                    
        PdfPCell celld = new PdfPCell(d);
        celld.setHorizontalAlignment(Element.ALIGN_CENTER);
        celld.setNoWrap(true);
        celld.setBorderColor(Color.white);
        subtable.addCell(celld);

        FormatDataCell(subtable,obj.getSpecialreviewdescription(),Color.white,Color.white,Element.ALIGN_LEFT,Font.PLAIN,globalTableFont,1);
        FormatDataCell(subtable,"Pending",Color.white,Color.white,Element.ALIGN_CENTER,Font.BOLD,globalTableFont,1);
        FormatDataCell(subtable,obj.getProtocolnumber(),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,globalTableFont,1);
        FormatDataCell(subtable,obj.getApplicationdate(),Color.white,Color.white,Element.ALIGN_CENTER,Font.PLAIN,globalTableFont,1);
        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_CENTER,Font.BOLD,globalTableFont,1);
        FormatDataCell(subtable,"",Color.white,Color.white,Element.ALIGN_CENTER,Font.BOLD,globalTableFont,1);
        FormatDataCell(subtable,obj.getProjecttitle(),Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,globalTableFont,7);
        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_CENTER,Font.BOLD,globalTableFont,2);
        FormatDataCell(subtable,obj.getSponsorname(),Color.white,Color.white,Element.ALIGN_LEFT,Font.ITALIC,globalTableFont,7);
        FormatDataCell(subtable," ",Color.white,Color.white,Element.ALIGN_CENTER,Font.BOLD,globalTableFont,9);

        PdfPCell cell11 = new PdfPCell(subtable);
        cell11.setBorderColor(Color.white);
        cell11.setColspan(9);
        cell11.setNoWrap(true);
        table.addCell(cell11);
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
        Vector result = pobj.getSpecReviewProposals(SchoolName);
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
			cell.setCellValue("REQUESTED_END_DATE_TOTAL	");
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
			cell.setCellValue("STATUS_CODE");
                cell = row.createCell((short)20); 
			cell.setCellValue("APPROVAL_TYPE_CODE");
                cell = row.createCell((short)21); 
			cell.setCellValue("PROTOCOL_NUMBER");
                cell = row.createCell((short)22); 
			cell.setCellValue("APPLICATION_DATE");
                cell = row.createCell((short)23); 
			cell.setCellValue("DESCRIPTION");

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
                    Object str20 = dbrow.get("STATUS_CODE");
                    Object str21 = dbrow.get("APPROVAL_TYPE_CODE");
                    Object str22 = dbrow.get("PROTOCOL_NUMBER");
                    Object str23 = dbrow.get("APPLICATION_DATE");
                    Object str24 = dbrow.get("DESCRIPTION");

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
                    addToXLSCell(str22,21,row);
                    addToXLSCell(str23,22,row);
                    addToXLSCell(str24,23,row);
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
        Vector result = pobj.getSpecReviewProposals(SchoolName);
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
                buf.append(printPlainObj("STATUS_CODE"));
                buf.append(",");
                buf.append(printPlainObj("APPROVAL_TYPE_CODE"));
                buf.append(",");
                buf.append(printPlainObj("PROTOCOL_NUMBER"));
                buf.append(",");
                buf.append(printPlainObj("APPLICATION_DATE"));
                buf.append(",");
                buf.append(printPlainObj("DESCRIPTION"));
                buf.append("\n");
                
                for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
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
                    Object str20 = row.get("STATUS_CODE");
                    Object str21 = row.get("APPROVAL_TYPE_CODE");
                    Object str22 = row.get("PROTOCOL_NUMBER");
                    Object str23 = row.get("APPLICATION_DATE");
                    Object str24 = row.get("DESCRIPTION");
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
                        buf.append(",");
                        buf.append(printPlainObj(str22));
                        buf.append(",");
                        buf.append(printPlainObj(str23));
                        buf.append(",");
                        buf.append(printPlainObj(str24));
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
    
    private int addToSpecVector(HashMap row, int inum, Vector result)
    {
       SpecReviewObject obj = new SpecReviewObject();

       Object cellSUBMITTED_DATE   = row.get("SUBMITTED_DATE");
       String strSUBMITTED_DATE = getCellValue(cellSUBMITTED_DATE).trim();
       obj.setSubmissiondate(strSUBMITTED_DATE);

       Object cellPROPOSAL_NUMBER   = row.get("PROPOSAL_NUMBER");
       String strPROPOSAL_NUMBER = getCellValue(cellPROPOSAL_NUMBER).trim();
       obj.setProposalnumber(strPROPOSAL_NUMBER);

       Object cellPI_NAME    = row.get("PI_NAME");
       String strPI_NAME = getCellValue(cellPI_NAME).trim();
       obj.setPiname(strPI_NAME);

       Object cellSTATUS_CODE = row.get("STATUS_CODE");
       String strSTATUS_CODE = getCellValue(cellSTATUS_CODE).trim();
       obj.setProjectstatus(strSTATUS_CODE);

       Object cellDESCRIPTION    = row.get("DESCRIPTION");
       String strDESCRIPTION = getCellValue(cellDESCRIPTION).trim();
       obj.setSpecialreviewdescription(strDESCRIPTION);

       Object cellAPPROVAL_TYPE_CODE    = row.get("APPROVAL_TYPE_CODE");
       String strAPPROVAL_TYPE_CODE = getCellValue(cellAPPROVAL_TYPE_CODE).trim();
       obj.setSpecialreviewstatus(strAPPROVAL_TYPE_CODE);

       Object cellPROTOCOL_NUMBER = row.get("PROTOCOL_NUMBER");
       String strPROTOCOL_NUMBER = getCellValue(cellPROTOCOL_NUMBER).trim();
       obj.setProtocolnumber(strPROTOCOL_NUMBER);

       Object cellAPPLICATION_DATE = row.get("APPLICATION_DATE");
       String strAPPLICATION_DATE = getCellValue(cellAPPLICATION_DATE).trim();
       obj.setApplicationdate(strAPPLICATION_DATE);

       Object cellTITLE     = row.get("TITLE");
       String strTITLE = getCellValue(cellTITLE).trim();
       obj.setProjecttitle(strTITLE);

       Object cellSPONSOR_NAME   = row.get("SPONSOR_NAME");
       String strSPONSOR_NAME = getCellValue(cellSPONSOR_NAME).trim();
       obj.setSponsorname(strSPONSOR_NAME);

       int newnum = inum + 1;
       for (int jnum = newnum; jnum < result.size(); jnum++)
       {
           
           HashMap row1 = (HashMap)result.elementAt(inum); 
           HashMap row2 = (HashMap)result.elementAt(jnum); 
           Object cellPROPOSALNUMBER1   = row1.get("PROPOSAL_NUMBER");
           String propnum1 = getCellValue(cellPROPOSALNUMBER1).trim();
           Object cellPROPOSALNUMBER2   = row2.get("PROPOSAL_NUMBER");
           String propnum2 = getCellValue(cellPROPOSALNUMBER2).trim();

           if (propnum1.compareTo(propnum2)==0)
           {
               Object objDesc = row2.get("DESCRIPTION");
               String strcand = getCellValue(objDesc).trim();
               obj.addSpecialreviewDescription(strcand);
               if (jnum == result.size()-1)
                  newnum = result.size();
           }
           else
           {
              newnum = jnum;
              break;
           }
       }
       specVector.addElement(obj);
       return newnum;
    }
    
}


