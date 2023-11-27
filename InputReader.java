import java.util.HashSet;
import java.util.Scanner;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

/**
 * InputReader reads typed text input from the standard text terminal. 
 * The text typed by a user is then chopped into words, and a set of words 
 * is provided.
 * 
 * @author Daniel Corritore
 *         modified from David J. Barnes and Michael Kölling.
 * @version 2023.11.26
 */
public class InputReader
{
    //private Scanner readerConsole;
    private BufferedReader readerFile;
    private Charset charset;
    private Path path;
    private String filename;

    /**
     * Create a new InputReader that reads text from a file
     * @throws IOException If there is an I/O error with opening a file
     */
    public InputReader()
    throws IOException
    {
        //readerConsole = new Scanner(System.in);
        charset = Charset.forName("US-ASCII");
        filename = "input_example.txt";
        path = Paths.get(filename);
        try {
            readerFile = Files.newBufferedReader(path, charset);
        }
        catch(IOException e) {
            System.out.println("InputReader(): Unable to open " + filename);
            throw e;
        }
    }
    
    /**
     * Read a line of text from the text file and return it as a set of words.
     * 
     * @return A set of Strings, each containing one word of a line of text in a file
     * @throws IOException If there is an I/O error during file reading
     */
    public HashSet<String> getInput()
    throws IOException
    {
        boolean exitLoop = false;
        String inputText = "";
        try {
            // grab a (potential) multi-line entry, combine until blank line
            do {
                String newLine = readerFile.readLine();
                // end-of-file reached?
                if (newLine == null) {
                    exitLoop = true;
                } else {
                    // clear whitespace, convert to lowercase
                    newLine = newLine.trim().toLowerCase();
                    // empty line reached? done
                    if (newLine.isEmpty()) {                    
                        exitLoop = true;
                    } else {
                        // add a space between lines to avoid merging words
                        if (!inputText.isEmpty())
                            inputText += " ";
    
                        // and combine new line
                        inputText += newLine;
                    }
                }
            } while (!exitLoop);
        }
        catch (IOException e) {
            System.out.println("getInput: Unexpected error reading file " + filename);
        }
        
        // Nothing to return
        if (inputText.isEmpty())
            return null;

        // Show input
        System.out.println(">" + inputText);
        
        // Strip punctuation to avoid them being included with "words"
        inputText = inputText.replaceAll("\\p{Punct}", "");
            
        // Make the hashset
        String[] wordArray = inputText.split(" ");  // split at spaces

        // add words from array into hashset 
        HashSet<String> words = new HashSet<>();
        for(String word : wordArray) {
            words.add(word);
        }
        return words;
    }
}
