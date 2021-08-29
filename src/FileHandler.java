import java.io.*;
import java.util.List;

public class FileHandler {
    //todo : update the writer and reader to handle more years, so for now, this is a skeletal implementation
    private static int counter = 0;

    static void generateDatabaseFromList(List<Question> questionList) {
        String newLine = "\n";
        try {
            int random = (int) (Math.random() * 100);
            File questionText = new File("C:\\Users\\HiddenLeaf\\Desktop\\CodeLab\\AndroidProjects\\" +
                    "NovaDataBase\\DataBaseTextFiles\\questionOutput" + random + ".txt");
            questionText.createNewFile();

            PrintWriter printWriter = new PrintWriter(questionText);
            boolean tools_courseNameWritten = false;
            for (Question question : questionList) {
                if (!tools_courseNameWritten) {
                    printWriter.write("Course Name: " + question.getCourseName() + newLine);
                    printWriter.write(("Course Year: " + question.getYearOfCourse()) + newLine);
                    printWriter.write("\n");
                    tools_courseNameWritten = true;
                }
            }
            int qNumber = 0;
            for (Question question : questionList) {
                printWriter.write("q" + ++qNumber + newLine);
                printWriter.write("Question: " + question.getQuestion() + newLine);
                printWriter.write("Option A: " + question.getOptionA() + newLine);
                printWriter.write("Option B: " + question.getOptionB() + newLine);
                printWriter.write("Option C: " + question.getOptionC() + newLine);
                printWriter.write("Option D: " + question.getOptionD() + newLine);
                printWriter.write("Option E: " + question.getOptionE() + newLine);
                printWriter.write("Reason: " + question.getReason() + newLine);
                printWriter.write(newLine);
            }

            printWriter.flush();
            printWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static File numberEditor(File oldFile) {
        String newFileName = oldFile.getName().replace(".txt", "") + "_editedNumber.txt";
        //oldFile.get
        File newFile = new File(oldFile.getParent() + "\\" + newFileName);


        try {
            newFile.createNewFile();
            FileReader fileReader = new FileReader(oldFile);
            FileWriter fileWriter = new FileWriter(newFile, true);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String readLine;
            int count = 0;
            while ((readLine = bufferedReader.readLine()) != null) {
                bufferedWriter.append(String.valueOf(++count)).append(". ").append(readLine).append("\n");
            }

            bufferedReader.close();
            bufferedWriter.close();
            fileReader.close();
            fileWriter.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile;
    }

}