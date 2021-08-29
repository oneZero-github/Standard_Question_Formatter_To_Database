import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GUI_DIALOG implements ActionListener, TextViewListener {
    StringBuilder backlog;
    JFrame jFrame;
    JTextField jTextField;
    JButton jButton;
    JLabel label_directory;
    JLabel label_information_display;
    private String question;
    private String questionEdit;
    private boolean taskState = false;
    private boolean anonymity = false;

    public GUI_DIALOG(String question) {
        this.question = question;

    }

    GUI_DIALOG() {
        backlog = new StringBuilder();
    }


    public void drawDefaultJFrame() throws IOException {
        jFrame = new JFrame("Question Text");
        jTextField = new JTextField(question);
        label_directory = new JLabel("Directory : ");
        label_information_display = new JLabel("Info");
        jButton = new JButton();
        jButton.setText("Done");
        jButton.setBounds(120, 250, 60, 30);
        jButton.addActionListener(this);
        jTextField.setBounds(50, 180, 200, 30);
        label_directory.setBounds(10, 35, 100, 30);
        label_information_display.setBounds(10, 60, 200, 50);
        jFrame.add(jTextField);
        jFrame.add(jButton);
        jFrame.add(label_directory);
        jFrame.add(label_information_display);
        jFrame.setSize(400, 400);
        jFrame.setLayout(null);
        jFrame.setVisible(true);
        if (anonymity) run();
    }

    private void run() throws IOException {
        String directory = getDirectory();
        FormaterEngine engine = new FormaterEngine(directory);
        engine.setTextViewListener(this);
        if (engine.getFileState()) {
            engine.setModalOptionCount(getModalOptionCount());
            for (File file : engine.getDirectoryList()) {
                try {
                    engine.formatFromFile(file);
                    engine.generateFormatFile();
                } catch (IncompatibleQuestionException e) {
                }
            }


        } else {

        }

    }

    private int getModalOptionCount() {
        return Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Modal Option Count "));
        //todo : handle exception here if integer is not a number
    }

    private String getDirectory() {
        return JOptionPane.showInputDialog(null, "Enter directory");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        questionEdit = jTextField.getText();
        taskState = true;
    }

    public boolean getTaskState() {
        return taskState;
    }

    public String getCorrection() {
        return questionEdit;
    }

    public void setAnonymity(boolean b) {
        anonymity = b;
    }

    @Override
    public String updateTextView(String message) {
        System.out.println(message);
        return JOptionPane.showInputDialog(null, "Please input the correct Option", message);

    }
}
