public class IncompatibleFileException extends Exception{

    public IncompatibleFileException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        System.err.println(getMessage());
    }
}
