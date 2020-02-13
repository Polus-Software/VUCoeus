/*
** $Header: /home/cvs/cvsroot/coeus/Source/WEB-INF/classes/edu/umdnj/coeus/reporting/bean/Attic/AnnualReportsGraphObj.java,v 1.1.2.2 2007/08/01 18:14:25 cvs Exp $
** $Log: AnnualReportsGraphObj.java,v $
** Revision 1.1.2.2  2007/08/01 18:14:25  cvs
** Add Annual Reports detail reports and PI reports
**
** Revision 1.1.2.1  2007/07/31 13:36:19  cvs
** Add Annual Reports server-side code
** Move many methods to base class Reporting Base Bean
**
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
import java.text.DecimalFormat;

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
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.TableOrder;

public class AnnualReportsGraphObj
                  extends GraphObj
{
    public AnnualReportsGraphObj()
    {
        super();
    }

    public AnnualReportsGraphObj(String unitname)
    {
        super();
        this.unitname = unitname;
    }

    private String seriestitle = "";
    private String categorytitle = "";
    private String currentFile = "";
    private String unitname = "";
    
    public void saveRawImage(
           String placeholder,
           Vector seriestitles,
           Vector categorytitles,
           double [][] dvalues, 
           String seriestitle,
           String categorytitle,
           boolean blegend,
           String strType)
        throws IOException
    {
        this.seriestitle = seriestitle;
        this.categorytitle = categorytitle;

        JFreeChart chart = null;
        CategoryDataset dataset = createDataSet(seriestitles,categorytitles,dvalues);
        chart = createChart(strType,dataset,blegend);
        chart.getTitle().setFont(new Font("Times New Roman",Font.BOLD,12));
        
        if (chart.getLegend() != null)
        {
            chart.getLegend().setPosition(RectangleEdge.RIGHT);
        }

	try {
              String directory = CoeusProperties.getProperty(CoeusPropertyKeys.COEUS_HOME);
              if (directory == null)
                 directory = "c:/dev/tools/Tomcat 5.0.28/webapps/coeus";
              directory += "/temp";
              ChartRenderingInfo info = new ChartRenderingInfo(
                      new StandardEntityCollection());
              String imgmap = placeholder + "annr00xyN";
              double srand = Math.random() * 1000;
              String tempFile = directory + "/" + imgmap + srand +".png";
              currentFile = tempFile;
              File file1 = new File(tempFile);
              ChartUtilities.saveChartAsPNG(file1, chart, 700, 550, info);
	}
        catch(IOException ex) { 
		ex.printStackTrace(System.out);
	}
    }
    
    
    protected CategoryDataset createDataSet(
               Vector seriestitles, 
               Vector categorytitles,
               double [][] dvalues)
    {
       DefaultCategoryDataset dataset = new DefaultCategoryDataset();
       
       for (int rowNum = 0; rowNum < seriestitles.size(); rowNum++)
       {
           for (int columnNum = 0; columnNum < categorytitles.size(); columnNum++)
           {
              double dval = dvalues[rowNum][columnNum];
              dataset.addValue(dval,(String)seriestitles.elementAt(rowNum),(String)categorytitles.elementAt(columnNum));
           }
       }
       return dataset;
    
    }

     protected JFreeChart createChart(String strType, CategoryDataset dataset, boolean blegend) 
     {

        // create the bar chart
        JFreeChart chart = null;
        if (strType.compareTo("LINE")==0) {
            chart = ChartFactory.createLineChart(
                charttitle,         // chart title
                categorytitle,               // domain axis label
                seriestitle,                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                blegend,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
            );
            
        }
        else
        if (strType.compareTo("AREA")==0) {
            chart = ChartFactory.createAreaChart(
                charttitle,         // chart title
                categorytitle,               // domain axis label
                seriestitle,                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                blegend,                     // include legend
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
                blegend,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
            );
            
        }
        else {
            chart = ChartFactory.createBarChart(
                charttitle,         // chart title
                categorytitle,               // domain axis label
                seriestitle,                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                blegend,                     // include legend
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

            renderer.setItemMargin(0.05);
	    if (unitname.length() > 0)
            {
               CategoryItemRenderer itemrenderer = plot.getRenderer();
               CategoryItemLabelGenerator generator = 
                    new StandardCategoryItemLabelGenerator( "${2}", new DecimalFormat());
               itemrenderer.setItemLabelGenerator(generator);
               itemrenderer.setItemLabelsVisible(true);
            }
        }

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }

    public String getCurrentFile()
    {
        return currentFile;
    }
}

