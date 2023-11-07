package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import controller.sql_handler;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LogInView extends JFrame {

	private JPanel contentPane;
	private JTextField tf_username;
	private JPasswordField tf_password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogInView frame = new LogInView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LogInView() {
		setTitle("Email");
		setForeground(new Color(192, 192, 192));
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\Downloads\\3158180.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 612, 426);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lb_username = new JLabel("Email Adress");
		lb_username.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lb_username.setBackground(new Color(0, 0, 0));
		lb_username.setBounds(10, 27, 131, 20);
		contentPane.add(lb_username);
		
		JLabel lb_password = new JLabel("Password");
		lb_password.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lb_password.setBounds(10, 106, 64, 20);
		contentPane.add(lb_password);
		
		tf_username = new JTextField();
		tf_username.setBounds(10, 58, 212, 20);
		contentPane.add(tf_username);
		tf_username.setColumns(10);
		
		JButton btn_login = new JButton("Sign In");
		btn_login.setBackground(new Color(255, 255, 255));
		btn_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user =  tf_username.getText();
		    	String pass = tf_password.getText();
		        if (sql_handler.isValidLogin(user, pass)) {
		            java.awt.EventQueue.invokeLater(new Runnable() {
		                public void run() {
		                    new HomeView(user).setVisible(true);
		                }
		            });
		            LogInView.this.dispose();
		        } else {
		            JOptionPane.showMessageDialog(LogInView.this, "Wrong email or password !");
		            tf_password.setText("");
		        }
			}
		});
		btn_login.setBounds(10, 168, 212, 23);
		contentPane.add(btn_login);
		
		JLabel btn_forgotpass = new JLabel("( Forgot your password ? )");
		btn_forgotpass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
	                public void run() {
	                    new ForgotPasswordView().setVisible(true);
	                }
	            });
	            LogInView.this.dispose();
				
			}
		});
		btn_forgotpass.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		btn_forgotpass.setHorizontalAlignment(SwingConstants.CENTER);
		btn_forgotpass.setLabelFor(btn_forgotpass);
		btn_forgotpass.setForeground(new Color(255, 0, 0));
		btn_forgotpass.setBounds(77, 109, 153, 14);
		contentPane.add(btn_forgotpass);
		
		tf_password = new JPasswordField();
		tf_password.setBounds(10, 137, 212, 20);
		contentPane.add(tf_password);
		
		JLabel lblNewLabel = new JLabel("New to Email ?");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(10, 202, 85, 14);
		contentPane.add(lblNewLabel);
		
		JLabel btn_createAccount = new JLabel("Create an account");
		btn_createAccount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
	                public void run() {
	                    new SignInView().setVisible(true);
	                }
	            });
	            LogInView.this.dispose();
			}
		});
		btn_createAccount.setHorizontalAlignment(SwingConstants.CENTER);
		btn_createAccount.setForeground(Color.RED);
		btn_createAccount.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		btn_createAccount.setBounds(105, 202, 119, 14);
		contentPane.add(btn_createAccount);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\ASUS\\Downloads\\pngtree-email-mail-message-text-flat-color-icon-vector-icon-png-image_1620997.jpg"));
		lblNewLabel_1.setBounds(87, 0, 509, 387);
		contentPane.add(lblNewLabel_1);
	}
}
