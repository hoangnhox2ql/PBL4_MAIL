package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.sql_handler;

import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.management.Notification;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ForgotPasswordView extends JFrame {

	private JPanel contentPane;
	private static JTextField textField;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ForgotPasswordView frame = new ForgotPasswordView();
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
	public ForgotPasswordView() {
		setTitle("Forgot Password");
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\Downloads\\3158180.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 641, 445);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Verify Account");
		lblNewLabel_1.setForeground(new Color(0, 0, 64));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.ITALIC, 24));
		lblNewLabel_1.setBounds(10, 11, 168, 47);
		contentPane.add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBounds(10, 129, 182, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Enter your phone's numbers");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 104, 182, 14);
		contentPane.add(lblNewLabel);
		
		JLabel notification = new JLabel("");
		notification.setForeground(new Color(255, 0, 0));
		notification.setBounds(10, 194, 227, 14);
		contentPane.add(notification);
		
		JButton btnNewButton = new JButton("Confirm");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String phone = textField.getText();
				if(sql_handler.isValidPhone(phone)) {
					java.awt.EventQueue.invokeLater(new Runnable() {
		                public void run() {
		                    new ChangePassView(phone).setVisible(true);
		                }
		            });
		            ForgotPasswordView.this.dispose();
				}else {
					notification.setText("Your phone numbers is not correct");
				}
			}
		});
		btnNewButton.setBackground(new Color(255, 255, 255));
		btnNewButton.setBounds(53, 160, 89, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_4 = new JLabel(" ←");
		lblNewLabel_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
					java.awt.EventQueue.invokeLater(new Runnable() {
		                public void run() {
		                    new LogInView().setVisible(true);
		                }
		            });
		            ForgotPasswordView.this.dispose();
			}
		});
		lblNewLabel_4.setForeground(new Color(0, 64, 128));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel_4.setBounds(10, 366, 54, 29);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon("C:\\Users\\ASUS\\Downloads\\pngtree-email-mail-message-text-flat-color-icon-vector-icon-png-image_1620997.jpg"));
		lblNewLabel_2.setBounds(109, 0, 516, 406);
		contentPane.add(lblNewLabel_2);
	}
}
