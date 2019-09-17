import java.sql.*;
/**
 * Connects and interacts with a database of subjects, questions, and answers. 
 *
 * @Talia Wang
 * @2019
 */
public class Database
{
        // static variables
        private static Connection c;
        private static String databaseName;
        private static String url;
        protected static ResultSet rs;
        protected static Statement st;
    
        /**
         * Constructor for this database.
         * Creates a connection to the database.
         */
        public Database()
        {
            try
            {  
                databaseName = "ICS_IA_database.accdb"; // database exists in the current directory  
  
                url= "jdbc:ucanaccess://" + databaseName;
                    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");  
                c = DriverManager.getConnection(url);  
                st = c.createStatement();  
            }catch(Exception ee){System.out.println(ee);}  
        }
        
        /**
         * Changes the name of the specified subject to another name.
         * @param oldSubjectName the subject's previous name
         * @param newSubjectName the subject's new name
         */
        public void changeSubjectName(String oldSubjectName, String newSubjectName)
        {
            int updateCount = 0;
            
            try
            {
                  updateCount = st.executeUpdate("UPDATE QuizInformation SET Subject = '" + newSubjectName + "' WHERE Subject = '" + oldSubjectName + "'");
            }catch(Exception ee){System.out.println(ee);}  
        }
        
        /**
         * Deletes the specified subject, and all of its questions and answers, from the database. 
         * @param subjectName the name of the subject to be deleted
         * @return the number of records deleted from the database
         */
        public int deleteSubject(String subjectName)
        {
            int deleteCount = 0;
            try
            {
                deleteCount = st.executeUpdate("DELETE * FROM QuizInformation WHERE Subject = '" + subjectName + "'");
            }catch(Exception ee){System.out.println(ee);}  
            return deleteCount;
        }
        
        /**
         * Deletes the specified question and its corresponding answer.
         * @param question the question to be deleted, along with its answer
         */
        public void deleteQA(String question)
        {
            int deleteCount = 0;
            try
            {
                deleteCount = st.executeUpdate("DELETE * FROM QuizInformation WHERE Question = '" + question + "'");
            }catch(Exception ee){System.out.println(ee);}  
        }
        
        /**
         * Gets the corresponding answer to a question in the database.
         * @param question the question for which an answer is retrieved
         * @return the answer to the specified question
         */
        public String getAnswer(String question)
        {
            String answer = new String("");
            try
            {
                rs = st.executeQuery("SELECT * FROM QuizInformation WHERE Question = '" + question + "'");
                while(rs.next())
                {  
                    answer = rs.getString("Answer");
                } 
            }catch(Exception ee){System.out.println(ee);}  
            return answer;
        }
        
        /**
         * Gets the number of questions and answers of a specific subject from the database.
         * @param subjectName the subject from which to retrieve questions and answers
         * @return the number of questions and answers in the specified subject
         */
        public int getNumberOfQA(String subjectName)
        {
            int numberOfQA = 0;
            try
            {
                rs = st.executeQuery("SELECT COUNT(*) FROM QuizInformation WHERE Subject = '" + subjectName + "'");
                while(rs.next())
                {  
                    numberOfQA = rs.getInt(1);
                } 
            }catch(Exception ee){System.out.println(ee);}  
            return numberOfQA;
        }
        
        /**
         * Gets all the questions of a specific subject from the database.
         * @param subjectName the subject from which to retrieve all questions
         * @param arrayLength the length of the array to be returned
         * @return an array with all of the subject's questions and answers 
         */
        public String[] getAllQuestions(String subjectName, int arrayLength)
        {
            int index = 0;
            String[] array = new String[arrayLength];
            try
            {
                rs = st.executeQuery("SELECT * FROM QuizInformation WHERE Subject = '" + subjectName + "'");
                while(rs.next())
                {  
                    array[index] = rs.getString("Question");
                    index++;
                } 
            }catch(Exception ee){System.out.println(ee);}
            
            return array;
        }
        
        /**
         * Inserts a new question and answer of a specific subject into the database.
         * @param question the question to be added
         * @param answer the answer to be added
         * @param subjectName the subject in which to add a question and answer
         */
        public void insertQA(String question, String answer, String subjectName)
        {
            int modified = 0;
            try
            {
                modified = st.executeUpdate("INSERT INTO QuizInformation VALUES ('" + question + "', '" + answer + "', '" + subjectName + "')");
            }catch(Exception ee){System.out.println(ee);}  
        }
        
        /**
         * Checks whether a specific question already exists in the database.
         * @param question the question to be checked for duplicates
         * @return whether the question already exists
         */
        public boolean questionAlreadyExists (String question)
        {
            try
            {
                rs = st.executeQuery("SELECT * FROM QuizInformation");
                while(rs.next())
                {  
                    if (question.equals(rs.getString("Question")))
                    {
                        return true;
                    }
                } 
            }
            catch(Exception ee){System.out.println(ee);}
            return false;
        }
        
        /**
         * Updates the specified answer in the database with a new answer.
         * @param question the question to which to change the answer
         * @param newAnswer the new answer to the question
         */
        public void updateAnswer (String question, String newAnswer)
        {
            int updateCount = 0;
            try
            {
                if (!newAnswer.equals(""))
                {
                    updateCount = st.executeUpdate("UPDATE QuizInformation SET Answer = '" + newAnswer + "' WHERE Question = '" + question + "'");
                }
            }catch(Exception ee){System.out.println(ee);}  
        }
        
        /**
         * Updates the specified question in the database with a new question.
         * @param oldQuestion the question to be replaced
         * @param newQuestion the question to replace the old question
         */
        public void updateQuestion (String oldQuestion, String newQuestion)
        {
            int updateCount = 0;
            
            try
            {
                if (!newQuestion.equals(""))
                {
                    updateCount = st.executeUpdate("UPDATE QuizInformation SET Question = '" + newQuestion + "' WHERE Question = '" + oldQuestion + "'");
                }
            }catch(Exception ee){System.out.println(ee);}  
        }
}
