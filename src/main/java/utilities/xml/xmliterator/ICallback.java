package utilities.xml.xmliterator;

import org.w3c.dom.Node;

/**
 * Implement this interface.
 * 
 * @author oscarromero
 */
public interface ICallback {
   
    /**
     * Code to be executed for each iterated node.
     * 
     * @param node 
     */
    void execute(Node node);
    void registerLastNode();
    
}
