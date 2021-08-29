public class IncompatibleQuestionException extends Exception {

    public static final int QUESTION_NUMBERING_ERROR = 1;

    public IncompatibleQuestionException(String message, int error) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
