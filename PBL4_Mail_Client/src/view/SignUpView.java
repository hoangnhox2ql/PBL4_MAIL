package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignUpView extends JFrame {
	public int id = 1;  // đăng kí id user
	public boolean check = false;
	public boolean check1 = true;
	public boolean check2 = true;
	public boolean check3 = true;
	public boolean check4 = true;
	
	private JPanel contentPane;
	private JTextField tf_username;
	private JPasswordField passwordField;
	private JPasswordField passwordField_confirm;
	private JTextField tf_phone;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SignUpView frame = new SignUpView();
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
	public SignUpView() {
		setTitle("Create an account");
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\Downloads\\3158180.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 302, 435);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Email address");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 49, 266, 14);
		contentPane.add(lblNewLabel);
		
		tf_username = new JTextField();
		tf_username.setBounds(10, 74, 266, 20);
		contentPane.add(tf_username);
		tf_username.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 122, 266, 14);
		contentPane.add(lblNewLabel_1);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(10, 147, 266, 20);
		contentPane.add(passwordField);
		
		JLabel lblNewLabel_2 = new JLabel("Confirm password");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(10, 200, 266, 14);
		contentPane.add(lblNewLabel_2);
		
		passwordField_confirm = new JPasswordField();
		passwordField_confirm.setBounds(10, 225, 266, 20);
		contentPane.add(passwordField_confirm);
		
		JLabel lblNewLabel_3 = new JLabel("Phone");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(10, 273, 266, 14);
		contentPane.add(lblNewLabel_3);
		
		tf_phone = new JTextField();
		tf_phone.setBounds(10, 298, 266, 20);
		contentPane.add(tf_phone);
		tf_phone.setColumns(10);
		
		JLabel errorLabel = new JLabel("");
		errorLabel.setForeground(new Color(255, 0, 0));
		errorLabel.setBackground(new Color(255, 255, 255));
		errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		errorLabel.setBounds(10, 329, 266, 22);
		contentPane.add(errorLabel);
		
		JButton bt_confirm = new JButton("Sign up");
		bt_confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            String username = tf_username.getText();
	            String password = passwordField.getText();
	            String confirm_password = passwordField_confirm.getText();
	            String phone = tf_phone.getText();

	            // Validate input
	            boolean valid = true;
	            if (username.isEmpty()) {
	            	JOptionPane.showMessageDialog(SignUpView.this, "Please enter your Email address");
	                valid = false;
	            }
	            if (!username.endsWith("@sv1.dut.udn.vn")) {
	                JOptionPane.showMessageDialog(SignUpView.this, "Username must end with @sv1.dut.udn.vn");
	                valid = false;
	            }
	            if (password.isEmpty()) {
	            	JOptionPane.showMessageDialog(SignUpView.this, "Please enter your password.");
	                valid = false;
	            } else if (!password.equals(confirm_password)) {
	                errorLabel.setText("Password confirmation is not correct.");
	                valid = false;
	            }
	            if (phone.isEmpty()) {
	            	JOptionPane.showMessageDialog(SignUpView.this, "Please enter your phone number.");
	                valid = false;
	            }

	            // If input is valid, create a new account and redirect to HomeView
	            if (valid) {
	                try {
	                	SignInView.dos.writeUTF("SIGN_UP");
	                	SignInView.dos.writeUTF(username);
	                	SignInView.dos.writeUTF(password);
	                	SignInView.dos.writeUTF(phone);
						String rep = SignInView.dis.readUTF();
	                	if(rep.equals("SIGN_UP_OK")){
	                		java.awt.EventQueue.invokeLater(new Runnable() {
			                    @Override
			                    public void run() {
			                        //new HomeView(username).setVisible(true);
			                    }
			                });
			                SignUpView.this.dispose();
	                	}
	                	else if(rep.equals("SAME_USERNAME_PHONE")) {
	                		errorLabel.setText("Trùng tên tài khoản hoặc số điện thoại!!!");
	                	}
					} catch (Exception e2) {
						// TODO: handle exception
						System.out.println("khong ket noi dc server");
					}
	            }
			}
		});
		bt_confirm.setBackground(new Color(255, 255, 255));
		bt_confirm.setBounds(97, 362, 89, 23);
		contentPane.add(bt_confirm);
		
		JLabel lblNewLabel_4 = new JLabel(" ←");
		lblNewLabel_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
	                public void run() {
	                    new SignInView().setVisible(true);
	                }
	            });
	            SignUpView.this.dispose();
			}
		});
		lblNewLabel_4.setForeground(new Color(0, 64, 128));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel_4.setBounds(10, 11, 46, 14);
		contentPane.add(lblNewLabel_4);
	}

}
