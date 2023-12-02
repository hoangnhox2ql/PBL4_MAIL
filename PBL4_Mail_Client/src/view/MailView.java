package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.MyFile;

import java.io.FileOutputStream;
import java.awt.Toolkit;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;

public class MailView extends JFrame {

	private JPanel contentPane;
	private JTextField text_sender;
	private JTextField text_subject;
	private JTextField text_date_time;
	
	static ArrayList<MyFile> myFiles = new ArrayList<>();

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
		text_body.setEditable(false);
		text_body.setBounds(10, 142, 422, 207);
		contentPane.add(text_body);
		
		text_sender = new JTextField();
		text_sender.setEditable(false);
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
		text_subject.setEditable(false);
		text_subject.setBounds(109, 44, 234, 20);
		contentPane.add(text_subject);
		text_subject.setColumns(10);
		
		JLabel lblSubject = new JLabel("SUBJECT :");
		lblSubject.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblSubject.setBounds(10, 46, 70, 14);
		contentPane.add(lblSubject);
		
		text_date_time = new JTextField();
		text_date_time.setEditable(false);
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
		    	String nameFile = cbb_file.getSelectedItem().toString();
		    	getFile(nameFile);
		    	try {
					String mess = SignInView.dis.readUTF();
					if(mess.equals("DOWNLOAD_GO")) {
						
						int fileId =0;
						//Your file handling code here
						int fileNameLength = SignInView.dis.readInt();

						if (fileNameLength > 0) {
						    byte[] fileNameBytes = new byte[fileNameLength];
						    SignInView.dis.readFully(fileNameBytes, 0, fileNameBytes.length);

						    String fileName = new String(fileNameBytes);
						    int fileContentLength = SignInView.dis.readInt();

						    if (fileContentLength > 0) {
						        byte[] fileContentBytes = new byte[fileContentLength];
						        SignInView.dis.readFully(fileContentBytes, 0, fileContentBytes.length);

						        JPanel jpFileRow = new JPanel();
						        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));

						        JLabel jlFileName = new JLabel(fileName);
						        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
						        jlFileName.setBorder(new EmptyBorder(10, 0, 0, 0));

						        if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
						            jpFileRow.setName(String.valueOf(fileId));
						            jpFileRow.add(jlFileName);
						        } else {
						            jpFileRow.setName(String.valueOf(fileId));
						            jpFileRow.add(jlFileName);
						        }
						        myFiles.add(new model.MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName)));
						        fileId++;

						        // Tự động tải file mới gửi đến
						        // Open a directory chooser dialog for selecting the directory
						        JFileChooser fileChooser = new JFileChooser();
						        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						        int result = fileChooser.showSaveDialog(null);

						        // If a directory is selected, return the directory path
						        if (result == JFileChooser.APPROVE_OPTION) {
						            String directoryPath = fileChooser.getSelectedFile().getPath();
						            downloadFile(fileName, fileContentBytes,directoryPath);
						        }
						        
						    }
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
	public void showMailBody(JTextArea text_body, String sender, String subject, LocalDateTime date, JComboBox<String> cbb_file) {
	    try {
	        SignInView.dos.writeUTF("GET_BODY");
	        SignInView.dos.writeUTF(sender);
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


	public void getFile(String nameFile) {
		try {
			SignInView.dos.writeUTF("DOWNLOAD");
			SignInView.dos.writeUTF(nameFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');

        if (i > 0) {
            return fileName.substring(i + 1);
        } else {
            return "No extension found";
        }
    }
	
	public static void downloadFile(String fileName, byte[] fileData, String savePath) {
	    File fileToDownload = new File(savePath, fileName);

	    try (FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload)) {
	        fileOutputStream.write(fileData);
	        fileOutputStream.close();

	        System.out.println("File downloaded successfully to " + savePath);
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
	
}




