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
final List<File> filesToSend = new ArrayList<>();

	public File filechosse;
    private JPanel contentPane;
    private JTextField toTextField;
    private JTextField subjectTextField;
    private JTextArea messageTextArea;
    private JButton attachButton;
    
	public SendListMail(String username) {
		setTitle("Compose");
    	setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\Downloads\\3158180.png"));
    	setBackground(new Color(255, 255, 255));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 429);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
		JComboBox<String> cbb_file = new JComboBox<String>();
        cbb_file.setBounds(10, 329, 400, 25);
        contentPane.add(cbb_file);
        
        JButton btn_cancel = new JButton("Cancel");
        btn_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Replace exit with dispose
            }
        });
        btn_cancel.setBackground(new Color(255, 255, 255));
        btn_cancel.setBounds(420, 277, 120, 23);
        contentPane.add(btn_cancel);
        
		attachButton = new JButton("Attach File");
        attachButton.setBounds(420, 328, 120, 25);
        attachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                int option = fileChooser.showOpenDialog(null);

                if (option == JFileChooser.APPROVE_OPTION) {
                    for (File file : fileChooser.getSelectedFiles()) {
                        filechosse = file;
                        cbb_file.addItem(file.getName());                       
                    }
                }
            }
        });
        contentPane.add(attachButton);
        
        JButton delete_file = new JButton("Delete File");
        delete_file.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int i = cbb_file.getSelectedIndex();
        		if(i>=0) {
        			
            		cbb_file.removeItemAt(i);
        		}
        		else {
        			JOptionPane.showMessageDialog(SendListMail.this, "No file existed");
        		}
        		
        		
        	}
        });
        delete_file.setBounds(420, 356, 120, 23);
        contentPane.add(delete_file);
        
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(420, 241, 120, 25);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//            	sendEmail(username,fileToSend);
            	sendEmail(username);
            }
        });
        contentPane.add(sendButton);
	}
	public void sendEmail(String username)
	{
		
	}
	
}
