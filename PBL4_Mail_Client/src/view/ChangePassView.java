package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChangePassView extends JFrame {

	private JPanel contentPane;
	private JPasswordField passwordField;
	private JPasswordField passwordField_confirm;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ChangePassView frame = new ChangePassView();
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
	public ChangePassView(String phone) {
		setTitle("Change Password");
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\Downloads\\3158180.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 371, 308);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 29, 266, 14);
		contentPane.add(lblNewLabel_1);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(10, 54, 335, 20);
		contentPane.add(passwordField);
		
		JLabel lblNewLabel_2 = new JLabel("Confirm password");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(10, 120, 266, 14);
		contentPane.add(lblNewLabel_2);
		
		passwordField_confirm = new JPasswordField();
		passwordField_confirm.setBounds(10, 145, 335, 20);
		contentPane.add(passwordField_confirm);
		
		JLabel lb_error = new JLabel("");
		lb_error.setForeground(new Color(255, 0, 0));
		lb_error.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lb_error.setBounds(10, 190, 335, 14);
		contentPane.add(lb_error);
		
		JButton btn_confirm = new JButton("Confirm");
		btn_confirm.setForeground(new Color(0, 0, 0));
		btn_confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = passwordField.getText();
				if((passwordField.getText()).equals(passwordField_confirm.getText())) {
					text = passwordField.getText();
					try {
						SignInView.dos.writeUTF("CHANGE_PASSWORD");
						SignInView.dos.writeUTF(text);
						SignInView.dos.writeUTF(phone);
						String rep =SignInView.dis.readUTF();
						if(rep.equals("CHANGE_PASSWORD_OK")) {
							JOptionPane.showMessageDialog(ChangePassView.this, "Your password was changed");
							ChangePassView.this.dispose();
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										SignInView frame = new SignInView();
										frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						}
					} catch (Exception e2) {
						// TODO: handle exception
						System.out.println("loi ko ket noi dc server");
					}
				}
				else {
					lb_error.setText("Your confirmation password doesn't match!");
				}
			}
		});
		btn_confirm.setBackground(Color.WHITE);
		btn_confirm.setBounds(133, 215, 89, 23);
		contentPane.add(btn_confirm);
		
		
	}

}
