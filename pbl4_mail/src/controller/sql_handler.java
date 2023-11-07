package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.account;
import model.message;
import model.message;

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
		  String query ="insert into account(id,user_name,password,phone) values(?,?,?,?)"; 
		  try { 
			  Connection cnn = getConnection(); 
			  PreparedStatement pstm = cnn.prepareStatement(query);
			  pstm.setString(1,newAccount.getId()); 
			  pstm.setString(2,newAccount.getUser_name());
			  pstm.setString(3,newAccount.getPassword());
			  pstm.setString(4,newAccount.getPhone());
			  pstm.executeUpdate(); 
			  }catch(Exception e) { 
				  e.printStackTrace(); 
			  } 
	  }
	
	//lấy email dựa trên tên tài khoản
			public static List<message> findAll(String username){
				List<message> messageList = new ArrayList<message>();
				String query ="SELECT subject,sender,send_date from email where receiver = '"+username+"' ";
				try {
					Connection cnn = getConnection();
					Statement stm = cnn.createStatement();
					ResultSet re = stm.executeQuery(query);
					while(re.next()) {
						message mess = new message(re.getString("subject"),re.getString("sender"),re.getTimestamp("send_date").toLocalDateTime());
						messageList.add(mess);
					}
				}catch(Exception e) {
					
				}
				return messageList;
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
		public static void changePass(String pass,String phone) {
			String query="update account set pass=? where phone = '"+phone+"' ";
				 try {
					 Connection cnn = getConnection();
					 PreparedStatement pstm = cnn.prepareStatement(query);
					 pstm.setString(1, pass);
					 pstm.executeUpdate();
					 cnn.close();
			  	  }catch(Exception e) {}
		}
		
	
}
