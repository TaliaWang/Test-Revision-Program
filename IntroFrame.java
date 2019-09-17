import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/**
 * First frame of test revision program.
 * User can choose to edit a subject or write a quiz on that subject. 
 *
 * @Talia Wang
 * @2019
 */
public class IntroFrame implements ActionListener
{
    // constants
    private static final float TITLE_FONT_SIZE = 30;
    private static final Dimension DIMENSION = new Dimension(300, 50); 
    protected static final int X1 = 3; // default GUI component x-position
    protected static final int Y1 = 1; // default GUI component y-position
    protected static final Insets SPACING = new Insets(10,10,10,10);
    
    // instance variables 
    private String input;
    
    private JFrame frame;
    private Container container;
    private JButton addBtn;
    private JButton changeBtn;
    private JButton deleteBtn;
    private JButton editBtn;
    private JButton writeBtn;
    private JComboBox subjectBox;
    private JLabel optionLabel;
    private JLabel subjectLabel;
    private JLabel title;

    /**
     * Constructor for objects of class IntroFrame
     */
    public IntroFrame()
    {
        // miscellaneous
        input = new String("");
        
        // frames and containers
        frame = new JFrame("Test Revision Program");
        container = new Container();
        container.setLayout(new GridBagLayout());
        
        // gridbag 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = SPACING;
       
        // labels
        title = new JLabel("Test Revision Program");
        title.setFont(title.getFont().deriveFont(TITLE_FONT_SIZE));
        title.setForeground(new Color (5, 100, 100));
        container.add(title, gbc);
        
        gbc.gridwidth = 1;
        
        subjectLabel = new JLabel("Step 1: Select a subject");
        gbc.gridx = 0;
        gbc.gridy = Y1;
        container.add(subjectLabel, gbc);
        
        // buttons
        addBtn = new JButton("Add a new quiz subject");
        addBtn.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = Y1 + 4;
        addBtn.setPreferredSize(DIMENSION);
        container.add(addBtn, gbc);
        
        changeBtn = new JButton("Change this subject's name");
        changeBtn.addActionListener(this);
        gbc.gridx = X1;
        gbc.gridy = Y1 + 4;
        changeBtn.setPreferredSize(DIMENSION);
        container.add(changeBtn, gbc);
        
        deleteBtn = new JButton("Delete this subject");
        deleteBtn.addActionListener(this);
        gbc.gridx = X1;
        gbc.gridy = Y1 + 3;
        deleteBtn.setPreferredSize(DIMENSION);
        container.add(deleteBtn, gbc);
       
        editBtn = new JButton("Edit this subject's information");
        editBtn.setPreferredSize(DIMENSION);
        editBtn.addActionListener(this);
        gbc.gridx = X1;
        gbc.gridy = Y1 + 2;
        container.add(editBtn, gbc);
        
        writeBtn = new JButton("Write quiz on this subject");
        writeBtn.setPreferredSize(DIMENSION);
        writeBtn.addActionListener(this);
        gbc.gridx = X1;
        gbc.gridy = Y1 + 1;  
        container.add(writeBtn, gbc);

        // label
        optionLabel = new JLabel("Step 2: Choose one of the options below:");
        gbc.gridx = X1;
        gbc.gridy = Y1;
        container.add(optionLabel, gbc);
        
        // combo box 
        subjectBox = new JComboBox();
        subjectBox.setPreferredSize(DIMENSION);
        subjectBox.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = Y1 + 1;
        container.add(subjectBox, gbc);
        populateComboBox();
        
        // adds all components to frame
        frame.add(container);
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Checks if a subject already exists in the drop down menu.
     * @param subjectName the subject to be checked for duplicates
     * @return whether the subject exists in the drop down menu
     */
    public boolean subjectAlreadyExists(String subjectName)
    {
        loop: for (int x = 0; x < subjectBox.getItemCount(); x++)
        {
            if (subjectBox.getItemAt(x).toString().equals(subjectName))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Populates the drop down menu with unique subject names that already exist in the database.
     */
    public void populateComboBox ()
    {
        subjectBox.removeAllItems();
        try
        {
            Main.database.rs = Main.database.st.executeQuery("SELECT DISTINCT Subject from QuizInformation");
            while(Main.database.rs.next())
            {  
                subjectBox.addItem(Main.database.rs.getString("Subject"));
            }  
        }catch(Exception ee){System.out.println(ee);} 
    }
    
    /**
     * Changes visibility of this frame. 
     * @param visibility true or false
     */
    public void setVisible(boolean visibility)
    {
        frame.setVisible(visibility);
    }
    
    /**
     * Handles user actions.
     * @param action the user action
     */
    public void actionPerformed(ActionEvent action)
    {
        if (action.getSource() == addBtn)
        {
           input = JOptionPane.showInputDialog("Enter name of subject.");

           // gets a valid subject name
           while (input != null && (input.equals("") || subjectAlreadyExists(input)))
           {
               if (input.equals(""))
               {
                   JOptionPane.showMessageDialog(null, "Please enter a valid subject name.", "Error", JOptionPane.ERROR_MESSAGE);
               }
               else if (subjectAlreadyExists(input))
               {
                   JOptionPane.showMessageDialog(null, "The subject, " + input + ", already exists. Please use a different subject name.", "Error", JOptionPane.ERROR_MESSAGE);
               }
               input = JOptionPane.showInputDialog("Enter name of subject.");
           }
           if (input != null)
           {
               subjectBox.addItem(input);
           }
        }
        if (action.getSource() == writeBtn || action.getSource() == editBtn || action.getSource() == deleteBtn
            || action.getSource() == changeBtn)
        {
            if (subjectBox.getSelectedIndex() == -1) // if no subjects exist
            {
                JOptionPane.showMessageDialog(null, "No subjects exist. Please add a new subject.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if(action.getSource() == writeBtn) 
            {
                // if there are no records of this subject in the database
                if (Main.database.getNumberOfQA(subjectBox.getSelectedItem().toString()) <= 0) 
                {
                    JOptionPane.showMessageDialog(null, "There are no questions nor answers in this subject. "
                        + "Please click 'edit this subject's information' to add some questions and answers.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                }
                // if there are too few questions
                else if (Main.database.getNumberOfQA(subjectBox.getSelectedItem().toString()) <= 3) 
                {
                    JOptionPane.showMessageDialog(null, "There is/are only " + Main.database.getNumberOfQA(subjectBox.getSelectedItem().toString())
                        + " question(s) and answer(s) in this subject. You must create at least 4 to write a quiz."
                        + "\nPlease click on 'Edit this subject's information' to add more questions and answers.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    Subject.selectedSubject = subjectBox.getSelectedItem().toString();
                    int integer = 0;
                    boolean isValid = false;
                    
                    input = JOptionPane.showInputDialog("There is/are " + Main.database.getNumberOfQA(Subject.selectedSubject)
                            + " question(s) and answer(s) in this subject. " 
                            + "Please enter the number of questions you would like in this quiz, up to and including "
                            + Main.database.getNumberOfQA(Subject.selectedSubject) + ".");
                    
                    // Gets a valid number of quiz questions
                    while (input != null && !isValid)
                    {
                        try
                        {
                            integer = Integer.parseInt(input);
                            if (integer <= 0 || integer > Main.database.getNumberOfQA(Subject.selectedSubject))
                            {
                                input = JOptionPane.showInputDialog("Please enter a positive integer from 1 to " 
                                + Main.database.getNumberOfQA(Subject.selectedSubject) + ".");
                            }
                            else
                            {
                                isValid = true;
                            }
                        }
                        catch (Exception ee)
                        {
                            input = JOptionPane.showInputDialog("Please enter a positive integer from 1 to " 
                            + Main.database.getNumberOfQA(Subject.selectedSubject) + ".");
                        }
                    }
                    
                    // Begins a quiz
                    if (input != null)
                    {
                        frame.setVisible(false);
                        QuizFrame quiz = new QuizFrame();
                        quiz.setNumberOfQuizQ(integer);
                        quiz.startQuiz(Subject.selectedSubject);
                    }
                }
            }
            else if (action.getSource() == deleteBtn)
            {
                boolean inputValid = false;
                
                // Gets confirmation from user to delete the subject, handling faulty input
                do
                {
                    input = JOptionPane.showInputDialog("Deleting this subject will delete all of its questions and answers." + 
                            " Are you sure you wish to delete " + subjectBox.getSelectedItem().toString() + "?"
                            + "\nEnter yes or no.");
                    if (input != null)
                    {
                        if (input.equalsIgnoreCase("yes")) 
                        {
                            // deletes the subject
                            inputValid = true;
                            int deleteCount = Main.database.deleteSubject(subjectBox.getSelectedItem().toString());
                            JOptionPane.showMessageDialog(null, subjectBox.getSelectedItem().toString() + " has been deleted, along with " + deleteCount + " question(s) and answer(s).");
                            subjectBox.removeItem(subjectBox.getSelectedItem());
                        }   
                        else if (input.equalsIgnoreCase("no"))
                        {
                            inputValid = true;
                        }
                        else
                        {
                            // faulty input handling
                            JOptionPane.showMessageDialog(null, "Invalid input. Please enter yes or no.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } while (!inputValid && input != null);
            }
            else if (action.getSource() == editBtn)
            {
                // Opens up editing window 
                Subject.selectedSubject = subjectBox.getSelectedItem().toString();
                Main.editFrame.updateInstruction(Subject.selectedSubject);
                Main.editFrame.populateComboBox(Subject.selectedSubject);
                Main.editFrame.setVisible(true);
                Main.introFrame.setVisible(false);
            }
            else if (action.getSource() == changeBtn)
            {
                input = JOptionPane.showInputDialog("What would you like to change this subject's name to?");
                
                // Gets a valid new subject name 
                while (input != null && (input.equals("") || subjectAlreadyExists(input)))
                {
                    if (input.equals(""))
                    {
                        JOptionPane.showMessageDialog(null, "Please enter a valid subject name.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (subjectAlreadyExists(input))
                    {
                        JOptionPane.showMessageDialog(null, "The subject, " + input + ", already exists. Please use a different subject name.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    input = JOptionPane.showInputDialog("Enter name of subject.");
                }    
                if (input != null)
                {
                    Main.database.changeSubjectName(subjectBox.getSelectedItem().toString(), input);
                    populateComboBox();
                }
            }
        }
    }
}
