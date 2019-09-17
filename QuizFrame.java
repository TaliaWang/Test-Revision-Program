import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/**
 * Frame that hosts multiple choice quizzes to help the user review for tests. 
 *
 * @Talia Wang
 * @2019
 */
public class QuizFrame implements ActionListener
{
    // constants
    private static final Dimension DIMENSION = new Dimension(300, 100);
    
    // instance variables 
    private int numberOfQuizQ;
    private int currentQ;
    private String[] possibleAnswers;
    private String[] possibleQuestions;
    private String[] tempAns;
    private int score;
    private boolean answerCorrect;
    
    private JButton checkAnsBtn;
    private JButton endBtn;
    private ButtonGroup btngroup;
    private JFrame frame;
    private Container container;
    private JRadioButton[] radioBtn;
    private JScrollPane scroll;
    private JTextArea questionArea;
    private JLabel label;
    
    /**
     * Constructor for objects of class QuizFrame
     */
    public QuizFrame()
    {
        // miscellaneous
        numberOfQuizQ = 0;
        currentQ = 0;
        score = 0;
        answerCorrect = false;
        
        // frames and containers
        frame = new JFrame("Quiz in Progress");
        container = new Container();
        container.setLayout(new GridBagLayout());
        
        // gridbag 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = IntroFrame.SPACING;
        
        // label
        label = new JLabel();
        gbc.gridx = IntroFrame.X1;
        gbc.gridy = 0;
        container.add(label, gbc);
        
        // radio buttons
        radioBtn = new JRadioButton[4]; // 4 multiple choice answers per question
        btngroup = new ButtonGroup();
        for (int index = 0; index < radioBtn.length; index++)
        {
            radioBtn[index] = new JRadioButton("default");
            btngroup.add(radioBtn[index]);
            gbc.gridx = IntroFrame.X1;
            gbc.gridy = IntroFrame.Y1 + index + 1;
            container.add(radioBtn[index], gbc);
        }
        
        // text area 
        questionArea = new JTextArea("start");
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        gbc.gridx = IntroFrame.X1;
        gbc.gridy = IntroFrame.Y1;
        
        // Scroll pane for text area 
        scroll = new JScrollPane(questionArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(DIMENSION);
        container.add(scroll, gbc);
        
        // buttons
        checkAnsBtn = new JButton("Check answer and continue");
        checkAnsBtn.addActionListener(this);
        gbc.gridx = IntroFrame.X1;
        gbc.gridy = IntroFrame.Y1 + 5;
        checkAnsBtn.setPreferredSize(new Dimension(300, 50));
        container.add(checkAnsBtn, gbc);
        
        endBtn = new JButton("End quiz and return to home page");
        endBtn.addActionListener(this);
        gbc.gridx = IntroFrame.X1;
        gbc.gridy = IntroFrame.Y1 + 6;
        endBtn.setPreferredSize(new Dimension(300, 50));
        container.add(endBtn, gbc);
        
        // adds all components to frame
        frame.add(container);
        frame.pack();
        frame.setSize(350, 550);
        frame.setVisible(true);
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
     * Sets the number of questions that will be asked during this quiz. 
     * @param numQuestions number of questions that will be asked during this quiz
     */
    public void setNumberOfQuizQ(int numQuestions)
    {
        numberOfQuizQ = numQuestions;
    }
    
    /**
     * Shuffles an array to make its order of elements random.
     * @param list the array to be shuffled
     */
   public void shuffle(String[] list)
    {
         // number of times two elements are swapped
         // Note that number of possible swaps increases as number of questions and answers in the subject increases
        int timesSwapped = (int)(Math.random()*(Main.database.getNumberOfQA(Subject.selectedSubject) + Main.database.getNumberOfQA(Subject.selectedSubject) / 2));  
        
        // swaps two random elements a random number of times
        for (int currentSwap = 0; currentSwap < timesSwapped; currentSwap++)
        {
            int index1 = (int)(Math.random()*list.length);
            String temp = list[index1];
            
            int index2 = (int)(Math.random()*list.length);
            list[index1] = list[index2];
            list[index2] = temp;
        }
    }
    
    /**
     * Displays the next question with a set of possible answers.
     * @param questionIndex the index of the next question to be asked
     */
    public void displayQuestionAndAnswers(int questionIndex)
    {
        questionArea.setText(possibleQuestions[questionIndex]); // displays the question at this index
        
        /* The following displays 4 possible answers with 1 correct answer */

        shuffle(possibleAnswers);
        
        tempAns = new String[radioBtn.length]; // temporary storage for answer text
        
        boolean realAnswerPresent = false;
        for (int index = 0; index < radioBtn.length; index++)
        {
            tempAns[index] = possibleAnswers[index];
            if (tempAns[index].equals(Main.database.getAnswer(questionArea.getText())))
            {
                realAnswerPresent = true;
            }
        }
        if (!realAnswerPresent)
        {
            // ensures the correct answer is always present for each question
            tempAns[0] = Main.database.getAnswer(questionArea.getText());
        }
        
        shuffle(tempAns); // the possible answers appear in a random order
        for (int index = 0; index < radioBtn.length; index++)
        {
            radioBtn[index].setText(tempAns[index]);
        }
    }
    
    /**
     * Begins the quiz.
     * @param subjectName the subject of the quiz
     */
    public void startQuiz(String subjectName)
    {
        label.setText("Question 1/" + numberOfQuizQ + " in " + Subject.selectedSubject);
        
        possibleQuestions = new String[Main.database.getNumberOfQA(subjectName)]; 
        possibleAnswers = new String[Main.database.getNumberOfQA(subjectName)]; 
        
        // Gets all of the questions in this subject and puts them in the possibleQuestions array
        try
        {
            possibleQuestions = Main.database.getAllQuestions(subjectName, possibleQuestions.length);
        }catch(Exception ee){System.out.println(ee);}  
        
        shuffle(possibleQuestions);
        
        // Gets all of the answers in this subject and puts them in the possibleAnswers array
        for (int index = 0; index < possibleAnswers.length; index++)
        {
            possibleAnswers[index] = Main.database.getAnswer(possibleQuestions[index]);
        }
        
        currentQ = 0;
        displayQuestionAndAnswers(currentQ);
    }
    
    /**
     * Handles user actions.
     * @param action the user action
     */
    public void actionPerformed(ActionEvent action)
    {
        if (action.getSource().equals(endBtn))
        {
            // ends quiz
            frame.setVisible(false);
            frame.dispose();
            Main.introFrame.setVisible(true);
        }
        else if (action.getSource().equals(checkAnsBtn))
        {
            currentQ++;
            
            // checks whether the user answered correctly
            for (int index = 0; index < radioBtn.length; index++)
            {
                // if the selected option is the correct answer
                if (radioBtn[index].isSelected() && radioBtn[index].getText().equals(Main.database.getAnswer(questionArea.getText())))
                {
                    answerCorrect = true;
                }
            }
            
            if (answerCorrect)
            {
                // increments score
                score++;
                JOptionPane.showMessageDialog(null, "Correct!");
                answerCorrect = false;
            }
            else
            {
                // displays correct answer
                JOptionPane.showMessageDialog(null, "Incorrect.\nThe correct answer is: " + Main.database.getAnswer(questionArea.getText()), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            if (currentQ < numberOfQuizQ)
            {
                // proceeds to next question
                label.setText("Question " + (currentQ + 1) + "/" + numberOfQuizQ + " in " + Subject.selectedSubject);
                displayQuestionAndAnswers(currentQ);
            }
            else if (currentQ == numberOfQuizQ)
            {
                // finishes quiz
                JOptionPane.showMessageDialog(null, "Finished! Your score is: " + score + "/" + numberOfQuizQ + ".");
                frame.dispose();
                Main.introFrame.setVisible(true);
            }
        }
    }
}
