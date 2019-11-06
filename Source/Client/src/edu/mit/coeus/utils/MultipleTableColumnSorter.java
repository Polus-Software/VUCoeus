package edu.mit.coeus.utils;

/*
 * MultipleTableColumnSorter.java
 *
 * Created on Jan 19, 2005, 10:39 PM
 */

import java.awt.*;
import java.awt.event.*;
import java.text.Collator;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

/**
 * TableSorter is a decorator for TableModels; adding sorting
 * functionality to a supplied TableModel. TableSorter does
 * not store or copy the data in its TableModel; instead it maintains
 * a map from the row indexes of the view to the row indexes of the
 * model. As requests are made of the sorter (like getValueAt(row, col))
 * they are passed to the underlying model after the row numbers
 * have been translated via the internal mapping array. This way,
 * the TableSorter appears to hold another copy of the table
 * with the rows in a different order.
 * <p/>
 * TableSorter registers itself as a listener to the underlying model,
 * just as the JTable itself would. Events recieved from the model
 * are examined, sometimes manipulated (typically widened), and then
 * passed on to the TableSorter's listeners (typically the JTable).
 * If a change to the model has invalidated the order of TableSorter's
 * rows, a note of this is made and the sorter will resort the
 * rows the next time a value is requested.
 * <p/>
 * When the tableHeader property is set, either by using the
 * setTableHeader() method or the two argument constructor, the
 * table header may be used as a complete UI for TableSorter.
 * The default renderer of the tableHeader is decorated with a renderer
 * that indicates the sorting status of each column. In addition,
 * a mouse listener is installed with the following behavior:
 * <ul>
 * <li>
 * Mouse-click: Clears the sorting status of all other columns
 * and advances the sorting status of that column through three
 * values: {NOT_SORTED, ASCENDING, DESCENDING} (then back to
 * NOT_SORTED again).
 * <li>
 * SHIFT-mouse-click: Clears the sorting status of all other columns
 * and cycles the sorting status of the column through the same
 * three values, in the opposite order: {NOT_SORTED, DESCENDING, ASCENDING}.
 * <li>
 * CONTROL-mouse-click and CONTROL-SHIFT-mouse-click: as above except
 * that the changes to the column do not cancel the statuses of columns
 * that are already sorting - giving a way to initiate a compound
 * sort.
 *
 * @author  nadhgj
 */

public class MultipleTableColumnSorter extends DefaultTableModel {
    protected TableModel tableModel;

    public static final int DESCENDING = -1;
    public static final int NOT_SORTED = 0;
    public static final int ASCENDING = 1;

    private static Directive EMPTY_DIRECTIVE = new Directive(-1, NOT_SORTED);

    private boolean usingClassicArrow = false;
    public void setUsingClassicArrow(boolean b) { usingClassicArrow = b;}
    public boolean usingClassicArrow() { return usingClassicArrow; }
    
    private boolean usingCollators = true;
    public void setUsingCollators(boolean b) { usingCollators = b; setupCollators(); }
    public boolean usingCollators() { return usingCollators; }
    
    private Comparator comparableComparator;
    private Comparator lexicalComparator;
    
    private Row[] viewToModel;
    private int[] modelToView;

    private JTableHeader tableHeader;
    private MouseListener mouseListener;
    private TableModelListener tableModelListener;
    private Map columnComparators = new HashMap();
    private List sortingColumns = new ArrayList();
    private JTable currentTable;
    private boolean tableSelected = false;
    
    DateUtils dtUtils = new DateUtils();
    private java.text.SimpleDateFormat dtFormat
        = new java.text.SimpleDateFormat("MM/dd/yyyy");

    public MultipleTableColumnSorter() {
        this.mouseListener = new MouseHandler();
        this.tableModelListener = new TableModelHandler();
        setupCollators();
    }

    public MultipleTableColumnSorter(TableModel tableModel) {
        this();
        setTableModel(tableModel);
    }

    public MultipleTableColumnSorter(TableModel tableModel, JTableHeader tableHeader) {
        this();
        setTableHeader(tableHeader);
        setTableModel(tableModel);
    }
    
    
    
    protected void setupCollators() {
        if (usingCollators()) {
            comparableComparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (o1 instanceof String) {;
                        Collator collator = Collator.getInstance();
                        collator.setStrength(Collator.SECONDARY);
                        return collator.compare(
                                o1.toString(), o2.toString());            
                    } else {
                        return ((Comparable) o1).compareTo(o2);
                    }
                }
            };
            
            lexicalComparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    Collator collator = Collator.getInstance();
                    collator.setStrength(Collator.SECONDARY);
                    return collator.compare(
                            o1.toString(), o2.toString());            
                }
            };

        } else {

            comparableComparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Comparable) o1).compareTo(o2);
                }
            };

            lexicalComparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            };
        }
    }
    
    private void clearSortingState() {
        viewToModel = null;
        modelToView = null;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(TableModel tableModel) {
        if (this.tableModel != null) {
            this.tableModel.removeTableModelListener(tableModelListener);
        }

        this.tableModel = tableModel;
        if (this.tableModel != null) {
            this.tableModel.addTableModelListener(tableModelListener);
        }

        clearSortingState();
        fireTableStructureChanged();
    }
    
    /**
     * this will insert a new row
     * @param aRow Row number to be inserted.
     * @param data collection of data values of the row.
     */
    public void insertRow(int aRow, Vector data) {
        ((DefaultTableModel)tableModel).insertRow(aRow,data); 
    }

    /** By default, implement TableModel by forwarding all messages 
     * to the model. 
     * @param aRow row number to be removed
     */
    public void removeRow(int aRow) {
        ((DefaultTableModel)tableModel).removeRow(aRow); 
    }
    
    public JTableHeader getTableHeader() {
        return tableHeader;
    }

    public void setTableHeader(JTableHeader tableHeader) {
        if (this.tableHeader != null) {
            this.tableHeader.removeMouseListener(mouseListener);
            TableCellRenderer defaultRenderer = this.tableHeader.getDefaultRenderer();
            if (defaultRenderer instanceof SortableHeaderRenderer) {
                this.tableHeader.setDefaultRenderer(((SortableHeaderRenderer) defaultRenderer).tableCellRenderer);
            }
        }
        this.tableHeader = tableHeader;
        if (this.tableHeader != null) {
            this.tableHeader.addMouseListener(mouseListener);
            this.tableHeader.setDefaultRenderer(
                    new SortableHeaderRenderer(this.tableHeader.getDefaultRenderer()));
        }
    }

    public boolean isSorting() {
        return sortingColumns.size() != 0;
    }

    private Directive getDirective(int column) {
        for (int i = 0; i < sortingColumns.size(); i++) {
            Directive directive = (Directive)sortingColumns.get(i);
            if (directive.column == column) {
                return directive;
            }
        }
        return EMPTY_DIRECTIVE;
    }

    public int getSortingStatus(int column) {
        return getDirective(column).direction;
    }

    private void sortingStatusChanged() {
        clearSortingState();
        fireTableDataChanged();
        if (tableHeader != null) {
            tableHeader.repaint();
        }
    }

    public void setSortingStatus(int column, int status) {
        Directive directive = getDirective(column);
        if (directive != EMPTY_DIRECTIVE) {
            sortingColumns.remove(directive);
        }
        if (status != NOT_SORTED) {
            sortingColumns.add(new Directive(column, status));
        }
        sortingStatusChanged();
    }
    
    public void setSortingStatus(int[] column, int status) {
        cancelSorting();
        for (int index=0; index<column.length; index++) {
            Directive directive = getDirective(column[index]);
            if (directive != EMPTY_DIRECTIVE) {
                sortingColumns.remove(directive);
            }
            if (status != NOT_SORTED) {
                sortingColumns.add(new Directive(column[index], status));
            }
            sortingStatusChanged();
        }
    }
    
    protected Icon getHeaderRendererIcon(int column, int size) {
        Directive directive = getDirective(column);
        if (directive == EMPTY_DIRECTIVE) {
            return null;
        }
        return createArrow(directive.direction == DESCENDING, size, sortingColumns.indexOf(directive));
    }

    private void cancelSorting() {
        sortingColumns.clear();
        sortingStatusChanged();
    }

    public void setColumnComparator(Class type, Comparator comparator) {
        if (comparator == null) {
            columnComparators.remove(type);
        } else {
            columnComparators.put(type, comparator);
        }
    }

    protected Comparator getComparator(int column) {
        Class columnType = tableModel.getColumnClass(column);
        Comparator comparator = (Comparator) columnComparators.get(columnType);
        if (comparator != null) {
            return comparator;
        }
        if (Comparable.class.isAssignableFrom(columnType)) {
            return comparableComparator;
        }
        return lexicalComparator;
    }

    private Row[] getViewToModel() {
        if (viewToModel== null) {
            int tableModelRowCount = tableModel.getRowCount();
            viewToModel = new Row[tableModelRowCount];
            for (int row = 0; row < tableModelRowCount; row++) {
                viewToModel[row] = new Row(row);
            }
            if (isSorting()) {
                Arrays.sort(viewToModel);
            }
        }
        return viewToModel;
    }

    public int modelIndex(int viewIndex) {
        return getViewToModel()[viewIndex].modelIndex;
    }

    private int[] getModelToView() {
        if (modelToView == null) {
            int n = getViewToModel().length;
            modelToView = new int[n];
            for (int i = 0; i < n; i++) {
                modelToView[modelIndex(i)] = i;
            }
        }
        return modelToView;
    }

    // TableModel interface methods 

    public int getRowCount() {
        return (tableModel == null) ? 0 : tableModel.getRowCount();
    }

    public int getColumnCount() {
        return (tableModel == null) ? 0 : tableModel.getColumnCount();
    }

    public String getColumnName(int column) {
        return tableModel.getColumnName(column);
    }

    public Class getColumnClass(int column) {
        return tableModel.getColumnClass(column);
    }

    public boolean isCellEditable(int row, int column) {
        return tableModel.isCellEditable(modelIndex(row), column);
    }

    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(modelIndex(row), column);
    }

    public void setValueAt(Object aValue, int row, int column) {
        tableModel.setValueAt(aValue, modelIndex(row), column);
    }

    // Helper classes
    
    private class Row implements Comparable {
        private int modelIndex;
        
        public Row(int index) {
            this.modelIndex = index;
        }
        
        public int compareTo(Object o) {
            int row1 = modelIndex;
            int row2 = ((Row) o).modelIndex;
            
            for (Iterator it = sortingColumns.iterator(); it.hasNext();) {
                Directive directive = (Directive) it.next();
                int column = directive.column;
                Object o1 = tableModel.getValueAt(row1, column);
                Object o2 = tableModel.getValueAt(row2, column);
                int val1 = 0, val2 = 0;
                int comparison = 0;
                // Define null less than everything, except null.
                if (o1 == null && o2 == null) {
                    comparison = 0;
                } else if (o1 == null) {
                    comparison = -1;
                } else if (o2 == null) {
                    comparison = 1;
                } else {
                    try{
                        val1 = Integer.parseInt( o1.toString() );
                        val2 = Integer.parseInt( o2.toString() );
                        if (val1 < val2) {
                            comparison = -1;
                        } else if (val1 > val2) {
                            comparison = 1;
                        } else if (val1 == val2){
                            comparison = 0;
                        }
                        
                    }catch(NumberFormatException nfe) {
                        String rest1 = dtUtils.restoreDate(o1.toString(),"/:-,");
                        String rest2 = dtUtils.restoreDate(o2.toString(),"/:-,");
                        if(dtUtils.validateDate(rest1,"/:-,") && dtUtils.validateDate(rest2,"/:-,")){
                            Date d1,d2;
                            long n1=0,n2=0;
                            try{
                                d1 = dtFormat.parse(rest1);
                                d2 = dtFormat.parse(rest2);
                                n1 = d1.getTime();
                                n2 = d2.getTime();
                            }catch (java.text.ParseException pe){
                                d1 = new Date();
                                d2 = new Date();
                            }
                            if (n1 < n2) {
                                comparison = -1;
                            } else if (n1 > n2) {
                                comparison = 1;
                            } else {
                                comparison = 0;
                            }
                        }else
                            comparison = getComparator(column).compare(o1, o2);
                    }
                }
                if (comparison != 0) {
                    return directive.direction == DESCENDING ? -comparison : comparison;
                }
            }
            return 0;
        }
    }
    
    
    
    private class TableModelHandler implements TableModelListener {
        public void tableChanged(TableModelEvent e) {
            // If we're not sorting by anything, just pass the event along.             
            if (!isSorting()) {
                clearSortingState();
                fireTableChanged(e);
                return;
            }
                
            // If the table structure has changed, cancel the sorting; the             
            // sorting columns may have been either moved or deleted from             
            // the model. 
            if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
                cancelSorting();
                fireTableChanged(e);
                return;
            }

            // We can map a cell event through to the view without widening             
            // when the following conditions apply: 
            // 
            // a) all the changes are on one row (e.getFirstRow() == e.getLastRow()) and, 
            // b) all the changes are in one column (column != TableModelEvent.ALL_COLUMNS) and,
            // c) we are not sorting on that column (getSortingStatus(column) == NOT_SORTED) and, 
            // d) a reverse lookup will not trigger a sort (modelToView != null)
            //
            // Note: INSERT and DELETE events fail this test as they have column == ALL_COLUMNS.
            // 
            // The last check, for (modelToView != null) is to see if modelToView 
            // is already allocated. If we don't do this check; sorting can become 
            // a performance bottleneck for applications where cells  
            // change rapidly in different parts of the table. If cells 
            // change alternately in the sorting column and then outside of             
            // it this class can end up re-sorting on alternate cell updates - 
            // which can be a performance problem for large tables. The last 
            // clause avoids this problem. 
            int column = e.getColumn();
            if (e.getFirstRow() == e.getLastRow()
                    && column != TableModelEvent.ALL_COLUMNS
                    && getSortingStatus(column) == NOT_SORTED
                    && modelToView != null) {
                int viewIndex = getModelToView()[e.getFirstRow()];
                fireTableChanged(new TableModelEvent(MultipleTableColumnSorter.this, 
                                                     viewIndex, viewIndex, 
                                                     column, e.getType()));
                return;
            }

            // Something has happened to the data that may have invalidated the row order. 
            clearSortingState();
            fireTableDataChanged();
            return;
        }
    }

    private class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            JTableHeader h = (JTableHeader) e.getSource();
            currentTable = h.getTable();
            int selRow = currentTable.getSelectedRow();
            String tableHiddenIndex = null;
            TableColumnModel columnModel = h.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            tableHiddenIndex = getTempIndex(selRow);
            if (column != -1) {
                int status = getSortingStatus(column);
                if (!e.isControlDown()) {
                    cancelSorting();
                }
                // Cycle the sorting states through {NOT_SORTED, ASCENDING, DESCENDING} or 
                // {NOT_SORTED, DESCENDING, ASCENDING} depending on whether shift is pressed. 
                status = status + (e.isShiftDown() ? -1 : 1);
                status = (status + 4) % 3 - 1; // signed mod, returning {-1, 0, 1}
                setSortingStatus(column, status);
                if( tableSelected ) {
                    selRow = getViewIndex(tableHiddenIndex);
                    currentTable.setRowSelectionInterval(selRow,selRow);
                }
            }
            viewToModel = null;
        }
    }
    
    private String getTempIndex(int selRow) {
        String tableHiddenIndex = null;
        if( selRow != -1 ) {
            String tempIndex = null;
            tableSelected = true;
            tempIndex = (String) currentTable.getValueAt(
            selRow,currentTable.getColumnCount() -1 );
            tableHiddenIndex = tempIndex;
        }
        return tableHiddenIndex;
    }
    
    public void doSort(JTable table,Vector col) {
        final JTable tableView = table;
        currentTable = table;
        int selRow = table.getSelectedRow();
        cancelSorting();
        tableView.setColumnSelectionAllowed(false);
        String tableHiddenIndex = null;
        tableHiddenIndex = getTempIndex(selRow);
        for(int i=0;i<col.size();i++) {
            TableColumnModel columnModel =
            tableView.getColumnModel();
            Vector vecColumn = (Vector)col.get(i);
            int column =
            tableView.convertColumnIndexToModel(((Integer)vecColumn.get(1)).intValue());
            boolean ascending = ((Boolean)vecColumn.get(2)).booleanValue();
            setSortingStatus(column, ascending==true ? ASCENDING : DESCENDING);
            
        }
        if( tableSelected ) {
            selRow = getViewIndex(tableHiddenIndex);
            currentTable.setRowSelectionInterval(selRow,selRow);
        }
        viewToModel = null;
    }
    
    private int getViewIndex( String row ) {
        int rowCount = currentTable.getRowCount();
        String tempIndex = null;
        for( int i=0; i< rowCount; i++ ){
            tempIndex = (String) currentTable.getValueAt(
            i,currentTable.getColumnCount() -1 ) ;
            if( row.equals(tempIndex )) {
                return i;
            }
        }
        return 0;
    }
    
    private Icon createArrow(boolean descending, int size, int priority) {
        return usingClassicArrow
                ? (Icon) new ClassicArrow(descending, size, priority)
                : new Arrow(       descending, size, priority);
    }

    private static class Arrow implements Icon {
        private boolean descending;
        private int priority;
        private int size;

        public Arrow(boolean descending, int size, int priority) {
            this.descending = descending;
            this.size = size;
            this.priority = priority;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {

            // Override base size with a value calculated from the
            // component's font.
            updateSize(c);
            
            Color color = c == null ? Color.BLACK : c.getForeground();             
            g.setColor(color);

            int npoints = 3;
            int [] xpoints = new int [] { 0, size/2, size };
            int [] ypoints = descending 
                    ? new int [] { 0,    size, 0 }
                    : new int [] { size, 0,    size };

            Polygon triangle = new Polygon(xpoints, ypoints, npoints);
                    
            // Center icon vertically within the column heading label.
            int dy = (c.getHeight() - size) / 2;
            
            g.translate(x, dy);
            g.drawPolygon(triangle);
            g.fillPolygon(triangle);
            g.translate(-x, -dy);

        }
        
        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }
        
        private void updateSize(Component c) {
            if (c != null) {
                FontMetrics fm = c.getFontMetrics(c.getFont());
                int baseHeight = fm.getAscent();

                // In a compound sort, make each succesive triangle 20% 
                // smaller than the previous one. 
                size = (int)(baseHeight*3/4*Math.pow(0.8, priority));
            }
        }
    }
    
    private static class ClassicArrow implements Icon {

        private boolean descending;
        private int size;
        private int priority;

        public ClassicArrow(boolean descending, int size, int priority) {
            this.descending = descending;
            this.size = size;
            this.priority = priority;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color color = c == null ? Color.GRAY : c.getBackground();           
  
            // In a compound sort, make each succesive triangle 20% 
            // smaller than the previous one. 
            int dx = (int)(size/2*Math.pow(0.8, priority));
            int dy = descending ? dx : -dx;
            // Align icon (roughly) with font baseline. 
            y = y + 5*size/6 + (descending ? -dy : 0);
            int shift = descending ? 1 : -1;
            g.translate(x, y);
            
            // Right diagonal. 
            g.setColor(color.darker());
            g.drawLine(dx / 2, dy, 0, 0);
            g.drawLine(dx / 2, dy + shift, 0, shift);
            
            // Left diagonal. 
            g.setColor(color.brighter());
            g.drawLine(dx / 2, dy, dx, 0);
            g.drawLine(dx / 2, dy + shift, dx, shift);
            
            // Horizontal line. 
            if (descending) {
                g.setColor(color.darker().darker());
            } else {
                g.setColor(color.brighter().brighter());
            }
            g.drawLine(dx, 0, 0, 0);

            g.setColor(color);
            g.translate(-x, -y);
        }
        
        
        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }
    }

    private class SortableHeaderRenderer implements TableCellRenderer {
        private TableCellRenderer tableCellRenderer;

        public SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
            this.tableCellRenderer = tableCellRenderer;
        }

        public Component getTableCellRendererComponent(JTable table, 
                                                       Object value,
                                                       boolean isSelected, 
                                                       boolean hasFocus,
                                                       int row, 
                                                       int column) {
            Component c = tableCellRenderer.getTableCellRendererComponent(table, 
                    value, isSelected, hasFocus, row, column);
            if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                l.setHorizontalTextPosition(JLabel.LEFT);
                int modelColumn = table.convertColumnIndexToModel(column);
                l.setIcon(getHeaderRendererIcon(modelColumn, l.getFont().getSize()));
            }
            return c;
        }
    }

    private static class Directive {
        private int column;
        private int direction;

        public Directive(int column, int direction) {
            this.column = column;
            this.direction = direction;
        }
    }
}
