/*
 * RTFPrintPreview.java
 *
 * Created on December 23, 2004, 6:32 PM
 */

package edu.mit.coeus.utils.rtf;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
/**
 *
 * @author  bijosht
 */

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.awt.print.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class CoeusPrintPreview extends JComponent {
    protected int m_wPage;
    protected int m_hPage;
    protected int m_orientation;
    protected Printable m_target;
    protected JComboBox m_cbScale;
    protected PreviewContainer m_preview;
    private CoeusDlgWindow dlgPrintPreview;
    private static final int WIDTH=600;
    private static final int HEIGHT = 500;
    
    public javax.swing.ButtonGroup btnGrpOrientation;
    public javax.swing.JRadioButton rdBtnLandscape;
    public javax.swing.JRadioButton rdBtnPortrairt;
    private CoeusAppletMDIForm mdiForm;
    
    //    public CoeusPrintPreview(Printable target) {
    //        this(target, "Print Preview", PageFormat.PORTRAIT);
    //    }
    
    public CoeusPrintPreview(CoeusAppletMDIForm mdiForm,Printable target,
    String title, int orientation){
        //super(title);
        this.mdiForm = mdiForm;
        setSize(600, 400);
        setLayout(new BorderLayout());
        //getContentPane().setLayout(new BorderLayout());
        m_target = target;
        m_orientation = orientation;
        JToolBar tb = new JToolBar();
        String[] scales = { "10 %", "25 %", "50 %", "100 %" };
        m_cbScale = new JComboBox(scales);
        ActionListener lst = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Thread runner = new Thread() {
                    public void run() {
                        String str = m_cbScale.getSelectedItem().
                        toString();
                        if (str.endsWith("%"))
                            str = str.substring(0, str.length()-1);
                        str = str.trim();
                        int scale = 0;
                        try {
                            scale = Integer.parseInt(str);
                        }catch (NumberFormatException ex){
                            return;
                        }
                        int w = (int)(m_wPage*scale/100);
                        int h = (int)(m_hPage*scale/100);
                        
                        Component[] comps = m_preview.getComponents();
                        for (int k=0; k<comps.length; k++){
                            if (!(comps[k] instanceof PagePreview))
                                continue;
                            PagePreview pp = (PagePreview)comps[k];
                            pp.setScaledSize(w, h);
                        }
                        m_preview.doLayout();
                        m_preview.getParent().getParent().validate();
                    }
                };
                runner.start();
            }
        };
        m_cbScale.addActionListener(lst);
        m_cbScale.setMaximumSize(m_cbScale.getPreferredSize());
        m_cbScale.setEditable(true);
        tb.add(m_cbScale);
        tb.addSeparator();
        add(tb, BorderLayout.NORTH);
       // getContentPane().add(tb, BorderLayout.NORTH);
        m_preview = new PreviewContainer();
        PrinterJob prnJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = prnJob.defaultPage();
        pageFormat.setOrientation(m_orientation);
        if (pageFormat.getHeight()==0 || pageFormat.getWidth()==0){
            System.err.println("Unable to determine default page size");
            return;
        }
        m_wPage = (int)(pageFormat.getWidth());
        m_hPage = (int)(pageFormat.getHeight());
        int scale = 10;
        int w = (int)(m_wPage*scale/100);
        int h = (int)(m_hPage*scale/100);
        
        int pageIndex = 0;
        try{
            while (true){
                BufferedImage img = new BufferedImage(m_wPage, m_hPage, BufferedImage.TYPE_INT_RGB);
                Graphics g = img.getGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, m_wPage, m_hPage);
                if (target.print(g, pageFormat, pageIndex) != Printable.PAGE_EXISTS)
                    break;
                PagePreview pp = new PagePreview(w, h, img);
                m_preview.add(pp);
                pageIndex++;
            }
        }catch (PrinterException e){
            e.printStackTrace();
            System.err.println("Printing error: "+e.toString());
        }
        JScrollPane ps = new JScrollPane(m_preview);
        add(ps, BorderLayout.CENTER);
        setVisible(true);
    }
    
    class PreviewContainer extends JPanel {
        protected int H_GAP = 16;
        protected int V_GAP = 10;
        public Dimension getPreferredSize(){
            int n = getComponentCount();
            if (n == 0)
                return new Dimension(H_GAP, V_GAP);
            Component comp = getComponent(0);
            Dimension dc = comp.getPreferredSize();
            int w = dc.width;
            int h = dc.height;
            
            Dimension dp = getParent().getSize();
            int nCol = Math.max((dp.width-H_GAP)/(w+H_GAP), 1);
            int nRow = n/nCol;
            if (nRow*nCol < n)
                nRow++;
            
            int ww = nCol*(w+H_GAP) + H_GAP;
            int hh = nRow*(h+V_GAP) + V_GAP;
            Insets ins = getInsets();
            return new Dimension(ww+ins.left+ins.right,
            hh+ins.top+ins.bottom);
        }
        
        public Dimension getMaximumSize(){
            return getPreferredSize();
        }
        
        public Dimension getMinimumSize(){
            return getPreferredSize();
        }
        
        public void doLayout() {
            Insets ins = getInsets();
            int x = ins.left + H_GAP;
            int y = ins.top + V_GAP;
            
            int n = getComponentCount();
            if (n == 0)
                return;
            Component comp = getComponent(0);
            Dimension dc = comp.getPreferredSize();
            int w = dc.width;
            int h = dc.height;
            
            Dimension dp = getParent().getSize();
            int nCol = Math.max((dp.width-H_GAP)/(w+H_GAP), 1);
            int nRow = n/nCol;
            if (nRow*nCol < n)
                nRow++;
            
            int index = 0;
            for (int k = 0; k<nRow; k++){
                for (int m = 0; m<nCol; m++){
                    if (index >= n)
                        return;
                    comp = getComponent(index++);
                    comp.setBounds(x, y, w, h);
                    x += w+H_GAP;
                }
                y += h+V_GAP;
                x = ins.left + H_GAP;
            }
        }
    }
    
    class PagePreview extends JPanel {
        protected int m_w;
        protected int m_h;
        protected Image m_source;
        protected Image m_img;
        
        public PagePreview(int w, int h, Image source) {
            m_w = w;
            m_h = h;
            m_source= source;
            m_img = m_source.getScaledInstance(m_w, m_h, Image.SCALE_SMOOTH);
            m_img.flush();
            setBackground(Color.white);
            setBorder(new MatteBorder(1, 1, 2, 2, Color.black));
        }
        
        public void setScaledSize(int w, int h){
            m_w = w;
            m_h = h;
            m_img = m_source.getScaledInstance(m_w, m_h,
            Image.SCALE_SMOOTH);
            repaint();
        }
        
        public Dimension getPreferredSize(){
            Insets ins = getInsets();
            return new Dimension(m_w+ins.left+ins.right, m_h+ins.top+ins.bottom);
        }
        
        public Dimension getMaximumSize(){
            return getPreferredSize();
        }
        
        public Dimension getMinimumSize(){
            return getPreferredSize();
        }
        
        public void paint(Graphics g){
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(m_img, 0, 0, this);
            paintBorder(g);
        }
    }
    
    /** Specifies the Modal window */
    public  void postInitComponents() {
        dlgPrintPreview = new CoeusDlgWindow(mdiForm);
        dlgPrintPreview.getContentPane().add(this);
        //dlgPrintPreview.add(this);
        dlgPrintPreview.setTitle("Print Preview");
        dlgPrintPreview.setFont(CoeusFontFactory.getLabelFont());
        dlgPrintPreview.setModal(true);
        dlgPrintPreview.setResizable(false);
        dlgPrintPreview.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgPrintPreview.getSize();
        dlgPrintPreview.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgPrintPreview.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgPrintPreview.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgPrintPreview.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        
        dlgPrintPreview.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    
    private void setWindowFocus(){
    }
    
    private void performCancelAction(){
        dlgPrintPreview.setVisible(false);
    }
    
    public void display(){
        dlgPrintPreview.setVisible(true);
    }
}