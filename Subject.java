import java.util.*;
/**
 * This is a quiz subject. 
 *
 * @Talia Wang
 * @2019
 */
public class Subject
{
    // static variables
    protected static String selectedSubject = new String("");
    
    // instance variables
    private String name;
    
    /**
     * Constructor for objects of class Subject.
     * Takes in the name of the subject as a parameter.
     */
    public Subject(String n)
    {
        name = n;
    }
    
     /**
     * Returns the name of this subject. 
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Sets the name of this subject. 
     */
    public void setName(String n)
    {
        if (!n.equals(null)) name = n;
    }
}

