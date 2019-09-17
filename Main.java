/**
 * Main class to begin test revision program.
 *
 * @Talia Wang
 * @2019
 */
public class Main
{
    // static variables
    protected static IntroFrame introFrame;
    protected static EditFrame editFrame;
    protected static Database database;
    
    /**
     * Main method
     * Runs test revision program.
     */
    public static void main (String[] args)
    {
         database = new Database();
         introFrame = new IntroFrame();
         editFrame = new EditFrame();
    }
}
