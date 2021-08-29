//TODO : This class needs proper documentation
/*
Todo : delete leading and trailing spaces
todo delete all variables prefixed with 'tools_', they are inefficient and for creation processes only

todo : method for further processing of questions
 */
//todo Solve the issue of leading and trailing spaces
//todo : work on saving edited files

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Todo : Make a GUI of this stuff later
//Todo : regarding the upper todo, gui can be implemented slowly. with things like dialog boxes etc

/*

    @Author : Gideon Kane M.
    7th june 2021, This code is going to be the first
    version of a database maker for the nova project which wll
    soon be ported to all platforms. I will try to make this as generic as possible so i can port to other
    implementations and frameworks

    August 23rd here, can't believe this project is 3 months old already
 */
//for now main will serve as the GUI


public class FormaterEngine {
    private static File fileDirectory;
    private final int directory = 1;
    private final int file = 2;
    private final int error = 3;
    private boolean isFile = false;
    private boolean isDirectory = false;
    private boolean isError = false;
    private static List<Question> questionList;
    private int modal_option_count;
    private TextViewListener textViewListener;


    public FormaterEngine(String directory) {
        validateFilePath(directory);
    }

    private static Question questionParserEngine(Scanner questionParser) throws IncompatibleQuestionException {
        List<String> optionList = new ArrayList<>();
        StringBuilder questionStringBuilder = new StringBuilder();
        StringBuilder optionStringBuilder = new StringBuilder();
        String token;
        int optionCount = 0, optionTracker = 0;
        boolean optionNotEncountered = true; // no option reached yet

        while (questionParser.hasNext()) {
            token = questionParser.next();
            if (token.matches("[^\\w]?[a-eA-E][^\\w]?") && token.length() != 1) { //if (a. is found, or similar
                optionCount++;
                optionNotEncountered = false;
            } else {
                if (optionNotEncountered) {
                    questionStringBuilder.append(token).append(" ");
                } else {
                    if (optionCount > optionTracker) {
                        /*
                        If a new question is encountered, add the previous tokens up
                        and then  clear the former question builder
                         */
                        //keeping up with the count :)
                        if (optionTracker != 0) {
                            optionList.add(optionStringBuilder.toString());
                            optionStringBuilder.delete(0, optionStringBuilder.length() - 1);
                        }
                        optionStringBuilder.append(token).append(" ");
                        optionTracker++;
                    } else optionStringBuilder.append(token).append(" ");
                }
            }
        }
        //To ensure the last option is always added :
        optionList.add(optionStringBuilder.toString());
        return generateQuestion(questionStringBuilder.toString(), optionList);
    }

    public void setTextViewListener(TextViewListener textViewListener) {
        this.textViewListener = textViewListener;
    }

    private static void saveChangesAndRewriteFile() throws IOException {

        //This part c
        File rewriteFile;
        String file_name = "\\rewriteFile.txt";
        if (fileDirectory.isDirectory())
            rewriteFile = new File(fileDirectory.getPath() + file_name);
        else
            rewriteFile = new File(fileDirectory.getParent() + file_name);
        rewriteFile.createNewFile();
        StringBuilder question_line = new StringBuilder();
        String space = " ";

        int count = 0, questionCount = 1;
        FileWriter fileWriter = new FileWriter(rewriteFile, true);

        questionList.remove(questionList.size() - 1);

        for (Question question : questionList) {
            if (question != null) {
                char optionChar = 'A';
                question_line.append(questionCount).append(space).append(question.getQuestion()).append(space);
                for (String option : question.getOptionArray()) {
                    question_line.append(optionChar++).append(".").append(" ").append(option).append(space);
                }
                fileWriter.append(question_line).append("\n");
                fileWriter.append("\n");
                fileWriter.flush();
                question_line.replace(0, question_line.length() - 1, "");
                questionCount++;
            }
        }
        fileWriter.close();

    }

    public boolean getFileState() {
        return isFile || isDirectory;
    }

    private String showCorrectQuestionDialog(String readLine) {
        return textViewListener.updateTextView(readLine);
    }

    public List<File> getDirectoryList() throws IOException {
        //iterate through directory and display only textFiles
        List<File> textFilesList = new ArrayList<>();
        try {
            File textFile = fileDirectory;
            File[] fileList = textFile.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.getName().endsWith("txt")) textFilesList.add(file); // sort only the text files
                }
            }
        } catch (Exception e) {

        }
        return textFilesList;
    }

    private Question parseQuestionFromLine(String readLine, int line_count, int modal_option_count)
            throws IncompatibleQuestionException {
        Scanner questionParser = new Scanner(readLine);
        //readLine should be a question, all questions should start with a number
        int q_number = 0;
        String number = questionParser.next();
        String numberNum = "";
        if (number.contains(".")) numberNum = number.replace(".", ""); // e.g 4. A Boy.. will turn to just 4
        try {
            q_number = Integer.parseInt(numberNum);
        } catch (NumberFormatException e) {
            String message = "Error : it seems the number : " + "\"" + number + "\"" +
                    " at line " + line_count + " Seems to be incompatible \n " +
                    readLine;
            throw new IncompatibleQuestionException(message, IncompatibleQuestionException.QUESTION_NUMBERING_ERROR);
        }

        readLine = readLine.replace(number, "").trim();


        return questionParserEngine(questionParser);

    }

    private static Question generateQuestion(String questionString, List<String> optionList) {
        Question Question = new Question();
        Question.setQuestion(questionString);
        String[] optionArray = optionList.toArray(new String[0]);
        Question.setOptionFromArray(optionArray);
        return Question;
    }

    private static void generateQuestionList(Question question) {
        questionList.add(question);
    }

    private static boolean errorHandler(Question question, int normalOptionCount) {
        return question.optionInitialisedCount() < normalOptionCount;
    }

    public void formatFromFile(File questionFile) throws IOException, IncompatibleQuestionException {
        String file_name = questionFile.getName();
        System.out.println();
        System.out.println("Formatting process beginning for " + file_name);

        //main job
        try {
            /*
            The parser Class is just a class that contains a list of requirements to look out for while parsing
            View, Add or Delete requirements from the parser class
             */
            parseFileForLine(new ParserList(), new BufferedReader(new FileReader(questionFile)));
            //Todo: Continue Scanning even though an error is found, to show all possible errors
            /*
                if document isn't standardized then an error message is thrown,
                indicating the error and the cause,
             */

            // if scanning is successful, then continue with code
            int modal_number_of_options = modal_option_count;


            BufferedReader reader = new BufferedReader(new FileReader(questionFile));
            String readLine;
            String courseName = reader.readLine().replace("Course Name: ", "");
            String courseYear = reader.readLine().replace("Course Year: ", "");
            String version = reader.readLine().replace("Version: ", "");
            //at this point, line count should be at least 3

            int line_count = 3;
            boolean file_reached_end = false;
            questionList = new ArrayList<>();

            boolean questionValid = true;
            Question question;
            while (((readLine = reader.readLine()) != null) && !file_reached_end) {
                line_count++;
                if (!(readLine.length() < 2)) // if the length of the line read is less than 2, then skip,
                    if (!readLine.equalsIgnoreCase("file_end")) {
                        do {
                            questionValid = true;
                            question = parseQuestionFromLine(readLine, line_count, modal_number_of_options);

                            if (errorHandler(question, modal_option_count)) {
                                readLine = showCorrectQuestionDialog(readLine);
                                questionValid = false;
                            } else generateQuestionList(question);
                        } while (!questionValid);
                    } else file_reached_end = true;

            }
            System.out.println(file_name + " formatted successfully.\n " + "End of file reached = " + file_reached_end);

        } catch (IncompatibleFileException e) {
            e.printStackTrace();
            Scanner sc = new Scanner(System.in);
            System.err.println("Error occurred while parsing file, read stack trace and press enter");
            String placeholder = sc.nextLine();
            System.err.println("Ensure all parameters are satisfied");

        } catch (Exception e) {
            System.out.println("Oops, an error has occurred,  see stack trace below");
            e.printStackTrace();
        }
    }

    private static void parseFileForLine(ParserList parserList, BufferedReader reader) throws Exception {
        //This method ensures that the file contains all the required information as specified
        // by the file class.
        //See ParserList Class for more explanation
        String readLine;
        List<String> parseList = new ArrayList<>(parserList.parseList);

        while ((readLine = reader.readLine()) != null) {
            for (int i = 0; i < parseList.size(); i++)
                if (readLine.contains(parseList.get(i))) parseList.set(i, "found".toUpperCase());
        }

        for (String checker : parseList)
            if (!checker.equalsIgnoreCase("found"))
                throw new IncompatibleFileException(checker + " Not found");
        reader.close(); // free up resources
    }

    private void validateFilePath(String filePath) {
        try {
            fileDirectory = new File(filePath);
            if (fileDirectory.isDirectory())
                isDirectory = true;
            else if (fileDirectory.isFile()) {
                fileDirectory = fileDirectory.getParentFile();
                isFile = true;
            } else isError = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModalOptionCount(int modalOptionCount) {
        modal_option_count = modalOptionCount;
    }

    public void generateFormatFile() {
        FileHandler.generateDatabaseFromList(questionList);
    }
}
