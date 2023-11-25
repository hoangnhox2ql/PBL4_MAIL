package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import model.email;
import java.io.IOException;
import javax.swing.Timer;


public class HomeView extends JFrame {

	private JPanel contentPane;
	private JTextField txtSearchingMails;
	private JTable table_mail;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					HomeView frame = new HomeView();
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
	public HomeView(String userName) {
		setTitle("Home");
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\Downloads\\3158180.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 660, 430);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lb_name = new JLabel("Hello "+userName);
		lb_name.setForeground(new Color(0, 128, 128));
		lb_name.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lb_name.setBounds(337, 11, 230, 30);
		contentPane.add(lb_name);
		
		JLabel lblNewLabel_1 = new JLabel("( Log out )");
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HomeView.this.dispose();
				
			}
		});
		lblNewLabel_1.setForeground(new Color(255, 0, 0));
		lblNewLabel_1.setFont(UIManager.getFont("Button.font"));
		lblNewLabel_1.setBounds(577, 21, 57, 14);
		contentPane.add(lblNewLabel_1);
		
		txtSearchingMails = new JTextField();
		txtSearchingMails.setForeground(new Color(192, 192, 192));
		txtSearchingMails.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtSearchingMails.setText("Searching mails");
		txtSearchingMails.setBounds(10, 18, 317, 30);
		contentPane.add(txtSearchingMails);
		txtSearchingMails.setColumns(10);
		
		JComboBox comboBox_search = new JComboBox();
		comboBox_search.setForeground(new Color(0, 0, 0));
		comboBox_search.setBackground(new Color(255, 255, 255));
		comboBox_search.setModel(new DefaultComboBoxModel(new String[] {"All", "Sender", "Subject"}));
		comboBox_search.setBounds(10, 60, 159, 22);
		contentPane.add(comboBox_search);
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String textSearch = txtSearchingMails.getText();
		    	String selectedItem = (String) comboBox_search.getSelectedItem();
		    	java.awt.EventQueue.invokeLater(new Runnable() {
	                public void run() {
	                    new SearchView(userName,textSearch,selectedItem).setVisible(true);
	                }
	            });
		    }
		});
		btnNewButton.setBackground(new Color(255, 255, 255));
		btnNewButton.setBounds(220, 60, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btn_compose = new JButton("COMPOSE");
		btn_compose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
	                public void run() {
	                    new ComposeView(userName).setVisible(true);
	                }
	            });
	            
			}
		});
		btn_compose.setBackground(new Color(255, 255, 255));
		btn_compose.setBounds(538, 271, 96, 23);
		contentPane.add(btn_compose);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 115, 520, 265);
		contentPane.add(scrollPane);
		
		table_mail = new JTable();
		table_mail.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int selectedRow = table_mail.getSelectedRow();
				if (selectedRow != -1) {
				    String sender = (String) table_mail.getValueAt(selectedRow, 0);
				    String subject = (String) table_mail.getValueAt(selectedRow, 1);
				    LocalDateTime date = (LocalDateTime) table_mail.getValueAt(selectedRow, 2);
				    java.awt.EventQueue.invokeLater(new Runnable() {
		                public void run() {
		                    //new HomeView(user).setVisible(true);
		                	new MailView(sender,subject,date).setVisible(true);
		                }
		            });
				}
				
			}
		});
		table_mail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_mail.setModel(new DefaultTableModel(new Object[][]{},
			new String[] {
					"Sender", "Subject", "Date"
			}		
		));
		scrollPane.setViewportView(table_mail);
		
		JLabel lblNewLabel = new JLabel("INBOX");
		lblNewLabel.setBounds(10, 93, 46, 14);
		contentPane.add(lblNewLabel);
		Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Gọi lại hàm showdata để cập nhật dữ liệu //0395163068
                showdata(userName);
            }
        });
        // Bắt đầu timer
        timer.start();
		
		JButton btn_deleteMail = new JButton("DELETE");
		btn_deleteMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table_mail.getSelectedRow(); // Lấy chỉ mục của hàng đã chọn
		        if (selectedRow >= 0) {
		            Object value = table_mail.getValueAt(selectedRow, 1); 
		            String valueString = value.toString();
		            try {
		            	SignInView.dos.writeUTF("DELETE");
						SignInView.dos.writeUTF(valueString);
						String rep = SignInView.dis.readUTF();
						if(rep.equals("DELETE_OK")) {
				        	showdata(userName);
				        }
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("ko the ket noi den sever");
					}
		        }
			}
		});
		btn_deleteMail.setBackground(new Color(255, 255, 255));
		btn_deleteMail.setBounds(538, 332, 96, 23);
		contentPane.add(btn_deleteMail);
		
		JButton btn_sent = new JButton("SENT");
		btn_sent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
	                public void run() {
	                    new ListSentView(userName).setVisible(true);
	                }
	            });
			}
		});
		btn_sent.setBackground(new Color(255, 255, 255));
		btn_sent.setBounds(538, 205, 89, 23);
		contentPane.add(btn_sent);
		
	}
	public void showdata(String userName) {
	    try {
	        SignInView.dos.writeUTF("ONLINE");
	        SignInView.dos.writeUTF(userName);

	        ObjectInputStream ois = new ObjectInputStream(SignInView.soc.getInputStream());
	        List<email> message_data = (List<email>) ois.readObject();
	        DefaultTableModel tableModel = (DefaultTableModel) table_mail.getModel();
	        tableModel.setRowCount(0);
	        message_data.forEach((message) -> {
	            tableModel.addRow(new Object[] {
	                message.getSubject(), message.getSender(), message.getSendDate()
	            });
	        });

	    } catch (IOException | ClassNotFoundException e) {
	        // Xử lý lỗi (có thể ghi log, thông báo, hoặc thực hiện các hành động phù hợp)
	        e.printStackTrace();
	    }
	}
}


