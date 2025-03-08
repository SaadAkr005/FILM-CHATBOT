// MP LV 8
// Saad Sajid Akram 
// Student ID: 240169554
// 01/12/2024
// VERSION 1 
// 

import java.io.*; 
import java.util.*; // Needed to make Scanner available

class Question { // record storing questions
    String title; 
    String question; 
}

class QuestionDB {
    Question[] questionarray; 
    int nextFree; 
}
    
class cinebot 
{ 
    public static void main (String [] a) throws IOException 
    {
        boolean endLoop = false; 
        String name = askName(); 
        while (endLoop == false) {
            boolean userExists = checkExists(name);
            String film = getFilm(); 
            //greeting(name, userExists); 
            if (userExists) {
                boolean filmExists = checkFilm(name, film); 
                if (filmExists) {
                    System.out.println("We've discussed "+ film +" before!");  
                    // Check the users file for the film name and its question. 
                    // Checks the question and ensures a different one gets asked.
                    // If all have already been asked, it just moves on. 
                    String newQuestion = getNewQ(name, film); 
                    handleNewQ(name, newQuestion, film); 
                } else {
                    // Add a new entry for the film and ask a random question. Store it in named file. 
                    inputNewFilm(name,film); 
                } 
            } else {
                // Add name to list of names. Create a file for this name. Call inputNewFilm. 
                inputNewUser(name,film); 
            } 
            endLoop = getAnother(); 
        }
    } // END OF MAIN METHOD

    // Introduce itself and ask for the users name. 
    //
    public static String askName () {            
        System.out.println("Hi, I'm Cinebot!");
        String name = inputString("What is your name?");
        
        return name; 
    } // END OF METHOD askName 

    
    // Greets new user 
    /*
    public static void greeting(String name, boolean userExists) {
        if (userExists) {
            System.out.println("Welcome back " + name); 
        } else {
            System.out.println("Nice to meet you " + name); 
        }
    }*/

    // Asks for movie user wants to discuss 
    // 
    public static String getFilm (){ 
        String fav_film = inputString("What film do you want to talk about?"); 
        
        return fav_film; 
    } // END OF METHOD userFav 

    // GET A RANDOM QUESTION ABOUT FILM
    public static String getQuestion() {
        String[] questions = {"Favourite actor?", "Best moment?", "Best line?"}; 
        String q = genRandQ(questions);
        return q; 
    }
    
    // ASKS A USER A RANDOM QUESTION AND RESPONDS. 
    public static String getResponse(String q, String fav) {
        System.out.println("Ahh so you want to discuss " + fav + " do you? Great!"); 
        String response = inputString(q); 
        System.out.println("Hmmm... Interesting response!"); 
        return response; 
    } 

    // GENERATES A RANDOM QUESTION BASED ON QUESTIONS ARRAY PASSED IN 
    public static String genRandQ(String[] qs) {
        final int UPPER = qs.length; 
        Random random = new Random(); 
        int index = random.nextInt(UPPER); 
        return qs[index]; 
    }

    // Checks if user exists in a text file.  
    public static boolean checkExists(String name) throws IOException {
        BufferedReader checkName = new BufferedReader (new FileReader("names.txt")); 
        String s = checkName.readLine(); 
        while (s != null) {
            if (s.trim().equals(name)) {
                checkName.close(); 
                return true; 
            }
            s = checkName.readLine(); 
        }
        checkName.close(); 
        return false; 
    }

    // CHECKS IF A FILM EXISTS WITHIN A CSV FILE 
    public static boolean checkFilm(String name, String film) throws IOException {
        BufferedReader inputStream = new BufferedReader (new FileReader(name + ".csv"));
        String question = inputStream.readLine(); 
        while(question!=null) { 
            String[] question_components = question.split(","); 
            String title = question_components[0]; 
            if (film.equals(title)){
                inputStream.close(); 
                return true; 
            } else {
                question = inputStream.readLine(); 
            }
        }
        inputStream.close(); 
        return false; 
    }

    public static String getNewQ(String name, String film) throws IOException {
        BufferedReader inputStream = new BufferedReader(new FileReader(name + ".csv")); 
        String question = inputStream.readLine(); 
        QuestionDB asked = createNewDB(); 
        while(question!=null) {
            String[] question_components = question.split(","); 
            addDetails(asked, question_components[0], question_components[1]);  
            question = inputStream.readLine(); 
        }
        inputStream.close();
        // we will now check if any element matches fully with the array below. 
        String[] questions = {"Favourite actor?", "Best moment?", "Best line?"}; 
        for (int i=0; i<questions.length; i++) {
            String q = questions[i]; 
            boolean haveAsked = false; 
            for (int j =0; j<asked.nextFree; j++) {
                if (getTitle(asked.questionarray[j]).equals(film) && getQuestion(asked.questionarray[j]).equals(q)) {
                    haveAsked = true; 
                    break; 
                }
            }
            if (haveAsked==false) {
                return q; 
            }
        }
        return null; 
    }

    public static void handleNewQ(String name, String newQ, String film) throws IOException {
        if (newQ == null) {
            System.out.println("Cool. Let's move on to another film."); 
        } else {
            String response = inputString(newQ); 
            System.out.println("Hmm.. Interesting."); 
            // If a new question is asked, save this question in the name's CSV file
            PrintWriter file = new PrintWriter(new FileWriter(name + ".csv", true));  // Open the file for appending
            file.println(film + "," + newQ);  // Save the film, question
            file.close();
        }
    }

    public static void inputNewFilm(String name, String film) throws IOException {
        String question = getQuestion(); 
        String response = getResponse(question, film); 
        PrintWriter file = new PrintWriter(new FileWriter(name + ".csv", true)); 
        file.println(film + "," + question); 
        file.close(); 
    }

    public static void inputNewUser(String name, String film) throws IOException {
        PrintWriter file = new PrintWriter(new FileWriter("names.txt", true)); 
        file.println(name); 
        file.close(); 
        inputNewFilm(name, film); 
    }
    
   
    // ASKS IF USER WOULD LIKE TO ENTER ANOTHER FAV FILM 
    public static boolean getAnother() {
        String response = inputString("Do you have another movie you'd like to discuss? (Y/N)"); 
        if (response.equals("N")) {
            return true; 
        } 
        return false; 
    }
     
    // ALLOWS FOR INPUT OF A STRING 
    public static String inputString(String msg) {
        Scanner scanner = new Scanner(System.in); 
        System.out.println(msg); 
        return scanner.nextLine().trim(); 
    }

        // ----------- METHOD FOR ADT QuestionDB ------- 
    /* Operations: 1. createNewDB() 
                   2. addDetails()
                   3. addToDB */

    // 1. Creates a new database of questions and returns it. 
    public static QuestionDB createNewDB() {
        QuestionDB db = new QuestionDB(); 
        db.questionarray = new Question[100]; 
        db.nextFree = 0; 
        return db; 
    }

    // 2. adds details of new q to question database. 
    public static QuestionDB addDetails(QuestionDB qdb, String film, String q) {
        Question Q = createQ(film, q); 
        addToDB(qdb, Q); 
        return qdb; 
    }

    // 3. adds a new question to the database. 
    public static QuestionDB addToDB(QuestionDB db, Question q) {
        Question[] qarray = db.questionarray; 
        if (db.nextFree < qarray.length) {
            db.questionarray[db.nextFree] = q; 
            db.nextFree += 1; 
        }
        return db; 
    }

    // ------- ACCESSOR METHODS FOR RECORD QUESTION ----- 
    // CREATE NEW RECORD Q.
    public static Question createQ(String film, String question) {
        Question q = new Question(); 
        q = setFilmName(q, film); 
        q= setQuestion(q, question); 
        return q; 
    }

    // SETTERS.
    public static Question setFilmName(Question q, String film) {
        q.title = film; 
        return q; 
    }

    public static Question setQuestion(Question q, String question) {
        q.question = question; 
        return q; 
    }

    // GETTERS 
    public static String getTitle(Question q) {
        return q.title; 
    }

    public static String getQuestion(Question q) {
        return q.question; 
    }
    
} // END OF CLASS Cinebot

