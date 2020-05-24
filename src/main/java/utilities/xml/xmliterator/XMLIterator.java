package utilities.xml.xmliterator;

import utilities.stream.InputStreamToString;
import java.util.List;
import java.util.ArrayList;

import org.json.XML;
import org.json.JSONObject;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Iterate every xml node that comes from a xml, txt, object or Document class.
 * 
 * @author oscarromero
 */
public class XMLIterator {
   
    private final DocumentBuilderFactory dbf;
    private final DocumentBuilder builder;
    private Document document;
    
    private ICallback callback;
    private List<Node> nodes;
        
    public XMLIterator() throws Exception {
        dbf = DocumentBuilderFactory.newInstance();
        builder = dbf.newDocumentBuilder();        
    }
    
    /**
     * Create a Document instance from String content.
     * 
     * @param xmlContent the xml content as string.
     * @throws Exception
     */
    public void fromString(String xmlContent) throws Exception {
        InputSource inputSource = new InputSource(new StringReader(xmlContent));
        document = builder.parse(inputSource);
    }
    
    /**
     * Create a Document instance from File object.
     * 
     * @param xmlContent the xml content as File instance.
     * @throws Exception
     */
    public void fromFile(File xmlContent) throws Exception {
        document = builder.parse(xmlContent);
    }
    
    /**
     * Create a Document instance from InputStream object.
     * 
     * @param xmlContent the xml content as InputStream instance.
     * @throws Exception
     */
    public void fromInputStream(InputStream xmlContent) throws Exception {
        fromString(InputStreamToString.get(xmlContent));
    }
    
    /**
     * Convert to XML a TXT delimited by | , ; : / - 
     * 
     * @param txtContent the text file
     * @param separator the delimiter
     * @throws Exception
     */
    public void fromTXT(InputStream txtContent, String separator) 
    throws Exception {
        String content = InputStreamToString.get(txtContent);
        String lines[] = content.split("\n");
        if (content.trim().isEmpty()) {
            throw new Exception("InputStreamToString returned an empty String.");
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        document = db.newDocument();
        Node rootNode = document.createElement("Structure");        
        document.appendChild(rootNode);
        
        for(int l = 0; l < lines.length; l++) {
            Node lastNode = document.createElement("Node");
            rootNode.appendChild(lastNode);
            String[] fields = lines[l].split(separator);
            for(int i = 0; i < fields.length; i++) {
                String field = fields[i].trim().replace(" ", "");
                System.out.println(field);
                lastNode.appendChild(document.createElement(field));
            }
        }
    }
    
    /**
     * Create a Document instance from JSON object.
     * 
     * @param jsonContent the json content as String.
     * @throws Exception 
     */
    public void fromJsonString(String jsonContent) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonContent);
        String xml = XML.toString(jsonObject, "document");
        document = builder.parse(new InputSource(new StringReader(xml)));
    }
    
    /**
     * Create a Document instance from an object.
     * 
     * @param object The object to iterate as xml node.
     * @throws java.lang.Exception
     */
    public void fromObject(Class<?> object, Object content) throws Exception {
        JAXBContext context = JAXBContext.newInstance(object);
        Marshaller marshaller = context.createMarshaller();
        document = builder.newDocument();
        marshaller.marshal(content, document);
    }
        
    /**
     * This method is called for each iterated node.
     * Implement your logic in another class implementing the ICallback interface.
     * 
     * @param callback Callback to execute for every iterated node.
     * @throws Exception
     */
    public void forEachNode(ICallback callback) throws Exception {
        this.callback = callback;
        this.nodes = new ArrayList();
        Node root = document.getChildNodes().item(0);
        
        callback.execute(root);
        scanNodes(root, nodes);
        callback.registerLastNode();
    }
    
    public Document getDocument() {
        return document;
    }
    
    @Override
    public String toString() {
        return XMLToString.get(document);
    }
    
    private List<Node> scanNodes(Node currentNode, List<Node> nodes) 
    throws Exception
    {
        final NodeList children = currentNode.getChildNodes();
        for(int i = 0; i < children.getLength(); i++) {
            final Node node = children.item(i);
            final boolean isRootNode = node.getChildNodes().getLength() > 0;
            final String nodeName = node.getNodeName();
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                callback.execute(node);
                nodes.add(node);
                nodes = scanNodes(node, nodes);
            }
        }
        return nodes;
    }
    
}
