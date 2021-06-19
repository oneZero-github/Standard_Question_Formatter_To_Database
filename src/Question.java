import java.io.Serializable;

public class Question implements Serializable {

    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String optionE;
    private String[] optionArray; // for conveniences
    private int answeredOption, correctOption;
    private boolean questionEntered, optionArrayInitialised;
    private String correctOptionString, reason, yearOfCourse, courseName;

    public Question(String question, String optionA, String optionB, String optionC, String optionD, int correctOption) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
    }
    public Question() {
        optionArrayInitialised = false;
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

    public int setOptionFromOptionArray(String [] options){
        if (options.length  < 4) return -1;
        optionArray = new String [options.length];
        int count = 0;
        for(String option: options){
            optionArray[count] = option;
            count++;
        }
        optionA = optionArray[0];
        optionB = optionArray[1];
        optionC = optionArray[2];
        optionD = optionArray[3];

        if(options.length > 4) optionE = optionArray[4];
        optionArrayInitialised = true;
        return 1;

    }

    public String[] getOptionArray(){
        return optionArray;
    }

    public int generateArrayFromOptionArray(){

        if (optionArrayInitialised) {
            optionArray[0] = optionA;
            optionArray[1] = optionB;
            optionArray[2] = optionC;
            optionArray[3] = optionD;

            if(optionArray.length > 4) optionE = optionArray[4];
        } else return  -1;
        return  0;
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
}

