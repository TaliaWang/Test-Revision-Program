import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/**
 * Frame to edit (add, delete, change) quiz information. 
 *
 * @Talia Wang
 * @2019
 */
public class EditFrame implements ActionListener
{
    // constants
    private static final Dimension DIMENSION = new Dimension(300, 100);
    
    // instance variables
    private JFrame frame;
    private Container container;
    private String input;
    
    private JButton addQABtn;
    private JButton backBtn;
    private JButton deleteQABtn;
    private JButton editAnsBtn;
    private JButton editQBtn;
    private JButton showQABtn;
    private JComboBox questionBox;
    private JScrollPane scroll;
    private JTextPane instruction;
    private JTextArea answerField;

    /**
     * Constructor for objects of class EditFrame
     */
    public EditFrame()
    {
        // miscellaneous
        input = new String("");
        
        // frame and container
        frame = new JFrame("Edit Subject Information");
        container = new Container();
        container.setLayout(new GridBagLayout());
        
        // gridbag 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = IntroFrame.SPACING;
        
        // buttons
        addQABtn = new JButton("Add a question and its answer");
        addQABtn.setPreferredSize(new Dimension(600, 100));
        addQABtn.addActionListener(this);
        gbc.gridwidth = 2; // span 2 grids for aesthetics
        gbc.gridx = IntroFrame.X1 - 2;
        gbc.gridy = IntroFrame.Y1 + 1;  
        container.add(addQABtn, gbc);
        
        backBtn = new JButton("Return to home page");
        backBtn.setPreferredSize(new Dimension(900, 50));
        backBtn.addActionListener(this);
        gbc.gridwidth = 3; // span 3 grids for aesthetics
        gbc.gridx = IntroFrame.X1 - 2;
        gbc.gridy = IntroFrame.Y1 + 2;  
        container.add(backBtn, gbc);
        
        gbc.gridwidth = 1 ;
        
        deleteQABtn = new JButton("Delete this question and its answer");
        deleteQABtn.setPreferredSize(DIMENSION);
        deleteQABtn.addActionListener(this);
        gbc.gridx = IntroFrame.X1;
        gbc.gridy = 0;  
        container.add(deleteQABtn, gbc);
        
        editQBtn = new JButton("Change this question");
        editQBtn.setPreferredSize(DIMENSION);
        editQBtn.addActionListener(this);
        gbc.gridx = IntroFrame.X1;
        gbc.gridy = IntroFrame.Y1 ;  
        container.add(editQBtn, gbc);
        
        editAnsBtn = new JButton("Change the answer to this question");
        editAnsBtn.setPreferredSize(DIMENSION);
        editAnsBtn.addActionListener(this);
        gbc.gridx = IntroFrame.X1;
        gbc.gridy = IntroFrame.Y1 + 1;  
        container.add(editAnsBtn, gbc);
        
        showQABtn = new JButton("Display question and answer");
        showQABtn.setPreferredSize(DIMENSION);
        showQABtn.addActionListener(this);
        gbc.gridx = IntroFrame.X1 - 1;
        gbc.gridy = 0;  
        container.add(showQABtn, gbc);
        
        // combo box 
        questionBox = new JComboBox();
        questionBox.setPreferredSize(DIMENSION);
        questionBox.addActionListener(this);
        gbc.gridx = IntroFrame.X1 - 2;
        gbc.gridy = IntroFrame.Y1;  
        container.add(questionBox, gbc);
        
        // Text pane
        instruction = new JTextPane();
        instruction.setEditable(false);
        instruction.setPreferredSize(DIMENSION);
        gbc.gridx = IntroFrame.X1 - 2;
        gbc.gridy = 0;
        container.add(instruction, gbc);
        
        // Text area
        answerField = new JTextArea();
        answerField.setEditable(false);
        answerField.setLineWrap(true);
        answerField.setWrapStyleWord(true);
        gbc.gridx = IntroFrame.X1 - 1;
        gbc.gridy = IntroFrame.Y1;  
        
        // Scroll pane for text area 
        scroll = new JScrollPane(answerField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(300, 100));
        container.add(scroll, gbc);
        
        // adds all components to frame
        frame.add(container);
        frame.pack();
        frame.setVisible(false); // frame only becomes visible when user wishes so
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
     * Updates subject name in the instruction.
     * @param subjectName the subject name in the instruction
     */
    public void updateInstruction(String subjectName)
    {
        instruction.setText("You are currently editing: " + Subject.selectedSubject 
        + ". \n\nChoose a question from this subject in the drop down menu below to start editing. "
        + "Click 'Display question and answer' to display the full question you have selected and its answer.");
    }
    
    /**
     * Populates the drop down menu with the questions that the user has already created (ie. already in the database). 
     * @param subjectName the subject from which to retrieve questions in the database
     */
    public void populateComboBox(String subjectName)
    {
        questionBox.removeAllItems(); 
        try
        {
            Main.database.rs = Main.database.st.executeQuery("SELECT * FROM QuizInformation WHERE Subject = '" + subjectName + "'");
            while(Main.database.rs.next())
            {  
                questionBox.addItem(Main.database.rs.getString("Question"));
            }
        }catch(Exception ee){System.out.println(ee);}  
    }

    /**
     * Handles user actions.
     * @param action the user action
     */
    public void actionPerformed(ActionEvent action)
    {
        if (action.getSource() == showQABtn || action.getSource() == deleteQABtn || action.getSource() == editQBtn || action.getSource() == editAnsBtn)
        {
            if (questionBox.getSelectedIndex() == -1) // if there are no questions nor answers in the subject
            {
                JOptionPane.showMessageDialog(null, "There are no questions nor answers in this subject.\nPlease add at least one question and answer by clicking 'Add a question and its answer'.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                // displays question and answer
                answerField.setText("Selected question: \n" + questionBox.getSelectedItem().toString() +
                    "\n\nAnswer to this question: \n" + Main.database.getAnswer(questionBox.getSelectedItem().toString()));
                if (action.getSource() == deleteQABtn)
                {
                    boolean inputValid = false;
                
                    // gets confirmation to delete question and answer, handling faulty input
                    do
                    {
                        input = JOptionPane.showInputDialog("Are you sure you wish to delete this question and answer?"
                            + "\nEnter yes or no.");
                        if (input != null)
                        {
                            if (input.equalsIgnoreCase("yes"))
                            {
                                // deletes question
                                inputValid = true;
                                JOptionPane.showMessageDialog(null, "The following will be deleted.\n\nQuestion: " + questionBox.getSelectedItem().toString() + "\nAnswer: " 
                                    + Main.database.getAnswer(questionBox.getSelectedItem().toString()) + ".\n\nThey will no longer be available in the drop down menu.");
                                Main.database.deleteQA(questionBox.getSelectedItem().toString());
                                questionBox.removeItem(questionBox.getSelectedItem());
                            }
                            else if (input.equalsIgnoreCase("no"))
                            {
                                inputValid = true;
                            }
                            else
                            {
                                // handles faulty input
                                JOptionPane.showMessageDialog(null, "Invalid input. Please enter yes or no.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } while (!inputValid && input != null);
                }
                else if (action.getSource() == editQBtn)
                {
                    input = JOptionPane.showInputDialog("What would you like to change this question to?");
                    
                    // gets a new question from the user, handling faulty input
                    while (input != null && input.equals(""))
                    {
                        JOptionPane.showMessageDialog(null, "Invalid question", "Error", JOptionPane.ERROR_MESSAGE);
                        input = JOptionPane.showInputDialog("What would you like to change this question to?");
                    }
                    while (input != null && Main.database.questionAlreadyExists(input))
                    {
                        JOptionPane.showMessageDialog(null, "This question already exists in this subject or another subject.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                        input = JOptionPane.showInputDialog("Please enter a new question:");
                    }
                    if (input != null)
                    {
                        Main.database.updateQuestion(questionBox.getSelectedItem().toString(), input); // updates question in the database
                        populateComboBox(Subject.selectedSubject); 
                        answerField.setText("Selected question: \n" + questionBox.getSelectedItem().toString() +
                            "\n\nAnswer to this question: \n" + Main.database.getAnswer(questionBox.getSelectedItem().toString())); 
                    }
                }
                else if (action.getSource() == editAnsBtn)
                {
                    input = JOptionPane.showInputDialog("What would you like to change this answer to?");
                    
                    // gets a new answer from the user, handling faulty input
                    while(input != null && input.equals(""))
                    {
                        JOptionPane.showMessageDialog(null, "Invalid answer", "Error", JOptionPane.ERROR_MESSAGE);
                        input = JOptionPane.showInputDialog("What would you like to change this answer to?");
                    }
                    if (input != null)
                    {
                        Main.database.updateAnswer(questionBox.getSelectedItem().toString(), input); // updates answer in the database
                        answerField.setText("Selected question: \n" + questionBox.getSelectedItem().toString() +
                            "\n\nAnswer to this question: \n" + Main.database.getAnswer(questionBox.getSelectedItem().toString()));
                    }
                }
            }
        }
        else if (action.getSource() == addQABtn)
        {
            if (questionBox.getSelectedIndex() != -1) // if there is at least 1 question and answer in the subject
            {
                answerField.setText("Selected question: \n" + questionBox.getSelectedItem().toString() +
                            "\n\nAnswer to this question: \n" + Main.database.getAnswer(questionBox.getSelectedItem().toString()));
            }
            String question = new String("");
            String answer = new String("");
            
            question = JOptionPane.showInputDialog("Please enter a new question:");
            
            // gets a new question from the user, handling faulty input
            while (question != null && question.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Invalid question",
                    "Error", JOptionPane.ERROR_MESSAGE);
                question = JOptionPane.showInputDialog("Please enter a new question:");
            }
            while (question != null && Main.database.questionAlreadyExists(question))
            {
                JOptionPane.showMessageDialog(null, "This question already exists in this subject or another subject.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                question = JOptionPane.showInputDialog("Please enter a new question:");
            }
            
            // gets the answer to the question, assuming the user did not click "cancel" when entering a new question
            if (question != null)
            {
                answer = JOptionPane.showInputDialog("Please enter the answer to this question:");
                while (answer != null && answer.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "Invalid answer",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    answer = JOptionPane.showInputDialog("Please enter the answer to this question:");
                }
            }
            
            if (question == null || answer == null) // if user cancelled mid-way
            {
                JOptionPane.showMessageDialog(null, "You tried to add an empty question and/or answer, so none of your input was added.");
            }
            else
            {
                Main.database.insertQA(question, answer, Subject.selectedSubject); // adds question and answer to database
                populateComboBox(Subject.selectedSubject); 
                JOptionPane.showMessageDialog(null, "You have added the following to the subject, " + Subject.selectedSubject + ":"
                + "\n\n Question: " + question + "\n\n Answer: " + answer);
            }
        }
        else
        {
            // sets default blank answer if a question is selected in the drop down menu
            // encourages the user to click "Display question and answer" so that they know what they are changing
            answerField.setText("");  
        }
        
        if (action.getSource() == backBtn)
        {
            // returns to home page
            Main.introFrame.setVisible(true);
            Main.editFrame.setVisible(false);
        }
        
    }
}
