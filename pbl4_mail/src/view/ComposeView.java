package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.sql_handler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ComposeView extends JFrame {

    private JPanel contentPane;
    private JTextField toTextField;
    private JTextField subjectTextField;
    private JTextArea messageTextArea;
    private JButton attachButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ComposeView frame = new ComposeView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ComposeView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Tạo các thành phần giao diện
        JLabel toLabel = new JLabel("To:");
        toLabel.setBounds(10, 10, 30, 20);
        contentPane.add(toLabel);

        toTextField = new JTextField();
        toTextField.setBounds(50, 10, 200, 20);
        contentPane.add(toTextField);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setBounds(10, 40, 50, 20);
        contentPane.add(subjectLabel);

        subjectTextField = new JTextField();
        subjectTextField.setBounds(70, 40, 180, 20);
        contentPane.add(subjectTextField);

        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setBounds(10, 70, 70, 20);
        contentPane.add(messageLabel);

        messageTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(messageTextArea);
        scrollPane.setBounds(10, 100, 400, 200);
        contentPane.add(scrollPane);

        attachButton = new JButton("Attach File");
        attachButton.setBounds(420, 100, 120, 25);
        attachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attachFile();
            }
        });
        contentPane.add(attachButton);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(420, 140, 120, 25);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmail();
            }
        });
        contentPane.add(sendButton);
    }

    private void attachFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // Xử lý việc đính kèm tệp tin ở đây
            System.out.println("Attached file: " + selectedFile.getAbsolutePath());
        }
    }

    private void sendEmail() {
        String to = toTextField.getText();
        String subject = subjectTextField.getText();
        String message = messageTextArea.getText();

        // Xử lý việc gửi email ở đây

        // Hiển thị thông báo thành công (hoặc xử lý lỗi)
//        JOptionPane.showMessageDialog(this, "Email sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        sql_handler.SendMessage(to,subject,message);
    }
}
