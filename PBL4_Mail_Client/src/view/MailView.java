package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import java.awt.Toolkit;
import java.time.LocalDateTime;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class MailView extends JFrame {

	private JPanel contentPane;
	private JTextField text_sender;
	private JTextField text_subject;
	private JTextField text_date_time;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MailView frame = new MailView();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public MailView(String sender,String subject,LocalDateTime date) {
		setTitle("Mail");
		setBackground(new Color(255, 255, 255));
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\Downloads\\3158180.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 557, 399);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea text_body = new JTextArea();
		text_body.setBounds(10, 113, 422, 236);
		contentPane.add(text_body);
		
		text_sender = new JTextField();
		text_sender.setBounds(109, 12, 234, 20);
		contentPane.add(text_sender);
		text_sender.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("FROM :");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 14, 55, 14);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("CANCEL");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton.setBackground(new Color(255, 255, 255));
		btnNewButton.setBounds(442, 326, 89, 23);
		contentPane.add(btnNewButton);
		
		text_subject = new JTextField();
		text_subject.setBounds(109, 44, 234, 20);
		contentPane.add(text_subject);
		text_subject.setColumns(10);
		
		JLabel lblSubject = new JLabel("SUBJECT :");
		lblSubject.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblSubject.setBounds(10, 46, 70, 14);
		contentPane.add(lblSubject);
		
		text_date_time = new JTextField();
		text_date_time.setBounds(109, 75, 234, 20);
		contentPane.add(text_date_time);
		text_date_time.setColumns(10);
		
		JLabel lblDatetime = new JLabel("DATE-TIME  :");
		lblDatetime.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblDatetime.setBounds(10, 78, 89, 14);
		contentPane.add(lblDatetime);
		
		text_sender.setText(sender);
		text_subject.setText(subject);
		text_date_time.setText(date.toString());
		showMailBody(text_body,sender,subject,date);
	}
	public void showMailBody(JTextArea text_body ,String sender,String subject,LocalDateTime date) {
		try {
			SignInView.dos.writeUTF("GET_BODY");
			SignInView.dos.writeUTF(sender);
			SignInView.dos.writeUTF(subject);
			SignInView.dos.writeUTF(date.toString());
			String body = SignInView.dis.readUTF();
			text_body.setText(body);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
