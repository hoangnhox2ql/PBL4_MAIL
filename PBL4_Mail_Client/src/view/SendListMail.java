package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SendListMail extends JFrame{
	private JPanel contentPane;
    private JTextField toTextField;
    private JTextField subjectTextField;
    private JTextArea messageTextArea;
    private JButton attachButton;
    public File fileToSend;
    private JTextField text_link_file;
    
	public SendListMail() {
		attachButton = new JButton("Attach File");
        attachButton.setBounds(420, 328, 120, 25);
        attachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileToSend = attachFile();
            }
        });
	}
	public File attachFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // Xử lý việc đính kèm tệp tin ở đây
            text_link_file.setText("Attached file: " + selectedFile.getAbsolutePath());
            return selectedFile;
        }
        return null;
    }
	
}
