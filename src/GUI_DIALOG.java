import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI_DIALOG implements ActionListener {

    JFrame jFrame;
    JTextField jTextField;
    JButton jButton;
    private String question;
    private String questionEdit;
    private boolean taskState = false;

    public GUI_DIALOG(String question) {
        this.question = question;

    }


    GUI_DIALOG() {

    }


    public void drawDefaultJFrame() {
        jFrame = new JFrame("Question Text");
        jTextField = new JTextField(question);
        jButton = new JButton("Done");
        jButton.setBounds(120, 200, 50, 50);
        jButton.addActionListener(this);
        jTextField.setBounds(50, 100, 200, 30);
        jFrame.add(jTextField);
        jFrame.add(jButton);
        jFrame.setSize(400, 400);
        jFrame.setLayout(null);
        jFrame.setVisible(true);
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
}
