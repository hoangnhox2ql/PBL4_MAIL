package controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import model.account;
import model.email;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class sql_handler {
	static String serverName = "LAPTOP-0DP7SHTA\\SQLEXPRESS";
	static String db = "pbl4_mail";
	static String url = "jdbc:sqlserver://" + serverName + ":1433;databaseName =" + db + ";encrypt=true;trustServerCertificate=true;";
	static String user = "sa";
	static String pass = "123456";

	public static Connection getConnection() {// connection function
		Connection cnn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			cnn = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnn;	
	}
	
	//thêm tài khoản
	public static void insert(account newAccount) { 
		  String query ="insert into account(user_name,password,phone) values(?,?,?)"; 
		  try { 
			  Connection cnn = getConnection(); 
			  PreparedStatement pstm = cnn.prepareStatement(query);
			  pstm.setString(1,newAccount.getUser_name());
			  pstm.setString(2,newAccount.getPassword());
			  pstm.setString(3,newAccount.getPhone());
			  pstm.executeUpdate(); 
			  }catch(Exception e) { 
				  e.printStackTrace(); 
			  } 
	  }
	
	//lấy email dựa trên tên tài khoản
			public static List<email> findAll(String username){
				List<email> emailList = new ArrayList<email>();
				String query ="SELECT subject,sender,send_date from email where receiver = '"+username+"' ";
				try {
					Connection cnn = getConnection();
					Statement stm = cnn.createStatement();
					ResultSet re = stm.executeQuery(query);
					while(re.next()) {
						email mess = new email(re.getString("subject"),re.getString("sender"),re.getTimestamp("send_date").toLocalDateTime());
						emailList.add(mess);
					}
				}catch(Exception e) {
					
				}
				return emailList;
			}
			
	//lấy email dựa trên sender		
			public static List<email> findSender(String sender , String username){
			    List<email> emailList = new ArrayList<email>();
			    String query ="SELECT subject,sender,send_date from email where receiver = '"+username+"' and sender LIKE '%"+sender+"%'";
			    try {
			        Connection cnn = getConnection();
			        Statement stm = cnn.createStatement();
			        ResultSet re = stm.executeQuery(query);
			        while(re.next()) {
			            email mess = new email(re.getString("subject"),re.getString("sender"),re.getTimestamp("send_date").toLocalDateTime());
			            emailList.add(mess);
			        }
			    }catch(Exception e) {
			        // handle exception
			    }
			    return emailList;
			}
			
	//lấy email dựa trên subject		
			public static List<email> findSubject(String subject, String username) {
			    List<email> emailList = new ArrayList<email>();
			    String query = "SELECT subject,sender,send_date from email where receiver = '" + username + "' and subject LIKE '%" + subject + "%'";
			    try {
			        Connection cnn = getConnection();
			        Statement stm = cnn.createStatement();
			        ResultSet re = stm.executeQuery(query);
			        while (re.next()) {
			            email mess = new email(re.getString("subject"), re.getString("sender"), re.getTimestamp("send_date").toLocalDateTime());
			            emailList.add(mess);
			        }
			    } catch (Exception e) {
			        // handle exception
			    }
			    return emailList;
			}
			
	//đăng nhập
		public static boolean isValidLogin(String username, String pass) {
			boolean isValidLogin = false;
		    try {
		        Connection cnn = getConnection();
		        String sql = "SELECT * FROM account WHERE user_name = ? AND password = ?";
		        PreparedStatement stm = cnn.prepareStatement(sql);
		        stm.setString(1, username);
		        stm.setString(2, pass);
		        ResultSet resultSet = stm.executeQuery();
		        if (resultSet.next()) {
		            isValidLogin = true;
		        }
		        cnn.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return isValidLogin;
		}
		
	//check số điện thoại 
		public static boolean isValidPhone(String phone) {
			boolean isValidPhone = false;
			try {
				Connection cnn = getConnection();
		        String sql = "SELECT user_name,password FROM account WHERE phone = ?";
		        PreparedStatement stm = cnn.prepareStatement(sql);
		        stm.setString(1, phone);
		        ResultSet resultSet = stm.executeQuery();
		        if (resultSet.next()) {
		        	isValidPhone = true;
		        }
		        cnn.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return isValidPhone;
		}
	
	//cập nhật mật khẩu
		public static boolean changePass(String pass, String phone) {
		    boolean changePass = false;
		    try {
		        Connection cnn = getConnection();
		        String query = "update account set password=? where phone = ?";
		        try (PreparedStatement pstm = cnn.prepareStatement(query)) {
		            pstm.setString(1, pass);
		            pstm.setString(2, phone);         
		            int rowsAffected = pstm.executeUpdate();
		            changePass = rowsAffected > 0;
		        }
		        cnn.close();
		    } catch (Exception e) {
		        e.printStackTrace();
		        // Xử lý lỗi (có thể ghi log, thông báo, hoặc thực hiện các hành động phù hợp)
		    }
		    return changePass;
		}

		//xóa tên dựa trên tiêu đề 
		public static boolean deleteMail(String sender,String subject,String date) {
		    try {
		        String query = "delete FROM email WHERE sender = ? AND subject = ? AND send_date = '"+date+"' ";

		        Connection cnn = getConnection();
		        PreparedStatement pstm = cnn.prepareStatement(query);
		        pstm.setString(1, sender);
	            pstm.setString(2, subject);
		        int rowsAffected = pstm.executeUpdate();
		        return rowsAffected > 0;
		    } catch (Exception e) {
		        return false;
		    }
		}
		
		//check tên
		public static boolean isValidUsername(String username) {
			boolean isValidLogin = false;
		    try {
		        Connection cnn = getConnection();
		        String sql = "SELECT * FROM account WHERE user_name = ?";
		        PreparedStatement stm = cnn.prepareStatement(sql);
		        stm.setString(1, username);
		        ResultSet resultSet = stm.executeQuery();
		        if (resultSet.next()) {
		            isValidLogin = true;
		        }
		        cnn.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return isValidLogin;
		}

		//Trả về phần thân thư 
		public static String getMailBody(String sender, String subject, String date) {
		    try {
		        String query = "SELECT body FROM email WHERE sender = ? AND subject = ? AND send_date = '"+date+"' ";
		        try (Connection cnn = getConnection();
		            PreparedStatement pstm = cnn.prepareStatement(query)) {
		            pstm.setString(1, sender);
		            pstm.setString(2, subject);
		            ResultSet rs = pstm.executeQuery();
		            if (rs.next()) {
		                String body = rs.getString(1);
		                return body;
		            } else {
		                return null;
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return null;
		    }
		}
		
		//trả về phần thư đã gửi
		public static String getMailSent(String receiver, String subject, String date) {
		    try {
		        String query = "SELECT body FROM email WHERE receiver = ? AND subject = ? AND send_date = '"+date+"' ";
		        try (Connection cnn = getConnection();
		            PreparedStatement pstm = cnn.prepareStatement(query)) {
		            pstm.setString(1, receiver);
		            pstm.setString(2, subject);
		            ResultSet rs = pstm.executeQuery();
		            if (rs.next()) {
		                String body = rs.getString(1);
		                return body;
		            } else {
		                return null;
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return null;
		    }
		}
		
		//Lấy tên của file
				public static String getNameFileSent(String receiver, String subject, String date) {
					// TODO Auto-generated method stub
					try {
				        String query = "SELECT file_name FROM email WHERE receiver = ? AND subject = ? AND send_date = '"+date+"' ";
				        try (Connection cnn = getConnection();
				            PreparedStatement pstm = cnn.prepareStatement(query)) {
				            pstm.setString(1, receiver);
				            pstm.setString(2, subject);
				            ResultSet rs = pstm.executeQuery();
				            if (rs.next()) {
				                String nameFile = rs.getString(1);
				                return nameFile;
				            } else {
				                return null;
				            }
				        }
					} catch (SQLException e) {
						e.printStackTrace();
						return null;
					}
				}

		//Trả về list Thư mà người dùng đã gửi 
		public static List<email> findListSent(String username) {
			List<email> emailList = new ArrayList<email>();
		    String query = "SELECT subject,receiver,send_date from email where sender = '" + username + "'";
		    try {
		        Connection cnn = getConnection();
		        Statement stm = cnn.createStatement();
		        ResultSet re = stm.executeQuery(query);
		        while (re.next()) {
		            email mess = new email(re.getString("subject"), re.getString("receiver"), re.getTimestamp("send_date").toLocalDateTime());
		            emailList.add(mess);
		        }
		    } catch (Exception e) {
		        // handle exception
		    }
		    return emailList;
		}

		public static List<String> getNameFiles(String id_mail) {
		    List<String> nameFiles = new ArrayList<>();

		    try {
		        String query = "SELECT file_name FROM [file] WHERE id_mail = ?";
		        try (Connection cnn = getConnection();
		             PreparedStatement pstm = cnn.prepareStatement(query)) {

		            pstm.setString(1, id_mail);
		            ResultSet rs = pstm.executeQuery();

		            while (rs.next()) {
		                String nameFile = rs.getString("file_name");
		                nameFiles.add(nameFile);
		            }

		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return nameFiles.isEmpty() ? null : nameFiles;
		}


		
		public static boolean insertMail(String sender, String receiver, String subject, String body) {
	        boolean success = false;
	        try {
	            Connection connection = getConnection();
	            String query = "  INSERT INTO email (sender, receiver, subject,body, send_date) VALUES (?,?,?,?,GETDATE());";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	                preparedStatement.setString(1, sender);
	                preparedStatement.setString(2, receiver);
	                preparedStatement.setString(3, subject);
	                preparedStatement.setString(4, body);
	                
	                int rowsAffected = preparedStatement.executeUpdate();
	                success = rowsAffected > 0;
	            }
	            connection.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return success;
	    }

		 public static boolean insertMailWithFiles(String sender, String receiver, String subject, String body, List<String> fileNames) {
		        boolean success = false;

		        try {
		            Connection connection = getConnection();
		            connection.setAutoCommit(false);  // Bắt đầu giao dịch

		            // Chèn dữ liệu vào bảng Mail
		            String mailQuery = "INSERT INTO email (sender, receiver, subject, body, send_date) VALUES (?, ?, ?, ?, GETDATE());";

		            try (PreparedStatement mailStatement = connection.prepareStatement(mailQuery, Statement.RETURN_GENERATED_KEYS)) {
		                mailStatement.setString(1, sender);
		                mailStatement.setString(2, receiver);
		                mailStatement.setString(3, subject);
		                mailStatement.setString(4, body);

		                int rowsAffected = mailStatement.executeUpdate();

		                if (rowsAffected > 0) {
		                    // Lấy ID của email vừa chèn vào bảng Mail
		                    ResultSet generatedKeys = mailStatement.getGeneratedKeys();

		                    if (generatedKeys.next()) {
		                        int lastMailId = generatedKeys.getInt(1);

		                        // Chèn dữ liệu vào bảng File sử dụng INNER JOIN
		                        String fileQuery = "INSERT INTO [file] (id_mail, file_name) VALUES (?, ?);";

		                        try (PreparedStatement fileStatement = connection.prepareStatement(fileQuery)) {
		                            // Sử dụng vòng lặp để chèn từng tên file
		                            for (String fileName : fileNames) {
		                                fileStatement.setInt(1, lastMailId);
		                                fileStatement.setString(2, fileName);
		                                fileStatement.addBatch(); // Thêm lệnh vào batch
		                            }

		                            // Thực hiện tất cả các lệnh trong batch
		                            int[] fileRowsAffected = fileStatement.executeBatch();

		                            // Kiểm tra xem có ít nhất một lệnh nào đó đã được thực hiện thành công hay không
		                            for (int rows : fileRowsAffected) {
		                                if (rows > 0) {
		                                    success = true;
		                                    break;
		                                }
		                            }
		                        }
		                    }
		                }
		            }

		            // Kết thúc giao dịch
		            if (success) {
		                connection.commit();
		            } else {
		                connection.rollback();
		            }

		            connection.setAutoCommit(true);  // Đặt lại trạng thái mặc định
		            connection.close();

		        } catch (Exception e) {
		            e.printStackTrace();
		        }

		        return success;
		    }

		public static int getIdMail(String sender, String subject, String date) {
			// TODO Auto-generated method stub
			try {
		        String query = "SELECT id_mail FROM email WHERE sender = ? AND subject = ? AND send_date = '"+date+"' ";
		        try (Connection cnn = getConnection();
		            PreparedStatement pstm = cnn.prepareStatement(query)) {
		            pstm.setString(1, sender);
		            pstm.setString(2, subject);
		            ResultSet rs = pstm.executeQuery();
		            if (rs.next()) {
		                int id_mail = rs.getInt(1);
		                return id_mail;
		            } else {
		                return (Integer) null;
		            }
		        }
			} catch (SQLException e) {
				e.printStackTrace();
				return (Integer) null;
			}
		}

		public static boolean deleteMailFile(int id_mail) {
			try {
		        String query = "delete FROM [file] WHERE id_mail = ? ";

		        Connection cnn = getConnection();
		        PreparedStatement pstm = cnn.prepareStatement(query);
		        pstm.setInt(1, id_mail);
		        int rowsAffected = pstm.executeUpdate();
		        return rowsAffected > 0;
		    } catch (Exception e) {
		        return false;
		    }
		}



}
