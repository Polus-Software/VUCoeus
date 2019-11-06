/*
** $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/GraphObj.java,v 1.1.2.15 2007/07/31 13:36:19 cvs Exp $
** $Log: GraphObj.java,v $
** Revision 1.1.2.15  2007/07/31 13:36:19  cvs
** Add Annual Reports server-side code
** Move many methods to base class Reporting Base Bean
**
** Revision 1.1.2.14  2007/03/27 17:41:12  cvs
** Graph: Address issues with Award Type and Activity Type display
**
** Revision 1.1.2.13  2007/02/28 19:29:17  cvs
** Modify graphs to accept current values of APP_HOME_URL and COEUS_HOME directories
**
** Revision 1.1.2.12  2007/02/16 16:04:07  cvs
** Add randomized string to insure unique image creation
**
** Revision 1.1.2.11  2007/02/13 19:45:43  cvs
** Support headless in graphs
**
** Revision 1.1.2.10  2007/01/24 16:30:56  cvs
** Add support for Graph Award Types
**
** Revision 1.1.2.9  2007/01/24 14:56:18  cvs
** Address some bugs related with drilldown report display
**
** Revision 1.1.2.8  2007/01/23 20:45:36  cvs
** Add support for Graph Activity Types
**
** Revision 1.1.2.7  2007/01/23 18:13:12  cvs
** Add GUI support for Graph Sponsor Types
**
** Revision 1.1.2.6  2007/01/04 19:09:34  cvs
** Add support for Sponsor Type Graph by School by Sponsor Type
**
** Revision 1.1.2.5  2007/01/03 20:55:20  cvs
** Work on Activity Type report 2
**
** Revision 1.1.2.4  2006/12/28 20:37:20  cvs
**  Add support for grants by sponsor type graph
**
** Revision 1.1.2.3  2006/12/14 18:17:48  cvs
** Add sample graph for Number of Proposals by Department
**
** Revision 1.1.2.2  2006/12/08 19:46:45  cvs
** Add support for JFreeChart charting engine
**
** Revision 1.1.2.1  2006/11/20 20:50:24  cvs
** Add TDG engine support for demo purposes
**
*/
package edu.umdnj.coeus.reporting.bean;

// Java imports
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.image.BufferedImage;

// Coeus MIT imports
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import org.apache.axis.wsdl.symbolTable.Type;

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

public class GraphObj
{
    public GraphObj()
    {
        super();
    }
    
    protected String charttitle = "";
    
    public void setTitle(String title)
    {
        charttitle = title;
    }
    
    public void generateRawImage(Vector result, Vector vecFields, String strFormat, String strType, HttpServletResponse res)
        throws ServletException, IOException
    {
        // If Pie Chart with one series, go through this condition
        JFreeChart chart = null;
        if (strType.compareTo("PIE")==0 && vecFields.size()==2)
        {
            PieDataset dataset = createPieDataset(result,vecFields);
            chart = createSinglePieChart(dataset);
        }
        else
        {
            CategoryDataset dataset = createDataSet(result,vecFields);
            chart = createChart(strType,vecFields,dataset);
        }

	try {
                ServletOutputStream out = res.getOutputStream();
		BufferedImage image = chart.createBufferedImage(700,500);
                if (strFormat.compareTo("PNG")==0)
                    ChartUtilities.writeBufferedImageAsPNG(out,image);
                else
                if (strFormat.compareTo("JPEG")==0)
                    ChartUtilities.writeBufferedImageAsJPEG(out,image);
	}
        catch(IOException ex) { 
		ex.printStackTrace(System.out);
	}
    }

    public void saveRawImage(
           Vector result, 
           Vector vecFields, 
           String strType)
        throws IOException
    {
        // If Pie Chart with one series, go through this condition
        JFreeChart chart = null;
        if (strType.compareTo("PIE")==0 && vecFields.size()==2)
        {
            PieDataset dataset = createPieDataset(result,vecFields);
            chart = createSinglePieChart(dataset);
        }
        else
        {
            CategoryDataset dataset = createDataSet(result,vecFields);
            chart = createChart(strType,vecFields,dataset);
        }

	try {
              String directory = CoeusProperties.getProperty(CoeusPropertyKeys.COEUS_HOME);
              if (directory == null)
                 directory = "c:/dev/tools/Tomcat 5.0.28/webapps/coeus";
              directory += "/temp";
              ChartRenderingInfo info = new ChartRenderingInfo(
                      new StandardEntityCollection());
              String imgmap = "img00xGy";
              double srand = Math.random() * 1000;
              String tempFile = directory + "/" + imgmap + srand +".png";
              File file1 = new File(tempFile);
              ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);
	}
        catch(IOException ex) { 
		ex.printStackTrace(System.out);
	}
    }
    
    
    protected CategoryDataset createDataSet(Vector result, Vector vecFields)
    {
       int size = vecFields.size();
       int listSize = result.size();
       HashMap row = null;
       String[] categoryaxis = new String[listSize];
       String[] seriesaxis = new String[size-1];
       ReportingBaseBean rbbobj = new ReportingBaseBean();
       DefaultCategoryDataset dataset = new DefaultCategoryDataset();
       
       // Get all category axis titles;
       for(int rowNum = 0; rowNum < listSize; rowNum++)
       {
           row=(HashMap)result.elementAt(rowNum); 
           String fieldDescriptor = (String)vecFields.elementAt(0);
           Object obj = row.get(fieldDescriptor);
           categoryaxis[rowNum] = rbbobj.getCellValue(obj);
	}

       // Get all series label titles;
       for (int ol = 1; ol < size; ol++)
       {
           seriesaxis[ol-1] = (String)vecFields.elementAt(ol);
       }
       
       for (int rowNum = 0; rowNum < listSize; rowNum++)
       {
           row=(HashMap)result.elementAt(rowNum);
           for (int columnNum = 1; columnNum < size; columnNum++)
           {
              String fieldDescriptor = (String)vecFields.elementAt(columnNum);
              Object obj = row.get(fieldDescriptor);
              double dval = rbbobj.getNumericCellValue(obj);
              dataset.addValue(dval,seriesaxis[columnNum-1],categoryaxis[rowNum]);
           }
       }
       return dataset;
    
    }

     protected JFreeChart createChart(String strType, Vector vecFields, CategoryDataset dataset) 
     {
        String categoryTitle = (String)vecFields.elementAt(0);
        String seriestitle = "";
        if (vecFields.size()==2)
           seriestitle = (String)vecFields.elementAt(1);
        // create the bar chart
        JFreeChart chart = null;
        if (strType.compareTo("LINE")==0) {
            chart = ChartFactory.createLineChart(
                charttitle,         // chart title
                categoryTitle,               // domain axis label
                seriestitle,                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
            );
            
        }
        else
        if (strType.compareTo("AREA")==0) {
            chart = ChartFactory.createAreaChart(
                charttitle,         // chart title
                categoryTitle,               // domain axis label
                seriestitle,                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
            );
            
        }
        else
        if (strType.compareTo("PIE")==0) {
            chart = ChartFactory.createMultiplePieChart(
                charttitle,         // chart title
                dataset,                  // data
                TableOrder.BY_ROW,
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
            );
            
        }
        else {
            chart = ChartFactory.createBarChart(
                charttitle,         // chart title
                categoryTitle,               // domain axis label
                seriestitle,                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
            );
        }

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        if (strType.compareTo("PIE")==0)
        {
            MultiplePiePlot plot = (MultiplePiePlot) chart.getPlot();
            plot.setNoDataMessage("No data available");
            return chart;
        }
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        if (strType.compareTo("BAR")==0)
        {
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setDrawBarOutline(false);
        
        // set up gradient paints for series...
            GradientPaint gp0 = new GradientPaint(
                0.0f, 0.0f, Color.blue, 
                0.0f, 0.0f, new Color(0, 0, 64)
            );
            GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, new Color(0, 64, 0)
            );
            GradientPaint gp2 = new GradientPaint(
                0.0f, 0.0f, Color.red, 
                0.0f, 0.0f, new Color(64, 0, 0)
            );
            renderer.setSeriesPaint(0, gp0);
            renderer.setSeriesPaint(1, gp1);
            renderer.setSeriesPaint(2, gp2);
            
        }

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }
    protected PieDataset createPieDataset(Vector result, Vector vecFields) {
       DefaultPieDataset dataset = new DefaultPieDataset(); 
       int size = vecFields.size();
       int listSize = result.size();
       HashMap row = null;
       String[] categoryaxis = new String[listSize];
       ReportingBaseBean rbbobj = new ReportingBaseBean();
       
       // Get all category axis titles;
       for(int rowNum = 0; rowNum < listSize; rowNum++)
       {
           row=(HashMap)result.elementAt(rowNum); 
           String fieldDescriptor = (String)vecFields.elementAt(0);
           Object obj = row.get(fieldDescriptor);
           categoryaxis[rowNum] = rbbobj.getCellValue(obj);
       }

       for (int rowNum = 0; rowNum < listSize; rowNum++)
       {
           row=(HashMap)result.elementAt(rowNum);
           String fieldDescriptor = (String)vecFields.elementAt(1);
           Object obj = row.get(fieldDescriptor);
           double dval = rbbobj.getNumericCellValue(obj);
           dataset.setValue(categoryaxis[rowNum], new Double(dval));
       }
       return dataset;

    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    protected JFreeChart createSinglePieChart(PieDataset dataset) 
    {
        
        JFreeChart chart = ChartFactory.createPieChart(
            charttitle,  // chart title
            dataset,             // data
            false,               // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionOutlinesVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
        
    }

    protected String[] seriestitles = 
    {
        "New Jersey Dental School",
        "New Jersey Medical School",
        "Robert Wood Johnson Medical School",
        "School of Osteopathic Medicine",
        "School of Health-Related Professions",
        "School of Public Health",
        "School of Nursing"
    };

    protected String[] seriestypes = 
    {
       "DS",
       "NJMS",
       "RWJ",
       "SOM",
       "SHRP",
       "SPH",
       "SN"
    };

    protected static final int DS    = 0;
    protected static final int NJMS  = 1;
    protected static final int RWJ   = 2;
    protected static final int SOM   = 3;
    protected static final int SHRP  = 4;
    protected static final int SPH   = 5;
    protected static final int SN    = 6;

    protected static final int SPONSORLEN = 7;
    protected static final int ACTIVITYLEN = 9;
    protected static final int AWARDLEN = 6;
    
    protected String [] categorytitles =
    {
        "Federal",
        "Foundation",
        "Institution of Higher Education",
        "Local Government",
        "Other",
        "Private Non-Profit",
        "Private Profit",
    };
    protected static int FED = 0;
    protected static int FON = 1;
    protected static int HIG = 2;
    protected static int LOC = 3;
    protected static int OTH = 4;
    protected static int PNP = 5;
    protected static int PPR = 6;
    protected static int CON = 7;
    protected static int COO = 8;
    
    protected String[] activitytitles =
    {
      "Other",
      "Clinical Trial",
      "Public Service",
      "Organized Research",
      "TBD - Import Data Not Mapped",
      "Instruction and Departmental Research",
      "Construction",
      "Conference/Lectureship",
      "Fellowship"
    };
    
    protected String[] awardtitles =
    {
      "Contract",
      "Consortium",
      "Cooperative Agreement",
      "Grant",
      "Grant Agreement",
      "Other Transaction Agreement"
    };
    

    protected double[][] graph3array = new double[7][7];
    protected double[][] graph5array = new double[7][9];
    protected double[][] graph6array = new double[7][6];
        
    
    public void specializedGraphThree(Vector result,String strFormat,String strType,HttpServletResponse res)
        throws ServletException, IOException
    {
       HashMap row = null;
       int listSize = result.size();
       ReportingBaseBean rbbobj = new ReportingBaseBean();
       for (int rowNum = 0; rowNum < listSize; rowNum++)
       {
           row=(HashMap)result.elementAt(rowNum);
           Object obj1 = row.get("PARENT_UNIT_NUMBER");
           Object obj2 = row.get("DESCRIPTION");
           Object obj3 = row.get("NUM_AWARDS");
           String strval1 = rbbobj.getCellValue(obj1);
           String strval2 = rbbobj.getCellValue(obj2);
           double dval = rbbobj.getNumericCellValue(obj3);
           assignGraph3Array(strval1,strval2,dval);
       }
       DefaultCategoryDataset dataset = new DefaultCategoryDataset();

       for (int xnum = 0; xnum < seriestitles.length; xnum++)
       {
           for(int ynum = 0; ynum < categorytitles.length; ynum++)
              dataset.addValue(graph3array[xnum][ynum],seriestitles[xnum],categorytitles[ynum]);
       }
       Vector vecFields = new Vector();
       vecFields.addElement((String)"Sponsor Types");
       vecFields.addElement((String)"Number of Awards");
       charttitle = "Awards by Sponsor Type by School";
       JFreeChart chart = createChart(strType,vecFields,dataset);;

       try {
               ServletOutputStream out = res.getOutputStream();
       	BufferedImage image = chart.createBufferedImage(800,600);
               if (strFormat.compareTo("PNG")==0)
                   ChartUtilities.writeBufferedImageAsPNG(out,image);
               else
               if (strFormat.compareTo("JPEG")==0)
                   ChartUtilities.writeBufferedImageAsJPEG(out,image);
       }
       catch(IOException ex) { 
       	ex.printStackTrace(System.out);
       }
               
    }

    protected int currentseries = 0;
    public void specializedGraphFour(
                    Vector result,
                    String strFormat,
                    String strType,
                    String strSchool, 
                    HttpServletResponse res)
        throws ServletException, IOException
    {
       resetGraphArrays();
       String directory = CoeusProperties.getProperty(CoeusPropertyKeys.COEUS_HOME);
       if (directory == null)
           directory = "c:/dev/tools/Tomcat 5.0.28/webapps/coeus";
       directory += "/temp";
   
       HashMap row = null;
       int listSize = result.size();
       ReportingBaseBean rbbobj = new ReportingBaseBean();
       for (int rowNum = 0; rowNum < listSize; rowNum++)
       {
           row=(HashMap)result.elementAt(rowNum);
           Object obj1 = row.get("PARENT_UNIT_NUMBER");
           Object obj2 = row.get("DESCRIPTION");
           Object obj3 = row.get("NUM_AWARDS");
           String strval1 = rbbobj.getCellValue(obj1);
           String strval2 = rbbobj.getCellValue(obj2);
           double dval = rbbobj.getNumericCellValue(obj3);
           assignGraph3Array(strval1,strval2,dval);
       }

       res.setContentType("text/html");
       PrintWriter writer = res.getWriter();
       writer.println("<html><body>");

       if (strSchool != null && strSchool.length() > 0)
       {
           int xnum = 0;
           for (int num = 0; num < seriestypes.length; num++)
           {
               if (strSchool.compareTo(seriestypes[num])==0)
               {
                   xnum = num;
                   break;
               }
               
           }           
           HandleIndividualGraph(xnum,"Sponsor",writer,directory);
       }
       else
       for (int xnum = 0; xnum < seriestitles.length; xnum++)
       {
           HandleIndividualGraph(xnum,"Sponsor",writer,directory);
       } // end of for loop
       writer.println("</body></html>");
       writer.close();
    }

    public void specializedGraphFive(Vector result,String strFormat,String strType,String strSchool, HttpServletResponse res)
        throws ServletException, IOException
    {
       resetGraphArrays();
       String directory = CoeusProperties.getProperty(CoeusPropertyKeys.COEUS_HOME);
       if (directory == null)
           directory = "c:/dev/tools/Tomcat 5.0.28/webapps/coeus";
       directory += "/temp";
   
       HashMap row = null;
       int listSize = result.size();
       ReportingBaseBean rbbobj = new ReportingBaseBean();
       for (int rowNum = 0; rowNum < listSize; rowNum++)
       {
           row=(HashMap)result.elementAt(rowNum);
           Object obj1 = row.get("PARENT_UNIT_NUMBER");
           Object obj2 = row.get("DESCRIPTION");
           Object obj3 = row.get("NUM_AWARDS");
           String strval1 = rbbobj.getCellValue(obj1);
           String strval2 = rbbobj.getCellValue(obj2);
           double dval = rbbobj.getNumericCellValue(obj3);
           assignGraph5Array(strval1,strval2,dval);
       }

       res.setContentType("text/html");
       PrintWriter writer = res.getWriter();
       writer.println("<html><body>");

       if (strSchool != null && strSchool.length() > 0)
       {
           int xnum = 0;
           for (int num = 0; num < seriestypes.length; num++)
           {
               if (strSchool.compareTo(seriestypes[num])==0)
               {
                   xnum = num;
                   break;
               }
               
           }           
           HandleIndividualGraph(xnum,"Activity",writer,directory);
       }
       else
       for (int xnum = 0; xnum < seriestitles.length; xnum++)
       {
           HandleIndividualGraph(xnum,"Activity",writer,directory);
       } // end of for loop
       writer.println("</body></html>");
       writer.close();
    }

    public void specializedGraphSix(Vector result,String strFormat,String strType,String strSchool, HttpServletResponse res)
        throws ServletException, IOException
    {
       resetGraphArrays();
       String directory = CoeusProperties.getProperty(CoeusPropertyKeys.COEUS_HOME);
       if (directory == null)
           directory = "c:/dev/tools/Tomcat 5.0.28/webapps/coeus";
       directory += "/temp";
   
       HashMap row = null;
       int listSize = result.size();
       ReportingBaseBean rbbobj = new ReportingBaseBean();
       for (int rowNum = 0; rowNum < listSize; rowNum++)
       {
           row=(HashMap)result.elementAt(rowNum);
           Object obj1 = row.get("PARENT_UNIT_NUMBER");
           Object obj2 = row.get("DESCRIPTION");
           Object obj3 = row.get("NUM_AWARDS");
           String strval1 = rbbobj.getCellValue(obj1);
           String strval2 = rbbobj.getCellValue(obj2);
           double dval = rbbobj.getNumericCellValue(obj3);
           assignGraph6Array(strval1,strval2,dval);
       }

       res.setContentType("text/html");
       PrintWriter writer = res.getWriter();
       writer.println("<html><body>");

       if (strSchool != null && strSchool.length() > 0)
       {
           int xnum = 0;
           for (int num = 0; num < seriestypes.length; num++)
           {
               if (strSchool.compareTo(seriestypes[num])==0)
               {
                   xnum = num;
                   break;
               }
               
           }           
           HandleIndividualGraph(xnum,"Award",writer,directory);
       }
       else
       for (int xnum = 0; xnum < seriestitles.length; xnum++)
       {
           HandleIndividualGraph(xnum,"Award",writer,directory);
       } // end of for loop
       writer.println("</body></html>");
       writer.close();
    }

    protected void assignGraph3Array(String strval1,String strval2, double dval)
    {
       int xindex = -1;
       int yindex = -1;

       if (strval1.compareTo("DS")==0)	 
          xindex = DS;
       else if (strval1.compareTo("NJMS")==0)	 
          xindex = NJMS;
       else if (strval1.compareTo("RWJ")==0)	 
          xindex = RWJ;
       else if (strval1.compareTo("SOM")==0)	 
          xindex = SOM; 
       else if (strval1.compareTo("SHRP")==0)	 
          xindex = SHRP; 
       else if (strval1.compareTo("SPH")==0)	 
          xindex = SPH;
       else if (strval1.compareTo("SN" )==0)	 
          xindex = SN; 

       if (strval2.compareTo("Federal")==0)			       
          yindex = FED;
       else if (strval2.compareTo("Foundation")==0)			       
          yindex = FON;
       else if (strval2.compareTo("Institution of Higher Education")==0)    
          yindex = HIG;
       else if (strval2.compareTo("Local Government")==0)		       
          yindex = LOC;
       else if (strval2.compareTo("Other")==0)			       
          yindex = OTH;
       else if (strval2.compareTo("Private Non-Profit")==0)		       
          yindex = PNP;
       else if (strval2.compareTo("Private Profit")==0)		       
          yindex = PPR;

       if (xindex != -1 && yindex != -1)
          graph3array[xindex][yindex] = dval;
    }

    protected void assignGraph5Array(String strval1,String strval2, double dval)
    {
       int xindex = -1;
       int yindex = -1;

       if (strval1.compareTo("DS")==0)	 
          xindex = DS;
       else if (strval1.compareTo("NJMS")==0)	 
          xindex = NJMS;
       else if (strval1.compareTo("RWJ")==0)	 
          xindex = RWJ;
       else if (strval1.compareTo("SOM")==0)	 
          xindex = SOM; 
       else if (strval1.compareTo("SHRP")==0)	 
          xindex = SHRP; 
       else if (strval1.compareTo("SPH")==0)	 
          xindex = SPH;
       else if (strval1.compareTo("SN" )==0)	 
          xindex = SN; 

       if (strval2.compareTo("Other")==0)			       
          yindex = FED;
       else if (strval2.compareTo("Clinical Trial")==0)			       
          yindex = FON;
       else if (strval2.compareTo("Public Service")==0)    
          yindex = HIG;
       else if (strval2.compareTo("Organized Research")==0)		       
          yindex = LOC;
       else if (strval2.compareTo("TBD - Import Data Not Mapped")==0)			       
          yindex = OTH;
       else if (strval2.compareTo("Instruction and Departmental Research")==0)		       
          yindex = PNP;
       else if (strval2.compareTo("Construction")==0)		       
          yindex = PPR;
       else if (strval2.compareTo("Conference/Lectureship")==0)
          yindex = CON;
       else if (strval2.compareTo("Fellowship")==0)
          yindex = COO;

       if (xindex != -1 && yindex != -1)
          graph5array[xindex][yindex] = dval;
    }

    protected void assignGraph6Array(String strval1,String strval2, double dval)
    {
       int xindex = -1;
       int yindex = -1;

       if (strval1.compareTo("DS")==0)	 
          xindex = DS;
       else if (strval1.compareTo("NJMS")==0)	 
          xindex = NJMS;
       else if (strval1.compareTo("RWJ")==0)	 
          xindex = RWJ;
       else if (strval1.compareTo("SOM")==0)	 
          xindex = SOM; 
       else if (strval1.compareTo("SHRP")==0)	 
          xindex = SHRP; 
       else if (strval1.compareTo("SPH")==0)	 
          xindex = SPH;
       else if (strval1.compareTo("SN" )==0)	 
          xindex = SN; 

       if (strval2.compareTo("Contract")==0)			       
          yindex = FED;
       else if (strval2.compareTo("Consortium")==0)			       
          yindex = FON;
       else if (strval2.compareTo("Cooperative Agreement")==0)    
          yindex = HIG;
       else if (strval2.compareTo("Grant")==0)		       
          yindex = LOC;
       else if (strval2.compareTo("Grant Agreement")==0)			       
          yindex = OTH;
       else if (strval2.compareTo("Other Transaction Agreement (Do Not Use)")==0)		       
          yindex = PNP;

       if (xindex != -1 && yindex != -1)
          graph6array[xindex][yindex] = dval;
    }

    public void HandleIndividualGraph(int xnum, String Type,PrintWriter writer,String directory)
    {
           DefaultCategoryDataset dataset = new DefaultCategoryDataset();
           String series1 = "Number of Awards";
           int ilen = SPONSORLEN;
           if (Type.compareTo("Activity")==0)
              ilen = ACTIVITYLEN;
           else
           if (Type.compareTo("Award")==0)
              ilen = AWARDLEN;

           for (int ynum = 0; ynum < ilen; ynum++)
           {
               if (Type.compareTo("Sponsor")==0)
                  dataset.addValue(graph3array[xnum][ynum],series1,categorytitles[ynum]);
               else
               if (Type.compareTo("Activity")==0)
                  dataset.addValue(graph5array[xnum][ynum],series1,activitytitles[ynum]);
               else
               if (Type.compareTo("Award")==0)
                  dataset.addValue(graph6array[xnum][ynum],series1,awardtitles[ynum]);
                   
           }
           JFreeChart chart = null;
           
           String chrttitle = seriestitles[xnum];
           CategoryAxis3D categoryAxis = new CategoryAxis3D(Type+" Types");
           ValueAxis valueAxis = new NumberAxis3D("Number of Awards");
           BarRenderer3D renderer = new BarRenderer3D();
           renderer.setToolTipGenerator(
                   new StandardCategoryToolTipGenerator());
           currentseries = xnum;
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
           chart = new JFreeChart(chrttitle, JFreeChart.DEFAULT_TITLE_FONT, 
                   plot, false);
           chart.setBackgroundPaint(Color.lightGray);

           CategoryAxis domainAxis = plot.getDomainAxis();
           domainAxis.setCategoryLabelPositions(
           CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
           );
           
           if (Type.compareTo("Activity")==0)
           {
            renderer.setDrawBarOutline(false);
        
        // set up gradient paints for series...
            GradientPaint gp0 = new GradientPaint(
                0.0f, 0.0f, Color.green, 
                0.0f, 0.0f, new Color(64, 0, 0)
            );
            GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, new Color(0, 0, 64)
            );
            GradientPaint gp2 = new GradientPaint(
                0.0f, 0.0f, Color.blue, 
                0.0f, 0.0f, new Color(0, 64, 0)
            );
            renderer.setSeriesPaint(0, gp0);
            renderer.setSeriesPaint(1, gp1);
            renderer.setSeriesPaint(2, gp2);
        }

        if (Type.compareTo("Award")==0)
        {
            renderer.setDrawBarOutline(false);
        
        // set up gradient paints for series...
            GradientPaint gp0 = new GradientPaint(
                0.0f, 0.0f, Color.blue, 
                0.0f, 0.0f, new Color(0, 0, 64)
            );
            GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, new Color(0, 64, 0)
            );
            GradientPaint gp2 = new GradientPaint(
                0.0f, 0.0f, Color.red, 
                0.0f, 0.0f, new Color(64, 0, 0)
            );
            renderer.setSeriesPaint(0, gp0);
            renderer.setSeriesPaint(1, gp1);
            renderer.setSeriesPaint(2, gp2);
        }

           try {
              ChartRenderingInfo info = new ChartRenderingInfo(
                      new StandardEntityCollection());
              String imgmap = "img00"+xnum;
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
            servletstr += seriestypes[currentseries];
            servletstr += "&SponsorType=";
            servletstr += categorytitles[category];
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
            servletstr += seriestypes[currentseries];
            servletstr += "&ActivityType=";
            servletstr += activitytitles[category];
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
            servletstr += seriestypes[currentseries];
            servletstr += "&AwardType=";
            
            String awardtitle = awardtitles[category];
	    if (category == PNP)
               awardtitle += " (Do Not Use)";
            
            servletstr += awardtitle;
            servletstr += "&Format=PDF";
            return servletstr;
        }
        
    }

    protected void resetGraphArrays()
    {
        for (int inum = 0; inum < 7; inum++)
            for (int jnum = 0; jnum < 7; jnum++)
                graph3array[inum][jnum] = 0;
        
        for (int inum = 0; inum < 7; inum++)
            for (int jnum = 0; jnum < 9; jnum++)
                graph5array[inum][jnum] = 0;
        
        for (int inum = 0; inum < 7; inum++)
            for (int jnum = 0; jnum < 6; jnum++)
                graph6array[inum][jnum] = 0;

    }
}

