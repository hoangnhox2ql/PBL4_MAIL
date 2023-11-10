package PBL4_Mail_Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Scanner;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Server {
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
	public static void main(String []args)
	{
		String receiver;
		String body;
		String subject;
		boolean chack =false;
		try {
			
			ServerSocket server = new ServerSocket(5555);
			Connection con = getConnection();
			while(true)
			{
				Socket soc = server.accept();
				DataInputStream dis = new DataInputStream(soc.getInputStream());
				DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
				receiver = dis.readUTF();
				body = dis.readUTF();
				subject = dis.readUTF();
				chack = isValidUsername(receiver);
				if(!chack) {
					dos.writeUTF("email khong ton tai!!!");
				}
				else {
					
				}
			}
			
		}catch(Exception e)
		{
			
		}
		
	}
	
}