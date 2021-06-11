import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int isDirectory = 1;
    private static final int isFile = 2;
    private static final int isError = 3;
    private static File textFile;


    public static void main(String[] args) {
        //Todo : Make a GUI of this stuff later

        /*
            @Author : Gideon Kane M.
            7th june 2021, This code is going to be the first
            version of a database maker for the nova project which wll
            soon be ported to all platforms. I will try to make this as generic as possible so i can port to other
            implementations and frameworks
         */
        //for now main will serve as the GUI

        setUpInterface();
        askUserForFile();
    }

    private static void askUserForFile() {
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

    private static void formatFromDirectory(File textFile) {
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

        //Todo : create method to iterate through all files

        System.out.println();

        for (File file : textFilesList) {
            formatFromFile(file);
        }
    }

    private static void formatFromFile(File textFile) {
        String file_name = textFile.getName();
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
            parseFileForLine(new ParserList(), new BufferedReader(new FileReader(textFile)));
            //Todo: Continue Scanning even though an error is found, to show all possible errors

            /*
                if document isn't standardized then an error message is thrown,
                indicating the error and the cause,
             */

            // if scanning is successful, then continue with code
            int modal_number_of_options;
            while(true){
                System.out.println("Please input the modal number of options per question >>>");
                userInput = scanner.nextLine();
                if(userInput.matches("[2-6]")) {
                    modal_number_of_options = Integer.parseInt(userInput);
                    break;
                }
                else System.err.println("\n" + "The input \"" + userInput +
                        "\" is invalid, try again");
            }

            BufferedReader reader = new BufferedReader(new FileReader(textFile));
            String readLine = "";
            String courseName = reader.readLine().replace("Course Name: ", "");
            String courseYear = reader.readLine().replace("Course Year: ", "");
            String version = reader.readLine().replace("Version: ", "");
            //at this point, line count should be at least 3


            int line_count = 3;
            boolean file_reached_end = false;
            while (((readLine = reader.readLine()) != null) && !file_reached_end) {
                line_count++;
                if(! (readLine.length() < 2)) // if the length of the line read is less than 2, then skip,
                    if(!readLine.equalsIgnoreCase("file_end")){
                        parseQuestionFromLine(readLine,line_count, modal_number_of_options);
                    }else file_reached_end = true;

            }
            System.out.println(file_name + " formatted successfully.\n " + "End of file reached = " + file_reached_end );

        } catch (IncompatibleFileException e) {
            e.printStackTrace();
            Scanner sc = new Scanner(System.in);
            System.err.println("Error occurred while parsing file, read stack trace and press enter");
            String placeholder = sc.nextLine();
            System.err.println("Ensure all parameters are satisfied");

        }
        catch (IncompatibleQuestionException e){
            e.printStackTrace();

        }
        catch (Exception e) {
            System.out.println("Oops, an error has occurred,  see stack trace below");
            e.printStackTrace();
        }
    }

    private static void parseQuestionFromLine(String readLine, int line_count, int modal_question_count) throws IncompatibleQuestionException{
        Scanner questionParser = new Scanner(readLine);
        //readLine should be a question, all questions should start with a number
        int q_number = 0;
        String number =questionParser.next();
        if(number.contains(".")) number =  number.replace(".", ""); // e.g 4. A Boy.. will turn to just 4
        try {
            q_number = Integer.parseInt(number);
        } catch (NumberFormatException e ){
            String message = "Error : it seems the number : " + "\"" + number + "\"" +
                                " at line " + line_count + " Seems to be incompatible \n " +
                            readLine;
           throw new IncompatibleQuestionException(message);
        }
        //if Number is parsed successfully then question should be parsed next

        List<String> optionList =  new ArrayList<>();
        StringBuilder questionStringBuilder = new StringBuilder();
        StringBuilder optionStringBuilder = new StringBuilder();
        String token;
        int optionCount = 0, optionTracker = 0;
        boolean optionNotEncountered = true; // no option reached yet

        while(questionParser.hasNext()) {
            token = questionParser.next();
            if(token.matches("[^\\w]?[a-eA-E][^\\w]?") && token.length() !=1){ //if (a. is found, or similar
                 optionCount++;
                //todo: add more than here
                errorHandler(readLine, optionCount, modal_question_count, line_count,true);
                //a quick glance at the code should make you understand the above line

                //option might be found
                optionNotEncountered = false;
            }else{
                if(optionNotEncountered){
                    questionStringBuilder.append(token).append(" ");
                }else{
                    if(optionCount > optionTracker){
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
                    }else optionStringBuilder.append(token).append(" ");
                }
            }
        }
        //To ensure the last option is always added :
        optionList.add(optionStringBuilder.toString());

        boolean enableSmartFix = errorHandler(readLine.replace(number,""),optionCount,modal_question_count,line_count, false);
        /*
        The replace number with "",ensures only the question gets sent to the smart_fix parser
         */
        if(enableSmartFix) smartParseQuestionFromLine(readLine,modal_question_count,optionList,optionStringBuilder,questionStringBuilder, line_count);
        System.out.println(questionStringBuilder);
        for(String test : optionList) System.out.println(test);


    }

    private static void smartParseQuestionFromLine(String readLine, int modal_question_count,
                                                   List<String> optionList, StringBuilder optionStringBuilder,
                                                   StringBuilder questionStringBuilder,int linecount) throws IncompatibleQuestionException {
        /*
        This method greatly resembles the 'ordinary' option parser except that it is
        a bit more out there, see the first implementation for more details
         */

        Scanner smartQuestionParser = new Scanner(readLine);
        Scanner userInput = new Scanner(System.in);
        List<String>  smart_optionList =  new ArrayList<>();
        StringBuilder smart_questionStringBuilder = new StringBuilder();
        StringBuilder smart_optionStringBuilder = new StringBuilder();
        String smart_token = "";
        boolean optionFound = false;
        int option_count = 0, option_tracker = 0;
        StringBuilder readLineIndexHack = new StringBuilder(readLine);
        while(smartQuestionParser.hasNext()){
            smart_token = smartQuestionParser.next();
            if(smart_token.matches("[^\\w]*[a-eA-E]?[^\\w]*")){
                int index_of_zone = readLineIndexHack.indexOf(smart_token);
                System.out.println("Is the token >" +smart_token + "< at zone >" + readLineIndexHack.substring((index_of_zone -1), (index_of_zone+2)) +
                        "< an Option. Type Y/N");
                readLineIndexHack.delete(0,readLineIndexHack.indexOf(smart_token) +1);

                System.out.println(readLine);
                if ("y".equals(userInput.nextLine().toLowerCase())) {
                        optionFound = true;
                        option_count++;
                } else {

                }
            }else if(optionFound){
                if(option_count > option_tracker){
                    if (option_tracker != 0) {
                        smart_optionList.add(smart_optionStringBuilder.toString());
                       smart_optionStringBuilder.delete(0, smart_optionStringBuilder.length() - 1);
                    }
                    smart_optionStringBuilder.append(smart_token).append(" ");
                    option_tracker++;
                }else smart_optionStringBuilder.append(smart_token).append(" ");
                }
            }
        //To ensure the last option is always added :
        smart_optionList.add(smart_optionStringBuilder.toString());

        for(String option: smart_optionList) System.out.println("Found option :" +option);

        while(true){
            System.out.println("Are you satisfied with the result ? 1 for yes 2 for no");
            String userInputString = new Scanner(System.in).nextLine();
            if(userInputString.matches("[1-2]")) {
                if ("2".equals(userInputString)) {
                    throw new IncompatibleQuestionException("Error in line :" + linecount +   "\n" + readLine);
                }
                break;
            }else System.err.println("\n" + "The input \"" + userInputString +"\" is invalid, try again");
        }
        }



    private static boolean errorHandler(String readLine, int optionCount, int modal_question_count,
                                     int line_count, boolean checkIfGreater) throws IncompatibleQuestionException {
        boolean enableSmartFix = false;
        if((optionCount > modal_question_count) && checkIfGreater){
            while(true){
                System.out.println("The option count has exceeded " + modal_question_count+
                        "At question : " + readLine +"\nAt line : " + line_count
                        + "\nPress 1 to continue add option \nPress 2 to discard option\nPress 3 to terminate");
                String userInput = new Scanner(System.in).nextLine();
                if(userInput.matches("[1-3]")) {
                    switch (userInput){
                        case "1":
                            break;// do nothing for case 1
                        case "2":
                            break;
                        case "3":
                            throw new IncompatibleQuestionException("Error in line :"+ line_count +
                                    "\n" + readLine);
                    }
                    break;
                }else System.err.println("\n" + "The input \"" + userInput +"\" is invalid, try again");
            }
            //end of error handling while loop
        }// end of error handling
        boolean checkIfSmaller = !checkIfGreater;
        if(optionCount < modal_question_count  && checkIfSmaller) {// start of error handling
            while(true) {
                System.err.println("The parser could only find " + optionCount + " options in\n" +
                        "Question: " + readLine + "\nAt line " + line_count + "\n Press 1 to continue" +
                        "\nPress 2 to attempt smartFix " +"\nPress 3 to terminate");
                System.err.println("Make sure all options have either a parenthesis or full stop after" +
                        "the option letter");
                String input = new Scanner(System.in).nextLine();
                if(input.matches("[1-3]")) {//multiple break statements in an if -block isn't ideal
                    // todo: refractor to using switch later and delete guiding comment
                    if (input.equals("3")) {
                        throw new IncompatibleQuestionException("Question at line :" + line_count +
                                " doesn't have up to " + optionCount + " options\n " + "At : " +
                                readLine);
                    }else if(input.equals("2")) {
                        enableSmartFix = true;
                        break;
                        // to enable the smartfix method to be called without affecting the stack
                    }else break;

                }else{
                    System.out.println("Please input 1-3");
                }
            }

        }// end of error handling

        return  enableSmartFix;
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

    private static void errorFindingFileFromPath() {
        System.out.println("An error has occurred finding the specified file... try again");
        for (int i = 0; i < 2; i++) {
            System.out.println();// Lol, sometimes i want to be funny, life is short
        }
        askUserForFile(); // not needed during GUI implementation
    }
}
