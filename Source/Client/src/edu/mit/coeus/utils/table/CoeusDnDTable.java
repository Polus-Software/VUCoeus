/*
 * CoeusJTable.java
 *
 * Created on July 17, 2003, 5:46 PM
 */

package edu.mit.coeus.utils.table;

import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.Image;

import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.awt.Dimension;
import java.awt.Color;
import java.io.IOException;

/** JTable supporting Drag and Drop operations.
 * @author sharathk
 */
public class CoeusDnDTable extends JTable implements DropTargetListener, DragSourceListener, DragGestureListener {
    
    private DropTarget dropTarget;
    private DragSource dragSource;
    
    private boolean deleteDataOnDrop = true;
    private boolean addDataOnDrop = true;
    private boolean dropEnabled = true;
    private boolean dragEnabled = true;
    
    private HashMap tableColumnMap;
    
    /** creates a new instance of this.
     */
    public CoeusDnDTable() {
        super();
        
        dropTarget = new DropTarget(this,this);
        
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this,DnDConstants.ACTION_MOVE,this);
        
        this.setBackground(Color.lightGray);
    }
    
    /** creates a new instance of this.
     * @param rowData The Vector of Vectors of Object values.
     * @param columnNames column Names.
     */
    public CoeusDnDTable(Vector rowData, Vector columnNames) {
        super(rowData,columnNames);
        
        dropTarget = new DropTarget(this,this);
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this,DnDConstants.ACTION_MOVE,this);
        
        this.setRowSelectionAllowed(true);
        
        this.setBackground(Color.lightGray);
    }
    
    public Dimension getPreferredSize() {
        /*Dimension d;
        d = super.getPreferredSize();
        d.setSize(d.getWidth(), d.getHeight() + getRowHeight());
        return d;*/
        if(super.getPreferredSize().getHeight() < getParent().getSize().getHeight()) {
            return getParent().getSize();
        }
        else {
            return super.getPreferredSize();
        }
    }
    
    /** sets the column visibility.
     * @param columnIndex column Index
     * @param visible if true column is visible else not visible.
     */
    public void setColumnVisibility(int columnIndex, boolean visible) {
        TableColumn tableColumn;
        if(!visible) {
            if(tableColumnMap==null) {
                tableColumnMap = new HashMap();
            }
            tableColumn = getColumnModel().getColumn(columnIndex);
            tableColumnMap.put(new Integer(columnIndex),tableColumn);
            removeColumn(tableColumn);
        }
        else {
            tableColumn = (TableColumn)tableColumnMap.get(new Integer(columnIndex));
            addColumn(tableColumn);
            moveColumn(getColumnCount() - 1, columnIndex);
        }
    }
    
    /** returns true if column is visible
     * else return false.
     * @param columnIndex column Index
     * @return true if column is visible
     * else return false.
     */
    public boolean isColumnVisible(int columnIndex) {
        if(columnIndex > getColumnCount() || columnIndex < 0) return false;
        if(tableColumnMap.get(new Integer(columnIndex)) == null) {
            return true;
        }
        return false;
    }
    
    /** deletes/retains the data(row) on Drop which was dragged from here.
     * @param deleteDataOnDrop if true deletes the data(row) on Drop which was dragged from here.
     * if false retains the data(row).
     */
    public void setDeleteDataOnDrop(boolean deleteDataOnDrop) {
        this.deleteDataOnDrop = deleteDataOnDrop;
    }
        
    /** Determines whether the data(row) should be deleted/retained after drop.
     * @return returns true if data(row) will be deleted after Drop.
     * else returns false.
     */
    public boolean getDeleteDataOnDrop() {
        return deleteDataOnDrop;
    }
    
    /**
     * @param addDataOnDrop if true adds the data(row) on Drop which was dragged from other source.
     * if false doesn't add the data(row).
     */
    public void setAddDataOnDrop(boolean addDataOnDrop) {
        this.addDataOnDrop = addDataOnDrop;
    }
    
    /** 
     * @return returns true if data(row) will be added after Drop.
     * else returns false.
     */
    public boolean isAddDataOnDrop() {
        return addDataOnDrop;
    }
    
    /** enables/disables the component from listening to drop events.
     * @param dropEnabled if true listens to drop events.
     * else does not.
     */
    public void setDropEnabled(boolean dropEnabled) {
        this.dropEnabled = dropEnabled;
        dropTarget.setActive(dropEnabled);
    }
    
    /** determines whether the component listens to drop events.
     * @return true if component listens to drop events.
     * else return false.
     */
    public boolean isDropEnabled() {
        return dropEnabled;
    }
    
    /** enables/disables the component from listening to drag events.
     * @param dragEnabled if true listens to drag events.
     * else does not.
     */
    public void setDragEnabled(boolean dragEnabled) {
        this.dragEnabled = dragEnabled;
    }
    
    /** Sets the data model for this table to defaultTableModel and registers with it for listener notifications from the new data model.
     * @param defaultTableModel data model for this table
     */
    public void setModel(DefaultTableModel defaultTableModel) {
        super.setModel(defaultTableModel);
    }
    
    /** Called as the cursor's hotspot enters a platform-dependent drop site. This method is invoked when all the following conditions are true:
     * The cursor's hotspot enters the operable part of a platform- dependent drop site.
     * The drop site is active.
     * The drop site accepts the drag.
     * @param event the DragSourceDragEvent
     */
    public void dragEnter(DropTargetDragEvent event) {
        //System.out.println("droptarget : dragEnter"+getName());
        event.acceptDrag(DnDConstants.ACTION_MOVE);
    }
    
    /** CCalled as the cursor's hotspot exits a platform-dependent drop site. This method is invoked when any of the following conditions are true:
     * The cursor's hotspot no longer intersects the operable part of the drop site associated with the previous dragEnter() invocation.
     * OR
     * The drop site associated with the previous dragEnter() invocation is no longer active.
     * OR
     * The current drop site has rejected the drag.
     * @param event DropTargetEvent
     */
    public void dragExit(DropTargetEvent event) {
        //System.out.println("droptarget : dragExit"+getName());
    }
    
    /** Called as the cursor's hotspot moves over a platform-dependent drop site. This method is invoked when all the following conditions are true:
     * The cursor's hotspot has moved, but still intersects the operable part of the drop site associated with the previous dragEnter() invocation.
     * The drop site is still active.
     * The drop site accepts the drag.
     * @param event DropTargetDragEvent
     */
    public void dragOver(DropTargetDragEvent event) {
        //System.out.println("droptarget : dragOver"+getName());
    }
    
    /** Called when the drag operation has terminated with a drop on the operable part of the drop site for the DropTarget registered with this listener.
     * This method is responsible for undertaking the transfer of the data associated with the gesture. The DropTargetDropEvent provides a means to obtain a Transferable object that represents the data object(s) to be transfered.
     *
     * From this method, the DropTargetListener shall accept or reject the drop via the acceptDrop(int dropAction) or rejectDrop() methods of the DropTargetDropEvent parameter.
     *
     * Subsequent to acceptDrop(), but not before, DropTargetDropEvent's getTransferable() method may be invoked, and data transfer may be performed via the returned Transferable's getTransferData() method.
     *
     * At the completion of a drop, an implementation of this method is required to signal the success/failure of the drop by passing an appropriate boolean to the DropTargetDropEvent's dropComplete(boolean success) method.
     *
     * Note: The data transfer should be completed before the call to the DropTargetDropEvent's dropComplete(boolean success) method. After that, a call to the getTransferData() method of the Transferable returned by DropTargetDropEvent.getTransferable() is guaranteed to succeed only if the data transfer is local; that is, only if DropTargetDropEvent.isLocalTransfer() returns true. Otherwise, the behavior of the call is implementation-dependent.
     * @param event DropTargetDropEvent
     */
    public void drop(DropTargetDropEvent event) {
        //System.out.println("droptarget : drop"+getName());
        //System.out.println("isDragImageSupported() : "+dragSource.isDragImageSupported());
        
        if(!addDataOnDrop){
            event.getDropTargetContext().dropComplete(true);
            return ;
        }
        
        try {
            Transferable transferable = (Transferable)event.getTransferable();
            
            if(transferable == null) return ;
            
            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                Vector multipleRowData = (Vector)transferable.getTransferData(DataFlavor.stringFlavor);
                int transferredRowCount = multipleRowData.size();
                Vector rowData;
                ImageIcon imageIcon;
                Image image;
                
                if(transferredRowCount < 1 ) return ;
                
                //Check for similar Class Types.
                rowData = (Vector)multipleRowData.get(0);
                if(rowData == null || !isDataColumnTypesSame(rowData)) {
                    return ;
                }
                
                for(int row = 0; row < transferredRowCount; row++) {
                    rowData = (Vector)multipleRowData.get(row);
                    //Checks for ImageIcon
                    for(int colCount = 0; colCount < rowData.size(); colCount++) {
                        if(rowData.get(colCount).getClass().equals(ImageIcon.class)) {
                            imageIcon = (ImageIcon)rowData.get(colCount);
                            image = imageIcon.getImage();
                            if(image == null) continue;
                            rowData.set(colCount, new ImageIcon(image));
                        }
                    }//End for
                    
                    ((DefaultTableModel)getModel()).addRow(rowData);
                }
                
                ((DefaultTableModel)getModel()).fireTableDataChanged();
                
                event.getDropTargetContext().dropComplete(true);
                //event.dropComplete(true);
                //event.rejectDrop();
            }
            else
                event.rejectDrop();
        }
        catch(IOException exception) {
            exception.printStackTrace();
            System.err.println( "Exception" + exception.getMessage());
            event.rejectDrop();
        }
        catch(UnsupportedFlavorException ufException ) {
            ufException.printStackTrace();
            System.err.println( "Exception" + ufException.getMessage());
            event.rejectDrop();
        }
    }
    
    /** checks whether all Data Column Types are matching with this Data Column Types.
     * @param rowData Vector RowData
     * @return true if All Data Column Types are matching with this Data Column Types.
     * else return false.
     */
    private boolean isDataColumnTypesSame(Vector rowData) {
        //Check both column types are same.
        int dataColCount = rowData.size();
        
        //Table is Empty.
        if(getRowCount() == 0) return true;
        
        Vector thisRowData= (Vector)((DefaultTableModel)getModel()).getDataVector().get(0);
        //Table is Empty.
        //if(thisRowData != null) return true;
        
        int thisDataColCount = thisRowData.size();
        if(!(dataColCount == thisDataColCount)) return false;
        
        for(int col = 0; col < dataColCount; col++) {
            //System.out.println(rowData.get(col).getClass());
            if(! (rowData.get(col).getClass().equals(thisRowData.get(col).getClass()))) return false;
        }
        return true;
    }
    
    /** Called if the user has modified the current drop gesture.
     * @param e DropTargetDragEvent
     */
    public void dropActionChanged(DropTargetDragEvent e) {
        //System.out.println("dropActionChanged"+getName());
    }
    
    /** A DragGestureRecognizer has detected a platform-dependent drag initiating gesture and is notifying this listener in order for it to initiate the action for the user.
     * @param event DragGestureEvent.
     */
    public void dragGestureRecognized(DragGestureEvent event) {
        if(!dragEnabled) return ;
        
        Vector draggedRowData;
        /*
        int nDragRow;
        nDragRow = getSelectedRow();
        if(nDragRow == NONE_SELECTED) return ;
         
        draggedRowData = (Vector)((DefaultTableModel)getModel()).getDataVector().get(nDragRow);
         */
        
        dropTarget.setActive(false);
        
        //Enhance ment for Multiple Selction Drag and Drop.
        
        draggedRowData = new Vector();
        int selectedRows[] = getSelectedRows();
        for(int rowCount = 0; rowCount < selectedRows.length; rowCount++) {
            draggedRowData.add(((DefaultTableModel)getModel()).getDataVector().get(selectedRows[rowCount]));
        }
        
        TransferableVector tv = new TransferableVector(draggedRowData);
        
        if (draggedRowData.size() > 0 ) {
            //System.out.println("beginning drag:");
            dragSource.startDrag(event,DragSource.DefaultMoveDrop,tv,this);
        }
        else{
            //System.out.println("nothing was selected");
        }
    }
    
    /** This method is invoked to signify that the Drag and Drop operation is complete. The getDropSuccess() method of the DragSourceDropEvent can be used to determine the termination state. The getDropAction() method returns the operation that the drop site selected to apply to the Drop operation. Once this method is complete, the current DragSourceContext and associated resources become invalid.
     * @param event DragSourceDropEvent.
     */
    public void dragDropEnd(DragSourceDropEvent event) {
        dropTarget.setActive(dropEnabled);
        
        if(!event.getDropSuccess()) {
            return ;
        }
        
        if(!getDeleteDataOnDrop()) {
            return ;
        }
        
        int selectedRows[] = getSelectedRows();
        for(int row = 0; row < selectedRows.length; row++) {
            ((DefaultTableModel)getModel()).removeRow(selectedRows[row] - row);
        }
        ((DefaultTableModel)getModel()).fireTableDataChanged();
        
        //System.out.println("dragsource : dragDropEnd"+getName());
    }
    
    /** Called as the cursor's hotspot enters a platform-dependent drop site. This method is invoked when all the following conditions are true:
     * The cursor's hotspot enters the operable part of a platform- dependent drop site.
     * The drop site is active.
     * The drop site accepts the drag.
     * @param event DragSourceDragEvent.
     */
    public void dragEnter(DragSourceDragEvent event) {
        //System.out.println("dragsource : dragEnter"+getName());
    }
    
    /** Called as the cursor's hotspot exits a platform-dependent drop site. This method is invoked when any of the following conditions are true:
     * The cursor's hotspot no longer intersects the operable part of the drop site associated with the previous dragEnter() invocation.
     * OR
     * The drop site associated with the previous dragEnter() invocation is no longer active.
     * OR
     * The current drop site has rejected the drag.
     * @param event DragSourceEvent.
     */
    public void dragExit(DragSourceEvent event) {
        //System.out.println("dragsource : dragExit"+getName());
    }
    
    /** Called as the cursor's hotspot moves over a platform-dependent drop site. This method is invoked when all the following conditions are true:
     * The cursor's hotspot has moved, but still intersects the operable part of the drop site associated with the previous dragEnter() invocation.
     * The drop site is still active.
     * The drop site accepts the drag.
     * @param event DragSourceDragEvent.
     */
    public void dragOver(DragSourceDragEvent event) {
        //System.out.println("dragsource : dragOver"+getName());
    }
    
    /** Called when the user has modified the drop gesture. This method is invoked when the state of the input device(s) that the user is interacting with changes. Such devices are typically the mouse buttons or keyboard modifiers that the user is interacting with.
     * @param event DragSourceDragEvent
     */
    public void dropActionChanged(DragSourceDragEvent event) {
        //System.out.println("dragsource : dropActionChanged"+getName());
    }
    
    /** For Testing Purpose Only.   sharath (21/7/2003)
     */
    public static void main(String s[]) {
        CoeusDnDTable ct = new CoeusDnDTable();
        ct.test();
    }
    
    /** For Testing Purpose Only.
     */
    private void test() {
        javax.swing.ImageIcon ii = new javax.swing.ImageIcon(
        getClass().getClassLoader().getResource(edu.mit.coeus.utils.CoeusGuiConstants.PROP_ACTIVE_ROLE_ICON));
        
        java.util.Vector row1 = new java.util.Vector();
        row1.add("One");
        row1.add(new Integer(1));
        row1.add(ii);
        
        java.util.Vector row2 = new java.util.Vector();
        row2.add("Two");
        row2.add(new Integer(2));
        row2.add(ii);
        
        java.util.Vector data1 = new java.util.Vector();
        data1.add(row1);
        data1.add(row2);
        
        java.util.Vector data2 = new java.util.Vector();
        
        java.util.Vector row3 = new java.util.Vector();
        row3.add("Three");
        row3.add(new Integer(3));
        row3.add(ii);
        
        data2.add(row1);
        data2.add(row2);
        data2.add(row3);
        
        java.util.Vector colNames = new java.util.Vector();
        colNames.add("Col Name1");
        colNames.add("Col Name2");
        
        String colName[]={"first","second","Image"};
        Class colTypes[] = {String.class, Integer.class,javax.swing.ImageIcon.class};
        
        javax.swing.JFrame jf= new javax.swing.JFrame();
        CoeusDnDTable firstTab = new CoeusDnDTable();//(data1,colNames);
        firstTab.setDragEnabled(true);
        CoeusTableModel ctm1 = new CoeusTableModel(data1,colName,colTypes);
        firstTab.setModel(ctm1);
        
        firstTab.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        firstTab.setName("First");
        javax.swing.JScrollPane one = new javax.swing.JScrollPane(firstTab);
        one.setSize(200,100);
        
        CoeusDnDTable secondTab = new CoeusDnDTable();//(data2,colNames);
        secondTab.setDeleteDataOnDrop(false);
        //secondTab.setDropEnabled(false);
        secondTab.setName("Second");
        
        CoeusTableModel ctm2 = new CoeusTableModel(data2,colName,colTypes);
        secondTab.setModel(ctm2);
        secondTab.setColumnVisibility(0,false);
        secondTab.setColumnVisibility(0,true);
        
        javax.swing.JScrollPane two = new javax.swing.JScrollPane(secondTab);
        two.setSize(200,100);
        jf.getContentPane().setLayout(new java.awt.FlowLayout());
        jf.getContentPane().add(one,java.awt.BorderLayout.NORTH);
        jf.getContentPane().add(two,java.awt.BorderLayout.SOUTH);
        jf.setSize(400,300);
        jf.setVisible(true);
    }
    
    
    //Class Variables
    private static final int NONE_SELECTED = -1;
}
