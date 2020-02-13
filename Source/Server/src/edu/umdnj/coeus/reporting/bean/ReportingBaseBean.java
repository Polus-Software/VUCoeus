/**
  $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/ReportingBaseBean.java,v 1.1.2.19 2007/09/19 16:28:44 cvs Exp $
  $Log: ReportingBaseBean.java,v $
  Revision 1.1.2.19  2007/09/19 16:28:44  cvs
  Pre-qualify Fiscal Year before Fiscal Year Type addresses most report anomalies

  Revision 1.1.2.18  2007/08/16 18:11:45  cvs
  1. Add support for sorting by PI and Department
  2. Move sorting methods to parent class

  Revision 1.1.2.17  2007/08/02 19:19:05  cvs
  Enforce Snippet integrity in PDF output

  Revision 1.1.2.16  2007/07/31 13:36:19  cvs
  Add Annual Reports server-side code
  Move many methods to base class Reporting Base Bean

  Revision 1.1.2.15  2007/07/25 18:40:02  cvs
  Add Headers and Snippet integrity in PDF

  Revision 1.1.2.14  2007/02/27 18:29:34  cvs
  Simulate date parsing routine of MIT folks

  Revision 1.1.2.13  2007/02/14 16:34:38  cvs
  Check for empty reports

  Revision 1.1.2.12  2007/01/17 15:26:35  cvs
  Add CSV Support

  Revision 1.1.2.11  2006/12/12 20:08:31  cvs
  Added styled PDF support for GetSubmittedProposals retrieval

  Revision 1.1.2.10  2006/12/08 19:46:45  cvs
  Add support for JFreeChart charting engine

  Revision 1.1.2.9  2006/12/07 18:10:57  cvs
  Assign currency and comma formatting to appropriate double values

  Revision 1.1.2.8  2006/12/05 17:23:57  cvs
  Add fiscal year filtering for PDF-based SOM model

  Revision 1.1.2.7  2006/12/04 20:20:59  cvs
  Add PDF format report based on SOM model

  Revision 1.1.2.6  2006/11/30 19:36:37  cvs
  Add XLS support for formatted output

  Revision 1.1.2.5  2006/11/30 13:51:04  cvs
  Add styled PDF functionality based on iText tool

  Revision 1.1.2.4  2006/11/28 19:51:19  cvs
  Added support for GetGrantsByDept retrieval

  Revision 1.1.2.3  2006/11/20 14:17:02  cvs
  Subclass ReportingBaseBean to two child classes for easy inheritance of methods

  Revision 1.1.2.2  2006/11/17 20:15:25  cvs
  Add basic PDF data retrieval and browser output mechanism

  Revision 1.1.2.1  2006/11/16 18:56:38  cvs
  Base bean for Reporting data retrieval and browser output mechanism

*
*/
/*
 * @(#)ReportingBaseBean.java 
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
import java.util.StringTokenizer;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Color;
import java.awt.Font;
import java.sql.Date;
import java.sql.Timestamp;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.PageSize;
import com.lowagie.text.Image;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.fop.apps.FOPException;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.budget.report.ReportGenerator;
import edu.mit.coeus.utils.DateUtils;

import org.apache.poi.hssf.usermodel.*;

import org.apache.fop.apps.FOPException;
import edu.umdnj.coeus.utilities.UMDNJStrings;

public class ReportingBaseBean 
         extends PdfPageEventHelper
{

    static final String         LT                  = "&lt;";
    static final String         GT                  = "&gt;";
    static final String         AMP                 = "&amp;";
    static final String         APOS                = "&apos;";
    static final String         QUOT                = "&quot;";
    
    protected int pdfcellfontsize = 8;

    // Instance of a dbEngine

    protected DBEngineImpl dbEngine;

    
    protected String FiscalYearType = "NONE";
    protected String FiscalYear = "NONE";

    protected int fiscalYear = 0;
    protected Timestamp begindate = null;
    protected Timestamp enddate = null;
    protected String Sort = "";

    protected float width;

    /** Creates a new instance of ReportingBaseBean */

    public ReportingBaseBean() 
    {
	super();
        dbEngine = new DBEngineImpl();

    }        

     
    public String xmlTranslate(String s)
    {
        if(s != null && s.length() > 0) { // it can be ""
            StringBuffer buff = new StringBuffer();
            char [] chars = s.toCharArray();
            int len = chars.length;
        
            for(int i = 0; i < len; i++)
            {
                if(chars[i] == '<'){
                    buff = buff.append(LT);
                }else if(chars[i] == '>') {
                    buff = buff.append(GT);
                }else if(chars[i] == '&') {
                    buff = buff.append(AMP);
                }else if(chars[i] == '\'') {
                    buff = buff.append(APOS);
                }else if(chars[i] == '\"') {
                    buff = buff.append(QUOT);
                }else {
                    buff = buff.append(chars[i]);
                }
            }
            return buff.toString();
        } else
            return null;
    }
    
    public String getHTMLOutput(Vector result, Vector vecFields)
    {
        HashMap row = null;
        StringBuffer buf = new StringBuffer("");
        
        int listSize = 0;
        if (result != null)
           listSize = result.size();
        if (listSize > 0)
        {
		buf.append("<table border=1>");
		buf.append("<tr>");
		for (int i = 0; i < vecFields.size(); i++)
		{
                	String fieldDescriptor = (String)vecFields.elementAt(i);
			buf.append("<td><b><u>");
			buf.append(fieldDescriptor);
			buf.append("</u></b></td>");
		}
		buf.append("</tr>");
		

            	for(int rowNum = 0; rowNum < listSize; rowNum++)
		{
			buf.append("<tr>");

                	row=(HashMap)result.elementAt(rowNum); 
			for (int i = 0; i < vecFields.size(); i++)
			{
                		String fieldDescriptor = (String)vecFields.elementAt(i);
				Object obj = row.get(fieldDescriptor);
                                buf.append(printObj(obj));
			}
			buf.append("</tr>");
		}

        	buf.append("</table>");
        }
        else
		buf.append(UMDNJStrings.NoRecordsComingBack);
        return buf.toString();
    }

    public void addToCell(Object obj, PdfPTable table)
    {
        StringBuffer buf = new StringBuffer("");
        if (obj instanceof String)
            buf.append((String)obj);
        else
        if (obj instanceof Timestamp)
        {
            try {
               buf.append(getCellValue(obj));
            }
            catch(ClassCastException ex)
            {
                ex.printStackTrace(System.out);
            }
         }
         else
         if (obj instanceof Number)
         {
            Number num = (Number)obj;
            buf.append(num.doubleValue());
         }
         else
         if (obj == null)
         {
            buf.append("NULL");
         }
         else
         {
             System.out.println("The class is " + obj.getClass().getName());
         }
       table.getDefaultCell().setBackgroundColor(Color.white);
       table.addCell(buf.toString());
    }

        public String printObj(Object obj)
    {
        StringBuffer buf = new StringBuffer("<td nowrap>");
        if (obj instanceof String)
        {
          buf.append((String)obj);
        }
        else
        if (obj instanceof Timestamp)
        {
            try {
               buf.append(getCellValue(obj));
            }
            catch(ClassCastException ex)
            {
                ex.printStackTrace(System.out);
            }
         }
         else
         if (obj instanceof Number)
         {
            Number num = (Number)obj;
            buf.append(num.doubleValue());
         }
         else
         if (obj == null)
         {
            buf.append("NULL");
         }
         else
         {
             System.out.println("The class is " + obj.getClass().getName());
         }
        buf.append("</td>");
        return buf.toString().trim();
    }

    public void addToXLSCell(Object obj, int inum, HSSFRow row)
    {
        StringBuffer buf = new StringBuffer("");
         HSSFCell cell = row.createCell((short)inum);        

        if (obj instanceof String)
        {
            cell.setCellValue((String)obj);
        }
        else
        if (obj instanceof Timestamp)
        {
            try {
                buf.append(getCellValue(obj));
                cell.setCellValue(buf.toString());
            }
            catch(ClassCastException ex)
            {
                ex.printStackTrace(System.out);
            }
         }
         else
         if (obj instanceof Number)
         {
            Number num = (Number)obj;
            cell.setCellValue(num.doubleValue());
         }
    }
    public String getCellValue(Object obj)
    {
    	StringBuffer buf = new StringBuffer("");
    	if (obj != null)
    	{
                if (obj instanceof String)
                    buf.append((String)obj);
                else
                if (obj instanceof Timestamp)
                {
                    try {
                        DateUtils dateUtils = new DateUtils();
                        Timestamp ts = (Timestamp)obj;
                        String dateString = ts.toString();
                        String dateDisplayString = dateUtils.formatDate(dateString, "MM/dd/yyyy");
                        buf.append(dateDisplayString);
                    }
                    catch(ClassCastException ex)
                    {
                        ex.printStackTrace(System.out);
                    }
                }
                else
                if (obj instanceof Number)
                {
                    Number num = (Number)obj;
                    buf.append(num.doubleValue());
                }
        }
    	return buf.toString();
    }

    public double getNumericCellValue(Object obj)
    {
    	double val = 0;
    	if (obj != null)
    	{
                if (obj instanceof Number)
                {
                    Number num = (Number)obj;
                    val = num.doubleValue();
                }
   	}
    	return val;
    }
    
    public Timestamp getTimeStampCellValue(Object obj)
    {
        StringBuffer buf = new StringBuffer("");
        Timestamp ts = null;
        try {
               ts = (Timestamp)obj;
        }
        catch(ClassCastException ex)
        {
                ex.printStackTrace(System.out);
         }
         return ts;
    }


	public String getFormattedDate(String strdate)
	{
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < strdate.length();i++)
		{
			if (strdate.charAt(i)=='-')
				buf.append("/");
			else
				buf.append(strdate.charAt(i));
		}
		return buf.toString();
	}
	
	public String getFormattedNumber(String strnum)
	{
		StringBuffer buf = new StringBuffer("");
		String strcand = strnum;
		buf.append("$");
		int index = strnum.indexOf('.');
		if (index > 0)
			strcand = strnum.substring(0,index);
		buf.append(strcand);
		return buf.toString();
	}
        
        public String getCurrencyFormattedNumber(double value)
        {
            DecimalFormat format = (DecimalFormat)NumberFormat.getCurrencyInstance();
            DecimalFormatSymbols dSymbols = new DecimalFormatSymbols();
            dSymbols.setGroupingSeparator(',');
            dSymbols.setCurrencySymbol("$");
            format.applyPattern("$###,###,###");
            format.setDecimalFormatSymbols(dSymbols);
            format.setMinimumIntegerDigits(1);
            return format.format(value);
        }


     public Vector getFields(String strSQL)
    {
     Vector v = new Vector();
     int bindex = strSQL.indexOf("select");
     int eindex = strSQL.indexOf("from ");
     if (bindex > -1)
        bindex += 6;
     if (eindex > -1)
        eindex--;

     String strFields = strSQL.substring(bindex,eindex);
     StringTokenizer Tokenizer = new StringTokenizer(strFields,",");
     while (Tokenizer.hasMoreTokens())
     {
         String what = Tokenizer.nextToken();
         int sindex = what.indexOf(".");
         if (sindex > 0)
         {
            what = what.substring(sindex+1);
            
         }
         v.addElement(what.toUpperCase().trim());
     }
         
     return v;
  }

        public String printPlainObj(Object obj)
    {
        StringBuffer buf = new StringBuffer("");
        if (obj instanceof String)
        {
          buf.append((String)obj);
        }
        else
        if (obj instanceof Timestamp)
        {
            try {
                buf.append(getCellValue(obj));
            }
            catch(ClassCastException ex)
            {
                ex.printStackTrace(System.out);
            }
         }
         else
         if (obj instanceof Number)
         {
            Number num = (Number)obj;
            buf.append(num.doubleValue());
         }
         else
         if (obj == null)
         {
            buf.append("NULL");
         }
         else
         {
             System.out.println("The class is " + obj.getClass().getName());
         }
        return buf.toString().trim();
    }

    ///////////////////////////////////////////////////////////////////
    // METHODS ASSOCIATED WITH PdfPageEventHelper


    public void onCloseDocument(PdfWriter writer, Document document) 
    {
    }

    public void onStartPage(PdfWriter writer, Document document) 
    {
    }

    public void onEndPage(PdfWriter writer, Document document) 
    {
    }

    ///////////////////////////////////////////////////////////////////
    // METHODS ASSOCIATED WITH Pdf Output

     public void FormatDataCell(PdfPTable table,
                                   String strvalue,
                                   Color bordercolor,
                                   Color backgroundcolor,
                                   int alignment,
                                   int fontstyle,
                                   int fontsize,
                                   int colspan)
     {
        Paragraph f = new Paragraph();
        f.add(new Chunk(strvalue,
        		FontFactory.getFont(FontFactory.HELVETICA, fontsize, fontstyle, Color.BLACK)));
        PdfPCell cell = new PdfPCell(f);
        cell.setHorizontalAlignment(alignment);
        cell.setBorderColor(bordercolor);
        cell.setBackgroundColor(backgroundcolor);
        cell.setColspan(colspan);
        cell.setNoWrap(false);
        table.addCell(cell);
     }

     public void FormatSubheaderCell(PdfPTable table,
                                   String strvalue,
                                   Color color,
                                   int colspan)
     {
        Paragraph f = new Paragraph();
        f.add(new Chunk(strvalue,
        		FontFactory.getFont(FontFactory.HELVETICA, pdfcellfontsize, Font.BOLD, Color.BLACK)));
        PdfPCell cell = new PdfPCell(f);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderColor(color);
        cell.setBackgroundColor(color);
        cell.setColspan(colspan);
        cell.setNoWrap(false);
        table.addCell(cell);
     }

     public void FormatSubfooterCell(PdfPTable table,
                                   String strvalue,
                                   Color color,
                                   int colspan,
                                   int alignment)
     {
        Paragraph f = new Paragraph();
        f.add(new Chunk(strvalue,
        		FontFactory.getFont(FontFactory.HELVETICA, (pdfcellfontsize+1), Font.PLAIN, Color.BLACK)));
        PdfPCell cell = new PdfPCell(f);
        cell.setHorizontalAlignment(alignment);
        cell.setBorderColor(color);
        cell.setBackgroundColor(color);
        cell.setColspan(colspan);
        cell.setNoWrap(false);
        table.addCell(cell);
     }


     public void FormatHeaderCell(PdfPTable table,
                                   String strvalue,
                                   Color bordercolor,
                                   Color backgroundcolor,
                                   int alignment,
                                   int fontsize,
                                   int colspan)
     {
        Paragraph f = new Paragraph();
        f.add(new Chunk(strvalue,
        		FontFactory.getFont(FontFactory.HELVETICA, fontsize, Font.BOLD, Color.BLACK)));
        PdfPCell cell = new PdfPCell(f);
        cell.setHorizontalAlignment(alignment);
        cell.setBorderColor(bordercolor);
        cell.setBackgroundColor(backgroundcolor);
        cell.setColspan(colspan);
        cell.setNoWrap(false);
        table.addCell(cell);
     }


    protected void InitializeFiscalYear(String FiscalYearType, String FiscalYear)
    {
	if (FiscalYear == null || FiscalYear.length()==0 || FiscalYear.compareTo("NONE")==0)
	{
		this.FiscalYear = "NONE";
		return;
	}
	if (FiscalYearType == null || FiscalYearType.length()==0 || FiscalYearType.compareTo("NONE")==0)
	{
		this.FiscalYearType = "NONE";
		return;
	}

        try {
		fiscalYear = Integer.parseInt(FiscalYear);
		int fiscalYearBegin = fiscalYear - 1;
		String strfiscalYearBegin = "";
		String strfiscalYearEnd = "";
		if (FiscalYearType.compareTo("STATE")==0)
		{
			strfiscalYearBegin = fiscalYearBegin + "-07-01 00:00:00.000000000";
			strfiscalYearEnd = fiscalYear + "-06-30 11:59:59.999999999";
		}
		else
		if (FiscalYearType.compareTo("NIH")==0)
		{
			strfiscalYearBegin = fiscalYearBegin + "-10-01 00:00:00.000000000";
			strfiscalYearEnd = fiscalYear + "-09-30 11:59:59.999999999";
		}
		else
		if (FiscalYearType.compareTo("CALENDAR")==0)
		{
			fiscalYearBegin = fiscalYear;
			strfiscalYearBegin = fiscalYearBegin + "-01-01 00:00:00.000000000";
			strfiscalYearEnd = fiscalYear + "-12-31 11:59:59.999999999";
		}
		begindate = Timestamp.valueOf(strfiscalYearBegin);
		enddate = Timestamp.valueOf(strfiscalYearEnd);
        }
        catch (NumberFormatException ex) {
            System.out.println("NumberFormatException : Fiscal Year "+FiscalYear+" is not recognized.");
            ex.printStackTrace(System.out);
        }
        catch (IllegalArgumentException ex) {
            System.out.println("IllegalArgumentException: Fiscal Year "+FiscalYear+" is not recognized.");
            ex.printStackTrace(System.out);
        }
    }
    
    protected void InitializeFiscalYear(String FiscalYear)
    {
        try {
            
            fiscalYear = Integer.parseInt(FiscalYear);
            int fiscalYearBegin = fiscalYear - 1;
            String strfiscalYearBegin = fiscalYearBegin + "-07-01 00:00:00.000000000";
            String strfiscalYearEnd = fiscalYear + "-06-30 11:59:59.999999999";
            begindate = Timestamp.valueOf(strfiscalYearBegin);
            enddate = Timestamp.valueOf(strfiscalYearEnd);
        }
        catch (NumberFormatException ex) {
            System.out.println("NumberFormatException : Fiscal Year "+FiscalYear+" is not recognized.");
            ex.printStackTrace(System.out);
        }
        catch (IllegalArgumentException ex) {
            System.out.println("IllegalArgumentException: Fiscal Year "+FiscalYear+" is not recognized.");
            ex.printStackTrace(System.out);
        }
    }
    
    

    protected Timestamp getFiscalYearBeginTimestamp(int tmpfiscalYear)
    {
        Timestamp tmpTimestamp;
	int fiscalYearBegin = tmpfiscalYear - 1;
	String strfiscalYearBegin = "";
	if (FiscalYearType.compareTo("STATE")==0)
	{
		strfiscalYearBegin = fiscalYearBegin + "-07-01 00:00:00.000000000";
	}
	else
	if (FiscalYearType.compareTo("NIH")==0)
	{
		strfiscalYearBegin = fiscalYearBegin + "-10-01 00:00:00.000000000";
	}
	else
	if (FiscalYearType.compareTo("CALENDAR")==0)
	{
		fiscalYearBegin = fiscalYear;
		strfiscalYearBegin = fiscalYearBegin + "-01-01 00:00:00.000000000";
	}
	tmpTimestamp = Timestamp.valueOf(strfiscalYearBegin);
        return tmpTimestamp;
    }
    

    protected Timestamp getFiscalYearEndTimestamp(int tmpfiscalYear)
    {
        Timestamp tmpTimestamp;
	String strfiscalYearEnd = "";
	if (FiscalYearType.compareTo("STATE")==0)
	{
		strfiscalYearEnd = tmpfiscalYear + "-06-30 11:59:59.999999999";
	}
	else
	if (FiscalYearType.compareTo("NIH")==0)
	{
		strfiscalYearEnd = tmpfiscalYear + "-09-30 11:59:59.999999999";
	}
	else
	if (FiscalYearType.compareTo("CALENDAR")==0)
	{
		strfiscalYearEnd = tmpfiscalYear + "-12-31 11:59:59.999999999";
	}
	tmpTimestamp = Timestamp.valueOf(strfiscalYearEnd);
        return tmpTimestamp;
    }

   protected Vector sortIncomingVectorByTotalCost(Vector avector)
   {
      ArrayList alist = new ArrayList(avector);

      // Sort vector by DIMEN_NAME.
      Collections.sort(alist,
         new Comparator()
         {
            public int compare(Object o1, Object o2)
            {
               int icompare = 0;
               PIGraphObject f1 = (PIGraphObject)o1;
               PIGraphObject f2 = (PIGraphObject)o2;
               if (f1.getTotalCost() > f2.getTotalCost())
                  icompare = -1;
               else if (f1.getTotalCost() == f2.getTotalCost())
                  icompare = 0;
               else
                  icompare = 1;
               return(icompare);
            }
         }
      );

      // Repopulate sorted vector back to return vector
      Vector candVector = new Vector();
      int totalist = alist.size();
      if (Sort.compareTo("top10")==0 && alist.size() > 10)
          totalist = 10;
      for (int i = 0; i < totalist; i++)
         candVector.addElement((PIGraphObject)alist.get(i));
      return candVector;
   }

   protected Vector sortIncomingVectorByPI(Vector avector)
   {
      ArrayList alist = new ArrayList(avector);

      // Sort vector by PERSON_NAME.
      Collections.sort(alist,
         new Comparator()
         {
            public int compare(Object o1, Object o2)
            {
               int icompare = 0;
               GrantsObject f1 = (GrantsObject)o1;
               GrantsObject f2 = (GrantsObject)o2;
               icompare = f1.getInvestigatorName().compareTo(f2.getInvestigatorName());
               return(icompare);
            }
         }
      );

      // Repopulate sorted vector back to return vector
      Vector candVector = new Vector();
      int totalist = alist.size();
      for (int i = 0; i < totalist; i++)
         candVector.addElement((GrantsObject)alist.get(i));
      return candVector;
   }

   protected Vector sortIncomingVectorByDepartment(Vector avector)
   {
      ArrayList alist = new ArrayList(avector);

      // Sort vector by UNIT_NAME.
      Collections.sort(alist,
         new Comparator()
         {
            public int compare(Object o1, Object o2)
            {
               int icompare = 0;
               GrantsObject f1 = (GrantsObject)o1;
               GrantsObject f2 = (GrantsObject)o2;
               icompare = f1.getUnitName().compareTo(f2.getUnitName());
               return(icompare);
            }
         }
      );

      // Repopulate sorted vector back to return vector
      Vector candVector = new Vector();
      int totalist = alist.size();
      for (int i = 0; i < totalist; i++)
         candVector.addElement((GrantsObject)alist.get(i));
      return candVector;
   }

    public void setSort(String val)
    {
        Sort = val;
    }
    
}
