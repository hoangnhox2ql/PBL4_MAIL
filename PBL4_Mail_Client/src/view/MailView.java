package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.io.FileOutputStream;
import java.awt.Toolkit;
import java.time.LocalDateTime;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;

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
		text_body.setBounds(10, 142, 422, 207);
		contentPane.add(text_body);
		
		text_sender = new JTextField();
		text_sender.setBounds(109, 12, 234, 20);
		contentPane.add(text_sender);
		text_sender.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("FROM :");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 14, 55, 14);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Cancel");
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
		
		JComboBox<String> cbb_file = new JComboBox<String>();
		cbb_file.setModel(new DefaultComboBoxModel<String>(new String[] {"All"}));
		cbb_file.setBounds(10, 109, 422, 22);
		contentPane.add(cbb_file);
		
		JButton btn_download = new JButton("Download");
		btn_download.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Open a file chooser dialog for saving the file
		        JFileChooser fileChooser = new JFileChooser();
		        int result = fileChooser.showSaveDialog(null);
		        // If a location is selected, initiate the file download
		        if (result == JFileChooser.APPROVE_OPTION) {
		            // Chọn file để lấy đường dẫn 
		            File selectedFile = fileChooser.getSelectedFile();
		            String localPath = selectedFile.getAbsolutePath();
		            // Call the getFile function to download and save the file
		            getFile(sender, subject, date, cbb_file, localPath);
		        }
		    }
		});
		btn_download.setBackground(new Color(255, 255, 255));
		btn_download.setBounds(442, 109, 89, 23);
		contentPane.add(btn_download);
		
		text_sender.setText(sender);
		text_subject.setText(subject);
		text_date_time.setText(date.toString());
		showMailBody(text_body,sender,subject,date,cbb_file);
		
	}
	public void showMailBody(JTextArea text_body ,String sender,String subject,LocalDateTime date,JComboBox<String> cbb_file) {
		try {
			SignInView.dos.writeUTF("GET_BODY");
			SignInView.dos.writeUTF(sender);
			SignInView.dos.writeUTF(subject);
			SignInView.dos.writeUTF(date.toString());
			String body = SignInView.dis.readUTF();
			text_body.setText(body);
			String nameFile = SignInView.dis.readUTF();
			cbb_file.addItem(nameFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public File getFile(String sender, String subject, LocalDateTime date, JComboBox<String> cbb_file, String localPath) {
	    try {
	        // Gửi yêu cầu lấy nội dung tệp tin đến server
	        SignInView.dos.writeUTF("GET_FILE");
	        SignInView.dos.writeUTF(sender);
	        SignInView.dos.writeUTF(subject);
	        SignInView.dos.writeUTF(date.toString());
	        // Lấy tên tệp tin được chọn từ JComboBox và gửi đi
	        String selectedFile = (String) cbb_file.getSelectedItem();
	        SignInView.dos.writeUTF(selectedFile);

	        // Đọc kích thước của buffer từ server
	        int bufferSize = SignInView.dis.readInt();

	        // Khởi tạo mảng byte để chứa dữ liệu từ server
	        byte[] buffer = new byte[bufferSize];

	        // Đọc dữ liệu từ server vào buffer
	        SignInView.dis.readFully(buffer);

	        // Lưu dữ liệu vào tệp tin cục bộ
	        File file = new File(localPath);
	        try (FileOutputStream fos = new FileOutputStream(file)) {
	            fos.write(buffer);
	        }

	        // Trả về đối tượng File đã tạo
	        return file;

	    } catch (Exception e) {
	        e.printStackTrace(); // Xử lý ngoại lệ tùy thuộc vào yêu cầu của bạn
	        return null;
	    }
	}

}
