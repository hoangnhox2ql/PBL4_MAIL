package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.synth.SynthOptionPaneUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class ForwardMailView extends JFrame {

    private JPanel contentPane;
    private JTextField tf_receiver;
    private JTextField tf_subject;
    private JTextArea text_body;
    public File fileToSend;
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

    public ForwardMailView(String username,String receiver,String subject,LocalDateTime date) {
    	setTitle("Sent");
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

        tf_receiver = new JTextField();
        tf_receiver.setBounds(50, 10, 200, 20);
        contentPane.add(tf_receiver);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setBounds(10, 40, 50, 20);
        contentPane.add(subjectLabel);

        tf_subject = new JTextField();
        tf_subject.setEditable(false);
        tf_subject.setBounds(70, 40, 180, 20);
        contentPane.add(tf_subject);

        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setBounds(10, 70, 70, 20);
        contentPane.add(messageLabel);

        text_body = new JTextArea();
        text_body.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(text_body);
        scrollPane.setBounds(10, 100, 400, 200);
        contentPane.add(scrollPane);
        
        JButton btn_cancel = new JButton("Cancel");
        btn_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Replace exit with dispose
            }
        });
        btn_cancel.setBackground(new Color(255, 255, 255));
        btn_cancel.setBounds(420, 329, 120, 23);
        contentPane.add(btn_cancel);
        
        JLabel lblNewLabel = new JLabel("File : ");
        lblNewLabel.setBounds(10, 311, 30, 14);
        contentPane.add(lblNewLabel);
        
        JComboBox<String> cbb_file = new JComboBox<String>();
        cbb_file.setBounds(10, 330, 400, 20);
        contentPane.add(cbb_file);
        tf_receiver.setText(receiver);
        tf_subject.setText(subject);
        showMailBody(text_body,receiver,subject,date,cbb_file);
        
        JButton btn_forward = new JButton("Forward");
        btn_forward.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		sendEmail(username,cbb_file);
        	}
        });
        btn_forward.setBounds(420, 277, 120, 23);
        contentPane.add(btn_forward);
    }
    
    public void showMailBody(JTextArea text_body, String receiver, String subject, LocalDateTime date, JComboBox<String> cbb_file) {
        try {
            SignInView.dos.writeUTF("GET_SENT");
            SignInView.dos.writeUTF(receiver);
            SignInView.dos.writeUTF(subject);
            SignInView.dos.writeUTF(date.toString());

            String body = SignInView.dis.readUTF();
            text_body.setText(body);

            int fileCount = SignInView.dis.readInt();

            for (int j = 0; j < fileCount; j++) {
                String fileName = SignInView.dis.readUTF();
                cbb_file.addItem(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
  private void sendEmail(String username,JComboBox<String> cbb_file) {
	  String to = tf_receiver.getText();
	  String subject = tf_subject.getText();
	  String message = text_body.getText();
	  if(to.equals("") && subject.equals("")) {
		  JOptionPane.showMessageDialog(ForwardMailView.this, "Your mail's information is clear.");
	  }
	  else if (to.equals("") && !subject.equals("")) {
		  JOptionPane.showMessageDialog(ForwardMailView.this, "Please enter the Receiver.");
	  } else if (!to.equals("") && subject.equals("")) {
		  JOptionPane.showMessageDialog(ForwardMailView.this, "Please enter the Subject.");
	  }
	  else if(!to.equals("") && !subject.equals("")) {
		  try {
			  SignInView.dos.writeUTF("FORWARD_MAIL");
			  SignInView.dos.writeUTF(username);
			  SignInView.dos.writeUTF(to);
			  SignInView.dos.writeUTF(subject);
			  SignInView.dos.writeUTF(message);
			  String rep = SignInView.dis.readUTF();
			  if(rep.equals("MAIL_SENT_SUCCESS")) {
				  JOptionPane.showMessageDialog(this,"Sent mail success");
				  dispose();
			  }
			  else if(rep.equals("MAIL_SENT_FAILURE")) {
				  JOptionPane.showMessageDialog(this,"Email address was not found");
			  }
		  } catch (Exception e) {
  				//	TODO: handle exception
		  }
	  } 	 
}
}
