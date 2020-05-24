package utilities.xml.xmliterator;

import java.io.StringWriter;

import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Convert any object to a String.
 * 
 * @author oscarromero
 */
public class XMLToString {
    
    /**
     * Return the XML as String.
     * 
     * @param document The Document class to convert.
     * @return String
     */
    public static String get(Document document) {
        try {
            DOMSource domSource = new DOMSource(document);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);        
            return writer.toString();
        } catch (Exception ex) {
            return "";
        }        
    }
    
}
