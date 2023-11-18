package view;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.email;

import java.awt.Color;

public class ListSentView extends JFrame {

	private JPanel contentPane;
	private JTable table_mail;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ListSentView frame = new ListSentView();
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
	public ListSentView(String userName) {
		setBackground(new Color(255, 255, 255));
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\Downloads\\3158180.png"));
		setTitle("Mail Sent");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 576, 387);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 441, 326);
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
					"Receiver", "Subject", "Date"
			}		
		));
		scrollPane.setViewportView(table_mail);
		
		JButton btnNewButton = new JButton("CANCEL");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton.setBounds(461, 314, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btn_forward = new JButton("FORWARD");
		btn_forward.setBounds(461, 256, 89, 23);
		contentPane.add(btn_forward);
		
		  try {
          	SignInView.dos.writeUTF("LIST_MAIL_SENT");
				SignInView.dos.writeUTF(userName);
				String rep = SignInView.dis.readUTF();
				showdataListMailSent(rep);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("ko the ket noi den sever");
			}
	}
	
	public void showdataListMailSent(String rep) {
		try {
			if(rep.equals("LIST_MAIL_OK")) {
				ObjectInputStream ois = new ObjectInputStream(SignInView.soc.getInputStream());
		        List<email> message_data = (List<email>) ois.readObject();
		        DefaultTableModel tableModel = (DefaultTableModel) table_mail.getModel();
		        tableModel.setRowCount(0);
		        if (!message_data.isEmpty()) {
		            for (email email : message_data) {
		                tableModel.addRow(new Object[] {
		                    email.getSubject(), email.getSender(), email.getSendDate()
		                });
		            }
		        } else{
		            tableModel.addRow(new Object[] {
		                "Không tìm thấy email", "", ""
		            });
		        }
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("khong the ket noi server");
		}
	}

}
