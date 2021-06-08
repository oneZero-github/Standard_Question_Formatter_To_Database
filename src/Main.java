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

    private static void formatFromFile(File textFile) {
        String file_name = textFile.getName();
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Format file " + file_name + " ?");
        String formatFile = scanner.nextLine().toLowerCase();

        if (formatFile.contains("no")) {
            System.out.println(file_name + " skipped!");
            return; //stops the method if user doesn't want that file, gui implementation has to change
        }

        System.out.println();
        System.out.println("Formatting process beginning for " + file_name);


        //main job
        try {
            BufferedReader reader = new BufferedReader(new FileReader(textFile));
            String readLine = "";
            /*
            The parser Class is just a class that contains a list of requirements to look out for while parsing
            View, Add or Delete requirements from the parser class
             */

            parseFileForLine(new ParserList(), reader);

            while ((readLine = reader.readLine()) != null) {

            }
            System.out.println(file_name + " formatted successfully");

        }
        catch (IncompatibleFileException e){

        }
        catch (Exception e) {
            System.out.println("Oops, an error has occurred,  see stack trace below");
            e.printStackTrace();
        }
    }

    private static void parseFileForLine(ParserList parserList, BufferedReader reader) throws Exception{
        String readLine;
        while ((readLine = reader.readLine()) != null) {

        }
    }

    private static void formatFromDirectory(File textFile) {
        //iterate through directory and display only textFiles
        List<File> textFilesList = new ArrayList<>();
        File[] fileList = textFile.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.getName().endsWith("txt")) textFilesList.add(file);
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
            System.out.println();
        }
        askUserForFile(); // not needed during GUI implementation
    }
}
