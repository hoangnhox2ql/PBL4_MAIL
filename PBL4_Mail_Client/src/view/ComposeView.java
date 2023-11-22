package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;

public class ComposeView extends JFrame {

    private JPanel contentPane;
    private JTextField toTextField;
    private JTextField subjectTextField;
    private JTextArea messageTextArea;
    private JButton attachButton;
    public File fileToSend;
    private JTextField text_link_file;
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

    public ComposeView(String username) {
    	setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\Downloads\\3158180.png"));
    	setBackground(new Color(255, 255, 255));
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
        attachButton.setBounds(420, 328, 120, 25);
        attachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileToSend = attachFile();
            }
        });
        contentPane.add(attachButton);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(420, 241, 120, 25);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	sendEmail(username,fileToSend);
//              sendEmail(username);
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
        
        text_link_file = new JTextField();
        text_link_file.setBounds(10, 330, 400, 20);
        contentPane.add(text_link_file);
        text_link_file.setColumns(10);
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
    
    private void sendEmail(String username, File file) {
        String to = toTextField.getText();
        String subject = subjectTextField.getText();
        String message = messageTextArea.getText();

        if (to.equals("") && subject.equals("")) {
            JOptionPane.showMessageDialog(ComposeView.this, "Your mail's information is clear.");
        } else if (to.equals("") && !subject.equals("")) {
            JOptionPane.showMessageDialog(ComposeView.this, "Please enter the Receiver.");
        } else if (!to.equals("") && subject.equals("")) {
            JOptionPane.showMessageDialog(ComposeView.this, "Please enter the Subject.");
        } else if (!to.equals("") && !subject.equals("")) {
            try {
                SignInView.dos.writeUTF("SEND_MAIL");
                SignInView.dos.writeUTF(username);
                SignInView.dos.writeUTF(to);
                SignInView.dos.writeUTF(subject);
                SignInView.dos.writeUTF(message);

                if (file != null) {
                	SignInView.dos.writeUTF("YES");
                	System.out.println(file);
                	SignInView.dos.writeUTF(file.getName());
                	int bytes = 0;
            		// Open the File where he located in your pc
            		
            		FileInputStream fileInputStream
            			= new FileInputStream(file);

            		// Here we send the File to Server
            		SignInView.dos.writeLong(file.length());
            		// Here we break file into chunks
            		byte[] buffer = new byte[4 * 1024];
            		while ((bytes = fileInputStream.read(buffer))
            			!= -1) {
            		// Send the file to Server Socket 
            		SignInView.dos.write(buffer, 0, bytes);
            		
            		}
            		// close the file here
            		fileInputStream.close();
                } 
                
                // Check for server response
                String rep = SignInView.dis.readUTF();
                if (rep.equals("MAIL_SENT_SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "Sent mail success");
                } else {
                    JOptionPane.showMessageDialog(this, "Email address was not found");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



//    private void sendEmail(String username) {
//        String to = toTextField.getText();
//        String subject = subjectTextField.getText();
//        String message = messageTextArea.getText();
//        if(to.equals("") && subject.equals("")) {
//        	JOptionPane.showMessageDialog(ComposeView.this, "Your mail's information is clear.");
//        }
//        else if (to.equals("") && !subject.equals("")) {
//            JOptionPane.showMessageDialog(ComposeView.this, "Please enter the Receiver.");
//        } else if (!to.equals("") && subject.equals("")) {
//            JOptionPane.showMessageDialog(ComposeView.this, "Please enter the Subject.");
//        }
//        else if(!to.equals("") && !subject.equals("")) {
//        	try {
//        		SignInView.dos.writeUTF("SEND_MAIL");
//        		SignInView.dos.writeUTF(username);
//        		SignInView.dos.writeUTF(to);
//        		SignInView.dos.writeUTF(subject);
//        		SignInView.dos.writeUTF(message);
//        		String rep = SignInView.dis.readUTF();
//        		if(rep.equals("MAIL_SENT_SUCCESS")) {
//        			JOptionPane.showMessageDialog(this,"Sent mail success");
//        			//dispose();
//        		}
//        		else if(rep.equals("MAIL_SENT_FAILURE")) {
//        			JOptionPane.showMessageDialog(this,"Email address was not found");
//        		}
//        	} catch (Exception e) {
//        			//	TODO: handle exception
//        	}
//        }  
//    }
    

}
