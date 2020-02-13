package edu.mit.coeus.s2s.formattachment;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.validator.BindingFileReader;
import edu.mit.coeus.utils.UtilFactory;


public class FilterRemainForms {
	public static void main (String args[]) throws Exception{
		new FilterRemainForms().process();
	}
	public void process () throws Exception{
		Map m = BindingFileReader.getBindings();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new ErrorHandler() {
			
			public void warning(SAXParseException exception) throws SAXException {
				// TODO Auto-generated method stub
				
			}
			
			public void fatalError(SAXParseException exception) throws SAXException {
				// TODO Auto-generated method stub
				
			}
			
			public void error(SAXParseException exception) throws SAXException {
				// TODO Auto-generated method stub
				
			}
		});
        document = builder.parse( new FilterRemainForms().getClass().getResourceAsStream("/edu/mit/coeus/s2s/formattachment/MetaGrantsImports.xml"));
        NodeList formList = document.getElementsByTagName("xsd:import");
        System.out.println("mapping binding length : "+m.size());
        System.out.println("formList length : "+formList.getLength());
		int c = 0;
        for (int i = 0; i < formList.getLength(); i++) {
			Node n = formList.item(i);
			NamedNodeMap nm = n.getAttributes();
			Node keyNode = nm.getNamedItem("namespace");
			String name = keyNode.getNodeName();
			String val = keyNode.getNodeValue();
			if(m.get(val)==null){
				++c;
				Node sl = nm.getNamedItem("schemaLocation");
				System.out.println(val);
				saveFile(sl.getNodeValue());
			}
		}
		System.out.println(c);
	}

	private static void saveFile(String urlValue) throws Exception{
		URL url = new URL(urlValue);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		String[] tokens = url.getFile().split("/");
		FileUtils.copyURLToFile(url, new File("/Users/geo/eclipse/workspace/Coeus45Trunk/schemas/"+tokens[tokens.length-1]));
//		FileOutputStream fos = new FileOutputStream(new File("/Users/geo/eclipse/workspace/Coeus45Trunk/schemas",tokens[tokens.length-1]));
//		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}
}
