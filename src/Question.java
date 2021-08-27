import java.io.Serializable;
/*
        todo: This class Question should be a universal class, any 'copy' of this class should
todo: the same implementation, as such, a figure would be attached to show class version and compatibility,
        todo: maybe using it as module or some shit like that would work
 */

/*
todo: Question version 2.0
 */
public class Question implements Serializable {

    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String optionE;
    private final String[] optionArray;
    private int answeredOption, correctOption;
    private boolean questionEntered, optionArrayInitialised;
    private String correctOptionString, reason, yearOfCourse, courseName;

    public Question() {
        optionArrayInitialised = false;
        optionArray = new String[5];
    }

    public boolean isQuestionEntered() {
        return questionEntered;
    }

    public void setQuestionEntered(boolean questionEntered) {
        this.questionEntered = questionEntered;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    String getOptionE() {
        return optionE;
    }

    public void setOptionE(String optionE) {
        this.optionE = optionE;
    }

    public void setOptionFromArray(String[] options) {
        int count = 0;
        for (String option : options) {
            optionArray[count] = option;
            count++;
        }
        optionA = optionArray[0];
        optionB = optionArray[1];
        optionC = optionArray[2];
        optionD = optionArray[3];
        optionE = optionArray[4];
        optionArrayInitialised = true;
    }

    public String[] getOptionArray() {
        return optionArray;
    }

    public void generateArrayFromOption() {
        optionArray[0] = optionA;
        optionArray[1] = optionB;
        optionArray[2] = optionC;
        optionArray[3] = optionD;

        if (optionE != null) optionE = optionArray[4];
        optionArrayInitialised = true;
    }

    public int optionInitialisedCount() {
        int count = 0;
        for (String s : optionArray) if (s != null) count++;
        return count;
    }

    public int getAnsweredOption() {
        return answeredOption;
    }

    public void setAnsweredOption(int answeredOption) {
        this.answeredOption = answeredOption;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    public String getCorrectOptionString() {
        return correctOptionString;
    }

    public void setCorrectOptionString(String readLine) {
        correctOptionString = readLine;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getYearOfCourse() {
        return yearOfCourse;
    }

    public void setYearOfCourse(String yearOfCourse) {
        this.yearOfCourse = yearOfCourse;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                ", optionE='" + optionE + '\'' +
                '}';
    }
}


