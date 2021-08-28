import java.io.IOException;

public class Launcher {
    public static void main(String[] args) throws IOException {
        run();
    }

    static void run() throws IOException {
        GUI_DIALOG gui = new GUI_DIALOG();
        gui.setAnonymity(true);
        gui.drawDefaultJFrame();
    }
}
