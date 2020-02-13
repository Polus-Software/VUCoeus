/*
 * XX.java
 *
 * Created on March 17, 2003, 12:24 PM
 */

package edu.mit.coeus.utils;
import java.util.Vector;

public class OtherLookupBean {
        
        private String windowTitle;
        private Vector lookupDataValues;
        private ComboBoxBean cbBean;
        private String[] columnNames;
        private int selectedInd;
        
        public OtherLookupBean() {
        }
        
        public OtherLookupBean(String windowTitle, Vector lookupDataValues, String[] columnNames){
            this.windowTitle = windowTitle;
            this.lookupDataValues = lookupDataValues;
            this.columnNames = columnNames;
        }
        
        public void setSelectedInd(int index){
            this.selectedInd = index;
        }
        
        public int getSelectedInd(){
            return this.selectedInd;
        }
        
        public void setLookupDataValues(Vector lookupDataValues){
            this.lookupDataValues = lookupDataValues;
        }
        
        public Vector getLookupDataValues(){
            return this.lookupDataValues;
        }
        
        public void setWindowTitle(String windowTitle){
            this.windowTitle = windowTitle;
        }
        
        public String getWindowTitle(){
            return this.windowTitle;
        }
        
        public void setColumnNames(String[] columnNames){
            this.columnNames = columnNames;
        }
        
        public String[] getColumnNames(){
            return this.columnNames;
        }

        public void setCbBean(ComboBoxBean cbBean){
            this.cbBean = cbBean;
        }
        
        public ComboBoxBean getCbBean(){
            return this.cbBean;
        }
    }