package PBL4_Mail_Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.account;
import model.email;
import model.MyFile;
import controller.sql_handler;
import java.io.ObjectOutputStream;
import java.awt.Font;
import java.io.ByteArrayOutputStream;



public class Server {
	
	
	public static void main(String []args)
	{	
		new Server();
	}
	Vector<EmailProcessing> Clientlist = new Vector<EmailProcessing>();
	public Server() {
		try {                     
			ServerSocket server = new ServerSocket(5555);
			while (true) {
				Socket soc = server.accept();
				EmailProcessing t = new EmailProcessing(soc,this);
				Clientlist.add(t);
				t.start();
			}
		} catch(Exception e) {
			
		}
	}
}

class EmailProcessing extends Thread {
	Socket soc;
	Server server;
	
	static ArrayList<MyFile> myFiles = new ArrayList<>();
	final List<File> listFilesToSend = new ArrayList<>();
	
	public EmailProcessing(Socket soc,Server server) {
		this.soc = soc;
		this.server = server;
	}
	
	private void sendFileToServer(File fileToSend) {
        try{FileInputStream fileInputStream = new FileInputStream(fileToSend.getAbsolutePath());
             DataOutputStream dataOutputStream = new DataOutputStream(soc.getOutputStream());
            String fileName = fileToSend.getName();
            byte[] fileNameBytes = fileName.getBytes();
            byte[] fileContentBytes = new byte[(int) fileToSend.length()];
            fileInputStream.read(fileContentBytes);

            // Send file information
            dataOutputStream.writeInt(fileNameBytes.length);
            dataOutputStream.write(fileNameBytes);
            dataOutputStream.writeInt(fileContentBytes.length);
            dataOutputStream.write(fileContentBytes);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
	
    public static void downloadFile( String fileName, byte[] fileData) {
        File fileToDownload = new File(fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload)) {
            fileOutputStream.write(fileData);

            System.out.println("File downloaded successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
	
	public void run() {
		while (true) {
			try {
				
				DataInputStream dis = new DataInputStream(soc.getInputStream());
				DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
				String mess = dis.readUTF();
				if(mess.equals("SIGN_IN")) {
					String username = dis.readUTF();
					String password = dis.readUTF();
					if(sql_handler.isValidLogin(username, password)) {
						dos.writeUTF("SIGN_IN_OK");
					}else {
						dos.writeUTF("SIGN_IN_NO_OK");
					}
				}
				
				if(mess.equals("SIGN_UP")) {
					String usernamee = dis.readUTF();
					String passwordd = dis.readUTF();
					String phonee = dis.readUTF();
					account acc = new account();
					acc.setUser_name(usernamee);
					acc.setPassword(passwordd);
					acc.setPhone(phonee);
					if(usernamee != null && passwordd != null && phonee != null) {
						if(sql_handler.isValidUsername(usernamee) || sql_handler.isValidPhone(phonee)) {
							dos.writeUTF("SAME_USERNAME_PHONE");
						}else {
							sql_handler.insert(acc);
							dos.writeUTF("SIGN_UP_OK");
						}
						
					}
				}
				
				if(mess.equals("FORGOT_PASSWORD")) {
					String phone = dis.readUTF();
					if(sql_handler.isValidPhone(phone)) {
						dos.writeUTF("FORGOT_PASSWORD_OK");
					}
					else {
						dos.writeUTF("FORGOT_PASSWORD_NO_OK");
					}
				}
				
				if(mess.equals("CHANGE_PASSWORD")) {
					String newpass = dis.readUTF();
			        String phone = dis.readUTF();
			        if (newpass != null && !newpass.isEmpty() && phone != null && !phone.isEmpty()) {
			            boolean success = sql_handler.changePass(newpass, phone);
			            if (success) {
			                dos.writeUTF("CHANGE_PASSWORD_OK");
			            } else {
			                dos.writeUTF("CHANGE_PASSWORD_NO_OK");
			            }
			        }
				}
				
				if(mess.equals("ONLINE")) {
					String username = dis.readUTF();
					List<email> users = sql_handler.findAll(username);
					ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
				    oos.writeObject(users);
				    oos.flush();
				}
				
				if(mess.equals("FORWARD_MAIL")) {
					boolean success = false;
				    String username = dis.readUTF();
				    String receiver = dis.readUTF();
				    String subject = dis.readUTF();
				    String body = dis.readUTF();
				    String redFlagString = dis.readUTF();
				    boolean redflag = false;
				    List<String> nameFile = new ArrayList<String>();
				    boolean check = sql_handler.isValidUsername(receiver);
				    if (redFlagString.equalsIgnoreCase("yes")) {
				        redflag = true;
				        int numFiles = Integer.parseInt(dis.readUTF());
				        for (int i = 0; i < numFiles; i++) {
				            nameFile.add(dis.readUTF());
				        }
				    }
				    // Send the email if the receiver is valid
				    if (check) {
				            // Send the email with red flag and file names
				            if (redflag) {
				            	success = sql_handler.insertMailWithFiles(username, receiver, subject, body, nameFile);
				            } else {
				                success = sql_handler.insertMail(username, receiver, subject, body);
				            }
				            if (success) {
						        dos.writeUTF("MAIL_SENT_SUCCESS");
						    } else {
						        dos.writeUTF("MAIL_SENT_FAILURE");
						    }
				    } else {
				    	dos.writeUTF("MAIL_SENT_FAILURE");
				    }
				}
				

				    
				
				if (mess.equals("SEND_MAIL")) {

					
				    int fileId = 0;
				    boolean redFlag = false; // Flag to indicate whether there is a file or not

				    String sender = dis.readUTF();
				    String receiver = dis.readUTF();
				    String subject = dis.readUTF();
				    String body = dis.readUTF();
				    
				    // Read the redFlag value from the stream
				    String redFlagString = dis.readUTF();
				    List<String> nameFile = new ArrayList<String>() ;
				    // Check the value of redFlagString
				    if (redFlagString.equalsIgnoreCase("yes")) {
				        redFlag = true; // Set the flag to true if there is a file
				        String rep= dis.readUTF();
				        int i = Integer.parseInt(rep);
				        for(int j=0; j<i;j++) {
				        	int fileNameLength = dis.readInt();

					        if (fileNameLength > 0) {
					            byte[] fileNameBytes = new byte[fileNameLength];
					            dis.readFully(fileNameBytes, 0, fileNameBytes.length);

					            String fileName = new String(fileNameBytes);
					            int fileContentLength = dis.readInt();

					            if (fileContentLength > 0) {
					                byte[] fileContentBytes = new byte[fileContentLength];
					                dis.readFully(fileContentBytes, 0, fileContentBytes.length);
					                myFiles.add(new model.MyFile(fileId, fileName, fileContentBytes));
					                fileId++;
					                // Tự động tải file mới gửi đến
					                downloadFile(fileName, fileContentBytes);
					                nameFile.add(fileName);
					                
					             // Call insertMail based on the value of redFlag
					                
					            }
					        }
				        }
				        boolean check = sql_handler.isValidUsername(receiver);
				        if (check) {
				            boolean success1 = sql_handler.insertMailWithFiles(sender, receiver, subject, body, nameFile);
				            if (success1 ) {
				                dos.writeUTF("MAIL_SENT_SUCCESS");
				            } else {
				                dos.writeUTF("MAIL_SENT_FAILURE");
				            }
				        } else {
				        		dos.writeUTF("MAIL_SENT_FAILURE");
				        }
				    }
				    if (redFlagString.equalsIgnoreCase("no")) {
				    	boolean check = sql_handler.isValidUsername(receiver);
				        if (check) {
				            boolean success1 = sql_handler.insertMail(sender, receiver, subject, body);
				            if (success1 ) {
				                dos.writeUTF("MAIL_SENT_SUCCESS");
				            } else {
				                dos.writeUTF("MAIL_SENT_FAILURE");
				            }
				        } else {
				        	dos.writeUTF("MAIL_SENT_FAILURE");
				        }
				    }
				    
				}


				if (mess.equals("DELETE")) {
				    String sender = dis.readUTF();
				    String subject = dis.readUTF();
				    String date = dis.readUTF();

				    if (sender != null && !sender.isEmpty() && subject != null && !subject.isEmpty() && date != null && !date.isEmpty()) {
				        boolean success = sql_handler.deleteMail(sender, subject, date);
				        if (success) {
				            dos.writeUTF("DELETE_OK");
				        }
				    }
				}

				if(mess.equals("SEARCH")) {
				    String selectedItem = dis.readUTF();
				    String textSearch = dis.readUTF();
				    String username = dis.readUTF();

				    List<email> mails = null;
				    if (selectedItem != null && !selectedItem.isEmpty()) {
				        if (selectedItem.equals("All")) {
				            mails = sql_handler.findAll(username);
				            dos.writeUTF("SEARCH_OK");
				            ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
					        oos.writeObject(mails);
					        oos.flush();
				        } else if (selectedItem.equals("Sender")) {
				            mails = sql_handler.findSender(textSearch,username);
				            dos.writeUTF("SEARCH_OK");
				            ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
					        oos.writeObject(mails);
					        oos.flush();
				        } else if (selectedItem.equals("Subject")) {
				            mails = sql_handler.findSubject(textSearch, username);
				            dos.writeUTF("SEARCH_OK");
				            ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
					        oos.writeObject(mails);
					        oos.flush();
				        }
				    }
				}
				
				if (mess.equals("GET_BODY")) {
				    String sender = dis.readUTF();
				    String subject = dis.readUTF();
				    String date = dis.readUTF();
				    String body = sql_handler.getMailBody(sender, subject, date);
				    int id_mail = sql_handler.getIdMail(sender, subject, date);
				    List<String> nameFiles = sql_handler.getNameFiles(id_mail);

				    dos.writeUTF(body);

				    if (nameFiles != null) {
				        dos.writeInt(nameFiles.size());

				        for (String fileName : nameFiles) {
				            dos.writeUTF(fileName);
				        }
				    } else {
				        dos.writeInt(0);
				    }
				}

				
				if(mess.equals("LIST_MAIL_SENT")) {
					String username = dis.readUTF();
					List<email> mails = null;
					if (username != null && !username.isEmpty()) {
						mails = sql_handler.findListSent(username);
						dos.writeUTF("LIST_MAIL_OK");
			            ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
				        oos.writeObject(mails);
				        oos.flush();
					}
				}
				
				if (mess.equals("GET_SENT")) {
				    String receiver = dis.readUTF();
				    String subject = dis.readUTF();
				    String date = dis.readUTF();
				    String body = sql_handler.getMailSent(receiver, subject, date);
				    int id_mail = sql_handler.getIdMailForReceiver(receiver, subject, date);
				    List<String> nameFiles = sql_handler.getNameFiles(id_mail);
				    dos.writeUTF(body);

				    if (nameFiles != null) {
				        dos.writeInt(nameFiles.size());
				        System.out.println("5");
				        for (String fileName : nameFiles) {
				            dos.writeUTF(fileName);
				            System.out.println(fileName);
				        }
				    } else {
				        dos.writeInt(0);
				    }
				}
				
				if(mess.equals("DOWNLOAD")) {
					String nameFile = dis.readUTF();
					File fileToSend = new File("C:\\Users\\ASUS\\Documents\\Java\\PBL4_Mail_Server\\" + nameFile);
					listFilesToSend.add(fileToSend);
					dos.writeUTF("DOWNLOAD_GO");     
					sendFileToServer(fileToSend);
//					for (File file : listFilesToSend) {
//                        sendFileToServer(file);
//                    }
				}
				
				
			} catch (Exception e) {
				
			}
			
		}
	}
}