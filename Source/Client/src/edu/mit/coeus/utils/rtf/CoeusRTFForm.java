/*
 * CoeusRTFForm.java
 *
 * Created on December 22, 2004, 2:30 PM
 */

package edu.mit.coeus.utils.rtf;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import javax.swing.ListCellRenderer;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import javax.swing.*;
import java.util.Vector;
import javax.swing.border.LineBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import  javax.swing.AbstractAction;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.MouseInputListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleContext;
import javax.swing.text.View;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author  chandrashekara
 */
public class CoeusRTFForm extends javax.swing.JComponent implements ActionListener,ItemListener {
   // For Print Preview
    
    //private PrintView m_printView;
    
    private int selectedValue;
    public static final int OK_CLICKED = 0;
    public static final int CANCEL_CLICKED = 1;
    private static final String EMPTY_STRING="";
    //private String fontSize;
    private static final int MAX_FONT_SIZE=50;
    private Style style;
    private ColorBean colorBean = null;
    private String textData;
    private JDialog dlgAnnotation=null;
    private StyledDocument styledDocument;
    private static final int HEIGHT = 210;
    private static final int WIDTH = 375;
    private JComponent component;
    protected JTextPane m_monitor;
    protected DefaultStyledDocument defaultStyledDocument;
    protected RTFEditorKit editorKit;
    private StyleContext styleContext;
    protected int m_xStart = -1;
    protected int m_xFinish = -1;
    protected String fontName = "";
    protected int fontSize = 0;
    protected MutableAttributeSet m_attributes;
    private Color foreGround;
    private Color backGround;
    private String fontFamily;
    private boolean m_skipUpdate;
    private CoeusAppletMDIForm mdiForm;
    private String filePath;
    
    
   // private Docuemnt document = txtAnnotation.getDocument();
    
    
    private final UndoManager undo = new UndoManager();
    
    
    /** Creates new form CoeusRTFForm */
    public CoeusRTFForm(CoeusAppletMDIForm mdiForm, String filePath) {
        this.mdiForm = mdiForm;
        this.filePath = filePath;
        initComponents();
        postInitComponents();
        rtfData(filePath);
//        AttributeSet attr = txtAnnotation.getInputAttributes();
//        setDefaultAlignment(attr);
        registerComponents();
        focusListeners();
    }
    
    private void rtfData(String filePath){
        try{
            defaultStyledDocument = new DefaultStyledDocument(styleContext);
            FileInputStream inputStream = new FileInputStream(filePath);
            editorKit.read(inputStream, defaultStyledDocument, 0);
            txtAnnotation.setDocument(defaultStyledDocument);
            inputStream.close();
        }catch (FileNotFoundException fileNotFoundException){
            CoeusOptionPane.showErrorDialog(fileNotFoundException.getMessage());
            System.out.println("File Not Found");
        }catch (IOException iOException){
            CoeusOptionPane.showErrorDialog(iOException.getMessage());
            System.out.println("I/O Exception");
        }catch (BadLocationException badLocationException){
            CoeusOptionPane.showErrorDialog(badLocationException.getMessage());
            System.out.println("BadLocation");
        }
    }
    
    public void showPrintPreview(){
        
        CoeusPrintPreview coeusPrintPreview = 
            new CoeusPrintPreview(mdiForm,txtAnnotation,"Print Preview",
                PageFormat.PORTRAIT);
        coeusPrintPreview.postInitComponents();
        coeusPrintPreview.display();
    }
    
    /** 
     *This method will be called once Print button action is performed
     */
    public  void printData() throws Exception{
        PrinterJob prnJob = PrinterJob.getPrinterJob();
        PageFormat pf = prnJob.pageDialog(prnJob.defaultPage());
        if(pf.getOrientation()==PageFormat.PORTRAIT ) {
            Paper p=pf.getPaper();
            double xcoord=p.getImageableX();
            double ycoord=p.getImageableY();
            p.setImageableArea(xcoord,ycoord,p.getImageableWidth(),p.getImageableHeight());
            pf.setPaper(p);
        }
        if(pf.getOrientation()==PageFormat.LANDSCAPE ) {
            Paper p=pf.getPaper();
            double xcoord=p.getImageableX();
            double ycoord=p.getImageableY();
            p.setImageableArea(xcoord,ycoord,p.getImageableWidth(),p.getImageableHeight());
            pf.setPaper(p);
        }
        prnJob.setPrintable(txtAnnotation,pf);
        if (!prnJob.printDialog()){
            return;
        }
        prnJob.print();
    }
    
    
    
//    public void printData() throws Exception {
//        try {
//            PrinterJob prnJob = PrinterJob.getPrinterJob();
//            prnJob.setPrintable(txtAnnotation);
//            if (!prnJob.printDialog())
//                return;
//            setCursor( Cursor.getPredefinedCursor(
//            Cursor.WAIT_CURSOR));
//            prnJob.print();
//            setCursor( Cursor.getPredefinedCursor(
//            Cursor.DEFAULT_CURSOR));
//            JOptionPane.showMessageDialog(this,
//            "Printing completed successfully", "Info",
//            JOptionPane.INFORMATION_MESSAGE);
//        }
//        catch (PrinterException e) {
//            e.printStackTrace();
//            System.err.println("Printing error: "+e.toString());
//        }
//    }
//    public int print(Graphics pg, PageFormat pageFormat,
//    int pageIndex) throws PrinterException {
//        pg.translate((int)pageFormat.getImageableX(),
//        (int)pageFormat.getImageableY());
//        int wPage = (int)pageFormat.getImageableWidth();
//        int hPage = (int)pageFormat.getImageableHeight();
//        pg.setClip(0, 0, wPage, hPage);
//        // Only do this once per print
//        if (m_printView == null) {
//            BasicTextUI btui = (BasicTextUI)m_monitor.getUI();
//            View root = btui.getRootView(m_monitor);
//            m_printView = new PrintView(
//            defaultStyledDocument.getDefaultRootElement(),
//            root, wPage, hPage);
//        }
//        boolean bContinue = m_printView.paintPage(pg,
//        hPage, pageIndex);
//        System.gc();
//        if (bContinue)
//           // return PAGE_EXISTS;
//            return 1;
//        else {
//            m_printView = null;
//            //return NO_SUCH_PAGE;
//            return 0;
//        }
//    }
//    
//    public class PrintView extends BoxView {
//        protected int m_firstOnPage = 0;
//        protected int m_lastOnPage = 0;
//        protected int m_pageIndex = 0;
//        public PrintView(Element elem, View root, int w, int h) {
//            super(elem, Y_AXIS);
//            setParent(root);
//            setSize(w, h);
//            layout(w, h);
//        }
//        public boolean paintPage(Graphics g, int hPage,
//        int pageIndex) {
//            if (pageIndex > m_pageIndex) {
//                m_firstOnPage = m_lastOnPage + 1;
//                if (m_firstOnPage >= getViewCount())
//                    return false;
//                m_pageIndex = pageIndex;
//            }
//            int yMin = getOffset(Y_AXIS, m_firstOnPage);
//            int yMax = yMin + hPage;
//            Rectangle rc = new Rectangle();
//            for (int k = m_firstOnPage; k < getViewCount(); k++) {
//                rc.x = getOffset(X_AXIS, k);
//                rc.y = getOffset(Y_AXIS, k);
//                rc.width = getSpan(X_AXIS, k);
//                rc.height = getSpan(Y_AXIS, k);
//                if (rc.y+rc.height > yMax)
//                    break;
//                m_lastOnPage = k;
//                rc.y -= yMin;
//                paintChild(g, rc, k);
//            }
//            return true;
//        }
//    }
    
    
    
    
    public void display(){
        this.setVisible(true);
    }
    
    private void registerComponents(){
        // Add actionListeners for the button
        
        btnBold.addActionListener(this);
        btnItalic.addActionListener(this);
        btnStrike.addActionListener(this);
        btnUnderline.addActionListener(this);
        
        btnLeft.addActionListener(this);
        btnCentre.addActionListener(this);
        btnRight.addActionListener(this);
        //btnJustify.addActionListener(this);
        btnPrintPreview.addActionListener(this);
        // Add the itemListeners for the Combo
        cmbForeGround.addItemListener(this);
        cmbFontFamily.addItemListener(this);
        cmbFontSize.addItemListener(this);
    }
    private void focusListeners(){
        CaretListener lst = new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                AttributeSet attr = txtAnnotation.getInputAttributes();
                setDefaultAlignment(attr);
                showAttributes(e.getDot());
            }
        };
        txtAnnotation.addCaretListener(lst);
        FocusListener flst = new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (m_xStart>=0 && m_xFinish>=0)
                    if (txtAnnotation.getCaretPosition()==m_xStart) {
                        txtAnnotation.setCaretPosition(m_xFinish);
                        txtAnnotation.moveCaretPosition(m_xStart);
                    }
                    else{
                        txtAnnotation.select(m_xStart, m_xFinish);
                    }
            }
            public void focusLost(FocusEvent e) {
                m_xStart = txtAnnotation.getSelectionStart();
                m_xFinish = txtAnnotation.getSelectionEnd();
            }
        };
        txtAnnotation.addFocusListener(flst);
        WindowListener wndCloser = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        //addWindowListener(wndCloser);
        showAttributes(0);
        setVisible(true);
    }
    
    protected void showAttributes(int p) {
        m_skipUpdate = true;
        AttributeSet a = defaultStyledDocument.getCharacterElement(p-1).getAttributes();
        
        String name = StyleConstants.getFontFamily(a);
        fontFamily = cmbFontFamily.getSelectedItem().toString();
        if (!fontFamily.equals(name)) {
            fontFamily = name;
            cmbFontFamily.setSelectedItem(fontFamily);
        }
        int size = StyleConstants.getFontSize(a);
        if (fontSize != size) {
            fontSize = size;
            cmbFontSize.setSelectedItem(Integer.toString(fontSize));
        }
        
        Color foreGroundColor = StyleConstants.getForeground(a);
        if(foreGround!=foreGroundColor){
            foreGround = foreGroundColor;
            cmbForeGround.setSelectedItem(foreGround.toString());
        }else{
            cmbForeGround.setSelectedItem(foreGroundColor.toString());
        }
        
        boolean bold = StyleConstants.isBold(a);
        if (bold != btnBold.isSelected())
            btnBold.setSelected(bold);
        boolean italic = StyleConstants.isItalic(a);
        if (italic != btnItalic.isSelected())
            btnItalic.setSelected(italic);
        boolean underLine = StyleConstants.isUnderline(a);
        if (underLine != btnUnderline.isSelected())
            btnUnderline.setSelected(underLine);
        boolean strikeOut = StyleConstants.isStrikeThrough(a);
        if (strikeOut!= btnStrike.isSelected())
            btnStrike.setSelected(strikeOut);
        
        
        
        int alignment = StyleConstants.getAlignment(a);
        if(alignment==StyleConstants.ALIGN_CENTER){
            boolean centre = btnCentre.isSelected();
            if(centre){
                btnCentre.setSelected(true);
            }else{
                btnCentre.setSelected(false);
            }
        }else if(alignment==StyleConstants.ALIGN_LEFT){
            boolean left = btnLeft.isSelected();
            if(left){
                btnLeft.setSelected(true);
            }else{
                btnLeft.setSelected(false);
            }
        }else if(alignment==StyleConstants.ALIGN_RIGHT){
            boolean right = btnRight.isSelected();
            if(right){
                btnRight.setSelected(true);
            }else{
                btnRight.setSelected(false);
            }
        }
        m_skipUpdate = false;
    }
    
    public AttributeSet getAttributes() {
        if (m_attributes == null)
            return null;
        StyleConstants.setFontFamily(m_attributes, cmbFontFamily.getSelectedItem().toString());
        StyleConstants.setFontSize(m_attributes,  new Integer(cmbFontSize.getSelectedItem().toString()).intValue());
        StyleConstants.setBold(m_attributes, btnBold.isSelected());
        StyleConstants.setAlignment(m_attributes,StyleConstants.ALIGN_CENTER);
        StyleConstants.setItalic(m_attributes, btnItalic.isSelected());
        StyleConstants.setUnderline(m_attributes,btnUnderline.isSelected());
        StyleConstants.setStrikeThrough(m_attributes,btnStrike.isSelected());
        StyleConstants.setForeground(m_attributes,(Color)  cmbForeGround.getSelectedItem());
        //        StyleConstants.setBackground(m_attributes,(Color)cmbForeGround.getSelectedItem());
        return m_attributes;
    }
    public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
        if(itemEvent.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if(source.equals(cmbFontSize)){
                String fontsSize = (String)cmbFontSize.getSelectedItem();
                int size = Integer.parseInt(fontsSize);
                fontSize = size;
                MutableAttributeSet attr = txtAnnotation.getInputAttributes();
                StyleConstants.setFontSize(attr, fontSize);
                setAttributeSet(attr,true);
                txtAnnotation.grabFocus();
            }else if(source.equals(cmbForeGround)){
                Object highLighter = cmbForeGround.getSelectedItem();
                ColorBean colorBean = (ColorBean)highLighter;
                backGround = colorBean.getColor();
                MutableAttributeSet attr = txtAnnotation.getInputAttributes();
                StyleConstants.setForeground(attr,backGround);
                setAttributeSet(attr,true);
                txtAnnotation.grabFocus();
            }else if(source.equals(cmbFontFamily)){
                Object font = cmbFontFamily.getSelectedItem();
                fontFamily = font.toString();
                MutableAttributeSet attr = txtAnnotation.getInputAttributes();
                StyleConstants.setFontFamily(attr,fontFamily);
                setAttributeSet(attr,true);
                txtAnnotation.grabFocus();
            }
        }
    }// End of itemstateChanged
    
    // To be performed when Cut action is performed
    public void cutAction(){
        txtAnnotation.cut();
    }
    // To be performed when Copy action is performed
    public void copyAction(){
        MutableAttributeSet attr = txtAnnotation.getInputAttributes();
        txtAnnotation.copy();
        setAttributeSet(attr,true);
    }
    // To be performed when Paste action is performed
    public void pastAction(){
        MutableAttributeSet attr = txtAnnotation.getInputAttributes();
        txtAnnotation.paste();
        setAttributeSet(attr,true);
    }
    // To be performed when Selectall action is performed
    public void selectAllAction(){
        MutableAttributeSet attr = txtAnnotation.getInputAttributes();
        txtAnnotation.selectAll();
        setAttributeSet(attr,true);
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent ) {
        Object source = actionEvent.getSource();
        if(source.equals(btnBold)){
            doBoldAction();
        }else if(source.equals(btnItalic)){
            doItalicAction();
        }else if(source.equals(btnStrike)){
            doStrikeAction();
        }else if(source.equals(btnUnderline)){
            doUnderLineAction();
        }else if(source.equals(btnCentre)){
            doCentreAction();
        }else if(source.equals(btnRight)){
            doRightAlignmentAction();
        }else if(source.equals(btnLeft)){
            doLeftAllignmentAction();
        }else if(source.equals(btnPrintPreview)){
            showPrintPreview();
        }
    }
    
    
    
    private void doLeftAllignmentAction(){
        MutableAttributeSet attr = txtAnnotation.getInputAttributes();
        boolean isSelected = btnLeft.isSelected();
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
        setAllignmentAttributeSets(attr,isSelected);
        txtAnnotation.grabFocus();
        txtAnnotation.repaint();
    }
    
    private void doRightAlignmentAction(){
        MutableAttributeSet attr = txtAnnotation.getInputAttributes();
        boolean isSelected = btnRight.isSelected();
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
        setAllignmentAttributeSets(attr,isSelected);
        txtAnnotation.grabFocus();
        txtAnnotation.repaint();
    }
    private void doCentreAction(){
        MutableAttributeSet attr = txtAnnotation.getInputAttributes();
        boolean isSelected = btnCentre.isSelected();
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
        setAllignmentAttributeSets(attr,isSelected);
        txtAnnotation.grabFocus();
        txtAnnotation.repaint();
    }
    
    private void doBoldAction( ){
        MutableAttributeSet attr = txtAnnotation.getInputAttributes();
        boolean isSelected = btnBold.isSelected();
        StyleConstants.setBold(attr, isSelected);
        setAttributeSet(attr,isSelected);
        txtAnnotation.grabFocus();
        txtAnnotation.repaint();
    }
    
    
    
    private void doItalicAction(){
        MutableAttributeSet attr = txtAnnotation.getInputAttributes();
        boolean isSelected = btnItalic.isSelected();
        StyleConstants.setItalic(attr, isSelected);
        setAttributeSet(attr,isSelected);
        txtAnnotation.grabFocus();
        txtAnnotation.repaint();
    }
    
    private void doStrikeAction(){
        MutableAttributeSet attr = txtAnnotation.getInputAttributes();
        boolean isSelected = btnStrike.isSelected();
        StyleConstants.setStrikeThrough(attr, isSelected);
        setAttributeSet(attr,isSelected);
        txtAnnotation.grabFocus();
        txtAnnotation.repaint();
    }
    
    private void doUnderLineAction(){
        MutableAttributeSet attr = txtAnnotation.getInputAttributes();
        boolean isSelected = btnUnderline.isSelected();
        StyleConstants.setUnderline(attr, isSelected);
        setAttributeSet(attr,isSelected);
        txtAnnotation.grabFocus();
        txtAnnotation.repaint();
    }
    
    protected void setAttributeSet(AttributeSet attr,boolean isSelected) {
        if (m_skipUpdate)return;
        m_attributes = new SimpleAttributeSet(attr);
        int xStart = txtAnnotation.getSelectionStart();
        int xFinish = txtAnnotation.getSelectionEnd();
        if (!txtAnnotation.hasFocus()) {
            xStart = m_xStart;
            xFinish = m_xFinish;
        }
        if (xStart != xFinish) {
            if(isSelected){
                defaultStyledDocument.setCharacterAttributes(xStart, xFinish - xStart,
                attr, true);
                txtAnnotation.select(xStart,xFinish);
            }else{
                defaultStyledDocument.setCharacterAttributes(xStart, xFinish - xStart,
                attr, false);
                txtAnnotation.select(xStart,xFinish);
            }
        }
        else {
            MutableAttributeSet inputAttributes = editorKit.getInputAttributes();
            inputAttributes.addAttributes(attr);
        }
    }
    
    protected void setDefaultAlignment(AttributeSet attr){
        int alignment = StyleConstants.getAlignment(attr);
        if (alignment == StyleConstants.ALIGN_LEFT){
            btnLeft.setSelected(true);
        }else {
            btnLeft.setSelected(false);
        }
        if (alignment == StyleConstants.ALIGN_CENTER){
            btnCentre.setSelected(true);
        }else{
            btnCentre.setSelected(false);
        }
        if (alignment == StyleConstants.ALIGN_RIGHT){
            btnRight.setSelected(true);
        }else{
            btnRight.setSelected(false);
        }
        updatePreview();
    }
    
    protected void updatePreview() {
        txtAnnotation.repaint();
    }
    
    protected void setAllignmentAttributeSets(AttributeSet attr,
    boolean setParagraphAttributes) {
        if (m_skipUpdate)
            return;
        int xStart = txtAnnotation.getSelectionStart();
        int xFinish = txtAnnotation.getSelectionEnd();
        if (!txtAnnotation.hasFocus()) {
            xStart = m_xStart;
            xFinish = m_xFinish;
        }
        if (setParagraphAttributes){
            defaultStyledDocument.setParagraphAttributes(xStart, xFinish - xStart, attr, false);
            txtAnnotation.select(xStart,xFinish);
        }else if (xStart != xFinish){
            defaultStyledDocument.setCharacterAttributes(xStart,xFinish - xStart, attr, false);
            txtAnnotation.select(xStart,xFinish);
        }
        else {
            MutableAttributeSet inputAttributes = editorKit.getInputAttributes();
            inputAttributes.addAttributes(attr);
        }
    }
    
    public  void postInitComponents() {
        //Set Color Model and Renderer for Color Combos
        ColorComboBoxModel fontModel =  new ColorComboBoxModel();
        ColorComboBoxModel highlightModel =  new ColorComboBoxModel();
        ColorRenderer colorRenderer = new ColorRenderer();
        
        cmbForeGround.setModel(highlightModel);
        cmbForeGround.setRenderer(colorRenderer);
        cmbForeGround.allowModify(true);
        
        //Set Font from System to Font Combo
        String fontFamily[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        cmbFontFamily.setModel(new DefaultComboBoxModel(fontFamily));
        // Set the font size values for the combo box
        for(int fontCount=1; fontCount<=50; fontCount++){
            Object size =  new Integer(fontCount);
            cmbFontSize.addItem(size.toString());
        }
        
        styledDocument = txtAnnotation.getStyledDocument();
        style = txtAnnotation.addStyle("Style", null);
        editorKit = new RTFEditorKit();
        txtAnnotation.setEditorKit(editorKit);
        styleContext = new StyleContext();
        defaultStyledDocument = new DefaultStyledDocument(styleContext);
        
        Ruler ruler = new Ruler(txtAnnotation);
        ruler.setAutoscrolls(true);
        
        MouseInputListener listner = ruler.createMouseInputListener();
        scrpntxtComponent.setColumnHeaderView(ruler);
        
    }
    
    
    //    // Listen for undo and redo events
    //    document.addUndoableEditListener(new UndoableEditListener() {
    //        public void undoableEditHappened(UndoableEditEvent evt) {
    //            undo.addEdit(evt.getEdit());
    //        }
    //    });
    //
    //    // Create an undo action and add it to the text component
    //    txtAnnotation.getActionMap().put("Undo",
    //        new AbstractAction("Undo") {
    //            public void actionPerformed(ActionEvent evt) {
    //                try {
    //                    if (undo.canUndo()) {
    //                        undo.undo();
    //                    }
    //                } catch (CannotUndoException e) {
    //                }
    //            }
    //       });
    //
    //    // Bind the undo action to ctl-Z
    //    txtAnnotation.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
    //
    //    // Create a redo action and add it to the text component
    //    txtAnnotation.getActionMap().put("Redo",
    //        new AbstractAction("Redo") {
    //            public void actionPerformed(ActionEvent evt) {
    //                try {
    //                    if (undo.canRedo()) {
    //                        undo.redo();
    //                    }
    //                } catch (CannotRedoException e) {
    //                }
    //            }
    //        });
    //
    //    // Bind the redo action to ctl-Y
    //    txtAnnotation.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
    
    
    
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlHeader = new javax.swing.JPanel();
        cmbFontFamily = new javax.swing.JComboBox();
        cmbFontSize = new javax.swing.JComboBox();
        btnItalic = new javax.swing.JToggleButton();
        btnUnderline = new javax.swing.JToggleButton();
        btnStrike = new javax.swing.JToggleButton();
        btnCentre = new javax.swing.JToggleButton();
        btnRight = new javax.swing.JToggleButton();
        btnLeft = new javax.swing.JToggleButton();
        btnPrintPreview = new javax.swing.JButton();
        btnBold = new javax.swing.JToggleButton();
        cmbForeGround = new ColorComboBox();
        scrpntxtComponent = new javax.swing.JScrollPane();
        txtAnnotation = new edu.mit.coeus.utils.CoeusTextPane();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(605, 569));
        setPreferredSize(new java.awt.Dimension(605, 569));
        pnlHeader.setLayout(new java.awt.GridBagLayout());

        pnlHeader.setMinimumSize(new java.awt.Dimension(980, 23));
        pnlHeader.setPreferredSize(new java.awt.Dimension(980, 23));
        cmbFontFamily.setMinimumSize(new java.awt.Dimension(124, 22));
        cmbFontFamily.setPreferredSize(new java.awt.Dimension(124, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlHeader.add(cmbFontFamily, gridBagConstraints);

        cmbFontSize.setMinimumSize(new java.awt.Dimension(60, 22));
        cmbFontSize.setPreferredSize(new java.awt.Dimension(60, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlHeader.add(cmbFontSize, gridBagConstraints);

        btnItalic.setFont(CoeusFontFactory.getLabelFont());
        btnItalic.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.ITALIC_ICON)));
        btnItalic.setMnemonic('I');
        btnItalic.setMinimumSize(new java.awt.Dimension(39, 23));
        btnItalic.setPreferredSize(new java.awt.Dimension(39, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlHeader.add(btnItalic, gridBagConstraints);

        btnUnderline.setFont(CoeusFontFactory.getLabelFont());
        btnUnderline.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.UNDERLINE_ICON)));
        btnUnderline.setMnemonic('U');
        btnUnderline.setMinimumSize(new java.awt.Dimension(39, 23));
        btnUnderline.setPreferredSize(new java.awt.Dimension(39, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlHeader.add(btnUnderline, gridBagConstraints);

        btnStrike.setFont(CoeusFontFactory.getLabelFont());
        btnStrike.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.STRIKE_ICON)));
        btnStrike.setMnemonic('K');
        btnStrike.setMinimumSize(new java.awt.Dimension(39, 23));
        btnStrike.setPreferredSize(new java.awt.Dimension(39, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlHeader.add(btnStrike, gridBagConstraints);

        btnCentre.setFont(CoeusFontFactory.getLabelFont());
        btnCentre.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.CENTER_ALIGNMENT)));
        btnCentre.setToolTipText("Center");
        btnCentre.setMinimumSize(new java.awt.Dimension(39, 23));
        btnCentre.setPreferredSize(new java.awt.Dimension(39, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlHeader.add(btnCentre, gridBagConstraints);

        btnRight.setFont(CoeusFontFactory.getLabelFont());
        btnRight.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.RIGHT_ALIGNMENT)));
        btnRight.setToolTipText("Align Right");
        btnRight.setMinimumSize(new java.awt.Dimension(39, 23));
        btnRight.setPreferredSize(new java.awt.Dimension(39, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        pnlHeader.add(btnRight, gridBagConstraints);

        btnLeft.setFont(CoeusFontFactory.getLabelFont());
        btnLeft.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.LEFT_ALIGNMENT)));
        btnLeft.setToolTipText("Align Left");
        btnLeft.setMinimumSize(new java.awt.Dimension(39, 23));
        btnLeft.setPreferredSize(new java.awt.Dimension(39, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlHeader.add(btnLeft, gridBagConstraints);

        btnPrintPreview.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_PREVIEW_ICON)));
        btnPrintPreview.setMinimumSize(new java.awt.Dimension(39, 23));
        btnPrintPreview.setPreferredSize(new java.awt.Dimension(39, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlHeader.add(btnPrintPreview, gridBagConstraints);

        btnBold.setIcon(new javax.swing.ImageIcon( getClass().getClassLoader().getResource(CoeusGuiConstants.BOLD_ICON)));
        btnBold.setMinimumSize(new java.awt.Dimension(39, 23));
        btnBold.setPreferredSize(new java.awt.Dimension(39, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        pnlHeader.add(btnBold, gridBagConstraints);

        cmbForeGround.setMinimumSize(new java.awt.Dimension(90, 22));
        cmbForeGround.setPreferredSize(new java.awt.Dimension(90, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        pnlHeader.add(cmbForeGround, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(pnlHeader, gridBagConstraints);

        scrpntxtComponent.setMinimumSize(new java.awt.Dimension(990, 540));
        scrpntxtComponent.setPreferredSize(new java.awt.Dimension(990, 540));
        scrpntxtComponent.setViewportView(txtAnnotation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(scrpntxtComponent, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JToggleButton btnBold;
    public javax.swing.JToggleButton btnCentre;
    public javax.swing.JToggleButton btnItalic;
    public javax.swing.JToggleButton btnLeft;
    public javax.swing.JButton btnPrintPreview;
    public javax.swing.JToggleButton btnRight;
    public javax.swing.JToggleButton btnStrike;
    public javax.swing.JToggleButton btnUnderline;
    public javax.swing.JComboBox cmbFontFamily;
    public javax.swing.JComboBox cmbFontSize;
    public ColorComboBox cmbForeGround;
    public javax.swing.JPanel pnlHeader;
    public javax.swing.JScrollPane scrpntxtComponent;
    public edu.mit.coeus.utils.CoeusTextPane txtAnnotation;
    // End of variables declaration//GEN-END:variables
    
    
    class ColorComboBoxModel extends DefaultComboBoxModel {
    
    private Vector vecData;
    private ColorBean selectedColor;
    
    ColorComboBoxModel() {
        vecData = new Vector();
        vecData.add(new ColorBean(Color.lightGray,"LIght Gray"));
        vecData.add(new ColorBean(Color.gray,"Gray"));
        vecData.add(new ColorBean(Color.darkGray,"Dark Gray"));
        vecData.add(new ColorBean(Color.black,"Black"));
        vecData.add(new ColorBean(Color.red,"Red"));
        vecData.add(new ColorBean(Color.pink,"Pink"));
        vecData.add(new ColorBean(Color.orange,"Orange"));
        vecData.add(new ColorBean(Color.yellow,"Yellow"));
        vecData.add(new ColorBean(Color.green,"Green"));
        vecData.add(new ColorBean(Color.magenta,"Magenta"));
        vecData.add(new ColorBean(Color.cyan,"Cyan"));
        vecData.add(new ColorBean(Color.blue,"Blue"));
        vecData.add(new ColorBean(null,"more..."));
    }
    
    public int getSize() {
        return vecData.size();
    }
    
    public Object getElementAt(int index) {
        Object retValue = vecData.get(index);
        return retValue;
    }
    
    public Object getSelectedItem() {
        Object retValue ;
        if(selectedColor == null) {
            retValue = vecData.get(0);
        }else {
            retValue = selectedColor;
        }
        
        return retValue;
    }
    
    public void setSelectedItem(Object value) {
        selectedColor = (ColorBean)value;
    }
    
    public void addElement(Object obj) {
        if(obj instanceof ColorBean) {
            vecData.add(obj);
        }
    }
}

class ColorRenderer implements ListCellRenderer {
    
    private ColorPanel colorPanel;
    
    ColorRenderer() {
        colorPanel = new ColorPanel();
    }
    
    
    public Component getListCellRendererComponent(JList jList, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        ColorBean colorBean = (ColorBean)value;
        
        colorPanel.setColor(colorBean.getColor(),colorBean.getColorName());
        colorPanel.setBackground(Color.lightGray);
        
        if(isSelected) {
        }else {
            
        }
        return colorPanel;
    }
}
}
