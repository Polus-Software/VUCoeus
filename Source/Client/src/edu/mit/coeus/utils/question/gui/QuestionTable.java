 /*
 * @(#)QuestionTable.java 1.0   08/27/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.question.gui;

import edu.mit.coeus.utils.question.bean.YNQBean;
import edu.mit.coeus.utils.question.bean.QuestionListBean;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.gui.FontCellRenderer;
import edu.mit.coeus.gui.MultiLineCellRenderer;
import edu.mit.coeus.utils.TypeConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This class constructs the Organization detail's question table
 *
 * @version :1.0 August 27, 2002, 1:35 PM
 * @author Guptha K
 */
public class QuestionTable extends JPanel implements TypeConstants{

    public JTable tblQuestionTable;
    private Vector tableData;
    private Vector columnNames;

    private Color backGround = this.getBackground();
    public Color rowSelectionColor = Color.red;
    private Color gridColor = backGround;
    private int rowHeight = 50;
    
    //Bug Fix : 907 Start 1
    //Modified for Case#3893 -Java 1.5 issues  - Start
   // private int codeHeaderWidth = 38;
    private int codeHeaderWidth = 50;
    //Case#3893 - End
    //Bug Fix : 907 End 1
    
    private int iconHeaderWidth = 25;
    private int questionHeaderWidth = 318;
    private int answersHeaderWidth = 132;
    public int prevSelectedRow = -1;
    private char functionType;
    public int[] answerOptions = null;
    public String radButtAnswer;

    YNQBean[] orgQuestionList;
    QuestionListBean[] questionList;

    public int selectedRow;

   /**
     * Constructor, which constructs the Organization maintenance QuestionTable form.
     * The functionality of the form (enable/disable the 
     * components) will be decided based on the parameter functionType.
     *
     * @param functionType char  decides to enable/disable the form controls A- Add mode
     *                           D- Display mode M- Modify mode
     * @param orgQuestionList
     * @param questionList
     */
    public QuestionTable(char functionType, YNQBean[] orgQuestionList, QuestionListBean[] questionList) {
        this.functionType = functionType;
        this.orgQuestionList = orgQuestionList;
        this.questionList = questionList;
    }

    /**
     * Adds this component to panel,which calls the helper methods to get the table data, column
     * names and answers of the table and initializes the required components
     */
    public void addTable() {
        tableData = getTableData();
        columnNames = getTableColumnNames();
        answerOptions = getAnswerOptions();
        initComponents();
    }

    /**
     * Set all answer options of the questions
     *
     * @param answerOptions  Array of answers for questions in an array.
     */
    public void setAnswerOptions(int[] answerOptions) {
        this.answerOptions = answerOptions;
    }

    /**
     * Gets all Answers of questions in an array
     *
     * @return Answers in primitive int array
     */
    private int[] getAnswerOptions() {
        return this.answerOptions;
    }

    /**
     * Sets the data that will be shown in Table.
     *
     * @param tableData  A Vector of data
     */
    public void setTableData(Vector tableData) {
        this.tableData = tableData;
    }

    /**
     * Gets the data of table.
     *
     * @return Table data in Vector
     */
    public Vector getTableData() {
        return tableData;
    }

    /**
     * Sets the Column Names of a table available in Vector
     *
     * @param columns Names of the Columns in a Vector
     */
    public void setTableColumnNames(Vector columns) {
        columnNames = columns;
    }

    /**
     * Gets the All column Names avaialble in Table in a Vector
     * @return Column Names in a Vector
     */
    public Vector getTableColumnNames() {
        return columnNames;
    }

    /**
     * An helper method to initialize the Table and its components, which
     * sets the default properties of the table.
     */
    private void initComponents() {

        DefaultTableModel dm = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                // only the last (3rd) column is editable as the radio button panel is there
                
                //Bug Fix : 907 Start 2 made col = 2 from 3 
                if (col == 2) {
                    return true;
                }
                //Bug Fix : 907 End 2
                return false;
            }

        };

        // the tabledata and column names
        dm.setDataVector(getTableData(), getTableColumnNames());

        tblQuestionTable = new javax.swing.JTable(dm);

        // Icon column does not have any column header
        EmptyHeaderRenderer iconRenderer = new EmptyHeaderRenderer();

        Enumeration enums = tblQuestionTable.getColumnModel().getColumns();

        // set the renders
        while (enums.hasMoreElements()) {
            TableColumn column = (TableColumn) enums.nextElement();
            // handicon
            
            //Bug Fix : 907 Start 3 commented the hand icon 
//            if (column.getModelIndex() == 0) {
//                column.setHeaderRenderer(iconRenderer);
//                column.setPreferredWidth(iconHeaderWidth);
//                column.setResizable(false);
//                column.setCellRenderer(new IconRenderer());
//
//            }
            //Bug Fix : 907 End 3 
            
            
            //question id
            if (column.getModelIndex() == 0) {//Bug fix : 907  changed the index from 1 to 0 
                column.setResizable(false);
                column.setPreferredWidth(codeHeaderWidth);
                column.sizeWidthToFit();
                column.setCellRenderer(new FontCellRenderer()); //Needs differnt Font

            }
            //question description
            if (column.getModelIndex() == 1) {//Bug fix : 907  changed the index from 2 to 1 
                column.setPreferredWidth(questionHeaderWidth); // width of column
                column.setResizable(false);
                column.setCellRenderer(new MultiLineCellRenderer()); //multiline cell

            }
            //answers
            if (column.getModelIndex() == 2) {//Bug fix : 907  changed the index from 3 to 2 
                column.setPreferredWidth(answersHeaderWidth); // width of column
                column.setResizable(false); // lock the expandable (fit the data size) property
                column.setCellRenderer(new RadioButtonRenderer());
                column.setCellEditor(new RadioButtonEditor(new JCheckBox()));
            }
        }
        // hide vertical lines
        tblQuestionTable.setShowVerticalLines(false);
        //selection background
        tblQuestionTable.setSelectionBackground(backGround);

        // disable the focus when any component is selected
        tblQuestionTable.setRequestFocusEnabled(false);

        // set the row height
        tblQuestionTable.setRowHeight(rowHeight);

        // row selection true , needs selected row in color
        tblQuestionTable.setRowSelectionAllowed(true);

        // column selection is false
        tblQuestionTable.setColumnSelectionAllowed(false);

        // set the background color of table
        tblQuestionTable.setBackground(backGround);

        // set the grid color
        tblQuestionTable.setGridColor(gridColor);

        // set the autoresize off
        tblQuestionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // set the single selection
        tblQuestionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // avoid table header to resize/ rearrange
        tblQuestionTable.getTableHeader().setReorderingAllowed(false);
        tblQuestionTable.getTableHeader().setResizingAllowed(false);

        // Apply scrlPnQuestionTable
        JScrollPane scrlPnQuestionTable = new JScrollPane(tblQuestionTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        //Bug Fix : 907 Start 4
        tblQuestionTable.setPreferredScrollableViewportSize(new Dimension(510, 300));
        //Bug Fix : 907 End 4
        
        // set the default font for header
        tblQuestionTable.setFont(CoeusFontFactory.getNormalFont());
        tblQuestionTable.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        // default selection is first(0) row
        try {
            tblQuestionTable.getSelectionModel().setSelectionInterval(0, 0);
        } catch (Exception ex) {
        }
        add(scrlPnQuestionTable);
        //setPreferredSize(new Dimension(530, 100));

        setFont(CoeusFontFactory.getLabelFont());
    } // end initcomponent

    /**
     * A Renderer for Answer column as it holds RadioButton Components in it's cells
     * which sets the default font and the Radiobutton names,Foreground, background colors.
     */

    class RadioButtonRenderer implements TableCellRenderer {

        public JPanel pnl;
        public ButtonGroup group1;
        public JRadioButton btnOne;
        public JRadioButton btnTwo;
        public JRadioButton btnThree;
        public JRadioButton btnFour;

        /**
         * The Defualt constructor which sets the default font
         */
        public RadioButtonRenderer() {

            btnOne = new JRadioButton("Yes");
            btnTwo = new JRadioButton("No");
            btnThree = new JRadioButton("N/A");
            btnFour = new JRadioButton("");

            UIDefaults ui = UIManager.getLookAndFeel().getDefaults();
            btnOne.setFont(CoeusFontFactory.getNormalFont());
            btnTwo.setFont(CoeusFontFactory.getNormalFont());
            btnThree.setFont(CoeusFontFactory.getNormalFont());
            btnFour.setFont(CoeusFontFactory.getNormalFont());
            btnFour.setVisible(false);

            btnOne.setBackground(backGround);
            btnTwo.setBackground(backGround);
            btnThree.setBackground(backGround);
            btnFour.setBackground(backGround);

            if (functionType == 'D') {
                btnOne.setEnabled(false);
                btnTwo.setEnabled(false);
                btnThree.setEnabled(false);
                btnFour.setEnabled(false);
            }
        }

        /**
         * The Overridden method of TableCellRenderer which is called for every cell when a component
         * is going to be rendered in its cell.
         * Returns the component used for drawing the cell.
         * This method is used to configure the renderer appropriately before drawing
         *
         * @param table  the JTable that is asking the renderer to draw; can be null
         * @param value  the value of the cell to be rendered. It is up to the specific renderer to interpret and draw the value. For example, if value is the string "true", it could be rendered as a string or it could be rendered as a check box that is checked. null is a valid value
         * @param isSelected  true if the cell is to be rendered with the selection highlighted; otherwise false
         * @param hasFocus if true, render cell appropriately. For example, put a special border on the cell, if the cell can be edited, render in the color used to indicate editing
         * @param row the row index of the cell being drawn. When drawing the header, the value of row is -1
         * @param column  the column index of the cell being drawn
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            selectedRow = tblQuestionTable.getSelectedRow();
            // If a this cell row is selected then show the components text (label) in red color
            if (isSelected) {
                btnOne.setForeground(rowSelectionColor);
                btnTwo.setForeground(rowSelectionColor);
                btnThree.setForeground(rowSelectionColor);
                btnFour.setForeground(rowSelectionColor);
            } else {
                btnOne.setForeground(null);
                btnTwo.setForeground(null);
                btnThree.setForeground(null);
                btnFour.setForeground(null);
            }

            pnl = new JPanel();
            pnl.setBackground(backGround);
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));
            group1 = new ButtonGroup();


            if (value == null) return null;

            // Print the 2 buttons in this "row" by comparing the value of answerOptions at this row
            // add default buttons (btnOne, btnTwo)
            group1.add(btnOne);
            group1.add(btnTwo);

            pnl.add(btnOne);
            pnl.add(btnTwo);

            btnOne.setSelected(false);
            btnTwo.setSelected(false);

            btnOne.setFocusPainted(false);
            btnTwo.setFocusPainted(false);


            group1.add(btnThree);
            pnl.add(btnThree);
            btnThree.setSelected(false);
            btnThree.setFocusPainted(false);


            group1.add(btnFour);
            pnl.add(btnFour);
            btnFour.setSelected(false);
            btnFour.setFocusPainted(false);

            // Print the 3 buttons in this "row" by comparing the value of answerOptions at this row
            if (answerOptions[row] != 3) {
                btnThree.setVisible(false);
            } else {
                btnThree.setVisible(true);
            }

            switch (Integer.parseInt((String) value)) {

                case 1:
                    btnOne.setSelected(true);
                    break;
                case 2:
                    btnTwo.setSelected(true);
                    break;
                case 3:
                    btnThree.setSelected(true);
                    break;
                case 4:
                    btnFour.setSelected(true);
                    break;
            }

            return pnl;

        }
    }

    /**
     * A Cell Editor for the Column Answer, which is called when the cell is about to be modified.
     * Radio button object that would like to be an editor of values for components  needs to implement
     */
    class RadioButtonEditor extends DefaultCellEditor implements ItemListener {

        public JPanel pnl;
        public ButtonGroup group1;
        public JRadioButton btnOne;
        public JRadioButton btnTwo;
        public JRadioButton btnThree;
        public JRadioButton btnFour;

        /**
         * Default constructor which sets the default font for this editor
         */
        public RadioButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            btnOne = new JRadioButton("Yes");
            btnTwo = new JRadioButton("No");
            btnThree = new JRadioButton("N/A");
            btnFour = new JRadioButton("");

            btnOne.setFont(CoeusFontFactory.getNormalFont());
            btnTwo.setFont(CoeusFontFactory.getNormalFont());
            btnThree.setFont(CoeusFontFactory.getNormalFont());
            btnFour.setFont(CoeusFontFactory.getNormalFont());
            btnFour.setVisible(false);

            btnOne.setBackground(backGround);
            btnTwo.setBackground(backGround);
            btnThree.setBackground(backGround);
            btnFour.setBackground(backGround);

            if (functionType == 'D') {
                btnOne.setEnabled(false);
                btnTwo.setEnabled(false);
                btnThree.setEnabled(false);
                btnFour.setEnabled(false);
            }

            RadActionListener radListener = new RadActionListener();
            btnOne.addActionListener(radListener);
            btnTwo.addActionListener(radListener);
            btnThree.addActionListener(radListener);
            btnFour.addActionListener(radListener);
        }

        /**
         * Sets an initial value for the editor. This will cause the editor to stopEditing
         * and lose any partially edited value if the editor is editing when this method is called.
         * Returns the component that should be added to the client's Component hierarchy.
         * Once installed in the client's hierarchy this component will then be able to draw and receive user input.
         *
         * @param table  the JTable that is asking the editor to edit; can be null
         * @param value  the value of the cell to be edited; it is up to the specific editor to interpret and draw the value. For example, if value is the string "true", it could be rendered as a string or it could be rendered as a check box that is checked. null is a valid value
         * @param isSelected  true if the cell is to be rendered with highlighting
         * @param row  the row of the cell being edited
         * @param column  the column of the cell being edited
         * @return the component for editing
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            pnl = new JPanel();
            pnl.setBackground(backGround);
            pnl.setLayout(new BoxLayout(pnl, BoxLayout.X_AXIS));
            group1 = new ButtonGroup();
            if (value == null) return null;


            // add default buttons (btnOne, btnTwo)
            group1.add(btnOne);
            group1.add(btnTwo);

            pnl.add(btnOne);
            pnl.add(btnTwo);

            btnOne.setSelected(false);
            btnTwo.setSelected(false);

            btnOne.setFocusPainted(false);
            btnTwo.setFocusPainted(false);

            group1.add(btnThree);

            pnl.add(btnThree);

            btnThree.setSelected(false);
            btnThree.setFocusPainted(false);

            group1.add(btnFour);

            pnl.add(btnFour);

            btnFour.setSelected(false);
            btnFour.setFocusPainted(false);

            // Print the 3rd(N/A) radiobutton in this "row" by comparing the value of answerOptions at this row
            if (answerOptions[row] != 3) {
                btnThree.setVisible(false);
            } else {
                btnThree.setVisible(true);
            }

            switch (Integer.parseInt((String) value)) {

                case 1:
                    btnOne.setForeground(rowSelectionColor);
                    btnTwo.setForeground(rowSelectionColor);
                    btnThree.setForeground(rowSelectionColor);
                    btnFour.setForeground(rowSelectionColor);
                    btnOne.setSelected(true);
                    radButtAnswer = "1";
                    break;
                case 2:
                    btnOne.setForeground(rowSelectionColor);
                    btnTwo.setForeground(rowSelectionColor);
                    btnThree.setForeground(rowSelectionColor);
                    btnFour.setForeground(rowSelectionColor);
                    btnTwo.setSelected(true);
                    radButtAnswer = "2";
                    break;
                case 3:
                    btnOne.setForeground(rowSelectionColor);
                    btnTwo.setForeground(rowSelectionColor);
                    btnThree.setForeground(rowSelectionColor);
                    btnFour.setForeground(rowSelectionColor);
                    btnThree.setSelected(true);
                    radButtAnswer = "3";
                    break;
                case 4:
                    btnOne.setForeground(rowSelectionColor);
                    btnTwo.setForeground(rowSelectionColor);
                    btnThree.setForeground(rowSelectionColor);
                    btnFour.setForeground(rowSelectionColor);
                    btnFour.setSelected(true);
                    radButtAnswer = "4";
                    break;

            }

            return pnl;
        }

        public Object getCellEditorValue() {
            if (btnFour.isSelected()) {
                return "4";
            }
            if (btnThree.isSelected()) {
                return "3";
            }
            if (btnTwo.isSelected()) {
                return "2";
            }
            if (btnOne.isSelected()) {
                return "1";
            }
            return "";

        }

        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }

        class RadActionListener implements ActionListener {
            public void actionPerformed(ActionEvent ie) {
                String ans = "";
                Object source = ie.getSource();
                if (source.equals(btnOne)) {
                    ans = "Y";
                } else if (source.equals(btnTwo)) {
                    ans = "N";
                } else if (source.equals(btnThree)) {
                    ans = "X";
                } else if (source.equals(btnFour)) {
                    ans = "";
                }
                String questionId = questionList[selectedRow].getQuestionId();
                if (orgQuestionList != null) {
                    for (int j = 0; j < orgQuestionList.length; j++) {
                        if (orgQuestionList[j].getQuestionId().equalsIgnoreCase(questionId)) {
                             orgQuestionList[j].setAnswer(ans);
                             /*if ( functionType == MODIFY_MODE ) {
                                orgQuestionList[j].setAcType(UPDATE_RECORD);
                             }else if ( functionType == ADD_MODE ) {
                                orgQuestionList[j].setAcType(INSERT_RECORD);
                             }*/
                             if ( orgQuestionList[j].getAcType() == null 
                                    || !orgQuestionList[j].getAcType().equals( INSERT_RECORD ) ) {
                                orgQuestionList[j].setAcType(UPDATE_RECORD);
                             }
                            break;
                        }//if
                    }//for
                }//if
            }
        }

    }
}