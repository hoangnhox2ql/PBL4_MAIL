package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
	private void sendEmail(String username) {
        String filePath = filechosse.getAbsolutePath();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
            	String addAllFile;
                String[] parts = line.split("\t");
                String to = parts[0];
                String subject = parts[1];
                String message = parts[2];
                if(parts.length >= 4)
                {
                	
                	for(int i = 3; i < parts.length;i++)
                	{
                		addAllFile = parts[i];
                		File addFile = new File(addAllFile);
                        filesToSend.add(addFile);
                	}
                }

                boolean redFlag = !filesToSend.isEmpty();
                // Kiểm tra điều kiện và xử lý ngoại lệ
                if (to.isEmpty() && subject.isEmpty()) {
                    JOptionPane.showMessageDialog(SendListMail.this, "Thông tin email của bạn rỗng.");
                } else if (to.isEmpty() && !subject.isEmpty()) {
                    JOptionPane.showMessageDialog(SendListMail.this, "Vui lòng nhập người nhận.");
                } else if (!to.isEmpty() && subject.isEmpty()) {
                    JOptionPane.showMessageDialog(SendListMail.this, "Vui lòng nhập chủ đề.");
                }else {
                	SignInView.dos.writeUTF("SEND_MAIL");
                	SignInView.dos.writeUTF(username);
                    SignInView.dos.writeUTF(to);
                    SignInView.dos.writeUTF(subject);
                    SignInView.dos.writeUTF(message);
                    
                    
                    SignInView.dos.writeUTF(redFlag ? "yes" : "no");
                    SignInView.dos.writeUTF(filesToSend.size() + "");
                    
                    if (redFlag) {
                        for (File fileToSend : filesToSend) {
                        	sendFileToServer(fileToSend);
                        }
                    }
                    String rept = SignInView.dis.readUTF();

                    if (rept.equals("MAIL_SENT_SUCCESS")) {
                        JOptionPane.showMessageDialog(this, "Sent mail success");
                        dispose();
                    } else if (rept.equals("MAIL_SENT_FAILURE")) {
                        JOptionPane.showMessageDialog(this, "Email address was not found");
                    }
                    filesToSend.clear();
                }
                
                
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
	
}
