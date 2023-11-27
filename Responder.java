import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

/**
 * The responder class represents a response generator object.
 * It is used to generate an automatic response, based on specified input.
 * Input is presented to the responder as a set of words, and based on those
 * words the responder will generate a String that represents the response.
 *
 * Internally, the reponder uses a HashMap to associate words with response
 * strings and a list of default responses. If any of the input words is found
 * in the HashMap, the corresponding response is returned. If none of the input
 * words is recognized, one of the default responses is randomly chosen.
 * 
 * @author Daniel Corritore
 *         modified from David J. Barnes and Michael Kölling.
 * @version 2023.11.26
 */
public class Responder
{
    // Used to map key words to responses.
    private HashMap<String, String> responseMap;
    // Default responses to use if we don't recognise a word.
    private ArrayList<String> defaultResponses;
    // The name of the file containing the default responses.
    private static final String FILE_OF_DEFAULT_RESPONSES = "default.txt";
    private Random randomGenerator;

    /**
     * Construct a Responder
     * 
     * @throws IOException If there is an I/O error during file reading
     * of responseMap
     */
    public Responder()
    throws IOException
    {
        responseMap = new HashMap<>();
        defaultResponses = new ArrayList<>();
        fillResponseMap();
        fillDefaultResponses();
        randomGenerator = new Random();
    }

    /**
     * Generate a response from a given set of input words.
     * 
     * @param words  A set of words entered by the user
     * @return       A string that should be displayed as the response
     */
    public String generateResponse(HashSet<String> words)
    {
        Iterator<String> it = words.iterator();
        while(it.hasNext()) {
            String word = it.next();
            String response = responseMap.get(word);
            if(response != null) {
                return response;
            }
        }
        // If we get here, none of the words from the input line was recognized.
        // In this case we pick one of our default responses (what we say when
        // we cannot think of anything else to say...)
        return pickDefaultResponse();
    }

    /**
     * Enter all the known keywords and their associated responses
     * into our response map.
     * 
     * @throws IOException If there is an I/O error during file reading
     */
    private void fillResponseMap()
    throws IOException
    {
        Charset charset = Charset.forName("US-ASCII");
        String filename = "response_map.txt";
        Path path = Paths.get(filename);
        boolean exitLoop = false;
        
        try {
            BufferedReader readerFile = Files.newBufferedReader(path, charset);
        
            do {
                // grab the key(s)
                String keyLine = readerFile.readLine();
                
                // end-of-file reached? done with map
                if (keyLine == null) {
                    exitLoop = true;
                    break;
                }
                // clear whitespace, convert to lowercase
                keyLine = keyLine.trim().toLowerCase();
                
                // empty line reached? done with map
                if (keyLine.isEmpty()) {                    
                    exitLoop = true;
                    break;
                }

                String value = "";   
                boolean exitInnerLoop = false;
                // grab a (potential) multi-line entry, combine until blank line
                do {
                    String newLine = readerFile.readLine();
                    // end-of-file reached? done grabbing value
                    if (newLine == null) {
                        exitInnerLoop = true;
                        break;
                    }
                    // clear whitespace
                    newLine = newLine.trim();
                    
                    // empty line reached? done grabbing value
                    if (newLine.isEmpty()) {                    
                        exitInnerLoop = true;
                        break;
                    }
                    
                    // add a newline between lines
                    if (!value.isEmpty())
                        value += "\n";

                    // and combine new line
                    value += newLine;
                    
                } while (!exitInnerLoop);
                
                // Check that we actually got a value. If not, break
                if (value.isEmpty()) {
                    exitLoop = true;
                    break;
                }
                
                // Now we have key(s)/value pair
                
                // Split the keys and add them to the map with the value
                String[] keyArray = keyLine.split(",");  // split at commas
                // we trim() again because of whitespace between comma-separated keys
                for(String key : keyArray) {
                    responseMap.put(key.trim(), value);
                }
            } while (!exitLoop);
        }
        catch(IOException e) {
            System.out.println("fillResponseMap: Error reading " + filename);
            throw e;
        }

    }

    /**
     * Build up a list of default responses from which we can pick
     * if we don't know what else to say.
     */
    private void fillDefaultResponses()
    {
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(FILE_OF_DEFAULT_RESPONSES);
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String response = reader.readLine();
            while(response != null) {
                defaultResponses.add(response);
                response = reader.readLine();
            }
        }
        catch(FileNotFoundException e) {
            System.err.println("Unable to open " + FILE_OF_DEFAULT_RESPONSES);
        }
        catch(IOException e) {
            System.err.println("A problem was encountered reading " +
                               FILE_OF_DEFAULT_RESPONSES);
        }
        // Make sure we have at least one response.
        if(defaultResponses.size() == 0) {
            defaultResponses.add("Could you elaborate on that?");
        }
    }

    /**
     * Randomly select and return one of the default responses.
     * @return     A random default response
     */
    private String pickDefaultResponse()
    {
        // Pick a random number for the index in the default response list.
        // The number will be between 0 (inclusive) and the size of the list (exclusive).
        int index = randomGenerator.nextInt(defaultResponses.size());
        return defaultResponses.get(index);
    }
}
