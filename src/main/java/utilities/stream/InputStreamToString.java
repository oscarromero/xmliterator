package utilities.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Convert an InputStream to String.
 * 
 * @author oscarromero
 */
public class InputStreamToString {
   
    public static String get(InputStream stream) {
        if (stream == null) return null;
        
        try {
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder buffer = new StringBuilder();
            String str = "";
            while((null != (str = reader.readLine()))) {
                buffer.append(str);
                buffer.append("\n");
            }
            return buffer.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }        
    }
    
}
