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

public class ComposeView extends JFrame {

	final List<File> filesToSend = new ArrayList<>();
	
    private JPanel contentPane;
    private JTextField toTextField;
    private JTextField subjectTextField;
    private JTextArea messageTextArea;
    private JButton attachButton;
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    ComposeView frame = new ComposeView();
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    public ComposeView(String username,String receiver,String subject,String body) {
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
        
        JComboBox<String> cbb_file = new JComboBox<String>();
        cbb_file.setBounds(10, 329, 400, 25);
        contentPane.add(cbb_file);

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
                        filesToSend.add(file);
                        cbb_file.addItem(file.getName());
                        
                    }
                }
            }
        });
        contentPane.add(attachButton);
        
        JButton btn_sendListMail = new JButton("SendListMail");
		btn_sendListMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
	                public void run() {
	                    new SendListMail().setVisible(true);
	                }
	            });
	            
			}
		});
		btn_sendListMail.setBackground(new Color(255, 255, 255));
		btn_sendListMail.setBounds(420, 205, 120, 25);
		contentPane.add(btn_sendListMail); 
        
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
        
        JButton btn_cancel = new JButton("Cancel");
        btn_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Replace exit with dispose
            }
        });
        btn_cancel.setBackground(new Color(255, 255, 255));
        btn_cancel.setBounds(420, 277, 120, 23);
        contentPane.add(btn_cancel);
        
        JLabel lblNewLabel = new JLabel("File : ");
        lblNewLabel.setBounds(10, 311, 30, 14);
        contentPane.add(lblNewLabel);
        
        toTextField.setText(receiver);
        subjectTextField.setText(subject);
        messageTextArea.setText(body);
        
        
        
        JButton delete_file = new JButton("Delete File");
        delete_file.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int i = cbb_file.getSelectedIndex();
        		if(i>=0) {
        			filesToSend.remove(i);
            		cbb_file.removeItemAt(i);
        		}
        		else {
        			JOptionPane.showMessageDialog(ComposeView.this, "No file existed");
        		}
        		
        		
        	}
        });
        delete_file.setBounds(420, 356, 120, 23);
        contentPane.add(delete_file);
        
    }

    private static void sendFileToServer(File fileToSend) {
        try {
       FileInputStream fileInputStream = new FileInputStream(fileToSend.getAbsolutePath());
      String fileName = fileToSend.getName();
      byte[] fileNameBytes = fileName.getBytes();
      byte[] fileContentBytes = new byte[(int) fileToSend.length()];
      fileInputStream.read(fileContentBytes);
      
      // Send file information
      SignInView.dos.writeInt(fileNameBytes.length);
      SignInView.dos.write(fileNameBytes);
      SignInView.dos.writeInt(fileContentBytes.length);
      SignInView.dos.write(fileContentBytes);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
    
    private void sendEmail(String username) {
        String to = toTextField.getText();
        String subject = subjectTextField.getText();
        String message = messageTextArea.getText();
        
        boolean redFlag = !filesToSend.isEmpty(); // Set redFlag to true if there are files to send

        if (to.equals("") && subject.equals("")) {
            JOptionPane.showMessageDialog(ComposeView.this, "Your mail's information is clear.");
        } else if (to.equals("") && !subject.equals("")) {
            JOptionPane.showMessageDialog(ComposeView.this, "Please enter the Receiver.");
        } else if (!to.equals("") && subject.equals("")) {
            JOptionPane.showMessageDialog(ComposeView.this, "Please enter the Subject.");
        } else {
            try {
                SignInView.dos.writeUTF("SEND_MAIL");
                SignInView.dos.writeUTF(username);
                SignInView.dos.writeUTF(to);
                SignInView.dos.writeUTF(subject);
                SignInView.dos.writeUTF(message);
                
                // Send redFlag to the server
                SignInView.dos.writeUTF(redFlag ? "yes" : "no");
                SignInView.dos.writeUTF(filesToSend.size() + "");
                // If there are files, send them to the server
                if (redFlag) {
                    for (File fileToSend : filesToSend) {
                    	sendFileToServer(fileToSend);
                    }
                }

                String rep = SignInView.dis.readUTF();
                if (rep.equals("MAIL_SENT_SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "Sent mail success");
                    dispose();
                } else if (rep.equals("MAIL_SENT_FAILURE")) {
                    JOptionPane.showMessageDialog(this, "Email address was not found");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}




