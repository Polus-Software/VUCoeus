
package edu.mit.coeus.codetable.parser;
     

        import java.io.*;
        import java.util.*;
	import org.xml.sax.*;
	import org.xml.sax.helpers.DefaultHandler;
        import edu.mit.coeus.codetable.bean.*;

          /** CodeTableParser is the SAXhandler for parsing the CodeTables XML file.
         * <br>The parser object is created in the calling class, and a reference to the XML
         * <br>file, as well as a reference to this handler class,  is passed to the parser. 
         * <br>The XML file is read sequentially, and the parser notifies this handler when events occur.  
         * <br>For example, the  startElement() method is called when an opening tag 
         * <br>is encountered, and the endElement() method is called when the tag is closed.
         * <br>Attributes are provided with the startElement() call.
         * <br>The CodeTableParser class overrides two events - the startElement and endElement methods.        
         */    
       
	public class CodeTableParser extends DefaultHandler{
        
        private int  tableCount = -1;
        private int  storedProcCount = -1;
        private int  paramCount = -1;
        private int  columnCount = -1;
        private int  dependencyCount = -1;
        private int  groupCount = -1;
        private  HashMap tableHash = new HashMap(); 
        private  HashMap tableColumnsHash = new HashMap();
        private  HashMap storedProcHash  = new HashMap();
        private  Vector paramVector = new Vector() ;
        private HashMap hashDependency = new HashMap() ; 
        private HashMap hashGroup = new HashMap() ;
        private AllCodeTablesBean allCodeTables;
        private TableStructureBean currentTableStructBean;
        private StoredProcedureBean storedProcBean;
        private StoredProcedureBean currentStoredProcBean;
     
        static final private String codeTableMaintTag = "CodeTableMaintenance";
        static final private String codeTableTag = "CodeTable";
        static final private String codeTableColTag = "CodeTableColumn";
        static final private String storedProcTag = "StoredProcedure";
        static final private String paramTag = "PARAM";
        static final private String dependTag = "Dependency" ;
        static final private String groupTag = "Group" ;
        //Added for COEUSQA-2320 : Show in Lite for Special Review in Code table - Start
        private static final String SPACE_STRING = " ";
        //COEUSQA-2320 : End
        
	public CodeTableParser() {
        }
        
        
        /** The startElement method is invoked when the SAXParser reads the start tag of an
         * element.
         * <br>An element in an XML document might be associated with a namespace, which provides
         * <br>a context that helps applications interpret the element. The SAXParser used by this
         * <br>application is not namespace aware.
         *
         * @param namespaceURI  "namespace name" (always a URI) from namespace declarations - Null in this application.
         * @param sName local name from the XML text (removing any namespace prefix). Null in this application.
         * @param qName simple name of element -  exactly as found in the XML text
         * @param attrs  Attributes object containing attributes of an element
         */        
        public void startElement(String namespaceURI, 
           String sName, 
           String qName, 
           Attributes attrs) 
         {        
                                                                                                            
         
            if (qName.equals(codeTableMaintTag)) {
                System.out.println(" **************** Creating a new instance of Allcodetable bean  ************* ") ;
                allCodeTables = new AllCodeTablesBean();
                
                
            }else if (qName.equals(groupTag))
            {
                groupCount++ ;
                hashGroup.put(new Integer(groupCount), attrs.getValue("name")) ;
            } 
            else if (qName.equals(codeTableTag)){
                //  tableColumnsHash.clear(); - this resulted in overwriting columnbean
                tableColumnsHash = new HashMap();
                storedProcHash = new HashMap();
                hashDependency = new HashMap() ;
                
                columnCount = -1;
                dependencyCount = -1 ;
                 
                //create tableStructureBean and set simple fields 
                TableStructureBean tableStructureBean = new TableStructureBean();
                
                // for now u do this if check as xml file doesnt habe group names
                // but thi swill be a required attribute
                if (attrs.getValue("groupName")!= null) 
                {
                    tableStructureBean.setGroupName(attrs.getValue("groupName"));
                }
                tableStructureBean.setActualName(attrs.getValue("actualName"));
                tableStructureBean.setDescription(attrs.getValue("description"));
                tableStructureBean.setDisplayName(attrs.getValue("displayName"));
                
                
                // special case tables will have a formComponent name which implements the functionality.
                // formComponent is basically the form to be shown when special case table is accessed
                // instead of the normal ones.
                if (attrs.getValue("formComponent")!= null) 
                {
                    tableStructureBean.SetFormComponent(attrs.getValue("formComponent").toString()) ;
                }
                else //othertables
                {    
                        tableStructureBean.SetFormComponent(null) ; // set null for other tables
                }
                
                        //tableStructureBean.setPrimaryKeyIndex(Integer.parseInt(())) ;
                        StringTokenizer strPKey = new StringTokenizer(attrs.getValue("primaryKeyIndex").toString()) ;
                        Vector vecPKey = new Vector() ;
                        int pdx = 0 ;
                        while(strPKey.hasMoreTokens())
                        {
                            vecPKey.add(pdx, strPKey.nextToken()) ;
                            pdx++ ;
                        }    

                        tableStructureBean.setPrimaryKeyIndex(vecPKey) ;
                        tableStructureBean.setUserIndex(Integer.parseInt((attrs.getValue("userIndex").toString()))) ;

                        //tableStructureBean.setDuplicateIndex(Integer.parseInt((attrs.getValue("duplicateIndex").toString()))) ;
                        StringTokenizer strDKey = new StringTokenizer(attrs.getValue("duplicateIndex").toString()) ;
                        Vector vecDKey = new Vector() ;
                        int ddx = 0 ;
                        while(strDKey.hasMoreTokens())
                        {
                            vecDKey.add(ddx, strDKey.nextToken()) ;
                            ddx++ ;
                        }

                        tableStructureBean.setDuplicateIndex(vecDKey) ;

                        if (attrs.getValue("idAutoGenerated").equals("Y")){
                            tableStructureBean.setIdAutoGenerated(true);
                        } else {tableStructureBean.setIdAutoGenerated(false);}
                        Integer intCols = new Integer(attrs.getValue("numColumns"));
                        tableStructureBean.setNumColumns(intCols.intValue());
                        
                tableCount++;
                //put this tableStructureBean in the tableHash
                tableHash.put( new Integer(tableCount),tableStructureBean); 
               
            } else if (qName.equals(codeTableColTag)){
                //create columnBean and set simple fields 
                ColumnBean columnBean = new ColumnBean();
                columnBean.setColumnName(attrs.getValue("name"));
                columnBean.setDataType(attrs.getValue("type"));
                columnBean.setDisplayName(attrs.getValue("displayName"));
                columnBean.setDisplaySize(new Integer(attrs.getValue("length")).intValue());
                //Added with COEUSQA-2667:User interface for setting up question details for procedure categories
                columnBean.setMaxLength(new Integer(attrs.getValue("maxlength")==null?"0":attrs.getValue("maxlength")).intValue());
                //COEUSQA-2667:End
                columnBean.setColIdentifier(attrs.getValue("identifier"));
                columnBean.setQualifier(attrs.getValue("qualifier"));
                
                if (attrs.getValue("options") != null)
                {
                    //Case Id #3121 - start
                    //Modified to display the option values in order
//                    HashMap hashOptions = new HashMap() ;
                    LinkedHashMap hashOptions = new LinkedHashMap();
                    //Case Id #3121 - end
                    StringTokenizer strOptions = new StringTokenizer(attrs.getValue("options").toString()) ;
                    while(strOptions.hasMoreElements())
                    {
                        StringTokenizer strTemp = new StringTokenizer( strOptions.nextToken(), ",") ;
                        //Modified for COEUSQA-2320 : Show in Lite for Special Review in Code table - Start
//                        hashOptions.put(strTemp.nextToken(),  strTemp.nextToken()) ; // first token is key and second token is value
                        String key = strTemp.nextToken();
                        String value = strTemp.nextToken();
                        value = value.replaceAll("-",SPACE_STRING);
                        hashOptions.put(key,value) ;
                        //COEUSQA-2320 : End
                    }    
                    columnBean.setOptions(hashOptions) ;
                }    
                     
                //get new column's default value from xml file
                if (attrs.getValue("defaultValue") != null)
                {
//                    HashMap hashDefault = new HashMap() ;
//                    StringTokenizer strDefault = new StringTokenizer(attrs.getValue("default").toString()) ;
//                    while(strDefault.hasMoreElements())
//                    {
//                        StringTokenizer strTemp = new StringTokenizer( strDefault.nextToken(), ",") ;
//                        System.out.println("Token recvd " + strTemp) ;
//                        hashDefault.put(strTemp.nextToken(),  strTemp.nextToken()) ; // first token is key and second token is value
//                    }    
//                    columnBean.setDefault(hashDefault) ;
                    columnBean.setDefaultValue(attrs.getValue("defaultValue"));
                }    
                if (attrs.getValue("canbenull").equals("Y"))
                {
                    columnBean.setColumnCanBeNull(true);
                }
                else 
                {
                    columnBean.setColumnCanBeNull(false);
                }
                
                if (attrs.getValue("visible").equals("Y"))
                {
                    columnBean.setColumnVisible(true);
                }
                else 
                {
                    columnBean.setColumnVisible(false);
                }
                
               
                 if (attrs.getValue("editable").equals("Y"))
                {
                    columnBean.setColumnEditable(true);
                }
                else 
                {
                    columnBean.setColumnEditable(false);
                }
               
                
                columnCount++;
               
                //put columnbean in hashmap of columns              
               // tableColumnsHash.put( attrs.getValue("name"),columnBean);
                tableColumnsHash.put(new Integer(columnCount),columnBean);
                
                // get the current codtTableStructureBean and add this hashmap of columnBeans
                currentTableStructBean = (TableStructureBean) tableHash.get( new Integer(tableCount));                          
                currentTableStructBean.setHashTableColumns(tableColumnsHash);
                
                //put codetable bean back in tablehash - is this necessary? I don't think so
               //  tableHash.put(new Integer(tableCount),currentTableBean);
                    
            } else if (qName.equals(storedProcTag)){
                
                
                //create storedprocedureBean and set simple fields
                StoredProcedureBean storedProcBean = new StoredProcedureBean();
                storedProcBean.setName(attrs.getValue("name" ));
                storedProcBean.setOperation(attrs.getValue("operation"));
                storedProcCount++;
                //add storedProcBean to the storedProcHash
                storedProcHash.put(new Integer(storedProcCount), storedProcBean);
             
            } else if (qName.equals(paramTag)){
                //create parameterBean and  set simple fields
                ParameterBean parameterBean = new ParameterBean();
                parameterBean.setDataType(attrs.getValue("datatype"));
                parameterBean.setName(attrs.getValue("name"));
                paramCount++;
                 
                //add the parameterBean into the parameter vector       
              
                paramVector.add(paramCount,parameterBean);  
                
                //get the current stored proc bean and put parameter vector in it
                currentStoredProcBean = (StoredProcedureBean) storedProcHash.get(new Integer(storedProcCount));
               
                currentStoredProcBean.setVectParameters(paramVector);
            }else if (qName.equals(dependTag)){
                dependencyCount++ ;
                HashMap hashIndDependency = new HashMap() ;
                hashIndDependency.put("Table", attrs.getValue("tableName")) ;
                hashIndDependency.put("Column", attrs.getValue("columnName")) ;
                hashDependency.put(new Integer(dependencyCount), hashIndDependency) ;
                
                // get the current codtTableStructureBean and add this hashmap of dependency table to it
                currentTableStructBean = (TableStructureBean) tableHash.get( new Integer(tableCount));                          
                currentTableStructBean.setHashTableDependency(hashDependency);
            }//end else if dependency
            
        }
        
         /** The endElement method is invoked when the SAXParser reads the end tag of an
         * element.
         * @param namespaceURI
         * @param sName
         * @param qName
         */        
        public void endElement(String namespaceURI, 
                String sName, 
                String qName)
        {  
            if (qName.equals(codeTableMaintTag) ) {
                 //put the tablehash in the all table bean
                allCodeTables.setHashAllCodeTableStructure(tableHash);
                allCodeTables.setHashAllGroupNameList(hashGroup);
               
//                if (allCodeTables == null)
//                {
//                    System.out.println(" **************** Allcodetable bean is null ************* ") ;
//                }
//                else
//                {
//                    System.out.println(" **************** Allcodetable bean is not null ************* ") ;
//                }    
                
            }
        
            else if (qName.equals(codeTableTag)){
                //get appropriate TableStructureBean from tableHash
               TableStructureBean tableStructureBean = (TableStructureBean) tableHash.get( new Integer(tableCount));
               
               //add the storedProc hashmap to tableStructureBean                
               tableStructureBean.setHashStoredProceduresForThisTable(storedProcHash);
                
                storedProcCount = -1;
                
            }     
            else if (qName.equals(storedProcTag)) {
                paramCount = -1;
              //  paramVector.clear(); - this doesn't work - because the variable paramVector is pointing to
                // the memory location inside the storedprocBean.and so will be wiped out...
                paramVector = new Vector();
            }
           
         }

              
	public AllCodeTablesBean resultbean() {
            //this method is for debug
            return allCodeTables;
        }


   

}