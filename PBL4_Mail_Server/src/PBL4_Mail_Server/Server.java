package PBL4_Mail_Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import model.account;
import model.email;
import controller.sql_handler;
import java.io.ObjectOutputStream;
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
	public EmailProcessing(Socket soc,Server server) {
		this.soc = soc;
		this.server = server;
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
						if(sql_handler.isValidUsername(usernamee) && sql_handler.isValidPhone(phonee)) {
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
				
//				if(mess.equals("SEND_MAIL")) {
//					String sender = dis.readUTF();
//				    String receiver = dis.readUTF();
//				    String subject = dis.readUTF();
//				    String body = dis.readUTF();
//				    boolean check = sql_handler.isValidUsername(receiver);
//				    if(check) {
//				    	if (receiver != null && !receiver.isEmpty() && subject != null && !subject.isEmpty()) {
//					        boolean success = sql_handler.insertMail(sender,receiver, subject, body);
//					        if (success) {
//					            dos.writeUTF("MAIL_SENT_SUCCESS");
//					        } 
//						}
//				    }else {
//			            dos.writeUTF("MAIL_SENT_FAILURE");
//			        }
//				    
//				}
				
				if (mess.equals("SEND_MAIL")) {
				        String sender = dis.readUTF();
				        String receiver = dis.readUTF();
				        String subject = dis.readUTF();
				        String body = dis.readUTF();

				        // Check if the client is attaching a file
				        String attachFileFlag = dis.readUTF();
				        System.out.println(attachFileFlag);
				        String fileName = null;
				        byte[] buffer = new byte[4 * 1024];

				        if (attachFileFlag.equals("YES")) {
				        	System.out.println("chạy đoạn này");
				            try {
				                fileName = dis.readUTF();
				                System.out.println(fileName);
				                int bytes = 0;
				        		FileOutputStream fileOutputStream = new FileOutputStream(fileName);
				        		long size = dis.readLong(); // read file size
				        		
				        		while (size > 0 && (bytes = dis.read(buffer, 0,(int)Math.min(buffer.length, size)))!= -1) {
				        			// Here we write the file using write method
				        			fileOutputStream.write(buffer, 0, bytes);
				        			size -= bytes; // read upto file size
				        		}
				        		
				        		System.out.println(buffer);
				        		
				            } catch (IOException e) {
				                System.out.println("không thể gửi được file");
				            }
				        }

				        boolean check = sql_handler.isValidUsername(receiver);
				        if (check) {
				            if (receiver != null && !receiver.isEmpty() && subject != null && !subject.isEmpty()) {
				                boolean success = sql_handler.insertMail(sender, receiver, subject, body, fileName, buffer);
				                if (success) {
				                    dos.writeUTF("MAIL_SENT_SUCCESS");
				                } else {
				                    dos.writeUTF("MAIL_SENT_FAILURE");
				                }
				            }
				        } else {
				            dos.writeUTF("MAIL_SENT_FAILURE");
				        }
				    
				}
				if(mess.equals("DELETE")) {
					String selectedRow = dis.readUTF();
					if (selectedRow != null && !selectedRow.isEmpty() ) {
						boolean success = sql_handler.deleteMailSubject(selectedRow);
						if(success) {
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
				            mails = sql_handler.findSender(textSearch, username);
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
				if(mess.equals("GET_BODY")) {
					String sender = dis.readUTF();
					String subject = dis.readUTF();
					String date = dis.readUTF();
					String body = sql_handler.getMailBody(sender, subject, date);
					dos.writeUTF(body);
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
				
				
			} catch (Exception e) {
				
			}
		}
	}
}