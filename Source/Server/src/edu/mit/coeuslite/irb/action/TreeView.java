/*
 * Treeview.java
 *
 * Created on April 6, 2005, 12:41 PM
 */

package edu.mit.coeuslite.irb.action;

import java.util.Vector;
import java.lang.StringBuffer;
/**
 *
 * @author  shijiv
 */

public class TreeView{
	private String folder="/coeusliteimages/";
	private String color="navy";	
	public NodeList nodes;
	private String target="";
	public int length=0;
	private StringBuffer buf;
        private boolean flag=true;
        private String folderClass="folder";
        boolean isClicked=false;
        boolean isOpen=false;
        String par=null;
        private String contextUrl="";
	
	public TreeView(){
		nodes=new NodeList();
		buf  = new StringBuffer();
	}
	
	public void setImagesUrl(String url){
		this.folder = url;	
	}
	
	public void add(Node node){
		nodes.add(node);
		length++;
	}
	public void add(String text){
		add(new Node(text));
	}
	public Node createNode(String text){
		return (new Node(text));
	}
	public Node createNode(String text,String href,String toolTip){
		return (new Node(text,href,toolTip));
	}
	
	private void print(String text){
		buf.append(text);
	}
        
	public void setContextUrl(String url){
		this.contextUrl = url;	
	}
        
        public int getParentNodeIndex(Node node,int level,int itemIndex) {
            int index=0;
            int vec=0;
            if(level==1) {
                vec=nodes.size();
                for(int i=0;i<vec;i++) {
                  Node n=(Node)nodes.item(0);
                   if(n.text.equals(node.text)) {
                    index=i; 
                    break;
                   }
                  }
                }else if(level==2) {
                 vec=nodes.item(0).childNodes.size();
                 for(int i=0;i<vec;i++) {
                  Node n=(Node)nodes.item(0).childNodes.item(i);
                   if(n.text.equals(node.text)) {
                       index=i; 
                       break;
                     }
                  }
                }else if(level==3) {
                    vec=nodes.item(0).childNodes.item(itemIndex).childNodes.size();
                     for(int i=0;i<vec;i++) {
                      Node n=(Node)nodes.item(0).childNodes.item(itemIndex).childNodes.item(i);
                        if(n.text.equals(node.text)) {
                         index=i; 
                         break;
                      }
                     }
                }
            return index;
        }
        
        private String getCode(String text) {
            String str=text.substring(0,text.indexOf("-")-1);
            return str;
        }
        
        private String getDescription(String text) {
            String str=text.substring(text.indexOf("-")+1,text.length());
            return str;
        }
	
	public String getTree(boolean expand){
           // String expansion = value;
		//print("<script>function toggle(id,p){var myChild = document.getElementById(id);if(myChild.style.display!='block'){myChild.style.display='block';document.getElementById(p).className='folderOpen';}else{myChild.style.display='none';document.getElementById(p).className='folder';}}</script>");
                //print("<script>var i=0;var isFirst=false;function toggle(id,p){var myChild = document.getElementById(id);alert(i);alert(id+','+p);if(i==0){i+=1;if(id=='N0_0'){if(!isFirst){myChild.style.display='block';isFirst=true;i=0;}else{i=0;}}if(myChild.style.display!='block'){myChild.style.display='block';document.getElementById(p).className='folderOpen';alert('folderopen');}else{myChild.style.display='none';document.getElementById(p).className='folder';alert('folder');}}else{if(id=='N0_0'){i=0;}return;}} function isAdd(){i+=1;}</script>");
            //Code added for Case#3522 - Default list of areas of research only expand to first level - starts
            //For tree to expand
            if(expand){
                // Code Modified for 4.3 enhancement
                //COEUSQA:3209 - CoeusLite IACUC Areas of Research - Start
                // print("<script>var i=0;var isFirst=false;function toggle(id,p){var myChild = document.getElementById(id);if(i==0){i+=1;if(id=='N0_0'){if(!isFirst){myChild.style.display='block';isFirst=true;i=0;}else{i=0;}}if(myChild.style.display!='none'){myChild.style.display='none';document.getElementById(p).className='folder';}else{myChild.style.display='block';document.getElementById(p).className='folderOpen';}}else{if(id=='N0_0'){i=0;}return;}}  </script>");
                print("<script>var i=0;var isFirst=false;function toggle(id,p){var myChild = document.getElementById(id);if(i==0){i+=1;if(id=='N0_0'){if(!isFirst){myChild.style.display='block';isFirst=true;i=0;}else{i=0;}}if(myChild.style.display!='none'){myChild.style.display='none';document.getElementById(p).className='folder';}else{myChild.style.display='block';document.getElementById(p).className='folderOpen';} if(navigator.appName != 'Microsoft Internet Explorer'){i=0;}  }else{if(id=='N0_0'){i=0;}return;}}  </script>");
                //COEUSQA:3209 - End
                print("<script>function set_Data(code,desc){if(window.opener && !window.opener.closed){var_form = window.opener.document.areaForm;var_form.researchAreaCode.value=code;var_form.researchAreaDescription.value=desc;opener.save_Data();window.close();}}</script>");
                print("<style>ul.tree{display:none;margin-left:17px;}ul.tr{display:block;margin-left:17px;}li.folder{list-style-image: url("+ folder +"/plus.gif);}li.folderOpen{list-style-image: url("+ folder +"/minus.gif);}li.file{list-style-image: url("+ folder +"/dot.gif);}a.treeview{color:"+ color +";font-family:verdana;font-size:8pt;}a.treeview:link {text-decoration:none;font-size:8pt}a.treeview:visited{text-decoration:none;font-size:8pt}a.treeview:hover {text-decoration:underline;font-size:8pt}</style>");
              // print("<style>ul.tree{display:none;margin-left:17px;} .folder{list-style-image: url("+ folder +"/plus.gif);} .folderOpen{list-style-image: url("+ folder +"/minus.gif);} .file{list-style-image: url("+ folder +"/dot.gif);}a.treeview{color:"+ color +";font-family:verdana;font-size:8pt;}a.treeview:link {text-decoration:none;}a.treeview:visited{text-decoration:none;}a.treeview:hover {text-decoration:underline;}</style>");
                //For tree to collapse
            } else {
                //COEUSQA:3209 - CoeusLite IACUC Areas of Research - Start
                //print("<script>var i=0;var isFirst=false;function toggle(id,p){var myChild = document.getElementById(id);if(i==0){i+=1;if(id=='N0_0'){if(!isFirst){myChild.style.display='block';isFirst=true;i=0;}else{i=0;}}if(myChild.style.display!='block'){myChild.style.display='block';document.getElementById(p).className='folderOpen';}else{myChild.style.display='none';document.getElementById(p).className='folder';}}else{if(id=='N0_0'){i=0;}return;}}  </script>");
                print("<script>var i=0;var isFirst=false;function toggle(id,p){var myChild = document.getElementById(id);if(i==0){i+=1;if(id=='N0_0'){if(!isFirst){myChild.style.display='block';isFirst=true;i=0;}else{i=0;}}if(myChild.style.display!='none'){myChild.style.display='none';document.getElementById(p).className='folder';}else{myChild.style.display='block';document.getElementById(p).className='folderOpen';} if(navigator.appName != 'Microsoft Internet Explorer'){i=0;}  }else{if(id=='N0_0'){i=0;}return;}}  </script>");
                //COEUSQA:3209 - End
		print("<script>function set_Data(code,desc){if(window.opener && !window.opener.closed){var_form = window.opener.document.areaForm;var_form.researchAreaCode.value=code;var_form.researchAreaDescription.value=desc;opener.save_Data();window.close();}}</script>");
               print("<style>ul.tree{display:none;margin-left:17px;}ul.tr{display:block;margin-left:17px;}li.folder{list-style-image: url("+ folder +"/plus.gif);}li.folderOpen{list-style-image: url("+ folder +"/minus.gif);}li.file{list-style-image: url("+ folder +"/dot.gif);}a.treeview{color:"+ color +";font-family:verdana;font-size:8pt;}a.treeview:link {text-decoration:none;font-size:8pt}a.treeview:visited{text-decoration:none;font-size:8pt}a.treeview:hover {text-decoration:underline;font-size:8pt}</style>");                
            }
            //Code added for Case#3522 - Default list of areas of research only expand to first level - ends
                loopThru(nodes,"0", expand);
                isClicked=true;
		return buf.toString();
	}
        
        
	
	private void loopThru(NodeList nodeList, String parent, boolean expand){
		boolean hasChild;
		String style;
		
		if(parent!="0"){
                    if(flag) {
                       print("<ul id=\"N" + parent + "\">");
                      // print("\"javascript:toggle('N" + parent + "_" + 0+ "','P" + parent + 0+ "')\"");
                       //Code commented for 4.3 enhancement
//                       flag=false;
                       //Code added for Case#3522 - Default list of areas of research only expand to first level - starts
                       if(!expand){
                           flag=false;
                       }
                       //Code added for Case#3522 - Default list of areas of research only expand to first level - ends
                     }
                    else if(!flag){
			print("<ul class=tree id=\"N" + parent + "\">");
                    }
		}else{
                    print("<ul id=\"N" + parent + "\">");
                    //COEUSQA:3209 - CoeusLite IACUC Areas of Research - Start
                    if(!expand){
                        flag=false;
                        isOpen=true;
                    }
                    //COEUSQA:3209 - End
		}
          
                
                for (int i=0;i<nodeList.length;i++){
                   
			Node node = nodeList.item(i);	
				
			if(node.childNodes.length>0){ 
				hasChild=true;	
			}else{
				hasChild=false;
			}
			
			if(node.imageUrl==""){
				style="";
			}else{
				style="style='list-style-image: url("+ node.imageUrl +");'";
			}				
			if(hasChild){
                            
                               //print("<li>");
                                //print("<a href=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\"><li class=folder id='P" + parent + i + "'></a><a class=treeview href=\"\">" + node.text + "</a>");
                                // print("<li class=folder><a href=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\"  id='P" + parent + i + "'>" + style + "</a><a class=treeview href=\"\">" + node.text + "</a>");
				//print("<li "+ style +" class=folder id='P" + parent + i + "'><a class=treeview href=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\">" + node.text + "</a>");
                                //print("<a  "+ style +"href=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\"  id='P" + parent + i + "' class=folder> </a><a class=treeview href=\"\">" + node.text + "</a>");
                               //String s=node.text;
                               if(!isOpen) {
                                  // print("<li "+ style +" class=folderOpen id='P" + parent + i + "' onclick=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\" ><a class=treeview href=\""+contextUrl+"/areaOfResearch.do\">" + node.text + "</a>");
                                   //COEUSQA:3209 - CoeusLite IACUC Areas of Research - Start
                                   //print("<li "+ style +" class=folderOpen id='P" + parent + i + "' onclick=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\" ></li><a class=treeview href=\"javascript:set_Data('"+getCode(node.text)+"','"+getDescription(node.text)+"')\">" + node.text + "</a>");
                                   print("<li "+ style +" class=folderOpen id='P" + parent + i + "' onclick=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\" ><a class=treeview href=\"javascript:set_Data('"+getCode(node.text)+"','"+getDescription(node.text)+"')\">" + node.text + "</a></li>");
                                   //COEUSQA:3209 - End
                                   // print("<li "+ style +" class=folderOpen id='P" + parent + i + "' onclick=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\" ><a class=treeview href=\"javascript:set_Data('"+node.code+"','"+node.desc+"')\">" + node.text + "</a>");
                                   // print("<li "+ style +" class=folderOpen id='P" + parent + i + "' onclick=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\" ><a class=treeview href=\"\">" + node.text + "</a>");
                                   //Code Commented for 4.3 enhancement
//                                   isOpen=true;
                                   //Code added for Case#3522 - Default list of areas of research only expand to first level - starts
                                   if(!expand){
                                       isOpen=true;
                                   }
                                   //Code added for Case#3522 - Default list of areas of research only expand to first level - ends
                                }else if(isOpen) {
                                   //COEUSQA:3209 - CoeusLite IACUC Areas of Research - Start
                                   //print("<li "+ style +" class=folder id='P" + parent + i + "' onclick=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\" ></li><a class=treeview href=\"javascript:set_Data('"+getCode(node.text)+"','"+getDescription(node.text)+"')\">" + node.text + "</a>");
                                   print("<li "+ style +" class=folder id='P" + parent + i + "' onclick=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\" ><a class=treeview href=\"javascript:set_Data('"+getCode(node.text)+"','"+getDescription(node.text)+"')\">" + node.text + "</a></li>");
                                   //COEUSQA:3209 - End
                                  //  print("<li "+ style +" class=folder id='P" + parent + i + "' onclick=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\" ><a class=treeview href=\"javascript:set_Data('"+node.code+"','"+node.desc+"')\">" + node.text + "</a>");
                                   // print("<li "+ style +" class=folder id='P" + parent + i + "' onclick=\"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\" ><a class=treeview href=\"\">" + node.text + "</a>");
                            //  print("<li class=folder id='P" + parent + i + "'><a class=folder href= \"javascript:toggle('N" + parent + "_" + i + "','P" + parent + i + "')\">"+ style +"</a></li> <a class=treeview href=\"\">" + node.text + "</a>");
                                
                               }
			}else{
				if(node.target==""){
					node.target=target;
				}
                                 //COEUSQA:3209 - CoeusLite IACUC Areas of Research - Start
                                //print("<li "+ style +" class=file></li><a class=treeview href=\"javascript:set_Data('"+getCode(node.text)+"','"+getDescription(node.text)+"')\" target='" + node.target + "'  title=\"" + node.toolTip + "\" >" + node.text + "</a>");
                                print("<li "+ style +" class=file><a class=treeview href=\"javascript:set_Data('"+getCode(node.text)+"','"+getDescription(node.text)+"')\" target='" + node.target + "'  title=\"" + node.toolTip + "\" >" + node.text + "</a></li>");
                                //COEUSQA:3209 - End
				//print("<li "+ style +" class=file></li><a class=treeview href='" + node.href + "' target='" + node.target + "'  title=\"" + node.toolTip + "\" onclick=\"javascript:set_Data('"+getCode(node.text)+"','"+getDescription(node.text)+"')\">" + node.text + "</a>");
                               // print("<li "+ style +" class=file><a class=treeview href='" + node.href + "' target='" + node.target + "'  title=\"" + node.toolTip + "\" onclick=\"javascript:set_Data('"+node.code+"','"+node.desc+"')\">" + node.text + "</a>");
                                // print("<li "+ style +" class=file><a class=treeview href='" + node.href + "' target='" + node.target + "'  title=\"" + node.toolTip + "\" >" + node.text + "</a>");
			}
			
			if(hasChild){		
                            //Code modified for Case#3522 - Default list of areas of research only expand to first level
                            //loopThru(node.childNodes,parent + "_" + i);
                            loopThru(node.childNodes,parent + "_" + i,expand);
			}	
					
			//print("</li>");
		}
		print("</ul>");
	}
        
       public class NodeList{
            Vector v;
            int length=0;
            public NodeList(){
                v=new Vector();
            }
            
            public void add(Node node){
                v.add(node);
                length++;
            }
            public void add(String text){
                add(new Node(text));
            }
            public Node item(int index){
                return (Node)v.get(index);
            }
            
            public boolean isNodePresent(Node node) {
                if(v.contains(node)) {
                    return true;
                }else {
                    return false;
                }
            }
            
            public int size(){
                return v.size();
            }
        }
        
        public class Node{
            public String text;
            public String href;
            public String target="";
            public String toolTip;
            public NodeList childNodes;
            public String imageUrl="";
            public int length=0;
            
            
            public Node(){
                childNodes = new NodeList();
            }
            public Node(String text){
                this(text,"");
            }
            public Node(String text,String href){
                this(text,href,"");
            }
            public Node(String text,String href,String toolTip){
                this();
                this.text=text;
                this.href=href;
                this.toolTip=toolTip;
               
            }
            
            public void add(Node node){
                childNodes.add(node);
                length++;
            }
            public void add(String text){
                add(new Node(text));
            }
            
        }
}

