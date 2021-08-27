//TODO : This class needs proper documentation
/*
Todo : delete leading and trailing spaces
todo delete all variables prefixed with 'tools_', they are inefficient and for creation processes only

todo : method for further processing of questions
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int isDirectory = 1;
    private static final int isFile = 2;
    private static final int isError = 3;
    private static File textFile;
    private static List<Question> questionList;


    public static void main(String[] args) {
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

        setUpInterface();

        try {
            askUserForFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Question question : questionList)
            System.out.println(question.toString());
        FileMaker.generateDatabaseFromList(questionList);

    }

    private static void askUserForFile() throws IOException {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please input the path of the text file of questions   >");
        String filePath = userInput.nextLine();

        switch (validateFilePath(filePath)) {
            case isDirectory:
                formatFromDirectory(textFile);
                break;

            case isFile:
                formatFromFile(textFile);
                break;

            case isError:
                errorFindingFileFromPath();
                break;

            default:
                break;
        }
    }

    private static void setUpInterface() {
    }

    private static void formatFromDirectory(File textFile) throws IOException {
        //iterate through directory and display only textFiles
        List<File> textFilesList = new ArrayList<>();
        File[] fileList = textFile.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.getName().endsWith("txt")) textFilesList.add(file); // sort only the text files
            }
        }

        System.out.println("\n The files in the directory are :  ");
        int fileIndexCount = 0;
        for (File file : textFilesList) {
            System.out.println(fileIndexCount++ + " :" + file.getName());
        }

        System.out.println();
        for (File file : textFilesList) {
            formatFromFile(file);
        }
    }

    private static void formatFromFile(File questionFile) throws IOException {
        String file_name = questionFile.getName();
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Format file " + file_name + " ?");
        String userInput = scanner.nextLine().toLowerCase();

        if (userInput.contains("no")) {
            System.out.println(file_name + " skipped!");
            return; //stops the method if user doesn't want that file, gui implementation has to change
        }

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
            int modal_number_of_options;
            System.out.println("Launch NumberEditor  ?");
            userInput = scanner.nextLine();
            if (userInput.matches("yes")) {
                questionFile = FileMaker.numberEditor(questionFile);
            }


            while (true) {
                System.out.println("Please input the modal number of options per question >>>");
                userInput = scanner.nextLine();
                if (userInput.matches("[2-6]")) {
                    modal_number_of_options = Integer.parseInt(userInput);
                    break;
                } else System.err.println("\n" + "The input \"" + userInput +
                        "\" is invalid, try again");
            }

            BufferedReader reader = new BufferedReader(new FileReader(questionFile));
            String readLine;
            String courseName = reader.readLine().replace("Course Name: ", "");
            String courseYear = reader.readLine().replace("Course Year: ", "");
            String version = reader.readLine().replace("Version: ", "");
            //at this point, line count should be at least 3

            int line_count = 3;
            boolean file_reached_end = false;
            questionList = new ArrayList<>();

            while (((readLine = reader.readLine()) != null) && !file_reached_end) {
                line_count++;
                if (!(readLine.length() < 2)) // if the length of the line read is less than 2, then skip,
                    if (!readLine.equalsIgnoreCase("file_end")) {
                        parseQuestionFromLine(readLine, line_count, modal_number_of_options);
                    } else file_reached_end = true;

            }
            System.out.println(file_name + " formatted successfully.\n " + "End of file reached = " + file_reached_end);

        } catch (IncompatibleFileException e) {
            e.printStackTrace();
            Scanner sc = new Scanner(System.in);
            System.err.println("Error occurred while parsing file, read stack trace and press enter");
            String placeholder = sc.nextLine();
            System.err.println("Ensure all parameters are satisfied");

        } catch (IncompatibleQuestionException e) {
            e.printStackTrace();
            String dialogQuestion = "Do you want to rewrite the file using the successfully parsed questions\n" +
                    "This saves changes from smart fix\nPress 1 for yes\n 2 for no";

            while (true) {
                System.out.println(dialogQuestion);
                userInput = scanner.nextLine();
                if (userInput.matches("[1-2]")) {
                    if (userInput.equals("1")) saveChangesAndRewriteFile();
                    break;
                } else System.err.println("\n" + "The input \"" + userInput +
                        "\" is invalid, try again");
            }

        } catch (Exception e) {
            System.out.println("Oops, an error has occurred,  see stack trace below");
            e.printStackTrace();
        }
    }

    private static void parseQuestionFromLine(String readLine, int line_count, int modal_question_count)
            throws IncompatibleQuestionException {
        Scanner questionParser = new Scanner(readLine);
        //readLine should be a question, all questions should start with a number
        int q_number = 0;
        String number = questionParser.next();
        if (number.contains(".")) number = number.replace(".", ""); // e.g 4. A Boy.. will turn to just 4
        try {
            q_number = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            String message = "Error : it seems the number : " + "\"" + number + "\"" +
                    " at line " + line_count + " Seems to be incompatible \n " +
                    readLine;
            throw new IncompatibleQuestionException(message);
        }
        //if Number is parsed successfully then question should be parsed next
    readLine.replace(number, "");// removing the number from the question tag

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
                //todo: add more than here
                errorHandler(readLine, optionCount, modal_question_count, line_count, true);
                //a quick glance at the code should make you understand the above line

                //option might be found
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

        boolean enableSmartFix = errorHandler(readLine.replace(number, ""), optionCount, modal_question_count,
                line_count, false);
        /*
        The replace number with "",ensures only the question gets sent to the smart_fix parser
         */
        if (enableSmartFix)
            smartParseQuestionFromLine(readLine, modal_question_count, optionList, optionStringBuilder,
                    questionStringBuilder, line_count);
        else generateQuestionList(questionStringBuilder, optionList);

        System.out.println(questionStringBuilder);

    }

    private static void generateQuestionList(StringBuilder questionStringBuilder, List<String> optionList) {
        Question question = new Question();
        question.setQuestion(questionStringBuilder.toString());
        String[] optionArray = optionList.toArray(new String[0]);
        question.setOptionFromOptionArray(optionArray);
        questionList.add(question);
    }

    private static void smartParseQuestionFromLine(String readLine, int modal_question_count,
                                                   List<String> optionList, StringBuilder optionStringBuilder,
                                                   StringBuilder questionStringBuilder, int lineCount)
            throws IncompatibleQuestionException {
        /*
        This method greatly resembles the 'ordinary' option parser except that it is
        a bit more out there, see the first implementation for more details
         */

        boolean loopThis = false;
        List<String> smart_optionList;

        do {
            loopThis = false;
            Scanner smartQuestionParser = new Scanner(readLine);
            Scanner userInput = new Scanner(System.in);
            smart_optionList = new ArrayList<>();
            StringBuilder smart_questionStringBuilder = new StringBuilder();
            StringBuilder smart_optionStringBuilder = new StringBuilder();
            String smart_token;
            boolean optionFound = false, questionParserContinue = true;
            int option_count = 0, option_tracker = 0;

            //There is a bug because the readLineHack index doesn't follow the readLine Scanner
            //hence the introduction of a new variable to keep track
            int readLineHackCounter = -1;
            String zone;
            // todo: make word counter and return a=word and surrounding
            StringBuilder readLineIndexHack = new StringBuilder(readLine);
            while (smartQuestionParser.hasNext()) {
                readLineHackCounter++; //
                smart_token = smartQuestionParser.next();
                if (questionParserContinue) smart_questionStringBuilder.append(smart_token).append(" ");

                if (smart_token.matches("[^\\w]*[a-eA-E]?[^\\w]*")) {
                    zone = readLine.split(" ")[readLineHackCounter];

                    int index_of_zone = readLineIndexHack.indexOf(smart_token);
                    System.out.println("Is the token " + smart_token + " an option? Type Y/N");

                    readLineIndexHack.delete(0, readLineIndexHack.indexOf(smart_token) + 1);

                    System.out.println(readLine);
                    if ("y".equalsIgnoreCase(userInput.nextLine())) {
                        optionFound = true;
                        questionParserContinue = false;
                        option_count++;
                    } else {

                    }

                } else if (optionFound) {
                    if (option_count > option_tracker) {
                        if (option_tracker != 0) {
                            smart_optionList.add(smart_optionStringBuilder.toString());
                            smart_optionStringBuilder.delete(0, smart_optionStringBuilder.length() - 1);
                        }
                        smart_optionStringBuilder.append(smart_token).append(" ");
                        option_tracker++;
                    } else smart_optionStringBuilder.append(smart_token).append(" ");
                }
            }
            //To ensure the last option is always added :
            smart_optionList.add(smart_optionStringBuilder.toString());

            for (String option : smart_optionList) System.out.println("Found option :" + option);
            System.out.println(smart_questionStringBuilder);

            //check if the problem is just no space after option e.g A.cholesterol
            if (smart_optionList.size() < 4) {
                System.out.println("Is the problem a conjoined option tag and content" +
                        " in just a single option? ");
                String userInputString = userInput.nextLine();
                if (userInputString.toLowerCase().contains("y")) {
                    System.out.println();
                    System.out.println("Input the index you want the space to be appended to");
                    int append = userInput.nextInt();
                    StringBuilder readLineBuilder = new StringBuilder(readLine);
                    readLineBuilder.insert(append, " ");
                    readLine = readLineBuilder.toString();
                    loopThis = true;
                }
            }
        }while(loopThis);

        while (true) {
            System.out.println("Are you satisfied with the result ? 1 for yes 2 for no");
            String userInputString = new Scanner(System.in).nextLine();
            if (userInputString.matches("[1-2]")) {
                generateQuestionList(questionStringBuilder, smart_optionList);
                if ("2".equals(userInputString)) {
                    throw new IncompatibleQuestionException("Error in line :" + lineCount + "\n" + readLine);
                }
                break;
            } else System.err.println("\n" + "The input \"" + userInputString + "\" is invalid, try again");
        }
    }

    private static boolean errorHandler(String readLine, int optionCount, int modal_question_count,
                                        int line_count, boolean checkIfGreater) throws IncompatibleQuestionException {
        boolean enableSmartFix = false;
        if ((optionCount > modal_question_count) && checkIfGreater) {
            while (true) {
                System.out.println("The option count has exceeded " + modal_question_count +
                        "At question : " + readLine + "\nAt line : " + line_count
                        + "\nPress 1 to continue add option \nPress 2 to discard option greater than " + modal_question_count
                        + "\nPress 3 to terminate");
                String userInput = new Scanner(System.in).nextLine();
                if (userInput.matches("[1-3]")) {
                    switch (userInput) {
                        case "1":
                            break;// do nothing for case 1
                        case "2":
                            break;
                        case "3":
                            throw new IncompatibleQuestionException("Error in line :" + line_count +
                                    "\n" + readLine);
                    }
                    break;
                } else System.err.println("\n" + "The input \"" + userInput + "\" is invalid, try again");
            }
            //end of error handling while loop
        }// end of error handling
        boolean checkIfSmaller = !checkIfGreater;
        if (optionCount < modal_question_count && checkIfSmaller) {// start of error handling
            while (true) {
                System.err.println("The parser could only find " + optionCount + " options in\n" +
                        "Question: " + readLine + "\nAt line " + line_count + "\n Press 1 to continue" +
                        "\nPress 2 to attempt smartFix " + "\nPress 3 to terminate");
                System.err.println("Make sure all options have either a parenthesis or full stop after" +
                        "the option letter");
                String input = new Scanner(System.in).nextLine();
                if (input.matches("[1-3]")) {//multiple break statements in an if -block isn't ideal
                    // todo: refactor to using switch later and delete guiding comment
                    if (input.equals("3")) {
                        throw new IncompatibleQuestionException("Question at line :" + line_count +
                                " doesn't have up to " + optionCount + " options\n " + "At : " +
                                readLine);
                    } else if (input.equals("2")) {
                        enableSmartFix = true;
                        break;
                        // to enable the smart fix method to be called without affecting the stack
                    } else break;

                } else {
                    System.out.println("Please input 1-3");
                }
            }

        }// end of error handling
        return enableSmartFix;
    }

    private static void saveChangesAndRewriteFile() throws IOException {

        //This part c
        File rewriteFile;
        String file_name = "\\rewriteFile.txt";
        if (textFile.isDirectory())
            rewriteFile = new File(textFile.getPath() + file_name);
        else
            rewriteFile = new File(textFile.getParent() + file_name);
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

    private static int validateFilePath(String filePath) {
        int returnVal;
        try {
            textFile = new File(filePath);
            if (textFile.isDirectory())
                returnVal = isDirectory;
            else if (textFile.isFile())
                returnVal = isFile;
            else returnVal = isError;
        } catch (Exception e) {
            e.printStackTrace();
            returnVal = isError;
        }

        System.out.println(returnVal);
        return returnVal;
    }

    private static void errorFindingFileFromPath() throws IOException {
        System.out.println("An error has occurred finding the specified file... try again");
        for (int i = 0; i < 2; i++) {
            System.out.println();// Lol, sometimes i want to be funny, life is short
        }
        askUserForFile(); // not needed during GUI implementation
    }
}
