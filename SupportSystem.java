import java.util.HashSet;
import java.io.IOException;

/**
 * This class implements a technical support system.
 * It is the top level class in this project.
 * The support system communicates via text input/output
 * from a text file.
 * 
 * This class uses an object of class InputReader to read input
 * from a file, and an object of class Responder to generate responses.
 * It contains a loop that repeatedly reads input and generates
 * output until the end of a file or the word "bye" is encountered.
 * 
 * @author Daniel Corritore
 *         modified from David J. Barnes and Michael Kölling.
 * @version 2023.11.26
 */
public class SupportSystem
{
    private InputReader reader;
    private Responder responder;
    
    /**
     * Creates a technical support system.
     * 
     * @throws IOException If there's an I/O error opening a file
     */
    public SupportSystem()
    throws IOException
    {
        reader = new InputReader();
        responder = new Responder();
    }

    /**
     * Start the technical support system. This will print a welcome message and enter
     * into a dialog with the user, until the user ends the dialog.
     * 
     * @throws IOException If there's an I/O error reading file
     */
    public void start()
    throws IOException
    {
        boolean finished = false;

        printWelcome();

        while(!finished) {
            HashSet<String> input = reader.getInput();
            
            // NOTE: short-circuiting doesn't work in Java. who knew? (following throws on null):
            //if (input == null || input.contains("bye")) {
                
            if (input == null) {
                finished = true;
            }
            else if (input.contains("bye")) {
                finished = true;
            }
            else {
                String response = responder.generateResponse(input);
                System.out.println(response);
            }
        }
        printGoodbye();
    }

    /**
     * Print a welcome message to the screen.
     */
    private void printWelcome()
    {
        System.out.println("Welcome to the DodgySoft Technical Support System.");
        System.out.println();
        System.out.println("Please tell us about your problem.");
        System.out.println("We will assist you with any problem you might have.");
        System.out.println("Please type 'bye' to exit our system.");
    }

    /**
     * Print a good-bye message to the screen.
     */
    private void printGoodbye()
    {
        System.out.println("Nice talking to you. Bye...");
    }
}
